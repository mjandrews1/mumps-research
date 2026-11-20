package doc

import java.util.Locale

// Unit 194 (B7 tranche 1): interp-sym.cpp::_justify (batch 7,
// simple stratum; LOC 26, score 8.8, rank 81; physLOC 1328,
// physically verified this session, nonblank 26 matches the
// roster row). The JUSTIFY helper of the sym table: left-pad
// text mode when the third arg is "-1" (the strcmp against the
// literal; the render compares the C string of the p carrier,
// semantic identity per the String fold of the unit-70 command
// build), numeric mode otherwise. Left-pad arm: k = strlen of
// the in-copy, k >= l copies in verbatim, l clamps to 255, then
// the REAL lpad of StrmanipMisc.kt (unit-19-era render, direct
// call per the Fcn.kt unit-123 precedent). Numeric arm:
// sprintf(tmp2, "%c%d%c%dlf", ...) builds the C format, atof
// parses the value, sprintf(bd, tmp2, t1) formats: rendered as
// String.format Locale.US with the Java f conversion (H-009
// sprintf idiom, the Avg.kt %g precedent; Java f is the C lf
// for double per the JVM contract), the atof seat is the
// file-private atofJ below (H-010: the Zfcn atofC precedent,
// leading-double parse, 0.0 on no parse). strncpy carriers per
// H-012 (strcpyC stops at the C NUL, the strncpy-STR_MAX
// idiom is the same bytes for C-string inputs). Naming kept
// per the _find/_extract underscore-seat precedent.

private fun atofJ(buf: CharArray): Double {
    // atof: leading double of the C string, 0.0 if none parses
    val s = CstringOps.cstring(buf)
    val m = Regex("[+-]?(\\d+\\.?\\d*([eE][+-]?\\d+)?|\\.\\d+([eE][+-]?\\d+)?)").find(s)
        ?: return 0.0
    return m.value.toDoubleOrNull() ?: 0.0
}

fun _justify(out: CharArray, in_: CharArray, w: CharArray, p: CharArray) {
    val bd = CharArray(STR_MAX)
    var l = atoi(CstringOps.cstring(w))
    CstringOps.strcpyC(bd, in_)

    if (CstringOps.cstring(p) == "-1") {
        val k = CstringOps.strlenC(bd)

        if (k >= l) {
            CstringOps.strcpyC(out, in_)
            return
        }

        if (l > 255) {
            l = 255
        }
        lpad(bd, bd, l.toShort())
        CstringOps.strcpyC(out, bd)
        return
    }

    val k = atoi(CstringOps.cstring(p)) // arg 3
    // sprintf(tmp2, "%c%d%c%dlf", '%', l, '.', k) then
    // sprintf(bd, tmp2, t1): one Java format call (H-009 note)
    CstringOps.strcpyS(out, String.format(Locale.US, "%" + l + "." + k + "f", atofJ(in_)))
}
