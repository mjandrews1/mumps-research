package both

// Unit 144 (BOTH B5 T3, complex): include/mumpsc/sym.h::<misc> (39 LOC
// residue). Batch_plan ordinal 144: complex, LOC 39, score 34.3.
// Content: the GPL banner, the include block (iostream, cctype,
// string), the using-namespace directive, the strsep and sigint
// declarations, the struct stab layout, the parser prototypes and
// the two-armed _INTERP_ keyfix comment guard. There are no function
// bodies in this residual header chunk - every named body lives in
// its own roster unit.
//
// THIS RENDER FLIPS THE STAB SEAT: the T2 BothStubs.kt carrier
// (class Stab, layout owner sym.h::<misc> pending batch-5 complex)
// lands its owner with this unit. The Stab field set is the vendor
// layout transcribed: name/next/data/OrigName as the T2 carrier
// declares them, all verified against the pristine header this
// session (the C string* pointers fold to the T2 Stab shape: String?
// for the char* seats, String? for the std::string* seats).
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - reused: the _INTERP_ guard arms
//     (interp build and non-interp build) contain the SAME commented
//     out keyfix declaration text, so the reference build resolution
//     is identity; both arms documented, live content unchanged.
//   h014 out_of_scope_call - consulted, ADAPTED (hit, not reuse): the
//     prototype block has no call sites in this chunk, so the ledger
//     below carries the owner names instead of TODO stubs; every
//     owner is a pending roster unit and the stub seat discipline
//     applies at the future call sites (the T2 precedent for
//     body-less residues).
//   h017 naming - hit: the prototype owner names keep vendor case
//     (GlobalName, fullUnpad, sym_, _SymFree, SysDump, Qsub,
//     GlobalQuery, ServiceInterface, Qlength, LocalName, BuildGlobal,
//     ErrorMessage; Mltoa arrives via include mumpsc/arith.h).
//   MISS struct_carrier (recurrence): the struct stab layout rides
//     the T2 Stab carrier (this unit is the layout owner; the T2 miss
//     family record stays the capturing vehicle, recurrence grows
//     with this flip).
//
// Render: KDoc-only residue carrier (no runtime code emitted beyond
// the prototype owner ledger below; the Stab class itself stays in
// BothStubs.kt where T2 seated it).

internal object SymHMisc {
    // Residue ledger (include/mumpsc/sym.h, pristine vendor tree
    // verified this session):
    // - banner: Mumps Compiler Run-Time Support Functions (O'Kane,
    //   A.D. 2001, 2002, 2017)
    // - include iostream / cctype / string; using namespace std;
    // - char *strsep(char **, char *) - C library declaration
    //   (rendered where a corpus body ever calls it)
    // - void sigint(int) - roster unit 254, batch 7 (stub seat in
    //   BothCRuntime.kt, T2)
    // - struct stab at sym.h:43-51 - the layout owner seating the
    //   BothStubs.kt Stab carrier (fields name, next, data,
    //   OrigName, size, copy, exec)
    // - the mumps.c:496-provenance comment kept verbatim in the
    //   vendor header (the sym_ declaration note)
    // - void GlobalName(unsigned char *, unsigned char, MSV *) -
    //   roster unit: interp-sysfunc.cpp::GlobalName (b1)
    // - void fullUnpad(unsigned char *, MSV *) - no corpus owner
    //   (libmpscpp TU helper family)
    // - the _INTERP_ keyfix comment arms (h018 identity note above);
    //   keyfix itself is the include/mumpsc/keyfix.h inline (rendered
    //   real at the unit-145 Qlength seat this tranche)
    // - char *sym_(int, unsigned char *, unsigned char *, MSV *) -
    //   no corpus owner unit (interp-sym.cpp TU sym table core)
    // - int _SymFree(int, MSV *) - roster unit: interp-sym.cpp::
    //   _SymFree (b1)
    // - void SysDump(long, MSV *) - roster unit: interp-sym.cpp::
    //   SysDump (rendered, T2)
    // - include mumpsc/arith.h (the Mltoa declaration rides it -
    //   roster unit: interp-arith.cpp::Mltoa, b10; stub seat in
    //   BothCRuntime.kt, T2)
    // - void ErrorMessage(const char *, long) - non-unit vendor
    //   helper (rendered real in BothCRuntime.kt, T2)
    // - int BuildGlobal(int, int, unsigned char *, unsigned char *,
    //   MSV *) - roster unit: interp-parse.cpp::BuildGlobal (b8)
    // - the commented BuildLocal declaration kept verbatim
    // - void Qsub(...) - roster unit: interp-sym.cpp::Qsub (b7)
    // - void GlobalQuery(...) - roster unit: interp-sym.cpp::
    //   GlobalQuery (b4)
    // - void ServiceInterface(...) - roster unit:
    //   interp-service.cpp::ServiceInterface (b7)
    // - void Qlength(unsigned char *, unsigned char *, MSV *) - the
    //   FREE-function corpus unit of this tranche (unit 145; note the
    //   mstring-class member int Qlength() of the mstringClass.h
    //   residue is a different, class-member surface)
    // - void LocalName(...) - roster unit: interp-sysfunc.cpp::
    //   LocalName (b4)
    // - int xindex(const unsigned char *, const unsigned char *,
    //   short) - roster units: interp-strmanip.cpp::xindex (b9) and
    //   the compiler-sys1.c::xindex twin (b10)
    // - void substr(unsigned char *, unsigned char *, int, int) -
    //   roster units: interp-strmanip (b7) / compiler-sys1 twin (b7)
    // - void lpad(unsigned char *, unsigned char *, short) - no
    //   corpus owner (interp TU helper)
    // - void _fnumber(...) - roster unit: interp-fnumber.cpp::
    //   _fnumber (b4)
    // - endif guard
}
