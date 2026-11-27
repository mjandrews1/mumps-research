package both

// Unit 176 (BOTH B6 T3, complex): mpstime.cpp::main (176 LOC).
// Batch_plan ordinal 176: complex, LOC 176, rank 286. The MUMPS
// Compiler generated timing-benchmark main: the full mpstime.cpp
// driver TU of the reference tree; a seven-line benchmark MUMPS
// program compiled into C (set t to the dollarzd1 clock, store
// ten thousand globals, read them back, write the elapsed time).
//
// Vendor behavior: the intermediate-temps block (catx/_ftmp/for1/
// for2/jmp_buf) folds into the runtime scratch seats (h007; _ftmp
// is dead - dropped h013); the setjmp/longjmp For machinery of the
// two compiler-generated loops is boilerplate with no reachable
// Next/Quit operands (the _i==100 arm never fires; the two loops
// fold to the Kotlin while shapes below - h011 journal); the
// _doStack/_Refs/tpx0 residue and the __label_lookup reseats are
// dead in this chunk (h013). svPtr is the TU-static global already
// seated in BothCRuntime (h016; the vendor NULL guard never fires
// on the sealed carrier). The console default: vendor seats
// in_file[5]=stdin, out_file[5]=stdout; the render folds the
// io=5 console channel to the System.in/System.out seats (h001;
// the NULL write test below never fires; journaled). UDAT/UKEY
// receive the data.dat/key.dat defaults (stateVector.h:84/83;
// h004). The percent family: dollar-SymPut seats percent = argv[0]
// (the macro, libmpscpp.h:98) and the loop seats percent-0 through
// percent-(argc-1) with the sprintf percent-d fold (h009). The
// program sets t = dollar-zd1 (the zfcn stub of roster 184 times
// it; the timing core is EXEMPT - the ZFN family body), stores
// ^a(i) = i for i in 1..10000 through Mglobal STORE (the T2
// SQLITE-routing seat), reads them back into k, writes
// dollar-zd1 - t with the hor/ver bookkeeping, and the epilogue
// closes via Mglobal(CLOSE) + Mltoa(getpid())/cleanLocks (the
// LockDone guard). The catch(...) ladder mirrors the
// GlobalExceptionCode family with Mglobal(CLOSE) on the error
// path; the ladder never re-throws - the prints are the exact
// vendor shapes (including the Interprerer typo).
//
// Quirks kept (ledgered):
//   - the if(0) alarm block is dead (h013; the SIGALRM pair never
//     arms - the SyncClock rollback is journaled).
//   - dollar-zd1 seats through zfcn stub (roster 184); the z
//     function body is EXEMPT.
//   - strxcat is the file-local inline helper of THIS TU
//     (mpstime.cpp:102) - rendered REAL below (same-file
//     rationale of the keyfix precedent); strcatz is the
//     inline.h:42 seat (rendered REAL); numcomp2 (the floatstring
//     gate over atof/atoi) is the same-TU inline at mpstime.cpp:97
//     - rendered REAL below (its call sites live in the excluded
//     zfcn family; the seat is the TU-family completeness note).
//   - gtmp[0] stays stale (the vendor strcpy writes &gtmp[1]; the
//     [1]-offset shape kept - h020 scratch journal).
//   - the for-init store (sym_ "1") precedes each loop; the
//     increment store rounds through the add seat (roster-67 stub)
//     - the vendor add() string round-trip stores the incremented
//     value; the numcomp bounds test parses the sym_ read. All
//     three route through the roster stubs until batch 10/3 land.
//
// BOTH consults (find_rule before render, events logged):
//   h001 io_stream_param - reused: console stdin/stdout seats.
//   h004 globals_ext - reused: the Msv carrier seats (UDAT/UKEY,
//     hor/ver, io, LockDone, LineNumber; stateVector.h:40-100).
//   h007 scratch_collapse - hit: the temp block folds to the
//     runtime scratch seats; _ftmp and the dead stacks dropped.
//   h011 goto - reused: For1/For2 setjmp machinery folds into
//     the while shapes (no reachable cross-jumps).
//   h013 dead_store - hit: the alarm block, dead stacks/refs
//     residue, and _ftmp dropped.
//   h014 out_of_scope_call - reused: zfcn stub (roster 184),
//     sym_ stub (owner interp-sym.cpp::sym_, roster 280, batch
//     10), numcomp stub (roster 13, batch 1), add/sub stubs
//     (owner interp-arith.cpp residue family, roster 67, batch
//     3), VariableNotFound/VariableNotFound1 stubs (the
//     interp-sysfunc.cpp:230/241 riders; not units), errorMessage
//     REAL (cppsysfunc.h:31 seat), Mglobal REAL (T2 flip,
//     closes return 1), Mltoa stub (roster 292), cleanLocks stub
//     (libmpscpp.h:215 family), __symput stub (the macro body
//     family - the MumpsMain seat), getpid folds to the
//     ProcessHandle seat, AllocSV stub (roster DOC family; the
//     sealed carrier never routes it here).
//   h015 wrap - hit: this file (ASCII, no templates, no tabs).
//   h016 global_static - reused: the TU-static svPtr singleton.
//   h017 naming - reused: the vendor names kept (sym_ keeps its
//     trailing underscore exactly; the Kotlin keyword-free path
//     of the vendor identifiers).
//   h019 class_member_carrier - reused: the Msv field writes.
//   h020 pointer_handle - reused: CharArray scratch carriers and
//     the Msv CharArray seats (the [1]-offset write fora).

