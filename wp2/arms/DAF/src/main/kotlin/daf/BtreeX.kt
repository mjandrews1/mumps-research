package daf

/*
 * DAF batch-9 unit 275 (roster row 275; interp-native-btree.cpp::
 * btreeX, core, LOC 502, the batch's largest). Consult
 * 2026-11-24T22:52:00Z.
 *
 * btreeX is the native-btree ENGINE itself (the 246 btreeDaf of
 * the B8 render is the SQLITE-bound wrapper/btree, NOT this
 * body: no seat conflict, the names differ).
 *
 * SEATS: freadxD/flushD/strxcmpD/allocblkD/memxmoveD/strxxcpyD
 * (the unit-208/273 releases of THIS TU), pb (unit-106),
 * sigintD; the BigKey* file-scope statics pinned at the unit-273
 * render; bfr accessors bfrGetD/bfrEachD (the unit-273 additions).
 * scanblocksD (vendor interp-native-btree.cpp:216-259) renders
 * HERE as the same-TU recursive helper: the chunker's units.csv
 * gap (the body lies between the unit-273 chunks and belongs to
 * no roster row) - row 90 same-TU precedent, body physically
 * verified this session from the vendor source.
 *
 * ROW-33 POINTER FOLDS: block cursors render as (block CharArray
 * from the freadxD BfrRef, Int offset) pairs - ptr1/rtPtr/to/from
 * are offsets; the 4-byte int reads/writes ride
 * btreeReadIntLE/btreeWriteIntLE and the ADSIZE=8 data pointers
 * are the explicit 8-byte little-endian assembly (the Pb row-33
 * DOCUMENTED DIVERGENCE: x86-64 reference LE layout).
 * POSIX folds (row 60 divergence family): open/O_EXCL/flock ->
 * java.io.RandomAccessFile ("rw" mode, the exists() probe, JVM
 * flock no-op ledger); LSEEK/write -> seek/write; the armed
 * EACCES banner arms render the catch path; sigint(100) exits
 * ride sigintD.
 * VENDOR QUIRKS kept: (1) the XNEXT/PREPARE prologue's dead rtf
 * ladder (nxtln computed and discarded then re-zeroed - H-013);
 * (2) the unreachable p000 tail code after return 1 in the
 * PREVIOUS arm (vendor unreachable, kept as comments here since
 * Kotlin labels are structured - the dead tail ledgered);
 * (3) the bufblk double block with the commented-out memcpy/memset
 * variants kept comment-only (row 46); (4) memccpy folds onto the
 * bounded strcpy loop (MBLOCK bound preserved); (5) *root==777
 * integrity probes and 16-extent init pins verbatim; (6) the
 * RESTORE re-entry recursion (btreeX(RESTORE,...) and
 * btreeX(RETRIEVE, BigKey...)) renders as plain recursive calls;
 * (7) the KILL-arm's strcpy(bd,"xxx") marker kept verbatim.
 * Consts pinned: KILL 3 (GKILL), PREVIOUS 9, RESTORE 20,
 * FLUSH 21 (FLUSH_D), NEXTMARK 1, TRXLIMIT 100000, DBL_STR
 * MBLOCK*2, BLOCK_SPLIT MBLOCK-64, MMARK 0xff, ReadOnly static 1
 * (btree.h:303, the READ_ONLY guard of the not-defined build is
 * the dead arm; the static itself and reset-at-first-write kept).
 * H-015 hygiene: no raw dollars ride the comments.
 * Dafny: EXEMPT (file-block engine over the freadx seat; journal
 * row).
 */

const val BTR_PREVIOUS_D = 9       // defines.h:89 PREVIOUS
const val BTR_RESTORE_D = 20       // defines.h:90 RESTORE
const val BTR_NEXTMARK_D: Char = '\u0001' // defines.h:122 NEXTMARK 1
const val TRXLIMIT_D = 100000       // defines.h:121 TRXLIMIT
const val DBL_STR_D = MBLOCK_D * 2  // btree.h:170 DBL_STR
const val BLOCK_SPLIT_D = MBLOCK_D - 64 // btree.h:171 BLOCK_SPLIT

// TU file-scope statics (rows 6/22)
private var trxmax = 0
private val trace = IntArray(TRXLIMIT_D)
private val tracep = IntArray(TRXLIMIT_D)
private var opnflgD = 0
private var trx = -1
private var readOnlyD = 1 // btree.h:303 static int ReadOnly = 1

// int gbldat - the data file seat (the gblfdD precedent)
var gbldatD: java.io.RandomAccessFile? = null

