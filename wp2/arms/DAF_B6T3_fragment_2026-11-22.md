### DAF B6 tranche 3 (units 176-182, complex stratum) - 2026-11-22

DAF B6 T3 (units 176-182): the complex stratum of batch 6 minus
the big three, seven units, seven renders, pushed in commit (see
Files pushed below). Consults: logged hits over the BASE-log plus
the manual header-ledger decisions - heuristic-log rows 175-183;
one named-owner stub seat RELEASED (shredD), one TAG CORRECTION
kept-in-place (wstemD), and the largest stub-raise set of the arm
so far (the POSIX platform seats of the server loop included).

QUEUE NOTE (repeated from the tranche-1 record, PI decision of
2026-11-22): the PI instructed the arm to finish ALL of DAF before
proceeding to BOTH; BOTH B5 T2 stays first in the queue once DAF
closes (the 183-185 big-three PI session is next, then BOTH).

Sandbox state (rebuilt fresh this session at the post-splice
master 66045d6): vendor.tgz sha256 621deb25... verified; JDK17
corretto; kotlinc 2.1.20; Dafny 4.11.0+fcb2042d; gate dir
~/b4/gate/daf from the fresh post-T2 arm copy; baseline gate green
at 86 files, dafny suite 70 verified; all seven unit dumps
regenerated and LOC-verified (dump4 over select_study_subset;
Shred had FOUR overload candidates 3/3/40/5 - the units.csv loc 40
auto-disambiguated the libmpscpp.cpp string body; every other T3
unit single-candidate). All T3 units are Dafny EXEMPT (no new
spec this tranche; the suite stays 70).

Unit notes:

Unit 176 libmpscpp.cpp::Shred (40 LOC) - the token generator of
the bsv family: SEAT RELEASE + TAG CORRECTION. The LibmpscppMisc9
shredD stub is DELETED; the prior "selftext.cpp Shred" blame was
WRONG (the ShredQuery sibling keeps its selftext residue, THIS
batch-6 roster row is the libmpscpp.cpp Shred overload); the ShredW
wrapper call keeps the shredD(String, Int) signature. The C
function-static lattice (p1/p2/flg/string/word) folds onto the
ShredStateD singleton. VENDOR QUIRKS kept: the size>=128 guard
falls through AFTER sigint(100) (unreachable in practice - the
sigint seat throws); a final PARTIAL fragment is DISCARDED; a
final EXACT-size fragment resets on the NEXT call (which returns
the empty string); the consumed callback NULs the string but not
the cursors (flg alone gates the re-arm). Dafny EXEMPT.

Unit 177 interp-stem.cpp::<misc> (345 LOC) - the stemming-residue
TU: the two conflation-row structs and the thirteen prefix/ending
tables of the $ZALTER/$ZNORMAL service, closing with the Steady
launcher Stem(svPtr, ep, word). The tables were parsed
programmatically from the vendor dump (counts verified: prefix 4;
the d 60, s 42, g 71, n 26, e 14, l 20, c 5, y 34, v 3, t 7, r 17
endings) and copied VERBATIM - vendor data, not recomputable. TAG
CORRECTION at LibmpscppMisc5 wstemD: the "interp-bifs.cpp wstem
family; not a corpus unit" blame was WRONG - the true owner is
interp-stem.cpp::wstem at 418, a units.csv BATCH-10 roster cell
(28 loc); the stub STAYS (b10 owns the body; the tables this
render carries are the data the b10 body consumes). The table
offset/replen columns are copied as-is (the vendor's own column
mismatches ledgered at the render). Dafny EXEMPT.

Unit 178 compiler-write.c::write_code (63 LOC) - the W-command
converter of the emitted-C back end: the out_file guard emit, the
atTest postconditional probe (abort arm ==-1 HERE vs set_code
==99: both atTest return arms kept verbatim per unit), the at
direct-execute arm (INTERPRET emit + the 1920 POST close), then
the argument loop over the !/# * ? text lattice with the inout
relay; the * arm emits atoi/putchar linepads and jumps iosub1
SKIPPING the relay (vendor goto shape kept as the skipInout
fold); the !/# pair sets i=2 for ! and i=1 for #
(reversed-from-words, kept verbatim). inoutDaf RAISED named stub
(owner compiler-sys1.c::inout at 292, roster b2; the
interp-parse.cpp 4-arg sibling rides its own b10 cell). Dafny
EXEMPT (emitter over the stub seats).

Unit 179 server.cpp::main (65 LOC) - the sockets server entry:
the SysV IPC + AF_UNIX datagram family raises as POSIX platform
stubs (ftokDaf, msggetDaf, msgrcvDaf, socketDaf, sendtoDaf,
closeDaf - not corpus), so the message loop is path-gated and the
MglobalDaf transaction inside it is UNREACHABLE in the DAF build
(ledgered; msgrcvDaf raises before any dispatch). The mymsgbuf
carrier is the unit-101 ServerMisc MyMsgBufD hit-reuse and
logtime() is the unit-101 real body hit-reuse. VENDOR QUIRKS
kept and ledgered: the -j parse copies argv[i+i] (should be
i+1 - out-of-bounds read folded onto the clamp); the Journal==
NULL arm kept print-only; the msgrcv type passed UNINITIALIZED
(0-fold, not pinned); buf0/buf1 read UNINITIALIZED at the
journal fprintf (the fill code is commented out at the vendor -
the JVM zero-fill makes the STORE/KILL journal arms unreachable
there; the commented memcpy block stays comment-only verbatim);
GlobalClose macro folds onto MglobalDaf(CLOSE_D,...); the
close(ssock)+GlobalClose tail after the while(1) is unreachable
at the vendor too (comment-only ledger). Dafny EXEMPT.

