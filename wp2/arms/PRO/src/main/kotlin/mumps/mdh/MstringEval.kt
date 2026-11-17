package mumps.mdh

import mumps.interp.NUL
import mumps.interp.parse_
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::mstring::Eval (B3 unit 77,
 * rank 157). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 3903-3925.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::mstring::Eval, cpp, loc 18,
 * comment_ratio 0.1, pointer_marks 7, control_marks 1, score 18.2,
 * batch 3 - exact match to wp1/study-subset/units.csv row 157.
 *
 * mstring::Eval(): evaluates the mstring's contents as a Mumps
 * expression (the indirection operator) - copies data into xd, runs
 * the interpreter's parse_, and returns the pd1 result slice as a
 * new mstring.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 io_primitives    : strcpy -> writeCstring over the xd
 *    byte domain.
 *  - h012 unsigned_byte_domain: xd/pd1 byte domains; the result is
 *    the c-string at pd1+sdlim.
 *  - h013 dead_store       : the vendor's "tmp = "err"; return tmp;"
 *    after the throw is unreachable - kept as a comment.
 *  - h014 out_of_scope_call: parse_ (B3 unit 63, the real
 *    implementation) reused via package import - the vendor's local
 *    "int parse_(struct MSV*)" prototype resolves to it;
 *    GlobalExceptionCode -> MdhState (unit 31); throw
 *    MumpsGlobalException (unit 31 carrier).
 *  - h015 grammar_collision: pre-push scan incl. cross-file
 *    top-level names: new top-level Eval extension on Mstring - no
 *    collisions (the global::Eval family is not translated).
 *  - h017 naming           : Eval keeps the vendor name; emitted as
 *    an extension on the Mstring carrier (unit-14 GlobalRef
 *    precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The function prototype "int parse_(struct MSV*)" is declared
 *    INSIDE the body (vendor line 3908) - noted; the port resolves
 *    it via import.
 *  - pd1len/t2/sdlim assignments mirror the vendor's short domains
 *    (0/1/0 and the sdlim result from parse_).
 */
private const val INTERPRETER_ERROR = 8 // vendor libmpscpp.h:77

fun Mstring.Eval(): Mstring {
    // evaluate a mumps experession
    // acts like the indirection operator

    val svPtr = MdhState.svPtr

    writeCstring(svPtr.xd, c_str()) // strcpy(&svPtr->xd[0], data.c_str())
    svPtr.xpx = 0
    svPtr.t2 = 1
    svPtr.pd1len = 0

    svPtr.ierr = parse_(svPtr)
    if (svPtr.ierr != 0) {
        MdhState.globalExceptionCode = INTERPRETER_ERROR
        throw MumpsGlobalException()
        // tmp = "err"; return tmp; // h013: unreachable after the throw
    } else {
        val sb = StringBuilder()
        var i = svPtr.sdlim // (build clean) sdlim is already Int; the .toInt() was redundant
        while (svPtr.pd1[i] != NUL) {
            sb.append(svPtr.pd1[i])
            i++
        }
        return Mstring(sb.toString()) // tmp = (char *) &svPtr->pd1[sdlim]
    }
}
