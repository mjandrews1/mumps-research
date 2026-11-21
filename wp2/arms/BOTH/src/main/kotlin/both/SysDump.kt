package both

// Unit 142 (BOTH B5 T2, core): interp-sym.cpp::SysDump (18 LOC).
// Batch_plan ordinal 142: core, LOC 18, score 28.0. The symbol-table
// dump: write the current frame's chain (name line + data line per
// node) into the pid-named output file.
//
// Vendor behavior: Mltoa(pid,p) renders the pid; fopen(p,"w") opens
// the dump and a NULL open calls ErrorMessage("Internal sym dum
// failed", svPtr->LineNumber) (which sigint(100)s in the vendor, so
// the tail past it is unreachable); the walk starts at
// start[_Sym] and follows the stab next chain; fclose closes.
//
// Quirks kept (ledgered):
//   - the vendor derefs p1->name and (*p1->data).c_str() with no
//     NULL tests (UB on malformed nodes): quantized to
//     IllegalStateException at the exact same seats (deterministic;
//     no repair).
//   - h013 dead declarations dropped: char key[1024], struct stab
//     *p2, *p3, int i (unused locals of the vendor body).
//   - fopen failure rides the ErrorMessage path; the vendor's
//     fprintf-into-NULL tail is unreachable, so the Kotlin render
//     treats errorMessage as the abort seat (its sigint stub
//     throws) and smart-casts the writer non-null (no double-bang).
//
// BOTH consults (find_rule before render, events logged):
//   h001 io_stream_param - reused: fopen("w") is the java.io.Writer
//     edge (FileWriter); IOException quantizes to the NULL-open
//     branch; the fprintf shapes are exact ("%s\n" per line).
//   h004 globals_ext - reused: stateVector.h members ride the Msv
//     carrier extension (LineNumber :63, _Sym :65, start[SYM_MAX]
//     :71 / SYM_MAX=200 defines.h:218, pinned this tranche).
//   h013 dead_store - hit: key/p2/p3/i dropped.
//   h014 out_of_scope_call - reused: Mltoa rides its roster-292
//     stub (batch 10); errorMessage + sigint ride the BothCRuntime
//     seats; the stab chain type rides the Stab carrier (layout
//     owner sym.h::<misc>, pending batch-5 complex).
//   h009 format_string - reused: "%s\n" exact.
//   h017 naming - hit: SysDump/p1/cp1 per vendor (cp1 is the
//     h007 String fold of the copy carrier).
//   MISS struct_carrier: the struct stab chain has no base rule
//     (new family; capture candidate at batch close).
internal fun SysDump(pid: Long, sv: Msv) {
    // char key[1024]; struct stab *p2, *p3; int i; - dead (h013).

    val p = CharArray(32) // char p[32]
    Mltoa(pid, p) // stub-blocked until roster unit 292 (batch 10)

    var dump: java.io.Writer? = null
    try {
        dump = java.io.FileWriter(String(p, 0, strlenC(p)))
    } catch (e: java.io.IOException) {
        // fopen failure: NULL-open branch below
    }
    // if (dump==NULL) ErrorMessage(...); - sigint(100) aborts in the
    // vendor, so the walk below is the reached path afterwards; the
    // Kotlin fold returns early on the hypothetical open-failure-
    // survive (fprintf-into-NULL UB quantized, no double-bang).
    if (dump == null) {
        errorMessage("Internal sym dum failed", sv.LineNumber)
        return // unreachable in the vendor (sigint never returns)
    }

    var p1 = sv.start[sv._Sym] // p1 = svPtr->start[svPtr->_Sym];
    while (p1 != null) {
        dump.write(checkNotNull(p1.name) + "\n") // UB-deref quantized
        val cp1 = checkNotNull(p1.data) // (*p1->data).c_str() seat
        dump.write(cp1 + "\n")
        p1 = p1.next // p1 = p1->next;
    }
    dump.close() // fclose(dump);
}
