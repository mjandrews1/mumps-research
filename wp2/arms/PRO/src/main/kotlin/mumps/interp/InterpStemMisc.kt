package mumps.interp

/**
 * PRO-arm translation of interp-stem.cpp::<misc> (B6 unit 163).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * Identity: units.csv row 164 - interp-stem.cpp::<misc>, cpp,
 * loc 345, comment_ratio 0.102, pointer_marks 2,
 * control_marks 4, score 43.7, rank 229 - exact match via the
 * study tool (residual by identity: wstem (batch-10 row 293)
 * and escan (batch-4 row 122, DONE) are the file's corpus
 * functions; the Stem wrapper's 4-line body is BELOW the
 * min-loc fold and rides this chunk per the tool's misc rule).
 *
 * The chunk: the GPL banner + Feb 9 2022 date comment, the
 * stem.c runtime-library banner block, six includes
 * (string.h, iostream, mumpsc/defines.h, mumpsc/fcns.h,
 * mumpsc/inline.h), the "using namespace std;" fossil, the
 * wstem prototype, the fin conflation-table marker define,
 * struct endings, the twelve Porter-stemmer conflation tables
 * (prefix + d/s/g/n/e/l/c/y/v/t/r_endings - the ZALTER and
 * ZNORMAL stemming data, consumed by wstem/escan), and the
 * folded Stem wrapper.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext : the vendor's file-scope const tables
 *    become arm top-level vals (read-only data; wstem and
 *    escan are their only consumers).
 *  - h012 unsigned_byte_domain : every ending/replace field
 *    is a NUL-terminated C-string CharArray (the table
 *    terminator convention is ending[0]==NUL, Escan.kt
 *    precedent).
 *  - h014 out_of_scope_call : wstem (MdhStubs stub, batch-10
 *    corpus unit, owner interp-stem.cpp::wstem) called as a
 *    seat from the folded Stem wrapper; strlenC reused; the
 *    include set maps to existing carriers (string.h ->
 *    CstringOps/strmove seats; iostream -> java.io edges;
 *    defines/fcns/inline -> the per-unit consumers), all REAL
 *    reuses - no new stubs (u35/u153 banner-ledger precedent).
 *  - h015 grammar_collision : the Ends carrier is REUSED from
 *    Escan.kt (its only consumer); the new Endings carrier
 *    (vendor struct endings, the 3-field prefix-table shape)
 *    and the twelve table vals + Stem are new top-levels - no
 *    collisions (the mdh lowercase stem() wrappers are a
 *    different file surface).
 *  - h017 naming        : Stem keeps the vendor capitalization
 *    (vendor 413); the table names keep the vendor spellings.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor's tab/space table alignment is not carried
 *    (data, not layout); every entry value, order and the
 *    trailing NUL-terminator rows are byte-faithful.
 *  - "using namespace std;" and the wstem prototype are
 *    roster fossils (the arm has no C++ namespaces and the
 *    seat is the wstem stub) - kept as this note.
 *  - fin (define fin 1) is the conflation marker the tables
 *    carry in their next field; -1 and 0 appear verbatim in
 *    the t/s tables' next fields - kept.
 *  - The prefix table's vendor struct is "endings" (3 fields)
 *    while the escan tables use "ends" (5 fields) - two
 *    carriers, both kept verbatim.
 */

import mumps.mdh.wstem

/**
 * vendor struct endings (interp-stem.cpp:54-58) - the 3-field
 * prefix-table shape (prefix string, offset length, replace).
 */
class Endings(
    /** vendor: const char *prefix. */
    val prefix: CharArray,
    /** vendor: int offset - length. */
    val offset: Int,
    /** vendor: const char *replace - replacement. */
    val replace: CharArray,
)

private fun cs(s: String): CharArray {
    val b = CharArray(s.length + 1)
    for (i in s.indices) b[i] = s[i]
    b[s.length] = NUL
    return b
}

private fun es(ending: String, offset: Int, replace: String, replen: Int, next: Int) =
    Ends(cs(ending), offset, cs(replace), replen, next)

/** vendor prefix[] (interp-stem.cpp:60-65) - struct endings rows. */
val stemPrefix: Array<Endings> = arrayOf(
    Endings(cs("pre"), 3, cs("")),
    Endings(cs("post"), 4, cs("")),
    Endings(cs("non"), 3, cs("")),
    Endings(cs(""), 0, cs("")),
)

