package doc

import java.util.Locale

// Unit 5 (B1 tranche 1): libmpscpp.cpp::EncodeHTML (batch 1, simple
// stratum; LOC 21, score 7.3). The two-overload URL percent-encoder:
// the mstring seat delegates to the char* seat; the char* seat
// percent-encodes every non-alphanumeric byte except spaces (spaces
// become plus). H-007 adapted (PRO u5 precedent, recorded BASE row):
// the vendor static char v1d[STR_MAX] is rendered as a fresh local
// explicit CharArray (byte-domain fidelity: NUL-terminated in-place
// C-string surgery, chars >= 0x80 kept in the char domain), NOT the
// StringBuilder default; the static scratch aliasing condition of
// H-008 applies (static because of the return via tmp only; no
// cross-call aliasing exists, so the fresh-per-call divergence is
// documented here). t1/t2 are transient parse buffers (H-007 roles).
// sprintf("%2X", c) per H-009: Locale.US, and the C char->int
// sign-extension quirk kept: a byte >= 0x80 sign-extends and the
// 2-wide hex of a negative int widens to 8 digits (e.g. 0x80 ->
// "FFFFFF80"), byte-domain per H-010/H-015 analysis. isalnum: C
// locale ASCII alnum only (documented domain note). strmove is the
// H-014 batch stub (owner inline.h:38). Naming per H-017.

fun EncodeHTML(str: Mstring): Mstring =
    EncodeHTML(str.c_str())

fun EncodeHTML(str: String): Mstring {
    val v1d = CharArray(STR_MAX)
    CstringOps.strcpyS(v1d, str)

    var i = 0
    while (v1d[i] != NUL) {
        if (v1d[i] == ' ') {
            v1d[i] = '+'
        } else if (!(v1d[i] in 'a'..'z' || v1d[i] in 'A'..'Z' || v1d[i] in '0'..'9')) {
            val t1 = CharArray(10)
            val t2 = CharArray(STR_MAX)

            // sprintf(t1, "%%%2X\0", v1d[i]) - the C char sign-extends
            val signed = if (v1d[i].code > 127) v1d[i].code - 256 else v1d[i].code
            CstringOps.strcpyS(t1, "%" + String.format(Locale.US, "%2X", signed) + NUL.toString())

            v1d[i] = NUL

            // strmove(t2, &v1d[1]); strcat(t2, t1); strcat(t2, &v1d[i+1]);
            val t2s = CstringOps.cstringAt(v1d, 1) + CstringOps.cstring(t1) + CstringOps.cstringAt(v1d, i + 1)
            CstringOps.strcpyS(t2, t2s)

            // strmove(&v1d[1], t2)
            CstringOps.copyAt(v1d, 1, t2)

            i = i + 2
        }
        i++
    }

    val tmp = Mstring(CstringOps.cstring(v1d))
    return tmp
}
