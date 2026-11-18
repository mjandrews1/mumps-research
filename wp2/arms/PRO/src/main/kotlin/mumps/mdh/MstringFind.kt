package mumps.mdh

import mumps.interp._find
import mumps.interp.atoiC
import mumps.interp.mglobal
import mumps.interp.writeCstring

// ----------------------------------------------------------------------------

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

// ----------------------------------------------------------------------------

/**
 * PRO-arm translation of libmpscpp.cpp::mstring::Find(global, int)
 * (roster row 246, batch 8, closing batch 8; units.csv: loc 16,
 * comment_ratio 0.059, pointer_marks 0, control_marks 1, score 4.2,
 * complexity_rank 8). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 7649-7670. Disambiguates from the sibling
 * mstring::Find overloads by the control-mark-1 retrieve arm: the
 * const char* form (7638-7647) and the mstring form (7671-7681) have
 * no Mglobal call (loc 10 each) and are not on the roster.
 *
 * The MUMPS dollar-Find intrinsic with THIS mstring's value as the
 * haystack and the key read from the database: st via Mltoa, one
 * retrieve of the key global (throwing MumpsGlobalException on
 * absence), then _find over data.c_str() and atoi of the result.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer     : a[40]/r[20] buffers plus tmp1[STR_MAX]
 *    (C-string domains); the receiver's data string rides a byte
 *    buffer for _find (u33 precedent, same call shape).
 *  - h010 numeric_promotion: vendor Mltoa(long,...) receives the
 *    int st via implicit widening - explicit .toLong().
 *  - h012 unsigned_byte_domain: buffers are Char(0..255) domains.
 *  - h014 out_of_scope_call: mglobal/_find/atoiC/Mltoa seats as in
 *    u33/u185/u216 (GlobalFind.kt) reused unmodified;
 *    MdhState.globalExceptionCode = GLOBAL_NOT_FOUND (2) +
 *    MumpsGlobalException() throw, mdh family convention; the key
 *    global's ref rides the String carrier (unit-39 precedent)
 *    folded into a byte buffer.
 *  - h015 grammar_collision: overload folds into the Kotlin
 *    overload set by receiver/parameter type (Mstring receiver,
 *    Global key - receiver-distinct from the GlobalRef.Find family
 *    in GlobalFind.kt rows 62/185/216); pre-push scan clean.
 *  - h017 naming          : Find keeps the vendor name; Mstring
 *    extension receiver (LibmpscppMisc6.kt family precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The receiver data is the haystack (data.c_str() at vendor
 *    7666) and the KEY comes from the database - the retrieve of
 *    key.ref with the GLOBAL_NOT_FOUND throw is the only control
 *    arm, exactly as in u185's second retrieve.
 */
fun Mstring.Find(key: Global, st: Int): Int {
    val a = CharArray(40)
    val r = CharArray(20)
    val tmp1 = CharArray(STR_MAX)

    Mltoa(st.toLong(), a) // h010: int -> long widening documented

    // vendor: Mglobal(RETRIEVE, (unsigned char *) key.ref, tmp1, svPtr)
    val keyRefBuf = CharArray(STR_MAX)
    writeCstring(keyRefBuf, key.ref)
    val f = mglobal(RETRIEVE, keyRefBuf, tmp1, MdhState.svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor: _find(r, (unsigned char *) data.c_str(), tmp1, a)
    val dataBuf = CharArray(STR_MAX)
    writeCstring(dataBuf, value)
    _find(r, dataBuf, tmp1, a)
    return atoiC(r) // vendor: rr = atoi((char *) r)
}
