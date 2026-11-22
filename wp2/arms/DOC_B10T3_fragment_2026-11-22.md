# DOC B10 T3 journal fragment, 2026-11-22

DOC batch 10 (cycle 9 per C-001) tranche 3, the complex
stratum: units.csv rows 279-288 (10 rows), DOC sequential ids
299-308 (the batch closer: the simple rows 278-288 landed at
tranche 1, the core rows 289-298 at tranche 2). All 10 T3
dumps extracted with the recreated dump4.py and physically
verified at exact physical LOC (1372/484/600/369/600/156/121/
38/112/41 in roster order) against vendor anchors
(compiler-mumps.c:227 next_line, interp-sym.cpp:89 sym_,
libmpscpp.cpp:591/m591 section banner, the libmpscpp.h whole
header, libmpscpp.cpp:836, interp-select.cpp:29 the _select
pair, interp-bmgsubs.c:121, interp-parse.cpp:104 inout,
interp-globalb.cpp::Mglobal tail, libmpscpp.cpp:2291
DocCorrelate). The MISC-8 CHUNKER anomaly was adjudicated
before rendering: the dump4 misc-pool reclaim re-adds the
small-function bodies (phys 15 or less) a naive walk skips,
so libmpscpp.cpp yields 9 reachable chunks where the roster
named 5; the reclaim's d4chunk8 (699 raw / 600 phys) is
BYTE-IDENTICAL to the committed vendor dump libmpscpp.cpp::
<misc-8>.c.txt (cmp verified), and d4chunk4 == <misc-4>.

Render ledger (unit; units.csv row: LOC, score, rank - entry;
all rendered and compiled this session):

- compiler-mumps.c::main (unit 299; row 279: LOC 1372, score
  1125.2, rank 304): H-014, hit-adapt - CompilerMumpsMain.kt,
  THE COMPILER DRIVER: the argv/-SUB parse with the odd
  no-dot argv[1] source kept verbatim, the output-.m and
  program opens, the leading '+' define copy (getc/ungetc
  pushback), the include/defines banner with the floatstring/
  numcomp2/strxcat/strcatz inline emits and SyncClock/
  set_alarm, the label/function-ep scan (the ^^ and ^
  extrinsics, the _Argkk fe1-loop with the EP1001-EP1005
  emissions, the LabelsStart list), the doty indent ladder,
  the ctable spell check, the ':' postconditional
  (atTest/atCopy INTERPRET arm, the parse_ atoi arm), and the
  ENTIRE label goto web (next_line, next_cmnd, pstrtn, ctch,
  cntnue, dcl, do_entry, another_do, next_goto, gexit,
  job_return, abrt_exit, the 25 error labels) folded into the
  private MumpsCtx state machine (the ParseCPCtx precedent);
  seats: the landed handler renders ifCode/set_code/
  kill_code/lock_code/read_code/write_code/for_code/
  new_code/mumps_open/zcommands_code bind through a shared
  return-code ladder (PARSE_ERROR 1..BAD_LOCK 9, the KillCode
  _KC const-file precedent), Stubs.parse_/atTest/atCopy/
  Extract2, EP/fcninit/linepad/copyrightNotice/doty/abrtx/
  errmod_, the InterpH command chars; the FCN 'D' case is
  DEAD (icmnd is lowercased; the macro value quirk kept
  verbatim); the except[] carrier table-fills here
  (compiler-globals.h:174 static initializer); the fgets
  newline strip pair is the vendor self-assign no-op
  (H-013); the dead text (the if (!MAIN_flg && 0) prologue
  cook, the if (0) zmain block, the another_do unreachable
  while-tail, the six unreferenced error labels) ledgered;
  the break emit carries the escaped <dollar>test text.
