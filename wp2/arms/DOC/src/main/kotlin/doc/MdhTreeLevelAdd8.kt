package doc

// Unit 298 (B10 tranche 2): libmpscpp.cpp::mdh_tree_level_add
// (batch 10, core stratum; units.csv row 298: loc 33, ptr 1,
// ctl 0, score 10.9, rank 108; physLOC 33 physically verified
// this session at vendor libmpscpp.cpp:481-532) - the
// NINE-column overload, the last family arity with a roster
// row after the landed seats covered one through eight
// (MdhTreeLevelAdd1..4.kt arities 1-4, ...5.kt five, unit 43
// seven, ...6.kt unit 226 eight, ...7.kt six) PLUS the
// unrowed TEN-column sibling at vendor :534 (the chunker
// fuses its head onto this row's body window - the roster
// owns the physLOC-33 nine-column overload, the ten-column
// body is ledgered here and NOT rendered: no roster row, the
// family-shape one-more-column extrapolation is the same
// construction as col10). Consult H-014 hit-reuse: coded
// delimiter bytes via Char(nnn) (commerce-e open paren, the
// d-zero comma, the commerce-f close paren per H-015), the
// command target's dollar byte built programmatically, the
// pointer sprintf %p rendered as a stable identity-hex text
// (documented divergence, family precedent), depth decimal,
// each of the nine columns comma-separated with the close
// paren on col9, zfcn dispatch on the H-014 stub. Kotlin
// overloads resolve by arity. Naming per H-017.
// Consult: unit 298, entry H-014, outcome hit-reuse,
// timestamp 2026-11-22T06:20:00Z.

private const val CMD_TARGET_8 = "\u0024z~mdh~tree~level~add"
private val OPEN_PAREN_8 = Char(0x00ce)
private val COMMA_8 = Char(0x00d0)
private val CLOSE_PAREN_8 = Char(0x00cf)

fun mdhTreeLevelAdd(
    tree: Any?, depth: Int,
    col1: String, col2: String, col3: String,
    col4: String, col5: String, col6: String,
    col7: String, col8: String, col9: String
) {
    val tmp = CharArray(4096)
    val tmp1 = CharArray(256)
    val tmp2 = CharArray(256)

    CstringOps.copyAtS(tmp, 1, CMD_TARGET_8)
    CstringOps.catAt(tmp, 1, OPEN_PAREN_8)

    // sprintf(tmp1, "%p", tree): stable identity-hex text
    CstringOps.strcpyS(tmp1, java.lang.System.identityHashCode(tree).toString(16))
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, COMMA_8)

    CstringOps.strcpyS(tmp1, depth.toString())
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, COMMA_8)

    CstringOps.catAt(tmp, 1, col1)
    CstringOps.catAt(tmp, 1, COMMA_8)

    CstringOps.catAt(tmp, 1, col2)
    CstringOps.catAt(tmp, 1, COMMA_8)

    CstringOps.catAt(tmp, 1, col3)
    CstringOps.catAt(tmp, 1, COMMA_8)

    CstringOps.catAt(tmp, 1, col4)
    CstringOps.catAt(tmp, 1, COMMA_8)

    CstringOps.catAt(tmp, 1, col5)
    CstringOps.catAt(tmp, 1, COMMA_8)

    CstringOps.catAt(tmp, 1, col6)
    CstringOps.catAt(tmp, 1, COMMA_8)

    CstringOps.catAt(tmp, 1, col7)
    CstringOps.catAt(tmp, 1, COMMA_8)

    CstringOps.catAt(tmp, 1, col8)
    CstringOps.catAt(tmp, 1, COMMA_8)

    CstringOps.catAt(tmp, 1, col9)
    CstringOps.catAt(tmp, 1, CLOSE_PAREN_8)

    Stubs.zfcn(tmp, tmp2, Globals.svPtr)
}
