package mumps.compiler

import java.io.IOException

/**
 * PRO-arm translation of compiler-parse.c::ExpandText
 * (batch-8 unit 235, units.csv data line 235, rank 117).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 2374-2411.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-parse.c::ExpandText, c, loc 23,
 * comment_ratio 0.042, pointer_marks 2, control_marks 2,
 * score 12.1, rank 117, batch 8 - exact match to
 * wp1/study-subset/units.csv line 235.
 *
 * ExpandText(v1d, i, bp): the $TEXT expansion - NUL the coded
 * run in v1d at the >128/SOH boundary, emit the
 * CodedOpen-bracketed <dollar>text fcn call with the Extract2a
 * field, and stage the result in tmp<iiitmp> (bp).
 *
 * No prior stub (the ParseMisc prototype roster documented the
 * seat); first Kotlin render.  Uses the Extract2a seat
 * (unit 231, same file/tranche - a same-batch reuse).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param: Out emissions via CompilerState.out
 *    (ExpandGet unit-16 precedent, byte-identical text).
 *  - h009 format_string : fprintf %s/%x/%d become spliced
 *    string writes; sprintf(bp,"tmp%d",iiitmp) writes bp
 *    through the C-string edge.
 *  - h012 unsigned_byte_domain: v1d/bp are Char(0..255)
 *    domains; the >128 and ==1 boundary tests read raw bytes.
 *  - h014 out_of_scope_call: Extract2a (u231) and memcheck
 *    (defines-owner seat, Extract precedent) reused; CodedOpen
 *    206 / CodedClose 207 per-file consts (vendor 131-132).
 *  - h015 grammar_collision: new top-level ExpandText - no
 *    collisions.
 *  - h017 naming      : ExpandText keeps the vendor name.
 *  - h018 preprocessor_conditional: none in this body.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - i++ FIRST: the scan starts one past the caller's i
 *    (the coded marker at i is skipped) - kept.
 *  - The boundary scan writes NUL AT the >128/SOH byte and
 *    keeps it NUL'd in the caller's v1d (in-place mutation,
 *    kept).
 *  - The emitted "\\x%x" escapes carry the CodedOpen/CodedClose
 *    values (206/207 - the "\xce"/"\xcf" family of unit 188).
 *  - memcheck brackets both the pre- and post-increment of
 *    iiitmp (kept).
 */

private const val CODED_OPEN = 206 // vendor compiler-parse.c:131 CodedOpen
private const val CODED_CLOSE = 207 // vendor compiler-parse.c:132 CodedClose

@Throws(IOException::class)
fun ExpandText(v1d: CharArray, iIn: Int, bp: CharArray) {
    val st = CompilerState
    var i = iIn + 1 // vendor: i++;

    // vendor: for (j=i; v1d[j]!='\0'; j++) if (v1d[j]>128||v1d[j]==1) {
    //         v1d[j]='\0'; break; }
    var j = i
    while (v1d[j] != 0.toChar()) {
        if (v1d[j] > 128.toChar() || v1d[j] == 1.toChar()) {
            v1d[j] = 0.toChar()
            break
        }
        j++
    }

    st.out.write(
        "\tstrcpy((char *) &gtmp[1], \"\\u0024text\\x" + Integer.toHexString(CODED_OPEN) +
            "\"); /* CodedOpen */\n"
    )

    st.out.write(
        "\tstrcat((char *) &gtmp[1], (const char *) " + Extract2a(v1d, i) + "); \n"
    )

    st.out.write(
        "\tstrcat((char *) &gtmp[1], \"\\x" + Integer.toHexString(CODED_CLOSE) +
            "\"); /* CodedClose */\n"
    )

    st.out.write(
        "\tstrmove((unsigned char *) &svPtr->v1d[1], " +
            "(unsigned char *) &gtmp[1]); \n"
    )

    memcheck(st.iiitmp)

    st.out.write("\tfcn(svPtr);\n")

    st.out.write(
        "\tstrmove((unsigned char *) tmp" + st.iiitmp +
            ",(unsigned char *) &svPtr->bd[1]);\n"
    )

    // vendor: sprintf (bp, "tmp%d", iiitmp);
    val tmpName = "tmp" + st.iiitmp
    for (x in tmpName.indices) bp[x] = tmpName[x]
    bp[tmpName.length] = 0.toChar()

    st.iiitmp++
    memcheck(st.iiitmp)
}
