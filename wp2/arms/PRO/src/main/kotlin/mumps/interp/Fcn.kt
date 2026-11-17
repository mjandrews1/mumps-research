package mumps.interp

import kotlin.math.abs
import kotlin.system.exitProcess

/**
 * PRO-arm translation of interp-fcn.cpp::fcn (B4 unit 94, rank 307).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 103-1281 (units.csv: loc 775, comment_ratio 0.136,
 * pointer_marks 356, control_marks 329, score 1221.5, batch 4 -
 * exact match).
 *
 * fcn(MSV *): the intrinsic dollar-function dispatcher - the
 * dollar-dollar extrinsic function launcher, the 256-entry opcode
 * table, and the switch over $extract/$ascii/$char/$find/$fnumber/
 * $justify/$job/$len/$piece/$select/$storage/$test/$text/
 * $translate/$random/$reverse/$x/$y/$io/$horolog/$get/pattern-match
 * codes/$name/$next/$noerr/$order/$query/$qlength/$qsubscript and
 * the dollar-z variables.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro: printf -> System.out.print; the
 *    unrecognized-dollar-z exit(EXIT_FAILURE) ->
 *    kotlin.system.exitProcess(1) (LibmpscppMisc1 unit 79
 *    precedent); getcwd -> System.getProperty("user.dir").
 *  - h004 globals_ext        : the 'const static unsigned char
 *    opcode[256]' local table becomes the file-private OPCODE val
 *    (same one-owner policy as the compiler-side CompilerState
 *    carriers); the $$ block's child state vector rides the
 *    allocSV stub and free() is dropped (GC).
 *  - h007 char_buffer        : tmp1/tmp2/tmp3 and the per-case
 *    scratch buffers are NUL-terminated CharArray byte buffers;
 *    the C strcpy/strcat pointer arithmetic is carried by the
 *    file-private copyC/appendC helpers.
 *  - h009 format_string      : sprintf(bd, "%d"/"%ld"/"%X", ...)
 *    rendered as plain decimal/hex String writes
 *    (GlobalQuery/Readf_ precedents); the runtime-built
 *    "%l.kf" justify format and the strfmon pattern strings are
 *    assembled by concatenation (no string templates - the
 *    dollar byte rides the DOLLAR char val, InterpPiece
 *    precedent).
 *  - h010 numeric_promotion  : the vendor 'long i, j' loop/count
 *    positions stay Int (STR_MAX domain, SetCode precedent); the
 *    reused j (function id char, then end-position) is carried
 *    as an Int in the char-code domain; the xindex start argument
 *    rides the Short stub param with explicit widening casts;
 *    atol/atof are the file-private atolC/atofC C-library scans.
 *  - h011 goto_restructuring : every goto target (ex1/ex2/err/
 *    arg_count_err/unknown_err/fnum_wrong/qlenerr/qsuberr1) ends
 *    the vendor function, so the tails are expanded inline at
 *    each jump site (goto -> return); the $select sel1 backward
 *    goto becomes while(true)+continue; the $translate trns
 *    forward goto becomes if/else.
 *  - h012 unsigned_byte_domain: v1d/bd/xd/pd1/args and every
 *    scratch buffer are C-string domains (Char 0..255); the
 *    206/207/208 coded punctuation, the 209 $select separator,
 *    the 1 SOH subscript marker and the '\x03'/'\x02' $get
 *    sentinels are carried as Chars.
 *  - h013 dead_store         : the unused 'double t1', the
 *    'char *ctime()' declaration, the unused 'long timezone/
 *    daylight' and 'short m, n' locals, the dead Mglobal/sym_
 *    STORE assignment results, and the unreachable qsuberr label
 *    (the vendor only ever jumps to qsuberr1) are dropped with
 *    this comment, not translated; free(svPtr1) is GC.
 *  - h014 out_of_scope_call : ScanParse (interp-sym.cpp::ScanParse,
 *    batch-8 corpus unit), Eval (interp-sym.cpp:1192, not a
 *    corpus unit), Qlength (interp-sym.cpp::Qlength, batch-5
 *    corpus unit), Qsub (interp-sym.cpp::Qsub, batch-7 corpus
 *    unit), LocalName (interp-sysfunc.cpp::LocalName, B4 corpus
 *    unit, groups 2/3), GlobalOrder (include/mumpsc/globalOrder.h:8
 *    inline, not a corpus unit), lpad (interp-strmanip.cpp:135,
 *    not a corpus unit), strfmon (C library, monetary.h) - all NEW
 *    owner-named InterpStubs stubs raised by this unit, with
 *    offset-carrying signatures for the &bd[1]/&v1d[1] pointer
 *    bindings; BuildGlobal/BuildLocal gain an arg4Off offset
 *    (default 0) and an Int return (vendor interp-parse.cpp:1836 /
 *    interp-sym.cpp:97 - the existing void-style stubs returned
 *    Unit because the parse_ call sites ignored the result; this
 *    unit tests it); the sql stub parameters relax to CharArray?
 *    for the NULL-argument OPEN call; sym_/strmove/xindex/
 *    mglobal/interpret/allocSV/sigint reuse previously raised
 *    stubs; _piece (DONE, unit 103), GlobalQuery (DONE, unit 98),
 *    GlobalName (DONE, unit 22) and mpsJustify (DONE, unit 83)
 *    are called directly, with offset-0 copies carrying the
 *    &bd[1]/&v1d[1] bindings and copies written back.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level fcn replaces its own h014
 *    stub (removed in the same tranche); new stubs Eval/
 *    ScanParse/Qlength/Qsub/LocalName/GlobalOrder/lpad/strfmon
 *    checked against public same-package signatures (the B4
 *    build-gate lesson) - the mdh-side GlobalOrder(String, Int)
 *    overload lives in another package and is not imported here;
 *    file-private copyC/appendC/atolC/atofC checked likewise.
 *  - h017 naming            : vendor spellings kept (fcn, sym_,
 *    GlobalOrder, LocalName, Qlength, Qsub, ScanParse, Eval,
 *    lpad, strfmon, BuildGlobal, BuildLocal, GlobalQuery,
 *    GlobalName, _piece).
 *  - h018 preprocessor_conditional: '#define SQLITE' is live, so
 *    the $ztabsize sql(OPEN,...) arm and the $zsqloutput arm are
 *    translated (the $ztable arm's sql calls are commented out in
 *    the vendor and stay commented here); the $random GMP block is
 *    MULTI_PRECISION (compiled out of the reference binary) and
 *    the $justify BIGFLOAT '%Lf' and MULTI_PRECISION '%RNf' arms
 *    are likewise excluded - the live plain-double '%l.kf' arm
 *    feeds mpsJustify (unit 83), whose own h018 note records the
 *    same resolution.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor reuses the local j as the function id char and
 *    then as end-position/work registers inside the cases (case 1
 *    $extract end, case 8 $select xindex position, case 9 $text
 *    walk) - kept as one Int register with the same clobbers.
 *  - Case 1 declares 'char tmp1[STR_MAX]' shadowing the outer
 *    tmp1[2] and further 'char tmp2[STR_MAX]' shadows inside the
 *    global arms; the Kotlin locals use distinct names (tmp1E/
 *    tmp2E/tmp2E2/ctmpE) with the shadowing noted at each site.
 *  - Case 1: 'if (j > iargs[1]) j = iargs[1]' caps the RHS end at
 *    the byte offset of the second argument, not the string
 *    length (vendor off-by design, kept); 'i >= iargs[1]' in the
 *    empty-string check is the same quirk.
 *  - The $justify 2-arg arm caps l at 255 before lpad, matching
 *    the bd[STR_MAX] reality but after the k >= l compare (kept
 *    in that order).
 *  - The $$ block writes the child's args buffer into bd[1] via
 *    the function-scope tmp2; free(svPtr1) is dropped (GC) - the
 *    start[] copy back and the nstart pointer copy are kept.
 *  - '$ztabsize' bounds TabSize to 1..22 but leaves ierr unset
 *    before jumping into the unknown tail (kept).
 *  - $ascii with fewer than 2 args reads iargs[1] before any second
 *    argument exists - the vendor reads an uninitialized stack slot
 *    (UB in C); the Kotlin carrier reads the zero-initialized
 *    IntArray, so the guard is always true there and the literal
 *    condition is kept (honest observable of the vendor UB).
 *  - The pattern-match codes build '_<id><S>' and look it up with
 *    sym_ code 1; the printf uses the literal x text.
 *  - strfmon is given maxsize 256 against the 64-byte rstr
 *    scratch (vendor heap overrun risk; the Kotlin buffer is
 *    65 Chars and the stub will throw where the vendor overruns -
 *    BMGSearch unit 97 precedent).
 */
