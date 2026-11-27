package both

// Unit 196 render: libmpscpp.cpp::embed1 (roster 196, batch 7,
// 29 LOC). The quote-doubler: returns the input unchanged when
// it contains no double quote; otherwise rewrites every '"'
// as '""' into the static buffer.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: no callables (pure
//     local walk).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: embed1 keeps vendor name/case.
//   h013 dead_store - reused: the k flag folds to a direct
//     contains-quote test (the vendor scan-then-rescan shape
//     is kept: the first pass only SETS the flag, the second
//     pass copies).
//
// Quirks kept (ledgered):
//   - the vendor returns the INPUT pointer when no quotes
//     (no copy); the arm returns the input string - same
//     observable value.
//   - static char tmp[STR_MAX]: the static aliasing wart (a
//     caller holding the pointer across the next embed1 call
//     sees it overwritten) is UB; QUANTIZED to a value return
//     (the copy folds the alias - ADJ flag, journaled).
//   - in==NULL returns the empty tmp (""): the Kotlin String
//     parameter is non-null; the NULL arm is unreachable
//     (journaled quantization).

// char* embed1(const char *in) - libmpscpp.cpp
internal fun embed1(ind: String): String {
    // first pass: k=0 on the first quote, else k stays 1
    if (!ind.contains('"')) return ind // contains no quotes

    // second pass: copy, doubling each quote
    val sb = StringBuilder(ind.length * 2)
    for (c in ind) {
        if (c != '"') {
            sb.append(c)
        } else {
            sb.append('"')
            sb.append('"')
        }
    }
    return sb.toString() // *p2 = 0; return tmp;
}
