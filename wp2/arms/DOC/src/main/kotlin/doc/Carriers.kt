package doc

// Arm DOC shared carriers (arm scaffolding, opened with batch B2 unit 32).
// Carriers mirror the vendor headers actually referenced by the translated
// units; each added property names its vendor source line. Translations are
// made from the pristine vendor tarball only (arm independence). Consult
// outcomes for this file are recorded in the arm journal (C-005).
//
// Vendor constants:
//   STR_MAX        include/mumpsc/strmax.h:3      32000
//   RETRIEVE       include/mumpsc/defines.h:83     0
//   STORE          include/mumpsc/defines.h:84     1
//   GKILL          include/mumpsc/defines.h:167    3
//   XNEXT          include/mumpsc/defines.h:88     8
//   GLOBAL_NOT_FOUND include/mumpsc/libmpscpp.h:71 2
//   CodedOpen      include/mumpsc/defines.h:38    206
//   CodedClose     include/mumpsc/defines.h:39    207
//   CodedComma     include/mumpsc/defines.h:40    208

const val STR_MAX = 32000
const val RETRIEVE = 0
const val STORE = 1
const val GKILL = 3
const val XNEXT = 8
// include/mumpsc/defines.h:170-171: ORDERNEXT 12 / ORDERPREV 13
// (the \u0024order direction codes of the globalOrder.h:8 inline;
// raised by the unit-265 batch-9 B9-T2 render, GlobalOrderH.kt)
const val ORDERNEXT = 12
const val ORDERPREV = 13
// include/mumpsc/defines.h:85 define NEXT (Mglobal code)
const val NEXT = 2
const val GLOBAL_NOT_FOUND = 2
// libmpscpp.h:73 error code 4
const val DATA_RANGE_ERROR = 4
// libmpscpp.h:70 error code 1
const val INVALID_CONVERSION = 1
const val CodedOpen = 206
const val CodedClose = 207
const val CodedComma = 208
// libmpscpp.h:78 error code 9 (the sym_ NULL-return code of the
// SymGet="__symput family; EDIT-ONLY carrier addition of the unit-120
// render)
const val SYMTAB_ERROR = 9
// defines.h:91 FLUSH 21 (Mglobal code of global::Flush; the vendor
// defines it a SECOND time at defines.h:180 - duplicate define kept
// as this note; EDIT-ONLY carrier addition of the unit-120 render)
const val FLUSH = 21

// Low bytes per cookbook H-015: built as named constants instead of raw
// control-byte literals (Mumps delimiter byte is 1; NUL is 0). The
// unicode escapes below are textual (no raw bytes in this file).
const val NUL: Char = '\u0000'
const val CHAR_1: Char = '\u0001'
// include/mumpsc/defines.h:218 SYM_MAX 200 (symbol table depth)
const val SYM_MAX = 200

