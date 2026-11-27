package both

// Unit 269 (BOTH B9, core): libmpscpp.cpp::Jaccard (58 LOC).
// Batch_plan ordinal 269: core, LOC 58. Vendor body the Jaccard
// coefficient sibling of Dice (unit 175): the A-side walk sums
// totD and the intersection tot (val*val1) through the parallel
// B key; the B-side walk then sums totQ over B's own children;
// the result is tot/(totD+totQ-tot) with the 0 denominator
// short-circuit.
//
// Vendor behavior: A.ref copies to tmp (i its length); B.ref
// copies to ctmp (j its length); a trailing 0x01 endcap pair is
// stripped from BOTH buffers (the ctmp strip rewrites the
// buffer: j-- then ctmp[j]=0; the tmp strip the same shape on
// i); OriginalB saves the stripped ctmp; loop 1 XNEXTs tmp,
// keeps keys matching A.ref over the first i bytes, retrieves
// the A value (get_global macro, tpx fail-throw), sums totD,
// rebuilds the B key as ctmp plus tmp's suffix past i (the
// strcpy(B.ref,ctmp); strcat(B.ref,&tmp[i]) receiver mutation),
// gates on B.Data() (continue on miss), retrieves the B value
// and sums tot += val*val1; the vendor then restores B.ref from
// OriginalB; loop 2 XNEXTs ctmp, keeps keys matching B.ref over
// the first j bytes, retrieves each value into totQ; the tail
// is val=totD+totQ-tot, 0.0 short-circuit, tot/val.
//
// Quirks kept (ledgered):
//   - the 0x01-pair strips read index -2/-1 with no length
//     guard (C OOB for refs shorter than 2); quantized with the
//     length>=2 guard (the unit-130/136/171 precedent).
//   - B.ref is MUTATED by the vendor in loop 1 (receiver-visible
//     side effect) and restored before loop 2; the Kotlin
//     GlobalRef.ref is a val, so the mutation and restore land
//     on locals. ARM-LOCAL DIVERGENCE: the caller's B receiver
//     never observes the rewrite, journaled (the Dice carrier
//     decision, same shape).
//   - the loop-1 continue on a Data() miss skips the totQ
//     side entirely (kept verbatim; Jaccard's totQ lives in
//     loop 2, unlike Dice's single-loop Q side).
//   - get_global is the libmpscpp.h:117 MACRO, folded as
//     getGlobalC (the unit-175 seat, tpx store + fail-throw).
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: tmp/ttt/ctmp/rslt ride the
//     CharArray XNEXT scratch carrier (the Dice walk shape);
//     cStringOf folds each read; OriginalB is the String fold.
//   h014 out_of_scope_call - reused: the XNEXT walk and the
//     RETRIEVE/tpx idiom ride the flipped Mglobal seat; B.Data()
//     folds through the globalDataC named-owner stub; strtod
//     folds to strtodC; sigint not reached in this body.
//   h017 naming - reused: Jaccard keeps the vendor name.
//   h019 class_member_carrier - reused: the two-global free
//     member of the global class layout owner (roster 89), the
//     Dice precedent; receivers pass explicitly.

internal fun Jaccard(A: GlobalRef, B: GlobalRef): Double {
    val ttt = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)

    // the A-side XNEXT-mutated key scratch
    val tmp = CharArray(STR_MAX)
    writeC(tmp, A.ref)
    var i = A.ref.length

    // the B-side XNEXT-mutated key scratch (loop 2 walks it)
    val ctmp = CharArray(STR_MAX)
    writeC(ctmp, B.ref)
    var j = B.ref.length

    // trailing 0x01-pair strips (buffer rewrites, guarded)
    if (j >= 2 && ctmp[j - 2] == '\u0001' && ctmp[j - 1] == '\u0001') {
        j--
        ctmp[j] = '\u0000'
    }
    val originalB = cStringOf(ctmp)

    if (i >= 2 && tmp[i - 2] == '\u0001' && tmp[i - 1] == '\u0001') {
        i--
        tmp[i] = '\u0000'
    }

    var totD = 0.0
    var totQ = 0.0
    var tot = 0.0

    // loop 1: A's children; totD and the intersection tot
    while (true) {
        if (Mglobal(XNEXT, tmp, ttt, svPtr) == 0) break

        val key = cStringOf(tmp)
        if (strncmpC(A.ref, key, i) != 0) break

        val f = getGlobalC(key, rslt, svPtr)
        if (f == 0) {
            GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }
        val v = strtodC(cStringOf(rslt))
        totD += v

        // strcpy(B.ref,ctmp); strcat(B.ref,&tmp[i]) on the local
        // fold (val receiver divergence, see KDoc)
        val bref = StringBuilder(cStringOf(ctmp))
        if (key.length > i) bref.append(key.substring(i))
        val btmp = bref.toString()

        if (globalDataC(btmp, svPtr) == 0) continue

        val g = getGlobalC(btmp, rslt, svPtr)
        if (g == 0) {
            GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }
        val v1 = strtodC(cStringOf(rslt))
        tot += v * v1
    }

    // strcpy(B.ref,OriginalB): the restore folds onto originalB
    // loop 2: B's children; totQ
    while (true) {
        if (Mglobal(XNEXT, ctmp, ttt, svPtr) == 0) break

        val ckey = cStringOf(ctmp)
        if (strncmpC(originalB, ckey, j) != 0) break

        val f = getGlobalC(ckey, rslt, svPtr)
        if (f == 0) {
            GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }
        totQ += strtodC(cStringOf(rslt))
    }

    val v = totD + totQ - tot
    if (v == 0.0) return 0.0
    return tot / v
}
