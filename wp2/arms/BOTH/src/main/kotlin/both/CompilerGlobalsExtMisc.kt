package both

// Unit 151 (BOTH B5 T3, complex): include/mumpsc/
// compiler-globals-ext.h::<misc> (144 LOC residue). Batch_plan
// ordinal 151: complex, LOC 144, score 83.4. Content: the compiler
// side's global register header - the extern blocks, the struct
// nmes/struct txt layouts, the dcls/cvars declared-variable table,
// the prototype block, and the cod208-style comment ledger. There
// are no function bodies in this residual header chunk: every
// extern DEFINITION lives in its compiler TU, whose corpus units
// own the compiler-arm behavior; this arm carries the roster unit
// per the committed row order.
//
// THIS RENDER REGISTERS THE GLOBALS PER h004 (this header is the
// rule's own example): every extern seats as a property of the
// singleton object below with its header source pin; the struct
// layouts seat as carrier classes; the prototypes carry the owner
// ledger (no call sites exist in a residue chunk - the T1
// adapted-hit precedent).
//
// BOTH consults (find_rule before render, events logged):
//   h004 globals_ext - reused (the rule's example header): the
//     extern set seats as the CompilerGlobalsExt singleton, each
//     property with its header line pin, one owner object for the
//     C TU global set.
//   h014 out_of_scope_call - consulted, ADAPTED (hit): the
//     prototype block carries owner names below (no TODO stubs -
//     no call sites in the residue; every owner named per the
//     roster grammar).
//   h001 io_stream_param - reused: the FILE externs seat through
//     the h001 Writer/Reader edges (Out/Final/opnfile/in).
//   h017 naming - hit: the vendor names keep case (setpiece,
//     setname, Jflg, ArgMax, ExecStack, DoRetX, CX, JOB_flg,
//     at_flg, Set_flg, LockFlag, cvarx, cmdx, retz, icmnd, p2px,
//     ernbr, dirflg, lhsGF, lhsx, lhsf) - journaled.
//
// Render: the singleton property seats plus the prototype/owner
// ledger comments (no executable behavior beyond the property
// initialization shapes).

// struct nmes (compiler-globals-ext.h:56-59): name/next chain.
internal class Nmes(val name: String, var next: Nmes? = null)

// struct txt (compiler-globals-ext.h:61-65): name/lnbr/next.
internal class Txt(val name: String, val lnbr: Int, var next: Txt? = null)

// struct dcls (compiler-globals-ext.h:73-76): name/type.
internal class Dcs(val name: String, val type: Int)

internal const val CVARMAX = 100 // compiler-globals-ext.h:71
internal const val PS1 = 4097 // memsize.h:33 (PS+1; PS 4096, memsize.h:32)
internal const val FBMAX = 4000 // compiler-macros.h:124

internal object CompilerGlobalsExt {
    // The extern register (compiler-globals-ext.h pins, verified
    // in the pristine tree this session):
    var setpiece: Short = 0 // :31
    val setname = CharArray(128) // :32
    val bdG = CharArray(2048) // :33 (renamed: bd collides with the
    //   Msv byte-domain seat of the interpreter side; the extern
    //   NAME would collide at the package scope the h004 singleton
    //   shares with the Msv carrier - h017 journaled)
    var tpxG: Short = 0 // :34 (same collision note as bdG)
    var t2G: Short = 0 // :35 (same collision note)
    var pd1len: Short = 0 // :36
    var iorg: Short = 0 // :37
    var xpxG: Long = 0 // :38 (Long xpx; collision note)
    var t0pxG: Short = 0 // :39 (collision note)
    var ierr: Short = 0 // :40
    var symlen: Short = 0 // :41
    var sdlim: Short = 0 // :42
    val pd1 = CharArray(PS1) // :43
    val xdG = CharArray(2048) // :44 (collision note)
    val v1dG = CharArray(2048) // :45 (collision note)
    var ctrlc: Short = 0 // :46
    var gpad: Int = 0 // :47
    var ierr1: Short = 0 // :48
    var nstart: Nmes? = null // :49
    var Jflg: Short = 0 // :50
    var MAIN_flg: Short = 0 // :51
    val FunctionName = CharArray(256) // :52
    var ArgMax: Short = 0 // :53
    var ErrChar: Char = 0.toChar() // :54

    // struct txt extern pointers (compiler-globals-ext.h:66-67)
    var LabelsStart: Txt? = null
    var TextStart: Txt? = null
    var p1: Txt? = null
    var p3: Txt? = null

    // declared-variables table (compiler-globals-ext.h:71-78)
    val cvars: Array<Dcs?> = arrayOfNulls(CVARMAX) // :76
    var cvarx: Int = 0 // :78

