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
 * Ten C++ overloads (1..10 column params, libmpscpp.cpp:175-589)
 * collapse into one Kotlin function: required col1 + vararg moreCols
 * (heuristic 32).
 *
 * Byte markers as in the C: 0xCE open paren, 0xD0 comma, 0xCF close
 * paren — appended programmatically as Char(nn) per heuristics 43/80
 * (no escape literals).
 *
 * UNIT 43 FIDELITY AUDIT (2026-11-15): re-checking against the pristine
 * C found two divergences from the recorded decisions, both corrected:
 * (1) the zcall name had dropped the "~add" suffix — C writes
 * "$z~mdh~tree~level~add" in ALL ten overloads; (2) the uninitialized
 * tmp[0] placeholder was a NUL char (Char(0)) but heuristic row 86's
 * decision is
 * "prepend a single space" (sibling mdh zcalls, libmpscpp.cpp:131-158,
 * initialize that byte to " ": " $zwi") — now a space.
 */
fun mdhTreeLevelAdd(tree: GtkTreeStore, depth: Int, col1: String, vararg moreCols: String) {
    val b = StringBuilder()
    b.append(' ')                // C: tmp[0] uninitialized (UB); single space per row 86 (' $zwi' convention)
    b.append("\$z~mdh~tree~level~add")  // C: strcpy(&tmp[1], "$z~mdh~tree~level~add") — built from index 1
    b.append(Char(0xCE))        // xce open paren
    // C: sprintf(tmp1, "%p", tree) — pointer text is not reproducible on the
    // JVM; identity-hash hex used instead (heuristic 33, documented divergence;
    // GUI path is unreachable by the frozen oracle cases).
    b.append("0x" + System.identityHashCode(tree).toString(16))
    val comma = Char(0xD0).toString() // cd0 comma
    b.append(comma)
    b.append(depth.toString())     // C: sprintf(tmp1, "%d", depth)
    b.append(comma)
    b.append((listOf(col1) + moreCols).joinToString(comma))
    b.append(Char(0xCF))
    zfcn(b.toString(), MdhRuntime.svPtr)
}
