package mumps.mdh

/**
 * PRO-arm translation of libmpscpp.cpp::global::TermCorrelate (pilot
 * B1, unit 14). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 1270-1308.
 *
 * TermCorrelate (member of class global, this = A side) walks A's
 * document/subscript space and accumulates symmetric term co-occurrence
 * counts into B.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h010 numeric_promotion   : vendor's int xi = B(term1,term2).Data()
 *    truncates the double Data() return to int - explicit toInt().
 *  - h011 goto                : vendor's while(1)+break structure is
 *    already structured (no goto); kept as while(true)+break.
 *  - h014 out_of_scope_call   : the global subscript/Order/Data/Kill/
 *    assign machinery is owned by libmpscpp.cpp class global (not a
 *    B1 corpus unit) -> GlobalCarrier.kt owner-named stubs.
 *  - h017 naming              : TermCorrelate keeps the vendor name;
 *    emitted as an extension so the carrier stays minimal.
 */
fun Global.TermCorrelate(B: Global) {
    var doc: String

    B().Kill() // kill B

    doc = ""

    while (true) {
        doc = this(doc).Order(1)

        if (doc == "") break

        var term1 = ""

        while (true) {
            term1 = this(doc, term1).Order(1)

            if (term1 == "") break

            var term2 = term1

            while (true) {
                term2 = this(doc, term2).Order(1)

                if (term2 == "") break

                // h010: int xi = B(term1,term2).Data();
                val xi = B(term1, term2).Data().toInt()

                if (xi != 0) {
                    B(term1, term2).assign(B(term1, term2).toDouble() + 1)
                    B(term2, term1).assign(B(term1, term2).toDouble())
                } else {
                    B(term1, term2).assign(1.0)
                    B(term2, term1).assign(1.0)
                }
            }
        }
    }
}
