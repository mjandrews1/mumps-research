package mumps.mdh

/**
 * PRO-arm minimal carrier for the vendor C++ class global
 * (libmpscpp.cpp machinery). Not itself a corpus unit; members are
 * added per translated unit as needed (one-owner policy, cf.
 * rules_v0.pl h004). The subscript/Order/Data/assignment machinery is
 * out of scope for the B1 corpus (B2 units 32/33/38/39/44/49/53/60
 * own other global methods), so the members touched by unit 14 are
 * owner-named h014 stubs; delegate when an owning unit lands.
 *
 * B1 unit 31 (libmpscpp.cpp::&lt;misc-1&gt;) additions, all marked (u31):
 * the global constructor behavior, global::Eflg, the unary
 * operator+/operator-, the indirect-argument invoke overload, and the
 * MakeGlobal call inside the vararg invoke (the vendor's 30+
 * global::operator() overloads fold into it; see the journal).
 */
class Global(val ref: String) {

    /**
     * (u31) Vendor global::myname[256]: the constructor truncates the
     * name to 255 chars plus NUL (strncpy bound). The unit-14 carrier
     * shape keeps the name in ref; myname preserves the vendor's
     * separate field.
     */
    val myname: String = ref.take(255)

    init {
        // (u31) Vendor global::global(const char * n):
        //   strcpy(ref,""); strncpy(myname,n,256); myname[255]=0;
        //   if (Eflg>0) return; Eflg++; atexit(__finish);
        //   set_terminate(thrownMessage);
        // The carrier's ref carries the name (unit-14 shape, see
        // journal); the empty-ref initial state is the MakeGlobal
        // buffer, out of B1 scope.
        if (Eflg == 0) {
            Eflg++
            // atexit(__finish) -> one JVM shutdown hook (adaptation).
            Runtime.getRuntime().addShutdownHook(Thread { __finish() })
            // set_terminate(thrownMessage): the C++ terminate handler
            // has no JVM equivalent; left unwired (adaptation, journal).
        }
    }

    /** Vendor global::Kill - kill entire global (owner: libmpscpp.cpp). */
    fun Kill() {
        TODO("h014 stub: owner libmpscpp.cpp class global::Kill (not a B1 corpus unit)")
    }

    /**
     * Vendor global::operator() - subscript (0, 1 or 2 subscripts here;
     * unit 14). (u31) The vendor declares 30+ overloads of
     * global::operator() (string, mstring, const char* and long forms, arities
     * 0..10) that all call MakeGlobal(0, myname, "a,b,...", ...);
     * Kotlin cannot overload invoke that way, so the family folds
     * into this vararg form: the names list is rebuilt per arity and
     * passed to the makeGlobal stub (owner global::MakeGlobal,
     * batch-7).
     */
    operator fun invoke(vararg subs: String): GlobalRef {
        val names = if (subs.isEmpty()) "" else subs.indices.joinToString(",") { ('a' + it).toString() }
        val vals = if (subs.isEmpty()) arrayOf("") else subs
        makeGlobal(0, myname, names, *vals)
        return GlobalRef(this, subs.toList())
    }

    /**
     * (u31) Vendor: global & global::operator() (indirect a) - prints
     * the indirect reference; the vendor's MakeGlobal call is
     * commented out in the source (kept as a comment, not wired).
     */
    operator fun invoke(a: Indirect): GlobalRef {
        println(a.reference)
        // (*this).MakeGlobal(0,(*this).myname, (char *) "a", (char *) "dummy");  [commented out in vendor]
        return GlobalRef(this, emptyList())
    }

    /** (u31) Vendor: mstring global::operator+() - unary plus; tmp = *this + 0. */
    operator fun unaryPlus(): Mstring {
        val tmp = globalPlusInt(this, 0)
        return tmp
    }

    /**
     * (u31) Vendor: mstring global::operator-() - unary minus; the
     * misc-1 chunk boundary cuts this body after tmp = 0 - *this; -
     * the closing return is supplied to keep the unit compilable
     * (see journal).
     */
    operator fun unaryMinus(): Mstring {
        val tmp = intMinusGlobal(0, this)
        return tmp
    }

    companion object {
        /**
         * (u31) Vendor: int global::Eflg = 0 (libmpscpp.cpp file-scope
         * static class member).
         */
        var Eflg: Int = 0
    }
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
