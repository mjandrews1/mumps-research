package daf

// Units 126/127/128 (DAF B5 T1, simple): libmpscpp.cpp::
// mdh_tree_level_add, the THREE same-named overloads by arity (21/17/
// 15 LOC): each builds the \u0024z~mdh~tree~level~add coded service call
// (xce open paren, xd0 comma, xcf close paren) over the GTK
// tree-store handle, the depth, and 1-4 column strings, then relays
// through zfcn.
// Consults: BASE-log row 98 hit-reuse (the three C++ overloads of one
// name render as one Kotlin overload set by arity, per the
// ScanAlnum/Doty precedent), row 33 hit-adapt (the sprintf("%p",tree)
// pointer formatting row: NO native pointer exists on the JVM - the
// GtkTreeStore* becomes an opaque Int handle and %p renders the
// handle; DOCUMENTED DIVERGENCE: the printed text is the handle
// integer in hex per the vendor %p shape, not a C pointer value),
// row 26 hit-reuse (the %d depth format via the String int format),
// rows 8/81 hit-adapt (tmp[4096]/tmp[256] buffer roles: strcpy/
// strcat/sprintf folds onto a StringBuilder walk, per the row-8
// buffer-role classification; nothing is read past a terminator
// because the builder never writes one until the relay), rows 6/22
// hit-reuse (the implicit libmpscpp TU svPtr rides
// DafInterpState.svPtr), row 30 hit-adapt (tmp2 is the zfcn
// out-buffer carrier: CharArray seat, the seat writes only).
// Seats: zfcnDaf (owner: interp-zfcns.cpp::zfcn, roster b6 - the
// named-owner stub is raised at the bottom of this file; the
// \u0024z~mdh~tree~level~add consumer GtkTreeAdd is roster unit
// interp-zfcns.cpp::GtkTreeAdd, batch 3).
// The tmp writes start at &tmp[1] (the caret-bypass carriage of the
// row-101 v1d family adapted: the builder carries the same
// one-leading-byte shift; the relay passes the buffer with the same
// offset the vendor passes).
// Dafny: EXEMPT (service-call shaping over stub seats; journal row).

private const val XCE_D: Char = '\u00CE' // vendor "\xce" open paren
private const val XD0_D: Char = '\u00D0' // vendor "\xd0" comma
private const val XCF_D: Char = '\u00CF' // vendor "\xcf" close paren

// sprintf("%p", tree): opaque handle print (row-33 divergence fold).
// DOCUMENTED DIVERGENCE: no native pointer on the JVM; the handle
// prints in the C %p hex shape (0x...).
private fun fmtPointerD(handle: Int): String =
    "0x" + Integer.toHexString(handle)

// strcpy/strcat walk onto the tmp carriage (row 8 fold): the vendor
// builds at &tmp[1] and passes (unsigned char *) tmp downstream.
private fun buildZCallD(tree: Int, depth: Int, cols: List<String>): String {
    val sb = StringBuilder("\u0024z~mdh~tree~level~add")
    sb.append(XCE_D)
    sb.append(fmtPointerD(tree))
    sb.append(XD0_D)
    sb.append(depth.toString()) // sprintf(tmp1, "%d", depth)
    for (c in cols) {
        sb.append(XD0_D)
        sb.append(c)
    }
    sb.append(XCF_D)
    return sb.toString()
}

// zfcn relay seat (owner: interp-zfcns.cpp::zfcn, roster b6). The
// vendor's unsigned char* carrier pair folds to (msg, out-buffer).
private fun zfcnDaf(msg: String, out: CharArray, sv: DafMsv): Unit =
    throw NotImplementedError(
        "zfcn: out-of-scope owner (interp-zfcns.cpp::zfcn, roster b6)")

// unit 126: the 4-column overload (21 LOC)
fun mdhTreeLevelAddD(tree: Int, depth: Int,
                     col1: String, col2: String, col3: String, col4: String) {
    val msg = buildZCallD(tree, depth, listOf(col1, col2, col3, col4))
    val tmp2 = CharArray(STR_MAX_DAF)
    zfcnDaf(msg, tmp2, DafInterpState.svPtr) // zfcn(tmp, tmp2, svPtr)
}

