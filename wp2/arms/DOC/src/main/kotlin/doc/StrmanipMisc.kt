package doc

// Unit 141 (B5 tranche 2): interp-strmanip.cpp::<misc> (batch 5, core
// stratum; LOC 36, score 28.0) - the Mumps Run-Time Support residual
// (LGPL banner block kept verbatim below; note this file's license
// text DIFFERS from the GPL compiler banners - vendor fact, kept).
// The chunk is the banner + include set + three entry residues:
//
//   rindex(char*,int) - the vendor's plain definition shadowing the
//     libc rindex (kept; NOTE the empty #ifdef MSVC pair directly
//     above it resolves to NOTHING live per H-018 - the ifdef is a
//     vendor relic; nothing else is inside the pair). Nothing in-arm
//     calls it; rendered for residue fidelity as a private seat with
//     the pointer-return folded to the found-index offset per H-007
//     (NULL -> -1 sentinel). MSVC arm note: per the reference config
//     the definition stands (it is NOT inside the ifdef).
//
//   strcatx(unsigned char*,unsigned char*) - DUPLICATE-DEF LEDGER
//     NOTE: the vendor body here is a local copy of the inline.h:46
//     helper (append in at out's NUL, then the x01 delimiter byte,
//     then NUL - semantics identical to the internal strcatx seat
//     rendered with unit 23). The in-arm live seat stays the
//     existing internal helper (Input.kt); no second definition is
//     rendered (a duplicate top-level would collide). The in-body
//     commented-out strcat lines are vendor fossil, kept as the
//     comment in the seat's own header.
//
//   lpad(unsigned char[],unsigned char[],short) - the REAL body;
//     RESOLVES the unit-19-era H-014 stub seat Stubs.lpad (removed
//     in this push; the Fcn unit-123 call site rebinds to this
//     render). The vendor short-domain counters j/i/k keep the
//     Short types per H-010 (widening only at the index arithmetic);
//     the early return when k <= 0 leaves strout UNTOUCHED (the
//     C caller-owned buffer is not written - unit-123 call sites
//     pre-clear their own carriers, verified). Below-min-loc
//     siblings substr (batch 7) and xindex (batch 9) are later-batch
//     roster rows - their sections stand empty here per the
//     pre-registered chunker (banner markers kept).
//
// The sigint prototype maps to the Stubs.sigint seat (H-014).
// Naming per H-017 (rindex/lpad/strcatx vendor names kept).

private const val STRMANIP_BANNER = """
    Mumps Compiler Run-Time Support Functions
    Copyright (c) 2001, 2002, 2022  by Kevin C. O'Kane
    kc.okane@gmail.com, https://threadsafebooks.com/
    GNU LGPL 2.1 or later, no warranty (full text in vendor source).
    """

// include ledger (vendor includes of interp-strmanip.cpp):
//   stdio.h / stdlib.h   JVM console IO (System.err seats), no macros
//   string.h             CstringOps carrier
//   mumpsc/fcns.h        prototype roster (owners named per H-014)

// char *rindex(char *s, int c): last-index scan; the pointer return
// folds to the found index (H-007; NULL -> -1). Walk starts AT the
// NUL index and descends (vendor j = i start - the terminator itself
// matches when c == 0, vendor shape kept).
private fun rindex(s: CharArray, c: Int): Int {
    val i = CstringOps.strlenC(s)
    var j = i
    while (j >= 0) {
        if (c == s[j].code) {
            return j
        }
        j--
    }
    return -1
}

// char *lpad(unsigned char strin[], unsigned char strout[], short l)
fun lpad(strin: CharArray, strout: CharArray, l: Short) {
    val j = CstringOps.strlenC(strin) - 1
    val k = l.toInt() - j - 1

    if (k <= 0) return

    var i = j
    while (i >= 0) {
        strout[i + k] = strin[i]
        i--
    }

    strout[j + k + 1] = NUL

    i = 0
    while (i < k) {
        strout[i] = ' '
        i++
    }
}
