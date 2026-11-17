package mumps.interp

/**
 * PRO-arm translation of interp-piece.cpp::<misc> (B4 unit 103,
 * rank 236). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 1-207 (the whole file: the study tool's residual
 * chunk - the file's single _piece function rides the residual
 * because the tool's head matcher does not claim it, so the
 * pre-registered unit identity is the file-level misc slice; exact
 * metrics match below).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-piece.cpp::<misc>, cpp, loc 140,
 * comment_ratio 0.108, pointer_marks 3, control_marks 22, score
 * 52.2, batch 4 - exact match to wp1/study-subset/units.csv row
 * 236.
 *
 * _piece(out, in, key, start, end, setpiece, lhs, svPtr): the
 * dollar-piece engine for the interpreter - scans 'in' for the
 * start..end pieces delimited by 'key' (RHS extract into out), or
 * for setpiece==1 splices the lhs value into the variable named
 * by out (local via sym_(0,...) or global via Mglobal(STORE,...)).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro: printf -> System.out.print.
 *  - h006 out_param_holder  : no scalar out-params here; out/lhs
 *    are reference buffers as in C (see h012).
 *  - h007 char_buffer      : all parameters are NUL-terminated
 *    byte buffers; the local tmp/tmp2/tmp3 are STR_MAX scratch
 *    buffers (32,000, strmax.h:3).
 *  - h012 unsigned_byte_domain: out/in/key/start/end/lhs hold
 *    bytes 0-255; the piece-delimiter scan is a byte-domain
 *    comparison kept exact.
 *  - h014 out_of_scope_call: xindex (InterpStubs stub, owner
 *    interp-strmanip.cpp::xindex); strmove (InterpStubs stub,
 *    owner interp-strmanip.cpp::strmove, offset-0 calls);
 *    substr (InterpStubs stub, owner interp-strmanip.cpp::substr,
 *    batch-7 unit); sym_ (InterpStubs stub, batch-10 owner);
 *    mglobal STORE offset variant (mglobalAt stub); sigint
 *    (InterpStubs stub).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level _piece - no collisions; the
 *    vendor printf literal contains a dollar byte ("dollar-Piece
 *    error") - the dollar rides a file-private Char(36) constant
 *    (h015 action).
 *  - h017 naming          : _piece keeps the vendor name (the
 *    leading underscore is legal Kotlin); the parameter 'in' is a
 *    Kotlin hard keyword and is renamed 'in_' (DirectRead
 *    precedent, unit 24; only keyword rename in this unit).
 *  - h018 preprocessor_conditional: the file's #define SQLITE
 *    (line 30) and #define _INTERP_ (line 44) are both LIVE in
 *    the reference build config; the commented-out
 *    "#define SYMDEBUG" (line 32) is commented vendor text, not
 *    an arm - no excluded arms to document; the CSTR/USTR cast
 *    macros (lines 45-46) are type casts with no Kotlin carrier.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The setpiece==1 splice writes the prefix via substr(tmp2,
 *    tmp, 1, k-1), then sets tmp[k]=1 and tmp[k+1]=0, then
 *    strcat(tmp,lhs) from the FIRST NUL - so for k>1 the 1 at
 *    tmp[k] is overwritten by the strcat when lhs is non-empty
 *    and the byte tmp[k-1] (set to NUL by substr's terminator)
 *    becomes the splice point; kept byte-for-byte.
 *  - The "piece1" fall-through leaves m == strlen(tmp2)+1 on the
 *    missing-end-delimiter path (vendor 161-165) - kept.
 *  - The double "if (setpiece == 0)" at piece1 (vendor 169-173,
 *    nested inside its own condition) is vendor redundancy kept
 *    as a single branch with this note.
 *  - k/m/n arithmetic is Int; the xindex start argument rides a
 *    Short param in the vendor - widened explicitly (h010).
 */

private const val STORE_OP = 1 // vendor defines.h:84
private const val STR_MAX = 32000 // vendor strmax.h:3

/** vendor printf literal dollar byte (h015). */
private val DOLLARC = 36.toChar()

/** C atoi over the NUL-terminated view (leading spaces/sign/digits). */
private fun atoiC(b: CharArray, off: Int = 0): Int {
    var i = off
    while (b[i] == ' ') i++
    var neg = false
    if (b[i] == '+' || b[i] == '-') {
        neg = b[i] == '-'
        i++
    }
    var v = 0
    while (b[i] in '0'..'9') {
        v = v * 10 + (b[i] - '0')
        i++
    }
    return if (neg) -v else v
}

/** C strcat(dst, src) over NUL-terminated buffers; returns new length. */
private fun strcatC(dst: CharArray, src: CharArray, srcOff: Int = 0): Int {
    var d = 0
    while (dst[d] != NUL) d++
    var s = srcOff
    while (src[s] != NUL) {
        dst[d] = src[s]
        d++; s++
    }
    dst[d] = NUL
    return d
}

fun _piece(out: CharArray, in_: CharArray, key: CharArray, start: CharArray,
           end: CharArray, setpiece: Int, lhs: CharArray, svPtr: Msv) {
    val tmp2 = CharArray(STR_MAX)
    val tmp3 = CharArray(STR_MAX)
    var k: Int
    var l: Int
    var m: Int
    var n: Int

    if (cstringAt(start) == "-1") { // no start or end - goto p1
        k = 1
        l = 1
    } else {
        strmove(tmp2, 0, start, 0)
        k = atoiC(tmp2) // start

        l = k // default end is same as start

        if (cstringAt(end) != "-1") { // end?
            strmove(tmp2, 0, end, 0)
            l = atoiC(tmp2)
        }

        if (k < 0 || l < k) { // check
            System.out.print("** " + DOLLARC + "Piece error start=" + k + " end=" + l + "\n")
            sigint(100)
        }
    }

    // p1:
    strmove(tmp2, 0, in_, 0)
    strmove(tmp3, 0, key, 0)

    if (tmp2[0] == NUL || tmp3[0] == NUL || k > l || l <= 0) {
        if (setpiece == 0) writeCstring(out, "")
        return
    }

    m = 0
    n = 0

    while (n < k - 1) {
        m = xindex(tmp2, tmp3, m.toShort()) + 1

        if (m == 1) {
            if (setpiece == 0) writeCstring(out, "")
            return
        }

        n++
    }

    if (k != 1) k = m + cstringAt(tmp3).length - 1

    m = 0
    n = 0
    var atPiece1 = false
    while (n != l) {
        m = xindex(tmp2, tmp3, (m + 1).toShort())

        if (m <= 0) {
            m = cstringAt(tmp2).length + 1
            atPiece1 = true // goto piece1
            break
        } else n++
    }

    if (!atPiece1) m = m - k

    // piece1:
    if (m == 0 && setpiece == 0) {
        // vendor nests "if (setpiece == 0)" inside its own
        // condition (169-173) - kept as the single branch
        writeCstring(out, "")
        return
    }

    if (setpiece == 1) {
        val tmp = CharArray(STR_MAX)
        strmove(tmp, 0, in_, 0)
        substr(tmp2, 0, tmp, 1, k - 1)
        tmp[k] = Char(1)
        tmp[k + 1] = NUL
        strcatC(tmp, lhs)
        if (k + m - 1 < cstringAt(tmp2).length)
            strcatC(tmp, tmp2, k + m - 1)
        if (out[0] != '^') sym_(0, out, 0, tmp, 0, svPtr)
        else mglobalAt(STORE_OP, out, 0, tmp, 0, svPtr)
        return
    }

    substr(tmp2, 0, out, k, m)
    return
}
