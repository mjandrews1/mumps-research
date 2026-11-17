package mumps.mdh

import mumps.interp.Msv
import mumps.interp.cstringAt
import mumps.interp.fcn
import mumps.interp.mglobal
import mumps.interp.strcatC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::global::Translate(mstring
 * a1) (B5 tranche C, unit 147, rank 84). Translated
 * independently from the pristine vendor source (mumps-14-may-
 * 2026.tgz, sha256 621deb25...ffdbe), reference source lines
 * 4292-4316.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): global::Translate, cpp,
 * loc 19, comment_ratio 0.05, pointer_marks 2, control_marks 1,
 * score 9.3 - exact match to wp1/study-subset/units.csv row 148
 * (batch 5). Overload disambiguation journaled: this is the
 * single-arg mstring form (:4292); the two-arg mstring form
 * (:4264) is a separate vendor overload not in this batch's
 * corpus rows.
 *
 * global::Translate(a1): the MUMPS dollar-translate over the
 * global's value - retrieves the value, builds the coded fcn
 * command (translate Open <value> Comma <a1> Close), runs it
 * through the interpreter's fcn seat, and returns the bd[1]
 * result as mstring.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer     : gtmp[STR_MAX] Mglobal out buffer;
 *    the vendor's mstring m Concat chain is the plain
 *    concatenation of the command tokens (String domain - the
 *    mstring::Concat members are untranslated, GlobalFnumber
 *    unit-84 precedent).
 *  - h012 unsigned_byte_domain: the command lands in
 *    svPtr.v1d at offset 1 (the vendor's &v1d[1] pointer) and
 *    the result is read from svPtr.bd at offset 1 - the u42/
 *    u84 offset-carrying convention; CodedOpen/CodedComma/
 *    CodedClose are the 206/208/207 byte markers (defines.h:38-
 *    40, Parse_.kt:196-198 seat family).
 *  - h014 out_of_scope_call: Mglobal (InterpStubs stub;
 *    RETRIEVE = 0, defines.h:83) and fcn (Fcn.kt:142 seat) -
 *    reused unmodified; GlobalExceptionCode/throw
 *    MumpsGlobalException -> MdhState (unit 31);
 *    GLOBAL_NOT_FOUND = 2 (libmpscpp.h:71).
 *  - h015 grammar_collision: pre-push scan incl. cross-file
 *    top-level names: new top-level Translate extension on the
 *    Global carrier - no collisions (the Mstring.Translate
 *    owners are untranslated).
 *  - h017 naming          : Translate keeps the vendor name;
 *    emitted as an extension on the Global carrier
 *    (GlobalFnumber unit-84 precedent); the dollar byte is
 *    rebuilt as 36.toChar() (h015 scan, u34 precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor builds m as a bare "translate" word first and
 *    prepends the dollar byte only via the FIRST Concat's marker
 *    order - rendered as the DOLLAR + "translate" prefix with
 *    the marker sequence kept verbatim (Open, value, Comma,
 *    a1, Close; u84 fold).
 *  - The bd[1] readback re-binds the mstring m to the
 *    interpreter's result buffer (aliasing kept as a fresh
 *    Mstring value per the u84 carrier convention).
 */
private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71
private const val CODED_OPEN = 206 // vendor defines.h:38 CodedOpen (vendor \xce)
private const val CODED_CLOSE = 207 // vendor defines.h:39 CodedClose (vendor \xcf)
private const val CODED_COMMA = 208 // vendor defines.h:40 CodedComma (vendor \xd0)

// dollar byte for the vendor literal; not written as a raw
// character (h015 pre-push scan forbids it in Kotlin sources)
private val DOLLAR = 36.toChar()

fun Global.Translate(a1: Mstring): Mstring {
    val gtmp = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val svPtr: Msv = MdhState.svPtr

    // vendor: Mglobal(RETRIEVE,(unsigned char *)ref,gtmp,svPtr)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, ref)
    val f = mglobal(RETRIEVE, refBuf, gtmp, svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor: m="<dollar>translate"; m=m.Concat(Open);
    //         m=m.Concat(gtmp); m=m.Concat(Comma);
    //         m=m.Concat(a1); m=m.Concat(Close);
    writeCstring(tmp, DOLLAR + "translate")
    strcatC(tmp, CODED_OPEN.toString())
    strcatC(tmp, cstringAt(gtmp)) // invoking global's value
    strcatC(tmp, CODED_COMMA.toString())
    strcatC(tmp, a1.c_str())
    strcatC(tmp, CODED_CLOSE.toString())

    // vendor: strcpy((char *) &svPtr->v1d[1], m.data.c_str());
    //         fcn(svPtr);
    writeCstring(svPtr.v1d, cstringAt(tmp), 1)
    fcn(svPtr)

    // vendor: m = (char *) &svPtr->bd[1];
    return Mstring(cstringAt(svPtr.bd, 1))
}
