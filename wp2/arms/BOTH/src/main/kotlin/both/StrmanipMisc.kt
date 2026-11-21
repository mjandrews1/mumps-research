package both

// Unit 141 (BOTH B5 T2, core): interp-strmanip.cpp::<misc> (36 LOC).
// Batch_plan ordinal 141: core, LOC 36, score 28.0. The strmanip
// residual chunk: the file banner and include block (not carried -
// residue-banner rule per the T1 unit-124/125 precedent), the void
// sigint(int) prototype (owner roster unit 254, batch 7, noted),
// and three bodies: rindex, strcatx, lpad. The substr/xindex
// section banners name units of later batches (substr roster 45
// batch 7; xindex roster 114 batch 9) and are noted only.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused / adapted: rindex's returned
//     pointer-into-buffer folds to an Int offset (-1 for NULL);
//     strcatx is the mutable C array on the out side; lpad writes
//     the strout array directly.
//   h015 grammar_collision - hit: the '\x01' control byte of strcatx
//     renders as Char(1) escape text ('\u0001'); the banner's block
//     comments are not carried (no star-slash text leaks into KDoc).
//   h017 naming - hit: rindex/strcatx/lpad keep vendor names: the
//     rindex-vs-libc collision is a pure top-level Kotlin fold
//     (no libc in the sandbox), journaled.
//
// Quirks kept (ledgered):
//   - rindex starts at strlen(s) inclusive: a c of 0 matches at the
//     terminator first, so rindex(s, 0) returns s.length (the C
//     pointer-to-NUL seat). Kept and journaled.
//   - lpad returns WITHOUT touching strout when k <= 0: the caller's
//     strout is left unterminated in C (UB on read). Kept verbatim;
//     the Kotlin CharArray stays unwritten, journaled as the
//     caller-discipline note.
//   - strcatx's C form copies then appends '\x01' and NUL; the
//     commented-out strcat variant stays a comment (vendor).

// char *rindex(char *s, int c) - the backward scan, offset fold.
internal fun rindex(s: String, c: Int): Int {
    var j = s.length // i = strlen(s);
    while (j >= 0) {
        if (c == (if (j < s.length) s[j].code else 0)) {
            return j // &s[j] -> offset
        }
        j--
    }
    return -1 // NULL
}

// inline void strcatx(unsigned char * out, unsigned char * in) -
// append in to out at its NUL, then '\x01', then NUL.
internal fun strcatx(out: CharArray, input: CharArray) {
    // for (; *out!=0; out++);
    var p = 0
    while (p < out.size && out[p] != 0.toChar()) p++
    // for (; *in!=0; in++) *(out++)=*in;
    var q = 0
    while (q < input.size && input[q] != 0.toChar()) {
        out[p] = input[q]
        p++
        q++
    }
    // *(out++)='\x01'; *out='\0';
    out[p] = '\u0001'
    if (p + 1 < out.size) out[p + 1] = 0.toChar()
}

// void lpad(unsigned char strin[], unsigned char strout[], short l)
// - right-shift strin into strout, left-padding spaces. Physical
// LOC 9 lines; the arrays stay C-shaped: indices are computed
// against the NUL-terminated content, not the array bounds.
internal fun lpad(strin: CharArray, strout: CharArray, l: Int) {
    val j = strlenC(strin) - 1 // j = strlen(strin) - 1;
    val k = l - j - 1 // k = (l - j - 1);
    if (k <= 0) {
        return // vendor quirk: strout untouched, see header
    }
    var i = j
    while (i >= 0) { // for (i = j; i >= 0; i--) strout[i+k] = strin[i];
        strout[i + k] = strin[i]
        i--
    }
    strout[j + k + 1] = 0.toChar()
    for (m in 0 until k) strout[m] = ' ' // for (i = 0; i < k; i++) strout[i] = ' ';
}

// local strlen over a NUL-terminated CharArray (lpad's only need).
internal fun strlenC(buf: CharArray): Int {
    var i = 0
    while (i < buf.size && buf[i] != 0.toChar()) i++
    return i
}
