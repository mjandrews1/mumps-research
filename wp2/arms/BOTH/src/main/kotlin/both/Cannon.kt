package both

// Unit 222 render: interp-parse.cpp::cannon (roster 222, batch 8,
// 45 LOC). void cannon(char a[]) - the numeric canonicalizer:
// folds the sign ladder, strips the leading zeros and the
// trailing fraction zeros, and expands a trailing E exponent
// by repeated mult (10 / .1).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h013 dead_store - hit: the `if (a[i]!='e'&&a[i]!='E')
//     return;` guard after the a[i]==0 block is dead (the only
//     fall-through carries an E/e byte - kept in shape,
//     ledgered).
//   h014 out_of_scope_call - reused: mult is a NEW TODO stub
//     raised here (owner: the interp-arith.cpp residue family
//     roster 67, batch 3 pending - the add/sub stub twins in
//     MpstimeMain); atoi folds to atolC(cStringOf()).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: cannon keeps vendor name and case
//     (the compiler-parse.c prototype ledger pin; no Kotlin
//     collision); the strcpy-in-place folds ride local helpers.
//   h007 char_buffer - reused: char tmp[25] rides a local
//     CharArray; the 25 cap clamps the exponent scan (UB
//     quantization family; ledgered).
//
// Quirks kept (ledgered):
//   - the sign walk counts '-' parity into j then rewrites the
//     sign byte at a[0] when negative (the '+' ladder kept).
//   - the trailing-zero strip walks i BACKWARD from the last
//     digit (i-- BEFORE the test - the post-decrement shape).
//   - the leading-zero strip uses strcpy(&a[j],&a[j+1]) INSIDE
//     a while loop (the repeated in-place left shift; kept via
//     the helper).
//   - the "all zeros" case lands "0" at a[0..1] twice via two
//     different guards (kept, both paths).
//   - the exponent mult loop calls mult(a,"10",a) / mult(a,
//     ".1",a) i times (kept; the literals seat as locals).

// void mult(char *a, char *b, char *c) - owner: the
// interp-arith.cpp residue family (the add/sub stub twins,
// MpstimeMain pins; roster-67, batch 3 pending).
// TODO-stub (h014).
internal fun mult(a: CharArray, b: CharArray, c: CharArray) {
    TODO("stub: owner interp-arith.cpp::mult (roster-67 residue family, batch 3)")
}

// strcpy at offset: copy the srcOff..NUL run to dstOff.
private fun strcpyAtC(a: CharArray, dstOff: Int, srcOff: Int) {
    var s = srcOff
    var d = dstOff
    while (a[s] != '\u0000') {
        a[d++] = a[s++]
    }
    a[d] = '\u0000'
}

// strcpy(&a[j], &a[j+1]) - the one-step in-place left shift.
private fun shiftLeftAtC(a: CharArray, j: Int) {
    var s = j + 1
    var d = j
    while (a[s] != '\u0000') {
        a[d++] = a[s++]
    }
    a[d] = '\u0000'
}

// void cannon(char a[]) - interp-parse.cpp
internal fun cannon(a: CharArray) {
    var i = 0
    var j = 0

    // while (a[i]=='+'||a[i]=='-') sign parity walk
    while (a[i] == '+' || a[i] == '-') {
        if (a[i] == '-') {
            if (j == 0) j = 1 else j = 0
        }
        i++
    }
    if (i > 0) {
        if (j == 0) {
            strcpyAtC(a, 0, i)
        } else {
            a[0] = '-'
            if (i > 1) strcpyAtC(a, 1, i)
        }
    }

    j = -1
    while (a[i] == '.' || (a[i] >= '0' && a[i] <= '9')) {
        if (a[i] == '.') j = i
        i++
    }
    if (i == 0) {
        a[0] = '0'
        a[1] = '\u0000'
        return
    }
    if (a[i] != 'E' && a[i] != 'e') a[i] = '\u0000'

    if (a[i] == '\u0000') {
        if (j >= 0) {
            i--
            while (a[i] == '0') {
                a[i] = '\u0000'
                i--
            }
            if (a[i] == '.') a[i] = '\u0000'
            if (i == 0) {
                a[0] = '0'
                a[1] = '\u0000'
            }
        }
        if (a[0] == '-') j = 1 else j = 0
        while (a[j] == '0') shiftLeftAtC(a, j)
        if (a[j] == '\u0000') {
            a[0] = '0'
            a[1] = '\u0000'
        }
        if (a[j] == '.' && a[j + 1] == '\u0000') a[j] = '0'
        return
    }

    if (a[i] != 'e' && a[i] != 'E') return // dead (h013)

    a[i] = '\u0000'
    i++
    if (a[i] == '-') {
        i++
        j = 1
    } else {
        j = 0
        if (a[i] == '+') i++
    }

    val tmp = CharArray(25)
    var k = 0
    while (a[i] != '\u0000' && a[i] >= '0' && a[i] <= '9' && k < 24) {
        tmp[k] = a[i]
        k++
        i++
    }
    tmp[k] = '\u0000'
    i = atolC(cStringOf(tmp)).toInt()

    if (i > 0) {
        val ten = charArrayOf('1', '0', '\u0000')
        val dot1 = charArrayOf('.', '1', '\u0000')
        if (j == 0) {
            for (jj in 1..i) mult(a, ten, a)
        } else {
            for (jj in 1..i) mult(a, dot1, a)
        }
    }
}
