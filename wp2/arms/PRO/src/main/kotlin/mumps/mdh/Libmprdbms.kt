package mumps.mdh

import mumps.interp.Msv

/**
 * PRO-arm translation of include/mumpsc/libmpsrdbms.h (pilot B1,
 * unit 9 — the RDBMS macro shims and the library declarations).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h003 state_macro        : ADAPTED. The vendor macros
 *    (SELECT(arr,out,exp) etc.) expand to calls that splice in the
 *    file-scope svPtr. Kotlin has no capture-by-name macros, and
 *    svPtr is a pointer value rather than addressable singleton state,
 *    so each shim takes svPtr as an explicit leading parameter instead
 *    of becoming a member function. Logged hit, not reuse.
 *  - h014 out_of_scope_call  : Select/Print/Union/Project/Subtract/
 *    Intersect/Join/TablePrint — the RDBMS library implementation is
 *    not part of the corpus (no rdbms .cpp in the component) -> owner-
 *    named stubs.
 *  - h017 naming             : macro and function names keep vendor
 *    case (Select, SELECT, ...).
 */

/** Owner: RDBMS library implementation (not in the corpus). */
fun Select(svPtr: Msv, table: String, arr: String, out: String, exp: String): String {
    TODO("h014 stub: owner RDBMS library (libmpsrdbms implementation, not in corpus)")
}

/** Owner: RDBMS library implementation (not in the corpus). */
fun Print(svPtr: Msv, table: String, arr: String, exp: String): String {
    TODO("h014 stub: owner RDBMS library (libmpsrdbms implementation, not in corpus)")
}

/** Owner: RDBMS library implementation (not in the corpus). */
fun Union(svPtr: Msv, table: String, arr1: String, arr2: String, out: String): String {
    TODO("h014 stub: owner RDBMS library (libmpsrdbms implementation, not in corpus)")
}

/** Owner: RDBMS library implementation (not in the corpus). */
fun Project(svPtr: Msv, table: String, arr: String, out: String, cols: String): String {
    TODO("h014 stub: owner RDBMS library (libmpsrdbms implementation, not in corpus)")
}

/** Owner: RDBMS library implementation (not in the corpus). */
fun Subtract(svPtr: Msv, table: String, arr: String, sub: String, out: String): String {
    TODO("h014 stub: owner RDBMS library (libmpsrdbms implementation, not in corpus)")
}

/** Owner: RDBMS library implementation (not in the corpus). */
fun Intersect(svPtr: Msv, table: String, arr: String, sub: String, out: String): String {
    TODO("h014 stub: owner RDBMS library (libmpsrdbms implementation, not in corpus)")
}

/** Owner: RDBMS library implementation (not in the corpus). */
fun Join(svPtr: Msv, table: String, arr1: String, arr2: String, out: String, exp: String): String {
    TODO("h014 stub: owner RDBMS library (libmpsrdbms implementation, not in corpus)")
}

/** Owner: RDBMS library implementation (not in the corpus). */
fun TablePrint(svPtr: Msv, table: String, arr: String, out: String, exp: String): String {
    TODO("h014 stub: owner RDBMS library (libmpsrdbms implementation, not in corpus)")
}

// Vendor macro shims (h003, adapted — svPtr spliced in by the C macros
// becomes an explicit parameter here):

/** Vendor: SELECT(arr,out,exp) -> Select(svPtr,"",arr,out,exp) */
fun SELECT(svPtr: Msv, arr: String, out: String, exp: String): String =
    Select(svPtr, "", arr, out, exp)

/** Vendor: PRINT(arr,exp) -> Print(svPtr,"",arr,exp) */
fun PRINT(svPtr: Msv, arr: String, exp: String): String =
    Print(svPtr, "", arr, exp)

/** Vendor: UNION(arr1,arr2,out) -> Union(svPtr,"",arr1,arr2,out) */
fun UNION(svPtr: Msv, arr1: String, arr2: String, out: String): String =
    Union(svPtr, "", arr1, arr2, out)

/** Vendor: PROJECT(arr,out,cols) -> Project(svPtr,"",arr,out,cols) */
fun PROJECT(svPtr: Msv, arr: String, out: String, cols: String): String =
    Project(svPtr, "", arr, out, cols)

/** Vendor: SUBTRACT(arr,sub,out) -> Subtract(svPtr,"",arr,sub,out) */
fun SUBTRACT(svPtr: Msv, arr: String, sub: String, out: String): String =
    Subtract(svPtr, "", arr, sub, out)

/** Vendor: INTERSECT(arr,sub,out) -> Intersect(svPtr,"",arr,sub,out) */
fun INTERSECT(svPtr: Msv, arr: String, sub: String, out: String): String =
    Intersect(svPtr, "", arr, sub, out)

/** Vendor: JOIN(arr1,arr2,out,exp) -> Join(svPtr,"",arr1,arr2,out,exp) */
fun JOIN(svPtr: Msv, arr1: String, arr2: String, out: String, exp: String): String =
    Join(svPtr, "", arr1, arr2, out, exp)
