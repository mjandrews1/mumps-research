### BOTH batch 6 tranche 1 (units 155-164, simple stratum) - 2026-11-22

BOTH B6 T1 (units 155-164): the simple stratum of batch 6, ten
units, ten renders plus one stub flip, pushed in one commit (see
Files pushed below). Consults: 56 events rows this tranche
(26 reuse / 26 hit / 0 miss) appended to
wp2/logs/BOTH_knowledge_events.csv (266 data rows total); the
h019/h020 promotions of the B5 close take their first consults-as-
rule (h019 at units 156/159/160; h020 at unit 161), all reuse+hit,
zero miss - the promoted actions carried the constructs as
written. Timestamp band 2026-11-22T21:40:00-21:40:55+0000 (fresh,
no collisions with the spent bands).

GATE RECORD: whole-arm kotlinc over the gate copy (41 files) and
over the fresh repo-copy submission, both SILENT EXIT=0; dafny
per-spec loop 35 verified 0 errors (30 baseline: expx 4,
length_count 5, mps_log10 5, parse_count 2, smoke_test 2,
strmanip 12; + the new arm-own spec/mps_log2.dfy at 5). H-015
hygiene scan passed on all new/edited files (ASCII only, no
backticks, no tabs, no dollar bytes, EOF newlines).

Sandbox state: rebuilt fresh this session per the rebuild recipe
(vendor sha256 621deb25 verified); dump4.py regenerated and all
ten unit dumps LOC-verified against units.csv batch-6 rows;
overload variants split out (Find x3, Length x4, ScanAlnum x3).

Unit notes:

Unit 155 compiler-fcninit.c::<misc> (30 LOC) - the residue chunk of
the compiler-fcninit TU: banner (lines 1-27), May 15 2024 date
note, include block (stdio.h; mumpsc/memsize.h;
mumpsc/compiler-macros.h, Sysout/Sysout_skip home verified at
compiler-macros.h:50-52; mumpsc/compiler-globals-ext.h). KDoc-only
residue ledger (the unit-144 sym.h precedent); no declarations ride
the residue.

Unit 156 libmpscpp.cpp::global::Find (22 LOC) - the roster row is
the global-key overload at libmpscpp.cpp:7587 (unit 7 owns the
char-key twin at 7566, unit 4 the mstring twin at 7617; derived
from the batch-row loc values, not file order). h019 first
consults-as-rule: member folds to globalFind(ref, key: GlobalRef,
st) with the receiver state explicit. _find raised as a NEW
named-owner stub in BothStubs.kt (interp-sym.cpp::_find, roster 6,
batch 1 pending); Mltoa/stats ride the existing roster-292 stub;
the atoi tail rides the internal-promoted atolC. Quirk: the FIRST
retrieve validates the haystack then discards it into the _find
pass; kept verbatim (two mglobalRetrieve calls).

Unit 157 interp-rpm.cpp::leaning_toothpickize (17 LOC) - STUB
FLIP: the RpmMisc.kt TODO (owner roster 157 batch 6) is replaced by
the real body (the h014 delegate flip precedent; vendor
interp-rpm.cpp:479-497). The \xd2 DOUBLE_QUOTE marker byte unescapes
to a quote; ISPUNCT bytes gain the toothpick backslash; the
locale is quantized to the ASCII punct set (ledgered); a RAW
double-quote byte in the input is silently dropped (the != '"'
arm) - vendor verbatim.

Unit 158 libmpscpp.cpp::SmithWaterman (17 LOC) - the padding-parse
wrapper (svPtr/ep unused, dropped). The five score-string
sscanf("%d") seats fold onto the internal-promoted atolC quiet
parse (failure quantized to 0, journal). SEAT-NAME MISMATCH kept
and journaled: the sw() engine (raised as a named-owner stub,
libmpscpp.cpp::sw roster 287 batch 2) declares ShowAlign /
ShowMatrix / gap / mismatch / match, and the caller passes
sax/smx/gapx/mmx/max by position - forwarded verbatim. The
static char tmp[32] return folds fresh (h008).

