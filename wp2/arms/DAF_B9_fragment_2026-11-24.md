# DAF batch 9 (units 247-277, all strata) - 2026-11-24

DAF batch 9 in ONE session per the C-011 protocol: all 31 units
(247-277) rendered this session, in the pre-registered
batch_plan.md order. All dumps loc-asserted via the ~/b4 dump4.py
WANT list. The batch includes the two largest single units of the
arm so far (btreeX 502 LOC, libmpscpp misc-3 600 LOC) and closes
on the 1201-LOC compiler expression parser parse_ (score 765.3),
which lands on its own seat parseCDaf over the DafCompilerState
globals. This close carries five PI adjudication flags (section
below) and one open call-graph question (the zseek rebind).

## Units rendered (17 new kt files + 16 edits, package daf)

- 247 interp-sym.cpp::_horolog (LibmpscppMisc6.kt edit):
  horologDaf SEAT RELEASED (stub deleted from DafInterpStubsT3
  with a release note); the ZfcnsH forward tag flips to landed.
  Consult 22:30:00Z.
- 248 interp-bstring.cpp::bstrcat (InterpBstring.kt): DBstringD
  (newBstringEmptyD renamed to clear the RpmParserStubsD
  newBstringD clash), bstrcatDaf; STR_MAX 500-vs-32000 strmax.h
  blame inaccuracy DISCLOSED, no const change. Consult 22:30:30Z.
- 249 compiler-write.c::<misc> (CompilerWriteMisc.kt):
  pre-existing render; batch roster attached, no code change.
  Consult 22:31:00Z.
- 250 interp-sym.cpp::_random (InterpSymRandom.kt): pre-existing
  render; roster attached. Consult 22:31:30Z.
- 251 libmpscpp.cpp::GlobalOrder (LibmpscppGlobalOrder.kt):
  pre-existing render; roster attached. Consult 22:32:00Z.
- 252 interp-arith.cpp::mps_log (MpsLog.kt): pre-existing render;
  roster attached. Consult 22:32:30Z.
- 253 interp-arith.cpp::mps_cos (MpsCos.kt): pre-existing render;
  roster attached. Consult 22:33:00Z.
- 254 interp-arith.cpp::mps_tan (MpsTan.kt): pre-existing render;
  roster attached. Consult 22:33:30Z.
- 255 libmpscpp.cpp::global::Translate (GlobalTranslate.kt):
  pre-existing render; roster attached. Consult 22:34:00Z.
- 256 interp-arith.cpp::mps_exp2 (MpsExp2.kt): pre-existing render;
  roster attached. Consult 22:34:30Z.
- 257 interp-arith.cpp::mps_floor (MpsFloor.kt): pre-existing
  render; roster attached. Consult 22:35:00Z.
- 258 libmpscpp.cpp::mdh_tree_level_add (MdhTreeLevelAdd.kt):
  pre-existing render; roster attached. Consult 22:35:30Z.
- 259 include/mumpsc/globalOrder.h::GlobalOrder (GlobalOrderH.kt):
  pre-existing render; roster attached; 1 pre-existing comment
  dollar normalized (compile-identical). Consult 22:36:00Z.
- 260 interp-strmanip.cpp::xindex (InterpStrmanipXindex.kt):
  xindexDaf SEAT RELEASED (stub deleted with a note); the b10
  compiler-sys1 xindex twin keeps its own roster row. Consult
  22:36:30Z.
- 261 interp-service.cpp::<misc> (InterpServiceMisc.kt): prelude
  ledger only, no code; every extern names a landed seat.
  Consult 22:37:00Z.
- 262 libmpscpp.cpp::mstring::FixGblRef (LibmpscppMisc7.kt edit):
  fixGblRefD RELEASED - TAG CORRECTION: the not-a-corpus-unit
  blame is superseded, row 262 owns it; the ^gbl() -> ^gbl("")
  shape; the mstringQlengthFixD/mstringQueryFixD call sites bind
  unchanged. Consult 22:37:30Z.
- 263 libmpscpp.cpp::global::Avg (LibmpscppGlobalAvg.kt):
  globalAvgD XNEXT walker; the GlobalNodeStub.kt avg member
  RELEASED (delegates); unit-130 AvgD rides unchanged;
  siCopyState promoted to package-level copyStateDaf in
  InterpService.kt for reuse; sscanf %lf probe via private
  avgScanD. Consult 22:38:00Z.
