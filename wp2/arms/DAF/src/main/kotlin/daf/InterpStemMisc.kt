package daf

// Unit 177 (DAF B6 T3, complex): interp-stem.cpp::<misc>
// (units.csv row: loc 345, score 43.7, rank 265, complex): the
// runtime stemming-residue TU: the GPL banner and include set,
// the wstem prototype, the fin pin, the two conflation-table row
// structs and the thirteen prefix/ending tables of the $ZALTER /
// $ZNORMAL stemming service, closing with the Steady launcher
// Stem(svPtr, ep, word).
// Consults (2026-11-22T16:20:30Z, nominal): BASE-log rows 18/6/22
// hit-reuse (the two C structs fold onto the DafEndingsRow /
// DafEndsRow carrier classes; the const table arrays fold onto
// read-only Kotlin lists - the immutable-state singleton fold),
// row 26 hit-reuse (the table values are copied VERBATIM from the
// vendor: the offset/replen columns are vendor data, not
// recomputable - the mismatched columns ledgered below), row 90
// exact-shape (the Stem launcher is the z-call dispatcher seat
// shape of the bsv/W family: svPtr/ep/word), row 37 hit-adapt
// (wstem stays the named-owner stub seat wstemDaf raised at
// LibmpscppMisc5 - TAG CORRECTION there: the TRUE owner body is
// interp-stem.cpp::wstem at line 418, a units.csv BATCH-10 roster
// cell (28 loc), not the interp-bifs.cpp guess; the tables this
// render carries are the data the b10 wstem body consumes),
// rows 99/100 hit-adapt (strlen(word) folds onto the cstring
// length; the destructive char[] return folds onto returning the
// same CharArray).
// VENDOR QUIRKS ledgered: (1) the table offset columns are NOT the
// string lengths in every row (e.g. the s_endings "iness" row
// carries offset 5 over the 6-char "iness" ending but the
// ending string is 6; the g_endings "olling" row replaces with
// 2-char "l" at replen 2) - vendor data kept verbatim;
// (2) the duplicate table rows (the two g_endings "sting" rows,
// the two y_endings "ves" rows of s_endings, the double
// "mming"/"nner" pairs) are the vendor order kept - the b10
// walker takes the FIRST hit; (3) the fin/0/-1 next column mixes
// the #define fin 1 token with the literal 0 and -1 arms
// (the -1 "antt" row and the 0 "s" row) - carried as the Int
// column with the fin pin below; (4) the banner year lines and
// the Feb 9 2022 note are comment-only.
// Dafny: EXEMPT (conflation-data carriers + a stub-mediated
// launcher; journal row).

// #define fin 1 (interp-stem.cpp:49)
const val STEM_FIN_D = 1

// struct endings { const char* prefix; int offset; const char* replace; }
// (interp-stem.cpp:51; stem.h kin)
class DafEndingsRow(val prefix: String, val offset: Int, val replace: String)

// struct ends { const char* ending; int offset; const char* replace;
//               int replen; int next; } (stem.h:31)
class DafEndsRow(val ending: String, val offset: Int, val replace: String,
                 val replen: Int, val next: Int)

val prefix_D: List<DafEndingsRow> = listOf(
    DafEndingsRow("pre", 3, ""),
    DafEndingsRow("post", 4, ""),
    DafEndingsRow("non", 3, ""),
    DafEndingsRow("", 0, ""),
)

