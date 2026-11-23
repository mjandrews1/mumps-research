package both

// BOTH-arm local runtime carriers (arm cold open at batch 5, cycle 1).
// This file carries the minimal C-runtime seats the batch-5 tranche-1
// renders need; every seat names its roster owner and delegates when
// the owner unit lands (h014 action). NO other arm's Kotlin was
// consulted; all values verified against the pristine vendor headers.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: every seat below is a TODO stub
//     naming its units.csv owner (zfcn roster unit 184, fcn roster unit 123,
//     Mglobal roster unit 167, MSV stateVector roster unit 172,
//     GlobalExceptionCode/MumpsGlobalException + RETRIEVE libmpscpp.h
//     residue, roster unit 305). Adapt note: the vendor file-scope svPtr becomes
//     the arm-local singleton below until the MSV owner unit lands.
//   h012 unsigned_byte_domain - reused: byte-domain comparisons stay
//     in the Char(0..255) domain (GLOBAL_NOT_FOUND 2, RETRIEVE 0 are
//     plain Int codes; the coded-argument bytes are compared as
//     chars, see the unit renders).
//   h017 naming - reused: C names kept mechanically (snake->camel
//     only where the vendor name itself changes case).
//
// Constants verified in the vendor tree (2026-11-21):
//   STR_MAX 32000          include/mumpsc/strmax.h:3
//   RETRIEVE 0             include/mumpsc/defines.h.in (live build)
//   GLOBAL_NOT_FOUND 2     include/mumpsc/libmpscpp.h:71

internal const val STR_MAX = 32000 // strmax.h:3; file-private (collision lesson)
internal const val RETRIEVE = 0 // defines.h: RETRIEVE 0
internal const val GLOBAL_NOT_FOUND = 2 // libmpscpp.h:71
internal const val INVALID_CONVERSION = 1 // libmpscpp.h:70 (T3-add; thrownMessage seat)

// libmpscpp.h:62 extern int GlobalExceptionCode (owner: the
// libmpscpp.h::<misc> residue unit, roster unit 305).
internal var GlobalExceptionCode = 0

// MumpsGlobalException (class declared libmpscpp.h:93 area; the throw
// machinery owner is the libmpscpp.h::<misc> residue unit,
// roster unit 305). TODO-stub: member shape only; behavior fills in when the
// owner lands.
internal class MumpsGlobalException : Exception()

// struct MSV minimal carrier: only the members the batch-5 units
// touch (bd, v1d - unsigned char[STR_MAX] each,
// include/mumpsc/stateVector.h:42,44; tpx :64, _Sym :65, LineNumber
// :63, start[SYM_MAX] :71; owner: stateVector.h::<misc>,
// roster unit 172). TODO-stub per h014. start resolves the Stab
// carrier (see BothStubs.kt); every pin verified against
// stateVector.h in the pristine vendor tree.
internal class Msv {
    val bd = CharArray(STR_MAX)
    val v1d = CharArray(STR_MAX)
    var tpx: Long = 0 // stateVector.h:64 (execution status)
    var _Sym: Int = 0 // stateVector.h:65 (current symtab frame)
    var LineNumber: Long = 0 // stateVector.h:63
    val start: Array<Stab?> = arrayOfNulls(SYM_MAX) // stateVector.h:71

    // T3-add extension fields (tranche-3 pins, stateVector.h):
    //   xd :45 (Qlength input arena), t0px :55 / xpx :56 (the
    //   Qlength walk pair), t2 :67, program :100 (the mumps main
    //  _argv seat, 512). No collisions with the existing fields
    //   (checked against the T1/T2 set above).
    val xd = CharArray(STR_MAX) // stateVector.h:45
    var t0px: Int = 0 // stateVector.h:55
    var xpx: Int = 0 // stateVector.h:56
    var t2: Int = 0 // stateVector.h:67
    val program = CharArray(512) // stateVector.h:100

