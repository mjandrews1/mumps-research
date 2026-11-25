package daf

// DAF T3 seat file #2: DafCompilerState - the compiler TU singleton
// (BASE-log rows 6/22 hit-reuse: the file-scope compiler globals fold
// into one object, per the CompilerState precedent). Carries the
// globals used by the batch-4 compiler renders (units 116/117/121/122)
// plus the Out writer seat (BASE-log row 2 family adapted: Sysout/
// fprintf(Out,...) become emit() appends onto the output writer; the
// emitted C text itself is kept verbatim per row 26).

object DafCompilerState {
    // FILE *Out - the emitted-C program stream. StringBuilder seat; the
    // reference IDE-source writes to a FILE*, the render appends text.
    val out = StringBuilder()

    // compiler TU cursor/buffer globals
    val xd = CharArray(STR_MAX_DAF)      // scanned line buffer
    val pd1 = CharArray(STR_MAX_DAF * 2) // source line buffer
    val v1d = CharArray(STR_MAX_DAF)     // token build buffer
    val vd = CharArray(STR_MAX_DAF)      // parsed name buffer
    val forv = CharArray(128)            // FOR index variable name
    var xpx = 0
    var sdlim = 0
    var lines = 0
    var t0px = 0
    var t2 = 0
    var ierr = 0
    var retz = 0
    var lhsf = 0
    var lhsx = 0
    var Set_flg = 0
    var GBL_flg = 0
    var POST_flg = 0
    var LockFlag = 0
    var MergeFlg = 0
    var MergeTmp = 0
    var iiitmp = 0
    var IF_flg = 0
    var dirflg = 0
    var at_flg = 0
    var PD1 = 0

    // FOR bookkeeping
    var forx = 0
    var forxmax = 0
    var for_mult = 0
    var SinglesFlg = 0
    var for_fcn_flg = 0
    var for_fcn_flg2 = 0
    var FOR_limit = 0
    // the extra vendor compiler-TU FOR globals (units 122/123 carrier)
    var forLine = 0
    var forxFLG = 0
    // vendor compiler-globals.h:115-116: short ForBlocks[FBMAX] /
    // Blocks[FBMAX]. Resized FBMAX_D (4000) at the row-278 render:
    // the unit-213 epi -1 fill walks FBMAX_D cells over the old
    // 64-cell carrier (bounds fault); the vendor FBMAX walk is now
    // in-bounds (disclosed divergence FIX, ledgered this session).
    val ForBlocks = IntArray(FBMAX_D)
    val Blocks = IntArray(FBMAX_D)

    // SET LHS store (lhs[25] char* array; malloc storage folds onto the
    // JVM heap rows - the MEM_ERROR malloc-NULL branch is unreachable on
    // the JVM, logged divergence)
    val setname = CharArray(STR_MAX_DAF)  // setname[STR_MAX] TU carrier
    val lhs = Array(25) { "" }
    val lhsGF = IntArray(25)

    // fprintf(Out, ...) seat: appends the verbatim emitted-C text.
    fun emit(s: String) {
        out.append(s)
    }

    // int fprintf(Out, "%s...") style single-arg helpers are rendered
    // at the call sites with String templates (row 26: format text kept
    // verbatim; Locale.US unchanged - the emitted text is not formatted
    // by Kotlin beyond substituting the vendor's %s pieces).

    // doty/exec family stacks (compiler TU globals)
    var ExecLevel = 0
    var ExecName = 0
    var ExecX = 0
    val ExecStack = IntArray(SYM_MAX_D)
    val ExecSym = IntArray(SYM_MAX_D)
    var DoRetX = 0
    val DoRet = IntArray(SYM_MAX_D)
    var pd1cur = 0
    var pd1Last = 0
}