// libmpscpp.h:69-79 error-code family (the catch ladder; the
// collector mirrors the vendor defines - UNSPECIFIED 0 and the
// ladder order exact).
internal const val UNSPECIFIED = 0 // libmpscpp.h:69
internal const val DATA_RANGE_ERROR = 4 // libmpscpp.h:73
internal const val NUMERIC_RANGE = 5 // libmpscpp.h:74
internal const val INPUT_ERROR = 6 // libmpscpp.h:75
internal const val INTERPRETER_ERROR = 8 // libmpscpp.h:77
internal const val SYMTAB_ERROR = 9 // libmpscpp.h:78

internal const val SYMSTORE = 0 // defines.h:200
internal const val SYMGET_NOLBL = 11 // defines.h:202 (the 11 arm)

internal fun mpstimeMain(args: Array<String>): Int {
    val argc = args.size
    val tmp0 = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val tmp2 = CharArray(STR_MAX)
    val tmp3 = CharArray(STR_MAX)
    val atmp = CharArray(STR_MAX)
    val _dummy = CharArray(STR_MAX)
    val gtmp = CharArray(2 * STR_MAX) // vendor: 2*STR_MAX

    // if (svPtr == NULL) svPtr = AllocSV(); - the singleton seat
    // (h016; the guard never fires). _SymPush=0 zap: dead (h013).
    // in_file[5]=stdin / out_file[5]=stdout: the console seats
    // (h001; io defaults to 5 - the channel fold; the arrays keep
    // their nulls; journaled).
    svPtr.io = 5

    writeC(svPtr.UDAT, "data.dat") // strcpy svPtr->UDAT
    writeC(svPtr.UKEY, "key.dat") // strcpy svPtr->UKEY

    // if (0) { signal(SIGALRM,SyncClock); set_alarm(1); } - dead
    // (h013); __label_lookup/__text_function reseats - dead
    // compiler-scaffolding residue (h013).

    __symput("%", args[0]) // SymPut macro seat

    if (argc > 0) {
        for (i in 0 until argc) {
            // sprintf(t,"%%%d",i) - the percent-d doubling fold
            __symput("%" + i.toString(), args[i])
        }
    }

    try {
        //=============================================================
        // LineNumber=4; s t=dollar-zd1
        //=============================================================
        svPtr.LineNumber = 4
        // strcpy(&gtmp[1], dollar-zd1) - the [1]-offset write (gtmp[0]
        // stays stale; the dollar seats through the escape).
        writeAt(gtmp, 1, "\u0024zd1")
        zfcn(gtmp, tmp1, svPtr)
        sym_(SYMSTORE, "t", tmp1, svPtr) // Store result s274

        //=============================================================
        // LineNumber=5; for i=1:1:10000 s ^a(i)=i
        //=============================================================
        svPtr.LineNumber = 5
        // for1: strmove for1_init "1" / for1_incr "1" / for1_lim
        // "10000"; strchr('.') test absent - the atol arm (for1F
        // = 0; the atof arm EXCLUDED); the setjmp body machinery
        // is empty here (h011 fold; no Quit operands reach it).
        writeC(_dummy, "1")
        sym_(SYMSTORE, "i", _dummy, svPtr) // init store 2618
        runForLoop(10000, tmp0, tmp1, tmp2, _dummy)

        //=============================================================
        // LineNumber=6; for i=1:1:10000 s k=^a(i)
        //=============================================================
        svPtr.LineNumber = 6
        writeC(_dummy, "1")
        sym_(SYMSTORE, "i", _dummy, svPtr) // init store 2618
        readForLoop(10000, tmp0, tmp1, tmp2, tmp3, _dummy)

        //=============================================================
        // LineNumber=7; w dollar-zd1-t,!
        //=============================================================
        svPtr.LineNumber = 7
        // if (out_file[io] == NULL) ErrorMessage("Write to input
        // file",...) - the console channel is never NULL (h001
        // quantization; journaled).
        writeAt(gtmp, 1, "\u0024zd1")
        zfcn(gtmp, tmp0, svPtr)
        sym_(SYMGET_NOLBL, "t", tmp1, svPtr)
        sub(tmp0, tmp1, tmp2) // sub(...) seat
        // hor[io] += fprintf(out, "%s", tmp2): the strlen fold of
        // the console print seat.
        val w = strlenC(tmp2)
        System.out.print(cStringOf(tmp2))
        svPtr.hor[svPtr.io] += w
        System.out.print("\n") // the exclamation-mark fold
        svPtr.hor[svPtr.io] = 0 // hor reset; ver increment
        svPtr.ver[svPtr.io]++
    } catch (e: MumpsGlobalException) {
        // catch(...) - the ladder keeps the exact print family
        // (the vendor Interprerer typo kept byte-faithful).
        if (GlobalExceptionCode == INPUT_ERROR) {
            System.out.print("Input Error\n")
        } else if (GlobalExceptionCode == INPUT_LENGTH) {
            System.out.print("Input Length Error\n")
        } else if (GlobalExceptionCode == SYMTAB_ERROR) {
            System.out.print("Symbol Table Error\n")
        } else if (GlobalExceptionCode == GLOBAL_NOT_FOUND) {
            System.out.print("Global Not Found Error\n")
        } else if (GlobalExceptionCode == INTERPRETER_ERROR) {
            System.out.print("Interprerer Error\n")
        } else if (GlobalExceptionCode == INVALID_CONVERSION) {
            System.out.print("Invalid Conversion Error\n")
        } else if (GlobalExceptionCode == DATA_RANGE_ERROR) {
            System.out.print("Data Range Error\n")
        } else if (GlobalExceptionCode == NUMERIC_RANGE) {
            System.out.print("Numeric Range Error\n")
        } else if (GlobalExceptionCode == UNSPECIFIED) {
            System.out.print("Unspecified Error\n")
        }
        System.out.print(
            "Uncaught error in or near line " +
                svPtr.LineNumber + "\n",
        )
        tmp0[0] = 0.toChar()
        tmp1[0] = 0.toChar()
        Mglobal(CLOSE, tmp0, tmp1, svPtr) // Close Globals
        return 0 // the vendor return EXIT_SUCCESS tail
    }

    //=============================================================
    // _epilogue
    //=============================================================
    tmp0[0] = 0.toChar() // tmp0[0]=0; tmp1[0]=0
    tmp1[0] = 0.toChar()
    Mltoa(ProcessHandle.current().pid(), atmp) // Mltoa(getpid(),..)
    if (svPtr.LockDone != 0) cleanLocks(atmp)
    Mglobal(CLOSE, tmp0, tmp1, svPtr)
    return 0 // EXIT_SUCCESS
}

