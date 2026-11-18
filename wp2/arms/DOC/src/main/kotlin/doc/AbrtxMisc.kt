package doc

// Unit 35: compiler-abrtx.c::<misc> (batch 2, simple stratum). The unit
// is the residual header banner and include set of compiler-abrtx.c; the
// function body (abrtx) is the separate batch-1 corpus unit. Rendered as a
// declaration-only ledger: banner kept verbatim, each include mapped to
// its carrier, no executable code (nothing to translate).

private const val BANNER_35 = """
    Mumps Bioinformatics Software Library
    Copyright (C) 2003, 2004, 2008, 2015, 2017,
    2022, 2024 by Kevin C. O'Kane

    Kevin C. O'Kane, kc.okane@gmail.com, okane@uni.edu
    https://threadsafebooks.com/
    https://www.cs.uni.edu/~okane
    GPL v2 or later, no warranty (full text in vendor source).
    """

// include ledger (vendor lines 29-32 of the residual chunk):
//   stdio.h              JVM console/file IO, per-unit imports
//   mumpsc/memsize.h     carrier constants (Carriers.kt; e.g. STR_MAX)
//   mumpsc/compiler-macros.h  per-unit Kotlin carriers, no macros needed
//   mumpsc/compiler-globals-ext.h  compiler-side externs are owned by the
//                        units that use them (H-004 one owner per entity)

private val includeLedger35 = listOf("stdio", "memsize", "compiler-macros", "compiler-globals-ext")
