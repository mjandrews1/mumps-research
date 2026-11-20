package doc

// Unit 208 (B7 tranche 3): interp-sym.cpp::Qsub (batch 7, complex
// stratum; units.csv row 208: loc 52, score 53.8). The dollar-qsub
// subscript extractor: pushes the state vector (AllocSV the
// batch-8 H-014 seat, correctly tagged), copies the caller's state
// vector over it (the vendor's own "inefficient" comment; the
// copyMsv field adapter is the unit-119 shape DUPLICATED here
// per the vendor's verbatim repetition - the duplicate-def ledger
// of the u141 strcatx precedent applies, H-013 family; the
// Qlength unit-145 twin renders the same prologue), parses the
// name (global or local per the leading carat flag) through the
// real unit-93 parse_ render, keyfixes the v1d at its offset-1
// pointer (the offset slice copy idiom of the unit-119 render),
// writes the rekeyed name back into tmp0 (offset 1 when global,
// offset 0 when local, the copyAtS String form), and extracts the
// j-th SOH-delimited subscript (or the whole name at the -2
// full-name request, empty at the -1 request or past the last
// subscript) into tmp1. The optional tmp2 depth argument is a
// nullable seat (the vendor NULL test shapes the default j=0
// first-subscript). Vendor quirk kept and noted: the inner delimit
// walk overwrites tmp0 in place (the SOH 1 and above-byte-128
// delimiter bytes are NULed inside the caller's buffer - the
// vendor side effect through tmp0 is kept verbatim; tmp1 receives
// the m-offset tail). The unsigned char comparisons ride the Char
// domain (unsigned by construction, H-012 note). atoi is the
// Find.kt strtol-contract render (H-009 family Locale.US not
// needed for atoi). H-010: the LineNumber Long narrows to the
// parse index copy (i is int in vendor); n is the strlen bound.
// free(svPtr) is a JVM GC no-op (ledger). Naming per H-017.

fun Qsub(tmp0: CharArray, tmp1: CharArray, tmp2: CharArray?, svPtrX: Msv) {
    var i: Int
    var j = 0
    var k: Int
    var m = 0 // Kotlin definite-assignment seat: the vendor reads m
    // only after the k loop run; Kotlin requires the zero init.
    var n: Int
    var gblflg = 0

    if (tmp0[0] == '^') gblflg = 1

    i = svPtrX.LineNumber.toInt()
    val svPtr = Stubs.AllocSV() /* push statevector */
    svPtr.LineNumber = i.toLong()

    // memcpy(svPtr,svPtrX,sizeof(struct MSV)); /* copy old state
    // vector - inefficient */ - unit-119 copyMsv shape, duplicated
    // per the vendor's verbatim body repetition (dup ledger; the
    // Qlength unit-145 twin is the same prologue).
    copyMsvQsub(svPtr, svPtrX)
    svPtr.LineNumber = i.toLong()

    svPtr.t0px = 0
    svPtr.t2 = 1

    CstringOps.strcpyC(svPtr.xd, tmp0)
    svPtr.xpx = 0

    j = svPtr.gpad
    svPtr.gpad = 0
    i = Stubs.parse_(svPtr)

    // keyfix(&svPtr->v1d[1]) - the offset-1 slice (unit-119 idiom).
    val v1do = svPtr.v1d.copyOfRange(1, svPtr.v1d.size)
    Stubs.keyfix(v1do)

    svPtr.gpad = j

    // strcpy(&tmp0[gblflg], &svPtr->v1d[1]) - copyAt with the
    // offset-1 source slice; the write-back offset is 1 when
    // global (leading carat kept), 0 when local.
    if (gblflg == 1) {
        CstringOps.copyAt(tmp0, 1, v1do)
    } else {
        CstringOps.copyAt(tmp0, 0, v1do)
    }

    if (tmp2 != null) j = atoi(CstringOps.cstring(tmp2))

    if (j == -1) {
        CstringOps.strcpyS(tmp1, "")
        // free (svPtr); - JVM GC no-op (ledger).
        return
    }

    if (j == -2) {
        CstringOps.strcpyC(tmp1, tmp0)
        // free (svPtr); - JVM GC no-op (ledger).
        return
    }

    n = CstringOps.strlenC(tmp0)
    i = 0

    for (k in 0..j) {
        m = i
        if (m >= n) {
            CstringOps.strcpyS(tmp1, "")
            // free (svPtr); - JVM GC no-op (ledger).
            return
        }
        while (tmp0[i] != NUL) {
            if (tmp0[i] == CHAR_1 || tmp0[i] > '\u0080') {
                tmp0[i] = NUL
                break
            }
            i++
        }
        i++
    }

    // strcpy(tmp1, &tmp0[m]) - the m-offset tail of the NULed
    // name (slice read via cstringAt, String write-back).
    CstringOps.strcpyS(tmp1, CstringOps.cstringAt(tmp0, m))

    // free (svPtr); - JVM GC no-op (ledger).
}

// Msv field-copy adapter: the vendor memcpy of the whole state
// vector over the AllocSV copy (the unit-119 private adapter
// duplicated for this file's owner scope per the vendor's
// verbatim body repetition; bounded to the Msv carrier surface).
private fun copyMsvQsub(dst: Msv, src: Msv) {
    dst.LineNumber = src.LineNumber
    dst.tpx = src.tpx
    dst.t0px = src.t0px
    dst.t2 = src.t2
    dst.io = src.io
    dst.ierr = src.ierr
    dst.NOERR = src.NOERR
    dst.symflg = src.symflg
    dst.order = src.order
    dst.sdlim = src.sdlim
    dst.pd1len = src.pd1len
    dst.ERROR = src.ERROR
    dst.xpx = src.xpx
    dst.ErrChar = src.ErrChar
    dst.gpad = src.gpad
    CstringOps.strcpyC(dst.xd, src.xd)
    CstringOps.strcpyC(dst.pd1, src.pd1)
    CstringOps.strcpyC(dst.bd, src.bd)
    CstringOps.strcpyC(dst.v1d, src.v1d)
    CstringOps.strcpyC(dst.setname, src.setname)
    CstringOps.strcpyC(dst.args, src.args)
}
