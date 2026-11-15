package mumps.compiler

import java.util.Locale
import mumps.compiler.CompilerState.Out

/**
 * Kotlin translation of compiler-globalarray.c::GlobalArray (Study Subset
 * unit 52, Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: void GlobalArray(unsigned char v1d[], short *GBL_flg, unsigned char *vd)
 * Expands an LHS-style global array reference (^name(args)) into inline
 * C++ string-building code.
 *
 * Mapping: unsigned char buffers -> StringBuilder (v1d is the shared
 * CompilerState workspace, direct indexing per LocalArray precedent);
 * short *GBL_flg out-param -> ShortArray[0] holder (heuristic 66, same
 * shape as LocalArray's *Set_flg); vd sprintf target -> StringBuilder.
 *
 * Divergence (documented, row 113): the C loop body does
 * `if (v1d[i]==0) v1d[i]=207;` — an in-place mutation of the terminating
 * NUL whose only observable use is the immediately following read
 * (sprintf of v1d[i]); the buffer is never read again afterward, so it is
 * modeled as a local val (end-of-buffer -> 207) instead of mutating v1d.
 *
 * `unsigned char h[6]` and the sprintf(h,"%2x",...) idiom collapse to a
 * 2-char hex string: %2x of a value in 0..255 is always exactly 2 chars
 * (space-padded below 0x10), matching the h[0]/h[1] reads verbatim.
 */
fun globalArray(v1d: StringBuilder, gblFlg: ShortArray, vd: StringBuilder) {
    val S = CompilerState

    fun atEnd(i: Int) = i >= v1d.length || v1d[i] == '\u0000'

    // C: unsigned char t[1024] — segment accumulator (fixed 1024 buffer
    // dropped; StringBuilder cannot overflow the way C can).
    val t = StringBuilder()

    // C: if (v1d[1] == '^') — v1d models the C buffer; index 1 may be the
    // virtual NUL (LocalArray's c1 precedent).
    val c1 = if (v1d.length > 1) v1d[1] else '\u0000'
    if (c1 == '^') {

        var j = 0

        // C: for (i=2; v1d[i]!=0; i++) { if (32..127) t[j++]=v1d[i]; else break; }
        // (starts at 2: skips '^'; comment notes keyfix does not shift)
        var i = 2
        while (!atEnd(i)) {
            if (v1d[i].code in 32..127) { t.append(v1d[i]); j++ }
            else break
            i++
        }

        gblFlg[0] = 1 // C: *GBL_flg = 1
        vd.setLength(0); vd.append("tmp").append(CompilerState.iiitmp) // C: sprintf(vd,"tmp%d",iiitmp)

        // C: if (v1d[i]=='\0') h[2]=206; else h[2]=v1d[i];
        //    sprintf(h, "%2x", h[2]);
        val stop = if (atEnd(i)) 206 else v1d[i].code
        val h = java.lang.String.format(Locale.US, "%2x", stop)

        t.append('\\').append('x').append(h[0]).append(h[1]) // C: t[j++]='\\','x',h[0],h[1]; t[j]=0

        Out.write("\tstrmove((UNS) tmp" + CompilerState.iiitmp + ", (UNS) \"" + t + "\"); // M3701\n")

        if (atEnd(i)) { // C: if (v1d[i]=='\0') — name had no subscript
            S.iiitmp++
            memcheck(S.iiitmp)
            return
        }
        i++ // C: i++

        while (true) {

            t.setLength(0) // C: j = 0 (t rebuilt from scratch)

            while (!atEnd(i) && v1d[i].code in 32..127) { t.append(v1d[i]); i++ }

            if (t.isNotEmpty()) { // C: if (strlen(t))
                if (t.regionMatches(0, "#G#", 0, 3) || t.regionMatches(0, "#S#", 0, 3))
                    Out.write(
                        "\tcatx = strxcat((char *) tmp" + CompilerState.iiitmp +
                            ", (char *) " + extract2(t.toString()) + "); // a2926\n")
                else
                    Out.write(
                        "\tcatx = strxcat((char *) tmp" + CompilerState.iiitmp +
                            ", (char *) " + t + "); // b\n")
            }

            // C: if (v1d[i]==0) v1d[i]=207;  — in-place terminator overwrite,
            // only observable via the next read; modeled without mutation.
            val stop2 = if (atEnd(i)) 207 else v1d[i].code
            val h2 = java.lang.String.format(Locale.US, "%2x", stop2)

            t.setLength(0)
            t.append('\\').append('x').append(h2[0]).append(h2[1])

            Out.write("\tstrcat((char *) catx, (char *) \"" + t + "\"); //a3956\n")

            i++ // C: if (v1d[++i] != 0) continue; else break;
            if (atEnd(i)) break
            // continue
        }

    } else {
        gblFlg[0] = 0 // C: else *GBL_flg = 0
    }

    // common tail (reached by falling out of the loop or the non-global path)
    CompilerState.iiitmp++ // C: iiitmp++
    memcheck(CompilerState.iiitmp)
    return
}
