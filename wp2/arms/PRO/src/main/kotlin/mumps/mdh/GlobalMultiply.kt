package mumps.mdh

/**
 * PRO-arm translation of libmpscpp.cpp::global::Multiply(global,
 * global) (pilot B2, unit 53, provisional under deviation C-003).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 2164-2209 (units.csv: 26 LOC, score 34.2, rank 213; the MSV
 * intrinsic wrapper of the same name, unit 46, dispatches here and
 * is now delegated to this rendering - the unit-46 h014 stub is
 * removed with this unit).
 *
 * The matrix cross-product over global arrays: C = this x B, where
 * rows/columns are ordered subscripts and the cells combine through
 * implicit global-to-double conversions.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h010 numeric_promotion  : the vendor's C truthiness tests on
 *    doubles (!B(k).Data(), !C(i,m).Data()) become == 0.0
 *    comparisons (Kotlin has no boolean coercion of Double) - an
 *    adaptation logged hit-only; the double products/sums are
 *    explicit.
 *  - h014 out_of_scope_call  : every dependency is an existing
 *    owner-named carrier stub, reused unmodified - Global.Kill and
 *    GlobalRef.Kill (owner libmpscpp.cpp class global::Kill),
 *    GlobalRef.Order (owner global::Order), GlobalRef.Data (owner
 *    global::Data), GlobalRef.toDouble (owner global operator
 *    double) and GlobalRef.assign (owner global operator=(double));
 *    the vendor's global x global product in a double context is the
 *    pair of implicit operator double() conversions, rendered as the
 *    two toDouble() calls.
 *  - h017 naming             : Multiply keeps the vendor name;
 *    emitted as a Global extension (the vendor's *this), unit-14
 *    carrier precedent.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor's commented-out debug printf block (per-cell traces
 *    of C/this/B subscripts and values) stays out, as there.
 *  - The (string) casts on the Order results are redundant in C and
 *    dropped; the (double) cast on C(i, m) is the explicit operator
 *    double, kept as toDouble().
 */

fun Global.Multiply(B: Global, C: Global) {
    var i: String
    var j: String
    var k: String
    var m: String

    C().Kill() // vendor: C().Kill();
    i = ""

    while (true) {
        // vendor: i = (string) (*this)(i).Order(1); // each row
        i = this(i).Order(1)
        if (i == "") break

        j = ""
        while (true) {
            // vendor: j = (string) (*this)(i, j).Order(1); // each column
            j = this(i, j).Order(1)
            if (j == "") break

            k = j

            // vendor: if (!B(k).Data()) continue; (C truthiness on double)
            if (B(k).Data() == 0.0) continue

            m = ""
            while (true) {
                m = B(k, m).Order(1)
                if (m == "") break

                // vendor: if (!C(i, m).Data()) { C(i, m) = ...; } else { ... }
                if (C(i, m).Data() == 0.0) {
                    // vendor: C(i, m) = (*this)(i, j) * B(k, m);
                    C(i, m).assign(this(i, j).toDouble() * B(k, m).toDouble())
                } else {
                    // vendor: double x = (*this)(i, j) * B(k, m); double y = (double)C(i, m) + x; C(i, m) = y;
                    val x = this(i, j).toDouble() * B(k, m).toDouble()
                    val y = C(i, m).toDouble() + x
                    C(i, m).assign(y)
                }
            }
        }
    }
}
