# BOTH B7 close (2026-11-27)

BOTH B7 tranche (units 186-215, 30 units in the committed row
order: ServiceInterface, misc-6, epi, match, sigint, zcommands_code,
freadx, Qsub, fcninit, mumps-misc, embed1, Extract, Sim1,
rslt_callback, Multiply, fcn-misc, MakeGlobal, sysfunc-GlobalData,
externs-misc, Fnumber, mkpt, _justify, libmpscpp-GlobalData, Soundex,
Translate, strmanip-substr, sys1-substr, Piece, zcommands-misc, Find)
translated 2026-11-27: thirty units pushed, whole-arm kotlinc gate
EXIT=0 at 90 files (60 master + 30 renders), 0 errors, hygiene clean.

Seat releases with notes: sim1SvcZb (Sim1 owns kernel+face per the
Sim1W adjudication: corpus seat kernel :1565, face :10431 wrapper;
the unit-186 ServiceInterface pin corrected), makeGlobalZb,
globalDataC (BothStubs fold), fcninitZb, EPZb, substr stub (the
realloc-arm kernel 211 is DISTINCT from the 255-clamp kernel 212:
separate kernels, not an identical-body fold), sigint stub,
BifsZfcn stub (unit 186 release-at-render). New TODO stubs raised:
_pieceZb (owner interp-piece.cpp misc, roster 103 b4),
GlobalNode.data() (MultiplyString, roster 259 family), zero-arg
GlobalRef.invoke() placeholder (MultiplyString).

Duplicate-body folds disclosed: matchSys1 (match delegates to
Match), Multiply wrapper. Quantizations disclosed: mkpt static
residue kept, embed1 alias, zcommands Sysout %d UB, btree NPE/stale,
ReadLine EOF UB (carried from misc-6).

Session-span disclosure: the batch spanned multiple resume turns
(sandbox wiped 11 times total); continuation consult rows ride the
2026-11-27 session stamp per the C-011 one-session discipline
(32 rows on the nominal 11-26 19:30:00Z band, 94 rows on the
11-27 16:00:00Z continuation band; events CSV 503 -> 629 lines).

Cosmetic rank-pin mislabels in stub comments (sigint 254->190,
parse_ 248->247, Extract 171->17, multiply 213->41, Mltoa 292->291,
fcn 123->94, substr 212->211) NOT renumbered pending PI note.

Next protocol step: DAF supplementary render session (four stub
rows per the 2026-11-26 PI adjudication: if_code r41, new_code r65,
copyright_notice r47, abrtx), then BOTH batch 8. C-001 cycle-10
check backfill still owed study-wide.
