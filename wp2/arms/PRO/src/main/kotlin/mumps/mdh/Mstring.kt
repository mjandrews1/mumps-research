package mumps.mdh

/**
 * PRO-arm minimal carrier for the vendor C++ class mstring
 * (libmpscpp machinery). Not itself a corpus unit; members are added per
 * translated unit as needed (one-owner policy, cf. rules_v0.pl h004).
 */
class Mstring(var value: String = "") {

    /** c_str() equivalent for translated call sites. */
    fun c_str(): String = value
}
