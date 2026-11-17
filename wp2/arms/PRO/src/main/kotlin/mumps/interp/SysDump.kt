package mumps.interp

import mumps.mdh.Mltoa
import java.io.File
import java.io.PrintWriter

/**
 * PRO-arm translation of interp-sym.cpp::SysDump (B5 tranche B,
 * unit 137, rank 200). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 925-943.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): SysDump, cpp, loc 18, comment_ratio 0.053,
 * pointer_marks 11, control_marks 2, score 28.0 - exact match to
 * wp1/study-subset/units.csv row 138 (batch 5).
 *
 * SysDump(pid, svPtr): the symbol-table dump - the file NAME is
 * the pid in decimal (Mltoa into the 32-byte carrier), the file
 * is truncated-open for write, and every stab node of the CURRENT
 * frame (start[_Sym] head) writes its name line and its data
 * line; fclose at the end. A debug-era helper (the sym.h:55-56
 * comment block places its ancestry in mumps.c:496).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_edge            : FILE * renders PrintWriter at the
 *    edge only (CompilerState FILE* / SynInit FileReader
 *    precedents; fopen failure -> try/catch null carrier); the
 *    vendor's fprintf("%s\n") pair renders print + literal NL.
 *  - h004 globals_ext       : svPtr->start[svPtr->_Sym] and the
 *    struct stab chain are the DONE carriers (Msv.start :71,
 *    _Sym :65; class Stab in SymFree.kt, u28).
 *  - h012 unsigned_byte_domain: p1->name is a C byte domain
 *    (cstringAt over the CharArray carrier); p[32] is the
 *    Mltoa NUL-terminated buffer; p1->data is a std::string
 *    (String carrier).
 *  - h014 out_of_scope_call  : this unit RESOLVES the
 *    SymHeaderMisc.kt SysDump h014 stub raised by u134 (the
 *    prototype :70 had no seat until then); Mltoa keeps its
 *    MdhStubs stub (owner interp-arith.cpp, batch-10 unit);
 *    ErrorMessage is DONE (InterpSysfuncMisc.kt:102) - direct
 *    call.
 *  - h015 grammar_collision  : pre-push scan incl. cross-file
 *    top-level names: new top-level SysDump - the removed u134
 *    stub clears its seat; no other collisions.
 *  - h017 naming             : SysDump keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The fopen-NULL branch only calls ErrorMessage - which
 *    sigint(100)-aborts - and the vendor then falls through to
 *    fprintf(dump,...) on the NULL FILE (UB). Kotlin renders the
 *    same control flow: ErrorMessage throws inside the stub
 *    contract, and the unexecutable tail is quantized with the
 *    Nothing-returning error carrier so the local smart-casts
 *    non-null (u128 UB-quantization precedent).
 *  - p1->data is dereferenced without a NULL test (vendor
 *    (*p1->data).c_str()); the data==NULL deref UB is quantized
 *    to a deterministic IllegalStateException at the same seat.
 *  - p1->name is likewise printed without a NULL test
 *    (fprintf("%s", p1->name)); the same quantization applies at
 *    the name seat.
 *  - char key[1024] and struct stab *p2, *p3 are declared and
 *    never used (h013 dead declarations, recorded here).
 *  - The dump list is the CURRENT frame only (start[_Sym] head),
 *    not a full table walk - kept verbatim.
 */

fun SysDump(pid: Long, svPtr: Msv) {
    val p = CharArray(32)
    Mltoa(pid, p) // h014 stub (batch-10 owner)
    val dump: PrintWriter? = try {
        PrintWriter(File(cstringAt(p))) // fopen(p, "w")
    } catch (e: java.io.IOException) {
        null
    }
    if (dump == null) {
        ErrorMessage("Internal sym dum failed".toCharArray(), svPtr.LineNumber)
        error("unreachable in the vendor: ErrorMessage sigint(100)-aborted; the tail fprintf(NULL) UB is quantized")
    }

    var p1: Stab? = svPtr.start[svPtr._Sym]

    while (p1 != null) {
        val name = p1.name ?: error("vendor UB quantized: fprintf(p1->name) with name==NULL")
        dump.print(cstringAt(name))
        dump.print("\n")
        val data = p1.data ?: error("vendor UB quantized: (*p1->data).c_str() with data==NULL")
        dump.print(data)
        dump.print("\n")
        p1 = p1.next
    }
    dump.close()
}
