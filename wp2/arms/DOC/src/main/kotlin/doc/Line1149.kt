package doc

// Unit 291 (B10 tranche 2): libmpscpp.cpp::line1149 (batch 10,
// core stratum; units.csv row 291: loc 36, cr 0.027, ptr 0,
// ctl 12, score 19.6, rank 167; physically verified this
// session at vendor libmpscpp.cpp:1150-1190, physLOC 36) -
// the ROSTER NAME IS THE CHUNKER ARTIFACT: the units.csv id
// "line1149" is the study chunker's fallback name for the
// nameless conversion operator Hash::operator const char *()
// (no identifier before the paren head, so the row carries the
// vendor line anchor instead). This render is the READ SIDE of
// the file-backed Hash table: probe lhash(ref)%size, scan the
// key stream with the double-EOF wraparound and its loopflg
// restart-at-0 arm, c==1 and c==2 record bytes SKIPPED (the
// vendor kept both skip continuations one commented-out and
// one live: c==1 continue, c==2 continue - both "skip"
// verbatim), the NUL terminator ends each key and strcmp with
// the saved ref decides "found", then the value is read to the
// next NUL and returned as the static p text. H-015 named
// seat (a conversion operator cannot render as invoke; the
// operator= assignHash precedent): fun Hash.lookupC(); the
// NULL returns fold to Kotlin null (the EOF-in-value miss and
// the double-EOF miss are the C NULL paths). Carriers per the
// assignHash seat (H-014 hit-reuse: RandomAccessFile seek/read
// = FSEEK/fgetc, Stubs.lhash, CstringOps.cstring). The vendor
// static char p[STR_MAX] rides the call frame (letter-for-
// letter the assignHash carrier choice).
// Consult: unit 291, entry H-014, outcome hit-reuse,
// timestamp 2026-11-22T06:13:00Z.
// Naming per H-017 (the roster id line1149 kept as file base
// name per the vendor-line-artifact convention).

fun Hash.lookupC(): String? {
    val p = CharArray(STR_MAX)
    val hk = checkNotNull(hashkey)

    var i = Stubs.lhash(ref) % size
    hk.seek(i)

    var loopflg = 0
    var j: Int

    while (true) {
        j = 0
        while (true) {
            val c = hk.read() // fgetc(hashkey)

            if (c == -1 && loopflg != 0) return null // EOF twice

            if (c == -1) {
                loopflg = 1
                i = 0
                hk.seek(i)
                continue
            }

            // if (c==1) return NULL; // not found - the vendor
            // fossil comment is FOLDED INERT by its own live
            // edit: the live line below is c==1 continue
            if (c == 1) continue

            if (c == 2) continue // skip

            p[j++] = c.toChar()

            if (c.toChar() == NUL) break
        }

        // strcmp(p, ref.c_str())==0 - the key stops at its NUL
        // (p[j-1]); the compare is the prefix text vs ref
        if (p.copyOf(j - 1).concatToString() == ref) break // found
    }

    j = 0
    while (true) { // get value
        val c = hk.read()

        if (c == -1) return null

        p[j++] = c.toChar()

        if (c.toChar() == NUL) break
    }

    return CstringOps.cstring(p)
}
