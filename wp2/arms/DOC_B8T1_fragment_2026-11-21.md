# DOC arm journal - 2026-11-21 (batch 8, tranche 1)

DOC batch 8 (cycle 7 per the C-001 committed row order) opens with
its simple stratum: 11 roster rows, DOC units 216-226 (units.csv
rows 237 through 247 in score order 10.3 down to 2.0; batch-8
tracker lives in this journal per the C-001 note -
PILOT_TRACKING tabulates batches 1-7 only). All bodies derived from the pristine
vendor source this session (vendor tarball sha256 621deb...ffdbe);
every vendor anchor physically verified line-for-line before
rendering. Consult ledger (one row per unit per C-008; the events
CSV append is in this same push):

- include/mumpsc/builtinTable.h::<misc> (unit 216; LOC 20, score
  2.0, rank 1): H-015, hit-adapt - the caret-literal BuiltIn[]
  roster as the internal BuiltInTable array (BuiltinTableH.kt),
  terminator kept verbatim, visibility internal pending the
  fcninit/ServiceInterface family rows.
- libmpscpp.cpp::mstring::Find (unit 217; LOC 16, score 4.2, rank
  8): H-014, hit-reuse - the global-key member
  (MstringFind.kt): Mltoa BEFORE the single RETRIEVE (vendor
  order kept; the unit-33 seat writes Mglobal first - noted as
  seat-order divergence, not vendor divergence), Mfind on the
  str carrier, atoi tail.
- compiler-kill.c::<misc> (unit 218; LOC 30, score 5.2, rank 20):
  H-004, hit-reuse - banner ledger (CompilerKillMisc.kt), June 5
  2024 date line; kill_code named as the batch-9 row.
- compiler-lock.c::<misc> (unit 219; LOC 30, score 5.2, rank
  22): H-004, hit-reuse - banner ledger (CompilerLockMisc.kt),
  May 10 2024 date line; lock_code named as the batch-9 row (the
  landed LockCode render is the vendor-file batch-4 unit).
- compiler-set.c::<misc> (unit 220; LOC 30, score 5.2, rank 26):
  H-004, hit-reuse - banner ledger (CompilerSetMisc.kt), May 26
  2024 date line; set_code already landed (unit 121, batch 4).
- include/mumpsc/zfcns.h::<misc> (unit 221; LOC 18, score 6.2,
  rank 38): H-018, hit-reuse - prototype disposition ledger
  (ZfcnsH.kt): _piece landed (unit-114 real), _justify landed
  (unit-194), ServiceInterface landed (batch-7 T3), the zfcn
  include-guard arm noted, BuildGlobal/BuildLocal named as THIS
  batch's tranche-2 rows (the vendor lines are commented in the
  header), _ascii/_horolog/_length/_random as future-batch rows.
- compiler-mumps.c::atCopy (unit 222; LOC 15, score 6.5, rank 39):
  H-014, hit-adapt - the at-copy helper rendered
  (CompilerAtCopy.kt) AND a Stubs tag correction: the stub seat
  had tagged the owner a compiler-at-test.c helper with no
  roster row; the physical owner is compiler-mumps.c:1956 and is
  units.csv row 241 (this row). The seat flips to the delegate;
  the IfCode/SetCode/WriteCode call sites bind unchanged; the
  j=2 caller-prefill contract documented in situ.
- libmpscpp.cpp::IDF (unit 223; LOC 15, score 6.7, rank 42):
  H-009, hit-reuse - the FREE wrapper (Idf.kt), distinct from
  the unit-49 member: SOH top-level-global guard with banner +
  sigint(100), caret-bypass constructor, strtod with the
  unit-49 empty-field semantic check (INVALID_CONVERSION), the
  member call bound through the invoke family - which was
  missing its CHAR-STAR single-arg arity, so this push adds the
  String overload beside the unit-31 family (LibMisc1.kt, this
  unit as owner note, MakeGlobal fold unchanged) and flips the
  Stubs.IDF seat to the delegate.
- libmpscpp.cpp::global::Piece (unit 224; LOC 17, score 7.3,
  rank 48): H-007, hit-reuse - the MSTRING-needle member
  (GlobalPiece.kt append; vendor :7259), the unit-188 pointer
  adjudication holding: unit-188's body reused verbatim with the
  Mstring str needle carrier; the char-star twin (:7181) stays a
  future-batch row.
- libmpscpp.cpp::GlobalData (unit 225; LOC 17, score 8.7, rank
  78): H-004, hit-adapt - the STRING-needle overload, verified
  line-for-line byte-equivalent to the landed unit-70 seat
  (vendor :2636); NO new body per the unit-186 KDoc-fold
  precedent (GlobalData.kt row-note; each roster row keeps its
  seat). The batch-plan C-string-twin caution discharged by
  verification.
- libmpscpp.cpp::mdh_tree_level_add (unit 226; LOC 31, score
  10.3, rank 102): H-015, hit-reuse - the EIGHT-column overload
  (MdhTreeLevelAdd6.kt), closing the family arity set (one
  through eight columns all have renders now).

Miss rows: none recorded this tranche (0 of 11 consults).

Whole-arm kotlinc gate (kotlinc 2.1.20, sha a118197b...): 215
source files, silent pass, EXIT=0, 0 errors / 0 warnings, 283
class files. H-015 hygiene scan clean on all new and edited
files (no raw dollar outside unicode escapes, no backticks, no
double-bang, no non-ASCII). Red-build count: 0.

Events: 22 rows appended this tranche (the 11 consults x reuse +
hit pairs; 668 consult rows -> 1334 event rows total at this
push, 88839 bytes). The regenerated-CSV byte-identity
verification runs at batch close per the B7-close instrument
precedent.

Next protocol step: DOC batch 8 tranche 2 (the core stratum rows:
global::ReadLine, ExpandText, ScanParse, BuildGlobal, BuildLocal,
Extract2a, the interp-zfcns misc, getstr1, the interp-sql misc
and the compiler-macros.h header).
