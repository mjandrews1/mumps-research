package mumps.interp

/**
 * PRO-arm interpreter-side carrier for struct MSV (vendor
 * include/mumpsc/... state struct). Not itself a corpus unit; properties
 * are added per translated unit as needed (same one-owner policy as
 * the compiler side's CompilerState, rules_v0.pl h004).
 */
class Msv

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
