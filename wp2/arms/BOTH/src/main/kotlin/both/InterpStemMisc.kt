package both

// Unit 177 (BOTH B6 T3, complex): interp-stem.cpp::<misc> (345
// LOC). Batch_plan ordinal 177: complex, LOC 345, rank 229. The
// stem.c run-time support residue: the conflation-table universe
// (the PrefixStrip family and the eleven escan scan tables) and
// the Stem entry wrapper of the dollar-ZALTER/dollar-ZNORMAL word
// stemming service.
//
// Vendor behavior: the chunk is the table data plus the char*
// Stem(struct MSV*, char*, char*) wrapper; the sibling bodies are
// roster units rendered elsewhere (wstem interp-stem.cpp:418,
// roster 293 batch 10 - the wstem stub keeps the seat; escan
// interp-stem.cpp:472, roster 122 batch 4 - the escan body sits
// with its owner). The struct carrier: struct ends is the
// include/mumpsc/stem.h:31 carrier (ending/offset/replace/
// replen/next - the five fields exact; the next field is the
// escan goto index, fin=1 at defines... the vendor #define fin 1
// at interp-stem.cpp:49); struct endings is the 3-field prefix
// variant at interp-stem.cpp:51 (prefix/offset/replace - the
// prefix table strips pre/post/non). The tables: prefix (4 rows),
// d/s/g/n/e/l/c/y/v/t/r _endings (the eleven escan scan families,
// each terminated by its empty sentinel row); every row keeps
// the vendor initializer exact - including the "00l" replace of
// the ooled row (the vendor double-zero typo, byte-faithful),
// the -1 next of the antt row (the re-scan sentinel), and the 0
// (not fin) next of the final "s" and empty sentinel rows of
// s_endings (the vendor literal; the other tables close with
// fin). Stem(sv,ep,word) is the thin wrapper: wstem(word,
// strlen(word)) then return word (ep is unused vendor residue -
// the param kept; the sv param kept; both journaled h013
// neighbors - NOT dropped, the signature is the vendor contract).
//
// Quirks kept (ledgered):
//   - "ooled" replaces to "00l" (the vendor zero-glyph row kept
//     byte-faithful; the comment says cooled - the LibreOffice
//     corpus quirk).
//   - the {"antt",4,"antt",4,-1} row: next = -1 (the negative
//     goto - escan never routes it; kept exact).
//   - two g_endings rows repeat "sting" (the vendor duplicate).
//   - "writting" replaces to "write" (vendor double-t).
//
// BOTH consults (find_rule before render, events logged):
//   h021 struct_carrier - reused: struct ends (stem.h:31) and
//     struct endings (interp-stem.cpp:51) seat on the StemEnds/
//     StemEndings carriers below (field order exact).
//   h007 array_of_struct - reused: the twelve table families
//     fold to the Kotlin list-of-carrier seats (send the tab
//     hygiene note: the vendor comment columns collapse to
//     KDoc).
//   h013 dead_store - hit: the unused ep/sv wrapper params kept
//     by contract; the fin define folds to the literal 1.
//   h014 out_of_scope_call - reused: wstem stub (roster 293,
//     batch 10); escan (roster 122, batch 4) referenced in the
//     escan TODO family only - not called from this chunk.
//   h015 wrap - hit: this file.
//   h017 naming - hit: Stem overloads the LibmpscppMisc5 Stem
//     seat on parameters (the vendor char* family signature
//     kept distinct); the table names keep vendor exact.
//   h020 pointer_handle - reused: const char* endings seat on
//     the String fields; the char* word seat stays CharArray at
//     the Stem boundary.
//   h004 globals_ext - reused: the struct MSV* param of the
//     wrapper (the Msv carrier - sv unused here, journaled).

private const val STEM_FIN = 1 // interp-stem.cpp:49 "#define fin 1"

