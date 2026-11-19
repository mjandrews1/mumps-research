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