- 264 interp-sym.cpp::Eval (InterpSymEval.kt): evalDaf SEAT
  RELEASED, stub deleted with a release note; AllocSV +
  copyStateDaf + parseDaf(sv) relay, out-copy from pd1+sdlim.
  Consult 22:38:30Z.
- 265 compiler-parse.c::ExpandName (CompilerExpandName.kt):
  expandNameD; verbatim vendor quirks: the duplicated #S# test
  kept, the un-terminated arg buffers guarded; extract2Daf
  raised stub called. Consult 22:39:00Z.
- 266 interp-sym.cpp::<misc> (InterpSymMisc.kt): prelude ledger +
  asciiDaf fold-in body; ZfcnsH _ascii and _horolog forward tags
  flipped. Consult 22:39:30Z.
- 267 compiler-sys1.c::<misc> (CompilerSys1Misc.kt): lpad is
  byte-identical to the landed lpadStrmanipD so NO duplicate body
  (ledger only); linepadDaf RELEASED (ForCode stub deleted);
  linepad2Daf RELEASED (CompilerFcninit stub deleted, TAG
  CORRECTION: owner compiler-sys1, not compiler-read); multDaf
  RELEASED (Cannon stub deleted, the not-a-corpus blame
  superseded); sigcD/itoaD/linepad1D/gcvtD bodies. Consult
  22:40:00Z.
- 268 interp-rpm.cpp::perl_pm (RpmMisc.kt edit): perlPmD RELEASED
  - TAG CORRECTION: row 268 owns it, the earlier 256 blame is
  superseded; java.util.regex.Pattern fold, the sigintD(100) arm;
  callers bind unchanged. Consult 22:40:30Z.
- 269 libmpscpp.cpp::Jaccard (LibmpscppJaccard.kt): jaccardD
  RELEASED from the Misc9 stub; Dice-skeleton + second B-side
  XNEXT walk; sibling of unit-175. Consult 22:41:00Z.
- 270 include/mumpsc/cgi.h::<misc> (CgiMisc.kt): cgiInitD; QS_MAX_D
  2048 / QS_MAX1_D 2047; the symRetDaf(0/11) seat calls; %hh
  decode, dup counter, shift arm. Consult 22:41:30Z.
- 271 interp-sql.cpp::sql (InterpSql.kt): BOTH sqlDaf overloads
  RELEASED: String seat (DafInterpStubsT3) and CharArray-out seat
  (the InterpSqlBtree raise note superseded); java.sql JDBC
  "jdbc:sqlite:mumps.sqlite" DOCUMENTED DIVERGENCE; engine-absent
  fold -> vendor banners + sigintD(100) + SQL_BAD_D; the
  BEGIN 25 / COMMIT 26 / ROLLBACK 27 / SAVEPOINT 28 / PRAGMA 29
  ladder pinned as consts; SQL_OK_D 1 / SQL_BAD_D 0; callBackD
  latch with rsltCallbackD per-row. Consult 22:42:00Z.
- 272 mpstime.cpp::<misc> (MpstimeMisc.kt): numcomp2D, strxcatDaf
  RELEASED (MpstimeMain stub deleted; the unit-182 call at line
  ~205 binds; the seat returns the joined String), strcatzD,
  syncClockD + DBSemD, setAlarmD (JVM zero-timer ledger),
  labelLookupDefaultD/textFunctionDefaultD; floatstring rides the
  existing floatstringDaf stub, no duplicate. Consult 22:42:30Z.
- 273 interp-native-btree.cpp::<misc> (InterpNativeBtreeMisc.kt):
  BigKey* statics pinned; memxmoveD, strxxcpyD, flushD, strxcmpD
  (returns Pair), allocblkD; btreeReadIntLE/btreeWriteIntLE
  public 4-byte LE helpers; bfrGetD/bfrEachD accessors +
  writeBlockD made public in InterpNativeBtreeFreadx.kt. Consult
  22:43:00Z.