// struct ends - include/mumpsc/stem.h:31 (the five fields exact;
// next drives the escan re-scan goto family).
internal class StemEnds(
    val ending: String,
    val offset: Int,
    val replace: String,
    val replen: Int,
    val next: Int,
)

// struct endings - interp-stem.cpp:51 (the 3-field prefix strip
// family variant).
internal class StemEndings(
    val prefix: String,
    val offset: Int,
    val replace: String,
)

// const struct endings prefix[] - interp-stem.cpp:57 (pre/post/
// non strip family + sentinel).
internal val stemPrefix = listOf(
    StemEndings("pre", 3, ""),
    StemEndings("post", 4, ""),
    StemEndings("non", 3, ""),
    StemEndings("", 0, ""),
)

// const struct ends d_endings[] - interp-stem.cpp:63 (the "d"
// scan family; ooled's 00l typo and the -1 antt neighbor exact).
internal val dEndings = listOf(
    StemEnds("nned", 4, "n", 1, STEM_FIN),
    StemEnds("rated", 5, "rate", 4, STEM_FIN),
    StemEnds("cated", 5, "cate", 4, STEM_FIN),
    StemEnds("iated", 5, "iate", 4, STEM_FIN),
    StemEnds("mated", 5, "mate", 4, STEM_FIN),
    StemEnds("gated", 5, "gate", 4, STEM_FIN),
    StemEnds("lated", 5, "late", 4, STEM_FIN),
    StemEnds("bated", 5, "bate", 4, STEM_FIN),
    StemEnds("nated", 5, "nate", 4, STEM_FIN),
    StemEnds("ated", 4, "te", 2, STEM_FIN),
    StemEnds("eted", 4, "ete", 3, STEM_FIN),
    StemEnds("ated", 4, "ate", 3, STEM_FIN),
    StemEnds("kted", 4, "kt", 2, STEM_FIN),
    StemEnds("oted", 4, "ote", 3, STEM_FIN),
    StemEnds("uted", 4, "ute", 3, STEM_FIN),
    StemEnds("sted", 4, "ste", 3, STEM_FIN),
    StemEnds("ted", 3, "t", 1, STEM_FIN),
    StemEnds("ered", 4, "er", 2, STEM_FIN),
    StemEnds("dded", 4, "dd", 2, STEM_FIN),
    StemEnds("rred", 4, "r", 1, STEM_FIN),
    StemEnds("ored", 4, "r", 1, STEM_FIN),
    StemEnds("red", 3, "re", 2, STEM_FIN),
    StemEnds("zed", 3, "ze", 2, STEM_FIN),
    StemEnds("oded", 4, "de", 2, STEM_FIN),
    StemEnds("oved", 4, "ove", 3, STEM_FIN),
    StemEnds("eved", 4, "eve", 3, STEM_FIN),
    StemEnds("ied", 3, "y", 1, STEM_FIN),
    StemEnds("ced", 3, "ce", 2, STEM_FIN),
    StemEnds("olled", 4, "ol", 2, STEM_FIN),
    StemEnds("alled", 5, "al", 2, STEM_FIN),
    StemEnds("ooled", 5, "00l", 3, STEM_FIN),
    StemEnds("oled", 4, "l", 1, STEM_FIN),
    StemEnds("lled", 4, "l", 1, STEM_FIN),
    StemEnds("led", 3, "le", 2, STEM_FIN),
    StemEnds("reed", 4, "ree", 3, STEM_FIN),
    StemEnds("eed", 3, "eed", 3, STEM_FIN),
    StemEnds("ased", 4, "ased", 4, STEM_FIN),
    StemEnds("ssed", 4, "ss", 2, STEM_FIN),
    StemEnds("ccused", 6, "ccuse", 5, STEM_FIN),
    StemEnds("used", 4, "us", 2, STEM_FIN),
    StemEnds("sed", 3, "se", 2, STEM_FIN),
    StemEnds("ved", 3, "ve", 2, STEM_FIN),
    StemEnds("ined", 4, "ne", 2, STEM_FIN),
    StemEnds("ned", 3, "n", 1, STEM_FIN),
    StemEnds("ached", 6, "ache", 4, STEM_FIN),
    StemEnds("hed", 3, "h", 1, STEM_FIN),
    StemEnds("ged", 3, "ge", 2, STEM_FIN),
    StemEnds("med", 3, "me", 2, STEM_FIN),
    StemEnds("mmed", 4, "m", 1, STEM_FIN),
    StemEnds("ued", 3, "ue", 2, STEM_FIN),
    StemEnds("mbed", 4, "b", 1, STEM_FIN),
    StemEnds("bed", 3, "be", 2, STEM_FIN),
    StemEnds("ended", 5, "end", 3, STEM_FIN),
    StemEnds("nded", 4, "d", 1, STEM_FIN),
    StemEnds("aded", 4, "d", 1, STEM_FIN),
    StemEnds("ded", 3, "de", 2, STEM_FIN),
    StemEnds("ored", 4, "or", 2, STEM_FIN),
    StemEnds("pped", 4, "p", 1, STEM_FIN),
    StemEnds("ed", 2, "", 0, STEM_FIN),
    StemEnds("", 0, "", 0, STEM_FIN),
)

