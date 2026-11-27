package daf

// R1 stub-completion (2026-11-27, C-020 erratum session, tranche 2):
// compiler-parse.c::fixstr (25 LOC, roster row 26, simple): the
// in-place pattern-string fixer of the compile-time PATTERN arm.
// Vendor scans the NUL-terminated buffer, escapes double-quote,
// single-quote and backslash by prefixing a backslash into a tmp
// staging buffer, then copies the staged text back over the buffer.
// The seat keeps the CharArray-in-place fold: bp is written back
// in place and NUL-terminated (strcpy(a,tmp) fold; putAt-style
// carriers not needed - the call sites in CompilerParseC.kt pass
// bp directly, fixstr(&bd[1]) raised-seat convention unchanged).
// Consults (2026-11-27T00:00:00Z, nominal): BASE-log row 45 family
// hit-adapt (the char tmp[1024] staging buffer renders as the
// StringBuilder append fold of the Expand* precedent of this TU);
// rows 111/68 hit-reuse (NUL_D seat of the CharArray-NUL
// convention); row 2 family hit-reuse (no Out emission in this
// unit - the buffer fix is pure data movement).
// Vendor quirks kept: (1) the escape order means a char is tested
// quote, then single-quote, then backslash - mutually exclusive
// so order is inert, kept verbatim; (2) the trailing strcpy(a,tmp)
// truncates a (NUL at the staged length) - the render writes the
// NUL at the staged length, same truncation.
// Dafny: EXEMPT (buffer-shaping plumbing; journal row).

fun fixstrDaf(a: CharArray): Unit {

    val tmp = StringBuilder() // char tmp[1024]

    var i = 0
    while (at(a, i) != NUL_D) { // for (i = 0, j = 0; a[i] != 0; i++)
        val c = a[i]
        if (c == '"') {
            tmp.append('\\'); tmp.append('"') // tmp[j++]='\\'; tmp[j++]='"'
        } else if (c == '\'') {
            tmp.append('\\'); tmp.append('\'') // tmp[j++]='\\'; tmp[j++]='\''
        } else if (c == '\\') {
            tmp.append('\\'); tmp.append('\\') // tmp[j++]='\\'; tmp[j++]='\\'
        } else {
            tmp.append(c) // tmp[j++] = a[i]
        }
        i++
    }

    // strcpy(a, tmp): write the staged text back in place, NUL at
    // the staged length (the truncation fold)
    val s = tmp.toString()
    for (k in s.indices) a[k] = s[k]
    a[s.length] = NUL_D
}
