package both

// Unit 296 (BOTH B10, core): libmpscpp.cpp::line1149 (36
// LOC). Batch_plan ordinal 296; units.csv row 290. The Hash
// probe: Hash::operator const char*() at libmpscpp.cpp:1149 -
// the hash-file lookup member of the Hash class (layout owner
// include/mumpsc/libmpscpp.h::<misc>, roster 305, rendered
// this batch as the proto/macro ledger; the member renders
// package-level per the receiver-state pattern). The probe
// seeks to lhash(ref) MOD size in the hash file, then scans
// NUL-terminated key records: bytes 1/2 are skipped (the
// vendor's deleted-entry markers), EOF wraps once to offset 0
// (loopflg; a second EOF returns NULL), a key record equal to
// ref breaks the scan, and the following record (up to its
// NUL) is the value; EOF mid-value returns NULL.
//
// BOTH consults (find_rule before render, events logged):
//   h008 static_scratch - reused and hit: the vendor's
//     static char p[STR_MAX] is a cross-call aliasing buffer;
//     the Kotlin fold returns a String COPY of the record
//     (no aliasing surface) - the static r in the vendor is
//     dead (never read); divergence journaled.
//   h014 out_of_scope_call - reused: lhash rides its unit-154
//     seat (LibmpscppMisc5.kt); the hashkey FILE* rides the
//     RandomAccessFile fold (the h020 FILE* precedent, the
//     sysLoad render this batch).
//   h017 naming - reused: the operator face renders as
//     hashOpConstChar (the operator-name fold, the vendor
//     member name kept); ref/i/j/k/m/c/loopflg keep vendor
//     names.
//   h019 class_member_carrier - reused and hit: member of the
//     Hash class (layout owner roster 305, untranslated as a
//     class); the receiver state passes explicitly (ref, the
//     table size, the hash file handle).
//   h020 pointer_handle - reused: the FILE* hashkey seats on
//     the RandomAccessFile fold; fgetc seats on read()
//     (EOF = -1), FSEEK(i,SEEK_SET) on seek(i).
//   Dafny: EXEMPT (file I/O walk; no decidable core).
//
// Vendor quirks kept (ledgered):
//   - the c==1 not-found test is COMMENTED OUT in the vendor
//     (deleted records are skipped by the continue, not
//     returned); kept verbatim with the comment.
//   - the wrap-around re-seek to 0 runs at most once
//     (loopflg); a record straddling EOF is re-read from the
//     top - kept.
//   - the key scan appends the NUL to p (j++ BEFORE the
//     break), so strcmp sees the terminator; the fold
//     includes it in the compare string.

// Hash::operator const char *() - libmpscpp.cpp:1149
internal fun hashOpConstChar(
    ref: String,
    size: Long,
    hashkey: java.io.RandomAccessFile?,
): String? {
    if (hashkey == null) return null // no open table (vendor: ctor invariant)
    val i = (lhash(ref).toLong() % size)
    hashkey.seek(i)
    var loopflg = 0

    val p = CharArray(STR_MAX)

    outer@ while (true) {
        var j = 0

        // the key record walk
        while (true) {
            val c = hashkey.read() // fgetc(hashkey)

            if (c == -1 && loopflg != 0) return null // EOF twice

            if (c == -1) { // EOF once: wrap to 0
                loopflg = 1
                hashkey.seek(0)
                continue
            }

            // if (c==1) return NULL; // not found (commented out)
            if (c == 1) continue
            if (c == 2) continue // skip

            p[j++] = c.toChar()
            if (c == 0) break
        }

        if (cStringOf(p) == ref) break@outer // found
    }

    var j = 0

    // the value record walk
    while (true) {
        val c = hashkey.read()
        if (c == -1) return null // EOF mid-value
        p[j++] = c.toChar()
        if (c == 0) break
    }

    return cStringOf(p)
}