Unit 180 interp-parse.cpp::match (86 LOC) - the structured-walk
pattern matcher over the 3x20 pts matrix, ct counts, bd pattern
and ad subject. The odd pointer arithmetic is KEPT VERBATIM: ptr
starts -1, every case does ptr++ then reads ad[ptr] (first
consume at index 0); the E/e case has NO explicit continue on
pass (foot fall-through, same effect); the '"' literal case
spins ct[i] times doing NOTHING when pts[2][i]==0; the substr
seat: substrDaf RAISED named stub (owner
interp-strmanip.cpp::substr, roster b7; the bd FROM OFFSET 1
quirk of the &bd[1] call kept via srcOff); strcmp folds onto
String == over the cstring carriers; the xindex punct probe
keeps the C-string tmp shape from=1 (xindexDaf seat hit-reuse
per Fcn.kt); the table string in the render carries the
vendor's own backtick byte (only legitimate backtick of the
tranche). Dafny EXEMPT.

Unit 181 compiler-read.c::read_code (96 LOC) - the R-command
converter, the read side of the writeCodeDaf pair: the in_file
guard emit; the !/# arms i=2/i=1 pair routing to inout with the
DISCARDED IntCellD fold (the vendor passes the "" literal col);
the ? arm passing the pd1 interior cell; the head-text arm; the *
direct-mode flag; the set_code(SET) walk of the read item with
the ladder returning literal 99 on the fall-through; the timeout
colon arm (TimeOut_getstr1 io line emit with the dirflg int; the
commented file[5]=stdin linepad stays comment-only; the
strcpy gtmp back-fill); the DirectRead vs getstr1 read-mode
emits; the set_code(SET_STORE) store walk falling into the
cont_read tail (xpx advance, dirflg reset, comma skip, the
NUL/space/tab end check with the POST close-brace emit). retz
pins CMD_READ_D ('r', compiler-macros.h:100). Dafny EXEMPT.

Unit 182 mpstime.cpp::main (176 LOC) - the compiler-generated
benchmark entry (the executed emitted-C of the timing benchmark:
s t=$ZD1 for i=1:1:10000 s ^a(i)=i for i... s k=^a(i) w
$ZD1-t,!): a RUNTIME harness, every seat a runtime seat. The
setjmp/longjmp for1J/for2J state machine folds onto structured
while loops (the for-init/incr/limit triple and the forF
direction ternary ledgered at each loop head; the _i==100
longjmp exit signal is the structured exit). RAISED named stubs:
zfcnDaf (owner interp-zfcns.cpp::zfcn, the roster b6 big-three
unit 184 that STAYS QUEUED for the big-three session; the
interp-bifs.cpp::zfcn unit 183 likewise remains a raised-user of
that session), numcompDaf/addDaf/subDaf (interp-arith.cpp:
numcomp at 980; add/sub via arith.h:48/51; none roster cells),
strxcatDaf (mpstime.cpp:102 this-TU local inline), the
variableNotFound/VariableNotFound1 family
(interp-sysfunc.cpp:230/:241, non-roster). VENDOR QUIRKS kept:
the argv loop starts at 0 so the "$" entry is immediately
overwritten by "%0" (kept verbatim order); the if(0) alarm
slice stays the ALARM_SLICE_ZERO_D constant-fold of the
MumpsMainD precedent; __label_lookup/_text_function stay
comment-only; in/out_file[5] console default onto the T2
IntArray handle carriers; UDAT="data.dat"/UKEY="key.dat" onto
the T2 CharArray carriers; the M3701 coded bytes ride Char
literals (a-CE key fold, the CODED_COMMA_D CF after the catx
concat, the \u0001 subscript mark of ^a); the catch ladder
folds onto the Kotlin Throwable catch printing the vendor
"Interprerer Error" MISSPELLING verbatim; the epilogue
tmp0/tmp1 NULs, Mltoa(getpid()) seat, LockDone cleanLocks and
Mglobal(CLOSE) kept. SYMGET_NOLBL_D=11 and the libmpscpp.h
exception codes INPUT_ERROR 6 / INTERPRETER_ERROR 8 /
DATA_RANGE_ERROR 4 / NUMERIC_RANGE 5 / UNSPECIFIED 0 pinned at
the render. Dafny EXEMPT.

