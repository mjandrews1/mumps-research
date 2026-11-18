package mumps.mdh

/**
 * PRO-arm translation of libmpscpp.cpp::global::DocCorrelate
 * (roster row 288, batch 10; units.csv: loc 41, comment_ratio
 * 0.024, pointer_marks 10, control_marks 14, score 42.3, rank
 * 225). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source: the complete void global::DocCorrelate(global B,
 * string fcn, double threshold) body (vendor 1215 - distinct
 * from the misc-5 char-star/mstring wrapper folds at vendor
 * 1203-1213, LibmpscppMisc5.kt). This unit RESOLVES the
 * MdhStubs docCorrelate stub raised at u60 (roster row 288; stub
 * removed, cleanLocks precedent); the Misc5 wrapper delegation
 * binds this seat unchanged.
 *
 * DocCorrelate (member of class global, this = document side)
 * walks this's document space pairwise, scores each doc pair
 * with the chosen coefficient (Cosine/Sim1/Dice/Jaccard), and
 * stores the symmetric correlation into B when above threshold.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h010 numeric_narrowing : none - the threshold compare is
 *    double-native.
 *  - h014 out_of_scope_call : this(doc1)/this(doc2)/B(doc1,doc2)
 *    GlobalRef machinery (operator()/Order/assign/Kill seats,
 *    the TermCorrelate unit-14 precedent); Cosine/Sim1/Dice/
 *    Jaccard are the DONE units 71/72/73/74 - direct calls.
 *  - h015 grammar_collision : the subscripted-reference argument
 *    form (*this)(doc1) mirrors the unit-282 LibmpscppMisc9 fold:
 *    Global(myname + SOH + subscript) (the MakeGlobal machine-
 *    reference shape).
 *  - h017 naming            : the member seat keeps the stub''s
 *    docCorrelate fold (the DocCorrelate(Global,String,Double)
 *    Kotlin name is owned by the misc-5 wrapper pair; lowercase
 *    fold, h015); emitted as an extension (TermCorrelate
 *    precedent) so the Global carrier stays minimal.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The fcn ladder's final else s=1 (the unknown-name fallback
 *    is the Cosine arm) - kept.
 *  - c is uninitialized in the vendor when the switch misses; the
 *    four-case switch cannot miss (the ladder forces 1..4), the
 *    Kotlin c pre-declares 0.0 as the never-read initial - noted.
 *  - The s local and doc1 self-subscripting walk read exactly as
 *    the vendor: doc1=(string)(*this)(doc1).Order(1) - kept.
 */

private val SOH = 1.toChar() // the vendor's x01 subscript separator byte

/**
 * vendor 1215: global::DocCorrelate(global B, string fcn, double
 * threshold) - the member body (this = A side); the Kotlin name
 * keeps the raised stub's docCorrelate fold (h017/h015 note in the
 * header).
 */
fun Global.docCorrelate(B: Global, fcn: String, threshold: Double) {
    var doc2: String
    var c: Double
    val s: Int

    if (fcn == "Cosine") s = 1
    else if (fcn == "Sim1") s = 2
    else if (fcn == "Dice") s = 3
    else if (fcn == "Jaccard") s = 4
    else s = 1

    B().Kill()
    var doc1 = ""

    while (true) {
        doc1 = this(doc1).Order(1)

        if (doc1 == "") break

        doc2 = doc1

        while (true) {
            doc2 = this(doc2).Order(1)

            if (doc2 == "") break

            c = 0.0 // vendor: uninitialized; the switch cannot miss (h013)
            when (s) {
                1 -> c = Cosine(
                    Global(this.myname + SOH + doc1),
                    Global(this.myname + SOH + doc2)
                )

                2 -> c = Sim1(
                    Global(this.myname + SOH + doc1),
                    Global(this.myname + SOH + doc2)
                )

                3 -> c = Dice(
                    Global(this.myname + SOH + doc1),
                    Global(this.myname + SOH + doc2)
                )

                4 -> c = Jaccard(
                    Global(this.myname + SOH + doc1),
                    Global(this.myname + SOH + doc2)
                )
            }

            if (c > threshold) {
                B(doc1, doc2).assign(c)
                B(doc2, doc1).assign(c)
            }
        }
    }
}
