package doc

// Unit 167 (B6 tranche 2): interp-globalb.cpp::Mglobal (batch 6,
// core stratum; LOC 17, score 12.3) - the global-array dispatcher
// behind the whole Stubs.Mglobal surface. RESOLVES the
// Stubs.Mglobal stub (unit-33-era seat; every call site binds
// unchanged through the delegate flips). H-018: the reference
// build compiles SQLITE DEFINED (the interp-fcn/BifsMisc ledger),
// so the roster chunk IS the live arm: CLOSE returns 1 without
// touching the store, RETRIEVE/STORE run keyfix then fall to
// btree, everything else falls to btree straight. The native
// (non-SQLITE) arm - the NEXT/ORDERNEXT/ORDERPREV remap ladder,
// GKILL/XNEXT/CLOSE blocks - is compiled out and IS a corpus row
// of its own at interp-globalb.cpp::<misc> (batch 10), so it is
// NOT restated here beyond this pointer (the H-018 boundary note).
// h013: the vendor's in-body declaration-only locals (the static
// CloseFlg=0 dead store, the unpad/sigint/flush forward
// declarations) are not rendered, noted. Stubs.keyfix is the
// H-014 seat raised at unit 37 (not a corpus unit) and btree is
// the REAL unit-4 render (Btree.kt). CLOSE = 4 rides the
// file-private const (defines.h Mglobal close code; the
// MumpsMainUnit150 CLOSE_MGV precedent). Naming per H-017
// (Mglobal kept verbatim).

// defines.h:85 CLOSE 4 (Mglobal close code; file-private per the
// u150 CLOSE_MGV precedent)
private const val CLOSE_MGV = 4

fun Mglobal(g: Int, key: CharArray, bd: CharArray, svPtr: Msv): Int {
    if (g == CLOSE_MGV) return 1 // if (g == CLOSE) return 1;

    if (g == RETRIEVE) {
        Stubs.keyfix(key)
        return btree(g, key, bd, svPtr)
    }

    if (g == STORE) {
        Stubs.keyfix(key)
        return btree(g, key, bd, svPtr)
    }

    return btree(g, key, bd, svPtr)
}
