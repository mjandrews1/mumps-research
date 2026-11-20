package doc

// Unit 142 (B5 tranche 2): interp-sym.cpp::SysDump (batch 5, core
// stratum; LOC 18, score 28.0) - the symbol-table dump to the
// pid-named file. Vendor FILE* dump opens at the program edge: H-001
// renders PrintWriter over a FileWriter (close in all paths via the
// try/finally edge wrapper; the vendor's fopen-NULL arm calls
// ErrorMessage and CONTINUES with dump==NULL - the downstream
// fprintf(NULL,...) would then be C UB; quantized here: the
// null-open arm throws the IllegalStateException edge carrier
// instead, H-001 divergence ledgered). Mltoa rides the H-014 stub
// with the long-pid Int narrowing documented per H-010 (pid values
// are OS pids - Int range in practice). The struct stab walk rides
// the unit-28 Stab carrier: name is the CharArray field; the
// (*p1->data).c_str() deref is unguarded in the vendor - Kotlin
// nullable data requires the local-capture guard (unit-28 smartcast
// convention, adaptation noted) and empty-string fallback. The
// unused p2/p3 locals of the vendor declaration are dropped per
// H-013. Naming per H-017 (SysDump -> sysDump).

fun sysDump(pid: Long, svPtr: Msv) {
    val p = CharArray(32)
    Stubs.Mltoa(pid.toInt(), p)

    val dump: java.io.PrintWriter

    try {
        dump = java.io.PrintWriter(java.io.FileWriter(CstringOps.cstring(p)))
    } catch (e: java.io.IOException) {
        // fopen-NULL: vendor calls ErrorMessage then continues into
        // fprintf(NULL,...) UB; quantized per the header note
        Stubs.ErrorMessage("Internal sym dum failed", svPtr.LineNumber)
        throw IllegalStateException("SysDump: dump file open failed")
    }

    dump.use {
        var p1 = svPtr.start[svPtr._Sym]

        while (p1 != null) {
            it.print(CstringOps.cstring(p1.name))
            it.print("\n")

            // cp1 = (*p1->data).c_str(); - the vendor deref is
            // unguarded; Kotlin null guard with fallback (see header)
            val data = p1.data
            val cp1 = data?.c_str() ?: ""

            it.print(cp1)
            it.print("\n")

            p1 = p1.next
        }
    }
}
