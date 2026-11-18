package mumps.interp

/**
 * PRO-arm translation of interp-bstring.cpp::bstrcat (roster row 277,
 * batch 10; units.csv: loc 18, comment_ratio 0.053, pointer_marks 0,
 * control_marks 1, score 4.2, rank 5). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 52-77 (the unit selected
 * out of the B1 unit-22 residue, interp/Bstring.kt).
 *
 * struct bstring bstrcat(const struct bstring left,
 *                        const struct bstring right)
 *
 * Concatenate two length-based strings; abort through sigint(100)
 * when the result would exceed STR_MAX (32,000).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param     : fprintf(stderr,...) -> System.err.
 *  - h007 char_buffer         : the C struct-by-value args map to
 *    Bstring references; bstrcat never writes through left/right,
 *    so reading through the alias is observationally identical to
 *    the vendor's copies (the bcatchr *str alias precedent).
 *  - h009 format_string       : stderr message byte-exact.
 *  - h012 unsigned_byte_domain: result.string rides the zero-filled
 *    CharArray; the terminator slots land on zeros (a benign
 *    superset, same as newBstring).
 *  - h014 out_of_scope_call   : sigint(int) is owned by
 *    interp-sysfunc.cpp -> the InterpStubs.sigint stub (B1 unit
 *    25 delegate noted in Bstring.kt).
 *  - h015 grammar_collision   : STR_MAX declared file-private
 *    (Bstring.kt / GlobalName.kt / MumpsMain.kt precedent).
 *  - h017 naming              : bstrcat single word keeps vendor
 *    spelling; new_bstring -> newBstring.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The "Dummy return - unreachable" statement after sigint(100)
 *    is kept (sigint aborts in the vendor; the Kotlin stub throws).
 *  - The dummy return is new_bstring("") - an empty length-based
 *    struct (length 0), rendered as newBstring over a lone NUL.
 */
private const val STR_MAX = 32000 // h015: file-private (Bstring.kt precedent)

/** interp-bstring.cpp:55-77 — struct bstring bstrcat(left, right). */
fun bstrcat(left: Bstring, right: Bstring): Bstring {
    val catlength = left.length + right.length

    if (catlength > STR_MAX) {
        System.err.print("String too long - aborting.\n")
        sigint(100) // h014 stub (owner interp-sysfunc.cpp, B1 unit 25)
        /* Dummy return - unreachable */
        return newBstring(CharArray(1)) // new_bstring("")
    }

    val result = Bstring()

    result.length = catlength
    // strncpy(result.string, left.string, left.length);
    for (i in 0 until left.length) {
        result.string[i] = left.string[i]
    }
    result.string[left.length] = NUL
    // strncat(result.string, right.string, right.length);
    for (i in 0 until right.length) {
        result.string[left.length + i] = right.string[i]
    }
    result.string[left.length + right.length] = NUL

    return result
}
