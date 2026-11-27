package both

// Unit 293 (BOTH B10, core): include/mumpsc/sysparms.h::<misc>
// (39 LOC). Batch_plan ordinal 293; units.csv row 291. The
// sysparms.h config lattice: the banner/license and the
// READ-ME comment block are residue, not carried; the model
// is the macro lattice the reference build resolves (h018 -
// the MemsizeHMisc precedent). The header include guard, the
// strmax.h include (STR_MAX 32000 already seated,
// BothCRuntime.kt:28) and the PS/PS1 pair (sysparms.h:74-75,
// PS 32000, PS1 32001) carry the numeric core.
//
// LATTICE RESOLUTION (h018, the reference build config - the
// oracle build on Linux):
//   LIVE:  PS 32000, PS1 PS+1 = 32001, Linux (via _MSC_VER
//     undefined), UNIX 1, USYSTEM LINUX, DOS 2, SINGLE (via
//     SERVER undefined). GCVT 1 is the __APPLE__ arm - the
//     reference build defines it (the MemsizeHMisc ruling,
//     the B6 unit-164 ledger; the pin rides that seat).
//   EXCLUDED ARMS (documented only): MSVisualC (UNIX 0,
//     USYSTEM DOS, DOS 2, SYSTEM DOS, MSVC, SINGLE);
//   Solaris (UNIX 1, USYSTEM UNIX, DOS 2); GnuDos (UNIX 0,
//     USYSTEM DOS, DOS 2, SYSTEM DOS); the SYSTEM defines are
//     commented out in every arm (not emitted).
//
// NEW PINS: sysparmsPS 32000 / sysparmsPS1 32001 (h017
// collision note: the compiler-side package consts PS/PS1
// (memsize.h:32-33, 4096/4097, MemsizeHMisc.kt) are different
// corpus pins - the sysparms pair keeps the interpreter-side
// width under the file-qualified names; the 32001 literal at
// the Msv pd1 seat (BothCRuntime.kt:96) now ledger-references
// these pins).
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - reused and hit: the
//     platform lattice resolves against the reference build
//     config (Linux arm live; MSVisualC/Solaris/GnuDos
//     documented, excluded).
//   h017 naming - reused and hit: the PS/PS1 collision
//     disambiguates file-qualified (sysparmsPS/sysparmsPS1);
//     SINGLE/Linux/UNIX/USYSTEM/DOS keep vendor names in the
//     ledger only (no emitted symbol - config macros fold to
//     the documented pins).
//   h014 out_of_scope_call - reused: STR_MAX ledgers to its
//     strmax.h seat (BothCRuntime.kt:28); GCVT ledgers to the
//     MemsizeHMisc pin.
//   Dafny: EXEMPT (config macro lattice; no model beyond the
//     constant pins - the MemsizeHMisc family record).

internal const val sysparmsPS = 32000 // sysparms.h:74 (#define PS 32000)

internal const val sysparmsPS1 = 32001 // sysparms.h:75 (#define PS1 PS+1)

// SINGLE / Linux / UNIX 1 / USYSTEM LINUX / DOS 2: config
// macros resolved live at render time (h018); they document
// the reference build state and emit no symbol. GCVT 1 rides
// the MemsizeHMisc pin (the __APPLE__ arm, reference-live).
