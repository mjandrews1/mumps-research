# RENDER-CLOSE PLAN (C-019, Option A) - 2026-11-27

Objective: close the DAF and BOTH coverage gaps found by the C-018
audit and corrected by the C-020 erratum (amended ledger:
wp2/analysis/data/daf_both_coverage_audit.csv, v2). Corrected gap
counts: DAF 60 (32 stub-only + 28 no-render), BOTH 99 (51 stub-only
+ 48 no-render). Then re-audit to 308/308 per arm, then resume S1.

Discipline: C-011 batch render rules (roster-keyed, journal close
records per batch, gradle gates on the m1-build study machine, h-rule
consults, fragment + splice journal flow). Stub-only rows complete
the existing seat body in place; no-render rows get fresh renders,
residue seat files for TU residues.

## Session R1: DAF stub-completions (32 units)

Rows 5, 6, 9, 10, 11, 12, 16, 23, 26, 43, 47, 50, 55, 77, 84, 90
(16 original C-018 rows) PLUS C-020 additions 3, 7 (patrn1Daf), 13
(numcompDaf), 17 (gotamatchD), 22 (expandNextDaf), 27 (findDaf), 28
(encodeHtmlDaf), 34 (swD), 36 (inoutDaf), 37 (prnterrDaf), 42
(global::Multiply member), 49 (Multiply char* wrapper), 51 (global::Min
member), 86 (mpsAtanD), 89 (maxW), 201 (Multiply MSV dispatcher).
Seats exist: Interpret.kt, CompilerParseC.kt, InterpService.kt,
RpmParserStubsD.kt, DafInterpStubsT3.kt, Zfcns.kt, GlobalNodeStub.kt.
Replace each TODO stub body with the real vendor body; keep the owner
pins.

R1 PROGRESS (2026-11-27): rows 23, 55, 84, 90 (mps_acos, mps_exp10,
mps_justify, mps_sqrt) complete, compile gate passed (kotlinc 2.1.20,
corretto JDK17, no new errors); call sites folded in Zfcns.kt,
ZfcnBifs.kt, Fcn.kt; stubs deleted with release notes. 28 rows remain.

## Session R2: DAF no-render part 1 (14 units, B1/B2/B3 slices)

Rows 4, 8, 14, 21, 25, 30, 31, 32, 35, 38, 39, 40 PLUS C-020
additions 63 (global::Extract), 65 (patrn), 69 (patrn). Mixed header
residues (numSetup.h, libmpsrdbms.h, arith.h, btree.h), compiler TU
residues (interp-sysfunc, interp-interp, mumps.cpp, compiler-if,
compiler-open, compiler-copyright-notice) and libmpscpp helpers
(TreePrint).

## Session R3: DAF no-render part 2 (13 units)

Rows 59, 60, 67, 68, 71, 75, 76, 81, 83, 93, 94, 252 PLUS C-020
addition 70 (errmod_). Includes the remaining compiler residues
(abrtx, doty, epi, read), interp residues (arith, parse, lock),
globalClass.h/cppsysfunc.h residues, and libmpscpp helpers
(bmg_fsearch, RunMumps, Cost) plus interp-native-btree residue.

## Session R4: BOTH stub-completions (50+ units)

Rows 6, 9, 10, 12, 15, 16, 26, 37, 41, 47, 48, 50, 66, 67, 70, 74,
77, 90, 96, 97, 101, 102, 104, 112, 225 (25 original C-018 rows)
PLUS C-020 additions 3, 5 (symFreeZb), 7 (patrn1), 13 (numcompZb),
17 (gotamatch), 19 (termCorrelateSvcZb), 22 (ExpandNext), 27 (_find),
28 (encodeHtmlC), 34 (sw), 36 (WriteCode inout), 42 (GlobalRef
multiply), 45 (scanblocks), 51, 120 (minSvcZb), 58 (killC), 64
(parse_), 72 (Cosine), 79 (readf_Zb), 89 (maxSvcZb), 95 (BothCRuntime
fcn), 105 (parse_pattern_element), 106 (centroidSvcZb), 109 (atTest),
122 (escan), 248 (CompilerParseMain parse_). Seats: CompilerMain.kt,
CompilerParseMain.kt, ReadCode.kt, Interpret.kt, InterpService.kt,
Cannon.kt, GtkTreeAdd.kt, Piece.kt, BtreeFreadx.kt, BifsZfcn.kt,
CompilerLocalArrayMiscB5.kt, BothStubs.kt, LibmpscppMisc6.kt,
BothCRuntime.kt. GtkTreeAdd (74/225) is the GTK platform edge:
complete per the h-rule ruling used for its render body (the TODO
cites JVM unreachability; the close needs the ruled fold or a
documented platform-exemption consult, PI to confirm at that seat).

## Session R5: BOTH no-render part 1 (16 units)

Rows 4, 8, 14, 20, 21, 23, 25, 30, 31, 32, 38, 39, 40 PLUS C-020
additions 35 (TreePrint), 61 (SQL_Table), 63 (global::Extract).
Mirrors the DAF shared-gap slice plus GlobalName and mps_acos.

## Session R6: BOTH no-render part 2 (14 units)

Rows 43, 53, 55, 59, 60, 68, 71, 76, 81, 83, 93, 94 PLUS C-020
additions 65, 69 (patrn), 73 (compiler-sys1.c cannon). Includes
C-014's scheduled-but-unlanded renders (embed 43, mstring::Eval 77
seat neighborhood), globalClass.h/cppsysfunc.h residues, and the
shared compiler residues.

## Session R7: BOTH no-render part 3 (17 units)

Rows 99, 100, 107, 108, 111, 113, 115, 116, 117, 121, 123, 124 PLUS
C-020 additions 75 (RunMumps), 84 (mps_justify), 86 (mps_atan), 103
(interp-parse.cpp readf_), 110 (libmpscpp.cpp init). Includes
GlobalQuery, pcre.h residue, _fnumber, interp-bifs residue,
sysfunc.h residue, LocalName, mps_asin, rdbms-if/server residues,
mstring::Fnumber seats, globalarray/new residues.

## Close-out

After R7: re-run the C-018 classifier with the C-020 claim-based
verification over both arms, expect 308/308 rendered per arm, commit
the re-audit ledger as data (C-021), then resume S1 over the full
roster.
