package doc

// Unit 136 (B5 tranche 2): libmpscpp.cpp::global::Sum (batch 5, core
// stratum; LOC 21, score 11.9) - the sibling-summing class member with
// no arguments, rendered as a GlobalRef extension (unit-65 Concat
// member precedent). VENDOR TRAILING-DELIMITER QUIRK kept verbatim:
// after i=strlen(tmp) the test reads tmp[i-1] and tmp[i-2] against
// 32 - when the last real byte is a low control byte a
// pre-decrement clears it (tmp[--i]=0), so a delimiter-terminated
// key loses its final delimiter; preserved in shape. The XNEXT walk
// (Mglobal code 8) is the unit-37 KiLL family idiom: while the next
// sibling whose first i bytes match this global's name prefix
// exists, fetch and accumulate. strncmp(ref,tmp,i) breaks the walk
// when the sibling diverges from the prefix. get_global rides the
// H-014 stub; the !tpx fetch-failure arm throws MumpsGlobalException
// on GLOBAL_NOT_FOUND=2; strtod is the arm decimal-prefix helper
// (unit-40 seat; H-010 numeric conversion contract). Naming per
// H-017 (Sum -> sum).

fun GlobalRef.sum(): Double {
    val ttt = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)
    var tot = 0.0

    CstringOps.strcpyS(tmp, ref)
    var i = CstringOps.strlenC(tmp)

    // trailing control-byte trim quirk kept (see header)
    if (tmp[i - 1] < Char(32) && tmp[i - 2] < Char(32)) {
        i--
        tmp[i] = NUL
    }

    val refA = ref.toCharArray()

    while (true) {
        if (Stubs.Mglobal(XNEXT, tmp, ttt, Globals.svPtr) == 0) break

        if (CstringOps.strncmpC(refA, tmp, i) != 0) break

        CstringOps.strcpyC(tmp1, tmp)
        Stubs.get_global(tmp1, rslt)

        if (Globals.svPtr.tpx == 0L) {
            Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        tot = tot + strtod(CstringOps.cstring(rslt))
    }

    return tot
}
