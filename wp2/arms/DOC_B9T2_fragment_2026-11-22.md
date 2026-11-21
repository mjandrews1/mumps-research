# DOC B9 T2 journal fragment, 2026-11-22

### DOC B9 tranche 2 (units 257-267, core stratum) - 2026-11-22

Sandbox alive on the T1 session continuation (no reset between
tranches this time); base state unzipped per rebuild-recipe.md from
repo.zip at master e2c21db (journal 6059 lines, events CSV 1395
lines / 92907 bytes). Batch-9 core tranche: the units.csv
batch-column-9 block rows 258-268 (11 rows), DOC sequential ids
257-267. All 11 T2 dumps dumped with dump4.py and physically
verified at exact physical LOC (107/75/47/15/28/25/55/25/16/26/30
x the roster order below; the GlobalOrder four-arg inline
disambiguated at globalOrder.h:8 against the libmpscpp corpus
rows; Eval physically at interp-sym.cpp:1192).

Render ledger (unit; units.csv row: LOC, score, rank - entry);
all physically verified this session:

- compiler-sys1.c::<misc> (unit 257; row 258: LOC 107, score
  22.4): H-004, hit-reuse - CompilerSys1Misc.kt, the biggest
  unit of the tranche: banner ledger (the Fortran lineage note
  and the "not all functions work at present" admission kept
  verbatim), define ledger (Sysout macro -> CompilerState.emit;
  GBLROOT 30 raised as the per-file const; CRTN/CTRLZ and TAB/
  LINEFEED CITED not redeclared - package-wide consts), extern
  FILE* Out fold, prototype roster (cannon the landed unit),
  lpad DUPLICATE-DEF note (byte-identical to the StrmanipMisc
  body, the strcatx precedent - no second definition), sigc
  (compiler-sys1.c:804; the PLAIN runtime ctrlc extern folded
  to the Globals.ctrlc carrier EDIT-ADDED in Carriers.kt, the
  GlobalExceptionCode raise precedent - NOT the Msv field of
  the Sigint ladder), itoa K&R (rad dead per H-013), linepad/
  linepad1/linepad2 emitters (the sent count and dead locals
  kept per H-013; linepad1 keeps the fprintf-return-count dot
  fill quirk to column 25), gcvt K&R (i dead; %g Locale.US per
  H-009), mult K&R (FLOAT precision per H-010; atof folds to
  strtod; the "//double atof();" vendor fossil kept as note).
- interp-sym.cpp::<misc> (unit 258; row 259: LOC 75, score
  24.9): H-004, hit-reuse - InterpSymMisc.kt: the sym.c banner/
  define/include/prototype ledger with dispositions per line
  (Interpret the unit-119 family, ScanParse landed, the __label_
  lookup/__text_function externs opaque seats, the commented-out
  _text_function init block kept as the H-013 fossil) PLUS THE
  ONE BODY IN BAND: _ascii rendered inline (atoi the Find.kt
  seat, the 1-based clamp, the "-1" miss arm via strmove, the %d
  Locale.US hit arm).
- compiler-parse.c::ExpandName (unit 259; row 260: LOC 47,
  score 22.4): H-014, hit-reuse - ExpandName.kt: the NAME
  expander of the ExpandGet/ExpandOrder family (the unit-16
  template; arg1/arg2 32-char carriers; the CodedComma
  default-slot Extract2-vs-raw-copy tail; the GlobalName
  emission and the two-line LocalName emission kept byte
  verbatim including the trailing space); the "#S#"-or-"#S#"
  DUPLICATE test of the #S# arm kept verbatim (the ExpandGet
  unit-16 fossil, H-013; ExpandOrder has the corrected pair).
- interp-sym.cpp::Eval (unit 260; row 261: LOC 15, score 4.5):
  H-014, hit-adapt - Eval.kt: the XECUTE-family indirection
  evaluator; AllocSV (unit-239) push, the vendor memcpy
  whole-struct copy folds to the unit-119 copyMsv field-wise
  helper (per-file private twin per the vendor verbatim body
  repetition), the dead LineNumber store between the pair kept
  as the H-013 note, t2=1, strcpy to xd, xpx=0, Stubs.parse_
  delegate, the pd1-slice tail at sdlim, free the GC no-op.
