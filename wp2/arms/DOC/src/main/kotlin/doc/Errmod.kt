package doc

import java.util.Locale

// Unit 87 (B3 tranche 3 / sub-push A): compiler-sys1.c::errmod_
// (batch 3, complex stratum; LOC 109, score 52.5). The compiler error
// message catalog: maps the message number to its fixed text and
// prints the "\n*** %d %s" banner to System.out (printf per H-001
// console mapping; compiler-side prints do not ride the out_file
// Writers - the Prnterr precedent). The switch arms are the vendor
// strings verbatim, INCLUDING the quirks: cases 20 and 21 print the
// literal strings "21" and "22" (off-by-one legacy, kept); case 26
// carries the \u0024Order name and case 32 the \u0024text name (H-015: raw
// dollar-letter in C strings renders via the unicode escape, same
// runtime bytes); the vendor default prints ONLY the number with the
// empty message and the UNEVALUATED extra argument (printf
// "\n*** %d " with msgnbr, msg ignored - printf ignores extra args,
// kept in the same shape); the main banner prints msgnbr - 1 (the
// off-by-one kept verbatim). The msg[80] buffer folds into the when
// result String (H-007 scan-only buffer). Naming per H-017 (the
// trailing underscore of errmod_ is the vendor FORTRAN-style name,
// kept verbatim; not a Kotlin keyword issue).

fun errmod_(msgnbr: Int) {
    val msg: String = when (msgnbr - 1) {
        0 -> "multiple operators"
        1 -> "unmatched quotes"
        2 -> "global not found"
        3 -> "expecting comma"
        4 -> "argument not permitted"
        5 -> "post-conditional error"
        6 -> "invalid quote"
        7 -> "label or file not found"
        8 -> "too many/few fcn args"
        9 -> "invalid nbr"
        10 -> "missing operator"
        11 -> "unrecognized operator"
        12 -> "keyword"
        13 -> "argument list"
        14 -> "divide by zero"
        15 -> "invalid expression"
        16 -> "variable not found"
        17 -> "invalid reference"
        18 -> "logical space exceeded"
        19 -> "I/O Error"
        20 -> "21"
        21 -> "22"
        22 -> "symbol table full"
        23 -> "function argument error"
        24 -> "global not permitted"
        25 -> "file error"
        26 -> "Next/\u0024Order error"
        27 -> "28"
        28 -> "break> at line:"
        29 -> "function not found"
        30 -> "program space exceeded"
        31 -> "stack overflow"
        32 -> "\u0024text() not permitted in the compiler"
        else -> {
            // vendor default: printf("\n*** %d ", msgnbr, msg) - the
            // empty message and the IGNORED extra argument, kept
            System.out.print(String.format(Locale.US, "\n*** %d ", msgnbr))
            return
        }
    }

    System.out.print(String.format(Locale.US, "\n*** %d %s", msgnbr - 1, msg))
}
