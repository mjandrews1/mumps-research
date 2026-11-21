package daf

// Unit 122 (DAF B4 T3, complex): compiler-for.c::for_code (232 LOC):
// the FOR command emitter: the argumentless while(1) shortcut, the
// index-variable init/increment/limit parse-and-emit chain, the
// function-increment (\\$-case) group form, the singles/multi-block
// setjmp bookkeeping, and the do{ loop-body entry emit.
// Consults: BASE-log rows 10/73 manual-decision (the goto for_again/
// for_again2/while01/one_arg/singles mesh is a FORWARD-branching gotos
// mesh that does not reduce to a labeled-loop nest; rendered as the
// five-state dispatch machine below, with pc values pinned to the
// vendor labels; the vendor's UNREACHABLE post-goto SinglesFlg
// branches inside the singles tail are kept as the comment-fold),
// rows 2/26 hit-reuse (fprintf(Out,...) -> C.emit verbatim, the %s
// template pieces substituted exactly as the vendor's argv; the %%s/
// \\" escapes preserved; atol/atof/strchr stay INSIDE the emitted
// text), rows 6/22 hit-reuse (the compiled-in TU globals ride the
// DafCompilerState carrier; the extra vendor globals forLine/forxFLG
// added to the state object), row 81 (&pd1[sdlim] stays literal
// emitted text; the Extract2 calls resolve to the seat), row 30
// (Extract2 out-param fold; sym_(11)/SYMSTORE emitted text:
// seat-mediated), row 99 (the PARSE_ERROR/ARG_LIST_ERROR return-code
// folds).
// Seats: parseDaf (b9), extract2Daf (b1), cvarDaf (compiler TU),
// linepadDaf (the compiler-TU pad helper; named-owner stub raised
// here), FOR_D pinned from compiler-macros.h #define FOR 'f' (l.103).
// Dafny: EXEMPT (emitter; journal row).

const val FOR_D = 'f'.code // compiler-macros.h #define FOR 'f'

