package mumps.interp

/**
 * PRO-arm translation of interp-arith.cpp::<misc> (B3 unit 66, rank
 * 267). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): the misc unit is the file residue (unclaimed lines in
 * file order) plus the small function bodies (physical loc < 15)
 * appended - interp-arith.cpp::<misc>, cpp, loc 118, comment_ratio
 * 0.451, pointer_marks 28, control_marks 9, score 85.2, batch 3 -
 * exact match to wp1/study-subset/units.csv row 267. The residue is
 * the file header/include/#define banner plus the section banner
 * comments; the small functions are floatstring (13), add (4),
 * add1 (6), add2 (7), add3 (6), sub (4), mult (4), divx (4),
 * numcomp1 (9), numcomp2 (4); divi (the integer-division wrapper,
 * interp-arith.cpp:615) also rides in the residue because the study
 * extractor does not claim its #include-macro body.
 *
 * The add/sub/mult/divx/divi bodies are the vendor's textual
 * includes of numSetup.h with 'opcode' pre-defined; the numSetup.h
 * fragment itself is owned by unit 29 (NumSetup.kt, numSetup).
 * This unit resolves five InterpStubs h014 stubs (mult/add/sub/
 * divx/divi, added by unit 63 for parse_) - the definitions below
 * are their implementations - and the Numcomp.kt floatstring stub
 * (owner interp-arith.cpp:597, this unit). numSetup/numcomp/
 * mpsAcos/gcvtWrapper callers now resolve here.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string       : sprintf(bb,"%ld",b) and
 *    sprintf(bb,"%lf",b) (add1/add2/add3) -> Long/Double toString
 *    (C %ld/%lf decimal renderings for in-range values).
 *  - h010 numeric_promotion   : vendor 'return atoll(aa) - bb'
 *    (numcomp1) computes in C long long then truncates to int on
 *    return; the port computes in Long and applies .toInt() at the
 *    return (h010 note; atollL parses the same signed prefix).
 *  - h012 unsigned_byte_domain: all operands are NUL-terminated
 *    CharArray byte domains; the offset parameters model the
 *    vendor's pointer arithmetic (parse_ calls add(pd1+adx,...)).
 *  - h014 out_of_scope_call   : numSetup (unit 29, reference
 *    include/mumpsc/numSetup.h) and gcvtWrapper reused, not
 *    redefined; five InterpStubs stubs and the Numcomp floatstring
 *    stub resolved by moving the definitions to this owner file.
 *  - h015 grammar_collision   : pre-push scan incl. cross-file
 *    top-level names: floatstring moves from Numcomp.kt (stub
 *    removed there), add/sub/mult/divx/divi move from InterpStubs
 *    (stubs removed there); add1/add2/add3/numcomp1/numcomp2 are
 *    new top-level names with no package collisions. The private
 *    NS_* opcode constants restate numSetupDefines.h:1-5 (the
 *    unit-29 copies are private to NumSetup.kt).
 *  - h017 naming              : add/sub/mult/divx/divi/divi-family,
 *    floatstring, numcomp1, numcomp2 keep vendor names; atollL/
 *    numOp are private helpers.
 *
 * MISSED TRIGGER (preprocessor_conditional): the residue carries
 * the GCVT/BIGFLOAT gcvt_wrapper #define chain (interp-arith.cpp:
 * 69-77); the reference build's selected arm is owned by unit 29
 * (NumSetup.kt gcvtWrapper) and is not retranslated here.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - floatstring treats '-' or '+' ANYWHERE in the string as a
 *    digit-continue (not just a leading sign); a pure-integer
 *    string yields f=0 -> false, which gates callers into the
 *    integer paths - mirrored exactly.
 *  - numcomp1's strchr gate only tests aa for '.', so "1e5" vs 5
 *    falls to the atoll branch and truncates at the 'e' - vendor
 *    behavior kept.
 *  - numcomp2 returns the boolean (atof != 0.0) as int 1/0 in the
 *    floatstring branch, else atoi.
 *  - divi is integer division: numSetup's NS_INTDIV arm floors
 *    the floating quotient when an operand looks float - vendor
 *    semantics, kept via numSetup.
 *  - add1/add3 return c (the caller's buffer) per the vendor
 *    char* return; the port returns the buffer reference.
 */