    // ------------------------------------------------------------------
    // B6 T2 residual field universe (unit 172,
    // include/mumpsc/stateVector.h::<misc>, vendor lines 39-100).
    // The unit-172 render: the full struct MSV member set in vendor
    // declaration order (fields already seated from B5/T1 are
    // elided here; the pins are stateVector.h line numbers).
    //   Excluded arms (vendor comment lines 97-98, on the record
    //   only): char *grslts[22] and int grsltsx (the commented-out
    //   sql result roster, dead in the pristine tree).
    //   h020 fold: FILE *in_file[10]/out_file[10] :61 seat on
    //   java.io.RandomAccessFile handles (the B5 ZSeek/Ztell FILE*
    //   fold; Array of 10 nullable handles each).
    //   h004 reuse: the struct at file scope names its header;
    //   the AllocSV decl (:104) and the StateVector typedef (:106)
    //   have no observable behavior (the typedef is a spelling
    //   macro; svPtr is the B5 singleton above), journaled.
    //   h021 FIRST CONSULTS-AS-RULE (struct_carrier, promoted at
    //   the B5 close): the residual member shapes follow the
    //   vendor declaration order and widths exactly; fixed
    //   buffers ride CharArray, ints ride Int, longs ride Long.
    //   Collision note (h017): pd1 uses the LITERAL 32001 here -
    //   stateVector.h:43 references sysparms.h PS1 (PS 32000 + 1),
    //   while the compiler-side package const PS1 (memsize.h:33,
    //   4097) is a different corpus pin; the literal keeps the
    //   interpreter-side width exact.
    val cfgdata = CharArray(256) // stateVector.h:40
    val cfgkey = CharArray(256) // stateVector.h:41
    val pd1 = CharArray(32001) // stateVector.h:43 (sysparms.h:74-75 PS 32000)
    val setname = CharArray(STR_MAX) // stateVector.h:46
    var LockDone: Int = 0 // stateVector.h:47
    var kflg: Int = 0 // stateVector.h:48
    var io: Int = 0 // stateVector.h:49
    var ierr: Int = 0 // stateVector.h:50
    var sdlim: Int = 0 // stateVector.h:51
    var symflg: Int = 0 // stateVector.h:52
    var ITRAP: Int = 0 // stateVector.h:53
    var setpiece: Int = 0 // stateVector.h:54
    var ctrlc: Int = 0 // stateVector.h:57
    var iorg: Int = 0 // stateVector.h:58
    var symlen: Int = 0 // stateVector.h:59
    var NOERR: Int = 0 // stateVector.h:60
    val in_file = arrayOfNulls<java.io.RandomAccessFile>(10) // :61 (h020)
    val out_file = arrayOfNulls<java.io.RandomAccessFile>(10) // :61 (h020)
    val hor = IntArray(10) // stateVector.h:62
    val ver = IntArray(10) // stateVector.h:62
    var pd1len: Long = 0 // stateVector.h:66
    var nflg: Int = 0 // stateVector.h:68
    var ERROR: Int = 0 // stateVector.h:69
    var gpad: Int = 0 // stateVector.h:70
    var nstart: Nmes? = null // stateVector.h:72 (CompilerGlobalsExt carrier)
    val RefParm = Array(10) { CharArray(16) } // stateVector.h:73
    val Rargs = Array(10) { CharArray(16) } // stateVector.h:74
    var Rargsx: Int = 0 // stateVector.h:75
    var _SymPush: Int = 0 // stateVector.h:76
    var order: Int = 0 // stateVector.h:77
    var hit: Long = 0 // stateVector.h:78 (btree cache hit ratio)
    var count: Long = 0 // stateVector.h:78
    var restoredata: Long = 0 // stateVector.h:79 (unsigned long long cap)
    var ScanMinWordSize: Int = 0 // stateVector.h:80
    var ScanMaxWordSize: Int = 0 // stateVector.h:81
    var PD1: Int = 0 // stateVector.h:82
    val UKEY = CharArray(1024) // stateVector.h:83
    val UDAT = CharArray(1024) // stateVector.h:84
    val args = CharArray(STR_MAX) // stateVector.h:85
    val Table = CharArray(256) // stateVector.h:86 (data base table)
    var TabSize: Int = 0 // stateVector.h:87
    var ConOpen: Int = 0 // stateVector.h:88
    val pgsql_msg = CharArray(STR_MAX) // stateVector.h:89
    val Connection = CharArray(STR_MAX) // stateVector.h:90
    val Col_Names = CharArray(STR_MAX) // stateVector.h:91
    val fdes = IntArray(2) // stateVector.h:92
    var exec: Int = 0 // stateVector.h:93 (one variant; both members below)
    val reference = CharArray(1024) // stateVector.h:94 (call by ref name)
    var ErrChar: Char = 0.toChar() // stateVector.h:95
    val CurrentFile = CharArray(512) // stateVector.h:96 (readf_ name)
    val sqloutput = CharArray(512) // stateVector.h:99 (sql output file)
    // program :100 seated above (T3)
}

