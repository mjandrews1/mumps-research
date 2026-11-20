# DOC B7 tranche 3 journal fragment, 2026-11-20

Units 206-215 (complex stratum) of batch 7, per the committed row
order. The consult ledger rows live in the ARM_WORKING_NOTES
journal section of this same date (not duplicated here per the
C-005/C-008 one-consult rule and the B6 close splice-repair
lesson).

## Renders

- 206 compiler-mumps.c::<misc> (vendor residue): the includes
  ledger chunk plus EP(flg, ExecName, pid) and LevCheck; EP body
  emits the if-goto label routing; VENDOR QUIRK kept verbatim:
  the epi() call and MAIN_flg=0 are UNCONDITIONAL (outside the
  if(flg) closing brace).
- 207 compiler-fcninit.c::fcninit (vendor :20): the emitted-C
  prologue generator; local Sysout/SysoutSkip helpers; call
  sites bind fcninit(CompilerState.output_file,
  CompilerState.pid) - the output_file seat is String (was
  CharArray) matching the CompilerState carrier, pid Long.
- 208 interp-sym.cpp::Qsub (vendor :145): the Qlength-twin
  template (unit-145 precedent), copyMsvQsub adapter, gpad
  saved/restored through parse_, tmp0 in-place NUL fill; the
  var m definite-assignment seat ledgered.
- 209 interp-native-btree.cpp::freadx (vendor :55): ByteArray
  return contract flipping the Stubs.freadx seat; MODIFIED const
  (vendor :55; UNMODIFIED is BtreeExterns); FreadxState (static
  indx) + BtreeBlockCache (bfr[GBLBUF] lazy alloc at the
  gblbfx=-1 sentinel); gblraf() backstop over the new BtreeH
  RandomAccessFile gblFile seat (null sentinel = gblfd -1).
- 210 compiler-zcommands.c::zcommands_code (vendor :244): the
  z-command router; PARSE_ERROR_ZC/ABRTX_ZC/NEXT_LINE_ZC private
  consts; banner strings written POST-fprintf-conversion (vendor
  percent-escapes reconverted, all ledgered); dollars
  backslash-escaped per H-015; fcninit/parse_ zero-arg/EP/linepad
  seats bound.
- 211 interp-sysfunc.cpp::sigint (SYM.h:41): the signal relay;
  the private Linux x86-64 const ladder (SIGHUP_SI=1 through
  SIGSYS_SI=31; inert #ifndef fallback ladder ledgered H-018;
  duplicate SIGKILL guard dead, SIGABRT fossil comment kept);
  CLOSE_SI write_history no-op per unit-150; exitProcess(1) Fcn
  precedent; AllocSV NULL arm unreachable ledger.
- 212 compiler-sys1.c::match (vendor :26): the FIVE-arg compiler
  twin resolving Stubs.match; body is the vendor copy-paste of
  the unit-180 Match.kt walk with ONE semantic difference (bd =
  the CompilerState extern); per-file punctSetCM/strcmpCM/
  sliceTailCM dups; the six-arg interp twin already landed,
  Kotlin overload by arity.
- 213 compiler-epi.c::epi (vendor :640): the emitted epilogue
  and label/text table walks (TxtEntry), FOR hfile via
  java.io.FileWriter, global resets; EPI_RULE banner 81 '=';
  var p1 + non-null first binding avoids the double-bang.
- 214 libmpscpp.cpp::<misc-6> (vendor chunk, 600 LOC roster /
  740 dump lines): orphan-head/orphan-tail chunk ledgers; mcvt x4
  + cvt x4 (static-buffer divergence), count (RESOLVES the
  Stubs.count seat seated by tranche 2), HitRatio (integer-
  division quirk), global::Name, __test, the ReadLine family
  (FILE*/istream fold onto the Reader domain, H-001/H-016), the
  mstring conversion-operator ladder (H-010 to* fold: atoi/
  atof domains), zBlanks x2 (implicit mstring(-1) ctor quirk),
  Ascii x4 (signed-char domain note), Length x5, Horolog (the
  Fcn zhorolog intrinsic math inlined, _horolog unowned),
  global::Merge (128-quirk tail walk), Lock/UnLock/CleanLocks/
  CleanAllLocks (Mltoa macro seat, NULL timeout ""), the Set/Get
  family (the stale class-static tmp quirk ledgered), Kill,
  Order/C_Order/Order_Next/Order_Prior, Data, the SQL relay
  family (sql/c trailing-space quirks kept; SQL_Table hit-reuse
  of the unit-34 render), the operator& forwards as the amp
  extension family over the new Stubs.Concat(x7) seats; the
  ostream operator<< has no Kotlin seat (ledger).
- 215 interp-service.cpp::ServiceInterface (vendor :81): the
  20-token service entry; early-return GETENV/SCANALNUM/SCAN/
  INPUT arms, the AllocSV state-vector push with the copyMsvSI
  adapter (the vendor memcpy repetition kept), the verbatim-
  repeated parse-and-keyfix prologue per arm (tmpA/tmpB from the
  v1d offset-1 tail), the MSV-intrinsic wrapper battery and
  Mglobal GBLNME walk; INVDF keeps the strlen+1 second-NUL
  quirk; the nullable tmp4/tmp5 seats raise on the arms that
  dereference them; the new Stubs.IDF seat (FREE wrapper owner,
  batch-10); the Stubs.ServiceInterface seat flips RESOLVED.

## Gate and scan

- Whole-arm kotlinc 2.1.20 gate 0 errors / 0 warnings at 206
  files (196 + 10 new; 4 edits: Stubs.kt, BtreeH.kt,
  CompilerCarriers.kt, CompilerGlobalsExtMisc.kt); compile-round
  repairs in-session: the makeGlobal expression-body flip seat,
  the Qsub definite-assignment seat, the Epi non-null first
  binding, the ServiceInterface nullable tmp4/tmp5 backstops,
  and the misc-6 stdin carrier reseat ( STDIN_READER reuse).
- H-015 scan clean on all touched files (backticks reworded,
  raw dollars escaped, no double-bangs, no non-ASCII).

## Files pushed

- 10 new: Qsub.kt, Fcninit.kt, Freadx.kt, Epi.kt,
  CompilerMatch.kt, CompilerMumpsMisc.kt, ZcommandsCode.kt,
  Sigint.kt, ServiceInterface.kt, LibmpscppMisc6.kt (all under
  wp2/arms/DOC/src/main/kotlin/doc per the arm layout)
- 4 edited: Stubs.kt, BtreeH.kt, CompilerCarriers.kt,
  CompilerGlobalsExtMisc.kt
- wp2/PILOT_TRACKING.md (batch-7 section, rows 206-215 translated)
- wp2/arms/ARM_WORKING_NOTES.md (tranche section + consult rows)
- wp2/logs/DOC_knowledge_events.csv (regenerated per C-005, +20
  event rows; the pre-tranche CSV verified as byte-exact prefix)
- this fragment
