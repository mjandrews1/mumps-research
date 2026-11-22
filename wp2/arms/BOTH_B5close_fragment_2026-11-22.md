# BOTH B5 close journal fragment, 2026-11-22

### BOTH B5 close - coverage checklist (units 124-154), events verification, and rule-base promotions - 2026-11-22

Batch-close section of the BOTH arm. Batch 5 (PILOT units 124-154,
the BOTH arm's cycle-1 batch per the C-001 Latin square) CLOSED:
31/31 translated 2026-11-21/22 in three tranches per the
pre-registered stratification (T1 124-133 simple, push 9184cea0d
with the two trailing-newline fixups 00a0643a5 + 6e0e7dd20,
splice at 01525ab; T2 134-143 core, push eb6d33c9a, splice
85caa1a; T3 144-154 complex, push c306b0c2c, splice b6f20e7,
fragment removal cb4bc41). The tranche sections above carry the
per-unit records; this section is the coverage checklist, the
events verification, and the promotion decisions.

Gates at this close: whole-arm kotlinc SILENT EXIT=0 at 32 files
(T3 close, gate copy plus fresh repo-copy submission; re-verified
on the fresh post-reset sandbox rebuild of 2026-11-22, exit 0,
zero output bytes); dafny suite 30 verified 0 errors on the
committed spec set (smoke_test 2, length_count 5, mps_log10 5,
expx 4, parse_count 2, strmanip 12; per-spec loop and the
one-call suite discipline both green, re-verified in the fresh
sandbox); gradle BUILD SUCCESSFUL on Mark's MacBook at the T3
splice close (4s, first pull, zero fix rounds). The m1-build
gradle round is REPORTED BY THE PI AS NOT YET RUN (recorded
2026-11-22; the run and its journal note follow), and the swipl
coverage_report run flagged at the T1 section stays deferred to
m1-build with it; the hand-computed checklist below follows the
DOC-arm precedent in its place.

Events verification (C-008): wp2/logs/BOTH_knowledge_events.csv
re-validated byte-level on the fresh 2026-11-22 sandbox fetch:
211 lines (header + 210 data rows), 13272 bytes, 4-column
unquoted PRO schema, ASCII-only, no malformed rows, timestamp
bands contiguous (T1+T2 2026-11-21T02:05:00-02:07:37Z, T3
2026-11-22T19:05:00-19:05:52Z). Tranche ledger: T1 71 rows
(30 reuse / 35 hit / 6 miss), T2 86 rows (34 / 47 / 5), T3 53
rows (34 / 16 / 3). Batch totals: 210 rows = 98 reuse, 98 hit,
14 miss. Coverage: 196/210 consults answered by the v0 rule
base (93.3%); 98/196 answered unmodified (50.0% reuse of
answered); 12 of the 29 distinct unit ids carry at least one
miss row (every such row belongs to one of the three promotion
families below; no unit went wholly unanswered).

Coverage checklist (canonical constructs per the tranche
sections, normalized per section 2.4; the batch's construct
families and their rule coverage):
- io_stream/char_buffer/scratch constructs: h001/h007/h008
  exercised across all three tranches, answered everywhere.
- preprocessor lattices: h018 answered at every occurrence
  (T1 mps_log10 + mdh family, T3 mumps main banner + sql - the
  sql h018 resolution re-verified the reference Makefile
  object set and the CFLAGS; zero uncovered lattices).
- stub/out-of-scope surfaces: h014 seats answered at every
  occurrence; the arm-close stub ledger below itemizes the
  seats that stay raised.
- naming/format/numeric/unsigned/dead-store/goto/comma/
  globals-ext families: answered at every occurrence
  (h002-h006, h009-h013, h015-h017).
- UNCOVERED at v0 (the miss families, all pre-capture):
  class_member_carrier x9 (units 129/131/133/134/136/137/138/
  149/154), pointer_handle x3 (the mdh_tree_level_add
  overload set, units 126-128), struct_carrier x2 (units 142
  SysDump stab chain, 144 sym.h misc). These are the only
  constructs with no v0 match; all three are captured at this
  close (below).

RULE-BASE PROMOTIONS (PI decision of 2026-11-22, all three
families at T1 recurrence >= 2; G1 general-construct, G4 dedup
cleared, G3 no oracle content; the units above followed these
actions throughout B5, every render build-green at its tranche
gate):
- h019 class_member_carrier: member units of not-yet-rendered
  classes pass receiver state explicitly (data String /
  GlobalRef.ref) or reuse/raise the minimal carrier seats
  naming the layout owner roster id; vendor member name kept
  in the fold name. Example libmpscpp.cpp::mstring::Length.
- h020 pointer_handle: C pointer domains with no JVM
  counterpart and no observable address semantics seat on
  String handle carriers naming the vendor pointer type; the
  FILE* family seats on the arm RandomAccessFile fold. Example
  libmpscpp.cpp::mdh_tree_level_add.
- h021 struct_carrier: structs whose layout owner is not in
  the batch become minimal carrier classes keeping vendor field
  order and names, only the touched surface seated. Example
  interp-sym.cpp::SysDump.
All three verified(untested) at capture per the h018 precedent;
first consults-as-rule land with BOTH B6 and the
untested->unit_tested upgrade is recorded at the next close.
The 14 miss rows are pre-capture history and remain as misses
per the append-only log; the events CSV is NOT rewritten.

ARM-CLOSE STUB LEDGER (h014 out-of-scope discipline; the seats
raised in B5 whose owners live outside the arm's drawing):
allocSV (roster 285 b8), parse_ (303 b3), parse_pattern_element
(196 b4), leaning_toothpickize (157 b6), make_cstring (212 b1),
perl_pm (214 b9), wstem (164 b10), gotamatch (180 b1), the
GTK platform family (17 mdh seats, non-roster), pm (libmpscpp.h
237 non-unit), cleanLocks/__symput/interpret (308 b6), the
globalClass.h layout seats (extractValue/assignValue/order/
kill/value/setValue/docCorrelate, roster 259 b3 with the
corrected pin), mumps-ecosystem non-units (readHistory,
history-lib edges). Resolved within the batch: acceptIt (T2
stub flipped by the roster-271 T3 render), GlobalNode.transpose
(T2 stub flipped by the unit-154 render), Avg/Sum/Translate/
Length member families rendered in-batch, keyfix inline, the
zfcn/Mglobal/xecute/SymGet carrier folds seated T1/T2.

Red-build count for the batch: 0 red gate builds (every kotlinc
and gradle invocation green on first submission; the T1 push
carries two newline-restoration fixups before the splice - push
plumbing repairs at 00a0643a5/6e0e7dd20, not builds; the in-
sandbox pre-gate iterations of the three tranches are logged as
data at the tranche sections).

Files pushed (one commit, this close):
- wp2/arms/BOTH/rules_v0.pl (190 lines; h019/h020/h021 appended
  with the capture provenance block; the 18-rule v0 seed text
  untouched above it)
- wp2/PILOT_TRACKING.md (the BOTH B5 closed paragraph appended;
  no table-cell changes in this push - the rows were all marked
  at their tranche pushes)
- wp2/arms/BOTH_B5close_fragment_2026-11-22.md (this file)

Open items at this close:
- the m1-build gradle round (PI runs it; journal note follows)
  plus the deferred swipl coverage_report verification of the
  hand-computed checklist above;
- the splice of this fragment (journal base 9187 lines; see
  the splice command block), then fragment removal per the
  standing two-commit pattern;
- BOTH B6 (units 155-185) opens next per the committed order:
  T1 simple 155-164 first, the 183-185 big three per the PRO
  B6 precedent stay a dedicated PI session decision;
- knowledge_tooling.pl needs no edit for the promotions (the
  rules file is the consulted artifact); the first B6
  find_rule(h019/h020/h021) queries land at the T1 renders.
