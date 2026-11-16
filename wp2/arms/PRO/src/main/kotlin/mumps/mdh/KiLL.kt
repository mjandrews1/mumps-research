package mumps.mdh

import mumps.interp.Msv
import mumps.interp.NUL
import mumps.interp.cstringAt
import mumps.interp.mglobal
import mumps.interp.strlenC
import mumps.interp.strncmpC

/**
 * PRO-arm translation of libmpscpp.cpp::KiLL (pilot B2, unit 37,
 * provisional under deviation C-003). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 7685-7712 (units.csv:
 * 17 LOC). Free function (not a global member; the global::Kill
 * wrapper is a separate unit).
 *
 * The MUMPS Kill intrinsic (vendor spelling KiLL): keyfix the key,
 * trim a trailing subscript delimiter, kill the node, then walk the
 * sibling chain with XNEXT killing every node sharing the key prefix.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : tmp14/tmp15[STR_MAX] buffers
 *    (C-string domains); gtmp is a caller-owned in/out key buffer.
 *  - h012 unsigned_byte_domain: all buffers are Char(0..255)
 *    domains; the subscript delimiter is char 1.
 *  - h014 out_of_scope_call  : keyfix (owner include/mumpsc/keyfix.h
 *    inline, not a corpus unit) and strmove (owner
 *    include/mumpsc/inline.h:38, not a corpus unit) -> new MdhStubs
 *    stubs; Mglobal -> existing InterpStubs stub (GKILL/XNEXT
 *    operation codes); strlen/strncmp resolve to the real B1
 *    CstringOps helpers - reused unmodified.
 *  - h015 grammar_collision  : the vendor locals _k/_j start with an
 *    underscore, reserved for implementation in Kotlin - renamed
 *    k/j with the vendor names kept in comments.
 *  - h017 naming             : KiLL keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The trim test is written on two delimiter bytes and truncates
 *    only the last one (gtmp[k-1] = 0; k--), exactly as the vendor.
 *  - The XNEXT loop kills while the sibling shares the trimmed key
 *    prefix (strncmp of k bytes); any other condition breaks.
 *  - GKILL = 3, XNEXT = 8, RETRIEVE = 0 (defines.h:167/:88/:83) -
 *    file-private constants.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val GKILL = 3 // vendor defines.h:167
private const val XNEXT = 8 // vendor defines.h:88

fun KiLL(gtmp: CharArray, svPtr: Msv) {
    val tmp14 = CharArray(STR_MAX)
    val tmp15 = CharArray(STR_MAX)
    var k: Int // vendor: _k
    var j: Int // vendor: _j

    keyfix(gtmp)
    k = strlenC(gtmp)

    // vendor: if (gtmp[_k-1]==1 && gtmp[_k-2] == 1) gtmp[_k-- -1]=0;
    if (gtmp[k - 1] == '\u0001' && gtmp[k - 2] == '\u0001') {
        gtmp[k - 1] = NUL
        k--
    }

    strmove(tmp14, gtmp)

    mglobal(GKILL, gtmp, tmp15, svPtr)

    while (true) {
        j = mglobal(XNEXT, gtmp, tmp15, svPtr)

        // vendor: if (_j && strncmp((char *)tmp14,gtmp,_k) == 0)
        if (j != 0 && strncmpC(tmp14, 0, cstringAt(gtmp), k) == 0) {
            mglobal(GKILL, gtmp, tmp15, svPtr)
            continue
        }

        break
    }
}
