package doc

// Unit 272 (B9 tranche 3): interp-native-btree.cpp::<misc>
// (batch 9, complex stratum; units.csv row 252: loc 84, score
// 60.1, rank 296; physLOC 84 physically verified this session)
// - the native/shared misc region between the section banners:
// the OK/BAD defines, the bfr[GBLBUF] buffers-table extern
// (the landed BtreeBlockCache unit-209 seat, cross-referenced
// NOT duplicated), the BigKey-family unsigned externs (the
// landed BtreeExterns.kt unit-50/209 seats; this tranche ADDS
// the missing FoundBlock/Reinsert/FoundOffset triple there -
// see that edit), the freadx/flush/strxcmp/scanblocks/
// allocblk/btreeX banner fossils and the sigint extern, the
// DumpBlocks pb dumper banners, and the four inline bodies:
// memxmove, strxxcpy, flush, strxcmp, allocblk. Consult H-004
// hit-reuse (2026-11-22T05:36:00Z): the banner pulls ride the
// Fossils-not-fixed rule (the June 19, 2025 banner kept
// verbatim; the Fortran-odd-array-usage banner too).
//
// BYTE-DOMAIN ADJUDICATION (H-012): the btree block image is
// the ByteArray domain of BtreeBuffers.gblbf (unit 55) and
// BigKey (unit 50); the vendor char* pointer arithmetic of
// memxmove/strxxcpy/strxcmp renders on the BPtr(buf, off)
// file-local pointer-holder idiom (H-016; the vendor Fortran
// odd-array-usage banner cited). strxcmp's unsigned char**
// out-param r renders as the IntArray[1] out-seat (the H-006
// out-arg holder precedent); the comparisons are unsigned-byte
// (and 0xff masks, H-010 promotion).
//
// flush (H-014 hit-adapt of the unit-209 carriers): the
// fwrite of a modified block is raf.write at gblbfx<<
// PAGE_SHIFT (Freadx write-back precedent); the vendor
// freadx((unsigned char **)\u0026root, 12L, MODIFIED); *root =
// 777L alias-write pair folds onto a DIRECT slot-0 write: the
// LE int 777 at super-block offset 12 (the alias returned a
// copy in the Java seat, so the write goes through
// BtreeBlockCache.slot(0).gblbf, then the super-block dump
// and bfr[0] gblmod reset, exactly the vendor sequence: the
// freadx(12,MODIFIED) consult is folded into the direct write
// because the unit-209 ByteArray seat returns a copy, not an
// alias - the H-007 buffer-role note). allocblk rides
// freadx(ADSIZE, MODIFIED): the *lastrec int renders as the
// LE 4-byte int at the returned image head (MBLOCK byte
// domain); the rec long shift math kept verbatim in the Long
// domain; the trailing freadx(*lastrec, MODIFIED) is the
// new-block preload kept verbatim; the vendor (int) cast on
// the return kept on the Int seat.
// Naming per H-017 (memxmove/strxxcpy/strxcmp/allocblk/flush
// all valid Kotlin, kept verbatim; OK/BAD become OK_NB/BAD_NB
// - the H-016 collision-disambiguation, OK clashes with the
// zfcn family seats).

const val OK_NB = 1 // vendor #define OK 1 (file-family public: the btreeX render
// consults it; the H-016 OK-collision disambiguation stands)
const val BAD_NB = 0 // vendor #define BAD 0 (public twin of OK_NB)

// class BPtr - the file-local char* pointer holder (buf, off);
// the vendor pointer arithmetic renders as off advancement.
class BPtr(val buf: ByteArray, var off: Int)

// file-local Little-Endian int at the block-image offset (the
// fwrite *root = 777L / *lastrec int seats)
private fun leIntSet(buf: ByteArray, offc: Int, v: Int) {
    buf[offc] = (v and 0xff).toByte()
    buf[offc + 1] = ((v shr 8) and 0xff).toByte()
    buf[offc + 2] = ((v shr 16) and 0xff).toByte()
    buf[offc + 3] = ((v shr 24) and 0xff).toByte()
}

private fun leInt(buf: ByteArray, offc: Int): Int =
    (buf[offc].toInt() and 0xff) or
        ((buf[offc + 1].toInt() and 0xff) shl 8) or
        ((buf[offc + 2].toInt() and 0xff) shl 16) or
        ((buf[offc + 3].toInt() and 0xff) shl 24)

