package mumps.mdh

import mumps.interp.Msv
import mumps.interp.NUL
import mumps.interp.cstringAt
import mumps.interp.sigint

/**
 * PRO-arm translation of libmpscpp.cpp::Multiply (the extrinsic
 * intrinsic entry, pilot B2, unit 46, provisional under deviation
 * C-003). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 10193-10233 (units.csv: 28 LOC, score 16.8, rank 154).
 *
 * Overload disambiguation: the same-named string/char* library
 * helpers at lines 2211/2252 are the batch-5/-7 units, and
 * global::Multiply at line 2164 is the later B2 complex unit - this
 * is the MSV intrinsic wrapper (five-parameter ep signature).
 *
 * The MUMPS <dollar>Multiply(a,b,c) machinery: each argument must be
 * a top-level (unsubscripted) global; the wrapper trims any
 * subscript-marked tail, bypasses the leading caret and dispatches to
 * global::Multiply.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro : the vendor printf diagnostics become
 *    println (unit-31 precedent).
 *  - h008 static_scratch     : the vendor returns (char *) "" from a
 *    function returning static storage - Kotlin returns a fresh
 *    immutable String.
 *  - h012 unsigned_byte_domain: the args are Char(0..255) domains;
 *    the vendor's byte-1 subscript marker is 1.toChar() (KiLL
 *    precedent), and &arg[1] pointer arithmetic becomes the cstringAt
 *    offset parameter.
 *  - h013 dead_store         : the vendor's static char tmp[32] and
 *    double x are never used - dropped with this note (the static
 *    would also be a shared-scratch hazard, cf. h008).
 *  - h014 out_of_scope_call  : global::Multiply (owner
 *    libmpscpp.cpp:2164, a batch-2 complex unit later in B2) -> new
 *    MdhStubs stub, delegate when it lands; sigint (existing
 *    InterpStubs stub, owner interp-sysfunc.cpp) reused unmodified.
 *  - h017 naming             : Multiply keeps the vendor name; the
 *    svptr/ep parameters are unused by the vendor body, kept in the
 *    signature unused as there (the free Min wrapper shares this ep
 *    shape).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The second and third diagnostics test arg2[i] and arg3[i] - the
 *    index left over from the arg1 scan, not the j/k the scans
 *    compute. A copy-paste bug in the vendor; kept as-is (a
 *    subscripted arg2 would only be diagnosed if arg1 happened to
 *    carry its marker at i).
 *  - The commented-out subscripted dispatch
 *   (a(...).Multiply(b(...),c(...))) stays a comment, as in the
 *    vendor.
 */

// vendor subscript delimiter byte (char 1), cf. KiLL unit 37
private const val SUBSCRIPT_MARK = 1.toChar()

fun Multiply(svptr: Msv, ep: String, arg1: CharArray, arg2: CharArray, arg3: CharArray): String {
    // vendor: for (i=0; arg1[i]!=0; i++) if (arg1[i]==1) break;
    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == SUBSCRIPT_MARK) break
        i++
    }

    if (arg1[i] == SUBSCRIPT_MARK) {
        println("\n*** Arg 1 must be top level global")
        sigint(100)
    }

    arg1[i] = NUL // vendor: arg1[i]=0 (trims the marked tail)
    val a = Global(cstringAt(arg1, 1)) // vendor: global a(&arg1[1]); // bypass ^

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == SUBSCRIPT_MARK) break
        j++
    }

    // vendor quirk: tests arg2[i], not arg2[j] (kept, see header)
    if (arg2[i] == SUBSCRIPT_MARK) {
        println("\n*** Arg 2 must be top level global")
        sigint(100)
    }

    arg2[j] = NUL
    val b = Global(cstringAt(arg2, 1)) // vendor: global b(&arg2[1]); // bypass ^

    var k = 0
    while (arg3[k] != NUL) {
        if (arg3[k] == SUBSCRIPT_MARK) break
        k++
    }

    // vendor quirk: tests arg3[i], not arg3[k] (kept, see header)
    if (arg3[i] == SUBSCRIPT_MARK) {
        println("\n*** Arg 3 must be top level global")
        sigint(100)
    }

    arg3[k] = NUL
    val c = Global(cstringAt(arg3, 1)) // vendor: global c(&arg3[1]); // bypass ^

    // vendor (commented out there): a(&arg1[i+1]).Multiply(b(&arg2[j+1]),c(&arg3[k+1]));
    a.Multiply(b, c)

    return "" // vendor: return (char *) "";
}

