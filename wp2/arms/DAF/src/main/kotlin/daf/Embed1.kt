package daf

// Unit 205 (DAF B7 T2, core): interp-sql.cpp::embed1 (19 LOC,
// score 15.0): the embedded-SQL quote-doubling escaper. Near-
// direct BASE row 112 precedent (the interp-sql.cpp::embed twin
// of the SQL TU). Divergence ledger: the vendor's static char
// tmp[STR_MAX] shared scratch (the SAME buffer persists across
// calls, a reentrancy hazard in the C code) folds onto a fresh
// Kotlin String per call - recorded as the row 112 adaptation
// the BASE render carried for this same family.
// Consults (2026-11-24T16:14:30Z, nominal): BASE-log row 112
// hit-reuse (the quote probe k flag + the doubling copy loop),
// row 56 hit-adapt (the static char* return folds onto String),
// manual-decision (no BASE row: the in==NULL early return
// folds onto a null String? receiver returning the empty
// carrier).
// Dafny: EXEMPT (SQL-TU string escaper over the carrier fold;
// journal row).

fun embed1Daf(in_: String?): String { // char* embed1(const char *in)

    if (in_ == null) return "" // if (in==NULL) return tmp (tmp[0]=0)

    val cs = in_.toCharArray()

    var k = 1 // int k=1
    for (c in cs) { // quote probe
        if (c == DOUBLE_QUOTE_D) {
            k = 0
            break
        }
    }
    if (k != 0) return in_ // if (k) return (char *)in; no quotes

    val sb = StringBuilder() // char tmp[STR_MAX]; fresh fold
    for (c in cs) { // the doubling copy loop
        if (c != DOUBLE_QUOTE_D) sb.append(c)
        else { sb.append(DOUBLE_QUOTE_D); sb.append(DOUBLE_QUOTE_D) }
    }

    return sb.toString() // *p2=0; return tmp;
}
