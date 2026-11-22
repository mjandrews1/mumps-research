package daf

// Unit 141 (DAF B5 T2, core): libmpscpp.cpp::Multiply
// (units.csv row 141: loc 26, ptr 2, score 21.4, rank 172, core):
// the string-argument matrix multiply driver: C().Kill() clears
// the product, then the double Order walk (rows of A, columns j
// of A(i), the B(k) Data gate, the m-walk of B(k,m)) accumulates
// the A(i,j)*B(k,m) products into C(i,m) with the += fold for
// repeats. The SAME-ID ADJUDICATION (logged manual-decision): the
// study-tool corpus contains three libmpscpp.cpp::Multiply rows
// (units.csv rows 49/141/201); this tranche claims the
// string-overload body (vendor :2211, phys 26, score 21.4 - the
// metric-unique match; the char* twin at :2252 is score 20.8, the
// MSV dispatcher at :10193 is loc 28) picked by the per-row study
// metrics exactly as the global::Length twins were adjudicated at
// B5 T1.
// Consults: BASE-log row 105 hit-reuse (the C++ global node model
// of unit 31: ctor/invoke/value covers the wrapper walk - the BASE
// multiply(a,b,c) is this same body), row 54 hit-reuse (operator()
// subscript + the var value fold covering operator=; the order
// walk rides the MpsGlobalNode seats of the arm node stub), rows
// 41+43 hit-reuse (the C(i,m)=double store formats through
// global::operator=(double) -> gcvt_wrapper(x,15) which resolves
// to gcvt(x,8) - the plain %.8g/no-decimal-point family verified
// at row 43; the DAF render stores DafCRuntime.fmt8g on the node
// value), row 40 + vendor operator*(global) hit-adapt (the product
// reads both operand values (operator>> fold = the node value) and
// strtod-parses each, the C++ double product at :5069; the +=
// accumulation is the fresh store of value + fmt8g(last product)).
// Dafny: EXEMPT (global-access triple walk; journal row).

fun multiplyD(a: String, b: String, c: String) {

    val A = MpsGlobalNode(a) // global A((char*)a.c_str());
    val B = MpsGlobalNode(b) // global B(...)     (vendor b-c_str twin)
    val C = MpsGlobalNode(c) // global C((char*)c.c_str());

    C().kill() // C().Kill();
    var i = "" // i="";

    while (true) { // while (1)

        i = A(i).order(1) // i=(string)A(i).Order(1);
        if (i == "") break

        var j = "" // j="";

        while (true) { // while (1)
            j = A(i, j).order(1) // j=(string)A(i,j).Order(1);
            if (j == "") break

            val k = j // k=j;

            if (!B(k).data()) continue // if (!B(k).Data()) continue;

            var m = "" // m="";

            while (true) { // while (1)
                m = B(k, m).order(1) // m=(string)B(k,m).Order(1);
                if (m == "") break

                val product =
                    DafCRuntime.strtod(A(i, j).value) *
                    DafCRuntime.strtod(B(k, m).value)
                // A(i,j)*B(k,m) - the operator*(global) fold
                if (!C(i, m).data()) {
                    C(i, m).value = DafCRuntime.fmt8g(product)
                    // if (!C(i,m).Data()) C(i,m)=...
                } else {
                    C(i, m).value = DafCRuntime.fmt8g(
                        DafCRuntime.strtod(C(i, m).value) + product)
                    // else C(i,m)=C(i,m)+A(i,j)*B(k,m);
                }
            }
        }
    }
}
