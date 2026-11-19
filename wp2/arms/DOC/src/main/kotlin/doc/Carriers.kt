package doc

// Arm DOC shared carriers (arm scaffolding, opened with batch B2 unit 32).
// Carriers mirror the vendor headers actually referenced by the translated
// units; each added property names its vendor source line. Translations are
// made from the pristine vendor tarball only (arm independence). Consult
// outcomes for this file are recorded in the arm journal (C-005).
//
// Vendor constants:
//   STR_MAX        include/mumpsc/strmax.h:3      32000
//   RETRIEVE       include/mumpsc/defines.h:83     0
//   STORE          include/mumpsc/defines.h:84     1
//   GKILL          include/mumpsc/defines.h:167    3
//   XNEXT          include/mumpsc/defines.h:88     8
//   GLOBAL_NOT_FOUND include/mumpsc/libmpscpp.h:71 2
//   CodedOpen      include/mumpsc/defines.h:38    206
//   CodedClose     include/mumpsc/defines.h:39    207
//   CodedComma     include/mumpsc/defines.h:40    208

const val STR_MAX = 32000
const val RETRIEVE = 0
const val STORE = 1
const val GKILL = 3
const val XNEXT = 8
const val GLOBAL_NOT_FOUND = 2
// libmpscpp.h:73 error code 4
const val DATA_RANGE_ERROR = 4
// libmpscpp.h:70 error code 1
const val INVALID_CONVERSION = 1
const val CodedOpen = 206
const val CodedClose = 207
const val CodedComma = 208

// Low bytes per cookbook H-015: built as named constants instead of raw
// control-byte literals (Mumps delimiter byte is 1; NUL is 0). The
// unicode escapes below are textual (no raw bytes in this file).
const val NUL: Char = '\u0000'
const val CHAR_1: Char = '\u0001'

// struct MSV state vector (include/mumpsc/stateVector.h:39-), fields added
// per unit with the vendor line recorded: bd (stateVector.h:42),
// v1d (stateVector.h:44), tpx (stateVector.h:64, long), pd1 (:43),
// io (:49), NOERR (:60), out_file (:61), hor/ver (:62),
// LineNumber (:63), ErrChar (:95), in_file (:61 carrier).
class Msv {
    val bd = CharArray(STR_MAX)
    val v1d = CharArray(STR_MAX)
    var tpx: Long = 0
    // stateVector.h FILE *in_file[10] carrier (unit 48: getstr2 binds
    // lun 5 to stdin); rendered as Readers indexed as in the vendor.
    val in_file = arrayOfNulls<java.io.Reader>(10)
    // stateVector.h:61 FILE *out_file[10] - Writers, same indexing;
    // assigned by the open/use owner units.
    val out_file = arrayOfNulls<java.io.Writer>(10)
    // stateVector.h:43 unsigned char pd1[PS1] (sysparms PS1 32001)
    val pd1 = CharArray(PS1)
    // stateVector.h:49 int io
    var io: Int = 0
    // stateVector.h:60 int NOERR
    var NOERR: Int = 0
    // stateVector.h:62 int hor[10], ver[10]
    val hor = IntArray(10)
    val ver = IntArray(10)
    // stateVector.h:63 long LineNumber
    var LineNumber: Long = 0
    // stateVector.h:95 char ErrChar
    var ErrChar: Char = NUL
}

// File-scope externs of libmpscpp.h:62 (GlobalExceptionCode) :210
// (svPtr) live in one owner object per the one-owner-per-C-entity rule.
object Globals {
    var GlobalExceptionCode: Int = 0
    val svPtr = Msv()
}

// libmpscpp.h:332 class MumpsGlobalException: message accessor kept verbatim.
class MumpsGlobalException(msg: String = "Global reference not found") :
    RuntimeException(msg)

// mstringCarrier: minimal mstring (include/mumpsc/mstringClass.h:31-)
// surface required by batch B2 units: content string with char* conversion.
// Only members observed by translated units are rendered.
class Mstring(content: String) {
    var str: String = content

    constructor() : this("")

    operator fun invoke(s: String): Mstring {
        str = s
        return this
    }

    fun c_str(): String = str

    override fun toString(): String = str
}

