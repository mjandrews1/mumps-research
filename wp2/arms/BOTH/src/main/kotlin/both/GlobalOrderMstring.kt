package both

// Unit 251 (BOTH B9, simple): libmpscpp.cpp::GlobalOrder (15 LOC).
// Batch_plan ordinal 251; units.csv row 273. The \\u0024order
// mstring face: build the "s %=\\u0024order(...)" xecute envelope
// over the mstring argument, run it, and hand back the %
// variable through SymGet; the int parameter i is unused in the
// vendor body (kept, h013 note). The unsigned-char overload
// family (include/mumpsc/globalOrder.h::GlobalOrder, unit 259
// this batch) is a distinct face and renders separately.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: char tmp[STR_MAX] literal +
//     strcats fold onto the unbounded String build (the GlobalQS
//     quantization precedent, no wraparound possible); the
//     mstring rr return folds to String (class owner
//     mstringClass.h::<misc> roster 149, not yet in this arm).
//   h013 dead_store - hit: the int i parameter is never read in
//     the vendor body; kept in the signature for the vendor call
//     shape, noted here.
//   h014 out_of_scope_call - reused: xecute and SymGet ride the
//     BothCRuntime seats (non-unit provenance, see there);
//     svPtr is the passed Msv carrier.
//   h017 naming - reused: GlobalOrder/SymGet/xecute keep vendor
//     case; tmp/rr keep vendor names.
//   Dafny: exempt (xecute interpreter side effect; no decidable
//     core).
//
// Quirks kept (ledgered):
//   - the vendor strcat chain has no bounds check on
//     tmp[STR_MAX]; overflow is silent C UB, quantized by the
//     String build (GlobalQS precedent).

internal fun GlobalOrder(a: String, i: Int, sv: Msv): String {
    // char tmp[STR_MAX]="s %=\\u0024order(" + strcat(a.c_str()) + ")" (h007 fold)
    val tmp = "s %=\u0024order(" + a + ")"

    return if (xecute(tmp) == 0) {
        sv.tpx = 1
        SymGet("%") // rr = SymGet((char *) "%")  (mstring -> String, h007)
    } else {
        sv.tpx = 0
        "" // rr = ""
    }
}