// unit 127: the 2-column overload (17 LOC)
fun mdhTreeLevelAddD(tree: Int, depth: Int, col1: String, col2: String) {
    val msg = buildZCallD(tree, depth, listOf(col1, col2))
    val tmp2 = CharArray(STR_MAX_DAF)
    zfcnDaf(msg, tmp2, DafInterpState.svPtr)
}

// unit 128: the 1-column overload (15 LOC)
fun mdhTreeLevelAddD(tree: Int, depth: Int, col1: String) {
    val msg = buildZCallD(tree, depth, listOf(col1))
    val tmp2 = CharArray(STR_MAX_DAF)
    zfcnDaf(msg, tmp2, DafInterpState.svPtr)
}

// unit 226 (DAF B8 T1, simple): the NINE-column overload
// (33 LOC, score 10.3; vendor libmpscpp.cpp:534, physically
// verified this session - the loc-asserted roster occurrence). The
// family arity ladder now covers 1 (unit 128), 2 (unit 127), 4
// (unit 126) and 9 (this row); the vendor's other arities (3, 5-8,
// 10) are roster rows of later cycles or non-roster kins, each
// keeping its own seat. Same build shape as the landed overloads:
// the coded \u0024z~mdh~tree~level~add relay through buildZCallD,
// the handle/depth decimal carriers, and the zfcnDaf dispatch (the
// public Zfcns.kt real resolves; this file's private stub predates
// the real and stays shadowed, H-016 single-seat rule).
// Consults (2026-11-24T21:15:00Z, nominal): the unit-126/127/128
// family consult set applies verbatim (row 98 overload-set, row 33
// pointer-hex divergence fold, row 26 depth decimal, rows 6/22
// implicit svPtr via DafInterpState.svPtr, row 30 tmp2 write-only
// carrier).
// Dafny: EXEMPT (service-call shaping over the zfcn seat; journal
// row).

fun mdhTreeLevelAddD(tree: Int, depth: Int,
                     col1: String, col2: String, col3: String,
                     col4: String, col5: String, col6: String,
                     col7: String, col8: String, col9: String) {
    val msg = buildZCallD(tree, depth,
        listOf(col1, col2, col3, col4, col5, col6, col7, col8, col9))
    val tmp2 = CharArray(STR_MAX_DAF)
    zfcnDaf(msg, tmp2, DafInterpState.svPtr) // zfcn(tmp, tmp2, svPtr)
}

// unit 258 (DAF B9, core): the SIX-column overload (26 LOC, score
// 11.0; loc-asserted roster occurrence, physically verified this
// session). The family arity ladder now covers 1 (unit 128), 2
// (unit 127), 4 (unit 126), 6 (this row) and 9 (unit 226); the
// vendor's other arities (3, 5, 7, 8, 10) are roster rows of
// later cycles or non-roster kins, each keeping its own seat.
// Same build shape as the landed overloads: the coded
// \u0024z~mdh~tree~level~add relay through buildZCallD, the
// handle/depth decimal carriers, the zfcnDaf dispatch (H-016
// single-seat rule: this file's private stub stays shadowed).
// Consults (2026-11-24T22:36:00Z, nominal): the unit-126/127/128/
// 226 family consult set applies verbatim (row 98 overload-set,
// row 33 pointer-hex divergence fold, row 26 depth decimal, rows
// 6/22 implicit svPtr via DafInterpState.svPtr, row 30 tmp2
// write-only carrier).
// Dafny: EXEMPT (service-call shaping over the zfcn seat; journal
// row).

fun mdhTreeLevelAddD(tree: Int, depth: Int,
                     col1: String, col2: String, col3: String,
                     col4: String, col5: String, col6: String) {
    val msg = buildZCallD(tree, depth,
        listOf(col1, col2, col3, col4, col5, col6))
    val tmp2 = CharArray(STR_MAX_DAF)
    zfcnDaf(msg, tmp2, DafInterpState.svPtr) // zfcn(tmp, tmp2, svPtr)
}
