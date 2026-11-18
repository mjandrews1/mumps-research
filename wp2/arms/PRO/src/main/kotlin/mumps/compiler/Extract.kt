package mumps.compiler

import java.io.IOException

/**
 * PRO-arm translation of compiler-parse.c::Extract (B7 tranche 2,
 * unit 198, rank 186). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 2466-2507 (the _extract argument builder
 * after ConCheck in compiler-parse.c).
 *
 * Identity verified against the study tool: compiler-parse.c::
 * Extract, c, loc 30, comment_ratio 0.032, pointer_marks 4,
 * control_marks 9, score 24.2 - exact match to
 * wp1/study-subset/units.csv row 198 (batch 7).
 *
 * Extract(v1d, bp): strips the function name off the front of
 * v1d (the SSS/TTT shift), then walks the remaining cstring,
 * accumulating Extract2-quoted argument fragments (the first
 * with no separator, later ones prefixed ", (UNS) "). On end
 * of string with a in 1..3 it pads the missing -1 args
 * (a==1 gets both, a==2 gets one), emits the
 * _extract(tmpN, ...) line to Out and writes "tmpN" back
 * into bp; returns 0. Returns a itself when a==0 or a>3, and
 * any nonzero a on a mid-loop terminator.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param  : the Out emission via
 *    CompilerState.out.
 *  - h006 low_level_pointer: the file-scope SSS/TTT cursor pair
 *    (vendor lines 180) local to the shift walk.
 *  - h007 char_buffer      : char tmp[1024] argument
 *    accumulator.
 *  - h012 unsigned_byte_domain / data_dependent: the 32..127
 *    name-scan bounds and the a==0||a>3 early returns.
 *  - h014 out_of_scope_call: no prior stub (parse_ owns the
 *    row-199-class seats); Extract2 (unit 15), strcatC,
 *    strcpyC, memcheck (Stubs), cstringAt reused.
 *  - h017 naming           : Extract keeps the vendor name.
 *
 * Vendor notes translated faithfully:
 *  - The name scan starts at i=1 (not 0): the first character
 *    of v1d is skipped, matching vendor line 2471.
 *  - The SSS/TTT shift starts the source cursor at &v1d[i+1]:
 *    the character AT the terminator is dropped with the name.
 *  - iiitmp increments AFTER the emission uses it; bp gets
 *    the pre-increment value (vendor order: fprintf, sprintf,
 *    iiitmp++, memcheck).
 *  - v1d is mutated in place (name removal, mid-string NULs):
 *    the caller's buffer is the aliased one (h006 aliasing).
 *  - a>3 falls through the a==1/a==2 pads and returns a with
 *    NO emission (vendor 2486-2487).
 */
@Throws(IOException::class)
fun Extract(v1d: CharArray, bp: CharArray): Int {
    val st = CompilerState

    var i = 1
    while (v1d[i].code in 32..127) i++ // fcn nme

    // vendor: SSS=v1d, TTT=&v1d[i+1] shift (drops name + terminator char).
    var sss = 0
    var ttt = i + 1
    while (v1d[ttt] != NUL) {
        v1d[sss] = v1d[ttt]
        sss++
        ttt++
    }
    v1d[sss] = NUL

    i = 0
    var a = 0
    val tmp = CharArray(1024)
    tmp[0] = NUL

    while (true) {
        val j = i

        while (v1d[i].code in 32..127) i++

        if (v1d[i] == NUL) {
            if (a == 0 || a > 3) return a
            if (a == 1) strcatC(tmp, ", (UNS) " + '"' + "-1" + '"' + ", (UNS) " + '"' + "-1" + '"')
            else if (a == 2) strcatC(tmp, ", (UNS) " + '"' + "-1" + '"')
            memcheck(st.iiitmp)

            st.out.write(
                "\t_extract(" + " (UNS) tmp" + st.iiitmp + ",\n " +
                    "\t\t(UNS) " + cstringAt(tmp) + "); // p2493\n"
            )

            strcpyC(bp, "tmp" + st.iiitmp)
            st.iiitmp++
            memcheck(st.iiitmp)
            return 0
        }

        v1d[i] = NUL
        i++

        if (a++ > 0) strcatC(tmp, ", (UNS) ")

        strcatC(tmp, Extract2(v1d, j))
    }
}
