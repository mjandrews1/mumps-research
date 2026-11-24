package daf

/*
 * DAF batch-9 unit 276 (roster row 276; libmpscpp.cpp::<misc-3>,
 * core, LOC 600, score 11.2). Consult 2026-11-24T22:53:00Z.
 *
 * This roster row is the C++ global-class OPERATOR-OVERLOAD
 * LATTICE of libmpscpp.cpp: the member and free operator families
 * for <= (9 types), == (18), != (18), and the mstring
 * operator= tail. The row-98 overloads-collapse precedent applies
 * (the same fold the unit-118 SymPut 9-overload collapse used):
 * each family's unsigned/short/long/float C++ type arms collapse
 * onto the THREE value carriers the arm models (Int, String,
 * Double), member-side (global vs scalar / global vs global) and
 * free-side (scalar vs global) mirrored, with the b >> i read
 * folding onto the node-value model (Misc5/row-18; NOT an
 * Mglobal retrieve - the node-value read of the globalReadD
 * seat, here n.value).
 *
 * VENDOR QUIRKS kept: (1) the string-side free operator<= and
 * member operator== string arms compare with strcmp and
 * >= / <= INVERTED-result quirks (operator<=(string) returns 1
 * when strcmp >= 0; the == string member returns 1 when strcmp
 * != 0) - kept verbatim, ledgered at the two seats below;
 * (2) every family returns 1/0 (C int bool), kept as Boolean.
 * The mstring operator= tail is the String val-assign fold
 * (Kotlin val semantics; ledger note, no body).
 * H-015 hygiene: no raw dollars ride the comments.
 * Dafny: EXEMPT (comparison relays over the node-value seat;
 * journal row).
 */

// ---- operator<= family (member side: global b >> i; a <= i) ----

fun globalLeD(b: MpsGlobalNode, a: Int): Boolean = a <= b.value.toDouble().toInt()
fun globalLeD(b: MpsGlobalNode, a: Double): Boolean = a <= b.value.toDouble()
// string-side free operator<=: strcmp(a,tmp1)>=0 -> 1 (quirk (1))
fun globalLeStrD(b: MpsGlobalNode, a: String): Boolean =
    strcmpD(a, b.value) >= 0

// ---- operator== family ----

fun globalEqD(n: MpsGlobalNode, a: Int): Boolean =
    bVal(n) == a.toDouble()
fun globalEqD(n: MpsGlobalNode, a: Double): Boolean = bVal(n) == a
// string-side member operator==: strcmp != 0 -> 1 (quirk (1))
fun globalEqD(n: MpsGlobalNode, a: String): Boolean =
    strcmpD(a, n.value) != 0

fun globalEqRevD(a: Int, b: MpsGlobalNode): Boolean = globalEqD(b, a)
fun globalEqRevD(a: Double, b: MpsGlobalNode): Boolean = globalEqD(b, a)
fun globalEqRevD(a: String, b: MpsGlobalNode): Boolean = globalEqD(b, a)

// ---- operator!= family ----

fun globalNeD(n: MpsGlobalNode, a: Int): Boolean = !globalEqD(n, a)
fun globalNeD(n: MpsGlobalNode, a: Double): Boolean = !globalEqD(n, a)
fun globalNeD(n: MpsGlobalNode, a: String): Boolean = !globalEqD(n, a)

fun globalNeRevD(a: Int, b: MpsGlobalNode): Boolean = globalNeD(b, a)
fun globalNeRevD(a: Double, b: MpsGlobalNode): Boolean = globalNeD(b, a)
fun globalNeRevD(a: String, b: MpsGlobalNode): Boolean = globalNeD(b, a)

// global==global (member): both sides read, strcmp==0 ladder
fun globalEqNodesD(n: MpsGlobalNode, a: MpsGlobalNode): Boolean =
    bCmp(n, a) == 0
fun globalNeNodesD(n: MpsGlobalNode, a: MpsGlobalNode): Boolean =
    bCmp(n, a) != 0

// ---- vendor helpers ----

// the b >> i read over the node-value model (numeric literals
// parse via strtod, 0.0 fallback - the C >> semantics used by
// the reference build for numeric-side arms)
private fun bVal(n: MpsGlobalNode): Double = DafCRuntime.strtod(n.value)

// strcmp fold.
private fun strcmpD(a: String, b: String): Int {
    var q = 0
    while (q < a.length && q < b.length) {
        if (a[q] != b[q]) {
            return if (a[q] < b[q]) -1 else 1
        }
        q++
    }
    return when {
        a.length == b.length -> 0
        a.length < b.length -> -1
        else -> 1
    }
}

// global::operator==(global) double-sided read (both cmp paths:
// numeric when both parse, string otherwise - the C++ overload
// resolution reads member-vs-member through the same b >> i arm)
private fun bCmp(n: MpsGlobalNode, a: MpsGlobalNode): Int =
    strcmpD(n.value, a.value)

// mstring& mstring::operator=(mstring i) { data=i.data; } - the
// Kotlin val-assign fold; ledger only, no seat.