// numSetupDefines.h:1-5 (same values as the private copies in
// NumSetup.kt, restated here for the wrapper calls below).
private const val NS_ADD = 1
private const val NS_SUB = 2
private const val NS_MULT = 3
private const val NS_DIV = 4
private const val NS_INTDIV = 5

/**
 * Shared body of the vendor textual-include wrappers: copies the
 * operands from their offsets into fresh byte domains (the C
 * pointers a+off/b+off), delegates to numSetup (unit 29) and copies
 * the NUL-terminated result back into r at rOff.
 */
private fun numOp(op: Int, a: CharArray, aOff: Int, b: CharArray, bOff: Int,
                  r: CharArray, rOff: Int) {
    val la = strlenC(a, aOff)
    val aa = CharArray(la + 1)
    for (i in 0 until la) aa[i] = a[aOff + i]
    aa[la] = NUL

    val lb = strlenC(b, bOff)
    val bb = CharArray(lb + 1)
    for (i in 0 until lb) bb[i] = b[bOff + i]
    bb[lb] = NUL

    val res = CharArray(512)
    numSetup(op, aa, bb, res)

    val n = strlenC(res)
    for (i in 0 until n) r[rOff + i] = res[i]
    r[rOff + n] = NUL
}

/** interp-arith.cpp::add - void add(const char *a, const char *b, char *c). */
fun add(a: CharArray, aOff: Int, b: CharArray, bOff: Int, r: CharArray, rOff: Int) {
    // #define opcode NS_ADD
    // #include <mumpsc/numSetup.h>
    numOp(NS_ADD, a, aOff, b, bOff, r, rOff)
}

/** interp-arith.cpp::sub - void sub(const char *a, const char *b, char *c). */
fun sub(a: CharArray, aOff: Int, b: CharArray, bOff: Int, r: CharArray, rOff: Int) {
    // #define opcode NS_SUB
    // #include <mumpsc/numSetup.h>
    numOp(NS_SUB, a, aOff, b, bOff, r, rOff)
}

/** interp-arith.cpp::mult - void mult(const char *a, const char *b, char *c). */
fun mult(a: CharArray, aOff: Int, b: CharArray, bOff: Int, r: CharArray, rOff: Int) {
    // #define opcode NS_MULT
    // #include <mumpsc/numSetup.h>
    numOp(NS_MULT, a, aOff, b, bOff, r, rOff)
}

/** interp-arith.cpp::divx - void divx(const char *a, const char *b, char *c). */
fun divx(a: CharArray, aOff: Int, b: CharArray, bOff: Int, r: CharArray, rOff: Int) {
    // #define opcode NS_DIV
    // #include <mumpsc/numSetup.h>
    numOp(NS_DIV, a, aOff, b, bOff, r, rOff)
}

/** interp-arith.cpp::divi - integer division (vendor comment kept). */
fun divi(a: CharArray, aOff: Int, b: CharArray, bOff: Int, r: CharArray, rOff: Int) {
    // #define opcode NS_INTDIV // int division
    // #include <mumpsc/numSetup.h>
    numOp(NS_INTDIV, a, aOff, b, bOff, r, rOff)
}

/**
 * interp-arith.cpp::add1 - char *add1(char const *a, long b, char *c):
 * sprintf(bb,"%ld",b); add(a,bb,c); return c.
 */
