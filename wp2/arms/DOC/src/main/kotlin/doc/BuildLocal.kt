package doc

// Unit 231 (B8 tranche 2): interp-sym.cpp::BuildLocal (batch 8,
// core stratum; LOC 29, score 16.7, rank 152; units.csv row 232;
// vendor interp-sym.cpp:97, physically verified this session:
// the dump asserts physical loc 29 = the roster row). The
// local-symbol build-stack sibling of the unit-230 BuildGlobal
// render: identical three-arm code dispatch over its OWN
// function-static TSTACK x STR_MAX scratch and stk counter (the
// vendor keeps two independent static stacks; separate H-004
// holder object BuildLocalState). Consult H-014 hit-reuse: the
// same overflow banner (LineNumber + sigint(100), the batch-7
// Sigint.kt real), the same push/pop/reset contract; the vendor
// code-1 trailing arm of this body is the commented-out fff
// printf debug fossil (kept verbatim as a comment per H-013) and
// the return-1 tail (no GlobalOrder walk here - the vendor body
// ends at the code-2 arm). str/rslt/svPtr stay nullable per the
// widened H-012 stub seat being resolved. The code-1 tail walks
// the LOCAL symbol order: the fff printf fossils kept verbatim
// per H-013, then the NEXT/ORDERNEXT/ORDERPREV branches hand
// rslt to the new Stubs.LocalOrder seat (owner
// interp-sym.cpp::LocalOrder, NOT a corpus unit - raised this
// render per H-014; "1"/"-1" direction strings kept; other g
// values return 0). Naming per H-017.

private const val ORDERNEXT_BL = 12
private const val ORDERPREV_BL = 13

object BuildLocalState {
    // static unsigned char tmp[TSTACK][STR_MAX] (vendor :98)
    val tmp = Array(TSTACK) { CharArray(STR_MAX) }
    // static int stk (vendor :99)
    var stk = 0
}

fun BuildLocal(
    code: Int, g: Int, str: CharArray?,
    rslt: CharArray?, svPtr: Msv?
): Int {
    val T = BuildLocalState

    if (code < 0) {
        T.stk = 0
        return 1
    }

    if (code == 0) {
        if (T.stk >= TSTACK) {
            System.out.print("*** Build Stack Overflow " +
                    (svPtr?.LineNumber ?: 0) + "\n\n")
            sigint(100)
        }

        // the vendor strcpy dereferences str; the H-012 widening
        // keeps callers honest here instead of the C UB
        val src = str
            ?: throw IllegalArgumentException("null str on the push arm")
        CstringOps.strcpyC(T.tmp[T.stk++], src)
// fff printf("4444444 str=%s\n", str);
        return 1
    }

    if (code == 2) {
        val out = rslt
            ?: throw IllegalArgumentException("null rslt on the pop arm")
        if (T.stk == 0) {
            out[0] = NUL
        } else {
            CstringOps.strcpyC(out, T.tmp[--T.stk])
        }

        return 1
    }

    // code 1

    T.stk--

    // the walk arms dereference rslt/svPtr in the vendor; the H-012
    // widening keeps callers honest here instead of the C UB
    val popRslt = rslt
        ?: throw IllegalArgumentException("null rslt on the order arm")
    val sv: Msv = svPtr ?: Globals.svPtr

// fff printf("55555 stack=%s g=%d ORDERNEXT=%d\n", tmp[stk], g, ORDERNEXT);

    if (g == NEXT) Stubs.LocalOrder(T.tmp[T.stk], popRslt, "1", sv)
    else if (g == ORDERNEXT_BL) Stubs.LocalOrder(T.tmp[T.stk], popRslt, "1", sv)
    else if (g == ORDERPREV_BL) Stubs.LocalOrder(T.tmp[T.stk], popRslt, "-1", sv)
    else return 0

    return 1
}
