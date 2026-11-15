package mumps.mdh

/**
 * PRO-arm translation of libmpscpp.cpp::mdh_tree_level_add (pilot B1,
 * unit 8 — all ten overloads col1..col10). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext        : the file-scope svPtr rides the mdh owner
 *    object (MdhState).
 *  - h007 char_buffer        : tmp[4096]/tmp1[256]/tmp2[256] roles: the
 *    command is built at &tmp[1] (pointer-into-buffer offset), tmp2 is
 *    uninit scratch handed to the callee.
 *  - h009 format_string      : sprintf "%p" (pointer) and "%d" idioms;
 *    %d collapses to the decimal string.
 *  - h014 out_of_scope_call  : zfcn (owner interp-bifs.cpp::zfcn, not a
 *    corpus unit) -> MdhStubs.kt.
 *  - h015 grammar_collision  : the "$z~..." command prefix contains a
 *    dollar sign and is escaped for Kotlin.
 *  - h017 naming             : mdh_tree_level_add -> mdhTreeLevelAdd.
 */

private const val NUL = '\u0000'
private const val XCE = '\u00CE'  // open paren
private const val XD0 = '\u00D0'  // comma
private const val XCF = '\u00CF'  // close paren

fun mdhTreeLevelAdd(tree: GtkTreeStore, depth: Int, col1: String) {
    mdhTreeLevelAddImpl(tree, depth, listOf(col1))
}

fun mdhTreeLevelAdd(tree: GtkTreeStore, depth: Int, col1: String, col2: String) {
    mdhTreeLevelAddImpl(tree, depth, listOf(col1, col2))
}

fun mdhTreeLevelAdd(tree: GtkTreeStore, depth: Int, col1: String, col2: String, col3: String) {
    mdhTreeLevelAddImpl(tree, depth, listOf(col1, col2, col3))
}

fun mdhTreeLevelAdd(tree: GtkTreeStore, depth: Int, col1: String, col2: String, col3: String, col4: String) {
    mdhTreeLevelAddImpl(tree, depth, listOf(col1, col2, col3, col4))
}

fun mdhTreeLevelAdd(
    tree: GtkTreeStore, depth: Int,
    col1: String, col2: String, col3: String, col4: String, col5: String
) {
    mdhTreeLevelAddImpl(tree, depth, listOf(col1, col2, col3, col4, col5))
}

fun mdhTreeLevelAdd(
    tree: GtkTreeStore, depth: Int,
    col1: String, col2: String, col3: String, col4: String, col5: String, col6: String
) {
    mdhTreeLevelAddImpl(tree, depth, listOf(col1, col2, col3, col4, col5, col6))
}

fun mdhTreeLevelAdd(
    tree: GtkTreeStore, depth: Int,
    col1: String, col2: String, col3: String, col4: String, col5: String, col6: String, col7: String
) {
    mdhTreeLevelAddImpl(tree, depth, listOf(col1, col2, col3, col4, col5, col6, col7))
}

fun mdhTreeLevelAdd(
    tree: GtkTreeStore, depth: Int,
    col1: String, col2: String, col3: String, col4: String, col5: String,
    col6: String, col7: String, col8: String
) {
    mdhTreeLevelAddImpl(tree, depth, listOf(col1, col2, col3, col4, col5, col6, col7, col8))
}

fun mdhTreeLevelAdd(
    tree: GtkTreeStore, depth: Int,
    col1: String, col2: String, col3: String, col4: String, col5: String,
    col6: String, col7: String, col8: String, col9: String
) {
    mdhTreeLevelAddImpl(tree, depth, listOf(col1, col2, col3, col4, col5, col6, col7, col8, col9))
}

fun mdhTreeLevelAdd(
    tree: GtkTreeStore, depth: Int,
    col1: String, col2: String, col3: String, col4: String, col5: String,
    col6: String, col7: String, col8: String, col9: String, col10: String
) {
    mdhTreeLevelAddImpl(tree, depth, listOf(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10))
}

private fun mdhTreeLevelAddImpl(tree: GtkTreeStore, depth: Int, cols: List<String>) {
    val tmp = CharArray(4096)
    val tmp2 = CharArray(256)

    // The vendor builds the command string at &tmp[1]; tmp[0] is left
    // uninitialized (NUL here) and zfcn owns the buffer's interpretation.
    val sb = StringBuilder("\$z~mdh~tree~level~add")
    sb.append(XCE)

    // h009: sprintf(tmp1, "%p", tree) — the C heap pointer cannot be
    // reproduced in the JVM; an identity-based analog is used (same shape,
    // "0x" + lowercase hex).
    sb.append("0x").append(Integer.toHexString(System.identityHashCode(tree)))
    sb.append(XD0)

    sb.append(depth.toString()) // h009: %d collapse
    sb.append(XD0)

    for (k in cols.indices) {
        sb.append(cols[k])
        if (k < cols.size - 1) sb.append(XD0)
    }
    sb.append(XCF)

    for (k in 0 until sb.length) tmp[1 + k] = sb[k]
    tmp[1 + sb.length] = NUL

    zfcn(tmp, tmp2, MdhState.svPtr) // h014 stub
}
