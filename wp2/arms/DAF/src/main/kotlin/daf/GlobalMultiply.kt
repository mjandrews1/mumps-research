package daf

// R1 stub-completion (2026-11-28, C-020 erratum session, tranche 5):
// libmpscpp.cpp::global::Multiply (roster row 42, loc 26,
// complex): the class member form of the matrix multiply: the
// receiver is A, the B/C arguments are the operand globals;
// the walk is the landed unit-141 string-overload body
// (Multiply.kt multiplyD, vendor :2211) with the member
// receiver (*this) in place of the ctor-built A - the C++
// member and free-function bodies are the vendor copy-paste
// pair (the Count/Min wrapper precedent of unit 99).
// Consults (2026-11-28T00:00:00Z, nominal): the multiplyD
// consult set verbatim (rows 105/54/41+43/40 hit-reuse: the
// node-model walk, the operator() subscript + var value fold,
// the fmt8g store through gcvt(x,8), the strtod-parsed product
// with the += accumulation); rows 54/68 hit-reuse (the
// receiver is the MpsGlobalNode carrier the wrapper passes).
// Vendor quirks kept: (1) the C().Kill() pre-clear rides the
// raised no-arg invoke seat (the multiplyD precedent); (2) the
// dead commented printf lines of the vendor body are not
// carried (comment-only residue, the row-46 discipline); (3)
// the member reads NO svPtr - the walk is entirely over the
// node seats.
// Dafny: EXEMPT (global-access triple walk; the multiplyD
// precedent; journal row).

fun globalMultiplyD(a: MpsGlobalNode, b: MpsGlobalNode, c: MpsGlobalNode) {

    c().kill() // C().Kill();
    var i = "" // i="";

    while (true) { // while (1)

        i = a(i).order(1) // i = (string)(*this)(i).Order(1);  each row
        if (i == "") break

        var j = "" // j="";
        while (true) { // while (1)

            j = a(i, j).order(1) // each column
            if (j == "") break

            val k = j // k = j;

            if (!b(k).data()) continue // if (!B(k).Data()) continue;
            var m = "" // m="";

            while (true) { // while (1)
                m = b(k, m).order(1)
                if (m == "") break

                // if (!C(i,m).Data()) C(i,m)=(*this)(i,j)*B(k,m);
                val product =
                    DafCRuntime.strtod(a(i, j).value) *
                    DafCRuntime.strtod(b(k, m).value)

                if (!c(i, m).data()) {
                    c(i, m).value = DafCRuntime.fmt8g(product)
                } else {
                    // else C(i,m)=C(i,m)+(*this)(i,j)*B(k,m);
                    c(i, m).value = DafCRuntime.fmt8g(
                        DafCRuntime.strtod(c(i, m).value) + product)
                }
            }
        }
    }
}
