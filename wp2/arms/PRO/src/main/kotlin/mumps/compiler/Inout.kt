package mumps.compiler

import java.io.IOException

/**
 * PRO-arm translation of compiler-sys1.c::inout (pilot B2, unit 59,
 * provisional under deviation C-003). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 292-348 (units.csv:
 * 44 LOC, score 102.0, rank 273).
 *
 * The compiler's device-control emitter: writes the C glue for the
 * Mumps device operators (new page, skip line, tab, text print) to
 * the compiler output stream Out.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 Writer            : fprintf(Out, ...) emissions map to
 *    CompilerState.out.write (compiler-globals-ext.h:106 FILE* Out;
 *    the MyFunc precedent), one write per vendor fprintf, same
 *    escape-resolved strings.
 *  - h011 control_flow      : the cmnd switch renders as a Kotlin
 *    when, arms kept in the vendor order.
 *  - h013 dead_branches     : the vendor case 3 falls through a
 *    return - the seven fprintf calls of the legacy if/else tab
 *    expansion after it are unreachable dead code and are dropped
 *    with this note (kept in the vendor only as fossil text).
 *  - h014 out_of_scope_call : the local Extract2 prototype resolves
 *    to the real B1 translation (compiler-parse.c::Extract2, unit
 *    15) - a reused unmodified dependency, no stub.
 *  - h017 naming            : inout keeps its name.
 */

@Throws(IOException::class)
fun inout(text: CharArray, cmnd: Short, col: CharArray) {

    when (cmnd.toInt()) {

        1 -> { // new page
            CompilerState.out.write("\t fprintf(svPtr->out_file[svPtr->io],\"\\f\");")
            CompilerState.out.write("svPtr->hor[svPtr->io]=1; svPtr->ver[svPtr->io]=1;\n")
            return
        }

        2 -> { // skip line
            CompilerState.out.write("\tfprintf(svPtr->out_file[svPtr->io], \"\\n\"); \n")
            CompilerState.out.write("\tsvPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; \n")
            return
        }

        3 -> { // tab
            CompilerState.out.write("\t _Column(&svPtr->hor[svPtr->io], &svPtr->ver[svPtr->io], \n")
            CompilerState.out.write(Extract2(col) + ", svPtr->out_file[svPtr->io]); // tab operator\n")
            return
            // h013: the vendor's unreachable legacy tab expansion
            // (seven further fprintf calls building the old
            // if/else _i padding loops) is dead code after this
            // return and is dropped.
        }

        4 -> { // text print
            CompilerState.out.write("\tsvPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], \"%s\", \n")
            CompilerState.out.write("\t\t" + Extract2(text) + ");\n")
            return
        }
    }
}
