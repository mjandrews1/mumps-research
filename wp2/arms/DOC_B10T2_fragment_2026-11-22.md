# DOC B10 T2 journal fragment, 2026-11-22

DOC batch 10 (cycle 9 per C-001) tranche 2, the core stratum:
units.csv rows 289-298 (10 rows), DOC sequential ids 289-298
(core-first order within the batch: the simple rows 278-288
landed at tranche 1, the complex rows 279-288 wait for tranche
3, units renumbered per units.csv row through this batch). All
10 T2 dumps extracted with the recreated dump4.py (load_units
function mode, min_loc 15 / chunk 600) and physically verified
at exact physical LOC (16/179/36/31/28/39/56/18/21/33 x the
roster order below) against vendor line anchors (unpad
interp-sysfunc.cpp:947, defines.h include residual, libmpscpp
.cpp:1150 Hash::operator const char*, Mltoa interp-arith.
cpp:84, wstem interp-stem.cpp:418, sysparms.h include
residual, fnumber.cpp whole-file residual, errors.h whole-
file residual, compiler-sys1.c:249, libmpscpp.cpp:481).

Render ledger (unit; units.csv row: LOC, score, rank - entry);
all physically verified this session:

- interp-sysfunc.cpp::unpad (unit 289; row 289: LOC 16, score
  22.0, rank 175): H-014, hit-adapt - Unpad.kt: the gpad
  numeric-key pad REMOVER (the col9 gpad entry decode: the
  \u001f marker, forced sign byte, the 40-digit GPADWIDTH
  window): the GPADWIDTH/GPADFMT pair RAISED at the row-290
  defines.h render this push, the in-place sign rewrite
  ('0' -> '-', else '+') kept H-007 verbatim, memcpy ->
  arraycopy of the fixed window, sscanf %lf -> the atof
  leading-double seat (the Zfcn atofC / InterpJustify atofJ
  precedent, the ignored-return UB folded to the 0.0 no-parse
  contract), sprintf %lg -> String.format Locale.US %g (the
  Avg precedent); Stubs.unpad FLIPS to delegate (the
  SysfuncMisc fullUnpad token loop binds unchanged).
- include/mumpsc/defines.h::<misc> (unit 290; row 290: LOC
  179, score 21.5, rank 173): H-004, hit-reuse - DefinesH.kt,
  the whole-header macro ledger (the compiler-globals.h /
  builtin.h pure-ledger precedent): Coded* byte family (the
  landed Carriers consts; CodedColon ledgered), the Sym codes
  ledgered to the batch-10 row-280 sym_ owner, the order and
  transaction code blocks (landed Carriers/SqlIsc/BtreeX
  seats; duplicate-define fossils kept), SEED/NBR_ITERATIONS,
  MMARK/TRXLIMIT/NEXTMARK (landed Btree seats), GBLPERMIT,
  LINEFEED/CRTN/CTRLZ (landed), SYMSTORE/SYMGET, GPADWIDTH
  40 / GPADFMT (RAISED there - the unpad consumer), SYM_MAX,
  DELIM/BUF_PARSE/STEM/MNORMAL, the similarity opcode table
  COSINE..GBLNME led to its owner renders.
- libmpscpp.cpp::line1149 (unit 291; row 291: LOC 36, score
  19.6, rank 167): H-014, hit-reuse - Line1149.kt: the ROSTER
  NAME IS THE CHUNKER ARTIFACT (the nameless Hash::operator
  const char* conversion carries the vendor line anchor as
  its id; a regular fun cannot be a conversion operator, so
  the H-015 named seat Hash.lookupC, the operator= assignHash
  precedent) - the read side of the file-backed hash table:
  lhash%size probe, the double-EOF wraparound with loopflg
  restart, c==1/c==2 skips, the strcmp found break, value
  read to NUL, NULL -> Kotlin null; carriers all reuse the
  assignHash seat family (RandomAccessFile seek/read,
  Stubs.lhash, CstringOps.cstring).
