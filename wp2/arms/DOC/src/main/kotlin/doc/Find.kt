package doc

// Unit 33: libmpscpp.cpp::global::Find (batch 2, simple stratum). The
// unit is the char* overload per the pre-registered chunker slice.

fun GlobalRef.find(key: String, st: Int): Int {
    val a = CharArray(40)
    val r = CharArray(20)
    val tmp = CharArray(STR_MAX)

    val f = Stubs.Mglobal(RETRIEVE, ref, tmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    Stubs.Mltoa(st, a)
    Stubs.Mfind(r, tmp, key.toCharArray(), a)
    val rr = atoi(CstringOps.cstring(r))
    return rr
}

// strtol-style int conversion of a decimal prefix, per the C atoi contract
// (skips whitespace, optional sign, stops at first non-digit).
fun atoi(s: String): Int {
    var i = 0
    while (i < s.length && (s[i] == ' ' || s[i] == '\t' || s[i] == '\n')) i++
    var sign = 1
    if (i < s.length && (s[i] == '+' || s[i] == '-')) {
        if (s[i] == '-') sign = -1
        i++
    }
    var v = 0
    while (i < s.length && s[i] in '0'..'9') {
        v = v * 10 + (s[i] - '0')
        i++
    }
    return sign * v
}
