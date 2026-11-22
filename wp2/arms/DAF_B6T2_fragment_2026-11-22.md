### DAF B6 tranche 2 (units 165-175, core stratum) - 2026-11-22

DAF B6 T2 (units 165-175): the core stratum of batch 6, eleven
units, eleven renders plus one spec, pushed in commit (see Files
pushed below). Consults: logged hits over the BASE-log plus the
manual header-ledger decisions - heuristic-log rows 161-174; the
three queued named-owner stub seats RELEASED this tranche
(MglobalDaf, stopInitD, diceD).

QUEUE NOTE (repeated from the tranche-1 record, PI decision of
2026-11-22): the PI instructed the arm to finish ALL of DAF before
proceeding to BOTH; BOTH B5 T2 stays first in the queue once DAF B6
closes (T3 complex 176-182 next, then the 183-185 big-three PI
session).

Sandbox state (rebuilt fresh this session at the post-splice
master 84b53df): vendor.tgz sha256 621deb25... verified; JDK17
corretto; kotlinc 2.1.20; Dafny 4.11.0+fcb2042d; gate dir
~/b4/gate/daf from the fresh arm copy; baseline gate green at 75
files, dafny suite 55 verified; all eleven unit dumps regenerated
and LOC-verified (dump4 over select_study_subset; every T2 unit
had a single overload candidate - the T1 ScanAlnum lesson repeated
cleanly). The three T1 rider wording fixes queued at tranche close
are applied and pushed with this tranche (no semantic change).

Unit notes:

Unit 165 interp-arith.cpp::mps_abs - the fourth ifdef-web twin of
the asin/log10/log2/sin family (34 LOC): reference body is
gcvt_wrapper(fabs(strtod(in))) with the always-0 return folded;
the BIGFLOAT fabsl branch and the whole #else arm (the mpf strchr
split with gmp_sscanf; the mpz branch with the failed-parse rebind
to literal 0) dead on the reference build, comment-folded. Dafny
EXEMPT (no honest range fact for abs over the full parse domain
beyond tautologies).

Unit 166 libmpscpp.cpp::StopInit - the stoplist loader: SEAT
RELEASE + TAG CORRECTION. The LibmpscppMisc9 stopInitD stub is
DELETED; the prior "libmpscpp.cpp/selftext.cpp StopInit; not a
roster unit" blame was WRONG - units.csv batch-6 row 166 IS the
string overload at libmpscpp.cpp:7729 (the StopList stop set
carrier at 7718). Real body renders in StopInit.kt: clear the
DafStop singleton set, the file loader over the java.io reader
seat with the DOCUMENTED DIVERGENCE that the missing-file
exit(EXIT_FAILURE) throws FileNotFoundException (the SynInit
precedent), the lowercase-prefix token scan per line, the empty
line inserting the empty token (the vendor does too). The sibling
stopLookupD stub stays with the corrected owner note.

Unit 167 interp-globalb.cpp::Mglobal - the 4-way dispatcher across
CLOSE/RETRIEVE/STORE/fallthrough: SEAT RELEASE. The DafInterpStubsT3
MglobalDaf stub is DELETED (its roster-b6 raise is THIS unit);
every existing MglobalDaf caller (Fcn, GlobalFind, GlobalLength,
GlobalQuery, GlobalTranslate, Sum, LibmpscppMisc5, InterpPieceMisc)
binds to the real body unchanged. KEY FINDING:
interp-globalb.cpp:45 defines SQLITE, so the SQLITE arm IS the
live arm and IS the 17-loc roster row; the native #else arm and the
dead SQLITE-side decls (unpad/sigint/CloseFlg/flush) are
comment-folded. btreeDaf raised as a named-owner stub (owner
interp-native-btree.cpp::btree at line 293); keyfix relays to the
existing keyfixDaf seat. Vendor quirk kept: CLOSE returns 1 without
touching btree.

Unit 168 interp-sym.cpp::_extract - the EXTRACT kernel: the from-1
adjust (negative clamps to 0), the to-1 adjust with the asymmetry
(a negative len becomes the ADJUSTED from), the inverted-range
empty, the i==0 and j==0 first-char special, the i>n empty, the
j>n clamp, the INCLUSIVE copy loop. First binding at DAF (the DOC
Stubs.Mextract precedent named this owner but no DAF seat existed)
- no stub to release. VENDOR QUIRKS ledgered: a start of "0" makes
the adjust produce index -1 and the C reads in[-1] (the render
rides the at() NUL guard - the OOB-read divergence); the origin
special reads in[0] even when in is empty (same guard); the clamp
lets the loop copy the terminator NUL before the final out[k]=0
(the observable is unchanged).

Unit 169 include/mumpsc/interp.h::<misc> - the command-macro +
operator-code + prototype residue (69 LOC): KDoc-only disposition
ledger per the unit-144 precedent. The command macros ledger to
the unit-185 render; the POPEN..PREDICATEVAR operator codes are
ledgered NOT PINNED (pinning untaken consts would invite drift
against defines.h at the bench verification); the four prototypes
dispositioned to their roster cells and named seats.

