package doc

// Unit 128 (B5 tranche 1): libmpscpp.cpp::mdh_tree_level_add (batch 5,
// simple stratum; LOC 21, score 7.7) - the FOUR-column overload
// (vendor signature at libmpscpp.cpp:265-299). Same family idiom as
// the unit 126/127 renders; columns one to three each emit the
// commerce-d-zero comma separator, the fourth emits the commerce-f
// close (byte order kept verbatim).

private const val CMD_TARGET_4 = "\u0024z~mdh~tree~level~add"
private val OPEN_PAREN_4 = Char(0x00ce)
private val COMMA_4 = Char(0x00d0)
private val CLOSE_PAREN_4 = Char(0x00cf)

fun mdhTreeLevelAdd(
    tree: Any?, depth: Int,
    col1: String, col2: String, col3: String, col4: String
) {
    val tmp = CharArray(4096)
    val tmp1 = CharArray(256)
    val tmp2 = CharArray(256)

    CstringOps.copyAtS(tmp, 1, CMD_TARGET_4)
    CstringOps.catAt(tmp, 1, OPEN_PAREN_4)

    // sprintf(tmp1, "%p", tree): stable identity-hex text (documented
    // divergence, vendor family precedent).
    CstringOps.strcpyS(tmp1, java.lang.System.identityHashCode(tree).toString(16))
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, COMMA_4)

    CstringOps.strcpyS(tmp1, depth.toString())
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, COMMA_4)

    CstringOps.catAt(tmp, 1, col1)
    CstringOps.catAt(tmp, 1, COMMA_4)

    CstringOps.catAt(tmp, 1, col2)
    CstringOps.catAt(tmp, 1, COMMA_4)

    CstringOps.catAt(tmp, 1, col3)
    CstringOps.catAt(tmp, 1, COMMA_4)

    CstringOps.catAt(tmp, 1, col4)
    CstringOps.catAt(tmp, 1, CLOSE_PAREN_4)

    Stubs.zfcn(tmp, tmp2, Globals.svPtr)
}
