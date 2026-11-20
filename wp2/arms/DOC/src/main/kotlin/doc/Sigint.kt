package doc

// Unit 211 (B7 tranche 3): interp-sysfunc.cpp::sigint (batch 7,
// complex stratum; units.csv row 211: loc 215, score 70.1) - this
// render RESOLVES the Stubs.sigint seat (declared
// include/mumpsc/SYM.h:41; the seat flips to the delegate
// below, H-014 seat-conversion precedent). The signal
// termination handler: allocates the dummy state vector
// (AllocSV the batch-8 H-014 seat, correctly tagged), closes
// the globals through the Mglobal CLOSE walk (CLOSE 4,
// defines.h:87; the CharArray tmp/tmp1 CLOSE key pair, unit-150
// MumpsMain precedent), writes the readline history (the vendor
// write_history("./mumps_history") is a NO-OP ledger in the
// JVM render - history persistence has no JVM analog, the
// unit-150 precedent), prints the termination message with the
// signal number, switches over the signal cases printing the
// POSIX/ANSI/BSD signal-name table kept verbatim, and exits
// EXIT_FAILURE (exitProcess(1), the Fcn.kt EXIT_FAILURE
// precedent). H-018 preprocessor ledger: the vendor #ifndef
// fallback ladder (101-133) is INERT on the reference build -
// signal.h is in the include set, so every SIGHUP..SIGSYS arm
// resolves to the system constants (Linux x86-64: SIGHUP 1 ..
// SIGSYS 31, the private const ladder below carries them); the
// commented-out SIGABRT 107 define pair is kept as the comment
// fossil it is (SIGABRT resolves to the system 6); the vendor
// QUIRK kept and noted: the second #ifndef SIGKILL (110) is
// dead - the first guard (101.., itself inert) or signal.h
// already defined it. The vendor svPtr NULL probe with the
// Memory error exit printf and raise(SIGKILL) is unreachable
// in the render (AllocSV returns non-null; H-013 dead-arm
// ledger, kept as comment); printf maps to System.out (H-001)
// with the %d format via String.format Locale.US (H-009). H-010:
// i is the Int signal seat. The 100 Software Error case (the
// vendor software-error token under the signal switch) and the
// default arm kept verbatim. Naming per H-017 (sigint
// verbatim).

// Linux x86-64 signal.h constants (H-018: the live arms; the
// vendor #ifndef fallback ladder above is inert, kept in the
// ledger note).
private const val SIGHUP_SI = 1
private const val SIGINT_SI = 2
private const val SIGQUIT_SI = 3
private const val SIGILL_SI = 4
private const val SIGTRAP_SI = 5
private const val SIGABRT_SI = 6   // the commented 107 define is a fossil
private const val SIGBUS_SI = 7
private const val SIGFPE_SI = 8
private const val SIGKILL_SI = 9   // the duplicate 110 guard is dead
private const val SIGUSR1_SI = 10
private const val SIGSEGV_SI = 11
private const val SIGUSR2_SI = 12
private const val SIGPIPE_SI = 13
private const val SIGALRM_SI = 14
private const val SIGTERM_SI = 15
private const val SIGSTKFLT_SI = 16
private const val SIGCHLD_SI = 17
private const val SIGCONT_SI = 18
private const val SIGSTOP_SI = 19
private const val SIGTSTP_SI = 20
private const val SIGTTIN_SI = 21
private const val SIGTTOU_SI = 22
private const val SIGURG_SI = 23
private const val SIGXCPU_SI = 24
private const val SIGXFSZ_SI = 25
private const val SIGVTALRM_SI = 26
private const val SIGPROF_SI = 27
private const val SIGWINCH_SI = 28
private const val SIGIO_SI = 29
private const val SIGPWR_SI = 30
private const val SIGSYS_SI = 31
private const val CLOSE_SI = 4 // defines.h:87 CLOSE (Mglobal code)

