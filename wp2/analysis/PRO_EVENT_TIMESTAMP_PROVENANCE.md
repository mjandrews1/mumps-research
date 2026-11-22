# PRO event timestamp provenance (analysis-side note)

Registered by protocol clarification **C-009** (2026-11-22).
This note records, for the analysis phase, what the `timestamp`
column of `wp2/logs/PRO_knowledge_events.csv` does and does not
measure, with the row-level evidence. The CSV itself is
append-only and is NOT edited by this note; every fact below is
derivable from the CSV as committed, from the git history, and
from the arm journal.

## 1. The instrumental contract and what actually happened

`wp2/arms/PRO/knowledge_tooling.pl` records events through
`record_reuse/2`, `record_hit/2`, `record_miss/2`, each appending
one CSV row whose timestamp is taken from `get_time` **at record
time** (second precision). Only rows 2-31 of the CSV (roster
units 1-2, B1) match that shape:

```
2026-11-15T14:54:12+0000   (unit 1, copyright-notice.c, rows 2-9)
2026-11-15T14:56:13+0000   (unit 2, compiler-if.c, rows 10-31)
```

From row 32 onward, every stamp is minute/hour-aligned
(15:10, 15:20, 15:35, 21:20, 22:10, 00:15, 01:00, 09:00, ...),
a shape the `get_time` path cannot produce: 3,658 of 3,688 event
rows were hand-appended with nominal timestamps. The arm journal
discloses the ladders per tranche (e.g. the B4 group-3 ledger:
"Events +106 rows (u114 09:00 x5 rules ... u123 18:00 x5, all
2026-11-24 +0000)"), so the stamps function as labels, not as
clock evidence; C-009 registers that reading.

## 2. Provenance classes (row ranges are current CSV lines)

Line 1 is the header. All row references are to the file as
committed at the C-009 registration sha; the only intervening
log-integrity correction (commit 8039e9d, 2026-11-18, one blank
line excised at batch-3-close append window) predates this note
and is journaled.

| Class | Rows | Count | Stamp shape | Nominal dates | Committed |
|---|---|---|---|---|---|
| A. tool-recorded | 2-31 | 30 | true seconds | 2026-11-15 | 2026-11-15 (matches) |
| B. authored, near-dated | 32-1211 | 1,180 | round ladders (:00/:15/:30...) | 2026-11-15 .. 2026-11-22 | 2026-11-15 .. 2026-11-18 |
| C. authored, future-dated | 1212-3689 | 2,478 | round ladders (hour steps per unit) | 2026-11-23 .. 2026-12-02 | 2026-11-17 .. 2026-11-18 |

Class C is defined by the wall clock of the commits: the first
future-dated row is 1212 (`reuse,h004,interp-rpm.cpp,
2026-11-23T09:00:00+0000`), committed 2026-11-17; the last rows
(3687-3689, `libmpscpp.cpp::global::Justify`, stamped
2026-12-02T12:00:00+0000) landed in commit c852e615 on
2026-11-18T19:57Z. 67% of the log therefore claims event dates
up to 14 days after they were written.

## 3. Nominal per-day stamp distribution vs commit chronology

Event rows per nominal stamp date (all committed by 2026-11-18):

| Nominal date | Rows | Nominal date | Rows |
|---|---|---|---|
| 2026-11-15 | 149 | 2026-11-24 | 162 |
| 2026-11-16 | 196 | 2026-11-25 | 260 |
| 2026-11-17 | 110 | 2026-11-26 | 260 |
| 2026-11-18 | 173 | 2026-11-27 | 350 |
| 2026-11-19 | 210 | 2026-11-28 | 286 |
| 2026-11-20 | 180 | 2026-11-29 | 304 |
| 2026-11-21 | 20 | 2026-11-30 | 310 |
| 2026-11-22 | 172 | 2026-12-01 | 298 |
| 2026-11-23 | 112 | 2026-12-02 | 136 |

