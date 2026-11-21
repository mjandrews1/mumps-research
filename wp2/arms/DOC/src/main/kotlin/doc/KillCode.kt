package doc

// Unit 271 (B9 tranche 3): compiler-kill.c::kill_code (batch 9,
// complex stratum; units.csv row 251: loc 132, score 110.8,
// rank 278; physLOC 132 physically verified this session) -
// the KILL command compiler emitter, the kill-side sibling of
// the landed unit-121 set_code arm (SetCode.kt; the retz KILL
// token rides the set_code entry, the LockCode/NewCode
// precedent call shape verbatim). Four families: the bare KILL
// (xpx sits on space/NUL/tab - the _SymFree(0) emission), the
// @-indirection arm (the atTest/atCopy/INTERPRET macro seat
// sequence of SetCode, hit-reuse verbatim, the "k " seed and
// the post conditional 3053 fossil kept byte-identical), the
// specific-kill chain (parse_ compiler-twin seat, cvar guard
// with the KILL_DCL_ERROR arm, the sym_(2,...) //...* kill
// specific emission with Extract1 over the pd1 tail at
// sdlim, the k003 comma chain), and the two paren families:
// kill-all-except (the emitted nmes list build - malloc
// domain folds to the emitted C text verbatim, the p1->name
// strlen+1 idiom and the ErrorMessage("Out of memory") pair
// are EMITTED bytes, not Kotlin calls; the vd atmp/keyfix
// branch and the quoted-vd alias; the kill2 loopback; sym_(5)
// close) and kill-globals (k004: the v1d printable-tail scan
// building the 0x01-terminated t walk, the strcatx(gtmp)
// emission via Stubs.Extract2(t,0) - vendor Extract2(t) is
// the offset-0 call, the IfCode H-014 seat-conversion
// precedent; the GKILL XNEXT sibling Mglobal block kept
// byte-identical; the commented-out duplicate block before it
// kept verbatim as the vendor fossil, H-013).
//
// The k004 while(1) scaffolding: every vendor exit from the
// loop body jumps to k002 OUT of the loop, so the loop
// back-edge is unreachable - the body renders once with the
// dead-control note (H-013). The vendor label chain
// (k001/k002/k003/k004/kill2/kill_return) renders as the
// explicit label state machine below (the LockCode flag
// precedent, generalized to the fuller graph: k002 CAN chain
// back to k003 and from there to k004 again, so the revisit
// is live and rendered). Unsigned/UNS casts fold per H-012.
// retz KILL stages the set_code SET pass (SET 0,
// compiler-macros.h:83; KILL 'k' :108). Consult row logged
// 2026-11-22T05:34:00Z (H-014 hit-reuse, the set_code seat
// family). Naming per H-017 (kill_code kept verbatim).

private const val PARSE_ERROR_KC = 1 // compiler-macros.h:69
private const val ARG_LIST_ERROR_KC = 2 // compiler-macros.h:70
private const val NO_GLOBAL_PERMITTED_KC = 3 // compiler-macros.h:71
private const val MEM_ERROR_KC = 4 // compiler-macros.h:72
private const val TOO_MANY_LHS_KC = 5 // compiler-macros.h:73
private const val ABRTX_KC = 6 // compiler-macros.h:74
private const val KILL_DCL_ERROR_KC = 7 // compiler-macros.h:75
private const val SET_KC = 0 // compiler-macros.h:83
private const val TAB_KC = '\t' // the bare-KILL exit idiom

