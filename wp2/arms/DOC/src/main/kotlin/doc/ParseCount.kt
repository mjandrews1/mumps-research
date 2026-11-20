package doc

// Unit 143 (B5 tranche 2): interp-rpm.cpp::parse_count (physical 40
// loc) - the pattern repetition-count builder. Mirrors the carrier
// set of ParsePatternElement.kt (unit 142's companion family): the
// parser/token/token_type carriers, the real new_bstring/bstrcat/
// bcatchr renders where landed, and the H-014 stubs for the seats
// whose C++ owners are not roster units - accept_it (owner
// interp-rpm.cpp::accept_it, the batch-5 rpm-misc stub) and bstrcat
// (owner interp-bstring.cpp::bstrcat, the batch-9 roster row). The
// vendor's default-arm comment "Must have been a DOT." is kept
// verbatim; the braces/asterisk pattern syntax ({n}, {n,m}, {0,m},
// *) is emitted literally as in the vendor. Naming per H-017.

fun parse_count(p: parser): Bstring {
    var result: Bstring

    when (p.currentToken.kind) {
        token_type.INT -> {
            val lowerbound = p.currentToken.text

            Stubs.accept_it(p)
            result = newBstring("{".toCharArray())
            when (p.currentToken.kind) {
                token_type.DOT -> {
                    Stubs.accept_it(p)
                    result = Stubs.bstrcat(result,
                             Stubs.bstrcat(lowerbound, newBstring(",".toCharArray())))
                    if (p.currentToken.kind == token_type.INT) {
                        Stubs.bstrcat(result, p.currentToken.text)
                        Stubs.accept_it(p)
                    }
                }
                else -> {
                    Stubs.bstrcat(result, lowerbound)
                }
            }
            bcatchr(result, '}')
        }
        else -> { // Must have been a DOT.
            Stubs.accept_it(p)
            if (p.currentToken.kind == token_type.INT) {
                result = Stubs.bstrcat(newBstring("{0,".toCharArray()),
                         Stubs.bstrcat(p.currentToken.text,
                                  newBstring("}".toCharArray())))
                Stubs.accept_it(p)
            }
            else {
                result = newBstring("*".toCharArray())
            }
        }
    }
    return result
}
