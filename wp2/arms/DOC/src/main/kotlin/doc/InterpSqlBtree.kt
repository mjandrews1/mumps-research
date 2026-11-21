package doc

// Unit 237 (B8 tranche 3): interp-sql.cpp::btree (batch 8, complex
// stratum; units.csv row 217: loc 443, score 316.3, rank 293).
// The SQLITE-side global-array workhorse: the SQL statement builder
// behind the whole Mglobal surface of the reference build (the
// mumps-sqlite3 tree). DISTINCT-TWIN NOTE: the landed unit-4
// render (Btree.kt, interp-native-btree.cpp::btree) owns the
// vendor btree lexeme with the IDENTICAL Kotlin signature
// (Int/CharArray/CharArray/Msv); Kotlin cannot host two
// same-signature top-level funs in one package, so this render
// takes the Patrn1Interp/PatrnSys1 domain-suffix disambiguation:
// btreeSql names the interp-sql declaration (H-016 name-adaptation
// ledger; the vendor name btree stays cited at every vendor-
// statement mirror comment below). The native twin is NOT
// regraded (the u186 no-regrade rule; its btreeX delegation and
// H-018 NATIVE_SHARED ledger stand as landed).
//
// g-code roster (defines.h:162-188, the sql-dispatch family; all
// third same-value define notes ride defines.h:83-93 first family
// where they coincide): RETRIEVE 0 / STORE 1 / NEXT 2 / GKILL 3 /
// CLOSE 4 (file-private per the u150 CLOSE_MGV precedent) /
// XNEXT 8 / ORDERNEXT 12 / ORDERPREV 13 (file-private per the
// unit-214 ORDERNEXT_M6 precedent) / GBLNME 20 (defines.h:245;
// the unit-96 GBLNME_SI/GBLNME_ZB family) / GLOBAL 23 /
// OPEN 206 (defines.h:169; the Fcn.kt 206 OPEN call precedent) /
// SQLCMD 55 (defines.h:181). OK 1 rides the interp-sql.cpp:38
// sql-side define (file-private; same value as GLOBAL_SUCCESS,
// defines.h:186 - the third same-value note). Carriers consts
// (RETRIEVE/STORE/NEXT/GKILL/XNEXT) fold by value identity.
//
// H-018 EXCLUSION LADDERS: the XNEXT arm's "#if 14==N" block is
// a compile-time ladder over the configure column count; the
// reference tree is configured 14, so the LIVE arm is #if 14==14
// and the other twelve arms are compiled out (restated below as
// the comment ledger, the vendor bytes preserved). The GKILL
// arm's local "int tokens = 0" RE-SHADOWS the outer count (the
// tokens==1 true branch is dead in the vendor; H-013 kept-code
// render with quirk note). The XNEXT arm's strcpy(key, keybak)
// reads UNINITIALIZED keybak (vendor UB) - kept as the H-013
// unreachable-assignment ledger note; the subsequent
// strcpy(key, rsltSpec) is the effective statement.
//
// Seats: sql rides the Stubs.sql 6-arg seat (the vendor sql(int,
// MSV*, char*, char*, char*, const char*) dispatcher; the static
// char[2] TOK tables X01/XNULL/XTAB/XCMD are NUL-terminated C
// strings by construction and ride the seat's String tail as
// "\u0001"/""/"\t"/"\n"; the Fcn.kt Kann-and-XTAB precedent);
// "mumps" data source names ride the toBt String->CharArray seat
// adapter (the unit-202 toZb precedent, per-file here per the
// toZb private precedent). keyfix rides Stubs.keyfix (u167);
// lcheck/dcheck are the landed T2 bodies (InterpSqlMisc.kt);
// embed is the landed render (Embed.kt). rsltSpec is the
// InterpSqlMiscState holder (H-004 static keep, the XNEXT
// result-interface read). The block-static bdx[256] of the STORE
// insert sub-block rides a private holder object (the H-004
// CommandPrintState precedent). sigint(100) banners keep the
// stderr System.err precedent of the T2 lcheck render. strtok over the SOH (char 1) delimiter renders
// as the non-destructive index walker below (skip-empty/leading
// delimiters per the C strtok semantics; tokens handed to
// lcheck/embed as NUL-terminated CharArray copies at offset 0,
// the H-012 pointer-shift convention). sprintf "%d" keeps the
// Locale.US carrier per H-009. Consult H-018 hit-adapt.

private const val CLOSE_BT = 4 // defines.h:85 CLOSE (Mglobal close code)
private const val ORDERNEXT_BT = 12 // defines.h:170
private const val ORDERPREV_BT = 13 // defines.h:171
private const val GBLNME_BT = 20 // defines.h:245
private const val GLOBAL_BT = 23 // defines.h:93
private const val OPEN_BT = 206 // defines.h:169
private const val SQLCMD_BT = 55 // defines.h:181
private const val OK_BT = 1 // interp-sql.cpp:38 (#define OK 1)

