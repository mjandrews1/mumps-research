package both

// Unit 178 (BOTH B6 T3, complex): compiler-write.c::write_code
// (63 LOC). Batch_plan ordinal 178: complex, LOC 63, rank 231.
// The C-side Write command code generator of the MUMPS Compiler:
// the emitted output-file NULL guard, the postconditional at
// family, and the write-argument walk (!, #, *, ?, text).
//
// Vendor behavior: the two lead fprintf(Out, ...) seat the
// generated out_file[io]==NULL guard exactly (\t and \t\t
// indents, the m1944 comment); atTest(xd, pd1, &pd1Last, lines,
// xpx) probes for the @ postconditional (the &pd1Last out-param
// folds to the CompilerGlobalsExt.pd1Last global seat - the
// stub mutates the singleton directly; journaled); at_flg == -1
// returns ABRTX (6, compiler-macros.h:74); a live at_flg builds
// the tmp[1024] ("w " prefix, atCopy escapes the quote and
// backslash pairs), runs the INTERPRET macro emission (the
// compiler-macros.h:39 five-fprintf family: the _jj = Interpret
// header, the quoted tmp argument, the svPtr->ERROR store, the
// NOERR guard, the ErrorMessage line), sets xpx = at_flg, closes
// the postconditional block when POST_flg is set and returns 0.
// The walk: t0px = 0 (the store kept; the value is not read in
// this unit - the global-exposed shape journaled); each
// argument: '!' seats i=2 (skip line), '#' seats i=1 (new page),
// '*' seats i=4 through the atoi/putchar emission pair (the '\t
// _i=atoi( CSTR %s);' text with the &pd1[sdlim] parse product
// and the '\tputchar(_i);' direct-output line; both linepad-
// annotated), '?' seats i=3 (the tab operator), anything else
// seats i=4 after the parse_() product; inout(&pd1[sdlim], i,
// &pd1[sdlim]) consumes the family (the compiler-sys1.c::inout
// roster-36 owner rides its stub); iosub1 folds (the '*' arm
// skips inout via the direct fallthrough); a comma continues the
// walk; NUL, space or tab closes (the POST_flg block close +
// return 0). The vendor returns PARSE_ERROR (1,
// compiler-macros.h:69) whenever parse_() is nonzero (the C
// parse_ of compiler-parse.c::parse_, roster 248 batch 9 - the
// stub family below; the interp-side parse_ of roster 303 is a
// different function, both keep the vendor name through
// overloads - h017).
//
// Quirks kept (ledgered):
//   - the '!'-vs-'#' i numbering is inverted-looking in the
//     vendor (i=1 for '#', i=2 for '!' - the inout case labels
//     new page = 1, skip line = 2); kept exact.
//   - tmp is built with strcpy("w ") then atCopy appends from
//     j=2 (the prefix shape kept).
//   - the two inout call arguments are the same &pd1[sdlim]
//     pointer pair - the stub signature keeps both array+offset
//     pairs.
//
// BOTH consults (find_rule before render, events logged):
//   h001 io_stream_param - reused: fprintf(Out,..) seats through
//     the CompilerOutState.out Writer singleton (B5 T2 seat).
//   h009 format_string - reused: every emitted line keeps the
//     exact vendor shapes (tabs, the %s positions at their
//     fprintf argument seats).
//   h011 goto - hit: iosub/iosub1 fold into the while walk (the
//     '*' arm bypasses inout; the loop structure carries it).
//   h013 dead_store - hit: t0px=0 kept as the global-exposed
//     store (journaled; not dropped - the global is read by
//     sibling compiler units).
//   h014 out_of_scope_call - reused: atTest stub (owner
//     compiler-at-test.c::atTest, roster 109 batch 4), atCopy
//     stub (owner compiler-mumps.c::atCopy, roster 241 batch 8),
//     inout stub (owner compiler-sys1.c::inout, roster 36 batch
//     2), linepad stub (the compiler-sys1.c:949 sys1 helper -
//     not a unit), parse_ stub (owner compiler-parse.c::parse_,
//     roster 248 batch 9 - the no-arg overload distinct from
//     the interp-side roster-303 parse_(sv)).
//   h015 wrap - hit: this file.
//   h017 naming - hit: write_code keeps its vendor name and the
//     at_flg/POST_flg/ierr/ierr1 extern names seat through the
//     CompilerGlobalsExt collision-renamed family (h019
//     journal: the G-suffixed extern seats carry the pins).
//   h019 class_member_carrier - reused: the CompilerGlobalsExt
//     singleton seats (at_flg, POST_flg, xpxG, t0pxG, t2G,
//     sdlim, pd1Last, lines, ierr, iiitmp neighbors).
//   h020 pointer_handle - reused: the &pd1[sdlim] offsets seat
//     through the array+offset pairs; the char tmp[1024] and the
//     short* pd1Last folds journaled above.

