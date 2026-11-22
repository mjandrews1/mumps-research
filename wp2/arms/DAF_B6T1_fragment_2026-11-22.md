### DAF B6 tranche 1 (units 155-164, simple stratum) - 2026-11-22

DAF B6 T1 (units 155-164): the simple stratum of batch 6, ten
units, ten renders plus one spec, pushed in commit (see Files
pushed below). Consults: 52 logged hits over the BASE-log
(44 hit-reuse / 8 hit-adapt / 0 miss) plus 2 manual-decisions (the
istream seat; the Sum empty-subscript fold) - heuristic-log rows
148-160.

QUEUE NOTE (PI decision of 2026-11-22, logged against the committed
order): the committed order had BOTH B5 T2 as the next tranche
after DAF B5 T3; the PI instructed the arm to finish ALL of DAF
before proceeding to BOTH. DAF B6 T1 runs ahead of BOTH B5 T2 per
that decision; BOTH B5 T2 stays first in the queue once DAF B6
closes (T2 core 165-175, T3 complex 176-182, then the 183-185
big-three session per the PRO B6 PI precedent).

Sandbox state (rebuilt fresh this session at 7111b99): vendor.tgz
sha256 621deb25... verified; JDK17 corretto; kotlinc 2.1.20; Dafny
4.11.0+fcb2042d; gate dir ~/b4/gate/daf from the fresh arm copy;
baseline gate green at 65 files, dafny suite 50 verified; all ten
unit dumps regenerated and LOC-verified (dump4 over
select_study_subset).

Unit notes:

Unit 155 compiler-fcninit.c::<misc> - the banner + include residue
(30 LOC): the dump proves the model bag EMPTY (GPL banner, the May
15 2024 line, four includes); KDoc-only disposition ledger
(CompilerFcninitMisc.kt), rows 37/39/78. The includes pin their
dispositions at the unit-164 lattice and the unit-151 seat; nothing
else is rendered.

Unit 156 libmpscpp.cpp::global::Find - the two-site
retrieve-then-throw wrapper: ref retrieve FIRST then key retrieve
(each GLOBAL_NOT_FOUND + MumpsGlobalExceptionD, row 92), the Mltoa
-> mltoaDaf stub seat, _find raised as the named stub findDaf
(owner interp-sym.cpp:1282), atoi over the r carrier (row 28).
Node receivers on MpsGlobalNode ref Strings (row 54). r[20] and
a[40] ride fresh CharArrays; the r read is cstring-terminated by
the stub-shape assumption (the stub carrier writes through
eventually; read is post-stub at the reference shape).

Unit 157 interp-rpm.cpp::leaning_toothpickize - the same-TU
pattern-munger: SEAT RELEASE + TAG CORRECTION. The
RpmParserStubsD.kt stub is DELETED; the real body renders in
LeaningToothpickize.kt; ParsePatternElement.kt binds unchanged
(row 90). The prior stub header blame "not a roster unit" was a TAG
ERROR - units.csv batch-6 row 157 IS this function (the row-182
note was the BASE-side numbering; SI stand corrected in the seat
header). Body: DOUBLE_QUOTE pinned \\u00D2 (rows 39/78/111); the
DOUBLE_QUOTE branch escapes to a plain quote and continues (the
vendor shape); the ispunct fold is the ASCII range set (row 25
family) with the 91..96 backtick range written as an explicit
Char.code check so the H-015 hygiene scan stays clean; bstring ops
remain unit-22 named stubs (row 37). Vendor quirk kept: the plain-
quote token itself falls through the != guard and is DROPPED.

Unit 158 libmpscpp.cpp::SmithWaterman - the bsv wrapper of the
sequence scorer: leading-blank carriers as String concat (rows
80/101), five sscanf %d -> atoi (row 28), the sw core raised as the
named stub swD (owner libmpscpp.cpp far chunk ~8153), the sprintf
%d over static char tmp[32] folds to a fresh String with the
single-buffer hazard ledgered (rows 33/56).

Unit 159 libmpscpp.cpp::global::Length(mstring) - the
mstring-needle overload: kernel IDENTICAL to the unit-129/133
renders - one retrieve-then-throw site (the needle is a VALUE so
no second site), then the strstr stride walk over String indexOf;
spec reuse of length_count.dfy (no new kernel spec; EXEMPT-by-reuse
ledger). Empty-needle vendor nontermination kept and ledgered (the
unit-129/133 quirk).

