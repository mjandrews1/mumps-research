package both

// Unit 303 (BOTH B10, complex): interp-select.cpp::<misc>
// (156 LOC). Batch_plan ordinal 303; units.csv row 283. The
// \\u0024select runtime twin: the generated-code helper the
// compiler emits for the MUMPS select construct (the B9
// CompilerParseMain emitter calls it with the twenty-arg
// carriage). Ten condition/value pairs walk in order: the
// first pair tests unconditionally; every later condition
// arg is NULL-checked first (a NULL sentinel means the arg
// list ran out - the Error M4 report and sigint(100) abort);
// the first pair whose condition parses nonzero (atol) moves
// its value into out. Exhausting all ten pairs reports the
// same M4 error and aborts. The banner/date/config/include
// residue ledgers as in the unit-292 record (SQLITE live,
// SYMDEBUG undefined).
//
// BOTH consults (find_rule before render, events logged):
//   h005 out_param_bundle - reused: out is the single
//     out-param (strmove folds to the one copy-in with the
//     NUL, the writeC seat).
//   h007 char_buffer - reused: the in-carriage renders as the
//     nullable String carriers (the NULL sentinel is the
//     arg-list-end marker, not a byte-domain value).
//   h009 format_string - reused: the M4 report is the exact
//     "%d" line fold (the vendor printf, System.out print +
//     the LineNumber field).
//   h010 numeric_promotion - reused: atol on the condition
//     strings is the atolC quiet-parse seat; the nonzero test
//     is the C truthiness of the long.
//   h014 out_of_scope_call - reused: strmove/sigint ride
//     their seats (writeC, roster-254 Sigint.kt); the
//     svPtr->LineNumber field rides the Msv seat.
//   h017 naming - reused: _select keeps the vendor name; the
//     in1..in20 carriage keeps vendor names.
//   Dafny: EXEMPT (the ten-pair walk is the emitted-runtime
//     family envelope; the condition parse is the atol
//     family exemption).
//
// Vendor quirks kept (ledgered):
//   - the FIRST pair is not NULL-checked (in1 is always
//     passed by the generated call); kept.
//   - a NULL condition arg and an all-false walk share the
//     same M4 report + abort; kept verbatim (the vendor has
//     no distinct error codes).

// void _select(unsigned char * out, ... in1..in20,
//              struct MSV * svPtr) - interp-select.cpp
internal fun _select(
    out: CharArray,
    in1: String?,
    in2: String?,
    in3: String?,
    in4: String?,
    in5: String?,
    in6: String?,
    in7: String?,
    in8: String?,
    in9: String?,
    in10: String?,
    in11: String?,
    in12: String?,
    in13: String?,
    in14: String?,
    in15: String?,
    in16: String?,
    in17: String?,
    in18: String?,
    in19: String?,
    in20: String?,
    svPtr: Msv,
) {
    if (atolC(in1 ?: "").toInt() != 0) {
        writeC(out, in2 ?: "")
        return
    }

    val m4 = "*** Error M4 - Select in or near line " +
        svPtr.LineNumber + "\n"
    val rest = listOf(
        in3 to in4, in5 to in6, in7 to in8, in9 to in10,
        in11 to in12, in13 to in14, in15 to in16, in17 to in18,
        in19 to in20,
    )
    for ((cond, value) in rest) {
        if (cond == null) {
            System.out.print(m4)
            sigint(100)
        }
        if (atolC(cond ?: "").toInt() != 0) {
            writeC(out, value ?: "")
            return
        }
    }

    System.out.print(m4)
    sigint(100)
}
