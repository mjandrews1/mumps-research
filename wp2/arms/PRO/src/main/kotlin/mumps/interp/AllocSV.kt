package mumps.interp

/**
 * PRO-arm translation of interp-sysfunc.cpp::AllocSV
 * (batch-8 unit 219, units.csv data line 219, rank 285).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 528-651.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-sysfunc.cpp::AllocSV, cpp, loc 103,
 * comment_ratio 0.01, pointer_marks 69, control_marks 6,
 * score 164.9, rank 285, batch 8 - exact match to
 * wp1/study-subset/units.csv line 219.
 *
 * AllocSV(): allocate and fully initialize a fresh state
 * vector (struct MSV).  This unit RESOLVES the InterpStubs
 * allocSV() h014 stub (raised by unit 98 with a "not a corpus
 * unit" tag - tag CORRECTED, u141/u148/u169 family: AllocSV is
 * roster row 219, batch 8; the stub now delegates here, and
 * the Qsub/Sigint/SqlBtree call sites bind unchanged).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h003 state_macro    : the Msv constructor's zero/empty
 *    defaults carry the vendor memsets (bd/pd1/v1d/xd, the
 *    SYM_MAX start sweep); the non-default assignments below
 *    are exactly the vendor's divergent fields.
 *  - h004 globals_ext    : static struct MSV *p1 - the
 *    first-call signal-registration gate rides the
 *    AllocSVState.seen flag (one-shot semantics kept).
 *  - h008 static_scratch : as above (Sigint unit precedent).
 *  - h014 out_of_scope_call: signal()/SIGHUP/SIG_IGN machinery
 *    has no JVM analog - the vendor registration list is kept
 *    as a documented note, not wired (the sigint handler seat
 *    itself is unit 191).
 *  - h018 preprocessor_conditional: the SQLITE arm is LIVE on
 *    the reference build (Col_Names/pgsql_msg emptied, Table
 *    "mumps", TabSize 14 - SqlBtree unit-217 precedent); the
 *    #else MumpsNativeDatabase getenv arm (cfgdata/cfgkey/
 *    UDAT/UKEY) is EXCLUDED, kept as a note.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The signal list registers a handler for SIGKILL, which
 *    POSIX never delivers to a handler (fossil, kept as note).
 *  - signal(SIGALRM,...) is commented out in the vendor -
 *    kept out.
 *  - The malloc-NULL arm (printf + return NULL) cannot occur
 *    in the JVM carrier (constructor never fails) - dropped,
 *    Sigint unit-191 "dummy vector" precedent.
 *  - memset(setname,0,256) clears only the first 256 bytes of
 *    the STR_MAX setname buffer - the carrier's full buffer is
 *    already zeroed (fold, journaled).
 *  - reference[0]=0 and the strcpy(CurrentFile,"") /
 *    strcpy(Col_Names,"") / strcpy(pgsql_msg,"") forms fold to
 *    the carrier's zeroed arrays.
 *  - in_file[5]=stdin / out_file[5]=stdout: the carrier's
 *    Reader/Writer seats take the reflective System.in fetch
 *    (unit-188 precedent) and a stdout Writer (h001 edge).
 */

/**
 * Carrier for the vendor file-scope static struct MSV *p1 of
 * AllocSV (h004/h008): the first call registers the signal
 * handlers (one-shot), every call allocates a fresh vector.
 */
private object AllocSVState {
    /** vendor: static struct MSV *p1 = NULL - first-call gate. */
    var seen: Boolean = false
}

fun AllocSV(): Msv {
    if (!AllocSVState.seen) {
        AllocSVState.seen = true
        // vendor one-shot signal registration (no JVM analog,
        // kept as note, h014):
        //   signal(SIGHUP,SIG_IGN); signal(SIGINT,&sigint);
        //   signal(SIGQUIT,&sigint); signal(SIGILL,&sigint);
        //   signal(SIGTRAP,&sigint); signal(SIGABRT,sigint);
        //   signal(SIGIOT,&sigint); signal(SIGBUS,&sigint);
        //   signal(SIGFPE,&sigint); signal(SIGKILL,sigint);
        //   [SIGKILL: POSIX never delivers it - fossil]
        //   signal(SIGUSR1,&sigint); signal(SIGSEGV,&sigint);
        //   signal(SIGUSR2,&sigint); signal(SIGPIPE,&sigint);
        //   [signal(SIGALRM,...) commented out in vendor]
        //   signal(SIGTERM,&sigint); signal(SIGSTKFLT,&sigint);
        //   signal(SIGXCPU,&sigint); signal(SIGXFSZ,&sigint);
        //   signal(SIGPWR,&sigint); signal(SIGSYS,&sigint);
    }

    // vendor: p1 = (struct MSV *) malloc(sizeof(struct MSV));
    //         [the malloc-NULL arm cannot occur - dropped]
    val p1 = Msv()

    // vendor: p1->LockDone=0; p1->io=5; p1->ierr=0; [memsets];
    //         p1->symflg=0; p1->ITRAP=0; p1->setpiece=0;
    //         p1->t0px=0; p1->xpx=0; p1->nstart=NULL;
    //         p1->ctrlc=0; p1->iorg=1; p1->symlen=0;
    //         p1->NOERR=0;  [constructor zero-defaults fold]
    p1.io = 5
    p1.iorg = 1

    // vendor: for (i=0;i<10;i++) { in_file[i]=NULL;
    //         out_file[i]=NULL; hor[i]=0; ver[i]=1; }
    for (i in 0 until 10) {
        p1.hor[i] = 0
        p1.ver[i] = 1
    }

    // vendor: p1->in_file[5]=stdin; p1->out_file[5]=stdout;
    // (reflective System.in fetch, unit-188 precedent; h001 edge)
    val stdin = System::class.java.getField("in").get(null) as java.io.InputStream
    p1.in_file[5] = java.io.InputStreamReader(stdin)
    p1.out_file[5] = java.io.OutputStreamWriter(java.io.PrintStream(java.io.FileOutputStream(java.io.FileDescriptor.out)))

    // vendor: p1->LineNumber=0; tpx=0; _Sym=0; pd1len=0; t2=0;
    //         nflg=0; Rargsx=0; ERROR=0; gpad=0;
    //         for (i=0;i<SYM_MAX;i++) p1->start[i]=NULL;
    //         p1->order=0; _SymPush=0; hit=0; count=0;
    //         [constructor zero-defaults fold]

    p1.ScanMinWordSize = 3
    p1.ScanMaxWordSize = 25

    // vendor: p1->fdes[0]=-1; p1->fdes[1]=-1; p1->exec=0;
    //         p1->reference[0]=0; strcpy(p1->CurrentFile,"");
    p1.fdes[0] = -1
    p1.fdes[1] = -1

    // h018: SQLITE arm LIVE (reference build) - the #else
    // MumpsNativeDatabase getenv arm (cfgdata/cfgkey/UDAT/UKEY)
    // is EXCLUDED, kept as note.
    // vendor: strcpy(p1->Col_Names,""); strcpy(p1->pgsql_msg,"");
    //         strcpy(p1->Table,"mumps"); p1->TabSize=14;
    writeCstring(p1.Col_Names, "")
    writeCstring(p1.pgsql_msg, "")
    writeCstring(p1.Table, "mumps")
    p1.TabSize = 14

    return p1
}
