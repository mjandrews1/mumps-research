package mumps.interp

/**
 * PRO-arm translation of interp-sym.cpp::_length (roster row 306,
 * batch 10; units.csv: loc 16, comment_ratio 0.059,
 * pointer_marks 0, control_marks 2, score 5.4, rank 30).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256
 * 621deb2586f27e741ce6db1a2a86fbddf4e4da40c9c6bb363f84c814f53ffdbe),
 * reference source lines 1361-1385.
 *
 * _length(out, in, key): the <dollar>LENGTH run-time with an
 * occurrence-count key: if the key is non-empty, xindex-walk the
 * occurrences of key in in (advancing i by both the hit position
 * and the key length each round) and render the hit count PLUS ONE
 * - the piece count convention (vendor comment: one piece when
 * there are no hits); with an EMPTY key the plain character
 * strlen of in is rendered.
 *
 * Seat note: the ZfcnsH residue ledger tagged _length as the
 * MstringLength/GlobalLength mdh sibling family - those are the
 * libmpscpp overload twins; THIS seat is the interp-sym.cpp
 * unsigned-char trio carrier (the compiler's Parse_ <dollar>length
 * intrinsic emits calls to it), u202/InterpFcnMisc disposable-
 * prototype precedent, no stub was raised for it.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : out/in/key are C-string CharArray
 *    buffers; strlen renders strlenC.
 *  - h009 format_string     : sprintf((char*) out, "%d", ...) with
 *    a decimal i renders via writeCstring(out, i.toString()) (h009
 *    "%d" collapse, Find.kt:38 precedent).
 *  - h014 out_of_scope_call  : xindex is the DONE same-package
 *    seat (Xindex.kt:36, u256 family); its K&R short strt carrier
 *    takes the Int i with a toShort() at the call (u213 clamp
 *    convention, xindex KDoc note).
 *  - h017 naming             : _length keeps the vendor
 *    underscore-leading spelling verbatim (the arm's own name, no
 *    collision; the mdh twins are class members elsewhere).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The returned count is j + 1: an input with ZERO occurrences
 *    renders "1" - the piece convention (the vendor's own "one
 *    piece" shape, kept; the GlobalLength.kt twin note matches).
 *  - The non-empty-key arm never re-renders plain length; the
 *    empty-key arm returns before the walk - both kept verbatim.
 *  - i is reused as the xindex start cursor INSIDE the while
 *    condition (i = xindex(...)) and immediately advanced by k on
 *    each hit - kept exactly; xindex returns 0 on no (further)
 *    match, terminating the walk.
 */
fun _length(out: CharArray, in_: CharArray, key: CharArray) {
    if (strlenC(key) != 0) {
        var i = 1
        var j = 0
        val k = strlenC(key)

        while (true) {
            i = xindex(in_, key, i.toShort()) // (short) i
            if (i == 0) break
            j++
            i += k
        }

        writeCstring(out, (j + 1).toString()) // sprintf(out, "%d", j + 1)
        return
    }

    writeCstring(out, strlenC(in_).toString()) // sprintf(out, "%d", strlen(in))
    return
}
