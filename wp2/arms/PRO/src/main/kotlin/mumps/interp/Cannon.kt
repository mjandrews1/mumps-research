package mumps.interp

/**
 * PRO-arm translation of interp-parse.cpp::cannon
 * (batch-8 unit 223, units.csv data line 223, rank 220).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 215-256.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-parse.cpp::cannon, cpp, loc 35,
 * comment_ratio 0.028, pointer_marks 0, control_marks 27,
 * score 37.3, rank 220, batch 8 - exact match to
 * wp1/study-subset/units.csv line 223.
 *
 * cannon(char a[]): canonicalize a numeric literal string in
 * place - fold the leading sign, strip leading zeros and
 * trailing fraction zeros, and expand an E exponent by
 * repeated mult() by 10 or .1.
 *
 * This unit RESOLVES the InterpStubs cannon() h014 stub (the
 * stub's "owner interp-parse.cpp::<misc> (B3 unit 75)" tag was
 * a mis-tag: the InterpParseMisc residue named cannon as its
 * own corpus unit - tag CORRECTED here, u141/u148/u169/u219
 * family; the Patrn call sites at :101/:108/:119 bind
 * unchanged).  The compiler-sys1.c::cannon B3 unit 72 is the
 * compiler-side twin (separate seat, h004 one-owner policy).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer : in-place edits on the caller's buffer;
 *    the strcpy(&a[x],&a[y]) self-shifts ride the private
 *    strshift helper (copy from src to the NUL, incl. it).
 *  - h012 unsigned_byte_domain: a/tmp are Char(0..255) domains.
 *  - h013 dead_store  : the trailing `return;` after the last
 *    arm is kept (vendor form).
 *  - h014 out_of_scope_call: mult (ArithMisc seat) and atoi
 *    (atoiC) reused unmodified.
 *  - h015 grammar_collision: top-level cannon on the interp
 *    side - no collision (the compiler-side twin lives in
 *    another package; the InterpStubs stub is removed).
 *  - h017 naming      : cannon keeps the vendor name.
 *  - h018 preprocessor_conditional: none in this body.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The sign fold: an ODD number of leading '-' leaves one
 *    '-' (j toggles), and the copy drops the consumed signs.
 *  - if (i==0) arm writes "0" and returns BEFORE any exponent
 *    handling - kept.
 *  - The zero-stripping loop starts at i-- (past the NUL
 *    written by a[i]=0) and then only strips while a[i]=='0'
 *    - kept verbatim (chars before the fraction point are
 *    untouched because the loop stops at the first non-'0').
 *  - The single '0' in "if (a[j]==0) { a[0]='0'; a[1]=0; }"
 *    arm covers inputs that strip to empty - kept.
 *  - The exponent arm uses a bare `if (i>0) if (j==0) ...`
 *    double-if with the else binding to the INNER if (kept:
 *    negative exponents multiply by ".1", positive by "10").
 */

private const val CANNON_TMP = 25 // vendor: char tmp[25]

/** strcpy(dst+dstOff, src+srcOff): self-shift incl. the NUL. */
private fun strshift(dst: CharArray, dstOff: Int, src: CharArray, srcOff: Int) {
    var d = dstOff
    var s = srcOff
    while (src[s] != NUL) {
        dst[d] = src[s]
        d++
        s++
    }
    dst[d] = NUL
}

fun cannon(a: CharArray) {
    var i = 0
    var j = 0
    val tmp = CharArray(CANNON_TMP)

    // vendor: while (a[i]=='+'||a[i]=='-') { if (a[i]=='-')
    //         if (j==0) j=1; else j=0; i++; }
    while (a[i] == '+' || a[i] == '-') {
        if (a[i] == '-') j = if (j == 0) 1 else 0
        i++
    }

    if (i > 0) {
        if (j == 0) strshift(a, 0, a, i) // vendor: strcpy(a,&a[i]);
        else {
            a[0] = '-'
            if (i > 1) strshift(a, 1, a, i) // vendor: strcpy(&a[1],&a[i]);
        }
    }

    j = -1
    while (a[i] == '.' || (a[i] >= '0' && a[i] <= '9')) {
        if (a[i] == '.') j = i
        i++
    }

    if (i == 0) {
        a[0] = '0'
        a[1] = NUL
        return
    }

    if (a[i] != 'E' && a[i] != 'e') a[i] = NUL

    if (a[i] == NUL) {
        if (j >= 0) {
            i--
            while (a[i] == '0') {
                a[i] = NUL
                i--
            }
            if (a[i] == '.') a[i] = NUL
            if (i == 0) {
                a[0] = '0'
                a[1] = NUL
            }
        }
        j = if (a[0] == '-') 1 else 0
        while (a[j] == '0') strshift(a, j, a, j + 1) // vendor: strcpy(&a[j],&a[j+1]);
        if (a[j] == NUL) {
            a[0] = '0'
            a[1] = NUL
        }
        if (a[j] == '.' && a[j + 1] == NUL) a[j] = '0'
        return
    }

    if (a[i] != 'e' && a[i] != 'E') return

    a[i] = NUL
    i++
    if (a[i] == '-') {
        i++
        j = 1
    } else {
        j = 0
        if (a[i] == '+') i++
    }

    // vendor: for (k=0; (tmp[k]=a[i])!=0&&(tmp[k]>='0'&&
    //         tmp[k]<='9'); (k++,i++));
    var k = 0
    while (true) {
        tmp[k] = a[i]
        if (tmp[k] == NUL || tmp[k] < '0' || tmp[k] > '9') break
        k++
        i++
    }
    tmp[k] = NUL
    i = atoiC(tmp)

    if (i > 0) {
        if (j == 0) {
            val ten = CharArray(3)
            strcpyC(ten, "10")
            for (j2 in 1..i) mult(a, 0, ten, 0, a, 0) // vendor: mult(a,"10",a);
        } else {
            val tenth = CharArray(4)
            strcpyC(tenth, ".1")
            for (j2 in 1..i) mult(a, 0, tenth, 0, a, 0) // vendor: mult(a,".1",a);
        }
    }
}
