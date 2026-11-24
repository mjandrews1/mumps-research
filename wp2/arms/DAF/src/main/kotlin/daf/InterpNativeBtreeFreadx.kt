package daf

// Unit 209 (DAF B7 T3, complex): interp-native-btree.cpp::freadx
// (27 LOC, score 56.1): the native b-tree block-cache fetcher -
// the root-pointer arm for r<16 and the AMASK-indexed cache arm
// with the modified-block write-back, LSEEK/read block loads, and
// the gblbfx/gblmod tag updates. TU kin of the unit-106 pb
// dumper (B4 T2): the raw block storage stays CharArray
// carriers and the at() end-guard discipline.
// Vendor values pinned from btree.h (the mumps-sqlite3-amd64
// shipped headers, verified this session): MBLOCK 2048,
// PAGE_SHIFT 11 (the MBLOCK 2048 ladder rung),
// GBLBUF 65537, AMASK 0xffff (the GBLBUF 65537 ladder rung);
// MODIFIED 1 / UNMODIFIED 0 and OFF_T=off_t / LSEEK=lseek pinned
// from interp-native-btree.cpp:55-59.
// Model divergences ledgered (all named):
// (1) the vendor's static pointer array bfr[GBLBUF] (65537
// block entries) would cost hundreds of MB as an eager Kotlin
// array; the render keeps a SPARSE map keyed by the vendor's
// indx slot. The slot arithmetic (indx = (r and AMASK) + 1) is
// kept verbatim and the map only materializes blocks the fetch
// path touches - the cache semantics (addressed slots) are
// preserved, the residency is lazy. bfr[0] is the root slot and
// materializes on first use, preserving the vendor's
// implicit-root-init read path.
// (2) the unsigned char* out-param *buf folds onto the returned
// BfrRef (block carrier + offset) - the pb precedent's
// pointer-as-carrier discipline.
// (3) read()/write() fold onto the java.io RandomAccessFile seat
// (gblfdD) with byte<->Char assembly at the 0xff mask - the
// reference build is little-endian raw bytes; the Chars carry
// the exact 0..255 codes (row 33 fold as in the unit-106
// little-endian assembly note).
// (4) the inline-local static int indx persists across calls
// in C but is ALWAYS overwritten before use (both paths assign
// it); it renders as a local var, ledger note.
// (5) the commented-out svPtr->hit++ statistics line stays a
// comment exactly as vendor.
// Consults (2026-11-24T20:11:30Z, nominal): rows 22/6 file-scope
// statics hit-adapt (bfr cache + gblfd the sparse-map/seat fold
// above), row 33 hit-adapt (raw byte assembly; lseek/read/write
// libc folds onto the RandomAccessFile seat of the Readf java.io
// precedent), row 8 hit-reuse (gblbf/gblbfx/gblmod the carrier
// roles of struct buffers, pinned verbatim), row 81 hit-reuse
// (at() end-guard reads), row 56 hit-adapt (the block carriers),
// rows 14/15 hit-reuse (the r<16 / gblbfx<0 / ==r compares),
// manual-decision (the sparse-cache and BfrRef folds; the
// gblmod |= flag OR-tag kept on the Int domain).
// Dafny: EXEMPT (native b-tree IO over the file seat; journal row).

const val PAGE_SHIFT_D = 11 // btree.h PAGE_SHIFT (MBLOCK 2048 rung)
const val GBLBUF_D = 65537  // btree.h GBLBUF
const val AMASK_D = 0xffff // btree.h AMASK (GBLBUF 65537 rung)
const val BTR_MODIFIED_D = 1   // interp-native-btree.cpp:55 MODIFIED
const val BTR_UNMODIFIED_D = 0 // :56 UNMODIFIED

// struct buffers { unsigned char gblbf[MBLOCK]; int gblbfx; int gblmod; }
class BfrBlockD {
    val gblbf = CharArray(MBLOCK_D)
    var gblbfx = -1 // the vendor leaves slots unallocated until first
                     // use; -1 models the fresh-load state (gblbfx<0 arm)
    var gblmod = BTR_UNMODIFIED_D
}