// The two for-loop bodies (LineNumber pinned inside; the vendor
// do-while(0) + longjmp close folds to the single-pass body; the
// loop family below is the driver's own lattice arm - the vendor
// numcomp direction test over the constant limit 10000 with the
// atol arm (forF = 0): the LE walk; the increment rounds through
// add(sym_(11)+ "1") and stores via sym_ - the two roster stubs
// carry the parse; the numcomp bounds ride the driver's own
// 1..10000 seat (the vendor numcomp stub is roster 13/1).
private fun runForLoop(
    limit: Int,
    tmp0: CharArray,
    tmp1: CharArray,
    tmp2: CharArray,
    _dummy: CharArray,
) {
    // the sym_ parse seat of the loop variable (the numcomp test
    // reads it each iteration; the stubs carry the round-trip -
    // the while quantization keeps the LE-direction walk exact).
    var i = 1
    while (i <= limit) {
        svPtr.LineNumber = 5
        // ^a(i): strmove tmp1 "a\xCE"; strxcat(tmp0=i); "\xCF"
        writeC(tmp1, "a\u00CE") // the 0xCE subscript open
        if (sym_(SYMGET_NOLBL, "i", tmp0, svPtr) == null) {
            variableNotFound1(svPtr.LineNumber, "i")
        }
        strxCat(tmp1, tmp0) // catx = strxcat(...)
        strcatz(tmp1, "\u00CF") // the 0xCF subscript close
        if (sym_(SYMGET_NOLBL, "i", tmp2, svPtr) == null) {
            variableNotFound1(svPtr.LineNumber, "i")
        }
        Mglobal(STORE, tmp1, tmp2, svPtr) // f = Mglobal(STORE..)
        // the for-clause: add(sym_(11,"i"), "1", _dummy) then the
        // numcomp(sym_(11) vs lim) <= 0 walk; the add/incr/store
        // triple rounds through the roster stubs.
        i++
        writeC(_dummy, i.toString())
        add(_dummy, tmp1, tmp2) // add(increment arm) seat
        sym_(SYMSTORE, "i", _dummy, svPtr) // store rslt
    }
}

