package daf

// Unit 238 (DAF B8 T3, complex): interp-bifs.cpp::GtkTreeAdd
// (30 LOC, score 30.8; units.csv row 225): the GTK tree-store row
// appender of the coded gtree family: recovers the GtkTreeStore
// handle from the coded treestore argument, appends the row under
// the iterb parent, then sets column after column from the v1d
// coded stream at the iargs offsets, bounding by nargs (vendor
// interp-bifs.cpp:89, physically verified this session).
// SEAT RELEASE + TAG CORRECTION: the gtkTreeAddD named stub at
// Zfcns.kt is released - the real body lands here under the stub's
// own name and signature (every Zfcns/ZfcnBifs dispatcher call
// site resolves unchanged), the stub deleted; the stub's tag cited
// the interp-zfcns.cpp twin (units.csv row 74, batch 3, not drawn
// by this arm) - the vendor duplicated the function text
// BYTE-IDENTICALLY across the bifs and zfcns twist files (diffed
// physically this session), so THE INTERP-BIFS ROSTER ROW 225 (the
// batch-8 row) owns this FIRST in-arm body of the vendor name, and
// the row-74 zfcns twin FOLDS onto this seat (the byte-identical
// single-name seat; the reverse of the DOC arm's fold, whose
// landed owner is the zfcns twin).
// Consults (2026-11-24T21:21:00Z, nominal): the sscanf "%p"
// handle recovery renders as the opaque handle fold (the unit-126
// mdh %p divergence precedent - no native pointer domain on the
// JVM; the payload is the handle text); the gtk_tree_store_append/
// set C calls are out-of-scope library functions: RAISED named
// stubs below (the gtk*D glue family; not corpus units);
// GtkTreeIter* renders as the opaque IntArray holder domain the
// dispatcher already passes. Vendor quirks kept: the SECOND
// "if (nargs == 10) return" check is dead (kept verbatim), the
// inline column comments drift (column 6 twice etc., kept
// verbatim), the trailing nargs==13 return guards nothing, and
// column 0 is set unconditionally before any bound test.
// Dafny: EXEMPT (GTK glue over stub seats; journal row).

fun gtkTreeAddD(
    itera: IntArray, iterb: IntArray?, treestore: CharArray,
    v1d: CharArray, iargs: ShortArray, nargs: Short
) {
    // sscanf((const char *) treestore, "%p", &t); // get GtkTreeStore ptr
    // (opaque handle recovery; the static GtkTreeStore *t carrier
    // is per-call - no cross-call state is read)
    val t = DafCRuntime.cstring(treestore)

    gtkTreeStoreAppendD(t, itera, iterb)

    gtkTreeStoreSetD(t, itera, 0, DafCRuntime.cstringAt(v1d, iargs[2].toInt())) // column 0

    if (nargs == 3.toShort()) return

    gtkTreeStoreSetD(t, itera, 1, DafCRuntime.cstringAt(v1d, iargs[3].toInt())) // column 1

    if (nargs == 4.toShort()) return

    gtkTreeStoreSetD(t, itera, 2, DafCRuntime.cstringAt(v1d, iargs[4].toInt())) // column 2

    if (nargs == 5.toShort()) return

    gtkTreeStoreSetD(t, itera, 3, DafCRuntime.cstringAt(v1d, iargs[5].toInt())) // column 3

    if (nargs == 6.toShort()) return

    gtkTreeStoreSetD(t, itera, 4, DafCRuntime.cstringAt(v1d, iargs[6].toInt())) // column 4

    if (nargs == 7.toShort()) return

    gtkTreeStoreSetD(t, itera, 5, DafCRuntime.cstringAt(v1d, iargs[7].toInt())) // column 5

    if (nargs == 8.toShort()) return

    gtkTreeStoreSetD(t, itera, 6, DafCRuntime.cstringAt(v1d, iargs[8].toInt())) // column 6

    if (nargs == 9.toShort()) return

    gtkTreeStoreSetD(t, itera, 7, DafCRuntime.cstringAt(v1d, iargs[9].toInt())) // column 6

    if (nargs == 10.toShort()) return

    gtkTreeStoreSetD(t, itera, 8, DafCRuntime.cstringAt(v1d, iargs[10].toInt())) // column 7

    // vendor dead check: the value 10 has already returned above
    if (nargs == 10.toShort()) return

    gtkTreeStoreSetD(t, itera, 9, DafCRuntime.cstringAt(v1d, iargs[11].toInt())) // column 8

    if (nargs == 11.toShort()) return

    gtkTreeStoreSetD(t, itera, 10, DafCRuntime.cstringAt(v1d, iargs[12].toInt())) // column 9

    if (nargs == 12.toShort()) return

    gtkTreeStoreSetD(t, itera, 11, DafCRuntime.cstringAt(v1d, iargs[13].toInt())) // column 10

    // vendor guards nothing here (function ends); kept verbatim
    if (nargs == 13.toShort()) return
}

// gtk_tree_store_append - raised named stub (external GTK C
// library owner, not a corpus unit)
fun gtkTreeStoreAppendD(t: String, itera: IntArray, iterb: IntArray?): Unit =
    throw NotImplementedError(
        "gtk_tree_store_append: out-of-scope owner (GTK C library glue; not a corpus unit)")

// gtk_tree_store_set - raised named stub (external GTK C library
// owner, not a corpus unit)
fun gtkTreeStoreSetD(t: String, itera: IntArray, column: Int, value: String): Unit =
    throw NotImplementedError(
        "gtk_tree_store_set: out-of-scope owner (GTK C library glue; not a corpus unit)")
