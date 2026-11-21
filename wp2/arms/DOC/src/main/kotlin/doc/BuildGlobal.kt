package doc

// Unit 230 (B8 tranche 2): interp-parse.cpp::BuildGlobal (batch 8,
// core stratum; LOC 27, score 16.1, rank 150; units.csv row 233;
// vendor interp-parse.cpp:1836, physically verified this session:
// the dump asserts physical loc 27 = the roster row). The global
// build-stack helper of the parser MERGE/order walk: the three-arm
// code dispatch over the function-static TSTACK x STR_MAX scratch
// and stk counter. Consult H-014 hit-reuse: code<0 resets stk
// (returns 1), code==0 pushes str (the overflow banner prints
// svPtr->LineNumber and calls sigint(100) - the Sigint.kt real of
// the batch-7 flip), code==2 pops into rslt (empty write when the
// stack is empty), and the trailing code-1 arm (the vendor's
// fall-through below the // code 1 comment) decrements and hands
// the walk to the four-arg GlobalOrder stub seat (the NEXT/
// ORDERNEXT branches pass the "1" direction, ORDERPREV the "-1"
// one; other g values return 0 per vendor). The function-statics
// render as the H-004 holder object BuildGlobalState (the
// MakeGlobalState/MakeGlobal precedent; separate state from the
// unit-231 BuildLocal twin - the vendor keeps two independent
// static stacks). str/rslt/svPtr stay nullable per the widened
// H-012 stub seat being resolved (the unit-185 Interpret cleaner
// passes NULLs down the code<0 arm); the deref arms keep callers
// honest with explicit IllegalArgumentException seats instead of
// the C UB (the H-012 contract note). Naming per H-017.

private const val ORDERNEXT_BG = 12 // compiler-macros.h:59-era NEXT codes
private const val ORDERPREV_BG = 13

object BuildGlobalState {
    // static unsigned char tmp[TSTACK][STR_MAX] (vendor :1837)
    val tmp = Array(TSTACK) { CharArray(STR_MAX) }
    // static int stk (vendor :1838)
    var stk = 0
}

fun BuildGlobal(
    code: Int, g: Int, str: CharArray?,
    rslt: CharArray?, svPtr: Msv?
): Int {
    val T = BuildGlobalState

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

    if (g == NEXT) Stubs.GlobalOrder(T.tmp[T.stk], popRslt, "1", sv)
    else if (g == ORDERNEXT_BG) Stubs.GlobalOrder(T.tmp[T.stk], popRslt, "1", sv)
    else if (g == ORDERPREV_BG) Stubs.GlobalOrder(T.tmp[T.stk], popRslt, "-1", sv)
    else return 0

    return 1
}