- interp-arith.cpp::Mltoa (unit 292; row 292: LOC 31, score
  19.5, rank 166): H-014, hit-reuse - Mltoa.kt (the arith.h
  u57 prototype-roster owner arrives; the batch-2 raise
  DELEGATES with the Int-to-long widening): the i<10 fast
  path, the static digit table, the reverse-digit loop into
  s[126] downward with the residual window strcpy out, BOTH
  overflow guards verbatim (MIN_VALUE negation wrap and the
  Long.MIN/MAX edge equality) - cout banner +
  sigint(100) live/fatal (the SqlIsc precedent).
- interp-stem.cpp::wstem (unit 293; row 293: LOC 28, score
  19.2, rank 164): H-014, hit-reuse - Wstem.kt: the Porter
  ending-group dispatcher (ten when-arms over the landed
  InterpStemMisc tables, escan unit 96, the default returns
  wl); TAG CORRECTION at the seat (the xindex precedent): the
  raise said "libmpscpp.cpp::wstem, not a roster unit" -
  WRONG; the body is interp-stem.cpp:418 and row 293 IS this
  roster unit; Stubs.wstem FLIPS to delegate (the batch-5
  pmw Stem/stem call sites bind unchanged).
- include/mumpsc/sysparms.h::<misc> (unit 294; row 294: LOC
  39, score 18.7, rank 163): H-004, hit-reuse - SysparmsH.kt,
  the whole-header ledger: the configuration-guide block (the
  feature-switch cross-family of the defines.h row-290
  ledger), the PS/PS1 pair riding the landed CompilerCarriers
  consts, GCVT apple arm (the B8 gcvt render), the platform
  Linux/Solaris/GnuDos/MSVisualC conditionals (the Linux arm
  live for the reference build), the commented-out SYSTEM
  fossils and the halt-on-all-error empty arm H-013.
- interp-fnumber.cpp::<misc> (unit 295; row 295: LOC 56,
  score 15.4, rank 147): H-004, hit-reuse -
  InterpFnumberMisc.kt, the whole-file PRELUDE ledger (the
  compiler-write.c shape): banner, Dec 13 2025 revision
  line, SQLITE/SYMDEBUG feature defines (the commented-out
  one the fossil), the include ledger (all rendered or
  ledgered header rows), and the free declaration roster
  (Interpret/ScanParse/Eval led to their corpus renders;
  the __label_lookup/__text_function extern pointers ledgered
  to their mpstime/fcn seats) closing at the fnumber()
  wrapper banner - the fnumber corpus functions are the
  landed earlier-batch Fnumber family.
- include/mumpsc/errors.h::<misc> (unit 296; row 296: LOC 18,
  score 14.4, rank 139): H-004, hit-reuse - ErrorsH.kt, the
  CODE-FRAGMENT include ledger (a fragment include with no
  guard; the ZcommandsCode Out convention): the nine
  emitted if/else-if exception-code C lines, per code its
  printed message (the vendor "Interprerer" MISSPELLING kept
  verbatim H-013), each code named to its landed const seat
  (Carriers/libmpscpp.h const family; NUMERIC_RANGE 5 and
  UNSPECIFIED 0 ride their private const seats); the study-
  side counterpart carriers cited (MumpsGlobalException,
  GlobalExceptionCode).
- compiler-sys1.c::xindex (unit 297; row 297: LOC 21, score
  10.9, rank 107): H-014, hit-reuse - VERIFICATION-ONLY: the
  compiler-side xindex TWIN (vendor :249) of the landed
  unit-264 seat (FindSym.kt, interp-strmanip.cpp:87) - the
  bodies are statement-for-statement identical modulo the
  goto-outer vs flg-flag spelling; the row FOLDS onto the
  u264 render (the fold addendum appended to FindSym.kt, the
  H-015 same-name collision rule disclosed); no new code.