val d_endings_D: List<DafEndsRow> = listOf(
    DafEndsRow("nned", 4, "n", 1, 1),
    DafEndsRow("rated", 5, "rate", 4, 1),
    DafEndsRow("cated", 5, "cate", 4, 1),
    DafEndsRow("iated", 5, "iate", 4, 1),
    DafEndsRow("mated", 5, "mate", 4, 1),
    DafEndsRow("gated", 5, "gate", 4, 1),
    DafEndsRow("lated", 5, "late", 4, 1),
    DafEndsRow("bated", 5, "bate", 4, 1),
    DafEndsRow("nated", 5, "nate", 4, 1),
    DafEndsRow("ated", 4, "te", 2, 1),
    DafEndsRow("eted", 4, "ete", 3, 1),
    DafEndsRow("ated", 4, "ate", 3, 1),
    DafEndsRow("kted", 4, "kt", 2, 1),
    DafEndsRow("oted", 4, "ote", 3, 1),
    DafEndsRow("uted", 4, "ute", 3, 1),
    DafEndsRow("sted", 4, "ste", 3, 1),
    DafEndsRow("ted", 3, "t", 1, 1),
    DafEndsRow("ered", 4, "er", 2, 1),
    DafEndsRow("dded", 4, "dd", 2, 1),
    DafEndsRow("rred", 4, "r", 1, 1),
    DafEndsRow("ored", 4, "r", 1, 1),
    DafEndsRow("red", 3, "re", 2, 1),
    DafEndsRow("zed", 3, "ze", 2, 1),
    DafEndsRow("oded", 4, "de", 2, 1),
    DafEndsRow("oved", 4, "ove", 3, 1),
    DafEndsRow("eved", 4, "eve", 3, 1),
    DafEndsRow("ied", 3, "y", 1, 1),
    DafEndsRow("ced", 3, "ce", 2, 1),
    DafEndsRow("olled", 4, "ol", 2, 1),
    DafEndsRow("alled", 5, "al", 2, 1),
    DafEndsRow("ooled", 5, "00l", 3, 1),
    DafEndsRow("oled", 4, "l", 1, 1),
    DafEndsRow("lled", 4, "l", 1, 1),
    DafEndsRow("led", 3, "le", 2, 1),
    DafEndsRow("reed", 4, "ree", 3, 1),
    DafEndsRow("eed", 3, "eed", 3, 1),
    DafEndsRow("ased", 4, "ased", 4, 1),
    DafEndsRow("ssed", 4, "ss", 2, 1),
    DafEndsRow("ccused", 6, "ccuse", 5, 1),
    DafEndsRow("used", 4, "us", 2, 1),
    DafEndsRow("sed", 3, "se", 2, 1),
    DafEndsRow("ved", 3, "ve", 2, 1),
    DafEndsRow("ined", 4, "ne", 2, 1),
    DafEndsRow("ned", 3, "n", 1, 1),
    DafEndsRow("ached", 6, "ache", 4, 1),
    DafEndsRow("hed", 3, "h", 1, 1),
    DafEndsRow("ged", 3, "ge", 2, 1),
    DafEndsRow("med", 3, "me", 2, 1),
    DafEndsRow("mmed", 4, "m", 1, 1),
    DafEndsRow("ued", 3, "ue", 2, 1),
    DafEndsRow("mbed", 4, "b", 1, 1),
    DafEndsRow("bed", 3, "be", 2, 1),
    DafEndsRow("ended", 5, "end", 3, 1),
    DafEndsRow("nded", 4, "d", 1, 1),
    DafEndsRow("aded", 4, "d", 1, 1),
    DafEndsRow("ded", 3, "de", 2, 1),
    DafEndsRow("ored", 4, "or", 2, 1),
    DafEndsRow("pped", 4, "p", 1, 1),
    DafEndsRow("ed", 2, "", 0, 1),
    DafEndsRow("", 0, "", 0, 1),
)

