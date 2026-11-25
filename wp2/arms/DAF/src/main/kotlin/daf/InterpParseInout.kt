package daf

/*
 * DAF batch-10 unit 285 (roster row 285; interp-parse.cpp::inout,
 * LOC 38, ptr 6, score 44.0). Consult 2026-11-25T15:03:30Z.
 *
 * RELEASES the raise (Interpret.kt inoutInterpDaf raise deleted):
 * the real 4-arg interpreter seat (the three IOSUB call sites
 * keep the name); the WriteCode.kt inoutDaf 3-arg seat remains
 * the raised b2 compiler twin (compiler-sys1.c::inout, unit 180).
 *
 * DOMAIN FOLD: svPtr->out_file[dev] is the Int handle carrier on
 * this arm (0 closed, 1 open, DafMsv); the java.io Writer/Reader
 * registry rides the new DafFileHandles side carrier of this
 * file, wired by the Interpret FILEOPEN/SHELL/CLOSE seats (the
 * fopenWriteI/popenWriteI/fopenReadI call sites now register the
 * stream at the device index; release-noted at each edit). The
 * console device 5 folds to System.out / the fputc('\n') stdout
 * writes of vendor case 3 (putchar) - disclosed.
 *
 * DISCLOSED: fwrite to a device whose writer is absent (file
 * opened as read-only, or dev 5 console) folds to the System.out
 * console arm; verbatim vendor decrements/returns otherwise.
 *
 * H-015: ASCII; no raw dollars in comments; trailing newline.
 * Dafny: EXEMPT (IO-bound; journal row).
 */

object DafFileHandles {
    // FILE *out_file[10] / *in_file[10]: the java.io side carrier
    val outW = arrayOfNulls<java.io.Writer>(10)
    val inR = arrayOfNulls<java.io.Reader>(10)
}

fun inoutInterpDaf(text: CharArray, cmnd: Short, col: Short,
        svPtr: DafMsv): Unit {

    val dev = svPtr.io // dev = svPtr->io

    when (cmnd.toInt()) {

        1 -> { // new page

            outFputcS('\u000C', dev, svPtr) // fputc('\014', ...)
            svPtr.hor[dev] = 1
            svPtr.ver[dev] = 0
            return
        }

        2 -> { // skip line

            outFputsS("\n", dev, svPtr) // fprintf(...,"\n")
            svPtr.hor[dev] = 0
            svPtr.ver[dev]++
            return
        }

        3 -> { // tab

            if (col.toInt() == svPtr.hor[dev]) return
            if (col.toInt() < 1) return

            if (dev == 5) {
                if (svPtr.hor[5] > col.toInt()) {
                    System.out.print('\n') // putchar('\n')
                    svPtr.hor[5] = 0
                }
                var i = svPtr.hor[dev]
                while (i <= col - 1) { System.out.print(' '); i++ }
            } else {
                if (svPtr.hor[dev] > col.toInt())
                    outFputcS('\n', dev, svPtr) // fputc('\n', ...)
                var j = svPtr.hor[dev]
                while (j <= col - 1) {
                    outFputcS(' ', dev, svPtr)
                    j++
                }
            }

            svPtr.hor[dev] = col.toInt()
            return
        }

        4 -> { // text print

            outFputsS(DafCRuntime.cstring(text), dev, svPtr)
            // fprintf(out_file[dev], "%s", text)

            if (dev == 5) System.out.flush() // fflush(stdout)

            var n = 0
            while (n < text.size && text[n] != NUL_D) n++
            svPtr.hor[dev] = svPtr.hor[dev] + n // + strlen(text)
            return
        }
    }
}

// fputc(c, svPtr->out_file[dev]): the console device folds to
// System.out; a registered writer writes; an absent stream folds
// to the console arm (disclosed header divergence)
private fun outFputcS(c: Char, dev: Int, svPtr: DafMsv) {
    if (dev == 5 || DafFileHandles.outW[dev] == null) {
        System.out.print(c)
    } else {
        try {
            DafFileHandles.outW[dev]!!.append(c)
            DafFileHandles.outW[dev]!!.flush()
        } catch (e: java.io.IOException) {
            // (vendor FILE* writes have no error arm here)
        }
    }
}

// fprintf(svPtr->out_file[dev], "%s", s)
private fun outFputsS(s: String, dev: Int, svPtr: DafMsv) {
    if (dev == 5 || DafFileHandles.outW[dev] == null) {
        System.out.print(s)
    } else {
        try {
            DafFileHandles.outW[dev]!!.append(s)
            DafFileHandles.outW[dev]!!.flush()
        } catch (e: java.io.IOException) {
            // (fold; vendor writes have no error arm)
        }
    }
}
