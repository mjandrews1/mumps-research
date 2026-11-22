# DAF B5 T2 journal fragment, 2026-11-22

### DAF B5 tranche 2 (units 134-143, core stratum) - 2026-11-22

Tranche scope per the committed batch-5 rank order: the T2 core
stratum (PILOT units 134-143 = units.csv rows 136-145), ten units,
one Dafny spec (spec/expx.dfy), one repair render of a colliding
id (disclosed below). Sandbox ~/b4 was rebuilt fresh this session:
vendor tree sha256 verified (621deb...ffdbe, 190531550 bytes), JDK17
+ kotlinc 2.1.20 (kcd sha a118197b...), Dafny 4.11.0
(4.11.0+fcb2042d6d043a2634f0854338c08feeaaaf4ae2, matches m1-build;
needs DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1 in this sandbox),
repo tree = master @dd9b6cb (the DOC B10 close already spliced; 44
kt in DAF arms tree pre-tranche). dump4.py recreated; all 10 T2
dumps verified at exact physical LOC against the vendor tree.
Arm independence kept as before: rendered from pristine vendor
sources + BASE-heuristic-log consultations only; no DOC/PRO
Kotlin consulted.

PROTOCOL DEVIATION, LOGGED (one-render rule repair disclosure):
the B4 T1 tranche rendered the colliding Transpose id against the
WRONG corpus body. units.csv carries exactly TWO
libmpscpp.cpp::Transpose rows: row 118 (loc 15, score 8.5, the
void Transpose(char* a, char* b) body at :2092) and row 142 (loc
23, ptr 0, score 14.1, the 23-LOC MSV service wrapper at :10316).
The B4 T1 render of unit 100 wrote the row-142 wrapper body into
Transpose.kt while logging two consult misses on the id as if it
were the row-118 batch-4 unit; row 118's actual body was never
rendered. Adjudication per the one-render rule (a corpus body may
be rendered exactly once, and every roster row must get exactly
one render): (a) TransposeAb.kt is a NEW repair render of the
row-118 body (the char a/b swap walk with the arg2 i/j order
quirk preserved, miss-then-adapt), landing in THIS tranche; (b)
Transpose.kt keeps the row-142 wrapper and gets a verification-
only addendum column recording that mapping (no code change; the
wrapper body is verbatim the B4-T1 render); (c) this paragraph is
the logged repair disclosure. The T2 roster row 142 (the wrapper)
is therefore satisfied by the B4-T1 render + the addendum, and the
row-118 batch-4 unit is satisfied by TransposeAb.kt. The B4 close
totals are NOT reopened; this repair lands entirely within B5 T2.

Multiply adjudication: the corpus carries THREE
libmpscpp.cpp::Multiply rows (49 loc 28 score 16.8 = the MSV
dispatcher at :10193; 141 = the string overload at :2211 score
21.4; 201 = the char* overload at :2252 score 20.8). This tranche
renders the ROW-141 string overload (metrics-unique score 21.4;
the found-dict last-wins collision of the dump stage was fixed at
the dump, not at the render). The dispatcher and char* rows stay
with their owning batches.

lpad TAG CORRECTION, LOGGED: the earlier DafInterpStubsT3 lpadD
raise wrongly tagged the owner as interp-arith.cpp; grep of the
vendor tree found no lpad body there. The owner is
interp-strmanip.cpp (row 137, this tranche). The stub was flipped
to delegate to lpadStrmanipD(src, dst, l) with a mapping comment;
the sole caller (Fcn.kt) passes the same array twice, alias-safe
for the in-place pad. Stub-seat discipline otherwise unchanged.

Per-unit render notes:
- Unit 143 interp-rpm.cpp::parse_count (PILOT 143; units.csv 136):
  the token_type constants render as TOKEN_COMMA_D=4 and
  TOKEN_DOT_D=5 (PATTERN_CODE=6 already seated); the goto exit arm
  folds to early returns; the hyphen/equals exit flags and the
  two-part argument comma split are preserved (ParseCount.kt).
- Unit 141 interp-strmanip.cpp::<misc> (units.csv 137): the trio
  strcatxD (destination-alias fold) / rindexD (the strrchr
  last-index walk) / lpadStrmanipD (the named-owner seat of the
  corrected lpad tag) (StrmanipMisc.kt).
