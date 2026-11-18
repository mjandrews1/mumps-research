package mumps.mdh

// ----------------------------------------------------------------------------
// PRO-arm translation of include/mumpsc/builtinTable.h::<misc>
// (roster row 247, batch 9 OPENING ROW; units.csv: loc 20,
// comment_ratio 0.048, pointer_marks 0, control_marks 0, score 2.0,
// rank 1 - the lowest-score unit in the study). Translated
// independently from the pristine vendor source
// (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe). The header has NO
// extracted functions (residual by identity, u168/u171 precedent):
// the whole physical unit is the static const char* BuiltIn[] string
// table - the built-in intrinsic NAME table the scanner matches
// against at byte level ("^Cosine" .. "^Input", vendor terminator
// "" at :19-20).
//
// Rules applied (wp2/arms/PRO/rules_v0.pl):
//  - h004 static_mutable     : the file-scope static array becomes a
//    top-level const val (immutable table, the C static-const
//    analog; GC-backed, u44 banner precedent).
//  - h012 unsigned_byte_domain: the C strings fold onto the Kotlin
//    String carrier (unit-39 char-star-to-String precedent); the
//    caret prefix is the vendor's SOH-convention marker byte, kept
//    verbatim in the literals.
//  - h015 grammar_collision  : BuiltIn keeps the vendor name with
//    the capital-B capital-I spelling; the trailing empty-string
//    terminator element kept (the C sentinel arm scans with 0 ==
//    strcmp on the table walk).
//  - h017 naming              : BuiltIn keeps the vendor name.
//
// Vendor notes (translated faithfully, not "fixed"):
//  - The table is the builtin.h (u224) roster's runtime counterpart:
//    every seat named here has a rendered seat (Cosine pilot, Avg
//    u266, Count u118, Max u169, Min u44, Multiply u53, Sum u182,
//    Transpose, IDF u240, TermCorrelate, DocCorrelate, Sim1 u199,
//    Dice u165, Jaccard row 255, Centroid, Scan, ScanAlnum u113,
//    Input).
// ----------------------------------------------------------------------------

/** vendor builtinTable.h:1-20 - static const char* BuiltIn[] (name table). */
val BuiltIn = arrayOf(
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
    "" // vendor :19 - the empty-string terminator sentinel
)
