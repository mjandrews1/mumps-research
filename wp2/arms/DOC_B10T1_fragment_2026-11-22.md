# DOC B10 T1 journal fragment, 2026-11-22

### DOC B10 tranche 1 (units 278-288, simple stratum) - 2026-11-22

Sandbox rebuilt fresh this session per rebuild-recipe.md from
repo.zip at master 1a512e1 (vendor sha256 621deb...ffdbe; JDK
Corretto 17.0.21.1; kotlinc 2.1.20 sha a118197b...; baseline
gate silent EXIT=0 at the 260 landed files; journal 6634
lines, events CSV 1437 lines / 95733 bytes at rest). Batch 10
opens: the units.csv batch-column-10 block (rows 279-309, 31
rows: 10 complex / 10 core / 11 simple); DOC sequential ids run
278-288 in the committed stratum order (simple first, the B9
convention). All 11 T1 dumps extracted with the recreated
dump4.py (load_units function mode, min_loc 15 / chunk 600) and
physically verified at exact physical LOC (17/18/17/26/26/17/
17/16/15/27/18 x the roster order below) against vendor line
anchors (GlobalLength :3330/:3354, SysLoad interp-sym.cpp:949,
mps_trunc :406, mps_ceil :483, global::Piece :7259, mstring::
Piece :7332, _length :1363, global::Pattern :7121, at-test
banner, global::Justify :7372). The GlobalLength and
global::Piece four-overload families were ADJUDICATED with the
study-tool metrics THIS session (the 8.1/5.5 rows are
metric-twins; the pointer_marks 1 / score 7.3 rows are uniquely
the char-star twins).

Render ledger (unit; units.csv row: LOC, score, rank - entry);
all physically verified this session:

- libmpscpp.cpp::global::Length (unit 278; row 299: LOC 17,
  score 8.1, rank 64): H-007, hit-reuse - VERIFICATION-ONLY:
  the std::string needle twin (vendor :3330) of the landed
  unit-159 mstring render (:3306); the bodies are
  byte-identical modulo the needle access (needle.c_str() on
  both forms); the u159 seat serves the row, the fold note
  appended to GlobalLength.kt; no new code.
- interp-sym.cpp::SysLoad (unit 279; row 300: LOC 18, score
  8.0, rank 62): H-001, hit-adapt - SysLoad.kt: the unit-142
  sysDump TWIN, the READ side of the pid-file symbol-table
  pair: Mltoa pid (the H-014 stub seat, the long-pid Int
  narrowing per H-010), fopen the pid-named file H-001
  (BufferedReader/FileReader; the vendor fopen-NULL is a CLEAN
  return 0, no ErrorMessage - the twin's quantization does not
  apply), the fgets PAIR walk (readLine strips the vendor
  newline trim - a fold; the unchecked second fgets quantized
  to the fresh empty string per the deterministic-UB rule, odd
  final line), sym_(0,...) over the H-014 seat (owner tag now
  cites the batch-10 row-280 sym_ render; flips at tranche 3),
  fclose the GC no-op, 1/0 returns.
- libmpscpp.cpp::global::Length (unit 280; row 301: LOC 17,
  score 7.7, rank 56): H-012, hit-reuse - VERIFICATION-ONLY:
  the const-char-star needle twin (vendor :3354) of the same
  unit-159 seat (the bare-needle strstr walk); fold note in
  GlobalLength.kt; no new code.
- interp-arith.cpp::mps_trunc (unit 281; row 302: LOC 26,
  score 7.6, rank 51): H-018, hit-reuse - MpsTrunc.kt in the
  unit-267 mpsFloor family shape (verified line-for-line
  against the :483 mps_ceil sibling this session): truncate
  intrinsic over strtod, gcvt_wrapper H-014 stub seat, the
  BIGFLOAT/GMP arms documentary, the int-0 return folded to
  Unit per the family-delegate convention; Stubs.mpsTrunc
  FLIPS to the delegate (the batch-10 tag resolves).
- interp-arith.cpp::mps_ceil (unit 282; row 303: LOC 26,
  score 7.6, rank 52): H-018, hit-reuse - MpsCeil.kt: the
  ceil twin of unit 281 (the vendor bodies differ only in
  trunc/ceil and mpf_trunc/mpf_ceil, verified line-for-line);
  Stubs.mpsCeil FLIPS (the mps throwing-seat family is now
  fully delegated).
