package daf

// DAF global-node stub: the C++ "global" class of libmpscpp (the
// (unsigned char*)ref node with the vararg invoke and the tree members
// Count()/Min()/Transpose()) is machinery whose bodies live in roster
// units outside this tranche (or outside the corpus), so this tranche
// renders the call-shape the wrappers need, with per-member named
// owners (BASE-log rows 37/105 family: prototypes -> named-owner TODO
// stubs):
//   count()      - owner: libmpscpp.cpp global-class member (NOT a
//                  corpus unit; permanent stub unless a later batch
//                  renders the family)
//   min()        - owner: libmpscpp.cpp::global::Min (roster unit of
//                  batch 2; rendered when DAF reaches that batch cycle)
//   transpose()  - owner: libmpscpp.cpp global-class transpose member
//                  (not a corpus unit)
// The ctor takes the raw ref slice the vendor passes after the bypass
// (global a(&arg1[1]) bypasses the caret); the invoke takes the raw
// subscript slice (the vendor ctor parses the coded separators; the
// stub keeps the slice verbatim so the named-owner render can bind).

class MpsGlobalNode(val ref: String) {
    operator fun invoke(sub: String): MpsGlobalNode =
        throw NotImplementedError(
            "global node invoke: out-of-scope owner (libmpscpp.cpp global class ctor/invoke family)")

    fun count(): Double =
        throw NotImplementedError(
            "global.Count member: out-of-scope owner (libmpscpp.cpp global class)")

    // min() RELEASED (R1 stub-completion 2026-11-28, tranche 5):
    // roster row 51; real body at GlobalMin.kt (globalMinD, the
    // unit-171 Max sibling walk); the Min.kt unit-99 wrapper call
    // sites resolve here.
    fun min(): Double = globalMinD(this)

    fun transpose(b: MpsGlobalNode) {
        throw NotImplementedError(
            "global.Transpose member: out-of-scope owner (libmpscpp.cpp global class)")
    }

    // -- T2 seat additions (raised by the Centroid render, unit 112) --

    // Node data payload (BASE-log row 54 hit-reuse: "var value covers
    // operator="); the assignment forms B(bb)=X render as value stores.
    var value: String = ""

    // global() - the no-arg operator() fold (the B() of B().Kill()):
    // returns the receiver's bare sub-node shape; owner is the global
    // class ctor/invoke family (out of scope as for invoke(sub)).
    operator fun invoke(): MpsGlobalNode =
        throw NotImplementedError(
            "global node invoke (no-arg): out-of-scope owner (libmpscpp.cpp global class)")

    // Two-subscript invoke (Centroid's (*this)(aa,bb) form): the C++
    // multi-argument global ref; owner as above.
    operator fun invoke(a: String, b: String): MpsGlobalNode =
        throw NotImplementedError(
            "global node invoke (two-subscript): out-of-scope owner (libmpscpp.cpp global class)")

    // Order(dir) - the \u0024ORDER walk member; the (string) cast of the C++
    // call site is absorbed by the String return. Owner: the
    // libmpscpp.cpp global-class Order member (kin of the roster unit
    // libmpscpp.cpp::GlobalOrder, BASE-log row 98 overloads-collapse
    // precedent).
    fun order(dir: Int): String =
        throw NotImplementedError(
            "global.Order member: out-of-scope owner (libmpscpp.cpp global class Order; kin of roster unit GlobalOrder)")

    // Data() - the has-data probe member. Owner: the libmpscpp.cpp
    // global-class Data member.
    fun data(): Boolean =
        throw NotImplementedError(
            "global.Data member: out-of-scope owner (libmpscpp.cpp global class)")

    // Kill() - the kill member. Owner: the libmpscpp.cpp global-class
    // Kill member.
    fun kill() {
        throw NotImplementedError(
            "global.Kill member: out-of-scope owner (libmpscpp.cpp global class)")
    }

    // -- T3 seat addition (raised by the unit-120 libmpscpp.cpp::<misc-9>
    // TermCorrelate wrapper render) --

    // TermCorrelate(b) - the term-correlate member (row 105 node model).
    // Owner: libmpscpp.cpp global class TermCorrelate (kin of roster unit
    // libmpscpp.cpp::global::TermCorrelate, batch-1 core).

    // -- T1 seat addition (raised by the unit-130 libmpscpp.cpp::Avg
    // render, DAF B5) --

    // Avg(sub) - the average member (row 54/105 node model). The C++
    // a(sub).Avg() invoke folds onto the node + subscript shape.
    // RELEASED batch 9 unit 263 (roster row 263; libmpscpp.cpp::
    // global::Avg): real body in LibmpscppGlobalAvg.kt (the TAG
    // CORRECTION note there; the "not a corpus unit" blame is
    // superseded); the member walks the receiver ref, the sub
    // argument keeps the unit-130 ctor/invoke binding.
    fun avg(sub: String): Double = globalAvgD(this)

    // -- T5 seat additions (R1 stub-completion 2026-11-28, tranche 5) --

    // Max() - the max member: kin owner libmpscpp.cpp::global::Max
    // (roster row 170) RELEASED earlier at GlobalMax.kt
    // (globalMaxD); this seat binds the maxW wrapper reach (row
    // 89). The sv binding DafInterpState.svPtr per the Avg member
    // rule (the vendor member reads the TU svPtr).
    fun max(): Double = globalMaxD(this, DafInterpState.svPtr)

    // Multiply(b, c) - the matrix-multiply member (roster row 42):
    // RELEASED at GlobalMultiply.kt (globalMultiplyD, the
    // member form of the landed unit-141 walk); the multiplyW
    // dispatcher (row 49) reaches it as a.Multiply(b,c).
    fun multiply(b: MpsGlobalNode, c: MpsGlobalNode) {
        globalMultiplyD(this, b, c)
    }

    fun termCorrelate(b: MpsGlobalNode) {
        throw NotImplementedError(
            "global.TermCorrelate member: out-of-scope owner (libmpscpp.cpp global class TermCorrelate; kin of roster unit TermCorrelate)")
    }
}

// sigint(int) - owner interp-sysfunc.cpp::sigint (roster batch-7 unit for
// the DOC cycle; named owner stub; Transpose calls it with code 100).
// sigintDaf SEAT RELEASED (DAF B7 T3, unit 211): delegates to
// the real body at InterpSysfuncSigint.kt (sigintD); owner
// interp-sysfunc.cpp::sigint = roster unit 211 of THIS batch (the
// earlier DOC-cycle note and the StrmanipMisc ledger tag that
// read "batch 10" were wrong; StrmanipMisc corrected in the same
// push). Call sites resolve with the same (Int) code shape.
fun sigintDaf(code: Int): Unit = sigintD(code)

