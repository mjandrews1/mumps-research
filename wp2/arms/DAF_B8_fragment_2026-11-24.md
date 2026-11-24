# DAF batch 8 (units 216-246, all strata) - 2026-11-24

DAF batch 8 in ONE session per the C-011 protocol change: all 31
units (216-246) rendered this session, in the pre-registered
batch_plan.md order. All dumps loc-asserted via ~/b4 dump4.py
against the batch-8 WANT dict. This batch close carries two PI
adjudication flags (the CODED-lattice name inversion and the
DOC-vs-DAF batch-8 tail-id divergence; section below) and one env
disclosure (the sandbox wiped mid-close; the scripted gate re-ran
clean from the knowledge checkpoints with identical counts, and
the heuristic append ids 225-257 was rebuilt from the session
ledger).

## Units rendered (27 new kt files + 10 edits, package daf)

- 216 include/mumpsc/builtinTable.h::<misc> (BuiltinTableH.kt): the
  internal built-in function name table: 19 verbatim entries with
  the vendor trailing empty-string entry kept. Consult 21:10:00Z.
- 217 libmpscpp.cpp::mstring::Find (MstringFind.kt): the
  MSTRING-needle member: receiver String + MpsGlobalNode key
  carriage, Mltoa on the line-number slot before the walk. Consult
  21:10:30Z.
- 218 compiler-kill.c::<misc> (CompilerKillMisc.kt): banner-residue
  chunk -> comment ledger file. Consult 21:11:00Z.
- 219 compiler-lock.c::<misc> (CompilerLockMisc.kt): same
  disposition as 218; only the date line differs among the three
  banners. Consult 21:11:30Z.
- 220 compiler-set.c::<misc> (CompilerSetMisc.kt): same disposition
  as 218/219; the three-way date-line-only divergence disclosed.
  Consult 21:12:00Z.
- 221 include/mumpsc/zfcns.h::<misc> (ZfcnsH.kt): the z-function
  prototype roster: per-prototype DAF seat map (_justify ->
  InterpJustify u194, _piece -> the landed u188 piece family, each
  forward named to its landed or raised seat). Consult 21:12:30Z.
- 222 compiler-mumps.c::atCopy (CompilerAtCopy.kt): the xpx-walk
  copy helper; atCopyDaf(atFlg,tmp) SEAT RELEASED (stub deleted
  from DafInterpStubsT3 with a release note; the unit-215 raise
  resolves). Consult 21:13:00Z.
- 223 libmpscpp.cpp::IDF (Idf.kt): the IDF body (banner +
  sigintD(100), strtod seat); the idfW stub (unit-215 raise)
  resolves to the real member. Consult 21:13:30Z.
- 224 libmpscpp.cpp::global::Piece (GlobalPiece.kt edit):
  VERIFICATION-ONLY row: :7181 body folds line-for-line onto the
  landed u188 char* twin; the header note records the adjudication.
  Consult 21:14:00Z.
- 225 libmpscpp.cpp::GlobalData (GlobalData.kt edit): the string
  twin -> String seat fold (H-015); verification-only ledger row
  (u186 precedent); pre-existing comment dollars normalized while
  touching. Consult 21:14:30Z.
- 226 libmpscpp.cpp::mdh_tree_level_add (MdhTreeLevelAdd.kt edit):
  vendor :534 physically verified; tree/depth + col1..col9 ->
  buildZCallD envelope -> zfcn relay (the 4/2/1-column vendor counts
  kept); pre-existing comment dollars normalized. Consult 21:15:00Z.
- 227 libmpscpp.cpp::global::ReadLine (LibmpscppMisc6.kt edit):
  VERIFICATION-ONLY row against the landed globalReadLineRD
  (Reader) twin: getline fold, CR-LF strip, the store-on-EOF
  vendor truth kept. Consult 21:15:30Z.
- 228 compiler-parse.c::ExpandText (ExpandText.kt): the peel-walk
  over the DafCompilerState payload carriers; CODED_OPEN 208 -> d0
  per the MstringFnumber const seats; sprintfInt228D helper names
  the tmp percent-d emit. Consult 21:16:00Z.
