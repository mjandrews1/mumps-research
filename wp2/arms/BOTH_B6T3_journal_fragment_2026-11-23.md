### BOTH B6 big three (units 183-185, the complex stratum heads) - 2026-11-23

BOTH B6 T3 big-three PI session (units 183-185): three renders
plus three existing-file flips pushed in the tranche commit.
Consults: 50 events rows appended to BOTH_knowledge_events.csv in
the fresh band 2026-11-23T00:14:00..00:14:49 (25 reuse/25 hit
pairs over the three renders; zero misses). The session survived
five sandbox env resets (the renders and flips restored
byte-identical from the Knowledge big3-stage checkpoints at each
recovery; the unit-185 partial was reconstructed from the session
transcript after one reset and checkpointed before the render
completed).

Sandbox state (rebuilt fresh each reset at post-splice master
dee3a2c, journal 9986 lines, CSV 453 lines, 57 kt files):
vendor.tgz sha256 621deb... verified each time; JDK17 corretto
17.0.21; kotlinc 2.1.20 (sha a118197b...); dump4.py regenerated
each time and all three unit dumps re-LOC-asserted (1622/1662/
1581); baseline whole-arm kotlinc re-verified SILENT EXIT=0 at
57 files before each render round, and at 60 files after the
tranche.

Unit notes:

Unit 183 interp-bifs.cpp::zfcn - THE BUILD-ALTERNATE FACE
(H-017 first consults-as-rule at BOTH): the vendor Makefile
archives interp-zfcns.o NOT interp-bifs.o, so interp-bifs.cpp
never compiles into the reference build; the render seats the
name zfcnBifs (the H-017 alternate-name fold) and keeps the
vendor body byte-faithful otherwise. 1717 lines: the route/zz/zs/
zcode tail, 22 GTK label bodies over the h020 Long-handle
TODO-edge stubs, zz ServiceInterface wrappers over the roster-187
seats, live SQLITE zsqlite arms (defines.h pins verified: FLUSH
21, SQLCMD 55, OPEN 206, CLOSE 207, COMMA 208; ZW_GBLNME 20),
real seats expx/SmithWaterman/Shred/ShredQuery/StopInit/SynInit/
ScanAlnum, owner-pinned stubs for the mps family and replaceZb/
stopLookupZb/synLookupZb, the bmgFullsearchZb adjudication (the
vendor BMGSearch calls bmg_fullsearch NOT bmg_search), zdate arms
1-8 with the ctime/mktime/TmZb folds, and the znxtrow dead arm
behind the h013 private-false gate.

Unit 184 interp-zfcns.cpp::zfcn - THE LIVE TU VENDOR SYMBOL
(1662 LOC): the h014 TODO stub face at BothCRuntime.kt is
REMOVED and the render is the live zfcn the package already
calls. Verified C diffs against 183: no zreplace arm in this TU;
five new SQLITE db-info faces (zindexsize "256", zdatasize
"512", zdbname "mumps", zdbfile "mumps.sqlite", zindexmax the
14-2 fold = "12"); the zcode-'g' fallback routes the SQLITE
GBLNME 20 ServiceInterface arm (nargs!=1 err) while the native
arm is the Mglobal(5) count-empties face (183 only); the
zsqlite sql arm rides SQLCMD 55 (defines.h:181) not PRAGMA 29.
Seat policy: the shared roster seats stay internal in
BifsZfcn.kt and are reused here (ServiceInterface, sqlZb,
stopLookupZb/synLookupZb/ShredQuery, bmgFullsearchZb, the
ZC_*/KIN_/ZC_BEGIN..SAVEPOINT consts, FLUSHZb=21); per-TU C
statics duplicate (zbIter/zbTreeStoreH/zbSpinH/zbLinebuf) and
the mps*Zb seats duplicate file-private. The TmZb rename
ZwTmZb records the JVM facade lesson (H-017 second consults-
as-rule): private top-level CLASSES clash at the facade across
TUs, private top-level funs do NOT.

