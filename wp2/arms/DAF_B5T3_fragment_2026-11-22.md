# DAF B5 T3 journal fragment, 2026-11-22

### DAF B5 tranche 3 (units 144-154, complex stratum) - 2026-11-22

Tranche scope per the committed batch-5 rank order: the T3 complex
stratum (PILOT units 144-154 = units.csv rows 125-135), eleven
units, one Dafny spec (spec/qlength_count.dfy). Sandbox ~/b4
rebuilt earlier this session and carried through from the C-009
registry work: vendor tree sha256 verified (621deb...ffdbe), JDK17
+ kotlinc 2.1.20 (kcd sha a118197b...), Dafny 4.11.0
(4.11.0+fcb2042d, the m1-build match;
DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1 in this sandbox), repo tree
= master @f93409a (the C-009 journal cross-reference spliced; 54 kt
in the DAF arm tree pre-tranche). dump4.py recreated from the
T3-stage with WANT reset to the eleven T3 units; all 11 dumps
verified at exact physical LOC against the vendor tree. Vendor
facts pinned this tranche: SYNMAX 20000 (libmpscpp.h.in:385), Syn
struct/syn[] at libmpscpp.cpp:7788, bmg BUFSIZE 50000/PATSIZE
9000/LARGE, CLOSE 4 (defines.h:87), INVALID_CONVERSION 1 /
GLOBAL_NOT_FOUND 2 / INPUT_LENGTH 10 (libmpscpp.h:70-79), PM_DEBUG
0 (pm.h:25), the parse_count/parse_pattern_element bodies
re-grounded from the vendor tree beyond the dump cut. Arm
independence kept: rendered from pristine vendor sources +
BASE-heuristic-log consultations only; no DOC/PRO Kotlin
consulted.

