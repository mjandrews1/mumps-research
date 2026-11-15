package mumps.interp

/**
 * PRO-arm translation of interp-sysfunc.cpp::GlobalName (pilot B1,
 * unit 13). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 871-911.
 *
 * GlobalName rewrites the \x01 subscript separators in tmp1 in place:
 * with tok==0 into ("...",...)-quoted form; otherwise each separator
 * becomes tok.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h006 out_param_holder    : tmp1 is written in place and the C API
 *    shape (caller-owned buffer) survives batch integration; the
 *    CharArray in/out param is the holder.
 *  - h012 unsigned_byte_domain: tmp1/tmp3 byte domains; the \x01
 *    separator is Char(0x01).
 *  - h013 dead_store          : vendor's int j=999 is never used and
 *    int k counts separators but is never read - both dropped with
 *    this comment, not "fixed".
 *  - h015 grammar_collision  : the raw \x01 control byte is written
 *    as the '\u0001' escape; the quote chars emitted are plain Kotlin
 *    escapes.
 *  - h017 naming             : GlobalName keeps the vendor name.
 */
fun GlobalName(tmp1: CharArray, tok: Char, svPtr: Msv) {
    // h013: int j=999 and int k=0 (separator counter) are dead - dropped.
    var oflg = 0

    val tmp3 = CharArray(STR_MAX)
    var tx = 0

    if (tok == NUL) {
        var i = 0
        while (tmp1[i] != NUL) {
            if (tmp1[i] == '\u0001' && oflg == 0) {
                tmp3[tx++] = '('
                tmp3[tx++] = '\"'
                oflg = 1
            } else if (tmp1[i] == '\u0001' && oflg != 0 && tmp1[i + 1] != NUL) {
                tmp3[tx++] = '\"'
                tmp3[tx++] = ','
                tmp3[tx++] = '\"'
            } else if (tmp1[i] == '\u0001') {
                tmp3[tx++] = '\"'
                tmp3[tx++] = ')'
            } else {
                tmp3[tx++] = tmp1[i]
            }
            i++
        }
    } else {
        var i = 0
        while (tmp1[i] != NUL) {
            if (tmp1[i] == '\u0001' && oflg == 0) {
                tmp3[tx++] = tok
                oflg = 1
            } else if (tmp1[i] == '\u0001') {
                tmp3[tx++] = tok
            } else {
                tmp3[tx++] = tmp1[i]
            }
            i++
        }
    }

    tmp3[tx] = NUL
    strcpyC(tmp1, tmp3)
}

private const val STR_MAX = 32000 // strmax.h:3
