package mumps.interp

/**
 * PRO-arm translation of interp-sysfunc.cpp::LocalName (B4 unit
 * 112, rank 121). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 827-869.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-sysfunc.cpp::LocalName, cpp, loc 36,
 * comment_ratio 0.027, pointer_marks 0, control_marks 7, score
 * 12.6 - exact match to wp1/study-subset/units.csv row 113.
 *
 * LocalName(): render the interpreter's coded local-variable
 * reference (CodedOpen 206 / CodedClose 207 / CodedComma 208
 * subscript separators) back into the parenthesized, quoted
 * display form - "(sub1,sub2)" - honoring the optional subscript
 * limit tmp2 (default 999: no limit). The translated name lands
 * in tmp1; the third-level high-byte (>127) forms stop the copy.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : tmp3 is the STR_MAX staging
 *    buffer; the tmp0/tmp1 pointer-into-buffer arguments ride
 *    offset params (the Fcn.kt call sites pass offsets 0/1 and
 *    the iargs[1] subscript position).
 *  - h012 unsigned_byte_domain: all buffers Char(0..255); the
 *    coded separators compare against the .code of the domain
 *    bytes (206/207/208), and the vendor's tmp0[i]>127 compares
 *    the unsigned byte domain (.code > 127).
 *  - h014 out_of_scope_call  : atoi resolves to the real B1
 *    atoiC helper (CstringOps), reused unmodified; strcpy is the
 *    real strcpyC. The InterpStubs LocalName stub raised by B4
 *    unit 94 (owner this unit) is RESOLVED and REMOVED
 *    (cleanLocks precedent, unit 56); the Fcn.kt call sites now
 *    bind here with their existing offset shapes.
 *  - h015 grammar_collision  : the NUL/separator literals are
 *    Char carriers and Int pins (no quoting hazards); the
 *    Coded* pins are the file-private copies (cf. Fcn.kt /
 *    Fnumber_.kt, which keep their own).
 *  - h017 naming             : LocalName keeps the vendor
 *    spelling; tmp0/tmp1/tmp2 keep the vendor parameter names.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - j defaults to 999 (no subscript limit); tmp2 == NULL keeps
 *    the default (the Fcn call sites pass null for the
 *    one-argument form).
 *  - The first high-byte arm "k>=j && k==0" emits a bare NUL
 *    (name with no subscripts opened); the second emits a
 *    closing paren first - the two arms are kept as written.
 *  - k counts opened subscripts but is compared k>=j only at
 *    the high-byte arms (a name whose THIRD-level element
 *    crosses the limit stops there; plain bytes always copy) -
 *    vendor shape, kept.
 *  - "k==0" in the first arm is only reachable when j<=0 (e.g.
 *    tmp2="0"): the vendor's 999 default makes k>=j false until
 *    subscripts open; kept verbatim.
 */
private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. Fcn.kt)

private const val CODED_OPEN = 206 // vendor defines.h (CodedOpen)
private const val CODED_CLOSE = 207 // vendor defines.h:39 (CodedClose)
private const val CODED_COMMA = 208 // vendor defines.h:40 (CodedComma)

/** vendor interp-sysfunc.cpp:827-869 - void LocalName(tmp0, tmp1, tmp2, svPtr). */
fun LocalName(tmp0: CharArray, tmp0Off: Int, tmp1: CharArray, tmp1Off: Int,
              tmp2: CharArray?, tmp2Off: Int, svPtr: Msv) {

    var i = 0
    var j = 999
    var k = 0
    var m = 0
    val tmp3 = CharArray(STR_MAX)

    if (tmp2 != null) j = atoiC(tmp2, tmp2Off)

    while (tmp0[tmp0Off + i] != NUL) {

        if (k >= j && k == 0 && tmp0[tmp0Off + i].code > 127) {
            tmp3[m] = NUL
            m++
            break
        } else if (k >= j && tmp0[tmp0Off + i].code > 127) {
            tmp3[m] = ')'
            m++
            tmp3[m] = NUL
            m++
            break
        } else if (tmp0[tmp0Off + i].code == CODED_OPEN) {
            tmp3[m] = '('
            m++
            tmp3[m] = '"'
            m++
            k++
        } else if (tmp0[tmp0Off + i].code == CODED_CLOSE) {
            tmp3[m] = '"'
            m++
            tmp3[m] = ')'
            m++
            k++
        } else if (tmp0[tmp0Off + i].code == CODED_COMMA) {
            tmp3[m] = '"'
            m++
            tmp3[m] = ','
            m++
            tmp3[m] = '"'
            m++
            k++
        } else {
            tmp3[m] = tmp0[tmp0Off + i]
            m++
        }

        i++
    }

    tmp3[m] = NUL

    // vendor: strcpy((char *) tmp1, (char *) tmp3);
    writeCstring(tmp1, cstringAt(tmp3), tmp1Off)
}
