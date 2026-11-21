package daf

// Unit 97 (DAF B4 T1): libmpscpp.cpp::mstring::Fnumber (17 LOC): the
// mstring member that assembles the \$fnumber service call and relays
// it through fcn(svPtr). BASE-log row 101 consulted (hit-adapt):
// no Kotlin mstring class - the C++ receiver (this->data) and the
// argument become String params; CodedOpen/CodedClose/CodedComma are
// appended programmatically as the 206/207/208 code points (row 101
// anchor values); the dollar of \$fnumber backslash-escaped per the
// binding pre-push scan discipline (section 0.4). The v1d[0] sentinel
// convention (strcpy at offset 1) kept on the DAF CharArray carrier
// (row 101 v1d modeling adapted: DAF keeps the flat CharArray).
// fcn is the roster unit interp-fcn.cpp::fcn (unit 123, the closing
// complex row of THIS batch) - named-owner stub until that render.
// Dafny: EXEMPT (MSV state-machine relay; journal row).

const val CODED_OPEN_D: Char = '\u00CE'   // 206 CodedOpen
const val CODED_COMMA_D: Char = '\u00CF'  // 207 CodedComma
const val CODED_CLOSE_D: Char = '\u00D0'  // 208 CodedClose

fun mstringFnumber(data: String, a: String): String {

    val cmd = StringBuilder()
    cmd.append("\$fnumber")
    cmd.append(CODED_OPEN_D)
    cmd.append(data) // invoking mstring (C++ receiver)
    cmd.append(CODED_COMMA_D)
    cmd.append(a) // first argument required
    cmd.append(CODED_CLOSE_D)

    val sv = DafInterpState.svPtr
    var k = 0
    while (k < cmd.length) { // strcpy(&sv.v1d[1], tmp)
        sv.v1d[1 + k] = cmd[k]
        k++
    }
    sv.v1d[1 + k] = NUL_D

    dafFcn(sv)

    return DafCRuntime.cstringAt(sv.bd, 1) // rslt = &sv.bd[1]
}

// struct MSV *fcn(struct MSV *) - owner interp-fcn.cpp::fcn (roster
// batch-4 unit 123; named-owner stub until that render lands).
fun dafFcn(sv: DafMsv): Unit =
    throw NotImplementedError(
        "fcn: out-of-scope owner (interp-fcn.cpp::fcn, roster batch-4 unit 123)")