(One row dated 2026-11-15 within a later correction window also
appears; the 2026-11-19/20/21 stamps belong to the B3 batch-close
correction appends, whose journal paragraph is itself dated
2026-11-21 while the landing commit 41fc507 is 2026-11-17T05:19Z
- the same nominal-calendar practice recorded here.)

Batch-to-commit anchors (git-authoritative append windows):

| Batch | Representative append commits | Commit date range | Nominal event days |
|---|---|---|---|
| B1 (31 u) | 8b640c0, d116023 | 2026-11-15 | Nov 15 |
| B2 (31 u) | 25df3cc, 95dd870, c25bd86c | 2026-11-16 | Nov 16-17, 19 |
| B3 (31 u) | 5b41baf, a62b92b, 41fc507 | 2026-11-17 | Nov 19-21 |
| B4 (30 u) | fba3d73 .. 2b0fd22b | 2026-11-17 | Nov 22-24 |
| B5 (31 u) | cef8e03 .. 7789708b | 2026-11-17 | Nov 24-25 |
| B6 (30 u) | 0062d55 .. c5673e77 | 2026-11-17 | Nov 26 |
| B7 (30 u) | 2b6e682 .. 0a283615 | 2026-11-18 | Nov 27-28 |
| B8 (30 u) | d5d5854f | 2026-11-18 | Nov 28-29 |
| B9 (30 u) | 280121aa | 2026-11-18 | Nov 29-30 |
| B10 (30 u) | b8647fb3 | 2026-11-18 | Nov 30-Dec 1 |
| B11 (3 u) | c852e615 | 2026-11-18 | Dec 2 |
| log integrity | 8039e9d1 (blank-line excision) | 2026-11-18 | n/a |

(Short commit ids for readability; resolve to the full shas via
`git log --follow wp2/logs/PRO_knowledge_events.csv`.)

## 4. Rules for the analysis phase (per C-009)

1. The PRO `timestamp` column is **nominal**. Inadmissible as
   evidence for ordering, pacing, session structure, cadence, or
   any time-derived covariate. Do not compute durations, inter-
   arm pace comparisons, calendar alignments, or time-series
   features from it.
2. **Canonical PRO chronology = git commit history.** Analyses
   needing PRO order or timing (the C-003 practice-effect cells,
   actual completion ordinals for the batch-position covariate)
   use the commit anchors above, not event stamps.
3. **Content columns stand.** `kind`, `target`, `unit` remain
   admissible: coverage_report arithmetic is stamp-independent,
   and the study-close figures (1,856 triggering situations;
   1,851 hits / 5 misses; hit rate 99.7%, reuse rate 99.0%)
   reconcile against the journal irrespective of stamps. The 5
   misses are the B1 preprocessor_conditional pre-capture units
   (4/10/12/20/29), excluded per the SAP rule.
4. **Provenance flag, not CSV edits.** Derive the class of each
   row by row number (A: 2-31, B: 32-1211, C: 1212-3689) at
   load time; any analysis-side join table carries this flag.
   The CSV is append-only and is not modified.
5. **Record-at-consult evidence** for PRO rests on the arm
   journal's per-unit paragraphs and per-file KDoc annotations,
   not on event timestamps; consult timing is unverifiable from
   the log (C-006-style limitation, recorded openly).
6. **Sensitivity analyses** must re-run any PRO figure that
   would have consumed timestamps under commit chronology, and
   report both. The instrumentation calibration (section 5)
   attaches the nominal-stamp qualification to PRO's
   hit/reuse/miss counts.
7. **Other arms are not implicated.** DOC events are exported
   from journal consult rows with the consult timestamps of the
   journal (C-005/C-008 machinery); DAF consult tracking is the
   journal ledger; BOTH rows are swipl-recorded. A separate
   per-arm audit can confirm the same stamp-shape test; this
   note's scope is PRO.
