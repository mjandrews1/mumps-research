### BOTH B6 tranche 2 (units 165-175, core stratum) - 2026-11-22

BOTH B6 T2 (units 165-175): the core stratum of batch 6, eleven
units, nine renders plus one existing-file flip and one
existing-file extension, pushed in commit (see Files pushed
below). Consults: 68 events rows appended to
BOTH_knowledge_events.csv in the fresh band
2026-11-22T22:10:00..22:11:07 (34 reuse/34 hit pairs over the
11 renders; zero misses). The h021 struct_carrier promotion takes
its FIRST CONSULTS-AS-RULE at the unit-172 Msv extension (the
h019/h020 promotions take their second consult round at 171/172
and 172 respectively).

Sandbox state (rebuilt fresh this session at the post-splice
master f75f7f3, journal 9508 lines, CSV 267 lines, 41 kt files):
vendor.tgz sha256 621deb... verified; JDK17 corretto; kotlinc
2.1.20; Dafny 4.11.0 x64-ubuntu; baseline re-verified whole-arm
kotlinc 41 files SILENT EXIT=0 and dafny suite 35 verified 0
errors; dump4.py regenerated; all eleven T2 unit dumps
regenerated and LOC-verified (every T2 unit single-instance).

Unit notes:

Unit 165 interp-arith.cpp::mps_abs - the ifdef-web twin of the
asin/log10/log2/sin family (34 LOC): reference body is
gcvt_wrapper(fabs(strtod(in))) riding the gcvtWrapper/strtodC
seats; the BIGFLOAT fabsl arm and the MP mpz lattice dead on the
reference build, comment-folded (h018).

Unit 166 libmpscpp.cpp::StopInit - the stoplist loader: the
string overload at libmpscpp.cpp:7729 (the batch-6 roster row;
the char* 7720 / mstring 7725 twins DELEGATE to it). The
file-scope "StopList stop" at 7718 lands as the stopWords
mutableSetOf seat; the missing-file arm prints "Stoplist file
not found." and exitProcess(1) (the SynInit precedent); the
fgets 512-cap chunk walk over fgetcC keeps the 511-byte cap and
the newline stop; tolower over the ASCII C-locale seat
lowerC; the first-space truncation trace and the final-byte
strip kept verbatim; the line[-1] UB at the empty-token edge
quantized with the coerceAtLeast guard.

Unit 167 interp-globalb.cpp::Mglobal - SEAT FLIP: the B5 h014
TODO stub is replaced by the real SQLITE-arm routing. KEY
FINDING (DAF parity): interp-globalb.cpp:45 defines SQLITE, so
the SQLITE arm IS the live arm and IS the 17-loc roster row.
CLOSE returns 1; RETRIEVE/STORE seat keyfix real (static inline
at include/mumpsc/keyfix.h:7-35, same-include rationale - the
xecute/SymGet stub precedent applied only to cross-TU bodies,
journaled); every op delegates to the raised btreeC named-owner
stub (owner interp-native-btree.cpp::btree, roster 29, batch 1
pending); the native #else arm comment-folded. The STORE=1 const
(defines.h:84) seated next to XNEXT.

