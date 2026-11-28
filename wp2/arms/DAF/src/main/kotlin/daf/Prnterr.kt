package daf

// R1 stub-completion (2026-11-28, C-020 erratum session, tranche 6):
// interp-parse.cpp::prnterr (roster row 37): the interpreter
// parse-error printer: the pd1 walk locates the error line
// (counting the NUL line separators up to pd1cur, then walking
// back to the line start), sets svPtr->LineNumber, and - unless
// NOERR is set - prints the "*** in or near line" banner with
// the source line and the E-number tag followed by the
// error-text switch (E1..E37, the ErrChar interpolation of E12,
// the Unknown-error default).
// Consults (2026-11-28T00:00:00Z, nominal): BASE-log row 2
// family hit-reuse (fprintf(stderr,...) -> System.err.print
// with the message text VERBATIM, the Getstr1 diagnostics
// precedent); rows 81/56 hit-adapt (the pd1 program buffer and
// its NUL line separators ride the svPtr.pd1 CharArray; the
// &pd1[nx+1] line cut renders as the cstringAt slice; the
// banner keeps the vendor \n\n prefix); row 54 hit-reuse (the
// PS program-space bound = 32000, the vendor #define of the
// DOC CompilerCarriers; the DAF pd1 carrier is STR_MAX*2 wide,
// so the walk carries the size guard as the H-quantization,
// disclosed).
// Vendor quirks kept: (1) the first walk counts LN over the
// FULL pd1 up to pd1cur but breaks AFTER incrementing on the
// n > pd1cur test (an off-by-one the vendor carries - kept);
// (2) the walk-back loop `for (; pd1[nx]&&nx>0; nx--);` leaves
// nx ON a NUL or at 0, and the second walk-back (nx>1) repeats
// once more - the double-walk is the vendor line-start
// heuristic, kept verbatim; (3) NOERR suppresses only the
// printing (LineNumber is still set); (4) the switch has NO
// case 28 (the vendor skips 28 - 27 then 29, kept); (5) case 12
// interpolates svPtr->ErrChar.
// Dafny: EXEMPT (diagnostic printer; journal row).
// Release note: the Interpret.kt stub is deleted this session;
// the fifteen Interpret.kt call sites resolve here unchanged.

private const val PS_PRNTERR = 32000 // #define PS (program space)

fun prnterrDaf(i: Int, svPtr: DafMsv, pd1cur: Int): Unit {

    var n = 1
    var nx = 1
    var LN = 1

    // for (n=1; n<PS; n++) { if (!pd1[n]) LN++;
    //      if (n > pd1cur) break; }
    // H-quantization: the DAF pd1 carrier is 2*STR_MAX wide (the
    // vendor pd1 is PS-sized); the walk carries the size guard
    while (n < PS_PRNTERR && n < svPtr.pd1.size) {
        if (svPtr.pd1[n] == NUL_D) LN++
        if (n > pd1cur) break
        n++
    }

    // if (svPtr->pd1[n]) nx=n; else nx=n-1;
    nx = if (svPtr.pd1[n] != NUL_D) n else n - 1

    // for (; svPtr->pd1[nx]&&nx>0; nx--);
    while (svPtr.pd1[nx] != NUL_D && nx > 0) nx--

    // if (nx>1) { nx--; for (; svPtr->pd1[nx]&&nx>0; nx--); }
    if (nx > 1) {
        nx--
        while (svPtr.pd1[nx] != NUL_D && nx > 0) nx--
    }

    svPtr.LineNumber = LN - 1 // svPtr->LineNumber = LN-1;

    if (svPtr.NOERR != 0) return // if (svPtr->NOERR) return;

    // fprintf(stderr, "\n\n*** in or near line %d:\n%s\n*** E%d ",
    //          svPtr->LineNumber, &svPtr->pd1[nx+1], i);
    System.err.print("\n\n*** in or near line " + svPtr.LineNumber +
        ":\n" + DafCRuntime.cstringAt(svPtr.pd1, nx + 1) +
        "\n*** E" + i + " ")

    // switch (i) - the vendor error-text table, verbatim
    val msg: String = when (i) {
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
        27 -> "\u0024N error"
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
    }
    System.err.print(msg)
}
