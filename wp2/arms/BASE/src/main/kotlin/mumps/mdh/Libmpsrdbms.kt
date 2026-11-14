package mumps.mdh

import mumps.interp.MsvState

/**
 * Kotlin translation of include/mumpsc/libmpsrdbms.h (Study Subset unit 9,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * The header contains two things: call-shaping macros that inject the
 * library-global svPtr and an empty second arg, and prototypes for the
 * relational operators (whose bodies live in libmpsrdbms.cpp, outside
 * this batch -> TODO stubs).
 *
 * The macro call shape is preserved EXACTLY: Kotlin allows SELECT and
 * Select to coexist, so C call sites translate without renaming.
 */

// --- prototypes (C: char * Xxx(struct MSV *, char *, const char * ...)) ---

fun Select(svPtr: MsvState, a: String, arr: String, out: String, exp: String): String =
    TODO("libmpsrdbms.cpp not yet translated (outside batch 1)")

fun Print(svPtr: MsvState, a: String, arr: String, exp: String): String =
    TODO("libmpsrdbms.cpp not yet translated (outside batch 1)")

fun Union(svPtr: MsvState, a: String, arr1: String, arr2: String, out: String): String =
    TODO("libmpsrdbms.cpp not yet translated (outside batch 1)")

fun Project(svPtr: MsvState, a: String, arr: String, out: String, cols: String): String =
    TODO("libmpsrdbms.cpp not yet translated (outside batch 1)")

fun Subtract(svPtr: MsvState, a: String, arr: String, sub: String, out: String): String =
    TODO("libmpsrdbms.cpp not yet translated (outside batch 1)")

fun Intersect(svPtr: MsvState, a: String, arr: String, sub: String, out: String): String =
    TODO("libmpsrdbms.cpp not yet translated (outside batch 1)")

fun Join(svPtr: MsvState, a: String, arr1: String, arr2: String, out: String, exp: String): String =
    TODO("libmpsrdbms.cpp not yet translated (outside batch 1)")

fun TablePrint(svPtr: MsvState, a: String, arr: String, b: String, exp: String): String =
    TODO("libmpsrdbms.cpp not yet translated (outside batch 1)")

// --- call-shaping macros (C: #define SELECT(arr,out,exp) Select(svPtr,"",arr,out,exp)) ---

fun SELECT(arr: String, out: String, exp: String): String = Select(MdhRuntime.svPtr, "", arr, out, exp)
fun PRINT(arr: String, exp: String): String = Print(MdhRuntime.svPtr, "", arr, exp)
fun UNION(arr1: String, arr2: String, out: String): String = Union(MdhRuntime.svPtr, "", arr1, arr2, out)
fun PROJECT(arr: String, out: String, cols: String): String = Project(MdhRuntime.svPtr, "", arr, out, cols)
fun SUBTRACT(arr: String, sub: String, out: String): String = Subtract(MdhRuntime.svPtr, "", arr, sub, out)
fun INTERSECT(arr: String, sub: String, out: String): String = Intersect(MdhRuntime.svPtr, "", arr, sub, out)
fun JOIN(arr1: String, arr2: String, out: String, exp: String): String = Join(MdhRuntime.svPtr, "", arr1, arr2, out, exp)
