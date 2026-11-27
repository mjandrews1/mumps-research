package both

// Unit 192 render: interp-native-btree.cpp::freadx (roster 192,
// batch 7, 27 LOC). The btree block-buffer fetch: root blocks
// (r < 16) read through buffer 0; data blocks hash to
// bfr[(r & AMASK) + 1], with the modified-block write-back and
// the block reload from the global file at r << PAGE_SHIFT.
//
// This is the FIRST interp-native-btree.cpp unit rendered in
// the arm (batches 1-6 did not touch this TU; the arm's own
// committed order leaves btree 28 / scanblocks 44 / pb 111
// pending in earlier batches). The render therefore seats the
// TU's shared carrier here, owner-pinned to the batch-9
// btreeX/misc owners:
//   - struct buffers (btree.h:247-250) -> class Buffers;
//   - bfr[GBLBUF] (interp-native-btree.cpp:64) and gblfd ->
//     BtreeRuntime.bfr / BtreeRuntime.gblRaf;
//   - the malloc of bfr[i] (interp-native-btree.cpp:373) and
//     the open() of gblfd (:399+) stay with their batch-9
//     owners (btreeX roster 249 / misc roster 251).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: no callable seats exist
//     for this TU yet (the helpers below are this render's own;
//     LSEEK is the interp-native-btree.cpp:59 lseek macro, and
//     read/write are libc calls, not corpus units).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h019 class_member_carrier - hit: the bfr/gblfd globals seat
//     as the BtreeRuntime object fields (the carrier is
//     new-file, owner-pinned to the batch-9 misc unit).
//   h020 pointer_handle - hit: the unsigned char **buf
//     out-parameter seats as the BufAt(buf, off) return value
//     (the vendor's pointer-into-gblbf+r root-arm arithmetic
//     folds to the offset field).
//
// Quirks kept (ledgered):
//   - bfr[0]/bfr[indx] deref: the vendor bfr is malloc'ed at
//     open time (batch-9 owner); before that the deref is NULL
//     UB. The arm quantizes with !! (the NPE IS the vendor
//     crash; journaled UB quantization).
//   - gblfd == -1 (vendor open failure) folds to gblRaf == null
//     with SILENT no-op lseek/read/write (the vendor syscalls
//     return -1 silently and the buffer stays stale; the arm
//     keeps the stale-buffer shape exactly).
//   - the single read() call is kept (NOT readFully): a short
//     read leaves the tail of gblbf stale, exactly as the
//     vendor partial-read path.
//   - static int indx is function-static with no cross-call
//     state read before write; folds to a local.
//   - the commented-out hit++ statistics line and the
//     commented-out if (flag) predecessor of the gblmod |=
//     are vendor comments, not seats.

// btree.h:165 (MBLOCK 2048), :189 (PAGE_SHIFT 11 for MBLOCK
// 2048), :241 (GBLBUF 65537), :257 (AMASK 0xffff for GBLBUF
// 65537; AMASK == GBLBUF-2 in hex per the btree.h comment)
internal const val MBLOCK = 2048
internal const val PAGE_SHIFT = 11
internal const val GBLBUF = 65537
internal const val AMASK = 0xffff

// struct buffers - btree.h:247-250
internal class Buffers {
    val gblbf = ByteArray(MBLOCK)
    var gblbfx: Int = 0 // int gblbfx (block index; -1 = not loaded)
    var gblmod: Int = 0 // int gblmod (modified flag)
}

// The interp-native-btree.cpp TU-static carrier: bfr (the
// buffer pool, interp-native-btree.cpp:64) and gblfd (the
// global file descriptor; -1 in the vendor = null here). The
// malloc/open seats are the batch-9 owners (btreeX / misc);
// nothing in this object mutates gblRaf.
internal object BtreeRuntime {
    val bfr = arrayOfNulls<Buffers>(GBLBUF)
    var gblRaf: java.io.RandomAccessFile? = null // gblfd (-1 = null)
    // unit-273 adds (the BigKey-family file-scope externs,
    // interp-native-btree.cpp:70-80; unsigned char/int/long
    // domains per the vendor declarations):
    val BigKey = ByteArray(STR_MAX) // unsigned char BigKey[STR_MAX]
    var BigKeyBlock = 0 // unsigned int BigKeyBlock
    var FoundBlock = 0 // unsigned int FoundBlock
    var Reinsert = 0 // unsigned int Reinsert
    var BigKeyParent = 0 // unsigned int BigKeyParent
    var BigKeyParentOffset = 0 // unsigned int BigKeyParentOffset
    var BigKeyOffset = 0 // int BigKeyOffset
    var BigKeyLower = 0 // int BigKeyLower
    var FoundOffset = 0 // int FoundOffset
    var BigKeyData = 0L // OFF_T BigKeyData
    // unit-275 adds (the btreeX open-path seats):
    // gbldat - the data file descriptor (the gblfd twin;
    // -1 = null); EDIT-ONLY seat by the unit-275 render
    var gblDatRaf: java.io.RandomAccessFile? = null
    var ReadOnly = 0 // the btree.h ReadOnly flag
}