- 274 compiler-kill.c::kill_code (KillCode.kt): killCodeD; the
  goto web folded on labeled loops; seats atTest / parseDaf(b3) /
  cvarDaf / extract1Daf via a local pd1-sdlim copy helper /
  setCodeDaf (SET_CODE_SET_D, NOT SET_STORE_D) / extract2Daf
  raised / KILL_DCL_ERROR_D 7 pinned; the KILL-arm g-arms emit
  text verbatim. Consult 22:43:30Z.
- 275 interp-native-btree.cpp::btreeX (BtreeX.kt): btreeXD,
  1000+ lines; scanblocksD rendered as a same-TU helper per row 90
  (the chunker units.csv gap; body verified vendor 216-259);
  consts BTR_PREVIOUS_D 9 / BTR_RESTORE_D 20 / BTR_NEXTMARK_D /
  TRXLIMIT_D / DBL_STR_D / BLOCK_SPLIT_D; KILL_D 3 alias used
  for GKILL; gbldatD RandomAccessFile; open/create/reopen fold;
  8-byte LE readOffT/writeOffT; the btreeXDDescent/btreeXDFail
  helper re-entry folds for the goto weave; KNOWN APPROXIMATIONS
  disclosed in the header quirks ledger. Consult 22:44:00Z.
- 276 libmpscpp.cpp::<misc-3> (LibmpscppMisc3.kt): the row-98
  overloads collapse: the C++ operator <=/==/!= lattices (600
  LOC) collapse to globalLeD/globalLeStrD/globalEqD(3)/
  globalEqRevD(3)/globalNeD(3)/globalNeRevD(3)/globalEqNodesD/
  globalNeNodesD + strcmpD/bVal/bCmp private helpers; quirk: the
  string-side free operator<= and member == have INVERTED strcmp
  results kept verbatim and ledgered; the mstring operator=
  ledger only. Consult 22:44:30Z.
- 277 compiler-parse.c::parse_ (CompilerParseC.kt): parseCDaf on
  its OWN seat over DafCompilerState globals; the
  code/opcode/ncode/dcode/operand table lattice verbatim; the
  goto web folded on a label-driven state walk (the H-010
  structural fold, BtreeX/KillCode precedent); the globals
  build + dollar-intrinsic ladder + unary/binary operator
  emitters; RAISES patrn1Daf, fixstrDaf, expandQSDaf,
  expandNextDaf, expandGetDaf, myFuncDaf; the parseDaf b3 interp
  seat STAYS RAISED; the vendor zseek call-site candidate rebind
  is flagged below. Consult 22:45:00Z.

## Seat ledger of the batch

