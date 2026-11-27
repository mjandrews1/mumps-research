package both

// Unit 247 (BOTH B9, simple): interp-sym.cpp::_horolog (16 LOC).
// Batch_plan ordinal 247: simple, score 2.8. The \\u0024H horolog
// helper: read the wall clock, split into day-count (offset
// 47122, the vendor MUMPS epoch) and seconds-of-day, and emit
// "day,sec" into the caller's byte buffer.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: the unsigned char in[] out-param
//     stays a CharArray in the C byte domain; the sprintf+strcat
//     writes are rendered as direct string assembly then one
//     copy-in with NUL (the C idiom, no partial-overlap).
//   h008 static_scratch - reused: char tmp2[64] is a local (not
//     static) scratch; the two-step sprintf(tmp2)+strcat dance
//     folds into the single assembly string (no caller aliases
//     the buffer; the fold is behavior-identical).
//   h009 format_string - reused: "%ld" is a plain long decimal;
//     Kotlin Long.toString gives the same digit shape for the
//     nonnegative values this site produces.
//   h014 out_of_scope_call - reused: time(&timex) is the
//     System.currentTimeMillis()/1000 seat (BifsZfcn zdateZb
//     precedent, same-TU arm seat); no new clock seat raised.
//   h017 naming - reused (the fun keeps the vendor name
//     _horolog; day/fd/timex keep vendor names).
//   Dafny: exempt (wall-clock side effect; no decidable core).
//
// Vendor quirks kept (ledger):
//   - the C division/remainder pair (day = timex/86400;
//     timex -= day*86400) truncates toward zero; for the
//     pre-1970 negative range the vendor would emit a negative
//     seconds-of-day, kept here by using the same truncating
//     division (Kotlin Long '/' matches C for this shape).
//   - in[] receives no explicit NUL from the vendor beyond the
//     final strcat's terminator; the copy-in adds it once.

internal fun _horolog(inBuf: CharArray) {
    // C: timex = time(&timex);
    var timex = System.currentTimeMillis() / 1000

    // C: day = timex / 86400; timex = timex - (day * 86400);
    //    day = 47122 + day;
    val day = timex / 86400
    timex -= day * 86400
    val fd = 47122L + day

    // sprintf(in,"%ld",fd); strcat(in,","); sprintf(tmp2,"%ld",timex);
    // strcat(in,tmp2);  (h007/h008 fold: one assembly, one copy-in)
    val s = fd.toString() + "," + timex.toString()
    require(s.length < inBuf.size) { "horolog out-buffer too small" }
    for (i in s.indices) inBuf[i] = s[i]
    inBuf[s.length] = 0.toChar()
}