private fun readForLoop(
    limit: Int,
    tmp0: CharArray,
    tmp1: CharArray,
    tmp2: CharArray,
    tmp3: CharArray,
    _dummy: CharArray,
) {
    var i = 1
    while (i <= limit) {
        svPtr.LineNumber = 6
        // ^a(i): strmove tmp2 "^a\x01"; strcatx(tmp2, i)
        writeC(tmp2, "\u005Ea\u0001") // the caret + 0x01 init p674
        if (sym_(SYMGET_NOLBL, "i", tmp1, svPtr) == null) {
            variableNotFound1(svPtr.LineNumber, "i")
        }
        strcatx(tmp2, tmp1)
        if (Mglobal(RETRIEVE, tmp2, tmp3, svPtr) == 0) { // miss
            variableNotFound(svPtr.LineNumber, tmp2)
        }
        sym_(SYMSTORE, "k", tmp3, svPtr) // Store result s274
        i++
        writeC(_dummy, i.toString())
        add(tmp0, tmp1, _dummy) // add(increment arm) seat
        sym_(SYMSTORE, "i", _dummy, svPtr)
    }
}

// char * strxcat(char *, char *) - mpstime.cpp:102, the
// file-local inline of THIS TU: append b at the first NUL of a;
// rendered REAL (same-file rationale of the keyfix precedent).
private fun strxCat(a: CharArray, b: CharArray) {
    var p = 0
    while (a[p] != '\u0000') p++
    var q = 0
    while (b[q] != '\u0000') {
        a[p++] = b[q]
        q++
    }
}

// char * strcatz(char *, char *) - inline.h:42 (the vendor uses
// it through the strcat("\xCF") arm; the inline body rendered
// REAL - the App-C non-unit inline family).
private fun strcatz(out: CharArray, in_: String) {
    var p = 0
    while (out[p] != '\u0000') p++
    for (j in in_.indices) out[p++] = in_[j]
    out[p] = '\u0000'
}

// inline int numcomp2(unsigned char *) - mpstime.cpp:97, the
// file-local inline: the floatstring gate then the atof/atoi
// fold. Its call sites live in the excluded zfcn body family -
// the seat rendered REAL for the TU-family completeness (h014
// journal: same-TU inline; the Modulo floatstringC seat reused).
private fun numcomp2(a: CharArray): Int {
    val s = cStringOf(a)
    if (floatstringC(s) != 0) {
        return if (strtodC(s) != 0.0) 1 else 0
    }
    return atolC(s).toInt()
}

// char * sym_(int, unsigned char *, unsigned char *, struct MSV *)
// - include/mumpsc/sym.h (SYMSTORE 0 / SYMGET 1 / SYMGET_NOLBL 11;
// defines.h:200-202). Owner: interp-sym.cpp::sym_, roster unit
// 280 (batch 10, pending). TODO-stub (h014).
internal fun sym_(
    op: Int,
    name: String,
    data: CharArray,
    sv: Msv,
): CharArray? {
    TODO("stub: owner interp-sym.cpp::sym_ (roster 307, batch 10 - the old pin said roster 280, C-015)")
}

// unsigned char * VariableNotFound(long, char *) /
// VariableNotFound1(long, unsigned char *) - the interp-sysfunc
// riders at interp-sysfunc.cpp:230/241 (the cppsysfunc.h:30
// family; not units). TODO-stubs (h014); both print the
// ErrorMessage line and sigint(100)-abort in the vendor.
internal fun variableNotFound(lineNumber: Long, key: CharArray): CharArray? {
    TODO("stub: vendor VariableNotFound, interp-sysfunc.cpp:230 rider (not a corpus unit)")
}

internal fun variableNotFound1(lineNumber: Long, name: String): CharArray? {
    TODO("stub: vendor VariableNotFound1, interp-sysfunc.cpp:241 rider (not a corpus unit)")
}

// void add(const char *, const char *, char *) / void sub(...) -
// interp-arith.cpp:624/658 (the numSetup.h macro bodies of the
// operator family). Owner: the interp-arith.cpp residue, roster
// unit 67 (batch 3, pending). TODO-stubs (h014).
internal fun add(a: CharArray, b: CharArray, c: CharArray) {
    TODO("stub: owner interp-arith.cpp::add (residue, non-roster - the old pin said roster-67, C-015)")
}

internal fun sub(a: CharArray, b: CharArray, c: CharArray) {
    TODO("stub: owner interp-arith.cpp::sub (residue, non-roster - the old pin said roster-67, C-015)")
}

// strcpy at offset: the &gtmp[1] write shape (h020; the stale
// leading byte kept).
private fun writeAt(dst: CharArray, off: Int, src: String) {
    for (j in src.indices) dst[off + j] = src[j]
    dst[off + src.length] = '\u0000'
}
