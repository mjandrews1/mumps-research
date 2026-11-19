package doc

// Unit 91 (B3 tranche 3 / sub-push B): compiler-new.c::new_code (batch
// 3, complex stratum; LOC 148, score 26.7; file batch-3 corpus row:
// compiler-new.c, June 2024 banner above the cut - file-residue rows
// of that banner belong to the misc ingest of a later chunk). The NEW
// command compiler: three arms - the no-argument form (emits the
// Sym-overflow guard, nstart NULL, the sym_(7,...) release and any
// pending post-conditional epilogue), the Push-all-locals-except
// form (paren list: per item, set_code(SET) processes the name, the
// atmp token emits keyfix (the Stubs.keyfix seat), other names are
// quoted and emitted through the nmes linked-list build), and the
// Push-locals-inclusive default (name list: same set_code walk, then
// either the sym_(SymMarkCopyFlag,...) mark-copy emission when
// exportflag was set by the SET pass or the sym_(100,...) level
// create). The nonzero return_code cascade of set_code is returned
// verbatim (each of the six error macros). Vendor quirks kept and
// noted: SET is defined TWICE in compiler-macros.h (line 83: 0, the
// set entry-point table; line 91: 's', the command table) - the
// C preprocessor takes the LAST redefinition without an intervening
// undef, so every translation unit of this build sees SET == 's'
// (code 115) - the render passes that value with this quirk ledgered
// (the first definition is dead); ExecSym[ExecX] = 1 is set in all
// three arms; the trailing skip-run for( ; xd[xpx]!=blank; xpx++)
// skip-run of the third arm is kept verbatim after the loop break;
// the exports: label immediately follows the POST_flg epilogue of the
// third arm and is fall-through-only (dead label, H-013, ledger
// here). New CompilerState seats (H-004 one owner per C entity):
// exportflag (compiler-globals.h:126 extern short), ExecSym[25] plus
// ExecX (compiler-globals-ext.h:130-131), retz (:169), t0px (:39), vd
// (vendor vd[1024], :170; carried at STR_MAX with the sizing
// divergence ledgered at the carrier, H-007). set_code rides the NEW
// Stubs.set_code seat (owner compiler-set.c::set_code, batch-4
// unit); set_code's SET argument is the set entry-point table token
// of the quirk note above. The goto loops new2/new3 linearize per
// H-011 as Kotlin while-true loops with the shared vd/quoted-name
// tail; Sysout renders as CompilerState.emit (IfCode unit-58
// precedent, cookbook H-002/H-003). Naming per H-017 (new_code is
// kept verbatim, the sym_ naming precedent for C-ABI snake names).

private const val PARSE_ERROR_91 = 1 // compiler-macros.h:69
private const val ARG_LIST_ERROR_91 = 2 // compiler-macros.h:70
private const val NO_GLOBAL_PERMITTED_91 = 3 // compiler-macros.h:71
private const val MEM_ERROR_91 = 4 // compiler-macros.h:72
private const val TOO_MANY_LHS_91 = 5 // compiler-macros.h:73
private const val ABRTX_91 = 6 // compiler-macros.h:74

private const val SET_91 = 's' // compiler-macros.h:91 (the LAST of the
// two SET defines - see the quirk ledger in the unit KDoc above);
// passed to set_code as the set entry-point token

private const val NEW_91 = 'n' // compiler-macros.h:96
private const val NEW1_91 = 'N' // compiler-macros.h:97

private const val SYM_MARK_COPY_FLAG_91 = 102 // defines.h:53

