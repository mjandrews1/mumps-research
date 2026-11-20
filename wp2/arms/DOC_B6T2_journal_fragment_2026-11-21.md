
### DOC B6 tranche 2 (units 165-175, core stratum) - 2026-11-21

T2 rendered in two sessions with one sandbox reset between them
(the env reset wiped the working copy mid-tranche; the sandbox
was rebuilt per the rebuild recipe, the repo zip freshness was
re-verified - Batch 6 table present, 162 kt files, Stubs and
ARM_WORKING_NOTES md5 checks - and all 11 T2 units re-dump-verified
before the re-render). Roster order per the batch_plan strata:
165-175 core, complexity rank ascending; dump-verified loc +
score for every unit (165 34/11.6, 166 24/12.2, 167 17/12.3,
168 34/13.0, 169 69/13.3, 170 33/14.1, 171 26/14.8, 172 76/18.4,
173 50/24.2, 174 56/25.6, 175 44/26.8).

Renders and consult ledger (each consult logged before applying):

- DOC consult: unit 165, entry H-018, outcome hit-reuse, timestamp 2026-11-21T15:30:00Z
- DOC consult: unit 166, entry H-001, outcome hit-adapt, timestamp 2026-11-21T15:31:00Z
- DOC consult: unit 167, entry H-018, outcome hit-adapt, timestamp 2026-11-21T15:32:00Z
- DOC consult: unit 168, entry H-012, outcome hit-reuse, timestamp 2026-11-21T15:33:00Z
- DOC consult: unit 169, entry H-004, outcome hit-adapt, timestamp 2026-11-21T15:34:00Z
- DOC consult: unit 170, entry H-001, outcome hit-adapt, timestamp 2026-11-21T15:35:00Z
- DOC consult: unit 171, entry H-007, outcome hit-reuse, timestamp 2026-11-21T15:36:00Z
- DOC consult: unit 172, entry H-004, outcome hit-reuse, timestamp 2026-11-21T15:37:00Z
- DOC consult: unit 173, entry H-001, outcome hit-reuse, timestamp 2026-11-21T15:38:00Z
- DOC consult: unit 174, entry H-018, outcome hit-adapt, timestamp 2026-11-21T15:39:00Z
- DOC consult: unit 175, entry H-007, outcome hit-reuse, timestamp 2026-11-21T15:40:00Z

Unit ledger notes:

165 interp-arith.cpp::mps_abs: mpsLog10 template verbatim (H-018
reference config, plain-double arms live, long-double / MPFR
documentary comments); kotlin.math.abs over the MpsExp strtod
seat; Stubs.gcvtWrapper H-014 seat; Int return per the vendor
shape.

166 libmpscpp.cpp::StopInit: the stopword loader. The vendor
StopList static table renders as the mutable top-level stopWords
carrier set (the only mutable global: file-scope StopList at
libmpscpp.cpp:7718, first-reader seats it); FileReader edge per
H-001 with the missing-file banner + exitProcess(1) (Nothing
return type; a first draft added an unreachable throw and was
fixed); readLine with the newline re-append sentinel, the 512
CharArray uppercase force with the vendor space-cut cond loop
kept, and the unconditional trailing-newline NUL strip quirk
(adaptations per the MDH arm quirk ledger). The Stubs.stopInit
seat flips to delegate; Stubs.stopLookup stays stubbed (not a
corpus unit; reads the stopWords carrier when its owner renders).

167 libmpscpp.cpp::Mglobal: the dispatcher - CLOSE returns 1;
RETRIEVE / STORE route through Stubs.keyfix + the btree seat;
all other codes pass through to the REAL unit-4 btree render.
The vendor SQLITE arm IS the live reference config (the native
arm belongs to the batch-10 interp-globalb.cpp::<misc> row, not
restated here). Resolves the raised Stubs.Mglobal CharArray
seat (now a delegate; the String overload keeps folding into
it).

168 interp-sym.cpp::_extract: exact vendor substring semantics
(i negative to 0 else decrement; j negative to j=i else
decrement; j below i yields empty; i==0 and j==0 single byte;
bounds-past-n reject / clamp-after order kept; inclusive copy).
Empty arms via Stubs.strmove(out, charArrayOf(NUL)). Appended to
the unit-32 Extract.kt owner file. TAG CORRECTION ledger: the
Stubs.Mextract tag said batch-10 under the units.csv line
numbering; the batch_plan row is unit 168 this batch - the seat
now delegates to the real render and the mis-tag is journaled.

169 include/mumpsc/interp.h::<misc>: the command-char / operator
const roster (TSTACK 10, BREAK through NEW command chars, QUOTE,
the POPEN 0 through EXPONEN 29 operator code table, NOT_NOT 18
duplicating NOTEQ per the vendor, PREDICATEVAR tilde). TAB is
NOT redeclared (the Getstr2 unit-48 render owns it per H-004,
noted in the file header); zero collisions verified against
the arm's existing top-level consts (grep sweep). Prototype
roster notes: Interpret is the deferred unit-185 giant, parse_
is the real unit-93 render, prnterr is not stubbed per the
house rule, BuildGlobal / BuildLocal are batch-8 rows.

