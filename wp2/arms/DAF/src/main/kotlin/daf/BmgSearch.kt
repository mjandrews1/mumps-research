package daf

// Unit 148 (DAF B5 T3, complex): interp-bmgsubs.c::bmg_search (units.csv
// row 130: loc 60, cr 0.189, ptr 10, ctl 17, score 47.0, rank 233,
// complex): the Boyer/Moore/Gosper-assisted egrep buffer walk: the
// NL-forced tail, the BUFSIZE chunk loop with the last-full-line
// trim, the delta0 fast-forward consume, the LARGE rewind, the tail-
// anchored candidate confirm scan over cmap/pattern, and the
// gotamatch report of the line-start submatch.
// Consults: BASE-log row 23 hit-reuse (the buffer/bd pass-through
// params keep the CharArray carrier roles; buflen explicit as in
// the b-tree key carriers), row 59 hit-adapt (the int (*action)()
// function-pointer parameter folds onto the Kotlin function type -
// the gotamatch seat decides the call shape; nullable returns carry
// the C NULL), rows 6/22 hit-adapt (the pattern tables are the
// file-scope struct bmg_data bmdata singleton: DafBmgState mirrors
// the vendor fields delta0/cmap/str/linetemp/pattern/patlen/nsuccess
// one-for-one; the table SETUP (bmg_setup) is a same-TU non-corpus
// function - named-owner stub bmgSetupD below, sibling of the
// gotamatch b1 stub), row 8 hit-adapt (register char *k/*s/*strend
// pointer arithmetic folds onto Int indices into the buffer
// CharArray; all derefs ride the at() NUL-guard seat of row 81:
// int-array reads past the end yield the NUL char - the vendor's
// unbounded derefs (delta0 probe at the old k before the widen
// check; pattern[-1] on full confirm) are THE SPEC and the render
// keeps their loop shapes with the at() guard standing in for the
// C read-past-end bytes; divergence ledger), row 111 hit-reuse (the
// Char(0..255) unsigned-char indexing family of the b-tree block
// walk: delta0/cmap index by the raw buffer char code), row 56
// hit-adapt (the forced-NL tail save/restore writes the buffer in
// place exactly as vendor - the caller owns the mutation).
// Quirk ledger (kept verbatim): the byte_offset local is declared
// and never used by the vendor body (kept as a comment-only fold -
// kotlinc/detekt unused-local discipline); the chunk-punt continue
// when no full line fits (a line longer than BUFSIZE); the
// delta-less shortcut: a confirm mismatch bumps k by one (NOT the
// delta table) while a full confirm jumps to the line start via the
// do-while backwalk; the submatch's gotamatch return advances k and
// the k>=strend exit; the vendor restores the forced NL only on the
// normal exit (the early -1 return of the NULL gotamatch relay does
// NOT restore: the caller's tail keeps the forced NL - kept
// verbatim, odd as it reads).
// Dafny: EXEMPT (pointer-mesh buffer walker over unproven table
// stubs; the B4 exemption family of the state-machine renders).

const val BUFSIZE_D = 50000 // interp-bmgsubs.c #ifndef BUFSIZE
const val PATSIZE_D = 9000  // #ifndef PATSIZE
const val LARGE_D = BUFSIZE_D + PATSIZE_D // #define LARGE

// struct bmg_data bmdata (file-scope singleton, rows 6/22): the
// pattern tables of the search; setup owner is the same-TU
// bmg_setup (not a corpus unit) - named-owner stub below.
object DafBmgState {
    val delta0 = IntArray(256)      // int bmg_delta0[256] (ascii only)
    val cmap = CharArray(256)       // char bmg_cmap[256] ((un)folded)
    val bmstr = CharArray(BUFSIZE_D + 2)   // char bmg_str[BUFSIZE+2]
    val linetemp = CharArray(BUFSIZE_D)   // char bmg_linetemp[BUFSIZE]
    val pattern = CharArray(PATSIZE_D)     // char bmg_pattern[PATSIZE]
    var patlen = 0                  // int bmg_patlen
    var nsuccess = 0                // int bmg_nsuccess
    var bmgFound = 0                // int bmg_found (same-TU global kin)
}