internal const val PARSE_ERROR = 1 // compiler-macros.h:69
internal const val ABRTX = 6 // compiler-macros.h:74

internal fun write_code(): Int {
    val g = CompilerGlobalsExt

    // fprintf(Out, the out_file NULL guard pair; m1944)
    outp("\tif (svPtr->out_file[svPtr->io]==NULL) \n")
    outp("\t\tErrorMessage(\"Write to input file\", " +
        "svPtr->LineNumber); // m1944 \n")

    // at_flg = atTest(xd, pd1, &pd1Last, lines, xpx)
    g.at_flg = atTest(g.xdG, g.pd1, g.lines, g.xpxG.toInt()).toShort()

    if (g.at_flg.toInt() == -1) return ABRTX // goto abrtx;

    if (g.at_flg.toInt() != 0) {
        val tmp = CharArray(1024) // char tmp[1024]

        writeC(tmp, "w ") // strcpy(tmp, "w ")
        atCopy(g.at_flg.toInt(), tmp)

        // INTERPRET - compiler-macros.h:39 (five-fprintf family;
        // the tmp argument lands at the quoted %s seat).
        outp("\t_jj = Interpret( (CCHR) \n")
        outp("\t\t\"" + pd1strOf(tmp) + "\", svPtr); \n")
        outp("\tsvPtr->ERROR = _jj; \n")
        outp("\tif (!svPtr->NOERR && _jj < 0) \n")
        outp("\t\tErrorMessage(\"Indirection error\", " +
            "svPtr->LineNumber); \n")

        g.xpxG = g.at_flg.toLong() // xpx = at_flg

        if (g.POST_flg.toInt() != 0) {
            outp("\t} /* post conditional 1920*/\n")
            g.POST_flg = 0
        }
        return 0 // goto next_cmnd;
    }

    g.t0pxG = 0 // t0px = 0 (kept; global-exposed store)

    while (true) {
        val c = g.xdG[g.xpxG.toInt()]

        if (c == '!' || c == '#') { // new line or page
            val i: Int
            if (c != '#') {
                i = 2 // '!' - skip line (inout case 2)
            } else {
                i = 1 // '#' - new page (inout case 1)
            }
            g.xpxG++
            inout(g.pd1, g.sdlim.toInt(), i, g.pd1, g.sdlim.toInt())
        } else if (c == '*') { // "*" output
            var i = 0
            g.xpxG++
            g.t2G = 1
            g.ierr = parse_().toShort()
            if (g.ierr.toInt() != 0) return PARSE_ERROR // goto parse_err;
            val atoiLine = "\t_i=atoi( CSTR " +
                pd1strAt(g.pd1, g.sdlim.toInt()) + ");"
            linepad(atoiLine.length, "Convert to integer")
            outp(atoiLine)
            outp("\tputchar(_i);")
            linepad("\tputchar(_i);".length, "Direct output")
            i = 4
            // goto iosub1 - inout bypassed (h011 fold); fall to
            // the continuation test below.
            if (g.xdG[g.xpxG.toInt()] == ',') {
                g.xpxG++
                continue
            }
            val c2 = g.xdG[g.xpxG.toInt()]
            if (c2 == '\u0000' || c2 == ' ' || c2 == '\t') {
                if (g.POST_flg.toInt() != 0) {
                    outp("\t} /* post conditional 4092*/\n")
                    g.POST_flg = 0
                }
                return 0 // goto next_cmnd;
            }
            continue
        } else {
            var i = 0
            if (c == '?') { // tab
                g.t2G = 1
                g.xpxG++
                g.ierr = parse_().toShort()
                if (g.ierr.toInt() != 0) return PARSE_ERROR
                i = 3
                inout(g.pd1, g.sdlim.toInt(), i, g.pd1, g.sdlim.toInt())
            } else {
                g.t2G = 1
                g.ierr = parse_().toShort()
                if (g.ierr.toInt() != 0) return PARSE_ERROR
                i = 4
                inout(g.pd1, g.sdlim.toInt(), i, g.pd1, g.sdlim.toInt())
            }
        }

        // iosub1 continuation test
        val c3 = g.xdG[g.xpxG.toInt()]
        if (c3 == ',') {
            g.xpxG++
            continue
        }
        if (c3 == '\u0000' || c3 == ' ' || c3 == '\t') {
            if (g.POST_flg.toInt() != 0) {
                outp("\t} /* post conditional 4092*/\n")
                g.POST_flg = 0
            }
            return 0 // goto next_cmnd;
        }
    }
}

