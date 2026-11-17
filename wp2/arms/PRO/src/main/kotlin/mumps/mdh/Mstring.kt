package mumps.mdh

/**
 * PRO-arm minimal carrier for the vendor C++ class mstring
 * (libmpscpp machinery). Not itself a corpus unit; members are added per
 * translated unit as needed (one-owner policy, cf. rules_v0.pl h004).
 */
class Mstring(var value: String = "") {

    /** c_str() equivalent for translated call sites. */
    fun c_str(): String = value

    /**
     * s_str() equivalent (B1 unit 31: the vendor Hash::operator()
     * (mstring) reads via s_str() while the global::operator() family
     * uses c_str(); kept distinct to preserve the vendor call shape).
     */
    fun s_str(): String = value

    // --- added by B4 unit 97 (libmpscpp.cpp::<misc-9>) ---

    /**
     * (u97) Vendor operator const char*() (libmpscpp.cpp:9840):
     * return data.c_str(); toString serves as the Kotlin implicit
     * conversion point (the explicit c_str() above predates this
     * unit and remains).
     */
    override fun toString(): String = value

    /**
     * (u97) The vendor == / != overload families (libmpscpp.cpp:
     * 9726-9837) ride Kotlin's equals: C++ compares data against
     * the rhs - atol for Int/Long rhs, atof for Double/Float,
     * value equality for string-ish rhs (char*/const char*/
     * std::string all map to String), extraction (i >> t1) for
     * global rhs, data equality for mstring rhs. The != families
     * fold into Kotlin's negated equals (h015); the <= family is
     * translated as compareTo extensions in LibmpscppMisc9.kt.
     */
    override fun equals(other: Any?): Boolean = when (other) {
        is Int -> atol_(value) == other.toLong()
        is Long -> atol_(value) == other
        is Float -> atof_(value) == other.toDouble()
        is Double -> atof_(value) == other
        is String -> value == other
        is Mstring -> value == other.value
        is Global -> {
            val t1 = CharArray(STR_MAX_)
            other.readInto(t1) // vendor: i >> t1 (h014 stub, unit 97)
            value == mumps.interp.cstringAt(t1)
        }
        else -> false // no vendor fallback overload
    }

    /**
     * (u97) Required companion to the equals override (Kotlin
     * equals/hashCode consistency); the vendor has no hashCode -
     * adaptation (h015).
     */
    override fun hashCode(): Int = value.hashCode()
}

/** strmax.h:3 STR_MAX 32000 (file-private, cf. Cosine unit 71). */
private const val STR_MAX_ = 32000

/** (u97) atol(data.c_str()) over the Kotlin String value (h009 local parser). */
private fun atol_(s: String): Long {
    var i = 0
    while (i < s.length && (s[i] == ' ' || s[i] == '\t')) i++
    var neg = false
    if (i < s.length && (s[i] == '+' || s[i] == '-')) {
        neg = s[i] == '-'
        i++
    }
    var v = 0L
    while (i < s.length && s[i] in '0'..'9') {
        v = v * 10 + (s[i] - '0')
        i++
    }
    return if (neg) -v else v
}

/** (u97) atof(data.c_str()) over the Kotlin String value (strtodC reuse, h014). */
private fun atof_(s: String): Double = mumps.interp.strtodC(toCbuf_(s))

/** (u97) String -> NUL-terminated C-string buffer (call-shape helper). */
private fun toCbuf_(s: String): CharArray {
    val b = CharArray(s.length + 1)
    for (k in s.indices) b[k] = s[k]
    b[s.length] = mumps.interp.NUL
    return b
}
