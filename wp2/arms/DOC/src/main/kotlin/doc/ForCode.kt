package doc

// Unit 122 (B4 tranche 3): compiler-for.c::for_code (batch 4,
// complex stratum; units.csv row 96: loc 232, ptr 1, ctrl 119,
// score 175.2, rank 289) - the FOR command compiler: the
// argumentless form, the list-argument FOR roster, the dollar-
// while case (no start value; the goto while01 skip), the
// function-valued start/increment cases (for_fcn_flg / the
// one_arg fcn section), the increment parse, the optional limit,
// the float/integer incr-decr direction emission pair, the loop
// init emission family (cvar decl vs sym_), the limit numcomp
// emissions, the singles comma chain (goto for_again2 with the
// vendor's DEAD code after the goto ledgered here, not rendered
// - H-013), the setjmp scaffolding A/B arms (the f264 arg quirk:
// the for_mult>=0 arm's SECOND line prints FORX not for_mult -
// kept verbatim), and the ForBlocks/Blocks close with the
// LineNumber emission.
//
// EMITTED C text kept byte-identical (the m2522/m2534/m2564/
// m2566/m2575/m2711/m2719/m2719? m2752 f-number fossils, the
// 2518/2559/2613/2618/264/273/2725/2743/2758/2767/2771 comment
// anchors); the two UNUSED-VARARG warts of the vendor (the float
// incr/dec pair passes forx five times over three conversions;
// the integer pair three times over two) land identical bytes
// because every substituted conversion takes the same forx value
// - ledgered here, the extra args carry no bytes. The state
// externs for_fcn_flg/for_fcn_flg2/forxFLG/forLine/forx/forxmax/
// for_mult (short, H-010 Int)/SinglesFlg (short, Int)/forv
// (unsigned char[256], CharArray)/FOR_limit are the NEW
// CompilerState seats of this unit (EDIT-ONLY, vendor anchors
// compiler-globals.h:110-151). linepad is the H-014 stub seat;
// parse_ is the compiler-twin stub seat (LockCode precedent);
// cvar is the unit-121 seat; Extract2 and atTest... only
// Extract2 here - is the REAL unit-15 render via the arm
// Stubs delegate. The goto control-flow renders as the
// skip-while01 / goto-singles / comma-chain continue pattern of
// the arm (goto set of LockCode/NewCode precedent). The dollar
// of the emitted comments (the two f-number dcomments with the
// dollar byte, the while-case test) renders as the u0024 escape
// with identical bytes. strings compared on xd kept as Char.
// Consult rows logged 2026-11-19T20:53:08Z. Naming per H-017.

private const val PARSE_ERROR_FC = 1 // compiler-macros.h:69
private const val ARG_LIST_ERROR_FC = 2 // compiler-macros.h:70
private const val FOR_FC = 'f' // compiler-macros.h:103
private const val DOLLAR_FC = '\u0024'

