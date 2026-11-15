package mumps.interp

import mumps.mdh.sym_

/**
 * PRO-arm translation of interp-sym.cpp::_SymFree (pilot B1, unit 28).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 876-914 (units.csv: 35 SLOC).
 *
 * _SymFree(i, svPtr): frees the top symbol-table list. If the table
 * depth _Sym is 0, the variables are simply dropped; otherwise each
 * node with exec set and a non-empty OrigName is first stored back via
 * sym_(0, OrigName, data, svPtr) (store call by ref), and the depth is
 * decremented afterwards (underflow message + sigint(100) if it goes
 * negative). The parameter i is unused by the vendor body.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro : printf -> System.out.print; the
 *    std::string c_str() arguments for sym_ become fresh CharArray
 *    scratch via the local toC helper.
 *  - h004 globals_ext        : svPtr->start[SYM_MAX] (stateVector.h:71,
 *    SYM_MAX=200, defines.h:218) and svPtr->_Sym (:65) are added to
 *    Msv with this unit; the struct stab carrier (sym.h:43-51) is
 *    added here as class Stab - the full symbol-table machinery it
 *    belongs to is interp-sym.cpp::sym_ (batch-10 unit, not in B1).
 *  - h012 unsigned_byte_domain: name is a C byte domain (CharArray);
 *    data/OrigName are std::string heap objects -> Kotlin String?.
 *  - h014 out_of_scope_call  : sym_ (MdhStubs stub, owner sym_
 *    machinery) and sigint (InterpStubs stub, batch-7 unit).
 *  - h015 grammar_collision  : pre-push scan incl. cross-file top-level
 *    names (new top-level: _SymFree, Stab, private helper - no
 *    collisions; leading-underscore name kept per h017).
 *  - h017 naming             : _SymFree keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - free(p1->name) / delete p1->data / delete p1->OrigName / free(p1)
 *    have no Kotlin analog (GC); the fields are nulled and the node
 *    dropped from the list, which is observably equivalent here.
 *  - The parameter i is never used (dead parameter, kept in the
 *    signature).
 *  - The vendor dereferences OrigName without a null check; the port
 *    adds the nullable guard required by Kotlin String? (the vendor
 *    would segfault on the equivalent state).
 *  - sym_(0,...) is the store operation (symflg 0), matching the
 *    vendor comment "store call by ref".
 */
fun _SymFree(i: Int, svPtr: Msv): Int {
    var p1: Stab? = svPtr.start[svPtr._Sym]
    svPtr.start[svPtr._Sym] = null

    // if argument is zero, delete variables only
    // (argument of zero is used with kill all locals)
    if (svPtr._Sym == 0) {
        while (p1 != null) {
            p1.name = null // free(p1->name)
            p1.data = null // delete p1->data
            p1.OrigName = null // delete p1->OrigName
            val p2 = p1.next
            p1 = p2 // free(p1)
        }
        return 0
    }

    while (p1 != null) {
        if (p1.exec != 0 && p1.OrigName != null && p1.OrigName.isNotEmpty()) {
            // vendor: sym_(0, USTR(*p1->OrigName).c_str(),
            //                USTR(*p1->data).c_str(), svPtr); // store call by ref
            sym_(0, toC(p1.OrigName!!), toC(p1.data ?: ""), svPtr)
        }

        p1.name = null // free(p1->name)
        p1.data = null // delete p1->data
        p1.OrigName = null // delete p1->OrigName
        val p2 = p1.next
        p1 = p2 // free(p1)
    }

    svPtr._Sym--

    if (svPtr._Sym < 0) {
        System.out.print("*** SymFree symbol table underflow\n")
        sigint(100)
    }

    return 0
}

/**
 * Vendor struct stab (include/mumpsc/sym.h:43-51). Carrier added by
 * B1 unit 28; the full machinery owner is interp-sym.cpp::sym_
 * (batch-10 unit, not in B1). name is a C byte domain; data/OrigName
 * are std::string heap objects (String? here); size/copy/exec are
 * the vendor int fields.
 */
class Stab {
    /** sym.h:44 - char *name. */
    var name: CharArray? = null

    /** sym.h:45 - struct stab *next. */
    var next: Stab? = null

    /** sym.h:46 - string *data (new-allocated in the vendor). */
    var data: String? = null

    /** sym.h:47 - string *OrigName (new-allocated in the vendor). */
    var OrigName: String? = null

    /** sym.h:48 - int size. */
    var size: Int = 0

    /** sym.h:49 - int copy. */
    var copy: Int = 0

    /** sym.h:50 - int exec. */
    var exec: Int = 0
}

/** USTR(str).c_str() analog - fresh CharArray scratch (h008-style). */
private fun toC(s: String): CharArray {
    val a = CharArray(s.length + 1)
    for (i in s.indices) a[i] = s[i]
    return a
}