// globalClass.h:31 class global: per-instance global reference carrier.
// The vendor private field ref (globalClass.h:536) becomes the Kotlin
// constructor property; unit member functions are rendered as extensions
// in their own unit files so each unit owns its file.
class GlobalRef(val ref: String)

// C string operations used by translated units (strcpy/strcat/strlen/
// strncmp over CharArray carriers). Byte-domain semantics per the C
// standard; only what translated units call is here.
object CstringOps {
    fun strlenC(buf: CharArray): Int {
        var i = 0
        while (i < buf.size && buf[i] != '\u0000') i++
        return i
    }

    fun strcpyC(dst: CharArray, src: CharArray) {
        var i = 0
        while (i < src.size && src[i] != '\u0000') {
            dst[i] = src[i]
            i++
        }
        dst[i] = '\u0000'
    }

    fun strcpyS(dst: CharArray, src: String) {
        var i = 0
        while (i < src.length) {
            dst[i] = src[i]
            i++
        }
        dst[i] = '\u0000'
    }

    fun strcatS(dst: CharArray, src: String) {
        val start = strlenC(dst)
        var i = 0
        while (i < src.length) {
            dst[start + i] = src[i]
            i++
        }
        dst[start + i] = '\u0000'
    }

    fun strncmpC(a: CharArray, b: CharArray, n: Int): Int {
        var i = 0
        while (i < n) {
            val ca = a[i]
            val cb = b[i]
            if (ca == '\u0000' && cb == '\u0000') return 0
            if (ca < cb) return -1
            if (ca > cb) return 1
            i++
        }
        return 0
    }

    // strcpy(&dst[offset], src): copy a NUL-terminated String into dst
    // starting at the given offset (vendor idiom &v1d[1]).
    fun copyAt(dst: CharArray, offset: Int, src: CharArray) {
        var i = 0
        while (i < src.size && src[i] != NUL) {
            dst[offset + i] = src[i]
            i++
        }
        dst[offset + i] = NUL
    }

    // read a NUL-terminated String from buf starting at offset
    // (vendor idiom &bd[1]).
    fun cstringAt(buf: CharArray, offset: Int): String {
        val sb = StringBuilder()
        var i = offset
        while (i < buf.size && buf[i] != NUL) {
            sb.append(buf[i])
            i++
        }
        return sb.toString()
    }

    fun cstring(buf: CharArray): String {
        val sb = StringBuilder()
        var i = 0
        while (i < buf.size && buf[i] != NUL) {
            sb.append(buf[i])
            i++
        }
        return sb.toString()
    }

    // strcpy(&dst[offset], src) String form (vendor idiom &tmp[1]).
    fun copyAtS(dst: CharArray, offset: Int, src: String) {
        var i = 0
        while (i < src.length) {
            dst[offset + i] = src[i]
            i++
        }
        dst[offset + i] = NUL
    }

    // strcat(&dst[offset], ch): append one char at the NUL of a
    // NUL-terminated region beginning at offset.
    fun catAt(dst: CharArray, offset: Int, ch: Char) {
        var i = offset
        while (i < dst.size && dst[i] != NUL) i++
        if (i < dst.size) {
            dst[i] = ch
            if (i + 1 < dst.size) dst[i + 1] = NUL
        }
    }

    // catAt String form used by the mdh_tree_level_add unit file.
    fun catAt(dst: CharArray, offset: Int, src: String) = catAtS(dst, offset, src)

    // strcat(&dst[offset], src) String form.
    fun catAtS(dst: CharArray, offset: Int, src: String) {
        var i = offset
        while (i < dst.size && dst[i] != NUL) i++
        var j = 0
        while (j < src.length && i < dst.size) {
            dst[i] = src[j]
            i++
            j++
        }
        if (i < dst.size) dst[i] = NUL
    }

    // strncmp(ref, tmp, i): compare a String against the first n bytes
    // of a NUL-terminated buffer region (vendors global::Min and IDF).
    fun strncmpS(a: String, b: CharArray, n: Int): Int {
        var i = 0
        while (i < n) {
            val ca = if (i < a.length) a[i] else NUL
            val cb = if (i < b.size) b[i] else NUL
            if (ca != cb) return if (ca < cb) -1 else 1
            if (ca == NUL) return 0
            i++
        }
        return 0
    }
}
