package mumps.compiler

import java.io.Writer

/**
 * PRO-arm owner object for the compiler's file-scope globals
 * (h004 globals_ext; source header include/mumpsc/compiler-globals-ext.h
 * — one owner object per C translation unit's global set). Each property
 * records its header line; byte-domain arrays map to Char(0..255)
 * per h012.
 *
 * Independent translation from the pristine vendor source
 * (mumps-14-may-2026.tgz); not derived from any other arm's output.
 */
object CompilerState {

    /** memsize.h:32-33 — PS=4096, PS1=PS+1. */
    const val PS1 = 4096 + 1

    /** compiler-macros.h:124. */
    const val FBMAX = 4000

    /** compiler-globals-ext.h:106 — FILE* Out; PrintWriter only at edges (h001). */
    lateinit var out: Writer

    var tpx: Short = 0                            // :34
    var t2: Short = 0                             // :35
    var pd1len: Short = 0                         // :36
    var xpx: Long = 0                             // :38
    var ierr: Short = 0                           // :40
    var sdlim: Short = 0                          // :42
    var pd1: CharArray = CharArray(PS1)           // :43 (h012 byte domain)
    var xd: CharArray = CharArray(2048)           // :44 (h012 byte domain)

    /** :45 - extern unsigned char v1d[2048] (h012 byte domain; owner added by unit 21). */
    var v1d: CharArray = CharArray(2048)
    /** :33 - extern unsigned char bd[2048] (h012 byte domain; owner added by unit 26). */
    var bd: CharArray = CharArray(2048)
    var ierr1: Short = 0                          // :48
    var IF_flg: Int = 0                           // :112
    var IF_flg_max: Int = 0                       // :113
    var ForBlocks: ShortArray = ShortArray(FBMAX) // :115
    var Blocks: ShortArray = ShortArray(FBMAX)    // :116
    var lines: Int = 0                            // :118
    var pd1cur: Short = 0                         // :121
    var i: Short = 0                              // :121
    var j: Short = 0                              // :121
    var k: Short = 0                              // :121
    var cmdx: Short = 0                           // :124
    var pd1Last: Short = 0                        // :124
    var POST_flg: Short = 0                       // :124
    var at_flg: Short = 0                         // :136 (C: short at_flg = int atTest)
    var Set_flg: Short = 0                        // :136

    // --- compiler-abrtx.c unit 18 additions (h004; header lines) ---

    /** compiler-globals-ext.h:106 - FILE *Final (Writer at edges, h001). */
    lateinit var Final: java.io.Writer

    /** :51 - extern short MAIN_flg. */
    var MAIN_flg: Short = 0

    /** :117 - extern short FCN_flg. */
    var FCN_flg: Short = 0

    /** :141 - extern int F_flg. */
    var F_flg: Int = 0

    /** :107 - extern short ExitCode. */
    var ExitCode: Short = 0

    /** :128 - extern short ExecName. */
    var ExecName: Short = 0
    /** :53 - extern short ArgMax (owner added by unit 27). */
    var ArgMax: Short = 0

    /** :158 - extern long pid. */
    var pid: Long = 0

    /** :170 - extern unsigned char output_file[1024] (h012). */
    var output_file: CharArray = CharArray(1024)

    /**
     * h003 state-macro: vendor INTERPRET (compiler-macros.h:39-44),
     * a multi-emission macro writing via the shared Out stream, with the
     * caller's local tmp spliced into the second emission. Member of the
     * state singleton; one-line call sites preserved.
     */
    /** :109 - extern int iiitmp; count on temp strings used so far in this stmt (h004; owner added by unit 52). */
    var iiitmp: Int = 0

    // --- compiler-globals-ext.h globals added by B3 unit 65 (compiler-new.c::new_code) ---
    /** :39 - extern short t0px. */
    var t0px: Short = 0
    /** :126 - extern short exportflag. */
    var exportflag: Short = 0
    /** :131 - extern short ExecX. */
    var ExecX: Short = 0
    /** :130 - extern short ExecSym[25] (h012 byte domain not needed; short values). */
    var ExecSym: ShortArray = ShortArray(25)
    /** :169 - extern short retz. */
    var retz: Short = 0
    /** :170 - extern unsigned char vd[1024] (h012 byte domain). */
    var vd: CharArray = CharArray(1024)

