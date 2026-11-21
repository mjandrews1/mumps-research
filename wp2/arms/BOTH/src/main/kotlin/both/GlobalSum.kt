package both

// Unit 136 (BOTH B5 T2, core): libmpscpp.cpp::global::Sum (21 LOC).
// Batch_plan ordinal 136: core, LOC 21, score 11.9. The global-class
// Sum service: walk the subscripted children in collating order
// (Mglobal XNEXT), staying in the receiver's subscript family, and
// total their values as doubles.
//
// Vendor behavior: ref is copied to tmp; a trailing pair of control
// bytes (<32) is stripped (the coded-subscript tail); then the loop
// walks Mglobal(XNEXT, tmp, ttt) rewriting tmp with each next key in
// order, stops when the family prefix no longer matches ref over i
// bytes or the walk returns 0, and accumulates strtod of each
// retrieved value. The get_global(ref,rslt) macro expands to
// svPtr->tpx=Mglobal(RETRIEVE,...), so the !tpx arm is the
// GLOBAL_NOT_FOUND throw idiom shared with the T1 renders.
//
// Quirks kept (ledgered):
//   - the strip reads tmp[i-2] without checking i: single-byte or
//     empty refs deref before the buffer in C (UB). Quantized with
//     an i>=2 guard (T1 unit-130 Avg precedent); the i<2 case keeps
//     the full ref, journaled as the divergence.
//   - ref is read past its length by nothing here otherwise; the
//     family test uses NUL-stop strncmp semantics over the first
//     i bytes (strncmpC seat).
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: the in-place key buffer (XNEXT
//     rewrites it, so it is the mutable-scratch carrier) and the
//     ttt/rslt value scratch ride CharArrays read via cStringOf.
//   h012 unsigned_byte_domain - reused: the trailing control-byte
//     compare stays a numeric <32 test in the unsigned-byte domain.
//   h014 out_of_scope_call - reused: Mglobal rides its roster-167
//     stub; the get_global macro folds to the RETRIEVE call with the
//     libmpscpp.h:117 pin (XNEXT 8 / RETRIEVE 0 from defines.h).
//     strtod rides the strtodC seat (T1, BothNumRuntime).
//   h017 naming - hit: Sum->globalSum per-arm fold, journaled.
//   MISS class_member_carrier: the receiver is a global-class member
//     (layout owner globalClass.h::<misc> roster 89); receiver state
//     passed explicitly (T1 recurrence; T2 +1).
internal fun globalSum(ref: String): Double {
    var tmp = ref
    var i = tmp.length

    // if (tmp[i-1] < 32 && tmp[i-2] < 32) tmp[--i] = 0;
    if (i >= 2 && tmp[i - 1] < ' ' && tmp[i - 2] < ' ') {
        tmp = tmp.substring(0, i - 1)
        i--
    }

    var tot = 0.0
    val key = CharArray(STR_MAX) // mutated in place by XNEXT (h007 scratch)
    val ttt = CharArray(STR_MAX) // value scratch per walk
    writeC(key, tmp)

    while (true) {
        // if (Mglobal(XNEXT,tmp,ttt,svPtr)==0) break;
        if (Mglobal(XNEXT, key, ttt, svPtr) == 0) break

        tmp = cStringOf(key) // the rewritten key (C keeps it in tmp)

        // if (strncmp(ref,tmp,i)!=0) break;
        if (strncmpC(ref, tmp, i) != 0) break

        // strcpy(tmp1,tmp); get_global(tmp1,rslt);
        val rslt = CharArray(STR_MAX)
        val f = Mglobal(RETRIEVE, tmp.toCharArray(), rslt, svPtr)
        svPtr.tpx = f.toLong()

        if (f == 0) {
            GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        // val=strtod(rslt,NULL); tot=tot+val;
        tot += strtodC(cStringOf(rslt))
    }

    return tot
}
