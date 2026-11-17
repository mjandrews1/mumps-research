package mumps.interp

/**
 * PRO-arm translation of interp-strmanip.cpp::<misc> (B5 tranche B,
 * unit 136, rank 199). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): the extractor claims substr (19 physLOC, batch-7
 * unit) and xindex (25 physLOC, batch-9 unit) as their own units;
 * the residual misc is 36 physLOC, comment_ratio 0.561,
 * pointer_marks 6, control_marks 9, score 28.0 - exact match to
 * wp1/study-subset/units.csv row 137 (batch 5).
 *
 * The chunk is the non-contiguous residue: the file header
 * (vendor 1-40), the MSVC-only rindex (43-53 inside the ifdef pair
 * at 42/54), strcatx (122-130) and lpad (135-149).
 *
 * Declaration ledger (vendor line -> disposition):
 *  :40 void sigint(int)     -> DONE: InterpStubs stub (batch-7
 *     owner interp-sysfunc.cpp).
 *  :42-54 rindex ifdef pair -> h018 trichotomy: the WHOLE rindex
 *     body sits inside pair-ifdef MSVC, which is UNDEFINED in the
 *     reference build (Linux/gcc) - the arm is EXCLUDED, rendered
 *     as this ledger comment only (u125 excluded-arm precedent);
 *     callers bind the libc rindex/strrchr. Roster comment only,
 *     no seat raised.
 *  :59-82 substr           -> NOT this unit: batch-7 corpus unit
 *     (units.csv rank 45); DONE as the InterpStubs stub.
 *  :87-117 xindex          -> NOT this unit: batch-9 corpus unit
 *     (units.csv rank 114); DONE as the InterpStubs stub.
 *  :122-130 strcatx        -> REAL BODY here; RESOLVES the
 *     MdhStubs strcatx stub (whose 'libmpscpp.cpp owner' tag was
 *     wrong - libmpscpp.cpp only CALLS strcatx at 6978+; the
 *     single C definition is this file, u127 rpm tag-correction
 *     precedent). InterpSysfuncMisc.kt rebinds its import.
 *  :135-149 lpad           -> REAL BODY here; RESOLVES the
 *     InterpStubs lpad stub (whose 'not a corpus unit' tag was
 *     wrong - lpad rides this misc chunk, 14 physLOC below the
 *     15-LOC function threshold, u127 rpm precedent).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h012 unsigned_byte_domain: out/in/strin/strout are C byte
 *    domains (CharArray, NUL-terminated regions off the offset
 *    carriers); the \x01 subscript separator renders as
 *    1.toChar().
 *  - h014 out_of_scope_call  : this unit RESOLVES the strcatx
 *    (MdhStubs) and lpad (InterpStubs) stubs - call sites bind
 *    unchanged on the stub signatures (Fcn.kt:678 lpad,
 *    InterpSysfuncMisc.kt:194/199 strcatx after the import
 *    rebind); strlenC is a DONE shared helper.
 *  - h015 grammar_collision  : pre-push scan incl. cross-file
 *    top-level names: new top-levels strcatx, lpad - the removed
 *    stubs clear their seats; the mdh-package NUL privates are
 *    per-file (no collision); the removed mdh strcatx leaves the
 *    mdh name free for any future owner seat.
 *  - h017 naming             : in keyword -> in_ (u129 FILE *in
 *    precedent).
 *  - h018 if_else_resolved  : the MSVC ifdef pair at :42-54
 *    resolves to the EMPTY arm - the whole rindex body is
 *    excluded in the reference build.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - rindex's excluded body would start the scan at s[strlen(s)]
 *    (the NUL seat itself) - recorded as a roster comment; the
 *    excluded arm is never exercised in the reference build.
 *  - strcatx has no bounds check anywhere (C buffer overrun UB):
 *    the Kotlin carriers throw the deterministic
 *    IndexOutOfBounds where the vendor wrote past the buffer -
 *    quantized, documented, not widened (u128 NUL-walk
 *    precedent).
 *  - lpad's k <= 0 early return leaves strout UNTOUCHED - the
 *    caller's buffer is left with its prior contents (observable
 *    vendor quirk kept verbatim, not "fixed" to zero the output).
 *  - lpad assigns size_t strlen into a short (j): the >32767
 *    wraparound is the vendor's truncation, kept via toShort().
 */

/**
 * vendor 122-130: inline void strcatx(unsigned char * out,
 * unsigned char * in) - walk out to its NUL, append in's bytes
 * up to (not including) in's NUL, then the \x01 subscript
 * separator and a fresh NUL.
 *
 * RESOLVES the MdhStubs strcatx stub (tag corrected: libmpscpp.cpp
 * only calls it; this file owns the single definition).
 */
fun strcatx(out: CharArray, in_: CharArray) {
    var o = 0
    while (out[o] != NUL) o++
    for (n in in_.indices) {
        if (in_[n] == NUL) break
        out[o++] = in_[n]
    }
    out[o++] = 1.toChar() // \x01 subscript separator (h012)
    out[o] = NUL
}

/**
 * vendor 135-149: void lpad(unsigned char strin[], unsigned char
 * strout[], short l) - right-shift strin by k = l - len + ... :
 * j = strlen(strin) - 1 (short), k = l - j - 1; k <= 0 returns
 * with strout UNTOUCHED; otherwise the input lands at offset k,
 * padded with k leading spaces and NUL-closed.
 *
 * RESOLVES the InterpStubs lpad stub (tag corrected: rides this
 * misc chunk). Signature keeps the stub's offset carriers so the
 * Fcn.kt:678 call site (svPtr.bd at offset 1) binds unchanged.
 */
fun lpad(strin: CharArray, strinOff: Int, strout: CharArray, stroutOff: Int,
         l: Short) {
    var j = (strlenC(strin, strinOff) - 1).toShort()
    val k = (l - j - 1).toShort()
    if (k <= 0) return // strout left untouched (vendor quirk, kept)

    var i = j
    while (i >= 0) {
        strout[stroutOff + i + k] = strin[strinOff + i]
        i--
    }
    strout[stroutOff + j + k + 1] = NUL
    i = 0
    while (i < k) {
        strout[stroutOff + i] = ' '
        i++
    }
}
