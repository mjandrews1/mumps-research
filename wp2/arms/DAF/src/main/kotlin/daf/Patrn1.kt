package daf

// R1 stub-completion (2026-11-28, C-020 erratum session, tranche 6):
// the patrn1 DISTINCT-TWIN pair (same C symbol, two vendor files;
// the getstr1 unit-139/234 precedent):
//
// roster row 7 - compiler-sys1.c::patrn1 (the compiler TU form,
// vendor :743): the pattern argument (right side) set-up of the
// compiler parse: the quote arm appends the quoted literal to
// the bd[1] buffer (the doubled-quote 210 code for the escaped
// quote), the tail arm appends bytes until the terminator set
// " ,&:!''><=[)+-*/#_" - with the m paren-depth counter the
// compiler twin carries (a parenthesized pattern atom keeps the
// terminators appending until the depth returns to zero); the
// cursor leaves through the TU global xpx = jpx - 1 and ernbr
// returns 0 (or 2 on an unterminated quote). Consults
// (2026-11-28T00:00:00Z, nominal): BASE-log row 54 hit-reuse
// (the TU globals xd/xpx ride DafCompilerState; bd[1] rides the
// bp CharArray carrier of the CompilerParseC call site - the
// call site comment bp[0]=NUL_D IS bd[1]=0); row 15 hit-reuse
// (the terminator-set membership via xindexDaf, the
// CompilerSys1Xindex seat of the compiler TU, called with the
// 1-based from); rows 81/56 hit-adapt (the short *jpx/*ernbr
// pointer pair folds onto the jpx local reassignment at the
// call site + the Int return; the 210 code rides Char(210)).
// Vendor quirks kept: (1) the closing-quote arm falls into the
// tail which APPENDS the quote (the quote is not in the
// terminator set) and keeps scanning - the vendor control flow
// verbatim; (2) static tmp1/cod210 keep the {210,0} pair shape;
// (3) xpx = jpx - 1 leaves the cursor ON the terminator byte.
//
// roster row 3 - interp-parse.cpp::patrn1 (the interpreter
// svPtr form, vendor :1335): the interpreter twin WITHOUT the m
// counter (the interpreter pattern syntax has no parenthesized
// atoms): the same quote arm and tail over svPtr->xd appending
// to svPtr->bd[0] (NOT bd[1] - the interpreter buffer is
// 0-based), the cursor leaves through svPtr->xpx. NO DAF call
// site exists for this twin yet (the interpreter parse area
// reaches it through the vendor parse_ body; this render is the
// row-3 roster seat, standing alone as the getstr2 render did).
// Consults: the row-7 set verbatim with the svPtr carrier swap
// (DafMsv.xd/bd/xpx of DafCRuntime.kt).
// Dafny: EXEMPT (parse-state cursor walk over carrier seats;
// journal row).

private val PATRN1_COD210 = charArrayOf(Char(210), NUL_D) // cod210
// the terminator set as a NUL-terminated carrier (the vendor C
// string literal; xindex reads to the NUL - a bare toCharArray()
// over-reads, smoke-found)
private val PATRN1_TERM_C = (" ,&:!''><=[)+-*/#_" + NUL_D).toCharArray()