// const struct ends s_endings[] (the "s" scan family; the s
// realizations of the ves/ies/ies triple, the final row's 0).
internal val sEndings = listOf(
    StemEnds("tiveness", 5, "tive", 4, STEM_FIN),
    StemEnds("iness", 5, "iness", 0, STEM_FIN),
    StemEnds("ness", 4, "", 0, STEM_FIN),
    StemEnds("ss", 2, "ss", 1, STEM_FIN),
    StemEnds("sses", 4, "ss", 2, STEM_FIN),
    StemEnds("xes", 3, "x", 1, STEM_FIN),
    StemEnds("xas", 3, "xas", 3, STEM_FIN),
    StemEnds("ious", 4, "ious", 4, STEM_FIN),
    StemEnds("nous", 4, "nous", 4, STEM_FIN),
    StemEnds("uous", 4, "e", 1, STEM_FIN),
    StemEnds("ous", 3, "", 0, STEM_FIN),
    StemEnds("aries", 5, "ary", 3, STEM_FIN),
    StemEnds("eries", 5, "ery", 3, STEM_FIN),
    StemEnds("ories", 5, "ory", 3, STEM_FIN),
    StemEnds("tries", 5, "try", 4, STEM_FIN),
    StemEnds("ries", 4, "ries", 4, STEM_FIN),
    StemEnds("ies", 3, "y", 1, STEM_FIN),
    StemEnds("bios", 4, "bios", 4, STEM_FIN),
    StemEnds("dus", 3, "dus", 3, STEM_FIN),
    StemEnds("ves", 3, "ve", 2, STEM_FIN),
    StemEnds("res", 3, "re", 2, STEM_FIN),
    StemEnds("sis", 3, "sis", 3, STEM_FIN),
    StemEnds("ases", 4, "ase", 3, STEM_FIN),
    StemEnds("ses", 3, "s", 1, STEM_FIN),
    StemEnds("ces", 3, "ce", 2, STEM_FIN),
    StemEnds("dos", 3, "dos", 3, STEM_FIN),
    StemEnds("des", 3, "de", 2, STEM_FIN),
    StemEnds("cus", 3, "cus", 3, STEM_FIN),
    StemEnds("tus", 3, "tus", 3, STEM_FIN),
    StemEnds("ays", 3, "ay", 2, STEM_FIN),
    StemEnds("nys", 3, "ny", 2, STEM_FIN),
    StemEnds("ys", 2, "ys", 2, STEM_FIN),
    StemEnds("ius", 3, "ius", 3, STEM_FIN),
    StemEnds("is", 2, "", 0, STEM_FIN),
    StemEnds("hes", 3, "h", 1, STEM_FIN),
    StemEnds("ves", 3, "ve", 2, STEM_FIN),
    StemEnds("tes", 3, "te", 2, STEM_FIN),
    StemEnds("nus", 3, "nu", 2, STEM_FIN),
    StemEnds("us", 2, "us", 2, STEM_FIN),
    StemEnds("news", 4, "news", 4, STEM_FIN),
    StemEnds("s", 1, "", 0, 0),
    StemEnds("", 0, "", 0, 0),
)

