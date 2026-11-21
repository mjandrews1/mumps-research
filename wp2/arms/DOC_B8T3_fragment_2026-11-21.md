# DOC arm journal - 2026-11-21 (batch 8, tranche 3)

DOC batch 8 tranche 3: the complex stratum, 10 roster rows, DOC
units 237-246 (the units.csv FILE LINES are 217 btree down to 226
Merge; score order 316.3 down to 30.1). All bodies derived from the
pristine vendor source this session (vendor tarball sha256
621deb...ffdbe); every vendor anchor physically verified
line-for-line before rendering. Consult ledger (one row per unit
per C-008; the events CSV append is in this same push, timestamps
03:11 to 03:20Z, one-minute steps, prefix verified byte-identical
to the landed file before the append):

- interp-sql.cpp::btree (unit 237; LOC 443, score 316.3, rank
  293): H-018, hit-adapt - InterpSqlBtree.kt: the SQLITE-side
  global-array workhorse. DISTINCT-TWIN NOTE: the landed unit-4
  native render owns the vendor btree lexeme with the IDENTICAL
  Kotlin signature, so this render takes the Patrn1Interp/
  PatrnSys1 domain-suffix disambiguation: btreeSql (H-016 name
  adaptation; the native twin not regraded). Bodies: the OPEN
  connection head (sql seat, the "mumps" data source, the vendor
  tmp1 double-site alias), the SQLCMD relay, the RETRIEVE select
  builder, the STORE delete-then-insert double walk (the insert
  pointer-walk p3 cursor, dcheck on bd, the block-static bdx[256]
  holder object), the ORDERNEXT/ORDERPREV order-by arm (the
  tokens==1 pointer-offset 12-shift vendor quirk kept, the return-0
  BEFORE printf/sigint dead-rest ledger), the XNEXT query arm, the
  GKILL arm (the local tokens shadow that kills its own tokens==1
  branch, H-013 kept), GBLNME, CLOSE. H-018 exclusion ladders: the
  XNEXT "#if 14==N" block - the reference tree is configured 14,
  the live arm is 14==14 and the twelve compiled-out arms stay
  comment ledgers (the a8 skip in the live order-by tail kept).
  The XNEXT keybak read is vendor UB (uninitialized) - H-013
  ledger, the effective statement is the rsltSpec copy (the
  T2 rsltSpec holder read). strtok over the SOH delimiter renders
  as the non-destructive index walker (skip-empty/leading
  collapse per C strtok; tokens handed to lcheck/embed as
  NUL-terminated CharArray copies, H-012).
- libmpscpp.cpp::<misc-7> (unit 238; LOC 600, score 288.4, rank
  292): H-014, hit-reuse - LibmpscppMisc7.kt: the Concat(x7) member
  families of both carriers (the double arm rendered WHOLE here -
  its signature is at the misc-6 dump tail cut, the chunk-edge
  whole-render precedent; the global.global arm stays the landed
  unit-65 render, not regraded), the mstring intrinsic-command
  members (Soundex via the zfcn seat - the LEADING BLANK before
  the fcn name is the vendor's own, unlike the unit-193 global
  member; the ONE-ARG Translate on the fcn v1d/bd offset-1
  convention; Reverse x2 with the &gtmp[9] offset-9 copy; Qsubscript
  x2 with the mstring data vs global Sref arms; Qlength x2 and
  Query x2 through the FixGblRef route), the global conversion
  operator ladder as the to* family (each arm the Stubs.globalGet*
  typed extraction seat, H-010), the operator*> scalar-star family
  (member times extensions + the globalTimes friend family, the
  globalPlus precedent; the vendor ulong/ushort mix-domain computes
  ledgered inline), the gt/lt ladders (char*/string twins folded
  onto the String seat per H-015; the vendor > ladder has NO
  mstring member while the < ladder does - the asymmetry kept,
  ledger), and the operator<<(global) write-assign family (the
  shl seat, set_global/get_global seats, the tpx zero check with
  GLOBAL_NOT_FOUND throw, the "%ud" sprintf VENDOR QUIRK kept
  byte-verbatim, "%d"/"%hd" Locale.US). SEAT FLIPS: the twelve
  Stubs.Concat seats flip to these renders with TAG CORRECTION
  (the "batch-10 owner" tags of the unit-214 seats were forward-
  tags; the member bodies ARE this row - the u119 tag-correction
  precedent); the amp operator& forwards of LibmpscppMisc6 route
  through them unchanged. NEW SEAT RAISED: Stubs.fixGblRef (owner
  libmpscpp.cpp::FixGblRef, declared in the unit-149 header
  ledger, no roster row). The dump tail closes at the
  operator<<(short) close brace; the unsigned short/long/unsigned
  long/float/double shl members that follow in the vendor file
  are the next chunk's text, ledger only.
- interp-sysfunc.cpp::AllocSV (unit 239; LOC 103, score 164.9,
  rank 285): H-018, hit-adapt - AllocSV.kt: the state-vector
  allocator; Msv non-null (the Fcn call sites demand Msv; the
  NULL arm is dead, H-013), the allocSvFirstCall once-only gate
  for the signal-reg fossil (H-018 divergence ledger, the
  unit-211 Sigint precedent), STDOUT_WRITER lazy seat, STDIN_READER
  reused from Getstr2.kt, field-init roster verbatim, memset as the
  construction-zero H-018 note, the SQLITE arm live (Col_Names,
  pgsql_msg, Table "mumps", TabSize 14), the non-SQLITE getenv arm
  an inert ledger.
