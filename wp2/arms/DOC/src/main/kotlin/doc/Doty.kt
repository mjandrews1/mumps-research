package doc

// Unit 117 (B4 tranche 3): compiler-doty.c::doty (batch 4, complex
// stratum; LOC 27, score 57.3, rank 245) - the dotted-Do block
// indent/un-indent manager of the compiler: on entering a dotted
// level it emits the block-bypass/entry scaffolding and pushes
// the Exec level; on un-indenting it emits the while-zero closers
// and the longjmp returns. The vendor comment fossils are kept
// verbatim (C3551, D3607, the INDENT mark strings are EMITTED C
// text). The short* out-parameters render as the H-006 holder
// arrays (ShortArray(1); pd1Last is the unit-54-54 holder
// precedent); the DoRet/ExecStack/ExecSym arrays are the vendor
// extern arrays passed by the caller (ExecSym is the unit-91
// CompilerState seat; all are passed in - no global rebinding).
// The stream emission of the file-scope externs rides
// CompilerState (H-004 one-owner). LevCheck is the NEW H-014 stub
// seat (owner compiler-mumps.c:1949, its body's owner file; not a
// roster unit). printf maps to System.out per H-001. Vendor
// quirks kept and noted: the vendor typo "preceeding" in the
// banner is byte-identical; the evaluation order of the vendor
// ExecStack[(*ExecX)++] = (*ExecName)++ double post-increment is
// split into explicit steps (the C sequencing maps to: index the
// stack at the old ExecX, then advance both - the ledger note
// here records the split, values identical); the un-indent
// Do_Block_Bypass emission passes ExecStack[*ExecX] TWICE but
// consumes only one percent-d (the extra argument is a vendor
// fossil, kept in the ledger); the H-013 note: the vendor label
// comments (indenting / un-indenting) kept. Naming per H-017.

fun doty(
    j: Int,
    ExecLevel: ShortArray,
    ExecName: ShortArray,
    DoRet: ShortArray,
    DoRetX: Short,
    pd1Last: ShortArray,
    pd1cur: Short,
    ExecStack: ShortArray,
    ExecX: ShortArray,
    ExecSym: ShortArray
): Int {
    if (ExecLevel[0] == (j - 1).toShort()) { // indenting

        if (Stubs.LevCheck(ExecName[0], DoRetX, DoRet) != 0) {
            System.out.print("\n*** Indentation without preceeding Do\n")
            pd1Last[0] = pd1cur
            return 99
        }

        CompilerState.emit(
            "\tgoto Do_Block_Bypass" + ExecName[0] +
            "; // C3551 bypass indented group\n"
        )
        CompilerState.emit("\n//-------------------------------------\n")
        CompilerState.emit("Do_Block_Entry" + ExecName[0] + ":; // Enter Do Block\n")
        CompilerState.emit("//-------------------------------------\n\n")

        // ExecStack[(*ExecX)++] = (*ExecName)++;  (split, see header)
        ExecStack[ExecX[0].toInt()] = ExecName[0]
        ExecX[0] = (ExecX[0] + 1).toShort()
        ExecName[0] = (ExecName[0] + 1).toShort()

        CompilerState.emit("do { /* dotted indent entry */\n")
        ExecLevel[0] = j.toShort()

    } else if (ExecLevel[0] > j) { // un-indenting

        while (ExecLevel[0] > j) {
            CompilerState.emit(
                "\t} while (0); /* dotted un-indent " +
                (ExecX[0] - 1) + " */\n"
            )
            if (ExecSym[ExecX[0].toInt()].toInt() != 0) {
                ExecSym[ExecX[0].toInt()] = 0
            }

            ExecX[0] = (ExecX[0] - 1).toShort()
            CompilerState.emit(
                "\tlongjmp(Efor" + ExecStack[ExecX[0].toInt()] +
                "J,99); /* return */\n"
            )
            // vendor passes ExecStack[*ExecX] twice, consumes once
            CompilerState.emit(
                "\n\tDo_Block_Bypass" + ExecStack[ExecX[0].toInt()] +
                ": ; // D3607 \n"
            )

            ExecLevel[0] = (ExecLevel[0] - 1).toShort()
        }

        ExecLevel[0] = j.toShort()
    }

    return 0
}
