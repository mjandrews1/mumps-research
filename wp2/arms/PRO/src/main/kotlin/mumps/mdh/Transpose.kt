package mumps.mdh

import mumps.interp.Msv
import mumps.interp.NUL
import mumps.interp.cstringAt
import mumps.interp.sigint

// vendor subscript delimiter byte (char 1), per-file private
// (u46 Multiply.kt convention)
private const val SUBSCRIPT_MARK = 1.toChar()

/**
 * PRO-arm translation of libmpscpp.cpp::Transpose (B4 unit 117,
 * rank 75). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 2113-2134.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::Transpose, cpp, loc 15,
 * comment_ratio 0.062, pointer_marks 0, control_marks 4, score
 * 8.5 - exact match to wp1/study-subset/units.csv row 118; the
 * score disambiguates the string overload from the sibling
 * char* (score 8.0) and mstring (8.4) forwarders, which forward
 * to this shape and are NOT separate corpus rows of this batch.
 *
 * Transpose(string, string): build the two named globals, then
 * for every first-level subscript aa of A and second-level bb
 * under it, write A(aa,bb)'s value at B(bb,aa) - the matrix
 * transpose.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h014 out_of_scope_call  : the Global ctor, the vararg
 *    invoke, Order, and the read/assign surfaces ride the
 *    u31/u14 carriers (Order/assign are themselves owner-named
 *    h014 stubs of the class-global machinery); the free MSV
 *    wrapper (libmpscpp.cpp:10316, loc 23, not a corpus unit)
 *    is NOT translated here - roster note only.
 *  - h015 grammar_collision  : pre-push scan clean; the top-level
 *    name Transpose is new (no prior public/private conflict).
 *  - h017 naming             : Transpose keeps the vendor
 *    spelling (PascalCase in the vendor source).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - "B(bb,aa)=A(aa,bb);" is the global-from-global assignment:
 *    read the source value through the numeric/string read
 *    surface and assign into the target reference (toDouble +
 *    assign, the u31 folding conventions - documented, not
 *    silently changed).
 *  - The vendor writes "bb=\"\";" TWICE at the top of the outer
 *    loop body - the second is a dead store (bb is re-assigned
 *    before any read); the first is kept, the duplicate dropped
 *    with this note (h013).
 *  - The string/mstring forwarders fold into this one Kotlin
 *    signature per the Sw.kt wrapper convention (roster note).
 */
fun Transpose(a: String, b: String) {
    val A = Global(a)
    val B = Global(b)
    var aa = ""
    var bb = ""

    while (true) {
        aa = A(aa).Order(1)

        if (aa == "") break

        bb = "" // (the vendor's duplicate dead store dropped)

        while (true) {
            bb = A(aa, bb).Order(1)

            if (bb == "") break

            // vendor: B(bb,aa)=A(aa,bb);
            B(bb, aa).assign(A(aa, bb).toDouble())
        }
    }
}

