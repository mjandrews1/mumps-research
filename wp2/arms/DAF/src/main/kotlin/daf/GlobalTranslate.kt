package daf

// Unit 131 (DAF B5 T1, simple): libmpscpp.cpp::global::Translate
// (19 LOC): the global-class member that builds the \$translate
// service call over the node's retrieved value: Mglobal retrieve of
// the ref, then the mstring Concat chain of the coded service
// envelope (xce/d0/xcf) into v1d[1], the fcn(svPtr) relay, and the
// bd[1] result read back as the returned mstring.
// Consults: BASE-log row 92 hit-reuse (the Mglobal(RETRIEVE,...)
// retrieve-then-throw with GlobalExceptionCode=GLOBAL_NOT_FOUND and
// the MumpsGlobalException fold), row 101 hit-reuse (the v1d[1]
// carriage of the coded service call - the leading-byte shift is
// the vendor's &v1d[1] convention: the message is built at offset 1
// and read from there), row 30 hit-adapt (the mstring m local +
// Concat chain folds onto a String builder; the strcpy into v1d[1]
// is the copy-to-carrier fold; bd[1] is the NUL-terminated out-read
// via cstringAt), rows 97/98 family hit-reuse (the fcn service relay
// resolves to the T3 Fcn.kt dafFcn body - the seat flip closed in
// DAF B4 T3; this render is the first in-batch caller), row 54
// hit-reuse (the node receiver ref String per the node-model fold).
// Dafny: EXEMPT (service-call shaping over the fcn dispatcher seat;
// journal row).

private const val XT_CE_D: Char = '\u00CE' // vendor Concat("\xce")
private const val XT_D0_D: Char = '\u00D0' // vendor Concat("\xd0")
private const val XT_CF_D: Char = '\u00CF' // vendor Concat("\xcf")

fun globalTranslateD(ref: String, a1: String, sv: DafMsv): String {

    val gtmp = CharArray(STR_MAX_DAF)

    val f = MglobalDaf(RETRIEVE_D, nameCarriageOf(ref), gtmp, sv)
    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Translate: global not found")
    }

    // m = "\$translate"; m.ToString + "\xce" + gtmp + "\xd0" + a1 + "\xcf"
    val m = StringBuilder("\$translate")
    m.append(XT_CE_D)
    m.append(DafCRuntime.cstring(gtmp))
    m.append(XT_D0_D)
    m.append(a1)
    m.append(XT_CF_D)

    // strcpy(&v1d[1], m): the row-101 offset-1 carriage
    var q = 0
    while (q < m.length && q + 1 < sv.v1d.size) {
        sv.v1d[q + 1] = m[q]; q++
    }
    sv.v1d[q + 1] = NUL_D

    dafFcn(sv) // fcn(svPtr) - the T3 seat resolution

    return DafCRuntime.cstringAt(sv.bd, 1) // m = (char *)&bd[1]
}

private fun nameCarriageOf(src: String): CharArray {
    val dst = CharArray(STR_MAX_DAF)
    var q = 0
    while (q < src.length) { dst[q] = src[q]; q++ }
    return dst
}
