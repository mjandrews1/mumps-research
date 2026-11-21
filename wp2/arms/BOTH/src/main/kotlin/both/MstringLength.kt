package both

// Unit 129 (BOTH B5 T1, simple): libmpscpp.cpp::mstring::Length (16
// LOC). Batch_plan ordinal 129: simple, score 7.8.
//
// Vendor behavior: retrieve the needle global (fail -> GlobalException
// Code=GLOBAL_NOT_FOUND and throw MumpsGlobalException), then count
// the occurrences of the needle value inside this mstring's data:
// 1 + non-overlapping leftmost matches; empty data -> 0.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: gtmp is the Mglobal out-buffer role
//     (value comes back as a String through the stubbed retrieve, so
//     no local buffer is carried); data is scan-only -> String scan
//     with indexOf per the classify-by-role action.
//   h014 out_of_scope_call - reused: Mglobal/exception seats name
//     their owners (roster 167 / 305); the mstring-class layout owner
//     is roster 149 (not yet translated).
//   h017 naming - reused: member Length -> length-keeping render name
//     mstringLength, vendor names kept.
//   MISS class_member_carrier - see BothStubs.kt ledger.
//   VENDOR QUIRK (ledgered, kept): if the needle value is the empty
//     string, the vendor strstr never returns NULL and never advances
//     p1, so the C function does not terminate; the Kotlin render has
//     the same non-termination (indexOf of the empty needle at the
//     current cursor returns the cursor and the cursor never moves),
//     byte-faithful to the vendor.
//   Dafny: the captured scan is specified in spec/length_count.dfy
//     (pure decidable core); this Kotlin unit is the arm render of it.

internal fun mstringLength(data: String, needle: GlobalRef): Int {
    val gtmp = mglobalRetrieve(needle.ref)

    if (data.isEmpty()) return 0 // C: if (strlen(p1)==0) return 0

    var i = 1
    var cursor = 0 // C: char *p1 = data.c_str() then strstr walks
    while (true) {
        // C: if ((p1=strstr(p1,(char *) gtmp))==NULL) return i;
        val idx = data.indexOf(gtmp, cursor)
        if (idx == -1) return i
        i++
        cursor = idx + gtmp.length // C: p1=p1+strlen(gtmp)
    }
}