/**
 * PRO-arm translation of libmpscpp.cpp::Multiply(string, string,
 * string) (B5 tranche B, unit 140, rank 172). Translated
 * independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 2211-2250.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): Multiply, cpp, loc 26, comment_ratio 0.037,
 * pointer_marks 2, control_marks 8, score 21.4 - exact match to
 * wp1/study-subset/units.csv row 141 (batch 5).
 *
 * Overload disambiguation (four same-named units in the roster):
 * this is the STRING-argument library helper at :2211; the
 * intrinsic MSV wrapper (:10193, rank 154) is the fun above;
 * global::Multiply (:2164, rank 213) is GlobalMultiply.kt (u53);
 * the char-star helper (:2252, rank 169) is the batch-7 unit.
 *
 * The vendor body is a VERBATIM copy of the global::Multiply
 * matrix loop (the only difference is the three fresh global
 * constructions from the string args). The arm folds the
 * duplicate to the single u53 seat (u117 Transpose-forwarder
 * precedent): construct A/B/C from the strings and dispatch.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch     : the vendor takes the three strings
 *    BY VALUE (fresh std::string copies); Kotlin String is
 *    immutable - the copy semantic is the same aliasing freedom.
 *  - h014 out_of_scope_call  : the matrix machinery is DONE -
 *    the Global.Multiply extension (u53) is a direct call; the
 *    Global(String) construction is the DONE carrier
 *    (GlobalCarrier.kt:21).
 *  - h015 grammar_collision  : pre-push scan incl. cross-file
 *    top-level names: new overload Multiply(String, String,
 *    String) - distinct from the MSV-intrinsic signature above
 *    (overload resolution is the C++ name-overload analog).
 *  - h017 naming             : Multiply keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The C().Kill() clears the destination global first - the
 *    fold preserves it inside the u53 seat (its first statement).
 *  - The duplicated loop in the vendor is dead weight on the
 *    roster side; the fold is journaled as the diverging render
 *    decision with the u117 precedent.
 */
fun Multiply(a: String, b: String, c: String) {
    val A = Global(a) // vendor: global A((char *) a.c_str());
    val B = Global(b) // vendor: global B((char *) b.c_str());
    val C = Global(c) // vendor: global C((char *) c.c_str());
    A.Multiply(B, C) // the verbatim-duplicate matrix loop (u53 seat)
}

/**
 * PRO-arm translation of libmpscpp.cpp::Multiply (the CHAR-STAR
 * library helper, B7 tranche 2, unit 201, rank 169). Translated
 * independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 2252-2290.
 *
 * Identity verified against the study tool: Multiply, cpp,
 * loc 26, comment_ratio 0.037, pointer_marks 2,
 * control_marks 8, score 20.8 - exact match to
 * wp1/study-subset/units.csv row 201 (batch 7).
 *
 * Overload disambiguation (four same-named units, the row-141
 * string twin's ledger): this is the CHAR-STAR-argument library
 * helper at :2252; the intrinsic MSV wrapper (:10193, rank
 * 154) and the string twin (:2211, rank 141 = u140) ride this
 * file above; global::Multiply (:2164, rank 213) is
 * GlobalMultiply.kt (u53).
 *
 * The vendor body is a VERBATIM copy of the string twin (the
 * only difference is global A/B/C constructed from the raw
 * char-star args instead of a.c_str()) and of the u53 matrix
 * loop. The arm folds the duplicate to the single u53 seat
 * (u117 Transpose-forwarder precedent, u140 fold twin):
 * construct A/B/C from the char-star cstrings and dispatch.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h006 low_level_pointer  : the char* args become the
 *    caller-owned CharArray cstrings (cstringAt reads them
 *    from offset 0; the null terminator is the boundary).
 *  - h012 unsigned_byte_domain: the args are Char(0..255)
 *    domains.
 *  - h014 out_of_scope_call  : the matrix machinery is DONE -
 *    the Global.Multiply extension (u53) is a direct call; the
 *    Global(String) construction is the DONE carrier
 *    (GlobalCarrier.kt:21), reached via cstringAt (interp
 *    CstringOps seat).
 *  - h015 grammar_collision  : pre-push scan incl. cross-file
 *    top-level names: new overload Multiply(CharArray,
 *    CharArray, CharArray) - distinct from the MSV-intrinsic
 *    and String-twin signatures above (overload resolution is
 *    the C++ name-overload analog).
 *  - h017 naming             : Multiply keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The C().Kill() clears the destination global first - the
 *    fold preserves it inside the u53 seat (its first
 *    statement).
 *  - The duplicated loop in the vendor is dead weight on the
 *    roster side; the fold is journaled as the diverging
 *    render decision with the u117/u140 precedents.
 */
fun Multiply(a: CharArray, b: CharArray, c: CharArray) {
    val A = Global(cstringAt(a, 0)) // vendor: global A((char *) a);
    val B = Global(cstringAt(b, 0)) // vendor: global B((char *) b);
    val C = Global(cstringAt(c, 0)) // vendor: global C((char *) c);
    A.Multiply(B, C) // the verbatim-duplicate matrix loop (u53 seat)
}
