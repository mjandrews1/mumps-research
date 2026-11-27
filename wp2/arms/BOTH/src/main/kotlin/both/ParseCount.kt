package both

// Unit 143 (BOTH B5 T2, core): interp-rpm.cpp::parse_count (40 LOC).
// Batch_plan ordinal 143: core, LOC 40, score 28.4. The RPM pattern
// parser's repeat-count production: INT "{x}" / "{x,y}" / DOT "{0,y}"
// / bare "*" per the token stream.
//
// Vendor behavior: on INT the lower-bound text is saved, accept_it
// advances, the result opens with "{"; a following DOT advances,
// concatenates the lower bound, ",", and an optional INT upper
// bound; then bcatchr appends "}"; other tokens concatenate the
// lower bound and close. On any other token (must have been a DOT),
// accept_it advances: an INT yields "{0,<n>}", anything else "*".
//
// Carriers here (this arm's first rpm TU render - batch-5 T3's
// interp-rpm.cpp::<misc>, roster 271, owns scan/accept and the rest
// of the parser when it lands):
//   enum token_type (interp-rpm.cpp:70-71): INT, STRING, LPAREN,
//     RPAREN, COMMA, DOT, PATTERN_CODE, EOT, ERROR.
//   struct bstring (include/mumpsc/bstring.h:33-35): length +
//     string[STR_MAX]. The bstring op owners are interp-bstring.cpp:
//     ::<misc> (roster 212) and ::bstrcat (roster 5), pending.
//   struct token/parser (interp-rpm.cpp:74-87): current_token and
//     the token carrier.
//
// Quirks kept (ledgered):
//   - the default arm's comment ("Must have been a DOT.") and the
//     shape-else-STAR fallthrough stay verbatim.
//   - bcatchr(&result, '}') passes the struct by POINTER in C to
//     mutate: folded to the returned value here (h006-style holder
//     contract avoided; journaled).
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: result is the bstring carrier chain
//     over the seats below (the string[STR_MAX] capacity rides the
//     carrier; the Kotlin seat copies bounds-checked).
//   h014 out_of_scope_call - reused: acceptIt rides the roster-271
//     stub; newBstring/bstrcat/bcatchr ride the bstring-family stubs
//     (roster 212 / 5).
//   h017 naming - hit: accept_it->acceptIt, new_bstring->newBstring,
//     bstrcat/bcatchr per vendor shapes.
//   Dafny workflow: this unit's decidable core (the production
//     grammar over the token kinds) is specified in
//     spec/parse_count.dfy (verified; see the spec header).
internal enum class TokenType { INT, STRING, LPAREN, RPAREN, COMMA, DOT, PATTERN_CODE, EOT, ERROR }

// struct bstring (bstring.h:33): capacity-faithful carrier.
internal class Bstring(val length: Int, val string: CharArray)

// struct token (interp-rpm.cpp:74-77).
internal class RpmToken(val kind: TokenType, val text: Bstring)

// struct parser (interp-rpm.cpp:85-88): current_token plus the
// scanner seat; the scanner field seats at the T3 flip (the
// interp-rpm.cpp::<misc> render of the same batch extends this
// carrier: Parser.s; the T2 declaration carried current_token
// only because no parser body existed yet - journaled there).
internal class Parser(var currentToken: RpmToken, var s: Scanner? = null)

// void accept_it(struct parser*) - owner: interp-rpm.cpp::<misc>,
// roster 271 (the T3 render of the same batch; the h014
// delegate-when-owner-lands flip: this T2 seat now delegates to
// the scan cycle).
internal fun acceptIt(p: Parser) {
    p.currentToken = scan(p.s!!) // p->current_token = scan(&p->s)
}

// struct bstring new_bstring(const char[]) - owner:
// interp-bstring.cpp::<misc>, roster 212 (pending). TODO-stub.
internal fun newBstring(s: String): Bstring {
    TODO("stub: owner interp-bstring.cpp::<misc> (roster 22 - the old pin said roster 212, a both_seq number, C-015)")
}

// struct bstring bstrcat(const struct bstring, const struct bstring)
// - owner: interp-bstring.cpp::bstrcat, roster 248 (batch 9);
//   LANDED in this arm: the h014 delegate-when-owner-lands
//   flip - the real body lives in Bstrcat.kt, this seat removed.

// void bcatchr(struct bstring *, const char) - C mutates through the
// pointer; the fold returns the appended carrier. Owner:
// interp-bstring.cpp::<misc>, roster 212 (pending). TODO-stub.
internal fun bcatchr(b: Bstring, c: Char): Bstring {
    TODO("stub: owner interp-bstring.cpp::<misc> (roster 22 - the old pin said roster 212, a both_seq number, C-015)")
}

internal fun parseCount(p: Parser): Bstring {
    var result: Bstring

    when (p.currentToken.kind) {
        TokenType.INT -> {
            val lowerbound = p.currentToken.text

            acceptIt(p)
            result = newBstring("{")
            when (p.currentToken.kind) {
                TokenType.DOT -> {
                    acceptIt(p)
                    result = bstrcat(result, bstrcat(lowerbound, newBstring(",")))
                    if (p.currentToken.kind == TokenType.INT) {
                        result = bstrcat(result, p.currentToken.text)
                        acceptIt(p)
                    }
                }
                else -> {
                    result = bstrcat(result, lowerbound) // default: lower bound alone
                }
            }
            result = bcatchr(result, '}') // &result fold: returned value
        }
        else -> { /* Must have been a DOT. (vendor comment kept) */
            acceptIt(p)
            if (p.currentToken.kind == TokenType.INT) {
                result = bstrcat(newBstring("{0,"), bstrcat(p.currentToken.text, newBstring("}")))
                acceptIt(p)
            } else {
                result = newBstring("*")
            }
        }
    }

    return result
}
