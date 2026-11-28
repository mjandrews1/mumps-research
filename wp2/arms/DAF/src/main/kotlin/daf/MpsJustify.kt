package daf

// Unit: interp-arith.cpp::mps_justify (roster row 84; DAF R1, the
// C-019 Option A render-close): the \\$JUSTIFY string-format
// intrinsic: sprintf(b, a, fc) with the caller-built format a and
// the parsed value fc - the Fcn.kt \\$JUSTIFY() seat builds the
// "%<l>.<k>f" format there.
// Consults (2026-11-27, nominal): BASE-log rows 39/40 hit-reuse
// (the config ifdef web resolves to the reference set -
// MULTI_PRECISION false, BIGFLOAT false - so the MPFR mpfr_sprintf
// branch and the long-double strtold branch are dead and carried
// as the comment fold below), row 40 hit-reuse (strtod ->
// DafCRuntime.strtod), rows 99/100 hit-adapt (the out-param b
// folds to the String return; the sprintf format call folds to
// java.lang.String.format with Locale.US - the C-locale render,
// the H-009 note family; the caller writes the result into its
// own buffer via its bdSet carrier).
// Vendor quirk kept: the vendor passes the raw caller-built
// format string to sprintf with a single double argument; the
// render passes the identical format string to String.format -
// format-string provenance stays at the Fcn.kt seat, kept
// verbatim there.
// Dafny: EXEMPT (the sprintf/format family reason: the format
// string is caller-built data; no honest decidable core; journal
// row).
// Release note: the Fcn.kt JUSTIFY call site and this render
// replace the mpsJustifyDaf carrier stub (out-of-scope owner note
// in DafInterpStubsT3.kt; stub deleted this session per the
// C-019 R1 close; signature note: the res out-param folds to the
// String return).

fun mpsJustifyDaf(fmt: String, valS: CharArray): String =
    java.lang.String.format(java.util.Locale.US, fmt, DafCRuntime.strtod(DafCRuntime.cstring(valS)))

// Reference-build config note (row 39): the C
//   #ifndef MULTI_PRECISION / #ifdef BIGFLOAT strtold #else strtod
// lattice selects the double fc=strtod(c,NULL) branch with
// sprintf(b,a,fc); the bigfloat strtold branch and the MPFR
// mpfr_set_str/mpfr_sprintf branch (the static init-once flag f)
// are dead on the reference build, carried as this comment fold
// only.
