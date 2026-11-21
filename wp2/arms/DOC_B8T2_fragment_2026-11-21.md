# DOC arm journal - 2026-11-21 (batch 8, tranche 2)

DOC batch 8 tranche 2: the core stratum, 10 roster rows, DOC
units 227-236 (the units.csv rows are the FILE LINES 236 for
ReadLine down to 227 for the macros header; score order 10.7 up
to 24.8). All bodies derived from the pristine vendor
source this session (vendor tarball sha256 621deb...ffdbe); every
vendor anchor physically verified line-for-line before rendering.
Consult ledger (one row per unit per C-008; the events CSV append
is in this same push):

- libmpscpp.cpp::global::ReadLine (unit 227; LOC 21, score 10.7,
  rank 106): H-016, hit-adapt - VERIFICATION-ONLY row-note on the
  landed GlobalRef Reader fold (LibmpscppMisc6.kt): the istream
  overload at vendor :3039 verified line-for-line against the
  fold; the ONE divergence is the EOF arm, where the vendor stores
  the zeroed scratch (Mglobal(STORE) runs, may throw
  GLOBAL_NOT_FOUND) before returning false, while the landed fold
  returns false at the null read BEFORE the store. DEVIATION
  DISCLOSED, fold retained unchanged (single-reader-seat rule,
  H-016; the graded landed render is not regraded); no new body
  per the unit-186 KDoc-fold precedent.
- compiler-parse.c::ExpandText (unit 228; LOC 23, score 12.1,
  rank 117): H-015, hit-adapt - ExpandText.kt: every fprintf(Out)
  maps to CompilerState.emit with the emitted text byte-verbatim
  (the strcpy/strcat gtmp build-up, CodedOpen/CodedClose as bare
  lowercase hex, dollar escaped, extract2a dispatch, strmove,
  memcheck(iiitmp), the fcn(svPtr) line - emitted SOURCE text in
  the vendor, so emit not a call, iiitmp post-increment, bp
  sprintf tail).
- interp-sym.cpp::ScanParse (unit 229; LOC 25, score 14.7, rank
  142): H-007, hit-reuse - ScanParse.kt, the scan loop as the
  walker idiom of the arm (quote and paren tracking, escapable
  non-ASCII byte NUL handling, the break-position scratch
  contract preserved); Stubs flip to the delegate; the vendor
  scan idiom matches the unit-176 family reads.
- interp-parse.cpp::BuildGlobal (unit 230; LOC 27, score 16.1,
  rank 150): H-014, hit-reuse - BuildGlobal.kt: the Build-stack
  walker (code<0 resets, code==0 pushes a str, code==2 pops the
  leftover rslt, code==1 walks to the four-arg GlobalOrder stub
  seat) on the BuildGlobalState holder + TSTACK scratch, overflow
  banner + sigint(100) kept, no double-bang (the H-012 widening
  keeps callers honest with explicit exceptions instead).
- interp-sym.cpp::BuildLocal (unit 231; LOC 29, score 16.7, rank
  152): H-014, hit-reuse - BuildLocal.kt, the local twin: same
  Build-stack shape, the walk tail through LocalOrder - a seat
  RAISED THIS PUSH (vendor interp-sym.cpp::LocalOrder is not a
  corpus unit, no roster row; the four-arg stub added beside
  GlobalOrder with the owner note). fff fossil kept as a comment.
- compiler-parse.c::Extract2a (unit 232; LOC 21, score 18.3, rank
  158): Extract2a.kt, H-014, hit-reuse - the Extract2 seat family:
  the #S#/#G#name#target# parse into the static tmp1/tmp2[64]
  scratch, the two ternary emissions byte-verbatim against the
  vendor (sym_(1,...) ? : VariableNotFound1 //E21000 and the
  Mglobal(RETRIEVE) ? : VariableNotFound arm - both verified
  character-for-character this tranche), tmp2 returned.
- interp-zfcns.cpp::<misc> (unit 233; LOC 60, score 18.4, rank
  160): H-018, hit-adapt - InterpZfcnsMisc.kt, disposition
  ledger: the residue is the IDENTICAL stock of the unit-110
  interp-bifs.cpp::<misc> chunk - Mget verified byte-identical
  to the landed BifsMisc seat, so NO new body per the unit-186
  fold precedent; the ledger documents the fold per function.
- compiler-sys1.c::getstr1 (unit 234; LOC 37, score 19.3, rank
  165): H-001, hit-adapt - CompilerGetstr1.kt, the K&R getstr1
  (distinct-domain twin of the landed unit-140 getstr2 per the
  unit-189 substr precedent - same FILE* table shape, different
  domain: this one walks rows, the unit-48 FileDescriptor
  STDIN_READER const reused for lun 5): the while-loop form (the
  for-variable is not reassigned), posixWrite/posixRead seats
  RAISED THIS PUSH for the opnflg==3 raw-fd arm (vendor write()/_
  read(), not corpus units, no roster rows; owner notes in situ),
  LINEFEED handling byte-verbatim.
