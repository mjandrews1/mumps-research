package doc

// Unit 29 (B1 tranche 5): include/mumpsc/numSetup.h::<misc> (batch 1,
// complex stratum; units.csv row 29: loc 212). The numSetup.h
// arithmetic include-fragment: textually pasted by the vendor into
// the arithmetic evaluator with the opcode preprocessor symbol
// (numSetupDefines.h NS_ADD..NS_INTDIV). H-018 preprocessor
// conditionals: the reference build (no BIGFLOAT, no
// MULTI_PRECISION) selects the hardware floating-point and the
// hardware fixed-point arms; the BIGFLOAT long-double arms, the
// MPFR (multi-precision real) arm and the GMP (multi-precision
// integer) arm are excluded and restated as the ledger comments
// below. The preprocessor opcode dispatch renders as the runtime
// switch (the opcode seat is the NS_* consts, kept verbatim from
// numSetupDefines.h); the NS_DIV entry quirk is kept exactly: in
// the vendor the division is ALWAYS done in floating point (the
// empty then-arm of the first conditional), while the other
// opcodes fall through when neither operand is a float. a, b and c
// are the surrounding evaluator's operand and result carriers.
// floatstring is the H-014 stub; gcvt_wrapper rides the H-014 stub
// seat (owner numSetup.h residue, per the unit-10 seat note; the
// vendor GCVT define arm selecting the sprintf %g variant over the
// gcvt() library call is the H-018 ledger note here); strtod
// reuses the internal helper; the C atoll domain reuses the
// internal helper (widened to Int per H-010 as the vendor writes
// atoll results into ints). The integer division by zero is C
// undefined behavior; the render throws the Kotlin
// ArithmeticException at that point (divergence documented). The
// NS_INTDIV integer arm keeps the C truncating division shape.
// Naming per H-017.

// numSetupDefines.h:1-5
const val NS_ADD = 1
const val NS_SUB = 2
const val NS_MULT = 3
const val NS_DIV = 4
const val NS_INTDIV = 5

fun numSetupArith(op: Int, a: CharArray, b: CharArray, c: CharArray) {
    // floating point section: NS_DIV is always floating point; the
    // other opcodes only when either operand is a float
    if (op == NS_DIV ||
            Stubs.floatstring(a) || Stubs.floatstring(b)) {

        // hardware floating point (live arm; BIGFLOAT long double
        // arms via strtold excluded per the reference build)

        val af = strtod(CstringOps.cstring(a))
        val bf = strtod(CstringOps.cstring(b))
        val cf: Double = when (op) {
            NS_ADD -> af + bf
            NS_SUB -> af - bf
            NS_MULT -> af * bf
            NS_DIV -> af / bf
            NS_INTDIV -> Math.floor(af / bf)
            else -> af // unreachable: vendor dispatch is exhaustive
        }
        Stubs.gcvtWrapper(cf, c)
        return

        // MULTI_PRECISION MPFR arm excluded (mpfr_init2/fa/fb/fc
        // and the divide-by-zero MumpsGlobalException path); the MP
        // per-opcode mpfr_add/sub/mul/div/trunc dispatch and the
        // mpfr_sprintf %.8RNg render are the H-018 ledger entries.
    }

    // integer section - both operands are integer

    // hardware fixed point (live arm)
    val aa = atollShared(CstringOps.cstring(a)).toInt()
    val bb = atollShared(CstringOps.cstring(b)).toInt()

    when (op) {
        NS_ADD -> {
            CstringOps.strcpyS(c, (aa + bb).toString())
            return
        }
        NS_SUB -> {
            CstringOps.strcpyS(c, (aa - bb).toString())
            return
        }
        NS_MULT -> {
            CstringOps.strcpyS(c, (aa * bb).toString())
            return
        }
        NS_DIV -> {
            // integer div actually done as flt pt above
            val af = strtod(CstringOps.cstring(a))
            val bf = strtod(CstringOps.cstring(b))
            val cf = af / bf
            Stubs.gcvtWrapper(cf, c)
            return
        }
        NS_INTDIV -> {
            // C truncating division; a zero divisor is undefined
            // behavior in the vendor (throw documented)
            val cc = aa / bb
            CstringOps.strcpyS(c, cc.toString())
            return
        }
    }

    // MULTI_PRECISION GMP mpz arm excluded (mpz_init/na/nb/nc and
    // the gmp_sscanf/gmp_add..gmp_div/gmp_sprintf render are the
    // H-018 ledger entries)
}
