# DAF B7 tranche 3 (units 206-215, complex stratum) - 2026-11-24

DAF batch 7 closes here: T1 (186-195, simple) and T2 (196-205,
core) landed earlier this session; T3 renders the complex stratum
206-215 in batch_plan.md order. All ten dumps loc-asserted via
~/b4 dump4.py against want_t3.json. This tranche is the FIRST under
the two protocol changes the PI adopted this session: C-010 (the
gate and push-verification steps are script-driven: gate.sh runs
the 4-stage tranche gate, sha-stage.sh the blob verification) and
C-011 (one session per batch for batches 8-10). The C-010/C-011
PROTOCOL_CLARIFICATIONS.md entries ride this push.

## Units rendered (9 new kt files + 1 ledger append, package daf)

- 206 compiler-mumps.c::<misc> (CompilerMumpsMisc.kt): the
  compiler-side residue: EP pin, mainFlgD, the label/text chain
  heads with clearLabelChainsD, and the levCheck body - LEV SHADED
  CORRECTION: the levCheckDaf "not a corpus unit" blame was wrong
  (the body is the residual cell of this row-206 chunk; SEAT
  RELEASED at DafInterpStubsT3:203, Doty.kt call sites rebound).
  TxtNodeD carrier introduced here and CORRECTED at the unit-213
  render to the vendor struct txt {name,lnbr,next} of
  compiler-globals.h:61 (the NmesNodeD-to-TxtNodeD rename
  adjudication). Consult 20:10:00Z: rows 37/62/6/22.
- 207 compiler-fcninit.c::fcninit (CompilerFcninit.kt): the
  emit-side preamble; Sysout(x)==fprintf(Out,x), the Sysout_skip
  "\n" pinned from compiler-macros.h, linepad2D + functionNameD
  raises. Consult 20:10:30Z: rows 27/62.
- 208 interp-sym.cpp::Qsub (InterpSymQsub.kt): the q-subscript
  push-state walk over the GlobalQuery copyState scaffold; gpad
  save/restore, j==-1/-2 arms, atoi tail, the gblflg
  caret-target copy. Consult 20:11:00Z: rows 119/81/80/33.
- 209 interp-native-btree.cpp::freadx (InterpNativeBtreeFreadx.kt):
  the block-io pair: BfrBlockD model (gblbf/gblbfx/gblmod), the
  SPARSE bfrD HashMap DOCUMENTED DIVERGENCE (vendor GBLBUF 65537
  full array), gblfdD RandomAccessFile seat, byte assembly
  read/write, BfrRef out-param fold, PAGE_SHIFT 11/AMASK
  0xffff/GBLBUF/BTR consts. Consult 20:11:30Z: rows 56/91.
- 210 compiler-zcommands.c::zcommands_code (CompilerZcommandsCode.kt):
  the Z router; NEXT_LINE_D=8 raise (PARSE_ERROR_D/ABRTX_D
  already seated); the pending-carrier lattice (p2pxD/fcnFlgD/
  fFlgD/exitCodeD/cvarxD/pidD/outputFileD) resolves at consumers;
  the %% escapes decoded at the OUTPUT level (one percent; six
  percents print %%%d); the errors.h ladder verbatim incl. the
  vendor "Interprerer Error" misspelling. Consult 20:12:00Z:
  rows 27/83.
- 211 interp-sysfunc.cpp::sigint (InterpSysfuncSigint.kt): the
  SIGINT dispatcher real body; pinned 100..133 codes;
  raise(SIGKILL)/exit folds via exitProcess; the svPtr-null arm
  comment-only per the malloc-NULL precedent; the GlobalNodeStub
  sigintDaf stub RELEASED to a delegate; TAG CORRECTION applied
  at StrmanipMisc.kt:30-32 (sigint is roster 211 of THIS batch,
  not batch 10). Consult 20:12:30Z: rows 62/56.
- 212 compiler-sys1.c::match (Match.kt): LEDGER APPEND discharge:
  the sys1 twin verified line-for-line identical to the landed
  unit-180 body modulo casts/formatting and the bd global-vs-param
  fold; the xindex punct tables byte-identical. The Match.kt
  punct-table backtick is VENDOR STRING DATA (landed B6): excluded
  from the H-015 hygiene list with this note. Consult 20:13:00Z:
  rows 180/49/186.
