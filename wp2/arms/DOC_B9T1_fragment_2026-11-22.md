# DOC B9 T1 journal fragment, 2026-11-22

### DOC B9 tranche 1 (units 247-256, simple stratum) - 2026-11-22

Sandbox rebuilt per rebuild-recipe.md from repo.zip at master fcb223b
(vendor sha256 621deb...ffdbe; JDK Corretto 17.0.21.1; kotlinc
2.1.20 sha a118197b...; baseline gate silent EXIT=0 at the 232
landed files). Batch 9 opens: the units.csv batch-column-9 block
(rows 248-278, 31 rows: 10 complex / 11 core / 10 simple); DOC
sequential ids continue 247-277 in the committed stratum order
(simple first), per the B8 Unit-vs-row conventions (the 2cfddec
repair). All 10 T1 dumps extracted with dump4.py (recreated per
recipe; the GlobalOrder overload disambiguated by physical LOC
15 against the 17-loc char* twin) and verified at exact
physical LOC (26/21/22/22/22/15/18/30/18/16 x the roster order
below).

Render ledger (unit; units.csv row: LOC, score, rank - entry);
all physically verified this session:

- interp-arith.cpp::mps_exp2 (unit 247; row 269: LOC 26, score
  8.3, rank 100): H-018, hit-reuse - MpsExp2.kt: exp2 double as
  2.0.pow(x) (the MpsExp10 10.0.pow precedent; Kotlin math has
  no exp2 intrinsic); strtod (MpsExp.kt seat) + gcvt_wrapper
  (H-014 stub) ride the family shape; BIGFLOAT/MPFR arms
  documentary per the reference config.
- libmpscpp.cpp::global::Translate (unit 248; row 270: LOC 21,
  score 6.9, rank 96): H-014, hit-reuse - the TWO-mstring
  overload appended to GlobalTranslate.kt (the unit-131 one-arg
  sibling; its overload note now resolves). Same Mglobal
  RETRIEVE/GLOBAL_NOT_FOUND throw, the second commerce-d-zero
  separator pair added verbatim, fcn dispatch v1d[1] -> bd[1]
  seats as unit 131.
- interp-arith.cpp::mps_log (unit 249; row 271: LOC 22, score
  8.4, rank 88): H-018, hit-reuse - MpsLog.kt: kotlin.math.ln
  intrinsic over strtod; excluded arms documentary;
  Stubs.mpsLog FLIPPED to delegate this push.
- interp-arith.cpp::mps_cos (unit 250; row 272: LOC 22, score
  8.4, rank 89): H-018, hit-reuse - MpsCos.kt (kotlin.math.cos);
  Stubs.mpsCos FLIPPED.
- interp-arith.cpp::mps_tan (unit 251; row 273: LOC 22, score
  8.4, rank 94): H-018, hit-reuse - MpsTan.kt (kotlin.math.tan);
  Stubs.mpsTan FLIPPED.
- libmpscpp.cpp::GlobalOrder (unit 252; row 274: LOC 15, score
  5.5, rank 63): H-014, hit-reuse - the MSTRING overload appended
  to GlobalOrder.kt (third corpus row of the file; overload
  disambiguated by LOC 15 in the units.csv block). Same xecute/
  SymGet seats as the unit-39/69 pair; NO dead r[STR_MAX]
  scratch here (the unit-69 dead-store note does not apply);
  the dead int i kept per H-013.
- interp-sym.cpp::_random (unit 253; row 275: LOC 18, score
  5.5, rank 43): H-010, hit-adapt - Random.kt: the vendor static
  first/srand(time) lazy seed folds to a private holder object
  seeded once from the epoch-second clock; the libc rand ladder
  j * rand() / (RAND_MAX + 1.0) folds to kotlin.random.Random
  with the vendor rejection loop kept verbatim (the empty C
  while-body is the scalar test itself, quirk kept); atoi is the
  real Find.kt seat; strmove(out,"0") rides the H-014 throwing
  seat per the Extract.kt precedent; sprintf %d via
  String.format Locale.US per H-009.
- compiler-write.c::<misc> (unit 254; row 276: LOC 30, score
  5.2, rank 27): H-004, hit-reuse - CompilerWriteMisc.kt, pure
  disposition ledger (banner + include file, no function
  bodies): includes name their landed seats (CompilerMacrosH
  unit 236, CompilerGlobalsExtMisc unit 151) or the
  not-a-corpus-unit note (memsize.h) and the canonical-IO banner
  marker (stdio.h, H-001 banner-side).
- interp-bstring.cpp::bstrcat (unit 255; row 277: LOC 18, score
  5.5, rank 5): H-014, hit-reuse - Bstrcat.kt: owner of the
  unit-22 banner note lands; Stubs.bstrcat FLIPPED to delegate.
  The Bstring carrier, System.err (H-001), the real unit-211
  sigint seat; vendor shape kept (strncpy/exact-count, explicit
  left.length NUL, strncat + implicit terminator, unreachable
  dummy new_bstring("") after the sigint fall-through); the
  catlength == STR_MAX one-past terminator guarded with the
  bcatchr/checked_strlen one-past quirk noted (H-012).
