package mumps.compiler

import java.io.IOException
import mumps.interp.NUL

/**
 * PRO-arm translation of compiler-set.c::set_code (B4 unit 96,
 * rank 288). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 40-394.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-set.c::set_code, c, loc 256,
 * comment_ratio 0.052, pointer_marks 15, control_marks 76, score
 * 173.4, batch 4 - exact match to wp1/study-subset/units.csv row
 * 288. The B3-era Stubs.kt set_code stub (raised by unit 65,
 * new_code) is removed with this landing; the NewCode call sites
 * now call setCode (cannon precedent, unit 72).
 *
 * set_code(entry_pt): the SET command compiler - the at-test
 * indirect arms, the nxt_lhs loop collecting left-hand sides into
 * the lhs list, the optional MergeFlg RHS dance, and the
 * set_store emission ladder (global Mglobal STORE scaffold,
 * dollar-piece/dollar-extract LHS reinterpretation via emitted
 * Interpret source, plain cvar/sym_ stores). entry_pt == SET_STORE
 * (compiler-read.c:138) enters directly at set_store.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param : Out is the java.io.Writer on
 *    CompilerState (fprintf(Out,...) -> st.out.write).
 *  - h003 state_macro     : the INTERPRET macro -> the
 *    CompilerState.interpret member fun (unit 65 precedent).
 *  - h004 globals_ext     : new CompilerState owners added with
 *    this unit: setpiece (compiler-globals-ext.h:31), setname
 *    (:32), GBL_flg (:125), lhs (:154), lhsGF (:155), lhsx/lhsf
 *    (:156), MergeFlg (:143), MergeTmp (:144). The lhs[i]=malloc/
 *    free pairs are GC-managed String slots (vendor lines 40,
 *    115-117); the malloc-NULL MEM_ERROR check is dropped with
 *    the allocation, not translated.
 *  - h005 out_param_bundle: atTest's short out-parameter pd1Last
 *    rides the existing AtTestResult bundle (Stubs.kt).
 *  - h006 out_param_holder: Set_flg/GBL_flg ride ShortArray
 *    holders (LocalArray/GlobalArray signatures, unit 21/14).
 *  - h009 format_string   : every fprintf emission keeps its
 *    conversion shape via Kotlin concatenation; the emitted
 *    sprintf(...) C source keeps its %%s/%%d literals verbatim.
 *  - h011 goto            : set_store (forward from entry_pt) ->
 *    first-iteration skip flag; nxt_lhs (backward) -> inner
 *    while(true)+continue; set (backward from rhs1) -> outer
 *    while(true)+continue; rhs1 (forward from the MergeFlg arm)
 *    -> break out of the lhs collection.
 *  - h012 unsigned_byte_domain: xd/vd/v1d/pd1/setname byte
 *    domains (Char 0..255).
 *  - h013 dead_store      : vendor 'int i, j;' in the two at_flg
 *    tmp arms (lines 59/74) are unused declarations - dropped,
 *    never fixed by initializing; vendor strcpy(rstr, Extract2(...))
 *    (line 247) stores into rstr which is never read - the
 *    Extract2 call is kept for its emission side effect, the
 *    rstr buffer is dropped.
 *  - h014 out_of_scope_call: parse_ (compiler Stubs stub, owner
 *    compiler-parse.c); atTest/atCopy (Stubs stubs, owner
 *    compiler-at-test.c, not in corpus); cvar (Stubs stub raised
 *    by this unit, owner compiler-parse.c:2074 cvar - not a
 *    corpus unit); LocalArray/GlobalArray (DONE, units 21/14);
 *    Extract2 (DONE, unit 15); Extract1 (Stubs stub); memcheck
 *    (Stubs stub).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level setCode - no collisions; the
 *    emitted C text contains dollar bytes (s $p/s $e) - each
 *    rides the file-private DOLLARC constant; the emitted text
 *    contains mid-line block-comment closers (post conditional
 *    2116/2313) - accepted vendor text per the NewCode precedent.
 *  - h017 naming          : set_code -> setCode (if_code ->
 *    ifCode precedent); entry_pt keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The LOCK arm emission is a no-argument fprintf: the "%s" and
 *    "lhs[i]" inside it are LITERAL text (vendor line 233) - kept
 *    verbatim, no conversion applied.
 *  - In the dollar-piece 4-arg forms the vendor passes the raw
 *    end buffer (already Extract2'd at line 246), while strt is
 *    Extract2'd a SECOND time at the emission site (lines 279/
 *    292) - both kept exactly.
 *  - The eval-result arms pass tmp UNstripped (the vendor comment
 *    block notes the quoted-first/last-character convention;
 *    lines 296/351) - kept; only the quoted-name arms strip the
 *    surrounding quotes.
 *  - The dollar-piece/dollar-extract section REUSES the outer
 *    lhs loop counter i (vendor lines 209-240) - the scans
 *    clobber i, so the vendor for-loop then increments the
 *    clobbered value (an apparent bug; the loop body typically
 *    exits via the last lhs) - kept by reusing the same counter.
 *  - The scans 'for (i = 0; vd[i] < 128; i++)' have no NUL guard
 *    (NUL < 128 is true); the Kotlin arrays are large enough that
 *    the scan stays in bounds - kept as written.
 *  - "if (xd[xpx+1]==',' || xd[xpx+1]==')')" probes one PAST the
 *    current byte (vendor line 126) - kept.
 *  - The lhs collection frees the previous list at set: (vendor
 *    lines 44-47) - GC drops the frees.
 */

