### BOTH B6 tranche 3 (units 176-182, complex stratum) - 2026-11-22

BOTH B6 T3 (units 176-182): the complex stratum of batch 6
minus the big three, seven units, seven renders, all in new
files (the stub and helper seats ride inside the render files
this tranche; no existing-file edits were required). Consults:
118 events rows appended to BOTH_knowledge_events.csv in the
fresh band 2026-11-22T23:30:00..23:31:57 (59 reuse/59 hit
pairs over the 7 renders; zero misses). All consults answer
reuse+hit: h008 static_scratch takes its FIRST consults in the
BOTH rule base at the unit-182 Shred render (the DOC
ScanScratch precedent shape applied), and the h019/h020/h021
promotions all answer reuse+hit through their carrier sitings.

Sandbox state (rebuilt fresh this session at the post-splice
master 845215f, journal 9759 lines, CSV 335 lines, 50 kt
files): vendor tree pristine at the Mumps-Interpreter-Compiler-
Library root; JDK17; kotlinc 2.1.20 (kcd sha re-verified after a
re-download; asset name unchanged); Dafny 4.11.0 (note: the
release asset is now dafny-4.11.0-x64-ubuntu-22.04.zip; the
older dafny-x64-ubuntu-4.11.0.zip name 404s; still needs
DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1); baseline re-verified
whole-arm kotlinc 50 files SILENT EXIT=0 and dafny suite
40 verified 0 errors (expx 4, extract 5, length_count 5,
mps_log10 5, mps_log2 5, parse_count 2, smoke_test 2, strmanip
12); rules_v0.pl copied from wp2/arms/BOTH/ to the gate root;
all seven T3 unit dumps regenerated via the dump4.py rebuild
and LOC-verified (every T3 unit single-instance).

Unit notes:

Unit 176 mpstime.cpp::main - the generated-benchmark main
(176 LOC, rank 286): the M3701 coded bytes (CE, CF comma, 01
mark) seated as Char literals; the setjmp/longjmp for-loop
state machine folded onto structured iteration (the run/read
helper pair); sym_ NOLBL calls seat as sym_(11, ...) per
defines.h:202 (SYMGET_NOLBL, NOT SYMGET); strxcat is the
mpstime.cpp:102 this-TU inline (rendered real); numcomp2
private and unused compiles silently in this setup (tested);
variableNotFound/add/sub family seated; retz Short and dirflg
Char seats per compiler-globals-ext.h:169-170 (.code used, NOT
toInt() - deprecated warns).

Unit 177 interp-stem.cpp::<misc> - the stem-engine residual
(345 LOC, rank 229): the struct ends (stem.h:31 five fields,
next=-1 kept exact) and struct endings (interp-stem.cpp:51
three fields) carriers; the Stem(sv, ep, word) entry over the
Msv carrier.

Unit 178 compiler-write.c::write_code - the W-command code
emitter (63 LOC, rank 231): the ExpandData-shape scanner over
pd1; the outp/pd1str helpers; atTest (roster 109 b4),
atCopy (roster 241 b8, at_flg/tmp), inout (roster 36 b2, the
K&R (text,cmnd,col) shape seats as inout(text, textOff, cmnd,
col, colOff)), linepad (compiler-sys1.c:949 non-unit, the
linepad1 :955 / linepad2 :963 siblings noted), and the no-arg
compiler parse_ (roster 248 b9 - distinct from the interp-side
parse_(sv) roster 303 b3; Kotlin overload legal) all raised
inside the render file.

Unit 179 server.cpp::main - the server transaction main
(65 LOC, rank 235): struct mymsgbuf (server.cpp:52-59, the
SERVERBUF=1024 cap at :47) carrier; logtime() rendered REAL
(same-TU helper, server.cpp:62); the POSIX edges ftok/msgget/
msgrcv/socket/sendto/perror seat as named-owner platform
stubs inside the render file (non-corpus, the selectStdinC
precedent); the type-passed-uninitialized msgrcv UB seats at
0; the argv[i+i] journal index bug kept byte-faithful; the
buf0/buf1 dead journal arms kept as shapes; the int socket
local shadowing dropped per h013.

Unit 180 compiler-read.c::read_code - the R-command reader
(96 LOC, rank 280): the READ='r' code-114 seat
(compiler-macros.h:100); the gclose GlobalClose fold to
Mglobal(CLOSE) (CLOSE=4 already seated at T2); sprintf/lseek
IO over the h020 RandomAccessFile fold; the linepad/parse_
family shared with the unit-178 render.

Unit 181 interp-parse.cpp::match - the MUMPS pattern-match
classifier (86 LOC, rank 261): the pts[3][20]/ct[] short
matrix seats on Array<IntArray>/IntArray carriers; the short*
rslt out-param folds to the Int return (the only-caller 0/1
semantics, journaled); ptr starts -1 (the first ptr++ lands ad
index 0 - the 1-based C walk over the 0-based array, kept
exact); classes C (32/127 control domain), N, P, A, L, U, E
(<=127; the >127 return arm) with exact code-point ranges; the
P punctuation literal kept byte-faithful through the escape
seats (the dollar as unicode-escape, the backtick likewise -
H-015 keeps the files free of both raw bytes; the vendor double
single-quote pair kept); the double-quote class substr pair
over the raised xindex (roster 265 b9) and substr (roster 212
b7) named-owner stubs with the &ad[ptr]/&bd[1] offset call
shapes seated as (input, inOff, out, start, len) ; the
strlen((char*)ad) recompute per check folds to the single adLen
seat (pure, journaled); the ptr+1 != strlen close (the
full-string requirement) and the E-class final ptr++ kept
exact; the default class returns (unknown class fails).

