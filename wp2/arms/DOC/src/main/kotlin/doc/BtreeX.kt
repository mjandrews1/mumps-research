package doc

// Unit 270 (B9 tranche 3): interp-native-btree.cpp::btreeX
// (batch 9, complex stratum; units.csv row 250: loc 502, score
// 393.4, rank 219; physLOC 502 physically verified this
// session) - the native btree global-array engine (the
// open/auto-open, the XNEXT/PREVIOUS walk, the search, the
// RETRIEVE, the STORE reuse path, the KILL chain, the INSERT
// with split, the trace stack). Consult H-014 hit-adapt
// (2026-11-22T05:33:00Z): the Btree.kt unit-4 dispatcher's
// btreeX seat (formerly tagged "not a roster unit" - the
// tag corrected in B9 T3) FLIPS to this render; the Mglobal
// family call sites bind unchanged.
//
// BYTE-DOMAIN FOLD (H-012/H-016): the vendor key[] and bd[]
// are unsigned char throughout (the block images, BigKey,
// bufblk are all byte domain); the dispatcher-facing seat
// keeps the CharArray signature (unit 4) and the render wraps
// through byte-domain copies (entry copy, exit write-back);
// internal cursors are the H-016 file-local idiom of the
// unit-272 misc render (BPtr holders, LE int/long int
// get/set, cstrlen). isz adapts the vendor is (Kotlin
// keyword).
//
// H-007 ALIAS FOLD: the vendor char* block cursor is an
// ALIAS into bfr[indx]->gblbf; the unit-209 freadx ByteArray
// seat returns a copy, so every mutation-bearing fetch binds
// blk to aliasOf(r).gblbf directly (the flush precedent): the
// fwrite path sees the same bytes the vendor writes through
// the alias. The stale-pointer hazards (ptr1 across upblock
// refetches, prev across evictions) are the vendor's own
// aliasing semantics and are preserved by construction
// (same array object, same offsets).
//
// H-018 JVM-DOMAIN ADJUDICATIONS: open()/close() ride the
// RandomAccessFile carriers (gblFile landed unit 209; the
// gbldat carrier added to BtreeStatic as the EDIT-ONLY seat
// by this render, the gblFile precedent). The O_EXCL
// without O_CREAT quirk (a POSIX no-op the vendor banner
// mislabels "fails if file already exists") folds onto the
// exists-probe + create path. flock(LOCK_EX) has no JVM
// carrier (FileChannel locking is advisory and
// platform-dependent) - kept as the inert ledger comment, the
// do-not-proceed intent cited. The create-path close+goto
// reopen pair folds onto keeping the carrier open (the
// reopen loop's observable content is the integrity check).
// NEW_TREE (#ifdef pid-named temp files; excludes) and
// READ_ONLY (#ifdef arms, excluded build) stay excluded dead
// arms per H-018, their banners kept in ledgers. getpid()
// has no non-dead use (NEW_TREE excluded).
//
// H-013 dead-lead ledgers kept verbatim: the rtf/nxtln prep
// ladder (rtf = 1 tested true then nxtln assigned three
// times ending 0; the whole if-block is dead storage), the
// unreachable PREVIOUS tail after the p000 goto nxtblk (the
// NEXTMARK slice ladder), the unused locals (upper,
// upperoffset, vv[2], downblocknbr, kk, the trailing
// block[blkoff+4] writes in the insert arms are live), the
// double fwrite of the KILL arm (freadx(Reinsert,MODIFIED)
// immediately overwritten by the BigKeyBlock fetch - the
// vendor's own reload, kept as the call ledger).
// Naming per H-017 (btreeX, fopen banners verbatim; RESTORE
// code 20 per defines.h:90; PREVIOUS 9 per :89; KILL rides
// the Carriers GKILL 3 seat per defines.h:86; CLOSE 4 per
// defines.h:87; NEXTMARK 1 per defines.h:122; TRXLIMIT
// 100000 per defines.h:121).

private const val PREVIOUS_BX = 9 // defines.h:89
private const val RESTORE_BX = 20 // defines.h:90
private const val CLOSE_BX = 4 // defines.h:87
private const val NEXTMARK_BX = 1 // defines.h:122
private const val TRXLIMIT_BX = 100000 // defines.h:121
private const val SIGINT_BX = 2 // vendor sigint(SIGINT) macro seat

