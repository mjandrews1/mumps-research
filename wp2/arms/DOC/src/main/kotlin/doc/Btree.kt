package doc

// Unit 4 (B1 tranche 1): interp-native-btree.cpp::btree (batch 1, simple
// stratum; LOC 18, score 5.4). The thin btree global-array dispatcher:
// the live arm is exactly "int i = btreeX(g,key,bd,svPtr); return i;".
// VENDOR EXCLUDED ARM (H-018, first DOC application): the whole
// #ifdef NATIVE_SHARED block is compiled OUT of the reference build
// (NATIVE_SHARED undefined): the no-slice DB close-and-return path and
// the DBSem alarm-lock path, both RESTATED (not translated) below as
// the mandatory H-018 comment ledger:
//   if (!0) { btreeX(CLOSE,t1,t2,svPtr); usleep(1); return i; }
//   if (DBSem) { btreeX(CLOSE,t1,t2,svPtr); DBSem = 0; usleep(0); }
// btreeX is out of scope per H-014 (not a roster unit; vendor body at
// interp-native-btree.cpp:319). Naming per H-017: btree kept verbatim
// (single lowercase word).

// btreeX seat (B9 T3 tag correction + FLIP, C-008 consult
// 2026-11-22T05:33:00Z, H-014 hit-adapt): the former TODO stub
// here tagged the owner "not a roster unit" - batch 9 row
// 250 lands the real body (BtreeX.kt, unit 270); this file
// keeps the dispatcher and cross-references the render, no
// second definition.

fun btree(g: Int, key: CharArray, bd: CharArray, sv: Msv): Int {
    val i = btreeX(g, key, bd, sv)
    return i
}
