package mumps.interp

/**
 * PRO-arm translation of interp-sym.cpp::_extract (B6 tranche
 * B, unit 172, rank 126). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 1235-1277.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): _extract, cpp, loc 34,
 * comment_ratio 0.029, pointer_marks 0, control_marks 7,
 * score 13.0 - exact match to wp1/study-subset/units.csv row
 * 173 (batch 6).
 *
 * TAG CORRECTION (u141/u148/u169 precedent): the MdhStubs
 * _extract stub raised by u85-era GlobalExtract was tagged
 * "batch-10 unit" - roster row 173 IS this function, batch 6;
 * the superseded note is kept verbatim in the MdhStubs resolved
 * block. This unit RESOLVES the stub (the GlobalExtract.kt:65
 * call site binds unchanged on the 4-CharArray signature; the
 * mdh import rebind follows the u136 strcatx precedent).
 *
 * _extract(out, in, start, len): the dollar-extract range copy -
 * start/len are 1-based decimal strings: a negative start
 * clamps to 0, else it is decremented; a negative len means "to
 * the end of start" (j = i), else it is decremented; an empty
 * result lands on j < i or i > strlen(in); the i==0 && j==0 arm
 * copies the single in[0]; otherwise the inclusive in[i..j]
 * range lands in out NUL-terminated.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h012 unsigned_byte_domain: out/in/start/len are C string
 *    byte domains (CharArray); atoi rides the shared CstringOps
 *    atoiC seat; strlen rides strlenC (CstringOps).
 *  - h014 out_of_scope_call   : strmove is the existing
 *    InterpStubs stub seat (owner interp-strmanip.cpp, not a
 *    corpus unit) - direct call on the offset signature; the
 *    empty-literal strmove arms pass a one-NUL carrier (the C
 *    string "" is a one-byte 0 domain).
 *  - h015 grammar_collision   : pre-push scan incl. cross-file
 *    top-level names: the new top-level _extract - the removed
 *    mdh stub clears its seat; no other collisions; the vendor
 *    parameter name in collides with the Kotlin keyword and
 *    renders as in_ (u44 val->value precedent).
 *  - h017 naming              : _extract keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The empty-range test is j < i (not <=) - an exactly-one-
 *    element request (j == i) falls through to the copy loop -
 *    kept.
 *  - The bounds test is i > n (not >=): with i == n the copy
 *    loop starts at in[n], the NUL terminator, and writes it
 *    into out[0] - an "empty" output produced by copying the
 *    terminator, kept verbatim (reads in[n] in-bounds in C, the
 *    CharArray carrier is in-bounds the same way).
 *  - j > n clamps j to n AFTER the i > n rejection - the copy
 *    may then run i..n inclusive, reading the NUL as the last
 *    byte - kept.
 */
private val EMPTY_C = CharArray(1) // the C "" literal: one NUL byte

fun _extract(out: CharArray, in_: CharArray, start: CharArray, len: CharArray) {
    var i = atoiC(start) // vendor: i = atoi(start);
    var j = atoiC(len) // vendor: j = atoi(len);

    if (i < 0) {
        i = 0
    } else {
        i--
    }

    if (j < 0) {
        j = i // vendor: negative length means "to the end"
    } else {
        j--
    }

    if (j < i) {
        strmove(out, 0, EMPTY_C, 0) // vendor: strmove(out,"")
        return
    }

    if (i == 0 && j == 0) {
        out[0] = in_[0]
        out[1] = NUL
        return
    }

    val n = strlenC(in_) // vendor: n = strlen(in);

    if (i > n) {
        strmove(out, 0, EMPTY_C, 0) // vendor: strmove(out,"")
        return
    }

    if (j > n) {
        j = n
    }
    var k = 0
    while (i <= j) { // vendor: for (k=0; i<=j; i++) out[k++]=in[i];
        out[k] = in_[i]
        k++
        i++
    }
    out[k] = NUL
    return
}
