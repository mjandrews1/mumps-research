package daf

/*
 * DAF batch-9 unit 273 (roster row 273; interp-native-btree.cpp::
 * <misc>, residue, LOC 84, score 11.2). Consult
 * 2026-11-24T22:50:00Z.
 *
 * This roster row is the native btree TU FILE PRELUDE plus the
 * fold-in helper tail (memxmove, strxxcpy, flush, strxcmp,
 * allocblk). freadx (the unit-208 seat) and pb (the unit-106
 * render) stay where they landed; btreeX is ONLY a prototype in
 * this chunk - the 502-LOC body is roster row 275 of THIS batch
 * (seat raised there, not here).
 *
 * PRELUDE LEDGER: the June 19 2025 banner, the Fortran-heritage
 * note, SQLITE/CSTR/OK/BAD defines (SQL_OK_D/SQL_BAD_D pins),
 * the NATIVE/SHARED banner, MODIFIED 1 / UNMODIFIED 0
 * (BTR_MODIFIED_D/BTR_UNMODIFIED_D, unit-208 pins), LSEEK/
 * OFF_T folds (the RandomAccessFile seat), the bfr[] cache
 * (the unit-208 sparse-map fold), DumpBlocks define (pb: the
 * unit-106 render), the sigint prototype (sigintDaf), and the
 * file-scope BigKey/BigKeyBlock/FoundBlock/Reinsert/
 * BigKeyParent/BigKeyParentOffset/BigKeyOffset/BigKeyLower/
 * FoundOffset/BigKeyData block-walk statics - PINNED here for
 * the btreeX body of row 275 (rows 6/22 top-level state).
 *
 * FOLD-IN BODIES (row-33 pointer folds: C pointers render as
 * (buffer, offset) pairs; the 4-byte int reads/writes are the
 * explicit little-endian assembly of the Pb precedent,
 * DOCUMENTED DIVERGENCE: reference build x86-64 LE):
 * - memxmove(to,from,limit,end): inclusive copy from..limit,
 *   zero-fill to < end.
 * - strxxcpy(a,b): the strcpy loop; returns the b tail offset.
 * - flush: the modified-block dump over the sparse cache, the
 *   root 777 proper-close mark, the super-block dump (the
 *   vendor's NULL-slot deref over bfr[is] is the sparse-map
 *   fold: only live slots iterate - the H-013 vendor-UB
 *   divergence ledgered, the unit-208 divergence-(1) same
 *   note).
 * - strxcmp: the modified comparison scanning p2 to its NUL on
 *   the p1<p2 arm (r = the after-NUL offset of the second
 *   argument, returned as the Int tail).
 * - allocblk: the last-record bump arm over the ADSIZE slot.
 * H-015 hygiene: no raw dollars ride the comments.
 * Dafny: EXEMPT (file-block helpers; journal row).
 */

// ---- file-scope block-walk statics (rows 6/22; for row 275) ----
var BigKeyD = CharArray(STR_MAX_DAF) // unsigned char BigKey[STR_MAX]
var BigKeyBlockD = 0u                // unsigned int BigKeyBlock
var FoundBlockD = 0u                 // unsigned int FoundBlock
var ReinsertD = 0u                   // unsigned int Reinsert
var BigKeyParentD = 0u               // unsigned int BigKeyParent
var BigKeyParentOffsetD = 0u        // unsigned int BigKeyParentOffset
var BigKeyOffsetD = 0                // int BigKeyOffset
var BigKeyLowerD = 0                 // int BigKeyLower
var FoundOffsetD = 0                 // int FoundOffset
var BigKeyDataD = 0L                 // OFF_T BigKeyData
// extern int DBSem - the MpstimeMisc DBSemD pin.

// inline void memxmove(char *to, char *from, char *limit, char *end)
fun memxmoveD(to: CharArray, toOff: Int, from: CharArray, fromOff: Int,
              limitOff: Int, endOff: Int) {
    var t = toOff
    var f = fromOff
    while (f <= limitOff) { // while (from <= limit) *(to++) = *(from++);
        to[t] = from[f]
        t++
        f++
    }
    while (t < endOff) { // while (to < end) *to++ = 0;
        to[t] = NUL_D
        t++
    }
}

