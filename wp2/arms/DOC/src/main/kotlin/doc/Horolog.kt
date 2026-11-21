package doc

import java.util.Locale

// Unit 256 (B9 tranche 1): interp-sym.cpp::_horolog (batch 9,
// simple stratum; units.csv row 278: loc 16, ptr 0, ctl 2, score
// 4.9, rank 2; physLOC 16 physically verified this session).
// The sym zfcn helper behind the compiler's emitted
// _horolog(tmp) call (compiler-parse.c:829 emission /* dollar-h
// */) and the libmpscpp.cpp:3446 Horolog() free function (its
// call site rebinds to this real seat this push per the
// delegate-when-owner-lands mechanism; see the LibmpscppMisc6
// row-note). VENDOR EPOCH QUIRK (H-013): this body uses the
// 47122 day ladder, NOT the 47116+1 ladder of the Fcn.kt case-14
// arm - the vendor itself carries two different horolog bases
// (the inline zhorolog arm vs this helper); both kept verbatim,
// the divergence is vendor-truth. time(&timex) renders as the
// System.currentTimeMillis()/1000 intrinsic (H-010 adjacent,
// the Fcn.kt precedent); sprintf %ld + strcat render as
// String.format Locale.US writes onto the carrier per H-009
// (strcpyS writes at offset 0, strcatS appends at the NUL; the
// tmp2 scratch keeps the 64-char carrier). Naming kept per the
// underscore-seat precedent.

fun _horolog(out: CharArray) {
    var timex = System.currentTimeMillis() / 1000L // time(&timex)
    var day_ = timex / 86400
    timex = timex - (day_ * 86400)
    day_ = 47122 + day_
    var fd = day_
    // sprintf((char*) in, "%ld", fd)
    CstringOps.strcpyS(out, String.format(Locale.US, "%d", fd))
    // strcat((char*) in, ",")
    CstringOps.strcatS(out, ",")
    fd = timex
    val tmp2 = CharArray(64)
    // sprintf(tmp2, "%ld", fd); strcat((char*) in, tmp2)
    CstringOps.strcpyS(tmp2, String.format(Locale.US, "%d", fd))
    CstringOps.strcatS(out, CstringOps.cstring(tmp2))
    return
}