// struct MSV state vector (include/mumpsc/stateVector.h:39-), fields added
// per unit with the vendor line recorded: bd (stateVector.h:42),
// v1d (stateVector.h:44), tpx (stateVector.h:64, long), pd1 (:43),
// io (:49), NOERR (:60), out_file (:61), hor/ver (:62),
// LineNumber (:63), ErrChar (:95), in_file (:61 carrier).
class Msv {
    val bd = CharArray(STR_MAX)
    val v1d = CharArray(STR_MAX)
    var tpx: Long = 0
    // stateVector.h:71 struct stab *start[SYM_MAX] (symbol table
    // level heads; Stab carrier class rendered by unit 28)
    val start = arrayOfNulls<Stab>(SYM_MAX)
    // stateVector.h:65 int _Sym (current symbol table level; leading
    // underscore kept verbatim per H-017 vendor-name fidelity)
    var _Sym: Int = 0
    // stateVector.h:45 unsigned char xd[STR_MAX] (source text buffer)
    val xd = CharArray(STR_MAX)
    // stateVector.h:56 int xpx (source index)
    var xpx: Int = 0
    // stateVector.h FILE *in_file[10] carrier (unit 48: getstr2 binds
    // lun 5 to stdin); rendered as Readers indexed as in the vendor.
    val in_file = arrayOfNulls<java.io.Reader>(10)
    // stateVector.h:61 FILE *out_file[10] - Writers, same indexing;
    // assigned by the open/use owner units.
    val out_file = arrayOfNulls<java.io.Writer>(10)
    // stateVector.h:43 unsigned char pd1[PS1] (sysparms PS1 32001)
    val pd1 = CharArray(PS1)
    // stateVector.h:46 unsigned char setname[STR_MAX] (name scratch
    // for SET/NEW symbol resolution; bound by unit 93).
    val setname = CharArray(STR_MAX)
    // stateVector.h:48 int kflg (kill flag; bound by unit 93).
    var kflg: Int = 0
    // stateVector.h:55 int t0px (saved xpx source index; bound by
    // unit 93).
    var t0px: Int = 0
    // stateVector.h:50 int ierr (interpreter error return code; bound
    // by unit 93).
    var ierr: Int = 0
    // stateVector.h:49 int io
    var io: Int = 0
    // stateVector.h:60 int NOERR
    var NOERR: Int = 0
    // stateVector.h:62 int hor[10], ver[10]
    val hor = IntArray(10)
    val ver = IntArray(10)
    // stateVector.h:63 long LineNumber
    var LineNumber: Long = 0
    // stateVector.h:95 char ErrChar
    var ErrChar: Char = NUL
    // stateVector.h:52 int symflg (symbol store/kill routing)
    var symflg: Int = 0
    // stateVector.h:77 int order (the order walk direction)
    var order: Int = 0
    // stateVector.h:51 int sdlim (pd1 result lower bound; read by the
    // mstring::Eval unit 79 render)
    var sdlim: Int = 0
    // stateVector.h:66 long pd1len (length of the pd1 parse buffer)
    var pd1len: Long = 0
    // stateVector.h:67 int t2 (indirection reentry flag; Eval sets 1)
    var t2: Int = 0
    // stateVector.h:69 int ERROR (last interpretation error code)
    var ERROR: Int = 0
    // stateVector.h:59 int symlen (the ZLOAD read bound; readf_
    // unit-115 seat, EDIT-ONLY addition by that unit)
    var symlen: Int = 0
    // stateVector.h:96 char CurrentFile[512] (name of current
    // file - see readf_; String render, unit-115 seat)
    var CurrentFile: String = ""
    // stateVector.h:54 int setpiece (LHS piece/extract store
    // flag; the unit-93 binder, dispatched by unit 123)
    var setpiece: Int = 0
    // stateVector.h:72 struct nmes * nstart (the pushed name
    // stack pointer; opaque pointer seat carried for the
    // copy-through of the fcn unit-123 \u0024\u0024FUNCTION launch)
    var nstart: Any? = null
    // stateVector.h:82 int PD1 (available pd1 parse-buffer
    // space; the \u0024storage read, unit 123 seat)
    var PD1: Int = 0
    // stateVector.h:85 unsigned char args[STR_MAX] (the pushed
    // function-argument carrier read back by the \u0024\u0024FUNCTION
    // launch, unit 123 seat)
    val args = CharArray(STR_MAX)
    // stateVector.h:87 int TabSize (the \u0024ztabsize RDBMS bound,
    // unit 123 seat; SQLITE live per the reference build)
    var TabSize: Int = 0
    // stateVector.h:99 char sqloutput[512] (sql output file name;
    // String render per the CurrentFile H-008 precedent, unit
    // 123 seat)
    var sqloutput: String = ""
    // stateVector.h:100 char program[512] (current program name;
    // EDIT-ONLY carrier addition of the unit-150 mumps.cpp::main
    // render with this single-owner note).
    val program = CharArray(512)

