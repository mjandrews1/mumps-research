package mumps.mdh

import mumps.interp.cstringAt
import mumps.interp.strncmpC

// ----------------------------------------------------------------------------
// (B10 unit 291) libmpscpp.cpp::line1149 (Hash::operator const char
// *, vendor reference lines 1149-1205). Identity: units.csv row 291
// - libmpscpp.cpp::line1149, cpp, loc 36, comment_ratio 0.037,
// pointer_marks 0, control_marks 12, score 19.6 - exact match.
// ----------------------------------------------------------------------------

/**
 * Hash::operator const char *() (libmpscpp.cpp:1149-1205): the
 * hash-file LOOKUP arm - the read-side twin of the class Hash
 * insert/replace body (LibmpscppMisc1.kt assign, batch-5 seat):
 * seek to lhash(ref) % size, walk the fgetc chain (1 = empty slot
 * marker / not-found, 2 = skip pad - both continue; EOF restarts
 * ONCE from offset 0 with loopflg, then a SECOND EOF returns
 * NULL), p[j++] = c accumulates the candidate key NUL-closed,
 * strcmp against ref; on match a second get-value walk reads the
 * value and returns it.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : static char p[STR_MAX] + static
 *    string r render as the Line1149Scratch object (h007
 *    static-scratch carrier, ConCheck/InterpStrmanipMisc
 *    precedent); p is the fgetc byte accumulator, NUL-closed.
 *  - h012 unsigned_byte_domain: fgetc bytes (Int -1 == EOF) widen
 *    into the Char domain exactly as the assign seat does.
 *  - h014 out_of_scope_call  : lhash is the DONE mdh seat
 *    (LibmpscppMisc5.kt:679, h014-owner noted there); strncmpC
 *    is the interp CstringOps seat (strcmp over the NUL-closed
 *    buffer vs ref.c_str()); hashkey is the widened
 *    LibmpscppMisc1 class member (see the visibility note).
 *  - h015 grammar_collision  : the operator name has no Kotlin
 *    analog - constCharStar keeps the arm; the free Hash(String)
 *    4-byte-sum function (LibmpscppMisc5.kt:654) is a DIFFERENT
 *    vendor unit (2314), no collision.
 *  - h017 naming             : line1149/vendor operator arms keep
 *    their seat names documented; scratch/carrier names carry
 *    the (u291) notes.
 *
 * VISIBILITY NOTE (u291): the class Hash field `hashkey` in
 * LibmpscppMisc1.kt is widened from private to internal so this
 * sibling seat can walk the same RandomAccessFile - behavior
 * unchanged, the LibmpscppMisc5 wrapper pair and the assign seat
 * bind unchanged.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The commented-out "// if (c==1) return NULL; // not found"
 *    fossil above the live "if (c==1) continue;" is kept as this
 *    note (h013 dead text).
 *  - c==2 "continue" skips the pad byte; c==EOF AFTER loopflg
 *    returns NULL - the walk wraps the file exactly ONCE; kept.
 *  - The key match walk breaks on strcmp==0 WITHOUT consuming the
 *    value; the SECOND while(1) then reads the value bytes with
 *    its own EOF-null return - kept verbatim as the two separate
 *    walks (outer@ label rides the restart arm).
 */
private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, u44 precedent)
private const val EOF = -1 // C fgetc end-of-file marker

/** vendor: static char p[STR_MAX] + static string r (h007 scratch). */
private object Line1149Scratch {
    val p = CharArray(STR_MAX)
}

/** vendor: Hash::operator const char *() - the lookup arm. */
fun Hash.constCharStar(): String? {
    val f = hashkey // widened class member (u291 note above)
    val scratch = Line1149Scratch.p
    var i = lhash(ref) % size // i = lhash(ref.c_str())%size
    f.seek(i) // FSEEK(hashkey, i, SEEK_SET)
    var loopflg = false

    var c: Int
    outer@ while (true) {
        var j = 0L

        while (true) { // key walk
            c = f.read() // fgetc(hashkey)

            if (c == EOF && loopflg) return null // second EOF: not found

            if (c == EOF) {
                loopflg = true
                i = 0
                f.seek(i) // restart once from offset 0
                continue
            }

            if (c == 1) continue // empty-slot marker

            if (c == 2) continue // skip pad

            scratch[j.toInt()] = c.toChar() // p[j++] = c
            j++

            if (c == 0) break // NUL: key candidate complete
        }

        if (strncmpC(scratch, 0, ref, ref.length) == 0) break // found
        // vendor: while(1) again (chain walk continues)
        continue@outer
    }

    var jj = 0
    while (true) { // get value
        c = f.read()
        if (c == EOF) return null
        scratch[jj++] = c.toChar() // p[j++] = c (value walk reuses p)
        if (c == 0) break
    }

    return cstringAt(scratch) // return p (the NUL-closed window)
}
