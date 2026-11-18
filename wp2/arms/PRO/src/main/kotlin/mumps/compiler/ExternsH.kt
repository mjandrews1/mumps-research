package mumps.compiler

/**
 * PRO-arm translation of include/mumpsc/externs.h::<misc> (B7
 * tranche 2, unit 205, rank 124). Translated independently
 * from the pristine vendor source (mumps-14-may-2026.tgz,
 * sha256 621deb25...ffdbe); the study-tool residual chunk
 * (file header/banner, the stdio include, the struct nmes
 * declaration, and the extern block gpad/xpx/Jflg/ierr1/
 * ierr/ctrlc/tpx/iorg/pd1len/sdlim/setpiece/symlen/t0px/t2/
 * nstart/bd/pd1/setname/symflg/v1d/xd; loc 26,
 * comment_ratio 0.587, pointer_marks 4, control_marks 2,
 * score 12.8 - exact match to wp1/study-subset/units.csv
 * row 205 (batch 7).
 *
 * Header residue: no Kotlin declarations emitted in this
 * file. The header's carriers live where their owners placed
 * them:
 *  - struct nmes -> CompilerState.Nmes (owner global owner
 *    seat); extern struct nmes *nstart ->
 *    CompilerState.nstart.
 *  - The extern scalars/arrays all ride the CompilerState
 *    owner object (each property records its externs.h
 *    line): gpad :47, xpx :38, Jflg :49-50, ierr1 :48,
 *    ierr :40, ctrlc :46, tpx :34, iorg :37, pd1len :36,
 *    sdlim :42, setpiece :31, symlen :41, t0px :39, t2 :35,
 *    bd :33, pd1 :43, setname :32, v1d :45, xd :44.
 *  - symflg (externs.h:60) was the ONLY previously
 *    uncarried extern - it is ADDED to CompilerState by this
 *    unit (unsigned char -> Char(0..255) per h012).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext       : the extern block is the
 *    per-translation-unit global set convention - the
 *    CompilerState owner object IS this header's carrier
 *    (one owner object per C translation unit's global set).
 *  - h012 unsigned_byte_domain: bd/pd1/setname/symflg/v1d/xd
 *    are Char(0..255) domains.
 *  - h015 grammar_collision : no new top-level declarations -
 *    pre-push scan clean.
 *  - h017 naming            : every extern keeps its vendor
 *    name on the carrier.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The commented-out duplicate struct nmes *nstart line
 *    stays a comment, as in the vendor.
 *  - The banner's Fortran provenance note ("Some of this code
 *    was originally written in Fortran which will explain the
 *    odd array and label usage, especially arrays beginning
 *    at index 1") is roster text - the arm's index-1 array
 *    conventions follow it.
 */
