package doc

// Unit 82 (B3 tranche 2): libmpscpp.cpp::RunMumps (batch 3, core
// stratum; LOC 33, score 25.9). The embedded-MUMPS call bridge: binds
// the caller's arguments into the symbol table in key order, runs
// the expression through the interpreter with NOERR armed, releases
// the bound symbols, and returns the interpreter error code via
// svPtr->ERROR (new carrier seat, stateVector.h:69). The varargs
// tail (char * key, ...) renders as the Kotlin vararg of nullable
// strings (the vendor walks the key comma-tokens pairing each with
// va_arg) ; the va_list re-arm of the vendor (va_start for the
// release pass) renders as the same token walk. sym_ rides the
// existing H-014 Stubs.sym_ seat with opcode 0 store / 1 release
// (the vendor (unsigned char*) casts are the CharArray token copies
// of the arm call-site idiom, Input.kt precedent; the name/rslt
// buffers use toCharArray copies at the seat contract). Interpret
// rides the Stubs.Interpret seat (owner named there). Vendor quirks
// kept and noted: the argument count j = 1 + number of commas in the
// key can EXCEED the actual vararg count - the vendor walks i<j and
// breaks on the NULL sentinel read past the vararg tail (standard C
// va_arg UB coerced into a sentinel by the caller passing NULL; the
// render breaks on running past the vararg array with this ledger
// note); the SECOND strtok pass tokenizes with the SPACE delimiter
// ("strtok(NULL, \" \")") instead of the comma of the first pass -
// over an already-comma-tokenized buffer the two delimiters yield
// the SAME token sequence, so the shape is preserved by walking the
// same tokens (the delimiter divergence is asserted by the vendor's
// own double pass, kept in shape and ledgered here); the stray
// strcpy(tmp,exp) into the team STR_MAX local is the Interp idiom of
// the arm (tmp CharArray fold, H-007). Naming per H-017.

fun RunMumps(svPtr: Msv, exp: String, key: String, vararg args: String?): Int {
    var j = 1
    for (ch in key) if (ch == ',') j++

    // strtok(tmp, ","): the comma-token walk (both passes see the
    // same sequence - see ledger note on the second-pass delimiter).
    val keyTokens = key.split(",")

    for (i in 0 until j) {
        if (i >= keyTokens.size) break
        val x = if (i < args.size) args[i] else null

        if (x == null) break

        Stubs.sym_(0, keyTokens[i].toCharArray(), x.toCharArray(), svPtr)
    }

    svPtr.NOERR = 1
    val k = Stubs.Interpret(exp, svPtr)
    svPtr.ERROR = k

    // release pass: sym_(1, name, rslt) over the same tokens
    for (i in 0 until j) {
        if (i >= keyTokens.size) break
        val x = if (i < args.size) args[i] else null

        if (x == null) break

        Stubs.sym_(1, keyTokens[i].toCharArray(), x.toCharArray(), svPtr)
    }

    return k
}