Unit 160 libmpscpp.cpp::Sum - the one-arg free wrapper: the
caret-bypass decode of the misc-9 family (rows 80/54); the coded-1
tail trim and boundary probe on at() guarded carriers (rows
111/81), the in-place arg1[i]=0 split kept (row 80); the member
call relays to the unit-143 globalSumD without inlining (rows
90/98). MANUAL-DECISION (rows 54/68): a("") is the empty-subscript
extension which appends nothing to the receiver ref, so the render
passes the receiver node; the nonempty branch passes the
arg1[i+1] slice node. sprintf %g -> fmtG; static tmp[32] fold
(rows 33/56).

Unit 161 libmpscpp.cpp::ScanAlnum(istream) - DUMP DISAMBIGUATION:
the dump4 first-body-by-loc extraction returned the FILE* overload
body (both overloads are 18 LOC, and the FILE* body comes first at
libmpscpp.cpp:10060); the units.csv row for this cell carries
pointer_marks 0, and the direct vendor read at line 10086 fixes
the row's body as the istream overload. The unit-104 FILE* render
keeps its body; this render (ScanAlnumStream.kt,
ScanAlnumStreamD) is the istream owner. MANUAL-DECISION (row 60
family): istream taken BY VALUE folds onto the PushbackReader
stream seat; u >> word rides fscanfS, whose 8191 token cap stands
in for the vendor's unbounded 8192-buffer write (UB on the
reference build; the cap is the seat divergence, ledgered). The
filter kernel is the unit-104 kernel; the max 511-through-EOF
branch quirk kept (closure rows 25/28/10; Dafny EXEMPT per the
unit-104 overload-family reason).

Unit 162 interp-arith.cpp::mps_log2 - the ifdef-web twin of the
unit-132 mps_log10: reference build pins MULTI_PRECISION and
BIGFLOAT false, live body fmt8g(log2(strtod)) (rows 39/40/41/43,
99/100); MPFR and long-double branches are the comment fold; the
nonpositive-input domain pathology kept. Spec: spec/mps_log2.dfy
mirrors mps_log10.dfy (the {:axiom} Log2 carrier; range facts over
positives including the x>=2 implies >=1 band; the %.8g text
format out of scope per the same SPEC-ASSUMPTION DIVERGENCE note).

Unit 163 interp-arith.cpp::mps_sin - the third twin of the
lattice: fmt8g(sin(strtod)); Dafny EXEMPT per the asin-family
reason (no honest range fact over the full input domain for the
declared-assumption pattern; the vendor guards nothing).

Unit 164 include/mumpsc/memsize.h::<misc> - the define lattice
(50 LOC): model bag EMPTY per the dump; KDoc-only ledger
(MemsizeHMisc.kt). Reference build resolves Linux per the ifdef
web (USYSTEM LINUX, UNIX 1, DOS 2, SYSTEM UNIX; rows 39/78); APPLE
GCVT and MSVC MSVisualC arms dead; the Solaris comment-table vs
ifdef-block shape disagreement ledgered (the comment table claims
MSVC undefined; the Solaris ifdef block never defines or undefines
it; the Linux arm is the pin). PS 4096 / PS1 / SHOW_CODE / VERSION
1000 / DDAT / DKEY / NOTABS / FORPARM 32 disposition-lines ledger
per define; the FIXME sysparms.h double definition is config-debt
commentary.

Seat adjudications this tranche (heuristic-log row 158):
  - leaningToothpickizeD stub DELETED from RpmParserStubsD.kt
    (released by the unit-157 render; header TAG CORRECTION).
  - findDaf raised in GlobalFind.kt (owner interp-sym.cpp:1282).
  - swD raised in SmithWaterman.kt (owner libmpscpp.cpp sw far
    chunk ~8153).
  - The ScanAlnum FILE* vs istream overload bodies disambiguated
    against the vendor source (the dump4 first-loc extraction had
    returned the wrong overload for the units.csv row).
  - DOUBLE_QUOTE_D const raised in LeaningToothpickize.kt
    (interp-rpm.cpp \\xd2 pin).

