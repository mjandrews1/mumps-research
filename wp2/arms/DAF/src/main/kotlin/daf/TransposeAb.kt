package daf

// B4-T1 MAPPING REPAIR render, landed with DAF B5 T2 (logged
// protocol disclosure in the journal section of this tranche):
// libmpscpp.cpp::Transpose, the units.csv ROW-118 body (loc 15,
// ptr 0, score 8.5, the void Transpose(char* a, char* b)
// global-pair wrapper at vendor :2092). The B4 T1 unit-100 render
// (Transpose.kt) was built from the OTHER corpus Transpose (the
// 23-LOC MSV service wrapper at :10316, corpus row 142 of THIS
// batch) - the per-id dump collision of the two same-id units
// (select-tool name collision; the corpus carries exactly two
// Transpose rows, rows 118 and 142) silently picked the row-142
// body for the B4 tranche, leaving the row-118 body unrendered.
// This render completes the row-118 unit; Transpose.kt gains the
// row-142 verification addendum in the same push (one-render rule:
// two corpus rows, two distinct renders).
// Consults: BASE-log row 105 hit-reuse (the C++ global node model
// reused from unit 31: ctor/invoke/value; the BASE row-105 walk
// shape - the same double-Order pair walk as its multiply), row
// 54 hit-reuse (operator() subscript + value store fold:
// B(bb,aa)=A(aa,bb) renders as the value copy between the two
// node invokes; the plain-copy store needs no gcvt format - the
// vendor global::operator=(global s) routes s >> tmp1 then
// set_global, the render copies the String value verbatim), rows
// 103/109 family hit-reuse (the Order(1) sibling-walk skeleton).
// Dafny: EXEMPT (global-access double walk; journal row).

fun transposeAbD(a: String, b: String) {

    val A = MpsGlobalNode(a) // global A(a), B(b);
    val B = MpsGlobalNode(b)
    var aa = "" // string aa="", bb="";
    var bb = ""

    while (true) { // while ( 1)
        aa = A(aa).order(1) // aa = (string) A(aa).Order(1);
        if (aa == "") break

        bb = "" // bb="";

        while (true) { // while (1)
            bb = A(aa, bb).order(1) // bb = (string) A(aa,bb).Order(1);
            if (bb == "") break

            // B(bb,aa)=A(aa,bb);
            B(bb, aa).value = A(aa, bb).value
        }
    }
}
