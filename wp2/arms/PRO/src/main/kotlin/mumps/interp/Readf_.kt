package mumps.interp

/**
 * PRO-arm translation of interp-parse.cpp::readf_ (B4 unit 102,
 * rank 238). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 1518-1593.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-parse.cpp::readf_, cpp, loc 55,
 * comment_ratio 0.018, pointer_marks 13, control_marks 16, score
 * 52.9, batch 4 - exact match to wp1/study-subset/units.csv row
 * 238.
 *
 * readf_(zd, pd1org, svPtr): reads the source file named by zd into
 * svPtr->pd1 starting at offset pd1org - folding backslash
 * continuations, mapping CR/CTRL-Z out of the stream, and stopping
 * at the first byte below 1 (EOF) with the pd1len bookkeeping.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param : the vendor's FILE* infile is opened
 *    and closed inside this unit - java.io.FileReader at the edge
 *    (fopen failure -> null carrier check; fclose -> close()).
 *  - h002 simple_output_macro: printf -> System.out.print.
 *  - h007 char_buffer     : zd and pd1 are NUL-terminated byte
 *    buffers (h012 domain); CurrentFile is the strcpy target.
 *  - h012 unsigned_byte_domain: pd1 stores bytes 0-255; getc
 *    returns -1 at EOF and the vendor's "k > 127 -> 32" and "k<1"
 *    tests are byte-domain comparisons kept verbatim.
 *  - h013 dead_store     : "l = j" (vendor 1535) is never
 *    observed - dropped with a comment.
 *  - h014 out_of_scope_call: sigint (InterpStubs stub, owner
 *    interp-sysfunc.cpp::sigint); ErrorMessage (DONE - direct
 *    call, InterpSysfuncMisc.kt carrier with the writeCstring
 *    adapter for the literal).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level readf_ - no collisions (the
 *    compiler-side Readf.kt is a different vendor unit in package
 *    mumps.compiler).
 *  - h017 naming          : readf_ keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - "while (k=getc(infile))" (vendor 1548) is an assignment-in-
 *    condition loop over the continuation scan; the body's k<1
 *    break is the only exit (EOF -1 is nonzero) - rendered as
 *    while(true) with the same break structure (h016 sibling
 *    shape, no comma operator involved).
 *  - The CR and CTRL-Z bytes drop via i-- (vendor 1569) and the
 *    k>0 guard keeps NUL bytes out of pd1 - kept verbatim.
 *  - svPtr->pd1len = i - 1 at the EOF exit (vendor 1584) is one
 *    less than the NUL index - kept verbatim.
 *  - The overflow guard fires after the loop when i reached
 *    symlen (vendor 1590) - kept as the post-loop check.
 */

private const val CRTN = 13.toChar() // vendor defines.h:197 ('\015')
private const val CTRLZ = 26.toChar() // vendor defines.h:198 ('\032')
private const val LINEFEED = 10.toChar() // vendor defines.h:196 ('\012')

fun readf_(zd: CharArray, pd1org: Short, svPtr: Msv) {
    var i: Int
    var k: Int

    val j = pd1org.toInt()
    writeCstring(svPtr.currentFile, cstringAt(zd)) // strcpy(CurrentFile, zd)
    val infile: java.io.FileReader? = try {
        java.io.FileReader(cstringAt(zd))
    } catch (e: java.io.FileNotFoundException) {
        null
    }

    if (infile == null) {
        zd[0] = NUL
        return
    }

    // h013 dead_store: the vendor's "l = j" (line 1535) is never
    // observed afterwards - dropped with this comment, never fixed
    // by initializing.

    i = j
    while (i < svPtr.symlen) {
        k = infile.read()

        if (k > 127) k = 32

        if (k == '\\'.code) { // backslash at the end of line is continuation
            k = infile.read()

            if (k < 1) {
                System.out.print("\n*** Input error in readf_()\n")
                sigint(100)
            }

            if (k == '\n'.code) {
                while (true) {
                    k = infile.read()
                    if (k == ' '.code || k == '\t'.code) continue
                    if (k == '\r'.code) continue
                    if (k < 1) break
                    if (k > 0) svPtr.pd1[i] = k.toChar()
                    break
                }
            } else {
                svPtr.pd1[i] = '\\'
                i++ // vendor: pd1[i++]='\\'
                svPtr.pd1[i] = k.toChar()
                i++ // vendor: continue -> the for-loop's i++
                continue
            }
        }

        if (k == CRTN.code || k == CTRLZ.code) i--

        else {
            if (k > 0) svPtr.pd1[i] = k.toChar()
        }

        if (k == LINEFEED.code) {
            svPtr.pd1[i] = NUL
            i++ // vendor: continue -> the for-loop's i++
            continue
        }

        if (k < 1) {
            infile.close()
            svPtr.pd1[i] = NUL
            svPtr.pd1[i + 1] = NUL
            svPtr.pd1len = (i - 1).toLong()
            return
        }

        i++
    }

    if (i >= svPtr.symlen) {
        val msg = CharArray(80)
        writeCstring(msg, "Interpreter cache overflow\n*** Increase ibuf in configure")
        ErrorMessage(msg, svPtr.LineNumber)
    }
}
