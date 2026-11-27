package both

// Unit 205 render: libmpscpp.cpp::global::Fnumber (roster 205,
// batch 7, 34 LOC; units.csv rank 173 region; vendor body in
// the global-class member family). The \u0024fnumber intrinsic
// re-invoker: retrieves the receiver global's value, builds
// the \u0024fnumber(value,a[,b]) expression over the coded
// open/comma/close bytes, seats it at v1d[1], and runs fcn -
// the result reads back from bd[1].
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: Mglobal RETRIEVE direct;
//     fcn TODO stub called direct (owner interp-fcn.cpp::fcn -
//     the BothCRuntime pin says "roster unit 123"; units.csv
//     line 95 gives unit 94 rank 307, another pin-family
//     discrepancy, cosmetic fix queued); GlobalExceptionCode/
//     MumpsGlobalException idiom direct.
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline; the intrinsic
//     name's dollar rides the \u0024 escape (Kotlin string
//     templates would otherwise interpolate).
//   h017 naming - reused: Fnumber keeps vendor name/case as a
//     GlobalRef member (the Int()/Double() precedent); the
//     CodedOpen/CodedComma/CodedClose consts seat locally with
//     the defines.h:38-40 pins (the BifsZfcn ZB_OPENC family
//     precedent - private per-file consts, not re-seated).
//   h019 class_member_carrier - reused: the member rides the
//     GlobalRef receiver; the TU-static svPtr is the
//     BothCRuntime singleton.
//
// Quirks kept (ledgered):
//   - the fail-throw on a missing receiver value (f==0) is
//     BEFORE any expression build (kept first).
//   - b.c_str()==0 (the default-constructed mstring) vs ""
//     (an EMPTY mstring is non-null): the Kotlin String?
//     nullable mirrors the NULL arm only; an empty string
//     still takes the comma arm (exact).
//   - strcpy(&v1d[1],tmp) / rslt=&bd[1]: the offset-1 seats
//     (the established v1d,1/bd,1 offset family); the private
//     helpers below carry the pins.

// defines.h:38-40 (CodedOpen 206, CodedClose 207, CodedComma
// 208; the BifsZfcn ZB_OPENC family precedent)
private const val FN_OPENC = 206.toChar()
private const val FN_CLOSEC = 207.toChar()
private const val FN_COMMAC = 208.toChar()

// strcpy(&dst[off], src) fold. Visibility widened private->
// internal at the roster-209 render (Soundex's &v1d[1] store is
// the same offset copy - ONE helper, TWO consumers).
internal fun fnCpyAtZb(dst: CharArray, off: Int, src: String) {
    var d = off
    for (c in src) {
        dst[d] = c
        d++
    }
    dst[d] = '\u0000'
}

// &bd[1] read fold. Widened private->internal at the roster-209
// render (same TWO-consumer note as fnCpyAtZb).
internal fun fnBdStrAt1Zb(bd: CharArray): String {
    val sb = StringBuilder()
    var i = 1
    while (i < bd.size && bd[i] != '\u0000') {
        sb.append(bd[i])
        i++
    }
    return sb.toString()
}

// mstring global::Fnumber(mstring a, mstring b) - the
// global-class member
internal fun GlobalRef.Fnumber(a: String, b: String?): String {
    val gtmp = CharArray(STR_MAX)

    val f = Mglobal(RETRIEVE, ref.toCharArray(), gtmp, svPtr)

    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    val sb = StringBuilder()
    sb.append("\u0024fnumber")
    sb.append(FN_OPENC)
    sb.append(cStringOf(gtmp)) // invoking mstring
    sb.append(FN_COMMAC)
    sb.append(a) // first argument required
    if (b == null) { // second arge optional
        sb.append(FN_CLOSEC)
    } else {
        sb.append(FN_COMMAC)
        sb.append(b)
        sb.append(FN_CLOSEC)
    }

    fnCpyAtZb(svPtr.v1d, 1, sb.toString()) // strcpy(&v1d[1],tmp)
    fcn(svPtr)
    return fnBdStrAt1Zb(svPtr.bd) // rslt = (char *) &bd[1]
}