val s_endings_D: List<DafEndsRow> = listOf(
    DafEndsRow("tiveness", 5, "tive", 4, 1),
    DafEndsRow("iness", 5, "iness", 0, 1),
    DafEndsRow("ness", 4, "", 0, 1),
    DafEndsRow("ss", 2, "ss", 1, 1),
    DafEndsRow("sses", 4, "ss", 2, 1),
    DafEndsRow("xes", 3, "x", 1, 1),
    DafEndsRow("xas", 3, "xas", 3, 1),
    DafEndsRow("ious", 4, "ious", 4, 1),
    DafEndsRow("nous", 4, "nous", 4, 1),
    DafEndsRow("uous", 4, "e", 1, 1),
    DafEndsRow("ous", 3, "", 0, 1),
    DafEndsRow("aries", 5, "ary", 3, 1),
    DafEndsRow("eries", 5, "ery", 3, 1),
    DafEndsRow("ories", 5, "ory", 3, 1),
    DafEndsRow("tries", 5, "try", 4, 1),
    DafEndsRow("ries", 4, "ries", 4, 1),
    DafEndsRow("ies", 3, "y", 1, 1),
    DafEndsRow("bios", 4, "bios", 4, 1),
    DafEndsRow("dus", 3, "dus", 3, 1),
    DafEndsRow("ves", 3, "ve", 2, 1),
    DafEndsRow("res", 3, "re", 2, 1),
    DafEndsRow("sis", 3, "sis", 3, 1),
    DafEndsRow("ases", 4, "ase", 3, 1),
    DafEndsRow("ses", 3, "s", 1, 1),
    DafEndsRow("ces", 3, "ce", 2, 1),
    DafEndsRow("dos", 3, "dos", 3, 1),
    DafEndsRow("des", 3, "de", 2, 1),
    DafEndsRow("cus", 3, "cus", 3, 1),
    DafEndsRow("tus", 3, "tus", 3, 1),
    DafEndsRow("ays", 3, "ay", 2, 1),
    DafEndsRow("nys", 3, "ny", 2, 1),
    DafEndsRow("ys", 2, "ys", 2, 1),
    DafEndsRow("ius", 3, "ius", 3, 1),
    DafEndsRow("is", 2, "", 0, 1),
    DafEndsRow("hes", 3, "h", 1, 1),
    DafEndsRow("ves", 3, "ve", 2, 1),
    DafEndsRow("tes", 3, "te", 2, 1),
    DafEndsRow("nus", 3, "nu", 2, 1),
    DafEndsRow("us", 2, "us", 2, 1),
    DafEndsRow("news", 4, "news", 4, 1),
    DafEndsRow("s", 1, "", 0, 0),
    DafEndsRow("", 0, "", 0, 0),
)