Seat adjudications this tranche (heuristic-log row 182):
  - LibmpscppMisc9 shredD stub DELETED (released by the unit-176
    render; the selftext.cpp-to-libmpscpp.cpp tag correction
    noted in place; the ShredW wrapper unchanged).
  - LibmpscppMisc5 wstemD TAG CORRECTION in place (owner
    interp-stem.cpp::wstem at 418, roster b10 cell; the stub
    STAYS; b10 owns the body).
  - inoutDaf raised at WriteCode.kt (compiler-sys1.c::inout at
    292, roster b2; re-read by ReadCode).
  - substrDaf raised at Match.kt (interp-strmanip.cpp::substr,
    roster b7).
  - zfcnDaf raised at MpstimeMain.kt (interp-zfcns.cpp::zfcn,
    roster b6 big-three unit 184 - stays queued).
  - numcompDaf/addDaf/subDaf/strxcatDaf and the
    variableNotFound family raised at MpstimeMain.kt (non-roster
    owners, ledgered).
  - ftokDaf/msggetDaf/msgrcvDaf/socketDaf/sendtoDaf/closeDaf
    raised at ServerMain.kt (POSIX platform, not corpus; the
    message loop path-gated).

Dafny ledger: suite 70 verified 0 errors 0 warnings UNCHANGED
(Escan 9, at_test 5, expx 5, extract 15, length_count 6,
mps_asin 3, mps_log10 5, mps_log2 5, piece 8, qlength_count 7,
smoke_test 2); exit 0 on every spec, in-sandbox dafny 4.11.0
with DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1. No new spec this
tranche: all seven T3 units are EXEMPT (platform stub gates,
emitters over stub seats, table carriers, the setjmp-fold
benchmark harness - none carries an honest functional fact the
suite does not already seat).

Gates: kotlinc whole-arm gate over the 93-file daf tree SILENT
EXIT=0 twice (the gate copy; then the fresh repo arm copy with
the new files added: 86 post-T2 base + 7 tranche renders).
H-015 hygiene scan clean on all touched files (no CJK; no em
dashes; dollars escaped in Kotlin strings; trailing newlines
verified; the one backtick is the vendor own literal byte in
the Match.kt punct table string - ledgered as such). Two
compile iterations caught in-sandbox pre-push: the Match.kt
punct table escape set and the ReadCode.kt $Test template
escape.

Consult ledger (nominal ladder 2026-11-22T16:20:00Z..16:24:00Z,
30s steps, no collision with the spent bands 21:05-21:16,
02:00-02:20, 03:40-03:51, 04:20-04:29, 05:10-05:20, 05:30-05:39,
06:00-06:30, 07:00-07:17, 15:10-15:41:30, 15:45-15:51,
16:00-16:06:30):
  176 16:20:00Z rows 6/22/25/59/99/100/101  release + cursor fold
  177 16:20:30Z rows 18/6/22/90/37/26  tables + wstem TAG fix
  178 16:21:00Z rows 2/6/22/26/28/30/90/91  emitter + inout raise
  179 16:21:30Z rows 3/6/18/68/90  platform stub set
  180 16:22:00Z rows 5/54/56/59/90  matcher + substr raise
  181 16:22:30Z rows 2/6/22/26/30/90/91  read converter
  182 16:23:00Z rows 5/6/18/22/33/56/68/90  benchmark + raises
  (seats 16:23:30Z rows 59/68; gate 16:24:00Z rows 93/113)

PILOT_TRACKING: the batch-6 tranche-3 rows 176-182 marked DAF
translated (Status and the check columns stay todo per the
C-001 cycle-10 decision); the DAF B6 tranche-3 note paragraph
sits above the tranche-3 table. The heuristic log grows to 183
rows.

Files pushed (byte counts, ASCII bytes):
  wp2/arms/DAF/src/main/kotlin/daf/Shred.kt 4857
  wp2/arms/DAF/src/main/kotlin/daf/InterpStemMisc.kt 15837
  wp2/arms/DAF/src/main/kotlin/daf/WriteCode.kt 7677
  wp2/arms/DAF/src/main/kotlin/daf/ServerMain.kt 7334
  wp2/arms/DAF/src/main/kotlin/daf/Match.kt 5619
  wp2/arms/DAF/src/main/kotlin/daf/ReadCode.kt 10245
  wp2/arms/DAF/src/main/kotlin/daf/MpstimeMain.kt 16400
  wp2/arms/DAF/src/main/kotlin/daf/LibmpscppMisc9.kt 16417 (edit)
  wp2/arms/DAF/src/main/kotlin/daf/LibmpscppMisc5.kt 29032 (edit)
  wp2/logs/DAF_heuristic_log.csv 77015 (174 + 9 rows)
  wp2/PILOT_TRACKING.md 43239 chars (T3 rows + note)
  wp2/arms/DAF_B6T3_fragment_2026-11-22.md (this file)

Tranche closed; open items for the PI: the 183-185 big-three
session (interp-bifs.cpp::zfcn, interp-zfcns.cpp::zfcn - the
zfcnDaf stub seat of unit 182 releases then -
interp-interp.cpp::Interpret) is next, then BOTH B5 T2 remains
queued FIRST after DAF closes per the committed order. The
T1/T2/T3 fragment files are still on master at this splice:
remove all three in a separate removal commit after the journal
splice (the standing house pattern).
