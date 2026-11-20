package doc

import java.util.Locale

// Unit 123 (B4 tranche 3): interp-fcn.cpp::fcn (batch 4, complex
// stratum; units.csv row 95: loc 775, ptr 356, ctrl 329, score
// 1221.5, rank 307) - the interpreter dollar-function dispatcher,
// the largest unit of the arm. The \u0024\u0024FUNCTION head (the
// extempore do-command build over a pushed AllocSV state vector -
// start[]/nstart copied in and out, the code-206 caret scan, the
// no-caret local-label arm with the '(' argument re-attach and the
// last-char trim quirk, the code-3/206 arg-marker walk); then the
// operand strip (the twice-run strmove left-shift of the coded
// 206 canon - the vendor's 206 literal equals the CodedOpen
// carrier, ledgered), the noname CodedOpen arms-into-args form,
// the CodedClose/CodedComma iargs roster walk, the tolower fold
// (ASCII two-case compare per H-012), the 128-entry opcode table
// (parsed byte-identical from the vendor initializer; indices
// 128..255 are C zero-fill remainder - a high function byte hits
// opcode zero and falls to the default unknown arm; the Kotlin
// array-index bound renders the same arms via the size guard,
// ledger), and the 19 dispatcher cases.
//
// IFDEF LEDGER (H-018): SQLITE IS defined at interp-fcn.cpp file
// scope (line 29) - the \u0024ztable commission blocks are live
// (the sql() bodies are commented in the vendor and ledgered, the
// \u0024ztabsize TabSize 1..22 gate is live through the sql seat,
// the \u0024zsqloutput strcpy is live). Multi-precision per
// arith.h:33-40 config (HAVE_GMP/HAVE_MPFR undef in the reference
// build config.h) is UNDEFINED: the case-10 gmp random arm is DEAD
// (journaled here, not rendered; the plain rand() arm is live) and
// the case-5 ifdef sandwich resolves to the "%c%d%c%df" percent-f
// format arm (the BIGFLOAT long-double and MULTI_PRECISION RNf
// arms are DEAD, journaled - the mps_justify unit-73 render's H-018
// note is the precedent).
//
// SEAT LEDGER (H-014): ScanParse/Eval are batch-8/batch-9 corpus
// seats raised by this unit; GlobalOrder is the globalOrder.h:8
// inline (batch-9 roster row 266) - the four-arg form is DISTINCT
// from the landed unit-39/69 libmpscpp GlobalOrder renders (same
// name, different family, H-015 same-name rule); strfmon is the
// POSIX monetary.h libc entry (no POSIX roster unit, OS-libc
// owner, the read(2) seat precedent); AllocSV/Interpret/sql/
// sym_/xindex/lpad/Qsub/Qlength/Mglobal/Mpiece/keyfix/unpad are
// the pre-existing seats; mps_justify,LocalName,GlobalName,
// GlobalQuery,parse_ are REAL renders bound per their contracts
// (GlobalName and GlobalQuery take offset-0 carriers and write
// back into tmp1 - the &bd[1] call sites pass a slice for tmp0
// and a scratch for tmp1 with the copyAt write-back; the vendor's
// bd-aliasing of tmp0/tmp1 in GlobalQuery is ledgered). The
// Stubs.fcn seat converts to THIS render's delegate (the
// Fnumber_/Fnumber/_fnumber callers bind unchanged).
//
// ADAPTATION LEDGER: sprintf "%d"/"%ld" folds to String.format
// (Locale.US per H-009; the FindSym precedent) with copyAtS/
// catAt carrier writes; sscanf "%ld" atoi-folds to the arm
// atoi(String) seat (Find.kt, decimal-prefix C contract) and the
// case-3 atol folds onto the same Int seat with the truncation
// kept as the and-0xFF char domain fold (H-012; full-64-bit strtoul
// divergence ledgered); atof folds to the internal strtod(String)
// seat (MpsExp.kt, the identical double contract); strcasecmp
// folds to the ASCII two-case String compare (H-012); strchr
// probes fold to String contains; getpid/time/getcwd/rand render
// as JVM intrinsics per H-010 (ProcessHandle.current().pid(),
// System.currentTimeMillis()/1000, user.dir, and the seedless
// kotlin.random.Random respectively - the last per the libc
// rand(3) numeric-intrinsic note); exit(EXIT_FAILURE) renders
// exitProcess(1) (LibMisc1 precedent, EXIT_FAILURE equals 1);
// free(svPtr1) is the GC no-op (GlobalQuery precedent); the null
// returns of getcwd and the malloc-null arms are unreachable on
// the JVM and LEDGERED, not rendered.
//
// DEAD-CODE LEDGER (H-013): the unused \u0024\u0024FUNCTION locals
// (status, the shadowed tmp1[STR_MAX]) and file-scope dead
// powder (the unsigned char tmp1[2] IS live - the \u0024char
// single-byte holder; the dead ones are: double t1, the char
// *ctime() redeclaration, the SQLITE-block int sql() forward
// declaration, and long timezone/daylight); the case-15 redundant
// inner NULL recycle after the NULL return guard (rendered
// without the trivially-true retest); and the UNREACHABLE LABEL
// qsuberr (the 2nd-argument banner tail - every vendor goto to it
// is commented; only qsuberr1/qlenerr are live; the label is
// ledgered here and NOT rendered). The goto err/unknown_err/
// arg_count_err/ex1/ex2 tail geometry renders as the labeled
// run-block returning the FcnT tail code (the H-011 flag
// precedent of the arm) plus the case-local direct returns.
//
// CARRIER LEDGER: this unit adds the stateVector fields it
// touches to Msv (setpiece :54, nstart :72 as the opaque struct
// nmes pointer seat, PD1 :82, args :85, TabSize :87, sqloutput
// :99 as the String render per the CurrentFile H-008 precedent;
// Table :86 rides only the COMMENTED vendor sql lines and is not
// added). The iargs short roster renders as IntArray (the vendor
// shorts are count/offset domains, H-012); rslt is the byte-code
// Int domain. The scratch tmp2/tmp3 are the STR_MAX per-call
// carriers (H-008: no cross-call state). Consult rows logged
// 2026-11-19T20:53:08Z. Naming per H-017 (fcn, sym_, sigint,
// mps_justify, LocalName, GlobalName, GlobalOrder, Qsub, Qlength,
// strfmon, ScanParse, Eval, AllocSV kept verbatim; the vendor
// shadowed C locals carry the render-suffix rename - tmp1E,
// tmp2Gi, tmp2L - documented per the Kotlin no-shadow rule).

private const val DOLLAR_FC = '\u0024'

// defines.h:170-171 (the order-walk direction codes)
private const val ORDERNEXT_FC = 12
private const val ORDERPREV_FC = 13

// vendor opcode[256]: the per-byte function id to case code table
// (128 initialized entries; the rest is the C zero-fill, see the
// header ledger). Byte-identical values in the vendor order.
private val OPCODE_FC = intArrayOf(
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 21,
    22, 23, 24, 25, 26, 27, 28, 29, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 2, 99, 3,
    99, 1, 4, 15, 14, 13, 5, 99, 6, 99,
    30, 32, 7, 31, 10, 8, 9, 99, 99, 99,
    11, 12, 98, 99, 99, 99, 99, 99
)

// the vendor goto-tail codes (the ex1 tail folds to the case-local
// ex1Emit helper per its single-use site trio)
private enum class FcnT { EX2, ERR, FNUM_WRONG, QLENERR, QSUBERR1, ARG_COUNT, UNKNOWN }

