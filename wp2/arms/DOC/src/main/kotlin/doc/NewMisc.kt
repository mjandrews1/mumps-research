package doc

// Unit 95 (B4 tranche 1): compiler-new.c::<misc> (batch 4, simple
// stratum; LOC 30, score 5.2). The file-residue chunk: the May 10,
// 2024 GPL banner plus the four-include block; the file's function
// new_code is the DONE batch-3 unit-91 render (NewCode.kt). Same
// EpiMisc shape as unit 94 (the banners differ only in the date line,
// which is kept verbatim per template quirk).

private const val NEW_BANNER_DATE = "May 10, 2024"

// include ledger (vendor residual chunk):
//   stdio.h                       JVM console/file IO, per-unit imports
//   mumpsc/memsize.h              the memsize constants ride their
//                                 owning units
//   mumpsc/compiler-macros.h      macro expansions inline at owners
//   mumpsc/compiler-globals-ext.h the CompilerState object carrier
//                                 (H-004)
private val includeLedger95 = listOf(
    "stdio",
    "memsize.h",
    "compiler-macros.h",
    "compiler-globals-ext.h",
)
