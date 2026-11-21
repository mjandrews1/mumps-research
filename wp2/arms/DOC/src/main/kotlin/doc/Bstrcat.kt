package doc

// Unit 255 (B9 tranche 1): interp-bstring.cpp::bstrcat (batch 9,
// simple stratum; units.csv row 277: loc 18, rank 5; physLOC 18
// physically verified this session). Owner of the unit-22 banner
// note (the body sits below the misc chunk cut); the Stubs.bstrcat
// H-014 seat flips to this delegate this push. The Bstring
// carrier is the unit-22 class (BstringMisc.kt); fprintf(stderr)
// maps to System.err (H-001); sigint is the real unit-211 seat
// (H-014 reuse). Vendor shape kept: the else arm returns the
// exact-length concatenation (strncpy of left.length chars, the
// explicit left.length NUL, then strncat of right.length chars
// with its implicit terminator); the post-sigint fall-through
// reaches the unreachable dummy return new_bstring("") kept with
// the vendor comment. H-012 domain note: at catlength == STR_MAX
// the strncat terminator would write string[STR_MAX] one element
// past the carrier (the same one-past quirk as the unit-22
// bcatchr/checked_strlen bodies; guarded here with the
// divergence documented there too). Naming per H-017.

fun bstrcat(left: Bstring, right: Bstring): Bstring {
    val catlength = left.length + right.length

    if (catlength > STR_MAX) {
        System.err.print("String too long - aborting.\n")
        Stubs.sigint(100)
    } else {
        val result = Bstring()
        result.length = catlength

        // strncpy(result.string, left.string, left.length): exact
        // count copy with the vendor's explicit terminator
        var i = 0
        while (i < left.length) {
            result.string[i] = left.string[i]
            i++
        }
        result.string[left.length] = NUL

        // strncat(result.string, right.string, right.length)
        System.arraycopy(right.string, 0, result.string,
                left.length, right.length)
        if (catlength < result.string.size) {
            result.string[catlength] = NUL
        }

        return result
    }

    // Dummy return - unreachable (kept verbatim from the vendor)
    return newBstring(charArrayOf(NUL))
}
