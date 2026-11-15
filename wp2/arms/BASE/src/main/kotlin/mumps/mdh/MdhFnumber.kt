package mumps.mdh

import mumps.interp.MsvState

/**
 * Kotlin translation of libmpscpp.cpp::mstring::Fnumber (Study Subset
 * unit 42, Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: mstring mstring::Fnumber(mstring a, mstring b)
 * (libmpscpp.cpp:7483-7509) and mstring mstring::Fnumber(mstring a)
 * (7512-7531) — $FNUMBER on an mstring: assemble the function-call
 * text with the coded delimiter bytes, hand it to the interpreter's
 * fcn() via the workspace &svPtr->v1d[1], and read the result from
 * &svPtr->bd[1].
 *
 * MSTRING MODELING: Kotlin has no mstring class; the C++ receiver
 * (this->data) and arguments become explicit String parameters, and
 * the two C++ overloads become two Kotlin overloads. The two-arg
 * version's "optional second argument" is the C `if (b.c_str()==0)`
 * check on a default-constructed mstring — modeled as a nullable
 * String? parameter (heuristic 44 family: optional-arg -> String?).
 * C comment typo "second arge optional" noted verbatim here.
 *
 * SENTINEL BYTES: CodedOpen=206 / CodedClose=207 / CodedComma=208
 * (defines.h:38-40) are appended programmatically as Char(nn) — never
 * as escape literals (heuristics 43/80). The "$fnumber" literal's
 * dollar is escaped (heuristic 89).
 *
 * WORKSPACES: svPtr->v1d and svPtr->bd are MsvState StringBuilders
 * modeling the C arrays at offset 1 (sentinel byte at [0]; unit-30
 * convention); strcpy(&v1d[1],tmp) -> clear+append; rslt=&bd[1] ->
 * bd.toString(). fcn(MSV*) (libmpscpp.h:431, void) is a TODO stub —
 * body outside batch scope. svPtr from MdhRuntime (unit-8 owner).
 */
const val CODED_OPEN = 206   // C: defines.h:38 #define CodedOpen 206
const val CODED_CLOSE = 207  // C: defines.h:39 #define CodedClose 207
const val CODED_COMMA = 208  // C: defines.h:40 #define CodedComma 208

/** C: void fcn(MSV *) (include/mumpsc/libmpscpp.h:431) — interpreter
 *  function-call evaluator; body outside batch scope. */
fun fcn(svPtr: MsvState): Unit =
    TODO("fcn not yet translated (lives in the interpreter core, outside batch scope)")

/** C: mstring mstring::Fnumber(mstring a, mstring b) — two-arg, b optional */
fun fnumber(data: String, a: String, b: String?): String {
    val svPtr = MdhRuntime.svPtr

    // C: strcpy(tmp,"$fnumber"); strcat Open, data, Comma, a; then
    //    if (b.c_str()==0) strcat Close; else Comma, b, Close.
    val sb = StringBuilder("\$fnumber")
    sb.append(Char(CODED_OPEN))
    sb.append(data)
    sb.append(Char(CODED_COMMA))
    sb.append(a)
    if (b == null) {
        sb.append(Char(CODED_CLOSE))
    } else {
        sb.append(Char(CODED_COMMA))
        sb.append(b)
        sb.append(Char(CODED_CLOSE))
    }

    // C: strcpy((char *) &svPtr->v1d[1],tmp);
    svPtr.v1d.setLength(0)
    svPtr.v1d.append(sb)

    fcn(svPtr)

    // C: rslt = (char *) &svPtr->bd[1]; return rslt;
    return svPtr.bd.toString()
}

/** C: mstring mstring::Fnumber(mstring a) — one-arg, always closes */
fun fnumber(data: String, a: String): String {
    val svPtr = MdhRuntime.svPtr

    // C: strcpy(tmp,"$fnumber"); strcat Open, data, Comma, a, Close.
    val sb = StringBuilder("\$fnumber")
    sb.append(Char(CODED_OPEN))
    sb.append(data)
    sb.append(Char(CODED_COMMA))
    sb.append(a)
    sb.append(Char(CODED_CLOSE))

    svPtr.v1d.setLength(0)
    svPtr.v1d.append(sb)

    fcn(svPtr)

    return svPtr.bd.toString()
}
