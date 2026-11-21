# DAF B4 T2 journal fragment, 2026-11-21

### DAF B4 tranche 2 (units 104-113, core stratum) - 2026-11-21

Sandbox rebuilt from scratch this session (fresh env reset): vendor
tree sha256 verified, JDK17 + kotlinc 2.1.20 reinstalled (sha
verified), Dafny 4.11.0+fcb2042d reinstalled
(DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1 in-sandbox; --version matches
m1-build exactly), dump4.py re-created (all 10 T2 unit dumps verified
at exact physical LOC; the unit-104 row is the score-10.6 overload of
the three ScanAlnum overloads, pointer_marks=1 candidate rendered).
Tranche follows the committed batch-4 rank order (core stratum).
Arm independence kept: rendered from pristine vendor sources +
BASE-heuristic-log consultations only; no DOC/PRO Kotlin consulted.

Adjudication notes: unit 104 ScanAlnum keeps the vendor quirk ledger
(fscanf %8191s seat as the new DafCRuntimeT2.fscanfS over
PushbackReader with the delimiter left unread; the max>511 guard
shares the EOF branch; isdigit(tmp1[0]) evaluates only when j>0 per
the || short-circuit order). Unit 105 LocalName preserves the break
branches that append a literal NUL into tmp3 (result can carry an
embedded NUL exactly as the C buffer) and the tmp0[i]>127 wide-char
compare; the 206/207/208 decode reuses the T1 CODED_*_D constants per
row 101. Unit 106 pb: memcpy(&x,block,4) renders as an explicit
little-endian 4-byte assembly (DOCUMENTED DIVERGENCE: the JVM cannot
reinterpret raw storage; the x86-64 reference build is little-endian);
the vendor's commented-out OFF_T print line kept as a comment per row
46; MBLOCK 2048 / ADSIZE 8 / MMARK 0xff pinned from btree.h/defines.h.
Unit 107 sysfunc.h is the row-37 residue (KDoc-only, prototypes as
named-owner seats; sigint already seated from T1). Unit 108 init
folds the libmpscpp.cpp TU externs (__a/Size_s/Size_t/S_RESULT) into
the SmithWatermanD singleton per rows 6/22; both malloc-NULL +
raise(SIGSEGV) branches render as the documented-divergence comment
block per row 46 (Kotlin allocation fails natively via OOME);
EMPTY_CELL -32768 short. Unit 109 atTest renders the quote-walk mesh
per row 73 (the while(k++) post-test increments become the explicit
k+=2 doubled-quote skip), end-guard reads via the new at() seat per
row 81; VERIFIED SPEC at spec/at_test.dfy (5 verified, 0 errors, 0
warnings: QuoteEnd/AtScan walkers with the terminal-index / 0 / -1
contract and the doubled-quote escape; diagnostics out of scope per
row 69). Unit 110 interp-bifs residue: CLOSE=207/COMMA=208 kept
verbatim under qualified names (NOT the libmpscpp 206/207/208 triple;
vendor quirk ledgered), gcvtWrapperD resolves the reference-build
gcvt branch via fmtG per row 79 (digit-count argument dropped as the
row-79 divergence), sym_ raised as a permanent named-owner stub (no
roster unit for the dispatcher; Mget renders over it per row 56).
Unit 111 _fnumber is the row-101 coded-command relay ("$fn" short
form kept verbatim) over the T1 dafFcn seat, bd[1] read per row 81,
out-buffer to String return per row 30. Unit 112 Centroid extends the
T1 MpsGlobalNode stub with the named-owner seats value/invoke
overloads/order/data/kill per rows 54/37; the mstring numeric
operators +,++,/= are DOCUMENTED DIVERGENCE approximations
(strtod+fmt8g; the full numSetup canonicalization of the non-roster
interp-arith.cpp::add stays out of scope; logged manual-decision);
the final loop's x=B(bb,"1") then B(bb)/=x order kept verbatim. Unit
113 parse_pattern_element carries the DafBstring class carrier per
row 68 + parser/token carriers and named-owner stubs (bstring ops:
owner roster unit 22; accept/accept_it/syntax_error: parser framework
owners; leaning_toothpickize/parse_pattern_atom_list: same-TU non-
roster helpers); switch->when logged manual-decision (zero-switch
corpus, no BASE row); the strchr(pattern_codes,toupper(...)) pointer
difference is the found-index Int over PATTERN_CODES_D/CHAR_CLASSES_D
carried verbatim from interp-rpm.cpp (lines 109-117).

