package doc

// Unit 59: compiler-sys1.c::inout (batch 2, complex stratum; row 36:
// loc 44, ptr 40, ctrl 9, score 102.0, rank 273). The compiler-side
// output-priming emission switch: new page, skip line, tab, and text
// print. Emitted C text is byte-identical to the vendor (escaped per
// cookbook H-015); every Extract2 call rides the batch stub
// (H-014, owner compiler-parse.c::Extract2). The vendor's case-3 DEAD
// BLOCK after the return (the eight fprintf emissions of the old
// hand-rolled tab implementation) is unreachable per H-013 and kept
// as the comment ledger below. Out is the CompilerState Writer
// (H-001 carrier).

fun inout(text: CharArray, cmnd: Short, col: CharArray) {
    when (cmnd) {
        1.toShort() -> { // new page
            CompilerState.Out.write("\t fprintf(svPtr->out_file[svPtr->io],\"\\f\");")
            CompilerState.Out.write("svPtr->hor[svPtr->io]=1; svPtr->ver[svPtr->io]=1;\n")
            return
        }
        2.toShort() -> { // skip line
            CompilerState.Out.write("\tfprintf(svPtr->out_file[svPtr->io], \"\\n\"); \n")
            CompilerState.Out.write("\tsvPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; \n")
            return
        }
        3.toShort() -> { // tab
            CompilerState.Out.write(
                "\t _Column(&svPtr->hor[svPtr->io], &svPtr->ver[svPtr->io], \n" +
                        CstringOps.cstring(Stubs.Extract2(col, 0)) +
                        ", svPtr->out_file[svPtr->io]); // tab operator\n"
            )
            return

            // VENDOR DEAD BLOCK (unreachable after the return, H-013,
            // kept as comment): the old tab implementation:
            //   fprintf (Out, "\t if (svPtr->hor[svPtr->io] > atoi((const char *) %s)) {\n", Extract2(col));
            //   ... eight emission lines, then return;
        }
        4.toShort() -> { // text print
            CompilerState.Out.write(
                "\tsvPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], \"%s\", \n" +
                        "\t\t" + CstringOps.cstring(Stubs.Extract2(text, 0)) + ");\n"
            )
            return
        }
    }
}
