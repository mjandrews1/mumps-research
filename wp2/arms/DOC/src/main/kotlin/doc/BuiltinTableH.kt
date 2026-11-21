package doc

// Unit 216 (B8 tranche 1): include/mumpsc/builtinTable.h::<misc>
// (batch 8, simple stratum; LOC 20, score 2.0, rank 1, the rank-top
// row of the batch). The builtin-table header residual: the static
// const BuiltIn[] roster of caret-prefixed intrinsic names the fcn
// machinery dispatches on (vendor builtinTable.h:10-28, physically
// verified this session; the trailing "" terminator is kept verbatim
// per H-013). Visibility is internal: the roster is read by the
// fcninit/ServiceInterface family (the batch-8+ interpreter-side
// rows), so no public seat is opened yet. Consult H-015 hit-adapt:
// the caret literals are legal Kotlin string bytes (no H-015 escape
// needed for the caret - the raw-dollar rule does not apply here),
// the array seat itself is the adaptation. Naming per H-017
// (BuiltIn -> BuiltInTable: the BuiltIn identifier is reserved here
// only by this ledger, the table suffix disambiguates against the
// builtin.h unit family of batch 10).

internal val BuiltInTable = arrayOf(
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
    ""
)
