package mumps.compiler

import mumps.compiler.CompilerState.Out

/**
 * Kotlin translation of compiler-parse.c::ExpandNext (Study Subset unit 11,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * C: void ExpandNext(unsigned char *v1d, int i, unsigned char *bp)
 * Parses #G#arg1#arg2# (global order) or #S#arg1#arg2# (local next) from
 * v1d starting at i, emits the corresponding code, and copies arg2 into
 * the caller's bp — ONLY on a match (bp stays untouched otherwise).
 *
 * Kotlin mapping: v1d scan-only -> String (heuristic 8); &v1d[i] -> offset
 * param; conditional bp write -> String? return, null = bp untouched
 * (heuristic 44). Unbounded scanning replaces the 32-char zero-filled
 * buffers (heuristic 45: overflow UB removed).
 */
fun expandNext(v1d: String, iStart: Int): String? {
    var i = iStart + 1 // C: i++

    fun scanUntilHash(): String { // C: for (j=0; v1d[i]!='#'; j++) arg[j]=v1d[i++]
        val b = StringBuilder()
        while (v1d[i] != '#') { b.append(v1d[i]); i++ }
        i++
        return b.toString()
    }

    if (v1d.regionMatches(i, "#G#", 0, 3)) { // C: strncmp(&v1d[i],"#G#",3)==0
        i += 3
        val arg1 = scanUntilHash()
        val arg2 = scanUntilHash()
        Out.write("\tGlobalOrder(" + arg1 + ", " + arg2 + ", (unsigned char *) \"1\", svPtr); //2306\n")
        Out.write("\tif (strlen((char *)" + arg2 + ")==0) strcpy((char *)" + arg2 + ",\"-1\");\n")
        return arg2 // C: strcpy(bp, arg2)
    }

    if (v1d.regionMatches(i, "#S#", 0, 3)) {
        i += 3
        val arg1 = scanUntilHash()
        val arg2 = scanUntilHash()
        Out.write("\tLocalNext((unsigned char *) " + arg1 + ",(unsigned char *) " + arg2 + ",svPtr);\n")
        return arg2
    }

    return null // C: falls through without touching bp
}
