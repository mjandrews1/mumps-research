# BOTH B5 T3 journal fragment, 2026-11-22

### BOTH batch 5 tranche 3 (units 144-154, the complex stratum) - 2026-11-22

Tranche scope per the committed batch-5 rank order: the T3 complex
stratum (PILOT units 144-154, eleven units), the batch-closing
tranche of BOTH B5. With this push BOTH batch 5 is complete
(31/31 translated). Sandbox notes: vendor tree pristine-verified
per the rebuild recipe; JDK17 + kotlinc 2.1.20; Dafny 4.11.0
(invariant-globalization env var); dump4.py recreated from the
repo tool with all eleven T3 dumps LOC-verified before render.
Arm independence kept: pristine vendor + rules_v0.pl (18 rules
h001-h018) + the arm's own seat carriers only; no DOC/PRO/DAF
Kotlin consulted. The fresh repo copy at tranche open showed the
T2 splice already in master and the DAF B6 close complete with
the queue-note repair spliced (journal base 8947 lines at this
fragment's push).

Per-unit render notes:

- Unit 144 include/mumpsc/sym.h::<misc> (rank 215): KDoc-only
  residue ledger (the _INTERP_ two-armed keyfix comment guard is
  the only function-shaped residue; no bodies in the chunk).
  struct_carrier miss recurrence (the sym.h stab chain family);
  h018 reused.
- Unit 145 interp-sym.cpp::Qlength (rank 216): real body over
  the pushed AllocSV carrier (shallow-fold note: the memcpy seat
  shares the donor arrays; xd/v1d written before read), the
  keyfix.h static inline rendered as keyfixV1d, parse_ stub
  (roster 303 b3), walk count over the coded-marker bytes,
  j==0 arm writes "0".
- Unit 146 libmpscpp.cpp::SynInit (rank 219): real body; the
  fgets 12000-cap char walk seats on FileReader (read-error
  quantizes to EOF, h001); the line[i-1]=0 unconditional strip
  kept with the empty-line guard note; Syn + SynTable carriers
  for the TU globals; bubble sort by strcmp; SYNMAX 20000
  (libmpscpp.h:385) seated in BothStubs; the overflow arm
  returns 1 exactly as the vendor does.
- Unit 147 interp-rpm.cpp::scan Token (rank 232): real body;
  takeChar/syntaxError/Scanner ride the unit-152 render; h012
  unsigned-byte domain on the punct classes; renamed
  scanToken per h017.
- Unit 148 interp-bmgsubs.c::bmg_search (rank 233): real body
  with the delta tables, the BmgData carrier (delta0/cmap/
  pattern/patlen/nsuccess, the struct_carrier miss family
  seat), the unsigned-byte cmap index quirk quantized (vendor
  UB), the k-jump landing at strend kept, gotamatch TODO stub
  (roster 180 b1), consts BMG_BUFSIZE 50000 / PATSIZE 9000 /
  LARGE / NL; two H-015 backtick fixes caught at the pre-push
  scan (comment quotes).
- Unit 149 include/mumpsc/mstringClass.h::<misc> (rank 234):
  KDoc-only residue ledger of the class layout (the operator
  families itemized; no bodies in the chunk). The class
  member carrier miss is filed for the THIRD time (T1 + T2 +
  this tranche; capture candidate at batch close).
- Unit 150 mumps.cpp::main (rank 250): real body. The h018
  lattice resolved at the banner exactly like the DAF T2
  finding (SQLITE defined at interp-globalb.cpp:45 is the
  reference build's DBMS; NATIVE/BIGFLOAT/CLIENT arms stay
  comment-ledgered). argv percent plumbing via __symput
  (non-unit helper stub), the dispatch line tokens kept
  verbatim, readline seats on the stdin Reader (FileDescriptor
  reflection for the keyword-shaped member in), readHistory
  quantized no-op, cleanLocks stub (non-unit), the goodbye tail
  folded once, GlobalClose via the new CLOSE 4 (defines.h:87)
  const, MUMPS_BUILD_TIME/DATE kept token-shaped.
- Unit 151 include/mumpsc/compiler-globals-ext.h::<misc> (rank
  265): the h004 singleton with the verified header pins; the
  Nmes/Txt/Dcs class carriers; CVARMAX 100 / PS1 4097 / FBMAX
  4000; the G-suffixed collision renames (bdG, tpxG, t2G, xpxG,
  t0pxG, xdG, tmp2G, tmp3G, vdG, v1dG) journaled at the seats.
- Unit 152 interp-rpm.cpp::<misc> (rank 271): Scanner/Parser
  family - newScanner/scan/newParser delegates; this render
  LANDS the T2 acceptIt seat: the ParseCount.kt stub flips to
  the real p.currentToken = scan(p.s) delegate (h014 seat
  protocol; the flip rides this push as a targeted edit);
  parse/parsePattern/parsePatternAtom/parsePatternAtomList/
  accept/syntaxError plus rpm/perlmatch/perlcpp real; stubbed:
  perl_pm (roster 214 b9), parse_pattern_element (roster 196
  b4), leaning_toothpickize (roster 157 b6 after the DAF tag
  correction), make_cstring (roster 212 b1);
  RPM_DOUBLE_QUOTE kept as the U+00D2 seat.
- Unit 153 rdbms-if.c::sql (rank 276): h018 deciding - the
  reference build defines NEITHER SQL NOR POSTGRES (rdbms-if.c
  is not in the reference Makefile object set; the CFLAGS
  define neither), so the LIVE arm is the #else return 0; both
  excluded arms (the ODBC section and the PostgreSQL section,
  with their byte-1-coded MumpsArray row stores) stay as the
  comment ledger in the render. 209 LOC of preprocessor
  surface quantized to the honest contract.
- Unit 154 libmpscpp.cpp::<misc-5> (rank 295): the residual
  C++-binding chunk. Real bodies: the chunk-complete mstring
  comparison tails (fold family mstringLessEqual/mstringEquals/
  mstringNotEquals with receivers passed explicitly; char*/
  const char*/string fold onto the String arm), thrownMessage
  (INVALID_CONVERSION 1, libmpscpp.h:70, new seat), __finish
  (the GlobalClose fold over the new CLOSE const), the global
  member extract family (Int/Double/Mstring/Char over the new
  extractValue seat), Stem/TokenInit/Token (TokenInit keeps
  the vendor quirk of assigning the result from svPtr.bd, not
  the zfcn output), mdh_dialog_new_with_buttons (the sprintf
  percent-p pointer seat quantized to a String handle; the
  direct gtk_* wrappers seat as BothStubs platform stubs),
  Dump/Restore x3 (the zcd/zcl CE/CF envelopes), Getenv,
  ZSeek/Ztell x2 (FILE* seats on RandomAccessFile: the fpos_t
  pair maps onto getFilePointer/seek - ARM-LOCAL DIVERGENCE
  from the DAF big-three IntArray handle domain, journaled as
  an arm decision; the sscanf-fail UB quantizes to offset 0),
  Perl x2 (bodies commented in vendor; the live tpx return
  kept), DocCorrelate x2 over the string-arm member stub, pt
  (the UNINITIALIZED vendor index quantized to 0, journal
  note), global::Count REAL (the XNEXT order walk under the
  receiver subscript family; the trailing-two-sub-32-char strip
  kept), the global::Transpose member LANDS (the T2 transpose
  stub in BothStubs flips to the real loop body over the new
  order/kill/valuation carrier seats), the two Transpose
  wrappers (shared walk, mstring arm + char* arm), stem x3 and
  Hash/lhash (the 4-byte little-endian chunk walk, ULong
  domain; the vendor static char result folds fresh per h008;
  the past-NUL chunk reads see the zeroed buffer), the indirect
  operator* seat (Indirect carrier; named indirectOf - h015
  grammar_collision: times is reserved for repetition),
  GlobalSet x7 (fold to 4 overloads; the stray line-splice
  backslash in the vendor body journaled; no quote escaping,
  vendor-identical), GlobalGet x3 (fold to 2; the static r
  h008-fresh), pmw x5 (fold to 4; pm non-unit stub by
  provenance, libmpscpp.h:237), and the mcvt CHUNK-BOUNDARY
  FRAGMENT (the visible half renders; the continuation lands
  with misc-6, roster 294 b7; the vendor returns i, not the
  tmp it just built - kept).

Seat ledger of this push (new seats unless noted): SYNMAX 20000
(const); allocSV (roster 285 b8); parse_ (roster 303 b3); wstem
(roster 164 b10); pm (libmpscpp.h:237, non-unit); Indirect
carrier (struct indirect, globalClass.h); GlobalRef.extractValue
and GlobalRef.assignValue (globalClass.h layout roster 259,
pending); GlobalNode order/kill/value/setValue + the
one/two-subscript node-invoke seats (same layout owner);
docCorrelate string-arm member seat; the GTK platform stub
family (17 mdh_* seats); CLOSE 4 and INVALID_CONVERSION 1
(BothCRuntime); the Msv carrier extension (xd stateVector.h:45,
t0px :55, xpx :56, t2 :67, program :100; collision-checked
against the T1/T2 field set). FLIPS: acceptIt (ParseCount.kt,
roster 271 landed), GlobalNode.transpose (BothStubs, landed by
unit 154). PIN CORRECTION: the T1 BothStubs header cites the
global-class layout owner as roster 89; units.csv pins
include/mumpsc/globalClass.h::<misc> at rank 259 batch 3. The
new seats cite 259 and this correction is journaled here; the
stale T1 comment text stays untouched for history.

Consult ledger (find_rule before render; events CSV rows
appended at the 2026-11-22T19:05:00-19:05:52Z band, 53 rows:
34 reuse, 16 hit, 3 miss):
- Unit 144: h018 reuse, h017 hit, struct_carrier miss
  (timestamps 19:05:00/01/02Z)
- Unit 145: h004/h007/h009/h012/h014 reuse, h017 hit
  (19:05:03-08Z)
- Unit 146: h001/h004/h007 reuse, h013/h017 hit (19:05:09-13Z)
- Unit 147: h007/h012/h014 reuse, h017 hit (19:05:14-17Z)
- Unit 148: h004/h007/h012/h014 reuse, h013/h017 hit
  (19:05:18-23Z)
- Unit 149: h017 hit, class_member_carrier miss (19:05:24/25Z)
- Unit 150: h001/h004/h009/h011/h014/h018 reuse, h013/h017
  hit (19:05:26-33Z)
- Unit 151: h001/h004 reuse, h017 hit (19:05:34-36Z)
- Unit 152: h007/h014/h015/h017 reuse, h013 hit (19:05:37-41Z)
- Unit 153: h018 reuse, h017 hit, h007 reuse (19:05:42-44Z)
- Unit 154: h007/h012/h013/h014 reuse, h008/h015/h017 hit,
  class_member_carrier miss (19:05:45-52Z)

Miss families at this close: struct_carrier (recurrence: the
sym.h stab chain T2 seat now shared by the bmg carrier;
capture candidate at batch close) and class_member_carrier
(third recurrence: mstring/global members ride explicit
receiver state; capture candidate at batch close).

Gates: whole-arm kotlinc gate SILENT EXIT=0 at 32 files, run
both over the gate copy and over the fresh repo-copy submission
(the same file set; two invocations, both silent, exit 0, zero
output bytes); dafny suite 30 verified 0 errors UNCHANGED
(spec/expx.dfy 4, spec/length_count.dfy 5, spec/mps_log10.dfy 5,
spec/parse_count.dfy 2, spec/smoke_test.dfy 2, spec/strmanip.dfy
12; per-spec loop, never the glob - the Mark-side qlength_count
single-file glob failure of 2026-11-22 was the in-shell
mistake, not a repo event); every T3 unit EXEMPT, no new spec.
H-015 hygiene: all new/edited files ASCII-only, no backticks
(two BmgSearch comment-quote fixes caught pre-push), no tabs,
escaped dollars only (the zcd/zcl/zwi/zwp envelope strings),
trailing newlines at EOF. red-build count for the tranche: 0
red gate builds (the four pre-gate compile fixes - the SynInit
Long/Int loop boundary, the seat edits, the BmgSearch hygiene
pass, the Sql rule-name citation fix - were caught by the
in-sandbox pre-gate compile and are logged as data, per the
designed catching point).

Files pushed (byte counts, ASCII bytes; one commit):
  wp2/arms/BOTH/src/main/kotlin/both/SymHMisc.kt 5497
  wp2/arms/BOTH/src/main/kotlin/both/Qlength.kt 4713
  wp2/arms/BOTH/src/main/kotlin/both/SynInit.kt 5990
  wp2/arms/BOTH/src/main/kotlin/both/ScanToken.kt 3596
  wp2/arms/BOTH/src/main/kotlin/both/BmgSearch.kt 6939
  wp2/arms/BOTH/src/main/kotlin/both/MstringClassMisc.kt 4975
  wp2/arms/BOTH/src/main/kotlin/both/MumpsMain.kt 10972
  wp2/arms/BOTH/src/main/kotlin/both/CompilerGlobalsExtMisc.kt 8097
  wp2/arms/BOTH/src/main/kotlin/both/RpmMisc.kt 11062
  wp2/arms/BOTH/src/main/kotlin/both/Sql.kt 3102
  wp2/arms/BOTH/src/main/kotlin/both/LibmpscppMisc5.kt 24633
  (11 new renders, all gate-verified)
  wp2/arms/BOTH/src/main/kotlin/both/BothStubs.kt 13613 (edit:
  the T3 seat section, the transpose member flip, the GTK
  family, the pin-correction note)
  wp2/arms/BOTH/src/main/kotlin/both/BothCRuntime.kt 10369
  (edit: the Msv extension fields, CLOSE, INVALID_CONVERSION)
  wp2/arms/BOTH/src/main/kotlin/both/ParseCount.kt 5430 (edit:
  the Parser scanner field + the acceptIt stub flip)
  wp2/logs/BOTH_knowledge_events.csv 13272 (53 rows appended to
  the 157-row base: header + 210 data rows)
  wp2/PILOT_TRACKING.md 48592 chars (BOTH column rows 144-154
  todo -> translated, 11 cell edits; the batch-5 BOTH close
  paragraph appended; 499 lines)
  wp2/arms/BOTH_B5T3_fragment_2026-11-22.md (this file)

Open items:
- the splice of this fragment into wp2/arms/ARM_WORKING_NOTES.md
  (journal base 8947 lines + this fragment's 241 lines;
  expected wc -l 9188), then removal of the fragment file in
  the standing two-commit house pattern;
- the gradle gate round on the spliced tree (m1-build and the
  MacBook: cd wp2/arms/BOTH then gradle build; the sandbox
  here runs kotlinc only) - the earlier BOTH B5 gates are
  journaled at their tranche sections and B2/B3 show the
  m1-build first-pull pattern;
- the BOTH B5 BATCH-CLOSE protocol follows this splice per the
  committed order: coverage checklist, the C-008 events export
  regeneration (157 + 53 = 210 data rows), PILOT close prose,
  and the two miss-family capture decisions;
- the mcvt continuation rides libmpscpp.cpp::<misc-6> (roster
  294, batch 7, the next batch the BOTH arm draws);
- the named-owner seats raised against roster cells outside
  the arm's current drawing stay as the honest UNRESOLVED stub
  record per the H-014 discipline.
