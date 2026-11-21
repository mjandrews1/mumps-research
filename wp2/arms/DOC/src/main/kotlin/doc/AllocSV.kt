package doc

// Unit 239 (B8 tranche 3): interp-sysfunc.cpp::AllocSV (batch 8,
// complex stratum; LOC 103, score 164.9, rank 285; units.csv row
// 219). The state-vector constructor: builds and field-initializes
// a fresh MSV per call (the vendor malloc cannot fail in the JVM
// domain - the NULL return arm is dead per the unit-211 Sigint
// precedent, H-013 ledger), with the once-only first-call gate
// preserved for the signal-registration block. H-018 divergence
// ledger: the vendor signal(SIGHUP..SIGSYS) registration block can
// NOT install POSIX handlers from the JVM domain - the handler
// itself (sigint) IS the landed unit-211 render; the registration
// block renders as the once-only comment ledger below (the vendor
// signal constants list is carried by the Sigint.kt ladder).
// H-018 inert-arm ledger: the #else (non-SQLITE) MumpsNativeDatabase
// getenv arm is dead on the reference build (SQLITE defined per the
// unit-202 sql family ruling); kept as the comment fossil, and the
// SQLITE arm (Col_Names/pgsql_msg/Table/TabSize) is live.
// memset divergences: the Kotlin CharArray/IntArray carriers are
// zero-initialized at construction, so the vendor memset(p1->bd/
// pd1/v1d/xd/setname, '\0', ...) family and the reference[0]=0
// write are structurally satisfied by construction (H-018, noted
// in situ). The lun-5 default devices bind through the STDIN_READER
// carrier (unit-48 Getstr2) and the STDOUT_WRITER carrier raised
// here (the writer twin of that seat). Consult H-018 hit-adapt:
// the same build-config and JVM-domain rulings the sql family and
// unit-211 established, applied to the constructor. Naming per
// H-017 (AllocSV verbatim).

// static struct MSV *p1 = NULL: the once-only gate of the vendor
// first call (signal registration). The vendor then mallocs a
// FRESH MSV on every call (the static is never returned); the
// gate is the only static behavior, kept as this flag.
private var allocSvFirstCall = true

// FILE *stdout of the vendor default device table: the Writer twin
// of the Getstr2 STDIN_READER seat (H-014, per-file carrier raise).
val STDOUT_WRITER: java.io.Writer by lazy {
    java.io.OutputStreamWriter(System.out, Charsets.US_ASCII)
}

fun AllocSV(): Msv {

    if (allocSvFirstCall) {
        // first-call signal-registration block (once-only in the
        // vendor): SIGHUP ignored, then SIGINT/SIGQUIT/SIGILL/
        // SIGTRAP/SIGABRT/SIGIOT/SIGBUS/SIGFPE/SIGKILL/SIGUSR1/
        // SIGSEGV/SIGUSR2/SIGPIPE/SIGTERM/SIGSTKFLT/SIGXCPU/
        // SIGXFSZ/SIGPWR/SIGSYS all bound to sigint (the unit-211
        // render); SIGALRM stays the commented-out vendor fossil.
        // JVM domain: handlers cannot be installed; ledgered here,
        // the gate itself is kept so call-order behavior matches.
        allocSvFirstCall = false
    }

    val p1 = Msv()

    // printf("*** Unable to allocate MSV\n") + return NULL: dead
    // arm in the JVM domain (H-013, the Sigint.kt precedent).

    p1.LockDone = 0
    p1.io = 5
    p1.ierr = 0
    // memset(bd/pd1/v1d/xd/setname): construction-zeroed (H-018)
    p1.symflg = 0
    p1.ITRAP = 0
    p1.setpiece = 0
    p1.t0px = 0
    p1.xpx = 0
    p1.nstart = null
    p1.ctrlc = 0
    p1.iorg = 1
    p1.symlen = 0
    p1.NOERR = 0

    for (i in 0 until 10) {
        p1.in_file[i] = null /* file ptrs */
        p1.out_file[i] = null /* file ptrs */
        p1.hor[i] = 0 /* horizontal cursors */
        p1.ver[i] = 1 /* vertical cursors */
    }

    p1.in_file[5] = STDIN_READER
    p1.out_file[5] = STDOUT_WRITER
    p1.LineNumber = 0
    p1.tpx = 0
    p1._Sym = 0
    p1.pd1len = 0
    p1.t2 = 0
    p1.nflg = 0
    p1.Rargsx = 0
    p1.ERROR = 0
    p1.gpad = 0

    for (i in 0 until SYM_MAX) p1.start[i] = null

    p1.order = 0
    p1._SymPush = 0
    p1.hit = 0
    p1.count = 0
    p1.ScanMinWordSize = 3
    p1.ScanMaxWordSize = 25
    p1.fdes[0] = -1
    p1.fdes[1] = -1
    p1.exec = 0
    // reference[0] = 0: construction-zeroed carrier (H-018)
    p1.CurrentFile = ""

    // SQLITE live on the reference build (the unit-202 ruling):
    CstringOps.strcpyS(p1.Col_Names, "")
    CstringOps.strcpyS(p1.pgsql_msg, "")
    CstringOps.strcpyS(p1.Table, "mumps")
    p1.TabSize = 14

    // #else (non-SQLITE) getenv("MumpsNativeDatabase") cfgdata/
    // cfgkey/UDAT/UKEY arm: dead on the reference build (H-018
    // inert-arm ledger; the vendor populates the native-btree
    // path files, which the SQLITE build does not read).

    return p1
}
