package doc

// Unit 113 (B4 tranche 2): interp-rpm.cpp::parse_pattern_element
// (batch 4, core stratum; LOC 35, score 27.1, rank 196) - one arm
// of the rpm Perl-matcher pattern grammar: PATTERN_CODE tokens
// expand to their character-class words inside brackets, STRING
// tokens render leaning-toothpickized inside parens, LPAREN
// recurses into the atom list. The token/scanner/parser carrier
// types are file-scope entities of interp-rpm.cpp (lines 69-90)
// that the batch-5 interp-rpm.cpp::<misc> unit owns when it
// lands; they are rendered HERE per H-004 with that ownership
// note (the vendor names token/scanner/parser/token_type are
// kept, H-017; they are new to the arm - no collisions today).
// The bstring result rides the unit-22 Bstring carrier with the
// real newBstring/bcatchr renders; the data tables pattern_codes
// ("ACELNPU") and char_classes (the seven class words) are
// file-scope statics of the same vendor file, seated here as
// private vals with the batch-5 ownership note (one-owner rule:
// the misc unit's supersede note points here). Helper calls ride
// new H-014 stub seats raised with this unit: bstrcat (owner
// interp-bstring.cpp::bstrcat, the batch-9 roster row - correctly
// tagged), leaning_toothpickize (owner: the batch-6 roster row
// of this vendor file), parse_pattern_atom_list / accept_it /
// accept / syntax_error (sub-min-loc functions of interp-rpm.cpp
// - owned by the batch-5 misc chunk). Vendor quirks kept and
// noted: the strchr(pattern_codes, toupper(...)) against
// pattern_codes yields a negative pointer difference when the
// token's first char is NOT a pattern code - the C
// char_classes[i] read is UB there, quantized here to the
// deterministic clamp 0 (divergence ledgered; the missing-code
// arm is journaled); the vendor comment "Grouping needed due to
// precedence issues" is kept verbatim; the do-while re-test at
// PATTERN_CODE keeps the vendor's accept-then-retest order.
// Naming per H-017.

// enum token_type (interp-rpm.cpp:69)
enum class token_type {
    INT, STRING, LPAREN, RPAREN, COMMA,
    DOT, PATTERN_CODE, EOT, ERROR
}

// struct token (interp-rpm.cpp:74)
class token {
    var kind: token_type = token_type.ERROR
    val text = Bstring()
}

// struct scanner (interp-rpm.cpp:80): the const char* input /
// current_char pair rides the String + index domain (the parser
// keeps the offset state; the batch-5 scanner renders the walk).
class scanner {
    var input = ""
    var currentChar = 0
    val currentToken = token()
}

// struct parser (interp-rpm.cpp:85)
class parser {
    val currentToken = token()
    val s = scanner()
}

// static const char pattern_codes[] (interp-rpm.cpp:109)
private const val PATTERN_CODES = "ACELNPU"

// static const char * const char_classes[] (interp-rpm.cpp:110)
private val CHAR_CLASSES = arrayOf(
    "[:alpha:]", "[:cntrl:]", "[:print:][:cntrl:]",
    "[:lower:]", "[:digit:]", "[:punct:]", "[:upper:]"
)

fun parse_pattern_element(p: parser): Bstring {
    val result: Bstring

    when (p.currentToken.kind) {
        token_type.PATTERN_CODE -> {
            result = newBstring("[".toCharArray())
            do {
                // int i = strchr(pattern_codes,
                //         toupper(p->current_token.text.string[0]))
                //         - pattern_codes;
                val up = p.currentToken.text.string[0].code
                val upper = if (up in 97..122) up - 32 else up
                var i = PATTERN_CODES.indexOf(upper.toChar())
                if (i < 0) i = 0 // UB quantization: header note

                val classWord = newBstring(CHAR_CLASSES[i].toCharArray())
                Stubs.bstrcat(result, classWord)
                Stubs.accept_it(p)
            } while (p.currentToken.kind == token_type.PATTERN_CODE)
            bcatchr(result, ']')
        }
        token_type.STRING -> {
            // Grouping needed due to precedence issues
            val toothpicked = Stubs.leaning_toothpickize(p.currentToken.text)
            result = Stubs.bstrcat(
                newBstring("(".toCharArray()),
                toothpicked
            )
            bcatchr(result, ')')
            Stubs.accept_it(p)
        }
        token_type.LPAREN -> {
            Stubs.accept_it(p)
            val inner = Stubs.parse_pattern_atom_list(p)
            result = Stubs.bstrcat(newBstring("(".toCharArray()), inner)
            Stubs.accept(p, token_type.RPAREN)
            bcatchr(result, ')')
        }
        else -> {
            result = newBstring(CharArray(1))
            Stubs.syntax_error(
                "Syntax error in pattern",
                p.s.input,
                p.s.currentChar
            )
        }
    }

    return result
}
