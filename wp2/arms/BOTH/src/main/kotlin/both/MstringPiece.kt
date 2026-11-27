package both

// Unit 283 (BOTH B10, simple): libmpscpp.cpp::mstring::Piece
// (17 LOC). Batch_plan ordinal 283; units.csv row 304. The
// global-pat overload of the mstring piece member face, at
// libmpscpp.cpp:7332 - OVERLOAD DISAMBIGUATION (the units.csv
// duplicate-id ledger, the file-stable score sort): the 17-LOC
// score-5.5 rank-34 batch-10 row is the global-pat overload;
// the 11-LOC const-char*/string/mstring-and-pat twins
// (:7301/:7317/:7355) are non-roster overloads (the same-body
// fold adjudicates them to this seat, the Sim1W standing
// rule).
//
// Vendor behavior: retrieve the PATTERN global pat's value
// (fail-throw on miss), normalize jj<0 to ii, and run the
// \\u0024piece engine over THIS mstring's data with the
// pattern value as the delimiter, the "%d"-formatted
// counters, setpiece=0, empty lhs. Differs from the
// global::Piece family: the pieces come from the mstring
// receiver's data, and the retrieved global is the pattern.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: gtmp/tmp/cc1/cc2 are the C
//     byte-domain buffers; the retrieve folds to the
//     mglobalRetrieve String seat and the engine call
//     carries CharArray copies (the Piece precedent).
//   h008 static_scratch - reused: cc1/cc2 are LOCAL scratch
//     (not static); the two sprintf writes fold into the
//     counter strings (no caller aliases them).
//   h009 format_string - reused: "%d" is a plain decimal.
//   h014 out_of_scope_call - reused: the Mglobal RETRIEVE
//     and fail-throw idiom ride the mglobalRetrieve seat;
//     the _piece engine rides the _pieceZb stub (owner
//     interp-piece.cpp::<misc>, roster 114 - renumbered on
//     this push from the units.csv-row mislabel 103).
//   h017 naming - reused: the member face renders as the
//     package-level mstringPiece (the mstringLength
//     precedent, receiver state passed explicitly); ii/jj/f
//     keep vendor names.
//   h019 class_member_carrier - reused: member of the
//     mstring class (layout owner mstringClass.h::<misc>,
//     roster 149, untranslated in this arm); the receiver
//     data passes explicitly as the String carrier.
//   Dafny: exempt (Mglobal retrieval side effect; the piece
//     engine is the stub-seat exemption).
//
// Vendor quirks kept (ledgered):
//   - jj<0 -> ii BEFORE the counter format (the vendor
//     normalizes then sprintf %d's both; the Piece precedent).
//   - fail-throw BEFORE the engine call (kept first).

// mstring mstring::Piece(global pat, int ii, int jj)
//   - libmpscpp.cpp:7332
internal fun mstringPiece(data: String, pat: GlobalRef, ii: Int, jj: Int): String {
    // unsigned char gtmp[STR_MAX]; Mglobal(RETRIEVE, pat.ref, gtmp)
    val gtmp = mglobalRetrieve(pat.ref)

    var j2 = jj
    if (j2 < 0) j2 = ii // if (jj<0) jj=ii;

    // sprintf(cc1,"%d",ii); sprintf(cc2,"%d",jj)
    //   (h008 fold: local scratch collapses into the counter
    //   strings the engine call carries)
    val tmp = CharArray(STR_MAX)
    _pieceZb(
        tmp, data.toCharArray(), gtmp,
        ii.toString(), j2.toString(),
        0, "",
        svPtr,
    )

    // mstring tmp1 = tmp; return tmp1;
    return cStringOf(tmp)
}
