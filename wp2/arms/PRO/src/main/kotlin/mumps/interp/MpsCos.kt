package mumps.interp

import kotlin.math.cos

/**
 * PRO-arm translation of interp-arith.cpp::mps_cos (roster row
 * 272, batch 9; units.csv: loc 22, comment_ratio 0.043,
 * pointer_marks 2, control_marks 1, score 9.8, rank 89).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 223-249 - RESOLVES the InterpStubs mpsCos stub
 * raised at u156 (the Zfcn zcos arm binds unchanged).
 *
 * mps_cos(a, b): the cosine intrinsic over decimal strings; the
 * vendor's bare trailing return renders as Unit (u178
 * mps_log2 precedent).
 *
 * h018 TWO-LEVEL trichotomy (u179 mps_sin twin, same file):
 * MULTI_PRECISION OUT (the MPFR static fa/fb arm journaled);
 * BIGFLOAT OUT (the cosl/strtold arm journaled); the plain
 * cos/strtod arm IN.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string     : gcvtWrapper (NumSetup.kt:114).
 *  - h012 unsigned_byte_domain: a/b NUL-terminated; strtodC
 *    shared regex seat.
 *  - h015 grammar_collision : a/b kept; new top-level mpsCos
 *    scanned clean cross-file.
 *  - h017 naming            : mps_cos -> mpsCos.
 */
fun mpsCos(a: CharArray, b: CharArray) {
    gcvtWrapper(cos(strtodC(b)), a)
}
