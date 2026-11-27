# Protocol Clarifications Registry

Pre-registered instruments — `wp1/order_assignments.md`,
`wp1/capture_criteria.md`, and the oracle manifests — are committed
before the pilot and never revised afterward. Questions, clarifications,
and deviations that arise during execution are logged here **as data**,
per the design (§7.4: deviations from the pre-assigned order are logged
as data, not silently repaired) and `wp1/order_assignments.md`'s own
instruction. Each entry states the observation, the clarification, the
effect on the committed schedule, and the methodological consequences.

| # | Date | Title | Status |
|---|------|-------|--------|
| C-001 | 2026-11-16 | Pilot B1 vs. Latin-square slots | Resolved (before cycle 1) |
| C-002 | 2026-11-16 | Per-unit check definitions formalized | Resolved (definition written) |
| C-003 | 2026-11-16 | PRO early B2 execution | Active (deviation logged) |
| C-004 | 2026-11-18 | Statistical Analysis Plan specified before pending-arm outcomes | Registered (SAP committed) |
| C-005 | 2026-11-18 | DOC cookbook-consult event-log mapping | Registered (prospective) |
| C-006 | 2026-11-19 | DOC batch-2 tranche 1-2 consult rows: one-time reconstruction | Registered (retrospective, bounded) |
| C-007 | 2026-11-19 | DOC's B2-before-B1 execution, and H-018 capture | Active (deviation logged) |
| C-008 | 2026-11-19 | export_doc_events.R duplicate-consult key vs. non-unique roster ids | Registered (tooling amendment) |
| C-009 | 2026-11-22 | PRO event-log timestamps are a nominal calendar, not wall-clock records | Registered (retrospective, logged as data) |
| C-010 | 2026-11-24 | Translator gate and push-verification steps are script-driven | Registered (C-010 entry) |
| C-011 | 2026-11-24 | One-session-per-batch for batches 8-10 | Registered (C-011 entry) |
| C-012 | 2026-11-24 | DAF CODED-lattice const-name inversion (unit 243 fold-in) | Resolved (PI ruled 2026-11-27: rename to vendor macros) |
| C-013 | 2026-11-24 | Batch-8 fresh tail-id divergence across arms (DOC vs DAF) | Resolved (PI ruled 2026-11-27: cross-walk checked in) |
| C-014 | 2026-11-27 | BOTH batch-3 coverage findings (records 43 and 77) | Registered (PI ruled 2026-11-27: renders scheduled) |
| C-015 | 2026-11-27 | Cosmetic rank/grep-line mislabel family in stub pins | Registered (PI ruled 2026-11-27: renumber on next pushes) |

---

## C-001 — Pilot B1 vs. Latin-square slots (resolved 2026-11-16)

**Observation.** WP2 (design §8; `wp2/README.md`) pre-registered the
pilot as batch **B1 translated in all five arms** (Nov 2026); B1 has been
executed in BASE and PRO (31/31 each; DOC, DAF, BOTH pending). The
committed Latin square (`wp1/order_assignments.md`) places each arm's B1
at a different cycle position: BASE cycle 1, BOTH cycle 7, DAF cycle 8,
PRO cycle 9, DOC cycle 10. As written, four arms would either
re-translate B1 at their assigned slot (duplicating pilot work) or leave
their row incomplete. Flagged in `CADENCE.md` as an open protocol
question to resolve before cycle 1.

**Clarification (logged as data; `order_assignments.md` unchanged).**

1. The pilot's B1 execution **credits each arm's B1 cell** in its
   committed row. No arm re-translates B1 in a later cycle.
2. Each arm executes its **committed row order with B1 removed**. This
   preserves each arm's relative batch order exactly; the only deviation
   is B1's position, hoisted from its assigned cycle to the pilot
   (position 0) in four arms (DOC, PRO, DAF, BOTH). BASE's B1 sits at its
   assigned position (first in its row) and is therefore not a deviation.
3. Nine batches remain per arm, so translation completes in
   **cycles 1-9 (Dec 2026 - Aug 2027)**. **Cycle 10 (Sep 2027) becomes a
   contingency/buffer cycle**: deferred-unit catch-up, micro-replication
   preparation, and per-unit check backfill. The analysis window
   (Oct-Dec 2027, Q5) is unchanged.

**Resulting per-cycle batch map (cycles 1-9):**

| Arm | C1 | C2 | C3 | C4 | C5 | C6 | C7 | C8 | C9 | C10 |
|-----|----|----|----|----|----|----|----|----|----|-----|
| BASE | B2 | B3 | B4 | B5 | B6 | B7 | B8 | B9 | B10 | buffer |
| DOC | B2 | B3 | B4 | B5 | B6 | B7 | B8 | B9 | B10 | buffer |
| PRO | B3 | B4 | B5 | B6 | B7 | B8 | B9 | B10 | B2 | buffer |
| DAF | B4 | B5 | B6 | B7 | B8 | B9 | B10 | B2 | B3 | buffer |
| BOTH | B5 | B6 | B7 | B8 | B9 | B10 | B2 | B3 | B4 | buffer |

(B1: all arms, pilot, Nov 2026 = position 0 for every arm.)

**Methodological consequences (recorded for the analysis phase).**

- **Latin property preserved per arm:** each arm still translates each
  batch exactly once, in committed relative order. No batch is dropped or
  duplicated.
- **B1 becomes a shared-prior batch:** all five arms encounter B1 at the
  same, earliest experience level (position 0). Cross-arm comparisons on
  B1 are therefore a fully paired baseline — cleaner than the square
  intended — at the cost that B1 no longer spans learning positions.
- **Batch-position covariate:** the H11 learning-curve analysis and the
  batch-position covariate (§10.3) use each arm's *actual* ordinal
  position: B1 = 0 (pilot) for every arm; remaining batches = cycle
  number (1-9) per the map above. The covariate, not the square's nominal
  positions, carries the learning-curve adjustment.
