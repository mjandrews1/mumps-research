package doc

// Unit 129 (B5 tranche 1): libmpscpp.cpp::mstring::Length (batch 5,
// simple stratum; LOC 16, score 7.8) - the mstring member taking a
// GLOBAL needle (vendor at libmpscpp.cpp:3406-3427; the char-star,
// std::string and mstring needle siblings are later-batch rows).
// Retrieved needle value via Mglobal(RETRIEVE=0) with the
// MumpsGlobalException throw on GLOBAL_NOT_FOUND=2 (H-014 stub seat);
// the scan is a strstr walk over the member content rendered as an
// offset/substring walk per H-007 (pointer-into-buffer role); the
// piece-count semantics are kept verbatim: an empty needle text makes
// the walk descend forever, as in C (vendor expression kept).
// Naming per H-017 (Length -> length).

fun Mstring.length(needle: GlobalRef): Int {
    val gtmp = CharArray(STR_MAX)

    val f = Stubs.Mglobal(RETRIEVE, needle.ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    val needleText = CstringOps.cstring(gtmp)

    // char * p1 = (char *) data.c_str();
    var p1 = str

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
