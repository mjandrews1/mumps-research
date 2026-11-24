# DAF B7 tranche 2 (units 196-205, core stratum) - 2026-11-24

DAF batch 7 continues per the committed row order (PILOT units
186-215). T2 renders the core stratum 196-205 in batch_plan.md
order. All ten dumps loc-asserted this session via ~/b4 dump4.py
against want_t2.json (the Multiply row disambiguated to the char*
twin at libmpscpp.cpp:2252, physically verified line-for-line equal
to the landed unit-141 string form; the GlobalData row disambiguated
to the interp-sysfunc kernel at :1592 by TU).

## Units rendered (9 new kt files + 1 ledger append, package daf)

- 196 libmpscpp.cpp::global::Fnumber (28 LOC, GlobalFnumber.kt):
  the global-class twin of the unit-97 mstring::Fnumber render:
  retrieve-then-throw, coded Open/Comma/Close envelope over the
  retrieved value + required first pattern + OPTIONAL second pattern
  (the b.c_str()==0 gate folds onto a null String? default), v1d
  offset-1 carriage, dafFcn relay, bd read-back. The CODED consts
  cited from MstringFnumber.kt, not re-defined. Consult
  16:10:00Z: rows 101/92/54/33/97/98 hit-reuse.
- 197 include/mumpsc/externs.h::<misc> (ExternsHMisc.kt): GPL
  banner + struct nmes + the TU extern set residue; the rows 6/22
  seat map delivered in-KDoc (state fields on DafCompilerState/
  DafMsv, the nmes chain the row-62 fold, the rest
  declaration-only until drawn). No body. Consult 16:10:30Z: rows
  6/22/62 hit-adapt.
- 198 interp-sysfunc.cpp::GlobalData (43 LOC,
  SysfuncGlobalData.kt): the data intrinsic kernel, 2-arg svPtr
  service. TU verified: interp-sysfunc.cpp:52 defines SQLITE, so
  the SQLITE arm is the LIVE reference-build arm; BOTH ifdef arms
  render gated behind SQLITE_D (GlobalQuery precedent), dead-arm
  probe bytes 1,0 kept distinct from live 1,1,0; the tmp3
  save-restore around the RETRIEVE call kept verbatim. Consult
  16:11:00Z: rows 96/8/103/14/15 hit-reuse, one manual-decision.
- 199 libmpscpp.cpp::global::MakeGlobal (38 LOC, MakeGlobal.kt):
  the vararg reference-assembly worker; the int/char* two-arm
  lattice keyed on flg rides an Any vararg (divergence ledgered);
  the file-scope statics Sref/Sname/ArgCount fold onto top-level
  state srefD/snameD/argCountD (rows 6/22); sprintf percent-ld via
  Locale.US digits; strcatxD seat. countDaf RAISED IN-FILE as a
  named-owner stub (owner libmpscpp.cpp:2932, not a drawn row).
  Consult 16:11:30Z: rows 105/6/22/62/37/26/28/14/15 hit-adapt.
- 200 interp-fcn.cpp::<misc> (InterpFcnMisc.kt): TU residue; the
  define SQLITE verified at the TU head (strengthens the arm-live
  lattice of the T2 KEY FINDINGS); SYMSTORE/SYMRETRIEVE already
  seated as consts. The fcn body itself is roster 123, rendered
  B4 T3. Consult 16:12:00Z: row 37 hit-reuse.
- 201 libmpscpp.cpp::Multiply (Multiply.kt append, no new body):
  the char* twin at :2252 verified line-for-line identical to the
  landed unit-141 string body modulo ctor carriers; discharged as
  a ledger note per the DOC unit-225 carrier-twin precedent. The
  three-Multiply family complete in-arm: 49 dispatcher (not yet
  drawn) / 141 string / 201 char*. Consult 16:12:30Z: row 105
  hit-reuse.
- 202 interp-sql.cpp::rslt_callback (31 LOC, RsltCallback.kt): the
  SQLITE result-row callback; callBackD/rsltSpecD raised as
  top-level state (row-62 fold; the rsltSpec pointer identity
  replaced by the carrier string, divergence ledgered); the mark
  byte kept as the RSLT_MARK_D code point; the i-2/i-1 byte
  surgery kept verbatim. Consult 16:13:00Z: rows 62/8 hit-adapt,
  one manual-decision.