Unit 182 libmpscpp.cpp::Shred - the stateful fragment
iterator (40 LOC, rank 221): the file-scope statics p1/p2/flg/
string/word PERSIST ACROSS CALLS as top-level private vars
(h008 first consults, the DOC ScanScratch precedent); the
size>=128 diagnostic + sigint(100) stub seat with the C
fall-through kept; malloc/free fold to GC; the copy loop keeps
only alphanumerics lowercased (per-file isalnumC/tolowerC
C-locale byte-domain helpers, the ScanAlnum precedent); the
empty-result return WITHOUT setting flg kept (every call
re-copies); the strncpy fold NUL-pads exactly; the p1 > p2
(not >=) reset test yields the NUL-padded last partial
fragment (kept); the dead static int i dropped with a note and
the "Out of memory in Token()" copy-paste diagnostic kept on
the comment path (the GC seat cannot fail, the arm folds
away); the always-returning while(1) shape kept.

Seat adjudications this tranche:
  - All seven renders are new files; the named-owner stubs
    (xindex roster 265 b9, substr roster 212 b7), the platform
    POSIX seats (ftok/msgget/msgrcv/socket/sendto/perror) and
    the TU-local helpers (strxcat, logtime, linepad) seat
    INSIDE their render files - no BothStubs.kt or
    BothCRuntime.kt edits this tranche.

Dafny ledger: suite 40 verified 0 errors, UNCHANGED (all T3
units EXEMPT per the DAF B6 T3 precedent; no new spec added,
exemption journaled here); exit 0 on every spec, in-sandbox
dafny 4.11.0 with DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1
(expx 4, extract 5, length_count 5, mps_log10 5, mps_log2 5,
parse_count 2, smoke_test 2, strmanip 12).

Gates: kotlinc whole-arm gate over the 57-file both tree SILENT
EXIT=0 (the gate copy refreshed from the repo tree; 50 base +
7 tranche renders). Mark-side MacBook gradle build gate after
splice; the m1-build gradle round rides the batch-6 close (the
B5close pattern). H-015 hygiene scan clean on all touched
files (ASCII only, no tabs, no backticks, no raw dollar bytes,
trailing newlines verified).

Events ledger (kind,target,unit,timestamp; fresh band
2026-11-22T23:30:00..23:31:57, no collision with the spent bands
02:05:00-02:07:37 of 2026-11-21, 19:05:00-19:05:52,
21:40:00-21:40:55 and 22:10:00-22:11:07 of 2026-11-22): 118
rows appended, CSV now 453 lines (header + 452; 334 + 118 data
rows). Per unit: 176 h001/h004/h007/h011/h013/h014/h015/h016/
h017/h019/h020; 177 h004/h007/h013/h014/h015/h017/h020/h021;
178 h001/h009/h011/h013/h014/h015/h017/h019/h020; 179 h001/
h004/h009/h011/h013/h014/h015/h017/h019/h020/h021; 180 h001/
h009/h011/h013/h014/h015/h017/h019/h020; 181 h007/h011/h014/
h015/h017/h020; 182 h008/h012/h014/h015/h017 - each as a
reuse+hit pair. Only h008 is a first consults-as-family at
BOTH; the h018-h021 verification-upgrade discussion stays
deferred to batch close per the precedent.

Open items:
  - The 183-185 big-three tranche (Interpret, zfcns zfcn, bifs
    zfcn) stays queued for the dedicated PI session per the
    committed order (render-only session).
  - The batch-6 close round (coverage check, PILOT prose,
    m1-build gradle gating the close) follows the big three.
  - One incidental fix this tranche: the PILOT_TRACKING batch-4
    table header/separator (units 93-122 region) briefly gained
    a stray BOTH column through an over-broad in-session sed
    and was reverted to the shipped shape before the push (the
    rows were never touched; verified column counts 11 across
    that table and 12 across the T3 table rows 176-185 with the
    183-185 BOTH cells seated todo).

Files pushed this tranche:
  wp2/arms/BOTH/src/main/kotlin/both/MpstimeMain.kt 16842
  wp2/arms/BOTH/src/main/kotlin/both/InterpStemMisc.kt 19231
  wp2/arms/BOTH/src/main/kotlin/both/WriteCode.kt 11259
  wp2/arms/BOTH/src/main/kotlin/both/ServerMain.kt 13892
  wp2/arms/BOTH/src/main/kotlin/both/ReadCode.kt 11048
  wp2/arms/BOTH/src/main/kotlin/both/Match.kt 8274
  wp2/arms/BOTH/src/main/kotlin/both/Shred.kt 4683
  wp2/logs/BOTH_knowledge_events.csv 27850 (334 + 118 rows)
  wp2/PILOT_TRACKING.md 55264 chars (T3 table BOTH column +
    tranche paragraph; the 183-185 rows seated todo)
  wp2/arms/BOTH_B6T3_fragment_2026-11-22.md (this file)

Tranche closed; the 183-185 big-three PI session and the
batch-6 close follow per the committed order.
