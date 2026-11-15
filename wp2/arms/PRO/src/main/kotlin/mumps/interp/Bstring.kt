package mumps.interp

/**
 * PRO-arm translation of interp-bstring.cpp::<misc> (pilot B1, unit
 * 22). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 1-121 minus the extracted unit interp-bstring.cpp::bstrcat
 * (lines 52-77, separately selected simple unit, batch 9 - NOT
 * translated here).
 *
 * The <misc> residue is the "better string" library support: struct
 * carrier (bstring.h:33-36), new_bstring, bcatchr, make_cstring and
 * the file-static checked_strlen, plus the file banner/includes and
 * the forward declarations (sigint declared include/mumpsc/sym.h:41).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param     : fprintf(stderr,...) -> System.err.
 *  - h007 char_buffer         : struct bstring -> Bstring class
 *    (length + string[STR_MAX]); local struct returns are fresh
 *    objects; bcatchr's *str return aliases the argument.
 *  - h009 format_string       : stderr messages byte-exact.
 *  - h012 unsigned_byte_domain: string buffers Char(0..255); the C
 *    uninitialized struct buffer maps to Kotlin's zero fill (a
 *    benign superset; new_bstring does not write a terminator,
 *    matching the vendor - bstring uses length, not NUL).
 *  - h014 out_of_scope_call   : sigint(int) is declared here but
 *    owned by interp-sysfunc.cpp (B1 unit 25 interp-
 *    sysfunc.cpp::<misc>) -> InterpStubs.sigint stub; delegate when
 *    unit 25 lands.
 *  - h015 grammar_collision   : STR_MAX declared file-private -
 *    GlobalName.kt (u13) and MumpsMain.kt (u12) already carry their
 *    own STR_MAX; a public top-level const here caused
 *    overload-resolution ambiguity at their usage sites (red build
 *    #2, fixed same day).
 *  - h017 naming              : mechanical snake->camel per-arm
 *    mapping: new_bstring -> newBstring, checked_strlen ->
 *    checkedStrlen, make_c_string -> makeCstring; bcatchr/bstrcat
 *    single words keep vendor spelling; struct bstring -> Bstring.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The "Dummy return - unreachable" statements after sigint(100)
 *    are kept (sigint aborts in the vendor; the Kotlin stub throws,
 *    so they stay unreachable).
 *  - STR_MAX = 32000 (include/mumpsc/strmax.h:3).
 */
private const val STR_MAX = 32000 // h015: file-private; GlobalName.kt/MumpsMain.kt declare their own

/**
 * vendor struct bstring (bstring.h:33-36): int length; char
 * string[STR_MAX]. Reference type: passing/returning aliases, like
 * the C struct-pointer use in bcatchr; value-like copies (bstrcat's
 * struct-by-value args, owned by the bstrcat unit) must be made
 * explicitly there.
 */
class Bstring {
    var length: Int = 0
    val string = CharArray(STR_MAX)
}

/** interp-bstring.cpp:42-50 — struct bstring new_bstring(const char[]). */
fun newBstring(initial: CharArray, off: Int = 0): Bstring {
    val result = Bstring()

    result.length = checkedStrlen(initial, off)
    // strncpy(result.string, initial, result.length);
    for (i in 0 until result.length) {
        result.string[i] = initial[off + i]
    }
    // no terminator write in the vendor (length-based struct)

    return result
}

/** interp-bstring.cpp:82-93 — bcatchr(struct bstring *, const char). */
fun bcatchr(str: Bstring, c: Char): Bstring {
    str.length++
    if (str.length > STR_MAX) {
        System.err.print("String too long, aborting.\n")
        sigint(100) // h014 stub (owner interp-sysfunc.cpp, B1 unit 25)
    }
    str.string[str.length - 1] = c
    str.string[str.length] = NUL

    return str // vendor returns *str - same object aliased
}

/** interp-bstring.cpp:98-102 — void make_cstring(char *, struct bstring). */
fun makeCstring(dest: CharArray, src: Bstring) {
    // strncpy(dest, src.string, src.length);
    for (i in 0 until src.length) {
        dest[i] = src.string[i]
    }
    dest[src.length] = NUL
}

/** interp-bstring.cpp:107-121 — static int checked_strlen(const char[]). */
private fun checkedStrlen(cstring: CharArray, off: Int = 0): Int {
    for (i in 0..STR_MAX) {
        if (cstring[off + i] == NUL) {
            return i
        }
    }
    System.err.print("String too long - aborting.\n")
    sigint(100) // h014 stub (owner interp-sysfunc.cpp, B1 unit 25)

    /* Dummy return - unreachable */
    return 0
}
