package doc

// Unit 28 (B1 tranche 5): interp-sym.cpp::_SymFree (batch 1, complex
// stratum; units.csv row 28: loc 35). Symbol-table scope release:
// with a nonzero argument pops the table level (re-storing live
// globals by reference via sym_ before freeing), with zero deletes
// the variables only. struct stab (sym.h:43: name, next, data,
// OrigName, size, copy, exec) renders as the Stab carrier class;
// the MSV fields start[SYM_MAX], _Sym, xd, xpx ride the Msv
// carriers (stateVector.h:71/65/45/56; SYM_MAX 200 defines.h:218).
// free/delete render as referent nulling with the GC divergence
// note (the C explicit-free ordering has no analog; the next-links
// are walked before unlinking). sym_ is the H-014 stub; sigint is
// the H-014 stub; printf maps to System.out (H-001); the vendor
// comma-operator line (printf, sigint) is kept as the two
// statements. The Stubs.SymFree seat closes to the delegate.
// Naming per H-017.

// sym.h:43 struct stab
class Stab {
    var name: CharArray = CharArray(0)
    var next: Stab? = null
    var data: Mstring? = null
    var OrigName: Mstring? = null
    var size: Int = 0
    var copy: Int = 0
    var exec: Int = 0
}

fun symFree(i: Int, svPtr: Msv): Int {
    // if argument is non zero, pop the symbol table.
    // if argument is zero, delete variables only.
    // argument of zero is used with kill all locals.

    var p1 = svPtr.start[svPtr._Sym]
    svPtr.start[svPtr._Sym] = null

    if (svPtr._Sym == 0) {
        while (p1 != null) {
            p1.name = CharArray(0) // free(p1->name)
            p1.data = null // delete p1->data
            p1.OrigName = null // delete p1->OrigName
            val p2 = p1.next
            p1.next = null // free(p1)
            p1 = p2
        }
        return 0
    }

    while (p1 != null) {
        val origName = p1.OrigName
        val data = p1.data
        if (p1.exec != 0 && origName != null &&
                origName.str.isNotEmpty()) {
            // *p1->OrigName != "" is the vendor string comparison
            Stubs.sym_(0, origName.str.toCharArray(),
                    data?.str?.toCharArray() ?: CharArray(0), svPtr)
        }

        p1.name = CharArray(0)
        p1.data = null
        p1.OrigName = null
        val p2 = p1.next
        p1.next = null
        p1 = p2
    }

    svPtr._Sym--

    if (svPtr._Sym < 0) {
        System.out.print("*** SymFree symbol table underflow\n")
        Stubs.sigint(100)
    }

    return 0
}
