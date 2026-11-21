package doc

// Unit 292 (B10 tranche 2): interp-arith.cpp::Mltoa
// (batch 10, core stratum; units.csv row 292: loc 31, cr 0.088,
// ptr 3, ctl 8, score 19.5, rank 166; physically verified this
// session at vendor interp-arith.cpp:84-118, physLOC 31) - the
// long-to-decimal-ASCII emitter of the arith.h prototype roster
// (the unit-57 ArithH ledger row cites this seat: the batch-2
// Stubs.Mltoa throw flips to delegate to this render this
// push). The digit table is the static "0123456789" scratch;
// the i<10 fast path writes sign + t[i] + NUL directly. The
// two OVERFLOW guards banner "Integer overflow" and raise
// sigint(100) (the live/fatal Sigint.kt seat, the SqlIsc
// precedent): the i=-Long.MIN_VALUE negation overflow, and
// the i<=Long.MIN||i>=Long_MAX edge both per the vendor
// verbatim (the boundary equality is kept even though the
// >= arm also catches plain Long_MAX: vendor truth, H-013).
// The reverse-digit loop builds into s[126] downward and the
// residual p1 window (sign rewound when f=1) is strcpyC'd out
// (the C pointer walk is the index pair, H-007). cout renders
// as System.out.print per the house carrier. Naming per
// H-017 (Mltoa verbatim, capital M kept; no clash).
// Consult: unit 292, entry H-014, outcome hit-reuse,
// timestamp 2026-11-22T06:14:00Z.

fun Mltoa(i0: Long, p: CharArray) {
    var i = i0
    var f = 0
    val t = "0123456789"
    val s = CharArray(128)
    var m: Long
    var p1: Int

    if (i < 0) {
        f = 1
        i = -i
        if (i < 0) {
            System.out.print("Integer overflow\n")
            sigint(100) // overflow
        }
    }

    if (i <= java.lang.Long.MIN_VALUE || i >= java.lang.Long.MAX_VALUE) {
        System.out.print("Integer overflow\n")
        sigint(100) // overflow
    }

    if (i < 10) {
        if (f == 1) {
            CstringOps.strcpyS(p, "-" + t[i.toInt()])
        } else {
            CstringOps.strcpyS(p, t[i.toInt()].toString())
        }
        return
    }

    // for (p1=s+S_SIZE-2; p1!=s; p1--) { ... }
    s[128 - 1] = NUL
    p1 = 128 - 2
    while (p1 > 0) {
        m = i % 10
        i = i / 10
        s[p1] = t[m.toInt()]
        if (i == 0L) break
        p1--
    }

    if (f == 1) {
        p1--
        s[p1] = '-'
    }
    // strcpy((char*)p, &s[p1]);
    CstringOps.copyAtS(p, 0, CstringOps.cstringAt(s, p1))
}
