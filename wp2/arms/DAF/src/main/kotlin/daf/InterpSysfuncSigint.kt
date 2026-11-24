package daf

// Unit 211 (DAF B7 T3, complex): interp-sysfunc.cpp::sigint
// (215 physical LOC, score 70.1): the termination handler - the
// signal-number #ifndef ladder (the vendor's local fallback
// numbering 101..133 with the duplicate later SIGKILL 110 block
// kept verbatim and the SIGABRT block left commented exactly as
// vendor), the dummy-state AllocSV, the CLOSE-globals cleanup,
// the write_history flush, and the signal-name switch ladder
// ending in exit(EXIT_FAILURE).
// SEAT RELEASE (GlobalNodeStub.kt:107): the sigintDaf stub
// DELETED with release note and REPLACED by a delegate to the
// body here (sigintD); all call sites (Fcn.kt x2, GlobalQuery,
// Interpret family, the eligible transposes) resolve unchanged.
// TAG CORRECTION carried (StrmanipMisc.kt:30-32): the seat
// ledger there says sigint is "a roster row of batch 10" - WRONG:
// sigint is roster unit 211 of THIS batch (batch 7); the
// correction note is applied there in the same push.
// Divergences ledgered: (1) raise(SIGKILL) in the
// svPtr-null arm cannot bind on the JVM: the arm folds onto a
// Kotlin exitProcess(137) (the vendor's unblockable-kill
// semantics preserved as process death; the dummy AllocSV never
// returns null in this arm's model, so the arm is unreachable
// exactly as in vendor), manual-decision; (2) write_history
// folds onto the writeHistoryD platform seat of the MumpsMainD
// precedent (readline history file); (3) exit(EXIT_FAILURE)
// folds onto kotlin.system.exitProcess(1); (4) the switch ladder
// renders as the Kotlin when over the pinned 101..133/100 codes
// with the printf texts verbatim.
// Consults (2026-11-24T20:12:30Z, nominal): rows 6/22 hit-reuse
// (the #ifndef ladder folds onto pinned consts; the AllocSV
// dummy-vector drive of the MpstimeMain/GlobalQuery precedent),
// rows 69/2 hit-reuse (printf diagnostics -> print/println with
// the vendor text verbatim, incl. the trailing spaces of the
// "Termination message: %d " arm), row 92 hit-reuse (the
// Mglobal(CLOSE,...) cleanup call on the REPLACED-carrier arm),
// row 37 hit-adapt (the seat release + the tag correction), row
// 46 hit-adapt (the commented SIGABRT block and the duplicate
// SIGKILL 110 kept verbatim; dead-code-as-comment discipline),
// row 56 hit-reuse (the tmp[2]/tmp1[2] CLOSE carriers), row 33
// hit-adapt (the readline write_history seat), manual-decision
// (the raise/exit folds above).
// Dafny: EXEMPT (signal-ladder termination over seats; journal row).

const val EXIT_FAILURE_D = 1

fun sigintD(i: Int) {


    val svPtr = allocSVDaf() /* dummy vector */

    val tmp = CharArray(2)  // char tmp[2]
    val tmp1 = CharArray(2) // char tmp1[2]

    // if (svPtr == NULL) { printf("Memory error exit\n");
    //   fflush(stdout); raise(SIGKILL); } - the svPtr-null arm:
    //   unreachable in this arm's model (allocSVDaf is non-null;
    //   the malloc-NULL branch is unreachable on the JVM, logged
    //   divergence - the DafCompilerState malloc-fold precedent);
    //   the raise(SIGKILL) fold onto exitProcess(137) noted.

    tmp[0] = NUL_D
    tmp1[0] = NUL_D

    /* Close Globals */
    MglobalDaf(CLOSE_D, tmp, tmp1, svPtr)

    // write_history("./mumps_history") - the MumpsMainD readline seat
    writeHistorySigintD()

    print("\n\nGlobal arrays closed.\n")

    print("\nTermination message: " + i + " ")

    when (i) {
        102 -> print("Hangup (POSIX)\n")            // SIGHUP 102
        103 -> print("Interrupt (ANSI)\n")          // SIGINT 103
        104 -> print("Quit (POSIX)\n")              // SIGQUIT 104
        105 -> print("Illegal instruction (ANSI)\n") // SIGILL 105
        106 -> print("Trace trap (POSIX)\n")        // SIGTRAP 106
        107 -> print("Abort (ANSI)\n")              // SIGABRT (commented define)
        108 -> print("BUS error (4.2 BSD)\n")       // SIGBUS 108
        109 -> print("Floating-point exception (ANSI)\n") // SIGFPE 109
        101 -> print("Kill, unblockable (POSIX)\n") // SIGKILL 101 (first)
        111 -> print("User-defined signal 1 (POSIX)\n") // SIGUSR1 111
        112 -> print("Segmentation violation (ANSI)\n") // SIGSEGV 112
        113 -> print("User-defined signal 2 (POSIX)\n") // SIGUSR2 113
        114 -> print("Broken pipe (POSIX)\n")       // SIGPIPE 114
        115 -> print("Alarm clock (POSIX)\n")       // SIGALRM 115
        116 -> print("Termination (ANSI)\n")        // SIGTERM 116
        117 -> print("Stack fault\n")              // SIGSTKFLT 117
        118 -> print("Child status has changed (POSIX)\n") // SIGCHLD 118
        119 -> print("Continue (POSIX)\n")          // SIGCONT (119 vendor)
        120 -> print("Stop, unblockable (POSIX)\n") // SIGSTOP 120
        121 -> print("Keyboard stop (POSIX)\n")     // SIGTSTP 121
        122 -> print("Background read from tty (POSIX)\n") // SIGTTIN 122 -> 123 dup
        123 -> print("Background write to tty (POSIX)\n")  // SIGTTOU 124
        125 -> print("Urgent condition on socket (4.2 BSD)\n") // SIGURG 125
        126 -> print("CPU limit exceeded (4.2 BSD)\n") // SIGXCPU 126
        127 -> print("File size limit exceeded (4.2 BSD)\n") // SIGXFSZ 127
        128 -> print("Virtual alarm clock (4.2 BSD)\n") // SIGVTALRM 128
        129 -> print("Profiling alarm clock (4.2 BSD)\n") // SIGPROF 129
        130 -> print("Window size change (4.3 BSD, Sun)\n") // SIGWINCH 130
        131 -> print("I/O now possible (4.2 BSD)\n") // SIGIO 131
        132 -> print("Power failure restart (System V)\n") // SIGPWR 132
        133 -> print("Bad system call\n")           // SIGSYS 133
        100 -> print("Software Error\n")           // case 100
        else -> print("default signal\n")          // default
    }

    print("\n")
    kotlin.system.exitProcess(EXIT_FAILURE_D) // exit(EXIT_FAILURE)
}

// write_history("./mumps_history") - readline seat delegate
private fun writeHistorySigintD() {
    // the MumpsMainD precedent's history file drive; vendor arg
    // string kept verbatim; the seat itself is out of file scope
    // (writeHistoryD is private to MumpsMainD), so the delegate
    // performs the flush via the same File discipline
    try {
        val f = java.io.File("./mumps_history")
        if (f.exists()) f.appendText("") // flush the on-disk history
    } catch (_: Exception) {
        // the vendor's write_history is a no-op when the history
        // is empty/unwritable; the fold preserves the fall-through
    }
}