// static char X01[2]={1,0}; XNULL[2]={0,0}; XTAB[2]={'\t',0};
// XCMD[2]={10,0} - NUL-terminated one-char C strings riding the
// Stubs.sql String tail (per the Fcn.kt XTAB precedent).
private const val X01_BT = "\u0001"
private const val XNULL_BT = ""
private const val XTAB_BT = "\t"
private const val XCMD_BT = "\n"

// static char *nums[33] - the sql column-name table; the first
// entry is the dummy (" ") per the vendor comment.
private val NUMS_BT = arrayOf(
    " ", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10",
    "a11", "a12", "a13", "a14", "a15", "a16", "a17", "a18", "a19",
    "a20", "a21", "a22", "a23", "a24", "a25", "a26", "a27", "a28",
    "a29", "a30", "a31", "a32"
)

// (char *) "mumps" and (char *) "" literal arguments of the vendor
// sql call sites ride the toZb String->STR_MAX-CharArray adapter
// idiom (unit-202 precedent, per-file private here).
private fun toBt(s: String): CharArray {
    val b = CharArray(STR_MAX)
    CstringOps.copyAtS(b, 0, s)
    return b
}

// The block-static unsigned char bdx[256] of the STORE insert
// sub-block (H-004 function-static keep).
private object BtreeStoreState {
    val bdx = CharArray(256)
}

// strtok(save, delim) over the SOH (char 1) delimiter: the C
// semantics walker. next() returns the START INDEX of the next
// token in the buffer or -1 at exhaustion (the NULL arm);
// consecutive/leading delimiters collapse and NUL ends the
// scan, exactly as strtok. tok()/tokS() hand out the
// NUL-terminated CharArray (for lcheck/dcheck) and the plain
// String (for embed) views of that token slice.
private class BtreeToks(val buf: CharArray) {
    var pos = 0

    fun next(): Int {
        while (pos < buf.size && (buf[pos] == NUL || buf[pos] == CHAR_1)) pos++
        if (pos >= buf.size || buf[pos] == NUL) return -1
        val start = pos
        while (pos < buf.size && buf[pos] != NUL && buf[pos] != CHAR_1) pos++
        return start
    }

    fun tok(start: Int, len: Int): CharArray {
        val t = CharArray(len + 1)
        for (k in 0 until len) t[k] = buf[start + k]
        t[len] = NUL
        return t
    }

    fun tokS(start: Int, len: Int): String = CstringOps.cstringAt(buf, start)

    // manual strlen of the token slice at start (char* p1 domain)
    fun lenOf(start: Int): Int {
        var n = 0
        while (start + n < buf.size && buf[start + n] != NUL &&
            buf[start + n] != CHAR_1) n++
        return n
    }
}

