package mumps.interp

/**
 * PRO-arm translation of interp-fnumber.cpp::_fnumber (B4 unit
 * 106, rank 174). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 91-116 (the dollar-fnumber wrapper for
 * the compiler).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-fnumber.cpp::_fnumber, cpp, loc 17,
 * comment_ratio 0.056, pointer_marks 9, control_marks 0, score
 * 21.9 - exact match to wp1/study-subset/units.csv row 107.
 *
 * _fnumber(): assemble the coded dollar-fnumber call
 * "<dollar>fn<Open>number<Comma>arg1<Comma>arg2<Close>" into the
 * state vector's v1d buffer at offset 1, run the interpreter's
 * function dispatcher fcn, and copy the result from bd at offset
 * 1 into tmp0.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : the call assembly rides a local
 *    STR_MAX C-string buffer, then one writeCstring lands it at
 *    v1d offset 1 (the vendor's per-strcat pointer arithmetic
 *    collapses; byte order identical).
 *  - h012 unsigned_byte_domain: all buffers Char(0..255); the
 *    vendor offset-1 convention (v1d[1]/bd[1]) kept.
 *  - h014 out_of_scope_call  : fcn is the REAL B4 unit 94
 *    translation (Fcn.kt) - reused, not stubbed; strcpy/strcat
 *    resolve to the real B1 CstringOps helpers - reused
 *    unmodified.
 *  - h015 grammar_collision  : the vendor literal "<dollar>fn"
 *    contains a raw dollar byte the pre-push scan forbids; built
 *    via the DOLLAR char literal carrier (36) and string
 *    concatenation. The coded separators are the file-private
 *    CodedOpen/CodedComma/CodedClose pins (defines.h:169/39/40;
 *    cf. Fcn.kt and the mdh Fnumber.kt, which keep their own
 *    copies - h015 file-private duplicates).
 *  - h017 naming            : _fnumber keeps the vendor spelling
 *    (leading-underscore identifiers are legal Kotlin, cf.
 *    _piece/_extract precedents); the file is named Fnumber_
 *    after the vendor file interp-fnumber.cpp, mirroring the
 *    Readf_.kt naming.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor literal is "$fn", NOT "$fnumber" - the fcn
 *    dispatcher keys the opcode off the leading letters after
 *    the dollar byte ("fn" reaches the $fnumber case via the
 *    opcode 102 table entry); the two-arg mstring::Fnumber
 *    carrier spells out "$fnumber" (mdh/Fnumber.kt, B2 unit 42)
 *    - both spellings are vendor-verbatim and both dispatch to
 *    the same case; kept, not harmonized.
 *  - The wrapper returns void; the result is the tmp0 copy.
 */
private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. Fcn.kt)

private const val CODED_OPEN = 206 // vendor defines.h (CodedOpen)
private const val CODED_COMMA = 208 // vendor defines.h:40 (CodedComma)
private const val CODED_CLOSE = 207 // vendor defines.h:39 (CodedClose)

/** vendor printf literal dollar byte (h015; cf. Fcn.kt DOLLAR). */
private val DOLLAR = 36.toChar()

/** vendor interp-fnumber.cpp:91-116 - void _fnumber(tmp0, number, arg1, arg2, svPtr). */
fun _fnumber(tmp0: CharArray, number: CharArray, arg1: CharArray, arg2: CharArray,
             svPtr: Msv) {

    // vendor: char open[] = {(char)CodedOpen,0}; comma/comma/close likewise
    val open = charArrayOf(CODED_OPEN.toChar(), NUL)
    val comma = charArrayOf(CODED_COMMA.toChar(), NUL)
    val close = charArrayOf(CODED_CLOSE.toChar(), NUL)

    // vendor assembles &svPtr->v1d[1] with strcpy + a strcat chain;
    // the identical byte sequence is staged in a local buffer (h007)
    val tmp = CharArray(STR_MAX)
    writeCstring(tmp, DOLLAR + "fn")
    strcatC(tmp, cstringAt(open))
    strcatC(tmp, cstringAt(number))
    strcatC(tmp, cstringAt(comma))
    strcatC(tmp, cstringAt(arg1))
    strcatC(tmp, cstringAt(comma))
    strcatC(tmp, cstringAt(arg2))
    strcatC(tmp, cstringAt(close))

    writeCstring(svPtr.v1d, cstringAt(tmp), 1)

    fcn(svPtr)

    // vendor: strcpy((char *) tmp0, (char *) &svPtr->bd[1]);
    writeCstring(tmp0, cstringAt(svPtr.bd, 1))
}
