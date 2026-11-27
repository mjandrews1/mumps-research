package daf


// Unit 65 (DAF supplementary render session, 2026-11-27, per the
// 2026-11-26 PI adjudication of the B10 flags):
// compiler-new.c::new_code (units.csv record 65, grep line 66 - the
// ledger's r65 pin; 106 LOC, batch-3 complex stratum; the named
// stub newCodeDaf raised at B10 is RELEASED by this render,
// same-name body here, release note at the stub site).
// The NEW command compiler arm: the no-argument form emits the
// symbol-overflow guard + nstart NULL + sym_(7,...) reset cell and
// flags ExecSym; the specific '(' form loops new2 through
// set_code(SET) (setCodeDaf, unit-209 seat) with the six-code
// return ladder, quote-wraps vd (or the atmp keyfix cell), emits
// the nmes malloc/strmove/push text verbatim, and walks the comma
// list to the ')' close; the inclusive form flags ExecSym, flushes
// the postconditional, then loops new3 through set_code(SET) with
// retz = NEW1 ('N'), the exportflag SymMarkCopyFlag vs sym_(100)
// emit pair, the ' '-terminated list walk and the trailing
// for-loop skip to end-of-command.
//
// Consults (BASE heuristic rows): 2/26 hit-reuse (Sysout/fprintf
// (Out) -> DafCompilerState.emit, emitted C text verbatim, incl
// the malloc/strmove block and the _doStackN cell); 6/22/62
// hit-reuse (TU state rides DafCompilerState: xd/xpx/vd/t0px/t2/
// retz/POST_flg/ExecSym/ExecX; exportflag rides the shared
// exportFlagD seat - the main render's private exportFlagMD
// carrier is consolidated to the shared seat this session, main's
// ELSE-X write rebound, disclosure below); 30 hit-reuse (vd cells);
// 56 hit-reuse (strcmp(vd,"atmp") -> String compare); 22 hit-reuse
// (malloc NULL arms are unreachable on the JVM - GC ledger - but
// the EMITTED malloc NULL text stays verbatim); 92 hit-reuse (the
// emitted sym_ SYMSTORE/NEW text verbatim, SymMarkCopyFlag macro
// name kept for the C preprocessor); 99 hit-reuse (return-fold of
// the vendor goto ladder: new2/new3 render as labeled while
// machines); 116/121/122 hit-reuse (the setCodeDaf command seat
// and the return-code ladder consts).
// Vendor quirks kept: (1) the unreachable exports: label is
// comment-folded (no goto traffic); (2) the quote-wrap writes the
// "name" literal through a tmp[256] then strcpy(vd,tmp) - rendered
// as the String wrap + carrier refill (row 56 fold); (3) the
// emitted malloc(strlen((const char *) atmp+1)) pointer-plus-one
// text is verbatim (vendor cell); (4) the inclusive tail's
// for( ; xd[xpx]!=' ' && xd[xpx]!=0; xpx++) double-walk after the
// new3 loop's own ' ' exit - kept verbatim (the loop exits on the
// same predicate, the for is the vendor's belt-and-braces skip).
// Dafny: EXEMPT (emit-side procedural arm, the T2 exemption
// family; journal row).

// short exportflag - the SHARED compiler global (vendor externs
// family; written by main's ELSE-X cell, read here). The main
// render's private exportFlagMD carrier is consolidated to this
// public seat this session (main's write site rebound; the
// consolidation disclosed in the fragment).
var exportFlagD = 0

