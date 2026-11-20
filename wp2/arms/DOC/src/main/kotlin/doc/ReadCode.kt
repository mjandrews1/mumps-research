package doc

// Unit 181 (B6 tranche 3): compiler-read.c::read_code (batch 6,
// complex stratum; roster row 181: loc 96, score 112.2, batch_plan
// strata order). The READ command compiler: the NULL in_file
// guard emission with its ErrorMessage(...Read from input
// file...) pair, the carriage-control / tab / literal-text arms
// through the inout seat (unit 59, real), the setCode seat
// (real, SET 0 then SET_STORE 1 passes) with the 6-code rc
// ladder and the vendor return-99 fall-through on an
// unrecognized rc, the timeout arm emitting the
// TimeOut_getstr1 call text (the unit-170 runtime body behind
// it), the direct arm emitting DirectRead, and the plain arm
// emitting getstr1 - all EMITTED C TEXT, not arm calls (the
// generated-program contract). Seat table: inout and set_code
// are the arm renders; parse_ is the compiler twin Stubs
// seat; READ rides the unit-169 InterpH const (r, interp.h
// command char); retz rides the CompilerState seat; the
// ampersand-pd1-plus-sdlim pointers ride the per-file
// NUL-terminated slice carriers (H-012; inout reads its text
// and col from index 0). h011: the vendor goto cont_read /
// set_return / store family linearizes with the gotoContRead /
// gotoStore flags preserving exactly which arms reach which
// point. Vendor quirks kept and noted: the xpx re-read quirk
// (the timeout arm decrements xpx after the strcpy so the
// set_code store pass re-scans the same site); the
// commented-out linepad fossil stays a comment (no emission);
// the two emitted Set-Test comments carry the dollar tokens
// via the per-file DOLLAR const (value 36) so the emitted C
// text stays byte-identical to the vendor while the arm
// source keeps the no-raw-sigil rule (the emitted-C dollar
// precedent); the vendor inout calls with the empty-string
// literal render as an all-NUL CharArray carrier. Naming
// per H-017.

private const val PARSE_ERROR_RC = 1 // compiler-macros.h:69
private const val ARG_LIST_ERROR_RC = 2 // compiler-macros.h:70
private const val NO_GLOBAL_PERMITTED_RC = 3 // compiler-macros.h:71
private const val MEM_ERROR_RC = 4 // compiler-macros.h:72
private const val TOO_MANY_LHS_RC = 5 // compiler-macros.h:73
private const val ABRTX_RC = 6 // compiler-macros.h:74
private const val SET_RC = 0 // compiler-macros.h:83
private const val SET_STORE_RC = 1 // compiler-macros.h:84

private const val DOLLAR_RC = 36.toChar() // the emitted-C sigil carrier

