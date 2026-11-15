package mumps.interp

/**
 * PRO-arm translation of interp-native-btree.cpp::btree (pilot B1,
 * unit 4). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h012 unsigned_byte_domain: key[]/bd[] byte domains -> CharArray.
 *  - h014 out_of_scope_call    : btreeX (same file, not a corpus unit)
 *                                -> InterpStubs.kt, owner named.
 *  - h017 naming               : btree keeps its name; wrapper object
 *                                follows the file (NativeBtree).
 *
 * MISSED TRIGGER (logged as record_miss): the vendor btree wrapper has an
 * #ifdef NATIVE_SHARED block that locks the DB after each call. The
 * reference build (vendor build-mumps.script, default configure; no
 * --with-shared) does NOT define NATIVE_SHARED, so the block is compiled
 * out of the reference binary and is omitted here — no rule family
 * (e.g. preprocessor_conditional) existed to guide this decision.
 * If NATIVE_SHARED semantics are ever in scope, the omitted block is:
 * close-on-every-call (btreeX CLOSE), usleep(1), return i; plus a
 * DBSem-triggered close with usleep(0).
 */
object NativeBtree {

    fun btree(g: Int, key: CharArray, bd: CharArray, svPtr: Msv): Int =
        btreeX(g, key, bd, svPtr) // h014 stub
}
