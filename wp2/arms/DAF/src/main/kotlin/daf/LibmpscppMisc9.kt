package daf

// Unit 120 (DAF B4 T3, complex): libmpscpp.cpp::<misc-9> (342 LOC): the
// C++ wrapper-cluster residue: the mstring operator* family, the
// mstring operator< / operator> overload sets, SymGet/SymPut/__symput,
// global::Flush, and the bsv char* wrappers that bridge \\$z-sqlite
// service calls onto the global class members (Cosine/TermCorrelate/
// DocCorrelate/Sim1/Jaccard/Dice/StopInit/StopLookup/SynInit/
// SynLookup/Centroid/BMGSearch/Shred/ShredQuery).
// Consults: BASE-log row 41 hit-reuse (the mstring numeric operator
// family: operator* triple resolves through the interp-arith.cpp
// mult() machinery whose reference-build output is the numSetup %.8g
// (row 79) modeled by DafCRuntime.fmt8g, DOCUMENTED DIVERGENCE:
// full numSetup canonicalization approximated by strtod+fmt8g, per the
// T2 mstringAddD/mstringDivD precedent), rows 98/97 hit-reuse (the 8
// operator< and 8 operator> overloads collapse onto the numeric
// atol/atof and lexicographic String families; atol seat atolD below
// rides row 28; atof folds onto strtod), row 92 hit-reuse (SymGet/
// SymPut __symput: sym_(11,...) retrieve-then-throw MumpsGlobalException
// with GlobalExceptionCode=SYMTAB_ERROR, exactly the vendor fold), rows
// 6/22 hit-reuse (the TU svPtr singleton: the wrappers' implicit
// svPtr rides DafInterpState.svPtr as in the T2 renders), row 33
// hit-adapt (the malloc/strcpy/strcat/sprintf %g libc folds; sprintf
// %g -> DafCRuntime.fmtG (row 26/79); malloc'd p1 in BMGSearch folds
// onto a String), row 80 hit-adapt (the (char)1 coded splits of the
// bsv arg1/arg2 in-place writes kept: the wrappers break their arg
// buffers at the first 1-byte and write the terminator in place
// exactly as vendor), row 56 hit-adapt (static char tmp[32] returns
// fold onto fresh Strings; the shared static buffer hazard ledgered),
// row 54 hit-reuse (the global class node model: MpsGlobalNode seats
// carry the wrapper \\$-subscript invokes; the new cosine/
// termCorrelate/docCorrelate/sim1/jaccard/dice member seats raised
// here in GlobalNodeStub.kt are named-owner stubs of the libmpscpp.cpp
// global class callers of this file, riding rows 105/37 seat
// discipline), row 28 (Shred/ShredQuery atoi).
// Seats: symRetDaf (permanent sym_ dispatcher stub), MglobalDaf
// (FLUSH), sigintDaf not used here; free-function seats: StopInit/
// StopLookup/SYN/bmg_fullsearch/Shred/ShredQuery owners named below
// (non-roster families); SynInit was RELEASED at B5 T3 (unit 146:
// the roster body libmpscpp.cpp::SynInit rendered as synInitD in
// SynInit.kt; the selftext.cpp attribution was a tag error);
// SYN tag-corrected alongside (grep-verified libmpscpp.cpp:7788). the bsv
// wrapper names themselves are the corpus bodies of THIS unit, so they
// are rendered as the real functions and the inner C++ free functions
// they call become named-owner stubs.
// Dafny: EXEMPT (wrapper/residue cluster over stub seats; journal row).

// atol(const char*) - numeric fold (BASE-log row 28; C atol: stops at
// the first non-numeric char; no overflow semantics needed by this
// cluster). Returns the C long as Long.
fun atolD(s: String): Long {
    var i = 0
    var neg = false
    while (i < s.length && (s[i] == ' ' || s[i] == '\t')) i++
    if (i < s.length && (s[i] == '+' || s[i] == '-')) {
        neg = s[i] == '-'; i++
    }
    var v = 0L
    while (i < s.length && s[i] in '0'..'9') {
        v = v * 10 + (s[i] - '0'); i++
    }
    return if (neg) -v else v
}

