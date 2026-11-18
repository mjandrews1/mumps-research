package mumps.mdh

import mumps.interp.NUL
import mumps.interp.strcatx

/**
 * PRO-arm translation of libmpscpp.cpp::global::MakeGlobal (B7
 * tranche 2, unit 203, rank 140). Translated independently
 * from the pristine vendor source (mumps-14-may-2026.tgz,
 * sha256 621deb25...ffdbe), reference source lines 6963-7000
 * (the variadic reference assembler between the operator()
 * call sites at 2742-2799 and RunMumps at 7002).
 *
 * Identity verified against the study tool:
 * libmpscpp.cpp::global::MakeGlobal, cpp, loc 38,
 * comment_ratio 0.05, pointer_marks 2, control_marks 3,
 * score 14.6 - exact match to wp1/study-subset/units.csv
 * row 203 (batch 7). RESOLVES the MdhStubs makeGlobal stub
 * (B1 unit 31 raised it with the correct batch-7 owner tag) -
 * stub removed; the GlobalCarrier.kt:96 fold call site rebinds
 * to this extension seat (the one-token this. prefix is the
 * member-method adaptation, journal-noted).
 *
 * MakeGlobal(flg, gblnme, key, ...): the variadic subscript
 * assembler. Sref/Sname open from the bare global name
 * (Sref gains the open paren), ArgCount = count(key), and the
 * buf accumulator builds the \x01-separated machine
 * reference (strcatx) from gblnme plus each argument - the
 * flg==1 arm emits sprintf %ld numbers, the flg==0 arm emits
 * the quoted strings into Sref and raw into buf. The trailing
 * Sref comma becomes the close paren; the assembled buf is
 * copied into ref; returns *this.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h006 low_level_pointer : the va_list walk becomes the
 *    vararg vals array (C va_arg(long) in the flg==1 arm is
 *    the u188 Long-invoke fold's pre-stringified value).
 *  - h007 char_buffer       : vendor static char buf[STR_MAX]
 *    is file-scope shared in C; the Kotlin render allocates a
 *    fresh STR_MAX buffer per call (the assembled content is
 *    consumed before return via Sref/ref, so the sharing was
 *    unobservable - u44 fresh-precedent, journal-noted).
 *  - h012 unsigned_byte_domain: buf is a Char(0..255) domain;
 *    the \x01 subscript separator rides strcatx (1.toChar()).
 *  - h014 out_of_scope_call : count is the DONE unit-188 seat
 *    (LibmpscppMisc6.kt:237, direct call); strcatx is the
 *    DONE unit-136 seat (interp InterpStrmanipMisc.kt:88,
 *    import rebind - the u136 precedent); sprintf %ld ->
 *    toLong().toString() (no stdio seat for a numeric fold).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: the extension makeGlobal(Global, Int,
 *    String, String, vararg String) replaces the removed
 *    stub one-for-one - no collisions.
 *  - h017 naming           : MakeGlobal keeps the vendor name
 *    modulo the Kotlin camel fold (global::MakeGlobal is a
 *    C++ member; the extension receiver is the *this carrier).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The strcmp(key,"\"\"") flg=0 reset (an empty-quoted key
 *    forces the string arm) is kept.
 *  - strcpy(ref,buf) copies the \x01-terminated assembly into
 *    the instance ref INCLUDING the trailing separator byte
 *    (the Cosine/Dice/Sim1 SOH-pair strip exists downstream
 *    for exactly this tail) - kept. DIVERGENCE: the unit-14
 *    carrier shape keeps ref a val (the bare name); the
 *    assembled machine reference rides Sref and the
 *    GlobalRef(this, subs) fold product (u31 journal), so the
 *    strcpy(ref,buf) side effect is journal-noted here rather
 *    than re-carried - no caller reads this instance's ref
 *    expecting the assembled form.
 *  - The trailing Sref comma is overwritten with ')' by the
 *    length-1 index (Sref[Sref.length()-1]=')') - rendered as
 *    the dropLast(1)+")" fold (String is immutable).
 *  - The commented-out strcat(Sref,tmp)/strcat(Sref,",")
 *    lines stay comments, as in the vendor.
 */
fun Global.makeGlobal(x: Int, gblnme: String, key: String, vararg vals: String): Global {
    val flg = x // vendor: int flg (the x carrier name keeps the stub seat)

    var sref = gblnme
    Sname = gblnme
    sref = sref + "("

    val j = count(key)
    ArgCount = j
    val buf = CharArray(STR_MAX_MG)
    buf[0] = NUL
    strcatx(buf, toCbufMk(gblnme))

    var flgEff = flg
    if (key == "\"\"") flgEff = 0

    for (i in 0 until j) {
        if (flgEff == 1) {
            // vendor: ix=va_arg(ap,long); sprintf(tmp,"%ld",ix)
            val tmp = vals[i].toLong().toString()
            strcatx(buf, toCbufMk(tmp))
            //strcat(Sref,tmp);
            sref = sref + tmp
            //strcat(Sref,",");
            sref = sref + ","
        } else {
            val xv = vals[i]
            strcatx(buf, toCbufMk(xv))
            sref = sref + "\""
            sref = sref + xv
            sref = sref + "\""
            sref = sref + ","
        }
    }

    Sref = sref.dropLast(1) + ")" // vendor: Sref[Sref.length()-1] = ')'
    return this // vendor: return *this
}

private const val STR_MAX_MG = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)

/** String -> NUL-terminated CharArray bridge (per-file convention). */
private fun toCbufMk(s: String): CharArray {
    val a = CharArray(s.length + 1)
    for (i in s.indices) a[i] = s[i]
    a[s.length] = NUL
    return a
}
