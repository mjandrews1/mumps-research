package mumps.mdh

/**
 * Kotlin translation of libmpscpp.cpp::global::TermCorrelate (Study Subset
 * unit 14, Batch 1, arm BASE; translated 2026-11-14).
 *
 * C: void global::TermCorrelate(global B) — walks this global's
 * doc/term/term hierarchy via Order() and accumulates symmetric term
 * correlation counts into B.
 */
fun MdhGlobal.termCorrelate(B: MdhGlobal) {

    B().kill() // C: B().Kill();

    var doc = ""

    while (true) {
        doc = this(doc).order(1) // C: doc = (string) (*this)(doc).Order(1);
        if (doc == "") break

        var term1 = ""
        while (true) {
            term1 = this(doc, term1).order(1)
            if (term1 == "") break

            var term2 = term1
            while (true) {
                term2 = this(doc, term2).order(1)
                if (term2 == "") break

                // C: if (xi = B(term1,term2).Data()) — assignment-in-condition
                // with xi never read afterwards (heuristic 55).
                if (B(term1, term2).data() != 0) {
                    // C: B(term1,term2)=B(term1,term2)+1; — mstring numeric add
                    B(term1, term2).value = (B(term1, term2).value.toInt() + 1).toString()
                    B(term2, term1).value = B(term1, term2).value
                } else {
                    B(term1, term2).value = "1"
                    B(term2, term1).value = "1"
                }
            }
        }
    }
}
