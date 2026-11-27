package both

// Unit 248 render (BOTH B9, simple): interp-bstring.cpp::bstrcat
// (18 LOC, batch_plan ordinal 248; the ParseCount.kt TODO stub
// carried a "roster 5" pin - the rank/grep mislabel family,
// C-015; corrected here per the 2026-11-27 PI ruling). struct
// bstring bstrcat(const struct bstring, const struct bstring) -
// the bstring concatenation kernel: length-sum capacity check
// against STR_MAX, byte-copy left, NUL, byte-append right, NUL.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h007 char_buffer - reused: result.string is the C byte
//     buffer; the strncpy/strncat exact-count copies render as
//     indexed array writes over a CharArray (byte domain), the
//     intermediate NUL at result.string[left.length] kept
//     verbatim (strncat re-bases from it).
//   h008 static_scratch - reused: struct bstring result is a
//     by-value local; the carrier is allocated fresh per call.
//   h014 out_of_scope_call - reused: sigint(100) is the
//     Sigint.kt seat (unit 190, terminates; the vendor dummy
//     return new_bstring("") stays unreachable, so the
//     newBstring family stub (roster-212 owner pending) is not
//     exercised - the dead return folds to nothing, h013 note).
//   h017 naming - reused: bstrcat/left/right/catlength keep
//     vendor names.
//   Dafny: exempt (struct carrier + termination path; the
//     decidable core is a byte copy).
//
// Quirks kept (ledgered):
//   - vendor capacity check is catlength > STR_MAX, so
//     catlength == STR_MAX writes the final NUL one past the
//     declared buffer (off-by-one, the vendor relies on the
//     struct padding); kept by allocating the carrier at
//     STR_MAX + 1 here and noting the divergence.
//   - the unreachable return new_bstring("") is not rendered
//     (sigint terminates; h013 dead-code note).

internal fun bstrcat(left: Bstring, right: Bstring): Bstring {
    val catlength = left.length + right.length

    // C: if (catlength > STR_MAX) { fprintf(stderr,...); sigint(100); }
    if (catlength > STR_MAX) {
        System.err.println("String too long - aborting.")
        sigint(100)
    }

    // struct bstring result; result.length = catlength;
    // strncpy(result.string, left.string, left.length);
    // result.string[left.length] = '\0';
    // strncat(result.string, right.string, right.length);
    val result = Bstring(catlength, CharArray(STR_MAX + 1))
    for (i in 0 until left.length) result.string[i] = left.string[i]
    result.string[left.length] = 0.toChar()
    for (i in 0 until right.length) result.string[left.length + i] =
        right.string[i]
    result.string[catlength] = 0.toChar()
    return result
}