Seats raised/extended for later tranches: DafCRuntimeT2.kt (atoi,
isAlnumAscii/toLowerAscii/toUpperAscii, at, fscanfS - logged arm-
runtime seat file), MpsGlobalNode order/data/kill + invoke overloads
+ value (owner libmpscpp.cpp global class), DafBstring + rpm parser
stubs (owner roster unit 22 / TU helpers), symDaf (permanent named-
owner stub), SmithWatermanD singleton (owner: the Smith-Waterman
family of the libmpscpp TU). No pre-existing DAF seats flipped this
tranche (arm gate green first pass; the dafFcn and sigintDaf seats
stay raised pending units 123/211 in later tranches).

Dafny ledger: exemptions (coded-service/call-shaping/state seats out
of pure-logic spec scope): ScanAlnum (stream-bound filter), LocalName
(buffer-coded decode relay), pb (raw console dumper), sysfunc.h
residue (107), init (state initializer), interp-bifs residue (110),
_fnumber (MSV relay), Centroid (stub-mediated global traversal),
parse_pattern_element (parser-framework relay over stub seats). Spec
rendered and verified: spec/at_test.dfy (5 verified, 0 errors, 0
warnings, exit 0: the quote-walk contract of unit 109 as above).

Gates: whole-arm kotlinc 2.1.20 silent PASS first shot at 24 files
(12 T1 + DafCRuntimeT2.kt + GlobalNodeStub.kt edit + 10 T2 renders);
H-015 scan clean on all touched files (no backticks, raw dollars
escaped wholesale incl. the "$fn" literal, no double-bangs, no
non-ASCII, no raw NUL bytes; the write-tool unicode-escape artifacts
of the T1 session did not recur - files written ASCII-clean and
re-verified). dafny verify of the new spec and the two T1 specs
pending gradle+m1-build re-run at tranche close (Mark).

PILOT repair note: the bc3b366 byte-exact journal restore recovered
the pre-T1 PILOT state, which predates the T1 push's batch-4 table
edit (T1 recorded rows 94-103 DAF translated; the restored file had
no DAF column). This tranche's PILOT push reconstructs that intended
state: the batch-4 table gains a DAF column, rows 94-113 translated
(94-103 = the T1 repair; 104-113 = this tranche), 114-123 todo, no
other line touched (diff verified: lines 166-197 only).

