package mumps.interp

/**
 * PRO-arm translation of interp-globalb.cpp::<misc> (roster row 287,
 * batch 10; units.csv: loc 112, comment_ratio 0.304, pointer_marks
 * 4, control_marks 25, score 53.4, rank 239). Translated
 * independently from the pristine vendor source (mumps-14-may-2026
 * .tgz, sha256 621deb25...ffdbe), reference source: the residual
 * <misc> chunk of interp-globalb.cpp - the file header block (the
 * license banner, the #define SQLITE reference-build switch, the
 * #include list) and the complete non-SQLITE (#else) native-btree
 * arm of Mglobal, from the RETRIEVE/STORE pair through the final
 * return 0 / #endif tail. The chunk reproduces the study tool's
 * misc residue exactly (loc 112, cr 0.304, ptr 4, ctrl 25,
 * score 53.4 - load_units repro verified).
 *
 * h018 preprocessor_conditional: the reference build DEFINES
 * SQLITE (config.log DBMS SQLITE, the Mglobal.kt u173 note), so
 * the vendor compiles this arm OUT - but the arm IS this unit's
 * corpus carrier, so it renders as its own seat MglobalNative
 * (the compiled-in SQLITE arm stays Mglobal.kt, one-owner h004:
 * two arms of one vendor function, two Kotlin seats).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 one_owner        : the struct/#define residue is
 *    ledgered here, not re-rendered; keyfix is the MdhStubs seat;
 *    btree is the NativeBtree.btree real seat; unpad is the
 *    InterpStubs batch-10 stub (roster row 289 resolves it).
 *  - h007 char_buffer      : hold rides a STR_MAX CharArray
 *    (vendor local unsigned char hold[STR_MAX], 32000, strmax.h:3).
 *  - h010 numeric_narrowing : the vendor 1/2 byte sentinels ride
 *    toChar() code-point carriers (NEXTMARK 1, defines.h:122).
 *  - h012 unsigned_byte_domain: key/bd are C string byte domains
 *    over CharArray; the x/y pointer shift renders as index
 *    arithmetic (in-bd shift, offset-carrier precedent);
 *    strcpy(bd,"") etc. are strcpyC over the whole-buffer
 *    carriers; strncmp is strncmpC.
 *  - h013 dead_code        : the vendor writes key[j-2] = 1 then
 *    key[j-2] = 0 in the same breath (the <dollar>next -1 case arm) -
 *    the first store is dead, BOTH renders kept verbatim as the
 *    vendor wrote them.
 *  - h014 out_of_scope_call: the discarded btree return of the
 *    NEXT/ORDER post-scan leg (vendor b=) rides the call-only
 *    form; the GKILL/XNEXT returns keep their carriers.
 *  - h017 naming           : the seat takes the MglobalNative name
 *    (the vendor arm shares the compiled-out Mglobal body; the
 *    Kotlin seat must not collide with the Mglobal.kt owner).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The POST-btree hold-vs-bd scan walks j DOWNWARD from
 *    strlen(hold)-1 to the last NEXTMARK; the strncmp uses that
 *    j as length - kept (the strncmpC(String, j) form compares j
 *    chars against the C string at bd).
 *  - The -1 result convention: after every non-NEXT arm failure
 *    bd is the empty string; NEXT writes "-1" - kept.
 *  - The CLOSE arm returns EXIT_SUCCESS (0) AFTER calling btree
 *    (unlike the SQLITE arm's early return) - kept.
 *  - The key buffer rides the whole-buffer (offset 0) form: the
 *    native arm mutates key in place across the keyfix/btree
 *    pair, exactly the vendor's shared storage behavior.
 */
private const val RETRIEVE = 0 // vendor defines.h:83
private const val STORE = 1    // vendor defines.h:84
private const val GKILL = 3    // vendor defines.h:167
private const val CLOSE = 4    // vendor defines.h:87
private const val XNEXT = 8    // vendor defines.h:88
private const val PREVIOUS = 9 // vendor defines.h:89
private const val NEXT = 2     // vendor defines.h:168
private const val ORDERNEXT = 12 // vendor defines.h:170
private const val ORDERPREV = 13 // vendor defines.h:171

