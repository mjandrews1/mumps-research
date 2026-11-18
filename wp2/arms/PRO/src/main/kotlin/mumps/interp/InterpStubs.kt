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

    // --- stateVector.h fields added by B2 unit 58 (interp-parse.cpp::prnterr) ---
    /** :43 - unsigned char pd1[PS1] (h012 byte domain; PS=32000, sysparms.h:74, PS1=PS+1). */
    val pd1: CharArray = CharArray(32001)
    /** :60 - int NOERR. */
    var NOERR: Int = 0
    /** :69 - int ERROR (owner added by B3 unit 74, libmpscpp.cpp::RunMumps). */
    var ERROR: Int = 0
    /** :95 - char ErrChar. */
    var ErrChar: Char = ' '

    // --- stateVector.h fields added by B3 unit 63 (interp-parse.cpp::parse_) ---
    /** :46 - unsigned char setname[STR_MAX] (h012 byte domain; STR_MAX=32000, strmax.h:3). */
    val setname: CharArray = CharArray(32000)
    /** :48 - int kflg. */
    var kflg: Int = 0
    /** :50 - int ierr. */
    var ierr: Int = 0
    /** :51 - int sdlim. */
    var sdlim: Int = 0
    /** :55 - int t0px. */
    var t0px: Int = 0
    /** :66 - long pd1len. */
    var pd1len: Long = 0
    /** :67 - int t2. */
    var t2: Int = 0

    // --- stateVector.h fields added by B4 unit 102 (interp-parse.cpp::readf_) ---
    /** :59 - int symlen. */
    var symlen: Int = 0
    /** :96 - char CurrentFile[512] (h012 byte domain; see readf_). */
    val currentFile: CharArray = CharArray(512)

    // --- stateVector.h fields added by B4 unit 94 (interp-fcn.cpp::fcn) ---
    /** :54 - int setpiece (the dollar-piece/dollar-extract LHS flag). */
    var setpiece: Int = 0
    /** :62 - int hor[10], ver[10] ($x/$y cursor coordinates per io channel). */
    val hor: IntArray = IntArray(10)
    val ver: IntArray = IntArray(10)
    /**
     * :72 - struct nmes * nstart (opaque pointer carrier; the nmes
     * struct is not in the corpus - fcn's dollar-dollar block only
     * copies the pointer into the child state vector).
     */
    var nstart: Any? = null
    /** :82 - int PD1 ($storage). */
    var PD1: Int = 0
    // --- stateVector.h fields added by B6 unit 158 (mpstime.cpp::main) ---
    /** :47 - int LockDone (the cleanLocks epilogue gate). */
    var LockDone: Int = 0
    /** :83 - char UKEY[1024] (h012 byte domain; the btree key file). */
    val UKEY: CharArray = CharArray(1024)
    /** :84 - char UDAT[1024] (h012 byte domain; the btree data file). */
    val UDAT: CharArray = CharArray(1024)
    /** :85 - unsigned char args[STR_MAX] (h012 byte domain; STR_MAX=32000, strmax.h:3). */
    val args: CharArray = CharArray(32000)
    /** :86 - char Table[256] (h012 byte domain; data base table). */
    val Table: CharArray = CharArray(256)
    /** :87 - int TabSize. */
    var TabSize: Int = 0
    /** :99 - char sqloutput[512] (h012 byte domain). */
    val sqloutput: CharArray = CharArray(512)

    // --- stateVector.h fields added by B6 unit 168
    // (include/mumpsc/stateVector.h::<misc>, the header residual;
    // every remaining struct member of vendor 40-100, banner and
    // includes journaled in ARM_WORKING_NOTES, u35 ledger shape) ---

    /** :40 - char cfgdata[256] (h012 byte domain; config data). */
    val cfgdata: CharArray = CharArray(256)
    /** :41 - char cfgkey[256] (h012 byte domain; config key). */
    val cfgkey: CharArray = CharArray(256)
    /** :53 - int ITRAP. */
    var ITRAP: Int = 0
    /** :57 - int ctrlc. */
    var ctrlc: Int = 0
    /** :58 - int iorg (the compiler-side CompilerState extern twin is separate). */
    var iorg: Int = 0
    /** :68 - int nflg. */
    var nflg: Int = 0
    /** :73 - char RefParm[10][16] (h012 byte domain). */
    val RefParm: Array<CharArray> = Array(10) { CharArray(16) }
    /** :74 - char Rargs[10][16] (h012 byte domain). */
    val Rargs: Array<CharArray> = Array(10) { CharArray(16) }
    /** :75 - int Rargsx. */
    var Rargsx: Int = 0
    /** :76 - int _SymPush. */
    var _SymPush: Int = 0
    /** :78 - unsigned long int hit, count (btree cache hit ratio;
     * unsigned 64-bit domain maps to Long, u50 NativeBtreeState note). */
    var hit: Long = 0
    var count: Long = 0
    /** :79 - unsigned long long restoredata (64-bit domain -> Long). */
    var restoredata: Long = 0
    /** :80 - int ScanMinWordSize. */
    var ScanMinWordSize: Int = 0
    /** :81 - int ScanMaxWordSize. */
    var ScanMaxWordSize: Int = 0
    /** :88 - int ConOpen. */
    var ConOpen: Int = 0
    /** :89 - char pgsql_msg[STR_MAX] (h012 byte domain; STR_MAX=32000, strmax.h:3). */
    val pgsql_msg: CharArray = CharArray(32000)
    /** :90 - char Connection[STR_MAX] (h012 byte domain). */
    val Connection: CharArray = CharArray(32000)
    /** :91 - char Col_Names[STR_MAX] (h012 byte domain). */
    val Col_Names: CharArray = CharArray(32000)
    /** :92 - int fdes[2]. */
    val fdes: IntArray = IntArray(2)
    /** :93 - int exec. */
    var exec: Int = 0
    /** :94 - char reference[1024] (h012 byte domain; call-by-ref underlying name). */
    val reference: CharArray = CharArray(1024)
    // :97-98 - the vendor's commented-out char-star grslts[22] and
    // int grsltsx are fossil declarations (h013) - roster note only.
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

