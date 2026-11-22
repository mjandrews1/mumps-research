package daf

// Unit 178 (DAF B6 T3, complex): compiler-write.c::write_code
// (units.csv row: loc 63, score 45.1, rank 272, complex): the
// compiler W command converter of the emitted-C back end: the
// out_file guard emit, the atTest postconditional probe, the
// at-argument direct-execute arm, then the read loop over the
// write argument lattice: the !/# carriage controls, the *
// direct-output arm, the ? tab arm, and the plain-text default,
// each relaying through the inout io service with the vendor's
// iosub/iosub1 goto shape.
// Consults (2026-11-22T16:21:00Z, nominal): BASE-log rows 2/26
// hit-reuse (every fprintf(Out,...) -> DafCompilerState.emit with
// the emitted C text verbatim; the INTERPRET macro emit keeps the
// compiler-macros.h:39 pin - the four emitted lines including the
// p2236 m1944 tail comments are the SetCode.emitInterpretDaf
// precedent, duplicated private per this render's file), row 28
// hit-reuse (the atoi/parse seats stay text: nothing is parsed at
// compile-emit time here), rows 6/22 hit-reuse (the compiler TU
// globals at_flg/t0px/t2/xpx/ierr/sdlim/pd1 ride the
// DafCompilerState singleton exactly as at the SetCode render),
// row 90 (the read/write converter family: writeCodeDaf IS the
// kin of the SetCode/if_code/lock_code renders of the b4 batch),
// row 30/91 (the &pd1[sdlim] arguments ride the cstringAt text
// fold: inout takes the string AT the sdlim interior and the
// write-back rides the IntCellD carrier raised below), row 33
// family hit-reuse (sprintf %s of the tmp carrier folds onto
// String concat at the emit).
// Seats: atTest (the unit-108 render; the &pd1Last out-param is
// the ledgered value fold of the SetCode call), parseDaf (the b9
// stub), linepadDaf (the ForCode seat), atCopyDaf (the
// DafInterpStubsT3 stub), inoutDaf (RAISED below: owner
// compiler-sys1.c::inout at 292, the 3-arg compiler-side shape
// (text, cmnd, col) - roster batch-2 cell; the interp-parse.cpp
// 4-arg sibling rides its own b10 cell).
// VENDOR QUIRKS kept: (1) the atTest abort probe checks at_flg
// == -1 HERE while set_code checks == 99 - both are atTest return
// arms; kept verbatim per unit; (2) the * arm's atoi/putchar
// emits cite "CSTR %s" with the pd1 interior text (the emitted C
// reads _i=atoi(CSTR expr)) and jump iosub1 SKIPPING the inout
// relay; (3) the !/# pair sets i=2 for ! and i=1 for # (the
// vendor initializes i=1 then overrides to 2 when not #) - the
// render keeps the if-not-# shape; (4) the argument loop ends
// on NUL/space/tab with the POST-flg close-brace emit and falls
// out of the while only there (an unterminated argument list
// would walk off the carrier - unreachable per the parse seat).
// Dafny: EXEMPT (emitter over the stub seats; journal row).

// void inout(text, cmnd, col) - compiler-sys1.c:292 - the
// compiler-side io relay (3 args; the col write-back rides the
// IntCellD carrier; the col = "" literal call of read_code passes
// a discarded cell). RAISED named-owner stub (roster batch-2
// cell; the interp-parse.cpp::inout 4-arg sibling has its b10
// cell).
fun inoutDaf(text: String, cmnd: Int, col: IntCellD): Unit =
    throw NotImplementedError(
        "inout: out-of-scope owner (compiler-sys1.c::inout at 292, roster b2; the interp-parse.cpp::inout sibling, roster b10)")

