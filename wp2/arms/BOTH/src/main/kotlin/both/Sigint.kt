package both

// Unit 190 render: interp-sysfunc.cpp::sigint (roster 190,
// batch 7, 215 LOC). The interpreter termination-signal
// handler: AllocSV dummy vector, close globals, history write,
// the signal-name message ladder, exit(EXIT_FAILURE).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: allocSV TODO stub owner
//     interp-sysfunc.cpp::AllocSV (roster 285, batch 8) called
//     direct; Mglobal CLOSE called direct (BothCRuntime seat).
//   h015 grammar-collision - reused: no backticks, no raw
//     comment dollars, ASCII only, trailing newline.
//   h017 naming - reused: sigint/readHistory/AllocSV keep
//     vendor case (writeHistory mirrors the MumpsMain
//     readHistory seat spelling).
//   h013 dead_store - reused: the vendor #ifndef fallback
//     signal numbers (101-133) are compile-time dead on POSIX
//     (signal.h always defines them); the render seats the
//     POSIX values, fallbacks noted here only.
//
// Quirks kept (ledgered):
//   - raise(SIGKILL) on the AllocSV NULL path: SIGKILL cannot
//     be trapped; folded to exitProcess(137) (the 128+9
//     shell convention, same family as the abortZb 134 fold).
//     The svPtr NULL check itself is quantized: allocSV here
//     returns or throws (the stub blocks), the NULL arm is
//     journaled UB quantization per the house convention.
//   - write_history("./mumps_history"): the GNU history-lib
//     program edge is a QUANTIZED NO-OP (the MumpsMain
//     readHistory precedent, same journal entry family).
//   - printf faces are System.out (exact emission text kept,
//     including the leading blank-line conventions).
//   - exit(EXIT_FAILURE) = exit(1).

// the POSIX signal values (signal.h always defines these on
// the target; the vendor 101-133 #ifndef fallbacks are dead)
internal const val SIGHUP = 1
internal const val SIGINT = 2
internal const val SIGQUIT = 3
internal const val SIGILL = 4
internal const val SIGTRAP = 5
internal const val SIGABRT = 6
internal const val SIGBUS = 7
internal const val SIGFPE = 8
internal const val SIGKILL = 9
internal const val SIGUSR1 = 10
internal const val SIGSEGV = 11
internal const val SIGUSR2 = 12
internal const val SIGPIPE = 13
internal const val SIGALRM = 14
internal const val SIGTERM = 15
internal const val SIGSTKFLT = 16
internal const val SIGCHLD = 17
internal const val SIGCONT = 18
internal const val SIGSTOP = 19
internal const val SIGTSTP = 20
internal const val SIGTTIN = 21
internal const val SIGTTOU = 22
internal const val SIGURG = 23
internal const val SIGXCPU = 24
internal const val SIGXFSZ = 25
internal const val SIGVTALRM = 26
internal const val SIGPROF = 27
internal const val SIGWINCH = 28
internal const val SIGIO = 29
internal const val SIGPWR = 30
internal const val SIGSYS = 31

// void write_history(const char *) - GNU history-lib seat,
// quantized no-op (the MumpsMain readHistory precedent)
internal fun writeHistory(path: String) {
    // quantized no-op (history-lib program edge; journaled)
}

// void sigint(int i) - interp-sysfunc.cpp
internal fun sigint(i: Int) {
    val tmp = CharArray(2)
    val tmp1 = CharArray(2)
    val svPtr = allocSV() // dummy vector (stub-blocked, owner 285)
    // the vendor NULL check: AllocSV NULL is UB quantized here
    // (the stub throws); the raise(SIGKILL) arm folds below for
    // the record
    // if (svPtr == NULL) { printf; raise(SIGKILL); }

    tmp[0] = 0.toChar()
    tmp1[0] = 0.toChar()

    // Close Globals
    Mglobal(CLOSE, tmp, tmp1, svPtr)

    writeHistory("./mumps_history")

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
    kotlin.system.exitProcess(1) // exit(EXIT_FAILURE)
}
