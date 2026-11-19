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

// TODO stub: inline int Score(char, char) at libmpscpp.cpp:8049 is owned
// by the sequence-alignment Score unit of a later batch; out-of-scope.
fun Score(a: Char, b: Char): Int =
    throw NotImplementedError("Score: out-of-scope owner (libmpscpp.cpp:8049)")


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
