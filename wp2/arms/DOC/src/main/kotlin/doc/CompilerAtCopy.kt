package doc

// Unit 222 (B8 tranche 1): compiler-mumps.c::atCopy (batch 8,
// simple stratum; LOC 15, score 6.5, rank 39; units.csv row 241;
// vendor compiler-mumps.c:1956, physically verified this session).
// The at-copy helper of the compiler emitter: copies the xd scan
// buffer from xpx up to the at-flg bound into tmp[2...] with quote
// and backslash escaping, NUL-terminating at the write cursor.
// The j=2 contract is a CALLER prefill: every call site
// (IfCode.kt:54, SetCode.kt:104/121, WriteCode.kt:65, bound through
// the Stubs.atCopy seat) writes tmp[0] and tmp[1] before the call;
// this body keeps the vendor start index verbatim. Consult H-014
// hit-adapt: the helper is a seat flip plus a TAG CORRECTION - the
// stub comment tagged the owner a compiler-at-test.c helper with
// no roster row; the physical owner is compiler-mumps.c:1956 and
// IS a roster unit (units.csv row 241, this row). The xpx/xd
// state rides the CompilerState carrier (xpxIdx() read access),
// quote and backslash escapes kept verbatim; the vendor declares
// int i, j unused-in-else arms (H-013: no dead locals here, both
// drive the loop). Naming per H-017 (atCopy kept; the vendor name
// is already lower-camel).

fun atCopy(at_flg: Int, tmp: CharArray) {
    val xd = CompilerState.xd
    var i = CompilerState.xpxIdx()
    var j = 2

    while (xd[i] != NUL && i < at_flg) {

        if (xd[i] != '\"' && xd[i] != '\\') {
            tmp[j++] = xd[i]
        } else if (xd[i] == '\"') {
            tmp[j++] = '\\'
            tmp[j++] = '\"'
        } else if (xd[i] == '\\') {
            tmp[j++] = '\\'
            tmp[j++] = '\\'
        }

        i++
    }

    tmp[j] = NUL
}
