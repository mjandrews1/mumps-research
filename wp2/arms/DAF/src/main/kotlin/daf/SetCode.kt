package daf

// Unit 121 (DAF B4 T3, complex): compiler-set.c::set_code (256 LOC):
// the SET/NEW/KILL/READ/LOCK LHS+RHS emitter: parses the LHS list,
// stacks lhs[] targets, then emits the stores (Mglobal global stores,
// the \\$piece/\\$extract LHS Interpret rebuilds, and the sym_
// SYMSTORE local stores), with the Merge rhs splice and the @-indirect
// READ/SET shortcuts.
// Consults: BASE-log rows 10/73 hit-adapt (goto set/set_store/nxt_lhs/
// rhs1 -> the labeled-loop structure below; the rhs1 ',' chain re-enters
// set exactly as the vendor goto), row 2/26 hit-reuse (every
// fprintf(Out,...) -> C.emit with the emitted C text verbatim, incl.
// the vendor LOCK-store emit line that cites one conversion for one
// argument - vendor text kept verbatim per row 26; the \\" and %%s of
// the sprintf rebuild formats preserved), row 33 family hit-adapt
// (malloc/strcpy/strlen of the C-side carrier record only: nothing is
// heap-allocated by the render itself), rows 6/22 hit-reuse (compiler
// TU globals in DafCompilerState; the lhs[25] char* array folds onto
// the lhs Array(25) String store; the malloc MEM_ERROR NULL branch is
// UNREACHABLE on the JVM - logged divergence), row 99 (the 99 ABRTX
// sentinel ride), row 81 (the pd1[sdlim] NUL-terminated reads via
// cstringAt text and the vd[i]<128 coded-marker walks kept
// verbatim - those loop tests are the vendor's coded-carrier walk),
// row 30 (the Extract2/Extract1 out-param folds of the extract
// seats; the &pd1[sdlim] arguments are emitted as literal text),
// row 92 (the emitted sym_ SYMSTORE text; seat-mediated).
// Seats: setCodeDaf IS this render (the lock_code delegate seat of
// unit 116 resolves here); atTestDaf (T2 unit-109 render: the
// &pd1Last out-param folds onto the C.pd1Last value copy - ledgered
// value fold); atCopyDaf/parseDaf/localArrayDaf/globalArrayDaf/
// extract2Daf/extract1Daf/cvarDaf/memcheckDaf (named-owner stubs).
// Dafny: EXEMPT (emitter; journal row).

