package doc

// Unit 1 (B1 tranche 1): compiler-copyright-notice.c::<misc> (batch 1,
// simple stratum; LOC 29, score 5.1). The residual ledger: GPL banner
// (May 26, 2024 date line kept verbatim) and the three-include residue;
// copyright_notice() itself is the batch-2 corpus unit (CopyrightNotice.kt,
// unit 51). Declaration-only ledger, no executable code.

private const val BANNER_1 = """
    Mumps Bioinformatics Software Library
    Copyright (C) 2003, 2004, 2008, 2015, 2017,
    2022, 2024 by Kevin C. O'Kane
    May 26, 2024
    GPL v2 or later, no warranty (full text in vendor source).
    """

// include ledger (vendor residual chunk):
//   stdio.h                      JVM console/file IO, per-unit imports
//   time.h                       the ctime() stamp inside copyright_notice
//                                (the function render's concern; here a
//                                ledger note - the misc chunk has no use)
//   mumpsc/compiler-macros.h     per-unit Kotlin carriers, no macros needed

private val includeLedger1 = listOf("stdio", "time", "compiler-macros")
