package doc

// Unit 146 (B5 tranche 3): libmpscpp.cpp::SynInit (batch 5, complex
// stratum; units.csv row 146: loc 58, score 36.4, rank 219). The
// synonym-table loader: reads the synonym file line by line, folds
// to lowercase, splits each line on space delimiters, loads the
// first token as the shared alias and every following token as a
// table row (VENDOR QUIRK kept verbatim: the row stores the
// FOLLOWING token in .word and the FIRST token in .alias - the
// field naming is the vendor's own), then bubble-sorts the table
// by .word strcmp descending-swap order until stable. SYNMAX 20000
// and the struct Syn type are file-scope entities of libmpscpp.cpp
// (libmpscpp.h:385/:387, syn[] at vendor :7785) - the H-004 owner
// object SynStatic renders here in this unit file (the only unit
// that touches the table today; the batch-9 wrapper renders bind
// this carrier when they land).
//
// RETIRE NOTE: this render RESOLVES the unit-120-raised
// Stubs.synInit seat - the seat flips to the delegate (H-014
// delegate precedent). Divergences ledgered per H-001: fopen
// renders as the java.io.FileReader edge (the reader over the
// char stream; the vendor FILE* bulk-read contract has no JVM
// analog); the two exit(EXIT_FAILURE) arms quantize to
// IllegalStateException carriers (SysDump u142 precedent; the C
// abrupt-exit divergence is registered); cout maps to System.out
// per the arm H-001 convention. malloc folds to the String copy
// (H-008: the JVM allocation domain; the malloc-NULL arms are
// unreachable in the JVM and ledgered - the "out of memory"
// banner text survives in the comment). fgets renders on the
// buffered read (line[i-1]=0 strips the newline exactly where the
// vendor strips it: a final line without a trailing newline loses
// its last byte - the vendor quirk kept byte-faithful). strtok
// renders as the explicit space-delimiter scan walk (H-007 scratch
// role: the CharArray line carrier; the null-token continuation
// of the C strtok is carried by the walk state). The
// strlen(p1)==0 inner-loop break is UNREACHABLE after a real
// strtok token (tokens are non-empty) - the vendor quirk is kept
// as written. Naming per H-017 (snake synx/entry kept: array
// carriers).

// libmpscpp.h:385 define SYNMAX
private const val SYNMAX = 20000

class Syn {
    // struct Syn (libmpscpp.h:387): the char* members fold to
    // String copies (H-008 allocation domain).
    var word: String = ""
    var alias: String = ""
}

// libmpscpp.cpp:7785 Syn syn[SYNMAX] + the loader's synx counter:
// the one-owner object of this file's table (H-004).
object SynStatic {
    val syn = Array(SYNMAX) { Syn() }
    var synx: Int = 0
}

fun synInit(file: String): Int {
    var flg: Int
    val reader: java.io.BufferedReader

    reader = try {
        java.io.BufferedReader(java.io.FileReader(file))
    } catch (e: java.io.FileNotFoundException) {
        System.out.print("synonym file missing\n")
        throw IllegalStateException("synonym file missing")
    }

    while (true) {
        val l = reader.readLine()
        if (l == null) break
        // fgets(line,12000,fp): the read line without its newline;
        // the line[i-1]=0 strip below re-applies the carrier's
        // own boundary (see header - the newline-less eof line
        // loses its last byte, vendor-faithful).
        val line = CharArray(12000)
        CstringOps.strcpyS(line, l)
        var i = 0
        while (line[i] != NUL) { // for (i=0; line[i]!=0; i++) tolower
            line[i] = Character.toLowerCase(line[i])
            i++
        }

        // line[i-1]=0 (strip the newline at the length position).
        if (i > 0) line[i - 1] = NUL

        // p1=strtok(line," ") - the explicit delimiter walk with a
        // per-line holder ( strtok continuation state; no second
        // token falls through to the next line read ).
        val tokPos = IntArray(1)
        val first = strtokSpaceFirst(line, tokPos)
        if (first == null) continue

        val word = first // malloc folds to the String copy (H-008)

        while (true) {
            val tok = strtokSpaceNext(line, tokPos)
            if (tok == null) break
            if (tok.length == 0) break // unreachable vendor quirk kept

            val type = tok // malloc fold (H-008); the "out of
            // memory" banner dies with the malloc domain (ledger).

            SynStatic.syn[SynStatic.synx].word = type
            SynStatic.syn[SynStatic.synx].alias = word
            SynStatic.synx++

            if (SynStatic.synx >= SYNMAX) {
                System.out.print("Too many synonyms - quitting \n")
                return 1
            }
        }
    }

    reader.close()

    flg = 1

    while (flg == 1) {
        flg = 0

        var ii = 0
        while (ii < SynStatic.synx - 1) {
            if (SynStatic.syn[ii].word.compareTo(SynStatic.syn[ii + 1].word) > 0) {
                val w = SynStatic.syn[ii].word
                val a = SynStatic.syn[ii].alias
                SynStatic.syn[ii].word = SynStatic.syn[ii + 1].word
                SynStatic.syn[ii].alias = SynStatic.syn[ii + 1].alias
                SynStatic.syn[ii + 1].word = w
                SynStatic.syn[ii + 1].alias = a
                flg = 1
            }
            ii++
        }
    }

    return SynStatic.synx
}

// Explicit strtok(" ") walk over the line carrier: the first
// token pass (fresh scan) then the continuation passes over the
// remaining region (the holder carries the strtok walk position;
// no shared mutable state). Delimiter: the single space byte;
// consecutive delimiters yield no token (strtok semantics - the
// walk skips the run first).
private fun strtokSpaceFirst(line: CharArray, holder: IntArray): String? {
    holder[0] = 0
    return strtokSpaceNext(line, holder)
}

private fun strtokSpaceNext(line: CharArray, holder: IntArray): String? {
    var i = holder[0]
    while (i < line.size && line[i] == ' ') i++
    if (i >= line.size || line[i] == NUL) {
        holder[0] = i
        return null
    }
    val sb = StringBuilder()
    while (i < line.size && line[i] != NUL && line[i] != ' ') {
        sb.append(line[i])
        i++
    }
    holder[0] = i
    return sb.toString()
}

