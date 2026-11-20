package doc

// Unit 197 (B7 tranche 2): include/mumpsc/externs.h::<misc> (batch 7,
// core stratum; LOC 26, score 12.8, rank 124) - the compiler-C extern
// declaration header (banner + struct nmes declaration + the extern
// variable roster, vendor lines 32-66; physically verified this
// session, nonblank 26 matches the roster row). Declaration-only
// ledger, no executable code (CopyrightMisc unit-1, CppsysfuncMisc
// unit-76, InterpArithMisc unit-90, GlobalClassMisc unit-89
// precedents). Consult H-004 hit-reuse: every extern on the roster
// already has a single-owner carrier from earlier batches - this
// file maps each declaration line to that seat and raises NOTHING;
// H-015 no-redeclaration rule referenced per the OPEN_PAREN
// precedent (the constant is named, never redeclared).
//
// Idem-partner note (the compiler-globals-ext.h unit-151 ledger
// applies): the extern C linkage names here are the compiler
// translation unit's view of state whose interp twins live on Msv
// (stateVector.h members); both organs keep their earlier seats -
// H-004 cross-reference, no duplication.

// externs.h:40-43 struct nmes - the SAME C struct tag the batch-5
// unit-151 render declared from compiler-globals-ext.h:56-59; the
// type is referenced, not redeclared (H-004/H-015): the Nmes class
// of CompilerGlobalsExtMisc.kt owns this shape, and the nstart
// chain heads live on the Msv.nstart (stateVector.h:72) /
// CompilerState.nstart-comment seats.
//
// externs.h guest anchors to seats (vendor line -> arm owner):
//   :45  extern int gpad          -> CompilerState.gpad
//        (compiler-globals-ext.h:47 seat; Msv.gpad state twin)
//   :46  extern long xpx           -> CompilerState.xpx
//        (compiler-globals-ext.h:38 seat; the xpxIdx narrowing
//        helper documents each Kotlin Int use)
//   :47  extern short Jflg         -> CompilerState.Jflg
//        (compiler-globals-ext.h:50 seat, H-010 Int)
//   :48  extern short ierr1        -> the top-level ierr1 carrier
//        (InterpParseMisc.kt; the H-004 keep-first-owner rule -
//        compiler-globals-ext.h:48 cross-ref note)
//   :49  extern short ierr         -> CompilerState.ierr
//        (compiler-globals-ext.h:40 seat; Msv.ierr state twin)
//   :50  extern short int ctrlc    -> CompilerState.ctrlc
//        (compiler-globals-ext.h:46 seat; Msv.ctrlc state twin)
//   :51  extern short int tpx      -> CompilerState.tpx
//        (compiler-globals-ext.h:34 seat; Msv.tpx Long state twin)
//   :52  extern short iorg         -> CompilerState.iorg
//        (compiler-globals-ext.h seat; Msv.iorg state twin)
//   :53  extern short pd1len       -> CompilerState.pd1len
//        (compiler-globals-ext.h:36 seat, H-010 Int)
//   :54  extern short sdlim        -> CompilerState.sdlim
//        (compiler-globals-ext.h:42 seat; Msv.sdlim state twin)
//   :55  extern short setpiece     -> CompilerState.setpiece
//        (compiler-globals-ext.h:31 twin-of-Msv note; Msv.setpiece
//        operates per-organ, both seats live)
//   :56  extern short symlen       -> CompilerState.symlen
//        (the idem-partner note; Msv.symlen state twin; both organs)
//   :57  extern short t0px         -> CompilerState.t0px
//        (compiler-globals-ext.h:39 seat; Msv.t0px state twin)
//   :58  extern short t2          -> CompilerState.t2
//        (compiler-globals-ext.h:35 seat; Msv.t2 state twin)
//   :59  extern struct nmes *nstart -> the Msv.nstart Any? opaque
//        seat (stateVector.h:72) is the idem partner; the
//        commented duplicate :60 (vendor fossil, "// struct nmes
//        *nstart;") kept verbatim in this ledger note per H-013
//   :61  extern unsigned char bd[] -> CompilerState.bd
//        (the compiler unsigned char scratch at STR_MAX)
//   :62  extern unsigned char pd1[] -> CompilerState.pd1
//        (compiler-globals-ext.h:43, PS1 32001 sizing)
//   :63  extern unsigned char setname[] -> CompilerState.setname
//        (compiler-globals.h:32, the 128-byte compiler twin;
//        Msv.setname state twin)
//   :64  extern unsigned char symflg -> Msv.symflg
//        (stateVector.h:52 Int seat; the scalar byte domain rides
//        Int per the H-010 promotion note)
//   :65  extern unsigned char v1d[] -> CompilerState.v1d
//        (the compiler globals-array parsing buffer, STR_MAX)
//   :66  extern unsigned char xd[]  -> CompilerState.xd
//        (compiler-globals-ext.h:44, 2048 sizing)
//
// The banner block (Mumps Compiler, Kevin C. O'Kane copyright/GPL,
// the Fortran-origin note) and the #include <stdio.h> line are
// comment-include residue with no Kotlin analog (ledger only).
