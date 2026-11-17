package mumps.interp

/**
 * PRO-arm translation of mumps.cpp::main (B5 unit 130, rank 250).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 73-239 - the interpreter driver: argv SymPut
 * plumbing, the "./" program-name strip, the announce banner and
 * the readline REPL with its EOF/halt exits.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): mumps.cpp::main, cpp, loc 125, comment_ratio 0.023,
 * pointer_marks 10, control_marks 15, score 60.7 - exact match to
 * wp1/study-subset/units.csv row 129.
 *
 * PI DECISION (re-render): this unit overlaps the pilot B1 unit
 * 12 (mumps.cpp::<misc>, lines 40-239, MumpsMain.kt - pad,
 * SyncClock, set_alarm and the same main body). Per the PI's
 * tranche-A decision it is FULLY RE-RENDERED here as a new file,
 * an independent translation that does not consult the u12
 * render; Kotlin allows one main per package, so this seat takes
 * the distinct entry name main130 (h017 note; the u12 main
 * remains the JVM entry point). Divergences between the two
 * renders are expected and journaled, not reconciled:
 *  - this seat returns Int (the C "int main" contract) instead of
 *    terminating the process - it is not the JVM entry;
 *  - readline(prompt)/^D reads via kotlin readlnOrNull() after a
 *    print prompt (no System.in backtick access - scan gate);
 *  - the history analogs are file-private here (same shape as
 *    u12's, independently written).
 *
 * Reference-build configuration (component config.log line 7 -
 * authoritative for every #ifdef here):
 *   ./configure --with-sqlite --with-dbname=mumps --with-slice=0
 *     --with-hardware-math=yes --with-float-digits=8 --with-int-32
 *     ... --with-alarm=0
 * hence SQLITE defined (also #defined unconditionally at vendor
 * line 39); NATIVE/NATIVE_SHARED/CLIENT/READ_ONLY and BIGFLOAT
 * not defined; HAVE_HARDWARE_MATH defined.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h003 state_macro    : dollarSymPut -> the symPut shim; GlobalClose
 *    -> the globalClose shim (InterpStubs, h014 owners named).
 *  - h011 goto           : the "?" goto announce renders as a
 *    break to the outer re-announce loop; the vendor's single
 *    fall-through announce entry is the first outer pass.
 *  - h012 unsigned_byte_domain: tmp/tmp0/tmp1 program buffers
 *    Char(0..255).
 *  - h013 dead_store     : the static struct C list (S/p1/p2,
 *    vendor 76-79) is never used and the SIGALRM/set_alarm setup
 *    is behind if(0) (vendor 88-91, slice zero) - dropped with
 *    comments; the DirectRead forward declaration (vendor 132)
 *    has no call - dropped.
 *  - h014 out_of_scope_call: interpret/mglobal/symPut/globalClose
 *    (InterpStubs stubs, owners named); cleanLocks is DONE
 *    (LockMisc.kt) - direct call; strcpyC/strcatC/strncmpC/
 *    writeCstring/cstringAt real helpers reused unmodified.
 *  - h015 grammar_collision: pre-push scan incl. cross-file
 *    top-level names: main130 plus the private history analogs -
 *    the file-private seats repeat u12's private helper names
 *    legally (per-file private convention); no public
 *    collisions.
 *  - h017 naming         : main -> main130 (distinct entry seat,
 *    see PI decision above); everything else keeps vendor shape.
 *  - h018 preprocessor_conditional: the #define database/
 *    floatSize/Ints/FltDigits/Cache/BlkSiz/Fpbits block (vendor
 *    96-131) resolves per the reference build: database =
 *    "; DBMS: SQLITE; Memory Resident: 0", floatSize =
 *    "; Float: double" (BIGFLOAT out), Fpbits = "; FP Bits: N/A"
 *    (HAVE_HARDWARE_MATH in) - the excluded arms (NATIVE,
 *    NATIVE_SHARED/CLIENT/READ_ONLY, BIGFLOAT, Fpbits:72) are
 *    documented here and not translated; the REPL's
 *    #ifdef NATIVE_SHARED btree(CLOSE,...) deadlock guard is
 *    compiled out (SQLITE backend) - comment only.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - __TIME__/__DATE__ are build-machine constants; placeholders
 *    (BUILD_TIME/BUILD_DATE below) stand in - the announce line
 *    differs from the reference binary in exactly that field
 *    (u12 flagged the same divergence).
 *  - GNU readline/history: readlnOrNull() after print("> ") - no
 *    completion/editing; history kept in memory and persisted by
 *    the analogs (u12 diverged the same way).
 *  - argv mapping: the JVM hands the runtime args (no program
 *    name), so args[0] is the argv[1] analog (u12 convention);
 *    argv[0] maps to the "mumps" default when args is empty.
 *  - The exit path's free(line)/puts("\n") order and the
 *    Mltoa(getpid(),tmp1) byte shape (ASCII pid into tmp1) are
 *    kept - cleanLocks reads the C-string back out.
 */

