package daf

// DAF CRuntime (T2 seat additions): companion to DafCRuntime.kt, logged
// arm-runtime extensions raised by the DAF B4 T2 units. Each addition
// carries its consult anchor:
//   atoi          <- BASE-log row 28 (leading-int parse with overflow
//                   quirk documented on the BASE render; adapted)
//   isAlnumAscii  <- BASE-log row 25 (ASCII-only explicit letter/digit
//                   range check; NOT Kotlin isLetterOrDigit)
//   toLowerAscii / toUpperAscii <- row 25 family (ASCII-only ctype)
//   at            <- BASE-log row 81 (NUL-terminated buffer reads past
//                   the logical end yield the NUL char; end-guard helper)
//   fscanfS       <- manual-decision (no BASE row: fscanf token reads
//                   appear in ScanAlnum only; ad-hoc additions logged
//                   per ARM notes section 3): "%8191s" token read over
//                   a PushbackReader (the seat of FILE*), whitespace
//                   delimited, null at EOF; the delimiter character
//                   stays unconsumed for the next call, matching the
//                   fscanf semantics of the reference build.

fun atoi(s: String): Int {
    var i = 0
    while (i < s.length && (s[i] == ' ' || s[i] == '\u0009')) i++
    var j = i
    var neg = false
    if (j < s.length && (s[j] == '+' || s[j] == '-')) {
        neg = s[j] == '-'
        j++
    }
    var v = 0L
    var ovf = false
    while (j < s.length && s[j] in '0'..'9') {
        v = v * 10 + (s[j] - '0')
        if (v > Int.MAX_VALUE) { // C atoi overflow is UB; clamp (divergence)
            ovf = true
            v = Int.MAX_VALUE.toLong()
        }
        j++
    }
    if (ovf) return if (neg) Int.MIN_VALUE else Int.MAX_VALUE
    return if (neg) (-v).toInt() else v.toInt()
}

fun isAlnumAscii(c: Char): Boolean =
    c in 'a'..'z' || c in 'A'..'Z' || c in '0'..'9'

fun toLowerAscii(c: Char): Char =
    if (c in 'A'..'Z') (c.code + 32).toChar() else c

fun toUpperAscii(c: Char): Char =
    if (c in 'a'..'z') (c.code - 32).toChar() else c

fun at(buf: CharArray, i: Int): Char =
    if (i in buf.indices) buf[i] else NUL_D

// int fscanf(FILE *u, "%8191s", char *word) analog: reads up to 8191
// non-whitespace characters; returns null when the stream is exhausted
// before any token character (the EOF case of the vendor guard).
fun fscanfS(u: java.io.PushbackReader): String? {
    var c = u.read()
    while (c != -1 && c.toChar().isWhitespace()) c = u.read()
    if (c == -1) return null
    val sb = StringBuilder()
    while (c != -1 && !c.toChar().isWhitespace() && sb.length < 8191) {
        sb.append(c.toChar())
        c = u.read()
    }
    if (c != -1) u.unread(c) // delimiter stays unconsumed (fscanf keeps it)
    return sb.toString()
}
