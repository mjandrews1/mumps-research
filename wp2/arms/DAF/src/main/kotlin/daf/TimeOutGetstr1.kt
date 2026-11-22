package daf

// Unit 170 (DAF B6 T2, core): interp-sysfunc.cpp::TimeOut_getstr1
// (33 LOC, score 14.1, rank 135): the timed read gate of the
// \$ TimeOut read service: the unit-5 guard with the hard stop, the
// select(2) wait over stdin with the parsed timeout, the raw-mode
// single-char read vs the line read, and the 1/0 verdict.
// Consults (2026-11-22T16:02:30Z, nominal): BASE-log row 92
// hit-reuse (the io!=5 guard prints and calls sigint(100) - the
// sigintDaf seat raised at T1 with GlobalNodeStub, code 100
// exactly), row 28 hit-reuse (tv_sec = atoi(timeout)), rows 60/59
// family MANUAL-DECISIONS (the select(2) call and the stty raw /
// stty -raw system() calls are POSIX platform operations with no
// Kotlin-library semantic: both fold onto named-owner platform
// stubs (selectStdinDaf below, sttyDaf below) whose owners are the
// POSIX platform seats - the Getenv/System.getenv divergence
// precedent; the stubs throw, so the render compiles and the
// platform behavior is the bench-verification target), row 33
// hit-adapt (the area carrier writes ride the CharArray with the
// at() NUL domain: area[0]=getchar() over System.in.read, the
// fgets line read over the reader seat with the trailing-newline
// kill of the vendor area[strlen-1]=0 fold).
// Vendor quirks kept: (1) the guard fires only when io!=5
// (printf then sigint(100): the vendor sigint at the T1 seat exits
// or throws per that seat); (2) the no-data path returns 0 with the
// area ALREADY cleared and the raw mode restored before the return;
// (3) fgets(area,STR_MAX,stdin) reads STR_MAX-1 bytes so a long
// line is CUT - the reader-seat fold reads the whole line (the
// 512-cut is the vendor buffer bound; the fold is the same
// observable for lines under the bound, ledgered); (4) the newline
// kill area[strlen-1]=0 assumes the line is nonempty (an empty-read
// UB is unreachable for fgets: it returns NULL at end of file, so
// the kill always addresses a real newline).
// Dafny: EXEMPT (platform-mediated timed read; journal row).

// int select(1, &rfds, NULL, NULL, &tv) over stdin - named-owner
// platform stub (the POSIX select(2) wait; owner: the platform
// seat; returns 1 when stdin is ready within the timeout, 0
// otherwise).
fun selectStdinDaf(timeoutSec: Int): Int =
    throw NotImplementedError(
        "select: out-of-scope owner (POSIX select(2) over stdin; platform seat; roster row interp-sysfunc.cpp::TimeOut_getstr1)")

// system("stty raw") / system("stty -raw") - named-owner platform
// stub (the terminal raw-mode toggles; owner: the platform seat).
fun sttyDaf(raw: Boolean): Unit =
    throw NotImplementedError(
        "stty: out-of-scope owner (POSIX stty raw-mode toggle; platform seat; roster row interp-sysfunc.cpp::TimeOut_getstr1)")

fun timeOutGetstr1D(io: Int, area: CharArray, timeout: CharArray,
                    dirflg: Boolean, stdin: java.io.InputStream): Int {

    // if (io != 5) { printf(...); sigint(100); }
    if (io != 5) {
        println("*** Timed read only permitted to unit 5")
        sigintDaf(100)
    }

    // tv.tv_sec = atoi(timeout); ... i = select(1,&rfds,NULL,NULL,&tv);
    val i = selectStdinDaf(atoi(DafCRuntime.cstring(timeout)))

    if (dirflg) sttyDaf(true) // if (dirflg) system("stty raw");

    if (i == 0) { // if (!i) {
        // strcpy(area, "");
        area[0] = NUL_D
        if (dirflg) sttyDaf(false) // if (dirflg) system("stty -raw");
        return 0
    }

    if (dirflg) {
        // i = getchar(); area[0]=i; area[1]='\0';
        // system("stty -raw");
        val c = stdin.read()
        area[0] = if (c == -1) NUL_D else c.toChar() // char read fold
        area[1] = NUL_D
        sttyDaf(false)
    } else {
        // fgets(area,STR_MAX,stdin); area[strlen(area)-1]=0;
        // (the reader-seat line read folds the newline kill; the
        // STR_MAX-1 cut is the vendor buffer bound, ledgered.)
        val reader = java.io.BufferedReader(java.io.InputStreamReader(stdin))
        val line = reader.readLine() ?: "" // fgets NULL at EOF folded
        var t = 0
        while (t < line.length && t < area.size - 1) {
            area[t] = line[t]
            t++
        }
        area[t] = NUL_D
        // area[strlen-1]=0: the reader seat already dropped the
        // newline; the observable is the unterminated line buffer.
    }

    return 1
}
