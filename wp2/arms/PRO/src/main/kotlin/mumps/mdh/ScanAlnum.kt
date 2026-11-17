package mumps.mdh

import mumps.interp.Msv
import mumps.interp.NUL
import mumps.interp.strlenC

/**
 * PRO-arm translation of libmpscpp.cpp::ScanAlnum (B3 unit 79, rank
 * 127). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 10112-10139 (the third of the three vendor overloads).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::ScanAlnum, cpp, loc 17, comment_ratio
 * 0.056, pointer_marks 2, control_marks 5, score 13.1, batch 3 -
 * exact match to wp1/study-subset/units.csv row 127. The overload
 * selection disambiguates against the two sibling ScanAlnum
 * overloads (FILE-ptr/istream tokenizers, loc 18 each - different
 * corpus rows).
 *
 * char* ScanAlnum(MSV* svptr, char* ep, char* toklist = NULL):
 * scans the next token via Scan(svptr), repeats until it starts
 * with an alphanumeric, lowercases the alphanumerics into the
 * static tmp buffer and returns it (tpx=1; tpx=0 with an empty
 * tmp1 on end-of-input).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch    : vendor "static char tmp[1024],
 *    tmp1[1024]" persist across calls - carried on the
 *    ScanAlnumScratch object.
 *  - h012 unsigned_byte_domain: tmp/tmp1 byte domains.
 *  - h013 dead_store        : the parameters ep and toklist (with
 *    its default NULL) are never used in the body - kept in the
 *    signature for call-shape fidelity, documented.
 *  - h014 out_of_scope_call : Scan (libmpscpp.cpp::Scan = B3 unit
 *    81, this batch) -> MdhStubs stub raised by this unit
 *    (cleanLocks precedent: resolved when unit 81 lands);
 *    isalnum/tolower -> local helpers (unsigned-char domain).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level ScanAlnum (the FILE-ptr/istream
 *    overloads are not in the B3 corpus rows here) - no collisions.
 *  - h017 naming            : ScanAlnum keeps the vendor name; the
 *    char* return is the carrier's CharArray (NUL-terminated).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor returns the STATIC tmp (state persists into the
 *    next call) - kept via the scratch object.
 *  - The empty-token arm returns tmp1 (NOT tmp) - the caller gets
 *    the empty Scan result, not the previous token - kept.
 *  - The again-loop re-Scans until a token starts alphanumeric;
 *    a stream of non-alnum tokens loops forever (vendor behavior
 *    kept).
 */
object ScanAlnumScratch {
    /** vendor: static char tmp[1024]. */
    val tmp = CharArray(1024)

    /** vendor: static char tmp1[1024]. */
    val tmp1 = CharArray(1024)
}

fun ScanAlnum(svptr: Msv, ep: CharArray?, toklist: CharArray? = null): CharArray {
    val tmp = ScanAlnumScratch.tmp
    val tmp1 = ScanAlnumScratch.tmp1

    while (true) { // again:
        strcpyOff(tmp1, Scan(svptr))

        if (strlenC(tmp1) == 0) {
            svptr.tpx = 0
            return tmp1
        }

        if (!isAlnumC(tmp1[0])) continue // goto again

        var j = 0
        for (i in 0 until strlenC(tmp1)) {
            if (isAlnumC(tmp1[i])) {
                tmp[j] = tolowerC(tmp1[i])
                j++
            }
        }

        tmp[j] = NUL
        svptr.tpx = 1
        return tmp
    }
}

/** C strcpy(dst, src) over the byte domains. */
private fun strcpyOff(dst: CharArray, src: CharArray) {
    var i = 0
    while (src[i] != NUL) {
        dst[i] = src[i]
        i++
    }
    dst[i] = NUL
}

/** C isalnum (unsigned-char domain). */
private fun isAlnumC(c: Char): Boolean =
    c in 'a'..'z' || c in 'A'..'Z' || c in '0'..'9'

/** C tolower (unsigned-char domain; non-letters unchanged). */
private fun tolowerC(c: Char): Char = if (c in 'A'..'Z') c + 32 else c