Releases: horologDaf (247), xindexDaf (260), fixGblRefD (262,
tag corrected), GlobalNodeStub avg member (263), evalDaf (264),
linepadDaf + linepad2Daf + multDaf (267, two tag corrections),
perlPmD (268, tag corrected), jaccardD (269), sqlDaf x2 both-arm
seats (271, the InterpSqlBtree raise note superseded), strxcatDaf
(272; MpstimeMain stub deleted). Raises: extract2Daf stays
raised and is called (265); six raises for the parse_ render
(277): patrn1Daf, fixstrDaf, expandQSDaf, expandNextDaf,
expandGetDaf, myFuncDaf. Promotions: copyStateDaf package-level
(InterpService, 263), emitInterpretDaf public (SetCode, both
copies), bfrGetD/bfrEachD accessors + writeBlockD public
(InterpNativeBtreeFreadx, 273). Hygiene-normalized pre-existing
comment dollars (compile-identical \u0024 escapes): SetCode.kt 4
lines, ForCode.kt 2, LibmpscppMisc9.kt 2, GlobalOrderH.kt 1,
MpstimeMain.kt 9, RpmMisc.kt 1. Compile-blocker fixes ledger
(the batch's red builds): the BtreeX is-keyword rename (isCmp),
the duplicate siStrlen removal (InterpService), the
emitInterpretDaf publish, the itoaD param rename, the atoi
top-level seat fix (DafCRuntimeT2), the datlen init.

## Gates

Scripted 4-stage gate (C-010 gate.sh), ONE batch run per C-011:
stage 1 kotlinc gate copy exit 0 (6 accepted Interpret.kt
vendor-lattice warnings); stage 2 repo-copy recompile exit 0 at
177 kt files (151 post-batch-8 + 26 new this batch); stage 3
dafny per-spec loop 70 verified 0 errors over 11 specs (no new
specs; all 31 batch-9 units EXEMPT or residue); stage 4 H-015
hygiene clean on the 43 rendered/edited files (backtick, raw
comment dollars, trailing newline; the QUOTE define of the
parse_ render is the \u0060 escape so the file stays
backtick-free).

## ADJUDICATION FLAGS for the PI (disclosed, no code change)

- STR_MAX blame (248): the vendor bstrcat blames strmax.h for a
  32000 bound the reference build caps at STR_MAX 500. No const
  change; disclosed for the record.
- scanblocksD chunk gap (275): units.csv carries no own row for
  the btreeX scanblocks chunker (vendor 216-259); it renders as
  a same-TU helper per row 90 with the body physically verified.
  The BtreeX header carries the KNOWN APPROXIMATIONS ledger
  (structural folds of the block-cache arms) for PI review.
- Row-98 inverted-compare quirks (276): the string-side free
  operator<= and the member == have INVERTED strcmp results
  relative to the other arms of the lattice; kept vendor-verbatim
  and flagged for review.
- parse_ seat + zseek rebind (277): parse_ (compiler-parse.c,
  1201 LOC) renders as parseCDaf over the compiler globals; the
  interpreter seat parseDaf (interp-parse.cpp, 584 LOC, roster
  b3) STAYS RAISED. The one candidate call site
  (CompilerZcommandsCode zseek, currently parseDaf(
  DafInterpState.svnPtr)) is NOT rebound this batch: whether it
  should bind parseCDaf(compiler state, svnPtr) awaits Mark's
  ruling. Per the C-012 adjudication the CodedOpen/CodedClose/
  CodedComma family maps NAME-TO-NAME onto the arm consts, so the
  array-setup emit %d prints the arm-inverted pair (arm-internal
  symmetric).
- heuristic-log CSV anomaly (pre-existing): a csv column-count
  scan this session found rows 134-146 of
  wp2/logs/DAF_heuristic_log.csv parse with non-11 column counts
  (embedded-comma quoting from an earlier batch). Shipped
  unchanged through earlier close gates; flagged for a possible
  data-quality repair at a later close.

## Files pushed (paths repo-relative)

- 17 new kt files under wp2/arms/DAF/src/main/kotlin/daf/:
  InterpBstring.kt, InterpStrmanipXindex.kt,
  InterpServiceMisc.kt, LibmpscppGlobalAvg.kt, InterpSymEval.kt,
  CompilerExpandName.kt, InterpSymMisc.kt, CompilerSys1Misc.kt,
  LibmpscppJaccard.kt, CgiMisc.kt, InterpSql.kt, MpstimeMisc.kt,
  InterpNativeBtreeMisc.kt, KillCode.kt, BtreeX.kt,
  LibmpscppMisc3.kt, CompilerParseC.kt
- edited under the same dir (releases, promotions, tag flips,
  hygiene normalizations): LibmpscppMisc6.kt, ZfcnsH.kt,
  LibmpscppMisc7.kt, GlobalNodeStub.kt, InterpService.kt,
  DafInterpStubsT3.kt, SetCode.kt, ForCode.kt,
  CompilerFcninit.kt, Cannon.kt, RpmMisc.kt, LibmpscppMisc9.kt,
  InterpSqlBtree.kt, MpstimeMain.kt, InterpNativeBtreeFreadx.kt,
  GlobalOrderH.kt, MdhTreeLevelAdd.kt
- wp2/logs/DAF_heuristic_log.csv (ids 258-290 appended: roster
  rows 247-277, seats row 289, gate row 290)
- wp2/PILOT_TRACKING.md (the new Batch 9 section: the DAF batch-9
  note paragraph + rows 247-277 with the DAF column marked
  translated)
- this fragment (wp2/arms/DAF_B9_fragment_2026-11-24.md)

## Next

Mark's splice: cat this fragment >> the arm journal (wp2/arms/
ARM_WORKING_NOTES.md; expect 10987 + N lines), rm in a separate
commit; then the repo-copy gradle + dafny (70) round. This
batch-9 close leaves the DAF arm at 277/308 translated. Next
protocol step: DAF batch 10 per the committed row order, one
session per batch per C-011.