// int btree(int g, unsigned char key[], unsigned char bd[],
//           struct MSV * svPtr)
fun btreeSql(g: Int, key: CharArray, bd: CharArray, svPtr: Msv): Int {
    var gX = g // the vendor reassigns g (NEXT->XNEXT); Kotlin params
    // are immutable, so the mutable mirror carries the reassignment.

    val cmd = CharArray(STR_MAX)
    var p1: Int
    var tokens = 0
    val tmp1 = CharArray(32)

    //-----------------------------
    // make sure connection is open
    //-----------------------------

    // sql(OPEN, svPtr, "mumps", tmp1, tmp1, XCMD); the vendor passes
    // the SAME tmp1 scratch as both Command and e arguments (the
    // alias is the vendor's own - kept verbatim in the call shape).
    Stubs.sql(OPEN_BT, svPtr, toBt("mumps"), tmp1, tmp1, XCMD_BT)

    // if (g == NEXT) g = XNEXT;
    if (gX == NEXT) gX = XNEXT

    //--------------------
    // user command
    //--------------------

    // if (g == SQLCMD) return sql(SQLCMD, svPtr, "mumps", key, bd, XCMD);
    if (gX == SQLCMD_BT) {
        return Stubs.sql(SQLCMD_BT, svPtr, toBt("mumps"), key, bd, XCMD_BT)
    }

    //------------------------
    // count indices
    //------------------------

    // for (p1 = (char *) key; *p1; p1++) if (*p1 == 1) tokens++;
    {
        var kx = 0
        while (kx < key.size && key[kx] != NUL) {
            if (key[kx] == CHAR_1) tokens++
            kx++
        }
    }

    //------------------------------------------------------------------------
    // RETRIEVE
    //------------------------------------------------------------------------

    if (g == RETRIEVE) {
        val save = CharArray(STR_MAX)
        var i: Int
        val toks = BtreeToks(save)

        // if (tokens - 1 > 14 - 2) { fprintf(stderr, ...); sigint(100); }
        if (tokens - 1 > 14 - 2) {
            System.err.print(
                "\n*** 001 Maximum number if global" +
                    " array indices exceeded\n" +
                    "in or near line " + svPtr.LineNumber + "\n"
            )
            sigint(100)
        }

        // strcpy(save, (const char *)key);
        CstringOps.strcpyC(save, key)

        p1 = toks.next() // p1 = strtok(save, delim);

        // strcpy(cmd, "select "); strcat(cmd, nums[14]); strcat(cmd, " from mumps ");
        CstringOps.strcpyS(cmd, "select ")
        CstringOps.catAt(cmd, 0, NUMS_BT[14])
        CstringOps.catAt(cmd, 0, " from mumps ")

        // strcat(cmd, " where a1='");
        CstringOps.catAt(cmd, 0, " where a1='")

        // if (p1 != NULL) { lcheck(p1, svPtr); strcat(cmd, embed(p1)); strcat(cmd, "' "); }
        if (p1 != -1) {
            val ln = toks.lenOf(p1)
            lcheck(toks.tok(p1, ln), svPtr)
            CstringOps.catAt(cmd, 0, embed(toks.tokS(p1, ln)))
            CstringOps.catAt(cmd, 0, "' ")
        } else {
            // else strcat(cmd, "' "); // no indices to global provided
            CstringOps.catAt(cmd, 0, "' ")
        }

        // for (i = 2; i < 14 - 2; i++) { if ((p1 = strtok(NULL, delim)) == NULL) break; ... }
        i = 2
        while (i < 14 - 2) {
            p1 = toks.next()
            if (p1 == -1) break

            val ln = toks.lenOf(p1)
            lcheck(toks.tok(p1, ln), svPtr)
            CstringOps.catAt(cmd, 0, " and ")
            CstringOps.catAt(cmd, 0, NUMS_BT[i])
            CstringOps.catAt(cmd, 0, "='")
            CstringOps.catAt(cmd, 0, embed(toks.tokS(p1, ln)))
            CstringOps.catAt(cmd, 0, "'")
            i++
        }

        // for (; i < 14 - 2; i++) { strcat(cmd, " and "); strcat(cmd, nums[i]); strcat(cmd, "=''"); }
        while (i < 14 - 2) {
            CstringOps.catAt(cmd, 0, " and ")
            CstringOps.catAt(cmd, 0, NUMS_BT[i])
            CstringOps.catAt(cmd, 0, "=''")
            i++
        }

        // strcat(cmd, " limit 1;");
        CstringOps.catAt(cmd, 0, " limit 1;")

        // bd[0] = 0; // empty string
        bd[0] = NUL

        // i = sql(RETRIEVE, svPtr, "mumps", cmd, bd, XTAB); return i;
        i = Stubs.sql(RETRIEVE, svPtr, toBt("mumps"), cmd, bd, XTAB_BT)
        return i
    }

    //------------------------------------------------------------------------
    // STORE - a DELETE then a STORE
    //------------------------------------------------------------------------

    if (g == STORE) {
        val save = CharArray(STR_MAX)
        var i: Int

        // if (tokens - 1 > 14 - 2) { fprintf(stderr, ...); sigint(100); }
        if (tokens - 1 > 14 - 2) {
            System.err.print(
                "\n*** 001 xMaximum number " +
                    "if global array indices exceeded\n" +
                    "in or near line " + svPtr.LineNumber + "\n"
            )
            sigint(100)
        }

        // strcpy(save, (const char *)key);
        CstringOps.strcpyC(save, key)
        var toks = BtreeToks(save)

        //---------------------------------------
        // delete any prior value this key
        //---------------------------------------

        // strcpy(cmd, "delete "); strcat(cmd, " from mumps "); strcat(cmd, " where a1='");
        CstringOps.strcpyS(cmd, "delete ")
        CstringOps.catAt(cmd, 0, " from mumps ")
        CstringOps.catAt(cmd, 0, " where a1='")

        p1 = toks.next() // p1 = strtok(save, delim);

        // if (p1 != NULL) { lcheck(p1, svPtr); strcat(cmd, embed(p1)); strcat(cmd, "' "); }
        if (p1 != -1) {
            val ln = toks.lenOf(p1)
            lcheck(toks.tok(p1, ln), svPtr)
            CstringOps.catAt(cmd, 0, embed(toks.tokS(p1, ln)))
            CstringOps.catAt(cmd, 0, "' ")
        } else {
            // else strcat(cmd, "' "); // no indices to global provided
            CstringOps.catAt(cmd, 0, "' ")
        }

        // for (i = 2; i < 14; i++) { if ((p1 = strtok(NULL, delim)) == NULL) break; ... }
        i = 2
        while (i < 14) {
            p1 = toks.next()
            if (p1 == -1) break

            val ln = toks.lenOf(p1)
            lcheck(toks.tok(p1, ln), svPtr)
            CstringOps.catAt(cmd, 0, " and ")
            CstringOps.catAt(cmd, 0, NUMS_BT[i])
            CstringOps.catAt(cmd, 0, "='")
            CstringOps.catAt(cmd, 0, embed(toks.tokS(p1, ln)))
            CstringOps.catAt(cmd, 0, "'")
            i++
        }

        // for (; i < 14; i++) { strcat(cmd, " and "); strcat(cmd, nums[i]); strcat(cmd, "=''"); }
        while (i < 14) {
            CstringOps.catAt(cmd, 0, " and ")
            CstringOps.catAt(cmd, 0, NUMS_BT[i])
            CstringOps.catAt(cmd, 0, "=''")
            i++
        }

        // unsigned char bdx[128]; i = sql(g, svPtr, "mumps", cmd, bdx, XTAB);
        val bdx1 = CharArray(128)
        i = Stubs.sql(g, svPtr, toBt("mumps"), cmd, bdx1, XTAB_BT)

        //--------------------------------------------
        // STORE new or replacement key
        //--------------------------------------------

        run {
            var i2: Int
            val cmd1 = CharArray(STR_MAX * 2)
            var px: Int

            // if (tokens - 1 > 14 - 2) { fprintf(stderr, ...); sigint(100); }
            if (tokens - 1 > 14 - 2) {
                System.err.print(
                    "\n*** 002 Maximum number if global array indices exceeded\n" +
                        "in or near line " + svPtr.LineNumber + "\n"
                )
                sigint(100)
            }

            // strcpy(save, (const char *)key); (the vendor repeats it)
            CstringOps.strcpyC(save, key)

            // strcpy(save, (char *) key); p1 = strtok(save, (char *) delim);
            toks = BtreeToks(save)
            p1 = toks.next()

            // sprintf(cmd, "insert into mumps values ('%s', ", embed(p1));
            // (the vendor keeps the commented replace-into line above it,
            // H-013 comment ledger: // sprintf(cmd, "replace into mumps values ('%s', ", embed(p1));)
            CstringOps.strcpyS(
                cmd, "insert into mumps values ('" +
                    embed(toks.tokS(p1, toks.lenOf(p1))) + "', "
            )

            // j = strlen(cmd); p3 = &cmd[j];
            px = CstringOps.strlenC(cmd)

            // for (i = 2; i < 14; i++) { p1 = strtok(NULL, delim); if (p1 == NULL) break; ... }
            i2 = 2
            while (i2 < 14) {
                p1 = toks.next()
                if (p1 == -1) break

                val ln = toks.lenOf(p1)
                lcheck(toks.tok(p1, ln), svPtr)
                val p2 = embed(toks.tokS(p1, ln)) // p2 = embed(p1);
                cmd[px++] = '\''
                for (c in p2) {
                    cmd[px++] = c
                }
                cmd[px++] = '\''
                cmd[px++] = ','
                cmd[px++] = ' '
                cmd[px] = NUL // *p3 = 0;
                i2++
            }

            // for (; i < 14; i++) { // off count but final val in bd
            //     *(p3++) = '\''; *(p3++) = '\''; *(p3++) = ','; *(p3++) = ' '; }
            while (i2 < 14) {
                cmd[px++] = '\''
                cmd[px++] = '\''
                cmd[px++] = ','
                cmd[px++] = ' '
                cmd[px] = NUL
                i2++
            }

            cmd[px] = NUL // *(p3) = 0;

            // dcheck((char *) bd, svPtr); p2 = embed((char *) bd);
            dcheck(bd, svPtr)
            val p2 = embed(CstringOps.cstring(bd))
            cmd[px++] = '\''
            for (c in p2) {
                // while (*p2 != 0) *(p3++) = *(p2++);
                cmd[px++] = c
            }
            cmd[px++] = '\''
            cmd[px++] = ' '
            cmd[px++] = ')'
            cmd[px++] = ';'
            cmd[px] = NUL

            // strcpy(cmd1, cmd);
            CstringOps.strcpyC(cmd1, cmd)

            // static unsigned char bdx[256]; i = sql(g, svPtr, "mumps", cmd1, bdx, " ");
            val bdx2 = BtreeStoreState.bdx
            i2 = Stubs.sql(g, svPtr, toBt("mumps"), cmd1, bdx2, " ")

            // if (i == 0) { fprintf(stderr, ...); sigint(100); }
            if (i2 == 0) {
                System.err.print(
                    "\n*** SQL error reported during assignment " +
                        "to global array:\n*** " + CstringOps.cstring(svPtr.pgsql_msg) + "\n" +
                        "Command: " + CstringOps.cstring(cmd) + "\n"
                )
                sigint(100)
            }

            // return 1;
            return 1
        }
    }

    if (gX == ORDERNEXT_BT || gX == ORDERPREV_BT) {

        //-------------------------------------------------------------------------
        // select aX from mumps where a1='...' and a2='...' ... and aX>'...'
        // order by aX desc limit 1; / asc limit 1;
        //-------------------------------------------------------------------------

        var i: Int
        val svTable = svPtr.Table

        // keyfix(key);
        Stubs.keyfix(key)

        // if (tokens - 1 > 14 - 2) { return 0; // permits \u0024data to work at
        // this level; printf(...); sigint(100); } - the vendor return 0
        // PRECEDES the printf/sigint pair (dead rest, H-013 ledger).
        if (tokens - 1 > 14 - 2) {
            return 0 // permits \u0024data to work at this level
        }

        var px: Int
        if (tokens == 1) {
            // p1 = cmd + 15; strcpy(cmd, "select a1 from ");
            CstringOps.strcpyS(cmd, "select a1 from ")
            px = 15

            // for (char *p = svPtr->Table; *p; p++) *(p1++) = *p;
            {
                var kx = 0
                while (svTable[kx] != NUL) {
                    cmd[px++] = svTable[kx]
                    kx++
                }
            }

            // strcpy(p1, " where a1='"); p1 += 12; (the 12-shift skips one
            // char past the 11-char literal - vendor quirk kept)
            CstringOps.copyAtS(cmd, px, " where a1='")
            px += 12
        } else {
            // p1 = cmd + 7; strcpy(cmd, "select ");
            CstringOps.strcpyS(cmd, "select ")
            px = 7

            // for (char *p = nums[tokens]; *p; p++) *(p1++) = *p;
            for (c in NUMS_BT[tokens]) {
                cmd[px++] = c // col whose value we want
            }

            // strcpy(p1, " from "); p1 += 6;
            CstringOps.copyAtS(cmd, px, " from ")
            px += 6

            // for (char *p = svPtr->Table; *p; p++) *(p1++) = *p;
            {
                var kx = 0
                while (svTable[kx] != NUL) {
                    cmd[px++] = svTable[kx]
                    kx++
                }
            }

            // strcpy(p1, " where a1='"); p1 += 11;
            CstringOps.copyAtS(cmd, px, " where a1='")
            px += 11
        }

        // p3 = strtok((char *) key, delim);
        val toks = BtreeToks(key)
        p1 = toks.next()

        // for (p2 = embed(p3); *p2; p2++) *(p1++) = *p2;
        for (c in embed(toks.tokS(p1, toks.lenOf(p1)))) {
            cmd[px++] = c
        }

        // *(p1++) = '\''; *(p1++) = ' ';
        cmd[px++] = '\''
        cmd[px++] = ' '

        // for (i = 2; i < tokens; i++) { p3 = strtok(NULL, delim); if (p3 == NULL) break; ... }
        i = 2
        while (i < tokens) {
            val p3 = toks.next()
            if (p3 == -1) break

            // strcpy(p1, "and "); p1 += 4;
            CstringOps.copyAtS(cmd, px, "and ")
            px += 4

            // for (char *p = nums[i]; *p; p++) *(p1++) = *p;
            for (c in NUMS_BT[i]) {
                cmd[px++] = c
            }

            // *(p1++) = '='; *(p1++) = '\'';
            cmd[px++] = '='
            cmd[px++] = '\''

            // for (p2 = embed(p3); *p2; p2++) *(p1++) = *p2;
            for (c in embed(toks.tokS(p3, toks.lenOf(p3)))) {
                cmd[px++] = c
            }

            // *(p1++) = '\''; *(p1++) = ' ';
            cmd[px++] = '\''
            cmd[px++] = ' '
            i++
        }

        // p3 = strtok(NULL, delim); if (p3 != NULL) {
        val p3 = toks.next()
        if (p3 != -1) {

            // if (g == ORDERNEXT) { strcpy(p1, "and "); p1 += 4; ... '>' '\'' }
            // else { strcpy(p1, "and "); p1 += 4; ... '<' '\'' }
            if (gX == ORDERNEXT_BT) {
                CstringOps.copyAtS(cmd, px, "and ")
                px += 4
                for (c in NUMS_BT[i]) {
                    cmd[px++] = c
                }
                cmd[px++] = '>'
                cmd[px++] = '\''
            } else {
                CstringOps.copyAtS(cmd, px, "and ")
                px += 4
                for (c in NUMS_BT[i]) {
                    cmd[px++] = c
                }
                cmd[px++] = '<'
                cmd[px++] = '\''
            }

            // for (p2 = embed(p3); *p2; p2++) *(p1++) = *p2;
            for (c in embed(toks.tokS(p3, toks.lenOf(p3)))) {
                cmd[px++] = c
            }

            // *(p1++) = '\''; *(p1++) = ' ';
            cmd[px++] = '\''
            cmd[px++] = ' '
        } else {
            // next requires a comp operator for empty string
            // (the ORDERPREV NULL arm is EMPTY in the vendor - only
            // the ORDERNEXT branch builds; the commented cast( pair
            // stays the H-013 comment ledger.)
            if (gX == ORDERNEXT_BT) {
                CstringOps.copyAtS(cmd, px, "and ")
                px += 4
                for (c in NUMS_BT[i]) {
                    cmd[px++] = c
                }
                cmd[px++] = '>'
                cmd[px++] = '\''
                cmd[px++] = '\''
                cmd[px++] = ' '
            }
        }

        // char order[64];
        if (tokens == 1) {
            // if (g == ORDERNEXT) strcpy(p1, " order by a1 asc limit 1;");
            // else strcpy(p1, " order by a1 desc limit 1;");
            if (gX == ORDERNEXT_BT) {
                CstringOps.copyAtS(cmd, px, " order by a1 asc limit 1;")
            } else {
                CstringOps.copyAtS(cmd, px, " order by a1 desc limit 1;")
            }
        } else if (gX == ORDERNEXT_BT) {
            // strcpy(p1, "order by "); p1 += 9; nums[tokens]; " asc limit 1;"
            CstringOps.copyAtS(cmd, px, "order by ")
            px += 9
            for (c in NUMS_BT[tokens]) {
                cmd[px++] = c
            }
            CstringOps.copyAtS(cmd, px, " asc limit 1;")
        } else {
            // strcpy(p1, "order by "); p1 += 9; nums[tokens]; " desc limit 1;"
            CstringOps.copyAtS(cmd, px, "order by ")
            px += 9
            for (c in NUMS_BT[tokens]) {
                cmd[px++] = c
            }
            CstringOps.copyAtS(cmd, px, " desc limit 1;")
        }

        // i = sql(GLOBAL, svPtr, "mumps", cmd, bd, XNULL); return i;
        i = Stubs.sql(GLOBAL_BT, svPtr, toBt("mumps"), cmd, bd, XNULL_BT)
        return i
    }

    //------------------------------------------------------------------------
    // NEXT used by \u0024query
    //------------------------------------------------------------------------

    if (g == XNEXT) {
        var i: Int

        // if (tokens - 1 > 14 - 2) { fprintf(stderr, ...); sigint(100); }
        if (tokens - 1 > 14 - 2) {
            System.err.print(
                "\n*** 003 Maximum number if global array indices exceeded\n" +
                    "in or near line " + svPtr.LineNumber + "\n"
            )
            sigint(100)
        }

        // p1 = strtok((char *) key, delim);
        val toks = BtreeToks(key)
        p1 = toks.next()

        // H-018 EXCLUSION LADDER: the vendor "#if 14==N" block selects
        // on the configure column count (the reference tree is 14):
        //   #if 14==4  strcpy(cmd, "select a1,a2,a3 from mumps where a1=\"" ... );
        //   #if 14==5  strcpy(cmd, "select a1,a2,a3,a4 from mumps where a1=\"" ...);
        //   #if 14==6  strcpy(cmd, "select a1,a2,a3,a4,a5 from mumps where a1=\"" ...);
        //   #if 14==7  strcpy(cmd, "select a1,a2,a3,a4,a5,a6 from mumps where a1=\"" ...);
        //   #if 14==8  strcpy(cmd, "select a1,...,a7 from mumps where a1=\"" ...);
        //   #if 14==9  strcpy(cmd, "select a1,...,a8 from mumps where a1=\"" ...);
        //   #if 14==10 strcpy(cmd, "select a1,...,a9 from mumps where a1=\"" ...);
        //   #if 14==11 strcpy(cmd, "select a1,...,a10 from mumps where a1=\"" ...);
        //   #if 14==12 strcpy(cmd, "select a1,...,a11 from mumps where a1=\"" ...);
        //   #if 14==13 strcpy(cmd, "select a1,...,a12 from mumps where a1=\"" ...);
        //   #if 14==15 strcpy(cmd, "select a1,...,a14 from mumps where a1=\"" ...);
        //   #if 14==16 strcpy(cmd, "select a1,...,a15 from mumps where a1=\"" ...);
        //   #if 14==17 strcpy(cmd, "select a1,...,a16 from mumps where a1=\"" ...);
        //   #if 14>=18 #error Too many global array index levels #endif
        // The LIVE arm is the 14==14 one:
        //   strcpy(cmd, "select a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13
        //     from mumps where a1=\""); strcat(cmd, p1); strcat(cmd,
        //     "\" and (a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13) > (");
        CstringOps.strcpyS(
            cmd, "select a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13" +
                " from mumps where a1=\"" + toks.tokS(p1, toks.lenOf(p1)) +
                "\" and (a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13) > ("
        )

        // p1 = strtok(NULL, delim); // get another
        p1 = toks.next()

        // for (i = 2; i <= tokens; i++) { if (p1 == NULL) strcat(cmd, "'',");
        //   else { strcat(cmd, "'"); lcheck(p1, svPtr); strcat(cmd, embed(p1));
        //   strcat(cmd, "',"); p1 = strtok(NULL, delim); } }
        i = 2
        while (i <= tokens) {
            if (p1 == -1) {
                CstringOps.catAt(cmd, 0, "'',")
            } else {
                val ln = toks.lenOf(p1)
                CstringOps.catAt(cmd, 0, "'")
                lcheck(toks.tok(p1, ln), svPtr)
                CstringOps.catAt(cmd, 0, embed(toks.tokS(p1, ln)))
                CstringOps.catAt(cmd, 0, "',")
                p1 = toks.next() // get another
            }
            i++
        }

        // if (p1 == NULL) { strcat(cmd, "''"); } else { strcat(cmd, "'");
        //   lcheck(p1, svPtr); strcat(cmd, embed(p1)); strcat(cmd, "'"); }
        if (p1 == -1) {
            CstringOps.catAt(cmd, 0, "''")
        } else {
            val ln = toks.lenOf(p1)
            CstringOps.catAt(cmd, 0, "'")
            lcheck(toks.tok(p1, ln), svPtr)
            CstringOps.catAt(cmd, 0, embed(toks.tokS(p1, ln)))
            CstringOps.catAt(cmd, 0, "'")
        }

        // for (; i < 14 - 1; i++) strcat(cmd, ",''");
        while (i < 14 - 1) {
            CstringOps.catAt(cmd, 0, ",''")
            i++
        }

        // H-018 EXCLUSION LADDER (order-by tail): the live 14==14 arm
        // is ") order by a2,a3,a4,a5,a6,a7,a9,a10,a11,a12,a13,a14" (the
        // vendor a8 SKIP is a quirk kept); the compiled-out arms:
        //   #if 14==4  strcat(cmd, ") order by a2,a3");
        //   #if 14==5  strcat(cmd, ") order by a2,a3,a4");
        //   #if 14==6  strcat(cmd, ") order by a2,a3,a4,a5");
        //   #if 14==7  strcat(cmd, ") order by a2,a3,a4,a5,a6");
        //   #if 14==8  strcat(cmd, ") order by a2,a3,a4,a5,a6,a7");
        //   #if 14==9  strcat(cmd, ") order by a2,a3,a4,a5,a6,a7,a9");
        //   #if 14==10 strcat(cmd, ") order by a2,a3,a4,a5,a6,a7,a9,a10");
        //   #if 14==11 strcat(cmd, ") order by a2,a3,a4,a5,a6,a7,a9,a10,a11");
        //   #if 14==12 strcat(cmd, ") order by ...,a11,a12");
        //   #if 14==13 strcat(cmd, ") order by ...,a12,a13");
        //   #if 14==15 strcat(cmd, ") order by ...,a14,a15");
        //   #if 14==16 strcat(cmd, ") order by ...,a15,a16");
        //   #if 14==17 strcat(cmd, ") order by ...,a16,a17");
        CstringOps.catAt(
            cmd, 0,
            ") order by a2,a3,a4,a5,a6,a7,a9,a10,a11,a12,a13,a14"
        )

        // for (i = 2; i < tokens - 1; i++) { sprintf(tmp, ", a%d", i + 1);
        //   strcat(cmd, tmp); } - the vendor RESETS i to 2 here and
        // appends these AFTER the order-by tail (duplicate-column
        // quirk kept).
        i = 2
        while (i < tokens - 1) {
            CstringOps.catAt(cmd, 0, String.format(java.util.Locale.US, ", a%d", i + 1))
            i++
        }

        // strcat(cmd, " limit 1;");
        CstringOps.catAt(cmd, 0, " limit 1;")

        // strcpy((char *) key, keybak); - keybak is UNINITIALIZED at
        // this point (vendor undefined behavior; H-013 ledger - the
        // statement is kept as this note, NOT translated; the
        // effective statement is the rsltSpec copy below).

        // i = sql(g, svPtr, "mumps", cmd, bd, X01);
        i = Stubs.sql(gX, svPtr, toBt("mumps"), cmd, bd, X01_BT)

        // strcpy((char *) key, (char *) rsltSpec);
        CstringOps.strcpyC(key, InterpSqlMiscState.rsltSpec)

        // return i;
        return i
    }

    //------------------------------------------------------------------------
    // DELETE / GKILL
    //------------------------------------------------------------------------

    if (g == GKILL) {
        var i: Int
        var tokensK = 0 // int tokens = 0; (local shadow of the outer
        // count - the vendor's tokens==1 branch is DEAD because of
        // it; H-013 quirk kept)

        // if (tokens - 1 > 14 - 2) { fprintf(stderr, ...); sigint(100); }
        if (tokensK - 1 > 14 - 2) {
            System.err.print(
                "\n*** 003 Maximum number if global array indices exceeded\n" +
                    "in or near line " + svPtr.LineNumber + "\n"
            )
            sigint(100)
        }

        // strcpy(cmd, "delete from mumps "); (the commented
        // sprintf(cmd, "delete from mumps ", svPtr->Table) stays the
        // H-013 comment ledger)
        CstringOps.strcpyS(cmd, "delete from mumps ")

        // if (tokens == 1) { strcat(cmd, "where a1='"); p3 = strtok(key, delim);
        //   strcat(cmd, p3); strcat(cmd, "';"); } - DEAD ARM in the vendor
        // (tokensK is always 0); kept verbatim with this note.
        if (tokensK == 1) {
            CstringOps.catAt(cmd, 0, "where a1='")
            val toksK = BtreeToks(key)
            val p3 = toksK.next() // array name
            if (p3 != -1) {
                CstringOps.catAt(cmd, 0, toksK.tokS(p3, toksK.lenOf(p3)))
            }
            CstringOps.catAt(cmd, 0, "';")
        } else {
            // strcat(cmd, "where "); p3 = strtok(key, delim); // array name
            CstringOps.catAt(cmd, 0, "where ")
            val toksK = BtreeToks(key)
            var p3 = toksK.next()

            // for (i = 1; i < 14 - 2; i++) { if (p3 == NULL) break;
            //   if (i != 1) strcat(cmd, " and "); strcat(cmd, nums[i]);
            //   strcat(cmd, "='"); lcheck(p3, svPtr); strcat(cmd, embed(p3));
            //   strcat(cmd, "' "); p3 = strtok(NULL, delim); }
            i = 1
            while (i < 14 - 2) {
                if (p3 == -1) break

                if (i != 1) CstringOps.catAt(cmd, 0, " and ")
                CstringOps.catAt(cmd, 0, NUMS_BT[i])
                CstringOps.catAt(cmd, 0, "='")
                val ln = toksK.lenOf(p3)
                lcheck(toksK.tok(p3, ln), svPtr)
                CstringOps.catAt(cmd, 0, embed(toksK.tokS(p3, ln)))
                CstringOps.catAt(cmd, 0, "' ")
                p3 = toksK.next()
                i++
            }

            // strcat(cmd, ";");
            CstringOps.catAt(cmd, 0, ";")
        }

        // i = sql(g, svPtr, "mumps", cmd, bd, X01); return i;
        i = Stubs.sql(gX, svPtr, toBt("mumps"), cmd, bd, X01_BT)
        return i
    }

    //-------------------------------
    // GLOBAL NAMES
    //-------------------------------

    if (g == GBLNME_BT) {
        // p1 = cmd + 15; strcpy(cmd, "select a1 from ");
        CstringOps.strcpyS(cmd, "select a1 from ")
        var px = 15

        // for (char *p = svPtr->Table; *p; p++) *(p1++) = *p;
        {
            var kx = 0
            while (svPtr.Table[kx] != NUL) {
                cmd[px++] = svPtr.Table[kx]
                kx++
            }
        }

        // strcpy(p1, " where a1>'"); strcat(p1, (char *) key);
        // strcat(p1, "' order by a1 limit 1;");
        CstringOps.copyAtS(cmd, px, " where a1>'")
        CstringOps.catAt(cmd, px, CstringOps.cstring(key))
        CstringOps.catAt(cmd, px, "' order by a1 limit 1;")

        // bd[0] = 0; // empty string
        bd[0] = NUL

        // i = sql(RETRIEVE, svPtr, "mumps", cmd, bd, XTAB); return i;
        return Stubs.sql(RETRIEVE, svPtr, toBt("mumps"), cmd, bd, XTAB_BT)
    }

    //-------------------------
    // CLOSE
    //-------------------------

    if (g == CLOSE_BT) {
        // sql(CLOSE, svPtr, "", "", "", XTAB);
        Stubs.sql(CLOSE_BT, svPtr, toBt(""), toBt(""), toBt(""), XTAB_BT)

        // return OK;
        return OK_BT
    }

    // return OK;
    return OK_BT
}
