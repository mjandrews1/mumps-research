package mumps.mdh

import mumps.interp.Msv

/**
 * Opaque PRO-arm stand-in for the GTK type GtkTreeStore (GUI glue;
 * the mdh functions only forward an opaque handle to the $z dispatcher).
 */
class GtkTreeStore

/**
 * PRO-arm mdh-side owner object for libmpscpp.cpp's file-scope globals
 * (h004 globals_ext). svPtr is the shared interpreter state pointer.
 */
object MdhState {
    lateinit var svPtr: Msv

    /** Owner: libmpscpp.cpp file-scope residue (B1 unit 31, misc-1): short ierr1. */
    var ierr1: Short = 0

    /**
     * Owner: libmpscpp.cpp file-scope residue (B1 unit 31, misc-1):
     * int GlobalExceptionCode = UNSPECIFIED (libmpscpp.h:69: 0).
     */
    var globalExceptionCode: Int = 0
}

/**
 * PRO-arm mdh-side out-of-scope stubs (h014 out_of_scope_call).
 * Each stub names its owner; delegate when the owner lands.
 */

/** Owner: interp-bifs.cpp::zfcn (not a corpus unit). */
fun zfcn(v1d: CharArray, bd: CharArray, svPtr: Msv) {
    TODO("h014 stub: owner interp-bifs.cpp::zfcn (not a corpus unit)")
}

/**
 * Owner: interp-sym.cpp / sym.c symbol-table primitive
 * sym_(fn,key,bd,svPtr) (not a corpus unit).
 * Return type promoted from Unit to CharArray? (B1 unit 25,
 * LocalGet tests sym_(11,...)==NULL; owner semantics: NULL when the
 * variable is absent, bd pointer otherwise).
 */
fun sym_(fn: Int, key: CharArray, bd: CharArray, svPtr: Msv): CharArray? {
    TODO("h014 stub: owner sym_ symbol-table machinery (not a corpus unit)")
}

/** Owner: libmpscpp.cpp::strcatx - appends src with the vendor x01 subscript separator (not a corpus unit). */
fun strcatx(dst: CharArray, src: CharArray) {
    TODO("h014 stub: owner libmpscpp.cpp::strcatx (not a corpus unit)")
}

/** Owner: libmpscpp.cpp::lhash - disk-hash function used by Hash (misc-2 residue, not a B1 corpus unit; B1 unit 31). */
fun lhash(ref: String): Long {
    TODO("h014 stub: owner libmpscpp.cpp::lhash (not a corpus unit)")
}

/**
 * Owner: libmpscpp.cpp::global::MakeGlobal (batch-7 unit).
 * C variadic MakeGlobal(int, char *gblnme, char *key, ...) ->
 * vararg values (B1 unit 31).
 */
fun makeGlobal(x: Int, gblnme: String, key: String, vararg vals: String) {
    TODO("h014 stub: owner libmpscpp.cpp::global::MakeGlobal (batch-7 unit)")
}

/** Owner: libmpscpp.cpp::__finish - atexit target of the global constructor (misc-2 residue, not a B1 corpus unit; B1 unit 31). */
fun __finish() {
    TODO("h014 stub: owner libmpscpp.cpp::__finish (not a corpus unit)")
}

/** Owner: libmpscpp.cpp::thrownMessage - set_terminate target of the global constructor (misc-2 residue, not a B1 corpus unit; B1 unit 31). */
fun thrownMessage() {
    TODO("h014 stub: owner libmpscpp.cpp::thrownMessage (not a corpus unit)")
}

/**
 * Owner: libmpscpp.cpp global implicit-conversion operators
 * (operator int/long/... via globalClass.h; not in the B1 corpus).
 * The C++ per-type conversions collapse to one Double-valued stub
 * (B1 unit 31, see journal).
 */
fun globalNumericValue(j: Global): Double {
    TODO("h014 stub: owner libmpscpp.cpp global implicit conversion operators (not a corpus unit)")
}

/** Owner: libmpscpp.cpp/globalClass.h global arithmetic - global + int (not in the B1 corpus; B1 unit 31 unary operator+). */
fun globalPlusInt(g: Global, i: Int): Mstring {
    TODO("h014 stub: owner globalClass.h global arithmetic operators (not a corpus unit)")
}

/** Owner: libmpscpp.cpp/globalClass.h global arithmetic - int - global (not in the B1 corpus; B1 unit 31 unary operator-). */
fun intMinusGlobal(i: Int, g: Global): Mstring {
    TODO("h014 stub: owner globalClass.h global arithmetic operators (not a corpus unit)")
}

private const val MUMPS_GLOBAL_EXCEPTION_MESSAGE = "Global reference not found"

/**
 * Carrier for the vendor header class MumpsGlobalException
 * (libmpscpp.h:332; not a corpus unit). Thrown by the global members
 * when Mglobal cannot find the reference (B1 unit 31 added the
 * globalExceptionCode carrier; B2 units 32+ throw this).
 */
class MumpsGlobalException : RuntimeException(MUMPS_GLOBAL_EXCEPTION_MESSAGE) {
    /** Vendor: char *what(). */
    fun what(): String = MUMPS_GLOBAL_EXCEPTION_MESSAGE
}

/** Owner: interp-arith.cpp::Mltoa (batch-10 unit). */
fun Mltoa(n: Long, dst: CharArray) {
    TODO("h014 stub: owner interp-arith.cpp::Mltoa (batch-10 unit)")
}

/** Owner: interp-sym.cpp::_extract (batch-10 unit). */
fun _extract(r: CharArray, src: CharArray, a: CharArray, b: CharArray) {
    TODO("h014 stub: owner interp-sym.cpp::_extract (batch-10 unit)")
}

/** Owner: include/mumpsc/keyfix.h inline keyfix (not a corpus unit). */
fun keyfix(key: CharArray) {
    TODO("h014 stub: owner keyfix.h inline keyfix (not a corpus unit)")
}

/** Owner: include/mumpsc/inline.h:38 strmove (not a corpus unit). */
fun strmove(a: CharArray, b: CharArray) {
    TODO("h014 stub: owner inline.h:38 strmove (not a corpus unit)")
}