/** vendor d_endings[] (interp-stem.cpp) - struct ends rows, escan/wstem conflation data. */
val d_endings: Array<Ends> = arrayOf(
    es("nned", 4, "n", 1, 1),
    es("rated", 5, "rate", 4, 1),
    es("cated", 5, "cate", 4, 1),
    es("iated", 5, "iate", 4, 1),
    es("mated", 5, "mate", 4, 1),
    es("gated", 5, "gate", 4, 1),
    es("lated", 5, "late", 4, 1),
    es("bated", 5, "bate", 4, 1),
    es("nated", 5, "nate", 4, 1),
    es("ated", 4, "te", 2, 1),
    es("eted", 4, "ete", 3, 1),
    es("ated", 4, "ate", 3, 1),
    es("kted", 4, "kt", 2, 1),
    es("oted", 4, "ote", 3, 1),
    es("uted", 4, "ute", 3, 1),
    es("sted", 4, "ste", 3, 1),
    es("ted", 3, "t", 1, 1),
    es("ered", 4, "er", 2, 1),
    es("dded", 4, "dd", 2, 1),
    es("rred", 4, "r", 1, 1),
    es("ored", 4, "r", 1, 1),
    es("red", 3, "re", 2, 1),
    es("zed", 3, "ze", 2, 1),
    es("oded", 4, "de", 2, 1),
    es("oved", 4, "ove", 3, 1),
    es("eved", 4, "eve", 3, 1),
    es("ied", 3, "y", 1, 1),
    es("ced", 3, "ce", 2, 1),
    es("olled", 4, "ol", 2, 1),
    es("alled", 5, "al", 2, 1),
    es("ooled", 5, "00l", 3, 1),
    es("oled", 4, "l", 1, 1),
    es("lled", 4, "l", 1, 1),
    es("led", 3, "le", 2, 1),
    es("reed", 4, "ree", 3, 1),
    es("eed", 3, "eed", 3, 1),
    es("ased", 4, "ased", 4, 1),
    es("ssed", 4, "ss", 2, 1),
    es("ccused", 6, "ccuse", 5, 1),
    es("used", 4, "us", 2, 1),
    es("sed", 3, "se", 2, 1),
    es("ved", 3, "ve", 2, 1),
    es("ined", 4, "ne", 2, 1),
    es("ned", 3, "n", 1, 1),
    es("ached", 6, "ache", 4, 1),
    es("hed", 3, "h", 1, 1),
    es("ged", 3, "ge", 2, 1),
    es("med", 3, "me", 2, 1),
    es("mmed", 4, "m", 1, 1),
    es("ued", 3, "ue", 2, 1),
    es("mbed", 4, "b", 1, 1),
    es("bed", 3, "be", 2, 1),
    es("ended", 5, "end", 3, 1),
    es("nded", 4, "d", 1, 1),
    es("aded", 4, "d", 1, 1),
    es("ded", 3, "de", 2, 1),
    es("ored", 4, "or", 2, 1),
    es("pped", 4, "p", 1, 1),
    es("ed", 2, "", 0, 1),
    es("", 0, "", 0, 1),
)

