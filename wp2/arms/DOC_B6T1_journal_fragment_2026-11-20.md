
### DOC B6 tranche 1 (units 155-164, simple stratum) - 2026-11-20

Batch 6 opened per the committed row order (batch_plan.md B6, 31
units). Shaping mirrors the PRO B6 precedent: T1 simple 155-164,
T2 core 165-175, T3 complex minus the big three 176-182, and the
three ~1600-LOC giants (183 interp-bifs.cpp::zfcn, 184
interp-zfcns.cpp::zfcn, 185 interp-interp.cpp::Interpret)
deferred to a dedicated session PI decision. Sandbox rebuilt
from the rebuild recipe this session (vendor sha256 621deb...ffdbe,
kotlinc 2.1.20, JDK17 corretto; baseline whole-arm gate 0/0 at
155 files before the first render). Roster verification done
before any render: the batch-6 units.csv rows are rank-descending
(Interpret first); the batch_plan strata order is authoritative
per the B5 e1e868a lesson, and the DOC unit ids follow it
(155-164 simple, 165-175 core, 176-185 complex). All 31 batch-6
units dump-verified against the pristine vendor source
(load_units chunking reproduced exactly, misc fold-back
included; the idem twin global::Length resolved :3306 mstring
form this batch, :3330 twin is batch-10).

Renders and consult ledger (each consult logged before applying):

- DOC consult: unit 155, entry H-004, outcome hit-reuse, timestamp 2026-11-20T19:05:00Z
- DOC consult: unit 156, entry H-014, outcome hit-reuse, timestamp 2026-11-20T19:06:00Z
- DOC consult: unit 157, entry H-012, outcome hit-adapt, timestamp 2026-11-20T19:07:00Z
- DOC consult: unit 158, entry H-012, outcome hit-reuse, timestamp 2026-11-20T19:08:00Z
- DOC consult: unit 159, entry H-014, outcome hit-reuse, timestamp 2026-11-20T19:09:00Z
- DOC consult: unit 160, entry H-008, outcome hit-reuse, timestamp 2026-11-20T19:10:00Z
- DOC consult: unit 161, entry H-001, outcome hit-adapt, timestamp 2026-11-20T19:11:00Z
- DOC consult: unit 162, entry H-018, outcome hit-reuse, timestamp 2026-11-20T19:12:00Z
- DOC consult: unit 163, entry H-018, outcome hit-reuse, timestamp 2026-11-20T19:13:00Z
- DOC consult: unit 164, entry H-018, outcome hit-adapt, timestamp 2026-11-20T19:14:00Z

Unit ledger notes:

155 compiler-fcninit.c::<misc>: banner/includes ledger file
(the fcninit body is a batch-7 roster row, owner noted); the
four includes map to their carriers (stdio -> java.io H-001;
mumpsc/memsize.h -> the unit-164 render this tranche;
compiler-macros.h -> per-unit consumers;
compiler-globals-ext.h -> the unit-151 render +
CompilerCarriers seats).

156 libmpscpp.cpp::global::Find: the GLOBAL-key overload, double
Mglobal(RETRIEVE) walk with MumpsGlobalException throws at both
retrievals; Mltoa/Mfind/atoi tail of the unit-33 render verbatim.
TAG CORRECTION ledger: the unit-33 KDoc called itself the
char-star overload, but its batch-2 roster row (score 4.2) is the
mstring-key form; both string-needle forms fold into the
existing String render - noted in the unit-156 KDoc.

157 interp-rpm.cpp::leaning_toothpickize: new render resolving
the raised Stubs.leaning_toothpickize seat (now a delegate, the
parse_pattern_atom_list precedent); call site
ParsePatternElement.kt:95 unchanged. Quirks kept: the 0xd2
DOUBLE_QUOTE sentinel maps to the bcatchr quote; a literal ASCII
quote is silently dropped (vendor shape, noted in KDoc);
ispunct renders as the ASCII C-locale punctuation domain helper
per H-012. Ownership correction ledger: the unit-152 InterpRpmMisc
comment said DOUBLE_QUOTE stays with the batch-9 perl_pm owner,
but the define is file-scope at interp-rpm.cpp:58 and this unit
is the first corpus reader - first-reader seats it
(file-private in LeaningToothpickize.kt, the batch-9 render
reuses the byte value).

