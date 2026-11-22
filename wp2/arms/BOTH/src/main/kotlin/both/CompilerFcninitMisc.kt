package both

// Unit 155 (BOTH B6 T1, simple): compiler-fcninit.c::<misc> (30
// LOC residue). Batch_plan ordinal 155: simple, score 5.2,
// complexity rank 16. The residual <misc> chunk of the
// compiler-fcninit.c TU: the O'Kane banner (lines 1-27), the
// May 15, 2024 date note (line 29) and the include block
// (lines 31-37: stdio.h; mumpsc/memsize.h; mumpsc/
// compiler-macros.h; mumpsc/compiler-globals-ext.h). Everything
// else in the TU is claimed by function bodies that are their own
// roster units, so this unit seats as a KDoc-only residue ledger
// (the unit-144 include/mumpsc/sym.h::<misc> precedent).
//
// Residue resolution (h018): each include resolves against an
// already-seated owner in this arm:
//   - mumpsc/memsize.h -> the unit-164 render (MemsizeHMisc.kt,
//     this tranche) seats the config lattice; nothing here outruns
//     it.
//   - mumpsc/compiler-globals-ext.h -> the unit-151 render
//     (CompilerGlobalsExtMisc.kt, B5 T3) seats the Nmes/Txt/Dcs
//     carriers and the CVARMAX/PS1/FBMAX consts.
//   - mumpsc/compiler-macros.h and stdio.h carry no declarations
//     this arm's renders depend on beyond seats already raised
//     (Sysout/Sysout_skip appear only inside the function bodies
//     of the compiler-OO TUs, not in this residue).
//
// No declarations ride the residue (the banner and the include
// block emit no symbols); the Ledger records the provenance so
// later units citing this TU resolve cleanly.
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - reused and hit: the include
//     lattice resolves against the owners named above; no live
//     arm of this TU's own conditionals sits in the residue.

// (No declarations; KDoc-only residue ledger.)