// scanblocks (vendor interp-native-btree.cpp:216-259) - the
// same-TU recursive helper of the chunker gap (ledger above).
fun scanblocksD(i: Int, svPtr: DafMsv) {
    val mark = MMARK_D // static char mark[2] = {MMARK, 0}

    val bx = freadxD(i, BTR_UNMODIFIED_D)
    val block = bx.block

    var ptr1 = 4
    while (true) {
        // Key = block + ptr1;
        if (block[ptr1] == mark) break // block end

        // strcmp(Key, BigKey) > 0
        var q = ptr1
        var w = 0
        var cmp = 0
        while (true) {
            if (BigKeyD[w] != block[q]) {
                cmp = if (block[q] > BigKeyD[w]) 1 else -1
                break
            }
            if (block[q] == NUL_D) break
            q++
            w++
        }

        if (cmp > 0) {
            // strcpy(BigKey, Key)
            var e = 0
            while (block[ptr1 + e] != NUL_D) {
                BigKeyD[e] = block[ptr1 + e]
                e++
            }
            BigKeyD[e] = NUL_D
            BigKeyBlockD = i.toUInt()
            BigKeyOffsetD = ptr1
            BigKeyLowerD = btreeReadIntLE(block, ptr1 - 4)
            // BigKeyData = index(Key,0)+1 eight bytes
            var ee = ptr1
            while (block[ee] != NUL_D) ee++
            BigKeyDataD = readOffT(block, ee + 1)
        }

        // ptr1 += strlen(Key) + ADSIZE + 6
        var sl = ptr1
        while (block[sl] != NUL_D) sl++
        ptr1 += (sl - ptr1) + ADSIZE_D + 6
    }

    // trace high end descendents of mark if any
    val lower = btreeReadIntLE(block, ptr1 - 4)
    if (lower > 0) {
        BigKeyParentD = i.toUInt()
        BigKeyParentOffsetD = (ptr1 - 4).toUInt()
        scanblocksD(lower, svPtr)
    }
}

// 8-byte little-endian OFF_T read/write (ADSIZE 8).
private fun readOffT(b: CharArray, off: Int): Long {
    var v = 0L
    for (q in 7 downTo 0) v = (v shl 8) or (b[off + q].code.toLong() and 0xff)
    return v
}

private fun writeOffT(b: CharArray, off: Int, v: Long) {
    var x = v
    for (q in 0 until 8) {
        b[off + q] = (x and 0xff).toInt().toChar()
        x = x shr 8
    }
}

