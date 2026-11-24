package daf

/*
 * DAF batch-9 unit 272 (roster row 272; mpstime.cpp::<misc>,
 * residue, LOC 98, score 11.2). Consult 2026-11-24T22:49:00Z.
 *
 * This roster row is the compiler-generated mpstime.cpp FILE
 * PRELUDE plus the inline-helper tail chunks (floatstring,
 * numcomp2, strxcat, strcatz, SyncClock, set_alarm,
 * _label_lookup, _text_function).
 *
 * PRELUDE LEDGER (no new code): the Apr 2 2025 generated banner,
 * the include ladder, FORPARM STR_MAX / CSTR / UNS / uns / CCHR
 * micro-defines, the DBSem deadlock-prevention global (pin: the
 * SyncClock body below), the Mglobal interface comment block, the
 * SUB-guarded fcns/sysfunc includes, the __label_lookup /
 * __text_function pointer decls and the fcn/zfcn/sym_/makeLock/
 * cleanLocks/releaseLock/eraseLocks/add/sub/mult/divx/divi/
 * _fnumber prototype roster - every name rides its landed or
 * stubbed seat (the unit-182 MpstimeMain render pins this TU's
 * stub lattice).
 *
 * FOLD-IN BODIES:
 * - floatstring: BYTE-IDENTICAL to the interp-arith.cpp:597
 *   static inline already seated as floatstringDaf (Modulo.kt
 *   stub, non-roster) - NO duplicate body; this TU's local copy
 *   rides that seat (row 98 precedent).
 * - numcomp2D: the ONE-ARG sibling of the numcompDaf (aa,bb)
 *   stub - a DIFFERENT body (floatstring gate, atof!=0 or atoi),
 *   rendered here fresh.
 * - strxcat: SEAT RELEASE - the strxcatDaf stub raised at
 *   MpstimeMain.kt:77 (blame "this-TU misc cell") is deleted
 *   with a release note; the real body lands at the same (a, b)
 *   signature the MpstimeMain:205 catx call binds. The arm
 *   returns the C char* tail; the DAF seat returns the joined
 *   String (the row-56 value fold).
 * - strcatz: the same-TU sibling: concat then the trailing \u0001
 *   mark + NUL (the vendor strcatz quirk: the mark byte APPENDED
 *   after the copy, kept verbatim).
 * - SyncClock: DBSem = 1 (the deadlock-prevention latch; the
 *   DBSemD top-level per rows 6/22).
 * - set_alarm: the itimerval disarm arm (tv zeroed) - the JVM
 *   POSIX fold renders the zero-timer ledger (no setitimer on
 *   the JVM; the return-old-seconds value is 0, DOCUMENTED
 *   DIVERGENCE: the socket/alarm family precedent).
 * - _label_lookup/-_text_function: the vendor defaults
 *   (return -1 / NULL) - rendered as the labelLookupDefaultD /
 *   textFunctionDefaultD seats with the leading-underscore
 *   names folded (the unit-182 quirk-5 note: the pointer
 *   assignments stay comment-only there; THESE defaults bind
 *   the __label_lookup family CompileEpi emits).
 * H-015 hygiene: no raw dollars ride the comments.
 * Dafny: EXEMPT (fold-in TU helpers; journal row).
 */

var DBSemD = 0 // int DBSem = 0; deadlock prevention

// inline int numcomp2(unsigned char *aa)
fun numcomp2D(aa: String): Int {
    if (floatstringDaf(aa) != 0) {
        return if (DafCRuntime.strtod(aa) != 0.0) 1 else 0 // atof(aa) != 0.0
    }
    return atoi(aa)
}

// inline char * strxcat(char *a, char *b) - RELEASED seat.
fun strxcatDaf(a: CharArray, b: CharArray): String {
    var out = DafCRuntime.cstring(a) // while (*a) a++;
    out += DafCRuntime.cstring(b) // while ((*(a) = *(b))) { a++; b++; }
    return out
}

// inline char * strcatz(char *out, char *in)
fun strcatzD(out: CharArray, in_: CharArray): CharArray {
    var q = 0
    while (at(out, q) != NUL_D) q++ // while (*out) out++;
    var w = 0
    while (at(in_, w) != NUL_D) {
        out[q] = in_[w]
        q++
        w++
    }
    out[q++] = '\u0001' // *(out++) = '\1';
    out[q] = NUL_D
    return out
}

// void SyncClock()
fun syncClockD() {
    DBSemD = 1
}

// unsigned int set_alarm(unsigned int seconds) - the itimerval
// disarm (zeroed new timer); JVM fold ledger, returns 0.
fun setAlarmD(seconds: Int): Int {
    return 0
}

// int _label_lookup(char *label) - the vendor default.
fun labelLookupDefaultD(label: String): Int {
    return -1
}

// const char * _text_function(int disp) - the vendor default.
fun textFunctionDefaultD(disp: Int): String? {
    return null
}
