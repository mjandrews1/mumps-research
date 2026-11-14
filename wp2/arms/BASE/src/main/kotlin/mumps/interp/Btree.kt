package mumps.interp

/**
 * Reference build configuration flag (C: #ifdef NATIVE_SHARED).
 * The frozen-oracle build (/usr/bin/mumps, sqlite3 backend) does NOT
 * define NATIVE_SHARED; flipping this const reproduces the native-shared
 * source configuration.
 */
const val NATIVE_SHARED = false

/**
 * Kotlin translation of interp-native-btree.cpp::btree (Study Subset
 * unit 4, Batch 1, arm BASE; translated 2026-11-14).
 *
 * Wrapper around btreeX. In the native-shared build this locks the DB
 * after each operation (the if (!0) branch); in the reference build the
 * wrapper is a plain pass-through.
 */
fun btree(g: Int, key: String, bd: StringBuilder, svPtr: MsvState): Int {
    val i = btreeX(g, key, bd, svPtr)

    if (NATIVE_SHARED) {
        if (true) { // C: if (!0) — no slice: lock the DB (always taken)
            btreeX(InterpState.CLOSE, "", StringBuilder(), svPtr)
            Thread.sleep(0, 1000) // C: usleep(1) = 1 microsecond
            return i
        }
        // Unreachable when NATIVE_SHARED (the !0 branch always returns),
        // translated for source fidelity:
        if (InterpState.DBSem != 0) { // lock when alarm triggers
            btreeX(InterpState.CLOSE, "", StringBuilder(), svPtr)
            InterpState.DBSem = 0
            Thread.sleep(0) // C: usleep(0)
        }
    }

    return i
}
