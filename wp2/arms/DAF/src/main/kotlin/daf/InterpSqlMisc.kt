package daf

// Unit 235 (DAF B8 T2, core): interp-sql.cpp::<misc>
// (50 LOC, score 23.6): the sql-btree library file residue
// recomputed with the study-subset chunker this session (physical
// loc 50 = the roster row: the file minus the four rostered bodies
// embed/btree/rslt_callback/sql, plus the three under-min-loc
// residual bodies that ride the misc unit per the tool's rule;
// vendor file head physically verified this session).
// Consults (2026-11-24T21:19:30Z, nominal): the SQLITE define is
// live per the reference build config (the same ledger seat as
// the unit-110/113 renders); CSTR/OK/BAD are file-local defines
// (no cross-file seat claimed); the Jan 10 2026 date line and the
// SQL BTREE / SQLITE CALLBACK INTERFACE section banners ride the
// header ledger. The file-scope unsigned char rsltSpec[STR_MAX]
// (the result interface) and the static int call_back latch are
// ALREADY SEATED at RsltCallback.kt (the unit-202 render:
// rsltSpecD/callBackD, the rows 6/22 object-state discipline) - no
// new holder opens; the sigint forward declaration names the
// unit-211 real. Include ledger (single-owner notes): stdio/
// stdlib/string (the DafCRuntime io family), mumpsc/defines.h
// (the Retriever consts), mumpsc/btree.h (the btree row of THIS
// batch, unit 246), mumpsc/global.h and mumpsc/libmpscpp.h (the
// landed render families), mumpsc/keyfix.h (the keyfixDaf seat),
// sqlite3.h (the btree C-API edge, not a corpus unit).
//
// Residual bodies below: the three minimal helpers of the file
// (all below the roster min-loc, riding this unit): the SOH-to-
// hash command printer (the debug view; the static char
// xcmd[2048] return folds to a fresh String per the row-56
// static-return-buffer precedent of the u130 render), and the
// two configure-max guards (index 256, data 512) that banner and
// sigintD(100) per the arg convention; lcheck/dcheck keep the
// stderr multi-line banners verbatim (the u139 System.err
// precedent).
// Dafny: EXEMPT (residual helpers over the sigint seat; journal
// row).

// char * command_print(char * command) - SOH (char 1) becomes '#'
fun commandPrintD(command: CharArray): String {
    val xcmd = CharArray(2048) // static char xcmd[2048] (row-56 fold)
    var i = 0
    while (command[i] != NUL_D) {
        if (command[i] == Char(1)) {
            xcmd[i] = '#'
        } else {
            xcmd[i] = command[i]
        }
        i++
    }
    xcmd[i] = NUL_D // xcmd[i] = 0
    return DafCRuntime.cstring(xcmd)
}

// void lcheck(char * p, MSV * svPtr) - global array index length
// check (index_size 256 filled in by configure)
fun lcheckD(p: CharArray, svPtr: DafMsv) {

    // if (strlen(p) > 256)
    var n = 0
    while (p[n] != NUL_D) n++
    if (n > 256) {
        System.err.print("\n*** Length of global array index exceeds configuration max.\n")
        System.err.print("*** " + DafCRuntime.cstring(p) + "\n")
        System.err.print("*** In or near line " + svPtr.LineNumber + "\n\n")
        sigintD(100)
    }

    return // ok
}

// void dcheck(char * p, MSV * svPtr) - global array data length
// check (data_size 512 filled in by configure)
fun dcheckD(p: CharArray, svPtr: DafMsv) {

    var n = 0
    while (p[n] != NUL_D) n++ // if (strlen(p) > 512)
    if (n > 512) {
        System.err.print("\n*** Length of global array data value exceeds configuration max.\n")
        System.err.print("*** " + DafCRuntime.cstring(p) + "\n")
        System.err.print("*** In or near line " + svPtr.LineNumber + "\n\n")
        sigintD(100)
    }

    return // ok
}
