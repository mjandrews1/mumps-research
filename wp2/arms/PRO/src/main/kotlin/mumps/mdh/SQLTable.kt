package mumps.mdh

import mumps.interp.cstringAt
import mumps.interp.interpret
import mumps.interp.strcatC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::SQL_Table(char *, int)
 * (pilot B2, unit 34, provisional under deviation C-003). Translated
 * independently from the pristine vendor source (mumps-14-may-2026.tgz,
 * sha256 621deb25...ffdbe), reference source lines 3828-3849
 * (units.csv: 17 LOC).
 * The MUMPS SQL table intrinsic: set the interpreter's current-table and
 * ztable-size state via Interpret, returning the table name.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : cmd[STR_MAX] command buffer (C-string
 *    domain), rebuilt per statement like the vendor's strcpy/sprintf.
 *  - h009 format_string      : sprintf(cmd, "set <dollar>ztabzize=%d", c)
 *    collapses to the exact decimal string of c (no formatting
 *    remainder).
 *  - h012 unsigned_byte_domain: cmd is a Char(0..255) domain.
 *  - h014 out_of_scope_call  : Interpret (owner interp-parse.cpp
 *    machinery) -> existing InterpStubs.interpret stub;
 *    strcpy/strcat resolve to the real B1 CstringOps helpers -
 *    reused unmodified, no new stubs.
 *  - h015 grammar_collision  : the vendor command strings contain raw
 *    dollar-sign bytes, which the pre-push scan forbids in Kotlin
 *    sources; the byte is rebuilt as 36.toChar() (the pre-existing
 *    exception is the MdhStubs dollar-z comment only).
 *  - h017 naming             : SQL_Table keeps the vendor name;
 *    top-level fun (vendor free function).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The second Interpret sets "ztabzize" - the vendor's spelling of
 *    the ztable size tag, typo kept.
 *  - cout << "Table size too large.\n" maps to println (stdout).
 *  - The c>10 guard throws the shared MumpsGlobalException carrier
 *    (unit 32) with GLOBAL_NOT_FOUND = 2 (libmpscpp.h:71) - here a
 *    size error, kept as the vendor wrote it.
 *  - The no-arg/string/mstring SQL_Table overload siblings are
 *    separate units (below min LOC for this one's scope).
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

// dollar byte for the vendor command strings; not written as a raw
// character (h015 pre-push scan forbids it in Kotlin sources)
private val DOLLAR = 36.toChar()

fun SQL_Table(s: String, c: Int): Mstring {
    val cmd = CharArray(STR_MAX)
    val tab = Mstring(s) // vendor: mstring tab; tab = s;

    if (c > 10) {
        println("Table size too large.") // vendor: cout << ... << endl
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor: strcpy(cmd,"set <dollar>table=\""); strcat(cmd,s);
    //         strcat(cmd,"\""); Interpret(cmd,svPtr);
    writeCstring(cmd, "set " + DOLLAR + "table=" + '"')
    strcatC(cmd, s)
    strcatC(cmd, "\"")
    interpret(cstringAt(cmd), MdhState.svPtr)

    // vendor: sprintf(cmd,"set <dollar>ztabzize=%d",c); Interpret(cmd,svPtr);
    writeCstring(cmd, "set " + DOLLAR + "ztabzize=" + c) // h009 collapse
    interpret(cstringAt(cmd), MdhState.svPtr)

    return tab
}
