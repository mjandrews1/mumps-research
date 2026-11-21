# BOTH B5 T1 journal fragment, 2026-11-21

### BOTH arm opened + B5 tranche 1 (units 124-133, simple stratum) - 2026-11-21

Arm provisioning (first BOTH commit per the C-001 Latin square; BOTH's
cycle-1 batch is B5, so this tranche is exactly on schedule). Sandbox
~/b4 rebuilt from scratch this session: vendor tree sha256 verified
(621deb...ffdbe, 190531550 bytes), JDK17 + kotlinc 2.1.20 (kcd sha
a118197b...), Dafny 4.11.0 (4.11.0+fcb2042d6d043a2634f0854338c08feeaaaf4ae2,
matches m1-build; needs DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1 in this
sandbox, no libicu), repo zip = master @bcc9a11. Prolog note: swi-prolog
is NOT installed in the render sandbox (and package installs are
forbidden there), so the rule-base sessions below ran as recorded
consult ledgers per the BOTH-adapted knowledge_tooling.pl workflow; the
swipl coverage_report run is deferred to m1-build at tranche close.
Per RESEARCH_DESIGN 5.4 and ARM_WORKING_NOTES 4: BOTH = the
Prolog-rules + Dafny cell. Rule base is a SEPARATE file
wp2/arms/BOTH/rules_v0.pl seeded verbatim from PRO's v0 (18 rules
h001-h018, identical start condition); BOTH-local
knowledge_tooling.pl adapted to the BOTH_knowledge_events.csv path;
PRO's artifact was NOT edited (arm-independence kept). DAF workflow
runs per-unit Dafny specs under wp2/arms/BOTH/spec/ with verification
outcomes feeding back into the rule base.

Arm scaffolding committed this push: wp2/arms/BOTH/README.md,
build.gradle.kts (rootProject.name="mumps-both-arm") +
settings.gradle.kts, rules_v0.pl + knowledge_tooling.pl (both seeded
from PRO master; their em-dash content mirrors PRO's file verbatim),
spec/smoke_test.dfy (DAF smoke copy, date bumped; 2 verified 0/0).
Both-tree kotlinc gate baseline green at open.

dump4.py recreated; all 10 T1 unit dumps verified at exact physical
LOC (30/30/21/17/15/16/17/19/22/23; the three mdh_tree_level_add
overloads disambiguated by LOC with __locNN filename suffixes).
Tranche follows the committed batch-5 rank order (simple stratum
first). Rendered from pristine vendor sources + rules_v0.pl
consultations only; no DOC/DAF/PRO Kotlin consulted.

ROSTER NOTE (journal): the batch-5 PILOT table (after DAF's B5T1
push it was 11-column with a DAF field) had no BOTH column. This
push reformats the headers + separator to the 12-column shape (BOTH
column between DAF and the code-check columns, per the DAF
7cf10d2/bd68a65 precedent) and inserts the BOTH field into all 31
batch-5 rows (todo), marking 124-133 translated. Diff = the batch-5
table region only. DEVIATION this push repairs: pre-existing row 154
(libmpscpp.cpp::<misc-5>) had been one cell short AND missing its
trailing pipe since the table's origin (the DAF close preserved the
missing trailing pipe as-is); inserting the BOTH cell into a
10-cell row would have misaligned BOTH into the code-check column.
Row 154 was therefore NORMALIZED to the standard 12-cell +
trailing-pipe shape (verified: 13 pipes, matching the header). This
is a table repair, not a data change: all row-154 values remain
todo/translated as before. Flagged for PI ruling at the close gates.