    // --- added by B4 units 95/96/101 (compiler-for.c, compiler-set.c, compiler-lock.c) ---

    var setpiece: Short = 0                           // :31
    val setname: CharArray = CharArray(128)           // :32 (h012 byte domain)
    var GBL_flg: Short = 0                            // :125
    var LockFlag: Short = 0                           // :137
    var MergeFlg: Int = 0                             // :143 (C int)
    var MergeTmp: Int = 0                             // :144 (C int)
    var forxmax: Int = 0                              // :110 (C int)
    var forxFLG: Int = 0                              // :111 (C int)
    var forLine: Int = 0                              // :111 (C int)
    var forx: Int = 0                                 // :111 (C int)
    var for_fcn_flg: Int = 0                          // :114 (C int)
    var for_fcn_flg2: Int = 0                         // :114 (C int)
    var SinglesFlg: Int = 0                           // :142 (C short; h010 widened)
    var for_mult: Int = 0                             // :140 (C short; h010 widened)
    val forv: CharArray = CharArray(256)              // :150 (h012 byte domain)
    var FOR_limit: Int = 0                            // :151 (C int)
    val lhs: Array<String?> = arrayOfNulls(25)        // :154 (C char*[25]; GC slots)
    val lhsGF: IntArray = IntArray(25)                // :155
    var lhsx: Int = 0                                 // :156
    var lhsf: Int = 0                                 // :156
    var dirflg: Char = 0.toChar()                     // :170 (h012 byte domain)

    // ------------------------------------------------------------
    // --- B5 unit 129 completion (compiler-globals-ext.h::<misc>):
    //     the full-header render. Every extern of the header is now
    //     carried here; the entries above (units 18/21/26/27/35/36/
    //     52/65/95/96/101 riders) are the FRONT-RUN of this unit -
    //     verified against the header, no redeclaration (u124
    //     precedent). New entries carry their header line anchors.
    // ------------------------------------------------------------

    /** :37 - extern short iorg. */
    var iorg: Short = 0
    /** :41 - extern short symlen. */
    var symlen: Short = 0
    /** :46 - extern short int ctrlc. */
    var ctrlc: Short = 0
    /** :47 - extern int gpad. */
    var gpad: Int = 0
    /** :49 - extern struct nmes *nstart. */
    var nstart: Nmes? = null
    /** :50 - extern short Jflg. */
    var Jflg: Short = 0
    /** :52 - extern char FunctionName[256] (h012 byte domain). */
    val FunctionName: CharArray = CharArray(256)
    /** :54 - extern char ErrChar (h012 byte domain). */
    var ErrChar: Char = 0.toChar()
    /** :66 - extern struct txt *LabelsStart, *TextStart, *p1, *p3. */
    var LabelsStart: Txt? = null
    /** :66 - see LabelsStart. */
    var TextStart: Txt? = null
    /** :66 - see LabelsStart. */
    var p1: Txt? = null
    /** :66 - see LabelsStart. */
    var p3: Txt? = null
    /** :71 - #define CVARMAX 100. */
    const val CVARMAX = 100
    /** :73-74 - extern struct dcs cvars[CVARMAX]. */
    val cvars: Array<Dcs> = Array(CVARMAX) { Dcs() }
    /** :76 - extern int cvarx. */
    var cvarx: Int = 0
    /** :104 - extern short MaxTmp (max temp strings used so far in
     *  this function - vendor comment kept). */
    var MaxTmp: Short = 0
    /** :122 - extern short l. */
    var l: Short = 0
    /** :127 - extern short ExecLevel. */
    var ExecLevel: Short = 0
    /** :129 - extern short ExecStack[25]. */
    val ExecStack: ShortArray = ShortArray(25)
    /** :132 - extern short DoRet[1000]. */
    val DoRet: ShortArray = ShortArray(1000)
    /** :133 - extern short DoRetX. */
    var DoRetX: Short = 0
    /** :134 - extern short CX, EmitLabel. */
    var CX: Short = 0
    /** :134 - see CX. */
    var EmitLabel: Short = 0
    /** :135 - extern short JOB_flg. */
    var JOB_flg: Short = 0
    /** :138 - extern short SubFlag. */
    var SubFlag: Short = 0
    /** :139 - extern short SymIncrFlg. */
    var SymIncrFlg: Short = 0
    /** :146 - extern int TryFlg, CatchFlg. */
    var TryFlg: Int = 0
    /** :146 - see TryFlg. */
    var CatchFlg: Int = 0
    /** :148 - extern FILE *opnfile[7]; open-stream handles at the
     *  h001 edges (the Out/Final Writer precedent - Closeable
     *  carrier until the include-file owner binds a concrete
     *  stream type). */
    val opnfile: Array<java.io.Closeable?> = arrayOfNulls(7)
    /** :149 - extern FILE *in; 'in' is a Kotlin hard keyword -
     *  h017 keyword rename (u124 Getenv 'in_' precedent). */
    var in_: java.io.Closeable? = null
    /** :152 - extern unsigned char dosave[64] (h012 byte domain). */
    val dosave: CharArray = CharArray(64)
    /** :153 - extern char cur_line[512] (h012 byte domain). */
    val cur_line: CharArray = CharArray(512)
    /** :169 - extern short retz, icmnd, p2px, ernbr (retz above). */
    var icmnd: Short = 0
    /** :169 - see icmnd. */
    var p2px: Short = 0
    /** :169 - see icmnd. */
    var ernbr: Short = 0
    /** :170 - extern unsigned char tmp2[1024] (h012 byte domain). */
    val tmp2: CharArray = CharArray(1024)
    /** :170 - extern unsigned char tmp3[1024] (h012 byte domain). */
    val tmp3: CharArray = CharArray(1024)
    /** :171 - extern int pl. */
    var pl: Int = 0
    /** :172 - extern char *p2. */
    var p2: String? = null
    /** :174 - extern char *except[] - incomplete-array extern;
     *  unbound carrier until its owner (the C exception-string
     *  table) initializes the table. */
    var except: Array<String?>? = null
    /** :176 - extern int flg. */
    var flg: Int = 0

