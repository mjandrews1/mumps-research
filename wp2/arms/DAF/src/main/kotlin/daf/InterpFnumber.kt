package daf

// Unit 111 (DAF B4 T2, core): interp-fnumber.cpp::_fnumber (17 LOC): the
// $FN[umber] service relay: builds the coded svc command
// "$fn<open>number<comma>arg1<comma>arg2<close>" into v1d[1] and relays
// through fcn(svPtr), returning the bd[1] result.
// Consult: BASE-log row 101 hit-reuse (the coded-command build shape
// of the mstring::Fnumber T1 render: the 206/207/208 code points are
// appended programmatically via the CODED_*_D constants and the "\$"
// escape keeps the command token intact; "$fn" (short form) kept
// verbatim where the T1 unit spells "\$fnumber"); row 81 family (the
// strcpy/strcat chain into v1d[1] renders as the incremental v1d fill
// with the NUL_D terminator, identical to the T1 carrier); row 30
// hit-reuse (the tmp0 out-buffer becomes the String return).
// Seat: fcn is the roster unit interp-fcn.cpp::fcn (unit 123, the
// closing complex row of THIS batch) - the dafFcn stub raised in T1 is
// reused unchanged.
// Dafny: EXEMPT (MSV state-machine relay; journal row).

fun fnumberD(number: String, arg1: String, arg2: String): String {

    val sv = DafInterpState.svPtr

    val cmd = StringBuilder()
    cmd.append("\$fn")
    cmd.append(CODED_OPEN_D)   // strcpy + strcat open
    cmd.append(number)
    cmd.append(CODED_COMMA_D)  // strcat comma
    cmd.append(arg1)
    cmd.append(CODED_COMMA_D)  // strcat comma
    cmd.append(arg2)
    cmd.append(CODED_CLOSE_D)  // strcat close

    var k = 0 // strcpy(&sv.v1d[1], cmd)
    while (k < cmd.length) {
        sv.v1d[1 + k] = cmd[k]
        k++
    }
    sv.v1d[1 + k] = NUL_D

    dafFcn(sv)

    return DafCRuntime.cstringAt(sv.bd, 1) // strcpy(tmp0, &sv.bd[1])
}