// the vendor function-static storage (the FreadxState
// precedent); the pass-through scratch statics (ilong, m, ks,
// ... ) render as call locals (single-call lifetime, the
// vendor's own usage pattern, H-013 note)
object BtreeXState {
    var trxmax = 0
    val trace = IntArray(TRXLIMIT_BX)
    val tracep = IntArray(TRXLIMIT_BX)
    var opnflg = 0
    var rtf = 0
}

// file-private LE int/long seats over the block image (the
// unit-272 misc twins, repeated file-locally per the
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

// char-domain to byte-domain entry copy (H-012 fold)
private fun toBytesX(src: CharArray): ByteArray {
    val out = ByteArray(STR_MAX)
    var k = 0
    while (k < src.size && src[k] != NUL && k < STR_MAX) {
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
    if (k < dst.size) dst[k] = NUL
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

// the gblfd alias seat: bind the block to the cache slot's
// actual buffer (the H-007 alias fold; call AFTER the freadx
// that loads r - the FreadxState.indx read is the unit-209
// modulo index of the load just performed)
private fun aliasOfX(r: Int): BtreeBuffers =
    if (r < 16) BtreeBlockCache.slot(0)
    else BtreeBlockCache.slot(FreadxState.indx)

// the data-file carrier backstop (the Freadx gblraf twin)
private fun gblDataRafX(): java.io.RandomAccessFile =
    BtreeStatic.gblDataFile
        ?: throw IllegalStateException(
            "gbldat: btree data file not opened (vendor gbldat -1 sentinel)")

// the byte-image strxcmp consult (wraps the unit-272 render's
// signature into the (first, cursor, out) shape of the search)
private fun strxcmpX(keyB: ByteArray, blk: ByteArray, offc: Int, rtOut: IntArray): Int =
    strxcmp(BPtr(keyB, 0), BPtr(blk, offc), rtOut)

fun btreeX(gIn: Int, key: CharArray, bd: CharArray, svPtr: Msv): Int {
    val keyB = toBytesX(key)
    val bdB = toBytesX(bd)
    val r = btreeXCore(gIn, keyB, bdB, svPtr)
    fromBytesX(key, keyB)
    fromBytesX(bd, bdB)
    return r
}

// the engine core (byte domain; the recursive KILL arm calls
// this core directly - the entry/exit copies are the wrapper's)
private fun btreeXCore(gArg: Int, keyB: ByteArray, bdB: ByteArray, svPtr: Msv): Int {
    var g = gArg

    // int ... restoreFlag=0;
    var restoreFlag = false

    if (g == RESTORE_BX) {
        restoreFlag = true // special re-insertion case during deletes
        g = STORE
    }

    if (BtreeXState.opnflg == 0 && g == CLOSE_BX) return OK_NB // files not open - no need to close

    // rtf = 1; (the retry flag; per-call priming makes the
    // static lifetime inert - the prep block below is the
    // vendor's own dead ladder, H-013, kept as the ledger)
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
        // the vendor dead-ladder (H-013): for (nxtln =
        // blkoff-2; ...key[nxtln] != NEXTMARK...); nxtln++ ;
        // nxtln = blkoff-1; nxtln = 0; - the final assignment
        // nulls the walk, and nxtln's only later reads sit in
        // the unreachable PREVIOUS tail (below). Ledger only.
        BtreeXState.rtf = 0
    }

    /* *****************************************
       section to open main data and key files
       ***************************************** */

    if (BtreeXState.opnflg == 0) { /* auto open */

        // for (i = 0; i < GBLBUF; i++) { malloc buffers; } -
        // the pre-allocation loop folds onto the unit-209
        // lazy BtreeBlockCache (the slot() born-at-sentinel
        // carrier); the printf/sigint OOM banner is the
        // vendor's malloc failure arm (JVM OOM is the
        // native hazard fold, ledger)
        for (bx in 0 until GBLBUF) BtreeBlockCache.slot(bx)
        BtreeXState.opnflg = 1

        // #ifdef NEW_TREE: the pid-named keyN.tmp/datN.tmp
        // sprintf pair - excluded build (H-018 ledger)

        // reopen: - the vendor close+goto reopen loop folds
        // onto the kept-open carrier (header ledger)

        // open fails (O_EXCL) if file already exists
        // (the vendor banner fossil: O_EXCL without O_CREAT
        // is a POSIX no-op; the fold is the exists-probe)
        // #ifndef READ_ONLY arms:
        val ukFile = java.io.File(CstringOps.cstring(svPtr.UKEY))
        val udFile = java.io.File(CstringOps.cstring(svPtr.UDAT))

        val ukExists = ukFile.exists()
        val udExists = udFile.exists()

        // the EACCES banners (errno == EACCES) fold onto the
        // JVM access faults (the RandomAccessFile open throw
        // of the rw probe on unreadable files, ledger; the
        // banner text kept verbatim)
        BtreeStatic.gblFile =
            if (ukExists) java.io.RandomAccessFile(ukFile, "rw") else null
        BtreeStatic.gblfd = if (ukExists) 1 else -1

        BtreeStatic.gblDataFile =
            if (udExists) java.io.RandomAccessFile(udFile, "rw") else null
        BtreeStatic.gbldat = if (udExists) 1 else -1

        // unix file locking: LOCK_EX is a system symbol; will
        // wait for file if already locked - flock(gblfd,
        // LOCK_EX) has no JVM carrier (advisory-lock
        // divergence cited; the do-not-proceed intent rides
        // the carrier itself), inert ledger

        if (BtreeStatic.gblfd == -1) { /* Does not exist - create and initialize */

            // #ifdef READ_ONLY: "Attempt to create read only
            // native database" banner - excluded build ledger

            BtreeStatic.gblFile = java.io.RandomAccessFile(ukFile, "rw")
            BtreeStatic.gblfd = 1
            BtreeStatic.gblDataFile = java.io.RandomAccessFile(udFile, "rw")
            BtreeStatic.gbldat = 1

            val gblFile = BtreeStatic.gblFile
                ?: throw IllegalArgumentException("null gblFile seat")

            val b0 = BtreeBlockCache.slot(0) /* get a buffer address */
            for (bx in 0 until MBLOCK) b0.gblbf[bx] = 0

            for (isx in 0..4) { /* init to zeros */
                gblFile.seek(isx.toLong() * MBLOCK)
                gblFile.write(b0.gblbf, 0, MBLOCK)
            }

            val gblDataFile = BtreeStatic.gblDataFile
                ?: throw IllegalArgumentException("null gblDataFile seat")
            gblDataFile.seek(0) /* move to origin */
            gblDataFile.write(byteArrayOf(0, 0)) /* init data file */

            // the super-block marks through the slot-0 alias
            leIntSetX(b0.gblbf, 12, 777) /* integrity flg */
            leIntSetX(b0.gblbf, 0, 0) /* root */
            leIntSetX(b0.gblbf, 4, 0) /* no meaning */
            leIntSetX(b0.gblbf, 8, 16) /* extent */

            gblFile.seek(0)
            gblFile.write(b0.gblbf, 0, MBLOCK) /* root block */

            // close(gblfd); close(gbldat); goto reopen; - the
            // close+reopen pair folds onto the kept-open
            // carrier (H-018, header ledger)
        }

        // freadx((unsigned char **) &root, 12L, MODIFIED); the
        // integrity flag consult (the direct alias read)
        val sb = BtreeBlockCache.slot(0).gblbf
        val integ = leIntX(sb, 12)

        if (integ != 777) {
            println("\nFile system error in program " + CstringOps.cstring(svPtr.program))
            // abort() renders as the thrown RuntimeException after
            // the banner (the Abrtx.kt precedent)
            throw RuntimeException("abort: btree integrity flag failure")
        }

        /*..........................................
           now done only when file system altered .....
           the commented fossil kept per the vendor
           ............................................ */
    }

    if (g == FLUSH) {
        flush(svPtr)
        return OK_NB
    }

    if (g == CLOSE_BX) { /* CLOSE SECTION */

        // #ifdef NEW_TREE: the remove(UKEY/UDAT) pair -
        // excluded build ledger

        if (BtreeXState.opnflg == 0) return OK_NB

        flush(svPtr)

        BtreeStatic.gblFile?.close()
        BtreeStatic.gblFile = null
        BtreeStatic.gblfd = -1
        BtreeStatic.gblDataFile?.close()
        BtreeStatic.gblDataFile = null
        BtreeStatic.gbldat = -1
        BtreeXState.opnflg = 0
        return OK_NB
    }

    // freadx((unsigned char **) &root, 0L, UNMODIFIED);
    freadx(0, UNMODIFIED)
    val rootVal = leIntX(BtreeBlockCache.slot(0).gblbf, 0)

    // -----------------------------------------------------------------------------
    //                            new tree
    // -----------------------------------------------------------------------------

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

            if (BtreeStatic.ReadOnly != 0) {

                // #ifdef READ_ONLY: "Native database read only
                // violation" banner - excluded build ledger

                // freadx(&RDFlg, 12L, MODIFIED); *RDFlg = 0;
                leIntSetX(BtreeBlockCache.slot(0).gblbf, 12, 0) /* mark as open - reset at close */
                (BtreeStatic.gblFile
                    ?: throw IllegalArgumentException("null gblFile seat")).seek(0)
                (BtreeStatic.gblFile
                    ?: throw IllegalArgumentException("null gblFile seat")).write(BtreeBlockCache.slot(0).gblbf, 0, MBLOCK) /* dump super block */
                BtreeStatic.ReadOnly = 0
            }

            var blkaddr1 = allocblk(svPtr)
            freadx(0, MODIFIED)
            var blk = BtreeBlockCache.slot(0).gblbf
            leIntSetX(blk, 0, blkaddr1) // the root child pointer
            freadx(blkaddr1, MODIFIED) /* k block */
            blk = aliasOfX(blkaddr1).gblbf

            for (bx in 0 until MBLOCK) blk[bx] = 0

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
                gblDataFile.write(byteArrayOf((datlen and 0xff).toByte(), ((datlen shr 8) and 0xff).toByte())) /* length code */
                gblDataFile.write(bdB, 0, datlen) /* data */
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
        println("\n*** Global array reference too long:\n" + strBytesX(keyB))
        sigint(SIGINT_BX)
    }

    var i = rootVal // disk address of tree root block
    BtreeStatic.trx = -1

    var blk = BtreeBlockCache.slot(0).gblbf
    var ptr1off = 0
    var isz = 0
    val rtOut = IntArray(1)
    var ilong: Int
    var datptr: Long

    mainloop@ while (true) { // nxtblk :

        BtreeStatic.trx++
        BtreeXState.trace[BtreeStatic.trx] = i

        if (BtreeStatic.trx > BtreeXState.trxmax) BtreeXState.trxmax = BtreeStatic.trx

        if (BtreeStatic.trx >= TRXLIMIT_BX) {
            println("Btree trace stack limit reached\n" + strBytesX(keyB) + "\n" + BtreeStatic.trx + " - increase TRXLIMIT")
            sigint(SIGINT_BX)
        }

        freadx(i, UNMODIFIED) // fetch tree root block
        blk = aliasOfX(i).gblbf

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

                BtreeXState.tracep[BtreeStatic.trx] = ptr1off - 4

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

                FoundOffset = ptr1off
                FoundBlock = i

                // p000:

                if (g == PREVIOUS_BX) {

                    ilong = leIntX(blk, ptr1off - 4) /* block address */

                    if (ilong <= 0) {
                        blkoff = ptr1off
                        // strcpy(bd, prev);
                        if (prevBlk != null) {
                            val pb2 = prevBlk
                                ?: throw IllegalArgumentException("null prevBlk seat")
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
                    // goto (H-013), kept as this ledger comment:
                    // nxtln = strlen(key)-2; blkoff = nxtln; the
                    // key[NEXTMARK] walk; the strncmp(prev,key,
                    // nxtln) comparison arm; the bd NEXTMARK
                    // slice; the leading-space strip (the
                    // while(bd[0]==' ') strcpy(bd,&bd[1]) ladder);
                    // the final OK return.
                }

                // ------------------------------------------------------------------

                if (g == RETRIEVE) { // search operation

                    datptr = leLongX(blk, rtOut[0])

                    if (datptr <= 0L) {
                        bdB[0] = 0 // wmpty string (vendor typo fossil)
                        return OK_NB
                    } else {
                        val gblDataFile = gblDataRafX()
                        gblDataFile.seek(datptr)
                        val lenb = ByteArray(2)
                        gblDataFile.readFully(lenb) // length of data
                        val datlen = (lenb[0].toInt() and 0xff) or ((lenb[1].toInt() and 0xff) shl 8)
                        gblDataFile.readFully(bdB, 0, datlen) // data (the
                        // STR_MAX bound is the vendor bd bound itself, kept)
                        return OK_NB
                    }
                    // the vendor trailing return OK - dead tail (H-013)
                }

                // ------------------------------------------------------------------

                if (g == STORE) { /* store - reuse old record */

                    if (cstrlenX(keyB, 0) > 256) {
                        println("\n*** Global array reference too long:\n" + strBytesX(keyB))
                        sigint(SIGINT_BX)
                    }

                    if (BtreeStatic.ReadOnly != 0) {

                        // #ifdef READ_ONLY banner ledger

                        leIntSetX(BtreeBlockCache.slot(0).gblbf, 12, 0) /* mark as open - reset at close */
                        (BtreeStatic.gblFile
                    ?: throw IllegalArgumentException("null gblFile seat")).seek(0)
                        (BtreeStatic.gblFile
                    ?: throw IllegalArgumentException("null gblFile seat")).write(BtreeBlockCache.slot(0).gblbf, 0, MBLOCK) /* dump super block */
                        BtreeStatic.ReadOnly = 0
                    }

                    freadx(i, MODIFIED) // reload through the modified flag
                    blk = aliasOfX(i).gblbf

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
                    }

                    if (atNewrec) {
                        if (isData == 0) {
                            datptr = 0L
                            atSave1 = true
                        } else {
                            val gblDataFile = gblDataRafX()
                            datptr = gblDataFile.length()
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
                        gblDataFile.seek(datptr)
                        gblDataFile.write(byteArrayOf((datlen and 0xff).toByte(), ((datlen shr 8) and 0xff).toByte())) /* length code */
                        gblDataFile.write(bdB, 0, datlen) /* data */
                    }

                    // save1:
                    leLongSetX(blk, ptrReuse, datptr) /* data address */
                    return OK_NB
                }

                blkoff = ptr1off

                // ----------------------------------------------------------------------
                // KILLER
                // ----------------------------------------------------------------------

                if (g == GKILL) { /* delete */

                    if (BtreeStatic.ReadOnly != 0) {

                        // #ifdef READ_ONLY banner ledger

                        leIntSetX(BtreeBlockCache.slot(0).gblbf, 12, 0)
                        (BtreeStatic.gblFile
                    ?: throw IllegalArgumentException("null gblFile seat")).seek(0)
                        (BtreeStatic.gblFile
                    ?: throw IllegalArgumentException("null gblFile seat")).write(BtreeBlockCache.slot(0).gblbf, 0, MBLOCK)
                        BtreeStatic.ReadOnly = 0
                    }

                    freadx(i, MODIFIED)
                    blk = aliasOfX(i).gblbf
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
                    BigKey[0] = 0
                    BigKeyOffset = 0
                    BigKeyBlock = 0
                    Reinsert = ilong

                    scanblocks(ilong, svPtr) // pass the lower

                    if (BigKey[0] == 0.toByte()) return OK_NB

                    // freadx(&block,Reinsert,MODIFIED); - the
                    // vendor's own overwritten reload (the next
                    // fetch replaces the image immediately;
                    // H-013 reload ledger, kept as the call below)
                    freadx(Reinsert, MODIFIED)

                    // ilong points to descendent chain of key to
                    // be deleted; Big... is the highest key in
                    // that chain; reload the lower block, remove
                    // the key
                    freadx(BigKeyBlock, MODIFIED)
                    blk = aliasOfX(BigKeyBlock).gblbf
                    ptr1off = BigKeyOffset

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
                    leIntSetX(blk, ix - 4, BigKeyLower) // put stray descendants at end

                    if (blk[4] == MMARK.toByte() && BigKeyBlock == Reinsert) { // collapse empty immediate lower block
                        ilong = leIntX(blk, 0) // its descendent, if any
                        if (ilong >= 0) Reinsert = ilong
                    } else if (blk[4] == MMARK.toByte()) { // collapse non-immediate empty lower block
                        ilong = leIntX(blk, 0) // its descendent, if any
                        freadx(BigKeyParent, MODIFIED)
                        leIntSetX(aliasOfX(BigKeyParent).gblbf, BigKeyParentOffset, ilong)
                    }

                    // reinsert key with descendents

                    svPtr.restoredata = BigKeyData
                    // strcpy(bd,"xxx");
                    bdB[0] = 'x'.code.toByte()
                    bdB[1] = 'x'.code.toByte()
                    bdB[2] = 'x'.code.toByte()
                    bdB[3] = 0

                    run { // the STR_MAX tmp
                        val tmp = ByteArray(STR_MAX)
                        var q = 0
                        while (q < STR_MAX && BigKey[q] != 0.toByte()) {
                            tmp[q] = BigKey[q]
                            q++
                        }
                        if (q < STR_MAX) tmp[q] = 0
                        btreeXCore(RESTORE_BX, tmp, bdB, svPtr)
                    }
                    btreeXCore(RETRIEVE, BigKey, bdB, svPtr)

                    // memcpy(&ilong, block+FoundOffset-4, 4); -
                    // the stale block cursor is the vendor's own
                    // alias hazard, preserved (H-007 ledger)
                    ilong = leIntX(blk, FoundOffset - 4)

                    if (ilong == 0) {
                        freadx(FoundBlock, MODIFIED)
                        leIntSetX(aliasOfX(FoundBlock).gblbf, FoundOffset - 4, Reinsert)
                    } else {
                        println("open not slot found BigKey=" + strBytesX(BigKey))
                        // #ifdef DumpBlocks: pb(FoundBlock, block);
                        // (the dumper call - the pb signature takes
                        // the image copy; the stale alias passes
                        // the current slot content)
                        pb(FoundBlock, blk)
                        sigint(100)
                    }

                    // flush(svPtr); - the vendor comment fossil
                    return OK_NB
                }

                // ----------------------------------------------------------------------

                if (g == XNEXT) { /* found next */

                    blkoff += cstrlenX(blk, blkoff) + ADSIZE + 6 /* next key */
                    ilong = leIntX(blk, blkoff - 4) /* look for less-than childrn */

                    if (ilong <= 0) {
                        phaseScan = false // goto fail
                        continue@inner
                    }

                    i = ilong
                    BtreeXState.tracep[BtreeStatic.trx] = blkoff - 4 /* new parent */
                    continue@mainloop // goto nxtblk
                }

                /*end found */

                bdB[0] = 0
                return 0 /* default failure */
            }

            // ------------------------------------------------------------
            // fail:
            // ------------------------------------------------------------

            if (g == PREVIOUS_BX) {
                // goto p000 re-entry: uses the stale ptr1 cursor
                // exactly as the vendor
                ilong = leIntX(blk, ptr1off - 4)

                if (ilong <= 0) {
                    blkoff = ptr1off
                    if (prevBlk != null) {
                        val pb2 = prevBlk
                                ?: throw IllegalArgumentException("null prevBlk seat")
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

            // g == XNEXT here (all other ops returned above)

            if (blk[blkoff] != MMARK.toByte()) { // not at end of block

                // modify key
                // copy global array ref to both key and bd (the
                // tandem copy loop)
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

                BtreeStatic.trx--

                if (BtreeStatic.trx < 0) {
                    bdB[0] = 0
                    return BAD_NB
                }

                i = BtreeXState.trace[BtreeStatic.trx]
                blkoff = BtreeXState.tracep[BtreeStatic.trx] + 4 /* key */
                freadx(i, UNMODIFIED)
                blk = aliasOfX(i).gblbf

                if (blk[blkoff] != MMARK.toByte()) break@upblock
                // goto upblock
            }

            // goto fail: re-tests the XNEXT fail arm on the
            // refetched parent block (phaseScan stays false)
            continue@inner
        }
    }
}
