package both

// Unit 275 (BOTH B9, giant): interp-native-btree.cpp::btreeX
// (502 LOC). Batch_plan ordinal 275: giant. The native btree
// global-array engine: the auto-open/create family, the
// XNEXT/PREVIOUS walk, the search, the RETRIEVE, the STORE
// reuse path, the KILL chain, and the STORE insert WITH THE
// SPLIT SECTION RENDERED IN FULL (f1/split/rootfix; the
// double-length bufblk carrier, the right-end overlap copy,
// the memccpy key install, the mid-block boundary walk, the
// strxxcpy key promotion, the left/right block split, the
// trace-stack parent refix and the rootfix grow).
//
// BYTE-DOMAIN FOLD (h007/h016): the vendor key[]/bd[] are
// unsigned char throughout (the block images, BigKey and
// bufblk are byte domain); the render wraps through
// byte-domain copies (entry copy, exit write-back); internal
// cursors are the BPtr/index idiom of the unit-273 misc
// render (LE int/long get/set, cstrlen).
//
// ALIAS FOLD (h007): the vendor char* block cursor is an
// ALIAS into bfr[indx].gblbf; the unit-192 freadx seat
// returns the live gblbf reference in BufAt.buf, so every
// mutation-bearing fetch binds blk = BufAt.buf directly (no
// copy fold needed, unlike the DOC arm's cache carrier): the
// fwrite path sees the same bytes the vendor writes through
// the alias. The stale-pointer hazards (ptr1 across upblock
// refetches, prev across evictions) are the vendor's own
// aliasing semantics, preserved by construction.
//
// JVM-DOMAIN ADJUDICATIONS (h018): open()/close() ride the
// RandomAccessFile carriers (gblRaf landed unit 192; the
// gblDatRaf carrier added to BtreeRuntime as the EDIT-ONLY
// seat by this render, the gblRaf precedent). The O_EXCL
// without O_CREAT quirk (a POSIX no-op the vendor banner
// mislabels "fails if file already exists") folds onto the
// exists-probe + create path. flock(LOCK_EX) has no JVM
// carrier - the inert ledger comment, the do-not-proceed
// intent cited. The create-path close+goto reopen pair folds
// onto keeping the carrier open (the reopen loop's observable
// content is the integrity check). NEW_TREE (#ifdef pid-named
// temp files) and READ_ONLY (#ifdef arms) are excluded dead
// arms per h018, their banners kept in ledgers. getpid() has
// no non-dead use (NEW_TREE excluded).
//
// DEAD-LEAD LEDGERS kept verbatim (h013): the rtf/nxtln prep
// ladder (rtf = 1 tested true then nxtln assigned three times
// ending 0; the whole if-block is dead storage), the
// unreachable PREVIOUS tail after the p000 goto nxtblk (the
// NEXTMARK slice ladder), the unused locals (upper,
// upperoffset, vv[2], downblocknbr, kk), the double fetch of
// the KILL arm (freadx(Reinsert,MODIFIED) immediately
// overwritten by the BigKeyBlock fetch - the vendor's own
// reload), the commented-out memmove-substitution banner and
// the commented to/from pair inside split.
//
// Quirks kept (ledgered):
//   - the trace[0] rootfix write: the FIRST trace entry is
//     the old root, written as the new root's child at the
//     split-grow tail. Kept verbatim.
//   - the right-end overlap copy shifts length+1 bytes (the
//     length++ before the while(length--) shape).
//   - memccpy(to,key,0,MBLOCK): copies through the NUL, the
//     cursor lands past it.
//   - the split mid-block walk steps child(4)+key+ADSIZE+1
//     per record; the equal-key advance arm steps past the
//     promoted record so promotion is idempotent.
//   - the insert tail writes MMARK,0 after the child+key+
//     datptr record; blkoff+4 carries the next-child zeros.
//   - abort() renders as the thrown RuntimeException after
//     the banner (the vendor integrity-check failure).
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: the byte-domain fold above.
//   h010 numeric_domain - hit: unsigned-byte compares masked.
//   h012 numeric_domain - reused: LE int/long seats over the
//     block image; the OFF_T Long domain.
//   h013 dead_store - hit: the ledgers above.
//   h014 out_of_scope_call - reused: freadx/allocblk/flush/
//     strxcmp/memxmove/strxxcpy ride the landed unit-192/273
//     renders; sigint rides the unit-190 seat; scanblocks
//     rides its NEW TODO stub (owner
//     interp-native-btree.cpp::scanblocks, roster 44, batch 4
//     of this arm's cycle); pb rides its NEW TODO stub (owner
//     interp-native-btree.cpp::pb, roster 111, batch 8); the
//     recursive KILL-arm calls re-enter the byte-domain core
//     directly (the wrapper's entry/exit copies are the
//     caller-facing fold only).
//   h015 grammar-collision - reused: ASCII only, no
//     backticks, no raw comment dollars, trailing newline.
//   h017 naming - hit: btreeX keeps the vendor name; is
//     adapts to isz (the Kotlin keyword); the BtreeXState
//     carrier holds the vendor function statics (trxmax/
//     trace/tracep/opnflg/rtf/trx).
//   h018 preprocessor_conditional - REUSED: the NEW_TREE/
//     READ_ONLY exclusions and the JVM-DOMAIN adjudications
//     above.
//   h019 class_member_carrier - reused: gblDatRaf/ReadOnly
//     seat in BtreeRuntime (the EDIT-ONLY addition, the
//     gblRaf precedent).

