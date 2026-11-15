package mumps.interp

/**
 * PRO-arm interpreter-side carrier for struct MSV (vendor
 * include/mumpsc/... state struct). Not itself a corpus unit; properties
 * are added per translated unit as needed (same one-owner policy as
 * the compiler side's CompilerState, rules_v0.pl h004).
 */
class Msv {
    /** mumps.cpp main: svPtr->program - strncpy'd program name, 512. */
    var program: CharArray = CharArray(512)
}

/**
 * PRO-arm interpreter-side out-of-scope stubs (h014 out_of_scope_call).
 * Each stub names its owner; delegate when the owner lands.
 */
object InterpStubs

/** Owner: interp-native-btree.cpp::btreeX (same file, not a corpus unit). */
fun btreeX(g: Int, key: CharArray, bd: CharArray, svPtr: Msv): Int {
    TODO("h014 stub: owner interp-native-btree.cpp::btreeX (not a corpus unit)")
}

/** Owner: interp-strmanip.cpp::xindex (not a corpus unit). */
fun xindex(source: CharArray, ptrn: CharArray, strt: Short): Int {
    TODO("h014 stub: owner interp-strmanip.cpp::xindex (not a corpus unit)")
}

/**
 * Owner: gcvt_wrapper macro (interp-arith.cpp lines 69-77, config-
 * dependent; reference build: gcvt(x, 8, a)). Delegate when B1 unit 29
 * (include/mumpsc/numSetup.h) lands.
 */
fun gcvtWrapper(x: Double, a: CharArray) {
    TODO("h014 stub: owner gcvt_wrapper macro (unit 29 numSetup.h)")
}

/** Owner: interpreter core - Interpret(cmd, svPtr) (not a corpus unit). */
fun interpret(cmd: String, svPtr: Msv): Int {
    TODO("h014 stub: owner interpreter core (Interpret, not a corpus unit)")
}

/** Owner: global-array machinery Mglobal(g,key,bd,svPtr) (not a corpus unit). */
fun mglobal(g: Int, key: CharArray, bd: CharArray, svPtr: Msv): Int {
    TODO("h014 stub: owner Mglobal machinery (not a corpus unit)")
}

/** Owner: symbol-table machinery - vendor $SymPut macro (not a corpus unit). */
fun symPut(svPtr: Msv, name: String, value: String) {
    TODO("h014 stub: owner \$SymPut/SymPut machinery (not a corpus unit)")
}

/** Owner: global-array lock service cleanLocks(pid) (declared mumps.cpp). */
fun cleanLocks(pid: String) {
    TODO("h014 stub: owner cleanLocks (global array lock service)")
}

/** Owner: vendor GlobalClose macro (not a corpus unit). */
fun globalClose(svPtr: Msv) {
    TODO("h014 stub: owner GlobalClose macro (not a corpus unit)")
}

/** Owner: interp-sysfunc.cpp (B1 unit 25 interp-sysfunc.cpp::<misc>; declared sym.h:41). */
fun sigint(i: Int) {
    TODO("h014 stub: owner interp-sysfunc.cpp::sigint (B1 unit 25 interp-sysfunc.cpp::<misc>)")
}