/** vendor s_endings[] (interp-stem.cpp) - struct ends rows, escan/wstem conflation data. */
val s_endings: Array<Ends> = arrayOf(
    es("tiveness", 5, "tive", 4, 1),
    es("iness", 5, "iness", 0, 1),
    es("ness", 4, "", 0, 1),
    es("ss", 2, "ss", 1, 1),
    es("sses", 4, "ss", 2, 1),
    es("xes", 3, "x", 1, 1),
    es("xas", 3, "xas", 3, 1),
    es("ious", 4, "ious", 4, 1),
    es("nous", 4, "nous", 4, 1),
    es("uous", 4, "e", 1, 1),
    es("ous", 3, "", 0, 1),
    es("aries", 5, "ary", 3, 1),
    es("eries", 5, "ery", 3, 1),
    es("ories", 5, "ory", 3, 1),
    es("tries", 5, "try", 4, 1),
    es("ries", 4, "ries", 4, 1),
    es("ies", 3, "y", 1, 1),
    es("bios", 4, "bios", 4, 1),
    es("dus", 3, "dus", 3, 1),
    es("ves", 3, "ve", 2, 1),
    es("res", 3, "re", 2, 1),
    es("sis", 3, "sis", 3, 1),
    es("ases", 4, "ase", 3, 1),
    es("ses", 3, "s", 1, 1),
    es("ces", 3, "ce", 2, 1),
    es("dos", 3, "dos", 3, 1),
    es("des", 3, "de", 2, 1),
    es("cus", 3, "cus", 3, 1),
    es("tus", 3, "tus", 3, 1),
    es("ays", 3, "ay", 2, 1),
    es("nys", 3, "ny", 2, 1),
    es("ys", 2, "ys", 2, 1),
    es("ius", 3, "ius", 3, 1),
    es("is", 2, "", 0, 1),
    es("hes", 3, "h", 1, 1),
    es("ves", 3, "ve", 2, 1),
    es("tes", 3, "te", 2, 1),
    es("nus", 3, "nu", 2, 1),
    es("us", 2, "us", 2, 1),
    es("news", 4, "news", 4, 1),
    es("s", 1, "", 0, 0),
    es("", 0, "", 0, 0),
)

/** vendor g_endings[] (interp-stem.cpp) - struct ends rows, escan/wstem conflation data. */
val g_endings: Array<Ends> = arrayOf(
    es("zing", 4, "ze", 2, 1),
    es("olling", 5, "l", 2, 1),
    es("lling", 5, "ll", 2, 1),
    es("aling", 5, "al", 2, 1),
    es("eling", 5, "el", 2, 1),
    es("iling", 5, "il", 2, 1),
    es("ling", 4, "le", 2, 1),
    es("writting", 8, "write", 5, 1),
    es("iting", 5, "it", 2, 1),
    es("ipting", 6, "ipt", 3, 1),
    es("epting", 6, "ept", 3, 1),
    es("apting", 6, "apt", 3, 1),
    es("pting", 5, "t", 1, 1),
    es("mming", 5, "m", 1, 1),
    es("cting", 5, "ct", 2, 1),
    es("inting", 6, "int", 3, 1),
    es("nting", 5, "nt", 2, 1),
    es("fting", 5, "ft", 2, 1),
    es("rting", 5, "rt", 2, 1),
    es("atting", 6, "at", 2, 1),
    es("cating", 6, "cate", 4, 1),
    es("dating", 6, "date", 4, 1),
    es("nating", 6, "nate", 4, 1),
    es("mating", 6, "mate", 4, 1),
    es("eating", 6, "eate", 4, 1),
    es("operating", 9, "operating", 9, 1),
    es("pating", 6, "pate", 4, 1),
    es("lating", 6, "late", 4, 1),
    es("rating", 6, "rate", 4, 1),
    es("ating", 5, "at", 2, 1),
    es("sting", 5, "st", 2, 1),
    es("eting", 5, "et", 2, 1),
    es("hting", 5, "ht", 2, 1),
    es("ting", 4, "te", 2, 1),
    es("sting", 5, "st", 2, 1),
    es("rming", 5, "rm", 2, 1),
    es("mming", 5, "m", 1, 1),
    es("ming", 4, "me", 2, 1),
    es("ering", 5, "er", 2, 1),
    es("loring", 6, "lore", 4, 1),
    es("oring", 5, "or", 2, 1),
    es("aring", 5, "ar", 2, 1),
    es("uing", 4, "ue", 2, 1),
    es("ring", 4, "re", 2, 1),
    es("rning", 5, "rn", 2, 1),
    es("nning", 5, "n", 1, 1),
    es("oning", 5, "on", 2, 1),
    es("gning", 5, "gn", 2, 1),
    es("fining", 6, "ine", 3, 1),
    es("ining", 5, "in", 2, 1),
    es("ning", 4, "ne", 2, 1),
    es("cing", 4, "ce", 2, 1),
    es("ving", 4, "ve", 2, 1),
    es("ssing", 5, "ss", 2, 1),
    es("using", 5, "use", 3, 1),
    es("asing", 5, "as", 2, 1),
    es("sing", 4, "se", 2, 1),
    es("gging", 5, "g", 1, 1),
    es("ging", 4, "ge", 2, 1),
    es("bing", 4, "be", 2, 1),
    es("rding", 5, "rding", 5, 1),
    es("nding", 5, "nd", 2, 1),
    es("dding", 5, "d", 1, 1),
    es("eding", 5, "ed", 2, 1),
    es("ding", 4, "de", 2, 1),
    es("aying", 5, "ay", 2, 1),
    es("nying", 5, "ny", 2, 1),
    es("lying", 5, "y", 1, 1),
    es("ying", 4, "y", 1, 1),
    es("ing", 3, "", 0, 1),
    es("", 0, "", 0, 1),
)

