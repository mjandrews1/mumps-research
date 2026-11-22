package both

// Unit 160 (BOTH B6 T1, simple): libmpscpp.cpp::Sum (17 LOC) -
// the FILE-LEVEL Sum (distinct from the member
// libmpscpp.cpp::global::Sum, unit 136 / batch 5, vendor line
// 1620, already rendered as globalSum). Batch_plan ordinal 160:
// simple, LOC 17, score 8.1, complexity rank 70. Vendor line
// 10289; the svPtr and ep parameters are unused in the body
// (dropped per the arm fold, journaled).
//
// Vendor behavior: the arg1 byte string carries a MUMPS global
// reference: byte 0 is the caret, the name follows; a trailing
// pair of the 0x01 coded-subscript marker is stripped
// ("if (i>0 && arg1[i-1]==1 && arg1[i-2]==1) arg1[i-2]=0"); the
// walk then finds the FIRST 0x01 marker (j saves its byte), the
// name is taken past the caret up to that marker
// ("global a(&arg1[1]) // bypass ^"), and the target is bound
// by a second constructor call: with no marker found (j==0) the
// empty-subscript call a("") binds the bare name; with a marker
// found the subscript bytes after it bind over the name
// (a(&arg1[i+1])). MakeGlobal composes the ref as the plain
// concatenation of name and subscript bytes (libmpscpp.cpp:6963,
// strcatx; the quote decoration goes to the Sref twin, not the
// ref key). The member Sum() (unit-136 render) then walks the
// composed target. The double is sprintf("%g")-seated into the
// static char tmp[32] and returned.
//
// Quirks kept (ledgered):
//   - the trailing-pair strip reads arg1[i-2] at i==1 (the C
//     index walks out of bounds); quantized with a length>=2
//     guard (the unit-130/136 Avg precedent), the divergence
//     journaled.
//   - an empty arg1 takes &arg1[1] past the buffer in C (UB);
//     quantized to the empty name (journal), the target then
//     sums the globalSum("") walk verbatim.
//   - MakeGlobal/strcatx are untranslated TUs (not corpus units
//     in the rendered batches): the composition folds to the
//     plain concat (documented divergence risk if a later
//     MakeGlobal render changes the ref key shape; the h014
//     stub ledger keeps the seam visible).
//   - static char tmp[32] folds to a fresh String per the h008
//     unit-154 precedent.
//
// BOTH consults (find_rule before render, events logged):
//   h008 static_local_state - reused and hit (fresh String fold).
//   h009 format_string - reused and hit: sprintf(tmp,"%g",x)
//     rides the fmtG seat (%g default precision 6,
//     BothNumRuntime.kt).
//   h014 out_of_scope_call - reused and hit: the member Sum
//     delegate rides the rendered unit-136 globalSum (the
//     T2 flip precedent); the constructor/operator() family
//     and MakeGlobal are untranslated provenance, folded per
//     the ledger above.
//   h019 class_member_carrier - reused and hit: the global-typed
//     local carrier a (layout owner roster 89, untranslated in
//     this arm) folds to explicit state: name/sub strings with
//     the ref composed per the MakeGlobal ledger.

internal fun Sum(arg1: String): String {
    var s = arg1
    // trailing coded-subscript pair strip (length>=2 guard)
    if (s.length >= 2 && s[s.length - 1] == '\u0001' && s[s.length - 2] == '\u0001') {
        s = s.substring(0, s.length - 2)
    }

    val first = s.indexOf('\u0001')
    val j = if (first == -1) 0 else 1
    val name = if (first == -1) {
        if (s.isEmpty()) "" else s.substring(1) // bypass the caret
    } else {
        s.substring(1, first)
    }
    val sub = if (first == -1) "" else s.substring(first + 1)

    val x = if (j == 0) {
        globalSum(name) // a(""): bare-name target
    } else {
        globalSum(name + sub) // a(&arg1[i+1]): name+subscript ref
    }
    return fmtG(x) // sprintf(tmp,"%g",x) on the fresh fold
}
