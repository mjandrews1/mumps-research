package daf

// Unit 293 (DAF B10 T3, ledger): include/mumpsc/sysparms.h::<misc>
// (units.csv row 293: 39 physical LOC - the non-banner residue of
// the header): the OS/build config lattice. The dump proves the
// model bag is EMPTY after the banner and the vendor's own head
// note block: every physical line is an #include/#define/#ifdef.
// Consults (2026-11-25T15:09:00Z, nominal): BASE-log rows 39/78
// hit-adapt resolving like the unit-164 MemsizeHMisc.kt precedent
// (the same ifdef family duplicated here: it is configuration
// debt that sysparms.h repeats the OS lattice and PS/PS1 appear in
// memsize.h too), rows 46/85 hit-adapt (the READ THIS head block
// is build documentation: it instructs tweakers on UNIX/DOS/
// USYSTEM/SINGLE/SERVER/BUF_PARSE/MNORMAL/STEM/GCVT/NEW_TREE/DELIM/
// SUB - none of it compiles to the model).
// Disposition per line (nothing emitted):
//   - #include <mumpsc/strmax.h> - include residue; STR_MAX seated
//     as STR_MAX_D = 500 (DafCRuntime.kt; the strmax.h lattice
//     LEDGERS there).
//   - PS 32000 / PS1 PS+1 - the LARGE twin of memsize.h's 4096 pin
//     (the two headers disagree; the vendor comment "STR_MAX now
//     defined in strmax.h" admits the header is in flux; the
//     CompilerMumpsMain renders pin symlen = 4096 per the
//     memsize.h reading, quirk-ledgered at both).
//   - __APPLE__ GCVT arm: dead on the reference build (Linux);
//     LEDGER.
//   - _MSC_VER -> MSVisualC else Linux: the reference arm defines
//     Linux (and with it UNIX 1, USYSTEM LINUX, DOS 2 - identical
//     to the unit-164 lattice; MSVisualC/MSVC/SINGLE dead arms
//     LEDGER).
//   - #ifndef SERVER -> SINGLE: SINGLE defined on the reference
//     build (single-user globals); LEDGER to ServerMain unit kin.
//   - The halt-on-error enable block is an empty comment scaffold:
//     no model effect.
//   - Solaris/GnuDos arms: dead; LEDGER (the GnuDos arm is the
//     noted DOS twin; MSVisualC/Solaris quirk: the comment lattices
//     cite SYSTEM defines that stay commented out in every arm).
// VENDOR QUIRK kept: the header's own child comment "STR_MAX now
// dfined in strmax.h" contains the vendor's MISSPELLING
// ("dfined") - verbatim ledger.
// Dafny: EXEMPT (config residue; journal row).

// (no Kotlin emitted: the unit's model bag is empty per the dump
// verification; all residue lines are the ifdef/define lattice)