Dafny ledger: suite 55 verified 0 errors 0 warnings (50 baseline +
mps_log2 5: Escan 9, at_test 5, expx 5, length_count 6, mps_asin 3,
mps_log10 5, mps_log2 5, piece 8, qlength_count 7, smoke_test 2);
exit 0 on every spec, in-sandbox dafny 4.11.0 with
DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1. mps_sin EXEMPT
(asin-family reason, in the render KDoc).

Gates: kotlinc whole-arm gate over the 75-file daf tree SILENT
EXIT=0 twice (the gate copy; then the fresh repo arm copy with the
new files added). H-015 hygiene scan clean on all touched files
(no CJK; no em dashes; no backticks; dollars escaped; trailing
newlines verified). One compile round caught in-sandbox pre-push:
parameter name 'in' in findDaf (Kotlin keyword; renamed inBuf).

Consult ledger (nominal ladder 2026-11-22T15:45:00Z..15:51:00Z,
30s steps, no collision with the spent bands 21:05-21:16, 02:00-
02:20, 03:40-03:51, 04:20-04:29, 05:10-05:20, 05:30-05:39, 06:00-
06:30, 07:00-07:17, 15:10-15:41:30):
  155 15:45:00Z rows 37/39/78  banner residue ledger
  156 15:45:30Z rows 92/54/33/56/37/28  retrieve-then-throw pair
  157 15:46:00Z rows 68/25/37/111/90  bstring filter + seat release
  158 15:46:30Z rows 80/101/28/33/56/37  bsv wrapper discipline
  159 15:47:00Z rows 92/54/98/101/33/72/10  kernel reuse
  160 15:47:30Z rows 80/54/111/81/90/98/33/56  wrapper decode
  161 15:48:00Z rows 101/60/25/28/10  istream seat manual-decision
  162 15:48:30Z rows 39/40/41/43/99/100  lattice twin
  163 15:49:00Z rows 39/40/41/43/99/100  lattice twin + EXEMPT
  164 15:49:30Z rows 39/78/37  config lattice ledger
  (seats 15:50:00Z; spec 15:50:30Z; gate 15:51:00Z)

PILOT_TRACKING: the batch-6 tables gain the DAF column after DOC
per the batch-5 convention; rows 155-164 marked translated, 165-185
stay todo; the DAF B6 tranche-1 note paragraph (with the queue
note) sits above the batch-6 tranche-1 table. The heuristic log
grows to 160 rows.

Files pushed (byte counts, ASCII bytes):
  wp2/arms/DAF/src/main/kotlin/daf/CompilerFcninitMisc.kt 1315
  wp2/arms/DAF/src/main/kotlin/daf/GlobalFind.kt 3295
  wp2/arms/DAF/src/main/kotlin/daf/LeaningToothpickize.kt 2791
  wp2/arms/DAF/src/main/kotlin/daf/SmithWaterman.kt 2562
  wp2/arms/DAF/src/main/kotlin/daf/GlobalLengthM.kt 2713
  wp2/arms/DAF/src/main/kotlin/daf/SumW.kt 2801
  wp2/arms/DAF/src/main/kotlin/daf/ScanAlnumStream.kt 2986
  wp2/arms/DAF/src/main/kotlin/daf/MpsLog2.kt 1894
  wp2/arms/DAF/src/main/kotlin/daf/MpsSin.kt 1928
  wp2/arms/DAF/src/main/kotlin/daf/MemsizeHMisc.kt 2147
  wp2/arms/DAF/src/main/kotlin/daf/RpmParserStubsD.kt 3137 (edit)
  wp2/arms/DAF/spec/mps_log2.dfy 2337
  wp2/logs/DAF_heuristic_log.csv 59945 (148 + 13 rows)
  wp2/PILOT_TRACKING.md 40129 chars (DAF column + note)
  wp2/arms/DAF_B6T1_fragment_2026-11-22.md (this file)

Tranche closed; DAF B6 T2 (units 165-175, core stratum) follows
next, then T3 (176-182), then the 183-185 big-three session; BOTH
B5 T2 remains queued FIRST after DAF closes per the committed
order.
