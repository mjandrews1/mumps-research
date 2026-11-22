package both

// Unit 158 (BOTH B6 T1, simple): libmpscpp.cpp::SmithWaterman (17
// LOC). Batch_plan ordinal 158: simple, LOC 17, score 7.7,
// complexity rank 58. Vendor signature
// "char * SmithWaterman(struct MSV *, char * ep, char * s1,
// char *s2, char *sa, char * sm, char * gap, char * mm,
// char * ma)" (libmpscpp.cpp:10351); the svPtr and ep parameters
// are unused in the body (dropped per the arm fold, journaled).
//
// Vendor behavior: s1/s2 are copied one byte late into the
// STR_MAX scratch carriers s1a/s2a (the byte-0 seat is seeded
// with a blank, the sequences follow); the five score-string
// args are sscanf-int parse seats (sa -> sax, sm -> smx,
// gap -> gapx, mm -> mmx, ma -> max); the alignment score is
// the sw() call over the padded carriers; the int is sprintf
// format seated into the static char tmp[32] and returned.
//
// Quirks kept (ledgered):
//   - the sw call passes the five parsed values by position into
//     a parameter list the vendor names ShowAlign, ShowMatrix,
//     gap, mismatch, match (libmpscpp.cpp:8153): the caller-side
//     sax/smx land on the show flags and gapx/mmx/max on the
//     penalty/match seats. The values are forwarded verbatim by
//     position; the seat-name mismatch is journaled, not fixed.
//   - sscanf failure (non-numeric score strings) leaves the C
//     locals unassigned (UB); quantized to the atolC quiet-parse
//     0 tail per the unit-154 pt precedent (i-quantization), the
//     divergence journaled.
//   - the STR_MAX strcatx overflow quirk of s1a/s2a cannot occur
//     in the String fold (no bounded carrier); noted.
//   - the static char tmp[32] return folds to a fresh String per
//     the unit-154 Hash/lhash h008 fold (no cross-call residue).
//
// BOTH consults (find_rule before render, events logged):
//   h008 static_local_state - reused and hit: static char tmp
//     folds to a fresh String return.
//   h014 out_of_scope_call - reused and hit: sw is a NEW stub
//     raised in BothStubs.kt naming its owner libmpscpp.cpp::sw,
//     roster 287, batch 2 pending (releases per the h014
//     contract when the owner lands); the sscanf seats ride the
//     internal atolC promotion (B6 T1 seat edit).
//   h017 naming - reused and hit: vendor name kept (per the
//     Dump/Restore/Stem house style); s1a/s2a padding kept.

internal fun SmithWaterman(s1: String, s2: String, sa: String, sm: String,
                          gap: String, mm: String, ma: String): String {
    val s1a = " " + s1 // byte-0 blank seed, then the sequence
    val s2a = " " + s2
    val sax = atolC(sa).toInt()
    val smx = atolC(sm).toInt()
    val gapx = atolC(gap).toInt()
    val mmx = atolC(mm).toInt()
    val max = atolC(ma).toInt()
    val score = sw(s1a, s2a, sax, smx, gapx, mmx, max)
    return score.toString() // sprintf("%d") on the fresh fold
}
