package mumps.interp

/**
 * PRO-arm translation of interp-bmgsubs.c::<misc> (roster row 285,
 * batch 10; units.csv: loc 121, comment_ratio 0.284,
 * pointer_marks 15, control_marks 23, score 76.3, rank 258).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source: the residual <misc> chunk of interp-bmgsubs.c - the
 * file-header block (the bmgsubs.Z provenance banner, the entries
 * documentation, the BSD/BUFSIZE/PATSIZE/LARGE/NL/EOS define
 * residue, the struct bmg_data declaration with its #define
 * aliases, and the O'Kane modification prototypes), then the four
 * bodies the chunk carries: bmg_setup, bmg_act, bmg_fullsearch,
 * Bcopy. The chunk reproduces the study tool's misc residue
 * exactly (loc 121, cr 0.284, ptr 15, ctrl 23, score 76.3).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext        : delta0/cmap/str/linetemp/pattern/
 *    patlen/nsuccess ride the DONE BmgData carrier (Gotamatch.kt
 *    unit 17 h004 seat; struct body lines 84-93 verified, no
 *    redeclaration); the file-scope int bmg_found rides this
 *    file's private bmgFound.
 *  - h007 static_scratch    : bmdata is the vendor file-scope
 *    struct singleton (already BmgData); bmg_found likewise rides
 *    a top-level private var, persisting as the vendor's does.
 *  - h012 unsigned_byte_domain: pat/inp/pattern byte domains ride
 *    CharArray; the &inp[orgoff] interior pointer renders as
 *    copyOfRange(orgoff, len) (the search mutates its window's
 *    last byte to NL; vendor mutations land in the shared inp
 *    storage after orgoff, the copies never read them again -
 *    benign superset, BmgSearch NL-forcing precedent).
 *  - h013 dead_code          : the vendor stray statement
 *    "pattern[0];" (a bare array read with no effect) is kept as
 *    this ledger note, not rendered; delta0[0]=0/cmap[0]=0/
 *    str[0]=0/linetemp[0]=0/patlen=0 are rendered as written.
 *  - h014 out_of_scope_call  : bmg_search is the DONE B5 seat
 *    (BmgSearch.kt); strlen is strlenC (CstringOps.kt).
 *  - h015 grammar_collision : the action callback's int
 *    truthiness rides the (String, Int) -> Boolean contract of the
 *    bmg_search seat (BmgSearch.kt precedent); bmg_act returns
 *    the vendor's 1 as true (the char* p argument of bmg_act is
 *    never read by its body - dead-parameter note, h013).
 *  - h017 naming            : bmg_setup/bmg_act/bmg_fullsearch/
 *    Bcopy keep the vendor names.
 *  - h018 preprocessor_conditional: the #ifdef BSD index alias,
 *    the commented malloc prototype, and the #ifndef define
 *    guards are fossil text, carried as this ledger note
 *    (BUFSIZE 50000 / PATSIZE 9000 / LARGE BUFSIZE+PATSIZE / NL
 *    newline / EOS NUL).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - bmg_setup's fold loop and cmap A-Z lowering mirror the vendor
 *    byte-for-byte, including the compile-time-might cmap fill.
 *  - bmg_act is the file-scope callback that records the hit
 *    offset in bmg_found and always accepts the line.
 */
private const val PATSIZE = 9000   // interp-bmgsubs.c:78
private const val BUFSIZE = 50000  // interp-bmgsubs.c:75
private const val LARGE = BUFSIZE + PATSIZE // interp-bmgsubs.c:81
private const val NL = '\n'         // interp-bmgsubs.c:82

/** interp-bmgsubs.c:66 - int bmg_found=0 (O'Kane file-scope). */
private var bmgFound = 0

/**
 * void bmg_setup(pat, folded) - compute the "boyer-moore" delta
 * table (vendor misc body; vendor name kept).
 */
fun bmg_setup(pat: CharArray, folded: Int) {
    var j = 0 // vendor: register int j=0
    BmgData.delta0[0] = 0 // bmdata.bmg_delta0
    BmgData.cmap[0] = 0.toChar()   // bmdata.bmg_cmap
    BmgData.str[0] = 0.toChar()    // bmdata.bmg_str
    BmgData.linetemp[0] = 0.toChar() // bmdata.bmg_linetemp
    // pattern[0]; // bmdata.bmg_pattern - stray vendor read (h013)
    BmgData.patlen = 0   // bmdata.bmg_patlen

    BmgData.patlen = strlenC(pat)
    if (folded != 0) { /* fold case while saving pattern */
        for (j in 0 until BmgData.patlen) {
            BmgData.pattern[j] =
                if (Character.isUpperCase(pat[j])) pat[j].lowercaseChar()
                else pat[j]
        }
    } else {
        Bcopy(pat, BmgData.pattern, BmgData.patlen)
    }
    for (j in 0 until 256) {
        BmgData.delta0[j] = BmgData.patlen
        BmgData.cmap[j] = j.toChar() // could be done at compile time
    }
    for (j in 0 until BmgData.patlen - 1) {
        BmgData.delta0[BmgData.pattern[j].code] = BmgData.patlen - j - 1
    }
    BmgData.delta0[BmgData.pattern[BmgData.patlen - 1].code] = LARGE
    if (folded != 0) {
        for (j in 0 until BmgData.patlen - 1) {
            if (Character.isLowerCase(BmgData.pattern[j])) {
                BmgData.delta0[BmgData.pattern[j].uppercaseChar().code] =
                    BmgData.patlen - j - 1
            }
        }
        if (Character.isLowerCase(BmgData.pattern[BmgData.patlen - 1])) {
            BmgData.delta0[BmgData.pattern[BmgData.patlen - 1].uppercaseChar().code] = LARGE
        }
        for (j in 'A'..'Z') {
            BmgData.cmap[j.code] = j.lowercaseChar()
        }
    }
}

/** int bmg_act(char *p, int i) - record the hit offset, accept. */
fun bmg_act(p: CharArray, i: Int): Boolean {
    bmgFound = i
    return true // vendor: return 1
}

/**
 * int bmg_fullsearch(char *p, char *inp) - O'Kane full-buffer
 * score: repeat bmg_search over the shifting window and sum the
 * per-hit candidates until the window exhausts. (TAG CORRECTION,
 * u285: the mdh MdhStubs core stub wore a "not a corpus unit" tag
 * - the core IS this corpus unit; the stub resolves as a thin
 * delegate over this seat.)
 */
fun bmg_fullsearch(p: CharArray, inp: CharArray): Int {
    var score = 0
    bmg_setup(p, 0)
    bmgFound = 0
    var orgoff = 0
    val len = strlenC(inp)
    while (orgoff < len) {
        val window = inp.copyOfRange(orgoff, len)
        val bmx = bmg_search(window, strlenC(inp, orgoff), ::bmgActRef)
        if (bmx == 0) break
        score += bmx
        orgoff += bmgFound
    }
    return score
}

/** bmg_search seat adapter: the vendor passes bmg_act directly. */
private fun bmgActRef(s: String, i: Int): Boolean =
    bmg_act(CharArray(1), i)

/** void Bcopy(from, to, len) - vendor byte copy. */
fun Bcopy(from: CharArray, to: CharArray, len: Int) {
    var f = 0
    var t = 0
    var n = len
    while (n-- > 0) {
        to[t++] = from[f++]
    }
}
