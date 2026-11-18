package mumps.interp

import java.util.Locale

/**
 * PRO-arm translation of interp-sysfunc.cpp::unpad (roster row 289,
 * batch 10; units.csv: loc 16, comment_ratio 0.059, pointer_marks 8,
 * control_marks 3, score 22.0, rank 175). Translated independently
 * from the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source: the complete void
 * unpad(unsigned char *key, struct MSV *svPtr) body. This unit
 * RESOLVES the InterpStubs unpad stub raised at u155 (roster row
 * 289; stub removed, cleanLocks precedent; the fullUnpad caller in
 * InterpSysfuncMisc.kt and the GlobalbMisc NEXT-scan call site bind
 * unchanged).
 *
 * unpad: when <dollar>SYSTEM gpad is set, a leading GPADWIDTH numeric pad
 * entry (\\x1f signed-lead marker) is scanned: the sign byte after
 * the marker is normalized ('0' -> '-', else '+'), the GPADWIDTH
 * bytes are read as a double, and the key is rewritten as the %lg
 * form.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string     : sscanf("%lf") reads the GPADWIDTH
 *    prefix as a double (C strtol-style scan; scanner seats such
 *    as Locale.US "%g" folds - the JVM "%g" covers the C "%lg"
 *    shape, LibmpscppMisc9 precedent); sprintf("%lg") ->
 *    String.format(Locale.US, "%g", x).
 *  - h012 unsigned_byte_domain: key rides CharArray; the \\x1f
 *    marker is 31.toChar(); strcpyC/writeCstring carry the NUL-
 *    terminated rewrite.
 *  - h013 dead_code          : on sscanf failure the vendor's x is
 *    uninitialized local memory - the Kotlin fold reads 0.0 (the
 *    benign-superset sentinel, never reached by the gpad writer's
 *    well-formed entries; noted).
 *  - h014 out_of_scope_call : GPADWIDTH is the defines.h:215
 *    constant (40, InterpSysfuncMisc private seat; re-declared
 *    per-file, u44 precedent).
 *  - h017 naming            : unpad keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The gpad==0 early return is first, before any byte is read -
 *    kept.
 *  - The sign normalization happens on key[1] in place BEFORE the
 *    GPADWIDTH copy (so the scanned number carries the normalized
 *    sign) - kept.
 */
private const val GPADWIDTH = 40 // include/mumpsc/defines.h:215

/** interp-sysfunc.cpp unpad(unsigned char *key, struct MSV *svPtr). */
fun unpad(key: CharArray, svPtr: Msv) {
    if (svPtr.gpad == 0) return

    if (key[0] == 31.toChar()) { // a gpad entry found
        val tmp = CharArray(64) // vendor: unsigned char tmp[64]
        var x: Double

        // r++ past the marker
        if (key[1] == '0') key[1] = '-'
        else key[1] = '+'

        // memcpy(tmp, r, GPADWIDTH); tmp[GPADWIDTH] = 0;
        for (i in 0 until GPADWIDTH) tmp[i] = key[1 + i]
        tmp[GPADWIDTH] = 0.toChar()

        // sscanf(tmp, "%lf", &x);  (h013: vendor x uninitialized on miss)
        x = cstringAt(tmp, 0).trim().toDoubleOrNull() ?: 0.0

        // sprintf(key, "%lg", x);
        val s = String.format(Locale.US, "%g", x)
        writeCstring(key, s)
    }
}
