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

    fun min(): Double =
        throw NotImplementedError(
            "global.Min member: out-of-scope owner (libmpscpp.cpp::global::Min, roster batch-2 unit)")

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

    // Order(dir) - the $ORDER walk member; the (string) cast of the C++
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
    fun termCorrelate(b: MpsGlobalNode) {
        throw NotImplementedError(
            "global.TermCorrelate member: out-of-scope owner (libmpscpp.cpp global class TermCorrelate; kin of roster unit TermCorrelate)")
    }
}

// sigint(int) - owner interp-sysfunc.cpp::sigint (roster batch-7 unit for
// the DOC cycle; named owner stub; Transpose calls it with code 100).
fun sigintDaf(code: Int): Unit =
    throw NotImplementedError(
        "sigint: out-of-scope owner (interp-sysfunc.cpp::sigint, roster unit 211)")