- interp-sym.cpp::_horolog (unit 256; row 278: LOC 16, score
  4.9, rank 2): H-009, hit-adapt - Horolog.kt: the 47122 day
  ladder kept VERBATIM (VENDOR EPOCH DIVERGENCE vs the Fcn.kt
  case-14 inline arm's 47116+1 ladder - the vendor carries two
  horolog bases, both vendor-truth, journal-ledgered here);
  time intrinsic per the Fcn precedent; sprintf %ld + strcat
  as Locale.US carrier writes; tmp2 64-char scratch kept.

Seat flips and ledger edits this push (Stubs.kt + two ledger
corrections):

- Stubs.mpsLog/mpsCos/mpsTan/mpsExp2 -> doc delegates (tags
  "batch-9/-10" resolved: all four are batch-9 simple rows);
  mpsFloor stays throwing with its tag CORRECTED to the
  batch-9 CORE row 268 (flips at tranche 2); mpsCeil/mpsTrunc
  stay throwing, batch-10 roster rows.
- Stubs.bstrcat -> doc.bstrcat delegate (the correctly-tagged
  forward seat resolves).
- ZfcnsH.kt ledger: _horolog TAG CORRECTION (the forward tag
  wrote "batch-10 roster row"; the units.csv row is 278, batch
  column 9 - superseded per the Concat forward-tag precedent);
  _random and _horolog owner notes flip to RENDERED with the
  278/275 row cites.
- LibmpscppMisc6.kt Horolog(): CALL-SITE REBIND (the
  delegate-when-owner-lands mechanism) - the vendor
  libmpscpp.cpp:3446 body calls _horolog(t); the pre-rebind
  inline had used the Fcn 47116+1 ladder (five day-ladders
  behind the vendor helper); the delegate restores vendor
  fidelity, the divergence disclosed at the row-note.

Miss rows: none recorded this tranche (0 of 10 consults).

Whole-arm kotlinc gate (kotlinc 2.1.20, sha a118197b...): 240
source files, silent pass, EXIT=0, 0 errors / 0 warnings, 312
class files at the closer. H-015 hygiene scan clean on all new
and edited files (no raw dollar outside the DOLLAR_131 const
and \u0024 escapes, no backticks, no double-bang, no non-ASCII).
Red-build count: 0 (the gate was silent first pass over the
whole tranche; one pre-gate fold settled during drafting - the
mps_exp2 exponent intrinsic, resolved to the pow precedent
before the compile).

Events: 20 rows appended this tranche (the 10 consults x
reuse + hit pairs; 698 consult rows -> 1394 event rows total at
this push, 92907 bytes). The regenerated-CSV byte-identity
verification runs at batch close per the B7-close instrument
precedent.

Next protocol step: DOC batch 9 tranche 2 (the core stratum
rows, DOC units 257-267, units.csv rows 258-268: mps_floor with
the Stubs.mpsFloor flip, GlobalOrder.h inline, Eval, Avg,
FixGblRef, the interp-service misc, xindex, ExpandName, the
interp-sym misc and the compiler-sys1 misc).

DOC consult grammar rows (timestamps identical to the events CSV
append of this push):

- DOC consult: unit interp-arith.cpp::mps_exp2, entry H-018, outcome hit-reuse, timestamp 2026-11-22T04:20:00Z
- DOC consult: unit libmpscpp.cpp::global::Translate, entry H-014, outcome hit-reuse, timestamp 2026-11-22T04:21:00Z
- DOC consult: unit interp-arith.cpp::mps_log, entry H-018, outcome hit-reuse, timestamp 2026-11-22T04:22:00Z
- DOC consult: unit interp-arith.cpp::mps_cos, entry H-018, outcome hit-reuse, timestamp 2026-11-22T04:23:00Z
- DOC consult: unit interp-arith.cpp::mps_tan, entry H-018, outcome hit-reuse, timestamp 2026-11-22T04:24:00Z
- DOC consult: unit libmpscpp.cpp::GlobalOrder, entry H-014, outcome hit-reuse, timestamp 2026-11-22T04:25:00Z
- DOC consult: unit interp-sym.cpp::_random, entry H-010, outcome hit-adapt, timestamp 2026-11-22T04:26:00Z
- DOC consult: unit compiler-write.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T04:27:00Z
- DOC consult: unit interp-bstring.cpp::bstrcat, entry H-014, outcome hit-reuse, timestamp 2026-11-22T04:28:00Z
- DOC consult: unit interp-sym.cpp::_horolog, entry H-009, outcome hit-adapt, timestamp 2026-11-22T04:29:00Z