fun fcn(svPtr: Msv) {

    // vendor 105-126: locals (dead decls dropped per h013)
    val tmp1 = CharArray(2) // vendor tmp1[2]={0,0}
    val tmp2 = CharArray(STR_MAX)
    val tmp3 = CharArray(STR_MAX)
    var rslt = 0
    val cod209 = charArrayOf(209.toChar(), NUL)
    var day: Long

    //----------------
    // $$FUNCTION
    //----------------

    // vendor 141-193
    if (svPtr.v1d[1] == DOLLAR && svPtr.v1d[2] == DOLLAR) {

        svPtr.xpx++
        val org = svPtr.xpx
        val end = ScanParse(svPtr.xd, svPtr.xpx) + svPtr.xpx
        val hold = svPtr.xd[end]
        svPtr.xd[end] = NUL
        var tcmd = "do " + cstringAt(svPtr.v1d, 3)

        // look for ^ in function call - if not, function is local

        var ca = svPtr.xpx
        while (svPtr.xd[ca] != NUL) {
            if (svPtr.xd[ca] == '^') break
            ca++
        }

        if (svPtr.xd[ca] == NUL) { // local label form
            var i = 3
            while (svPtr.v1d[i] != NUL) {
                if (svPtr.v1d[i].code == CODED_OPEN) break
                i++
            }
            val noArgs = svPtr.v1d[i] == NUL // vendor j flag: 1 = no arg case
            svPtr.v1d[i] = NUL
            var t = "do " + cstringAt(svPtr.v1d, 3) + "^" + cstringAt(svPtr.currentFile)
            if (!noArgs) { // attach argument
                svPtr.v1d[i] = '('
                t += cstringAt(svPtr.v1d, i)
                t = t.dropLast(1) // vendor tmp[strlen(tmp)-1]=0
                t += ")"
            }
            tcmd = t
        } else {
            tcmd += cstringAt(svPtr.xd, org)
            svPtr.xd[end] = hold
        }

        //--------------------------------------
        // create environment and launch command
        //--------------------------------------

        val svPtr1 = allocSV()
        svPtr1._Sym = svPtr._Sym
        svPtr1.LineNumber = svPtr.LineNumber
        for (i in 0 until SYM_MAX) svPtr1.start[i] = svPtr.start[i]
        svPtr1.nstart = svPtr.nstart

        svPtr.ierr = interpret(tcmd, svPtr1)

        for (i in 0 until SYM_MAX) svPtr.start[i] = svPtr1.start[i]

        val res = cstringAt(svPtr1.args) // strcpy(tmp2,&svPtr1->args)

        if (svPtr.ierr != 0) { // goto err
            fcnErr(svPtr)
            return
        }

        svPtr.xpx = end - 1
        writeCstring(svPtr.bd, res, 1) // strcpy(&bd[1],tmp2)
        return
    }

    // vendor 195-218: argument scan

    svPtr.ierr = 0
    val iargs = IntArray(10)
    iargs[0] = 1
    var nargs = 0
    var j = svPtr.v1d[2].code // hold function id (reused below per vendor)
    val S = svPtr.v1d[3]

    copyC(tmp2, 0, svPtr.v1d, 1) // strcpy(tmp2,&v1d[1])

    while (svPtr.v1d[1].code != CODED_OPEN && svPtr.v1d[1] != NUL)
        strmove(svPtr.v1d, 1, svPtr.v1d, 2)

    if (svPtr.v1d[1].code == CODED_OPEN) strmove(svPtr.v1d, 1, svPtr.v1d, 2)

    if (j == CODED_OPEN) { // noname form - parms to sub only
        copyC(svPtr.args, 0, svPtr.v1d, 1)
        svPtr.ierr = 0 // ex2
        return
    }

    var i = 1
    while (svPtr.v1d[i] != NUL) {
        if (svPtr.v1d[i].code == CODED_CLOSE || svPtr.v1d[i].code == CODED_COMMA) {
            svPtr.v1d[i] = NUL
            nargs++
            iargs[nargs] = i + 1
        }
        i++
    }

    j = Character.toLowerCase(j) // vendor 220: tolower conversion

    val opc = OPCODE[j]
    if (opc == 99) { // vendor 222-225
        svPtr.ierr = 30
        fcnUnknown(svPtr) // unknown_err
        return
    }

    when (opc) {

        // (vendor default: - see the trailing else at the end of this when,
        // which Kotlin requires to be its last entry)

        //-----------------------------------------------------------------------
        // $z variable and LHS functions
        //-----------------------------------------------------------------------

        98 -> { // vendor 244-270
            if (cstringAt(tmp2).equals(DOLLAR + "ztable", ignoreCase = true)) {
                // SQLITE arm: the vendor sql(CLOSE)/sql(OPEN) calls here are
                // commented out in the source (kept commented)
                svPtr.ierr = 0 // ex2
                return
            }

            if (cstringAt(tmp2).equals(DOLLAR + "ztabsize", ignoreCase = true)) {
                // SQLITE arm (live)
                sql(OPEN, svPtr, null, null, null, null)
                svPtr.TabSize = atoiC(svPtr.pd1, svPtr.sdlim)
                if (svPtr.TabSize > 22) { // unknown_err (ierr left unset - vendor)
                    fcnUnknown(svPtr)
                    return
                }
                if (svPtr.TabSize < 1) {
                    fcnUnknown(svPtr)
                    return
                }
                svPtr.ierr = 0 // ex2
                return
            }

            if (cstringAt(tmp2).equals(DOLLAR + "zsqloutput", ignoreCase = true)) {
                copyC(svPtr.sqloutput, 0, svPtr.pd1, svPtr.sdlim)
                svPtr.ierr = 0 // ex2
                return
            }

            System.out.print(
                "\n*** Unrecognized Z-type variable.\n" + cstringAt(tmp2) +
                    "\nIn or near line number " + svPtr.LineNumber + "\n\n"
            )
            exitProcess(1) // exit(EXIT_FAILURE)
        }

        //-----------------------------------------------------------------------
        // $extract()
        //-----------------------------------------------------------------------

        1 -> { // vendor 274-455
            if (nargs == 1) { // no 2nd or 3rd operand - return first char

                if (svPtr.setpiece != 0) { // LHS ref
                    val tmp1E = CharArray(STR_MAX) // vendor shadow of tmp1[2]

                    if (svPtr.setname[0] == '^') { // global array reference
                        val tmp2E = CharArray(STR_MAX) // vendor shadow

                        val f = mglobal(RETRIEVE, svPtr.setname, tmp2E, svPtr)

                        if (f != 0) { // global exists
                            copyC(tmp1E, 0, svPtr.pd1, svPtr.sdlim)
                            if (strlenC(tmp2E) != 0) appendC(tmp1E, tmp2E, 1)
                            mglobal(STORE, svPtr.setname, tmp1E, svPtr) // h013: dead f
                            svPtr.ierr = 0 // ex2
                            return
                        } else { // global does not exist
                            copyC(tmp1E, 0, svPtr.pd1, svPtr.sdlim)
                            mglobal(STORE, svPtr.setname, tmp1E, svPtr) // h013: dead f
                            svPtr.ierr = 0 // ex2
                            return
                        }
                    } else { // local variable reference
                        val ctmp = sym_(SYMRETRIEVE, svPtr.setname, 0, svPtr.bd, 1, svPtr)
                        if (ctmp != null) { // variable exists
                            copyC(tmp1E, 0, svPtr.pd1, svPtr.sdlim)
                            if (strlenC(ctmp) != 0) appendC(tmp1E, ctmp, 1)
                            sym_(SYMSTORE, svPtr.setname, 0, tmp1E, 0, svPtr) // h013: dead ctmp
                            svPtr.ierr = 0 // ex2
                            return
                        } else { // variable does not exist
                            copyC(tmp1E, 0, svPtr.pd1, svPtr.sdlim)
                            sym_(SYMSTORE, svPtr.setname, 0, tmp1E, 0, svPtr) // h013: dead ctmp
                            svPtr.ierr = 0 // ex2
                            return
                        }
                    }
                }

                //---------------------------
                // not LHS $e() - RHS function
                //---------------------------

                svPtr.bd[1] = svPtr.v1d[1] // return first char
                svPtr.bd[2] = NUL
                svPtr.ierr = 0 // ex2
                return
            }

            if (nargs != 2 && nargs != 3) {
                fcnArgCount(svPtr) // arg_count_err
                return
            }

            i = atolC(svPtr.v1d, iargs[1]) // arg 2

            if (i <= 0) i = 1

            if (nargs != 3) j = i // 2nd operand only - return one char
            else j = atolC(svPtr.v1d, iargs[2]) // arg 3

            if (j <= 0) j = 0 // return null string

            //------------------------------------
            // RHS function where end before start
            //------------------------------------

            if (svPtr.setpiece == 0 && (i >= iargs[1] || j < i)) {
                svPtr.bd[1] = NUL // empty string
                svPtr.ierr = 0 // ex2
                return
            }

            //--------------------------
            // LHS where end before start
            //--------------------------

            if (svPtr.setpiece != 0 && j < i) { // store empty string
                val tmp1S = CharArray(16) // "" initial

                if (svPtr.setname[0] == '^') { // global array reference
                    mglobal(STORE, svPtr.setname, tmp1S, svPtr) // h013: dead f
                    svPtr.ierr = 0 // ex2
                    return
                } else { // local variable
                    sym_(SYMSTORE, svPtr.setname, 0, tmp1S, 0, svPtr) // h013: dead ctmp
                    svPtr.ierr = 0 // ex2
                    return
                }
            }

            // if j exceeds str length, make it str length - RHS
            // (vendor compares against iargs[1], kept)

            if (svPtr.setpiece == 0 && j > iargs[1]) j = iargs[1]

            if (i < 1) i = 1 // if start is less than 1, make it 1

            // 3 argument forms

            // LHS ref ------------------------------------------

            if (svPtr.setpiece != 0) { // LHS ref
                val tmp1E = CharArray(STR_MAX) // vendor shadow of tmp1[2]
                val tmp2E = CharArray(STR_MAX) // vendor shadow of tmp2

                if (svPtr.setname[0] == '^') { // global array reference
                    val tmp2E2 = CharArray(STR_MAX) // vendor inner shadow
                    val ctmpE = CharArray(STR_MAX)

                    val f = mglobal(RETRIEVE, svPtr.setname, tmp2E2, svPtr) // fetch gbl

                    if (f != 0) { // global exists
                        copyC(ctmpE, 0, tmp2E2, 0) // gbl array value

                        if (strlenC(tmp2E2) < i)
                            while (strlenC(tmp2E2) < j) appendC(tmp2E2, " ") // make it longer
                        tmp2E2[i - 1] = NUL // truncate

                        copyC(tmp1E, 0, svPtr.pd1, svPtr.sdlim) // RHS

                        if (strlenC(ctmpE) != 0) appendC(tmp2E2, tmp1E, 0) // insert RHS
                        if (strlenC(ctmpE) >= j) appendC(tmp2E2, ctmpE, j)

                        mglobal(STORE, svPtr.setname, tmp2E2, svPtr) // h013: dead f

                        svPtr.ierr = 0 // ex2
                        return
                    } else { // global does not exist
                        var k = 0
                        while (k < i - 1) {
                            tmp1E[k] = ' '
                            k++
                        }
                        tmp1E[k] = NUL // initial string
                        appendC(tmp1E, svPtr.pd1, svPtr.sdlim)
                        mglobal(STORE, svPtr.setname, tmp1E, svPtr) // h013: dead f
                        svPtr.ierr = 0 // ex2
                        return
                    }
                } else { // local variable
                    val ctmp = sym_(SYMRETRIEVE, svPtr.setname, 0, svPtr.bd, 1, svPtr)
                    if (ctmp != null) { // variable exists
                        copyC(tmp2E, 0, ctmp, 0) // value of variable
                        if (strlenC(tmp2E) < i)
                            while (strlenC(tmp2E) < j) appendC(tmp2E, " ") // make it longer
                        tmp2E[i - 1] = NUL // truncate
                        copyC(tmp1E, 0, svPtr.pd1, svPtr.sdlim) // RHS
                        if (strlenC(ctmp) != 0) appendC(tmp2E, tmp1E, 0) // insert RHS
                        if (strlenC(ctmp) >= j) appendC(tmp2E, ctmp, j)
                        sym_(SYMSTORE, svPtr.setname, 0, tmp2E, 0, svPtr) // h013: dead ctmp
                        svPtr.ierr = 0 // ex2
                        return
                    } else { // variable does not exist - create and append
                        var k = 0
                        while (k < i - 1) {
                            tmp1E[k] = ' '
                            k++
                        }
                        tmp1E[k] = NUL // initial string
                        appendC(tmp1E, svPtr.pd1, svPtr.sdlim)
                        sym_(SYMSTORE, svPtr.setname, 0, tmp1E, 0, svPtr) // h013: dead ctmp
                        svPtr.ierr = 0 // ex2
                        return
                    }
                }
            }

            // RHS ref --------------------------------------------

            var k = 1
            var ii = i
            while (ii <= j) {
                svPtr.bd[k] = svPtr.v1d[ii]
                k++
                ii++
            }

            svPtr.bd[k] = NUL
            svPtr.ierr = 0 // ex2
        }

        //-----------------------------------------------------------------------
        // $ascii()
        //-----------------------------------------------------------------------

        2 -> { // vendor 457-471
            if (nargs > 2) {
                fcnArgCount(svPtr)
                return
            }

            i = if (nargs == 2) atolC(svPtr.v1d, iargs[1]) else 1

            if (i >= iargs[1] || i <= 0) {
                svPtr.bd[1] = '-'
                svPtr.bd[2] = '1'
                svPtr.bd[3] = NUL
                return
            }

            rslt = svPtr.v1d[i].code
            if (rslt == 0) rslt = -1
            writeCstring(svPtr.bd, rslt.toString(), 1) // ex1
        }

        //-----------------------------------------------------------------------
        // $char()
        //-----------------------------------------------------------------------

        3 -> { // vendor 473-489
            svPtr.bd[1] = NUL
            var jc = 1
            for (pc in 1..nargs) {
                strmove(tmp2, 0, svPtr.v1d, jc)
                day = atolC(tmp2).toLong()
                if (day <= 0) day = 0
                tmp1[0] = Char((day and 0xFF).toInt()) // vendor char truncation
                tmp1[1] = NUL
                appendC(svPtr.bd, tmp1, 0, 1) // strcat(&bd[1], tmp1)
                jc = iargs[pc]
            }
            return
        }

        //-----------------------------------------------------------------------
        // $find() $fnumber()
        //-----------------------------------------------------------------------

        4 -> { // vendor 491-602
            if (S.lowercaseChar() == 'n') { // $fnumber()
                var dp = -1
                var plus = 0
                var minus = 0
                var comma = 0
                var t = 0
                var t1f = 0 // vendor t1 (shadow of the outer unused t1, h013)
                var p = 0

                if (nargs != 3 && nargs != 2) {
                    fcnArgCount(svPtr)
                    return
                }

                if (nargs == 3) dp = atoiC(svPtr.v1d, iargs[2])

                var x = atofC(svPtr.v1d, 1) // value to be converted

                if (svPtr.v1d[iargs[1]] == '*') { // arg is for strfmon
                    strfmon(svPtr.bd, 1, 256, svPtr.v1d, iargs[1] + 1, x)
                    return
                }

                val pat = cstringAt(svPtr.v1d, iargs[1])
                if (pat.contains('+')) plus = 1
                if (pat.contains('-')) minus = 1
                if (pat.contains(',')) comma = 1
                if (pat.contains('T')) t = 1
                if (pat.contains('t')) t = 1
                if (pat.contains('P')) p = 1
                if (pat.contains('p')) p = 1

                if (p != 0 && (plus != 0 || minus != 0 || t != 0)) {
                    fcnFnumWrong(svPtr) // fnum_wrong
                    return
                }

                var fmt = "%!" // format suppress currency sym

                if (t != 0) { // trailing sign
                    if (x < 0) t1f = 1
                    x = abs(x)
                }

                if (comma == 0) fmt += "^" // no grouping chars
                if (p != 0) fmt += "(" // use parens for neg
                if (minus != 0) x = abs(x) // suppress minus

                if (dp != -1) {
                    fmt += "." + dp
                } else {
                    var di = 1
                    while (svPtr.v1d[di] != NUL && svPtr.v1d[di] != '.') di++
                    if (svPtr.v1d[di] == NUL) fmt += ".0"
                    else {
                        val cnt = strlenC(svPtr.v1d, 1) - di // vendor i = strlen(&v1d[1]) - i
                        fmt += "." + cnt
                    }
                }

                fmt += "i"

                val rstr = CharArray(65) // vendor rstr[64] (see the overrun note)
                val fmtBuf = CharArray(64)
                writeCstring(fmtBuf, fmt)
                strfmon(rstr, 1, 256, fmtBuf, 0, x)

                rstr[0] =
                    if (plus != 0 && x > 0 && minus == 0 && t == 0) '+'
                    else ' '

                if (t != 0) {
                    if (t1f != 0) writeCstring(rstr, "-", strlenC(rstr))
                    else if (plus != 0) writeCstring(rstr, "+", strlenC(rstr))
                }

                if (rstr[0] == ' ')
                    writeCstring(svPtr.bd, cstringAt(rstr, 1), 1)
                else
                    writeCstring(svPtr.bd, cstringAt(rstr), 1)

                return
            }

            //------
            // $find
            //------

            if (nargs != 2 && nargs != 3) {
                fcnArgCount(svPtr)
                return
            }

            strmove(tmp2, 0, svPtr.v1d, 1)
            strmove(tmp3, 0, svPtr.v1d, iargs[1])

            i = if (nargs == 2) 1 else atolC(svPtr.v1d, iargs[2])

            if (tmp3[0] != NUL) {
                i = xindex(tmp2, tmp3, i.toShort())
                if (i > 0) i += strlenC(tmp3)
            }

            if (i > strlenC(tmp2) + 1) i = 0
            rslt = i
            writeCstring(svPtr.bd, rslt.toString(), 1) // ex1
        }

        //-----------------------------------------------------------------------
        // $justify() $job
        //-----------------------------------------------------------------------

        5 -> { // vendor 604-636
            if (nargs == 0) { // $job
                writeCstring(svPtr.bd, ProcessHandle.current().pid().toString(16).uppercase(), 1)
                svPtr.ierr = 0 // ex2
                return
            }

            if (nargs < 2 || nargs > 3) {
                fcnArgCount(svPtr)
                return
            }

            var l = atoiC(svPtr.v1d, iargs[1])

            strmove(svPtr.bd, 1, svPtr.v1d, 1)

            if (nargs != 3) {
                val k = strlenC(svPtr.bd, 1)
                if (k >= l) {
                    svPtr.ierr = 0 // ex2
                    return
                }
                if (l > 255) l = 255
                lpad(svPtr.bd, 1, svPtr.bd, 1, l.toShort())
                svPtr.ierr = 0 // ex2
                return
            }

            val k = atoiC(svPtr.v1d, iargs[2]) // arg 3

            // h018: live plain-double arm ('%c%d%c%df' via sprintf);
            // the BIGFLOAT '%Lf' and MULTI_PRECISION '%RNf' arms are
            // compiled out of the reference binary

            val fmt = "%" + l + "." + k + "f"
            writeCstring(tmp2, fmt)

            // mps_justify(tmp2, &bd[1], &v1d[1]) - the owner-level
            // mpsJustify (unit 83) is offset-free, so the &bd[1]/
            // &v1d[1] pointer bindings ride temp copies (h014 note)
            val bTmp = CharArray(STR_MAX)
            val cTmp = CharArray(STR_MAX)
            copyC(bTmp, 0, svPtr.bd, 1)
            copyC(cTmp, 0, svPtr.v1d, 1)
            mpsJustify(tmp2, bTmp, cTmp)
            copyC(svPtr.bd, 1, bTmp, 0)

            svPtr.ierr = 0 // ex2
        }

        //-----------------------------------------------------------------------
        // $len()
        //-----------------------------------------------------------------------

        6 -> { // vendor 638-664
            if (nargs == 0) {
                svPtr.bd[1] = '0'
                svPtr.bd[2] = NUL
                return
            }

            strmove(tmp2, 0, svPtr.v1d, 1)

            if (nargs == 2) {
                var ii = 1
                var jj = 0
                val k = strlenC(svPtr.v1d, iargs[1])
                if (k == 0) {
                    rslt = 0
                    writeCstring(svPtr.bd, "0", 1) // ex1
                    return
                }
                val pat = copyOfC(svPtr.v1d, iargs[1])
                while (true) {
                    ii = xindex(tmp2, pat, ii.toShort())
                    if (ii == 0) break
                    jj++
                    ii += k
                }
                rslt = jj + 1
                writeCstring(svPtr.bd, rslt.toString(), 1) // ex1
                return
            }

            if (nargs > 1) {
                fcnArgCount(svPtr)
                return
            }

            rslt = strlenC(tmp2)
            writeCstring(svPtr.bd, rslt.toString(), 1) // ex1
        }

        //-----------------------------------------------------------------------
        // $piece()
        //-----------------------------------------------------------------------

        7 -> { // vendor 666-706
            val ONE_C = charArrayOf('1', NUL)

            if (nargs == 2) {
                if (svPtr.setpiece != 0) {
                    _piece(svPtr.setname, copyOfC(svPtr.v1d, 1), copyOfC(svPtr.v1d, iargs[1]),
                           ONE_C, ONE_C, 1, copyOfC(svPtr.pd1, svPtr.sdlim), svPtr)
                } else {
                    val out = CharArray(STR_MAX) // vendor &bd[1] out
                    _piece(out, copyOfC(svPtr.v1d, 1), copyOfC(svPtr.v1d, iargs[1]),
                           ONE_C, ONE_C, 0, copyOfC(svPtr.bd, 1), svPtr)
                    strmove(svPtr.bd, 1, out, 0)
                }
                svPtr.ierr = 0 // ex2
                return
            }

            if (nargs == 3) {
                if (svPtr.setpiece != 0) {
                    _piece(svPtr.setname, copyOfC(svPtr.v1d, 1), copyOfC(svPtr.v1d, iargs[1]),
                           copyOfC(svPtr.v1d, iargs[2]), copyOfC(svPtr.v1d, iargs[2]),
                           1, copyOfC(svPtr.pd1, svPtr.sdlim), svPtr)
                } else {
                    val out = CharArray(STR_MAX)
                    _piece(out, copyOfC(svPtr.v1d, 1), copyOfC(svPtr.v1d, iargs[1]),
                           copyOfC(svPtr.v1d, iargs[2]), copyOfC(svPtr.v1d, iargs[2]),
                           0, copyOfC(svPtr.bd, 1), svPtr)
                    strmove(svPtr.bd, 1, out, 0)
                }
                svPtr.ierr = 0 // ex2
                return
            }

            if (nargs != 4) {
                fcnArgCount(svPtr)
                return
            }

            if (svPtr.setpiece != 0) {
                _piece(svPtr.setname, copyOfC(svPtr.v1d, 1), copyOfC(svPtr.v1d, iargs[1]),
                       copyOfC(svPtr.v1d, iargs[2]), copyOfC(svPtr.v1d, iargs[3]),
                       1, copyOfC(svPtr.pd1, svPtr.sdlim), svPtr)
            } else {
                val out = CharArray(STR_MAX)
                _piece(out, copyOfC(svPtr.v1d, 1), copyOfC(svPtr.v1d, iargs[1]),
                       copyOfC(svPtr.v1d, iargs[2]), copyOfC(svPtr.v1d, iargs[3]),
                       0, copyOfC(svPtr.bd, 1), svPtr)
                strmove(svPtr.bd, 1, out, 0)
            }

            svPtr.ierr = 0 // ex2
        }

        //-----------------------------------------------------------------------
        // $select() $storage
        //-----------------------------------------------------------------------

        8 -> { // vendor 708-730
            if (nargs == 0) { // $storage
                rslt = svPtr.PD1
                writeCstring(svPtr.bd, rslt.toString(), 1)
                svPtr.ierr = 0 // ex2
                return
            }

            //----------
            // $select()
            //----------

            var si = 0
            while (true) { // sel1
                strmove(tmp2, 0, svPtr.v1d, iargs[si]) // scan for a true result
                j = xindex(tmp2, cod209, 1) - 1
                if (j <= 0) { // err
                    fcnErr(svPtr)
                    return
                }
                if (tmp2[j - 1] == '0') {
                    si++
                    if (si >= nargs) {
                        fcnErr(svPtr)
                        return
                    }
                } else {
                    strmove(svPtr.bd, 1, tmp2, j + 1)
                    svPtr.ierr = 0 // ex2
                    return
                }
            }
        }

        //-----------------------------------------------------------------------
        // $test $text() $translate()
        //-----------------------------------------------------------------------

        9 -> { // vendor 732-860
            if (nargs == 0) { // $test
                if (svPtr.tpx == 1L) svPtr.bd[1] = '1' else svPtr.bd[1] = '0'
                svPtr.bd[2] = NUL
                svPtr.ierr = 0 // ex2
                return
            }

            if (S.uppercaseChar() == 'R') { // trns: $translate() - see below
                if (nargs < 2 || nargs > 3) {
                    fcnArgCount(svPtr)
                    return
                }

                if (nargs == 2) { // 2 argument form
                    val r = CharArray(256)
                    strmove(r, 0, svPtr.v1d, iargs[1])

                    val rs = cstringAt(r)
                    var jj = 1
                    var ii = 1
                    while (svPtr.v1d[ii] != NUL) {
                        if (rs.indexOf(svPtr.v1d[ii]) < 0) {
                            svPtr.bd[jj] = svPtr.v1d[ii]
                            jj++
                        }
                        ii++
                    }
                    svPtr.bd[jj] = NUL
                    svPtr.ierr = 0 // ex2
                    return
                }

                // 3 argument form
                val r = CharArray(256)
                val s = CharArray(256)
                val t = CharArray(256)
                t[0] = NUL

                strmove(r, 0, svPtr.v1d, iargs[1])
                strmove(s, 0, svPtr.v1d, iargs[2])

                if (strlenC(r) > strlenC(s)) {
                    val ri = strlenC(s)
                    strmove(t, 0, r, ri)
                }

                val rs = cstringAt(r)
                val ts = cstringAt(t)
                var jj = 1
                var ii = 1
                while (svPtr.v1d[ii] != NUL) {
                    if (ts.indexOf(svPtr.v1d[ii]) < 0) {
                        val rp = rs.indexOf(svPtr.v1d[ii])
                        if (rp < 0) {
                            svPtr.bd[jj] = svPtr.v1d[ii]
                            jj++
                        } else {
                            svPtr.bd[jj] = s[rp] // s[strchr(r,c) - r]
                            jj++
                        }
                    }
                    ii++
                }
                svPtr.bd[jj] = NUL
                svPtr.ierr = 0 // ex2
                return
            }

            //-----------------------------------------------------------------------
            // $text()
            //-----------------------------------------------------------------------

            if (nargs != 1) {
                fcnArgCount(svPtr)
                return
            }

            i = atoiC(svPtr.v1d, 1)
            if (i <= 0) {
                svPtr.bd[1] = NUL
                svPtr.ierr = 0 // ex2
                return
            }
            var jj = 1
            var kk = 1
            while (kk != i) {
                jj = jj + strlenC(svPtr.pd1, jj) + 1
                if (jj > svPtr.pd1len) {
                    svPtr.bd[1] = NUL
                    svPtr.ierr = 0 // ex2
                    return
                }
                kk++
            }
            var iw = 1
            while (svPtr.pd1[jj] != NUL) {
                if (svPtr.pd1[jj] != TAB_C && svPtr.pd1[jj] != ' ') {
                    svPtr.bd[iw] = svPtr.pd1[jj]
                    iw++
                    jj++
                } else {
                    svPtr.bd[iw] = ' '
                    iw++
                    jj++
                }
            }
            svPtr.bd[iw] = NUL
            svPtr.ierr = 0 // ex2
        }

        //-----------------------------------------------------------------------
        // $random() $reverse()
        //-----------------------------------------------------------------------

        10 -> { // vendor 862-912
            if (nargs != 1) {
                fcnArgCount(svPtr)
                return
            }

            if (S.lowercaseChar() == 'e') { // $reverse()
                var jj = strlenC(svPtr.v1d, 1) + 1
                svPtr.bd[jj] = NUL
                var ii = 1
                while (svPtr.v1d[ii] != NUL) {
                    jj--
                    svPtr.bd[jj] = svPtr.v1d[ii]
                    ii++
                }
                return
            }

            // h018: the MULTI_PRECISION GMP random block is compiled
            // out of the reference binary (documented, not translated)

            val jj = atoiC(svPtr.v1d, 1)

            if (jj < 2) {
                svPtr.bd[1] = '0'
                svPtr.bd[2] = NUL
                return
            }

            writeCstring(svPtr.bd, kotlin.random.Random.nextInt(jj).toString(), 1) // rand()%j
        }

        11 -> { // $x - vendor 914
            writeCstring(svPtr.bd, svPtr.hor[svPtr.io].toString(), 1)
            svPtr.ierr = 0 // ex2
        }

        12 -> { // $y - vendor 916
            writeCstring(svPtr.bd, svPtr.ver[svPtr.io].toString(), 1)
            svPtr.ierr = 0 // ex2
        }

        13 -> { // $io - vendor 918
            writeCstring(svPtr.bd, svPtr.io.toString(), 1)
            svPtr.ierr = 0 // ex2
        }

        14 -> { // $horolog - vendor 920-932
            var timex = System.currentTimeMillis() / 1000L // time(&timex)
            var d = timex / 86400L
            timex = timex - d * 86400L
            d = 47116L + d
            day = d + 1L
            // sprintf(bd,"%ld",fd); strcat(bd,","); sprintf(tmp2,"%ld",timex);
            // strcat(bd,tmp2) - fused into one write of the same bytes
            writeCstring(svPtr.bd, day.toString() + "," + timex, 1)
            return
        }

        //------------------------
        // $get()
        //------------------------

        15 -> { // vendor 934-968
            if (nargs < 1 || nargs > 3) {
                fcnArgCount(svPtr)
                return
            }

            // the following lines are unknown (vendor comment kept)

            if (svPtr.v1d[1] == 3.toChar()) {
                if (svPtr.v1d[2] == 2.toChar()) {
                    if (nargs == 1) {
                        svPtr.bd[1] = NUL // strmove(&bd[1], "")
                        svPtr.ierr = 0 // ex2
                        return
                    }
                    strmove(svPtr.bd, 1, svPtr.v1d, iargs[1])
                    svPtr.ierr = 0 // ex2
                    return
                }
                copyC(svPtr.bd, 1, svPtr.v1d, 2)
                svPtr.ierr = 0 // ex2
                return
            }

            // actual $get code

            val ctmp = sym_(1, svPtr.v1d, 1, svPtr.bd, 1, svPtr)

            if (ctmp != null) { // value returned and in bd
                svPtr.ierr = 0 // ex2
                return
            }

            if (nargs == 1) {
                svPtr.bd[1] = NUL // ctmp == NULL here
                svPtr.ierr = 0 // ex2
                return
            }

            if (nargs != 2) {
                fcnArgCount(svPtr)
                return
            }

            strmove(svPtr.bd, 1, svPtr.v1d, iargs[1])
            svPtr.ierr = 0 // ex2
        }

        //-----------------------------------------------------------------------
        // Pattern match codes
        //-----------------------------------------------------------------------

        in 21..29 -> { // vendor 970-994: $1 ... $9
            val x = charArrayOf('_', j.toChar(), S, NUL) // x[0]='_', x[1]=j, x[2]=S
            svPtr.ierr = 0
            if (sym_(1, x, 0, svPtr.bd, 1, svPtr) == null) {
                System.out.print(
                    "\n*** " + cstringAt(x) +
                        ": No such Perl back reference in or near line " +
                        svPtr.LineNumber + "\n\n"
                )
                sigint(100)
            }
            return
        }

        //----------------------------
        // $name()
        // $next()
        // $noerr
        //----------------------------

        30 -> { // vendor 996-1036
            if (S.uppercaseChar() == 'O') { // $noerr
                writeCstring(svPtr.bd, svPtr.NOERR.toString(), 1)
                svPtr.ierr = 0
                return
            }

            if (S.uppercaseChar() == 'A') { // $name() section
                if (cstringAt(svPtr.v1d, 1) == "<lcl>") {
                    BuildLocal(2, 0, EMPTY_C, 0, tmp2, svPtr)
                    if (nargs == 1) LocalName(tmp2, 0, svPtr.bd, 1, null, 0, svPtr)
                    else LocalName(tmp2, 0, svPtr.bd, 1, svPtr.v1d, iargs[1], svPtr)
                } else if (cstringAt(svPtr.v1d, 1) == "<gbl>") {
                    // globals processed by compiler have x01 codes embedded.
                    // use LocalName() here because interpreter does not
                    // embed x01's at this point. (vendor comment kept)
                    BuildGlobal(2, 0, EMPTY_C, 0, tmp2, svPtr)
                    if (nargs == 1) LocalName(tmp2, 0, svPtr.bd, 1, null, 0, svPtr)
                    else LocalName(tmp2, 0, svPtr.bd, 1, svPtr.v1d, iargs[1], svPtr)
                }
                svPtr.ierr = 0
                return
            }

            // $next() section

            if (S.uppercaseChar() != 'E' && S.isLetter()) { // err
                fcnErr(svPtr)
                return
            }

            if (cstringAt(svPtr.v1d, 1) == "<gbl>") {
                if (BuildGlobal(1, NEXT, svPtr.v1d, 1, svPtr.bd, svPtr, 1) == 0) {
                    fcnErr(svPtr)
                    return
                }
                if (strlenC(svPtr.bd, 1) == 0) writeCstring(svPtr.bd, "-1", 1)
            } else {
                if (BuildLocal(1, NEXT, svPtr.v1d, 1, svPtr.bd, svPtr, 1) == 0) {
                    fcnErr(svPtr)
                    return
                }
            }
            svPtr.ierr = 0
            return
        }

        32 -> { // $order() - vendor 1038-1064
            val ONE_C = charArrayOf('1', NUL)
            val MINUS1_C = charArrayOf('-', '1', NUL)

            if (nargs == 1) {
                if (svPtr.v1d[1] == '^') {
                    GlobalOrder(svPtr.v1d, 1, svPtr.bd, 1, ONE_C, 0, svPtr)
                    svPtr.ierr = 0 // ex2
                    return
                }
                if (BuildLocal(1, ORDERNEXT, svPtr.v1d, 1, svPtr.bd, svPtr, 1) == 0) {
                    fcnErr(svPtr)
                    return
                }
                svPtr.ierr = 0 // ex2
                return
            }

            if (nargs != 2) {
                fcnErr(svPtr)
                return
            }

            if (cstringAt(svPtr.v1d, iargs[1]) == "1") {
                if (svPtr.v1d[1] == '^') {
                    GlobalOrder(svPtr.v1d, 1, svPtr.bd, 1, ONE_C, 0, svPtr)
                    svPtr.ierr = 0 // ex2
                    return
                }
                if (BuildLocal(1, ORDERNEXT, svPtr.v1d, 1, svPtr.bd, svPtr, 1) == 0) {
                    fcnErr(svPtr)
                    return
                }
                svPtr.ierr = 0 // ex2
                return
            }

            if (cstringAt(svPtr.v1d, iargs[1]) == "-1") {
                if (svPtr.v1d[1] == '^') {
                    GlobalOrder(svPtr.v1d, 1, svPtr.bd, 1, MINUS1_C, 0, svPtr)
                    svPtr.ierr = 0 // ex2
                    return
                }
                if (BuildLocal(1, ORDERPREV, svPtr.v1d, 1, svPtr.bd, svPtr, 1) == 0) {
                    fcnErr(svPtr)
                    return
                }
                svPtr.ierr = 0 // ex2
                return
            }

            fcnErr(svPtr) // err
        }

        31 -> { // $query(x) $qlength(x) $qsubscript(x,y) - vendor 1066-1192

            //-------------------------
            // $query()
            //-------------------------

            if (S.lowercaseChar() == 'u' || !S.isLetter()) { // $query()
                if (nargs > 1 || nargs == 0) {
                    fcnArgCount(svPtr)
                    return
                }

                if (cstringAt(svPtr.v1d, 1) == "<lcl>") {
                    BuildLocal(2, 0, EMPTY_C, 0, tmp2, svPtr)
                    LocalName(tmp2, 0, svPtr.bd, 1, null, 0, svPtr)
                    strmove(svPtr.v1d, 1, tmp2, 0) // strcpy(&v1d[1], tmp2)
                    val MINUS2_C = charArrayOf('-', '2', NUL)
                    Qsub(svPtr.v1d, 1, tmp2, 0, MINUS2_C, 0, svPtr)
                    var qi = 1
                    while (svPtr.v1d[qi] != 1.toChar() && svPtr.v1d[qi] != NUL) qi++
                    if (svPtr.v1d[qi] == NUL) {
                        svPtr.v1d[qi] = 1.toChar()
                        qi++
                        svPtr.v1d[qi] = NUL
                    }
                    sym_(101, svPtr.v1d, 1, svPtr.bd, 1, svPtr)
                    // GlobalName(&bd[1], 0, svPtr) - the owner-level
                    // GlobalName (unit 22) is offset-free; the &bd[1]
                    // binding rides a temp copy written back (h014 note)
                    val gn = copyOfC(svPtr.bd, 1)
                    GlobalName(gn, NUL, svPtr)
                    strmove(svPtr.bd, 1, gn, 0)
                    svPtr.ierr = 0 // ex2
                    return
                } else if (cstringAt(svPtr.v1d, 1) == "<gbl>") {
                    // globals processed by compiler have x01 codes embedded.
                    // use LocalName() here because interpreter does not
                    // embed x01's at this point. (vendor comment kept)
                    BuildGlobal(2, 0, EMPTY_C, 0, tmp2, svPtr)
                    LocalName(tmp2, 0, svPtr.bd, 1, null, 0, svPtr)
                }

                // GlobalQuery(&bd[1], &bd[1], 0, svPtr) - the owner-level
                // GlobalQuery (unit 98) is offset-free; the &bd[1] in/out
                // binding rides a temp copy written back (h014 note)
                val gq = copyOfC(svPtr.bd, 1)
                GlobalQuery(gq, gq, NUL, svPtr)
                strmove(svPtr.bd, 1, gq, 0)
                svPtr.ierr = 0 // ex2
                return
            }

            //-----------------------
            // $qlength()
            //-----------------------

            if (S.lowercaseChar() == 'l') { // $qlength()
                val op1 = CharArray(512)

                if (nargs != 1) {
                    fcnArgCount(svPtr)
                    return
                }

                if (cstringAt(svPtr.v1d, 1) == "<lcl>") {
                    BuildLocal(2, 0, EMPTY_C, 0, op1, svPtr) // extract lcl stack
                    if (Eval(op1, op1, svPtr) != 0) { // qlenerr
                        System.out.print(
                            "\n*** Qlength Function argument error. In or near line " +
                                svPtr.LineNumber + "\n\n"
                        )
                        sigint(100)
                        return
                    }
                } else if (cstringAt(svPtr.v1d, 1) == "<gbl>") {
                    BuildGlobal(2, 0, EMPTY_C, 0, op1, svPtr) // get global indx nbr
                } else {
                    strmove(op1, 0, svPtr.v1d, 1)
                }

                Qlength(op1, 0, svPtr.bd, 1, svPtr)
                svPtr.ierr = 0 // ex2
                return
            }

            //------------------------
            // $qsubscript()
            //------------------------

            if (S.lowercaseChar() == 's') { // $qsubscript()
                if (nargs != 2) {
                    fcnArgCount(svPtr)
                    return
                }

                val op1 = CharArray(512)
                val op2 = CharArray(512)

                if (cstringAt(svPtr.v1d, iargs[1]) == "-1") {
                    // return current working directory
                    writeCstring(svPtr.bd, System.getProperty("user.dir"), 1) // getcwd
                    svPtr.ierr = 0 // ex2
                    return
                }

                // order of these operations is important because of the stack
                // do not change. (vendor comment kept)

                if (cstringAt(svPtr.v1d, iargs[1]) == "<lcl>") {
                    BuildLocal(2, 0, EMPTY_C, 0, op2, svPtr) // extract lcl stack
                    var qi = 0
                    while (op2[qi] != NUL) {
                        if (op2[qi].code == CODED_OPEN) op2[qi] = '('
                        else if (op2[qi].code == CODED_CLOSE) op2[qi] = ')'
                        else if (op2[qi].code == CODED_COMMA) op2[qi] = ','
                        qi++
                    }
                    if (Eval(op2, op2, svPtr) != 0) { // qsuberr1
                        fcnQsub1(svPtr)
                        return
                    }
                } else if (cstringAt(svPtr.v1d, iargs[1]) == "<gbl>") {
                    BuildGlobal(2, 0, EMPTY_C, 0, op2, svPtr) // get global indx nbr
                    if (Eval(op2, op2, svPtr) != 0) {
                        fcnQsub1(svPtr)
                        return
                    }
                } else {
                    strmove(op2, 0, svPtr.v1d, iargs[1])
                }

                if (cstringAt(svPtr.v1d, 1) == "<lcl>") {
                    BuildLocal(2, 0, EMPTY_C, 0, op1, svPtr) // extract lcl stack
                    if (Eval(op1, op1, svPtr) != 0) {
                        fcnQsub1(svPtr)
                        return
                    }
                } else if (cstringAt(svPtr.v1d, 1) == "<gbl>") {
                    BuildGlobal(2, 0, EMPTY_C, 0, op1, svPtr) // get global indx nbr
                } else {
                    strmove(op1, 0, svPtr.v1d, 1)
                }

                Qsub(op1, 0, svPtr.bd, 1, op2, 0, svPtr)

                svPtr.ierr = 0 // ex2
                return
            }

            fcnUnknown(svPtr) // function not recognized
        }

        //-----------------------------------------------------------------------
        // unknown code (vendor default: - shared with opc 99 above)
        //-----------------------------------------------------------------------

        else -> {
            svPtr.ierr = 30
            fcnUnknown(svPtr)
        }
    }
}

