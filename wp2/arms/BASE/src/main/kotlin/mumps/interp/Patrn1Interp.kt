package mumps.interp

/**
 * Kotlin translation of interp-parse.cpp::patrn1 (Study Subset unit 30,
 * Batch 1, arm BASE; translated 2026-11-15).
 *
 * C: static void patrn1(short *jpx, short *ernbr, struct MSV *svPtr) —
 * "pattern argument [right side] set up": copy the pattern tail from the
 * parse buffer svPtr->xd at jpx into svPtr->bd, decoding doubled quotes
 * ('""') inside the pattern literal into the 210 sentinel byte, until a
 * terminator outside the char set " ,&:!''><=[)+-*/#_" is met.
 *
 * NOTE: this is the INTERPRETER patrn1 (interp-parse.cpp) — a different
 * function from the compiler's Patrn1 (compiler-sys1.c, unit 26); the
 * mumps.interp and mumps.compiler packages keep them separate.
 *
 * Pointer out-params jpx/ernbr stay short[1] holders (heuristic 66).
 * The C goto mesh (a1285/a1283/a1281) becomes labeled loops
 * (heuristic 73). The C reads xd[jpx] past its NUL as 0 — modeled by
 * xdAt(), since Kotlin Strings carry no NUL terminator.
 */

/** C: const static unsigned char cod210[2] = { 210, 0 } — the doubled-quote
 *  sentinel byte appended to bd. Built programmatically (Char(210)) rather
 *  than as a literal, to keep raw >0x7F bytes out of the tool payload
 *  (heuristic 80; same class as the unit-26 NUL/Ð incidents). */
private val COD210: Char = Char(210)

/** C: char *xindex set — pattern literal terminators. */
private const val PATRN1_TERMINATORS = " ,&:!''><=[)+-*/#_"

/** C: svPtr->xd[i] with the C NUL-terminated-buffer convention: reads at
 *  or past the end yield 0. Char(0) is the NUL analog (never in a Kotlin
 *  String, so it uniquely encodes "end"). */
private inline fun xdAt(xd: String, i: Int): Char =
    if (i < 0 || i >= xd.length) Char(0) else xd[i]

fun patrn1(jpx: ShortArray, ernbr: ShortArray, svPtr: MsvState) {
    val xd = svPtr.xd
    val bd = svPtr.bd

    outer@ while (true) { // a1285: start of pattern element

        if (xdAt(xd, jpx[0].toInt()) == '"') {
            bd.append('"') // C: strcat(&svPtr->bd[0], "\"")

            while (true) { // a1283: inside the quoted literal
                jpx[0] = (jpx[0] + 1).toShort()

                val c = xdAt(xd, jpx[0].toInt())
                if (c == Char(0)) { // C: if (svPtr->xd[*jpx] == 0)
                    ernbr[0] = 2
                    return
                }

                if (c == '"') {
                    if (xdAt(xd, jpx[0].toInt() + 1) != '"')
                        continue@outer // C: goto a1281 (literal ended)

                    bd.append(COD210) // C: strcat(&svPtr->bd[0], cod210)
                    jpx[0] = (jpx[0] + 1).toShort()
                } else {
                    bd.append(c) // C: tmp1[0]=xd[*jpx]; strcat(bd, tmp1)
                }
                // C: goto a1283 — loop continues
            }
        }

        // a1281: at/after the literal — copy until a terminator
        val c = xdAt(xd, jpx[0].toInt())
        if (c != Char(0) && xindex(PATRN1_TERMINATORS, c.toString(), 1.toShort()) == 0) {
            bd.append(c)
            jpx[0] = (jpx[0] + 1).toShort()
            continue@outer // C: goto a1285
        }

        svPtr.xpx = jpx[0].toInt() - 1 // C: svPtr->xpx = *jpx - 1
        ernbr[0] = 0
        return
    }
}
