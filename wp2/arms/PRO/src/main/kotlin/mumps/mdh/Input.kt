package mumps.mdh

import java.io.IOException
import java.io.Reader
import mumps.interp.Msv
import mumps.interp.NUL
import mumps.interp.strcpyC

/**
 * PRO-arm translation of libmpscpp.cpp::Input (pilot B1, unit 23).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 9984-10036.
 *
 * Input(svptr, ep, name1): reads one line from the current input
 * file, splits it into whitespace-delimited words, strips C-locale
 * punctuation (keeping '-'), and stores each word under name1 with
 * the vendor's x01-separator subscripting (strcatx); returns the
 * word count as a decimal string ("0" at EOF).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param  : FILE* in_file[io] -> Reader carrier
 *    fields on Msv (stateVector.h:49/52/61/64).
 *  - h002 simple_output_macro: fgets/sscanf/ispunct libc calls ->
 *    local helpers (fgetsC/sscanfS/ispunctC) preserving call shape.
 *  - h008 static_scratch  : vendor static char tmp[1024],
 *    scount[32] -> fresh locals; the char* return becomes a fresh
 *    String per call.
 *  - h011 goto            : backward goto reread -> read loop with
 *    continue; forward goto noword -> storage block guarded.
 *  - h012 unsigned_byte_domain: line/name/tmp1 byte domains.
 *  - h014 out_of_scope_call: sym_ (symbol-table primitive) and
 *    strcatx (libmpscpp.cpp helper) are not corpus units ->
 *    owner-named stubs in MdhStubs.kt.
 *  - h017 naming          : Input keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor mixes the parameter 'svptr' and the file-scope
 *    global 'svPtr' in the same body (symflg stores go to svPtr,
 *    tpx to svptr); both map to the same Msv carrier here.
 *  - Parameter 'ep' is never used by the vendor body (dead
 *    parameter); kept in the signature.
 *  - The doubled label line 'goto noword;;' has a stray semicolon
 *    (line 10013); the structure, not the typo, is translated.
 */
@Throws(IOException::class)
fun Input(svptr: Msv, ep: CharArray, name1: CharArray): String {
    val tmp = CharArray(1024)   // vendor static char tmp[1024]
    val scount = CharArray(32) // vendor static char scount[32]
    val line = CharArray(2 * 32000) // vendor char line[2*STR_MAX]
    val name = CharArray(256)
    val tmp1 = CharArray(1024)
    var count = 0
    var off = 0

    svptr.symflg = 2       /* kill existing array */
    sym_(2, name1, CharArray(1), svptr) // vendor: sym_(2, name1, "", svPtr)

    // reread: backward goto -> loop
    var nl: Boolean
    while (true) {
        nl = fgetsC(svptr.in_file[svptr.io]!!, line, 32000)
        if (!nl) return "0"
        if (line[0] == '\n') continue // vendor: goto reread
        break
    }

    while (true) {
        tmp[0] = NUL
        if (sscanfS(line, off, tmp1) == 0) {
            // number of words read
            svptr.tpx = 0
            return countStr(count.toString(), tmp)
        }

        svptr.tpx = 1

        // for (j=0,i=0; tmp1[i]!=0 && i<1023; i++) if ('-' or !ispunct) tmp[j++]=tmp1[i]
        var j = 0
        var i = 0
        while (tmp1[i] != NUL && i < 1023) {
            if (tmp1[i] == '-' || !ispunctC(tmp1[i])) {
                tmp[j] = tmp1[i]
                j++
            }
            i++
        }
        tmp[j] = NUL
        tmp[1023] = NUL

        // vendor: if (tmp[0]==0) goto noword; - forward goto -> guard
        if (tmp[0] != NUL) {
            scountFromCount(scount, count)
            count++
            strcpyC(name, name1)
            strcatx(name, CharArray(1)) // vendor: strcatx(name,"") - adds x01s
            strcatx(name, scount)      // adds x01s
            svptr.symflg = 0           /* store */
            sym_(0, name, tmp, svptr)
        }

        // noword: while(line[off]!=0 && line[off]!=' ' && line[off]!='\t') off++;
        while (line[off] != NUL && line[off] != ' ' && line[off] != '\t') off++

        if (line[off] == NUL) {
            // number of words read
            svptr.tpx = 0
            return countStr(count.toString(), tmp)
        }

        off++
    }
}

/** sprintf(scount,"%d",count) analog into the C buffer. */
private fun scountFromCount(scount: CharArray, count: Int) {
    val s = count.toString()
    for (i in s.indices) scount[i] = s[i]
    scount[s.length] = NUL
}

/** sprintf(tmp,"%d",count) analog returning the C static buffer content. */
private fun countStr(s: String, tmp: CharArray): String {
    for (i in s.indices) tmp[i] = s[i]
    tmp[s.length] = NUL
    return s
}

/**
 * fgets analog: reads up to max-1 chars from r into buf, stopping
 * after '\n'; NUL-terminates; false on EOF with nothing read.
 */
private fun fgetsC(r: Reader, buf: CharArray, max: Int): Boolean {
    var n = 0
    while (n < max - 1) {
        val c = r.read()
        if (c == -1) {
            if (n == 0) return false
            break
        }
        buf[n] = c.toChar()
        n++
        if (c.toChar() == '\n') break
    }
    buf[n] = NUL
    return true
}

/**
 * sscanf(&line[off], "%1023s", tmp1) analog: skips isspace, copies up
 * to 1023 non-space chars into tmp1 (NUL-terminated); returns 0 if no
 * conversion (only whitespace/NUL from off), 1 otherwise.
 */
private fun sscanfS(line: CharArray, off: Int, tmp1: CharArray): Int {
    var i = off
    while (line[i] != NUL && isSpaceC(line[i])) i++
    if (line[i] == NUL) return 0
    var j = 0
    while (line[i] != NUL && !isSpaceC(line[i]) && j < 1023) {
        tmp1[j] = line[i]
        i++
        j++
    }
    tmp1[j] = NUL
    return 1
}

/** C-locale isspace: space \t \n \v \f \r. */
private fun isSpaceC(c: Char): Boolean =
    c == ' ' || c == '\t' || c == '\n' || c == '\u000B' || c == '\u000C' || c == '\r'

/** C-locale ispunct: printable, non-alnum, non-space ASCII. */
private fun ispunctC(c: Char): Boolean {
    val code = c.code
    if (code < 33 || code > 126) return false
    return !isSpaceC(c) && !(c in '0'..'9') && !(c in 'a'..'z') && !(c in 'A'..'Z')
}
