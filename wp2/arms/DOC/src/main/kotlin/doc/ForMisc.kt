package doc

// Unit 124 (B5 tranche 1): compiler-for.c::<misc> (batch 5, simple
// stratum; LOC 30, score 5.2). The unit is the residual header banner
// and include set of compiler-for.c; the function body (for_code) is
// the separate batch-4 corpus unit (unit 122). Rendered as a
// declaration-only ledger per the unit-35/36 banner precedent: banner
// kept verbatim, each include mapped to its carrier, no executable
// code (nothing to translate). Vendor date line kept: June 16, 2024.

private const val BANNER_124 = """
    Mumps Bioinformatics Software Library
    Copyright (C) 2003, 2004, 2008, 2015, 2017,
    2022, 2024 by Kevin C. O'Kane

    Kevin C. O'Kane, kc.okane@gmail.com, okane@uni.edu
    https://threadsafebooks.com/
    https://www.cs.uni.edu/~okane
    GPL v2 or later, no warranty (full text in vendor source).
    """

// include ledger (vendor residual includes of compiler-for.c):
//   stdio.h              JVM console/file IO, per-unit imports
//   mumpsc/memsize.h     carrier constants (Carriers.kt; e.g. STR_MAX)
//   mumpsc/compiler-macros.h  per-unit Kotlin carriers, no macros needed
//   mumpsc/compiler-globals-ext.h  compiler-side externs are owned by
//                        the units that use them (H-004 one owner per
//                        entity; no property is added by this ledger)

private val includeLedger124 = listOf("stdio", "memsize", "compiler-macros", "compiler-globals-ext")