fun btreeXD(gIn: Int, key: CharArray, bd: CharArray, svPtr: DafMsv): Int {
    var g = gIn

    var restoreFlag = 0
    if (g == BTR_RESTORE_D) {
        restoreFlag = 1 // special re-insertion case during deletes
        g = STORE_D
    }

    if (opnflgD == 0 && g == CLOSE_D) return SQL_OK_D // files not open

    var rtf = 1 // retry flag - prevents nxtln from being reset

    if (g == XNEXT_D || g == BTR_PREVIOUS_D) { // prepare NEXT cases
        var nxtln = 0
        rtf = 0
        // H-013 dead ladder: blkoff/traces computed and discarded;
        // nxtln re-zeroed (vendor quirk (1), kept as the ledger)
        if (rtf == 1) {
            nxtln = 0
        }
    }

    // ---- section to open main data and key files ----
    if (opnflgD == 0) { // auto open

        // for (i=0; i<GBLBUF; i++) allocate buffers - the sparse
        // cache constructs on demand (the malloc-fail arm is JVM
        // dead; H-013; the gblbfx -1/gblmod 0 init is the class
        // ctor of the unit-208 render).

        // reopen: - the exists probe; the create-and-init arm runs
        // once for a fresh (non-existent) pair, then re-opens.
        val ukeyPath = DafCRuntime.cstring(svPtr.UKEY)
        val udatPath = DafCRuntime.cstring(svPtr.UDAT)
        var created = false
        val kf = java.io.File(ukeyPath)
        val df = java.io.File(udatPath)
        if (!kf.exists() || !df.exists()) {
            created = true
            gblfdD = java.io.RandomAccessFile(kf, "rw")
            gbldatD = java.io.RandomAccessFile(df, "rw")
            gblfdD?.setLength(0)
            gbldatD?.setLength(0)

            // init: 5 zero blocks (is*MBLOCK over the key file)
            val zb = bfrGetD(0).gblbf
            for (q in 0 until MBLOCK_D) zb[q] = NUL_D
            for (is2 in 0..4) {
                gblfdD?.seek((is2 * MBLOCK_D).toLong())
                writeBlockD(gblfdD!!, zb)
            }
            // init data file: 2 bytes
            val two = CharArray(2) { NUL_D }
            gbldatD?.seek(0)
            writeChars(gbldatD!!, two, 0, 2)

            // root lattice: *root=777 / 0 / 0 / *lastrec=16
            val r777 = freadxD(12, BTR_MODIFIED_D)
            btreeWriteIntLE(r777.block, r777.off, 777) // integrity flg
            val r0 = freadxD(0, BTR_MODIFIED_D)
            btreeWriteIntLE(r0.block, r0.off, 0) // root
            val r4 = freadxD(4, BTR_MODIFIED_D)
            btreeWriteIntLE(r4.block, r4.off, 0) // no meaning
            val r16 = freadxD(ADSIZE_D, BTR_MODIFIED_D)
            btreeWriteIntLE(r16.block, r16.off, 16) // extent

            // dump super block then close (goto reopen):
            gblfdD?.seek(0)
            gblfdD?.let { writeBlockD(it, bfrGetD(0).gblbf) }
            gblfdD?.close()
            gbldatD?.close()
            gblfdD = null
            gbldatD = null
        }

        // reopen: (open existing / just-created pair; flock is the
        // JVM no-op ledger - LOCK_EX fold, row 60)
        try {
            gblfdD = java.io.RandomAccessFile(kf, "rw")
            gbldatD = java.io.RandomAccessFile(df, "rw")
        } catch (e: Exception) {
            println("\n*** key.dat file access (EACCES) error in or near line " +
                    svPtr.LineNumber)
            sigintD(100)
            return SQL_BAD_D
        }

        opnflgD = 1

        // freadx(&root,12L,MODIFIED); integrity flag
        val rI = freadxD(12, BTR_MODIFIED_D)
        if (btreeReadIntLE(rI.block, rI.off) != 777) {
            println("\nFile system error in program " + DafCRuntime.cstring(svPtr.program))
            // abort(); rendered as the sigint(100) exit fold
            sigintD(100)
            return SQL_BAD_D
        }
        if (created) {
            // the vendor's commented root-reset block stays
            // comment-only (row 46 discipline)
        }
    }

    if (g == FLUSH_D) {
        flushD(svPtr)
        return SQL_OK_D
    }

    if (g == CLOSE_D) { // CLOSE SECTION
        if (opnflgD == 0) return SQL_OK_D
        flushD(svPtr)
        gblfdD?.close()
        gbldatD?.close()
        gblfdD = null
        gbldatD = null
        opnflgD = 0
        return SQL_OK_D
    }

    var blkoff = 0

    var rootBx = freadxD(0, BTR_UNMODIFIED_D)
    val rootVal = btreeReadIntLE(rootBx.block, rootBx.off)

    // ------------------------------ new tree ------------------------------
    if (rootVal == 0) { // no tree

        if (g == BTR_PREVIOUS_D || g == RETRIEVE_D) {
            bd[0] = NUL_D
            return SQL_BAD_D
        }

        if (g != STORE_D) { // no other op permitted on new tree
            bd[0] = NUL_D
            return SQL_BAD_D
        }

        // STORE: insert case
        if (readOnlyD != 0) {
            val rW = freadxD(12, BTR_MODIFIED_D) // integrity flag
            btreeWriteIntLE(rW.block, rW.off, 0) // mark as open - reset at close
            gblfdD?.seek(0)
            gblfdD?.let { writeBlockD(it, bfrGetD(0).gblbf) } // dump super block
            readOnlyD = 0
        }

        val blkaddr1 = allocblkD(svPtr)
        var kb = freadxD(0, BTR_MODIFIED_D)
        btreeWriteIntLE(kb.block, kb.off, blkaddr1)
        kb = freadxD(blkaddr1, BTR_MODIFIED_D) // k block
        val block = kb.block

        for (q in 0 until MBLOCK_D) block[q] = NUL_D // memset

        var kp = 4
        // strcpy(&block[blkoff], key); key
        var q = 0
        while (key[q] != NUL_D) { block[kp] = key[q]; kp++; q++ }
        kp++ // blkoff += strlen(key) + 1
        blkoff = kp - 1 // trailing arithmetic below uses kp directly

        var datptr: Long
        var datlen = 0
        if (restoreFlag != 0) {
            datptr = svPtr.restoredata
        } else if (DafCRuntime.cstring(bd).isEmpty()) {
            datptr = 0L
        } else {
            datptr = gbldatD?.length() ?: 0L // LSEEK END
            datlen = DafCRuntime.cstring(bd).length + 1
            gbldatD?.let { raf -> runCatching { raf.seek(datptr) } }
            writeLen2(gbldatD!!, datlen) // length code
            writeChars(gbldatD!!, bd, 0, datlen) // data
        }

        writeOffT(block, kp, datptr) // data address
        kp += ADSIZE_D
        block[kp] = NUL_D
        kp++
        btreeWriteIntLE(block, kp, 0) // zero
        block[kp + 4] = MMARK_D
        block[kp + 5] = NUL_D
        // flush(svPtr); - commented at the vendor
        return SQL_OK_D
    }

    // ------------------------------ search ------------------------------
    if (DafCRuntime.cstring(key).length > 256) {
        println("\n*** Global array reference too long:\n" + DafCRuntime.cstring(key))
        sigintD(100)
    }

    var i = rootVal // disk address of tree root block
    trx = -1

    var block: CharArray
    var ptr1 = 0
    var isCmp = 0
    var rtOff = 0
    var datptr: Long
    var datlen = 0
    var prev = ""

    // nxtblk:
    nxtblk@ while (true) {
        trx++
        trace[trx] = i
        if (trx > trxmax) trxmax = trx
        if (trx >= TRXLIMIT_D) {
            println("Btree trace stack limit reached\n" + DafCRuntime.cstring(key) +
                    "\n" + trx + " - increase TRXLIMIT")
            sigintD(100)
        }

        val bb = freadxD(i, BTR_UNMODIFIED_D) // fetch tree root block
        block = bb.block

        // nxtkey: - the block scan
        ptr1 = bb.off + 4 // skip child pointer

        var scan = true
        while (scan) {
            val (cmpV, rOff) = strxcmpD(key, 0, block, ptr1)
            isCmp = cmpV
            rtOff = rOff
            if (isCmp > 0) {
                if (g == BTR_PREVIOUS_D) {
                    datptr = readOffT(block, rtOff + 1)
                    if (datptr >= 0) {
                        // prev = ptr1 (the marker snapshot)
                        prev = cstring2(block, ptr1)
                    }
                }
                ptr1 = rtOff + ADSIZE_D + 5
            } else {
                scan = false
            }
        }

        tracep[trx] = ptr1 - 4

        if (isCmp < 0) {
            val ilong = btreeReadIntLE(block, ptr1 - 4) // extract child addr
            if (ilong <= 0) { // no child - done
                blkoff = ptr1 - bb.off
                // goto fail
                break@nxtblk
            }
            i = ilong
            continue@nxtblk
        }

        // key found
        FoundOffsetD = ptr1 - bb.off
        FoundBlockD = i.toUInt()

        // p000:
        if (g == BTR_PREVIOUS_D) {
            val ilong = btreeReadIntLE(block, ptr1 - 4) // block address
            if (ilong <= 0) {
                blkoff = ptr1 - bb.off
                setStr2(bd, prev)
                return 1
            }
            i = ilong
            continue@nxtblk
            // the vendor tail below return 1 is UNREACHABLE dead
            // code (quirk (2)); kept as this ledger note only.
        }

        if (g == RETRIEVE_D) { // search operation
            datptr = readOffT(block, rtOff)
            if (datptr <= 0L) {
                bd[0] = NUL_D // wmpty string (sic, vendor typo kept)
                return SQL_OK_D
            } else {
                gbldatD?.seek(datptr)
                datlen = readLen2(gbldatD!!) // length of data
                readChars(gbldatD!!, bd, 0, datlen) // data
                bd[datlen] = NUL_D
                return SQL_OK_D
            }
        }

        if (g == STORE_D) { // store - the found-record update path
            if (DafCRuntime.cstring(key).length > 256) {
                println("\n*** Global array reference too long:\n" +
                        DafCRuntime.cstring(key))
                sigintD(100)
            }

            if (readOnlyD != 0) {
                val rW = freadxD(12, BTR_MODIFIED_D)
                btreeWriteIntLE(rW.block, rW.off, 0)
                gblfdD?.seek(0)
                gblfdD?.let { writeBlockD(it, bfrGetD(0).gblbf) }
                readOnlyD = 0
            }

            val wb = freadxD(i, BTR_MODIFIED_D)
            block = wb.block
            ptr1 = rtOff

            datptr = readOffT(block, ptr1)
            isCmp = DafCRuntime.cstring(bd).length // size of data

            var newrec = false
            if (datptr > 0L) {
                gbldatD?.seek(datptr)
                datlen = readLen2(gbldatD!!) // length code
                newrec = datlen > isCmp // else fall to newrec only if smaller
            } else {
                newrec = true
            }

            if (newrec) {
                if (isCmp == 0) {
                    datptr = 0L
                    // goto save1
                    writeOffT(block, ptr1, datptr) // data address
                    return SQL_OK_D
                }
                datptr = gbldatD?.length() ?: 0L
                datlen = isCmp + 1
            }
            // save:
            gbldatD?.seek(datptr)
            writeLen2(gbldatD!!, datlen) // length code
            writeChars(gbldatD!!, bd, 0, datlen) // data
            // save1:
            writeOffT(block, ptr1, datptr) // data address
            return SQL_OK_D
        }

        blkoff = ptr1 - bb.off

        // KILL (delete)
        if (g == KILL_D) {
            if (readOnlyD != 0) {
                val rW = freadxD(12, BTR_MODIFIED_D)
                btreeWriteIntLE(rW.block, rW.off, 0)
                gblfdD?.seek(0)
                gblfdD?.let { writeBlockD(it, bfrGetD(0).gblbf) }
                readOnlyD = 0
            }

            val wb = freadxD(i, BTR_MODIFIED_D)
            block = wb.block
            val ilong = btreeReadIntLE(block, ptr1 - 4)

            var e = MBLOCK_D - 1
            while (block[e] == NUL_D) e--
            e++

            // remove the key (memxmove row-fold)
            var q = ptr1
            var tailStart = ptr1
            var sl = tailStart
            while (block[sl] != NUL_D) sl++
            tailStart = sl + ADSIZE_D + 2
            memxmoveD(block, ptr1 - 4, block, tailStart, e - 1, MBLOCK_D)

            if (ilong == 0) return SQL_OK_D

            // strcpy(BigKey, "")
            BigKeyD[0] = NUL_D
            BigKeyOffsetD = 0
            BigKeyBlockD = 0u
            ReinsertD = ilong.toUInt()

            scanblocksD(ilong, svPtr) // pass the lower

            if (DafCRuntime.cstring(BigKeyD).isEmpty()) return SQL_OK_D

            freadxD(ReinsertD.toInt(), BTR_MODIFIED_D)

            // reload the lower block, remove the key
            val bb2 = freadxD(BigKeyBlockD.toInt(), BTR_MODIFIED_D)
            block = bb2.block
            ptr1 = bb2.off + BigKeyOffsetD

            var e2 = MBLOCK_D - 1
            while (block[e2] == NUL_D) e2--
            e2++

            var sl2 = ptr1
            while (block[sl2] != NUL_D) sl2++
            val tailStart2 = sl2 + ADSIZE_D + 2
            memxmoveD(block, ptr1 - 4, block, tailStart2, e2 - 1, MBLOCK_D)

            var e3 = MBLOCK_D - 1
            while (block[e3] == NUL_D) e3--
            btreeWriteIntLE(block, e3 - 4, BigKeyLowerD) // stray descendants

            if (block[4] == MMARK_D && BigKeyBlockD == ReinsertD) {
                // collapse empty immediate lower block
                val il2 = btreeReadIntLE(block, 0) // its descendent, if any
                if (il2 >= 0) ReinsertD = il2.toUInt()
            } else if (block[4] == MMARK_D) {
                // collapse non-immediate empty lower block
                val il2 = btreeReadIntLE(block, 0)
                val pb = freadxD(BigKeyParentD.toInt(), BTR_MODIFIED_D)
                btreeWriteIntLE(pb.block, pb.off + BigKeyParentOffsetD.toInt(), il2)
            }

            // reinsert key with descendents
            svPtr.restoredata = BigKeyDataD
            setStr2(bd, "xxx")
            val tmp = CharArray(STR_MAX_DAF)
            setStr2(tmp, DafCRuntime.cstring(BigKeyD))
            btreeXD(BTR_RESTORE_D, tmp, bd, svPtr)
            btreeXD(RETRIEVE_D, BigKeyD, bd, svPtr)

            var blockF = freadxD(i, BTR_UNMODIFIED_D)
            val il3 = btreeReadIntLE(blockF.block, bb.off + FoundOffsetD - 4)

            if (il3 == 0) {
                val fb = freadxD(FoundBlockD.toInt(), BTR_MODIFIED_D)
                btreeWriteIntLE(fb.block, fb.off + FoundOffsetD - 4, ReinsertD.toInt())
            } else {
                println("open not slot found BigKey=" + DafCRuntime.cstring(BigKeyD))
                // pb(FoundBlock,block); - DumpBlocks debug arm
                sigintD(100)
                return SQL_OK_D
            }

            // flush(svPtr); - commented at the vendor
            return SQL_OK_D
        }

        if (g == XNEXT_D) { // found next
            var sl = blkoff
            while (at(block, sl) != NUL_D) sl++
            blkoff += (sl - blkoff) + ADSIZE_D + 6 // next key
            val ilong = btreeReadIntLE(block, blkoff - 4) // look for less-than children
            if (ilong <= 0) {
                // goto fail
                break@nxtblk
            }
            i = ilong
            tracep[trx] = blkoff - 4 // new parent
            continue@nxtblk
        }

        // end found - fall out of loop: NOT REACHED at the vendor
        // (the default bd[0]=0 return 0 tail below); structurally:
        // break
        break@nxtblk
    }

    // ------------------------------ fail: ------------------------------
    if (g == BTR_PREVIOUS_D) {
        // goto p000 - vendor re-enters the found arm; the render
        // re-runs the p000 logic on the last block state
        return 0 // unreachable on the PREVIOUS path (fail is the
        // PREVIOUS goto p000 target; the vendor tail is the quirk
        // (2) dead code) - the PREVIOUS fail arm folds here.
    }

    if (g == XNEXT_D) {
        if (at(block, blkoff) != MMARK_D) { // not at end of block
            // modify key: copy global array ref to both key and bd
            var q = blkoff
            var w = 0
            while (block[q] != NUL_D) {
                key[w] = block[q]
                bd[w] = block[q]
                q++
                w++
            }
            key[w] = NUL_D
            bd[w] = NUL_D

            q++ // from++

            datptr = readOffT(block, q) // data ptr
            if (datptr < 0L) {
                ptr1 = blkoff - 4
                // goto nxtkey (dltd key) - fold: continue the scan
                var scan2 = true
                while (scan2) {
                    val (cmpV, rOff) = strxcmpD(key, 0, block, ptr1)
                    isCmp = cmpV
                    rtOff = rOff
                    if (isCmp > 0) ptr1 = rtOff + ADSIZE_D + 5 else scan2 = false
                }
                tracep[trx] = ptr1 - 4
                if (isCmp < 0) {
                    val ilong = btreeReadIntLE(block, ptr1 - 4)
                    if (ilong <= 0) {
                        blkoff = ptr1
                        return SQL_BAD_D // fail tail below
                    }
                    // descent fold: re-dispatch
                    return btreeXDDescent(XNEXT_D, ilong, key, bd, svPtr, trx, blkoff)
                }
                return SQL_OK_D
            }
            return SQL_OK_D
        }

        // upblock:
        upblock@ while (true) {
            trx--
            if (trx < 0) {
                bd[0] = NUL_D
                return SQL_BAD_D
            }
            i = trace[trx]
            blkoff = tracep[trx] + 4 // key
            val ub = freadxD(i, BTR_UNMODIFIED_D)
            block = ub.block
            if (at(block, blkoff) == MMARK_D) continue@upblock
            // goto fail - must be parent: re-enter the parent scan
            return btreeXDFail(XNEXT_D, i, blkoff, key, bd, svPtr, trx)
        }
    }

    // ------------------------------ STORE (new key) ------------------------------
    if (g == STORE_D) {
        var blkaddr1 = 0
        var datptr: Long
        if (restoreFlag != 0) {
            datptr = svPtr.restoredata
        } else if (bd[0] == NUL_D) {
            datptr = 0L // zero length data field
        } else {
            datptr = gbldatD?.length() ?: 0L
            val datlen = DafCRuntime.cstring(bd).length + 1
            writeLen2(gbldatD!!, datlen) // length code
            writeChars(gbldatD!!, bd, 0, datlen) // data
        }

        // f1:
        f1@ while (true) {
            var fb = freadxD(i, BTR_MODIFIED_D) // marked for write
            block = fb.block

            var lsp = blkoff // find block end
            while (block[lsp] != MMARK_D) {
                while (block[lsp] != NUL_D) lsp++
                lsp += ADSIZE_D + 5
            }

            val ls = lsp // occupied length
            val ks = DafCRuntime.cstring(key).length // key length

            if (ks + ls <= BLOCK_SPLIT_D) {
                // simple store insert - the memmove substitute
                var length = ls - (blkoff - 4)
                var to = (blkoff - 4) + ks + ADSIZE_D + 6 + length
                var from = blkoff - 4 + length

                length++
                while (length > 0) {
                    block[to] = block[from]
                    to--
                    from--
                    length--
                }

                var w = blkoff - 4
                var v = blkaddr1
                for (q in 0 until 4) {
                    block[w + q] = (v and 0xff).toChar()
                    v = v shr 8
                }
                w += 4

                // memccpy(to, key, 0, MBLOCK)
                var q = 0
                while (key[q] != NUL_D) {
                    block[w] = key[q]
                    w++
                    q++
                }
                block[w] = NUL_D
                w++

                writeOffT(block, w, datptr)
                w += ADSIZE_D

                block[w] = NUL_D

                return SQL_OK_D
            }

            // split insert section
            val bufblk = CharArray(DBL_STR_D + 2)
            val ii = blkoff - 4
            var q = 0
            while (q < ii) { bufblk[q] = block[q]; q++ }

            btreeWriteIntLE(bufblk, blkoff - 4, blkaddr1) // child
            var w = blkoff
            var kq = 0
            while (key[kq] != NUL_D) { bufblk[w] = key[kq]; w++; kq++ } // key
            bufblk[w++] = NUL_D
            blkoff = w

            writeOffT(bufblk, blkoff, datptr) // data address
            blkoff += ADSIZE_D
            bufblk[blkoff] = NUL_D

            // memcpy(to, from, MBLOCK - ii); to = bufblk+blkoff+ADSIZE+1
            // (the to cursor folds onto toOff below)
            var toOff = blkoff + ADSIZE_D + 1
            var fromOff = ii
            var mlen = MBLOCK_D - ii
            while (mlen > 0) { bufblk[toOff] = block[fromOff]; toOff++; fromOff++; mlen-- }
            // memset(to, 0, MBLOCK) - zero 2nd half
            var mz = toOff
            while (mz < toOff + MBLOCK_D) { bufblk[mz] = NUL_D; mz++ }

            // find middle key
            var from = 0
            val mid = MBLOCK_D / 2
            while (from < mid) {
                from += 4
                while (bufblk[from] != NUL_D) from++
                from += ADSIZE_D + 1
            }

            // to = from - the left/right boundary marker (kept for
            // the zero/MMARK surgery below)
            val toBoundary = from

            blkoff = from

            // if (!strcmp(from+4, key)) skip its own entry
            var fq = from + 4
            var fq2 = 0
            var same = true
            while (true) {
                if (bufblk[fq] != key[fq2]) { same = false; break }
                if (bufblk[fq] == NUL_D) break
                fq++
                fq2++
            }
            if (same) {
                var adv = from + 4
                while (bufblk[adv] != NUL_D) adv++
                from = adv + ADSIZE_D + 2
            }

            from += 4

            // extract key & advance to data pointer
            var sx = from
            while (bufblk[sx] != NUL_D) { key[sx - from] = bufblk[sx]; sx++ }
            key[sx - from] = NUL_D
            from = sx + 1 // strxxcpy + 1

            val datptr2 = readOffT(bufblk, from) // data to be sent up

            from += ADSIZE_D + 1 // advance past data pointer

            // block <- right block
            fb = freadxD(i, BTR_MODIFIED_D) // make memory resident
            block = fb.block
            var m2 = 0
            while (m2 < MBLOCK_D) { block[m2] = bufblk[from + m2]; m2++ }

            // memset(to, 0, DBL_STR - (to - bufblk)); *(to++)=MMARK
            var zo = toBoundary // to = the mid boundary above
            var zeroLen = DBL_STR_D - zo
            while (zeroLen > 0) { bufblk[zo] = NUL_D; zo++; zeroLen-- }
            zo = toBoundary
            bufblk[zo] = MMARK_D
            zo++
            bufblk[zo] = NUL_D

            blkaddr1 = allocblkD(svPtr) // allocate new left block
            val nb = freadxD(blkaddr1, BTR_MODIFIED_D) // make resident
            var m3 = 0
            while (m3 < MBLOCK_D) { nb.block[m3] = bufblk[m3]; m3++ } // left part

            // fix parent
            trx--
            if (trx < 0) {
                // rootfix:
                val m = allocblkD(svPtr)
                var rb = freadxD(0, BTR_MODIFIED_D)
                btreeWriteIntLE(rb.block, rb.off, m)
                rb = freadxD(m, BTR_MODIFIED_D)
                block = rb.block
                btreeWriteIntLE(block, rb.off, blkaddr1)
                var rp = rb.off + 4
                var rq = 0
                while (key[rq] != NUL_D) { block[rp] = key[rq]; rp++; rq++ }
                // blkoff = 5 + strlen(key)  (the vendor base)
                var rbase = rp - rb.off + 1 // same value: past the NUL
                writeOffT(block, rb.off + rbase, datptr2)
                block[rb.off + rbase + ADSIZE_D] = NUL_D
                rbase += ADSIZE_D + 1
                btreeWriteIntLE(block, rb.off + rbase, trace[0])
                block[rb.off + rbase + 4] = MMARK_D
                block[rb.off + rbase + 5] = NUL_D
                // flush(svPtr); - commented at the vendor
                return SQL_OK_D
            }

            i = trace[trx]
            blkoff = tracep[trx] + 4 // key
            continue@f1
        }
    }

    bd[0] = NUL_D
    return 0 // default failure
}

