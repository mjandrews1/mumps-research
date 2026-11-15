package mumps.interp

/**
 * Kotlin translation of interp-native-btree.cpp::scanblocks (Study
 * Subset unit 50, Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: inline void scanblocks(int i, struct MSV * svPtr)
 * (interp-native-btree.cpp:216-261) — trace descendant b-tree blocks
 * for the highest key value: read block i, walk its key/data records
 * (stride strlen+ADSIZE+6), track the max key (and its block/offset/
 * lower/data) into the BigKey* globals, then recurse into the last
 * lower-block pointer.
 *
 * BYTE MODEL (Hash precedent, row 88): block buffers are ByteArray;
 * keys are read as NUL-terminated byte strings with each byte mapped
 * to Char(0..255) — this preserves C strcmp's UNSIGNED byte comparison
 * exactly (Kotlin String compareTo would be wrong for bytes >= 0x80).
 * Multi-byte ints are little-endian, as on the reference x86 build.
 *
 * NEW ENTITIES (interp-native-btree.cpp:51-101): the BigKey* globals
 * (92-101), MMARK=0xff (defines.h:120), ADSIZE=8 (btree.h:51),
 * UNMODIFIED=0 (interp-native-btree.cpp:56), OFF_T=off_t -> Long.
 * freadx(buf,r,flag) — the block reader (lines 106-190) — is outside
 * batch scope (btree.h::<misc> is unit 55): TODO stub returning the
 * block ByteArray. unsigned int globals -> Int (block numbers and
 * offsets fit; widening documented).
 *
 * DEAD CODE PRESERVED AS COMMENT: the C's second memcpy(&lower, Key-4,
 * 4) after the max-update branch recomputes a `lower` that is never
 * read before being recomputed after the loop — dead store, dropped
 * with a comment. static Key/mark exist only to avoid per-call
 * recreation (C comment) — plain locals in Kotlin.
 */
const val MMARK = 0xff       // C: defines.h:120 #define MMARK 0xff
const val ADSIZE = 8         // C: btree.h:51 #define ADSIZE 8
const val UNMODIFIED = 0     // C: interp-native-btree.cpp:56 #define UNMODIFIED 0

/** C: unsigned char BigKey[STR_MAX] (line 92) — running max key buffer. */
var BigKey = ByteArray(STR_MAX)

/** C: unsigned int BigKeyBlock / BigKeyParent / BigKeyParentOffset;
 *  int BigKeyOffset / BigKeyLower; OFF_T BigKeyData (lines 93-101). */
var BigKeyBlock: Int = 0
var BigKeyParent: Int = 0
var BigKeyParentOffset: Int = 0
var BigKeyOffset: Int = 0
var BigKeyLower: Int = 0
var BigKeyData: Long = 0    // C: OFF_T (off_t)

/** C: inline void freadx(unsigned char **buf, int r, int flag)
 *  (interp-native-btree.cpp:106) — block reader (btree buffer pool);
 *  body outside batch scope (btree.h machinery, unit 55). */
fun freadx(r: Int, flag: Int): ByteArray =
    TODO("freadx not yet translated (lives in interp-native-btree.cpp:106, outside batch scope)")

/** NUL-terminated byte string at off, each byte mapped to Char(0..255):
 *  preserves C strcmp's unsigned byte order for the >0x80 range. */
private fun blockCString(block: ByteArray, off: Int): String {
    var end = off
    while (end < block.size && block[end].toInt() != 0) end++
    val sb = StringBuilder(end - off)
    for (i in off until end) sb.append(Char(block[i].toInt() and 0xFF))
    return sb.toString()
}

/** strcpy into a fixed byte buffer (NUL-terminated, rest left as-is). */
private fun putCString(buf: ByteArray, s: String) {
    for (i in s.indices) buf[i] = s[i].code.toByte()
    buf[s.length] = 0
}

/** strcmp over the unsigned-byte mapping (both sides Char 0..255). */
private fun compareKey(a: String, b: String): Int = a.compareTo(b)

private fun readInt32LE(block: ByteArray, off: Int): Int =
    (block[off].toInt() and 0xFF) or ((block[off + 1].toInt() and 0xFF) shl 8) or
        ((block[off + 2].toInt() and 0xFF) shl 16) or ((block[off + 3].toInt() and 0xFF) shl 24)

private fun readLongLE(block: ByteArray, off: Int): Long =
    readInt32LE(block, off).toLong() and 0xFFFFFFFFL or (readInt32LE(block, off + 4).toLong() shl 32)

/** C: inline void scanblocks(int i, struct MSV * svPtr) */
fun scanblocks(i: Int, svPtr: MsvState) {
    val block = freadx(i, UNMODIFIED) // C: freadx(&block, i, UNMODIFIED);

    var ptr1 = 4 // C: int ptr1 = 4;
    var lower: Int

    while (true) { // C: while (1) {
        // C: Key = block + ptr1; if (mark[0]==Key[0] && mark[1]==Key[1]) break;
        if ((block[ptr1].toInt() and 0xFF) == MMARK && (block[ptr1 + 1].toInt() and 0xFF) == 0) break // block end

        val key = blockCString(block, ptr1) // C: Key = block + ptr1

        // C: if (strcmp((const char *)Key,(const char *)BigKey) > 0) {
        if (compareKey(key, blockCString(BigKey, 0)) > 0) {
            putCString(BigKey, key) // C: strcpy((char *)BigKey,(const char *)Key);
            BigKeyBlock = i
            BigKeyOffset = ptr1
            lower = readInt32LE(block, ptr1 - 4) // C: memcpy(&lower, Key-4, 4);
            BigKeyLower = lower
            // C: memcpy(&BigKeyData, index((char *)Key, 0)+1, ADSIZE);
            BigKeyData = readLongLE(block, ptr1 + key.length + 1)
        }

        // C: memcpy(&lower, Key-4, 4); — DEAD STORE: lower is recomputed
        // after the loop and never read in between; dropped.

        // C: ptr1 += strlen(Key) + ADSIZE + 6;  /* key & data */
        ptr1 += key.length + ADSIZE + 6
    }

    // C: memcpy(&lower, block + ptr1 - 4, 4);
    lower = readInt32LE(block, ptr1 - 4)

    // C: if (lower > 0) { BigKeyParent = i; BigKeyParentOffset = ptr1 - 4; scanblocks(lower, svPtr); }
    if (lower > 0) { // trace high end descendents of mark if any
        BigKeyParent = i
        BigKeyParentOffset = ptr1 - 4
        scanblocks(lower, svPtr)
    }
}
