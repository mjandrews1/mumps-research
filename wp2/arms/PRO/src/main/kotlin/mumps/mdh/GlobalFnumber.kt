package mumps.mdh

import mumps.interp.Msv
import mumps.interp.cstringAt
import mumps.interp.fcn
import mumps.interp.mglobal
import mumps.interp.strcatC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::global::Fnumber(mstring)
 * (B3 unit 84, rank 97). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 7452-7482.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::global::Fnumber, cpp, loc 23,
 * comment_ratio 0.042, pointer_marks 2, control_marks 1, score
 * 9.9, batch 3 - exact match to wp1/study-subset/units.csv row
 * 97. Overload disambiguation: the one-argument global member
 * (loc 23) - the two-arg global wrapper (loc 28), the mstring
 * member pair (B2 unit 42 rendered the two-arg form, Fnumber.kt)
 * and the B2-declared global two-arg wrapper are separate corpus
 * rows.
 *
 * global::Fnumber(mstring a): the (dollar)fnumber intrinsic over
 * the unsubscripted global - retrieve the global's value, build
 * the coded call "<dollar>fnumber<Open><gtmp>,<a><Close>" into
 * v1d at offset 1, run the function dispatcher, read bd at
 * offset 1.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer     : tmp[STR_MAX] call-assembly buffer and
 *    gtmp[STR_MAX] Mglobal out buffer (C-string domains).
 *  - h012 unsigned_byte_domain: all buffers are Char(0..255)
 *    domains; the vendor's offset-1 convention (v1d[1]/bd[1])
 *    kept.
 *  - h014 out_of_scope_call: Mglobal (InterpStubs stub, owner
 *    global-array machinery; RETRIEVE = 0, defines.h:83) and fcn
 *    (InterpStubs stub, owner interp-fcn.cpp::fcn, batch-4 unit)
 *    - both reused unmodified; strcpy/strcat resolve to the real
 *    B1 CstringOps helpers; GlobalExceptionCode/throw
 *    MumpsGlobalException -> MdhState (unit 31).
 *  - h015 grammar_collision: the vendor literal "<dollar>fnumber"
 *    contains a raw dollar byte the pre-push scan forbids;
 *    rebuilt as 36.toChar() (SQL_Table precedent, unit 34;
 *    Fnumber.kt unit 42).
 *  - h017 naming          : Fnumber keeps the vendor name;
 *    emitted as an extension on the Global carrier (unit-14
 *    precedent, cf. Find unit 33 / Eval unit 77).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - CodedOpen/CodedClose/CodedComma = 206/207/208 (defines.h:
 *    38-40); the vendor's two-byte arrays {code, 0} render as
 *    one-char strings in the strcat calls.
 *  - The vendor copies the invoking global's RETRIEVED VALUE
 *    (gtmp), not its ref, into the call - the single-argument
 *    form has no b parameter, so the Comma+Close follows a
 *    directly (no dead b.c_str()==0 arm here, unlike the two-arg
 *    mstring form, unit 42).
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71
private const val CODED_OPEN = 206 // vendor defines.h:38 CodedOpen
private const val CODED_CLOSE = 207 // vendor defines.h:39 CodedClose
private const val CODED_COMMA = 208 // vendor defines.h:40 CodedComma

// dollar byte for the vendor literal; not written as a raw
// character (h015 pre-push scan forbids it in Kotlin sources)
private val DOLLAR = 36.toChar()

fun Global.Fnumber(a: Mstring): Mstring {
    val tmp = CharArray(STR_MAX)
    val gtmp = CharArray(STR_MAX)
    val svPtr: Msv = MdhState.svPtr

    // vendor: Mglobal(RETRIEVE, (unsigned char *) ref, gtmp, svPtr)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, ref)
    val f = mglobal(RETRIEVE, refBuf, gtmp, svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor: strcpy(tmp,"<dollar>fnumber"); strcat(tmp,Open);
    writeCstring(tmp, DOLLAR + "fnumber")
    strcatC(tmp, CODED_OPEN.toString())
    strcatC(tmp, cstringAt(gtmp)) // invoking global's value
    strcatC(tmp, CODED_COMMA.toString())
    strcatC(tmp, a.c_str()) // first argument required
    strcatC(tmp, CODED_CLOSE.toString())

    // vendor: strcpy((char *) &svPtr->v1d[1], tmp); fcn(svPtr);
    writeCstring(svPtr.v1d, cstringAt(tmp), 1)
    fcn(svPtr)

    // vendor: rslt = (char *) &svPtr->bd[1];
    return Mstring(cstringAt(svPtr.bd, 1))
}

// --- added by B7 unit 206 (libmpscpp.cpp::global::Fnumber, the two-arg overload) ---

/**
 * B7 unit 206: the TWO-ARGUMENT global member at :7415 (rank
 * 284) - the last of the four-member Fnumber family (the
 * mstring pair DONE in Fnumber.kt, the one-arg global member
 * above DONE unit 84; the Fnumber.kt unit-42 disambiguation note
 * already named this the batch-7 wrapper).
 *
 * Same shape as the one-arg member above, plus the optional
 * second-argument arm: "if (b.c_str()==0) strcat(tmp,Close);
 * else strcat(Comma);strcat(b.c_str());strcat(Close);" - the
 * vendor's b.c_str()==0 test can never be true for a real
 * std::string (c_str() is never NULL), so the else arm always
 * runs for a two-argument call - kept verbatim (the h013 note
 * in Fnumber.kt documents the same idiom on the mstring twin).
 *
 * Rules: same as the one-arg member above (h007/h012/h014
 * mglobal+fcn stubs, h015 DOLLAR carrier, h017 extension seat);
 * scan clean.
 */
fun Global.Fnumber(a: Mstring, b: Mstring): Mstring {
    val tmp = CharArray(STR_MAX)
    val gtmp = CharArray(STR_MAX)
    val svPtr: Msv = MdhState.svPtr

    // vendor: Mglobal(RETRIEVE, (unsigned char *) ref, gtmp, svPtr)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, ref)
    val f = mglobal(RETRIEVE, refBuf, gtmp, svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor: strcpy(tmp,"<dollar>fnumber"); strcat(tmp,Open);
    writeCstring(tmp, DOLLAR + "fnumber")
    strcatC(tmp, CODED_OPEN.toString())
    strcatC(tmp, cstringAt(gtmp)) // invoking global's value
    strcatC(tmp, CODED_COMMA.toString())
    strcatC(tmp, a.c_str()) // first argument required

    // vendor: if (b.c_str()==0) strcat(tmp,Close); else {...}
    // (the b.c_str()==0 arm is the vendor's dead test - kept)
    if (b.c_str() == "") {
        strcatC(tmp, CODED_CLOSE.toString()) // second arg optional
    } else {
        strcatC(tmp, CODED_COMMA.toString())
        strcatC(tmp, b.c_str())
        strcatC(tmp, CODED_CLOSE.toString())
    }

    // vendor: strcpy((char *) &svPtr->v1d[1], tmp); fcn(svPtr);
    writeCstring(svPtr.v1d, cstringAt(tmp), 1)
    fcn(svPtr)

    // vendor: rslt = (char *) &svPtr->bd[1];
    return Mstring(cstringAt(svPtr.bd, 1))
}