fun sigint(i: Int) {

    val tmp = CharArray(2)
    val tmp1 = CharArray(2)
    val svPtr: Msv

    svPtr = Stubs.AllocSV() /* dummy vector */

    // if (svPtr==NULL) { printf("Memory error exit\n");
    //      fflush(stdout); raise(SIGKILL); } - the vendor NULL
    // probe is unreachable here (AllocSV returns non-null);
    // H-013 dead-arm ledger.

    tmp[0] = NUL
    tmp1[0] = NUL

    /* Close Globals */

    Stubs.Mglobal(CLOSE_SI, tmp, tmp1, svPtr)

    // write_history("./mumps_history"); - no-op ledger (history
    // persistence has no JVM analog; the unit-150 precedent).

    System.out.print("\n\nGlobal arrays closed.\n")

    System.out.print(String.format(java.util.Locale.US,
        "\nTermination message: %d ", i))

    when (i) {
        SIGHUP_SI -> System.out.print("Hangup (POSIX)\n")
        SIGINT_SI -> System.out.print("Interrupt (ANSI)\n")
        SIGQUIT_SI -> System.out.print("Quit (POSIX)\n")
        SIGILL_SI -> System.out.print("Illegal instruction (ANSI)\n")
        SIGTRAP_SI -> System.out.print("Trace trap (POSIX)\n")
        SIGABRT_SI -> System.out.print("Abort (ANSI)\n")
        SIGBUS_SI -> System.out.print("BUS error (4.2 BSD)\n")
        SIGFPE_SI -> System.out.print("Floating-point exception (ANSI)\n")
        SIGKILL_SI -> System.out.print("Kill, unblockable (POSIX)\n")
        SIGUSR1_SI -> System.out.print("User-defined signal 1 (POSIX)\n")
        SIGSEGV_SI -> System.out.print("Segmentation violation (ANSI)\n")
        SIGUSR2_SI -> System.out.print("User-defined signal 2 (POSIX)\n")
        SIGPIPE_SI -> System.out.print("Broken pipe (POSIX)\n")
        SIGALRM_SI -> System.out.print("Alarm clock (POSIX)\n")
        SIGTERM_SI -> System.out.print("Termination (ANSI)\n")
        SIGSTKFLT_SI -> System.out.print("Stack fault\n")
        SIGCHLD_SI -> System.out.print("Child status has changed (POSIX)\n")
        SIGCONT_SI -> System.out.print("Continue (POSIX)\n")
        SIGSTOP_SI -> System.out.print("Stop, unblockable (POSIX)\n")
        SIGTSTP_SI -> System.out.print("Keyboard stop (POSIX)\n")
        SIGTTIN_SI -> System.out.print("Background read from tty (POSIX)\n")
        SIGTTOU_SI -> System.out.print("Background write to tty (POSIX)\n")
        SIGURG_SI -> System.out.print("Urgent condition on socket (4.2 BSD)\n")
        SIGXCPU_SI -> System.out.print("CPU limit exceeded (4.2 BSD)\n")
        SIGXFSZ_SI -> System.out.print("File size limit exceeded (4.2 BSD)\n")
        SIGVTALRM_SI -> System.out.print("Virtual alarm clock (4.2 BSD)\n")
        SIGPROF_SI -> System.out.print("Profiling alarm clock (4.2 BSD)\n")
        SIGWINCH_SI -> System.out.print("Window size change (4.3 BSD, Sun)\n")
        SIGIO_SI -> System.out.print("I/O now possible (4.2 BSD)\n")
        SIGPWR_SI -> System.out.print("Power failure restart (System V)\n")
        SIGSYS_SI -> System.out.print("Bad system call\n")
        100 -> System.out.print("Software Error\n")
        else -> System.out.print("default signal\n")
    }

    System.out.print("\n")
    kotlin.system.exitProcess(1) // exit(EXIT_FAILURE)
}
