package doc

// Unit 110 (B4 tranche 2): interp-bifs.cpp::<misc> (batch 4, core
// stratum; LOC 60, score 18.4, rank 159). The Mumps builtins
// library file residue: the O'Kane GPL banner, the Oct 20, 2025
// astyle note, the bifs.c module banner, the include ledger, the
// sym_/wstem/sigint forward declarations, the SQLITE define, and
// the zfcn-support defines CLOSE/COMMA/NO/YES plus the gcvt
// wrapper macro - and, inside the chunk, the Mget body (the
// three-line symbol fetch used by zfcn; below the roster min-loc,
// it rides this misc unit per the tool's misc rule).
//
// H-018 ledger: SQLITE is defined per the reference build config
// (live); the GCVT macro arm of gcvt_wrapper is NOT in the
// reference config - the #else gcvt(x, i, a) arm is live and is
// already seated at Stubs.gcvtWrapper (numSetup residue owner);
// the TODO extern-globals note (preventing a dynamic lib) rides
// as this ledger line. The gtk/gtkx includes ride the include
// ledger (the gtk seats live at Stubs.gtkTreeStoreAppend/
// gtkTreeStoreSet, raised by the tree units).
//
// Define ledger (zfcn support, values verbatim; CLOSE/COMMA
// coincide in value with defines.h CodedClose/CodedComma but are
// DISTINCT file-local defines here - no cross-file seat is
// claimed; NO/YES are the 0/1 booleans):
//   CLOSE 207   COMMA 208   NO 0   YES 1
//
// Include ledger: stdio, stdlib, string, ctype, math, time,
// gtk/gtk.h, gtk/gtkx.h, sys/types, sys/stat, fcntl, unistd,
// mumpsc/defines.h, mumpsc/fcns.h, mumpsc/global.h,
// mumpsc/builtin.h, mumpsc/arith.h, mumpsc/inline.h.
// Forward declarations ride the seats: sym_ (Stubs.sym_),
// wstem (Stubs wstem family, interpreter vaporize) and
// sigint (Stubs.sigint).

fun Mget(s: CharArray, svPtr: Msv): CharArray? {
    // char tmp[128] - the local scratch the vendor passes as the
    // sym_ result buffer; the vendor returns sym_'s own pointer.
    val tmp = CharArray(128)
    return Stubs.sym_(1, s, tmp, svPtr)
}
