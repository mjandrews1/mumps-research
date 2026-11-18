package doc

import kotlin.math.pow

// Unit 41: interp-arith.cpp::mps_exp10 (batch 2, simple stratum).
// Same reference-config resolution as unit 40 (no MULTI_PRECISION, no
// BIGFLOAT; plain double arm live, excluded arms documentary).
// Vendor defect kept and documented (not fixed): in the excluded
// MULTI_PRECISION arm the code calls mpfr_exp2 (a copy-paste from the
// mps_exp2 family), which would be the wrong function if that arm were
// ever selected; the live arm is unaffected.
// The preprocessor_conditional construct has no cookbook entry (consult
// miss on the closest entry, H-003); noted for the batch-close checklist.

fun mpsExp10(c: CharArray, a: String) {
    // BIGFLOAT arm (excluded by the reference config):
    //   long double aa = exp10l(strtold(a, NULL)); gcvt_wrapper(aa, c)
    // MULTI_PRECISION arm (excluded; carries the mpfr_exp2 copy-paste
    // defect described above):
    //   mpfr statics; mpfr_set_str(fa, a, 10, MPFR_RNDN); mpfr_exp2(...)
    //   mpfr_sprintf(c, "%.8RNg", fc)
    val aa = 10.0.pow(strtod(a))
    Stubs.gcvtWrapper(aa, c)
    return
}
