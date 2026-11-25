package daf

/*
 * DAF batch-10 unit 288 (roster row 288; interp-sysfunc.cpp::unpad,
 * LOC 16, ptr 8, score 22.0). Consult 2026-11-25T15:05:00Z.
 *
 * unpad + its same-TU sibling fullUnpad (the sym_ 101 call site,
 * interp-sym.cpp:457; the sym_ release InterpSym.kt binds it).
 * Seats: unpadDaf(key, off, svPtr) - the offset form carries the
 * vendor unpad(key)-style in-place semantics at an arbitrary NUL
 * head - and fullUnpadDaf(key, svPtr).
 *
 * Fold notes: GPADWIDTH 40 (defines.h:215) pinned GPADWIDTH_D;
 * sscanf %lf folds onto the guarded scanLfUS (garbage -> 0.0,
 * the C convention); sprintf %lg folds onto sprintfLgUS (the JVM
 * percent-g seat, the sym_ coded-number exit precedent); strtok
 * over the {1, 0} delimiter folds onto a char-1 split walk
 * (the null-return-first-token vendor semantics kept via a
 * String build); strcatx rides the arm strcatxD seat of
 * StrmanipMisc.kt (unit-52 family).
 *
 * H-015: ASCII; no raw dollars in comments; trailing newline.
 * Dafny: EXEMPT (string-manipulation seat; journal row).
 */

const val GPADWIDTH_D = 40 // #define GPADWIDTH 40 (defines.h:215)

// void unpad(unsigned char *key, struct MSV *)
fun unpadDaf(key: CharArray, off: Int, svPtr: DafMsv) {

    var r = off // unsigned char *r = key;

    if (svPtr.gpad == 0) return

    if (key[r] == '\u001F') { // a gpad entry found (\x1f)
        val tmp = CharArray(64) // unsigned char tmp[64]
        r++

        // if (*r=='0') *r='-'; else *r='+';
        if (key[r] == '0') key[r] = '-' else key[r] = '+'

        // memcpy(tmp, r, GPADWIDTH); tmp[GPADWIDTH] = '\0';
        var i = 0
        while (i < GPADWIDTH_D && key[r + i] != NUL_D) {
            tmp[i] = key[r + i]
            i++
        }
        tmp[i] = NUL_D

        // sscanf(tmp, "%lf", &x); sprintf(key, "%lg", x);
        val x = scanLfUS(DafCRuntime.cstring(tmp))

        val res = sprintfLgUS(x)
        var q = 0
        while (q < res.length && off + q < key.size) {
            key[off + q] = res[q]
            q++
        }
        key[off + q] = NUL_D
    }

    return
}

// void fullUnpad(unsigned char *key, struct MSV *)
fun fullUnpadDaf(key: CharArray, svPtr: DafMsv) {

    val tmp1 = CharArray(STR_MAX_DAF) // tmp1[STR_MAX]=""
    val tmp2 = CharArray(STR_MAX_DAF) // tmp2[STR_MAX]
    // static unsigned char delim[2] = {1, 0}
    // int i, j, k; (unused locals kept as this note)

    tmp1[0] = NUL_D

    // strtok(key, delim): the first token (vendor NULL when the
    // key is empty - the tmp1 stays empty; the walk folds onto a
    // char-1 split of the in-place carrier)
    val src = DafCRuntime.cstring(key)
    if (src.isNotEmpty()) {
        // strtok returns the token up to the first delimiter
        var e = 0
        while (e < src.length && src[e] != '\u0001') e++
        val first = src.substring(0, e)
        strcatxD(tmp1, first.toCharArray()) // strcatx(tmp1, strtok(key))
    }

    var pend = -1
    var q = 0
    while (q <= src.length) {
        val atEnd = q == src.length
        if (atEnd || src[q] == '\u0001') {
            if (pend >= 0) {
                var n = 0
                tmp2[0] = NUL_D
                while (pend + n < q && n < tmp2.size - 1) {
                    tmp2[n] = src[pend + n]
                    n++
                }
                tmp2[n] = NUL_D
                unpadDaf(tmp2, 0, svPtr) // unpad(tmp2, svPtr)
                strcatxD(tmp1, tmp2) // strcatx(tmp1, tmp2)
                pend = -1
            }
            if (atEnd) break
            pend = q + 1
        }
        q++
    }

    // strcpy(key, tmp1)
    var k = 0
    while (k < tmp1.size && tmp1[k] != NUL_D) {
        key[k] = tmp1[k]
        k++
    }
    key[k] = NUL_D

    return
}

// sscanf("%lf") guard fold: garbage -> 0.0 (the C convention)
private fun scanLfUS(s: String): Double =
    try { s.trim().toDouble() } catch (e: NumberFormatException) { 0.0 }

// sprintf("%lg") fold (the JVM percent-g seat)
private fun sprintfLgUS(x: Double): String = DafCRuntime.fmtG(x)
