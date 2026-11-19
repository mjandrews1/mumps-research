package doc

// Unit 121 (B4 tranche 3): compiler-set.c::set_code (batch 4,
// complex stratum; units.csv row 97: loc 256, ptr 15, ctrl 76,
// score 173.4, rank 288) - the SET command compiler entry of the
// arm (also the set-side of READ/KILL/NEW/LOCK/no-argument forms
// via retz). Two entry points (compiler-macros.h:83 SET 0 and :84
// SET_STORE 1; the entry_pt store arm jumps straight to the LHS
// store loop). SET: free of the previous LHS roster, the paren-
// roster flag, the atTest indirect arms (r/s INTERPRET arms, the
// H-003 CompilerState.interpret macro seat with the atCopy stub
// seat), the nxt_lhs loop (parse_ compiler-twin stub seat,
// v1d[1]-tail vd copy, the REAL LocalArray/GlobalArray unit
// renders, the NEW/NEW1 guard, the 25-deep malloc roster - malloc
// domain folds to the String seat per the CompilerState lhs
// carrier note, the vendor malloc-null MEM_ERROR arm is
// unreachable on the JVM and LEDGERED here, not rendered), the
// READ early-out, the MergeFlg hold-emission arms, the LHS store
// loop (global arms, the piece/extract indirection forest, the
// cvar store arms), and the rhs1 comma chain back to set.
//
// EMITTED C text kept byte-identical (the s2097/s2116/s2313 post
// conditional fossils, the s264/s270/s274 store comments, the
// seg/Merge/keyfix( via LOCK) lines); the vendor MISSING-ARG
// quirk of the LOCK emit (a percent-s format with NO argument -
// the percent-s lands in the emitted text verbatim; the
// subsequent args are empty) is kept byte-identical and ledgered.
// The piece/extract forest section: the vendor less-128 scans
// (marker bytes 128+ separating var/pat/strt/end) are rendered
// with the exact index arithmetic including the scan4 pre-
// increment tail read, bound-guarded for the C beyond-buffer
// Undefined Behavior fold (the Kotlin carrier reads zeros past
// the end; the vendor reads uninit bytes; the <128 idiom scans
// through NUL in BOTH domains - kept, with the bound guard the
// only divergence, ledgered). The var local of the vendor is
// renamed varArr (Kotlin reserved word; H-012 note). The rstr
// buffer of the vendor is a write-only dead store (copied and
// never read - H-013, not rendered). The unused i/j dead locals
// of the dead-ish wrapper section arms are not rendered (H-013).
// The vendor dirt of tmp[1024] reuse is scoped per vendor. The
// malloc-per-LHS strlen+1 sizing folds (String seat, H-008).
// tolower on the byte domain renders as the two-case compare
// (ASCII domain, H-012). The StrC compares (strcmp vd atmp)
// fold to String equals. sprintf tmp of MergeTmp folds to the
// Int concat (identical bytes, H-009 adjacent). The parse_()
// twin and atCopy and cvar and Extract1 seats are H-014 stubs
// (the batch-9 parse twin precedent of LockCode; cvar owner
// compiler-parse.c:2074; atCopy owner compiler-at-test.c).
// atTest, Extract2, LocalArray, GlobalArray are REAL renders
// (AtTest.kt, Extract2.kt, LocalArray.kt, GlobalArray.kt).
// Consult rows logged 2026-11-19T20:53:08Z. Naming per H-017
// (set_code, parse_, atTest, LocalArray, GlobalArray, Extract1,
// Extract2, cvar kept verbatim).

private const val PARSE_ERROR_SC = 1 // compiler-macros.h:69
private const val NO_GLOBAL_PERMITTED_SC = 3 // compiler-macros.h:71
private const val TOO_MANY_LHS_SC = 5 // compiler-macros.h:73
private const val ABRTX_SC = 6 // compiler-macros.h:74
private const val SET_STORE_SC = 1 // compiler-macros.h:84
private const val SET_SC = 's' // compiler-macros.h:91
private const val NEW_SC = 'n' // compiler-macros.h:96
private const val NEW1_SC = 'N' // compiler-macros.h:97
private const val READ_SC = 'r' // compiler-macros.h:100
private const val KILL_SC = 'k' // compiler-macros.h:108
private const val LOCK_SC = 'l' // compiler-macros.h:112
private const val DOLLAR_SC = '\u0024'
// the vendor 128 marker byte (the less-128 scan idiom)
private const val MARK_128 = '\u0080'