    // ---------------------------------------------------------
    // Unit 172 (B6 tranche 2): include/mumpsc/stateVector.h::<misc>
    // residual field roster (batch 6, complex stratum; roster row
    // 172: loc 76, score 18.4). The fields below are the struct
    // members not carried by earlier units; vendor source line
    // anchors in each comment. grslts/grsltsx (stateVector.h:97-98)
    // are commented out in the vendor header itself - commented
    // fossil, roster note only, not translated.

    // stateVector.h:40 char cfgdata[256] (config data file name
    // carrier; the mumps.cpp::main config scan seat)
    val cfgdata = CharArray(256)
    // stateVector.h:41 char cfgkey[256] (config key carrier)
    val cfgkey = CharArray(256)
    // stateVector.h:47 int LockDone (lock-completed flag)
    var LockDone: Int = 0
    // stateVector.h:53 int ITRAP (error trap activation flag)
    var ITRAP: Int = 0
    // stateVector.h:57 int ctrlc (interrupt flag; the u170
    // TimeOut_getstr1 render reads it in the selectC carrier note)
    var ctrlc: Int = 0
    // stateVector.h:58 int iorg (origin 0/1 flag)
    var iorg: Int = 0
    // stateVector.h:68 int nflg (new flag)
    var nflg: Int = 0
    // stateVector.h:70 int gpad (global padding flag)
    var gpad: Int = 0
    // stateVector.h:73 char RefParm[10][16] (call-by-reference
    // parameter name carrier)
    val RefParm = Array(10) { CharArray(16) }
    // stateVector.h:74 char Rargs[10][16] (routine argument names)
    val Rargs = Array(10) { CharArray(16) }
    // stateVector.h:75 int Rargsx (routine argument count)
    var Rargsx: Int = 0
    // stateVector.h:76 int _SymPush (symbol table push flag)
    var _SymPush: Int = 0
    // stateVector.h:78 unsigned long int hit,count (btree cache
    // hit ratio counters)
    var hit: Long = 0L
    var count: Long = 0L
    // stateVector.h:79 unsigned long long restoredata (restore-data
    // flag/counter)
    var restoredata: Long = 0L
    // stateVector.h:80 int ScanMinWordSize; :81 ScanMaxWordSize
    // (scan word bounds)
    var ScanMinWordSize: Int = 0
    var ScanMaxWordSize: Int = 0
    // stateVector.h:83 char UKEY[1024]; :84 UDAT[1024] (order Prot
    // UKEY/UDAT feature carriers)
    val UKEY = CharArray(1024)
    val UDAT = CharArray(1024)
    // stateVector.h:86 char Table[256] (data base table name)
    val Table = CharArray(256)
    // stateVector.h:88 int ConOpen (connection open flag)
    var ConOpen: Int = 0
    // stateVector.h:89 char pgsql_msg[STR_MAX]; :90 Connection;
    // :91 Col_Names (sql carriers)
    val pgsql_msg = CharArray(STR_MAX)
    val Connection = CharArray(STR_MAX)
    val Col_Names = CharArray(STR_MAX)
    // stateVector.h:92 int fdes[2] (pipe descriptors)
    val fdes = IntArray(2)
    // stateVector.h:93 int exec (exec pipeline flag)
    var exec: Int = 0
    // stateVector.h:94 char reference[1024] (call by ref underlying
    // name carrier)
    val reference = CharArray(1024)
    // stateVector.h:97-98 grslts[22]/grsltsx: commented out in the
    // vendor header itself (commented fossil, roster note only)
}

// libmpscpp.h:75 define INPUT_ERROR 6 (bounded-read error code; the
// Scan unit 75 render raises through GlobalExceptionCode)
const val INPUT_ERROR = 6
// libmpscpp.h:77 define INTERPRETER_ERROR 8 (parse_ failure code; the
// mstring::Eval unit 79 render raises through GlobalExceptionCode)
const val INTERPRETER_ERROR = 8
// defines.h:197-198 CRTN 13, CTRLZ 26 (readf_ unit 78 byte filters;
// LINEFEED 10 is the Getstr2 unit-48 render's const - referenced
// there, not redeclared, H-015 reuse; .code reads for the Int getc
// compares per H-010)
const val CRTN: Int = 13
const val CTRLZ: Int = 26

