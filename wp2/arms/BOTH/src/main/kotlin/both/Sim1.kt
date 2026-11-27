package both

// Unit 198 render: libmpscpp.cpp::Sim1 (roster 198, batch 7,
// 40 LOC per units.csv; vendor body :1565-1618, the kernel).
// THE Sim1W DUAL-SEAT ADJUDICATION (PI ruling 2026-11-26,
// deferred to this render: THE NEW RENDER OWNS THE SEAT, the
// earlier kernel folds in):
//   - the corpus seat is the KERNEL double Sim1(global,global)
//     at :1565 (units.csv has exactly ONE Sim1 row: unit 198,
//     rank 187; there is no roster unit 208 for Sim1 - the
//     unit-186 InterpService stub pin "roster 208" was the
//     vendor interp-service.cpp:208 FACE-ARM LINE NUMBER, not
//     a roster unit; corrected in the release note there);
//   - the FACE char *Sim1(struct MSV*,char*,char*,char*) at
//     :10431 is a NON-UNIT service wrapper (the ErrorMessage
//     non-unit precedent: render, not stub); it is rendered
//     HERE as sim1FaceZb and the unit-186 sim1SvcZb TODO stub
//     RELEASED (its call site now calls the face); the two
//     Sim1 ledgers stay distinct: kernel = corpus unit 198,
//     face = non-unit wrapper owned by this render;
//   - the face's global::operator()(string) at :2751 rides
//     MakeGlobal(0,myname,"a",sub) - roster 202, THIS batch -
//     so a makeGlobalZb TODO stub is raised here (h014) and
//     releases at the 202 render.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h007 char_buffer - reused: the XNEXT walk rides the
//     CharArray scratch carrier (the unit-136/171/Dice shape).
//   h014 out_of_scope_call - reused: Mglobal XNEXT + getGlobalC
//     (RETRIEVE/tpx idiom, unit-175 Dice seats) called direct;
//     globalDataC named-owner stub direct (B.Data() ->
//     GlobalData, the Dice fold); strtodC direct; fmtG direct
//     (BothNumRuntime %g seat, the mcvt precedent); NEW TODO
//     stub makeGlobalZb (owner libmpscpp.cpp::global::MakeGlobal,
//     roster 202, this batch).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: Sim1 keeps the vendor name (the
//     kernel; no collision - the released stub was sim1SvcZb);
//     the face carries sim1FaceZb.
//   h019 class_member_carrier - reused: the kernel is a
//     two-global free member (global, global) of the global
//     class layout owner (roster 89), receivers explicit -
//     the Dice precedent.
//   h020 pointer_handle - reused: the arg 0x01-separator
//     splits seat through substring pairs; the face's static
//     tmp[32] folds to the fmtG value return.
//
// Quirks kept (ledgered):
//   - the 0x01-pair strips read ctmp[j-2]/ctmp[j-1] and
//     tmp[i-2]/tmp[i-1] with no j/i>=2 guard; quantized with
//     the length>=2 guard (the Dice precedent).
//   - B.ref is MUTATED in the loop by the vendor
//     (strcpy(B.ref,ctmp); strcat(B.ref,&tmp[i])); the Kotlin
//     GlobalRef.ref is a val, so the mutation lands on the
//     local btmp copy. ARM-LOCAL DIVERGENCE (the Dice
//     precedent): the caller's B receiver does not observe the
//     rewritten reference.
//   - kernel tpx checks on BOTH retrieves throw
//     GLOBAL_NOT_FOUND (kept exact; the face does not check).
//   - the face truncates arg1/arg2 AT the first 0x01
//     (arg1[i]=0) and subscript-pastes &arg[i+1]; the
//     name/subscript split is byte-exact (bypass ^: &arg[1]).
//   - the face sprintf("%g") rides fmtG (cFormatG 6-digit
//     family, the mcvt precedent).

