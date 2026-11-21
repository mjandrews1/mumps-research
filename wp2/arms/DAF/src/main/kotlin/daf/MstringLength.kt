package daf

// Unit 129 (DAF B5 T1, simple): libmpscpp.cpp::mstring::Length
// (16 LOC): the mstring member that counts the occurrences of a
// global's value as substrings of the receiver's data (the \$Length
// service shape over the mstring data carrier).
// Consults: BASE-log row 101 hit-reuse (no Kotlin mstring class: the
// receiver (this->data) and the needle arg become explicit String
// params), row 92 hit-reuse (the Mglobal(RETRIEVE,...) retrieve-
// then-throw pattern with GlobalExceptionCode=GLOBAL_NOT_FOUND and
// the MumpsGlobalException fold, exactly the vendor shape; the
// exception code is the row-92 carrier const seated in the T3 stubs),
// row 33 family hit-adapt (the strstr folds onto String indexOf; the
// NUL-terminated gtmp reads are the Mglobal out-buffer cstring),
// rows 72/10 hit-adapt (the while(1)+break walk renders as the
// labeled while(true)+break loop).
// VENDOR QUIRK preserved exactly (ledgered, no repair): when the
// needle global exists but holds an EMPTY value, every strstr match
// succeeds at the cursor and the cursor advances by strlen("")==0,
// so the vendor loop does not terminate. The Kotlin render keeps
// the same walk shape (indexOf + advance-by-needle-length) and
// therefore keeps the vendor nontermination on the empty needle;
// the Dafny spec (spec/length_count.dfy) guards needle != empty,
// which is the reachable-only contract, and the journal carries
// the divergence row. Also: data "" returns 0 before the walk, so
// the quirk fires only for empty needle on non-empty data.
// Dafny: the pure counting kernel is spec'd at spec/length_count.dfy
// (this unit + unit 133 share the kernel; see GlobalLength.kt).

const val GLOBAL_NOT_FOUND_D = 2 // vendor libmpscpp.h GLOBAL_NOT_FOUND

fun mstringLengthD(data: String, needleRef: String, sv: DafMsv): Int {

    val gtmp = CharArray(STR_MAX_DAF)

    val f = MglobalDaf(RETRIEVE_D, needleCarriage(needleRef), gtmp, sv)

    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("mstring.Length: global not found")
    }

    if (data.isEmpty()) return 0 // if (strlen(p1)==0) return 0

    var i = 1
    val needle = DafCRuntime.cstring(gtmp) // strlen-terminated read
    var from = 0
    val p1 = data
    while (true) {
        val idx = p1.indexOf(needle, from) // strstr(p1, gtmp)
        if (idx < 0) return i              // one piece / no more

        i++
        from = idx + needle.length        // p1 = p1 + strlen(gtmp)
    }
}

// name-carrier helper (row 33 family): (unsigned char *) needle.ref
// carriage for the Mglobal call, NUL-padded.
private fun needleCarriage(src: String): CharArray {
    val dst = CharArray(STR_MAX_DAF)
    var q = 0
    while (q < src.length) { dst[q] = src[q]; q++ }
    return dst
}
