package mumps.mdh

// ----------------------------------------------------------------------------
// (B10 unit 292) interp-arith.cpp::Mltoa (vendor reference lines
// 84-128). Identity: units.csv row 292 - interp-arith.cpp::Mltoa,
// cpp, loc 31, comment_ratio 0.088, pointer_marks 3,
// control_marks 8, score 19.5 - exact match.
// ----------------------------------------------------------------------------

/**
 * void Mltoa(long i, unsigned char * p): long-to-decimal C-string
 * converter hand-rolled over a digit table (no itoa/sprintf): the
 * negative-input arm flips the sign flag and re-negates (the C
 * LONG_MIN overflow test rides the same seat), the LONG_MIN/MAX
 * bounds arm sigint(100)-aborts, single digits take the fast path,
 * and everything else fills the S[128] scratch downward from the
 * NUL and strcpy's the window into the caller's buffer.
 *
 * Seat: the raised MdhStubs stub signature is kept (fun Mltoa(n:
 * Long, dst: CharArray), h015 note - the seat lives in mdh per the
 * stub package; the SysDump/GlobalExtract/GlobalFind/
 * LibmpscppMisc7 callers bind unchanged via import mumps.mdh.Mltoa).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : dst is a C-string CharArray buffer
 *    (NUL-closed); s[128] is the file-private scratch carrier
 *    (S_SIZE 128, the vendor's stack array is per-call in modern
 *    C; the Kotlin render keeps a fresh array per call - same
 *    observable behavior, static-scratch semantics kept only in
 *    the vendor's own note).
 *  - h010 numeric_radix      : i%10 / i/10 decimal walk kept.
 *  - h012 unsigned_byte_domain: p is an unsigned char carrier
 *    (the prototype says unsigned char *, dst: CharArray).
 *  - h014 out_of_scope_call  : cout renders System.out.print; the
 *    vendor's "Integer overflow" text kept verbatim; sigint is
 *    the DONE mumps.interp seat (Sigint.kt:136).
 *  - h015 grammar_collision  : same name as the removed MdhStubs
 *    stub; public signature kept, callers bind unchanged.
 *  - h017 naming             : Mltoa keeps the vendor spelling.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The LONG_MIN re-negation check happens INSIDE the i<0 arm
 *    BEFORE the bounds arm (i = -i; if (i<0) overflow) - the C
 *    two's-complement shape, kept exactly.
 *  - The bounds arm tests i <= LONG_MIN || i >= LONG_MAX AFTER
 *    the sign fix - the vendor's >= (not >) makes LONG_MAX itself
 *    an overflow; kept verbatim, not "fixed".
 *  - The fast path i<10 writes the '-' (if flagged), the digit,
 *    and the NUL, then returns without touching the scratch.
 *  - The downward fill starts at s[S_SIZE-2] (S_SIZE-1 is the
 *    NUL), breaks on the first zero quotient, and the '-' (if
 *    flagged) lands at --p1; strcpy(p, p1) then copies the tail
 *    window INCLUDING the trailing NUL - kept exactly as the
 *    strcpy tail over the CharArray window.
 */
import mumps.interp.sigint

private const val S_SIZE = 128 // vendor: #define S_SIZE 128
private const val LONG_MIN = java.lang.Long.MIN_VALUE
private const val LONG_MAX = java.lang.Long.MAX_VALUE

private val T = "0123456789".toCharArray() // vendor: static char t[]

fun Mltoa(n: Long, dst: CharArray) {
    var i = n
    var f = 0 // vendor: long f=0

    if (i < 0) {
        f = 1
        i = -i
        if (i < 0) { // LONG_MIN re-negation overflow
            print("Integer overflow") // cout << "Integer overflow\n"
            sigint(100) // overflow
        }
    }

    if (i <= LONG_MIN || i >= LONG_MAX) {
        print("Integer overflow") // cout << "Integer overflow\n"
        sigint(100) // overflow
    }

    var dp = 0 // p cursor

    if (i < 10) { // fast path
        if (f == 1) dst[dp++] = '-'
        dst[dp++] = T[i.toInt()]
        dst[dp] = '\u0000'
        return
    }

    val s = CharArray(S_SIZE) // vendor: char s[S_SIZE]
    s[S_SIZE - 1] = '\u0000'
    var p1 = S_SIZE - 2
    var ii = i
    while (true) {
        val m = (ii % 10).toInt()
        ii /= 10
        s[p1] = T[m]
        p1--
        if (ii == 0L) break
    }

    if (f == 1) {
        s[p1] = '-'
        p1--
    }

    // vendor: strcpy((char *) p, p1) - copy the window incl. NUL
    var src = p1 + 1
    var k = 0
    while (true) {
        val c = s[src]
        dst[k++] = c
        if (c == '\u0000') break
        src++
    }
}
