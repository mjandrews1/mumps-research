package doc

// Unit 8 (B1 tranche 2): libmpscpp.cpp::mdh_tree_level_add, the FIVE-
// column overload of the vendor's GtkTreeStore helper family (batch 1,
// simple stratum; LOC 24, score 8.4; roster row 24; the units.csv file
// carries nine same-named overload instances, this is the loc-24
// instance). Same idiom as the batch-2 loc-29 seven-column render
// (MdhTreeLevelAdd.kt, unit 43): coded delimiter bytes via Char(nnn)
// per H-015, DOLLAR-byte command target, pointer sprintf rendered as a
// stable identity-hex text (documented divergence, in-line), zfcn
// dispatch on the H-014 stub. A Kotlin OVERLOAD by arity (five vs
// seven columns), matching the vendor overload family exactly.

private const val CMD_TARGET_8 = "\u0024z~mdh~tree~level~add"
private val OPEN_PAREN_8 = Char(0x00ce)
private val COMMA_8 = Char(0x00d0)
private val CLOSE_PAREN_8 = Char(0x00cf)

fun mdhTreeLevelAdd(
    tree: Any?, depth: Int,
    col1: String, col2: String, col3: String,
    col4: String, col5: String
) {
    val tmp = CharArray(4096)
    val tmp1 = CharArray(256)
    val tmp2 = CharArray(256)

    CstringOps.copyAtS(tmp, 1, CMD_TARGET_8)
    CstringOps.catAt(tmp, 1, OPEN_PAREN_8)

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
    CstringOps.catAt(tmp, 1, CLOSE_PAREN_8)

    Stubs.zfcn(tmp, tmp2, Globals.svPtr)
}
