# DAF B7 tranche 1 (units 186-195, simple stratum) - 2026-11-23

DAF batch 7 opened per the committed row order (PILOT units 186-215,
batch_plan.md order within the strata; T1 simple 186-195, T2 core
196-205, T3 complex 206-215). Sandbox rebuilt from the recipe (repo
zip = master 9dce2ff; vendor sha 621deb...ffdbe verified; JDK17
corretto 17.0.21.1; kotlinc 2.1.20 sha a118197b; Dafny
4.11.0+fcb2042d; baseline gate 96 kt SILENT EXIT=0 with only the
accepted Interpret.kt vendor-lattice warning set; dafny baseline
70 verified 0 errors). dump4.py recreated from the recipe; all ten
T1 dumps loc-asserted against batch_plan (GlobalData disambiguated
to the mstring overload at libmpscpp.cpp:2616, physical loc 17 -
the unit-70 char* and batch-8 string rows are the family siblings;
same adjudication the DOC unit-192 KDoc records).

## Units rendered (10 new kt files, package daf)

- 186 libmpscpp.cpp::global::Find (16 LOC, GlobalFindB7.kt): the
  C-string-key overload twin of the batch-6 unit-156 body; the
  key-retrieve ABSENCE is the twin's distinguishing feature (only
  the ref is retrieved; the raw key rides its own carrier to the
  findDaf seat). Consult 18:30:00Z: rows 92/54/37/33/28 hit-reuse.
- 187 compiler-zcommands.c::<misc> (CompilerZcommandsMisc.kt): GPL
  banner + TU include set residue, the unit-95 shape; no body, no
  wrappers raised. Consult 18:30:30Z: row 37 hit-reuse.
- 188 libmpscpp.cpp::global::Piece (17 LOC, GlobalPiece.kt): the
  row-97 piece-service read-path call (setpiece 0) over the live
  unit-114 pieceDaf kernel - the first read-path call in this arm;
  the cc1[65]/cc2[64] digit carriers fold onto Locale.US digits; the
  jj<0 -> ii rebinding kept before the carriages. Consult 18:31:00Z:
  rows 97/92/54/33/8 hit-reuse.
- 189 compiler-sys1.c::substr (17 LOC, CompilerSubstr.kt): compilerSubstrD
  - the short-domain sys1 twin (255 clamp in BOTH arms; no realloc
  arm); out-seat kept (row-30 fold not taken - the Match.kt call
  sites read the NUL tail onward). Consult 18:31:30Z: rows 8/27/30
  hit-adapt.
- 190 interp-strmanip.cpp::substr (19 LOC, StrmanipSubstr.kt): the
  substrDaf SEAT RELEASE (the B6 T3 Match.kt raise): stub body
  deleted with release note; the real body here. The vendor realloc
  arm (len-start<<1 precedence) is a UB seat quantized onto the
  carrier bound (the note rides the KDoc ledger); the two compiler-side
  Match.kt call sites REBIND to compilerSubstrD per the DOC unit-59
  inout precedent (match binds its TU-local twin). Consult
  18:32:00Z: rows 8/27/30 hit-adapt.
- 191 libmpscpp.cpp::mstring::Translate (15 LOC, MstringTranslate.kt):
  the row-101 mstring fold (receiver/args -> String params); the
  unit-131 GlobalTranslate envelope precedent with the SECOND
  separator byte (three-arg envelope) kept verbatim. Consult
  18:32:30Z: rows 101/30/97/98/54 hit-reuse.
- 192 libmpscpp.cpp::GlobalData (17 LOC, GlobalData.kt): the mstring
  needle at :2616; the xecute-then-read tail (xecuteDaf GlobalQS
  seat FIRST in-arm caller; SymGetD; atoi; tpx outcome kept); the
  command build folds onto String +=. Consult 18:33:00Z: rows
  92/97/98/28/33/54 hit-reuse.
- 193 libmpscpp.cpp::global::Soundex (17 LOC, GlobalSoundex.kt): the
  no-arg service envelope (no separator bytes between receiver and
  close) on the row-101 carriage family; dafFcn relay. Consult
  18:33:30Z: rows 92/101/30/54 hit-reuse.
