package doc

// Unit 3 (B1 tranche 1): compiler-open.c::<misc> (batch 1, simple
// stratum; LOC 30, score 5.2). The residual ledger: GPL banner (May 6,
// 2024 date line kept verbatim) and the four-include residue; the
// mumps_open() body is not a selected corpus unit (its rendered
// pieces, if selected elsewhere, live in their own files).
// Declaration-only ledger, no executable code.

private const val BANNER_3 = """
    Mumps Bioinformatics Software Library
    Copyright (C) 2003, 2004, 2008, 2015, 2017,
    2022, 2024 by Kevin C. O'Kane
    May 6, 2024
    GPL v2 or later, no warranty (full text in vendor source).
    """

// include ledger (vendor residual chunk):
//   stdio.h                      JVM console/file IO, per-unit imports
//   mumpsc/memsize.h             carrier constants (Carriers.kt)
//   mumpsc/compiler-macros.h     per-unit Kotlin carriers, no macros needed
//   mumpsc/compiler-globals-ext.h  compiler-side externs are owned by the
//                                 units that use them (H-004)

private val includeLedger3 = listOf("stdio", "memsize", "compiler-macros", "compiler-globals-ext")