// File-scope externs of libmpscpp.h:62 (GlobalExceptionCode) :210
// (svPtr) live in one owner object per the one-owner-per-C-entity rule.
object Globals {
    var GlobalExceptionCode: Int = 0
    val svPtr = Msv()

    // extern int ctrlc - the PLAIN RUNTIME flag of the signal
    // family (compiler-sys1.c sigc handler :804 writes 1; NOT
    // the Msv.ctrlc field, which is the per-state-vector twin
    // of the Sigint unit-211 ladder). EDIT-ADDED by the unit-
    // 257 batch-9 B9-T2 render (CompilerSys1Misc.kt), the
    // GlobalExceptionCode per-entity raise precedent (H-014).
    var ctrlc: Int = 0
}

// libmpscpp.h:332 class MumpsGlobalException: message accessor kept verbatim.
class MumpsGlobalException(msg: String = "Global reference not found") :
    RuntimeException(msg)

// mstringCarrier: minimal mstring (include/mumpsc/mstringClass.h:31-)
// surface required by batch B2 units: content string with char* conversion.
// Only members observed by translated units are rendered.
class Mstring(content: String) {
    var str: String = content

    // mstringClass.h private: string name (the private-name seat;
    // EDIT-ONLY carrier addition of the batch-5 unit 149 header
    // render - MstringClassMisc.kt - with this single-owner note).
    var name: String = ""

    constructor() : this("")

    operator fun invoke(s: String): Mstring {
        str = s
        return this
    }

    fun c_str(): String = str

    override fun toString(): String = str
}

// globalClass.h:31 class global: per-instance global reference carrier.
// The vendor private field ref (globalClass.h:536) becomes the Kotlin
// constructor property; unit member functions are rendered as extensions
// in their own unit files so each unit owns its file.
//
// UNIT-199 EDIT (B7 tranche 2, global::MakeGlobal): ref widens
// val -> var for the member's strcpy(ref, buf) reference store (the
// vendor overwrites the pristine name with the built subscripted
// reference - the divergence from the earlier read-only usage is
// this ledger note); the private members ArgCount (globalClass.h:
// 534, int), Sref (:538, std::string - the quoted visible form)
// and Sname (:540, std::string) open their per-instance seats here
// (the -next batch readers of Sref ride them; H-004 member
// ledger).
class GlobalRef(var ref: String) {
    // globalClass.h:534 int ArgCount (private member per vendor)
    var ArgCount: Int = 0
    // globalClass.h:538 std::string Sref (private; the quoted
    // visible-reference builder seat)
    var Sref: String = ""
    // globalClass.h:540 std::string Sname (private)
    var Sname: String = ""
}

// C string operations used by translated units (strcpy/strcat/strlen/
// strncmp over CharArray carriers). Byte-domain semantics per the C
// standard; only what translated units call is here.
object CstringOps {
    fun strlenC(buf: CharArray): Int {
        var i = 0
        while (i < buf.size && buf[i] != '\u0000') i++
        return i
    }

    fun strcpyC(dst: CharArray, src: CharArray) {
        var i = 0
        while (i < src.size && src[i] != '\u0000') {
            dst[i] = src[i]
            i++
        }
        dst[i] = '\u0000'
    }

    fun strcpyS(dst: CharArray, src: String) {
        var i = 0
        while (i < src.length) {
            dst[i] = src[i]
            i++
        }
        dst[i] = '\u0000'
    }

    fun strcatS(dst: CharArray, src: String) {
        val start = strlenC(dst)
        var i = 0
        while (i < src.length) {
            dst[start + i] = src[i]
            i++
        }
        dst[start + i] = '\u0000'
    }