// const struct ends g_endings[] (the "g" scan family; the
// writting double-t and the duplicated sting rows exact).
internal val gEndings = listOf(
    StemEnds("zing", 4, "ze", 2, STEM_FIN),
    StemEnds("olling", 5, "l", 2, STEM_FIN),
    StemEnds("lling", 5, "ll", 2, STEM_FIN),
    StemEnds("aling", 5, "al", 2, STEM_FIN),
    StemEnds("eling", 5, "el", 2, STEM_FIN),
    StemEnds("iling", 5, "il", 2, STEM_FIN),
    StemEnds("ling", 4, "le", 2, STEM_FIN),
    StemEnds("writting", 8, "write", 5, STEM_FIN),
    StemEnds("iting", 5, "it", 2, STEM_FIN),
    StemEnds("ipting", 6, "ipt", 3, STEM_FIN),
    StemEnds("epting", 6, "ept", 3, STEM_FIN),
    StemEnds("apting", 6, "apt", 3, STEM_FIN),
    StemEnds("pting", 5, "t", 1, STEM_FIN),
    StemEnds("mming", 5, "m", 1, STEM_FIN),
    StemEnds("cting", 5, "ct", 2, STEM_FIN),
    StemEnds("inting", 6, "int", 3, STEM_FIN),
    StemEnds("nting", 5, "nt", 2, STEM_FIN),
    StemEnds("fting", 5, "ft", 2, STEM_FIN),
    StemEnds("rting", 5, "rt", 2, STEM_FIN),
    StemEnds("atting", 6, "at", 2, STEM_FIN),
    StemEnds("cating", 6, "cate", 4, STEM_FIN),
    StemEnds("dating", 6, "date", 4, STEM_FIN),
    StemEnds("nating", 6, "nate", 4, STEM_FIN),
    StemEnds("mating", 6, "mate", 4, STEM_FIN),
    StemEnds("eating", 6, "eate", 4, STEM_FIN),
    StemEnds("operating", 9, "operating", 9, STEM_FIN),
    StemEnds("pating", 6, "pate", 4, STEM_FIN),
    StemEnds("lating", 6, "late", 4, STEM_FIN),
    StemEnds("rating", 6, "rate", 4, STEM_FIN),
    StemEnds("ating", 5, "at", 2, STEM_FIN),
    StemEnds("sting", 5, "st", 2, STEM_FIN),
    StemEnds("eting", 5, "et", 2, STEM_FIN),
    StemEnds("hting", 5, "ht", 2, STEM_FIN),
    StemEnds("ting", 4, "te", 2, STEM_FIN),
    StemEnds("sting", 5, "st", 2, STEM_FIN),
    StemEnds("rming", 5, "rm", 2, STEM_FIN),
    StemEnds("mming", 5, "m", 1, STEM_FIN),
    StemEnds("ming", 4, "me", 2, STEM_FIN),
    StemEnds("ering", 5, "er", 2, STEM_FIN),
    StemEnds("loring", 6, "lore", 4, STEM_FIN),
    StemEnds("oring", 5, "or", 2, STEM_FIN),
    StemEnds("aring", 5, "ar", 2, STEM_FIN),
    StemEnds("uing", 4, "ue", 2, STEM_FIN),
    StemEnds("ring", 4, "re", 2, STEM_FIN),
    StemEnds("rning", 5, "rn", 2, STEM_FIN),
    StemEnds("nning", 5, "n", 1, STEM_FIN),
    StemEnds("oning", 5, "on", 2, STEM_FIN),
    StemEnds("gning", 5, "gn", 2, STEM_FIN),
    StemEnds("fining", 6, "ine", 3, STEM_FIN),
    StemEnds("ining", 5, "in", 2, STEM_FIN),
    StemEnds("ning", 4, "ne", 2, STEM_FIN),
    StemEnds("cing", 4, "ce", 2, STEM_FIN),
    StemEnds("ving", 4, "ve", 2, STEM_FIN),
    StemEnds("ssing", 5, "ss", 2, STEM_FIN),
    StemEnds("using", 5, "use", 3, STEM_FIN),
    StemEnds("asing", 5, "as", 2, STEM_FIN),
    StemEnds("sing", 4, "se", 2, STEM_FIN),
    StemEnds("gging", 5, "g", 1, STEM_FIN),
    StemEnds("ging", 4, "ge", 2, STEM_FIN),
    StemEnds("bing", 4, "be", 2, STEM_FIN),
    StemEnds("rding", 5, "rding", 5, STEM_FIN),
    StemEnds("nding", 5, "nd", 2, STEM_FIN),
    StemEnds("dding", 5, "d", 1, STEM_FIN),
    StemEnds("eding", 5, "ed", 2, STEM_FIN),
    StemEnds("ding", 4, "de", 2, STEM_FIN),
    StemEnds("aying", 5, "ay", 2, STEM_FIN),
    StemEnds("nying", 5, "ny", 2, STEM_FIN),
    StemEnds("lying", 5, "y", 1, STEM_FIN),
    StemEnds("ying", 4, "y", 1, STEM_FIN),
    StemEnds("ing", 3, "", 0, STEM_FIN),
    StemEnds("", 0, "", 0, STEM_FIN),
)