fun writeCodeDaf(): Int {

    val C = DafCompilerState
    val sv = DafInterpState.svPtr

    // fprintf(Out, "\tif (svPtr->out_file[svPtr->io]==NULL) \n");
    C.emit("\tif (svPtr->out_file[svPtr->io]==NULL) \n")

    // fprintf(Out, "\t\tErrorMessage(\"Write to input file\",
    //               svPtr->LineNumber); // m1944 \n");
    C.emit("\t\tErrorMessage(\\\"Write to input file\\\", " +
            "svPtr->LineNumber); // m1944 \n")

    // at_flg=atTest(xd, pd1, &pd1Last, lines, xpx);
    C.at_flg = atTest(C.xd, C.pd1, C.pd1Last, C.lines.toShort(), C.xpx)
    // (the &pd1Last out-param folds onto the value copy - the
    // ledgered value fold of the SetCode call)

    // if (at_flg == -1) return ABRTX; goto abrtx;
    if (C.at_flg == -1) return ABRTX_D

    if (C.at_flg != 0) { // if (at_flg)

        // char tmp[1024]; strcpy(tmp, "w "); atCopy(at_flg, tmp);
        val tmp = CharArray(1024)
        // strcpy(tmp, "w "); (the row-101 carrier copy fold)
        for (t in 0 until 2) tmp[t] = "w "[t]
        atCopyDaf(C.at_flg, tmp)

        // INTERPRET (compiler-macros.h:39; the private per-file
        // duplicate of the SetCode.emitInterpretDaf pin)
        C.emit("\t_jj = Interpret( (CCHR) " +
                DafCRuntime.cstring(tmp) + ", svPtr); \n")
        C.emit("\tsvPtr->ERROR = _jj; \n")
        C.emit("\tif (!svPtr->NOERR && _jj < 0) \n")
        C.emit("\t\tErrorMessage(\\\"Indirection error\\\", " +
                "svPtr->LineNumber); \n")

        // xpx = at_flg;
        C.xpx = C.at_flg

        // if (POST_flg) { fprintf(Out,"\t} /* post conditional
        // 1920*/ \n"); POST_flg = 0; }
        if (C.POST_flg != 0) {
            C.emit("\t} /* post conditional 1920*/ \n")
            C.POST_flg = 0
        }
        return 0 // goto next_cmnd;
    }

    // t0px = 0;
    C.t0px = 0

    while (true) { // while (1)

        var i = 0 // short i
        var skipInout = false // the iosub1-arrival fold

        when (at(C.xd, C.xpx)) {

        '!', '#' -> { // if (xd[xpx]=='!' || xd[xpx]=='#')
            // new line or page
            i = 1
            if (at(C.xd, C.xpx) != '#') i = 2 // ! -> 2, # -> 1
            C.xpx++
            // goto iosub;
        }

        '*' -> { // if (xd[xpx]=='*') "*" output
            C.xpx++
            C.t2 = 1
            C.ierr = parseDaf(sv)
            if (C.ierr != 0) return PARSE_ERROR_D // goto parse_err;

            // linepad(fprintf(Out,"\t_i=atoi( CSTR %s);",
            //                 &pd1[sdlim]),"Convert to integer");
            linepadDaf(C, "\t_i=atoi( CSTR " +
                    DafCRuntime.cstringAt(C.pd1, C.sdlim) + ");",
                    "Convert to integer")

            // linepad(fprintf(Out,"\tputchar(_i);"),"Direct output");
            linepadDaf(C, "\tputchar(_i);", "Direct output")

            i = 4
            skipInout = true // goto iosub1;
        }

        '?' -> { // if (xd[xpx]=='?') /* tab */
            C.t2 = 1
            C.xpx++
            C.ierr = parseDaf(sv)
            if (C.ierr != 0) return PARSE_ERROR_D // goto parse_err;
            i = 3
            // goto iosub;
        }

        else -> { // the plain-text default
            C.t2 = 1
            C.ierr = parseDaf(sv)
            if (C.ierr != 0) return PARSE_ERROR_D // goto parse_err;
            i = 4
            // goto iosub;
        }
        }

        // iosub: inout(&pd1[sdlim], i, &pd1[sdlim]);
        if (!skipInout) {
            // (the col write-back rides the IntCellD carrier; the
            // cell result is not re-stored here - inout's vendor
            // write lands at the pd1 interior the stub will carry;
            // ledgered row-30 fold)
            inoutDaf(DafCRuntime.cstringAt(C.pd1, C.sdlim), i,
                    IntCellD(0))
        }

        // iosub1:
        if (at(C.xd, C.xpx) == ',') { // if (xd[xpx]==',')
            C.xpx++
            continue
        }

        // if (xd[xpx]==0 || xd[xpx]==' ' || xd[xpx]=='\t') {
        if (at(C.xd, C.xpx) == NUL_D ||
                at(C.xd, C.xpx) == ' ' ||
                at(C.xd, C.xpx) == '\t') {

            if (C.POST_flg != 0) { // if (POST_flg)
                C.emit("\t} /* post conditional 4092*/ \n")
                C.POST_flg = 0
            }
            return 0 // goto next_cmnd;
        }
    }
}
