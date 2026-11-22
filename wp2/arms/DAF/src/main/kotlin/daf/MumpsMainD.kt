package daf

import java.io.File

// Unit 150 (DAF B5 T3, complex): mumps.cpp::main (units.csv row 131:
// loc 125, ctl 30, score ~47-50, rank ~340s, complex): the
// interpreter entry point: the argv syllabus (program name into
// svPtr->program, the %-symbols, the %0/%c arg-count seats), the
// program-mode '"s %="""^prog""" g @%%"' boot, the announce banner
// with the build-macro disclosure, and the REPL read loop with the
// EOF/halt/? exits and the GlobalClose/cleanLocks shutdown frame.
// Consults: BASE-log row 53 family hit-reuse (main's argv/argc
// lattice folds onto the Kotlin args array; argv[0] is the program
// name seat), row 22 hit-adapt (svPtr->program: the DafMsv seat
// lattice of the earlier T3 renders pins no program field - the
// strncpy target becomes a LOCAL 512-char carrier ledged as the
// row-18 field-absence fold; no rendered downstream unit reads
// it), row 46 hit-adapt (signal/set_alarm: the alarm slice is
// constant-zero call-site-dead (if (0)) - the vendor guard is kept
// verbatim with the constant fold; the SyncClock/set_alarm bodies
// are non-existent seats and stay comment-only), row 60
// hit-adapt (readline/read_history/write_history/add_history:
// the C readline/history GLIBC API maps DOCUMENTED DIVERGENCE onto
// the JVM: readline -> BufferedReader.readLine over stdin,
// history -> an ArrayList replay written back to a
// ./mumps_history file analog; strcasecmp -> equalsIgnoreCase;
// Mltoa(getpid(),t) -> the getpidD() Long rendered into the char
// register; free(line) -> GC no-op ledger), row 33 hit-reuse
// (sprintf(t,"%%%d",i-1) and the argc count fold onto String
// formatting), row 101 family hit-reuse (the raw tmp/tmp0/tmp1
// registers keep the CharArray carriers; the vendor strncpy
// bounds ride explicit copy loops per the b-tree key precedent),
// row 37 hit-adapt (Interpret rides the named stub InterpretDaf;
// \\$SymPut rides SymPutD; Mglobal(CLOSE) via MglobalDaf with the
// CLOSE opcode pinned here as CLOSE_D = 4 (defines.h:87; the
// CLOSE_D_207 code-point constant of MstringFnumber is a
// different family), cleanLocks via the cleanLocksDaf stub).
// Build-macro lattice disclosed (reference build pins): __TIME__
// and __DATE__ are compile-time stamp macros - they have no
// single vendor value; the render substitutes the literals
// "__TIME__"/"__DATE__" verbatim into the banner (a logged
// placeholder divergence); database = "" (no SQLITE/NATIVE*
// macro defined in the reference build); floatSize =
// "; Float: double" (BIGFLOAT unset); Fpbits = "; FP Bits: 72"
// (HAVE_HARDWARE_MATH unset); HardwareMath/Ints/FltDigits/Cache/
// BlkSiz verbatim. cgi.h include disposition: reference build
// carries no cgi functions into main's body - KDoc ledger only.
// The NATIVE_SHARED btree(CLOSE,...) pre-loop deadlock guard is
// a dead #ifdef branch (not defined in the reference build) -
// KDoc ledger only, not emitted. The static struct C *S,*p1,*p2
// locals are declared and never used by the vendor body -
// comment-only fold (unused-local ledger).
// Quirk ledger (kept verbatim): the "./" prefix strip of argv[1]
// in program mode; program mode GlobalCloses and returns WITHOUT
// announcing; the REPL '?' line re-announces via the goto
// (Kotlin label seat); the halt family is case-INSENSITIVE
// ("h","\h","\q","\quit","halt"); an empty line free's and
// continues; EOF and halt both print "EOF - goodbye ...", write
// history, Mglobal(CLOSE), pid-cleanLocks, puts("\n"), return 0.
// Dafny: EXEMPT (entry-point procedural harness over the stubbed
// Interpret/global seats; the T2 exemption family).

