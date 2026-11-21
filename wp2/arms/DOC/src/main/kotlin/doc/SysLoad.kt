package doc

// Unit 279 (B10 tranche 1): interp-sym.cpp::SysLoad (batch 10,
// simple stratum; units.csv row 300: loc 18, cr 0.176, ptr 1, ctl
// 2, score 8.0, rank 62; the unit-142 SysDump TWIN, the read
// side of the pid-file symbol-table pair; physically verified
// this session at vendor interp-sym.cpp:949, physLOC 18). The
// sym-table load: Mltoa the pid (the H-014 stub seat with the
// long-pid Int narrowing documented per H-010, OS pids are Int
// range in practice - the sysDump precedent), fopen the
// pid-named file H-001 (java.io.BufferedReader over FileReader;
// the vendor fopen returns NULL for a missing file, which is
// NOT an error here - the clean return 0 arm, no ErrorMessage
// call: the twin's open-NULL UB quantization does NOT apply).
// The fgets PAIR walk reads name/value lines; readLine() strips
// the newline the vendor's [strlen-1]=0 trims, so the two strip
// arms are no-ops on the readLine domain (fold noted; the
// StopInit reverse-fold precedent). The SECOND fgets is
// unchecked by the vendor - a NULL return (odd final line)
// dereferences the stale buffer in C; quantized to the fresh
// empty string per the deterministic-UB convention.
// sym_(0, name, value, svPtr) rides the H-014 seat with the
// batch-10 tranche-3 owner tag (interp-sym.cpp::sym_, roster
// row 280); fclose is the GC no-op. The vendor returns 1 with
// the file read (and 0 without); naming per H-017 (SysLoad ->
// sysLoad, the sysDump precedent).

fun sysLoad(pid: Long, svPtr: Msv): Int {
    val inbuf = CharArray(2048)
    Stubs.Mltoa(pid.toInt(), inbuf)

    val dumpFile = java.io.File(CstringOps.cstring(inbuf))

    if (!dumpFile.exists()) return 0 // fopen NULL: clean miss, no error

    java.io.BufferedReader(java.io.FileReader(dumpFile)).use { r ->
        while (true) {
            val line = r.readLine() ?: break // fgets(inbuf) == NULL

            // fgets(inbuf2) - vendor does not check the second read;
            // NULL (odd final line) means C stale-buffer UB, fresh
            // empty string quantization here
            val line2 = r.readLine() ?: ""

            // the [strlen-1]=0 newline strips are no-ops (readLine
            // returns without the newline - header note)
            Stubs.sym_(0, line.toCharArray().copyOf(2048), line2.toCharArray().copyOf(2048), svPtr)
        }
    }

    return 1
}
