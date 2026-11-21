# DAF B5 T1 journal fragment, 2026-11-22

### DAF B5 tranche 1 (units 124-133, simple stratum) - 2026-11-22

Sandbox rebuilt from scratch this session (fresh env reset; the B4
close already spliced): vendor tree sha256 verified (621deb...ffdbe,
190531550 bytes), JDK17 + kotlinc 2.1.20 restored (kcd sha
a118197b... 4891a), Dafny 4.11.0 reinstalled (4.11.0+
fcb2042d6d043a2634f0854338c08feeaaaf4ae2, matches m1-build),
repo zip = master @bd68a65 (36 kt in DAF arms tree = 24 committed + 12
T3; spec suite verified 27/0/0 baseline before any render), gate
baseline silent EXIT=0 46 classes before the first render. dump4.py
recreated (recipe pattern; load_units deletes bodies so extraction
redone per the recipe note): all 10 T1 unit dumps verified at exact
physical LOC (30/30/21/17/15/16/17/19/22/23; the three
mdh_tree_level_add overloads disambiguated by LOC with .locNN
filename suffixes). Tranche follows the committed batch-5 rank order
(simple stratum first per batch_plan). Arm independence kept:
rendered from pristine vendor sources + BASE-heuristic-log
consultations only; no DOC/PRO Kotlin consulted.

ROSTER NOTE (journal): the batch-5 PILOT table (pre-existing) had
no DAF column (old 10-column format). This push reformats the table
headers + separator to the 11-column shape used for batch 4 (DAF
column between DOC and code-check, per the 7cf10d2 precedent) and
inserts the DAF field into all 31 batch-5 rows (todo), marking 124-
133 translated. Nothing else in PILOT changed (diff = lines
203-235 only; the last row's pre-existing missing trailing pipe
preserved as-is). units.csv counts batch 5 = 31 units (124-154),
one MORE than the B4 batch (30): the B5 tranche split is T1 simple
124-133 (10), T2 core 134-143 (10), T3 complex 144-154 (11).

Adjudication notes: unit 124 compiler-for.c::<misc> and unit 125
compiler-localarray.c::<misc> are pure TU residue (GPL banner +
include set; the TU globals live in DafCompilerState; the
localArrayDaf stub home is ledgered) - residue objects per the T1-T3
precedent. Units 126/127/128 mdh_tree_level_add are the THREE
same-named C++ overloads by arity: one Kotlin overload set with a
shared buildZCallD kernel (row-98 collapse); the sprintf %p row-33
fold carries a DOCUMENTED DIVERGENCE: no native pointer on the JVM -
the GtkTreeStore* is an opaque Int handle printed in the %p hex
shape; zfcnDaf named-owner stub raised in-render (owner
interp-zfcns.cpp::zfcn roster b6; consumer GtkTreeAdd roster b3).
Unit 129 mstring::Length: receiver fold (row 101) + Mglobal
retrieve-then-throw (row 92; GLOBAL_NOT_FOUND=2 pinned from
libmpscpp.h) + strstr-count walk; VENDOR QUIRK kept exactly
(ledgered, no repair): an EMPTY needle value (global exists, value
empty) makes the vendor walk nonterminating - the Kotlin indexOf
walk preserves the vendor shape; the spec guards needle nonempty.
Unit 133 global::Length is the same kernel through the node
receiver: TWO retrieves (ref first per vendor order), both
row-92 throws; the two Length twins share one kernel spec
(spec/length_count.dfy, 6 verified). Unit 130 Avg: the Count-T1 fold
(byte-1 coded splits; the i-2 no-separator-guarantee UB quirk kept,
ledgered), the node ctor caret bypass + a NEW avg(sub) named-owner
seat on MpsGlobalNode (owner: libmpscpp.cpp global-class member Avg,
not a corpus unit), sprintf %g over static char tmp[32] -> fresh
String fmtG (rows 56/112/26). Unit 131 global::Translate: row-92
retrieve, the mstring Concat chain of the coded \$translate envelope
(xce/xd0/xcf) folded onto a String builder written into v1d AT
OFFSET 1 (the row-101 leading-byte carriage; bd[1] read back via
cstringAt), and the fcn(svPtr) relay resolves to the T3 Fcn.kt
dafFcn body - the first in-batch caller of the B4-T3 seat flip.
Unit 132 mps_log10: the ifdef-web twin of the T1 mps_asin render
(same file, same lattice): reference arm is
gcvt_wrapper(log10(strtod(b,NULL)),a) -> fmt8g(log10(strtod)) with
the MPFR and long-double branches dead (row 39/40 family); the C
library nonpositive-domain pathology unguarded, ledgered per the
asin precedent. Residue banners not reproduced anywhere (row 37).