Adjudication notes (render decisions mirror the DAF B5T1 arm report
where the same vendor kernel governs; BOTH-specific deviations are
the two-workflow consult ledger + rule-base reuse bookkeeping):
units 124/125 (compiler-for.c/compiler-localarray.c residue, GPL
banner + include set) are pure TU residue objects: h014 hit-only,
ADAPTED (KDoc-only carrier, no stub body possible), residues per the
T1-T3 precedent. Units 126-128 mdh_tree_level_add are the THREE
same-named C++ overloads by arity: one Kotlin overload set over a
shared mdhTreeLevelAddImpl kernel; sprintf %p handled as the
DOCUMENTED DIVERGENCE (no native pointer on the JVM: opaque Int
handle in %p hex shape), pointer_handle MISS x3 (svPtr-style carrier
hierarchy does not reach raw C pointers; miss targets capture at
T1 recurrence 3 for the batch-close report); zfcn TODO stub naming
roster owner interp-zfcns.cpp::zfcn (roster b6). Unit 129
mstring::Length: h007 receiver fold, h014 retrieve-then-throw
carriage (GLOBAL_NOT_FOUND=2 pinned), h017 string-builder fold;
class_member_carrier miss (mstringClass.h::<misc> roster 149:
class-receiver seat, no prior rule); VENDOR QUIRK kept exactly:
an EMPTY needle (global exists, value empty) makes the vendor
strstr-count walk nonterminate; the Kotlin indexOf walk preserves
the vendor shape and the spec contracts only the reaching states.
Unit 133 global::Length is the same kernel through the node
receiver (two retrieves, ref first per vendor order), sharing the
length_count.dfy spec with 129; class_member_carrier miss
(globalClass.h::<misc> roster 89). Unit 130 Avg: h008 coded-arg
split fold (byte-1 coded splits; i>=2 no-separator-guarantee UB
quirk kept, ledgered), h009 sprintf %g seat (fresh String via
fmtG over the 8-digit gcvtWrapper vs printf's 6-digit seat),
h012 char tmp[32] buffer fold, h014 rung/stub carriage, h017
builder fold; avg(sub) TODO seat on the GlobalNode carrier naming
owner libmpscpp.cpp global-class Avg (roster 301, not a corpus
unit; permanent stub unless a later batch renders the family).
Unit 131 global::Translate: h007 receiver fold, h015
retrieve-idiom, h014 stub seats, h017 builder fold (mstring Concat
chain of the coded $translate envelope xce/xd0/xcf, folded onto a
String builder written into v1d AT OFFSET 1, bd[1] read back);
fcn(svPtr) relay raised as a TODO stub naming roster owner
localArray.c fcn (roster 123) and its consumer seat; DAF's B4T3
dafFcn flip has NO analogue here yet (BOTH has no T3 history:
this is arm batch 1, cycle 1); class_member_carrier miss
(globalClass.h::<misc> roster 89). Unit 132 mps_log10: h018
live-arm lattice (reference arm gcvt_wrapper(log10(strtod(b))),
MPFR and long-double branches dead), h009 format seats, h017
builder fold; the C library nonpositive-domain pathology
unguarded, ledgered per the asin precedent; excluded arms
documented in-spec.

Dafny ledger (BOTH arm, batch 1: the two-workflow rules here are
PRO-shaped, wrapped around the same pure-decidable-unit scope used
by the DAF arm; exemptions logged, per-unit): exempt units
124/125 (residue, no bodies), 126-128 (GTK service-call shaping
over the zfcn seat), 130 (global-access wrapper over carrier
seats), 131 (service-call shaping over the fcn dispatcher seat).
Specs rendered and verified (in-sandbox dafny 4.11.0):
spec/length_count.dfy (independent of DAF's, authored for BOTH:
PieceCount/Find/MatchAt walk with empty-needle precondition
mirroring the nontermination quirk guard; 5 verified 0 errors)
and spec/mps_log10.dfy (h018 lattice + LiveArmIsPlainDouble +
DigitPrefixBounds iterative parse bound; 5 verified 0 errors).
SPEC CORRECTION AT GATE (disclosure): the first full-suite run
caught a REAL error in mps_log10.dfy's DigitPrefixBounds - the
draft contract ensured v == 0 <==> |s| == 0, which is FALSIFIABLE
(an all-zero digit string, e.g. "0", parses to v == 0 with |s| >
0); the loop invariant v == 0 <==> k == 0 failed identically.
Contract and invariant were corrected to v == 0 <==> (|s| == 0 ||
all digits zero) before any push; the corrected spec verifies 5/0.
This is exactly the BOTH feedback loop working as designed
(verification outcomes policing rule-base reuse claims); recorded
as the first BOTH-generated evidence event.

Full suite in-sandbox: smoke_test + length_count + mps_log10 =
12 verified, 0 errors (2 + 5 + 5).

Gates: whole-arm kotlinc 2.1.20 silent PASS at 15 classes (11 new
.kt renders + GlobalRef/GlobalNode/Msv/MumpsGlobalException
carrier classes); H-015 scan clean on all touched files (no
backticks, no double-bangs, no raw dollar outside escapes);
non-ASCII confirmed only in the two .pl rule files (em dashes
mirroring PRO's master, expected) and the pre-existing PILOT doc
text. Dafny verify green above; gradle + dafny + optional swipl
coverage_report re-run at tranche close on m1-build (Mark).

TIMESTAMP DISCLOSURE (C-006-style bounded reconstruction, flagged
for PI ruling at the gates): the events CSV rows were recorded at
row-assembly time after the renders, as 1-second-step sequence
2026-11-21T02:05:01+0000 through 02:06:11+0000, not at the moment
of each individual consultation during rendering. Kind, rule id,
and unit are exact; only the intra-tranche ordering/spacing is
reconstructed as a dense sequence.

Renders and consult ledger (each consult logged before applying;
kind = event kind in wp2/logs/BOTH_knowledge_events.csv, 71 rows
= 30 reuse + 35 hit + 6 miss):
- BOTH consult: unit compiler-for.c::<misc>, kind hit, rule h014, outcome hit-adapt (KDoc-only residue carrier), timestamp 2026-11-21T02:05:01+0000
- BOTH consult: unit compiler-localarray.c::<misc>, kind hit, rule h014, outcome hit-adapt (KDoc-only residue carrier), timestamp 2026-11-21T02:05:02+0000
- BOTH consult: unit libmpscpp.cpp::mdh_tree_level_add, kind reuse, rule h007, outcome receiver-fold reuse, timestamp 2026-11-21T02:05:03+0000
- BOTH consult: unit libmpscpp.cpp::mdh_tree_level_add, kind hit, rule h009, outcome hit-adapt (%p Int-handle divergence), timestamp 2026-11-21T02:05:05+0000
- BOTH consult: unit libmpscpp.cpp::mdh_tree_level_add, kind reuse, rule h015, outcome builder-fold reuse, timestamp 2026-11-21T02:05:06+0000
- BOTH consult: unit libmpscpp.cpp::mdh_tree_level_add, kind reuse, rule h014, outcome stub-carriage reuse, timestamp 2026-11-21T02:05:08+0000
- BOTH consult: unit libmpscpp.cpp::mdh_tree_level_add, kind reuse, rule h017, outcome string-builder reuse, timestamp 2026-11-21T02:05:10+0000
- BOTH consult: unit libmpscpp.cpp::mdh_tree_level_add, kind miss, rule pointer_handle, outcome miss (opaque Int handle; capture at T1 recurrence 3), timestamp 2026-11-21T02:05:12+0000
- BOTH consult: unit libmpscpp.cpp::MstringLength/GlobalLength pair, kind reuse x6 (h007/h014/h017 per unit), outcome kernel reuse, timestamps 2026-11-21T02:05:13-02:05:38+0000
- BOTH consult: unit libmpscpp.cpp::mstring::Length, kind miss, rule class_member_carrier, outcome miss (mstringClass.h member seat, roster 149), timestamp 2026-11-21T02:05:39+0000
- BOTH consult: unit libmpscpp.cpp::Avg, kind reuse x5 (h008/h009/h012/h014/h017), outcome folds as adjudicated, timestamps 2026-11-21T02:05:40-02:05:49+0000
- BOTH consult: unit libmpscpp.cpp::global::Translate, kind reuse x4 (h007/h015/h014/h017), outcome folds as adjudicated, timestamps 2026-11-21T02:05:50-02:05:57+0000
- BOTH consult: unit libmpscpp.cpp::global::Translate, kind miss, rule class_member_carrier, outcome miss (globalClass.h member seat, roster 89), timestamp 2026-11-21T02:05:58+0000
- BOTH consult: unit interp-arith.cpp::mps_log10, kind reuse x3 (h018/h009/h017), outcome live-arm lattice reuse, timestamps 2026-11-21T02:05:59-02:06:04+0000
- BOTH consult: unit libmpscpp.cpp::global::Length, kind miss, rule class_member_carrier, outcome miss (globalClass.h member seat, roster 89), timestamp 2026-11-21T02:06:11+0000 (reuse rows at 02:06:05-02:06:10)
- Both-workflow Dafny events: kind miss, rule pointer_handle, unit libmpscpp.cpp::mdh_tree_level_add, x2 further recurrences at 02:05:22 and 02:05:32+0000 (overload repeats, logged per-overload)

Miss-capture candidates for the batch-close report: pointer_handle
(x3, mdh_tree_level_add overloads, T1 recurrence 3) and
class_member_carrier (x3, the mstring/global member units). Both
flagged for rule-base promotion review at BOTH B5 close per the
two-workflow feedback loop.

BOTH B1 pilot tracking cell: as with the DAF arm at its open, the
BOTH B1 batch-1 row in PILOT_TRACKING remains pending the
translator's own pass over the B1 units; noted, not changed this
push.

Files pushed (one commit):
- Arm scaffolding under wp2/arms/BOTH/: README.md,
  build.gradle.kts, settings.gradle.kts, rules_v0.pl (18-rule seed
  from PRO v0), knowledge_tooling.pl (BOTH-path adaptation),
  spec/smoke_test.dfy (2 verified 0/0)
- 10 new renders + runtime under wp2/arms/BOTH/src/main/kotlin/both/:
  BothCRuntime.kt (STR_MAX/RETRIEVE/GLOBAL_NOT_FOUND consts, Msv +
  Mglobal/zfcn/fcn TODO owner-naming stubs, cStringOf),
  BothNumRuntime.kt (8-digit gcvtWrapper + 6-digit fmtG/cFormatG
  C99 %g w/ trailing-zero strip + nan/inf, strtodC prefix parse),
  BothStubs.kt (GlobalRef/GlobalNode carriers, avg owner-301 seat),
  CompilerForMiscB5.kt (124 residue), CompilerLocalArrayMiscB5.kt
  (125 residue), MdhTreeLevelAdd.kt (126/127/128 overload set),
  MstringLength.kt (129), Avg.kt (130), GlobalTranslate.kt (131),
  MpsLog10.kt (132), GlobalLength.kt (133)
  (all gate-verified at 15 classes; H-015 scanned)
- 2 new specs under wp2/arms/BOTH/spec/: length_count.dfy (5
  verified 0/0), mps_log10.dfy (5 verified 0/0 after the
  DigitPrefixBounds contract correction disclosed above; full
  suite 12 verified in-sandbox)
- wp2/logs/BOTH_knowledge_events.csv (NEW: header + 71 rows, 30
  reuse + 35 hit + 6 miss, PRO schema, footer conventions
  consistent with knowledge_tooling.pl split)
- wp2/PILOT_TRACKING.md (batch-5 table reformatted to the
  12-column shape with a BOTH column; rows 124-133 BOTH=translated,
  134-154 todo; row-154 normalization disclosed above)
- wp2/arms/BOTH_B5T1_fragment_2026-11-21.md (this fragment)
