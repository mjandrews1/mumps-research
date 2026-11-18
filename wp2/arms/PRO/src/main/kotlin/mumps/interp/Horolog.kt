package mumps.interp

/**
 * PRO-arm translation of interp-sym.cpp::_horolog (roster row 278,
 * batch 10; units.csv: loc 16, comment_ratio 0.059, pointer_marks 0,
 * control_marks 0, score 2.8, rank 2). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 1305-1322.
 *
 * void _horolog(unsigned char in[])
 *
 * Write the $H (horolog) day,seconds pair into the caller buffer.
 * Resolves the _horolog seat raised at the B7 u188 render (owner tag
 * corrected batch-9 -> batch-10 roster row 278).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string       : sprintf("%ld") renders via
 *    Long.toString (the ArithMisc u? %ld precedent).
 *  - h011 control_flow        : the vendor tail return folds to the
 *    last statement (no fallthrough residue).
 *  - h012 unsigned_byte_domain: the in[] buffer rides the h012
 *    Char(0..255) domain via strcpyC/strcatC.
 *  - h017 naming              : _horolog keeps the vendor name
 *    (the Parse_.kt _horolog emission and ZfcnsH seat bind
 *    unchanged).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - time_t timex = time(&timex) -> System.currentTimeMillis() /
 *    1000L (the Fcn.kt $horolog precedent, vendor case 14).
 *  - The epoch constant here is 47122 - this DIFFERS from the Fcn.kt
 *    $horolog arm (47116 + day + 1); both constants are vendor
 *    originals and both are kept as-is, not unified.
 *  - day/fd reuse the vendor two-variable shape (fd = day before the
 *    sprintf, fd = timex after) even though Kotlin could fuse them.
 */
/** interp-sym.cpp:1305-1322 — void _horolog(unsigned char in[]). */
fun _horolog(t: CharArray) {
    val timex0 = System.currentTimeMillis() / 1000L // time(&timex)
    var day = timex0 / 86400L
    var timex = timex0 - day * 86400L
    day = 47122L + day
    var fd = day
    // sprintf((char*) in, "%ld", fd);
    strcpyC(t, fd.toString())
    // strcat((char*) in, ",");
    strcatC(t, ",")
    fd = timex
    // sprintf(tmp2, "%ld", fd); strcat((char*) in, tmp2);
    val tmp2 = fd.toString()
    strcatC(t, tmp2)
}
