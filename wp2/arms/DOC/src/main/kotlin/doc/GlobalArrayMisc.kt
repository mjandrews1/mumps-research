package doc

// Unit 94 (B4 tranche 1): compiler-globalarray.c::<misc> (batch 4, simple
// stratum; LOC 30, score 5.2). The file-residue chunk: the May 12, 2024
// GPL banner plus the four-include block; the file's sole function
// GlobalArray is the DONE batch-2 unit-52 render (GlobalArray.kt; its
// callers IfCode/Parse_/(B4) bind unchanged). Declaration-only ledger
// with no executable code (ReadMisc/EpiMisc sibling shape).
// Vendor quirk kept: the banner text is byte-identical to the other
// compiler banners except its date line (template quirk, kept).

private const val GLOBALARRAY_BANNER_DATE = "May 12, 2024"

// include ledger (vendor residual chunk):
//   stdio.h                       JVM console/file IO, per-unit imports
//   mumpsc/memsize.h              the memsize constants ride their
//                                 owning units (NOTABS memsize.h:43 rode
//                                 the batch-3 readf row)
//   mumpsc/compiler-macros.h      macro expansions inline at owners
//   mumpsc/compiler-globals-ext.h the CompilerState object carrier
//                                 (H-004)
private val includeLedger94 = listOf(
    "stdio",
    "memsize.h",
    "compiler-macros.h",
    "compiler-globals-ext.h",
)
