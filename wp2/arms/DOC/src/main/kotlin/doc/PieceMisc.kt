package doc

// Unit 114 (B4 tranche 3): interp-piece.cpp::<misc> (batch 4,
// complex stratum; LOC 140, score 52.2, rank 236). The piece
// library file residue: the Dec 13, 2025 GPL banner, the SQLITE
// define (IN-FILE, annotated), the module banner, the include
// ledger, the _INTERP_/CSTR/USTR/intLong define notes, the
// Interpret/ScanParse/Eval prototype ledger (the extern
// __label_lookup/__text_function roster notes, the extern "C"
// _getpid/time notes), and the _piece body itself (the piece
// extractor/setter of the \u0024Piece intrinsic family). H-018 note:
// the vendor comment "floatSize is defines BIGFLOAT if selected
// in configure" records the config switch - BIGFLOAT is OUT
// per the reference build; the #define intLong is IN-FILE
// UNCONDITIONAL here (the interp-arith.cpp:59 precedent: the
// long arm is the compiled domain of this TU). _piece: the
// start/end -1 sentinels default k/l to 1; the start<0 || end<
// start banner prints through the DOLLAR_P const (H-015); the
// delimiter walk is the xindex seat chain (H-014) with the
// m==1 depletion early-out; the end walk stops at l with the
// length-plus-one tail clamp (a fullwidth vendor semicolon
// was narrowed to ASCII);
// rebuild concatenates lhs via the catAt seat adapter with the
// SOH tmp[k]/tmp[k+1] delimiter pair, delivering through sym_
// (local) or Mglobal(STORE) (caret); the plain extract writes
// substr(tmp2, out, k, m). strmove rides the H-014 seat
// (inline.h owner); atoi the arm seat (Find.kt); printf maps to
// System.out (H-001); sigint/substr/sym_/Mglobal the H-014
// seats. Vendor quirks kept and noted: the dead l assignment
// of the no-start arm is the p1 goto shape; the two strmove
// calls into tmp2/tmp3 fold through the seat; the piece1
// m==0 inner if(setpiece==0) double-test kept verbatim; the
// xindex shifts cast to Short per the vendor cast. Naming per
// H-017 (_piece underscore verbatim; banner consts private).

private const val DOLLAR_P = '\u0024'

// include ledger (vendor residual chunk):
//   stdio/stdlib/string/<string>/sys/types/monetary/locale/math
//   mumpsc/defines.h, sym.h, btree.h, globalOrder.h, keyfix.h,
//   inline.h, unistd, time, sys/types, fcns.h, builtin.h,
//   interp.h, global.h
// prototype roster:
//   Interpret (Done: the batch-6 roster row family)
//   ScanParse (not a roster unit, future batch seat)
//   Eval      (not a roster unit, future batch seat)
//   extern int (*__label_lookup)(char*) - roster note
//   extern char* (*__text_function)(int) - roster note
//   extern "C" _getpid / time - roster notes (JVM analogs)
private val includeLedger114 = listOf(
    "stdio", "stdlib", "string", "sys/types", "monetary.h",
    "locale.h", "math.h", "unistd.h", "time.h",
    "mumpsc/defines.h", "mumpsc/sym.h", "mumpsc/btree.h",
    "mumpsc/globalOrder.h", "mumpsc/keyfix.h", "mumpsc/inline.h",
    "mumpsc/fcns.h", "mumpsc/builtin.h", "mumpsc/interp.h",
    "mumpsc/global.h"
)

fun _piece(
    out: CharArray,
    in_: CharArray,
    key: CharArray,
    start: CharArray,
    end: CharArray,
    setpiece: Int,
    lhs: CharArray,
    svPtr: Msv
) {
    /* for setpiece=1:
       out is name of variable into which to store result
       in is input value of variable given in out
       key is the pattern code
       start and end are location counters
       lhs is the value to be inserted into in
    */

    val tmp2 = CharArray(STR_MAX)
    val tmp3 = CharArray(STR_MAX)
    // k/l init 1: the vendor goto p1 always defines them
    var k = 1
    var l = 1
    var m: Int
    var n: Int

    var atP1 = false

    if (CstringOps.strncmpS("-1", start, 2) == 0) { /* no start or end */
        k = 1
        l = 1
        atP1 = true
    }

    if (!atP1) {
        Stubs.strmove(tmp2, start)
        k = atoi(CstringOps.cstring(tmp2)) /*start */

        l = k /* default end is same as start */

        if (CstringOps.strncmpS("-1", end, 2) != 0) { /* end? */
            Stubs.strmove(tmp2, end)
            l = atoi(CstringOps.cstring(tmp2))
        }

        if (k < 0 || l < k) { /* check */
            System.out.print("** " + DOLLAR_P + "Piece error start=" + k + " end=" + l + "\n")
            Stubs.sigint(100)
        }
    }

    // p1:
    Stubs.strmove(tmp2, in_)
    Stubs.strmove(tmp3, key)

    if (tmp2[0] == NUL || tmp3[0] == NUL || k > l || l <= 0) {
        if (setpiece == 0) Stubs.strmove(out, CharArray(1))
        return
    }

    m = 0
    n = 0

    while (n < k - 1) {
        m = Stubs.xindex(tmp2, tmp3, m.toShort()) + 1

        if (m == 1) {
            if (setpiece == 0) Stubs.strmove(out, CharArray(1))
            return
        }

        n++
    }

    if (k != 1) k = m + CstringOps.strlenC(tmp3) - 1

    m = 0
    n = 0
    var atPiece1 = false
    while (n != l) {
        m = Stubs.xindex(tmp2, tmp3, (m + 1).toShort())

        if (m <= 0) {
            m = CstringOps.strlenC(tmp2) + 1
            atPiece1 = true
            break
        } else {
            n++
        }
    }

    if (!atPiece1) m = m - k

    // piece1:
    if (m == 0 && setpiece == 0) {
        if (setpiece == 0) Stubs.strmove(out, CharArray(1))
        return
    }

    if (setpiece == 1) {
        val tmp = CharArray(STR_MAX)
        Stubs.strmove(tmp, in_)
        Stubs.substr(tmp2, tmp, 1, k - 1)
        tmp[k] = CHAR_1
        tmp[k + 1] = NUL
        CstringOps.catAt(tmp, CstringOps.strlenC(tmp), lhs)
        if (k + m - 1 < CstringOps.strlenC(tmp2)) {
            CstringOps.catAt(
                tmp, CstringOps.strlenC(tmp),
                CstringOps.cstringAt(tmp2, k + m - 1)
            )
        }
        if (out[0] != '^') {
            Stubs.sym_(0, out, tmp, svPtr)
        } else {
            Stubs.Mglobal(STORE, out, tmp, svPtr)
        }
        return
    }

    Stubs.substr(tmp2, out, k, m)
}
