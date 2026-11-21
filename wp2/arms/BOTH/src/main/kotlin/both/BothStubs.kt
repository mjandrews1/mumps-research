package both

// BOTH-arm carrier seats for the not-yet-rendered vendor classes touched
// by the batch-5 tranche-1 units. Every seat names its roster owner and
// delegates when the owner unit lands (h014 action). The vendor class
// layouts live in pristine headers only; no other arm's Kotlin is
// consulted.
//
// Consults:
//   h014 out_of_scope_call - reused (TODO seats below name owners:
//     global-class layout include/mumpsc/globalClass.h::<misc> roster
//     89; the global-node Avg() service libmpscpp.cpp::global::Avg
//     roster 301; the mstring-class layout
//     include/mumpsc/mstringClass.h::<misc> roster 149).
//   MISS class_member_carrier (units 129/131/133): units 129 (mstring::
//     Length), 131 (global::Translate) and 133 (global::Length) are
//     member functions of classes whose roster owners are not yet
//     translated in this arm; the base has no rule for representing a
//     member unit of an unrendered class, so the renders pass the
//     receiver state explicitly (data/ref) as package-level functions.
//     Capture candidate at batch close (T1 recurrence 3).
//   h017 naming - reused: vendor member/property names kept (ref,
//     avg -> Avg on the node seat); snake->camel only for multiword
//     identifiers.

// Minimal `global` class carrier (vendor: class global, char
// ref[STR_MAX] inside globalClass.h:536 area; owner roster 89). Only
// the surface the batch-5 tranche-1 units touch: the ref key. The
// constructor shape mirrors `global a(...)` / `global needle` from
// the vendor signatures.
internal class GlobalRef(val ref: String)

// The subscripted global-node proxy (vendor: global::operator()
// result usable as a global). The avg() service member is a TODO seat
// (owner libmpscpp.cpp::global::Avg, roster 301).
internal class GlobalNode {
    fun avg(): Double {
        TODO("stub: owner libmpscpp.cpp::global::Avg (roster 301)")
    }
}

internal operator fun GlobalRef.invoke(sub: String): GlobalNode {
    // vendor appends the coded subscript when invoked; the concrete
    // key construction is only exercised by the avg() owner seat, so
    // it stays with the owner unit when it lands.
    return GlobalNode()
}

// -------------------------------------------------------------------------
// Batch-5 tranche-2 seats (core units 134-143), added 2026-11-21.
//
// T2 consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: the transpose/multiply member
//     seats and the Transpose multiply both name their owners
//     (libmpscpp.cpp::global::Multiply roster 213 batch 2; the
//     global-class Transpose member is not itself a corpus unit - its
//     file-level wrappers are roster 75 batch 4 and roster 136 batch 5,
//     both pending in this arm).
//   MISS struct_carrier (unit 142): the struct stab chain
//     (include/mumpsc/sym.h:43-51) has no base rule for typed heap
//     chains; the Stab carrier below is filed as a new miss family
//     (capture candidate at batch close, T1 recurrence 1).
//   MISS class_member_carrier recurrence: units 137/138 ride the
//     GlobalRef/GlobalNode member seats below; owners not yet in
//     this arm (T1 recurrence grows: see events CSV).
//   h017 naming - reused: stab/next/OrigName fields keep vendor case.

// struct stab - include/mumpsc/sym.h:43-51. Layout owner:
// include/mumpsc/sym.h::<misc>, a batch-5 complex unit pending in
// this arm. Only the fields the batch-5 tranche-2 units touch carry
// TODO bodies; the rest are the vendor declaration order (name,
// next, data, OrigName, size, copy, exec).
internal class Stab {
    var name: String? = null
    var next: Stab? = null
    var data: String? = null
    var OrigName: String? = null
    var size: Int = 0
    var copy: Int = 0
    var exec: Int = 0
}

// global-class member Transpose(b) - vendor call at libmpscpp.cpp
// file Transpose wrapper (unit 137 of this batch); the member body is
// not a corpus unit (layout owner globalClass.h::<misc> roster 89).
internal fun GlobalNode.transpose(other: GlobalNode): Unit {
    TODO("stub: global-class Transpose member (layout owner roster 89; not a corpus unit)")
}

// global-class member Multiply(b,c) - owner: libmpscpp.cpp::global::
// Multiply, roster unit 213, batch 2. TODO-stub (h014); the caller
// passes the three file-level wrappers' carriers.
internal fun GlobalRef.multiply(b: GlobalRef, c: GlobalRef) {
    TODO("stub: owner libmpscpp.cpp::global::Multiply (roster unit 213, batch 2)")
}
