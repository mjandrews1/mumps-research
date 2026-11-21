package both

// Unit 133 (BOTH B5 T1, simple): libmpscpp.cpp::global::Length (23
// LOC). Batch_plan ordinal 133: simple, score 9.9. The Length twin of
// unit 129 with the receiver role inverted: the OWN global is
// retrieved into gtmp (the value scanned), the NEEDLE global into
// gtmp1 (the delimiter searched for).
//
// Vendor behavior: two Mglobal(RETRIEVE) walks, each failing -> set
// GlobalExceptionCode=GLOBAL_NOT_FOUND and throw MumpsGlobalException
// (note the vendor sets the same code even when the SECOND retrieval
// fails after the first succeeded - the first value is then discarded
// by the throw); then count pieces of the own value split by the
// needle value: 1 + non-overlapping leftmost matches; empty own value
// -> 0.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused (same classification as unit 129: both
//     buffers are Mglobal out-buffer roles -> returned Strings).
//   h014 out_of_scope_call - reused (Mglobal/exception seats, roster
//     167 / 305; global-class layout owner roster 89).
//   h017 naming - reused.
//   MISS class_member_carrier - see BothStubs.kt ledger.
//   VENDOR QUIRK (ledgered, kept): empty needle value -> vendor
//     non-termination, byte-faithful here (see unit 129).
//   Dafny: shares spec/length_count.dfy with unit 129 (the same
//     piece-count scan specified once for the twins).

internal fun globalLength(ref: String, needle: GlobalRef): Int {
    val gtmp = mglobalRetrieve(ref) // own value
    val gtmp1 = mglobalRetrieve(needle.ref) // needle value

    if (gtmp.isEmpty()) return 0

    var i = 1
    var cursor = 0
    while (true) {
        val idx = gtmp.indexOf(gtmp1, cursor)
        if (idx == -1) return i
        i++
        cursor = idx + gtmp1.length
    }
}
