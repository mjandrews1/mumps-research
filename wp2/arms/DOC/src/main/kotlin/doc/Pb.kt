package doc

// Unit 106 (B4 tranche 2): interp-native-btree.cpp::pb (batch 4,
// core stratum; LOC 19, score 13.7, rank 131) - DumpBlocks-style
// raw block printer (the pb of the native-btree diagnostics). The
// ByteArray carrier rides the arm btree byte domain (BtreeH.kt
// gblbf / BtreeExterns BigKey precedent); MBLOCK (btree.h:169/172
// both arms 2048) and ADSIZE (btree.h:51, BtreeExterns) are the
// live seats; MMARK is defines.h:120 (0xff, BtreeExterns Int for
// the ByteArray domain). printf maps to System.out per H-001.
// The opaque int read is memcpy(&x, block, 4) - the little-endian
// four-byte load renders as the explicit byte assembly (H-012
// byte domain; the platform ABI load kept in value). The pointer
// walk rebinds block locally (param rebind: the caller's buffer
// is only read). Vendor quirks kept and noted: the OFF_T z local
// is read only by the commented-out diagnostic lines (H-013 dead
// local, kept as comment); the trailing-reset loop tests
// block[ss]==0 BEFORE ss>0 (index at ss==0 reads the first byte
// - the render keeps the vendor's test order shape via the two-
// condition while, bounds-equal here since the walk starts at
// MBLOCK-1); x is declared and the first memcpy overwrites the
// walk unused x s[0] reads (the vendor reads block[0..3] each
// iteration through the advancing pointer); the doubled-quote
// of the // memcpy z / %x diagnostic remains a comment per the
// vendor. Naming per H-017 (pb kept verbatim, pb the vendor name).

fun pb(bn: Int, blockIn: ByteArray) {
    var ss = MBLOCK - 1
    while (blockIn[ss] == 0.toByte() && ss > 0) ss--

    System.out.print("\n=== block=" + bn + " size=" + ss + " =====>\n")

    var block = blockIn
    var s = 0
    while (s <= ss) {
        // memcpy(&x, block, 4)
        val x = (block[0].toInt() and 0xff) or
            ((block[1].toInt() and 0xff) shl 8) or
            ((block[2].toInt() and 0xff) shl 16) or
            ((block[3].toInt() and 0xff) shl 24)
        System.out.print(" [" + x)
        block = block.copyOfRange(4, block.size)
        s++

        if ((block[0].toInt() and 0xff) == MMARK) {
            System.out.print(" MBLOCK END MMARK] ")
            break
        }

        var len = 0
        while (len < block.size && block[len] != 0.toByte()) len++
        System.out.print(" " + String(block, 0, len) + "]")

        // memcpy(&z, block + strlen(block) + 1, ADSIZE);
        // printf(" %x]", z);
        block = block.copyOfRange(len + ADSIZE + 2, block.size)
    }

    System.out.print("\n===========================================================\n")
}
