package doc

// Unit 63 (B3 tranche 1): compiler-epi.c::<misc> (batch 3, simple
// stratum; LOC 30, score 5.2). The header-residue chunk: the June 16,
// 2024 banner plus four includes; the sole function epi is a batch-7
// corpus unit (units.csv row 189) and is NOT rendered here.
// Declaration-only ledger, no executable code (CopyrightMisc
// precedent, B1 unit 1).

private const val EPI_BANNER_DATE = "June 16, 2024"

// include ledger (vendor residual chunk):
//   stdio.h                       JVM console/file IO, per-unit imports
//   mumpsc/memsize.h              the memsize constants ride their
//                                 owning units (stateVector sizes,
//                                 CompilerState seats)
//   mumpsc/compiler-macros.h      macro expansions inline at their
//                                 owners (Sysout -> CompilerState.emit,
//                                 H-002)
//   mumpsc/compiler-globals-ext.h the CompilerState object carrier
//                                 (H-004)

private val includeLedger63 = listOf(
    "stdio",
    "memsize.h",
    "compiler-macros.h",
    "compiler-globals-ext.h",
)
