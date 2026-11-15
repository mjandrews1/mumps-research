package mumps.interp

import mumps.mdh.strcatx
import mumps.mdh.sym_

/**
 * PRO-arm translation of interp-sysfunc.cpp::<misc> (pilot B1,
 * unit 25). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * Unit scope: per the Study Subset extraction
 * (wp1/study-subset/units.csv, tools/select_study_subset.py), the
 * <misc> unit is the file's residue after the extracted function
 * units - here everything outside getstr1 (batch 5),
 * TimeOut_getstr1 (batch 6), DirectRead (B1 unit 24), sigint
 * (batch 7, so its InterpStubs stub stays), AllocSV (batch 8),
 * Merge (batch 8), GlobalData (batch 7), LocalName (batch 4),
 * GlobalName (B1 unit 13), and unpad (batch 10, h014 stub here):
 * the file header/defines/includes, VariableNotFound,
 * VariableNotFound1, ErrorMessage, LocalData, LocalOrder, LocalNext,
 * GlobalNext, LocalGet, GlobalGet, and fullUnpad.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro : printf -> System.out.print string
 *    building; strcmp -> strncmpC; strcpy -> strcpyC; the strtok
 *    walk in fullUnpad becomes an explicit scan loop with the same
 *    token semantics (delimiters between tokens, empty tokens
 *    skipped, key consumed).
 *  - h008 static_scratch      : vendor's static unsigned char
 *    delim[2]={1,0} and the sprintf/strtok scratch in fullUnpad
 *    become fresh per-call buffers.
 *  - h012 unsigned_byte_domain: all c-string params/buffers are
 *    CharArray byte domains; the strtok delimiter is Char(0x01)
 *    ('\u0001'), LINEFEED is Char(0x0A).
 *  - h014 out_of_scope_call   : sym_ (mumps.mdh.MdhStubs, promoted
 *    to return CharArray? for LocalGet's NULL test), strcatx
 *    (MdhStubs), Mglobal -> mglobal (InterpStubs), sigint
 *    (InterpStubs; batch-7 unit interp-sysfunc.cpp::sigint, not in
 *    B1, stub stays), unpad (InterpStubs stub added; batch-10 unit,
 *    not in B1).
 *  - h015 grammar_collision  : pre-push scan incl. cross-file
 *    top-level name check; all new top-level constants in this file
 *    (LINEFEED, RETRIEVE, NEXT, GPADWIDTH, STR_MAX) are private to
 *    avoid the red-build-#2 class of package collisions. The
 *    \u0024 escape keeps the order() dollar literal out of a Kotlin
 *    string template (comments avoid the raw sign too).
 *  - h017 naming             : all functions keep vendor names; the
 *    C char-star vs unsigned char-star distinction collapses to CharArray
 *    (VariableNotFound vs VariableNotFound1 kept as the vendor's
 *    duplicate pair); Mglobal keeps the arm-wide mglobal stub name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The file header's #define FIX (char *) cast macro has no
 *    Kotlin analog (typed CharArrays) and is dropped with this
 *    comment; #define SQLITE is resolved by the reference build
 *    configuration (defined).
 *  - VariableNotFound and VariableNotFound1 are byte-for-byte
 *    duplicates apart from the parameter type that does not survive
 *    the CharArray collapse; both are kept.
 *  - fullUnpad's first token is strcatx'd into tmp1 WITHOUT unpad
 *    (vendor behavior - only subsequent tokens are unpadded).
 *  - The 'fff' debug printfs in LocalOrder are commented out in the
 *    vendor and stay comments here.
 */

/** interp-sysfunc.cpp:49 - #define LINEFEED '\012'. */
private const val LINEFEED = '\u000A'

/** include/mumpsc/defines.h:204 (region) - #define RETRIEVE 0. */
private const val RETRIEVE = 0

/** include/mumpsc/defines.h:168 (region) - #define NEXT 2. */
private const val NEXT = 2

/** include/mumpsc/defines.h:215 - #define GPADWIDTH 40. */
private const val GPADWIDTH = 40

/** include/mumpsc/strmax.h:3 - file-private per red-build-#2 lesson. */
private const val STR_MAX = 32000

/**
 * interp-sysfunc.cpp:230 - VariableNotFound. Vendor returns
 * (unsigned char *) "" - a zeroed CharArray(1) is the empty c-string.
 */
fun VariableNotFound(LineNumber: Long, var_: CharArray): CharArray {
    System.out.print("\n*** Variable " + cstringAt(var_) +
        " not found in or near line " + LineNumber + "\n")
    sigint(100)
    return CharArray(1)
}