// gblfd carrier backstop (the Freadx.kt twin, file-local here)
private fun gblrafNB(): java.io.RandomAccessFile =
    BtreeStatic.gblFile
        ?: throw IllegalStateException(
            "gblFile: btree block file not opened (vendor gblfd -1 sentinel)")

// inline void memxmove(char *to, char *from, char * limit,
// char *end) - copy the source region (from..limit inclusive)
// and zero-fill the destination tail (to..end exclusive);
// both walks advance the holders.
fun memxmove(to: BPtr, from: BPtr, limitIx: Int, endIx: Int) {
    while (from.off <= limitIx) {
        to.buf[to.off] = from.buf[from.off]
        to.off++
        from.off++
    }
    while (to.off < endIx) {
        to.buf[to.off] = 0
        to.off++
    }
}

// inline char * strxxcpy(char * a, char * b) - copy b onto a
// through the NUL; returns the offset of b's NUL (the vendor
// return b pointer-fold).
fun strxxcpy(a: BPtr, b: BPtr): Int {
    while (true) {
        a.buf[a.off] = b.buf[b.off]
        if (a.buf[a.off] == 0.toByte()) break
        a.off++
        b.off++
    }
    return b.off
}

// void flush(struct MSV * svPtr) - flush all internal buffers
// (the modified-block dump walk, the 777 proper-close mark at
// super-block offset 12, the super-block dump, bfr[0] reset).
fun flush(svPtr: Msv) {
    // int is, *root;
    for (ix in 0 until GBLBUF) { /* dump global buffers */
        val b = BtreeBlockCache.bfr[ix] ?: continue
        if (b.gblmod > 0) {
            // LSEEK(gblfd, (OFF_T)(bfr[is]->gblbfx)<<PAGE_SHIFT,
            // SEEK_SET); write(gblfd, bfr[is]->gblbf, MBLOCK);
            val raf = gblrafNB()
            raf.seek(b.gblbfx.toLong() shl PAGE_SHIFT)
            raf.write(b.gblbf, 0, MBLOCK)
            b.gblmod = 0
        }
    }

    // freadx((unsigned char **) &root, 12L, MODIFIED);
    // *root = 777L; - the H-007 direct slot-0 alias-write
    // fold (the ByteArray seat returns a copy; the mark goes
    // straight into the cached super block)
    val root = BtreeBlockCache.slot(0)
    leIntSet(root.gblbf, 12, 777) /* mark as proper close */

    // LSEEK(gblfd, (OFF_T)0, SEEK_SET); write(gblfd,
    // bfr[0]->gblbf, MBLOCK); - dump super block
    val raf = gblrafNB()
    raf.seek(0L)
    raf.write(root.gblbf, 0, MBLOCK)
    root.gblmod = 0
}

// inline int strxcmp(unsigned char *p1, unsigned char *p2,
// unsigned char **r) - the modified string comparison that
// scans to the end of the second argument if the first is
// less; r receives p2's post-NUL offset (IntArray[1] out-seat,
// H-006); signed-byte unsigned compares masked (H-010).
fun strxcmp(p1: BPtr, p2: BPtr, r: IntArray): Int {
    while (p1.buf[p1.off] == p2.buf[p2.off] &&
        p1.buf[p1.off] != 0.toByte()) {
        p1.off++
        p2.off++
    }

    val a = p1.buf[p1.off].toInt() and 0xff
    val b = p2.buf[p2.off].toInt() and 0xff

    if (a < b) return -1

    // *r = (unsigned char *) index((char *) p2, 0) + 1;; - the
    // post-NUL offset of the p2 cursor
    var k = p2.off
    while (p2.buf[k] != 0.toByte()) k++
    r[0] = k + 1

    // if (*p1 == *p2) return 0;
    if (a == b) return 0

    // return 1;
    return 1
}

// inline int allocblk(struct MSV * svPtr)
fun allocblk(svPtr: Msv): Int {
    // int *lastrec; OFF_T rec; unsigned char *block;
    val lastrecB = freadx(ADSIZE, MODIFIED)
    var lastrec = leInt(lastrecB, 0)
    var rec: Long = lastrec.toLong()
    rec = rec shl PAGE_SHIFT
    rec = rec + MBLOCK
    lastrec = (rec shr PAGE_SHIFT).toInt()
    // freadx(&block, (*lastrec), MODIFIED); - the new-block
    // preload (the result block cursor unused, H-013 vendor
    // tail kept as the call ledger)
    freadx(lastrec, MODIFIED)
    // return (int) (*lastrec);
    return lastrec
}
