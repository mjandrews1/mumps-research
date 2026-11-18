package mumps.mdh

import mumps.interp.Msv
import mumps.interp.cstringAt
import mumps.interp.strcatC
import mumps.interp.fcn
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::mstring::Translate(mstring,
 * mstring) (B7 unit 211, units.csv data row 211: loc 15, score
 * 7.7, complexity_rank 57, simple stratum; vendor libmpscpp.cpp:
 * 4224-4244). The one-argument mstring twin at :4246 is a
 * separate vendor unit not in this corpus row (mis-tag ledger
 * note); the global-class sibling Translate(a1) is DONE (B5 unit
 * 147, GlobalTranslate.kt) - the Mstring members are distinct
 * from the global-class siblings of the same names (the
 * Mstring.kt :61-67 header note).
 *
 * Vendor shape: Translate(a1, a2) builds m = "$translate" +
 * \xce + data + \xd0 + a1 + \xd0 + a2 + \xcf, strcpy's m into
 * svPtr->v1d at offset 1, runs fcn(svPtr), and returns bd at
 * offset 1.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h003 state_macro      : svPtr reads MdhState.svPtr (unit 31
 *    carrier).
 *  - h007 char_buffer      : the v1d/bd offset-1 carriers are
 *    NUL-terminated byte buffers (h012 domain).
 *  - h009 format_string    : none (no sprintf in this body).
 *  - h012 unsigned_byte_domain: the \xce/\xd0/\xcf
 *    argument-delimiter bytes are 206/208/207 code points.
 *  - h013 dead_store       : none - m is live through the chain.
 *  - h014 out_of_scope_call: Concat (Mstring member extension,
 *    MdhStubs real seat); strcpy -> writeCstring; fcn (unit 25
 *    real seat, interp package import); bd/v1d read via
 *    cstringAt.
 *  - h015 grammar_collision: the dollar byte in "$translate"
 *    rides the DOLLAR Char(36) carrier (SQL_Table precedent);
 *    the escapes render as code points, not text. Scan clean -
 *    no backtick, double-underscore, em dash or !! in this file.
 *  - h017 naming           : Translate keeps the vendor name;
 *    the mstring member renders as an extension on the Mstring
 *    carrier (the MstringEval precedent) - the (Mstring,
 *    Mstring) arity keeps it distinct from the Global sibling.
 *  - h018 reference_build_visibility: none - no conditionals.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The Concat chain reassigns m each step - kept.
 */
fun Mstring.Translate(a1: Mstring, a2: Mstring): Mstring {
    val svPtr: Msv = MdhState.svPtr

    // vendor: m = "$translate"; m=m.Concat(\xce); m=m.Concat(data);
    //         m=m.Concat(\xd0); m=m.Concat(a1); m=m.Concat(\xd0);
    //         m=m.Concat(a2); m=m.Concat(\xcf); - the Concat chain
    //         folds into the strcatC build (u147 GlobalTranslate
    //         precedent; the Mstring.Concat owner is not a corpus
    //         unit).
    val tmp = CharArray(STR_MAX)
    writeCstring(tmp, DOLLAR + "translate")
    strcatC(tmp, XCE.toString())
    strcatC(tmp, this.c_str()) // the member's own data
    strcatC(tmp, XD0.toString())
    strcatC(tmp, a1.c_str())
    strcatC(tmp, XD0.toString())
    strcatC(tmp, a2.c_str())
    strcatC(tmp, XCF.toString())

    // strcpy(&svPtr->v1d[1], m.data.c_str());
    writeCstring(svPtr.v1d, cstringAt(tmp), 1)

    fcn(svPtr)

    // m = (char *) &svPtr->bd[1];
    return Mstring(cstringAt(svPtr.bd, 1))
}

private val DOLLAR = 36.toChar() // vendor dollar byte (h015)
private val XCE = 206.toChar() // vendor \xce argument-open byte
private val XD0 = 208.toChar() // vendor \xd0 argument-separator byte
private val XCF = 207.toChar() // vendor \xcf argument-close byte

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. Fcn.kt)
