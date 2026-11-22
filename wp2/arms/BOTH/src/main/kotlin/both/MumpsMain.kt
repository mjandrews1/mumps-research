package both

// Unit 150 (BOTH B5 T3, complex): mumps.cpp::main (125 LOC).
// Batch_plan ordinal 150: complex, LOC 125, score 60.7. The
// interactive Mumps REPL main: argument plumbing into the percent
// variables, the one-shot program dispatch, the announce banner
// and the readline-driven command loop.
//
// Vendor behavior: the static struct C list (S/p1/p2) and the
// if(0) alarm block (signal SIGALRM + set_alarm(1)) are dead
// (dropped, h013); the database banner macros resolve against the
// reference build config per h018 (SQLITE defined at
// interp-globalb.cpp:45; NATIVE/NATIVE_SHARED/READ_ONLY/CLIENT
// undefined; BIGFLOAT and MULTI_PRECISION undefined;
// HAVE_HARDWARE_MATH defined): database is "; DBMS: SQLITE;
// Memory Resident: 0", floatSize "Float: double", Fpbits
// "; FP Bits: N/A" (the excluded arms stay in the comments
// below). argv plumbing: svPtr->program receives argv[1] when
// present else argv[0] (strncpy 512, the stateVector.h:100
// carrier seat); the percent variables seat through the vendor
// dollar-SymPut macro (libmpscpp.h:98: it is __symput - the TODO
// stub below); argc<=1 seats percent EMPTY; argc>=2 additionally
// seats the numbered percent variables percent-1 .. percent-(n)
// (the sprintf-per shapes exact), the percent-c variable with the
// argument-count string and percent-0 with the program name. With
// a nonempty argv[1]: a leading .-slash strip runs, the dispatch
// line is built (the vendor doubles the percent and quote tokens
// in the C literals - kept byte-faithful below), Interpret runs
// it (the roster owner below), GlobalClose folds to the
// Mglobal(CLOSE,"","",svPtr) macro seat (CLOSE 4, defines.h:87)
// and the retcode returns. The announce banner carries the
// __TIME__ __DATE__ build stamps (the preprocessor seats stay
// token-shaped below) plus floatSize, database, HardwareMath,
// Ints, FltDigits, Cache, BlkSiz, Fpbits. The loop: readline
// (the GNU readline/history program edge folds to the stdin
// Reader seat below; the history seats are quantized no-ops -
// journaled); NULL at EOF prints the goodbye, closes globals,
// Mltoa(getpid()) + cleanLocks (the roster-292 stub / the
// libmpscpp.h:215 helper stub) and returns 0; empty lines
// continue; the exit words (h, backslash-h, backslash-q,
// backslash-quit, halt - strcasecmp fold) exit the same goodbye
// path; a bare question-mark line jumps to announce (the vendor
// goto; h011 folds to the flag back edge); anything else
// Interprets the line.
//
// Quirks kept (ledgered):
//   - the argv[1] dispatch line keeps the vendor percent doubling
//     verbatim (s %%= then ""-doubled quotes around the caret name;
//     the tail g @%% keeps its doubled percent too).
//   - puts("\n") prints newline + a second newline from the C
//     newline-in-literal (the exact byte shape kept: puts writes
//     the literal plus ONE newline).
//   - strcasecmp folds to equalsIgnoreCase over the lowercase
//     command words (the ASCII command domain).
//   - tmp/tmp1 scratch shapes exact (STR_MAX CharArray seats).
//
// BOTH consults (find_rule before render, events logged):
//   h001 io_stream_param - reused: readline/stdin is the
//     java.io.Reader edge (FileDescriptor.in seat - the reader
//     loop prints the "> " prompt itself, the shell-free fold of
//     the readline prompt); the write faces are System.out (exact
//     shapes).
//   h004 globals_ext - reused: svPtr->program seats on the Msv
//     carrier (stateVector.h:100, pinned this tranche).
//   h009 format_string - reused: the sprintf-per shapes are the
//     exact decimal renders.
//   h011 goto - reused: announce folds to the flag back edge.
//   h013 dead_store - hit: the if(0) alarm block and the static
//     struct C list dropped.
//   h018 preprocessor_conditional - reused: the database/float/
//     fp-bit macro lattice resolves against the reference build
//     config (the live arms fold into the literals; the excluded
//     arms are documented here; the __TIME__ __DATE__ seats stay
//     token-shaped).
//   h014 out_of_scope_call - reused: interpret rides its new TODO
//     stub (owner interp-interp.cpp::Interpret, roster 308, batch
//     6); __symput (the dollar-SymPut macro body, owner the
//     libmpscpp.cpp TU helper - non-unit) rides its new TODO
//     stub; cleanLocks (libmpscpp.h:215, owner the libmpscpp.cpp
//     TU helper - non-unit) rides its new TODO stub; Mglobal and
//     Mltoa ride the existing BothCRuntime seats.
//   h017 naming - hit: the percent-variable seats keep the vendor
//     macro name through the __symput seat.

