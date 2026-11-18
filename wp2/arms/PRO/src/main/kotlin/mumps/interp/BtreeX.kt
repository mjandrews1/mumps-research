package mumps.interp

import java.io.RandomAccessFile

/**
 * PRO-arm translation of interp-native-btree.cpp::btreeX (roster
 * row 250, batch 9; units.csv: loc 502, comment_ratio 0.188,
 * pointer_marks 86, control_marks 117, score 393.4, rank 298).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 318-1193 (prototype :287; the file-scope statics
 * :92-103 are the row-252 residue seats). RESOLVES the
 * InterpStubs btreeX stub with TAG CORRECTION (u141 family: the
 * "not a corpus unit" tag superseded - roster row 250 IS the
 * owner; the NativeBtree.btree u50 call sites bind unchanged on
 * the same signature).
 *
 * btreeX(g, key, bd, svPtr): the native B+tree engine - RESTORE
 * re-stores a displaced key, CLOSE/FLUSH dump and close, the
 * auto-open block allocates the bfr pool and creates/locks the
 * UKEY/UDAT files, and the op dispatch walks the block trace:
 * RETRIEVE/STORE/PREVIOUS/XNEXT/KILL over the strxcmp block
 * scan, with the new-tree insert, the reuse/new-record data
 * file append, the KILL descendant-chain reinsertion
 * (scanblocks/BigKey), and the split/rootfix insert paths.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param : the POSIX edges (open/O_EXCL/flock/
 *    LSEEK/read/write/close) fold onto RandomAccessFile on the
 *    BtreeFile carriers (key = raf, data = datRaf, both added
 *    by this unit); flock(LOCK_EX) -> FileChannel.lock attempt
 *    (LOCK_EX IS defined on the reference unix, h018).
 *  - h004 file_scope_static: the btreeX statics (:320-336) ride
 *    the BtreeXState object (u158 precedent).
 *  - h008 static_scratch   : trace/tracep and bufblk ride the
 *    same state object (persistent, matching the statics).
 *  - h010 numeric_promotion: OFF_T datptr maps to Long; the
 *    4/8-byte memcpy block reads/writes are explicit
 *    little-endian shifts (x86-64 reference platform, Scanblocks
 *    precedent); the 2-byte datlen codes are LE shorts.
 *  - h011 label_model       : the nxtblk/nxtkey/fail/upblock/
 *    p000/f1/split goto web is a flat state machine over the Lb
 *    enum; the vendor names kept on every transition.
 *  - h012 unsigned_byte_domain: key/bd/block/bufblk are
 *    NUL-domain CharArrays; interior pointers become offsets.
 *  - h013 dead_store      : the rtf nxtln scan (:344-349) is
 *    dead (nxtln overwritten) - kept as comment; the unreachable
 *    PREVIOUS tail after the return (:694-716) kept as
 *    comment; the KILL double-fetch (freadx Reinsert) kept as
 *    comment; the vv[2]/tmp1/kk unused statics dropped.
 *  - h014 out_of_scope_call: freadx (u193 real seat; the root
 *    arm returns a COPY - MITIGATED by the rootAlias adapter
 *    below, journaled divergence fix-up); scanblocks (u50
 *    real); sigint (real); flush/allocblk/strxcmp/memxmove/
 *    strxxcpy are vendor inlines NOT corpus units - rendered
 *    file-private per the one-owner policy; abort() ->
 *    exitProcess(1) quantization (u128 precedent).
 *  - h015 grammar_collision: btreeX keeps the stub name
 *    one-for-one; BigKey family -> NativeBtreeState (foundBlock/
 *    reinsert/foundOffset ADDED to the InterpStubs carrier by
 *    this unit, row-252 residue seats :93/:94/:99).
 *  - h017 naming          : btreeX keeps the vendor name.
 *  - h018 preprocessor_conditional: reference build resolves
 *    NEW_TREE OUT (:441-445, :500-503; not in config.h),
 *    READ_ONLY OUT (every ReadOnly/READ_ONLY arm excluded as
 *    comments), LOCK_EX IN, DumpBlocks OUT (the u111 Pb.kt
 *    B4 seat is authoritative: the reference binary compiles
 *    the pb dumper out; the pb call sites stay comments here,
 *    joining the reference build's fossil shape).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The reopen flow closes and re-opens after creating the
 *    files fresh - modeled as the reopen loop over File.exists.
 *  - The 5-zero-block init writes and the 2-byte data header
 *    kept; the root 777/0/no-meaning/16- extent writes kept.
 *  - The KILL collapse ladder, BigKey reinsertion pairing
 *    (RESTORE then RETRIEVE) and the FoundOffset/FoundBlock
 *    fix-up kept verbatim.
 *  - The split insert's double-block bufblk walk and the
 *    mid-block key boundary search kept byte-faithful; the
 *    vendor's memset target (the mid key string start, "to")
 *    carried as toMark.
 *  - rootAlias adapter: vendor freadx(&root, r<16) ALIASES the
 *    pool super block; the u193 seat returns a COPY there. This
 *    render binds root/lastrec/integrity writes directly onto
 *    BtreeBufferState.bfr[0].gblbf at the vendor offsets - the
 *    copy divergence is thereby closed for the root block only
 *    (journaled).
 */