// void patrn1(short *, short *) - the compiler-sys1.c form
// (roster row 7). Fold: jpx by value (the call site's local),
// ernbr returned; the bd[1] string rides the bp carrier.
fun patrn1Daf(jpxIn: Int, ernbr: Int, bp: CharArray): Int {

    val C = DafCompilerState
    var jpx = jpxIn
    var m = 0 // int m=0 (the paren-depth counter)

    val bd = StringBuilder(DafCRuntime.cstringAt(bp, 0)) // &bd[1]

    val tmp1 = CharArray(2) // static unsigned char tmp1[2]
    tmp1[1] = NUL_D

    outer@ while (true) { // a1285:

        if (C.xd[jpx] == '"') {
            bd.append('"') // strcat(&bd[1], "\"")

            // the a1283 inner loop exits ONLY via the return-2 or
            // the goto a1281 (the closing-quote test); a Kotlin
            // break@outer would LEAVE the whole loop, not jump to
            // the a1281 tail - a carrier flag keeps the vendor fall
            var toA1281 = false
            while (!toA1281) { // a1283:
                jpx = jpx + 1
                if (C.xd[jpx] == NUL_D) { // if (xd[*jpx] == 0)
                    writeBackPatrn1(bp, bd)
                    return 2 // *ernbr = 2
                }
                if (C.xd[jpx] == '"') {
                    if (C.xd[jpx + 1] != '"') toA1281 = true // goto a1281
                    else {
                        // strcat(&bd[1], cod210); *jpx = (*jpx + 1);
                        bd.append(PATRN1_COD210[0])
                        jpx = jpx + 1
                    }
                } else {
                    tmp1[0] = C.xd[jpx]
                    bd.append(tmp1[0]) // strcat(&bd[1], tmp1)
                }
            }
            // fall through to a1281 (jpx ON the closing quote)
        }

        // a1281:
        if (C.xd[jpx] != NUL_D) {
            tmp1[0] = C.xd[jpx]
            if (m != 0 || xindexDaf(
                    PATRN1_TERM_C, tmp1, 1.toShort()) == 0) {
                if (C.xd[jpx] == '(') m++ // if (xd[*jpx]=='(') m++;
                if (C.xd[jpx] == ')') m-- // if (xd[*jpx]==')') m--;
                bd.append(tmp1[0]) // strcat(&bd[1], tmp1)
                jpx = jpx + 1
                continue@outer // goto a1285
            }
        }

        break
    }

    C.xpx = jpx - 1 // xpx = (*jpx - 1);
    writeBackPatrn1(bp, bd)
    return 0 // *ernbr = 0
}

// void patrn1(short *, short *, struct MSV *) - the
// interp-parse.cpp form (roster row 3). No call site yet; the
// svPtr domain twin.
fun patrn1SvDaf(jpxIn: Int, ernbr: Int, svPtr: DafMsv): Int {

    var jpx = jpxIn

    val bd = StringBuilder(DafCRuntime.cstring(svPtr.bd)) // &bd[0]

    val tmp1 = CharArray(2)
    tmp1[1] = NUL_D

    outer@ while (true) { // a1285:

        if (svPtr.xd[jpx] == '"') {
            bd.append('"') // strcat(&svPtr->bd[0], "\"")

            var toA1281 = false
            while (!toA1281) { // a1283:
                jpx = jpx + 1
                if (svPtr.xd[jpx] == NUL_D) {
                    writeBackSvPatrn1(svPtr, bd)
                    return 2
                }
                if (svPtr.xd[jpx] == '"') {
                    if (svPtr.xd[jpx + 1] != '"') toA1281 = true // a1281
                    else {
                        // strcat(&svPtr->bd[0], cod210)
                        bd.append(PATRN1_COD210[0])
                        jpx = jpx + 1
                    }
                } else {
                    tmp1[0] = svPtr.xd[jpx]
                    bd.append(tmp1[0])
                }
            }
        }

        // a1281:
        tmp1[0] = svPtr.xd[jpx]

        if (svPtr.xd[jpx] != NUL_D) {
            if (xindexDaf(
                    PATRN1_TERM_C, 0, tmp1, 0, 1) == 0) {
                bd.append(tmp1[0]) // strcat(&svPtr->bd[0], tmp1)
                jpx = jpx + 1
                continue@outer // goto a1285
            }
        }

        break
    }

    svPtr.xpx = jpx - 1 // svPtr->xpx = (*jpx - 1);
    writeBackSvPatrn1(svPtr, bd)
    return 0
}

// the strcat-into-carrier write-back (the bd StringBuilder folds
// back into the bp CharArray at the return points)
private fun writeBackPatrn1(bp: CharArray, bd: StringBuilder) {
    var w = 0
    while (w < bd.length) {
        bp[w] = bd[w]
        w++
    }
    bp[w] = NUL_D
}

private fun writeBackSvPatrn1(svPtr: DafMsv, bd: StringBuilder) {
    var w = 0
    while (w < bd.length) {
        svPtr.bd[w] = bd[w]
        w++
    }
    svPtr.bd[w] = NUL_D
}