// Build-config banner constants (h018 resolved; the excluded arms:
// NATIVE would give "; DBMS: Native Stand Alone" + the
// CLIENT/SHARED/READ_ONLY variants; BIGFLOAT would give "Float:
// long double"; HAVE_HARDWARE_MATH undefined would give
// "; FP Bits: 72" - all excluded by the reference build config).
internal const val MUMPS_FLOATSIZE = "Float: double"
internal const val MUMPS_DATABASE = "; DBMS: SQLITE; Memory Resident: 0"

// __TIME__ __DATE__ seats: the vendor pins the compile stamps here;
// the render keeps the tokens (the reference binary's own stamps
// pin at ITS m1-build; the seat stays token-shaped and journaled).
internal const val MUMPS_BUILD_TIME = "__TIME__"
internal const val MUMPS_BUILD_DATE = "__DATE__"

internal fun mumpsMain(args: Array<String>): Int {
    val argc = args.size
    val tmp0 = CharArray(STR_MAX) // unsigned char tmp0[STR_MAX]
    val tmp1 = CharArray(STR_MAX) // unsigned char tmp1[STR_MAX]

    // if (0) { signal(SIGALRM, SyncClock); set_alarm(1); } - dead
    // (h013); static struct C list S/p1/p2 - dead (h013).
    // int DirectRead(int, unsigned char *) and char *cleanLocks
    // - vendor prototypes, ride the stub ledger; include
    // mumpsc/cgi.h - residue ledger note (no body in this chunk).

    // program plumbing (strncpy 512; stateVector.h:100 seat)
    if (argc > 1) {
        strncpyC(svPtr.program, args[1], 512)
    } else {
        strncpyC(svPtr.program, args[0], 512)
    }

    if (argc >= 2) {
        __symput("%", args[1]) // dollar-SymPut macro seat
    } else {
        __symput("%", "")
    }

    if (argc > 2) {
        for (i in 2 until argc) {
            __symput("%" + (i - 1).toString(), args[i]) // sprintf per
        }
        __symput("%c", (argc - 2).toString()) // the count seat
        __symput("%0", args[1])
    }

    // one-shot program dispatch (before announce)
    if (argc > 1 && args[1].isNotEmpty()) {
        var tmp = args[1]
        if (tmp.startsWith("./")) {
            // the .-slash strip (the vendor shift-left loop)
            tmp = tmp.substring(2)
        }
        val dispatch =
            "s %%=\"\"\"^" + tmp + "\"\"\" g @%%" // tokens kept verbatim
        val retcode = interpret(dispatch, svPtr)
        Mglobal(CLOSE, tmp0, tmp1, svPtr) // GlobalClose fold
        return retcode
    }

    var announce = true
    // The stdin seat: FileDescriptor.in is the C-side stdio edge
    // (h001). The Kotlin keyword collision at the member name in
    // resolves through the reflection seat (h017 journal note: no
    // keyword-shaped member aliasing, the field reads once here).
    val stdFd = java.io.FileDescriptor::class.java
        .getField("in").get(null) as java.io.FileDescriptor
    val stdin = java.io.BufferedReader(
        java.io.InputStreamReader(java.io.FileInputStream(stdFd)),
    )

    while (true) { // announce:
        readHistory("./mumps_history") // history seat (quantized)

        while (true) {
            if (announce) {
                interpret(
                    "w !,\"Mumps Built: " + MUMPS_BUILD_TIME + " " +
                        MUMPS_BUILD_DATE + "\n" +
                        MUMPS_FLOATSIZE + MUMPS_DATABASE +
                        "; Hardware math: yes" + "; Int: int" +
                        ";\nFloat digits: 8" + "; Cache: 65537" +
                        "; Block: 2048" + "; FP Bits: N/A" +
                        "\",!,\"http://www.cs.uni.edu/~okane/\"," +
                        "!,\"Enter ^D or HALT to exit\",!",
                    svPtr,
                )
                announce = false
                System.out.print("\n") // the pre-loop printf("\n")
            }

            System.out.print("> ") // readline prompt (h001 fold)
            val line = try {
                stdin.readLine()
            } catch (e: java.io.IOException) {
                null // read error quantizes to EOF (h001)
            }

            if (line == null) { // EOF (^d)
                return goodbye(tmp0, tmp1)
            }

            if (line.isEmpty()) continue

            // add_history(line) - history seat (quantized no-op)

            val low = line.lowercase() // strcasecmp fold
            if (low == "halt" || low == "h" || low == "\\h" ||
                low == "\\q" || low == "\\quit"
            ) {
                return goodbye(tmp0, tmp1)
            }

            if (low == "?") { // goto announce
                announce = true
                break // back edge to the announce outer loop
            }

            interpret(line, svPtr)
            System.out.print("\n") // printf("\n")
        }
    }
}

