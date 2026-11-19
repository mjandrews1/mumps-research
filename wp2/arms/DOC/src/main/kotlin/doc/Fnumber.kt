package doc

// Unit 42: libmpscpp.cpp::mstring::Fnumber (batch 2, core stratum) - the
// two-mstring-argument member. The vendor builds the coded $fnumber call
// into the invoking mstring and reads the dispatched result from the state
// vector: v1d (stateVector.h:44) at offset 1, result bd (stateVector.h:42)
// at offset 1; fcn dispatch is an out-of-scope stub (H-014). The dollar
// byte in the command text is built per cookbook H-015. Dead-construct
// drop per H-013, documented: the vendor branch testing b.c_str()==0 is
// unreachable (c_str never returns null on this carrier), so the else
// arm (comma plus b) executes unconditionally.

private const val DOLLAR = '$'

fun Mstring.fnumber(a: Mstring, b: Mstring): Mstring {
    val tmp = CharArray(STR_MAX)
    val open = charArrayOf(CodedOpen.toChar())
    val comma = charArrayOf(CodedComma.toChar())
    val close = charArrayOf(CodedClose.toChar())

    CstringOps.strcpyS(tmp, DOLLAR + "fnumber")
    CstringOps.strcatS(tmp, CstringOps.cstring(open))

    CstringOps.strcatS(tmp, this.c_str()) // invoking mstring
    CstringOps.strcatS(tmp, CstringOps.cstring(comma))

    CstringOps.strcatS(tmp, a.c_str()) // first argument required

    // second argument optional; the vendor null-branch is dropped per
    // H-013 (unreachable), so the else arm is unconditional:
    CstringOps.strcatS(tmp, CstringOps.cstring(comma))
    CstringOps.strcatS(tmp, b.c_str())
    CstringOps.strcatS(tmp, CstringOps.cstring(close))

    CstringOps.copyAt(Globals.svPtr.v1d, 1, tmp)
    Stubs.fcn(Globals.svPtr)
    val rslt = Mstring(CstringOps.cstringAt(Globals.svPtr.bd, 1))
    return rslt
}


// Unit 97 (B4 tranche 1): libmpscpp.cpp::mstring::Fnumber (batch 4,
// simple stratum; LOC 17, score 7.9) - the ONE-mstring-argument member
// overload, appended to the unit-42 file (the two-argument batch-2
// member render sits above; overload arity resolves the call sites).
// Same family shape as unit 42: the vendor's coded fnumber command (dollar byte
// built per H-015) is built into the invoking mstring and the dispatched
// result is read from the state
// vector (v1d at offset 1 in, bd at offset 1 out); fcn dispatch is the
// H-014 stub seat (owner is this batch's own unit 123, the fcn
// dispatcher, not yet translated). The dollar byte in the command text
// is built per cookbook H-015. H-012: the unsigned char Open/Comma/Close
// coded-byte arrays render as the same charArrayOf carriers as unit 42.
// Vendor comment kept: the single argument is required.

fun Mstring.fnumber(a: Mstring): Mstring {
    val tmp = CharArray(STR_MAX)
    val open = charArrayOf(CodedOpen.toChar())
    val comma = charArrayOf(CodedComma.toChar())
    val close = charArrayOf(CodedClose.toChar())

    CstringOps.strcpyS(tmp, DOLLAR + "fnumber")
    CstringOps.strcatS(tmp, CstringOps.cstring(open))

    CstringOps.strcatS(tmp, this.c_str()) // invoking mstring
    CstringOps.strcatS(tmp, CstringOps.cstring(comma))

    CstringOps.strcatS(tmp, a.c_str()) // first argument required
    CstringOps.strcatS(tmp, CstringOps.cstring(close))

    CstringOps.copyAt(Globals.svPtr.v1d, 1, tmp)
    Stubs.fcn(Globals.svPtr)
    val rslt = Mstring(CstringOps.cstringAt(Globals.svPtr.bd, 1))
    return rslt
}
