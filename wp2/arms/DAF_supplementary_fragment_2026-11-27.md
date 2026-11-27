# DAF supplementary render session (2026-11-27)

Render arm of the five-arm translation study (Mark Andrews, PI solo).
This session discharges the 2026-11-26 PI adjudication of the B10
flags: the four named-stub roster rows ruled RENDER BEFORE PROCEEDING
(B9-repair precedent: PI-approved deviation, journaled, separate
commute). Four units rendered, gate green, one push. DAF is now
study-complete: 308/308 rows carry real renders.

## Units rendered

- compiler-if.c::if_code (units.csv record 40, grep line 41 - the
  ledger's r41 pin cites the grep line; off-by-one disclosed, ids
  not renumbered): CompilerIfCode.kt. The IF command compiler arm:
  the no-argument tpx guard + IF_flg bump, the postconditional
  atTest/atCopyDaf/INTERPRET arm (emitInterpretDaf, unit-209 pin),
  the if_again parse loop (parseCDaf, the compiler TU parse_ seat,
  row-277 PI-ruled rebind precedent; the older SetCode/ForCode
  parseDaf(sv) folds stay as previously adjudicated), the numcomp2
  Extract2 emit (extract2Daf String side, row 30) with the " ))"
  close-paren quirk, and the if_structure ForBlocks/Blocks tagging
  with the ',' re-entry. atTest -1 -> ABRTX_D per this vendor cell
  (main's ':' site checks 99 per its own cell - both kept verbatim).
- compiler-new.c::new_code (record 65, r65 pin): CompilerNewCode.kt.
  The NEW command compiler arm: the no-argument overflow-guard
  cell, the specific '(' nmes push loop (new2: setCodeDaf with the
  six-code return ladder, the vd quote-wrap vs the atmp keyfix
  cell, the malloc/strmove emit pair with the vendor
  pointer-plus-one strlen text verbatim, the ')' close and the
  _doStackN ExecX cell), and the inclusive NEW1 loop (new3) with
  the exportflag SymMarkCopyFlag vs sym_(100) emit pair, the
  unreachable exports: label comment-folded, and the trailing
  skip walk. SEAT CONSOLIDATION: the shared exportflag global now
  rides the public exportFlagD in this file; main's private
  exportFlagMD carrier is rebound (the ELSE-X write site) and the
  old declaration comment-ledgered.
- compiler-copyright-notice.c::copyright_notice (record 47, r47
  pin): CompilerCopyrightNotice.kt. The generated-file banner cell:
  every Sysout/fprintf(Out) emit onto the C.out writer (rows 2/26,
  CompilerFcninit pin); ctime via the ServerMisc logtime precedent
  (SimpleDateFormat EEE MMM dd HH:mm:ss yyyy, Locale.US) with the
  ctime trailing newline KEPT (logtime strips it; this cell does
  not); the year list verbatim.
- compiler-abrtx.c::abrtx (record 14): CompilerAbtx.kt. The
  compiler exit cell: the Zmain/Zfunction warning arm, the
  epiDaf/epDaf epilogue pair with the vendor empty else block, and
  the close/conversion cell the B10 driver header deferred to this
  seat: the fclose(Out) + output_file read-back rides the C.out
  emit-builder fold (the .m physical file never exists on this arm;
  fgets 1024-chunk semantics kept verbatim), the INCLUDE splice
  into the .cpp Final (offset-8 filename cell; the INCLUDE line
  itself never reaches Final; the include file is removed after the
  splice), the double fclose, and the vendor's mangled remove-cell
  arithmetic kept verbatim (removes base+".c.m", nothing; the
  intended base+".m" never existed on this arm - inert either way).
  abort() renders as the epi precedent fold (print + exitProcess
  134).

## Seat ledger

- RELEASES: ifCodeDaf / newCodeDaf / abrtxDaf / copyrightNoticeDaf
  (the four B10 raised named stubs; release notes at the
  DafInterpStubsT3 stub block; main's call sites bind unchanged;
  main's historical PERMANENT-raise header note is superseded by
  the release notes, kept as history).
- CONSOLIDATION: exportflag -> public exportFlagD
  (CompilerNewCode.kt), main's exportFlagMD rebound.
- BINDINGS: if_code's parse_ -> parseCDaf (row-277 PI-ruled rebind
  precedent); tpx = 1 rides the parseCDaf svPtr.tpx sv-carrier fold
  (disclosed there).

## Gates

Whole-arm kotlinc gate exit 0 at 207 kt files (6 accepted
Interpret.kt vendor-lattice warnings only; zero new diagnostics on
the 4 rendered + 2 edited files). Dafny suite 11 specs / 70
verified / 0 errors unchanged (all four units EXEMPT, no new
spec). H-015 hygiene clean (ASCII only, no tabs, no backticks, no
raw comment dollars, trailing newlines). Heuristic log rows
324-329 appended (4 unit rows + seats row + gate row; the
pre-existing malformed 134-146 family untouched per the PI
ruling). PILOT_TRACKING supplementary paragraph appended after
the B10 close prose (the early-batch heterogeneous tables carry no
per-row DAF cells for these rows, so no table flips; the
asymmetry-with-DOC/PRO note is discharged).

## Arm state and next steps

DAF arm: 308/308 rendered; 304 prior + 4 supplementary; nothing
DAF-side pending except the C-001 cycle-10 check backfill and
grading (study-wide). Next protocol step: BOTH batch 8 (units
216-246) per the committed row order.
