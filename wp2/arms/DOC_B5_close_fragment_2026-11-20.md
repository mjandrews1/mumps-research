### DOC B5 close — coverage checklist (units 124-154), 2026-11-20

Manual batch-close coverage checklist per the DOC batch-close
discipline (section 1: for each canonical construct in the batch,
did at least one entry match?). Batch 5 (units 124-154) CLOSED:
31/31 translated 2026-11-20 in three tranches per the
pre-registered batch_plan.md stratification (T1 124-133 simple,
commits 02aff5f/e1e868a; T2 134-143 core, code 279a6ab and journal
1eed85e; T3 144-154 complex, code b8cde8da, journal repaired
after the truncation incident, see the T3 section's TOOLING
INCIDENT ledger and repair commits 82651bd5/8197a54).
Gates: whole-arm kotlinc 2.1.20 0 errors / 0 warnings at every
tranche close (135 -> 144 -> 155 files); gradle gate on m1-build
BUILD SUCCESSFUL at every tranche, all first-pull, zero gradle
fix rounds (T1 12s, T2 15s, T3 19s - the four T2 compile slips
and the T3 Getstr2/MumpsMainUnit150/take_char fixes were all
caught by the local pre-push gate, logged as data). Events
export regeneration via export_doc_events.R follows this
checklist per the C-005/C-008 keys.

Entry-by-entry coverage across the 31 B5 unit journals (129
consult rows; the mdh_tree_level_add triple 126-128 shares one
vendor roster id, its three consults carry distinct timestamps
per the C-008 (unit, entry, timestamp) triple key; none
consulted an entry without applying it; journal counts below
count distinct unit ids, consult counts count rows):

- Canonical IO type (H-001): matched in 8 B5 unit journal(s); 8 consults, 2 reuse / 6 adapt
- Stateful macro (H-003): matched in 1 B5 unit journal(s); 1 consult, 1 reuse
- File-scope globals (H-004): matched in 8 B5 unit journal(s); 8 consults, 7 reuse / 1 adapt
- Out-parameter -> holder array (H-006): matched in 3 B5 unit journal(s); 3 consults, 3 reuse
- Buffer role classification (H-007): matched in 5 B5 unit journal(s); 5 consults, 5 reuse
- Shared static scratch buffer (H-008): matched in 5 B5 unit journal(s); 5 consults, 5 reuse
- sprintf format-string idiom (H-009): matched in 5 B5 unit journal(s); 5 consults, 5 reuse
- Numeric promotion / truncation (H-010): matched in 7 B5 unit journal(s); 7 consults, 7 reuse
- goto -> structured control (H-011): matched in 1 B5 unit journal(s); 1 consult, 1 reuse
- Unsigned char domain mapping (H-012): matched in 5 B5 unit journal(s); 5 consults, 5 reuse
- Dead store / dead parameter (H-013): matched in 10 B5 unit journal(s); 10 consults, 10 reuse
- Out-of-scope C function -> stub (H-014): matched in 23 B5 unit journal(s); 25 consults, 17 reuse / 8 adapt
- Kotlin grammar vs quoted C source (H-015): matched in 9 B5 unit journal(s); 11 consults, 7 reuse / 4 adapt
- snake_case -> camelCase (H-017): matched in 27 B5 unit journal(s); 29 consults, 28 reuse / 1 adapt
- Preprocessor conditional arms (H-018): matched in 6 B5 unit journal(s); 6 consults, 6 reuse