- Unit 142 interp-sym.cpp::SysDump (units.csv 138): the symbol
  table dump walk renders with the DafStabNode + DafSymChains side
  carriers (tree shape carried; the C key[] carrier quirk kept,
  no key array); mltoa rides the mltoaDaf named-owner stub (Mltoa
  at interp-arith.cpp:84, not a corpus unit) (SysDump.kt).
- Unit 140 interp-sysfunc.cpp::getstr1 (units.csv 139): fgetc ->
  a reader fold with the C NUL-byte falsiness quirk of the drain
  kept verbatim (vendor quirks are not repaired on any arm); the
  oversize error arm prints the same 69-char prefix snippet;
  INPUT_LENGTH_D=10 added (Getstr1.kt).
- Unit 139 interp-arith.cpp::expx (units.csv 140): the ifdef-web
  twin of mps_asin/mps_log10 (same file, same lattice): reference
  arm is gcvt_wrapper(pow(strtod(a),strtod(b))) -> fmt8g over the
  kotlin pow; the MPFR and long-double branches are dead on the
  reference build, carried as comment fold. The mps_exp family
  precedent (row 99) carries pow; the exponent-domain NaN
  pathology of a negative base is unguarded, ledgered per the
  asin/log10 precedents (Expx.kt).
- Unit 138 libmpscpp.cpp::Multiply (units.csv 141): the row-141
  string overload over the MpsGlobalNode seats
  (invoke/order/data/kill/value) (Multiply.kt).
- Unit 137 libmpscpp.cpp::Transpose (units.csv 142): satisfied by
  the B4-T1 wrapper render + the verification addendum recorded
  in the repair disclosure above (no new code).
- Row-118 repair render: TransposeAb.kt (transposeAbD), the
  void Transpose(char* a, char* b) body at :2092, the arg2 i/j
  order quirk preserved verbatim.
- Unit 136 libmpscpp.cpp::global::Sum (units.csv 143): the
  get_global 16-site TU service seat (getGlobalDaf named-owner
  stub, raised in-file); the GLOBAL_NOT_FOUND_D=2 check is reused
  from MstringLength.kt (B5 T1), not duplicated; the Min/IDF
  prefix-mismatch break kept exactly (Sum.kt).
- Unit 135 compiler-parse.c::ExpandData (units.csv 144): the
  mistranslate check then the pointer-expand walk in two stages;
  the void reference body renders String? per the BASE row-44
  out-param carrier fold on the null return arm (ExpandData.kt).
- Unit 134 libmpscpp.cpp::GlobalQS (units.csv 145): the coded
  service relay: the Mumps text s %=$qs(a,c) is built into a
  method-local StringBuilder (the static char tmp[STR_MAX]/r[STR_MAX]
  carriers fold; r is ledgered, the vendor never uses it), the
  literal dollar escaped per the row-36 rule, xecuted through the
  xecuteDaf named-owner stub (the row-98 overload collapse: the
  three C++ xecute overloads at :7053/:7063/:7073 collapse onto the
  String seat), and the % fetch rides SymGetD (row 92); the tpx
  flags live on DafMsv (GlobalQS.kt).

Dafny ledger: exemption carried for the service-call shaping
units (Sum over the get_global seat, ExpandData over the parse
walk with the String? carrier, GlobalQS over the xecute relay,
Multiply over the node stub seats); the two Transpose rows are
swap-shape twins over the node carriers, ledgered with the
SysDump/parse_count/getstr1/strmanip seats under the same
call-shaping exemption family as T1. Spec rendered and verified
(in-sandbox dafny 4.11.0): spec/expx.dfy (5 verified, 0 errors, 0
warnings, exit 0; the Pow positivity and unit-exponent properties
are DECLARED {:axiom} assumptions - spec-assumption divergence
note mirroring mps_log10.dfy: the %.8g text format is out of
scope). Full suite in-sandbox after expx: 38 verified pre-tranche
+ expx = 43 verified, 0 errors, 0 warnings.

Gates: whole-arm kotlinc 2.1.20 silent PASS at 54 files (44
pre-tranche + 10 new; Transpose.kt and DafInterpStubsT3.kt edits
in-place), run twice: once over the render gate copy and once
over the staged repo copy (both SILENT EXIT=0, 0 warnings). H-015
scan clean on all touched files: one CJK non-ASCII slip in Sum.kt
and one em dash caught and fixed pre-push (the em dash replaced
with a hyphen); all dollar occurrences escaped (\u0024 in Kotlin
strings, \$ in comments); trailing newlines verified byte-wise.
In-sandbox dafny 4.11.0 verify green; gradle + dafny re-run at
tranche close on m1-build (Mark).

