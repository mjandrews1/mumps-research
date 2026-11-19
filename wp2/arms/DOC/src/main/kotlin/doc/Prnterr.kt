package doc

// Unit 58: interp-parse.cpp::prnterr (batch 2, complex stratum; row
// 37: loc 127, ctrl 43, score 83.1, rank 264). The runtime error
// reporter: walks svPtr->pd1 up to the current position counting
// lines, backs off to a line start, stamps LineNumber, and prints
// the in-or-near-line banner plus the numbered message ladder.
// Vendor quirks kept: the nx back-walk double-step (the nx>1 block
// decrements once more before the final back-walk); LN starts at 1
// and LineNumber = LN-1; error codes 21, 22 and 28 are absent from
// the vendor ladder (absent here too - default prints "Unknown
// error"); case 27 carries a raw dollar byte in the message text,
// built via DOLLAR per cookbook H-015; the %c of case 12 renders the
// ErrChar carrier verbatim; the fprintf(stderr,...) formats collapse
// per H-009 (Locale.US). stderr maps to System.err (H-001 console
// divergence, documented). pd1/LineNumber/NOERR/ErrChar ride the Msv
// carriers (stateVector.h:43/63/60/95).

private const val DOLLAR = '$'

fun prnterr(i: Int, svPtr: Msv, pd1cur: Int) {
    var n = 1
    var nx: Int
    var LN = 1

    n = 1
    while (n < PS) {
        if (svPtr.pd1[n] == NUL) {
            LN++
        }
        if (n > pd1cur) break
        n++
    }

    if (svPtr.pd1[n] != NUL) nx = n else nx = n - 1

    while (svPtr.pd1[nx] != NUL && nx > 0) nx--

    if (nx > 1) {
        nx--
        while (svPtr.pd1[nx] != NUL && nx > 0) nx--
    }

    svPtr.LineNumber = (LN - 1).toLong()

    if (svPtr.NOERR != 0) return

    val lineText = CstringOps.cstringAt(svPtr.pd1, nx + 1)
    System.err.print("\n\n*** in or near line " +
            String.format(java.util.Locale.US, "%d", svPtr.LineNumber) +
            ":\n" + lineText + "\n*** E" + i + " ")

    System.err.print(when (i) {
        1 -> "Multiple adjacent operators"
        2 -> "Unmatched quotes"
        3 -> "Global not found"
        4 -> "Missing comma"
        5 -> "Argument not permitted"
        6 -> "Bad character after post-conditional"
        7 -> "Invalid quote"
        8 -> "label not found:"
        9 -> "Too many/few fcn arguments"
        10 -> "Invalid number"
        11 -> "Missing operator"
        12 -> "Unrecognized operator: " + svPtr.ErrChar
        13 -> "Keyword"
        14 -> "Argument list"
        15 -> "Divide by zero"
        16 -> "Invalid expression"
        17 -> "Variable not found"
        18 -> "Invalid reference"
        19 -> "Logical table space overflow"
        20 -> "I/O error"
        23 -> "Symbol table full"
        24 -> "Function argument error"
        25 -> "Global not permitted"
        26 -> "File error"
        27 -> DOLLAR.toString() + "N error"
        29 -> "<break> at line:"
        30 -> "Function not found"
        31 -> "Program space exceeded"
        32 -> "Stack overflow"
        33 -> "SQL error"
        34 -> "Arg count mis-match"
        35 -> "String length too long"
        36 -> "Use unit not open"
        37 -> "Unrecognized statement"
        else -> "Unknown error"
    })

    System.err.print("\n\n")
    return
}