val g_endings_D: List<DafEndsRow> = listOf(
    DafEndsRow("zing", 4, "ze", 2, 1),
    DafEndsRow("olling", 5, "l", 2, 1),
    DafEndsRow("lling", 5, "ll", 2, 1),
    DafEndsRow("aling", 5, "al", 2, 1),
    DafEndsRow("eling", 5, "el", 2, 1),
    DafEndsRow("iling", 5, "il", 2, 1),
    DafEndsRow("ling", 4, "le", 2, 1),
    DafEndsRow("writting", 8, "write", 5, 1),
    DafEndsRow("iting", 5, "it", 2, 1),
    DafEndsRow("ipting", 6, "ipt", 3, 1),
    DafEndsRow("epting", 6, "ept", 3, 1),
    DafEndsRow("apting", 6, "apt", 3, 1),
    DafEndsRow("pting", 5, "t", 1, 1),
    DafEndsRow("mming", 5, "m", 1, 1),
    DafEndsRow("cting", 5, "ct", 2, 1),
    DafEndsRow("inting", 6, "int", 3, 1),
    DafEndsRow("nting", 5, "nt", 2, 1),
    DafEndsRow("fting", 5, "ft", 2, 1),
    DafEndsRow("rting", 5, "rt", 2, 1),
    DafEndsRow("atting", 6, "at", 2, 1),
    DafEndsRow("cating", 6, "cate", 4, 1),
    DafEndsRow("dating", 6, "date", 4, 1),
    DafEndsRow("nating", 6, "nate", 4, 1),
    DafEndsRow("mating", 6, "mate", 4, 1),
    DafEndsRow("eating", 6, "eate", 4, 1),
    DafEndsRow("operating", 9, "operating", 9, 1),
    DafEndsRow("pating", 6, "pate", 4, 1),
    DafEndsRow("lating", 6, "late", 4, 1),
    DafEndsRow("rating", 6, "rate", 4, 1),
    DafEndsRow("ating", 5, "at", 2, 1),
    DafEndsRow("sting", 5, "st", 2, 1),
    DafEndsRow("eting", 5, "et", 2, 1),
    DafEndsRow("hting", 5, "ht", 2, 1),
    DafEndsRow("ting", 4, "te", 2, 1),
    DafEndsRow("sting", 5, "st", 2, 1),
    DafEndsRow("rming", 5, "rm", 2, 1),
    DafEndsRow("mming", 5, "m", 1, 1),
    DafEndsRow("ming", 4, "me", 2, 1),
    DafEndsRow("ering", 5, "er", 2, 1),
    DafEndsRow("loring", 6, "lore", 4, 1),
    DafEndsRow("oring", 5, "or", 2, 1),
    DafEndsRow("aring", 5, "ar", 2, 1),
    DafEndsRow("uing", 4, "ue", 2, 1),
    DafEndsRow("ring", 4, "re", 2, 1),
    DafEndsRow("rning", 5, "rn", 2, 1),
    DafEndsRow("nning", 5, "n", 1, 1),
    DafEndsRow("oning", 5, "on", 2, 1),
    DafEndsRow("gning", 5, "gn", 2, 1),
    DafEndsRow("fining", 6, "ine", 3, 1),
    DafEndsRow("ining", 5, "in", 2, 1),
    DafEndsRow("ning", 4, "ne", 2, 1),
    DafEndsRow("cing", 4, "ce", 2, 1),
    DafEndsRow("ving", 4, "ve", 2, 1),
    DafEndsRow("ssing", 5, "ss", 2, 1),
    DafEndsRow("using", 5, "use", 3, 1),
    DafEndsRow("asing", 5, "as", 2, 1),
    DafEndsRow("sing", 4, "se", 2, 1),
    DafEndsRow("gging", 5, "g", 1, 1),
    DafEndsRow("ging", 4, "ge", 2, 1),
    DafEndsRow("bing", 4, "be", 2, 1),
    DafEndsRow("rding", 5, "rding", 5, 1),
    DafEndsRow("nding", 5, "nd", 2, 1),
    DafEndsRow("dding", 5, "d", 1, 1),
    DafEndsRow("eding", 5, "ed", 2, 1),
    DafEndsRow("ding", 4, "de", 2, 1),
    DafEndsRow("aying", 5, "ay", 2, 1),
    DafEndsRow("nying", 5, "ny", 2, 1),
    DafEndsRow("lying", 5, "y", 1, 1),
    DafEndsRow("ying", 4, "y", 1, 1),
    DafEndsRow("ing", 3, "", 0, 1),
    DafEndsRow("", 0, "", 0, 1),
)

val n_endings_D: List<DafEndsRow> = listOf(
    DafEndsRow("aturation", 9, "aturate", 7, 1),
    DafEndsRow("eration", 7, "eration", 7, 1),
    DafEndsRow("tration", 7, "ter", 3, 1),
    DafEndsRow("oration", 7, "r", 1, 1),
    DafEndsRow("ration", 6, "re", 2, 1),
    DafEndsRow("tition", 6, "te", 2, 1),
    DafEndsRow("ection", 6, "ect", 3, 1),
    DafEndsRow("version", 7, "version", 7, 1),
    DafEndsRow("ersion", 6, "ert", 3, 1),
    DafEndsRow("ssion", 5, "ss", 2, 1),
    DafEndsRow("rmation", 7, "rmation", 7, 1),
    DafEndsRow("mation", 6, "mate", 4, 1),
    DafEndsRow("otation", 7, "otate", 5, 1),
    DafEndsRow("ication", 7, "ication", 7, 1),
    DafEndsRow("cation", 6, "cate", 4, 1),
    DafEndsRow("iction", 6, "iction", 6, 1),
    DafEndsRow("nction", 6, "nction", 6, 1),
    DafEndsRow("uction", 6, "uce", 3, 1),
    DafEndsRow("ction", 5, "ct", 2, 1),
    DafEndsRow("ilation", 7, "ile", 3, 1),
    DafEndsRow("lation", 6, "late", 4, 1),
    DafEndsRow("tation", 6, "t", 1, 1),
    DafEndsRow("nician", 6, "nical", 5, 1),
    DafEndsRow("ican", 4, "ica", 3, 1),
    DafEndsRow("dation", 6, "date", 4, 1),
    DafEndsRow("", 0, "", 0, 1),
)