Renders and consult ledger (each consult logged before applying):
- DAF consult: unit interp-rpm.cpp::parse_count, entry BASE-log:68, outcome hit-reuse, timestamp 2026-11-22T07:00:00Z
- DAF consult: unit interp-rpm.cpp::parse_count, entry BASE-log:37, outcome hit-adapt (enum-constant named seats), timestamp 2026-11-22T07:00:30Z
- DAF consult: unit interp-strmanip.cpp::<misc>, entry BASE-log:70, outcome hit-reuse (strcatx fold), timestamp 2026-11-22T07:01:00Z
- DAF consult: unit interp-strmanip.cpp::<misc>, entry BASE-log:33, outcome hit-reuse (rindex strrchr walk), timestamp 2026-11-22T07:01:30Z
- DAF consult: unit interp-strmanip.cpp::<misc>, entry BASE-log:37, outcome hit-adapt (lpad owner tag correction logged), timestamp 2026-11-22T07:02:00Z
- DAF consult: unit interp-sym.cpp::SysDump, entry BASE-log:60, outcome hit-adapt (DafStabNode + DafSymChains side carriers), timestamp 2026-11-22T07:02:30Z
- DAF consult: unit interp-sym.cpp::SysDump, entry BASE-log:18, outcome hit-reuse (mltoaDaf named-owner stub; Mltoa at interp-arith.cpp:84), timestamp 2026-11-22T07:03:00Z
- DAF consult: unit interp-sym.cpp::SysDump, entry BASE-log:64, outcome hit-reuse, timestamp 2026-11-22T07:03:30Z
- DAF consult: unit interp-sysfunc.cpp::getstr1, entry BASE-log:2, outcome hit-adapt (fgetc reader fold; C NUL falsiness quirk kept), timestamp 2026-11-22T07:04:00Z
- DAF consult: unit interp-sysfunc.cpp::getstr1, entry BASE-log:108, outcome hit-reuse, timestamp 2026-11-22T07:04:30Z
- DAF consult: unit interp-sysfunc.cpp::getstr1, entry BASE-log:69, outcome hit-reuse, timestamp 2026-11-22T07:05:00Z
- DAF consult: unit interp-sysfunc.cpp::getstr1, entry BASE-log:46, outcome hit-adapt (oversize error arm; 69-char snippet), timestamp 2026-11-22T07:05:30Z
- DAF consult: unit interp-arith.cpp::expx, entry BASE-log:39, outcome hit-reuse (reference arm plain-double; MPFR/long-double dead), timestamp 2026-11-22T07:06:00Z
- DAF consult: unit interp-arith.cpp::expx, entry BASE-log:40, outcome hit-reuse (strtod x2 with 0.0 fallback), timestamp 2026-11-22T07:06:30Z
- DAF consult: unit interp-arith.cpp::expx, entry BASE-log:41, outcome hit-reuse (rows 41+43 verification: gcvt_wrapper(x,15) = gcvt(x,8), no appended decimal point), timestamp 2026-11-22T07:07:00Z
- DAF consult: unit interp-arith.cpp::expx, entry BASE-log:99, outcome hit-reuse (row 99 fold: pow over the hardware-double path), timestamp 2026-11-22T07:07:30Z
- DAF consult: unit spec/expx.dfy (unit 139 expx), entry BASE-log:--, outcome miss (spec rendered: 5 verified; Pow positivity as declared assumption; %.8g format out of scope), timestamp 2026-11-22T07:08:00Z
- DAF consult: unit libmpscpp.cpp::Multiply, entry BASE-log:105, outcome hit-adapt (row-141 string overload over MpsGlobalNode seats), timestamp 2026-11-22T07:08:30Z
- DAF consult: unit libmpscpp.cpp::Multiply, entry BASE-log:54, outcome hit-reuse, timestamp 2026-11-22T07:09:00Z
- DAF consult: unit libmpscpp.cpp::Multiply, entry BASE-log:41, outcome hit-reuse (rows 41+43/40 folds carried), timestamp 2026-11-22T07:09:30Z
- DAF consult: unit libmpscpp.cpp::Transpose, entry BASE-log:105, outcome hit-reuse (row-142 wrapper: B4-T1 render + verification addendum; protocol deviation disclosed above), timestamp 2026-11-22T07:10:00Z
- DAF consult: unit libmpscpp.cpp::Transpose (row 118 repair), entry BASE-log:105, outcome miss, timestamp 2026-11-22T07:10:30Z
- DAF consult: unit libmpscpp.cpp::Transpose (row 118 repair), entry BASE-log:54, outcome hit-adapt (TransposeAb render; arg2 i/j quirk preserved), timestamp 2026-11-22T07:11:00Z
- DAF consult: unit libmpscpp.cpp::global::Sum, entry BASE-log:103, outcome hit-reuse, timestamp 2026-11-22T07:11:30Z
- DAF consult: unit libmpscpp.cpp::global::Sum, entry BASE-log:92, outcome hit-reuse (tpx retrieve-then-throw; GLOBAL_NOT_FOUND_D=2 reused from B5 T1), timestamp 2026-11-22T07:12:00Z
- DAF consult: unit libmpscpp.cpp::global::Sum, entry BASE-log:40, outcome hit-reuse (getGlobalDaf named-owner stub; 16-site TU service), timestamp 2026-11-22T07:12:30Z
- DAF consult: unit libmpscpp.cpp::global::Sum, entry BASE-log:91, outcome hit-adapt (Min/IDF prefix-mismatch break kept), timestamp 2026-11-22T07:13:00Z
- DAF consult: unit compiler-parse.c::ExpandData, entry BASE-log:44, outcome hit-adapt (String? carrier per the row-44 out-param fold), timestamp 2026-11-22T07:13:30Z
- DAF consult: unit compiler-parse.c::ExpandData, entry BASE-log:45, outcome hit-reuse, timestamp 2026-11-22T07:14:00Z
- DAF consult: unit compiler-parse.c::ExpandData, entry BASE-log:2, outcome hit-adapt (mistranslate check then staged expand walk), timestamp 2026-11-22T07:14:30Z
- DAF consult: unit compiler-parse.c::ExpandData, entry BASE-log:16, outcome hit-reuse, timestamp 2026-11-22T07:15:00Z
- DAF consult: unit libmpscpp.cpp::GlobalQS, entry BASE-log:98, outcome hit-adapt (xecuteDaf named-owner stub; row-98 overload collapse), timestamp 2026-11-22T07:15:30Z
- DAF consult: unit libmpscpp.cpp::GlobalQS, entry BASE-log:36, outcome hit-reuse (literal dollar escaped per the escape rule), timestamp 2026-11-22T07:16:00Z
- DAF consult: unit libmpscpp.cpp::GlobalQS, entry BASE-log:92, outcome hit-reuse (SymGetD of the % symbol), timestamp 2026-11-22T07:16:30Z
- DAF consult: unit libmpscpp.cpp::GlobalQS, entry BASE-log:27, outcome hit-adapt (static char carriers fold to the method-local builder; tpx on DafMsv), timestamp 2026-11-22T07:17:00Z

