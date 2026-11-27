package both

// Unit 270 (BOTH B9, core): include/mumpsc/cgi.h::<misc> (78 LOC).
// Batch_plan ordinal 270: core, LOC 78. The CGI header-paste
// block: the anonymous decode block the vendor header drops
// into the interpreter start path (no function symbol of its
// own; h017 note: the block renders named cgiEnvDecode per the
// file purpose, the GlobalOrderH header-face precedent). The
// banner/license and the OS2 _envargs conditional (commented
// out in the vendor) are residue, not carried; QS_MAX/QS_MAX1
// seat as constants.
//
// Vendor behavior: REMOTE_ADDR and REMOTE_HOST, when set in the
// environment, copy into t and store as the symbols %RA / %RH
// (sym_ SYMSTORE); QUERY_STRING, when set, copies (strncpy
// QS_MAX1), scans to the first '&' (a full-width scan sets
// t[0]=0), stores %QS, and exits when empty; the plus-to-blank
// conversion runs over the whole buffer; then the nxtarg loop
// splits on '&': each segment splits at '=' (a segment with no
// '=' exits the whole block), the right side decodes %XX hex
// escapes (the a-f uppercase fold is verbatim) into _ftmp, a
// sym_ SYMGET_NOLBL retrieve detects a duplicate variable name
// (dup++ and the count appended to the name), the pair stores
// via sym_ SYMSTORE, and a '&' terminator shifts the remainder
// to the front and repeats.
//
// Quirks kept (ledgered):
//   - the first-scan i==QS_MAX case sets t[0]=0 (stores an
//     empty %QS) and exits; the strncpy QS_MAX1 truncation makes
//     the case a boundary guard, kept verbatim.
//   - the '=' scan reads t[j] BEFORE the j<QS_MAX bound (the C
//     order); a segment without '=' walks the NUL run out to
//     QS_MAX and exits the block. Kept: the scan folds to the
//     same exit (j==QS_MAX -> return).
//   - the %XX decode reads t[m+1]/t[m+2] with no guard past the
//     NUL (the C terminator-read UB domain: a trailing '%' would
//     compute (0-'A'+10)*16 arithmetic on the NUL bytes).
//     QUANTIZED: the two digits are required to sit inside the
//     NUL-terminated run; otherwise the copy stops at the '%'
//     (the vendor value is char-domain UB on a terminator the
//     reference inputs never produce). Journaled.
//   - the vendor %XX arithmetic does NOT validate hex (any
//     non-digit non-A-F byte computes a garbage code point);
//     kept verbatim (the arithmetic, not a validation).
//   - dup is declared inside the QUERY_STRING block: the count
//     resets per call (kept: local to the render).
//
// BOTH consults (find_rule before render, events logged):
//   h001 io_stream_param - consulted: no output macro occurs in
//     this block (ledger note; the block is env-read/sym-store
//     only).
//   h007 char_buffer - reused: t (QS_MAX), t1 (1024) and _ftmp
//     (STR_MAX) ride the CharArray carrier; cStringOf folds each
//     sym_ name/data handoff.
//   h014 out_of_scope_call - reused: sym_ (SYMSTORE/SYMGET_NOLBL
//     ops) rides its TODO stub (owner interp-sym.cpp::sym_, roster
//     280, batch 10 pending; the stub stays); getenv rides the
//     System.getenv fold (the Getenv seat precedent, here called
//     directly on the three CGI names).
//   h015 grammar_collision - reused: the %RA/%RH/%QS and %XX
//     text bytes are plain literals (no raw comment dollars; the
//     escape bytes are ASCII percent).
//   h017 naming - hit: the anonymous block takes the render name
//     cgiEnvDecode (no vendor symbol exists); QS_MAX/QS_MAX1
//     keep vendor case. Journaled.

private const val QS_MAX = 2048
private const val QS_MAX1 = 2047

internal fun cgiEnvDecode(sv: Msv) {
    val ftmp = CharArray(STR_MAX)

    System.getenv("REMOTE_ADDR")?.let {
        val t = CharArray(QS_MAX)
        writeC(t, it.take(QS_MAX1))
        sym_(0, "%RA", t, sv) // store
    }

    System.getenv("REMOTE_HOST")?.let {
        val t = CharArray(QS_MAX)
        writeC(t, it.take(QS_MAX1))
        sym_(0, "%RH", t, sv) // store
    }

    val qs = System.getenv("QUERY_STRING") ?: return
    val t = CharArray(QS_MAX)
    writeC(t, qs.take(QS_MAX1))

    // for (i=0; t[i]!='&' && t[i]!=0 && i<QS_MAX; i++);
    var i = 0
    while (i < QS_MAX && t[i] != '&' && t[i] != '\u0000') i++
    if (i == QS_MAX) t[0] = '\u0000'
    sym_(0, "%QS", t, sv) // store

    if (t[0] == '\u0000') return // goto cgi_exit

    // convert plus signs to blanks
    for (k in 0 until QS_MAX) {
        if (t[k] == '\u0000') break
        if (t[k] == '+') t[k] = ' '
    }

    val t1 = CharArray(1024)
    var dup = 0

    // nxtarg loop
    while (true) {
        var ii = 0
        while (ii < QS_MAX && t[ii] != '&' && t[ii] != '\u0000') ii++
        val k = if (ii < QS_MAX && t[ii] == '\u0000') 0 else 1
        t[ii] = '\u0000'

        // for (j=0; t[j]!='=' && j<QS_MAX; j++);
        var j = 0
        while (j < QS_MAX && t[j] != '=') j++
        if (j == QS_MAX) return // goto cgi_exit
        t[j] = '\u0000'

        // copy right hand side to _ftmp (%XX hex decode)
        var n = 0
        var m = j + 1
        while (t[m] != '\u0000') {
            if (t[m] != '%') {
                ftmp[n] = t[m]
                n++
                m++
                continue
            }
            // the two digits must sit inside the run (see KDoc)
            if (m + 2 >= t.size || t[m + 1] == '\u0000' || t[m + 2] == '\u0000') break
            var p: Int
            // first hex digit
            if (t[m + 1] in 'a'..'z') t[m + 1] = t[m + 1] - 32
            p = if (t[m + 1] in '0'..'9') {
                (t[m + 1] - '0') * 16
            } else {
                (t[m + 1] - 'A' + 10) * 16
            }
            // second hex digit
            if (t[m + 2] in 'a'..'z') t[m + 2] = t[m + 2] - 32
            p += if (t[m + 2] in '0'..'9') {
                (t[m + 2] - '0')
            } else {
                (t[m + 2] - 'A' + 10)
            }
            ftmp[n] = p.toChar()
            n++
            m += 2
        }
        ftmp[n] = '\u0000'

        // sym_(11,...) retrieve: duplicate-name detect
        val p1 = sym_(11, cStringOf(t), t1, sv)
        if (p1 != null) {
            dup++
            val s = cStringOf(t) + dup.toString() // sprintf/strcat
            writeC(t, s)
        }

        sym_(0, cStringOf(t), ftmp, sv) // store

        if (k == 1) {
            // shift input string: t[k-(i+1)] = t[k] for the tail
            var kk = ii + 1
            while (kk < QS_MAX && t[kk] != '\u0000') {
                t[kk - (ii + 1)] = t[kk]
                kk++
            }
            t[kk - (ii + 1)] = '\u0000'
            continue // goto nxtarg
        }
        break
    }
}