fun forCodeDaf(): Int {

    val C = DafCompilerState
    val sv = DafInterpState.svPtr

    C.for_fcn_flg = 0
    C.for_fcn_flg2 = 0
    C.forxFLG = 1
    C.forLine = 1

    if (at(C.xd, C.xpx) == ' ') { // argumentless form
        linepadDaf(C, "\twhile(1) {", "Argumentless FOR")
        if (at(C.xd, C.xpx + 1) != '{') C.IF_flg++ // what is this?
        return 0 // goto next_cmnd
    }

    C.for_mult = -1
    C.SinglesFlg = 0

    var pc = 0 // 0=for_again 1=for_again2 2=while01 3=one_arg 4=singles
    while (true) {
        when (pc) {

        0 -> { // for_again:
            C.t0px = 1

            if (at(C.xd, C.xpx) == '\$') { // while case - no start, no finish
                forvSetDaf("") // strcpy(forv,"")
                C.forx++
                if (C.forx > C.forxmax) C.forxmax = C.forx
                C.emit("\tstrcpy(for${C.forx}_lim, \\\"\\\");\n") // empty limit
                pc = 2 // goto while01
            } else {
                C.ierr = parseDaf(sv)
                if (C.ierr != 0) return PARSE_ERROR_D // goto parse_err

                // check if FOR variables allocated
                C.forx++
                if (C.forx > C.forxmax) C.forxmax = C.forx

                C.emit("\tstrcpy(for${C.forx}_lim, \\\"\\\");\n")

                C.xpx += 2
                forvSetDaf(DafCRuntime.cstringAt(C.v1d, 1)) // retain index var
                pc = 1 // fall to for_again2
            }
        }

        1 -> { // for_again2:
            if (at(C.xd, C.xpx) == '\$') { // for i=\$xxx(aaa) case
                C.for_fcn_flg = 1 // a function
                C.for_fcn_flg2 = 1

                if (!cvarDaf(forvGetDaf())) {
                    C.emit("\tsym_(SYMSTORE, (UNS) \\\"${forvGetDaf()}\\\", " +
                            "(UNS) \\\"\\\", svPtr); // init \$ case 2518 \n")
                }

                C.emit("\n")
                C.emit("For_Group${C.forx}: // m2522\n\n")
            }

            C.t2 = 1
            C.t0px = 0
            C.ierr = parseDaf(sv)
            if (C.ierr != 0) return PARSE_ERROR_D // goto parse_err

            if (C.for_fcn_flg != 0) {
                pc = 3 // goto one_arg
            } else {
                C.emit("\tstrmove((UNS) for${C.forx}_init, " +
                        "(UNS) ${extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim))}); " +
                        "// init value m2534\n")

                if (at(C.xd, C.xpx) == ',' || at(C.xd, C.xpx) == NUL_D ||
                    at(C.xd, C.xpx) == ' ') {
                    C.emit("\tsym_(SYMSTORE, (UNS) \\\"${forvGetDaf()}\\\"," +
                            "(UNS) for${C.forx}_init, svPtr); " +
                            "// init list loop variable\n")
                    C.SinglesFlg = 1
                    pc = 4 // goto singles
                } else if (at(C.xd, C.xpx) != ':') {
                    return ARG_LIST_ERROR_D // goto arg_list_error
                } else {
                    C.for_fcn_flg = 0 // is the increment a function?
                    C.t2 = 1
                    C.xpx++
                    pc = 2 // goto while01
                }
            }
        }

        2 -> { // while01:
            if (at(C.xd, C.xpx) == '\$') {

                C.for_fcn_flg = 1 // incr is a function

                if (forvGetDaf().isNotEmpty()) { // strlen(forv)
                    if (cvarDaf(forvGetDaf())) {
                        C.emit("\tstrcpy(${forvGetDaf()},for${C.forx}_init); " +
                                "// init 2559\n")
                    } else {
                        C.emit("\tsym_(SYMSTORE,(UNS) \\\"${forvGetDaf()}\\\", " +
                                "(UNS) for${C.forx}_init, svPtr); // init m2564 \n")
                    }
                }
                C.emit("For_Group${C.forx}: // m2566\n")
            }

            C.t2 = 1
            C.t0px = 0

            C.ierr = parseDaf(sv)
            if (C.ierr != 0) return PARSE_ERROR_D // goto parse_err

            C.emit("\tstrmove( (UNS) for${C.forx}_incr, " +
                    "(UNS) ${extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim))}); " +
                    "// inc/dec m2575\n")

            if (at(C.xd, C.xpx) != ':') {
                C.FOR_limit = 0 // no upper limit
            } else {
                C.t2 = 1
                C.xpx++
                C.ierr = parseDaf(sv)
                if (C.ierr != 0) return PARSE_ERROR_D // goto parse_err
                C.emit("\tstrmove((UNS) for${C.forx}_lim, " +
                        "(UNS) ${extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim))}); " +
                        "// limit value\n")
                C.FOR_limit = 1
            }

            pc = 3 // fall into one_arg
        }

        3 -> { // one_arg:
            if (C.for_fcn_flg == 0) {

                // extract and setup increment/decrement

                C.emit("\n\tif (strchr((char *) for${C.forx}_incr, '.')) " +
                        "// inc/dec is float\n" +
                        "\t\tfor${C.forx}F = atof(for${C.forx}_incr) " +
                        "< 0.0 ? 1 : 0; // incr or decr?\n")

                C.emit("\telse // inc/dec is integer\n" +
                        "\t\tfor${C.forx}F = atol(for${C.forx}_incr) " +
                        "< 0 ? 1 : 0; // incr or decr?\n")

                // start loop and init loop variable

                if (cvarDaf(forvGetDaf())) {
                    C.emit("\n\tfor (\tstrcpy(${forvGetDaf()}, " +
                            "for${C.forx}_init); // init 2613\n")
                } else {
                    C.emit("\n\tfor ( sym_(SYMSTORE, (UNS) " +
                            "\\\"${forvGetDaf()}\\\", " +
                            "(UNS) for${C.forx}_init, svPtr); // init 2618\n")
                }

                // if there is a limit

                C.emit("\n")

                if (C.FOR_limit != 0) {
                    if (cvarDaf(forvGetDaf())) {
                        C.emit("\t\tfor${C.forx}F ? \n" +
                                " numcomp((UNS) ${forvGetDaf()}, " +
                                "(UNS) for${C.forx}_lim) >= 0\n : \n" +
                                " numcomp((UNS) ${forvGetDaf()}, " +
                                "(UNS) for${C.forx}_lim) <= 0; \n\n")
                    } else {
                        C.emit("\t\tfor${C.forx}F ? // loop limit test direction\n" +
                                "\t\t\tnumcomp((UNS) sym_(11, (UNS) " +
                                "\\\"${forvGetDaf()}\\\", \n" +
                                "\t\t\t\t _dummy, svPtr),\n" +
                                "\t\t\t\t(UNS) for${C.forx}_lim) " +
                                ">= 0 // GE limit value?\n" +
                                "\t\t\t: // ternary operator\n" +
                                "\t\t\tnumcomp((UNS) sym_(11, (UNS) " +
                                "\\\"${forvGetDaf()}\\\", \n" +
                                "\t\t\t\t_dummy,svPtr),\n" +
                                "\t\t\t\t(UNS) for${C.forx}_lim) " +
                                "<= 0; // LE limit value\n\n")
                    }
                } else {
                    // no limit
                    C.emit("\t\t1;  // limit expression - no limit\n\n")
                }

                // increment or decrement

                if (cvarDaf(forvGetDaf())) {
                    C.emit("\tadd(${forvGetDaf()}, for${C.forx}_incr, " +
                            "${forvGetDaf()})\n\t)\n\n")
                } else {
                    C.emit("\t\tadd( sym_(11, (UNS) \\\"${forvGetDaf()}\\\", " +
                            "_dummy, svPtr), // inc/dec loop var\n" +
                            "\t\t\tfor${C.forx}_incr, (char *) _dummy),\n" +
                            "\t\tsym_(SYMSTORE, (UNS) \\\"${forvGetDaf()}\\\", " +
                            "(UNS) _dummy, svPtr) ) // store rslt\n\n")
                }

                // singles: (the label target; also the pc=4 entry)
                pc = 4
            } else { // function increment section

                if (forvGetDaf().isNotEmpty() && C.for_fcn_flg2 == 0) {

                    if (cvarDaf(forvGetDaf())) {
                        C.emit("\tstrcpy(${forvGetDaf()},for${C.forx}_incr); " +
                                "// set loop variable m2711\n")
                    } else {
                        C.emit("\tsym_(SYMSTORE, (UNS) \\\"${forvGetDaf()}\\\", " +
                                "(UNS) for${C.forx}_incr, svPtr); " +
                                "// set loop variable m2719\n")
                    }

                    C.emit("\tif (strcmp( for${C.forx}_incr, " +
                            "for${C.forx}_lim) == 0) " +
                            "goto For${C.forx}Exit; // exit if match 2725\n\n")
                } else if (forvGetDaf().isNotEmpty() && C.for_fcn_flg2 != 0) {

                    // declared or regular variable?

                    if (cvarDaf(forvGetDaf())) { // declared
                        C.emit("\tstrcpy(${forvGetDaf()}, CSTR " +
                                "${extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim))});" +
                                " // set loop dcl var \$ case\n")
                        val e2 = extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim))
                        C.emit("\tif (strcmp( (char *) ${e2}, \\\"0\\\") == 0 || " +
                                "strlen( (const char *) ${e2}) == 0 ) \n" +
                                "\t\tgoto For${C.forx}Exit; // exit if match 2743\n")
                    } else { // regular
                        C.emit("\tsym_(SYMSTORE, (UNS) \\\"${forvGetDaf()}\\\", " +
                                "(UNS) ${extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim))}, " +
                                "svPtr); // set loop variable m2752\n")
                        C.emit("\tif (strlen( (const char *) " +
                                "${extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim))}) == 0 ) " +
                                "goto For${C.forx}Exit; // exit if empty 2758\n")
                    }
                } else {
                    C.emit("\tif (strcmp( for${C.forx}_incr, \\\"0\\\")==0 || " +
                            "strlen(for${C.forx}_incr)==0 ) " +
                            "goto For${C.forx}Exit;" +
                            " // exit if match 2767\n")
                }

                C.emit("\tif ( setjmp( for${C.forx}J ) == 0 ) " +
                        "goto For${C.forx}; \n")

                // --------------------------------------------------------
                // \\$order optimization hack: reusing the tmp-variable
                // names of the initial GlobalOrder() call permits the
                // returned value to feed the next GlobalOrder() without
                // rebuilding the global array reference (vendor comment).
                // --------------------------------------------------------

                C.emit("\tgoto For_Group${C.forx}; // m2771\n")

                // fall to the tail checks below (shared finals)
                return forTailDaf()
            }

            // (state fall: pc == 4 continues below)
            if (pc != 4) return ARG_LIST_ERROR_D
            pc = 4
        }

        else -> { // 4: singles:
            if (C.for_mult >= 0) {
                C.emit("\t\tif ( (_i = setjmp(for${C.for_mult}J)) == 0) " +
                        "goto For${C.for_mult}; // else continue\n")
                C.emit("\telse if (_i == 100) goto For${C.forx}Exit; // f264\n\n")
            } else {
                C.emit("\t\tif ((_i = setjmp(for${C.forx}J)) == 0) " +
                        "goto For${C.forx}; // else continue\n\n")
                C.emit("\telse if (_i == 100) goto For${C.forx}Exit; // f273\n\n")
            }

            if (at(C.xd, C.xpx) == ',') {
                C.xpx++
                C.for_mult = C.forx
                pc = 1 // goto for_again2
                // (unreachable vendor text after this goto:
                //   if (SinglesFlg) goto for_again2; else goto for_again;
                // comment-fold, unreachable by construction)
            } else {
                return forTailDaf()
            }
        }
        }
    }
}

