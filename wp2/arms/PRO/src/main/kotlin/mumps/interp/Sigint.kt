package mumps.interp

import kotlin.system.exitProcess

/**
 * PRO-arm translation of interp-sysfunc.cpp::sigint (B7 tranche 2,
 * unit 191, rank 254). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 260-524 (definition at line 260).
 *
 * Identity verified against the study tool: interp-sysfunc.cpp::
 * sigint, cpp, loc 215, comment_ratio 0.018, pointer_marks 0,
 * control_marks 34, score 70.1 - exact match to
 * wp1/study-subset/units.csv row 191 (batch 7). RESOLVES the
 * InterpStubs.kt sigint h014 stub (same signature; the
 * ErrorMessage/TimeOut_getstr1/DirectRead/getstr1 call sites and
 * every sigint(100) diagnostic site in the arm bind unchanged).
 *
 * sigint(int i): the fatal-signal/error handler - closes globals
 * over a dummy state vector, flushes the readline history file,
 * prints the signal-number message table, and exits
 * EXIT_FAILURE.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param  : printf/fflush -> plain print
 *    (established convention); the history flush -> the
 *    ReadlineHistory object carrier below (the MumpsMain.kt
 *    readHistory/writeHistory seats predate and stay; the object
 *    carries the sigint write_history contract for future
 *    owners).
 *  - h002 simple_output_macro: the vendor printf calls keep
 *    their literal strings verbatim.
 *  - h008 static_scratch   : AllocSV's static p1 has no JVM
 *    analog (the allocSV() seat pushes fresh state); tmp/tmp1 are
 *    stack buffers - fresh CharArrays.
 *  - h012 unsigned_byte_domain: tmp/tmp1 ride NUL-terminated
 *    CharArrays for the Mglobal edge.
 *  - h013 dead_code         : none.
 *  - h014 out_of_scope_call: mglobal (InterpStubs, owner
 *    interp-globalb.cpp::Mglobal, u161-era stub) reused at the
 *    CLOSE edge; allocSV (InterpStubs, state-vector machinery)
 *    reused.
 *  - h015 grammar_collision: the SIG* names are file-private
 *    consts (no package collisions); the double-redefinition
 *    SIGKILL fossil (vendor 272 defines 101 under #ifndef, then
 *    311 re-#ifndefs SIGKILL 110 - dead under the first) is a
 *    KDoc note only.
 *  - h017 naming           : sigint keeps the vendor name.
 *  - h018 reference_build_visibility: every SIG* macro IS defined
 *    by the platform headers in the reference (Linux glibc)
 *    build, so ALL 33 #ifndef fallback arms (101-133) are
 *    EXCLUDED (u125 MSVC-arm precedent); the consts below carry
 *    the Linux values, and the fallback arm is recorded in the
 *    ledger, not emitted.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - svPtr = AllocSV() "dummy vector" - the handler allocates a
 *    THROWAWAY state vector just to close globals; the malloc-NULL
 *    arm (printf + raise(SIGKILL)) is unreachable in the JVM
 *    carrier (no malloc; allocSV never returns null) - dropped
 *    with the u126 note.
 *  - case 100 is the vendor's software-error sentinel (every
 *    sigint(100) diagnostic call in the arm lands here: "Software
 *    Error").
 *  - SIGIOT aliases SIGABRT (6) on the reference platform: the
 *    vendor lists case SIGABRT and case SIGIOT as separate arms
 *    with distinct texts, but both evaluate 6 there (a duplicate
 *    C case value; the SIGIOT arm can never be selected) - the
 *    Kotlin when carries the SIGABRT arm only and the IOT arm is
 *    this comment-only note.
 *  - exit(EXIT_FAILURE) -> exitProcess(1) (EXIT_FAILURE is 1 on
 *    the reference platform).
 */

private const val CLOSE = 4 // vendor defines.h:87 (Mglobal close)
private const val NULC = '\u0000'

