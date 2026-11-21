package doc

import java.util.Locale

// Unit 253 (B9 tranche 1): interp-sym.cpp::_random (batch 9,
// simple stratum; units.csv row 275: loc 18, ptr 0, ctl 3, score
// 5.5, rank 43; physLOC 18 physically verified this session).
// The sym zfcn helper behind the compiler's emitted
// _random((UNS) tmp, (UNS) arg) call (compiler-parse.c:1245
// emission; the unit body renders here, no call-site rebind).
// The once-only static int first / srand(time) seed (H-004-era
// once-only static, the vendor's lazy init) folds to the private
// object below, seeded once from the epoch-second clock
// (System.currentTimeMillis()/1000, the Fcn.kt time intrinsic
// precedent; H-010 hit-adapt: the libc rand() ladder
// (double) j * rand() / (RAND_MAX + 1.0) folds to the
// kotlin.random.Random seedless intrinsic uniform already seeded
// once, nextDouble() the U in [0, 1) with the vendor rejection
// loop kept verbatim as the do/while rslt >= j); atoi is the
// real Find.kt seat (H-014 reuse); strmove(out, "0") rides the
// throwing H-014 stub seat per the Extract.kt precedent (owner
// inline.h:38, not a corpus unit); sprintf %d renders as
// String.format Locale.US per H-009. Vendor quirk kept: the
// rejection while body is empty (the C statement is the scalar
// test itself). Naming kept per the _find/_extract/_justify
// underscore-seat precedent.

private object RandomSeedHolder {
    // static int first + srand(time(&timex) / first = 0 lazy init
    val rnd: kotlin.random.Random = kotlin.random.Random(
            System.currentTimeMillis() / 1000L)
}

fun _random(out: CharArray, in_: CharArray) {
    val j = atoi(CstringOps.cstring(in_))

    if (j < 2) {
        Stubs.strmove(out, "0".toCharArray())
        return
    }

    // while ((rslt = (long) ((double) j * rand() / (RAND_MAX + 1.0))) >= j);
    var rslt: Int
    do {
        rslt = (j.toDouble() *
                RandomSeedHolder.rnd.nextDouble()).toInt()
    } while (rslt >= j)

    // sprintf((char *) out, "%d", rslt)
    CstringOps.strcpyS(out, String.format(Locale.US, "%d", rslt))
    return
}
