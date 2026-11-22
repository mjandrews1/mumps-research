package doc

// UNIT 304 RENDER - batch 10 DOC tranche T3 (complex).
// interp-select.cpp::<misc> - roster row 284, loc 156. The
// unit body is _select: the runtime \u0024select evaluation
// cascade over the ten condition/value pairs (copied by the
// compiler as the emitted _select(...) call, so the render
// owns the whole file body: preamble banners ledgered below).
//
// ADJUDICATIONS (file misc pool, vendor order):
// - The sym.c-style banner block (copyright, Dec 13 2025,
//   SYMDEBUG comment, sym.c description, the include family,
//   the SQLITE define, _INTERP_/CSTR/USTR defines), the
//   Interpret/ScanParse/Eval prototypes and the
//   __label_lookup/__text_function extern pair, and the
//   intLong/_INTERP_ define pair (floatSize comment) ride
//   the landed precedent renders (DefinesH ledger, InterpSymMisc
//   banners, InterpFcnMisc, LibmpscppHMisc) - banner rows,
//   no code seat in this render (H-018 family).
// - The odd slots after the first pair are C NULL-capable
//   call arms; the render takes CharArray? and probes them
//   in vendor order (in3/in5/.../in19, then the double
//   fall-out print+sigint tail). in1/in2/unprobed evens and
//   out are the working non-null seats.
// - atol((char *) inN) rides a private atolSel helper over
//   the NUL-terminated CharArray (sign/digit walk, vendor
//   atol semantics: leading whitespace skipped, non-digits
//   stop the parse - strtod's carrier seat precedent); the
//   '\u0024' literal is not part of this body (name kept
//   verbatim _select, the leading underscore preserved).
// - strmove rides the Stubs.strmove seat; printf keeps the
//   *** Error M4 banner text; sigint(100) via Stubs.sigint.
// - NO Stubs flip: _select has no landside Stubs seat (the
//   compile-side emission names it in generated C; the run
//   arm consumes this render directly in future call arms).
//
// Consult row logged 2026-11-22T06:24:00Z (H-014 hit-reuse,
// the strmove seat family).

private fun atolSel(a: CharArray?): Long {
    if (a == null) return 0L
    var i = 0
    val n = a.size
    while (i < n && (a[i] == ' ' || a[i] == '\t' || a[i] == '\n' ||
            a[i] == '\r' || a[i] == '\u000C')
    ) i++
    var neg = false
    if (i < n && (a[i] == '+' || a[i] == '-')) {
        neg = (a[i] == '-')
        i++
    }
    var v = 0L
    while (i < n && a[i] in '0'..'9') {
        v = v * 10 + (a[i].code - '0'.code)
        i++
    }
    return if (neg) -v else v
}

private fun selectErrSel(svPtr: Msv) {
    System.out.print(
        "*** Error M4 - Select in or near line " +
            svPtr.LineNumber + "\n"
    )
    Stubs.sigint(100)
}

fun _select(
    out: CharArray,
    in1: CharArray,
    in2: CharArray,
    in3: CharArray?,
    in4: CharArray,
    in5: CharArray?,
    in6: CharArray,
    in7: CharArray?,
    in8: CharArray,
    in9: CharArray?,
    in10: CharArray,
    in11: CharArray?,
    in12: CharArray,
    in13: CharArray?,
    in14: CharArray,
    in15: CharArray?,
    in16: CharArray,
    in17: CharArray?,
    in18: CharArray,
    in19: CharArray?,
    in20: CharArray,
    svPtr: Msv
) {
    if (atolSel(in1) != 0L) {
        Stubs.strmove(out, in2)
        return
    }

    if (in3 == null) selectErrSel(svPtr)
    if (atolSel(in3) != 0L) {
        Stubs.strmove(out, in4)
        return
    }

    if (in5 == null) selectErrSel(svPtr)
    if (atolSel(in5) != 0L) {
        Stubs.strmove(out, in6)
        return
    }

    if (in7 == null) selectErrSel(svPtr)
    if (atolSel(in7) != 0L) {
        Stubs.strmove(out, in8)
        return
    }

    if (in9 == null) selectErrSel(svPtr)
    if (atolSel(in9) != 0L) {
        Stubs.strmove(out, in10)
        return
    }

    if (in11 == null) selectErrSel(svPtr)
    if (atolSel(in11) != 0L) {
        Stubs.strmove(out, in12)
        return
    }

    if (in13 == null) selectErrSel(svPtr)
    if (atolSel(in13) != 0L) {
        Stubs.strmove(out, in14)
        return
    }

    if (in15 == null) selectErrSel(svPtr)
    if (atolSel(in15) != 0L) {
        Stubs.strmove(out, in16)
        return
    }

    if (in17 == null) selectErrSel(svPtr)
    if (atolSel(in17) != 0L) {
        Stubs.strmove(out, in18)
        return
    }

    if (in19 == null) selectErrSel(svPtr)
    if (atolSel(in19) != 0L) {
        Stubs.strmove(out, in20)
        return
    }

    selectErrSel(svPtr)
}
