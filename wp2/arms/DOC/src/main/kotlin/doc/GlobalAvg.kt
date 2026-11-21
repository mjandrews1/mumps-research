package doc

// Unit 261 (B9 tranche 2): libmpscpp.cpp::global::Avg (batch 9,
// core stratum; units.csv row 262: loc 28, ptr 6, ctl 3, score
// 11.2, rank 116; physLOC 28 physically verified this session).
// The averaging class member of the XNEXT aggregation family -
// the unit-136 global::Sum twin, rendered as a GlobalRef
// extension (unit-65 Concat member precedent). VENDOR TRAILING-
// DELIMITER QUIRK kept verbatim (the Sum header ruling): after
// i=strlen(ref) the tmp[i-1]/tmp[i-2] tests against 32 can
// pre-decrement off the final delimiter of a
// delimiter-terminated key (tmp[--i]=0); preserved in shape.
// The walk is the unit-37 KiLL family idiom: order XNEXT over
// the siblings whose first i bytes match this global's ref
// prefix (strncmp(ref,tmp,i) break), fetch each through the
// get_global two-buffer member (H-014 stub seat, called then
// tpx checked per the family), and accumulate tot/j (the C
// long j and the j==0 return-0.0 arm kept verbatim). VENDOR
// DIVERGENCE from Sum (H-010 adjacent, H-013 ledger): the
// fetch-body conversion is sscanf(rslt, "%lf", &val) whose
// ZERO-RITEMS return (no conversion) raises
// INVALID_CONVERSION=1 + MumpsGlobalException - NOT a plain
// strtod fold; the ASCII decimal-prefix scan folds to the
// private sscanfLF helper below (strtod contract, with the
// no-conversion signal surfaced as null so the ==0 arm can
// throw DISTINCTLY from a legitimate 0.0 parse). Consult
// H-014 hit-reuse (Mglobal XNEXT 8, get_global, ErrorMessage
// family through the Sum precedent). The Stubs.globalAvg seat
// flips to delegate this push (the unit-130 free Avg wrapper
// call sites bind unchanged). Naming per H-017 (Avg verbatim
// member, GlobalRef receiver per the Concat precedent).

fun GlobalRef.Avg(): Double {
    val ttt = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)
    var j = 0L
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
        if (Stubs.Mglobal(XNEXT, tmp, ttt, Globals.svPtr) == 0)
            break

        if (CstringOps.strncmpC(refA, tmp, i) != 0) break

        CstringOps.strcpyC(tmp1, tmp)
        Stubs.get_global(tmp1, rslt)

        if (Globals.svPtr.tpx == 0L) {
            Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        // if (sscanf(rslt, "%lf", &val) == 0) - the zero-ritems
        // no-conversion arm (see header)
        val val_ = sscanfLF(CstringOps.cstring(rslt))
        if (val_ == null) {
            Globals.GlobalExceptionCode = INVALID_CONVERSION
            throw MumpsGlobalException()
        }
        val v = val_

        tot = tot + v
        j++
    }

    if (j == 0L) return 0.0

    return tot / j
}

// C sscanf("%lf") adapter: the strtod ASCII decimal-prefix
// contract (leading whitespace skipped, optional sign, digits
// with optional fraction and exponent); NULL when the format
// converted nothing (the ==0 arm), the parsed double otherwise
// (0.0 is a VALID parse, distinct from null).
private fun sscanfLF(a: String): Double? {
    var i = 0
    while (i < a.length && (a[i] == ' ' || a[i] == '\t' || a[i] == '\n' ||
            a[i] == '\r' || a[i] == '\u000c')) i++
    val start = i
    if (i < a.length && (a[i] == '+' || a[i] == '-')) i++
    var sawDigit = false
    while (i < a.length && a[i] in '0'..'9') { i++; sawDigit = true }
    if (i < a.length && a[i] == '.') {
        i++
        while (i < a.length && a[i] in '0'..'9') { i++; sawDigit = true }
    }
    if (!sawDigit) return null
    if (i < a.length && (a[i] == 'e' || a[i] == 'E')) {
        var j = i + 1
        if (j < a.length && (a[j] == '+' || a[j] == '-')) j++
        var any = false
        while (j < a.length && a[j] in '0'..'9') { j++; any = true }
        if (any) i = j
    }
    return a.substring(start, i).toDouble()
}
