package mumps.mdh

/**
 * Kotlin node-object model for the C++ `global` class (globalClass.h;
 * bodies live in libmpscpp.cpp — Study Subset unit 31 added the
 * operator() subscript overloads, the global(const char*) constructor,
 * Eflg, and the unary operators; the DB-backed machinery is outside
 * batch 1 -> stub methods).
 *
 * C++ pattern: operator() subscripts (`B(term1,term2)`) yield a global
 * reference; (string) casts read the value; operator= writes it; .Order()/
 * .Data()/.Kill() are member calls (heuristic 54).
 *
 * Kotlin mapping: operator fun invoke(...) returns a node exposing
 * `var value` for the read/write sides.
 *
 * OVERLOAD COLLAPSE (unit 31): the C++ operator() is defined 30+ times
 * over string/mstring/const char* at arities 0..10. Kotlin has one String
 * type, so the triplicated per-type overloads collapse into the vararg
 * String overload; the "a,b,c,..." signature string the C passes to
 * MakeGlobal is GENERATED from the arity (('a'..'j').take(n)).
 */
class MdhGlobal {

    /** C: char myname[256] — global name (ctor: strncpy(n,256); [255]=0). */
    var myname: String = ""

    /** C: char ref[] member cleared by the constructor (strcpy(ref,"")). */
    var ref: String = ""

    companion object {
        /** C: int global::Eflg = 0 (libmpscpp.cpp:100) — one-time init flag. */
        var Eflg: Int = 0
    }

    /**
     * C: global::global(const char * n) (libmpscpp.cpp:3451-3462).
     * QUIRK PRESERVED: the Eflg guard means atexit()/set_terminate() are
     * registered only by the FIRST named global ever constructed.
     * atexit -> JVM shutdown hook; set_terminate -> default uncaught
     * exception handler (heuristic 71 family: POSIX/C++ runtime ->
     * JVM substitutes).
     */
    constructor(n: String) {
        ref = ""
        myname = if (n.length > 255) n.substring(0, 255) else n
        // C: strncpy(myname,n,256); myname[255]=0;

        if (Eflg > 0) return // C: if (Eflg>0) return;
        Eflg++               // C: Eflg++;

        // C: atexit(__finish);
        Runtime.getRuntime().addShutdownHook(Thread { __finish() })

        // C: set_terminate(thrownMessage);
        Thread.setDefaultUncaughtExceptionHandler { _, _ -> thrownMessage() }
    }

    constructor()

    /**
     * C: global & global::operator() (string/mstring/const char* a...j)
     * (libmpscpp.cpp:2739-2930) — subscript a global reference. The C
     * passes MakeGlobal a literal signature ("a", "a,b", ... "a,b,...,j")
     * naming the formal parameters; generated from arity here.
     */
    operator fun invoke(vararg subscripts: String): MdhGlobal {
        // C zero-arg: MakeGlobal(0,myname,"","")
        val sig = ('a'..'j').take(subscripts.size).joinToString(",")
        makeGlobal(0, myname, sig, *subscripts)
        return this
    }

    /** C: global & global::operator() (const long int a) (line 2871) —
     *  sprintf(x,"%d",a); MakeGlobal(0,myname,"a",x). */
    operator fun invoke(a: Long): MdhGlobal {
        makeGlobal(0, myname, "a", a.toString())
        return this
    }

    /**
     * C: global & global::operator() (indirect a) (line 2739) — QUIRK
     * PRESERVED: the C only PRINTS the reference; its MakeGlobal call is
     * commented out in the source (line 2742), so no subscript is made.
     */
    operator fun invoke(a: Indirect): MdhGlobal {
        println(a.reference) // C: cout << a.reference << endl;
        return this
    }

    /** C: int MakeGlobal(int, char[], char[], ...) — DB name/subscript
     *  machinery; body outside batch 1. */
    fun makeGlobal(flag: Int, name: String, sig: String, vararg args: String): MdhGlobal =
        TODO("global::MakeGlobal not yet translated (outside batch 1)")

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

    /**
     * C: mstring global::operator+() (libmpscpp.cpp:4671) — tmp=*this+0.
     * global-numeric arithmetic is outside batch 1 -> stub.
     */
    operator fun unaryPlus(): String =
        TODO("global::operator+ — *this+0 (global arithmetic outside batch 1)")

    /** C: mstring global::operator-() (line 4677) — tmp=0-*this. */
    operator fun unaryMinus(): String =
        TODO("global::operator- — 0-*this (global arithmetic outside batch 1)")
}
