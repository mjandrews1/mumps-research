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
