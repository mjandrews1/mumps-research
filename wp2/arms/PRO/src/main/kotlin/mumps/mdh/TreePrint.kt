package mumps.mdh

import kotlin.io.print

/**
 * PRO-arm translation of libmpscpp.cpp::global::TreePrint(int,
 * const char) (pilot B2, unit 60, provisional under deviation
 * C-003). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 1831-2035 (units.csv: 116 LOC, score 142.6, rank 282).
 *
 * The global-array tree printer: walks the first ten subscript
 * levels in order, printing each node name (indented by depth x c)
 * and, for data-bearing nodes (Data() 1 or 11), an "= value" line.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h010 numeric_promotion  : the vendor's int i (the indent depth
 *    parameter, reused as the Data() scratch) receives Data() as a
 *    Double on the Kotlin carrier, truncated with .toInt() as in C;
 *    the Data()<2 depth gates compare as doubles.
 *  - h011 control_flow       : the ten nested while(1) loops render
 *    as while(true) with the vendor break/continue shape kept
 *    level for level; the indent for loops use fresh loop variables
 *    (the vendor reuses i there - noted at each site).
 *  - h013 dead_branches     : the vendor's redundant (string) casts
 *    on the Order results are dropped (u53 precedent).
 *  - h014 out_of_scope_call  : Order/Data/toDouble ride the existing
 *    GlobalRef owner-named stubs; the cout << global implicit
 *    string read is a new carrier stub GlobalRef.stringValue()
 *    (owner: libmpscpp.cpp class global operator mstring/const
 *    char* machinery, not a B1 corpus unit).
 *  - h017 naming             : TreePrint keeps its name; emitted as
 *    a Global extension (the vendor's *this), unit-14 precedent.
 *
 * Vendor quirks kept faithfully (not fixed):
 *  - the level-8 Order call drops the a1 subscript: the vendor
 *    reads (*this)(a0,a2,a3,a4,a5,a6,a7,a8) - missing a1 - while
 *    every other level threads all subscripts; kept verbatim.
 *  - levels 8 and 9 print the node name with endl BEFORE the
 *    Data() test, so a data-bearing node prints name, then a
 *    separate "= value" line (unlike levels 0-7, which print
 *    name + eq + value on one line).
 *  - only levels 5 and 6 carry the extra Data()<2 descend gate
 *    before opening the next level (duplicated with the i<2
 *    continue two lines above).
 *  - the indent loops print (level+1) indents: for (i=0; i<1..9).
 */

