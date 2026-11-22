package daf

// Unit 136 (DAF B5 T2, core): interp-rpm.cpp::parse_count
// (units.csv row 136: loc 40, ptr 7, score 28.4, rank 203, core): the rpm
// pattern-grammar repetition-count rule: INT '{' lowerbound [DOT
// upperbound] '}' renders the {n,m} bound pair, a bare DOT arm
// renders {0,n} or the * wildcard, and the INT braces close with
// the '}' catch. Sibling grammar rule of the unit-113
// parse_pattern_element render of B4 T2 (same TU, same carrier
// seats).
// Consults: BASE-log row 68 hit-reuse (struct bstring by-value
// returns -> the DafBstring class instance carrier, fresh instance
// per construction site; struct parser pointer -> the DafParser
// object reference), row 37 family hit-adapt (accept_it and the
// token-kind seats ride the RpmParserStubsD stub roster with named
// owners - the BASE prototype discipline; the bstring ops
// new_bstring/bstrcat/bcatchr carry their roster-unit-22 owner).
// Manual-decision (no BASE row, logged per the T2 precedent):
// switch -> when over the token kind, mirroring the unit-113
// parse_pattern_element decision of B4 T2.
// Token-kind values pinned from the interp-rpm.cpp enum order
// (COMMA 4, DOT 5); raised here alongside RpmParserStubsD's
// existing three because parse_count is their first caller.
// Dafny: EXEMPT (parser-framework relay over stub seats; the
// same exemption reason as unit 113, journal row).

const val TOKEN_COMMA_D = 4 // enum token_type COMMA (interp-rpm.cpp:70)
const val TOKEN_DOT_D = 5 // enum token_type DOT (interp-rpm.cpp:71)

fun parseCountD(p: DafParser): DafBstring {

    var result: DafBstring

    when (p.currentTokenKind) { // switch (p->current_token.kind)

        TOKEN_INT_D -> {
            val lowerbound = p.currentTokenText // struct bstring copy

            acceptItD(p)
            result = newBstringD("{")
            when (p.currentTokenKind) { // switch (kind)
                TOKEN_DOT_D -> {
                    acceptItD(p)
                    result = bstrcatD(
                        result,
                        bstrcatD(lowerbound, newBstringD(",")))
                    if (p.currentTokenKind == TOKEN_INT_D) {
                        result = bstrcatD(result, p.currentTokenText)
                        acceptItD(p)
                    }
                }
                else -> {
                    result = bstrcatD(result, lowerbound)
                }
            }
            bcatchrD(result, '}')
        }

        else -> { // Must have been a DOT. (vendor comment)
            acceptItD(p)
            if (p.currentTokenKind == TOKEN_INT_D) {
                result = bstrcatD(
                    newBstringD("{0,"),
                    bstrcatD(p.currentTokenText, newBstringD("}")))
                acceptItD(p)
            } else {
                result = newBstringD("*")
            }
        }
    }

    return result
}