Consult totals for the tranche: 35 consults, 22 hit-reuse, 12
hit-adapt, 1 miss (the spec entry; the row-118 repair miss is
followed by its hit-adapt render consult). Coverage: all ten core
units 134-143 rendered, verification-folded or verification-only-
ledgered; the two collision repairs (Transpose row-118 body,
Multiply row-141 overload) are disclosed above.

Files pushed (one commit):
- 10 new renders under wp2/arms/DAF/src/main/kotlin/daf/:
  ParseCount.kt (units.csv 136), StrmanipMisc.kt (137),
  SysDump.kt (138), Getstr1.kt (139), Expx.kt (140),
  Multiply.kt (141), TransposeAb.kt (the row-118 repair render),
  Sum.kt (143), ExpandData.kt (144), GlobalQS.kt (145)
  (all gate-verified; H-015 scanned)
- 2 edited: Transpose.kt (row-142 verification addendum, no code
  change), DafInterpStubsT3.kt (the lpadD tag-correction flip)
- 1 new spec under wp2/arms/DAF/spec/: expx.dfy (5 verified 0/0,
  exit 0; full suite 43 verified in-sandbox)
- wp2/logs/DAF_heuristic_log.csv (rows 117-133 appended; ids
  contiguous 1-133, 11-col CSV validated; BASE-log hits carry
  source=BASE-log + reused_from row ids, misses manual-decision)
- wp2/PILOT_TRACKING.md (batch-5 table DAF column rows 134-143
  todo -> translated; no header or other-row change; the row-154
  missing trailing pipe preserved as-is)
- wp2/arms/DAF_B5T2_fragment_2026-11-22.md (this fragment)
