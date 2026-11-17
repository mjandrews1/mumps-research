package mumps.compiler

import java.io.IOException
import mumps.interp.NUL

/**
 * PRO-arm translation of compiler-for.c::for_code (B4 unit 95,
 * rank 289). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 40-394.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-for.c::for_code, c, loc 232,
 * comment_ratio 0.059, pointer_marks 9, control_marks 61, score
 * 148.1, batch 4 - exact match to wp1/study-subset/units.csv row
 * 289.
 *
 * for_code(): the Mumps FOR command compiler - argumentless form
 * (bare while(1) scaffold), then the loop-variable parse, the
 * dollar-function ($order/$data) fast paths, the
 * init/increment/limit strmove emissions, the float/integer
 * for%xF direction ternary, the setjmp/For_Group%d scaffold, and
 * the multi-variable comma continuation (for_again2) with the
 * shared singles exit code.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param : Out is the java.io.Writer on
 *    CompilerState (fprintf(Out,...) -> st.out.write).
 *  - h004 globals_ext     : new CompilerState owners added with
 *    this unit: forxmax (compiler-globals-ext.h:110), forxFLG/
 *    forLine/forx (:111), for_fcn_flg/for_fcn_flg2 (:114),
 *    for_mult (:140), SinglesFlg (:142), forv (:150), FOR_limit
 *    (:151). for_mult/SinglesFlg are C shorts carried as Int
 *    (h010: widened; no narrowing use).
 *  - h009 format_string   : every fprintf emission keeps its
 *    conversion shape via Kotlin concatenation; %%s/%%d literals
 *    and the \t runs kept verbatim.
 *  - h011 goto            : the five vendor labels (for_again,
 *    for_again2, while01, one_arg, singles) become a label
 *    dispatch loop: the only REACHABLE backward goto (for_again2
 *    from the singles comma) becomes the loop's continue; the
 *    forward gotos (while01 from the start-$ case, one_arg from
 *    the for_fcn_flg test, singles from the comma-list check)
 *    become label transitions; for_again itself has no reachable
 *    backward goto (see h013) and runs once.
 *  - h012 unsigned_byte_domain: xd/v1d/forv byte domains.
 *  - h013 dead_store      : the vendor tail after 'goto
 *    for_again2;' in the singles comma arm (lines 300-306: 'if
 *    (SinglesFlg) goto for_again2; else goto for_again;') is
 *    unreachable code after an unconditional goto - dropped with
 *    this comment, not translated (GlobalQuery precedent); this
 *    is also why for_again has no reachable backward goto.
 *  - h014 out_of_scope_call: parse_ (compiler Stubs stub, owner
 *    compiler-parse.c); cvar (Stubs stub, owner compiler-parse.c
 *    cvar, not a corpus unit); linepad (compiler Stubs stub,
 *    owner compiler-read.c, not in corpus); Extract2 (DONE, unit
 *    15).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level forCode - no collisions; the
 *    emitted C text contains no dollar bytes; FOR rides the
 *    file-private FOR_C constant.
 *  - h017 naming          : for_code -> forCode (if_code ->
 *    ifCode precedent); label names keep vendor spellings.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - 'if (cvar(forv)) ;' (line 88) is a vendor empty statement
 *    with a stray semicolon - kept as an empty branch with this
 *    comment.
 *  - The first float-direction fprintf (lines 214-216) passes
 *    FIVE forx arguments to THREE conversions - the two extra C
 *    arguments are harmless varargs; the emission uses the three
 *    converted values.
 *  - The same happens in the m2767 exit emission (line 341: four
 *    forx args, three conversions).
 *  - In the for_mult >= 0 singles arm the setjmp line uses
 *    for_mult but the exit line uses forx (line 266: 'goto
 *    For%dExit' with forx while the setjmp/For%d pair used
 *    for_mult) - kept exactly.
 *  - The '$order optimization hack' comment block (lines 348-355)
 *    is kept in place as a comment over the m2771 emission.
 *  - 'if (xd[xpx + 1] != '{') IF_flg++;' probes one PAST the
 *    current byte in both the argumentless and tail arms - kept.
 */