    fun strncmpC(a: CharArray, b: CharArray, n: Int): Int {
        var i = 0
        while (i < n) {
            val ca = a[i]
            val cb = b[i]
            if (ca == '\u0000' && cb == '\u0000') return 0
            if (ca < cb) return -1
            if (ca > cb) return 1
            i++
        }
        return 0
    }

    // strcpy(&dst[offset], src): copy a NUL-terminated String into dst
    // starting at the given offset (vendor idiom &v1d[1]).
    fun copyAt(dst: CharArray, offset: Int, src: CharArray) {
        var i = 0
        while (i < src.size && src[i] != NUL) {
            dst[offset + i] = src[i]
            i++
        }
        dst[offset + i] = NUL
    }

    // read a NUL-terminated String from buf starting at offset
    // (vendor idiom &bd[1]).
    fun cstringAt(buf: CharArray, offset: Int): String {
        val sb = StringBuilder()
        var i = offset
        while (i < buf.size && buf[i] != NUL) {
            sb.append(buf[i])
            i++
        }
        return sb.toString()
    }

    fun cstring(buf: CharArray): String {
        val sb = StringBuilder()
        var i = 0
        while (i < buf.size && buf[i] != NUL) {
            sb.append(buf[i])
            i++
        }
        return sb.toString()
    }

    // strcpy(&dst[offset], src) String form (vendor idiom &tmp[1]).
    fun copyAtS(dst: CharArray, offset: Int, src: String) {
        var i = 0
        while (i < src.length) {
            dst[offset + i] = src[i]
            i++
        }
        dst[offset + i] = NUL
    }

    // strcat(&dst[offset], src) CharArray form (vendors patrn1
    // append of the tmp1/cod210 carriers).
    fun catAt(dst: CharArray, offset: Int, src: CharArray) {
        var i = offset
        while (i < dst.size && dst[i] != NUL) i++
        var j = 0
        while (j < src.size && src[j] != NUL && i < dst.size) {
            dst[i] = src[j]
            i++
            j++
        }
        if (i < dst.size) dst[i] = NUL
    }

    // strncmp(&buf[offset], s, n): compare a NUL-terminated literal
    // against a buffer region starting at offset (vendors
    // ExpandNext/ExpandGet/Extract2 idiom strncmp(&v1d[i],"#G#",3)).
    fun strncmpAt(buf: CharArray, offset: Int, s: String, n: Int): Int {
        var i = 0
        while (i < n) {
            val ca = buf[offset + i]
            val cb = if (i < s.length) s[i] else NUL
            if (ca != cb) return if (ca < cb) -1 else 1
            if (ca == NUL) return 0
            i++
        }
        return 0
    }

    // strcat(&dst[offset], ch): append one char at the NUL of a
    // NUL-terminated region beginning at offset.
    fun catAt(dst: CharArray, offset: Int, ch: Char) {
        var i = offset
        while (i < dst.size && dst[i] != NUL) i++
        if (i < dst.size) {
            dst[i] = ch
            if (i + 1 < dst.size) dst[i + 1] = NUL
        }
    }

    // catAt String form used by the mdh_tree_level_add unit file.
    fun catAt(dst: CharArray, offset: Int, src: String) = catAtS(dst, offset, src)

    // strcat(&dst[offset], src) String form.
    fun catAtS(dst: CharArray, offset: Int, src: String) {
        var i = offset
        while (i < dst.size && dst[i] != NUL) i++
        var j = 0
        while (j < src.length && i < dst.size) {
            dst[i] = src[j]
            i++
            j++
        }
        if (i < dst.size) dst[i] = NUL
    }

    // strncmp(ref, tmp, i): compare a String against the first n bytes
    // of a NUL-terminated buffer region (vendors global::Min and IDF).
    fun strncmpS(a: String, b: CharArray, n: Int): Int {
        var i = 0
        while (i < n) {
            val ca = if (i < a.length) a[i] else NUL
            val cb = if (i < b.size) b[i] else NUL
            if (ca != cb) return if (ca < cb) -1 else 1
            if (ca == NUL) return 0
            i++
        }
        return 0
    }
}