// atof(const char*) - folds onto strtod (row 28/40 family)
fun atofD(s: String): Double = DafCRuntime.strtod(s)

// ---- mstring operator* family (row 41/79: mult() modeled as
// strtod+fmt8g per the T2 mstringAddD DOCUMENTED DIVERGENCE) ----

// mstring operator*(string i, mstring k)
fun mstringMulD(i: String, k: String): String =
    DafCRuntime.fmt8g(DafCRuntime.strtod(i) * DafCRuntime.strtod(k))

// mstring operator*(global i, mstring k): the global operand reads
// through the node retrieve seat; owner: libmpscpp.cpp global class
// operator>> (named-owner stub seat below).
fun mstringMulD(i: MpsGlobalNode, k: String): String =
    DafCRuntime.fmt8g(DafCRuntime.strtod(globalReadD(i)) * DafCRuntime.strtod(k))

// mstring operator*(const char * i, mstring k) - same shape as the
// string overload (row 98 collapse)
fun mstringMulD(i: CharArray, k: String): String =
    mstringMulD(DafCRuntime.cstring(i), k)

// ---- mstring::operator< (8 overloads; row 98 collapse) ----

fun mstringLtD(a: String, i: Int): Boolean = atolD(a) < i      // operator<(int)
fun mstringLtD(a: String, i: Long): Boolean = atolD(a) < i     // operator<(long)
fun mstringLtD(a: String, i: Double): Boolean = atofD(a) < i   // operator<(double)
fun mstringLtD(a: String, i: Float): Boolean = atofD(a) < i    // operator<(float)
fun mstringLtD(a: String, i: CharArray): Boolean = a < DafCRuntime.cstring(i) // operator<(char *)
fun mstringLtD(a: String, i: String): Boolean = a < i          // operator<(string/const char *)
fun mstringLtD(a: String, i: MpsGlobalNode): Boolean =         // operator<(global)
    a < globalReadD(i)
// mstring operator<(mstring i): the mstring operand folds onto its
// String data member (row 98: the mstring/String collapse).
fun mstringLtM(a: String, i: String): Boolean = a < i

// ---- mstring::operator> (8 overloads; row 98 collapse) ----

fun mstringGtD(a: String, i: Int): Boolean = atolD(a) > i
fun mstringGtD(a: String, i: Long): Boolean = atolD(a) > i
fun mstringGtD(a: String, i: Double): Boolean = atofD(a) > i
fun mstringGtD(a: String, i: Float): Boolean = atofD(a) > i
fun mstringGtD(a: String, i: CharArray): Boolean = a > DafCRuntime.cstring(i)
fun mstringGtD(a: String, i: String): Boolean = a > i
fun mstringGtD(a: String, i: MpsGlobalNode): Boolean =
    a > globalReadD(i)
fun mstringGtM(a: String, i: String): Boolean = a > i

// mstring::operator const char *() - fold: the conversion returns the
// data String itself; recorded, no separate carrier needed.

// ---- SymGet/SymPut/__symput (row 92 retrieve-then-throw) ----

// mstring SymGet(mstring/char*/global) - 3 overloads collapse onto the
// String name carrier (row 98); the global overload reads the name
// through the node seat first.
fun SymGetD(str: String): String {
    val tmp = CharArray(STR_MAX_DAF)
    val sv = DafInterpState.svPtr
    val p1 = symRetDaf(11, str.toCharArray(), 0, tmp, 0, sv)
    if (p1 == null) {
        GlobalExceptionCodeD = SYMTAB_ERROR_D
        throw MumpsGlobalExceptionD("SymGet: sym_(11) symbol table error")
    }
    return DafCRuntime.cstring(tmp)
}

fun SymGetD(str: MpsGlobalNode): String = SymGetD(globalReadD(str))

