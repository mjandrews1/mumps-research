package both

// Unit 200 render: libmpscpp.cpp::Multiply (roster 200,
// batch 7, 26 LOC; units.csv rank 169). The FILE-LEVEL
// Multiply(string a, string b, string c), vendor :2211-2247:
// the matrix product C = A x B over global arrays (rows i,
// columns j; k = j pairs A's column with B's row; the inner
// m-walk accumulates A(i,j)*B(k,m) into C(i,m)).
//
// MULTI-ROW NAME (the Sim1W family, journaled): the vendor
// seats FOUR Multiply definitions - the member
// global::Multiply(B,C) :2164 (units.csv row line 42: unit
// 41, rank 213, batch 2, PENDING in this arm's committed
// order; the BothStubs GlobalRef.multiply TODO stub cites
// "roster unit 213" which is the RANK column - true unit 41;
// same rank/unit mislabel family, cosmetic fix queued), the
// string wrapper :2211 (THIS unit-200 corpus seat), the
// char* wrapper :2252 (same-body sibling, not a corpus row),
// and the service face char* Multiply(MSV*,...) :10193
// (rendered B5 unit 138, Multiply.kt - no Kotlin collision:
// the overload signatures differ). DUPLICATE-BODY NOTE: the
// string wrapper repeats the member body inline over A
// instead of (*this); when roster 41 lands, the same-body
// fold adjudicates (the Sim1W standing rule: the later
// landing render owns the member seat; this render keeps the
// file-level seat).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: the GlobalNode order/
//     kill/value/setValue/invoke lattice (layout owner
//     globalClass.h::<misc> roster 259, batch 3 pending -
//     the Transpose member precedent); NEW TODO stub
//     GlobalNode.data() (owner globalClass.h global::Data,
//     roster 259 family / the globalDataC engine; roster rows
//     76/78/79 per the Dice pin). NEW lattice seat
//     GlobalRef.invoke() zero-arg (owner global::operator()()
//     :2746, MakeGlobal(0,myname,"","") - the roster-202
//     family; placeholder per the sibling invoke seats).
//     fmtG/strtodC direct.
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: Multiply keeps the vendor name as a
//     String-triple overload (distinct from the B5 service
//     face's Msv overload); the FILE is MultiplyString.kt
//     (Multiply.kt is taken by the unit-138 face).
//   h019 class_member_carrier - reused: the receivers ride the
//     GlobalRef/GlobalNode carrier (the Transpose precedent).
//
// Quirks kept (ledgered):
//   - C().Kill() folds through the zero-arg invoke + the kill
//     stub (the Transpose B().Kill() fold precedent).
//   - the product fold: A(i,j)*B(k,m) is the mstring multiply
//     (atof x atof); the arm folds strtodC(value()) products
//     and the fmtG %g assignment format (the mstringCast
//     precedent; both arms assign through the same format).
//   - the else arm's double x/y locals are kept in shape.
//   - k=j then B(k)... : the k alias kept exact.

// global & global::operator()() - libmpscpp.cpp:2746:
// MakeGlobal(0, myname, "", "") (the bare-name self seat; the
// roster-202 MakeGlobal family). Placeholder per the sibling
// invoke seats (layout owner roster 259, pending).
internal operator fun GlobalRef.invoke(): GlobalNode {
    return GlobalNode()
}

// bool global::Data() - the GlobalNode carrier arm (layout
// owner globalClass.h::<misc> roster 259 family; the
// globalDataC engine, roster rows 76/78/79 per the Dice pin).
// TODO-stub (h014).
internal fun GlobalNode.data(): Boolean {
    TODO("stub: owner globalClass.h::<misc> global::Data (roster 259 family, batch 3 pending)")
}

// void Multiply(string a, string b, string c) - libmpscpp.cpp:2211
internal fun Multiply(a: String, b: String, c: String) {
    val A = GlobalRef(a)
    val B = GlobalRef(b)
    val C = GlobalRef(c)

    C.invoke().kill() // C().Kill()

    var i = ""
    while (true) {
        i = A.invoke(i).order(1) // each row
        if (i == "") break

        var j = ""
        while (true) {
            j = A.invoke(i, j).order(1) // each column
            if (j == "") break

            val k = j

            if (!B.invoke(k).data()) continue

            var m = ""
            while (true) {
                m = B.invoke(k, m).order(1)
                if (m == "") break

                // A(i,j) * B(k,m) - the mstring multiply fold
                val prod =
                    strtodC(A.invoke(i, j).value()) *
                        strtodC(B.invoke(k, m).value())

                if (!C.invoke(i, m).data()) {
                    // C(i,m) = A(i,j) * B(k,m)
                    C.invoke(i, m).setValue(fmtG(prod))
                } else {
                    // double x = A(i,j)*B(k,m); double y = C(i,m)+x;
                    // C(i,m) = y;
                    val x = prod
                    val y = strtodC(C.invoke(i, m).value()) + x
                    C.invoke(i, m).setValue(fmtG(y))
                }
            }
        }
    }
}
