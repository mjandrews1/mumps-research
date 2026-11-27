package both

// Unit 218 render: interp-sysfunc.cpp::AllocSV (roster 218, batch 8,
// 116 LOC; the BothStubs TODO stub carried a "roster 285" pin - the
// complexity-rank mislabel family, the B7 Mltoa lesson; corrected
// here, the stale pin stays untouched in history). struct MSV *
// AllocSV(void) - the MSV factory: seats the POSIX signal lattice
// once, allocates the vector, clears the fixed buffers, zeroes the
// scalar fields, blanks the file/cursor tables with the ver=1
// exception, binds io=5, and runs the SQLITE config arm (the
// reference build; the #else MumpsNativeDatabase getenv arm is
// not compiled).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h020 pointer_handle - consulted, ADAPTED (hit, not reuse):
//     the 20-entry signal() lattice (SIGHUP/SIG_IGN then SIGINT
//     through SIGSYS on &sigint, SIGALRM commented out - kept)
//     has no JVM twin; the lattice renders as a documented no-op
//     guarded by a once-flag (the static p1==NULL guard), the
//     Sigint.kt precedent; sigint(100) stays the in-band error
//     path. malloc(sizeof(struct MSV)) renders as the Msv()
//     constructor; the p1==NULL printf/return-NULL arm is
//     unreachable under JVM allocation (OutOfMemoryError aborts
//     first) - quantized, ledgered.
//   h021 struct_carrier - reused: the member writes follow the
//     vendor assignment order exactly; fixed buffers ride
//     CharArray with java.util.Arrays.fill memsets, ints ride
//     Int, the FILE* pair rides the B5 RandomAccessFile fold
//     (null slots; the principal device routes through io=5 and
//     the console seats).
//   h014 out_of_scope_call - reused: no out-of-TU callees in the
//     body (strcpy/memset fold to fill/writeC direct).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: the lowercase allocSV seat stays (the
//     MpstimeMain/Fcninit/Qlength call sites keep the carrier
//     name; vendor case AllocSV noted in the header).
//
// Quirks kept (ledgered):
//   - the static p1 is OVERWRITTEN by each malloc (the vendor
//     leaks the prior vector on a second call - the guard only
//     seats the signals; kept as the once-flag semantics).
//   - ver[i]=1 while hor[i]=0 (the vertical cursor starts at
//     line 1, the horizontal at 0 - kept exact).
//   - in_file[5]/out_file[5] bind stdin/stdout AFTER the null
//     loop (slot 5 twice-touched - kept in order).
//   - reference[0]=0 only (the rest of the buffer is not
//     cleared - kept).
//   - SQLITE arm: Table="mumps", TabSize=14; Col_Names and
//     pgsql_msg cleared to "" (the native .dat/.key arm is
//     compiled out per the reference SQLITE build).

private var allocSvSignalsSeatedZb = false // static struct MSV *p1=NULL

internal fun allocSV(): Msv {
    if (!allocSvSignalsSeatedZb) {
        // signal(SIGHUP,SIG_IGN); signal(SIGINT,&sigint); ...
        // signal(SIGSYS,&sigint) - the 20-entry lattice,
        // SIGALRM commented out in the vendor (kept out).
        // JVM quantization: POSIX signal(2) is unreachable; the
        // lattice is a no-op (h020, ledgered above).
        allocSvSignalsSeatedZb = true
    }

    // p1 = (struct MSV *) malloc(sizeof(struct MSV)); if (p1==
    // NULL) { printf("*** Unable to allocate MSV\n"); return
    // NULL; } - the null arm is unreachable under JVM allocation
    // (quantized).
    val p = Msv()

    p.LockDone = 0
    p.io = 5
    p.ierr = 0
    java.util.Arrays.fill(p.bd, '\u0000') // memset(p1->bd,0,STR_MAX)
    java.util.Arrays.fill(p.pd1, '\u0000') // memset(p1->pd1,0,PS1)
    java.util.Arrays.fill(p.v1d, '\u0000') // memset(p1->v1d,0,STR_MAX)
    java.util.Arrays.fill(p.xd, '\u0000') // memset(p1->xd,0,STR_MAX)
    java.util.Arrays.fill(p.setname, '\u0000') // memset(...,256)
    p.symflg = 0
    p.ITRAP = 0
    p.setpiece = 0
    p.t0px = 0
    p.xpx = 0
    p.nstart = null
    p.ctrlc = 0
    p.iorg = 1
    p.symlen = 0
    p.NOERR = 0

    for (i in 0 until 10) {
        p.in_file[i] = null /* file ptrs */
        p.out_file[i] = null /* file ptrs */
        p.hor[i] = 0 /* horizontal cursors */
        p.ver[i] = 1 /* vertical cursors */
    }

    // p1->in_file[5]=stdin; p1->out_file[5]=stdout - the
    // principal device pair; the h020 FILE* fold leaves the
    // handle slots null (the console seats own the principal
    // streams; the io=5 channel routes them).
    p.LineNumber = 0
    p.tpx = 0
    p._Sym = 0
    p.pd1len = 0
    p.t2 = 0
    p.nflg = 0
    p.Rargsx = 0
    p.ERROR = 0
    p.gpad = 0

    for (i in 0 until SYM_MAX) p.start[i] = null

    p.order = 0
    p._SymPush = 0
    p.hit = 0
    p.count = 0
    p.ScanMinWordSize = 3
    p.ScanMaxWordSize = 25
    p.fdes[0] = -1
    p.fdes[1] = -1
    p.exec = 0
    p.reference[0] = '\u0000' // call by ref original name
    writeC(p.CurrentFile, "")

    // #if defined(SQLITE) - the reference build arm.
    writeC(p.Col_Names, "")
    writeC(p.pgsql_msg, "")
    writeC(p.Table, "mumps")
    p.TabSize = 14

    // #else - the MumpsNativeDatabase getenv/cfgdata/cfgkey/
    // UDAT/UKEY arm is compiled out (reference SQLITE build).

    return p
}