// bool SymPut(name, value) - 9 overloads + __symput collapse onto the
// String pair carrier (row 98); every permutation of name/value kinds
// resolves to __symputD(strings).
fun SymPutD(name: String, value: String): Boolean {
    __symputD(name, value)
    return true
}

fun SymPutD(name: String, value: MpsGlobalNode): Boolean =
    SymPutD(name, globalReadD(value))

fun SymPutD(name: MpsGlobalNode, value: String): Boolean =
    SymPutD(globalReadD(name), value)

fun SymPutD(name: MpsGlobalNode, value: MpsGlobalNode): Boolean =
    SymPutD(globalReadD(name), globalReadD(value))

// void __symput(str, val)
fun __symputD(str: String, valS: String) {
    val sv = DafInterpState.svPtr
    val p1 = symRetDaf(0, str.toCharArray(), 0, valS.toCharArray(), 0, sv)
    if (p1 == null) {
        GlobalExceptionCodeD = SYMTAB_ERROR_D
        throw MumpsGlobalExceptionD("__symput: sym_(0) symbol table error")
    }
}

// ---- global::Flush() ----

fun globalFlushD() {
    val t1 = CharArray(16)
    val t2 = CharArray(16)
    MglobalDaf(FLUSH_D, t1, t2, DafInterpState.svPtr)
}

// ---- bsv char* wrappers (row 80 in-place 1-byte splits preserved) ----

// The shared arg probe: for (i=0; arg[i]!=0; i++) if (arg[i]==1) break;
// then arg[i]=0 terminates the name in place (kept: the render writes
// the terminator into the caller's buffer).
private fun split1(arg: CharArray): Int {
    var i = 0
    while (at(arg, i) != NUL_D) {
        if (arg[i] == FCN_MARK1_D) break
        i++
    }
    arg[i] = NUL_D
    return i
}

// char * Cosine(svptr, ep, arg1, arg2)
fun CosineW(svptr: DafMsv, ep: CharArray, arg1: CharArray, arg2: CharArray): String {
    val i = split1(arg1)
    globalASeat(arg1, arg1, 1) // global a(&arg1[1]) - bypass ^ (ctor seat)
    val j = split1(arg2)
    globalASeat(arg2, arg2, 1) // global b(&arg2[1])
    val x = nodeCosineD(globalASeat(arg1, arg1, i + 1),
                        globalASeat(arg2, arg2, j + 1))
    return DafCRuntime.fmtG(x) // sprintf(tmp, "%g", x)
}

// char * TermCorrelate(svptr, ep, arg1, arg2) - writes into the target
// node tree, returns "" (roster unit: the global member owner rides
// the node seat).
fun TermCorrelateW(svptr: DafMsv, ep: CharArray, arg1: CharArray, arg2: CharArray): String {
    val i = split1(arg1)
    globalASeat(arg1, arg1, 1)
    val j = split1(arg2)
    globalASeat(arg2, arg2, 1)
    NodeASeat(globalASeat(arg1, arg1, i + 1))
        .termCorrelate(NodeASeat(globalASeat(arg2, arg2, j + 1)))
    return ""
}

// char * DocCorrelate(svptr, ep, arg1, arg2, arg3, arg4)
fun DocCorrelateW(svptr: DafMsv, ep: CharArray, arg1: CharArray, arg2: CharArray,
                  arg3: CharArray, arg4: CharArray): String {
    val i = split1(arg1)
    globalASeat(arg1, arg1, 1)
    val j = split1(arg2)
    globalASeat(arg2, arg2, 1)
    val x = DafCRuntime.strtod(DafCRuntime.cstring(arg4)) // sscanf %lf
    docCorrelateD(globalASeat(arg1, arg1, i + 1),
                  globalASeat(arg2, arg2, j + 1),
                  DafCRuntime.cstring(arg3), x)
    return ""
}