// ---------------------------------------------------------------------------
// file-private constants (defines.h / strmax.h pins; h004/h012)
// ---------------------------------------------------------------------------

private const val STR_MAX = 32000 // vendor strmax.h:3
private const val SYM_MAX = 200 // vendor defines.h:218
private const val SYMSTORE = 0 // vendor interp-fcn.cpp:91
private const val SYMRETRIEVE = 1 // vendor interp-fcn.cpp:92
private const val RETRIEVE = 0 // vendor defines.h:83
private const val STORE = 1 // vendor defines.h:84
private const val NEXT = 2 // vendor defines.h:85
private const val ORDERNEXT = 12 // vendor defines.h:170
private const val ORDERPREV = 13 // vendor defines.h:171
private const val OPEN = 206 // vendor defines.h:169
private const val CODED_OPEN = 206 // vendor defines.h (CodedOpen)
private const val CODED_CLOSE = 207 // vendor defines.h:39 (CodedClose)
private const val CODED_COMMA = 208 // vendor defines.h:40 (CodedComma)

/** vendor interp.h:56 TAB ('\t'). */
private val TAB_C = 9.toChar()

/** vendor printf literal dollar byte (h015; cf. InterpPiece DOLLARC). */
private val DOLLAR = 36.toChar()

/** The C "" literal as a one-byte NUL buffer (BuildLocal/BuildGlobal key). */
private val EMPTY_C = charArrayOf(NUL)

