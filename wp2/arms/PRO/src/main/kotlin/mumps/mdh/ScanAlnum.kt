package mumps.mdh

import mumps.interp.Msv
import mumps.interp.NUL
import mumps.interp.strlenC

/**
 * PRO-arm translation of libmpscpp.cpp::ScanAlnum (B3 unit 79, rank
 * 127). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 10112-10139 (the third of the three vendor overloads).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::ScanAlnum, cpp, loc 17, comment_ratio
 * 0.056, pointer_marks 2, control_marks 5, score 13.1, batch 3 -
 * exact match to wp1/study-subset/units.csv row 127. The overload
 * selection disambiguates against the two sibling ScanAlnum
 * overloads (FILE-ptr/istream tokenizers, loc 18 each - different
 * corpus rows).
 *
 * char* ScanAlnum(MSV* svptr, char* ep, char* toklist = NULL):
 * scans the next token via Scan(svptr), repeats until it starts
 * with an alphanumeric, lowercases the alphanumerics into the
 * static tmp buffer and returns it (tpx=1; tpx=0 with an empty
 * tmp1 on end-of-input).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch    : vendor "static char tmp[1024],
 *    tmp1[1024]" persist across calls - carried on the
 *    ScanAlnumScratch object.
 *  - h012 unsigned_byte_domain: tmp/tmp1 byte domains.
 *  - h013 dead_store        : the parameters ep and toklist (with
 *    its default NULL) are never used in the body - kept in the
 *    signature for call-shape fidelity, documented.
 *  - h014 out_of_scope_call : Scan (libmpscpp.cpp::Scan = B3 unit
 *    81, this batch) -> MdhStubs stub raised by this unit
 *    (cleanLocks precedent: resolved when unit 81 lands);
 *    isalnum/tolower -> local helpers (unsigned-char domain).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level ScanAlnum (the FILE-ptr/istream
 *    overloads are not in the B3 corpus rows here) - no collisions.
 *  - h017 naming            : ScanAlnum keeps the vendor name; the
 *    char* return is the carrier's CharArray (NUL-terminated).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor returns the STATIC tmp (state persists into the
 *    next call) - kept via the scratch object.
 *  - The empty-token arm returns tmp1 (NOT tmp) - the caller gets
 *    the empty Scan result, not the previous token - kept.
 *  - The again-loop re-Scans until a token starts alphanumeric;
 *    a stream of non-alnum tokens loops forever (vendor behavior
 *    kept).
 */
object ScanAlnumScratch {
    /** vendor: static char tmp[1024]. */
    val tmp = CharArray(1024)

    /** vendor: static char tmp1[1024]. */
    val tmp1 = CharArray(1024)
}

fun ScanAlnum(svptr: Msv, ep: CharArray?, toklist: CharArray? = null): CharArray {
    val tmp = ScanAlnumScratch.tmp
    val tmp1 = ScanAlnumScratch.tmp1

    while (true) { // again:
        strcpyOff(tmp1, Scan(svptr))

        if (strlenC(tmp1) == 0) {
            svptr.tpx = 0
            return tmp1
        }

        if (!isAlnumC(tmp1[0])) continue // goto again

        var j = 0
        for (i in 0 until strlenC(tmp1)) {
            if (isAlnumC(tmp1[i])) {
                tmp[j] = tolowerC(tmp1[i])
                j++
            }
        }

        tmp[j] = NUL
        svptr.tpx = 1
        return tmp
    }
}

/** C strcpy(dst, src) over the byte domains. */
private fun strcpyOff(dst: CharArray, src: CharArray) {
    var i = 0
    while (src[i] != NUL) {
        dst[i] = src[i]
        i++
    }
    dst[i] = NUL
}

/** C isalnum (unsigned-char domain). */
private fun isAlnumC(c: Char): Boolean =
    c in 'a'..'z' || c in 'A'..'Z' || c in '0'..'9'

/** C tolower (unsigned-char domain; non-letters unchanged). */
private fun tolowerC(c: Char): Char = if (c in 'A'..'Z') c + 32 else c

// ----------------------------------------------------------------------------
// (B4 unit 113) libmpscpp.cpp::ScanAlnum(FILE*, int, int) - the FIRST of the
// three vendor overloads (reference lines 10060-10085; the MSV overload above
// is B3 unit 79). Identity: units.csv row 114 - libmpscpp.cpp::ScanAlnum,
// cpp, loc 18, comment_ratio 0.053, pointer_marks 1, control_marks 5, score
// 10.6, rank 105 - exact match; the pointer_mark count (1: the FILE*
// parameter) disambiguates this overload from the istream sibling (0
// pointer marks; the istream overload is not a corpus row of this batch).
// ----------------------------------------------------------------------------

