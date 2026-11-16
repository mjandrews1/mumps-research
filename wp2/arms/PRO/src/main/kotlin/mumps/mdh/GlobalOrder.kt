package mumps.mdh

import mumps.interp.cstringAt
import mumps.interp.strcatC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::GlobalOrder(char *, int)
 * (pilot B2, unit 39, provisional under deviation C-003). Translated
 * independently from the pristine vendor source (mumps-14-may-2026.tgz,
 * sha256 621deb25...ffdbe), reference source lines 2538-2557
 * (units.csv: 17 LOC; the mstring/string overloads are batch-9/-3
 * units).
 *
 * The MUMPS order function over globals: builds the command
 * "s %=<dollar>order(<a>)", executes it, and reads the result variable.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : tmp[STR_MAX] command buffer built with
 *    strcpy/strcat; the vendor's static result buffer r becomes the
 *    returned String (h008: statics fresh per call).
 *  - h008 static_local       : vendor returns char * into a static
 *    buffer; Kotlin returns an immutable String, so the static's
 *    aliasing quirk disappears - fresh value per call.
 *  - h012 unsigned_byte_domain: tmp is a Char(0..255) domain.
 *  - h014 out_of_scope_call  : xecute (owner libmpscpp.cpp:7073, not
 *    a corpus unit) and SymGet (owner libmpscpp.cpp:9858, not a
 *    corpus unit) -> new MdhStubs stubs; strcat resolves to the real
 *    B1 CstringOps helper - reused unmodified.
 *  - h015 grammar_collision  : the vendor command string contains a
 *    raw dollar byte ("s %=<dollar>order(") which the pre-push scan
 *    forbids; the byte is rebuilt as 36.toChar() (SQL_Table
 *    precedent, unit 34).
 *  - h017 naming             : GlobalOrder keeps the vendor name;
 *    top-level fun (vendor free function).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The int parameter i is never used by the vendor - kept in the
 *    signature, unused here as there.
 *  - tpx is set to 1 on success and 0 on failure (svPtr->tpx).
 *  - The result variable is read back with SymGet("%") - the vendor's
 *    percent-variable convention.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)

// dollar byte for the vendor command string; not written as a raw
// character (h015 pre-push scan forbids it in Kotlin sources)
private val DOLLAR = 36.toChar()

fun GlobalOrder(a: String, i: Int): String {
    // vendor: static char r[STR_MAX]; mstring t;
    val tmp = CharArray(STR_MAX)

    // vendor: strcpy(tmp, "s %=<dollar>order("); strcat(tmp, a); strcat(tmp, ")");
    writeCstring(tmp, "s %=" + DOLLAR + "order(")
    strcatC(tmp, a)
    strcatC(tmp, ")")

    if (xecute(cstringAt(tmp)) == 0) {
        MdhState.svPtr.tpx = 1
        return SymGet("%").c_str() // vendor: t = SymGet("%"); strcpy(r, t.c_str());
    }

    MdhState.svPtr.tpx = 0
    return "" // vendor: strcpy(r, "");
}
