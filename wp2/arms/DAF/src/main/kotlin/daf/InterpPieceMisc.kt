package daf

// Unit 114 (DAF B4 T3, complex): interp-piece.cpp::<misc> (140 LOC): the
// interpreter symbol-TU residue (GPL banner + include set + the
// SQLITE/_INTERP_ defines + prototype externs) plus the one body the
// unit carries: _piece, the \$piece service kernel.
// Residue consult: BASE-log row 37 hit-adapt (banner not emitted by any
// graded path and not replicated, per the T1/T2 residue precedent;
// prototypes recorded as named-owner seats in DafInterpStubsT3.kt; the
// SQLITE define rides the SQLITE_D const seated in T2).
// _piece consults: BASE-log row 97 hit-reuse (the \$piece service call
// shape; the out-buffer of the setpiece==0 read path folds onto the
// returned String per the row-97 BASE fold; the setpiece==1 LHS store
// keeps the out NAME because the vendor stores into the variable the
// name denotes), rows 15/14 (strcmp "-1" sentinel compares), row 28
// (atoi for the start/end counters), row 81 (NUL-terminated buffer
// reads; the v1d/bd slices passed by the fcn call sites are
// NUL-terminated at their offsets, so the render reads String slices),
// row 8 family (strmove/substr utils collapse onto String operations;
// substrate noted: strmove copies with terminator), row 2 family (the
// printf \$Piece error diagnostic on stdout), row 10 (the goto p1/piece1
// restarts -> the shared-tail fold + structured control).
// Seats: xindexDaf (owner roster interp-strmanip.cpp::xindex b9 /
// compiler-sys1.c::xindex b10), symRetDaf (permanent sym_ dispatcher
// stub, SYMSTORE path), MglobalDaf (roster b6, STORE path), sigintDaf
// (T1 seat). The sigint(100) calls relay the vendor fatal-exit.
// Dafny: the pure piece-splitting contract of the setpiece==0 read
// path is spec'd at spec/piece.dfy (row-97 fold); the LHS store path is
// EXEMPT (stub-mediated).

const val FCN_MARK1_D: Char = '\u0001' // vendor single-byte marker (char)1

// void _piece(out, in, key, start, end, setpiece, lhs, svPtr)
// Row-97 fold: setpiece==0 reads return the piece (out-buffer -> return
// value); setpiece==1 keeps out as the target variable NAME and stores
// into it via sym_/Mglobal exactly as the vendor does.
fun pieceDaf(
    out: String, inS: String, key: String, start: String, end: String,
    setpiece: Int, lhs: String, sv: DafMsv
): String {

    var k: Int
    var l: Int

    if (start == "-1") { // no start or end
        k = 1
        l = 1
        return pieceBody(out, inS, key, k, l, setpiece, lhs, sv) // goto p1
    }

    k = atoi(start) // strmove(tmp2, start); k = atoi
    l = k           // default end is same as start

    if (end != "-1") { // end?
        l = atoi(end)
    }

    if (k < 0 || l < k) { // check
        print("** \$Piece error start=$k end=$l\n")
        sigintDaf(100)
    }

    return pieceBody(out, inS, key, k, l, setpiece, lhs, sv) // p1
}

// p1: the shared tail of the goto chain (row 10 fold)
private fun pieceBody(
    out: String, inS: String, key: String, kIn: Int, lIn: Int,
    setpiece: Int, lhs: String, sv: DafMsv
): String {

    var k = kIn
    var l = lIn
    val inArr = inS.toCharArray()
    val keyArr = key.toCharArray()

    // strmove(tmp2, in); strmove(tmp3, key)
    if (inS.isEmpty() || key.isEmpty() || k > l || l <= 0) {
        return "" // strmove(out, "") / plain return
    }

    var m = 0
    var n = 0
    fun xindexAt(from: Int): Int = xindexDaf(inArr, 0, keyArr, 0, from)

    while (n < k - 1) {
        m = xindexAt(m) + 1
        if (m == 1) {
            return "" // setpiece==0 printf-free empty return
        }
        n++
    }

    if (k != 1) k = m + key.length - 1

    m = 0
    n = 0
    var ranPastEnd = false
    while (n != l) {
        m = xindexAt(m + 1)
        if (m <= 0) {
            m = inS.length + 1
            ranPastEnd = true // goto piece1
            break
        }
        n++
    }

    if (!ranPastEnd) m = m - k

    // piece1:
    if (m == 0 && setpiece == 0) {
        return "" // strmove(out, "")
    }

    if (setpiece == 1) {
        // substr(tmp2, tmp, 1, k - 1); tmp[k]=1; strcat(tmp, lhs);
        // if (k + m - 1 < strlen(tmp2)) strcat(tmp, &tmp2[k+m-1]);
        var tmp = StringBuilder()
        val headEnd = (k - 1).coerceIn(0, inS.length)
        tmp.append(inS, 0, headEnd)
        tmp.append(FCN_MARK1_D) // tmp[k] = 1 marker byte
        tmp.append(lhs)
        if (k + m - 1 < inS.length) {
            tmp.append(inS, (k + m - 1).coerceIn(0, inS.length), inS.length)
        }
        val value = tmp.toString()

        // if (out[0] != '^') sym_(0, out, tmp, svPtr);
        // else Mglobal(STORE, out, tmp, svPtr);
        val outArr = out.toCharArray()
        val valArr = value.toCharArray()
        if (!out.startsWith("^")) {
            symRetDaf(SYMSTORE_D, outArr, 0, valArr, 0, sv)
        } else {
            MglobalDaf(STORE_D, outArr, valArr, sv)
        }
        return value
    }

    // substr(tmp2, out, k, m) (read path; row-97 return fold)
    if (m < 0) return ""
    val from = (k - 1).coerceIn(0, inS.length)
    val to = (k - 1 + m).coerceIn(from, inS.length)
    return inS.substring(from, to)
}