// h018: Linux values (all platform macros defined in the
// reference build; the 101-133 fallback arms excluded).
private const val SIGHUP = 1
private const val SIGINT = 2
private const val SIGQUIT = 3
private const val SIGILL = 4
private const val SIGTRAP = 5
private const val SIGABRT = 6
private const val SIGBUS = 7
private const val SIGFPE = 8
private const val SIGKILL = 9
private const val SIGUSR1 = 10
private const val SIGSEGV = 11
private const val SIGUSR2 = 12
private const val SIGPIPE = 13
private const val SIGALRM = 14
private const val SIGTERM = 15
private const val SIGSTKFLT = 16
private const val SIGCHLD = 17
private const val SIGCONT = 18
private const val SIGSTOP = 19
private const val SIGTSTP = 20
private const val SIGTTIN = 21
private const val SIGTTOU = 22
private const val SIGURG = 23
private const val SIGXCPU = 24
private const val SIGXFSZ = 25
private const val SIGVTALRM = 26
private const val SIGPROF = 27
private const val SIGWINCH = 28
private const val SIGIO = 29
private const val SIGPWR = 30
private const val SIGSYS = 31

/**
 * Carrier for the vendor's readline history state (the
 * write_history("./mumps_history") edge). The MumpsMain.kt
 * local-file seats predate this unit and stay; owners push
 * entries here and sigint flushes them at exit.
 */
object ReadlineHistory {
    /** One command per entry (readline history-file shape). */
    val lines = mutableListOf<String>()

    /** write_history("./mumps_history") analog (u130 seat shape). */
    fun writeHistoryFile() {
        try {
            java.io.File("./mumps_history").writeText(lines.joinToString("") { it + "\n" })
        } catch (e: java.io.IOException) {
            // vendor write_history is a void readline call: a
            // failed flush is silent - kept.
        }
    }
}

/**
 * vendor 260: void sigint(int i) - the termination handler.
 */
fun sigint(i: Int) {
    val tmp = CharArray(2)
    val tmp1 = CharArray(2)

    // vendor: svPtr = AllocSV(); dummy vector (malloc-NULL arm
    // unreachable in the JVM carrier - journal note)
    val svPtr = allocSV()

    tmp[0] = NULC
    tmp1[0] = NULC

    // Close Globals
    mglobal(CLOSE, tmp, tmp1, svPtr)

    ReadlineHistory.writeHistoryFile() // write_history("./mumps_history")

    print("\n\nGlobal arrays closed.\n")

    print("\nTermination message: " + i + " ")

    when (i) {
        SIGHUP -> print("Hangup (POSIX)\n")
        SIGINT -> print("Interrupt (ANSI)\n")
        SIGQUIT -> print("Quit (POSIX)\n")
        SIGILL -> print("Illegal instruction (ANSI)\n")
        SIGTRAP -> print("Trace trap (POSIX)\n")
        SIGABRT -> print("Abort (ANSI)\n")
        SIGBUS -> print("BUS error (4.2 BSD)\n")
        SIGFPE -> print("Floating-point exception (ANSI)\n")
        SIGKILL -> print("Kill, unblockable (POSIX)\n")
        SIGUSR1 -> print("User-defined signal 1 (POSIX)\n")
        SIGSEGV -> print("Segmentation violation (ANSI)\n")
        SIGUSR2 -> print("User-defined signal 2 (POSIX)\n")
        SIGPIPE -> print("Broken pipe (POSIX)\n")
        SIGALRM -> print("Alarm clock (POSIX)\n")
        SIGTERM -> print("Termination (ANSI)\n")
        SIGSTKFLT -> print("Stack fault\n")
        SIGCHLD -> print("Child status has changed (POSIX)\n")
        SIGCONT -> print("Continue (POSIX)\n")
        SIGSTOP -> print("Stop, unblockable (POSIX)\n")
        SIGTSTP -> print("Keyboard stop (POSIX)\n")
        SIGTTIN -> print("Background read from tty (POSIX)\n")
        SIGTTOU -> print("Background write to tty (POSIX)\n")
        SIGURG -> print("Urgent condition on socket (4.2 BSD)\n")
        SIGXCPU -> print("CPU limit exceeded (4.2 BSD)\n")
        SIGXFSZ -> print("File size limit exceeded (4.2 BSD)\n")
        SIGVTALRM -> print("Virtual alarm clock (4.2 BSD)\n")
        SIGPROF -> print("Profiling alarm clock (4.2 BSD)\n")
        SIGWINCH -> print("Window size change (4.3 BSD, Sun)\n")
        SIGIO -> print("I/O now possible (4.2 BSD)\n")
        SIGPWR -> print("Power failure restart (System V)\n")
        SIGSYS -> print("Bad system call\n")
        100 -> print("Software Error\n")
        else -> print("default signal\n")
    }

    print("\n")
    exitProcess(1) // exit(EXIT_FAILURE)
}
