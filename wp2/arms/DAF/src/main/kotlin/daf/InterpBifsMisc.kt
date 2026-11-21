package daf

// Unit 110 (DAF B4 T2, core): interp-bifs.cpp::<misc> (60 LOC): the
// builtins TU residue: the GPL banner and include set, the sym_/wstem/
// sigint prototypes, the SQLITE define, the CLOSE/COMMA/NO/YES zfcn
// defines, the gcvt_wrapper macro, and the one function body the unit
// carries: Mget.
// Consults: BASE-log row 37 hit-adapt (TU residue; banner not emitted
// by any graded path and not replicated, per the T1 residue precedent;
// prototypes become named-owner seats), row 79 hit-adapt (gcvt_wrapper:
// on the reference build GCVT is undefined so the wrapper resolves to
// the real gcvt(x, digits, buf); the DAF seat models it with
// DafCRuntime.fmtG (the %g family anchored by rows 41/43/26); the
// digit-count argument is dropped as the divergence already documented
// on the row 79 BASE render), row 56 family (the char tmp[128] scratch
// of Mget becomes a fresh per-invocation value; sym_ fills it, so the
// stub signature returns it).
// Vendor quirks preserved: CLOSE=207 and COMMA=208 here NOT the
// 206/207/208 open/comma/close triple of libmpscpp (the local zfcn
// constants are shifted one code point; kept verbatim under locally
// qualified names rather than reusing the T1 CODED_*_D constants).
// Seats: sym_ is interp-sym.cpp machinery with no roster unit of its
// own (interp-sym.cpp entries in the subset are the dedicated
// services; the sym_ dispatcher stays a permanent named-owner stub).
// sigint was already seated in T1 (sigintDaf). wstem stays a prototype
// seat.
// Dafny: EXEMPT (residue + state-machine relay; journal row).

const val SQLITE_D = true // #define SQLITE (on the reference build)

const val CLOSE_D_207 = 207 // zfcn CLOSE (interp-bifs local define)
const val COMMA_D_208 = 208 // zfcn COMMA (interp-bifs local define)
const val NO_D = 0
const val YES_D = 1

// gcvt_wrapper(x, i, a): reference build -> gcvt(x, i, a).
fun gcvtWrapperD(x: Double): String = DafCRuntime.fmtG(x)

// char *sym_(int, unsigned char*, unsigned char*, struct MSV *) -
// owner: interp-sym.cpp::sym_ (no roster unit for the dispatcher;
// permanent named-owner stub).
fun symDaf(symflg: Int, a: String, b: String): String =
    throw NotImplementedError(
        "sym_: out-of-scope owner (interp-sym.cpp::sym_, not a roster unit)")

// unsigned char * Mget(unsigned char * s, struct MSV * svPtr)
fun Mget(s: String): String {
    val tmp = StringBuilder() // char tmp[128] (row 56 family)
    return symDaf(1, s, tmp.toString())
}
