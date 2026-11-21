package daf

// Unit 112 (DAF B4 T2, core): libmpscpp.cpp::global::Centroid (30 LOC):
// the global class member that collapses a two-level global tree into
// its per-leaf centroid: kills the target, then walks the source
// subscripts, accumulating per-second-level sums and counts, and
// finally divides the sums by the counts in place.
// Consults: BASE-log row 54 hit-reuse (the operator() subscript + node
// model of the TermCorrelate BASE render: MpsGlobalNode seats with the
// var-value operator= fold and the invoke members), rows 6/22 (the
// (string) cast of the Order walk return is absorbed by the String
// order() seat, per the node-model fold), rows 97/98 family (the
// Mglobal/mstring conversion overloads collapse onto String values,
// per the overload-collapse precedent), rows 39/40/79 family hit-adapt
// (the mstring numeric operators +, ++ and /= resolve through the
// interp-arith.cpp add()/divide() machinery and numSetup, whose
// reference-build output is the %.8g gcvt_wrapper (row 79) modeled by
// DafCRuntime.fmt8g; DOCUMENTED DIVERGENCE: the full numSetup
// canonicalization of interp-arith.cpp::add (not a roster unit) is
// approximated by strtod+fmt8g), rows 72/10 (the vendor while(1)
// break loops render as labeled while(true)+break).
// Seats raised in GlobalNodeStub.kt for this unit: value, invoke()
// (no-arg), order(dir), data(), kill() - all named-owner stubs of the
// libmpscpp.cpp global class.
// Vendor quirk preserved: the final loop reads x from B(bb,"1") and
// divides B(bb) (the value node) by it, exactly as written.
// Dafny: EXEMPT (stub-mediated global traversal; journal row).

// mstring operator+ (numeric): add(a,b) of interp-arith.cpp modeled as
// strtod + numSetup %.8g output (row 79 reference-build fact).
fun mstringAddD(a: String, b: String): String =
    DafCRuntime.fmt8g(DafCRuntime.strtod(a) + DafCRuntime.strtod(b))

// mstring operator/= (numeric): divide(a,b) of interp-arith.cpp.
fun mstringDivD(a: String, b: String): String =
    DafCRuntime.fmt8g(DafCRuntime.strtod(a) / DafCRuntime.strtod(b))

fun globalCentroid(node: MpsGlobalNode, B: MpsGlobalNode) { // *this -> node

    var aa = ""
    var bb = ""
    var x = 0.0

    B().kill() // B().Kill(): the no-arg invoke fold

    while (true) { // while (1) over the first-level subscripts
        aa = node(aa).order(1) // aa = (string)(*this)(aa).Order(1)

        if (aa == "") break

        bb = ""
        while (true) { // while (1) over the second-level subscripts
            bb = node(aa, bb).order(1) // (string)(*this)(aa,bb).Order(1)

            if (bb == "") break

            if (!B(bb).data()) {    // if (!B(bb).Data())
                B(bb).value = node(aa, bb).value // B(bb)=(*this)(aa,bb)
                B(bb, "1").value = "1"          // B(bb,"1")=1
            } else {
                B(bb).value = mstringAddD(B(bb).value, node(aa, bb).value)
                B(bb, "1").value =               // B(bb,"1")++
                    mstringAddD(B(bb, "1").value, "1")
            }
        }
    }

    bb = ""
    while (true) { // the averaging pass
        bb = B(bb).order(1) // bb = (string)B(bb).Order(1)

        if (bb == "") break

        x = DafCRuntime.strtod(B(bb, "1").value) // x = B(bb,"1")  (mstring->double)
        B(bb).value = mstringDivD(B(bb).value, DafCRuntime.fmt8g(x)) // B(bb)/=x
    }
}
