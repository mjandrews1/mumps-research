package doc

// Unit 81 (B3 tranche 2): interp-parse.cpp::<misc> (batch 3, core
// stratum; LOC 59, score 25.7). The header-residue chunk of the
// modern interpreter parse file: the Feb 13, 2026 banner (O'Kane
// 2001-2026 copyright, with the astyle invocation comment line
// residency), the include ledger (iostream, stdio/stdlib/string/
// ctype, the GTK/gdk/gtkx windowing includes, gdk keysyms; the
// mumpsc/defines, btree, sys types, locale, monetary, semaphore,
// unistd, time, arith, globalOrder set; then global, fcns, sysparms,
// keyfix), the SQLITE define (resolved DEFINED per the reference
// build config, H-018 - the vendor defines SQLITE at file scope and
// the parse core keys on it), the _INTERP_ define, the define
// constants, and the prototype roster up to the extern ierr1 (the
// chunker cut is ahead of the match definition). Residue render,
// no executable code (InterpMisc unit-19 precedent) - with one
// divergence from the unit-19 sibling: this file's prototype roster
// OVERLAPS the unit-19 roster of interp-interp.cpp (both vendor
// files carry the same core declarations), so the seats here render
// as references to the existing Stubs seats and landed owners, not
// redeclarations. Vendor quirks kept and noted: the paren-coding
// defines of this chunk are OPEN_PAREN 206, COMMA 207, CLOSE_PAREN
// 209 - NOT the stateVector CodedClose 207/CodedComma 208 pair, so
// this chunk's COMMA names the SAME byte as CodedClose and
// CLOSE_PAREN introduces a 209 byte the Carriers do not carry (the
// vendor's own coding divergence, kept verbatim in the consts below
// with the H-015 reference-not-redeclare rule honored for
// OPEN_PAREN/GOSUBMAX/SWAP/FORMAX/EXECLEVS/LBLS already declared by
// the unit-19 render; the CSTR/USTR cast macros have no Kotlin
// analog, comment ledger). The GOSUBMAX/SWAP/FORMAX/EXECLEVS/LBLS
// values of this chunk equal the unit-19 declarations and are
// referenced, not redeclared (H-015 reuse). The static patrn1
// declaration resolves to the landed unit-30 render (Patrn1Interp,
// the Stubs delegate). The patrn prototype names THIS batch's unit
// 92 (interp-parse.cpp::patrn, complex stratum, not yet landed) -
// the declaration rides the new H-014 seat below. makeLock and
// cleanLocks are landed renders (LockMisc, batch 2). The extern
// short ierr1 has no prior seat: it renders as the top-level owner
// below (H-004 one owner per C entity).

// interp-parse.cpp defines (verbatim chunk values; OPEN_PAREN and
// the five level constants are referenced from the unit-19 render,
// not redeclared):
//   OPEN_PAREN 206        (reference: InterpMisc)
//   GOSUBMAX 50, SWAP 30, FORMAX 30, EXECLEVS 40, LBLS 80
const val IPPARSE_COMMA = 207
const val IPPARSE_CLOSE_PAREN = 209

// interp-parse.cpp extern short ierr1 (prototype-roster tail extern;
// owner the interp-parse error path; int seat per the H-010
// promotion note)
var ierr1: Int = 0

// Prototype roster ledger (each entry names the existing seat or
// landed owner; order verbatim):
//   lpad, substr, sigint, LocalName, GlobalQuery, Qsub, Qlength,
//   ErrorMessage, zfcn, fcn, xindex, TimeOut, DirectRead,
//   TimeOut_getstr1, _SymFree, sql - the Stubs seats of the unit-19
//   roster (DirectRead/_SymFree/ErrorMessage now delegates)
//   makeLock, cleanLocks - landed LockMisc renders (batch-2 units)
//   static patrn1 - landed unit 30 (Patrn1Interp, Stubs delegate)
//   void patrn(unsigned char *, struct MSV *) - unit 92 of THIS
//     batch, not yet landed: the seat below (H-014, converts on
//     landing per the Extract2 precedent)