private const val PARSE_ERROR = 1 // compiler-macros.h:69
private const val NO_GLOBAL_PERMITTED = 3 // :71
private const val TOO_MANY_LHS = 5 // :73
private const val ABRTX = 6 // :74
private const val SET_STORE = 1 // :84

/** compiler-macros.h:91-112 effective defines (char command codes). */
private const val READ_C = 'r'
private const val SET_C = 's'
private const val NEW_C = 'n'
private const val NEW1_C = 'N'
private const val KILL_C = 'k'
private const val LOCK_C = 'l'

/** vendor emitted-C dollar byte (h015). */
private val DOLLARC = 36.toChar()

/** strcpy(dst, s) over the byte domain (dst NUL-terminated). */
private fun strcpyC(dst: CharArray, s: String) {
    for (k in s.indices) dst[k] = s[k]
    dst[s.length] = NUL
}

/** C char* -> CharArray view for the Extract2 CharArray overload. */
private fun cbuf(s: String): CharArray {
    val b = CharArray(s.length + 1)
    for (k in s.indices) b[k] = s[k]
    b[s.length] = NUL
    return b
}

@Throws(IOException::class)
fun setCode(entry_pt: Int): Int {
    val st = CompilerState
    val setFlgHolder = ShortArray(1)
    val gblFlgHolder = ShortArray(1)
    var i: Int
    var j: Int

    // h011: goto set_store (forward) -> skip flag
    var atStore = entry_pt == SET_STORE

    // h011: goto set (backward from rhs1) -> outer while(true)
    while (true) {

        if (!atStore) {

            // set:
            // vendor lines 44-47: free(lhs[i]) loop - GC-dropped (h004)

            st.lhsf = 0
            st.lhsx = 0

            if (st.xd[st.xpx.toInt()] == '(') {
                st.lhsf = 1
                st.xpx++
            }

            st.Set_flg = 0

            val r = atTest(st.xd, st.pd1, st.lines, st.xpx) // h005 bundle
            st.at_flg = r.rc.toShort() // h010: int result into the short domain
            st.pd1Last = r.pd1Last

            if (st.at_flg == 99.toShort()) return ABRTX // goto abrtx;

            if (st.at_flg != 0.toShort() && st.retz == READ_C.code.toShort()) {

                // h013: vendor 'int i, j;' - unused declarations, dropped
                val tmp = StringBuilder("r ")
                atCopy(st.at_flg.toInt(), tmp) // h014 stub

                CompilerState.interpret(tmp.toString()) // h003: INTERPRET

                st.xpx = st.at_flg.toLong()

                if (st.POST_flg != 0.toShort()) {
                    st.out.write("\t} // post conditional 2097\n")
                    st.POST_flg = 0
                }

                return 0 // goto next_cmnd;
            }

            if (st.at_flg != 0.toShort()) {

                // h013: vendor 'int i, j;' - unused declarations, dropped
                val tmp = StringBuilder("s ")
                atCopy(st.at_flg.toInt(), tmp) // h014 stub

                CompilerState.interpret(tmp.toString()) // h003: INTERPRET

                st.xpx = st.at_flg.toLong()
                if (st.POST_flg != 0.toShort()) {
                    st.out.write("\t} /* post conditional 2116*/\n")
                    st.POST_flg = 0
                }
                return 0 // goto next_cmnd;
            }

            // h011: nxt_lhs (backward) -> inner while(true)+continue
            while (true) {
                st.t0px = 1
                st.t2 = 1
                st.setname[0] = NUL

                st.ierr = parse_().toShort() // h014 stub; h010 widening
                if (st.ierr != 0.toShort()) return PARSE_ERROR

                // strcpy (vd, &v1d[1])
                var sOff = 1
                var dOff = 0
                while (st.v1d[sOff] != NUL) {
                    st.vd[dOff] = st.v1d[sOff]
                    dOff++; sOff++
                }
                st.vd[dOff] = NUL

                setFlgHolder[0] = st.Set_flg
                LocalArray(st.vd, setFlgHolder) // check and process local array ref
                st.Set_flg = setFlgHolder[0]

                if (st.v1d[1] == '^')
                    if (st.retz == NEW1_C.code.toShort() || st.retz == NEW_C.code.toShort())
                        return NO_GLOBAL_PERMITTED // goto nogblpermitted;

                gblFlgHolder[0] = st.GBL_flg
                GlobalArray(st.v1d, gblFlgHolder, st.vd) // check and process global array
                st.GBL_flg = gblFlgHolder[0]

                if (st.retz == KILL_C.code.toShort()) return 0 // goto kill_return;
                if (st.retz == NEW_C.code.toShort()) return 0 // goto new_return;
                if (st.retz == NEW1_C.code.toShort()) return 0 // goto new1_return;
                if (st.retz == LOCK_C.code.toShort()) return 0 // goto lock_return;

                if (st.lhsx > 24) return TOO_MANY_LHS // goto too_lhs;
                // vendor malloc+NULL check (lines 115-117) - GC-dropped (h004)
                st.lhs[st.lhsx] = cstringAt(st.vd)
                st.lhsGF[st.lhsx] = st.GBL_flg.toInt()
                st.lhsx++

                if (st.xd[st.xpx.toInt() + 1] == ',' || st.xd[st.xpx.toInt() + 1] == ')') {
                    if (st.xd[st.xpx.toInt() + 1] != ')') {
                        st.xpx += 2
                        continue // goto nxt_lhs
                    }
                }

                if (st.lhsf != 0) st.xpx++

                if (st.retz == READ_C.code.toShort()) return 0 // goto set_return;

                st.xpx += 2
                st.t0px = 0
                st.t2 = 1

                if (st.MergeFlg != 0) {
                    st.t0px = 1
                    st.setname[0] = NUL
                    st.out.write("\tstrcpy((char *) tmp" + st.iiitmp + ", (const char *) atmp); /* hold result */\n")
                    st.MergeTmp = st.iiitmp
                    st.iiitmp++
                    memcheck(st.iiitmp) // h014 stub
                }

                st.ierr = parse_().toShort() /* process RHS */
                if (st.ierr != 0.toShort()) return PARSE_ERROR

                if (st.MergeFlg != 0) {

                    // strcpy (vd, &v1d[1])
                    var s2 = 1
                    var d2 = 0
                    while (st.v1d[s2] != NUL) {
                        st.vd[d2] = st.v1d[s2]
                        d2++; s2++
                    }
                    st.vd[d2] = NUL

                    setFlgHolder[0] = st.Set_flg
                    LocalArray(st.vd, setFlgHolder) // check and process local array ref
                    st.Set_flg = setFlgHolder[0]

                    gblFlgHolder[0] = st.GBL_flg
                    GlobalArray(st.v1d, gblFlgHolder, st.vd) // check and process global array
                    st.GBL_flg = gblFlgHolder[0]

                    if (cstringAt(st.vd) == "atmp") st.lhs[0] = "tmp" + st.MergeTmp

                    st.out.write("\tf=Merge(" + st.lhs[0] + ", " + cstringAt(st.vd) + ", svPtr); \n")

                    st.out.write("\tif (f != 0) ErrorMessage(\"Merge failed \", svPtr->LineNumber); \n")

                    st.xpx++
                    break // goto rhs1 (forward; exits lhs collection)
                }

                break // fall through to set_store
            }
        }

        // set_store:
        i = 0
        while (i < st.lhsx) {

            st.GBL_flg = st.lhsGF[i].toShort() // h010: int domain into short

            if (st.GBL_flg != 0.toShort()) {

                if (st.retz == READ_C.code.toShort()) {

                    st.out.write("\n")
                    st.out.write("\t{ \n")

                    st.out.write("\tunsigned char seg[STR_MAX]; \n")

                    st.out.write("\tstrcpy(seg, " + Extract2(st.pd1, st.sdlim.toInt()) + "); \n")

                    st.out.write("\tf = Mglobal(STORE, " + Extract2(cbuf(st.lhs[i] ?: "")) + ", seg, svPtr); \n")

                    st.out.write("\t} \n")
                    st.out.write("\n")
                }

                if (st.retz == SET_C.code.toShort()) {

                    st.out.write("\n")
                    st.out.write("\t{ \n")

                    st.out.write(
                        "\tf = Mglobal(STORE, " + Extract2(cbuf(st.lhs[i] ?: "")) + ", " +
                            Extract2(st.pd1, st.sdlim.toInt()) + ", svPtr); \n"
                    )

                    st.out.write("\t} \n")
                    st.out.write("\n")
                }

                if (st.retz == LOCK_C.code.toShort())
                    st.out.write("\tf=Mglobal(LOCK, %s, \"\", lhs[i], svPtr); \n") // literal vendor text (see header)
            } else if (st.vd[0] == DOLLARC &&
                (Character.toLowerCase(st.vd[1]) == 'p' ||
                    Character.toLowerCase(st.vd[1]) == 'e')) { // Process LHS $PIECE

                val varB = CharArray(256)
                val pat = CharArray(256)
                val strt = CharArray(256)
                val end = CharArray(256)

                i = 0
                while (st.vd[i] < Char(128)) i++
                var sOff = i + 1
                var dOff = 0
                while (st.vd[sOff] != NUL) {
                    varB[dOff] = st.vd[sOff]; dOff++; sOff++
                }
                varB[dOff] = NUL
                j = 0
                while (varB[j] < Char(128)) j++
                varB[j] = NUL

                i++ // vendor: for (i++; vd[i] < 128; i++)
                while (st.vd[i] < Char(128)) i++
                sOff = i + 1
                dOff = 0
                while (st.vd[sOff] != NUL) {
                    pat[dOff] = st.vd[sOff]; dOff++; sOff++
                }
                pat[dOff] = NUL
                j = 0
                while (pat[j] < Char(128)) j++
                pat[j] = NUL

                i++ // vendor: for (i++; vd[i] < 128 && vd[i] != 0; i++)
                while (st.vd[i] < Char(128) && st.vd[i] != NUL) i++
                sOff = i + 1
                dOff = 0
                while (st.vd[sOff] != NUL) {
                    strt[dOff] = st.vd[sOff]; dOff++; sOff++
                }
                strt[dOff] = NUL
                j = 0
                while (strt[j] < Char(128)) j++
                strt[j] = NUL

                i++ // vendor: for (i++; vd[i] < 128 && vd[i] != 0; i++)
                while (st.vd[i] < Char(128) && st.vd[i] != NUL) i++

                i++ // vendor: if (vd[++i] == 0)
                if (st.vd[i] == NUL) strcpyC(end, "")
                else {
                    sOff = i
                    dOff = 0
                    while (st.vd[sOff] != NUL) {
                        end[dOff] = st.vd[sOff]; dOff++; sOff++
                    }
                    end[dOff] = NUL
                    j = 0
                    while (end[j] < Char(128)) j++
                    end[j] = NUL
                }

                strcpyC(pat, Extract2(pat))
                strcpyC(strt, Extract2(strt))
                strcpyC(end, Extract2(end))
                // h013: vendor strcpy(rstr, Extract2(&pd1[sdlim])) (line 247)
                // stores into rstr which is never read - the Extract2 call
                // is kept for its emission side effect, rstr is dropped.
                Extract2(st.pd1, st.sdlim.toInt())

                //--------------------
                //  PIECE LHS
                //--------------------
                if (Character.toLowerCase(st.vd[1]) == 'p') {
                    if (cstringAt(strt).isEmpty()) strcpyC(strt, "\"1\"")

                    // first character is a " character
                    // last character is a " character
                    var tmp = Extract1(varB) // h014 stub

                    if (tmp.isNotEmpty() && tmp[0] == '"') { // variable name not rslt of eval
                        tmp = tmp.substring(1) // strcpy(tmp, &tmp[1])
                        tmp = tmp.substring(0, tmp.length - 1) // remove final quote; start with index 1

                        if (cstringAt(end).isEmpty()) // 3 arg form
                            st.out.write(
                                "\n" +
                                    "{ char x[STR_MAX]; " +
                                    "sprintf(x, \"s " + DOLLARC + "p(" + tmp + ",\\\"%%s\\\",\\\"%%s\\\")=\\\"%%s\\\" \", " +
                                    cstringAt(pat) + ", " + Extract2(strt) + ", " +
                                    cstringAt(st.pd1, st.sdlim.toInt()) + ");\n"
                            )
                        else // 4 arg form
                            st.out.write(
                                "\n" +
                                    "{ char x[STR_MAX]; " +
                                    "sprintf(x, \"s " + DOLLARC + "p(" + tmp + ",\\\"%%s\\\",\\\"%%s\\\",\\\"%%s\\\")=\\\"%%s\\\" \", " +
                                    cstringAt(pat) + ", " + Extract2(strt) + ", " + cstringAt(end) + ", " +
                                    cstringAt(st.pd1, st.sdlim.toInt()) + "); \n"
                            )

                        interpretTail()
                    } else { // eval result

                        if (cstringAt(end).isEmpty()) // 3 arg form
                            st.out.write(
                                "\n" +
                                    "{ char x[STR_MAX]; " +
                                    "sprintf(x, \"s " + DOLLARC + "p(" + tmp + ",\\\"%%s\\\",\\\"%%s\\\")=\\\"%%s\\\" \", " +
                                    cstringAt(pat) + ", " + Extract2(strt) + ", " +
                                    cstringAt(st.pd1, st.sdlim.toInt()) + ");\n"
                            )
                        else // 4 arg form
                            st.out.write(
                                "\n" +
                                    "{ char x[STR_MAX]; " +
                                    "sprintf(x, \"s " + DOLLARC + "p(" + tmp + ",\\\"%%s\\\",\\\"%%s\\\",\\\"%%s\\\")=\\\"%%s\\\" \", " +
                                    cstringAt(pat) + ", " + Extract2(strt) + ", " + cstringAt(end) + ", " +
                                    cstringAt(st.pd1, st.sdlim.toInt()) + "); \n"
                            )

                        interpretTail()
                    }
                }

                //-------------------
                //  EXTRACT LHS
                //-------------------
                else if (Character.toLowerCase(st.vd[1]) == 'e') { // $extract

                    // first character is a " character
                    // last character is a " character
                    val tmp = Extract1(varB) // h014 stub

                    if (tmp.isNotEmpty() && tmp[0] == '"') { // variable name not rslt of eval
                        var t2 = tmp.substring(1) // strcpy(tmp, &tmp[1])
                        t2 = t2.substring(0, t2.length - 1) // remove final quote; start with index 1

                        if (cstringAt(strt).isNotEmpty()) // 3 arg form
                            st.out.write(
                                "\n" +
                                    "{ char x[STR_MAX]; " +
                                    "sprintf(x, \"s " + DOLLARC + "e(" + t2 + ",\\\"%%s\\\",\\\"%%s\\\")=\\\"%%s\\\" \", " +
                                    cstringAt(pat) + ", " + Extract2(strt) + ", " +
                                    cstringAt(st.pd1, st.sdlim.toInt()) + "); \n"
                            )
                        else // 2 arg form
                            st.out.write(
                                "\n" +
                                    "{ char x[STR_MAX]; sprintf(x, \"s " + DOLLARC + "e(" + t2 + ",\\\"%%s\\\")=\\\"%%s\\\" \", " +
                                    cstringAt(pat) + ", " + cstringAt(st.pd1, st.sdlim.toInt()) + "); \n"
                            )

                        interpretTail()
                    } else { // rslt of exp eval

                        if (cstringAt(strt).isNotEmpty()) // 3 arg form
                            st.out.write(
                                "\n" +
                                    "{ char x[STR_MAX]; " +
                                    "sprintf(x, \"s " + DOLLARC + "e(%%s,\\\"%%s\\\",\\\"%%s\\\")=\\\"%%s\\\" \", " +
                                    tmp + ", " + cstringAt(pat) + ", " + Extract2(strt) + ", " +
                                    cstringAt(st.pd1, st.sdlim.toInt()) + "); \n"
                            )
                        else // 2 arg form
                            st.out.write(
                                "\n" +
                                    "{ char x[STR_MAX]; sprintf(x, \"s " + DOLLARC + "e(%%s,\\\"%%s\\\")=\\\"%%s\\\" \", " +
                                    tmp + ", " + cstringAt(pat) + ", " + cstringAt(st.pd1, st.sdlim.toInt()) + "); \n"
                            )

                        interpretTail()
                    }
                }
            } else {

                if (cvar(st.lhs[i] ?: "")) { // h014 stub
                    st.out.write(
                        "\tstrcpy( (char *) " + st.lhs[i] + ", (const char *) " +
                            Extract2(st.pd1, st.sdlim.toInt()) + "); // Store result s264\n"
                    )
                } else {

                    if (st.Set_flg != 0.toShort())
                        st.out.write(
                            "\tsym_(SYMSTORE, (UNS) " + st.lhs[i] + ", " +
                                "(UNS) " + Extract2(st.pd1, st.sdlim.toInt()) + ", svPtr); // Store result s270 \n"
                        )
                    else
                        st.out.write(
                            "\tsym_(SYMSTORE, (UNS) \"" + st.lhs[i] + "\", " +
                                "(UNS) " + Extract2(st.pd1, st.sdlim.toInt()) + ", svPtr); // Store result s274 \n"
                        )
                }
            }

            i++
        }

        // rhs1:
        if (st.retz == READ_C.code.toShort()) return 0 // goto cont_read;

        if (st.xd[st.xpx.toInt()] == ',') {
            st.xpx++
            atStore = false
            continue // goto set
        }

        if (st.POST_flg != 0.toShort()) {
            st.out.write("\t} /* post conditional 2313*/\n")
            st.POST_flg = 0
        }

        return 0 // goto next_cmnd;
    }
}

/**
 * The emitted _jj = Interpret(...) tail shared by the four
 * dollar-piece/dollar-extract LHS arms (vendor lines 284-288 and
 * repeats; kept as one helper over four verbatim copies).
 */
@Throws(IOException::class)
private fun interpretTail() {
    val st = CompilerState
    st.out.write("       _jj = Interpret( (CCHR) x, svPtr); \n")
    st.out.write("       svPtr->ERROR = _jj; \n")
    st.out.write("       if (!svPtr->NOERR && _jj < 0) \n")
    st.out.write("               ErrorMessage(\"Indirection error\", svPtr->LineNumber); \n")
    st.out.write("};\n")
}