fun setCodeDaf(entryPt: Int): Int {

    val C = DafCompilerState
    val sv = DafInterpState.svPtr

    if (entryPt == SET_STORE_D) return setStoreDaf() // goto set_store

    var tmp: StringBuilder

    // set: (the rhs1 ',' re-entry targets this head; lhsx/lhsf reset
    // exactly as the vendor does at the set label)
    while (true) {

        // { for (i=0; i<lhsx; i++) free(lhs[i]); }
        // (JVM heap fold row 22: the free() loop clears the carriers)
        for (kx in 0 until C.lhsx) C.lhs[kx] = ""

        C.lhsf = 0
        C.lhsx = 0

        if (at(C.xd, C.xpx) == '(') { // (a,b)= style LHS list
            C.lhsf = 1
            C.xpx++
        }

        C.Set_flg = 0

        C.at_flg = atTest(C.xd, C.pd1, C.pd1Last, C.lines.toShort(), C.xpx)
        // (vendor passes &pd1Last; the out-param folds onto the value
        // copy - ledgered value fold, rows 22/30)

        if (C.at_flg == 99) return ABRTX_D // goto abrtx

        if (C.at_flg != 0 && C.retz == CMD_READ_D) {
            tmp = StringBuilder("r ")  // strcpy(tmp, "r ")
            atCopyDaf(C.at_flg, tmp.toString().toCharArray())
            emitInterpretDaf(tmp.toString()) // INTERPRET
            C.xpx = C.at_flg
            if (C.POST_flg != 0) {
                C.emit("\t} // post conditional 2097\n")
                C.POST_flg = 0
            }
            return 0 // goto next_cmnd
        }

        if (C.at_flg != 0) {
            tmp = StringBuilder("s ") // strcpy(tmp, "s ")
            atCopyDaf(C.at_flg, tmp.toString().toCharArray())
            emitInterpretDaf(tmp.toString()) // INTERPRET
            C.xpx = C.at_flg
            if (C.POST_flg != 0) {
                C.emit("\t} /* post conditional 2116*/\n")
                C.POST_flg = 0
            }
            return 0 // goto next_cmnd
        }

        // nxt_lhs:
        var continueNxtLhs = true // the goto nxt_lhs fold
        while (continueNxtLhs) {
            continueNxtLhs = false

            C.t0px = 1
            C.t2 = 1
            C.setname[0] = NUL_D

            C.ierr = parseDaf(sv)
            if (C.ierr != 0) return PARSE_ERROR_D

            copySliceDaf(C.vd, C.v1d, 1) // strcpy(vd, &v1d[1])

            // LocalArray(vd, &Set_flg): the &Set_flg write-back rides
            // the IntCellD carrier (row 30 pointer fold)
            val setFlgCell = IntCellD(C.Set_flg)
            localArrayDaf(DafCRuntime.cstring(C.vd), setFlgCell)
            C.Set_flg = setFlgCell.v

            if (C.v1d[1] == '^') {
                if (C.retz == CMD_NEW1_D || C.retz == CMD_NEW_D) {
                    return NO_GLOBAL_PERMITTED_D // goto nogblpermitted
                }
            }

            // GlobalArray(v1d, &GBL_flg, vd)
            val gblFlgCell = IntCellD(C.GBL_flg)
            globalArrayDaf(C.v1d, gblFlgCell, DafCRuntime.cstring(C.vd))
            C.GBL_flg = gblFlgCell.v

            if (C.retz == CMD_KILL_D) return 0  // goto kill_return
            if (C.retz == CMD_NEW_D) return 0   // goto new_return
            if (C.retz == CMD_NEW1_D) return 0 // goto new1_return
            if (C.retz == CMD_LOCK_D) return 0  // goto lock_return

            if (C.lhsx > 24) return TOO_MANY_LHS_D // goto too_lhs
            // lhs[lhsx]=malloc(...): the MEM_ERROR NULL branch is
            // unreachable on the JVM (rows 22/46; ledgered)
            C.lhs[C.lhsx] = DafCRuntime.cstring(C.vd) // strcpy(lhs[lhsx],vd)
            C.lhsGF[C.lhsx] = C.GBL_flg
            C.lhsx++

            // vendor quirk kept: ',' -> nxt_lhs, but ')' falls through
            if (at(C.xd, C.xpx + 1) == ',' || at(C.xd, C.xpx + 1) == ')') {
                if (at(C.xd, C.xpx + 1) != ')') {
                    C.xpx += 2
                    continueNxtLhs = true // goto nxt_lhs
                }
            }
        }

        if (C.lhsf != 0) C.xpx++

        if (C.retz == CMD_READ_D) return 0 // goto set_return

        C.xpx += 2
        C.t0px = 0
        C.t2 = 1

        if (C.MergeFlg != 0) {
            C.t0px = 1
            C.setname[0] = NUL_D
            C.emit("\tstrcpy((char *) tmp${C.iiitmp}, " +
                    "(const char *) atmp); /* hold result */\n")
            C.MergeTmp = C.iiitmp
            C.iiitmp++
            memcheckDaf(C.iiitmp)
        }

        C.ierr = parseDaf(sv) // process RHS
        if (C.ierr != 0) return PARSE_ERROR_D

        if (C.MergeFlg != 0) {
            copySliceDaf(C.vd, C.v1d, 1) // strcpy(vd, &v1d[1])
            val setFlgCell2 = IntCellD(C.Set_flg)
            localArrayDaf(DafCRuntime.cstring(C.vd), setFlgCell2)
            C.Set_flg = setFlgCell2.v
            val gblFlgCell2 = IntCellD(C.GBL_flg)
            globalArrayDaf(C.v1d, gblFlgCell2, DafCRuntime.cstring(C.vd))
            C.GBL_flg = gblFlgCell2.v

            // if (strcmp(vd,"atmp")==0) sprintf(lhs[0], "tmp%d", MergeTmp)
            if (DafCRuntime.cstring(C.vd) == "atmp") {
                C.lhs[0] = "tmp${C.MergeTmp}"
            }

            C.emit("\tf=Merge(${C.lhs[0]}, ${DafCRuntime.cstring(C.vd)}, " +
                    "svPtr); \n")
            C.emit("\tif (f != 0) ErrorMessage(\\\"Merge failed \\\", " +
                    "svPtr->LineNumber); \n")

            C.xpx++
            return rhs1Daf() // goto rhs1
        }

        return setStoreDaf() // fall into set_store
    }
}

