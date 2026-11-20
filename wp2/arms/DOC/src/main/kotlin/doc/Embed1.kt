package doc

// Unit 205 (B7 tranche 2): libmpscpp.cpp::embed1 (batch 7, core
// stratum; LOC 19, score 27.5, rank 198; vendor libmpscpp.cpp:4151,
// physically verified this session; nonblank 19 matches the roster
// row) - the DOUBLE-quote escaper of the libmpscpp SQL arm, twin
// of the batch-2 unit-51 interp-sql.cpp::embed render (the single
// quote pairs; overload-family note per H-015 same-name rule: the
// vendor qualifier this by FILE). Consult H-008 hit-reuse: the
// vendor static char tmp[STR_MAX] shared scratch renders as the
// fresh per-call String (shipped unit-51 Embed.kt precedent
// verbatim); the no-quote fast path returns the ORIGINAL input
// string (vendor returns the input pointer - the original String,
// unit-51 document note); NULL maps to the empty result at the
// vendor's NULL guard. The doubling walk keeps the vendor's own
// p1/p2 pointer style as the String index walk (v. the fresh
// StringBuilder carrier). Dead locals kept per H-013 as this
// comment: the vendor int i is only the scan index seat and the
// int j=0 initializer is never read (the vendor loop uses p1/p2
// pointer seats; no write path observes j). Naming per H-017
// (embed1 kept).

fun embed1(inp: String?): String {
    // static char tmp[STR_MAX]; tmp[0]=0;
    // if (in==NULL) return tmp; - the NULL-in guard maps null to
    // the empty result
    if (inp == null) return ""

    var k = 1
    for (c in inp) {
        if (c == '"') {
            k = 0
            break
        }
    }

    if (k == 1) return inp // contains no quotes

    val sb = StringBuilder()
    for (c in inp) {
        if (c != '"') {
            sb.append(c)
        } else {
            sb.append('"')
            sb.append('"')
        }
    }

    return sb.toString()
}