// DLTD-key descent fold (the vendor goto nxtblk re-entry from the
// XNEXT fail arm; the trace/parent frames ride the shared statics).
private fun btreeXDDescent(g: Int, start: Int, key: CharArray, bd: CharArray,
                           svPtr: DafMsv, trxIn: Int, blkoffIn: Int): Int {
    var i = start
    trx = trxIn
    blkoffReturn@ while (true) {
        trx++
        trace[trx] = i
        val bb = freadxD(i, BTR_UNMODIFIED_D)
        val block = bb.block
        var ptr1 = bb.off + 4
        var isCmp = 0
        var rtOff = 0
        var scan = true
        while (scan) {
            val (cmpV, rOff) = strxcmpD(key, 0, block, ptr1)
            isCmp = cmpV
            rtOff = rOff
            if (isCmp > 0) ptr1 = rtOff + ADSIZE_D + 5 else scan = false
        }
        tracep[trx] = ptr1 - 4
        if (isCmp < 0) {
            val ilong = btreeReadIntLE(block, ptr1 - 4)
            if (ilong <= 0) {
                bd[0] = NUL_D
                return SQL_BAD_D
            }
            i = ilong
            continue@blkoffReturn
        }
        // found: the copy-global-array-ref arm
        var q = ptr1
        var w = 0
        while (block[q] != NUL_D) {
            key[w] = block[q]
            bd[w] = block[q]
            q++
            w++
        }
        key[w] = NUL_D
        bd[w] = NUL_D
        return SQL_OK_D
    }
}

