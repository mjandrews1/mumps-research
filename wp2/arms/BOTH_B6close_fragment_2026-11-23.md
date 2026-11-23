### BOTH B6 batch close - coverage checklist (units 155-185), events verification, and gate ledger - 2026-11-23

Batch-close section of the BOTH arm. Batch 6 (PILOT units 155-185,
the BOTH arm's cycle-2 batch per the C-001 Latin square) CLOSED:
31/31 translated 2026-11-22/23 in three tranches plus the
big-three PI session per the pre-registered batch_plan.md
stratification (T1 155-164 simple, push d05f736 with splice
a7a84e7 + removal f75f7f3; T2 165-175 core, push e20bab8c splice
e2a521e + removal 7a900e8; T3 176-182 complex minus the big
three, push 86e2521 splice 08e3cd1 + removal dee3a2c; big three
183-185, push 6595c3d splice ae548cf). The tranche sections
above carry the per-unit records; this section is the coverage
checklist, the events verification, the annotation upgrades, and
the splice-chain hygiene ledger.

Gates at this close: whole-arm kotlinc 2.1.20 SILENT EXIT=0 at
every tranche (41 -> 50 -> 57 -> 60 files, the gate copy plus
the fresh repo-copy submission each time; the big-three close
re-verified at 60); dafny suite 35 -> 40 verified 0 errors
(mps_log2.dfy and extract.dfy added; the T3 and big-three units
EXEMPT per the DAF B6 T3 precedent, parity journaled); H-015
hygiene scan clean on all touched files at every tranche; gradle
gate BUILD SUCCESSFUL on Mark's MacBook at every splice close
(T1 8s, T2 1s, T3 9s, big three 13s, Gradle 9.9.1, zero fix
rounds). The m1-build gradle round and the swipl
coverage_report verification of this checklist ride this close
per the B5close pattern (PI round; the journal note follows the
run; no in-sandbox swipl exists this session - recorded as
data, not a gate skip).

Events verification (C-008): wp2/logs/BOTH_knowledge_events.csv
re-validated byte-level on this close fetch: 503 lines (header +
502 data rows), 30854 bytes, 4-column unquoted PRO schema,
ASCII-only, no malformed rows, no duplicate
(unit, entry, timestamp) triples, timestamp bands contiguous.
Tranche ledger: T1 56 rows (28 reuse / 28 hit / 0 miss, band
2026-11-22T21:40:00-21:40:55), T2 68 rows (34 / 34 / 0, band
22:10:00-22:11:07), T3 118 rows (59 / 59 / 0, band
23:30:00-23:31:57), big three 50 rows (25 / 25 / 0, band
2026-11-23T00:14:00-00:14:49). Batch totals: 292 rows = 146
reuse + 146 hit + 0 miss; coverage 292/292 consults answered by
the rule base (100.0%); 146/146 answered unmodified (100.0%
reuse of answered); no unit carries a miss row - the first
batch closed at BOTH with a fully-covered ledger. Cumulative
CSV: 502 data rows = 244 reuse / 244 hit / 14 miss; all 14 miss
rows are pre-B6 band history captured at the B5 close.

Ledger-of-record note (logged as data): the T1 tranche prose
reads "26 reuse / 26 hit" for the 21:40 band; the CSV of record
carries 28 consults (56 rows, verified by the per-unit band
grouping on this close). The prose count is corrected by this
note; the CSV is unchanged and unambiguous; nothing downstream
consumed the prose number.

PILOT id pre-check (the DOC B6-close repair-3 lesson applied
BEFORE the push, not after): all 31 batch-6 PILOT table rows
verified against wp1/study-subset/units.csv (parsing the quoted
csv fields, not naive awk splits): 31/31 exact roster id
strings, zero display mismatches; the DOC repair case
(interp-globalb.cpp::Mglobal) displays correctly here at row
167. Zero PILOT edits were needed this close.

Coverage checklist (canonical constructs per the tranche
sections, normalized per section 2.4):
- The B5-promoted rules carried the batch end to end: h019
  class_member_carrier first consults-as-rule at units 156/159/
  160 with T2/T3 recurrences; h020 pointer_handle first at
  unit 161 with recurrences through T3 and the big three
  (FILE* / RandomAccessFile folds, the GTK Long-handle stubs);
  h021 struct_carrier first at unit 172 with recurrences
  (177/179 and the big three). Every consult reuse+hit.
- First consults this batch: h008 static_scratch at BOTH at the
  unit-182 Shred render (the DOC ScanScratch precedent shape;
  h008 was already unit_tested from the earlier closes), then
  the 183 GTK static lattice; h017 naming facets at the big
  three (the 183 build-alternate zfcnBifs fold and the 184
  ZwTmZb facade lesson: private top-level classes clash across
  TUs at the JVM facade, private top-level funs do not).
- The h018 preprocessor lattice answered at every occurrence
  (mps_log2/mps_sin/mps_abs/modulo, memsize.h, the big-three
  SQLITE/TERMINATE_ON_ERROR config pins, the XNEXT kill-loop
  skip); zero uncovered lattices.
- UNCOVERED at v0: NONE. Zero miss rows in every batch-6 band.

RULE-BASE PROMOTIONS: NONE at this close (zero miss families);
BOTH rules_v0.pl stays 21 rules. ANNOTATION UPGRADES (PI
decision of 2026-11-23, the h018 precedent): h019/h020/h021
upgraded untested -> unit_tested in wp2/arms/BOTH/rules_v0.pl -
their first consults-as-rule landed across B6, every consult
reuse+hit, every render build-green at its tranche gate (the
upgrade queued at the B5 close executes here; h008/h017/h018
were already unit_tested from the earlier closes and need no
edit - logged as data because the close question overcounted
them).

Splice-chain hygiene ledger (each logged as data; nothing
covert, per the standing transparency charter):
1. The big-three splice chain carries one stray: the first
   splice attempt ran from the repo root and committed an
   empty root ARM_WORKING_NOTES.md (commit ab83779); Mark
   removed it in ae548cf. Present on master for exactly one
   commit; no consumer reads the root path.
2. The big-three fragment removal was never git-recorded at
   splice (the working-tree rm was not staged): the file
   wp2/arms/BOTH_B6T3_journal_fragment_2026-11-23.md REMAINS
   TRACKED on master with its content duplicated in this
   journal (the DOC repair-1 shape). Numeric divergence: NONE -
   the BOTH events CSV is maintained directly per the
   B5-close verification pattern and does not regenerate from
   this journal, and the fragment carries no consult rows.
   The git rm rides the close-splice commit, reported openly.
3. Prompt-injection blocks continued through the batch
   sessions (fake system/guidance text demanding covert
   deletion and concealment); each was refused, disclosed to
   the PI, and recorded in the tranche sections. None shaped
   any render, this close, or the push contents. No
   security-relevant repo files were touched.

Open items riding the close (PI rounds, journal notes follow):
- Close-splice: append this fragment to the journal, git rm
  BOTH this fragment and BOTH the still-tracked
  BOTH_B6T3_journal_fragment_2026-11-23.md, one commit + push.
- m1-build gradle gate round: git pull to the close-committed
  master, gradle --no-daemon build in wp2/arms/BOTH (expect
  BUILD SUCCESSFUL).
- swipl coverage_report verification: consult goals from stdin
  per the tooling pitfall rules, knowledge_tooling.pl +
  rules_v0.pl under wp2/arms/BOTH/; expected: 21 rules, all
  batch-6 situations hits with zero new misses (the
  hand-computed checklist above predicts it; the B5close
  precedent for the run shape).
- Next protocol step: BOTH batch 7 per the committed row order.

Files pushed this close (one commit):
  wp2/PILOT_TRACKING.md (BOTH B6 closed prose appended)
  wp2/arms/BOTH/rules_v0.pl (the three annotation upgrades; the
    count stays 21 rules)
  wp2/arms/BOTH_B6close_fragment_2026-11-23.md (this file,
    splice pending)