- **Position balance is degraded, not destroyed:** with B1 fixed at
  position 0, the remaining nine batches no longer occupy each position
  exactly once across arms (e.g., B2 is translated by BASE and DOC in
  cycle 1 but by PRO in cycle 9; BASE and DOC rows coincide once B1 is
  removed, an unavoidable consequence of crediting the pilot). The
  imbalance is logged here; the pre-registered batch-position covariate
  is the instrument for absorbing it. The fresh-attention rotation
  (BASE -> DOC -> PRO -> DAF -> BOTH) is unaffected and applies as
  pre-registered.
- **BASE's early B2 opening (Nov 2026)** is a schedule fact recorded in
  `wp2/PILOT_TRACKING.md`; actual start/close dates are logged per unit
  and the batch-position covariate uses actual completion ordinals.

**Files touched by this entry:** `CADENCE.md` (open-question section
resolved; cycle mapping updated). No pre-registered file was modified.


---

## C-002 — Per-unit check definitions formalized (2026-11-16)

**Observation.** The four per-unit verification columns (code-check,
output-check, runtime-check, target-check) entered use with batch 2 on
2026-11-15 without a written definition; units 50-51 were graded
against the operative informal practice. On 2026-11-16 the B1 table was
retrofitted with the same columns, making the definition gap material
for both batches and the pending B1 backfill pass.

**Clarification.** The operative practice is now written down unchanged
as `wp2/PER_UNIT_CHECKS.md`: code-check (static equivalence review,
design §9.1 code-review discipline), output-check (byte equivalence vs.
the frozen oracle, per `wp2/arms/ARM_WORKING_NOTES.md` §0.2),
runtime-check (edge/error-path/state exercise per design §9.3),
target-check (Kotlin target-spec compliance per design §7.2). The
researcher confirmed (2026-11-16) that the written definitions match
the practice under which units 50-51 were graded; no graded value is
reinterpreted or changed. The definition governs the B1 backfill pass
and all batches from B2 onward.

**Files touched by this entry:** `wp2/PER_UNIT_CHECKS.md` (new);
`wp2/PILOT_TRACKING.md` (checks note now points at the definition).
No pre-registered file was modified.

---

## C-003 — PRO's early B2 execution (logged 2026-11-16, active)

**Observation.** Per C-001, PRO's committed row order (B1 hoisted to
position 0) is B3, B4, ..., B10, B2; B2 is PRO's cycle-9 batch. On
2026-11-16 the PRO arm translated B2 unit 32
(libmpscpp.cpp::global::Extract; build green) before the C-001 cycle
map was propagated into the arm's working loop. The PI elected
(2026-11-16) to retain the translation and continue B2 as a logged
deviation rather than revert.

**Clarification (logged as data; no pre-registered instrument is
modified).**

1. PRO's early B2 translations are **provisional data**: retained in
   the repo and recorded in the PRO arm journal, and **re-verified at
   PRO's committed B2 position (cycle 9)** - each early unit receives
   its full per-unit check pass (wp2/PER_UNIT_CHECKS.md) there before
   the arm's B2 grading is finalized. The cycle-10 buffer (C-001) is
   the fallback slot if the re-verification pass overruns.
2. The batch-2 table's four check columns track the BASE reference
   translation (C-002); PRO's early work is NOT recorded in them.
   Unit 32's code-check was briefly set to `translated` in error
   (2026-11-16) and is reverted in the same-day registry commit.
   PRO-arm progress lives in wp2/arms/ARM_WORKING_NOTES.md and
   wp2/logs/PRO_knowledge_events.csv.
3. Ordinal accounting follows C-001: the batch-position covariate
   uses actual completion ordinals; each early PRO B2 unit records
   both its actual ordinal and its committed position (cycle 9).

**Methodological consequences (recorded for the analysis phase).**

- The Latin property per arm is preserved in kind - each batch is
  still translated exactly once, and no committed relative order is
  otherwise altered - but B2's position imbalance deepens: PRO now
  encounters B2 at actual ordinal ~1 instead of cycle 9.
- PRO's early-B2 units are flagged for a **practice-effect
  sensitivity analysis** (compare against the arms that meet B2 at
  its committed early position, BASE/DOC): if the study's arm-level
  B2 metrics show a learning-position effect, the PRO early-B2 run
  is the identifiable contaminated cell.
- The re-verification pass at cycle 9 guards against drift; the
  early translations' final graded values are those of the committed
  position, not of the early run.

**Files touched by this entry:** `wp2/PILOT_TRACKING.md` (unit-32
check column reverted; batch-position note), `wp2/arms/ARM_WORKING_NOTES.md`
(PRO journal deviation note). No pre-registered file was modified.

---

## C-004 — Statistical Analysis Plan specified before pending-arm outcomes (registered 2026-11-18)

**Observation.** Design §11.2 pre-specifies the primary contrasts
(H2-H5) and the language subgroups (S1-S3) at the level of intent
("paired per-module comparisons; blocked model with arm term",
"statistical test on decision logs"), but commits no concrete test
statistics, effect estimators, exclusion rules, or multiplicity
control. At this date BASE and PRO are complete and closed; DOC, DAF,
and BOTH have produced no translation outcomes (todo across
PILOT_TRACKING.md).

**Clarification.** A Statistical Analysis Plan is registered as
`wp2/STATISTICAL_ANALYSIS_PLAN.md`, specifying: matched-pair tests
per endpoint type (McNemar exact / Wilcoxon signed-rank with
Hodges-Lehmann / batch-paired Fisher with Mantel-Haenszel pooling for
coverage endpoints); Holm-Bonferroni across the four primary
contrasts; the pre-capture-history exclusion rule (the PRO h018
units 4/10/12/20/29 modeled formally); the treatment of front-run and
verification-only units (PN-1, unit-43 precedent: real outcomes,
one situation per unit-rule); pilot-B1 exclusion from confirmatory
tests with sensitivity appendix; and reporting requirements
(effect estimate with CI, N analyzed/excluded/deferred, per test).

**Effect on the committed schedule.** None. The SAP is registered
before any DOC/DAF/BOTH outcome exists; within-study PRO-vs-BASE
comparisons are classified specified-exploratory in this study and
carry forward as the confirmatory pre-registration basis for the
RSM/GT.M replications (docs/REPLICATION_ROADMAP.md).

