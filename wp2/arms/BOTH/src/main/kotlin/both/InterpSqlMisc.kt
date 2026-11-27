package both

// Unit 227 render: interp-sql.cpp::<misc> (roster 227, batch 8,
// 110 LOC dump; the TU head before the btree giant, record
// 216). The interp-sql.cpp file-scope residue: the SQLITE build
// defines, the include ledger, the rsltSpec/call_back TU
// statics (VERIFICATION FOLDS - both already seated in the
// SqlRuntime object, RsltCallback.kt:50-51, the :69/:886 pins;
// verified line-for-line, no new seats), the sql() prototype,
// and the three helpers command_print/lcheck/dcheck.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h004 include_only - reused: the banner, the
//     #define SQLITE/CSTR/OK/BAD block, the includes and the
//     section-comment ledgers ride this KDoc (no seats); the
//     sql() prototype is the record 216 btree-family pin
//     source.
//   h007 char_buffer - reused: command_print's static
//     xcmd[2048] rides a file-private CharArray (the static
//     last-value shape kept).
//   h014 out_of_scope_call - reused: sigint(100) direct (the
//     in-band abort, both checkers); the stderr fprintfs ride
//     System.err (the h001 io edge, the vendor stderr channel
//     kept).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: command_print/lcheck/dcheck keep
//     vendor names (no Kotlin collisions); rsltSpec/call_back
//     cite the SqlRuntime seats (the callBack spelling note).
//   h020 pointer_handle - reused: the sqlite3.h include and
//     the CSTR cast macro are platform edges consumed by the
//     record 216 render (ledgered, no seats).
//
// Quirks kept (ledgered):
//   - command_print maps ONLY the SOH byte (1) to '#'
//     (everything else verbatim; no other quoting - kept).
//   - lcheck's threshold is 256 (index_size) and dcheck's is
//     512 (data_size), both "filled in by configure" comments
//     kept.
//   - the two checkers print THREE stderr lines (the banner,
//     the offending value, the line number) before sigint(100)
//     - kept exact.
//   - command_print's xcmd[2048] has NO bounds check in the
//     vendor (a 2048+ command overflows); the seat clamps at
//     2048 (UB quantization family).

private val xcmdIsm = CharArray(2048) // static char xcmd[2048]

// char * command_print (char * command) - interp-sql.cpp
internal fun command_print(command: CharArray): CharArray {
    var i = 0
    while (command[i] != '\u0000' && i < 2048) {
        xcmdIsm[i] = if (command[i] == '\u0001') '#' else command[i]
        i++
    }
    xcmdIsm[i] = '\u0000'
    return xcmdIsm
}

// void lcheck(char * p, MSV * svPtr) - the index-length check.
internal fun lcheck(p: CharArray, svPtr: Msv) {
    val s = cStringOf(p)
    if (s.length > 256) { // index_size filled in by configure
        System.err.print(
            "\n*** Length of global array index exceeds configuration max.\n"
        )
        System.err.print("*** " + s + "\n")
        System.err.print(
            "*** In or near line " + svPtr.LineNumber + "\n\n"
        )
        sigint(100)
    }
    return // ok
}

// void dcheck(char * p, MSV * svPtr) - the data-length check.
internal fun dcheck(p: CharArray, svPtr: Msv) {
    val s = cStringOf(p)
    if (s.length > 512) { // data_size filled in by configure
        System.err.print(
            "\n*** Length of global array data value exceeds configuration max.\n"
        )
        System.err.print("*** " + s + "\n")
        System.err.print(
            "*** In or near line " + svPtr.LineNumber + "\n\n"
        )
        sigint(100)
    }
    return // ok
}