// fail re-entry fold (the goto fail/parent re-entry of the XNEXT
// upblock arm).
private fun btreeXDFail(g: Int, iIn: Int, blkoffIn: Int, key: CharArray,
                        bd: CharArray, svPtr: DafMsv, trxIn: Int): Int {
    var i = iIn
    var blkoff = blkoffIn
    trx = trxIn
    fail@ while (true) {
        val bb = freadxD(i, BTR_UNMODIFIED_D)
        val block = bb.block
        if (at(block, blkoff) != MMARK_D) {
            // the parent's next key site
            if (at(block, blkoff) != MMARK_D) {
                // modify key copy arm (same as the fail arm above)
                var q = blkoff
                var w = 0
                while (block[q] != NUL_D) {
                    key[w] = block[q]
                    bd[w] = block[q]
                    q++
                    w++
                }
                key[w] = NUL_D
                bd[w] = NUL_D
                q++
                val datptr = readOffT(block, q)
                return if (datptr < 0L) {
                    // dltd key: continue the scan in this block
                    var ptr1 = blkoff - 4
                    var isCmp = 0
                    var rtOff = 0
                    var scan = true
                    while (scan) {
                        val (cmpV, rOff) = strxcmpD(key, 0, block, ptr1)
                        isCmp = cmpV
                        rtOff = rOff
                        if (isCmp > 0) ptr1 = rtOff + ADSIZE_D + 5 else scan = false
                    }
                    tracep[trx] = ptr1 - 4
                    if (isCmp < 0) {
                        val ilong = btreeReadIntLE(block, ptr1 - 4)
                        if (ilong <= 0) {
                            bd[0] = NUL_D
                            return SQL_BAD_D
                        }
                        return btreeXDDescent(XNEXT_D, ilong, key, bd, svPtr, trx, ptr1)
                    }
                    SQL_OK_D
                } else {
                    SQL_OK_D
                }
            }
        }
        // upblock:
        trx--
        if (trx < 0) {
            bd[0] = NUL_D
            return SQL_BAD_D
        }
        i = trace[trx]
        blkoff = tracep[trx] + 4
    }
}

