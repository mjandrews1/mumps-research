package both

// Unit 180 (BOTH B6 T3, complex): compiler-read.c::read_code
// (96 LOC). Batch_plan ordinal 180: complex, LOC 96, rank 280.
// The C-side Read command code generator of the MUMPS Compiler:
// the emitted input-file NULL guard, the carriage-control and
// tab-argument folds, the direct-mode (*), the timeout (:)
// family, and the set_code dispatch pair.
//
// Vendor behavior: the two lead fprintf seat the generated
// in_file[io]==NULL ErrorMessage guard exactly. The loop resets
// dirflg each pass (unsigned char extern, compiler-globals-ext
// :170); '!'/#' seat the inout carriage-control family ('!' ->
// i=2, '#' -> i=1 - the same inverted-looking numbering as
// write_code); '?' runs parse_ then inout with the &pd1[sdlim]
// pair (i=3 tab); '"' runs parse_ then inout (i=4); '*' steps
// xpx and sets dirflg=1 (the direct family). Then the FIRST
// set_code family: retz = READ ('r', compiler-macros.h:100),
// set_code(SET) (0, :83 - the set dispatcher entry-point macro
// group) and the seven-way return ladder (0 -> set_return
// label, PARSE_ERROR 1, ARG_LIST_ERROR 2,
// NO_GLOBAL_PERMITTED 3, MEM_ERROR 4, TOO_MANY_LHS 5, ABRTX 6,
// else 99 - the vendor catch-all quirk kept). set_return: the
// timeout family at xd[xpx+1]==':' (parse_, the TIMEOUT print
// with the &pd1[sdlim] product, the fflush(stdout) emit, the
// TimeOut_getstr1 io/gtmp/dirflg emit with the %d literal
// fold, the dollar-Test fold line, strcpy &pd1[sdlim] "gtmp",
// xpx--, then the store jump); else the direct (DirectRead +
// sprintf atmp gtmp[0], strcpy atmp) or plain (getstr1 into
// gtmp + dollar-Test fold, strcpy gtmp) emission. store: retz
// = READ again; set_code(SET_STORE) (1, :84) and the same seven
// -way ladder minus the 99 (falls straight into cont_read;
// vendTok: nonzero falls past to cont_read - the missing
// return is the vendor fall-through, byte-kept). cont_read:
// xpx++, dirflg = 0, the comma skip, and the NUL/space/tab
// close with the POST_flg fold + return 0.
//
// Quirks kept (ledgered):
//   - the return 99 catch-all after the FIRST set_code ladder
//     (unreachable beyond the six named codes; kept exact).
//   - the second ladder has no 99 and no final return: the
//     nonzero fall-through carries into cont_read (kept as the
//     straight-line fall).
//   - the inout "" pairs: the '!'/#' and '"' arms pass "" as the
//     col argument (array-offset fold: the empty-string seat).
//   - xpx-- before the store jump (the timeout family re-reads
//     the ':' walk position).
//
// BOTH consults (find_rule before render, events logged):
//   h001 io_stream_param - reused: fprintf(Out,..) through the
//     CompilerOutState.out Writer singleton; the emitted reader
//     shapes reference stdin/fflush as generated-source text.
//   h009 format_string - reused: every emitted line exact (the
//     %s/%d argument seats: the timeout seconds product and the
//     dirflg literal at their fprintf positions; the %d is the
//     emitted sprintf((char*)atmp,..) line's doubled %%d).
//   h011 goto - hit: set_return/store/cont_read fold into the
//     loop structure (the timeout family jumps the direct/plain
//     emission into the store tail; the ladder falls through).
//   h013 dead_store - reused: the commented linepad stdin line
//     stays a ledger note.
//   h014 out_of_scope_call - reused: inout stub (roster 36 b2),
//     parse_ (owner compiler-parse.c::parse_, roster 277 - RELEASED at the unit-277 render, CompilerParseMain.kt; the old pin said roster 248
//     b9 - the no-arg overload), set_code stub (owner
//     compiler-set.c::set_code, roster 97 b4).
//   h015 wrap - hit: this file.
//   h017 naming - hit: read_code vendor name kept; SET/READ/
//     SET_STORE/retZ families seat through the macro-value
//     consts below (the READ 'r' / SET 0 :83 / SET_STORE 1 :84
//     pins; the :91 SET 's' opcode of the entry-point group is
//     a different macro family - the KDoc notes the two SETs).
//   h019 class_member_carrier - reused: the CompilerGlobalsExt
//     singleton seats (dirflg the unsigned-char :170 seat,
//     retz :169, t2G, xpxG, sdlim, POST_flg, ierr, i).
//   h020 pointer_handle - reused: the &pd1[sdlim] strcpy/
//     inout/inout("") pairs seat through the array+offset
//     folds; strcpy into the slice writes through the writeAt
//     offset seat.

