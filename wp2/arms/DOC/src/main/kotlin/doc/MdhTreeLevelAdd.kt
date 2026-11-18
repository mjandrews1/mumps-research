package doc

// Unit 43: libmpscpp.cpp::mdh_tree_level_add (batch 2, core stratum) -
// the ten-column GtkTreeStore overload (vendor signature carries tree,
// depth and seven columns). The vendor coded-delimiter bytes (open paren
// commerce-e, comma d-zero, close paren commerce-f) are built with
// Char(nnn) programmatically per cookbook H-015; the command target
// string's dollar byte is also built programmatically (here DOLLAR).
// The pointer formatting adaptation is documented in-line; zfcn
// dispatch is an out-of-scope stub.

private const val CMD_TARGET = "\u0024z~mdh~tree~level~add"
private val OPEN_PAREN = Char(0x00ce)
private val COMMA = Char(0x00d0)
private val CLOSE_PAREN = Char(0x00cf)

fun mdhTreeLevelAdd(
    tree: Any?, depth: Int,
    col1: String, col2: String, col3: String,
    col4: String, col5: String, col6: String, col7: String
) {
    val tmp = CharArray(4096)
    val tmp1 = CharArray(256)
    val tmp2 = CharArray(256)

    CstringOps.copyAtS(tmp, 1, CMD_TARGET)
    CstringOps.catAt(tmp, 1, OPEN_PAREN)

    // sprintf(tmp1, "%p", tree): vendor prints an implementation-defined
    // pointer text; rendered as a stable identity-hex text (documented
    // divergence, only stability within the emitted command matters).
    CstringOps.strcpyS(tmp1, java.lang.System.identityHashCode(tree).toString(16))
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, COMMA)

    CstringOps.strcpyS(tmp1, depth.toString())
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, COMMA)

    CstringOps.catAt(tmp, 1, col1)
    CstringOps.catAt(tmp, 1, COMMA)

    CstringOps.catAt(tmp, 1, col2)
    CstringOps.catAt(tmp, 1, COMMA)

    CstringOps.catAt(tmp, 1, col3)
    CstringOps.catAt(tmp, 1, COMMA)

    CstringOps.catAt(tmp, 1, col4)
    CstringOps.catAt(tmp, 1, COMMA)

    CstringOps.catAt(tmp, 1, col5)
    CstringOps.catAt(tmp, 1, COMMA)

    CstringOps.catAt(tmp, 1, col6)
    CstringOps.catAt(tmp, 1, COMMA)

    CstringOps.catAt(tmp, 1, col7)
    CstringOps.catAt(tmp, 1, CLOSE_PAREN)

    Stubs.zfcn(tmp, tmp2, Globals.svPtr)
}
