package mumps.compiler

/**
 * PRO-arm translation of compiler-parse.c::<misc>
 * (batch-8 unit 220, units.csv data line 220, rank 277).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), the file's
 * full residue after the copyright banner (misc physical loc
 * 210, the largest header residue in the batch: banner,
 * includes, the #define block, the prototype roster, and the
 * file-scope globals).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-parse.c::<misc>, c, loc 210,
 * comment_ratio 0.062, pointer_marks 29, control_marks 18,
 * score 108.8, rank 277, batch 8 - exact match to
 * wp1/study-subset/units.csv line 220.
 *
 * Residue contents (vendor lines):
 *  - 2-27: the GPL/copyright banner (CopyrightNotice carrier
 *    family, cf. EpiMisc unit 92).
 *  - 38-53: includes (mumpsc/memsize.h, stdio.h, sys/stat.h,
 *    fcntl.h, time.h, stdlib.h, string.h, ctype.h; the
 *    UNIX==0 DOS arm at 48-53 is EXCLUDED on the reference
 *    build, h018).
 *  - 55-139: the #define block - SymStore..SymName (55-62),
 *    RETRIEVE..PREVIOUS (65-72), the command codes JOB..LBL
 *    (73-98), the operator codes OPEN..PREDICATEVAR
 *    (100-130), and CodedOpen..ORDER (131-139).  Every
 *    constant consumed by an already-rendered unit rides that
 *    unit's file-private const precedent (Match unit 190's
 *    PUNCT/DOLLAR carriers, ZcommandsCode unit 192's arm
 *    tables, Epi/Fcninit unit 189/195 seats); no new global
 *    Kotlin consts are hoisted here.
 *  - 141-189: the includes/externs roster (externs.h at 141;
 *    extern short MaxTmp, extern int iiitmp, extern char
 *    ErrChar at 143-145 - all carried on CompilerState by
 *    their owners) and the prototype block (149-178: ExpandQS,
 *    patrn, cvar, patrn1, xindex, errmod_, memcheck, add, sub,
 *    mult, divx, numcomp, cannon, MyFunc, sym_, keyfix,
 *    fixstr, Out, Extract2, Extract1, Extract2a, the Expand*
 *    family, Extract, ExpandQsub).  Rendered seats: Cannon
 *    (u62), Extract (u198), ExpandQS/ExpandData/ExpandGet/
 *    ExpandNext/ExpandOrder (B3), FixStr, Extract2 (B4);
 *    ExpandText is row 235 and Extract2a row 231 this batch;
 *    the remaining prototypes name batch-9/10 owners and stay
 *    on their Stubs.kt seats (h014).
 *  - 180: unsigned char *SSS,*TTT - the file-scope scratch
 *    cursors (parse_ body use at vendor 198/418-424/847-848);
 *    carried on CompilerState by THIS unit (see below).
 *  - 184-189: extern struct dcls cvars[CVARMAX] / extern int
 *    cvarx - already carried on CompilerState (cvars/cvarx/
 *    CVARMAX, externs.h owner).
 *  - 2039/2066: the SYSTEM==DOS if/endif pair is EXCLUDED on
 *    the reference build (h018).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext: SSS/TTT added to CompilerState (the
 *    pointer folds to a buffer+offset pair, Msv.bigKey carrier
 *    precedent); the extern roster resolves to existing
 *    CompilerState members.
 *  - h013 dead_store : nothing executable in the residue.
 *  - h014 out_of_scope_call: unrendered prototypes keep their
 *    Stubs.kt seats with owners named.
 *  - h015 grammar_collision: this file introduces no new
 *    top-level Kotlin declarations (the only new identifiers
 *    are the CompilerState SSS/TTT members) - no collisions.
 *  - h017 naming     : header residue; the carrier keeps the
 *    vendor SSS/TTT names.
 *  - h018 reference_build_visibility: the DOS/UNIX==0 and
 *    SYSTEM==DOS arms are dead on the reference build
 *    (excluded); the main include/define roster is live.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - QUOTE is defined as a raw apostrophe ('\'' at 99) - the
 *    Match unit's PUNCT_CHARS carrier precedent; no raw
 *    apostrophe is emitted here (documentation only).
 *  - TAB 9 collides numerically with ELSE 12 vs ORDERNEXT 12
 *    and QUIT 8 vs XNEXT 8 etc. - the vendor reuses numeric
 *    codes across tables (kept as roster documentation).
 *  - PREDICATEVAR '~' (130) and the 206-209 Coded* markers
 *    match the XCE/XCF carriers of units 188 (zBlanks).
 */
// Header residue: no Kotlin declarations emitted (see KDoc).
