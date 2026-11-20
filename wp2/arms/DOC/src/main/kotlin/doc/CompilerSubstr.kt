package doc

// Unit 189 (B7 tranche 1): compiler-sys1.c::substr (batch 7,
// simple stratum; LOC 17, score 6.7, rank 41; physLOC 396,
// physically verified this session, K&R header lines included).
// The compiler-side substring twin: short-domain start/len (the
// K&R parameter line, H-010 shorts kept), len 0 writes the C
// empty string, negative len clamps to 255, the >255 clamp arm
// (vendor: else if (len > 255) len = 255), then the 1-based
// start/len walk copies and NUL-terminates. DISTINCT-TWIN NOTE
// (the unit-59 inout precedent): the interpreter twin
// interp-strmanip.cpp::substr (same batch, row 190) is a
// separate roster row with int parameters and the realloc quirk;
// the two C symbols share a name, and the Kotlin seats stay
// distinct by the vendor parameter domains - this SHORT-domain
// seat is the compiler-sys1 form, the INT-domain seat in
// StrmanipSubstr.kt is the interpreter form. The compiler-side
// call sites of landed renders (Match.kt unit 180, PatrnSys1.kt
// unit 26) rebind from the H-014 Stubs.substr seat to this real
// in this push (the past-int expressions narrow to the shorts
// exactly as the C call sites do). Naming kept (substr is the C
// symbol; H-017 has no camel form here).

fun substr(in_: CharArray, out: CharArray, start: Short, len: Short) {
    var startV = start
    var lenV = len
    var i: Short = 0

    if (lenV == 0.toShort()) {
        out[0] = '\u0000'
        return
    }

    if (lenV < 0) {
        lenV = 255
    } else if (lenV > 255) {
        lenV = 255
    }

    startV--
    lenV--
    while (i <= lenV) {
        out[i.toInt()] = in_[startV.toInt()]
        startV++
        i++
    }
    out[(lenV + 1).toInt()] = '\u0000'
}