CHUNK-BOUNDARY DISCLOSURE, LOGGED (BASE row 83, the B4 misc-9
adjudication style): the libmpscpp.cpp::<misc-5> dump opens
mid-body ("else return false; }" completes the preceding chunk's
last NUMERIC operator<= body; the leading numeric <=/>= sets stay
with the preceding chunk's owner) and closes mid-body of
"mstring mcvt (char *)" (the misc-6 batch-7 roster row 187).
Adjudication: the render starts at the first complete body
(operator<=(global)) and the mcvt carry renders as a named-owner
stub (mcvtD) disclosed in KDoc and here. Neither neighboring chunk
is reopened and no earlier render is touched.

Scanner seat releases, logged at close (all four stubs raised in
prior tranches, all four released by this tranche's renders):
(a) qlengthDaf stub DELETED from DafInterpStubsT3.kt (released by
the Qlength.kt render, same signature qlengthDaf(tmp0,tmp2,sv));
(b) synInitD stub DELETED from LibmpscppMisc9.kt (released by the
SynInit.kt render); (c) the RpmParserStubsD seats acceptItD/
acceptD/syntaxErrorD/parsePatternAtomListD DELETED (released by
the RpmMisc.kt render; the ParseCount.kt/ParsePatternElement.kt/
ScanToken.kt callers bind unchanged); (d) the LibmpscppMisc9
docCorrelateD member stub DELETED (released by the global::
DocCorrelate render in LibmpscppMisc5.kt; its free-function relay
is the docCorrelateFreeD named stub). Additionally: the synD owner
TAG CORRECTION stands (grep-verified libmpscpp.cpp:7788, not
selftext.cpp; flipped in the earlier session and carried here);
the sqlDaf owner comment co-flips (rdbms-if b5 released by the
RdbmsSql.kt render); new named stubs raised with owners:
perlPmD (roster row 256 b9, outside the DAF batch), makeCstringD
(bstring-family TU helper, roster kin of unit 22), wstemD
(interp-bifs.cpp wstem family; not a corpus unit), pmD (the
interp-pm.cpp:43 body; not a corpus unit), the gtk C library seat
stubs (external non-corpus owners), zseekD/ztellD (FILE* fpos
seats, JVM Reader divergence), and the gtk-handle folds ride
opaque Ints (row 35).

Per-unit render notes:
- Unit 144 include/mumpsc/sym.h::<misc> (units.csv 125): the chunk
  carries extern declarations only (SY_* prototypes and the rt
  table externs owned by the libmpscpp/interp bodies); rendered as
  the SymHMisc.kt KDoc disposition ledger per the B4
  include-chunk precedent; no Kotlin bodies emitted (model-bag
  empty; the miss is the disposition itself).
- Unit 145 interp-sym.cpp::Qlength (units.csv 126): the counting
  walk over the v1d register with the char 1 / >129 mark predicate
  and the j==0 -> 0 else j-1 shaping; parseD/keyfixD/allocSVDaf
  ride their named-owner stubs; DOCUMENTED DIVERGENCE: the socket
  clone folds onto a field-copy helper (Qlength.kt).
- Unit 146 libmpscpp.cpp::SynInit (units.csv 127): SYNMAX 20000
  rows into the DafSyn singleton; the File reader fold carries a
  FILE* divergence ledger; the exit() failure arm folds onto
  FileNotFoundException; the mid-fill SYNMAX return 1 quirk and
  the compareTo bubble sort kept verbatim (SynInit.kt).
- Unit 147 interp-rpm.cpp::scan_token (units.csv 128): the
  toupper-probe scanner switch; take_char/syntax_error resolve in
  the unit-152 RpmMisc render same-tranche; the digit do-while,
  the quote-span while (vendor: unterminated strings run to the
  terminator) and the EOT-on-NUL kept verbatim (ScanToken.kt).
- Unit 148 interp-bmgsubs.c::bmg_search (units.csv 130): the BMG
  buffer walker: forced-NL tail, the 50000-buffer chunk loop with
  the last-full-line trim and the chunk punt, the delta0
  fast-forward consume, the LARGE rewind, the tail-anchored
  cmap/pattern confirm, the do-while line-start backwalk and the
  gotamatch report; read-past-end derefs ride the at() NUL-guard
  (row 81 divergence ledger); bmg_setup and gotamatch stay
  named-owner stubs; the unused byte_offset local kept
  comment-only; QUIRK KEPT VERBATIM: the -1 NULL-relay early
  return does NOT restore the forced tail (the vendor restores
  only on the normal exit) (BmgSearch.kt).
- Unit 149 include/mumpsc/mstringClass.h::<misc> (units.csv 129):
  the class ctors and operator bodies whose live call sites sit in
  the libmpscpp misch chunks; the Justify branch is constant-zero
  dead on the reference build (neutralized; ledger); rendered as
  the MstringClassHMisc.kt KDoc disposition ledger.
- Unit 150 mumps.cpp::main (units.csv 131): the argv syllabus
  (program name, the percent-symbols, %0/%c), the program-mode
  "s %="""^prog""" g @%%" boot with the "./" prefix strip, the
  announce banner with the build-macro lattice pinned (database
  empty; floatSize double; Fpbits 72; __TIME__/__DATE__ disclosed
  as compile-macro placeholders), and the REPL loop with the
  EOF/halt/? exits and the GlobalClose/pid-cleanLocks shutdown;
  DOCUMENTED DIVERGENCE: readline/read_history/write_history map
  onto kotlin.io readLine plus a mumps_history file analog; the
  program-name strncpy rides a LOCAL 512-char carrier (the DafMsv
  seat lattice pins no program field; disclosed); CLOSE_D=4
  pinned in-file (MumpsMainD.kt).
- Unit 151 include/mumpsc/compiler-globals-ext.h::<misc>
  (units.csv 132): the extern lattice carried by DafCompilerState
  TU seats and the struct nmes/txt/cvars fold ledger; one prior
  comment block rewritten for accuracy (the inout/add/numcomp
  neutralization) and re-verified against the vendor; rendered as
  the CompilerGlobalsExtMisc.kt KDoc disposition ledger.
- Unit 152 interp-rpm.cpp::<misc> (units.csv 133): the rpm
  pattern harness: the scanner/parser carriers (new_scanner/scan/
  take_char/new_parser/parse), the parse_pattern / atom / atom_
  list rules with the vendor's element-then-count concatenation
  order kept, accept/accept_it, the syntax_error caret pad with
  sigint(100), rpm() with the raw-regex hack path, the perlmatch
  "1"/"0" string relays, and perlcpp; PM_DEBUG pin 0 with the
  debug print constant-false; parse_count (unit 136) and
  parse_pattern_element (unit 113) bind as the B5-T2/B4-T2
  renders; the PCRE dead defines (DOUBLE_QUOTE/MAX_BACKREFS/
  OFFSET_BUF_LEN/min) live in the KDoc ledger only (RpmMisc.kt).
- Unit 153 rdbms-if.c::sql (units.csv 134): the reference build
  pins SQL/POSTGRES undefined so the live body is the return-0
  shape; the ODBC/PG branches stay KDoc-ledgered not emitted
  (RdbmsSql.kt).
- Unit 154 libmpscpp.cpp::<misc-5> (units.csv 135): the wrapper-
  library residue: the operator<= (global/mstring) bodies, the
  full operator==/operator!= sets (atol/atof + String collapse),
  thrownMessage (the vendor "detetected" typo kept),
  the __finish flg latch with the GlobalClose+exit fold, the
  global::Int/Double/Mstring/Char read members (the operator>>
  numeric fold disclosed), Stem/TokenInit/Token/Dump/Restore with
  the coded z-call splits kept (TokenInit reading svPtr->bd),
  the mdh_* GTK wrapper family over named external seat stubs
  with the %p handle fold, Getenv -> System.getenv, the ZSeek/
  Ztell fpos stubs, the Perl passthroughs (tpx-only per vendor),
  the global::DocCorrelate member pair (seat release), pt,
  global::Count with its tmp dual-role XNEXT walk, global::
  Transpose + the two free overloads over the node seats, the
  stem trio (by-reference mutation folded onto the returned
  value, disclosed), Hash/lhash with the 4-byte little-endian ULong
  accumulation and the %lu toString divergence, the indirect
  operator* minimal-carrier fold, the GlobalSet/GlobalGet overload
  sets over the String seats with the vendor stray-backslash token
  line kept comment-only, the pmw relays onto pmD, and the mcvt
  chunk-carry stub (LibmpscppMisc5.kt).

Dafny ledger: exemption carried for all eleven units (the
call-shaping/service-relay exemption family of T1/T2; the three
include-chunk dispositions and the ifdef-web/external-relay
clusters with the same reasons in the KDocs). Spec rendered and
verified (in-sandbox dafny 4.11.0): spec/qlength_count.dfy (7
detected, 0 errors, 0 warnings, exit 0): the Qlength (unit 145)
counting kernel over a byte-code domain
model (char-code literals are outside this Dafny grammar; the model
disclosed in the KDoc with the unsigned-char semantics kept
exact), CountMarked recursion + the NULSuffix terminator lemma + the
QlengthWalk method whose loop invariant pins the suffix
consumption ledger j + CountMarked(a[i..]) == CountMarked(a[..]);
the j==0/else j-1 output shaping sits outside the kernel.
Full suite in-sandbox after qlength_count: 43 pre-tranche + 7 =
50 detected, 0 errors, 0 warnings.

Gates: whole-arm kotlinc 2.1.20 silent PASS at 65 files (54
pre-tranche + 11 new; the three stub-file edits in-place), run
twice: once over the render gate copy and once over a fresh
repo-copy + new-files submission (both SILENT EXIT=0, 0 warnings).
H-015 scan clean on all touched files: one stray-brace repair and
one backticked stdin access caught and fixed pre-push (the
backticked System.in replaced with kotlin.io readLine); all dollar
occurrences escaped in strings and comments per the row-36 rule;
trailing newlines verified byte-wise. In-sandbox dafny 4.11.0
verify green; gradle + dafny re-run at tranche close on m1-build
(Mark).

Renders and consult ledger (each consult logged before applying):
- DAF consult: unit sym.h::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-22T15:15:00Z
- DAF consult: unit sym.h::<misc>, entry BASE-log:90, outcome hit-adapt, timestamp 2026-11-22T15:15:30Z
- DAF consult: unit interp-sym.cpp::Qlength, entry BASE-log:8, outcome hit-adapt, timestamp 2026-11-22T15:16:00Z
- DAF consult: unit interp-sym.cpp::Qlength, entry BASE-log:81, outcome hit-reuse, timestamp 2026-11-22T15:16:30Z
- DAF consult: unit interp-sym.cpp::Qlength, entry BASE-log:111, outcome hit-reuse, timestamp 2026-11-22T15:17:00Z
- DAF consult: unit interp-sym.cpp::Qlength, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-22T15:17:30Z
- DAF consult: unit libmpscpp.cpp::SynInit, entry BASE-log:18, outcome hit-adapt, timestamp 2026-11-22T15:18:00Z
- DAF consult: unit libmpscpp.cpp::SynInit, entry BASE-log:22, outcome hit-reuse, timestamp 2026-11-22T15:18:30Z
- DAF consult: unit libmpscpp.cpp::SynInit, entry BASE-log:60, outcome hit-adapt, timestamp 2026-11-22T15:19:00Z
- DAF consult: unit libmpscpp.cpp::SynInit, entry BASE-log:46, outcome hit-adapt, timestamp 2026-11-22T15:19:30Z
- DAF consult: unit libmpscpp.cpp::SynInit, entry BASE-log:28, outcome hit-adapt, timestamp 2026-11-22T15:20:00Z
- DAF consult: unit interp-rpm.cpp::scan_token, entry BASE-log:25, outcome hit-reuse, timestamp 2026-11-22T15:20:30Z
- DAF consult: unit interp-rpm.cpp::scan_token, entry BASE-log:37, outcome hit-reuse, timestamp 2026-11-22T15:21:00Z
- DAF consult: unit interp-rpm.cpp::scan_token, entry BASE-log:111, outcome hit-reuse, timestamp 2026-11-22T15:21:30Z
- DAF consult: unit interp-rpm.cpp::scan_token, entry BASE-log:90, outcome hit-adapt, timestamp 2026-11-22T15:22:00Z
- DAF consult: unit interp-bmgsubs.c::bmg_search, entry BASE-log:6, outcome hit-reuse, timestamp 2026-11-22T15:22:30Z
- DAF consult: unit interp-bmgsubs.c::bmg_search, entry BASE-log:59, outcome hit-adapt, timestamp 2026-11-22T15:23:00Z
- DAF consult: unit interp-bmgsubs.c::bmg_search, entry BASE-log:81, outcome hit-adapt, timestamp 2026-11-22T15:23:30Z
- DAF consult: unit interp-bmgsubs.c::bmg_search, entry BASE-log:56, outcome hit-adapt, timestamp 2026-11-22T15:24:00Z
- DAF consult: unit interp-bmgsubs.c::bmg_search, entry BASE-log:8, outcome hit-adapt, timestamp 2026-11-22T15:24:30Z
- DAF consult: unit interp-bmgsubs.c::bmg_search, entry BASE-log:37, outcome hit-reuse, timestamp 2026-11-22T15:25:00Z
- DAF consult: unit mstringClass.h::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-22T15:25:30Z
- DAF consult: unit mstringClass.h::<misc>, entry BASE-log:90, outcome hit-adapt, timestamp 2026-11-22T15:26:00Z
- DAF consult: unit mstringClass.h::<misc>, entry BASE-log:--, outcome miss, timestamp 2026-11-22T15:26:30Z
- DAF consult: unit mumps.cpp::main, entry BASE-log:53, outcome hit-adapt, timestamp 2026-11-22T15:27:00Z
- DAF consult: unit mumps.cpp::main, entry BASE-log:46, outcome hit-adapt, timestamp 2026-11-22T15:27:30Z
- DAF consult: unit mumps.cpp::main, entry BASE-log:60, outcome hit-adapt, timestamp 2026-11-22T15:28:00Z
- DAF consult: unit mumps.cpp::main, entry BASE-log:33, outcome hit-reuse, timestamp 2026-11-22T15:28:30Z
- DAF consult: unit mumps.cpp::main, entry BASE-log:18, outcome hit-adapt, timestamp 2026-11-22T15:29:00Z
- DAF consult: unit mumps.cpp::main, entry BASE-log:37, outcome hit-reuse, timestamp 2026-11-22T15:29:30Z
- DAF consult: unit mumps.cpp::main, entry BASE-log:--, outcome miss, timestamp 2026-11-22T15:30:00Z
- DAF consult: unit compiler-globals-ext.h::<misc>, entry BASE-log:6, outcome hit-reuse, timestamp 2026-11-22T15:30:30Z
- DAF consult: unit compiler-globals-ext.h::<misc>, entry BASE-log:68, outcome hit-adapt, timestamp 2026-11-22T15:31:00Z
- DAF consult: unit compiler-globals-ext.h::<misc>, entry BASE-log:--, outcome miss, timestamp 2026-11-22T15:31:30Z
- DAF consult: unit interp-rpm.cpp::<misc>, entry BASE-log:68, outcome hit-adapt, timestamp 2026-11-22T15:32:00Z
- DAF consult: unit interp-rpm.cpp::<misc>, entry BASE-log:90, outcome hit-adapt, timestamp 2026-11-22T15:32:30Z
- DAF consult: unit interp-rpm.cpp::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-22T15:33:00Z
- DAF consult: unit interp-rpm.cpp::<misc>, entry BASE-log:59, outcome hit-adapt, timestamp 2026-11-22T15:33:30Z
- DAF consult: unit interp-rpm.cpp::<misc>, entry BASE-log:83, outcome hit-adapt, timestamp 2026-11-22T15:34:00Z
- DAF consult: unit interp-rpm.cpp::<misc>, entry BASE-log:46, outcome hit-reuse, timestamp 2026-11-22T15:34:30Z
- DAF consult: unit rdbms-if.c::sql, entry BASE-log:46, outcome hit-adapt, timestamp 2026-11-22T15:35:00Z
- DAF consult: unit rdbms-if.c::sql, entry BASE-log:85, outcome hit-reuse, timestamp 2026-11-22T15:35:30Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:98, outcome hit-reuse, timestamp 2026-11-22T15:36:00Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:99, outcome hit-reuse, timestamp 2026-11-22T15:36:30Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:83, outcome hit-adapt, timestamp 2026-11-22T15:37:00Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:35, outcome hit-adapt, timestamp 2026-11-22T15:37:30Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:60, outcome hit-adapt, timestamp 2026-11-22T15:38:00Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:56, outcome hit-adapt, timestamp 2026-11-22T15:38:30Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:111, outcome hit-reuse, timestamp 2026-11-22T15:39:00Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:106, outcome hit-reuse, timestamp 2026-11-22T15:39:30Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:90, outcome hit-reuse, timestamp 2026-11-22T15:40:00Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:80, outcome hit-reuse, timestamp 2026-11-22T15:40:30Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:92, outcome hit-reuse, timestamp 2026-11-22T15:41:00Z
- DAF consult: unit spec/qlength_count.dfy (unit 145 Qlength kernel), entry BASE-log:--, outcome miss, timestamp 2026-11-22T15:41:30Z

Consult totals for the tranche: 54 consults, 20 hit-reuse, 30
hit-adapt, 4 miss. The four misses are the three model-bag-empty
include-chunk dispositions (sym.h and mstringClass.h carried
hit-adapt owner ledgers; the misses log the model-bag-empty
adjudication itself and the build-macro lattice pin of main) and
the spec entry (the miss-then-render pattern: qlength_count.dfy
rendered and verified 7/0/0). Coverage: all eleven complex units
144-154 rendered or disposition-ledgered per the include-chunk
precedent; four stub seats released; the chunk-boundary cuts
disclosed above.

DAF arm position after this tranche: batch 5 complete (T1-T3:
units 124-154 rendered; the misch/complex strata closed); the
whole-arm tree now 65 kt files (54 pre-tranche + 11). NEXT per
the committed order: BOTH B5 T2 (core 134-143), then DAF B6
(units 155-185).

Files pushed (one commit):
- 11 new renders under wp2/arms/DAF/src/main/kotlin/daf/:
  SymHMisc.kt (3425 bytes), Qlength.kt (5307), SynInit.kt (5600),
  ScanToken.kt (3761), BmgSearch.kt (7386), MstringClassHMisc.kt
  (4190), MumpsMainD.kt (10902), CompilerGlobalsExtMisc.kt (5349),
  RpmMisc.kt (9415), RdbmsSql.kt (2640), LibmpscppMisc5.kt
  (29024) (all gate-verified twice; H-015 scanned)
- 3 edited: DafInterpStubsT3.kt (12514; qlengthDaf release +
  sqlDaf co-owner flip), LibmpscppMisc9.kt (16141; synInitD +
  docCorrelateD releases + synD tag carry), RpmParserStubsD.kt
  (3112; four scanner seats released)
- 1 new spec under wp2/arms/DAF/spec/: qlength_count.dfy (2521
  bytes, 7 verified 0/0, exit 0; full suite 50 verified
  in-sandbox)
- wp2/logs/DAF_heuristic_log.csv (rows 134-147 appended; ids
  contiguous 1-147; BASE-log hits carry source=BASE-log +
  reused_from row ids, dispositions/gates manual-decision)
- wp2/PILOT_TRACKING.md (batch-5 table DAF column rows 144-154
  todo -> translated; no header or other-row change; diff is
  exactly those 11 cells; the row-154 missing trailing pipe
  preserved as-is)
- wp2/arms/DAF_B5T3_fragment_2026-11-22.md (this fragment)
