package mumps.interp

/**
 * PRO-arm translation of interp-arith.cpp::mps_justify (B3 unit
 * 83, rank 110). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 888-912.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-arith.cpp::mps_justify, cpp, loc 24,
 * comment_ratio 0.04, pointer_marks 3, control_marks 1, score
 * 11.2, batch 3 - exact match to wp1/study-subset/units.csv row
 * 110. Single overload in the vendor.
 *
 * void mps_justify(char* a, char* b, char* c): the MUMPS
 * (dollar)justify formatting core - NOTE the vendor parameter
 * order: a is the C FORMAT STRING, b the OUTPUT buffer, c the
 * width/value text; sprintf(b, a, fc).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string     : sprintf(b, a, fc) with a RUNTIME
 *    format string - rendered as java.util.String.format over
 *    the format text carried in a (the JVM formatter accepts the
 *    C-style conversions; the vendor's own call sites pass
 *    numeric formats like "%n.nf").
 *  - h012 unsigned_byte_domain: a/b/c are NUL-terminated byte
 *    domains; strtod resolves to the real strtodC (unit 10).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level mpsJustify - no collisions.
 *  - h017 naming            : mps_justify -> mpsJustify (mpsAcos
 *    precedent).
 *  - h018 preprocessor_conditional: the 3-way #ifdef below is
 *    resolved against the reference build config (B1-close rule;
 *    see the paragraph at the end of this KDoc).
 *
 * h018 preprocessor_conditional (CORRECTED at B3 close, was first
 * written as a record_miss): the vendor body is a 3-way #ifdef
 * (MULTI_PRECISION / BIGFLOAT / default). Rule h018 has existed
 * since the B1 close (captured from units 4/10/12/20/29, upgraded
 * unit_tested at the B2 close exercising units 40/41) - so a miss
 * could not apply. The reference build (MULTI_PRECISION and
 * BIGFLOAT undefined) selects the plain double branch
 * (double fc = strtod(c,NULL); sprintf(b,a,fc)) - translated
 * here; the MPFR and long-double arms are compiled out of the
 * reference binary and documented as the excluded arms, per the
 * h018 action.
 */
fun mpsJustify(a: CharArray, b: CharArray, c: CharArray) {
    val fc = strtodC(c)
    val s = String.format(cstringAt(a), fc) // sprintf(b, a, fc)
    for (k in s.indices) b[k] = s[k]
    b[s.length] = NUL
}

