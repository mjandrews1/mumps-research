package doc

// Unit 170 (B6 tranche 2): interp-sysfunc.cpp::TimeOut_getstr1
// (batch 6, core stratum; LOC 33, score 14.1) - the timed READ
// runtime, the getstr1 unit-140 sibling with the select timeout.
// No in-arm call site and no stub was raised for this unit (the
// generated-program contract: the emitted C text of the compiler
// READ expander names it; the SysfuncH roster notes anticipated
// the seat). Vendor mixed-case name kept per H-017.
//
// Render notes: the io!=5 banner prints then sigint(100) rides
// the Stubs.sigint seat with the VENDOR FALL-THROUGH kept - the
// body runs the select anyway after the abort call (the sigint
// abort makes it unreachable in practice; the fall-through shape
// preserved, the unit-137 Nothing-carrier precedent). The bare C
// scope block around the fd_set/timeval declarations is
// linearized. tv_sec = atoi(timeout), whole seconds only. The
// select/stty/getchar/fgets-on-stdin surface is the PROCESS-LEVEL
// OS layer: four file-private stub carriers below (the ServerMain
// OS-syscall precedent, never arm-owned). The timeout arm lands
// the empty area and returns 0 with the stty -raw restore; the
// dirflg arm stores the getchar result UNMASKED (EOF -1 stores
// the 0xFF-shape char, kept); the fgets arm keeps the
// unconditional last-byte chop (strlen-1) including the fgets-NULL
// stale-buffer case - the caller's prior contents lose their last
// byte, vendor quirk kept verbatim; the strlen==0 case indexes -1
// and the IndexOutOfBounds bounds the C UB (deterministic-
// exception quantization, journaled). Naming per H-017.

// process-level OS stub carriers (h014; never arm-owned)
private fun selectC(tvSec: Int): Int =
    throw NotImplementedError("select: process-level OS surface, not arm-owned")

private fun systemStty(raw: Boolean): Unit =
    throw NotImplementedError("system(\"stty ...\"): process-level OS surface, not arm-owned")

private fun getcharC(): Int =
    throw NotImplementedError("getchar: process-level OS surface, not arm-owned")

private fun fgetsStdinC(area: CharArray): Boolean =
    throw NotImplementedError("fgets(stdin): process-level OS surface, not arm-owned")

fun TimeOut_getstr1(io: Int, area: CharArray, timeout: CharArray, dirflg: Int): Int {
    if (io != 5) {
        System.err.print("*** Timed read only permitted to unit 5\n")
        Stubs.sigint(100)
    }

    val tvSec = atoi(CstringOps.cstring(timeout)) // tv.tv_sec

    if (dirflg != 0) systemStty(raw = true) // system("stty raw")

    val i = selectC(tvSec) // select(1, &rfds, NULL, NULL, &tv)

    if (i == 0) {
        area[0] = NUL // strcpy((char *) area, "")

        if (dirflg != 0) systemStty(raw = false) // system("stty -raw")

        return 0
    }

    if (dirflg != 0) {
        val c = getcharC()
        area[0] = c.toChar() // UNMASKED: EOF -1 stores 0xFF-shape
        area[1] = NUL
        systemStty(raw = false)
    } else {
        // fgets((char *) area, STR_MAX, stdin) - the -1 return arm
        // is quantized to the stale-buffer chop below (the C NULL
        // case leaves area holding the caller's prior contents)
        if (fgetsStdinC(area)) {
            val n = CstringOps.strlenC(area)
            area[n - 1] = NUL // unconditional last-byte chop
        } else {
            val n = CstringOps.strlenC(area)
            area[n - 1] = NUL // fgets-NULL stale-buffer chop (UB bounded)
        }
    }

    return 1
}
