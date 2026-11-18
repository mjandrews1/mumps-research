package mumps.mdh

import mumps.interp.Msv
import mumps.interp.cstringAt
import mumps.interp.strcatC
import mumps.interp.fcn
import mumps.interp.mglobal
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::global::Soundex (B7 unit
 * 210, units.csv data row 210: loc 17, score 8.7,
 * complexity_rank 80, simple stratum; vendor libmpscpp.cpp:4200-
 * 4219). The mstring member twin at :4182 (loc 18, " $zzsoundex"
 * via zfcn) is a separate vendor unit NOT in the corpus rows -
 * noted for the mis-tag ledger.
 *
 * Vendor shape: global::Soundex() retrieves the global's value
 * via Mglobal(RETRIEVE,...) into gtmp (f==0 raises the
 * GLOBAL_NOT_FOUND exception), builds m = "$zzsoundex" +
 * \xce + gtmp + \xcf, strcpy's m into svPtr->v1d at offset 1,
 * runs the function dispatcher fcn(svPtr), and returns bd at
 * offset 1.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h003 state_macro      : svPtr reads MdhState.svPtr (unit 31
 *    carrier); GlobalExceptionCode -> MdhState.
 *  - h007 char_buffer      : gtmp STR_MAX and the v1d/bd offset-1
 *    carriers are NUL-terminated byte buffers (h012 domain).
 *  - h009 format_string    : none (no sprintf in this body).
 *  - h012 unsigned_byte_domain: the \xce/\xcf argument-delimiter
 *    bytes are 206.toChar()/207.toChar() code points.
 *  - h013 dead_store       : none - f is live in both its tests.
 *  - h014 out_of_scope_call: Mglobal -> mglobal (InterpStubs
 *    4-arg stub, the mdh-family binding precedent, RETRIEVE=0
 *    defines.h:83); Concat (Mstring member, real reuse);
 *    MumpsGlobalException/MdhState.globalExceptionCode (MdhStubs
 *    carriers); strcpy -> writeCstring; fcn (unit 25 real seat,
 *    interp package import); bd/v1d read via cstringAt.
 *  - h015 grammar_collision: the dollar byte in "$zzsoundex"
 *    rides the DOLLAR Char(36) carrier (SQL_Table precedent);
 *    the \xce/\xcf escapes render as 206/207 code points, not
 *    text. Scan clean - no backtick, double-underscore, em dash
 *    or !! in this file.
 *  - h017 naming           : Soundex keeps the vendor name; the
 *    global member renders as a Global extension (u202 MakeGlobal
 *    precedent - the carrier class seat binds unchanged).
 *  - h018 reference_build_visibility: none - no conditionals.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The Concat chain reassigns m each step (m = m.Concat(...))
 *    - kept as reassignments.
 *  - f's RETRIEVE result is tested only for 0; other negative
 *    codes fall through to the build (vendor behavior).
 */
fun Global.Soundex(): Mstring {
    val svPtr: Msv = MdhState.svPtr
    val gtmp = CharArray(STR_MAX)

    val f = mglobal(RETRIEVE, toCbuf(ref), gtmp, svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor: m = "$zzsoundex"; m=m.Concat(\xce); m=m.Concat(gtmp);
    //         m=m.Concat(\xcf); - the Concat chain folds into the
    //         strcatC build (u147 GlobalTranslate precedent; the
    //         Mstring.Concat owner is not a corpus unit).
    val tmp = CharArray(STR_MAX)
    writeCstring(tmp, DOLLAR + "zzsoundex")
    strcatC(tmp, XCE.toString())
    strcatC(tmp, cstringAt(gtmp)) // invoking global's value
    strcatC(tmp, XCF.toString())

    // strcpy(&svPtr->v1d[1], m.data.c_str());
    writeCstring(svPtr.v1d, cstringAt(tmp), 1)

    fcn(svPtr)

    // m = (char *) &svPtr->bd[1];
    return Mstring(cstringAt(svPtr.bd, 1))
}

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. Fcn.kt)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

private val DOLLAR = 36.toChar() // vendor dollar byte (h015)
private val XCE = 206.toChar() // vendor \xce argument-open byte
private val XCF = 207.toChar() // vendor \xcf argument-close byte

/** per-file String -> byte-buffer carrier (h014, MakeGlobal precedent) */
private fun toCbuf(s: String): CharArray {
    val b = CharArray(STR_MAX)
    for (i in s.indices) b[i] = s[i]
    b[s.length] = mumps.interp.NUL
    return b
}
