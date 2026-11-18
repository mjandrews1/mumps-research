package mumps.interp

/**
 * PRO-arm translation of interp-sql.cpp::<misc>
 * (batch-8 unit 228, units.csv data line 228, rank 183).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), the
 * file's full residue (misc physical loc 50, comment_ratio
 * 0.519 - the banner/comment-heavy residue typical of this
 * file).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-sql.cpp::<misc>, cpp, loc 50,
 * comment_ratio 0.519, pointer_marks 4, control_marks 6,
 * score 23.6, rank 183, batch 8 - exact match to
 * wp1/study-subset/units.csv line 228.
 *
 * Residue contents (vendor lines, all carried by existing
 * seats):
 *  - 34: #define SQLITE - the SQLITE build flag is DEFINED in
 *    this very header on the reference build (the h018 anchor
 *    of the whole SQL family: SqlBtree unit 217, RsltCallback
 *    unit 200, AllocSV unit 219, GlobalData unit 204 all treat
 *    the SQLITE arms as LIVE because of this line).
 *  - 36: #define CSTR (char *) - plain cast, disappears.
 *  - 38-39: OK 1 / BAD 0 - per-unit file-private const
 *    precedent (SqlBtree OK_B, unit 217).
 *  - 46-53: includes (stdio/stdlib/string, mumpsc/defines.h,
 *    btree.h, global.h, libmpscpp.h, keyfix.h) - carried by
 *    their owning units.
 *  - 67: void sigint(int) prototype - Sigint unit 191.
 *  - 69: unsigned char rsltSpec[STR_MAX] - the
 *    SqlCallbackState.rsltSpec carrier (RsltCallback unit
 *    200; comment "result interface" kept there).
 *  - 71-72: the sql() prototype - the InterpStubs sql stub
 *    (owner: interp-sql.cpp::sql, batch-9 residue; SqlBtree
 *    unit 217 calls it).
 *  - 884: #include <sqlite3.h> mid-file - the sqlite3 C API
 *    edge; the JVM carrier documents it (h001 family).
 *  - 886: static int call_back = 0 - the
 *    SqlCallbackState.callBack carrier (RsltCallback unit 200).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h013 dead_store : nothing executable in the residue.
 *  - h014 out_of_scope_call: the sql() prototype keeps its
 *    named InterpStubs stub (batch-9 owner).
 *  - h015 grammar_collision: this file introduces no new
 *    top-level Kotlin declarations - no collisions.
 *  - h017 naming     : header residue; no vendor names are
 *    rendered as new Kotlin identifiers here.
 *  - h018 reference_build_visibility: #define SQLITE (line 34)
 *    is the LIVE anchor - every #if defined(SQLITE) arm in the
 *    family resolves to its SQLITE branch on the reference
 *    build.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The sqlite3.h include sits at line 884, INSIDE the
 *    function span of the file (after btree's body) - the
 *    vendor includes mid-TU; kept as note.
 *  - call_back is static (file-scope) but read by
 *    rslt_callback and the sql() machinery - the shared
 *    SqlCallbackState carrier is the fold (unit-200 KDoc).
 */
// Header residue: no Kotlin declarations emitted (see KDoc).
