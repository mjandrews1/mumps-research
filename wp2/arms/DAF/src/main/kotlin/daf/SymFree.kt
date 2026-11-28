package daf

// R1 stub-completion (2026-11-27, C-020 erratum session, tranche 3):
// interp-sym.cpp::_SymFree (35 LOC, roster row 5, complex): the
// symbol-table layer pop. If the argument is non-zero the top
// layer is popped; if zero, variables are deleted only (the
// vendor header comment kept). The Kotlin seat walks the real
// chain store (DafSymChains.start, the unit-279 side carrier;
// DafMsv.start models the layer STACK indices and is left
// untouched - the chain head nulling is the observable, ledgered).
// Consults (2026-11-27T00:00:00Z, nominal): BASE-log row 37
// hit-adapt (struct stab * chain walk rides the DafStabNode
// next links of the unit-279 store); rows 18/22 hit-reuse (the
// free(p1->name)/delete p1->data/p1->OrigName trio is the GC
// fold - no explicit free carrier on the JVM, disclosed); row 2
// family hit-reuse (the underflow printf renders as the
// vendor-message println precedent); rows 111/68 hit-reuse
// (symRetDaf the sym_ seat; sigintDaf the T1 seat).
// Vendor quirks kept: (1) the store-by-ref relay sym_(0, OrigName,
// data, svPtr) fires only when the node is exec AND OrigName is
// non-empty (the vendor test *p1->OrigName != ""); (2) the
// i argument is accepted but never consulted by the vendor body
// (the two call sites pass 1) - kept verbatim, the parameter
// stays; (3) svPtr->_Sym-- happens only on the non-zero-argument
// path AFTER the chain walk, and the underflow check prints and
// sigints but still returns 0 - kept; (4) the _Sym==0 arm frees
// the whole chain without the store relay - kept.
// Dafny: EXEMPT (state-machine symbol-table plumbing; journal
// row).

fun symFreeDaf(i: Int, svPtr: DafMsv): Int {

    var p1 = DafSymChains.start[svPtr._Sym] // p1 = svPtr->start[svPtr->_Sym]
    DafSymChains.start[svPtr._Sym] = null   // svPtr->start[svPtr->_Sym]=NULL

    if (svPtr._Sym == 0) {

        while (p1 != null) {
            // free(p1->name); delete p1->data; delete p1->OrigName;
            // (the GC fold - no explicit free carrier)
            val p2 = p1.next
            p1 = p2
        }
        return 0
    }

    while (p1 != null) {

        if (p1.exec != 0 && p1.origName != "") {
            // sym_(0, USTR(*p1->OrigName).c_str(),
            //      USTR(*p1->data).c_str(), svPtr); // store call by ref
            val aB = CharArray(STR_MAX_DAF)
            var q = 0
            while (q < p1.origName.length && q < aB.size - 1) {
                aB[q] = p1.origName[q]; q++
            }
            aB[q] = NUL_D
            val outB = CharArray(STR_MAX_DAF)
            q = 0
            while (q < p1.data.length && q < outB.size - 1) {
                outB[q] = p1.data[q]; q++
            }
            outB[q] = NUL_D
            symRetDaf(0, aB, 0, outB, 0, svPtr)
        }

        // free(p1->name); delete p1->data; delete p1->OrigName;
        val p2 = p1.next
        p1 = p2
    }

    svPtr._Sym--

    if (svPtr._Sym < 0) {
        println("*** SymFree symbol table underflow")
        sigintDaf(100)
    }

    return 0
}
