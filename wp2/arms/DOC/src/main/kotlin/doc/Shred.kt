package doc

// Unit 176 (B6 tranche 3): libmpscpp.cpp::Shred (batch 6, complex
// stratum; roster row 176: loc 40, score 37.4, batch_plan strata
// order). The stateful fragment iterator behind the shred call:
// the first call strips the input to lowercase alphanumerics into
// a retained buffer, every call hands back the next fixed-size
// fragment, and the reset return clears the retained state for
// the next first-call. Seat table: the sigint abort rides the
// Stubs.sigint seat (SYM.h:41) with the VENDOR FALL-THROUGH kept
// in shape; atoi is the arm Find.kt seat; the C statics p1/p2/
// flg/string/word PERSIST ACROSS CALLS on the ShredScratch
// object (the BtreeStatic file-scope carrier precedent, H-008) -
// the iterator protocol depends on it. The SymOpsMisc.kt Shred
// wrapper (the MSV svptr/ep/arg1/arg2 shape) binds unchanged
// through the Stubs.shred seat, which this unit resolves.
// Vendor quirks kept and noted: the "Out of memory in Token()"
// diagnostic is a copy-paste fossil (Token, not Shred) - kept
// verbatim; the always-returning while(1) loop with its single
// return inside (shaped as a Kotlin while(true) with the same
// observable returns); the p1 follows p2 exhausted test is
// strictly GREATER (p1 greater than p2), which hands back the
// NUL-padded last partial fragment before resetting; the dead
// static int i of the vendor is dropped (H-013); the empty-input
// early return leaves flg at 0 so every call re-copies - kept.
// free/malloc fold to GC + fresh CharArray (H-008). Private
// isalnumC/tolowerC are per-file C-locale byte-domain helpers
// (StopInit precedent, H-012). Naming per H-017.

// file-scope statics carrier (H-008: state persists across calls)
private object ShredScratch {
    var p1 = 0                // read cursor into string
    var p2 = 0                // after-last-written index
    var flg = 0               // 0: next call re-copies the input
    var string: CharArray? = null
    val word = CharArray(128)
}

fun shred(str: String, size: Int): String {
    val s = ShredScratch

    if (size >= 128) {
        println("Shred fragment size too large")
        Stubs.sigint(100)
    }

    if (s.flg == 0) {
        // first call for this input: copy the lowercase
        // alphanumerics into the retained buffer (malloc folds
        // to a fresh CharArray; the vendor free/malloc pair is GC)
        s.p1 = 0
        val fresh = CharArray(str.length + 1)
        var q = 0
        for (c in str) {
            if (isalnumC(c)) {
                fresh[q] = tolowerC(c)
                q++
            }
        }
        s.string = fresh
        s.p2 = q
        s.p1 = 0

        if (q == 0) {
            // empty after the filter: vendor returns "" WITHOUT
            // setting flg, so every call re-copies - kept
            return ""
        }

        fresh[q] = NUL
        s.flg = 1 // do not re-enter this block until consumed
    }

    val buf = s.string ?: error("Shred: retained buffer missing")
    while (true) {
        // strncpy(word, p1, size) with its zero-fill tail, then
        // the NUL at word[size] (clamped if the tail runs past
        // the retained buffer)
        var i = 0
        while (i < size) {
            s.word[i] = if (s.p1 + i <= s.p2) buf.getOrElse(s.p1 + i) { NUL } else NUL
            i++
        }
        s.word[size] = NUL
        s.p1 = s.p1 + size

        if (s.p1 > s.p2) {
            // exhausted (strictly-greater vendor test: one last
            // partial fragment reaches here), reset the state
            s.flg = 0
            s.string = null
            return ""
        }

        return CstringOps.cstring(s.word)
    }
}

// per-file C-locale byte-domain helpers (H-012, StopInit precedent)
private fun isalnumC(c: Char): Boolean =
    c in 'a'..'z' || c in 'A'..'Z' || c in '0'..'9'

private fun tolowerC(c: Char): Char =
    if (c in 'A'..'Z') c + 32 else c