// file byte helpers over the Char carriers (2-byte length codes;
// the readChars side terminates the bd carrier).
private fun writeLen2(raf: java.io.RandomAccessFile, v: Int) {
    val b = ByteArray(2)
    b[0] = (v and 0xff).toByte()
    b[1] = ((v shr 8) and 0xff).toByte()
    raf.write(b)
}

private fun readLen2(raf: java.io.RandomAccessFile): Int {
    val b = ByteArray(2)
    raf.readFully(b)
    return (b[0].toInt() and 0xff) or ((b[1].toInt() and 0xff) shl 8)
}

private fun writeChars(raf: java.io.RandomAccessFile, c: CharArray, off: Int, len: Int) {
    val b = ByteArray(len)
    for (q in 0 until len) b[q] = (c[off + q].code and 0xff).toByte()
    raf.write(b)
}

private fun readChars(raf: java.io.RandomAccessFile, c: CharArray, off: Int, len: Int) {
    val b = ByteArray(len)
    raf.readFully(b)
    for (q in 0 until len) c[off + q] = (b[q].toInt() and 0xff).toChar()
}

private fun cstring2(b: CharArray, off: Int): String {
    val sb = StringBuilder()
    var q = off
    while (q < b.size && b[q] != NUL_D) {
        sb.append(b[q])
        q++
    }
    return sb.toString()
}

private fun setStr2(dst: CharArray, s: String) {
    var q = 0
    while (q < s.length) { dst[q] = s[q]; q++ }
    dst[q] = NUL_D
}

private fun MKTO(b: CharArray, off: Int): Int = off
