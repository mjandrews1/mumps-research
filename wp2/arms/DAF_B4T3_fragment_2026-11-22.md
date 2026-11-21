# DAF B4 T3 journal fragment, 2026-11-22

### DAF B4 tranche 3 (units 114-123, complex stratum) - 2026-11-22

Sandbox rebuilt from scratch this session (fresh env reset): vendor
tree sha256 verified, JDK17 + kotlinc 2.1.20 reinstalled (sha
verified), Dafny 4.11.0 reinstalled
(DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1 in-sandbox; --version matches
m1-build), dump4.py re-created (all 10 T3 unit dumps verified at
exact physical LOC, including the 775-LOC interp-fcn.cpp::fcn dump).
Tranche follows the committed batch-4 rank order (complex stratum,
the last batch-4 tranche). Arm independence kept: rendered from
pristine vendor sources + BASE-heuristic-log consultations only; no
DOC/PRO Kotlin consulted.

Adjudication notes: unit 114 interp-piece residue carries the
_piece kernel: the setpiece==0 read path folds the out-buffer onto
the returned String (row-97 BASE fold) while the setpiece==1 LHS
store keeps the out NAME the vendor stores into; the goto p1/piece1
restarts fold onto the shared tail; strcmp "-1" sentinels (rows
15/14) and atoi counters (row 28) kept. Unit 115 readf_ renders the
FILE* stream as a java.io Reader seat (getc = Int char code, -1 at
EOF); the vendor getc-truncation quirks are kept verbatim
(documented divergence ledger, row 46 - no repair on any arm); zd[0]
NUL write on open failure per row 81. Unit 116 lock_code: the vendor
LOCK-store emit line cites THREE conversions but passes ONE
argument (lhs[i]) - vendor bug kept verbatim and ledgered; the
set_code delegate seat resolves to the unit-121 render of this same
tranche. Unit 117 doty folds the short* out-params onto the
DafCompilerState carrier exactly as the C caller passes the TU
globals by address; the LevCheck seat is stub-mediated (stub returns
false, the 99 path stays reachable only through the seat). Unit 118
pcre.h is the third-party PCRE 4.5 header residue: carrier summary +
PCRE_MAJOR/PCRE_MINOR consts recorded only, per the T1/T2 residue
precedent (rows 94/95/107). Unit 119 GlobalQuery: the (char)1
subscript separators kept verbatim as FCN_MARK1_D carrier bytes (row
80); the SQLITE reference-build strncmp/tmp3 reformat tail is
rendered but gated behind !SQLITE_D with the fold noted (row 46);
Mglobal(XNEXT) retrieve-then-empty rides the MglobalDaf seat (row
92). Unit 120 libmpscpp misc-9: the mstring operator* family
resolves through the mult() machinery with numSetup %.8g modeled by
DafCRuntime.fmt8g - DOCUMENTED DIVERGENCE approximated strtod+fmt8g
per the T2 mstringAddD/mstringDivD precedent; the 8 operator< and 8
operator> overloads collapse onto the numeric atol/atof and
lexicographic String families (rows 98/97); SymGet/SymPut/__symput
render the sym_(11,...) retrieve-then-throw with
GlobalExceptionCode=SYMTAB_ERROR (row 92); the bsv wrappers keep the
(char)1 in-place arg splits exactly as vendor (row 80); the
TermCorrelate member needs a NEW MpsGlobalNode seat (named-owner,
GlobalNodeStub.kt edit). Unit 121 set_code: verbatim emitted text
includes the $piece/$extract sprintf rebuild formats with the
\\" and %%s escapes preserved (INTERPRET macro tails pinned verbatim
via emitInterpretDaf/emitInterpretTailDaf); the malloc MEM_ERROR NULL
branch is UNREACHABLE on the JVM - logged divergence; the LHS ')'
fall-through and set_name 3-arg vs 4-arg format variants kept
verbatim (vendor quirks, ledgered). Unit 122 for_code: the
for_again/for_again2/while01/one_arg/singles mesh is a
forward-branching goto mesh that does not reduce to a labeled-loop
nest - MANUAL-DECISION: rendered as the five-state dispatch machine
with pc values pinned to the vendor labels; the vendor's
UNREACHABLE post-goto SinglesFlg branches kept as the comment-fold.
Unit 123 fcn (775 LOC): the $$FUNCTION indirection branch, the
opcode[256] switch (switch->when MANUAL-DECISION per the zero-switch
corpus), and the 30+ intrinsic services; the opendir family,
rand/getpid/time/getcwd are divergent seats (deterministic/
environment-free on the JVM) and strfmon is NOT modeled (named
stub; mpsJustify seat keeps the call shape) - divergences ledgered
in stub headers; SQLITE reference-build $z guards gated like unit
119; atol/ctype/strchr/string-format folds per rows 28/25/33/26.