- 229 interp-sym.cpp::ScanParse (ScanParse.kt): stub-seat release:
  real body, same name/signature; scanParseDaf released. Consult
  21:16:30Z.
- 230 interp-parse.cpp::BuildGlobal (BuildGlobal.kt): the g,a
  with-args global-build dispatch; buildGlobalDaf stub-seat
  release. Consult 21:17:00Z.
- 231 interp-sym.cpp::BuildLocal (BuildLocal.kt): buildLocalDaf
  stub-seat release: real body + holder raise + new stub seat; the
  vendor fff debug fossils kept as comments. Consult 21:17:30Z.
- 232 compiler-parse.c::Extract2a (Extract2a.kt): the peel-walk
  over the DafCompilerState payload carriers (pd1/sdlim slots ride
  the state fields); hit-reuse of the Expand-family emit
  conventions. Consult 21:18:00Z.
- 233 interp-zfcns.cpp::<misc> (InterpZfcnsMisc.kt):
  header/forward residue -> ledger (u236 precedent); each forward
  names its landed or upcoming seat. Consult 21:18:30Z.
- 234 compiler-sys1.c::getstr1 (CompilerGetstr1.kt): the stdin read
  helper (opnflg, opnfile carriers); stdin via the Kotlin keyword
  escape System.\u0060in\u0060 (Interpret.kt precedent; the escape is
  exempt from the hygiene backtick probe). Consult 21:19:00Z.
- 235 interp-sql.cpp::<misc> (InterpSqlMisc.kt): chunker recomputed;
  file residue: commandPrintD (SOH -> # debug view, row-56
  static-return fold). Consult 21:19:30Z.
- 236 include/mumpsc/compiler-macros.h::<misc> (CompilerMacrosH.kt):
  pure ledger: set_code u121 / for_code u122 / lock_code u116
  LANDED; error_code/abtrx/if_code/copyright_notice seats named.
  Consult 21:20:00Z.
- 237 interp-sysfunc.cpp::Merge (Merge.kt): the fresh Merge body
  (tmp4 swap; file-local strcpy/strlen/strncmp/spliceAt helpers);
  mergeDaf lands (the unit-214 raise resolves). Consult 21:20:30Z.
- 238 interp-bifs.cpp::GtkTreeAdd (GtkTreeAdd.kt): stub-seat
  release: real body + shim seat raise (gtkTreeAddDaf); the
  vendor dead nargs==10 check + drifting column comments kept
  verbatim. Consult 21:21:00Z.
- 239 include/mumpsc/builtin.h::<misc> (BuiltinH.kt): the forward
  tags of the builtin roster (the unit-216 table render is the body
  carrier). Consult 21:21:30Z.
- 240 interp-parse.cpp::cannon (Cannon.kt): tmp[25] bound guarded
  at index 24; copyFromCannonD/shiftLeftCannonD file-local helpers;
  DOC's landed cannon render is the cross-arm mirror. Consult
  21:22:00Z.
- 241 compiler-open.c::mumps_open (MumpsOpen.kt): the file-open walk
  over DafCompilerState; extract2Daf pd1/sdlim payload stub raised;
  cstringAt tmp0 compare; POST_flg tail; the emitted text
  byte-verbatim. Consult 21:22:30Z.
- 242 include/mumpsc/compiler-globals.h::<misc> (CompilerGlobalsH.kt):
  pure disposition ledger: the compiler file-scope state ALL carried
  by DafCompilerState (t2/ierr/sdlim/pd1/xd/xpx/out/emit/lines/
  forx/forxmax/IF_flg/ForBlocks/ExecName). Consult 21:23:00Z.
- 243 compiler-parse.c::<misc> (CompilerParseMisc.kt): the parser
  file-head residue (banner + Fortran-heritage block + include
  ladder + define lattices + prototype/externs ledger); RELEASES
  memcheckDaf/cvarDaf/extract1Daf with the fold-in bodies;
  conCheckD/strcasecmpD/strncasecmpD helpers; the CODED-lattice
  adjudication below. Consult 21:23:30Z.