Unit 159 libmpscpp.cpp::global::Length (17 LOC) - OVERLOAD
DISAMBIGUATION against the units.csv duplicate-id ledger: five
vendor overloads, three 17-LOC bodies; the batch-row metrics
(both score-8.1 twins, one score-7.7) plus the file-stable score
sort map the string-needle overload (libmpscpp.cpp:3330-3352) to
THIS batch-6 rank-65 row, the mstring twin (3306) to the batch-10
rank-64 row, the char twin (3354) to the batch-10 rank-56 row.
Rendered as globalLength(ref, needle: String) overloading the
unit-133 render; the empty-needle non-termination quirk and the
empty-own 0 short-circuit kept verbatim (129/133 twins); h019
consults-as-rule.

Unit 160 libmpscpp.cpp::Sum (17 LOC) - the FILE-LEVEL Sum at
libmpscpp.cpp:10289, distinct from the member unit-136 global::Sum
(line 1620, rendered globalSum at B5 T2). The arg1 caret-bytes
reference: trailing 0x01-pair strip quantized with the length>=2
guard (Avg precedent), name past the caret up to the first 0x01,
subscript bytes after it; the MakeGlobal composition folds to the
plain concat (the vendor strcatx key, libmpscpp.cpp:6963 ledger;
no quote decoration reaches the ref key - verified against the
vendor body); the member Sum delegate rides unit-136 globalSum; the
%g seat rides fmtG. Quirk: empty arg1 takes &arg1[1] past the
buffer in C (UB) - quantized to the empty name, journaled.

Unit 161 libmpscpp.cpp::ScanAlnum (18 LOC) - OVERLOAD
DISAMBIGUATION by batch-row metrics: the istream body
(libmpscpp.cpp:10086, zero pointer marks, rank 73 batch 6) is the
roster row; the FILE* twin (10060, one pointer mark) is unit 104
batch 4; the MSV* lexical twin (10112) is unit 127 batch 3. The
istream carrier folds onto the java.io.Reader handle seat (h020
first consults-as-rule). Quirks kept: max>511 discards a
successfully extracted token; the goto-repeat folds to the loop;
the 8191-byte word cap rides the tmp1 max test not the extraction
(seat divergence ledgered); isalnum/tolower quantized to the C
locale ASCII domain.

Unit 162 interp-arith.cpp::mps_log2 (22 LOC) - the mps_log10 twin
lattice (h018): MULTI_PRECISION and BIGFLOAT both undefined, the
live arm is gcvt_wrapper(log2(strtod(b,NULL)),a); excluded arms
(BIGFLOAT log2l strtold arm, the mpfr %.8RNg block including the
@FLT_PRECISION@ substitution) documented as comments. Seat call
kotlin.math.log2 (JVM, not a log(x)/log(2) restructuring), silent
under kotlinc. NEW arm-own Dafny spec mps_log2.dfy (the
mps_log10.dfy twin: ResolveArm, LiveArmIsPlainDouble, Pow10,
DigitPrefixBounds), verified 5/0.

Unit 163 interp-arith.cpp::mps_sin (22 LOC) - the same twin lattice
at interp-arith.cpp:253 (h018 live arm
gcvt_wrapper(sin(strtod(b,NULL)),a); excluded sinl/mpfr_sin arms
commented). Dafny EXEMPT journaled: the arm-resolution lattice and
the digit-prefix bounds are specified once for the family; the sin
core adds no decidable surface. No new spec.

