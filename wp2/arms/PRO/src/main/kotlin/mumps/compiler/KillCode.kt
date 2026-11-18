package mumps.compiler

import java.io.IOException
import mumps.interp.NUL
import mumps.interp.cstringAt

private const val SOH = 1.toChar() // vendor '\x01' global-name separator (Merge.kt precedent)

/**
 * PRO-arm translation of compiler-kill.c::kill_code (batch 9 unit,
 * roster row 251, file rank 20). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 39-227.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-kill.c::kill_code, c, loc 132,
 * comment_ratio 0.061 exact to units.csv row 251 (pointer_marks /
 * control_marks per the roster row). The banner-residue chunk of
 * this file landed earlier this tranche as CompilerKillMisc.kt
 * (roster row 243); kill_code is the one vendor declaration the
 * file carries.
 *
 * kill_code(): the Mumps KILL command compiler - the argumentless
 * form emits the SymFree(0) scaffold, the kill-specific arm emits
 * the sym_(2,...) store of the parsed name, the kill-all-except
 * arm loops set_code(SET) over the parenthesised exclusion list
 * building the emitted nmes linked-list scaffold, and the kill-
 * globals arm emits the strmove/gtmp build plus the Mglobal(GKILL)
 * first-kill and XNEXT walk that kills every longer sibling.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param : Out is the java.io.Writer on
 *    CompilerState (fprintf(Out,...) -> st.out.write).
 *  - h005 out_param_bundle : atTest's short out-parameter pd1Last
 *    rides the existing AtTestResult bundle (Stubs.kt, unit 108).
 *  - h007 cstring_local   : the vendor char tmp[1024] indirect
 *    buffer is a StringBuilder (WriteCode precedent); the char
 *    t[1024] global-name buffer in the kill-globals arm is a
 *    CharArray slab kept at vendor size.
 *  - h009 format_string   : every fprintf emission keeps its
 *    conversion shape via cstring views and concatenation; the
 *    emitted C literals keep their %%s/%%d text verbatim.
 *  - h011 goto            : k003 (backward from the comma tails)
 *    -> the outer while(true)+continue; kill2 (backward from the
 *    exclusion-list comma) -> the inner while(true)+continue of
 *    the kill-except arm; k004 (forward) -> the globals body in
 *    the same iteration; k002/k001 are the common tail. The
 *    argumentless form's goto k001 and the kill-except/k004 falls
 *    into k002 ride the goK002/goK001 tail flags (LockCode
 *    skipToLck1 precedent).
 *  - h012 unsigned_byte_domain: xd/vd/v1d/pd1 byte domains
 *    (Char 0..255); xpx is the Long carrier indexed via toInt()
 *    (h010 Long<->Int narrowing at every index site).
 *  - h013 dead_store      : vendor 'int i, j;' in the at_flg arm
 *    (line 54) are unused declarations - dropped, never fixed by
 *    initializing; the commented-out strcatx fossil block inside
 *    the kill-globals subscript walk (lines 183-187) is kept as
 *    a comment.
 *  - h014 out_of_scope_call: parse_ (compiler-side Stubs stub,
 *    owner compiler-parse.c, batch-9 misc owner); cvar and
 *    Extract1 (compiler-parse.c:2074/:2119 Stubs stubs, not
 *    corpus units); set_code (owner landed, SetCode.kt unit 96);
 *    Extract2 (Extract2.kt, unit 122); keyfix stays inside the
 *    emitted C text (interp helper of the generated code).
 *  - h015 grammar_collision: new top-level killCode - no
 *    cross-file collision (the interp-side Kill family on
 *    GlobalCarrier/Misc7 receivers is method-scoped).
 *  - h017 naming          : kill_code -> killCode (if_code ->
 *    ifCode precedent).
 *  - h018 preprocessor_conditional: none in the body (the file's
 *    #include arms landed with the row-243 banner residue).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The at_flg arm emits vendor text "...k " via atCopy into the
 *    "k "(tmp, (k ") seeded StringBuilder - the strcpy(tmp,"k ")
 *    prefix is kept verbatim.
 *  - Vendor 'retz = KILL' stores the char code 'k' into the short
 *    domain (h010), LockCode LOCK_C precedent.
 *  - The exclusion-list error ladder mirrors lock_code's exactly,
 *    including the fall-through to kill_return for return_code
 *    == 0 only.
 *  - The vd quote-wrap (atmp stays bare, any other target gets
 *    wrapped in double quotes in place) mutates vd in place -
 *    kept.
 *  - The globals-arm subscript walk appends SOH ('\\x01') after
 *    the base name and walks 'while (v1d[i] <= 127 && v1d[i] >=
 *    32)' - bytes, not NUL-terminated reads; the ++i comma skip in
 *    'if (v1d[++i] != 0) continue;' is kept verbatim (i advances
 *    PAST the sentinel byte as in C).
 *  - The unused j = 0 reset at the top of the inner while(1) is
 *    live (t is reused for the next subscript).
 */