Seats raised/extended for later tranches: DafCompilerState.kt
(compiler TU singleton; owner: the compiler TU globals of units
116/117/121/122), DafInterpStubsT3.kt (vendor-consts block + named-
owner stub roster: xindexDaf/MglobalDaf/parseDaf/symRetDaf/
InterpretDaf/scanParseDaf/allocSVDaf/buildLocalDaf/buildGlobalDaf/
globalOrderDaf/globalQueryDaf/globalNameDaf/qsubDaf/qlengthDaf/
evalDaf/mpsJustifyDaf/sqlDaf/extract2Daf/extract1Daf/localArrayDaf/
globalArrayDaf + non-roster helpers/keyfixDaf/makeLockDaf/
levCheckDaf/cvarDaf/atCopyDaf/memcheckDaf/bsv wrappers + divergent
libc seats getpidD/randDSeed/timeD/getcwdD/lpadDaf/isAlphaAscii/
SQL_OPEN_D); DafCRuntime.kt DafMsv extension (xd/pd1/args/
CurrentFile/setname/sqloutput/start/xpx/pd1len/sdlim/symlen/
setpiece/tpx/t0px/t2/ierr/LineNumber/io/NOERR/PD1/TabSize/nstart/
_Sym/hor/ver, pinned from mumpsc/stateVector.h struct MSV; carrier
fold per rows 18/22); GlobalNodeStub.kt termCorrelate member; seat
FLIPS closing stub trails raised in earlier tranches: dafFcn (the
T1/T2 stub in MstringFnumber.kt DELETED - resolved by the unit-123
Fcn.kt body), setCodeDaf (lock_code delegate resolves to unit 121),
pieceDaf/$piece service (Fcn.kt $piece calls resolve to unit 114),
globalQueryDaf (the stub header cites this unit-119 render),
atTestDaf (T2 render used by set_code) and LocalNameDaf (T2 render
used by fcn $name) reused from T2.

Dafny ledger: exemptions (coded-service/call-shaping/state seats out
of pure-logic spec scope): set_code/lock_code/for_code/doty (emitters
over TU state; journal rows), readf_ (stream-bound loader),
GlobalQuery (stub-mediated global traversal), libmpscpp misc-9
(wrapper cluster over stub seats), pcre.h residue (no rendered body),
fcn (stub-mediated dispatcher relay). Spec rendered and verified:
spec/piece.dfy (8 verified, 0 errors, 0 warnings, exit 0: the pure
$piece read-path splitting contract of the unit-114 kernel -
FindKey/Occ/PieceStart/PieceEnd/PieceWindow/PieceReadSpec walkers,
xindex modeled pure, PieceWindow guards st<=en, FindKey requires
0<=from). Full suite in-sandbox: Escan + mps_asin + at_test + piece
+ smoke_test = 27 verified, 0 errors, 0 warnings.

