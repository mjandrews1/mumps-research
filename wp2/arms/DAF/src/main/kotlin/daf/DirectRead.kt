package daf

// R1 stub-completion (2026-11-27, C-020 erratum session, tranche 3):
// interp-sysfunc.cpp::DirectRead (31 LOC, roster row 9, complex):
// the single-character READ arm. The io!=5 branch fstats the
// in_file handle (the bad-file test) and fgetcs one character;
// the io==5 console branch distinguishes terminal / FIFO / regular
// file via st_mode and reads a raw getchar with the stty
// echo/raw toggles.
// H-001 OS quantizations (the unit-170/TimeOut_getstr1 area
// precedent): fstat(fileno(...)) has no JVM carrier - the bad-file
// arm folds onto the reader-null test of the DafFileHandles.inR
// seat (a handle without a Reader is the unopened-file
// observable); stty has the sttyDaf platform seat (raw-mode
// toggle; the vendor's "-echo raw" variant quantizes onto it -
// echo suppression is not observable through the JVM reader,
// disclosed); the S_ISCHR/S_ISFIFO/S_ISREG trichotomy of the
// console branch is not observable through a java.io stream -
// the terminal arm (S_ISCHR) is the folded behavior for the
// console, and the FIFO arm (return 0) is carried by the
// read() == -1 EOF test, disclosed.
// Consults (2026-11-27T00:00:00Z, nominal): BASE-log row 2 family
// hit-reuse (System.in the console read carrier, the
// TimeOut_getstr1 precedent); rows 81/56 hit-adapt (the unsigned
// char *to out-param keeps the CharArray(1) carrier of the call
// sites in Interpret.kt).
// Vendor quirks kept: (1) the branch test reads the GLOBAL
// svPtr->io while the read uses the in_ argument (the call sites
// pass svPtr.io so the two are the same value - kept verbatim);
// (2) the fstat(...) == 1 bad-file test can never fire on POSIX
// (fstat returns 0 or -1) - the null-reader fold models the
// intended unopened-file arm; (3) the console arm stores k
// WITHOUT an EOF test (a getchar() at EOF stores (char)-1 and
// returns 1) - kept verbatim: to[0] receives '\uFFFF' at stream
// end and the return is 1, disclosed; (4) the regular-file arm
// of the console branch returns 0 WITHOUT clearing *to at EOF -
// the fold shares the EOF test of the non-console arm (to[0]=0),
// disclosed as the io==5 quantization note.
// Dafny: EXEMPT (IO-bound; journal row).

fun directReadDaf(in_: Int, to: CharArray): Int {

    val svPtr = DafInterpState.svPtr // the vendor global svPtr

    if (svPtr.io != 5) {

        // fstat(fileno(svPtr->in_file[in]), &buf) == 1 - bad file
        val reader = DafFileHandles.inR[in_] ?: run {
            to[0] = NUL_D
            return 0
        }

        val k = reader.read() // k = fgetc(svPtr->in_file[in])
        if (k == -1) { // if (k == EOF)
            to[0] = NUL_D
            return 0
        }
        to[0] = k.toChar()
        return 1
    }

    // io == 5: the console arm. fstat/S_ISCHR: stdin is a
    // terminal (the folded console behavior)
    sttyDaf(true) // system("stty -echo raw")
    val k = System.`in`.read() // k = getchar()
    sttyDaf(false) // system("stty echo -raw")
    to[0] = k.toChar() // *to = k (no EOF test on this arm)
    return 1
}
