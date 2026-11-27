package both

// Unit 246 render: include/mumpsc/builtinTable.h::<misc> (roster
// 246, batch 8, 20 LOC; the data table include ledger).
// static const char * BuiltIn[] - the ^-prefixed built-in
// dispatch table, the ServiceInterface name roster. Data table
// render (no bodies; the ExternsHMisc/allocSvZc ledger
// precedents; the trailing "" sentinel terminates the walk).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h004 include_only - reused: the unit is a const data table
//     with no behavior; the entries keep the vendor order and
//     spelling exactly (the dispatch walk uppercases its probe
//     against these names).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline; the ^ prefix
//     rides the string literals (no template hazard).
//   h017 naming - reused: BuiltIn keeps the vendor name and
//     case (no Kotlin collision; the CharArray/IntArray seats
//     nearby are distinct).
//
// Quirks kept (ledgered):
//   - the trailing "" sentinel is the terminator entry (kept;
//     the C idiom rides the array size, the Kotlin seat keeps
//     the empty-string element).
//   - the ^IDF and ^Sim1 names collide with the record 239/198
//     service faces by design (the dispatch table is their pin
//     source; the InterpService idfSvcZb/sim1SvcZb seats).

internal val BuiltIn = arrayOf(
    "^Cosine",
    "^Avg",
    "^Count",
    "^Max",
    "^Min",
    "^Multiply",
    "^Sum",
    "^Transpose",
    "^IDF",
    "^TermCorrelate",
    "^DocCorrelate",
    "^Sim1",
    "^Dice",
    "^Jaccard",
    "^Centroid",
    "^Scan",
    "^ScanAlnum",
    "^Input",
    "",
)
