package mumps.mdh

import mumps.interp.MsvState
import mumps.interp.STR_MAX

/**
 * Kotlin translation of libmpscpp.cpp::KiLL (Study Subset unit 37,
 * Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: void KiLL(char * gtmp, struct MSV * svPtr) (libmpscpp.cpp:7685-
 * 7706) — KILL a global node AND its descendants: keyfix the key, kill
 * the node, then walk XNEXT siblings sharing the key prefix.
 *
 * BUFFER ROLES: gtmp is a DUAL-ROLE buffer — read by Mglobal(GKILL),
 * OVERWRITTEN by Mglobal(XNEXT) with each next key — so it stays a
 * StringBuilder parameter (callers pass StringBuilder(ref); the C
 * caller global::Kill passes the global's own ref buffer). tmp15 is
 * the Mglobal scratch/holder (heuristic 66); tmp14 is a snapshot of
 * the truncated key taken via strmove before the walk (C: value-copy
 * buffer -> immutable String local).
 *
 * SENTINEL BYTES: the trailing-key check compares against the C
 * subscript-separator byte 1 (defines.h family); built programmatically
 * as Char(1) — never as an escape literal (heuristics 43/80).
 * strncmp(tmp14, gtmp, _k) -> String.regionMatches(0, gtmp, 0, _k):
 * insufficient length yields false, matching C's NUL-vs-char nonzero.
 *
 * Mglobal: the String-key owner lives in MdhGlobalExtract.kt (unit
 * 32); KiLL needs the mutating role, so a StringBuilder-key overload
 * is added here (same C entity, mutability-role overload).
 */
const val GKILL = 3 // C: include/mumpsc/defines.h:167 #define GKILL 3
const val XNEXT = 8 // C: include/mumpsc/defines.h:177 #define XNEXT 8

/** C: int Mglobal(int op, unsigned char *key, unsigned char *bd, MSV *)
 *  — mutating-key role (XNEXT overwrites the key buffer with the next
 *  key); overload of the unit-32 String-key stub. */
fun mglobal(op: Int, key: StringBuilder, bd: StringBuilder, svPtr: MsvState): Int =
    TODO("Mglobal(StringBuilder-key role) not yet translated (native DB API, outside batch scope)")

/** C: include/mumpsc/keyfix.h — static inline void keyfix(unsigned
 *  char *vxd) — remove leading ^; convert high-bit marker bytes to 1.
 *  Header static-inline dependency of KiLL; translated in place (its
 *  only file is the include itself). Shift branch drops the ^ by
 *  left-shifting one position, converting high codes to 1 as it goes. */
fun keyfix(vxd: StringBuilder) {
    if (vxd.isNotEmpty() && vxd[0] == '^') {
        // C: while (*vxd) { *vxd = *(vxd+1) & 0x80 ? 1 : *(vxd+1); vxd++; }
        for (i in 0 until vxd.length - 1) {
            val c = vxd[i + 1]
            vxd[i] = if (c.code and 0x80 != 0) Char(1) else c
        }
        vxd.deleteCharAt(vxd.length - 1) // the shifted-in NUL shortens the string
        return
    }

    // C: while (*vxd) { if (*vxd & 0x80) *vxd = 1; vxd++; }
    for (i in vxd.indices) {
        if (vxd[i].code and 0x80 != 0) vxd[i] = Char(1)
    }
}

/** C: void KiLL(char * gtmp, struct MSV * svPtr) */
fun KiLL(gtmp: StringBuilder, svPtr: MsvState) {
    val tmp15 = StringBuilder(STR_MAX)

    keyfix(gtmp)

    var k = gtmp.length // C: _k=strlen(gtmp);

    // C: if (gtmp[_k-1]==1 && gtmp[_k-2]==1) gtmp[_k-- -1]=0;
    if (k >= 2 && gtmp[k - 1] == Char(1) && gtmp[k - 2] == Char(1)) {
        gtmp.deleteCharAt(k - 1)
        k--
    }

    // C: strmove((unsigned char *)tmp14,(unsigned char *) gtmp);
    val tmp14 = gtmp.toString()

    mglobal(GKILL, gtmp, tmp15, svPtr)

    while (true) { // C: while(1) { ... }
        val j = mglobal(XNEXT, gtmp, tmp15, svPtr)

        // C: if (_j && strncmp((char *)tmp14,gtmp,_k) == 0) { ...; continue; }
        if (j != 0 && tmp14.regionMatches(0, gtmp.toString(), 0, k)) {
            mglobal(GKILL, gtmp, tmp15, svPtr)
            continue
        }
        break
    }
}
