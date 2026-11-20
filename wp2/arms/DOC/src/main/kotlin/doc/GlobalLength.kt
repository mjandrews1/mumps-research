package doc

// Unit 133 (B5 tranche 1): libmpscpp.cpp::global::Length (batch 5,
// simple stratum; LOC 23, score 9.9) - the global::Length member
// taking a GLOBAL needle (vendor at libmpscpp.cpp:3274-3304; the
// no-arg, mstring, std::string and char-star needle siblings are
// other-batch rows), rendered as a GlobalRef extension. Double
// Mglobal(RETRIEVE=0) walk (this global's value, then the needle's)
// with the MumpsGlobalException throw on GLOBAL_NOT_FOUND=2
// (H-014 stub seat; the vendor's rustic same-code throw on the
// second retrieval after the first succeeded is the unit-65 Concat
// quirk family - noted). The empty-haystack early return and the
// strstr walk render as an offset/substring walk per H-007; the
// piece-count semantics kept verbatim: an empty retrieved needle
// makes the walk descend forever, as in C (vendor expression kept).
// Naming per H-017 (Length -> length).

fun GlobalRef.length(needle: GlobalRef): Int {
    val gtmp = CharArray(STR_MAX)
    val gtmp1 = CharArray(STR_MAX)

    var f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    f = Stubs.Mglobal(RETRIEVE, needle.ref, gtmp1, Globals.svPtr)

    if (f == 0) { // same error code as the first arm (family quirk, noted)
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    val needleText = CstringOps.cstring(gtmp1)

    // char * p1 = (char *) gtmp;   (the retrieved value of this global)
    var p1 = CstringOps.cstring(gtmp)

    var i = 1

    if (p1.isEmpty()) return 0

    while (true) {
        // if ((p1=strstr(p1,needle))==NULL) return i; // one piece
        val hit = p1.indexOf(needleText)
        if (hit == -1) return i

        i++
        p1 = p1.substring(hit + needleText.length)
    }
}
