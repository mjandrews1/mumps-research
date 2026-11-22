package daf

// Unit 138 (DAF B5 T2, core): interp-sym.cpp::SysDump
// (units.csv row 138: loc 18, ptr 11, score 28.0, rank 200, core):
// the pid-named symbol-table dump writer (the WRITE side of the
// pid-file pair; the READ side is the SysLoad roster unit of the
// DOC-cycle batch-10 block, not of this arm batch): walk the
// current symbol-table layer chain and write name/data pairs to
// fopen(Mltoa(pid), "w").
// Consults: BASE-log row 60 family hit-adapt (stdio file API:
// fopen("w")/fprintf/fclose fold onto a java.io writer over a
// File named by the pid string; the C fopen-NULL branch folds
// onto the open-failure guard with ErrorMessage kept verbatim),
// row 18 hit-adapt (the struct stab node chain -> class
// DafStabNode; the layer array start[Sym] of struct MSV: the
// DafMsv carrier models the layer stack as IntArray, so the node
// chains ride the side carrier DafSymChains at the same layer
// index - manual-decision, logged: no BASE row models the stab
// chain), row 64 family hit-reuse (Mltoa raised as a named-owner
// stub seat, owner interp-arith.cpp::Mltoa - a roster row of the
// batch-10 block, future DAF cycle). ErrorMessage rides the
// errorMessageDaf named-owner seat (message text kept verbatim
// incl. the vendor "dum" misspelling).
// Dafny: EXEMPT (IO-bound dump writer; journal row).

// struct stab (stateVector.h family): the symbol-table node.
// Manual-decision fold per row 18 (struct -> class); fields name/
// data/next per the vendor chain.
class DafStabNode {
    var name = ""
    var data = ""
    var next: DafStabNode? = null
}

// Side carrier of the layer chains: DafMsv.start models the layer
// STACK indices; the per-layer node chains (svPtr->start[svPtr->_Sym])
// live here at the same layer index (SYMLEVELS sized).
object DafSymChains {
    val start = arrayOfNulls<DafStabNode?>(SYM_MAX_D)
}

// char *Mltoa(long, unsigned char *) - named-owner stub seat
// (owner interp-arith.cpp::Mltoa, a roster row of the batch-10
// block; rendered when the DAF cycle reaches that cell).
fun mltoaDaf(pid: Long, p: CharArray): Unit =
    throw NotImplementedError(
        "Mltoa: out-of-scope owner (interp-arith.cpp::Mltoa, roster batch-10 row)")

fun SysDumpD(pid: Long, svPtr: DafMsv) {

    // (vendor declares char key[1024] but never uses it; the dead
    // local is ledgered here, not declared - the gate is 0-warning)
    val p = CharArray(32) // char p[32], cp1[STR_MAX];

    mltoaDaf(pid, p)

    val dump = java.io.File(DafCRuntime.cstring(p)) // dump=fopen(p,"w")
    val writer: java.io.PrintWriter? =
        try {
            java.io.PrintWriter(java.io.FileWriter(dump))
        } catch (e: java.io.IOException) {
            null // dump==NULL
        }
    if (writer == null) {
        errorMessageDaf("Internal sym dum failed", svPtr.LineNumber)
        return
    }

    var p1 = DafSymChains.start[svPtr._Sym] // p1 = svPtr->start[svPtr->_Sym]

    while (p1 != null) { // while (p1 != NULL)
        writer.print(p1.name) // fprintf(dump, "%s\n", p1->name)
        writer.print('\n'.toString())
        writer.print(p1.data) // fprintf(dump, "%s\n", cp1)
        writer.print('\n'.toString())
        p1 = p1.next // p1 = p1->next
    }
    writer.close() // fclose(dump)
}