private const val MBLOCK_BT = 2048 // btree.h:165/167
private const val PAGE_SHIFT_BT = 11
private const val GBLBUF_BT = 65537
private const val ADSIZE_BT = 8 // btree.h:51
private const val BLOCK_SPLIT = MBLOCK_BT - 64 // btree.h:176
private const val DBL_STR_BT = MBLOCK_BT * 2 // btree.h:175
private const val TRXLIMIT = 100000 // defines.h:121
private const val MMARK_BT = 255.toChar() // defines.h:120
private const val NEXTMARK_BT = 1.toChar() // defines.h:122
private const val MODIFIED_C = 1
private const val UNMODIFIED_C = 0
private const val OK_BT = 1
private const val BAD_BT = 0
private const val KILL_BT = 3 // defines.h:86
private const val XNEXT_BT = 8 // defines.h:88
private const val PREVIOUS_BT = 9 // defines.h:89
private const val RESTORE_BT = 20 // defines.h:90
private const val FLUSH_BT = 21 // defines.h:91
private const val CLOSE_BT = 4 // defines.h:87
private const val STORE_BT = 1 // defines.h:84
private const val RETRIEVE_BT = 0 // defines.h:83

private const val NUL_BT = '\u0000'

/** h004: the btreeX function-scope statics (:320-336). */
private object BtreeXState {
    var trxmax = 0
    val trace = IntArray(TRXLIMIT)
    var ilong = 0
    var blkaddr1 = 0
    var datptr = 0L
    var datlen = 0
    var nxtln = 0
    var blkoff = 0
    var is_ = 0
    var ks = 0
    var ls = 0
    var ii = 0
    var opnflg = 0
    val tracep = IntArray(TRXLIMIT)
    var prev = "" // vendor char* prev (the "" literal or block window)
    var rtf = 0
    var trx = 0
}

/** 4 little-endian domain bytes as an int (h010). */
private fun getIntBT(b: CharArray, off: Int): Int =
    b[off].code or (b[off + 1].code shl 8) or
        (b[off + 2].code shl 16) or (b[off + 3].code shl 24)

/** 4 int bytes little-endian in place (h010). */
private fun putIntBT(b: CharArray, off: Int, v: Int) {
    b[off] = (v and 0xff).toChar()
    b[off + 1] = ((v shr 8) and 0xff).toChar()
    b[off + 2] = ((v shr 16) and 0xff).toChar()
    b[off + 3] = ((v shr 24) and 0xff).toChar()
}

/** 8 little-endian domain bytes as a long (h010, ADSIZE 8). */
private fun getLongBT(b: CharArray, off: Int): Long {
    var v = 0L
    for (k in 7 downTo 0) v = (v shl 8) or (b[off + k].code.toLong() and 0xffL)
    return v
}

/** 8 little-endian domain bytes in place (h010). */
private fun putLongBT(b: CharArray, off: Int, v: Long) {
    for (k in 0 until 8) {
        b[off + k] = ((v shr (8 * k)) and 0xffL).toInt().toChar()
    }
}

/**
 * h014 file-private (vendor inline :190, not a corpus unit):
 * modified compare scanning past the end of the SECOND argument;
 * rOut[0] receives the offset of p2's NUL terminator + 1.
 */
private fun strxcmpC(p1: CharArray, p1Off: Int, p2: CharArray, p2Off: Int,
                     rOut: IntArray): Int {
    var a = p1Off
    var b = p2Off
    while (p1[a] == p2[b] && p1[a] != NUL_BT) {
        a++
        b++
    }
    if (p1[a].code < p2[b].code) return -1
    // vendor: *r = index(p2,0)+1 - the NUL of the p2 window
    var n = b
    while (p2[n] != NUL_BT) n++
    rOut[0] = n + 1
    if (p1[a] == p2[b]) return 0
    return 1
}

/**
 * h014 file-private (vendor inline :74, not a corpus unit): the
 * memmove substitute - copy from..limit to the shrinking to,
 * then zero-fill to end.
 */
private fun memxmoveC(block: CharArray, toOff: Int, fromOff: Int, limitOff: Int,
                      endOff: Int) {
    var to = toOff
    var from = fromOff
    while (from <= limitOff) {
        block[to] = block[from]
        to++
        from++
    }
    while (to < endOff) {
        block[to] = NUL_BT
        to++
    }
}

/**
 * h014 file-private (vendor :162, not a corpus unit): the
 * modified-buffer dumper + the super-block 777 handshake.
 */
private fun flushC(svPtr: Msv) {
    val f = BtreeFile.raf ?: return
    for (i in 0 until GBLBUF_BT) { /* dump global buffers */
        val buf = BtreeBufferState.bfr[i]
        if (buf.gblmod > 0) {
            f.seek(buf.gblbfx.toLong() shl PAGE_SHIFT_BT)
            writeBlockBT(f, buf.gblbf)
            buf.gblmod = 0
        }
    }

    // vendor: freadx(&root,12L,MODIFIED); *root=777L;
    loadRootBT()
    putIntBT(BtreeBufferState.bfr[0].gblbf, 12, 777)

    f.seek(0)
    writeBlockBT(f, BtreeBufferState.bfr[0].gblbf) // dump super block
    BtreeBufferState.bfr[0].gblmod = 0
}

