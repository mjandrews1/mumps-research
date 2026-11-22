package daf

import java.io.File
import java.io.FileNotFoundException

// Unit 166 (DAF B6 T2, core): libmpscpp.cpp::StopInit (the string
// overload at line 7729; 24 LOC, score 12.2, rank 118): the
// stoplist loader: clear the file-scope stop set, open the file,
// read lines, lowercase-and-cut each line at the first space, strip
// the trailing newline, insert.
// Consults (2026-11-22T16:00:30Z, nominal): BASE-log row 60
// hit-adapt (the C stdio file API fopen/fgets/fclose folds onto the
// java.io File reader seat, the SynInit/Readf precedent;
// DOCUMENTED DIVERGENCE on exit(EXIT_FAILURE): the missing-file
// guard throws FileNotFoundException (the SynInit precedent
// divergence ledger)), row 25 hit-reuse (tolower ->
// toLowerAscii, the ASCII-only ctype fold), rows 6/22 hit-reuse
// (StopList stop at libmpscpp.cpp:7718 is the file-scope
// set<string> state: the DafStop singleton carrier below; the
// carrier is shared with the StopLookup service seat), row 98
// hit-reuse (the StopInit(char*)/StopInit(mstring)/StopInit(string)
// overload triple at lines 7720/7725/7729 collapses onto the single
// String seat - the row-98 overload-collapse fold; the two relay
// overloads call through to this body in the vendor via the
// mstring/char conversions).
// SEAT RESOLUTION + TAG CORRECTION: this render RELEASES the
// stopInitD named-owner stub of LibmpscppMisc9.kt - the b4-T3 raise
// attributed the owner to "libmpscpp.cpp/selftext.cpp StopInit; not
// a roster unit"; grep-verified, the units.csv batch-6 row 166 IS
// this string overload at libmpscpp.cpp:7729: stub DELETED, real
// body rendered under the same name. The sibling stopLookupD stub
// keeps a corrected owner note (StopLookup stays a permanent
// named-owner stub - not a corpus unit; its future body would read
// the DafStop carrier).
// Vendor quirk kept: the line[i-1] NUL overwrite after the token
// scan kills the fgets newline (the case-b fold); with the
// reader-seat line already newline-stripped the observable is
// identical: the token is the full line when it holds no space,
// and the lowercase prefix before the first space otherwise; a
// blank line inserts the empty string (the vendor inserts empty
// lines too - fgets returns the lone newline and the scan kills
// it).
// Dafny: EXEMPT (file-I/O loader over a set carrier; journal row).

object DafStop {
    // StopList stop; - libmpscpp.cpp:7718, the file-scope
    // set<string> carrier (row 6/22 singleton fold).
    val stop = HashSet<String>()
}

fun stopInitD(file: String): Unit {

    // stop.clear();
    DafStop.stop.clear()

    // FILE * fp = fopen(file.c_str(), "r");
    // if (fp==NULL) { cout << "Stoplist file not found." << endl;
    //                 exit(EXIT_FAILURE); }
    val fp = File(file)
    if (!fp.exists()) {
        throw FileNotFoundException("Stoplist file not found.")
    }

    val lines = fp.readLines() // fgets(line,512,fp) walk seat (row 60)

    for (rawLine in lines) {

        // for (i=0; line[i]!='\0'; i++) {
        //      if (line[i]==' ') { line[i++]=0; break; }
        //      line[i]=tolower(line[i]); }
        // line[i-1]='\0';
        // stop.insert(line);
        //
        // The scan lowercases up to the first space and terminates
        // the token there (the bytes after the space stay unread -
        // the C observable); the line[i-1] overwrite addresses the
        // newline (the reader-seat line already dropped it, so the
        // no-space fold tokenizes the whole line; observable
        // identical to the vendor walk).
        val cut = rawLine.indexOf(' ')
        val tokenLower = StringBuilder()
        val scanLen = if (cut >= 0) cut else rawLine.length
        for (k in 0 until scanLen) tokenLower.append(toLowerAscii(rawLine[k]))
        val token = tokenLower.toString()
        // (a blank line yields the empty token - the vendor inserts
        // it too; the quirk fold above.)

        // stop.insert(line);
        DafStop.stop.add(token)
    }

    // fclose(fp) folds onto the reader seat close.
}
