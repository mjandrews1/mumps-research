package mumps.interp

/**
 * PRO-arm translation of include/mumpsc/cgi.h::<misc> (roster
 * row 255, batch 9; units.csv: loc 78, comment_ratio 0.345,
 * pointer_marks 0, control_marks 27, score 42.8, rank 227).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 27-149. cgi.h is an INCLUDE-FUNCTION header: a
 * raw code body textually included into mumps.cpp main
 * (mumps.cpp:136) that decodes CGI environment arguments into
 * the symbol table before the interpreter proper. Rendered here
 * as the mumpsCgi(svPtr) seat; the include site in
 * MumpsMain.kt binds to it (this batch, u130 row note).
 *
 * Body walk (vendor :39-149):
 *  - QS_MAX 2048 / QS_MAX1 2047 (the OS2 _envargs arm is
 *    commented out in the vendor, kept out).
 *  - REMOTE_ADDR -> sym_ store "%RA"; REMOTE_HOST -> "%RH";
 *    QUERY_STRING -> full query in "%QS" (truncated to '&').
 *  - The '+'-to-blank pass, then the nxtarg loop: split at '&',
 *    split the pair at '=', hex-decode "%XY" escapes into _ftmp
 *    (both hex digits uppercased first, the a..z comparisons
 *    kept verbatim), store with sym_ over the name; on duplicate
 *    names append the dup counter (sprintf t1 "%d"; strcat).
 *  - The not-at-end shift advances the query and repeats.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param  : getenv folds onto System.getenv.
 *  - h009 format_string    : sprintf(t1,"%d",dup) -> toString.
 *  - h011 label_model      : the nxtarg/cgi_exit goto pair is
 *    a while loop over the not-at-end shift.
 *  - h012 unsigned_byte_domain: t/_ftmp/t1 are NUL-domain
 *    CharArrays (trncpy folds strncpy(to,from,QS_MAX1)).
 *  - h014 out_of_scope_call: sym_ is the batch-10 row-297
 *    roster seat - the sym_ STORE-and-RETRIEVE pair binds to
 *    the InterpStubs sym_ stub (mutating, raises with its
 *    batch); strncpy preserves the vendor non-termination
 *    exactly (the QS_MAX1 cap).
 *  - h017 naming          : mumpsCgi names the include body
 *    per the u130 one-main-per-package precedent.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The first hex-digit arm maps a..z only to itself minus 32
 *    (chars beyond 'z' or below 'a' fall into the -'A'+10 arm)
 *    - kept verbatim; identical for the second digit.
 *  - The dup counter starts at 0 and pre-increments, so the
 *    SECOND instance of a name appends "1" (vendor kept).
 *  - The REMOTE/QUERY arms run only when the environment
 *    variables exist; under the JVM they are usually unset and
 *    the whole body is inert at startup (u130 argc note).
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (FTMP seat)
private const val QS_MAX = 2048
private const val QS_MAX1 = 2047

/** The cgi.h include body (mumps.cpp:136 include site). */
fun mumpsCgi(svPtr: Msv) {
    val ftmp = CharArray(STR_MAX) // char _ftmp[STR_MAX]

    System.getenv("REMOTE_ADDR")?.let { ra ->
        val t = CharArray(QS_MAX)
        ncopyStr(t, ra) // strncpy(t,getenv(...),QS_MAX1)
        sym_(0, "%RA".toCharArray(), 0, t, 0, svPtr) /* store */
    }

    System.getenv("REMOTE_HOST")?.let { rh ->
        val t = CharArray(QS_MAX)
        ncopyStr(t, rh)
        sym_(0, "%RH".toCharArray(), 0, t, 0, svPtr) /* store */
    }

    System.getenv("QUERY_STRING")?.let { qs ->
        val t = CharArray(QS_MAX)
        val t1 = CharArray(1024)
        var dup = 0
        ncopyStr(t, qs) // strncpy(t,getenv(...),QS_MAX1)

        var i = 0 // for (i=0; t[i]!='&' && t[i]!=0 && i<QS_MAX; i++)
        while (t[i] != '&' && t[i] != NUL && i < QS_MAX) i++
        if (i == QS_MAX) t[0] = NUL
        sym_(0, "%QS".toCharArray(), 0, t, 0, svPtr) /* store */
        if (t[0] == NUL) return // goto cgi_exit

        // convert plus signs to blanks
        i = 0
        while (t[i] != NUL && i < QS_MAX) {
            if (t[i] == '+') t[i] = ' '
            i++
        }

        var k: Int
        var notDone = true // the nxtarg loop (h011)
        while (notDone) {
            i = 0
            while (t[i] != '&' && t[i] != NUL && i < QS_MAX) i++
            k = if (t[i] == NUL) 0 else 1
            t[i] = NUL

            var j = 0 // for (j=0; t[j]!='=' && j<QS_MAX; j++);
            while (t[j] != '=' && j < QS_MAX) j++
            if (j == QS_MAX) return // goto cgi_exit
            t[j] = NUL

            // copy right hand side to _ftmp (the hex decode)
            var n = 0
            var m = j + 1
            while (t[m] != NUL) {
                if (t[m] != '%') {
                    ftmp[n] = t[m]
                    n++
                    m++
                    continue
                }

                // first hex digit
                var p: Int
                if (t[m + 1] >= 'a' && t[m + 1] <= 'z') {
                    t[m + 1] = (t[m + 1].code - 32).toChar()
                }
                if (t[m + 1] >= '0' && t[m + 1] <= '9') {
                    p = (t[m + 1].code - '0'.code) * 16
                } else {
                    p = (t[m + 1].code - 'A'.code + 10) * 16
                }

                // second hex digit
                if (t[m + 2] >= 'a' && t[m + 2] <= 'z') {
                    t[m + 2] = (t[m + 2].code - 32).toChar()
                }
                if (t[m + 2] >= '0' && t[m + 2] <= '9') {
                    p += t[m + 2].code - '0'.code
                } else {
                    p += t[m + 2].code - 'A'.code + 10
                }

                ftmp[n] = p.toChar() /* insert value into output */
                n++
                m += 2
                m++
            }
            ftmp[n] = NUL

            run {
                // p1 = sym_(11,t,t1,svPtr); retrieve
                val mayDup = sym_(11, t, 0, t1, 0, svPtr)

                if (mayDup != null) {
                    // multiple instances of the same variable
                    // name have numbers appended to the name
                    dup++
                    val d = dup.toString() // sprintf(t1,"%d",dup)
                    writeCstring(t1, d)
                    // strcat(t,t1)
                    var ai = strlenC(t)
                    var dj = 0
                    while (t1[dj] != NUL) {
                        t[ai] = t1[dj]
                        ai++
                        dj++
                    }
                    t[ai] = NUL
                }

                sym_(0, t, 0, ftmp, 0, svPtr) /* store */
            }

            // not at end yet - shift input string
            if (k != 0) {
                var kk = i + 1
                while (t[kk] != NUL && kk < QS_MAX) {
                    t[kk - (i + 1)] = t[kk]
                    kk++
                }
                t[kk - (i + 1)] = NUL
                notDone = true // goto nxtarg
            } else {
                notDone = false
            }
        }
    } // cgi_exit
}

/** strncpy(t, from, QS_MAX1) - no terminator written (vendor). */
private fun ncopyStr(dst: CharArray, src: String) {
    val mx = if (src.length < QS_MAX1) src.length else QS_MAX1
    for (k in 0 until mx) dst[k] = src[k]
}
