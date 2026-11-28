package daf

// Unit 216 (DAF B8 T1, simple): include/mumpsc/builtinTable.h::<misc>
// (20 LOC, score 2.0): the builtin-table header residual: the static
// const BuiltIn[] roster of caret-prefixed intrinsic names the fcn
// machinery dispatches on (vendor builtinTable.h, physically
// verified this session from the pristine tarball; the trailing ""
// terminator is kept verbatim). The roster names the wrappers of the
// SV-bound dispatcher family: Cosine/Avg/Count/Max/Min/Multiply/Sum/
// Transpose/IDF are the wrapper rows (the RELEASED multiplyW/
// idfW/maxW seats - R1 tranche 5 landed multiplyW/maxW - and the
// landed CosineW/SumW/Count/Min/AvgD/Transpose seats), Scan/
// ScanAlnum/Input are the scan-seats family (scanSvD/scanAlnumSvD/
// inputSvD), TermCorrelate/DocCorrelate/Sim1/Dice/Jaccard/Centroid
// are the landed correlation members of LibmpscppMisc9 and kin.
// Consults (2026-11-24T21:10:00Z, nominal): the caret literals are
// legal Kotlin string bytes (the raw-dollar hygiene rule does not
// apply to the caret); visibility internal - the roster is dispatch
// data, no public seat opens beyond file scope (the banner residue
// is the unit; no function body exists in the chunk).
// Dafny: EXEMPT (declaration residue; journal row).

internal val BuiltInTableD = arrayOf(
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
