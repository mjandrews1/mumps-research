package doc

// Externs of the libmpscpp.cpp sequence-alignment scope (vendor
// libmpscpp.cpp:8025-8035) used by unit 45 (prnt). One owner file per the
// cookbook one-owner-per-C-entity rule; the owning units (the alignment
// initializers of later batches) replace the initialization when they
// land.

// libmpscpp.cpp:8026
const val EMPTY_CELL = -32768

// libmpscpp.cpp:8031 short ** __a
var a: Array<IntArray>? = null

// libmpscpp.cpp:8032 std::string S_RESULT
var S_RESULT = ""

// inline int Score(char a, char b) at libmpscpp.cpp:8049. B10 T3
// FLIP (2026-11-22T06:21:00Z consult, hit-reuse): the raised
// forward-tag stub resolves onto the unit-301 LibmpscppMisc10.kt
// owner - the misc-8 chunk IS the sequence-alignment Score unit
// it waited for. The body rides the GAP/MATCH/MISMATCH file
// scope below.
fun Score(a: Char, b: Char): Int {
    if (a == '-' || b == '-') return GAP

    if (a == b) return MATCH

    return MISMATCH
}


// Same libmpscpp.cpp sequence-alignment file scope as EMPTY_CELL/__a/
// S_RESULT above: the gap/match/mismatch penalties set by sw, the
// sequence sizes + Size padding, and the sweep origin index sx.
// Owner: the alignment initializers (init/Cost seats ride Stubs).
var GAP = 0
var MISMATCH = 0
var MATCH = 0
var Size_s = 0
var Size_t = 0
var Size = 0
var sx = 0
