package daf

// Unit 144 (DAF B5 T3, complex): include/mumpsc/sym.h::<misc>
// (units.csv row 135: loc 39, cr 0.466, ptr 11, ctl 3, score 34.3,
// rank 215, complex): the Mumps Runtime Library symbol-table header
// residue: the stab struct declaration and the run-time symbol-table
// prototype set (strsep, sigint, GlobalName, fullUnpad,
// sym_/_SymFree/SysDump, ErrorMessage, BuildGlobal, Qsub,
// GlobalQuery, ServiceInterface, Qlength, LocalName, xindex, substr,
// lpad, _fnumber) over the SYM_H include guard, iostream/cctype/
// string includes, and the arith.h tail include.
// Consults: BASE-log row 37 hit-adapt (header -> wrappers-plus-stubs
// family, the T1 unit-94/95 and T2 unit-107 precedent: no macro in
// this residue shapes a call this tranche renders, so NO wrappers are
// raised; every prototype is ledgered as a named-owner seat with its
// roster home), row 18 hit-reuse (struct stab -> the existing
// DafStabNode/DafSymChains class carriers of the unit-138 SysDump
// render; the name/data/OrigName fields are the DafSymChains side
// carriers, size/copy/exec not consumed by any DAF render), row 6/22
// hit-reuse: strsep is a libc seat (not a corpus unit, not consumed
// by any DAF-rendered call site; disposition-only ledger).
// Disposition ledger (prototype -> seat home):
//   sigint        - seated: sigintDaf (GlobalNodeStub.kt, T1)
//   SysDump       - rendered: SysDump.kt (unit 138, T2)
//   Qlength       - rendered: Qlength.kt (unit 145, this tranche)
//   LocalName     - rendered: LocalName.kt (unit 105, T2)
//   ErrorMessage  - stub: errorMessageDaf (DafInterpStubsT3.kt)
//   sym_          - stub: symRetDaf (permanent dispatcher stub)
//   _SymFree      - not a roster unit; no DAF render calls it yet
//                   (B1-preview unit in the DOC cycle; owner-named
//                   here, seat raised by its call sites)
//   GlobalName    - stub: globalNameDaf (DafInterpStubsT3.kt)
//   fullUnpad     - prototyped by the unit-107 residue (sysfunc.h);
//                   owner-named, seat raised by its call sites
//   BuildGlobal   - stub: buildGlobalDaf (DafInterpStubsT3.kt)
//   Qsub          - stub: qsubDaf (DafInterpStubsT3.kt)
//   GlobalQuery   - rendered: GlobalQuery.kt (unit 119, b4 T3)
//   ServiceInterface - not a roster unit; library service family
//                   (libmpsrdbms.h kin, unit-9 seat); owner-named
//   xindex        - stub: xindexDaf (DafInterpStubsT3.kt, b9/b10)
//   substr        - vendor canonical string service; library family
//                   seat rendered by its call sites (not a corpus
//                   unit; strmanip kin)
//   lpad          - seated: lpadDaf (DafInterpStubsT3.kt, flipped to
//                   the unit-137 lpadStrmanipD render at B5 T2)
//   _fnumber      - rendered: InterpFnumber.kt (unit 110, T2)
//   stab          - carrier: DafStabNode/DafSymChains (SysDUMP seats)
//   keyfix guarded comment block + the arith.h include tail ride the
//   keyfixDaf stub (DafInterpStubsT3.kt) and the mps_* render family
//   respectively; no code emitted from here either way.
// Dafny: EXEMPT (declaration residue; journal row).
//
// DAF consult: BASE-log row 37, outcome hit-adapt
// (2026-11-22T15:10:00Z). DAF consult: BASE-log row 18, outcome
// hit-reuse (2026-11-22T15:10:30Z). DAF consult: BASE-log row 6,
// outcome hit-reuse (2026-11-22T15:11:00Z).
// Dafny: EXEMPT (residue; journal row).
