package daf

// Unit 219 (DAF B8 T1, simple): compiler-lock.c::<misc> (30 LOC,
// score 5.2): the compiler lock-command emitter source banner:
// license header (the GPL block, the 2003 through 2024 copyright
// family), the May 10 2024 date line, and the include ledger. The
// file head opens the lock_code walk; lock_code itself is a later-
// batch roster row (compiler-lock.c::lock_code) and renders at its
// owner - this chunk owns only the banner and the four includes.
// All derived from the pristine vendor source this session (vendor
// tarball sha256 621deb...ffdbe; physically verified: the chunk
// spans the vendor file head; byte-compared against the unit-218
// kill banner - only the date line differs).
//
// Include ledger (single-owner notes, the unit-187
// CompilerZcommandsMisc precedent - the four includes are the same
// set in all three batch-8 compiler-banner rows; only the date line
// differs between them):
// - stdio.h: the java.io/kotlin.io edge carriers of the whole arm
//   (the DafCRuntime/DafCRuntimeT2 io seats).
// - mumpsc/memsize.h: the unit-164 render (MemsizeHMisc.kt).
// - mumpsc/compiler-macros.h: the unit-236 render of THIS batch
//   (CompilerMacrosH); macro families read per-unit by the owner
//   renders at their point of use (the per-unit consumer rule; no
//   chunk-level seats).
// - mumpsc/compiler-globals-ext.h: the unit-151 render
//   (CompilerGlobalsExtMisc.kt) plus the DafCompilerState carrier
//   seats.
//
// No runtime seat: the chunk carries no function bodies or
// file-scope definitions. Consults (2026-11-24T21:11:30Z, nominal):
// the ledger shape verbatim of the unit-187 precedent.
// Dafny: EXEMPT (declaration residue; journal row).
