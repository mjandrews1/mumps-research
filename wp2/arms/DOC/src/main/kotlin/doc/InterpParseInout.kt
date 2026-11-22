package doc

// UNIT 306 RENDER - batch 10 DOC tranche T3 (complex).
// interp-parse.cpp::inout - unit roster row 286, loc 38. The
// interpreter output control writer: the new page, skip line,
// tab and text print arms over the per-device hor/ver pair.
//
// ADJUDICATIONS:
// - The compiler-side twin Inout.kt (compiler-sys1.c::inout,
// unit 59) keeps its own render; same vendor name, two
// distinct bodies and signatures (col: Short int here vs the
// compiler col CharArray) - the H-015 overload-family rule
// resolves by signature (the parse_ 59/93 precedent).
// - FILE* seats ride the landed Msv carriers (out_file
// Writer array, io device index; H-001). fputc/fprintf map
// to Writer.write; the dev==5 arms are the console device
// (putchar/fflush render as System.out.print/flush - the
// house carrier).
// - out_file[dev] is nullable when the device was never
// opened; the write arms guard with a null check (the C
// dereference is UB there - the JVM null guard is the
// controlled H-012 twin).
// - 'cases fall off the switch' shape: unknown cmnd values
// return without action (vendor fall-through past the
// switch, H-013 banner).
//
// Consult row logged 2026-11-22T06:26:00Z (H-004 hit-reuse,
// the Stubs.inout seat flips to this render).

fun outwIO(dev: Int, c: Char, svPtr: Msv) {
    // the fputc(c, svPtr->out_file[dev]) idiom seat
    val w = svPtr.out_file[dev]
    if (w != null) w.write(c.code)
}

fun inout(text: CharArray, cmnd: Int, col: Int, svPtr: Msv) {
    val dev = svPtr.io

    when (cmnd) {
        1 -> { // new page
            outwIO(dev, '\u000C', svPtr)
            svPtr.hor[dev] = 1
            svPtr.ver[dev] = 0
        }

        2 -> { // skip line
            val w2 = svPtr.out_file[dev]
            if (w2 != null) w2.write("\n")
            svPtr.hor[dev] = 0
            svPtr.ver[dev] = svPtr.ver[dev] + 1
        }

        3 -> { // tab
            if (col == svPtr.hor[dev]) return
            if (col < 1) return

            if (dev == 5) {
                if (svPtr.hor[5] > col) {
                    System.out.print('\n')
                    svPtr.hor[5] = 0
                }
                var i = svPtr.hor[dev]
                while (i <= col - 1) {
                    System.out.print(' ')
                    i++
                }
            } else {
                val w3 = svPtr.out_file[dev]
                if (w3 != null) {
                    if (svPtr.hor[dev] > col) w3.write("\n")
                    var j = svPtr.hor[dev]
                    while (j <= col - 1) {
                        w3.write(' '.code)
                        j++
                    }
                }
            }
            svPtr.hor[dev] = col
        }

        4 -> { // text print
            val w4 = svPtr.out_file[dev]
            if (w4 != null) w4.write(CstringOps.cstring(text))
            if (dev == 5) System.out.flush()
            svPtr.hor[dev] = svPtr.hor[dev] +
                CstringOps.strlenC(text)
        }
    } // unknown cmnd: vendor fall-through, no action (H-013)
}
