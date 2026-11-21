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
}

// sigint(int) - owner interp-sysfunc.cpp::sigint (roster batch-7 unit for
// the DOC cycle; named owner stub; Transpose calls it with code 100).
fun sigintDaf(code: Int): Unit =
    throw NotImplementedError(
        "sigint: out-of-scope owner (interp-sysfunc.cpp::sigint, roster unit 211)")
