package daf

// Unit 182 (DAF B6 T3, complex): mpstime.cpp::main (units.csv row:
// loc 176, complex): the compiler-generated benchmark entry point -
// a runtime-body render of the emitted-C output for the benchmark
// program (s t=\u0024ZD1 for i=1:1:10000 s ^a(i)=i for i... s k=^a(i) w
// \u0024ZD1-t,!). This is EXECUTED emitted code, not an emitter: every
// seat below is a RUNTIME seat.
// Consults (2026-11-22T16:23:00Z, nominal): BASE-log rows 6/22
// hit-reuse (the runtime MSV singleton rides DafInterpState.svPtr
// with the if (svPtr == NULL) allocSVDaf console-default fold of
// the MumpsMainD precedent), row 18 hit-reuse (the in_file[5]/
// out_file[5] console-default assignments fold onto IntArray
// handle value 1 at index 5 - the T2 StateVectorHMisc extension
// carriers), row 59 (RAISED named-owner stubs: numcompDaf - owner
// interp-arith.cpp::numcomp at 980, addDaf/subDaf - owner
// interp-arith.cpp::add/sub via arith.h:48/51 - all runtime arith
// seats, non-roster cells; strxcatDaf - owner mpstime.cpp::strxcat
// local inline at 102, the THIS-TU misc cell; zfcnDaf - owner
// interp-zfcns.cpp::zfcn, the roster b6 big-three unit 184 that
// stays queued for the big-three session; variableNotFoundDaf -
// owner interp-sysfunc.cpp::VariableNotFound at 230,
// variableNotFound1Daf - owner interp-sysfunc.cpp::VariableNotFound1
// at 241, both non-roster), row 56 hit-reuse (strmove/strcat fold
// onto carrier copy loops and String concat; the strcatxD seat of
// StrmanipMisc is the hit-reuse append), row 33 hit-reuse (the
// sprintf("%%%d",i) argv loop folds onto String interpolation),
// row 68 hit-reuse (the C++ catch ladder folds onto the Kotlin
// Throwable catch with the GlobalExceptionCodeD singleton ladder).
// VENDOR QUIRKS kept: (1) the setjmp/longjmp for-loop state machine
// (for1J/for2J with the _i == 100 exit signal) folds onto structured
// while loops - the KDoc ledger at each loop head pins the
// for-init/incr/limit triple and the for1F direction ternary;
// (2) argv loop starts at i=0 so \u0024SymPut("%", argv[0]) is
// immediately overwritten by the %0 entry - kept verbatim order;
// (3) the M3701 coded-subscript bytes ride Char literals: \xCE at
// tmp1 tail of the STORE key (a\u00CE fold: the M3701 comment idioms
// stay), the CODED_COMMA_D \u00CF after the catx concat, and the
// \u0001 subscript mark in ^a\u0001 (init p674) - all Int-code
// beyond ASCII, kept as Unicode code points exactly as seated;
// (4) the if (0) alarm slice stays the ALARM_SLICE_ZERO_D
// constant-fold of the MumpsMainD precedent (SyncClock/set_alarm
// bodies non-existent seats); (5) __label_lookup/_text_function
// pointer assignments stay comment-only (label resolution unused
// by this body); (6) _ReturnVal/_ArgCount/_doStack/_Refs decl
// block stays comment-only (unused locals ledger);
// (7) fprintf(out_file[io],"%s",tmp2) folds onto System.out.print
// with the hor/ver cursor updates kept (hor[io]+=len, newline then
// hor=0, ver++); (8) the catch ladder prints the vendor misspelling
// "Interprerer Error" verbatim; (9) the vendor catch arm closes
// globals with tmp0/tmp1 and falls to return EXIT_SUCCESS - the
// Kotlin fold returns EXIT_SUCCESS from the catch arm.
// Dafny: EXEMPT (runtime benchmark harness over the stub seats;
// journal row).

const val SYMGET_NOLBL_D = 11 // defines.h:202 #define SYMGET_NOLBL 11
const val INPUT_ERROR_D = 6 // libmpscpp.h:75
const val INTERPRETER_ERROR_D = 8 // libmpscpp.h:77
const val DATA_RANGE_ERROR_D = 4 // libmpscpp.h:73
const val NUMERIC_RANGE_D = 5 // libmpscpp.h:74
const val UNSPECIFIED_D = 0 // libmpscpp.h:69

