package both

// Unit 203 render: interp-sysfunc.cpp::GlobalData (roster 203,
// batch 7, 43 LOC; units.csv rank 137; vendor body :733-776).
// The \u0024DATA engine: the RETRIEVE data probe plus the
// SQLITE-arm descendant probe (endcap pair + NEXT + prefix
// compare), writing the two-character code into tmp2
// ("0" nothing, "1" data only, "10" descendant only,
// "11" both).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: Mglobal (RETRIEVE/NEXT)
//     called direct (the BothCRuntime seat); writeC/strlenC/
//     cStringOf/strncmpC direct. The globalDataC TODO stub
//     (BothStubs) RELEASED at this render (release note there;
//     its fold is the global::Data() member engine: GlobalData
//     then atol - the Dice/Sim1/Multiply existence gates ride
//     it). NOTE the stub pin cited "roster 76/78/79" - those
//     are RANKS of the libmpscpp.cpp::GlobalData Interpret
//     wrappers (units 86/237/208, batches 3/8/7 - 208 is a
//     LATER unit in THIS batch, a different vendor family);
//     the engine row is THIS unit 203.
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: GlobalData keeps vendor name/case.
//   h020 pointer_handle - reused: the endcap append lands on
//     the CALLER's tmp0 (the vendor restores tmp0 from tmp3
//     and then re-appends; the caller-visible endcap is kept).
//
// Quirks kept (ledgered):
//   - SQLITE is defined (the arm's SQLITE routing): the
//     non-SQLITE single-endcap arm is compile-time dead and
//     NOT rendered (noted here only).
//   - the descendant probe REWRITES tmp0 (the NEXT walk mutates
//     the key buffer); the prefix compare reads the REWRITTEN
//     tmp0 against the saved tmp3 over the original _i bytes -
//     kept exact.
//   - the redundant second NUL writes in the vendor case table
//     ("10" and "11" arms write tmp2[2]=0 after tmp2[1]=0)
//     fold to the single writeC terminator.
//   - "10" is the descendant-only code (MUMPS \u0024DATA
//     semantics; Data() atol-folds it truthy).

// defines.h:85 (NEXT 2; the Mglobal descendant-probe op; the
// XNEXT=8 order-walk sibling is seated in BothCRuntime)
internal const val NEXT = 2

// void GlobalData(unsigned char *tmp0, unsigned char *tmp2,
//                 struct MSV *svPtr) - interp-sysfunc.cpp:733
internal fun GlobalData(tmp0: CharArray, tmp2: CharArray, svPtr: Msv) {
    val tmp1 = CharArray(STR_MAX)
    val tmp3 = CharArray(STR_MAX)

    writeC(tmp3, cStringOf(tmp0)) // strcpy(tmp3,tmp0)

    val i = strlenC(tmp0)

    val f = Mglobal(RETRIEVE, tmp0, tmp1, svPtr)

    writeC(tmp0, cStringOf(tmp3)) // strcpy(tmp0,tmp3) - restore

    // SQLITE arm: endcap pair + NEXT probe (descendant?)
    tmp0[i] = '\u0001'
    if (i + 1 < tmp0.size) tmp0[i + 1] = '\u0001'
    if (i + 2 < tmp0.size) tmp0[i + 2] = '\u0000'
    var f1 = Mglobal(NEXT, tmp0, tmp1, svPtr)
    // the NEXT walk rewrote tmp0; the prefix compare reads the
    // rewritten key against the saved original
    f1 = if (f1 == 1 && strncmpC(cStringOf(tmp0), cStringOf(tmp3), i) == 0) 1 else 0

    when {
        f == 0 && f1 == 0 -> { // no data; no descendant
            writeC(tmp2, "0")
        }
        f == 0 && f1 == 1 -> { // no data; has descendant
            writeC(tmp2, "10")
        }
        f == 1 && f1 == 0 -> { // has data; no descendant
            writeC(tmp2, "1")
        }
        else -> { // has data; has descendant
            writeC(tmp2, "11")
        }
    }
}
