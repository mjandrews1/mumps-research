package both

// Unit 208 render: libmpscpp.cpp::GlobalData (roster 208,
// batch 7, 17 LOC; units.csv rank 79). The mstring overload
// of the \u0024data evaluator: builds "s %=\u0024data(<a>)",
// runs it through xecute, and reads the result back from the
// "%" symbol via SymGet (the tpx success/fail contract).
//
// MULTI-ROW NAME (the GlobalData family, journaled): the
// vendor seats THREE libmpscpp.cpp overloads - char* :2595
// (unit 86, rank 76, batch 3 pending), mstring :2616 (THIS
// unit-208 corpus seat), string :2636 (unit 237, rank 78,
// batch 8 pending). All three are the SAME BODY family (the
// Interpret/xecute re-invoker, distinct from the unit-203
// interp-sysfunc.cpp::GlobalData engine that globalDataC
// folds onto - which the arm seats at Mglobal level). The
// char* and mstring bodies are identical modulo the c_str()
// adapter (checked); the string twin differs only in the
// parameter type. When units 86/237 land, the same-body fold
// adjudicates (the Sim1W standing rule).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: xecute and SymGet are
//     seated TODO stubs (BothCRuntime; both pinned not-a-
//     corpus-unit, libmpscpp.cpp:7053 / libmpscpp.h:179) -
//     called direct, stub-blocked until their owners land.
//     atolC direct.
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline; the intrinsic
//     dollars ride \u0024 escapes (Kotlin template hazard).
//   h017 naming - reused: GlobalData keeps vendor name/case
//     as the String-overload (no Kotlin collision: the unit-
//     203 seat is the CharArray/CharArray/Msv signature; the
//     FILE is GlobalDataMstring.kt, the GlobalData.kt name is
//     taken by unit 203 - the unit-197 collision lesson).
//   h019 class_member_carrier - reused: the TU-static svPtr is
//     the BothCRuntime singleton; tpx rides the Msv seat.
//
// Quirks kept (ledgered):
//   - the expression embeds the argument UNQUOTED
//     ("s %=\u0024data(" + a + ")") - a caller-supplied a with
//     quotes/code injects into the executed line (the vendor
//     is an injection-by-design shape; kept exact).
//   - the fail arm sets tpx=0 and returns -1 (the error
//     contract distinct from the \u0024data codes).
//   - the 32-byte-ish mstring t folds to the value flow.

// int GlobalData(mstring a) - libmpscpp.cpp:2616
internal fun GlobalData(a: String): Int {
    // char tmp[STR_MAX]="s %=$data("; strcat(tmp,a); strcat(tmp,")")
    val exp = "s %=\u0024data(" + a + ")"

    return if (xecute(exp) == 0) {
        svPtr.tpx = 1
        // t = SymGet("%"); i = atoi(t.c_str())
        atolC(SymGet("%")).toInt()
    } else {
        svPtr.tpx = 0
        -1
    }
}
