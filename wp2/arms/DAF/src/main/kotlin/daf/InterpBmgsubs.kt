package daf

// Unit 284 (DAF B10 T3, residue): interp-bmgsubs.c::<misc> (units.csv
// row 284: 121 physical LOC): the Boyer/Moore/Gosper search TU's
// residue chunk: the bmg_data struct + macros (rows 6/22 fold),
// bmg_setup (the delta0/cmap build), bmg_act, bmg_fullsearch
// (K. O'Kane's additions) and Bcopy; the header comment (public-
// domain ftp.uu.net provenance banner) LEDGERED not emitted
// (banner-residue precedent; bmg_found is a row 6 file-scope
// global). bmg_search (vendor :134) and bmg_fsearch (:218) are
// OUTSIDE this chunk (they own bmg_str/linetemp, ledgered).
// SEAT RESOLUTION: the bmgFullsearchD stub of LibmpscppMisc9.kt
// (blamed "selftext.cpp bmg_fullsearch; not a corpus unit") is
// RELEASED here - the vendor body is interp-bmgsubs.c:117, a
// row-284 cell (TAG CORRECTION, the selftext blame was wrong).
// Consults (2026-11-25T15:04:00Z, nominal): row 6/22 hit-reuse
// (the bmdata struct carrier renders as the DafBmg object),
// row 33 hit-reuse (strlen/strcpy folds), row 56 hit-adapt
// (isupper/tolower/toupper via the ASCII helpers), row 54/68
// hit-adapt (the int (*)(char*, int) action_func pointer folds
// onto the bmg_act fixed seat - the vendor passes only bmg_act,
// DISCLOSED), row 37 hit-reuse (bmg_search RAISED as a named-owner
// stub: owner interp-bmgsubs.c:134, outside the row-284 chunk),
// row 96 hit-reuse (LARGE = BUFSIZE+PATSIZE = 59000 pin).
// VENDOR QUIRKS kept: (1) pattern[0]; in bmg_setup is a stray
// expression statement, no effect (kept comment-only); (2) cmap
// built for all 256 entries even unfolded; (3) delta0 LAST-pattern
// slot set to LARGE (59000, effectively "keep going"); (4) the
// fold arm writes delta0 of BOTH lower and upper case letters
// (the upper-case write happens after the pattern loop via
// toupper) - folded search maps upper input to lower cmap anyway,
// the doubled pins are vendor-faithful; (5) bmg_fullsearch walks
// orgoff += bmg_found (the LAST hit position, not the end of the
// hit); (6) case_fold_flag==0 only (the bmgFullsearchD caller
// passes folded=0; bmg_setup keeps the folded arm for the
// bmg_search owner).
// Dafny: EXEMPT (delta-table relay lattice over the raised search
// stub; journal row).

// BUFSIZE 50000 / PATSIZE 9000 (vendor defines; the matrix sizes
// ride the IntArray/CharArray carriers, not the raw char bufs -
// bmg_str/linetemp belong to the out-of-chunk bmg_search)
const val BMG_BUFSIZE_D = 50000
const val BMG_PATSIZE_D = 9000
const val BMG_LARGE_D = BMG_BUFSIZE_D + BMG_PATSIZE_D // LARGE

// struct bmg_data bmdata (row 6/22 fold; the bmg_found global is a
// member here - KO'Kane moved it into the struct domain as int
// bmg_found=0 beside the macro family; kept file-scope)
object DafBmg {
    val delta0 = IntArray(256)   // bmg_delta0 - ascii only
    val cmap = CharArray(256)    // (un)folded characters
    var pattern = ""             // bmg_pattern (PATSIZE cap)
    var patlen = 0
    var bmgFound = 0             // int bmg_found
}

