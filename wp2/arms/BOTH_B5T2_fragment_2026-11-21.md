# BOTH B5 T2 journal fragment, 2026-11-21

### BOTH B5 tranche 2 (units 134-143, core stratum) - 2026-11-21

Sandbox rebuilt from scratch this session (post-T1-close reset; the
B5T1 close is already spliced at 01525ab): vendor tree sha256
verified (621deb...ffdbe, 190531550 bytes), JDK17 + kotlinc 2.1.20
restored (kcd sha a118197b... 4891a), Dafny 4.11.0 reinstalled
(4.11.0+fcb2042d6d043a2634f0854338c08feeaaf4ae2, matches m1-build;
DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1 required in this sandbox),
repo zip = master @01525ab. Both-tree gate baseline silent EXIT=0
at 15 classes before the first render; committed spec suite green
at 12 verified 0 errors baseline. dump4.py recreated per the recipe
(WANT dict asserted against units.csv): all 10 T2 unit dumps
verified at exact physical LOC (19/24/21/23/28/48/30/36/18/40).
Tranche follows the committed batch-5 rank order (core stratum).
Arm independence kept: rendered from pristine vendor sources +
rules_v0.pl consultations only; no DOC/DAF/PRO Kotlin consulted.

ROSTER ADJUDICATION (journal): unit 138 libmpscpp.cpp::Multiply is
recorded at LOC 28 in units.csv (rank 154 per the B2 units table and
rank 172 in the batch-5 rows - the file's overload family appears
at several ranks) and the vendor extraction dumps at exactly 28
physical LOC, but the batch_plan.md bullet for ordinal 138 says
"LOC 26". The units.csv + dump ground truth (28) was followed; the
batch_plan bullet discrepancy is flagged here for the PI's B5 close
ruling. All other T2 bullets match units.csv exactly.

Adjudication notes (per unit; consult pairs in the events CSV,
kind rows reuse/hit/miss with the rule id; miss = no covering rule
at render time):
- 134 GlobalQS: the \$qs wrapper. Envelope build folds the
  four-strcat chain onto a String (h007); static char r[STR_MAX] is
  a dead store (h013, dropped); xecute + SymGet ride new
  BothCRuntime TODO stubs (both non-unit vendor families:
  libmpscpp.h:277/16:7053 and libmpscpp.h:179); the vendor's
  unbounded strcat overflow into char[STR_MAX] is quantized by the
  unbounded String build (deterministic, journaled); mstring t is
  the class_member_carrier miss (roster 149, recurrence).
- 135 ExpandData: the compiler's #G#/#S# indirection coder. The
  TU-global FILE* Out (extern at compiler-parse.c:166) becomes the
  CompilerOutState.out Writer property per h004 (singleton,
  source-line registered); the two emitted lines are exact h009
  folds (tab, verbatim "(unsigned char *)" text, newline); arg
  extraction accumulates Strings (h007) with the 32-byte buffer
  overflow UB and the unbounded '#'-scan UB both quantized
  (CharArray-end stop / no truncation seat to preserve, journaled);
  unknown directives fall through silently, kept verbatim.
  DAF-workflow: the Writer emission keeps this unit out of
  pure-logic spec scope (exemption log).
- 136 global::Sum: receiver state passed explicitly (globalClass.h
  layout roster 89; class_member_carrier miss, recurrence). The
  XNEXT walk rides the Mglobal roster-167 stub with the in-place
  key-buffer scratch (h007; XNEXT=8 pinned defines.h:88); the
  get_global macro expands per libmpscpp.h:117 (tpx=Mglobal(
  RETRIEVE,...), the !tpx arm the T1 GLOBAL_NOT_FOUND throw idiom);
  the trailing control-byte strip reads tmp[i-2] unguarded in C
  (UB at i<2) - quantized with an i>=2 guard (T1 unit-130 Avg
  precedent), divergence journaled; the family test is NUL-stop
  strncmpC over i bytes (h012 byte domain).
- 137/138 Transpose/Multiply: the file-level wrappers over the
  global-class member ops. Coded-appearance parses (byte-1 markers,
  h012), dead static tmp[32]/double x stores dropped (h013),
  diagnostics exact (h009, System.out), sigint rides its new
  roster-254 TODO stub (batch 7), the member dispatches ride the
  GlobalRef/GlobalNode carrier seats (h014): GlobalNode.transpose
  (member not a corpus unit; layout owner roster 89) and
  GlobalRef.multiply (owner libmpscpp.cpp::global::Multiply roster
  213 batch 2). VENDOR COPY-PASTE BUGS kept faithfully: Transpose
  tests arg2[i] with the ARG1 index (not j), Multiply repeats the
  same bug twice (arg2[i], arg3[i]); out-of-range quantized to
  no-diagnostic, documented not fixed. Multiply keeps the vendor's
  commented-out subscripted dispatch as a comment. Both units are
  out of pure-logic spec scope (sigint + stubbed dispatch).