internal val svPtr = Msv() // vendor file-scope svPtr (libmpscpp.cpp TU)

// int Mglobal(const int g, unsigned char key[], unsigned char bd[],
//             struct MSV*) - include/mumpsc/global.h:78. UNIT 167
// FLIP (B6 T2): the h014 TODO stub (owner interp-globalb.cpp::
// Mglobal, roster unit 167, batch 6) is replaced by the real
// SQLITE-arm routing below - the owner unit lands with this
// tranche (the accepting-the-seat flip precedent).
//
// Vendor behavior (interp-globalb.cpp:70-101... see the live arm):
// the TU arm-selector at interp-globalb.cpp:45 is "#define SQLITE"
// (the h018 reference build config: SQLITE defined), so the LIVE
// arm is the #if defined(SQLITE) block: CLOSE returns 1 (the
// SQLITE backend closes nothing here); RETRIEVE and STORE run
// keyfix over the key (the leading caret strip + high-bit marker
// collapse, keyfix.h:7, seated REAL below - a same-include static
// inline header body, not another TU's unit; the xecute/SymGet
// stub precedent applied only to cross-TU bodies, journaled);
// every op then delegates to btree(g, key, bd, svPtr) (declared
// include/mumpsc/btree.h:112; owner interp-native-btree.cpp::btree,
// roster 29, batch 1 pending - stub raised in BothStubs.kt, h014).
//
// Excluded arm (#else native section, translated only for the
// record as comments): the RETRIEVE/STORE keyfix twins, the
// ORDERNEXT/ORDERPREV/NEXT/PREVIOUS prepare-block with the
// unsigned char hold[STR_MAX] rewrite and the CLOSE-arm
// CloseFlg static, per interp-globalb.cpp:107-190.
//
// Quirks kept (ledgered):
//   - CLOSE returns 1 BEFORE any key touching: a CLOSE with a
//     garbage key stays benign, kept verbatim.
//   - non-RETRIEVE/STORE ops (XNEXT and the rest) SKIP keyfix:
//     the walk keys are already fixed, kept verbatim.
//   - keyfix mutates the key IN PLACE over the CharArray byte
//     domain (the 0x80 high-bit bytes collapse to 1; the leading
//     caret shifts the string left by one inside the same
//     condition, with the trailing byte washed), byte-faithful
//     via the cStringOf/writeC seats.

internal fun Mglobal(op: Int, key: CharArray, out: CharArray, sv: Msv): Int {
    if (op == CLOSE) return 1 // SQLITE arm: CLOSE is a no-op return

    if (op == RETRIEVE || op == STORE) {
        keyfixC(key)
    }
    return btreeC(op, key, out, sv)
}