- 213 compiler-epi.c::epi (CompilerEpi.kt): the epilogue emitter:
  epiD real body (epiDaf raised then RELEASED, delegating);
  label/text table generators over the CORRECTED TxtNodeD
  carrier; cvarsD/DclsEntryD + CVARMAX 100/FBMAX 4000 +
  maxTmpD 2/argMaxD 0/iflgMaxD 0 raises; the runSidecar fix (the
  vendor if/else picks ONLY the name; the decl loops are SHARED
  after the open); abort() on the function-path open failure via
  exitProcess(134); the state reset block; the errors.h ladder
  verbatim. Consult 20:13:30Z: rows 27/62/6/22.
- 214 libmpscpp.cpp::<misc-6> (LibmpscppMisc6.kt, 600 LOC): the
  wrapper-library residue: the mcvt/cvt numeric ladder, the free
  count at libmpscpp.cpp:2932 (countDaf SEAT RELEASED: the
  MakeGlobal.kt stub of T2 deleted with a release note; call site
  resolves), HitRatio (integer-division quirk kept), the EncodeHTML
  cover over a named stub (DOC unit-5 kin, not drawn on this arm),
  global::Name/__test, the ReadLine family (the global twins keep
  the vendor store-on-EOF truth; FILE*/istream twins fold onto
  Reader, H-016), the mstring conversion-operator ladder (H-010
  toD fold; the operator char() atoi-into-char quirk kept), the
  zBlanks pair over zfcnDaf, the Ascii family (signed-char domain
  note), the Length members (the needle triple IS the landed
  unit-129 render), Horolog over a named _horolog stub (DOC b9
  row 278 kin), global::get, the operator<< no-seat ledger,
  global::Merge's 128-quirk walk over mergeDaf, Lock/UnLock over
  makeLockDaf/cleanLocksDaf, CleanLocks/CleanAllLocks (JVM
  delete fold), the Set family over a raised setGlobalDaf seat,
  the Get family (the stale-scratch vendor quirk reads the filled
  carrier, H-012), Kill over a raised killDaf seat (DOC unit-37
  kin), the Order family, Data relaying the landed globalDataD
  render, the Assign family (the char* backend builds
  "set <data>=\"<b>\"" through InterpretDaf; covers fold onto the
  String seat), the SQL relay family (string/mstring twins
  collapse; SQL_Table(char*,int) outside the cut rides a named
  stub), the operator& forwards over a concatDaf named stub (the
  Concat x7 bodies beyond the tail cut). Chunk-boundary orphans
  both edges ledgered (the head tail-completion of the Misc-5
  mcvt char* twin; the tail Concat opener). Consult 20:14:00Z:
  rows 56/112/83/92/101/33/98/26.
- 215 interp-service.cpp::ServiceInterface (InterpService.kt, 279
  LOC): the service entry: the GETENV/SCANALNUM/SCAN/INPUT
  early-return arms (the SV-bound wrappers not drawn on this arm:
  named stubs), the AllocSV push-state over the copyState carrier
  (unit-119/Qsub fold), the parse/keyfix prologue repeated
  VERBATIM per token arm (vendor copy-paste kept), the wrapper
  battery hand-offs (CosineW/Sim1W/JaccardW/CentroidW/DiceW/
  TermCorrelateW/DocCorrelateW, SumW, Min, Count, Transpose, AvgD
  - all landed renders; MMULTIPLY/IDF/MAX as named stubs), the
  GBLNME Mglobal walk with the dead f capture, the INVDF strlen+1
  second-NUL quirk and the tmp1-unparsed tmpB, the nullable
  tmp4/tmp5 with backstop raises (RsltCallback name0 precedent),
  the trailing tpx hand-off and the free()/GC no-op.
  serviceInterfaceDaf SEAT RELEASED to a delegate at Zfcns.kt;
  SymHMisc.kt:38 TAG CORRECTION (the "not a roster unit" blame
  was wrong; roster row 187 owns the seat). Consult 20:14:30Z:
  rows 119/22/81/56/92.

## Seat ledger of the tranche

Releases: levCheckDaf (206), epiDaf (213), sigintDaf (211),
countDaf (214), serviceInterfaceDaf (215). Raises:
linepad2D/functionNameD (207); bfrD/gblfdD/PAGE_SHIFT_D/AMASK_D/
GBLBUF_D/BTR (209); NEXT_LINE_D and the p2px/fcnFlg/fFlg/
exitCode/cvarx/pid/outputFile pending carriers (210);
TxtNodeD/cvarsD/DclsEntryD/CVARMAX_D/FBMAX_D/maxTmpD/argMaxD/
iflgMaxD (213); setGlobalDaf/concatDaf/encodeHtmlDaf/killDaf/
horologDaf/sqlTableXDaf (214); scanAlnumSvD/scanSvD/inputSvD/
multiplyW/idfW/maxW (215). Tag corrections: StrmanipMisc.kt:30-32
(211) and SymHMisc.kt:38 (215). Hygiene-normalized pre-existing
comment dollars in touched files: DafInterpStubsT3:221,
GlobalNodeStub:59.