// set_store: the SET_STORE entry path and the set fall-through
private fun setStoreDaf(): Int {

    val C = DafCompilerState

    for (ix in 0 until C.lhsx) { // for (i = 0; i < lhsx; i++)

        C.GBL_flg = C.lhsGF[ix]

        if (C.GBL_flg != 0) {

            if (C.retz == CMD_READ_D) {
                C.emit("\n")
                C.emit("\t{ \n")
                C.emit("\tunsigned char seg[STR_MAX]; \n")
                C.emit("\tstrcpy(seg, ${extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim))}); \n")
                C.emit("\tf = Mglobal(STORE, ${extract2Daf(C.lhs[ix])}, " +
                        "seg, svPtr); \n")
                C.emit("\t} \n")
                C.emit("\n")
            }

            if (C.retz == CMD_SET_D) {
                C.emit("\n")
                C.emit("\t{ \n")
                C.emit("\tf = Mglobal(STORE, ${extract2Daf(C.lhs[ix])}, " +
                        "${extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim))}, " +
                        "svPtr); \n")
                C.emit("\t} \n")
                C.emit("\n")
            }

            // vendor quirk kept verbatim (row 26): the LOCK store line's
            // format cites ONE conversion and one argument lhs[i]
            if (C.retz == CMD_LOCK_D) {
                C.emit("\tf=Mglobal(LOCK, %s, \\\"\\\", lhs[i], svPtr); \n")
            }
        } else if (C.vd[0] == '$' &&
                   (toLowerAscii(C.vd[1]) == 'p' || toLowerAscii(C.vd[1]) == 'e')) {
            // Process LHS \\$PIECE / \\$EXTRACT
            setCodePieceExtractDaf()
        } else {

            if (cvarDaf(C.lhs[ix])) {
                C.emit("\tstrcpy( (char *) ${C.lhs[ix]}, " +
                        "(const char *) ${extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim))}); " +
                        "// Store result s264\n")
            } else {
                if (C.Set_flg != 0) {
                    C.emit("\tsym_(SYMSTORE, (UNS) ${C.lhs[ix]}, " +
                            "(UNS) ${extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim))}, " +
                            "svPtr); // Store result s270 \n")
                } else {
                    C.emit("\tsym_(SYMSTORE, (UNS) \\\"${C.lhs[ix]}\\\", " +
                            "(UNS) ${extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim))}, " +
                            "svPtr); // Store result s274 \n")
                }
            }
        }
    } // lhs

    return rhs1Daf()
}

