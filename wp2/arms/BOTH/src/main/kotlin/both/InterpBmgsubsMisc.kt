package both

// Unit 302 (BOTH B10, complex): interp-bmgsubs.c::<misc>
// (121 LOC). Batch_plan ordinal 302; units.csv row 284. The
// bmg TU residue MODEL: the banner/attribution block and the
// BSD/BUFSIZE/PATSIZE defines are residue (BUFSIZE/PATSIZE/
// NL/LARGE seat at the B5 unit-148 consts); the model is the
// bmg_data file-scope struct macro lattice (already seated
// BmgData, the B5 carrier - this render LEDGERS to it, the
// single-owner rule), bmg_setup (the delta0/cmap table
// builder), bmg_act (the hit recorder), bmg_fullsearch (the
// O'Kane score loop), and Bcopy. NOTE: the B5 unit-148
// release note said "no interp-bmgsubs.c residue unit exists
// on the roster" - the roster row 302 proves the residue unit
// IS rostered; this render is that unit (disclosed for PI).
//
// Vendor behavior:
//   bmg_setup(pat, folded): patlen=strlen(pat); the pattern
//   saves case-folded (isupper -> tolower) when folded, else
//   Bcopy verbatim; delta0[j]=patlen for all 256, then
//   delta0[pattern[j]]=patlen-j-1 for j<patlen-1, and the
//   LAST pattern byte gets LARGE; folded also mirrors the
//   delta0 entries under toupper and folds cmap 'A'..'Z' to
//   lowercase.
//   bmg_act(p, i): bmg_found=i; return 1.
//   bmg_fullsearch(p, inp): setup unfolded, bmg_found=0, then
//   walk the input: bmg_search over the remaining slice, stop
//   on a zero return, score += the return, orgoff +=
//   bmg_found (the last hit's slice offset).
//   Bcopy(from,to,len): the byte copy loop.
//
// BOTH consults (find_rule before render, events logged):
//   h008 static_scratch - reused: the vendor's pattern[0];
//   dead store (a bare expression statement) is NOT emitted
//   (the h013 dead-store action, journal row); the rest of
//   the setup writes are live table fills.
//   h014 out_of_scope_call - reused and hit: bmg_search rides
//     its unit-148 seat (BmgSearch.kt); the bmg_data struct
//     ledgers to the B5 BmgData carrier (single-owner); the
//     bmgFullsearchZb stubs in BifsZfcn/ZfcnsZfcn (pinned
//     "non-roster cross-TU helper" - a MISLABEL: the true
//     owner is THIS unit, roster 302) flip to delegate on
//     this push (the h014 delegate-when-owner-lands flip).
//   h017 naming - reused: bmg_setup/bmg_act/bmg_fullsearch/
//     Bcopy keep vendor names (bmgSetup/bmgAct/bmgFullsearch/
//     Bcopy snake->camel where multiword).
//   h013 dead_store - reused and hit: the pattern[0] bare
//     statement (see h008 note above).
//   Dafny: EXEMPT (the delta-table build is the BMG family
//     envelope; the search walk is the unit-148 exemption).
//
// Vendor quirks kept (ledgered):
//   - the setup's first four statements (delta0[0]=0, cmap[0]
//     =0, str[0]=0, linetemp[0]=0) are dead stores for the
//     setup path (the tables are fully overwritten below);
//     the B5 carrier omits str/linetemp (the fsearch file
//     face owns them, roster 86 ahead of the committed
//     order); journaled.
//   - bmg_fullsearch's bmx return is nsuccess (hits), and
//     orgoff advances by bmg_found - the LAST hit's
//     position - so overlapping hits between iterations are
//     walked slice-wise; kept verbatim.

// int bmg_found - interp-bmgsubs.c:107 (file scope).
internal var bmgFound = 0

// void bmg_setup(char *, int) - interp-bmgsubs.c:110
internal fun bmgSetup(pat: CharArray, folded: Int) {
    BmgData.patlen = strlenC(pat)

    if (folded != 0) { // fold case while saving pattern
        for (j in 0 until BmgData.patlen) {
            val c = pat[j]
            BmgData.pattern[j] =
                if (c.isUpperCase()) c.lowercaseChar() else c
        }
    } else {
        Bcopy(pat, BmgData.pattern, BmgData.patlen)
    }

    for (j in 0 until 256) {
        BmgData.delta0[j] = BmgData.patlen
        BmgData.cmap[j] = j.toChar() // could be done at compile time
    }

    for (j in 0 until BmgData.patlen - 1) {
        BmgData.delta0[BmgData.pattern[j].code and 0xFF] = BmgData.patlen - j - 1
    }
    BmgData.delta0[BmgData.pattern[BmgData.patlen - 1].code and 0xFF] = BMG_LARGE

    if (folded != 0) {
        for (j in 0 until BmgData.patlen - 1) {
            val c = BmgData.pattern[j]
            if (c.isLowerCase()) {
                BmgData.delta0[c.uppercaseChar().code and 0xFF] = BmgData.patlen - j - 1
            }
        }
        val last = BmgData.pattern[BmgData.patlen - 1]
        if (last.isLowerCase()) {
            BmgData.delta0[last.uppercaseChar().code and 0xFF] = BMG_LARGE
        }
        for (j in 'A'..'Z') {
            BmgData.cmap[j.code] = j.lowercaseChar()
        }
    }
}

// int bmg_act(char *, int) - interp-bmgsubs.c:139
internal fun bmgAct(p: String, i: Int): Int {
    bmgFound = i
    return 1
}

// int bmg_fullsearch(char *, char *) - interp-bmgsubs.c:143
internal fun bmgFullsearch(p: String, inp: String): Int {
    var score = 0
    bmgSetup(p.toCharArray(), 0)
    bmgFound = 0
    var orgoff = 0
    val len = inp.length

    while (orgoff < len) {
        // bmg_search(&inp[orgoff], strlen(&inp[orgoff]), bmg_act);
        val slice = inp.substring(orgoff).toCharArray()
        val bmx = bmg_search(slice, slice.size, ::bmgAct)
        if (bmx == 0) break
        score += bmx
        orgoff += bmgFound
    }
    return score
}

// void Bcopy(char *, char *, int) - interp-bmgsubs.c:160
internal fun Bcopy(from: CharArray, to: CharArray, len: Int) {
    for (n in 0 until len) to[n] = from[n]
}
