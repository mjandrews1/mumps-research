package both

// Unit 148 (BOTH B5 T3, complex): interp-bmgsubs.c::bmg_search (60
// LOC). Batch_plan ordinal 148: complex, LOC 60, score 47.0. The
// Boyer-Moore-Gosper assisted buffer search over the bmg TU's
// global pattern state, driving an action callback at each match.
//
// Vendor behavior: the shared TU globals (delta0/cmap/pattern/
// patlen/nsuccess and the chunking constants BUFSIZE 50000 /
// PATSIZE 9000 / LARGE 59000 / NL newline) seat as the carrier
// below (the file-scope bmdata struct, interp-bmgsubs.c:84-99 - no
// interp-bmgsubs.c residue unit exists on the roster, so this
// render owns the carrier per h004). The body: nsuccess zeroed; the
// buffer's last byte is forced to NL (restored after); the buffer
// walks in BUFSIZE chunks; the chunk tail retracts to the last full
// line (a line longer than BUFSIZE punts the chunk); the BMG jump
// loop advances k by delta0 over the unsigned byte at k while
// inside the chunk; the LARGE margin test breaks or backs k up;
// the match test walks cmap/pattern backwards; j >= 0 means a
// mismatch (k++ and continue), the submatch arm walks s back to the
// line start and delegates to gotamatch (NULL -> -1 return; the
// return value continues k); the restore puts the original last
// byte back; return is nsuccess.
//
// Quirks kept (ledgered):
//   - register char* arithmetic folds to Int offsets over the
//     CharArray buffer (the C pointer differences t-buffer and
//     k-strbegin become subtraction).
//   - cmap[*s--] indexes a char (SIGNED in C): a high byte would
//     index cmap negatively (vendor UB): the fold quantizes to the
//     unsigned byte domain (index = code and 0xFF) and journals the
//     quantization here.
//   - while ((k += delta0[*(unsigned char *) k]) < strend) ; reads
//     the delta0 table at the UNSIGNED byte: kept (code domain).
//     The jump can land exactly at strend and exit the loop with k
//     == strend (the LARGE test then breaks); it can also step
//     past a chunk tail only up to the allowed margin because the
//     last line was retracted (the vendor comment "we beat existing
//     microcode this way" kept below).
//   - the back-match loop decrements BOTH indices inside the
//     condition (post/read, pre/decrement order preserved at the
//     fold; the loop body is the empty C statement).
//   - int byte_offset = 0; is dead (h013).
//   - the action function pointer folds to the lambda seat
//     ((lineStart, matchOffset) -> Int); gotamatch returns the next
//     k (C char pointer) or NULL - the NULL folds to -1 at the
//     stub.
//
// BOTH consults (find_rule before render, events logged):
//   h004 globals_ext - reused: the bmdata file-scope state seats as
//     the BmgData carrier below (this render is the seat owner; no
//     residue unit exists for this TU on the roster).
//   h007 char_buffer - reused: the buffer is the caller's CharArray;
//     the walk is index arithmetic.
//   h012 unsigned_byte_domain - reused: delta0 indexes and the cmap
//     quantization stay in the Char(0..255) domain.
//   h013 dead_store - hit: byte_offset dropped.
//   h014 out_of_scope_call - reused: gotamatch rides its TODO stub
//     (owner interp-bmgsubs.c::gotamatch, roster 180, batch 1); the
//     action callback rides the lambda seat.
//   h016 comma_operator - consulted: none occur (register
//     declarations are declaration lists, not comma exprs).
//   h017 naming - hit: bmg_search/gotamatch/names kept.

internal const val BMG_BUFSIZE = 50000 // interp-bmgsubs.c:75
internal const val BMG_PATSIZE = 9000 // interp-bmgsubs.c:79
internal const val BMG_LARGE = BMG_BUFSIZE + BMG_PATSIZE // :80
internal const val BMG_NL = '\n' // interp-bmgsubs.c:81

// struct bmg_data file-scope seat (interp-bmgsubs.c:84-99).
internal object BmgData {
    val delta0 = IntArray(256) // int bmg_delta0[256] (ascii only)
    val cmap = CharArray(256) // char bmg_cmap[256]
    var patlen = 0 // int bmg_patlen
    var nsuccess = 0 // int bmg_nsuccess
    val pattern = CharArray(BMG_PATSIZE) // char bmg_pattern[PATSIZE]
}

// char *gotamatch(s, pos, action) - owner: interp-bmgsubs.c::
// gotamatch, roster 180 (batch 1, pending in this arm). The C
// signature is (char *s, int pos, int (*action)()); the NULL return
// folds to -1 at the seat. TODO-stub (h014).
internal fun gotamatch(
    s: Int,
    pos: Int,
    action: (String, Int) -> Int,
): Int {
    TODO("stub: owner interp-bmgsubs.c::gotamatch (roster 180, batch 1)")
}

internal fun bmg_search(
    buffer: CharArray,
    buflen: Int,
    action: (String, Int) -> Int,
): Int {
    var strbegin = 0
    var strend: Int
    var k: Int
    var s: Int
    val last_byte = buffer[buflen - 1] // char last_byte

    BmgData.nsuccess = 0

    // make sure that buffer ends with NL
    if (last_byte != BMG_NL) buffer[buflen - 1] = BMG_NL

    // process the buffer in chunks of BUFSIZE ("LARGE" hack).
    outer@ while (strbegin < buflen) {
        strend = minOf(strbegin + BMG_BUFSIZE, buflen) // stay inside

        // find end of last full line in this chunk of the buffer
        s = strbegin
        while (strend - 1 >= strbegin && buffer[strend - 1] != BMG_NL)
            strend--
        if (strend <= strbegin) { // line longer than BUFSIZE, punt
            strbegin += BMG_BUFSIZE
            continue@outer
        }

        k = strbegin + BmgData.patlen - 1

        while (true) {
            // (the 80% microcode quote kept: the delta jump loop)
            while (true) {
                k += BmgData.delta0[buffer[k].code and 0xFF]
                if (k >= strend) break
            }
            if (k < strbegin + BMG_LARGE) break
            k -= BMG_LARGE

            var j = BmgData.patlen - 1
            s = k - 1
            // back-match: cmap[*s--] vs pattern[--j], empty body
            while (true) {
                val c = BmgData.cmap[buffer[s].code and 0xFF]
                s-- // *s-- post-decrement
                j-- // --j pre-decrement
                if (c != BmgData.pattern[j]) break
            }
            if (j >= 0) {
                k++ // mismatch: continue at the next seat
            } else { // submatch
                val t = k
                s = k - BmgData.patlen + 1
                // walk back to the line start (empty do-body fold)
                while (buffer[s] != BMG_NL && s - 1 >= strbegin) {
                    s--
                }
                if (buffer[s] != BMG_NL) s = strbegin // off-front guard
                k = s + 1 // at line start

                val ret = gotamatch(
                    k, t, action,
                )
                if (ret == -1) return -1 // gotamatch NULL
                k = ret
                if (k >= strend) break
            }
        }
        strbegin += BMG_BUFSIZE
    }

    if (buffer[buflen - 1] != last_byte)
        buffer[buflen - 1] = last_byte
    return BmgData.nsuccess
}