// char * Sim1(svptr, ep, arg1, arg2)
fun Sim1W(svptr: DafMsv, ep: CharArray, arg1: CharArray, arg2: CharArray): String {
    val i = split1(arg1)
    globalASeat(arg1, arg1, 1)
    val j = split1(arg2)
    globalASeat(arg2, arg2, 1)
    val x = sim1D(globalASeat(arg1, arg1, i + 1), globalASeat(arg2, arg2, j + 1))
    return DafCRuntime.fmtG(x)
}

// char * Jaccard(svptr, ep, arg1, arg2)
fun JaccardW(svptr: DafMsv, ep: CharArray, arg1: CharArray, arg2: CharArray): String {
    val i = split1(arg1)
    globalASeat(arg1, arg1, 1)
    val j = split1(arg2)
    globalASeat(arg2, arg2, 1)
    val x = jaccardD(globalASeat(arg1, arg1, i + 1), globalASeat(arg2, arg2, j + 1))
    return DafCRuntime.fmtG(x)
}

// char * Dice(svptr, ep, arg1, arg2)
fun DiceW(svptr: DafMsv, ep: CharArray, arg1: CharArray, arg2: CharArray): String {
    val i = split1(arg1)
    globalASeat(arg1, arg1, 1)
    val j = split1(arg2)
    globalASeat(arg2, arg2, 1)
    val x = diceD(globalASeat(arg1, arg1, i + 1), globalASeat(arg2, arg2, j + 1))
    return DafCRuntime.fmtG(x)
}

// char * StopInit(svptr, ep, arg1)
fun StopInitW(svptr: DafMsv, ep: CharArray, arg1: CharArray): String {
    stopInitD(DafCRuntime.cstring(arg1))
    return ""
}

// char * StopLookup(svptr, ep, arg1)
fun StopLookupW(svptr: DafMsv, ep: CharArray, arg1: CharArray): String =
    if (stopLookupD(DafCRuntime.cstring(arg1))) "1" else "0"

// char * SynInit(svptr, ep, arg1)
fun SynInitW(svptr: DafMsv, ep: CharArray, arg1: CharArray): String {
    synInitD(DafCRuntime.cstring(arg1))
    return ""
}

// char * SynLookup(svptr, ep, arg1)
fun SynLookupW(svptr: DafMsv, ep: CharArray, arg1: CharArray): String =
    synD(DafCRuntime.cstring(arg1))

// char * Centroid(svptr, ep, arg1, arg2) - SEAT FLIP: the wrapper
// delegates to the unit-112 T2 render globalCentroid (node, B).
fun CentroidW(svptr: DafMsv, ep: CharArray, arg1: CharArray, arg2: CharArray): String {
    val i = split1(arg1)
    globalASeat(arg1, arg1, 1)
    val j = split1(arg2)
    globalASeat(arg2, arg2, 1)
    globalCentroid(globalASeat(arg1, arg1, i + 1), globalASeat(arg2, arg2, j + 1))
    return ""
}

// char * BMGSearch(svptr, ep, arg1, arg2)
fun BMGSearchW(svptr: DafMsv, ep: CharArray, arg1: CharArray, arg2: CharArray): String {
    // char *p1 = malloc(strlen(arg2)+2); strcpy(p1,arg2); strcat(p1," ")
    // (row 33: heap fold onto String; note the vendor malloc length
    // strlen(arg2+2) is one short of the copy - kept in the ledger,
    // the JVM String append cannot overrun)
    val p1 = DafCRuntime.cstring(arg2) + " "
    val i = bmgFullsearchD(DafCRuntime.cstring(arg1), p1)
    return String.format(java.util.Locale.US, "%d", i) // sprintf(tmp,"%d",i)
}

// char * Shred(svptr, ep, arg1, arg2)
fun ShredW(svptr: DafMsv, ep: CharArray, arg1: CharArray, arg2: CharArray): String {
    val i = atoi(DafCRuntime.cstring(arg2)) // atoi seat (row 28)
    return shredD(DafCRuntime.cstring(arg1), i)
}

// char * ShredQuery(svptr, ep, arg1, arg2)
fun ShredQueryW(svptr: DafMsv, ep: CharArray, arg1: CharArray, arg2: CharArray): String {
    val i = atoi(DafCRuntime.cstring(arg2))
    return shredQueryD(DafCRuntime.cstring(arg1), i)
}

