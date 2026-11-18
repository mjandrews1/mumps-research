package mumps.interp

import kotlin.math.ln

/**
 * PRO-arm translation of interp-arith.cpp::mps_log (roster row
 * 271, batch 9; units.csv: loc 22, comment_ratio 0.043,
 * pointer_marks 2, control_marks 1, score 9.8, rank 88).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 192-217 - RESOLVES the InterpStubs mpsLog stub
 * raised at u156 (the Zfcn zlog-family arms bind unchanged).
 *
 * mps_log(a, b): the natural-log intrinsic over decimal strings.
 * The C log is natural log; the JVM ln is the same function
 * (h017 note: log vs ln naming documented).
 *
 * h018 TWO-LEVEL trichotomy (u178 mps_log2 twin, same file):
 * MULTI_PRECISION OUT (the MPFR static fa/fb arm journaled);
 * BIGFLOAT OUT (the logl/strtold arm journaled); the plain
 * log/strtod arm IN.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string     : gcvtWrapper (NumSetup.kt:114).
 *  - h012 unsigned_byte_domain: a/b NUL-terminated; strtodC
 *    shared regex seat (MpsAcos.kt:34).
 *  - h015 grammar_collision : a/b kept; new top-level mpsLog
 *    scanned clean cross-file.
 *  - h017 naming            : mps_log -> mpsLog; ln is the JVM
 *    intrinsic for the C natural-log log.
 */
fun mpsLog(a: CharArray, b: CharArray) {
    gcvtWrapper(ln(strtodC(b)), a)
}
