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

    // --- stateVector.h fields added by B1 unit 23 (libmpscpp.cpp::Input) ---
    /** stateVector.h:49 - int io. */
    var io: Int = 0
    /** :52 - int symflg. */
    var symflg: Int = 0
    /** :61 - FILE *in_file[10], *out_file[10] (Reader/Writer at edges, h001). */
    val in_file: Array<java.io.Reader?> = arrayOfNulls(10)
    val out_file: Array<java.io.Writer?> = arrayOfNulls(10)
    /** :64 - long tpx. */
    var tpx: Long = 0

    // --- stateVector.h fields added by B1 unit 25 (interp-sysfunc.cpp::<misc>) ---
    /** stateVector.h:63 - long LineNumber. */
    var LineNumber: Long = 0
    /** :70 - int gpad. */
    var gpad: Int = 0
    /** :77 - int order. */
    var order: Int = 0

    // --- stateVector.h fields added by B1 unit 28 (interp-sym.cpp::_SymFree) ---
    /** :65 - int _Sym (symbol table stack depth). */
    var _Sym: Int = 0
    /** :71 - struct stab *start[SYM_MAX] (SYM_MAX=200, defines.h:218); stab carrier in SymFree.kt. */
    val start: Array<Stab?> = arrayOfNulls(200)

    // --- stateVector.h fields added by B1 unit 30 (interp-parse.cpp::patrn1) ---
    /** :42 - unsigned char bd[STR_MAX] (h012 byte domain; STR_MAX=32000, strmax.h:3). */
    val bd: CharArray = CharArray(32000)
    /** :44 - unsigned char v1d[STR_MAX] (h012 byte domain; added by B2 unit 42, mstring::Fnumber). */
    val v1d: CharArray = CharArray(32000)
    /** :45 - unsigned char xd[STR_MAX] (h012 byte domain). */
    val xd: CharArray = CharArray(32000)
    /** :56 - int xpx. */
    var xpx: Int = 0
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
 * gcvt_wrapper (interp-arith.cpp:69-77) was stubbed here until B1
 * unit 29 (include/mumpsc/numSetup.h) landed; the owner-level
 * definition now lives in NumSetup.kt (gcvtWrapper, gcvt(x, 8, a)
 * reference arm) and the stub has been removed.
 */

/** Owner: interpreter core - Interpret(cmd, svPtr) (not a corpus unit). */
/** Owner: interp-fcn.cpp::fcn (batch-4 unit, 775 LOC). */
fun fcn(svPtr: Msv) {
    TODO("h014 stub: owner interp-fcn.cpp::fcn (batch-4 unit)")
}

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

/** Owner: vendor GlobalClose macro (not a corpus unit). */
fun globalClose(svPtr: Msv) {
    TODO("h014 stub: owner GlobalClose macro (not a corpus unit)")
}

/** Owner: interp-sysfunc.cpp (B1 unit 25 interp-sysfunc.cpp::<misc>; declared sym.h:41). */
fun sigint(i: Int) {
    TODO("h014 stub: owner interp-sysfunc.cpp::sigint (batch-7 unit, not in B1 - see ARM_WORKING_NOTES)")
}

/** Owner: interp-sysfunc.cpp::unpad (batch-10 unit, not in B1). */
fun unpad(key: CharArray, svPtr: Msv) {
    TODO("h014 stub: owner interp-sysfunc.cpp::unpad (batch-10 unit, not in B1)")
}

/**
 * (u50) Vendor file-scope globals of the interp-native-btree.cpp
 * high-key scan (h004: file-scope globals -> singleton object
 * properties, source lines 92-101):
 *   unsigned char BigKey[STR_MAX] -> bigKey (:92)
 *   unsigned int BigKeyBlock -> bigKeyBlock (:93, unsigned 32-bit
 *   domain maps to Int; block numbers stay in range)
 *   unsigned int BigKeyParent -> bigKeyParent (:96)
 *   unsigned int BigKeyParentOffset -> bigKeyParentOffset (:97)
 *   int BigKeyOffset -> bigKeyOffset (:98)
 *   int BigKeyLower -> bigKeyLower (:99)
 *   OFF_T BigKeyData -> bigKeyData (:101, off_t -> Long)
 */
object NativeBtreeState {
    val bigKey: CharArray = CharArray(32000) // vendor STR_MAX (strmax.h:3)
    var bigKeyBlock: Int = 0
    var bigKeyParent: Int = 0
    var bigKeyParentOffset: Int = 0
    var bigKeyOffset: Int = 0
    var bigKeyLower: Int = 0
    var bigKeyData: Long = 0
}

/** Owner: interp-native-btree.cpp:111 inline freadx (batch-7 unit). */
fun freadx(r: Int, flag: Int): CharArray {
    TODO("h014 stub: owner interp-native-btree.cpp::freadx (batch-7 unit); the vendor's unsigned char** out-param becomes the return value")
}

/** Owner: interp-sql.cpp::sql (batch-9 corpus unit, not yet translated). */
fun sql(op: Int, svPtr: Msv, DataSourceName: CharArray, Command: CharArray,
        arg5: CharArray, arg6: CharArray): Int {
    TODO("h014 stub: owner interp-sql.cpp::sql (batch-9 unit); prototype include/mumpsc/btree.h:37")
}