Unit 164 include/mumpsc/memsize.h::<misc> (50 LOC) - the config
header ledger (h018): the __APPLE__ block is LIVE per the frozen
reference build (GCVT 1 seated, memsize.h:63), the Linux platform
arm seats USYSTEM "LINUX" / UNIX 1 / DOS 2 / SYSTEM UNIX
(:84-87); excluded arms (Solaris/Watcom/GnuDos/MSVisualC) stay
comments; dead defines (STEM, UDAT/UKEY, RELEASE) stay dead;
NOTABS seats as the valueless-define boolean NOTABS_ACTIVE (:43);
FORPARM 32, SHOW_CODE 1, VERSION 1000, DDAT/DKEY, PS 4096 seated;
PS1 VERIFIED-EQUAL with the unit-151 seat (CompilerGlobalsExtMisc
:51) and REUSED, not re-declared. The vendor FIXME (PS/PS1 also in
sysparms.h, :31) ledgered. DOC's unit-129 readf blast-radius edit
does NOT apply (no readf render in the BOTH tree, verified).

Seat ledger (this tranche):

- RELEASED: leaning_toothpickize (RpmMisc.kt H014 TODO -> real
  body, owner roster 157 batch 6 landed).
- RAISED: _find (BothStubs, owner interp-sym.cpp::_find roster 6
  batch 1); sw (BothStubs, owner libmpscpp.cpp::sw roster 287
  batch 2).
- PROMOTED: atolC private -> internal (LibmpscppMisc5.kt, body
  unchanged), the atoi/sscanf quiet-parse seat for units 156/158.
- UNCHANGED STUBS still pending named owners: Mltoa (roster 292),
  Mglobal (roster 167), parse_ (303), parse_pattern_element
  (196), make_cstring (212), perl_pm (214), wstem (164), pm,
  allocSV (285), gotamatch (180), GTK family, globalClass.h
  seats (259), Perl cleanLocks/__symput/interpret (MumpsMain).

Files pushed (one commit, master):

- wp2/arms/BOTH/src/main/kotlin/both/CompilerFcninitMisc.kt (new,
  unit 155)
- wp2/arms/BOTH/src/main/kotlin/both/GlobalFind.kt (new, 156)
- wp2/arms/BOTH/src/main/kotlin/both/SmithWaterman.kt (new, 158)
- wp2/arms/BOTH/src/main/kotlin/both/GlobalLengthString.kt (new,
  159)
- wp2/arms/BOTH/src/main/kotlin/both/Sum.kt (new, 160)
- wp2/arms/BOTH/src/main/kotlin/both/ScanAlnum.kt (new, 161)
- wp2/arms/BOTH/src/main/kotlin/both/MpsLog2.kt (new, 162)
- wp2/arms/BOTH/src/main/kotlin/both/MpsSin.kt (new, 163)
- wp2/arms/BOTH/src/main/kotlin/both/MemsizeHMisc.kt (new, 164)
- wp2/arms/BOTH/spec/mps_log2.dfy (new spec, 5 verified)
- wp2/arms/BOTH/src/main/kotlin/both/RpmMisc.kt (unit-157 flip +
  isPunctC seat)
- wp2/arms/BOTH/src/main/kotlin/both/BothStubs.kt (sw/_find
  stubs + B6 T1 consult block)
- wp2/arms/BOTH/src/main/kotlin/both/LibmpscppMisc5.kt (atolC
  promotion)
- wp2/logs/BOTH_knowledge_events.csv (56 rows appended)
- wp2/PILOT_TRACKING.md (BOTH column added to the B6 T1 table,
  rows 155-164 marked translated, BOTH B6 T1 narrative)
- wp2/arms/BOTH_B6T1_fragment_2026-11-22.md (this fragment)

Open items:

- The B5close fragment (wp2/arms/BOTH_B5close_fragment_
  2026-11-22.md, push 43bde50) awaits the PI splice; splice BEFORE
  this fragment (journal base 9187 + 137 close + this fragment).
- m1-build gradle round for B6 T1: run at the PI's MacBook after
  the splice (the B5-note m1-build round is still pending too).
- B6 T2 (units 165-175, core) queued next per the committed row
  order; the 183-185 big-three PI session stands per the PRO/DAF
  precedent.
- artifacts: none this tranche beyond the repo files above.
