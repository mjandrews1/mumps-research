package daf

import java.io.File
import java.io.FileNotFoundException

// Unit 146 (DAF B5 T3, complex): libmpscpp.cpp::SynInit (units.csv row
// 133: loc 58, cr 0.017, ptr 5, ctl 14, score 36.4, rank 219,
// complex): the synonym-table loader: read the synonym file one line
// at a time, lowercase, split on spaces, first token = the shared
// alias word, each further token = a table entry (word -> alias), the
// SYNMAX guard, then the ascending bubble sort on the word key and
// the entry count return.
// Consults: BASE-log row 60 hit-adapt (the C stdio file API
// fopen/fgets/fclose folds onto the java.io File reader seat, the
// Readf/abrtx precedent; DOCUMENTED DIVERGENCE on the two exit()
// calls: the vendor exit(EXIT_FAILURE) paths render as thrown
// exceptions - the missing-file guard throws FileNotFoundException,
// the malloc-NULL guard is unreachable on the JVM (OOME is the
// native analog, the row-46/85 divergence ledger)), row 25
// hit-reuse (the tolower walk is the ASCII-only toLowerAscii seat,
// NOT Kotlin toLowerCase - C-locale semantics per the ctype fold),
// rows 98/101 hit-reuse (the SynInit(char*)/SynInit(mstring)/
// SynInit(string) overload triple at libmpscpp.cpp:7810/7815/7819
// collapses onto the single String seat exactly as the row-98
// overload-collapse fold), row 6/22 hit-reuse (syn[SYNMAX] + synx
// are the file-scope synonym state: the DafSyn singleton seat is
// shared with the SYN() lookup seat (the synD stub of
// LibmpscppMisc9.kt; same-TU family)); SYNMAX 20000 pinned from
// include/mumpsc/libmpscpp.h.in:385. The bubble sort keeps strcmp
// lexicographic > on words (row 14-kin: the strcmp family renders as
// the String compareTo, no BASE row consulted - manual-decision,
// logged).
// Quirk ledger (kept): the line[strlen-1] NUL overwrite of the
// trailing newline (renders as the strip of the single trailing
// newline before the token scan); the "Too many synonyms - quitting"
// path RETURNS 1 with the table mid-filled (not an exception; the
// vendor returns, it does not exit); the vendor reuses one malloc'd
// word per line across several entries (renders as the shared alias
// String reference).
// SEAT RESOLUTION + TAG CORRECTION: this render releases the
// synInitD named-owner stub of LibmpscppMisc9.kt - the b4-T3 raise
// attributed the owner to selftext.cpp; grep-verified, the SynInit
// bodies live at libmpscpp.cpp:7810-7836 and ARE the roster unit of
// this tranche (row 133): stub DELETED, real body rendered here
// under the same name; the sibling SYN() stub comment (synD) is
// tag-corrected in the same push (SYN's body is grep-verified at
// libmpscpp.cpp:7788, also not selftext.cpp; SYN stays a permanent
// named-owner stub - not a corpus unit).
// Dafny: EXEMPT (file-I/O loader over a sort; the pure counting/sort
// loop is trivially covered by the bmg/exemption family; journal
// row).

const val SYNMAX_D = 20000 // libmpscpp.h.in SYNMAX

object DafSyn {
    // Syn syn[SYNMAX]; long synx; - the file-scope synonym table and
    // its fill cursor (row 6/22 singleton fold; the table is shared
    // with the SYN() lookup seat).
    val syn = arrayOfNulls<Pair<String, String>?>(SYNMAX_D) // (word, alias)
    var synx = 0L
}

fun synInitD(file: String): Int {

    // FILE * fp = fopen(file.c_str(), "r"); if (fp==NULL) { ... exit }
    val fp = File(file)
    if (!fp.exists()) {
        // "synonym file missing" + exit(EXIT_FAILURE): the throw is
        // the documented divergence seat of the exit call
        throw FileNotFoundException("synonym file missing")
    }

    val lines = fp.readLines() // fgets walk seat (row 60)

    for (rawLine in lines) {

        // for (i=0; line[i]!=0; i++) line[i]=tolower(line[i])
        var line = StringBuilder()
        for (c in rawLine) line.append(toLowerAscii(c))

        // line[i-1]='\0' - the trailing-newline overwrite quirk: the
        // fgets line carries it, the reader seat already strips it;
        // the fold is the same observable (no empty trailing token)
        val str = line.toString()

        // p1 = strtok(line, " ")
        val tokens = str.split(' ').filter { it.isNotEmpty() }
        // (strtok NULL + strlen==0 skip both fold onto the empty
        // token list - , continue either way)
        if (tokens.isEmpty()) continue

        // the malloc'd shared alias word of this line (strdup fold)
        val word = tokens[0]

        for (t in tokens.subList(1, tokens.size)) { // strtok(NULL," ")
            // malloc(NULL) guard: unreachable on the JVM (OOME
            // divergence ledger, rows 46/85)
            val type = t
            // syn[synx].word = type; syn[synx].alias = word; synx++
            DafSyn.syn[DafSyn.synx.toInt()] = Pair(type, word)
            DafSyn.synx++

            if (DafSyn.synx >= SYNMAX_D) {
                // "Too many synonyms - quitting " - the vendor RETURNS
                // 1 here (mid-filled table kept); quirk ledgged above
                return 1
            }
        }
    }

    // fclose(fp) folds onto the reader seat close; flg=1

    // the ascending bubble sort on the word key (strcmp > swap)
    var flg = true
    while (flg) {
        flg = false
        for (i in 0 until DafSyn.synx - 1) {
            val a = DafSyn.syn[i.toInt()]!!
            val b = DafSyn.syn[i.toInt() + 1]!!
            if (a.first.compareTo(b.first) > 0) {
                DafSyn.syn[i.toInt()] = b
                DafSyn.syn[i.toInt() + 1] = a
                flg = true
            }
        }
    }

    return DafSyn.synx.toInt() // return synx
}
