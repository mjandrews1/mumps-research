
## PRO arm completion audit + TRANSLATION PHASE CLOSE (2026-11-27)

- PI DECISION (Mark, 2026-11-27): the PRO arm is ACCEPTED AS
  COMPLETE and the study TRANSLATION PHASE IS DECLARED DONE. All
  four agent arms stand at 308/308: DOC, DAF, PRO, BOTH; all
  pushes landed and spliced; MacBook gradle gates green on BOTH
  and DOC (splice d0f69fe round).
- AUDIT CONTEXT: a pre-audit session read PILOT_TRACKING.md and
  concluded the PRO arm still owed 60 rows (units.csv rows 32-93,
  "246/308"). The audit disproved the premise. The batch 2-10
  PILOT tables carry NO PRO column (their schema is
  # / Unit / Lang / Class / Status / DOC / DAF / BOTH / the four
  C-002 check columns); only the B1 table has a PRO column. The
  "todo" entries at rows 32-93 and beyond are the BASE arm's
  Status column (todo for 257 of 308 rows; graded-ok only for
  rows 1-51) plus the four check columns, which are todo table-
  wide per the C-001/C-002 backfill decisions. No agent arm fills
  the Status column; those todos are grading bookkeeping, not
  unrendered units.
- EVIDENCE OF PRO 308/308 (verified this session against the
  working tree at the splice-d0f69fe state): the arm journal's own
  study-close ledger (B11, rows 307-309) states "units.csv rows
  2-309 (308 units) ALL CLAIMED... no unrendered roster rows";
  batch history B1 closed 31/31, B2 close all 31 green, B3
  complete (63-93), B4-B9 closes, B10 rows 277-306, B11
  STUDY-CLOSE; renders physically present (279 kt files under
  mumps/{compiler,interp,mdh}; spot-verified row 32-93 seats
  including GlobalExtract.kt, GlobalFind.kt, SQLTable.kt,
  Fnumber.kt, MpsAtan.kt, MpsSqrt.kt, IfCode.kt); events CSV at
  3689 rows; B10-close coverage baseline 18 rules, hit 99.7%,
  reuse 98.9%; all m1-build gates green through B11 close.
- BOOKKEEPING TRAP RECORDED (cross-batch lesson): the PRO
  journal's "- Unit N" numbers are crosswalk global_unit_ids,
  NOT units.csv rows (e.g. mps_atan = journal Unit 85 but
  units.csv row 71). PRO units must be matched to PILOT rows by
  unit id string, never by number. The pre-audit session's 60-row
  misread combined this with the Status/check-column confusion.
- NEXT (PI choices, same session): (1) a per-unit check pass for
  the C-002 check columns is to be planned (grading work, no new
  renders); (2) the BASE Status column (257 todo rows) remains a
  PI grading decision; (3) the study moves to the write-up /
  analysis phase of the five-arm comparison.

Mark's splice: cat this fragment >> the arm journal
(wp2/arms/ARM_WORKING_NOTES.md; the working tree carries the
earlier splices; do NOT clobber); git add
wp2/arms/ARM_WORKING_NOTES.md; git commit -m "PRO completion
audit + translation phase close spliced (journal append)"; git
push; rm wp2/arms/PRO-CLOSE-AUDIT_fragment_2026-11-27.md. No
gate round needed: no code changed (journal-only append; the
BOTH/DOC gradle gates stand green from the d0f69fe round).