/** h007 edge: MBLOCK bytes out of the NUL-domain CharArray. */
private fun writeBlockBT(f: RandomAccessFile, buf: CharArray) {
    val bytes = ByteArray(MBLOCK_BT)
    for (k in 0 until MBLOCK_BT) bytes[k] = (buf[k].code and 0xff).toByte()
    f.write(bytes)
}

private fun readBlockBT(f: RandomAccessFile, buf: CharArray) {
    val bytes = ByteArray(MBLOCK_BT)
    f.readFully(bytes) // vendor ignores short counts - kept
    for (k in 0 until MBLOCK_BT) buf[k] = (bytes[k].toInt() and 0xff).toChar()
}

/**
 * rootAlias adapter (h001, journaled): the vendor's root
 * freadx(&root, r<16) aliases the pool super block; the u193
 * seat returns a COPY there. This load pins bfr[0] into the
 * pool and the root-domain writes bind onto bfr[0].gblbf.
 */
private fun loadRootBT() {
    val f = BtreeFile.raf ?: error("gblfd null carrier")
    BtreeBufferState.bfr[0].gblmod = 1
    if (BtreeBufferState.bfr[0].gblbfx < 0) {
        f.seek(0)
        readBlockBT(f, BtreeBufferState.bfr[0].gblbf)
        BtreeBufferState.bfr[0].gblbfx = 0
    }
}

/**
 * h014 file-private (vendor inline :267, not a corpus unit):
 * allocate the next key-file block, bumping the extent counter.
 */
private fun allocblkC(svPtr: Msv): Int {
    loadRootBT()
    val rootVal = getIntBT(BtreeBufferState.bfr[0].gblbf, 8) // vendor ADSIZE=8
    var rec = rootVal.toLong()
    rec = rec shl PAGE_SHIFT_BT
    rec = rec + MBLOCK_BT
    val newLast = (rec shr PAGE_SHIFT_BT).toInt()
    putIntBT(BtreeBufferState.bfr[0].gblbf, 8, newLast) // *lastrec = ...
    // vendor: freadx(&block,(*lastrec),MODIFIED); return *lastrec;
    freadx(newLast, MODIFIED_C)
    BtreeBufferState.bfr[0].gblmod = 1
    return newLast
}

/** h008: the vendor static bufblk[DBL_STR+2] scratch. */
private val bufB = CharArray(DBL_STR_BT + 2)

/** h011 label model (vendor btreeX goto labels). */
private enum class Lb { NXTBLK, NXTKEY, P000, KILLX, FAIL, UPBLOCK, F1, SPLIT }

