package both

// Unit 209 render: libmpscpp.cpp::global::Soundex (roster 209,
// batch 7; vendor body in the global-class member family,
// ~20 LOC). The \u0024zzsoundex re-invoker: retrieves the
// receiver global's value, builds the \u0024zzsoundex expression
// over the 0xCE/0xCF coded delimiters, seats it at v1d[1],
// runs fcn, reads the result from bd[1] - the Fnumber shape.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: Mglobal RETRIEVE direct;
//     fcn TODO stub direct (owner interp-fcn.cpp::fcn, the
//     Fnumber-render pin note); fnCpyAtZb/fnBdStrAt1Zb widened
//     private->internal at this render (ONE helper pair, TWO
//     consumers - the Fnumber.kt release note carries it).
//   h015 grammar-collision - reused: ASCII only in source (the
//     0xCE/0xCF bytes ride \u00CE/\u00CF escapes), no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: Soundex keeps vendor name/case as a
//     GlobalRef member.
//
// Quirks kept (ledgered):
//   - the 0xCE/0xCF coded delimiters are the zBlanks family
//     bytes (misc-6 zBlanksM/zBlanksG precedent, the
//     svcEvalZb expression envelope).
//   - the mstring concat chain folds to string concatenation;
//     gtmp is the raw retrieved value (no canonicalization).
//   - fail-throw BEFORE the build (Fnumber order kept).

// mstring global::Soundex() - libmpscpp.cpp (member)
internal fun GlobalRef.Soundex(): String {
    val gtmp = CharArray(STR_MAX)

    val f = Mglobal(RETRIEVE, ref.toCharArray(), gtmp, svPtr)

    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // m = "$zzsoundex" .Concat("\xce") .Concat(gtmp) .Concat("\xcf")
    val m = "\u0024zzsoundex" + '\u00CE' + cStringOf(gtmp) + '\u00CF'

    fnCpyAtZb(svPtr.v1d, 1, m) // strcpy(&v1d[1], m.data)
    fcn(svPtr)
    return fnBdStrAt1Zb(svPtr.bd) // m = (char *) &bd[1]
}
