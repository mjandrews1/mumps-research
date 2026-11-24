package daf

/*
 * DAF batch-9 unit 265 (roster row 265; compiler-parse.c::ExpandName,
 * core, LOC 47, score 12.1). Consult 2026-11-24T22:43:00Z.
 *
 * Vendor shape kept (the unit-228 ExpandText sibling): signature
 * family (st, v1d, iIn, bp) per the row-30 carrier fold; Out
 * (FILE*) rides the C.emit house fold; arg1/arg2/arg3 are the
 * vendor char[32] locals rendered as CharArray(32) carriers.
 *
 * VERBATIM VENDOR BUGS kept, ledgered here (H-018 fossil):
 * - the #S# arm's Extract2 guard duplicates the "#S#" test twice
 *   (the vendor almost surely meant #G#/#S#; kept verbatim).
 * - the fill loops never NUL-terminate arg1/arg2/arg3 (the
 *   char[32]="" zero-init only clears cell 0); the render's
 *   helper writes the terminator at the stop index, the
 *   bounds-guard fold of the vendor's stack read (DOCUMENTED
 *   DIVERGENCE: no stack-overread hazard).
 * Extract2 resolves to the extract2Daf roster-b1 seat (STILL
 * RAISED stub; GlobalName emit arms of batch 1 will release it).
 * CodedComma: the delimiter test rides the CODED_COMMA_D seat per
 * the ExpandOrder row-111 precedent (the C-012 name-inversion
 * adjudication; arm-internal).
 * bp <- arg2 (the strcpy(bp,arg2) name handoff on both arms).
 * H-015 hygiene: no raw dollars ride the comments.
 * Dafny: EXEMPT (emit-side compiler plumbing; journal row).
 */

fun expandNameD(st: DafCompilerState, v1d: CharArray, iIn: Int, bp: CharArray) {
    val C = st
    val arg1 = CharArray(32)
    val arg2 = CharArray(32)
    val arg3 = CharArray(32)

    var i = iIn
    i++ // i++

    if (at(v1d, i) == '#' && at(v1d, i + 1) == 'G' && at(v1d, i + 2) == '#') {
        i += 3
        var j = 0
        while (at(v1d, i) != '#') { arg1[j] = v1d[i]; j++; i++ }
        i++
        j = 0
        while (at(v1d, i) != '#') { arg2[j] = v1d[i]; j++; i++ }
        i += 1
        if (at(v1d, i) == CODED_COMMA_D) {
            i++
            if ((at(v1d, i) == '#' && at(v1d, i + 1) == 'G' && at(v1d, i + 2) == '#') ||
                (at(v1d, i) == '#' && at(v1d, i + 1) == 'S' && at(v1d, i + 2) == '#')
            ) {
                // strcpy(arg3,Extract2(&v1d[i])); - roster-b1 seat
                val e = extract2Daf(cstringTailD(v1d, i))
                var q = 0
                while (q < e.length && q < 31) { arg3[q] = e[q]; q++ }
                arg3[q.coerceAtMost(31)] = NUL_D
            } else {
                j = 0
                while (at(v1d, i) < '\u0080' && at(v1d, i) != NUL_D) {
                    arg3[j] = v1d[i]
                    j++
                    i++
                }
                arg3[j] = NUL_D
            }
        } else {
            val e = "NULL"
            var q = 0
            while (q < e.length) { arg3[q] = e[q]; q++ }
            arg3[q] = NUL_D
        }

        C.emit("\tGlobalName(" + DafCRuntime.cstring(arg1) + ", " +
                DafCRuntime.cstring(arg2) + ", " +
                DafCRuntime.cstring(arg3) + ", svPtr); \n")

        bpCopyD(bp, arg2)
        return
    }

    if (at(v1d, i) == '#' && at(v1d, i + 1) == 'S' && at(v1d, i + 2) == '#') {
        i += 3
        var j = 0
        while (at(v1d, i) != '#') { arg1[j] = v1d[i]; j++; i++ }
        i++
        j = 0
        while (at(v1d, i) != '#') { arg2[j] = v1d[i]; j++; i++ }
        i += 1
        if (at(v1d, i) == CODED_COMMA_D) {
            i++
            // vendor bug kept verbatim: both tests read "#S#"
            if ((at(v1d, i) == '#' && at(v1d, i + 1) == 'S' && at(v1d, i + 2) == '#') ||
                (at(v1d, i) == '#' && at(v1d, i + 1) == 'S' && at(v1d, i + 2) == '#')
            ) {
                val e = extract2Daf(cstringTailD(v1d, i))
                var q = 0
                while (q < e.length && q < 31) { arg3[q] = e[q]; q++ }
                arg3[q.coerceAtMost(31)] = NUL_D
            } else {
                j = 0
                while (at(v1d, i) < '\u0080' && at(v1d, i) != NUL_D) {
                    arg3[j] = v1d[i]
                    j++
                    i++
                }
                arg3[j] = NUL_D
            }
        } else {
            val e = "NULL"
            var q = 0
            while (q < e.length) { arg3[q] = e[q]; q++ }
            arg3[q] = NUL_D
        }

        C.emit("\tLocalName( (unsigned char *) " + DafCRuntime.cstring(arg1) +
                ", (unsigned char *) " + DafCRuntime.cstring(arg2) +
                ", (unsigned char *) " + DafCRuntime.cstring(arg3) + ", svPtr); \n")

        bpCopyD(bp, arg2)
        return
    }
}

// &v1d[i] tail -> String (the Extract2 C-string arg fold).
private fun cstringTailD(v1d: CharArray, i: Int): String {
    val sb = StringBuilder()
    var q = i
    while (q < v1d.size && v1d[q] != NUL_D) {
        sb.append(v1d[q])
        q++
    }
    return sb.toString()
}

// strcpy(bp, arg2) carrier copy.
private fun bpCopyD(bp: CharArray, src: CharArray) {
    var q = 0
    while (q < src.size && src[q] != NUL_D) {
        bp[q] = src[q]
        q++
    }
    bp[q] = NUL_D
}
