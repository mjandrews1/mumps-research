package doc

// Unit 12 (B1 tranche 3): mumps.cpp::<misc> (batch 1, ledger chunk;
// units.csv row: loc 58). Chunker boundary: this is the leading
// residue of mumps.cpp - GPL banner, SQLITE define, readline/history
// includes (the readline dependency is documented at the include
// ledger only: no readline call site is inside this chunk; the
// stdin-reader divergence is registered in the ARM journal for the
// main() units that own the actual call sites), and the small support
// members pad/SyncClock/set_alarm plus the file-scope ints DBTimePulse
// and DBSem. File-scope state rides one owner object per H-004
// (MumpsRunStatic, named for mumps.cpp runtime). printf maps to
// System.out per H-001. Naming per H-017.

object MumpsRunStatic {
    // mumps.cpp file-scope int DBTimePulse (clock pulse flag)
    var DBTimePulse: Int = 0
    // mumps.cpp file-scope int DBSem (database semaphore flag)
    var DBSem: Int = 0
}

// pad(p, x): space-pad p to width x (char out[512] scratch, strncpy
// into it, then strcat single spaces until strlen(out) reaches x).
// Divergence (documented): the vendor returns the address of its own
// stack-local out[] (a dangling pointer in C semantics); the Kotlin
// render returns the padded content as a String copy - pointer
// identity has no safe analog here. Content semantics are unchanged:
// truncation at 512 bytes and padding only up to x are verbatim.
fun pad(p: CharArray, x: Int): String {
    var out = CstringOps.cstring(p).take(512)
    while (out.length < x) out = out + " "
    return out
}

// SyncClock(): DBSem = 1 (mumps.cpp).
fun SyncClock() {
    MumpsRunStatic.DBSem = 1
}

// set_alarm(seconds) SEAT-MOVED (B9 T3, the xindex tag-correction
// precedent): the former stub here tagged the owner "OS libc
// itimer service", a misconstruction - the corpus DOES define the
// body: mpstime.cpp::<misc> (batch-9 unit 273, roster row 253)
// renders setAlarm in MpstimeMisc.kt (unsigned/inert H-018 fold
// returning 0, per that dump's #ifdef-duel adjudication). This
// file keeps NO second definition; call sites bind the
// MpstimeMisc.kt render.
