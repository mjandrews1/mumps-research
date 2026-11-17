package mumps.compiler

import java.io.IOException

/**
 * PRO-arm translation of compiler-doty.c::doty (B4 unit 100,
 * rank 245). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 36-77.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-doty.c::doty, c, loc 27, comment_ratio 0.1,
 * pointer_marks 22, control_marks 7, score 57.3, batch 4 - exact
 * match to wp1/study-subset/units.csv row 245. Single definition
 * in the vendor (the file's LevCheck is defined in
 * compiler-mumps.c, not here).
 *
 * doty(j, ...): the dotted-indent (DO-block) tracker of the Mumps
 * compiler - when the source line indents to j-1 levels it opens a
 * new do-block (Efor longjmp scaffold emitted), and when the level
 * un-indends past j it closes blocks, emitting the
 * longjmp(EforNJ,99)/Do_Block_BypassN scaffold.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param : Out is the java.io.Writer on
 *    CompilerState (fprintf(Out,...) -> st.out.write).
 *  - h002 simple_output_macro: the two console printf guards ->
 *    System.out.print.
 *  - h006 out_param_holder : the four short* out-parameters
 *    (ExecLevel, ExecName, pd1Last, ExecX) become typed
 *    single-element ShortArray holders read/written at [0]; the
 *    three array parameters (DoRet, ExecStack, ExecSym) are
 *    reference-passed arrays as in C.
 *  - h009 format_string   : the fprintf "%d" emissions ride Kotlin
 *    string interpolation over the short domain (explicit h010
 *    widening to Int for the arithmetic).
 *  - h010 numeric_promotion: every short arithmetic
 *    (increment/decrement, index, subtraction) is widened
 *    explicitly; results narrowed back with .toShort() at the
 *    holder writes.
 *  - h014 out_of_scope_call: LevCheck (compiler-mumps.c:1949, not
 *    a corpus unit) -> Stubs.kt owner-named stub raised by this
 *    unit.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level doty - no collisions; the
 *    emitted vendor C text contains mid-string block-comment
 *    closers ("dotted un-indent ... star-slash", "dotted indent
 *    entry") - accepted vendor text per the NewCode precedent.
 *  - h017 naming          : doty keeps the vendor name (no
 *    snake-case in the identifier).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - ExecStack[(*ExecX)++] = (*ExecName)++ (vendor line 59) stores
 *    the pre-increment ExecName at the pre-increment ExecX and
 *    then advances both - kept exactly.
 *  - The un-indent loop's fprintf passes *ExecX - 1 (the value
 *    before the loop's own decrement) while the longjmp line uses
 *    the already-decremented stack top - kept exactly.
 *  - The Bypass emission passes ExecStack[*ExecX] twice with a
 *    single conversion (the second argument is unused by the
 *    format) - kept as the single interpolated value.
 *  - The vendor includes mumpsc/compiler-globals-ext.h only for
 *    the Out stream and the prototypes - carried by CompilerState
 *    and the Stubs (h004 no new file-scope globals in this unit).
 */
@Throws(IOException::class)
fun doty(j: Int, ExecLevel: ShortArray, ExecName: ShortArray, DoRet: ShortArray,
         DoRetX: Short, pd1Last: ShortArray, pd1cur: Short, ExecStack: ShortArray,
         ExecX: ShortArray, ExecSym: ShortArray): Int {
    val st = CompilerState

    if (ExecLevel[0].toInt() == j - 1) { // indenting

        if (levCheck(ExecName[0], DoRetX, DoRet) != 0) {

            System.out.print("\n*** Indentation without preceeding Do\n")
            pd1Last[0] = pd1cur
            return 99
        }

        st.out.write("\tgoto Do_Block_Bypass" + ExecName[0] + "; // C3551 bypass indented group\n")

        st.out.write("\n//-------------------------------------\n")
        st.out.write("Do_Block_Entry" + ExecName[0] + ":; // Enter Do Block\n")
        st.out.write("//-------------------------------------\n\n")
        ExecStack[ExecX[0].toInt()] = ExecName[0]
        ExecX[0] = (ExecX[0].toInt() + 1).toShort()
        ExecName[0] = (ExecName[0].toInt() + 1).toShort()
        st.out.write("\tdo { /* dotted indent entry */\n")
        ExecLevel[0] = j.toShort()
    } else if (ExecLevel[0].toInt() > j) { // un-indenting
        while (ExecLevel[0].toInt() > j) {
            st.out.write("\t} while (0); /* dotted un-indent " + (ExecX[0].toInt() - 1) + " */\n")
            if (ExecSym[ExecX[0].toInt()].toInt() != 0) {
                ExecSym[ExecX[0].toInt()] = 0
            }

            ExecX[0] = (ExecX[0].toInt() - 1).toShort()
            st.out.write("\tlongjmp(Efor" + ExecStack[ExecX[0].toInt()] + "J,99); /* return */\n")
            st.out.write("\n\tDo_Block_Bypass" + ExecStack[ExecX[0].toInt()] + ": ; // D3607 \n")
            ExecLevel[0] = (ExecLevel[0].toInt() - 1).toShort()
        }

        ExecLevel[0] = j.toShort()
    }
    return 0
}
