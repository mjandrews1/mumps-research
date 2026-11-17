package mumps.compiler

import java.io.IOException
import mumps.interp.NUL
import mumps.interp.cstringAt

/**
 * PRO-arm translation of compiler-read.c::read_code (B6 tranche
 * A, unit 159, rank 280). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 39-166.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): read_code, c, loc 96,
 * comment_ratio 0.01, pointer_marks 17, control_marks 56,
 * score 112.2 - exact match to wp1/study-subset/units.csv row
 * 160 (batch 6).
 *
 * read_code(): the READ command compiler - emits the C read
 * glue (guard, carriage control, timeout, direct mode) into
 * the compiler's Out stream and hands the read target to
 * set_code(SET) then set_code(SET_STORE).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 Writer          : fprintf(Out,...) emissions map to
 *    CompilerState.out.write, one write per vendor fprintf
 *    (Inout.kt u59 precedent).
 *  - h010 int_short       : parse_()'s int return lands in the
 *    short ierr domain (LockCode u-family precedent).
 *  - h011 control_flow    : the goto cont_read / goto store /
 *    goto set_return family linearizes with an early-exit flag
 *    (the three carriage-control arms fall to the cont_read
 *    tail via the flag; goto set_return is the fall-through
 *    after the set_code ladder's rc==0 case; goto store from
 *    the timeout arm skips the dirflg/else pair - u95
 *    label-dispatch precedent).
 *  - h012 unsigned_byte_domain: xd/pd1 are C byte buffers;
 *    &pd1[sdlim] pointer args render as NUL-terminated slice
 *    copies (per-file pd1At helper; the inout seat takes
 *    index-0 C-strings).
 *  - h014 out_of_scope_call: setCode (SetCode.kt real seat),
 *    inout (Inout.kt real seat), parse_ (Stubs.kt seat) -
 *    reused unmodified; the emitted TimeOut_getstr1 /
 *    DirectRead / getstr1 references are EMITTED C TEXT, not
 *    arm calls (the generated program's runtime surface;
 *    TimeOut_getstr1 is a batch-6 corpus row, getstr1 DONE u138,
 *    DirectRead not a corpus unit - u130 forward-decl note).
 *  - h015 grammar_collision: new top-level readCode - no
 *    collisions.
 *  - h017 naming          : read_code -> readCode (vendor name
 *    in the header comment, LockCode/NewCode precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The return 99 fallthrough after the first set_code ladder
 *    (an unrecognized return code reaches the function's only
 *    anonymous exit) - kept verbatim.
 *  - The timeout arm decrements xpx (xpx--) AFTER the emit
 *    block and before store - the re-read of the ':'-delimited
 *    timeout expression - kept verbatim.
 *  - The commented-out linepad(fprintf(Out,...)) line is kept
 *    as roster text (the vendor's fossil comment).
 *  - The store ladder has NO return 99 (control falls to
 *    cont_read on rc==0) - kept.
 *  - The two emitted Set-Test comments carry the dollar sigil
 *    via the per-file DOLLAR const so the emitted C text stays
 *    byte-identical to vendor while the arm source keeps the
 *    no-raw-dollar scan rule (SetCode.kt DOLLARC precedent).
 */
private const val PARSE_ERROR = 1 // compiler-macros.h:69
private const val ARG_LIST_ERROR = 2 // :70
private const val NO_GLOBAL_PERMITTED = 3 // :71
private const val MEM_ERROR = 4 // :72
private const val TOO_MANY_LHS = 5 // :73
private const val ABRTX = 6 // :74
private const val SET = 0 // compiler-macros.h:83
private const val SET_STORE = 1 // :84
private const val READ_C = 'r' // compiler-macros.h:100
// Scan-gate carrier for the emitted dollar sign (raw dollars are
// forbidden in arm sources, h015 emit-fidelity workaround;
// SetCode.kt DOLLARC precedent, value 36 = the dollar char).
private const val DOLLAR = 36.toChar()

/** &pd1[sdlim] pointer carrier: NUL-terminated slice copy (h012). */
private fun pd1At(): CharArray {
    val st = CompilerState
    val off = st.sdlim.toInt()
    var n = off
    while (st.pd1[n] != NUL) n++
    val b = CharArray(n - off + 1)
    for (k in 0 until n - off) b[k] = st.pd1[off + k]
    b[n - off] = NUL
    return b
}

