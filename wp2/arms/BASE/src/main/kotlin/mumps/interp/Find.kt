package mumps.interp

/**
 * Kotlin translation of interp-sym.cpp::_find (Study Subset unit 6,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * Mumps $FIND: starting from position atoi(begin) (clamped to >= 0),
 * find key in in; if found, return the position AFTER the occurrence,
 * else 0. Result written to the C out-buffer -> returned as String
 * (heuristic 7: out-param -> return type).
 */
fun _find(in_: String, key: String, begin: String): String {
    var i = CRuntime.atoi(begin)
    if (i < 0) i = 0

    if (key.isNotEmpty()) {
        val xi = xindex(in_, key, i.toShort())
        i = if (xi > 0) xi + key.length else xi
    }

    if (i > in_.length + 1) i = 0

    return i.toString() // C: sprintf(out, "%d", i)
}
