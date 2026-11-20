package doc

// Compiler-side extern carriers (arm scaffolding, opened with batch-2
// unit 52 and extended by unit 54). One owner object per the cookbook
// one-owner-per-C-entity rule (H-004); each property names its vendor
// source line.
//
// Emission helper: the vendor fprintf returns the number of chars
// written and the compiler code passes that count to linepad
// (compiler-if.c:70 shape); emit() returns the char count for that
// idiom.

object CompilerState {
    // extern short ArgMax (compiler-globals.h:53); int seat per the
    // H-010 promotion note (short ext carrier in Kotlin Int renders)
    var ArgMax: Int = 0

    // extern FILE *Out (compiler-parse.c:166); the Writer is assigned by
    // the owning initialization units of later batches (H-001 FILE*
    // to Writer).
    var Out: java.io.Writer = java.io.PrintWriter(java.io.OutputStreamWriter(
        java.lang.System.out), true)

    // fprintf(Out, ...) emission with the fprintf char-count return.
    fun emit(text: String): Int {
        Out.write(text)
        return text.length
    }

    // interpret(tmp): the INTERPRET stateful macro
    // (include/mumpsc/compiler-macros.h:39) - five sequential
    // emissions reading Out and tmp (cookbook H-003 example itself).
    fun interpret(tmp: String) {
        Out.write("\t_jj = Interpret( (CCHR) \n")
        Out.write("\t\t\"" + tmp + "\", svPtr); \n")
        Out.write("\tsvPtr->ERROR = _jj; \n")
        Out.write("\tif (!svPtr->NOERR && _jj < 0) \n")
        Out.write("\t\tErrorMessage(\"Indirection error\", svPtr->LineNumber); \n")
    }

    // include/mumpsc/compiler-globals-ext.h:109 - count on temp strings
    // used so far in this statement (initializer 0, vendor
    // compiler-globals.h:109).
    var iiitmp: Int = 0

    // compiler-globals-ext.h:38 extern long xpx (Kotlin array indexing
    // takes Int; the narrowing helper documents each use).
    var xpx: Long = 0
    fun xpxIdx(): Int = xpx.toInt()
    fun xpxInc() { xpx++ }

    // compiler-globals-ext.h:35 extern short t2 (H-010 explicit widening)
    var t2: Int = 0
    // compiler-globals-ext.h:40 extern short ierr
    var ierr: Int = 0
    // compiler-globals-ext.h:42 extern short sdlim
    var sdlim: Int = 0
    // compiler-globals-ext.h:36 extern short pd1len (line length of
    // the pd1 read; int seat per the H-010 promotion note)
    var pd1len: Int = 0
    // compiler-globals-ext.h:43 extern unsigned char pd1[PS1]
    // (PS1 32001, sysparms.h:75 - the live sizing)
    val pd1 = CharArray(PS1)
    // compiler-globals.h:124 extern short pd1Last (atTest out-param
    // contract, compiler-globals.h:103 - H-006 holder array)
    val pd1Last = ShortArray(1)
    // compiler-globals-ext.h:44 extern unsigned char xd[2048]
    val xd = CharArray(2048)
    // compiler-globals-ext.h:112 extern int IF_flg
    var IF_flg: Int = 0
    // compiler-globals.h:124 extern short POST_flg
    var POST_flg: Int = 0
    // compiler-globals.h:136 extern short at_flg
    var at_flg: Int = 0
    // compiler-globals-ext.h:118 extern int lines
    var lines: Int = 0
    // compiler-globals-ext.h:115/116 extern short ForBlocks[FBMAX],
    // Blocks[FBMAX] (FBMAX 4000, compiler-macros.h:124)
    val ForBlocks = ShortArray(4000)
    val Blocks = CharArray(4000)
    // compiler-globals-ext.h:34 extern short int tpx
    var tpx: Int = 0

