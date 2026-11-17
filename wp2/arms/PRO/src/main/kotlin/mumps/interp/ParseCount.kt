package mumps.interp

/**
 * PRO-arm translation of interp-rpm.cpp::parse_count (B5 tranche B,
 * unit 135, rank 203). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 360-401.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): parse_count, cpp, loc 40, comment_ratio 0.024,
 * pointer_marks 7, control_marks 6, score 28.4 - exact match to
 * wp1/study-subset/units.csv row 136.
 *
 * BOUNDARY NOTE (the u128 glitch family): the pre-registered span
 * is 360-506 - the chunker's brace balancer never closes on the
 * real function (the quote-scan case upstream), so the recorded
 * boundary runs on and overlaps unit 128 scan_token (recorded
 * 259-506, real 259-297, DONE) and the batch-6 leaning_toothpickize
 * (recorded 479-506, real 479-497, stub pending). This unit
 * translates the REAL body (vendor 360-401); the residue inside
 * the recorded span is already owned - parse_pattern_element
 * (DONE, unit 104) and parse_pattern_atom_list/accept/accept_it/
 * syntax_error (DONE, unit 127 InterpRpmMisc.kt) - verification
 * only, no redeclaration (u124/u128 front-run precedent).
 *
 * parse_count(p): the quantifier arm of the pattern parser. INT
 * head: accept the lower bound, then either ".upper" (range) or a
 * bare lower bound, all wrapped in braces. Anything else "must have
 * been a DOT" (vendor comment): accept it, then INT upper gives
 * "{0,upper}", no INT gives the star quantifier. The built text
 * lands in a fresh bstring per the bstring builder family.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h014 out_of_scope_call  : this unit RESOLVES the InterpStubs
 *    parseCount stub raised by u127 (ScanToken.kt:22 ride note and
 *    the InterpRpmMisc.kt:291 parse_pattern_atom call site bind
 *    unchanged - u81/u133 stub-removal precedent); bstrcat keeps
 *    its h014 stub (owner interp-bstring.cpp, batch-9 unit);
 *    accept_it/scan/syntax_error are DONE (u127) - direct calls.
 *  - h015 grammar_collision  : pre-push scan incl. cross-file
 *    top-level names: new top-level parseCount - the removed stub
 *    clears its seat; no other collisions.
 *  - h017 naming             : parseCount - the u127 stub already
 *    fixed the vendor snake case to the arm's camel seat; vendor
 *    name parse_count kept in KDoc.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The default arm's comment "Must have been a DOT" is a vendor
 *    assumption, not a check: if the head token were neither INT
 *    nor DOT the C result stays UNINITIALIZED (UB). The Kotlin else
 *    renders the DOT arm verbatim; the unguarded arm keeps the
 *    assumption (parse_pattern only calls with INT/DOT heads, so
 *    the UB is unreachable in the vendor call graph - documented,
 *    not widened).
 *  - The nested default (INT head, non-DOT follow) has no explicit
 *    break dependency: the vendor appends lowerbound and falls to
 *    the shared bcatchr '}' close; rendered as the when-else arm.
 *  - bstring concatenations are the vendor's fresh-struct returns;
 *    the bstrcat stub family carries them until batch-9.
 */

fun parseCount(p: Parser): Bstring {
    return when (p.currentToken.kind) {
        TokenType.INT -> {
            val lowerbound = p.currentToken.text

            acceptIt(p)
            var result = newBstringLit("{")
            when (p.currentToken.kind) {
                TokenType.DOT -> {
                    acceptIt(p)
                    result = bstrcat(result,
                        bstrcat(lowerbound, newBstringLit(","))) // h014 stub (batch-9 owner)
                    if (p.currentToken.kind == TokenType.INT) {
                        result = bstrcat(result,
                            p.currentToken.text) // h014 stub (batch-9 owner)
                        acceptIt(p)
                    }
                }
                else -> {
                    result = bstrcat(result, lowerbound) // h014 stub (batch-9 owner)
                }
            }
            bcatchr(result, '}')
            result
        }
        else -> { // must have been a DOT (vendor comment, not a check)
            acceptIt(p)
            if (p.currentToken.kind == TokenType.INT) {
                val result = bstrcat(newBstringLit("{0,"),
                    bstrcat(p.currentToken.text,
                        newBstringLit("}"))) // h014 stub (batch-9 owner)
                acceptIt(p)
                result
            } else {
                newBstringLit("*")
            }
        }
    }
}
