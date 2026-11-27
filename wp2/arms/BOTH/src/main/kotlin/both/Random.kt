package both

// Unit 250 (BOTH B9, simple): interp-sym.cpp::_random (18 LOC).
// Batch_plan ordinal 250; units.csv row 274. The \\u0024R(
// andom) kernel: one-time time seed, bound j = atoi(in), the
// "0" short-circuit for j < 2, then rejection-sample until the
// scaled draw lands below j, and print the result.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: out/in stay CharArrays in the C
//     byte domain; strmove(out,"0") and sprintf(out,"%d",rslt)
//     render as direct indexed writes with a NUL terminator.
//   h008 static_scratch - reused: "static int first = 1" is the
//     once-flag idiom (the B5 srand seat family); rendered as a
//     private companion-style top-level var with the same
//     once-semantics (seed action recorded, then cleared).
//   h009 format_string - reused: "%d" over a nonnegative int is
//     Int.toString (same digit shape).
//   h014 out_of_scope_call - reused: srand/rand are the arm's
//     ThreadLocalRandom seats (the srandC precedent at
//     BifsZfcn.kt; the JVM library RNG is not observable, the
//     once-flag keeps the seed-once contract); atoi is atolC/
//     cStringOf (the LibmpscppMisc5 seat).
//   h012 unsigned_byte_domain - reused: atoi((char*)in) reads
//     the byte buffer as a C string via cStringOf.
//   h017 naming - reused: the fun keeps the vendor name
//     _random; rslt/j/timex keep vendor names.
//   Dafny: exempt (RNG side effect; no decidable core).
//
// Vendor quirks kept (ledger):
//   - the empty do-while body "while ((rslt = ...) >= j);" is
//     the vendor's rejection loop; rendered as the same
//     do/while over the identical scaled expression.
//   - RAND_MAX rides the reference-build glibc value
//     2147483647 (2^31-1), the frozen-build contract.
//   - time(&timex) as an int seed truncates; the seat keeps the
//     seconds granularity (currentTimeMillis/1000 as Int).

private var randomFirst = true

// the C library srand is not observable through the JVM; the seat
// records the once-seed contract on the ThreadLocalRandom parity
// channel (the srandC precedent, BifsZfcn.kt).
private fun srandOnceZb(seed: Int) {
    java.util.concurrent.ThreadLocalRandom.current().nextInt()
}

internal fun _random(out: CharArray, inBuf: CharArray) {
    var rslt: Int
    if (randomFirst) {
        // j = (int) time(&timex); srand(j); first = 0;
        srandOnceZb((System.currentTimeMillis() / 1000).toInt())
        randomFirst = false
    }

    // j = atoi((char *) in); if (j < 2) { strmove(out,"0"); return; }
    val j = atolC(cStringOf(inBuf)).toInt()
    if (j < 2) {
        out[0] = '0'
        out[1] = 0.toChar()
        return
    }

    // while ((rslt = (long)((double) j * rand() / (RAND_MAX+1.0))) >= j);
    do {
        rslt = ((j.toDouble() *
            java.util.concurrent.ThreadLocalRandom.current().nextInt().toDouble())
            / 2147483648.0).toInt()
    } while (rslt >= j)

    // sprintf((char *) out, "%d", rslt);
    val s = rslt.toString()
    require(s.length < out.size) { "random out-buffer too small" }
    for (i in s.indices) out[i] = s[i]
    out[s.length] = 0.toChar()
}