/**
 * vendor 122-134: the 'const static unsigned char opcode[256]' dispatch
 * table (h004: file-scope static -> file-private val; 99 = invalid).
 */
private val OPCODE = IntArray(256) { 99 }.also {
    // vendor 122-134, row by row:
    //   row 40: ... 49 -> 21 ('1'); row 50:-57 -> 22..29 ('2'..'9');
    //   row 97: -> 2 ('a' $ascii), 99 -> 3 ('c' $char);
    //   row 101: -> 1 ('e' $extract), 102 -> 4 ('f' $find/$fnumber),
    //   103 -> 15 ('g' $get), 104 -> 14 ('h' $horolog), 105 -> 13
    //   ('i' $io), 106 -> 5 ('j' $justify/$job), 108 -> 6 ('l' $len);
    //   row 110: -> 30 ('n'), 111 -> 32 ('o' $order), 112 -> 7
    //   ('p' $piece), 113 -> 31 ('q' $query/$qlength/$qsubscript),
    //   114 -> 10 ('r' $random/$reverse), 115 -> 8 ('s' $select/
    //   $storage), 116 -> 9 ('t' $test/$text/$translate);
    //   row 120: -> 11 ('x'), 121 -> 12 ('y'), 122 -> 98 ('z')
    it[49] = 21
    it[50] = 22; it[51] = 23; it[52] = 24; it[53] = 25
    it[54] = 26; it[55] = 27; it[56] = 28; it[57] = 29
    it[97] = 2 // 'a' $ascii
    it[99] = 3 // 'c' $char
    it[101] = 1 // 'e' $extract
    it[102] = 4 // 'f' $find/$fnumber
    it[103] = 15 // 'g' $get
    it[104] = 14 // 'h' $horolog
    it[105] = 13 // 'i' $io
    it[106] = 5 // 'j' $justify/$job
    it[108] = 6 // 'l' $len
    it[110] = 30 // 'n' $name/$next/$noerr
    it[111] = 32 // 'o' $order
    it[112] = 7 // 'p' $piece
    it[113] = 31 // 'q' $query/$qlength/$qsubscript
    it[114] = 10 // 'r' $random/$reverse
    it[115] = 8 // 's' $select/$storage
    it[116] = 9 // 't' $test/$text/$translate
    it[120] = 11 // 'x' $x
    it[121] = 12 // 'y' $y
    it[122] = 98 // 'z' $z variables
}