private const val PARSE_ERROR = 1 // compiler-macros.h:69
private const val KILL_DCL_ERROR = 7 // :75 (compiler-macros.h error ladder)
private const val ARG_LIST_ERROR = 2 // :70
private const val NO_GLOBAL_PERMITTED = 3 // :71
private const val MEM_ERROR = 4 // :72
private const val TOO_MANY_LHS = 5 // :73
private const val ABRTX = 6 // :74

/** compiler-macros.h:91-112 effective defines (char command codes). */
private const val SET_C = 's'
private const val KILL_C = 'k'

@Throws(IOException::class)
fun killCode(): Int {
    val st = CompilerState

    var goK001 = false // goto k001 from the argumentless form
    if (st.xd[st.xpx.toInt()] == ' ' || st.xd[st.xpx.toInt()] == NUL ||
        st.xd[st.xpx.toInt()] == '\t') { // Kill all locals
        st.out.write("\t_SymFree(0,svPtr);\n")
        goK001 = true // goto k001
    }

    if (!goK001) {

        // Check for indirection in statement
        val r = atTest(st.xd, st.pd1, st.pd1Last, st.lines, st.xpx)
        st.at_flg = r.rc.toShort()
        st.pd1Last = r.pd1Last

        if (st.at_flg == (-1).toShort()) return ABRTX // goto abrtx;

        if (st.at_flg.toInt() != 0) {

            val tmp = StringBuilder(1024) // h007: vendor char tmp[1024]
            tmp.append("k ") // strcpy(tmp,"k ")
            atCopy(st.at_flg.toInt(), tmp)

            st.interpret(tmp.toString()) // h003: INTERPRET macro

            st.xpx = st.at_flg.toLong() // xpx = at_flg
            if (st.POST_flg.toInt() != 0) {
                st.out.write("\t} // post conditional 3053\n")
                st.POST_flg = 0
            }
            return 0 // goto next_cmnd;
        }

        // k003:
        while (true) {

            var goK002 = false

            if (st.xd[st.xpx.toInt()] == '^') { // Kill globals - k004

                while (true) {

                    st.t0px = 1
                    st.ierr = parse_().toShort() // h010: int into the short domain
                    if (st.ierr != 0.toShort()) return PARSE_ERROR // goto parse_err;

                    st.out.write("\tstrmove( (UNS) gtmp, (UNS) \"\");\n")

                    val t = CharArray(1024) // h007: vendor char t[1024]
                    var i = 1
                    var j = 0
                    while (st.v1d[i] != NUL) {
                        if (st.v1d[i] >= ' ' && st.v1d[i] <= Char(127)) {
                            t[j] = st.v1d[i]
                            j++
                            i++
                        } else {
                            break
                        }
                    }

                    t[j] = SOH
                    j++
                    t[j] = NUL

                    st.out.write(
                        "\tstrcat((char *) gtmp,(char *) \"" + cstringAt(t) + "\"); \n"
                    )

                    if (st.v1d[i] != NUL) {
                        i++
                        var walk = true
                        while (walk) {
                            j = 0
                            while (st.v1d[i] <= Char(127) && st.v1d[i] >= ' ') {
                                t[j] = st.v1d[i]
                                j++; i++
                            }
                            t[j] = NUL
                            st.out.write(
                                "\tstrcatx((char *) gtmp,(char *) " + Extract2(t) + ");\n"
                            )
                            // vendor fossil (lines 183-187): the older
                            // SOH+strcat emission, commented out in C.
                            i++
                            if (st.v1d[i] != NUL) continue // walk on
                            walk = false // else break;
                        }
                    }

                    st.out.write("\t{ \n")
                    st.out.write("\tunsigned char tmp14[STR_MAX], tmp15[STR_MAX]; \n")
                    st.out.write("\tkeyfix(gtmp);\n")
                    st.out.write("\t_k = strlen((const char *) gtmp); \n") // original length

                    st.out.write("\tstrmove((UNS) tmp14, (UNS) gtmp); \n") // vendor line 205 tail (space before newline kept)
                    st.out.write("\tMglobal(GKILL, gtmp, tmp15, svPtr); \n") // kill first

                    st.out.write("\twhile(1) { \n")
                    st.out.write("\t\t_j = Mglobal(XNEXT, gtmp, tmp15, svPtr); \n") // kill others like it
                    st.out.write("\t\tif (_j && strncmp( (CCHR) tmp14, (CCHR) gtmp,_k) == 0) { \n")
                    st.out.write("\t\t\tMglobal(GKILL, gtmp, tmp15, svPtr); \n")
                    st.out.write("\t\t\tcontinue; \n")
                    st.out.write("\t\t\t} \n")

                    st.out.write("\t\tbreak; \n")
                    st.out.write("\t\t} \n")
                    st.out.write("\t}\n")

                    goK002 = true
                    break // fall out of the k004 while(1) into k002
                }
            } else if (st.xd[st.xpx.toInt()] != '(') { // Kill specific

                st.t2 = 1
                st.ierr = parse_().toShort() // h010
                if (st.ierr != 0.toShort()) return PARSE_ERROR // goto parse_err;
                if (cvar(cstringAt(st.v1d, 1))) return KILL_DCL_ERROR // goto kill_dcl_err;

                st.out.write(
                    "\tsym_(2, (UNS) " + Extract1(st.pd1.copyOfRange(st.sdlim.toInt(), st.pd1.size)) +
                        ", (UNS) \"\",svPtr); //...* kill specific\n"
                )

                if (st.xd[st.xpx.toInt()] == ',') {
                    st.xpx++
                    continue // goto k003
                }

                goK001 = true // goto k001
            } else { // Kill all locals except...

                st.out.write("\t{ \n\tstruct nmes *p1, *p2; \n")
                st.out.write("\tsvPtr->nstart = NULL;\n")
                st.xpx++

                // kill2:
                var collecting = true
                while (collecting) {

                    st.t0px = 1
                    st.t2 = 1
                    st.retz = KILL_C.code.toShort() // h010: char code into the short domain

                    val return_code = setCode(SET_C.code) // goto set; process item to read

                    if (return_code == PARSE_ERROR) return PARSE_ERROR // goto parse_err;
                    if (return_code == ARG_LIST_ERROR) return ARG_LIST_ERROR // goto arg_list_error;
                    if (return_code == NO_GLOBAL_PERMITTED) return NO_GLOBAL_PERMITTED
                    if (return_code == MEM_ERROR) return MEM_ERROR // goto mem_err;
                    if (return_code == TOO_MANY_LHS) return TOO_MANY_LHS // goto too_lhs;
                    if (return_code == ABRTX) return ABRTX // goto abrtx;

                    // kill_return: (return_code == 0)
                    if (cstringAt(st.vd) == "atmp") {
                        st.out.write("\tkeyfix(atmp); \n")
                    } else {

                        val tmp = StringBuilder(256) // h007: vendor char tmp[256]
                        tmp.append('"')
                        tmp.append(cstringAt(st.vd))
                        tmp.append('"')
                        var dOff = 0
                        while (dOff < tmp.length) { // strcpy(vd,tmp) in place
                            st.vd[dOff] = tmp[dOff]
                            dOff++
                        }
                        st.vd[dOff] = NUL
                    }

                    st.out.write("\tp1 = (struct nmes *) malloc(sizeof (struct nmes));\n")

                    st.out.write(
                        "\tif (p1 == NULL) ErrorMessage(\"Out of memory\", " +
                            "svPtr->LineNumber);\n"
                    )

                    st.out.write(
                        "\tp1->name = (char *) malloc(strlen((const char *) " +
                            cstringAt(st.vd) + ") + 1);\n"
                    )
                    st.out.write(
                        "\tif (p1->name == NULL) ErrorMessage(\"Out of memory\", " +
                            "svPtr->LineNumber);\n"
                    )
                    st.out.write("\tstrmove((UNS) p1->name, (UNS) " + cstringAt(st.vd) + ");\n")
                    st.out.write("\tp1->next = svPtr->nstart;\n")
                    st.out.write("\tsvPtr->nstart = p1;\n")
                    st.xpx++

                    if (st.xd[st.xpx.toInt()] != ')' && st.xd[st.xpx.toInt()] != NUL) {
                        st.xpx++
                        continue // goto kill2
                    }
                    collecting = false
                }

                st.out.write("\tsym_(5, (UNS) \"\",(UNS) \"\", svPtr); // kill all except\n")
                st.out.write("\t}\n")
                goK002 = true // goto k002
            }

            // k002:
            if (goK002) {
                st.xpx++
                if (st.xd[st.xpx.toInt()] == ',') {
                    st.xpx++
                    continue // goto k003
                }
            }

            break // fall to k001
        }
    }

    // k001:
    if (st.POST_flg.toInt() != 0) {
        st.out.write("\t} /* post conditional 3208*/\n")
        st.POST_flg = 0
    }
    return 0 // goto next_cmnd;
}
