package doc

// Unit 50: interp-native-btree.cpp::scanblocks (batch 2, core stratum)
// - the inline recursive block walker of the native btree search. The
// block domain is ByteArray (raw block bytes, unsigned as in the
// vendor); the vendor idiom Key = block + ptr1 becomes the integer
// offset of the key inside the block (H-007 buffer-role note, kept as
// an offset param). The static mark pair is the constant MMARK,0 pair
// (banner: do not recreate during recursion - a constant here, no
// state is carried); the static Key pointer has no cross-call use
// (re-assigned before every read), so a local is faithful. memcpys of
// 4-byte little-endian block ints and ADSIZE-byte data are local
// helpers; the second commented-out vendor memcpy line (strlen form)
// is kept as a comment exactly as in the vendor. freadx is an
// out-of-scope H-014 stub (btree block IO owner).

fun scanblocks(i: Int, svPtr: Msv) {
    val block = Stubs.freadx(i, UNMODIFIED)

    var ptr1 = 4

    while (true) {
        val Key = ptr1 // Key = block + ptr1, offset into the block

        if (block[Key].toInt() == MMARK && block[Key + 1].toInt() == MMARK) break // block end

        if (bstrcmp(block, Key, BigKey, 0) > 0) {
            bstrcpy(BigKey, block, Key)
            BigKeyBlock = i
            BigKeyOffset = ptr1
            BigKeyLower = readIntLE(block, Key - 4)
            // memcpy(&BigKeyData, index((char *) Key, 0) + 1, ADSIZE):
            // the data follows the terminating NUL of the key.
            BigKeyData = readLongLE(block, bstrlen(block, Key) + 1)
        }

        BigKeyLower = readIntLE(block, Key - 4)

        ptr1 += bstrlen(block, Key) + ADSIZE + 6 // key & data
    }

    //--------------------------------------------------
    //       trace high end descendents of mark if any
    //--------------------------------------------------

    val lower = readIntLE(block, ptr1 - 4)

    if (lower > 0) {
        BigKeyParent = i
        BigKeyParentOffset = ptr1 - 4
        scanblocks(lower, svPtr)
    }
}

// byte-domain strlen at offset (the vendor strlen on the key)
private fun bstrlen(a: ByteArray, off: Int): Int {
    var i = off
    while (i < a.size && a[i] != 0.toByte()) i++
    return i - off
}

// byte-domain strcmp of two NUL-terminated regions at offsets
private fun bstrcmp(a: ByteArray, offA: Int, b: ByteArray, offB: Int): Int {
    var i = 0
    while (true) {
        val ca = if (offA + i < a.size) a[offA + i].toInt() else 0
        val cb = if (offB + i < b.size) b[offB + i].toInt() else 0
        if (ca != cb) return if (ca < cb) -1 else 1
        if (ca == 0) return 0
        i++
    }
}

// byte-domain strcpy from a region of src at off into dst
private fun bstrcpy(dst: ByteArray, src: ByteArray, off: Int) {
    var i = 0
    while (off + i < src.size && src[off + i] != 0.toByte()) {
        dst[i] = src[off + i]
        i++
    }
    dst[i] = 0.toByte()
}

// little-endian 4-byte int read (the vendor memcpy(&lower, Key - 4, 4))
private fun readIntLE(a: ByteArray, off: Int): Int {
    var v = 0
    for (i in 3 downTo 0) v = (v shl 8) or (a[off + i].toInt() and 0xff)
    return v
}

// little-endian little-8-byte long read (the vendor memcpy ADSIZE bytes)
private fun readLongLE(a: ByteArray, off: Int): Long {
    var v = 0L
    for (i in ADSIZE - 1 downTo 0) v = (v shl 8) or (a[off + i].toLong() and 0xff)
    return v
}
