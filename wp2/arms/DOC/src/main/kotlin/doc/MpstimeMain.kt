package doc

// Unit 182 (B6 tranche 3): mpstime.cpp::main (batch 6, complex
// stratum; roster row 182: loc 176, score 170.4, batch_plan strata
// order). The mumps2c-GENERATED timing benchmark - the compiled
// mpstime.mps program (the set-t-equals-zd1 seed, two
// for-i-equals-1:1:10000 loops writing and reading back the era
// global, and the write of the zd1-minus-t elapsed time). One
// main per package: the mumps.cpp::main render main150 stays
// the interpreter entry; this render is the entry mpstimeMain
// (the main150 name-in-header precedent). Seat table: sym_ and
// zfcn ride the Stubs seats (generated-call surface; sym_ the
// symbol-routing interface, zfcn the batch-6/-10 big-three
// owner), Mglobal is the REAL unit-167 render (STORE and
// RETRIEVE and the CLOSE epilogue), numcomp is the real
// Numcomp.kt render, strcatx is the real inline.h:46 append
// (Input.kt, with its x01-delimiter write-back), SymPut(String,
// String) is the real SymOpsMisc overload, cleanLocks is the
// real LockMisc render, ErrorMessage the Stubs delegate,
// Mltoa the Stubs seat; getpid renders as the ProcessHandle
// JVM intrinsic (the Fcn.kt H-010 precedent). Adaptations and
// drops per the linearized-setjmp precedent (unit 95 / the
// main150 alarm-block shape): the generated setjmp/longjmp FOR
// mechanism renders as plain Kotlin while loops - the
// For1/For1Exit label pair and the _i code-100 exit ladder
// fold into the loop condition and increment; the regular
// for(...) header executes sym_ store, the numcomp limit test
// (for1F direction ternary kept), then add and sym_ store as
// the increment - all at their vendor positions. h013: the
// unreachable generated prologue temps and globals are
// dropped with notes (_ftmp, _ReturnVal, _ArgCount, the
// _doStack/_doStackN/_doStx <do> stack, _Refs/_Refsx, the
// _label_lookup/_text_function pointer pair, the dead locals
// _jj/_j/_k/_new/g and the unused _SymPush/_epilogue temps);
// the if(0) alarm block is dead guard text kept as the
// comment ledger (the main150 precedent). The xce/xcf/x01
// key encodings are kept verbatim with their M3701/a2926/
// a3956/p674 vendor comment markers; the zd1 intrinsic names
// carry the sigil via the DOLLAR const (the emitted-C sigil
// carrier precedent, H-015). The catch(...) ladder renders as
// the Throwable catch on GlobalExceptionCode with the vendor
// code strings verbatim, including the vendor Interprerer
// typo. Console defaults: lun 5 binds the STDIN_READER
// carrier (the Getstr2 unit-48 reflective-fetch precedent)
// and the stdout Writer. Naming per H-017.

// defines.h:200/201/202 sym_ opcodes (generated call surface)
private const val SYMSTORE_MP = 0
private const val SYMGET_MP = 1
private const val SYMGET_NOLBL_MP = 11

// libmpscpp.h:69-79 catch-ladder codes not already in Carriers
private const val NUMERIC_RANGE_MP = 5 // libmpscpp.h:76
private const val INPUT_LENGTH_MP = 10 // libmpscpp.h:79
private const val UNSPECIFIED_MP = 0 // libmpscpp.h:70

// the raw-sigil carrier for the intrinsic names (H-015)
private const val DOLLAR_MP = 36.toChar()