// strmove(&buf[off], &buf[off + 1]): the in-place left shift of the
// coded operand head (inline.h:38 strmove is the overlapping-copy
// inline; the shift renders in place on the carrier)
private fun strmoveShift(buf: CharArray, off: Int) {
    var d = off
    var s = off + 1
    while (true) {
        buf[d] = buf[s]
        if (buf[d] == NUL) break
        d++
        s++
    }
}

// C string literal to the NUL-terminated carrier (the seat key
// and cc1/cc2 arguments; the Mltoa NUL-pad precedent)
private fun cstr(s: String): CharArray = s.toCharArray() + NUL

fun fcn(svPtr: Msv) {
    val tmp1 = CharArray(2) // the \u0024char single-byte holder
    val tmp2 = CharArray(STR_MAX)
    val tmp3 = CharArray(STR_MAX)
    // the vendor file-scope char *ctmp folds to the per-branch local
    // vals (ctmpE/ctmpL/ctmpG2; the Kotlin no-capture-shadow rule)
    val cod209 = charArrayOf(209.toChar(), NUL)

    val tail = run fcnRun@{

        // ex1 tail: sprintf(&bd[1], "%d", rslt); return
        fun ex1Emit(r: Int) {
            CstringOps.copyAtS(svPtr.bd, 1, String.format(Locale.US, "%d", r))
        }

        //----------------
        // \u0024\u0024FUNCTION
        //----------------

        if (svPtr.v1d[1] == DOLLAR_FC && svPtr.v1d[2] == DOLLAR_FC) {

            svPtr.xpx++
            val org = svPtr.xpx
            val endF = Stubs.ScanParse(svPtr.xd.copyOfRange(svPtr.xpx, svPtr.xd.size)) + svPtr.xpx
            val hold = svPtr.xd[endF]
            svPtr.xd[endF] = NUL
            val tcmd = CharArray(128)
            CstringOps.strcpyS(tcmd, "do ")
            CstringOps.strcatS(tcmd, CstringOps.cstringAt(svPtr.v1d, 3))

            // look for ^ in function call - if not, function is local

            var ca = svPtr.xpx
            while (svPtr.xd[ca] != NUL) {
                if (svPtr.xd[ca] == '^') break
                ca++
            }

            if (svPtr.xd[ca] == NUL) {
                val tmpL = CharArray(STR_MAX)
                var iF = 3
                while (svPtr.v1d[iF] != NUL) {
                    if (svPtr.v1d[iF] == CodedOpen.toChar()) break
                    iF++
                }
                val jf = if (svPtr.v1d[iF] == NUL) 1 else 0 // no arg case
                svPtr.v1d[iF] = NUL
                CstringOps.strcpyS(tmpL, "do ")
                CstringOps.strcatS(tmpL, CstringOps.cstringAt(svPtr.v1d, 3)) // label
                CstringOps.strcatS(tmpL, "^") // caret
                CstringOps.strcatS(tmpL, svPtr.CurrentFile) // current directory
                if (jf == 0) { // attach argument
                    svPtr.v1d[iF] = '('
                    CstringOps.strcatS(tmpL, CstringOps.cstringAt(svPtr.v1d, iF))
                    tmpL[CstringOps.strlenC(tmpL) - 1] = NUL
                    CstringOps.strcatS(tmpL, ")")
                }
                CstringOps.strcpyC(tcmd, tmpL)

            } else {

                CstringOps.strcatS(tcmd, CstringOps.cstringAt(svPtr.xd, org))
                svPtr.xd[endF] = hold
            }

            //--------------------------------------
            // create environment and launch command
            //--------------------------------------

            run {
                val svPtr1 = Stubs.AllocSV()

                svPtr1._Sym = svPtr._Sym
                svPtr1.LineNumber = svPtr.LineNumber
                for (iS in 0 until SYM_MAX) svPtr1.start[iS] = svPtr.start[iS]
                svPtr1.nstart = svPtr.nstart

                svPtr.ierr = Stubs.Interpret(CstringOps.cstring(tcmd), svPtr1)

                for (iS in 0 until SYM_MAX) svPtr.start[iS] = svPtr1.start[iS]

                CstringOps.strcpyS(tmp2, CstringOps.cstringAt(svPtr1.args, 0))

                // SQLITE ifdef body is empty in the vendor (the sql()
                // forward declaration is dead powder, H-013)

                freeLedgerFcn() // free(svPtr1): the GC no-op note
            }

            if (svPtr.ierr != 0) return@fcnRun FcnT.ERR

            svPtr.xpx = endF - 1
            CstringOps.copyAt(svPtr.bd, 1, tmp2)
            return
        }

        svPtr.ierr = 0
        var nargs = 0
        val iargs = IntArray(10)
        iargs[0] = 1
        var jB = svPtr.v1d[2].code // hold function id
        val S = svPtr.v1d[3]

        CstringOps.strcpyS(tmp2, CstringOps.cstringAt(svPtr.v1d, 1))

        while (svPtr.v1d[1] != CodedOpen.toChar() && svPtr.v1d[1] != NUL)
            strmoveShift(svPtr.v1d, 1)

        if (svPtr.v1d[1] == CodedOpen.toChar()) strmoveShift(svPtr.v1d, 1)

        if (jB == CodedOpen) { // noname form - parms to sub only
            CstringOps.strcpyS(svPtr.args, CstringOps.cstringAt(svPtr.v1d, 1))
            return@fcnRun FcnT.EX2
        }

        var iB = 1
        while (svPtr.v1d[iB] != NUL) {
            if (svPtr.v1d[iB] == CodedClose.toChar() || svPtr.v1d[iB] == CodedComma.toChar()) {
                svPtr.v1d[iB] = NUL
                nargs++
                iargs[nargs] = iB + 1
            }
            iB++
        }

        if (jB in 65..90) jB += 32 // lower case conversion

        val iOp = if (jB < OPCODE_FC.size) OPCODE_FC[jB] else 0
        if (iOp == 99) {
            svPtr.ierr = 30
            return@fcnRun FcnT.UNKNOWN
        }

        when (iOp) {

            //---------------------------------------------------------------
            // \u0024z variable and LHS functions
            //---------------------------------------------------------------

            98 -> {
                // SQLITE is live per the reference build (header ledger)
                if (CstringOps.cstring(tmp2).equals("\u0024ztable", ignoreCase = true)) {
                    // change RDBMS name: the sql bodies are commented in
                    // the vendor (the Close/St OPEN re-open pair and the
                    // Table strcpy; the Table carrier is not added)
                } else if (CstringOps.cstring(tmp2).equals("\u0024ztabsize", ignoreCase = true)) {
                    // change RDBMS table size
                    Stubs.sql(206, svPtr, null, null, null, null) // sql(OPEN,...), defines.h:169
                    svPtr.TabSize = atoi(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim))
                    if (svPtr.TabSize > 22) return@fcnRun FcnT.UNKNOWN
                    if (svPtr.TabSize < 1) return@fcnRun FcnT.UNKNOWN
                } else if (CstringOps.cstring(tmp2).equals("\u0024zsqloutput", ignoreCase = true)) {
                    // change RDBMS table size
                    svPtr.sqloutput = CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim)
                } else
                    return@fcnRun {
                        System.out.print("\n*** Unrecognized Z-type variable.\n" +
                                CstringOps.cstring(tmp2) + "\nIn or near line number " +
                                svPtr.LineNumber + "\n\n")
                        kotlin.system.exitProcess(1) // exit(EXIT_FAILURE)
                        FcnT.ERR
                    }
                return@fcnRun FcnT.EX2
            }

            //---------------------------------------------------------------
            // \u0024extract()
            //---------------------------------------------------------------

            1 -> {
                if (nargs == 1) { // no 2nd or 3rd operand - return first char

                    if (svPtr.setpiece != 0) { // LHS ref
                        val tmp1E = CharArray(STR_MAX)
                        val pd1s = CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim)

                        if (svPtr.setname[0] == '^') { // global array reference
                            val tmp2E = CharArray(STR_MAX)
                            var f = Stubs.Mglobal(RETRIEVE, svPtr.setname, tmp2E, svPtr) // fetch gbl
                            if (f != 0) { // global exists
                                CstringOps.strcatS(tmp1E, pd1s)
                                if (CstringOps.strlenC(tmp2E) != 0)
                                    CstringOps.strcatS(tmp1E, CstringOps.cstringAt(tmp2E, 1))
                                f = Stubs.Mglobal(STORE, svPtr.setname, tmp1E, svPtr)
                                return@fcnRun FcnT.EX2
                            } else { // global does not exist
                                CstringOps.strcpyS(tmp1E, pd1s)
                                f = Stubs.Mglobal(STORE, svPtr.setname, tmp1E, svPtr)
                                return@fcnRun FcnT.EX2
                            }
                        } else { // local variable reference
                            val rsltE = CharArray(STR_MAX)
                            val ctmpE = Stubs.sym_(11, svPtr.setname, rsltE, svPtr)
                            CstringOps.copyAt(svPtr.bd, 1, rsltE)
                            if (ctmpE != null) { // variable exists
                                CstringOps.strcpyS(tmp1E, pd1s)
                                if (CstringOps.strlenC(ctmpE) != 0)
                                    CstringOps.strcatS(tmp1E, CstringOps.cstringAt(ctmpE, 1))
                                Stubs.sym_(0, svPtr.setname, tmp1E, svPtr)
                                return@fcnRun FcnT.EX2
                            } else { // variable does not exist
                                CstringOps.strcpyS(tmp1E, pd1s)
                                Stubs.sym_(0, svPtr.setname, tmp1E, svPtr)
                                return@fcnRun FcnT.EX2
                            }
                        }
                    }

                    //---------------------------
                    // not LHS \u0024e() - RHS function
                    //---------------------------

                    svPtr.bd[1] = svPtr.v1d[1] // return first char
                    svPtr.bd[2] = NUL
                    return@fcnRun FcnT.EX2
                }

                if (nargs != 2 && nargs != 3) return@fcnRun FcnT.ARG_COUNT

                var iE = atoi(CstringOps.cstringAt(svPtr.v1d, iargs[1])) // arg 2
                if (iE <= 0) iE = 1

                var jE: Int // 2nd operand only - return one char
                jE = if (nargs != 3) iE
                else atoi(CstringOps.cstringAt(svPtr.v1d, iargs[2])) // arg 3

                if (jE <= 0) jE = 0 // return null string

                //------------------------------------
                // RHS function where end before start
                //------------------------------------

                if (svPtr.setpiece == 0 && (iE >= iargs[1] || jE < iE)) {
                    svPtr.bd[1] = NUL // empty string
                    return@fcnRun FcnT.EX2
                }

                //--------------------------
                // LHS where end before start
                //--------------------------

                if (svPtr.setpiece != 0 && jE < iE) { // store empty string if end exceeds start
                    val tmp1S = CharArray(16)
                    CstringOps.strcpyS(tmp1S, "")

                    if (svPtr.setname[0] == '^') { // global array reference
                        Stubs.Mglobal(STORE, svPtr.setname, tmp1S, svPtr)
                        return@fcnRun FcnT.EX2
                    } else { // local variable
                        Stubs.sym_(0, svPtr.setname, tmp1S, svPtr)
                        return@fcnRun FcnT.EX2
                    }
                }

                // if j exceeds str length, make it str length - RHS

                if (svPtr.setpiece == 0 && jE > iargs[1]) jE = iargs[1]

                if (iE < 1) iE = 1 // if start is less than 1, make it 1

                // LHS ref ------------------------------------------

                if (svPtr.setpiece != 0) { // LHS ref
                    val tmp1L = CharArray(STR_MAX)
                    val tmp2L = CharArray(STR_MAX)
                    val pd1s2 = CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim)

                    if (svPtr.setname[0] == '^') { // global array reference
                        // the vendor's inner shadowed tmp2 (H-017 rename ledger):
                        val tmp2Gi = CharArray(STR_MAX)

                        var fL = Stubs.Mglobal(RETRIEVE, svPtr.setname, tmp2Gi, svPtr) // fetch gbl

                        if (fL != 0) { // global exists
                            val ctmpG = CstringOps.cstring(tmp2Gi) // gbl array value

                            if (CstringOps.strlenC(tmp2Gi) < iE)
                                while (CstringOps.strlenC(tmp2Gi) < jE)
                                    CstringOps.strcatS(tmp2Gi, " ") // make it longer
                            tmp2Gi[iE - 1] = NUL // truncate

                            CstringOps.strcpyS(tmp1L, pd1s2) // RHS

                            if (ctmpG.isNotEmpty())
                                CstringOps.catAt(tmp2Gi, 0, tmp1L) // insert RHS
                            if (ctmpG.length >= jE)
                                CstringOps.strcatS(tmp2Gi, ctmpG.substring(jE))

                            fL = Stubs.Mglobal(STORE, svPtr.setname, tmp2Gi, svPtr)

                            return@fcnRun FcnT.EX2
                        } else { // global does not exist
                            var kL = 0
                            while (kL < iE - 1) {
                                tmp1L[kL] = ' '
                                kL++
                            }
                            tmp1L[kL] = NUL // initial string
                            CstringOps.strcatS(tmp1L, pd1s2)
                            fL = Stubs.Mglobal(STORE, svPtr.setname, tmp1L, svPtr)
                            return@fcnRun FcnT.EX2
                        }
                    } else { // local variable

                        val rsltL = CharArray(STR_MAX)
                        val ctmpL = Stubs.sym_(11, svPtr.setname, rsltL, svPtr) // fetch variable value
                        CstringOps.copyAt(svPtr.bd, 1, rsltL)
                        if (ctmpL != null) { // variable exists
                            val ctmpS = CstringOps.cstring(ctmpL)
                            CstringOps.strcpyS(tmp2L, ctmpS) // value of variable
                            if (CstringOps.strlenC(tmp2L) < iE)
                                while (CstringOps.strlenC(tmp2L) < jE)
                                    CstringOps.strcatS(tmp2L, " ") // make it longer
                            tmp2L[iE - 1] = NUL // truncate
                            CstringOps.strcpyS(tmp1L, pd1s2) // RHS
                            if (ctmpS.isNotEmpty())
                                CstringOps.catAt(tmp2L, 0, tmp1L) // insert RHS
                            if (ctmpS.length >= jE)
                                CstringOps.strcatS(tmp2L, ctmpS.substring(jE))
                            Stubs.sym_(0, svPtr.setname, tmp2L, svPtr)
                            return@fcnRun FcnT.EX2
                        } else { // variable does not exist - create and append
                            var kL2 = 0
                            while (kL2 < iE - 1) {
                                tmp1L[kL2] = ' '
                                kL2++
                            }
                            tmp1L[kL2] = NUL // initial string
                            CstringOps.strcatS(tmp1L, pd1s2)
                            Stubs.sym_(0, svPtr.setname, tmp1L, svPtr)
                            return@fcnRun FcnT.EX2
                        }
                    }
                }

                // RHS ref --------------------------------------------

                var kR = 1
                var iR = iE
                while (iR <= jE) {
                    svPtr.bd[kR] = svPtr.v1d[iR]
                    kR++
                    iR++
                }
                svPtr.bd[kR] = NUL
                return@fcnRun FcnT.EX2
            }

            //---------------------------------------------------------------
            // \u0024ascii()
            //---------------------------------------------------------------

            2 -> {
                if (nargs > 2) return@fcnRun FcnT.ARG_COUNT

                val iA = if (nargs == 2) atoi(CstringOps.cstringAt(svPtr.v1d, iargs[1])) else 1

                if (iA >= iargs[1] || iA <= 0) {
                    svPtr.bd[1] = '-'
                    svPtr.bd[2] = '1'
                    svPtr.bd[3] = NUL
                    return
                }

                var rsltA = svPtr.v1d[iA].code
                if (rsltA == 0) rsltA = -1
                ex1Emit(rsltA)
                return
            }

            //---------------------------------------------------------------
            // \u0024char()
            //---------------------------------------------------------------

            3 -> {
                svPtr.bd[1] = NUL
                var jC = 1
                var iC = 1
                while (iC <= nargs) {
                    CstringOps.strcpyS(tmp2, CstringOps.cstringAt(svPtr.v1d, jC))
                    // day = atol(tmp2); the Int-seat atof fold per the header
                    // ledger (the and-0xFF char domain truncation kept)
                    var dayC = atoi(CstringOps.cstring(tmp2)).toLong()
                    if (dayC <= 0) dayC = 0
                    tmp1[0] = (dayC and 0xFF).toInt().toChar()
                    tmp1[1] = NUL
                    CstringOps.catAt(svPtr.bd, 1, tmp1)
                    jC = iargs[iC]
                    iC++
                }
                return
            }

            //---------------------------------------------------------------
            // \u0024find() \u0024fnumber()
            //---------------------------------------------------------------

            4 -> {
                if (S == 'n' || S == 'N') { // \u0024fnumber()

                    var dp = -1
                    var plus = 0
                    var minus = 0
                    var comma = 0
                    var tF = 0
                    var t1F = 0
                    var pF = 0 // expr flags

                    if (nargs != 3 && nargs != 2) return@fcnRun FcnT.ARG_COUNT

                    if (nargs == 3) dp = atoi(CstringOps.cstringAt(svPtr.v1d, iargs[2]))

                    var x = strtod(CstringOps.cstringAt(svPtr.v1d, 1)) // value to be converted

                    if (svPtr.v1d[iargs[1]] == '*') { // arg is for strfmon
                        val dst = CharArray(STR_MAX)
                        Stubs.strfmon(dst, 256, CstringOps.cstringAt(svPtr.v1d, iargs[1] + 1), x)
                        CstringOps.copyAt(svPtr.bd, 1, dst)
                        return
                    }

                    val pat = CstringOps.cstringAt(svPtr.v1d, iargs[1])
                    if (pat.contains('+')) plus = 1
                    if (pat.contains('-')) minus = 1
                    if (pat.contains(',')) comma = 1
                    if (pat.contains('T')) tF = 1
                    if (pat.contains('t')) tF = 1
                    if (pat.contains('P')) pF = 1
                    if (pat.contains('p')) pF = 1

                    if (pF != 0 && (plus != 0 || minus != 0 || tF != 0))
                        return@fcnRun FcnT.FNUM_WRONG

                    var strF = "%!" // format supress currency sym

                    if (tF != 0) { // trailing sign
                        if (x < 0) t1F = 1
                        x = kotlin.math.abs(x)
                    }

                    if (comma == 0) strF += "^" // no grouping chars
                    if (pF != 0) strF += "(" // use parens for neg
                    if (minus != 0) x = kotlin.math.abs(x) // supress minus

                    if (dp != -1) {
                        // sprintf(dps,".%d",dp); strcat(str,dps) - the H-009 fold
                        strF += "." + dp
                    } else {
                        var iD = 1
                        while (svPtr.v1d[iD] != NUL && svPtr.v1d[iD] != '.') iD++
                        if (svPtr.v1d[iD] == NUL) strF += ".0"
                        else {
                            // i = strlen(&v1d[1]) - i; sprintf(tmp,".%d",i)
                            iD = CstringOps.cstringAt(svPtr.v1d, 1).length - iD
                            strF += "." + iD
                        }
                    }

                    strF += "i"

                    val rstr = CharArray(64)
                    val dst2 = CharArray(STR_MAX)
                    // strfmon(&rstr[1],256,str,x) - the 64/256 size quirk kept
                    // (the vendor over-declares max; ledger)
                    Stubs.strfmon(dst2, 256, strF, x)
                    CstringOps.copyAt(rstr, 1, dst2)

                    rstr[0] = if (plus != 0 && x > 0.0 && minus == 0 && tF == 0)
                        '+' // number was made positive by minus/t
                    else ' '

                    if (tF != 0) {
                        if (t1F != 0) CstringOps.catAt(rstr, 0, "-")
                        else if (plus != 0) CstringOps.catAt(rstr, 0, "+")
                    }

                    if (rstr[0] == ' ')
                        CstringOps.copyAtS(svPtr.bd, 1, CstringOps.cstringAt(rstr, 1))
                    else
                        CstringOps.copyAtS(svPtr.bd, 1, CstringOps.cstring(rstr))

                    return
                }

                //------
                // \u0024find
                //------

                if (nargs != 2 && nargs != 3) return@fcnRun FcnT.ARG_COUNT

                CstringOps.strcpyS(tmp2, CstringOps.cstringAt(svPtr.v1d, 1))
                CstringOps.strcpyS(tmp3, CstringOps.cstringAt(svPtr.v1d, iargs[1]))

                var iFd = if (nargs == 2) 1
                else atoi(CstringOps.cstringAt(svPtr.v1d, iargs[2]))

                if (tmp3[0] != NUL) {
                    iFd = Stubs.xindex(tmp2, tmp3, iFd.toShort())
                    if (iFd > 0) iFd += CstringOps.strlenC(tmp3)
                }

                if (iFd > CstringOps.strlenC(tmp2) + 1) iFd = 0
                ex1Emit(iFd)
                return
            }

            //---------------------------------------------------------------
            // \u0024justify() \u0024job
            //---------------------------------------------------------------

            5 -> {
                if (nargs == 0) { // \u0024job
                    // sprintf(&bd[1], "%X", getpid()) - the JVM intrinsic
                    CstringOps.copyAtS(svPtr.bd, 1,
                            String.format(Locale.US, "%X", java.lang.ProcessHandle.current().pid()))
                    return@fcnRun FcnT.EX2
                }

                if (nargs < 2 || nargs > 3) return@fcnRun FcnT.ARG_COUNT

                var lJ = atoi(CstringOps.cstringAt(svPtr.v1d, iargs[1]))

                CstringOps.copyAtS(svPtr.bd, 1, CstringOps.cstringAt(svPtr.v1d, 1))

                if (nargs != 3) {
                    var kJ = CstringOps.cstringAt(svPtr.bd, 1).length
                    if (kJ >= lJ) return@fcnRun FcnT.EX2
                    if (lJ > 255) lJ = 255
                    // lpad(&bd[1], &bd[1], l) - the in-place pad via the slice
                    val padS = svPtr.bd.copyOfRange(1, svPtr.bd.size)
                    val padD = padS.copyOf()
                    lpad(padD, padS, lJ.toShort())
                    CstringOps.copyAtS(svPtr.bd, 1, CstringOps.cstring(padD))
                    return@fcnRun FcnT.EX2
                }

                val kJ2 = atoi(CstringOps.cstringAt(svPtr.v1d, iargs[2])) // arg 3

                // sprintf(tmp2, "%c%d%c%df", '%', l, '.', k) - the live
                // reference-build arm (H-018); the %c paren fold per H-009
                val fmtJ = "%" + lJ + "." + kJ2 + "f"

                val aJ = CharArray(STR_MAX)
                CstringOps.strcpyS(aJ, fmtJ)
                val bJ = CharArray(STR_MAX)
                val cJ = svPtr.v1d.copyOfRange(1, svPtr.v1d.size)
                mps_justify(aJ, bJ, cJ)
                CstringOps.copyAtS(svPtr.bd, 1, CstringOps.cstring(bJ))
                return@fcnRun FcnT.EX2
            }

            //---------------------------------------------------------------
            // \u0024len()
            //---------------------------------------------------------------

            6 -> {
                if (nargs == 0) {
                    svPtr.bd[1] = '0'
                    svPtr.bd[2] = NUL
                    return
                }

                CstringOps.strcpyS(tmp2, CstringOps.cstringAt(svPtr.v1d, 1))

                if (nargs == 2) {
                    var iL = 1
                    var jL = 0
                    val kL = CstringOps.cstringAt(svPtr.v1d, iargs[1]).length
                    if (kL == 0) {
                        ex1Emit(0)
                        return
                    }
                    val key = svPtr.v1d.copyOfRange(iargs[1], svPtr.v1d.size)
                    while (true) {
                        iL = Stubs.xindex(tmp2, key, iL.toShort())
                        if (iL == 0) break
                        jL++
                        iL += kL
                    }
                    ex1Emit(jL + 1)
                    return
                }

                if (nargs > 1) return@fcnRun FcnT.ARG_COUNT

                ex1Emit(CstringOps.strlenC(tmp2))
                return
            }

            //---------------------------------------------------------------
            // \u0024piece()
            //---------------------------------------------------------------

            7 -> {
                if (nargs == 2) {
                    if (svPtr.setpiece != 0) {
                        Stubs.Mpiece(svPtr.setname,
                                svPtr.v1d.copyOfRange(1, svPtr.v1d.size),
                                svPtr.v1d.copyOfRange(iargs[1], svPtr.v1d.size),
                                cstr("1"), cstr("1"), 1,
                                svPtr.pd1.copyOfRange(svPtr.sdlim, svPtr.pd1.size), svPtr)
                    } else {
                        val dstP = CharArray(STR_MAX)
                        Stubs.Mpiece(dstP,
                                svPtr.v1d.copyOfRange(1, svPtr.v1d.size),
                                svPtr.v1d.copyOfRange(iargs[1], svPtr.v1d.size),
                                cstr("1"), cstr("1"), 0,
                                // the vendor aliases dst and modes at bd+1
                                // (ledger); the scratch carries the seat out
                                dstP.copyOf(), svPtr)
                        CstringOps.copyAt(svPtr.bd, 1, dstP)
                    }
                    return@fcnRun FcnT.EX2
                }

                if (nargs == 3) {
                    if (svPtr.setpiece != 0)
                        Stubs.Mpiece(svPtr.setname,
                                svPtr.v1d.copyOfRange(1, svPtr.v1d.size),
                                svPtr.v1d.copyOfRange(iargs[1], svPtr.v1d.size),
                                svPtr.v1d.copyOfRange(iargs[2], svPtr.v1d.size),
                                svPtr.v1d.copyOfRange(iargs[2], svPtr.v1d.size), 1,
                                svPtr.pd1.copyOfRange(svPtr.sdlim, svPtr.pd1.size), svPtr)
                    else {
                        val dstP = CharArray(STR_MAX)
                        Stubs.Mpiece(dstP,
                                svPtr.v1d.copyOfRange(1, svPtr.v1d.size),
                                svPtr.v1d.copyOfRange(iargs[1], svPtr.v1d.size),
                                svPtr.v1d.copyOfRange(iargs[2], svPtr.v1d.size),
                                svPtr.v1d.copyOfRange(iargs[2], svPtr.v1d.size), 0,
                                dstP.copyOf(), svPtr)
                        CstringOps.copyAt(svPtr.bd, 1, dstP)
                    }
                    return@fcnRun FcnT.EX2
                }

                if (nargs != 4) return@fcnRun FcnT.ARG_COUNT

                if (svPtr.setpiece != 0)
                    Stubs.Mpiece(svPtr.setname,
                            svPtr.v1d.copyOfRange(1, svPtr.v1d.size),
                            svPtr.v1d.copyOfRange(iargs[1], svPtr.v1d.size),
                            svPtr.v1d.copyOfRange(iargs[2], svPtr.v1d.size),
                            svPtr.v1d.copyOfRange(iargs[3], svPtr.v1d.size), 1,
                            svPtr.pd1.copyOfRange(svPtr.sdlim, svPtr.pd1.size), svPtr)
                else {
                    val dstP = CharArray(STR_MAX)
                    Stubs.Mpiece(dstP,
                            svPtr.v1d.copyOfRange(1, svPtr.v1d.size),
                            svPtr.v1d.copyOfRange(iargs[1], svPtr.v1d.size),
                            svPtr.v1d.copyOfRange(iargs[2], svPtr.v1d.size),
                            svPtr.v1d.copyOfRange(iargs[3], svPtr.v1d.size), 0,
                            dstP.copyOf(), svPtr)
                    CstringOps.copyAt(svPtr.bd, 1, dstP)
                }

                return@fcnRun FcnT.EX2
            }

            //---------------------------------------------------------------
            // \u0024select() \u0024storage
            //---------------------------------------------------------------

            8 -> {
                if (nargs == 0) { // \u0024storage
                    val rsltS = svPtr.PD1
                    CstringOps.copyAtS(svPtr.bd, 1, String.format(Locale.US, "%d", rsltS))
                    return@fcnRun FcnT.EX2
                }

                //----------
                // \u0024select()
                //----------

                var iS2 = 0
                while (true) { // sel1:
                    CstringOps.strcpyS(tmp2, CstringOps.cstringAt(svPtr.v1d, iargs[iS2]))
                    val jS2 = Stubs.xindex(tmp2, cod209, 1.toShort()) - 1
                    if (jS2 <= 0) return@fcnRun FcnT.ERR
                    if (tmp2[jS2 - 1] == '0') {
                        iS2++
                        if (iS2 >= nargs) return@fcnRun FcnT.ERR
                        else continue // goto sel1
                    }
                    CstringOps.copyAtS(svPtr.bd, 1, CstringOps.cstringAt(tmp2, jS2 + 1))
                    return@fcnRun FcnT.EX2
                }
            }

            //---------------------------------------------------------------
            // \u0024test \u0024text() \u0024translate()
            //---------------------------------------------------------------

            9 -> {
                if (nargs == 0) { // \u0024test
                    svPtr.bd[1] = if (svPtr.tpx == 1L) '1' else '0'
                    svPtr.bd[2] = NUL
                    return@fcnRun FcnT.EX2
                }

                if (S == 'r' || S == 'R') { // \u0024translate() - goto trns

                    if (nargs < 2 || nargs > 3) return@fcnRun FcnT.ARG_COUNT

                    if (nargs == 2) { // 2 argument form
                        val rT = CharArray(256)
                        CstringOps.strcpyS(rT, CstringOps.cstringAt(svPtr.v1d, iargs[1]))
                        val rTs = CstringOps.cstring(rT)

                        var jT = 1
                        var iT = 1
                        while (svPtr.v1d[iT] != NUL) {
                            if (!rTs.contains(svPtr.v1d[iT])) {
                                svPtr.bd[jT] = svPtr.v1d[iT]
                                jT++
                            }
                            iT++
                        }
                        svPtr.bd[jT] = NUL
                        return@fcnRun FcnT.EX2
                    }

                    run {
                        // 3 argument form
                        val rT = CharArray(256)
                        val sT = CharArray(256)
                        val tT = CharArray(256)
                        CstringOps.strcpyS(tT, "")

                        CstringOps.strcpyS(rT, CstringOps.cstringAt(svPtr.v1d, iargs[1]))
                        CstringOps.strcpyS(sT, CstringOps.cstringAt(svPtr.v1d, iargs[2]))

                        val rTs = CstringOps.cstring(rT)
                        val sTs = CstringOps.cstring(sT)
                        val tTs: String
                        if (rTs.length > sTs.length) {
                            val iSl = sTs.length
                            CstringOps.strcpyS(tT, rTs.substring(iSl))
                            tTs = CstringOps.cstring(tT)
                        } else {
                            tTs = CstringOps.cstring(tT)
                        }

                        var i3 = 1
                        var j3 = 1
                        while (svPtr.v1d[i3] != NUL) {
                            val ch3 = svPtr.v1d[i3]
                            if (!tTs.contains(ch3)) {
                                if (!rTs.contains(ch3)) {
                                    svPtr.bd[j3] = ch3
                                    j3++
                                } else {
                                    // bd[j++] = s[strchr(r, v1d[i]) - r]
                                    svPtr.bd[j3] = sTs[rTs.indexOf(ch3)]
                                    j3++
                                }
                            }
                            i3++
                        }
                        svPtr.bd[j3] = NUL
                        return@fcnRun FcnT.EX2
                    }
                }

                //-------------------------------------------------------
                // \u0024text()
                //-------------------------------------------------------

                if (nargs != 1) return@fcnRun FcnT.ARG_COUNT

                var iTx = atoi(CstringOps.cstringAt(svPtr.v1d, 1))
                if (iTx <= 0) {
                    svPtr.bd[1] = NUL
                    return@fcnRun FcnT.EX2
                }
                var jTx = 1
                var kTx = 1
                while (kTx != iTx) {
                    jTx = jTx + CstringOps.cstringAt(svPtr.pd1, jTx).length + 1
                    if (jTx > svPtr.pd1len) {
                        svPtr.bd[1] = NUL
                        return@fcnRun FcnT.EX2
                    }
                    kTx++
                }
                iTx = 1
                while (svPtr.pd1[jTx] != NUL) {
                    if (svPtr.pd1[jTx] != TAB &&
                            svPtr.pd1[jTx] != ' ') {
                        svPtr.bd[iTx] = svPtr.pd1[jTx]
                        iTx++
                        jTx++
                    } else {
                        svPtr.bd[iTx] = ' '
                        iTx++
                        jTx++
                    }
                }
                svPtr.bd[iTx] = NUL
                return@fcnRun FcnT.EX2
            }

            //---------------------------------------------------------------
            // \u0024random() \u0024reverse()
            //---------------------------------------------------------------

            10 -> {
                if (nargs != 1) return@fcnRun FcnT.ARG_COUNT

                if (S == 'e' || S == 'E') { // \u0024reverse()
                    var jR = CstringOps.cstringAt(svPtr.v1d, 1).length + 1
                    svPtr.bd[jR] = NUL
                    var iR = 1
                    while (svPtr.v1d[iR] != NUL) {
                        jR--
                        svPtr.bd[jR] = svPtr.v1d[iR]
                        iR++
                    }
                    return
                }

                // MULTI_PRECISION gmp arm DEAD per the reference build
                // (H-018; journaled in the header, not rendered)

                val jR2 = atoi(CstringOps.cstringAt(svPtr.v1d, 1))

                if (jR2 < 2) {
                    svPtr.bd[1] = '0'
                    svPtr.bd[2] = NUL
                    return
                }
                // sprintf(&bd[1], "%d", rand() % j) - the seedless libc
                // rand(3) numeric intrinsic (H-010)
                CstringOps.copyAtS(svPtr.bd, 1,
                        String.format(Locale.US, "%d", kotlin.random.Random.nextInt(jR2)))

                return
            }

            11 -> { // \u0024x
                CstringOps.copyAtS(svPtr.bd, 1,
                        String.format(Locale.US, "%d", svPtr.hor[svPtr.io]))
                return@fcnRun FcnT.EX2
            }

            12 -> { // \u0024y
                CstringOps.copyAtS(svPtr.bd, 1,
                        String.format(Locale.US, "%d", svPtr.ver[svPtr.io]))
                return@fcnRun FcnT.EX2
            }

            13 -> { // \u0024io
                CstringOps.copyAtS(svPtr.bd, 1,
                        String.format(Locale.US, "%d", svPtr.io))
                return@fcnRun FcnT.EX2
            }

            14 -> { // \u0024horolog
                var timex = System.currentTimeMillis() / 1000L // time(&timex)
                var dayH = timex / 86400
                timex = timex - dayH * 86400
                dayH = 47116 + dayH
                var fdH = dayH + 1
                CstringOps.copyAtS(svPtr.bd, 1, String.format(Locale.US, "%d", fdH))
                CstringOps.catAt(svPtr.bd, 1, ",")
                fdH = timex
                CstringOps.strcpyS(tmp2, String.format(Locale.US, "%d", fdH))
                CstringOps.catAt(svPtr.bd, 1, tmp2)
                return
            }

            //------------------------
            // \u0024get()
            //------------------------

            15 -> {
                if (nargs < 1 || nargs > 3) return@fcnRun FcnT.ARG_COUNT

                // the following lines are unknown

                if (svPtr.v1d[1] == '\u0003') {
                    if (svPtr.v1d[2] == '\u0002') {
                        if (nargs == 1) {
                            CstringOps.copyAtS(svPtr.bd, 1, "")
                            return@fcnRun FcnT.EX2
                        }
                        CstringOps.copyAtS(svPtr.bd, 1,
                                CstringOps.cstringAt(svPtr.v1d, iargs[1]))
                        return@fcnRun FcnT.EX2
                    }
                    CstringOps.copyAtS(svPtr.bd, 1, CstringOps.cstringAt(svPtr.v1d, 2))
                    return@fcnRun FcnT.EX2
                }

                // actual \u0024get code

                val rsltG = CharArray(STR_MAX)
                val ctmpG2 = Stubs.sym_(1, svPtr.v1d.copyOfRange(1, svPtr.v1d.size), rsltG, svPtr)
                CstringOps.copyAt(svPtr.bd, 1, rsltG)

                if (ctmpG2 != null) return@fcnRun FcnT.EX2 // value returned and in bd

                if (nargs == 1) {
                    // the vendor's trivially-true NULL retest is dropped
                    // (H-013 duplicate-guard ledger)
                    CstringOps.copyAtS(svPtr.bd, 1, "")
                    return@fcnRun FcnT.EX2
                }

                if (nargs != 2) return@fcnRun FcnT.ARG_COUNT

                CstringOps.copyAtS(svPtr.bd, 1,
                        CstringOps.cstringAt(svPtr.v1d, iargs[1]))
                return@fcnRun FcnT.EX2
            }

            //---------------------------
            // Pattern match codes
            //---------------------------

            21, 22, 23, 24, 25, 26, 27, 28, 29 -> { // \u00241 .. \u00249

                val x = CharArray(10)
                x[0] = '_'
                x[1] = jB.toChar()
                x[2] = S
                x[3] = NUL
                svPtr.ierr = 0
                val rsltP = CharArray(STR_MAX)
                if (Stubs.sym_(1, x, rsltP, svPtr) == null) {
                    System.out.print("*** " + CstringOps.cstring(x) +
                            ": No such Perl back reference in or near line " +
                            svPtr.LineNumber + "\n\n")
                    Stubs.sigint(100)
                }
                CstringOps.copyAt(svPtr.bd, 1, rsltP)
                return
            }

            //----------------------------
            // \u0024name()
            // \u0024next()
            // \u0024noerr
            //----------------------------

            30 -> { // \u0024name() \u0024next() \u0024noerr

                if (S == 'o' || S == 'O') { // \u0024noerr
                    CstringOps.copyAtS(svPtr.bd, 1,
                            String.format(Locale.US, "%d", svPtr.NOERR))
                    svPtr.ierr = 0
                    return
                }

                if (S == 'a' || S == 'A') { // \u0024name() section

                    if (CstringOps.cstringAt(svPtr.v1d, 1) == "<lcl>") {
                        Stubs.BuildLocal(2, 0, cstr(""), tmp2, svPtr)
                        val dstN = CharArray(STR_MAX)
                        if (nargs == 1) LocalName(tmp2, dstN, null, svPtr)
                        else LocalName(tmp2, dstN,
                                svPtr.v1d.copyOfRange(iargs[1], svPtr.v1d.size), svPtr)
                        CstringOps.copyAt(svPtr.bd, 1, dstN)
                    } else if (CstringOps.cstringAt(svPtr.v1d, 1) == "<gbl>") {

                        // globals processed by compiler have x01 codes embedded.
                        // use LocalName() here because interpreter does not
                        // embed x01's at this point.

                        Stubs.BuildGlobal(2, 0, cstr(""), tmp2, svPtr)
                        val dstN = CharArray(STR_MAX)
                        if (nargs == 1) LocalName(tmp2, dstN, null, svPtr)
                        else LocalName(tmp2, dstN,
                                svPtr.v1d.copyOfRange(iargs[1], svPtr.v1d.size), svPtr)
                        CstringOps.copyAt(svPtr.bd, 1, dstN)
                    }
                    svPtr.ierr = 0
                    return
                }

                // \u0024next() section

                if ((S != 'e' && S != 'E') &&
                        (S in 'a'..'z' || S in 'A'..'Z'))
                    return@fcnRun FcnT.ERR // second char not correct

                if (CstringOps.cstringAt(svPtr.v1d, 1) == "<gbl>") {
                    val dstN2 = CharArray(STR_MAX)
                    if (Stubs.BuildGlobal(1, NEXT,
                                    svPtr.v1d.copyOfRange(1, svPtr.v1d.size), dstN2, svPtr) == 0)
                        return@fcnRun FcnT.ERR
                    CstringOps.copyAt(svPtr.bd, 1, dstN2)
                    if (CstringOps.cstringAt(svPtr.bd, 1).isEmpty())
                        CstringOps.copyAtS(svPtr.bd, 1, "-1")
                } else {
                    val dstN3 = CharArray(STR_MAX)
                    if (Stubs.BuildLocal(1, NEXT,
                                    svPtr.v1d.copyOfRange(1, svPtr.v1d.size), dstN3, svPtr) == 0)
                        return@fcnRun FcnT.ERR
                    CstringOps.copyAt(svPtr.bd, 1, dstN3)
                }
                svPtr.ierr = 0
                return
            }

            32 -> { // \u0024order() section

                if (nargs == 1) {

                    if (svPtr.v1d[1] == '^') {
                        val dstO = CharArray(STR_MAX)
                        Stubs.GlobalOrder(svPtr.v1d.copyOfRange(1, svPtr.v1d.size),
                                dstO, "1", svPtr)
                        CstringOps.copyAt(svPtr.bd, 1, dstO)
                        return@fcnRun FcnT.EX2
                    }

                    val dstO2 = CharArray(STR_MAX)
                    if (Stubs.BuildLocal(1, ORDERNEXT_FC,
                                    svPtr.v1d.copyOfRange(1, svPtr.v1d.size), dstO2, svPtr) == 0)
                        return@fcnRun FcnT.ERR
                    CstringOps.copyAt(svPtr.bd, 1, dstO2)
                    return@fcnRun FcnT.EX2
                }

                if (nargs != 2) return@fcnRun FcnT.ERR

                if (CstringOps.cstringAt(svPtr.v1d, iargs[1]) == "1") {

                    if (svPtr.v1d[1] == '^') {
                        val dstO = CharArray(STR_MAX)
                        Stubs.GlobalOrder(svPtr.v1d.copyOfRange(1, svPtr.v1d.size),
                                dstO, "1", svPtr)
                        CstringOps.copyAt(svPtr.bd, 1, dstO)
                        return@fcnRun FcnT.EX2
                    }

                    val dstO2 = CharArray(STR_MAX)
                    if (Stubs.BuildLocal(1, ORDERNEXT_FC,
                                    svPtr.v1d.copyOfRange(1, svPtr.v1d.size), dstO2, svPtr) == 0)
                        return@fcnRun FcnT.ERR
                    else {
                        CstringOps.copyAt(svPtr.bd, 1, dstO2)
                        return@fcnRun FcnT.EX2
                    }
                }

                if (CstringOps.cstringAt(svPtr.v1d, iargs[1]) == "-1") {

                    if (svPtr.v1d[1] == '^') {
                        val dstO = CharArray(STR_MAX)
                        Stubs.GlobalOrder(svPtr.v1d.copyOfRange(1, svPtr.v1d.size),
                                dstO, "-1", svPtr)
                        CstringOps.copyAt(svPtr.bd, 1, dstO)
                        return@fcnRun FcnT.EX2
                    }

                    val dstO2 = CharArray(STR_MAX)
                    if (Stubs.BuildLocal(1, ORDERPREV_FC,
                                    svPtr.v1d.copyOfRange(1, svPtr.v1d.size), dstO2, svPtr) == 0)
                        return@fcnRun FcnT.ERR
                    else {
                        CstringOps.copyAt(svPtr.bd, 1, dstO2)
                        return@fcnRun FcnT.EX2
                    }
                }
                return@fcnRun FcnT.ERR
            }

            31 -> { // \u0024query(x) \u0024qlength(x) \u0024qsubscript(x,y)

                //-------------------------
                // \u0024query()
                //-------------------------

                if (S == 'u' || S == 'U' ||
                        !(S in 'a'..'z' || S in 'A'..'Z')) { // \u0024query()

                    if (nargs > 1 || nargs == 0) return@fcnRun FcnT.ARG_COUNT

                    if (CstringOps.cstringAt(svPtr.v1d, 1) == "<lcl>") {
                        Stubs.BuildLocal(2, 0, cstr(""), tmp2, svPtr)
                        val dstQ = CharArray(STR_MAX)
                        LocalName(tmp2, dstQ, null, svPtr)
                        CstringOps.copyAt(svPtr.bd, 1, dstQ)
                        CstringOps.copyAtS(svPtr.v1d, 1, CstringOps.cstring(tmp2))
                        // Qsub(&v1d[1], tmp2, "-2", sv) - the a-argument is
                        // the vendor's live v1d slice (the keyfix slice
                        // copy precedent; the in-place write back is the
                        // seat owner's future business)
                        Stubs.Qsub(svPtr.v1d.copyOfRange(1, svPtr.v1d.size),
                                tmp2, cstr("-2"), svPtr)
                        var iQ = 1
                        while (svPtr.v1d[iQ] != CHAR_1 && svPtr.v1d[iQ] != NUL) iQ++
                        if (svPtr.v1d[iQ] == NUL) {
                            svPtr.v1d[iQ] = CHAR_1
                            iQ++
                            svPtr.v1d[iQ] = NUL
                        }
                        // sym_((int) 101, &v1d[1], &bd[1], sv)
                        val rsltQ = CharArray(STR_MAX)
                        Stubs.sym_(101, svPtr.v1d.copyOfRange(1, svPtr.v1d.size),
                                rsltQ, svPtr)
                        CstringOps.copyAt(svPtr.bd, 1, rsltQ)
                        val dstGN = svPtr.bd.copyOfRange(1, svPtr.bd.size)
                        GlobalName(dstGN, NUL, svPtr) // name fixer
                        CstringOps.copyAt(svPtr.bd, 1, dstGN)
                        return@fcnRun FcnT.EX2
                    } else if (CstringOps.cstringAt(svPtr.v1d, 1) == "<gbl>") {

                        // globals processed by compiler have x01 codes
                        // embedded. use LocalName() here because interpreter
                        // does not embed x01's at this point.

                        Stubs.BuildGlobal(2, 0, cstr(""), tmp2, svPtr)
                        val dstQ = CharArray(STR_MAX)
                        LocalName(tmp2, dstQ, null, svPtr)
                        CstringOps.copyAt(svPtr.bd, 1, dstQ)
                    }

                    // GlobalQuery(&bd[1], &bd[1], 0, sv) - the tmp0 slice
                    // and the tmp1 scratch with the copyAt write back (the
                    // vendor bd-aliasing ledger)
                    val gqIn = svPtr.bd.copyOfRange(1, svPtr.bd.size)
                    val gqOut = CharArray(STR_MAX)
                    GlobalQuery(gqIn, gqOut, NUL, svPtr)
                    CstringOps.copyAt(svPtr.bd, 1, gqOut)
                    return@fcnRun FcnT.EX2
                }

                //-----------------------
                // \u0024qlength()
                //-----------------------

                if (S == 'l' || S == 'L') { // \u0024qlength()

                    val op1 = CharArray(512)

                    if (nargs != 1) return@fcnRun FcnT.ARG_COUNT

                    if (CstringOps.cstringAt(svPtr.v1d, 1) == "<lcl>") {
                        Stubs.BuildLocal(2, 0, cstr(""), op1, svPtr) // extract lcl stack
                        if (Stubs.Eval(op1, op1, svPtr) != 0) return@fcnRun FcnT.QLENERR
                    } else if (CstringOps.cstringAt(svPtr.v1d, 1) == "<gbl>") {
                        Stubs.BuildGlobal(2, 0, cstr(""), op1, svPtr) // get global indx nbr
                        // (the vendor Eval call is commented here - ledger)
                    } else CstringOps.strcpyS(op1, CstringOps.cstringAt(svPtr.v1d, 1))

                    val dstL2 = CharArray(STR_MAX)
                    // Qlength(op1, &bd[1], sv)
                    Stubs.Qlength(op1, dstL2, svPtr)
                    CstringOps.copyAt(svPtr.bd, 1, dstL2)
                    return@fcnRun FcnT.EX2
                }

                //------------------------
                // \u0024qsubscript()
                //------------------------

                if (S == 's' || S == 'S') { // \u0024qsubscript()

                    if (nargs != 2) return@fcnRun FcnT.ARG_COUNT

                    if (CstringOps.cstringAt(svPtr.v1d, iargs[1]) == "-1") {
                        // return current working directory (the getcwd
                        // user-dir intrinsic; the NULL arm is unreachable,
                        // H-013)
                        CstringOps.copyAtS(svPtr.bd, 1, System.getProperty("user.dir"))
                        return@fcnRun FcnT.EX2
                    }

                    // order of these operations is important because of the
                    // stack. do not change.

                    val op2 = CharArray(512)

                    if (CstringOps.cstringAt(svPtr.v1d, iargs[1]) == "<lcl>") {
                        Stubs.BuildLocal(2, 0, cstr(""), op2, svPtr) // extract lcl stack
                        var iQ2 = 0
                        while (op2[iQ2] != NUL) {
                            if (op2[iQ2] == CodedOpen.toChar()) op2[iQ2] = '('
                            else if (op2[iQ2] == CodedClose.toChar()) op2[iQ2] = ')'
                            else if (op2[iQ2] == CodedComma.toChar()) op2[iQ2] = ','
                            iQ2++
                        }
                        if (Stubs.Eval(op2, op2, svPtr) != 0) return@fcnRun FcnT.QSUBERR1
                    } else if (CstringOps.cstringAt(svPtr.v1d, iargs[1]) == "<gbl>") {
                        Stubs.BuildGlobal(2, 0, cstr(""), op2, svPtr) // get global indx nbr
                        if (Stubs.Eval(op2, op2, svPtr) != 0) return@fcnRun FcnT.QSUBERR1
                    } else CstringOps.strcpyS(op2,
                            CstringOps.cstringAt(svPtr.v1d, iargs[1]))

                    val op1q = CharArray(512)

                    if (CstringOps.cstringAt(svPtr.v1d, 1) == "<lcl>") {
                        Stubs.BuildLocal(2, 0, cstr(""), op1q, svPtr) // extract lcl stack
                        if (Stubs.Eval(op1q, op1q, svPtr) != 0) return@fcnRun FcnT.QSUBERR1
                    } else if (CstringOps.cstringAt(svPtr.v1d, 1) == "<gbl>") {
                        Stubs.BuildGlobal(2, 0, cstr(""), op1q, svPtr) // get global indx nbr
                        // (the vendor Eval call is commented here - ledger)
                    } else CstringOps.strcpyS(op1q, CstringOps.cstringAt(svPtr.v1d, 1))

                    val dstX = CharArray(STR_MAX)
                    // Qsub(op1, &bd[1], op2, sv)
                    Stubs.Qsub(op1q, dstX, op2, svPtr)
                    CstringOps.copyAt(svPtr.bd, 1, dstX)

                    return@fcnRun FcnT.EX2
                }

                return@fcnRun FcnT.UNKNOWN // function not recognized
            }

            //---------------------------------------------------------------
            // unknown code
            //---------------------------------------------------------------

            // default: the Kotlin else-last grammar rule moves the
            // vendor's leading default arm here (the opcode 99 arm
            // guard above makes the arm-reach identical; H-011 note)

            else -> {
                svPtr.ierr = 30
                return@fcnRun FcnT.UNKNOWN
            }
        }
    }

    // ex2: and the vendor goto-tail bouquet

    when (tail) {
        FcnT.EX2 -> {
            svPtr.ierr = 0
            return
        }
        else -> {
            when (tail) {
                FcnT.ERR -> System.out.print("\n*** Function error in or near line " +
                        svPtr.LineNumber + "\n\n")
                FcnT.FNUM_WRONG -> System.out.print("\n*** Function error: " + DOLLAR_FC +
                        "fnumber pattern error. In or near line " + svPtr.LineNumber + "\n\n")
                FcnT.QLENERR -> System.out.print("\n*** Qlength Function argument error. " +
                        "In or near line " + svPtr.LineNumber + "\n\n")
                FcnT.QSUBERR1 -> System.out.print("\n*** Qsubscript Function error: 1nd " +
                        "argument. In or near line " + svPtr.LineNumber + "\n\n")
                FcnT.ARG_COUNT -> System.out.print("\n*** Function error: wrong number of " +
                        "arguments. In or near line " + svPtr.LineNumber + "\n\n")
                else -> System.out.print("\n*** Unknown function. In or near line " +
                        svPtr.LineNumber + "\n\n")
            }
            Stubs.sigint(100)
        }
    }
}

// free(svPtr1): the C free is the JVM GC no-op (rendered call site
// duty; the GlobalQuery precedent keeps this a named note carrier)
private fun freeLedgerFcn() {
    return
}