/**
 * mstring ScanAlnum(FILE *u, int min, int max): read whitespace-delimited
 * tokens from the file, keep the first whose alphanumeric-only lowercase
 * fold has length within [min, max] and does not start with a digit; on
 * EOF or the max>511 guard return the empty mstring.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param     : FILE* u -> java.io.Reader (the input-side
 *    analog of the Writer edge rule; IOException thrown explicitly - the
 *    vendor's fscanf return surface collapses to null-at-EOF).
 *  - h008 static_scratch      : word/tmp1 are STACK buffers in this
 *    overload (8192 bytes each, fresh per call) - no scratch object;
 *    noted against the static-scratch shapes of the MSV overload above.
 *  - h011 goto                : the backward "goto repeat" becomes the
 *    while(true)+continue shape.
 *  - h012 unsigned_byte_domain: word/tmp1 Char(0..255); isalnum/tolower/
 *    isdigit are the C-locale byte-domain helpers (shared with the MSV
 *    overload above).
 *  - h015 grammar_collision   : the overload folds into the Kotlin
 *    overload set by parameter types (Reader/Int/Int vs Msv/...);
 *    pre-push scan clean.
 *  - h017 naming              : ScanAlnum keeps the vendor name; the
 *    mstring return rides the Mstring carrier.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor reads the token FIRST and then discards it when
 *    "max > 511" - the guard is checked after the fscanf, so one token is
 *    always consumed before the empty return; kept.
 *  - fscanf's "%8191s" reads at most 8191 bytes into the 8192-byte word
 *    buffer; the rendered token read caps at 8191 bytes likewise.
 *  - "isdigit(tmp1[0])" is only evaluated when j>0 (the j==0 test
 *    short-circuits first in both C and Kotlin) - the vendor never reads
 *    tmp1[0] uninitialized; the fold order is kept.
 *  - A file of only alnum-free or digit-leading tokens loops until EOF
 *    (the vendor's repeat loop; kept).
 */
@Throws(java.io.IOException::class)
fun ScanAlnum(u: java.io.Reader, min: Int, max: Int): Mstring {

    val word = CharArray(8192)
    val tmp1 = CharArray(8192)
    val tmp = Mstring()

    while (true) { // repeat:

        // vendor: if (fscanf(u,"%8191s",word)==EOF || max > 511)
        val token = scanToken(u) // null == EOF
        if (token == null || max > 511) {
            tmp.value = ""
            return tmp
        }

        for (k in token.indices) word[k] = token[k]
        word[token.length] = NUL

        var j = 0
        var i = 0
        while (word[i] != NUL) { // vendor: for (int i=0; word[i]; i++)
            if (isAlnum(word[i])) {
                tmp1[j] = toLower(word[i])
                j++
            }
            i++
        }

        // vendor: if ( j == 0 || j < min || j > max || isdigit(tmp1[0]) )
        // goto repeat;
        if (j == 0 || j < min || j > max || isDigit(tmp1[0])) continue

        tmp1[j] = NUL
        tmp.value = cstring(tmp1)
        return tmp
    }
}

/** fscanf("%8191s") analog: skip whitespace, read up to 8191 non-space
 *  bytes; null on EOF before any byte. Whitespace is the C-locale set. */
@Throws(java.io.IOException::class)
private fun scanToken(u: java.io.Reader): String? {
    val sb = StringBuilder()
    var gotAny = false
    while (true) {
        val c = u.read()
        if (c == -1) return if (gotAny) sb.toString() else null
        val ch = (c and 0xFF).toChar()
        if (isSpace(ch)) {
            if (gotAny) break
        } else {
            if (sb.length == 8191) break // %8191s width cap
            sb.append(ch)
            gotAny = true
        }
    }
    return sb.toString()
}

/** C isspace (unsigned-char domain). */
private fun isSpace(c: Char): Boolean =
    c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\u000C' || c == '\u000B'

/** C isdigit (unsigned-char domain). */
private fun isDigit(c: Char): Boolean = c in '0'..'9'

/** C tolower over the byte domain (C locale ASCII fold). */
private fun toLower(c: Char): Char = if (c in 'A'..'Z') c + 32 else c

/** C isalnum over the byte domain. */
private fun isAlnum(c: Char): Boolean =
    c in 'a'..'z' || c in 'A'..'Z' || c in '0'..'9'

/** String view of a NUL-terminated buffer (cstringAt is interp-side). */
private fun cstring(b: CharArray): String {
    var i = 0
    while (b[i] != NUL) i++
    return b.concatToString(0, i)
}
