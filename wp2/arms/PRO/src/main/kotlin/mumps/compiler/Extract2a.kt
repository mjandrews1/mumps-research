package mumps.compiler

/**
 * PRO-arm translation of compiler-parse.c::Extract2a
 * (batch-8 unit 231, units.csv data line 231, rank 158).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 2129-2150.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-parse.c::Extract2a, c, loc 21,
 * comment_ratio 0.045, pointer_marks 4, control_marks 5,
 * score 18.3, rank 158, batch 8 - exact match to
 * wp1/study-subset/units.csv line 231.
 *
 * Extract2a(char *str): the #-directive extractor - split a
 * "#P#first#second" token and emit the sym_ (S) or Mglobal (G)
 * retrieval line for it; returns the second field (the static
 * tmp2) or the input when the token does not start with '#'.
 *
 * No prior stub (the ParseMisc prototype roster documented the
 * seat); first Kotlin render (h015 clean).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch: static char tmp1[64],tmp2[64] ride
 *    the Extract2aState object (per-call writes overwrite the
 *    prior content, matching the static reuse).
 *  - h009 format_string : the fprintf emissions become
 *    CompilerState.out writes with the %s operands spliced in
 *    (Extract2 unit precedent, byte-identical text).
 *  - h012 unsigned_byte_domain: str is a Char(0..255) domain;
 *    field slicing folds to String ops at the C-string edge
 *    (Extract2 precedent).
 *  - h015 grammar_collision: new top-level Extract2a - no
 *    collisions (Extract2/Extract1/Extract are separate
 *    seats).
 *  - h017 naming      : Extract2a keeps the vendor name
 *    (capital E, the file's own prototype form at vendor 169).
 *  - h018 preprocessor_conditional: none in this body.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The '#' scan starts at i=3 (skipping the "#P#" prefix);
 *    a str without '#' at [0] is returned UNPARSED (kept).
 *  - When neither #S# nor #G# matches (some other letter),
 *    NOTHING is emitted but tmp2 is still built and returned
 *    (silent fall-through kept verbatim).
 *  - The memcpy bounds: tmp1 gets i-3 bytes (field 1), tmp2
 *    gets i-j bytes (field 2) - the trailing '#' or NUL is
 *    excluded by the loop conditions (kept).
 *  - E21000 comment kept in the emitted sym_ line.
 */

private const val TMP_SZ = 64 // vendor: static char tmp1[64], tmp2[64]

/** Carrier for the vendor statics (h008). */
private object Extract2aState {
    val tmp1 = CharArray(TMP_SZ)
    val tmp2 = CharArray(TMP_SZ)
}

fun Extract2a(str: CharArray, off: Int = 0): String {
    val st = CompilerState

    if (str[off] != '#') return cstringAt(str, off)

    val s = cstringAt(str, off)

    // vendor: for (i=3; str[i]!='#' && str[i]!='\0'; i++);
    var i = 3
    while (i < s.length && s[i] != '#' && s[i] != '\u0000') i++
    // vendor: memcpy(tmp1,&str[3],i-3); tmp1[i-3]='\0';
    val f1 = s.substring(3, i)
    writeC(Extract2aState.tmp1, f1)

    // vendor: for (j=++i; str[i]!='#' && str[i]!='\0'; i++);
    val j = i + 1
    i++
    while (i < s.length && s[i] != '#' && s[i] != '\u0000') i++
    // vendor: memcpy(tmp2,&str[j],i-j); tmp2[i-j]='\0';
    val f2 = if (j <= i) s.substring(j, i) else ""
    writeC(Extract2aState.tmp2, f2)

    if (s.startsWith("#S#")) {
        st.out.write(
            "\tsym_(1, (UNS) " + f1 + ", (UNS) " + f2 + "," +
                "svPtr) ? " + f2 + " : VariableNotFound1(svPtr->LineNumber, (UNS) " + f1 + "); //E21000\n"
        )
    } else if (s.startsWith("#G#")) {
        st.out.write(
            "\tMglobal(RETRIEVE, " + f1 + ", " + f2 + ", svPtr) ?" + f2 +
                " : VariableNotFound(svPtr->LineNumber, " + f1 + ");\n"
        )
    }

    return f2
}

/** Write a field into the static slot incl. NUL (h007 helper). */
private fun writeC(buf: CharArray, s: String) {
    for (x in s.indices) buf[x] = s[x]
    buf[s.length] = 0.toChar()
}

private const val RETRIEVE = 0 // vendor defines.h:83 (emitted-C constant)
