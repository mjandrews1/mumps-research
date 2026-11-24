package daf

// Unit 244 (DAF B8 T3, complex): interp-sysfunc.cpp::AllocSV (103
// LOC, score 164.9): the state-vector constructor - SEAT
// RELEASED: the allocSVDaf named-owner stub (raised with the
// roster-b8 seat ledger) is deleted from DafInterpStubsT3 with a
// release note; the call sites (Fcn.kt:189, GlobalQuery.kt:44,
// InterpService.kt:145, InterpSymQsub.kt:48) resolve here with the
// same name and signature. The vendor mallocs a FRESH MSV on
// every call after a once-only first-call gate that carries the
// POSIX signal-registration block only; the gate is kept so
// call-order behavior matches (H-018). The NULL return arm is
// dead in the JVM domain (H-013, the unit-211 Sigint precedent).
// Consults (2026-11-24T21:24:00Z, nominal): memset family and
// reference[0] construction-zeroed (H-018, in situ notes);
// SQLITE arm live on the reference build (the unit-202 ruling);
// console-default handle fold value 1 (the MpstimeMain row-18
// precedent); the vendor signal list is carried by the unit-211
// render.
// Dafny: EXEMPT (struct-init plumbing; journal row).

// static struct MSV *p1 = NULL: the once-only first-call gate of
// the vendor (the signal-registration block rides it).
private var allocSvFirstCallD = true

fun allocSVDaf(): DafMsv {

    if (allocSvFirstCallD) {
        // first-call signal-registration block (once-only in the
        // vendor): SIGHUP ignored, then SIGINT/SIGQUIT/SIGILL/
        // SIGTRAP/SIGABRT/SIGIOT/SIGBUS/SIGFPE/SIGKILL/SIGUSR1/
        // SIGSEGV/SIGUSR2/SIGPIPE/SIGTERM/SIGSTKFLT/SIGXCPU/
        // SIGXFSZ/SIGPWR/SIGSYS all bound to sigint (the unit-211
        // InterpSysfuncSigint render); SIGALRM stays the
        // commented-out vendor fossil. JVM domain: POSIX handlers
        // cannot be installed; the block renders as this ledger,
        // the gate itself is kept.
        allocSvFirstCallD = false
    }

    val p1 = DafMsv() // malloc fold: fresh state vector per call

    // if (p1==NULL) { printf("*** Unable to allocate MSV\n");
    //   return NULL; } - dead arm in the JVM domain (H-013; the
    //   malloc-NULL precedent of the unit-211 svPtr-null ruling).

    p1.LockDone = 0
    p1.io = 5
    p1.ierr = 0
    // memset(bd/pd1/v1d/xd/setname, '\0', ...) - the CharArray
    // carriers are construction-zeroed (H-018, structurally
    // satisfied; noted, not re-emitted).
    p1.symflg = 0
    p1.ITRAP = 0
    p1.setpiece = 0
    p1.t0px = 0
    p1.xpx = 0
    p1.nstart = 0 // NULL fold (int handle domain)
    p1.ctrlc = 0
    p1.iorg = 1
    p1.symlen = 0
    p1.NOERR = 0

    for (i in 0 until 10) {
        // p1->in_file[i]=NULL; p1->out_file[i]=NULL; file ptrs
        p1.in_file[i] = 0
        p1.out_file[i] = 0
        p1.hor[i] = 0 // horizontal cursors
        p1.ver[i] = 1 // vertical cursors
    }

    // p1->in_file[5]=stdin; p1->out_file[5]=stdout; - console
    // default (the IntArray handle-domain fold, value 1; the
    // MpstimeMain row-18 precedent; actual device IO routes
    // through the per-unit Reader/Writer seats, e.g. the unit-234
    // SYSIN_READER_234).
    p1.in_file[5] = 1
    p1.out_file[5] = 1
    p1.LineNumber = 0
    p1.tpx = 0
    p1._Sym = 0
    p1.pd1len = 0
    p1.t2 = 0
    p1.nflg = 0
    p1.Rargsx = 0
    p1.ERROR = 0
    p1.gpad = 0

    for (i in 0 until SYM_MAX_D) p1.start[i] = 0 // =NULL (layer stack)

    p1.order = 0
    p1._SymPush = 0
    p1.hit = 0
    p1.count = 0
    p1.ScanMinWordSize = 3
    p1.ScanMaxWordSize = 25
    p1.fdes[0] = -1
    p1.fdes[1] = -1
    p1.exec = 0
    // p1->reference[0]=0; call by ref original name -
    // construction-zeroed carrier (H-018).
    strcpyAllocD(p1.CurrentFile, "")

    // #if defined(SQLITE) - live on the reference build (the
    // unit-202 sql family ruling):
    strcpyAllocD(p1.Col_Names, "")
    strcpyAllocD(p1.pgsql_msg, "")
    strcpyAllocD(p1.Table, "mumps")
    p1.TabSize = 14

    // #else getenv("MumpsNativeDatabase") cfgdata/cfgkey/UDAT/UKEY
    // arm and the data.dat/key.dat fallback: INERT on the
    // reference build (SQLITE defined; H-018 inert-arm ledger);
    // the native-btree carriers (cfgdata/cfgkey/UDAT/UKEY) keep
    // their construction-zero state, matching the vendor's
    // dead-config build.

    return p1
}

// file-local strcpy twin (the b1 CRuntime precedent).
private fun strcpyAllocD(dst: CharArray, src: String) {
    for (t in src.indices) dst[t] = src[t]
    dst[src.length] = NUL_D
}
