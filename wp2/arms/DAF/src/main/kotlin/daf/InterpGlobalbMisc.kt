package daf

// Unit 286 (DAF B10 T3, residue): interp-globalb.cpp::<misc> (units.csv
// row 286: 112 physical LOC): the NATIVE #else arm of Mglobal - the
// btree-native global dispatcher. DEAD ARM on the reference build:
// interp-globalb.cpp #defines SQLITE (:45) so the unit-167 render
// (Mglobal.kt MglobalDaf) IS the live arm and this #else body is
// preprocessor-eliminated in C. Per the vendor-dead lattice
// discipline (CompilerMumpsMain "if (0)" precedent), the body
// renders here VERBATIM-DEAD under a distinct name (mglobalNativeD)
// that NO caller binds; the SQLITE_D const (InterpBifsMisc.kt:29)
// records the pin.
// Consults (2026-11-25T15:05:00Z, nominal): row 78/39 hit-reuse
// (the #define SQLITE lattice already seated at Mglobal.kt),
// rows 37/90 hit-reuse (btreeDaf released B8, keyfixDaf b3),
// row 54 hit-adapt (the opcode falls; gcc-style single-char
// subscript writes fold onto CharArray index writes - the C 1
// and 2 are unsigned char constants, kept as '\u0001'/'\u0002'),
// row 56 hit-adapt (strlen/strcpy/strncmp via cstring folds),
// row 92 hit-reuse (unpadDaf of the row-288 render binds the
// bd-unpad tail).
// VIP: the native arm KEYS use binary subscript markers
// (char 1 = NEXTMARK, char 2 = the "incremented" NEXT probe, '~'
// = the ORDERPREV ceiling); the hold[] probe walk and bd splice
// are byte-exact per vendor.
// VENDOR QUIRKS kept: (1) the -1 \u0024next probe overwrites key with
// marker 2 then zeroes TWO of the same slots (key[j-2] = 1 THEN
// key[j-2] = 0 - the vendor writes 1 and immediately 0; kept);
// (2) the NEXT/ORDERNEXT probe writes markers at j-1..j+3 with no
// bounds guard (kept; the gate exercises no native path);
// (3) the hold/bd "same-prefix" check passes j from a loop that
// ends with hold[j]==1 OR j==0, then ALSO requires hold[j]==1 &&
// bd[j]==1 (a hold prefix check); (4) the marker-scan loop has an
// EMPTY body (a pure scan to the first marker/NUL) and the tail
// write bd[j]=0 zeroes ONLY that one marker byte; (5) the XNEXT opaque fall (i unused)
// and GKILL pass-through; (6) native CLOSE returns EXIT_SUCCESS.
// Dafny: EXEMPT (dead-arm render; journal row).

