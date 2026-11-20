package doc

// Unit 126 (B5 tranche 1): libmpscpp.cpp::mdh_tree_level_add (batch 5,
// simple stratum; LOC 15, score 5.9) - the ONE-column overload (vendor
// signature tree, depth, col1 at libmpscpp.cpp:175-200). Same family
// idiom as the unit 8 five-column, unit 66 three-column and unit 43
// seven-column renders: coded delimiter bytes via Char(nnn) per
// H-015, the command target's dollar byte built programmatically,
// pointer sprintf rendered as a stable identity-hex text (documented
// divergence), zfcn dispatch on the H-014 stub. Kotlin overload by
// arity (3 params here vs 5/6/8/10 elsewhere) resolves the same-name
// vendor overloads; the family rows now all have renders.

private const val CMD_TARGET_1 = "\u0024z~mdh~tree~level~add"
private val OPEN_PAREN_1 = Char(0x00ce)
private val COMMA_1 = Char(0x00d0)
private val CLOSE_PAREN_1 = Char(0x00cf)

fun mdhTreeLevelAdd(
    tree: Any?, depth: Int,
    col1: String
) {
    val tmp = CharArray(4096)
    val tmp1 = CharArray(256)
    val tmp2 = CharArray(256)

    CstringOps.copyAtS(tmp, 1, CMD_TARGET_1)
    CstringOps.catAt(tmp, 1, OPEN_PAREN_1)

    // sprintf(tmp1, "%p", tree): vendor prints an implementation-defined
    // pointer text; rendered as a stable identity-hex text (documented
    // divergence, vendor family precedent).
    CstringOps.strcpyS(tmp1, java.lang.System.identityHashCode(tree).toString(16))
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, COMMA_1)

    CstringOps.strcpyS(tmp1, depth.toString())
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, COMMA_1)

    CstringOps.catAt(tmp, 1, col1)
    CstringOps.catAt(tmp, 1, CLOSE_PAREN_1)

    Stubs.zfcn(tmp, tmp2, Globals.svPtr)
}