- 244 interp-sysfunc.cpp::AllocSV (InterpSysfuncAllocsv.kt): the
  state-vector constructor; allocSVDaf SEAT RELEASED (four call
  sites resolve: Fcn.kt:189, GlobalQuery.kt:44 and the rest);
  once-only gate, HSF, SQLITE arm live, percent-g/ld/d decode,
  console-default handle 1, rsltSpec. Consult 21:24:00Z.
- 245 libmpscpp.cpp::<misc-7> (LibmpscppMisc7.kt + LibmpscppMisc6.kt
  edit): the Concat(x7) families real (concatDaf collapsed stub
  DELETED; the twelve amp relays of Misc6 reshaped with sv params;
  TAG CORRECTION note in the Misc6 header; no external callers
  existed); mstring Soundex (zfcn), one-arg Translate
  (mstringTranslate1D via dafFcn), Reverse/Qsubscript/Qlength/
  Query (evalDaf; rsltSpecD), globalAs* conversion ladder
  (node-value model per Misc5/row-18, NOT Mglobal RETRIEVE),
  mul/gt/lt/shl families (the %ud quirk folds to %d, DOCUMENTED
  DIVERGENCE); NEW stub fixGblRefD; the misc-7 misc-6 tail-tag
  corrected. Consult 21:24:30Z.
- 246 interp-sql.cpp::btree (InterpSqlBtree.kt + Mglobal.kt edit):
  the SQLITE global-array workhorse; btreeDaf SEAT RELEASED from
  Mglobal.kt - TAG CORRECTION: the interp-native-btree blame was
  wrong (SQLITE binds btree to the interp-sql body, a roster row of
  THIS batch); the six g-arms verbatim (RETRIEVE, STORE
  delete+insert, ORDERNEXT+ORDERPREV, XNEXT 14==14 live arm, GKILL
  shadow-quirk, GBLNME, CLOSE); per-arm ub and iIns/iXn naming
  fixes; java.io tmp files. Consult 21:25:00Z.

## Seat ledger of the batch

Releases: atCopyDaf (222), idfW (223), scanParseDaf (229),
buildGlobalDaf (230), buildLocalDaf (231), mergeDaf (237, the
unit-214 raise resolves), gtkTreeAddDaf (238), allocSVDaf (244),
concatDaf (245, collapsed stub DELETED), btreeDaf (246, Mglobal
stub removed), memcheckDaf/cvarDaf/extract1Daf (243, fold-in
bodies; DafInterpStubsT3 header-ledger updates). Raises: extract2Daf
named stub (241), fixGblRefD (245), sprintfInt228D (228),
conCheckD/strcasecmpD/strncasecmpD (243),
copyFromCannonD/shiftLeftCannonD (240). Tag corrections: btreeDaf
interp-native-btree blame (246) and the misc-7 misc-6 tail tag
(245); the StrmanipMisc/GlobalNodeStub roster tags verified
current. Hygiene-normalized pre-existing comment dollars:
GlobalPiece.kt:9, GlobalData.kt:4/34, MdhTreeLevelAdd.kt:5/26/47.

## Gates

Scripted 4-stage gate (C-010 gate.sh), ONE batch run per C-011:
stage 1 kotlinc gate copy exit 0 (6 accepted Interpret.kt
vendor-lattice warnings); stage 2 repo-copy recompile exit 0 at
151 kt files (124 pre-batch + 27 new); stage 3 dafny per-spec loop
70 verified 0 errors over 11 specs (no new specs; all 31 batch-8
units EXEMPT or residue); stage 4 H-015 hygiene clean on the 37
rendered/edited files. Hygiene probe refinements this batch: the
Kotlin keyword escape System.\u0060in\u0060 is exempt from the backtick
probe (CompilerGetstr1 via the Interpret.kt precedent) and the
dollar probe stays comment-dollars only. ENV RESET DISCLOSURE: the
sandbox wiped mid-close AFTER the first green scripted run; the
sandbox was rebuilt from the knowledge checkpoints and the gate
re-ran clean with identical counts (151 files, dafny 70/0, hygiene
clean on 37).

## ADJUDICATIONS FLAGS for the PI (disclosed, no code change)