/** vendor n_endings[] (interp-stem.cpp) - struct ends rows, escan/wstem conflation data. */
val n_endings: Array<Ends> = arrayOf(
    es("aturation", 9, "aturate", 7, 1),
    es("eration", 7, "eration", 7, 1),
    es("tration", 7, "ter", 3, 1),
    es("oration", 7, "r", 1, 1),
    es("ration", 6, "re", 2, 1),
    es("tition", 6, "te", 2, 1),
    es("ection", 6, "ect", 3, 1),
    es("version", 7, "version", 7, 1),
    es("ersion", 6, "ert", 3, 1),
    es("ssion", 5, "ss", 2, 1),
    es("rmation", 7, "rmation", 7, 1),
    es("mation", 6, "mate", 4, 1),
    es("otation", 7, "otate", 5, 1),
    es("ication", 7, "ication", 7, 1),
    es("cation", 6, "cate", 4, 1),
    es("iction", 6, "iction", 6, 1),
    es("nction", 6, "nction", 6, 1),
    es("uction", 6, "uce", 3, 1),
    es("ction", 5, "ct", 2, 1),
    es("ilation", 7, "ile", 3, 1),
    es("lation", 6, "late", 4, 1),
    es("tation", 6, "t", 1, 1),
    es("nician", 6, "nical", 5, 1),
    es("ican", 4, "ica", 3, 1),
    es("dation", 6, "date", 4, 1),
    es("", 0, "", 0, 1),
)

/** vendor e_endings[] (interp-stem.cpp) - struct ends rows, escan/wstem conflation data. */
val e_endings: Array<Ends> = arrayOf(
    es("rable", 5, "are", 3, 1),
    es("eable", 5, "e", 1, 1),
    es("lable", 5, "le", 2, 1),
    es("ortable", 7, "ortable", 2, 1),
    es("table", 5, "te", 2, 1),
    es("zable", 5, "ze", 2, 1),
    es("able", 4, "", 0, 1),
    es("tible", 5, "tible", 5, 1),
    es("ible", 4, "", 0, 1),
    es("titive", 6, "te", 2, 1),
    es("ective", 6, "ect", 3, 1),
    es("native", 6, "nate", 4, 1),
    es("lyze", 4, "lysis", 5, 1),
    es("", 0, "", 0, 1),
)

/** vendor l_endings[] (interp-stem.cpp) - struct ends rows, escan/wstem conflation data. */
val l_endings: Array<Ends> = arrayOf(
    es("ational", 7, "ational", 7, 1),
    es("tional", 6, "tion", 4, 1),
    es("tial", 4, "tial", 4, 1),
    es("ial", 3, "ial", 3, 1),
    es("eral", 4, "eral", 4, 1),
    es("ral", 3, "re", 2, 1),
    es("ual", 3, "ual", 3, 1),
    es("mal", 3, "mal", 3, 1),
    es("onal", 4, "on", 2, 1),
    es("nal", 3, "nal", 3, 1),
    es("hical", 5, "hic", 3, 1),
    es("cal", 3, "cal", 3, 1),
    es("eal", 3, "eal", 3, 1),
    es("bal", 3, "bal", 3, 1),
    es("sal", 3, "sal", 3, 1),
    es("val", 3, "ve", 2, 1),
    es("ntal", 4, "nt", 2, 1),
    es("tal", 3, "tal", 3, 1),
    es("al", 2, "", 0, 1),
    es("", 0, "", 0, 1),
)