/**
 * PRO-arm translation of libmpscpp.cpp::Transpose(struct MSV *,
 * char *, char *, char *) (B5 tranche B, unit 141, rank 136).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 10316-10347.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): Transpose, cpp, loc 23, comment_ratio 0.042,
 * pointer_marks 0, control_marks 8, score 14.1 - exact match to
 * wp1/study-subset/units.csv row 142 (batch 5).
 *
 * TAG CORRECTION (u127 rpm precedent): the unit-117 KDoc called
 * this MSV wrapper "not a corpus unit - roster note only" - the
 * roster row 142 (score 14.1, rank 136, batch 5) is exactly this
 * function; this unit is its seat and the u117 note is superseded
 * (the string/string seat above stays the u117 unit).
 *
 * The MUMPS <dollar>Transpose(a,b) machinery, in the u46
 * Multiply-wrapper shape: trim any subscript-marked tail off both
 * args, diagnose non-top-level args, bypass the leading caret,
 * and dispatch to the transpose.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro : the two printf diagnostics become
 *    println (u46 precedent).
 *  - h008 static_scratch     : the vendor's static char tmp[32]
 *    and double x are declared and NEVER used (dead, and the
 *    static is a shared-scratch hazard) - dropped with this note
 *    (h013 dead-store, u46 precedent). The (char *) "" return is
 *    a fresh immutable String.
 *  - h012 unsigned_byte_domain: args are Char(0..255) domains; the
 *    subscript marker is 1.toChar() (SUBSCRIPT_MARK convention,
 *    u46/KiLL precedent); the pointer bypass &arg[1] renders as
 *    the cstringAt offset parameter.
 *  - h014 out_of_scope_call  : the transpose body is DONE - the
 *    unit-117 Transpose(String, String) seat above binds directly
 *    (the vendor's subscripted dispatch region is always EMPTY
 *    here: any actual marker aborts in the diagnostics, so
 *    a(&arg1[i+1]) is the whole global - journaled, cf. the u46
 *    commented-dispatch note); sigint (InterpStubs stub) reused.
 *  - h015 grammar_collision  : pre-push scan incl. cross-file
 *    top-level names: new overload Transpose(Msv, String,
 *    CharArray, CharArray) - distinct from the string overload
 *    above (C++ overload analog).
 *  - h017 naming             : Transpose keeps the vendor name;
 *    the svptr/ep parameters are unused by the vendor body and
 *    stay in the signature unused as there (u46 ep shape).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The second diagnostic tests arg2[i], not arg2[j] (the
 *    index left over from the arg1 scan) - the u46 copy-paste
 *    bug family, kept verbatim.
 *  - The "array name only - no indices" branch writes
 *    arg[i+1]=0 ONE PAST the terminating NUL (the vendor clears
 *    potential residue after the name); kept verbatim - the
 *    buffers are STR_MAX-sized so the write is in bounds for any
 *    real call, and the arm keeps the byte-domain quirk.
 *  - The live subscripted dispatch a(&arg1[i+1]).Transpose(
 *    b(&arg2[j+1])) always carries empty subscript regions (see
 *    h014); the free-seat fold is journaled as the render
 *    decision with the u46/u117 precedents.
 */
fun Transpose(svptr: Msv, ep: String, arg1: CharArray, arg2: CharArray): String {
    // vendor: for (i=0; arg1[i]!=0; i++) if (arg1[i]==1) break;
    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == SUBSCRIPT_MARK) break
        i++
    }

    // vendor: if (arg1[i]==0) arg1[i+1]=0; // array name only
    if (arg1[i] == NUL) arg1[i + 1] = NUL

    if (arg1[i] == SUBSCRIPT_MARK) {
        println("\n*** Arg 1 must be top level global")
        sigint(100)
    }

    arg1[i] = NUL // vendor: arg1[i]=0 (trims the marked tail)
    val a = cstringAt(arg1, 1) // vendor: global a(&arg1[1]); // bypass caret

    // vendor: for (j=0; arg2[j]!=0; j++) if (arg2[j]==1) break;
    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == SUBSCRIPT_MARK) break
        j++
    }

    // vendor: if (arg2[j]==0) arg2[j+1]=0;
    if (arg2[j] == NUL) arg2[j + 1] = NUL

    // vendor quirk: tests arg2[i], not arg2[j] (kept, see header)
    if (arg2[i] == SUBSCRIPT_MARK) {
        println("\n*** Arg 2 must be top level global")
        sigint(100)
    }

    arg2[j] = NUL
    val b = cstringAt(arg2, 1) // vendor: global b(&arg2[1]); // bypass caret

    // vendor: a(&arg1[i+1]).Transpose(b(&arg2[j+1])); - the
    // subscript regions are always empty here (markers rejected)
    Transpose(a, b)

    return "" // vendor: return (char *) "";
}
