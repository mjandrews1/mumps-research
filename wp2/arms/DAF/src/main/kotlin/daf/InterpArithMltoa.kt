package daf

/*
 * DAF batch-10 unit 291 (roster row 291; interp-arith.cpp::Mltoa,
 * LOC 31, ptr 3, score 19.5). Consult 2026-11-25T15:06:30Z.
 *
 * RELEASES the mltoaDaf raise (SysDump.kt stub deleted; the
 * SysDumpD/Interpret/Misc6 flock caller seats keep the name and
 * resolve to the real body here).
 *
 * Fold notes: long -> Long; the vendor digit build (t[] = "0123..."
 * digit string, S_SIZE 128 back-to-front walk) renders verbatim
 * over the CharArray out-param; the cout << "Integer overflow"
 * banners fold to System.out prints (the cout-to-System.out
 * precedent); sigint(100) stays the sigintD seat; the LONG_MIN /
 * LONG_MAX edge checks use the Kotlin Long.MIN_VALUE/MAX_VALUE
 * pins (same 64-bit domain); the i<0 negate-overflow recheck is
 * kept verbatim (i = -i; if (i<0) overflow banner).
 *
 * H-015: ASCII; no raw dollars in comments; trailing newline.
 * Dafny: EXEMPT (numeric-to-string conversion; journal row).
 */

// void Mltoa(long i, unsigned char *p) - interp-arith.cpp
fun mltoaDaf(pid: Long, p: CharArray): Unit {
    val S_SIZE = 128 // #define S_SIZE 128 (function-local)
    val t = "0123456789" // static char t[] = "0123456789"

    var i = pid
    var f = 0 // long j,m,f=0; (j,m kept by the loop below)
    var pp = 0 // char *p walking head

    if (i < 0) {
        f = 1
        i = -i
        if (i < 0) {
            System.out.print("Integer overflow\n") // cout <<
            sigintD(100) // overflow
        }
    }

    if (i <= Long.MIN_VALUE || i >= Long.MAX_VALUE) {
        System.out.print("Integer overflow\n")
        sigintD(100)
    }

    if (i < 10) {
        if (f == 1) { p[pp] = '-'; pp++ }
        p[pp] = t[i.toInt()]
        pp++
        p[pp] = NUL_D
        return
    }

    // char s[S_SIZE]; s[S_SIZE-1] = '\0';
    val s = CharArray(S_SIZE)
    s[S_SIZE - 1] = NUL_D

    // for (p1 = s+S_SIZE-2; p1 != s; p1--) { ... if (!i) break; }
    var p1 = S_SIZE - 2
    while (p1 > 0) {
        val m = (i % 10).toInt() // m = i%10
        i = i / 10
        s[p1] = t[m]
        if (i == 0L) break
        p1--
    }

    // if (f) *(--p1) = '-';  strcpy(p, p1);
    if (f == 1) {
        p1--
        s[p1] = '-'
    }

    // strcpy(p, &s[p1])
    var q = 0
    while (p1 + q < S_SIZE && s[p1 + q] != NUL_D) {
        p[q] = s[p1 + q]
        q++
    }
    p[q] = NUL_D
}
