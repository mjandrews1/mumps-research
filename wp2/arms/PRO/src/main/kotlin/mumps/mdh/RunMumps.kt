package mumps.mdh

import mumps.interp.Msv
import mumps.interp.NUL
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::RunMumps (B3 unit 74, rank
 * 193). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 2860-2905.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::RunMumps, cpp, loc 33, comment_ratio
 * 0.029, pointer_marks 6, control_marks 6, score 25.9, batch 3 -
 * exact match to wp1/study-subset/units.csv row 193.
 *
 * RunMumps(svPtr, exp, key, ...): evaluates a Mumps expression with
 * the caller's arguments bound as local variables first (sym_ 0 =
 * set), Interpret(exp), then unbinds them (sym_ 1 = clear) with the
 * same name/value pairs; returns the Interpret error code k.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext          : svPtr->ERROR (stateVector.h:69)
 *    added to the Msv carrier by this unit.
 *  - h007 char_buffer          : tmp[STR_MAX] key/expr buffer.
 *  - h012 unsigned_byte_domain : tmp and the sym_ name/value
 *    arguments are byte domains.
 *  - h014 out_of_scope_call    : sym_ (MdhStubs stub, symbol-table
 *    machinery, promoted CharArray?/null return, unit 25) and
 *    interpret (InterpStubs stub, interpreter core) reused;
 *    strtok -> local strtokC helper (static pointer becomes a
 *    holder offset).
 *  - h015 grammar_collision    : pre-push scan incl. cross-file
 *    top-level names: new top-level RunMumps + private strtokC -
 *    no collisions.
 *  - h017 naming               : RunMumps keeps the vendor name;
 *    the C va_list/va_arg machinery folds into a Kotlin vararg
 *    (makeGlobal precedent, unit 31); NULL sentinel = null.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The FIRST strtok pass splits the key on ','; the SECOND pass
 *    (after Interpret) splits on ' ' (vendor line 2898,
 *    strtok(NULL," ") - a copy-paste bug against the ',' of the
 *    first pass) - kept verbatim: the unbind pass re-tokenizes the
 *    same tmp buffer with the space delimiter.
 *  - The loop bound j counts commas in the key + 1, but the loop
 *    breaks early on a NULL vararg - kept.
 *  - svPtr->NOERR=1 suppresses error printing during Interpret -
 *    set before the call, not restored after (vendor behavior).
 */
fun RunMumps(svPtr: Msv, exp: String, key: String, vararg vals: String?): Int {
    val tmp = CharArray(STR_MAX)
    var p1: Int // strtok holder offset (-1 = exhausted)
    var j = 1

    writeCstring(tmp, key) // strcpy(tmp,key)
    for (t in 0 until tmp.indexOfOrEnd()) {
        if (tmp[t] == ',') j++
    } // count args

    p1 = strtokC(tmp, ",")
    var next = 0

    for (i in 0 until j) {
        val x = vals.getOrNull(next)
        next++
        if (x == null) break

        val nameBuf = CharArray(STR_MAX)
        writeCstring(nameBuf, cstringAtOff(tmp, p1))
        val xBuf = CharArray(STR_MAX)
        writeCstring(xBuf, x)
        sym_(0, nameBuf, xBuf, svPtr)
        p1 = strtokNext(tmp, ",") // strtok(NULL,",")
    }

    svPtr.NOERR = 1
    writeCstring(tmp, exp) // strcpy(tmp,exp)
    val k = interpret(cstringAtOff(tmp, 0), svPtr)
    svPtr.ERROR = k

    writeCstring(tmp, key) // va_start/strcpy(tmp,key)
    p1 = strtokC(tmp, ",")
    next = 0

    for (i in 0 until j) {
        val x = vals.getOrNull(next)
        next++
        if (x == null) break

        val nameBuf = CharArray(STR_MAX)
        writeCstring(nameBuf, cstringAtOff(tmp, p1))
        val xBuf = CharArray(STR_MAX)
        writeCstring(xBuf, x)
        sym_(1, nameBuf, xBuf, svPtr)
        p1 = strtokNext(tmp, " ") // strtok(NULL," ") - vendor space delimiter
    }

    return k
}

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)

/** C strtok(str, delim) over the byte domain; returns token start offset or -1. */
private fun strtokC(str: CharArray, delim: String): Int {
    strtokCPos = 0 // fresh call: static holder resets (strtok shape)
    return strtokNext(str, delim)
}

private var strtokCPos = 0 // C strtok's static pointer holder

private fun strtokNext(str: CharArray, delim: String): Int {
    var i = strtokCPos
    while (str[i] != NUL && str[i] == delim[0]) i++
    if (str[i] == NUL) {
        strtokCPos = i
        return -1
    }
    val start = i
    while (str[i] != NUL && str[i] != delim[0]) i++
    if (str[i] == NUL) {
        strtokCPos = i
    } else {
        str[i] = NUL
        strtokCPos = i + 1
    }
    return start
}

/** cstringAt over the byte domain from an offset. */
private fun cstringAtOff(b: CharArray, off: Int): String {
    val sb = StringBuilder()
    var i = off
    while (b[i] != NUL) {
        sb.append(b[i])
        i++
    }
    return sb.toString()
}

/** tmp.indexOfOrEnd - the NUL position of the byte domain. */
private fun CharArray.indexOfOrEnd(): Int {
    var i = 0
    while (this[i] != NUL) i++
    return i
}
