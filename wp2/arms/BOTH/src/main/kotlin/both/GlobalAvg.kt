package both

// Unit 263 (BOTH B9, core): libmpscpp.cpp::global::Avg (28 LOC).
// Batch_plan ordinal 263; units.csv row 261. The global-class
// member \\u0024avg kernel (the B5 unit-130 Avg is the free
// (char*) wrapper face that seats this member on the node
// proxy; that wrapper's BothStubs GlobalNode.avg() TODO seat
// releases here - the h014 delegate-when-owner-lands flip, the
// call sites in Avg.kt bind unchanged). Walk the global's
// sibling chain by XNEXT while the key prefix matches, parse
// each value as a double, and return the arithmetic mean.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: ttt/tmp/tmp1/rslt are the
//     CharArray(STR_MAX) working buffers (copy-in/cStringOf
//     folds); the double return is the Kotlin Double face.
//   h008 static_scratch - reused: no static scratch in this
//     body (locals only), the carriers allocate per call.
//   h010 numeric_promotion - reused: tot accumulates double,
//     j counts long; tot/j is the C double/long promotion -
//     Kotlin Double division, same semantics.
//   h014 out_of_scope_call - reused: Mglobal XNEXT, the
//     GlobalExceptionCode/MumpsGlobalException fail-throw
//     idiom, and the file-scope svPtr singleton ride their
//     seats (BothCRuntime, LibmpscppMisc7); get_global rides
//     the libmpscpp.h:117-118 MACRO-fold seat seated real at
//     the B8 unit-217 close (LibmpscppMisc7.kt), with tpx the
//     XNEXT success flag on the Msv carrier.
//   h017 naming - reused: the member face rides the GlobalRef
//     receiver as globalAvg (the reverseG precedent);
//     ttt/tmp/tmp1/rslt/tot/val/j/i keep vendor names.
//   MISS class_member_carrier - reused (the receiver-state
//     pattern; class layout owner roster 149 pending).
//   Dafny: exempt (Mglobal walk side effect; the sscanf
//     double-parse face is the family exemption).
//
// Vendor quirks kept (ledgered):
//   - the trailing control-byte strip reads tmp[i-1] AND
//     tmp[i-2] BEFORE the i>0 guard: an empty ref (i==0)
//     reads tmp[-1]/tmp[-2] in the vendor (UB); guarded here
//     (i >= 2 required), divergence noted - the globalRef
//     contract makes i==0 unreachable in practice (a global
//     reference is never empty at this seat).
//   - strncmp(ref,tmp,i) compares only the ORIGINAL prefix
//     length i (the strip may have shortened tmp first); kept
//     verbatim.
//   - the while(1) walk breaks on XNEXT==0 (end of chain)
//     or the prefix mismatch; both arms return through the
//     j==0 -> 0.0 short-circuit.

internal fun GlobalRef.globalAvg(): Double {
    val ttt = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)
    var j = 0L
    var tot = 0.0

    // strcpy(tmp, ref); i = strlen(ref);
    for (idx in ref.indices) tmp[idx] = ref[idx]
    tmp[ref.length] = 0.toChar()
    var i = ref.length

    // if (tmp[i-1] < 32 && tmp[i-2] < 32) tmp[--i] = 0;
    if (i >= 2 && tmp[i - 1].code < 32 && tmp[i - 2].code < 32) {
        i--
        tmp[i] = 0.toChar()
    }

    // while (1) { ... }
    while (true) {
        if (Mglobal(XNEXT, tmp, ttt, svPtr) == 0) break

        // if (strncmp(ref, tmp, i) != 0) break;
        var eq = true
        for (k in 0 until i) {
            if (k >= ref.length || ref[k] != tmp[k]) { eq = false; break }
        }
        if (!eq) break

        // strcpy(tmp1, tmp); get_global(tmp1, rslt);
        //   (get_global is the libmpscpp.h:118 MACRO fold:
        //   tpx = Mglobal(RETRIEVE, key, rslt, svPtr))
        System.arraycopy(tmp, 0, tmp1, 0, tmp.size)
        svPtr.tpx = Mglobal(RETRIEVE, tmp1, rslt, svPtr).toLong()

        if (svPtr.tpx == 0L) {
            GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        // if (sscanf(rslt, "%lf", &val) == 0) { INVALID_CONVERSION; throw; }
        //   (sscanf returns 0 only when no numeric prefix converts)
        val rs = cStringOf(rslt)
        if (!REGEX_AVG_NUM.containsMatchIn(rs)) {
            GlobalExceptionCode = INVALID_CONVERSION
            throw MumpsGlobalException()
        }
        val vald = strtodC(rs)

        tot += vald
        j++
    }

    // if (j == 0) return 0.0; return tot / j;
    if (j == 0L) return 0.0
    return tot / j
}

// the sscanf %lf convertibility test (a leading numeric prefix;
// the strtodC seat's shape, local to keep the seat file-private)
private val REGEX_AVG_NUM =
    Regex("[+-]?(\\d+\\.?\\d*|\\.\\d+)([eE][+-]?\\d+)?")
