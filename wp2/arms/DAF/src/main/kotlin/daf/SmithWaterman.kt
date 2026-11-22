package daf

// Unit 158 (DAF B6 T1, simple): libmpscpp.cpp::SmithWaterman
// (17 LOC, score 7.7, rank 58): the bsv char* wrapper of the
// Smith-Waterman sequence scorer: the two operand strings get the
// leading-blank carriers, the five tuning integers parse via
// sscanf %d, and the sw core returns its integer score printed into
// a static char[32].
// Consults (2026-11-22T15:46:30Z, nominal): BASE-log rows 80/101
// hit-reuse (the bsv wrapper shape of the LibmpscppMisc9 family:
// first split-arg probe, node/operand seats, opcode strings as
// CharArray carriers; the leading ' ' carriers kept verbatim as
// String concatenation per the row-101 mstring fold), row 28
// hit-adapt (the five sscanf %d calls -> the DafCRuntimeT2 atoi; the
// row-28 leading-int parse with the documented overflow clamp),
// rows 33/56 hit-adapt (sprintf(tmp, \"%d\", sax) over static char
// tmp[32] returned by pointer: the C idiom is a shared static
// buffer overwritten per call; the render returns a fresh String
// per call (row 56) with the single-buffer hazard ledgered: callers
// of the vendor code that retain the pointer across calls would
// alias; no DAF caller does), row 37 hit-reuse (the sw core is
// raised as the named-owner stub swD below: owner libmpscpp.cpp sw
// far chunk ~line 8153, not a batch T1 cell).
// Dafny: EXEMPT (stub-relay wrapper; journal row).

// int sw(char*, char*, int, int, int, int, int) - named-owner stub
// (owner libmpscpp.cpp sw family, the far chunk of the same TU near
// line 8153; not a batch T1 cell).
fun swD(s1: String, s2: String, sax: Int, smx: Int, gapx: Int,
        mmx: Int, max: Int): Int =
    throw NotImplementedError(
        "sw: out-of-scope owner (libmpscpp.cpp sw core, same-TU far chunk ~line 8153; not a batch T1 cell)")

fun SmithWatermanW(svptr: DafMsv, ep: CharArray, s1: CharArray, s2: CharArray,
                  sa: CharArray, sm: CharArray, gap: CharArray,
                  mm: CharArray, ma: CharArray): String {

    // char s1a[STR_MAX], s2a[STR_MAX]; s1a[0]=s2a[0]=' ';
    val s1a = " " + DafCRuntime.cstring(s1)
    val s2a = " " + DafCRuntime.cstring(s2)

    // sscanf(sa, "%d", &sax) - the five tuning ints (row 28 fold)
    var sax = atoi(DafCRuntime.cstring(sa))
    var smx = atoi(DafCRuntime.cstring(sm))
    var gapx = atoi(DafCRuntime.cstring(gap))
    var mmx = atoi(DafCRuntime.cstring(mm))
    var max = atoi(DafCRuntime.cstring(ma))

    sax = swD(s1a, s2a, sax, smx, gapx, mmx, max)

    return sax.toString() // sprintf(tmp, "%d", sax); return tmp (row 56 fold)
}