Seats raised for later tranches: zfcnDaf (in MdhTreeLevelAdd.kt;
owner interp-zfcns.cpp::zfcn roster b6), MpsGlobalNode.avg(sub)
(GlobalNodeStub.kt addition; owner: libmpscpp.cpp global-class Avg
member, not a corpus unit - permanent stub unless a later batch
renders the family). Seat REUSE: dafFcn (T3 Fcn.kt flip) first
in-batch caller via global::Translate; MglobalDaf/
MumpsGlobalExceptionD/GlobalExceptionCodeD (T3 stubs) used by both
Length twins and Translate; fmtG/fmt8g/strtod/cstring/cstringAt
(DafCRuntime/T2 seats) used as before. No stub trails were left
dangling this tranche (no prior seats pointed at these units).

Dafny ledger: exemptions (call-shaping/state seats out of
pure-logic spec scope): the two residue objects (124/125 - no
bodies), units 126-128 (GTK service-call shaping over the zfcn
seat), 130 (global-access wrapper over node stub seats), 131
(service-call shaping over the fcn dispatcher seat). Specs
rendered and verified (in-sandbox dafny 4.11.0): spec/mps_log10.dfy
(5 verified, 0 errors, 0 warnings, exit 0; the log10 sign property
is a DECLARED {:axiom} assumption - spec-assumption divergence
note mirroring mps_asin.dfy: the %.8g text format is out of scope)
and spec/length_count.dfy (6 verified, 0 errors, 0 warnings, exit 0;
CountWalk/CountWalkSuffix walk with IndexOfChar as {:axiom} fold
+ OnePieceContract + totality method; the empty-needle
nontermination quirk is the divergence guard: the spec contracts
only the reaching states). Full suite in-sandbox: Escan + mps_asin
+ at_test + piece + smoke_test + mps_log10 + length_count = 38
verified, 0 errors, 0 warnings.

Gates: whole-arm kotlinc 2.1.20 silent PASS at 54 classes (36
committed files incl. the GlobalNodeStub.kt edit + 8 new T1
renders); H-015 scan clean on all touched files (no backticks, no
double-bangs, ASCII-only, no raw dollar outside escapes; one raw
dollar in a GlobalTranslate comment caught and escaped pre-push;
the Kotlin error-type fix zfcnDaf: Nothing -> explicit : Unit
folded in). In-sandbox dafny 4.11.0 verify green; gradle + dafny
re-run at tranche close on m1-build (Mark).

