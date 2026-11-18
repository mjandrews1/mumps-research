package mumps.interp

import kotlin.math.pow

/**
 * PRO-arm translation of interp-arith.cpp::mps_exp2 (roster row
 * 269, batch 9; units.csv: loc 26, comment_ratio 0.037,
 * pointer_marks 2, control_marks 1, score 10.2, rank 100).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 818-843 - RESOLVES the InterpStubs mpsExp2 stub
 * raised at u156 (the Zfcn/zz-family arms bind unchanged).
 *
 * mps_exp2(c, a): the 2^x intrinsic over decimal strings. The
 * vendor's parameter naming differs from the mps_log family
 * (c/a here, a/b there) - kept verbatim (h017).
 *
 * h018 TWO-LEVEL trichotomy (u178 mps_log2 twin, same file,
 * same structure - journaled): MULTI_PRECISION OUT (the MPFR
 * static fa/fc arm journaled); BIGFLOAT OUT (the exp2l/strtold
 * long-double arm journaled); the plain exp2/strtod arm IN.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string     : gcvtWrapper (NumSetup.kt:114).
 *  - h012 unsigned_byte_domain: c/a NUL-terminated; strtodC
 *    shared regex seat.
 *  - h015 grammar_collision : vendor parameter c/a kept (no
 *    Kotlin collision); new top-level mpsExp2 scanned clean
 *    cross-file.
 *  - h017 naming            : mps_exp2 -> mpsExp2; the JVM has
 *    no exp2 intrinsic - 2^x via pow (value-identical, h009-
 *    adjacent note; vendor aa=exp2(...) intermediate kept).
 */
fun mpsExp2(c: CharArray, a: CharArray) {
    val aa = 2.0.pow(strtodC(a)) // vendor: double aa=exp2(strtod(a,NULL)); JVM has no exp2, 2^x via pow (value-identical)
    gcvtWrapper(aa, c)
}