Renders and consult ledger (each consult logged before applying):
- DAF consult: unit libmpscpp.cpp::ScanAlnum, entry BASE-log:25, outcome hit-reuse, timestamp 2026-11-21T22:00:00Z
- DAF consult: unit libmpscpp.cpp::ScanAlnum, entry BASE-log:10, outcome hit-adapt, timestamp 2026-11-21T22:00:30Z
- DAF consult: unit libmpscpp.cpp::ScanAlnum, entry BASE-log:56, outcome hit-adapt, timestamp 2026-11-21T22:01:00Z
- DAF consult: unit libmpscpp.cpp::ScanAlnum, entry BASE-log:--, outcome miss (fscanf seat: manual-decision, DafCRuntimeT2.fscanfS), timestamp 2026-11-21T22:01:30Z
- DAF consult: unit interp-sysfunc.cpp::LocalName, entry BASE-log:101, outcome hit-reuse, timestamp 2026-11-21T22:02:00Z
- DAF consult: unit interp-sysfunc.cpp::LocalName, entry BASE-log:28, outcome hit-adapt, timestamp 2026-11-21T22:02:30Z
- DAF consult: unit interp-sysfunc.cpp::LocalName, entry BASE-log:30, outcome hit-reuse, timestamp 2026-11-21T22:03:00Z
- DAF consult: unit interp-sysfunc.cpp::LocalName, entry BASE-log:81, outcome hit-adapt, timestamp 2026-11-21T22:03:30Z
- DAF consult: unit interp-native-btree.cpp::pb, entry BASE-log:69, outcome hit-adapt, timestamp 2026-11-21T22:04:00Z
- DAF consult: unit interp-native-btree.cpp::pb, entry BASE-log:33, outcome hit-adapt (little-endian divergence), timestamp 2026-11-21T22:04:30Z
- DAF consult: unit interp-native-btree.cpp::pb, entry BASE-log:46, outcome hit-adapt, timestamp 2026-11-21T22:05:00Z
- DAF consult: unit include/mumpsc/sysfunc.h::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-21T22:05:30Z
- DAF consult: unit libmpscpp.cpp::init, entry BASE-log:6, outcome hit-reuse, timestamp 2026-11-21T22:06:00Z
- DAF consult: unit libmpscpp.cpp::init, entry BASE-log:22, outcome hit-reuse, timestamp 2026-11-21T22:06:30Z
- DAF consult: unit libmpscpp.cpp::init, entry BASE-log:46, outcome hit-adapt (SIGSEGV-as-comment divergence), timestamp 2026-11-21T22:07:00Z
- DAF consult: unit compiler-at-test.c::atTest, entry BASE-log:73, outcome hit-reuse, timestamp 2026-11-21T22:07:30Z
- DAF consult: unit compiler-at-test.c::atTest, entry BASE-log:81, outcome hit-reuse, timestamp 2026-11-21T22:08:00Z
- DAF consult: unit compiler-at-test.c::atTest, entry BASE-log:69, outcome hit-adapt, timestamp 2026-11-21T22:08:30Z
- DAF consult: unit interp-bifs.cpp::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-21T22:09:00Z
- DAF consult: unit interp-bifs.cpp::<misc>, entry BASE-log:79, outcome hit-adapt, timestamp 2026-11-21T22:09:30Z
- DAF consult: unit interp-bifs.cpp::<misc>, entry BASE-log:56, outcome hit-adapt, timestamp 2026-11-21T22:10:00Z
- DAF consult: unit interp-fnumber.cpp::_fnumber, entry BASE-log:101, outcome hit-reuse, timestamp 2026-11-21T22:10:30Z
- DAF consult: unit interp-fnumber.cpp::_fnumber, entry BASE-log:81, outcome hit-adapt, timestamp 2026-11-21T22:11:00Z
- DAF consult: unit interp-fnumber.cpp::_fnumber, entry BASE-log:30, outcome hit-reuse, timestamp 2026-11-21T22:11:30Z
- DAF consult: unit libmpscpp.cpp::global::Centroid, entry BASE-log:54, outcome hit-reuse, timestamp 2026-11-21T22:12:00Z
- DAF consult: unit libmpscpp.cpp::global::Centroid, entry BASE-log:98, outcome hit-adapt, timestamp 2026-11-21T22:12:30Z
- DAF consult: unit libmpscpp.cpp::global::Centroid, entry BASE-log:105, outcome hit-adapt, timestamp 2026-11-21T22:13:00Z
- DAF consult: unit libmpscpp.cpp::global::Centroid, entry BASE-log:79, outcome hit-adapt (strtod+fmt8g numeric fold), timestamp 2026-11-21T22:13:30Z
- DAF consult: unit libmpscpp.cpp::global::Centroid, entry BASE-log:10, outcome hit-adapt, timestamp 2026-11-21T22:14:00Z
- DAF consult: unit libmpscpp.cpp::global::Centroid, entry BASE-log:--, outcome miss (mstring operator numeric semantics: manual-decision, divergence ledgered), timestamp 2026-11-21T22:14:30Z
- DAF consult: unit interp-rpm.cpp::parse_pattern_element, entry BASE-log:68, outcome hit-reuse, timestamp 2026-11-21T22:15:00Z
- DAF consult: unit interp-rpm.cpp::parse_pattern_element, entry BASE-log:25, outcome hit-adapt, timestamp 2026-11-21T22:15:30Z
- DAF consult: unit interp-rpm.cpp::parse_pattern_element, entry BASE-log:--, outcome miss (switch->when: manual-decision, zero-switch corpus), timestamp 2026-11-21T22:16:00Z

Files pushed (one commit):
- 10 new renders under wp2/arms/DAF/src/main/kotlin/daf/: ScanAlnum.kt,
  LocalName.kt, Pb.kt, SysfuncHMisc.kt, InitSmithWaterman.kt,
  AtTest.kt, InterpBifsMisc.kt, InterpFnumber.kt, Centroid.kt,
  ParsePatternElement.kt (all gate-verified; H-015 scanned at render)
- 2 new seat files: DafCRuntimeT2.kt, RpmParserStubsD.kt
- 1 edited: GlobalNodeStub.kt (value/invoke overloads/order/data/kill
  named-owner seats)
- 1 new spec under wp2/arms/DAF/spec/: at_test.dfy (verified 0
  errors, 0 warnings, exit 0)
- wp2/logs/DAF_heuristic_log.csv (rows 18-48 appended; BASE-log hits
  carry source=BASE-log + reused_from row ids, misses
  manual-decision)
- wp2/PILOT_TRACKING.md (batch-4 DAF column added; rows 94-113
  translated; lines 166-197 only)
- wp2/arms/DAF_B4T2_fragment_2026-11-21.md (this fragment)
