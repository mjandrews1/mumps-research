package doc

// Unit 48: interp-parse.cpp::getstr2 (batch 2, core stratum) - the line
// reader of the interpreter parse path. The vendor FILE* carrier (state
// vector in_file[10], lun-indexed) is rendered as Readers on Msv; lun 5
// binds to the process stdin (JVM: FileDescriptor.in based Reader; same
// byte stream, documented divergence from the C stdin FILE*). fgetc
// becomes Reader.read with the C EOF -1 contract. The vendor one-past-
// the-end write area[STR_MAX]=0 is out of the carrier bounds in Kotlin;
// documented divergence (no observable difference: the function returns
// -1 at that point regardless).

const val LINEFEED: Char = '\u000a'
const val TAB: Char = '\u0009'

val STDIN_READER: java.io.Reader by lazy {
    java.io.InputStreamReader(java.io.FileInputStream(java.io.FileDescriptor.`in`))
}

fun getstr2(lun: Short, area: CharArray, svPtr: Msv): Int {
    var lunI = lun.toInt()

    if (lunI == 5) svPtr.in_file[lunI] = STDIN_READER

    var i = 0
    while (i < STR_MAX) { // restrict input line length
        val chr = svPtr.in_file[lunI]?.read() ?: -1

        if (chr == -1) {
            area[0] = NUL
            return -1
        }

        area[i] = chr.toChar()

        if (area[i] == LINEFEED) {
            area[i] = NUL
            return i
        }

        if (area[i].code > 127) area[i] = Char(0x20)

        if (area[i] == '\n') {
            // vendor decrements then the for-increment cancels: slot
            // reused, nothing stored
        } else if (area[i] == '\r') {
            // same cancellation for carriage return
        } else if (area[i] == TAB) {
            area[i] = TAB
        } else if (area[i] < Char(32)) {
            area[i] = Char(0x20)
        }

        i++
    }

    // area[STR_MAX] = 0: out-of-bounds on this carrier, see header note.
    return -1
}
