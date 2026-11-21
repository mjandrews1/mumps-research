package doc

// Unit 235 (B8 tranche 2): interp-sql.cpp::<misc> (batch 8, core
// stratum; LOC 50, score 23.6, rank 183; units.csv row 228). The
// sql-btree library file residue recomputed with the study-subset
// chunker this session (physical loc 50 = the roster row: the
// file minus the four rostered bodies embed/btree/rslt_callback/
// sql, plus the three under-min-loc residual bodies that ride the
// misc unit per the tool's rule). Consult H-018 hit-adapt: the
// SQLITE define is live per the reference build config (the same
// ledger seat as the u110/u245 renders); CSTR/OK/BAD are
// file-local defines (no cross-file seat claimed); the Jan 10
// 2026 date line and the SQL BTREE / SQLITE CALLBACK INTERFACE
// section banners ride the header ledger. The file-scope
// unsigned char rsltSpec[STR_MAX] (the result interface) renders
// as the H-004 holder object InterpSqlMiscState; the sigint
// forward declaration names the batch-7 real. Include ledger
// (H-004 single-owner notes): stdio/stdlib/string (arm-wide io
// carriers), mumpsc/defines.h (the Retriever.h-family consts of
// Carriers.kt), mumpsc/btree.h (batch-9 complex row), mumpsc/
// global.h and mumpsc/libmpscpp.h (landed render families),
// mumpsc/keyfix.h (the Stubs.keyfix seat), sqlite3.h (the
// batch-8 complex btree row's C-API edge, not a corpus unit).
//
// Residual bodies below: the three minimal helpers of the file
// (all below the roster min-loc, riding this unit): the SOH-to-
// hash command printer (the printf debug view), and the two
// configure-max guards (index 256, data 512) that banner and
// sigint(100) per the arg convention. lcheck/dcheck keep the
// stderr multi-line banners verbatim. Naming per H-017.

object InterpSqlMiscState {
    // interp-sql.cpp:45 static unsigned char rsltSpec[STR_MAX]
    val rsltSpec = CharArray(STR_MAX)
}

// char * command_print(char * command) - SOH (char 1) becomes '#'
// in the scratch view; the static char xcmd[2048] rides the
// holder-object idiom (H-004, function-static keep).
private object CommandPrintState {
    val xcmd = CharArray(2048)
}

fun commandPrint(command: CharArray): CharArray {
    val xcmd = CommandPrintState.xcmd
    var i = 0
    while (command[i] != NUL) {
        if (command[i] == Char(1)) {
            xcmd[i] = '#'
        } else {
            xcmd[i] = command[i]
        }
        i++
    }
    xcmd[i] = NUL
    return xcmd
}

// void lcheck(char * p, MSV * svPtr) - global-array INDEX guard
// (256, the configure index_size) - banner + sigint(100).
fun lcheck(p: CharArray, svPtr: Msv) {

    if (CstringOps.strlenC(p) > 256) { // index_size filled in by configure

        System.err.print("\n*** Length of global array index exceeds configuration max.\n")
        System.err.print("*** " + CstringOps.cstring(p) + "\n")
        System.err.print("*** In or near line " + svPtr.LineNumber + "\n\n")
        sigint(100)
    }

    return // ok
}

// void dcheck(char * p, MSV * svPtr) - global-array DATA guard
// (512, the configure data_size) - same banner + sigint(100).
fun dcheck(p: CharArray, svPtr: Msv) {

    if (CstringOps.strlenC(p) > 512) { // data_size filled in by configure

        System.err.print("\n*** Length of global array data value exceeds configuration max.\n")
        System.err.print("*** " + CstringOps.cstring(p) + "\n")
        System.err.print("*** In or near line " + svPtr.LineNumber + "\n\n")
        sigint(100)
    }

    return // ok
}
