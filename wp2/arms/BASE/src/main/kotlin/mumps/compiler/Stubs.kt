package mumps.compiler

/**
 * Stubs for units not yet translated (BASE arm, pilot). Each is replaced
 * by its real translation when that unit's turn comes in batch order.
 * Signatures reflect the C prototypes; bodies are TODO by design.
 */

/** C: int atTest(unsigned char xd[], unsigned char pd1[], short *pd1Last, short lines, int xpx) */
data class AtTestResult(val atFlg: Int, val pd1Last: Short)

fun atTest(xd: String, pd1: StringBuilder, pd1Last: Short, lines: Short, xpx: Long): AtTestResult =
    TODO("unit compiler-at-test.c not yet translated")

/** C: void atCopy(int at_flg, unsigned char tmp[]) */
fun atCopy(atFlg: Int, tmp: StringBuilder): Unit =
    TODO("atCopy not yet translated (lives in compiler-at-test.c)")

/** C: short parse_() — the expression parser (compiler-parse.c) */
fun parse_(): Short =
    TODO("unit compiler-parse.c parse_() not yet translated")

/** C: char *Extract2(unsigned char *) — called as Extract2(&pd1[sdlim]) */
fun extract2(pd1: StringBuilder, from: Int): String =
    TODO("unit compiler-parse.c::Extract2 not yet translated")

/** C: void linepad(int, char *) — count is derivable from the text length */
fun linepad(text: String, comment: String): Unit =
    TODO("linepad not yet translated")