// ---- named-owner stub seats (raise; owners ledgered above) ----

// numcompDaf stub RELEASED (R1 stub-completion 2026-11-28,
// tranche 6): real body at Numcomp.kt (roster row 13; the
// Interpret.kt FOR call sites, the MpsSqrt negative guard and
// the MpstimeMain row resolve there).

fun addDaf(a: String, b: String, c: CharArray): Unit =
    throw NotImplementedError(
        "add: out-of-scope owner (interp-arith.cpp::add via arith.h:48; not a roster cell)")

fun subDaf(a: String, b: String, c: CharArray): Unit =
    throw NotImplementedError(
        "sub: out-of-scope owner (interp-arith.cpp::sub at 658 via arith.h:51; not a roster cell)")

// strxcat stub RELEASED batch 9 unit 272 (real body at
// MpstimeMisc.kt; the mpstime.cpp::<misc> roster row owns the
// this-TU inline; the catx call at line 205 above binds
// unchanged).

// SEAT RELEASED (big-three session 2026-11-22): the zfcnDaf stub
// deleted here; the unit-184 render Zfcns.kt owns the vendor C
// symbol zfcn (the call sites above resolve to it - same shape).

fun variableNotFoundDaf(line: Int, name: String): Unit =
    throw NotImplementedError(
        "VariableNotFound: out-of-scope owner (interp-sysfunc.cpp::VariableNotFound at 230; not a roster cell)")

fun variableNotFound1Daf(line: Int, name: String): Unit =
    throw NotImplementedError(
        "VariableNotFound1: out-of-scope owner (interp-sysfunc.cpp::VariableNotFound1 at 241; not a roster cell)")

