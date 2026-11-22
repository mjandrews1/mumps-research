package daf

// Unit 169 (DAF B6 T2, core): include/mumpsc/interp.h::<misc>
// (69 LOC, score 13.3, rank 130): the interpreter dispatch-residue
// header: the GPL banner, the INTERP_H guard, the defines.h
// include, TSTACK 10, the 22 single-letter command macros, the
// 30 integer operator-precedence codes, PREDICATEVAR, and the four
// interpret-family prototypes plus the symbasic.h include.
// Consults (2026-11-22T16:02:00Z, nominal): BASE-log row 37
// hit-reuse (include residue -> the KDoc disposition ledger; the
// SymHMisc/MemsizeHMisc disposition precedent), rows 39/78
// hit-adapt (the macro lattice is recorded below per define; the
// reference builds read the same lattice because the header is
// unconditional - no ifdef web to resolve).
// Dispositions (nothing emitted; values recorded per define):
//   TSTACK 10 and the BREAK/CMMNT/DATAKILL/.../NEW single-letter
//   command macros - consumed by the interpret core (roster unit
//   185, the deferred big-three session); the DafCompilerState and
//   Fcn renders already pin the SEPARATE compiler-side command
//   lattice, so nothing binds today.
//   The POPEN..PREDICATEVAR integer codes 0..28 and '~' - the
//   postfix expression operator codes of the interpret parser; the
//   DAF-Fcn (unit 111) parse table renders its own family codes, so
//   these are ledgered here for the unit-185 render rather than
//   pinned now (no DAF seat reads them today; pinning untaken
//   consts would invite drift against defines.h at the bench
//   verification).
//   The four prototypes (Interpret, parse_, prnterr, BuildGlobal,
//   BuildLocal): Interpret is the roster unit 185 cell; parseDaf is
//   the existing named-owner stub of the b9 roster; prnterr and
//   the Build pair are interpreter services with no corpus units -
//   their named seats raise at the unit-185 session when called.
//   The symbasic.h include pins the symbol-basic lattice already
//   seated at the DafInterpStubsT3 renders.
// Dafny: EXEMPT (header residue; journal row).

// (no Kotlin emitted: the unit's model bag is residue per the dump
// verification; all 69 physical lines are banner/macros/prototypes)