// ---------------------------------------------------------------------------
// shared error tails (h011: every vendor label ends the function; the
// printf/sigint tails are raised as file-private helpers so each expanded
// goto site emits the exact vendor text)
// ---------------------------------------------------------------------------

/** vendor err tail (1196-1216 region). */
private fun fcnErr(svPtr: Msv) {
    System.out.print("\n*** Function error in or near line " + svPtr.LineNumber + "\n\n")
    sigint(100)
}

/** vendor arg_count_err tail. */
private fun fcnArgCount(svPtr: Msv) {
    System.out.print(
        "\n*** Function error: wrong number of arguments. In or near line " +
            svPtr.LineNumber + "\n\n"
    )
    sigint(100)
}

/** vendor unknown_err tail. */
private fun fcnUnknown(svPtr: Msv) {
    System.out.print("\n*** Unknown function. In or near line " + svPtr.LineNumber + "\n\n")
    sigint(100)
}

/** vendor fnum_wrong tail (the literal $fnumber rides the DOLLAR char). */
private fun fcnFnumWrong(svPtr: Msv) {
    System.out.print(
        "\n*** Function error: " + DOLLAR + "fnumber pattern error. In or near line " +
            svPtr.LineNumber + "\n\n"
    )
    sigint(100)
}

/** vendor qsuberr1 tail. */
private fun fcnQsub1(svPtr: Msv) {
    System.out.print(
        "\n*** Qsubscript Function error: 1nd argument. In or near line " +
            svPtr.LineNumber + "\n\n"
    )
    sigint(100)
}

