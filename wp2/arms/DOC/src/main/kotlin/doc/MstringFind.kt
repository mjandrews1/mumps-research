package doc

// Unit 217 (B8 tranche 1): libmpscpp.cpp::mstring::Find (batch 8,
// simple stratum; LOC 16, score 4.2, rank 8; vendor
// libmpscpp.cpp:7649, physically verified this session) - the
// GLOBAL-KEY member overload (int mstring::Find(global key, int
// st)). Roster adjudication: the mstring::Find needle family is
// char*-key at :7638, global-key here and mstring-key at :7671;
// this batch's row is the global-key row (the mstring-key sibling
// is the batch-10 row). Consult H-014 hit-reuse: the entire body is
// landed seats - Mltoa BEFORE the single Mglobal(RETRIEVE) over
// key.ref (the unit-33/156 ordering kept: the vendor renders Mltoa
// first, the Find.kt unit-33 seat writes Mglobal first - this
// render keeps the VENDOR order and notes the divergence-from-
// seat, not from vendor), GLOBAL_NOT_FOUND throw, the Mfind
// four-carrier call with data.c_str() as the haystack (the Mstring
// str carrier per the Carriers.kt invoke precedent), and the atoi
// tail on the Find.kt seat. Naming per H-017 (Find -> find).

fun Mstring.find(key: GlobalRef, st: Int): Int {
    val a = CharArray(40)
    val r = CharArray(20)
    val tmp1 = CharArray(STR_MAX)

    Stubs.Mltoa(st, a)

    val f = Stubs.Mglobal(RETRIEVE, key.ref, tmp1, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    Stubs.Mfind(r, str.toCharArray(), tmp1, a)

    val rr = atoi(CstringOps.cstring(r))
    return rr
}