// const struct ends n_endings[] (the "n" scan family).
internal val nEndings = listOf(
    StemEnds("aturation", 9, "aturate", 7, STEM_FIN),
    StemEnds("eration", 7, "eration", 7, STEM_FIN),
    StemEnds("tration", 7, "ter", 3, STEM_FIN),
    StemEnds("oration", 7, "r", 1, STEM_FIN),
    StemEnds("ration", 6, "re", 2, STEM_FIN),
    StemEnds("tition", 6, "te", 2, STEM_FIN),
    StemEnds("ection", 6, "ect", 3, STEM_FIN),
    StemEnds("version", 7, "version", 7, STEM_FIN),
    StemEnds("ersion", 6, "ert", 3, STEM_FIN),
    StemEnds("ssion", 5, "ss", 2, STEM_FIN),
    StemEnds("rmation", 7, "rmation", 7, STEM_FIN),
    StemEnds("mation", 6, "mate", 4, STEM_FIN),
    StemEnds("otation", 7, "otate", 5, STEM_FIN),
    StemEnds("ication", 7, "ication", 7, STEM_FIN),
    StemEnds("cation", 6, "cate", 4, STEM_FIN),
    StemEnds("iction", 6, "iction", 6, STEM_FIN),
    StemEnds("nction", 6, "nction", 6, STEM_FIN),
    StemEnds("uction", 6, "uce", 3, STEM_FIN),
    StemEnds("ction", 5, "ct", 2, STEM_FIN),
    StemEnds("ilation", 7, "ile", 3, STEM_FIN),
    StemEnds("lation", 6, "late", 4, STEM_FIN),
    StemEnds("tation", 6, "t", 1, STEM_FIN),
    StemEnds("nician", 6, "nical", 5, STEM_FIN),
    StemEnds("ican", 4, "ica", 3, STEM_FIN),
    StemEnds("dation", 6, "date", 4, STEM_FIN),
    StemEnds("", 0, "", 0, STEM_FIN),
)

