package daf

/*
 * DAF batch-10 unit 308 (roster row 308; libmpscpp.cpp::global::
 * Justify, LOC 18, ptr 0, score 4.2). Consult 2026-11-25T15:15:00Z.
 *
 * The global receiver side of the JUSTIFY family (the mstring
 * twin Justify(int,int) is the mstring::Justify lattice tail,
 * ledgered here - same body over data.c_str() instead of the
 * retrieved gtmp).
 *
 * Fold notes: VENDOR ORDER KEPT: sprintf(cc1)/sprintf(cc2) run
 * BEFORE the Mglobal retrieve; the retrieve rides MglobalDaf +
 * GlobalExceptionCodeD/MumpsGlobalExceptionD (row 92 family);
 * _justify(tmp, gtmp, cc1, cc2) rides the unit-194 released seat
 * justifyDaf (InterpJustify.kt) with CharArray carriages; the
 * dead vendor locals (int i,j=1,k,x) ride this note.
 *
 * H-015: ASCII; no raw dollars; trailing newline.
 * Dafny: EXEMPT (formatting service over the justify kernel;
 * journal row).
 */

// mstring global::Justify(int c1, int c2) - libmpscpp.cpp
fun globalJustifyD(n: MpsGlobalNode, c1: Int, c2: Int, sv: DafMsv): String {

    // char cc1[32],cc2[32]; sprintf BEFORE the retrieve (vendor order)
    val cc1 = String.format(java.util.Locale.US, "%d", c1)
    val cc2 = String.format(java.util.Locale.US, "%d", c2)

    val gtmp = CharArray(STR_MAX_DAF)
    val f = MglobalDaf(RETRIEVE_D, justifyCstr(n.ref), gtmp, sv)
    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Justify: global not found")
    }

    val tmp = CharArray(STR_MAX_DAF)
    val cc1C = CharArray(STR_MAX_DAF)
    val cc2C = CharArray(STR_MAX_DAF)
    copyCstr(cc1C, cc1)
    copyCstr(cc2C, cc2)

    // _justify(tmp, gtmp, cc1, cc2) - the unit-194 seat
    justifyDaf(tmp, gtmp, cc1C, cc2C)

    return DafCRuntime.cstring(tmp) // tmp1 = tmp; return tmp1
}

// name-carrier helper (row 33 family)
private fun justifyCstr(src: String): CharArray {
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

private fun copyCstr(dst: CharArray, src: String) {
    var i = 0
    while (i < src.length) { dst[i] = src[i]; i++ }
    dst[i] = NUL_D
}
