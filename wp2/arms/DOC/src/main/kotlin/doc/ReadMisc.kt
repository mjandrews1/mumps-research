package doc

// Unit 64 (B3 tranche 1): compiler-read.c::<misc> (batch 3, simple
// stratum; LOC 30, score 5.2). The header-residue chunk: the May 15,
// 2024 banner plus four includes; the sole function read_code is a
// batch-6 corpus unit (units.csv row 160) and is NOT rendered here.
// Vendor quirk kept: the banner is byte-identical to compiler-epi.c's
// except the date line (a template quirk, kept, not fixed).
// Declaration-only ledger, no executable code (EpiMisc sibling).

private const val READ_BANNER_DATE = "May 15, 2024"

// include ledger (vendor residual chunk):
//   stdio.h                       JVM console/file IO, per-unit imports
//   mumpsc/memsize.h              the memsize constants ride their
//                                 owning units (NOTABS memsize.h:43
//                                 rode the Readf batch-3 row below)
//   mumpsc/compiler-macros.h      macro expansions inline at owners
//   mumpsc/compiler-globals-ext.h the CompilerState object carrier
//                                 (H-004)

private val includeLedger64 = listOf(
    "stdio",
    "memsize.h",
    "compiler-macros.h",
    "compiler-globals-ext.h",
)