// const struct ends e_endings[] (the "e" scan family).
internal val eEndings = listOf(
    StemEnds("rable", 5, "are", 3, STEM_FIN),
    StemEnds("eable", 5, "e", 1, STEM_FIN),
    StemEnds("lable", 5, "le", 2, STEM_FIN),
    StemEnds("ortable", 7, "ortable", 2, STEM_FIN),
    StemEnds("table", 5, "te", 2, STEM_FIN),
    StemEnds("zable", 5, "ze", 2, STEM_FIN),
    StemEnds("able", 4, "", 0, STEM_FIN),
    StemEnds("tible", 5, "tible", 5, STEM_FIN),
    StemEnds("ible", 4, "", 0, STEM_FIN),
    StemEnds("titive", 6, "te", 2, STEM_FIN),
    StemEnds("ective", 6, "ect", 3, STEM_FIN),
    StemEnds("native", 6, "nate", 4, STEM_FIN),
    StemEnds("lyze", 4, "lysis", 5, STEM_FIN),
    StemEnds("", 0, "", 0, STEM_FIN),
)

// const struct ends l_endings[] (the "l" scan family).
internal val lEndings = listOf(
    StemEnds("ational", 7, "ational", 7, STEM_FIN),
    StemEnds("tional", 6, "tion", 4, STEM_FIN),
    StemEnds("tial", 4, "tial", 4, STEM_FIN),
    StemEnds("ial", 3, "ial", 3, STEM_FIN),
    StemEnds("eral", 4, "eral", 4, STEM_FIN),
    StemEnds("ral", 3, "re", 2, STEM_FIN),
    StemEnds("ual", 3, "ual", 3, STEM_FIN),
    StemEnds("mal", 3, "mal", 3, STEM_FIN),
    StemEnds("onal", 4, "on", 2, STEM_FIN),
    StemEnds("nal", 3, "nal", 3, STEM_FIN),
    StemEnds("hical", 5, "hic", 3, STEM_FIN),
    StemEnds("cal", 3, "cal", 3, STEM_FIN),
    StemEnds("eal", 3, "eal", 3, STEM_FIN),
    StemEnds("bal", 3, "bal", 3, STEM_FIN),
    StemEnds("sal", 3, "sal", 3, STEM_FIN),
    StemEnds("val", 3, "ve", 2, STEM_FIN),
    StemEnds("ntal", 4, "nt", 2, STEM_FIN),
    StemEnds("tal", 3, "tal", 3, STEM_FIN),
    StemEnds("al", 2, "", 0, STEM_FIN),
    StemEnds("", 0, "", 0, STEM_FIN),
)

// const struct ends c_endings[] (the "c" scan family).
internal val cEndings = listOf(
    StemEnds("istic", 5, "", 0, STEM_FIN),
    StemEnds("ific", 4, "", 0, STEM_FIN),
    StemEnds("thmic", 5, "thm", 3, STEM_FIN),
    StemEnds("matic", 5, "mate", 4, STEM_FIN),
    StemEnds("", 0, "", 0, STEM_FIN),
)

