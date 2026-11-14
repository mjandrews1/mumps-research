package mumps.interp

/**
 * Kotlin translation of interp-interp.cpp::<misc> (Study Subset unit 19,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * Region: the file-scope misc block — the InterpreterException class, the
 * sym_/patrn1 prototypes, and Interpret()'s local-declaration frame (the
 * Interpret body itself is OUTSIDE batch 1; the frame defined here is what
 * a future Interpret translation instantiates).
 *
 * PS note: sysparms.h (32000) is included by this file; memsize.h's 4096
 * is compiler-side only — no effective shadowing here.
 */

// --- C: interp-interp.cpp #defines (lines 68-80) ---
const val GOSUBMAX = 50
const val SWAP = 30
const val FORMAX = 30
const val EXECLEVS = 40
const val LBLS = 80
const val OPEN_PAREN = 206
const val STR_MAX = 32000 // C: include/mumpsc/strmax.h
const val PS = 32000      // C: include/mumpsc/sysparms.h (effective here)

/** C: class InterpreterException { ... char *message; char *what(); } */
class InterpreterException(val message: String) {
    fun what(): String = message
}

/** C: static void patrn1(short *jpx, short *ernbr, struct MSV *) —
 *  pointer-params modeled as short[1] mutable holders. */
fun patrn1(jpx: ShortArray, ernbr: ShortArray, svPtr: MsvState) {
    TODO("patrn1 (interp-interp.cpp static) not yet translated — belongs to the Interpret body")
}

/**
 * C static function locals of Interpret() — persist across calls, so they
 * are object state, not frame fields (heuristic 62).
 */
object InterpretStatics {
    /** C: static char *swp[SWAP] — swap pointers */
    val swp: Array<StringBuilder?> = arrayOfNulls(SWAP)
    /** C: static unsigned char *to, *from; */
    var to: StringBuilder? = null
    var from: StringBuilder? = null
}

/**
 * C: Interpret()'s per-call local frame (interp-interp.cpp:133-201).
 * Instantiated by the future Interpret translation (outside batch 1).
 * Fixed-size char buffers take their roles (heuristic 63): string scratch
 * -> StringBuilder, short grids -> ShortArray, chars -> Char.
 */
class InterpretFrame {
    // C: long int ilong, jlong;
    var ilong: Long = 0
    var jlong: Long = 0
    // C: short pd1hold1, pd1hold2, ibr, pd1cur, i, j, k; short l, New;
    var pd1hold1: Short = 0; var pd1hold2: Short = 0; var ibr: Short = 0
    var pd1cur: Short = 0; var i: Short = 0; var j: Short = 0; var k: Short = 0
    var l: Short = 0; var newv: Short = 0
    // C: char XTAB[2] = {'\t', 0};
    val xtab: Char = '\t'

    // gosub stack: C: short gosub[GOSUBMAX][9], gsbx, cmdx, forx, cmdxold, pd1Last;
    val gosub: Array<ShortArray> = Array(GOSUBMAX) { ShortArray(9) }
    var gsbx: Short = 0; var cmdx: Short = 0; var forx: Short = 0
    var cmdxold: Short = 0; var pd1Last: Short = 0
    var newFlag: Int = 0; var symInc: Int = 0
    val cmdstack: StringBuilder = StringBuilder(8192)

    // labels: C: unsigned char lbls[LBLS][20]; int lbloff[20], lblx, argless;
    // (lbloff is sized 20 while lbls rows are LBLS=80 — preserved verbatim,
    // a candidate C bug; flagged for the Interpret translation)
    val lbls: Array<StringBuilder> = Array(LBLS) { StringBuilder(20) }
    val lbloff = IntArray(20)
    var lblx: Int = 0; var argless: Int = 0

    // C: #ifdef NATIVE_SHARED long PulseCount — not defined in reference build.

    // for stacks: C: forv/forlv[FORMAX][256], forit/forlt[FORMAX], ftmp[STR_MAX],
    //             forin[FORMAX][STR_MAX]
    val forv: Array<StringBuilder> = Array(FORMAX) { StringBuilder(256) }
    val forlv: Array<StringBuilder> = Array(FORMAX) { StringBuilder(256) }
    val forit = CharArray(FORMAX)
    val forlt = CharArray(FORMAX)
    val ftmp: StringBuilder = StringBuilder(STR_MAX)
    val forin: Array<StringBuilder> = Array(FORMAX) { StringBuilder(STR_MAX) }

    // C: unsigned short swapextent, shold;
    var swapextent: Int = 0; var shold: Int = 0
    var execLevel: Short = 1
    val execLevNew = ShortArray(EXECLEVS)
    var breakFlag: Short = 0

    val dosave: StringBuilder = StringBuilder(STR_MAX)
    val cod206: Char = 206.toChar(); val cod207: Char = 207.toChar()
    val cod208: Char = 208.toChar(); val cod209: Char = 209.toChar()
    val cod210: Char = 210.toChar()

    var retz: Short = 0; var icmnd: Short = 0; var p2px: Short = 0
    var ernbr: Short = 0; var fileflg: Short = 0
    val tmp2: StringBuilder = StringBuilder(STR_MAX)
    val tmp3: StringBuilder = StringBuilder(STR_MAX)
    var dirflg: Char = ' '
    val vd: StringBuilder = StringBuilder(STR_MAX)
    var pd1p: StringBuilder? = null // C: char *pd1p = (char *)&svPtr->pd1[1]
    var mergeFlag: Int = 0
    val mergeRHS: StringBuilder = StringBuilder(STR_MAX)
}