- interp-sym.cpp::sym_ (unit 300; row 280: LOC 484, score
  632.4, rank 301): H-012, hit-reuse - InterpSym.kt, THE RUN
  TIME SYMBOL TABLE: all fifteen opcode arms of the vendor
  roster (0/1/2/3/4/5/6/7/8/9/10/11/33/99/100/101 plus the
  150/200 quirk: SymCPPdata and SymMarkCopyFlag are THE SAME
  VALUE 102 in defines.h - both stored arms fuse onto the
  same branch quirk ledger), the Stab start[] walk, the
  retrieve/11 no-label walk, the store family with the
  <dollar>noerr special case, 9/101 copy-flag/next with
  fullUnpad, the <dollar>order next-argument tail and the
  <dollar>next o_again key walk with the \\x1f coded-number
  exit decode, kill all/except with the Nmes list build, the
  New-inclusive push, the 99 push; the (char*)p1 store-arm
  returns are NOT reproducible (H-012: pointer identity is
  dead at every callsite; the non-NULL shape preserved by
  returning the b buffer); the symflg param writes are C
  dead stores (H-013); out-of-memory folds to the GC (H-008)
  with the sigint(100) tails kept; Stubs.sym_ FLIPS to
  delegate.
- libmpscpp.cpp::<misc-8> (unit 301; row 281: LOC 600,
  score 283.0, rank 291): H-014, hit-reuse -
  LibmpscppMisc10.kt: the global operator<< scalar tail
  store arms (UShort/Long/ULong/Float/Double/string/mstring),
  the ENTIRE operator>> read family (char*/global, the eight
  typed sscanf out-arms via scanIntM10/scanDoubleM10 with
  INVALID_CONVERSION, string, mstring; the Shr* holder
  classes per H-006), the free xecute/WriteMumps1 pair, the
  Pattern members (mstring/global sides, the pattern0
  no-code-raise quirk arm), the top-level Piece (Stubs.Mpiece
  seat) and the mstring Piece/Justify/Extract/Find members,
  __command__, the StopInit/StopLookup (stopWords.contains)/
  SYN/SynInit/Shred/ShredQuery adapters (the static cursor
  seat), nodash/Score/sw, the bmg_fullsearch C++ overload set
  (the malloc arm folding to the unit-305 render), the
  mstring scalar ctors with c_str/s_str, the ostream/istream
  pair, and the operator* family (seven member times arms,
  four friend commutes; GlobalRef arm unchecked, t1 aliasing,
  STR_MAX); CHUNK BOUNDARY ORPHANS: the dump head is the
  operator<<(short) orphan close brace (its signature/body
  ledgered at the misc-7 tail; the UShort/ULong store arms are
  rendered WHOLE here per the unit-202 precedent) and the dump
  tail cuts operator*(float, mstring) after its mult line
  (rendered WHOLE, one-render rule). Flips: Stubs.xecute
  (CharArray), stopLookup, SYN, shredQuery, mstringTimes x7.
- include/mumpsc/libmpscpp.h::<misc> (unit 302; row 282: LOC
  369, score 201.7, rank 290): H-014, hit-reuse -
  LibmpscppHMisc.kt, the whole-header pure ledger (the H-018
  convention): every declaration block dispositioned in
  vendor order to its landed owner (Carriers consts,
  ErrorsH, ArithH, the unit-293/297 renders, the add1/add3
  seats, MpstimeMisc/SynInit/StopInit seats, Misc5 GTK,
  unit-293 wstem, T1 Mltoa, the batch-10 renders of this
  tranche); no executable seat raised; the
  VARIABLE_NOT_FOUND 3 / PATTERN_ERROR 7 pair has no
  file-scope const and ledgered here.
- libmpscpp.cpp::<misc-4> (unit 303; row 283: LOC 600,
  score 152.4, rank 283): H-014, hit-reuse -
  LibmpscppMisc9.kt: the mstring operator=(global) tpx-
  checked assignment, operator!, pre/post inc/dec, unary +/-
  , the member + - % / ladders (Int/Long via the NEW
  Stubs.add1 seat, Double/Float via the NEW Stubs.add3 seat
  (both arith.h owners, raised throwing H-014; the 48-53
  controller arms); the <dollar>-sign fold family rides
  atollShared/Numcomp), the || concat family (eight member
  + seven friend commutes), the >= ladder, the <= ladder
  HEADS only (the <=(global)/(mstring) tails are the landed
  Misc5 le seats, one-render rule), and the scalar friend
  sets (the pure commutes render as the members they call);
  char*/const char* twins collapse onto String (H-015);
  the GlobalRef % arm reads unchecked, the GlobalRef + arm is
  the unchecked read (H-012). Flips: mstringPlus/Minus/Div
  (x7 each), mstringRem (x5), with the B10 T3 RESOLUTION
  family note; add1/add3 raised.