// const struct ends y_endings[] (the "y" scan family; the ically
// double-take rows exact).
internal val yEndings = listOf(
    StemEnds("rably", 5, "re", 2, STEM_FIN),
    StemEnds("ably", 4, "", 0, STEM_FIN),
    StemEnds("ibly", 4, "", 0, STEM_FIN),
    StemEnds("ily", 3, "", 0, STEM_FIN),
    StemEnds("tically", 7, "te", 2, STEM_FIN),
    StemEnds("fically", 7, "fic", 3, STEM_FIN),
    StemEnds("ically", 6, "ical", 4, STEM_FIN),
    StemEnds("ically", 6, "ic", 2, STEM_FIN),
    StemEnds("cally", 5, "c", 1, STEM_FIN),
    StemEnds("titively", 6, "te", 2, STEM_FIN),
    StemEnds("bly", 3, "bly", 3, STEM_FIN),
    StemEnds("ly", 2, "", 0, STEM_FIN),
    StemEnds("tary", 4, "tary", 4, STEM_FIN),
    StemEnds("sary", 4, "sary", 4, STEM_FIN),
    StemEnds("rary", 4, "rary", 4, STEM_FIN),
    StemEnds("ary", 3, "", 0, STEM_FIN),
    StemEnds("ability", 7, "", 0, STEM_FIN),
    StemEnds("tibility", 8, "tible", 5, STEM_FIN),
    StemEnds("ibility", 7, "", 0, STEM_FIN),
    StemEnds("tivity", 6, "t", 1, STEM_FIN),
    StemEnds("vity", 4, "ve", 2, STEM_FIN),
    StemEnds("sity", 4, "sity", 4, STEM_FIN),
    StemEnds("arity", 5, "ar", 2, STEM_FIN),
    StemEnds("rity", 4, "re", 2, STEM_FIN),
    StemEnds("cility", 6, "cility", 6, STEM_FIN),
    StemEnds("ality", 5, "", 0, STEM_FIN),
    StemEnds("nity", 4, "nity", 4, STEM_FIN),
    StemEnds("city", 4, "city", 4, STEM_FIN),
    StemEnds("lity", 4, "lity", 4, STEM_FIN),
    StemEnds("uity", 4, "uity", 4, STEM_FIN),
    StemEnds("ity", 3, "", 0, STEM_FIN),
    StemEnds("ify", 3, "", 0, STEM_FIN),
    StemEnds("uitry", 5, "uit", 3, STEM_FIN),
    StemEnds("", 0, "", 0, STEM_FIN),
)

// const struct ends v_endings[] (the "v" scan family).
internal val vEndings = listOf(
    StemEnds("iv", 2, "", 0, STEM_FIN),
    StemEnds("olv", 3, "olut", 4, STEM_FIN),
    StemEnds("", 0, "", 0, STEM_FIN),
)

// const struct ends t_endings[] (the "t" scan family; the antt
// -1 row exact).
internal val tEndings = listOf(
    StemEnds("eement", 6, "ee", 2, STEM_FIN),
    StemEnds("rment", 5, "rment", 5, STEM_FIN),
    StemEnds("ement", 5, "e", 1, STEM_FIN),
    StemEnds("at", 2, "", 0, STEM_FIN),
    StemEnds("antt", 4, "antt", 4, -1),
    StemEnds("lyst", 4, "lysis", 5, STEM_FIN),
    StemEnds("", 0, "", 0, STEM_FIN),
)

// const struct ends r_endings[] (the "r" scan family).
internal val rEndings = listOf(
    StemEnds("vector", 6, "vector", 6, STEM_FIN),
    StemEnds("ector", 5, "ect", 3, STEM_FIN),
    StemEnds("titor", 5, "te", 2, STEM_FIN),
    StemEnds("rator", 5, "rate", 4, STEM_FIN),
    StemEnds("mator", 5, "mate", 4, STEM_FIN),
    StemEnds("super", 5, "super", 5, STEM_FIN),
    StemEnds("supplier", 8, "supply", 6, STEM_FIN),
    StemEnds("lator", 5, "late", 4, STEM_FIN),
    StemEnds("ifier", 5, "ify", 3, STEM_FIN),
    StemEnds("lyser", 5, "lyze", 4, STEM_FIN),
    StemEnds("lyzer", 5, "lyze", 4, STEM_FIN),
    StemEnds("der", 3, "d", 1, STEM_FIN),
    StemEnds("erter", 5, "ert", 3, STEM_FIN),
    StemEnds("piler", 5, "pile", 4, STEM_FIN),
    StemEnds("zer", 3, "ze", 2, STEM_FIN),
    StemEnds("nner", 4, "n", 1, STEM_FIN),
    StemEnds("", 0, "", 0, STEM_FIN),
)

// char * Stem(struct MSV *, char *, char *) - interp-stem.cpp:413
// (the wrapper: wstem(word,strlen(word)); return word; the ep/sv
// params are the vendor contract, both unused in the body).
internal fun Stem(sv: Msv, ep: CharArray, word: CharArray): CharArray {
    wstem(word, strlenC(word))
    return word
}
