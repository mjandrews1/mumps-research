package mumps.compiler

import mumps.compiler.CompilerState.IF_TAG
import mumps.compiler.CompilerState.Out

/**
 * Kotlin translation of compiler-if.c::if_code (Study Subset unit 2,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * Emits C code for the Mumps IF command. Two entry paths per the C
 * original: the no-argument form (bare I with postconditional), the
 * argument-time (postconditional) form via INTERPRET, and the normal
 * parse() form — the latter looping for comma-separated arguments.
 */
fun ifCode(): Int {
    val c = CompilerState
    fun xd(i: Long): Char = c.xd[i.toInt()]

    // no argument form
    if (xd(c.xpx) == ' ' || xd(c.xpx) == '\u0000') {
        c.xpx++
        Out.write("\tif (svPtr->tpx) {\n")
        c.IF_flg++
        return 0 // C: goto next_cmnd
    }

    val at = atTest(c.xd, c.pd1, c.pd1Last, c.lines, c.xpx)
    c.pd1Last = at.pd1Last
    c.at_flg = at.atFlg

    if (at.atFlg == -1) return CompilerState.ABRTX // C: goto abrtx

    var needParse = true
    if (at.atFlg != 0) {
        // postconditional ($TEST) form
        val tmp = StringBuilder(1024)
        tmp.append("i ")
        atCopy(at.atFlg, tmp)
        c.interpret(tmp.toString())
        c.xpx = at.atFlg.toLong()

        if (c.POST_flg) {
            Out.write("\t} /* post conditional 1856*/\n")
            c.POST_flg = false
        }
        linepad("\tif (svPtr->tpx) {", "Test if expression true")
        needParse = false // C: goto if_structure
    }

    while (true) {
        if (needParse) { // C: label if_again
            c.t2 = 1
            val rc = parse_()
            c.ierr = rc
            if (rc != 0.toShort()) return CompilerState.PARSE_ERROR // C: goto parse_err

            Out.write("\tsvPtr->tpx = 0; //Default \$Test indicator\n")
            Out.write("\tif (numcomp2((UNS) " + extract2(c.pd1, c.sdlim.toInt()) + " )) { // Test if true \n")
            Out.write("\tsvPtr->tpx = 1; // \$test becomes true\n")
        }

        // C: label if_structure
        c.tpx = 1
        if (xd(c.xpx + 1) != '{') c.IF_flg++
        if (c.IF_flg > 0) c.ForBlocks[c.IF_flg] = c.ForBlocks[c.IF_flg - 1]
        c.Blocks[c.IF_flg] = IF_TAG

        if (xd(c.xpx) == ',') { // next IF argument
            c.xpx++
            needParse = true // C: goto if_again
            continue
        }
        return 0 // C: goto next_cmnd
    }
}