fun mpstimeMain(argv: Array<String>): Int {
    val svPtr = Globals.svPtr
    val argc = argv.size

    // intermediate temporaries; the addressed-only _ftmp and
    // other dead prologue temps are dropped (h013, header note)

    // FORPARM: the generated program re-defines FORPARM to
    // STR_MAX (the compiler-mumps.c:141 quirk ledgers) - the
    // for1/for2 control arrays carry at STR_MAX
    val for1Incr = CharArray(STR_MAX)
    val for1Lim = CharArray(STR_MAX)
    val for1Init = CharArray(STR_MAX)
    val for2Incr = CharArray(STR_MAX)
    val for2Lim = CharArray(STR_MAX)
    val for2Init = CharArray(STR_MAX)

    val tmp0 = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val tmp2 = CharArray(STR_MAX)
    val tmp3 = CharArray(STR_MAX)
    val dummy = CharArray(STR_MAX) // _dummy
    val atmp = CharArray(STR_MAX)
    val gtmp = CharArray(2 * STR_MAX) // 2*STR_MAX sizing kept
    val tmp = CharArray(STR_MAX)

    // if ( 0) { // no alarm if slice is zero
    //     signal(SIGALRM, SyncClock);
    //     set_alarm(1);
    //     }
    // - dead guard block kept as the vendor comment ledger
    // (h013, the main150 precedent); the __label_lookup /
    // __text_function pointer assignments drop the same way

    // zap pushed tables
    svPtr._SymPush = 0

    // console defaults: lun 5 (the STDIN_READER carrier binds;
    // the stdout Writer is the fresh OutputStreamWriter)
    svPtr.in_file[5] = STDIN_READER
    svPtr.out_file[5] = java.io.OutputStreamWriter(System.out)

    CstringOps.strcpyS(svPtr.UDAT, "data.dat")
    CstringOps.strcpyS(svPtr.UKEY, "key.dat")

    SymPut("%", argv[0])

    if (argc > 0) {
        for (i in 0 until argc) {
            // sprintf(t, "%d-shaped", i): the percent-d fold
            SymPut("%" + i, argv[i])
        }
    }

    try {
        //=========================================================
        svPtr.LineNumber = 4L // s t=zd1
        //=========================================================

        // -------------- Set --------------
        // the gtmp key carries the leading x01 slot; the zd1
        // name rides the DOLLAR carrier
        gtmp[0] = CHAR_1
        gtmpAt(gtmp, "" + DOLLAR_MP + "zd1")
        Stubs.zfcn(gtmp, tmp1, svPtr)
        Stubs.sym_(SYMSTORE_MP, "t".toCharArray(), tmp1, svPtr) // s274

        //=========================================================
        svPtr.LineNumber = 5L // for i=1:1:10000 s era(i)=i
        //=========================================================

        // -------------- For 1 --------------
        CstringOps.strcpyS(for1Lim, "")
        CstringOps.strcpyS(for1Init, "1") // init value m2534
        CstringOps.strcpyS(for1Incr, "1") // inc/dec m2575
        CstringOps.strcpyS(for1Lim, "10000") // limit value

        // direction: the for1F ternary kept (float vs int test)
        val for1F = if (strchrC(for1Incr, '.')) {
            if (CstringOps.cstring(for1Incr).toDouble() < 0.0) 1 else 0
        } else {
            if (atolC(for1Incr) < 0) 1 else 0
        }

        Stubs.sym_(SYMSTORE_MP, "i".toCharArray(), for1Init, svPtr) // init 2618

        while (if (for1F != 0) {
            numcomp(symGetNolbl("i", dummy, svPtr), for1Lim) >= 0
        } else {
            numcomp(symGetNolbl("i", dummy, svPtr), for1Lim) <= 0 // LE limit
        }) {
            // For1: the loop body (the setjmp/longjmp pair
            // linearized into this while shape)
            svPtr.LineNumber = 5L

            // -------------- Set --------------
            CstringOps.strcpyS(tmp1, "a\u00CE") // M3701
            if (Stubs.sym_(SYMGET_MP, "i".toCharArray(), tmp0, svPtr) == null) {
                variableNotFound1(svPtr.LineNumber, "i".toCharArray())
            }
            val catx = strxcatMp(tmp1, tmp0) // a2926
            strcatChar(catx, '\u00CF') // a3956

            if (Stubs.sym_(SYMGET_MP, "i".toCharArray(), tmp2, svPtr) == null) {
                variableNotFound1(svPtr.LineNumber, "i".toCharArray())
            }
            Mglobal(STORE, tmp1, tmp2, svPtr)

            // the increment trio at the vendor position: add
            // the loop var, store the rslt
            Stubs.add(symGetNolbl("i", dummy, svPtr), for1Incr, dummy)
            Stubs.sym_(SYMSTORE_MP, "i".toCharArray(), dummy, svPtr)
        }
        // For1Exit: exit the for loop (linearized; the f273
        // label ledger folds into the while condition)

        //=========================================================
        svPtr.LineNumber = 6L // for i=1:1:10000 s k=^a(i)
        //=========================================================

        // -------------- For 2 --------------
        CstringOps.strcpyS(for2Lim, "")
        CstringOps.strcpyS(for2Init, "1") // init value m2534
        CstringOps.strcpyS(for2Incr, "1") // inc/dec m2575
        CstringOps.strcpyS(for2Lim, "10000") // limit value

        val for2F = if (strchrC(for2Incr, '.')) {
            if (CstringOps.cstring(for2Incr).toDouble() < 0.0) 1 else 0
        } else {
            if (atolC(for2Incr) < 0) 1 else 0
        }

        Stubs.sym_(SYMSTORE_MP, "i".toCharArray(), for2Init, svPtr) // init 2618

        while (if (for2F != 0) {
            numcomp(symGetNolbl("i", dummy, svPtr), for2Lim) >= 0
        } else {
            numcomp(symGetNolbl("i", dummy, svPtr), for2Lim) <= 0 // LE limit
        }) {
            // For2: the loop body (linearized as above)
            svPtr.LineNumber = 6L

            // -------------- Set --------------
            CstringOps.strcpyS(tmp2, "^a\u0001") // init p674
            if (Stubs.sym_(SYMGET_MP, "i".toCharArray(), tmp1, svPtr) == null) {
                variableNotFound1(svPtr.LineNumber, "i".toCharArray())
            }
            strcatx(tmp2, tmp1)
            if (Mglobal(RETRIEVE, tmp2, tmp3, svPtr) == 0) { // retrieve global
                variableNotFound(svPtr.LineNumber, tmp2)
            }
            Stubs.sym_(SYMSTORE_MP, "k".toCharArray(), tmp3, svPtr) // s274

            Stubs.add(symGetNolbl("i", dummy, svPtr), for2Incr, dummy)
            Stubs.sym_(SYMSTORE_MP, "i".toCharArray(), dummy, svPtr)
        }
        // For2Exit: exit the for loop (linearized)

        //=========================================================
        svPtr.LineNumber = 7L // w zd1-minus-t,!
        //=========================================================

        // -------------- Write --------------
        if (svPtr.out_file[svPtr.io] == null) {
            Stubs.ErrorMessage("Write to input file", svPtr.LineNumber) // m1944
        }
        val out5 = svPtr.out_file[svPtr.io]
                ?: error("fprintf on the NULL out_file (vendor path)")

        val gtmp2 = CharArray(2 * STR_MAX)
        gtmp2[0] = CHAR_1
        gtmpAt(gtmp2, "" + DOLLAR_MP + "zd1")
        Stubs.zfcn(gtmp2, tmp0, svPtr)
        if (Stubs.sym_(SYMGET_MP, "t".toCharArray(), tmp1, svPtr) == null) {
            variableNotFound1(svPtr.LineNumber, "t".toCharArray())
        }
        Stubs.sub(tmp0, tmp1, tmp2)
        out5.write(CstringOps.cstring(tmp2))
        svPtr.hor[svPtr.io] += CstringOps.cstring(tmp2).length
        out5.write("\n")
        svPtr.hor[svPtr.io] = 0
        svPtr.ver[svPtr.io]++

        //=========================================================
        // _epilogue: Function Epilogue
        tmp0[0] = NUL
        tmp1[0] = NUL
        Stubs.Mltoa(java.lang.ProcessHandle.current().pid().toInt(), atmp)
        if (svPtr.LockDone != 0) {
            cleanLocks(CstringOps.cstring(atmp))
        }
        // Close Globals
        Mglobal(CLOSE_MTIME, tmp0, tmp1, svPtr)

    } // end of the try block
    catch (e: Throwable) {
        val code = Globals.GlobalExceptionCode
        if (code == INPUT_ERROR) println("Input Error")
        else if (code == INPUT_LENGTH_MP) println("Input Length Error")
        else if (code == SYMTAB_ERROR) println("Symbol Table Error")
        else if (code == GLOBAL_NOT_FOUND) println("Global Not Found Error")
        else if (code == INTERPRETER_ERROR) println("Interprerer Error")
        else if (code == INVALID_CONVERSION) println("Invalid Conversion Error")
        else if (code == DATA_RANGE_ERROR) println("Data Range Error")
        else if (code == NUMERIC_RANGE_MP) println("Numeric Range Error")
        else if (code == UNSPECIFIED_MP) println("Unspecified Error")
        println("Uncaught error in or near line " + svPtr.LineNumber)
        // Close Globals
        Mglobal(CLOSE_MTIME, tmp0, tmp1, svPtr)
    }

    return 0 // EXIT_SUCCESS
}

