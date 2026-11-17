package mumps.interp

/**
 * PRO-arm translation of interp-native-btree.cpp::pb (B4 unit
 * 111, rank 131). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 1211-1236 (the whole function sits
 * inside the file's "ifdef DumpBlocks" block-dumper section).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-native-btree.cpp::pb, cpp, loc 19,
 * comment_ratio 0.095, pointer_marks 3, control_marks 3, score
 * 13.7 - exact match to wp1/study-subset/units.csv row 112.
 *
 * pb(): the debugging block dumper - print the block number and
 * the index of the last nonzero byte, then walk the block's cell
 * headers (memcpy 4 bytes at a time as the row number), print
 * each key string, and stop at the MMARK block-end sentinel.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string       : the two printf headers and the
 *    per-cell fragments are byte-identical (%d/%s with no
 *    width specifiers, Locale.US pinned); the trailing equals
 *    rule keeps its exact 59-character banner shape.
 *  - h013 dead_store          : the OFF_T z local feeds only the
 *    commented-out memcpy/printf pair (kept commented out, as in
 *    the vendor) - the declaration is dropped with this note.
 *  - h014 out_of_scope_call   : none - pb is self-contained
 *    (strlen is the real B1 strlenC); noted for the roster.
 *  - h015 grammar_collision  : pre-push scan clean.
 *  - h017 naming             : pb keeps the vendor spelling.
 *  - h018 preprocessor_conditional: DumpBlocks is UNDEFINED in
 *    the reference build (vendor build-mumps.script, default
 *    configure) - the reference binary compiles this function
 *    OUT. The unit is still the study-selected sample, so the
 *    arm is translated in full here with its visibility status
 *    documented (no runtime switch is added - the vendor has
 *    none: the whole function is compile-time-only debris).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - "for(ss=MBLOCK-1; block[ss]==0&&ss>0; ss--);" has NO body -
 *    the semicolon body is kept as the empty-loop form (the scan
 *    runs entirely in the header).
 *  - memcpy(&x,block,4) reinterprets the first four header bytes
 *    as int - rendered as the little-endian 4-byte read over the
 *    Char domain (the honest observable; the vendor reads raw
 *    host-endian bytes).
 *  - The walk pointer advances block+=4, then either breaks on
 *    the MMARK sentinel (printed as " MBLOCK END MMARK] ") or
 *    prints the key at the cursor and advances by
 *    strlen(key)+ADSIZE+2 (vendor comments note the ADSIZE
 *    strides over the offset word).
 *  - The commented-out z/memcpy/printf pair mid-body stays
 *    commented out, as in the vendor.
 */
private const val MBLOCK = 2048 // vendor btree.h (BtreeH.kt keeps its own copy)
private const val ADSIZE = 8 // vendor btree.h (Scanblocks.kt/BtreeH.kt keep their own copies)
private const val MMARK = 255.toChar() // vendor defines.h:120

/** vendor interp-native-btree.cpp:1211-1236 - void pb(int bn, unsigned char * block). */
fun pb(bn: Int, blockIn: CharArray) {

    // vendor: for(ss=MBLOCK-1; block[ss]==0&&ss>0; ss--);
    var ss = MBLOCK - 1
    while (blockIn[ss] == NUL && ss > 0) ss--

    print("\n=== block=" + bn + " size=" + ss + " =====>\n")

    var off = 0
    var s = 0
    while (s <= ss) {
        // vendor: memcpy(&x,block,4);
        val x = (blockIn[off].code and 0xFF) or
                ((blockIn[off + 1].code and 0xFF) shl 8) or
                ((blockIn[off + 2].code and 0xFF) shl 16) or
                ((blockIn[off + 3].code and 0xFF) shl 24)

        print(" [" + x)
        off += 4 // vendor: block=block+4;

        if (blockIn[off] == MMARK) {
            print(" MBLOCK END MMARK] ")
            break
        }

        print(" " + cstringAt(blockIn, off) + "]")

        // vendor: block=block+strlen((const char *) block)+ADSIZE+2;
        off += strlenC(blockIn, off) + ADSIZE + 2

        s++
    }

    print("\n===========================================================\n")
}