fun set_code(entry_pt: Int): Int {
    var gotoSetStore = entry_pt == SET_STORE_SC // goto set_store;

    while (true) { // set:
        if (!gotoSetStore) {
            var i = 0
            while (i < CompilerState.lhsx) { // for (i=0; i<lhsx; i++) free (lhs[i]);
                CompilerState.lhs[i] = null
                i++
            }

            CompilerState.lhsf = 0
            CompilerState.lhsx = 0

            if (CompilerState.xd[CompilerState.xpxIdx()] == '(') {
                CompilerState.lhsf = 1
                CompilerState.xpxInc()
            }

            CompilerState.Set_flg = 0

            CompilerState.at_flg = Stubs.atTest(
                CompilerState.xd, CompilerState.pd1,
                CompilerState.pd1Last, CompilerState.lines,
                CompilerState.xpx
            )

            if (CompilerState.at_flg == 99) return ABRTX_SC // goto abrtx;

            if (CompilerState.at_flg != 0 &&
                CompilerState.retz == READ_SC.code
            ) {
                val tmp = CharArray(1024)
                CstringOps.strcpyS(tmp, "r ")
                Stubs.atCopy(CompilerState.at_flg, tmp)

                CompilerState.interpret(CstringOps.cstring(tmp))

                CompilerState.xpx = CompilerState.at_flg.toLong()

                if (CompilerState.POST_flg != 0) {
                    CompilerState.emit("\t} // post conditional 2097\n")
                    CompilerState.POST_flg = 0
                }

                return 0 // goto next_cmnd;
            }

            if (CompilerState.at_flg != 0) {
                val tmp = CharArray(1024)
                CstringOps.strcpyS(tmp, "s ")
                Stubs.atCopy(CompilerState.at_flg, tmp)

                CompilerState.interpret(CstringOps.cstring(tmp))

                CompilerState.xpx = CompilerState.at_flg.toLong()
                if (CompilerState.POST_flg != 0) {
                    CompilerState.emit("\t} /* post conditional 2116*/\n")
                    CompilerState.POST_flg = 0
                }
                return 0 // goto next_cmnd;
            }
        } else {
            gotoSetStore = false
        }

        var gotoRhs1 = false

        // nxt_lhs:
        nxtLhs@ while (true) {
            CompilerState.t0px = 1
            CompilerState.t2 = 1
            CompilerState.setname[0] = NUL

            CompilerState.ierr = Stubs.parse_()
            if (CompilerState.ierr != 0) return PARSE_ERROR_SC

            CstringOps.strcpyS(CompilerState.vd, CstringOps.cstringAt(CompilerState.v1d, 1))

            val setFlgHolder = shortArrayOf(CompilerState.Set_flg.toShort())
            LocalArray(CompilerState.vd, setFlgHolder)
            CompilerState.Set_flg = setFlgHolder[0].toInt()

            if (CompilerState.v1d[1] == '^') {
                if (CompilerState.retz == NEW1_SC.code ||
                    CompilerState.retz == NEW_SC.code
                ) return NO_GLOBAL_PERMITTED_SC // goto nogblpermitted;
            }

            val gblFlgHolder = shortArrayOf(CompilerState.GBL_flg.toShort())
            GlobalArray(CompilerState.v1d, gblFlgHolder, CompilerState.vd)
            CompilerState.GBL_flg = gblFlgHolder[0].toInt()

            if (CompilerState.retz == KILL_SC.code) return 0 // goto kill_return;
            if (CompilerState.retz == NEW_SC.code) return 0 // goto new_return;
            if (CompilerState.retz == NEW1_SC.code) return 0 // goto new1_return;
            if (CompilerState.retz == LOCK_SC.code) return 0 // goto lock_return;

            if (CompilerState.lhsx > 24) return TOO_MANY_LHS_SC // goto too_lhs;

            // lhs[lhsx]=malloc(strlen(vd)+1) - String seat; the
            // malloc-null MEM_ERROR arm is unreachable on the JVM
            // (ledger note, not rendered).
            CompilerState.lhs[CompilerState.lhsx] = CstringOps.cstring(CompilerState.vd)
            CompilerState.lhsGF[CompilerState.lhsx] = CompilerState.GBL_flg
            CompilerState.lhsx++

            if (CompilerState.xd[CompilerState.xpxIdx() + 1] == ',' ||
                CompilerState.xd[CompilerState.xpxIdx() + 1] == ')'
            ) {
                if (CompilerState.xd[CompilerState.xpxIdx() + 1] != ')') {
                    CompilerState.xpx += 2
                    continue@nxtLhs
                }
            }

            if (CompilerState.lhsf != 0) CompilerState.xpxInc()

            if (CompilerState.retz == READ_SC.code) return 0 // goto set_return;

            CompilerState.xpx += 2
            CompilerState.t0px = 0
            CompilerState.t2 = 1

            if (CompilerState.MergeFlg != 0) {
                CompilerState.t0px = 1
                CompilerState.setname[0] = NUL
                CompilerState.emit(
                    "\tstrcpy((char *) tmp" + CompilerState.iiitmp +
                        ", (const char *) atmp); /* hold result */\n"
                )
                CompilerState.MergeTmp = CompilerState.iiitmp++
                Stubs.memcheck(CompilerState.iiitmp)
            }

            CompilerState.ierr = Stubs.parse_() // process RHS
            if (CompilerState.ierr != 0) return PARSE_ERROR_SC

            if (CompilerState.MergeFlg != 0) {
                CstringOps.strcpyS(
                    CompilerState.vd,
                    CstringOps.cstringAt(CompilerState.v1d, 1)
                )

                val sfh = shortArrayOf(CompilerState.Set_flg.toShort())
                LocalArray(CompilerState.vd, sfh)
                CompilerState.Set_flg = sfh[0].toInt()

                val gfh = shortArrayOf(CompilerState.GBL_flg.toShort())
                GlobalArray(CompilerState.v1d, gfh, CompilerState.vd)
                CompilerState.GBL_flg = gfh[0].toInt()

                if (CstringOps.cstring(CompilerState.vd) == "atmp") {
                    // sprintf(lhs[0], "tmp%d", MergeTmp)
                    CompilerState.lhs[0] = "tmp" + CompilerState.MergeTmp
                }

                CompilerState.emit(
                    "\tf=Merge(" + (CompilerState.lhs[0] ?: "") + ", " +
                        CstringOps.cstring(CompilerState.vd) + ", svPtr); \n"
                )

                CompilerState.emit(
                    "\tif (f != 0) ErrorMessage(\"Merge failed \", " +
                        "svPtr->LineNumber); \n"
                )

                CompilerState.xpxInc()
                gotoRhs1 = true // goto rhs1;
            }
            break@nxtLhs
        }

        if (!gotoRhs1) {
            // set_store:
            var i = 0
            while (i < CompilerState.lhsx) {
                CompilerState.GBL_flg = CompilerState.lhsGF[i]

                if (CompilerState.GBL_flg != 0) {
                    if (CompilerState.retz == READ_SC.code) {
                        CompilerState.emit("\n")
                        CompilerState.emit("\t{ \n")

                        CompilerState.emit("\tunsigned char seg[STR_MAX]; \n")

                        CompilerState.emit(
                            "\tstrcpy(seg, " + extract2str(CompilerState.pd1, CompilerState.sdlim) + "); \n"
                        )

                        CompilerState.emit(
                            "\tf = Mglobal(STORE, " +
                                extract2str(CompilerState.lhs[i] ?: "") +
                                ", seg, svPtr); \n"
                        )

                        CompilerState.emit("\t} \n")
                        CompilerState.emit("\n")

                    }

                    if (CompilerState.retz == SET_SC.code) {
                        CompilerState.emit("\n")
                        CompilerState.emit("\t{ \n")

                        CompilerState.emit(
                            "\tf = Mglobal(STORE, " +
                                extract2str(CompilerState.lhs[i] ?: "") + ", " +
                                extract2str(CompilerState.pd1, CompilerState.sdlim) +
                                ", svPtr); \n"
                        )

                        CompilerState.emit("\t} \n")
                        CompilerState.emit("\n")
                    }

                    if (CompilerState.retz == LOCK_SC.code) {
                        // vendor quirk: percent-s with NO argument
                        // - kept byte-identical
                        CompilerState.emit(
                            "\tf=Mglobal(LOCK, %s, \"\", lhs[i], svPtr); \n"
                        )
                    }
                } else if (CompilerState.vd[0] == DOLLAR_SC &&
                    (CompilerState.vd[1] == 'p' || CompilerState.vd[1] == 'P' ||
                        CompilerState.vd[1] == 'e' || CompilerState.vd[1] == 'E')
                ) { // Process LHS piece-family indirection

                    val varArr = CharArray(256)
                    val pat = CharArray(256)
                    val strt = CharArray(256)
                    val end = CharArray(256)

                    var i2 = 0
                    val vd = CompilerState.vd
                    while (i2 < vd.size && vd[i2] < MARK_128) i2
                    CstringOps.strcpyS(varArr, CstringOps.cstringAt(vd, i2 + 1))
                    var j = 0
                    while (j < varArr.size && varArr[j] < MARK_128) j++
                    varArr[j] = NUL

                    i2++
                    while (i2 < vd.size && vd[i2] < MARK_128) i2++
                    CstringOps.strcpyS(pat, CstringOps.cstringAt(vd, i2 + 1))
                    j = 0
                    while (j < pat.size && pat[j] < MARK_128) j++
                    pat[j] = NUL

                    i2++
                    while (i2 < vd.size && vd[i2] < MARK_128 &&
                        vd[i2] != NUL
                    ) i2++
                    CstringOps.strcpyS(strt, CstringOps.cstringAt(vd, i2 + 1))
                    j = 0
                    while (j < strt.size && strt[j] < MARK_128) j++
                    strt[j] = NUL

                    i2++
                    while (i2 < vd.size && vd[i2] < MARK_128 &&
                        vd[i2] != NUL
                    ) i2++

                    i2++ // the vendor pre-increment (vd[++i])
                    if (vd[i2] == NUL && i2 < vd.size) {
                        CstringOps.strcpyS(end, "")
                    } else if (i2 < vd.size) {
                        CstringOps.strcpyS(end, CstringOps.cstringAt(vd, i2))
                        j = 0
                        while (j < end.size && end[j] < MARK_128) j++
                        end[j] = NUL
                    }
                    // vendor beyond-buffer read: the Byte-domain
                    // fold is bound-guarded (banner ledger)

                    // strcpy(pat, Extract2(pat)); strcat twins;
                    // the rstr copy is the H-013 dead store
                    copyExtract2(pat)
                    copyExtract2(strt)
                    copyExtract2(end)

                    // -------------------- PIECE LHS
                    if (CompilerState.vd[1] == 'p' || CompilerState.vd[1] == 'P') {
                        if (CstringOps.strlenC(strt) == 0) {
                            CstringOps.strcpyS(strt, "\"1\"")
                        }

                        val tmp = CharArray(1024)

                        // Extract1(var) then the quote test
                        val e1 = Stubs.Extract1(varArr)
                        CstringOps.strcpyC(tmp, e1)

                        // cstring locals for the emission values
                        var tmpStr = CstringOps.cstring(tmp)
                        val patStr = CstringOps.cstring(pat)
                        val strtStr = CstringOps.cstring(Stubs.Extract2(strt, 0))
                        val pd1Tail = CstringOps.cstringAt(CompilerState.pd1, CompilerState.sdlim)
                        val endStr = CstringOps.cstring(end)

                        if (tmp[0] == '"') { // variable name not rslt of eval
                            tmpStr = tmpStr.substring(1)
                            tmpStr = tmpStr.substring(0, tmpStr.length - 1) // remove final quote

                            if (endStr.isEmpty()) { // 3 arg form
                                CompilerState.emit(
                                    "\n" +
                                        "{ char x[STR_MAX]; " +
                                        "sprintf(x, \"s \u0024p(" + tmpStr +
                                        ",\\\"%s\\\",\\\"%s\\\")=\\\"%s\\\" \", " +
                                        patStr + ", " + strtStr + ", " + pd1Tail + ");\n"
                                )
                            } else { // 4 arg form
                                CompilerState.emit(
                                    "\n" +
                                        "{ char x[STR_MAX]; " +
                                        "sprintf(x, \"s \u0024p(" + tmpStr +
                                        ",\\\"%s\\\",\\\"%s\\\",\\\"%s\\\")=\\\"%s\\\" \", " +
                                        patStr + ", " + strtStr + ", " + endStr + ", " +
                                        pd1Tail + "); \n"
                                )
                            }
                        } else { // eval result
                            if (endStr.isEmpty()) { // 3 arg form
                                CompilerState.emit(
                                    "\n" +
                                        "{ char x[STR_MAX]; " +
                                        "sprintf(x, \"s \u0024p(" + tmpStr +
                                        ",\\\"%s\\\",\\\"%s\\\")=\\\"%s\\\" \", " +
                                        patStr + ", " + strtStr + ", " + pd1Tail + ");\n"
                                )
                            } else { // 4 arg form
                                CompilerState.emit(
                                    "\n" +
                                        "{ char x[STR_MAX]; " +
                                        "sprintf(x, \"s \u0024p(" + tmpStr +
                                        ",\\\"%s\\\",\\\"%s\\\",\\\"%s\\\")=\\\"%s\\\" \", " +
                                        patStr + ", " + strtStr + ", " + endStr + ", " +
                                        pd1Tail + "); \n"
                                )
                            }
                        }

                        emitInterpretTail()
                    } else if (CompilerState.vd[1] == 'e' ||
                        CompilerState.vd[1] == 'E'
                    ) { // EXTRACT LHS

                        val tmp = CharArray(1024)

                        val e1 = Stubs.Extract1(varArr)
                        CstringOps.strcpyC(tmp, e1)

                        var tmpStr = CstringOps.cstring(tmp)
                        val patStr = CstringOps.cstring(pat)
                        val strtStr = CstringOps.cstring(Stubs.Extract2(strt, 0))
                        val pd1Tail = CstringOps.cstringAt(CompilerState.pd1, CompilerState.sdlim)

                        if (tmp[0] == '"') { // variable name not rslt of eval
                            tmpStr = tmpStr.substring(1)
                            tmpStr = tmpStr.substring(0, tmpStr.length - 1)

                            if (strtStr.isNotEmpty()) { // 3 arg form
                                CompilerState.emit(
                                    "\n" +
                                        "{ char x[STR_MAX]; " +
                                        "sprintf(x, \"s \u0024e(" + tmpStr +
                                        ",\\\"%s\\\",\\\"%s\\\")=\\\"%s\\\" \", " +
                                        patStr + ", " + strtStr + ", " + pd1Tail + "); \n"
                                )
                            } else { // 2 arg form
                                CompilerState.emit(
                                    "\n" +
                                        "{ char x[STR_MAX]; sprintf(x, \"s \u0024e(" +
                                        tmpStr + ",\\\"%s\\\")=\\\"%s\\\" \", " +
                                        patStr + ", " + pd1Tail + "); \n"
                                )
                            }
                        } else { // rslt of exp eval
                            if (strtStr.isNotEmpty()) { // 3 arg form
                                // the eval form keeps the literal
                                // percent-s target and passes TMP as
                                // the generated call first argument
                                CompilerState.emit(
                                    "\n" +
                                        "{ char x[STR_MAX]; " +
                                        "sprintf(x, \"s \u0024e(%s,\\\"%s\\\",\\\"%s\\\")=\\\"%s\\\" \", " +
                                        tmpStr + ", " + patStr + ", " + strtStr + ", " +
                                        pd1Tail + "); \n"
                                )
                            } else { // 2 arg form
                                CompilerState.emit(
                                    "\n" +
                                        "{ char x[STR_MAX]; sprintf(x, \"s \u0024e(%s,\\\"%s\\\")=\\\"%s\\\" \", " +
                                        tmpStr + ", " + patStr + ", " + pd1Tail + "); \n"
                                )
                            }
                        }

                        emitInterpretTail()
                    }
                } else {
                    if (Stubs.cvar(CompilerState.lhs[i] ?: "") != 0) {
                        CompilerState.emit(
                            "\tstrcpy( (char *) " + (CompilerState.lhs[i] ?: "") +
                                ", (const char *) " +
                                extract2str(CompilerState.pd1, CompilerState.sdlim) +
                                "); // Store result s264\n"
                        )
                    } else {
                        if (CompilerState.Set_flg != 0) {
                            CompilerState.emit(
                                "\tsym_(SYMSTORE, (UNS) " + (CompilerState.lhs[i] ?: "") +
                                    ", (UNS) " +
                                    extract2str(CompilerState.pd1, CompilerState.sdlim) +
                                    ", svPtr); // Store result s270 \n"
                            )
                        } else {
                            CompilerState.emit(
                                "\tsym_(SYMSTORE, (UNS) \"" + (CompilerState.lhs[i] ?: "") +
                                    "\", (UNS) " +
                                    extract2str(CompilerState.pd1, CompilerState.sdlim) +
                                    ", svPtr); // Store result s274 \n"
                            )
                        }
                    }
                }
                i++
            }
        }

        // rhs1:
        if (CompilerState.retz == READ_SC.code) return 0 // goto cont_read;

        if (CompilerState.xd[CompilerState.xpxIdx()] == ',') {
            CompilerState.xpxInc()
            continue // goto set;
        }

        if (CompilerState.POST_flg != 0) {
            CompilerState.emit("\t} /* post conditional 2313*/\n")
            CompilerState.POST_flg = 0
        }

        return 0 // goto next_cmnd;
    }
}