    // compiler-globals externs used by the abrtx epilogue
    // (compiler-abrtx.c; MAIN_flg/FCN_flg/F_flg/ExitCode externs,
    // ExecName extern char *, pid extern int), added by batch-1
    // unit 18 (owner named per H-004 single-owner rule).
    var MAIN_flg: Int = 0
    var FCN_flg: Int = 0
    var F_flg: Int = 0
    var ExitCode: Int = 0
    var ExecName: String = ""
    var pid: Int = 0
    // compiler-globals-ext.h extern unsigned char v1d[] (the vendor
    // extern scanned by LocalArray; global-array parsing buffer).
    val v1d = CharArray(STR_MAX)
    // compiler-globals-ext.h extern unsigned char bd[] (the pattern
    // argument scratch, appended at offset 1 by patrn1).
    val bd = CharArray(STR_MAX)
    // compiler-globals-ext.h:126 extern short exportflag (def
    // compiler-globals.h:125; unit 91 seat;
    // set by the SET pass, read by the NEW mark-copy emission).
    var exportflag: Int = 0
    // compiler-globals-ext.h:130 extern short ExecSym[25] (the
    // per-level Exec mark table of the command compilers; unit 91
    // seat, H-010 promotion note: short array rides ShortArray).
    val ExecSym = ShortArray(25)
    // compiler-globals-ext.h:131 extern short ExecX (the Exec level
    // index paired with ExecSym; unit 91 seat, H-010 Int).
    var ExecX: Int = 0
    // compiler-globals-ext.h:169 extern short retz (the set entry-point
    // command token staged for the SET call; unit 91 seat, H-010 Int).
    var retz: Int = 0
    // compiler-globals-ext.h:39 extern short t0px (the set-call tail
    // flag; unit 91 seat, H-010 Int).
    var t0px: Int = 0
    // compiler-globals-ext.h:170 extern unsigned char vd[1024] (the
    // set-code variable-name buffer; H-007 sizing: carried at STR_MAX
    // like bd/v1d, the vendor 1024 carries only fixed-name loads -
    // divergence ledgered here, unit 91 seat).
    val vd = CharArray(STR_MAX)
    // compiler output_file name carrier (the abrtx name surgery
    // drop-last-2 + suffix append renders over the String content).
    var output_file: String = ""
    // compiler-globals.h:136 extern short LockFlag (set by the
    // LOCK command compiler; unit-116 lock_code seat, H-010 Int).
    var LockFlag: Int = 0
    // compiler-globals.h:170 extern unsigned char dirflg (the
    // directive direction flag; unit-116 lock_code seat, Char
    // render of the byte domain)
    var dirflg: Char = NUL
    // compiler-globals.h:32 extern unsigned char setname[128] (the
    // set-name scratch; unit-121 set_code seat).
    val setname = CharArray(128)
    // compiler-globals.h:124 extern short GBL_flg (the global-array
    // flag out of GlobalArray; unit-121 set_code seat, H-010 Int).
    var GBL_flg: Int = 0
    // compiler-globals.h:135 extern short Set_flg (the local-array
    // flag out of LocalArray; unit-121 set_code seat, H-010 Int).
    var Set_flg: Int = 0
    // compiler-globals.h:142 extern int MergeFlg (unit-121 seat).
    var MergeFlg: Int = 0
    // compiler-globals.h:143 extern int MergeTmp (unit-121 seat).
    var MergeTmp: Int = 0
    // compiler-globals.h:153 extern char *lhs[25] - the malloc
    // domain folds to the String seat (H-008: JVM allocation; the
    // vendor malloc-null MEM_ERROR arm is unreachable in the JVM
    // and ledgered in the unit-121 render); free() maps to the
    // null store exactly where the vendor frees.
    val lhs = arrayOfNulls<String>(25)
    // compiler-globals.h:154 extern int lhsGF[25] (unit-121 seat).
    val lhsGF = IntArray(25)
    // compiler-globals.h:155 extern int lhsx (unit-121 seat).
    var lhsx: Int = 0
    // compiler-globals.h:156 extern int lhsf (unit-121 seat).
    var lhsf: Int = 0
    // compiler-globals.h:111 extern int forxFLG, forLine, forx
    // (unit-122 for_code seats).
    var forxFLG: Int = 0
    var forLine: Int = 0
    var forx: Int = 0
    // compiler-globals.h:110 extern int forxmax (unit-122 seat).
    var forxmax: Int = 0
    // compiler-globals.h:114 extern int for_fcn_flg, for_fcn_flg2
    // (unit-122 seats).
    var for_fcn_flg: Int = 0
    var for_fcn_flg2: Int = 0
    // compiler-globals.h:139 extern short for_mult (unit-122 seat,
    // H-010 Int).
    var for_mult: Int = 0
    // compiler-globals.h:141 extern short SinglesFlg (unit-122
    // seat, H-010 Int).
    var SinglesFlg: Int = 0
    // compiler-globals.h:149 extern unsigned char forv[256] (the
    // loop index name; unit-122 seat).
    val forv = CharArray(256)
    // compiler-globals.h:150 extern int FOR_limit (unit-122 seat).
    var FOR_limit: Int = 0