- interp-select.cpp::<misc> (unit 304; row 284: LOC 156,
  score 115.0, rank 281): H-014, hit-reuse -
  InterpSelectMisc.kt: the two _select bodies (the H-015
  overload pair resolved by signature; the select with more
  output than input is the vendor sigaction select probe
  family): the NULL-capable odd slots render as CharArray?
  probed in vendor order, the selectErrSel banner + sigint
  tail, the private atolSel (the leading-whitespace sign/
  digit walk), strmove on the Stubs seat; NO Stubs flip
  (no landside seat exists; compile-side call arms bind
  directly).
- interp-bmgsubs.c::<misc> (unit 305; row 285: LOC 121,
  score 76.3, rank 258): H-004, hit-reuse -
  InterpBmgsubsMisc.kt: the Boyer-Moore-Gosper search:
  bmg_setup (the BmgStatics delta0/cmap/str/linetemp/pattern
  carriers, delta0[pattern-j-1] write loop), bmg_act, the
  bmg_fullsearch window walk (copyOfRange over the
  NUL-terminated CharArray, H-007), Bcopy, the file-scope
  bmg_found owner as bmgFoundB (H-017), LARGE_BMS 59000,
  the ftp.uu.net attribution banner rows (H-013), the
  pattern[0] no-op statement ledgered; Stubs.bmgFullSearch
  FLIPS to the bmgFullSearchStrM10 adapter (CharArray(0)
  for the empty string per the _piece family idiom).
- interp-parse.cpp::inout (unit 306; row 286: LOC 38, score
  58.6, rank 249): H-004, hit-reuse - InterpParseInout.kt:
  the runtime device writer (inout plus the outwIO helper):
  the switch arms over the device index with the dev==5
  console arms (putchar/fflush render as System.out, H-001),
  the Writer-array out_file carrier with the null guard
  (the C dereference UB folded to the controlled guard,
  H-012 twin), the fall-off-the-switch return shape
  (H-013); the compiler-side unit-361 col twin is a DISTINCT
  vendor overload (Short int vs CharArray, H-015); Stubs.
  inout FLIPS to delegate.
- interp-globalb.cpp::<misc> (unit 307; row 287: LOC 112,
  score 53.4, rank 239): H-018, hit-reuse -
  InterpGlobalbMisc.kt, the excluded-arm ledger: every code
  row of this chunk is a native-build arm EXCLUDED from the
  reference build by the ifdef family (the -1 probe's dead
  1-store key rewrite, the NEXT/ORDERNEXT result walk with
  the empty/-1 rewrites, the hold[j]==1 back-off, the in-place
  bd shift, keyfix before every btree call) - each cites its
  live-arm twin already landed at Mglobal.kt and the btree
  codec family; the LGPL/attribution banners are comment rows
  (H-013).
- libmpscpp.cpp::global::DocCorrelate (unit 308; row 288:
  LOC 41, score 42.3, rank 225): H-014, hit-reuse -
  GlobalDocCorrelate.kt: the document correlation driver: the
  nested Order(1) walks over THIS vs B, the Cosine/Sim1/
  Dice/Jaccard scorer ladder (the default 1), the symmetric
  threshold write B(doc1,doc2)=c; the C++ member rides the
  GlobalRef extension seat under the docCorrelateStr name
  (H-017: the Misc5-landed DocCorrelate delegate twins own
  the name; redeclaration clash avoided), the string fcn
  ladder rides the char* twins' working body (the unit-154
  front-run ledger), (*this)(doc1) rides the NEW
  Stubs.globalInvokeKeyed seat (raised throwing H-014, the
  subscripted operator() owner family next to the
  setGlobalDoubleKeyed seat), Order/Kill rides Stubs.
  globalOrderN/globalKill; Stubs.globalDocCorrelate FLIPS to
  delegate.

Seat flips and ledger edits this push (Stubs.kt,
SeqAlignExterns.kt):

