package doc

import java.util.Locale

// Unit 73 (B3 tranche 2): interp-arith.cpp::mps_justify (batch 3, core
// stratum; LOC 24, score 11.2). The MUMPS JUSTIFY formatting echo:
// c is the numeric string parsed with the arm-wide C-protocol strtod
// (MpsExp.kt render, H-010 numeric parse), and a is the caller-supplied
// FORMAT STRING handed straight to the emission - sprintf(b, a, fc)
// with a dynamic format. That is legal C and the render keeps it:
// String.format(Locale.US, fmt, fc) per H-009 with a runtime format,
// and the formatted result strcpyS'd into the b carrier (H-007 b is a
// write-only out buffer on this call path). H-018 application: the
// reference-build live arm is the plain-double strtod path; the
// BIGFLOAT arm (long double fc via strtold, same sprintf(a, fc)
// emission) and the MULTI_PRECISION arm (static mpfr_t fc initialized
// on the first call only - the f=1 one-time-init quirk; mpfr_set_str
// base 10 MPFR_RNDN; mpfr_sprintf(b, a, fc)) are compiled out per the
// reference build config and ledgered here. Naming per H-017.

fun mps_justify(a: CharArray, b: CharArray, c: CharArray) {
    val fc = strtod(CstringOps.cstring(c))
    val fmt = CstringOps.cstring(a)
    CstringOps.strcpyS(b, String.format(Locale.US, fmt, fc))
}
