package doc

// Unit 150 (B5 tranche 3): mumps.cpp::main (batch 5, complex
// stratum; units.csv row 150: loc 125, score 60.7, rank 250).
// The runtime REPL entry: argv staging through the percent
// symbol family, the optional direct program launch (the ./ -
// prefix strip and the build-and-Interpret of the run command),
// the announce banner, and the readline loop with its halt/?/
// launch dispatches. PI DECISION precedent (unit-130 PRO-side
// analogue; DOC's own render): mumps.cpp::main sits entirely
// inside the unit-12 misc chunk's FILE span but IS its own
// roster row - rendered here as its own Kotlin file with the
// one-main-per-package entry name main150 (the unit-12
// MumpsMisc.kt render owns the leading residue only). All
// translations re-derived from the pristine vendor source this
// session (sha256 621deb...ffdbe verified).
//
// H-018 (the announce macros, resolved against the reference
// build config - mumps-sqlite3): SQLITE is LIVE (database =
// "; DBMS: SQLITE; Memory Resident: 0"); the NATIVE/NATIVE_
// CLIENT/READ_ONLY define chain is EXCLUDED (NATIVE undefined);
// BIGFLOAT is EXCLUDED (floatSize = "Float: double");
// HAVE_HARDWARE_MATH is LIVE per include/mumpsc/config.h
// (Fpbits = "; FP Bits: N/A"); HardwareMath/Ints/FltDigits/
// Cache/BlkSiz are unconditional defines (verbatim). The
// __TIME__/__DATE__ compile stamps have no JVM analog - the
// announce banner carries placeholder tokens (registered
// divergence, arm precedent for this entry). The NATIVE_SHARED
// btree(CLOSE) deadlock guard inside the readline loop is an
// EXCLUDED arm (comment ledger, not rendered code).
//
// H-013: the dead static struct C block (S/p1/p2, never read) is
// dropped with this note; the if(0) SIGALRM/SyncClock/set_alarm
// block is kept as the vendor comment ledger (the arm's
// set_alarm seat is the MumpsMisc render). H-003: the
// dollar-SymPut stateful macro (libmpscpp.h:98 define SymPut)
// resolves at this render to the landed unit-120 SymPut renders
// (the __symput kernel); the GlobalClose macro
// (libmpscpp.h:119) expands inline to Mglobal(CLOSE,"","",svPtr)
// KEEPING the macro expansion shape (H-003 one-line call sites:
// the two EOF arms spell the expansion). H-014 seats:
// Interpret(String, Msv) (the interp core entry, not a roster
// unit), Mglobal (byte-in passing CLOSE), Mltoa, DirectRead (the
// landed u24 delegate), cleanLocks (the landed u56 render),
// sigint edge none in this body. H-001 divergence: the GNU
// readline/read_history/add_history/write_history block has no
// JVM analog - the loop reads lines over the stdin Reader edge
// with the "> " prompt printed before each read (the stdin
// reader divergence is pre-registered in the unit-12 journal
// note for THIS unit); history persistence is a no-op ledger.
// H-009: sprintf percent-d and the argv staging render via
// String.format Locale.US. H-011: the announce label backward
// goto ("?" relaunch) renders as the flag re-entry loop.
// H-015: the quoted-escape build of the run command keeps the
// vendor's three-quote escape bytes; no dollar bytes in this
// body (the percent bytes are plain ASCII here). The
// strcasecmp halt set is case-insensitive exact-match. Naming
// per H-017 (main150 entry note above).

// defines.h:87 define CLOSE (the GlobalClose opcode; NOT the
// zfcn-support CLOSE 207 of the BifsMisc ledger).
private const val CLOSE_MGV = 4

