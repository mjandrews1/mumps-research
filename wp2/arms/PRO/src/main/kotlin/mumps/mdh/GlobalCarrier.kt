package mumps.mdh

/**
 * PRO-arm minimal carrier for the vendor C++ class global
 * (libmpscpp.cpp machinery). Not itself a corpus unit; members are
 * added per translated unit as needed (one-owner policy, cf.
 * rules_v0.pl h004). The subscript/Order/Data/assignment machinery is
 * out of scope for the B1 corpus (B2 units 32/33/38/39/44/49/53/60
 * own other global methods), so the members touched by unit 14 are
 * owner-named h014 stubs; delegate when an owning unit lands.
 */
class Global(val ref: String) {

    /** Vendor global::Kill - kill entire global (owner: libmpscpp.cpp). */
    fun Kill() {
        TODO("h014 stub: owner libmpscpp.cpp class global::Kill (not a B1 corpus unit)")
    }

    /** Vendor global::operator() - subscript (0, 1 or 2 subscripts here). */
    operator fun invoke(vararg subs: String): GlobalRef =
        GlobalRef(this, subs.toList())
}

/**
 * A subscripted global reference (vendor's operator() result). The
 * Order/Data/read/assign members touched by unit 14 are owner-named
 * h014 stubs (owner: libmpscpp.cpp class global machinery, not in the
 * B1 corpus).
 */
class GlobalRef internal constructor(val g: Global, val subs: List<String>) {

    /** Vendor B().Kill() - kill on the unsubscripted reference. */
    fun Kill() = g.Kill()

    /** Vendor global::Order(dir). */
    fun Order(dir: Int): String {
        TODO("h014 stub: owner libmpscpp.cpp class global::Order (not a B1 corpus unit)")
    }

    /** Vendor global::Data(). */
    fun Data(): Double {
        TODO("h014 stub: owner libmpscpp.cpp class global::Data (not a B1 corpus unit)")
    }

    /** Vendor operator double (implicit numeric read: B(t1,t2)+1). */
    fun toDouble(): Double {
        TODO("h014 stub: owner libmpscpp.cpp class global operator double (not a B1 corpus unit)")
    }

    /** Vendor operator=(double) (B(t1,t2)=1). */
    fun assign(v: Double) {
        TODO("h014 stub: owner libmpscpp.cpp class global operator=(double) (not a B1 corpus unit)")
    }
}
