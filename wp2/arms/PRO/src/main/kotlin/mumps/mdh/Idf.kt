package mumps.mdh

import java.util.Locale
import kotlin.math.log2
import mumps.interp.NUL
import mumps.interp.cstringAt
import mumps.interp.mglobal
import mumps.interp.strcpyC
import mumps.interp.strlenC
import mumps.interp.strncmpC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::global::IDF(double) (pilot
 * B2, unit 49, provisional under deviation C-003). Translated
 * independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 1764-1809 (units.csv: 36 LOC, score 18.4, rank 161; the free
 * MSV intrinsic wrapper IDF at line 10370 is the batch-8 unit and
 * calls this method).
 *
 * The inverse-document-frequency transform over the subscripted
 * global's children: DC is log2-scaled, then each child value is
 * retrieved, log2-transformed and stored back as one decimal.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : ttt/tmp/tmp1/rslt/ref STR_MAX C-string
 *    buffers.
 *  - h009 format_string     : the vendor sscanf(rslt, "%lf", &val)
 *    becomes the local C-locale parser sscanfLf (item-count shape
 *    preserved: null for 0 conversions); the sprintf(rslt, "%.1lf",
 *    val) becomes a Locale.US %.1f formatter (h009 unit-1 ctime
 *    precedent).
 *  - h012 unsigned_byte_domain: buffers are Char(0..255) domains; the
 *    trailing-control trim compares .code against 32.
 *  - h013 dead_store        : the vendor's long j=0 is never used -
 *    dropped with this note.
 *  - h014 out_of_scope_call  : mglobal (InterpStubs stub) and
 *    MumpsGlobalException (MdhStubs) reused unmodified; LOG2 is the
 *    libmpscpp.h:86 macro over math.h log2 -> kotlin.math.log2 (real
 *    mapping); get_global/set_global are the libmpscpp.h:117/118
 *    preprocessor macros and are expanded inline (unit-44 precedent) -
 *    no stubs are created for macros.
 *  - h017 naming            : IDF keeps the vendor name; emitted as a
 *    GlobalRef extension (unit-14/32/44 precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - As in Min (unit 44), Mglobal(XNEXT,...) writes the next key back
 *    into the key buffer in place, so the loop reads tmp after the
 *    call - intended contract, kept as-is.
 *  - The vendor declares int i but never branches on it after the
 *    trim - i is the strncmp length only, kept.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val STORE = 1 // vendor defines.h:84
private const val XNEXT = 8 // vendor defines.h:88
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71
private const val INVALID_CONVERSION = 1 // vendor libmpscpp.h:70
private const val DATA_RANGE_ERROR = 4 // vendor libmpscpp.h:73

/**
 * h009: vendor sscanf(s, "%lf", &val) - C-locale leading-double scan;
 * returns null where the vendor reports 0 converted items.
 */
private fun sscanfLf(s: CharArray): Double? {
    val m = Regex("[+-]?(?:[0-9]+(?:\\.[0-9]*)?|\\.[0-9]+)(?:[eE][+-]?[0-9]+)?")
        .find(cstringAt(s).trimStart(' ', '\t', '\n', '\u000B', '\u000C', '\r'))
        ?: return null
    return m.value.toDoubleOrNull()
}

fun GlobalRef.IDF(DC: Double) {
    var dc = DC // vendor reassigns DC (LOG2 scale)

    val ttt = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)
    val ref = CharArray(STR_MAX)
    var i: Int
    var v: Double

    // vendor: if (DC<=0) { GlobalExceptionCode=DATA_RANGE_ERROR; throw }
    if (dc <= 0.0) {
        MdhState.globalExceptionCode = DATA_RANGE_ERROR
        throw MumpsGlobalException()
    }

    dc = log2(dc) // vendor: DC=LOG2(DC); (libmpscpp.h:86 macro over log2)

    // vendor: strcpy(tmp,ref); i=strlen(ref);
    writeCstring(ref, g.ref) // ref buffer, unit-44 conflation note
    strcpyC(tmp, ref)
    i = strlenC(ref)

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
        // get_global is the libmpscpp.h:117 macro, expanded inline:
        //   (svPtr->tpx = Mglobal(RETRIEVE, ref, rslt, svPtr), rslt)
        MdhState.svPtr.tpx = mglobal(RETRIEVE, tmp1, rslt, MdhState.svPtr).toLong()

        // vendor: if (!svPtr->tpx) { ...GLOBAL_NOT_FOUND...; throw }
        if (MdhState.svPtr.tpx == 0L) {
            MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        // vendor: if (sscanf(rslt,"%lf",&val)==0) { ...INVALID_CONVERSION...; throw }
        v = sscanfLf(rslt)
        if (v == null) {
            MdhState.globalExceptionCode = INVALID_CONVERSION
            throw MumpsGlobalException()
        }

        // vendor: if (val <= 0.0) { ...DATA_RANGE_ERROR...; throw }
        if (v <= 0.0) {
            MdhState.globalExceptionCode = DATA_RANGE_ERROR
            throw MumpsGlobalException()
        }

        v = dc - log2(v) + 1 // vendor: val=DC-LOG2(val)+1;

        // vendor: sprintf(rslt,"%.1lf",val);
        writeCstring(rslt, String.format(Locale.US, "%.1f", v))

        // set_global is the libmpscpp.h:118 macro, expanded inline:
        //   (svPtr->tpx = Mglobal(STORE, ref, rslt, svPtr), rslt)
        MdhState.svPtr.tpx = mglobal(STORE, tmp1, rslt, MdhState.svPtr).toLong()
    }
}
