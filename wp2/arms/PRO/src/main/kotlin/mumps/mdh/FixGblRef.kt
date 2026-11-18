package mumps.mdh

private const val NUL = '\u0000'

/**
 * PRO-arm translation of libmpscpp.cpp::mstring::FixGblRef
 * (roster row 263, batch 9; units.csv: loc 25, comment_ratio 0.04,
 * pointer_marks 1, control_marks 7, score 11.4, rank 115).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 4405-4436 (the vendor's own "specal case" banner
 * comment kept in the seat doc, spelling verbatim).
 *
 * Mstring.FixGblRef(): Mstring - handles the special case
 * mstring ref of the type ^gbl() and converts it to ^gbl(""):
 * returns an EMPTY Mstring for blank/non-'^' refs; returns
 * *this UNCHANGED unless the ref is exactly ^alpha...();
 * when the parentheses are empty the () becomes ("") and the
 * rebuilt ref is returned.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer     : tmp[STR_MAX] byte buffer carrying
 *    data.c_str() (strcpy domain).
 *  - h012 unsigned_byte_domain: tmp[i+4]=0 NUL-terminate; the
 *    isalpha test is the vendor's own ctype gate.
 *  - h013 dead_store      : b is a declared mstring used only
 *    in the blank arms; kept as the empty-carrier returns.
 *  - h014 out_of_scope_call: strlen/strcpy/strcat family binds
 *    on the String carrier; isalpha gates on the file-private
 *    isalphaG (ZfcnBifs isalphaC is THAT file's private helper,
 *    not importable - duplicated per the one-owner policy,
 *    cf. STR_MAX file-private precedent).
 *  - h015 grammar_collision: the vendor returns TWO different
 *    things from one function ("b" or *this) - rendered with
 *    the same Mstring carrier; the quote bytes are 34.toChar()
 *    (no raw double-quote escape trick, vendor-faithful bytes).
 *  - h017 naming         : FixGblRef keeps the vendor name
 *    (Mstring extension seat, per the FileGblRef roster row
 *    263; the Mstring.kt :107 roster note "Query/Qlength/
 *    Qsubscript/FixGblRef :111-114 pending" - FixGblRef now
 *    RESOLVED here, the other three stay pending).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The is-local j = strlen(tmp) is checked for zero first
 *    (return b on empty).
 *  - The paren test reads tmp[i+1] AFTER the break; the alpha
 *    scan starts at 1 to "avoid ^".
 *  - Returns *this (the receiver) when the paren content is not
 *    empty - a by-value C++ return riding the Kotlin carrier.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)

// vendor double-quote byte (34) for the ("") rebuild
private val QUOTE = 34.toChar()

/** C isalpha on the byte domain (file-private, one-owner policy). */
private fun isalphaG(c: Char): Boolean =
    c.code in 65..90 || c.code in 97..122

fun Mstring.FixGblRef(): Mstring {
    val b = Mstring("") // vendor: mstring b="";
    val tmp = CharArray(STR_MAX)

    var s = 0
    while (s < value.length && s < STR_MAX - 5) { // strcpy(tmp,data.c_str())
        tmp[s] = value[s]
        s++
    }
    tmp[s] = NUL

    var j = 0
    while (tmp[j] != NUL) j++ // vendor: j=strlen(tmp)
    if (j == 0) return b // vendor: if ((j=strlen(tmp))==0) return b;

    if (tmp[0] != '^') { // vendor: if (tmp[0]!='^') return b;
        return b
    }

    var i = 1
    while (i < j) { // vendor: for (i=1; i<j; i++) // avoid ^
        if (isalphaG(tmp[i])) {
            i++
            continue
        }
        break
    }

    if (tmp[i] != '(') return b // vendor: if (tmp[i]!='(') return b;

    if (tmp[i + 1] != ')') { // vendor: if (tmp[i+1]!=')') return *this;
        return this
    }

    tmp[i + 1] = QUOTE // vendor: tmp[i+1]='"';
    tmp[i + 2] = QUOTE // vendor: tmp[i+2]='"';
    tmp[i + 3] = ')' // vendor: tmp[i+3]=')';
    tmp[i + 4] = NUL // vendor: tmp[i+4]=0;

    var e = 0
    while (tmp[e] != NUL) e++ // vendor: b=tmp;
    return Mstring(tmp.sliceArray(0 until e).concatToString())
}
