package doc

// Unit 266 (B9 tranche 2): libmpscpp.cpp::mdh_tree_level_add
// (batch 9, core stratum; units.csv row 267: loc 26, ptr 2, ctl
// 0, score 7.6, rank 110; physLOC 26 physically verified this
// session) - the SIX-column overload, closing the family arity
// gap (the landed seats covered one, two, three, four, five,
// seven and eight columns: MdhTreeLevelAdd1..4.kt, ...5.kt,
// ...kt unit 43 seven, ...6.kt unit 226 eight; this is the
// col1..col6 arity the roster row 267 owns - the family rows
// now ALL have renders). Consult H-015 hit-reuse: coded
// delimiter bytes via Char(nnn) per H-015 (the commerce-e open
// paren, the d-zero comma, the commerce-f close paren), the
// command target's dollar byte built programmatically, pointer
// sprintf rendered as a stable identity-hex text (documented
// divergence, family precedent), depth decimal, each of the
// six columns comma-separated with the close paren on the
// last col6, zfcn dispatch on the H-014 stub. Kotlin overloads
// resolve by arity. Naming per H-017.

private const val CMD_TARGET_7 = "\u0024z~mdh~tree~level~add"
private val OPEN_PAREN_7 = Char(0x00ce)
private val COMMA_7 = Char(0x00d0)
private val CLOSE_PAREN_7 = Char(0x00cf)

fun mdhTreeLevelAdd(
    tree: Any?, depth: Int,
    col1: String, col2: String, col3: String,
    col4: String, col5: String, col6: String
) {
    val tmp = CharArray(4096)
    val tmp1 = CharArray(256)
    val tmp2 = CharArray(256)

    CstringOps.copyAtS(tmp, 1, CMD_TARGET_7)
    CstringOps.catAt(tmp, 1, OPEN_PAREN_7)

    // sprintf(tmp1, "%p", tree): rendered as a stable identity-hex
    // text (documented divergence, family precedent).
    CstringOps.strcpyS(tmp1, java.lang.System.identityHashCode(tree).toString(16))
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, COMMA_7)

    CstringOps.strcpyS(tmp1, depth.toString())
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, COMMA_7)

    CstringOps.catAt(tmp, 1, col1)
    CstringOps.catAt(tmp, 1, COMMA_7)

    CstringOps.catAt(tmp, 1, col2)
    CstringOps.catAt(tmp, 1, COMMA_7)

    CstringOps.catAt(tmp, 1, col3)
    CstringOps.catAt(tmp, 1, COMMA_7)

    CstringOps.catAt(tmp, 1, col4)
    CstringOps.catAt(tmp, 1, COMMA_7)

    CstringOps.catAt(tmp, 1, col5)
    CstringOps.catAt(tmp, 1, COMMA_7)

    CstringOps.catAt(tmp, 1, col6)
    CstringOps.catAt(tmp, 1, CLOSE_PAREN_7)

    Stubs.zfcn(tmp, tmp2, Globals.svPtr)
}