// The shared EOF/exit-word tail (printf EOF - goodbye, globals
// close, pid lock cleanup, puts newline; the vendor shape once,
// folded once - h007 scratch: same two buffers threaded through).
internal fun goodbye(tmp0: CharArray, tmp1: CharArray): Int {
    System.out.print("EOF - goodbye ...\n") // exact printf shape
    Mglobal(CLOSE, tmp0, tmp1, svPtr)
    Mltoa(ProcessHandle.current().pid(), tmp1) // getpid
    cleanLocks(tmp1)
    System.out.print("\n\n") // puts("\n") + the newline literal
    return 0
}

// void read_history(const char *) - GNU history-lib seat, quantized
// no-op (journal: the interactive history is a program-edge
// convenience with no observable contract in the reference flow).
internal fun readHistory(path: String) {
    // quantized no-op (history-lib edge; journaled)
}

// char *cleanLocks(char *) - include/mumpsc/libmpscpp.h:215; the
// body is a libmpscpp.cpp TU helper (non-unit). TODO-stub (h014).
internal fun cleanLocks(a: CharArray): CharArray {
    TODO("stub: vendor cleanLocks, libmpscpp.cpp TU helper (libmpscpp.h:215; not a corpus unit)")
}

// int __symput(const char *, const char *) - the dollar-SymPut
// macro body (libmpscpp.h:98); owner: the libmpscpp.cpp TU helper
// family (non-unit). TODO-stub (h014).
internal fun __symput(name: String, value: String) {
    TODO("stub: vendor __symput, libmpscpp.cpp TU helper (the SymPut macro body; not a corpus unit)")
}

// int Interpret(const char *, struct MSV *) - owner:
// interp-interp.cpp::Interpret, roster 308 (batch 6, pending in
// this arm). TODO-stub (h014).
internal fun interpret(cmd: String, sv: Msv): Int {
    TODO("stub: owner interp-interp.cpp::Interpret (roster 308, batch 6)")
}