// strcpy(buf, Extract2(buf)) - the piece-family Extract2 fold back
// into the same buffer (the pat/strt/end triple of the vendor).
private fun copyExtract2(buf: CharArray) {
    val src = Stubs.Extract2(buf, 0)
    CstringOps.strcpyC(buf, src)
}

// Extract2 over a String seat (the String lhs roster domain of the
// malloc fold) - the string copy into the STR_MAX window then the
// arm Extract2 delegate (extract2(buf, offset), the two-argument
// arm signature; the one-argument vendor idiom is offset 0).
private fun extract2str(s: String, offset: Int = 0): String {
    val buf = CharArray(STR_MAX)
    CstringOps.strcpyS(buf, s)
    return CstringOps.cstring(Stubs.Extract2(buf, offset))
}

// Extract2 over a CharArray window (the pd1+sdlim idiom).
private fun extract2str(buf: CharArray, offset: Int): String =
    CstringOps.cstring(Stubs.Extract2(buf, offset))

// the five-line _jj emission tail shared by all four piece/extract
// indirection arms (byte-identical; the 7-space indents are the
// vendor's own).
private fun emitInterpretTail() {
    CompilerState.emit("       _jj = Interpret( (CCHR) x, svPtr); \n")
    CompilerState.emit("       svPtr->ERROR = _jj; \n")
    CompilerState.emit("       if (!svPtr->NOERR && _jj < 0) \n")
    CompilerState.emit("               ErrorMessage(\"Indirection error\", svPtr->LineNumber); \n")

    CompilerState.emit("};\n")
}