    // ------- compiler-globals-ext.h batch-5 unit 151 additions
    // (EDIT-ONLY seat additions; the header's extern roster is
    // closed here per the H-004 single-owner rule, one carrier
    // per C symbol; see CompilerGlobalsExtMisc.kt for the
    // header's struct/prototype ledger).

    // compiler-globals-ext.h:31 extern short setpiece - the
    // compiler-set.c file-scope twin of the Msv.setpiece
    // member (stateVector.h:54, distinct C symbols in the
    // vendor; both keep their own carriers here, idem partner
    // noted).
    var setpiece: Int = 0
    // compiler-globals-ext.h:37 extern short iorg
    var iorg: Int = 0
    // compiler-globals-ext.h:41 extern short symlen - the
    // compiler- set/ assignment scanner length; idem partner of
    // Msv.symlen (stateVector.h:59, a distinct C symbol; both
    // keep their own carriers).
    var symlen: Int = 0
    // compiler-globals-ext.h:46 extern short int ctrlc
    var ctrlc: Int = 0
    // compiler-globals-ext.h:47 extern int gpad
    var gpad: Int = 0
    // compiler-globals-ext.h:48 extern short ierr1 - landed
    // earlier as the top-level carrier of InterpParseMisc
    // (H-004 owner; cross-referenced, not duplicated).
    // compiler-globals-ext.h:49 extern struct nmes *nstart -
    // the Msv.nstart seat (stateVector.h:72) is the idem
    // partner; the nmes type lands in
    // CompilerGlobalsExtMisc.kt.
    // compiler-globals-ext.h:50 extern short Jflg
    var Jflg: Int = 0
    // compiler-globals-ext.h:52 extern char FunctionName[256]
    val FunctionName = CharArray(256)
    // compiler-globals-ext.h:54 extern char ErrChar - the
    // Msv.ErrChar seat (stateVector.h:95) is the owner for
    // the SAME state; cross-referenced, not duplicated.
    // compiler-globals-ext.h:61-65 struct txt extern
    // *LabelsStart, *TextStart, *p1, *p3
    var LabelsStart: TxtEntry? = null
    var TextStart: TxtEntry? = null
    var p1: TxtEntry? = null
    var p3: TxtEntry? = null
    // compiler-globals-ext.h:73-76 struct dcs cvars + :78
    // extern int cvarx (CVARMAX :71 define; Dcs type in
    // CompilerGlobalsExtMisc.kt).
    val cvars = Array(100) { Dcs() }
    var cvarx: Int = 0
    // compiler-globals-ext.h:106 extern FILE *Final (the
    // function-emission destination twin of Out; H-001
    // FILE* to Writer diverge, nullable until fcninit binds
    // it - unit-151 seat).
    var Final: java.io.Writer? = null
    // compiler-globals-ext.h:108 extern short MaxTmp
    var MaxTmp: Int = 0
    // compiler-globals-ext.h:113 extern int IF_flg_max
    var IF_flg_max: Int = 0
    // compiler-globals-ext.h:121 extern short pd1cur, i, j, k
    var pd1cur: Int = 0
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    // compiler-globals-ext.h:122 extern short l
    var l: Int = 0
    // compiler-globals-ext.h:124 extern short cmdx
    var cmdx: Int = 0
    // compiler-globals-ext.h:127 extern short ExecLevel
    var ExecLevel: Int = 0
    // compiler-globals-ext.h:129 extern short ExecStack[25]
    val ExecStack = ShortArray(25)
    // compiler-globals-ext.h:132 extern short DoRet[1000]
    val DoRet = ShortArray(1000)
    // compiler-globals-ext.h:133 extern short DoRetX
    var DoRetX: Int = 0
    // compiler-globals-ext.h:134 extern short CX, EmitLabel
    var CX: Int = 0
    var EmitLabel: Int = 0
    // compiler-globals-ext.h:135 extern short JOB_flg
    var JOB_flg: Int = 0
    // compiler-globals-ext.h:138 extern short SubFlag
    var SubFlag: Int = 0
    // compiler-globals-ext.h:139 extern short SymIncrFlg
    var SymIncrFlg: Int = 0
    // compiler-globals-ext.h:145/146 extern int TryFlg, CatchFlg
    var TryFlg: Int = 0
    var CatchFlg: Int = 0
    // compiler-globals-ext.h:148 extern FILE *opnfile[7] -
    // the Z- command open-file slots (H-001 FILE* to Writer;
    // null until the owning command opens).
    val opnfile = arrayOfNulls<java.io.Writer>(7)
    // compiler-globals-ext.h:149 extern FILE *in - the ZLOAD
    // source slot; the in keyword is a Kotlin keyword, rendered in_
    // (H-017 keyword rename ledger).
    var in_: java.io.Writer? = null
    // compiler-globals-ext.h:152 extern unsigned char dosave[64]
    val dosave = CharArray(64)
    // compiler-globals-ext.h:153 extern char cur_line[512]
    val cur_line = CharArray(512)
    // compiler-globals-ext.h:169 extern short retz, icmnd,
    // p2px, ernbr (retz already seated above, batch-1 unit 91)
    var icmnd: Int = 0
    var p2px: Int = 0
    var ernbr: Int = 0
    // compiler-globals-ext.h:170 extern unsigned char
    // tmp2[1024], tmp3[1024] (output_file/dirflg/vd already
    // seated; output note: the String output_file seat of the
    // batch-2 opening stands - H-004 keep-first owner)
    val tmp2 = CharArray(1024)
    val tmp3 = CharArray(1024)
    // compiler-globals-ext.h:171 extern int pl
    var pl: Int = 0
    // compiler-globals-ext.h:172 extern char *p2 (the
    // post-conditional set name carrier next to the p1 label
    // pointer; H-008 malloc-domain String seat)
    var p2: String? = null
    // compiler-globals-ext.h:174 extern char *except[] -
    // the Ztrap exception message table; slots stay null
    // until the owning roster unit table-fills.
    val except = arrayOfNulls<String>(20)
    // compiler-globals-ext.h:176 extern int flg
    var flg: Int = 0
}

// sysparms.h:74-75 live PS/PS1 sizes (the memsize.h 4096 twins are the
// vendor FIXME mismatch, kept as a roster note)
const val PS: Int = 32000
const val PS1: Int = 32001

// compiler-macros.h:95 define IF - the reference-build opcode for the
// IF command block table (Blocks assignment of unit 54)
const val IF_OPCODE: Char = 'i'