**Methodological consequences.** No pre-registered instrument
(order assignments, capture criteria, oracle manifests) is modified.
The SAP binds the analysis of outcomes not yet observed; any
deviation from it during analysis must be logged here with the
analysis not silently re-run. Because PRO is already complete, its
pairwise contrasts are not blinded; the SAP states this openly
(§0) rather than claiming full prospective registration.

**Files touched by this entry:** `wp2/STATISTICAL_ANALYSIS_PLAN.md`
(new). No pre-existing pre-registered file was modified.

---

## C-005 — DOC cookbook-consult event-log mapping (registered 2026-11-18)

**Observation.** The DOC arm's pre-registered instrumentation
(`wp2/arms/DOC/cookbook_v0.md`, "Lookup logging") records each
cookbook consult as an arm-journal row holding (unit id, entry id,
outcome), with outcome in {hit-reuse, hit-adapt, miss}. The analysis
input contract (`wp2/analysis/README.md`, contract 1; C-004)
requires event CSVs with columns exactly
`kind,target,unit,timestamp` and kind in {reuse, hit, miss}. A DOC
log in the pre-registered journal form therefore does not satisfy
the contract as-is, and an explicit mapping is needed before any
DOC batch produces analysable outcomes.

**Resolution.** The pre-registered journal practice is unchanged
(the instrument is not modified mid-study); the mapping is fixed
prospectively, before any DOC batch runs. At each DOC batch close,
`wp2/logs/DOC_knowledge_events.csv` is generated from that batch's
journal consult rows, LF line endings, header exactly
`kind,target,unit,timestamp`, one consult per (roster unit, cookbook
entry) expanded as follows:

- `hit-reuse` -> one `reuse` row plus one `hit` row (PRO pair
  semantics: target = cookbook entry id, e.g. H-001);
- `hit-adapt` -> one `reuse` row plus one `hit` row. Decision
  (researcher, 2026-11-18): adaptation of a cookbook entry counts as
  reuse, so PRO and DOC reuse rates both measure
  consult-and-apply of the knowledge artifact, not verbatim
  copying. Both arms' definitions are stated openly in the SAP's
  E5 interpretation as a result;
- `miss` -> one `miss` row.

Timestamps carry the consult time as recorded in the journal row.
Arms with no recorded consult for a unit emit no row, per PRO's
one-pair-per-(unit, rule) protocol carried over to (unit, entry).
Coverage/hit-rate denominators follow capture_criteria §4-5 as for
PRO. The BOTH arm, if it consults the combined artifact, must
register or match this schema before analysis likewise.

**Effect on the committed schedule.** None. No DOC outcome exists
yet; the mapping binds before any DOC batch begins.

**Methodological consequences.** The reuse-rate *definition* for DOC
now includes adaptations; this is recorded here rather than inferred
during analysis. Any later deviation from this mapping must be
logged as a new entry, with the analysis not silently re-run.

**Files touched by this entry:** `wp2/PROTOCOL_CLARIFICATIONS.md`
(this entry), `wp2/analysis/README.md` (contract 1 and contract 3
and layout amended: DOC mapping reference, check-export generator,
and the `roster_row` unit key), `wp2/analysis/R/export_per_unit_checks.R`
(new). No pre-registered instrument (order assignments, capture
criteria, oracle manifests, cookbook content) was modified.

**Unit-key registration.** The per-unit check export
(`per_unit_checks.csv`, README contract 3) is keyed by
`roster_row` (units.csv physical row, header = line 1, units =
rows 2-309), not by the roster `id` string: roster ids are
file-level and not unique (one id appears on 9 rows), so an
id-keyed export would silently collapse distinct units, the same
hazard registered for the events log under C-004 contract 5. The
check generator emits `roster_row` as the join key and `unit_id`
as descriptive only. Registered before any export exists; no
recorded value is reinterpreted.

---

## C-006 — DOC batch-2 tranches 1-2 consult rows: one-time reconstruction (registered 2026-11-19)

**Observation.** The DOC arm's pre-registered workflow
(wp2/arms/ARM_WORKING_NOTES.md, section 1) requires each cookbook
consult (including misses) to be appended to the arm journal
BEFORE consultation. The batch-2 tranches 1 and 2 (units 32-51;
commits 7cfc6e2, 1e5187f, 2026-11-18) were pushed with
cookbook-entry annotations inside the Kotlin files but with NO
consult rows in the journal, so export_doc_events.R (C-005)
would emit no events for those units at batch close.

**Resolution (researcher decision, 2026-11-19).** One-time,
bounded reconstruction of the EVIDENCED consult rows:

1. Only consult rows evidenced by an explicit cookbook-entry
   citation (H-0xx) in the pushed unit files are reconstructed;
   nothing is inferred beyond the annotations. 26 rows land in
   the batch-2 DOC journal section, typed hit-reuse/hit-adapt/
   miss per the in-file outcome notes.
2. Units whose annotations cite no entry — 32
   (global::Extract), 33 (global::Find), 38 (global::Piece),
   48 (getstr2) — are recorded as consult-unrecoverable and
   emit no row; consults are not invented for them.
3. Timestamps are the tranche PUSH times (2026-11-18T22:45:30Z,
   2026-11-18T22:51:46Z), an explicitly stated proxy: actual
   consult times were not recorded.
4. The units 40/41 miss rows (preprocessor_conditional, closest
   entry H-003) preserve the in-file miss notes; the construct
   remains a DOC T1 capture candidate for the batch-2 close.
5. The copyright_notice KDoc references entry "H-09" (one
   digit); it is recorded as H-009 (the entry that exists); the
   KDoc typo is corrected in the same push.

**Effect on the committed schedule.** None. Batch 2 is still
open (units 52-62 pending); the reconstruction lands before the
batch-2 close export, so the close runs on the full journal.

**Methodological consequences.** The reconstructed set only
contains annotation-evidenced consults, so events for units
32-51 undercount consultations and are biased toward hits
(potential unrecorded misses are unrecoverable). The analysis
phase must treat DOC batch-2 units 32-51 events as
partial-evidence reconstructed data, per this entry; this is a
recorded limitation, not a silent patch. From unit 52 onward,
consult rows are appended before consultation per the original
instrument, with true consult timestamps.

