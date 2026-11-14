package mumps.mdh

/**
 * Kotlin node-object model for the C++ `global` class (globalClass.h;
 * bodies live in libmpscpp.cpp outside batch 1 -> stub methods).
 *
 * C++ pattern: operator() subscripts (`B(term1,term2)`) yield a global
 * reference; (string) casts read the value; operator= writes it; .Order()/
 * .Data()/.Kill() are member calls (heuristic 54).
 *
 * Kotlin mapping: operator fun invoke(vararg) returns a node exposing
 * `var value` for the read/write sides.
 */
class MdhGlobal {

    /** Subscripted node: C: global::operator()(...) */
    operator fun invoke(vararg subscripts: String): MdhGlobal {
        // stub: real subscript machinery (DB-backed) is outside batch 1
        return this
    }

    /** C: implicit (string) conversion / operator= — node value. */
    var value: String = ""

    /** C: global& Order(int n) — $ORDER next subscript; "" when exhausted. */
    fun order(n: Int): String =
        TODO("global::Order not yet translated (outside batch 1)")

    /** C: int Data() — $DATA node structure code. */
    fun data(): Int =
        TODO("global::Data not yet translated (outside batch 1)")

    /** C: void Kill() */
    fun kill() {
        TODO("global::Kill not yet translated (outside batch 1)")
    }
}
