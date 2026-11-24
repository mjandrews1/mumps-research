package daf

// Unit 117 (DAF B4 T3, complex): compiler-doty.c::doty (27 LOC): the
// dotted-line INDENT tracker of the compiler: opens Do Blocks on
// deeper indentation and closes them with the longjmp unwind emits on
// shallower indentation.
// Consults: BASE-log row 10 hit-adapt (no gotos here; the vendor FOR
// with the *ExecLevel-- third clause renders as the Kotlin for fold),
// rows 2/26 hit-reuse (fprintf(Out,...) -> DafCompilerState.emit with
// the emitted C text verbatim; note the vendor printf dispatch uses
// BOTH Out emits and one stdout printf for the diagnostic - stdout
// printf folds to Kotlin print per row 69/2), row 22 hit-reuse (the
// compiler TU globals ExecLevel/ExecName/ExecX/ExecStack/ExecSym/
// DoRet/DoRetX/pd1Last ride DafCompilerState; the short* out-params
// fold onto the state object exactly as the C caller passes the TU
// globals by address), row 81 (ExecStack[] reads/writes on the
// IntArray carrier; arr[E++] = v and arr[--E] folds kept verbatim),
// row 28 family not consulted (no atoi), row 99 return-fold.
// Seat: levCheckDaf (roster compiler-for.c::LevCheck b11) - the
// render calls the stub seat; the stub returns false, so the "***
// Indentation without preceeding Do" 99 path stays reachable only
// through the seat (stub-mediated honesty, per the T1/T2 stub
// discipline).
// Dafny: EXEMPT (emitter over TU state; journal row).

fun dotyDaf(j: Int): Int {

    val C = DafCompilerState

    if (C.ExecLevel == j - 1) { // indenting

        if (levCheckD(C.ExecName, C.DoRetX, C.DoRet)) {

            print("\n*** Indentation without preceeding Do\n")
            C.pd1Last = C.pd1cur // *pd1Last = pd1cur (caller TU global fold)
            return 99
        }

        C.emit("\tgoto Do_Block_Bypass${C.ExecName}; " +
                "// C3551 bypass indented group\n")
        C.emit("\n//-------------------------------------\n")
        C.emit("Do_Block_Entry${C.ExecName}:; // Enter Do Block\n")
        C.emit("//-------------------------------------\n\n")
        C.ExecStack[C.ExecX++] = C.ExecName++
        C.emit("\tdo { /* dotted indent entry */\n")
        C.ExecLevel = j
    } else if (C.ExecLevel > j) { // un-indenting
        while (C.ExecLevel > j) { // vendor FOR with the (*ExecLevel)-- clause
            C.emit("\t} while (0); /* dotted un-indent ${C.ExecX - 1} */\n")
            if (C.ExecSym[C.ExecX] != 0) {
                C.ExecSym[C.ExecX] = 0
            }
            C.emit("\tlongjmp(Efor${C.ExecStack[--C.ExecX]}J,99); " +
                    "/* return */\n")
            // the vendor format carries a second unused argument;
            // the emit text is identical (row 26 verbatim fold)
            C.emit("\n\tDo_Block_Bypass${C.ExecStack[C.ExecX]}: ; " +
                    "// D3607 \n")
            C.ExecLevel-- // the for-loop third clause
        }
        C.ExecLevel = j
    }
    return 0
}
