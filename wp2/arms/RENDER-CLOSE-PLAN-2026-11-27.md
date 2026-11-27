# RENDER-CLOSE PLAN (C-019, Option A) - 2026-11-27

Objective: close the 40 DAF and 62 BOTH coverage gaps found by the
C-018 audit (ledger: wp2/analysis/data/daf_both_coverage_audit.csv),
then re-audit to 308/308 per arm, then resume S1.

Discipline: C-011 batch render rules (roster-keyed, journal close
records per batch, gradle gates on the m1-build study machine, h-rule
consults, fragment + splice journal flow). Stub-only rows complete
the existing seat body in place; no-render rows get fresh renders,
residue seat files for TU residues.

## Session R1: DAF stub-completions (16 units)

Rows 5, 6, 9, 10, 11, 12, 16, 23, 26, 43, 47, 50, 55, 77, 84, 90.
Seats exist: Interpret.kt, CompilerParseC.kt, InterpService.kt,
RpmParserStubsD.kt, DafInterpStubsT3.kt, Zfcns.kt. Replace each TODO
stub body with the real vendor body; keep the owner pins.

## Session R2: DAF no-render part 1 (12 units, B1/B2/B3 slices)

Rows 4, 8, 14, 21, 25, 30, 31, 32, 35, 38, 39, 40. Mixed header
residues (numSetup.h, libmpsrdbms.h, arith.h, btree.h), compiler TU
residues (interp-sysfunc, interp-interp, mumps.cpp, compiler-if,
compiler-open, compiler-copyright-notice) and libmpscpp helpers
(TreePrint).

## Session R3: DAF no-render part 2 (12 units)

Rows 59, 60, 67, 68, 71, 75, 76, 81, 83, 93, 94, 252. Includes the
remaining compiler residues (abrtx, doty, epi, read), interp residues
(arith, parse, lock), globalClass.h/cppsysfunc.h residues, and
libmpscpp helpers (bmg_fsearch, RunMumps, Cost) plus
interp-native-btree residue.

## Session R4: BOTH stub-completions (25 units)

Rows 6, 9, 10, 12, 15, 16, 26, 37, 41, 47, 48, 50, 66, 67, 70, 74,
77, 90, 96, 97, 101, 102, 104, 112, 225. Seats: CompilerMain.kt,
CompilerParseMain.kt, ReadCode.kt, Interpret.kt, InterpService.kt,
Cannon.kt, GtkTreeAdd.kt, Piece.kt, BtreeFreadx.kt, BifsZfcn.kt,
CompilerLocalArrayMiscB5.kt. GtkTreeAdd (74/225) is the GTK platform
edge: complete per the h-rule ruling used for its render body (the
TODO cites JVM unreachability; the close needs the ruled fold or a
documented platform-exemption consult, PI to confirm at that seat).

## Session R5: BOTH no-render part 1 (13 units)

Rows 4, 8, 14, 20, 21, 23, 25, 30, 31, 32, 38, 39, 40. Mirrors the
DAF shared-gap slice plus GlobalName and mps_acos.

## Session R6: BOTH no-render part 2 (12 units)

Rows 43, 53, 55, 59, 60, 68, 71, 76, 81, 83, 93, 94. Includes
C-014's scheduled-but-unlanded renders (embed 43, mstring::Eval 77
seat neighborhood), globalClass.h/cppsysfunc.h residues, and the
shared compiler residues.

## Session R7: BOTH no-render part 3 (12 units)

Rows 99, 100, 107, 108, 111, 113, 115, 116, 117, 121, 123, 124.
Includes GlobalQuery, pcre.h residue, _fnumber, interp-bifs residue,
sysfunc.h residue, LocalName, mps_asin, rdbms-if/server residues,
mstring::Fnumber seats, globalarray/new residues.

## Close-out

After R7: re-run the C-018 classifier over both arms, expect 308/308
rendered per arm, commit the re-audit ledger as data (C-020), then
resume S1 over the full roster.
