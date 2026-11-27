package both

// Unit 213 render: libmpscpp.cpp::global::Piece (roster 213,
// batch 7, 17 LOC; units.csv rank 32). The const-char*
// overload: retrieves the receiver global's value, normalizes
// jj<0 to ii, and runs the \u0024piece engine through _piece
// with the "%d"-formatted counters, setpiece=0, empty lhs.
//
// MULTI-ROW NAME (the Piece family, journaled): FIVE corpus
// rows - units 56 (23 LOC, rank 50, b2 pending), 213 (THIS
// seat, the const char* overload :7192), 238 (17 LOC, rank 48,
// b8 pending), 303/304 (b10, the mstring::Piece twin family).
// The vendor overloads differ only in the pat adapter
// (char*/mstring/string .c_str()); the same-body fold
// adjudicates when the twins land (the Sim1W standing rule).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: Mglobal RETRIEVE direct;
//     NEW TODO stub _pieceZb (owner interp-piece.cpp::<misc>,
//     roster 103, batch 4 pending - the \u0024piece engine,
//     interp-piece.cpp:97); cStringOf/writeC direct.
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: Piece keeps vendor name/case as a
//     GlobalRef member; the stub carries the Zb qualifier (the
//     vendor name has a leading underscore: _piece).
//   h019 class_member_carrier - reused: the member rides the
//     GlobalRef receiver; svPtr the BothCRuntime singleton.
//
// Quirks kept (ledgered):
//   - jj<0 -> ii BEFORE the counter format (the vendor
//     normalizes then sprintf %d's both).
//   - fail-throw BEFORE the piece call (kept first).
//   - the mstring tmp1 return folds to the String value flow.

// void _piece(unsigned char out[], unsigned char in[],
//             unsigned char key[], unsigned char start[],
//             unsigned char end[], int setpiece,
//             unsigned char lhs[], struct MSV *svPtr) -
// interp-piece.cpp:97. Owner: interp-piece.cpp::<misc>, roster
// 103 (batch 4, pending). TODO-stub (h014).
internal fun _pieceZb(
    out: CharArray,
    ind: CharArray,
    key: String,
    start: String,
    end: String,
    setpiece: Int,
    lhs: String,
    sv: Msv,
) {
    TODO("stub: owner interp-piece.cpp::_piece (via <misc>, roster 103, batch 4)")
}

// mstring global::Piece(const char *pat, int ii, int jj) -
// libmpscpp.cpp:7192
internal fun GlobalRef.Piece(pat: String, ii: Int, jj: Int): String {
    val gtmp = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)

    val f = Mglobal(RETRIEVE, ref.toCharArray(), gtmp, svPtr)

    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    val j2 = if (jj < 0) ii else jj // if (jj<0) jj=ii;

    // sprintf(cc1,"%d",ii); sprintf(cc2,"%d",jj)
    _pieceZb(
        tmp, gtmp, pat,
        ii.toString(), j2.toString(),
        0, "",
        svPtr,
    )
    return cStringOf(tmp) // tmp1 = tmp; return tmp1;
}
