# DOC B8 T1 consult-rows repair (2026-11-21)

Instrument repair for the batch-8 tranche-1 section above: its
consult rows were written in the prose bullet form of the close
sections instead of the single-line DOC consult grammar
registered in wp2/analysis/README.md and parsed by
wp2/analysis/R/export_doc_events.R (strict regex, C-008 one row
per unit). The events CSV push (commit 6050da2) carries the 22
rows already; these journal rows are the missing source-of-truth
entries, with timestamps identical to the CSV rows (02:48 to
02:58Z, one-minute steps). After splicing, the R regen replays
668 consult rows -> 1334 event rows and the regenerated CSV is
byte-identical to the tracked file (working tree clean on the
re-run). Deviation logged: fragment grammar error, repaired
same-session, no data affected (the CSV was never wrong).

- DOC consult: unit include/mumpsc/builtinTable.h::<misc>, entry H-015, outcome hit-adapt, timestamp 2026-11-21T02:48:00Z
- DOC consult: unit libmpscpp.cpp::mstring::Find, entry H-014, outcome hit-reuse, timestamp 2026-11-21T02:49:00Z
- DOC consult: unit compiler-kill.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-21T02:50:00Z
- DOC consult: unit compiler-lock.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-21T02:51:00Z
- DOC consult: unit compiler-set.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-21T02:52:00Z
- DOC consult: unit include/mumpsc/zfcns.h::<misc>, entry H-018, outcome hit-reuse, timestamp 2026-11-21T02:53:00Z
- DOC consult: unit compiler-mumps.c::atCopy, entry H-014, outcome hit-adapt, timestamp 2026-11-21T02:54:00Z
- DOC consult: unit libmpscpp.cpp::IDF, entry H-009, outcome hit-reuse, timestamp 2026-11-21T02:55:00Z
- DOC consult: unit libmpscpp.cpp::global::Piece, entry H-007, outcome hit-reuse, timestamp 2026-11-21T02:56:00Z
- DOC consult: unit libmpscpp.cpp::GlobalData, entry H-004, outcome hit-adapt, timestamp 2026-11-21T02:57:00Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-015, outcome hit-reuse, timestamp 2026-11-21T02:58:00Z