// the shared final checks of the function (the block after one_arg/
// the function-increment section; vendor text falls through to here
// from both paths)
private fun forTailDaf(): Int {

    val C = DafCompilerState

    if (at(C.xd, C.xpx) == ' ' || at(C.xd, C.xpx) == NUL_D ||
        at(C.xd, C.xpx) == TAB_D) {

        if (at(C.xd, C.xpx + 1) != '{') C.IF_flg++

        if (C.for_mult >= 0) {
            C.ForBlocks[C.IF_flg] = C.for_mult
            C.emit("\tgoto For${C.for_mult}Exit; // done: bypass block \n\n")
            C.emit("For${C.for_mult}: // for loop entry point A\n\n")
            C.ForBlocks[C.IF_flg] = C.forx
            C.Blocks[C.IF_flg] = FOR_D
        } else {
            C.emit("\tgoto For${C.forx}Exit; // bypass block \n\n")
            C.emit("For${C.forx}: // for loop body entry point B\n\n")
            C.ForBlocks[C.IF_flg] = C.forx
            C.Blocks[C.IF_flg] = FOR_D
        }

        C.emit("\tdo { // for loop body\n\t\tsvPtr->LineNumber = " +
                "${C.lines}; \n")

        return 0 // goto next_cmnd
    }

    return ARG_LIST_ERROR_D // goto arg_list_error
}

// forv CharArray(128) carrier: strcpy folds
private fun forvSetDaf(s: String) {
    val C = DafCompilerState
    var d = 0
    while (d < s.length && d < C.forv.size) { C.forv[d] = s[d]; d++ }
    C.forv[d] = NUL_D
}

private fun forvGetDaf(): String = DafCRuntime.cstring(DafCompilerState.forv)

// linepad - the compiler-TU line-pad helper that flushes the emitted
// fragment and pads the trailing comment tag; named-owner stub.
fun linepadDaf(C: DafCompilerState, text: String, tag: String): Unit =
    throw NotImplementedError(
        "linepad: out-of-scope owner (compiler TU pad helper; macros family)")