- Stubs.sym_ -> doc delegate (row 280; the T1 anticipatory
  tag resolves to this owner).
- Stubs.inout, Stubs.bmgFullSearch, Stubs.inout family call
  sites unchanged; Stubs.globalDocCorrelate -> delegate
  (row 288); Stubs.globalInvokeKeyed RAISED (throwing, the
  operator() owner); Stubs.xecute, Stubs.stopLookup, Stubs.
  SYN, Stubs.shredQuery -> delegates (row 301); Stubs.
  mstringTimes x7 -> delegates (row 301); Stubs.mstringPlus/
  mstringMinus/mstringDiv (x7 each) and mstringRem (x5) ->
  delegates with the B10 T3 RESOLUTION family note (row 303;
  the one-arg Int seat family folded, tag corrected);
  Stubs.add1/Stubs.add3 RAISED (throwing; the arith.h scalar
  ladder owners, rows 302/303).
- SeqAlignExterns.kt: fun Score(a: Char, b: Char) flipped
  from the raise to the GAP/MATCH/MISMATCH logic (the
  adapter twin of the landed Jaccard-family scorer consts;
  the house ledger cited this file row earlier - scored
  resolved with this push, DAN1 cleared).

Miss rows: none recorded this tranche (0 of 10 consults; one
hit-adapt - the compiler driver command dispatch - and nine
hit-reuse).

Whole-arm kotlinc gate (kotlinc 2.1.20): 287 source files
(277 + 10 new), silent pass, EXIT=0, 0 errors / 0 warnings,
red-build count 0 across the tranche. H-015 hygiene scan
clean on all twelve pushed text artifacts (no raw dollar, no
backticks, no double-bang, ASCII only - the two InterpSym
banner dollars rewritten to the \\u0024 escape, the misc-8
banner dollar carried as the escaped Kotlin string) and
every pushed text file ends with the trailing-newline byte
(the B9 close splice-repair rule asserted again at push
time).

Events: 20 rows appended this tranche (the 10 consults x
reuse + hit pairs; 740 consult rows -> 750, 1478 event rows
-> 1498 at this push, 1479 CSV lines -> 1499). The
regenerated-CSV byte-identity verification runs with this
push: the python port of wp2/analysis/R/export_doc_events.R
recreated fresh this session reproduces the at-rest 1479-
line CSV byte-for-byte first (740 -> 1478; md5 ccb04dc0 vs
the master zip copy), then over the journal WITH this
fragment appended produces the 1499-line CSV whose first
1479 lines are byte-identical to the at-rest file (the
prefix append check; the T2 sim copy's inverted append
check was the known erratum, corrected in the recreate).

Next protocol step: the DOC batch 10 CLOSE instrument (the
B9-close shape: coverage checklist over rows 278-310, the
consult totals, the per-unit list, events regen, the mstring
33-seat ledger reconciliation - the mstringPlus family closure
and the add1/add3 raised seats - grading), which completes
the DOC arm roster 1-308 of units.csv.

DOC consult grammar rows (timestamps identical to the events
CSV append of this push; roster order 299-308, the CSV rows
are ordered by roster, not by timestamp):

- DOC consult: unit compiler-mumps.c::main, entry H-014, outcome hit-adapt, timestamp 2026-11-22T06:28:00Z
- DOC consult: unit interp-sym.cpp::sym_, entry H-012, outcome hit-reuse, timestamp 2026-11-22T06:29:00Z
- DOC consult: unit libmpscpp.cpp::<misc-8>, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:21:00Z
- DOC consult: unit include/mumpsc/libmpscpp.h::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:22:00Z
- DOC consult: unit libmpscpp.cpp::<misc-4>, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:23:00Z
- DOC consult: unit interp-select.cpp::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:24:00Z
- DOC consult: unit interp-bmgsubs.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T06:25:00Z
- DOC consult: unit interp-parse.cpp::inout, entry H-004, outcome hit-reuse, timestamp 2026-11-22T06:26:00Z
- DOC consult: unit interp-globalb.cpp::<misc>, entry H-018, outcome hit-reuse, timestamp 2026-11-22T06:27:00Z
- DOC consult: unit libmpscpp.cpp::global::DocCorrelate, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:30:00Z
