package mumps.compiler

import mumps.compiler.CompilerState.Out

/** C: #define CodedComma 208 (compiler-parse.c:133; same value in defines.h). */
private const val CODED_COMMA: Int = 208

/**
 * Kotlin translation of compiler-parse.c::ExpandGet (Study Subset unit 16,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * C: void ExpandGet(unsigned char *v1d, int i, unsigned char *bp)
 * Parses #G#/#S# get-expansions (with optional CodedComma third arg),
 * emits GlobalGet/LocalGet, and copies arg2 into bp only on match
 * -> String? return (heuristic 44 reused).
 *
 * FAITHFUL BUG PRESERVED (heuristic 58): the C #S# branch tests
 * strncmp(&v1d[i],"#S#",3)==0 || strncmp(&v1d[i],"#S#",3)==0 — the second
 * test duplicates the first (plainly meant to be "#G#"), so a #G# argument
 * in the #S# branch takes the raw-copy path instead of Extract2. The
 * duplicated condition is translated verbatim.
 */
fun expandGet(v1d: String, iStart: Int): String? {
    var i = iStart + 1 // C: i++

    fun scanUntilHash(): String { // C: for (j=0; v1d[i]!='#'; j++) argN[j]=v1d[i++]
        val b = StringBuilder()
        while (v1d[i] != '#') { b.append(v1d[i]); i++ }
        i++
        return b.toString()
    }

    if (v1d.regionMatches(i, "#G#", 0, 3)) {
        i += 3
        val arg1 = scanUntilHash()
        val arg2 = scanUntilHash()
        i++ // C: i=i+1
        val arg3: String
        if (i < v1d.length && v1d[i].code == CODED_COMMA) {
            i++
            arg3 = if (v1d.regionMatches(i, "#G#", 0, 3) || v1d.regionMatches(i, "#S#", 0, 3)) {
                extract2(v1d, i) // first offset-overload caller (heuristic 57)
            } else {
                val b = StringBuilder() // C: for (j=0; v1d[i]<128 && v1d[i]!='\0'; ...)
                while (i < v1d.length && v1d[i].code < 128) { b.append(v1d[i]); i++ }
                b.toString()
            }
        } else arg3 = "NULL"
        Out.write("\tGlobalGet( (unsigned char *) " + arg1 + ", (unsigned char *) " + arg2 +
                ", (unsigned char *) " + arg3 + ", svPtr); \n")
        return arg2 // C: strcpy(bp, arg2)
    }

    if (v1d.regionMatches(i, "#S#", 0, 3)) {
        i += 3
        val arg1 = scanUntilHash()
        val arg2 = scanUntilHash()
        i++
        val arg3: String
        if (i < v1d.length && v1d[i].code == CODED_COMMA) {
            i++
            // C verbatim: #S# || #S# — second test duplicates the first
            // (heuristic 58; plainly meant to be "#G#")
            arg3 = if (v1d.regionMatches(i, "#S#", 0, 3) || v1d.regionMatches(i, "#S#", 0, 3)) {
                extract2(v1d, i)
            } else {
                val b = StringBuilder()
                while (i < v1d.length && v1d[i].code < 128) { b.append(v1d[i]); i++ }
                b.toString()
            }
        } else arg3 = "NULL"
        Out.write("\tLocalGet( (unsigned char *) " + arg1 + ", (unsigned char *) " + arg2 +
                ", (unsigned char *) " + arg3 + ", svPtr); \n")
        return arg2
    }

    return null // C: falls through without touching bp
}
