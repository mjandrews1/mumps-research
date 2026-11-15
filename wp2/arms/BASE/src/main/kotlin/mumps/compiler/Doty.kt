package mumps.compiler

/**
 * Kotlin translation of compiler-doty.c::doty (Study Subset unit 36,
 * Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: int doty(int j, short *ExecLevel, short *ExecName, short DoRet[],
 * short DoRetX, short *pd1Last, short pd1cur, short ExecStack[],
 * short *ExecX, short ExecSym[]) (compiler-doty.c:37-76) — handles
 * Mumps dot-indentation: entering a dotted block emits a Do-block
 * bypass/entry, leaving un-indents and emits the bypass return.
 *
 * POINTER-PARAM COLLAPSE: the C passes its own file-scope globals
 * (ExecLevel, ExecName, DoRet, DoRetX, pd1Last, ExecStack, ExecX,
 * ExecSym — compiler-globals-ext.h:127-133) BY POINTER into doty.
 * Kotlin owns them in CompilerState (added by this unit), so the
 * signature collapses to the two by-value params: doty(j, pd1cur).
 *
 * QUIRKS PRESERVED: the diagnostic typo "preceeding" is carried
 * verbatim (faithful-bugs policy); fprintf calls passing an unused
 * extra argument (Do_Block_Bypass%d with ExecStack[*ExecX] twice) are
 * collapsed to the single substituted value.
 */
fun doty(j: Int, pd1cur: Short): Int {
    val S = CompilerState

    if (S.ExecLevel.toInt() == j - 1) { // C: if (*ExecLevel == j-1) { // indenting
        if (levCheck(S.ExecName, S.DoRetX, S.DoRet) != 0) {
            print("\n*** Indentation without preceeding Do\n") // typo verbatim (C)
            S.pd1Last = pd1cur
            return 99
        }

        val out = S.Out
        out.write("\tgoto Do_Block_Bypass" + S.ExecName + "; // C3551 bypass indented group\n")
        out.write("\n//-------------------------------------\n")
        out.write("Do_Block_Entry" + S.ExecName + ":; // Enter Do Block\n")
        out.write("\n//-------------------------------------\n\n")

        // C: ExecStack[(*ExecX)++] = (*ExecName)++;
        S.ExecStack[S.ExecX.toInt()] = S.ExecName
        S.ExecX = (S.ExecX + 1).toShort()
        S.ExecName = (S.ExecName + 1).toShort()

        out.write("do { /* dotted indent entry */\n")
        S.ExecLevel = j.toShort()
    } else if (S.ExecLevel > j) { // C: else if (*ExecLevel > j) { // un-indenting
        while (S.ExecLevel > j) {
            S.Out.write("\t} while (0); /* dotted un-indent " + (S.ExecX - 1) + " */\n")
            if (S.ExecSym[S.ExecX.toInt()].toInt() != 0) { // C: if (ExecSym[*ExecX]) { ... =0; }
                S.ExecSym[S.ExecX.toInt()] = 0
            }

            // C: fprintf(Out, "	longjmp(Efor%dJ,99); /* return */\n", ExecStack[--(*ExecX)]);
            S.ExecX = (S.ExecX - 1).toShort()
            S.Out.write("\tlongjmp(Efor" + S.ExecStack[S.ExecX.toInt()] + "J,99); /* return */\n")

            // C: fprintf(Out, "\n\tDo_Block_Bypass%d: ; // D3607 \n", ExecStack[*ExecX], ...)
            S.Out.write("\n\tDo_Block_Bypass" + S.ExecStack[S.ExecX.toInt()] + ": ; // D3607 \n")

            S.ExecLevel = (S.ExecLevel - 1).toShort()
        }
        S.ExecLevel = j.toShort()
    }

    return 0
}