// The fprintf(Out, ...) fold: write + the returned char count
// (the fprintf return the linepad call sites need; the Writer
// IOException quantizes to the RuntimeException pass-through of
// the ExpandOrder precedent).
private fun outp(s: String): Int {
    try {
        CompilerOutState.out.write(s)
    } catch (e: java.io.IOException) {
        throw RuntimeException(e)
    }
    return s.length
}

// The &pd1[sdlim] string seat (walks pd1 from off to NUL).
private fun pd1strAt(pd1: CharArray, off: Int): String {
    val sb = StringBuilder()
    var p = off
    while (pd1[p] != '\u0000') {
        sb.append(pd1[p])
        p++
    }
    return sb.toString()
}

// The tmp-string fold (atCopy writes through index 2 onward off
// the zeroed buffer; the NUL walk covers the same span).
private fun pd1strOf(tmp: CharArray): String {
    return pd1strAt(tmp, 0)
}

// int atTest(unsigned char xd[], unsigned char pd1[],
// short *pd1Last, short lines, int xpx) - compiler-at-test.c:33.
// Owner: compiler-at-test.c::atTest, roster unit 109 (batch 4,
// pending). The &pd1Last out-param folds to the
// CompilerGlobalsExt.pd1Last singleton seat (the only call site
// passes &pd1Last; the stub family mutates the global; h020
// journal). TODO-stub (h014).
internal fun atTest(
    xd: CharArray,
    pd1: CharArray,
    lines: Int,
    xpx: Int,
): Int {
    TODO("stub: owner compiler-at-test.c::atTest (roster 109, batch 4)")
}

// void atCopy - released: the batch-8 owner render now lives in
// AtCopy.kt (unit 240; the roster-241 pin was the grep-line
// mislabel family, corrected at the owner).

// void inout(unsigned char text[], short cmnd, unsigned char
// col[]) - compiler-sys1.c:292 (K&R style). Owner:
// compiler-sys1.c::inout, roster unit 36 (batch 2, pending). The
// two &pd1[sdlim] call arguments seat as the two array+offset
// pairs. TODO-stub (h014).
internal fun inout(
    text: CharArray,
    textOff: Int,
    cmnd: Int,
    col: CharArray,
    colOff: Int,
) {
    TODO("stub: owner compiler-sys1.c::inout (roster 36, batch 2)")
}

// void linepad(int sent, char *str) - compiler-sys1.c:949 (the
// sys1 TU helper; linepad1 :955 / linepad2 :963 siblings - not
// units). TODO-stub (h014).
internal fun linepad(sent: Int, str: String) {
    TODO("stub: vendor linepad, compiler-sys1.c:949 sys1 helper (not a corpus unit)")
}

// int parse_(void) - compiler-parse.c:203. Owner:
// compiler-parse.c::parse_, roster unit 248 (batch 9, pending).
// The no-arg overload of the interp-side parse_(sv) seat (roster
// 303 - a different vendor function). TODO-stub (h014).
internal fun parse_(): Int {
    TODO("stub: owner compiler-parse.c::parse_ (roster 248, batch 9)")
}
