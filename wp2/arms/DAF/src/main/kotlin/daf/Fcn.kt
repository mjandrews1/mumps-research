package daf

// Unit 123 (DAF B4 T3, complex): interp-fcn.cpp::fcn (775 LOC): the
// runtime \\$-function dispatcher: the \\$\\$FUNCTION indirection
// branch, the opcode[256] switch, and the 30+ intrinsic services
// (\\$extract, \\$ascii, \\$char, \\$fnumber/\\$find, \\$justify/\\$job,
// \\$len, \\$piece, \\$select/\\$storage, \\$test/\\$text/\\$translate,
// \\$random/\\$reverse, \\$x/\\$y/\\$io/\\$horolog, \\$get, the Perl
// back-references, \\$name/\\$next/\\$noerr, \\$query, \\$qlength,
// \\$qsubscript, \\$order, and the \\$z RDBMS variables).
// Consults: BASE-log row 101 hit-reuse (the v1d[0] sentinel + coded
// 206/207/208 argv carriage of the coded-command shape; the
// CODED_OPEN_D/CODED_COMMA_D/CODED_CLOSE_D consts seated by the unit-97
// render are reused verbatim), row 97 hit-reuse (the \\$piece service
// calls resolve to the unit-114 pieceDaf render of THIS tranche - the
// seat flip closes the T1/T2 stub trail), rows 8/81 hit-adapt
// (strmove/strcpy/strlen/sscanf of the CharArray carriers fold onto
// String slices and in-place left-shifts; sscanf "%ld" -> the atol atoi
// seat semantics: leading-numeric prefix parse), row 28 hit-reuse
// (atol/atoi family), row 25 hit-adapt (tolower/toupper/isalpha fold
// onto the ASCII-only ctyle seats), rows 69/2 hit-adapt (the printf
// error diagnostics on stdout and the sigint(100) relays; the error
// label tail chain renders as the exitLabel folds), row 10 hit-adapt
// (sel1/xindex walk loops; the ex1/ex2/err goto labels fold onto the
// run-label structure - ex1/ex2 are pure tails), switch->when
// MANUAL-DECISION (per the zero-switch study canonical form; the C
// switch with default renders as when->else), row 30 hit-adapt (the
// sym_/Mglobal out-buffer writes fold onto bd CharArray writes back),
// row 92 hit-reuse (sym_ retrieve-then-nonnull checks), row 33 family
// (strchr/index fold via String indexOf; %d/%X/%ld sprintf dialogs via
// String.format Locale.US), row 46 hit-adapt (SQLITE reference-build
// guard: the commented vendor dead code of \\$ztable kept as comment
// fold; rand/getpid/time/getcwd seats carry DOCUMENTED DIVERGENCES
// per their stub headers), row 79 (the strfmon path), row 26
// (format verbatim).
// Seats: pieceDaf (RESOLVES unit 114), globalQueryDaf (RESOLVES unit
// 119 in this tranche), LocalName (T2 unit 105 render), atTestDaf not
// used here; scanParseDaf (b8), InterpretDaf (b6), allocSVDaf (b8),
// symRetDaf (permanent), MglobalDaf (b6), buildLocalDaf/buildGlobalDaf
// (b8), globalOrderDaf (b2/b3/b9), globalNameDaf (b1), qsubDaf (b7),
// qlengthDaf (b5), sqlDaf (b9/b5), xindexDaf (b9/b10), mpsJustifyDaf
// (b3), strfmonD (libc stub), lpadDaf (arith stub), getpidD/randD/
// timeD/getcwdD (platform seats), sigintDaf (T1).
// SEAT FLIP: this render RESOLVES the dafFcn stub raised by the
// unit-97 T1 render (the stub deleted therefrom).
// Dafny: EXEMPT (MSV state-machine dispatcher over stub seats; T3
// spec coverage rides the unit-114 piece.dfy contract; journal row).

private val OPCODE_DAF: IntArray = IntArray(256).also { t ->
    val rows = arrayOf(
        intArrayOf(99, 99, 99, 99, 99, 99, 99, 99, 99, 99), //   0
        intArrayOf(99, 99, 99, 99, 99, 99, 99, 99, 99, 99), //  10
        intArrayOf(99, 99, 99, 99, 99, 99, 99, 99, 99, 99), //  20
        intArrayOf(99, 99, 99, 99, 99, 99, 99, 99, 99, 99), //  30
        intArrayOf(99, 99, 99, 99, 99, 99, 99, 99, 99, 21), //  40
        intArrayOf(22, 23, 24, 25, 26, 27, 28, 29, 99, 99), //  50
        intArrayOf(99, 99, 99, 99, 99, 99, 99, 99, 99, 99), //  60
        intArrayOf(99, 99, 99, 99, 99, 99, 99, 99, 99, 99), //  70
        intArrayOf(99, 99, 99, 99, 99, 99, 99, 99, 99, 99), //  80
        intArrayOf(99, 99, 99, 99, 99, 99, 2, 99, 3),      //  90
        intArrayOf(99, 1, 4, 15, 14, 13, 5, 99, 6, 99),    // 100
        intArrayOf(30, 32, 7, 31, 10, 8, 9, 99, 99, 99),   // 110
        intArrayOf(11, 12, 98, 99, 99, 99, 99, 99)         // 120
    )
    var p = 0
    for (r in rows) for (v in r) { t[p++] = v }
    // entries past the vendor initializer list remain 0 exactly as
    // the C static-storage zero fill (rows past /*120*/ are 0)
}

// char cod209[2] = { 209, 0 }
private const val COD209_D: Char = '\u00D1' // 209

