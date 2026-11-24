package daf

/*
 * DAF batch-9 unit 274 (roster row 274; compiler-kill.c::kill_code,
 * core, LOC 132, score 11.2). Consult 2026-11-24T22:51:00Z.
 *
 * Vendor shape kept (the unit-181 read_code sibling of the
 * compiler converter family): compiler TU globals ride the
 * DafCompilerState singleton exactly as at the read_code render;
 * every fprintf(Out,...) -> DafCompilerState.emit with the
 * emitted C text verbatim; the goto web folds onto the labeled
 * loop lattice below (k001/k002/k003/kill2/kill_return; the
 * k004 while(1) tail arms); the INTERPRET macro rides
 * emitInterpretDaf; kill_return is the inline continuation after
 * setCodeDaf's kill_return 0 (the SetCode "if retz==KILL return 0"
 * arm of this entry).
 *
 * Seats: atTest/atCopyDaf (unit-222 release), parseDaf (the b3
 * seat, still raised - the vendor's parse_ calls ride it; the
 * TAG CORRECTION ledger at DafInterpStubsT3), cvarDaf (the
 * unit-243 release; C-string side), extract1Daf (the unit-243
 * release; the &pd1[sdlim] offset read folds onto the 0-base
 * carrier slice per the SetCode precedent), extract2Daf (the b1
 * seat, still raised), setCodeDaf (the unit-108 real body; the
 * SET entry), errorMessageDaf (the raised seat; the Out of
 * memory emits), the error-ladder consts
 * PARSE_ERROR_D/ARG_LIST_ERROR_D/NO_GLOBAL_PERMITTED_D/
 * MEM_ERROR_D/TOO_MANY_LHS_D/ABRTX_D; KILL_DCL_ERROR 7 has no
 * landed const seat (CompilerMacrosH note) - pinned here as
 * KILL_DCL_ERROR_D per the defines ladder.
 * H-015 hygiene: no raw dollars ride the comments.
 * Dafny: EXEMPT (emitter over the converter-family seats;
 * journal row).
 */

const val KILL_DCL_ERROR_D = 7 // compiler kill error ladder (per-family pin)

