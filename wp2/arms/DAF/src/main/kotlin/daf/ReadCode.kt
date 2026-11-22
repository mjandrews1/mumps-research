package daf

// Unit 181 (DAF B6 T3, complex): compiler-read.c::read_code
// (units.csv row: loc 96, complex): the compiler R command converter
// of the emitted-C back end, the read side of the writeCodeDaf unit:
// the in_file guard emit, the !/# carriage controls routing to the
// inout io service with a DISCARDED cell (the vendor passes the ""
// literal for col), the ? tab and \" head-text arms relaying parse_
// first, the * direct-mode flag, then the set_code(SET) walk of the
// read item, the timeout colon arm, the DirectRead vs getstr1
// read-mode emits, and the set_code(SET_STORE) store walk with the
// cont_read tail (xpx advance, comma skip, NUL/space/tab end check
// with the POST-flg close-brace emit).
// Consults (2026-11-22T16:22:30Z, nominal): BASE-log rows 2/26
// hit-reuse (every fprintf(Out,...) -> DafCompilerState.emit with
// the emitted C text verbatim), row 6/22 hit-reuse (the compiler TU
// globals t2/xpx/ierr/sdlim/pd1/retz/dirflg ride the
// DafCompilerState singleton exactly as at the write_code render),
// row 90 (the read/write converter family kin of SetCode/if_code/
// lock_code of the b4 batch), row 30/91 (the &pd1[sdlim] arguments
// ride the cstringAt text fold; the strcpy back-fills onto the
// carrier write helper below).
// Seats: inoutDaf (the unit-180 raised named stub... re-used owner
// note: raised at the WriteCode.kt render, compiler-sys1.c::inout
// at 292, roster b2 cell), parseDaf (the b9 stub), setCodeDaf (the
// unit-108 real body; SET and SET_STORE entries),
// CMD_READ_D ('r', compiler-macros.h:100), the global lattice
// constants PARSE_ERROR_D/ARG_LIST_ERROR_D/NO_GLOBAL_PERMITTED_D/
// MEM_ERROR_D/TOO_MANY_LHS_D/ABRTX_D.
// VENDOR QUIRKS kept: (1) the ! arm sets i=2 and the # arm i=1 -
// the write_code render keeps the same reversed-from-words pair;
// (2) the ? arm passes &pd1[sdlim] for BOTH the text and the col -
// the col back-write rides the IntCellD carrier here (NOT the
// discarded cell of the !/\" arms); (3) the timeout arm comment
// linepad is commented out at the vendor and stays comment-only;
// (4) the set_code(SET) ladder returns literal 99 on the fall-through
// (no such arm at set_code(SET_STORE), which falls into cont_read
// implicitly after the ABRTX check at the vendor - the render keeps
// the fall-through explicit); (5) the * arm does NOT route to the
// inout relay: it sets dirflg=1 and falls into retz=READ.
// Dafny: EXEMPT (emitter over the stub seats; journal row).

// strcpy(&pd1[sdlim], "gtmp"/"atmp") - the pd1-interior back-fill
// char loop (row-101 carrier copy fold; the writeCodeDaf strcpy
// precedent).
private fun pd1StoreD(C: DafCompilerState, text: String): Unit {
    for (t in text.indices) C.pd1[C.sdlim + t] = text[t]
}

