package mumps.compiler

import java.io.IOException
import mumps.interp.NUL

/**
 * PRO-arm translation of compiler-lock.c::lock_code (B4 unit 101,
 * rank 242). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 36-122.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-lock.c::lock_code, c, loc 62,
 * comment_ratio 0.031, pointer_marks 5, control_marks 32, score
 * 56.0, batch 4 - exact match to wp1/study-subset/units.csv row
 * 242.
 *
 * lock_code(): the Mumps LOCK command compiler - the argumentless
 * form emits the cleanLocks/lock-off scaffold, the argument form
 * loops set_code(SET) over the lock list (plus/minus flag,
 * optional timeout), emitting the makeLock/tpx test scaffold.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param : Out is the java.io.Writer on
 *    CompilerState (fprintf(Out,...) -> st.out.write).
 *  - h004 globals_ext     : LockFlag (header :137) and dirflg
 *    (:170) are new CompilerState owners added with this unit.
 *  - h009 format_string   : the fprintf "%s"/"%c" emissions ride
 *    Kotlin concatenation over the NUL-terminated views.
 *  - h011 goto            : the backward goto NxtLckArg becomes
 *    while(true) + continue; the forward goto lck1 from the
 *    argumentless form becomes the lck1 skip flag; the fall-
 *    through to lock_return after set_code==0 is straight-line
 *    code.
 *  - h012 unsigned_byte_domain: xd/vd/pd1/to byte domains; to is
 *    the local 32-byte timeout buffer.
 *  - h014 out_of_scope_call: set_code (owner landed this same
 *    tranche as SetCode.kt - the B3-era Stubs.kt stub is removed
 *    with the NewCode call sites updated); parse_ (compiler-side
 *    Stubs stub, owner compiler-parse.c, not a corpus unit).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level lockCode - no collisions; the
 *    emitted vendor C text contains a mid-line block-comment
 *    closer ("post conditional in break") - accepted vendor text
 *    per the NewCode precedent.
 *  - h017 naming          : lock_code -> lockCode (if_code ->
 *    ifCode precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The argumentless form jumps straight to the lck1 tail,
 *    skipping the LockDone=1/lock parse - carried by the skip
 *    flag, not duplicated code.
 *  - The timeout probe reads xd[xpx+1] (one PAST the current
 *    byte) before deciding ':' (vendor line 92) - kept verbatim.
 *  - The commented-out "goto set;" and "// goto bad_lock;" lines
 *    are vendor comments kept in spirit by the code order.
 *  - The error ladder after set_code returns for every nonzero
 *    code except 0 itself; vendor falls through to lock_return
 *    only for return_code==0 - kept as the ladder order.
 */

private const val PARSE_ERROR = 1 // compiler-macros.h:69
private const val ARG_LIST_ERROR = 2 // :70
private const val NO_GLOBAL_PERMITTED = 3 // :71
private const val MEM_ERROR = 4 // :72
private const val TOO_MANY_LHS = 5 // :73
private const val ABRTX = 6 // :74
private const val BAD_LOCK = 9 // :77

/** compiler-macros.h:91-112 effective defines (char command codes). */
private const val SET_C = 's'
private const val LOCK_C = 'l'

@Throws(IOException::class)
fun lockCode(): Int {
    val st = CompilerState

    // lock group
    var Lf = ' '
    val to = CharArray(32)

    var skipToLck1 = false
    if (st.xd[st.xpx.toInt()] == ' ' || st.xd[st.xpx.toInt()] == NUL) { // no argument form
        st.out.write("\tMltoa(getpid(),(char *) atmp);\n")
        st.out.write("\tif (svPtr->LockDone) cleanLocks((char *) atmp); /* remove locks for this job */\n")
        st.out.write("\tsvPtr->LockDone = 0; /* indicates a lock has been performed */\n")
        skipToLck1 = true // goto lck1
    }

    // NxtLckArg:
    while (true) {

        if (!skipToLck1) {

            st.out.write("\tsvPtr->LockDone = 1; /* indicates a lock has been performed */\n")
            st.LockFlag = 1
            st.retz = LOCK_C.code.toShort() // h010: char code into the short domain
            if (st.xd[st.xpx.toInt()] == '+' || st.xd[st.xpx.toInt()] == '-') {
                Lf = st.xd[st.xpx.toInt()]
                st.xpx++
            } else {
                Lf = ' '
            }
            if (st.xd[st.xpx.toInt()] != '^') return BAD_LOCK // goto bad_lock;

            val return_code = setCode(SET_C.code) // goto set; process item to read

            if (return_code == PARSE_ERROR) return PARSE_ERROR // goto parse_err;
            if (return_code == ARG_LIST_ERROR) return ARG_LIST_ERROR // goto arg_list_error;
            if (return_code == NO_GLOBAL_PERMITTED) return NO_GLOBAL_PERMITTED
            if (return_code == MEM_ERROR) return MEM_ERROR // goto mem_err;
            if (return_code == TOO_MANY_LHS) return TOO_MANY_LHS // goto too_lhs;
            if (return_code == ABRTX) return ABRTX // goto abrtx;

            // lock_return: (return_code == 0)
            st.out.write("\tkeyfix(" + mumps.interp.cstringAt(st.vd) + ");\n")

            var tn = 0
            while (tn < 4) { // strcpy(to,"NULL")
                to[tn] = "NULL"[tn]
                tn++
            }
            to[tn] = NUL

            if (st.xd[st.xpx.toInt() + 1] == ':') { // timeout
                st.t2 = 1
                st.xpx += 2
                st.ierr = parse_().toShort() // h010: int into the short domain
                if (st.ierr != 0.toShort()) return PARSE_ERROR // goto parse_err;
                st.out.write(
                    " // TIMEOUT REQUESTED FOR " +
                        mumps.interp.cstringAt(st.pd1, st.sdlim.toInt()) + " SECONDS\n"
                )
                var sOff = st.sdlim.toInt()
                var dOff = 0
                while (st.pd1[sOff] != NUL) { // strcpy(to,&pd1[sdlim])
                    to[dOff] = st.pd1[sOff]
                    dOff++; sOff++
                }
                to[dOff] = NUL
                st.xpx--
            }

            st.out.write("\tMltoa(getpid(),atmp);\n")
            st.out.write(
                "\tif (strcmp((const char *) makeLock((char *) " + mumps.interp.cstringAt(st.vd) +
                    ",(char *) \"1\", (char *) atmp,'" + Lf + "',(char *) " +
                    mumps.interp.cstringAt(to) + "),(const char *) \"1\")==0) " +
                    "svPtr->tpx=1; else svPtr->tpx=0;\n"
            )
        }

        // lck1:
        skipToLck1 = false
        st.xpx++
        st.dirflg = 0
        if (st.xd[st.xpx.toInt()] == ',') {
            st.xpx++
            continue // goto NxtLckArg
        }

        if (st.xd[st.xpx.toInt()] == NUL || st.xd[st.xpx.toInt()] == ' ' ||
            st.xd[st.xpx.toInt()] == '\t') {

            if (st.POST_flg != 0.toShort()) {
                st.out.write("\t} /* post conditional in break */\n")
                st.POST_flg = 0
            }

            return 0 // goto next_cmnd;
        }

        return 0 // goto next_cmnd;
    }
}