// void keyfix(unsigned char *) - static inline body at
// include/mumpsc/keyfix.h:7-35 (not a corpus unit; same-include
// residue of the Mglobal live arm, seated real per the ledger
// above). Remove the leading caret; collapse high-bit bytes to 1;
// in the caret arm the shift leaves the final byte washed by the
// same &0x80 test (byte-faithful trace kept).
internal fun keyfixC(vxd: CharArray) {
    if (vxd.isEmpty()) return
    if (vxd[0] == '^') {
        // while (*vxd) { *vxd = *(vxd+1)&0x80 ? 1 : *(vxd+1); vxd++; }
        var i = 0
        while (vxd[i] != '\u0000') {
            val next = if (i + 1 < vxd.size) vxd[i + 1] else '\u0000'
            val washed = if (next.code and 0x80 != 0) '\u0001' else next
            // the C loop advances vxd and NOTHING writes the byte
            // past the shifted NUL: the vector keeps trailing
            // residue; the observable key (up to NUL) is the same.
            vxd[i] = washed
            i++
        }
        return
    }
    // while (*vxd) { if (*vxd & 0x80) *vxd = 1; vxd++; }
    var i = 0
    while (vxd[i] != '\u0000') {
        if (vxd[i].code and 0x80 != 0) vxd[i] = '\u0001'
        i++
    }
}

// void zfcn(unsigned char*, unsigned char*, struct MSV*) -
// include/mumpsc/zfcns.h:48. Owner: interp-zfcns.cpp::zfcn,
// roster unit 184. RENDERED (batch-6 big three): the live seat now
// lives in ZfcnsZfcn.kt (this stub face removed from this file).

// void fcn(MSV*) - include/mumpsc/libmpscpp.h:431. Owner:
// interp-fcn.cpp::fcn, roster unit 123. TODO-stub (h014).
internal fun fcn(sv: Msv) {
    TODO("stub: owner interp-fcn.cpp::fcn (roster unit 123)")
}

