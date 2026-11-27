package daf


// Unit 40 (DAF supplementary render session, 2026-11-27, per the
// 2026-11-26 PI adjudication of the B10 flags):
// compiler-if.c::if_code (units.csv record 40, grep line 41 - the
// ledger's r41 pin cites the grep line; 40 LOC, batch-2 complex
// stratum; the named stub ifCodeDaf raised at B10 is RELEASED by
// this render, same-name body here, release note at the stub site).
// The IF command compiler arm: the no-argument form emits the bare
// svPtr->tpx guard and bumps IF_flg; the ':' postconditional form
// seats atTest/atCopyDaf + INTERPRET (emitInterpretDaf, unit-209
// pin), flushes the postconditional close-brace, linepads the guard
// and jumps if_structure; the argument form loops if_again through
// parse_ (the compiler TU's own parse_, seated parseCDaf per the
// row-277 PI-ruled rebind precedent; the older SetCode/ForCode
// parseDaf(sv) folds stay as previously adjudicated), emits the
// tpx default/reset pair and the numcomp2 test over
// Extract2(&pd1[sdlim]) through extract2Daf (String side, row 30),
// then if_structure: tpx = 1 rides the parseCDaf sv-carrier fold
// (svPtr.tpx, disclosed there), the xd[xpx+1] != '{' IF_flg bump,
// the ForBlocks[IF_flg] = ForBlocks[IF_flg-1] inheritance and
// Blocks[IF_flg] = IF ('i', compiler-macros.h:95) tag, with the
// ',' comma tail looping if_again.
//
// Consults (BASE heuristic rows): 2/26 hit-reuse (Sysout/fprintf(Out)
// -> DafCompilerState.emit, emitted C text verbatim); 6/22/62
// hit-reuse (compiler TU state rides DafCompilerState: xpx/xd/
// IF_flg/POST_flg/at_flg/t2/sdlim; the IF tag is the per-file
// const below); 30 hit-adapt (&pd1[sdlim] Extract2 through the
// extract2Daf seat, String side); 56 hit-reuse (strcmp/char-lattice
// folds); 69 hit-reuse (printf stdout -> Kotlin print, none here);
// 99 hit-reuse (return-fold of the vendor goto ladder: if_again/
// if_structure render as the labeled while machine, plain return
// per C return); 92 hit-reuse (emitted svPtr/tpx text verbatim).
// Vendor quirks kept: (1) the dead commented-out numcomp fprintf
// cell above the live numcomp2 emit is comment-folded residue;
// (2) the numcomp2 close-paren double quirk " )) { // Test if true"
// kept verbatim; (3) atTest returns -1 -> ABRTX_D at this site
// (the atTest contract cell; main's ':' site checks 99 per its
// own vendor cell - both kept, each verbatim).
// Dafny: EXEMPT (emit-side procedural arm, the T2 exemption
// family; journal row).

// compiler-macros.h:95 #define IF 'i' - the Blocks[] type tag of
// this row (main's private IF_M twin stays per the per-unit rule).
const val IF_TAG_IF_CODE_D = 'i'.code

fun ifCodeDaf(): Int {

    val C = DafCompilerState
    val sv = DafInterpState.svPtr

    // if (xd[xpx] == ' ' || xd[xpx] == '\0') - no argument form
    if (at(C.xd, C.xpx) == ' ' || at(C.xd, C.xpx) == NUL_D) {
        C.xpx++
        C.emit("\tif (svPtr->tpx) {\n")
        C.IF_flg++
        return 0 // goto next_cmnd
    }

    // at_flg = atTest(xd, pd1, &pd1Last, lines, xpx); - the unit-165
    // body (atTest contract: -1 on unterminated quoted span).
    C.at_flg = atTest(C.xd, C.pd1, C.pd1Last, C.lines.toShort(), C.xpx)

    if (C.at_flg == -1) return ABRTX_D // goto abrtx

    // goto if_structure arm when the postconditional fired
    var skipAgain = C.at_flg != 0

    if (C.at_flg != 0) {

        val tmp = CharArray(1024)
        strcpyAtIfCode(tmp, 0, "i ") // strcpy(tmp, "i ")
        atCopyDaf(C.at_flg, tmp)
        emitInterpretDaf(DafCRuntime.cstring(tmp)) // INTERPRET
        C.xpx = C.at_flg

        if (C.POST_flg != 0) {
            C.emit("\t} /* post conditional 1856*/\n")
            C.POST_flg = 0
        }

        linepadDaf(
            C,
            "\tif (svPtr->tpx) {",
            "Test if expression true")
        // goto if_structure
    }

    while (true) {

        if (!skipAgain) {
            // if_again:
            C.t2 = 1
            // if ((ierr = parse_())) return PARSE_ERROR; - the
            // compiler TU parse_ seat (row-277 rebind precedent)
            C.ierr = parseCDaf(C, sv)
            if (C.ierr != 0) return PARSE_ERROR_D

            C.emit("\tsvPtr->tpx = 0; //Default \u0024Test indicator\n")

            // the dead commented-out numcomp cell (vendor residue,
            // comment-folded):
            //   fprintf(Out, "\tif (numcomp((UNS) %s, "
            //       "(UNS) \"0\")) { // Test if true \n",
            //       Extract2(&pd1[sdlim]));

            C.emit(
                "\tif (numcomp2((UNS) " +
                extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim)) +
                " )) { // Test if true \n")

            C.emit("\tsvPtr->tpx = 1; // \u0024test becomes true\n")
        }
        skipAgain = false

        // if_structure:
        sv.tpx = 1 // tpx = 1 (the parseCDaf sv-carrier fold)
        if (at(C.xd, C.xpx + 1) != '{') C.IF_flg++

        if (C.IF_flg > 0) {
            C.ForBlocks[C.IF_flg] = C.ForBlocks[C.IF_flg - 1]
        }

        C.Blocks[C.IF_flg] = IF_TAG_IF_CODE_D

        if (at(C.xd, C.xpx) == ',') {
            C.xpx++
            continue // goto if_again
        }

        return 0 // goto next_cmnd
    }
}

// strcpy analog into a CharArray at offset (per-file private,
// main's copyStrAt twin per the per-unit rule; bounds-bounded).
private fun strcpyAtIfCode(dst: CharArray, off: Int, s: String) {
    var i = 0
    val n = dst.size - 1 - off
    while (i < s.length && i < n) { dst[off + i] = s[i]; i++ }
    dst[off + i] = NUL_D
}
