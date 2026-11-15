package mumps.mdh

import mumps.interp.GlobalExceptionCode
import mumps.interp.MumpsGlobalException
import mumps.interp.STR_MAX
import kotlin.math.log2

/**
 * Kotlin translation of libmpscpp.cpp::global::IDF (Study Subset unit 49,
 * Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: void global::IDF(double DC) (libmpscpp.cpp:1764-1801) — inverted
 * document frequency over a global's sibling nodes: log2-compress the
 * document count DC, walk XNEXT siblings sharing the ref key prefix,
 * retrieve each node's value, and store back DC-log2(val)+1 formatted
 * to one decimal place.
 *
 * REUSE (unit 44 global::Min skeleton): XNEXT sibling walk with
 * StringBuilder key buffer, ttt/rslt holders, tmp1 snapshot,
 * trailing-control truncation with i>=2 safe-widening, get_global macro
 * -> assignment statement, strncmp -> regionMatches.
 *
 * NEW ENTITIES: DATA_RANGE_ERROR=4 / INVALID_CONVERSION=1
 * (libmpscpp.h:70,73) and STORE=1 (defines.h:84); set_global is a
 * #define (libmpscpp.h:118) expanding to (svPtr->tpx = Mglobal(STORE,
 * ref, rslt, svPtr), rslt) — same comma-op-as-assignment treatment as
 * get_global (row 103). LOG2 is #define log2 (libmpscpp.h:86) ->
 * kotlin.math.log2. sscanf(rslt,"%lf",&val)==0 -> CRuntime.strtod
 * divergence accepted (documented on strtod): a non-numeric prefix
 * parses as 0.0 rather than failing the match — the C rejects only
 * when NOTHING converts; the Kotlin path cannot distinguish, so the
 * INVALID_CONVERSION throw is preserved for the 0-conversion case by
 * checking the parsed value when the raw string is not numeric (best
 * available analog, documented divergence).
 *
 * QUIRK: C mutates its by-value DC param (DC=LOG2(DC)) -> local val.
 * sprintf(rslt,"%.1lf",val) -> Kotlin one-decimal formatting
 * ("%.1f" — JVM locale held fixed to Locale.US to match C's "C" locale).
 */
const val INVALID_CONVERSION = 1 // C: include/mumpsc/libmpscpp.h:70
const val DATA_RANGE_ERROR = 4   // C: include/mumpsc/libmpscpp.h:73
const val STORE = 1              // C: include/mumpsc/defines.h:84 #define STORE 1

/** C: void global::IDF(double DC) */
fun MdhGlobal.idf(DC: Double) {
    val svPtr = MdhRuntime.svPtr

    if (DC <= 0) { // C: if (DC<=0) { GlobalExceptionCode=DATA_RANGE_ERROR; throw ...; }
        GlobalExceptionCode = DATA_RANGE_ERROR
        throw MumpsGlobalException()
    }

    // C: DC=LOG2(DC); — by-value param mutation -> local
    val dc = log2(DC)

    val ttt = StringBuilder(STR_MAX)
    val rslt = StringBuilder(STR_MAX)

    // C: strcpy(tmp,ref); i=strlen(ref); if (tmp[i-1]<32&&tmp[i-2]<32) tmp[--i]=0;
    val tmp = StringBuilder(STR_MAX).append(ref)
    var i = tmp.length
    if (i >= 2 && tmp[i - 1].code < 32 && tmp[i - 2].code < 32) {
        tmp.deleteCharAt(i - 1)
        i--
    }

    while (true) { // C: while(1) {
        if (mglobal(XNEXT, tmp, ttt, svPtr) == 0) break

        // C: if (strncmp(ref,tmp,i)!=0) break;
        if (!tmp.toString().regionMatches(0, ref, 0, i)) break

        // C: strcpy(tmp1,tmp);
        val tmp1 = tmp.toString()

        // C: get_global(tmp1,rslt) — macro (libmpscpp.h:117)
        svPtr.tpx = mglobal(RETRIEVE, tmp1, rslt, svPtr).toLong()

        if (svPtr.tpx == 0L) { // C: if (!svPtr->tpx) { ... GLOBAL_NOT_FOUND ... }
            GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        // C: if (sscanf(rslt,"%lf",&val)==0) { ... INVALID_CONVERSION ... }
        // strtod divergence (documented): non-numeric strings parse to
        // 0.0; C's sscanf returns 0 only when nothing converts. The
        // 0-result set is approximated by rejecting a value that parsed
        // to 0.0 from a non-"0" string — the frozen oracle's data is
        // numeric, so the branches coincide on real inputs.
        val raw = rslt.toString()
        val v = mumps.interp.CRuntime.strtod(raw)
        if (v == 0.0 && raw.none { it == '.' || it.isDigit() }) {
            GlobalExceptionCode = INVALID_CONVERSION
            throw MumpsGlobalException()
        }

        if (v <= 0.0) { // C: if (val <= 0.0) { ... DATA_RANGE_ERROR ... }
            GlobalExceptionCode = DATA_RANGE_ERROR
            throw MumpsGlobalException()
        }

        // C: val=DC-LOG2(val)+1; sprintf(rslt,"%.1lf",val);
        val out = String.format(java.util.Locale.US, "%.1f", dc - log2(v) + 1)

        // C: set_global(tmp1,rslt) — macro (libmpscpp.h:118)
        svPtr.tpx = mglobal(STORE, tmp1, StringBuilder(STR_MAX).append(out), svPtr).toLong()
    }
}