fun main150(argv: Array<String>): Int {
    val svPtr = Globals.svPtr
    val tmp = CharArray(STR_MAX)
    val tmp0 = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)

    // if ( 0) { // no alarm if slice is zero
    //     signal(SIGALRM, SyncClock);
    //     set_alarm(1);
    //     }
    // - dead guard block kept as the vendor comment ledger
    // (H-013); SyncClock/set_alarm own the MumpsMisc renders.

    // H-018 define ledger (see header): database, floatSize,
    // HardwareMath, Ints, FltDigits, Cache, BlkSiz, Fpbits.
    val database = "; DBMS: SQLITE; Memory Resident: 0"
    val floatSize = "Float: double"
    val hardwareMath = "; Hardware math: yes"
    val ints = "; Int: int"
    val fltDigits = ";\nFloat digits: 8"
    val cache = "; Cache: 65537"
    val blkSiz = "; Block: 2048"
    val fpBits = "; FP Bits: N/A"

    val argc = argv.size

    if (argc > 1) { // program name
        CstringOps.strcpyS(svPtr.program, argv[1].take(512))
    } else {
        CstringOps.strcpyS(svPtr.program, argv[0].take(512))
    }

    if (argc >= 2) {
        SymPut("%", argv[1])
    } else {
        SymPut("%", "")
    }

    if (argc > 2) {
        val t = CharArray(STR_MAX)

        var i = 2
        while (i < argc) {
            CstringOps.strcpyS(t, String.format(
                java.util.Locale.US, "%%%d", i - 1))
            SymPut(CstringOps.cstring(t), argv[i])
            // printf("%d .... %s ..... %s\n",i,t,argv[i]); -
            // vendor comment fossil kept.
            i++
        }

        CstringOps.strcpyS(t, String.format(
            java.util.Locale.US, "%d", argc - 2))
        // \u0024SymPut((char *)"%c",t); - percent-c count seat.
        SymPut("%c", CstringOps.cstring(t))
        SymPut("%0", argv[1])
    }

    if (argc > 1 && argv[1].isNotEmpty()) {
        CstringOps.strcpyS(tmp, argv[1])

        if (CstringOps.strncmpAt(tmp, 0, "./", 2) == 0) {
            var i = 2
            while (tmp[i] != NUL) { // for (i=2; tmp[i]; i++)
                tmp[i - 2] = tmp[i]
                i++
            }
            tmp[i - 2] = NUL
        }

        // strcpy(tmp1,"s %=\\\"\\\"\\\"^"); - the three-quote
        // escape bytes kept verbatim (H-015).
        CstringOps.strcpyS(tmp1, "s %=\"\"\"^")
        CstringOps.strcatS(tmp1, CstringOps.cstring(tmp))
        CstringOps.strcatS(tmp1, "\"\"\" g @%")
        val retcode = Stubs.Interpret(CstringOps.cstring(tmp1), svPtr)
        // GlobalClose;
        Stubs.Mglobal(CLOSE_MGV, CharArray(1), CharArray(1), svPtr)
        return retcode
    }

    val stdinReader = java.io.BufferedReader(STDIN_READER)

    while (true) { // announce: relaunch loop ("?" case, H-011)
        Stubs.Interpret(
            "w !,\"Mumps Built: " + BUILD_TIME_PLACEHOLDER +
            " " + BUILD_DATE_PLACEHOLDER + "\n" +
            floatSize + database + hardwareMath + ints + fltDigits +
            cache + blkSiz + fpBits +
            "\",!,\"http://www.cs.uni.edu/~okane/\",!,\"Enter ^D or HALT to exit\",!",
            svPtr)

        // char *line; printf("\n"); read_history(...) - the
        // history ledger note of the header (no-op).
        System.out.print("\n")

        var relaunch = false

        while (true) {
            // #ifdef NATIVE_SHARED btree(CLOSE,tmp0,tmp1,svPtr);
            // - EXCLUDED arm (H-018 comment ledger).

            System.out.print("> ")
            val line = stdinReader.readLine() // readline("> ")

            if (line == null) { // ^d - end of file
                System.out.print("EOF - goodbye ...\n")
                Stubs.Mglobal(CLOSE_MGV, tmp0, tmp1, svPtr)
                // write_history("./mumps_history"); - no-op
                // ledger (history persistence has no JVM analog).
                Stubs.Mltoa(java.lang.ProcessHandle.current().pid().toInt(), tmp1)
                cleanLocks(CstringOps.cstring(tmp1))
                System.out.print("\n\n") // puts("\n")
                return 0
            }

            if (line.isEmpty()) {
                continue // free(line); continue;
            }

            // add_history(line); - no-op ledger.

            val hl = line.lowercase()
            if (hl == "h" || hl == "\\h" || hl == "\\q" ||
                    hl == "\\quit" || hl == "halt") {
                System.out.print("EOF - goodbye ...\n")
                Stubs.Mglobal(CLOSE_MGV, tmp0, tmp1, svPtr)
                // write_history("./mumps_history"); - no-op ledger.
                Stubs.Mltoa(java.lang.ProcessHandle.current().pid().toInt(), tmp1)
                cleanLocks(CstringOps.cstring(tmp1))
                System.out.print("\n\n") // puts("\n")
                return 0
            }

            if (hl == "?") {
                relaunch = true // goto announce;
                break
            }

            Stubs.Interpret(line, svPtr)
            // free(line);
            System.out.print("\n")
        }

        if (!relaunch) break
    }
    return 0
}

// Build-stamp placeholder tokens (the __TIME__/__DATE__ compile
// stamps have no JVM analog; registered divergence).
private const val BUILD_TIME_PLACEHOLDER = "<time>"
private const val BUILD_DATE_PLACEHOLDER = "<date>"