Unit 168 interp-sym.cpp::_extract - the EXTRACT char-buffer
semantics (34 LOC, body at 1235): atolC quiet parses; the 1-based
decrement lattice; j<i empty family; the (1,1) single-byte fast
path with the empty-source quantization; TWO vendor UB seats
quantized and journaled: the start-of-0 walk decrements past
zero (the C for-loop reads in[-1]; the render takes the empty
piece), and the through-N clamp (vendor j=n copies the source
NUL mid-piece; the render clamps j=n-1, landing the identical
observable bytes over writeC's NUL).

Unit 169 include/mumpsc/interp.h::<misc> - the full const
lattice: command-char macros, QUOTE/PREDICATEVAR, token codes
POPON..EXPONEN, TSTACK 10, the 97-101 prototypes kept as comment
seats with their roster owners (Interpret 305 big-3 pending,
parse_ 303 b3, prnterr/BuildGlobal/BuildLocal non-units).
Keyword folds per h015: else/for/if/do are Kotlin hard keywords
(ELSE_K/FOR_K/IF_K/DO_K, with DO_C retired), WRITE_C='w' rename,
OR_K=25 rename; FCN and TAB as unicode escapes.

Unit 170 interp-sysfunc.cpp::TimeOut_getstr1 - the timed-read
service: the io!=5 guard with sigint(100) over the roster-254
stub; the select probe seats as the raised selectStdinC platform
stub (POSIX fd 0, NOT a corpus unit); the stty raw/-raw pair as
the sttyRaw/sttyRawOff platform stubs; the getchar single-char
arm and the fgets line arm over the FileDescriptor.in reflection
seat (the MumpsMain precedent - "in" is a Kotlin hard keyword
and the hygiene rule keeps the files free of backtick escapes);
the fgets strip-last-byte quirk with the empty-line guard; the
raw char walk (NOT readLine) keeps CR byte fidelity.

Unit 171 libmpscpp.cpp::global::Max - the XNEXT max walk over
the flipped unit-167 dispatcher (26 LOC, body at 1651): the
trailing control-pair strip unguarded in the vendor, quantized
with the length>=2 guard (the unit-130/136 precedent); the
max=0.0 dead-init flg arm kept (negatives-only families return
their true max); the empty family returns the vendor 0.0
residue; the h019 member seat (layout owner roster 89) passes
the receiver ref explicitly, fold named globalMax.

Unit 172 include/mumpsc/stateVector.h::<misc> - the Msv carrier
extension: the residual field universe in vendor declaration
order (cfgdata..program, stateVector.h:40-100), each field with
its header pin; in_file/out_file over the h020 RandomAccessFile
fold (Array of 10 nullable handles each); nstart over the
existing Nmes carrier; RefParm/Rargs as 10x16 CharArray grids;
the grslts/grsltsx sql rows excluded on the record (vendor
comment lines 97-98); the pd1 width kept at the literal 32001
(sysparms.h PS1 = PS+1, PS 32000) because the compiler-side
memsize.h PS1 const (4097) is a different corpus pin (h017
collision note). AllocSV and the StateVector typedef carry no
observable behavior (svPtr stays the B5 singleton), journaled.
h021 FIRST CONSULTS-AS-RULE.

Unit 173 compiler-parse.c::ExpandOrder - the compiler order
coder, the unit-135 ExpandData sibling (50 LOC, body at 2208):
the #G#/#S# tag walks, arg1/arg2 scans to the closing hash, the
third-argument CodedComma lattice (char 208, defines.h:40,
seated as the const with its pin), the coded third argument
through the extract2C named-owner stub (owner
compiler-parse.c::Extract2, roster 171, batch 1, slice-index
adaptation journaled), the bare argument below-128 cap walk, the
"NULL" literal fallback, the verbatim GlobalOrder/LocalOrder emit
lines over the CompilerOutState.out singleton (the p2236 tail
comment on the G arm only), the below-128 scan guard and the
unbounded accumulator quantizations as at ExpandData.

Unit 174 interp-arith.cpp::modulo - the operator-backing modulo
(56 LOC, body at 695): floatstring seated REAL at the render
(static inline at interp-arith.cpp:597 of the same TU, keyfix
precedent; NOT a corpus unit). h018 three-way resolution:
MULTI_PRECISION/BIGFLOAT dead (the mpz lattice and the
fmodl/strtold arm comment-folded), intLong DEFINED
(defines.h:31) so the long/atoi/%d arm is live; the fmod path
folds strtold through the fmod double argument (parity note);
the C % truncation exact under Kotlin Long %; the zero-divisor
SIGFPE UB renders as the natural ArithmeticException; the
NUMERIC_RANGE throw lives only on the dead MP arm (no const
seat needed).

Unit 175 libmpscpp.cpp::Dice - the Dice coefficient over two
global receivers (44 LOC, body at 1312): the trailing 0x01-pair
strips unguarded in the vendor, quantized (length>=2); the A
XNEXT walk over the flipped dispatcher (tmp scratch rewritten in
place, the unit-136 walk shape); the get_global MACRO
(libmpscpp.h:117) folded as getGlobalC with the tpx store and
the fail-throw idiom; B.Data() over the raised globalDataC
named-owner stub (owner libmpscpp.cpp::GlobalData, roster rows
76/78/79 batch 3/7/8); the B-ref caller mutation lands on a
local var copy (the val-receiver ARM-LOCAL DIVERGENCE,
journaled); the 2*tot/(totD+totQ) tail with the zero
accumulator short-circuit.

Seat adjudications this tranche:
  - BothCRuntime.kt Mglobal stub FLIPPED to the real SQLITE-arm
    routing (unit 167); keyfixC seated real; STORE=1 added.
  - BothStubs.kt T2 block: btreeC, extract2C, globalDataC
    named-owner stubs raised; selectStdinC/sttyRaw/sttyRawOff
    platform stubs raised.
  - The Msv carrier extended in BothCRuntime.kt (unit 172).
  - floatstringC seated real at the Modulo render (unit 174).

Dafny ledger: suite 40 verified 0 errors 35 baseline + the new
arm-own spec/extract.dfy at 5 (expx 4, extract 5, length_count 5,
mps_log10 5, mps_log2 5, parse_count 2, smoke_test 2, strmanip
12); exit 0 on every spec, in-sandbox dafny 4.11.0 with
DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1. The extract spec models
the clamp/decrement lattice (FoldIndex/FoldLen), the
exclusive-end clamped walk core with its length and sourcing
contracts, the (start=1,len=1) fast-path fold, the negative-len
mirror, and the excluded start-of-zero UB family. Two spec
rounds caught in-sandbox pre-push (the slice bound at the
clamp; the empty-family precondition shape).

Gates: kotlinc whole-arm gate over the 50-file both tree SILENT
EXIT=0 twice (the gate copy; then the fresh repo arm copy with
the new files added; 41 base + 9 tranche renders). Mark-side
MacBook gradle build gate after splice. H-015 hygiene scan
clean on all touched files including the spec (no CJK; no em
dashes; no backticks; no tabs; all dollar occurrences escaped;
trailing newlines verified).

Events ledger (kind,target,unit,timestamp; fresh band
2026-11-22T22:10:00..22:11:07, no collision with the spent bands
02:05:00-02:07:37 of 2026-11-21, 19:05:00-19:05:52 and
21:40:00-21:40:55 of 2026-11-22): 68 rows appended, CSV now 335
lines (header + 334; 266 + 68 data rows). Per unit: 165 h018/h009/
h017; 166 h001/h015/h017; 167 h018/h014/h007; 168 h007/h012/
h017; 169 h015/h017; 170 h001/h014/h017; 171 h007/h014/h019;
172 h021/h004/h020; 173 h004/h007/h009/h014; 174 h018/h009/h014/
h017; 175 h007/h014/h019 - each as a reuse+hit pair. Only h021
is a first consults-as-rule (untested -> unit_tested upgrade
discussion deferred to batch close per the h018 precedent).

Open items:
  - The B5 m1-build gradle round (mark-side) and the swipl
    coverage_report verification remain pending from the B5
    close.
  - The 183-185 big-three tranche stays queued for a dedicated
    PI session per the committed order (PRO B6 precedent).

Files pushed this tranche:
  wp2/arms/BOTH/src/main/kotlin/both/MpsAbs.kt 2043
  wp2/arms/BOTH/src/main/kotlin/both/StopInit.kt 4296
  wp2/arms/BOTH/src/main/kotlin/both/TimeOutGetstr1.kt 4746
  wp2/arms/BOTH/src/main/kotlin/both/Extract.kt 3731
  wp2/arms/BOTH/src/main/kotlin/both/InterpHMisc.kt 6456
  wp2/arms/BOTH/src/main/kotlin/both/GlobalMax.kt 3444
  wp2/arms/BOTH/src/main/kotlin/both/ExpandOrder.kt 6122
  wp2/arms/BOTH/src/main/kotlin/both/Modulo.kt 5224
  wp2/arms/BOTH/src/main/kotlin/both/Dice.kt 5494
  wp2/arms/BOTH/src/main/kotlin/both/BothStubs.kt 18350 (edit)
  wp2/arms/BOTH/src/main/kotlin/both/BothCRuntime.kt 17804 (edit)
  wp2/arms/BOTH/spec/extract.dfy 3897
  wp2/logs/BOTH_knowledge_events.csv 21060 (266 + 68 rows)
  wp2/PILOT_TRACKING.md 53562 chars (T2 table BOTH column + note)
  wp2/arms/BOTH_B6T2_fragment_2026-11-22.md (this file)

Tranche closed; BOTH B6 T3 (units 176-182, complex stratum) and
the 183-185 big-three session follow per the committed order.
