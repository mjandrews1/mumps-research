package mumps.mdh

import mumps.interp.NUL
import mumps.interp.cstringAt
import mumps.interp.mglobal
import mumps.interp.strcpyC
import mumps.interp.strlenC
import mumps.interp.strncmpC
import mumps.interp.strtodC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::global::Min() (pilot B2,
 * unit 44, provisional under deviation C-003). Translated
 * independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 1690-1724 (units.csv: 26 LOC, score 14.8, rank 145; single
 * overload - the free intrinsic wrapper Min at line 10262 is the
 * batch-4 unit and calls this method).
 *
 * The MUMPS min-over-children walk on the subscripted global: order
 * the next sibling with Mglobal(XNEXT,...), stop when the key prefix
 * diverges from ref, retrieve each child value with get_global and
 * track the minimum.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : ttt/tmp/tmp1/rslt/ref STR_MAX C-string
 *    buffers.
 *  - h012 unsigned_byte_domain: buffers are Char(0..255) domains; the
 *    vendor char comparisons against 32 become .code comparisons.
 *  - h014 out_of_scope_call  : mglobal (InterpStubs stub, owner
 *    global-array machinery) and MumpsGlobalException (MdhStubs) -
 *    both reused unmodified. The vendor get_global call is the
 *    libmpscpp.h:117 preprocessor macro, which expands to
 *    (svPtr->tpx = Mglobal(RETRIEVE, ref, rslt, svPtr), rslt); it is
 *    expanded inline here - no stub is created for a macro. strtodC
 *    (real, unit 10 MpsAcos) and the CstringOps helpers
 *    (strcpyC/strlenC/strncmpC/cstringAt/writeCstring, B1) are real
 *    reuses.
 *  - h015 grammar_collision  : the vendor local double val collides
 *    with the Kotlin hard keyword val; renamed value (journal).
 *  - h017 naming             : Min keeps the vendor name; emitted as
 *    a GlobalRef extension (unit-14/32 precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor class member ref is the subscripted reference buffer;
 *    the carrier keeps it as Global.ref (unit-14 conflation, cf. unit
 *    32) and rebuilds the buffer here.
 *  - Mglobal(XNEXT,...) writes the next key back into the key buffer
 *    in place (interp-native-btree.cpp:945 copies the block entry to
 *    both key and bd), so the vendor loop reading tmp (not ttt) after
 *    the call is the intended contract, kept as-is.
 *  - If the walk finds no child, the vendor returns the 0.0
 *    initialization of min - kept.
 *  - The trailing-control trim reads tmp[i-1]/tmp[i-2] without a
 *    bound check; the vendor relies on ref being at least two bytes
 *    long (kept faithful; a shorter ref would be out of bounds in C
 *    too).
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val XNEXT = 8 // vendor defines.h:88
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

fun GlobalRef.Min(): Double {
    val ttt = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)
    val ref = CharArray(STR_MAX)
    var i: Int
    var flg = 1
    var min = 0.0
    var value: Double

    // vendor: strcpy(tmp,ref); i=strlen(tmp);
    writeCstring(ref, g.ref) // ref buffer, see header note
    strcpyC(tmp, ref)
    i = strlenC(tmp)

    // vendor: if (tmp[i-1]<32&&tmp[i-2]<32) tmp[--i]=0;
    if (tmp[i - 1].code < 32 && tmp[i - 2].code < 32) {
        i--
        tmp[i] = NUL
    }

    while (true) {
        // vendor: if(Mglobal(XNEXT,tmp,ttt,svPtr)==0) break;
        if (mglobal(XNEXT, tmp, ttt, MdhState.svPtr) == 0) break

        // vendor: if (strncmp(ref,tmp,i)!=0) break;
        if (strncmpC(ref, 0, cstringAt(tmp), i) != 0) break

        // vendor: strcpy(tmp1,tmp); get_global(tmp1,rslt);
        strcpyC(tmp1, tmp)
        // get_global is the libmpscpp.h:117 macro, expanded inline:
        //   (svPtr->tpx = Mglobal(RETRIEVE, ref, rslt, svPtr), rslt)
        MdhState.svPtr.tpx = mglobal(RETRIEVE, tmp1, rslt, MdhState.svPtr)

        // vendor: if (!svPtr->tpx) { GlobalExceptionCode=...; throw }
        if (MdhState.svPtr.tpx == 0) {
            MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        value = strtodC(rslt) // vendor: val=strtod(rslt,NULL);

        if (flg != 0) {
            min = value
            flg = 0
            continue
        }

        // vendor: if (val<min) min=val;
        if (value < min) min = value
    }

    return min
}
