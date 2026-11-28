package daf

// R1 stub-completion (2026-11-27, C-020 erratum session, tranche 4):
// interp-parse.cpp::getstr2 (23 LOC, roster row 47, core): the
// interpreter READ line reader: the svPtr->in_file[lun] fgetc
// loop with the STR_MAX line cap, the LINEFEED cut, the >127 and
// <32 blanking, the CR/LF skip and the trailing area[STR_MAX]
// restricted-length NUL. A DISTINCT-TWIN seat of the
// compiler-sys1.c::getstr1 render (CompilerGetstr1.kt, unit 234):
// same C symbol family, different vendor file and parameter
// domain (this seat is the svPtr in_file table form; the 234
// seat is the compiler popn/opnfile table form; per the
// unit-189 substr twin precedent).
// Consults (2026-11-27T00:00:00Z, nominal): BASE-log row 2
// family hit-reuse (FILE *in_file[] rides the DafFileHandles.inR
// carrier of InterpParseInout.kt - the established in_file fold
// of the DirectRead/Input seats; fgetc becomes Reader.read with
// the C EOF -1 contract); row 54 hit-adapt (the lun==5 console
// bind ASSIGNS the seat once - svPtr->in_file[5] = stdin renders
// as the guarded inR[5] assignment, a stored BufferedReader:
// a per-call wrapper would swallow the stream into its buffer
// and EOF the reread loop, the Input.kt smoke-found fold);
// rows 81/56 hit-adapt (the unsigned char area[] out-param
// keeps the CharArray carrier of the Interpret.kt call site;
// the sdlim copy-back stays with the caller).
// H-quantization: the caller scratch buffer is allocated
// STR_MAX+1 wide (the vendor writes area[STR_MAX] - one past
// the cap index - and the C scratch had the room; a bare
// STR_MAX_DAF array would be OOB on the JVM, disclosed).
// Vendor quirks kept: (1) the post-LINEFEED '\n' test is dead
// (LINEFEED_D caught it first) - the '\n' arm of the skip
// cascade kept verbatim per the dead-code discipline (the 234
// sibling renders the same line); (2) the '\t' self-assignment
// no-op kept verbatim; (3) svPtr stays a dead parameter (the
// vendor body reads it only through the in_file table, which
// folds onto DafFileHandles).
// Dafny: EXEMPT (IO-bound; journal row).

fun getstr2Daf(lun: Short, area: CharArray, svPtr: DafMsv): Int {

    var i = 0
    var chr: Int // short int chr (widened to the Int read domain)

    if (lun == 5.toShort()) { // if (lun == 5) svPtr->in_file[lun] = stdin
        if (DafFileHandles.inR[5] == null) {
            DafFileHandles.inR[5] =
                java.io.BufferedReader(java.io.InputStreamReader(System.`in`))
        }
    }

    val rdr = DafFileHandles.inR[lun.toInt()] // fgetc(svPtr->in_file[lun])
        ?: throw IllegalArgumentException("getstr2: null stream at lun " + lun)

    while (i < STR_MAX_DAF) { // for (i = 0; i < STR_MAX; i++)

        chr = rdr.read()

        if (chr == -1) { // if (chr == EOF)
            area[0] = NUL_D
            return -1
        }

        area[i] = chr.toChar()

        if (area[i] == LINEFEED_D) { // if (area[i] == LINEFEED)
            area[i] = NUL_D
            return i
        }

        if (area[i] > Char(127)) area[i] = ' '

        if (area[i] == '\n') i-- // dead arm (LINEFEED_D above), kept
        else if (area[i] == '\r') i--
        else if (area[i] == Char(9)) area[i] = Char(9) // vendor no-op
        else if (area[i] < Char(32)) area[i] = ' '

        i++
    }

    if (STR_MAX_DAF < area.size) area[STR_MAX_DAF] = NUL_D
    return -1 // restricted length
}
