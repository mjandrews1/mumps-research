package doc

// Unit 54: compiler-if.c::if_code (batch 2, complex stratum). The
// IF command compiler: no-argument form, at-test indirection form,
// and the parse_/numcomp2 shape, with the if_again/if_structure goto
// pair linearized per cookbook H-011 (its own example: row 10) -
// backward goto if_again -> while(true), forward goto if_structure
// -> atStructure flag fallthrough. The INTERPRET macro
// (compiler-macros.h:39, five sequential emissions reading Out and
// tmp) is the cookbook H-003 stateful-macro example itself: member
// function CompilerState.interpret(tmp). The emitted C text carries
// the dollar byte (the Test and test dollar-byte literals); the bytes are escaped per
// cookbook H-015 via DOLLAR, keeping the emission byte-identical.
// atTest is the batch-4 owner compiler-at-test.c::atTest (units.csv
// row 109, batch 4); parse_ is the batch-3 owner
// interp-parse.cpp::parse_; Extract2 the batch-1 owner
// compiler-parse.c::Extract2; atCopy, numcomp2 and linepad are not
// roster units - all H-014 stubs. pd1Last is written by atTest
// through the H-006 holder (ShortArray(1), the atTest out-parameter
// contract of compiler-globals.h:103). PD1 uses the LIVE sysparms
// sizing (PS 32000 / PS1 32001; the memsize.h 4096 twins are the
// vendor FIXME mismatch, H-018 roster note). short externs are
// widened explicitly to Int per H-010. Naming per H-017 (ifCode).

private const val ABRTX = 6 // compiler-macros.h:74
private const val PARSE_ERROR = 1 // compiler-macros.h:69
private const val DOLLAR = '$'

fun ifCode(): Int {
    var atStructure = false

    while (true) {
        if (!atStructure) {
            if (CompilerState.xd[CompilerState.xpxIdx()] == ' ' ||
                CompilerState.xd[CompilerState.xpxIdx()] == NUL) {
                // no argument form
                CompilerState.xpxInc()
                CompilerState.Out.write("\tif (svPtr->tpx) {\n")
                CompilerState.IF_flg++
                return 0 // goto next_cmnd;
            }

            CompilerState.at_flg = Stubs.atTest(
                CompilerState.xd, CompilerState.pd1, CompilerState.pd1Last,
                CompilerState.lines, CompilerState.xpx
            )

            if (CompilerState.at_flg == -1) return ABRTX // goto abrtx;

            if (CompilerState.at_flg != 0) {
                val tmp = CharArray(1024)

                CstringOps.strcpyS(tmp, "i ")
                Stubs.atCopy(CompilerState.at_flg, tmp)

                CompilerState.interpret(CstringOps.cstring(tmp))

                CompilerState.xpx = CompilerState.at_flg.toLong()

                if (CompilerState.POST_flg != 0) {
                    CompilerState.Out.write("\t} /* post conditional 1856*/\n")
                    CompilerState.POST_flg = 0
                }

                val nEmit = CompilerState.emit("\tif (svPtr->tpx) {")
                Stubs.linepad(nEmit, "Test if expression true")

                atStructure = true // goto if_structure;
            }
        }

        if (!atStructure) {
            // if_again:
            CompilerState.t2 = 1
            CompilerState.ierr = Stubs.parse_()
            if (CompilerState.ierr != 0) return PARSE_ERROR // goto parse_err;

            CompilerState.Out.write(
                "\tsvPtr->tpx = 0; //Default " + DOLLAR + "Test indicator\n"
            )

            // vendor commented fossil (lines 81-83), kept verbatim:
            //   fprintf (Out, "\tif (numcomp((UNS) %s, "
            //                     "(UNS) \"0\")) { // Test if true \n",
            //                     Extract2(&pd1[sdlim]));

            CompilerState.Out.write(
                "\tif (numcomp2((UNS) " +
                        CstringOps.cstring(Stubs.Extract2(CompilerState.pd1, CompilerState.sdlim)) +
                        " )) { // Test if true \n"
            )

            CompilerState.Out.write(
                "\tsvPtr->tpx = 1; // " + DOLLAR + "test becomes true\n"
            )

            atStructure = true // if_structure:
            CompilerState.tpx = 1
        } else {
            CompilerState.tpx = 1
        }

        if (CompilerState.xd[CompilerState.xpxIdx() + 1] != '{') CompilerState.IF_flg++

        if (CompilerState.IF_flg > 0) {
            CompilerState.ForBlocks[CompilerState.IF_flg] =
                CompilerState.ForBlocks[CompilerState.IF_flg - 1]
        }

        CompilerState.Blocks[CompilerState.IF_flg] = IF_OPCODE

        if (CompilerState.xd[CompilerState.xpxIdx()] == ',') {
            CompilerState.xpxInc()
            atStructure = false
            continue // goto if_again;
        }

        return 0 // goto next_cmnd;
    }
}
