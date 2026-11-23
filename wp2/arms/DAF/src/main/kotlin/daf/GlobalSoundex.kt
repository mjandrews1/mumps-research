package daf

// Unit 193 (DAF B7 T1, simple): libmpscpp.cpp::global::Soundex (17
// LOC, score 8.7): the global-class \$zzsoundex member: retrieve the
// receiver ref value, build the \$zzsoundex service message over it
// (the \xce/\xcf envelope, NO \xd0 arg separators - the no-arg
// service envelope), carry into v1d[1], relay via fcn(svPtr), and
// read bd[1] back as the returned mstring.
// Consults (2026-11-23T18:33:30Z, nominal): BASE-log row 92
// hit-reuse (the Mglobal(RETRIEVE) retrieve-then-throw on the ref
// with GlobalExceptionCode = GLOBAL_NOT_FOUND), row 101 hit-reuse
// (the v1d[1] offset-1 carriage of the coded service call - message
// built at offset 1, read from there; the receiver folds onto the
// ref String per the row-54 node model), row 30 hit-adapt (the
// mstring m local + Concat chain folds onto the String builder;
// bd[1] NUL-terminated out-read via cstringAt), the rows 97/98
// family hit-reuse (the fcn service relay resolves to the T3
// Fcn.kt dafFcn body). Envelope kept byte-exact per the unit-131
// GlobalTranslate precedent: \$zzsoundex, \xce, gtmp, \xcf.
// Dafny: EXEMPT (service call over the fcn dispatcher seat; journal
// row).

private const val SX_CE_D: Char = '\u00CE' // vendor Concat("\xce")
private const val SX_CF_D: Char = '\u00CF' // vendor Concat("\xcf")

fun globalSoundexD(n: MpsGlobalNode, sv: DafMsv): String {

    val gtmp = CharArray(STR_MAX_DAF)

    val refCarriage = sxName(n.ref)
    val f = MglobalDaf(RETRIEVE_D, refCarriage, gtmp, sv)
    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Soundex: global not found")
    }

    // m = "\$zzsoundex"; m + "\xce" + gtmp + "\xcf"
    val m = StringBuilder("\$zzsoundex")
    m.append(SX_CE_D)
    m.append(DafCRuntime.cstring(gtmp))
    m.append(SX_CF_D)

    // strcpy(&v1d[1], m): the row-101 offset-1 carriage
    var q = 0
    while (q < m.length && q + 1 < sv.v1d.size) {
        sv.v1d[q + 1] = m[q]; q++
    }
    sv.v1d[q + 1] = NUL_D

    dafFcn(sv) // fcn(svPtr) - the T3 seat resolution

    return DafCRuntime.cstringAt(sv.bd, 1) // m = (char *)&bd[1]
}

private fun sxName(src: String): CharArray {
    val out = CharArray(STR_MAX_DAF)
    val s = src.toCharArray()
    var i = 0
    while (i < s.size) {
        out[i] = s[i]
        i++
    }
    out[i] = NUL_D
    return out
}
