package both

// Unit 267 (BOTH B9, core): compiler-sys1.c::<misc> (107 LOC).
// Batch_plan ordinal 267: core, LOC 107, score 46.0. The K&R
// helper tail of compiler-sys1.c: the file banner and include
// block are not carried (residue-banner rule, T1 unit-124/125
// precedent), the Sysout(x) macro folds to the Out stream seat
// (h001), and the GBLROOT/CRTN/CTRLZ/TAB/LINEFEED defines and
// NO/YES are banner residue (noted only). Eight bodies render:
// lpad (duplicate), sigc, itoa, linepad, linepad1, linepad2,
// gcvt, mult.
//
// BOTH consults (find_rule before render, events logged):
//   h001 io_stream_param - reused: every fprintf(Out, ...) seat
//     rides CompilerOutState.out.write (the epiOutZb/fiOutZb
//     precedent); the fprintf return count is only consumed by
//     linepad1's dot padding.
//   h009 numeric_format - reused: gcvt's sprintf("%g") is the
//     cFormatG(x, 6) seat (default C99 precision 6); the mps_*
//     gcvtWrapper seat stays precision 8 (different caller
//     family, not folded).
//   h014 out_of_scope_call - reused: linepad releases the
//     CompilerEpi.kt linepadZb stub and the WriteCode.kt
//     linepad stub (both pinned to this unit); linepad2 folds
//     onto the Fcninit.kt linepad2Zb helper (verified twin,
//     line-for-line, no new code; ownership moves here).
//   h017 naming - hit: mult renames to multSys1Zb because the
//     top-level mult name is already seated by the Cannon.kt
//     stub whose owner is interp-arith.cpp::mult (roster-67,
//     batch 3 pending) - a different vendor function; lpad
//     renames to lpadSys1Zb for the same reason (the lpad name
//     is seated by the unit-141 strmanip render). Journaled.
//
// Quirks kept (ledgered):
//   - lpad is a vendor duplicate of the interp-strmanip.cpp lpad
//     (unit 141, B5 T2) - identical 9-line body. Rendered as a
//     twin seat lpadSys1Zb (verified line-for-line against the
//     unit-141 render, no new code); the duplicate is journaled,
//     not folded, because the two vendor files own separate
//     copies (h013 duplication).
//   - itoa ignores its rad argument (sprintf "%d" only); kept.
//   - linepad declares sent/i/j/f locals that are all dead in
//     the vendor body (the single fprintf is the only effect);
//     sent stays as the caller-side fprintf count convention
//     (the epiOutZb fold).
//   - linepad1 pads with dots to column 25 using the fprintf
//     return as the start column; the trailing "*/ " emission
//     has NO newline in the vendor (the caller supplies it).
//   - gcvt ignores its i (precision) argument; %g precision 6
//     applies regardless. Kept.
//   - mult computes in C float precision (atof -> float
//     multiply, promoted back to double only at the gcvt
//     boundary); the Kotlin fold uses Float to keep the
//     precision loss verbatim, and gcvt(cc, 7, c) still renders
//     precision 6 because gcvt ignores its arg (double quirk).
//   - sigc touches the compiler-side externs-h ctrlc (Short);
//     distinct from the Msv.ctrlc interpreter field.

// void lpad(unsigned char strin[], unsigned char strout[], short l)
// - vendor duplicate of the unit-141 lpad; twin seat.
internal fun lpadSys1Zb(strin: CharArray, strout: CharArray, l: Int) {
    lpad(strin, strout, l)
}

// int sigc() - externs-h ctrlc = 1, return 1.
internal fun sigc(): Int {
    CompilerGlobalsExt.ctrlc = 1
    return 1
}

// void itoa(int val, int rad, char *str) - rad ignored (sprintf "%d").
internal fun itoa(value: Int, rad: Int, str: CharArray) {
    val s = value.toString().toCharArray()
    require(s.size + 1 <= str.size) { "itoa out-buffer too small" }
    System.arraycopy(s, 0, str, 0, s.size)
    str[s.size] = 0.toChar()
}

// void linepad(int sent, char *str) - compiler-sys1.c:949. The
// sent/i/j/f locals are dead in the vendor body; the single
// fprintf emission is the only effect. Releases the CompilerEpi
// linepadZb and WriteCode linepad stubs (h014).
internal fun linepad(sent: Int, str: String) {
    try {
        CompilerOutState.out.write("  /* " + str + " */\n")
    } catch (e: java.io.IOException) {
        // vendor ignores the fprintf return (failure is silent)
    }
}

// fprintf(Out, ...) fold returning the written count (the
// epiOutZb precedent; the exact fprintf return over ASCII text).
private fun sys1OutZb(s: String): Int {
    try {
        CompilerOutState.out.write(s)
    } catch (e: java.io.IOException) {
        // vendor ignores the fprintf return (failure is silent)
    }
    return s.length
}

// void linepad1(char *str) - the dot-padded banner column.
internal fun linepad1(str: String) {
    val i = sys1OutZb("/* " + str + " ")
    var j = i
    while (j < 25) {
        sys1OutZb(".")
        j++
    }
    sys1OutZb("*/ ")
}

// void linepad2(char *str) - the /*** banner. Ownership moved
// here from the Fcninit.kt linepad2Zb helper (verified twin).
internal fun linepad2(str: String) {
    try {
        CompilerOutState.out.write("/*** " + str + " */\n")
    } catch (e: java.io.IOException) {
        // vendor ignores the fprintf return (failure is silent)
    }
}

// void gcvt(double x, int i, char a[]) - i ignored; sprintf "%g"
// (C99 default precision 6, the cFormatG seat; the NUL rides
// the sprintf semantics, unlike the no-NUL gcvtWrapper seat).
internal fun gcvt(x: Double, i: Int, a: CharArray) {
    val s = cFormatG(x, 6).toCharArray()
    require(s.size + 1 <= a.size) { "gcvt out-buffer too small" }
    System.arraycopy(s, 0, a, 0, s.size)
    a[s.size] = 0.toChar()
}

// void mult(char a[], char b[], char c[]) - compiler-sys1.c K&R
// multiply: atof into C float, multiply, gcvt(cc, 7, c). Renamed
// multSys1Zb (h017): the mult name is held by the Cannon.kt stub
// whose owner is interp-arith.cpp::mult (roster-67, batch 3).
internal fun multSys1Zb(a: CharArray, b: CharArray, c: CharArray) {
    val aa = strtodC(cStringOf(a)).toFloat()
    val bb = strtodC(cStringOf(b)).toFloat()
    val cc = aa * bb
    gcvt(cc.toDouble(), 7, c)
}
