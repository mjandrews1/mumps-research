package doc

// Unit 36: compiler-doty.c::<misc> (batch 2, simple stratum). Same shape
// as unit 35: the GPL banner (with the June 11 2024 date kept verbatim)
// plus the four-include residue; doty() itself is the batch-4 corpus unit.
// Declaration-only ledger, no executable code.

private const val BANNER_36 = """
    Mumps Bioinformatics Software Library
    Copyright (C) 2003, 2004, 2008, 2015, 2017,
    2022, 2024 by Kevin C. O'Kane
    June 11, 2024
    GPL v2 or later, no warranty (full text in vendor source).
    """

// include ledger (vendor residual chunk):
//   stdio.h                   JVM console/file IO, per-unit imports
//   mumpsc/memsize.h          carrier constants (Carriers.kt)
//   mumpsc/compiler-macros.h  per-unit Kotlin carriers, no macros needed
//   mumpsc/compiler-globals-ext.h  owned by the units that use them (H-004)

private val includeLedger36 = listOf("stdio", "memsize", "compiler-macros", "compiler-globals-ext")
