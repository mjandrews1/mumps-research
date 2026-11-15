package mumps.interp

/**
 * PRO-arm translation of interp-sysfunc.cpp::DirectRead (pilot B1,
 * unit 24). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 174-221.
 *
 * DirectRead(in, to): reads a single byte from input unit 'in' into
 * the caller's byte cell; returns 1 on success, 0 on EOF/failure.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param  : FILE* in_file[in] -> Reader carrier on
 *    Msv. ADAPTATION (hit-only, see ARM_WORKING_NOTES): Reader
 *    carriers cannot be fstat'd, so the vendor's fileno/fstat
 *    bad-file guard and the S_ISCHR/S_ISFIFO/S_ISREG trichotomy on
 *    the io==5 (stdin) path map to: null-carrier guard, console
 *    presence for the terminal case (the stty -echo raw / echo -raw
 *    echo toggling is not portable to Kotlin and is not reproduced),
 *    and regular-file read semantics for everything else. The FIFO
 *    early-out (vendor returns 0 without consuming a byte) is not
 *    detectable on a Reader.
 *  - h002 simple_output_macro: fgetc/getchar libc calls -> Reader
 *    carrier read calls keeping the call shape.
 *  - h006 out_param_holder  : unsigned char *to -> ShortArray(1)
 *    holder (caller-owned cell, C API shape survives integration).
 *  - h012 unsigned_byte_domain: *to holds 0-255; the terminal branch
 *    stores (unsigned char)k, so an EOF (-1) truncates to 255 - the
 *    vendor does not check EOF there and neither does the port.
 *  - h017 naming           : DirectRead keeps the vendor name; the
 *    parameter 'in' is a Kotlin hard keyword and is renamed 'in_'
 *    (only occurrence of the rename rule in this unit).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The io!=5 branch tests fstat(...)==1 "// bad file"; fstat never
 *    returns 1 (POSIX: 0 on success, -1 on error), so that branch is
 *    unreachable in the vendor build too. It is preserved here as the
 *    null-carrier guard with this comment.
 *  - The struct stat buf declared at function top is only consumed by
 *    the fstat/S_IS* checks and has no Kotlin carrier (h013-style dead
 *    declaration, dropped with this comment).
 */
fun DirectRead(in_: Int, to: ShortArray, svPtr: Msv): Int {
    if (svPtr.io != 5) {

        // vendor: fstat(fileno(svPtr->in_file[in]), &buf) == 1 // bad file
        val r = svPtr.in_file[in_]
        if (r == null) {
            to[0] = 0
            return 0
        }

        val k = r.read()
        if (k == -1) { // EOF
            to[0] = 0
            return 0
        }
        to[0] = k.toShort()
        return 1
    }

    // vendor: if (fstat(fileno(svPtr->in_file[in]), &buf) == 0) { ... }
    val r = svPtr.in_file[in_]
    if (r != null) {

        if (System.console() != null) { // S_ISCHR: stdin is a terminal
            // vendor: system("stty -echo raw"); k = getchar();
            //        system("stty echo -raw");
            // echo toggling not reproducible in Kotlin (h001 adaptation)
            val k = r.read()
            to[0] = (k and 0xFF).toShort() // (unsigned char)k
            return 1
        }

        // vendor: else if (S_ISFIFO(buf.st_mode)) { *to = 0; return 0; }
        // a FIFO cannot be distinguished from a regular file through a
        // Reader carrier (h001 adaptation) - fall through to the
        // regular-file read below.

        // else: stdin is a regular file
        val k = r.read()
        if (k == -1) return 0 // EOF: *to left untouched (vendor)
        to[0] = k.toShort()
        return 1
    }

    to[0] = 0
    return 0
}
