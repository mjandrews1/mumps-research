package mumps.compiler

import java.io.Writer
import mumps.interp.NUL

/**
 * PRO-arm translation of compiler-mumps.c::<misc> (B7 tranche 2,
 * unit 196, rank 205). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 1949-1998: LevCheck (1949), atCopy
 * (1956), EP (1978) - the 57-physLOC residue chunk; the rest of
 * compiler-mumps.c is owned by corpus units outside batch 7.
 *
 * Identity verified against the study tool: compiler-mumps.c::
 * &lt;misc&gt;, c, loc 57, comment_ratio 0.034, pointer_marks 4,
 * control_marks 12, score 29.3 - exact match to
 * wp1/study-subset/units.csv row 196 (batch 7).
 *
 * RESOLVES three Stubs.kt h014 seats (one TAG CORRECTION):
 *  - levCheck: the stub's "not a corpus unit" tag was wrong
 *    (u127 rpm precedent) - this residue chunk owns it (vendor
 *    1949); the Doty.kt call site binds unchanged.
 *  - atCopy: raised by the B1/B3 units (IfCode/SetCode/WriteCode
 *    call it); the StringBuilder carrier and the j=2 write
 *    contract stay (see below); call sites bind unchanged.
 *  - EP: raised by the B1 units; the ZcommandsCode.kt m/e arms
 *    (B7 unit 192) call it; call sites bind unchanged.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro : fprintf(Out, ...) -> out.write
 *    verbatim (EP).
 *  - h003 state_macro        : xpx/xd/LabelsStart/MAIN_flg are
 *    CompilerState globals (compiler-globals-ext.h; xpx Long -> 
 *    Int index narrowing, h010 note).
 *  - h007 char_buffer        : atCopy's tmp rides the stub-era
 *    StringBuilder (the vendor's unsigned char tmp[] with the
 *    j=2 start contract: every caller pre-loads the first two
 *    chars before the call, so writes begin after them; the C's
 *    trailing tmp[j]=0 terminator is the StringBuilder end -
 *    h015 fold, journaled).
 *  - h012 unsigned_byte_domain: xd reads are byte-domain; the
 *    quote/backslash escapes are byte copies.
 *  - h013 dead_store         : none.
 *  - h014 out_of_scope_call  : epi (Epi.kt, B7 unit 189, real
 *    seat) called by EP; no other out-of-scope callees.
 *  - h015 grammar_collision  : the EP label walk uses the
 *    nullable-walker idiom (no double bang); new top-levels
 *    levCheck/atCopy/EP re-bind the stub seats - no package
 *    collisions.
 *  - h017 naming             : LevCheck -> levCheck (Kotlin
 *    lowercase fn; the stub-era seat), atCopy/EP keep the vendor
 *    spellings.
 *  - h018 reference_build_visibility: no ifdefs in these bodies.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - atCopy escapes BOTH quotes and backslashes (doubled), and
 *    the loop bound i < at_flg caps at the @-position: kept.
 *  - EP's flg arm emits the _eps dispatch BEFORE the label
 *    compares and falls through to the ErrorMessage emission when
 *    no label matches - kept.
 *  - EP's comment misspells "subfucntion" - kept in the KDoc
 *    roster only (it is a C comment, not emitted).
 */

/** vendor 1949: int LevCheck(short ExecName, short DoRetX, short
 *  DoRet[]) - returns 0 if ExecName is in the do-return table. */
fun levCheck(ExecName: Short, DoRetX: Short, DoRet: ShortArray): Int {
    for (i in 0 until DoRetX) {
        if (DoRet[i] == ExecName) return 0
    }
    return 1
}

/** vendor 1956: void atCopy(int at_flg, unsigned char tmp[]) -
 *  the @-argument copier with quote/backslash escaping; the
 *  StringBuilder carrier keeps the stub-era shape (j=2 contract:
 *  the caller pre-loads the first two chars). */
fun atCopy(atFlg: Int, tmp: StringBuilder) {
    val st = CompilerState
    var i = st.xpx.toInt() // vendor: i = xpx (h010 Long->Int narrowing)
    while (st.xd[i] != NUL && i < atFlg) {
        val c = st.xd[i]
        if (c != '"' && c != '\\') {
            tmp.append(c)
        } else if (c == '"') {
            tmp.append('\\')
            tmp.append('"')
        } else {
            tmp.append('\\')
            tmp.append('\\')
        }
        i++
    }
    // vendor: tmp[j] = 0; - the StringBuilder end is the
    // terminator (h015 fold, journaled)
}

/** vendor 1978: void EP(int flg, short ExecName, long pid) - the
 *  subfucntion entry point router (vendor comment spelling). */
fun EP(flg: Int, ExecName: Short, pid: Long) {
    val st = CompilerState
    val out = st.out

    if (flg != 0) {
        out.write("\tgoto _epilogue;\n")
        out.write("_eps:\tif (strlen((const char *) offset)==0) goto _fcn;\n")

        if (st.LabelsStart != null) {
            var p1: Txt? = st.LabelsStart
            while (p1 != null) {
                out.write("\tif (strcmp((const char *) offset,(const char *) \"" +
                    p1.name + "\")==0) goto " + p1.name + ";\n")
                p1 = p1.next
            }
        }
        out.write("\tErrorMessage(\"Error - entry point label not found\", " +
            "svPtr->LineNumber);\n")
    }

    epi(ExecName, pid) // terminate prev function
    st.MAIN_flg = 0 // not in main function
}