fun Global.TreePrint(i: Int, c: Char) {

    var indent = ""
    val eq = "="
    var a0: String
    var a1: String
    var a2: String
    var a3: String
    var a4: String
    var a5: String
    var a6: String
    var a7: String
    var a8: String
    var a9: String
    // the vendor reuses the parameter i as the Data() scratch; Kotlin
    // self-shadowing forbids var i = i, so the scratch is di.
    var di = i

    if (i != 0) {
        for (j in 0 until i) indent += c
    }

    a0 = ""

    while (true) {
        a0 = this(a0).Order(1)

        if (a0 == "") break

        print(a0)
        di = this(a0).Data().toInt()

        if (di == 1 || di == 11) print(eq + this(a0).stringValue() + "\n")
        else print("\n")

        if (di < 2) continue

        a1 = ""

        while (true) {
            a1 = this(a0, a1).Order(1)

            if (a1 == "") break

            for (k in 0 until 1) print(indent) // vendor: for (i=0; i<1; i++)

            print(a1)
            di = this(a0, a1).Data().toInt()

            if (di == 1 || di == 11) print(eq + this(a0, a1).stringValue() + "\n")
            else print("\n")

            if (di < 2) continue

            a2 = ""

            while (true) {
                a2 = this(a0, a1, a2).Order(1)

                if (a2 == "") break

                for (k in 0 until 2) print(indent) // vendor: for (i=0; i<2; i++)

                print(a2)
                di = this(a0, a1, a2).Data().toInt()

                if (di == 1 || di == 11) print(eq + this(a0, a1, a2).stringValue() + "\n")
                else print("\n")

                if (di < 2) continue

                a3 = ""

                while (true) {
                    a3 = this(a0, a1, a2, a3).Order(1)

                    if (a3 == "") break

                    for (k in 0 until 3) print(indent)

                    print(a3)
                    di = this(a0, a1, a2, a3).Data().toInt()

                    if (di == 1 || di == 11) print(eq + this(a0, a1, a2, a3).stringValue() + "\n")
                    else print("\n")

                    if (di < 2) continue

                    a4 = ""

                    while (true) {
                        a4 = this(a0, a1, a2, a3, a4).Order(1)

                        if (a4 == "") break

                        for (k in 0 until 4) print(indent)

                        print(a4)
                        di = this(a0, a1, a2, a3, a4).Data().toInt()

                        if (di == 1 || di == 11) print(eq + this(a0, a1, a2, a3, a4).stringValue() + "\n")
                        else print("\n")

                        if (di < 2) continue

                        a5 = ""

                        while (true) {
                            a5 = this(a0, a1, a2, a3, a4, a5).Order(1)

                            if (a5 == "") break

                            for (k in 0 until 5) print(indent)

                            print(a5)
                            di = this(a0, a1, a2, a3, a4, a5).Data().toInt()

                            if (di == 1 || di == 11) print(eq + this(a0, a1, a2, a3, a4, a5).stringValue() + "\n")
                            else print("\n")

                            if (di < 2) continue

                            if (this(a0, a1, a2, a3, a4, a5).Data() < 2) continue

                            a6 = ""

                            while (true) {
                                a6 = this(a0, a1, a2, a3, a4, a5, a6).Order(1)

                                if (a6 == "") break

                                for (k in 0 until 6) print(indent)

                                print(a6)
                                di = this(a0, a1, a2, a3, a4, a5, a6).Data().toInt()

                                if (di == 1 || di == 11) print(eq + this(a0, a1, a2, a3, a4, a5, a6).stringValue() + "\n")
                                else print("\n")

                                if (di < 2) continue

                                if (this(a0, a1, a2, a3, a4, a5, a6).Data() < 2) continue

                                a7 = ""

                                while (true) {
                                    a7 = this(a0, a1, a2, a3, a4, a5, a6, a7).Order(1)

                                    if (a7 == "") break

                                    for (k in 0 until 7) print(indent)

                                    print(a7)
                                    di = this(a0, a1, a2, a3, a4, a5, a6, a7).Data().toInt()

                                    if (di == 1 || di == 11) print(eq + this(a0, a1, a2, a3, a4, a5, a6, a7).stringValue() + "\n")
                                    else print("\n")

                                    if (di < 2) continue

                                    a8 = ""

                                    while (true) {
                                        // vendor quirk kept: a1 is missing from
                                        // this subscript list in the source.
                                        a8 = this(a0, a2, a3, a4, a5, a6, a7, a8).Order(1)

                                        if (a8 == "") break

                                        for (k in 0 until 8) print(indent)

                                        print(a8 + "\n") // vendor prints name + endl before the Data test
                                        di = this(a0, a1, a2, a3, a4, a5, a6, a7, a8).Data().toInt()

                                        if (di == 1 || di == 11) print(eq + this(a0, a1, a2, a3, a4, a5, a6, a7, a8).stringValue() + "\n")
                                        else print("\n")

                                        if (di < 2) continue

                                        a9 = ""

                                        while (true) {
                                            a9 = this(a0, a1, a2, a3, a4, a5, a6, a7, a8, a9).Order(1)

                                            if (a9 == "") break

                                            for (k in 0 until 9) print(indent)

                                            print(a9 + "\n") // name + endl before the Data test, as at level 8
                                            di = this(a0, a1, a2, a3, a4, a5, a6, a7, a8, a9).Data().toInt()

                                            if (di == 1 || di == 11) print(eq + this(a0, a1, a2, a3, a4, a5, a6, a7, a8, a9).stringValue() + "\n")
                                            else print("\n")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
