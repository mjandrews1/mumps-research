package daf

// Unit 167 (DAF B6 T2, core): interp-globalb.cpp::Mglobal
// (17 LOC, score 12.3, rank 119): the SQLITE-arm global dispatcher:
// the reference build pins SQLITE at interp-globalb.cpp:45
// (#define SQLITE) and the vendor #if defined(SQLITE) arm ends with
// its own function-closing brace at the line the chunker cut, so the
// 17-loc roster row IS the live arm and the native #else arm is the
// dead one (the reverse of the usual lattice reading - this is the
// house SQLITE_D=true const already seated at the unit-119
// GlobalQuery render).
// Consults (2026-11-22T16:01:00Z, nominal): BASE-log rows 78/39
// hit-reuse (the TU-local #define SQLITE pins the reference arm;
// row 46/85 sibling: the local void-decl block (unpad, sigint), the
// static CloseFlg and the local void flush(MSV*) decl are DEAD
// DECLS inside the SQLITE arm - none is called in the live body -
// carried as the comment fold), row 54 hit-reuse (the four-way
// opcode dispatch over CLOSE/RETRIEVE/STORE/fallthrough), rows
// 37/90 hit-reuse (btree raised below as the named-owner stub
// btreeDaf: owner interp-native-btree.cpp::btree at line 293, the
// native backend service, not a batch T2 cell; keyfix relays to
// the existing keyfixDaf seat of the b3 roster).
// SEAT RESOLUTION: this render RELEASES the MglobalDaf named-owner
// stub of DafInterpStubsT3.kt (raised at b4-T3 with the owner
// "roster b6" - the roster b6 row IS this unit): stub DELETED, the
// real body renders here under the same name, and every existing
// MglobalDaf caller (Fcn/GlobalFind/GlobalLength/GlobalQuery/
// GlobalTranslate/Sum/LibmpscppMisc5/InterpPieceMisc) binds to the
// real body unchanged (row 90 family).
// Vendor quirk kept: the dispatch is exactly the vendor four-way
// shape; CLOSE on the SQLITE arm returns 1 WITHOUT touching btree
// (the native arm has no CLOSE case at all - dead-lattice note).
// Dafny: EXEMPT (stub-mediated global dispatcher; journal row).

// int btree(int g, unsigned char key[], unsigned char bd[],
//          struct MSV * svPtr) - named-owner stub (owner
// interp-native-btree.cpp::btree at line 293; the native global
// backend; not a batch T2 cell).
fun btreeDaf(op: Int, key: CharArray, bd: CharArray, sv: DafMsv): Int =
    throw NotImplementedError(
        "btree: out-of-scope owner (interp-native-btree.cpp::btree at line 293; not a batch T2 cell)")

fun MglobalDaf(op: Int, key: CharArray, bd: CharArray, sv: DafMsv): Int {

    // if (g == CLOSE) return 1;
    if (op == CLOSE_D) return 1

    // if (g == RETRIEVE) { keyfix(key); return btree(g,key,bd,svPtr); }
    if (op == RETRIEVE_D) {
        keyfixDaf(key)
        return btreeDaf(op, key, bd, sv)
    }

    // if (g == STORE) { keyfix(key); return btree(g,key,bd,svPtr); }
    if (op == STORE_D) {
        keyfixDaf(key)
        return btreeDaf(op, key, bd, sv)
    }

    // return btree(g,key,bd,svPtr);
    return btreeDaf(op, key, bd, sv)
}

// Reference-build config note (row 39): the SQLITE-arm local decls
//   void unpad(...); void sigint(int); static int CloseFlg=0;
//   void flush(MSV *);
// are dead decls in the live arm (no call site inside the SQLITE
// body); the native #else arm (the RETRIEVE/STORE keyfix + btree
// plus the NEXT/ORDER/PREVIOUS prepared-key section) is dead on the
// reference build because interp-globalb.cpp defines SQLITE at
// line 45, and is carried as this comment fold only.