// the sparse bfr[] cache (divergence (1))
private val bfrD = HashMap<Int, BfrBlockD>()

// int gblfd - the native global file descriptor. Named-owner seat:
// the open() site and the close/refcount path are out of scope
// (btree TU init, not a corpus unit); the RandomAccessFile seat is
// the Readf java.io precedent fold. Null seat = the vendor's
// unopened-fd domain at a call that cannot occur on this render's
// callers; guarded like every other file seat of this arm.
var gblfdD: java.io.RandomAccessFile? = null

// the *buf out-param fold (divergence (2))
class BfrRef(val block: CharArray, val off: Int)

// inline void freadx(unsigned char **buf, int r, int flag)
fun freadxD(r: Int, flag: Int): BfrRef {

    if (r < 16) {                 /* root block */

        val root = bfrD.getOrPut(0) { BfrBlockD() }
        root.gblmod = BTR_MODIFIED_D // bfr[0]->gblmod=1

        if (root.gblbfx < 0L) {    /* load root */
            // LSEEK(gblfd, (OFF_T)0, SEEK_SET);
            gblfdD?.seek(0)
            // read(gblfd, bfr[0]->gblbf, MBLOCK)
            gblfdD?.let { raf -> readBlockD(raf, root.gblbf) }
            root.gblbfx = 0
            return BfrRef(root.gblbf, r) // *buf = bfr[0]->gblbf + r
        }

        return BfrRef(root.gblbf, r)
    }

    val indx = (r and AMASK_D) + 1 // indx = (r & AMASK) + 1

    val slot = bfrD.getOrPut(indx) { BfrBlockD() }

    if (slot.gblbfx == r) {
        //            if (flag) bfr[indx]->gblmod = 1;
        slot.gblmod = slot.gblmod or flag // bfr[indx]->gblmod |= flag
        return BfrRef(slot.gblbf, 0)      // *buf = bfr[indx]->gblbf
        // svPtr->hit++; // statistics (vendor comment kept)
    }

    if (slot.gblmod != 0) {       /* write modified block */
        // LSEEK(gblfd, (OFF_T)(bfr[indx]->gblbfx) << PAGE_SHIFT, SEEK_SET)
        gblfdD?.seek((slot.gblbfx.toLong() shl PAGE_SHIFT_D))
        // write(gblfd, bfr[indx]->gblbf, MBLOCK)
        gblfdD?.let { raf -> writeBlockD(raf, slot.gblbf) }
    }

    // LSEEK(gblfd, (OFF_T)(r) << PAGE_SHIFT, SEEK_SET)
    gblfdD?.seek(r.toLong() shl PAGE_SHIFT_D)
    // read(gblfd, bfr[indx]->gblbf, MBLOCK)
    gblfdD?.let { raf -> readBlockD(raf, slot.gblbf) }
    slot.gblbfx = r
    slot.gblmod = flag
    return BfrRef(slot.gblbf, 0)  // *buf = bfr[indx]->gblbf
}

// read(fd, buf, MBLOCK): byte -> 0..255 Char assembly (divergence 3)
private fun readBlockD(raf: java.io.RandomAccessFile, buf: CharArray) {
    val bytes = ByteArray(MBLOCK_D)
    val n = raf.read(bytes)
    var i = 0
    while (i < MBLOCK_D) {
        buf[i] = if (i < n) (bytes[i].toInt() and 0xff).toChar() else NUL_D
        i++
    }
}

// write(fd, buf, MBLOCK): 0..255 Char -> byte assembly
private fun writeBlockD(raf: java.io.RandomAccessFile, buf: CharArray) {
    val bytes = ByteArray(MBLOCK_D)
    var i = 0
    while (i < MBLOCK_D) {
        bytes[i] = (buf[i].code and 0xff).toByte()
        i++
    }
    raf.write(bytes)
}