// the unsigned char **buf out-parameter seat: the block buffer
// and the byte offset into it (the root arm's gblbf + r folds
// to the offset field)
internal class BufAt(val buf: ByteArray, val off: Int)

// #define LSEEK lseek - interp-native-btree.cpp:59; the -1
// silent failure folds to the null/no-op seat (journaled)
private fun btreeLseekZb(off: Long) {
    try {
        BtreeRuntime.gblRaf?.seek(off)
    } catch (e: java.io.IOException) {
        // vendor: lseek fails silently (returns -1, ignored)
    }
}

// read(gblfd, buf, MBLOCK) - libc; single read call kept (short
// read leaves the tail stale, exactly as the vendor)
private fun btreeReadZb(buf: ByteArray) {
    try {
        BtreeRuntime.gblRaf?.read(buf)
    } catch (e: java.io.IOException) {
        // vendor: read fails silently (returns -1, ignored)
    }
}

// write(gblfd, buf, MBLOCK) - libc
private fun btreeWriteZb(buf: ByteArray) {
    try {
        BtreeRuntime.gblRaf?.write(buf)
    } catch (e: java.io.IOException) {
        // vendor: write fails silently (returns -1, ignored)
    }
}

// inline void freadx(unsigned char **buf, int r, int flag) -
// interp-native-btree.cpp
internal fun freadx(r: Int, flag: Int): BufAt {
    if (r < 16) { // root block
        val b0 = BtreeRuntime.bfr[0]!! // open-time alloc (batch-9 owner)
        b0.gblmod = 1
        if (b0.gblbfx < 0) { // load root
            btreeLseekZb(0)
            btreeReadZb(b0.gblbf)
            b0.gblbfx = 0
            return BufAt(b0.gblbf, r)
        }
        return BufAt(b0.gblbf, r)
    }

    val indx = (r and AMASK) + 1

    val b = BtreeRuntime.bfr[indx]!! // open-time alloc (batch-9 owner)

    if (b.gblbfx == r) {
        b.gblmod = b.gblmod or flag
        return BufAt(b.gblbf, 0)
        // svPtr->hit++; // statistics - vendor comment, not a seat
    }

    if (b.gblmod != 0) { // write modified block
        btreeLseekZb(b.gblbfx.toLong() shl PAGE_SHIFT)
        btreeWriteZb(b.gblbf)
    }

    btreeLseekZb(r.toLong() shl PAGE_SHIFT)
    btreeReadZb(b.gblbf)
    b.gblbfx = r
    b.gblmod = flag
    return BufAt(b.gblbf, 0)
}

// -------------------------------------------------------------------------
// unit-275 btreeX-render stubs (h014; the KILL-arm consults)

// void scanblocks(int, struct MSV *) - owner:
// interp-native-btree.cpp::scanblocks, roster 44 (batch 4 of
// this arm's cycle, pending). TODO-stub (h014).
internal fun scanblocks(i: Int, svPtr: Msv) {
    TODO("stub: owner interp-native-btree.cpp::scanblocks (roster 50, batch 2 - the old pin said roster 44 batch 4, C-015)")
}

// void pb(int, unsigned char *) - the DumpBlocks block dumper.
// Owner: interp-native-btree.cpp::pb, roster 111 (batch 8,
// pending). TODO-stub (h014).
internal fun pb(bn: Int, block: ByteArray) {
    TODO("stub: owner interp-native-btree.cpp::pb (roster 106, batch 4 - the old pin said roster 111 batch 8, C-015)")
}