- libmpscpp.cpp::global::Piece (unit 283; row 304: LOC 17,
  score 5.5, rank 33): H-007, hit-reuse - VERIFICATION-ONLY:
  the metric adjudication (study-tool complexity_metrics over
  all four :7181/:7204/:7236/:7259 bodies) shows the landed
  unit-224 render (:7259, the mstring-ref body, ptr 0 / score
  5.5) IS this row's form. TAG CORRECTION at GlobalPiece.kt
  (the u141/u148/u169/u172 family): the u224 header's
  "units.csv row 239" attribution was the misconstruction -
  the row-239 metrics (score 7.3, ptr 1) are uniquely the
  :7181 char-star twin, which FOLDS onto the unit-188 String
  seat (:7236 vs :7181 differ only in the needle carrier,
  the u214 carrier-fold convention); the physical u224 body
  is unchanged, its roster row re-tagged 304; no new code.
- libmpscpp.cpp::mstring::Piece (unit 284; row 305: LOC 17,
  score 5.5, rank 34): H-007, hit-reuse - MstringPieceGlobal.kt:
  the RECEIVER-REVERSED twin: the key comes from the database
  (Mglobal RETRIEVE over pat.ref, the u38 conflation
  convention, GLOBAL_NOT_FOUND throw), the SOURCE is the
  receiver's own value (the data.c_str() str carrier), the
  _piece source/needle pair REVERSED vs the global-receiver
  family (the reversal IS this unit's H-007 quirk ledger);
  jj<0 collapse to ii, strcpyS %d decimals, empty modes
  CharArray, _piece real (unit-114), Mstring return.
- interp-sym.cpp::_length (unit 285; row 306: LOC 16, score
  5.4, rank 30): H-014, hit-reuse - LengthSym.kt (fun _length,
  the _find leading-underscore key id precedent): the KEYED
  dollar-length fold - xindex (the REAL unit-264 seat,
  FindSym.kt) occurrence walk with i += k past-the-needle
  advance (overlaps do not double-count, vendor verbatim),
  the (short) i probe narrowing per H-010, j+1 piece-count
  semantics (zero hits report 1, the u133/159 family
  convention); the empty-key arm is the plain strlen; sprintf
  %d via strcpyS on the NUL-terminated out carrier.
- libmpscpp.cpp::global::Pattern (unit 286; row 307: LOC 15,
  score 5.1, rank 12): H-014, hit-reuse - GlobalPattern.kt:
  the GLOBAL-key member (the :7142/:7156/:7169 string/mstring/
  char-star siblings are below min_loc, not roster units; the
  unit-89 GlobalClassMisc "no owner unit" Pattern ledger row
  is SUPERSEDED, the seat note edited) - the unit-133
  double-retrieve template (this global's value, then the
  argument global's, both GLOBAL_NOT_FOUND throw arms), the
  return the Stubs.pm verdict (interp-pm.cpp::pm, not a
  corpus unit, the unit-154 pmw seat).
- compiler-at-test.c::<misc> (unit 287; row 308: LOC 27,
  score 4.9, rank 10): H-004, hit-reuse -
  CompilerAtTestMisc.kt, the u254 compiler-write.c ledger
  shape: the banner (kept in summary per the copyright-family
  ledger), the May 5 2024 revision line, the single
  <stdio.h> include disposition (H-001 banner-side marker;
  the atTest printf banners ride the unit-109 System.out
  folds); atTest itself is the LANDED unit-109 render
  (AtTest.kt); no runtime seat.
- libmpscpp.cpp::global::Justify (unit 288; row 309: LOC 18,
  score 4.2, rank 6): H-014, hit-reuse - GlobalJustify.kt:
  the JUSTIFY MSV member (the unit-89 declaration-row Justify
  supersession seat-edited to cite this render): the strcpyS
  %d decimal pair, the single Mglobal RETRIEVE of this
  global's own value, GLOBAL_NOT_FOUND throw, then the REAL
  unit-194 _justify seat (InterpJustify.kt), the Mstring
  return wrap; the vendor's dead i/j=1/k/x locals
  ledgered H-013.

Seat flips and ledger edits this push (Stubs.kt, GlobalPiece.kt,
GlobalClassMisc.kt, GlobalLength.kt):

- Stubs.mpsCeil/mpsTrunc -> doc delegates (both batch-10 tags
  resolved; the mps throwing-seat family fully delegated).
- Stubs.sym_ owner tag updated (interp-sym.cpp::sym_ = the
  batch-10 complex roster row 280; flips at tranche 3; the
  sysLoad render calls the seat).
- GlobalPiece.kt: the u224 TAG CORRECTION (row 239 -> row 304;
  the :7181 char-star twin folds onto the u188 String seat).
- GlobalClassMisc.kt: the Pattern ledger row supersession
  (global = the unit-286 render) and the Justify row (the
  unit-288 render).
- GlobalLength.kt: the unit-278/280 twin-fold addendum note
  (the u133 "other-batch rows" anticipation resolves).

Miss rows: none recorded this tranche (0 of 11 consults).

Whole-arm kotlinc gate (kotlinc 2.1.20, sha a118197b...): 268
source files, silent pass, EXIT=0, 0 errors / 0 warnings, red-
build count 0 across the tranche (the KDoc apostrophe slip of
the first SysLoad draft - a lost leading comment slash - was
caught by the pre-gate hygiene scan, before any compile).
H-015 hygiene scan clean on all new and edited files (no raw
dollar, no backticks, no double-bang, ASCII only) and every
pushed text file ends with the trailing-newline byte (the B9
close splice-repair rule asserted at push time).

Events: 22 rows appended this tranche (the 11 consults x
reuse + hit pairs; 730 consult rows -> 1458 event rows total
at this push, 1459 CSV lines). The regenerated-CSV byte-
identity verification runs with this push: the python port of
wp2/analysis/R/export_doc_events.R over the journal WITH this
fragment appended reproduces the appended CSV byte-for-byte
(the same port reproduced the at-rest 1437-line CSV first,
719 -> 1436, before the splice).

Next protocol step: DOC batch 10 tranche 2 (the core stratum
rows, DOC units 289-298, units.csv rows 289-298: unpad,
defines.h misc, line1149, Mltoa, wstem, sysparms.h misc,
fnumber misc, errors.h misc, the compiler-sys1 xindex twin and
the mdh col9 overload twin - the last two prospective
verification folds), then tranche 3 (the complex stratum,
rows 279-288: main, sym_, misc-8, libmpscpp.h, misc-4, select
misc, bmgsubs misc, inout, globalb misc, DocCorrelate), then
the B10 close.

DOC consult grammar rows (timestamps identical to the events
CSV append of this push):

- DOC consult: unit libmpscpp.cpp::global::Length, entry H-007, outcome hit-reuse, timestamp 2026-11-22T06:00:00Z
- DOC consult: unit interp-sym.cpp::SysLoad, entry H-001, outcome hit-adapt, timestamp 2026-11-22T06:01:00Z
- DOC consult: unit libmpscpp.cpp::global::Length, entry H-012, outcome hit-reuse, timestamp 2026-11-22T06:02:00Z
- DOC consult: unit interp-arith.cpp::mps_trunc, entry H-018, outcome hit-reuse, timestamp 2026-11-22T06:03:00Z
- DOC consult: unit interp-arith.cpp::mps_ceil, entry H-018, outcome hit-reuse, timestamp 2026-11-22T06:04:00Z
- DOC consult: unit libmpscpp.cpp::global::Piece, entry H-007, outcome hit-reuse, timestamp 2026-11-22T06:05:00Z
- DOC consult: unit libmpscpp.cpp::mstring::Piece, entry H-007, outcome hit-reuse, timestamp 2026-11-22T06:06:00Z
- DOC consult: unit interp-sym.cpp::_length, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:07:00Z
- DOC consult: unit libmpscpp.cpp::global::Pattern, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:08:00Z
- DOC consult: unit compiler-at-test.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T06:09:00Z
- DOC consult: unit libmpscpp.cpp::global::Justify, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:10:00Z
