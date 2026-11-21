# DOC B9 T3 journal fragment, 2026-11-22

### DOC B9 tranche 3 (units 268-277, complex stratum) - 2026-11-22

Sandbox rebuilt fresh this session per rebuild-recipe.md from
repo.zip at master 6875907 (journal 6250 lines, events CSV 1417
lines / 94421 bytes, 250 kt files in the DOC arm source set; the
baseline gate silent EXIT=0 at 250 before any T3 write). Batch-9
complex tranche: the units.csv batch-column-9 block rows 248-257
(10 rows), DOC sequential ids 268-277. All 10 T3 dumps dumped
with the recreated dump4.py (load_units residual/misc-chunking
reproduced) and physically verified at exact physical LOC
(1201/600/502/132/84/98/121/78/58/51 x the roster order below);
the parse_ monster dump is 1796 lines, the largest of the arm.

Render ledger (unit; units.csv row: LOC, score, rank - entry);
all physically verified this session:

- compiler-parse.c::parse_ (unit 268; row 248: LOC 1201, score
  765.3, rank 118): H-014, hit-adapt - CompilerParseMain.kt,
  the tranche monster and the arm's single biggest unit: the
  compiler-side expression parser/code generator. The five
  static tables transcribed MECHANICALLY from the dump (the
  declared-256/initialized-128 C partial-initialization tails
  kept byte-for-byte, the opcode/ncode/dcode/operand zero-
  fills included); the single-function goto web (start/nchar/
  scan_operand/un_nest/var1/exec/nxt_expr/dec_stk/
  nxt_operator/finish) folded to the mutually-recursive label
  functions of a private ctx holder (the 40-entry s1p stack
  the shared static); the nchar ladder (CodedOpen push, the
  comma stack-join, the fcn/array and precedence close-paren
  arms, the \u0024select unification colon with the cod209
  append, QUOTE remap through the code[] table, the exponent
  entry, the opcode insert with the Msv.ErrChar seat); the
  numeric scan with the %-lf prefix parse folded to a decimal-
  prefix regex (H-012) and the %.15g canonicalize (H-009
  Locale.US) plus the cannon and quote-wrap; the escaped-quote
  literal walk; the variable-name walk with the goto-start
  CodedOpen recursion; the global reference builder (the \\x0
  extension, the #G#/#S# strcatx segments, the ZZ interface,
  the three-conversions-two-percents vararg quirk ledgered);
  the FULL \u0024-family ladder (test/io/principal/x/y/
  horolog; char/fnumber/extract/length/find/ascii/justify/
  piece/select/storage/random; the catx gtmp tail; the
  Expand* dispatch by first letter; no_arg Z/z, \u0024\u0024
  MyFunc, the fcn chain); the has-array bracket emitters over
  the 206/207/208 indexcoding; the cvar/setname local seat;
  the nxt_expr operator emission ladder (INDIRECT with its
  dead tail ledgered H-013, the case-18 LIVE fallthrough into
  NOT kept sequential, ConCheck numcomp1-vs-numcomp, the
  PATTERN/NOTPATTERN fixstr arms, MODULO, AND/OR/NOTAND/
  NOTOR); the pd1-solving finish. Stubs.parse_() zero-arg
  seat FLIPS to the render (H-015: the unit-93 interp twin
  keeps the one-arg overload, arity-resolved; IfCode.kt
  binds unchanged).
- libmpscpp.cpp::<misc-3> (unit 269; row 249: LOC 600, score
  472.2): H-004, hit-reuse - LibmpscppMisc8.kt: the residual
  misc ledger of the third misc chunk (the earlier-batch
  LibmpscppMiscN family pattern): globalLe/globalEq/
  globalNe plus the GlobalRef eq/ne/assign twins, the
  SwGlobalsM8 swap arm, and the Mstring operator-family
  bodies (get/addAssign/subAssign/mulAssign/divAssign/
  remAssign/invoke2); 33 NEW below-roster throwing seats
  raised in Stubs.kt for the remaining operator backends
  (batch-10 roster rows cited at the seats).
