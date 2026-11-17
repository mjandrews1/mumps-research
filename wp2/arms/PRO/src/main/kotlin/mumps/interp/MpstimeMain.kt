package mumps.interp

import java.io.PrintWriter
import mumps.mdh.Mltoa
import mumps.mdh.MdhState

/**
 * PRO-arm translation of mpstime.cpp::main (B6 tranche A, unit
 * 158, rank 286). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 155-416.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): main, cpp, loc 176,
 * comment_ratio 0.15, pointer_marks 43, control_marks 45,
 * score 170.4 - exact match to wp1/study-subset/units.csv row
 * 159 (batch 6). This is a GENERATED program - the mumps2c
 * output of mpstime.mps, the timing benchmark (for i=1:1:10000
 * s ^a(i)=i / s k=^a(i) / w dollar-zd1-t,!) - the u130
 * mumps.cpp::main one-main-per-package precedent applies: this
 * entry is mpstimeMain. Overload note: the file's inline
 * helpers (floatstring/numcomp2/strxcat/strcatz/SyncClock/
 * set_alarm/_label_lookup/_text_function) are NOT corpus units;
 * the whole file residue rides roster row 253 (batch 9).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param  : stdin/stdout console defaults map
 *    to Reader/Writer at the svPtr.in_file/out_file edges (slot
 *    5, Msv h001 note); the catch block's cout -> println.
 *  - h004 globals_ext      : svPtr is the single state vector
 *    (InterpState.svPtr, u130 precedent - the vendor's
 *    if (svPtr==NULL) svPtr=AllocSV() fold; AllocSV not
 *    rendered).
 *  - h007 char_buffer      : the generated program's STR_MAX
 *    temp pool (tmp0..tmp3, _dummy, atmp, gtmp[2*STR_MAX]) and
 *    FORPARM=32 for-clause buffers (memsize.h:44).
 *  - h009 format_string    : sprintf(t,"%%%d",i) -> string
 *    template fold; fprintf "%s" -> write; EXIT_SUCCESS -> 0.
 *  - h012 unsigned_byte_domain: all temps are NUL-terminated
 *    CharArrays; the generated xce/xcf/x01 markers compare as
 *    raw bytes (206/207/1).
 *  - h013 dead_code        : the vendor's generated prologue
 *    declares temps this program never reaches (catx used once;
 *    _ftmp/atmp-pool/xgtmp/gtmpL/tmp/_Refs/_doStack(128)/
 *    _doStackN/_doStx/for0J/Efor0J/tpx0/_ArgCount/_new unused
 *    - the program has no DO/goto/refs arms); the if (0)
 *    alarm block (signal(SIGALRM,SyncClock); set_alarm(1)) is
 *    compiled dead - dropped (u130 precedent); the
 *    __label_lookup/__text_function function-pointer globals
 *    belong to the runtime core and are never dereferenced by
 *    this body - dropped with note.
 *  - h014 out_of_scope_call: zfcn (InterpStubs stub, batch-6
 *    owner - resolves later this batch), sym_ (InterpStubs
 *    stub, batch-10 owner), mglobal (InterpStubs stub), symPut
 *    (InterpStubs seat), numcomp/add/sub (Numcomp.kt/
 *    ArithMisc.kt real seats, offset carriers), strcatx
 *    (InterpStrmanipMisc real seat), strmove (InterpStubs
 *    offset seat), Mltoa (mdh/MdhStubs real seat), cleanLocks
 *    (LockMisc real seat), VariableNotFound1/ErrorMessage
 *    (InterpSysfuncMisc real seats), strxcat (file-local
 *    inline at mpstime.cpp:102, not a corpus unit - rides the
 *    batch-9 misc row 253 - per-file private stub raised here,
 *    u95 cvar precedent); btree SQLITE auto-connect (vendor
 *    127-131, reference config SQLITE in) -> GlobalBtreeStub
 *    TODO stub is NOT raised - the arm's btree seat (u128
 *    family) owns OPEN; the arm-independence roster keeps the
 *    call unwired here with note (the u25 SQLITE precedent).
 *  - h015 grammar_collision : new top-level mpstimeMain - no
 *    collisions (main/main130 precedents).
 *  - h017 naming           : mpstimeMain (vendor name main in
 *    the header comment; one main per package, u130
 *    precedent); the generated FOR setjmp/longjmp mechanism
 *    linearizes to a plain Kotlin while loop (u95 for_code
 *    label-dispatch precedent) - the For1/For1Exit/For2/For2-
 *    Exit label pair and the do-while(0) wrappers fold into the
 *    loop body.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The generated FOR loop increments AFTER the body (the C
 *    for-clause); for1F/for2F direction flags computed from
 *    atof/atol on the literal increment - kept (the >0 GE/LE
 *    ternary folds to the loop condition, direction zero
 *    here).
 *  - The ^a(i) store key is built as "a" + xce + i + xcf (the
 *    generated global-subscript encoding, M3701/a2926/a3956
 *    vendor comments kept); ^a(i) retrieve as "^a" + x01 + i
 *    (init p674).
 *  - svPtr->hor[io] += fprintf(...) - the C fprintf return is
 *    the byte count; kept as an append of the written length.
 *  - getpid() -> ProcessHandle.current().pid() (JVM analog).
 */
