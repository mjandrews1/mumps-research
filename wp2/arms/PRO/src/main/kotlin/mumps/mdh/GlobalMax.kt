package mumps.mdh

import mumps.interp.Msv
import mumps.interp.NUL
import mumps.interp.cstringAt
import mumps.interp.mglobal
import mumps.interp.strcpyC
import mumps.interp.strlenC
import mumps.interp.strncmpC
import mumps.interp.strtodC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::global::Max (B6 tranche
 * B, unit 169, rank 144). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 1651-1688.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): global::Max, cpp, loc 26,
 * comment_ratio 0.037, pointer_marks 1, control_marks 7,
 * score 14.8 - exact match to wp1/study-subset/units.csv row 170
 * (batch 6; the roster id keeps the global:: namespace qualifier,
 * u145/u147 namespace-verbatim convention).
 *
 * TAG CORRECTION (u141/u148 precedent): the B3 u88 Max wrapper
 * tagged this method "not a corpus unit" - roster row 170 IS this
 * function; the superseded note is kept verbatim in the MdhStubs
 * resolved block. This unit RESOLVES the MdhStubs GlobalRef.Max
 * stub raised by u88 (the Max.kt wrapper call sites a("").Max() /
 * a(subs).Max() bind unchanged).
 *
 * global::Max(): the max-over-children walk on the subscripted
 * global - the unit-44 global::Min sibling: order the next
 * sibling with Mglobal(XNEXT,...), stop when the key prefix
 * diverges from ref, retrieve each child value with get_global
 * and track the maximum (val>max).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : ttt/tmp/tmp1/rslt/ref STR_MAX
 *    C-string buffers.
 *  - h012 unsigned_byte_domain: buffers are Char(0..255) domains;
 *    the vendor char comparisons against 32 become .code
 *    comparisons.
 *  - h013 dead_store        : the vendor local int j is declared
 *    and never used (1655, the Min twin drops it) - not rendered,
 *    noted.
 *  - h014 out_of_scope_call : mglobal (InterpStubs stub, owner
 *    global-array machinery) and MumpsGlobalException (MdhStubs
 *    carrier) - both reused unmodified. The vendor get_global
 *    call is the libmpscpp.h:117 preprocessor macro, expanded
 *    inline here (unit-44 note) - no stub is created for a
 *    macro. strtodC (real, unit-10 MpsAcos) and the CstringOps
 *    helpers (strcpyC/strlenC/strncmpC/cstringAt/writeCstring)
 *    are real reuses.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    names: the vendor local double val collides with the Kotlin
 *    hard keyword val; renamed value (unit-44 journal); the new
 *    top-level is the GlobalRef.Max extension itself - the stub
 *    it replaces clears the seat.
 *  - h017 naming            : Max keeps the vendor name; emitted
 *    as a GlobalRef extension (unit-14/44 precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - If the walk finds no child, the vendor returns the 0.0
 *    initialization of max - kept.
 *  - The trailing-control trim reads tmp[i-1]/tmp[i-2] without a
 *    bound check; the vendor relies on ref being at least two
 *    bytes long (unit-44 note; out of bounds in C too).
 *  - Mglobal(XNEXT,...) writes the next key back into the key
 *    buffer in place, so the loop reads tmp (not ttt) after the
 *    call - the intended contract, kept (unit-44 note).
 *  - Unlike Min's trailing space, the vendor Max trim is the
 *    same two-byte test - identical bodies modulo max/val>max.
 */
private const val STR_MAX = 32000 // vendor strmax.h:3 (unit-44 precedent)
private const val XNEXT = 8 // vendor defines.h:88
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

fun GlobalRef.Max(): Double {
    val ttt = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)
    val ref = CharArray(STR_MAX)
    var i: Int
    var flg = 1
    var max = 0.0
    var value: Double

    // vendor: strcpy(tmp,ref); i=strlen(tmp);
    writeCstring(ref, g.ref) // ref buffer, unit-44 carrier note
    strcpyC(tmp, ref)
    i = strlenC(tmp)

    // vendor: if (tmp[i-1]<32 && tmp[i-2]<32) tmp[--i]=0;
    if (tmp[i - 1].code < 32 && tmp[i - 2].code < 32) {
        i--
        tmp[i] = NUL
    }

    while (true) {
        // vendor: if (Mglobal(XNEXT,tmp,ttt,svPtr)==0) break;
        if (mglobal(XNEXT, tmp, ttt, MdhState.svPtr) == 0) break

        // vendor: if (strncmp(ref,tmp,i)!=0) break;
        if (strncmpC(ref, 0, cstringAt(tmp), i) != 0) break

        // vendor: strcpy(tmp1,tmp); get_global(tmp1,rslt);
        strcpyC(tmp1, tmp)
        // get_global is the libmpscpp.h:117 macro, expanded inline:
        //   (svPtr->tpx = Mglobal(RETRIEVE, ref, rslt, svPtr), rslt)
        MdhState.svPtr.tpx = mglobal(RETRIEVE, tmp1, rslt, MdhState.svPtr).toLong()

        // vendor: if (!svPtr->tpx) { GlobalExceptionCode=...; throw }
        if (MdhState.svPtr.tpx == 0L) {
            MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        value = strtodC(rslt) // vendor: val=strtod(rslt,NULL);

        if (flg != 0) {
            max = value
            flg = 0
            continue
        }

        // vendor: if (val>max) max=val;
        if (value > max) max = value
    }

    return max
}