// global & global::operator()(string a) - libmpscpp.cpp:2751:
// MakeGlobal(0, myname, "a", a.c_str()). RELEASED at the
// roster-202 render (MakeGlobal.kt); the makeGlobalZb TODO stub
// was removed and the face call sites below call MakeGlobal
// direct.

// double Sim1(global A, global B) - libmpscpp.cpp:1565 (the
// kernel; the corpus unit-198 seat)
internal fun Sim1(A: GlobalRef, B: GlobalRef): Double {
    val ttt = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)

    // the XNEXT-mutated key scratch (h007; the Dice walk shape)
    val tmp = CharArray(STR_MAX)
    writeC(tmp, A.ref)
    var i = A.ref.length
    var j = B.ref.length
    val ctmp = B.ref

    // trailing 0x01-pair strips (guard quantized, Dice precedent):
    // ctmp's pair is length-only locally; tmp's pair rewrites the
    // buffer endcap
    if (j >= 2 && ctmp[j - 2] == '\u0001' && ctmp[j - 1] == '\u0001') {
        j--
    }
    if (i >= 2 && tmp[i - 2] == '\u0001' && tmp[i - 1] == '\u0001') {
        i--
        tmp[i] = '\u0000'
    }

    var tot = 0.0

    while (true) {
        // XNEXT walk over the tmp scratch (key rewritten in place)
        if (Mglobal(XNEXT, tmp, ttt, svPtr) == 0) break

        val key = cStringOf(tmp) // the rewritten key

        if (strncmpC(A.ref, key, i) != 0) break

        // strcpy(B.ref,ctmp); strcat(B.ref,&tmp[i]) on the local
        // copy (val receiver divergence, Dice precedent)
        val bref = StringBuilder(ctmp)
        if (key.length > i) bref.append(key.substring(i))
        val btmp = bref.toString()

        // if (!B.Data()) continue;  the globalDataC named-owner
        // stub fold of the Data() member (the Dice fold)
        if (globalDataC(btmp, svPtr) == 0) continue

        // strcpy(tmp1,tmp); get_global(tmp1,rslt); if (!tpx) throw
        val f = getGlobalC(key, rslt, svPtr)
        if (f == 0) {
            GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }
        val v = strtodC(cStringOf(rslt))

        // get_global(btmp,rslt); if (!tpx) throw
        val g = getGlobalC(btmp, rslt, svPtr)
        if (g == 0) {
            GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val v1 = strtodC(cStringOf(rslt))
        tot += v * v1
    }

    return tot
}

// char * Sim1(struct MSV *svptr, char *ep, char *arg1,
//             char *arg2) - libmpscpp.cpp:10431 (the service
// FACE; non-unit wrapper, rendered here - the Sim1W dual-seat
// adjudication, see file header)
internal fun sim1FaceZb(sv: Msv, ep: String, arg1: String, arg2: String): String {
    // for (i=0; arg1[i]!=0; i++) if (arg1[i]==1) break; arg1[i]=0;
    var i = 0
    while (i < arg1.length && arg1[i] != '\u0000') {
        if (arg1[i] == '\u0001') break
        i++
    }
    val nameA = arg1.substring(1, i) // global a(&arg1[1]) - bypass ^

    var j = 0
    while (j < arg2.length && arg2[j] != '\u0000') {
        if (arg2[j] == '\u0001') break
        j++
    }
    val nameB = arg2.substring(1, j) // global b(&arg2[1]) - bypass ^

    // x = Sim1(a(&arg1[i+1]), b(&arg2[j+1]));  the operator()
    // MakeGlobal folds (roster-202 render)
    val a = MakeGlobal(0, nameA, "a", listOf(arg1.substring(i + 1)))
    val b = MakeGlobal(0, nameB, "a", listOf(arg2.substring(j + 1)))

    val x = Sim1(a, b)

    return fmtG(x) // sprintf(tmp,"%g",x); return tmp;
}
