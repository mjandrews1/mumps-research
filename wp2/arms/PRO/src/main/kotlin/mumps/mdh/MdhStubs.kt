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
}

/**
 * PRO-arm mdh-side out-of-scope stubs (h014 out_of_scope_call).
 * Each stub names its owner; delegate when the owner lands.
 */

/** Owner: interp-bifs.cpp::zfcn (not a corpus unit). */
fun zfcn(v1d: CharArray, bd: CharArray, svPtr: Msv) {
    TODO("h014 stub: owner interp-bifs.cpp::zfcn (not a corpus unit)")
}