Gates: whole-arm kotlinc 2.1.20 silent PASS at 46 classes/27 files
(24 committed + 12 new T3 renders - the 3 T3 edits in place);
H-015 scan clean on all touched files (no backticks, raw dollars
escaped wholesale, no double-bangs, no non-ASCII, no raw NUL bytes).
In-sandbox dafny 4.11.0 verify of piece.dfy and the full spec suite
green; gradle + dafny re-run at tranche close on m1-build (Mark).

Renders and consult ledger (each consult logged before applying):
- DAF consult: unit interp-piece.cpp::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-22T02:00:00Z
- DAF consult: unit interp-piece.cpp::<misc>, entry BASE-log:97, outcome hit-reuse, timestamp 2026-11-22T02:00:30Z
- DAF consult: unit interp-piece.cpp::<misc>, entry BASE-log:10, outcome hit-adapt, timestamp 2026-11-22T02:01:00Z
- DAF consult: unit interp-piece.cpp::<misc>, entry BASE-log:28, outcome hit-reuse, timestamp 2026-11-22T02:01:30Z
- DAF consult: unit interp-piece.cpp::<misc>, entry BASE-log:81, outcome hit-adapt, timestamp 2026-11-22T02:02:00Z
- DAF consult: unit interp-parse.cpp::readf_, entry BASE-log:2, outcome hit-adapt (FILE* -> Reader), timestamp 2026-11-22T02:02:30Z
- DAF consult: unit interp-parse.cpp::readf_, entry BASE-log:10, outcome hit-adapt, timestamp 2026-11-22T02:03:00Z
- DAF consult: unit interp-parse.cpp::readf_, entry BASE-log:46, outcome hit-adapt (quirk-verbatim divergence ledger), timestamp 2026-11-22T02:03:30Z
- DAF consult: unit compiler-lock.c::lock_code, entry BASE-log:26, outcome hit-reuse, timestamp 2026-11-22T02:04:00Z
- DAF consult: unit compiler-lock.c::lock_code, entry BASE-log:10, outcome hit-adapt, timestamp 2026-11-22T02:04:30Z
- DAF consult: unit compiler-lock.c::lock_code, entry BASE-log:22, outcome hit-reuse, timestamp 2026-11-22T02:05:00Z
- DAF consult: unit compiler-doty.c::doty, entry BASE-log:10, outcome hit-adapt, timestamp 2026-11-22T02:05:30Z
- DAF consult: unit compiler-doty.c::doty, entry BASE-log:69, outcome hit-reuse, timestamp 2026-11-22T02:06:00Z
- DAF consult: unit compiler-doty.c::doty, entry BASE-log:22, outcome hit-reuse, timestamp 2026-11-22T02:06:30Z
- DAF consult: unit include/pcre.h::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-22T02:07:00Z
- DAF consult: unit interp-sym.cpp::GlobalQuery, entry BASE-log:18, outcome hit-reuse, timestamp 2026-11-22T02:07:30Z
- DAF consult: unit interp-sym.cpp::GlobalQuery, entry BASE-log:80, outcome hit-adapt, timestamp 2026-11-22T02:08:00Z
- DAF consult: unit interp-sym.cpp::GlobalQuery, entry BASE-log:92, outcome hit-reuse, timestamp 2026-11-22T02:08:30Z
- DAF consult: unit interp-sym.cpp::GlobalQuery, entry BASE-log:46, outcome hit-adapt (SQLITE dead-tail gate), timestamp 2026-11-22T02:09:00Z
- DAF consult: unit libmpscpp.cpp::<misc-9>, entry BASE-log:41, outcome hit-reuse (fmt8g divergence fold per row 79), timestamp 2026-11-22T02:09:30Z
- DAF consult: unit libmpscpp.cpp::<misc-9>, entry BASE-log:98, outcome hit-reuse, timestamp 2026-11-22T02:10:00Z
- DAF consult: unit libmpscpp.cpp::<misc-9>, entry BASE-log:92, outcome hit-reuse, timestamp 2026-11-22T02:10:30Z
- DAF consult: unit libmpscpp.cpp::<misc-9>, entry BASE-log:80, outcome hit-adapt, timestamp 2026-11-22T02:11:00Z
- DAF consult: unit libmpscpp.cpp::<misc-9>, entry BASE-log:54, outcome hit-reuse, timestamp 2026-11-22T02:11:30Z
- DAF consult: unit compiler-set.c::set_code, entry BASE-log:10, outcome hit-adapt, timestamp 2026-11-22T02:12:00Z
- DAF consult: unit compiler-set.c::set_code, entry BASE-log:26, outcome hit-reuse, timestamp 2026-11-22T02:12:30Z
- DAF consult: unit compiler-set.c::set_code, entry BASE-log:22, outcome hit-reuse (JVM-unreachable malloc branch divergence), timestamp 2026-11-22T02:13:00Z
- DAF consult: unit compiler-set.c::set_code, entry BASE-log:30, outcome hit-adapt, timestamp 2026-11-22T02:13:30Z
- DAF consult: unit compiler-for.c::for_code, entry BASE-log:--, outcome miss (forward goto mesh: manual-decision, five-state dispatch machine), timestamp 2026-11-22T02:14:00Z
- DAF consult: unit compiler-for.c::for_code, entry BASE-log:26, outcome hit-reuse, timestamp 2026-11-22T02:14:30Z
- DAF consult: unit compiler-for.c::for_code, entry BASE-log:22, outcome hit-reuse, timestamp 2026-11-22T02:15:00Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:101, outcome hit-reuse, timestamp 2026-11-22T02:15:30Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:97, outcome hit-reuse (pieceDaf seat flip), timestamp 2026-11-22T02:16:00Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:--, outcome miss (switch->when: manual-decision, zero-switch corpus), timestamp 2026-11-22T02:16:30Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:25, outcome hit-adapt, timestamp 2026-11-22T02:17:00Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:28, outcome hit-reuse, timestamp 2026-11-22T02:17:30Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:--, outcome miss (rand/getpid/time/getcwd divergent seats: manual-decision; strfmon named stub: manual-decision), timestamp 2026-11-22T02:18:00Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:33, outcome hit-adapt, timestamp 2026-11-22T02:18:30Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:92, outcome hit-reuse, timestamp 2026-11-22T02:19:00Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:46, outcome hit-adapt (SQLITE $z gates), timestamp 2026-11-22T02:19:30Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:69, outcome hit-adapt, timestamp 2026-11-22T02:20:00Z

Files pushed (one commit):
- 12 new renders under wp2/arms/DAF/src/main/kotlin/daf/: DafCompilerState.kt,
  DafInterpStubsT3.kt, InterpPieceMisc.kt, Readf.kt, LockCode.kt,
  Doty.kt, PcreHMisc.kt, GlobalQuery.kt, LibmpscppMisc9.kt,
  SetCode.kt, ForCode.kt, Fcn.kt (all gate-verified; H-015 scanned)
- 3 edited: DafCRuntime.kt (DafMsv struct-MSV field extension),
  GlobalNodeStub.kt (termCorrelate named-owner seat),
  MstringFnumber.kt (dafFcn stub deleted - resolved by Fcn.kt)
- 1 new spec under wp2/arms/DAF/spec/: piece.dfy (verified 8/0/0,
  exit 0; full suite 27 verified in-sandbox)
- wp2/logs/DAF_heuristic_log.csv (rows 49-92 appended; BASE-log hits
  carry source=BASE-log + reused_from row ids, misses
  manual-decision)
- wp2/PILOT_TRACKING.md (batch-4 DAF column: rows 114-123 translated;
  lines 188-197 only)
- wp2/arms/DAF_B4T3_fragment_2026-11-22.md (this fragment)