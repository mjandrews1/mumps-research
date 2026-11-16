package mumps.interp

/**
 * PRO-arm translation of interp-native-btree.cpp::scanblocks (pilot
 * B2, unit 50, provisional under deviation C-003). Translated
 * independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 216-258 (units.csv: 27 LOC, score 27.1, rank 195).
 *
 * Traces descendant blocks of the B-tree keeping the highest key
 * value seen (the BigKey* globals), recursing into the high-end
 * descendants.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext        : the file-scope BigKey/BigKeyBlock/
 *    BigKeyOffset/BigKeyLower/BigKeyData/BigKeyParent/
 *    BigKeyParentOffset (lines 92-101) become the NativeBtreeState
 *    singleton (InterpStubs), registered with source lines.
 *  - h008 static_scratch    : the vendor's static Key pointer and
 *    static mark[2] (commented "do not recreate during recursion")
 *    carry no state across iterations - Key is reassigned before
 *    every use and mark is a constant - so fresh locals preserve
 *    the semantics; the statics were an allocation optimization
 *    only (documented divergence).
 *  - h010 numeric_promotion  : the vendor's unsigned int globals map
 *    to Int (block numbers stay in range) and OFF_T (off_t) maps to
 *    Long; the 4/8-byte little-endian block reads are explicit
 *    .code shifts (the vendor's memcpy relies on the x86-64
 *    reference platform's endianness, kept).
 *  - h012 unsigned_byte_domain: the block is a Char(0..255) domain;
 *    the vendor's pointer arithmetic (block + ptr1, Key - 4,
 *    index(Key, 0) + 1) becomes offsets into the CharArray.
 *  - h013 dead_store         : the vendor's int is (declared with
 *    lower and ptr1) is never used - dropped with this note.
 *  - h014 out_of_scope_call  : freadx (owner interp-native-btree.cpp
 *    :111, batch-7 corpus unit) -> new InterpStubs stub (the vendor's
 *    unsigned char** out-param becomes the return value); strcmp/
 *    strcpy/index become the CstringOps/String helpers - real reuses.
 *  - h017 naming            : scanblocks keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The svPtr parameter is never used by the vendor body (freadx
 *    takes no svPtr in its inline form); kept in the signature.
 *  - The commented-out BigKeyData memcpy above the live one stays
 *    out, as in the vendor.
 */

private const val MMARK = 255.toChar() // vendor defines.h:120
private const val ADSIZE = 8 // vendor btree.h:51
private const val UNMODIFIED = 0 // vendor interp-native-btree.cpp:56

/** 4 little-endian domain bytes as an int (x86-64 memcpy, h010). */
private fun getIntLE(b: CharArray, off: Int): Int =
    b[off].code or (b[off + 1].code shl 8) or
        (b[off + 2].code shl 16) or (b[off + 3].code shl 24)

/** 8 little-endian domain bytes as a long (x86-64 memcpy, h010). */
private fun getLongLE(b: CharArray, off: Int): Long {
    var v = 0L
    for (k in 7 downTo 0) v = (v shl 8) or (b[off + k].code.toLong() and 0xffL)
    return v
}

fun scanblocks(i: Int, svPtr: Msv) {
    // vendor: static unsigned char mark[2] = {MMARK, 0}; (h008: fresh,
    // constant - see header)
    val mark = charArrayOf(MMARK, NUL)

    // vendor: int lower, is, ptr1 = 4;  (is dropped, h013)
    var ptr1 = 4
    var lower: Int

    // vendor: freadx(&block, i, UNMODIFIED);
    val block = freadx(i, UNMODIFIED) // h014 stub

    while (true) {
        // vendor: Key = block + ptr1;
        val keyOff = ptr1

        // vendor: if (mark[0]==Key[0] && mark[1]==Key[1]) break; // block end
        if (mark[0] == block[keyOff] && mark[1] == block[keyOff + 1]) break

        // vendor: if (strcmp(Key, BigKey) > 0) {
        if (cstringAt(block, keyOff).compareTo(cstringAt(NativeBtreeState.bigKey)) > 0) {

            // vendor: strcpy(BigKey, Key);
            writeCstring(NativeBtreeState.bigKey, cstringAt(block, keyOff))

            NativeBtreeState.bigKeyBlock = i
            NativeBtreeState.bigKeyOffset = ptr1
            lower = getIntLE(block, keyOff - 4) // vendor: memcpy(&lower, Key-4, 4)
            NativeBtreeState.bigKeyLower = lower

            // vendor: memcpy(&BigKeyData, index(Key, 0) + 1, ADSIZE);
            // (index(Key,0) is the NUL terminator)
            val nul = keyOff + strlenC(block, keyOff)
            NativeBtreeState.bigKeyData = getLongLE(block, nul + 1)
        }

        lower = getIntLE(block, keyOff - 4) // vendor: memcpy(&lower, Key-4, 4);

        // vendor: ptr1 += strlen(Key) + ADSIZE + 6; /*key & data*/
        ptr1 += strlenC(block, keyOff) + ADSIZE + 6
    }

    // vendor: memcpy(&lower, block + ptr1 - 4, 4);
    lower = getIntLE(block, ptr1 - 4)

    if (lower > 0) {
        NativeBtreeState.bigKeyParent = i
        NativeBtreeState.bigKeyParentOffset = ptr1 - 4
        scanblocks(lower, svPtr) // vendor: trace high end descendents
    }
}