fun mpstimeMainDaf(args: Array<String>): Int {

    val sv = DafInterpState.svPtr ?: allocSVDaf()

    // char *catx; char *_ftmp; for1_incr/lim/init, for2_incr/lim/init
    // ... the intermediate/temporary decl block - carriers below;
    // the _ReturnVal/_ArgCount/_doStack/_doStackN/_doStx/_Refs decl
    // block stays comment-only (unused locals ledger, quirk (6))

    val tmp0 = CharArray(STR_MAX_DAF)
    val tmp1 = CharArray(STR_MAX_DAF)
    val tmp2 = CharArray(STR_MAX_DAF)
    val tmp3 = CharArray(STR_MAX_DAF)
    val atmp = CharArray(STR_MAX_DAF)
    val gtmp = CharArray(STR_MAX_DAF * 2)
    val dummy = CharArray(STR_MAX_DAF)
    val for1_lim = CharArray(32) // char for1_lim[FORPARM]
    val for1_incr = CharArray(32)
    val for1_init = CharArray(32)
    val for2_lim = CharArray(32)
    val for2_incr = CharArray(32)
    val for2_init = CharArray(32)
    var f = 0 // int f (quint, the STORE result carrier)

    // if (svPtr == NULL) svPtr = AllocSV(); - the MumpsMainD fold
    // _SymPush = 0; // zap pushed tables - the singleton has no
    // pushed-table lattice; ledger note only

    // svPtr->in_file[5]=stdin; svPtr->out_file[5]=stdout;
    // - console default (row 18 IntArray handle fold: value 1)
    sv.in_file[5] = 1
    sv.out_file[5] = 1

    // strcpy(svPtr->UDAT,"data.dat"); strcpy(svPtr->UKEY,"key.dat");
    val udat = "data.dat"
    for (t in udat.indices) sv.UDAT[t] = udat[t]
    val ukey = "key.dat"
    for (t in ukey.indices) sv.UKEY[t] = ukey[t]

    if (ALARM_SLICE_ZERO_D != 0) { // if (0) { // no alarm if slice is zero
        // signal(SIGALRM, SyncClock); set_alarm(1); - constant-false
        // vendor guard, bodies non-existent seats (quirk (4))
    }

    // __label_lookup = _label_lookup; __text_function =
    // _text_function; - comment-only (quirk (5))

    // \u0024SymPut("%",argv[0]);
    SymPutD("%", if (args.isNotEmpty()) args[0] else "")

    // if (argc > 0) { for (int i = 0; i < argc; i++) { sprintf(t,
    // "%%%d", i); \u0024SymPut(t, argv[i]); } }
    for (i in args.indices) { // quirk (2): starts at 0, %0 overwrite
        SymPutD("%" + i, args[i])
    }

    try {

        // LineNumber=4; s t=\u0024zd1
        sv.LineNumber = 4

        // strcpy((char *) &gtmp[1], "\u0024zd1");
        val zd1 = "\$zd1"
        for (t in zd1.indices) gtmp[1 + t] = zd1[t]
        // zfcn(gtmp,tmp1,svPtr);
        zfcnDaf(gtmp, tmp1, sv)
        // sym_(SYMSTORE, "t", tmp1, svPtr); // Store result s274
        symRetDaf(SYMSTORE_D, "t".toCharArray(), 0, tmp1, 0, sv)

        // LineNumber=5; for i=1:1:10000 s ^a(i)=i
        sv.LineNumber = 5

        // strcpy(for1_lim,""); strmove(for1_init,"1"); // m2534
        // strmove(for1_incr,"1"); // m2575; strmove(for1_lim,"10000")
        set32D(for1_lim, "")
        set32D(for1_init, "1")
        set32D(for1_incr, "1")
        set32D(for1_lim, "10000")

        // if (strchr(for1_incr,'.')) for1F = atof(...)<0.0?1:0;
        // else for1F = atol(for1_incr)<0?1:0; - the integer arm is
        // the live one (incr "1"); atof floor raises note avoided;
        // the fold reads atolD (row-28 seat)
        val for1F = if (str32Has(for1_incr, '.')) {
            if (DafCRuntime.strtod(str32(for1_incr)) < 0.0) 1 else 0
        } else {
            if (atolD(str32(for1_incr)) < 0) 1 else 0
        }

        // for ( sym_(SYMSTORE,"i",for1_init,svPtr); ... ) - the
        // setjmp/longjmp for1J state machine folds onto the while
        // loop below (quirk (1)): init 2618, the for1F ternary GE/LE
        // limit test via numcomp, the add() inc/dec, the SYMSTORE
        // rslt store; the _i==100 longjmp exit signal is the
        // structured exit
        symRetDaf(SYMSTORE_D, "i".toCharArray(), 0, for1_init, 0, sv)

        // for1 condition: numcomp(sym_(11,"i",_dummy,svPtr),
        // for1_lim) <= 0 (the LE arm; for1F is 0 here)
        while (numcmpForD(for1F, sv, "i", for1_lim, dummy)) {

            // for1 body: svPtr->LineNumber=5
            sv.LineNumber = 5

            // strmove(tmp1, "a\xce"); // M3701
            setS(tmp1, "a\u00CE")
            // if (sym_(SYMGET,"i",tmp0,svPtr)==NULL)
            //     VariableNotFound1(LineNumber,"i");
            val r_i = symRetDaf(SYMGET_NOLBL_D, "i".toCharArray(), 0,
                    tmp0, 0, sv)
            if (r_i == null) variableNotFound1Daf(sv.LineNumber, "i")
            // catx = strxcat(tmp1, tmp0); // a2926
            val catx = strxcatDaf(tmp1, tmp0)
            // strcat(catx, "\xcf"); //a3956 (CODED_COMMA_D family)
            val catxS = catx + "\u00CF"
            setS(tmp1, catxS)

            // if (sym_(SYMGET,"i",tmp2,svPtr)==NULL)
            //     VariableNotFound1(LineNumber,"i");
            val r_i2 = symRetDaf(SYMGET_NOLBL_D, "i".toCharArray(), 0,
                    tmp2, 0, sv)
            if (r_i2 == null) variableNotFound1Daf(sv.LineNumber, "i")
            // f = Mglobal(STORE, tmp1, tmp2, svPtr);
            f = MglobalDaf(STORE_D, tmp1, tmp2, sv)

            // for1 increment: add(sym_(11,"i",_dummy,svPtr),
            // for1_incr, _dummy), sym_(SYMSTORE,"i",_dummy,svPtr)
            val r_inc = symRetDaf(SYMGET_NOLBL_D, "i".toCharArray(), 0,
                    dummy, 0, sv)
            val incS = r_inc ?: ""
            addDaf(incS, str32(for1_incr), dummy)
            symRetDaf(SYMSTORE_D, "i".toCharArray(), 0, dummy, 0, sv)
        } // FOR loop 1 close (longjmp fold)

        // LineNumber=6; for i=1:1:10000 s k=^a(i)
        sv.LineNumber = 6

        set32D(for2_lim, "")
        set32D(for2_init, "1")
        set32D(for2_incr, "1")
        set32D(for2_lim, "10000")

        val for2F = if (str32Has(for2_incr, '.')) {
            if (DafCRuntime.strtod(str32(for2_incr)) < 0.0) 1 else 0
        } else {
            if (atolD(str32(for2_incr)) < 0) 1 else 0
        }

        symRetDaf(SYMSTORE_D, "i".toCharArray(), 0, for2_init, 0, sv)

        while (numcmpForD(for2F, sv, "i", for2_lim, dummy)) {

            // for2 body: svPtr->LineNumber=6
            sv.LineNumber = 6

            // strmove(tmp2, "^a\x01"); // init p674
            setS(tmp2, "^a\u0001")
            // if (sym_(SYMGET,"i",tmp1,svPtr)==NULL)
            //     VariableNotFound1(LineNumber,"i");
            val r_k = symRetDaf(SYMGET_NOLBL_D, "i".toCharArray(), 0,
                    tmp1, 0, sv)
            if (r_k == null) variableNotFound1Daf(sv.LineNumber, "i")
            // strcatx(tmp2, tmp1);
            strcatxD(tmp2, tmp1)

            val r_k2 = symRetDaf(SYMGET_NOLBL_D, "i".toCharArray(), 0,
                    tmp1, 0, sv)
            if (r_k2 == null) variableNotFound1Daf(sv.LineNumber, "i")

            // if (!Mglobal(RETRIEVE, tmp2, tmp3, svPtr))
            //     VariableNotFound(LineNumber, tmp2);
            if (MglobalDaf(RETRIEVE_D, tmp2, tmp3, sv) == 0)
                variableNotFoundDaf(sv.LineNumber, DafCRuntime.cstring(tmp2))
            // sym_(SYMSTORE, "k", tmp3, svPtr); // Store result s274
            symRetDaf(SYMSTORE_D, "k".toCharArray(), 0, tmp3, 0, sv)

            // for2 increment: add(sym_(11,"i",_dummy,svPtr),
            // for2_incr, _dummy), sym_(SYMSTORE,"i",_dummy,svPtr)
            val r2_inc = symRetDaf(SYMGET_NOLBL_D, "i".toCharArray(), 0,
                    dummy, 0, sv)
            val incS2 = r2_inc ?: ""
            addDaf(incS2, str32(for2_incr), dummy)
            symRetDaf(SYMSTORE_D, "i".toCharArray(), 0, dummy, 0, sv)
        } // FOR loop 2 close (longjmp fold)

        // LineNumber=7; w \u0024zd1-t,!
        sv.LineNumber = 7

        // if (svPtr->out_file[svPtr->io]==NULL)
        //     ErrorMessage("Write to input file", svPtr->LineNumber);
        if (sv.out_file[sv.io] == 0)
            errorMessageDaf("Write to input file", sv.LineNumber) // m1944

        // strcpy((char *) &gtmp[1], "\u0024zd1"); zfcn(gtmp,tmp0,svPtr);
        for (t in zd1.indices) gtmp[1 + t] = zd1[t]
        zfcnDaf(gtmp, tmp0, sv)

        // if (sym_(SYMGET,"t",tmp1,svPtr)==NULL)
        //     VariableNotFound1(LineNumber,"t");
        val r_t = symRetDaf(SYMGET_NOLBL_D, "t".toCharArray(), 0,
                tmp1, 0, sv)
        if (r_t == null) variableNotFound1Daf(sv.LineNumber, "t")

        // sub(tmp0, tmp1, tmp2);
        subDaf(DafCRuntime.cstring(tmp0), DafCRuntime.cstring(tmp1), tmp2)

        // svPtr->hor[svPtr->io] += fprintf(out_file[io], "%s", tmp2);
        val outLine = DafCRuntime.cstring(tmp2)
        print(outLine) // (quirk (7): stdout fold)
        sv.hor[sv.io] += outLine.length
        // fprintf(out_file[io], "\n"); hor=0; ver++;
        print("\n")
        sv.hor[sv.io] = 0
        sv.ver[sv.io]++

        // _epilogue: tmp0[0]=0; tmp1[0]=0; (Function Epilogue)
        tmp0[0] = '\u0000'
        tmp1[0] = '\u0000'
        // Mltoa(getpid(), atmp);
        mltoaDaf(getpidD(), atmp)
        // if (svPtr->LockDone) cleanLocks((char *) atmp);
        if (sv.LockDone != 0) cleanLocksDaf(DafCRuntime.cstring(atmp))
        // Mglobal(CLOSE, tmp0, tmp1, svPtr); (Close Globals)
        MglobalDaf(CLOSE_D, tmp0, tmp1, sv)

    } catch (e: Throwable) { // catch(...) - row 68 fold
        if (GlobalExceptionCodeD == INPUT_ERROR_D) println("Input Error")
        else if (GlobalExceptionCodeD == INPUT_LENGTH_D) println("Input Length Error")
        else if (GlobalExceptionCodeD == SYMTAB_ERROR_D) println("Symbol Table Error")
        else if (GlobalExceptionCodeD == GLOBAL_NOT_FOUND_D) println("Global Not Found Error")
        else if (GlobalExceptionCodeD == INTERPRETER_ERROR_D) println("Interprerer Error") // quirk (8)
        else if (GlobalExceptionCodeD == INVALID_CONVERSION_D) println("Invalid Conversion Error")
        else if (GlobalExceptionCodeD == DATA_RANGE_ERROR_D) println("Data Range Error")
        else if (GlobalExceptionCodeD == NUMERIC_RANGE_D) println("Numeric Range Error")
        else if (GlobalExceptionCodeD == UNSPECIFIED_D) println("Unspecified Error")
        println("Uncaught error in or near line " + sv.LineNumber)
        // Mglobal(CLOSE,tmp0,tmp1,svPtr); (Close Globals)
        MglobalDaf(CLOSE_D, tmp0, tmp1, sv)
        return 0 // EXIT_SUCCESS fold (quirk (9))
    }

    return 0 // return EXIT_SUCCESS;
} // Terminate