- CODED-lattice: all four vendor defines.h copies set CodedClose
  207 / CodedComma 208 (defines.h:38-41). DAF's
  MstringFnumber.kt:17-19 CODED_COMMA_D=207 / CODED_CLOSE_D=208
  inverts the two NAMES but keeps the vendor values; it is
  arm-internal and symmetric (Zfcns/ZfcnBifs CLOSE_ZB=207 /
  COMMA_ZB=208 align with the vendor values). Disclosed for PI
  ruling; no code change pending.
- Batch-8 tail-id divergence: DAF numbers the fresh batch-8 tail
  237=Merge / 240=cannon parse / 241=mumps_open / 243=compiler-parse
  misc / 244=AllocSV / 245=libmpscpp misc-7 / 246=interp-sql btree
  per the pre-registered batch_plan.md order; DOC numbers its own
  batch-8 tail differently per its committed order. PI adjudication
  requested so cross-arm comparisons stay like-for-like.

## Files pushed (paths repo-relative)

- 27 new kt files under wp2/arms/DAF/src/main/kotlin/daf/:
  BuiltinTableH.kt, MstringFind.kt, CompilerKillMisc.kt,
  CompilerLockMisc.kt, CompilerSetMisc.kt, ZfcnsH.kt,
  CompilerAtCopy.kt, Idf.kt, ExpandText.kt, ScanParse.kt,
  BuildGlobal.kt, BuildLocal.kt, Extract2a.kt, InterpZfcnsMisc.kt,
  CompilerGetstr1.kt, InterpSqlMisc.kt, CompilerMacrosH.kt,
  Merge.kt, GtkTreeAdd.kt, BuiltinH.kt, Cannon.kt, MumpsOpen.kt,
  CompilerGlobalsH.kt, CompilerParseMisc.kt,
  InterpSysfuncAllocsv.kt, LibmpscppMisc7.kt, InterpSqlBtree.kt
- wp2/arms/DAF/src/main/kotlin/daf/GlobalPiece.kt (edit:
  verification-only twin note + comment-dollar hygiene)
- wp2/arms/DAF/src/main/kotlin/daf/GlobalData.kt (edit:
  verification-only twin note + comment-dollar hygiene)
- wp2/arms/DAF/src/main/kotlin/daf/MdhTreeLevelAdd.kt (edit:
  mdh append + comment-dollar hygiene)
- wp2/arms/DAF/src/main/kotlin/daf/LibmpscppMisc6.kt (edit:
  ReadLine verification note + the reshaped amp relays + the misc-7
  tail-tag correction)
- wp2/arms/DAF/src/main/kotlin/daf/DafInterpStubsT3.kt (edit:
  atCopy/scanParse/buildGlobal/buildLocal/allocSV releases +
  memcheck/cvar/extract1 fold-in release notes)
- wp2/arms/DAF/src/main/kotlin/daf/InterpService.kt (edit)
- wp2/arms/DAF/src/main/kotlin/daf/Interpret.kt (edit)
- wp2/arms/DAF/src/main/kotlin/daf/GlobalNodeStub.kt (edit)
- wp2/arms/DAF/src/main/kotlin/daf/Zfcns.kt (edit)
- wp2/arms/DAF/src/main/kotlin/daf/Mglobal.kt (edit: btreeDaf
  release; stub removed)
- wp2/logs/DAF_heuristic_log.csv (ids 225-257 appended; base
  prefix byte-verified; the append rebuilt after the env reset as
  disclosed in the gate row)
- wp2/PILOT_TRACKING.md (the new Batch 8 section: the DAF batch-8
  note paragraph + rows 216-246 with the DAF column marked
  translated)
- this fragment (wp2/arms/DAF_B8_fragment_2026-11-24.md)

## Next

Mark's splice: cat this fragment >> the arm journal (wp2/arms/
ARM_WORKING_NOTES.md; expect 10752 + N lines), rm in a separate
commit; then the repo-copy gradle + dafny (70) round. This batch-8
close leaves the DAF arm at 246/308 translated. Next protocol step:
DAF batch 9 per the committed row order, one session per batch per
C-011.