    fun interpret(tmp: String) {
        out.write("\t_jj = Interpret( (CCHR) \n")
        out.write("\t\t\"" + tmp + "\", svPtr); \n")
        out.write("\tsvPtr->ERROR = _jj; \n")
        out.write("\tif (!svPtr->NOERR && _jj < 0) \n")
        out.write("\t\tErrorMessage(\"Indirection error\", svPtr->LineNumber); \n")
    }
}

/**
 * B5 unit 129 (include/mumpsc/compiler-globals-ext.h::<misc>,
 * rank 265) - the struct carriers of the header, riding the
 * CompilerState owner object (h004; identity: units.csv row 128 -
 * c, loc 144, comment_ratio 0.077, pointer_marks 31,
 * control_marks 2, score 83.4, batch 5; the 177-line physical
 * header minus blank/comment lines = the 144 physLOC).
 *
 * PROTOTYPE ROSTER (header lines 77-101 + 49 + 79 - declarations
 * carry no Kotlin obligation beyond this ledger; the functions
 * exist as renders or owner-named stubs): cvar (Stubs.kt,
 * owner compiler-parse.c:2074, with the cvars/cvarx table now
 * carried above); Extract2 (DONE, unit u122 Extract2.kt);
 * Extract1 (Stubs.kt); readf_ (DONE, Readf.kt); errmod_ (DONE,
 * Errmod.kt); inout (DONE, Inout.kt); doty (DONE, Doty.kt);
 * LevCheck (Stubs.kt levCheck); LocalArray (DONE, unit 21);
 * GlobalArray (DONE, units 26/119); atTest (DONE, AtTest.kt);
 * atCopy (Stubs.kt); linepad (Stubs.kt); epi (Stubs.kt);
 * EP (Stubs.kt); ScanPastPostCond, xindex, pindex, add, numcomp,
 * linepad2, fcninit, getstr1 - not yet raised; they raise with
 * their first caller per the h014 stub protocol.
 */
class Nmes {
    /** vendor :57 - char *name. */
    var name: String? = null
    /** vendor :58 - struct nmes *next. */
    var next: Nmes? = null
}

/** vendor :62-66 - struct txt { char *name; int lnbr; txt *next; }. */
class Txt {
    var name: String? = null
    var lnbr: Int = 0
    var next: Txt? = null
}

/** vendor :72-74 - struct dcs { char name[32]; int type; }
 *  (h012 byte domain for the 32-byte name field). */
class Dcs {
    val name: CharArray = CharArray(32)
    var type: Int = 0
}
