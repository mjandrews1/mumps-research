package daf

// R1 stub-completion (2026-11-28, C-020 erratum session, tranche 6):
// libmpscpp.cpp::EncodeHTML the char* body (roster row 28,
// vendor :2965): the URL-encoder of the WebU mstring family:
// spaces become '+', non-alphanumeric bytes become the %XX
// hex escape, the result builds in the static v1d buffer.
// The mstring&(mstring) forwarder at :2961 is a DIFFERENT
// function (the audit row's note; the mstringEncodeHtmlD cover
// of LibmpscppMisc6.kt relays here).
// Consults (2026-11-28T00:00:00Z, nominal): BASE-log row 25
// hit-reuse (isalnum -> the ASCII-only range check
// isAlnumAscii of DafCRuntimeT2.kt); row 26 hit-reuse (the
// sprintf "%%%2X" format keeps Locale.US, the %2X two-digit
// hex); rows 41/45 hit-reuse (strmove/strcat fold onto the
// String cut-and-concat over the v1d CharArray carrier - the
// static char v1d[STR_MAX] rides a fresh per-invocation
// CharArray, the row-56 fresh-value fold); rows 99/100
// hit-adapt (the mstring return folds to the String return).
// Vendor quirks kept VERBATIM, disclosed: (1) the whole build
// plays ONE BYTE OFF - the t2 staging string is cut from
// &v1d[1] (skipping position 0) and written back into &v1d[1],
// so v1d[0] keeps its original content and the final read
// starts there: the FIRST byte of the input is never
// re-encoded through the %XX path (a leading space still
// becomes '+' because that write hits v1d[0] directly, but a
// leading other-special byte hits the i=0 arm of quirk 2);
// (2) the i=0 arm is pathological vendor-side: v1d[0]=0 cuts
// the buffer at 0, t2 = v1d[1..] + t1 + v1d[1..] (the tail is
// DUPLICATED - strcat(&v1d[i+1]) with i=0 re-appends from
// index 1), and the final read from v1d[0]=0 yields the EMPTY
// string: a leading special byte destroys the whole result -
// kept verbatim (the vendor behavior, disclosed; the smoke
// covers it); (3) the escape walk skips i+=2 after a %XX
// substitution plus the loop i++ (three total over the three
// new bytes).
// Dafny: EXEMPT (byte-level buffer play; journal row).
// Release note: the LibmpscppMisc6.kt stub is deleted this
// session; mstringEncodeHtmlD relays here.

fun encodeHtmlDaf(s: String): String {

    // static char v1d[STR_MAX]; strcpy(v1d, str);
    val v1d = CharArray(STR_MAX_DAF) { NUL_D }
    var c = 0
    while (c < s.length && c < v1d.size - 1) {
        v1d[c] = s[c]
        c++
    }
    v1d[c] = NUL_D

    var i = 0
    while (i < v1d.size && v1d[i] != NUL_D) { // for (i=0; v1d[i]!=0; i++)

        if (v1d[i] == ' ') {
            v1d[i] = '+' // if (v1d[i] == ' ') v1d[i] = '+';
            i++
            continue
        }

        if (!isAlnumAscii(v1d[i])) { // else if (!isalnum(v1d[i]))

            // char t1[10]; sprintf(t1, "%%%2X\0", v1d[i]);
            val t1 = String.format(java.util.Locale.US, "%%%2X", v1d[i].code)

            v1d[i] = NUL_D // v1d[i] = 0;

            // strmove(t2, &v1d[1]); strcat(t2, t1);
            // strcat(t2, &v1d[i+1]); strmove(&v1d[1], t2);
            // - the one-byte-off staging play, kept verbatim
            val t2 = DafCRuntime.cstringAt(v1d, 1) +
                t1 +
                DafCRuntime.cstringAt(v1d, i + 1)

            var w = 1
            var q = 0
            while (q < t2.length && w < v1d.size) {
                v1d[w] = t2[q]
                w++
                q++
            }
            v1d[w] = NUL_D

            i += 2 // i = i + 2;
        }

        i++
    }

    // tmp = v1d; return tmp; - the read starts at v1d[0]
    return DafCRuntime.cstring(v1d)
}
