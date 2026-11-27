package both

// Unit 291 (BOTH B10, core): include/mumpsc/errors.h::<misc>
// (18 LOC). Batch_plan ordinal 291; units.csv row 295. The
// errors.h paste block: the anonymous fprintf(Out, ...) batch
// the vendor header drops into the translator's error-report
// emission path (no function symbol of its own; h017 note:
// the block renders named errorCascadeOut per the file
// purpose, the CgiHMisc header-face precedent). The block
// emits the C exception-to-message cascade: nine guarded
// cout lines keyed on GlobalExceptionCode (INPUT_ERROR,
// INPUT_LENGTH, SYMTAB_ERROR, GLOBAL_NOT_FOUND,
// INTERPRETER_ERROR, INVALID_CONVERSION, DATA_RANGE_ERROR,
// NUMERIC_RANGE, UNSPECIFIED). The banner/license lines are
// residue, not carried.
//
// BOTH consults (find_rule before render, events logged):
//   h001 io_stream_param - reused: fprintf(Out, ...) seats
//     through the CompilerOutState.out write fold (the
//     WriteCode outp precedent; the IOException quantizes to
//     the RuntimeException pass-through of the ExpandOrder
//     precedent).
//   h015 grammar_collision - reused and hit: the emitted
//     strings are quoted C source (embedded double quotes and
//     backslash-n); they render as Kotlin escaped string
//     literals with no raw-dollar or comment collision.
//   h017 naming - reused: the block face takes the file
//     purpose name errorCascadeOut; Out keeps the vendor
//     name on the state object seat.
//   Dafny: EXEMPT (emitted-source side effect; the cascade is
//     a constant string table, no decidable core).
//
// Vendor quirks kept (ledgered):
//   - the INTERPRETER_ERROR line carries the vendor typo
//     "Interprerer Error" (errors.h verbatim); kept.
//   - the block emits the C++ cascade (cout << ... << endl)
//     into the Out stream - the emitted text, not the Kotlin
//     runtime behavior, is the model (the generator-arm
//     idiom).

internal fun errorCascadeOut() {
    val out: java.io.Writer = CompilerOutState.out
    val w: (String) -> Unit = { s ->
        try {
            out.write(s)
        } catch (e: java.io.IOException) {
            throw RuntimeException(e) // the ExpandOrder pass-through
        }
    }
    w("     if (GlobalExceptionCode == INPUT_ERROR) ")
    w("cout << \"Input Error\" << endl;\n")
    w("     else if (GlobalExceptionCode == INPUT_LENGTH) ")
    w("cout << \"Input Length Error\" << endl;\n")
    w("     else if (GlobalExceptionCode == SYMTAB_ERROR) ")
    w("cout << \"Symbol Table Error\" << endl;\n")
    w("     else if (GlobalExceptionCode == GLOBAL_NOT_FOUND) ")
    w("cout << \"Global Not Found Error\" << endl;\n")
    w("     else if (GlobalExceptionCode == INTERPRETER_ERROR) ")
    w("cout << \"Interprerer Error\" << endl;\n")
    w("     else if (GlobalExceptionCode == INVALID_CONVERSION) ")
    w("cout << \"Invalid Conversion Error\" << endl;\n")
    w("     else if (GlobalExceptionCode == DATA_RANGE_ERROR) ")
    w("cout << \"Data Range Error\" << endl;\n")
    w("     else if (GlobalExceptionCode == NUMERIC_RANGE) ")
    w("cout << \"Numeric Range Error\" << endl;\n")
    w("     else if (GlobalExceptionCode == UNSPECIFIED) ")
    w("cout << \"Unspecified Error\" << endl;\n")
}