private const val PARSE_ERROR = 1 // compiler-macros.h:69
private const val ARG_LIST_ERROR = 2 // :70

/** compiler-macros.h:91-112 effective defines (char command codes). */
private const val FOR_C = 'f'

/** h011: vendor label set for the dispatch loop. */
private const val L_FOR_AGAIN2 = 0
private const val L_WHILE01 = 1
private const val L_ONE_ARG = 2
private const val L_SINGLES = 3
private const val L_TAIL = 4

@Throws(IOException::class)
fun forCode(): Int {
    val st = CompilerState

    st.for_fcn_flg = 0
    st.for_fcn_flg2 = 0
    st.forxFLG = 1
    st.forLine = 1

    if (st.xd[st.xpx.toInt()] == ' ') { // argumentless form
        val line = "\twhile(1) {"
        st.out.write(line)
        linepad(line.length, "Argumentless FOR") // h014 stub
        if (st.xd[st.xpx.toInt() + 1] != '{') st.IF_flg++ // what is this?
        return 0 // goto next_cmnd;
    }

    st.for_mult = -1
    st.SinglesFlg = 0

    // for_again: (no reachable backward goto - see h013 note)
    st.t0px = 1

    var label = L_FOR_AGAIN2

    if (st.xd[st.xpx.toInt()] == '$') { // while case - no start, no finish
        st.forv[0] = NUL // strcpy(forv,"")
        st.forx++
        if (st.forx > st.forxmax) st.forxmax = st.forx

        st.out.write("\tstrcpy(for" + st.forx + "_lim, \"\");\n") // empty string limit

        label = L_WHILE01 // goto while01
    } else {
        st.ierr = parse_().toShort() // h014 stub; h010 widening
        if (st.ierr != 0.toShort()) return PARSE_ERROR // goto parse_err;

        // check if FOR variables allocated
        st.forx++
        if (st.forx > st.forxmax) st.forxmax = st.forx

        st.out.write("\tstrcpy(for" + st.forx + "_lim, \"\");\n") // empty string limit

        st.xpx += 2
        // strcpy (forv, &v1d[1]); retain loop index variable name
        var sOff = 1
        var dOff = 0
        while (st.v1d[sOff] != NUL) {
            st.forv[dOff] = st.v1d[sOff]
            dOff++; sOff++
        }
        st.forv[dOff] = NUL
    }

    // h011: dispatch loop - for_again2 is the only reachable
    // backward goto target (continue from the singles comma arm).
    while (true) {
        when (label) {

            L_FOR_AGAIN2 -> {

                // for_again2:
                if (st.xd[st.xpx.toInt()] == '$') { // for i=$xxx(aaa) case
                    st.for_fcn_flg = 1 // a function
                    st.for_fcn_flg2 = 1

                    if (cvar(cstringAt(st.forv))) {
                        // vendor: if (cvar(forv)) ; - empty statement, kept
                    } else {
                        st.out.write(
                            "\tsym_(SYMSTORE, (UNS) \"" + cstringAt(st.forv) +
                                "\", (UNS) \"\", svPtr); // init $ case 2518 \n"
                        )
                    }

                    st.out.write("\n")
                    st.out.write("For_Group" + st.forx + ": // m2522\n\n")
                }

                st.t2 = 1
                st.t0px = 0
                st.ierr = parse_().toShort() // h014 stub; h010 widening
                if (st.ierr != 0.toShort()) return PARSE_ERROR // goto parse_err;

                if (st.for_fcn_flg != 0) {
                    label = L_ONE_ARG // goto one_arg
                } else {
                    st.out.write(
                        "\tstrmove((UNS) for" + st.forx + "_init, (UNS) " +
                            Extract2(st.pd1, st.sdlim.toInt()) + "); // init value m2534\n"
                    ) // initial index value

                    if (st.xd[st.xpx.toInt()] == ',' || st.xd[st.xpx.toInt()] == NUL ||
                        st.xd[st.xpx.toInt()] == ' ') {
                        st.out.write(
                            "\tsym_(SYMSTORE, (UNS) \"" + cstringAt(st.forv) +
                                "\",(UNS) for" + st.forx + "_init, svPtr); // init list loop variable\n"
                        )
                        st.SinglesFlg = 1
                        label = L_SINGLES // goto singles
                    } else {
                        if (st.xd[st.xpx.toInt()] != ':') return ARG_LIST_ERROR // goto arg_list_error;

                        st.for_fcn_flg = 0 // is the increment a function?
                        st.t2 = 1
                        st.xpx++

                        label = L_WHILE01 // fall into while01
                    }
                }
            }

            L_WHILE01 -> {

                // while01:
                if (st.xd[st.xpx.toInt()] == '$') {

                    st.for_fcn_flg = 1 // incr is a function

                    if (cstringAt(st.forv).isNotEmpty()) {
                        if (cvar(cstringAt(st.forv))) {
                            st.out.write(
                                "\tstrcpy(" + cstringAt(st.forv) + ",for" +
                                    st.forx + "_init); // init 2559\n"
                            )
                        } else {
                            st.out.write(
                                "\tsym_(SYMSTORE,(UNS) \"" + cstringAt(st.forv) +
                                    "\", (UNS) for" + st.forx + "_init, svPtr); // init m2564 \n"
                            )
                        }
                    }
                    st.out.write("For_Group" + st.forx + ": // m2566\n")
                }

                st.t2 = 1
                st.t0px = 0
                st.ierr = parse_().toShort() // h014 stub; h010 widening
                if (st.ierr != 0.toShort()) return PARSE_ERROR // goto parse_err;

                st.out.write(
                    "\tstrmove( (UNS) for" + st.forx + "_incr, (UNS) " +
                        Extract2(st.pd1, st.sdlim.toInt()) + "); // inc/dec m2575\n"
                ) // increment amount

                if (st.xd[st.xpx.toInt()] != ':') {
                    st.FOR_limit = 0 // no upper limit
                } else {
                    st.t2 = 1
                    st.xpx++
                    st.ierr = parse_().toShort() // h014 stub; h010 widening
                    if (st.ierr != 0.toShort()) return PARSE_ERROR // goto parse_err;
                    st.out.write(
                        "\tstrmove((UNS) for" + st.forx + "_lim, (UNS) " +
                            Extract2(st.pd1, st.sdlim.toInt()) + "); // limit value\n"
                    ) // upper limit
                    st.FOR_limit = 1
                }

                label = L_ONE_ARG // fall into one_arg
            }

            L_ONE_ARG -> {

                // one_arg:
                if (st.for_fcn_flg == 0) {

                    //--------------------------------------------
                    //  extract and setup increment/decrement
                    //--------------------------------------------
                    st.out.write(
                        "\n\tif (strchr((char *) for" + st.forx + "_incr, '.')) // inc/dec is float\n" +
                            "\t\tfor" + st.forx + "F = atof(for" + st.forx +
                            "_incr) < 0.0 ? 1 : 0; // incr or decr?\n"
                    )

                    st.out.write(
                        "\telse // inc/dec is integer\n" +
                            "\t\tfor" + st.forx + "F = atol(for" + st.forx +
                            "_incr) < 0 ? 1 : 0; // incr or decr?\n"
                    )

                    //----------------------------------------
                    //  start loop and init loop variable
                    //----------------------------------------
                    if (cvar(cstringAt(st.forv))) {
                        st.out.write(
                            "\n\tfor (\tstrcpy(" + cstringAt(st.forv) +
                                ", for" + st.forx + "_init); // init 2613\n"
                        )
                    } else {
                        st.out.write(
                            "\n\tfor ( sym_(SYMSTORE, (UNS) \"" + cstringAt(st.forv) +
                                "\", (UNS) for" + st.forx + "_init, svPtr); // init 2618\n"
                        )
                    }

                    //----------------------------
                    //  if there is a limit
                    //----------------------------
                    st.out.write("\n")

                    if (st.FOR_limit != 0) {
                        if (cvar(cstringAt(st.forv))) {
                            st.out.write(
                                "\t\tfor" + st.forx + "F ? \n" +
                                    " numcomp((UNS) " + cstringAt(st.forv) + ", (UNS) for" + st.forx + "_lim) >= 0\n : \n" +
                                    " numcomp((UNS) " + cstringAt(st.forv) + ", (UNS) for" + st.forx + "_lim) <= 0; \n\n"
                            )
                        } else {
                            st.out.write(
                                "\t\tfor" + st.forx + "F ? // loop limit test direction\n" +
                                    "\t\t\tnumcomp((UNS) sym_(11, (UNS) \"" + cstringAt(st.forv) + "\", \n" +
                                    "\t\t\t\t _dummy, svPtr),\n" +
                                    "\t\t\t\t(UNS) for" + st.forx + "_lim) >= 0 // GE limit value?\n" +
                                    "\t\t\t: // ternary operator\n" +
                                    "\t\t\tnumcomp((UNS) sym_(11, (UNS) \"" + cstringAt(st.forv) + "\", \n" +
                                    "\t\t\t\t_dummy,svPtr),\n" +
                                    "\t\t\t\t(UNS) for" + st.forx + "_lim) <= 0; // LE limit value\n\n"
                            )
                        }
                    } else {
                        //-----------------
                        //  no limit
                        //-----------------
                        st.out.write("\t\t1;  // limit expression - no limit\n\n")
                    }

                    //-------------------------------
                    //  increment or decrement
                    //-------------------------------
                    if (cvar(cstringAt(st.forv))) {
                        st.out.write(
                            "\tadd(" + cstringAt(st.forv) + ", for" + st.forx + "_incr, " +
                                cstringAt(st.forv) + ")\n\t)\n\n"
                        )
                    } else {
                        st.out.write(
                            "\t\tadd( sym_(11, (UNS) \"" + cstringAt(st.forv) +
                                "\", _dummy, svPtr), // inc/dec loop var\n" +
                                "\t\t\tfor" + st.forx + "_incr, (char *) _dummy),\n" +
                                "\t\tsym_(SYMSTORE, (UNS) \"" + cstringAt(st.forv) +
                                "\", (UNS) _dummy, svPtr) ) // store rslt\n\n"
                        )
                    }

                    label = L_SINGLES // fall into singles
                } else { // function increment section

                    if (cstringAt(st.forv).isNotEmpty() && st.for_fcn_flg2 == 0) {

                        if (cvar(cstringAt(st.forv))) {
                            st.out.write(
                                "\tstrcpy(" + cstringAt(st.forv) + ",for" +
                                    st.forx + "_incr); // set loop variable m2711\n"
                            )
                        } else {
                            st.out.write(
                                "\tsym_(SYMSTORE, (UNS) \"" + cstringAt(st.forv) +
                                    "\", (UNS) for" + st.forx + "_incr, svPtr); // set loop variable m2719\n"
                            )
                        }

                        st.out.write(
                            "\tif (strcmp( for" + st.forx + "_incr, for" + st.forx +
                                "_lim) == 0) goto For" + st.forx + "Exit; // exit if match 2725\n\n"
                        )
                    } else if (cstringAt(st.forv).isNotEmpty() && st.for_fcn_flg2 != 0) {

                        //-------------------------------------
                        //  declared or regular variable?
                        //-------------------------------------
                        if (cvar(cstringAt(st.forv))) { // declared
                            st.out.write(
                                "\tstrcpy(" + cstringAt(st.forv) + ", CSTR " +
                                    Extract2(st.pd1, st.sdlim.toInt()) + "); // set loop dcl var $ case\n"
                            )

                            st.out.write(
                                "\tif (strcmp( (char *) " + Extract2(st.pd1, st.sdlim.toInt()) +
                                    ", \"0\") == 0 || strlen( (const char *) " +
                                    Extract2(st.pd1, st.sdlim.toInt()) + ") == 0 ) \n" +
                                    "\t\tgoto For" + st.forx + "Exit; // exit if match 2743\n"
                            )
                        } else { // regular
                            st.out.write(
                                "\tsym_(SYMSTORE, (UNS) \"" + cstringAt(st.forv) +
                                    "\", (UNS) " + Extract2(st.pd1, st.sdlim.toInt()) +
                                    ", svPtr); // set loop variable m2752\n"
                            )

                            st.out.write(
                                "\tif (strlen( (const char *) " + Extract2(st.pd1, st.sdlim.toInt()) +
                                    ") == 0 ) goto For" + st.forx + "Exit; // exit if empty 2758\n"
                            )
                        }
                    } else {
                        st.out.write(
                            "\tif (strcmp( for" + st.forx + "_incr, \"0\")==0 || " +
                                "strlen(for" + st.forx + "_incr)==0 ) goto For" +
                                st.forx + "Exit; // exit if match 2767\n"
                        )
                    }

                    st.out.write("\tif ( setjmp( for" + st.forx + "J ) == 0 ) goto For" + st.forx + "; \n")

                    //-------------------------------------------------------------
                    //  $order optimization hack
                    //  Purpose: to get the names of the tmp variables
                    //  used in the initial GlobalOrder() call. This
                    //  permits using the value returned by GlobalOrder()
                    //  as the next input to GlobalOrder() without
                    //  rebuilding the global array reference.
                    //-------------------------------------------------------------

                    st.out.write("\tgoto For_Group" + st.forx + "; // m2771\n")

                    label = L_TAIL
                }
            }

            L_SINGLES -> {

                // singles:
                if (st.for_mult >= 0) {
                    st.out.write(
                        "\t\tif ( (_i = setjmp(for" + st.for_mult + "J)) == 0) goto For" +
                            st.for_mult + "; // else continue\n"
                    )
                    st.out.write("\telse if (_i == 100) goto For" + st.forx + "Exit; // f264\n\n")
                } else {
                    st.out.write(
                        "\t\tif ((_i = setjmp(for" + st.forx + "J)) == 0) goto For" +
                            st.forx + "; // else continue\n\n"
                    )
                    st.out.write("\telse if (_i == 100) goto For" + st.forx + "Exit; // f273\n\n")
                }

                if (st.xd[st.xpx.toInt()] == ',') {
                    st.xpx++
                    st.for_mult = st.forx
                    label = L_FOR_AGAIN2 // goto for_again2
                    // h013: vendor unreachable tail dropped (see header)
                } else {
                    label = L_TAIL
                }
            }

            L_TAIL -> {

                if (st.xd[st.xpx.toInt()] == ' ' ||
                    st.xd[st.xpx.toInt()] == NUL ||
                    st.xd[st.xpx.toInt()] == '\t'
                ) {

                    if (st.xd[st.xpx.toInt() + 1] != '{') st.IF_flg++

                    if (st.for_mult >= 0) {
                        st.ForBlocks[st.IF_flg] = st.for_mult.toShort()

                        st.out.write("\tgoto For" + st.for_mult + "Exit; // done: bypass block \n\n")

                        st.out.write("For" + st.for_mult + ": // for loop entry point A\n\n")

                        st.ForBlocks[st.IF_flg] = st.forx.toShort()
                        st.Blocks[st.IF_flg] = FOR_C.code.toShort()
                    } else {
                        st.out.write("\tgoto For" + st.forx + "Exit; // bypass block \n\n")

                        st.out.write("For" + st.forx + ": // for loop body entry point B\n\n")

                        st.ForBlocks[st.IF_flg] = st.forx.toShort()
                        st.Blocks[st.IF_flg] = FOR_C.code.toShort()
                    }

                    st.out.write(
                        "\tdo { // for loop body\n\t\tsvPtr->LineNumber = " + st.lines + "; \n"
                    )

                    return 0 // goto next_cmnd;
                }

                return ARG_LIST_ERROR // goto arg_list_error;
            }
        }
    }
}