fun dafFcn(sv: DafMsv) {

    val tmp1 = CharArray(2) // unsigned char tmp1[2] = { 0, 0 }
    var day: Long
    val tmp2 = CharArray(STR_MAX_DAF)
    val tmp3 = CharArray(STR_MAX_DAF)
    var rslt = 0
    val S: Char

    val iargs = IntArray(10)
    var nargs = 0
    var i: Long
    var j: Long

    // local buffer carriers (String snapshot reads per row 81)
    fun v1dAt(off: Int): String = DafCRuntime.cstringAt(sv.v1d, off)
    fun bdAt(off: Int): String = DafCRuntime.cstringAt(sv.bd, off)
    fun pd1At(off: Int): String = DafCRuntime.cstringAt(sv.pd1, off)
    fun bdSet(s: String) {
        var d = 1
        var q = 0
        while (q < s.length) { sv.bd[d++] = s[q++] }
        sv.bd[d] = NUL_D
    }
    fun bdClear() { sv.bd[1] = NUL_D }
    fun tmp2Set(s: String) {
        var d = 0
        var q = 0
        while (q < s.length) { tmp2[d++] = s[q++] }
        tmp2[d] = NUL_D
    }

    fun strmoveShift() { // strmove(&v1d[1], &v1d[2]) in-place left shift
        var q = 1
        while (q + 1 < sv.v1d.size) { sv.v1d[q] = sv.v1d[q + 1]; q++ }
    }

    fun strmoveBdLocal(src: CharArray) { // strcpy(&bd[1], tmp)
        var d = 1
        var q = 0
        while (q < src.size && at(src, q) != NUL_D) {
            sv.bd[d++] = src[q]; q++
        }
        sv.bd[d] = NUL_D
    }

    fun tmp3SetLocal(s: String) { // strmove(tmp3, ...) fold
        var d = 0
        var q = 0
        while (q < s.length) { tmp3[d++] = s[q]; q++ }
        tmp3[d] = NUL_D
    }

    // the error-label tails: printf + sigint(100) (rows 69/2; the
    // vendor chain falls through the labels - sigint exits, render
    // returns after the relay exactly as the sigint contract)
    fun fcnErr(msg: String) {
        print(msg)
        sigintDaf(100)
    }
    fun unknownErr() = fcnErr("\n*** Unknown function. In or near line " +
            "${sv.LineNumber}\n\n")
    fun argCountErr() = fcnErr("\n*** Function error: wrong number of " +
            "arguments. In or near line ${sv.LineNumber}\n\n")
    fun err() = fcnErr("\n*** Function error in or near line " +
            "${sv.LineNumber}\n\n")

    run { // ex1/ex2/return-label block (row 10 tail fold)

        //----------------
        // \$\$FUNCTION
        //----------------

        if (sv.v1d[1] == '\$' && sv.v1d[2] == '\$') {

            sv.xpx++
            val org = sv.xpx
            val end = scanParseDaf(sv.xd) + sv.xpx
            val hold = at(sv.xd, end)
            sv.xd[end] = NUL_D
            var tcmd = StringBuilder("do ")
            tcmd.append(v1dAt(3)) // strcat(tcmd, &v1d[3])

            // look for ^ in function call - if not, function is local
            var ca = sv.xpx
            while (at(sv.xd, ca) != NUL_D) {
                if (sv.xd[ca] == '^') break
                ca++
            }

            if (at(sv.xd, ca) == NUL_D) {
                var ix = 3
                while (at(sv.v1d, ix) != NUL_D) {
                    if (sv.v1d[ix] == CODED_OPEN_D) break
                    ix++
                }
                val argFlg = at(sv.v1d, ix) != NUL_D // j==0 means args
                sv.v1d[ix] = NUL_D
                var tmp = StringBuilder("do ")
                tmp.append(v1dAt(3))   // label
                tmp.append("^")        // caret
                tmp.append(DafCRuntime.cstring(sv.CurrentFile)) // dir
                if (argFlg) { // attach argument
                    sv.v1d[ix] = '('
                    tmp.append(v1dAt(ix))
                    tmp.setLength(tmp.length - 1)
                    tmp.append(")")
                }
                tcmd = StringBuilder(tmp.toString()) // strcpy(tcmd,tmp)
            } else {
                tcmd.append(DafCRuntime.cstringAt(sv.xd, org)) // &svPtr->xd[org]
                sv.xd[end] = hold
            }

            // create environment and launch command
            val svPtr1 = allocSVDaf()
            svPtr1._Sym = sv._Sym
            svPtr1.LineNumber = sv.LineNumber
            for (sx in 0 until SYM_MAX_D) svPtr1.start[sx] = sv.start[sx]
            svPtr1.nstart = sv.nstart

            sv.ierr = InterpretDaf(tcmd.toString(), svPtr1)

            for (sx in 0 until SYM_MAX_D) sv.start[sx] = svPtr1.start[sx]

            tmp2Set(DafCRuntime.cstring(svPtr1.args))

            // (SQLITE block empty in the vendor body - folded)

            if (sv.ierr != 0) { err(); return@run }

            sv.xpx = end - 1
            strmoveBdLocal(tmp2) // strcpy(&bd[1], tmp2)
            return@run
        }

        sv.ierr = 0
        nargs = 0
        iargs[0] = 1
        j = sv.v1d[2].code.toLong() // hold function id
        S = sv.v1d[3]

        tmp2Set(v1dAt(1))

        while (sv.v1d[1] != CODED_OPEN_D && sv.v1d[1] != NUL_D) {
            strmoveShift()
        }

        if (sv.v1d[1] == CODED_OPEN_D) strmoveShift()

        if (j == CODED_OPEN_D.code.toLong()) { // noname form - parms only
            copyToSv(sv.args, v1dAt(1)) // strcpy(svPtr->args, &v1d[1])
            sv.ierr = 0
            return@run // goto ex2
        }

        var ix = 1
        while (at(sv.v1d, ix) != NUL_D) {
            if (sv.v1d[ix] == CODED_CLOSE_D || sv.v1d[ix] == CODED_COMMA_D) {
                sv.v1d[ix] = NUL_D
                iargs[++nargs] = ix + 1
            }
            ix++
        }

        j = toLowerAscii(j.toInt().toChar()).code.toLong() // lower case conversion

        val op = OPCODE_DAF[j.toInt()]
        if (op == 99) {
            sv.ierr = 30
            unknownErr()
            return@run
        }

        when (op) {

        98 -> { // \$z variables

            val t2s = DafCRuntime.cstring(tmp2)
            if (t2s.equals("\\\$ztable", ignoreCase = true)) {
                // (vendor SQLITE dead code kept as comment fold:
                //   sql(CLOSE, svPtr, NULL, NULL, NULL, NULL);
                //   strcpy(svPtr->Table, &pd1[sdlim]);
                //   sql(OPEN, svPtr, NULL, NULL, NULL, NULL);)
                sv.ierr = 0
                return@run // goto ex2
            }

            if (t2s.equals("\\\$ztabsize", ignoreCase = true)) {
                // (SQLITE reference build: live)
                sqlDaf(SQL_OPEN_D, sv, null, null, null, null)
                sv.TabSize = atoi(pd1At(sv.sdlim))
                if (sv.TabSize > 22) { unknownErr(); return@run }
                if (sv.TabSize < 1) { unknownErr(); return@run }
                sv.ierr = 0
                return@run
            }

            if (t2s.equals("\\\$zsqloutput", ignoreCase = true)) {
                copyToSv(sv.sqloutput, pd1At(sv.sdlim))
                sv.ierr = 0
                return@run
            }

            print("\n*** Unrecognized Z-type variable.\n${t2s}\n" +
                    "In or near line number ${sv.LineNumber}\n\n")
            kotlin.system.exitProcess(1) // exit(EXIT_FAILURE)
        }

        1 -> { // \$extract

            if (nargs == 1) { // no 2nd/3rd operand
                if (sv.setpiece != 0) { // LHS ref

                    if (sv.setname[0] == '^') { // global
                        var tmp2l = CharArray(STR_MAX_DAF)
                        var f = MglobalDaf(RETRIEVE_D, sv.setname, tmp2l, sv)
                        if (f != 0) {
                            var t1 = pd1At(sv.sdlim)
                            if (DafCRuntime.cstring(tmp2l).isNotEmpty()) {
                                t1 += DafCRuntime.cstringAt(tmp2l, 1)
                            }
                            copySvTo(tmp2l, t1)
                            MglobalDaf(STORE_D, sv.setname, tmp2l, sv)
                            sv.ierr = 0
                            return@run
                        } else {
                            var t1 = pd1At(sv.sdlim)
                            copySvTo(tmp2l, t1)
                            MglobalDaf(STORE_D, sv.setname, tmp2l, sv)
                            sv.ierr = 0
                            return@run
                        }
                    } else { // local variable reference
                        val ctmp = symRetDaf(SYMRETRIEVE_D, sv.setname, 0,
                                             sv.bd, 1, sv)
                        if (ctmp != null) {
                            var t1 = pd1At(sv.sdlim)
                            if (ctmp.isNotEmpty()) t1 += ctmp.substring(1)
                            bdSet(t1)
                        } else {
                            bdSet(pd1At(sv.sdlim))
                            symRetDaf(SYMSTORE_D, sv.setname, 0,
                                     sv.bd, 1, sv)
                        }
                        sv.ierr = 0
                        return@run
                    }
                }

                // not LHS \$e() - RHS function
                sv.bd[1] = at(sv.v1d, 1) // return first char
                sv.bd[2] = NUL_D
                sv.ierr = 0
                return@run
            }

            if (nargs != 2 && nargs != 3) { argCountErr(); return@run }

            i = 0
            i = atolD(v1dAt(iargs[1])) // arg 2 (sscanf %ld fold)

            if (i <= 0) i = 1

            if (nargs != 3) {
                j = i // 2nd operand only - return one char
            } else {
                j = atolD(v1dAt(iargs[2])) // arg 3
            }

            if (j <= 0) j = 0 // return null string

            // RHS function where end before start
            if (sv.setpiece == 0 && (i >= iargs[1] || j < i)) {
                bdClear() // empty string
                sv.ierr = 0
                return@run
            }

            // LHS where end before start
            if (sv.setpiece != 0 && j < i) {
                val tmp1s = ""
                if (sv.setname[0] == '^') { // global
                    val w = CharArray(STR_MAX_DAF); copySvTo(w, tmp1s)
                    MglobalDaf(STORE_D, sv.setname, w, sv)
                } else { // local
                    bdSet(tmp1s)
                    symRetDaf(SYMSTORE_D, sv.setname, 0, sv.bd, 1, sv)
                }
                sv.ierr = 0
                return@run
            }

            // if j exceeds str length, make it str length - RHS
            if (sv.setpiece == 0 && j > iargs[1]) j = iargs[1].toLong()

            if (i < 1) i = 1

            // 3 argument forms - LHS ref
            if (sv.setpiece != 0) {
                var tmp1l: String
                if (sv.setname[0] == '^') { // global array reference
                    var tmp2l = CharArray(STR_MAX_DAF)
                    var f = MglobalDaf(RETRIEVE_D, sv.setname, tmp2l, sv)
                    if (f != 0) { // global exists
                        var ctmp = DafCRuntime.cstring(tmp2l) // gbl array value
                        if (ctmp.length < i) {
                            while (ctmp.length < j) ctmp += " " // longer
                        }
                        tmp2l[i.toInt() - 1] = NUL_D // truncate
                        val RHS = pd1At(sv.sdlim)
                        var t2s = DafCRuntime.cstring(tmp2l)
                        if (ctmp.isNotEmpty()) t2s += RHS // insert RHS
                        if (ctmp.length >= j) t2s += ctmp.substring(j.toInt())
                        copySvTo(tmp2l, t2s)
                        MglobalDaf(STORE_D, sv.setname, tmp2l, sv)
                        sv.ierr = 0
                        return@run
                    } else { // global does not exist
                        var t1 = ""
                        for (k in 0 until i - 1) t1 += " "
                        t1 += pd1At(sv.sdlim)
                        val w = CharArray(STR_MAX_DAF); copySvTo(w, t1)
                        MglobalDaf(STORE_D, sv.setname, w, sv)
                        sv.ierr = 0
                        return@run
                    }
                } else { // local variable
                    val ctmp = symRetDaf(SYMRETRIEVE_D, sv.setname, 0,
                                         sv.bd, 1, sv)
                    if (ctmp != null) { // variable exists
                        var t2s = ctmp
                        if (t2s.length < i) {
                            while (t2s.length < j) t2s += " "
                        }
                        val sb = t2s.toCharArray()
                        sb[i.toInt() - 1] = NUL_D // truncate
                        var t2f = DafCRuntime.cstring(sb)
                        val RHS = pd1At(sv.sdlim)
                        if (ctmp.isNotEmpty()) t2f += RHS
                        if (ctmp.length >= j) t2f += ctmp.substring(j.toInt())
                        bdSet(t2f)
                        symRetDaf(SYMSTORE_D, sv.setname, 0, sv.bd, 1, sv)
                        sv.ierr = 0
                        return@run
                    } else { // create and append
                        var t1 = ""
                        for (k in 0 until i - 1) t1 += " "
                        t1 += pd1At(sv.sdlim)
                        bdSet(t1)
                        symRetDaf(SYMSTORE_D, sv.setname, 0, sv.bd, 1, sv)
                        sv.ierr = 0
                        return@run
                    }
                }
            }

            // RHS ref
            var k = 1
            var ii = i
            while (ii <= j) {
                sv.bd[k++] = at(sv.v1d, ii.toInt())
                ii++
            }
            sv.bd[k] = NUL_D
            sv.ierr = 0
        }

        2 -> { // \$ascii
            if (nargs > 2) { argCountErr(); return@run }
            i = if (nargs == 2) atolD(v1dAt(iargs[1])) else 1
            if (i >= iargs[1] || i <= 0) {
                sv.bd[1] = '-'
                sv.bd[2] = '1'
                sv.bd[3] = NUL_D
                return@run
            }
            rslt = at(sv.v1d, i.toInt()).code
            if (rslt == 0) rslt = -1
            bdSet(String.format(java.util.Locale.US, "%d", rslt)) // ex1
        }

        3 -> { // \$char
            sv.bd[1] = NUL_D
            var jj = 1
            for (ii in 1..nargs) {
                tmp2Set(v1dAt(jj))
                day = atolD(DafCRuntime.cstring(tmp2))
                if (day <= 0) day = 0
                tmp1[0] = day.toInt().toChar()
                tmp1[1] = NUL_D
                bdSet(bdAt(1) + DafCRuntime.cstring(tmp1))
                jj = iargs[ii]
            }
            return@run
        }

        4 -> { // \$find() \$fnumber()
            if (toLowerAscii(S) == 'n') { // \$fnumber()
                var dp = -1
                var plus = false; var minus = false
                var comma = false; var t = false; var p = false
                var t1 = false
                if (nargs != 3 && nargs != 2) { argCountErr(); return@run }
                if (nargs == 3) dp = atoi(v1dAt(iargs[2]))
                val x = DafCRuntime.strtod(v1dAt(1)) // value to convert
                if (at(sv.v1d, iargs[1]) == '*') { // arg is for strfmon
                    bdSet(strfmonD(v1dAt(iargs[1] + 1), x))
                    return@run
                }
                val pat = v1dAt(iargs[1])
                if (pat.contains('+')) plus = true
                if (pat.contains('-')) minus = true
                if (pat.contains(',')) comma = true
                if (pat.contains('T')) t = true
                if (pat.contains('t')) t = true
                if (pat.contains('P')) p = true
                if (pat.contains('p')) p = true

                if (p && (plus || minus || t)) {
                    fcnErr("\n*** Function error: \\\$fnumber pattern " +
                            "error. In or near line ${sv.LineNumber}\n\n")
                    return@run
                }

                var str = "%!"           // format suppress currency sym
                var xx = x
                if (t) { // trailing sign
                    if (xx < 0) t1 = true
                    xx = Math.abs(xx)
                }
                if (!comma) str += "^"   // no grouping chars
                if (p) str += "("        // use parens for neg
                if (minus) xx = Math.abs(xx) // suppress minus

                if (dp != -1) {
                    str += ".${dp}"
                } else {
                    var q = 1
                    while (at(sv.v1d, q) != NUL_D && sv.v1d[q] != '.') q++
                    if (at(sv.v1d, q) == NUL_D) {
                        str += ".0"
                    } else {
                        val nDigits = DafCRuntime.cstringAt(sv.v1d, 1).length - q
                        str += ".${nDigits}"
                    }
                }

                str += "i"

                // strfmon(&rstr[1], 256, str, x): the seat returns the
                // formatted body; the rstr[0] sign slot folds below
                val rstrBody = strfmonD(str, xx)
                val signSlot = if (plus && xx > 0 && !minus && !t) '+' else ' '
                var rstr = signSlot + rstrBody

                if (t) {
                    if (t1) rstr += "-"
                    else if (plus) rstr += "+"
                }

                if (rstr[0] == ' ') bdSet(rstr.substring(1)) else bdSet(rstr)
                return@run
            }

            // \$find
            if (nargs != 2 && nargs != 3) { argCountErr(); return@run }

            tmp2Set(v1dAt(1))
            tmp3SetLocal(v1dAt(iargs[1]))

            i = if (nargs == 2) 1 else atolD(v1dAt(iargs[2]))

            var m = i.toInt()
            if (at(tmp3, 0) != NUL_D) {
                val xr = xindexDaf(tmp2, 0, tmp3, 0, i.toInt())
                if (xr > 0) m = xr + DafCRuntime.cstring(tmp3).length
            }

            if (m > DafCRuntime.cstring(tmp2).length + 1) m = 0
            rslt = m
            bdSet(String.format(java.util.Locale.US, "%d", rslt)) // ex1
        }

        5 -> { // \$justify() \$job
            if (nargs == 0) { // \$job
                bdSet(String.format(java.util.Locale.US, "%X", getpidD()))
                sv.ierr = 0
                return@run
            }

            if (nargs < 2 || nargs > 3) { argCountErr(); return@run }

            var l = atoi(v1dAt(iargs[1]))

            bdSet(v1dAt(1)) // strmove(&bd[1], &v1d[1])

            if (nargs != 3) { // 2 arg form: pad with spaces or truncate
                var k = bdAt(1).length
                if (k < l) {
                    if (l > 255) l = 255
                    lpadDaf(sv.bd, sv.bd, l) // lpad(&bd[1], &bd[1], l)
                }
                sv.ierr = 0
                return@run
            }

            val k = atoi(v1dAt(iargs[2])) // arg 3

            // (MULTI_PRECISION dead code comment-fold; reference build
            // uses the %c%d%c%df BIGFLOAT-free variant)
            val fmt = "%${l}.${k}f"
            bdSet(mpsJustifyDaf(fmt, sv.v1d))
            sv.ierr = 0
            return@run
        }

        6 -> { // \$len()
            if (nargs == 0) {
                sv.bd[1] = '0'
                sv.bd[2] = NUL_D
                return@run
            }

            tmp2Set(v1dAt(1))

            if (nargs == 2) {
                val keyArr = CharArray(STR_MAX_DAF)
                copySvTo(keyArr, v1dAt(iargs[1]))
                val k = DafCRuntime.cstring(keyArr).length
                if (k == 0) {
                    rslt = 0
                    bdSet(String.format(java.util.Locale.US, "%d", rslt))
                    return@run
                }
                var ip = 1
                var count = 0
                while (true) {
                    ip = xindexDaf(tmp2, 0, keyArr, 0, ip)
                    if (ip == 0) break
                    count++
                    ip += k
                }
                rslt = count + 1
                bdSet(String.format(java.util.Locale.US, "%d", rslt))
                return@run
            }

            if (nargs > 1) { argCountErr(); return@run }

            rslt = DafCRuntime.cstring(tmp2).length
            bdSet(String.format(java.util.Locale.US, "%d", rslt)) // ex1
        }

        7 -> { // \$piece()
            val lhs = pd1At(sv.sdlim)
            if (nargs == 2) {
                if (sv.setpiece != 0) {
                    pieceDaf(DafCRuntime.cstring(sv.setname), v1dAt(1),
                             v1dAt(iargs[1]), "1", "1", 1, lhs, sv)
                } else {
                    bdSet(pieceDaf(bdAt(1), v1dAt(1), v1dAt(iargs[1]),
                                   "1", "1", 0, bdAt(1), sv))
                }
                sv.ierr = 0
                return@run
            }
            if (nargs == 3) {
                if (sv.setpiece != 0) {
                    pieceDaf(DafCRuntime.cstring(sv.setname), v1dAt(1),
                             v1dAt(iargs[1]), v1dAt(iargs[2]),
                             v1dAt(iargs[2]), 1, lhs, sv)
                } else {
                    bdSet(pieceDaf(bdAt(1), v1dAt(1), v1dAt(iargs[1]),
                                   v1dAt(iargs[2]), v1dAt(iargs[2]),
                                   0, bdAt(1), sv))
                }
                sv.ierr = 0
                return@run
            }
            if (nargs != 4) { argCountErr(); return@run }
            if (sv.setpiece != 0) {
                pieceDaf(DafCRuntime.cstring(sv.setname), v1dAt(1),
                         v1dAt(iargs[1]), v1dAt(iargs[2]),
                         v1dAt(iargs[3]), 1, lhs, sv)
            } else {
                bdSet(pieceDaf(bdAt(1), v1dAt(1), v1dAt(iargs[1]),
                               v1dAt(iargs[2]), v1dAt(iargs[3]),
                               0, bdAt(1), sv))
            }
            sv.ierr = 0
        }

        8 -> { // \$select() \$storage
            if (nargs == 0) { // \$storage
                rslt = sv.PD1
                bdSet(String.format(java.util.Locale.US, "%d", rslt))
                sv.ierr = 0
                return@run
            }

            // \$select()
            var ix = 0
            while (true) { // sel1:
                tmp2Set(v1dAt(iargs[ix]))
                // scan for a true result
                var qx = xindexDaf(tmp2, 0, cod209Arr(), 0, 1) - 1
                if (qx <= 0) { err(); return@run }
                if (tmp2[qx - 1] == '0') {
                    ix++
                    if (ix >= nargs) { err(); return@run }
                    continue // goto sel1
                }
                bdSet(DafCRuntime.cstringAt(tmp2, qx + 1)) // &tmp2[j+1]
                sv.ierr = 0
                return@run
            }
        }

        9 -> { // \$test \$text() \$translate()

            if (nargs == 0) { // \$test
                sv.bd[1] = if (sv.tpx == 1) '1' else '0'
                sv.bd[2] = NUL_D
                sv.ierr = 0
                return@run
            }

            val upS = toUpperAscii(S)
            if (upS == 'R') { // \$translate(): trns

                if (nargs < 2 || nargs > 3) { argCountErr(); return@run }

                if (nargs == 2) { // 2 argument form: remove chars
                    val r = v1dAt(iargs[1])
                    var q = 1
                    var w = 1
                    while (at(sv.v1d, q) != NUL_D) {
                        if (!r.contains(sv.v1d[q])) {
                            sv.bd[w++] = sv.v1d[q]
                        }
                        q++
                    }
                    sv.bd[w] = NUL_D
                    sv.ierr = 0
                    return@run
                }

                // 3 argument form: map chars
                val r = v1dAt(iargs[1])
                val s = v1dAt(iargs[2])
                var t = ""
                if (r.length > s.length) {
                    t = r.substring(s.length) // strmove(t, &r[i])
                }
                var q = 1
                var w = 1
                while (at(sv.v1d, q) != NUL_D) {
                    if (!t.contains(sv.v1d[q])) {
                        val ri = r.indexOf(sv.v1d[q])
                        if (ri < 0) {
                            sv.bd[w++] = sv.v1d[q]
                        } else {
                            sv.bd[w++] = s[ri] // s[strchr(r,c)-r]
                        }
                    }
                    q++
                }
                sv.bd[w] = NUL_D
                sv.ierr = 0
                return@run
            }

            // \$text()
            if (nargs != 1) { argCountErr(); return@run }

            i = atoi(v1dAt(1)).toLong()
            if (i <= 0) {
                bdClear()
                sv.ierr = 0
                return@run
            }
            var jq = 1
            var kq = 1
            while (kq != i.toInt()) {
                jq = jq + DafCRuntime.cstringAt(sv.pd1, jq).length + 1
                if (jq > sv.pd1len) {
                    bdClear()
                    sv.ierr = 0
                    return@run
                }
                kq++
            }
            var ib = 1
            while (at(sv.pd1, jq) != NUL_D) {
                if (sv.pd1[jq] != TAB_D && sv.pd1[jq] != ' ') {
                    sv.bd[ib++] = sv.pd1[jq++]
                } else {
                    sv.bd[ib++] = ' '
                    jq++
                }
            }
            sv.bd[ib] = NUL_D
            sv.ierr = 0
            return@run
        }

        10 -> { // \$random() \$reverse()
            if (nargs != 1) { argCountErr(); return@run }

            if (toLowerAscii(S) == 'e') { // \$reverse()
                var jq = DafCRuntime.cstringAt(sv.v1d, 1).length + 1
                sv.bd[jq] = NUL_D
                var q = 1
                while (at(sv.v1d, q) != NUL_D) {
                    sv.bd[--jq] = sv.v1d[q]
                    q++
                }
                return@run
            }

            // (MULTI_PRECISION GMP block: dead code on the reference
            // build - comment fold per row 46)

            j = atoi(v1dAt(1)).toLong()

            if (j < 2) {
                sv.bd[1] = '0'
                sv.bd[2] = NUL_D
                return@run
            }
            bdSet(String.format(java.util.Locale.US, "%d",
                                Math.floorMod(randD(), j.toInt())))
            return@run
        }

        11 -> { // \$x
            bdSet(String.format(java.util.Locale.US, "%d", sv.hor[sv.io]))
            sv.ierr = 0
        }

        12 -> { // \$y
            bdSet(String.format(java.util.Locale.US, "%d", sv.ver[sv.io]))
            sv.ierr = 0
        }

        13 -> { // \$io
            bdSet(String.format(java.util.Locale.US, "%d", sv.io))
            sv.ierr = 0
        }

        14 -> { // \$horolog
            val timex = timeD()
            day = timex / 86400
            var tx = timex - day * 86400
            day = 47116 + day
            var fd = day + 1
            var out = String.format(java.util.Locale.US, "%d", fd)
            out += ","
            fd = tx
            out += String.format(java.util.Locale.US, "%d", fd)
            bdSet(out)
            return@run
        }

        15 -> { // \$get()
            if (nargs < 1 || nargs > 3) { argCountErr(); return@run }

            // the following lines are unknown (vendor comment kept)

            if (sv.v1d[1] == '\u0003') {
                if (sv.v1d[2] == '\u0002') {
                    if (nargs == 1) {
                        bdClear()
                        sv.ierr = 0
                        return@run
                    }
                    bdSet(v1dAt(iargs[1]))
                    sv.ierr = 0
                    return@run
                }
                bdSet(v1dAt(2)) // strcpy(&bd[1], &v1d[2])
                sv.ierr = 0
                return@run
            }

            // actual \$get code
            val ctmp = symRetDaf(1, sv.v1d, 1, sv.bd, 1, sv)

            if (ctmp != null) {
                sv.ierr = 0
                return@run // value returned and in bd
            }

            if (nargs == 1) {
                bdClear()
                sv.ierr = 0
                return@run
            }

            if (nargs != 2) { argCountErr(); return@run }

            bdSet(v1dAt(iargs[1]))
            sv.ierr = 0
            return@run
        }

        21, 22, 23, 24, 25, 26, 27, 28, 29 -> { // Pattern match codes \$1..\$9
            // x[0]='_'; x[1]=j (the id char); x[2]=S
            val cvt = symRetDaf(1, charArrayOf('_', j.toInt().toChar(), S,
                                               NUL_D), 0, sv.bd, 1, sv)
            sv.ierr = 0
            if (cvt == null) {
                print("*** _${j.toInt().toChar()}${S}: No such Perl back " +
                        "reference in or near line ${sv.LineNumber}\n\n")
                sigintDaf(100)
            }
            return@run
        }

        30 -> { // \$name() \$next() \$noerr

            if (toUpperAscii(S) == 'O') { // \$noerr
                bdSet(String.format(java.util.Locale.US, "%d", sv.NOERR))
                sv.ierr = 0
                return@run
            }

            if (toUpperAscii(S) == 'A') { // \$name()
                if (v1dAt(1) == "<lcl>") {
                    buildLocalDaf(2, 0, "".toCharArray(), tmp2, sv)
                    bdSet(LocalName(tmp2, null)) // LocalName(tmp2, &bd[1], NULL)
                } else if (v1dAt(1) == "<gbl>") {
                    // globals processed by compiler have x01 codes
                    // embedded; use LocalName() here because the
                    // interpreter does not embed x01's at this point
                    // (vendor comment kept)
                    buildGlobalDaf(2, 0, "".toCharArray(), tmp2, sv)
                    bdSet(LocalName(tmp2, null))
                }
                sv.ierr = 0
                return@run
            }

            // \$next() section
            if (toUpperAscii(S) != 'E' && isAlphaAscii(S)) { err(); return@run }

            if (v1dAt(1) == "<gbl>") {
                if (buildGlobalDaf(1, NEXT_D, sv.v1d, sv.bd, sv) == 0) {
                    err(); return@run
                }
                if (bdAt(1).isEmpty()) bdSet("-1")
            } else {
                if (buildLocalDaf(1, NEXT_D, sv.v1d, sv.bd, sv) == 0) {
                    err(); return@run
                }
            }
            sv.ierr = 0
            return@run
        }

        31 -> { // \$query(x) \$qlength(x) \$qsubscript(x,y)

            if (toLowerAscii(S) == 'u' || !isAlphaAscii(S)) { // \$query()
                if (nargs > 1 || nargs == 0) { argCountErr(); return@run }

                if (v1dAt(1) == "<lcl>") {
                    buildLocalDaf(2, 0, "".toCharArray(), tmp2, sv)
                    bdSet(LocalName(tmp2, null))
                    copyToSv(sv.v1d, DafCRuntime.cstring(tmp2))
                    val tmp2c = CharArray(STR_MAX_DAF) // Qsub res buffer
                    val subArr = CharArray(STR_MAX_DAF)
                    copySvTo(subArr, "-2")
                    qsubDaf(javaSlice(sv.v1d), tmp2c, subArr, sv) // Qsub(name,tmp2,"-2")
                    var q = 1
                    while (at(sv.v1d, q) != FCN_MARK1_D &&
                           at(sv.v1d, q) != NUL_D) q++
                    if (at(sv.v1d, q) == NUL_D) {
                        sv.v1d[q++] = FCN_MARK1_D
                        sv.v1d[q] = NUL_D
                    }
                    symRetDaf(101, sv.v1d, 1, sv.bd, 1, sv) // sym_(101,..)
                    globalNameDaf(javaSlice(sv.bd), 0, sv) // GlobalName name fixer
                    sv.ierr = 0
                    return@run
                } else if (v1dAt(1) == "<gbl>") {
                    // (vendor comment kept: globals processed by the
                    // compiler have x01 codes embedded; use LocalName()
                    // here because the interpreter does not embed x01's)
                    buildGlobalDaf(2, 0, "".toCharArray(), tmp2, sv)
                    bdSet(LocalName(tmp2, null))
                }

                // GlobalQuery(&bd[1], &bd[1], 0, svPtr) - the unit-119
                // render (seat flip); tmp0 folds onto a bd[1..] String
                // snapshot; tmp1 writes back through bd
                globalQueryDaf(javaSlice(sv.bd), sv.bd, NUL_D, sv)
                sv.ierr = 0
                return@run
            }

            if (toLowerAscii(S) == 'l') { // \$qlength()
                if (nargs != 1) { argCountErr(); return@run }
                val op1 = CharArray(512)
                when (v1dAt(1)) {
                    "<lcl>" -> {
                        buildLocalDaf(2, 0, "".toCharArray(), op1, sv)
                        if (evalDaf(op1, op1, sv) != 0) {
                            fcnErr("\n*** Qlength Function argument error. " +
                                    "In or near line ${sv.LineNumber}\n\n")
                            return@run
                        }
                    }
                    "<gbl>" -> {
                        buildGlobalDaf(2, 0, "".toCharArray(), op1, sv)
                        // (vendor keeps the gbr Eval commented - kept)
                    }
                    else -> copySvTo(op1, v1dAt(1))
                }
                qlengthDaf(op1, sv.bd, sv)
                sv.ierr = 0
                return@run
            }

            if (toLowerAscii(S) == 's') { // \$qsubscript()
                if (nargs != 2) { argCountErr(); return@run }

                if (v1dAt(iargs[1]) == "-1") {
                    // return current working directory
                    val cwd = getcwdD()
                    if (cwd == null) { err(); return@run }
                    bdSet(cwd)
                    sv.ierr = 0
                    return@run
                }

                // order of these operations is important because of
                // the stack; do not change (vendor comment kept)

                val op1 = CharArray(512)
                val op2 = CharArray(512)

                if (v1dAt(iargs[1]) == "<lcl>") {
                    buildLocalDaf(2, 0, "".toCharArray(), op2, sv)
                    var q = 0
                    while (at(op2, q) != NUL_D) { // recode 206/207/208
                        if (op2[q] == CODED_OPEN_D) op2[q] = '('
                        else if (op2[q] == CODED_CLOSE_D) op2[q] = ')'
                        else if (op2[q] == CODED_COMMA_D) op2[q] = ','
                        q++
                    }
                    if (evalDaf(op2, op2, sv) != 0) {
                        fcnErr("\n*** Qsubscript Function error: 1nd " +
                               "argument. In or near line ${sv.LineNumber}\n\n")
                        return@run
                    }
                } else if (v1dAt(iargs[1]) == "<gbl>") {
                    buildGlobalDaf(2, 0, "".toCharArray(), op2, sv)
                    if (evalDaf(op2, op2, sv) != 0) {
                        fcnErr("\n*** Qsubscript Function error: 1nd " +
                               "argument. In or near line ${sv.LineNumber}\n\n")
                        return@run
                    }
                } else {
                    copySvTo(op2, v1dAt(iargs[1]))
                }

                if (v1dAt(1) == "<lcl>") {
                    buildLocalDaf(2, 0, "".toCharArray(), op1, sv)
                    if (evalDaf(op1, op1, sv) != 0) {
                        fcnErr("\n*** Qsubscript Function error: 1nd " +
                               "argument. In or near line ${sv.LineNumber}\n\n")
                        return@run
                    }
                } else if (v1dAt(1) == "<gbl>") {
                    buildGlobalDaf(2, 0, "".toCharArray(), op1, sv)
                    // (vendor keeps this Eval commented - kept)
                } else {
                    copySvTo(op1, v1dAt(1))
                }

                qsubDaf(op1, sv.bd, op2, sv) // Qsub(op1, &bd[1], op2)
                sv.ierr = 0
                return@run
            }

            unknownErr() // function not recognized
            return@run
        }

        32 -> { // \$order()
            if (nargs == 1) {
                if (sv.v1d[1] == '^') {
                    val dirArr = CharArray(STR_MAX_DAF)
                    copySvTo(dirArr, "1")
                    val tmpBd = CharArray(STR_MAX_DAF)
                    copySvTo(tmpBd, bdAt(1))
                    globalOrderDaf(javaSlice(sv.v1d), tmpBd, dirArr, sv)
                    copyToSv(sv.bd, DafCRuntime.cstring(tmpBd))
                    sv.ierr = 0
                    return@run
                }
                if (buildLocalDaf(1, ORDERNEXT_D, sv.v1d, sv.bd, sv) == 0) {
                    err(); return@run
                }
                sv.ierr = 0
                return@run
            }

            if (nargs != 2) { err(); return@run }

            if (v1dAt(iargs[1]) == "1") {
                if (sv.v1d[1] == '^') {
                    val dirArr = CharArray(STR_MAX_DAF)
                    copySvTo(dirArr, "1")
                    val tmpBd = CharArray(STR_MAX_DAF)
                    copySvTo(tmpBd, bdAt(1))
                    globalOrderDaf(javaSlice(sv.v1d), tmpBd, dirArr, sv)
                    copyToSv(sv.bd, DafCRuntime.cstring(tmpBd))
                    sv.ierr = 0
                    return@run
                }
                if (buildLocalDaf(1, ORDERNEXT_D, sv.v1d, sv.bd, sv) == 0) {
                    err(); return@run
                }
                sv.ierr = 0
                return@run
            }

            if (v1dAt(iargs[1]) == "-1") {
                if (sv.v1d[1] == '^') {
                    val dirArr = CharArray(STR_MAX_DAF)
                    copySvTo(dirArr, "-1")
                    val tmpBd = CharArray(STR_MAX_DAF)
                    copyToSv(tmpBd, bdAt(1))
                    val tmpVd = CharArray(STR_MAX_DAF)
                    copySvTo(tmpVd, v1dAt(1))
                    globalOrderDaf(tmpVd, tmpBd, dirArr, sv)
                    copyToSv(sv.bd, DafCRuntime.cstring(tmpBd))
                    sv.ierr = 0
                    return@run
                }
                if (buildLocalDaf(1, ORDERPREV_D, sv.v1d, sv.bd, sv) == 0) {
                    err(); return@run
                }
                sv.ierr = 0
                return@run
            }
            err()
            return@run
        }

        else -> { // unknown code
            sv.ierr = 30
            unknownErr()
            return@run
        }
        }

        // ex2:
        sv.ierr = 0
    }
}

// ---- carrier helpers (CharArrays; row 8/81 folds) ----

// strcpy(dst-backed, src-of-bd): write a String into a CharArray at 0
private fun copySvTo(dst: CharArray, s: String) {
    var d = 0
    var q = 0
    while (q < s.length && d < dst.size - 1) { dst[d++] = s[q++] }
    dst[d] = NUL_D
}

// strcpy(sv-field-backed, src): same fold into an MSV field carrier
private fun copyToSv(dst: CharArray, s: String) {
    for (q in s.indices) dst[q] = s[q]
    dst[s.length] = NUL_D
}

private fun FcnHelpersNone() {} // (no-op keep structure marker)

// cod209 carrier (char[2] = {209,0})
private fun cod209Arr(): CharArray = charArrayOf(COD209_D, NUL_D)

// &buf[1] java snapshot fold: NUL-terminated CharArray of buf[1..]
private fun javaSlice(buf: CharArray): CharArray {
    var n = 0
    while (n + 1 < buf.size && buf[n + 1] != NUL_D) n++
    val r = CharArray(n + 1)
    for (q in 0 until n) r[q] = buf[q + 1]
    return r
}