fun btreeX(gIn: Int, key: CharArray, bd: CharArray, svPtr: Msv): Int {
    var g = gIn
    val st = BtreeXState

    var restoreFlag = 0
    if (g == RESTORE_BT) {
        restoreFlag = 1 // special re-insertion case during deletes
        g = STORE_BT
    }

    if (st.opnflg == 0 && g == CLOSE_BT) return OK_BT // files not open

    st.rtf = 1 // retry flag - prevents nxtln from being reset

    if (g == XNEXT_BT || g == PREVIOUS_BT) { /* prepare NEXT cases */
        st.prev = ""
        st.blkoff = strlenC(key)

        if (st.rtf != 0) {
            // vendor :344-349: the nxtln scan is dead (nxtln is
            // assigned blkoff-1, then 0) - h013; kept as comment:
            // for (nxtln = blkoff - 2; nxtln > 0 &&
            //      key[nxtln] != NEXTMARK; nxtln--);
            st.nxtln = 0
        }

        st.rtf = 0
    }

    // ******** section to open main data and key files ********

    if (st.opnflg == 0) { /* auto open */

        for (i in 0 until GBLBUF_BT) { /* allocate buffers */
            val buf = BtreeBufferState.bfr[i]
            buf.gblbfx = -1
            buf.gblmod = 0
        }
        st.opnflg = 1

        // vendor :441-445 NEW_TREE sprintf cfgkey arms - h018 OUT

        var reopen = true
        while (reopen) {
            reopen = false

            val keyFile = java.io.File(cstringAt(svPtr.UKEY))
            val datFile = java.io.File(cstringAt(svPtr.UDAT))

            // vendor: open(UKEY, O_RDWR | O_EXCL) - the O_EXCL-
            // without-O_CREAT has no effect; the does-not-exist
            // arm creates below; the EACCES arm prints+sigint
            val exists = keyFile.exists() && datFile.exists()
            if (exists) {
                try {
                    BtreeFile.raf = RandomAccessFile(keyFile, "rw")
                    BtreeFile.datRaf = RandomAccessFile(datFile, "rw")
                } catch (e: java.io.IOException) {
                    print(
                        "\n*** key.dat file access (EACCES) error in or near line " +
                            svPtr.LineNumber + "\n"
                    )
                    sigint(2)
                }
            } else {
                // Does not exist - create and initialize
                // vendor :476-481 READ_ONLY arm - h018 OUT
                try {
                    BtreeFile.raf = RandomAccessFile(keyFile, "rw")
                    BtreeFile.datRaf = RandomAccessFile(datFile, "rw")
                } catch (e: java.io.IOException) {
                    // quantized onto the IO error carrier
                    print("\nFile system error in or near line " + svPtr.LineNumber + "\n")
                    sigint(2)
                }

                // vendor: freadx(&block,0L,MODIFIED); zeroes
                val block0 = BtreeBufferState.bfr[0].gblbf
                for (i in 0 until MBLOCK_BT) block0[i] = NUL_BT
                val f = BtreeFile.raf!!
                val dat = BtreeFile.datRaf!!

                for (isK in 0..4) { /* init to zeros */
                    f.seek((isK * MBLOCK_BT).toLong())
                    writeBlockBT(f, block0)
                }

                dat.seek(0) /* move to origin */
                dat.write(0)
                dat.write(0) /* init data file (2 bytes) */

                // vendor: freadx(&root,12L,MODIFIED); *root=777L;
                // freadx(&root,0L,MODIFIED); *root=0L;
                // freadx(&root,4L,MODIFIED); *root=0L;
                // freadx(&lastrec,8,MODIFIED); *lastrec=16;
                // - all alias the super block (rootAlias: the
                // direct pool writes at the vendor offsets)
                loadRootBT()
                putIntBT(block0, 12, 777) // integrity flg
                putIntBT(block0, 0, 0) // root
                putIntBT(block0, 4, 0) // no meaning
                putIntBT(block0, 8, 16) // extent (lastrec)

                f.seek(0)
                writeBlockBT(f, block0) /* root block */

                // vendor: close(gblfd); close(gbldat); goto reopen;
                f.close()
                dat.close()
                BtreeFile.raf = null
                BtreeFile.datRaf = null
                reopen = true // reopen with the files now existing
            }
        }

        // vendor :483-441 READ_ONLY arms - h018 OUT

        // vendor :471-478 flock(LOCK_EX) - reference build IN
        try {
            BtreeFile.raf?.channel?.lock()
        } catch (e: java.io.IOException) {
            // will wait for file if already locked - the JVM
            // lock() blocks; the failure arm is unreachable
            // carrier-wise, quantized (noted)
        }

        // vendor: freadx(&root,12L,MODIFIED) - integrity flag
        loadRootBT()
        if (getIntBT(BtreeBufferState.bfr[0].gblbf, 12) != 777) {
            print("\nFile system error in program " + cstringAt(svPtr.program) + "\n")
            kotlin.system.exitProcess(1) // vendor abort()
        }

        // vendor: the commented-out root reset block - kept out
    }

    if (g == FLUSH_BT) {
        flushC(svPtr)
        return OK_BT
    }

    if (g == CLOSE_BT) { /* CLOSE SECTION */
        // vendor :500-503 NEW_TREE arm - h018 OUT
        if (st.opnflg == 0) return OK_BT

        flushC(svPtr)

        try {
            BtreeFile.raf?.close()
            BtreeFile.datRaf?.close()
        } catch (e: java.io.IOException) {
            // vendor ignores close errors here - kept
        }
        BtreeFile.raf = null
        BtreeFile.datRaf = null
        st.opnflg = 0
        return OK_BT
    }

    loadRootBT()
    var rootVal = getIntBT(BtreeBufferState.bfr[0].gblbf, 0)
    // var rootVal: the new-tree insert below updates the root

    // ----------------------------------------------------------------
    //                            new tree
    // ----------------------------------------------------------------
    if (rootVal == 0) { /* no tree */

        if (g == PREVIOUS_BT || g == RETRIEVE_BT) {
            bd[0] = NUL_BT
            return BAD_BT
        }

        if (g != STORE_BT) { // no other op permitted on new tree
            bd[0] = NUL_BT
            return BAD_BT
        }

        if (g == STORE_BT) { /* insert case */
            // vendor :539-558 READ_ONLY arms - h018 OUT

            val blkaddr1L = allocblkC(svPtr)
            st.blkaddr1 = blkaddr1L
            // vendor: freadx(&block,0L,MODIFIED);
            // memcpy(block,&blkaddr1,4); onto the root super
            // block (rootAlias: direct pool write)
            BtreeBufferState.bfr[0].gblmod = 1
            putIntBT(BtreeBufferState.bfr[0].gblbf, 0, blkaddr1L)
            val block = freadx(blkaddr1L, MODIFIED_C) /* k block */

            for (i in 0 until MBLOCK_BT) block[i] = NUL_BT // memset

            st.blkoff = 4
            var w = 0
            while (key[w] != NUL_BT) { // strcpy(&block[blkoff],key)
                block[st.blkoff + w] = key[w]
                w++
            }
            st.blkoff += strlenC(key) + 1

            if (restoreFlag != 0) {
                st.datptr = svPtr.restoredata
            } else if (strlenC(bd) == 0) {
                st.datptr = 0L
            } else {
                val dat = BtreeFile.datRaf!!
                st.datptr = dat.length()
                st.datlen = strlenC(bd) + 1
                dat.write(st.datlen and 0xff)
                dat.write((st.datlen shr 8) and 0xff) /* length code */
                for (k in 0 until st.datlen) { /* data */
                    if (k < st.datlen - 1) dat.write(bd[k].code and 0xff)
                    else dat.write(0)
                }
            }

            putLongBT(block, st.blkoff, st.datptr) /* data address */
            st.blkoff += ADSIZE_BT
            block[st.blkoff] = NUL_BT
            st.blkoff++
            putIntBT(block, st.blkoff, 0) // memcpy(&block[blkoff],&zero,4)
            block[st.blkoff + 4] = MMARK_BT
            block[st.blkoff + 5] = NUL_BT
            // flush(svPtr); - commented out in vendor, kept out
            return OK_BT
        }
    }

    /* ------------------- end new tree section -------------------- */

    // ************************ search section ***********************
    if (strlenC(key) > 256) {
        print("\n*** Global array reference too long:\n" + cstringAt(key) + "\n")
        sigint(2) // sigint(SIGINT), SIGINT=2
    }

    var i = rootVal // disk address of tree root block
    st.trx = -1

    // h010: 4 little-endian domain bytes as an int
    fun intOf(b: CharArray, off: Int): Int = getIntBT(b, off)

    val rOut = IntArray(1)
    val datlenR = IntArray(1) // char datlen[2]
    var block = CharArray(0)
    var ptr1 = 0
    var rtOff = 0
    var ilong: Int

    // h011: the NXTBLK/NXTKEY/FAIL/UPBLOCK/P000 goto web as one
    // flat state machine over the Lb enum; vendor label names kept.
    var lab = Lb.NXTBLK
    while (true) {

        when (lab) {

            Lb.NXTBLK -> {
                st.trx++
                st.trace[st.trx] = i

                if (st.trx > st.trxmax) st.trxmax = st.trx

                if (st.trx >= TRXLIMIT) {
                    print(
                        "Btree trace stack limit reached\n" + cstringAt(key) +
                            "\n" + st.trx + " - increase TRXLIMIT\n"
                    )
                    sigint(2)
                }

                block = freadx(i, UNMODIFIED_C) // fetch tree block
                ptr1 = 0 // ptr1 = block

                lab = Lb.NXTKEY
                continue
            }

            Lb.NXTKEY -> {
                // NXTKEY: ptr1 += 4; skip child pointer; scan keys
                ptr1 += 4

                st.is_ = strxcmpC(key, 0, block, ptr1, rOut)
                rtOff = rOut[0]

                if (st.is_ > 0) {
                    // remember previous key data file address
                    if (g == PREVIOUS_BT) {
                        st.datptr = getLongBT(block, rtOff + 1)
                        if (st.datptr >= 0L) {
                            st.prev = cstringAt(block, ptr1)
                        }
                    }
                    // advance past key & data; the while re-entry
                    // re-enters NXTKEY, whose += 4 completes the
                    // vendor rtPtr + ADSIZE + 5 advance
                    ptr1 = rtOff + ADSIZE_BT + 1
                    lab = Lb.NXTKEY
                    continue
                }

                st.tracep[st.trx] = ptr1 - 4

                if (st.is_ < 0) {
                    ilong = intOf(block, ptr1 - 4) // extract child block addr

                    if (ilong <= 0) { // no child - we're done
                        st.blkoff = ptr1 // blkoff = ptr1 - block
                        lab = Lb.FAIL
                        continue
                    }

                    i = ilong
                    lab = Lb.NXTBLK
                    continue
                }

                // ------------------- key found -------------------
                NativeBtreeState.foundOffset = ptr1
                NativeBtreeState.foundBlock = i
                lab = Lb.P000
                continue
            }

            Lb.P000 -> {
                if (g == PREVIOUS_BT) {
                    ilong = intOf(block, ptr1 - 4) /* block address */

                    if (ilong <= 0) {
                        st.blkoff = ptr1
                        writeCstring(bd, st.prev) // strcpy(bd,prev)
                        return 1
                    }

                    i = ilong
                    lab = Lb.NXTBLK // goto nxtblk

                    // vendor :694-716: the tail below the return is
                    // UNREACHABLE C (nxtln/blkoff scans) - kept as
                    // comment (h013)
                    continue
                }

                if (g == RETRIEVE_BT) { // search operation
                    st.datptr = getLongBT(block, rtOff) // memcpy(&datptr,rtPtr,ADSIZE)

                    if (st.datptr <= 0L) {
                        bd[0] = NUL_BT // empty string
                        return OK_BT
                    } else {
                        val dat = BtreeFile.datRaf!!
                        dat.seek(st.datptr)
                        // read(gbldat,&datlen,2); length of data
                        datlenR[0] = dat.read() or (dat.read() shl 8)
                        dat.seek(st.datptr)
                        for (k in 0 until datlenR[0]) { // read(gbldat,bd,datlen)
                            val b = dat.read()
                            bd[k] = if (b >= 0) b.toChar() else NUL_BT
                        }
                        return OK_BT
                    }
                }

                if (g == STORE_BT) { /* store (key found) */
                    if (strlenC(key) > 256) {
                        print(
                            "\n*** Global array reference too long:\n" +
                                cstringAt(key) + "\n"
                        )
                        sigint(2)
                    }

                    // vendor :739-747 READ_ONLY arms - h018 OUT

                    block = freadx(i, MODIFIED_C) // make memory resident

                    // vendor: ptr1 = rtPtr; memcpy(&datptr,ptr1,ADSIZE)
                    val pOff = rtOff
                    st.datptr = getLongBT(block, pOff)

                    st.is_ = strlenC(bd) // size of data

                    var sub = Lb.F1 // (init only; both arms set below)
                    sub = if (st.datptr <= 0L) {
                        Lb.F1
                    } else {
                        val dat = BtreeFile.datRaf!!
                        dat.seek(st.datptr)
                        // read(gbldat,&datlen,2); length code
                        datlenR[0] = dat.read() or (dat.read() shl 8)
                        if (datlenR[0] > st.is_) { // big enough
                            dat.seek(st.datptr)
                            Lb.SPLIT // (the SAVE arm alias)
                        } else {
                            Lb.F1
                        }
                    }

                    // newrec/save/goto save1 ladder (vendor :762-792)
                    if (sub == Lb.SPLIT) {
                        // save: write(gbldat,&datlen,2); write bd
                        val dat = BtreeFile.datRaf!!
                        dat.write(datlenR[0] and 0xff)
                        dat.write((datlenR[0] shr 8) and 0xff) /* length code */
                        for (k in 0 until datlenR[0]) { /* data */
                            if (k < datlenR[0] - 1) dat.write(bd[k].code and 0xff)
                            else dat.write(0)
                        }
                        // save1: memcpy(ptr1,&datptr,ADSIZE)
                        putLongBT(block, pOff, st.datptr) /* data address */
                        return OK_BT
                    }

                    // newrec: delete old data record, add new at EOF
                    if (st.is_ == 0) { // zero length data field
                        st.datptr = 0L
                        // goto save1
                        putLongBT(block, pOff, st.datptr) /* data address */
                        return OK_BT
                    }

                    val dat = BtreeFile.datRaf!!
                    st.datptr = dat.length() // LSEEK(gbldat,0,SEEK_END)
                    st.datlen = st.is_ + 1
                    // falls to save
                    dat.write(st.datlen and 0xff)
                    dat.write((st.datlen shr 8) and 0xff) /* length code */
                    for (k in 0 until st.datlen) { /* data */
                        if (k < st.datlen - 1) dat.write(bd[k].code and 0xff)
                        else dat.write(0)
                    }
                    putLongBT(block, pOff, st.datptr) /* data address */
                    return OK_BT
                }

                st.blkoff = ptr1 // blkoff = (int)(ptr1 - block)
                lab = Lb.KILLX
                continue
            }

            Lb.KILLX -> {
                if (g == KILL_BT) { /* delete */
                    // vendor :787-796 READ_ONLY arms - h018 OUT

                    block = freadx(i, MODIFIED_C)
                    ilong = intOf(block, ptr1 - 4) // memcpy(&ilong,ptr1-4,4)

                    var e = MBLOCK_BT - 1
                    while (block[e] == NUL_BT) e-- // for (i=MBLOCK-1; block[i]==0; i--)
                    e++

                    memxmoveC( // remove the key
                        block, ptr1 - 4,
                        ptr1 + strlenC(block, ptr1) + ADSIZE_BT + 2,
                        e, MBLOCK_BT
                    )

                    // ilong points to descendent chain of key to
                    // be deleted; BigKey is the highest key there
                    if (ilong == 0) {
                        return OK_BT
                    }

                    writeCstring(NativeBtreeState.bigKey, "")
                    NativeBtreeState.bigKeyOffset = 0
                    NativeBtreeState.bigKeyBlock = 0
                    NativeBtreeState.reinsert = ilong

                    scanblocks(ilong, svPtr) // pass the lower

                    if (cstringAt(NativeBtreeState.bigKey) == "") return OK_BT

                    // vendor: freadx(&block,Reinsert,MODIFIED); -
                    // a double-fetch fossil: overwritten by the
                    // next freadx before any read - h013 comment

                    // reload the lower block (BigKeyBlock),
                    // remove the key
                    block = freadx(NativeBtreeState.bigKeyBlock, MODIFIED_C)
                    ptr1 = NativeBtreeState.bigKeyOffset

                    e = MBLOCK_BT - 1
                    while (block[e] == NUL_BT) e--
                    e++

                    memxmoveC( // remove the key
                        block, ptr1 - 4,
                        ptr1 + strlenC(block, ptr1) + ADSIZE_BT + 2,
                        e, MBLOCK_BT
                    )

                    e = MBLOCK_BT - 1
                    while (block[e] == NUL_BT) e--
                    putIntBT(block, e - 4, NativeBtreeState.bigKeyLower)

                    if (block[4] == MMARK_BT && NativeBtreeState.bigKeyBlock ==
                        NativeBtreeState.reinsert
                    ) { // collapse empty immediate lower block
                        ilong = intOf(block, 0) // its descendent, if any
                        if (ilong >= 0) NativeBtreeState.reinsert = ilong
                    } else if (block[4] == MMARK_BT) { // collapse non-immediate
                        ilong = intOf(block, 0) // its descendent, if any
                        block = freadx(NativeBtreeState.bigKeyParent, MODIFIED_C)
                        putIntBT(block, NativeBtreeState.bigKeyParentOffset, ilong)
                    }

                    // reinsert key with descendents

                    svPtr.restoredata = NativeBtreeState.bigKeyData
                    writeCstring(bd, "xxx")
                    val tmp = CharArray(32000) // WHOLE_SIZE STR_MAX
                    writeCstring(tmp, cstringAt(NativeBtreeState.bigKey))
                    btreeX(RESTORE_BT, tmp, bd, svPtr)

                    btreeX(RETRIEVE_BT, NativeBtreeState.bigKey, bd, svPtr)

                    ilong = intOf(block, NativeBtreeState.foundOffset - 4)

                    if (ilong == 0) {
                        block = freadx(NativeBtreeState.foundBlock, MODIFIED_C)
                        putIntBT(
                            block, NativeBtreeState.foundOffset - 4,
                            NativeBtreeState.reinsert
                        )
                    } else {
                        print(
                            "open not slot found BigKey=" +
                                cstringAt(NativeBtreeState.bigKey) + "\n"
                        )
                        // vendor :915-918 DumpBlocks pb arm - h018 OUT
                        sigint(100)
                    }

                    // flush(svPtr); - commented out in vendor, kept out
                    return OK_BT
                }

                if (g == XNEXT_BT) { /* found next */
                    st.blkoff += strlenC(block, st.blkoff) + ADSIZE_BT + 6 // next key
                    ilong = intOf(block, st.blkoff - 4) // look for less-than children

                    if (ilong <= 0) {
                        lab = Lb.FAIL
                        continue
                    }

                    i = ilong
                    st.tracep[st.trx] = st.blkoff - 4 // new parent
                    lab = Lb.NXTBLK
                    continue
                }

                lab = Lb.FAIL /* end found */
                continue
            }

            Lb.FAIL -> {
                if (g == PREVIOUS_BT) {
                    // goto p000
                    lab = Lb.P000
                    continue
                }

                if (g == XNEXT_BT) {
                    if (block[st.blkoff] != MMARK_BT) { // not at end of block
                        if (g == XNEXT_BT) { /* modify key */
                            val from0 = st.blkoff
                            var w = 0
                            // copy global array ref to both key and bd:
                            // while ( *(tox++) = (*(to++) = *(from++)) );
                            while (true) {
                                val c = block[from0 + w]
                                key[w] = c
                                bd[w] = c
                                if (c == NUL_BT) break
                                w++
                            }
                            // *to = 0; *tox = 0; - the NULs already written

                            // from++ (skip the NUL); data ptr
                            st.datptr = getLongBT(block, from0 + w + 1)

                            if (st.datptr < 0L) {
                                ptr1 = st.blkoff - 4
                                lab = Lb.NXTKEY // goto nxtkey; dltd key
                                continue
                            }

                            return OK_BT
                        }
                    }

                    // end of block mark found - UPBLOCK
                    lab = Lb.UPBLOCK
                    continue
                }

                // ------------------ STORE section ------------------

                if (g == STORE_BT) { // store new key
                    st.blkaddr1 = 0

                    if (restoreFlag != 0) {
                        st.datptr = svPtr.restoredata
                    } else if (bd[0] == NUL_BT) { // zero length data field
                        st.datptr = 0L
                    } else {
                        val dat = BtreeFile.datRaf!!
                        st.datptr = dat.length()
                        st.datlen = strlenC(bd) + 1
                        dat.write(st.datlen and 0xff)
                        dat.write((st.datlen shr 8) and 0xff) /* length code */
                        for (k in 0 until st.datlen) { /* data */
                            if (k < st.datlen - 1) dat.write(bd[k].code and 0xff)
                            else dat.write(0)
                        }
                    }

                    lab = Lb.F1
                    continue
                }

                bd[0] = NUL_BT
                return 0 // default failure
            }

            Lb.UPBLOCK -> {
                st.trx--

                if (st.trx < 0) {
                    writeCstring(bd, "")
                    return BAD_BT
                }

                i = st.trace[st.trx]
                st.blkoff = st.tracep[st.trx] + 4 /* key */
                block = freadx(i, UNMODIFIED_C)

                if (block[st.blkoff] == MMARK_BT) {
                    lab = Lb.UPBLOCK
                    continue
                }

                lab = Lb.FAIL // must be parent
                continue
            }

            Lb.F1 -> {
                block = freadx(i, MODIFIED_C) /* marked for write */

                // find block end: the lsp walk to MMARK
                var lsp = st.blkoff
                while (block[lsp] != MMARK_BT) {
                    while (block[lsp] != NUL_BT) lsp++
                    lsp += ADSIZE_BT + 5
                }
                st.ls = lsp // ls = lsp - block; occupied length

                st.ks = strlenC(key) // key to be added length

                if (st.ks + st.ls > BLOCK_SPLIT) {
                    lab = Lb.SPLIT
                    continue
                }

                // ---------------- Simple store insert ----------------
                // The next block of code substitutes for memmove -
                // copies beginning at the high end to avoid the
                // overlap problem (vendor comment kept).
                var length = st.ls - (st.blkoff - 4)

                var to = (st.blkoff - 4) + st.ks + ADSIZE_BT + 6 + length
                var from = st.blkoff - 4 + length

                length++
                while (length-- > 0) {
                    block[to] = block[from]
                    to--
                    from--
                }

                to = st.blkoff - 4
                val v = st.blkaddr1 // from = (unsigned char *) &blkaddr1
                for (k in 0 until 4) {
                    block[to + k] = ((v shr (8 * k)) and 0xff).toChar()
                }
                to += 4

                // to = memccpy(to, key, 0, MBLOCK)
                var kx = 0
                while (true) {
                    val c = key[kx]
                    block[to] = c
                    to++
                    if (c == NUL_BT) break
                    kx++
                    if (to >= MBLOCK_BT) break
                }

                // memcpy(to, &datptr, ADSIZE + 4)
                for (k in 0 until ADSIZE_BT) {
                    block[to] = ((st.datptr shr (8 * k)) and 0xffL).toInt().toChar()
                    to++
                }

                block[to] = NUL_BT

                return OK_BT
                // ------------- end simple insert section -------------
            }

            Lb.SPLIT -> {
                val bb = bufB // vendor: to = bufblk; double length
                var length0 = st.blkoff - 4
                st.ii = length0

                var to = 0
                var from = 0
                while (length0-- > 0) {
                    bb[to] = block[from]
                    to++
                    from++
                }

                putIntBT(bb, st.blkoff - 4, st.blkaddr1) // child
                var w = 0
                while (key[w] != NUL_BT) { // strcpy(&bufblk[blkoff],key)
                    bb[st.blkoff + w] = key[w]
                    w++
                }
                bb[st.blkoff + w] = NUL_BT

                st.blkoff += st.ks + 1

                putLongBT(bb, st.blkoff, st.datptr) // data address
                bb[st.blkoff + ADSIZE_BT] = NUL_BT

                to = st.blkoff + ADSIZE_BT + 1
                from = st.ii
                // memcpy(to, from, MBLOCK - ii)
                var nn = MBLOCK_BT - st.ii
                while (nn-- > 0) {
                    bb[to] = block[from]
                    to++
                    from++
                }
                for (k in 0 until MBLOCK_BT) bb[to + k] = NUL_BT // memset 2nd half

                // find a mid-block key boundary
                var fr = 0
                val mid = MBLOCK_BT / 2
                while (fr < mid) { // find middle key
                    fr += 4
                    while (bb[fr] != NUL_BT) fr++
                    fr++ // from += 1 (the NUL)
                    fr += ADSIZE_BT + 1
                }

                st.blkoff = fr

                if (cstringAt(bb, fr + 4) == cstringAt(key)) {
                    fr += 4
                    fr += strlenC(bb, fr) + ADSIZE_BT + 2
                }

                fr += 4

                val toMark = fr // vendor: to = from (the mid key start)

                // extract key & advance to data pointer:
                // from = strxxcpy(key, from) + 1
                var k2 = 0
                while (true) {
                    val c = bb[fr]
                    key[k2] = c
                    if (c == NUL_BT) break
                    k2++
                    fr++
                }
                fr++ // + 1 past the NUL

                st.datptr = getLongBT(bb, fr) // extract data to be sent up

                st.blkoff = fr // blkoff = from - bufblk

                // advance past data pointer
                fr += ADSIZE_BT + 1

                block = freadx(i, MODIFIED_C) /* make memory resident */

                // memcpy(block, from, MBLOCK); right block
                for (k in 0 until MBLOCK_BT) block[k] = bb[fr + k]

                // memset(to, 0, DBL_STR - (to - bufblk)): the
                // first half ends at the mid key string start
                val zn = DBL_STR_BT - toMark
                for (k in 0 until zn) bb[toMark + k] = NUL_BT

                bb[toMark] = MMARK_BT // *(to++) = MMARK
                bb[toMark + 1] = NUL_BT // *to = 0

                st.blkaddr1 = allocblkC(svPtr) /* allocate new left block */
                val block1v = freadx(st.blkaddr1, MODIFIED_C)
                for (k in 0 until MBLOCK_BT) block1v[k] = bb[k] /* left */

                /* fix parent */

                st.trx--

                if (st.trx < 0) {
                    // rootfix: the parent fix needs a new root
                    val m = allocblkC(svPtr)
                    // vendor: freadx(&block,0L,MODIFIED);
                    // memcpy(block,&m,4); onto the root super
                    // block (rootAlias: direct pool write)
                    BtreeBufferState.bfr[0].gblmod = 1
                    putIntBT(BtreeBufferState.bfr[0].gblbf, 0, m)
                    block = freadx(m, MODIFIED_C)

                    putIntBT(block, 0, st.blkaddr1) // memcpy(block,&blkaddr1,4)
                    var w2 = 0
                    while (key[w2] != NUL_BT) { // strcpy(&block[4],key)
                        block[4 + w2] = key[w2]
                        w2++
                    }
                    st.blkoff = 5 + strlenC(key)
                    putLongBT(block, st.blkoff, st.datptr) /* data address */
                    block[st.blkoff + ADSIZE_BT] = NUL_BT
                    st.blkoff += ADSIZE_BT + 1
                    putIntBT(block, st.blkoff, st.trace[0]) // memcpy trace[0]
                    block[st.blkoff + 4] = MMARK_BT
                    block[st.blkoff + 5] = NUL_BT
                    // flush(svPtr); - commented out in vendor, kept out
                    return OK_BT
                }

                i = st.trace[st.trx]
                st.blkoff = st.tracep[st.trx] + 4
                lab = Lb.F1
                continue
            }
        }
    }
}
