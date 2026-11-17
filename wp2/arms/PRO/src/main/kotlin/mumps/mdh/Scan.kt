package mumps.mdh

import mumps.interp.Msv
import mumps.interp.NUL

/**
 * PRO-arm translation of libmpscpp.cpp::Scan (B3 unit 81, rank 123).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 10038-10059.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::Scan, cpp, loc 16, comment_ratio
 * 0.059, pointer_marks 4, control_marks 2, score 12.6, batch 3 -
 * exact match to wp1/study-subset/units.csv row 123.
 *
 * char* Scan(MSV* svptr): reads the next whitespace-delimited token
 * (fscanf "%1024s") from the current input unit into the static
 * scratch buffer; EOF clears the buffer and sets tpx=0, a read
 * token sets tpx=1.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch    : vendor "static char tmp[1024]"
 *    persists across calls (ScanAlnum, unit 79, consumes the
 *    alias) - carried on the ScanScratch object (ScanAlnumScratch
 *    precedent).
 *  - h012 unsigned_byte_domain: tmp is a Char(0..255) domain;
 *    in_file[io] is the Msv Reader edge (h001 carrier).
 *  - h013 dead_store        : the vendor's "if (i >= 1023)" arm is
 *    unreachable - fscanf returns the item count (0 or 1), never
 *    the token length, so the word-too-long guard can never fire
 *    in the vendor either; kept in shape as a comment, not a live
 *    branch.
 *  - h014 out_of_scope_call : GlobalExceptionCode/throw
 *    MumpsGlobalException -> MdhState (unit 31) / MdhStubs carrier
 *    (unit 31); the MdhStubs Scan stub raised by unit 79 is
 *    resolved by this unit and removed (cleanLocks precedent,
 *    unit 56).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level Scan - no collisions
 *    (the vendor has no other Scan; ScanAlnum is distinct).
 *  - h017 naming           : Scan keeps the vendor name; the
 *    char* return is the scratch CharArray (NUL-terminated).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - fscanf("%1024s") skips leading whitespace; EOF before any
 *    token returns EOF, and the vendor clears tmp[0]=0 only on
 *    that arm - a token read leaves the previous tail beyond the
 *    NUL intact (kept; tmp[1023]=0 is the only tail write).
 *  - The word-too-long check compares the fscanf ITEM COUNT
 *    against 1023, which is always false (see h013) - the INPUT_
 *    ERROR throw below it is unreachable in the vendor binary.
 */

/** vendor: static char tmp[1024] - persists across calls (h008). */
object ScanScratch {
    val tmp: CharArray = CharArray(1024)
}

private const val INPUT_ERROR = 6 // vendor libmpscpp.h:75

fun Scan(svptr: Msv): CharArray {
    val tmp = ScanScratch.tmp

    // vendor: fscanf(in_file[io], "%1024s", tmp) - skip whitespace,
    // read one token (at most 1023 chars + NUL). An unopened unit
    // (null Reader) is the h001 null-file edge (DirectRead
    // precedent, B1): empty token, tpx=0.
    val r = svptr.in_file[svptr.io]
    if (r == null) {
        tmp[0] = NUL
        svptr.tpx = 0
        return tmp
    }
    var i = 0
    var c = r.read()
    while (c != -1 && (c == 32 || c == 9 || c == 10 || c == 13 || c == 12 || c == 11)) {
        c = r.read()
    }
    if (c == -1) { // fscanf == EOF
        tmp[0] = NUL
        svptr.tpx = 0
        return tmp
    }
    while (c != -1 && c != 32 && c != 9 && c != 10 && c != 13 && c != 12 && c != 11 && i < 1023) {
        tmp[i] = c.toChar()
        i++
        c = r.read()
    } // fscanf assigned one item here (i == 1 in the vendor's variable)

    // vendor: if (i >= 1023) { GlobalExceptionCode=INPUT_ERROR; throw
    // MumpsGlobalException(); } - i is the fscanf item count (0/1),
    // so this arm never fires in the vendor binary (h013).

    svptr.tpx = 1
    tmp[i] = NUL
    tmp[1023] = NUL // vendor: tmp[1023] = 0

    return tmp
}
