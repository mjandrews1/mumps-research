package mumps.compiler

import mumps.compiler.CompilerState.Out

/**
 * Kotlin translation of compiler-parse.c::Extract2 (Study Subset unit 15,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * C: char *Extract2(char *str) — the translator's most-reused helper
 * (33 call sites). Parses #S#tmp1#tmp2# / #G#tmp1#tmp2#, emits the
 * corresponding null-check code, and returns tmp2 — or returns the input
 * pointer unchanged when it is not #-prefixed.
 *
 * C static tmp1/tmp2 return buffers -> per-invocation String values
 * (heuristic 56). Call sites pass either a whole pointer or &buf[j]:
 * the latter use the offset overload (heuristic 57).
 */
fun extract2(str: String): String {
    if (str.isEmpty() || str[0] != '#') return str // C: return str (identity)

    var i = 3
    while (i < str.length && str[i] != '#') i++ // C: for (i=3; str[i]!='#' && str[i]!='\0'; i++);
    val tmp1 = str.substring(3, i) // C: memcpy(tmp1, &str[3], i-3)

    i++ // C: j = ++i
    val j = i
    while (i < str.length && str[i] != '#') i++
    val tmp2 = str.substring(j, i) // C: memcpy(tmp2, &str[j], i-j)

    if (str.regionMatches(0, "#S#", 0, 3)) {
        Out.write("\tif (sym_(SYMGET, (UNS) " + tmp1 + ", (UNS) " + tmp2 + ", svPtr) == NULL) // get value \n")
        Out.write("\t\tVariableNotFound1(svPtr->LineNumber, (UNS) " + tmp1 + "); // not found\n")
    } else if (str.regionMatches(0, "#G#", 0, 3)) {
        Out.write("\tif (!Mglobal(RETRIEVE, " + tmp1 + ", " + tmp2 + ", svPtr)) // retrieve global\n")
        Out.write("\t\tVariableNotFound(svPtr->LineNumber, " + tmp1 + ");\n")
    }

    return tmp2
}

/** Call sites with C pointer-into-buffer args: Extract2(&buf[j]) -> extract2(buf, j). */
fun extract2(buf: String, from: Int): String = extract2(buf.substring(from))