private const val PREVIOUS_BX = 9 // defines.h:89
private const val RESTORE_BX = 20 // defines.h:90
private const val NEXTMARK_BX = 1 // defines.h:122
private const val TRXLIMIT_BX = 100000 // defines.h:121
private const val SIGINT_BX = 2 // vendor sigint(SIGINT) macro seat
private const val GKILL_BX = 3 // defines.h:86
private const val FLUSH_BX = 21 // defines.h:91/:180
internal const val MMARK = 0xff // btree.h block-end mark
internal const val DBL_STR = MBLOCK * 2 // btree.h:173
internal const val BLOCK_SPLIT = MBLOCK - 64 // btree.h:174

// the vendor function-static storage (the unit-192 carrier
// precedent); the pass-through scratch statics render as call
// locals (single-call lifetime, the vendor's own usage
// pattern, h013 note)
internal object BtreeXState {
    var trxmax = 0 // static int trxmax
    val trace = IntArray(TRXLIMIT_BX) // static int trace[TRXLIMIT]
    val tracep = IntArray(TRXLIMIT_BX) // static int tracep[TRXLIMIT]
    var opnflg = 0 // static int opnflg
    var rtf = 0 // static int rtf
    var trx = -1 // static int trx (the trace cursor)
}

// file-private LE int/long seats over the block image (the
// unit-273 misc twins, repeated file-locally per the
// private-top-level rule)
private fun leIntSetX(buf: ByteArray, offc: Int, v: Int) {
    buf[offc] = (v and 0xff).toByte()
    buf[offc + 1] = ((v shr 8) and 0xff).toByte()
    buf[offc + 2] = ((v shr 16) and 0xff).toByte()
    buf[offc + 3] = ((v shr 24) and 0xff).toByte()
}

private fun leIntX(buf: ByteArray, offc: Int): Int =
    (buf[offc].toInt() and 0xff) or
        ((buf[offc + 1].toInt() and 0xff) shl 8) or
        ((buf[offc + 2].toInt() and 0xff) shl 16) or
        ((buf[offc + 3].toInt() and 0xff) shl 24)

private fun leLongX(buf: ByteArray, offc: Int): Long {
    var v = 0L
    for (k in 7 downTo 0) v = (v shl 8) or (buf[offc + k].toLong() and 0xff)
    return v
}

private fun leLongSetX(buf: ByteArray, offc: Int, v: Long) {
    for (k in 0 until 8) {
        buf[offc + k] = ((v shr (8 * k)) and 0xff).toByte()
    }
}

// C strlen over the byte domain at the cursor
private fun cstrlenX(buf: ByteArray, offc: Int): Int {
    var k = offc
    while (k < buf.size && buf[k] != 0.toByte()) k++
    return k - offc
}

// char-domain to byte-domain entry copy (h007 fold)
private fun toBytesX(src: CharArray): ByteArray {
    val out = ByteArray(STR_MAX)
    var k = 0
    while (k < src.size && src[k] != '\u0000' && k < STR_MAX) {
        out[k] = (src[k].code and 0xff).toByte()
        k++
    }
    if (k < STR_MAX) out[k] = 0
    return out
}

// byte-domain write-back (NUL-terminated)
private fun fromBytesX(dst: CharArray, src: ByteArray) {
    var k = 0
    while (k < src.size && src[k] != 0.toByte() && k < dst.size) {
        dst[k] = (src[k].toLong() and 0xff).toInt().toChar()
        k++
    }
    if (k < dst.size) dst[k] = '\u0000'
}

// the byte-image string adapter (the banner consults)
private fun strBytesX(src: ByteArray): String {
    var k = 0
    val sb = StringBuilder()
    while (k < src.size && src[k] != 0.toByte()) {
        sb.append((src[k].toLong() and 0xff).toInt().toChar())
        k++
    }
    return sb.toString()
}

// the byte-image strxcmp consult (wraps the unit-273 render's
// signature into the (first, cursor, out) shape of the
// search)
private fun strxcmpX(keyB: ByteArray, blk: ByteArray, offc: Int, rtOut: IntArray): Int =
    strxcmp(BPtr(keyB, 0), BPtr(blk, offc), rtOut)

// the data-file carrier backstop (the gblRaf twin)
private fun gblDataRafX(): java.io.RandomAccessFile =
    BtreeRuntime.gblDatRaf
        ?: throw IllegalStateException(
            "gbldat: btree data file not opened (vendor gbldat -1 sentinel)")

// int btreeX(int, unsigned char key[], unsigned char bd[],
// struct MSV *) - the CharArray wrapper (entry/exit copies)
internal fun btreeX(gIn: Int, key: CharArray, bd: CharArray, svPtr: Msv): Int {
    val keyB = toBytesX(key)
    val bdB = toBytesX(bd)
    val r = btreeXCore(gIn, keyB, bdB, svPtr)
    fromBytesX(key, keyB)
    fromBytesX(bd, bdB)
    return r
}

