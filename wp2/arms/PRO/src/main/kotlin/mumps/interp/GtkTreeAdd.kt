package mumps.interp

import mumps.mdh.GtkTreeIter

/** u156 (interp-zfcns.cpp::zfcn): the local GtkTreeIter carrier
 *  declaration was removed - the mumps.mdh.MdhStubs GtkTreeIter
 *  (same opaque carrier) is imported instead, so the zfcn GTK arms
 *  can share one static iter across the GtkTreeAdd and mdh
 *  gtk_tree_* seats (the vendor has one struct type). */

/**
 * PRO-arm translation of interp-zfcns.cpp::GtkTreeAdd (B3 unit 73,
 * rank 208). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 3451-3483.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-zfcns.cpp::GtkTreeAdd, cpp, loc 30,
 * comment_ratio 0.118, pointer_marks 5, control_marks 12, score
 * 30.8, batch 3 - exact match to wp1/study-subset/units.csv row 208.
 *
 * GtkTreeAdd(itera, iterb, treestore, v1d, iargs, nargs): GUI glue
 * for the (dollar)z GTK wrapper - appends a row to the GtkTreeStore
 * whose pointer is carried (as text) in treestore, then fills up to
 * 12 columns (0..11) from v1d at the iargs offsets, returning as
 * soon as nargs runs out of column arguments.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch    : vendor "static GtkTreeStore *t" is
 *    assigned from the sscanf before every use - no state persists
 *    across calls, so the port carries a plain local.
 *  - h009 format_string     : sscanf(treestore, "%p", &t) is the
 *    pointer-text decode - stubbed as gtkTreeStoreFromPtr (the JVM
 *    has no %p domain; the carrier is the opaque handle registry).
 *  - h012 unsigned_byte_domain: treestore/v1d byte domains;
 *    GtkTreeIter is an opaque carrier (GTK C struct).
 *  - h014 out_of_scope_call : gtk_tree_store_append / gtk_tree_
 *    store_set are GTK C library calls (not corpus units) -
 *    owner-named stubs added to InterpStubs by this unit; the
 *    GtkTreeStore stand-in class is the established mdh opaque
 *    carrier (MdhStubs.kt, unit 31 era), imported here.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level GtkTreeAdd + the GtkTreeIter
 *    opaque class - no collisions (the mdh GtkTreeStore is in
 *    package mumps.mdh).
 *  - h017 naming            : GtkTreeAdd keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The comment "// column 6" on the column-7 call (vendor line
 *    3471) is a copy-paste error in the vendor - kept verbatim.
 *  - The ladder's ninth check repeats "if (nargs == 10) return;"
 *    (vendor line 3477, after the column-8 call at column index 8;
 *    it should read 11 by the pattern) - kept verbatim; the
 *    duplicate is unreachable because the earlier nargs==10 check
 *    returned.
 *  - v1d[1] -> treestore and v1d[iargs[1]] -> depth per the vendor
 *    header comment block (preserved above in spirit; the port
 *    takes treestore directly).
 */
/** iterb widened to GtkTreeIter? by u156 (interp-zfcns.cpp::zfcn):
 *  the level-1 vendor call site passes NULL (zfcns 555). */
fun GtkTreeAdd(itera: GtkTreeIter, iterb: GtkTreeIter?,
               treestore: CharArray, v1d: CharArray,
               iargs: ShortArray, nargs: Int) {

    val t = gtkTreeStoreFromPtr(treestore) // sscanf("%p") - opaque handle

    gtkTreeStoreAppend(t, itera, iterb)

    gtkTreeStoreSet(t, itera, 0, v1d, iargs[2].toInt()) // column 0

    if (nargs == 3) return

    gtkTreeStoreSet(t, itera, 1, v1d, iargs[3].toInt()) // column 1

    if (nargs == 4) return

    gtkTreeStoreSet(t, itera, 2, v1d, iargs[4].toInt()) // column 2

    if (nargs == 5) return

    gtkTreeStoreSet(t, itera, 3, v1d, iargs[5].toInt()) // column 3

    if (nargs == 6) return

    gtkTreeStoreSet(t, itera, 4, v1d, iargs[6].toInt()) // column 4

    if (nargs == 7) return

    gtkTreeStoreSet(t, itera, 5, v1d, iargs[7].toInt()) // column 5

    if (nargs == 8) return

    gtkTreeStoreSet(t, itera, 6, v1d, iargs[8].toInt()) // column 6

    if (nargs == 9) return

    gtkTreeStoreSet(t, itera, 7, v1d, iargs[9].toInt()) // column 6 (vendor copy-paste comment)

    if (nargs == 10) return

    gtkTreeStoreSet(t, itera, 8, v1d, iargs[10].toInt()) // column 7

    if (nargs == 10) return // vendor duplicate check (should be 11; kept)

    gtkTreeStoreSet(t, itera, 9, v1d, iargs[11].toInt()) // column 8

    if (nargs == 11) return

    gtkTreeStoreSet(t, itera, 10, v1d, iargs[12].toInt()) // column 9

    if (nargs == 12) return

    gtkTreeStoreSet(t, itera, 11, v1d, iargs[13].toInt()) // column 10

    if (nargs == 13) return
    return
}
