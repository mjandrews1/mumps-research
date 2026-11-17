package mumps.compiler

import java.io.IOException
import mumps.interp.NUL
import mumps.interp.cstringAt

/**
 * PRO-arm translation of compiler-new.c::new_code (pilot B3, unit 65,
 * rank 274, provisional under deviation C-003). Translated
 * independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 29-145 (study tool: loc 106, score 102.8, exact units.csv
 * match).
 *
 * new_code(): the NEW command compiler - three forms: no-argument
 * (emit the symbol-table overflow guard), specific (new except
 * (...), looping set_code(SET) with retz=NEW over the name list,
 * emitting the nmes push chain), and inclusive (loop with retz=NEW1
 * plus the exportflag SymMarkCopyCopyFlag/level-100 sym_ emission).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param : Out emitted via java.io.Writer.
 *  - h003 state_macro     : Sysout(x) (= fprintf(Out,x),
 *    compiler-macros.h:50) -> st.out.write(x).
 *  - h004 globals_ext     : exportflag/t0px/t2/retz/ExecX/ExecSym/vd
 *    on CompilerState (new owners added with this unit).
 *  - h009 format_string   : the "%s" fprintf emissions keep their
 *    single-placeholder shape via Kotlin concatenation.
 *  - h011 goto            : backward gotos new2/new3 become one
 *    while(true) per form; the exports:/new3 label fall-through is
 *    straight-line code.
 *  - h012 unsigned_byte_domain : xd/vd byte domains (Char(0..255)).
 *  - h014 out_of_scope_call : set_code (compiler-set.c::set_code,
 *    batch-4 corpus unit) -> Stubs.kt owner-named stub; strcmp via
 *    cstringAt comparison (String == over the C-string view).
 *  - h015 grammar_collision : no dollar bytes needed in the emitted
 *    C text of this unit.
 *  - h017 naming          : new_code -> newCode (B1 if_code ->
 *    ifCode precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - compiler-macros.h defines SET twice (:83 SET 0, then :91
 *    SET 's' - the later one wins at compile time); the arm uses the
 *    effective 's' and notes the shadowed first definition.
 *  - The specific-form loop reads xd[xpx] with xpx pre-incremented
 *    by one before the ')' test (vendor line 77) - kept exactly.
 *  - The inclusive form's tail loop (vendor line 137) is an empty
 *    scan to the next space; its body is empty, so it renders as the
 *    xpx advance while-loop only.
 */

object NewCode {

    // compiler-macros.h:69-73
    private const val PARSE_ERROR = 1
    private const val ARG_LIST_ERROR = 2
    private const val NO_GLOBAL_PERMITTED = 3
    private const val MEM_ERROR = 4
    private const val TOO_MANY_LHS = 5
    /** compiler-macros.h:74. */
    private const val ABRTX = 6

    // compiler-macros.h:91-97 (effective defines)
    private const val SET_C = 's'
    private const val NEW_C = 'n'
    private const val NEW1_C = 'N'

