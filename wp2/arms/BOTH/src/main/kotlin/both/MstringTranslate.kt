package both

// Unit 210 render: libmpscpp.cpp::mstring::Translate (roster
// 210, batch 7; vendor body in the mstring-class member
// family). The \u0024translate re-invoker on the mstring
// receiver: builds the \u0024translate expression over the
// 0xCE/0xD0/0xCF coded delimiters with the receiver data and
// the two arguments, seats it at v1d[1], runs fcn, reads the
// result from bd[1] - the Fnumber/Soundex shape, mstring arm.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: fcn TODO stub direct
//     (owner interp-fcn.cpp::fcn, the Fnumber-render pin
//     note); fnCpyAtZb/fnBdStrAt1Zb internal helpers direct
//     (the roster-209 widen).
//   h015 grammar-collision - reused: ASCII only in source (the
//     0xCE/0xD0/0xCF bytes ride escapes), no backticks, no raw
//     comment dollars, trailing newline.
//   h017 naming - reused: the mstring member renders as the
//     String extension mstringTranslate (the mstringLessEqual
//     precedent; the mstring-class layout owner roster 149 has
//     not landed - class_member_carrier, T1 recurrence).
//   h019 class_member_carrier - reused: the data member is the
//     receiver's own content (the String extension receiver);
//     the TU-static svPtr is the BothCRuntime singleton.
//
// Quirks kept (ledgered):
//   - the 0xD0 mid-argument delimiter joins the 0xCE/0xCF
//     envelope (the zBlanks family bytes; the Soundex
//     precedent).
//   - the concat chain folds to string concatenation; the
//     receiver data rides UNCANONICALIZED (exact).

// mstring mstring::Translate(mstring a1, mstring a2) -
// libmpscpp.cpp (member)
internal fun String.mstringTranslate(a1: String, a2: String): String {
    // m = "$translate" .Concat("\xce") .Concat(data)
    //     .Concat("\xd0") .Concat(a1) .Concat("\xd0")
    //     .Concat(a2) .Concat("\xcf")
    val m = "\u0024translate" + '\u00CE' + this + '\u00D0' +
        a1 + '\u00D0' + a2 + '\u00CF'

    fnCpyAtZb(svPtr.v1d, 1, m) // strcpy(&v1d[1], m.data)
    fcn(svPtr)
    return fnBdStrAt1Zb(svPtr.bd) // m = (char *) &bd[1]
}