- 203 compiler-parse.c::Extract (30 LOC, CompilerExtract.kt): the
  compiler _extract emitter; the FILE-SCOPE SSS/TTT scratch
  pointers fold onto local in-place index shifts (compaction, not
  durable cursor state; row-31 precedent); Out folds onto
  DafCompilerState.emit; iiitmp on the state; memcheckDaf
  (T3:215) and extract2Daf (T3:168) seats called and cited; the
  split-string fprintf with the p2493 trace comment kept verbatim;
  the (UNS) padding literals kept verbatim. Consult 16:13:30Z:
  rows 31/27/3 hit-reuse, one manual-decision.
- 204 libmpscpp.cpp::Sim1 (40 LOC, Sim1.kt): the product-moment
  walk on the Dice.kt scaffold; the C++ BY-VALUE global params
  fold onto local mutable refs + fresh nodes per rebuilt B ref
  (divergence ledgered); the raw (char)1 coded-suffix truncation
  probes; two tpx-guarded GLOBAL_NOT_FOUND throws; strtod pair.
  Consult 16:14:00Z: rows 54/68/31/40/92/56 hit-adapt.
- 205 interp-sql.cpp::embed1 (19 LOC, Embed1.kt): the quote-
  doubling escaper, near-direct BASE row 112; the static char
  shared scratch folds onto a fresh per-call String (row 56/112
  family; the reentrancy hazard documented); null gate on the
  receiver. Consult 16:14:30Z: rows 112/56 hit-reuse.

## Seat ledger

- countDaf: RAISED (named-owner stub in-file at MakeGlobal.kt;
  owner libmpscpp.cpp:2932).
- rsltSpecD/callBackD: RAISED (top-level state, RsltCallback.kt).
- extract2Daf/memcheckDaf: called unchanged (DafInterpStubsT3).
- sprintfIntD/copyIntoSpec/appendSpec/cspecLen/copyCarrier/
  CodedByte1D: private per-file folds (rows 27/8 family).
- gfName: reused from the unit-156/97 family.
- T3 tag-correction riders REMAIN QUEUED (SymHMisc:38,
  StrmanipMisc:30-32; corrections ride the T3 sigint/
  serviceInterface renders).

## Gates

kotlinc whole-arm SILENT EXIT=0 at 116 files (gate copy AND fresh
repo-copy, both green; only the accepted Interpret.kt vendor-lattice
warning set, 6 warnings). Dafny suite per-spec loop: 70 verified 0
errors UNCHANGED (all ten T2 units EXEMPT or residue; no new spec).
H-015 hygiene scan clean after pre-push fixes: Sim1.kt backticks
removed; GlobalFnumber.kt and SysfuncGlobalData.kt comment dollars
normalized to unicode escapes; GlobalFnumber.kt escaped-dollar
string literal rewritten to the unicode-escape form after a
doubled-backslash compile error (the known write_file hazard; fixed
pre-gate). ASCII-only; trailing newlines verified. Consult ladder
fresh band 2026-11-24T16:10:00Z to 16:14:30Z, 30s steps.

## Files pushed (13)

- wp2/arms/DAF/src/main/kotlin/daf/GlobalFnumber.kt (new)
- ExternsHMisc.kt (new)
- SysfuncGlobalData.kt (new)
- MakeGlobal.kt (new)
- InterpFcnMisc.kt (new)
- Multiply.kt (edit: unit-201 carrier-twin ledger append)
- RsltCallback.kt (new)
- CompilerExtract.kt (new)
- Sim1.kt (new)
- Embed1.kt (new)
- wp2/logs/DAF_heuristic_log.csv (ids 201-212 appended; base prefix
  byte-verified)
- wp2/PILOT_TRACKING.md (batch-7 rows 196-205 DAF column marked
  translated; the DAF B7 tranche-2 note paragraph before the table)

## Next

Mark's splice: cat this fragment >> the arm journal (wp2/arms/
ARM_WORKING_NOTES.md; expect 10416 + N lines), rm in a separate
commit; then the repo-copy gradle + dafny (70) round. Then DAF B7
T3 (complex 206-215: compiler-mumps residue, fcninit, Qsub,
freadx, zcommands_code, sigint, match, epi, libmpscpp misc-6,
ServiceInterface), carrying the two tag corrections.