**Files touched by this entry:** PROTOCOL_CLARIFICATIONS.md
(this entry), wp2/arms/ARM_WORKING_NOTES.md (batch-2 DOC
section with the reconstructed consult rows), and the
copyright_notice KDoc typo fix (H-09 -> H-009). No
pre-registered instrument (cookbook content, capture criteria,
order assignments, oracle manifests) is modified.

---

## C-007 — DOC's B2-before-B1 execution, and H-018 capture (logged 2026-11-19, active)

**Observation.** The DOC arm's committed pilot order is B1 first
(units 1-31, complexity_rank ascending, common discipline section 0.1)
then B2 (committed early position among the post-B1 batches per
C-001). On 2026-11-18/19 the DOC arm translated, gated, and closed
batch 2 (units 32-62; commits 8039e9d..8dbe4dd, kotlinc 0/0 and
gradle BUILD SUCCESSFUL on m1-build, events export instrument-stable)
before any B1 unit was started; PILOT_TRACKING's DOC column for B1
remained `todo`. The PI elected (2026-11-19) to retain the B2 work
as a logged deviation rather than revert, register the inversion
here, and open B1 now in phased tranches.

**Clarification (logged as data; no pre-registered instrument is
modified — the H-018 capture below follows capture_criteria T1 with
gates G1-G4 checked and mirrors PRO's existing h018 exactly).**

1. DOC's B2 translations remain graded at close as provisional data,
   C-003's model: ordinal accounting follows C-001 — the
   batch-position covariate uses actual completion ordinals; each
   DOC B2 unit records its actual ordinal (~1, before B1) and its
   committed batch position.
2. Practice-effect consequence mirrors C-003: DOC joins PRO as an
   early-B2 arm, so the identifiable contaminated cells for the
   practice-effect sensitivity analysis are the PRO and DOC early
   runs, against BASE (native order) and the arms meeting B2 at its
   committed position without inversion.
3. Combined with C-006 (partial-evidence reconstructed consults for
   B2 units 32-51), DOC's B2 is the study's most position- and
   evidence-confounded cell; the analysis phase treats it as such
   (both flags travel together on those units).
4. B1 opens 2026-11-19 in phased tranches under the unchanged fixed
   discipline; PILOT_TRACKING's DOC B1 column updates per tranche,
   and B1 consult rows are logged before consultation as from unit
   52 onward in B2 (no reconstruction expected).
5. The preprocessor_conditional capture decided by the PI on
   2026-11-19 lands as cookbook entry H-018 (T1 via mps_exp +
   mps_exp10 within DOC; PRO precedent under h018 since B1 close),
   restoring the two knowledge artifacts to entry-for-entry parity
   (18 entries each) before the interleaved cycles begin.

**Effect on the committed schedule.** None beyond the recorded
inversion: B1 is the open work; C-001's cycle map is otherwise
unmodified.

**Files touched by this entry:** PROTOCOL_CLARIFICATIONS.md (this
entry), wp2/arms/DOC/cookbook_v0.md (H-018),
wp2/arms/ARM_WORKING_NOTES.md (DOC section-5 calibration block,
B2-only scope), wp2/PILOT_TRACKING.md (B1 header note: DOC opened
2026-11-19). No pre-registered instrument is modified.

---

## C-008 — export_doc_events.R duplicate-consult key vs. non-unique roster ids (logged 2026-11-19, registered)

**Observation.** At the DOC B1 close export (2026-11-19, m1-build),
Rscript wp2/analysis/R/export_doc_events.R halted with "duplicate
consult for (unit, entry)" on the pair
libmpscpp.cpp::mdh_tree_level_add / H-015: the batch-2 seven-column
overload (consult 2026-11-18T22:51:46Z, hit-reuse) and the batch-1
five-column overload (consult 2026-11-19T01:52:03Z, hit-adapt). These
are two DISTINCT roster units: the units.csv id strings are
file-level and not unique (one id string appears on nine roster rows;
analysis README "Input contracts" #3: unit_id is descriptive only and
never a join key; #5: string-level dedup is forbidden because it
would collapse distinct units). Both consults are true single
consultations of H-015 by two different units; no journal row is
wrong and no recorded value is touched. The exporter's duplicate
check, as coded, keyed on (unit id string, entry), which contradicts
the C-005 roster-id contract it implements.

**Clarification (tooling amendment; no pre-registered instrument is
modified — the journal and both consult rows stand unchanged).**

1. The duplicate check in export_doc_events.R keys on
   (unit, entry, timestamp): the consult timestamp is captured per
   unit, so a repeated (unit, entry) pair at distinct timestamps is
   two roster units and both are emitted as separate consults; an
   exact repeat of all three fields is a true duplicate and still
   fails loudly.
2. The malformed-row grammar check, the roster-id membership check,
   and the C-005 event mapping (hit-reuse / hit-adapt each emit a
   reuse+hit pair; miss emits a miss row) are unchanged.
3. The analysis README "DOC consult rows" section documents the
   duplicate keying rule with a pointer to this entry.
4. The registry table is also backfilled with the C-007 row (the
   C-007 entry was committed without its table row; noted here, no
   content change).

**Effect on the committed schedule.** None. The B1 close export is
re-run after this amendment; the CSV is fully regenerated from the
journal, so the run is idempotent.

**Methodological consequences.** None to the data: consult rows are
timestamped and batch-identifiable, and the events CSV unit strings
were already file-level per C-005 (the same string may carry several
roster rows in later joins, per the README counting note). The
amendment only removes a false-positive halt; it cannot create or
drop events.

**Files touched by this entry:** PROTOCOL_CLARIFICATIONS.md (this
entry and the C-007 table row backfill),
wp2/analysis/R/export_doc_events.R (the duplicate-check key and two
comment blocks), wp2/analysis/README.md (one sentence in the DOC
consult rows section), wp2/arms/ARM_WORKING_NOTES.md (the B1 close
journal note). No pre-registered instrument is modified.


---

## C-009 — PRO event-log timestamps are a nominal calendar, not wall-clock records (logged 2026-11-22, registered)

**Observation.** The PRO arm's pre-registered instrumentation
(wp2/arms/PRO/knowledge_tooling.pl: record_reuse/record_hit/
record_miss append one row per call, the timestamp taken from
get_time at record time; coverage_report reads those rows) was
used as designed for only the first two roster units: rows 2-31
of wp2/logs/PRO_knowledge_events.csv (units 1-2, B1) carry true
second-precision stamps (2026-11-15T14:54:12+0000 /
14:56:13+0000) consistent with the tooling. From row 32 onward
every stamp is minute/hour-aligned (15:10, 22:10, 09:00, ...),
a shape the get_time path cannot produce: the remaining 3,658
event rows were hand-appended to the CSV with nominal timestamps,
not recorded by the tooling at consultation time. Two severity
tiers, established against the git history (all rows committed
2026-11-15..18):

1. Rows 32-1211 (nominal dates 2026-11-15..22, batches B1-close
   through B4 group 1) are authored near-dated ladders, partly
   retrospective, partly a few days ahead of the wall clock.
2. Rows 1212-3689 (nominal dates 2026-11-23 .. 2026-12-02,
   batches B4 onward) were future-dated at write time: 2,478
   rows (67% of the log) claim event dates up to 14 days after
   their actual commit dates. The nominal calendar assigns
   roughly one consulting day per roster tranche (B4 ~ Nov
   22-24 ... B11 = Dec 2), spreading the arm's actual ~3.5-day
   sprint (2026-11-15..18, ~60 commits, 308/308 units) across a
   17-day projected schedule.

The convention is disclosed IN the arm journal: tranche-close
paragraphs record the nominal ladders explicitly (e.g. the B4
group-3 ledger: "Events +106 rows (u114 09:00 x5 rules ...
u123 18:00 x5, all 2026-11-24 +0000)"), so the stamps are
recoverable as labels; but no registry entry had authorized
replacing tool-recorded get_time stamps with an authored
calendar, contrasting with the C-006 disclosure discipline
applied to DOC's reconstruction. One journal prose date also
contradicts git: the B3 close correction paragraph is dated
2026-11-21 while its commit (41fc507, "PRO arm B3 close
correction") is 2026-11-17T05:19Z.

**Clarification (logged as data; no pre-registered instrument is
modified — events CSV row content stands unchanged, the log is
append-only).**

1. The PRO_knowledge_events.csv timestamp column is declared
   NOMINAL: a per-unit label on the arm's projected consulting
   calendar, not a record of when a consultation happened. It is
   inadmissible as evidence for ordering, pacing, session
   structure, or any time-derived covariate of PRO events.
2. The canonical chronology of the PRO arm is the git history:
   every CSV append is keyed to a commit, and the arm journal
   already cites the landing commit per tranche. Analyses that
   need PRO chronology (including the C-003 practice-effect
   cells and the batch-position covariate's actual ordinals) use
   commit dates, not event stamps.
3. An analysis-side provenance note is checked in as
   wp2/analysis/PRO_EVENT_TIMESTAMP_PROVENANCE.md: event-row
   provenance classes with exact row ranges, the stamp-shape and
   commit evidence, per-day stamp distribution, and the analysis
   rules that follow from them. The CSV itself is not edited;
   provenance classes are derived by row number at analysis
   time, per the append-only discipline.
4. The event CONTENT columns (kind, target, unit) remain
   admissible: coverage_report arithmetic is stamp-independent,
   and the study-close numbers (1,856 triggering situations;
   1,851 hits, 5 misses; hit rate 99.7%, reuse rate 99.0%)
   reconcile exactly against the journal irrespective of
   stamps. The 5 misses are the B1 preprocessor_conditional
   pre-capture units (4/10/12/20/29), already modeled formally
   in the SAP's exclusion rule.
5. The record-at-consult evidence claim for PRO consequently
   rests on the arm journal's per-unit paragraphs and the per-
   file KDoc annotations (which exist per unit), not on event
   timestamps. The C-006-style limitation is recorded: consult
   timing for PRO cannot be verified from the log; consult
   occurrence per (unit, rule) is evidenced by journal + KDoc.

**Effect on the committed schedule.** None. PRO is complete
(308/308, B11 closed 2026-11-18); no translation, gate, or CSV
row is changed. The entry binds the analysis phase and the
instrumentation calibration (section 5) only.

**Methodological consequences.** Any analysis consuming PRO
event timestamps (time series, inter-arm pacing comparisons,
learning-curve time covariates) is invalid as stamped and must
be recomputed against commit chronology; the SAP's sensitivity
analyses carry this entry as a named limitation, alongside
C-003/C-007 (position) and C-006 (evidence) flags. Hit/reuse/
miss and rule-target comparisons across arms are unaffected by
this entry on the content side, but any arm-level comparison
that ever leaned on timestamp parity (e.g. session-length or
cadence measures) is restricted from PRO or recomputed. The
instrumentation calibration reports PRO's hit/reuse/miss
counts with the nominal-stamp qualification attached.

**Files touched by this entry:** PROTOCOL_CLARIFICATIONS.md
(this entry), wp2/analysis/PRO_EVENT_TIMESTAMP_PROVENANCE.md
(new). No pre-registered instrument (order assignments, capture
criteria, oracle manifests, rules_v0.pl, knowledge_tooling.pl)
is modified, and no data row of PRO_knowledge_events.csv is
changed.

## C-010 - Translator gate and push-verification steps are script-driven from DAF B7 T3 (logged 2026-11-24, registered; PI decision same day)

**Decision.** The hand-run tranche-gate sequence (whole-arm
kotlinc on the gate copy, kotlinc on the fresh repo-copy
submission, the dafny per-spec verification loop with the
expected-count check, the H-015 hygiene scan) and the local
push-verification sha computation are executed by scripts
persisted in the translator's Personal Knowledge
(mumps-render-sandbox/scripts: gate.sh, sha-stage.sh) rather than
as individually typed shell commands.

**Methodological neutrality.** The scripts perform the SAME
checks in the SAME order with the SAME acceptance criteria
(accepted Interpret.kt vendor-lattice warning set only; dafny
expected-verified count compared; hygiene failure aborts the
gate). No check is added, removed, or relaxed; the fail order is
the hand-run order. The sha recipe remains validated against a
known remote blob before every push. Renders, KDoc consult bands,
PILOT/fragment prose, and heuristic TRACE cells remain
individually authored; the consult-band stamping discipline is
unchanged.

**Motivation.** Three of the five pre-push fixes at the B7 T2
close were shell-quoting artifacts of the hand-run gate, not
render defects; scripting removes that failure class without
touching what the gate measures.

**Files touched by this entry:** PROTOCOL_CLARIFICATIONS.md (this
entry). No pre-registered instrument is modified.

## C-011 - One-session-per-batch for batches 8-10 (logged 2026-11-24, registered; PI decision same day)

**Decision.** For the remaining pilot batches 8-10, each arm's
batch may be completed in a single working session (all three
strata rendered, one gate cycle, one protocol push, one journal
splice) instead of the batch-7 tranche-at-a-time cadence, at the
translator's discretion per batch.

**Effect on pre-registration.** The pre-registered batching and
strata are unchanged: same units, same batch assignment, same
per-unit instruments. What changes is session granularity only.
The tranche-gate row of the heuristic log records ONE gate row
per session-gate cycle; when a batch is completed in one session
the gate row states the whole-batch file count and the strata
covered, and the PILOT note paragraph records the one-session
choice for that batch.

**Continuity constraints.** A batch split across sessions for
any reason reverts to per-tranche gate rows for the split
portions (the batch-7 pattern), so no gate cycle ever mixes files
that were not compiled together. The C-001 cycle-10 backfill
decision and the per-unit check columns are unaffected.

**Files touched by this entry:** PROTOCOL_CLARIFICATIONS.md (this
entry). No pre-registered instrument is modified.


## C-012 - DAF CODED-lattice const-name inversion at the unit-243 fold-in (logged 2026-11-24; PI ruled 2026-11-27)

**Observation.** At the DAF batch-8 unit-243 render
(compiler-parse.c::<misc>, the parser file-head residue), the
vendor define lattice was adjudicated across all four defines.h
copies in the vendor tree: every copy sets CodedClose 207 and
CodedComma 208 (defines.h lines 38-41). The DAF arm's landed const
pair in MstringFnumber.kt:17-19 (seated at the unit-196 render,
batch 7) reads CODED_COMMA_D=207 and CODED_CLOSE_D=208: the two
NUMERIC values are the vendor values, but the two NAMES are
inverted relative to the vendor macro names. The inversion is
arm-internal and symmetric: the arm's consumers (Zfcns.kt /
ZfcnBifs.kt CLOSE_ZB=207 / COMMA_ZB=208) align with the vendor
values, and the batch-1 LocalName.kt coded-command decode table
(row 101 anchor family) consumes the same numeric code points, so
runtime behavior is unaffected; no cross-arm consumer exists.

**Clarification (logged as data; disclosed at the batch-8 close in
the heuristic seats row 256, the gate row 257, the PILOT batch-8
note, and the journal fragment).**

1. No code change is made pending the ruling; the batch-8 push
   lands the fold-in with the existing const pair and this
   disclosure.
2. If the PI rules the names must match the vendor macros, the
   rename is a one-file, compile-identical two-line edit plus its
   in-file release note, ridden on the next DAF push (batch 9) and
   recorded here as resolved-named.
3. If the PI rules the arm-internal symmetric naming stands, the
   divergence is recorded as accepted-arm-internal and the SAP's
   code-equivalence checks for the affected units cite this entry;
   the analysis phase treats the name pair as semantically neutral
   (identical numeric decode table).

**Effect on the committed schedule.** None. The ruling rides the
next DAF push either way; no gate, batch, or per-unit check value
changes.

**PI ruling (2026-11-27):** Option 1 — the names must match the
vendor macros. The rename (CODED_CLOSE_D=207, CODED_COMMA_D=208)
is a one-file, compile-identical edit plus its in-file release
note, ridden on the next DAF push (batch 9) and recorded here as
resolved-named when it lands.

**Methodological consequences.** The unit-level outcome is not
affected: the code points 207/208 decode identically in every arm.
What is adjudicated is a code-equivalence bookkeeping question
(vendor macro-name fidelity), which the SAP's per-unit
code-review discipline records but does not grade against.

**Files touched by this entry:** PROTOCOL_CLARIFICATIONS.md (this
entry). No pre-registered instrument is modified.

## C-013 - Batch-8 fresh tail-id divergence across arms (logged 2026-11-24; PI ruled 2026-11-27)

**Observation.** The batch-8 roster rows are keyed by numeric
PILOT/table ids derived from each arm's committed batch_plan.md
row order, per C-001's relative-order preservation. At the batch-8
close, the fresh batch-8 units (units whose first render lands in
batch 8: Merge, cannon, mumps_open, the compiler-parse
file-head residue, AllocSV, the libmpscpp misc-7 chunk, the
interp-sql btree body) carry DIFFERENT numeric tail ids per arm:
the DAF arm numbers them 237=Merge / 240=cannon / 241=mumps_open
/ 243=compiler-parse misc / 244=AllocSV / 245=libmpscpp misc-7 /
246=interp-sql btree, per the pre-registered batch_plan.md; the
DOC arm numbers its own batch-8 tail differently per its committed
row order. The tail units are the same vendor bodies in both arms;
only the numeric table ids differ, and both orders are the
pre-registered ones for their arm (no arm deviated from its
committed plan).

**Clarification (logged as data; no pre-registered instrument is
modified).**

1. Both arms' orders are their committed pre-registered orders;
   neither is a deviation under design 7.4. The divergence is a
   numbering fact of the two committed plans, not an error; it is
   flagged so cross-arm batch-8 comparisons do not silently assume
   row-id correspondence.
2. Cross-arm joins for batch-8 units must key on the unit_id
   string (vendor file :: name) plus batch, per the analysis
   README's roster-row contract (C-005 unit-key registration
   precedent: ids are descriptive, joins carry roster_row or
   unit_id + arm), never on the bare numeric table id.
3. Requested PI ruling: whether to record a canonical cross-walk
   table (numeric id -> unit_id per arm for the overlapping tail
   units) checked in as analysis tooling, or to leave the join
   discipline of item 2 as the only instrument. Default if no
   ruling lands before analysis: item 2 discipline applies and
   this entry is the disclosed reason.

**Effect on the committed schedule.** None. No arm's batch work
changes; the entry binds the analysis phase and the batch-8 close
books only.

**PI ruling (2026-11-27):** Option 1 — a canonical cross-walk
table is checked in as analysis tooling at
`wp2/analysis/data/unit_id_crosswalk.csv` with its reproducible
builder `wp2/analysis/data/build_unit_crosswalk.py`. The table is
derived ONLY from the pre-registered instruments
(`wp1/study-subset/batch_plan.md`, `wp1/study-subset/units.csv`,
`wp1/order_assignments.json`), one row per roster ENTRY (308
rows), carrying: the global batch_plan ordinal (the PILOT/DAF
id), the units.csv data-row ordinal (the "record N" id used in
the coverage findings), the unit_id, lang, stratum, LOC, score,
batch, and each arm's position in its committed sequence
(base/doc/pro/daf/both_seq). Verified against the known anchors:
record 43 = interp-sql.cpp::embed (B2), record 77 =
libmpscpp.cpp::mstring::Eval (B3), global 237 =
interp-sysfunc.cpp::Merge. The join discipline of item 2
(unit_id + batch, never the bare numeric id) remains binding;
the cross-walk additionally discloses that 28 unit_id strings
recur across batches (e.g. libmpscpp.cpp::mdh_tree_level_add in
seven batches), so the roster key is the ENTRY (unit_id +
batch), not the name alone.

**Methodological consequences.** Any arm-level batch-8 metric
that groups by numeric row id would mis-pair units across arms;
the entry forbids that join and names the correct one. The
pre-registered per-arm batching (C-001) is untouched: each arm
still translates each batch exactly once in its committed
relative order.

**Files touched by this entry:** PROTOCOL_CLARIFICATIONS.md (this
entry). No pre-registered instrument is modified.

## C-014 - BOTH batch-3 coverage findings: records 43 and 77 (logged 2026-11-27, PI-ruled)

**Observation.** At the BOTH batch-8 close (2026-11-27), two
batch-3 roster entries were found to have NO renders and NO
events-CSV rows on the BOTH arm master: record 43
(interp-sql.cpp::embed, units.csv row 43, global ordinal 51) and
record 77 (libmpscpp.cpp::mstring::Eval, units.csv row 77, global
ordinal 79). The batch-8 renders raised named TODO stubs at
their owners citing both records: embed at Btree.kt (the
distinct double-quote twin embed1 IS rendered, batch 7), and
mstringEvalZb at LibmpscppMisc7.kt. DOC, DAF, and PRO carry
renders for both records per their batch-3 closes; the gap is
BOTH-side only.

**Clarification (logged as data; disclosed at the BOTH batch-8
close in the PILOT paragraph and the journal fragment).**

1. The finding is a coverage gap in the BOTH arm's batch-3
   tranche, not a cross-arm comparability break: the analysis
   joins per unit per arm, so BOTH-side nulls surface as missing
   outcome rows for exactly these two entries.
2. **PI ruling (2026-11-27):** the two renders are SCHEDULED, not
   discharged by the stubs. Both renders (embed, mstring::Eval)
   ride the BOTH batch-9 session (the next BOTH session per the
   committed row order, one session per C-011), with the cycle-10
   buffer (C-001) as the fallback if batch 9 closes without them.
   The named stubs raised at the batch-8 close stay in place
   until the real renders land, then release per the standard
   seat discipline.
3. The events CSV rows for both renders ride the batch-9 session
   band, with the retrospective gap disclosed in the row rationale
   (the render session is the consult row's session; the units'
   batch-3 provenance is noted).

**Effect on the committed schedule.** None beyond the two extra
renders inside the already-scheduled batch-9 session; if deferred
to cycle 10, the buffer cycle absorbs them by design.

**Methodological consequences.** The BOTH arm's batch-3
per-batch metrics are unaffected (the units were never claimed
rendered); when the renders land, they carry batch-9 (or buffer)
session stamps and the batch-position covariate uses the actual
session ordinal per C-001. The pair is the first
within-arm coverage finding of the study and is recorded as data.

**Files touched by this entry:** PROTOCOL_CLARIFICATIONS.md (this
entry). No pre-registered instrument is modified.


## C-015 - Cosmetic rank/grep-line mislabel family in stub pins (logged 2026-11-27, PI-ruled)

**Observation.** BothStubs-family TODO stub rank pins cite
complexity ranks or vendor grep line numbers instead of roster
ordinals. Confirmed members: allocSV "roster 285" is the
complexity rank (true ordinal 218), atCopy "roster 241" is a
vendor grep line (true ordinal 240), idfSvcZb "roster 45" is a
rank pin, and the InterpService stub pins (Scan 75, ScanAlnum 77,
IDF 45) cite ranks. The family was first disclosed at the BOTH
batch-7 close (sigint 254->190, parse_ 248->247, Extract 171->17,
multiply 213->41, Mltoa 292->291, fcn 123->94, substr 212->211)
and extended at the batch-8 close.

**Clarification (logged as data; disclosed at the batch-7 and
batch-8 closes).**

1. The pins are cosmetic comments in stub TODO lines; no gate,
   seat, or outcome value depends on them.
2. **PI ruling (2026-11-27):** the pins are RENUMBERED to true
   roster ordinals (per the C-013 cross-walk where needed), riding
   the next push of each owning arm: the BOTH-side pins (allocSV,
   atCopy, idfSvcZb, the InterpService block) on the BOTH batch-9
   push; any DAF-side pins on the DAF batch-9 push (which also
   carries the C-012 rename). Until then the family note stands
   as the disclosed record.
3. The renumber is comment-only and compile-identical; each
   renumbered pin cites this entry in its push note.

**Effect on the committed schedule.** None.

**Methodological consequences.** None for graded outcomes; the
renumber removes a cross-referencing hazard for the code-review
discipline (a rank or grep line is not a roster ordinal).

**Files touched by this entry:** PROTOCOL_CLARIFICATIONS.md (this
entry). No pre-registered instrument is modified.

## C-016 - Per-unit check instrument extended to all five arms; check-pass plan approved (logged 2026-11-27, PI-ruled same day)

**Context.** At SAP registration the committed instrument
`wp2/analysis/data/per_unit_checks.csv` carried two arms (BASE, PRO;
616 data rows, all `todo`) because DOC, DAF, and BOTH had not yet
been translated. All four agent arms completed 308/308 on
2026-11-25/27 (PRO completion audit + translation-phase close,
journal 2026-11-27, splice ef9561c). The PI approved the per-unit
check pass and ruled four scoping decisions on 2026-11-27.

**PI rulings (2026-11-27).**

1. The instrument is extended to all five arms (BASE, DOC, PRO,
   DAF, BOTH): 308 rows per arm, 1540 data rows. The three new arm
   blocks start `todo` on all four checks, identical in roster_row
   and unit_id to the registered rows.
2. The pass runs as check-type sub-passes (all code-checks first),
   per `wp2/CHECK_PASS_PLAN.md` sections 2-3.
3. The pass is agent-executed with PI spot audits (plan section 5;
   suggested 10 percent blind sample per tranche close).
4. BASE rows 52-308 remain `todo`: no BASE render exists to check
   (the BASE arm translated only the B1-B2 rows). This is reported
   in SAP Amendment 2 rather than silently dropped; the SAP timing
   statement line "BASE: 308/308 graded" is superseded by the
   amendment.

**Effect on the committed schedule.** None; the check pass is
post-translation grading work.

**Methodological consequences.** The instrument extension keeps
row/unit identity and vocabulary unchanged; the SAP's per-unit
check exports remain the only analysis inputs. Deferrals (oracle
coverage gaps) are reported data per C-002 and counted in the SAP's
N deferrals line. SAP Amendment 2 (appended to
`wp2/STATISTICAL_ANALYSIS_PLAN.md`) records the timing correction
and the instrument extension.

**Files touched by this entry:** PROTOCOL_CLARIFICATIONS.md (this
entry), STATISTICAL_ANALYSIS_PLAN.md (Amendment 2),
analysis/data/per_unit_checks.csv (three arm blocks appended),
CHECK_PASS_PLAN.md (new).


## C-017 - DAF/BOTH study-complete claims contradicted by S1-open coverage findings; S1 paused pending audit (logged 2026-11-27, PI-ruled same day)

**Context.** The per-unit check pass (S1, wp2/CHECK_PASS_PLAN.md,
C-016) opened 2026-11-27. Its opener verification found that the
DAF and BOTH "308/308 study-complete" claims (journal: "DAF arm:
308/308 rendered; 304 prior + 4 supplementary"; "BOTH ARM
STUDY-COMPLETE: 308/308 units translated") are contradicted by
concrete evidence in the arm trees. DOC and PRO hold: PRO was
verified 308/308 by the 2026-11-27 completion audit; DOC carries
complete B1-B10 batch records with 287 render files.

**Findings (all verified in source this session).**

1. Roster row 9 (interp-sysfunc.cpp::DirectRead): DAF carries
   only a NotImplementedError stub ("not drawn by the DAF arm");
   BOTH carries only a TODO stub mis-pinned as a "non-roster
   reader" - DirectRead is units.csv row 9, a batch-1 unit.
2. Roster row 2 (libmpscpp.cpp::<misc-1>, the 600-physical-LOC
   residue chunk): no id-or-name trace in either arm.
3. Roster row 6 (compiler-sys1.c::MyFunc): BOTH stub-only (pin
   says "roster 27" - a C-015-family mislabel); DAF renders it
   (myFuncDaf, CompilerParseC.kt).
4. Roster row 5 (interp-sym.cpp::_SymFree): BOTH stub-only.
5. C-014's PI-ruled scheduled renders (BOTH embed, record 43;
   BOTH mstring::Eval, record 77) NEVER LANDED: the BOTH batch-9
   close does not discharge them and both remain TODO stubs in
   the tree, yet the batch-10 close declared the arm
   study-complete.
6. The arm journal contains no DAF batch-1/2/3 and no BOTH
   batch-1-complete/2/3/4 records; the BOTH batch-1 session
   rendered 10-11 of 31 units with the remainder noted pending.
   The journal-recorded DAF batches (B4, B5, B7, B8, B9, B10 +
   the 4-unit supplementary) sum to roughly 186-216 units, not
   308.
7. Mechanical no-trace scan (upper bound, front-run false
   negatives possible per the PRO verification-only convention):
   DAF ~47, BOTH ~41 roster units with no id-or-name trace
   anywhere in the arm sources.

**PI rulings (2026-11-27).**

1. A DEDICATED COVERAGE-AUDIT SESSION runs next: a definitive
   per-unit render ledger for DAF and BOTH against the 308-unit
   roster (mechanical scan plus per-unit render verification,
   C-014 methodology), committed as data. The gap rulings (render
   sessions vs disclosed missing outcome rows per the SAP join)
   follow that audit.
2. S1 (the code-check pass) is PAUSED ENTIRELY until the audit
   is ruled. No per_unit_checks.csv flips from the S1 opener;
   the opener's completed reviews (DirectRead: BASE
   graded-deviation candidate - writes a space where the vendor
   writes NUL, unaccounted; DOC and PRO graded-ok candidates -
   all branches and vendor quirks verified) are recorded here
   and re-graded at S1 resume.
3. The 2026-11-27 PRO completion-audit journal paragraph stated
   "all four agent arms stand at 308/308" - that line is
   CORRECTED by this entry: PRO and DOC are verified 308/308;
   the DAF and BOTH claims are under audit with the concrete
   counterexamples above. The PRO arm's completion and the
   translation-phase record for PRO/DOC stand unchanged.

**Effect on the committed schedule.** The check pass was
post-translation grading work; its pause has no schedule effect.
The DAF/BOTH question may re-open translation work for those two
arms; that is ruled after the audit.

**Methodological consequences.** None yet to registered
instruments; per_unit_checks.csv remains all-todo. If the audit
confirms gaps and the PI rules missing-outcome-row treatment, the
SAP's per-unit joins already surface BOTH/DAF nulls as missing
outcome rows exactly as C-014 established for its pair; a render
ruling instead would restore the missing rows with render-session
stamps per C-014's session-ordinal treatment.

**Files touched by this entry:** PROTOCOL_CLARIFICATIONS.md (this
entry); ARM_WORKING_NOTES.md (journal note, same splice).