// the \\$piece/\\$extract LHS rebuild block. Vendor coded-marker walks
// (for (i=0; vd[i]<128; i++); etc.) and the Extract1/Extract2 call
// results are kept verbatim; the <128 tests render as Char code
// compares exactly as the C char compares. NOTE (row 26 verbatim
// discipline): the emitted sprintf format tails are byte-for-byte the
// vendor formats after their %% folds; the extraction carriers var/
// pat/strt/end are CharArray(256) locals exactly as the C locals.
private fun setCodePieceExtractDaf() {

    val C = DafCompilerState
    val vd = C.vd

    var i: Int
    var j: Int

    val varB = CharArray(256)
    val patB = CharArray(256)
    val strtB = CharArray(256)
    val endB = CharArray(256)
    val rstrB = CharArray(256)

    i = 0
    while (vd[i] < '\u0080') i++     // for (i = 0; vd[i] < 128; i++);
    copySliceDaf(varB, vd, i + 1)    // strcpy(var, &vd[i + 1])
    j = 0
    while (varB[j] < '\u0080') j++
    varB[j] = NUL_D

    i++
    while (vd[i] < '\u0080') i++     // for (i++; vd[i] < 128; i++);
    copySliceDaf(patB, vd, i + 1)    // strcpy(pat, &vd[i + 1])
    j = 0
    while (patB[j] < '\u0080') j++
    patB[j] = NUL_D

    i++
    while (vd[i] < '\u0080' && vd[i] != NUL_D) i++
    copySliceDaf(strtB, vd, i + 1)   // strcpy(strt, &vd[i + 1])
    j = 0
    while (strtB[j] < '\u0080') j++
    strtB[j] = NUL_D

    i++
    while (vd[i] < '\u0080' && vd[i] != NUL_D) i++

    endB[0] = NUL_D
    i++
    if (at(vd, i) == NUL_D) {
        endB[0] = NUL_D              // strcpy(end, "")
    } else {
        copySliceDaf(endB, vd, i)    // strcpy(end, &vd[i])
        j = 0
        while (endB[j] < '\u0080') j++
        endB[j] = NUL_D
    }

    setStrB(patB, extract2Daf(DafCRuntime.cstring(patB)))  // strcpy(pat, Extract2(pat))
    setStrB(strtB, extract2Daf(DafCRuntime.cstring(strtB)))
    setStrB(endB, extract2Daf(DafCRuntime.cstring(endB)))
    setStrB(rstrB, extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim)))

    val pat = DafCRuntime.cstring(patB)
    val strt = DafCRuntime.cstring(strtB)
    val endS = DafCRuntime.cstring(endB)

    //--------------------
    //  PIECE LHS
    //--------------------
    if (toLowerAscii(vd[1]) == 'p') {

        val strt2 = if (strt.isEmpty()) "\\\"1\\\"" else strt // strlen(strt)==0

        val tmpS = extract1Daf(varB, 0) // strcpy(tmp, Extract1(var))

        // first character is a " character / last character is a "
        val isName = tmpS.startsWith("\\\"") // tmp[0] == '\"'
        val name: String
        if (isName) { // variable name not rslt of eval
            name = tmpS.substring(1, tmpS.length - 1) // strip quotes
        } else {
            name = tmpS // eval result (kept verbatim as the vendor does)
        }

        // 3 arg form / 4 arg form (both name and eval branches share
        // the same format text in the vendor source - row 26 verbatim)
        if (endS.isEmpty()) { // 3 arg form
            C.emit("\n{ char x[STR_MAX]; sprintf(x, " +
                    "\\\"s \\\$p(${extract2Daf(name)}" +
                    ",\\\\\\\"%s\\\\\\\",\\\\\\\"%s\\\\\\\")" +
                    "=\\\\\\\"%s\\\\\\\" \\\", " +
                    "${extract2Daf(pat)}, ${extract2Daf(strt2)}, &pd1[sdlim]); \n")
        } else { // 4 arg form
            C.emit("\n{ char x[STR_MAX]; sprintf(x, " +
                    "\\\"s \\\$p(${extract2Daf(name)}" +
                    ",\\\\\\\"%s\\\\\\\",\\\\\\\"%s\\\\\\\",\\\\\\\"%s\\\\\\\")" +
                    "=\\\\\\\"%s\\\\\\\" \\\", " +
                    "${extract2Daf(pat)}, ${extract2Daf(strt2)}, " +
                    "${extract2Daf(endS)}, &pd1[sdlim]); \n")
        }

        emitInterpretTailDaf()
    }

    //-------------------
    //  EXTRACT LHS
    //-------------------
    else { // tolower(vd[1]) == 'e': \\$extract

        val tmpS = extract1Daf(varB, 0) // strcpy(tmp, Extract1(var))
        val isName = tmpS.startsWith("\\\"")
        val name: String
        if (isName) {
            name = tmpS.substring(1, tmpS.length - 1)
        } else {
            name = tmpS
        }

        if (strt.isNotEmpty()) { // 3 arg form
            if (isName) { // variable name: %s first conversion
                C.emit("\n{ char x[STR_MAX]; sprintf(x, " +
                        "\\\"s \\\$e(${extract2Daf(name)}" +
                        ",\\\\\\\"%s\\\\\\\",\\\\\\\"%s\\\\\\\")" +
                        "=\\\\\\\"%s\\\\\\\" \\\", " +
                        "${extract2Daf(pat)}, ${extract2Daf(strt)}); \n")
            } else { // rslt of exp eval: %%s first conversion
                C.emit("\n{ char x[STR_MAX]; sprintf(x, " +
                        "\\\"s \\\$e(%s,\\\\\\\"%s\\\\\\\",\\\\\\\"%s\\\\\\\")" +
                        "=\\\\\\\"%s\\\\\\\" \\\", " +
                        "${extract2Daf(name)}, ${extract2Daf(pat)}, " +
                        "${extract2Daf(strt)}, &pd1[sdlim]); \n")
            }
        } else { // 2 arg form
            if (isName) { // variable name: %s first conversion
                C.emit("\n{ char x[STR_MAX]; sprintf(x, " +
                        "\\\"s \\\$e(${extract2Daf(name)}" +
                        ",\\\\\\\"%s\\\\\\\")=\\\\\\\"%s\\\\\\\" \\\", " +
                        "${extract2Daf(pat)}); \n")
            } else { // rslt of exp eval: %%s first conversion
                C.emit("\n{ char x[STR_MAX]; sprintf(x, " +
                        "\\\"s \\\$e(%s,\\\\\\\"%s\\\\\\\")=\\\\\\\"%s\\\\\\\" " +
                        "\\\", ${extract2Daf(name)}, ${extract2Daf(pat)}); \n")
            }
        }

        emitInterpretTailDaf()
    }
}

