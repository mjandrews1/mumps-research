package both

// Unit 273 (BOTH B9, core): interp-native-btree.cpp::<misc>
// (84 LOC). Batch_plan ordinal 273: core, LOC 84. The NATIVE/
// SHARED misc region between the section banners: the OK/BAD
// defines, the MODIFIED/UNMODIFIED defines, the bfr[GBLBUF]
// buffers-table initializer (the landed unit-192 BtreeRuntime
// seat, cross-referenced NOT duplicated), the BigKey-family
// file-scope externs (SEATED this render in BtreeRuntime - the
// BtreeFreadx.kt edit, interp-native-btree.cpp:70-80 pins), the
// extern int DBSem (the MpstimeRuntime.DBSem seat, unit-272),
// the freadx/flush/strxcmp/scanblocks/allocblk/btreeX banner
// fossils (the btreeX forward declaration is the unit-275 pin;
// scanblocks rides its own roster owner), the sigint extern and
// the DumpBlocks pb dumper banners (comment fossils, h004),
// and the five inline bodies: memxmove, strxxcpy, flush,
// strxcmp, allocblk.
//
// BYTE-DOMAIN ADJUDICATION (h007): the btree block image is
// the ByteArray domain of Buffers.gblbf (unit 192); the vendor
// char* pointer arithmetic of memxmove/strxxcpy/strxcmp renders
// on the BPtr(buf, off) file-local pointer-holder idiom (the
// vendor Fortran odd-array-usage banner cited). strxcmp's
// unsigned char** out-param r renders as the IntArray[1]
// out-seat (the h006 out-arg holder precedent); the comparisons
// are unsigned-byte (and 0xff masks, the h010 promotion).
//
// Vendor behavior:
//   - memxmove copies the source region (from..limit inclusive)
//     and zero-fills the destination tail (to..end exclusive).
//   - strxxcpy copies b onto a through the NUL, returning the
//     offset of b's NUL (the vendor return-b pointer fold).
//   - flush dumps every modified buffer (lseek to gblbfx<<
//     PAGE_SHIFT, write MBLOCK, gblmod=0), then marks the
//     super block: freadx(&root,12,MODIFIED); *root=777L (the
//     proper-close mark at root-block offset 12), then dumps
//     the super block and resets bfr[0].
//   - strxcmp scans while bytes match, returns -1 when p1<p2,
//     otherwise sets *r to the post-NUL offset of p2 (+1),
//     returns 0 on equal, else 1 (the scan-to-end-of-second
//     quirk: the *r store runs BEFORE the equality return).
//   - allocblk reads the ADSIZE last-record slot through
//     freadx(ADSIZE, MODIFIED), advances the record by MBLOCK
//     (the rec<<PAGE_SHIFT; rec+MBLOCK; >>PAGE_SHIFT ladder),
//     writes it back, preloads the new block via freadx, and
//     returns the record.
//
// Quirks kept (ledgered):
//   - flush's bfr[is] walk derefs the table entries with no
//     NULL guard in the vendor (the table is all-NULL until
//     the open-time malloc, unit-275 owner); the arm keeps the
//     !! NPE-as-vendor-crash quantization (the unit-192
//     precedent).
//   - strxcmp's *r store happens even on the return-0 path
//     (the vendor sequence kept verbatim).
//   - allocblk's OFF_T rec math renders in the Long domain;
//     the (int) cast on the return kept on the Int seat; the
//     preload block cursor is unread (h013, ledger).
//   - the commented-out if (*p1 == *p2) return 0; line inside
//     strxcmp stays a vendor comment (kept, not a seat).
//   - the freadx prototype banner block (commented out) and
//     the pb DumpBlocks banners are h004 fossils.
//
// BOTH consults (find_rule before render, events logged):
//   h004 include_only - reused: the June 19 2025 banner, the
//     SQLITE/CSTR/OK/BAD define block and the include block
//     ride the banner ledger (the OK/BAD defines seat below as
//     OK_NB/BAD_NB).
//   h006 out_arg - hit: strxcmp's unsigned char** r is the
//     IntArray[1] out-seat.
//   h007 char_buffer - reused: the ByteArray/BPtr idiom above.
//   h010 numeric_promotion - hit: the unsigned-byte compares
//     mask with and 0xff.
//   h013 dead_store - hit: the allocblk preload cursor; the
//     MODIFIED/UNMODIFIED defines both seat (UNMODIFIED is the
//     0-value half, cited).
//   h014 out_of_scope_call - reused: freadx rides the landed
//     unit-192 seat (the root arm returns BufAt(gblbf, r), so
//     the *root/*lastrec int reads/writes go through the
//     returned alias - no copy fold needed, unlike the DOC
//     arm's unit-209 carrier); DBSem cites the MpstimeRuntime
//     seat; sigint rides the unit-190 seat (banner only here).
//   h015 grammar_collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h016 idiom_adaptation - hit: OK/BAD rename to OK_NB/BAD_NB
//     (the OK lexeme clashes with the zfcn-family seats);
//     memxmove/strxxcpy/strxcmp/allocblk/flush all valid
//     Kotlin, kept verbatim. Journaled.
//   h017 naming - reused: vendor names kept (the BPtr holder
//     is the render idiom, named per the DOC-arm twin of this
//     unit).
//   h019 class_member_carrier - reused: the BigKey extern
//     family seats in BtreeRuntime (the unit-192 carrier,
//     owner-pinned here); DBSem in MpstimeRuntime.