@Throws(IOException::class)
fun readCode(): Int {
    val st = CompilerState

    st.out.write("\tif (svPtr->in_file[svPtr->io] == NULL) \n")
    st.out.write("\t\tErrorMessage(\"Read from input file\", svPtr->LineNumber); \n")

    while (true) {
        st.dirflg = 0.toChar()

        var goCont = false // h011: goto cont_read flag

        if (st.xd[st.xpx.toInt()] == '!' || st.xd[st.xpx.toInt()] == '#') { // carriage control
            val i: Int = if (st.xd[st.xpx.toInt()] != '#') 2 else 1
            inout(pd1At(), i.toShort(), cbufOf(""))
            goCont = true
        }

        if (!goCont && st.xd[st.xpx.toInt()] == '?') { // tab format
            st.t2 = 1
            st.xpx++
            st.ierr = parse_().toShort()
            if (st.ierr != 0.toShort()) return PARSE_ERROR // goto parse_err;
            inout(pd1At(), 3, pd1At())
            goCont = true
        }

        if (!goCont && st.xd[st.xpx.toInt()] == '"') { // text to be printed first
            st.t2 = 1
            st.ierr = parse_().toShort()
            if (st.ierr != 0.toShort()) return PARSE_ERROR // goto parse_err;
            inout(pd1At(), 4, cbufOf(""))
            goCont = true
        }

        if (!goCont) {
            if (st.xd[st.xpx.toInt()] == '*') { // direct mode
                st.xpx++
                st.dirflg = 1.toChar()
            }

            st.retz = READ_C.code.toShort()

            var return_code = setCode(SET) // goto set; // process item to read

            if (return_code == 0) {
                // goto set_return
            } else {
                if (return_code == PARSE_ERROR) return PARSE_ERROR // goto parse_err;
                if (return_code == ARG_LIST_ERROR) return ARG_LIST_ERROR // goto arg_list_error;
                if (return_code == NO_GLOBAL_PERMITTED) return NO_GLOBAL_PERMITTED
                if (return_code == MEM_ERROR) return MEM_ERROR // goto mem_err;
                if (return_code == TOO_MANY_LHS) return TOO_MANY_LHS // goto too_lhs;
                if (return_code == ABRTX) return ABRTX // goto abrtx;
                return 99
            }

            // set_return:
            if (st.xd[st.xpx.toInt() + 1] == ':') { // timeout
                st.t2 = 1
                st.xpx += 2
                st.ierr = parse_().toShort()
                if (st.ierr != 0.toShort()) return PARSE_ERROR // goto parse_err;
                st.out.write(
                    " // TIMEOUT REQUESTED FOR " +
                        cstringAt(st.pd1, st.sdlim.toInt()) + " SECONDS\n"
                )
                // vendor fossil: //linepad(fprintf(Out,"\tfile[5]=stdin;"),"// unit ready for input");

                st.out.write("\tif (svPtr->io==5) fflush(stdout);\n")

                st.out.write(
                    "\tsvPtr->tpx = TimeOut_getstr1(svPtr->io, gtmp, (char *) " +
                        cstringAt(st.pd1, st.sdlim.toInt()) + ", " + st.dirflg.code +
                        "); // Read string into gtmp\n"
                )

                st.out.write(
                    "\tif (svPtr->tpx >= 0) svPtr->tpx = 1; else svPtr->tpx=0; // Set " + DOLLAR + "Test\n"
                )

                writeCstringAt(st.pd1, st.sdlim.toInt(), "gtmp")
                st.xpx--
                // goto store
            } else if (st.dirflg == 1.toChar()) {
                st.out.write("\tsvPtr->tpx=DirectRead(svPtr->io,gtmp); // Read Direct\n")
                st.out.write("\tsprintf((char *)atmp,\"%%d\",(char *) gtmp[0]);\n")
                writeCstringAt(st.pd1, st.sdlim.toInt(), "atmp")
            } else {
                st.out.write(
                    "\tsvPtr->tpx = getstr1(svPtr->in_file[svPtr->io], &gtmp); // Read string into gtmp\n"
                )
                st.out.write(
                    "\tif (svPtr->tpx >= 0) svPtr->tpx = 1; else svPtr->tpx = 0; // Set " + DOLLAR + "Test\n"
                )
                writeCstringAt(st.pd1, st.sdlim.toInt(), "gtmp")
            }

            // store:
            st.retz = READ_C.code.toShort()

            return_code = setCode(SET_STORE) // goto set; // process item to read

            if (return_code == 0) {
                // goto cont_read
            } else {
                if (return_code == PARSE_ERROR) return PARSE_ERROR // goto parse_err;
                if (return_code == ARG_LIST_ERROR) return ARG_LIST_ERROR // goto arg_list_error;
                if (return_code == NO_GLOBAL_PERMITTED) return NO_GLOBAL_PERMITTED
                if (return_code == MEM_ERROR) return MEM_ERROR // goto mem_err;
                if (return_code == TOO_MANY_LHS) return TOO_MANY_LHS // goto too_lhs;
                if (return_code == ABRTX) return ABRTX // goto abrtx;
            }
        }

        // cont_read:
        st.xpx++
        st.dirflg = 0.toChar()
        if (st.xd[st.xpx.toInt()] == ',') st.xpx++

        val c = st.xd[st.xpx.toInt()]
        if (c == NUL || c == ' ' || c == '\t') {
            if (st.POST_flg != 0.toShort()) {
                st.out.write("\t} /* post conditional cont_read */\n")
                st.POST_flg = 0
            }
            return 0 // goto next_cmnd;
        }
    }
}

/** C "" literal carrier (h012). */
private fun cbufOf(s: String): CharArray {
    val b = CharArray(s.length + 1)
    for (k in s.indices) b[k] = s[k]
    b[s.length] = NUL
    return b
}

/** strcpy(dst+off, s) over the byte domain (h012 offset carrier). */
private fun writeCstringAt(dst: CharArray, off: Int, s: String) {
    for (k in s.indices) dst[off + k] = s[k]
    dst[off + s.length] = NUL
}