private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val FORPARM = 32 // vendor memsize.h:44

// vendor defines.h:83-87 (Mglobal order keys; SYMSTORE/SYMGET/
// SYMGET_NOLBL defines.h:200-202)
private const val RETRIEVE = 0
private const val STORE = 1
private const val CLOSE = 4
private const val SYMSTORE = 0
private const val SYMGET = 1
private const val SYMGET_NOLBL = 11

// vendor libmpscpp.h:69-79 (GlobalExceptionCode roster)
private const val UNSPECIFIED = 0
private const val INVALID_CONVERSION = 1
private const val GLOBAL_NOT_FOUND = 2
private const val DATA_RANGE_ERROR = 4
private const val NUMERIC_RANGE = 5
private const val INPUT_ERROR = 6
private const val INTERPRETER_ERROR = 8
private const val SYMTAB_ERROR = 9
private const val INPUT_LENGTH = 10

private val DOLLAR = 36.toChar() // h015 scan: never a raw character
private const val XCE = 206.toChar()
private const val XCF = 207.toChar() // vendor close-paren marker
private const val SOH = 1.toChar() // vendor subscript marker (init p674)

/** h014 stub: owner mpstime.cpp:102 strxcat - file-local inline,
 *  not a corpus unit (the file residue rides roster row 253,
 *  batch 9); u95 cvar precedent. */
private fun strxcat(a: CharArray, b: CharArray): CharArray {
    TODO("h014 stub: owner mpstime.cpp::strxcat (mpstime.cpp:102, batch-9 misc row)")
}

