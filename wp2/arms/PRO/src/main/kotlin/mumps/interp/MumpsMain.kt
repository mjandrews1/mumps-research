package mumps.interp

/**
 * PRO-arm translation of mumps.cpp::<misc> (pilot B1, unit 12 - the
 * interpreter driver: pad, SyncClock, set_alarm, main/REPL).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * mumps.cpp lines 40-239.
 *
 * Reference-build configuration (component config.log line 7 -
 * authoritative for every #ifdef here):
 *   ./configure --with-sqlite --with-dbname=mumps --with-slice=0
 *     --with-hardware-math=yes --with-float-digits=8 --with-int-32
 *     --with-journal-mode=OFF --with-data-size=512
 *     --with-index-size=256 --with-index-max=14 --with-alarm=0
 * hence SQLITE defined; NATIVE/NATIVE_SHARED/CLIENT/READ_ONLY and
 * BIGFLOAT not defined; HAVE_HARDWARE_MATH defined.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h003 state_macro (adapted): the file-scope svPtr that $SymPut and
 *    GlobalClose splice is carried in InterpState; per the unit 9
 *    adaptation the shims take svPtr explicitly (symPut/globalClose
 *    wrappers over h014 stubs).
 *  - h004 globals_ext        : DBTimePulse/DBSem on MumpsGlobals;
 *    the driver's svPtr pointer on InterpState.
 *  - h011 goto               : backward goto announce -> outer
 *    while(true); the REPL loop stays inner.
 *  - h012 unsigned_byte_domain: tmp/tmp0/tmp1 program buffers.
 *  - h013 dead_store         : the static struct C list (S/p1/p2) is
 *    never used, the signal/set_alarm setup is behind if(0), and the
 *    DirectRead forward declaration has no call - all dropped with
 *    comments, never "fixed".
 *  - h014 out_of_scope_call  : Interpret, Mglobal, $SymPut/SymPut,
 *    GlobalClose, cleanLocks -> InterpStubs.kt (owners named).
 *  - h015 grammar_collision  : the MUMPS command strings with quote
 *    runs (s %="""^ etc.) and escapes verified for the Kotlin lexer.
 *  - h017 naming             : SyncClock->syncClock,
 *    set_alarm->setAlarm; pad/main keep vendor names.
 *
 * MISSED TRIGGER (record_miss, third occurrence of
 * preprocessor_conditional - T1 met since unit 10): the REPL's
 * #ifdef NATIVE_SHARED btree(CLOSE,...) deadlock guard is compiled out
 * of the reference build (SQLITE backend) and omitted here.
 *
 * Documented divergences (no JVM analog; flagged for grading):
 *  - __TIME__/__DATE__ are build-machine constants; placeholders
 *    (BUILD_TIME/BUILD_DATE) are used, so the announce line differs
 *    from the reference binary in exactly that field.
 *  - GNU readline/history: plain stdin reader; history kept in memory
 *    and persisted by readHistory/writeHistory analogs.
 *  - pad(): the C returns a dangling stack buffer (UB); Kotlin returns
 *    a fresh String with the same observable content.
 */

private const val STR_MAX = 32000 // strmax.h:3
private const val CLOSE = 4      // defines.h:87

/** Reference-build placeholders for the C __TIME__/__DATE__ macros. */
private const val BUILD_TIME = "00:00:00"
private const val BUILD_DATE = "Jan  1 2026"

/** h004: mumps.cpp file-scope globals (lines 51-52). */
object MumpsGlobals {
    var DBTimePulse: Int = 0
    var DBSem: Int = 0
}

/** h004: the interpreter-wide file-scope svPtr pointer. */
object InterpState {
    lateinit var svPtr: Msv
}

/** C: char * pad(char * p, int x) - pads to x with spaces. */
fun pad(p: String, x: Int): String {
    // strncpy(out,p,512) truncates at 512 (no NUL if longer - UB in C;
    // Kotlin returns the truncated prefix).
    var out = if (p.length >= 512) p.substring(0, 512) else p
    while (out.length < x) out += " " // while(strlen(out)<x) strcat(out," ");
    return out
}

/** C: void SyncClock() { DBSem = 1; } */
fun syncClock() {
    MumpsGlobals.DBSem = 1
}

/**
 * C: unsigned int set_alarm(unsigned int) - setitimer(ITIMER_REAL)
 * plumbing whose only call site is the if(0) dead block below (h013);
 * the POSIX timer API has no JVM analog, so the body is unobservable
 * in the reference build and kept as a no-op returning 0.
 */
