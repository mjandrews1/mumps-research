package doc

// UNIT 305 RENDER - batch 10 DOC tranche T3 (complex).
// interp-bmgsubs.c::<misc> - roster row 285, loc 121. The misc
// pool body: bmg_setup (the Boyer-Moore-Gosper delta table),
// bmg_act (the score-accumulating callback), bmg_fullsearch
// (K. O'Kane's free-search wrapper looping bmg_search with a
// moving window) and Bcopy (the raw copy that kings the
// fold-off pattern arm).
//
// ADJUDICATIONS:
// - The bmdata struct and the vendor macro family (delta0/
//   cmap/str/linetemp/pattern/patlen keyed at bmdata fields)
//   ride the LANDED unit-86 seat object BmgFsearchStatic
//   (BmgFsearch.kt, the H-004 owner); bmg_search is the
//   landed unit-148 render (BmgSearch.kt); bmg_fsearch and
//   gotamatch stay in their own landed units - no second
//   definitions here (the SyncClock one-render rule).
// - The ftp.uu.net / Boyer-Moore-Gosper attribution banner
//   and the BSD/BUFSIZE/PATSIZE/NL/EOS define block are
//   ledgered: BUFSIZE 50000/PATSIZE 9000 live in the unit-86
//   file-private seats; LARGE = BUFSIZE + PATSIZE rides the
//   local const val LARGE_BMS (59000) per H-017.
// - bmg_found (the file-scope int K. O'Kane added) becomes the
//   private var bmgFoundB (H-017 suffix; one owner per the
//   BmgStatic nsuccess precedent).
// - pattern[0]; (the vendor no-op statement after the str[0]
//   zeroing) is an H-013 ledger row here.
// - bmg_search(&inp[orgoff], strlen(...), bmg_act) renders as
//   the copyOfRange window over the NUL-terminated CharArray
//   (H-007 index-web idiom); the C strcmp-grade strlen rides
//   CstringOps.strlenC.
// - The Stubs.bmgFullSearch seat (String pair) flips to the
//   bmgFullSearchStrM10 adapter: CharArray(0)-terminated
//   conversions, CZNUL appended via CstringOps copyAt seats.
//
// Consult row logged 2026-11-22T06:25:00Z (H-004 hit-reuse,
// the unit-86/148 bmdata seats).

private const val LARGE_BMS = 59000 // vendor define LARGE = BUFSIZE + PATSIZE

private var bmgFoundB = 0 // int bmg_found (file scope)

// void bmg_setup(char * pat, int folded)
fun bmg_setup(pat: CharArray, folded: Int) {
    // compute "boyer-moore" delta table ... HAKMEM lives ...
    BmgFsearchStatic.delta0[0] = 0 // bmdata.bmg_delta0
    BmgFsearchStatic.cmap[0] = NUL // bmdata.bmg_cmap
    BmgFsearchStatic.str[0] = NUL // bmdata.bmg_str
    BmgFsearchStatic.linetemp[0] = NUL // bmdata.bmg_linetemp
    // pattern[0]; - vendor no-op statement (H-013)
    BmgFsearchStatic.patlen = 0 // bmdata.bmg_patlen

    // multibyte comment kept (vendor banner)

    BmgFsearchStatic.patlen = CstringOps.strlenC(pat)
    if (folded != 0) { // fold case while saving pattern
        var j = 0
        while (j < BmgFsearchStatic.patlen) {
            BmgFsearchStatic.pattern[j] =
                if (pat[j] in 'A'..'Z') (pat[j] + 32) else pat[j]
            j++
        }
    } else {
        Bcopy(pat, BmgFsearchStatic.pattern, BmgFsearchStatic.patlen)
    }
    var j = 0
    while (j < 256) {
        BmgFsearchStatic.delta0[j] = BmgFsearchStatic.patlen
        BmgFsearchStatic.cmap[j] = j.toChar() // compile-time fold comment
        j++
    }
    j = 0
    while (j < BmgFsearchStatic.patlen - 1) {
        BmgFsearchStatic.delta0[BmgFsearchStatic.pattern[j].code] =
            BmgFsearchStatic.patlen - j - 1
        j++
    }
    BmgFsearchStatic.delta0[
        BmgFsearchStatic.pattern[BmgFsearchStatic.patlen - 1].code
    ] = LARGE_BMS
    if (folded != 0) {
        j = 0
        while (j < BmgFsearchStatic.patlen - 1) {
            if (BmgFsearchStatic.pattern[j] in 'a'..'z') {
                BmgFsearchStatic.delta0[BmgFsearchStatic.pattern[j].code - 32] =
                    BmgFsearchStatic.patlen - j - 1
            }
            j++
        }
        if (BmgFsearchStatic.pattern[BmgFsearchStatic.patlen - 1] in 'a'..'z') {
            BmgFsearchStatic.delta0[
                BmgFsearchStatic.pattern[BmgFsearchStatic.patlen - 1].code - 32
            ] = LARGE_BMS
        }
        j = 'A'.code
        while (j <= 'Z'.code) {
            BmgFsearchStatic.cmap[j] = (j + 32).toChar()
            j++
        }
    }
}

// int bmg_act(char * p, int i)
fun bmg_act(p: CharArray, i: Int): Int {
    bmgFoundB = i
    return 1
}

// int bmg_fullsearch(char * p, char * inp)
fun bmg_fullsearch(p: CharArray, inp: CharArray): Int {
    var score = 0
    bmg_setup(p, 0)
    bmgFoundB = 0
    var orgoff = 0
    val len = CstringOps.strlenC(inp)
    while (orgoff < len) {
        val win = inp.copyOfRange(orgoff, len + 1) // &inp[orgoff]
        val bmx = bmg_search(win, CstringOps.strlenC(win), ::bmg_act)
        if (bmx == 0) break
        score += bmx
        orgoff += bmgFoundB
    }
    return score
}

// void Bcopy(char * from, char * to, int len)
fun Bcopy(from: CharArray, to: CharArray, len: Int) {
    var i = 0
    while (i < len) {
        to[i] = from[i]
        i++
    }
}

// The Stubs.bmgFullSearch String-pair adapter: NUL-terminated
// CharArray views over the String seats (H-015 String fold).
fun bmgFullSearchStrM10(p: String, inp: String): Int {
    val pcz = CharArray(p.length + 1)
    CstringOps.copyAtS(pcz, 0, p)
    val icz = CharArray(inp.length + 1)
    CstringOps.copyAtS(icz, 0, inp)
    return bmg_fullsearch(pcz, icz)
}
