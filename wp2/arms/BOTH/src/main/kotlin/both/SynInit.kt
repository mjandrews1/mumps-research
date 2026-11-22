package both

// Unit 146 (BOTH B5 T3, complex): libmpscpp.cpp::SynInit (58 LOC).
// Batch_plan ordinal 146: complex, LOC 58, score 36.4. The synonym
// file loader: read a whitespace-separated synonym file, store the
// (word, alias) pairs into the global Syn table and bubble-sort the
// table by word for the later bsearch lookups.
//
// Vendor behavior: fopen or fail (cout line + exit(EXIT_FAILURE));
// fgets each 12000-char line (newline kept, then stripped by the
// line[i-1]=0 write - see quirks); tolower over the whole line;
// strtok on single-space delimiters gives the alias word first, then
// every subsequent token is stored as a word bound to that alias
// (syn[synx].word = the token, syn[synx].alias = the first word);
// the SYNMAX (20000, libmpscpp.h:385) table cap prints the overflow
// line and RETURNS 1 (the vendor comment "quitting" is optimistic:
// the return path exits normally - kept); the loaded table then
// bubble-sorts by strcmp and the return is synx.
//
// Quirks kept (ledgered):
//   - line[i-1]='\0' after the tolower pass writes the byte before
//     the token content whenever the fgets carry is not
//     newline-terminated (EOF-truncated final line would lose its
//     last DATA character in C): the fold keeps the fgets carry
//     (newline included) and performs the same unconditional strip,
//     so the behavior is byte-faithful on newline-terminated lines
//     and the EOF-truncation UB quantizes to the same strip.
//   - strtok(line," ") treats runs of spaces as one delimiter and
//     skips leading/del ones: folded with the +space split and the
//     empty-token filter.
//   - the malloc/strcpy pairs are per-line token allocations: the
//     fold stores fresh Strings (GC); the malloc-failure exits are
//     unreachable in the fold and dropped with the h013 note.
//   - the bubble sort is the O(n^2) exchange with the flg rewalk:
//     kept structurally (the vendor compares adjacent words and
//     swaps the word/alias pairs wholesale).
//   - syn/synx are libmpscpp.cpp TU globals (the vendor file scope
//     at libmpscpp.cpp:7785-7786): seated as the carrier object
//     below (h004; this unit is the table's only writer).
//
// BOTH consults (find_rule before render, events logged):
//   h001 io_stream_param - reused: fopen(file,"r") is the Reader
//     edge (java.io.FileReader; the NULL-open cout+exit arm stays
//     exact, exit(EXIT_FAILURE) folds to exitProcess at this program
//     edge).
//   h004 globals_ext - reused: struct Syn (libmpscpp.h:387-390) and
//     the syn[SYNMAX]/synx TU globals seat as the carrier below,
//     each with its source pin.
//   h007 char_buffer - reused: char line[12000] is the per-line C
//     carry; the fgets loop folds to the 12000-cap char Reader walk
//     below (newline kept, exact strip).
//   h008 static_scratch - consulted, ADAPTED (hit): the rule's
//     trigger is shared static scratch, but these seats are per-line
//     mallocs; the fold keeps the fresh-value-per-token contract.
//   h013 dead_store - hit: the malloc-failure exits dropped.
//   h017 naming - hit: SynInit/SYNMAX/syn/synx/cmp-shaped vendor
//     names kept (word/type locals per vendor).

internal class Syn internal constructor(
    var word: String,
    var alias: String,
)

// libmpscpp.cpp:7785-7786 file-scope seats (h004 carrier).
internal object SynTable {
    val syn = arrayOfNulls<Syn>(SYNMAX) // syn[SYNMAX]
    var synx: Long = 0 // long synx
}

internal fun SynInit(file: String): Int {
    val inp = try {
        java.io.FileReader(file)
    } catch (e: java.io.IOException) {
        println("synonym file missing") // cout << ... << endl
        kotlin.system.exitProcess(1) // exit(EXIT_FAILURE)
    }

    val line = StringBuilder(12000) // char line[12000]
    while (true) {
        // fgets(line,12000,fp): carry up to 12000 bytes incl newline.
        line.setLength(0)
        var seen = false
        while (line.length < 12000) {
            val ch = try {
                inp.read()
            } catch (e: java.io.IOException) {
                -1 // read error quantizes to EOF (h001 edge)
            }
            if (ch == -1) break
            line.append(ch.toChar())
            seen = true
            if (ch == '\n'.code) break
        }
        if (!seen) break // fgets returns NULL at EOF

        val low = line.toString().lowercase() // tolower pass
        // line[i-1] = 0 unconditional strip (quirk above):
        var cut = low.length - 1
        if (cut < 0) cut = 0 // empty line guard (C would UB)
        val body = if (low.isEmpty()) "" else low.substring(0, cut)

        val toks = body.trim(' ').split(' ')
            .filter { it.isNotEmpty() } // strtok run-collapse
        val it2 = toks.iterator()

        if (!it2.hasNext()) continue // p1 == NULL: continue
        val word = it2.next().toString()

        while (true) {
            if (!it2.hasNext()) break // p1 == NULL: break
            val type = it2.next().toString() // strlen(p1)==0 filter:
            //  the empty-token arm cannot occur after the filter
            //  (the vendor checks strlen==0 past a delimiter run).
            SynTable.syn[SynTable.synx.toInt()] = Syn(type, word)
            SynTable.synx++

            if (SynTable.synx >= SYNMAX) {
                println("Too many synonyms - quitting ")
                return 1 // the vendor return path (kept)
            }
        }
    }

    inp.close() // fclose(fp)

    var flg = 1
    while (flg == 1) {
        flg = 0
        for (i in 0 until (SynTable.synx - 1).toInt()) {
            val a = SynTable.syn[i]!!
            val b = SynTable.syn[i + 1]!!
            if (a.word > b.word) { // strcmp(syn[i].word, syn[i+1].word) > 0
                // word/type swap through the locals (vendor shape)
                SynTable.syn[i] = b
                SynTable.syn[i + 1] = a
                flg = 1
            }
        }
    }

    return SynTable.synx.toInt()
}
