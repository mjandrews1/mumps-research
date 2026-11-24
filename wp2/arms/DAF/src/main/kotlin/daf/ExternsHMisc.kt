package daf

// Unit 197 (DAF B7 T2): include/mumpsc/externs.h residue (<misc>,
// 26 physical LOC): the compiler-header GPL banner, the struct nmes
// name-chain node declaration, and the TU extern set (gpad, xpx,
// Jflg, ierr1, ierr, ctrlc, tpx, iorg, pd1len, sdlim, setpiece,
// symlen, t0px, t2, the nmes-head nstart, and the buffer externs
// bd, pd1, setname, symflg, v1d, xd). No macros shape calls in this
// residue; no wrappers raised. Seat map (rows 6/22 file-scope
// discipline): xpx, sdlim, t0px, t2, ierr live on DafCompilerState
// (the B6 seat file); tpx is the DafMsv field; bd, v1d, xd are the
// DafMsv carriages; pd1 folds onto the DafCompilerState.pd1 buffer
// (pd1len alongside); the nmes chain is the DafCompilerState except-
// chain family of the B6 compiler-state render (row 62 fold);
// symflg/setname/setpiece/gpad/Jflg/ierr1/iorg/ctrlc are carried
// as declaration-only residue - no compiled render of this arm
// names them at a body yet (each rides its owning TU row when
// drawn; symflg and setpiece note their conflicts noted nowhere:
// symflg unseated, setpiece the InterpPieceMisc setpiece parameter
// family).
//
// DAF consult: BASE-log rows 6/22 residue discipline, outcome
// hit-reuse (2026-11-24T16:10:30Z, nominal); the banner rides the
// CopyrightNotice disclosure discipline, not re-emitted.
