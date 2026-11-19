package doc

// Unit 105 (B4 tranche 2): interp-sysfunc.cpp::LocalName (batch 4,
// core stratum; LOC 36, score 12.6, rank 121). The readable-name
// expander used by the query walkers: folds the coded delimiters
// 206/207/208 into the parenthesized name form. TAG CORRECTION on
// the Stubs.LocalName seat ("not a roster unit" mis-tag): row 121
// IS this batch-4 unit; the seat is removed with this note (no
// in-arm call site binds yet - the batch-5 InterpParseMisc ledger
// mentions the walker but binds via the Qsub/Qlength seats). The
// coded bytes coincide in VALUE with the CodedOpen(206)/
// CodedClose(207)/CodedComma(208) carriers (defines.h:38-40) but
// are compared as literal case values per the vendor; the tmp2
// optional subscript-limit argument (NULL default) renders as the
// nullable third parameter; atoi rides the arm atoi(String) seat
// (Find.kt, decimal-prefix C contract). Vendor quirks kept and
// noted: the high-byte test tmp0[i]>127 with k>=j and k==0 writes
// the lone NUL break (the unsigned-domain compare renders on the
// Char code); the second k>=j arm without the k==0 test splits
// the j>0 domain, so the two arms break differently; j defaults
// to 999 (the no-tmp2 domain never triggers the k>=j cuts). The
// unsigned char walk renders as the Char domain (H-012). The
// STR_MAX tmp3 scratch is per-call (H-008: the vendor local is
// not static - no cross-call state). Naming per H-017.

fun LocalName(
    tmp0: CharArray,
    tmp1: CharArray,
    tmp2: CharArray?,
    svPtr: Msv
) {
    var j = 999
    var k = 0
    val tmp3 = CharArray(STR_MAX)

    if (tmp2 != null) j = atoi(CstringOps.cstring(tmp2))

    var i = 0
    var m = 0
    while (tmp0[i] != NUL) {

        if (k >= j && k == 0 && tmp0[i].code > 127) {
            tmp3[m++] = NUL
            break
        } else if (k >= j && tmp0[i].code > 127) {
            tmp3[m++] = ')'
            tmp3[m++] = NUL
            break
        } else if (tmp0[i] == Char(206)) {
            tmp3[m++] = '('
            tmp3[m++] = '"'
            k++
        } else if (tmp0[i] == Char(207)) {
            tmp3[m++] = '"'
            tmp3[m++] = ')'
            k++
        } else if (tmp0[i] == Char(208)) {
            tmp3[m++] = '"'
            tmp3[m++] = ','
            tmp3[m++] = '"'
            k++
        } else {
            tmp3[m++] = tmp0[i]
        }

        i++
    }

    tmp3[m] = NUL

    CstringOps.strcpyS(tmp1, CstringOps.cstring(tmp3))
}