// the vendor fprintf tail shared by both \\$p and \\$e LHS blocks
// (compiler-macros.h INTERPRET pin; row 26 verbatim)
private fun emitInterpretTailDaf() {
    val C = DafCompilerState
    C.emit("       _jj = Interpret( (CCHR) x, svPtr); \n")
    C.emit("       svPtr->ERROR = _jj; \n")
    C.emit("       if (!svPtr->NOERR && _jj < 0) \n")
    C.emit("               ErrorMessage(\\\"Indirection error\\\", " +
            "svPtr->LineNumber); \n")
    C.emit("};\n")
}

// INTERPRET macro emit over the @-indirect tmp (compiler-macros.h:39)
private fun emitInterpretDaf(tmp: String) {
    val C = DafCompilerState
    C.emit("\t_jj = Interpret( (CCHR) ${tmp}, svPtr); \n")
    C.emit("\tsvPtr->ERROR = _jj; \n")
    C.emit("\tif (!svPtr->NOERR && _jj < 0) \n")
    C.emit("\t\tErrorMessage(\\\"Indirection error\\\", svPtr->LineNumber); \n")
}

// rhs1: the shared exit fold
private fun rhs1Daf(): Int {
    val C = DafCompilerState
    if (C.retz == CMD_READ_D) return 0    // goto cont_read
    if (at(C.xd, C.xpx) == ',') {
        C.xpx++
        return setCodeDaf(-1)             // goto set (entryPt bypassed)
    }
    if (C.POST_flg != 0) {
        C.emit("\t} /* post conditional 2313*/\n")
        C.POST_flg = 0
    }
    return 0 // goto next_cmnd
}

// strcpy(dst, &src[off]) carrier fold
private fun copySliceDaf(dst: CharArray, src: CharArray, off: Int) {
    var n = off
    var d = 0
    while (n < src.size && src[n] != NUL_D) {
        dst[d++] = src[n++]
    }
    dst[d] = NUL_D
}

// strcpy(b, s) fold into a CharArray carrier
private fun setStrB(b: CharArray, s: String) {
    var d = 0
    while (d < s.length) { b[d] = s[d]; d++ }
    b[d] = NUL_D
}