fun newCodeDaf(): Int {

    val C = DafCompilerState

    // exportflag = 0;
    exportFlagD = 0

    // if (xd[xpx]==' '||xd[xpx]==0) - no argument form
    if (at(C.xd, C.xpx) == ' ' || at(C.xd, C.xpx) == NUL_D) {

        C.emit("\tif (svPtr->_Sym>SYM_MAX) ")
        C.emit("ErrorMessage(\"Symbol Table Overflow\",svPtr->LineNumber);\n")
        C.emit("\tsvPtr->nstart=NULL;\n")
        C.emit("\tsym_(7,(UNS) \"\", (UNS) \"\", svPtr);\n")
        C.ExecSym[C.ExecX] = 1

        if (C.POST_flg != 0) {
            C.emit("\t} /* post conditional in new */\n")
            C.POST_flg = 0
        }

        C.xpx++
        return 0 // goto next_cmnd
    }

    // if (xd[xpx] == '(') - specific new except (...) form
    if (at(C.xd, C.xpx) == '(') {

        C.emit("\t{\n\tstruct nmes *p1,*p2;\n")
        C.emit("\tsvPtr->nstart=NULL;\n")
        C.xpx++

        while (true) { // new2:
            C.t0px = 1
            C.t2 = 1
            C.retz = CMD_NEW_D

            // return_code = set_code(SET); - with the vendor
            // six-code return ladder
            val returnCode = setCodeDaf(SET_CODE_SET_D)
            if (returnCode == PARSE_ERROR_D) return PARSE_ERROR_D
            if (returnCode == ARG_LIST_ERROR_D) return ARG_LIST_ERROR_D
            if (returnCode == NO_GLOBAL_PERMITTED_D) return NO_GLOBAL_PERMITTED_D
            if (returnCode == MEM_ERROR_D) return MEM_ERROR_D
            if (returnCode == TOO_MANY_LHS_D) return TOO_MANY_LHS_D
            if (returnCode == ABRTX_D) return ABRTX_D

            // new_return:
            if (DafCRuntime.cstring(C.vd) == "atmp") {
                C.emit("\tkeyfix(atmp);\n")
            } else {
                val tmpS = "\"" + DafCRuntime.cstring(C.vd) + "\""
                strcpyAtNewCode(C.vd, 0, tmpS) // strcpy(vd, tmp)
            }

            C.emit("\tp1=(struct nmes *) malloc(sizeof (struct nmes));\n")
            C.emit("\tif (p1==NULL) ErrorMessage(\"Out of memory\",svPtr->LineNumber);\n")
            C.emit("\tp1->name=(char *) malloc(strlen((const char *) atmp+1));\n")
            C.emit("\tif (p1->name==NULL) ErrorMessage(\"Out of memory\",svPtr->LineNumber);\n")

            C.emit("\tstrmove((UNS) p1->name, (UNS) " +
                    DafCRuntime.cstring(C.vd) + ");\n")

            C.emit("\tp1->next=svPtr->nstart;\n")
            C.emit("\tsvPtr->nstart=p1;\n")
            C.xpx++

            if (at(C.xd, C.xpx) != ')' && at(C.xd, C.xpx) != NUL_D) {
                C.xpx++
                continue // goto new2
            }

            C.emit("\tsym_(7,(UNS) \"\",(UNS) \"\",svPtr); // new except (...) \n")
            C.emit("\t}\n")
            C.ExecSym[C.ExecX] = 1

            if (C.ExecX == 0) {
                C.emit("\tif (_doStx == 0) ")
                C.emit("\t_doStackN[_doStx-1]=1;\n")
            }

            if (C.POST_flg != 0) {
                C.emit("\t} /* post conditional new return*/\n")
                C.POST_flg = 0
            }

            C.xpx++
            return 0 // goto next_cmnd
        }
    }

    // Push locals inclusive (vendor: ExecSym flag + post flush,
    // then the exports/new3 loop; the unreachable exports: label
    // carries no goto traffic - comment-folded)
    C.ExecSym[C.ExecX] = 1

    if (C.POST_flg != 0) {
        C.emit("\t} /* post conditional new push */\n")
        C.POST_flg = 0
    }

    while (true) { // new3:
        C.t0px = 1
        C.t2 = 1
        C.retz = CMD_NEW1_D

        val returnCode = setCodeDaf(SET_CODE_SET_D)
        if (returnCode == PARSE_ERROR_D) return PARSE_ERROR_D
        if (returnCode == ARG_LIST_ERROR_D) return ARG_LIST_ERROR_D
        if (returnCode == NO_GLOBAL_PERMITTED_D) return NO_GLOBAL_PERMITTED_D
        if (returnCode == MEM_ERROR_D) return MEM_ERROR_D
        if (returnCode == TOO_MANY_LHS_D) return TOO_MANY_LHS_D
        if (returnCode == ABRTX_D) return ABRTX_D

        // new1_return:
        if (DafCRuntime.cstring(C.vd) == "atmp") {
            C.emit("\tkeyfix(atmp);\n")
        } else {
            val tmpS = "\"" + DafCRuntime.cstring(C.vd) + "\""
            strcpyAtNewCode(C.vd, 0, tmpS)
        }

        if (exportFlagD != 0) {
            C.emit("\tsym_(SymMarkCopyFlag, (UNS) " +
                    DafCRuntime.cstring(C.vd) +
                    ", (UNS) \"\",svPtr); // mark copy flag\n")
        } else {
            C.emit("\tsym_(100,(UNS) " + DafCRuntime.cstring(C.vd) +
                    ",(UNS) \"\",svPtr); // create new variable at this level\n")
        }
        C.xpx++

        if (at(C.xd, C.xpx) != ' ' && at(C.xd, C.xpx) != NUL_D) {
            C.xpx++
            continue // goto new3
        }
        break
    }

    // for( ; xd[xpx]!=' ' && xd[xpx]!=0; xpx++); - the trailing
    // skip to end-of-command (vendor belt-and-braces walk)
    while (at(C.xd, C.xpx) != ' ' && at(C.xd, C.xpx) != NUL_D) C.xpx++

    return 0 // goto next_cmnd
}

// strcpy analog into a CharArray at offset (per-file private,
// main's copyStrAt twin per the per-unit rule; bounds-bounded).
private fun strcpyAtNewCode(dst: CharArray, off: Int, s: String) {
    var i = 0
    val n = dst.size - 1 - off
    while (i < s.length && i < n) { dst[off + i] = s[i]; i++ }
    dst[off + i] = NUL_D
}
