package doc

// Unit 209 (B7 tranche 3): interp-native-btree.cpp::freadx (batch 7,
// complex stratum; units.csv row 209: loc 27, score 56.1). The
// native-btree block reader: the unsigned char double-pointer out
// param *buf (the caller's alias into the 2048-byte block image)
// renders as the ByteArray return contract of the prior
// Stubs.freadx seat (the seat flips to the delegate below, H-014
// seat-conversion precedent, Stubs.SymFree): the caller receives
// the block-image copy - scanblocks' read-only walk and pb's
// printer never write through the alias, so the value copy is
// faithful (H-007 buffer-role note). The gblfd int fd maps to the
// RandomAccessFile carrier seat (H-001 fd to RandomAccessFile;
// BtreeStatic.gblFile, EDIT-ONLY seat added to BtreeH.kt by this
// unit in the H-004 seat family; the -1 fd sentinel rides the
// null carrier). LSEEK is interp-native-btree.cpp:59 lseek over
// OFF_T off_t (:60) - RandomAccessFile.seek in the Java long byte
// domain, the vendor block shift r<<PAGE_SHIFT kept; read/write
// of MBLOCK bytes render as the carrier readFully/write with a
// ByteArray shuttle (H-012 byte domain). The vendor static
// bfr[GBLBUF] buffers table (interp-native-btree.cpp:64, an
// interp-native-btree.cpp file-scope, NOT the btree.h:293
// commented fossil) rides the object BtreeBlockCache
// lazy-allocation carrier: the vendor pre-allocates and :380
// invalidates gblbfx to -1; this render allocates lazily at the
// -1 unloaded sentinel (H-013 dead-local family note: the same
// shape). The function-static int indx (vendor static storage)
// renders as the FreadxState object var. bfr[0] gblbfx<0L
// root-load test, the gblmod=1 root mark, the cache-hit
// gblmod |= flag (the commented-out predecessor line kept as a
// comment in the vendor, including its statistics comment), the
// modified-block write-back before the reload, and the r&AMASK+1
// modulo index walk all kept verbatim. H-010: r, flag, indx Int;
// gblbfx is the Int seat of BtreeBuffers (vendor long block
// number narrows; H-010 promotion note carried by the
// BtreeBuffers declaration, unit 55). read() byte counts are
// unchecked in the vendor; the carrier readFully raises on short
// read (H-007 divergence ledger: the vendor's silent short read
// is UB anyway). MODIFIED 1 is interp-native-btree.cpp:55,
// declared here (UNMODIFIED 0 is the BtreeExterns unit-50 seat).
// Naming per H-017 (freadx kept verbatim).

const val MODIFIED = 1 // interp-native-btree.cpp:55 (UNMODIFIED is the
// BtreeExterns unit-50 seat)

// The vendor function-static int indx (static storage persists
// across calls; assigned at entry and read within the call).
object FreadxState {
    var indx: Int = 0
}

// interp-native-btree.cpp:64 struct buffers *bfr[GBLBUF] = {0} -
// the block-pointer cache table. Lazily allocated carrier: the
// vendor table is {0}-nulled and seeded by the open unit of a
// later batch; every fetched seat is born at the vendor :380
// invalidate-sentinel gblbfx -1 (unloaded), which the root test
// and the cache-hit test read.
object BtreeBlockCache {
    val bfr = arrayOfNulls<BtreeBuffers>(GBLBUF)

    fun slot(i: Int): BtreeBuffers {
        val b = bfr[i]
        if (b != null) return b
        val nb = BtreeBuffers()
        nb.gblbfx = -1 // vendor :380 invalidate sentinel
        bfr[i] = nb
        return nb
    }
}

// gblfd carrier backstop: the -1 fd sentinel rides the null
// carrier until the opening unit assigns it (the ?: throw shape,
// T2 RsltCallback name0 backstop-binding precedent).
private fun gblraf(): java.io.RandomAccessFile =
    BtreeStatic.gblFile
        ?: throw IllegalStateException(
            "gblFile: btree block file not opened (vendor gblfd -1 sentinel)")

// inline void freadx(unsigned char **buf, int r, int flag) - the
// *buf alias renders as the ByteArray return (seat contract).
fun freadx(r: Int, flag: Int): ByteArray {

    if (r < 16) { /* root block */

        val root = BtreeBlockCache.slot(0)
        root.gblmod = 1

        if (root.gblbfx < 0) { /* load root */
            // LSEEK(gblfd, (OFF_T)0, SEEK_SET); read(gblfd, ...);
            val raf = gblraf()
            raf.seek(0L)
            raf.readFully(root.gblbf, 0, MBLOCK)

            root.gblbfx = 0
            // *buf = bfr[0]->gblbf + r - the offset-r alias
            // renders as the offset-r tail copy (r..MBLOCK).
            return root.gblbf.copyOfRange(r, MBLOCK)
        }

        // *buf = (unsigned char *) bfr[0]->gblbf + r;
        return root.gblbf.copyOfRange(r, MBLOCK)
    }

    FreadxState.indx = (r and AMASK) + 1
    val b = BtreeBlockCache.slot(FreadxState.indx)

    if (b.gblbfx == r) {
        //            if (flag) bfr[indx]->gblmod = 1;
        b.gblmod = b.gblmod or flag
        // *buf = (unsigned char *) bfr[indx]->gblbf;
        // svPtr->hit++; // statistics
        return b.gblbf.copyOf()
    }

    if (b.gblmod != 0) { /* write modified block */
        val raf = gblraf()
        raf.seek(b.gblbfx.toLong() shl PAGE_SHIFT)
        raf.write(b.gblbf, 0, MBLOCK)
    }

    // LSEEK(gblfd, (OFF_T)(r) << PAGE_SHIFT, SEEK_SET);
    // read(gblfd, bfr[indx]->gblbf, MBLOCK);
    val raf = gblraf()
    raf.seek(r.toLong() shl PAGE_SHIFT)
    raf.readFully(b.gblbf, 0, MBLOCK)

    // *buf = (unsigned char *) bfr[indx]->gblbf;
    val out = b.gblbf.copyOf()
    b.gblbfx = r
    b.gblmod = flag
    return out
}
