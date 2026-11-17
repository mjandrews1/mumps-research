package mumps.interp

/**
 * PRO-arm translation of interp-globalb.cpp::Mglobal (B6 tranche
 * B, unit 173, rank 119). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 70-93.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): Mglobal, cpp, loc 17,
 * comment_ratio 0.056, pointer_marks 2, control_marks 4,
 * score 12.3 - exact match to wp1/study-subset/units.csv row 174
 * (batch 6).
 *
 * Mglobal(g, key, bd, svPtr): the SQL-DB-backed global-array
 * dispatcher behind the transaction server's Mglobal call - the
 * SQLITE build's thin front door to the btree. This unit
 * RESOLVES the InterpStubs Mglobal stub raised by u161 (the
 * ServerMain.kt:237 call site binds unchanged on the
 * key-offset-carrying signature; the u161 KDoc tagged the owner
 * correctly, roster row 174).
 *
 * h018 reference_build resolution: the pair-#if defined(SQLITE)
 * - the reference build DEFINES SQLITE (config.log DBMS SQLITE,
 * u139 expx note) - the SQLITE arm is IN; the excluded non-SQLITE
 * arm (the btreeX on-disk paths with the unpad/flush machinery,
 * u50/u130 native-btree seats) is journaled, not rendered.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h012 unsigned_byte_domain: key/bd are C string byte
 *    domains; the key rides the offset carrier the u161 stub
 *    raised (the server passes the C string at &buff0[1]) - the
 *    per-file copyOfC adapter materializes the NUL-terminated
 *    key for the btree/keyfix seats that take a whole-buffer
 *    key at index 0 (u98 offset-adapter precedent).
 *  - h013 dead_code        : the three file-scope prototypes
 *    (unpad, sigint, flush, 72-76) and the static int CloseFlg
 *    (:75) are declaration-only in the SQLITE arm (their
 *    consumers live in the excluded non-SQLITE arm) - roster
 *    notes, not rendered.
 *  - h014 out_of_scope_call : keyfix resolves to the existing
 *    MdhStubs keyfix stub seat (owner keyfix.h inline, not a
 *    corpus unit; GlobalQuery.kt:133 copy/write-back adapter
 *    precedent); btree resolves to the NativeBtree.btree real
 *    seat (u50 native-btree object, delegating to the btreeX
 *    stub) - direct calls.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: the new top-level Mglobal - the removed
 *    InterpStubs stub clears its seat; the lowercase mglobal
 *    (global-array machinery, not a corpus unit) is a distinct
 *    vendor name, kept distinct.
 *  - h017 naming             : Mglobal keeps the vendor's
 *    capital-M spelling.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The CLOSE arm returns 1 WITHOUT calling btree - the
 *    server's dispatch loop reads the 1 as its exit code;
 *    kept verbatim (the SQLITE btree close is a no-op here,
 *    unlike the compiled-out NATIVE_SHARED guard family, u130).
 *  - RETRIEVE and STORE keyfix the key in place (via the
 *    copy/write-back adapter - the MdhStubs keyfix seat takes a
 *    whole buffer); the fall-through arm (XNEXT and friends)
 *    passes the key UNFIXED - kept.
 */
private const val RETRIEVE = 0 // vendor defines.h:83
private const val STORE = 1 // vendor defines.h:84
private const val CLOSE = 4 // vendor defines.h:87

fun Mglobal(g: Int, key: CharArray, keyOff: Int, bd: CharArray, svPtr: Msv): Int {
    if (g == CLOSE) return 1 // vendor: CLOSE returns 1 without btree

    if (g == RETRIEVE || g == STORE) {
        // vendor: keyfix(key); return btree(g,key,bd,svPtr);
        val kf = copyOfC(key, keyOff) // copy/write-back adapter (GlobalQuery precedent)
        mumps.mdh.keyfix(kf)
        writeBackC(key, keyOff, kf)
        return NativeBtree.btree(g, kf, bd, svPtr)
    }

    // vendor: return btree(g,key,bd,svPtr); (key unfixed)
    return NativeBtree.btree(g, copyOfC(key, keyOff), bd, svPtr)
}

/** NUL-terminated copy of the C string at off (u98 offset adapter;
 *  per-file private, Parse_.kt:246 precedent). */
private fun copyOfC(src: CharArray, off: Int): CharArray {
    val n = strlenC(src, off)
    val out = CharArray(n + 1)
    for (i in 0 until n) out[i] = src[off + i]
    out[n] = NUL
    return out
}

/** Write the fixed key back into the caller's offset carrier
 *  (the keyfix in-place contract). */
private fun writeBackC(dst: CharArray, dstOff: Int, src: CharArray) {
    for (i in src.indices) {
        if (src[i] == NUL) {
            dst[dstOff + i] = NUL
            return
        }
        dst[dstOff + i] = src[i]
    }
}
