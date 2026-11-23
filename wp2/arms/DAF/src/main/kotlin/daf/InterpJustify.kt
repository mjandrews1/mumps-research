package daf

// Unit 194 (DAF B7 T1, simple): interp-sym.cpp::_justify (26 LOC,
// score 8.8): the \$justify service helper: left-pad the value to
// width when the third arg is the "-1" sentinel, else format the
// numeric value with the ("%l.klf") width/precision dialog.
// Consults (2026-11-23T18:34:00Z, nominal): BASE-log rows 14/15
// hit-reuse (the strcmp "-1" sentinel compare folds onto the
// String == over the cstring carrier of p), row 28 hit-reuse
// (atoi for the width l and precision k), row 26 hit-adapt
// (FORMAT-STRING KEPT VERBATIM with one quantized seat: the
// vendor builds "%c%d%c%dlf" then formats the double t1 - the C
// 'l' length modifier on a double arg is the plain "%f" dialog in
// effect; the JVM formatter has no 'l' modifier, so the built
// format drops it ("%l.kf"), a semantic-equivalent quantization
// ledgered here; the %%c-=-% pass-through of the leading literal is
// the vendor's own construction order, kept by building the same
// digit pieces), row 27 hit-adapt (bd/tmp2 the scratch carriers:
// bd CharArray stays the lpad working buffer per the aliasing
// discipline of the lpadStrmanipD render - the in-place
// lpad(bd,bd,l) call is alias-safe exactly as documented there;
// tmp2's 32-char carrier folds onto the built format String), row
// 30 hit-adapt (the out[] param stays an out-seat: the callers
// read the NUL-terminated result onward; strncpy semantics fold
// onto the capped cstring write below), manual-decision (no BASE
// row: k >= l guard ABSENT at the format build - a k > l build
// produces "%5.7f" style dialogs the vendor passes to sprintf as
// is; kept verbatim, the JVM formatter raises where C would print
// the padded value: divergence noted, caller-domain adjudication).
// Dafny: EXEMPT (formatting dialog over the pad/format seats;
// journal row).

fun justifyDaf(out: CharArray, inStr: CharArray, w: CharArray, p: CharArray) {

    var l = atoi(DafCRuntime.cstring(w)) // l = atoi(w)
    val bd = CharArray(STR_MAX_DAF)      // char bd[STR_MAX]
    val inS = DafCRuntime.cstring(inStr)

    // strncpy((char *) bd, (char *) in, STR_MAX) - happens before the
    // compare in the vendor order
    copyIntoCarrier(bd, inS)

    if (DafCRuntime.cstring(p) == "-1") { // strcmp(p, "-1") == 0

        var k = DafCRuntime.cstring(bd).length // k = strlen(bd)
        if (k >= l) { // if (k >= l) { strncpy(out, in, STR_MAX); return }
            copyIntoCarrier(out, inS)
            return
        }

        if (l > 255) l = 255 // if (l > 255) l = 255;
        lpadDaf(bd, bd, l)    // lpad(bd, bd, l); - the T2 seat flip
        copyIntoCarrier(out, DafCRuntime.cstring(bd)) // strncpy(out, bd, ...)
        return
    }

    val k = atoi(DafCRuntime.cstring(p)) // k = atoi(p) - arg 3

    // sprintf(tmp2, "%c%d%c%dlf", '%', l, '.', k) builds "%<l>.<k>lf"
    // (the 'l' quantized out per the row-26 note)
    val tmp2 = "%" + String.format(java.util.Locale.US, "%d", l) +
        "." + String.format(java.util.Locale.US, "%d", k) + "f"

    val t1 = atofD(inS) // t1 = atof(in);
    val bdS = String.format(java.util.Locale.US, tmp2, t1) // sprintf(bd, tmp2, t1)

    copyIntoCarrier(out, bdS) // strncpy(out, bd, STR_MAX)
}

// strncpy-capped cstring write (row 27 family): copies the String
// into the carrier with NUL padding, capped at the carrier length.
private fun copyIntoCarrier(dst: CharArray, s: String) {
    val cs = s.toCharArray()
    var i = 0
    while (i < cs.size && i < dst.size) {
        dst[i] = cs[i]
        i++
    }
    if (i < dst.size) dst[i] = NUL_D
}