Renders and consult ledger (each consult logged before applying):
- DAF consult: unit compiler-for.c::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-22T03:40:00Z
- DAF consult: unit compiler-localarray.c::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-22T03:40:30Z
- DAF consult: unit libmpscpp.cpp::mdh_tree_level_add, entry BASE-log:98, outcome hit-reuse, timestamp 2026-11-22T03:41:00Z
- DAF consult: unit libmpscpp.cpp::mdh_tree_level_add, entry BASE-log:33, outcome hit-adapt (%p handle divergence), timestamp 2026-11-22T03:41:30Z
- DAF consult: unit libmpscpp.cpp::mdh_tree_level_add, entry BASE-log:22, outcome hit-reuse (svPtr singleton + tmp2 out-seat), timestamp 2026-11-22T03:42:00Z
- DAF consult: unit libmpscpp.cpp::mdh_tree_level_add, entry BASE-log:8, outcome hit-adapt (builder carriage fold), timestamp 2026-11-22T03:42:30Z
- DAF consult: unit libmpscpp.cpp::mdh_tree_level_add, entry BASE-log:37, outcome hit-adapt (zfcnDaf named-owner stub), timestamp 2026-11-22T03:43:00Z
- DAF consult: unit libmpscpp.cpp::mstring::Length, entry BASE-log:101, outcome hit-reuse, timestamp 2026-11-22T03:43:30Z
- DAF consult: unit libmpscpp.cpp::mstring::Length, entry BASE-log:92, outcome hit-reuse, timestamp 2026-11-22T03:44:00Z
- DAF consult: unit libmpscpp.cpp::mstring::Length, entry BASE-log:33, outcome hit-adapt (indexOf walk; empty-needle quirk ledgered), timestamp 2026-11-22T03:44:30Z
- DAF consult: unit libmpscpp.cpp::global::Length, entry BASE-log:92, outcome hit-reuse, timestamp 2026-11-22T03:45:00Z
- DAF consult: unit libmpscpp.cpp::global::Length, entry BASE-log:54, outcome hit-reuse, timestamp 2026-11-22T03:45:30Z
- DAF consult: unit libmpscpp.cpp::global::Length, entry BASE-log:33, outcome hit-adapt (shared kernel spec), timestamp 2026-11-22T03:46:00Z
- DAF consult: unit libmpscpp.cpp::Avg, entry BASE-log:56, outcome hit-reuse, timestamp 2026-11-22T03:46:30Z
- DAF consult: unit libmpscpp.cpp::Avg, entry BASE-log:80, outcome hit-adapt (i-2 UB quirk ledgered), timestamp 2026-11-22T03:47:00Z
- DAF consult: unit libmpscpp.cpp::Avg, entry BASE-log:54, outcome hit-reuse (avg seat raised), timestamp 2026-11-22T03:47:30Z
- DAF consult: unit libmpscpp.cpp::global::Translate, entry BASE-log:92, outcome hit-reuse, timestamp 2026-11-22T03:48:00Z
- DAF consult: unit libmpscpp.cpp::global::Translate, entry BASE-log:101, outcome hit-reuse (v1d offset-1 carriage), timestamp 2026-11-22T03:48:30Z
- DAF consult: unit libmpscpp.cpp::global::Translate, entry BASE-log:97, outcome hit-reuse (dafFcn T3 flip; first in-batch caller), timestamp 2026-11-22T03:49:00Z
- DAF consult: unit interp-arith.cpp::mps_log10, entry BASE-log:39, outcome hit-reuse, timestamp 2026-11-22T03:49:30Z
- DAF consult: unit interp-arith.cpp::mps_log10, entry BASE-log:41, outcome hit-reuse (row 43 verification; log10+strtod+out-param folds rows 40/99/100), timestamp 2026-11-22T03:50:00Z
- DAF consult: unit interp-arith.cpp::mps_log10, entry BASE-log:--, outcome miss (spec rendered: mps_log10.dfy 5 verified; sign property as declared assumption), timestamp 2026-11-22T03:50:30Z
- DAF consult: unit spec/length_count.dfy (units 129+133), entry BASE-log:--, outcome miss (spec rendered: 6 verified; empty-needle quirk guard), timestamp 2026-11-22T03:51:00Z

Files pushed (one commit):
- 8 new renders under wp2/arms/DAF/src/main/kotlin/daf/:
  CompilerForMiscB5.kt (124 residue), CompilerLocalArrayMiscB5.kt
  (125 residue), MdhTreeLevelAdd.kt (126/127/128 overload set +
  zfcnDaf seat), MstringLength.kt (129), Avg.kt (130),
  GlobalTranslate.kt (131), MpsLog10.kt (132), GlobalLength.kt (133)
  (all gate-verified; H-015 scanned)
- 1 edited: GlobalNodeStub.kt (avg(sub) named-owner seat)
- 2 new specs under wp2/arms/DAF/spec/: mps_log10.dfy (5 verified
  0/0, exit 0), length_count.dfy (6 verified 0/0, exit 0; full
  suite 38 verified in-sandbox)
- wp2/logs/DAF_heuristic_log.csv (rows 93-116 appended; ids
  contiguous 1-116, 11-col CSV validated; BASE-log hits carry
  source=BASE-log + reused_from row ids, misses manual-decision)
- wp2/PILOT_TRACKING.md (batch-5 table reformatted to the 11-column
  shape with a DAF column per the 7cf10d2 precedent; rows 124-133
  translated, 134-154 todo; diff = lines 203-235 only)
- wp2/arms/DAF_B5T1_fragment_2026-11-22.md (this fragment)
