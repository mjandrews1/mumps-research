package both

// Unit 175 (BOTH B6 T2, core): libmpscpp.cpp::Dice (44 LOC).
// Batch_plan ordinal 175: core, LOC 44, score 9.1, complexity
// rank 194. Vendor body libmpscpp.cpp:1312-1356. The Dice
// coefficient over two globals: walk A's children, retrieve
// each A value into the D-side sum, build the parallel B key
// (B.ref plus each child's suffix), run B's Data() existence
// check, retrieve the B value into the Q-side sum, and return
// 2*tot/(totD+totQ).
//
// Vendor behavior: A.ref is copied to tmp with i its length;
// B.ref is copied to ctmp with j its length; a trailing pair of
// 0x01 bytes (the coded-subscript endcap pair) is stripped from
// either buffer (the pre-decrement seats, unguarded); the XNEXT
// walk mutates tmp forward; each key still matching A.ref over
// the FIRST i bytes stays in the family; get_global (the
// libmpscpp.h:117 MACRO: svPtr->tpx=Mglobal(RETRIEVE,...)) yiel-
// ds the A value into rslt and leaves the result in tpx (a zero
// sets GlobalExceptionCode=GLOBAL_NOT_FOUND and throws); val
// sums into totD; the parallel B key is rebuilt as ctmp plus
// tmp's suffix past i (strcpy(B.ref,ctmp); strcat(B.ref,&tmp[i])
// MUTATES the B receiver reference); B.Data() (globalClass.h
// member, body libmpscpp.cpp:3669: GlobalData(ref,tmp,svPtr) then
// atol) gates the Q side; the B value sums into totQ; tot sums
// val*val1; the result is 2*tot/(totD+totQ) with a 0 denominator
// short-circuit to 0.0.
//
// Quirks kept (ledgered):
//   - the 0x01-pair strip reads ctmp[j-2]/ctmp[j-1] and
//     tmp[i-2]/tmp[i-1] with no j/i>=2 guard (the C index walks
//     out of bounds for a ref shorter than 2 bytes); quantized
//     with the length>=2 guard (the unit-130/136/171 precedent).
//   - B.ref is MUTATED in the loop by the vendor (caller-visible
//     side effect); the Kotlin GlobalRef.ref is a val, so the
//     mutation lands on a local var copy. ARM-LOCAL DIVERGENCE:
//     the caller's B receiver does not observe the rewritten
//     reference, journaled as the carrier decision.
//   - get_global is a MACRO (libmpscpp.h:117), folded as
//     getGlobalC with the tpx store + the fail-throw idiom.
//   - a key with data missing on the B side is skipped entirely
//     (continue BEFORE the Q retrieve once Data() fails), kept
//     verbatim; the A side throws on a missing value.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused and hit: tmp/ttt/ctmp/tmp1/btmp/
//     rslt ride the CharArray XNEXT scratch carrier (the
//     unit-136/171 walk shape); cStringOf folds each to String.
//   h014 out_of_scope_call - reused and hit: the XNEXT walk and
//     the RETRIEVE/tpx idiom ride the B6-T2-flipped Mglobal seat
//     (unit 167); B.Data() folds through the globalDataC
//     named-owner stub (owner libmpscpp.cpp global-class member
//     Data() -> GlobalData, roster rows 76/78/79 batch 3/7/8,
//     untranslated in this arm); strtod folds to strtodC.
//   h019 class_member_carrier - reused and hit: Dice is a
//     two-global free member (global, global) of the global
//     class layout owner (roster 89); the receivers pass
//     explicitly, fold named Dice keeping the vendor name.

// get_global macro fold (libmpscpp.h:117):
// (svPtr->tpx=Mglobal(RETRIEVE,ref,rslt,svPtr),rslt)
internal fun getGlobalC(key: String, rslt: CharArray, sv: Msv): Int {
    val f = Mglobal(RETRIEVE, key.toCharArray(), rslt, sv)
    sv.tpx = f.toLong()
    return f
}

internal fun Dice(A: GlobalRef, B: GlobalRef): Double {
    val ttt = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)

    // the XNEXT-mutated key scratch (h007; the unit-136 walk shape:
    // btreeX rewrites the key buffer in place)
    val tmp = CharArray(STR_MAX)
    writeC(tmp, A.ref)
    var i = A.ref.length
    val j = B.ref.length
    val ctmp = B.ref

    // trailing 0x01-pair strips (guard quantized, see KDoc):
    // ctmp's pair is length-only locally; tmp's pair rewrites the
    // buffer endcap with matchC-like truncation via writeC
    var iLoc = i
    if (i >= 2 && tmp[i - 2] == '\u0001' && tmp[i - 1] == '\u0001') {
        iLoc--
        tmp[iLoc] = '\u0000'
    }
    i = iLoc

    var totD = 0.0
    var totQ = 0.0
    var tot = 0.0

    while (true) {
        // XNEXT walk over the tmp scratch (key rewritten in place)
        if (Mglobal(XNEXT, tmp, ttt, svPtr) == 0) break

        val key = cStringOf(tmp) // the rewritten key

        if (strncmpC(A.ref, key, i) != 0) break

        val f = getGlobalC(key, rslt, svPtr)
        if (f == 0) {
            GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val v = strtodC(cStringOf(rslt))
        totD += v

        // strcpy(B.ref,ctmp); strcat(B.ref,&tmp[i]) on the
        // local copy (val receiver divergence, see KDoc)
        val bref = StringBuilder(ctmp)
        if (key.length > i) bref.append(key.substring(i))
        val btmp = bref.toString()

        // if (!B.Data()) continue;  the globalDataC named-owner
        // stub fold of the Data() member (GlobalData + atol)
        if (globalDataC(btmp, svPtr) == 0) continue

        val g = getGlobalC(btmp, rslt, svPtr)
        if (g == 0) {
            GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val v1 = strtodC(cStringOf(rslt))
        totQ += v1
        tot += v * v1
    }

    val v = totD + totQ
    if (v == 0.0) return 0.0
    return 2 * tot / v
}
