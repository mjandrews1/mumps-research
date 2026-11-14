package mumps.interp

/**
 * Kotlin translation of interp-arith.cpp::numcomp (Study Subset unit 20,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * C: int numcomp(unsigned char *aa, unsigned char *bb)
 * strcmp-style numeric comparison: floatstrings compare as doubles,
 * otherwise as (atoll-truncated) integers.
 *
 * Reference build: no MULTI_PRECISION (GMP branch TODO behind the flag,
 * heuristic 39) and no BIGFLOAT (double path).
 */
fun numcomp(aa: String, bb: String): Int {
    if (InterpState.MULTI_PRECISION) {
        TODO("GMP branch: reference build does not define MULTI_PRECISION (heuristic 39)")
    }

    if (floatstring(aa) || floatstring(bb)) {
        // C: double fa = strtod(aa, NULL); double fb = strtod(bb, NULL);
        val fa = CRuntime.strtod(aa)
        val fb = CRuntime.strtod(bb)
        if (fa == fb) return 0
        return if (fa > fb) 1 else -1
    }

    // C: int fa = atoll(aa); int fb = atoll(bb); — atoll's long long result is
    // TRUNCATED to int in the C (faithfully preserved via .toInt(), heuristic 65)
    val fa = CRuntime.atoll(aa).toInt()
    val fb = CRuntime.atoll(bb).toInt()
    if (fa == fb) return 0
    return if (fa > fb) 1 else -1
}
