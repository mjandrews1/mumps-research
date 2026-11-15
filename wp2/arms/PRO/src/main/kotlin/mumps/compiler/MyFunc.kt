package mumps.compiler

import java.io.IOException

/**
 * PRO-arm translation of compiler-sys1.c::MyFunc (pilot B1, unit 27).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 818-945 (units.csv: 89 SLOC).
 *
 * MyFunc(v1d, iiitmp): compiler back-end for extrinsic function calls
 * (dollar-dollar NAME(args)): extracts the function name and up to 30 args
 * from v1d, emits the strmove __ArgN setup code per argument (via
 * Extract1 for builtins, Extract2 otherwise), then emits either the
 * local-label jump (goto [ _]NAME / setjmp _doStack) or the
 * Interpret("x \"do ^NAME\"") indirection for ^-references. Returns 0,
 * or 9002 (invalid label chars) / 9003 (no ^ where one expected).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param     : all fprintf(Out, ...) emissions go
 *    through CompilerState.out.
 *  - h002 simple_output_macro : printf -> System.out.print string
 *    building; strcpy/strcmp/strncmp/strlen -> strcpyC/strncmpC/
 *    strlenC/cstringAt + local shift helper; isdigit/isalnum ->
 *    local C-locale helpers.
 *  - h004 globals_ext        : extern short ArgMax added to
 *    CompilerState (compiler-globals-ext.h:53); extern short ExitCode
 *    (:107) and extern int lines (:118) already carried.
 *  - h012 unsigned_byte_domain: v1d/bd/name/lbl byte domains.
 *  - h013 dead_store         : int j is stored (v1d[i] at the argument
 *    split) but never read - dropped with this note; int l is never
 *    used at all - dropped; the local char bd[1024] receives
 *    strcpy(bd,"gtmp") and is never read again - dropped.
 *  - h014 out_of_scope_call  : Extract1 (compiler-parse.c:2119, not a
 *    corpus unit) -> Stubs.kt stub added with this unit; Extract2
 *    resolved to the B1 unit 15 translation.
 *  - h015 grammar_collision  : pre-push scan incl. cross-file top-level
 *    names (new top-level: MyFunc + private helpers - no collisions);
 *    the mumpsc/builtinTable.h include inside the vendor function
 *    becomes the private BUILT_IN array (18 entries, verbatim).
 *  - h017 naming             : MyFunc keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor's "Too Many Extrinsic Function Arguments" branch sets
 *    k = 9 and then STILL processes the current argument (no break);
 *    the port keeps the fall-through.
 *  - The local-DO emission passes a stray k argument to a format with
 *    no conversion (ignored by fprintf); the port omits the dead
 *    argument silently-equivalently.
 *  - lbl starts as the two-char cstring "" (quote quote).
 *  - The vendor parameter v1d shadows the file-scope v1d; the port
 *    keeps the parameter (localChar carrier) with the vendor name.
 */
