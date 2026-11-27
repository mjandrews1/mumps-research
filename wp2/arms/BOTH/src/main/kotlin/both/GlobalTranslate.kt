package both

// Unit 255 (BOTH B9, simple): libmpscpp.cpp::global::Translate
// (21 LOC). Batch_plan ordinal 255; units.csv row 269. The
// global-class member \\u0024translate face (TWO-ARG; the B7
// mstringTranslate two-arg and the B8 one-arg mstringTranslate1
// are the distinct mstring faces): retrieve the global at ref,
// then build the compiled \\u0024translate envelope over the
// retrieved data and both arguments, run fcn, read bd.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: char gtmp[STR_MAX] is the
//     retrieve scratch (CharArray, cStringOf read); the
//     mstring Concat chain folds onto the String envelope
//     build (the mstringTranslate1 precedent).
//   h014 out_of_scope_call - reused: Mglobal RETRIEVE, the
//     GlobalExceptionCode/MumpsGlobalException fail-throw
//     idiom, fcn, fnCpyAtZb/fnBdStrAt1Zb, and the file-scope
//     svPtr singleton all ride their existing arm seats
//     (BothCRuntime, Fnumber, LibmpscppMisc7).
//   h017 naming - reused: the member face rides the GlobalRef
//     receiver as globalTranslate (the reverseG/Qsubscript
//     global-face precedent); a1/a2/m/f/gtmp keep vendor names.
//   MISS class_member_carrier - reused: the global-class member
//     unit renders as a package-level receiver-extension (the
//     T1 units 129/131/133 precedent; the class layout owner
//     roster 149 is not yet in this arm).
//   Dafny: exempt (fcn interpreter side effect).
//
// Quirks kept (ledgered):
//   - the envelope byte ladder \\xce / \\xd0 / \\xd0 / \\xcf
//     (206/208/208/207) rides the compiled Z-literal seat
//     verbatim ('\\u00CE', '\\u00D0', '\\u00CF').
//   - Mglobal writes the retrieved value into gtmp with no
//     length guard; the CharArray(STR_MAX) scratch keeps the
//     vendor capacity contract (overflow UB quantized by the
//     seat, the concatG precedent).

internal fun GlobalRef.globalTranslate(a1: String, a2: String): String {
    // char gtmp[STR_MAX]; int f = Mglobal(RETRIEVE, ref, gtmp, svPtr);
    val gtmp = CharArray(STR_MAX)
    val f = Mglobal(RETRIEVE, ref.toCharArray(), gtmp, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // m = "\\u0024translate" .Concat("\\xce") .Concat(gtmp)
    //     .Concat("\\xd0") .Concat(a1) .Concat("\\xd0") .Concat(a2)
    //     .Concat("\\xcf")   (h007 fold)
    val m = "\u0024translate" + '\u00CE' + cStringOf(gtmp) + '\u00D0' +
        a1 + '\u00D0' + a2 + '\u00CF'

    // strcpy((char *) &svPtr->v1d[1], m.data.c_str()); fcn(svPtr);
    fnCpyAtZb(svPtr.v1d, 1, m)
    fcn(svPtr)

    // m = (char *) &svPtr->bd[1]; return m;
    return fnBdStrAt1Zb(svPtr.bd)
}
