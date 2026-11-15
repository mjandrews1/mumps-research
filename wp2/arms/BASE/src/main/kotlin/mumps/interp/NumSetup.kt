package mumps.interp

import java.util.Locale
import kotlin.math.floor

/**
 * Kotlin translation of include/mumpsc/numSetup.h (Study Subset unit 29,
 * Batch 1, arm BASE; translated 2026-11-15), plus its opcode constants
 * from numSetupDefines.h.
 *
 * C SHAPE: numSetup.h is a FUNCTION-BODY FRAGMENT, not a header of
 * declarations. interp-arith.cpp includes it five times inside
 * add/sub/mult/divx/divi, each time preceded by `#define opcode NS_*`.
 * The preprocessor stamping (heuristic 74 family) becomes ONE function
 * parameterized over opcode; when(opcode) selects the branch, and the
 * surrounding `if (floatstring(a) || floatstring(b))` guard is compiled
 * in/out per include site — NS_DIV always enters the floating-point
 * section, the other opcodes only when an operand is a floatstring.
 *
 * Reference build config (verified against the frozen tarball banner):
 * no MULTI_PRECISION (MPFR), no BIGFLOAT (long double), no GCVT.
 * gcvt_wrapper(x, c) therefore expands to sprintf(c, "%.8g", x).
 * The MPFR/GMP branches are preserved as guarded TODOs (heuristic 39).
 */
const val NS_ADD = 1
const val NS_SUB = 2
const val NS_MULT = 3
const val NS_DIV = 4
const val NS_INTDIV = 5

/** C: include/mumpsc/libmpscpp.h: #define NUMERIC_RANGE 5 (MP branch only). */
const val NUMERIC_RANGE = 5

/**
 * C: gcvt_wrapper(x, c) -> sprintf(c, "%.8g", x) in the reference build
 * (GCVT undefined — verified against /usr/bin/mumps: $zacos(1) prints
 * "0" with no appended point). NOT the same as CRuntime.gcvt, which
 * models glibc gcvt()'s always-appended decimal point and is unused by
 * this configuration.
 *
 * C %.8g: 8 significant digits, trailing zeros trimmed, e+dd exponent
 * in scientific form. Java's %.8g matches on all of those except the
 * spellings of inf/nan; those are mapped to C's forms.
 */
internal fun fmt8g(x: Double): String {
    if (x.isNaN()) return "nan"
    if (x.isInfinite()) return if (x < 0) "-inf" else "inf"
    return String.format(Locale.US, "%.8g", x)
}

/**
 * The numSetup.h body. C signature (per include site): the enclosing
 * add/sub/mult/divx/divi(a, b, c) write the result into char *c; the
 * out-param becomes this function's return value.
 *
 * C: `if (opcode == NS_DIV) { } else if (floatstring(a) || floatstring(b))
 * { ...floating point section... return; }` — division is ALWAYS done in
 * floating point; other opcodes only when an operand is a floatstring.
 */
fun numSetup(opcode: Int, a: String, b: String): String {

    if (opcode == NS_DIV || floatstring(a) || floatstring(b)) {

        //........................
        // floating point section
        //........................

        if (InterpState.MULTI_PRECISION) {
            // C: MPFR path — mpfr_init2(fa/fb/fc/zero, FLT_PRECISION=72);
            // set_str with digit-prefix truncation on parse failure
            // (for i=0; ca[i]=a[i]; i++) ; zero-denominator check sets
            // GlobalExceptionCode=NUMERIC_RANGE and throws
            // MumpsGlobalException; mpfr_sprintf(c, "%.8RNg", fc).
            TODO("MPFR branch: reference build does not define MULTI_PRECISION (heuristic 39)")
        }

        // reference build: hardware floating point, no BIGFLOAT (double, not long double)
        val af = CRuntime.strtod(a)
        val bf = CRuntime.strtod(b)

        return when (opcode) {
            NS_ADD -> fmt8g(af + bf)
            NS_SUB -> fmt8g(af - bf)
            NS_MULT -> fmt8g(af * bf)
            NS_DIV -> fmt8g(af / bf) // C: no zero-denominator check on the hardware path (inf)
            NS_INTDIV -> fmt8g(floor(af / bf)) // C: floor(af/bf), not trunc
            else -> throw IllegalArgumentException("numSetup: bad opcode $opcode")
        }
    }

    //............................................
    // integer section - both operands are integer
    //............................................

    if (InterpState.MULTI_PRECISION) {
        // C: GMP path — mpz_init once (static f flag); gmp_sscanf(a,"%Zd")
        // with "" -> "0" fallback; mpz_add/sub/mul/div; gmp_sprintf(c,"%Zd",nc).
        TODO("GMP branch: reference build does not define MULTI_PRECISION (heuristic 39)")
    }

    // C: int aa = atoll(a); — long long result TRUNCATED to int (heuristic 65)
    val aa = CRuntime.atoll(a).toInt()
    val bb = CRuntime.atoll(b).toInt()

    return when (opcode) {
        NS_ADD -> (aa + bb).toString()   // C: cc=aa+bb; sprintf(c,"%d",cc)
        NS_SUB -> (aa - bb).toString()
        NS_MULT -> (aa * bb).toString()
        NS_INTDIV -> (aa / bb).toString() // C: cc=aa/bb — C int division truncates toward zero
        // C: NS_DIV never reaches the integer section (top-of-file #if) —
        // it is done as floating point divide even here; preserved faithfully.
        NS_DIV -> fmt8g(CRuntime.strtod(a) / CRuntime.strtod(b))
        else -> throw IllegalArgumentException("numSetup: bad opcode $opcode")
    }
}

/**
 * C: interp-arith.cpp thin wrappers around the five include sites.
 * add(a,b,c) is the include host itself; add1/add2/add3 convert a
 * numeric second operand via sprintf(bb,"%ld"|"lf") then delegate.
 * Provided so later units have the same entry points the C exposes.
 */
fun mumpsAdd(a: String, b: String): String = numSetup(NS_ADD, a, b)
fun mumpsSub(a: String, b: String): String = numSetup(NS_SUB, a, b)
fun mumpsMult(a: String, b: String): String = numSetup(NS_MULT, a, b)
fun mumpsDivx(a: String, b: String): String = numSetup(NS_DIV, a, b)
fun mumpsDivi(a: String, b: String): String = numSetup(NS_INTDIV, a, b)