- libmpscpp.cpp::mdh_tree_level_add (unit 298; row 298: LOC
  33, score 10.9, rank 108): H-014, hit-reuse -
  MdhTreeLevelAdd8.kt: the NINE-column overload (vendor
  :481-532), the last family arity with a roster row after
  the landed one-through-eight seats; the unrowed TEN-column
  sibling at :534 is ledgered in the render (the chunker
  fuses its head onto this row's window; the family-shape
  extrapolation is disclosed, no new seat); coded delimiters
  Char(nnn) per H-015, %p identity-hex divergence note,
  zfcn on the H-014 stub; Kotlin overloads resolve by arity.

Seat flips and ledger edits this push (Stubs.kt, FindSym.kt):

- Stubs.unpad -> doc delegate (row 289; SysfuncMisc fullUnpad
  call sites unchanged).
- Stubs.Mltoa -> doc delegate with the Int-to-long widening
  (row 292; the arith.h u57 ledger row ANTICIPATED this
  owner, the raise's "libmpscpp.h:120 macro" tag superseded).
- Stubs.wstem TAG CORRECTED + FLIPPED (row 293; owner tag was
  wrong on both counts; the pmw Stem/stem sites unchanged).
- FindSym.kt: the row-297 verification addendum (the row-265
  twin note in that header now resolves: this batch owns the
  compiler twin, fold adjudicated with the bodies diffed
  this session).

Miss rows: none recorded this tranche (0 of 10 consults; one
hit-adapt - the unpad sign-byte window decode - and nine
hit-reuse).

Whole-arm kotlinc gate (kotlinc 2.1.20, sha a118197b...): 277
source files (268 + 9 new), silent pass, EXIT=0, 0 errors / 0
warnings, red-build count 0 across the tranche. H-015 hygiene
scan clean on all new and edited files (no raw dollar, no
backticks, no double-bang, ASCII only) and every pushed text
file ends with the trailing-newline byte (the B9 close
splice-repair rule asserted at push time).

Events: 20 rows appended this tranche (the 10 consults x
reuse + hit pairs; 730 consult rows -> 740, 1458 event rows ->
1478 at this push, 1479 CSV lines). The regenerated-CSV byte-
identity verification runs with this push: the python port of
wp2/analysis/R/export_doc_events.R over the journal WITH this
fragment appended reproduces the appended CSV byte-for-byte
(the same port reproduces the at-rest 1459-line CSV first,
730 -> 1458, before the splice).

Next protocol step: DOC batch 10 tranche 3 (the complex
stratum, units.csv rows 279-288: compiler-mumps.c::main, the
interp-sym.cpp::sym_ monster resolving the Stubs.sym_ seat
tagged at tranche 1, libmpscpp misc-8, libmpscpp.h misc,
misc-4, interp-select misc, interp-bmgsubs misc, inout,
interp-globalb misc, global::DocCorrelate), then the B10 close
instrument (the B9-close shape: coverage checklist, consult
totals, per-unit list, events regen, stub ledger, red-build
ledger, grading).

DOC consult grammar rows (timestamps identical to the events
CSV append of this push):

- DOC consult: unit interp-sysfunc.cpp::unpad, entry H-014, outcome hit-adapt, timestamp 2026-11-22T06:11:00Z
- DOC consult: unit include/mumpsc/defines.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T06:12:00Z
- DOC consult: unit libmpscpp.cpp::line1149, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:13:00Z
- DOC consult: unit interp-arith.cpp::Mltoa, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:14:00Z
- DOC consult: unit interp-stem.cpp::wstem, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:15:00Z
- DOC consult: unit include/mumpsc/sysparms.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T06:16:00Z
- DOC consult: unit interp-fnumber.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T06:17:00Z
- DOC consult: unit include/mumpsc/errors.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T06:18:00Z
- DOC consult: unit compiler-sys1.c::xindex, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:19:00Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:20:00Z