## Gates

First fully scripted gate run (C-010: gate.sh): stage 1 kotlinc
gate copy exit 0 (6 accepted Interpret.kt warnings); stage 2
repo-copy recompile exit 0 at 124 kt files; stage 3 dafny
per-spec loop 70 verified 0 errors (no new specs; all ten T3
units EXEMPT or residue); stage 4 H-015 hygiene clean on the 12
rendered/edited files (Match.kt excluded with the vendor-string
note). The scripted run caught 1 authored string backtick
(Freadx) + 2 authored comment backticks (Zcommands, Epi) plus the
2 pre-existing comment dollars above; all normalized before the
green run; the hygiene probe refined to comment-dollars only
(Kotlin string templates are legitimate).

## CORRECTION carried by this push

The T2 close prose (the PILOT tranche-2 paragraph, the T2
fragment, and log row 212) said 116 files; the true post-T2 kt
count was 115 (9 new files on the 106 of T1). The error was
prose-only (no CSV cell carries it); the correction note rides
the PILOT tranche-3 paragraph and this fragment; the 124-file
count of this tranche (115 pre-batch + 9 new T3 files; the
unit-212 Match append added no file) is the corrected arithmetic.
Batch-7 file-count chain: 106 -> 115 -> 124 with 10 units per
tranche and one ledger append.

## Files pushed (paths repo-relative)

- wp2/arms/DAF/src/main/kotlin/daf/CompilerMumpsMisc.kt (new)
- wp2/arms/DAF/src/main/kotlin/daf/CompilerFcninit.kt (new)
- wp2/arms/DAF/src/main/kotlin/daf/InterpSymQsub.kt (new)
- wp2/arms/DAF/src/main/kotlin/daf/InterpNativeBtreeFreadx.kt (new)
- wp2/arms/DAF/src/main/kotlin/daf/CompilerZcommandsCode.kt (new)
- wp2/arms/DAF/src/main/kotlin/daf/InterpSysfuncSigint.kt (new)
- wp2/arms/DAF/src/main/kotlin/daf/CompilerEpi.kt (new)
- wp2/arms/DAF/src/main/kotlin/daf/LibmpscppMisc6.kt (new)
- wp2/arms/DAF/src/main/kotlin/daf/InterpService.kt (new)
- wp2/arms/DAF/src/main/kotlin/daf/Match.kt (edit: unit-212
  carrier-twin ledger append)
- wp2/arms/DAF/src/main/kotlin/daf/DafInterpStubsT3.kt (edit:
  levCheck release note; comment-dollar hygiene)
- wp2/arms/DAF/src/main/kotlin/daf/Doty.kt (edit: levCheckDaf
  call rebind)
- wp2/arms/DAF/src/main/kotlin/daf/GlobalNodeStub.kt (edit:
  sigintDaf delegate; comment-dollar hygiene)
- wp2/arms/DAF/src/main/kotlin/daf/StrmanipMisc.kt (edit: sigint
  tag correction)
- wp2/arms/DAF/src/main/kotlin/daf/SymHMisc.kt (edit:
  ServiceInterface tag correction)
- wp2/arms/DAF/src/main/kotlin/daf/Zfcns.kt (edit:
  serviceInterfaceDaf release to delegate)
- wp2/arms/DAF/src/main/kotlin/daf/MakeGlobal.kt (edit:
  countDaf release note)
- wp2/logs/DAF_heuristic_log.csv (ids 213-224 appended; base
  prefix byte-verified)
- wp2/PILOT_TRACKING.md (batch-7 rows 206-215 DAF column marked
  translated; the DAF B7 tranche-3 note paragraph before the
  table, carrying the batch-7 close note and the 115/116
  correction)
- wp2/PROTOCOL_CLARIFICATIONS.md (the C-010/C-011 entries,
  appended this session)
- this fragment (wp2/arms/DAF_B7T3_fragment_2026-11-24.md)

## Next

Mark's splice: cat this fragment >> the arm journal (wp2/arms/
ARM_WORKING_NOTES.md; expect 10550 + N lines), rm in a separate
commit; then the repo-copy gradle + dafny (70) round. This T3
close IS the DAF batch-7 close (30/30). Next protocol step: DAF
batch 8 per the committed row order, one session per batch (the
C-011 protocol change registered with this push).
