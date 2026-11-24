package daf

// Unit 223 (DAF B8 T1, simple): libmpscpp.cpp::IDF (15 LOC,
// score 6.7): the FREE IDF wrapper (the builtin-function dispatcher
// entry the builtinTable.h roster names at caret-IDF; vendor
// libmpscpp.cpp:10370, physically verified this session) - the SV-
// bound wrapper form the W-family convention names idfW. SEAT
// RELEASE: the idfW named stub of InterpService.kt is released -
// the real body lands here under the stub's own name and signature
// (the levCheck delegate precedent; the zfcn dispatcher call site
// at InterpService.kt resolves unchanged), the stub itself is
// deleted. The vendor's static char tmp[32] local is never read
// (dead per the dead-code discipline; kept as this ledger note).
// Consults (2026-11-24T21:13:30Z, nominal): the SOH scan of arg1
// for the subscript separator (the char-1 top-level-global guard;
// the FCN_MARK1_D byte family), the banner printf and the
// sigintD(100) raise (the unit-211 sigintD real, the pinned
// 100..133 codes), the arg1 truncation at the scan index (the
// in-place CharArray mutation, no copy), the caret-bypassing
// MpsGlobalNode construction from &arg1[1] (the GlobalNodeStub
// ctor convention: the ref slice carried verbatim), the
// sscanf(arg2, "%lf", &x) -> the DafCRuntime strtod seat (the
// Centroid precedent: leading double parse, no vendor check
// exists to render), the member global::IDF call is a NAMED-SEAT
// STUB RAISED HERE (the globalIdfD seat below; owner
// libmpscpp.cpp::global::IDF, an untranslated later-cycle roster
// row on this arm's rotation - the svptr and ep parameters of the
// dispatcher convention are unused, kept verbatim), the empty-string
// return of the vendor's static "" literal.
// Dafny: EXEMPT (wrapper relay over seats; journal row).

fun idfW(svptr: DafMsv, ep: CharArray, arg1: CharArray, arg2: CharArray): String {

    var i = 0
    while (arg1[i] != NUL_D) { // for (i=0; arg1[i]!=0; i++)
        if (arg1[i] == '\u0001') break // char-1 SOH subscript separator
        i++
    }

    if (arg1[i] == '\u0001') {
        System.out.print("\n*** Arg 1 must be top level global\n")
        sigintD(100)
    }

    arg1[i] = NUL_D // arg1[i]=0 (in-place truncation)

    // global a(&arg1[1]); // bypass ^
    val a = MpsGlobalNode(DafCRuntime.cstringAt(arg1, 1))

    val x = DafCRuntime.strtod(DafCRuntime.cstring(arg2)) // sscanf %lf

    a(DafCRuntime.cstringAt(arg1, i + 1)).let { node -> globalIdfD(node, x) }

    return "" // return (char *) ""
}

// global::IDF member seat (owner libmpscpp.cpp::global::IDF at
// vendor :1764, the 36-LOC body with the LOG2 walk; an untranslated
// roster row on this arm's later-cycle rotation - the stub goes home
// when its owner lands).
fun globalIdfD(n: MpsGlobalNode, x: Double): Unit =
    throw NotImplementedError(
        "global.IDF member: out-of-scope owner (libmpscpp.cpp::global::IDF at :1764; roster row of a later cycle on this arm)")
