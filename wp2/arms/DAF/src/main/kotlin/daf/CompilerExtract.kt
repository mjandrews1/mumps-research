package daf

// Unit 203 (DAF B7 T2, core): compiler-parse.c::Extract (30 LOC,
// score 22.4): the compiler's _extract(...) argument-lattice
// emitter. SSS/TTT are compiler-parse.c:180 FILE-SCOPE scratch
// POINTERS used as in-place compaction cursors; per the row 31
// in-place-rebuild precedent they fold onto local index shifts:
// the vendor walks the same v1d buffer the caller owns, so the
// pointer idiom is a within-call compaction, not durable state.
// The caller owns v1d/bp; they enter as CharArray carriers (row
// 30 pointer fold). Out (FILE*) is the house DafCompilerState
// emit fold (WriteCode precedent); iiitmp is seated on
// DafCompilerState. memcheckDaf seat: DafInterpStubsT3:215.
// Extract2 seat: DafInterpStubsT3:168 extract2Daf
// ("compiler-parse.c::Extract2, roster b1") - called, seat cited.
// The fprintf carries the split-string literal exactly as the
// vendor writes it (the p2493 trace comment kept verbatim).
// Consults (2026-11-24T16:13:30Z, nominal): BASE-log row 31
// hit-reuse (the in-place SSS/TTT compaction, i/j cursor family;
// the v1d[i++] = 0 splice lattice), row 27 hit-reuse (the
// sprintf(bp,"tmp%d",iiitmp) fold and the tmp accumulator),
// row 3 hit-adapt (the fcn-name skip loop: the file-scope SSS
// walk folds onto indices), manual-decision (no BASE row: the
// a==1/a==2 padding appends of ", (UNS) \"-1\"" pairs are the
// vendor's unsized-argument padding, kept verbatim; the
// counter-based a==0/>3 early return keeps the vendor's
// no-else structure).
// Dafny: EXEMPT (emit-side compiler plumbing; journal row).

private const val EXTRACT_PAD1_D = ", (UNS) \"-1\", (UNS) \"-1\""
private const val EXTRACT_PAD2_D = ", (UNS) \"-1\""

fun extractD(st: DafCompilerState, v1d: CharArray, bp: CharArray): Int {

    var i = 1
    while (i < v1d.size && v1d[i] >= ' ' && v1d[i] <= '\u007F') i++ //* fcn nme

    // SSS=v1d, TTT=&v1d[i+1]; copy-down compaction (row 31)
    var s = 0
    var t = i + 1
    while (t < v1d.size && v1d[t] != NUL_D) { v1d[s] = v1d[t]; s++; t++ }
    v1d[s] = NUL_D

    i = 0
    var a = 0
    val tmp = StringBuilder() // char tmp[1024]

    while (true) {

        val j = i

        while (i < v1d.size && v1d[i] >= ' ' && v1d[i] <= '\u007F') i++

        if (i < v1d.size && v1d[i] == NUL_D) {
            if (a == 0 || a > 3) return a
            if (a == 1) tmp.append(EXTRACT_PAD1_D)
            else if (a == 2) tmp.append(EXTRACT_PAD2_D)
            memcheckDaf(st.iiitmp) // seat DafInterpStubsT3:215

            // fprintf(Out, ...); // p2493
            // fprintf(Out, "\\t_extract(" " (UNS) tmp%d,\\n
            //   \\t\\t(UNS) %s); // p2493\\n", iiitmp, tmp)
            st.emit("\\t_extract(" + " (UNS) tmp" + st.iiitmp + ",\\n "
                + "\\t\\t(UNS) " + tmp.toString() + "); // p2493\\n")

            sprintfIntD(bp, "tmp%d", st.iiitmp) // sprintf(bp, "tmp%d", iiitmp)
            st.iiitmp++
            memcheckDaf(st.iiitmp)
            return 0
        }

        v1d[i++] = NUL_D

        if (a++ != 0) tmp.append(", (UNS) ")

        // Extract2(&v1d[j]); seat DafInterpStubsT3:168
        tmp.append(extract2Daf(String(v1d, j, i - 1 - j)))
    }
}

// minimal %d sprintf fold (row 27 family)
private fun sprintfIntD(dst: CharArray, fmt: String, v: Int) {
    val s = fmt.replace("%d", v.toString()).toCharArray()
    var i = 0
    while (i < s.size && i < dst.size - 1) { dst[i] = s[i]; i++ }
    dst[i] = NUL_D
}