158 libmpscpp.cpp::SmithWaterman: the free wrapper over the
unit-42 sw render; STR_MAX carriers with the vendor leading-blank
padding via CstringOps.copyAt; sscanf percent d renders as the
atoi prefix-scan seat (UB quantizes to 0 identically); sprintf
percent d as Int.toString per H-009. Vendor quirk kept: sax and
smx land in the sw ShowAlign/ShowMatrix seats (nonzero-test
forward into the Boolean carriers). The C static tmp[32] folds
per H-008.

159 libmpscpp.cpp::global::Length: the mstring-needle overload,
SINGLE retrieve (contrast with the unit-133 GlobalRef-needle
double walk of the same file - noted in the KDoc), strstr
piece-count walk verbatim; overload disambiguated by needle type
per H-004.

160 libmpscpp.cpp::Sum: exact-shape twin of the unit-130 Avg
render (SOH strip/scan, bypass-caret GlobalRef, String.format
percent g Locale.US). Calls the REAL batch-5 unit-136
global::Sum render (GlobalSum.kt) via the GlobalRef
invoke(vararg String) seat - the resolution of the open question
from the pre-tranche sandbox sweep: the a("") call shape in
Avg.kt binds the LibMisc1.kt vararg-keys overload, so Sum uses
a("").sum() / a(cstringAt(arg1, i+1)).sum().

161 libmpscpp.cpp::ScanAlnum: the istream overload renders as
java.util.Scanner (PRO-arm precedent; istream-by-value fossil
noted) - u >> word as hasNext/next, the max>511 guard and
restart filter reusing this file's private per-file helpers
versbatim; the same-file one-owner rule keeps the fold clean.

162 interp-arith.cpp::mps_log2 and 163 mps_sin: the unit-132
mps_log10 template verbatim (H-018 reference config: plain-double
arms live, long-double and MPFR arms documentary); strtod fold
from the MpsExp seat; kotlin.math log2/sin; Stubs.gcvtWrapper
H-014 seat.

164 include/mumpsc/memsize.h::<misc>: header/banner render as a
file-private const roster with the full arm ledger. CONFIG
CORRECTION LEDGER (the tranche's one consequential edit): NOTABS
is defined unconditionally in memsize.h and compiler-sys1.c:45
includes the header, so the ifdef NOTABS TAB-to-space arm in the
unit-129 readf render IS live - the B4 readf render had ledgered
it compiled out ("NOTABS undefined in the reference build"), a
config misread; amended in Readf.kt this tranche (arm restored
at the vendor expression, both notes rewritten to the corrected
resolution). Other ledger items: the PS 4096 / PS1 4097 FIXME
pair stays documentary (the build seats the sysparms.h 32000 /
32001 values per the batch-3 CompilerCarriers render);
SHOW_CODE / VERSION / DDAT / DKEY have no readers anywhere in
the vendor tree (grep-clean this session) - seated documentary;
FORPARM 32 sits only inside string literals the preprocessor
never expands (the generated programs re-define FORPARM STR_MAX
themselves at compiler-mumps.c:141); GCVT is the __APPLE__ arm
and is NOT defined on the Linux reference build (the
gcvt_wrapper selections live at their per-.cpp owners - the
interp-arith renders' H-018 notes stand); OS-detect cascade
falls through to the Linux arm (UNIX 1, DOS 2, SYSTEM UNIX,
USYSTEM LINUX); STEM / UDAT / UKEY / RELEASE are commented
fossils, kept documentary.

Gate: whole-arm kotlinc 2.1.20 gate GREEN 0 errors / 0 warnings
at 162 files. One compile round: the first SmithWaterman render
used the string-carrier strcpyS at an offset (3 arity errors),
re-fixed to the CstringOps.copyAt seat; second compile clean.
H-015 hygiene scan clean over all touched files: no backticks,
no raw dollars, no mid-comment close-star, no double-bang.

Tranche totals: 10 consults, 7 hit-reuse / 3 hit-adapt / 0 miss,
100.0 percent hit, 70.0 percent reuse. Heavier adaptation share
than the B5 simple tranche (three of the ten: the rpm sentinel
seat, the Scanner carrier, the header config adjudication).
Cookbook stays H-001..H-018: no new capture candidate this
tranche (the memsize.h NOTABS correction and the DOUBLE_QUOTE
first-reader seat are ledger corrections under existing entries
H-018 / H-004, not new rules).

T2 core (units 165-175: mps_abs, StopInit, Mglobal, _extract,
interp.h misc, TimeOut_getstr1, global::Max, stateVector.h
misc, ExpandOrder, modulo, Dice) is the next protocol step.