// void bmg_setup(char *, int) - same-TU table builder, not a corpus
// unit; the delta0/cmap/pattern fields bind when its caller's cell
// renders (named-owner stub, rows 37/105 discipline).
fun bmgSetupD(pat: String, foldCase: Int): Unit =
    throw NotImplementedError(
        "bmg_setup: out-of-scope owner (interp-bmgsubs.c bmg_setup; not a corpus unit)")

// char *gotamatch(char *at, int offset, int (*action)()) - roster
// unit interp-bmgsubs.c::gotamatch (b1 cell; outside the DAF batch
// assignment); nullable return carries the C NULL of the -1 relay.
fun gotamatchD(kOff: Int, offset: Int, action: (IntArray) -> Int): Int? =
    throw NotImplementedError(
        "gotamatch: out-of-scope owner (interp-bmgsubs.c::gotamatch, roster b1)")

fun bmgSearchD(buffer: CharArray, buflen: Int, action: (IntArray) -> Int): Int {

    var k: Int
    var strend: Int
    var s: Int
    var j: Int
    var t: Int
    // int byte_offset = 0; - declared and never used by the vendor
    // body; comment-only fold (unused-local ledger, above)
    val lastByte: Char = buffer[buflen - 1] // char last_byte

    DafBmgState.nsuccess = 0 // nsuccess = 0

    // make sure that buffer ends with NL
    if (lastByte != LINEFEED_D)
        buffer[buflen - 1] = LINEFEED_D

    var strbegin = 0 // char *strbegin = buffer
    while (strbegin < buflen) { // strbegin < &buffer[buflen]

        strend = strbegin + BUFSIZE_D // strend = strbegin + BUFSIZE
        if (strend > buflen) strend = buflen // stay inside buffer

        // find end of last full line in this chunk of the buffer
        s = strbegin
        while (at(buffer, strend - 1) != LINEFEED_D && strend > s)
            strend--
        if (strend <= strbegin) { // line longer than BUFSIZE, punt
            strbegin += BUFSIZE_D
            continue
        }

        k = strbegin + DafBmgState.patlen - 1 // k = strbegin + patlen - 1

        while (true) { // for (;;)
            // while ((k += delta0[*(unsigned char *) k]) < strend);
            while (true) {
                k += DafBmgState.delta0[at(buffer, k).code]
                if (k >= strend) break
            }
            if (k < strbegin + LARGE_D) break // if (k < strbegin+LARGE)
            k -= LARGE_D // k -= LARGE

            // j = patlen - 1; s = k - 1;
            // while (cmap[*s--] == pattern[--j]);
            j = DafBmgState.patlen - 1
            s = k - 1
            while (true) {
                val eq = DafBmgState.cmap[at(buffer, s).code] ==
                    at(DafBmgState.pattern, j)
                s--
                j--
                if (!eq) break
            }
            // delta-less shortcut for literati, but short shrift for
            // genetic engineers. (vendor comment kept verbatim)
            if (j >= 0)
                k++
            else { // submatch
                t = k
                s = k - DafBmgState.patlen + 1
                // do ; while (*s != NL && --s >= strbegin);
                while (true) {
                    if (at(buffer, s) == LINEFEED_D) break
                    s--
                    if (s < strbegin) break
                }
                k = s + 1 // at line start

                // if ((k = gotamatch(k, t - buffer, action)) == NULL)
                //     return -1;
                val g = gotamatchD(k, t /* - buffer: 0-based = t */,
                    action)
                if (g == null) return -1
                k = g
                if (k >= strend) break
            }
        }
        strbegin += BUFSIZE_D
    }

    if (buffer[buflen - 1] != lastByte)
        buffer[buflen - 1] = lastByte // restore the tail
    return DafBmgState.nsuccess // return (nsuccess)
}
