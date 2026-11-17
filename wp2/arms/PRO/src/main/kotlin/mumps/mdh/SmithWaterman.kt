package mumps.mdh

import mumps.interp.Msv
import mumps.interp.NUL
import mumps.interp.atoiC
import mumps.interp.strlenC

/**
 * PRO-arm translation of libmpscpp.cpp::SmithWaterman (B6 tranche
 * C, unit 184, rank 58). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 10351-10368.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): SmithWaterman, cpp, loc 17,
 * comment_ratio 0.056, pointer_marks 2, control_marks 0,
 * score 7.7 - exact match to wp1/study-subset/units.csv row 184
 * (batch 6).
 *
 * char* SmithWaterman(MSV*, ep, s1, s2, sa, sm, gap, mm, ma): the
 * MSV wrapper over sw() (the Smith-Waterman engine, Sw.kt real
 * seat) - space-prefix both sequences into STR_MAX buffers, scan
 * the five integer tuning args (show-align, show-matrix, gap,
 * mismatch, match), run the engine, sprintf percent-d the score
 * into the static 32-byte tmp.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer     : s1a/s2a[STR_MAX] sequence buffers.
 *  - h008 static_scratch  : vendor "static char tmp[32]" persists
 *    across calls - carried on the SmithWatermanScratch object
 *    (Sum u182 / Count u118 precedent).
 *  - h009 format_string   : sscanf percent-d folds to the shared
 *    atoiC CstringOps.kt:35 seat (leading-int semantics);
 *    sprintf(tmp,percent-d) renders via String.format with the
 *    EXPLICIT default Locale (potential-bugs rule satisfied).
 *  - h012 unsigned_byte_domain: all args NUL-terminated byte
 *    domains; the strcpy-to-offset and strlen ride the byte
 *    domain (strlenC seat).
 *  - h014 out_of_scope_call : sw() is the B2 real seat (Sw.kt:83,
 *    same package); svptr/ep are the vendor's dead wrapper
 *    parameters - kept in the signature, unused.
 *  - h015 grammar_collision: pre-push scan incl. cross-file
 *    top-level names: new top-level SmithWaterman - no
 *    collisions.
 *  - h017 naming          : SmithWaterman keeps the vendor
 *    spelling.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The sscanf return is UNCHECKED: a non-numeric tuning arg
 *    leaves the C local uninitialized (UB) - quantized to
 *    atoiC's deterministic 0 (u128 deterministic-UB precedent).
 *  - The wrapper ignores the engine's ShowAlign/ShowMatrix
 *    side effects and returns only the integer score string.
 */
private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22/29)

/** vendor: static char tmp[32] - persists across calls (h008). */
object SmithWatermanScratch {
    val tmp = CharArray(32)
}

fun SmithWaterman(
    svptr: Msv,
    ep: CharArray?,
    s1: CharArray,
    s2: CharArray,
    sa: CharArray,
    sm: CharArray,
    gap: CharArray,
    mm: CharArray,
    ma: CharArray,
): CharArray {
    val tmp = SmithWatermanScratch.tmp
    val s1a = CharArray(STR_MAX)
    val s2a = CharArray(STR_MAX)

    // vendor: s1a[0]=s2a[0]=' '; strcpy(&s1a[1],s1); strcpy(&s2a[1],s2);
    s1a[0] = ' '
    s2a[0] = ' '
    val n1 = strlenC(s1)
    for (k in 0..n1) s1a[k + 1] = s1[k] // strcpy copies the NUL too
    val n2 = strlenC(s2)
    for (k in 0..n2) s2a[k + 1] = s2[k]

    // vendor: sscanf(sa,"%d",&sax); ... (return unchecked)
    val sax = atoiC(sa) // h009: leading-int scan; non-numeric quantized to 0
    val smx = atoiC(sm)
    val gapx = atoiC(gap)
    val mmx = atoiC(mm)
    val max = atoiC(ma)

    // vendor: sax=sw(s1a,s2a,sax,smx,gapx,mmx,max);
    val score = sw(s1a, s2a, sax, smx, gapx, mmx, max)

    // vendor: sprintf(tmp,"%d",sax);
    val s = String.format(java.util.Locale.getDefault(), "%d", score) // h009: explicit default locale = C printf locale semantics
    for (k in s.indices) tmp[k] = s[k]
    tmp[s.length] = NUL

    return tmp
}
