package doc

// Unit 72 (B3 tranche 1): libmpscpp.cpp::global::Fnumber (batch 3,
// simple stratum; LOC 23, score 9.9) - the ONE-argument global member
// (mstring a), rendered as a GlobalRef extension. Overload notes: the
// two-argument global wrapper, the mstring member pair (unit 42
// rendered the two-mstring form, Fnumber.kt) and the declared global
// two-arg wrapper are separate corpus rows. Retrieve the global's
// value with Mglobal(RETRIEVE=0) and throw MumpsGlobalException on
// GLOBAL_NOT_FOUND=2; the coded call builds with the CodedOpen/
// CodedComma/CodedClose bytes (defines.h:38-40, carried as Char
// arrays per H-012) and the dollar byte rebuilt programmatically per
// H-015 (unit-42 DOLLAR precedent); the dispatch rides the fcn
// H-014 stub; the call goes to v1d[1] and the result reads bd[1]
// (offset-1 convention, unit 42 precedent). Unlike the two-mstring
// form there is NO dead null-branch here. Naming per H-017.

private const val DOLLAR_72 = '$'

fun GlobalRef.fnumber(a: Mstring): Mstring {
    val tmp = CharArray(STR_MAX)
    val rslt = Mstring("")
    val open = charArrayOf(CodedOpen.toChar(), NUL)
    val comma = charArrayOf(CodedComma.toChar(), NUL)
    val close = charArrayOf(CodedClose.toChar(), NUL)
    val gtmp = CharArray(STR_MAX)

    val f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    CstringOps.strcpyS(tmp, DOLLAR_72 + "fnumber")
    CstringOps.strcatS(tmp, CstringOps.cstring(open))

    CstringOps.strcatS(tmp, CstringOps.cstring(gtmp)) // invoking value
    CstringOps.strcatS(tmp, CstringOps.cstring(comma))

    CstringOps.strcatS(tmp, a.c_str()) // first argument required
    CstringOps.strcatS(tmp, CstringOps.cstring(close))

    CstringOps.copyAt(Globals.svPtr.v1d, 1, tmp)
    Stubs.fcn(Globals.svPtr)
    val rr = Mstring(CstringOps.cstringAt(Globals.svPtr.bd, 1))
    return rr
}


// Unit 196 (B7 tranche 2): libmpscpp.cpp::global::Fnumber (batch 7,
// core stratum; LOC 28, score 12.6, rank 122; vendor
// libmpscpp.cpp:7415, physically verified this session; nonblank 28
// matches the roster row) - the TWO-mstring-argument member
// (overload family ledger: the one-arg member above is the unit-72
// render; the mstring two-arg member is unit 42 Fnumber.kt; the
// mstring one-arg member is unit 97 - the same-file unit-42/97
// multiply precedent; arity resolves all in-arm call sites).
// Consult H-013 hit-reuse: the vendor branch testing b.c_str()==0
// is unreachable on this carrier (c_str never returns the null
// pointer), so the else arm (Comma + b + Close) executes
// unconditionally - the unit-42 precedent verbatim. Body: the
// same coded fnumber command build as unit 72 (the invoking
// global values feeds the coded call; the name text is the same
// word fnumber here, NOT the missing-umber wrapper spelling of
// unit 111); Mglobal RETRIEVE with the GLOBAL_NOT_FOUND throw
// frame; fcn dispatch at v1d offset 1 in, bd offset 1 out (the
// 131/42 offset-1 convention); the coded delimiters ride the
// CodedOpen/CodedComma/CodedClose byte carriers (defines.h:38-40,
// H-012 charArrayOf seats); the dead rslt pre-construction of
// the vendor initializer is kept as the Mstring("") construction
// note. Naming per H-017.

fun GlobalRef.fnumber(a: Mstring, b: Mstring): Mstring {
    val tmp = CharArray(STR_MAX)
    // vendor: mstring rslt=""; - the pre-dispatch empty construct
    val open = charArrayOf(CodedOpen.toChar(), NUL)
    val comma = charArrayOf(CodedComma.toChar(), NUL)
    val close = charArrayOf(CodedClose.toChar(), NUL)
    val gtmp = CharArray(STR_MAX)

    val f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    CstringOps.strcpyS(tmp, DOLLAR_72 + "fnumber")
    CstringOps.strcatS(tmp, CstringOps.cstring(open))

    CstringOps.strcatS(tmp, CstringOps.cstring(gtmp)) // invoking mstring
    CstringOps.strcatS(tmp, CstringOps.cstring(comma))

    CstringOps.strcatS(tmp, a.c_str()) // first argument required

    // second argument optional in the vendor; the null-branch is
    // dropped per H-013 (unreachable on this carrier), so the else
    // arm executes unconditionally:
    CstringOps.strcatS(tmp, CstringOps.cstring(comma))
    CstringOps.strcatS(tmp, b.c_str())
    CstringOps.strcatS(tmp, CstringOps.cstring(close))

    CstringOps.copyAt(Globals.svPtr.v1d, 1, tmp)
    Stubs.fcn(Globals.svPtr)
    // rslt = (char *) &svPtr->bd[1];
    val rslt = Mstring(CstringOps.cstringAt(Globals.svPtr.bd, 1))
    return rslt
}
