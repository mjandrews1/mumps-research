

### DOC B5 tranche 3 (units 144-154), 2026-11-20

Tranche 3 = the batch-5 complex stratum, eleven units, the batch
close. All rendered from the pristine vendor tarball (sha256
re-verified this session; exact physical-LOC matches against
units.csv for every unit via the study's own chunker-faithful
extractor). Whole-arm kotlinc 2.1.20 local gate: 155 files,
0 errors, 0 warnings (the compile gate caught and fixed the
mid-render slips before the push: the Getstr2Stdin symbol miss
in the u150 main render - the Getstr2.kt STDIN_READER lazy is
the live edge; the u150 announce banner emitted literal
template-escapes instead of the placeholder constants - rebuilt
as plain concatenation). Mark's m1-build gradle gate ran GREEN
(BUILD SUCCESSFUL, 19s) at this close.

Per-unit ledger (vendor line anchors from the pristine source):
- u144 include/mumpsc/sym.h::<misc> (39 physLOC): SymHeaderMisc.kt
  - declaration ledger only: the stab struct binds the unit-28
  Stab carrier (H-004 single owner); sym_/Qsub/Qlength/GlobalQuery/
  GlobalName/fullUnpad/ErrorMessage/SysDump/_SymFree/BuildGlobal
  each name its landed render or tagged owner seat; the twin
  _INTERP_/_else keyfix comment pair resolves EMPTY per H-018
  (both arms' lines are the same commented-out text); two NEW
  H-014 seats raised with the unit: Stubs.strsep (libc split
  helper; char** holder renders the Array<String> carrier,
  H-006 idiom) and Stubs.ServiceInterface (the interp-side
  service entry); BuildLocal stays the H-013 commented-fossil
  with the live compiler-globals-ext.h declaration owning the
  existing seat; Qlength names THIS tranche's u145 render.
- u145 interp-sym.cpp::Qlength (vendor 1007, 18 loc): Qlength.kt
  - the Qlength body over private copyMsvQ (the unit-119 adapter
  duplicate with the dup-def ledger noter; copyMsv in
  GlobalQuery.kt is private and stays private); keyfix via the
  offset-slice idiom at the vendor 9-key walk; CODE 4
  SETINT/SET query shape kept; sprintf percent-d via
  String.format Locale.US (H-009); Stubs.Qlength flips to
  this delegate (Fcn.kt call sites bind unchanged).
- u146 libmpscpp.cpp::SynInit (~750, 58 loc): SynInit.kt - the
  synonym-table loader; the C strtok(line," \t\n") walk renders
  the explicit strtokSpaceFirst/Next helpers over the IntArray
  holder (H-001 FILE* to BufferedReader; H-006 out-param
  carrier); fopen-NULL arm carries the IllegalStateException
  edge (SysDump u142 precedent); the vendor stores the
  FOLLOWING token in .word and the first in .alias - the vendor
  quirk kept verbatim with the ledger; Stubs.synInit flips to
  this delegate.
- u147 interp-rpm.cpp::scan_token (vendor 300-358, 39 loc):
  ScanToken.kt - the Lockner scanner boxed as fun
  scan_token(s: scanner): token_type over the u104/e113
  carriers (String input + Int currentChar walk; NUL
  quantization at the end-of-index exactly as the C \0
  sentinel); STRING/PATTERN_CODE/INT/COMMA/DOT/LPAREN/RPAREN/
  ERROR/EOT arms per the vendor switch; take_char binds the
  NEW Stubs.take_char seat (resolved by u152 same tranche);
  syntax_error calls ride the seat flipped this tranche.
- u148 interp-bmgsubs.c::bmg_search (vendor ~253, 60 loc):
  BmgSearch.kt - the Boyer-Moore-Gosper driver over the u17
  gotamatch render family; BmgFsearchStatic carries delta0/
  cmap/pattern/patlen; BmgStatic.nsuccess the success count;
  the gotamatch -1 NULL sentinel contract per u17; the
  want-reader FILE* loop with clamped-index guards where the
  vendor reads one element past the buffer (H-001 UB guard
  ledger); actions bind ((CharArray,Int)->Int)?.
- u149 include/mumpsc/mstringClass.h::<misc> (331 physLOC):
  MstringClassMisc.kt - declaration-heavy header ledger; the
  ctors/conversion syntax note over the Carriers Mstring;
  s_str() folds onto the c_str witness with the seat noter;
  EDIT-ONLY Carriers.kt addition: Mstring.name private seat
  (mstringClass.h field; H-004 single-owner note; visible
  to the member renders that own the private contract).
- u150 mumps.cpp::main (vendor 58-183, 125 loc):
  MumpsMainUnit150.kt - the REPL entry rendered as main150
  (the unit-12 MumpsMisc.kt residue owns the file head; the
  one-main-per-package rule); argv percent-family staging via
  the u120 SymPut renders over CstringOps carriers (percent
  percent-d loop, percent-c count, percent-0 pair); the ./
  prefix strip in place; the three-quote escape run command
  built verbatim then the H-014 Interpret seat; announce
  banner rides the __TIME__/__DATE__ placeholder tokens
  (registered divergence: no JVM analog of the compile
  stamps) plus the H-018 define ledger in full (SQLITE live,
  NATIVE/NATIVE_SHARED/BIGFLOAT arms documented per the
  reference config); GlobalClose expands inline to
  Mglobal(CLOSE,...) both EOF arms (libmpscpp.h macro shape);
  readline loop over the Getstr2.kt STDIN_READER
  (H-001 divergence ledger; history persistence no-op, the
  ?-relaunch backward goto rendered as the flag re-entry
  loop, H-011); pid via ProcessHandle.current().pid();
  cleanLocks the u56 render; the halt set strcasecmp via
  String.lowercase; the dead static struct C block dropped
  per H-013.
- u151 include/mumpsc/compiler-globals-ext.h::<misc> (185-219,
  144 physLOC): EDIT-ONLY CompilerCarriers.kt additions plus
  CompilerGlobalsExtMisc.kt. The extern roster closed on
  CompilerState with per-line anchors: setpiece/iorg/symlen/
  ctrlc/gpad/Jflg/FunctionName/LabelsStart/TextStart/p1/p3/
  cvars/cvarx/Final/MaxTmp/IF_flg_max/pd1cur/i/j/k/l/cmdx/
  ExecLevel/ExecStack/DoRet/DoRetX/CX/EmitLabel/JOB_flg/
  SubFlag/SymIncrFlg/TryFlg/CatchFlg/opnfile/in (in_ per
  H-017)/dosave/cur_line/icmnd/p2px/ernbr/tmp2/tmp3/pl/p2/
  except/flg; the idem partners keep their first owners
  (ierr1 at InterpParseMisc, ErrChar/nstart on Msv, output_
  file the String seat); the struct renders Nmes/TxtEntry/
  Dcs land in CompilerGlobalsExtMisc.kt; CVARMAX 100; the
  :82-104 prototype roster ledgered (landed: xindex, errmod_,
  readf_, inout, numcomp, getstr1, doty, LocalArray,
  GlobalArray, atTest, extract2; owner-tagged not-yet-landed:
  cvar Stubs seat, Extract1, add per the arith.h lattice,
  linepad, linepad2, fcninit, epi, EP, LevCheck, atCopy; H-013
  fossils grep-verified: ScanPastPostCond and pindex have NO
  defining body in the vendor tree); the cod206-210 mnemonic
  block stays the comment ledger.
- u152 interp-rpm.cpp::<misc> (118-242, 145 loc): InterpRpmMisc.kt
  - the driver tier REAL BODIES: rpm (the quote-tilt direct-perl
  arm keeps the trailing-quote strip hack; PM_DEBUG 0 file-
  private const of pm.h:25, dead printf arm kept as comment),
  perlmatch (the "1"/"0" literal fold), perlcpp, new_scanner,
  scan, take_char, new_parser, parse (EOT check), parse_pattern,
  parse_pattern_atom (the vendor element-then-count bstrcat
  argument order kept), parse_pattern_atom_list, accept,
  accept_it, syntax_error (the percent-star-c caret banner as
  the pos-1 space walk; sigint(100) terminator). H-015: the C
  whole-token assignments copy through copyTokenInto (the val
  Bstring carrier precludes reassignment); the String scanner
  quantizes end-of-index as NUL at take_char. SEAT FLIPS:
  rpm/accept/accept_it/syntax_error/parse_pattern_atom_list
  become delegates; perlPm NEW seat (owner interp-rpm.cpp::
  perl_pm, batch-9 roster row 257); bstrcat/
  leaning_toothpickize keep their batch-9/-6 owner seats;
  parse_count/parse_pattern_element stay the landed u143/u104
  renders (H-004 keep-first).
- u153 rdbms-if.c::sql (vendor 81-387, 209 loc): Sql.kt - the
  H-018 empty arm: the reference config defines neither SQL
  (WindowsXX ODBC section) nor POSTGRES (libpq-fe.h section;
  the ALLOW_POSTGRES autoconfigure gates resolve dead) - the
  LIVE body is the vendor's own else-arm return 0 with the
  vendor's comment kept; both dead arms stay documentary
  ledgers (the wlock.h wlock precedent). H-015 same-lexeme
  note: the Stubs.sql 6-int-arg seat is the btree.h SQL-side
  dispatcher, a DIFFERENT C symbol - both keep their seats
  (overload-family rule).
- u154 libmpscpp.cpp::<misc-5> (600 physLOC): LibmpscppMisc5.kt
  - the comparison ladders tail (le(global)/le(mstring), the
  == and != ladders rendered as eq/ne word-operator extension
  families per the SymOpsMisc lt/gt precedent; the const
  char*/char*/string twins fold onto the String seat - ledger:
  27 vendor overloads render 16 Kotlin overloads; the opening
  else-return-false CHUNK-EDGE FRAGMENT documented, not
  rendered - the tail of mstring::operator<=(string), owner
  misc-4 row 283 batch 10); thrownMessage (vendor "detetected"
  typo kept) over the Globals.GlobalExceptionCode carrier +
  file-private INPUT_LENGTH idem const; __finish with the
  per-file flg carrier and exitProcess; global::Int/Double/
  Mstring/Char over the unit-31 conversion seats (n-max
  strncpy clamp shape kept); Stem over the NEW Stubs.wstem
  seat (owner libmpscpp.cpp::wstem - not a roster unit);
  TokenInit/Token (the coded ce/cf bytes built
  programmatically, H-015; the z-command leads ride the
  unicode escape); the mdh GTK wrapper tier (dialog-new-
  with-buttons the zfcn emitter with the percent-p
  identity-hex render at the offset-1 domain; ~17 pass-
  through wrappers over per-file GtkPassThrough stub seats
  with the GTK-library owners named - the
  gtkTreeStoreAppend seat precedent; GdkRgba per-file quad;
  statics into per-file carriers, H-008); Dump/Restore folds
  (mstring + String seats; the zcd/zcl bytes escaped);
  Getenv real (System.getenv, H-001; in_ keyword render);
  ZSeek/Ztell x4 over the RandomAccessFile carrier (H-001
  FILE* divergence: fsetpos/fgetpos map to seek/filePointer,
  the fpos_t void* payload is the 64-bit offset domain);
  Perl x2 (the commented perlcpp call kept verbatim; both
  bodies return svPtr->tpx); global::DocCorrelate char*/mstring
  twins delegate into the Stubs.globalDocCorrelate seat which
  KEEPS its batch-10 row-288 owner - front-run overlap
  ledgered, seat NOT flipped to avoid the delegation circle
  (the twins call the string-twin working body); pt (the
  UNINITIALIZED int i vendor UB quantized to 0, ledger);
  global::Count REAL BODY (the XNEXT sibling walk, Min-family
  idiom; H-010: the Double seat cast at the return;
  Stubs.globalCount flips); global::Transpose member REAL
  BODY (B().Kill() via the globalKill seat; the Order/
  read/store seats; Stubs.globalRefTranspose flips) plus the
  char* and mstring free wrappers kept CharArray/Mstring so
  the unit-100 String overload keeps its H-015 overload-
  family slot; stem x3 string/mstring/char* (the string&
  in-place fold lands on the return - divergence ledgered;
  the static tmp carriers per-file); Hash/lhash REAL 4-byte
  sums (ULong wraparound kept, the sign-bit-free domain
  documented; percent-lu via unsigned decimal toString;
  Stubs.lhash flips to the real body); indirectStar (the
  indirect star operator render; the static indirect carrier
  per-file; H-014 name adapt - Kotlin has no star operator);
  GlobalSet/GlobalGet over the shared xecute bridge ("s " +
  name + "=quote value quote"; the vendor stray backslash
  line-splice noted once as the H-013 fossil; tpx set/reset
  contract kept; the seven/five overloads fold onto the
  Mstring/String pairs; GlobalGet string-twin's conditional
  shape folded onto the char* carrier with the unreachable
  empty-store kept in shape); pmw x5 folds (String pair +
  Mstring pair) over the NEW Stubs.pm seat (owner
  interp-pm.cpp::pm - not a roster unit); the chunk-end
  "mstring tmp;" front edge of mcvt documented, owned by
  misc-6 (batch 7).

Sub-push boundary: one tranche; the ferry push lands as TWO
commits (code+tracking first, then this journal) because the
transport size limit rejects a single code-plus-journal payload -
same tranche boundary, no scope change (the tranche-2
precedent). New files: SymHeaderMisc, Qlength, SynInit,
ScanToken, BmgSearch, MstringClassMisc, MumpsMainUnit150,
CompilerGlobalsExtMisc, InterpRpmMisc, Sql, LibmpscppMisc5
(eleven). Edits: Carriers.kt (Mstring.name + Msv.program
seats), CompilerCarriers.kt (the ext-header extern roster
close), Stubs.kt (+strsep +ServiceInterface +take_char +perlPm
+pm +wstem seats; flips: Qlength, synInit, rpm, accept,
accept_it, syntax_error, parse_pattern_atom_list, lhash,
globalCount, globalRefTranspose; the globalDocCorrelate comment
front-run note). PILOT_TRACKING.md: batch-5 table rows 144-154
DOC=translated; tranche-3 paragraph added; tally: batch 5 is
31/31 translated.

Pre-push scan (H-015 gate) on every new/changed file: no
backticks, no double-bang, no raw dollar outside carrier
escapes (the scan caught and fixed five in-comment raw
dollars and six backtick quotings before the push),
comment-closes end their lines, no for-loop shadowing (the new
files walk with while). Cross-file top-level name scan: the new
top-levels are overload- or arity-distinct from existing
package top-levels (le/eq/ne join the lt/gt family;
Transpose(CharArray,CharArray)/(Mstring,Mstring) stay
distinct from the unit-100 String pair and the u137 MSV
wrapper; scan/parse/accept take_char rpm perlmatch perlcpp are
new lexemes; Hash(char*) joins the u31 Hash class by usage
kind); the whole-arm compile is the decisive check and passed
0/0 at 155 files.

TOOLING INCIDENT (logged per the BASE lesson genre): the
tranche-3 journal push (commit 8fb5e6d) TRUNCATED
ARM_WORKING_NOTES.md - the agent-side file reader returned a
partial read of the 2934-line journal and the push shipped the
truncated head, dropping every section after the unit-68
entry (all DOC sections). The gradle gate ran green
regardless (the .kt payloads were pushed in commit b8cde8d
and verified additive). REPAIR: this fragment re-carries the
full tranche-3 section; the restoration script (run by the
PI on m1-build) resets the journal file to its complete
pre-tranche-3 blob from git history (git show
b8cde8da:wp2/arms/ARM_WORKING_NOTES.md) and appends this
fragment, then removes the fragment file in the same repair
commit. LESSON captured for the ferry: ANY pushed blob over
the file-reader limit must be chunk-read with its
truncation flag checked and the pushed result md5-verified
against the source blob (the unit-31 CSV clobber incident's
sibling failure mode; the T2 "never re-read via many paged
chunks" note applies to replay cost, not to truncation
safety - chunk-and-verify, do not single-read-and-hope).

Consult rows (tranche-wide timestamp 2026-11-20T01:44:51Z; the
eleven unit ids of rows 144-154 are distinct roster ids, a
single stamp holds the C-008 duplicate-key rule):
- DOC consult: unit include/mumpsc/sym.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/sym.h::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/sym.h::<misc>, entry H-014, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/sym.h::<misc>, entry H-018, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-sym.cpp::Qlength, entry H-006, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-sym.cpp::Qlength, entry H-009, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-sym.cpp::Qlength, entry H-014, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-sym.cpp::Qlength, entry H-017, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::SynInit, entry H-001, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::SynInit, entry H-006, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::SynInit, entry H-014, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::SynInit, entry H-017, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-rpm.cpp::scan_token, entry H-014, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-rpm.cpp::scan_token, entry H-015, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-rpm.cpp::scan_token, entry H-017, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-bmgsubs.c::bmg_search, entry H-001, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-bmgsubs.c::bmg_search, entry H-014, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-bmgsubs.c::bmg_search, entry H-017, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/mstringClass.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/mstringClass.h::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-001, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-003, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-009, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-011, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-013, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-014, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-015, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-017, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-018, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/compiler-globals-ext.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/compiler-globals-ext.h::<misc>, entry H-008, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/compiler-globals-ext.h::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/compiler-globals-ext.h::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/compiler-globals-ext.h::<misc>, entry H-015, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/compiler-globals-ext.h::<misc>, entry H-017, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-rpm.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-rpm.cpp::<misc>, entry H-014, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-rpm.cpp::<misc>, entry H-015, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-rpm.cpp::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit rdbms-if.c::sql, entry H-004, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit rdbms-if.c::sql, entry H-015, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit rdbms-if.c::sql, entry H-018, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-001, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-004, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-007, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-008, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-009, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-010, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-012, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-013, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-014, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-015, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-017, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z

Tranche totals: 53 consults, 40 hit-reuse / 13 hit-adapt (u144
the two new H-014 seats raised, u145 the copyMsvQ duplicate-
def + keyfix offset-slice adaptation, u146 the strtok/
fopen-NULL carries, u147 the take_char seat raise resolved
same-tranche, u150 the stdin-reader and compile-stamp diverges,
u151 the in_ keyword render, u152 the perlPm seat raise +
five stub flips, u154 the GTK pass-through seat lattice, the
DocCorrelate front-run and the indirectStar/op-fold
adaptations), 0 miss. Hit rate 100.0%, reuse rate 75.5% (the
complex stratum carries a heavier adaptation load: the GTK
lattice, the FILE* seats and the new-seat raises of this
tranche). TRANCHE 3 (units 144-154, 11 units) COMPLETE - the
complex stratum and the BATCH 5 CLOSE: sym.h misc, Qlength,
SynInit, scan_token, bmg_search, mstringClass misc, the mumps
REPL main, compiler-globals-ext misc, the rpm driver tier,
the sql trampoline, and the misc-5 library tail. Batch-5
tracking table now shows 31/31 DOC translated across the
three 2026-11-20 tranches; the batch-5 close review (gradle
gate, coverage checklist, events export at batch close) is
the next protocol step.