// inline char * strxxcpy(char *a, char *b) - returns the b tail
// offset (the C returned pointer at b's NUL).
fun strxxcpyD(a: CharArray, aOff: Int, b: CharArray, bOff: Int): Int {
    var q = aOff
    var w = bOff
    while (b[w] != NUL_D) { // while ((*(a) = *(b))) { a++; b++; }
        a[q] = b[w]
        q++
        w++
    }
    a[q] = NUL_D
    return w
}

// void flush(struct MSV *svPtr)
fun flushD(svPtr: DafMsv) {
    // for (is=0; is<GBLBUF; is++) dump modified global buffers
    // (the sparse-cache fold: live slots only)
    flushDumpModifiedBlocks()

    // int *root; freadx((unsigned char **)&root, 12L, MODIFIED);
    val root = freadxD(12, BTR_MODIFIED_D)
    val v = 777 // *root = 777L;  mark as proper close
    root.block[root.off] = (v and 0xff).toChar()
    root.block[root.off + 1] = ((v shr 8) and 0xff).toChar()
    root.block[root.off + 2] = ((v shr 16) and 0xff).toChar()
    root.block[root.off + 3] = ((v shr 24) and 0xff).toChar()

    // LSEEK(gblfd, 0, SEEK_SET); write(gblfd, bfr[0]->gblbf, MBLOCK);
    val super0 = bfrGetD(0)
    gblfdD?.seek(0)
    gblfdD?.let { raf -> writeBlockD(raf, super0.gblbf) }
    super0.gblmod = BTR_UNMODIFIED_D
}

// the modified-buffer dump loop of flush (package-private for the
// sparse-cache fold; iterates ONLY live slots).
fun flushDumpModifiedBlocks() {
    bfrEachD { slot ->
        if (slot.gblmod > 0) {
            gblfdD?.seek((slot.gblbfx.toLong()) shl PAGE_SHIFT_D)
            gblfdD?.let { raf -> writeBlockD(raf, slot.gblbf) }
            slot.gblmod = 0
        }
    }
}

// inline int strxcmp(p1,p2,&r) - returns (cmp, rOff): r the
// after-NUL offset of p2 on the non-less arms.
fun strxcmpD(p1: CharArray, p1Off: Int, p2: CharArray, p2Off: Int): Pair<Int, Int> {
    var a = p1Off
    var b = p2Off
    while (p1[a] == p2[b] && p1[a] != NUL_D) { // while (*p1==*p2 && *p1)
        a++
        b++
    }

    if (p1[a] < p2[b]) return Pair(-1, 0) // if (*p1 < *p2) return -1

    // *r = index(p2, 0) + 1;  (the after-NUL tail of p2)
    var e = p2Off
    while (p2[e] != NUL_D) e++
    val r = e + 1

    if (p1[a] == p2[b]) return Pair(0, r) // if (*p1 == *p2) return 0
    return Pair(1, r)
}

// inline int allocblk(struct MSV *svPtr)
fun allocblkD(svPtr: DafMsv): Int {
    // int *lastrec; freadx((unsigned char **)&lastrec, ADSIZE, MODIFIED);
    val lr = freadxD(ADSIZE_D, BTR_MODIFIED_D)
    var lastrec = btreeReadIntLE(lr.block, lr.off)

    // rec = *lastrec; rec = rec << PAGE_SHIFT; rec = rec + MBLOCK;
    var rec = lastrec.toLong() shl PAGE_SHIFT_D
    rec += MBLOCK_D
    // *lastrec = (int)(rec >> PAGE_SHIFT);
    lastrec = (rec shr PAGE_SHIFT_D).toInt()
    btreeWriteIntLE(lr.block, lr.off, lastrec)

    // freadx(&block, *lastrec, MODIFIED);
    freadxD(lastrec, BTR_MODIFIED_D) // block seat: the load side effect

    return lastrec
}

// 4-byte little-endian assembly (the Pb row-33 precedent).
fun btreeReadIntLE(b: CharArray, off: Int): Int =
    (b[off].code) or (b[off + 1].code shl 8) or
        (b[off + 2].code shl 16) or (b[off + 3].code shl 24)

fun btreeWriteIntLE(b: CharArray, off: Int, v: Int) {
    b[off] = (v and 0xff).toChar()
    b[off + 1] = ((v shr 8) and 0xff).toChar()
    b[off + 2] = ((v shr 16) and 0xff).toChar()
    b[off + 3] = ((v shr 24) and 0xff).toChar()
}