170 libmpscpp.cpp::TimeOut_getstr1: the timeout read. io != 5
banner + Stubs.sigint(100) with the vendor fall-through kept;
four process-level OS surfaces (select, system stty raw/restore,
getchar, fgets-on-stdin) render as private stub carriers that
throw NotImplementedError - process-level OS surface is never
arm-owned (seat table documented per file); dirflg unmasks the
getchar arm; the fgets arm keeps the unconditional last-byte
chop including the stale-buffer NULL case (MDH quirk ledger).

171 libmpscpp.cpp::global::Max: the Cosine-family XNEXT walk
single-datum variant: trailing control-pair trim, the
Stubs.Mglobal(XNEXT) loop, strncmpS prefix stop, get_global +
the tpx==0 GLOBAL_NOT_FOUND raise, strtod, first-value latch
flg; the dead local j of the vendor dropped (H-013); val widened
to value (H-015). TAG CORRECTION ledger: the units 68 / unit-31
era notes called global::Max "not a corpus unit" - the batch-6
batch_plan carries it (loc 26, score 14.8); the Stubs.globalMax
seat now delegates to the real render and the Max.kt unit-68
KDoc tag bullet is corrected this tranche.

172 include/mumpsc/stateVector.h::<misc> (complex stratum): the
MSV residual field roster, EDIT-ONLY appends to the Carriers.kt
Msv carrier with vendor line anchors per field (cfgdata /
cfgkey 256, LockDone, ITRAP, ctrlc, iorg, nflg, gpad, RefParm
10x16, Rargs, Rargsx, _SymPush, hit/count Long, restoredata
Long, ScanMinWordSize / ScanMaxWordSize, UKEY / UDAT 1024,
Table 256, ConOpen, pgsql_msg / Connection / Col_Names STR_MAX,
fdes IntArray(2), exec, reference 1024). grslts / grsltsx are
commented out in the vendor header itself - roster note only,
not translated. All earlier-seated fields verified present, no
duplicates.

173 compiler-parse.c::ExpandOrder: the unit-16 ExpandGet
template verbatim (the coded #G# / #S# payload walk, the
CodedComma tail routing through the real unit-15 extract2, the
sub-128 copy, the "NULL" default, the strcpy(bp,arg2)
convention). Emit byte-faithful: the TWO-LINE GlobalOrder
emission with its trailing space before the first newline and
the vendor p2236 debug fossil comment, the LocalOrder
single-line emission with its trailing space. NOTE: the
ExpandGet unit-16 duplicate-"#S#" fossil does NOT apply here -
the vendor nested tail tests in ExpandOrder are correct
(#G# then #S#; #S# then #G#); noted in KDoc.

174 interp-arith.cpp::modulo: Stubs.floatstring gate; the float
arm renders C fmod as the Kotlin percent operator (identical
sign-of-dividend semantics - an earlier draft used IEEErem and
was WRONG, fixed before push); H-018 gcvt on the float arm; the
intLong arm is IN (interp-arith.cpp defines it unconditionally):
aa / bb via atoi to Long, cc = aa percent bb, strcpyS of the
Long toString.

175 libmpscpp.cpp::Dice: the Cosine-family shape minus the
second walk - a single A-side XNEXT loop with the strncmpS
prefix stop, totD on the A values, the B-side probe at
ctmp + the A subscript suffix via Stubs.globalData (continue on
miss), totQ and the cross-product tot on hits, final
2*tot / (totD + totQ) with the zero guard. Vendor quirk kept and
noted: B.ref is written in place inside the loop and NEVER
restored at loop exit (Dice has no restore pass, unlike
Cosine); the immutable GlobalRef renders the walk state in the
local bref string, same observable seat sequence, the
documented unit-85 divergence precedent. The Stubs.dice seat
delegates; the SymOpsMisc wrapper call site binds unchanged.

Gate: whole-arm kotlinc 2.1.20 gate GREEN 0 errors / 0 warnings
at 171 files (162 + 9 new: MpsAbs, StopInit, Mglobal, InterpH,
TimeOutGetstr1, GlobalMax, Modulo, Dice, ExpandOrder), one
compile round. H-015 hygiene scan clean over all 13 touched
files: no backticks, no raw dollars, no mid-comment close-star,
no double-bang, ASCII only.

Tranche totals: 11 consults, 6 hit-reuse / 5 hit-adapt / 0 miss,
100.0 percent hit, 54.5 percent reuse. The heavier adaptation
share is the core-stratum profile: the StopInit mutable-table
carrier, the Mglobal SQLITE reference-config resolution, the
interp.h ownership re-checks, and the four OS stub carriers of
TimeOut_getstr1. Cookbook stays H-001..H-018: no new capture
candidate this tranche (the modulo fmod correction is a ledger
correction under H-018; the global::Max and _extract tag
corrections ride H-004 / H-014 ownership bookkeeping).

T3 (units 176-182: Shred, interp-stem.cpp::<misc>, write_code,
server.cpp::main, match, read_code, mpstime.cpp::main) is the
next protocol step; the big three stay deferred per the PI
decision.
