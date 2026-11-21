package doc

// Unit 226 (B8 tranche 1): libmpscpp.cpp::mdh_tree_level_add
// (batch 8, simple stratum; LOC 31, score 10.3, rank 102; units.csv
// row 237; vendor libmpscpp.cpp:534, physically verified this
// session) - the EIGHT-column overload, closing the family arity
// set (the landed seats cover one through seven columns: units
// 126, 127, 66, 128, 8, 43; this is arity ten: tree, depth and
// eight columns). Consult H-015 hit-reuse: coded delimiter bytes
// via Char(nnn) per H-015 (the commerce-e open paren, the d-zero
// comma, the commerce-f close paren), the command target's dollar
// byte built programmatically, pointer sprintf rendered as a
// stable identity-hex text (documented divergence, family
// precedent), depth decimal, each of the eight columns comma-
// separated with the close paren on the last, zfcn dispatch on
// the H-014 stub. Kotlin overloads resolve by arity; the family
// rows now all have renders. Naming per H-017.

private const val CMD_TARGET_6 = "\u0024z~mdh~tree~level~add"
private val OPEN_PAREN_6 = Char(0x00ce)
private val COMMA_6 = Char(0x00d0)
private val CLOSE_PAREN_6 = Char(0x00cf)

fun mdhTreeLevelAdd(
    tree: Any?, depth: Int,
    col1: String, col2: String, col3: String,
    col4: String, col5: String, col6: String,
    col7: String, col8: String
) {
    val tmp = CharArray(4096)
    val tmp1 = CharArray(256)
    val tmp2 = CharArray(256)

    CstringOps.copyAtS(tmp, 1, CMD_TARGET_6)
    CstringOps.catAt(tmp, 1, OPEN_PAREN_6)

    // sprintf(tmp1, "%p", tree): rendered as a stable identity-hex
    // text (documented divergence, family precedent).
    CstringOps.strcpyS(tmp1, java.lang.System.identityHashCode(tree).toString(16))
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, COMMA_6)

    CstringOps.strcpyS(tmp1, depth.toString())
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, COMMA_6)

    CstringOps.catAt(tmp, 1, col1)
    CstringOps.catAt(tmp, 1, COMMA_6)

    CstringOps.catAt(tmp, 1, col2)
    CstringOps.catAt(tmp, 1, COMMA_6)

    CstringOps.catAt(tmp, 1, col3)
    CstringOps.catAt(tmp, 1, COMMA_6)

    CstringOps.catAt(tmp, 1, col4)
    CstringOps.catAt(tmp, 1, COMMA_6)

    CstringOps.catAt(tmp, 1, col5)
    CstringOps.catAt(tmp, 1, COMMA_6)

    CstringOps.catAt(tmp, 1, col6)
    CstringOps.catAt(tmp, 1, COMMA_6)

    CstringOps.catAt(tmp, 1, col7)
    CstringOps.catAt(tmp, 1, COMMA_6)

    CstringOps.catAt(tmp, 1, col8)
    CstringOps.catAt(tmp, 1, CLOSE_PAREN_6)

    Stubs.zfcn(tmp, tmp2, Globals.svPtr)
}
