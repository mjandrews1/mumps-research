package mumps.interp

/**
 * PRO-arm translation of mpstime.cpp::<misc> (roster row 253,
 * batch 9; units.csv: loc 98, comment_ratio 0.347,
 * pointer_marks 30, control_marks 12, score 90.4, rank 270).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 39-146 - the residual chunk of mpstime.cpp after
 * the main unit (B6 tranche A, unit 158, MpstimeMain.kt).
 *
 * Residue contents, each line mapped onto its seat:
 *  - :39 #undef _MDH_; the includes :40-61 - the mpstime TU
 *    prologue; every include resolves to existing carriers
 *    (defines.h/sym.h/btree.h/globalOrder.h/stateVector.h via
 *    the state precedents; keyfix.h keyfix, mdh).
 *  - :54-58 FORPARM/CSTR/UNS/uns/CCHR macros - per-unit macro
 *    consumers translate their own macros (FORPARM seat:
 *    MpstimeMain.kt h007 note).
 *  - :62-77 the prototype roster (fcn/zfcn/sym_/makeLock/
 *    cleanLocks/releaseLock/eraseLocks/add/sub/mult/divx/divi/
 *    _fnumber) - real seats: the InterpFcn/InterpSysfuncMisc
 *    families (zfcn), LibmpscppMisc7 (makeLock/cleanLocks,
 *    batch 8), sym_ reserved for row 297 (batch 10).
 *  - :79 int DBSem = 0 - MumpsGlobals.DBSem (u130 h004 seat;
 *    the same-named mumps.cpp global already serves).
 *  - :81-101 the inline helpers floatstring/numcomp2/strxcat/
 *    strcatz - RENDERED below (strxcat/strcatz only; the
 *    floatstring/numcomp2 REAL seats are the ArithMisc.kt
 *    top-level pair, u48 Numcomp family - the same-name
 *    cross-TU siblings are served there, h015 note; the
 *    MpstimeMain strxcat stub is resolved here this batch).
 *  - :107-109 SyncClock - identical shape to the mumps.cpp
 *    SyncClock already rendered at the u133 MumpsMain seat;
 *    no new code, that binding serves both TUs.
 *  - :111-121 set_alarm - RENDERED below (the itimer syscall
 *    quantizes; the only call site is the vendor's own "if (0)"
 *    dead block, kept dead per MpstimeMain h013).
 *  - :126-131 the Mglobal interface comment; :137-146 the
 *    global.h/fcns.h/sysfunc.h includes (#ifndef SUB arm IN).
 *  - :155+ the zmain banner comment - MpstimeMain seat.
 *  - :215-221 the signal/SIGALRM dead block and the
 *    __label_lookup/__text_function default bindings - kept as
 *    the MpstimeMain comments (function pointers fold onto the
 *    Interpret family roster note).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param  : setitimer(ITIMER_REAL) has no JVM
 *    analog; the vendor's value semantics (set the repeating
 *    alarm to zero, return the previous remaining time)
 *    quantize to a no-op returning 0 - journaled divergence.
 *  - h012 unsigned_byte_domain: the string helpers take
 *    NUL-domain CharArrays at offset 0.
 *  - h014 out_of_scope_call: atof/atoi fold onto Kotlin
 *    toDouble/toInt (the FLOATSCAN h009 note); strcatz appends
 *    the SOH marker, 1.toChar() (u158 precedent).
 *  - h017 naming          : every helper keeps its vendor name.
 *  - h018 preprocessor_conditional: #ifndef SUB resolves IN for
 *    the reference build (fcns.h/sysfunc.h seat roster above).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - strxcat returns the offset of a's terminating NUL (the
 *    vendor returns the advanced pointer; the Kotlin carrier
 *    returns the array).
 *  - strcatz appends SOH then NUL; the caller's window carries
 *    the one-past end (kept).
 *  - floatstring returns 0 on any non-numeric char before the
 *    first '.'/'e' even if digits follow - the vendor early
 *    return kept verbatim at the ArithMisc seat.
 */

/**
 * C: inline char * strxcat(char * a, char * b) (:95-101) -
 * catenate b onto a; returns a (roster-resolved seat; the
 * MpstimeMain stub removed, call sites bind unchanged).
 */
fun strxcat(a: CharArray, b: CharArray): CharArray {
    var i = 0
    while (a[i] != NUL) i++
    var j = 0
    while (b[j] != NUL) {
        a[i] = b[j]
        i++
        j++
    }
    a[i] = NUL
    return a
}

/**
 * C: inline char * strcatz(char * out, char * in) (:103-110) -
 * catenate in onto out and append the SOH marker (the
 * subscript terminator).
 */
fun strcatz(out: CharArray, `in`: CharArray): CharArray {
    var i = 0
    while (out[i] != NUL) i++
    var j = 0
    while (`in`[j] != NUL) {
        out[i] = `in`[j]
        i++
        j++
    }
    out[i] = 1.toChar() // *(out++) = '\u0001' (SOH)
    i++
    out[i] = NUL
    return out
}

/**
 * C: unsigned int set_alarm(unsigned int seconds) (:111-121) -
 * the reference platform's ITIMER_REAL arm/set handshake;
 * quantized (h001 note above); the only vendor call site is
 * dead (h013).
 */
fun set_alarm(seconds: Int): Int {
    // vendor: the ITIMER_REAL setitimer handshake (set the
    // alarm to zero, return the previous remaining time); the
    // JVM has no process itimer - quantized to the no-op,
    // returning 0 (the "setitimer ok" arm's it_value.tv_sec of
    // a fresh zeroed timer).
    return 0
}