// ---- named-owner seats raised by this unit (not roster units; the
// permanent-stub discipline of rows 37/105 applies) ----

// global operator>> (the str >> t1 read): the node retrieve seat.
private fun globalReadD(n: MpsGlobalNode): String =
    n.value // operator>> reads the node value; the node-model fold

// global ctor global(ref) seat: returns the ref-slice node carrier.
private fun globalASeat(buf: CharArray, unused: CharArray, off: Int): MpsGlobalNode =
    MpsGlobalNode(DafCRuntime.cstringAt(buf, off))

// node holders for the member-call wrappers (ctor-then-subscript
// invoke shapes a(&arg[i+1]) folded onto the two-step seat)
private fun NodeASeat(n: MpsGlobalNode): MpsGlobalNode = n

// global member DocCorrelate(b, arg3, x) - SEAT RELEASED at B5 T3
// (unit 154: the roster body global::DocCorrelate rendered as
// docCorrelateD in LibmpscppMisc5.kt; its free-function relay is
// the named stub docCorrelateFreeD there).

// double Cosine(global,global) free function (selftext family)
fun nodeCosineD(a: MpsGlobalNode, b: MpsGlobalNode): Double =
    throw NotImplementedError(
        "Cosine(global,global): out-of-scope owner (libmpscpp.cpp free-function Cosine; called by the wrapper of roster unit Cosine)")

fun sim1D(a: MpsGlobalNode, b: MpsGlobalNode): Double =
    throw NotImplementedError(
        "Sim1(global,global): out-of-scope owner (libmpscpp.cpp free-function Sim1; called by the wrapper of roster unit Sim1)")

fun jaccardD(a: MpsGlobalNode, b: MpsGlobalNode): Double =
    throw NotImplementedError(
        "Jaccard(global,global): out-of-scope owner (libmpscpp.cpp free-function Jaccard; called by the wrapper of roster unit Jaccard)")

// TAG CORRECTION at B6 T2 (units 166/175): the deleted stopInitD
// stub blamed the owner "libmpscpp.cpp/selftext.cpp StopInit; not
// a roster unit" - WRONG blame unit family, RIGHT release: units.csv
// batch-6 row 166 IS the string overload at libmpscpp.cpp:7729 (the
// real body now renders in StopInit.kt; the StopList stop set
// carrier at 7718). The diceD stub deleted above was correctly
// blamed: roster unit 175 releases it (Dice.kt keeps the two-arg
// wrapper signature the DiceW call uses).
fun stopLookupD(a: String): Boolean =
    throw NotImplementedError(
        "StopLookup(char *): out-of-scope owner (selftext.cpp StopLookup; not a corpus unit; a future body would read the DafStop carrier of the unit-166 StopInit.kt render)")

// TAG CORRECTION at B5 T3 (unit 146): SYN's body is grep-verified at
// libmpscpp.cpp:7788 (not selftext.cpp - the b4-T3 raise mis-tagged
// it); the bsearch-over-syn kin of the unit-146 SynInit render (same
// file-scope DafSyn table). Still NOT a corpus unit; the permanent
// named-owner stub discipline stands.
fun synD(a: String): String =
    throw NotImplementedError(
        "SYN(char *): out-of-scope owner (libmpscpp.cpp SYN (grep-verified :7788); not a roster unit)")

fun bmgFullsearchD(a: String, b: String): Int =
    throw NotImplementedError(
        "bmg_fullsearch: out-of-scope owner (selftext.cpp bmg_fullsearch; not a roster unit)")

fun shredD(a: String, i: Int): String =
    throw NotImplementedError(
        "Shred(char *, int): out-of-scope owner (selftext.cpp Shred; not a roster unit)")

fun shredQueryD(a: String, i: Int): String =
    throw NotImplementedError(
        "ShredQuery(char *, int): out-of-scope owner (selftext.cpp ShredQuery; not a roster unit)")
