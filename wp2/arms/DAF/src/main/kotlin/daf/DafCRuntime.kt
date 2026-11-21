package daf

// DAF CRuntime: minimal C-runtime analog helpers shared by the DAF arm
// renders. Each helper carries the consult anchor of the BASE heuristic
// decision it derives from (BASE-log rows may be consulted per ARM notes
// section 3; adapted per-unit where noted):
//   strtod  <- BASE-log row 40 (leading-double parse, 0.0 fallback, hex /
//             inf / nan spellings documented divergence)
//   fmt8g   <- BASE-log rows 41+43 (gcvt_wrapper resolution: plain %.8g,
//             NO appended decimal point on the reference build; the GCVT
//             branch is dead in the reference config per the row-43
//             verification against the reference binary)
//   fmtG    <- BASE-log rows 26/33 family (sprintf format strings kept
//             verbatim; Locale.US pins the C locale)
//   cstring <- NUL-terminated CharArray read (row-8 buffer-role fold
//             adapted: scan-only reads keep arrays, yields String)

import java.util.Locale

const val STR_MAX_DAF = 500 // vendor defines.h STR_MAX
const val NUL_D: Char = '\u0000'

object DafCRuntime {
    fun cstring(buf: CharArray): String {
        val sb = StringBuilder()
        var i = 0
        while (i < buf.size && buf[i] != NUL_D) {
            sb.append(buf[i])
            i++
        }
        return sb.toString()
    }

    fun cstringAt(buf: CharArray, off: Int): String {
        val sb = StringBuilder()
        var i = off
        while (i < buf.size && buf[i] != NUL_D) {
            sb.append(buf[i])
            i++
        }
        return sb.toString()
    }

    // double strtod(const char *nptr, char **NULL) - leading double parse;
    // 0.0 fallback when no prefix parses (adapted from BASE-log row 40:
    // the C hex/inf/nan spellings collapse onto Kotlin's.toDoubleOrNull
    // domain; divergence documented there applies).
    fun strtod(s: String): Double {
        var i = 0
        while (i < s.length && (s[i] == ' ' || s[i] == '\u0009')) i++
        var j = i
        var seenDigit = false
        var seenDot = false
        var seenExp = false
        if (j < s.length && (s[j] == '+' || s[j] == '-')) j++
        while (j < s.length) {
            val c = s[j]
            if (c in '0'..'9') {
                seenDigit = true
                j++
            } else if (c == '.' && !seenDot && !seenExp) {
                seenDot = true
                j++
            } else if ((c == 'e' || c == 'E') && seenDigit && !seenExp) {
                seenExp = true
                j++
                if (j < s.length && (s[j] == '+' || s[j] == '-')) j++
                if (j >= s.length || s[j] !in '0'..'9') {
                    seenExp = false
                    j--
                    break
                }
            } else break
        }
        if (!seenDigit) return 0.0
        return s.substring(i, j).toDoubleOrNull() ?: 0.0
    }

    // gcvt_wrapper(x, c) on the reference build: plain %.8g (BASE-log
    // rows 41+43: no appended decimal point; verified against the
    // reference binary outputs).
    fun fmt8g(x: Double): String = String.format(Locale.US, "%.8g", x)

    fun fmtG(x: Double): String = String.format(Locale.US, "%g", x)
}

// File-scope svPtr of the C++ library TU: the interpreter state-vector
// singleton (BASE-log row 22 singleton fold; row 18 state-struct->class;
// the v1d StringBuilder/sentinel modeling of row 101 adapted: DAF keeps
// the vendor CharArray with the [0] sentinel byte in place).
class DafMsv {
    val v1d = CharArray(STR_MAX_DAF)
    val bd = CharArray(STR_MAX_DAF)

    // T3 seat extension (units 114-123; fields pinned from struct MSV of
    // mumpsc/stateVector.h; carrier fold per BASE-log rows 18/22):
    val xd = CharArray(STR_MAX_DAF)      // unsigned char *xd
    val pd1 = CharArray(STR_MAX_DAF * 2) // interpreter program buffer
    val args = CharArray(STR_MAX_DAF)    // runtime arg register
    val CurrentFile = CharArray(STR_MAX_DAF) // FILE *name carrier
    val setname = CharArray(STR_MAX_DAF)// \$set LHS name
    val sqloutput = CharArray(STR_MAX_DAF)
    val start = IntArray(SYM_MAX_D)      // symbol-table layer stack
    var xpx = 0
    var pd1len = 0
    var sdlim = 0
    var symlen = 0
    var setpiece = 0
    var tpx = 0
    var t0px = 0
    var t2 = 0
    var ierr = 0
    var LineNumber = 0
    var io = 0
    var NOERR = 0
    var PD1 = 0
    var TabSize = 0
    var nstart = 0
    var _Sym = 0
    val hor = IntArray(32)               // \$x cursor table
    val ver = IntArray(32)               // \$y cursor table
}

object DafInterpState {
    val svPtr = DafMsv()
}