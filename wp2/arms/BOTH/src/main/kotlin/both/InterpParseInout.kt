package both

// Unit 301 (BOTH B10, complex): interp-parse.cpp::inout (38
// LOC). Batch_plan ordinal 301; units.csv row 285. The
// output-formatting rider: the device-indexed write helper
// the interpreter's WRITE path calls. dev = svPtr->io; four
// commands ride the current device's stream: (1) new page -
// form feed, hor=1, ver=0; (2) skip line - newline, hor=0,
// ver++; (3) tab to col - no-op when col equals hor or
// col<1, a newline first when hor exceeds col, then spaces to
// col-1 (device 5 writes to stdout via putchar, the others to
// the out_file handle), hor=col; (4) text - print text, add
// its length to hor (device 5 flushes stdout).
//
// BOTH consults (find_rule before render, events logged):
//   h001 io_stream_param - reused and hit: fputc/fprintf on
//     svPtr->out_file[dev] seat on the Msv RandomAccessFile
//     handle array (the h020 fold, BothCRuntime.kt:111); the
//     dev==5 arms seat on System.out (putchar/fflush(stdout)
//     fold to print/flush - the stdout device convention).
//   h007 char_buffer - reused: text is the caller's
//     CharArray; the %s print and the strlen walk ride
//     cStringOf (the NUL-terminated read).
//   h010 numeric_promotion - reused: cmnd/col/i/j/dev stay
//     the C short domain on the Int carrier (the same-body
//     fold, the Mltoa digit-index precedent).
//   h017 naming - reused: inout keeps the vendor name;
//     cmnd/col/dev keep vendor names.
//   h014 out_of_scope_call - reused: the svPtr field family
//     (io/hor/ver/out_file) rides the Msv seats
//     (stateVector.h:49/62/61).
//   Dafny: EXEMPT (device I/O side effect; no decidable
//     core).
//
// Vendor quirks kept (ledgered):
//   - dev==5 writes through putchar to stdout, NOT the
//     out_file handle (kept: the two-arm split is the
//     observable behavior).
//   - case 3 on dev==5 does NOT check the col<1/hor==col
//     order differently, but the newline-then-space pair
//     differs from the file arm's single fputc loop; kept
//     verbatim (the space loops are hor..col-1 inclusive).
//   - an unhandled cmnd value falls through the switch with
//     no write (kept: no default arm).

// void inout(unsigned char text[], short cmnd, short col,
//            struct MSV * svPtr) - interp-parse.cpp
internal fun inout(text: CharArray, cmnd: Int, col: Int, svPtr: Msv) {
    val dev = svPtr.io

    when (cmnd) {
        1 -> { // new page
            // fputc('\014', svPtr->out_file[dev]);
            svPtr.out_file[dev]?.let { writeRafChar(it, 12.toChar()) }
            svPtr.hor[dev] = 1
            svPtr.ver[dev] = 0
            return
        }

        2 -> { // skip line
            // fprintf(svPtr->out_file[dev], "\n");
            svPtr.out_file[dev]?.let { writeRafChar(it, '\n') }
            svPtr.hor[dev] = 0
            svPtr.ver[dev]++
            return
        }

        3 -> { // tab
            if (col == svPtr.hor[dev]) return
            if (col < 1) return

            if (dev == 5) {
                if (svPtr.hor[5] > col) {
                    print('\n') // putchar('\n')
                    svPtr.hor[5] = 0
                }
                for (i in svPtr.hor[dev]..col - 1) print(' ')
            } else {
                if (svPtr.hor[dev] > col) {
                    svPtr.out_file[dev]?.let { writeRafChar(it, '\n') }
                }
                for (j in svPtr.hor[dev]..col - 1) {
                    svPtr.out_file[dev]?.let { writeRafChar(it, ' ') }
                }
            }

            svPtr.hor[dev] = col
            return
        }

        4 -> { // text print
            // fprintf(svPtr->out_file[dev], "%s", text);
            val t = cStringOf(text)
            svPtr.out_file[dev]?.let { writeRafString(it, t) }

            if (dev == 5) System.out.flush() // fflush(stdout)

            svPtr.hor[dev] = svPtr.hor[dev] + t.length
            return
        }
    }
}

// fputc(c, raf) fold: one byte in the unsigned-char domain.
private fun writeRafChar(raf: java.io.RandomAccessFile, c: Char) {
    raf.write(c.code and 0xff)
}

// fprintf(raf, "%s", s) fold: the byte-domain string write.
private fun writeRafString(raf: java.io.RandomAccessFile, s: String) {
    raf.write(s.toByteArray(Charsets.ISO_8859_1))
}