// int bmg_search(char*, int, int(*)()) - named-owner stub (owner
// interp-bmgsubs.c:134, outside the row-284 chunk; returns the
// offset of the hit or 0 per the bmg contract)
fun bmgSearchD(buffer: String, action: (String, Int) -> Int): Int =
    throw NotImplementedError(
        "bmg_search: out-of-scope owner (interp-bmgsubs.c:134; outside the row-284 residue chunk)")

// int bmg_act(char *p, int i) { bmg_found = i; return 1; }
fun bmgActD(p: String, i: Int): Int {
    DafBmg.bmgFound = i
    return 1
}

// void bmg_setup(char *pat, int folded) - the delta0/cmap build
fun bmgSetupD(pat: String, folded: Boolean) {
    // the vendor zero-clear of str/linetemp rides the out-of-
    // chunk bmg_search carrier; pattern[0] is a stray no-op
    // statement (quirk 1, kept comment-only).
    DafBmg.patlen = pat.length // patlen = strlen(pat)

    // pattern save (fold arm keeps case-fold while copying)
    if (folded) {
        val sb = StringBuilder()
        for (j in 0 until DafBmg.patlen) {
            val c = pat[j]
            sb.append(if (c in 'A'..'Z') c.lowercaseChar() else c)
        }
        DafBmg.pattern = sb.toString()
    } else {
        DafBmg.pattern = pat.substring(0, DafBmg.patlen) // Bcopy
    }

    // for (j=0; j<256; j++) { delta0[j]=patlen; cmap[j]=j; }
    for (j in 0 until 256) {
        DafBmg.delta0[j] = DafBmg.patlen
        DafBmg.cmap[j] = j.toChar()
    }

    // for (j=0; j<patlen-1; j++) delta0[pattern[j]] = patlen-j-1;
    for (j in 0 until DafBmg.patlen - 1) {
        DafBmg.delta0[DafBmg.pattern[j].code] = DafBmg.patlen - j - 1
    }
    // delta0[pattern[patlen-1]] = LARGE;
    DafBmg.delta0[DafBmg.pattern[DafBmg.patlen - 1].code] = BMG_LARGE_D

    if (folded) {
        // the doubled upper-case pins (quirk 4)
        for (j in 0 until DafBmg.patlen - 1) {
            val c = DafBmg.pattern[j]
            if (c in 'a'..'z') DafBmg.delta0[c.uppercaseChar().code] =
                DafBmg.patlen - j - 1
        }
        val last = DafBmg.pattern[DafBmg.patlen - 1]
        if (last in 'a'..'z')
            DafBmg.delta0[last.uppercaseChar().code] = BMG_LARGE_D
        // for (j='A'; j<='Z'; j++) cmap[j] = tolower(j);
        for (c in 'A'..'Z') DafBmg.cmap[c.code] = c.lowercaseChar()
    }
}

// int bmg_fullsearch(char *p, char *inp) - RELEASES the
// bmgFullsearchD stub of LibmpscppMisc9.kt (TAG CORRECTION: this
// vendor seat is interp-bmgsubs.c:117, a row-284 cell, not
// selftext.cpp)
fun bmgFullsearchD(p: String, inp: String): Int {
    var score = 0
    bmgSetupD(p, false)       // bmg_setup(p, 0)
    DafBmg.bmgFound = 0       // bmg_found = 0
    var orgoff = 0
    val len = inp.length      // len = strlen(inp)
    while (orgoff < len) {
        val bmx = bmgSearchD(inp.substring(orgoff), ::bmgActD)
        if (bmx == 0) break   // if (!bmx) break
        score += bmx
        orgoff += DafBmg.bmgFound  // orgoff += bmg_found (quirk 5)
    }
    return score
}

// void Bcopy(char *from, char *to, int len) - the byte copy
// (bmg_setup's fold==0 arm uses strlen-copy through the String
// substring; the kin ridges keep this seat for the record - the
// vendor declared it file-scope)
fun bmgBcopyD(from: CharArray, to: CharArray, len: Int) {
    var q = 0
    while (q < len) { to[q] = from[q]; q++ }
}
