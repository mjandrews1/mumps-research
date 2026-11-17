package mumps.mdh

import mumps.interp.NUL
import mumps.interp.cstringAt
import mumps.interp.sigint

/**
 * PRO-arm translation of libmpscpp.cpp::Shred (B6 unit 164).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 7907-7960.
 *
 * char *Shred(char *str, int size): the stateful fragment
 * iterator behind the MDH library - on the first call (flg==0)
 * it copies str to scratch keeping only alphanumerics
 * (lowercased); each call then returns the next size-char
 * fragment; the call past the end resets the state and
 * returns the empty string (the restart protocol the MSV
 * wrapper at LibmpscppMisc9.kt:603 and ShredQuery depend on).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch : the vendor's file-scope statics
 *    (p1/p2/flg/string/word) PERSIST ACROSS CALLS - carried
 *    on the ShredScratch object (Avg/Count Scratch precedent);
 *    malloc/free are GC, the NULL string carrier is a null
 *    CharArray?.
 *  - h012 unsigned_byte_domain : the isalnum/tolower filters
 *    are the C locale byte-domain forms (per-file private
 *    helpers, ScanAlnum precedent); word is a 128-char
 *    C-string domain.
 *  - h014 out_of_scope_call  : sigint (InterpStubs stub,
 *    owner interp-sysfunc.cpp) called as the vendor's abort
 *    path; strncpy folded to the fragment copy loop; this
 *    unit RESOLVES the MdhStubs Shred(String,Int) stub raised
 *    when the MSV wrapper landed - the stub is removed and
 *    the wrapper's call binds unchanged.
 *  - h015 grammar_collision  : the new top-level Shred
 *    replaces the removed stub one-for-one (same signature);
 *    the MSV-wrapper and ShredQuery overloads in
 *    LibmpscppMisc9.kt bind unchanged - no collisions.
 *  - h017 naming            : Shred keeps the vendor
 *    capitalization (vendor 7907).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The static "int i" is never used - a dead vendor local,
 *    dropped with this note.
 *  - The size>=128 abort prints and then FALLS THROUGH into
 *    the fragment loop (sigint is the vendor's exit path -
 *    here a stub seat, so the fall-through is kept verbatim).
 *  - The diagnostic string says "Out of memory in Token()" -
 *    Token, not Shred - a vendor copy-paste fossil kept
 *    verbatim (SQLTable println precedent for cout <<).
 *  - The while(1) loop body always returns on both paths - the
 *    loop is a vendor shape fossil, kept.
 *  - p1 advances by exactly size even past the content end;
 *    the p1>p2 test (not >=) decides the reset - so the LAST
 *    PARTIAL FRAGMENT (p1 < p2 but fewer than size chars
 *    left) is returned NUL-padded via the strncpy carrier -
 *    kept exactly.
 *  - The empty-content first-call path (p2==string, nothing
 *    alnum) returns "" WITHOUT setting flg - every call
 *    re-copies - kept.
 */

/** vendor statics (libmpscpp.cpp:7909-7912) - persist across calls (h008). */
object ShredScratch {
    /** vendor: static char *p1 (index into string) / *p2 (end index). */
    var p1: Int = 0
    var p2: Int = 0
    /** vendor: static int flg - 0 = (re)copy on next call. */
    var flg: Int = 0
    /** vendor: static char *string - the alnum-only scratch copy. */
    var string: CharArray? = null
    /** vendor: static char word[128] - the fragment carrier. */
    val word: CharArray = CharArray(128)
}

/** C isalnum (unsigned-char domain, C locale). */
private fun isalnumC(c: Char): Boolean =
    c in '0'..'9' || c in 'A'..'Z' || c in 'a'..'z'

/** C tolower (unsigned-char domain; non-letters unchanged). */
private fun tolowerC(c: Char): Char = if (c in 'A'..'Z') c + 32 else c

fun Shred(str: String, size: Int): String {
    val s = ShredScratch

    if (size >= 128) {
        println("Shred fragment size too large") // cout << ... << endl
        sigint(100)
    }

    if (s.flg == 0) {
        val src = str
        s.string = null // free(string) - GC

        val string = CharArray(src.length + 1) // malloc(strlen+1)
        s.string = string

        var p2 = 0
        for (k in src.indices) { // copy to local string
            val c = src[k]
            if (isalnumC(c)) { // permit only alphanumerics
                string[p2] = tolowerC(c)
                p2++
            }
        }

        if (p2 == 0) { // p2==string: nothing copied
            return ""
        }

        string[p2] = NUL
        s.p2 = p2
        s.flg = 1 // don't reenter this block until string consumed
        s.p1 = 0 // p1=string
    }

    while (true) {
        val string = s.string ?: error("Shred scratch string carrier") // no-double-bang carrier
        val word = s.word
        // strncpy(word,p1,size): copy up to size, NUL-pad past
        // the content end; word[size]=0
        for (k in 0 until size) {
            word[k] = if (s.p1 + k <= s.p2) string[s.p1 + k] else NUL
        }
        word[size] = NUL
        s.p1 = s.p1 + size

        if (s.p1 > s.p2) { // fragment past the end: reset state
            s.flg = 0
            s.string = null // free(string); string=NULL
            return ""
        }

        return cstringAt(word)
    }
}
