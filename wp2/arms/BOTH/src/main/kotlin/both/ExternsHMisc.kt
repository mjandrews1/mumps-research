package both

// Unit 204 render: include/mumpsc/externs.h::<misc> (roster
// 204, batch 7, 26 LOC per units.csv; the 66-line vendor
// residue). The externs.h declaration residue: the compiler
// banner and the pure extern declaration list (no bodies, no
// macros). The unit-155/201 residue precedent: the ledger
// records the provenance so units citing this TU resolve
// cleanly; NOTHING is emitted.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: every declaration in the
//     residue resolves to an existing seat: gpad (Msv :70),
//     xpx (CompilerGlobalsExt.xpxG Long, :38 collision note),
//     Jflg (:50), ierr1/ierr/ctrlc/tpx/iorg/pd1len/sdlim/
//     setpiece/symlen/t0px/t2 (CompilerGlobalsExt family, the
//     G-suffixed collision-renamed seats where pinned),
//     nstart (:49) and struct nmes (Nmes(name,next), the
//     CompilerGlobalsExtMisc class), bd/pd1/setname/symflg/
//     v1d/xd (the array seats, xdG the collision-renamed
//     compiler-side twin). NO new TODO stubs are raised.
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//
// Quirks kept (ledgered):
//   - the externs.h t2/ierr/sdlim etc. are the SHORT-decl
//     twins of the stateVector.h fields: the arm seats them
//     once through the CompilerGlobalsExt carriers (the h019
//     collision-renamed family); the externs.h re-declaration
//     rides the same seats, noted, not re-seated.
//   - the commented-out nstart re-declaration is a vendor
//     fossil (noted).
//   - the banner's Fortran-origin note (1-based arrays) is
//     the standing house convention for the C walks.
//
// No declarations ride the residue; nothing is emitted here.