// int Mglobal(...) - NATIVE arm, VERBATIM-DEAD name: no caller
// binds this on the reference build (SQLITE_D == true).
fun mglobalNativeD(g: Int, key: CharArray, bd: CharArray,
    svPtr: DafMsv): Int {

    // if (g == RETRIEVE) { keyfix(key); return btree(...); }
    if (g == RETRIEVE_D) {
        keyfixDaf(key)
        return btreeDaf(g, key, bd, svPtr)
    }
    // if (g == STORE) { keyfix(key); return btree(...); }
    if (g == STORE_D) {
        keyfixDaf(key)
        return btreeDaf(g, key, bd, svPtr)
    }

    // ----------------------------------
    // prepare NEXT/ORDER cases
    // ----------------------------------
    if (g == ORDERNEXT_D || g == ORDERPREV_D || g == NEXT_D ||
        g == BTR_PREVIOUS_D) {
        val hold = CharArray(STR_MAX_DAF)
        var j = DafCRuntime.cstring(key).length // j = strlen(key)

        // -----------------------
        // \u0024next -1 case
        // -----------------------
        if (g != ORDERNEXT_D && g != ORDERPREV_D && j >= 4 &&
            key[j - 1] == '\u0001' && key[j - 2] == '1' &&
            key[j - 3] == '-' && key[j - 4] == '\u0001') {
            key[j - 3] = '\u0002'
            key[j - 2] = '\u0001' // (vendor writes 1 THEN 0 - kept)
            key[j - 2] = NUL_D
            key[j - 1] = NUL_D
            bd[0] = NUL_D    // strcpy(bd,"")
            j--
        } else if (g == NEXT_D || g == ORDERNEXT_D) {
            key[j - 1] = '\u0002'
            key[j] = NUL_D
            if (j + 1 < key.size) key[j + 1] = NUL_D
            if (j + 2 < key.size) key[j + 2] = NUL_D
            if (j + 3 < key.size) key[j + 3] = NUL_D
            bd[0] = NUL_D    // strcpy(bd,"")
        } else if (g == ORDERPREV_D && key[j - 1] == '\u0001' &&
            key[j - 2] == '\u0001') {
            key[j - 1] = '~'
        }

        var gx = g
        if (gx == NEXT_D) gx = XNEXT_D
        else if (gx == ORDERNEXT_D) gx = XNEXT_D
        else if (gx == ORDERPREV_D) gx = BTR_PREVIOUS_D

        keyfixDaf(key)
        // strcpy(hold, key)
        var q = 0
        while (q < key.size) { hold[q] = key[q]; q++ }

        btreeDaf(gx, key, bd, svPtr) // b = btree(...) (b unused after)

        if (g == NEXT_D || g == ORDERNEXT_D || g == ORDERPREV_D) {
            var jj = DafCRuntime.cstring(hold).length - 1
            if (hold[jj] == '\u0001') jj--
            while (jj > 0 && hold[jj] != '\u0001') jj--
            j = jj
            // the same-prefix probe check
            var same = true
            var q2 = 0
            while (q2 < j) {
                if (q2 >= bd.size || hold[q2] != bd[q2]) { same = false; break }
                q2++
            }
            if (!same || hold[j] != '\u0001' ||
                (j >= bd.size || bd[j] != '\u0001')) {
                if (g == NEXT_D) {
                    bd[0] = '-'; bd[1] = '1'; bd[2] = NUL_D
                } else {
                    bd[0] = NUL_D
                }
                return 0
            }
            // splice bd left by j+1 (the unsigned char * x/y walk)
            if (j > 0) {
                var x = 0
                var y = j + 1
                while (y < bd.size && bd[y] != NUL_D) {
                    bd[x] = bd[y]; x++; y++
                }
                bd[x] = NUL_D
            }
            // for (j = 0; bd[j] !=1 && bd[j] != 0; j++); bd[j] = 0;
            // (quirk 4: the loop has an EMPTY body - it is a pure
            // scan to the first marker/NUL; the tail write zeroes
            // THAT ONE byte, killing the marker)
            var q3 = 0
            while (q3 < bd.size && bd[q3] != '\u0001' && bd[q3] != NUL_D) {
                q3++
            }
            if (q3 < bd.size) bd[q3] = NUL_D // bd[j] = 0

            unpadDaf(bd, 0, svPtr)

            if (DafCRuntime.cstring(bd).isEmpty()) {
                if (g == NEXT_D) {
                    bd[0] = '-'; bd[1] = '1'; bd[2] = NUL_D
                } else {
                    bd[0] = NUL_D
                }
                return 0
            }
            return 1
        }
    }

    // if (g == GKILL) { int i = btree(...); return i; }
    if (g == KILL_D) { // vendor GKILL 3 == KILL_D 3
        return btreeDaf(g, key, bd, svPtr)
    }

    // if (g == XNEXT) { keyfix(key); i = btree(...); return i; }
    if (g == XNEXT_D) {
        keyfixDaf(key)
        return btreeDaf(g, key, bd, svPtr)
    }

    // if (g == CLOSE) { btree(...); return EXIT_SUCCESS; }
    if (g == CLOSE_D) {
        btreeDaf(g, key, bd, svPtr)
        return 0 // EXIT_SUCCESS
    }

    return 0
}
