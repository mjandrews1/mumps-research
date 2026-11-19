package doc

// Unit 66 (B3 tranche 1): libmpscpp.cpp::mdh_tree_level_add (batch 3,
// simple stratum; LOC 19, score 7.1) - the THREE-column overload (the
// roster row whose vendor identity is the col1..col3 signature at
// libmpscpp.cpp:232-264). Same family idiom as the unit 8 five-column
// and unit 43 seven-column renders: coded delimiter bytes via
// Char(nnn) per H-015, the command target's dollar byte built
// programmatically, pointer sprintf rendered as a stable identity-hex
// text (documented divergence), zfcn dispatch on the H-014 stub.
// Kotlin OVERLOAD by arity (3 vs 5/7/10) resolves the four same-name
// vendor overloads that now have renders; the remaining
// overload-family rows in later batches verify against their own
// arities the same way.

private const val CMD_TARGET_3 = "\u0024z~mdh~tree~level~add"
private val OPEN_PAREN_3 = Char(0x00ce)
private val COMMA_3 = Char(0x00d0)
private val CLOSE_PAREN_3 = Char(0x00cf)

fun mdhTreeLevelAdd(
    tree: Any?, depth: Int,
    col1: String, col2: String, col3: String
) {
    val tmp = CharArray(4096)
    val tmp1 = CharArray(256)
    val tmp2 = CharArray(256)

    CstringOps.copyAtS(tmp, 1, CMD_TARGET_3)
    CstringOps.catAt(tmp, 1, OPEN_PAREN_3)

    // sprintf(tmp1, "%p", tree): stable identity-hex text (documented
    // divergence, only stability within the emitted command matters).
    CstringOps.strcpyS(tmp1,
            java.lang.System.identityHashCode(tree).toString(16))
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, COMMA_3)

    CstringOps.strcpyS(tmp1, depth.toString())
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, COMMA_3)

    CstringOps.catAt(tmp, 1, col1)
    CstringOps.catAt(tmp, 1, COMMA_3)

    CstringOps.catAt(tmp, 1, col2)
    CstringOps.catAt(tmp, 1, COMMA_3)

    CstringOps.catAt(tmp, 1, col3)
    CstringOps.catAt(tmp, 1, CLOSE_PAREN_3)

    Stubs.zfcn(tmp, tmp2, Globals.svPtr)
}
