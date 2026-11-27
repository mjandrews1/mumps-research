package both

// Units 126-128 (BOTH B5 T1, simple): the three libmpscpp.cpp
// mdh_tree_level_add overloads. Batch_plan ordinals 126 (15 LOC, score
// 5.9), 127 (17 LOC, score 6.5), 128 (21 LOC, score 7.7). All three
// build the same coded-argument carriage into the Mumps z-function
// call: "\$z~mdh~tree~level~add" + xce + tree-pointer + xd0 + depth +
// xd0 + column text (xd0 between columns, xcf at the end), then invoke
// zfcn. The vendor starts the buffer at tmp[1] (byte 0 left
// uninitialized for the coded-string convention); the payload string
// below starts at the same offset content.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: tmp[4096] is mutable scratch, tmp1 a
//     small formatter scratch, tmp2 the callee's out-buffer; classified
//     by role per the rule action (scratch here builds as CharArray
//     because the payload carries high bytes; the formatter scratch
//     becomes a local String).
//   h009 format_string - consulted, ADAPTED (hit, not reuse): %d is
//     the exact Locale.US digit formatter per the rule action, but C %p
//     has no Kotlin format twin; the tree pointer renders as an opaque
//     native handle printed "0x" + lowercase hex (C prints 0x-hex; a
//     null pointer would print "(nil)", unreachable for a non-null
//     Long handle; divergence ledgered in the journal).
//   h015 grammar_collision - reused: the "\$z~..." string is escaped
//     (Kotlin string template), the vendor byte escapes \xce/\xd0/\xcf
//     render as decimal char constants (206/208/207) per the rule's
//     control/high-byte action.
//   h014 out_of_scope_call - reused: zfcn is not yet translated (owner
//     interp-zfcns.cpp::zfcn, roster unit 184); the TODO stub in
//     BothCRuntime.kt names the owner and delegates when it lands.
//   h017 naming - reused: mdh_tree_level_add -> mdhTreeLevelAdd; col1..4
//     and depth keep their vendor names (no keyword collisions).
//   MISS pointer_handle (all three units): the GtkTreeStore* parameter
//     is an opaque native pointer with no representation rule in the
//     base; recorded as a miss and a batch-close capture candidate
//     (T1 recurrence 3 in this tranche alone).
//
// Dafny: exempt (string-carriage into a stubbed callee; no decidable
// numeric core to specify).

private const val XCE = 206.toChar() // vendor \xce open-paren marker
private const val XD0 = 208.toChar() // vendor \xd0 comma marker
private const val XCF = 207.toChar() // vendor \xcf close-paren marker

// tree: opaque native handle for GtkTreeStore* (see the pointer_handle
// miss note; the handle's printed form matches C %p for non-null).
internal fun mdhTreeLevelAdd(
    tree: Long,
    depth: Int,
    col1: String,
) = mdhTreeLevelAddImpl(tree, depth, listOf(col1))

internal fun mdhTreeLevelAdd(
    tree: Long,
    depth: Int,
    col1: String,
    col2: String,
) = mdhTreeLevelAddImpl(tree, depth, listOf(col1, col2))

internal fun mdhTreeLevelAdd(
    tree: Long,
    depth: Int,
    col1: String,
    col2: String,
    col3: String,
    col4: String,
) = mdhTreeLevelAddImpl(tree, depth, listOf(col1, col2, col3, col4))

private fun mdhTreeLevelAddImpl(tree: Long, depth: Int, cols: List<String>) {
    val carriages = StringBuilder()
    carriages.append("\$z~mdh~tree~level~add")
    carriages.append(XCE)
    carriages.append(ptrHex(tree)) // C: sprintf(tmp1, "%p", tree)
    carriages.append(XD0)
    carriages.append(depth.toString()) // C: sprintf(tmp1, "%d", depth)
    cols.dropLast(1).forEach {
        carriages.append(it)
        carriages.append(XD0)
    }
    cols.lastOrNull()?.let { carriages.append(it) }
    carriages.append(XCF)

    // C: zfcn((unsigned char *) tmp, (unsigned char *) tmp2, svPtr)
    // with the payload at tmp[1] (byte 0 left uninitialized in the
    // vendor; the stub takes the payload string directly).
    zfcn(carriages.toString().toCharArray(), CharArray(STR_MAX), svPtr)
}

// %p seat: C prints a non-null pointer as 0x + lowercase hex.
private fun ptrHex(p: Long): String = "0x" + java.lang.Long.toHexString(p)

// Unit 258 (BOTH B9, core): libmpscpp.cpp::mdh_tree_level_add
// (26 LOC, batch_plan ordinal 258; units.csv row 266) - the
// NINE-COLUMN overload of the units-126-128 family above (the
// 7th roster appearance of the unit_id string across batches;
// this batch-9 entry is the distinct col1..col9 face, NOT the
// batch-8 unit-236 twin). Same coded-argument carriage:
// z~mdh~tree~level~add + xce + tree + xd0 + depth + xd0 +
// col1..col8 (xd0 between) + col9 + xcf, then zfcn.
//
// Consults: the units-126-128 set above carries the family
// rulings (h007/h009/h015/h014/h017, the pointer_handle miss);
// this overload adds no new rule surface, so the family
// consults ride unchanged (events rows cite the unit-258
// application of each).
internal fun mdhTreeLevelAdd(
    tree: Long,
    depth: Int,
    col1: String,
    col2: String,
    col3: String,
    col4: String,
    col5: String,
    col6: String,
    col7: String,
    col8: String,
    col9: String,
) = mdhTreeLevelAddImpl(tree, depth, listOf(col1, col2, col3, col4, col5, col6, col7, col8, col9))