// Mglobal(RETRIEVE, key, out, svPtr) + the fail-throw idiom shared by
// the batch-5 tranche-1 global reads (vendor: if (f==0) {
// GlobalExceptionCode=GLOBAL_NOT_FOUND; throw MumpsGlobalException(); }).
// The Mglobal seat flipped REAL at B6 T2 (unit 167, the SQLITE-arm
// routing above); this fold's behavior is unchanged.
internal fun mglobalRetrieve(key: String): String {
    val out = CharArray(STR_MAX)
    val f = Mglobal(RETRIEVE, key.toCharArray(), out, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return cStringOf(out)
}

// C-string read: bytes up to the first NUL.
internal fun cStringOf(buf: CharArray): String {
    val end = buf.indexOf(0.toChar())
    return if (end == -1) String(buf) else String(buf, 0, end)
}

// -------------------------------------------------------------------------
// Batch-5 tranche-2 seats (core units 134-143), added 2026-11-21.
//
// T2 consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: every seat below is a TODO stub
//     naming its units.csv owner (sigint roster unit 254 batch 7;
//     Mltoa roster unit 292 batch 10) or its non-unit file provenance
//     (xecute: libmpscpp.h:277 + libmpscpp.cpp:7053 overload family,
//     not a corpus unit; SymGet: libmpscpp.h:179, non-unit member).
//   h004 globals_ext - reused: stateVector.h members added to the Msv
//     carrier below, each with its header source line.
//   h012 unsigned_byte_domain - reused: XNEXT/INPUT_LENGTH are plain
//     Int codes compared exactly (defines.h:88, libmpscpp.h:79).
//   h017 naming - reused: C names kept (Mltoa, xecute, SymGet keep
//     vendor case; errorMessage stays snake-shaped only where vendor
//     spells it so: ErrorMessage is kept as errorMessage-per-arm camel
//     and journaled).
//
// T2 constants verified in the vendor tree (2026-11-21):
//   XNEXT 8                include/mumpsc/defines.h:88 (compiler-macros.h:58)
//   INPUT_LENGTH 10        include/mumpsc/libmpscpp.h:79
//   SYM_MAX 200            include/mumpsc/defines.h:218

internal const val XNEXT = 8 // defines.h:88; the Mglobal order-walk op
internal const val STORE = 1 // defines.h:84 (T2-add; the Mglobal SQLITE routing seat)
internal const val INPUT_LENGTH = 10 // libmpscpp.h:79
internal const val SYM_MAX = 200 // defines.h:218; symbol table stack depth
internal const val CLOSE = 4 // defines.h:87 (T3-add; the GlobalClose macro seat)

// void sigint(int) - include/mumpsc/fcns.h family. Owner:
// interp-sysfunc.cpp::sigint, roster unit 254, batch 7. TODO-stub
// (h014): the vendor body aborts the process via signals; the
// arm-local contract is that the stub never returns normally.
internal fun sigint(i: Int) {
    TODO("stub: owner interp-sysfunc.cpp::sigint (roster unit 254, batch 7)")
}

// void ErrorMessage(const char *, long) - include/mumpsc/cppsysfunc.h:31,
// body interp-sysfunc.cpp:251. Non-unit vendor helper: rendered here,
// not stubbed (h014 names roster owners only). The printf is the
// exact h009 fold ("\\n*** %s in or near line %ld\\n"); sigint rides
// its stub, so the vendor's never-return shape is kept as TODO-throws.
internal fun errorMessage(msg: String, lineNumber: Long) {
    System.out.print("\n*** " + msg + " in or near line " + lineNumber + "\n")
    sigint(100) // vendor: aborts; stub throws until roster unit 254 lands
}

// void Mltoa(long, unsigned char *) - include/mumpsc/arith.h:46, body
// interp-arith.cpp:84. Owner: interp-arith.cpp::Mltoa, roster unit
// 292, batch 10. TODO-stub (h014).
internal fun Mltoa(i: Long, p: CharArray) {
    TODO("stub: owner interp-arith.cpp::Mltoa (roster unit 292, batch 10)")
}

// int xecute(char *) - include/mumpsc/libmpscpp.h:277, overload family
// libmpscpp.cpp:7053-7073. Not a corpus unit (file-local overload set);
// seat stays a TODO stub naming the vendor provenance.
internal fun xecute(exp: String): Int {
    TODO("stub: vendor xecute family, libmpscpp.cpp:7053 (not a corpus unit)")
}

// mstring SymGet(char *) - include/mumpsc/libmpscpp.h:179. Not a
// corpus unit; the mstring return folds to String per h007 (the
// mstring-class layout owner, mstringClass.h::<misc> roster 149, has
// not landed in this arm; class_member_carrier miss, T1 recurrence).
internal fun SymGet(s: String): String {
    TODO("stub: vendor SymGet, libmpscpp.h:179 (not a corpus unit)")
}

// C fgetc contract at the Java Reader edge (h001: FILE* input ->
// java.io.Reader, io exceptions surfaced like C's error return).
// EOF is -1 everywhere; a read error quantizes to EOF exactly as
// the vendor's fgetc does.
internal fun fgetcC(file: java.io.Reader): Int {
    return try {
        file.read()
    } catch (e: java.io.IOException) {
        -1
    }
}

// C strcpy: writes src's bytes plus the NUL. The vendor does no
// bounds check (overflow is silent UB in C); the Kotlin fold is
// unbounded too and throws IndexOutOfBounds past the buffer size,
// which is the deterministic quantization of that UB.
internal fun writeC(dst: CharArray, src: String) {
    for (j in src.indices) dst[j] = src[j]
    dst[src.length] = 0.toChar()
}

// C strncpy(dst, src, n): copies at most n bytes, stopping right
// after the NUL if src ends first (no tail padding folded in: no
// T2 call site reads past the copied NUL; journaled).
internal fun strncpyC(dst: CharArray, src: String, n: Int) {
    var j = 0
    while (j < n && j < src.length) {
        dst[j] = src[j]
        j++
    }
    if (j < n) dst[j] = 0.toChar()
}

// C strncmp(a, b, n): NUL-stop comparison, sign result. First
// difference in the first n bytes decides; 0 otherwise.
internal fun strncmpC(a: String, b: String, n: Int): Int {
    var j = 0
    while (j < n) {
        val ca = if (j < a.length) a[j].code else 0
        val cb = if (j < b.length) b[j].code else 0
        if (ca != cb) return ca - cb
        if (ca == 0) return 0
        j++
    }
    return 0
}