fun add1(a: CharArray, aOff: Int, b: Long, c: CharArray, cOff: Int): CharArray {
    val bb = CharArray(512)
    val s = b.toString() // sprintf(bb, "%ld", b)
    for (i in s.indices) bb[i] = s[i]
    bb[s.length] = NUL
    add(a, aOff, bb, 0, c, cOff)
    return c
}

/**
 * interp-arith.cpp::add2 - void add2(long a, long b, char *c):
 * sprintf both operands and add.
 */
fun add2(a: Long, b: Long, c: CharArray, cOff: Int) {
    val aa = CharArray(512)
    val sa = a.toString() // sprintf(aa, "%ld", a)
    for (i in sa.indices) aa[i] = sa[i]
    aa[sa.length] = NUL
    val bb = CharArray(512)
    val sb = b.toString() // sprintf(bb, "%ld", b)
    for (i in sb.indices) bb[i] = sb[i]
    bb[sb.length] = NUL
    add(aa, 0, bb, 0, c, cOff)
}

/**
 * interp-arith.cpp::add3 - char *add3(const char *a, double b, char *c):
 * sprintf(bb,"%lf",b); add(a,bb,c); return c.
 */
fun add3(a: CharArray, aOff: Int, b: Double, c: CharArray, cOff: Int): CharArray {
    val bb = CharArray(512)
    val s = b.toString() // sprintf(bb, "%lf", b)
    for (i in s.indices) bb[i] = s[i]
    bb[s.length] = NUL
    add(a, aOff, bb, 0, c, cOff)
    return c
}

/**
 * interp-arith.cpp::floatstring (line 597) - static inline int
 * floatstring(const char *p): 1 iff the string contains a '.' or an
 * 'e'/'E' (with everything else digits/signs); 0 otherwise.
 *
 * Resolves the Numcomp.kt h014 stub (unit 20 delegated here); same
 * signature (offset defaults to 0 for the numSetup/numcomp gates).
 */
fun floatstring(p: CharArray, off: Int = 0): Boolean {
    var f = false
    var i = off
    while (p[i] != NUL) {
        val ch = p[i]
        if (ch in '0'..'9' || ch == '-' || ch == '+') {
            i++
            continue
        }
        if (ch == '.' || ch == 'e' || ch == 'E') { // tolower(*p)=='e'
            f = true
            i++
            continue
        }
        return false
    }
    return f
}

/**
 * interp-arith.cpp::numcomp1 - int numcomp1(unsigned char *aa, long bb):
 * if aa contains '.', compare as doubles; else atoll(aa) - bb.
 */
fun numcomp1(aa: CharArray, bb: Long): Int {
    var i = 0
    var hasDot = false
    while (aa[i] != NUL) { // strchr((char *) aa, '.')
        if (aa[i] == '.') {
            hasDot = true
            break
        }
        i++
    }
    if (hasDot) {
        val a = strtodC(aa) // atof((char *) aa)
        val b = bb.toDouble()
        return if (a < b) -1 else if (a > b) 1 else 0
    }
    return (atollL(aa) - bb).toInt() // h010: long long -> int
}

/**
 * interp-arith.cpp::numcomp2 - int numcomp2(unsigned char *aa):
 * if floatstring, (atof(aa) != 0.0); else atoi(aa).
 */
fun numcomp2(aa: CharArray): Int {
    if (floatstring(aa)) return if (strtodC(aa) != 0.0) 1 else 0
    return atoiC(aa)
}

/**
 * C atoll over the byte domain, full Long domain (numcomp1's
 * subtraction happens in long long before the int truncation).
 */
private fun atollL(s: CharArray): Long {
    var i = 0
    while (s[i] == ' ' || s[i] == '\t' || s[i] == '\n' || s[i] == '\r') i++
    var neg = false
    if (s[i] == '+' || s[i] == '-') {
        neg = s[i] == '-'
        i++
    }
    var v = 0L
    while (s[i] in '0'..'9') {
        v = v * 10 + (s[i] - '0')
        i++
    }
    return if (neg) -v else v
}
