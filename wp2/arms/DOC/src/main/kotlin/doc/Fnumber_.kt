package doc

// Unit 111 (B4 tranche 2): interp-fnumber.cpp::_fnumber (batch 4,
// core stratum; LOC 17, score 21.9, rank 174) - the runtime
// support wrapper that builds the coded command for the SET
// ASSIGNMENT-time dollar-fn call and dispatches through the
// interpreter fcn. SPELLING QUIRK kept (vendor): the command text
// is "dollar-fn" - the MISSING "umber" suffix distinguishes this
// runtime wrapper from the fnumber intrinsics family (unit-97/42
// Fnumber renders build the full word); the emitted bytes are
// kept verbatim per the vendor. The dollar byte is built per
// cookbook H-015 (the per-file DOLLAR const; the unit-42 file
// keeps its own). The coded delimiters ride the arm CodedOpen/
// CodedComma/CodedClose carriers (defines.h:38-40, 206/207/208).
// The v1d command buffer write and the bd result read use the
// offset-1 state-vector seats (stateVector.h:44/:42; the
// copyAt/cstringAt adapters). fcn dispatch is the H-014 stub
// seat (owner: this batch's unit 123, the fcn dispatcher, not
// yet translated - the same seat the Fnumber renders bind).
// tmp0 is the caller's out-buffer (the vendor strcpy from bd+1
// into the first argument; the render strcpyS's the trimmed
// String). Vendor shape note: the vendor signature's fifth
// parameter svPtr rides Globals-shared Msv; the tabbed line
// continuation of the vendor source is folded (whitespace
// normalization only). Naming per H-017: the leading underscore
// is kept verbatim (_Sym precedent).

private const val DOLLAR_111 = '\u0024'

fun _fnumber(
    tmp0: CharArray,
    number: CharArray,
    arg1: CharArray,
    arg2: CharArray,
    svPtr: Msv
) {
    val open = charArrayOf(CodedOpen.toChar())
    val comma = charArrayOf(CodedComma.toChar())
    val close = charArrayOf(CodedClose.toChar())

    val cmd = CharArray(STR_MAX)

    CstringOps.strcpyS(cmd, DOLLAR_111 + "fn")
    CstringOps.strcatS(cmd, CstringOps.cstring(open))

    CstringOps.strcatS(cmd, CstringOps.cstring(number))
    CstringOps.strcatS(cmd, CstringOps.cstring(comma))

    CstringOps.strcatS(cmd, CstringOps.cstring(arg1))
    CstringOps.strcatS(cmd, CstringOps.cstring(comma))

    CstringOps.strcatS(cmd, CstringOps.cstring(arg2))
    CstringOps.strcatS(cmd, CstringOps.cstring(close))

    CstringOps.copyAt(svPtr.v1d, 1, cmd)
    Stubs.fcn(svPtr)

    CstringOps.strcpyS(tmp0, CstringOps.cstringAt(svPtr.bd, 1))
}
