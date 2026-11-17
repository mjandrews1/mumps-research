package mumps.interp

/**
 * PRO-arm translation of interp-sysfunc.cpp::TimeOut_getstr1
 * (B6 tranche B, unit 170, rank 135). Translated independently
 * from the pristine vendor source (mumps-14-may-2026.tgz,
 * sha256 621deb25...ffdbe), reference source lines 127-168.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): TimeOut_getstr1, cpp, loc 33, comment_ratio 0.029,
 * pointer_marks 1, control_marks 5, score 14.1 - exact match to
 * wp1/study-subset/units.csv row 171 (batch 6).
 *
 * TimeOut_getstr1(io, area, timeout, dirflg): the timed READ
 * runtime - the u159 read_code compiler EMITS this call (the
 * generated C text), and this unit is the interpreter-side body.
 * Only unit 5 is permitted (else the banner and sigint(100)
 * abort); fd 0 is watched by select() for timeout seconds; a
 * timeout lands an empty area and returns 0; a ready fd reads
 * ONE raw char (dirflg set, stty raw/-raw around it) or a full
 * line with the trailing byte stripped (fgets + the
 * area[strlen-1]=0 chop) and returns 1.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param     : printf renders println (console
 *    edge, u158/u164 banner precedent); the fd-0 select/read
 *    surface is the PROCESS-LEVEL OS layer, not an arm-owned
 *    stream - per-file private stub carriers (u161 server.cpp
 *    precedent: OS syscalls are never arm-owned).
 *  - h012 unsigned_byte_domain: area/timeout are C string byte
 *    domains (CharArray); atoi rides the shared CstringOps
 *    atoiC offset-carrying seat; the vendor's bare scope block
 *    (134-167) around the fd_set/timeval declarations is a C
 *    declaration-scope artifact - linearized away, the read
 *    contract unchanged.
 *  - h014 out_of_scope_call   : sigint is the existing
 *    InterpStubs stub seat (batch-7 owner, u158 precedent -
 *    direct call, binds unchanged); select/stty(getchar arm)/
 *    fgets-on-stdin are per-file private OS stubs raised here
 *    (u161 ftok/msgget/msgrcv precedent).
 *  - h015 grammar_collision   : pre-push scan incl. cross-file
 *    top-level names: TimeOut_getstr1 is a new top-level - the
 *    SysfuncH/CppsysfuncH roster notes anticipated it; no
 *    collision.
 *  - h017 naming              : the vendor mixed-case
 *    TimeOut_getstr1 keeps its exact spelling (SysfuncH
 *    vendor-verbatim note).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The io!=5 banner PRINTS and calls sigint(100) but does NOT
 *    return or skip the read - the vendor falls through into the
 *    select on fd 0 anyway; kept verbatim (sigint's abort makes
 *    the fall-through unreachable in practice, u137
 *    Nothing-carrier precedent).
 *  - dirflg's raw-mode read stores the getchar() result
 *    unmasked: a -1 EOF stores the char cast of -1 (0xFF in the
 *    byte domain) - kept, quantized to the Char carrier.
 *  - The fgets arm strips the LAST byte of area unconditionally
 *    (the newline); if fgets returns NULL (EOF/none ready after
 *    the select race) the vendor still runs
 *    area[strlen(area)-1]=0 on the CALLER's prior buffer
 *    contents - the stale-buffer chop is kept verbatim (h012
 *    byte-domain note; the IndexOutOfBounds carrier bounds the
 *    empty-buffer case).
 *  - tv.tv_sec = atoi(timeout), tv.tv_usec = 0 - whole-second
 *    timeouts only, kept.
 */
private const val TOG_STR_MAX = 32000 // vendor strmax.h:3 (file-private)

fun TimeOut_getstr1(io: Int, area: CharArray, timeout: CharArray, dirflg: Int): Int {
    if (io != 5) {
        // vendor: printf("*** Timed read only permitted to unit 5\n")
        println("*** Timed read only permitted to unit 5")
        sigint(100) // InterpStubs stub seat - the fall-through below is kept (see KDoc)
    }

    // vendor: tv.tv_sec = atoi(timeout); tv.tv_usec = 0;
    val secs = atoiC(timeout)

    if (dirflg != 0) osSystemSttyRaw(true) // vendor: system("stty raw")

    // vendor: i = select(1,&rfds,NULL,NULL,&tv) on fd 0
    val i = osSelectReadable(secs)

    if (i == 0) {
        area[0] = NUL // vendor: strcpy(area,"")
        if (dirflg != 0) osSystemSttyRaw(false) // vendor: system("stty -raw")
        return 0
    }

    if (dirflg != 0) {
        val c = osGetchar() // vendor: i = getchar()
        area[0] = c.toChar() // unmasked EOF -1 stores the 0xFF-shape char
        area[1] = NUL
        osSystemSttyRaw(false) // vendor: system("stty -raw")
    } else {
        // vendor: fgets(area,STR_MAX,stdin); area[strlen(area)-1]=0;
        osFgetsStdinLine(area) // stale-buffer chop on the NULL arm, see KDoc
    }

    return 1
}

/** OS syscall layer (u161 server.cpp precedent: process-level OS
 *  surface, never arm-owned) - system("stty raw") / stty -raw. */
private fun osSystemSttyRaw(on: Boolean) {
    TODO("h014 per-file OS stub: owner C library system(\"stty raw|-raw\") (not a corpus unit)")
}

/** OS syscall layer - select(1,&rfds,NULL,NULL,&tv) on fd 0;
 *  returns the select result (0 timeout, nonzero readable). */
private fun osSelectReadable(secs: Int): Int {
    TODO("h014 per-file OS stub: owner C library select on fd 0 (not a corpus unit)")
}

/** OS syscall layer - getchar() on fd 0 (the raw-mode read). */
private fun osGetchar(): Int {
    TODO("h014 per-file OS stub: owner C library getchar on fd 0 (not a corpus unit)")
}

/** OS syscall layer - fgets(area,STR_MAX,stdin) then the
 *  unconditional last-byte chop; kept as one seat so the NULL
 *  arm's stale-buffer chop stays inside the carrier. */
private fun osFgetsStdinLine(area: CharArray) {
    TODO("h014 per-file OS stub: owner C library fgets on stdin (not a corpus unit)")
}