/** interp-sysfunc.cpp:243 - VariableNotFound1 (vendor duplicate, kept). */
fun VariableNotFound1(LineNumber: Long, var_: CharArray): CharArray {
    System.out.print("\n*** Variable " + cstringAt(var_) +
        " not found in or near line " + LineNumber + "\n")
    sigint(100)
    return CharArray(1)
}

/** interp-sysfunc.cpp:251 - ErrorMessage. */
fun ErrorMessage(msg: CharArray, LineNumber: Long) {
    System.out.print("\n*** " + cstringAt(msg) +
        " in or near line " + LineNumber + "\n")
    sigint(100)
}

/** interp-sysfunc.cpp:791 - LocalData. */
fun LocalData(tmp0: CharArray, tmp2: CharArray, svPtr: Msv) {
    sym_(6, tmp0, tmp2, svPtr)
}

/** interp-sysfunc.cpp:798 - LocalOrder. */
fun LocalOrder(tmp0: CharArray, tmp1: CharArray, tmp2: CharArray?, svPtr: Msv) {
    if (tmp2 == null) svPtr.order = 1
    else if (strncmpC(tmp2, 0, "1", 2) == 0) svPtr.order = 1
    else if (strncmpC(tmp2, 0, "-1", 3) == 0) svPtr.order = -1
    else {
        // vendor: ErrorMessage(dollar + "order() second argument incorrect", ...)
        // '\u0024' keeps the dollar sign out of a Kotlin string template (comments avoid the raw sign too).
        val msg = CharArray(64) // h008: fresh scratch
        strcpyC(msg, "\u0024order() second argument incorrect")
        ErrorMessage(msg, svPtr.LineNumber)
    }

    // fff printf("LocalOrder: svPtr->order=%d tmp0=%s\n", svPtr->order, tmp0);

    sym_(33, tmp0, tmp1, svPtr)

    // fff printf("LocalOrder return tmp1=%s\n", tmp1);
}

/** interp-sysfunc.cpp:813 - LocalNext. */
fun LocalNext(tmp0: CharArray, tmp1: CharArray, svPtr: Msv) {
    sym_(3, tmp0, tmp1, svPtr)
}

/** interp-sysfunc.cpp:820 - GlobalNext. */
fun GlobalNext(tmp0: CharArray, tmp1: CharArray, svPtr: Msv) {
    mglobal(NEXT, tmp0, tmp1, svPtr)
}

/**
 * interp-sysfunc.cpp:923 - LocalGet. sym_(11,...) returns NULL when
 * the variable is absent -> the default (tmp2 or empty) is copied.
 */
fun LocalGet(tmp0: CharArray, tmp1: CharArray, tmp2: CharArray?, svPtr: Msv) {
    if (sym_(11, tmp0, tmp1, svPtr) == null) {
        if (tmp2 == null) strcpyC(tmp1, "")
        else strcpyC(tmp1, tmp2)
        return
    }
    return // ans in tmp1
}

/** interp-sysfunc.cpp:935 - GlobalGet. Mglobal returns 0 on failure. */
fun GlobalGet(tmp0: CharArray, tmp1: CharArray, tmp2: CharArray?, svPtr: Msv) {
    if (mglobal(RETRIEVE, tmp0, tmp1, svPtr) == 0) {
        if (tmp2 == null) strcpyC(tmp1, "")
        else strcpyC(tmp1, tmp2)
        return
    }
    return // ans in tmp1
}

/**
 * interp-sysfunc.cpp:969 - fullUnpad: unpad()s every '\u0001'-separated
 * subscript of key EXCEPT the first (vendor behavior), rebuilding key.
 * The vendor strtok walk (delim {1,0}) becomes an explicit scan loop.
 */
fun fullUnpad(key: CharArray, svPtr: Msv) {
    val tmp1 = CharArray(STR_MAX) // vendor: unsigned char tmp1[STR_MAX]=""
    val tmp2 = CharArray(STR_MAX)
    // vendor: static unsigned char delim[2] = {1,0}; - h008: not static here
    val delim = '\u0001'

    var pos = 0
    var first = true
    while (true) {
        // strtok: skip leading delimiters
        while (key[pos] == delim) pos++
        if (key[pos] == NUL) break

        // token runs to the next delimiter or NUL
        val start = pos
        while (key[pos] != delim && key[pos] != NUL) pos++
        val len = pos - start

        val tok = CharArray(len + 1) // h008: fresh scratch
        for (i in 0 until len) tok[i] = key[start + i]
        tok[len] = NUL

        if (first) {
            // first token: strcatx only, NOT unpadded (vendor)
            strcatx(tmp1, tok)
            first = false
        } else {
            strcpyC(tmp2, tok)
            unpad(tmp2, svPtr)
            strcatx(tmp1, tmp2)
        }
    }

    strcpyC(key, tmp1)
}