- interp-sql.cpp::<misc> (unit 235; LOC 50, score 23.6, rank
  183): H-018, hit-adapt - InterpSqlMisc.kt: the SQLITE define is
  live per the reference build config (same batch-6 ruling as
  the unit-202 sql family), the rslt char scratch as
  InterpSqlMiscState.rsltSpec holder, command_print/lcheck/dcheck
  bodies rendered (stderr banners, sigint(100) exits), the
  fold-back completions the chunker rolled in documented per
  function in the header ledger.
- include/mumpsc/compiler-macros.h::<misc> (unit 236; LOC 104,
  score 24.8, rank 190): H-004, hit-reuse - CompilerMacrosH.kt,
  pure disposition ledger (the whole header is one misc unit, no
  function bodies): every prototype and define names its landed
  seat, future owner, or prior anchor (set_code/if_code/lock_code/
  copyright_notice landed; for_code/error_code/abtrx as future
  rows; INTERPRET already the CompilerState.interpret seat; SWAP
  30 already the InterpMisc const; the Mglobal codes on the
  Carriers consts with the CLOSE-4 third-define note; the error
  codes and command chars as comment ledger). No new consts
  opened (H-004 single-owner).

Miss rows: none recorded this tranche (0 of 10 consults).

Whole-arm kotlinc gate (kotlinc 2.1.20, sha a118197b...): 224
source files, silent pass, EXIT=0, 0 errors / 0 warnings, 295
class files. H-015 hygiene scan clean on all new and edited
files (no raw dollar outside unicode escapes, no backticks, no
double-bang, no non-ASCII). Red-build count: 0 (one pre-gate
compile fix: the BuildLocal walk tail bound before the seat name
settled, and ExpandText x-escapes rendered as Kotlin escapes).

Events: 20 rows appended this tranche (the 10 consults x reuse +
hit pairs; 678 consult rows -> 1354 event rows total at this
push, 90211 bytes). The regenerated-CSV byte-identity
verification runs at batch close per the B7-close instrument
precedent.

Push hygiene and repair notes: the tranche-2 push first landed at
repo-ROOT paths (commit 013be4c created stray arms/ and logs/
copies; the intended wp2 paths were untouched), then the corrected
db210e6 push carried the same payloads at the proper wp2 paths, and
a follow-up commit renormalized the KDoc Unit numbering to the DOC
arm sequence (T1 continued: units 227-236 in committed batch
order) - the pre-repair headers had conflated the Unit id with the
units.csv file line (the units.csv row IS the file line: 236
ReadLine down to 227 macros). LESSON (the T1 lesson extended):
Unit NNN is the DOC sequential arm id; units.csv row is the file
line; never conflate. The stray root copies are removed by the
journal-splice cleanup (git rm of the two stray dirs, one commit).

DOC consult grammar rows (timestamps identical to the events CSV
append of this push; per the T1 repair lesson the rows live here
and in the CSV, never prose-only):

- DOC consult: unit libmpscpp.cpp::global::ReadLine, entry H-016, outcome hit-adapt, timestamp 2026-11-21T03:01:00Z
- DOC consult: unit compiler-parse.c::ExpandText, entry H-015, outcome hit-adapt, timestamp 2026-11-21T03:02:00Z
- DOC consult: unit interp-sym.cpp::ScanParse, entry H-007, outcome hit-reuse, timestamp 2026-11-21T03:03:00Z
- DOC consult: unit interp-parse.cpp::BuildGlobal, entry H-014, outcome hit-reuse, timestamp 2026-11-21T03:04:00Z
- DOC consult: unit interp-sym.cpp::BuildLocal, entry H-014, outcome hit-reuse, timestamp 2026-11-21T03:05:00Z
- DOC consult: unit compiler-parse.c::Extract2a, entry H-014, outcome hit-reuse, timestamp 2026-11-21T03:06:00Z
- DOC consult: unit interp-zfcns.cpp::<misc>, entry H-018, outcome hit-adapt, timestamp 2026-11-21T03:07:00Z
- DOC consult: unit compiler-sys1.c::getstr1, entry H-001, outcome hit-adapt, timestamp 2026-11-21T03:08:00Z
- DOC consult: unit interp-sql.cpp::<misc>, entry H-018, outcome hit-adapt, timestamp 2026-11-21T03:09:00Z
- DOC consult: unit include/mumpsc/compiler-macros.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-21T03:10:00Z

Next protocol step: DOC batch 8 tranche 3 (the complex stratum
rows: btree(sql), the libmpscpp misc-7 chunk, AllocSV, the
compiler-parse misc, compiler-globals.h misc, mumps_open, cannon,
the builtin.h misc, GtkTreeAdd and Merge), then the cycle-10
buffer close and the B8 close instrument.