const val CLOSE_D = 4 // #define CLOSE 4 (defines.h:87)

// The dead alarm slice of main (vendor: if (0)) - constant fold.
const val ALARM_SLICE_ZERO_D = 0

fun mainDaf(args: Array<String>): Int {

    // static struct C { char *s; C *next; } *S=NULL,*p1,*p2=NULL -
    // declared and never used by the vendor body (unused-local
    // ledger, comment-only fold)

    val tmp = CharArray(STR_MAX_DAF)      // unsigned char tmp[STR_MAX]
    val tmp0 = CharArray(STR_MAX_DAF)     // unsigned char tmp0[STR_MAX]
    val tmp1 = CharArray(STR_MAX_DAF)     // unsigned char tmp1[STR_MAX]
    val sv = DafInterpState.svPtr         // struct MSV *svPtr (singleton)

    if (ALARM_SLICE_ZERO_D != 0) { // if (0) { // no alarm if slice is zero
        // signal(SIGALRM, SyncClock); set_alarm(1);
        // - constant-false vendor guard, bodies non-existent seats
    }

    // int DirectRead(int, unsigned char*) - local prototype,
    // out-of-scope in this TU chunk (KDoc ledger only)
    // char *cleanLocks(char*) - rides cleanLocksDaf (stubs file)
    // #include <mumpsc/cgi.h> - no cgi call sites in main's body

    val program = CharArray(512 + 1) // strncpy((char*)svPtr->program,
    // ..., 512); - LOCAL carrier: the DafMsv seat lattice pins no
    // program field (row 18 field-absence fold, disclosed above)
    val src = if (args.isNotEmpty()) args[0] else ""
    var pi = 0
    while (pi < src.length && pi < 512) {
        program[pi] = src[pi]
        pi++
    }

    if (args.size > 1) { // if (argc>=2)
        SymPutD("%", args[1])
    } else {
        SymPutD("%", "")
    }

    if (args.size > 2) { // if (argc>2)
        for (i in 2 until args.size) {
            val t = "%${i - 1}" // sprintf(t,"%%%d",i-1)
            SymPutD(t, args[i])
            // printf("%d .... %s ..... %s\n",i,t,argv[i]) - vendor
            // debug print, kept comment-only
        }
        val count = "${args.size - 2}" // sprintf(t,"%d",argc-2)
        SymPutD("%c", count)
        SymPutD("%0", args[1])
    }

    if (args.size > 1 && args[1].isNotEmpty()) { // argc>1 && strlen>0
        copyInto(tmp, args[1]) // strcpy((char*)tmp, argv[1])

        if (tmp[0] == '.' && tmp[1] == '/') { // strncmp(tmp,"./",2)==0
            val i = lenUntilNul(tmp)
            var j = 2
            while (j < i) { // for (i=2; tmp[i]; i++) tmp[i-2]=tmp[i];
                tmp[j - 2] = tmp[j]
                j++
            }
            tmp[j - 2] = '\u0000' // tmp[i-2]=0
        }

        val line = "s %=\"\"\"^" + cstringCompatible(tmp) +
            "\"\"\" g @%" // strcpy/strcat pair into tmp1
        val retcode = InterpretDaf(line, sv) // int retcode=Interpret(...)
        MglobalDaf(CLOSE_D, tmp0, tmp1, sv) // GlobalClose
        return retcode
    }

    // -----------------------------------------------------------------
    // announce (goto announce label seat; history/REPL seats below)
    // -----------------------------------------------------------------

    fun announce() { // announce:
        // __TIME__/__DATE__ compile-time macros: logged placeholder
        // substitution (disclosed above); database lattice pinned
        InterpretDaf("w !,\"Mumps Built: __TIME__ __DATE__\n" +
            "; Float: double" + // floatSize (BIGFLOAT unset)
            "" + // database (no DB macro defined)
            "; Hardware math: yes" + // HardwareMath
            "; Int: int" + // Ints
            ";\nFloat digits: 8" + // FltDigits
            "; Cache: 65537" + // Cache
            "; Block: 2048" + // BlkSiz
            "; FP Bits: 72" + // Fpbits (HAVE_HARDWARE_MATH unset)
            "\",!,\"http://www.cs.uni.edu/~okane/\",!," +
            "\"Enter ^D or HALT to exit\",!", sv)
    }

    print("\n") // printf("\n")
    // read_history("./mumps_history") - JVM history analog: load
    val historyFile = File("mumps_history")
    val history = ArrayList<String>()
    if (historyFile.isFile) {
        historyFile.forEachLine { if (it.isNotEmpty()) history.add(it) }
    }
    val stdinLine = { readLine() } // readline seat: kotlin.io stdin

    while (true) { // while(1)

        // #ifdef NATIVE_SHARED btree(CLOSE,tmp0,tmp1,svPtr) - the
        // deadlock guard of the shared-native build: dead #ifdef
        // branch of the reference build, KDoc ledger only

        print("> ")
        val line = stdinLine() ?: run { // line = readline("> ")
            // if (line == NULL) { // ^d - end of file
            print("EOF - goodbye ...\n")
            MglobalDaf(CLOSE_D, tmp0, tmp1, sv) // Close Globals
            writeHistoryD(historyFile, history) // write_history
            pidInto(getpidD(), tmp1) // Mltoa(getpid(),tmp1)
            cleanLocksDaf(cstringCompatible(tmp1))
            println() // puts("\n")
            return 0
        }

        if (line.isEmpty()) { // if (strlen(line)==0)
            continue // free(line); GC no-op ledger
        }

        history.add(line) // add_history(line)

        val lower = line.lowercase() // strcasecmp family
        if (lower == "h" || lower == "\\h" || lower == "\\q" ||
            lower == "\\quit" || lower == "halt") {
            print("EOF - goodbye ...\n")
            MglobalDaf(CLOSE_D, tmp0, tmp1, sv) // Close Globals
            writeHistoryD(historyFile, history) // write_history
            pidInto(getpidD(), tmp1) // Mltoa(getpid(),tmp1)
            cleanLocksDaf(cstringCompatible(tmp1))
            println() // puts("\n")
            return 0
        }

        if (line == "?") {
            announce() // goto announce
            continue
        }

        InterpretDaf(line, sv) // Interpret(line,svPtr)
        // free(line); - GC no-op ledger
        print("\n") // printf("\n")
    }
}

