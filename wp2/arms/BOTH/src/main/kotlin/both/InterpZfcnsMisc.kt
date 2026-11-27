package both

// Unit 229 render: interp-zfcns.cpp::<misc> (roster 229, batch 8,
// 77 LOC dump; the TU head before zfcn, the roster-184 owner).
// The interp-zfcns.cpp file-scope residue: the banner, the
// include ledger, the sym_/wstem/sigint prototypes, the SQLITE
// define, the zfcn-local CLOSE 207 / COMMA 208 defines, the
// NO/YES pair, the gcvt_wrapper macro, and the Mget helper.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h004 include_only - reused: the banner/includes/prototype
//     block ride this KDoc (no seats); the gtk/gtkx includes
//     are platform edges consumed by the mdh family renders.
//   h007 char_buffer - reused: Mget's char tmp[128] rides a
//     local CharArray (the sym_ out-buffer; the 128 width
//     kept).
//   h014 out_of_scope_call - reused: sym_ is the seated TODO
//     stub (owner interp-sym.cpp::sym_, roster 280 batch 10
//     pending - the MpstimeMain seat); called direct.
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: Mget keeps vendor name/case (no
//     Kotlin collision; the BuiltIn-table "^Input" family is
//     distinct).
//
// Quirks kept (ledgered):
//   - this TU REDEFINES CLOSE as 207 (the CodedClose byte) -
//     a shadow of the package CLOSE=4 Mglobal opcode (the
//     record 226 ledger twin); the zfcn owner consumes the
//     207/208 pair as the \u0024z envelope bytes. NOT seated
//     here (no collision risk: the ledger cites the
//     BifsZfcn ZB_ family).
//   - the gcvt_wrapper macro has TWO arms: the GCVT build
//     sprintf(a,"%g",x) and the default gcvt(x,i,a) - the
//     zfcn owner render picks per the reference build; the
//     numdigits argument is IGNORED in the %g arm (kept as
//     the ledger note).
//   - Mget returns the sym_ RESULT, not tmp (tmp is the
//     out-param seat; the vendor cast returns sym_'s value -
//     kept).
//   - the "TODO: Extern globals prevent compiling this into a
//     dynamic lib" comment rides the vendor head (kept in the
//     ledger; the fcn extern is commented out - dead in the
//     pristine tree).

// unsigned char * Mget(unsigned char * s, struct MSV * svPtr)
// - interp-zfcns.cpp
internal fun Mget(s: CharArray, svPtr: Msv): CharArray? {
    val tmp = CharArray(128)
    return sym_(1, cStringOf(s), tmp, svPtr)
}