Unit 170 interp-sysfunc.cpp::TimeOut_getstr1 - the timed stdin
read: select and stty raise the named platform stubs
selectStdinDaf/sttyDaf; the sigintDaf(100) guard reused; the guard
fires only when io!=5; FIFO order kept (timeout BEFORE data);
fgets(area,STR_MAX,stdin) cuts at STR_MAX-1 bytes while the reader
seat reads the whole line (ledgered fold; identical observable
under the bound); dirflg folds Boolean (the 0/1 domain).

Unit 171 libmpscpp.cpp::global::Max - the sixth member of the
node-walk family (Count/Min/Avg/Sum/Dice this tranche): the XNEXT
walk with the control-char pair trim, the flg-first max tracking,
the tpx retrieve-then-throw via the existing getGlobalDaf stub;
the MglobalDaf call now rides the RELEASED unit-167 dispatcher.

Unit 172 include/mumpsc/stateVector.h::<misc> - the svPtr field
universe (76 LOC): KDoc ledger PLUS the DafMsv CARRIER EXTENSION
in DafCRuntime.kt - the residual struct MSV roster rendered in
vendor order (cfgdata/cfgkey, LockDone/kflg/symflg/ITRAP/ctrlc/
iorg/ERROR/gpad, in_file/out_file, RefParm/Rargs/Rargsx/_SymPush/
order, hit/count, restoredata, ScanMin/MaxWordSize, UKEY/UDAT,
Table, ConOpen, pgsql_msg/Connection/Col_Names, fdes, exec,
reference, ErrChar, program; the existing tpx and friends
unchanged). The banner "2001 - 2215" year pair ledgered.

Unit 173 compiler-parse.c::ExpandOrder - the compiler order
expansion, mirroring the unit-135 ExpandData render of the same
family: the CODED_COMMA carrier, the extract2Daf seat, the verbatim
DafCompilerState.emit GlobalOrder/LocalOrder lines ("NULL" literal
arg3, the p2236 tail comment on the G arm only), the below-128
byte-cap loops, null return = the untouched bp.

Unit 174 interp-arith.cpp::modulo - the operator-backing modulo:
floatstring raised as the named stub floatstringDaf (a static
inline at interp-arith.cpp:597 of the same TU; nine intrinsic
call sites; not a corpus unit). The fmod path parses a with
strtod but b with strtold - the MIX folds onto the double parse on
the reference build (quirk ledgered). The intLong define at line
59 is UNUSUALLY a plain define (not an ifdef): the long branch is
live and renders (the %d-of-long low-word print divergence
ledgered; the render prints the Int domain - identical at this
seat), the long-long branch dead, and the zero-divisor C UB renders
as the natural ArithmeticException (the UB-to-exception divergence;
the only vendor guard sits on the DEAD mpz path). The whole mpz
#else lattice is comment-folded.

Unit 175 libmpscpp.cpp::Dice - the Dice coefficient: SEAT RELEASE.
The LibmpscppMisc9 diceD stub is DELETED; the render keeps the
two-arg wrapper signature (the DiceW call unchanged; the svPtr
singleton rides DafInterpState.svPtr per the wrapper rule). The
coded-1 PAIR inner trims at both refs are a DIFFERENT trim lattice
from the Sum-family control-char trim (the FCN_MARK1 byte pair);
the vendor has NO j>=2 guard at the j-2 read (the render guards;
the OOB-read divergence ledgered). The A-side XNEXT walk rides the
RELEASED unit-167 dispatcher; the B-side ref rebuilt by appending
beyond the A ref constructs a fresh MpsGlobalNode per fetch (the
val-ref fold); B.Data() rides the path-gated MpsGlobalNode.data()
stub; two tpx retrieve-then-throw guards; the final 2*tot/val
divides only after the zero check on the accumulator sum.

Seat adjudications this tranche (heuristic-log row 172):
  - DafInterpStubsT3 MglobalDaf stub DELETED (released by the
    unit-167 render; the header roster row updated to RELEASED).
  - LibmpscppMisc9 stopInitD stub DELETED with the TAG CORRECTION
    (batch-6 row 166 IS the string overload at 7729).
  - LibmpscppMisc9 diceD stub DELETED (released by the unit-175
    render keeping the two-arg signature); stopLookupD stays with
    the corrected owner note.
  - floatstringDaf, btreeDaf, selectStdinDaf, sttyDaf raised as
    named-owner platform stubs in their render files.
  - The DafMsv carrier extended in DafCRuntime.kt (unit 172).
  - The three T1 rider wording fixes applied (LeaningToothpickize
    punt-to-punct + the predicate comment; MpsLog2
    -inf-or-NaN; MemsizeHMisc config constants).