- 139 expx: the h018 lattice (same family as T1's mps_log10):
  MULTI_PRECISION and BIGFLOAT both undefined -> live arm
  gcvt_wrapper(pow(strtod(a),strtod(b)),c); excluded arms
  (BIGFLOAT strtold/powl; MULTI_PRECISION mpz_pow_ui/mpfr) carried
  as comments per the h018 action. Rides the T1 seats
  gcvtWrapper/strtodC (h009 reuse). Spec'd in spec/expx.dfy (4
  verified 0/0): the lattice resolution + the iterated-product
  PowInt model (the mpz_pow_ui echo) with its loop-proof bound.
- 140 getstr1: the STR_MAX line reader. The FILE* input is the
  Reader edge (h001, new fgetcC seat: IOException quantizes to EOF
  like C's error return); the control-byte wash (h012) and the
  std::string accumulation (h007) are verbatim; the length arm's
  two stderr lines are exact (h009, 69-char preview); VENDOR BUG
  kept: the drain loop is an ASSIGNMENT IN CONDITION - a NUL byte
  stops the drain early (journaled); the past-throw tail is dead
  code (h013, KDoc only); h010: chr is short int in the vendor
  against fgetc's int - values 0..255/EOF fit, fold keeps Int with
  the narrowing note. INPUT_LENGTH=10 pinned (libmpscpp.h:79);
  the T1 roster-305 exception seats carry the throw. Out of spec
  scope (Reader io).
- 141 interp-strmanip.cpp::<misc>: the residue chunk. Banner and
  include block not carried (T1 124/125 precedent); the void
  sigint(int) prototype and the later-batch section banners noted
  in KDoc only. Three bodies: rindex (offset fold of the
  pointer-into-buffer return, h007; the C quirk c==0 matches at the
  terminator - rindex(s,0)==|s| - kept), strcatx (the '\x01'
  sentinel kept as Char(1), h015; C-array mutation seat), lpad
  (the k<=0 early return leaves strout untouched - the vendor no-
  NUL quirk kept verbatim, journaled). Spec'd in spec/strmanip.dfy
  (12 verified 0/0): RIndexScan last-occurrence contract,
  StrcatxFold prefix preservation + sentinel extension (sentinel
  parameterized - printable-source rule, see below), LpadFold
  right-alignment with the PadAllSpaces lemma carrying the
  induction.
- 142 SysDump: the symbol-table dump. fopen("w") is the Writer
  edge (h001; open failure rides errorMessage then an early return
  quantizing the vendor's fprintf-into-NULL UB tail - no
  double-bang); the Msv carrier gains stateVector.h pins (tpx:64,
  _Sym:65, LineNumber:63, start[SYM_MAX]:71, SYM_MAX=200
  defines.h:218) per h004; dead key[1024]/p2/p3/i dropped (h013);
  Mltoa rides its new roster-292 TODO stub (batch 10); errorMessage
  is rendered in BothCRuntime (non-unit, interp-sysfunc.cpp:251,
  exact printf + sigint) with sigint the roster-254 stub; the stab
  walk quantizes the vendor's unguarded name/data derefs as
  checkNotNull seats (deterministic IllegalStateException at the
  same lines, journaled). MISS struct_carrier (NEW family, T1
  recurrence 1): the struct stab chain (sym.h:43-51; layout owner
  sym.h::<misc>, pending batch-5 complex) has no base rule - the
  Stab carrier filed as the capture candidate. Out of spec scope
  (Writer io + stubs).
- 143 parse_count: this arm's first rpm TU render. Carriers:
  TokenType (9 kinds, interp-rpm.cpp:70-71), Bstring (bstring.h:
  33-35), RpmToken/Parser (:74-88); acceptIt rider is the
  interp-rpm.cpp::<misc> roster-271 stub (batch-5 T3, pending) and
  newBstring/bcatchr ride the interp-bstring.cpp family stubs
  (::<misc> roster 212, ::bstrcat roster 5); bcatchr's C
  struct-pointer mutation folds to the returned value (journaled).
  The four vendor productions are verbatim (incl. the DOT-with-
  comma arm and the bare "*"). Spec'd in spec/parse_count.dfy
  (2 verified 0/0): the pure grammar mapping + coverage
  non-emptiness (the state advance rides stubs, so the spec
  contracts the token-kind disposition instead).

Dafny exemptions this tranche (call-shaping/state seats out of
pure-logic spec scope, logged): 134 (xecute state seat), 135
(Writer emission), 136 (Mglobal state walk), 137/138 (sigint +
stubbed member dispatch), 140 (Reader io), 142 (Writer io + Mltoa
stub). Spec'd: 139 (expx.dfy 4 v), 141 (strmanip.dfy 12 v), 143
(parse_count.dfy 2 v).

Dafny workflow notes (verification edits never change graded
behavior):
- expx.dfy: symbol names are Expx-prefixed because the h018
  lattice family shares names with mps_log10.dfy and the whole spec
  suite verifies in ONE dafny call (single-call discipline kept:
  the combined suite ran 30 verified 0 errors in-sandbox).
- strmanip.dfy: Dafny 4.11 rejects \u escapes with Unicode chars
  enabled and cannot express the raw control byte; the strcatx
  sentinel is PARAMETERIZED (the vendor pins byte 1 at the Kotlin
  layer, Char(1) there). Same h015-borne policy as the Kotlin
  renders: control bytes never appear printable in source.
- strmanip.dfy grew two lemmas (PadLength/PadAllSpaces) carrying
  the induction the LpadFold postconditions need; first BOTH
  spec-suite lemma use.

Gates: whole-arm kotlinc 2.1.20 silent PASS at 32 classes (11
committed T1 files + 10 new T2 renders + 2 edited carrier files
[BothCRuntime +8 seat families, BothStubs +3 carrier seats]);
in-sandbox dafny 4.11.0 full-suite ONE-call verify green at 30
verified, 0 errors (smoke 2 + length_count 5 + mps_log10 5 + expx
4 + parse_count 2 + strmanip 12); gradle re-run at tranche close
on m1-build (Mark). H-015 scans clean on all NEW files (no
backticks, no double-bangs, no raw dollar outside Kotlin string
templates/code, no star-slash in comments, no control bytes,
ASCII-only); the pre-existing backticks in the T1 files
(BothStubs/GlobalTranslate) were scanned again and left
untouched (they shipped green at T1).

TIMESTAMP DISCLOSURE (C-006-style bounded reconstruction, same
flag-for-PI-ruling as T1): the 86 new events rows were recorded at
row-assembly time after the renders, as a 1-second-step sequence
2026-11-21T02:06:12+0000 through 02:07:37+0000, not at the moment
of each individual consultation. Kind, rule id, and unit are exact.

Miss-capture candidates for the B5 batch-close report:
class_member_carrier now at T2+4 (units 134/136/137/138; T1
recurrence was 3 - the recurrence threshold is met again and the
promotion review stays flagged for the close) and struct_carrier at
T1 recurrence 1 (new family, unit 142; first recurrence logged at
this tranche). Neither is promoted mid-batch per the append-only
log discipline.

Events totals this push: 86 rows (34 reuse, 47 hit, 5 miss);
the CSV now 157 rows + header across T1+T2.

Files pushed (one commit):
- 10 new renders under wp2/arms/BOTH/src/main/kotlin/both/:
  GlobalQS.kt (134), ExpandData.kt (135, w/ CompilerOutState),
  GlobalSum.kt (136), Transpose.kt (137), Multiply.kt (138),
  Expx.kt (139), Getstr1.kt (140), StrmanipMisc.kt (141),
  SysDump.kt (142), ParseCount.kt (143) (all gate-verified; scanned)
- 2 edited: BothCRuntime.kt (T2 seats: XNEXT/INPUT_LENGTH/SYM_MAX
  consts, Msv stateVector extension, sigint/errorMessage/Mltoa/
  xecute/SymGet stubs, fgetcC/writeC/strncpyC/strncmpC helpers),
  BothStubs.kt (Stab carrier + GlobalNode.transpose +
  GlobalRef.multiply seats)
- 3 new specs under wp2/arms/BOTH/spec/: expx.dfy (4 v 0/0),
  parse_count.dfy (2 v 0/0), strmanip.dfy (12 v 0/0; full suite one
  call: 30 verified, 0 errors)
- wp2/logs/BOTH_knowledge_events.csv (86 rows appended: T2 consult
  ledger, unquoted PRO schema)
- wp2/PILOT_TRACKING.md (batch-5 rows 134-143 BOTH: todo ->
  translated; diff = those 10 lines only)
- wp2/arms/BOTH_B5T2_fragment_2026-11-21.md (this fragment)
