package daf

// Unit 232 (DAF B8 T2, core): compiler-parse.c::Extract2a
// (21 LOC, score 18.3): the a-sibling of the Extract2 family:
// splits the coded #S#/#G#name#target# payload and emits the
// ternary lookup text (sym_(1, ...) ? target :
// VariableNotFound1(...), the E21000 tag; the #G# arm the
// Mglobal(RETRIEVE) ? : VariableNotFound ternary); returns the
// target slice (vendor compiler-parse.c:2129, physically verified
// this session).
// Consults (2026-11-24T21:18:00Z, nominal): the unit-203
// CompilerExtract consult set (the emit fold; the row-30 pointer
// fold: the &v1d[i] offset entry idiom renders as the (str, off)
// contract, the str[0] -> str[off] family note), the static char
// tmp1[64]/tmp2[64] function-scratch pair folds onto fresh local
// 64-carriers per the LibmpscppMisc5 static-fold precedent (the
// buffers are written and read within the call; no cross-call
// persistence is observable), the memcpy fills render as explicit
// loop writes, strncmp rides a file-local strncmpAtD helper, the
// early return str -> the cstring-at-offset value fold, the
// emission text byte-verbatim including the (UNS) casts.
// Dafny: EXEMPT (emit-side compiler plumbing; journal row).

fun extract2aD(st: DafCompilerState, str: CharArray, off: Int): String {

    if (str[off] != '#') return DafCRuntime.cstringAt(str, off) // if (str[0]!='#') return str;

    // for (i=3; str[i]!='#' && str[i]!='\0'; i++);
    var i = off + 3
    while (str[i] != '#' && str[i] != NUL_D) i++

    // memcpy(tmp1,&str[3],i-3); tmp1[i-3]='\0';
    val tmp1 = CharArray(64) // static char tmp1[64]
    var n = 0
    while (n < i - (off + 3)) {
        tmp1[n] = str[off + 3 + n]
        n++
    }
    tmp1[n] = NUL_D

    // for (j=++i; str[i]!='#' && str[i]!='\0'; i++);
    ++i
    val j = i
    while (str[i] != '#' && str[i] != NUL_D) i++

    // memcpy(tmp2,&str[j],i-j); tmp2[i-j]='\0';
    val tmp2 = CharArray(64) // static char tmp2[64]
    var n2 = 0
    while (n2 < i - j) {
        tmp2[n2] = str[j + n2]
        n2++
    }
    tmp2[n2] = NUL_D

    val t1s = DafCRuntime.cstring(tmp1)
    val t2s = DafCRuntime.cstring(tmp2)

    if (strncmpAtD(str, off, "#S#", 3) == 0) { // strncmp(str,"#S#",3)==0
        st.emit("\tsym_(1, (UNS) " + t1s + ", (UNS) " + t2s + "," +
                "svPtr) ? " + t2s +
                " : VariableNotFound1(svPtr->LineNumber, (UNS) " + t1s +
                "); //E21000\n")
    } else if (strncmpAtD(str, off, "#G#", 3) == 0) { // #G# arm
        st.emit("\tMglobal(RETRIEVE, " + t1s + ", " + t2s + ", svPtr) ?" +
                t2s + " : VariableNotFound(svPtr->LineNumber, " + t1s + ");\n")
    }

    return t2s // return tmp2
}

// strncmp(str, "x", n) at offset - file-local helper (the
// Interpret.kt strncmpAtI convention, per-file replication).
private fun strncmpAtD(buf: CharArray, offset: Int, s: String, n: Int): Int {
    var q = 0
    while (q < n && q < s.length) {
        val a = buf[offset + q]
        val b = s[q]
        if (a != b) return a - b
        if (a == NUL_D) return 0
        q++
    }
    return 0
}
