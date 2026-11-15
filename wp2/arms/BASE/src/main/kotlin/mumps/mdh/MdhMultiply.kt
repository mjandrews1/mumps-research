package mumps.mdh

/**
 * Kotlin translation of libmpscpp.cpp::Multiply (Study Subset unit 46,
 * Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: void Multiply(char * a, char * b, char * c) (libmpscpp.cpp:2252-
 * 2282) — matrix multiply over three globals: A x B -> C, walking
 * A's rows (i) and columns (j), and for each j-column present in B
 * (as row k=j), accumulating B's columns m into C(i,m).
 *
 * GLOBAL MODEL (StubsMdh unit-31 node model): global A(a) -> MdhGlobal
 * ctor; A(i,j) -> invoke(vararg) returning the node; (string) cast ->
 * .value; operator= -> .value assignment; .Order(1)/.Data()/.Kill() ->
 * order/data/kill (existing TODO stubs). C().Kill() zeroes C first.
 *
 * ARITHMETIC: the C global::operator*(mstring&) / operator+(mstring&)
 * (libmpscpp.cpp:4621-4640) delegate to the arith.h char* numeric
 * operators — arith.h::<misc> is unit 57, outside batch scope NOW, so
 * both become operator TODO stubs here as extensions on MdhGlobal
 * (owner file untouched; unit 31 owns the class body). Their C++
 * signatures take mstring&, which Kotlin models as String.
 *
 * QUIRK PRESERVED: the free Multiply(char*,char*,char*) is the C++
 * driver of the same body as global::Multiply (2164-2210); only the
 * free function is this unit. The (string) casts around Order() are
 * C++ conversion noise — order() already returns String.
 */
/** C: mstring global::operator*(mstring & a) (libmpscpp.cpp:4631) —
 *  numeric multiply via arith.h char* operators (unit 57); stub.
 *  C++ takes mstring& with implicit global->mstring conversion; the
 *  Kotlin call passes the node itself, so the stub takes MdhGlobal. */
operator fun MdhGlobal.times(a: MdhGlobal): String =
    TODO("global::operator* not yet translated (arith.h machinery, unit 57)")

/** C: mstring global::operator+(mstring & a) (libmpscpp.cpp:4621) —
 *  numeric add via arith.h char* operators (unit 57); stub. */
operator fun MdhGlobal.plus(a: String): String =
    TODO("global::operator+ not yet translated (arith.h machinery, unit 57)")

/** C: void Multiply(char * a, char * b, char * c) */
fun multiply(a: String, b: String, c: String) {
    val A = MdhGlobal(a) // C: global A((char *)a);
    val B = MdhGlobal(b) // C: global B((char *)b);
    val C = MdhGlobal(c) // C: global C((char *)c);

    C().kill() // C: C().Kill();

    var i = "" // C: string i,j,k,m; i="";

    while (true) { // C: while (1) {
        i = A(i).order(1) // C: i=(string) A(i).Order(1); // each row

        if (i == "") break

        var j = ""

        while (true) {
            j = A(i, j).order(1) // C: j=(string) A(i,j).Order(1); // each column

            if (j == "") break

            val k = j // C: k=j;

            if (B(k).data() == 0) continue // C: if (!B(k).Data()) continue;

            var m = ""

            while (true) {
                m = B(k, m).order(1) // C: m=(string) B(k,m).Order(1);

                if (m == "") break

                // C: if (!C(i,m).Data()) C(i,m)=A(i,j)*B(k,m);
                //    else C(i,m)=C(i,m)+A(i,j)*B(k,m);
                if (C(i, m).data() == 0) {
                    C(i, m).value = A(i, j) * B(k, m)
                } else {
                    C(i, m).value = C(i, m) + (A(i, j) * B(k, m))
                }
            }
        }
    }
}
