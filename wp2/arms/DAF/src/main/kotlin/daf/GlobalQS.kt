package daf

// Unit 145 (DAF B5 T2, core): libmpscpp.cpp::GlobalQS
// (units.csv row 145: loc 19, ptr 3, ctrl 1, score 11.3, rank 112,
// core): the \u0024qs built-in wrapper: the coded service string
// "s %=\u0024qs(<a>,<c>)" is built into the tmp carrier, xecuted,
// and on the success arm the % symbol's value is fetched with
// SymGet and copied into b; on the failure arm b gets the empty
// string and tpx clears.
// Consults: BASE-log row 98 hit-reuse (the globalOrder/xecute
// overload collapse family of that row: xecute rides a named-
// owner stub seat here - the three C++ overloads (string/mstring/
// char*) collapse onto the String seat per the row-98 collapse
// precedent), row 36 hit-reuse (the literal dollar of the coded
// Mumps text is the escaped \u0024 of the Kotlin string - the
// H-015 family escape rule), row 92 hit-reuse (SymGet: the row-92
// retrieve-then-throw seat of LibmpscppMisc9.kt SymGetD - the
// % symbol's value fetch binds the existing arm seat), row 27
// family hit-adapt (the static char r[STR_MAX] and the char
// tmp[STR_MAX] carriers of this TU fold onto the method-local
// StringBuilder/CharArray per the static-local precedent).
// Manual-decision (logged): the tpx result flags live on the DafMsv
// state (sv.tpx), matching the vendor writes through svPtr; b
// stays a CharArray out-carrier (the C unsigned char* b write).
// Dafny: EXEMPT (coded-service relay over stub seats; journal
// row).

// RELEASED at B10 T3 (row 280): the real xecuteDaf body renders in
// LibmpscppMisc8.kt (units.csv row 280 = libmpscpp.cpp::<misc-8>,
// the stream/relay chunk carrying the xecute triple; the b4 blame
// "not a corpus unit" was wrong - the vendor :7073 seat is a
// row-280 cell). This stub deleted; signature unchanged.

fun GlobalQSD(a: String, b: CharArray, c: String, svPtr: DafMsv) {

    var t: String
    val tmp = StringBuilder("s %=\u0024qs(") // char tmp[STR_MAX]="s %=$qs(";
    tmp.append(a) // strcat(tmp, a)
    tmp.append(",") // strcat(tmp, ",")
    tmp.append(c) // strcat(tmp, c)
    tmp.append(")")

    if (xecuteDaf(tmp.toString()) == 0) { // if (xecute(tmp)==0)
        svPtr.tpx = 1
        t = SymGetD("%") // t = SymGet("%");
        var i = 0
        while (i < t.length) { // strcpy((char*)b, t.c_str());
            b[i] = t[i]
            i++
        }
        b[i] = NUL_D
    } else {
        svPtr.tpx = 0
        b[0] = NUL_D // strcpy((char*)b, "");
        t = "" // (the vendor's unused-in-this-arm local)
    }
    // (kept verbatim quirk: the vendor declares the mstring t and
    // the static char r[STR_MAX]; r is never used by this body and
    // is ledgered here rather than declared.)
}
