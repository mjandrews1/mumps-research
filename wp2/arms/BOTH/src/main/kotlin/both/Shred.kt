package both

// Unit 182 (BOTH B6 T3, complex): libmpscpp.cpp::Shred
// (40 LOC). Batch_plan ordinal 182: complex, rank 221.
// The stateful fragment iterator: repeated Shred(str, size)
// calls hand out size-byte fragments of the alnum-filtered
// lowercase copy of the FIRST call's string, then the ""
// ender (the token-generator protocol used by the SQL-side
// callers).
//
// Vendor behavior kept:
//   - size >= 128: the "Shred fragment size too large"
//     diagnostic + sigint(100) stub seat, with the C
//     FALL-THROUGH kept (sigint does not stop the walk in this
//     arm's seat).
//   - flg == 0 first-call block: malloc/free fold to GC (a
//     fresh CharArray seat; the free-before-malloc of the prior
//     string is the null reassignment), the copy loop keeps
//     ONLY alphanumerics lowercased (C isalnum/tolower byte
//     domain); an empty result returns (char *) "" WITHOUT
//     setting flg (every subsequent call re-copies - vendor
//     kept); the NUL terminate and the p1=0 reset.
//   - the while(1) fragment loop (always returns per
//     iteration - the vendor shape kept as the while(true)):
//     strncpy folds to the NUL-padding copy (a source shorter
//     than size pads zeros, exactly as strncpy), word[size]
//     NUL seat, p1 += size, the p1 > p2 (not >=) reset test -
//     the last partial fragment is handed out NUL-padded and
//     the NEXT call resets (flg=0, string=null) and returns
//     the ender.
//
// Fossils kept (ledgered): the dead static int i (dropped with
// this note), the "Out of memory in Token()" copy-paste
// diagnostic (Token, not Shred - the malloc-failure arm folds
// to the OOM-free GC seat, the message seat kept on the copy
// path comment).
//
// BOTH consults (find_rule before render, events logged):
//   h008 static_scratch - reused: the file-scope statics
//     p1/p2/flg/string/word PERSIST ACROSS CALLS as top-level
//     private vars (the iterator protocol depends on it).
//   h012 unsigned_byte_domain - reused: per-file isalnumC/
//     tolowerC C-locale byte-domain helpers (the ScanAlnum
//     precedent: ASCII only, high bytes not alnum, tolower
//     ASCII letters only).
//   h014 out_of_scope_call - reused: sigint stub seat
//     (already in BothCRuntime; named-owner stub precedent).
//   h015 grammar_collision - hit: this file.
//   h017 naming - hit: Shred keeps the vendor name (a type
//     name Shred does not exist in this arm; no collision).

private var shredFlg = 0
private var shredP1 = 0
private var shredP2 = 0
private var shredString: CharArray? = null
private val shredWord = CharArray(128)

internal fun Shred(str: String, size: Int): String {
    if (size >= 128) {
        println("Shred fragment size too large")
        sigint(100)
        // C fall-through kept (the sigint seat does not stop)
    }

    if (shredFlg == 0) {
        val src = str // p1 = str (walk start)
        shredString = null // free(string) seat
        val buf = CharArray(src.length + 1) // malloc strlen+1
        var p2 = 0
        var p1 = 0
        while (p1 < src.length) { // copy to local string
            val c = src[p1]
            if (isalnumC(c)) {
                buf[p2] = tolowerC(c)
                p2++
            }
            p1++
        }
        if (p2 == 0) {
            return ""
        }
        buf[p2] = '\u0000'
        shredFlg = 1 // don't reenter until consumed
        shredString = buf
        shredP1 = 0
        shredP2 = p2
        // note: the vendor malloc-failure arm prints
        // "Out of memory in Token()" (sic) and sigint(100);
        // the GC seat cannot fail, the arm folds away.
    }

    while (true) {
        val string = shredString!!
        var i = 0
        while (i < size) { // strncpy fold (NUL padding)
            shredWord[i] =
                if (shredP1 + i < string.size) string[shredP1 + i] else '\u0000'
            i++
        }
        shredWord[size] = '\u0000'
        shredP1 = shredP1 + size

        if (shredP1 > shredP2) {
            shredFlg = 0
            shredString = null
            return ""
        }

        var n = 0
        while (n < shredWord.size && shredWord[n] != '\u0000') n++
        return String(shredWord, 0, n)
    }
}

// C isalnum seat (C-locale byte domain; ASCII only).
private fun isalnumC(c: Char): Boolean =
    (c.code >= '0'.code && c.code <= '9'.code) ||
        (c.code >= 'A'.code && c.code <= 'Z'.code) ||
        (c.code >= 'a'.code && c.code <= 'z'.code)

// C tolower seat (ASCII letters only).
private fun tolowerC(c: Char): Char =
    if (c.code >= 'A'.code && c.code <= 'Z'.code) {
        (c.code + 32).toChar()
    } else {
        c
    }