/** The non-SQLITE native-btree arm of Mglobal (vendor #else tail). */
fun MglobalNative(g: Int, key: CharArray, bd: CharArray, svPtr: Msv): Int {
    if (g == RETRIEVE) {
        mumps.mdh.keyfix(key)
        return NativeBtree.btree(g, key, bd, svPtr)
    }
    if (g == STORE) {
        mumps.mdh.keyfix(key)
        return NativeBtree.btree(g, key, bd, svPtr)
    }

    // --------------------------------
    //     prepare NEXT/ORDER cases
    // --------------------------------
    if (g == ORDERNEXT || g == ORDERPREV || g == NEXT || g == PREVIOUS) {
        val hold = CharArray(32000) // vendor: unsigned char hold[STR_MAX]
        var j = strlenC(key)

        // -----------------------
        //     <dollar>next -1 case
        // -----------------------
        if (g != ORDERNEXT && g != ORDERPREV &&
            key[j - 1] == 1.toChar() &&
            key[j - 2] == '1' &&
            key[j - 3] == '-' &&
            key[j - 4] == 1.toChar()) {
            key[j - 3] = 2.toChar()
            key[j - 2] = 1.toChar()
            key[j - 2] = 0.toChar()
            key[j - 1] = 0.toChar()
            strcpyC(bd, "")
            j--
        } else if (g == NEXT || g == ORDERNEXT) {
            key[j - 1] = 2.toChar()
            key[j] = 0.toChar()
            key[j + 1] = 0.toChar()
            key[j + 2] = 0.toChar()
            key[j + 3] = 0.toChar()
            strcpyC(bd, "")
        } else if (g == ORDERPREV && key[j - 1] == 1.toChar() &&
            key[j - 2] == 1.toChar()) {
            key[j - 1] = '~'
        }

        var gx = g
        if (gx == NEXT) gx = XNEXT
        else if (gx == ORDERNEXT) gx = XNEXT
        else if (gx == ORDERPREV) gx = PREVIOUS

        mumps.mdh.keyfix(key)
        strcpyC(hold, key)
        NativeBtree.btree(gx, key, bd, svPtr)

        if (g == NEXT || g == ORDERNEXT || g == ORDERPREV) {
            j = strlenC(hold) - 1
            if (hold[j] == 1.toChar()) j--
            while (j > 0 && hold[j] != 1.toChar()) j--
            if (strncmpC(hold, 0, cstringAt(bd), j) != 0 ||
                hold[j] != 1.toChar() ||
                bd[j] != 1.toChar()) {
                if (g == NEXT) strcpyC(bd, "-1") else strcpyC(bd, "")
                return 0
            }
            if (j > 0) {
                // unsigned char *x = bd, *y = &bd[j + 1];
                // for (; *y != 0; x++, y++) *x = *y;  // STRCPY
                var x = 0
                var y = j + 1
                while (bd[y] != 0.toChar()) {
                    bd[x] = bd[y]
                    x++
                    y++
                }
                bd[x] = 0.toChar()
            }
            j = 0
            while (bd[j] != 1.toChar() && bd[j] != 0.toChar()) j++
            bd[j] = 0.toChar()
            unpad(bd, svPtr) // InterpStubs stub (roster row 289 owner)

            if (strlenC(bd) == 0) {
                if (g == NEXT) strcpyC(bd, "-1") else strcpyC(bd, "")
                return 0
            }
            return 1
        }
    }

    if (g == GKILL) {
        return NativeBtree.btree(g, key, bd, svPtr)
    }

    if (g == XNEXT) {
        mumps.mdh.keyfix(key)
        return NativeBtree.btree(g, key, bd, svPtr)
    }

    if (g == CLOSE) {
        NativeBtree.btree(g, key, bd, svPtr)
        return 0 // EXIT_SUCCESS
    }

    return 0
}