fun kill_code(): Int {
    // Kill all locals: xpx sits on space/NUL/tab
    if (CompilerState.xd[CompilerState.xpxIdx()] == ' ' ||
        CompilerState.xd[CompilerState.xpxIdx()] == NUL ||
        CompilerState.xd[CompilerState.xpxIdx()] == TAB_KC
    ) {
        CompilerState.emit("\t_SymFree(0,svPtr);\n")
        return atPostConditional() // goto k001;
    }

    // Check for indirection in statement
    CompilerState.at_flg = atTest(
        CompilerState.xd, CompilerState.pd1, CompilerState.pd1Last,
        CompilerState.lines, CompilerState.xpx
    )
    if (CompilerState.at_flg == -1) return ABRTX_KC // goto abrtx;

    if (CompilerState.at_flg != 0) {
        val tmp = CharArray(1024)
        CstringOps.strcpyS(tmp, "k ")
        atCopy(CompilerState.at_flg, tmp)

        CompilerState.interpret(CstringOps.cstring(tmp))

        CompilerState.xpx = CompilerState.at_flg.toLong()
        if (CompilerState.POST_flg != 0) {
            CompilerState.emit("\t} // post conditional 3053\n")
            CompilerState.POST_flg = 0
        }
        return 0 // goto next_cmnd;
    }

    var label = 3 // k003
    while (true) {
        when (label) {

        3 -> { // k003
            if (CompilerState.xd[CompilerState.xpxIdx()] == '^') {
                label = 4 // goto k004;
                continue
            }

            if (CompilerState.xd[CompilerState.xpxIdx()] != '(') {
                // Kill specific
                CompilerState.t2 = 1
                CompilerState.ierr = Stubs.parse_()
                if (CompilerState.ierr != 0) return PARSE_ERROR_KC // goto parse_err;
                if (cvar(CstringOps.cstringAt(CompilerState.v1d, 1)) != 0) {
                    return KILL_DCL_ERROR_KC // goto kill_dcl_err;
                }

                CompilerState.emit(
                    "\tsym_(2, (UNS) " +
                    CstringOps.cstring(Stubs.Extract1(pd1TailKC())) +
                    ", (UNS) \"\",svPtr); //...* kill specific\n"
                )

                if (CompilerState.xd[CompilerState.xpxIdx()] == ',') {
                    CompilerState.xpx++
                    continue // goto k003;
                }

                label = 1 // goto k001;
                continue
            }

            // Kill all locals except...
            CompilerState.emit("\t{ \n\tstruct nmes *p1, *p2; \n")
            CompilerState.emit("\tsvPtr->nstart = NULL;\n")
            CompilerState.xpx++

            // kill2 / kill_return chain (paren-argument walk)
            while (true) {
                // kill2:
                CompilerState.t0px = 1
                CompilerState.t2 = 1
                CompilerState.retz = 'k'.code // KILL token, compiler-macros.h:108

                val return_code = Stubs.set_code(SET_KC) // goto set; process item
                if (return_code == PARSE_ERROR_KC) return PARSE_ERROR_KC
                if (return_code == ARG_LIST_ERROR_KC) return ARG_LIST_ERROR_KC
                if (return_code == NO_GLOBAL_PERMITTED_KC) return NO_GLOBAL_PERMITTED_KC
                if (return_code == MEM_ERROR_KC) return MEM_ERROR_KC
                if (return_code == TOO_MANY_LHS_KC) return TOO_MANY_LHS_KC
                if (return_code == ABRTX_KC) return ABRTX_KC

                // kill_return:
                if (CstringOps.cstring(CompilerState.vd) == "atmp") {
                    CompilerState.emit("\tkeyfix(atmp); \n")
                } else {
                    val tmpq = CharArray(256)
                    CstringOps.strcpyS(
                        tmpq, "\"" + CstringOps.cstring(CompilerState.vd) + "\""
                    )
                    CstringOps.strcpyC(CompilerState.vd, tmpq)
                }

                CompilerState.emit("\tp1 = (struct nmes *) malloc(sizeof (struct nmes));\n")
                CompilerState.emit(
                    "\tif (p1 == NULL) ErrorMessage(\"Out of memory\", " +
                    "svPtr->LineNumber);\n"
                )
                CompilerState.emit(
                    "\tp1->name = (char *) malloc(strlen((const char *) " +
                    CstringOps.cstring(CompilerState.vd) + ") + 1);\n"
                )
                CompilerState.emit(
                    "\tif (p1->name == NULL) ErrorMessage(\"Out of memory\", " +
                    "svPtr->LineNumber);\n"
                )
                CompilerState.emit(
                    "\tstrmove((UNS) p1->name, (UNS) " +
                    CstringOps.cstring(CompilerState.vd) + ");\n"
                )
                CompilerState.emit("\tp1->next = svPtr->nstart;\n")
                CompilerState.emit("\tsvPtr->nstart = p1;\n")
                CompilerState.xpx++

                if (CompilerState.xd[CompilerState.xpxIdx()] != ')' &&
                    CompilerState.xd[CompilerState.xpxIdx()] != NUL
                ) {
                    CompilerState.xpx++
                    continue // goto kill2;
                }

                CompilerState.emit(
                    "\tsym_(5, (UNS) \"\",(UNS) \"\", svPtr); // kill all except\n"
                )
                CompilerState.emit("\t}\n")
                break // fall to k002
            }

            label = 2 // k002
        }

        4 -> { // k004: Kill globals
            CompilerState.t0px = 1
            CompilerState.ierr = Stubs.parse_()
            if (CompilerState.ierr != 0) return PARSE_ERROR_KC // goto parse_err;

            CompilerState.emit("\tstrmove( (UNS) gtmp, (UNS) \"\");\n")

            val t = CharArray(1024)
            val v1d = CompilerState.v1d
            var i = 1
            var j = 0
            while (v1d[i] != NUL) {
                if (v1d[i] >= ' ' && v1d[i] <= '\u007f') {
                    t[j] = v1d[i]
                    j++
                    i++
                } else break
            }
            t[j] = CHAR_1
            j++
            t[j] = NUL

            CompilerState.emit(
                "\tstrcat((char *) gtmp,(char *) \"" + CstringOps.cstring(t) + "\"); \n"
            )

            if (v1d[i] != NUL) {
                i++
                while (true) {
                    j = 0
                    while (v1d[i] <= '\u007f' && v1d[i] >= ' ') {
                        t[j] = v1d[i]
                        j++
                        i++
                    }
                    t[j] = NUL
                    CompilerState.emit(
                        "\tstrcatx((char *) gtmp,(char *) " +
                        CstringOps.cstring(Stubs.Extract2(t, 0)) + "); \n"
                    )
                    // the vendor commented-out duplicate block (the j
                    // walk with the 0x01 tail and the plain strcat
                    // emission) kept verbatim in the vendor fossil,
                    // H-013
                    if (v1d[i] != NUL) {
                        i++
                        if (v1d[i] != NUL) continue else break
                    } else break
                }
            }

            CompilerState.emit("\t{ \n")
            CompilerState.emit("\tunsigned char tmp14[STR_MAX], tmp15[STR_MAX]; \n")
            CompilerState.emit("\tkeyfix(gtmp);\n")
            CompilerState.emit("\t_k = strlen((const char *) gtmp); \n") // original length
            CompilerState.emit("\tstrmove((UNS) tmp14, (UNS) gtmp); \n")
            CompilerState.emit("\tMglobal(GKILL, gtmp, tmp15, svPtr); \n") // kill first
            CompilerState.emit("\twhile(1) { \n")
            CompilerState.emit("\t\t_j = Mglobal(XNEXT, gtmp, tmp15, svPtr); \n") // kill others like it
            CompilerState.emit("\t\tif (_j && strncmp( (CCHR) tmp14, (CCHR) gtmp,_k) == 0) { \n")
            CompilerState.emit("\t\t\tMglobal(GKILL, gtmp, tmp15, svPtr); \n")
            CompilerState.emit("\t\t\tcontinue; \n")
            CompilerState.emit("\t\t\t} \n")
            CompilerState.emit("\t\tbreak; \n")
            CompilerState.emit("\t\t} \n")
            CompilerState.emit("\t}\n")
            // the vendor while(1) back-edge is unreachable: every
            // exit jumps out to k002 (H-013 dead-control note);
            // body once

            label = 2 // k002
        }

        2 -> { // k002:
            CompilerState.xpx++ // the vendor pre-increment read
            if (CompilerState.xd[CompilerState.xpxIdx()] == ',') {
                CompilerState.xpx++
                label = 3 // goto k003;
            } else {
                label = 1 // fall to k001
            }
        }

        1 -> { // k001:
            return atPostConditional()
        }
        }
    }
}

// the shared k001 tail of kill_code: the post conditional 3208
// fossil close and the return to the command dispatcher.
private fun atPostConditional(): Int {
    if (CompilerState.POST_flg != 0) {
        CompilerState.emit("\t} /* post conditional 3208*/\n")
        CompilerState.POST_flg = 0
    }
    return 0 // goto next_cmnd;
}

// the pd1 tail at sdlim (the vendor &pd1[sdlim] address fold):
// a fresh c-string carrier over the slice, offset-aware
private fun pd1TailKC(): CharArray {
    val off = CompilerState.sdlim
    val pd1 = CompilerState.pd1
    val tail = CharArray(PS1 - off)
    var k = 0
    while (pd1[off + k] != NUL) {
        tail[k] = pd1[off + k]
        k++
    }
    tail[k] = NUL
    return tail
}
