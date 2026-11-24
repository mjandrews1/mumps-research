package daf

// Unit 240 (DAF B8 T3, complex): interp-parse.cpp::cannon
// (35 LOC, score 37.3): the numeric literal canonicalizer (the
// interpreter-sys1 twin): folds leading +/- signs (toggling j),
// trims trailing fraction zeros and a bare trailing dot, strips
// leading zeros, and finally expands a trailing E/e exponent by
// repeated decimal multiplication (mult by "10" or ".1"), per the
// vendor's fixpoint string arithmetic (vendor
// interp-parse.cpp:215, the 35-LOC compact form, physically
// verified this session - the dump asserts physical loc 35 = the
// roster row). Cross-arm note: the DOC arm's landed cannon body
// is the compiler-sys1.c 71-LOC astyle-expanded twin
// (statement-identical walk-for-walk); THIS arm's roster row is
// the interp-parse compact form, so THIS render translates that
// body directly.
// Consults (2026-11-24T21:22:00Z, nominal): the mult helper
// (owner compiler-sys1.c:1075 area, the decimal string
// multiplier) is NOT a corpus unit anywhere in units.csv: RAISED
// named stub multDaf below (the findDaf named-seat precedent);
// atoi -> the DafCRuntimeT2 seat; the unsigned char a[] parameter
// mutates in place exactly as the C array (the CharArray
// carrier); the strcpy self-shifts render as file-local
// left-shift/copy helpers. Quirks kept and noted: the char
// tmp[25] exponent gather buffer has NO bound test in the
// vendor (an exponent longer than 24 digits overflows silently -
// the carrier is guarded at 24 per the bounds-walk precedent);
// the '-'/'+/'.' folding nested ifs preserved verbatim; the
// mult(a,".1",a) negative-exponent loop.
// Dafny: EXEMPT (in-place string arithmetic over the mult seat;
// journal row).

fun cannonD(a: CharArray) {
    var i = 0
    var j = 0
    val tmp = CharArray(25)

    // while(a[i]=='+'||a[i]=='-') { if '-' toggle j; i++ }
    while (a[i] == '+' || a[i] == '-') {
        if (a[i] == '-') {
            if (j == 0) j = 1 else j = 0
        }
        i++
    }

    if (i > 0) {
        if (j == 0) copyFromCannonD(a, 0, i) // strcpy(a,&a[i]);
        else {
            a[0] = '-'
            if (i > 1) copyFromCannonD(a, 1, i) // strcpy(&a[1],&a[i]);
        }
    }

    j = -1
    while (a[i] == '.' || (a[i] >= '0' && a[i] <= '9')) {
        if (a[i] == '.') j = i
        i++
    }

    if (i == 0) {
        a[0] = '0'
        a[1] = NUL_D
        return
    }

    if (a[i] != 'E' && a[i] != 'e') a[i] = NUL_D

    if (a[i] == NUL_D) {
        if (j >= 0) {
            i--
            while (a[i] == '0') { // for (i--; a[i]=='0'; i--)
                a[i] = NUL_D
                i--
            }
            if (a[i] == '.') a[i] = NUL_D
            if (i == 0) {
                a[0] = '0'
                a[1] = NUL_D
            }
        }

        if (a[0] == '-') j = 1 else j = 0

        while (a[j] == '0') shiftLeftCannonD(a, j)

        if (a[j] == NUL_D) {
            a[0] = '0'
            a[1] = NUL_D
        }

        if (a[j] == '.' && a[j + 1] == NUL_D) a[j] = '0'
        return
    }

    if (a[i] != 'e' && a[i] != 'E') return

    a[i] = NUL_D
    i++
    if (a[i] == '-') {
        i++
        j = 1
    } else {
        j = 0
        if (a[i] == '+') i++
    }

    var k = 0
    // for (k=0; (tmp[k]=a[i])!=0&&(tmp[k]>='0'&&tmp[k]<='9'); k++,i++)
    while (k < 24) { // the tmp[25] bound the vendor lacks (guard)
        val c = a[i]
        if (c == NUL_D || !(c >= '0' && c <= '9')) break
        tmp[k] = c
        k++
        i++
    }

    tmp[k] = NUL_D
    i = atoi(DafCRuntime.cstring(tmp))
    if (i > 0) {
        if (j == 0) {
            for (jj in 1..i) multDaf(a, "10".toCharArray(), a)
        } else {
            for (jj in 1..i) multDaf(a, ".1".toCharArray(), a)
        }
    }
}

// mult(char a[], char b[], char c[]) - raised named stub (owner
// compiler-sys1.c::mult, the decimal string multiplier; not a
// corpus unit).
fun multDaf(a: CharArray, b: CharArray, c: CharArray): Unit =
    throw NotImplementedError(
        "mult: out-of-scope owner (compiler-sys1.c::mult; not a corpus unit)")

// strcpy(&a[dst], &a[srcOff]) - file-local in-place copy
private fun copyFromCannonD(a: CharArray, dst: Int, srcOff: Int) {
    var s = srcOff
    var d = dst
    while (s < a.size && a[s] != NUL_D) {
        a[d] = a[s]
        d++
        s++
    }
    a[d] = NUL_D
}

// strcpy(&a[j], &a[j+1]) - file-local left-shift
private fun shiftLeftCannonD(a: CharArray, j: Int) {
    var i = j + 1
    var d = j
    while (i < a.size && a[i] != NUL_D) {
        a[d] = a[i]
        d++
        i++
    }
    a[d] = NUL_D
}