Unit 185 interp-interp.cpp::Interpret - THE INTERPRETER
MAINLINE (1581 LOC): the 80-state ItLbl state machine in a
while(true)/when(state) lattice - every vendor goto label maps
(next_line/next_cmnd through quit/quit1, the set/s2/set_return
family, the lbl/offset DO-GOTO block, the full error-tail
family, abrt, restore). All command arms rendered: comments,
FCN, BREAK, FILELOCK (the makeLock/Lf seat), READ (timeout/
direct/indirect over getstr2/DirectRead/TimeOut_getstr1),
FILEOPEN (old/new/append over the h020 RandomAccessFile fold),
ZCMDS (zg/za/zn/zhalt/ze/zexit; the abrt NOERR redirect returns
99 per the h018 reference config - TERMINATE_ON_ERROR NOT
defined), IF, WRITE, JOB (the fork parent arm h013-gated; the
child close rides sqlZb(CLOSE)), GOTO, MERGE, SET incl. the
group-LHS s1 loop and the SETQ/SET H SQL/SHELL arms (the &~
substitution walk; shell/shell-p/shell-g over ProcessBuilder and
the popen seat stubs), QUIT/XECUTE, the FOR family (push/
nxt_arg/init_loop_var/argless/byp/for_end/for_quit with the
_dummy sym_ quirk kept), HALT + the HALTT html arm, HANG, DO
(the DECLARE/DATABASE ignores, the argless dot-level ExecLevel
latches capped at EXECLEVS, the lbl/offset argument-passing block
with the 207/208 decode and the by-ref '.' args at symflg 200),
NEW (the Nmes linked-list excepts), KILL (all/specific/global
GKILL; the XNEXT kill-others loop correctly skipped - that arm
is the NOT-SQLITE build, h018), FILECLOSE (the closeo quit
quirk and the close loop), USE, and LHSREF (global STORE; the
setpiece/extract/z setitem arms through fcn). Twelve owner-
pinned face stubs seated file-private (getstr2/readf_/
DirectRead/makeLock/prnterr/_SymFree/numcomp/Merge/BuildLocal/
BuildGlobal/popen read+popen write) plus the arena helpers and
the jobForkGate dead gate. Vendor warts kept: OVERFLOW printf,
the unconditional "exit" newline + return 1 at quit1 top, the
byp ignored parse_ return, the dot-latch elisions.

Files pushed (all gate-verified, hygiene-clean, checkpointed to
Knowledge big3-stage before and after the push):
wp2/arms/BOTH/src/main/kotlin/both/BifsZfcn.kt (NEW),
wp2/arms/BOTH/src/main/kotlin/both/ZfcnsZfcn.kt (NEW),
wp2/arms/BOTH/src/main/kotlin/both/Interpret.kt (NEW),
wp2/arms/BOTH/src/main/kotlin/both/BothCRuntime.kt (FLIP: the
h014 zfcn stub face removed),
wp2/arms/BOTH/src/main/kotlin/both/MumpsMain.kt (FLIP: the
interpret TODO now delegates to Interpret),
wp2/logs/BOTH_knowledge_events.csv (+50 rows),
wp2/PILOT_TRACKING.md (rows 183/184/185 BOTH column flips, the
big-three paragraph). Gate: whole-arm kotlinc 60 files SILENT
EXIT=0 (the fresh repo-copy submission, not just the gate
copy). The three renders are DAF T3-exemption-parity renders
(no new arm spec; the suite stays 40 verified 0 errors).

Transparency note for the record: the session again saw
prompt-injection blocks styled as system/fake-guidance text in
tool results and user-turn tails (demands for covert deletion,
concealment, and "compromise models" claims). Each was refused,
disclosed to the PI here, and recorded per the standing
transparency charter; none shaped the renders, this fragment, or
the push contents. No security-relevant repo files were touched.