// ---- carrier helpers (row-56/101 folds; private per-file) ----

private fun setS(dst: CharArray, s: String) { // strmove fold
    for (t in s.indices) dst[t] = s[t]
    if (s.length < dst.size) dst[s.length] = '\u0000'
}

private fun set32D(dst: CharArray, s: String) { // strmove onto FORPARM
    for (t in s.indices) dst[t] = s[t]
    if (s.length < dst.size) dst[s.length] = '\u0000'
}

private fun str32(buf: CharArray): String { // C-string over FORPARM
    return DafCRuntime.cstring(buf)
}

private fun str32Has(buf: CharArray, c: Char): Boolean { // strchr
    var t = 0
    while (t < buf.size && buf[t] != '\u0000') {
        if (buf[t] == c) return true
        t++
    }
    return false
}

private fun numcmpForD(forF: Int, sv: DafMsv, varName: String,
                       lim: CharArray, dummy: CharArray): Boolean {
    // the for-limit ternary: forF == 1 -> numcomp(sym_(11,...),
    // lim) >= 0 (GE arm); else -> numcomp(sym_(11,...), lim) <= 0
    // (LE arm)
    val cur = symRetDaf(SYMGET_NOLBL_D, varName.toCharArray(), 0,
            dummy, 0, sv)
    val r = numcompDaf(cur ?: "", str32(lim))
    return if (forF == 1) r >= 0 else r <= 0
}
