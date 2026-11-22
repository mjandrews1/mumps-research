package daf

// Unit 164 (DAF B6 T1, simple): include/mumpsc/memsize.h::<misc>
// (50 LOC, score 10.0, rank 98): the define lattice of the run-time
// memory-shape header. The dump4 extraction proves the model bag is
// EMPTY: all 50 physical lines are the banner, the May 4 2024 date
// line, and the #define / #ifdef lattice; no function or table data
// reaches the DAF model.
// Consults (2026-11-22T15:49:30Z, nominal): BASE-log rows 39/78
// hit-adapt (the OS-detect ifdef lattice resolves to the reference
// build = Linux: #ifndef _MSC_VER and #ifndef GnuDos define Linux,
// so USYSTEM = LINUX, UNIX = 1, DOS = 2, SYSTEM = UNIX; the Watcom /
// GnuDos / MSVisualC arms are dead and carried here as the ledgered
// lattice; the __APPLE__ GCVT define and the _MSC_VER MSVisualC
// define are dead on the reference build), row 46/85 family
// manual-decision (the FIXME note that PS/PS1 also appear in
// sysparms.h is configuration-debt commentary, no model effect),
// row 37 hit-reuse (include residue -> the KDoc disposition ledger
// of the SymHMisc.kt unit-144 precedent).
// Disposition per define (nothing emitted; the values are recorded
// here as the arm config cents):
//   PS 4096, PS1 PS+1 - the memory page constants (no DAF seat
//   reads them today).
//   SHOW_CODE 1, VERSION 1000, FORPARM 32, NOTABS - build switches
//   already pinned at their consumer renders (NOTABS ledgered at the
//   unit-115 readf render shape; FORPARM is consumed by the
//   compiler family renders of later batches).
//   DDAT "data.dat", DKEY "key.dat" - the native data/key file
//   names; the STEM define is commented out in the vendor.
// VENDOR QUIRK kept in the lattice reading: the Solaris arm quotes
// "MSVC undefined" in the comment table but its own ifdef block
// never defines or undefines MSVC (the comment table and the ifdef
// blocks disagree in shape; the reference Linux arm matches the
// Linux ifdef block, which is the arm the render pins).
// Dafny: EXEMPT (config residue; journal row).

// (no Kotlin emitted: the unit's model bag is empty per the dump
// verification; all 50 physical lines are the define lattice)
