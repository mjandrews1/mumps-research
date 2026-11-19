package doc

// Unit 52: compiler-globalarray.c::GlobalArray (batch 2, complex
// stratum). Process LHS style global array refs: scans the coded v1d
// buffer after a leading caretx byte, emits strmove/strxcat/strcat C
// text into Out, and leaves the temp-name pair (vd, iiitmp) for the
// caller. Vendor quirks kept exactly: the h array sprintf overwrite
// order (h[2] set, then %2x writes h[0]/h[1]); the 207 code-byte
// written INTO the caller's v1d buffer when the delimiter is the NUL;
// the dead leading-comment fossil about the i=1 start (vendor lines
// 15-18, kept as a comment below). Buffers classified per cookbook
// H-007: t is mutable scratch with index writes, rendered as a
// CharArray (adaptation: index-based two-char appends need the array
// carrier, not StringBuilder); v1d/vd are caller-owned carriers.
// sprintf collapses per H-009 (Locale.US; %2x of a byte value is
// always a two-char hex string). The unsigned char domain range
// checks (32..127) per H-012. Out-of-scope calls are H-014 stubs:
// Extract2 (owner compiler-parse.c::Extract2, batch-1 row 15) and
// memcheck (owner compiler-parse.c:2068, below roster min loc).
// GBL_flg is the cookbook H-006 holder-array example itself:
// ShortArray(1) out-parameter. The emitted C text is escaped per
// H-015 (backslash-x emitted as literal text, no raw bytes).
//
// The vendor leading-comment fossil (lines 15-18):
//   starting as 2 eliminates the caret byte; that means keyfix does
//   not shift (starting as 1 would not).

fun GlobalArray(v1d: CharArray, GBL_flg: ShortArray, vd: CharArray) {
    val t = CharArray(1024)
    val h = CharArray(6)
    var i: Int
    var j = 0

    if (v1d[1] == Char(94)) {
        i = 2
        while (v1d[i] != NUL) {
            if (v1d[i] >= Char(32) && v1d[i] <= Char(127)) {
                t[j++] = v1d[i]
            } else break
            i++
        }

        GBL_flg[0] = 1
        CstringOps.strcpyS(vd, "tmp" + CompilerState.iiitmp)
        if (v1d[i] == NUL) h[2] = Char(206) else h[2] = v1d[i]
        val hs = String.format(java.util.Locale.US, "%2x", h[2].code)
        h[0] = hs[0]
        h[1] = hs[1]
        t[j++] = '\\'
        t[j++] = 'x'
        t[j++] = h[0]
        t[j++] = h[1]
        t[j] = NUL

        CompilerState.Out.write("\tstrmove((UNS) tmp" + CompilerState.iiitmp +
                ", (UNS) \"" + CstringOps.cstring(t) + "\"); // M3701\n")

        if (v1d[i] == NUL) {
            CompilerState.iiitmp++
            Stubs.memcheck(CompilerState.iiitmp)
            return
        }
        i++

        while (true) {
            j = 0
            while (v1d[i] <= Char(127) && v1d[i] >= Char(32)) {
                t[j++] = v1d[i]
                i++
            }
            t[j] = NUL

            if (CstringOps.strlenC(t) != 0) {
                val tStr = CstringOps.cstring(t)
                if (CstringOps.strncmpS("#G#", t, 3) == 0 || CstringOps.strncmpS("#S#", t, 3) == 0) {
                    CompilerState.Out.write("\tcatx = strxcat((char *) tmp" +
                            CompilerState.iiitmp + ", (char *) " +
                            CstringOps.cstring(Stubs.Extract2(t, 0)) + "); // a2926\\n")
                } else {
                    CompilerState.Out.write("\tcatx = strxcat((char *) tmp" +
                            CompilerState.iiitmp + ", (char *) " + tStr + "); // b\n")
                }
            }

            j = 0
            if (v1d[i] == NUL) v1d[i] = Char(207)

            val hs2 = String.format(java.util.Locale.US, "%2x", v1d[i].code)
            t[j++] = '\\'
            t[j++] = 'x'
            t[j++] = hs2[0]
            t[j++] = hs2[1]
            t[j] = NUL

            CompilerState.Out.write("\tstrcat((char *) catx, (char *) \"" +
                    CstringOps.cstring(t) + "\"); //a3956\n")

            if (v1d[++i] != NUL) continue else break
        }
    } else {
        GBL_flg[0] = 0
    }
    CompilerState.iiitmp++
    Stubs.memcheck(CompilerState.iiitmp)
    return
}