internal const val ARG_LIST_ERROR = 2 // compiler-macros.h:70
internal const val NO_GLOBAL_PERMITTED = 3 // compiler-macros.h:71
internal const val MEM_ERROR = 4 // compiler-macros.h:72
internal const val TOO_MANY_LHS = 5 // compiler-macros.h:73

internal const val CSET = 0 // compiler-macros.h:83 (set code path)
internal const val CSET_STORE = 1 // compiler-macros.h:84
internal const val CREAD = 'r'.code // compiler-macros.h:100

internal fun read_code(): Int {
    val g = CompilerGlobalsExt

    // fprintf(Out, the in_file NULL guard pair)
    outpRC("\tif (svPtr->in_file[svPtr->io] == NULL) \n")
    outpRC("\t\tErrorMessage(\"Read from input file\", " +
        "svPtr->LineNumber); \n")

    while (true) {
        var gotoContRead = false

        g.dirflg = 0.toChar()

        val c = g.xdG[g.xpxG.toInt()]

        if (c == '!' || c == '#') { // carriage control
            val i: Int
            if (c != '#') {
                i = 2
            } else {
                i = 1
            }
            inout(g.pd1, g.sdlim.toInt(), i, empty, 0)
            gotoContRead = true
        } else if (c == '?') { // tab format
            g.t2G = 1
            g.xpxG++
            g.ierr = parse_().toShort()
            if (g.ierr.toInt() != 0) return PARSE_ERROR
            val i = 3
            inout(g.pd1, g.sdlim.toInt(), i, g.pd1, g.sdlim.toInt())
            gotoContRead = true
        } else if (c == '"') { // text to be printed first
            g.t2G = 1
            g.ierr = parse_().toShort()
            if (g.ierr.toInt() != 0) return PARSE_ERROR
            val i = 4
            inout(g.pd1, g.sdlim.toInt(), i, empty, 0)
            gotoContRead = true
        }

        if (!gotoContRead) {
            if (c == '*') { // direct mode
                g.xpxG++
                g.dirflg = 1.toChar()
            }

            g.retz = CREAD.toShort() // retz = READ

            var returnCode = set_code(CSET) // process item
            var gotoStore = false
            when (returnCode) {
                0 -> { /* fall to set_return */ }
                PARSE_ERROR -> return PARSE_ERROR
                ARG_LIST_ERROR -> return ARG_LIST_ERROR
                NO_GLOBAL_PERMITTED -> return NO_GLOBAL_PERMITTED
                MEM_ERROR -> return MEM_ERROR
                TOO_MANY_LHS -> return TOO_MANY_LHS
                ABRTX -> return ABRTX
                else -> return 99
            }

            // set_return:
            if (g.xdG[(g.xpxG + 1).toInt()] == ':') { // timeout
                g.t2G = 1
                g.xpxG += 2
                g.ierr = parse_().toShort()
                if (g.ierr.toInt() != 0) return PARSE_ERROR
                outpRC(" // TIMEOUT REQUESTED FOR " +
                    pd1strAt(g.pd1, g.sdlim.toInt()) + " SECONDS\n")
                // linepad stdin line - commented out in the
                // vendor; ledger note only.
                outpRC("\tif (svPtr->io==5) fflush(stdout);\n")
                outpRC(
                    "\tsvPtr->tpx = TimeOut_getstr1(svPtr->io, " +
                        "gtmp, (char *) " +
                        pd1strAt(g.pd1, g.sdlim.toInt()) + ", " +
                        g.dirflg.code +
                        "); // Read string into gtmp\n",
                )
                outpRC(
                    "\tif (svPtr->tpx >= 0) svPtr->tpx = 1; " +
                        "else svPtr->tpx=0; // Set the-dollar-Test\n",
                )
                writeAtRC(g.pd1, g.sdlim.toInt(), "gtmp")
                g.xpxG--
                gotoStore = true
            }

            if (!gotoStore) {
                if (g.dirflg.code == 1) {
                    outpRC("\tsvPtr->tpx=DirectRead(svPtr->io," +
                        "gtmp); // Read Direct\n")
                    outpRC("\tsprintf((char *)atmp,\"%%d\"," +
                        "(char *) gtmp[0]);\n")
                    writeAtRC(g.pd1, g.sdlim.toInt(), "atmp")
                } else {
                    outpRC(
                        "\tsvPtr->tpx = getstr1(" +
                            "svPtr->in_file[svPtr->io], &gtmp);" +
                            " // Read string into gtmp\n",
                    )
                    outpRC(
                        "\tif (svPtr->tpx >= 0) svPtr->tpx = 1; " +
                            "else svPtr->tpx = 0; // Set the-dollar-Test\n",
                    )
                    writeAtRC(g.pd1, g.sdlim.toInt(), "gtmp")
                }
            }

            // store:
            g.retz = CREAD.toShort() // retz = READ

            returnCode = set_code(CSET_STORE)
            when (returnCode) {
                0 -> { /* fall to cont_read */ }
                PARSE_ERROR -> return PARSE_ERROR
                ARG_LIST_ERROR -> return ARG_LIST_ERROR
                NO_GLOBAL_PERMITTED -> return NO_GLOBAL_PERMITTED
                MEM_ERROR -> return MEM_ERROR
                TOO_MANY_LHS -> return TOO_MANY_LHS
                ABRTX -> return ABRTX
                // no 99; the nonzero fall-through carries into
                // cont_read (vendor fall; kept).
                else -> { /* fall to cont_read */ }
            }
        }

        // cont_read:
        g.xpxG++
        g.dirflg = 0.toChar()
        if (g.xdG[g.xpxG.toInt()] == ',') g.xpxG++

        val c2 = g.xdG[g.xpxG.toInt()]
        if (c2 == '\u0000' || c2 == ' ' || c2 == '\t') {
            if (g.POST_flg.toInt() != 0) {
                outpRC("\t} /* post conditional cont_read */\n")
                g.POST_flg = 0
            }
            return 0 // goto next_cmnd;
        }
    }
}

