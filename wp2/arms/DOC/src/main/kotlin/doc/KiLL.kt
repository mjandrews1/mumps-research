package doc

// Unit 37: libmpscpp.cpp::KiLL (batch 2, simple stratum). The unit deletes
// a global and all siblings sharing its first k bytes.
// Vendor quirk kept exactly: when the key ends in two delimiter bytes
// (byte value 1), only the LAST of the two is set to NUL and k is
// decremented after the index expression evaluates (the vendor index
// expression uses the pre-decrement k). The loop keeps the vendor
// break-continue shape. Per cookbook H-015 the raw low bytes are built
// programmatically instead of being written as literals.

private const val DELIM = CHAR_1

fun KiLL(gtmp: CharArray, sv: Msv) {
    val tmp14 = CharArray(STR_MAX)
    val tmp15 = CharArray(STR_MAX)

    Stubs.keyfix(gtmp)
    var k = CstringOps.strlenC(gtmp)

    if (gtmp[k - 1] == DELIM && gtmp[k - 2] == DELIM) {
        gtmp[k - 1] = NUL
        k--
    }

    CstringOps.strcpyC(tmp14, gtmp)

    Stubs.Mglobal(GKILL, gtmp, tmp15, sv)

    while (true) {
        val j = Stubs.Mglobal(XNEXT, gtmp, tmp15, sv)

        if (j != 0 && CstringOps.strncmpC(tmp14, gtmp, k) == 0) {
            Stubs.Mglobal(GKILL, gtmp, tmp15, sv)
            continue
        }

        break
    }
}