fun readCodeDaf(): Int {

    val C = DafCompilerState
    val sv = DafInterpState.svPtr

    // fprintf(Out, "\tif (svPtr->in_file[svPtr->io]==NULL) \n");
    C.emit("\tif (svPtr->in_file[svPtr->io]==NULL) \n")

    // fprintf(Out, "\t\tErrorMessage(\"Read from input file\",
    //               svPtr->LineNumber); \n");
    C.emit("\t\tErrorMessage(\\\"Read from input file\\\", " +
            "svPtr->LineNumber); \n")

    while (true) { // while (1)

        // dirflg = 0;
        C.dirflg = 0

        var gotoContRead = false // the cont_read goto fold
        var gotoStore = false // the store goto fold
        var gotoSetReturn = 99 // the set_return ladder falls

        if (at(C.xd, C.xpx) == '!' || at(C.xd, C.xpx) == '#') {
            // carriage control
            val i = 2
            val argI = if (at(C.xd, C.xpx) != '#') i else 1 // ! -> 2, # -> 1
            // inout(&pd1[sdlim], i, ""); (the "" col = discarded cell)
            inoutDaf(DafCRuntime.cstringAt(C.pd1, C.sdlim), argI,
                    IntCellD(0))
            gotoContRead = true
        }

        if (!gotoContRead && at(C.xd, C.xpx) == '?') { // /* tab format */
            C.t2 = 1
            C.xpx++
            C.ierr = parseDaf(sv)
            if (C.ierr != 0) return PARSE_ERROR_D // goto parse_err;
            // inout(&pd1[sdlim], i, &pd1[sdlim]); (col = the pd1 interior;
            // the write-back rides the IntCellD carrier)
            inoutDaf(DafCRuntime.cstringAt(C.pd1, C.sdlim), 3,
                    IntCellD(0))
            gotoContRead = true
        }

        if (!gotoContRead && at(C.xd, C.xpx) == '"') { // text first
            C.t2 = 1
            C.ierr = parseDaf(sv)
            if (C.ierr != 0) return PARSE_ERROR_D // goto parse_err;
            // inout(&pd1[sdlim], i, ""); (the "" col = discarded cell)
            inoutDaf(DafCRuntime.cstringAt(C.pd1, C.sdlim), 4,
                    IntCellD(0))
            gotoContRead = true
        }

        if (!gotoContRead && at(C.xd, C.xpx) == '*') { // direct mode
            C.xpx++
            C.dirflg = 1
        }

        if (!gotoContRead) {

            // retz = READ;
            C.retz = CMD_READ_D

            // return_code = set_code(SET); // process item to read
            val rc = setCodeDaf(SET_CODE_SET_D)

            if (rc == 0) gotoSetReturn = 0 // goto set_return;
            else if (rc == PARSE_ERROR_D) return PARSE_ERROR_D
            else if (rc == ARG_LIST_ERROR_D) return ARG_LIST_ERROR_D
            else if (rc == NO_GLOBAL_PERMITTED_D) return NO_GLOBAL_PERMITTED_D
            else if (rc == MEM_ERROR_D) return MEM_ERROR_D
            else if (rc == TOO_MANY_LHS_D) return TOO_MANY_LHS_D
            else if (rc == ABRTX_D) return ABRTX_D
            else return 99
        }

        // set_return:
        if (!gotoContRead && (gotoSetReturn != 99 || false)) {
        }
        // (the set_return label fold: the plain-text argument reaches
        // set_return via gotoSetReturn==0; the !/#/?/\" arms bypass it
        // through the cont_read fold)

        if (!gotoContRead) {

            if (at(C.xd, C.xpx + 1) == ':') { // timeout
                C.t2 = 1
                C.xpx += 2
                C.ierr = parseDaf(sv)
                if (C.ierr != 0) return PARSE_ERROR_D // goto parse_err;

                // fprintf(Out, " // TIMEOUT REQUESTED FOR %s SECONDS\n",
                //               &pd1[sdlim]);
                C.emit(" // TIMEOUT REQUESTED FOR " +
                        DafCRuntime.cstringAt(C.pd1, C.sdlim) +
                        " SECONDS\n")
                // (the linepad file[5]=stdin line is commented out at
                // the vendor; stays comment-only)

                // fprintf(Out, "\tif (svPtr->io==5) fflush(stdout);\n");
                C.emit("\tif (svPtr->io==5) fflush(stdout);\n")

                // fprintf(Out, "\tsvPtr->tpx = TimeOut_getstr1(svPtr->io,
                //     gtmp, (char *) %s, %d); // Read string into gtmp\n",
                //     &pd1[sdlim], dirflg);
                C.emit("\tsvPtr->tpx = TimeOut_getstr1(svPtr->io, " +
                        "gtmp, (char *) " +
                        DafCRuntime.cstringAt(C.pd1, C.sdlim) + ", " +
                        C.dirflg + "); // Read string into gtmp\n")

                // fprintf(Out, "\tif (svPtr->tpx >= 0) svPtr->tpx = 1;
                //     else svPtr->tpx=0; // Set \$Test\n");
                C.emit("\tif (svPtr->tpx >= 0) svPtr->tpx = 1; " +
                        "else svPtr->tpx=0; // Set \$Test\n")

                // strcpy(&pd1[sdlim], "gtmp");
                pd1StoreD(C, "gtmp")
                C.xpx--
                gotoStore = true // goto store;
            }

            if (!gotoStore && C.dirflg == 1) {
                // fprintf(Out, "\tsvPtr->tpx=DirectRead(svPtr->io,gtmp);
                //     // Read Direct\n");
                C.emit("\tsvPtr->tpx=DirectRead(svPtr->io,gtmp);" +
                        " // Read Direct\n")
                // fprintf(Out, "\tsprintf((char *)atmp,\"%%d\",
                //     (char *) gtmp[0]);\n");
                C.emit("\tsprintf((char *)atmp,\\\"%d\\\",(char *) " +
                        "gtmp[0]);\n")
                // strcpy(&pd1[sdlim], "atmp");
                pd1StoreD(C, "atmp")
            } else if (!gotoStore) {
                // fprintf(Out, "\tsvPtr->tpx =
                //     getstr1(svPtr->in_file[svPtr->io], &gtmp);
                //     // Read string into gtmp\n");
                C.emit("\tsvPtr->tpx = getstr1(svPtr->" +
                        "in_file[svPtr->io], &gtmp);" +
                        " // Read string into gtmp\n")
                // fprintf(Out, "\tif (svPtr->tpx >= 0) svPtr->tpx = 1;
                //     else svPtr->tpx = 0; // Set \$Test\n");
                C.emit("\tif (svPtr->tpx >= 0) svPtr->tpx = 1; " +
                        "else svPtr->tpx = 0; // Set \$Test\n")
                // strcpy(&pd1[sdlim], "gtmp");
                pd1StoreD(C, "gtmp")
            }
        }

        // store:
        if (!gotoContRead) {

            // retz = READ;
            C.retz = CMD_READ_D

            // return_code = set_code(SET_STORE); // process item
            val rc = setCodeDaf(SET_STORE_D)

            if (rc == 0) gotoContRead = true // goto cont_read;
            else if (rc == PARSE_ERROR_D) return PARSE_ERROR_D
            else if (rc == ARG_LIST_ERROR_D) return ARG_LIST_ERROR_D
            else if (rc == NO_GLOBAL_PERMITTED_D) return NO_GLOBAL_PERMITTED_D
            else if (rc == MEM_ERROR_D) return MEM_ERROR_D
            else if (rc == TOO_MANY_LHS_D) return TOO_MANY_LHS_D
            else if (rc == ABRTX_D) return ABRTX_D
        }

        // cont_read:
        C.xpx++
        C.dirflg = 0
        if (at(C.xd, C.xpx) == ',') C.xpx++

        // if (xd[xpx]==0 || xd[xpx]==' ' || xd[xpx]=='\t') {
        if (at(C.xd, C.xpx) == NUL_D ||
                at(C.xd, C.xpx) == ' ' ||
                at(C.xd, C.xpx) == '\t') {

            if (C.POST_flg != 0) { // if (POST_flg)
                // fprintf(Out, "\t} /* post conditional cont_read */ \n");
                C.emit("\t} /* post conditional cont_read */ \n")
                C.POST_flg = 0
            }
            return 0 // goto next_cmnd;
        }
    }
} // read_code: the vendor outer return 0 is the while exit fold -
  // the loop only exits through the returns above
