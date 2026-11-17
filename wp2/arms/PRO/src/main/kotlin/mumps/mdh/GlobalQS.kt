package mumps.mdh

import mumps.interp.Msv
import mumps.interp.cstringAt
import mumps.interp.strcatC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::GlobalQS (B5 tranche
 * B, unit 144, rank 112). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 2514-2536.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): GlobalQS, cpp, loc 19,
 * comment_ratio 0.05, pointer_marks 3, control_marks 1,
 * score 11.3 - exact match to wp1/study-subset/units.csv row 145
 * (batch 5).
 *
 * GlobalQS(a,b,c): the MUMPS dollar-qs function over globals -
 * builds the command "s %=<dollar>qs(<a>,<c>)", executes it, and
 * lands the percent-variable result into the caller's b buffer
 * (empty on failure); tpx carries the outcome. The Kotlin call
 * surface is the emitted-compiler contract only (u76 ExpandQS
 * emits the C call sites; no in-arm call site binds here).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : tmp[STR_MAX] command buffer built
 *    with strcpy/strcat; b is the caller's out buffer, written
 *    with writeCstring in BOTH arms (the vendor's strcpy((char
 *    *)b,...) contract, kept verbatim).
 *  - h012 unsigned_byte_domain: tmp/b are C byte buffers.
 *  - h013 dead_code         : the vendor's "static char
 *    r[STR_MAX]" local is NEVER touched by the body (the arms
 *    write b directly) - dead declaration, dropped with note.
 *  - h014 out_of_scope_call : xecute (owner libmpscpp.cpp:7073)
 *    and SymGet (owner libmpscpp.cpp:9858) -> existing MdhStubs
 *    seats - reused unmodified; strcat resolves to the real
 *    strcatC helper (GlobalOrder/GlobalData precedent, u39).
 *  - h015 grammar_collision : the vendor command string contains
 *    a raw dollar byte ("s %=<dollar>qs(") the pre-push scan
 *    forbids; rebuilt as 36.toChar() (SQL_Table precedent, unit
 *    34; GlobalOrder unit 39).
 *  - h017 naming            : GlobalQS keeps the vendor name;
 *    top-level fun (vendor free function); the char* parameters
 *    render as the String carrier for the read-only a/c inputs
 *    and the CharArray out carrier for b (u39/u124 family
 *    precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - tpx is set 1/0 by the xecute outcome (svPtr->tpx).
 *  - The result variable is read back with SymGet("%") - the
 *    vendor's percent-variable convention (u39/u46 family).
 *  - The mstring t local is the SymGet carrier; its c_str() fold
 *    feeds the b write directly.
 */
private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)

// dollar byte for the vendor command string; not written as a raw
// character (h015 pre-push scan forbids it in Kotlin sources)
private val DOLLAR = 36.toChar()

fun GlobalQS(a: String, b: CharArray, c: String) {
    val svPtr: Msv = MdhState.svPtr
    val tmp = CharArray(STR_MAX)

    // vendor: char tmp[STR_MAX]="s %=<dollar>qs("; strcat(tmp,a);
    //         strcat(tmp,","); strcat(tmp,c); strcat(tmp,")");
    writeCstring(tmp, "s %=" + DOLLAR + "qs(")
    strcatC(tmp, a)
    strcatC(tmp, ",")
    strcatC(tmp, c)
    strcatC(tmp, ")")

    if (xecute(cstringAt(tmp)) == 0) {
        svPtr.tpx = 1
        val t = SymGet("%")
        writeCstring(b, t.c_str()) // vendor: t=SymGet("%"); strcpy((char *)b,t.c_str());
    } else {
        svPtr.tpx = 0
        writeCstring(b, "") // vendor: strcpy((char *)b,"");
    }
}
