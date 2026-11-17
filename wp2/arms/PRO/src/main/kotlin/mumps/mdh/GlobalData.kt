package mumps.mdh

import mumps.interp.Msv
import mumps.interp.atoiC
import mumps.interp.cstringAt
import mumps.interp.strcatC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::GlobalData(char*) (B3
 * unit 86, rank 76). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 2595-2615.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::GlobalData, cpp, loc 18,
 * comment_ratio 0.053, pointer_marks 2, control_marks 1, score
 * 8.6, batch 3 - exact match to wp1/study-subset/units.csv row
 * 76. Overload disambiguation: the char* form (loc 18) - the
 * mstring (loc 17) and string (loc 17) forms are separate vendor
 * overloads not in this batch's corpus rows.
 *
 * int GlobalData(char* a): the MUMPS (dollar)data intrinsic over
 * globals - builds the command "s %=<dollar>data(<a>)", executes
 * it, and returns the result variable's integer value (-1 on
 * execution failure).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer     : tmp[STR_MAX] command buffer built
 *    with strcpy/strcat (C-string domain).
 *  - h012 unsigned_byte_domain: tmp is a Char(0..255) domain.
 *  - h014 out_of_scope_call: xecute (owner libmpscpp.cpp:7073,
 *    not a corpus unit) and SymGet (owner libmpscpp.cpp:9858,
 *    not a corpus unit) -> existing MdhStubs stubs - reused
 *    unmodified; atoi resolves to the real B1 atoiC helper;
 *    strcat to the real strcatC (GlobalOrder precedent, unit 39).
 *  - h015 grammar_collision: the vendor command string contains
 *    a raw dollar byte ("s %=<dollar>data(") the pre-push scan
 *    forbids; rebuilt as 36.toChar() (SQL_Table precedent, unit
 *    34; GlobalOrder unit 39).
 *  - h017 naming          : GlobalData keeps the vendor name;
 *    top-level fun (vendor free function); the char* parameter
 *    renders as the carrier String (unit-39 precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor's r[STR_MAX] result buffer is only used inside
 *    the success arm (the failure arm returns -1 without touching
 *    it); kept as a local.
 *  - tpx is set 1/0 by the xecute outcome (long domain in the
 *    carrier, Msv state).
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)

// dollar byte for the vendor literal; not written as a raw
// character (h015 pre-push scan forbids it in Kotlin sources)
private val DOLLAR = 36.toChar()

fun GlobalData(a: String): Int {
    val svPtr: Msv = MdhState.svPtr
    val tmp = CharArray(STR_MAX)
    val r = CharArray(STR_MAX)

    // vendor: strcpy(tmp,"s %=<dollar>data("); strcat(tmp,a);
    // strcat(tmp,")");
    writeCstring(tmp, "s %=" + DOLLAR + "data(")
    strcatC(tmp, a)
    strcatC(tmp, ")")

    if (xecute(cstringAt(tmp)) == 0) {
        svPtr.tpx = 1
        val t = SymGet("%")
        // vendor: t = SymGet("%"); i = atoi(t.c_str()); - the r[]
        // buffer is the vendor's declared-but-unread local (its
        // strcpy(r,t) residue folds into the atoi carrier here)
        writeCstring(r, t.c_str())
        return atoiC(r)
    } else {
        svPtr.tpx = 0
        return -1
    }
}
