package mumps.interp

/**
 * CRuntime: faithful Kotlin mappings of the libc functions the C/C++
 * interpreter uses (atoi, etc.). Shared utility (heuristic 28) — these are
 * library calls, not study units, so they live together and are translated
 * once with their quirks documented.
 */
object CRuntime {

    /**
     * C: atoi(str) — leading optional whitespace + sign + digits; 0 if no
     * valid prefix. Divergence note: C atoi overflow is undefined behavior
     * (glibc truncates); this returns 0 on values beyond Int range, which is
     * impossible to hit through the frozen oracle cases.
     */
    /**
     * C: strtod(str, NULL) — leading floating-point number, 0.0 if no valid
     * prefix. Divergence note: C strtod also accepts hex ("0x1p3") and
     * inf/nan spellings; unreachable through the frozen oracle cases.
     */
    fun strtod(s: String): Double {
        val m = Regex("^[+-]?(\\d+\\.\\d*|\\.\\d+|\\d+)([eE][+-]?\\d+)?").find(s.trim()) ?: return 0.0
        return m.value.toDoubleOrNull() ?: 0.0
    }

    /**
     * C: gcvt(x, ndigits, buf) — glibc semantics: same as %.{ndigits}g but
     * always includes a decimal point for finite results without one
     * (e.g. 0 -> "0.", 3 -> "3."). OPEN VERIFICATION FLAG (heuristic 41):
     * confirm against the reference binary $ZACOS output during integration.
     */
    fun gcvt(x: Double, ndigits: Int): String {
        var s = String.format("%." + ndigits + "g", x)
        if (!s.contains('.') && !s.contains('e') && !s.contains('E') &&
            !s.contains("inf") && !s.contains("NaN")) s += "."
        return s
    }

    fun atoi(s: String): Int {
        val m = Regex("^[+-]?\\d+").find(s.trim()) ?: return 0
        return m.value.toIntOrNull() ?: 0
    }
}