- compiler-parse.c::<misc> (unit 240; LOC 210, score 108.8, rank
  277): H-014, hit-reuse - CompilerParseMisc.kt: the residue
  disposition (RETRIEVE/STORE/the walk labels on the landed
  carriers, CONCAT..EXPONEN over InterpH u186 shared values, the
  Coded* const family, the SSS/TTT pointer-fold) plus the bodies
  ConCheck (public, the digit-quote test and SSS shift with the
  i >= b.size guard), strcasecmpCpm/strncasecmpCpm (PRIVATE, the
  non-POSIX vendor semantics returns 0 on prefix-equal), memcheck
  (the MaxTmp high-water), cvar (the CompilerState.cvars/cvarx
  table), and Extract1 (holder statics, the SILENT 63-clamp
  H-007 bounds-walk per the unit-84 Cannon precedent).
- include/mumpsc/compiler-globals.h::<misc> (unit 241; LOC 148,
  score 84.0, rank 266): H-004, hit-reuse - CompilerGlobalsH.kt:
  the pure disposition ledger (banner, every variable definition
  to its CompilerState seat, the struct families to the unit-151
  carrier, the prototype roster to landed owners/Stubs seats/the
  two declaration fossils).
- compiler-open.c::mumps_open (unit 242; LOC 53, score 72.7, rank
  256): H-015, hit-adapt - MumpsOpen.kt: the emitter-walker render
  (the openf loop, the CompileState.t2/ierr/parse_ pattern per the
  unit-46 ReadCode precedent, Stubs.Extract2 for the percent
  payload, byte-verbatim emissions including the atoi space pair,
  the POST_flg close file-private, PARSE_ERROR_MO/ARG_LIST_ERROR_MO
  file-private consts).
- interp-parse.cpp::cannon (unit 243; LOC 35, score 37.3, rank
  220): H-014, hit-reuse - VERIFICATION-ONLY row-note appended to
  the landed Cannon.kt: the compiler-sys 71-loc body and the
  interp-parse 35-loc body are the SAME algorithm astyle-
  different; the landed unit-84 render owns it; PatrnInterp/
  PatrnSys1 already bind cannon to it; no new body per the
  unit-186 KDoc-fold precedent (a non-ASCII multiplication sign
  in the earlier prose was found and fixed during the H-015 scan).
- include/mumpsc/builtin.h::<misc> (unit 244; LOC 33, score 33.5,
  rank 211): H-004, hit-reuse - BuiltinH.kt: pure prototype ledger,
  every prototype named to its landed render or Stubs seat (the
  minor StopInit footnote correction: unit-166 stopInit(file) is
  the landed owner, MSV-arg seat noted); Replace stays the Stubs
  stub.
- interp-bifs.cpp::GtkTreeAdd (unit 245; LOC 30, score 30.8, rank
  207): H-014, hit-reuse - VERIFICATION-ONLY row-note appended to
  the landed render: the interp-bifs body verified BYTE-IDENTICAL
  to the interp-zfcns unit-83 landed owner (diff this session); no
  new body per the single-owner rule.
- interp-sysfunc.cpp::Merge (unit 246; LOC 59, score 30.1, rank
  206): H-014, hit-reuse - Merge.kt: the whole body on landed
  seats (Mglobal RETRIEVE/STORE/XNEXT on Carriers codes, the four-
  arg Stubs.keyfix, the strcpyMergeAt private helper); the vendor
  j/k init = 0 break-loop completion semantic kept, the for
  (jj in i-1 downTo 1) if (>127) break pattern, MDEBUG and the
  empty SQLITE guard as the comment ledger.

Whole-arm kotlinc gate over the 232-file doc tree: silent, EXIT=0
with every file of this tranche in place (the 10 new/edited files
re-gated after the render pair: InterpSqlBtree, LibmpscppMisc7,
Stubs with the Concat flips and the fixGblRef seat, plus the eight
landed earlier this session). H-015 hygiene scan run over all ten:
no raw dollar (comment text cleaned to \u0024 during the scan), no
backticks, no double-bang, ASCII-only.

DOC consult grammar rows (timestamps identical to the events CSV
append of this push):

- DOC consult: unit interp-sql.cpp::btree, entry H-018, outcome hit-adapt, timestamp 2026-11-21T03:11:00Z
- DOC consult: unit libmpscpp.cpp::<misc-7>, entry H-014, outcome hit-reuse, timestamp 2026-11-21T03:12:00Z
- DOC consult: unit interp-sysfunc.cpp::AllocSV, entry H-018, outcome hit-adapt, timestamp 2026-11-21T03:13:00Z
- DOC consult: unit compiler-parse.c::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-21T03:14:00Z
- DOC consult: unit include/mumpsc/compiler-globals.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-21T03:15:00Z
- DOC consult: unit compiler-open.c::mumps_open, entry H-015, outcome hit-adapt, timestamp 2026-11-21T03:16:00Z
- DOC consult: unit interp-parse.cpp::cannon, entry H-014, outcome hit-reuse, timestamp 2026-11-21T03:17:00Z
- DOC consult: unit include/mumpsc/builtin.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-21T03:18:00Z
- DOC consult: unit interp-bifs.cpp::GtkTreeAdd, entry H-014, outcome hit-reuse, timestamp 2026-11-21T03:19:00Z
- DOC consult: unit interp-sysfunc.cpp::Merge, entry H-014, outcome hit-reuse, timestamp 2026-11-21T03:20:00Z

Next protocol step: batch 8 is now fully rendered (T1 simple 216-
226, T2 core 227-236, T3 complex 237-246); the next session is the
B8 buffer close and the batch-close instrument, with the events
CSV regen expected at 688 consult rows -> 1374 event rows
byte-identical to the tracked file, then batch 9 tranche 1.
