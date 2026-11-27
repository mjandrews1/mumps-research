package both

// Unit 239 render: libmpscpp.cpp::IDF (roster 239, batch 8,
// 15 LOC). char * IDF(struct MSV *svptr, char *ep, char *arg1,
// char *arg2) - the ^IDF wrapper face: rejects a non-top-level
// first argument (an embedded SOH byte), truncates the name at
// the SOH, parses the second argument as a double, and re-invokes
// the global member IDF on the subscript tail.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: sigint(100) direct (the
//     in-band abort path, Sigint.kt); the GlobalNode.IDF face
//     is a NEW TODO stub raised here (owner: the global::IDF
//     member implementation - the InterpService idfSvcZb seat
//     names roster 45 batch 2, another rank-pin of the mislabel
//     family; the member body is not this unit's corpus);
//     GlobalRef/GlobalNode invoke direct.
//   h009 format_string - reused: sscanf(arg2,"%lf",&x) renders as
//     strtodC (the BothNumRuntime seat; the GlobalRef.Double()
//     precedent).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: IDF keeps vendor name/case (no Kotlin
//     collision; the idfSvcZb dispatch seat is distinct).
//   h019 class_member_carrier - reused: global a(&arg1[1]) rides
//     the GlobalRef carrier; a(&arg1[i+1]) rides the GlobalNode
//     invoke operator; svPtr is the BothCRuntime singleton.
//
// Quirks kept (ledgered):
//   - the SOH probe is a byte-1 scan (arg1[i]==1, the C int
//     compare on char - kept as '\u0001').
//   - sigint(100) fires BEFORE the truncation (the vendor
//     continues after the printf+sigint pair only if sigint
//     returns; the abort is the expected path - kept in order).
//   - arg1[i]=0 mutates the caller's buffer in place (the
//     CharArray parameter keeps the inout shape).
//   - the top name and the subscript tail are both read from
//     the mutated buffer (&arg1[1] truncated, &arg1[i+1] past
//     the SOH) - the split is computed before the truncation.

// void global::IDF(double) - owner: libmpscpp.cpp::global::IDF
// (the member face; the InterpService idfSvcZb seat, roster pin
// 45, batch 2 pending). TODO-stub (h014).
internal fun GlobalNode.IDF(x: Double) {
    TODO("stub: owner libmpscpp.cpp::global::IDF member (idfSvcZb seat, roster 45, batch 2)")
}

// char * IDF(struct MSV *svptr, char *ep, char *arg1,
//            char *arg2) - libmpscpp.cpp:7053 region
internal fun IDF(sv: Msv, ep: String, arg1: CharArray, arg2: String): String {
    // for (i=0; arg1[i]!=0; i++) if (arg1[i]==1) break;
    var i = 0
    while (arg1[i] != '\u0000') {
        if (arg1[i] == '\u0001') break
        i++
    }

    if (arg1[i] == '\u0001') {
        print("\n*** Arg 1 must be top level global\n")
        sigint(100)
    }

    // arg1[i]=0; global a(&arg1[1]); - the full tail from [1]
    // is read BEFORE the truncation splits it (quirk above).
    val full = fnBdStrAt1Zb(arg1) // &arg1[1] .. first NUL/SOH run
    arg1[i] = '\u0000'
    val a = GlobalRef(full.substring(0, i - 1))

    // sscanf(arg2,"%lf",&x);
    val x = strtodC(arg2)

    // a(&arg1[i+1]).IDF(x);
    a(full.substring(i)).IDF(x)

    return "" // return (char *) "";
}
