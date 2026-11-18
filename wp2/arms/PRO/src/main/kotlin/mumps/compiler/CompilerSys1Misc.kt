package mumps.compiler

/**
 * PRO-arm translation of compiler-sys1.c::<misc> (roster row 258,
 * batch 9; units.csv: loc 107, comment_ratio 0.13,
 * pointer_marks 3, control_marks 6, score 28.9, rank 204).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 1-64 (banner/prologue) plus the non-unit inline
 * helpers :801-816 (sigc/itoa), :277-291 (lpad), :949-970
 * (linepad/linepad1/linepad2, mult prototype) and :1061-1090
 * (the GCVT gcvt arm and the float mult twin) - the residual
 * chunk of compiler-sys1.c after the MyFunc (u6), patrn1 (u7),
 * inout (u36), patrn (u69), errmod_ (u70), cannon (u73),
 * readf_ (u79), match (u190), substr (u213), getstr1 (u229)
 * and xindex (row 297, batch 10) units.
 *
 * Residue contents, each line mapped onto its seat:
 *  - :1-25 the GPL banner - kept verbatim in prior residue
 *    renders (CompilerAbrtxMisc precedent; roster note here).
 *  - :42 #define Sysout(x) fprintf(Out,x) - CompilerState.out
 *    (Writer) consumes; the macro folds per-unit (h009 notes).
 *  - :44-58 the includes; GBLROOT 30; the CRTN/CTRLZ/TAB/
 *    LINEFEED escape defines (compiler-arm constants ride the
 *    per-unit seats: LINEFEED at the u229 getstr1 seat);
 *    mumpsc/externs.h -> ExternsH.kt roster.
 *  - :60 extern FILE *Out - CompilerState.out (h004 carrier).
 *  - :62 linepad prototype - the call sites bind to the
 *    Stubs.kt linepad seat (owner compiler-read.c; the
 *    compiler-sys1 twin body :949-953 is identical in
 *    emissions, kept as a roster note - the two-file duplicate
 *    follows the u217 SQL_Table twin precedent).
 *  - :63 cannon prototype - the compiler-sys1 cannon body
 *    IS the u73 corpus unit (B3 seat, CannonAux at compiler
 *    package); roster note only.
 *  - :968-969 void mult prototype + the linepad2 seat below.
 *  - :1058-1059 NO/YES defines - kept as a roster note (the
 *    YES inside MyFunc rode the u6 seat).
 *  - :1061-1072 the GCVT arm: the reference build defines GCVT
 *    (numSetup precedent, u29) so the LOCAL gcvt is live -
 *    RENDERED below as the compiler-sys1 TU-local arm.
 *  - :1075-1090 void mult(a,b,c) - the float-octave twin of
 *    the arbitrary-precision interp mult - RENDERED below
 *    (only this TU's float callers bind).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string       : sprintf(str,"%d",val) and the
 *    "%g" gcvt arm fold onto String templates written back
 *    into the NUL-domain CharArrays.
 *  - h012 unsigned_byte_domain: lpad takes NUL-domain
 *    CharArrays; the short l/j/i/k locals map to Int.
 *  - h013 dead_store          : linepad's dead locals (i, j,
 *    f=0) kept as comments at the compiler-read twin roster.
 *  - h014 out_of_scope_call   : linepad1's dot padding walks
 *    CompilerState.out writes; gcvt is TU-local (no InterpStubs
 *    re-bind); memcpy folds onto char loops.
 *  - h015 grammar_collision   : itoa/mult/gcvt keep the vendor
 *    names (package placement avoids the stdlib collision).
 *  - h017 naming          : every helper keeps its vendor name.
 *  - h018 preprocessor_conditional: GCVT is IN for the reference
 *    build (u29 precedent); the #else gcvt (libc link) excluded.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - lpad writes the right pad first then the blanks, exactly
 *    as written (the overlapping index order kept).
 *  - mult's gcvt(cc, 7, c) digits argument is ignored by the
 *    local gcvt arm (the vendor's "%g" ignores i) - kept.
 *  - itoa's rad argument is unused - kept (the vendor signature
 *    carried).
 */

/** C :801: int sigc() - the SIGINT catcher sets ctrlc. */
fun sigc(): Int {
    CompilerState.ctrlc = 1
    return 1
}

/** C :808: void itoa(int val, char *str, int rad); rad unused. */
fun itoaC(val_: Int, str: CharArray, rad: Int) {
    val s = val_.toString() // sprintf(str,"%d",val)
    for (k in s.indices) str[k] = s[k]
    str[s.length] = NUL
}

/**
 * C :277: void lpad(strin, strout, l) - left-blank pad to width
 * l (K&R: unsigned char strin[], strout[]; short l).
 */
fun lpad(strin: CharArray, strout: CharArray, l: Int) {
    val j = strlenC(strin) - 1
    val k = l - j - 1
    if (k <= 0) return
    var i = j
    while (i >= 0) {
        strout[i + k] = strin[i]
        i--
    }
    strout[j + k + 1] = NUL
    for (i in 0 until k) strout[i] = ' '
}

/**
 * C :955: void linepad1(char *str) - open a C comment banner
 * (LSLASH + str) and dot-pad to column 25 (the emitted C
 * prologue header).
 */
fun linepad1(str: String) {
    val out = CompilerState.out
    out.write("/* ")
    var i = str.length + 4 // i = fprintf(Out,"(LSLASH)%s ",str)
    while (i < 25) {
        out.write(".")
        i++
    }
    out.write("*/ ")
}

/**
 * C :963: void linepad2(char *str) - the emitted C banner line
 * (RESOLVES the Stubs.kt linepad2 stub; the Fcninit.kt call
 * site binds unchanged).
 */
fun linepad2(str: String) {
    CompilerState.out.write("/*** " + str + " */\n")
}

/**
 * C :1064: void gcvt(double x, int i, char a[]) - the GCVT-arm
 * TU-local "%g" fold (i ignored, vendor kept).
 */
fun gcvtC(x: Double, i: Int, a: CharArray) {
    val s = java.lang.String.format(java.util.Locale.US, "%g", x) // sprintf "%g"
    for (k in s.indices) a[k] = s[k]
    a[s.length] = NUL
}

/** C :1075: void mult(char a[], char b[], char c[]) - float arm. */
fun multC(a: CharArray, b: CharArray, c: CharArray) {
    val aa = cstringAt(a).toFloat() // atof fold (h014)
    val bb = cstringAt(b).toFloat()
    val cc = aa * bb
    gcvtC(cc.toDouble(), 7, c)
}