// defines.h:87 CLOSE (the GlobalClose opcode; the cross-armed
// CLOSE members note applies, main150 precedent)
private const val CLOSE_MTIME = 4

// sym_(11, name, rslt, svPtr) with the null-carrier fold: the
// vendor passes the pointer straight to numcomp (its NULL case
// is ungated generated text - quantized with the error carrier,
// the u137 symNullCarrier precedent)
private fun symGetNolbl(name: String, rslt: CharArray, svPtr: Msv): CharArray =
        Stubs.sym_(SYMGET_NOLBL_MP, name.toCharArray(), rslt, svPtr)
                ?: error("sym_ SYMGET_NOLBL NULL (generated cond path)")

// strxcat (mpstime.cpp:102 static weak cat helper; batch-9 misc
// row 253 owner - process-level helper, per-file carrier)
private fun strxcatMp(dst: CharArray, src: CharArray): CharArray =
        throw NotImplementedError("strxcat: out-of-scope owner (mpstime.cpp:102) batch-9 unit")

// the miss reporters REUSE the real SysfuncMisc seats
// (variableNotFound / variableNotFound1, H-004 - no
// redeclaration; called straight in the bodies above)

// strchr(buf, c): the point-test fold over the NUL-terminated
// region (the atof/atol direction test support)
private fun strchrC(buf: CharArray, c: Char): Boolean {
    var k = 0
    while (k < buf.size && buf[k] != NUL) {
        if (buf[k] == c) return true
        k++
    }
    return false
}

// atol fold (the atoi prefix-scan contract)
private fun atolC(buf: CharArray): Long {
    val s = CstringOps.cstring(buf).trim()
    val m = Regex("^[-+]?[0-9]+").find(s) ?: return 0L
    return (m.value.toLongOrNull() ?: 0L)
}

// strcat(catx, "\xcf"): the single-char append at the NUL
private fun strcatChar(dst: CharArray, c: Char) {
    val n = CstringOps.strlenC(dst)
    dst[n] = c
    dst[n + 1] = NUL
}

// strcpy(&gtmp[1], s): the one-slot-offset write (the x01
// prefix key shape of the generated zfcn calls)
private fun gtmpAt(gtmp: CharArray, s: String) {
    for (k in s.indices) gtmp[1 + k] = s[k]
    gtmp[1 + s.length] = NUL
}
