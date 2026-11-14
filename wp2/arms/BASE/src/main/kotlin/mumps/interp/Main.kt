package mumps.interp

import java.io.File

/**
 * Kotlin translation of mumps.cpp (Study Subset unit 12, Batch 1, arm BASE;
 * translated 2026-11-14): pad, SyncClock, set_alarm, main — the interpreter
 * driver.
 */

// --- C: #define database / floatSize / banner parts (reference build: SQLITE) ---
private const val DATABASE = "; DBMS: SQLITE; Memory Resident: 0"
private const val FLOAT_SIZE = "Float: double"
private const val HARDWARE_MATH = "; Hardware math: yes"
private const val INTS = "; Int: int"
private const val FLT_DIGITS = ";\nFloat digits: 8"
private const val CACHE = "; Cache: 65537"
private const val BLK_SIZ = "; Block: 2048"
private const val FPBITS = "; FP Bits: 72" // C: HAVE_HARDWARE_MATH undefined — verify via banner probe

/** C: __TIME__ / __DATE__ compile-time macros (heuristic 48). The JVM has no
 *  text macros; the banner is interactive-only (unobservable by the frozen
 *  oracle). Stamp at integration build time. */
object BuildInfo {
    const val TIME = "00:00:00"
    const val DATE = "Jan  1 2026"
}

/** C: char *pad(char *p, int x) — the C returns a STACK buffer (UB); value
 *  return here is the safe equivalent (heuristic 50). */
fun pad(p: String, x: Int): String = p.padEnd(x, ' ')

/** C: void SyncClock() — SIGALRM handler body. */
fun syncClock() { InterpState.DBSem = 1 }

/** C: unsigned set_alarm(unsigned) — setitimer(ITIMER_REAL); JVM has no POSIX
 *  timers; the sole call site is inside the dead if(0) block. */
fun setAlarm(@Suppress("UNUSED_PARAMETER") seconds: UInt): UInt =
    TODO("setitimer not available on JVM (heuristic 46; sole call site is dead code)")

/** C: #define GlobalClose — closes the global arrays. */
fun globalClose(svPtr: MsvState) = mglobal(InterpState.CLOSE, "", "", svPtr)

// --- GNU readline replacement (heuristic 47: documented divergence) ---
private val history = mutableListOf<String>()

/** C: read_history("./mumps_history") — naive line list; readline's editing
 *  and history-file format are not reproduced (REPL is unreachable by the
 *  frozen oracle: it always runs with a program argument). */
private fun readHistory(file: String) {
    val f = File(file)
    if (f.exists()) history.addAll(f.readLines())
}

private fun writeHistory(file: String) {
    File(file).writeText(history.joinToString("\n"))
}

fun main(args: Array<String>) {
    val svPtr = MsvState()

    if (false) {
        // C: if (0) { signal(SIGALRM, SyncClock); set_alarm(1); } — dead guard
        // (heuristic 20); POSIX signal/timer wiring has no JVM equivalent and
        // the C branch is never taken (heuristic 46).
    }

    // C mid-function #include <mumpsc/cgi.h> — declarations only; merged at
    // file scope, no statement effect.

    // C argv is 0-based with argv[0] = program name; Kotlin args excludes it
    // (heuristic 49): all indices shift down by one.
    if (args.isNotEmpty()) svPtr.program = args[0] // C: argv[1]
    else svPtr.program = "mumps"                   // C: argv[0] — unavailable on JVM

    if (args.isNotEmpty()) symPut("%", args[0], svPtr)
    else symPut("%", "", svPtr)

    if (args.size > 1) {
        for (i in 1 until args.size) { // C: for (i=2; i<argc; i++) with t="%"+(i-1)
            symPut("%" + i, args[i], svPtr)
        }
        symPut("%c", (args.size - 1).toString(), svPtr)
        symPut("%0", args[0], svPtr)
    }

    if (args.isNotEmpty() && args[0].isNotEmpty()) { // program mode
        var tmp = args[0]
        if (tmp.startsWith("./")) tmp = tmp.substring(2) // C: strip leading ./
        val tmp1 = "s %%=\"\"\"^" + tmp + "\"\"\" g @%%"
        val retcode = interpret(tmp1, svPtr)
        globalClose(svPtr) // C: GlobalClose
        return retcode
    }

    outer@ while (true) { // C: label announce
        interpret("w !,\"Mumps Built: " + BuildInfo.TIME + " " + BuildInfo.DATE + "\n" +
                FLOAT_SIZE + DATABASE + HARDWARE_MATH + INTS + FLT_DIGITS + CACHE + BLK_SIZ + FPBITS +
                "\",!,\"http://www.cs.uni.edu/~okane/\",!,\"Enter ^D or HALT to exit\",!", svPtr)
        print("\n")
        readHistory("./mumps_history")

        while (true) {
            if (NATIVE_SHARED) btree(InterpState.CLOSE, "", StringBuilder(), svPtr) // C: prevent deadlocks

            print("> ") // C: readline("> ") — heuristic 47
            val line = readlnOrNull()

            if (line == null) { // ^D — end of file
                println("EOF - goodbye ...")
                mglobal(InterpState.CLOSE, "", "", svPtr)
                writeHistory("./mumps_history")
                cleanLocks(CRuntime.mltoa(ProcessHandle.current().pid().toLong()))
                println("\n")
                return 0
            }

            if (line.isEmpty()) continue

            history.add(line) // C: add_history

            val l = line.lowercase() // C: strcasecmp — ASCII case-fold
            if (l == "h" || l == "\\h" || l == "\\q" || l == "\\quit" || l == "halt") {
                println("EOF - goodbye ...")
                mglobal(InterpState.CLOSE, "", "", svPtr)
                writeHistory("./mumps_history")
                cleanLocks(CRuntime.mltoa(ProcessHandle.current().pid().toLong()))
                println("\n")
                return 0
            }

            if (l == "?") continue@outer // C: goto announce

            interpret(line, svPtr)
            print("\n")
        }
    }
}
