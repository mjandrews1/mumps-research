package daf

// R1 stub-completion (2026-11-27, C-020 erratum session, tranche 3):
// libmpscpp.cpp::Input (40 LOC, roster row 10, complex): the
// extrinsic \u0024&Input word-reader. CLAIM CORRECTION: the raised
// wrapper seat cited "interp-io.cpp::Input" - no interp-io.cpp
// exists in the vendor tree and the roster row owns
// libmpscpp.cpp::Input (the char* twin at :9984); the real body
// below renders the roster unit (ledger claim fix, disclosed).
// The seat kills any existing array (symflg 2, the sym_ relay),
// rereads fgets lines while the line is bare, then word-splits:
// each %-token is filtered (hyphen kept, other punctuation
// dropped), stored under name + strcatx indices (the x01
// separators of the strcatxD seat), and counted; the count-as-
// string return ends the read (tpx 0). A NULL fgets returns "0".
// The SV-bound wrapper (svPtrX, ep, name) signature stays: the
// ep argument is dead in the vendor body (kept), and the
// dispatcher call site in InterpService.kt resolves unchanged.
// Consults (2026-11-27T00:00:00Z, nominal): BASE-log row 2 family
// hit-reuse (fgets rides the reader-seat line read: the
// DafFileHandles.inR[io] Reader, the System.in console fallback
// of the unit-170 precedent; the fgets NUL-return at EOF folds to
// the null readLine); row 56 family hit-adapt (the static
// tmp[1024]/scount[32] carriers fold to fresh per-invocation
// locals - the caller siStrmove copies the return immediately,
// the persistence of the vendor statics is unobservable,
// disclosed); rows 111/68 hit-reuse (symRetDaf the sym_ seat,
// strcatxD the T2 seat); row 30 hit-adapt (the char name[256]
// build renders as the CharArray carrier with strcatxD appends).
// H-001 note: the console arm reads System.in (io==5 with no
// inR[5] Reader); the fgets-newline strip of readLine matches
// the vendor's later space/tab walk (the trailing newline is
// never a token byte), disclosed.
// Vendor quirks kept: (1) a bare line (fgets returning only the
// newline) triggers the reread; (2) the punct filter keeps '-'
// and drops every other ispunct byte; (3) a filtered-empty token
// skips the store AND the count increment (the noword fall
// through) but still advances the offset cursor; (4) svPtr->tpx
// is set 1 at the first token and 0 on the return paths; (5) the
// symflg 2 kill precedes the first fgets.
// Dafny: EXEMPT (IO-bound; journal row).

// ispunct ASCII domain (the H-017 private-copy precedent of
// LeaningToothpickize.kt)
private fun isPunctInputD(c: Char): Boolean =
    c in '!'..'/' || c in ':'..'@' || c.code in 91..96 || c in '{'..'~'

fun inputSvDaf(svPtrX: DafMsv, ep: CharSequence, name: CharArray): String {

    val name1 = DafCRuntime.cstring(name) // char *name1
    // (ep is dead in the vendor body; the parameter stays)

    val tmp1 = StringBuilder() // char tmp1[1024]
    var count = 0

    svPtrX.symflg = 2 // svPtr->symflg = 2; /* kill existing array */
    symRetDaf(2, name1.toCharArray() + NUL_D, 0,
        CharArray(1), 0, svPtrX) // sym_ (2, name1, "", svPtr)

    // fgets(line, STR_MAX, svPtr->in_file[svPtr->io]): the reader
    // seat (the console fallback at io==5). ONE buffered wrapper
    // for the whole call: a per-line wrapper would swallow the
    // stream into its buffer and EOF the reread loop (smoke-found
    // fold, disclosed)
    val reader: java.io.Reader = DafFileHandles.inR[svPtrX.io]
        ?: java.io.InputStreamReader(System.`in`)
    val lineReader = java.io.BufferedReader(reader)

    var line: String? // char line[2*STR_MAX]

    while (true) { // reread:
        line = lineReader.readLine()
        if (line == null) return "0" // if (fgets(...)==NULL) return "0"
        if (line.isNotEmpty()) break // if (line[0]=='\n') goto reread
    }

    var off = 0
    while (true) { // while(1)

        // if (sscanf(&line[off],"%1023s",tmp1)==0) - no more words
        while (off < line.length && (line[off] == ' ' ||
                    line[off] == '\t')) off++
        if (off >= line.length) {
            svPtrX.tpx = 0 // svptr->tpx=0
            return count.toString() // sprintf(tmp,"%d",count)
        }
        tmp1.setLength(0)
        while (off < line.length && line[off] != ' ' &&
                line[off] != '\t') {
            tmp1.append(line[off])
            off++
        }
        if (tmp1.length > 1023) tmp1.setLength(1023) // %1023s bound

        svPtrX.tpx = 1 // svptr->tpx=1

        // for (j=0,i=0; tmp1[i]!=0 && i<1023; i++)
        //      if (tmp1[i]=='-' || !ispunct(tmp1[i])) tmp[j++]=tmp1[i];
        val tmpB = StringBuilder() // char tmp[1024]
        for (c in tmp1) {
            if (c == '-' || !isPunctInputD(c)) tmpB.append(c)
        }
        val tmp = tmpB.toString()
        // tmp[1023]=0 bound: StringBuilder is unbounded; the
        // 1024-char truncation of the carrier is not observable
        // before the sym_ store, disclosed (H-012 note)

        if (tmp.isNotEmpty()) { // if (tmp[0]==0) goto noword;

            val scount = count.toString() // sprintf(scount,"%d",count)
            count++

            // strcpy(name,name1); strcatx(name,"");
            // strcatx(name,scount); // adds x01s
            val nameArr = CharArray(256)
            var q = 0
            while (q < name1.length) { nameArr[q] = name1[q]; q++ }
            nameArr[q] = NUL_D
            strcatxD(nameArr, CharArray(1)) // strcatx(name,"")
            strcatxD(nameArr, scount.toCharArray() + NUL_D)

            svPtrX.symflg = 0 // svPtr->symflg = 0; /* store */
            symRetDaf(0, nameArr, 0, tmp.toCharArray() + NUL_D, 0,
                svPtrX) // sym_ (0, name, tmp, svPtr)
        }
        // noword:

        // while(line[off]!=0 && line[off]!=' ' && line[off]!='\t') off++;
        // (the token scan above already advanced the cursor)

        if (off >= line.length) { // if (line[off]==0)
            svPtrX.tpx = 0
            return count.toString()
        }

        off++
    }
}
