### DAF B6 big-three session (units 183-185) - 2026-11-22

DAF B6 big-three session (units 183-185, the deferred ~1600-LOC
giants of the complex stratum): three renders, the largest single
renders of the arm, pushed in one commit. Consults: heuristic-log
rows 184-188; the session transplants the DOC arm's 2026-11-21
big-three renders (in the repo at
wp2/arms/DOC/src/main/kotlin/doc/) whole onto the DAF gate seats -
the H-012 same-name family rule pays off at the largest scale of
the program: the DOC Zfcn.kt/ZfcnBifs.kt/Interpret.kt bodies ride
with only seat-map edits.

QUEUE NOTE (unchanged from the tranche-3 record, PI decision of
2026-11-22): the PI instructed the arm to finish ALL of DAF before
proceeding to BOTH; BOTH B5 T2 stays first in the queue once DAF
closes. With the big three landed, DAF batch 6 is COMPLETE
(31/31); the batch-close protocol (coverage checklist, events
export, PILOT prose, gate round) follows this push.

Sandbox state (rebuilt fresh this session at the post-removal
master b58f99f, then wiped by an env reset AFTER the renders were
staged): the gate copy verified green before the reset - vendor
SHA-verified rebuild, dump4 LOC-verified zfcn twins 1622/1662
(interps) and 1581 (Interpret; 2311/2362/2485 raw), the DOC
big-three census taken from the in-repo DOC renders (the twin diff
is exactly five deltas; the vendor Makefile links interp-zfcns.o
ONLY), SQLITE #define verified LIVE at interp-zfcns.cpp:69. The
nine-session files staged to the knowledge sandbox survive the
reset byte-exact; the push rides those.

Unit notes:

Unit 184 interp-zfcns.cpp::zfcn (1662 LOC) - the LIVE
reference-build Z-function dispatcher, rendered FIRST: ZLblZ enum
machine (~75 labels, H-011), ZfcnsState statics (H-004; iter10 and
column drop per H-013), fun zfcnDaf releases the unit-182 seat
(the MpstimeMain raise DELETED, release note left; the two frame
call sites resolve same-shape). SQLITE arms live per H-018 with
the dead #else twins ledger-dropped; the generic sql arm passes
SQLCMD 55; zglobal is the ServiceInterface GBLNME 20 relay.
SEATS: serviceInterfaceDaf RAISED with TAG CORRECTION
(interp-service.cpp::ServiceInterface at 87 IS units.csv row 187
batch 7 - the SymHMisc.kt:38 not-a-roster-unit blame WRONG);
gtkTreeAddD RAISED (row 83; the DAF arm renders
mdh_tree_level_add only); the gtk library glue family raised as
twenty gtk*D seats; mpsFloorD/CeilD/TruncD (b9/b10 rows),
mpsLogD/CosD/TanD/Exp2D (b9 rows), mpsExpD/Exp10D/SqrtD/AcosD/
AtanD (the DOC-rendered kin rows, not in the DAF draw) RAISED
UNRESOLVED; sqlDaf called with the DAF String shape (the dmy
carrier wrapped, strlenCZc checks kept on the buffer); symRetDaf
pinned at 0/0 offsets; wstemD, stopLookupD (Boolean fold, the
vendor != 0 dropped), sigintDaf, stopInitD, SynInitW/SynLookupW/
BMGSearchW/ShredW/ShredQueryW/SmithWatermanW (the ep "" folds
onto CharArray(1)) delegated; the DAF real renders mpsLog2D/
mpsLog10D/mpsAbsD/mpsSinD/mpsAsin return String so the DOC
out-param calls fold onto strcpyS_B (the zabs != 0 ERR arm folds
UNREACHABLE - the intrinsic family success code is always 0 per
the MpsAbs row-43 note); expxD and MglobalDaf FLUSH_D 21 the
same way; ZSEEK/ZTELL raise per the LibmpscppMisc5 ZSeek/Ztell
JVM-divergence precedent (the fposZb store kept); the ctime
helper copies the DOC CopyrightNotice.kt:64 precedent. Dafny
EXEMPT.

Unit 183 interp-bifs.cpp::zfcn (1622 LOC) - the DEAD
build-alternate twin, rendered SECOND so the shared public seats
bind: fun zfcnBifsDaf (the H-017 name split), ZLblB machine,
ZfcnBifsState, TmZb. TWIN DELTAS kept per the vendor dump diff:
the zreplace arm rides with replaceDaf RAISED (builtin.h:40 API
entry, NOT a corpus unit; a bifs-only seat absent from the live
TU); the five SQLITE index arms absent; the generic sql arm
passes PRAGMA 29; zglobal is the g=5 count-empties MglobalDaf
probe; the internal recursion resolves to the LIVE zfcnDaf of
the unit-184 render (the vendor self-call is the same symbol;
DOC seated it to Stubs.zfcn the same way). The unit-184 seat map
applies unchanged (shared public raises; own private helper
copies + ctime twin per the H-017 file-scope rule). Dafny EXEMPT.