fun setAlarm(seconds: UInt): UInt {
    return 0u
}

fun main(args: Array<String>) {
    // h013: vendor declares a static struct C {...} list (S, p1, p2)
    // that is never used - dropped, not "fixed".
    // h013: vendor guards the SIGALRM/set_alarm setup with if(0)
    // (slice zero; reference built with --with-alarm=0) - dropped.
    // h013: DirectRead is forward-declared but never called - dropped.

    val svPtr = InterpState.svPtr
    val tmp = CharArray(STR_MAX)
    val tmp0 = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)

    if (args.isNotEmpty()) { // program name
        // strncpy((char *)svPtr->program, argv[1],512);
        // Kotlin args[0] is the argv[1] analog (program argument).
        val name = args[0]
        for (k in name.indices.take(512)) svPtr.program[k] = name[k]
    } else {
        val name = "mumps" // argv[0] analog when no argument given
        for (k in name.indices.take(512)) svPtr.program[k] = name[k]
    }

    if (args.isNotEmpty()) {
        symPut(svPtr, "%", args[0]) // $SymPut("%",argv[1])
    } else {
        symPut(svPtr, "%", "") // $SymPut("%","")
    }

    if (args.size > 1) {
        for (i in 1 until args.size) { // for (int i=2; i<argc; i++)
            symPut(svPtr, "%" + i, args[i]) // sprintf(t,"%%%d",i-1)
        }
        symPut(svPtr, "%c", (args.size - 1).toString()) // argc-2
        symPut(svPtr, "%0", args[0]) // argv[1] analog
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

        strcpyC(tmp1, "s %%=\"\"\"^")
        strcatC(tmp1, cstringAt(tmp))
        strcatC(tmp1, "\"\"\" g @%%")
        val retcode = interpret(cstringAt(tmp1), svPtr) // h014 stub
        globalClose(svPtr) // h003 macro shim -> h014 stub
        kotlin.system.exitProcess(retcode)
    }

    val history = ArrayList<String>()
    readHistory("./mumps_history", history)

    val stdin = System.`in`.bufferedReader() // readline("> ") analog

    // h011: backward goto announce -> outer while(true).
    // The announce string splices __TIME__/__DATE__ and the config
    // macros (floatSize database HardwareMath Ints FltDigits Cache
    // BlkSiz Fpbits), resolved per the reference build.
    while (true) {
        // announce:
        interpret(
            "w !,\"Mumps Built: " + BUILD_TIME + " " + BUILD_DATE +
                "\nFloat: double; DBMS: SQLITE; Memory Resident: 0" +
                "; Hardware math: yes; Int: int" +
                ";\nFloat digits: 8; Cache: 65537; Block: 2048" +
                "; FP Bits: N/A" +
                "\",!,\"http://www.cs.uni.edu/~okane/\"," +
                "!,\"Enter ^D or HALT to exit\",!",
            svPtr
        ) // h014 stub

        print("\n")

        while (true) {
            // #ifdef NATIVE_SHARED btree(CLOSE,tmp0,tmp1,svPtr) deadlock
            // guard compiled out (preprocessor_conditional, 3rd miss).

            print("> ")
            val line = stdin.readLine() ?: run { // ^d - end of file
                exitRepl(svPtr, tmp0, tmp1, history)
                return
            }

            if (line.isEmpty()) continue

            history.add(line) // add_history(line)

            val l = line.lowercase() // strcasecmp analog
            if (l == "h" || l == "\\h" || l == "\\q" || l == "\\quit" ||
                l == "halt"
            ) {
                exitRepl(svPtr, tmp0, tmp1, history)
                return
            }

            if (l == "?") break // goto announce

            interpret(line, svPtr) // h014 stub
            print("\n")
        }
    }
}

private fun exitRepl(svPtr: Msv, tmp0: CharArray, tmp1: CharArray, history: List<String>) {
    print("EOF - goodbye ...\n")
    mglobal(CLOSE, tmp0, tmp1, svPtr) // h014 stub - Close Globals
    writeHistory("./mumps_history", history)
    // Mltoa(getpid(),tmp1)
    val pid = ProcessHandle.current().pid()
    writeCstring(tmp1, pid.toString())
    cleanLocks(cstringAt(tmp1)) // h014 stub
    print("\n") // puts("\n")
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
