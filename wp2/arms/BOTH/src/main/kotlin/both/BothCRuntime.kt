package both

// BOTH-arm local runtime carriers (arm cold open at batch 5, cycle 1).
// This file carries the minimal C-runtime seats the batch-5 tranche-1
// renders need; every seat names its roster owner and delegates when
// the owner unit lands (h014 action). NO other arm's Kotlin was
// consulted; all values verified against the pristine vendor headers.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: every seat below is a TODO stub
//     naming its units.csv owner (zfcn roster unit 184, fcn roster unit 123,
//     Mglobal roster unit 167, MSV stateVector roster unit 172,
//     GlobalExceptionCode/MumpsGlobalException + RETRIEVE libmpscpp.h
//     residue, roster unit 305). Adapt note: the vendor file-scope svPtr becomes
//     the arm-local singleton below until the MSV owner unit lands.
//   h012 unsigned_byte_domain - reused: byte-domain comparisons stay
//     in the Char(0..255) domain (GLOBAL_NOT_FOUND 2, RETRIEVE 0 are
//     plain Int codes; the coded-argument bytes are compared as
//     chars, see the unit renders).
//   h017 naming - reused: C names kept mechanically (snake->camel
//     only where the vendor name itself changes case).
//
// Constants verified in the vendor tree (2026-11-21):
//   STR_MAX 32000          include/mumpsc/strmax.h:3
//   RETRIEVE 0             include/mumpsc/defines.h.in (live build)
//   GLOBAL_NOT_FOUND 2     include/mumpsc/libmpscpp.h:71

internal const val STR_MAX = 32000 // strmax.h:3; file-private (collision lesson)
internal const val RETRIEVE = 0 // defines.h: RETRIEVE 0
internal const val GLOBAL_NOT_FOUND = 2 // libmpscpp.h:71

// libmpscpp.h:62 extern int GlobalExceptionCode (owner: the
// libmpscpp.h::<misc> residue unit, roster unit 305).
internal var GlobalExceptionCode = 0

// MumpsGlobalException (class declared libmpscpp.h:93 area; the throw
// machinery owner is the libmpscpp.h::<misc> residue unit,
// roster unit 305). TODO-stub: member shape only; behavior fills in when the
// owner lands.
internal class MumpsGlobalException : Exception()

// struct MSV minimal carrier: only the members the batch-5 tranche-1
// units touch (bd, v1d - unsigned char[STR_MAX] each,
// include/mumpsc/stateVector.h:42,44; owner: stateVector.h::<misc>,
// roster unit 172). TODO-stub per h014.
internal class Msv {
    val bd = CharArray(STR_MAX)
    val v1d = CharArray(STR_MAX)
}

internal val svPtr = Msv() // vendor file-scope svPtr (libmpscpp.cpp TU)

// int Mglobal(const int g, unsigned char key[], unsigned char bd[],
//             struct MSV*) - include/mumpsc/global.h:78. Owner:
// interp-globalb.cpp::Mglobal, roster unit 167. TODO-stub (h014):
// signature and Int result contract only; the retrieval body lands
// with its owner.
internal fun Mglobal(op: Int, key: CharArray, out: CharArray, sv: Msv): Int {
    TODO("stub: owner interp-globalb.cpp::Mglobal (roster unit 167)")
}

// void zfcn(unsigned char*, unsigned char*, struct MSV*) -
// include/mumpsc/zfcns.h:48. Owner: interp-zfcns.cpp::zfcn,
// roster unit 184. TODO-stub (h014).
internal fun zfcn(input: CharArray, output: CharArray, sv: Msv) {
    TODO("stub: owner interp-zfcns.cpp::zfcn (roster unit 184)")
}

// void fcn(MSV*) - include/mumpsc/libmpscpp.h:431. Owner:
// interp-fcn.cpp::fcn, roster unit 123. TODO-stub (h014).
internal fun fcn(sv: Msv) {
    TODO("stub: owner interp-fcn.cpp::fcn (roster unit 123)")
}

// Mglobal(RETRIEVE, key, out, svPtr) + the fail-throw idiom shared by
// the batch-5 tranche-1 global reads (vendor: if (f==0) {
// GlobalExceptionCode=GLOBAL_NOT_FOUND; throw MumpsGlobalException(); }).
// Blocked on the Mglobal stub (owner roster unit 167): the TODO
// propagates until the owner lands, which is the h014 stub contract.
internal fun mglobalRetrieve(key: String): String {
    val out = CharArray(STR_MAX)
    val f = Mglobal(RETRIEVE, key.toCharArray(), out, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return cStringOf(out)
}

// C-string read: bytes up to the first NUL.
internal fun cStringOf(buf: CharArray): String {
    val end = buf.indexOf(0.toChar())
    return if (end == -1) String(buf) else String(buf, 0, end)
}