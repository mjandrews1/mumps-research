package mumps.interp

/**
 * Kotlin translation of interp-sym.cpp::_SymFree (Study Subset unit 28,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * C: int _SymFree(int i, struct MSV *svPtr) — pop/clear the current
 * symbol table level. QUIRK PRESERVED: the parameter i is documented as
 * pop-vs-kill-all but is NEVER TESTED in the C body — kept unused with
 * the doc comment (heuristic 75, dead-parameter class).
 *
 * free()/delete() are the GC's job here; the chain walk + call-by-ref
 * stores + underflow check are the live semantics. sym.h struct stab
 * fields carried over 1:1 (size/copy unused by this unit, present for
 * the struct's future units).
 */
class Stab {
    var name: String? = null        // C: char *name
    var next: Stab? = null         // C: struct stab *next
    var data: String? = null        // C: string *data
    var origName: String? = null    // C: string *OrigName
    var size: Int = 0               // C: int size
    var copy: Int = 0               // C: int copy
    var exec: Int = 0               // C: int exec
}

const val SYM_MAX = 200 // C: include/mumpsc/defines.h:218

fun symFree(@Suppress("UNUSED_PARAMETER") i: Int, svPtr: MsvState): Int {
    var p1 = svPtr.start[svPtr._Sym]
    svPtr.start[svPtr._Sym] = null

    if (svPtr._Sym == 0) { // C: level 0 — free the whole chain
        while (p1 != null) {
            val p2 = p1.next
            // C: free(p1->name); delete p1->data; delete p1->OrigName; free(p1);
            p1 = p2
        }
        return 0
    }

    while (p1 != null) { // C: store call-by-ref values, then free
        // C: if (p1->exec && *p1->OrigName != "") — mstring non-empty test
        if (p1.exec != 0 && !p1.origName.isNullOrEmpty()) {
            sym_(0, p1.origName!!, p1.data ?: "", svPtr) // C: sym_(0, OrigName, data, svPtr)
        }
        val p2 = p1.next
        p1 = p2
    }

    svPtr._Sym--

    if (svPtr._Sym < 0) {
        print("*** SymFree symbol table underflow\n"), sigint(100) // C: comma operator — both run
    }

    return 0
}
