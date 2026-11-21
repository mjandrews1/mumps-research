package both

// Unit 134 (BOTH B5 T2, core): libmpscpp.cpp::GlobalQS (19 LOC).
// Batch_plan ordinal 134: core, LOC 19, score 11.3. The \$qs global
// query intrinsic wrapper: build the "s %=\$qs(...)" xecute
// envelope, run it, and hand back the % variable through SymGet.
//
// Vendor behavior: the C buffer tmp is initialized with the literal
// "s %=\$qs(" then receives arg a, ",", arg c, ")" through four
// strcats; if xecute(tmp) returns 0 the envelope succeeded, tpx=1 and
// b receives SymGet("%"); otherwise tpx=0 and b receives "".
//
// Quirks kept (ledgered):
//   - the vendor strcats into char tmp[STR_MAX] with no bounds check;
//     overflow is silent UB in C. The Kotlin fold is an unbounded
//     String build, which is the deterministic quantization of the
//     overflow seat (no wraparound possible).
//   - static char r[STR_MAX] is dead (never read or written past
//     declaration): dropped under h013, never fixed by initializing.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: the strcat chain folds onto a String
//     build; the mstring t return folds to String (its class owner,
//     mstringClass.h::<misc> roster 149, not yet in this arm).
//   h013 dead_store - hit: r dropped with comment.
//   h014 out_of_scope_call - reused: xecute and SymGet ride the
//     BothCRuntime TODO stubs (non-unit provenance, see there).
//   h017 naming - hit: GlobalQS and SymGet keep vendor case.
//   MISS class_member_carrier: the mstring t seat (recurrence from
//     T1 units 129/131/133; T2 +1).
internal fun GlobalQS(a: String, b: CharArray, c: String, sv: Msv) {
    // static char r[STR_MAX]; - dead store (h013), dropped.

    // char tmp[STR_MAX]="s %=\$qs(" + strcat a, ",", c, ")" (h007 fold)
    val tmp = "s %=\$qs(" + a + "," + c + ")"

    if (xecute(tmp) == 0) {
        sv.tpx = 1
        val t = SymGet("%") // mstring -> String (h007)
        writeC(b, t) // strcpy((char *)b, t.c_str())
    } else {
        sv.tpx = 0
        writeC(b, "") // strcpy((char *)b,"")
    }
}
