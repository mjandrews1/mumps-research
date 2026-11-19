package doc

// Unit 9 (B1 tranche 2): include/mumpsc/libmpsrdbms.h::<misc> (batch
// 1, simple stratum; LOC 18, score 8.4). Whole-header residual ledger:
// the GPL banner, then the seven SELECT/PRINT/UNION/PROJECT/SUBTRACT/
// INTERSECT/JOIN macro definitions and the eight rdbms prototypes.
// H-003 adapted (no capture-by-name macros in Kotlin; svPtr splices as
// an explicit leading parameter, ARM journal precedent for this exact
// header): each macro maps from its bare macro syntax to the function
// with the empty tag string the vendor macro supplies. The prototype
// roster renders as H-014 stub seats naming the vendor definition file
// (rdbms-family, no corpus unit). Naming per H-015/H-017: rdbms prefix
// avoids collision with any same-shaped names elsewhere in the arm.

// ---- the seven macro shims (H-003: explicit svPtr parameter) ----

fun rdbmsSelectMacro(sv: Msv, arr: String, out: String, exp: String): String? =
    rdbmsSelect(sv, "", arr, out, exp)

fun rdbmsPrintMacro(sv: Msv, arr: String, exp: String): String? =
    rdbmsPrint(sv, "", arr, exp)

fun rdbmsUnionMacro(sv: Msv, arr1: String, arr2: String, out: String): String? =
    rdbmsUnion(sv, "", arr1, arr2, out)

fun rdbmsProjectMacro(sv: Msv, arr: String, out: String, cols: String): String? =
    rdbmsProject(sv, "", arr, out, cols)

fun rdbmsSubtractMacro(sv: Msv, arr: String, sub: String, out: String): String? =
    rdbmsSubtract(sv, "", arr, sub, out)

fun rdbmsIntersectMacro(sv: Msv, arr: String, sub: String, out: String): String? =
    rdbmsIntersect(sv, "", arr, sub, out)

fun rdbmsJoinMacro(sv: Msv, arr1: String, arr2: String, out: String, exp: String): String? =
    rdbmsJoin(sv, "", arr1, arr2, out, exp)

// ---- the prototype seats (H-014: definitions live in the vendor
// ---- rdbms family, not in any corpus unit) ----

fun rdbmsSelect(sv: Msv, tag: String, arr: String, out: String, exp: String): String? =
    throw NotImplementedError("Select: out-of-scope owner (vendor rdbms implementation)")

fun rdbmsPrint(sv: Msv, tag: String, arr: String, exp: String): String? =
    throw NotImplementedError("Print: out-of-scope owner (vendor rdbms implementation)")

fun rdbmsUnion(sv: Msv, tag: String, arr1: String, arr2: String, out: String): String? =
    throw NotImplementedError("Union: out-of-scope owner (vendor rdbms implementation)")

fun rdbmsProject(sv: Msv, tag: String, arr: String, out: String, cols: String): String? =
    throw NotImplementedError("Project: out-of-scope owner (vendor rdbms implementation)")

fun rdbmsSubtract(sv: Msv, tag: String, arr: String, sub: String, out: String): String? =
    throw NotImplementedError("Subtract: out-of-scope owner (vendor rdbms implementation)")

fun rdbmsIntersect(sv: Msv, tag: String, arr: String, sub: String, out: String): String? =
    throw NotImplementedError("Intersect: out-of-scope owner (vendor rdbms implementation)")

fun rdbmsJoin(sv: Msv, tag: String, arr1: String, arr2: String, out: String, exp: String): String? =
    throw NotImplementedError("Join: out-of-scope owner (vendor rdbms implementation)")

fun rdbmsTablePrint(sv: Msv, tag: String, arr: String, exp: String, exp2: String): String? =
    throw NotImplementedError("TablePrint: out-of-scope owner (vendor rdbms implementation)")
