package both

// Unit 280 (BOTH B10, simple): libmpscpp.cpp::global::Pattern
// (15 LOC). Batch_plan ordinal 280; units.csv row 306. The
// global-needle overload of the pattern-match member face:
// retrieve the OWN global's value and the pattern global p's
// value, then run the pm engine over the pair. The Pattern
// overload set (units.csv duplicate-id ledger, the file-stable
// score sort): the 15-LOC score-5.1 rank-12 row (THIS seat) is
// the global-needle overload at libmpscpp.cpp:7121; the 9-LOC
// string/mstring twins (:7142/:7156) and the 6-LOC char twin
// (:7169) are non-roster overloads (the same-body fold
// adjudicates them to this seat, the Sim1W standing rule).
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: tmp/tmp1 are the retrieve
//     out-buffers; both fold to the mglobalRetrieve String
//     seat (copy-in/cStringOf folds).
//   h014 out_of_scope_call - reused: the Mglobal RETRIEVE and
//     the fail-throw idiom ride the mglobalRetrieve seat; the
//     pm engine rides its permanent seat (owner
//     interp-pm.cpp::pm, libmpscpp.h:237, not a corpus unit -
//     the stub stays seated per the h014 action).
//   h017 naming - reused: the member face rides the GlobalRef
//     receiver as globalPattern (the globalAvg precedent);
//     p/f keep vendor names.
//   h019 class_member_carrier - reused: member of the global
//     class (layout owner globalClass.h::<misc>, roster 89,
//     untranslated in this arm); the receiver state passes
//     explicitly as the GlobalRef ref key.
//   Dafny: exempt (Mglobal retrieval side effect; the pm
//     engine is the stub-seat exemption).

// int global::Pattern(global p) - libmpscpp.cpp:7121
internal fun GlobalRef.globalPattern(p: GlobalRef): Int {
    // unsigned char tmp[STR_MAX]; Mglobal(RETRIEVE, ref, tmp)
    val tmp = mglobalRetrieve(ref)

    // unsigned char tmp1[STR_MAX]; Mglobal(RETRIEVE, p.ref, tmp1)
    val tmp1 = mglobalRetrieve(p.ref)

    // return pm(tmp, tmp1, svPtr);
    return pm(tmp.toCharArray(), tmp1.toCharArray(), svPtr)
}
