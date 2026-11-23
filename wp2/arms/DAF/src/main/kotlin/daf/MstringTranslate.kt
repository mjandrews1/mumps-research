package daf

// Unit 191 (DAF B7 T1, simple): libmpscpp.cpp::mstring::Translate (15
// LOC, score 7.7): the C++ mstring-class Translate member: builds the
// \$translate service message over the receiver's data plus the two
// pattern args, carries it into the v1d[1] offset, relays through
// fcn(svPtr) and reads the bd[1] result back. Row-101 fold: no
// Kotlin mstring class - the this->data receiver and the two mstring
// args become explicit String params (the unit-101 mstring::Fnumber
// precedent; the Concat chain folds onto a StringBuilder).
// Consults (2026-11-23T18:32:30Z, nominal): BASE-log row 101
// hit-reuse (receiver/args -> String params; the v1d[1] carriage of
// the coded service call - message built at offset 1, read from
// there), row 30 hit-adapt (the mstring m local + Concat chain folds
// onto the String builder; the strcpy into v1d[1] is the
// copy-to-carrier fold; bd[1] NUL-terminated out-read via
// cstringAt), the rows 97/98 family hit-reuse (the fcn service relay
// resolves to the T3 Fcn.kt dafFcn body - the seat flip closed in
// DAF B4 T3), row 54 family hit-reuse (the coded separator bytes
// ride \u escapes per the GlobalTranslate unit-131 precedent; the
// receive-side explicit receiver is the mstring-this fold noted
// above). Envelope kept byte-exact: \$translate, \xce, data,
// \xd0, a1, \xd0, a2, \xcf - the SECOND \xd0 (after a1) is the
// three-arg Translate distinguishing feature vs the unit-131
// two-arg global::Translate envelope; both are kept verbatim
// (same-file family ledger note).
// Dafny: EXEMPT (service-call shaping over the fcn dispatcher seat;
// journal row).

private const val MT_CE_D: Char = '\u00CE' // vendor Concat("\xce")
private const val MT_D0_D: Char = '\u00D0' // vendor Concat("\xd0")
private const val MT_CF_D: Char = '\u00CF' // vendor Concat("\xcf")

fun mstringTranslateD(data: String, a1: String, a2: String, sv: DafMsv): String {

    // m = "\$translate"; m + "\xce" + data + "\xd0" + a1 + "\xd0"
    // + a2 + "\xcf"
    val m = StringBuilder("\$translate")
    m.append(MT_CE_D)
    m.append(data)
    m.append(MT_D0_D)
    m.append(a1)
    m.append(MT_D0_D)
    m.append(a2)
    m.append(MT_CF_D)

    // strcpy(&v1d[1], m): the row-101 offset-1 carriage
    var q = 0
    while (q < m.length && q + 1 < sv.v1d.size) {
        sv.v1d[q + 1] = m[q]; q++
    }
    sv.v1d[q + 1] = NUL_D

    dafFcn(sv) // fcn(svPtr) - the T3 seat resolution

    return DafCRuntime.cstringAt(sv.bd, 1) // m = (char *)&bd[1]
}
