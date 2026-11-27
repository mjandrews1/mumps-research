package both

// FILE-NAME COLLISION NOTE: the repo already tracks Extract.kt
// (unit 168, interp-sym.cpp::_extract - a DIFFERENT vendor
// function; no Kotlin symbol collision: _extract vs Extract).
// This unit-197 render therefore files as ExtractCompiler.kt
// (h017). The gate copy of the unit-168 file is restored from
// master verbatim.

// Unit 197 render: compiler-parse.c::Extract (roster 197,
// batch 7; units.csv rank 178 lists ExpandGet/Extract2 family
// - this is the arg-list Extract, compiler-parse.c:443 region).
// The extrinsic-argument extractor: strips the function name,
// then splits the remainder on non-printable separators,
// building the _extract(...) emission argument list and
// returning the tmp-name in bp.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: extract2C stub called
//     direct (owner compiler-parse.c::Extract2; the BothStubs
//     pin says "roster 171" which is again the RANK column -
//     true roster unit 17, same mislabel family, cosmetic fix
//     queued). memcheck RENDERED here (compiler-parse.c:2068
//     non-unit 2-line helper - the ErrorMessage non-unit
//     precedent: render, not stub).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: Extract keeps vendor name/case.
//   h019 class_member_carrier - reused: iiitmp/MaxTmp ride the
//     CompilerGlobalsExt seats (:109 / MaxTmp).
//   h020 pointer_handle - reused: the SSS/TTT TU-static
//     pointers (compiler-parse.c:180) are pure scratch in this
//     body (no cross-call state read before write); they fold
//     to local index pairs (journaled). The &v1d[j] seat rides
//     the extract2C slice-index adaptation.
//
// Quirks kept (ledgered):
//   - the in-place shift (SSS=v1d, TTT=&v1d[i+1]) pulls the
//     remainder down over the function name - v1d is MUTATED
//     (kept).
//   - the a==1 / a==2 tail paddings append the ", (UNS) \"-1\""
//     literals exactly (the arg-count normalization wart).
//   - if (a++) strcat: the OLD a gates the comma, then a
//     increments (the post-increment kept exact).
//   - the a==0 || a>3 early return keeps a as the error code.
//   - array-end guard: the vendor scans rely on the NUL; a
//     missing NUL reads OOB (UB). QUANTIZED: past-end indexes
//     read as NUL (the loops stop; journaled).

// void memcheck(int iiitmp) - compiler-parse.c:2068; non-unit
// vendor helper (rendered, not stubbed - the ErrorMessage
// precedent)
internal fun memcheck(iiitmp: Int) {
    if (iiitmp > CompilerGlobalsExt.MaxTmp.toInt()) {
        CompilerGlobalsExt.MaxTmp = iiitmp.toShort()
    }
}

// fprintf(Out, ...) fold
private fun exOutZb(s: String) {
    try {
        CompilerOutState.out.write(s)
    } catch (e: java.io.IOException) {
        // vendor ignores the fprintf return (failure is silent)
    }
}

// int Extract(unsigned char *v1d, unsigned char *bp) -
// compiler-parse.c
internal fun Extract(v1d: CharArray, bp: CharArray): Int {
    val g = CompilerGlobalsExt

    // for (i = 1; v1d[i] >= 32 && v1d[i] <= 127; i++); fcn nme
    var i = 1
    while (i < v1d.size && v1d[i].code >= 32 && v1d[i].code <= 127) i++

    // for (SSS=v1d,TTT=&v1d[i+1]; *TTT!=0; SSS++,TTT++) *SSS=*TTT;
    // *SSS=0; - the in-place shift (SSS/TTT scratch, folds local)
    var s = 0
    var t = i + 1
    while (t < v1d.size && v1d[t] != '\u0000') {
        v1d[s] = v1d[t]
        s++
        t++
    }
    v1d[s] = '\u0000'

    i = 0
    var a = 0
    var tmp = "" // char tmp[1024]; the strcat chain folds

    while (true) {
        val j = i

        while (i < v1d.size && v1d[i].code >= 32 && v1d[i].code <= 127) i++

        if (i >= v1d.size || v1d[i] == '\u0000') {
            if (a == 0 || a > 3) return a
            if (a == 1) tmp += ", (UNS) \"-1\", (UNS) \"-1\""
            else if (a == 2) tmp += ", (UNS) \"-1\""
            memcheck(g.iiitmp)

            exOutZb(
                "\t_extract(" + " (UNS) tmp" + g.iiitmp +
                    ",\n \t\t(UNS) " + tmp + "); // p2493\n",
            )

            writeC(bp, "tmp" + g.iiitmp) // sprintf(bp, "tmp%d", iiitmp)
            g.iiitmp++
            memcheck(g.iiitmp)
            return 0
        }

        v1d[i] = '\u0000'
        i++

        val old = a // if (a++) - the old value gates
        a++
        if (old != 0) tmp += ", (UNS) "

        tmp += extract2C(v1d, j)
    }
}