@Throws(IOException::class)
fun MyFunc(v1d: CharArray, iiitmp: Int): Int {
    val st = CompilerState
    val name = CharArray(128)
    val lbl = CharArray(128)
    lbl[0] = '"'
    lbl[1] = '"'
    lbl[2] = NUL // vendor: char lbl[128]="\"\""

    // static const char * BuiltIn[] from <mumpsc/builtinTable.h>
    val builtIn = arrayOf(
        "^Cosine", "^Avg", "^Count", "^Max", "^Min", "^Multiply",
        "^Sum", "^Transpose", "^IDF", "^TermCorrelate", "^DocCorrelate",
        "^Sim1", "^Dice", "^Jaccard", "^Centroid", "^Scan", "^ScanAlnum",
        "^Input",
    )

    // for (i = 0; v1d[i] != 0 && v1d[i] < 127 && v1d[i] != ' '; i++) name[i] = v1d[i];
    var i = 0
    while (v1d[i] != NUL && v1d[i].code < 127 && v1d[i] != ' ') {
        name[i] = v1d[i]
        i++
    }

    if (v1d[i] != NUL) shiftLeft(v1d, i + 1)
    else v1d[0] = NUL

    name[i] = NUL

    // check if fcn is known builtin function against table of names
    var bFlg = 0
    val nameStr = cstringAt(name)
    var j = 0
    while (j < builtIn.size && builtIn[j].isNotEmpty()) {
        if (nameStr == builtIn[j]) {
            bFlg = 1
            break
        }
        j++
    }

    var k = 0

    // find arguments
    while (strlenC(v1d) > 0) {

        if (k > 29) {
            System.out.print("*** Too Many Extrinsic Function Arguments")
            System.out.print(" in or near line " + st.lines + "\n")
            st.ExitCode = 99
            k = 9 // vendor: falls through and still processes this arg
        }

        i = 0
        while (v1d[i] != NUL && v1d[i].code < 127) i++
        // vendor: j = v1d[i]; - stored, never read (h013, dropped)
        v1d[i] = NUL

        if (bFlg == 1 && strncmpC(v1d, 0, "#S#", 3) != 0)
            st.out.write(
                "\t strmove((unsigned char *) __Arg" + k +
                    ", (unsigned char *) " + Extract1(v1d) + "); // arg setup D1002\n"
            )
        else
            st.out.write(
                "\t strmove((unsigned char *) __Arg" + k +
                    ", (unsigned char *) " + Extract2(v1d) + "); // arg setup D1003\n"
            )

        shiftLeft(v1d, i + 1)
        k++
    }

    if (k > st.ArgMax) st.ArgMax = k.toShort()

    // vendor: strcpy(bd, "gtmp"); - bd never read afterwards (h013, dropped)
    st.out.write("\t _ArgCount=" + k + ";\n")

    i = 0
    while (name[i] != NUL) {
        if (name[i] == '^') break
        i++
    }

    // find start of external name
    if (name[i] != '^') { // a local reference DO

        st.out.write("\t _doStx++;\n")
        st.out.write(
            "\t if (_doStx > 127 ) ErrorMessage(\"Too many function calls\"," +
                "svPtr->LineNumber);\n"
        )

        st.out.write("\t sym_(99, (unsigned char *) \"\", (unsigned char *) \"\", svPtr); \n")

        if (isdigitC(name[0]))
            st.out.write("\t if (setjmp(_doStack[_doStx])==0) goto _" + cstringAt(name) + ";")
        else
            st.out.write("\t if (setjmp(_doStack[_doStx])==0) goto " + cstringAt(name) + ";")
        st.out.write("  /* Jump to function */\n")

        st.out.write("\t _doStx--; \n")

        if (iiitmp >= 0)
            st.out.write(
                "\t strmove((unsigned char *) tmp" + iiitmp +
                    ",(unsigned char *) _ReturnVal); // Copy result\n"
            )
        return 0
    }

    /* -----------------------------------------------*/
    /* a non-local reference DO - has one or more ^   */
    /* -----------------------------------------------*/

    if (i != 0) { // external name contains label
        name[i] = NUL
        lbl[0] = '"'
        lbl[1] = NUL
        strcatC(lbl, cstringAt(name))
        strcatC(lbl, "\"")
        name[i] = '^'
        shiftLeft(name, i) // strcpy(name, &name[i]) - same buffer shift
    }

    if (name[0] == '^' && name[1] == '^') { // double ^

        st.out.write(
            "\t_jj=Interpret(\"x \\\"do ^" + cstringAt(name, 1) +
                "\\\"\",svPtr);\n"
        )
        st.out.write("\tsvPtr->ERROR=_jj;\n")
        st.out.write(
            "\tif (!svPtr->NOERR && _jj<0) " +
                "ErrorMessage(\"Indirection error\",svPtr->LineNumber);"
        )
        return 0
    }

    if (name[0] == '^') { // single ^

        i = 0
        while (lbl[i] != NUL) { // valid LLL:  LLL^^RRR ?
            if (isalnumC(lbl[i])) {
                i++
                continue
            }
            if (lbl[i] == '%') {
                i++
                continue
            }
            if (lbl[i] == '"') {
                i++
                continue
            }
            return 9002
        }

        st.out.write(
            "\t_jj=Interpret(\"x \\\"do ^" + cstringAt(name, 1) +
                "\\\"\",svPtr);\n"
        )
        st.out.write("\tsvPtr->ERROR=_jj;\n")
        st.out.write(
            "\tif (!svPtr->NOERR && _jj<0) " +
                "ErrorMessage(\"Indirection error\",svPtr->LineNumber);"
        )
        return 0
    }

    return 9003
}

/** C strcpy(buf, &buf[from]) - in-place shift of the remainder. */
private fun shiftLeft(buf: CharArray, from: Int) {
    var t = 0
    var f = from
    while (true) {
        buf[t] = buf[f]
        if (buf[f] == NUL) break
        t++
        f++
    }
}

/** C-locale isdigit. */
private fun isdigitC(c: Char): Boolean = c in '0'..'9'

/** C-locale isalnum. */
private fun isalnumC(c: Char): Boolean =
    (c in '0'..'9') || (c in 'a'..'z') || (c in 'A'..'Z')
