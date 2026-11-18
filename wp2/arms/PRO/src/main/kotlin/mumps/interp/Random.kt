package mumps.interp

/**
 * PRO-arm translation of interp-sym.cpp::_random (roster row 275,
 * batch 9; units.csv: loc 18, comment_ratio 0.056,
 * pointer_marks 0, control_marks 3, score 9.9, rank 98).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 1387-1407 (the $random() builtin seat documented
 * in ZfcnsH.kt, row 242 batch 8).
 *
 * _random(out, in_): the $random(n) engine - one-time
 * srand(time()) init, j=atoi(in_), j<2 produces "0", else spin
 * the (long)((double)j*rand()/(RAND_MAX+1.0)) draw until it is
 * < j, and sprintf "%d" into out.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 stdlib_function_semantics: srand/rand (one-time
 *    static init) fold onto the persistent RandomState carrier
 *    seeded with System.currentTimeMillis(); the draw formula
 *    is kept value-identical (nextInt(RAND_MAX+1) is the
 *    rand() analog, u178 family note).
 *  - h012 unsigned_byte_domain: out/in_ are NUL-terminated byte
 *    domains; sprintf "%d" rides String.format with an explicit
 *    default locale (C printf locale semantics, byte-identical;
 *    potential-bugs rule satisfied).
 *  - h013 dead_store        : none (the vendor's empty spin body
 *    is a loop, not a store) - the do-while's empty statement is
 *    kept verbatim as a while(true) with a comment.
 *  - h014 out_of_scope_call : atoiC (CstringOps), strmove
 *    (InterpStubs:281), writeCstring (CstringOps).
 *  - h015 grammar_collision : _random keeps the vendor
 *    underscore spelling (leading-underscore name preserved,
 *    Match.kt-family precedent).
 *  - h017 naming           : in_ rides the keyword-fold carrier
 *    (u156 family); out/j/rslt/timex unchanged.
 */
fun _random(out: CharArray, in_: CharArray) {
    var j: Int

    // vendor: static int first=1; if (first) { j=time(&timex);
    //          srand(j); first=0; }
    // one-shot init: RandomState lazy-seeds on first use
    val rnd = RandomState.rnd

    j = atoiC(in_) // vendor: j = atoi((char *) in);

    if (j < 2) {
        strmove(out, 0, "0".toCharArray(), 0) // strmove(out,"0")
        return
    }

    var rslt: Int
    while (true) { // vendor: while ((rslt = ...) >= j);  (empty spin body)
        val r = rnd.nextInt(RAND_MAX + 1) // rand() analog
        rslt = (j.toDouble() * r / (RAND_MAX + 1.0)).toLong().toInt()
        if (rslt < j) break
    }

    // vendor: sprintf((char *) out, "%d", rslt);
    val s = String.format(java.util.Locale.getDefault(), "%d", rslt)
    writeCstring(out, s)
}

/** Vendor static `first`/srand carrier: seeded once, on first call. */
private object RandomState {
    /** vendor: srand(time(&timex)) - one-time, wall-clock seeded. */
    val rnd = java.util.Random(System.currentTimeMillis())
}

/** vendor stdlib.h RAND_MAX (Linux/glibc 2147483647). */
private const val RAND_MAX = 2147483647
