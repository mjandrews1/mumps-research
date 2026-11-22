package daf

// DAF rpm-parser seat carriers and named-owner stubs, raised by the
// parse_pattern_element render (unit 113). Carriers follow BASE-log
// rows 68 (struct by-value -> class instance: the bstring bag) and 54
// family (struct pointer params -> object references); stubs follow
// the row 37 prototype discipline (named-owner seats):
//   bstring ops (new_bstring/bstrcat/bcatchr) - owner interp-bstring.cpp::<misc>
//                  (roster unit 22, complex; rendered when the DAF cycle
//                  reaches its batch cell)
//   leaning_toothpickize - SEAT-RELEASED by the unit-157 render
//                  LeaningToothpickize.kt (DAF B6 T1): the real body
//                  lives there and the ParsePatternElement.kt caller
//                  binds unchanged (the prior header blame "not a
//                  roster unit" was a TAG ERROR: units.csv batch-6
//                  row 157 IS this function)
// accept_it/accept/syntax_error/parse_pattern_atom_list were SEAT-
// RELEASED by the unit-152 RpmMisc.kt render (DAF B5 T3): their real
// bodies live there and the ParseCount.kt/ParsePatternElement.kt/
// ScanToken.kt callers bind unchanged.
// Token-kind values mirror the interp-rpm.cpp enum order
// (INT, STRING, LPAREN, RPAREN, ...); the pattern_codes/char_classes
// carriers carry the vendor tables verbatim.

const val TOKEN_INT_D = 0
const val TOKEN_STRING_D = 1
const val TOKEN_LPAREN_D = 2
const val TOKEN_RPAREN_D = 3
const val TOKEN_PATTERN_CODE_D = 6
const val TOKEN_EOT_D = 7
const val TOKEN_ERROR_D = 8

// struct bstring (row 68 fold): fresh instance per construction site.
class DafBstring {
    val string = StringBuilder()
}

// struct parser { struct token current_token; struct scanner s; } with
// struct scanner { const char* input; const char* current_char; }:
// the pointer pair becomes an array plus an index (current_char -
// input == the index; row 81 end-guard family for the raw input).
class DafParser {
    var currentTokenKind: Int = TOKEN_ERROR_D
    var currentTokenText: DafBstring = DafBstring()
    var input: CharArray = CharArray(0)
    var currentChar: Int = 0
}

// static const char pattern_codes[] = "ACELNPU"
const val PATTERN_CODES_D = "ACELNPU"

// static const char * const char_classes[]
val CHAR_CLASSES_D = arrayOf(
    "[:alpha:]", "[:cntrl:]", "[:print:][:cntrl:]",
    "[:lower:]", "[:digit:]", "[:punct:]", "[:upper:]"
)

// struct bstring new_bstring(const char*) - owner interp-bstring.cpp::<misc>
// (roster unit 22).
fun newBstringD(s: String): DafBstring =
    throw NotImplementedError(
        "new_bstring: out-of-scope owner (interp-bstring.cpp::<misc>, roster unit 22)")

// struct bstring bstrcat(struct bstring, struct bstring) - same owner.
fun bstrcatD(a: DafBstring, b: DafBstring): DafBstring =
    throw NotImplementedError(
        "bstrcat: out-of-scope owner (interp-bstring.cpp::<misc>, roster unit 22)")

// void bcatchr(struct bstring*, char) - same owner.
fun bcatchrD(a: DafBstring, ch: Char): Unit =
    throw NotImplementedError(
        "bcatchr: out-of-scope owner (interp-bstring.cpp::<misc>, roster unit 22)")