internal const val OK_NB = 1 // vendor #define OK 1 (interp-native-btree.cpp)
internal const val BAD_NB = 0 // vendor #define BAD 0
internal const val MODIFIED = 1 // #define MODIFIED 1
internal const val UNMODIFIED = 0 // #define UNMODIFIED 0
internal const val ADSIZE = 8 // btree.h:51 (the reference-build value)

// the file-local char* pointer holder (buf, off); the vendor
// pointer arithmetic renders as off advancement (h016).
internal class BPtr(val buf: ByteArray, var off: Int)

// Little-Endian int at the block-image offset (the *root=777
// and *lastrec int seats)
internal fun leIntSet(buf: ByteArray, offc: Int, v: Int) {
    buf[offc] = (v and 0xff).toByte()
    buf[offc + 1] = ((v shr 8) and 0xff).toByte()
    buf[offc + 2] = ((v shr 16) and 0xff).toByte()
    buf[offc + 3] = ((v shr 24) and 0xff).toByte()
}

internal fun leInt(buf: ByteArray, offc: Int): Int =
    (buf[offc].toInt() and 0xff) or
        ((buf[offc + 1].toInt() and 0xff) shl 8) or
        ((buf[offc + 2].toInt() and 0xff) shl 16) or
        ((buf[offc + 3].toInt() and 0xff) shl 24)

// inline void memxmove(char *to, char *from, char * limit,
// char *end) - copy the source region and zero-fill the tail.
internal fun memxmove(to: BPtr, from: BPtr, limitIx: Int, endIx: Int) {
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

// inline char * strxxcpy(char * a, char * b) - copy through the
// NUL; returns b's NUL offset (the pointer fold).
internal fun strxxcpy(a: BPtr, b: BPtr): Int {
    while (true) {
        a.buf[a.off] = b.buf[b.off]
        if (a.buf[a.off] == 0.toByte()) break
        a.off++
        b.off++
    }
    return b.off
}

// void flush(struct MSV * svPtr) - flush all internal buffers.
internal fun flush(svPtr: Msv) {
    // int is, *root;
    for (ix in 0 until GBLBUF) { // dump global buffers
        val b = BtreeRuntime.bfr[ix]!! // open-time alloc (unit-275 owner;
        // the !! NPE is the vendor NULL-deref crash, unit-192 precedent)
        if (b.gblmod > 0) {
            // LSEEK(gblfd, (OFF_T)(bfr[is]->gblbfx)<<PAGE_SHIFT,
            // SEEK_SET); write(gblfd, bfr[is]->gblbf, MBLOCK);
            val raf = BtreeRuntime.gblRaf
            try {
                raf?.seek(b.gblbfx.toLong() shl PAGE_SHIFT)
                raf?.write(b.gblbf, 0, MBLOCK)
            } catch (e: java.io.IOException) {
                // vendor: lseek/write fail silently (returns -1, ignored)
            }
            b.gblmod = 0
        }
    }

    // freadx((unsigned char **) &root, 12L, MODIFIED);
    // *root = 777L; - the root-arm alias write (BufAt returns
    // the live gblbf reference; the mark lands at offset 12)
    val root = freadx(12, MODIFIED)
    leIntSet(root.buf, root.off, 777) // mark as proper close

    // LSEEK(gblfd, (OFF_T)0, SEEK_SET); write(gblfd,
    // bfr[0]->gblbf, MBLOCK); - dump super block
    val raf = BtreeRuntime.gblRaf
    try {
        raf?.seek(0L)
        raf?.write(BtreeRuntime.bfr[0]!!.gblbf, 0, MBLOCK)
    } catch (e: java.io.IOException) {
        // vendor: fail silently
    }
    BtreeRuntime.bfr[0]!!.gblmod = 0
}

// inline int strxcmp(unsigned char *p1, unsigned char *p2,
// unsigned char **r) - the modified string comparison.
internal fun strxcmp(p1: BPtr, p2: BPtr, r: IntArray): Int {
    while (p1.buf[p1.off] == p2.buf[p2.off] &&
        p1.buf[p1.off] != 0.toByte()
    ) {
        p1.off++
        p2.off++
    }

    val a = p1.buf[p1.off].toInt() and 0xff
    val b = p2.buf[p2.off].toInt() and 0xff

    if (a < b) return -1

    // *r = (unsigned char *) index((char *) p2, 0) + 1;;
    // - the post-NUL offset of the p2 cursor (BEFORE the
    // equality return, the vendor sequence)
    var k = p2.off
    while (p2.buf[k] != 0.toByte()) k++
    r[0] = k + 1

    // if (*p1 == *p2) return 0;
    if (a == b) return 0

    // return 1;
    return 1
}

// inline int allocblk(struct MSV * svPtr)
internal fun allocblk(svPtr: Msv): Int {
    // int *lastrec; OFF_T rec; unsigned char *block;
    val lastrecB = freadx(ADSIZE, MODIFIED)
    var lastrec = leInt(lastrecB.buf, lastrecB.off)
    var rec: Long = lastrec.toLong()
    rec = rec shl PAGE_SHIFT
    rec = rec + MBLOCK
    lastrec = (rec shr PAGE_SHIFT).toInt()
    // freadx(&block, (*lastrec), MODIFIED); - the new-block
    // preload (the result block cursor unread, h013 ledger)
    freadx(lastrec, MODIFIED)
    // return (int) (*lastrec);
    return lastrec
}
