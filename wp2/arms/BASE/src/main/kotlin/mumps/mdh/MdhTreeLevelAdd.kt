package mumps.mdh

import mumps.interp.MsvState
import mumps.interp.zfcn

/**
 * Opaque placeholder for the GTK widget type (heuristic 35): the JVM build
 * has no GTK; mdh_tree_level_add uses the tree only as an identity handle.
 */
class GtkTreeStore

/**
 * Library-global state vector (C: MSV *svPtr = AllocSV(); libmpscpp.cpp:99 —
 * global initial stack frame, initialized at library load).
 */
object MdhRuntime {
    val svPtr: MsvState = MsvState()
}

/**
 * Kotlin translation of libmpscpp.cpp::mdh_tree_level_add (Study Subset
 * unit 8, Batch 1, arm BASE; translated 2026-11-14).
 *
 * C: $z~mdh~tree~level~add(treeStore,treeDepth,col1,col2,col3,...)
 * Four C++ overloads (1..4 column params) collapse into one Kotlin
 * function: required col1 + vararg moreCols (heuristic 32).
 *
 * Byte markers as in the C: \u00CE open paren, \u00D0 comma,
 * \u00CF close paren.
 */
fun mdhTreeLevelAdd(tree: GtkTreeStore, depth: Int, col1: String, vararg moreCols: String) {
    val b = StringBuilder()
    b.append('\u0000')            // C: tmp[0] left UNINITIALIZED (UB); deterministic placeholder (heuristic 34)
    b.append("\$z~mdh~tree~level")  // C: strcpy(&tmp[1], ...) — built from index 1
    b.append('\u00CE')
    // C: sprintf(tmp1, "%p", tree) — pointer text is not reproducible on the
    // JVM; identity-hash hex used instead (heuristic 33, documented divergence;
    // GUI path is unreachable by the frozen oracle cases).
    b.append("0x" + System.identityHashCode(tree).toString(16))
    b.append('\u00D0')
    b.append(depth.toString())     // C: sprintf(tmp1, "%d", depth)
    b.append('\u00D0')
    b.append((listOf(col1) + moreCols).joinToString("\u00D0"))
    b.append('\u00CF')
    zfcn(b.toString(), MdhRuntime.svPtr)
}