private const val STR_MAX = 32000 // strmax.h:3 (file-private, cf. u12)
private const val CLOSE = 4      // defines.h:87 (file-private, cf. u12)

/** Reference-build placeholders for the C __TIME__/__DATE__ macros. */
private const val BUILD_TIME = "00:00:00"
private const val BUILD_DATE = "Jan  1 2026"

/**
 * vendor 73-239: int main(int argc, char **argv) - the
 * interpreter driver, re-rendered as the unit-130 seat (see
 * header). Returns the C exit code (0 on the REPL exits, the
 * Interpret retcode on the direct-program arm).
 */
fun main130(args: Array<String>): Int {
    // h013: vendor 76-79 - static struct C list (S, p1, p2),
    // declared and never used - dropped, not "fixed".
    // h013: vendor 88-91 - if (0) { signal(SIGALRM, SyncClock);
    // set_alarm(1); } - the alarm block is compiled dead (slice
    // zero, --with-alarm=0) - dropped.

    val svPtr = InterpState.svPtr
    val tmp = CharArray(STR_MAX)
    val tmp0 = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)

    // h018: vendor 96-131 preprocessor block resolved per the
    // reference build - SQLITE in (also #defined unconditionally
    // at line 39), NATIVE*/CLIENT/READ_ONLY/BIGFLOAT out,
    // HAVE_HARDWARE_MATH in; the spliced announce macros are
    // inlined at the interpret call below.

    // h013: vendor 132 - int DirectRead(int, unsigned char *)
    // forward-declared, never called - dropped.

    if (args.isNotEmpty()) { // program name (argv[1] analog)
        // strncpy((char *)svPtr->program, argv[1],512);
        val name = args[0]
        for (k in name.indices.take(512)) svPtr.program[k] = name[k]
    } else {
        // strncpy((char *)svPtr->program, argv[0], 512);
        val name = "mumps" // argv[0] analog when no argument given
        for (k in name.indices.take(512)) svPtr.program[k] = name[k]
    }

    if (args.size >= 1 + 1) { // argc>=2
        symPut(svPtr, "%", args[0]) // dollarSymPut("%",argv[1])
    } else {
        symPut(svPtr, "%", "") // dollarSymPut("%","")
    }

    if (args.size > 1 + 1) { // argc>2
        val t = CharArray(STR_MAX)

        for (i in 2 until args.size + 1) { // for (int i=2; i<argc; i++)
            // sprintf(t,"%%%d",i-1); dollarSymPut(t,argv[i]);
            writeCstring(t, "%" + (i - 1))
            symPut(svPtr, cstringAt(t), args[i])
        }

        // sprintf(t,"%d",argc-2); dollarSymPut((char *)"%c",t);
        writeCstring(t, (args.size - 2).toString())
        symPut(svPtr, "%c", cstringAt(t))
        symPut(svPtr, "%0", args[0]) // dollarSymPut("%0",argv[1])
    }

    if (args.isNotEmpty() && args[0].isNotEmpty()) {
        // strcpy((char *) tmp,argv[1]);
        strcpyC(tmp, args[0])

        if (strncmpC(tmp, 0, "./", 2) == 0) {
            var i = 2
            while (tmp[i] != NUL) { // for (i=2; tmp[i]; i++)
                tmp[i - 2] = tmp[i]
                i++
            }
            tmp[i - 2] = NUL
        }

        // strcpy/strcat: the indirection program
        //   s %%="""^<name>""" g @%
        strcpyC(tmp1, "s %%=\"\"\"^")
        strcatC(tmp1, cstringAt(tmp))
        strcatC(tmp1, "\"\"\" g @%%")
        val retcode = interpret(cstringAt(tmp1), svPtr) // h014 stub
        globalClose(svPtr) // h003 macro shim -> h014 stub
        return retcode
    }

    // announce: - h011: the "?" goto jumps back here
    val history = ArrayList<String>()
    readHistory("./mumps_history", history) // read_history

    while (true) {
        // announce:
        interpret(
            "w !,\"Mumps Built: " + BUILD_TIME + " " + BUILD_DATE +
                "\nFloat: double" +              // floatSize (h018)
                "; DBMS: SQLITE; Memory Resident: 0" + // database (h018)
                "; Hardware math: yes" + "; Int: int" +
                ";\nFloat digits: 8" + "; Cache: 65537" +
                "; Block: 2048" + "; FP Bits: N/A" +   // h018
                "\",!,\"http://www.cs.uni.edu/~okane/\"," +
                "!,\"Enter ^D or HALT to exit\",!",
            svPtr
        ) // h014 stub

        print("\n") // printf("\n")

        while (true) {
            // h018: #ifdef NATIVE_SHARED btree(CLOSE,tmp0,tmp1,
            // svPtr) deadlock guard - compiled out (SQLITE
            // backend), comment only.

            print("> ") // readline("> ") prompt
            val line = readlnOrNull()

            if (line == null) { // ^d - end of file
                return exitRepl(svPtr, tmp0, tmp1, history)
            }

            if (line.isEmpty()) {
                continue // free(line); continue
            }

            history.add(line) // add_history(line)

            val l = line.lowercase() // strcasecmp analog
            if (l == "h" || l == "\\h" || l == "\\q" ||
                l == "\\quit" || l == "halt"
            ) {
                return exitRepl(svPtr, tmp0, tmp1, history)
            }

            if (l == "?") break // goto announce

            interpret(line, svPtr) // h014 stub
            // free(line);
            print("\n")
        }
    }
}

/**
 * The shared EOF/halt exit tail (vendor 178-183 and 194-199):
 * close globals, persist history, pid into tmp1, cleanLocks,
 * puts; returns the C 0.
 */
private fun exitRepl(svPtr: Msv, tmp0: CharArray, tmp1: CharArray, history: List<String>): Int {
    print("EOF - goodbye ...\n")
    mglobal(CLOSE, tmp0, tmp1, svPtr) // h014 stub - Close Globals
    writeHistory("./mumps_history", history)
    // Mltoa(getpid(),tmp1)
    val pid = ProcessHandle.current().pid()
    writeCstring(tmp1, pid.toString())
    cleanLocks(cstringAt(tmp1)) // DONE - LockMisc.kt
    print("\n") // puts("\n")
    return 0
}

/** read_history analog: loads the history file if present. */
private fun readHistory(path: String, history: MutableList<String>) {
    val f = java.io.File(path)
    if (!f.exists()) return
    f.forEachLine { history.add(it) }
}

/** write_history analog. */
private fun writeHistory(path: String, history: List<String>) {
    java.io.File(path).writeText(history.joinToString("\n") + "\n")
}