H-002, H-005 and H-016 found no triggering situation in B5 (no
consult row; state recorded so entry-for-entry parity with
PRO's rule base is unaffected; no revision needed).

Consult outcome totals for the batch: 109 hit-reuse, 20
hit-adapt, 0 miss (129 consult rows). Hit rate 129/129 = 100.0%
(every consult matched an entry); reuse rate 109/129 = 84.5%
(adaptation, not failure, accounts for the remainder). Per
tranche: T1 35 consults (31 reuse / 4 adapt, 88.6% reuse), T2 41
consults (38 / 3, 92.7%), T3 53 consults (40 / 13, 75.5%) - the
complex stratum carries the heaviest adaptation load of the
study so far (the misc-5 GTK lattice, the FILE-star seats and
the new-seat raises of tranche 3). No miss rows: the B5 canon
was fully covered by cookbook v0 as it stood at batch open, so
no capture candidate exists under T1/T2/T3; cookbook v0 stays
at H-001..H-018 and remains entry-for-entry parallel with
PRO's rules_v0.pl (h001..h018).

Per-unit consult coverage (unit -> entries consulted,
batch_plan order; rows 126-128 share the mdh_tree_level_add id):

- compiler-for.c::<misc>: H-004, H-017
- compiler-localarray.c::<misc>: H-004, H-017
- libmpscpp.cpp::mdh_tree_level_add (roster rows 126/127/128, distinct stamps): H-014, H-015, H-017 per row
- libmpscpp.cpp::mstring::Length: H-007, H-012, H-014, H-017
- libmpscpp.cpp::Avg: H-008, H-009, H-013, H-014, H-017
- libmpscpp.cpp::global::Translate: H-012, H-014, H-015, H-017
- interp-arith.cpp::mps_log10: H-009, H-010, H-014, H-017, H-018
- libmpscpp.cpp::global::Length: H-007, H-012, H-014, H-017
- libmpscpp.cpp::GlobalQS: H-012, H-013, H-014, H-015, H-017
- compiler-parse.c::ExpandData: H-001, H-017
- libmpscpp.cpp::global::Sum: H-010, H-014, H-017
- libmpscpp.cpp::Transpose: H-001, H-008, H-013, H-014, H-017
- libmpscpp.cpp::Multiply: H-014, H-017
- interp-arith.cpp::expx: H-008, H-010, H-014, H-017, H-018
- interp-sysfunc.cpp::getstr1: H-001, H-006, H-007, H-010, H-013, H-017
- interp-strmanip.cpp::<misc>: H-007, H-010, H-013, H-014, H-017, H-018
- interp-sym.cpp::SysDump: H-001, H-010, H-013, H-014, H-017
- interp-rpm.cpp::parse_count: H-014, H-017
- include/mumpsc/sym.h::<misc>: H-004, H-013, H-014, H-018
- interp-sym.cpp::Qlength: H-006, H-009, H-014, H-017
- libmpscpp.cpp::SynInit: H-001, H-006, H-014, H-017
- interp-rpm.cpp::scan_token: H-014, H-015, H-017
- interp-bmgsubs.c::bmg_search: H-001, H-014, H-017
- include/mumpsc/mstringClass.h::<misc>: H-004, H-017
- mumps.cpp::main: H-001, H-003, H-009, H-011, H-013, H-014, H-015, H-017, H-018
- include/mumpsc/compiler-globals-ext.h::<misc>: H-004, H-008, H-013, H-014, H-015, H-017
- interp-rpm.cpp::<misc>: H-004, H-014, H-015, H-017
- rdbms-if.c::sql: H-004, H-015, H-018
- libmpscpp.cpp::<misc-5>: H-001, H-004, H-007, H-008, H-009, H-010, H-012, H-013, H-014, H-015, H-017

Close decisions: none beyond the registered instruments - zero
misses mean no capture candidate exists under T1/T2/T3, and the
per-unit check columns stay todo per the C-001 cycle-10
backfill decision (C-002 discipline; the batch-5 table's check
columns track the BASE reference translation). The stub-owner
corrections of this batch (Qlength, SynInit, the rpm family,
global::Avg) are each journaled at their unit; no registry
action required. PILOT_TRACKING's DOC column is "translated"
for rows 124-154; the "DOC B5 closed" prose line lands in the
same commit as this checklist. Next protocol step after the
export regeneration: batch 6 per the committed row order (DOC
follows B5; DAF B1 start remains the other open front).
