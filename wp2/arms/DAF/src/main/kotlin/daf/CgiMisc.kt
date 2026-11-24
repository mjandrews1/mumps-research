package daf

/*
 * DAF batch-9 unit 270 (roster row 270; include/mumpsc/cgi.h::<misc>,
 * residue, LOC 78, score 11.2). Consult 2026-11-24T22:47:00Z.
 *
 * cgi.h is the CGI include BODY the vendor shells include at the
 * head of the runtime (an isolate code block, not a header of
 * decls): the %QS/%RA/%RH shell-variable decode arm. The DAF
 * render lands the block as the cgiInitD helper (the include
 * folds to a call); the OS2 _envargs arm is the commented fossil
 * (H-018); QS_MAX 2048 / QS_MAX1 2047 pin as file consts.
 *
 * Vendor shape kept: getenv REMOTE_ADDR/REMOTE_HOST ->
 * System.getenv (the unit-152 row-60 DOCUMENTED DIVERGENCE
 * precedent); sym_((int)0,...) store / sym_((int)11,...) retrieve
 * ride the symRetDaf seat (0 = SymStore, 11 = the retrieve of the
 * vendor sym code ladder; the dispatcher stays the raised
 * roster-b3-family seat, so the arm throws at call time as the
 * placeholder discipline dictates); the vendor strncpy bounds
 * fold to the QS_MIN(2047)-clamp; the plus-sign -> blank pass; the
 * nxtarg loop: find '&' end, null it, split at '=', the %hh hex
 * decode (the vendor a-z vs A-Z ladder kept verbatim: the digit
 * fold p=(hi*16)+lo), the dup counter appending "%d" to the name
 * on a non-NULL retrieve, the tail shift for the k=1 case, and the
 * t empty -> early exit arms.
 * H-015 hygiene: no raw dollars ride the comments.
 * Dafny: EXEMPT (shell-decode residue over the sym seat; journal
 * row).
 */

const val QS_MAX_D = 2048
const val QS_MAX1_D = 2047

fun cgiInitD(sv: DafMsv) {
    val _ftmp = CharArray(STR_MAX_DAF)

    val ra = System.getenv("REMOTE_ADDR")
    if (ra != null) {
        val t = clEnv(ra)
        symRetDaf(0, "%RA".toCharArray(), 0, t, 0, sv) // store
    }

    val rh = System.getenv("REMOTE_HOST")
    if (rh != null) {
        val t = clEnv(rh)
        symRetDaf(0, "%RH".toCharArray(), 0, t, 0, sv) // store
    }

    val qs = System.getenv("QUERY_STRING")
    if (qs != null) {
        val t = CharArray(QS_MAX_D)
        val t1 = CharArray(1024)
        var i: Int
        var k: Int
        var dup = 0

        // strncpy(t, getenv("QUERY_STRING"), QS_MAX1)
        var q = 0
        while (q < qs.length && q < QS_MAX1_D) {
            t[q] = qs[q]
            q++
        }
        t[q] = NUL_D

        // for (i=0; t[i]!='&' && t[i]!=0 && i<QS_MAX; i++);
        i = 0
        while (t[i] != '&' && t[i] != NUL_D && i < QS_MAX_D) i++
        if (i == QS_MAX_D) t[0] = NUL_D
        symRetDaf(0, "%QS".toCharArray(), 0, t, 0, sv) // store
        if (t[0] == NUL_D) return // goto cgi_exit

        // convert plus signs to blanks
        i = 0
        while (t[i] != NUL_D && i < QS_MAX_D) {
            if (t[i] == '+') t[i] = ' '
            i++
        }

        // nxtarg:
        while (true) {
            i = 0
            while (t[i] != '&' && t[i] != NUL_D && i < QS_MAX_D) i++
            k = if (t[i] == NUL_D) 0 else 1

            if (i < QS_MAX_D) t[i] = NUL_D // t[i] = 0
            var j = 0
            while (t[j] != '=' && j < QS_MAX_D) j++
            if (j == QS_MAX_D) return // goto cgi_exit
            t[j] = NUL_D

            // copy right hand side to _ftmp (the %hh decode)
            var n = 0
            var m = j + 1
            while (t[m] != NUL_D) {
                if (t[m] != '%') {
                    _ftmp[n] = t[m]
                    n++
                    m++
                    continue
                }
                if (m + 2 >= QS_MAX_D) break // bounds guard (fold)

                var p: Int
                val hi = t[m + 1]
                val h = if (hi in 'a'..'z') hi - 32 else hi
                p = if (h in '0'..'9') (h - '0') * 16 else (h - 'A' + 10) * 16

                val lo = t[m + 2]
                val l = if (lo in 'a'..'z') lo - 32 else lo
                p += if (l in '0'..'9') (l - '0') else (l - 'A' + 10)

                _ftmp[n] = p.toChar() // insert value into output string
                n++
                m += 3 // m++ then the loop's m++ -> m+2; kept via +3
            }
            _ftmp[n] = NUL_D

            var tmp2 = DafCRuntime.cstring(t1)
            val p1 = symRetDaf(11, t, 0, t1, 0, sv) // retrieve
            if (p1 != null) {
                dup++
                tmp2 = "%d".format(java.util.Locale.US, dup)
                val base = DafCRuntime.cstring(t) + tmp2 // strcat(t,t1)
                var w = 0
                while (w < base.length && w < QS_MAX_D) { t[w] = base[w]; w++ }
                t[w] = NUL_D
            }

            symRetDaf(0, t, 0, _ftmp, 0, sv) // store

            if (k == 1) {
                var w2 = i + 1
                while (t[w2] != NUL_D && w2 < QS_MAX_D) {
                    t[w2 - (i + 1)] = t[w2]
                    w2++
                }
                t[w2 - (i + 1)] = NUL_D
                continue // goto nxtarg
            }
            break
        }
    }
}

// getenv strncpy clamp: the env tail cut at QS_MAX1.
private fun clEnv(s: String): CharArray {
    val c = CharArray(QS_MAX_D)
    var q = 0
    while (q < s.length && q < QS_MAX1_D) {
        c[q] = s[q]
        q++
    }
    c[q] = NUL_D
    return c
}
