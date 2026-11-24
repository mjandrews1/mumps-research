package daf

/*
 * DAF batch-9 unit 267 (roster row 267; compiler-sys1.c::<misc>,
 * residue, LOC 107, score 11.2). Consult 2026-11-24T22:45:00Z.
 *
 * This roster row is the compiler-sys1.c FILE PRELUDE plus the
 * K&R tail chunks the chunker folded in (lpad, sigc, itoa,
 * linepad, linepad1, linepad2, gcvt, mult).
 *
 * PRELUDE LEDGER (no new code): the April 20 2025 date line, the
 * Fortran-heritage banner, the "Not all functions work at
 * present" banner, Sysout(x) macro (the C.emit fold), the
 * include ladder, GBLROOT 30 / CRTN \15 / CTRLZ \32 / TAB \11 /
 * LINEFEED \12 - PINNED where they already ride (Cannon.kt of the
 * unit-240 render pins the cannon ladder; no new consts), the
 * externs.h include (the global carrier fold of
 * DafCompilerState/DafInterpState), and the mult prototype.
 *
 * SEAT RELEASE: linepadDaf (the named-owner stub raised at
 * ForCode.kt:372, blamed "compiler TU pad helper; macros
 * family") - TAG CORRECTION: the owner is THIS row's body; the
 * stub is deleted in ForCode.kt with a release note and the
 * real body lands here at the same (C, text, tag) signature the
 * ForCode/WriteCode call sites already bind: the vendor folds
 * linepad(fprintf(Out,text...), tag) into emit(text) then the
 * "  /* tag */" comment line; the dead i/j/f locals are the
 * H-013 zero seats.
 *
 * lpad: the K&R left-pad (k = l - j - 1 note: the vendor short
 * l, the -1 against the strlen-1 j renders verbatim; the
 * k<=0 early return keeps the caller's strout untouched).
 * sigc: ctrlc = 1 via the DafInterpState.svPtr seat (the vendor
 * TU global). itoa: sprintf %d. linepad1: the dot-leader pad to
 * 25. linepad2: the triple-star banner. gcvt: sprintf %g via
 * DafCRuntime.fmtG. mult: the FLOAT-precision product of the
 * vendor float aa/bb/cc (DOCUMENTED divergence note: kept float
 * per the vendor single-precision truncation).
 * H-015 hygiene: no raw dollars ride the comments.
 * Dafny: EXEMPT (fold-in compiler-TU helpers; journal row).
 */

// void linepad(int sent, char *str) - RELEASED seat body.
fun linepadDaf(C: DafCompilerState, text: String, tag: String): Unit {
    C.emit(text)
    C.emit("  /* " + tag + " */\n")
}

// void lpad(strin, strout, l) - the K&R left-pad of THIS chunk is
// BYTE-IDENTICAL to the interp-strmanip body already seated
// (lpadDaf -> lpadStrmanipD at StrmanipMisc.kt, roster row 137):
// j = strlen-1, k = l-j-1, k<=0 early return, the j+k+1 NUL quirk
// and the leading-space fill all match, physically verified this
// session. NO DUPLICATE BODY: the compiler callers bind the
// existing lpadDaf seat (row 98 overloads-collapse precedent).

// int sigc() - the ctrlc signal-set stub of the vendor.
fun sigcD(): Int {
    DafInterpState.svPtr.ctrlc = 1
    return 1
}

// void itoa(val, str, rad) - the rad param is the vendor's dead
// radix seat (H-013: only %d is ever emitted).
fun itoaD(v: Int, str: CharArray, rad: Int) {
    val s = "%d".format(java.util.Locale.US, v)
    var q = 0
    while (q < s.length) { str[q] = s[q]; q++ }
    str[q] = NUL_D
    return
}

// void linepad1(char *str) - the dot-leader comment pad. Vendor
// shape: fprintf(Out,"/* %s ",str) then dots to column 25; the
// fprintf return-count i feeds the pad loop (kept: q counts the
// emitted prefix length).
fun linepad1D(C: DafCompilerState, str: String) {
    val prefix = "/* " + str + " "
    C.emit(prefix)
    var j = prefix.length
    while (j < 25) {
        C.emit(".")
        j++
    }
    C.emit("*/ ")
}

// void linepad2(char *str) - the triple-star banner. SEAT
// RELEASE: the linepad2Daf named stub raised at
// CompilerFcninit.kt (blame "compiler-read.c linepad2; not a
// corpus unit") is released HERE - TAG CORRECTION: the body
// lives in compiler-sys1.c (THIS roster row; the compiler-read.c
// blame was wrong); the stub is deleted with a release note and
// the real body lands at the same (text) String signature the
// fcninit call site binds (DafCompilerState.emit static seat).
fun linepad2Daf(text: String) {
    DafCompilerState.emit("/*** " + text + " */\n")
}

// void gcvt(x, i, a) - %g sprintf; the i digit-count param is
// the vendor's dead seat (H-013).
fun gcvtD(x: Double, i: Int, a: CharArray) {
    val s = DafCRuntime.fmtG(x)
    var q = 0
    while (q < s.length) { a[q] = s[q]; q++ }
    a[q] = NUL_D
    return
}

// void mult(a, b, c) - the float-precision product. SEAT RELEASE:
// the multDaf named stub raised in Cannon.kt (blame "not a
// corpus unit") is released HERE - TAG CORRECTION: THIS roster
// row owns compiler-sys1.c::mult; the Cannon stub is deleted with
// a release note and the body lands at the same (a, b, c)
// signature the Cannon call site binds.
fun multDaf(a: CharArray, b: CharArray, c: CharArray) {
    val aa = DafCRuntime.strtod(DafCRuntime.cstring(a)).toFloat()
    val bb = DafCRuntime.strtod(DafCRuntime.cstring(b)).toFloat()
    val cc = aa * bb
    gcvtD(cc.toDouble(), 7, c)
    return
}
