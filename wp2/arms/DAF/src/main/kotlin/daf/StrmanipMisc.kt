package daf

// Unit 137 (DAF B5 T2, core): interp-strmanip.cpp::<misc>
// (units.csv row 137: loc 36, ptr 6, score 28.0, rank 199, core):
// the strmanip residual chunk - the LGPL/copyright banner and the
// stdio/string/stdlib include rows ride here as KDoc residue per the
// T1 banner precedent (the wrapped bodies own their roster cells);
// the chr banner-note row and the sigint prototype row are ledgered
// below. Three executable bodies land:
//   rindex(s, c)   - the highest-index scan (NOT libc rindex: vendor
//                    defines its own in this TU); consult BASE-log
//                    row 33 family hit-reuse (pointer arithmetic
//                    collapses to the found-index Int; the C NULL
//                    return renders as -1).
//   strcatx(out,in)- the \x01-terminated append; consult BASE-log
//                    row 70 hit-reuse (strcatx translated for real
//                    rather than stubbed, with the trailer separator
//                    byte - the BASE Input precedent; the NUL
//                    semantics render over the CharArray carriers:
//                    scan to the first NUL, append the input then
//                    the \u0001 separator then a fresh NUL).
//   lpad(...)      - the in-place left-pad; TAG CORRECTION + FLIP:
//                    the DafInterpStubsT3 seat said owner
//                    interp-arith.cpp (the B4-T3 misconstruction;
//                    grep-verified: no lpad body exists in
//                    interp-arith.cpp); the owner is THIS unit
//                    (interp-strmanip.cpp), so the seat lpadDaf
//                    flips to delegate below-render with the
//                    signature mapping noted at the seat.
// sigint prototype row: owner stays with the named-owner seat
// rideGlobalNodeStub carries (sigintDaf, owner
// interp-sysfunc.cpp::sigint, a roster row of batch 10).
// Dafny: EXEMPT (raw-buffer surgery seats; journal row).

// char *rindex(char *s, int c) - vendor-local highest-index scan;
// NULL (no hit) renders as -1 per the row-33 index fold.
fun rindexD(s: CharArray, c: Char): Int {
    var i = 0
    while (at(s, i) != NUL_D) i++
    // i = strlen(s); the vendor then scans j = i down to 0.
    var j = i
    while (j >= 0) {
        if (c == at(s, j)) {
            return j // &s[j]
        }
        j--
    }
    return -1 // NULL
}

// inline void strcatx(unsigned char *out, unsigned char *in):
// append the input at the first NUL, terminate with \x01 then NUL.
fun strcatxD(out: CharArray, in_: CharArray) {
    var o = 0
    while (at(out, o) != NUL_D) o++ // for (; *out!=0; out++);

    var i = 0
    while (at(in_, i) != NUL_D) { // for (; *in!=0; in++) *(out++)=*in;
        out[o] = in_[i]
        o++
        i++
    }
    out[o] = '\u0001' // *(out++)='\x01';
    o++
    out[o] = NUL_D // *out='\0';
}

// void lpad(unsigned char strin[], unsigned char strout[], short l):
// right-shift the string by the pad deficit and fill with spaces.
// In-place call shape (strin == strout, the Fcn.kt lpadDaf site)
// works: the vendor reads strin[i] AFTER writing strout[i+k], but
// only at indices that the right-shift has not yet overwritten
// (i+k > i always, k > 0 guarded), so the aliasing is safe exactly
// as in C - logged manual-decision, no BASE row for the alias
// reasoning.
fun lpadStrmanipD(strin: CharArray, strout: CharArray, l: Int) {

    val j = DafCRuntime.cstring(strin).length - 1
    val k = l - j - 1
    if (k <= 0) return

    var i = j
    while (i >= 0) { // for (i = j; i >= 0; i--)
        strout[i + k] = strin[i]
        i--
    }
    strout[j + k + 1] = NUL_D // strout[j + k + 1] = 0;

    i = 0
    while (i < k) { // for (i = 0; i < k; i++) strout[i] = ' ';
        strout[i] = ' '
        i++
    }
}
