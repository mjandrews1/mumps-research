package mumps.mdh

import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import kotlin.system.exitProcess

/**
 * PRO-arm translation of libmpscpp.cpp::SynInit (B5 unit 126,
 * rank 133). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 7810-7817 (the char-star/mstring/string forwarders) and
 * 7819-7897 (the string overload that owns the body).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::SynInit, cpp, loc 58, batch 5 - exact
 * match to wp1/study-subset/units.csv row 133. The vendor also has
 * two SynInit fragments below the 15-LOC threshold (the 10513-10518
 * MSV wrapper, already rendered by unit 97 in LibmpscppMisc9.kt, and
 * the header decls at libmpscpp.h:394-396) - out of this unit's
 * boundaries, not re-rendered here.
 *
 * SynInit(file): loads a synonym file - one line per base word, the
 * first token is the alias word and every remaining token an alias
 * entry - then bubble-sorts the table by word so the (not-a-corpus-
 * unit) SYN bsearch at vendor 7788 can look aliases up. Returns the
 * number of table entries.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param : fopen(file,"r") and fclose live inside
 *    this unit - java.io.FileReader at the edge (fopen failure ->
 *    null carrier check); fgets -> BufferedReader.readLine (the
 *    readLine contract already excludes the newline, so the
 *    vendor's line[i-1]='\0' terminator strip is the built-in
 *    behavior here - documented, not re-coded).
 *  - h002 simple_output_macro: cout << ... << endl ->
 *    System.out.println.
 *  - h008 static_scratch   : the vendor file-scope globals Syn
 *    syn[SYNMAX] (7785) and long synx (7786) persist across calls -
 *    carried as top-level val syn / var synx in this file; SYN
 *    (7788, not a corpus unit, MdhStubs stub) reads them when its
 *    owner renders - this file is the carrier.
 *  - h012 unsigned_byte_domain: tolower over the C unsigned-char
 *    domain - private tolowerC (ScanAlnum unit 120 precedent; the
 *    A-Z fold matches the C locale).
 *  - h013 dead_store      : the inner "if (strlen(p1)==0) break;"
 *    (vendor 7859) is a dead condition - strtok never returns an
 *    empty token - kept as a comment, not translated.
 *  - h014 out_of_scope_call: the MdhStubs SynInit(String) stub
 *    raised by unit 97 is REMOVED - this unit owns it now (Scan
 *    unit 81 stub-removal precedent); the LibmpscppMisc9.kt:541
 *    MSV wrapper call site rebinds to this real body. SYN and the
 *    stop-family stubs stay (their owners: SYN not a corpus unit,
 *    StopInit batch-6).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level SYNMAX, SynEntry, syn, synx,
 *    SynInit(String) - no collisions (the removed stub had Unit
 *    return; this real body returns Int).
 *  - h017 naming          : SynInit/syn/synx/SYNMAX keep the vendor
 *    names.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The three C++ overloads (char* 7810, mstring 7815, string
 *    7819) collapse to the single Kotlin String parameter - the
 *    char-star/mstring forwarders only relay to the string body.
 *  - The vendor shares one malloc'd alias pointer across every
 *    entry of a line (7866-7867 store the same word pointer); the
 *    Kotlin String is immutable and shared by reference - the
 *    sharing semantics carry over naturally, documented here.
 *  - The malloc-NULL "out of memory" exits (7836-7840, 7863-7867
 *    region) have no Kotlin analog (garbage-collected heap, no
 *    malloc contract) - dropped with this comment, not translated.
 *  - The "Too many synonyms - quitting" path (7870) returns 1
 *    without fclose - the file object is simply dropped; kept
 *    verbatim (the C fd leak rides along as documented quirk).
 *  - The bubble sort (7879-7897) swaps word and alias together but
 *    compares only word; the swapped-in temp pair keeps both
 *    fields - rendered as a triple assignment over the entry
 *    pair.
 *  - The C return narrows the long synx to int implicitly
 *    (vendor 7897); rendered as an explicit toInt() with this
 *    comment - same value domain for any file under SYNMAX.
 */

/** vendor libmpscpp.h:385: #define SYNMAX 20000. */
const val SYNMAX = 20000

/** vendor libmpscpp.h:387-390: struct Syn { char *word; char *alias; }. */
class SynEntry {
    var word: String = ""
    var alias: String = ""
}

/** vendor 7785: Syn syn[SYNMAX]; (h008 carrier - SYN reads this). */
val syn = Array(SYNMAX) { SynEntry() }

/** vendor 7786: long synx; (h008 carrier - entry count, SYNMAX max). */
var synx: Long = 0

/** C tolower over the byte domain (C locale ASCII fold; h012). */
private fun tolowerC(c: Char): Char = if (c in 'A'..'Z') c + 32 else c

/**
 * vendor 7810-7817 + 7819-7897: int SynInit(string file) - the
 * char-star/mstring overloads relay here; loads the synonym file and
 * sorts the table. Returns the entry count (vendor int; long synx
 * narrowed, see header).
 */
fun SynInit(file: String): Int {
    val fp: BufferedReader? = try {
        BufferedReader(FileReader(file))
    } catch (e: FileNotFoundException) {
        null
    }

    if (fp == null) {
        System.out.println("synonym file missing")
        exitProcess(1) // exit(EXIT_FAILURE)
    }

    while (true) {
        val raw = fp.readLine() ?: break // fgets==NULL -> break

        // vendor 7849-7850: lowercase the whole line, byte domain
        val low = StringBuilder(raw.length)
        for (i in raw.indices) low.append(tolowerC(raw[i]))

        // strtok(line," "): runs of spaces collapse; empty tokens
        // never occur (h013 note below applies to the inner loop)
        val tokens = low.toString().split(' ').filter { it.isNotEmpty() }

        if (tokens.isEmpty()) continue // p1==NULL -> continue

        // vendor 7855-7858: word = strdup of the first token (the
        // shared alias pointer for this line - see header note)
        val word = tokens[0]

        for (k in 1 until tokens.size) {
            val type = tokens[k] // vendor: type = strdup(p1)

            syn[synx.toInt()].word = type
            syn[synx.toInt()].alias = word
            synx++

            if (synx >= SYNMAX) {
                System.out.println("Too many synonyms - quitting ")
                return 1 // vendor: no fclose on this path
            }

            // h013 dead_store: the vendor's "if (strlen(p1)==0)
            // break;" after this point is unreachable (strtok never
            // returns an empty token) - not translated.
        }
    }

    fp.close() // fclose(fp)

    var flg = 1 // vendor 7876

    while (flg != 0) {
        flg = 0

        for (i in 0 until (synx - 1).toInt()) {
            if (syn[i].word.compareTo(syn[i + 1].word) > 0) {
                // swap (word, alias) as a pair; compare word only
                val tmpWord = syn[i].word
                val tmpAlias = syn[i].alias
                syn[i].word = syn[i + 1].word
                syn[i].alias = syn[i + 1].alias
                syn[i + 1].word = tmpWord
                syn[i + 1].alias = tmpAlias
                flg = 1
            }
        }
    }

    return synx.toInt() // vendor: return synx (long -> int narrow)
}
