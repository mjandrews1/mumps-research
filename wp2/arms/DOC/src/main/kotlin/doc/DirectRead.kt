package doc

// Unit 24 (B1 tranche 4): interp-sysfunc.cpp::DirectRead (batch 1,
// complex stratum; units.csv row 24: loc 31). Single-byte direct
// reader over the input unit: non-terminal units (io != 5) read one
// byte from in_file[in]; stdin (io == 5) discriminates terminal
// (raw-mode read with the stty echo off), pipe (immediate EOF) and
// regular file (plain read). fstat/fileno have no Reader-carrier
// analog: the bad-file probe and the S_ISCHR/S_ISFIFO/S_ISREG
// discrimination render as the documented H-001 divergence - the
// vendor's fstat==1 success test is itself inverted (POSIX fstat
// returns -1 on error; the probe quirk is noted verbatim), terminal
// detection uses the standard console (System.console() != null),
// the FIFO arm of the vendor (fail without reading) cannot be
// observed on a Java InputStream and falls to the read path with
// that note; the stty raw-mode scouts run as OS subprocesses with
// the vendor's exact argument orders, and the getchar seat reads
// the same in_file[lun] carrier. The Stubs.DirectRead seat closes
// to the delegate. Naming per H-017.

fun directRead(in_: Int, to: CharArray): Int {
    val svPtr = Globals.svPtr

    if (svPtr.io != 5) {
        val rd = svPtr.in_file[in_]
        if (rd == null) {
            to[0] = NUL
            return 0
        }
        val k = rd.read()
        if (k == -1) {
            to[0] = NUL
            return 0
        }
        to[0] = k.toChar()
        return 1
    }

    // stdin arm (io == 5)
    if (System.console() != null) { // S_ISCHR: stdin is a terminal
        try {
            java.lang.Runtime.getRuntime()
                    .exec(arrayOf("stty", "-echo", "raw")).waitFor()
        } catch (e: java.io.IOException) {
            // stty unavailable; the raw-mode scouted read proceeds
            // unchanged
        }
        val k: Int
        val rd = svPtr.in_file[in_]
        k = rd?.read() ?: -1
        try {
            java.lang.Runtime.getRuntime()
                    .exec(arrayOf("stty", "echo", "-raw")).waitFor()
        } catch (e: java.io.IOException) {
            // ignore as the vendor ignores the system() return
        }
        to[0] = if (k == -1) NUL else k.toChar()
        // vendor returns 1 unconditionally in the S_ISCHR arm (an
        // EOF test is absent there); kept verbatim
        return 1
    }

    // S_ISFIFO would return 0 unread here; the regular-file arm
    // reads one byte (the divergence is the H-001 note above)
    val rd = svPtr.in_file[in_]
    if (rd == null) {
        to[0] = NUL
        return 0
    }
    val k = rd.read()
    if (k == -1) return 0
    to[0] = k.toChar()
    return 1
}
