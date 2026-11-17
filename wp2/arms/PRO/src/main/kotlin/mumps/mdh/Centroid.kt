package mumps.mdh

/**
 * PRO-arm translation of libmpscpp.cpp::global::Centroid (B4 unit
 * 105, rank 177). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 2019-2050.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::global::Centroid, cpp, loc 30,
 * comment_ratio 0.032, pointer_marks 4, control_marks 7, score
 * 22.6 - exact match to wp1/study-subset/units.csv row 106.
 *
 * The MUMPS centroid-of-matrix walk over the subscripted global:
 * kill the target, then for every first- and second-level
 * subscript pair of this, accumulate the value into B with a
 * running count at the "1" subnode; the final pass divides each
 * accumulated B value by its count.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h010 numeric_promotion: the vendor's mixed int/double
 *    arithmetic (B(bb,"1")++ against double x) widens through the
 *    double domain on every accumulate and divide; each widening
 *    is explicit (toDouble() reads, Double assignments).
 *  - h012 unsigned_byte_domain: not triggered - no byte buffers
 *    in the unit (string subscripts ride Kotlin String).
 *  - h013 dead_store         : the vendor's "long i=0;" is never
 *    read - dropped with this note.
 *  - h014 out_of_scope_call  : the MdhStubs Global.Centroid stub
 *    raised by B4 unit 97 (wrapper owner) is RESOLVED by this
 *    unit and REMOVED (cleanLocks precedent, unit 56); the
 *    Order/Data/assign/invoke members ride the unit-31/14 Global
 *    and GlobalRef carriers (Order/Data/assign are themselves
 *    owner-named h014 stubs of the class-global machinery).
 *  - h015 grammar_collision  : pre-push scan clean - no dollar
 *    bytes, no comment-closer text.
 *  - h017 naming             : Centroid keeps the vendor
 *    spelling (PascalCase in the vendor source).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - "if (!B(bb).Data())" tests the MUMPS dollar-data code being
 *    0 (undefined); rendered as an explicit equality against 0.
 *  - "B(bb)=B(bb)+(*this)(aa,bb)" is the global+global sum; the
 *    C++ operator family folds through the numeric read/assign
 *    surface here (toDouble + assign), documented per the unit-31
 *    folding conventions.
 *  - "B(bb,"1")++" is the prefix increment of the count subnode -
 *    read-plus-1-assign.
 *  - The final divide pass reads the count, divides the value
 *    node, and leaves the "1" subnode untouched (vendor shape).
 *  - The second "bb=\"\";" reset inside the vendor loop body is a
 *    dead store (bb is re-assigned before use) - the first one is
 *    kept, the duplicate dropped with this note.
 */
fun Global.Centroid(B: Global) {

    var aa = ""
    var bb = ""

    B().Kill()

    while (true) {
        aa = this(aa).Order(1)

        if (aa == "") break

        bb = ""

        while (true) {
            bb = this(aa, bb).Order(1)

            if (bb == "") break

            if (B(bb).Data() == 0.0) { // vendor: if (!B(bb).Data())
                B(bb).assign(this(aa, bb).toDouble())
                B(bb, "1").assign(1.0)
            } else {
                B(bb).assign(B(bb).toDouble() + this(aa, bb).toDouble())
                B(bb, "1").assign(B(bb, "1").toDouble() + 1.0) // vendor: B(bb,"1")++
            }
        }
    }

    bb = ""

    while (true) {
        bb = B(bb).Order(1)

        if (bb == "") break

        val x = B(bb, "1").toDouble()
        B(bb).assign(B(bb).toDouble() / x)
    }
}