Dafny ledger: suite 70 verified 0 errors 0 warnings (55 baseline +
the new spec/extract.dfy at 15: Escan 9, at_test 5, expx 5,
extract 15, length_count 6, mps_asin 3, mps_log10 5, mps_log2 5,
piece 8, qlength_count 7, smoke_test 2); exit 0 on every spec,
in-sandbox dafny 4.11.0 with DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1.
The extract spec models the adjust ladder and the clamped
effective slice with bound-domain contracts (InvertedRangeContract,
OriginContract, PastEndContract, ClampIdentity, totality carrier);
the start-of-zero index -1 read and the empty-in origin read are
EXCLUDED from the contracted domain (the OOB divergences the
render guards); the inclusive-copy overshoot at the clamp folds
to the exclusive min(j+1, n) effective bound. Two spec compile
rounds caught in-sandbox pre-push: the helper slice bound (j+1
above length at the clamp) and the ClampIdentity bound shape. One
Kotlin compile detail: the wrapper keeps the two-arg diceD
signature so the push files stay call-compatible (the append-mode
svPtr taken from the DafInterpState singleton).

Gates: kotlinc whole-arm gate over the 86-file daf tree SILENT
EXIT=0 twice (the gate copy; then the fresh repo arm copy with
the new files added; 75 base + 11 tranche renders). H-015 hygiene
scan clean on all touched files including the spec (no CJK; no em
dashes; no backticks; all dollar occurrences escaped; trailing
newlines verified).

Consult ledger (nominal ladder 2026-11-22T16:00:00Z..16:06:30Z,
30s steps, no collision with the spent bands 21:05-21:16, 02:00-
02:20, 03:40-03:51, 04:20-04:29, 05:10-05:20, 05:30-05:39, 06:00-
06:30, 07:00-07:17, 15:10-15:41:30, 15:45-15:51):
  165 16:00:00Z rows 39/40/41/43/99/100  lattice twin + EXEMPT
  166 16:00:30Z rows 60/25/6/22/98  loader + release + TAG fix
  167 16:01:00Z rows 90/37/91  dispatcher + SQLITE live-arm
  168 16:01:30Z rows 28/72/44/81/15/10  adjust ladder kernel
  169 16:02:00Z row 37  header ledger (manual-decision)
  170 16:02:30Z rows 60/59/39  platform stubs + sigint reuse
  171 16:03:00Z rows 103/92/40/91/54  walk reuse on the released seat
  172 16:03:30Z rows 18/6/22  carrier extension
  173 16:04:00Z rows 83/90/33  compiler arm mirror
  174 16:04:30Z rows 39/40/28/41/43/26/99/100  dispatch + intLong
  175 16:05:00Z rows 92/103/40/91/54/68/111  pair trim + release
  (seats 16:05:30Z; spec 16:06:00Z; gate 16:06:30Z)

PILOT_TRACKING: the batch-6 tranche-2 rows 165-175 marked DAF
translated (the DAF column the tranche-1 note introduced); the DAF
B6 tranche-2 note paragraph sits above the tranche-2 table. The
heuristic log grows to 174 rows.

Files pushed (byte counts, ASCII bytes):
  wp2/arms/DAF/src/main/kotlin/daf/MpsAbs.kt 2173
  wp2/arms/DAF/src/main/kotlin/daf/StopInit.kt 4072
  wp2/arms/DAF/src/main/kotlin/daf/Mglobal.kt 3421
  wp2/arms/DAF/src/main/kotlin/daf/Extract.kt 3212
  wp2/arms/DAF/src/main/kotlin/daf/InterpHMisc.kt 2166
  wp2/arms/DAF/src/main/kotlin/daf/TimeOutGetstr1.kt 4378
  wp2/arms/DAF/src/main/kotlin/daf/GlobalMax.kt 3650
  wp2/arms/DAF/src/main/kotlin/daf/StateVectorHMisc.kt 2557
  wp2/arms/DAF/src/main/kotlin/daf/ExpandOrder.kt 5736
  wp2/arms/DAF/src/main/kotlin/daf/Modulo.kt 4355
  wp2/arms/DAF/src/main/kotlin/daf/Dice.kt 6125
  wp2/arms/DAF/src/main/kotlin/daf/DafInterpStubsT3.kt 12464 (edit)
  wp2/arms/DAF/src/main/kotlin/daf/LibmpscppMisc9.kt 16343 (edit)
  wp2/arms/DAF/src/main/kotlin/daf/DafCRuntime.kt 6979 (edit)
  wp2/arms/DAF/src/main/kotlin/daf/LeaningToothpickize.kt 2791 (rider)
  wp2/arms/DAF/src/main/kotlin/daf/MpsLog2.kt 1897 (rider)
  wp2/arms/DAF/src/main/kotlin/daf/MemsizeHMisc.kt 2151 (rider)
  wp2/arms/DAF/spec/extract.dfy 4433
  wp2/logs/DAF_heuristic_log.csv 69742 (160 + 14 rows)
  wp2/PILOT_TRACKING.md 41594 chars (T2 rows + note)
  wp2/arms/DAF_B6T2_fragment_2026-11-22.md (this file)

Tranche closed; DAF B6 T3 (units 176-182, complex stratum) follows
next, then the 183-185 big-three session; BOTH B5 T2 remains
queued FIRST after DAF closes per the committed order.