// The empty-string inout col seat (the "" argument arms).
private val empty = CharArray(1)

// The fprintf(Out, ...) fold of this unit (the Writer singleton
// + the char-count return).
private fun outpRC(s: String): Int {
    try {
        CompilerOutState.out.write(s)
    } catch (e: java.io.IOException) {
        throw RuntimeException(e)
    }
    return s.length
}

// strcpy(&pd1[sdlim], c) - the offset write seat.
private fun writeAtRC(pd1: CharArray, off: Int, s: String) {
    var p = off
    for (j in s.indices) {
        pd1[p++] = s[j]
    }
    pd1[p] = '\u0000'
}

// The &pd1[sdlim] string seat.
private fun pd1strAt(p: CharArray, off: Int): String {
    val sb = StringBuilder()
    var q = off
    while (p[q] != '\u0000') {
        sb.append(p[q])
        q++
    }
    return sb.toString()
}

// int set_code(int) - compiler-set.c (the set dispatcher;
// prototype at compiler-macros.h:30). Owner:
// compiler-set.c::set_code, roster unit 97 (batch 4, pending).
// TODO-stub (h014).
internal fun set_code(op: Int): Int {
    TODO("stub: owner compiler-set.c::set_code (roster 121, batch 4 - the old pin said roster 97, C-015)")
}