- 194 interp-sym.cpp::_justify (26 LOC, InterpJustify.kt): the -1
  sentinel left-pad arm (lpadDaf seat, the T2 flip; in-place alias
  documented alias-safe at the seat) and the numeric format arm:
  the "%c%d%c%dlf" dialog kept with ONE quantized seat (the C 'l'
  length modifier drops to the JVM 'f' conversion - semantic
  equivalence ledgered); out-seat kept; strcpy oddities via
  copyIntoCarrier. Consult 18:34:00Z: rows 14/15/26/27/30/28
  hit-adapt.
- 195 interp-sym.cpp::mkpt (16 LOC, InterpMkpt.kt): the row-56/112
  static-return-buffer fold to a fresh per-call String (no caller
  aliases the static as a write target; divergence ledgered); the
  substitution ladder kept in vendor order (each specific byte is
  below 32: dash write first, re-substitution overwrites); the >128
  unwritten-byte UB seat quantized (omitted from the fresh carrier).
  Consult 18:34:30Z: rows 56/27 hit-adapt.

## Seat ledger

- substrDaf: RELEASED (unit-190 body at StrmanipSubstr.kt; the
  Match.kt stub deleted with release note).
- compilerSubstrD: RAISED (the sys1 twin; Match.kt call sites
  rebound off the old seat).
- xecuteDaf: first in-arm caller (seat unchanged, GlobalQS.kt).
- findDaf/mltoaDaf/pieceDaf/dafFcn/sigintDaf: called unchanged.
- TAG-CORRECTION candidates for the T3 renders (NOT yet applied):
  SymHMisc.kt:38 says the service family is "not a roster unit" -
  WRONG: the ServiceInterface roster row sits in THIS batch (unit
  215; vendor interp-service.cpp:87); StrmanipMisc.kt:30-32 says
  sigint is a "batch 10" row - WRONG: roster unit 211 of THIS
  batch. Both corrections ride the T3 sigint/serviceInterface
  renders.

## Gates

kotlinc whole-arm SILENT EXIT=0 at 106 files (gate copy; the fresh
repo-copy submission rides this push; only the accepted Interpret.kt
always-true/false vendor-lattice warning set appears). Dafny suite
runs per-spec (the glob MERGES specs - known gotcha): 70 verified
0 errors UNCHANGED (all ten T1 units EXEMPT or residue; no new
spec). H-015 hygiene: comment-dollar normalizations in GlobalData.kt
and InterpJustify.kt fixed pre-push; ASCII-only verified; trailing
newlines verified. Consult ladder fresh band 2026-11-23T18:30:00Z
to 18:34:30Z, 30s steps, no collisions with the spent 2026-11-22
bands.

## Files pushed (14)

- wp2/arms/DAF/src/main/kotlin/daf/GlobalFindB7.kt (new)
- CompilerZcommandsMisc.kt (new)
- GlobalPiece.kt (new)
- CompilerSubstr.kt (new)
- StrmanipSubstr.kt (new)
- MstringTranslate.kt (new)
- GlobalData.kt (new)
- GlobalSoundex.kt (new)
- InterpJustify.kt (new)
- InterpMkpt.kt (new)
- Match.kt (edit: substrDaf release note + compiler-side rebind)
- wp2/logs/DAF_heuristic_log.csv (ids 189-200 appended; base prefix
  byte-verified; the T2-era seats/gate row field-shape convention
  left as-is per the B6 close note)
- wp2/PILOT_TRACKING.md (batch-7 rows 186-195 DAF column marked
  translated; the DAF B7 tranche-1 note paragraph before the table)

## Next

Mark's splice: cat this fragment >> the arm journal (wp2/arms/
ARM_WORKING_NOTES.md; expect 10284 + N lines), rm in a separate
commit; then the fresh repo-copy gradle + dafny (70) gate round.
Then DAF B7 T2 (core 196-205: global::Fnumber, externs.h residue,
interp-sysfunc::GlobalData, global::MakeGlobal, interp-fcn::<misc>,
Multiply, rslt_callback, compiler-parse::Extract, Sim1, embed1),
then T3 (complex 206-215 incl. the sigint/ServiceInterface seat
releases with the two tag corrections above).