fun new_code(): Int {
    var return_code: Int
    CompilerState.exportflag = 0

    if (CompilerState.xd[CompilerState.xpxIdx()] == ' ' ||
        CompilerState.xd[CompilerState.xpxIdx()] == NUL
    ) { // no argument form
        CompilerState.emit("\tif (svPtr->_Sym>SYM_MAX) ")
        CompilerState.emit("ErrorMessage(\"Symbol Table Overflow\",svPtr->LineNumber);\n")
        CompilerState.emit("\tsvPtr->nstart=NULL;\n")
        CompilerState.emit("\tsym_(7,(UNS) \"\", (UNS) \"\", svPtr);\n")
        CompilerState.ExecSym[CompilerState.ExecX] = 1

        if (CompilerState.POST_flg != 0) {
            CompilerState.emit("\t} /* post conditional in new */\n")
            CompilerState.POST_flg = 0
        }
        CompilerState.xpxInc()
        return 0 // goto next_cmnd;
    }

    // Push all locals except...

    if (CompilerState.xd[CompilerState.xpxIdx()] == '(') { // specific

        CompilerState.emit("\t{\n\tstruct nmes *p1,*p2;\n")
        CompilerState.emit("\tsvPtr->nstart=NULL;\n")
        CompilerState.xpxInc()

        // new2: (the vendor goto new2 loop; H-011 while-true form)
        while (true) {
            CompilerState.t0px = 1
            CompilerState.t2 = 1
            CompilerState.retz = NEW_91.code

            return_code = Stubs.set_code(SET_91.code) // goto set;

            if (return_code == PARSE_ERROR_91) return PARSE_ERROR_91 // goto parse_err;
            if (return_code == ARG_LIST_ERROR_91) return ARG_LIST_ERROR_91 // goto arg_list_error;
            if (return_code == NO_GLOBAL_PERMITTED_91) return NO_GLOBAL_PERMITTED_91
            if (return_code == MEM_ERROR_91) return MEM_ERROR_91 // goto mem_err;
            if (return_code == TOO_MANY_LHS_91) return TOO_MANY_LHS_91 // goto too_lhs;
            if (return_code == ABRTX_91) return ABRTX_91 // goto abrtx;

            // new_return:
            var vdStr = CstringOps.cstring(CompilerState.vd)
            if (vdStr == "atmp") {
                CompilerState.emit("\tkeyfix(atmp);\n")
            } else {
                CstringOps.strcpyS(CompilerState.vd, "\"" + vdStr + "\"")
            }

            CompilerState.emit(
                "\tp1=(struct nmes *) malloc(sizeof (struct nmes));\n" +
                    "\tif (p1==NULL) ErrorMessage(\"Out of memory\",svPtr->LineNumber);\n" +
                    "\tp1->name=(char *) malloc(strlen((const char *) atmp+1));\n" +
                    "\tif (p1->name==NULL) ErrorMessage(\"Out of memory\",svPtr->LineNumber);\n"
            )

            vdStr = CstringOps.cstring(CompilerState.vd)
            CompilerState.emit("\tstrmove((UNS) p1->name, (UNS) " + vdStr + ");\n")

            CompilerState.emit(
                "\tp1->next=svPtr->nstart;\n" +
                    "\tsvPtr->nstart=p1;\n"
            )
            CompilerState.xpxInc()

            if (CompilerState.xd[CompilerState.xpxIdx()] != ')' &&
                CompilerState.xd[CompilerState.xpxIdx()] != NUL
            ) {
                CompilerState.xpxInc()
                continue // goto new2;
            }

            CompilerState.emit("\tsym_(7,(UNS) \"\",(UNS) \"\",svPtr); // new except (...) \n")
            CompilerState.emit("\t}\n")
            CompilerState.ExecSym[CompilerState.ExecX] = 1
            if (CompilerState.ExecX == 0) {
                CompilerState.emit("\tif (_doStx == 0) ")
                CompilerState.emit("\t_doStackN[_doStx-1]=1;\n")
            }

            if (CompilerState.POST_flg != 0) {
                CompilerState.emit("\t} /* post conditional new return*/\n")
                CompilerState.POST_flg = 0
            }

            CompilerState.xpxInc()
            return 0 // goto next_cmnd;
        }
    }

    // Push locals inclusive

    CompilerState.ExecSym[CompilerState.ExecX] = 1

    if (CompilerState.POST_flg != 0) {
        CompilerState.emit("\t} /* post conditional new push */\n")
        CompilerState.POST_flg = 0
    }

    // exports: fall-through-only label above new3 (dead, H-013)

    // new3: (the vendor goto new3 loop; H-011 while-true form)
    while (true) {
        CompilerState.t0px = 1
        CompilerState.t2 = 1
        CompilerState.retz = NEW1_91.code

        return_code = Stubs.set_code(SET_91.code) // goto set;

        if (return_code == PARSE_ERROR_91) return PARSE_ERROR_91 // goto parse_err;
        if (return_code == ARG_LIST_ERROR_91) return ARG_LIST_ERROR_91 // goto arg_list_error;
        if (return_code == NO_GLOBAL_PERMITTED_91) return NO_GLOBAL_PERMITTED_91
        if (return_code == MEM_ERROR_91) return MEM_ERROR_91 // goto mem_err;
        if (return_code == TOO_MANY_LHS_91) return TOO_MANY_LHS_91 // goto too_lhs;
        if (return_code == ABRTX_91) return ABRTX_91 // goto abrtx;

        // new1_return:
        var vdStr = CstringOps.cstring(CompilerState.vd)
        if (vdStr == "atmp") {
            CompilerState.emit("\tkeyfix(atmp);\n")
        } else {
            CstringOps.strcpyS(CompilerState.vd, "\"" + vdStr + "\"")
        }

        vdStr = CstringOps.cstring(CompilerState.vd)
        if (CompilerState.exportflag != 0) {
            CompilerState.emit(
                "\tsym_(" + SYM_MARK_COPY_FLAG_91 + ", (UNS) " + vdStr +
                    ", (UNS) \"\",svPtr); // mark copy flag\n"
            )
        } else {
            CompilerState.emit(
                "\tsym_(100,(UNS) " + vdStr +
                    ",(UNS) \"\",svPtr); // create new variable at this level\n"
            )
        }
        CompilerState.xpxInc()

        if (CompilerState.xd[CompilerState.xpxIdx()] != ' ' &&
            CompilerState.xd[CompilerState.xpxIdx()] != NUL
        ) {
            CompilerState.xpxInc()
            continue // goto new3;
        }

        while (CompilerState.xd[CompilerState.xpxIdx()] != ' ' &&
            CompilerState.xd[CompilerState.xpxIdx()] != NUL
        ) {
            CompilerState.xpxInc()
        }
        return 0 // goto next_cmnd;
    }
}