fun killCodeD(): Int {

    val C = DafCompilerState
    val sv = DafInterpState.svPtr

    // if (xd[xpx]==' '||xd[xpx]==0||xd[xpx]=='\t') kill all locals
    if (at(C.xd, C.xpx) == ' ' || at(C.xd, C.xpx) == NUL_D ||
        at(C.xd, C.xpx) == TAB_D
    ) {
        C.emit("\t_SymFree(0,svPtr);\n")
        return 0 // goto k001 (POST arm folds; nothing follows it)
    }

    // check for indirection
    C.at_flg = atTest(C.xd, C.pd1, C.pd1Last, C.lines.toShort(), C.xpx)
    if (C.at_flg == 99) return ABRTX_D // goto abrtx;

    if (C.at_flg != 0) {
        val tmp = StringBuilder("k ")
        atCopyDaf(C.at_flg, tmp.toString().toCharArray())
        emitInterpretDaf(tmp.toString()) // INTERPRET
        C.xpx = C.at_flg
        if (C.POST_flg != 0) {
            C.emit("\t} // post conditional 3053\n")
            C.POST_flg = 0
        }
        return 0 // goto next_cmnd;
    }

    // k003: (the ','-re-entry target)
    var k003 = true // loop latches below keep the vendor web shape
    var k004 = false
    var k001 = false
    var k002 = false

    k003@ while (k003 || k004) {
        if (!k004) {
            if (at(C.xd, C.xpx) == '^') { // kill globals
                k004 = true
                continue@k003
            }

            if (at(C.xd, C.xpx) != '(') { // Kill specific
                C.t2 = 1
                C.ierr = parseDaf(sv)
                if (C.ierr != 0) return PARSE_ERROR_D // goto parse_err;
                if (cvarDaf(DafCRuntime.cstring(C.v1d))) return KILL_DCL_ERROR_D

                C.emit("\tsym_(2, (UNS) " + extract1DafCopied(C) +
                        ", (UNS) \"\",svPtr); //...* kill specific\n")

                if (at(C.xd, C.xpx) == ',') {
                    C.xpx++
                    continue@k003 // goto k003
                }
                k001 = true
            } else {
                // Kill all locals except ...
                C.emit("\t{ \n\tstruct nmes *p1, *p2; \n")
                C.emit("\tsvPtr->nstart = NULL;\n")
                C.xpx++

                // kill2: (the item loop)
                kill2@ while (true) {
                    C.t0px = 1
                    C.t2 = 1
                    C.retz = CMD_KILL_D

                    val returnCode = setCodeDaf(SET_CODE_SET_D) // set_code(SET)
                    // (kill_code passes SET 1; the DAF entry seat
                    // name is SET_STORE_D; the retz=KILL carrier
                    // routes SetCode's kill_return arm)

                    if (returnCode == PARSE_ERROR_D) return PARSE_ERROR_D
                    if (returnCode == ARG_LIST_ERROR_D) return ARG_LIST_ERROR_D
                    if (returnCode == NO_GLOBAL_PERMITTED_D) return NO_GLOBAL_PERMITTED_D
                    if (returnCode == MEM_ERROR_D) return MEM_ERROR_D
                    if (returnCode == TOO_MANY_LHS_D) return TOO_MANY_LHS_D
                    if (returnCode == ABRTX_D) return ABRTX_D
                    if (returnCode != 0) return returnCode // set_store ladder
                    // kill_return:
                    if (DafCRuntime.cstring(C.vd) == "atmp") {
                        C.emit("\tkeyfix(atmp); \n")
                    } else {
                        val tmp = "\"" + DafCRuntime.cstring(C.vd) + "\""
                        setStrD(C.vd, tmp) // strcpy(vd, tmp)
                    }

                    C.emit("\tp1 = (struct nmes *) malloc(sizeof (struct nmes));\n")
                    C.emit("\tif (p1 == NULL) ErrorMessage(\"Out of memory\", " +
                            "svPtr->LineNumber);\n")
                    C.emit("\tp1->name = (char *) malloc(strlen((const char *) " +
                            DafCRuntime.cstring(C.vd) + ") + 1);\n")
                    C.emit("\tif (p1->name == NULL) ErrorMessage(\"Out of memory\", " +
                            "svPtr->LineNumber);\n")
                    C.emit("\tstrmove((UNS) p1->name, (UNS) " +
                            DafCRuntime.cstring(C.vd) + ");\n")
                    C.emit("\tp1->next = svPtr->nstart;\n")
                    C.emit("\tsvPtr->nstart = p1;\n")
                    C.xpx++

                    if (at(C.xd, C.xpx) != ')' && at(C.xd, C.xpx) != NUL_D) {
                        C.xpx++
                        continue@kill2 // goto kill2
                    }
                    break
                }

                C.emit("\tsym_(5, (UNS) \"\",(UNS) \"\", svPtr); // kill all except\n")
                C.emit("\t}\n")
                k002 = true
            }
        } else {
            // k004: kill globals (while(1) tail arms)
            k004 = true // sticky: the ',' probe below may loop back
            C.t0px = 1
            C.ierr = parseDaf(sv)
            if (C.ierr != 0) return PARSE_ERROR_D // goto parse_err;

            C.emit("\tstrmove( (UNS) gtmp, (UNS) \"\");\n")

            var t = CharArray(1024)
            var j = 0
            var i = 1
            while (at(C.v1d, i) != NUL_D) {
                if (C.v1d[i] >= ' ' && C.v1d[i] <= '\u007f') {
                    t[j] = C.v1d[i]
                    j++
                    i++
                } else break
            }
            t[j] = '\u0001'
            j++
            t[j] = NUL_D

            C.emit("\tstrcat((char *) gtmp,(char *) \"" +
                    cstring0(t) + "\"); \n")

            if (at(C.v1d, i) != NUL_D) {
                i++
                while (true) {
                    j = 0
                    while (at(C.v1d, i) <= '\u007f' && at(C.v1d, i) >= ' ') {
                        t[j] = C.v1d[i]
                        j++
                        i++
                    }
                    t[j] = NUL_D
                    C.emit("\tstrcatx((char *) gtmp,(char *) " +
                            extract2Daf(cstring0(t)) + ");\n")
                    // the commented-out j=0/\u0001 block stays
                    // comment-only at the vendor (kept, row 46)

                    if (at(C.v1d, ++i) != NUL_D) continue
                    break
                }
            }

            C.emit("\t{ \n")
            C.emit("\tunsigned char tmp14[STR_MAX], tmp15[STR_MAX]; \n")
            C.emit("\tkeyfix(gtmp);\n")
            C.emit("\t_k = strlen((const char *) gtmp); \n") // original length
            C.emit("\tstrmove((UNS) tmp14, (UNS) gtmp); \n")
            C.emit("\tMglobal(GKILL, gtmp, tmp15, svPtr); \n") // kill first
            C.emit("\twhile(1) { \n")
            C.emit("\t\t_j = Mglobal(XNEXT, gtmp, tmp15, svPtr); \n") // kill others like it
            C.emit("\t\tif (_j && strncmp( (CCHR) tmp14, (CCHR) gtmp,_k) == 0) { \n")
            C.emit("\t\t\tMglobal(GKILL, gtmp, tmp15, svPtr); \n")
            C.emit("\t\t\tcontinue; \n")
            C.emit("\t\t\t} \n")
            C.emit("\t\tbreak; \n")
            C.emit("\t\t} \n")
            C.emit("\t}\n")

            // k002:
            if (at(C.xd, ++C.xpx) == ',') {
                C.xpx++
                k004 = false
                continue@k003 // goto k003
            }
            k001 = true
        }

        if (k001 || k002) break
    }

    // k001:
    if (C.POST_flg != 0) {
        C.emit("\t} /* post conditional 3208*/\n")
        C.POST_flg = 0
    }
    return 0 // goto next_cmnd;
}

// Extract1(&pd1[sdlim]) - the offset-side copy (carrier made,
// the SetCode fold precedent).
private fun extract1DafCopied(C: DafCompilerState): String {
    val varB = CharArray(STR_MAX_DAF)
    var q = C.sdlim
    var w = 0
    while (q < C.pd1.size && C.pd1[q] != NUL_D) {
        varB[w] = C.pd1[q]
        q++
        w++
    }
    varB[w] = NUL_D
    return extract1Daf(varB, 0)
}

// strcpy(vd, tmp) carrier write.
private fun setStrD(dst: CharArray, s: String) {
    var q = 0
    while (q < s.length) {
        dst[q] = s[q]
        q++
    }
    dst[q] = NUL_D
}

// t carrier cstring (NUL-terminated view).
private fun cstring0(b: CharArray): String {
    val sb = StringBuilder()
    var q = 0
    while (q < b.size && b[q] != NUL_D) {
        sb.append(b[q])
        q++
    }
    return sb.toString()
}
