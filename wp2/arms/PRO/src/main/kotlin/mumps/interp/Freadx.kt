package mumps.interp

import java.io.RandomAccessFile

/**
 * PRO-arm translation of interp-native-btree.cpp::freadx (B7
 * tranche 2, unit 193, rank 243). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 111-151 (inline
 * definition at line 111).
 *
 * Identity verified against the study tool: interp-native-btree.
 * cpp::freadx, cpp, loc 27, comment_ratio 0.182,
 * pointer_marks 23, control_marks 5, score 56.1 - exact match to
 * wp1/study-subset/units.csv row 193 (batch 7). RESOLVES the
 * InterpStubs.kt freadx h014 stub (the unsigned char** out-param
 * becomes the return value; the Scanblocks.kt call site binds
 * unchanged).
 *
 * freadx(&buf, r, flag): the block-cache reader - block numbers
 * below 16 are the root/super block (bfr[0]); others mask through
 * AMASK into the 65537-slot pool, writing a modified victim block
 * back before loading the requested one. flag (MODIFIED 1 /
 * UNMODIFIED 0) sets the loaded block's dirty bit.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param  : the raw POSIX edges (LSEEK/read/write
 *    on the file-scope gblfd) -> a java.io.RandomAccessFile on the
 *    BtreeFile carrier (the BtreeHeaderState.gblfd Int keeps the
 *    vendor fd number for parity; no OS fd exists in the JVM).
 *  - h007 char_buffer      : the MBLOCK 2048-byte block buffers
 *    ride NUL-domain CharArrays on the Buffers pool.
 *  - h008 static_scratch   : the vendor's static int indx is
 *    written before every read (never observed across calls) - a
 *    local here; the bfr[GBLBUF] pool and gblfd are FILE-SCOPE
 *    statics (btree.h:302-312) - the BtreeBufferState object.
 *  - h012 unsigned_byte_domain: block reads/writes ride the
 *    CharArray byte domain (BtreeH.kt convention).
 *  - h013 dead_store       : the commented-out statistics line
 *    (svPtr->hit++) stays a comment (vendor source, kept).
 *  - h014 out_of_scope_call: Buffers (BtreeH.kt, btree.h:296-300);
 *    the GBLBUF/AMASK/MBLOCK/PAGE_SHIFT consts re-declared
 *    file-private per house convention (BtreeH.kt:52-65 values).
 *  - h015 grammar_collision: new top-levels freadx (stub-seat
 *    re-bind), BtreeBufferState, BtreeFile - scanned clean
 *    cross-file; the root arm's interior-pointer return is a
 *    copyOfRange (see divergence note below).
 *  - h017 naming           : freadx keeps the vendor name.
 *  - h018 reference_build_visibility: no ifdefs in this body.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - DIVERGENCE (root arm): the C sets *buf = bfr[0]->gblbf + r -
 *    an interior pointer ALIASING the pool block; the CharArray
 *    return carrier has no interior-pointer form, so the root arm
 *    returns a copyOfRange of the tail (bfr[0].gblbf[r..MBLOCK)).
 *    A caller that writes through the returned C pointer mutates
 *    the pool block; the copy does not - journaled as the one
 *    observable divergence of this render (the non-root arms
 *    return the pool buffer ITSELF, so their aliasing semantics
 *    are preserved exactly).
 *  - read() short counts are ignored by the vendor - kept
 *    (RandomAccessFile.read return discarded).
 *  - r < 16 marks the root WITHOUT masking (the vendor trusts the
 *    caller's block numbering) - kept.
 */

private const val MBLOCK = 2048 // btree.h:165/167 (BtreeH.kt:52 value)
private const val PAGE_SHIFT = 11 // MBLOCK 2048 -> shift 11 (BtreeH.kt:59)
private const val GBLBUF = 65537 // number of internal buffers (BtreeH.kt:60)
private const val AMASK = 0xffff // GBLBUF 65537 -> 0xffff (BtreeH.kt:65)

/**
 * h008: the btree.h file-scope bfr[GBLBUF] pool (the commented-out
 * declaration noted on BtreeH.kt:79 - this unit is its first
 * carrier need).
 */
object BtreeBufferState {
    val bfr: Array<Buffers> = Array(GBLBUF) { Buffers() }
}

/**
 * h001: the gblfd RandomAccessFile carrier (the POSIX fd's JVM
 * analog; BtreeHeaderState.gblfd keeps the vendor fd number for
 * parity).
 */
object BtreeFile {
    var raf: RandomAccessFile? = null

    /** B9 row 250: the gbldat data-file carrier (vendor fd pair
     * gblfd/gbldat; UKEY/UDAT files, btreeX auto-open). */
    var datRaf: RandomAccessFile? = null
}

/**
 * vendor 111: inline void freadx(unsigned char **buf, int r, int
 * flag) - the stub-era return-value carrier replaces the
 * out-parameter.
 */
fun freadx(r: Int, flag: Int): CharArray {
    val f = BtreeFile.raf ?: error("gblfd null carrier")

    if (r < 16) { /* root block */

        BtreeBufferState.bfr[0].gblmod = 1

        if (BtreeBufferState.bfr[0].gblbfx < 0) { /* load root */
            f.seek(0)
            readBlock(f, BtreeBufferState.bfr[0].gblbf)
            BtreeBufferState.bfr[0].gblbfx = 0
            return BtreeBufferState.bfr[0].gblbf.copyOfRange(r, MBLOCK)
        }

        return BtreeBufferState.bfr[0].gblbf.copyOfRange(r, MBLOCK)
    }

    // vendor: static int indx; (h008 note - local here)
    val indx = (r and AMASK) + 1

    if (BtreeBufferState.bfr[indx].gblbfx == r) {
        // vendor: if (flag) bfr[indx]->gblmod = 1;  [superseded by |= flag]
        BtreeBufferState.bfr[indx].gblmod = BtreeBufferState.bfr[indx].gblmod or flag
        return BtreeBufferState.bfr[indx].gblbf
        // svPtr->hit++; // statistics (commented out in vendor - kept)
    }

    if (BtreeBufferState.bfr[indx].gblmod != 0) { /* write modified block */
        f.seek(BtreeBufferState.bfr[indx].gblbfx.toLong() shl PAGE_SHIFT)
        writeBlock(f, BtreeBufferState.bfr[indx].gblbf)
    }

    f.seek(r.toLong() shl PAGE_SHIFT)
    readBlock(f, BtreeBufferState.bfr[indx].gblbf)
    BtreeBufferState.bfr[indx].gblbfx = r
    BtreeBufferState.bfr[indx].gblmod = flag
    return BtreeBufferState.bfr[indx].gblbf
}

/** h001 edge bridge: MBLOCK bytes into the NUL-domain CharArray. */
private fun readBlock(f: RandomAccessFile, buf: CharArray) {
    val scratch = ByteArray(MBLOCK)
    f.readFully(scratch) // short counts not observable here
    for (k in 0 until MBLOCK) buf[k] = (scratch[k].toInt() and 0xff).toChar()
}

/** h001 edge bridge: the NUL-domain CharArray out as MBLOCK bytes. */
private fun writeBlock(f: RandomAccessFile, buf: CharArray) {
    val scratch = ByteArray(MBLOCK)
    for (k in 0 until MBLOCK) scratch[k] = (buf[k].code and 0xff).toByte()
    f.write(scratch)
}
