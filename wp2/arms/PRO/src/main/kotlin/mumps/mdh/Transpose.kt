package mumps.mdh

/**
 * PRO-arm translation of libmpscpp.cpp::Transpose (B4 unit 117,
 * rank 75). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 2113-2134.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::Transpose, cpp, loc 15,
 * comment_ratio 0.062, pointer_marks 0, control_marks 4, score
 * 8.5 - exact match to wp1/study-subset/units.csv row 118; the
 * score disambiguates the string overload from the sibling
 * char* (score 8.0) and mstring (8.4) forwarders, which forward
 * to this shape and are NOT separate corpus rows of this batch.
 *
 * Transpose(string, string): build the two named globals, then
 * for every first-level subscript aa of A and second-level bb
 * under it, write A(aa,bb)'s value at B(bb,aa) - the matrix
 * transpose.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h014 out_of_scope_call  : the Global ctor, the vararg
 *    invoke, Order, and the read/assign surfaces ride the
 *    u31/u14 carriers (Order/assign are themselves owner-named
 *    h014 stubs of the class-global machinery); the free MSV
 *    wrapper (libmpscpp.cpp:10316, loc 23, not a corpus unit)
 *    is NOT translated here - roster note only.
 *  - h015 grammar_collision  : pre-push scan clean; the top-level
 *    name Transpose is new (no prior public/private conflict).
 *  - h017 naming             : Transpose keeps the vendor
 *    spelling (PascalCase in the vendor source).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - "B(bb,aa)=A(aa,bb);" is the global-from-global assignment:
 *    read the source value through the numeric/string read
 *    surface and assign into the target reference (toDouble +
 *    assign, the u31 folding conventions - documented, not
 *    silently changed).
 *  - The vendor writes "bb=\"\";" TWICE at the top of the outer
 *    loop body - the second is a dead store (bb is re-assigned
 *    before any read); the first is kept, the duplicate dropped
 *    with this note (h013).
 *  - The string/mstring forwarders fold into this one Kotlin
 *    signature per the Sw.kt wrapper convention (roster note).
 */
fun Transpose(a: String, b: String) {
    val A = Global(a)
    val B = Global(b)
    var aa = ""
    var bb = ""

    while (true) {
        aa = A(aa).Order(1)

        if (aa == "") break

        bb = "" // (the vendor's duplicate dead store dropped)

        while (true) {
            bb = A(aa, bb).Order(1)

            if (bb == "") break

            // vendor: B(bb,aa)=A(aa,bb);
            B(bb, aa).assign(A(aa, bb).toDouble())
        }
    }
}
