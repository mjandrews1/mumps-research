package doc

// Unit 223 (B8 tranche 1): libmpscpp.cpp::IDF (batch 8, simple
// stratum; LOC 15, score 6.7, rank 42; units.csv row 240; vendor
// libmpscpp.cpp:10370, physically verified this session) - the
// FREE IDF wrapper (the builtin-function dispatcher entry the
// builtinTable.h roster names at caret-IDF), distinct from the
// member global::IDF of the unit-49 render. Consult H-009
// hit-reuse: the sscanf-percent-lf contract renders on the
// arm-wide C-protocol strtod (MpsExp.kt) plus the unit-49 member's
// semantic check that the field carries a value (INVALID_CONVERSION
// raised on the empty needle - the documented divergence: strtod
// never signals failure itself). Body: SOH scan of arg1 for the
// subscript separator (the top-level-global guard), banner and
// sigint(100) on SOH (the Sigint.kt real, batch-7 flip), arg1
// truncation at the scan index, the caret-bypassing global
// constructor from and-index-1, the subscript bind through the
// unit-31 invoke family (the CHAR-STAR single-arg overload is the
// MISSING arity: the family renders Mstring arities only, so this
// push adds the String overload beside the family in LibMisc1.kt
// with this unit as its owner note - MakeGlobal fold unchanged),
// the member IDF call (u49 seat), empty-string return of the
// vendor's static "" literal. svptr and ep are unused locals of
// the dispatcher convention (H-013: kept as parameters, no
// comment-fossil needed). Naming per H-017 (IDF kept: the vendor
// name is the dispatcher table key).

fun IDF(sv: Msv, ep: String, arg1: CharArray, arg2: CharArray): String {
    // static char tmp[32]: never read (H-013 drop, kept here as
    // this comment ledger)

    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == '\u0001') break // SOH subscript separator
        i++
    }

    if (arg1[i] == '\u0001') {
        System.out.print("\n*** Arg 1 must be top level global\n")
        sigint(100)
    }

    arg1[i] = NUL

    // global a(&arg1[1]); // bypass ^
    val a = GlobalRef(CstringOps.cstringAt(arg1, 1))

    // sscanf(arg2, "%lf", &x): strtod protocol plus the unit-49
    // empty-field semantic check.
    val arg2Str = CstringOps.cstring(arg2)
    if (arg2Str.isEmpty()) {
        Globals.GlobalExceptionCode = INVALID_CONVERSION
        throw MumpsGlobalException()
    }
    val x = strtod(arg2Str)

    a(CstringOps.cstringAt(arg1, i + 1)).IDF(x)

    return ""
}
