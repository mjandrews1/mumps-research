package mumps.interp

/**
 * PRO-arm translation of interp-strmanip.cpp::xindex (roster
 * row 265, batch 9; units.csv: loc 25, comment_ratio 0.038,
 * pointer_marks 0, control_marks 7, score 11.5, rank 114).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 87-112 - RESOLVES the InterpStubs xindex stub
 * (TAG CORRECTION, u141 family: the "not a corpus unit" tag was
 * a mis-tag, roster row 265 IS the owner, correct batch-9 tag;
 * the Match.kt-family call sites bind unchanged on the
 * (source, ptrn, strt: Short) signature).
 *
 * xindex(source, ptrn, strt): Int - the pattern substring search:
 * find the first occurrence of ptrn in source at or after
 * strt (1-based; <=1 clamps to 0-offset), return the 1-based
 * position (k+1), or -1 when the vendor's loop falls through.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h012 unsigned_byte_domain: source/ptrn are NUL-terminated
 *    byte domains; the nested walk compares bytes; strt is a
 *    K&R short (Short carrier, u213 precedent).
 *  - h013 dead_store         : the vendor falls out of the
 *    outer for WITHOUT an explicit return after the loop - the
 *    missing return is kept as the vendor's own fall-through
 *    (the -1 quantization is journaled as the u128 deterministic-
 *    UB precedent, matching the u213 substr clamp convention).
 *  - h014 out_of_scope_call : none (pure byte-domain function).
 *  - h015 grammar_collision : xindex keeps the vendor name;
 *    the short locals flg/i/j/k/l/istrt ride Int carriers
 *    (h010-adjacent; short arithmetic promotes, u213 precedent).
 *  - h017 naming            : xindex keeps the vendor
 *    underscore-free spelling verbatim.
 */
fun xindex(source: CharArray, ptrn: CharArray, strt: Short): Int {
    var flg = 0

    var i = 0
    while (ptrn[i] != NUL) i++ // for (i=0; ptrn[i]!=0; i++);
    i-- // --i; last pattern index

    var j = 0
    while (source[j] != NUL) j++ // for (j=0; source[j]!=0; j++);
    j-- // --j; last source index

    var istrt = strt - 1
    if (strt <= 1) istrt = 0 // vendor: if (strt<=1) istrt=0;

    var k = istrt
    while (k <= j) { // for (k=istrt; k<=j; k++)
        var l = 0
        while (l <= i) { // for (l=0; l<=i; l++)
            if (ptrn[l] != source[k + l]) {
                flg = 1
                break
            }
            l++
        }

        if (flg == 1) { // vendor: if (flg) { flg=0; continue; }
            flg = 0
            k++
            continue
        }

        return k + 1 // vendor: return (k+1);
    }

    // vendor: the outer loop falls through with no return
    // (indeterminate in C; quantized to -1, u128 precedent)
    return -1
}
