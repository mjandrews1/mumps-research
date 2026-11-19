package doc

// Unit 23 (B1 tranche 4): libmpscpp.cpp::Input (batch 1, complex
// stratum; units.csv row 23: loc 40). The READ-style word reader:
// loops fgets over the input unit, drops newline-only lines, scans
// whitespace-delimited tokens, strips punctuation (keeping the
// hyphen), and stores each word under the name subscript via sym_
// with the strcatx-delimited x01 encoding; returns the word count as
// a string (or "0" at end of input). sym_ is the H-014 stub. The
// static char tmp[1024]/scount[32] ride the InputStatic holder
// (H-006); the vendor returns the static tmp - the render returns
// its content copy (pointer identity has no analog; the divergence
// is noted). strcatx is the inline.h:46 inline (in appended at the
// NUL, then the x01 delimiter byte and NUL) - not a corpus unit, so
// the internal strcatx helper renders here (shared with the unit-25
// fullUnpad seat); the C ispunct domain (ASCII printable punctuation)
// renders as the local helper. svPtr->symflg/tpx ride the Msv
// carriers (stateVector.h struct MSV). goto reread linearizes per
// H-011 (the reread flag). fgets maps to a raw single-line reader
// over the in_file Reader: the retained-newline divergence is
// documented (the vendor skips lines whose FIRST byte is the newline;
// the render takes the empty line result for the same lines, line
// content otherwise identical). The vendor double noword label quirk
// (label declared with a doubled semicolon) is noted. Naming per
// H-017.

object InputStatic {
    // libmpscpp.cpp static char tmp[1024]
    val tmp = CharArray(1024)
    // libmpscpp.cpp static char scount[32]
    val scount = CharArray(32)
}

// inline.h:46 strcatx(out, in): append in at out's NUL, then add the
// x01 delimiter byte and NUL.
internal fun strcatx(out: CharArray, ins: CharArray) {
    var x = 0
    while (x < out.size && out[x] != NUL) x++
    var y = 0
    while (y < ins.size && ins[y] != NUL && x < out.size) {
        out[x] = ins[y]
        x++
        y++
    }
    if (x < out.size) {
        out[x] = CHAR_1
        if (x + 1 < out.size) out[x + 1] = NUL
    }
}

private fun ispunct(c: Char): Boolean {
    // C ispunct ASCII domain as byte ranges (33-47, 58-64,
    // 91-96, 123-126; the third range is written in codes to
    // keep the backtick byte out of source text)
    val b = c.code
    return b in 33..47 || b in 58..64 || b in 91..96 || b in 123..126
}

// fgets(line, STR_MAX, in_file[io]): one raw line, newline stripped
// into the line carrier; null at end of stream.
private fun fgetsLine(rd: java.io.Reader, line: CharArray): Int? {
    var c = rd.read()
    if (c == -1) return null
    var n = 0
    while (c != -1 && c != '\n'.code && n < STR_MAX - 1) {
        line[n] = c.toChar()
        n++
        c = rd.read()
    }
    line[n] = NUL
    return n
}

fun Input(svptr: Msv, ep: String, name1: String): String {
    val line = CharArray(2 * STR_MAX)
    val name = CharArray(256)
    val tmp1 = CharArray(1024)
    var count = 0
    var off = 0
    var lineLen = 0

    svptr.symflg = 2 // kill existing array
    Stubs.sym_(2, name1.toCharArray(), "".toCharArray(), svptr)

    // goto reread (H-011 flag)
    var reread = true
    while (reread) {
        reread = false
        val rd = svptr.in_file[svptr.io]
        if (rd == null) return "0"
        val n = fgetsLine(rd, line) ?: return "0"
        lineLen = n
        // vendor: line[0] == '\n' (the fgets-retained newline head);
        // the render sees the same blank lines as length 0
        if (lineLen == 0) reread = true
    }

    while (true) {
        InputStatic.tmp[0] = NUL
        // sscanf(&line[off], "%1023s", tmp1): next whitespace token
        while (off < lineLen && (line[off] == ' ' || line[off] == '\t')) off++
        var tlen = 0
        while (off + tlen < lineLen && line[off + tlen] != ' ' &&
                line[off + tlen] != '\t' && line[off + tlen] != NUL) {
            tmp1[tlen] = line[off + tlen]
            tlen++
        }
        if (tlen == 0) {
            val cnt = count.toString()
            CstringOps.strcpyS(InputStatic.tmp, cnt)
            svptr.tpx = 0
            return cnt
        }
        tmp1[tlen] = NUL

        svptr.tpx = 1

        // strip punctuation, keep hyphens
        var j = 0
        var x = 0
        while (x < tlen && x < 1023) {
            if (tmp1[x] == '-' || !ispunct(tmp1[x])) {
                InputStatic.tmp[j] = tmp1[x]
                j++
            }
            x++
        }
        InputStatic.tmp[j] = NUL
        InputStatic.tmp[1023] = NUL

        if (InputStatic.tmp[0] == NUL) {
            // noword (vendor double-semicolon label quirk noted)
        } else {
            CstringOps.strcpyS(InputStatic.scount, count.toString())
            count++
            CstringOps.strcpyS(name, name1)
            strcatx(name, "".toCharArray())
            strcatx(name, InputStatic.scount)
            svptr.symflg = 0 // store
            Stubs.sym_(0, name.copyOf(), InputStatic.tmp.copyOf(), svptr)
        }

        while (off < lineLen && line[off] != NUL && line[off] != ' ' &&
                line[off] != '\t') off++

        if (off >= lineLen || line[off] == NUL) {
            val cnt = count.toString()
            CstringOps.strcpyS(InputStatic.tmp, cnt)
            svptr.tpx = 0
            return cnt
        }

        off++
    }
}