- libmpscpp.cpp::global::Avg (unit 261; row 262: LOC 28,
  score 11.2): H-014, hit-reuse - GlobalAvg.kt: the GlobalRef
  extension twin of the landed unit-136 Sum (Concat member
  precedent); trailing control-byte trim quirk kept, XNEXT
  sibling walk with strncmp prefix break, get_global then tpx
  checked, GlobalExceptionCode INVALID_CONVERSION=1 + throw on
  the sscanf "%lf" NO-PARSE arm (folded to a local sscanfLF
  helper that surfaces null distinct from a genuine 0.0
  parse, unlike Sum's plain strtod fold), j==0 -> 0.0, tot/j.
- libmpscpp.cpp::mstring::FixGblRef (unit 262; row 263: LOC 25,
  score 6.0): H-012, hit-reuse - MstringFixGblRef.kt: the
  bare ^gbl() -> quoted normalizer; the b="" return arms vs
  the *this SELF-returning arm kept distinct (this, not a
  copy), ASCII isalpha walk, the two-quote splice surgery on
  the STR_MAX carrier.
- interp-service.cpp::<misc> (unit 263; row 264: LOC 55,
  score 18.4): H-004, hit-reuse - InterpServiceMisc.kt: PURE
  LEDGER (banner/define/include/prototype roster with
  dispositions; the Eval prototype resolves the unit-260 body
  THIS tranche; NO bodies in this band - the no-_ascii
  contrast note against the interp-sym twin is ledgered).
- interp-strmanip.cpp::xindex (unit 264; row 265: LOC 25,
  score 11.5): H-010, hit-reuse - FindSym.kt TAG CORRECTION
  (u243 Concat precedent): the batch-1 lub note was "owned by
  interp-sym.cpp::xindex; not a roster unit" - WRONG on both
  counts (interp-sym.cpp only CALLS xindex at :1291/:1371;
  the body is interp-strmanip.cpp:87 and row 265 IS a batch-9
  roster row), so the stub FLIPPED to the landed body in place
  (same-name top-level collision rule); vendor shape kept
  verbatim (pattern/source last-index walks, the istrt clamp,
  the flg probe loop, 1-based return). TWIN NOTE: the
  compiler-sys1.c::xindex sibling (row 297) is batch-10's row.
- include/mumpsc/globalOrder.h::GlobalOrder (unit 265; row
  266: LOC 16, score 5.6): H-014, hit-reuse - GlobalOrderH.kt:
  the FOUR-ARG inline (CharArray family; the landed unit-39/69
  String GlobalOrder renders distinguished per the same-name
  H-015 rule); the >127 quantization loop kept, tmp2 NULL the
  OrderNext default direction, the "1"/"-1" arms, the
  \u0024order error message with the dollar escaped (the vendor
  "\u0024order()..." vendor text kept, the dollar escaped as
  \u0024 per H-015); ORDERNEXT 12 /
  ORDERPREV 13 (defines.h:170/171) RAISED in Carriers.kt next
  to XNEXT; the Stubs.GlobalOrder inline seat FLIPPED with
  the tmp2 carrier widened String -> String? (source-
  compatible; all call sites pass non-null).
- libmpscpp.cpp::mdh_tree_level_add (unit 266; row 267: LOC
  26, score 7.6): H-015, hit-reuse - MdhTreeLevelAdd7.kt: the
  SIX-column overload closing the family arity gap (landed
  seats covered one through five, seven, eight); the
  MdhTreeLevelAdd6.kt template (dollar built programmatically,
  ce/d0/cf coded delimiters, identity-hex pointer fold, depth
  decimal, col6 the close-paren column, zfcn dispatch).
- interp-arith.cpp::mps_floor (unit 267; row 268: LOC 30,
  score 8.1, rank 103): H-018, hit-reuse - MpsFloor.kt: the
  floor intrinsic on the plain-double reference arm (floorl/
  strtold BIGFLOAT arm documentary); the MULTI_PRECISION GMP
  ladder with its strchr('.') nontrivial live-side gate and
  its strcmp(out,in) integer-arg tail ledgered as the excluded
  documentary arm; the vendor int-0 return FOLDED to Unit per
  the T1 family-delegate convention (MpsLog/MpsExp2 precede;
  the seat is Unit and no caller reads it), the constant kept
  as the comment fossil.

Seat flips and ledger edits this push (Stubs.kt, Carriers.kt,
ZfcnsH.kt, FindSym.kt):

- Stubs.mpsFloor -> doc.mpsFloor delegate (the T1-resolved tag
  flips at its tranche as scheduled; mpsCeil/mpsTrunc stay
  throwing, batch-10 roster rows).
- Stubs.Eval -> doc.Eval delegate; Stubs.linepad and
  Stubs.linepad2 -> doc delegates (the Epi.kt/ForCode.kt/
  fcninit call sites bind unchanged); Stubs.mult CHAR-ARRAY
  OVERLOAD -> doc.mult delegate (the String-arg overload STAYS
  for its Cannon.kt emission call sites; the earlier
  interp-arith.cpp:624 batch-7 tag was a boundary
  misconstruction, corrected in the seat note).
- Stubs.globalAvg -> g.Avg() delegate (the unit-130 seat
  resolves as its comment promised); Stubs.fixGblRef ->
  a.FixGblRef() delegate (the earlier "no roster row" note was
  the misconstruction; row 263 owns the body); Stubs.xindex ->
  doc.xindex delegate (FindSym.kt in-place body, tag corrected);
  Stubs.GlobalOrder inline -> doc delegate with tmp2 nullable.
- Carriers.kt: ORDERNEXT 12 / ORDERPREV 13 consts raised
  (defines.h:170/171, the globalOrder.h unit cite);
  Globals.ctrlc EDIT-ADDED (the plain runtime extern of the
  sigc handler).
- ZfcnsH.kt: _ascii FORWARD-TAG CORRECTION (the ledger wrote
  "batch-10 roster rows own it"; NO units.csv _ascii row
  exists, grep-verified; the single body rides the u258
  <misc> ledger row this tranche - the _horolog/Concat
  precedent).

Miss rows: none recorded this tranche (0 of 11 consults).

Whole-arm kotlinc gate (kotlinc 2.1.20, sha a118197b...): 250
source files, silent pass, EXIT=0, 0 errors / 0 warnings. H-015
hygiene scan clean on all new and edited files (raw dollar
escaped as \u0024, no backticks, no double-bang, no non-ASCII).
Red-build count: 0 across the tranche (one pre-gate fold
settled at the first compile: the mpsFloor vendor int-0 return
folded to Unit per the T1 family-delegate convention).

Events: 22 rows appended this tranche (the 11 consults x
reuse + hit pairs; 709 consult rows -> 1416 event rows total
at this push, 1417 CSV lines). The regenerated-CSV byte-
identity verification runs at batch close per the B7-close
instrument precedent.

Next protocol step: DOC batch 9 tranche 3 (the complex stratum
rows, DOC units >the rows 248-257 block<: parse_ the 1201-LOC
monster, the compiler misc-3 600-LOC ledger, btreeX, kill_code,
the native-btree misc, the mpstime misc, sql, cgi.h, Jaccard),
then the B9 close instrument.

DOC consult grammar rows (timestamps identical to the events CSV
append of this push):

- DOC consult: unit compiler-sys1.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T05:10:00Z
- DOC consult: unit interp-sym.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T05:11:00Z
- DOC consult: unit compiler-parse.c::ExpandName, entry H-014, outcome hit-reuse, timestamp 2026-11-22T05:12:00Z
- DOC consult: unit interp-sym.cpp::Eval, entry H-014, outcome hit-adapt, timestamp 2026-11-22T05:13:00Z
- DOC consult: unit libmpscpp.cpp::global::Avg, entry H-014, outcome hit-reuse, timestamp 2026-11-22T05:14:00Z
- DOC consult: unit libmpscpp.cpp::mstring::FixGblRef, entry H-012, outcome hit-reuse, timestamp 2026-11-22T05:15:00Z
- DOC consult: unit interp-service.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T05:16:00Z
- DOC consult: unit interp-strmanip.cpp::xindex, entry H-010, outcome hit-reuse, timestamp 2026-11-22T05:17:00Z
- DOC consult: unit include/mumpsc/globalOrder.h::GlobalOrder, entry H-014, outcome hit-reuse, timestamp 2026-11-22T05:18:00Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-014, outcome hit-reuse, timestamp 2026-11-22T05:19:00Z
- DOC consult: unit interp-arith.cpp::mps_floor, entry H-018, outcome hit-reuse, timestamp 2026-11-22T05:20:00Z