- interp-native-btree.cpp::btreeX (unit 270; row 250: LOC
  502, score 393.4, rank 219): H-014, hit-adapt - BtreeX.kt:
  the native btree global-array engine. Byte-domain wrapper
  (CharArray entry/exit copies into the ByteArray key/bd
  working pair; the recursion over a btreeXCore core); the
  slot-zero alias binds the freadx block cache (H-007);
  gblDataRafX backstop; BtreeXState (trxmax, the
  TRXLIMIT_BX 100000 trace stack, opnflg, rtf); the
  auto-open path (exists-probe, create with five zero blocks,
  the 2-byte dat init, root marks LE 777/0/0/16, super
  dump); RETRIEVE, the STORE reuse path (newrec/save/save1
  with the atSave seek(datptr) fix), the KILL chain
  (scanblocks, BigKey externs, recursive RESTORE/RETRIEVE
  cores, the FoundBlock/FoundOffset/Reinsert seats), XNEXT,
  upblock, the new-tree first insert; O_EXCL/flock/NEW_TREE/
  READ_ONLY as H-018 inert
  ledgers. Btree.kt: the btreeX
  throwing stub REMOVED with the tag corrected (the former
  "not a roster unit" note was the misconstruction; Btree
  dispatches to the render); BtreeExterns.kt EDIT (the
  FoundBlock/Reinsert/FoundOffset Int seats); BtreeH.kt
  EDIT (BtreeStatic.gblDataFile RandomAccessFile seat).
- compiler-kill.c::kill_code (unit 271; row 251: LOC 132,
  score 110.8): H-014, hit-reuse - KillCode.kt: the
  argument-kill expander (the one-render rule holds: the
  set_code/findseats/kbfind seats cited) with the traverse-
  and-emit arms of the vendor body (paren scan, the
  comma-split argument ledger, the kill() delegation).
- interp-native-btree.cpp::<misc> (unit 272; row 252: LOC 84,
  ptr 35): H-004, hit-reuse - InterpNativeBtreeMisc.kt: the
  btree banner/def/prototype ledger with its band bodies:
  OK_NB/BAD_NB (public: the BtreeX.kt cross-file cites),
  the BPtr(buf,off) pointer holder, memxmove, strxxcpy,
  flush (the direct slot-0 alias-write adaptation, the LE
  777 mark at offset 12), strxcmp (the r out-param), and
  allocblk (the LE int at the root ADSIZE).
- mpstime.cpp::<misc> (unit 273; row 253: LOC 98, ptr 30):
  H-018, hit-adapt - MpstimeMisc.kt: the module banner
  kept verbatim (the Mumps Compiler LGNU note cited); the
  file-private floatstring twin (NOT a flip: Stubs.floatstring
  stays throwing, the interp-arith.cpp:597
  owner is below-roster); numcomp2, strxcat, strcatz
  (the "in" KEYWORD param renamed inn), setAlarm the
  H-018 inert disarm fold returning 0u, _label_lookup
  (-1), _text_function (null). MumpsMisc.kt EDIT: the
  setAlarm throwing stub REMOVED, seat moved to this
  render (the xindex tag-correction precedent: the
  mumps.cpp stub's OS-setitimer owner tag was the
  misconstruction); SyncClock stays (the byte-identical
  twin cited, no second definition).
- interp-sql.cpp::sql (unit 274; row 254: LOC 121): H-018,
  hit-adapt - SqlIsc.kt: the six-arg sql body under the
  mumps-sqlite3 build-config ruling (the carrier seat
  SQLITE_NO_CARRIER = -1; the Stubs.sql seat stays
  throwing for the interp twin); the reopen fall-through
  quirk kept and ledgered; H-015: the five-arg Sql.kt
  unit-202 render keeps its overload (arity-resolved, no
  collision).
- include/mumpsc/cgi.h::<misc> (unit 275; row 255: LOC 78):
  H-014, hit-adapt - CgiH.kt: the header-embedded CGI
  bootstrap block (no C function name; the header-named
  BLOCK seat names the render cgiHMisc): the QS_MAX/
  QS_MAX1 consts, the getenv trio over System.getenv,
  the percent-hex decode with the verbatim 'a'..'z' quirk,
  the dup counter, the goto nxtkey fold.
- libmpscpp.cpp::Jaccard (unit 276; row 256: LOC 58): H-015,
  hit-adapt - Jaccard.kt: the Sim1-template two-walk count
  family, normalized totD + totQ - tot, the pass-by-value
  ledger (H-014 hit-adapt notes folded at the consult,
  local bRefLocal), the regex-free fold of the cross
  products; Stubs.jaccard FLIPS to the doc.Jaccard
  delegate.