// ---------------------------------------------------------------------------
// file-private C-library helpers (h007/h010/h015-checked)
// ---------------------------------------------------------------------------

/** C strcpy with offsets: copies the src C-string (incl. NUL). */
private fun copyC(dst: CharArray, dstOff: Int, src: CharArray, srcOff: Int) {
    var d = dstOff
    var s = srcOff
    while (src[s] != NUL) {
        dst[d] = src[s]
        d++
        s++
    }
    dst[d] = NUL
}

/** Returns a fresh offset-0 copy of the src C-string at off (Parse_ precedent). */
private fun copyOfC(src: CharArray, off: Int): CharArray {
    val b = CharArray(strlenC(src, off) + 1)
    copyC(b, 0, src, off)
    return b
}

/**
 * C strcat: appends the src C-string at srcOff to the dst NUL (optionally
 * at dstOff); String overload for the vendor literal-appends.
 */
private fun appendC(dst: CharArray, src: CharArray, srcOff: Int, dstOff: Int = 0) {
    var d = dstOff
    while (dst[d] != NUL) d++
    var s = srcOff
    while (src[s] != NUL) {
        dst[d] = src[s]
        d++
        s++
    }
    dst[d] = NUL
}

/** C strcat(dst, "literal"): appends s at dst's NUL (offset 0). */
private fun appendC(dst: CharArray, s: String) {
    writeCstring(dst, s, strlenC(dst))
}

