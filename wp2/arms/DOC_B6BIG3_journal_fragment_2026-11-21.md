# DOC B6 big-three journal fragment, 2026-11-21

Units 183-185 of the DOC batch-6 complex stratum, per the dedicated
session PI decision recorded in PILOT_TRACKING.md.

## Consult ledger (logged before applying)

- DOC consult: unit 183, entry H-011, outcome hit-adapt, timestamp 2026-11-21T18:01:00Z
- DOC consult: unit 184, entry H-011, outcome hit-reuse, timestamp 2026-11-21T18:02:00Z
- DOC consult: unit 185, entry H-011, outcome hit-adapt, timestamp 2026-11-21T18:03:00Z

## Unit 183 interp-bifs.cpp::zfcn (physLOC 1622, dump-verified)

- Rendered as ZfcnBifs.kt: `fun zfcnBifs(v1d: CharArray, bd: CharArray, svPtr: Msv)`.
- H-017 collision note: the Makefile compiles interp-zfcns.o, NOT
  interp-bifs.o, so this TU is the build-alternate; unit 184 owns the
  top-level `fun zfcn(...)` and the Stubs.zfcn seat (the analog of the
  zfcns.h `_INTERP_` selection macro).
- H-011 goto ladder (about 75 labels) rendered as the private ZLblB
  enum state machine; znormal and zalter share one arm; zblnk's zbl1
  inline body rides the ZBLNK arm with a ZBL1 slot.
- H-004 statics ride the private ZfcnBifsState object.
- H-018 arms under the SQLITE-live/NATIVE dead build: znative returns
  "0"; zcondense and zrestore render as immediate returns (dead
  build-alternate bodies dropped); znxtrow prints the banner and
  returns.
- OS carriers per precedent: fsetposC/fgetposC throwing, chdirC
  quantized, openC via java.io.File, systemC/popenC live via
  Runtime.exec, srandC stores the seed.
- zdate family: TmZb class + mktimeC Calendar walk; the unit-1 ctime
  seat reused for zd2/zd6.
- New Stubs seats raised: the GTK glue family (owner "GTK C library
  glue, not a corpus unit", all throwing, StrHolderZb/GdkRgbaZb out
  holders public in Stubs), the mpsFloor/mpsCeil/mpsTrunc and
  mpsLog/mpsCos/mpsTan/mpsExp2 seats (batch-9/10 roster owners), and
  Stubs.Replace (builtin.h:40, not a corpus unit);
  Stubs.ServiceInterface c/d params widened to CharArray?.

## Unit 184 interp-zfcns.cpp::zfcn (physLOC 1662, dump-verified)

- Rendered as Zfcn.kt: the LIVE reference-build TU, top-level
  `fun zfcn(v1d: CharArray, bd: CharArray, svPtr: Msv)`.
- Deltas vs 183 (vendor-diff verified): no zreplace arm; the
  commented zglobal-to-zfiletest fossil noted; the SQLITE-only
  ZINDEXSIZE/ZDATASIZE/ZDBNAME/ZDBFILE/ZINDEXMAX arms; the generic sql
  arm uses SQLCMD_ZB 55 not PRAGMA 29; ZGLOBAL is the ServiceInterface
  GBLNME 20 relay.
- H-011 hit-reuse: the ZLblZ machine, ZfcnsState object and TmZz
  class derive from the unit-183 render (private top-level classes
  cannot share names across files in a package; funs can).
- Stubs.zfcn FLIPPED to the doc.zfcn delegate with TAG CORRECTION (the
  old tag said a later batch owns it; the owner is roster unit 184,
  the Makefile reference-build TU).

## Unit 185 interp-interp.cpp::Interpret (physLOC 1581, dump-verified)

- Rendered as Interpret.kt: the ~77-entry private ILblI enum machine
  over the command ladder (RESTART/NEXT_LINE/... through RESTORE),
  with the file-private interp-interp.cpp:71-78 consts
  (GOSUBMAX_I 50, SWAP_I 30, FORMAX_I 30, EXECLEVS_I 40, LBLS_I 80,
  OPEN_PAREN_I 206, CMDSTACK_MAX_I 8192, the MGI codes and the
  LBLS/lbloff IntArray(20) wident kept verbatim with the MDH ledger
  note).
- Build defines: SQLITE live (in-TU define), NATIVE_SHARED and
  TERMINATE_ON_ERROR dead, so abrt is a return 99 and the JOB fork
  quantizes to always-parent with an H-001 note.
- H-012 pointers-into-buffers become sliceAtI scratch plus copyAt
  copy-back (getstr2, TimeOut_getstr1 io cases); the BUILD
  stack-cleaner arm passes the all-null BuildLocal/BuildGlobal calls
  (both Stubs seats widened to CharArray?/Msv? nullable).
- The two vendor strcmp sites (the FOR_END string-limit test and the
  label search) render as a file-private strcmpI C-string compare.
- Stubs.Interpret FLIPPED to the doc.Interpret delegate with TAG
  CORRECTION (the old "not a corpus unit" tag was a MIS-TAG; owner is
  roster row 185, rank 308).
- New roster seats raised: Stubs.inout (interp-parse.cpp::inout, the
  batch-10 roster unit; the compiler-sys1.c::inout unit-59 render is
  the DISTINCT landed twin of the same C body) and Stubs.Merge
  (interp-sysfunc.cpp:650, batch-8 roster unit), both throwing.

## Gate and scan

- Whole-arm kotlinc 2.1.20 gate 0 errors / 0 warnings at 181 files
  (178 + ZfcnBifs.kt + Zfcn.kt + Interpret.kt).
- H-015 scan clean on all touched files: no raw backticks, raw
  dollars, double-bangs, em-dashes or non-ASCII.

## Files pushed

- wp2/arms/DOC/src/main/kotlin/doc/ZfcnBifs.kt (new)
- wp2/arms/DOC/src/main/kotlin/doc/Zfcn.kt (new)
- wp2/arms/DOC/src/main/kotlin/doc/Interpret.kt (new)
- wp2/arms/DOC/src/main/kotlin/doc/Stubs.kt (modified)
- wp2/arms/ARM_WORKING_NOTES.md (ledger block)
- wp2/PILOT_TRACKING.md (rows 183-185 translated, session prose)
- wp2/arms/DOC_B6BIG3_journal_fragment_2026-11-21.md (this fragment)
