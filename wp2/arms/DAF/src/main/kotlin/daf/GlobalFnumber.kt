package daf

// Unit 196 (DAF B7 T2, core): libmpscpp.cpp::global::Fnumber
// (28 LOC, score 12.6): the global-class Fnumber member, twin of the
// unit-97 mstring::Fnumber render of B4 T1: retrieve the receiver ref,
// then assemble the \\u0024fnumber service envelope (Open/Comma/Close coded
// bytes) over the retrieved value plus the required first pattern and
// the OPTIONAL second pattern (the vendor's b.c_str()==0 gate is the
// mstring NULL-data probe), carry at v1d[1], relay via fcn, read
// bd[1] back as the mstring result.
// Consults (2026-11-24T16:10:00Z, nominal): BASE-log row 101
// hit-reuse (receiver/args -> String params; the envelope ladder and
// the v1d offset-1 carriage of the unit-97 precedent; the optional
// second arg rides a null default String param modeling the vendor
// NULL mstring - the row-101 C++ default-arg fold), row 92
// hit-reuse (the Mglobal(RETRIEVE) retrieve-then-throw with
// GLOBAL_NOT_FOUND), row 54 hit-reuse (the node receiver ref String
// through the name carrier), rows 97/98 family hit-reuse (the dafFcn
// T3 body relay), row 33 hit-adapt (the char tmp[STR_MAX] command
// build folds onto the StringBuilder of the unit-97 shape).
// Dafny: EXEMPT (service call over the dispatcher seat; journal row).

fun globalFnumberD(n: MpsGlobalNode, a: String, b: String?, sv: DafMsv): String {

    val gtmp = CharArray(STR_MAX_DAF)

    val refCarriage = gfName(n.ref)
    val f = MglobalDaf(RETRIEVE_D, refCarriage, gtmp, sv)
    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Fnumber: global not found")
    }

    // strcpy(tmp,"\\u0024fnumber"); Open; gtmp; Comma; a; - optional arm
    val tmp = StringBuilder("\\u0024fnumber")
    tmp.append(CODED_OPEN_D)
    tmp.append(DafCRuntime.cstring(gtmp)) // invoking value
    tmp.append(CODED_COMMA_D)
    tmp.append(a)                          // first argument required
    if (b == null) {
        // if (b.c_str()==0) strcat(tmp,(char *) Close);
        tmp.append(CODED_CLOSE_D)
    } else {
        tmp.append(CODED_COMMA_D)
        tmp.append(b)
        tmp.append(CODED_CLOSE_D)
    }

    // strcpy((char *) &sv.v1d[1], tmp);
    var k = 0
    while (k < tmp.length && k + 1 < sv.v1d.size) {
        sv.v1d[k + 1] = tmp[k]
        k++
    }
    sv.v1d[k + 1] = NUL_D

    dafFcn(sv) // fcn(svPtr) - the T3 seat resolution

    return DafCRuntime.cstringAt(sv.bd, 1) // rslt = (char *) &sv.bd[1]
}

private fun gfName(src: String): CharArray {
    val out = CharArray(STR_MAX_DAF)
    val s = src.toCharArray()
    var i = 0
    while (i < s.size) {
        out[i] = s[i]
        i++
    }
    out[i] = NUL_D
    return out
}
