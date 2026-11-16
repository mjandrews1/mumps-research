package mumps.compiler

import java.io.IOException
import java.util.Locale
import mumps.interp.NUL
import mumps.interp.cstringAt
import mumps.interp.strlenC
import mumps.interp.strncmpC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of compiler-globalarray.c::GlobalArray (pilot
 * B2, unit 52, provisional under deviation C-003). Translated
 * independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 40-127 (units.csv: 59 LOC, score 31.9, rank 209).
 *
 * Processes LHS-style global array references: for ^Name(s1,s2,...)
 * it emits the C glue lines (strmove / strxcat / strcat with \xNN
 * coded separator bytes) that rebuild the subscripted reference into
 * the temp-string pool, and returns the temp name (tmp%d) in vd.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param    : the vendor FILE* Out becomes the
 *    CompilerState.out Writer (@Throws IOException), unit-20/27
 *    precedent.
 *  - h004 globals_ext       : iiitmp (compiler-globals-ext.h:109,
 *    temp-string pool counter) becomes a CompilerState property;
 *    the vendor's h[2] staging slot collapses into the local code
 *    value.
 *  - h006 out_param_holder  : the short* GBL_flg out-parameter
 *    becomes a single-element ShortArray holder, read/written at [0]
 *    (compiler-localarray.c precedent); vd stays the caller's buffer
 *    (sprintf into it via writeCstring).
 *  - h007 char_buffer       : t[1024]/h[6] C-string work buffers.
 *  - h009 format_string     : the vendor sprintf "%2x" (space-padded
 *    lowercase hex) and "tmp%d" become Locale.US formatters /
 *    concatenation (unit-1 ctime precedent).
 *  - h012 unsigned_byte_domain: v1d/t/vd are Char(0..255) domains;
 *    the 32..127 range tests compare .code.
 *  - h014 out_of_scope_call : memcheck (owner compiler-parse.c:2068,
 *    not a corpus unit) -> new Stubs.kt stub; Extract2 (B1 unit) is a
 *    real reuse; strncmp/strlen via the CstringOps helpers - real
 *    B1 reuses.
 *  - h017 naming            : GlobalArray keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The mid-loop close-coding WRITES 207 into the caller's v1d
 *    buffer when it sees the terminator (v1d[i]=0 -> 207, the coded
 *    close byte) - an input-buffer mutation kept verbatim.
 *  - The first for-loop starts at 2 to skip the caret, per the
 *    vendor's block comment (keyfix does not shift).
 *  - The vendor stages the byte in h[2] before formatting h; here
 *    the staging slot becomes the local code value (h004 note).
 */

@Throws(IOException::class)
fun GlobalArray(v1d: CharArray, GBL_flg: ShortArray, vd: CharArray) {
    val t = CharArray(1024)
    var i: Int
    var j: Int

    // vendor: if (v1d[1] == '^') { // Process LHS global array reference
    if (v1d[1] == '^') {

        // vendor: for (i=2; v1d[i]!=0; i++) { if (32<=v1d[i]<=127) t[j++]=v1d[i]; else break; }
        j = 0
        i = 2
        while (v1d[i] != NUL) {
            if (v1d[i].code >= 32 && v1d[i].code <= 127) {
                t[j] = v1d[i]
                j++
                i++
            } else break
        }

        GBL_flg[0] = 1 // vendor: *GBL_flg = 1;

        // vendor: sprintf(vd,"tmp%d",iiitmp);
        writeCstring(vd, "tmp" + CompilerState.iiitmp)

        // vendor: if (v1d[i]=='\0') h[2]=206; else h[2]=v1d[i]; sprintf(h,"%2x",h[2]);
        val code = if (v1d[i] == NUL) 206 else v1d[i].code
        val h = String.format(Locale.US, "%2x", code)

        t[j] = '\\'
        j++
        t[j] = 'x'
        j++
        t[j] = h[0]
        j++
        t[j] = h[1]
        j++
        t[j] = NUL

        // vendor: fprintf(Out,"\tstrmove((UNS) tmp%d, (UNS) \"%s\"); // M3701\n", iiitmp, t);
        CompilerState.out.write("\tstrmove((UNS) tmp" + CompilerState.iiitmp + ", (UNS) \"" + cstringAt(t) + "\"); // M3701\n")

        if (v1d[i] == NUL) {
            CompilerState.iiitmp++
            memcheck(CompilerState.iiitmp)
            return
        }
        i++

        while (true) {
            j = 0

            // vendor: while (v1d[i] <= 127 && v1d[i] >= 32) t[j++] = v1d[i++];
            while (v1d[i].code <= 127 && v1d[i].code >= 32) {
                t[j] = v1d[i]
                j++
                i++
            }
            t[j] = NUL

            if (strlenC(t) > 0) {

                if (strncmpC(t, 0, "#G#", 3) == 0 || strncmpC(t, 0, "#S#", 3) == 0) {
                    // vendor: ... strxcat ... // a2926
                    CompilerState.out.write("\tcatx = strxcat((char *) tmp" + CompilerState.iiitmp + ", (char *) " + Extract2(t) + "); // a2926\n")
                } else {
                    CompilerState.out.write("\tcatx = strxcat((char *) tmp" + CompilerState.iiitmp + ", (char *) " + cstringAt(t) + "); // b\n")
                }
            }

            j = 0

            // vendor: if (v1d[i]==0) v1d[i]=207; (input-buffer mutation, kept)
            if (v1d[i] == NUL) v1d[i] = 207.toChar()

            // vendor: sprintf(h, "%2x", v1d[i]);
            val h2 = String.format(Locale.US, "%2x", v1d[i].code)

            t[j] = '\\'
            j++
            t[j] = 'x'
            j++
            t[j] = h2[0]
            j++
            t[j] = h2[1]
            j++
            t[j] = NUL

            // vendor: fprintf(Out,"\tstrcat((char *) catx, (char *) \"%s\"); //a3956\n", t);
            CompilerState.out.write("\tstrcat((char *) catx, (char *) \"" + cstringAt(t) + "\"); //a3956\n")

            i++ // vendor: if (v1d[++i] != 0) continue; else break;
            if (v1d[i] != NUL) continue else break
        }
    } else {
        GBL_flg[0] = 0 // vendor: else *GBL_flg = 0;
    }

    CompilerState.iiitmp++ // vendor: iiitmp++;
    memcheck(CompilerState.iiitmp)
}