// the engine core (byte domain; the recursive KILL arm calls
// this core directly - the entry/exit copies are the
// wrapper's)
private fun btreeXCore(gArg: Int, keyB: ByteArray, bdB: ByteArray, svPtr: Msv): Int {
    var g = gArg

    // int ...; restoreFlag=0;
    var restoreFlag = false

    if (g == RESTORE_BX) {
        restoreFlag = true // special re-insertion case during deletes
        g = STORE
    }

    if (BtreeXState.opnflg == 0 && g == CLOSE) return OK_NB // files not open - no need to close

    // rtf = 1; (the retry flag; per-call priming makes the
    // static lifetime inert - the prep block below is the
    // vendor's own dead ladder, h013, kept as the ledger)
    BtreeXState.rtf = 1

    // the XNEXT/PREVIOUS prep
    var prevBlk: ByteArray? = null
    var prevOff = 0
    var blkoff = 0

    if (g == XNEXT || g == PREVIOUS_BX) { /* prepare NEXT cases */

        // prev = (unsigned char *) "";
        prevBlk = null

        blkoff = cstrlenX(keyB, 0)

        // if (rtf) { ...nxtln ladder ending nxtln = 0... } -
        // the vendor dead-ladder (h013): the final assignment
        // nulls the walk, and nxtln's only later reads sit in
        // the unreachable PREVIOUS tail (below). Ledger only.
        BtreeXState.rtf = 0
    }

    /* *****************************************
       section to open main data and key files
       ***************************************** */

    if (BtreeXState.opnflg == 0) { /* auto open */

        // for (i = 0; i < GBLBUF; i++) { malloc buffers; } -
        // the pre-allocation loop (the vendor malloc failure
        // arm is the JVM-native OOM fold, ledger)
        for (bx in 0 until GBLBUF) {
            if (BtreeRuntime.bfr[bx] == null) {
                BtreeRuntime.bfr[bx] = Buffers()
            }
            BtreeRuntime.bfr[bx]!!.gblbfx = -1
            BtreeRuntime.bfr[bx]!!.gblmod = 0
        }
        BtreeXState.opnflg = 1

        // #ifdef NEW_TREE: the pid-named keyN.tmp/datN.tmp
        // sprintf pair - excluded build (h018 ledger)

        // reopen: - the vendor close+goto reopen loop folds
        // onto the kept-open carrier (header ledger)

        // open fails (O_EXCL) if file already exists
        // (the vendor banner fossil: O_EXCL without O_CREAT
        // is a POSIX no-op; the fold is the exists-probe)
        // #ifndef READ_ONLY arms:
        val ukFile = java.io.File(cStringOf(svPtr.UKEY))
        val udFile = java.io.File(cStringOf(svPtr.UDAT))

        val ukExists = ukFile.exists()
        val udExists = udFile.exists()

        // the EACCES banners (errno == EACCES) fold onto the
        // JVM access faults (the RandomAccessFile open throw
        // of the rw probe on unreadable files, ledger; the
        // banner text kept verbatim)
        BtreeRuntime.gblRaf =
            if (ukExists) java.io.RandomAccessFile(ukFile, "rw") else null
        BtreeRuntime.gblDatRaf =
            if (udExists) java.io.RandomAccessFile(udFile, "rw") else null

        // unix file locking: LOCK_EX is a system symbol; will
        // wait for file if already locked - flock(gblfd,
        // LOCK_EX) has no JVM carrier (advisory-lock
        // divergence cited; the do-not-proceed intent rides
        // the carrier itself), inert ledger

        val gblfdOpen = BtreeRuntime.gblRaf != null
        if (!gblfdOpen) { /* Does not exist - create and initialize */

            // #ifdef READ_ONLY: "Attempt to create read only
            // native database" banner - excluded build ledger

            BtreeRuntime.gblRaf = java.io.RandomAccessFile(ukFile, "rw")
            BtreeRuntime.gblDatRaf = java.io.RandomAccessFile(udFile, "rw")

            val gblFile = BtreeRuntime.gblRaf
                ?: throw IllegalStateException("null gblRaf seat")

            val b0 = freadx(0, MODIFIED) /* get a buffer address */
            for (bx in 0 until MBLOCK) b0.buf[bx] = 0

            for (isx in 0..4) { /* init to zeros */
                try {
                    gblFile.seek(isx.toLong() * MBLOCK)
                    gblFile.write(b0.buf, 0, MBLOCK)
                } catch (e: java.io.IOException) {
                    // vendor: lseek/write fail silently
                }
            }

            val gblDataFile = BtreeRuntime.gblDatRaf
                ?: throw IllegalStateException("null gblDatRaf seat")
            try {
                gblDataFile.seek(0) /* move to origin */
                gblDataFile.write(byteArrayOf(0, 0)) /* init data file */
            } catch (e: java.io.IOException) {
                // vendor: fail silently
            }

            // the super-block marks through the slot-0 alias
            // (freadx(12)/(0)/(4)/(8) + the int stores)
            leIntSetX(b0.buf, 12, 777) /* integrity flg */
            leIntSetX(b0.buf, 0, 0) /* root */
            leIntSetX(b0.buf, 4, 0) /* no meaning */
            leIntSetX(b0.buf, 8, 16) /* extent */

            try {
                gblFile.seek(0)
                gblFile.write(BtreeRuntime.bfr[0]!!.gblbf, 0, MBLOCK) /* root block */
            } catch (e: java.io.IOException) {
                // vendor: fail silently
            }

            // close(gblfd); close(gbldat); goto reopen; - the
            // close+reopen pair folds onto the kept-open
            // carrier (h018, header ledger)
        }

        // freadx(&root, 12L, MODIFIED); the integrity flag
        // consult (the direct alias read)
        val sb = freadx(12, MODIFIED)
        val integ = leIntX(sb.buf, sb.off)

        if (integ != 777) {
            System.out.print("\nFile system error in program " + cStringOf(svPtr.program) + "\n")
            // abort() renders as the thrown RuntimeException after
            // the banner (the vendor crash)
            throw RuntimeException("abort: btree integrity flag failure")
        }

        /*..........................................
           now done only when file system altered .....
           the commented fossil kept per the vendor
           ............................................ */
    }

    if (g == FLUSH_BX) {
        flush(svPtr)
        return OK_NB
    }

    if (g == CLOSE) { /* CLOSE SECTION */

        // #ifdef NEW_TREE: the remove(UKEY/UDAT) pair -
        // excluded build ledger

        if (BtreeXState.opnflg == 0) return OK_NB

        flush(svPtr)

        try {
            BtreeRuntime.gblRaf?.close()
        } catch (e: java.io.IOException) {
            // vendor: close fails silently
        }
        BtreeRuntime.gblRaf = null
        try {
            BtreeRuntime.gblDatRaf?.close()
        } catch (e: java.io.IOException) {
            // vendor: close fails silently
        }
        BtreeRuntime.gblDatRaf = null
        BtreeXState.opnflg = 0
        return OK_NB
    }

    // freadx(&root, 0L, UNMODIFIED);
    val rootBa = freadx(0, UNMODIFIED)
    val rootVal = leIntX(rootBa.buf, rootBa.off)

    // ---------------------------------------------------------------------------------------------
    //                            new tree
    // ---------------------------------------------------------------------------------------------

    if (rootVal == 0) { /*no tree */

        if (g == PREVIOUS_BX || g == RETRIEVE) {
            bdB[0] = 0
            return BAD_NB
        }

        if (g != STORE) { // no other op permitted on new tree
            bdB[0] = 0
            return BAD_NB
        }

        if (g == STORE) { /*insert case */

            if (BtreeRuntime.ReadOnly != 0) {

                // #ifdef READ_ONLY: "Native database read only
                // violation" banner - excluded build ledger

                // freadx(&RDFlg, 12L, MODIFIED); *RDFlg = 0;
                val sb12 = freadx(12, MODIFIED)
                leIntSetX(sb12.buf, sb12.off, 0) /* mark as open - reset at close */
                try {
                    BtreeRuntime.gblRaf?.seek(0)
                    BtreeRuntime.gblRaf?.write(BtreeRuntime.bfr[0]!!.gblbf, 0, MBLOCK) /* dump super block */
                } catch (e: java.io.IOException) {
                    // vendor: fail silently
                }
                BtreeRuntime.ReadOnly = 0
            }

            val blkaddr1 = allocblk(svPtr)
            var ba = freadx(0, MODIFIED)
            leIntSetX(ba.buf, ba.off, blkaddr1) // memcpy(block, &blkaddr1, 4)
            ba = freadx(blkaddr1, MODIFIED) /* k block */
            val blk = ba.buf

            for (bx in 0 until MBLOCK) blk[bx] = 0 // memset

            var boff = 4
            // strcpy(&block[blkoff], key); /* key */
            var kx = 0
            while (kx < STR_MAX && keyB[kx] != 0.toByte()) {
                blk[boff + kx] = keyB[kx]
                kx++
            }
            blk[boff + kx] = 0
            boff += cstrlenX(keyB, 0) + 1

            var datptr: Long
            if (restoreFlag) datptr = svPtr.restoredata
            else if (cstrlenX(bdB, 0) == 0) {
                datptr = 0L
            } else {
                val gblDataFile = gblDataRafX()
                datptr = gblDataFile.length()
                val datlen = cstrlenX(bdB, 0) + 1
                try {
                    gblDataFile.write(
                        byteArrayOf((datlen and 0xff).toByte(), ((datlen shr 8) and 0xff).toByte())
                    ) /* length code */
                    gblDataFile.write(bdB, 0, datlen) /* data */
                } catch (e: java.io.IOException) {
                    // vendor: fail silently
                }
            }

            leLongSetX(blk, boff, datptr) /* data address */
            boff += ADSIZE
            blk[boff] = 0
            boff++
            leIntSetX(blk, boff, 0) // memcpy(&block[blkoff], &zero, 4); (zeros)
            blk[boff + 4] = MMARK.toByte()
            blk[boff + 5] = 0
            // flush(svPtr); - the vendor comment fossil
            return OK_NB
        }
    }

    // ------------------------ end new tree section --------------------------

    /* *****************************************************************
                                search section
       ******************************************************************/

    if (cstrlenX(keyB, 0) > 256) {
        System.out.print("\n*** Global array reference too long:\n" + strBytesX(keyB) + "\n")
        sigint(SIGINT_BX)
    }

    var i = rootVal // disk address of tree root block
    BtreeXState.trx = -1

    var blk = rootBa.buf
    var ptr1off = 0
    var isz = 0
    val rtOut = IntArray(1)
    var ilong: Int
    var datptr: Long

    mainloop@ while (true) { // nxtblk :

        BtreeXState.trx++
        BtreeXState.trace[BtreeXState.trx] = i

        if (BtreeXState.trx > BtreeXState.trxmax) BtreeXState.trxmax = BtreeXState.trx

        if (BtreeXState.trx >= TRXLIMIT_BX) {
            System.out.print(
                "Btree trace stack limit reached\n" + strBytesX(keyB) +
                "\n" + BtreeXState.trx + " - increase TRXLIMIT\n"
            )
            sigint(SIGINT_BX)
        }

        var ba = freadx(i, UNMODIFIED) // fetch tree root block
        blk = ba.buf

        var phaseScan = true // the nxtkey / fail phase toggle
        var restartScan = false // the goto nxtkey re-entry flag

        inner@ while (true) {

            if (phaseScan) {

                if (restartScan) {
                    restartScan = false // goto nxtkey: ptr1 already primed
                } else {
                    ptr1off = 4 // skip child pointer
                }

                // scan keys in block; is is len of key scanned if positive
                isz = strxcmpX(keyB, blk, ptr1off, rtOut)
                while (isz > 0) {

                    if (g == PREVIOUS_BX) {
                        datptr = leLongX(blk, rtOut[0] + 1)
                        if (datptr >= 0L) {
                            prevBlk = blk // remember previous key
                            prevOff = ptr1off
                        }
                    }

                    // advance past key & data
                    ptr1off = rtOut[0] + ADSIZE + 5
                    isz = strxcmpX(keyB, blk, ptr1off, rtOut)
                }

                BtreeXState.tracep[BtreeXState.trx] = ptr1off - 4

                if (isz < 0) {

                    ilong = leIntX(blk, ptr1off - 4) // extract child block addr

                    if (ilong <= 0) { // no child - we're done
                        blkoff = ptr1off // integer offset in block
                        phaseScan = false // goto fail
                        continue@inner
                    }

                    i = ilong
                    continue@mainloop // goto nxtblk
                }

                // key found

                BtreeRuntime.FoundOffset = ptr1off
                BtreeRuntime.FoundBlock = i

                // p000:

                if (g == PREVIOUS_BX) {

                    ilong = leIntX(blk, ptr1off - 4) /* block address */

                    if (ilong <= 0) {
                        blkoff = ptr1off
                        // strcpy(bd, prev);
                        if (prevBlk != null) {
                            val pb2 = prevBlk!!
                            var q = 0
                            while (pb2[prevOff + q] != 0.toByte()) {
                                bdB[q] = pb2[prevOff + q]
                                q++
                            }
                            bdB[q] = 0
                        } else bdB[0] = 0
                        return 1
                    }

                    i = ilong
                    continue@mainloop // goto nxtblk

                    // the unreachable PREVIOUS tail (the NEXTMARK
                    // slice ladder) - vendor dead code after the
                    // goto (h013), kept as this ledger comment:
                    // nxtln = strlen(key)-2; blkoff = nxtln; the
                    // key[NEXTMARK] walk; the strncmp(prev,key,
                    // nxtln) comparison arm; the bd NEXTMARK
                    // slice; the leading-space strip (the
                    // while(bd[0]==' ') strcpy(bd,&bd[1]) ladder);
                    // the final OK return.
                }

                // -----------------------------------------------------------------------------------------------

                if (g == RETRIEVE) { // search operation

                    datptr = leLongX(blk, rtOut[0])

                    if (datptr <= 0L) {
                        bdB[0] = 0 // wmpty string (vendor typo fossil)
                        return OK_NB
                    } else {
                        val gblDataFile = gblDataRafX()
                        try {
                            gblDataFile.seek(datptr)
                            val lenb = ByteArray(2)
                            gblDataFile.readFully(lenb) // length of data
                            val datlen = (lenb[0].toInt() and 0xff) or ((lenb[1].toInt() and 0xff) shl 8)
                            gblDataFile.readFully(bdB, 0, datlen) // data (the
                            // STR_MAX bound is the vendor bd bound itself, kept)
                        } catch (e: java.io.IOException) {
                            // vendor: read fails silently
                        }
                        return OK_NB
                    }
                    // the vendor trailing return OK - dead tail (h013)
                }

                // -----------------------------------------------------------------------------------------------

                if (g == STORE) { /* store - reuse old record */

                    if (cstrlenX(keyB, 0) > 256) {
                        System.out.print("\n*** Global array reference too long:\n" + strBytesX(keyB) + "\n")
                        sigint(SIGINT_BX)
                    }

                    if (BtreeRuntime.ReadOnly != 0) {

                        // #ifdef READ_ONLY banner ledger

                        val sb12 = freadx(12, MODIFIED)
                        leIntSetX(sb12.buf, sb12.off, 0) /* mark as open - reset at close */
                        try {
                            BtreeRuntime.gblRaf?.seek(0)
                            BtreeRuntime.gblRaf?.write(BtreeRuntime.bfr[0]!!.gblbf, 0, MBLOCK) /* dump super block */
                        } catch (e: java.io.IOException) {
                            // vendor: fail silently
                        }
                        BtreeRuntime.ReadOnly = 0
                    }

                    ba = freadx(i, MODIFIED) // reload through the modified flag
                    blk = ba.buf

                    /* add new record - reuse old */

                    val ptrReuse = rtOut[0]
                    datptr = leLongX(blk, ptrReuse)

                    val isData = cstrlenX(bdB, 0) // size of data

                    // newrec / save / save1 ladder
                    var atNewrec = false
                    var atSave = false
                    var atSave1 = false
                    if (datptr <= 0L) atNewrec = true
                    else {
                        val gblDataFile = gblDataRafX()
                        try {
                            gblDataFile.seek(datptr)
                            val lenb = ByteArray(2)
                            gblDataFile.readFully(lenb) /* length code */
                            val datlen = (lenb[0].toInt() and 0xff) or ((lenb[1].toInt() and 0xff) shl 8)
                            if (datlen > isData) { // big enough
                                gblDataFile.seek(datptr)
                                atSave = true
                            } else {
                                /* delete old data record; add new at EOF */
                                atNewrec = true
                            }
                        } catch (e: java.io.IOException) {
                            // vendor: read fails silently (stale datlen 0)
                            atNewrec = true
                        }
                    }

                    if (atNewrec) {
                        if (isData == 0) {
                            datptr = 0L
                            atSave1 = true
                        } else {
                            val gblDataFile = gblDataRafX()
                            datptr = try {
                                gblDataFile.length()
                            } catch (e: java.io.IOException) {
                                0L
                            }
                            atSave = true
                        }
                    }

                    if (atSave && !atSave1) {
                        val datlen = if (atNewrec) isData + 1 else run {
                            // the reuse path's datlen is the just-read
                            // length code (re-read for the write)
                            val gblDataFile = gblDataRafX()
                            gblDataFile.seek(datptr)
                            val lenb = ByteArray(2)
                            gblDataFile.readFully(lenb)
                            (lenb[0].toInt() and 0xff) or ((lenb[1].toInt() and 0xff) shl 8)
                        }
                        val gblDataFile = gblDataRafX()
                        try {
                            gblDataFile.seek(datptr)
                            gblDataFile.write(
                                byteArrayOf((datlen and 0xff).toByte(), ((datlen shr 8) and 0xff).toByte())
                            ) /* length code */
                            gblDataFile.write(bdB, 0, datlen) /* data */
                        } catch (e: java.io.IOException) {
                            // vendor: fail silently
                        }
                    }

                    // save1:
                    leLongSetX(blk, ptrReuse, datptr) /* data address */
                    return OK_NB
                }

                blkoff = ptr1off

                // -------------------------------------------------------------------------------------------------------
                // KILLER
                // -------------------------------------------------------------------------------------------------------

                if (g == GKILL_BX) { /* delete */

                    if (BtreeRuntime.ReadOnly != 0) {

                        // #ifdef READ_ONLY banner ledger

                        val sb12 = freadx(12, MODIFIED)
                        leIntSetX(sb12.buf, sb12.off, 0)
                        try {
                            BtreeRuntime.gblRaf?.seek(0)
                            BtreeRuntime.gblRaf?.write(BtreeRuntime.bfr[0]!!.gblbf, 0, MBLOCK)
                        } catch (e: java.io.IOException) {
                            // vendor: fail silently
                        }
                        BtreeRuntime.ReadOnly = 0
                    }

                    ba = freadx(i, MODIFIED)
                    blk = ba.buf
                    ilong = leIntX(blk, ptr1off - 4)

                    var ix = MBLOCK - 1
                    while (blk[ix] == 0.toByte()) ix--
                    ix++

                    // remove the key (the memxmove tail-erase)
                    memxmove(
                        BPtr(blk, ptr1off - 4),
                        BPtr(blk, ptr1off + cstrlenX(blk, ptr1off) + ADSIZE + 2),
                        ix, MBLOCK
                    )

                    if (ilong == 0) {
                        return OK_NB
                    }

                    // strcpy(BigKey,""); BigKeyOffset = 0;
                    // BigKeyBlock = 0; Reinsert = ilong;
                    BtreeRuntime.BigKey[0] = 0
                    BtreeRuntime.BigKeyOffset = 0
                    BtreeRuntime.BigKeyBlock = 0
                    BtreeRuntime.Reinsert = ilong

                    scanblocks(ilong, svPtr) // pass the lower

                    if (BtreeRuntime.BigKey[0] == 0.toByte()) return OK_NB

                    // freadx(&block,Reinsert,MODIFIED); - the
                    // vendor's own overwritten reload (the next
                    // fetch replaces the image immediately;
                    // h013 reload ledger, kept as the call below)
                    freadx(BtreeRuntime.Reinsert, MODIFIED)

                    // ilong points to descendent chain of key to
                    // be deleted; Big... is the highest key in
                    // that chain; reload the lower block, remove
                    // the key
                    ba = freadx(BtreeRuntime.BigKeyBlock, MODIFIED)
                    blk = ba.buf
                    ptr1off = BtreeRuntime.BigKeyOffset

                    ix = MBLOCK - 1
                    while (blk[ix] == 0.toByte()) ix--
                    ix++

                    memxmove(
                        BPtr(blk, ptr1off - 4),
                        BPtr(blk, ptr1off + cstrlenX(blk, ptr1off) + ADSIZE + 2),
                        ix, MBLOCK
                    )

                    ix = MBLOCK - 1
                    while (blk[ix] == 0.toByte()) ix--
                    leIntSetX(blk, ix - 4, BtreeRuntime.BigKeyLower) // put stray descendants at end

                    if (blk[4] == MMARK.toByte() && BtreeRuntime.BigKeyBlock == BtreeRuntime.Reinsert) { // collapse empty immediate lower block
                        ilong = leIntX(blk, 0) // its descendent, if any
                        if (ilong >= 0) BtreeRuntime.Reinsert = ilong
                    } else if (blk[4] == MMARK.toByte()) { // collapse non-immediate empty lower block
                        ilong = leIntX(blk, 0) // its descendent, if any
                        val pbBa = freadx(BtreeRuntime.BigKeyParent, MODIFIED)
                        leIntSetX(pbBa.buf, pbBa.off + BtreeRuntime.BigKeyParentOffset, ilong)
                    }

                    // reinsert key with descendents

                    svPtr.restoredata = BtreeRuntime.BigKeyData
                    // strcpy(bd,"xxx");
                    bdB[0] = 'x'.code.toByte()
                    bdB[1] = 'x'.code.toByte()
                    bdB[2] = 'x'.code.toByte()
                    bdB[3] = 0

                    run { // the STR_MAX tmp
                        val tmp = ByteArray(STR_MAX)
                        var q = 0
                        while (q < STR_MAX && BtreeRuntime.BigKey[q] != 0.toByte()) {
                            tmp[q] = BtreeRuntime.BigKey[q]
                            q++
                        }
                        if (q < STR_MAX) tmp[q] = 0
                        btreeXCore(RESTORE_BX, tmp, bdB, svPtr)
                    }
                    btreeXCore(RETRIEVE, BtreeRuntime.BigKey, bdB, svPtr)

                    // memcpy(&ilong, block+FoundOffset-4, 4); -
                    // the stale block cursor is the vendor's own
                    // alias hazard, preserved (h007 ledger)
                    ilong = leIntX(blk, BtreeRuntime.FoundOffset - 4)

                    if (ilong == 0) {
                        val fbBa = freadx(BtreeRuntime.FoundBlock, MODIFIED)
                        leIntSetX(fbBa.buf, fbBa.off + BtreeRuntime.FoundOffset - 4, BtreeRuntime.Reinsert)
                    } else {
                        System.out.print("open not slot found BigKey=" + strBytesX(BtreeRuntime.BigKey) + "\n")
                        // #ifdef DumpBlocks: pb(FoundBlock, block);
                        // (the dumper call rides the roster-111 stub)
                        pb(BtreeRuntime.FoundBlock, blk)
                        sigint(100)
                    }

                    // flush(svPtr); - the vendor comment fossil
                    return OK_NB
                }

                // ------------------------------------------------------------------------------------------

                if (g == XNEXT) { /* found next */

                    blkoff += cstrlenX(blk, blkoff) + ADSIZE + 6 /* next key */
                    ilong = leIntX(blk, blkoff - 4) /* look for less-than childrn */

                    if (ilong <= 0) {
                        phaseScan = false // goto fail
                        continue@inner
                    }

                    i = ilong
                    BtreeXState.tracep[BtreeXState.trx] = blkoff - 4 /* new parent */
                    continue@mainloop // goto nxtblk
                }

                /*end found */

                bdB[0] = 0
                return 0 /* default failure */
            }

            // ---------------------------------------------------------------------
            // fail:
            // ---------------------------------------------------------------------

            if (g == PREVIOUS_BX) {
                // goto p000 re-entry: uses the stale ptr1 cursor
                // exactly as the vendor
                ilong = leIntX(blk, ptr1off - 4)

                if (ilong <= 0) {
                    blkoff = ptr1off
                    if (prevBlk != null) {
                        val pb2 = prevBlk!!
                        var q = 0
                        while (pb2[prevOff + q] != 0.toByte()) {
                            bdB[q] = pb2[prevOff + q]
                            q++
                        }
                        bdB[q] = 0
                    } else bdB[0] = 0
                    return 1
                }

                i = ilong
                continue@mainloop

                // the unreachable PREVIOUS tail ledger (see above)
            }

            // g == XNEXT here (the STORE insert path renders below)

            if (g == STORE) {

                // ------------------------------------------------------------
                // STORE section - store new key (the insert/split)
                // ------------------------------------------------------------

                var blkaddr1 = 0

                if (restoreFlag) datptr = svPtr.restoredata
                else if (bdB[0] == 0.toByte()) datptr = 0L // zero length data field
                else {
                    val gblDataFile = gblDataRafX()
                    datptr = try {
                        gblDataFile.length()
                    } catch (e: java.io.IOException) {
                        0L
                    }
                    val datlen = cstrlenX(bdB, 0) + 1
                    try {
                        gblDataFile.write(
                            byteArrayOf((datlen and 0xff).toByte(), ((datlen shr 8) and 0xff).toByte())
                        ) /* length code */
                        gblDataFile.write(bdB, 0, datlen) /* data */
                    } catch (e: java.io.IOException) {
                        // vendor: fail silently
                    }
                }

                var f1Again = true // f1: (the label re-entry flag)
                while (f1Again) {
                    f1Again = false

                    ba = freadx(i, MODIFIED) /* marked for write */
                    blk = ba.buf

                    // unsigned char *lsp = block + blkoff;
                    var lsp = blkoff
                    while (blk[lsp] != MMARK.toByte()) { // find block end
                        while (blk[lsp] != 0.toByte()) lsp++
                        lsp++
                        lsp += ADSIZE + 5
                    }

                    val ls = lsp // occupied length

                    val ks = cstrlenX(keyB, 0) // key to be added length

                    if (ks + ls <= BLOCK_SPLIT) {

                        // ------------------------------------------------------------
                        // Simple store insert
                        // ------------------------------------------------------------

                        // The next block of code substitutes for memmove
                        // It copies beginning at the right (high) end of the
                        // strings to avoid the overlap problem.

                        var length = ls - (blkoff - 4)

                        var to = (blkoff - 4) + ks + ADSIZE + 6 + length
                        var from = blkoff - 4 + length

                        length++
                        while (length > 0) {
                            length--
                            blk[to] = blk[from]
                            to--
                            from--
                        }

                        to = blkoff - 4

                        // from = &blkaddr1; the 4-byte child install
                        blk[to] = (blkaddr1 and 0xff).toByte()
                        blk[to + 1] = ((blkaddr1 shr 8) and 0xff).toByte()
                        blk[to + 2] = ((blkaddr1 shr 16) and 0xff).toByte()
                        blk[to + 3] = ((blkaddr1 shr 24) and 0xff).toByte()
                        to += 4

                        // to = memccpy(to, key, 0, MBLOCK); - copies
                        // through the NUL, cursor past it
                        var kx = 0
                        while (keyB[kx] != 0.toByte()) {
                            blk[to] = keyB[kx]
                            to++
                            kx++
                        }
                        blk[to] = 0
                        to++

                        // from = &datptr; the ADSIZE data install
                        for (dk in 0 until ADSIZE) {
                            blk[to + dk] = ((datptr shr (8 * dk)) and 0xff).toByte()
                        }
                        to += ADSIZE

                        blk[to] = 0

                        return OK_NB
                    }

                    // ------------------------------------------------------------
                    // split insert section - split required
                    // insert prior to blkoff-4
                    // ------------------------------------------------------------

                    // static unsigned char bufblk[DBL_STR+2]
                    val bufblk = ByteArray(DBL_STR + 2)

                    var length = blkoff - 4
                    val ii = length

                    var to = 0
                    var from = 0
                    while (length > 0) {
                        length--
                        bufblk[to] = blk[from]
                        to++
                        from++
                    }

                    // memcpy(&bufblk[blkoff - 4], &blkaddr1, 4); child
                    bufblk[blkoff - 4] = (blkaddr1 and 0xff).toByte()
                    bufblk[blkoff - 3] = ((blkaddr1 shr 8) and 0xff).toByte()
                    bufblk[blkoff - 2] = ((blkaddr1 shr 16) and 0xff).toByte()
                    bufblk[blkoff - 1] = ((blkaddr1 shr 24) and 0xff).toByte()

                    // strcpy(&bufblk[blkoff], key); key
                    var kx = 0
                    while (keyB[kx] != 0.toByte()) {
                        bufblk[blkoff + kx] = keyB[kx]
                        kx++
                    }
                    bufblk[blkoff + kx] = 0

                    blkoff += ks + 1

                    // memcpy(&bufblk[blkoff], &datptr, ADSIZE); data address
                    for (dk in 0 until ADSIZE) {
                        bufblk[blkoff + dk] = ((datptr shr (8 * dk)) and 0xff).toByte()
                    }

                    bufblk[blkoff + ADSIZE] = 0

                    // to = &bufblk[blkoff+ADSIZE+1]; from = &block[ii];
                    // memcpy(to, from, MBLOCK - ii); (the commented
                    // while-loop pair kept as the vendor fossil)
                    to = blkoff + ADSIZE + 1
                    System.arraycopy(blk, ii, bufblk, to, MBLOCK - ii)
                    to += MBLOCK - ii
                    // memset(to, 0, MBLOCK); zero 2nd half of block -
                    // same len as 1st half
                    for (zk in 0 until MBLOCK) bufblk[to + zk] = 0

                    // find a mid-block key boundary
                    from = 0
                    val mid = MBLOCK / 2
                    while (from < mid) { // find middle key
                        from += 4
                        while (bufblk[from] != 0.toByte()) from++
                        from++
                        from += ADSIZE + 1
                    }

                    blkoff = from

                    // if (!strcmp(from + 4, key)) { from += 4;
                    // from += strlen(from) + ADSIZE + 2; }
                    if (strcmpX(bufblk, from + 4, keyB) == 0) {
                        from += 4
                        from += cstrlenX(bufblk, from) + ADSIZE + 2
                    }

                    from += 4

                    val toMark = from

                    // extract key & advance to data pointer:
                    // from = strxxcpy(key, from) + 1;
                    from = strxxcpy(BPtr(keyB, 0), BPtr(bufblk, from)) + 1

                    // memcpy(&datptr, from, ADSIZE); extract data to be sent up
                    datptr = leLongX(bufblk, from)

                    blkoff = from

                    // advance past data pointer
                    from += ADSIZE + 1

                    ba = freadx(i, MODIFIED) /* make memory resident */
                    blk = ba.buf

                    // memcpy(block, from, MBLOCK); right block - zeroed?
                    System.arraycopy(bufblk, from, blk, 0, MBLOCK)

                    // memset(to, 0, DBL_STR - (to - bufblk));
                    for (zk in 0 until DBL_STR - toMark) bufblk[toMark + zk] = 0

                    bufblk[toMark] = MMARK.toByte()
                    bufblk[toMark + 1] = 0

                    blkaddr1 = allocblk(svPtr) /* allocate new left block */
                    ba = freadx(blkaddr1, MODIFIED) /* make memory resident */
                    // memcpy(block1, bufblk, MBLOCK); left part of block
                    System.arraycopy(bufblk, 0, ba.buf, 0, MBLOCK)

                    /* fix parent */

                    BtreeXState.trx--

                    if (BtreeXState.trx < 0) {

                        // rootfix:
                        val m = allocblk(svPtr)
                        ba = freadx(0, MODIFIED)
                        leIntSetX(ba.buf, ba.off, m)
                        ba = freadx(m, MODIFIED)

                        leIntSetX(ba.buf, ba.off, blkaddr1)
                        // strcpy(&block[4], key);
                        var q = 4
                        kx = 0
                        while (keyB[kx] != 0.toByte()) {
                            ba.buf[q] = keyB[kx]
                            q++
                            kx++
                        }
                        ba.buf[q] = 0
                        blkoff = 5 + cstrlenX(keyB, 0)
                        for (dk in 0 until ADSIZE) {
                            ba.buf[blkoff + dk] = ((datptr shr (8 * dk)) and 0xff).toByte()
                        }
                        ba.buf[blkoff + ADSIZE] = 0
                        blkoff += ADSIZE + 1
                        // memcpy(&block[blkoff], &trace[0], 4); the
                        // OLD ROOT becomes the new root's child
                        leIntSetX(ba.buf, blkoff, BtreeXState.trace[0])
                        ba.buf[blkoff + 4] = MMARK.toByte()
                        ba.buf[blkoff + 5] = 0
                        // flush(svPtr); - the vendor comment fossil
                        return OK_NB
                    }

                    i = BtreeXState.trace[BtreeXState.trx]
                    blkoff = BtreeXState.tracep[BtreeXState.trx] + 4
                    f1Again = true // goto f1
                }
            }

            // ---------------------------------------------------------------------
            // XNEXT fail arms
            // ---------------------------------------------------------------------

            if (blk[blkoff] != MMARK.toByte()) { // not at end of block

                // modify key: copy global array ref to both key
                // and bd (the tandem copy loop)
                var fromX = blkoff
                var toX = 0
                var toxX = 0
                while (true) {
                    val c = blk[fromX]
                    keyB[toX] = c
                    bdB[toxX] = c
                    if (c == 0.toByte()) break
                    fromX++
                    toX++
                    toxX++
                }
                keyB[toX] = 0
                bdB[toxX] = 0

                fromX++ // past the NUL

                datptr = leLongX(blk, fromX) /* data ptr */

                if (datptr < 0L) {
                    ptr1off = blkoff - 4
                    phaseScan = true // goto nxtkey
                    restartScan = true
                    continue@inner
                }

                return OK_NB
            }

            // end of block mark found

            upblock@ while (true) {

                BtreeXState.trx--

                if (BtreeXState.trx < 0) {
                    bdB[0] = 0
                    return BAD_NB
                }

                i = BtreeXState.trace[BtreeXState.trx]
                blkoff = BtreeXState.tracep[BtreeXState.trx] + 4 /* key */
                ba = freadx(i, UNMODIFIED)
                blk = ba.buf

                if (blk[blkoff] != MMARK.toByte()) break@upblock
                // goto upblock
            }

            // goto fail: re-tests the XNEXT fail arm on the
            // refetched parent block (phaseScan stays false)
            continue@inner
        }
    }
}

// strcmp over the byte image at offset vs the key (the split
// equal-key arm)
private fun strcmpX(buf: ByteArray, offc: Int, keyB: ByteArray): Int {
    var p = offc
    var q = 0
    while (true) {
        val a = buf[p].toLong() and 0xff
        val b = keyB[q].toLong() and 0xff
        if (a != b) return (a - b).toInt()
        if (a == 0L) return 0
        p++
        q++
    }
}
