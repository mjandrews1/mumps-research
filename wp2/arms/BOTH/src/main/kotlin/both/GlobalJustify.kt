package both

// Unit 278 (BOTH B10, simple): libmpscpp.cpp::global::Justify
// (18 LOC). Batch_plan ordinal 278; units.csv row 308. The
// global-class member face of the justify engine: retrieve the
// OWN global's value, format the two counters decimal, and run
// the unit-207 _justify engine (the interp-sym.cpp body seated
// at Justify.kt) over the value with the counter strings.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: gtmp/tmp are the C byte-domain
//     working buffers; the retrieve folds to the mglobalRetrieve
//     String seat and the engine call carries CharArray copies
//     (copy-in/cStringOf folds, the Piece precedent).
//   h008 static_scratch - reused: cc1/cc2 are LOCAL 32-byte
//     scratch (not static); the two sprintf writes fold into
//     the counter strings passed to the engine (no caller
//     aliases them; behavior-identical).
//   h009 format_string - reused: "%d" is a plain decimal;
//     Kotlin Int.toString gives the same digit shape.
//   h014 out_of_scope_call - reused: the Mglobal RETRIEVE and
//     the GlobalExceptionCode/MumpsGlobalException fail-throw
//     idiom ride the mglobalRetrieve seat (BothCRuntime); the
//     _justify engine rides its unit-207 seat (Justify.kt).
//   h017 naming - reused: the member face rides the GlobalRef
//     receiver as globalJustify (the globalAvg precedent);
//     cc1/cc2/c1/c2/f keep vendor names.
//   h019 class_member_carrier - reused: member of the global
//     class (layout owner globalClass.h::<misc>, roster 89,
//     untranslated in this arm); the receiver state passes
//     explicitly as the GlobalRef ref key (the standing
//     receiver-state pattern).
//   Dafny: exempt (Mglobal retrieval side effect; the engine
//     body is the unit-207 family exemption).
//
// Vendor quirks kept (ledgered):
//   - the fail-throw fires BEFORE any counter normalization
//     (kept first; the empty-ref case is unreachable at this
//     seat per the globalRef contract).
//   - the vendor sprintf's the counters into 32-byte buffers
//     (unbounded "%d" writes); the fold to Int.toString is
//     bounded by construction, divergence noted and benign.

// mstring global::Justify(int c1, int c2) - libmpscpp.cpp
internal fun GlobalRef.globalJustify(c1: Int, c2: Int): String {
    val gtmp = CharArray(STR_MAX) // unsigned char gtmp[STR_MAX]
    val tmp = CharArray(STR_MAX) // char tmp[STR_MAX]

    val f = Mglobal(RETRIEVE, ref.toCharArray(), gtmp, svPtr)

    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // sprintf(cc1,"%d",c1); sprintf(cc2,"%d",c2);
    //   (h008 fold: the local scratch collapses into the
    //   counter strings the engine call carries)
    val cc1 = c1.toString()
    val cc2 = c2.toString()

    // _justify(tmp, gtmp, cc1, cc2);
    _justify(tmp, gtmp, cc1.toCharArray(), cc2.toCharArray())

    // mstring tmp1 = tmp; return tmp1;
    return cStringOf(tmp)
}
