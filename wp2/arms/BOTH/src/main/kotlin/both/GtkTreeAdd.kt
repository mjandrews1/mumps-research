package both

// Unit 224 render: interp-bifs.cpp::GtkTreeAdd (roster 224,
// batch 8, 63 LOC). void GtkTreeAdd(GtkTreeIter *itera,
// GtkTreeIter *iterb, unsigned char * treestore, unsigned char
// * v1d, short int * iargs, short int nargs) - the GTK tree
// store builder: sscanf the treestore pointer, append the
// iter, then set columns 0..11 off the iargs offset ladder
// with the early returns per nargs.
//
// GTK PLATFORM EDGE (the mdh_tree_selection_get_selected /
// mdh_tree_store_clear stub family, BothStubs; the
// MdhTreeLevelAdd %p-handle precedent): gtk_tree_store_append
// and gtk_tree_store_set are GTK C-API calls with no JVM
// counterpart; the unit renders as a TODO stub with the body
// ledgered below.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h009 format_string - consulted: the sscanf "%p" reverse of
//     the MdhTreeLevelAdd %p print (the opaque native handle
//     idiom); quantized with the platform edge.
//   h014 out_of_scope_call - reused/hit: the gtk_* callees are
//     the platform edge (the stub family precedent).
//   h015 grammar-collision - reused: ASCII only, no
//     backticks, no raw comment dollars, trailing newline.
//   h017 naming - reused: GtkTreeAdd keeps vendor name/case
//     (no Kotlin collision).
//   h020 pointer_handle - reused: GtkTreeIter*/GtkTreeStore*
//     are opaque native handles (the MdhTreeLevelAdd render's
//     tree-handle Long seat family).
//
// Quirks kept (ledgered; the vendor body):
//   - the column ladder repeats "if (nargs == N) return;"
//     after each set, and the TENTH rung tests nargs == 10
//     TWICE (the duplicated vendor check; the column-7 set
//     under the first, the column-8 set under the second -
//     kept verbatim in the ledger).
//   - the last two set comments both read "column 6" typos in
//     the vendor ("// column 6" on the column-7 set; the
//     column-8 set says "column 7" - the off-by-one comment
//     family; kept verbatim).
//   - iargs[2..13] index the v1d offsets (iargs[1] is the
//     depth per the head comment; iargs[0] unused here).
//   - the set calls pass -1 as the vararg terminator (the GTK
//     C-API shape).

// void GtkTreeAdd(...) - interp-bifs.cpp; GTK platform edge.
internal fun GtkTreeAdd(
    itera: Long, iterb: Long, treestore: String, v1d: CharArray,
    iargs: ShortArray, nargs: Int,
) {
    TODO("stub: GTK platform edge, GtkTreeAdd (interp-bifs.cpp; gtk_tree_store_append/set unreachable on the JVM)")
}