// ---------------------------------------------------------------------
// unit-local helper seats (comment-headed; row 101 CharArray family)
// ---------------------------------------------------------------------

// strcpy analog with explicit bound (the strncpy(512) precedent).
private fun copyInto(dst: CharArray, s: String) {
    var i = 0
    val n = dst.size - 1
    while (i < s.length && i < n) {
        dst[i] = s[i]
        i++
    }
    dst[i] = '\u0000'
}

// strlen over the NUL-terminated CharArray carrier.
private fun lenUntilNul(buf: CharArray): Int {
    var i = 0
    while (i < buf.size && buf[i] != '\u0000') i++
    return i
}

// cstring-compatible read of a NUL-terminated register (the
// DafCRuntime.cstring seat kept local: unit-private copy, same
// shape).
private fun cstringCompatible(buf: CharArray): String {
    val sb = StringBuilder()
    var i = 0
    while (i < buf.size && buf[i] != '\u0000') {
        sb.append(buf[i])
        i++
    }
    return sb.toString()
}

// Mltoa(getpid(),tmp1) analog: the Long pid rendered into the char
// register (row 33 formatting fold).
private fun pidInto(pid: Long, dst: CharArray) {
    val s = pid.toString()
    var i = 0
    while (i < s.length && i < dst.size - 1) {
        dst[i] = s[i]
        i++
    }
    dst[i] = '\u0000'
}

// write_history("./mumps_history") analog: the JVM history file
// divergence (row 60 ledger, disclosed above).
private fun writeHistoryD(f: File, history: List<String>) {
    f.writeText(history.joinToString("\n") + "\n")
}
