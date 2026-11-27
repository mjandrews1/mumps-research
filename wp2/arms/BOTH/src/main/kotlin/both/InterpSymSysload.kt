package both

// Unit 287 (BOTH B10, simple): interp-sym.cpp::SysLoad (18
// LOC). Batch_plan ordinal 287; units.csv row 299. The sym
// table dump loader: format the pid decimal (Mltoa), open
// the pid-named file read-only, and when it exists read the
// file line-pairwise (name line then value line), strip the
// trailing newline bytes, and sym_-store each pair (op 0,
// SYMSTORE); return 1 on a loaded table, 0 when the file
// does not open.
//
// BOTH consults (find_rule before render, events logged):
//   h020 pointer_handle - reused and hit: the FILE* indump
//     seats on the arm RandomAccessFile fold (the B5
//     ZSeek/Ztell FILE* precedent, BothCRuntime.kt:110);
//     fopen != NULL becomes a non-null handle, fgets becomes
//     the readLine walk (the newline strip the vendor does by
//     hand is absent from readLine - the two strips below
//     become no-ops on the fold, kept for the record).
//   h007 char_buffer - reused: inbuf/inbuf2 are the 2048-byte
//     C buffers; the Mltoa out-param folds to the Long
//     digit string (the h009 shape) copied into the open
//     path.
//   h014 out_of_scope_call - reused: Mltoa rides its seat
//     (owner roster 295, released in THIS batch - the
//       BothCRuntime stub flips to the real body on this
//       push, the h014 delegate-when-owner-lands flip);
//     sym_ rides its seat (owner roster 307, released in
//     THIS batch the same way).
//   h017 naming - reused: SysLoad -> sysLoad (snake->camel);
//     pid/indump/inbuf/inbuf2 keep vendor names.
//   Dafny: exempt (file I/O side effect; no decidable core).
//
// Vendor quirks kept (ledgered):
//   - the vendor strips the newline from BOTH lines by
//     overwriting the byte before the NUL; readLine returns
//     the line without the terminator, so the strips are
//     structural no-ops on the fold (kept as the documented
//     -1 index guards below, which never fire).
//   - an odd trailing line (a name with no value line) makes
//     the vendor fgets NULL and sym_-store an inbuf2 that
//     still holds the PREVIOUS pair's value (stale-buffer
//     quirk); the fold stops at the null value line instead
//     (readLine null) - divergence journaled, the dump files
//     the reference produces are well-formed pairs.

// int SysLoad(long pid, struct MSV * svPtr) - interp-sym.cpp
internal fun sysLoad(pid: Long, svPtr: Msv): Int {
    // Mltoa(pid, inbuf);
    val inbuf = pid.toString()

    // indump = fopen((char *) inbuf, "r");
    val indump: java.io.RandomAccessFile? = try {
        java.io.RandomAccessFile(inbuf, "r")
    } catch (e: java.io.FileNotFoundException) {
        null
    }

    if (indump != null) {
        // while (fgets(inbuf,2048,indump) != NULL) { ... }
        while (true) {
            val name = indump.readLine() ?: break // fgets(inbuf)
            val value = indump.readLine() ?: break // fgets(inbuf2)

            // inbuf[strlen(inbuf)-1]=0; inbuf2[strlen(inbuf2)-1]=0;
            //   (structural no-ops on the readLine fold - the
            //   fold never carries the terminator)

            // sym_(0, inbuf, inbuf2, svPtr) - SYMSTORE
            sym_(0, name, value.toCharArray(), svPtr)
        }

        // fclose(indump);
        indump.close()
        return 1
    }

    return 0
}
