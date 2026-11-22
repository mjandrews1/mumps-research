package both

// Unit 159 (BOTH B6 T1, simple): libmpscpp.cpp::global::Length
// (17 LOC) - the std::string overload. Batch_plan ordinal 159:
// simple, LOC 17, score 8.1, complexity rank 65. OVERLOAD
// DISAMBIGUATION (the units.csv duplicate-id ledger): the vendor
// file carries five global::Length overloads; the batch-5 rows
// already own the no-arg pair pieces via unit 129/133 (the
// 23-LOC global-needle twin at line 3274, rendered unit 133)
// and this batch's row is the string-needle overload at line
// 3330 (the two 17-LOC score-8.1 overloads are the mstring twin
// at line 3306 and this string twin; the file-stable score sort
// maps the mstring twin to the batch-10 rank-64 row and this
// string twin to the rank-65 batch-6 row; the 17-LOC score-7.7
// char-needle overload at line 3354 is the batch-10 rank-56
// row). Rendered row: global::Length(string needle), vendor
// lines 3330-3352.
//
// Vendor behavior: the OWN global is retrieved into gtmp, failing
// -> GlobalExceptionCode=GLOBAL_NOT_FOUND and throw
// MumpsGlobalException (the shared idiom); pieces of the own
// value split by the needle are counted: 1 + non-overlapping
// leftmost matches; empty own value -> 0. Same body shape as
// the unit-133 render (the twins differ only in the needle
// carrier); the piece-count scan is specified once in
// spec/length_count.dfy (shared, verified 0 errors).
//
// VENDOR QUIRK (ledgered, kept per units 129/133): an empty
// needle value makes the C strstr never return NULL after the
// first hit, so the vendor loop does not terminate; byte-faithful
// here (the Kotlin indexOf loop reproduces the same walk).
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused and hit: the Mglobal out-buffer
//     (gtmp) returns a String via the mglobalRetrieve seat.
//   h014 out_of_scope_call - reused and hit: the retrieval/
//     exception idiom rides the mglobalRetrieve seat (owner
//     roster 167).
//   h019 class_member_carrier - reused and hit: member of the
//     global class (layout owner roster 89, untranslated in
//     this arm); receiver state passed explicitly, string needle
//     carrier per the vendor overload; the fold name globalLength
//     overloads the unit-133 render (Kotlin overload pair).

internal fun globalLength(ref: String, needle: String): Int {
    val gtmp = mglobalRetrieve(ref) // the own value

    if (gtmp.isEmpty()) return 0

    var i = 1
    var cursor = 0
    while (true) {
        val idx = gtmp.indexOf(needle, cursor)
        if (idx == -1) return i
        i++
        cursor = idx + needle.length
    }
}