    // the second extern block (compiler-globals-ext.h:106-158 pins):
    var Out: java.io.Writer? = null // :106 (FILE *Out - h001 edge)
    var Final: java.io.Writer? = null // :106 (FILE *Final)
    var ExitCode: Short = 0 // :107
    var MaxTmp: Short = 0 // :108
    var iiitmp: Int = 0 // :109
    var forxmax: Int = 0 // :110
    var forxFLG: Int = 0 // :111
    var forLine: Int = 0 // :111
    var forx: Int = 0 // :111
    var IF_flg: Int = 0 // :112
    var IF_flg_max: Int = 0 // :113
    var for_fcn_flg: Int = 0 // :114
    var for_fcn_flg2: Int = 0 // :114
    val ForBlocks = ShortArray(FBMAX) // :115
    val Blocks = ShortArray(FBMAX) // :116
    var FCN_flg: Short = 0 // :117
    var lines: Int = 0 // :118
    var pd1cur: Short = 0 // :121
    var i: Short = 0 // :121
    var j: Short = 0 // :121
    var k: Short = 0 // :121
    var l: Short = 0 // :122
    var cmdx: Short = 0 // :124
    var pd1Last: Short = 0 // :124
    var POST_flg: Short = 0 // :124
    var GBL_flg: Short = 0 // :125
    var exportflag: Short = 0 // :126
    var ExecLevel: Short = 0 // :127
    var ExecName: Short = 0 // :128
    val ExecStack = ShortArray(25) // :129
    val ExecSym = ShortArray(25) // :130
    var ExecX: Short = 0 // :131
    val DoRet = ShortArray(1000) // :132
    var DoRetX: Short = 0 // :133
    var CX: Short = 0 // :134
    var EmitLabel: Short = 0 // :134
    var JOB_flg: Short = 0 // :135
    var at_flg: Short = 0 // :136
    var Set_flg: Short = 0 // :136
    var LockFlag: Short = 0 // :137
    var SubFlag: Short = 0 // :138
    var SymIncrFlg: Short = 0 // :139
    var for_mult: Short = 0 // :140
    var F_flg: Int = 0 // :141
    var SinglesFlg: Short = 0 // :142
    var MergeFlg: Int = 0 // :143
    var MergeTmp: Int = 0 // :144
    var TryFlg: Int = 0 // :145
    var CatchFlg: Int = 0 // :146
    val opnfile = arrayOfNulls<java.io.Writer>(7) // :148 (h001)
    var inFile: java.io.Reader? = null // :149 (FILE *in - h001)
    val forv = CharArray(256) // :150
    var FOR_limit: Int = 0 // :151
    val dosave = CharArray(64) // :152
    val cur_line = CharArray(512) // :153
    val lhs = arrayOfNulls<String>(25) // :154
    val lhsGF = IntArray(25) // :155
    var lhsx: Int = 0 // :156
    var lhsf: Int = 0 // :157
    var pid: Long = 0 // :158

    // the cod comment ledger (compiler-globals-ext.h:162-166) kept
    // verbatim: cod208=208 comma, cod207=207 close, cod209=209
    // comma, cod210=210 colon, cod206=206 open.

    var retz: Short = 0 // :169
    var icmnd: Short = 0 // :169
    var p2px: Short = 0 // :169
    var ernbr: Short = 0 // :169
    val output_file = CharArray(1024) // :170
    val tmp2G = CharArray(1024) // :170 (collision note)
    val tmp3G = CharArray(1024) // :170 (collision note)
    var dirflg: Char = 0.toChar() // :170
    val vdG = CharArray(1024) // :170 (collision note)
    var pl: Int = 0 // :171
    var p2: String? = null // :172 (char *p2)
    val except: Array<String?> = arrayOfNulls(24) // :174 (char *except[])
    var flg: Int = 0 // :176 (int flg)
}

// Prototype owner ledger (compiler-globals-ext.h:80-104, the
// compiler-side owner names; NO stubs raised here - the residue has
// no call sites; the h014 discipline applies at the future TU
// owners): Extract2/Extract1 (compiler-parse.c b2/b3 units),
// ScanPastPostCond, xindex (compiler-sys1.c::xindex roster 107 b10
// twin and interp-strmanip roster 114 b9), errmod_ (roster b2),
// readf_ (compiler-sys1.c::readf_ roster b4), inout (compiler-sys1
// b2), pindex, add (the compiler arith helper), numcomp
// (interp-arith b6 unit), linepad/linepad2, fcninit
// (compiler-fcninit.c roster b7), epi and EP (compiler-epi.c
// roster b7 seats), LevCheck and doty (compiler-doty.c roster b4),
// LocalArray (roster 21 b1), GlobalArray (roster 52 b2), atTest
// (roster 109 b4), atCopy, cvar (the parser cvar helper).