Unit 185 interp-interp.cpp::Interpret (1581 LOC) - the
interpreter mainline: fun InterpretDaf releases the
DafInterpStubsT3:119 seat (stub DELETED, release note left;
documented in the InterpHMisc ledger, which names exactly this
event). ILblI machine, InterpretState, NmesI carrier per the DOC
render. The InterpHMisc unit-14 header residue LEDGER PAYS: the
command-char lattice the header explicitly deferred to the
unit-185 render is pinned here as private consts at the interp.h
values (BREAK..ZCMDS and the NEW lexeme; the QUOTE lexeme rides unused), plus
PS 32000, PS1 32001, CHAR_1 u0001, CodedClose 207, CodedComma
208. SEATS: parseDaf, btreeDaf (Mglobal.kt), keyfixDaf,
buildLocalDaf/buildGlobalDaf (the -1 stack-cleaner call
quantizes: str/rslt nulls fold to CharArray(1) carriers, the
NULL svPtr passes svPtr), numcompDaf/addDaf/mltoaDaf/
cleanLocksDaf/makeLockDaf/dafFcn/readfD/timeOutGetstr1D (the DOC
4-arg call folds onto the 5-arg DAF seat: false, System.in,
ledgered), sigintDaf, MglobalDaf with GKILL->KILL_D and
STORE->STORE_D; symSeatI private shape adapter -> symRetDaf
(pins 0/0, the null check rides through); RAISES:
inoutInterpDaf (interp-parse.cpp::inout 4-arg, roster b10; the
WriteCode inoutDaf is the b2 3-arg twin - the distinct-sibling
naming), symFreeDaf (roster b1 row 28), mergeDaf (roster b8 row
226), directReadDaf (roster b1 row 24), getstr2Daf, prnterrDaf
(CARRIED-IN RESIDUOUS-FIELD WARNING: five always-true/false
condition warnings at Interpret.kt 693/825x2/1004/2155 - the
H-018 dead-lattice comparisons of the reference build, kept
verbatim, same acceptance as prior tranches). INT-DOMAIN FOLDS:
pd1len/tpx Long casts stripped (the DafMsv Int fields); the
start[] NULL sentinel folds to 0 in the IntArray domain; the
in/out_file Reader/Writer carriers fold onto the T2 IntArray
handle domain in the OPEN/SHELL/CLOSE arms (0/1 open flag, no
stream carrier kept - the downstream read seats raise anyway);
the NmesI NEW/KILL except-chain folds onto InterpretState.
nmesChain (DafMsv.nstart stays the Int field the GlobalQuery/
Qlength state copies bind); DafMsv EXT: nflg Int field added
(the T2 stateVector precedent; the GlobalQuery/Qlength copies
untouched). Dafny EXEMPT.

Gates: kotlinc whole-arm gate over the 96-file daf tree SILENT
EXIT=0 twice (the gate copy; then the fresh repo arm copy with
the six overlays: 93 post-T3 base + 3 session renders + 3 edited
seat files). Zero errors; five condition warnings accepted
(vendor-lattice faithful, noted above). H-015 hygiene scan clean
on all touched files (ASCII-only; no CJK; no em dash; dollars
escaped in Kotlin strings; trailing newlines verified; the
backtick literal of the DOC scaffold renders as the BT_ZB
u0060 concat constant, no raw backtick). Dafny suite 70
verified 0 errors, unchanged; no new spec (all three units
EXEMPT - platform-glue dispatch over raised seats).

Consult ledger (nominal ladder 2026-11-22T16:30:00Z..16:31:30Z,
30s steps, no collision with the spent bands through 16:24 of
the T3 ladder):
  184 16:30:00Z rows 2/6/18/22/54/56/59/68/90/112  live zfcn
       (rendered first; the roster row cited from the render head)
  183 16:30:30Z rows 2/6/18/22/54/56/59/68/90  bifs twin
  185 16:31:00Z rows 2/6/18/22/26/56/68/90/112  interpret mainline
  (seats 16:31:30Z rows 37/59/68; gate row manual-decision)

PILOT_TRACKING: the batch-6 big-three rows 183-185 marked DAF
translated (Status and the check columns stay todo per the C-001
cycle-10 decision); the DAF B6 big-three session note paragraph
sits after the DOC big-three paragraph. The heuristic log grows
to 188 rows. With this session DAF batch 6 sits 31/31 - the
batch-close protocol (coverage checklist, events export, gate
round) follows.

Files pushed (byte counts, ASCII bytes):
  wp2/arms/DAF/src/main/kotlin/daf/Zfcns.kt 105644
  wp2/arms/DAF/src/main/kotlin/daf/ZfcnBifs.kt 93382
  wp2/arms/DAF/src/main/kotlin/daf/Interpret.kt 116949
  wp2/arms/DAF/src/main/kotlin/daf/MpstimeMain.kt 16408 (edit;
    zfcnDaf stub released)
  wp2/arms/DAF/src/main/kotlin/daf/DafInterpStubsT3.kt 12493
    (edit; InterpretDaf stub released)
  wp2/arms/DAF/src/main/kotlin/daf/DafCRuntime.kt 7069 (edit;
    the DafMsv nflg Int field ext)
  wp2/logs/DAF_heuristic_log.csv 83643 (184 + 5 rows)
  wp2/PILOT_TRACKING.md 45043 chars (rows + note)
  wp2/arms/DAF_B6_big3_fragment_2026-11-22.md (this file)

Session closed; open items for the PI: the splice of this
fragment into the arm journal (then remove the fragment in the
standing house pattern), the gradle/dafny/gate round on the
spliced tree, the DAF B6 batch-close protocol (coverage
checklist, events export, PILOT prose mark), and BOTH B5 T2
remains queued FIRST after DAF closes per the committed order.
