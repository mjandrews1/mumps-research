package daf

/*
 * DAF batch-9 unit 266 (roster row 266; interp-sym.cpp::<misc>,
 * residue, LOC 75, score 11.2). Consult 2026-11-24T22:44:00Z.
 *
 * This roster row is the interp-sym.cpp FILE PRELUDE plus the one
 * fold-in body the chunker appended at its tail (_ascii).
 *
 * PRELUDE LEDGER (no new code from the prelude: every name already
 * rides a landed seat): the Dec 13 2025 date line, the SQLITE
 * arm define (unit-152 allegiance pin precedent), SYMDEBUG
 * comment-only (H-018 fossil note), the include ladder, the
 * _INTERP_/CSTR/USTR micro-defines, intLong (64-bit default,
 * H-018 fossil note), the Interpret/ScanParse/Eval prototypes
 * (InterpretDaf unit-208; scanParseDaf unit-229; evalDaf released
 * THIS batch at unit 264), the __label_lookup/__text_function
 * extern seats (the DO/goto family; the vendor's commented-out
 * defaults below them are kept as this ledger note), global.h
 * (the DafMsv/stateVector.h fold), _getpid/time externs (the
 * System time seats of DafCRuntime), and the section-banner
 * comment lines for sym_/BuildLocal/_SymFree/evaluate/_ascii/
 * _extract/_find/_horolog/_justify (may be dead)/_length/_random
 * (the named owners of the corresponding roster rows; _justify is
 * the unit-194 InterpJustify render, BuildLocal the unit-231
 * release). The vendor's commented-out block // char *
 * _text_function(int i) ... / int _label_lookup(char * x) ...
 * (the static default bindings, disabled in source) is kept as
 * this ledger note only (H-018).
 *
 * FOLD-IN BODY: _ascii (the ZfcnsH unit-221 forward-tag flips
 * HERE): out/in/begin carriers; i = atoi(begin) with the i<1 ->
 * 1 floor; the i > strlen(in) -> "-1" early return kept; the
 * sprintf(out,"%d",in[i-1]) seat prints the UNSIGNED byte value
 * (0..255, the %d of the C unsigned char; DOCUMENTED DIVERGENCE:
 * the C signed-char platforms would print negatives, the vendor
 * declares unsigned).
 * H-015 hygiene: no raw dollars ride the comments.
 * Dafny: EXEMPT (fold-in strmanip helper; journal row).
 */

fun asciiDaf(out: CharArray, in_: CharArray, begin: CharArray) {
    var i = atoi(DafCRuntime.cstring(begin))
    if (i < 1) i = 1

    // strlen(in)
    var n = 0
    while (in_[n] != NUL_D) n++

    if (i > n) {
        val e = "-1"
        var q = 0
        while (q < e.length) { out[q] = e[q]; q++ }
        out[q] = NUL_D
        return
    }

    val s = "%d".format(java.util.Locale.US, in_[i - 1].code)
    var q = 0
    while (q < s.length) { out[q] = s[q]; q++ }
    out[q] = NUL_D
    return
}
