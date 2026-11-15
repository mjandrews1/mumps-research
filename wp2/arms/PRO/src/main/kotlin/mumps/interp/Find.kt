package mumps.interp

/**
 * PRO-arm translation of interp-sym.cpp::_find (pilot B1, unit 6).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h006 out_param_holder    : out is written in place and the C API
 *    shape (caller-owned buffer) survives batch integration -> the
 *    CharArray out-param is the holder.
 *  - h009 format_string       : sprintf(out, "%d", i) collapses to the
 *    exact decimal string of i (no formatting remainder).
 *  - h010 numeric_promotion   : the C (short) cast of i into xindex is
 *    an explicit toShort(); the int return re-widens.
 *  - h012 unsigned_byte_domain: out/in/key/begin char domains map to
 *    NUL-terminated CharArrays.
 *  - h014 out_of_scope_call   : xindex (owner interp-strmanip.cpp, not
 *    a corpus unit) -> InterpStubs.kt.
 *  - h015 grammar_collision   : pre-push scan caught raw VT/FF control
 *    bytes in the local atoi helper; rewritten as escapes.
 *  - h017 naming              : _find keeps the vendor name (the Kotlin
 *    keyword 'in' is kept as a backticked parameter).
 */
fun _find(out: CharArray, `in`, key: CharArray, begin: CharArray) {
    var i = atoiC(begin)
    if (i < 0) i = 0

    if (key[0] != NUL) {
        // C: if ((i = xindex(in, key, (short) i)) > 0) i += strlen(key);
        i = xindex(`in`, key, i.toShort()) // h014 stub; h010 short cast
        if (i > 0) i += strlenC(key)
    }

    if (i > strlenC(`in`) + 1) i = 0

    writeCstring(out, i.toString()) // h009: sprintf "%d" collapse
}