fun read_code(): Int {
    var return_code: Int
    var dirflg: Int
    var i: Int

    CompilerState.emit("\tif (svPtr->in_file[svPtr->io] == NULL) \n")
    CompilerState.emit(
        "\t\tErrorMessage(\"Read from input file\", " +
                "svPtr->LineNumber); \n"
    )

    var gotoContRead = false
    var gotoStore = false

    while (true) {
        dirflg = 0
        gotoContRead = false
        gotoStore = false

        if (CompilerState.xd[CompilerState.xpxIdx()] == '!' ||
                CompilerState.xd[CompilerState.xpxIdx()] == '#'
        ) { // carriage control
            i = if (CompilerState.xd[CompilerState.xpxIdx()] != '#') 2 else 1
            inout(sliceAtRd(CompilerState.pd1, CompilerState.sdlim), i.toShort(),
                    CharArray(1))
            gotoContRead = true // goto cont_read;
        } else if (CompilerState.xd[CompilerState.xpxIdx()] == '?') {
            // tab format
            CompilerState.t2 = 1
            CompilerState.xpxInc()
            CompilerState.ierr = Stubs.parse_()
            if (CompilerState.ierr != 0) return PARSE_ERROR_RC // goto parse_err;
            i = 3
            inout(sliceAtRd(CompilerState.pd1, CompilerState.sdlim), i.toShort(),
                    sliceAtRd(CompilerState.pd1, CompilerState.sdlim))
            gotoContRead = true // goto cont_read;
        } else if (CompilerState.xd[CompilerState.xpxIdx()] == '"') {
            // text to be printed first
            CompilerState.t2 = 1
            CompilerState.ierr = Stubs.parse_()
            if (CompilerState.ierr != 0) return PARSE_ERROR_RC // goto parse_err;
            i = 4
            inout(sliceAtRd(CompilerState.pd1, CompilerState.sdlim), i.toShort(),
                    CharArray(1))
            gotoContRead = true // goto cont_read;
        } else {
            if (CompilerState.xd[CompilerState.xpxIdx()] == '*') { // direct mode
                CompilerState.xpxInc()
                dirflg = 1
            }

            CompilerState.retz = READ.code // char code seat (short extern Int)

            return_code = Stubs.set_code(SET_RC) // process item to read

            if (return_code == 0) {
                // fall into set_return below
            } else if (return_code == PARSE_ERROR_RC) {
                return PARSE_ERROR_RC
            } else if (return_code == ARG_LIST_ERROR_RC) {
                return ARG_LIST_ERROR_RC
            } else if (return_code == NO_GLOBAL_PERMITTED_RC) {
                return NO_GLOBAL_PERMITTED_RC
            } else if (return_code == MEM_ERROR_RC) {
                return MEM_ERROR_RC
            } else if (return_code == TOO_MANY_LHS_RC) {
                return TOO_MANY_LHS_RC
            } else if (return_code == ABRTX_RC) {
                return ABRTX_RC
            } else {
                return 99
            }

            // set_return:
            if (CompilerState.xd[CompilerState.xpxIdx() + 1] == ':') {
                // timeout
                CompilerState.t2 = 1
                CompilerState.xpx += 2
                CompilerState.ierr = Stubs.parse_()
                if (CompilerState.ierr != 0) return PARSE_ERROR_RC // goto parse_err;
                CompilerState.emit(
                    " // TIMEOUT REQUESTED FOR " +
                            CstringOps.cstringAt(CompilerState.pd1, CompilerState.sdlim) +
                            " SECONDS\n"
                )
                // VENDOR FOSSIL (commented out in the source, no
                // emission): linepad(fprintf(Out, tab file[5]=stdin
                // form),// unit ready for input);

                CompilerState.emit("\tif (svPtr->io==5) fflush(stdout);\n")

                CompilerState.emit(
                    "\tsvPtr->tpx = TimeOut_getstr1(svPtr->io, " +
                            "gtmp, (char *) " +
                            CstringOps.cstringAt(CompilerState.pd1, CompilerState.sdlim) +
                            ", " + dirflg + "); // Read string into gtmp\n"
                )

                CompilerState.emit(
                    "\tif (svPtr->tpx >= 0) svPtr->tpx = 1; " +
                            "else svPtr->tpx=0; // Set " + DOLLAR_RC + "Test\n"
                )

                strcpyAt(CompilerState.pd1, CompilerState.sdlim, "gtmp")
                CompilerState.xpx-- // re-read quirk: de-point so the
                // set_code store pass rescans the same site
                gotoStore = true // goto store;
            }

            if (!gotoStore) {
                if (dirflg == 1) {
                    CompilerState.emit(
                        "\tsvPtr->tpx=DirectRead(svPtr->io,gtmp); // Read Direct\n"
                    )
                    CompilerState.emit(
                        "\tsprintf((char *)atmp,\"%d\",(char *) gtmp[0]);\n"
                    )
                    strcpyAt(CompilerState.pd1, CompilerState.sdlim, "atmp")
                } else {
                    CompilerState.emit(
                        "\tsvPtr->tpx = getstr1(svPtr->in_file[svPtr->io], &gtmp);" +
                                " // Read string into gtmp\n"
                    )
                    CompilerState.emit(
                        "\tif (svPtr->tpx >= 0) svPtr->tpx = 1; else svPtr->tpx = 0;" +
                                " // Set " + DOLLAR_RC + "Test\n"
                    )
                    strcpyAt(CompilerState.pd1, CompilerState.sdlim, "gtmp")
                }
            }

            // store:
            CompilerState.retz = READ.code // char code seat (short extern Int)

            return_code = Stubs.set_code(SET_STORE_RC) // process item to read

            if (return_code == 0) {
                gotoContRead = true // goto cont_read;
            } else if (return_code == PARSE_ERROR_RC) {
                return PARSE_ERROR_RC
            } else if (return_code == ARG_LIST_ERROR_RC) {
                return ARG_LIST_ERROR_RC
            } else if (return_code == NO_GLOBAL_PERMITTED_RC) {
                return NO_GLOBAL_PERMITTED_RC
            } else if (return_code == MEM_ERROR_RC) {
                return MEM_ERROR_RC
            } else if (return_code == TOO_MANY_LHS_RC) {
                return TOO_MANY_LHS_RC
            } else if (return_code == ABRTX_RC) {
                return ABRTX_RC
            }

            // the vendor falls through to cont_read with the
            // return_code still set (no fall-through return here)
        }

        // cont_read:
        CompilerState.xpxInc()
        dirflg = 0
        if (CompilerState.xd[CompilerState.xpxIdx()] == ',') CompilerState.xpxInc()

        if (CompilerState.xd[CompilerState.xpxIdx()] == NUL ||
                CompilerState.xd[CompilerState.xpxIdx()] == ' ' ||
                CompilerState.xd[CompilerState.xpxIdx()] == '\t'
        ) {
            if (CompilerState.POST_flg != 0) {
                CompilerState.emit("\t} /* post conditional cont_read */\n")
                CompilerState.POST_flg = 0
            }
            return 0 // goto next_cmnd;
        }
    }
}

// the &pd1[sdlim] pointer carrier (H-012): a fresh STR_MAX
// buffer holding the tail of buf from offset
private fun sliceAtRd(buf: CharArray, offset: Int): CharArray {
    val out = CharArray(STR_MAX)
    CstringOps.copyAt(out, 0, buf.copyOfRange(offset, buf.size))
    return out
}

// the strcpy(&pd1[sdlim], s) pointer fold: the in-place copy
// with the NUL terminator
private fun strcpyAt(buf: CharArray, offset: Int, s: String) {
    for (k in s.indices) buf[offset + k] = s[k]
    buf[offset + s.length] = NUL
}
