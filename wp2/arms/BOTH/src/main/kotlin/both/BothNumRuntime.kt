package both

// BOTH-arm numeric runtime seats for the mps_* family (batch-5
// tranche-1 renders). The vendor seats are C macros/helpers, not
// roster units: gcvt_wrapper (included via numSetup.h; defined
// interp-arith.cpp:69-77) expands to the reference-arm
// gcvt(x, @fp_digits@, a) with the frozen build config value
// fp_digits=8 (configure.ac:239 default, confirmed by the frozen
// reference build). Rendered here as a real function (manual
// decision, journal-logged; no rule family covers a non-roster macro
// seat); first exercise is mps_log10 (unit 132) with the mps_acos
// sibling (roster 10, batch 1) the family's other corpus consumer, and
// the sprintf("%g",...) site of unit 130 (Avg) as the printf-%g twin.
//
// Consults:
//   h009 format_string - reused: C %g digit shape (six significant
//     digits for the printf site; eight for the gcvt site), Locale.US
//     digit grouping-free, trailing mantissa zeros stripped and a
//     bare trailing point dropped, C99 nan/inf spellings.
//   h012 unsigned_byte_domain - reused at the buffer boundary: the
//     out-param stays a CharArray (byte domain), caller NUL-terminates
//     per the C idiom.
//   h017 naming - reused (gcvt_wrapper -> gcvtWrapper, strtod ->
//     strtodC per the C-prefixed call-site shape).

import java.util.Locale

// C gcvt(x, 8, a) for the reference build: the C %g shape with
// precision 8, written into the out-buffer without a NUL (callers in
// this tranche immediately consume or copy).
internal fun gcvtWrapper(x: Double, out: CharArray) {
    val a = cFormatG(x, 8).toCharArray()
    require(a.size <= out.size) { "gcvt out-buffer too small" }
    System.arraycopy(a, 0, out, 0, a.size)
}

// The printf("%g", x) seat (h009; default precision 6).
internal fun fmtG(x: Double): String = cFormatG(x, 6)

// C99 %.*g: precision p significant digits; %f style when the decimal
// exponent X satisfies -4 <= X < p, %e style otherwise; in both forms
// trailing fractional zeros (and a bare point) are removed; nan/inf
// lowercase per C99.
internal fun cFormatG(x: Double, p: Int): String {
    if (x.isNaN()) return "nan"
    if (x.isInfinite()) return if (x < 0) "-inf" else "inf"
    val prec = if (p == 0) 1 else p

    // Java's %.{p}g picks the same e/f split as C99 and emits the same
    // mantissa digits; it differs only in NOT stripping trailing
    // zeros (and NaN/inf spelling, handled above).
    var s = String.format(Locale.US, "%." + prec + "g", x)

    // split mantissa / exponent
    val eIdx = s.indexOf('e')
    var mant = if (eIdx == -1) s else s.substring(0, eIdx)
    val exp = if (eIdx == -1) "" else s.substring(eIdx)

    if (mant.contains('.')) {
        mant = mant.trimEnd('0')
        if (mant.endsWith(".")) mant = mant.dropLast(1)
    }
    return mant + exp
}

// C strtod(b, NULL): parse the leading numeric prefix; on a bad
// prefix return 0.0 (manual decision, journal note: adequate for the
// mps_* corpus inputs, which are the interpreter's own formatted
// numerics; no hex/inf special-form support).
internal fun strtodC(s: String): Double {
    val m = REGEX_NUMBER.find(s) ?: return 0.0
    return m.value.toDouble()
}

private val REGEX_NUMBER =
    Regex("[+-]?(\\d+\\.?\\d*|\\.\\d+)([eE][+-]?\\d+)?")
