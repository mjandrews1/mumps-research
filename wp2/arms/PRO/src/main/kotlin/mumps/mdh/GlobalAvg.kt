package mumps.mdh

import mumps.interp.NUL
import mumps.interp.mglobal
import mumps.interp.strlenC
import mumps.interp.strcpyC
import mumps.interp.strncmpC
import mumps.interp.writeCstring
import mumps.interp.cstringAt

/**
 * PRO-arm translation of libmpscpp.cpp::global::Avg (roster row
 * 262, batch 9; units.csv: loc 28, comment_ratio 0.034,
 * pointer_marks 1, control_marks 7, score 15.2, rank 146).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 1726-1761. Roster id keeps the global::
 * namespace qualifier (u145/u147/u169 verbatim convention).
 *
 * global::Avg(): the average-over-children walk on the
 * subscripted global - the unit-169 global::Max sibling: order
 * the next sibling with Mglobal(XNEXT,...), stop when the key
 * prefix diverges from ref, retrieve each child value with
 * get_global (sscanf %lf - a FAILED numeric scan throws
 * INVALID_CONVERSION, unlike Max's bare strtod), tot += val,
 * j++ - return tot/j (0.0 when j==0: no children).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : ttt/tmp/tmp1/rslt/ref STR_MAX
 *    C-string buffers (u169 precedent).
 *  - h012 unsigned_byte_domain: buffers are Char(0..255) domains;
 *    the trailing-control two-byte trim compares .code < 32; the
 *    sscanf arm folds onto the String carrier (trim + toDoubleOrNull).
 *  - h014 out_of_scope_call : mglobal with XNEXT=8 (defines.h:88);
 *    get_global expanded inline (libmpscpp.h:117 macro - tpx =
 *    mglobal(RETRIEVE,...).toLong(), u169/u218 precedent);
 *    GlobalExceptionCode/INVALID_CONVERSION(1)/throw
 *    MumpsGlobalException -> MdhState (u31, Idf precedent).
 *  - h015 grammar_collision : new GlobalRef.Avg() member,
 *    receiver-scoped in the overload set - no collisions.
 *  - h017 naming           : Avg keeps the vendor name; j is the
 *    child counter (vendor long j=0 -> Long; count semantics).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The trim tests tmp[i-1]<32 && tmp[i-2]<32 (Max twin) - kept.
 *  - sscanf(rslt,"%lf",&val)==0 throws INVALID_CONVERSION (a
 *    non-numeric child is a hard error here, unlike Max/Min
 *    where strtod's 0.0 passes silently) - kept.
 *  - j==0 (no children) returns 0.0 - kept.
 */
fun GlobalRef.Avg(): Double {
    val ttt = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)
    val ref = CharArray(STR_MAX)
    var j = 0L
    var tot = 0.0

    // vendor: strcpy(tmp,ref); i=strlen(tmp);
    writeCstring(ref, g.ref) // ref buffer, unit-44 carrier note
    strcpyC(tmp, ref)
    var i = strlenC(tmp)

    // vendor: if (tmp[i-1]<32&&tmp[i-2]<32) tmp[--i]=0;
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
        // get_global macro: (svPtr->tpx = Mglobal(RETRIEVE, ref, rslt, svPtr), rslt)
        MdhState.svPtr.tpx = mglobal(RETRIEVE, tmp1, rslt, MdhState.svPtr).toLong()

        if (MdhState.svPtr.tpx == 0L) {
            MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        // vendor: if (sscanf(rslt,"%lf",&val)==0) throw INVALID_CONVERSION
        val valStr = cstringAt(rslt).trim() // h012 String fold of the scan
        val value = valStr.toDoubleOrNull() // the %lf conversion
        if (value == null) {
            // no numeric conversion (sscanf==0): a hard error here,
            // unlike Max/Min (kept verbatim)
            MdhState.globalExceptionCode = INVALID_CONVERSION
            throw MumpsGlobalException()
        }

        tot = tot + value // vendor: tot=tot+val;
        j++ // vendor: j++;
    }

    if (j == 0L) return 0.0 // vendor: if (j==0) return 0.0;

    return tot / j // vendor: return tot/j;
}

private const val STR_MAX = 32000 // vendor strmax.h:3 (unit-44 precedent)
private const val XNEXT = 8 // vendor defines.h:88
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71
private const val INVALID_CONVERSION = 1 // vendor libmpscpp.h:70