/**
 * Owner: interpreter core - Interpret(cmd, svPtr). The stub that
 * lived here (u159-era mis-tag, corrected at u171: roster row 156
 * is interp-zfcns.cpp::zfcn, this seat is u155) was resolved by
 * batch-6 unit 155; the owner-level definition now lives in
 * Interpret.kt and call sites bind unchanged on this signature.
 */

/**
 * Owner: interp-sysfunc.cpp::Merge (batch-8 corpus unit). Raised at
 * u155 (h014 out_of_scope_call) - the MERGE command arm calls it
 * with left=v1d offset 0, right=MergeRHS offset 1; returns Int to
 * match the Mglobal(STORE,...) sibling call in the LHSREF arm.
 */
fun Merge(left: CharArray, leftOff: Int, right: CharArray, rightOff: Int,
          svPtr: Msv): Int {
    TODO("h014 stub: owner interp-sysfunc.cpp::Merge (batch-8 unit)")
}

/**
 * Owner: interp-parse.cpp::inout (batch-10 corpus unit). Raised at
 * u155 (h014 out_of_scope_call) - the NEXT_CMND comment/blank-line
 * skip passes (text=pd1 at sdlim, cmnd/col as ints); the batch-10
 * render will resolve the seat.
 */
fun inout(text: CharArray, textOff: Int, cmnd: Int, col: Int, svPtr: Msv) {
    TODO("h014 stub: owner interp-parse.cpp::inout (batch-10 unit)")
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

/**
 * sigint (interp-sysfunc.cpp::sigint, batch-7 roster row 191) was
 * stubbed here until B7 landed; the owner-level definition now
 * lives in Sigint.kt (same signature; every sigint(100)
 * diagnostic call site binds unchanged) and the stub has been
 * removed.
 */

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

/**
 * freadx (interp-native-btree.cpp::freadx, batch-7 roster row
 * 193) was stubbed here until B7 landed; the owner-level
 * definition now lives in Freadx.kt (same signature; the
 * Scanblocks call site binds unchanged) and the stub has been
 * removed.
 */

/** Owner: interp-sql.cpp::sql (batch-9 corpus unit, not yet translated). */
fun sql(op: Int, svPtr: Msv, DataSourceName: CharArray?, Command: CharArray?,
        arg5: CharArray?, arg6: CharArray?): Int {
    TODO("h014 stub: owner interp-sql.cpp::sql (batch-9 unit); prototype include/mumpsc/btree.h:37; the char* parameters are nullable for the vendor NULL-argument calls (u94 fcn)")
}

// --- added by B3 unit 63 (interp-parse.cpp::parse_) ---

/** Owner: interp-strmanip.cpp::strmove (not a corpus unit). */
fun strmove(dst: CharArray, dstOff: Int, src: CharArray, srcOff: Int) {
    TODO("h014 stub: owner interp-strmanip.cpp::strmove (not a corpus unit)")
}

/**
 * mult/add/sub/divx/divi (interp-arith.cpp) were stubbed here until
 * B3 unit 66 (interp-arith.cpp::<misc>) landed; the owner-level
 * definitions now live in ArithMisc.kt (thin numSetup wrappers,
 * unit 29) and the stubs have been removed.
 */

/** Owner: interp-arith.cpp::expx - RESOLVED by Expx.kt (unit 139,
 *  vendor 923-968; the reference-build double arm); stub removed
 *  (the Parse_.kt:1018 call site binds unchanged). */

/** Owner: interp-arith.cpp::modulo - RESOLVED by Modulo.kt (unit
 *  166, vendor 695-762; the reference-build double/long arms,
 *  the h018 trichotomy with the unconditional intLong at
 *  interp-arith.cpp:59); stub removed (the Parse_.kt:1074
 *  MODULO call site binds unchanged on the offset signature). */

/**
 * Owner: interp-zfcns.cpp::zfcn (batch-6 corpus unit; the
 * interp-bifs.cpp::zfcn batch-6 overload is a separate unit).
 * RESOLVED by Zfcn.kt (unit 156); stub removed.
 */

// --- added by u156 (interp-zfcns.cpp::zfcn mps_* call sites; owners
//     are the interp-arith.cpp batch-10 corpus units; seats raised,
//     bodies pending those units) ---

/** interp-arith.cpp:442 - int mps_floor(char * out, char * in). */
fun mpsFloor(out: CharArray, inCh: CharArray): Int {
    TODO("h014 stub: owner interp-arith.cpp::mps_floor (batch-10 corpus unit)")
}

/** interp-arith.cpp:483 - int mps_ceil(char * out, char * in). */
fun mpsCeil(out: CharArray, inCh: CharArray): Int {
    TODO("h014 stub: owner interp-arith.cpp::mps_ceil (batch-10 corpus unit)")
}

/** interp-arith.cpp:406 - int mps_trunc(char * out, char * in). */
fun mpsTrunc(out: CharArray, inCh: CharArray): Int {
    TODO("h014 stub: owner interp-arith.cpp::mps_trunc (batch-10 corpus unit)")
}

/** interp-arith.cpp:192 - void mps_log(char *a, char *b). */
fun mpsLog(a: CharArray, b: CharArray) {
    TODO("h014 stub: owner interp-arith.cpp::mps_log (batch-10 corpus unit)")
}

/** interp-arith.cpp:223 - void mps_cos(char *a, char *b). */
fun mpsCos(a: CharArray, b: CharArray) {
    TODO("h014 stub: owner interp-arith.cpp::mps_cos (batch-10 corpus unit)")
}

/** interp-arith.cpp:375 - void mps_tan(char *a, char *b). */
fun mpsTan(a: CharArray, b: CharArray) {
    TODO("h014 stub: owner interp-arith.cpp::mps_tan (batch-10 corpus unit)")
}

/** interp-arith.cpp:818 - void mps_exp2(char *c, char *a). */
fun mpsExp2(c: CharArray, a: CharArray) {
    TODO("h014 stub: owner interp-arith.cpp::mps_exp2 (batch-10 corpus unit)")
}

/**
 * Owner: interp-sym.cpp::sym_ (batch-10 corpus unit, 484 LOC).
 * Vendor returns unsigned char * (NULL on not-found); the Kotlin
 * carrier returns CharArray? for the NULL test at call sites.
 */
fun sym_(flag: Int, name: CharArray, nameOff: Int, bd: CharArray, bdOff: Int,
         svPtr: Msv): CharArray? {
    TODO("h014 stub: owner interp-sym.cpp::sym_ (batch-10 unit)")
}

/**
 * Owner: interp-parse.cpp::BuildGlobal (batch-8 corpus unit).
 * Returns Int (vendor interp-parse.cpp:1836) and carries the
 * rslt-buffer offset (u94 fcn passes and-tests the result and
 * binds arg4 to bd+1; the parse_ call sites ignore both, so the
 * arg4Off parameter defaults to 0).
 */
fun BuildGlobal(a: Int, g: Int, key: CharArray?, keyOff: Int, arg4: CharArray?,
                svPtr: Msv, arg4Off: Int = 0): Int {
    TODO("h014 stub: owner interp-parse.cpp::BuildGlobal (batch-8 unit); vendor arg4 NULL renders as null")
}

/** Owner: interp-sym.cpp::BuildLocal (batch-8 corpus unit). */
fun BuildLocal(a: Int, g: Int, key: CharArray?, keyOff: Int, arg4: CharArray?,
               svPtr: Msv, arg4Off: Int = 0): Int {
    TODO("h014 stub: owner interp-sym.cpp::BuildLocal (batch-8 unit); vendor arg4 NULL renders as null; returns Int (vendor interp-sym.cpp:97), arg4Off default 0 (u94 fcn)")
}

/** Owner: interp-rpm.cpp::perl_pm (batch-9 corpus unit) - the regex
 *  engine entry behind rpm/perlmatch/perlcpp; string/pattern carry
 *  the offset shape rpm passes (pcre_pattern local C-string at 0). */
fun perlPm(string: CharArray, stringOff: Int, pattern: CharArray, patternOff: Int,
           allowPartial: Int, svPtr: Msv): Int {
    TODO("h014 stub: owner interp-rpm.cpp::perl_pm (batch-9 unit)")
}

/** Owner: interp-rpm.cpp::scan_token - RESOLVED by ScanToken.kt
 *  (unit 128, the real vendor 259-297 body); stub removed. */

/** Owner: interp-rpm.cpp::parse_count - RESOLVED by ParseCount.kt
 *  (unit 135, the real vendor 360-401 body); stub removed (the
 *  InterpRpmMisc.kt parse_pattern_atom call site binds unchanged). */

/**
 * Owner: global-array machinery Mglobal(g,key,bd,svPtr) (not a corpus
 * unit) - the offset-carrying variant used by parse_ (vendor passes
 * &v1d[1]/&bd[1] pointers); same owner as the mglobal stub above.
 */
fun mglobalAt(g: Int, key: CharArray, keyOff: Int, bd: CharArray, bdOff: Int,
              svPtr: Msv): Int {
    TODO("h014 stub: owner Mglobal machinery, offset variant (not a corpus unit)")
}

// --- added by B3 unit 64 (interp-parse.cpp::patrn) ---

/**
 * interp-parse.cpp::match was stubbed here until B6 unit 160
 * landed; the owner-level definition now lives in Match.kt and
 * the stub has been removed.
 */

/**
 * interp-strmanip.cpp::substr was stubbed here until B7 unit 212
 * landed; the owner-level definition now lives in Substr.kt and
 * the stub has been removed.
 */

/**
 * Owner: interp-parse.cpp::<misc> (B3 unit 75, same tranche as the
 * caller patrn); the compiler-sys1.c::cannon B3 unit 72 is the
 * compiler-side twin.
 */
fun cannon(a: CharArray) {
    TODO("h014 stub: owner interp-parse.cpp::<misc> (B3 unit 75)")
}

// --- added by B3 unit 73 (interp-zfcns.cpp::GtkTreeAdd) ---

/**
 * Owner: GTK C library glue (not a corpus unit) - the sscanf("%p")
 * decode of the treestore pointer text into the opaque handle
 * carrier (cf. the mumps.mdh GtkTreeStore stand-in).
 */
fun gtkTreeStoreFromPtr(treestore: CharArray): mumps.mdh.GtkTreeStore {
    TODO("h014 stub: owner GTK C library glue - sscanf %p decode (not a corpus unit)")
}

/** Owner: GTK C library gtk_tree_store_append (not a corpus unit).
 *  iterb widened to GtkTreeIter? by u156 (interp-zfcns.cpp::zfcn):
 *  the vendor calls it with NULL for the level-1 add (zfcns 555). */
fun gtkTreeStoreAppend(t: mumps.mdh.GtkTreeStore, itera: mumps.mdh.GtkTreeIter, iterb: mumps.mdh.GtkTreeIter?) {
    TODO("h014 stub: owner GTK C library gtk_tree_store_append (not a corpus unit)")
}

/** Owner: GTK C library gtk_tree_store_set (not a corpus unit); value at v1d[off]. */
fun gtkTreeStoreSet(t: mumps.mdh.GtkTreeStore, itera: mumps.mdh.GtkTreeIter, column: Int,
                    v1d: CharArray, off: Int) {
    TODO("h014 stub: owner GTK C library gtk_tree_store_set (not a corpus unit)")
}

// --- added by B4 unit 98 (interp-sym.cpp::GlobalQuery) ---

/**
 * Owner: state-vector machinery AllocSV (interp.cpp; not a corpus
 * unit). Vendor: struct MSV *svPtr=AllocSV() - push a fresh state
 * vector; the caller's memcpy/free become the carrier copy (GC).
 */
fun allocSV(): Msv {
    TODO("h014 stub: owner state-vector machinery AllocSV (not a corpus unit)")
}

// --- added by B4 unit 94 (interp-fcn.cpp::fcn) ---

/** Owner: interp-sym.cpp::ScanParse (batch-8 corpus unit; declared interp-fcn.cpp:81). */
fun ScanParse(tmp1: CharArray, tmp1Off: Int): Int {
    TODO("h014 stub: owner interp-sym.cpp::ScanParse (batch-8 unit)")
}

/** Owner: interp-sym.cpp::Eval (interp-sym.cpp:1192, not a corpus unit; declared interp-fcn.cpp:82). */
fun Eval(tmp0: CharArray, tmp2: CharArray, svPtr: Msv): Int {
    TODO("h014 stub: owner interp-sym.cpp::Eval (not a corpus unit)")
}

/** Owner: interp-sym.cpp::Qlength - RESOLVED by Qlength.kt (unit
 *  133, vendor 1100-1124); stub removed (Fcn.kt call site binds
 *  unchanged). */

/**
 * Qsub (interp-sym.cpp::Qsub, batch-7 roster row 194) was stubbed
 * here until B7 landed; the owner-level definition now lives in
 * Qsub.kt (same signature; the Fcn.kt call sites bind unchanged)
 * and the stub has been removed.
 */

// (B4 unit 112) The LocalName stub raised by B4 unit 94 is RESOLVED:
// B4 unit 112 translated interp-sysfunc.cpp::LocalName - see
// LocalName.kt (the stub was removed, cleanLocks precedent, unit 56);
// the Fcn.kt call sites bind the real owner with their existing
// offset shapes.

/**
 * Owner: include/mumpsc/globalOrder.h:8 inline GlobalOrder (not a
 * corpus unit); tmp1 is the out buffer.
 */
fun GlobalOrder(tmp0: CharArray, tmp0Off: Int, tmp1: CharArray, tmp1Off: Int,
                tmp2: CharArray, tmp2Off: Int, svPtr: Msv) {
    TODO("h014 stub: owner globalOrder.h inline GlobalOrder (not a corpus unit)")
}

/** Owner: interp-strmanip.cpp::lpad - RESOLVED by
 *  InterpStrmanipMisc.kt (unit 136 - lpad rides the strmanip misc
 *  chunk below the function-unit threshold); stub removed (the
 *  Fcn.kt call site binds unchanged on the offset signature). */

/** Owner: C library strfmon (monetary.h; external library, no corpus unit). */
fun strfmon(s: CharArray, sOff: Int, maxsize: Int, format: CharArray, formatOff: Int,
            x: Double) {
    TODO("h014 stub: owner C library strfmon (monetary.h)")
}

// (added by B4 unit 104 - parse_pattern_element owner family)

/**
 * Owner: interp-bstring.cpp::bstrcat (batch-9 corpus unit); the
 * vendor takes both bstrings BY VALUE and returns the fresh
 * concatenation struct.
 */
fun bstrcat(a: Bstring, b: Bstring): Bstring {
    TODO("h014 stub: owner interp-bstring.cpp::bstrcat (batch-9 unit)")
}

/** Owner: interp-rpm.cpp accept_it/accept/syntax_error/
 *  parse_pattern_atom_list/rpm/perlmatch/perlcpp/scan/new_scanner/
 *  take_char/new_parser/parse/parse_pattern/parse_pattern_atom
 *  - all RESOLVED by the misc-chunk owner InterpRpmMisc.kt
 *  (unit 127); stubs removed. */

/** Owner: interp-rpm.cpp::leaning_toothpickize - RESOLVED by B6
 *  unit 183 (LeaningToothpickize.kt); stub removed. */

// --- added by B6 unit 161 (server.cpp::main) ---

/**
 * Owner: interp-globalb.cpp::Mglobal - RESOLVED by Mglobal.kt
 * (unit 173, vendor 70-93; roster row 174, batch 6 - the h018
 * SQLITE arm per the reference build; the keyfix copy/write-back
 * adapter and the NativeBtree.btree seat carry the body); stub
 * removed (the ServerMain.kt:237 call site binds unchanged on
 * the key-offset-carrying signature).
 */

/** Owner: server.cpp::logtime (server.cpp:62, not a corpus unit). */
fun logtime(): CharArray {
    TODO("h014 stub: owner server.cpp::logtime (not a corpus unit)")
}

// --- added by B7 unit 188 (libmpscpp.cpp::<misc-6>, Horolog) ---

/**
 * Owner: interp-sym.cpp::_horolog (vendor 1305; batch-9 corpus
 * unit, roster row 259 interp-sym.cpp::<misc> family). Raised by
 * the B7 Horolog render (LibmpscppMisc6.kt); the owner unit
 * resolves it. Param renamed in -> t (Kotlin hard keyword, h015).
 */
fun _horolog(t: CharArray) {
    TODO("h014 stub: owner interp-sym.cpp::_horolog (batch-9 corpus unit)")
}