- interp-rpm.cpp::perl_pm (unit 277; row 257: LOC 51):
  H-015, hit-adapt - PerlPm.kt: the POSIX regcomp/regexec
  whole-string pattern match under the rpm/perlmatch/
  perlcpp drivers; the Kotlin Regex full-anchored arm, the
  java.util.regex.PatternSyntaxException catch (the
  kotlin.text twin unresolved in kotlinc 2.1.20),
  regcomp ladder fold documented, the REG_* ladder kept
  as the comment fossil, sigint(100) + exitProcess(1) on
  the fatal arm, the i==1 -> 0 return mapping kept;
  Stubs.perlPm FLIPS to the doc.perl_pm delegate (the
  unit-152 driver call sites bind unchanged).

Seat flips and ledger edits this push (Stubs.kt, MumpsMisc.kt,
Btree.kt, BtreeExterns.kt, BtreeH.kt):

- Stubs.parse_() zero-arg -> doc.parse_() delegate (the u160
  tag-correction note retained above the seat; the one-arg
  interp twin untouched; IfCode.kt binds unchanged).
- Stubs.jaccard -> doc.Jaccard; Stubs.perlPm ->
  doc.perl_pm; the 33 mstringPlus/mstringMinus/mstringTimes/
  mstringDiv/mstringRem throwing seats RAISED before the
  Stubs closing brace (batch-10 roster rows).
- MumpsMisc.kt: setAlarm seat MOVED to MpstimeMisc (the
  xindex precedent, noted at the correction site);
  Btree.kt: the btreeX stub REMOVED and tag-corrected;
  BtreeExterns.kt: FoundBlock/Reinsert/FoundOffset;
  BtreeH.kt: BtreeStatic.gblDataFile seat.

Miss rows: none recorded this tranche (0 of 10 consults).

Whole-arm kotlinc gate (kotlinc 2.1.20, sha a118197b...): 260
source files (250 at T2 close plus the 10 T3 renders), silent
pass, EXIT=0, 0 errors / 0 warnings, red-build count 0 across
the tranche end state (the parse_ seat compile folded the
OPEN/EMPTY macros to the _CP consts and the tables were then
mechanically regenerated from the dump). H-015 hygiene scan
clean on all new and edited files (raw dollar escaped as
\u0024, no backticks, no double-bang, ASCII only).

Events: 20 rows appended this tranche (the 10 consults x
reuse + hit pairs; 719 consult rows -> 1436 event rows total
at this push, 1437 CSV lines). The regenerated-CSV byte-
identity verification ran at this push: the python port of
wp2/analysis/R/export_doc_events.R over the journal WITH this
fragment appended reproduced the appended CSV byte-for-byte
(the same port reproduced the at-rest 1417-line CSV first,
709 -> 1416, before the splice).

Next protocol step: DOC batch 9 CLOSE (the close instrument
per the B7-close precedent: the full regen, the both-tranche
review, the batch-9 roster ledger), then batch 10.

DOC consult grammar rows (timestamps identical to the events
CSV append of this push):

- DOC consult: unit compiler-parse.c::parse_, entry H-014, outcome hit-adapt, timestamp 2026-11-22T05:30:00Z
- DOC consult: unit libmpscpp.cpp::<misc-3>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T05:32:00Z
- DOC consult: unit interp-native-btree.cpp::btreeX, entry H-014, outcome hit-adapt, timestamp 2026-11-22T05:33:00Z
- DOC consult: unit compiler-kill.c::kill_code, entry H-014, outcome hit-reuse, timestamp 2026-11-22T05:34:00Z
- DOC consult: unit interp-native-btree.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T05:36:00Z
- DOC consult: unit mpstime.cpp::<misc>, entry H-018, outcome hit-adapt, timestamp 2026-11-22T05:35:00Z
- DOC consult: unit interp-sql.cpp::sql, entry H-018, outcome hit-adapt, timestamp 2026-11-22T05:31:00Z
- DOC consult: unit include/mumpsc/cgi.h::<misc>, entry H-014, outcome hit-adapt, timestamp 2026-11-22T05:37:00Z
- DOC consult: unit libmpscpp.cpp::Jaccard, entry H-015, outcome hit-adapt, timestamp 2026-11-22T05:38:00Z
- DOC consult: unit interp-rpm.cpp::perl_pm, entry H-015, outcome hit-adapt, timestamp 2026-11-22T05:39:00Z