fun for_code(): Int {
    CompilerState.for_fcn_flg = 0
    CompilerState.for_fcn_flg2 = 0
    CompilerState.forxFLG = 1
    CompilerState.forLine = 1
    if (CompilerState.xd[CompilerState.xpxIdx()] == ' ') { // argumentless form
        Stubs.linepad(
            CompilerState.emit("\twhile(1) {"),
            "Argumentless FOR"
        )
        if (CompilerState.xd[CompilerState.xpxIdx() + 1] != '{') {
            CompilerState.IF_flg++ // what is this? (vendor comment kept)
        }
        return 0 // goto next_cmnd;
    }

    CompilerState.for_mult = -1
    CompilerState.SinglesFlg = 0

    // for_again:
    while (true) {
        CompilerState.t0px = 1

        var skipWhile01 = false
        var gotoSingles = false

        if (CompilerState.xd[CompilerState.xpxIdx()] == DOLLAR_FC) {
            // while case - no start, no finish
            CstringOps.strcpyS(CompilerState.forv, "")
            CompilerState.forx++
            if (CompilerState.forx > CompilerState.forxmax) {
                CompilerState.forxmax = CompilerState.forx
            }

            // empty string limit
            CompilerState.emit(
                "\tstrcpy(for" + CompilerState.forx + "_lim, \"\");\n"
            )

            skipWhile01 = true // goto while01;
        }

        if (!skipWhile01) {
            // check if FOR variables allocated
            CompilerState.ierr = Stubs.parse_()
            if (CompilerState.ierr != 0) return PARSE_ERROR_FC // goto parse_err;

            CompilerState.forx++
            if (CompilerState.forx > CompilerState.forxmax) {
                CompilerState.forxmax = CompilerState.forx
            }

            CompilerState.emit(
                "\tstrcpy(for" + CompilerState.forx + "_lim, \"\");\n"
            ) // empty string limit

            CompilerState.xpx += 2
            // retain loop index variable name
            CstringOps.strcpyS(
                CompilerState.forv,
                CstringOps.cstringAt(CompilerState.v1d, 1)
            )
        }

        // for_again2:
        forAgain2@ while (true) {
            var oneArgFcn = false

            if (!skipWhile01) {
                skipWhile01 = false

                if (CompilerState.xd[CompilerState.xpxIdx()] == DOLLAR_FC) {
                    // for i=\u0024xxx(aaa) case (vendor comment,
                    // the dollar escape noted in the banner)
                    CompilerState.for_fcn_flg = 1 // a function
                    CompilerState.for_fcn_flg2 = 1

                    if (Stubs.cvar(CstringOps.cstring(CompilerState.forv)) != 0) {
                        // the vendor empty statement - the if-else
                        // cvar split kept
                    } else {
                        CompilerState.emit(
                            "\tsym_(SYMSTORE, (UNS) \"" +
                                CstringOps.cstring(CompilerState.forv) +
                                "\", (UNS) \"\", svPtr); // init " + DOLLAR_FC +
                                " case 2518 \n"
                        )
                    }

                    CompilerState.emit("\n")
                    CompilerState.emit(
                        "For_Group" + CompilerState.forx + ": // m2522\n\n"
                    )
                }

                CompilerState.t2 = 1
                CompilerState.t0px = 0
                CompilerState.ierr = Stubs.parse_()
                if (CompilerState.ierr != 0) return PARSE_ERROR_FC

                if (CompilerState.for_fcn_flg != 0) {
                    oneArgFcn = true // goto one_arg;
                } else {
                    // initial index value
                    CompilerState.emit(
                        "\tstrmove((UNS) for" + CompilerState.forx +
                            "_init, (UNS) " + extract2pd() + "); // init value m2534\n"
                    )

                    if (CompilerState.xd[CompilerState.xpxIdx()] == ',' ||
                        CompilerState.xd[CompilerState.xpxIdx()] == NUL ||
                        CompilerState.xd[CompilerState.xpxIdx()] == ' '
                    ) {
                        CompilerState.emit(
                            "\tsym_(SYMSTORE, (UNS) \"" +
                                CstringOps.cstring(CompilerState.forv) +
                                "\",(UNS) for" + CompilerState.forx +
                                "_init, svPtr); // init list loop variable\n"
                        )
                        CompilerState.SinglesFlg = 1
                        gotoSingles = true // goto singles;
                    } else {
                        if (CompilerState.xd[CompilerState.xpxIdx()] != ':') {
                            return ARG_LIST_ERROR_FC // goto arg_list_error;
                        }

                        CompilerState.for_fcn_flg = 0 // is the increment a function?
                        CompilerState.t2 = 1
                        CompilerState.xpxInc()
                    }
                }
            } else {
                skipWhile01 = false
            }

            if (!gotoSingles && !oneArgFcn) {
                // while01:
                if (CompilerState.xd[CompilerState.xpxIdx()] == DOLLAR_FC) {
                    CompilerState.for_fcn_flg = 1 // incr is a function

                    if (CstringOps.strlenC(CompilerState.forv) != 0) {
                        if (Stubs.cvar(CstringOps.cstring(CompilerState.forv)) != 0) {
                            CompilerState.emit(
                                "\tstrcpy(" + CstringOps.cstring(CompilerState.forv) +
                                    ",for" + CompilerState.forx + "_init); // init 2559\n"
                            )
                        } else {
                            CompilerState.emit(
                                "\tsym_(SYMSTORE,(UNS) \"" +
                                    CstringOps.cstring(CompilerState.forv) +
                                    "\", (UNS) for" + CompilerState.forx +
                                    "_init, svPtr); // init m2564 \n"
                            )
                        }
                    }
                    CompilerState.emit(
                        "For_Group" + CompilerState.forx + ": // m2566\n"
                    )
                }

                CompilerState.t2 = 1
                CompilerState.t0px = 0

                CompilerState.ierr = Stubs.parse_()
                if (CompilerState.ierr != 0) return PARSE_ERROR_FC

                // increment amount
                CompilerState.emit(
                    "\tstrmove( (UNS) for" + CompilerState.forx +
                        "_incr, (UNS) " + extract2pd() + "); // inc/dec m2575\n"
                )

                if (CompilerState.xd[CompilerState.xpxIdx()] != ':') {
                    CompilerState.FOR_limit = 0 // no upper limit
                } else {
                    CompilerState.t2 = 1
                    CompilerState.xpxInc()
                    CompilerState.ierr = Stubs.parse_()
                    if (CompilerState.ierr != 0) return PARSE_ERROR_FC
                    // upper limit
                    CompilerState.emit(
                        "\tstrmove((UNS) for" + CompilerState.forx +
                            "_lim, (UNS) " + extract2pd() + "); // limit value\n"
                    )
                    CompilerState.FOR_limit = 1
                }
            }

            if (CompilerState.for_fcn_flg == 0) {
                // one_arg, non-fcn path:
                // extract and setup increment/decrement
                CompilerState.emit(
                    "\n\tif (strchr((char *) for" + CompilerState.forx +
                        "_incr, '.')) // inc/dec is float\n" +
                        "\t\tfor" + CompilerState.forx + "F = atof(for" +
                        CompilerState.forx + "_incr) < 0.0 ? 1 : 0;" +
                        " // incr or decr?\n"
                )

                CompilerState.emit(
                    "\telse // inc/dec is integer\n" +
                        "\t\tfor" + CompilerState.forx + "F = atol(for" +
                        CompilerState.forx + "_incr) < 0 ? 1 : 0;" +
                        " // incr or decr?\n"
                )

                // start loop and init loop variable
                if (Stubs.cvar(CstringOps.cstring(CompilerState.forv)) != 0) {
                    CompilerState.emit(
                        "\n\tfor (\tstrcpy(" +
                            CstringOps.cstring(CompilerState.forv) +
                            ", for" + CompilerState.forx + "_init); // init 2613\n"
                    )
                } else {
                    CompilerState.emit(
                        "\n\tfor ( sym_(SYMSTORE, (UNS) \"" +
                            CstringOps.cstring(CompilerState.forv) +
                            "\", (UNS) for" + CompilerState.forx +
                            "_init, svPtr); // init 2618\n"
                    )
                }

                // if there is a limit
                CompilerState.emit("\n")

                if (CompilerState.FOR_limit != 0) {
                    if (Stubs.cvar(CstringOps.cstring(CompilerState.forv)) != 0) {
                        CompilerState.emit(
                            "\t\tfor" + CompilerState.forx + "F ? \n" +
                                " numcomp((UNS) " + CstringOps.cstring(CompilerState.forv) +
                                ", (UNS) for" + CompilerState.forx + "_lim) >= 0\n : \n" +
                                " numcomp((UNS) " + CstringOps.cstring(CompilerState.forv) +
                                ", (UNS) for" + CompilerState.forx + "_lim) <= 0; \n\n"
                        )
                    } else {
                        CompilerState.emit(
                            "\t\tfor" + CompilerState.forx + "F ? // loop limit test direction\n" +
                                "\t\t\tnumcomp((UNS) sym_(11, (UNS) \"" +
                                CstringOps.cstring(CompilerState.forv) + "\", \n" +
                                "\t\t\t\t _dummy, svPtr),\n" +
                                "\t\t\t\t(UNS) for" + CompilerState.forx +
                                "_lim) >= 0 // GE limit value?\n" +
                                "\t\t: // ternary operator\n" +
                                "\t\t\tnumcomp((UNS) sym_(11, (UNS) \"" +
                                CstringOps.cstring(CompilerState.forv) + "\", \n" +
                                "\t\t\t\t_dummy,svPtr),\n" +
                                "\t\t\t\t(UNS) for" + CompilerState.forx +
                                "_lim) <= 0; // LE limit value\n\n"
                        )
                    }
                } else {
                    // no limit
                    CompilerState.emit("\t\t1;  // limit expression - no limit\n\n")
                }

                // increment or decrement
                if (Stubs.cvar(CstringOps.cstring(CompilerState.forv)) != 0) {
                    CompilerState.emit(
                        "\tadd(" + CstringOps.cstring(CompilerState.forv) +
                            ", for" + CompilerState.forx + "_incr, " +
                            CstringOps.cstring(CompilerState.forv) + ")\n\t)\n\n"
                    )
                } else {
                    CompilerState.emit(
                        "\t\tadd( sym_(11, (UNS) \"" +
                            CstringOps.cstring(CompilerState.forv) +
                            "\", _dummy, svPtr), // inc/dec loop var\n" +
                            "\t\t\tfor" + CompilerState.forx +
                            "_incr, (char *) _dummy),\n" +
                            "\t\tsym_(SYMSTORE, (UNS) \"" +
                            CstringOps.cstring(CompilerState.forv) +
                            "\", (UNS) _dummy, svPtr) ) // store rslt\n\n"
                    )
                }
            }

            if (!gotoSingles) {
                // function increment section (one_arg fcn arm)
                if (CstringOps.strlenC(CompilerState.forv) != 0 &&
                    CompilerState.for_fcn_flg2 == 0
                ) {
                    if (Stubs.cvar(CstringOps.cstring(CompilerState.forv)) != 0) {
                        CompilerState.emit(
                            "\tstrcpy(" + CstringOps.cstring(CompilerState.forv) +
                                ",for" + CompilerState.forx +
                                "_incr); // set loop variable m2711\n"
                        )
                    } else {
                        CompilerState.emit(
                            "\tsym_(SYMSTORE, (UNS) \"" +
                                CstringOps.cstring(CompilerState.forv) +
                                "\", (UNS) for" + CompilerState.forx +
                                "_incr, svPtr); // set loop variable m2719\n"
                        )
                    }

                    CompilerState.emit(
                        "\tif (strcmp( for" + CompilerState.forx + "_incr, for" +
                            CompilerState.forx + "_lim) == 0) goto For" +
                            CompilerState.forx + "Exit; // exit if match 2725\n\n"
                    )
                } else if (CstringOps.strlenC(CompilerState.forv) != 0 &&
                    CompilerState.for_fcn_flg2 != 0
                ) {
                    // declared or regular variable?
                    if (Stubs.cvar(CstringOps.cstring(CompilerState.forv)) != 0) {
                        // declared
                        CompilerState.emit(
                            "\tstrcpy(" + CstringOps.cstring(CompilerState.forv) +
                                ", CSTR " + extract2pd() +
                                "); // set loop dcl var " + DOLLAR_FC + " case\n"
                        )

                        CompilerState.emit(
                            "\tif (strcmp( (char *) " + extract2pd() +
                                ", \"0\") == 0 || strlen( (const char *) " +
                                extract2pd() + ") == 0 ) \n" +
                                "\t\tgoto For" + CompilerState.forx +
                                "Exit; // exit if match 2743\n"
                        )
                    } else {
                        // regular
                        CompilerState.emit(
                            "\tsym_(SYMSTORE, (UNS) \"" +
                                CstringOps.cstring(CompilerState.forv) +
                                "\", (UNS) " + extract2pd() +
                                ", svPtr); // set loop variable m2752\n"
                        )

                        CompilerState.emit(
                            "\tif (strlen( (const char *) " + extract2pd() +
                                ") == 0 ) goto For" + CompilerState.forx +
                                "Exit; // exit if empty 2758\n"
                        )
                    }
                } else {
                    CompilerState.emit(
                        "\tif (strcmp( for" + CompilerState.forx + "_incr, \"0\")==0 || " +
                            "strlen(for" + CompilerState.forx + "_incr)==0 ) goto For" +
                            CompilerState.forx + "Exit; // exit if match 2767\n"
                    )
                }

                CompilerState.emit(
                    "\tif ( setjmp( for" + CompilerState.forx + "J ) == 0 ) goto For" +
                        CompilerState.forx + "; \n"
                )

                // order optimization hack comment block of the
                // vendor (fossil ledger, kept by the m2771 emit
                // that follows it in the vendor source)
                CompilerState.emit(
                    "\tgoto For_Group" + CompilerState.forx + "; // m2771\n"
                )
            }

            if (CompilerState.for_fcn_flg == 0) {
                // singles:
                if (CompilerState.for_mult >= 0) {
                    CompilerState.emit(
                        "\t\tif ( (_i = setjmp(for" + CompilerState.for_mult +
                            "J)) == 0) goto For" + CompilerState.for_mult +
                            "; // else continue\n"
                    )

                    // f264 quirk: the second line prints forx, not
                    // for_mult (vendor verbatim)
                    CompilerState.emit(
                        "\telse if (_i == 100) goto For" + CompilerState.forx +
                            "Exit; // f264\n\n"
                    )
                } else {
                    CompilerState.emit(
                        "\t\tif ((_i = setjmp(for" + CompilerState.forx +
                            "J)) == 0) goto For" + CompilerState.forx +
                            "; // else continue\n\n"
                    )

                    CompilerState.emit(
                        "\telse if (_i == 100) goto For" + CompilerState.forx +
                            "Exit; // f273\n\n"
                    )
                }

                if (CompilerState.xd[CompilerState.xpxIdx()] == ',') {
                    CompilerState.xpxInc()
                    CompilerState.for_mult = CompilerState.forx
                    continue@forAgain2 // goto for_again2;

                    // the vendor's dead post-goto SinglesFlg check
                    // is H-013 ledgered in the banner, not rendered
                }
            }

            if (CompilerState.xd[CompilerState.xpxIdx()] == ' ' ||
                CompilerState.xd[CompilerState.xpxIdx()] == NUL ||
                CompilerState.xd[CompilerState.xpxIdx()] == '\t'
            ) {
                if (CompilerState.xd[CompilerState.xpxIdx() + 1] != '{') {
                    CompilerState.IF_flg++
                }

                if (CompilerState.for_mult >= 0) {
                    CompilerState.ForBlocks[CompilerState.IF_flg] =
                        CompilerState.for_mult.toShort()

                    CompilerState.emit(
                        "\tgoto For" + CompilerState.for_mult +
                            "Exit; // done: bypass block \n\n"
                    )

                    CompilerState.emit(
                        "For" + CompilerState.for_mult +
                            ": // for loop entry point A\n\n"
                    )

                    CompilerState.ForBlocks[CompilerState.IF_flg] =
                        CompilerState.forx.toShort()
                    CompilerState.Blocks[CompilerState.IF_flg] = FOR_FC
                } else {
                    CompilerState.emit(
                        "\tgoto For" + CompilerState.forx +
                            "Exit; // bypass block \n\n"
                    )

                    CompilerState.emit(
                        "For" + CompilerState.forx +
                            ": // for loop body entry point B\n\n"
                    )

                    CompilerState.ForBlocks[CompilerState.IF_flg] =
                        CompilerState.forx.toShort()
                    CompilerState.Blocks[CompilerState.IF_flg] = FOR_FC
                }

                CompilerState.emit(
                    "\tdo { // for loop body\n\t\tsvPtr->LineNumber = " +
                        CompilerState.lines + "; \n"
                )

                return 0 // goto next_cmnd;
            }

            return ARG_LIST_ERROR_FC // goto arg_list_error;
        }
    }
}

// Extract2(&pd1[sdlim]) - the arm delegate (the two-argument arm
// signature over the pd1 window).
private fun extract2pd(): String =
    CstringOps.cstring(Stubs.Extract2(CompilerState.pd1, CompilerState.sdlim))
