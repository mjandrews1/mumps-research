package daf

// Unit 192 (DAF B7 T1, simple): libmpscpp.cpp::GlobalData (17 LOC,
// score 8.7): the MSTRING-needle overload of the \$data intrinsic
// wrapper family (vendor libmpscpp.cpp:2616). Same-file family
// adjudication (the family the DOC arm already ledgered): the
// char*-needle form is the batch-3 row (unit 70 there; outside this
// arm's Latin-square batches B4-B6 cycle - the DAF char* position
// never opened), the string-needle form is the batch-8 row; this
// batch-7 row is the mstring form at :2616, physically verified
// this session (dump loc 17). Body: the s-percent-equals-dollar-data
// command build (H-015: the dollar byte rides the Kotlin unicode
// escape), the xecute execution gate with tpx 1/0 by outcome, the
// SymGet("%") read of the data value, the atoi tail on the result;
// -1 on execution failure. The vendor's mstring t local + i are
// plain locals here (row-101 fold: receiver-less C++ locals ride
// Kotlin locals).
// Consults (2026-11-23T18:33:00Z, nominal): BASE-log row 92
// hit-reuse (the execution-outcome pattern shape; the
// xecute-then-read tail of the GlobalQS row-92 family), rows 97/98
// hit-reuse (the xecuteDaf and SymGetD seats already live in-arm -
// xecute at GlobalQS.kt (TU service, not a corpus unit), SymGet at
// LibmpscppMisc9.kt; this render is the first in-arm caller of the
// xecuteDaf seat), row 28 hit-reuse (atoi -> the DafCRuntimeT2
// CRuntime seat), row 33 hit-adapt (the char tmp[STR_MAX] command
// buffer folds onto the String build; the strcat appends fold onto
// String +=), row 54 hit-reuse (the a.c_str() needle is the mstring
// str carrier - String param per the row-101 fold).
// Dafny: EXEMPT (command-string build over service seats; journal
// row).

fun globalDataD(a: String, sv: DafMsv): Int {

    // char tmp[STR_MAX] = "s %=$data(" ... strcat(tmp, a) ... ")"
    var tmp = "s %=\u0024data("
    tmp += a
    tmp += ")"

    val i: Int
    if (xecuteDaf(tmp) == 0) { // if (xecute(tmp)==0)
        sv.tpx = 1
        val t = SymGetD("%") // t = SymGet((char *) "%");
        i = atoi(t)          // i = atoi(t.c_str());
    } else {
        sv.tpx = 0
        i = -1
    }

    return i
}
