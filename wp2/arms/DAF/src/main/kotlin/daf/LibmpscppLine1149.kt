package daf

// Unit 290 (DAF B10 T3, residue): libmpscpp.cpp::line1149 (units.csv
// row 290: 36 physical LOC): Hash::operator const char *() - the
// hash-table PROBE body of the file-backed Hash class: read from
// bucket i = lhash(ref) % size, walk the file chain, and on match
// read the value: i = lhash(ref.c_str())%size; FSEEK(hashkey,i,
// SEEK_SET); then the fgetc walk. The operators use the FILE*
// hashkey opened by the Hash ctor (libmpscpp.cpp:959 - OUTSIDE this
// row's chunk; the C++ class seats ref/size members there).
// SEAT RESOLUTION: lhashD/HashD already seated in
// LibmpscppMisc5.kt (the accumulation kernels): LEDGER, reused;
// the DafHashFile carrier is RAISED here for the ctor's open (the
// operator is the first corpus render that consumes hashkey).
// Consults (2026-11-25T15:07:00Z, nominal): rows 6/22 hit-reuse
// (static string r/static char p[STR_MAX] ARE file-scope in
// vendor: p buffers per call, r UNUSED in the body - the operator
// returns p; r folds away, ledger), rows 18/37 hit-reuse
// (FSEEK/fgetc windows: the DafFileHandles service is the DAF
// stream carrier for the row-285 inout renders, but hashkey is a
// C++ class FILE* - a dedicated RandomAccessFile carrier is the
// clean fit; the eof/rewind loop is byte-faithful),
// row 54 hit-adapt (off_t i folds to Long; the EOF int c folds to
// Int with -1 as EOF), row 56 hit-adapt (strcmp == 0 folds to
// String equality), row 33 hit-reuse (char p[STR_MAX] = CharArray).
// VENDOR QUIRKS kept: (1) the marker skips: if (c==1) continue
// and if (c==2) continue DROP the byte WITHOUT clearing j - a
// slot's record header bytes 1/2 contaminate p until the first
// '\0' (the commented-out early return // if (c==1) return NULL
// is a DEAD VENDOR COMMENT, kept); (2)EOF-wrap: first EOF rewinds
// to i=0 with loopflg=1, second EOF returns NULL; (3) the
// value-read walk has NO loopflg: EOF there is a plain NULL; (4)
// p[j++]=c writes c BEFORE the NUL test (the NUL is buffered -
// p keeps the trailing zero byte); (5) markers 1/2 mean
// erase-mark/continue (file-tombstone arm), NOT found checks -
// the probe can skip erased slots and still match a later
// duplicate write, vendor-true.
// Dafny: EXEMPT (file-I/O probe walk; journal row).

// The Hash-class file carrier: the ctor (libmpscpp.cpp:959,
// fopen(key, code) - OUTSIDE the row-290 chunk) owns the open;
// the operator reads through this seat. RAISED as the
// named-owner carrier for the ctor.
object DafHashFile {
    var hashkey: java.io.RandomAccessFile? = null // FILE * hashkey
}

// Hash::operator const char *() - the probe; ref/size are the C++
// class members (carried as parameters of the operator fold - the
// DafHashFile note owns the class fields).
fun hashOpConstCharD(ref: String, size: Long): String? {
    val fk = DafHashFile.hashkey ?: return null
    var i = (lhashD(ref) % size.toULong()).toLong() // i = lhash(ref)%size
    fk.seek(i) // FSEEK(hashkey, i, SEEK_SET)
    var loopflg = 0 // int loopflg = 0
    val p = StringBuilder()

    // while (1) { j=0; while (1) { c = fgetc ... } match test }
    scan@ while (true) {
        var j = 0
        p.setLength(0) // j = 0: p re-walks from [0] (the C static buffer is overwritten from the front; the reset folds here)

        walk@ while (true) {
            val c = fk.read() // c = fgetc(hashkey)

            if (c == -1 && loopflg != 0) return null // EOF && loop
            if (c == -1) { // first EOF: rewind, loopflg=1
                loopflg = 1
                i = 0
                fk.seek(0) // FSEEK(hashkey, 0, SEEK_SET)
                continue@walk
            }

            // if (c==1) continue; - the erase-marker skip (quirk 1)
            if (c == 1) continue@walk
            // if (c==2) continue; - the tombstone skip
            if (c == 2) continue@walk

            p.append(c.toChar()); j++ // p[j++] = c

            if (c == 0) break@walk // if (c=='\0') break
        }

        if (p.toString() == ref) break@scan // strcmp(p,ref)==0 found
    }

    // get value: while (1) { c = fgetc; if EOF return NULL; p[j++]=c;
    // if (c=='\0') break; } - the NO-loopflg QUARK arm
    val v = StringBuilder()
    while (true) {
        val c = fk.read()
        if (c == -1) return null
        v.append(c.toChar()) // p[j++] = c (NUL buffered)
        if (c == 0) break
    }

    return v.toString() // return p (c-string ends at the NUL)
}
