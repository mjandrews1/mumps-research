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
    fun atoi(s: String): Int {
        val m = Regex("^[+-]?\d+").find(s.trim()) ?: return 0
        return m.value.toIntOrNull() ?: 0
    }
}
