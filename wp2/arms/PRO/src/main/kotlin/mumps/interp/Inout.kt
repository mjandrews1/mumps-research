package mumps.interp

/**
 * PRO-arm translation of interp-parse.cpp::inout (roster row 286,
 * batch 10; units.csv: loc 38, comment_ratio 0.026, pointer_marks
 * 19, control_marks 13, score 58.6, rank 249). Translated
 * independently from the pristine vendor source (mumps-14-may-2026
 * .tgz, sha256 621deb25...ffdbe), reference source: the complete
 * void inout(unsigned char text[], short cmnd, short col,
 * struct MSV *svPtr) body.
 *
 * The runtime device-control helper behind the compiler-emitted
 * C glue: new page (cmnd 1), skip line (2), tab (3), text print
 * (4), each against the active io channel's Writer stream.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param : fputc/fprintf/putchar map to
 *    System.out for the dev==5 console legs and to the
 *    out_file[dev] Writer legs; fflush is flush() on the console
 *    leg (the device legs close through the Writer buffer).
 *  - h007 char_buffer     : the text[] parameter rides CharArray
 *    with an explicit offset (textOff, the Interpret.kt call sites
 *    pass pd1 at sdlim); strlen is strlenC(text, textOff).
 *  - h011 control_flow    : the cmnd switch renders as a Kotlin
 *    when, arms kept in the vendor order.
 *  - h012 unsigned_byte_domain: the '\014' form-feed and ' '
 *    bytes ride Char code points; a NULL out_file[dev] slot
 *    (vendor: FILE* NULL deref, UB) is quantized to a skip.
 *  - h014 out_of_scope_call: strlen is strlenC (CstringOps.kt).
 *  - h017 naming          : inout keeps the vendor name; resolves
 *    the InterpStubs u155 stub (removed, cleanLocks precedent)
 *    whose two Interpret.kt call sites bind this seat unchanged.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - hor/ver vendor shorts ride the Msv IntArray carriers; the
 *    col<1 early return and the hor==col zero-work pulse are kept
 *    as written.
 *  - case 3's dev==5 leg prints to stdout while every other leg
 *    prints through out_file[dev] - including the same-leg newline
 *    decision - kept exactly.
 *  - case 4's fprintf emits the NUL-terminated text and then
 *    bumps hor by its strlen - the (const char*) cast semantics
 *    ride strlenC.
 */
fun inout(text: CharArray, textOff: Int, cmnd: Int, col: Int, svPtr: Msv) {
    val dev = svPtr.io

    when (cmnd) {
        1 -> { /* new page */
            svPtr.out_file[dev]?.let { it.write("\u000c") } // fputc('\014')
            svPtr.hor[dev] = 1
            svPtr.ver[dev] = 0
            return
        }

        2 -> { /* skip line */
            svPtr.out_file[dev]?.let { it.write("\n") }
            svPtr.hor[dev] = 0
            svPtr.ver[dev]++
            return
        }

        3 -> { /* tab */
            if (col == svPtr.hor[dev]) return

            if (col < 1) return

            if (dev == 5) {
                if (svPtr.hor[5] > col) {
                    System.out.print("\n") // putchar('\n')
                    svPtr.hor[5] = 0
                }
                for (i in svPtr.hor[dev] until col) System.out.print(" ")
            } else {
                val w = svPtr.out_file[dev]
                if (w != null && svPtr.hor[dev] > col) w.write("\n")
                for (j in svPtr.hor[dev] until col) {
                    w?.write(" ")
                }
            }

            svPtr.hor[dev] = col
            return
        }

        4 -> { /* text print */
            val w = svPtr.out_file[dev]
            if (w != null) {
                w.write(cstringAt(text, textOff))
            }

            if (dev == 5) System.out.flush() // fflush(stdout)

            svPtr.hor[dev] = svPtr.hor[dev] + strlenC(text, textOff)
            return
        }
    }
}