    @Throws(IOException::class)
    fun newCode(): Int {
        val st = CompilerState
        var return_code: Int

        st.exportflag = 0

        // no argument form
        if (st.xd[st.xpx.toInt()] == ' ' || st.xd[st.xpx.toInt()] == NUL) {
            st.out.write("\tif (svPtr->_Sym>SYM_MAX) ")
            st.out.write("ErrorMessage(\"Symbol Table Overflow\",svPtr->LineNumber);\n")
            st.out.write("\tsvPtr->nstart=NULL;\n")
            st.out.write("\tsym_(7,(UNS) \"\", (UNS) \"\", svPtr);\n")
            st.ExecSym[st.ExecX.toInt()] = 1

            if (st.POST_flg != 0.toShort()) {
                st.out.write("\t} /* post conditional in new */\n")
                st.POST_flg = 0
            }
            st.xpx++
            return 0 // goto next_cmnd
        }

        // Push all locals except...
        if (st.xd[st.xpx.toInt()] == '(') { // specific
            st.out.write("\t{\n\tstruct nmes *p1,*p2;\n")
            st.out.write("\tsvPtr->nstart=NULL;\n")
            st.xpx++

            // new2:
            while (true) {
                st.t0px = 1
                st.t2 = 1
                st.retz = NEW_C.code.toShort() // h010: char code into the short domain

                return_code = set_code(SET_C) // goto set; process item to read

                if (return_code == PARSE_ERROR) return PARSE_ERROR // goto parse_err
                if (return_code == ARG_LIST_ERROR) return ARG_LIST_ERROR
                if (return_code == NO_GLOBAL_PERMITTED) return NO_GLOBAL_PERMITTED
                if (return_code == MEM_ERROR) return MEM_ERROR // goto mem_err
                if (return_code == TOO_MANY_LHS) return TOO_MANY_LHS // goto too_lhs
                if (return_code == ABRTX) return ABRTX // goto abrtx

                // new_return:
                if (cstringAt(st.vd, 0) == "atmp") {
                    st.out.write("\tkeyfix(atmp);\n")
                } else {
                    // char tmp[256]; quote vd into tmp; tmp back into vd
                    val tmp = "\"" + cstringAt(st.vd, 0) + "\""
                    vdWrite(st, tmp)
                }

                st.out.write(
                    "\tp1=(struct nmes *) malloc(sizeof (struct nmes));\n" +
                        "\tif (p1==NULL) ErrorMessage(\"Out of memory\",svPtr->LineNumber);\n" +
                        "\tp1->name=(char *) malloc(strlen((const char *) atmp+1);\n" +
                        "\tif (p1->name==NULL) ErrorMessage(\"Out of memory\",svPtr->LineNumber);\n"
                )

                st.out.write("\tstrmove((UNS) p1->name, (UNS) " + cstringAt(st.vd, 0) + ");\n")

                st.out.write(
                    "\tp1->next=svPtr->nstart;\n" +
                        "\tsvPtr->nstart=p1;\n"
                )
                st.xpx++

                if (st.xd[st.xpx.toInt()] != ')' && st.xd[st.xpx.toInt()] != NUL) {
                    st.xpx++
                    continue // goto new2
                }

                st.out.write("\tsym_(7,(UNS) \"\",(UNS) \"\",svPtr); // new except (...) \n")
                st.out.write("\t}\n")
                st.ExecSym[st.ExecX.toInt()] = 1
                if (st.ExecX == 0.toShort()) {
                    st.out.write("\tif (_doStx == 0) ")
                    st.out.write("\t_doStackN[_doStx-1]=1;\n")
                }

                if (st.POST_flg != 0.toShort()) {
                    st.out.write("\t} /* post conditional new return*/\n")
                    st.POST_flg = 0
                }

                st.xpx++
                return 0 // goto next_cmnd
            }
        }

        // Push locals inclusive
        st.ExecSym[st.ExecX.toInt()] = 1

        if (st.POST_flg != 0.toShort()) {
            st.out.write("\t} /* post conditional new push */\n")
            st.POST_flg = 0
        }

        // exports:/new3:
        while (true) {
            st.t0px = 1
            st.t2 = 1
            st.retz = NEW1_C.code.toShort() // h010: char code into the short domain

            return_code = set_code(SET_C) // goto set; process item to read

            if (return_code == PARSE_ERROR) return PARSE_ERROR // goto parse_err
            if (return_code == ARG_LIST_ERROR) return ARG_LIST_ERROR
            if (return_code == NO_GLOBAL_PERMITTED) return NO_GLOBAL_PERMITTED
            if (return_code == MEM_ERROR) return MEM_ERROR // goto mem_err
            if (return_code == TOO_MANY_LHS) return TOO_MANY_LHS // goto too_lhs
            if (return_code == ABRTX) return ABRTX // goto abrtx

            // new1_return:
            if (cstringAt(st.vd, 0) == "atmp") {
                st.out.write("\tkeyfix(atmp);\n")
            } else {
                val tmp = "\"" + cstringAt(st.vd, 0) + "\""
                vdWrite(st, tmp)
            }

            if (st.exportflag != 0.toShort()) {
                st.out.write(
                    "\tsym_(SymMarkCopyFlag, (UNS) " + cstringAt(st.vd, 0) +
                        ", (UNS) \"\",svPtr); // mark copy flag\n"
                )
            } else {
                st.out.write(
                    "\tsym_(100,(UNS) " + cstringAt(st.vd, 0) +
                        ",(UNS) \"\",svPtr); // create new variable at this level\n"
                )
            }
            st.xpx++

            if (st.xd[st.xpx.toInt()] != ' ' && st.xd[st.xpx.toInt()] != NUL) {
                st.xpx++
                continue // goto new3
            }

            // for (; xd[xpx] != ' ' && xd[xpx] != 0; xpx++);
            while (st.xd[st.xpx.toInt()] != ' ' && st.xd[st.xpx.toInt()] != NUL) {
                st.xpx++
            }
            return 0 // goto next_cmnd
        }
    }

    /** strcpy(vd, tmp): write the quoted name back into the vd buffer. */
    private fun vdWrite(st: CompilerState, s: String) {
        for (k in s.indices) st.vd[k] = s[k]
        st.vd[s.length] = NUL
    }
}
