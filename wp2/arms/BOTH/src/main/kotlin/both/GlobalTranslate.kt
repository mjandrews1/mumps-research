package both

// Unit 131 (BOTH B5 T1, simple): libmpscpp.cpp::global::Translate (19
// LOC). Batch_plan ordinal 131: simple, score 9.3.
//
// Vendor behavior: retrieve the OWN global value (fail -> GLOBAL_NOT_
// FOUND + MumpsGlobalException), build the \$translate coded call
// "\$translate" + xce + value + xd0 + a1 + xcf through mstring Concat,
// copy the built string into &svPtr->v1d[1] (byte 0 untouched, the
// coded-string offset convention), invoke fcn(svPtr), then adopt
// &svPtr->bd[1] as the result mstring.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: the mstring `m` local is scratch built
//     by Concat and consumed once (String scan role); gtmp is the
//     retrieve out-buffer role -> String; v1d/bd are SV state bytes,
//     carried on the Msv carrier (roster 172) since the layout owner
//     is not yet translated.
//   h015 grammar_collision - reused: "\$translate" escaped for the
//     Kotlin string template; the vendor escapes \xce/\xd0/\xcf render
//     as decimal char constants (206/208/207).
//   h014 out_of_scope_call - reused: fcn and Mglobal seats name owners
//     (roster units 123 / 167); the mstring-class layout owner is
//     roster 149, the stateVector layout owner roster 172, the
//     exception machinery owner roster 305.
//   h017 naming - reused (a1 keeps its vendor name; snake->camel only
//     for the render name globalTranslate).
//   MISS class_member_carrier - see BothStubs.kt ledger.
//   Dafny: exempt (SV-state carriage through stubbed callee; no
//     decidable core this unit).

internal fun globalTranslate(ref: String, a1: String): String {
    val gtmp = mglobalRetrieve(ref) // own value, fail-throw idiom

    val XCE = 206.toChar()
    val XD0 = 208.toChar()
    val XCF = 207.toChar()

    // C: m = "\$translate"; m = m.Concat(xce) ... (mstring Concat chain
    // -> plain String build; the mstring-class layout owner is roster
    // 149 and lands later in this batch).
    val m = StringBuilder()
    m.append("\$translate")
    m.append(XCE)
    m.append(gtmp)
    m.append(XD0)
    m.append(a1)
    m.append(XCF)

    // C: strcpy((char *) &svPtr->v1d[1], m.data.c_str());
    val payload = m.toString().toCharArray()
    if (payload.size >= STR_MAX) {
        throw IndexOutOfBoundsException("v1d overflow (STR_MAX=" + STR_MAX + ")")
    }
    System.arraycopy(payload, 0, svPtr.v1d, 1, payload.size) // v1d[0] untouched

    fcn(svPtr) // owner: interp-fcn.cpp::fcn (roster 123)

    // C: m = (char *) &svPtr->bd[1]; return m;
    return cStringOf(svPtr.bd.copyOfRange(1, svPtr.bd.size))
}