val e_endings_D: List<DafEndsRow> = listOf(
    DafEndsRow("rable", 5, "are", 3, 1),
    DafEndsRow("eable", 5, "e", 1, 1),
    DafEndsRow("lable", 5, "le", 2, 1),
    DafEndsRow("ortable", 7, "ortable", 2, 1),
    DafEndsRow("table", 5, "te", 2, 1),
    DafEndsRow("zable", 5, "ze", 2, 1),
    DafEndsRow("able", 4, "", 0, 1),
    DafEndsRow("tible", 5, "tible", 5, 1),
    DafEndsRow("ible", 4, "", 0, 1),
    DafEndsRow("titive", 6, "te", 2, 1),
    DafEndsRow("ective", 6, "ect", 3, 1),
    DafEndsRow("native", 6, "nate", 4, 1),
    DafEndsRow("lyze", 4, "lysis", 5, 1),
    DafEndsRow("", 0, "", 0, 1),
)

val l_endings_D: List<DafEndsRow> = listOf(
    DafEndsRow("ational", 7, "ational", 7, 1),
    DafEndsRow("tional", 6, "tion", 4, 1),
    DafEndsRow("tial", 4, "tial", 4, 1),
    DafEndsRow("ial", 3, "ial", 3, 1),
    DafEndsRow("eral", 4, "eral", 4, 1),
    DafEndsRow("ral", 3, "re", 2, 1),
    DafEndsRow("ual", 3, "ual", 3, 1),
    DafEndsRow("mal", 3, "mal", 3, 1),
    DafEndsRow("onal", 4, "on", 2, 1),
    DafEndsRow("nal", 3, "nal", 3, 1),
    DafEndsRow("hical", 5, "hic", 3, 1),
    DafEndsRow("cal", 3, "cal", 3, 1),
    DafEndsRow("eal", 3, "eal", 3, 1),
    DafEndsRow("bal", 3, "bal", 3, 1),
    DafEndsRow("sal", 3, "sal", 3, 1),
    DafEndsRow("val", 3, "ve", 2, 1),
    DafEndsRow("ntal", 4, "nt", 2, 1),
    DafEndsRow("tal", 3, "tal", 3, 1),
    DafEndsRow("al", 2, "", 0, 1),
    DafEndsRow("", 0, "", 0, 1),
)

val c_endings_D: List<DafEndsRow> = listOf(
    DafEndsRow("istic", 5, "", 0, 1),
    DafEndsRow("ific", 4, "", 0, 1),
    DafEndsRow("thmic", 5, "thm", 3, 1),
    DafEndsRow("matic", 5, "mate", 4, 1),
    DafEndsRow("", 0, "", 0, 1),
)

