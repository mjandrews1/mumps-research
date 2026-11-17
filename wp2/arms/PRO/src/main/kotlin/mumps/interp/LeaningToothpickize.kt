package mumps.interp

/**
 * PRO-arm translation of interp-rpm.cpp::leaning_toothpickize
 * (B6 tranche C, unit 183, rank 54). Translated independently
 * from the pristine vendor source (mumps-14-may-2026.tgz,
 * sha256 621deb25...ffdbe), reference source lines 479-496.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): leaning_toothpickize, cpp,
 * loc 17, comment_ratio 0.056, pointer_marks 0, control_marks
 * 4, score 7.7 - exact match to wp1/study-subset/units.csv row
 * 183 (batch 6).
 *
 * struct bstring leaning_toothpickize(const struct bstring
 * unmangled): escape a pattern-match token for regex
 * compilation - walk the unmangled token, emit a literal
 * double quote for the 0xD2 embedded-quote sentinel
 * (DOUBLE_QUOTE, interp-rpm.cpp:58), backslash-escape every
 * other punctuation byte, pass the rest through. RESOLVES the
 * InterpStubs leaningToothpickize stub raised by unit 104
 * (ParsePatternElement.kt:119 binds unchanged, same package
 * top-level name).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h012 unsigned_byte_domain: the walk is over the bstring
 *    byte domain; ispunct is the C-locale helper (printable,
 *    non-alnum, non-space ASCII; per-file private, mdh/Input
 *    ispunctC precedent - that one is private to mdh).
 *  - h014 out_of_scope_call : new_bstring/newBstringLit and
 *    bcatchr are the Bstring machinery shared seats
 *    (Bstring.kt:61/:75, newBstringLit reconciled into
 *    InterpRpmMisc.kt:132 unit 127); DOUBLE_QUOTE is the
 *    internal carrier (InterpRpmMisc.kt:109, vendor
 *    interp-rpm.cpp:58) - reused unmodified.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: the InterpStubs stub is REMOVED and this
 *    is the single top-level leaningToothpickize - no
 *    collisions.
 *  - h017 naming            : leaning_toothpickize keeps the
 *    vendor snake_case spelling (ParsePatternElement call site
 *    binds by that exact name).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - A REAL ASCII double quote is neither the 0xD2 sentinel
 *    nor passes the second if's literal-quote test - it is
 *    silently DROPPED (no emit at all); kept verbatim.
 *  - The second if ("!= double quote") is always true when
 *    reached (the sentinel arm continues first) - a redundant
 *    vendor guard, kept verbatim.
 */
fun leaningToothpickize(unmangled: Bstring): Bstring {
    val result = newBstringLit("") // vendor: new_bstring("")
    var i = 0
    while (i < unmangled.length) { // vendor: for (i=0; ...)
        if (unmangled.string[i] == DOUBLE_QUOTE) {
            bcatchr(result, '"') // vendor: bcatchr(&result,'"');
            i++
            continue
        }
        if (unmangled.string[i] != '"') { // vendor: != '"'
            if (ispunctC(unmangled.string[i])) {
                bcatchr(result, '\\') // vendor: bcatchr(&result,'\');
            }
            bcatchr(result, unmangled.string[i])
        }
        i++
    }
    return result
}

/** vendor backtick byte (ASCII 96); Match.kt BTICK carrier
 *  precedent - no raw backtick in source. */
private const val BTICK = 96.toChar()

/** C-locale ispunct: printable, non-alnum, non-space ASCII. */
private fun ispunctC(c: Char): Boolean =
    c in '!'..'/' || c in ':'..'@' || c in '['..BTICK || c in '{'..'~'
