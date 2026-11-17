package mumps.compiler

/**
 * PRO-arm translation of compiler-sys1.c::errmod_ (B3 unit 69, rank
 * 237). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 816-932 (definition at line 816).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-sys1.c::errmod_, c, loc 109, comment_ratio
 * 0.009, pointer_marks 0, control_marks 34, score 52.5, batch 3 -
 * exact match to wp1/study-subset/units.csv row 237.
 *
 * errmod_(msgnbr): prints the compiler's numbered error text for
 * the message number (the switch indexes msgnbr-1).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 output_macro         : printf -> top-level print with the
 *    same embedded newlines (established compiler convention, cf.
 *    Abrtx.kt); the %d/%s renderings concatenate msgnbr-1 and msg.
 *  - h011 goto                 : the switch's default return is an
 *    early return; the fall-out printf follows the when.
 *  - h012 unsigned_byte_domain  : msg rides a String (the vendor
 *    char msg[80] is only ever assigned whole-string literals that
 *    fit 80 bytes; the port notes the bound).
 *  - h015 grammar_collision    : pre-push scan incl. cross-file
 *    top-level names: new top-level errmod_ - no package
 *    collisions.
 *  - h017 naming              : errmod_ keeps the vendor name
 *    (trailing underscore is not a Kotlin digit issue; the vendor
 *    name is preserved).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The default arm prints "\n*** %d " with msgnbr (NOT msgnbr-1)
 *    and passes an extra msg argument printf never renders - the
 *    vendor asymmetry is kept (number only, message dropped).
 *  - Cases 20, 21 and 27 carry bare numeric texts ("21", "22",
 *    "28"), so those numbers print twice (once as msgnbr-1, once
 *    as the message) - vendor behavior kept.
 *  - The "Next/(dollar)Order error" text of case 26 contains a literal
 *    dollar-sign byte; per the arm's dollar-byte policy it is
 *    rebuilt as 36.toChar() concatenation (cf. unit 58, Prnterr.kt
 *    "36.toChar() + N error").
 *  - The vendor's msg buffer is 80 bytes; every message fits.
 */
fun errmod_(msgnbr: Int) {
    val msg: String // vendor: char msg[80] = ""; whole-literal stores only

    when (msgnbr - 1) {
        0 -> msg = "multiple operators"
        1 -> msg = "unmatched quotes"
        2 -> msg = "global not found"
        3 -> msg = "expecting comma"
        4 -> msg = "argument not permitted"
        5 -> msg = "post-conditional error"
        6 -> msg = "invalid quote"
        7 -> msg = "label or file not found"
        8 -> msg = "too many/few fcn args"
        9 -> msg = "invalid nbr"
        10 -> msg = "missing operator"
        11 -> msg = "unrecognized operator"
        12 -> msg = "keyword"
        13 -> msg = "argument list"
        14 -> msg = "divide by zero"
        15 -> msg = "invalid expression"
        16 -> msg = "variable not found"
        17 -> msg = "invalid reference"
        18 -> msg = "logical space exceeded"
        19 -> msg = "I/O Error"
        20 -> msg = "21"
        21 -> msg = "22"
        22 -> msg = "symbol table full"
        23 -> msg = "function argument error"
        24 -> msg = "global not permitted"
        25 -> msg = "file error"
        26 -> msg = "Next/" + 36.toChar() + "Order error" // dollar byte rebuilt
        27 -> msg = "28"
        28 -> msg = "break> at line:"
        29 -> msg = "function not found"
        30 -> msg = "program space exceeded"
        31 -> msg = "stack overflow"
        32 -> msg = 36.toChar() + "text() not permitted in the compiler" // dollar byte rebuilt
        else -> {
            // default: printf("\n*** %d ", msgnbr, msg); - the %s is
            // absent from the format; the extra arg is never rendered
            print("\n*** " + msgnbr + " ")
            return
        }
    }

    print("\n*** " + (msgnbr - 1) + " " + msg)
    return
}
