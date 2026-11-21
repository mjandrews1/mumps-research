package doc

// Unit 83 (B3 tranche 3 / sub-push A): interp-zfcns.cpp::GtkTreeAdd
// (batch 3, complex stratum; LOC 30, score 30.8). The GTK tree-store
// row appender of the \u0024z~gtree family: recovers the GtkTreeStore
// handle from the coded treestore argument (the vendor sscanf "%p"
// rehydrates a C pointer; the JVM has no pointer identity domain, so
// the render treats the payload as an opaque handle string per the
// u8/u43 mdh %p divergence precedent), appends the row under the
// iterb parent, then sets column after column from the v1d coded
// stream at the iargs offsets, bounding by nargs. The GTK calls are
// out-of-scope C functions (gtk_tree_store_* are not corpus units):
// H-014 stub seats named for the GTK library. GtkTreeIter* params
// render as H-006 single-slot IntArray holders (opaque struct
// pointers). Vendor quirks kept and noted: the SECOND
// "if (nargs == 10) return;" check after the column-8 set is dead
// (the first check at the same value has already returned) - kept
// verbatim in the cascade; the vendor's inline column comments drift
// ("column 6" twice, "column 7" attached to index 8 etc.) - kept as
// comments verbatim; the final "if (nargs == 13) return;" guards
// nothing (function ends anyway), kept; column 0 is set
// UNCONDITIONALLY before any nargs bound test (the vendor assumes
// nargs >= 2 minimum), kept verbatim. Naming per H-017.

fun GtkTreeAdd(
    itera: IntArray, iterb: IntArray?, treestore: CharArray,
    v1d: CharArray, iargs: ShortArray, nargs: Short
) {
    // sscanf(treestore, "%p", &t): opaque handle recovery (the %p
    // divergence note above); static GtkTreeStore *t renders as the
    // BmgStatic-style per-call local (no cross-call state is read).
    val t = CstringOps.cstring(treestore)

    Stubs.gtkTreeStoreAppend(t, itera, iterb)

    Stubs.gtkTreeStoreSet(t, itera, 0, CstringOps.cstringAt(v1d, iargs[2].toInt())) // column 0

    if (nargs == 3.toShort()) return

    Stubs.gtkTreeStoreSet(t, itera, 1, CstringOps.cstringAt(v1d, iargs[3].toInt())) // column 1

    if (nargs == 4.toShort()) return

    Stubs.gtkTreeStoreSet(t, itera, 2, CstringOps.cstringAt(v1d, iargs[4].toInt())) // column 2

    if (nargs == 5.toShort()) return

    Stubs.gtkTreeStoreSet(t, itera, 3, CstringOps.cstringAt(v1d, iargs[5].toInt())) // column 3

    if (nargs == 6.toShort()) return

    Stubs.gtkTreeStoreSet(t, itera, 4, CstringOps.cstringAt(v1d, iargs[6].toInt())) // column 4

    if (nargs == 7.toShort()) return

    Stubs.gtkTreeStoreSet(t, itera, 5, CstringOps.cstringAt(v1d, iargs[7].toInt())) // column 5

    if (nargs == 8.toShort()) return

    Stubs.gtkTreeStoreSet(t, itera, 6, CstringOps.cstringAt(v1d, iargs[8].toInt())) // column 6

    if (nargs == 9.toShort()) return

    Stubs.gtkTreeStoreSet(t, itera, 7, CstringOps.cstringAt(v1d, iargs[9].toInt())) // column 6

    if (nargs == 10.toShort()) return

    Stubs.gtkTreeStoreSet(t, itera, 8, CstringOps.cstringAt(v1d, iargs[10].toInt())) // column 7

    // vendor dead check: the value 10 has already returned above
    if (nargs == 10.toShort()) return

    Stubs.gtkTreeStoreSet(t, itera, 9, CstringOps.cstringAt(v1d, iargs[11].toInt())) // column 8

    if (nargs == 11.toShort()) return

    Stubs.gtkTreeStoreSet(t, itera, 10, CstringOps.cstringAt(v1d, iargs[12].toInt())) // column 9

    if (nargs == 12.toShort()) return

    Stubs.gtkTreeStoreSet(t, itera, 11, CstringOps.cstringAt(v1d, iargs[13].toInt())) // column 10

    // vendor guards nothing here (function ends); kept verbatim
    if (nargs == 13.toShort()) return
}

// ---------------- Unit 245 row-note (B8 tranche 3) -----------
//
// Unit 245 (B8 tranche 3): interp-bifs.cpp::GtkTreeAdd (batch 8,
// complex stratum; LOC 30, score 30.8, rank 207; units.csv row
// 225) is a VERIFICATION-ONLY row against THIS render (the unit-83
// interp-zfcns.cpp::GtkTreeAdd owner). The two vendor bodies are
// BYTE-IDENTICAL: interp-bifs.cpp carries the same 30-line
// function text as interp-zfcns.cpp at the source anchors (the
// vendor duplicated the gtree row appender across the bifs and
// zfcns twist files; byte-diff verified this session), so the
// landed unit-83 render covers both owner files. The landed
// InterpZfcnsMisc (batch-8 tranche 2) already holds the bifs/
// zfcns cross-file fold rulings of that family; this row CLOSES
// the batch-8 roster claim with no regrade and no new body per
// the unit-186 verification-row precedent (the GlobalData / Mget
// / ReadLine folds). Consult H-014 hit-reuse: byte-identical
// twin, single vendor-name seat per H-014/H-017; row-note only.