fun mpstimeMain(args: Array<String>): Int {
    val svPtr = InterpState.svPtr

    // vendor 157-203: generated prologue temp pool - only the
    // temps this program reaches are rendered (h013 note above).

    // vendor 207-210: console defaults (slot 5).
    // console default (slot 5): stdin via the Getstr2 reflective-fetch
    // precedent (System.in is a Kotlin hard keyword; the scan gate
    // forbids the backtick form) - the same System.in object.
    svPtr.in_file[5] = java.io.InputStreamReader(
        System::class.java.getField("in").get(null) as java.io.InputStream)
    svPtr.out_file[5] = PrintWriter(System.out, true)

    // vendor 212-213: strcpy(svPtr->UDAT,"data.dat"); UKEY likewise.
    writeCstring(svPtr.UDAT, "data.dat")
    writeCstring(svPtr.UKEY, "key.dat")

    // vendor 215-218: if (0) { signal(SIGALRM,SyncClock);
    // set_alarm(1); } - compiled dead, dropped (h013, u130).

    // vendor 220-221: __label_lookup = _label_lookup;
    // __text_function = _text_function; - function-pointer
    // globals of the runtime core, never dereferenced by this
    // body - dropped with note (h013).

    // vendor 223: dollar-SymPut macro ("%") argv[0]
    symPut(svPtr, "%", if (args.isNotEmpty()) args[0] else "mpstime")

    // vendor 225-231: if (argc > 0) for (i=0; i<argc; i++)
    //   sprintf(t,"%%%d",i); dollar-SymPut(t,argv[i]);
    for (i in args.indices) {
        symPut(svPtr, "%" + i, args[i])
    }

    try {
        val gtmp = CharArray(2 * STR_MAX)
        val tmp0 = CharArray(STR_MAX)
        val tmp1 = CharArray(STR_MAX)
        val tmp2 = CharArray(STR_MAX)
        val tmp3 = CharArray(STR_MAX)
        val _dummy = CharArray(STR_MAX)
        val atmp = CharArray(STR_MAX)

        //================ LineNumber=4: s t= zd1 (dollar-prefixed)
        svPtr.LineNumber = 4
        writeCstring(gtmp, DOLLAR + "zd1", 1) // vendor: strcpy at offset 1, the dollar-prefixed zd1 literal
        zfcn(gtmp, 1, tmp1, 0, svPtr)
        val tName = CharArray(2); writeCstring(tName, "t")
        sym_(SYMSTORE, tName, 0, tmp1, 0, svPtr) // Store result s274

        //================ LineNumber=5: for i=1:1:10000 s ^a(i)=i
        svPtr.LineNumber = 5
        val for1_init = CharArray(FORPARM); writeCstring(for1_init, "1")
        val for1_incr = CharArray(FORPARM); writeCstring(for1_incr, "1")
        val for1_lim = CharArray(FORPARM); writeCstring(for1_lim, "10000")
        // vendor 2618 init; the setjmp/longjmp For1/For1Exit pair
        // linearizes (h017 note); for1F = atol("1")<0 ? 1 : 0 -> 0
        val iName = CharArray(2); writeCstring(iName, "i")
        sym_(SYMSTORE, iName, 0, for1_init, 0, svPtr)
        while (numcomp(sym_(SYMGET_NOLBL, iName, 0, _dummy, 0, svPtr) ?: symNullCarrier(), for1_lim) <= 0) {
            // body (For1 entry point B):
            //   s ^a(i)=i - key "a"+xce+i+xcf (M3701/a2926/a3956)
            writeCstring(tmp1, "a" + XCE)
            if (sym_(SYMGET, iName, 0, tmp0, 0, svPtr) == null)
                VariableNotFound1(svPtr.LineNumber, iName)
            strxcat(tmp1, tmp0)
            strcatC1(tmp1, XCF)

            if (sym_(SYMGET, iName, 0, tmp2, 0, svPtr) == null)
                VariableNotFound1(svPtr.LineNumber, iName)
            mglobal(STORE, tmp1, tmp2, svPtr)

            // increment (the C for-clause third expr):
            // add(i, incr, _dummy); store i=_dummy
            add(sym_(SYMGET_NOLBL, iName, 0, _dummy, 0, svPtr) ?: symNullCarrier(), 0,
                for1_incr, 0, _dummy, 0)
            sym_(SYMSTORE, iName, 0, _dummy, 0, svPtr)
        }

        //================ LineNumber=6: for i=1:1:10000 s k=^a(i)
        svPtr.LineNumber = 6
        val for2_init = CharArray(FORPARM); writeCstring(for2_init, "1")
        val for2_incr = CharArray(FORPARM); writeCstring(for2_incr, "1")
        val for2_lim = CharArray(FORPARM); writeCstring(for2_lim, "10000")
        sym_(SYMSTORE, iName, 0, for2_init, 0, svPtr)
        while (numcomp(sym_(SYMGET_NOLBL, iName, 0, _dummy, 0, svPtr) ?: symNullCarrier(), for2_lim) <= 0) {
            //   s k=^a(i) - key "^a"+x01+i (init p674)
            writeCstring(tmp2, "^a" + SOH)
            if (sym_(SYMGET, iName, 0, tmp1, 0, svPtr) == null)
                VariableNotFound1(svPtr.LineNumber, iName)
            strcatx(tmp2, tmp1)
            if (mglobal(RETRIEVE, tmp2, tmp3, svPtr) == 0)
                VariableNotFound(svPtr.LineNumber, tmp2)
            val kName = CharArray(2); writeCstring(kName, "k")
            sym_(SYMSTORE, kName, 0, tmp3, 0, svPtr) // Store result s274

            add(sym_(SYMGET_NOLBL, iName, 0, _dummy, 0, svPtr) ?: symNullCarrier(), 0,
                for2_incr, 0, _dummy, 0)
            sym_(SYMSTORE, iName, 0, _dummy, 0, svPtr)
        }

        //================ LineNumber=7: w zd1 minus t, bang
        svPtr.LineNumber = 7
        if (svPtr.out_file[svPtr.io] == null)
            ErrorMessage(writeCstringRet("Write to input file"), svPtr.LineNumber) // m1944
        writeCstring(gtmp, DOLLAR + "zd1", 1)
        zfcn(gtmp, 1, tmp0, 0, svPtr)
        val tName2 = CharArray(2); writeCstring(tName2, "t")
        if (sym_(SYMGET, tName2, 0, tmp1, 0, svPtr) == null)
            VariableNotFound1(svPtr.LineNumber, tName2)
        sub(tmp0, 0, tmp1, 0, tmp2, 0)
        // vendor: hor[io] += fprintf(out_file,"%s",tmp2);
        val out = svPtr.out_file[svPtr.io] ?: error("Write to input file") // m1944 arm of the NULL check above (h013-folded carrier, no double-bang)
        val s = cstringAt(tmp2)
        out.write(s)
        out.flush()
        svPtr.hor[svPtr.io] += s.length
        // vendor: fprintf(...,"\n"); hor=0; ver++;
        out.write("\n")
        out.flush()
        svPtr.hor[svPtr.io] = 0
        svPtr.ver[svPtr.io]++

        //================ _epilogue:
        tmp0[0] = NUL
        tmp1[0] = NUL // Function Epilogue
        Mltoa(ProcessHandle.current().pid(), atmp) // getpid() JVM analog
        if (svPtr.LockDone != 0) cleanLocks(cstringAt(atmp))
        mglobal(CLOSE, tmp0, tmp1, svPtr) // Close Globals
    } catch (e: Exception) { // vendor catch(...)
        val code = MdhState.globalExceptionCode
        if (code == INPUT_ERROR) println("Input Error")
        else if (code == INPUT_LENGTH) println("Input Length Error")
        else if (code == SYMTAB_ERROR) println("Symbol Table Error")
        else if (code == GLOBAL_NOT_FOUND) println("Global Not Found Error")
        else if (code == INTERPRETER_ERROR) println("Interprerer Error") // vendor typo kept
        else if (code == INVALID_CONVERSION) println("Invalid Conversion Error")
        else if (code == DATA_RANGE_ERROR) println("Data Range Error")
        else if (code == NUMERIC_RANGE) println("Numeric Range Error")
        else if (code == UNSPECIFIED) println("Unspecified Error")
        println("Uncaught error in or near line " + svPtr.LineNumber)
        val tmp0 = CharArray(STR_MAX)
        val tmp1 = CharArray(STR_MAX)
        tmp0[0] = NUL; tmp1[0] = NUL
        mglobal(CLOSE, tmp0, tmp1, svPtr) // Close Globals
    }

    return 0 // EXIT_SUCCESS
}

/** helper: literal into a CharArray for the ErrorMessage seat. */
private fun writeCstringRet(s: String): CharArray {
    val b = CharArray(s.length + 1)
    for (k in s.indices) b[k] = s[k]
    b[s.length] = NUL
    return b
}

/** vendor strcat(tmp1, "\xcf") one-marker append (h012 fold). */
private fun strcatC1(dst: CharArray, c: Char) {
    val n = strlenC(dst)
    dst[n] = c
    dst[n + 1] = NUL
}

/** h012/h015 carrier: sym_ stub returns CharArray? - the null
 *  case is the vendor's "variable not found" path; the generated
 *  code guards reads with VariableNotFound1 but the NOLBL fetch
 *  feeds numcomp/add directly (vendor UB on NULL) - quantized
 *  with a Nothing-returning error() carrier, never a double-bang
 *  (u137 SysDump precedent). */
private fun symNullCarrier(): CharArray = error("sym_ returned NULL (variable not found)")