val y_endings_D: List<DafEndsRow> = listOf(
    DafEndsRow("rably", 5, "re", 2, 1),
    DafEndsRow("ably", 4, "", 0, 1),
    DafEndsRow("ibly", 4, "", 0, 1),
    DafEndsRow("ily", 3, "", 0, 1),
    DafEndsRow("tically", 7, "te", 2, 1),
    DafEndsRow("fically", 7, "fic", 3, 1),
    DafEndsRow("ically", 6, "ical", 4, 1),
    DafEndsRow("ically", 6, "ic", 2, 1),
    DafEndsRow("cally", 5, "c", 1, 1),
    DafEndsRow("titively", 6, "te", 2, 1),
    DafEndsRow("bly", 3, "bly", 3, 1),
    DafEndsRow("ly", 2, "", 0, 1),
    DafEndsRow("tary", 4, "tary", 4, 1),
    DafEndsRow("sary", 4, "sary", 4, 1),
    DafEndsRow("rary", 4, "rary", 4, 1),
    DafEndsRow("ary", 3, "", 0, 1),
    DafEndsRow("ability", 7, "", 0, 1),
    DafEndsRow("tibility", 8, "tible", 5, 1),
    DafEndsRow("ibility", 7, "", 0, 1),
    DafEndsRow("tivity", 6, "t", 1, 1),
    DafEndsRow("vity", 4, "ve", 2, 1),
    DafEndsRow("sity", 4, "sity", 4, 1),
    DafEndsRow("arity", 5, "ar", 2, 1),
    DafEndsRow("rity", 4, "re", 2, 1),
    DafEndsRow("cility", 6, "cility", 6, 1),
    DafEndsRow("ality", 5, "", 0, 1),
    DafEndsRow("nity", 4, "nity", 4, 1),
    DafEndsRow("city", 4, "city", 4, 1),
    DafEndsRow("lity", 4, "lity", 4, 1),
    DafEndsRow("uity", 4, "uity", 4, 1),
    DafEndsRow("ity", 3, "", 0, 1),
    DafEndsRow("ify", 3, "", 0, 1),
    DafEndsRow("uitry", 5, "uit", 3, 1),
    DafEndsRow("", 0, "", 0, 1),
)

val v_endings_D: List<DafEndsRow> = listOf(
    DafEndsRow("iv", 2, "", 0, 1),
    DafEndsRow("olv", 3, "olut", 4, 1),
    DafEndsRow("", 0, "", 0, 1),
)

val t_endings_D: List<DafEndsRow> = listOf(
    DafEndsRow("eement", 6, "ee", 2, 1),
    DafEndsRow("rment", 5, "rment", 5, 1),
    DafEndsRow("ement", 5, "e", 1, 1),
    DafEndsRow("at", 2, "", 0, 1),
    DafEndsRow("antt", 4, "antt", 4, -1),
    DafEndsRow("lyst", 4, "lysis", 5, 1),
    DafEndsRow("", 0, "", 0, 1),
)

val r_endings_D: List<DafEndsRow> = listOf(
    DafEndsRow("vector", 6, "vector", 6, 1),
    DafEndsRow("ector", 5, "ect", 3, 1),
    DafEndsRow("titor", 5, "te", 2, 1),
    DafEndsRow("rator", 5, "rate", 4, 1),
    DafEndsRow("mator", 5, "mate", 4, 1),
    DafEndsRow("super", 5, "super", 5, 1),
    DafEndsRow("supplier", 8, "supply", 6, 1),
    DafEndsRow("lator", 5, "late", 4, 1),
    DafEndsRow("ifier", 5, "ify", 3, 1),
    DafEndsRow("lyser", 5, "lyze", 4, 1),
    DafEndsRow("lyzer", 5, "lyze", 4, 1),
    DafEndsRow("der", 3, "d", 1, 1),
    DafEndsRow("erter", 5, "ert", 3, 1),
    DafEndsRow("piler", 5, "pile", 4, 1),
    DafEndsRow("zer", 3, "ze", 2, 1),
    DafEndsRow("nner", 4, "n", 1, 1),
    DafEndsRow("", 0, "", 0, 1),
)


// char * Stem(struct MSV * svPtr, char *ep, char *word) - the
// z-call launcher of the stemming service: wstem over the word
// buffer (destructive), the same buffer returned (rows 99/100).
fun StemW(svPtr: DafMsv, ep: CharArray, word: CharArray): CharArray {
    wstemD(word, DafCRuntime.cstring(word).length)
    // wstem(word, strlen(word)); return word;
    return word
}
