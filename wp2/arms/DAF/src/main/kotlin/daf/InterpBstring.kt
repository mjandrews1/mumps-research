package daf

/*
 * DAF batch-9 unit 248 (roster row 248; interp-bstring.cpp::bstrcat,
 * simple, LOC 18, score 4.2). Consult 2026-11-24T22:30:30Z.
 *
 * The "bstring" library: a struct of length + char array (the
 * malloc-free "better string" carrier of bstring.h). This render
 * lands the bstring carrier and the bstrcat member body; the rest
 * of interp-bstring.cpp rides the file-forward residue (roster
 * kin of this file's misc row, when drawn).
 *
 * Divergences/quicks:
 *  - STR_MAX: vendor strmax.h:3 pins 32000; the arm const
 *    STR_MAX_DAF (DafCRuntime.kt:20) pins 500 with a "defines.h"
 *    blame comment that is inaccurate (strmax.h owns the define).
 *    The 500 const is arm-internal (memory-scaled scratch, the
 *    unit-188 precedent) and is DISCLOSED for PI adjudication in
 *    the batch-9 seats row; no change rides this render.
 *  - sigint(100) relays to the landed sigintD (unit 211).
 */

// struct bstring { int length; char string[]; } - length + payload
// (the vendor header's malloc-free carrier; Kotlin mirror).
class DBstringD(val length: Int, val string: CharArray)

// new_bstring("") twin: the vendor constructor helper (unreachable
// dummy return path carries one; here the empty carrier serves).
fun newBstringEmptyD(): DBstringD {
    val a = CharArray(STR_MAX_DAF)
    a[0] = NUL_D
    return DBstringD(0, a)
}

// struct bstring bstrcat(left, right) - the concat member.
fun bstrcatDaf(left: DBstringD, right: DBstringD): DBstringD {
    val catlength = left.length + right.length
    if (catlength > STR_MAX_DAF) {
        // vendor: fprintf(stderr, "String too long - aborting.\n");
        // sigint(100);
        System.err.print("String too long - aborting.\n")
        sigintD(100)
    }
    val result = newBstringEmptyD()
    var i = 0
    // strncpy(result.string, left.string, left.length)
    while (i < left.length) {
        result.string[i] = left.string[i]
        i++
    }
    result.string[i] = NUL_D
    // strncat(result.string, right.string, right.length)
    var j = 0
    while (j < right.length) {
        result.string[i + j] = right.string[j]
        j++
    }
    result.string[i + j] = NUL_D
    return DBstringD(catlength, result.string)
}
