package daf

// Unit 106 (DAF B4 T2, core): interp-native-btree.cpp::pb (19 LOC): the
// debug block dumper that walks a b-tree block, printing each 4-byte
// integer key and its string tail until the MMARK end marker.
// Consults: BASE-log rows 69/2 family hit-adapt (printf diagnostics ->
// print/println with the vendor text kept verbatim; stdout, not the
// stderr variant of row 69, matching the vendor printf target), row 33
// (memcpy(&x, block, 4) raw pointer reads -> an explicit 4-byte
// little-endian assembly; DOCUMENTED DIVERGENCE: the JVM exposes no
// native-endian reinterpretation of a_char array, the x86-64 reference
// build is little-endian, so the assembly below reproduces the
// reference layout), row 81 family (the block walk uses the at()
// end-guard for the final-block tail reads), row 56 (the OFF_T z /
// memcpy ADSIZE print is commented out in the vendor: kept as the same
// comment, per the row 46 dead-code-as-comment discipline).
// Vendor values pinned from btree.h/defines.h: MBLOCK 2048, ADSIZE 8,
// MMARK 0xff.
// Dafny: EXEMPT (raw console dumper; journal row).

const val MBLOCK_D = 2048   // btree.h MBLOCK
const val ADSIZE_D = 8      // btree.h ADSIZE
const val MMARK_D: Char = '\u00ff' // defines.h MMARK 0xff

fun pb(bn: Int, block: CharArray) {

    var ss = MBLOCK_D - 1 // trailing-zero trim (for ss=MBLOCK-1; ...; ss--)
    while (at(block, ss) == NUL_D && ss > 0) ss--

    // printf("\n=== block=%d size=%d =====>\n", bn, ss)
    println()
    println("=== block=$bn size=$ss =====>")
    println()

    var off = 0 // block cursor (the vendor advances the pointer)
    var s = 0
    while (s <= ss) {

        var x = 0 // memcpy(&x, block, 4): little-endian assembly (row 33)
        var b = 0
        while (b < 4) {
            x = x or ((at(block, off + b).code and 0xff) shl (8 * b))
            b++
        }
        print(" [$x")
        off += 4

        if (at(block, off) == MMARK_D) {
            print(" MBLOCK END MMARK] ")
            break
        }

        val tailLen = run { // strlen((const char *) block)
            var n = 0
            while (at(block, off + n) != NUL_D) n++
            n
        }
        print(" ${DafCRuntime.cstringAt(block, off)}]")
        // memcpy(&z, block+strlen(block)+1, ADSIZE); printf(" %x", z)
        off += tailLen + ADSIZE_D + 2
        s++
    }

    // printf("\n=====================...\n")
    println()
    println("===========================================================")
}