/** vendor c_endings[] (interp-stem.cpp) - struct ends rows, escan/wstem conflation data. */
val c_endings: Array<Ends> = arrayOf(
    es("istic", 5, "", 0, 1),
    es("ific", 4, "", 0, 1),
    es("thmic", 5, "thm", 3, 1),
    es("matic", 5, "mate", 4, 1),
    es("", 0, "", 0, 1),
)

/** vendor y_endings[] (interp-stem.cpp) - struct ends rows, escan/wstem conflation data. */
val y_endings: Array<Ends> = arrayOf(
    es("rably", 5, "re", 2, 1),
    es("ably", 4, "", 0, 1),
    es("ibly", 4, "", 0, 1),
    es("ily", 3, "", 0, 1),
    es("tically", 7, "te", 2, 1),
    es("fically", 7, "fic", 3, 1),
    es("ically", 6, "ical", 4, 1),
    es("ically", 6, "ic", 2, 1),
    es("cally", 5, "c", 1, 1),
    es("titively", 6, "te", 2, 1),
    es("bly", 3, "bly", 3, 1),
    es("ly", 2, "", 0, 1),
    es("tary", 4, "tary", 4, 1),
    es("sary", 4, "sary", 4, 1),
    es("rary", 4, "rary", 4, 1),
    es("ary", 3, "", 0, 1),
    es("ability", 7, "", 0, 1),
    es("tibility", 8, "tible", 5, 1),
    es("ibility", 7, "", 0, 1),
    es("tivity", 6, "t", 1, 1),
    es("vity", 4, "ve", 2, 1),
    es("sity", 4, "sity", 4, 1),
    es("arity", 5, "ar", 2, 1),
    es("rity", 4, "re", 2, 1),
    es("cility", 6, "cility", 6, 1),
    es("ality", 5, "", 0, 1),
    es("nity", 4, "nity", 4, 1),
    es("city", 4, "city", 4, 1),
    es("lity", 4, "lity", 4, 1),
    es("uity", 4, "uity", 4, 1),
    es("ity", 3, "", 0, 1),
    es("ify", 3, "", 0, 1),
    es("uitry", 5, "uit", 3, 1),
    es("", 0, "", 0, 1),
)

/** vendor v_endings[] (interp-stem.cpp) - struct ends rows, escan/wstem conflation data. */
val v_endings: Array<Ends> = arrayOf(
    es("iv", 2, "", 0, 1),
    es("olv", 3, "olut", 4, 1),
    es("", 0, "", 0, 1),
)

/** vendor t_endings[] (interp-stem.cpp) - struct ends rows, escan/wstem conflation data. */
val t_endings: Array<Ends> = arrayOf(
    es("eement", 6, "ee", 2, 1),
    es("rment", 5, "rment", 5, 1),
    es("ement", 5, "e", 1, 1),
    es("at", 2, "", 0, 1),
    es("antt", 4, "antt", 4, -1),
    es("lyst", 4, "lysis", 5, 1),
    es("", 0, "", 0, 1),
)

/** vendor r_endings[] (interp-stem.cpp) - struct ends rows, escan/wstem conflation data. */
val r_endings: Array<Ends> = arrayOf(
    es("vector", 6, "vector", 6, 1),
    es("ector", 5, "ect", 3, 1),
    es("titor", 5, "te", 2, 1),
    es("rator", 5, "rate", 4, 1),
    es("mator", 5, "mate", 4, 1),
    es("super", 5, "super", 5, 1),
    es("supplier", 8, "supply", 6, 1),
    es("lator", 5, "late", 4, 1),
    es("ifier", 5, "ify", 3, 1),
    es("lyser", 5, "lyze", 4, 1),
    es("lyzer", 5, "lyze", 4, 1),
    es("der", 3, "d", 1, 1),
    es("erter", 5, "ert", 3, 1),
    es("piler", 5, "pile", 4, 1),
    es("zer", 3, "ze", 2, 1),
    es("nner", 4, "n", 1, 1),
    es("", 0, "", 0, 1),
)

/**
 * Folded vendor wrapper Stem (interp-stem.cpp:413-416,
 * physLOC below the min-loc fold - rides this chunk): calls
 * wstem on the word and returns it in place.
 */
fun Stem(svPtr: Msv, ep: CharArray, word: CharArray): CharArray {
    wstem(word, strlenC(word))
    return word
}
