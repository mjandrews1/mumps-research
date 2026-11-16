package mumps.interp

/**
 * PRO-arm translation of interp-parse.cpp::prnterr (pilot B2, unit
 * 58, provisional under deviation C-003). Translated independently
 * from the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 1660-1830 (units.csv:
 * 127 LOC, score 83.1, rank 264).
 *
 * The interpreter error reporter: locates the line containing the
 * parse cursor by walking pd1 (the NUL-separated program source
 * buffer), stores the line number, and prints the E-code message
 * to stderr unless NOERR is set.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 io_primitives     : fprintf(stderr, ...) maps to
 *    System.err.print with the same embedded newlines; the %d/%s/%c
 *    conversions render as string concatenation.
 *  - h004 globals_ext       : pd1 (stateVector.h:43, PS1=32001 -
 *    PS=32000 per sysparms.h:74), NOERR (:60) and ErrChar (:95) are
 *    added to the Msv carrier (one-owner policy; LineNumber already
 *    present from B1 unit 25).
 *  - h011 control_flow       : the 38-arm error switch renders as a
 *    Kotlin when, arms and default kept in the vendor order (note
 *    the vendor gap: cases 21, 22, 28 are absent from the roster).
 *  - h012 unsigned_byte_domain: pd1 is a Char(0..255) byte domain.
 *  - h017 naming             : prnterr keeps its name.
 *
 * Vendor quirks kept faithfully:
 *  - the line-count loop counts the NUL terminator of the CURRENT
 *    line before breaking at n > pd1cur, so the reported number is
 *    LN-1 (kept verbatim: svPtr->LineNumber = LN-1).
 *  - the double NUL walk (down to the previous line's terminator,
 *    then once more if nx > 1) makes &pd1[nx+1] the START of the
 *    current line's text; kept as the cstringAt(pd1, nx+1) read.
 */

// sysparms.h:74 - program source scan bound.
private const val PS = 32000

fun prnterr(i: Int, svPtr: Msv, pd1cur: Int) {

    var n = 1
    var LN = 1

    while (n < PS) {
        if (svPtr.pd1[n] == NUL) LN++
        if (n > pd1cur) break
        n++
    }

    var nx = if (svPtr.pd1[n] != NUL) n else n - 1

    while (svPtr.pd1[nx] != NUL && nx > 0) nx--

    if (nx > 1) {
        nx--
        while (svPtr.pd1[nx] != NUL && nx > 0) nx--
    }

    svPtr.LineNumber = (LN - 1).toLong()

    if (svPtr.NOERR != 0) return

    val err = System.err
    err.print("\n\n*** in or near line " + svPtr.LineNumber + ":\n" +
        cstringAt(svPtr.pd1, nx + 1) + "\n*** E" + i + " ")

    when (i) {
        1 -> err.print("Multiple adjacent operators")
        2 -> err.print("Unmatched quotes")
        3 -> err.print("Global not found")
        4 -> err.print("Missing comma")
        5 -> err.print("Argument not permitted")
        6 -> err.print("Bad character after post-conditional")
        7 -> err.print("Invalid quote")
        8 -> err.print("label not found:")
        9 -> err.print("Too many/few fcn arguments")
        10 -> err.print("Invalid number")
        11 -> err.print("Missing operator")
        12 -> err.print("Unrecognized operator: " + svPtr.ErrChar)
        13 -> err.print("Keyword")
        14 -> err.print("Argument list")
        15 -> err.print("Divide by zero")
        16 -> err.print("Invalid expression")
        17 -> err.print("Variable not found")
        18 -> err.print("Invalid reference")
        19 -> err.print("Logical table space overflow")
        20 -> err.print("I/O error")
        23 -> err.print("Symbol table full")
        24 -> err.print("Function argument error")
        25 -> err.print("Global not permitted")
        26 -> err.print("File error")
        27 -> err.print(36.toChar() + "N error") // dollar byte rebuilt (u34 precedent)
        29 -> err.print("<break> at line:")
        30 -> err.print("Function not found")
        31 -> err.print("Program space exceeded")
        32 -> err.print("Stack overflow")
        33 -> err.print("SQL error")
        34 -> err.print("Arg count mis-match")
        35 -> err.print("String length too long")
        36 -> err.print("Use unit not open")
        37 -> err.print("Unrecognized statement")
        else -> err.print("Unknown error")
    }

    err.print("\n\n")
}