/** C atol over a NUL-terminated region (vendor atol; Int domain per h010). */
private fun atolC(b: CharArray, off: Int = 0): Int {
    var k = off
    while (b[k] == ' ' || b[k] == '\t' || b[k] == '\n' ||
        b[k] == '\u000B' || b[k] == '\u000C' || b[k] == '\r') k++
    var sign = 1
    if (b[k] == '+' || b[k] == '-') {
        if (b[k] == '-') sign = -1
        k++
    }
    var r = 0
    while (b[k] in '0'..'9') {
        r = r * 10 + (b[k] - '0')
        k++
    }
    return sign * r
}

/** C atof over a NUL-terminated region (vendor atof; leading-double scan). */
private fun atofC(b: CharArray, off: Int = 0): Double {
    var k = off
    while (b[k] == ' ' || b[k] == '\t') k++
    var sign = 1.0
    if (b[k] == '+' || b[k] == '-') {
        if (b[k] == '-') sign = -1.0
        k++
    }
    var r = 0.0
    while (b[k] in '0'..'9') {
        r = r * 10 + (b[k] - '0')
        k++
    }
    if (b[k] == '.') {
        k++
        var f = 0.1
        while (b[k] in '0'..'9') {
            r += (b[k] - '0') * f
            f /= 10
            k++
        }
    }
    if (b[k] == 'e' || b[k] == 'E') {
        k++
        var esign = 1
        if (b[k] == '+' || b[k] == '-') {
            if (b[k] == '-') esign = -1
            k++
        }
        var e = 0
        while (b[k] in '0'..'9') {
            e = e * 10 + (b[k] - '0')
            k++
        }
        return sign * r * Math.pow(10.0, (esign * e).toDouble())
    }
    return sign * r
}
