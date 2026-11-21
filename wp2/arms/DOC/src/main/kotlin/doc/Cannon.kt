package doc

// Unit 84 (B3 tranche 3 / sub-push A): compiler-sys1.c::cannon (batch
// 3, complex stratum; LOC 71, score 40.9). The numeric literal
// canonicalizer: folds leading +/- signs (toggling j), trims trailing
// fraction zeros and a bare trailing dot, strips leading zeros, and
// finally expands a trailing E/e exponent by repeated decimal
// multiplication (mult by "10" or ".1"), per the vendor's fixpoint
// string arithmetic. The mult helper of compiler-sys1.c is NOT a
// corpus unit anywhere in units.csv: H-014 Stubs.mult seat named for
// that owner; atoi reuses the unit render of Find.kt. The unsigned
// char a[] parameter is the in-place mutated numeric buffer (H-007
// mutable scratch: the CharArray carrier mutates in place exactly as
// the C array). Quirks kept and noted: the char tmp[25] exponent
// gather buffer has NO bound test in the vendor (an exponent longer
// than 24 digits overflows silently - carrier guard below per the
// H-007 bounds-walk precedent); the - sign folding nested ifs are
// preserved verbatim.
// the strcpy self-shifts (strcpy(&a[j], &a[j+1])) render as the
// CharArray left-shift operator below. Naming per H-017 (the vendor
// name cannon is kept verbatim; no collision).

private fun shiftLeftFrom(a: CharArray, j: Int) {
    // strcpy(&a[j], &a[j+1]) without overlap protection (dest<src)
    var i = j + 1
    var d = j
    while (i < a.size && a[i] != NUL) {
        a[d] = a[i]
        d++
        i++
    }
    a[d] = NUL
}

private fun copyFrom(a: CharArray, dst: Int, srcOff: Int) {
    // strcpy(&a[dst], &a[srcOff])
    var s = srcOff
    var d = dst
    while (s < a.size && a[s] != NUL) {
        a[d] = a[s]
        d++
        s++
    }
    a[d] = NUL
}

fun cannon(a: CharArray) {
    var i = 0
    var j = 0
    val tmp = CharArray(25)

    while (a[i] == '+' || a[i] == '-') {
        if (a[i] == '-') {
            if (j == 0) j = 1 else j = 0
        }
        i++
    }

    if (i > 0) {
        if (j == 0) copyFrom(a, 0, i)
        else {
            a[0] = '-'
            if (i > 1) copyFrom(a, 1, i)
        }
    }

    j = -1
    while (a[i] == '.' || (a[i] >= '0' && a[i] <= '9')) {
        if (a[i] == '.') {
            j = i
        }
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

        if (a[0] == '-') j = 1 else j = 0

        while (a[j] == '0') shiftLeftFrom(a, j)

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
        if (a[i] == '+') {
            i++
        }
    }

    var k = 0
    // vendor gather loop: (tmp[k] = a[i]) != 0 && digit - the tmp[25]
    // buffer has no bound in the vendor; guarded here (H-007)
    while (k < 24) {
        val c = a[i]
        if (c == NUL || !(c >= '0' && c <= '9')) break
        tmp[k] = c
        k++
        i++
    }

    tmp[k] = NUL
    val nExp = atoi(CstringOps.cstring(tmp))
    if (nExp > 0) {
        if (j == 0) {
            for (jj in 1..nExp) Stubs.mult(a, "10", a)
        } else {
            for (jj in 1..nExp) Stubs.mult(a, ".1", a)
        }
    }
}

// ---------------- Unit 243 row-note (B8 tranche 3) -----------
//
// Unit 243 (B8 tranche 3): interp-parse.cpp::cannon (batch 8,
// complex stratum; LOC 35, score 37.3, rank 220; units.csv row
// 223) is a VERIFICATION-ONLY row against THIS render. The two
// vendor bodies are STATEMENT-IDENTICAL modulo formatting and
// spacing: compiler-sys1.c:cannon (the 71-LOC astyle-expanded
// form this unit-84 render translates) and interp-parse.cpp:
// cannon at :215 (the 35-LOC compact form) implement the same
// canonicalizer walk-for-walk (the sign fold + j toggle, the
// i==0 zero-out early return, the '.' index capture, the
// E-check, the trailing-zero trim + bare-dot strip + zero
// collapse of the a[a[i]==0] arm, the leading-zero strip, the
// exponent gather into tmp[25], the atoi(tmp) fold with the
// mult(a,"10",a) / mult(a,".1",a) loop pair). The seat
// adjudication precedes this row: the unit-83-family PatrnInterp
// render (the interp-parse.cpp::patrn owner) already binds its
// three cannon call sites to THIS landed render (the PatrnSys1
// twin does likewise), single-name seat per H-014 and H-017; this
// row CLOSES the roster claim by verifying the interpreter-file
// twin body is the same function, so the cross-file seat is
// correct, no regrade and no new body per the unit-186
// verification-row precedent. Consult H-014 hit-reuse: the
// landed render covers both vendor twins; row-note only.
