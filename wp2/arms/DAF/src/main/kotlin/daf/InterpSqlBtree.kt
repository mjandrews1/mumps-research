package daf

// Unit 246 (DAF B8 T3, complex): interp-sql.cpp::btree (443 LOC,
// score 316.3): the SQLITE-side global-array workhorse - the SQL
// statement builder behind the whole Mglobal surface of the
// reference build (the mumps-sqlite3 tree; the SQLITE define is
// live per the reference-build config, the unit-110/113/235
// ledger seat).
// Consults (2026-11-24T21:25:00Z, nominal): rows 6/22 (the
// block-static bdx[256] holder keep; rsltSpec rides the landed
// unit-208 RsltCallback rsltSpecD seat), row 90 (Mglobal's every
// call binds the same name: this render RELEASES the btreeDaf
// stub of Mglobal.kt - TAG CORRECTION there: the stub blame
// "interp-native-btree.cpp::btree at 293, not a batch cell" was
// wrong; the reference build's btree symbol IS this
// interp-sql.cpp body and the roster row is THIS batch, unit
// 246; the native twin is another arm's corpus unit and is NOT
// drawn here), rows 56/112 hit-adapt (the vendor strtok walks are
// destructive on the key carrier in C; the render tokenizes
// non-destructively - DOCUMENTED DIVERGENCE, the DOC-arm
// precedent; the p1+=12 gap byte of the tokens==1 arm is
// indeterminate in the vendor - modeled as a NUL appends, H-013
// ledger), row 18 (the CharArray out carrier rides the sqlDaf
// CharArray-overload seat raised with this unit below).
// Dafny: EXEMPT (SQL statement shaping over the sql/embed/
// sigint/keyfix seats; journal row).
//
// g-code roster (defines.h:162-188 plus file-private defines;
// values physically verified this session): RETRIEVE 0 /
// STORE 1 / NEXT 2 / GKILL 3 / CLOSE 4 (CLOSE_D)/ XNEXT 8 /
// ORDERNEXT 12 / ORDERPREV 13 (ORDERPREV_D)/ GBLNME 20 /
// GLOBAL 23 / OPEN 206 (SQL_OPEN_D; Interpret.kt OPEN_MGI = 206,
// same value) / SQLCMD 55; OK 1 rides the interp-sql.cpp:38
// sql-side define (same value as defines.h GLOBAL_SUCCESS 1, the
// third same-value note).

private const val GBLNME_BT_D = 20  // defines.h:245 GBLNME
private const val GLOBAL_BT_D = 23 // defines.h GLOBAL
private const val SQLCMD_BT_D = 55 // defines.h:181 SQLCMD
private const val OK_BT_D = 1       // interp-sql.cpp:38 OK

// TOK tables: static char[2] NUL-terminated by construction; the
// sqlDaf String-tail seat reads them as "\u0001"/""/"\t"/"\n"
// (the Fcn.kt Kann-and-XTAB precedent).
private const val X01_BT = "\u0001"   // static char X01[2] = {1, 0}
private const val XNULL_BT = ""       // static char XNULL[2] = {0, 0}
private const val XTAB_BT = "\t"      // static char XTAB[2] = {'\t', 0}
private const val XCMD_BT = "\n"      // static char XCMD[2] = {10, 0}

// The column-name roster (the first one is a dummy; 32 columns;
// column names begin with a1 - vendor comment kept in place).
private val numsBT = arrayOf(
    " ", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9",
    "a10", "a11", "a12", "a13", "a14", "a15", "a16", "a17", "a18",
    "a19", "a20", "a21", "a22", "a23", "a24", "a25", "a26", "a27",
    "a28", "a29", "a30", "a31", "a32")

// non-destructive strtok twin over the SOH (char 1) delimiter: C
// strtok skips empty tokens; DOCUMENTED DIVERGENCE: the C walk is
// destructive on the key carrier, this one reads it intact (the
// row-56/112 adaptation; ledger above).
class TokWalkerBT(key: CharArray) {
    private val toks = DafCRuntime.cstring(key)
        .split(Char(1).toString())
        .filter { it.isNotEmpty() }
    private var pos = 0
    fun next(): String? = if (pos < toks.size) toks[pos++] else null
}

// block-static unsigned char bdx[256] of the STORE insert
// sub-block (rows 6/22 holder keep, the H-004 precedent).
private object BdxHolderBT {
    val bdx = CharArray(256)
}

// static unsigned char bdx[128] of the STORE delete leg (same
// discipline).
private val bdxDelBT = CharArray(128)

// sqlDaf CharArray OVERLOAD SEAT raised with this unit (owner
// interp-sql.cpp::sql, roster b9 - the sql real lands in batch 9;
// the vendor btree arms pass the bd/bdx OUT carriers, which the
// all-String stub signature of the T3 seat cannot carry): same
// owner blame, same release discipline as the String seat.
// sqlDaf CharArray OVERLOAD SEAT RELEASED batch 9 unit 271: real
// body at InterpSqlSql.kt (roster row 271, interp-sql.cpp::sql;
// the unit-246 raise note above is superseded - same owner, same
// release discipline as the String seat; the bd/bdx OUT carriers
// bind through the shared sqlCoreD core).

// char* embed(char*) - the single-quote-doubling escaper of the
// SQL TU (quote probe + doubling loop; NOT the embed1 twin of
// libmpscpp.cpp:4151 - that one doubles DOUBLE quotes and is the
// landed unit-210 Embed1.kt): roster b9 row - named-owner stub
// until that render.
fun embedDaf(in_: String?): String =
    throw NotImplementedError(
        "embed: out-of-scope owner (interp-sql.cpp::embed at 115; roster b9; not the unit-210 embed1 twin)")

fun btreeDaf(gIn: Int, key: CharArray, bd: CharArray, svPtr: DafMsv): Int {

    var g = gIn

    // make sure connection is open: sql(OPEN, svPtr, "mumps",
    // tmp1, tmp1, XCMD); the two tmp1 scratch carriers fold to
    // empty String outs on the stub seat (row-56 ledger).
    sqlDaf(SQL_OPEN_D, svPtr, "mumps", "", "", XCMD_BT)

    // if (g == NEXT) g = XNEXT;
    if (g == NEXT_D) g = XNEXT_D

    // ------------------ user command --------------------------
    if (g == SQLCMD_BT_D) {
        // return sql(SQLCMD, svPtr, "mumps", key, bd, XCMD);
        return sqlDaf(g, svPtr, "mumps",
            DafCRuntime.cstring(key), bd, XCMD_BT)
    }

    // ------------------ count indices -------------------------
    var tokens = 0
    var ix = 0
    while (key[ix] != NUL_D) {         // for (p1=key; *p1; p1++)
        if (key[ix] == Char(1)) tokens++ // if (*p1 == 1) tokens++;
        ix++
    }

    // ================== RETRIEVE ==============================
    if (g == RETRIEVE_D) {

        var tok = 0
        // if (tokens - 1 > 14 - 2) { fprintf(stderr, "*** 001
        //   Maximum number if global array indices exceeded ...");
        //   sigint(100); } - banner verbatim incl. the vendor's
        //   "number if" typo; *** 001.
        if (tokens - 1 > 14 - 2) {
            System.err.print("\n*** 001 Maximum number if global")
            System.err.print(" array indices exceeded\n")
            System.err.print("in or near line " + svPtr.LineNumber + "\n")
            sigintD(100)
        }

        val toks = TokWalkerBT(key)

        // select a14 from mumps where a1='...',a2='...',...
        val cmd = StringBuilder("select ") // strcpy(cmd,"select ")
        cmd.append(numsBT[14])             // strcat(cmd,nums[14])
        cmd.append(" from mumps ")         // strcat(cmd," from mumps ")

        cmd.append(" where a1='")          // strcat(cmd," where a1='")

        val p1 = toks.next() // p1 = strtok(save, delim);
        if (p1 != null) {
            lcheckD(p1.toCharArray(), svPtr)
            cmd.append(embedDaf(p1))       // strcat(cmd, embed(p1))
            cmd.append("' ")
        } else {
            cmd.append("' ")               // else strcat(cmd,"' ")
        }

        var i = 2
        while (i < 14 - 2) {              // for (i = 2; i < 14-2; i++)
            val t = toks.next()
            if (t == null) break
            lcheckD(t.toCharArray(), svPtr)
            cmd.append(" and ")
            cmd.append(numsBT[i])
            cmd.append("='")
            cmd.append(embedDaf(t))
            cmd.append("'")
            i++
        }
        // for (; i < 14-2; i++) pads WHERE with: and an=''
        while (i < 14 - 2) {
            cmd.append(" and ")
            cmd.append(numsBT[i])
            cmd.append("=''")
            i++
        }

        cmd.append(" limit 1;")
        bd[0] = NUL_D                      // bd[0] = 0; empty string
        // i = sql(RETRIEVE, svPtr, "mumps", cmd, bd, XTAB);
        return sqlDaf(RETRIEVE_D, svPtr, "mumps", cmd.toString(), bd, XTAB_BT)
    }

    // ================== STORE ==================================
    // STORE involves a DELETE and a STORE
    if (g == STORE_D) {

        // *** 001 xMaximum banner (the vendor "xMaximum" typo is
        // verbatim, STORE copy of the RETRIEVE banner).
        if (tokens - 1 > 14 - 2) {
            System.err.print("\n*** 001 xMaximum number ")
            System.err.print("if global array indices exceeded\n")
            System.err.print("in or near line " + svPtr.LineNumber + "\n")
            sigintD(100)
        }

        val toks = TokWalkerBT(key)

        // ------- delete any prior value this key -------
        val cmd = StringBuilder("delete ")  // strcpy(cmd,"delete ")
        cmd.append(" from mumps ")

        cmd.append(" where a1='")

        val p1 = toks.next() // p1 = strtok(save, delim);
        if (p1 != null) {
            lcheckD(p1.toCharArray(), svPtr)
            cmd.append(embedDaf(p1))
            cmd.append("' ")
        } else {
            cmd.append("' ")
        }

        var i = 2
        while (i < 14) {                    // for (i = 2; i < 14; i++)
            val t = toks.next()
            if (t == null) break
            lcheckD(t.toCharArray(), svPtr)
            cmd.append(" and ")
            cmd.append(numsBT[i])
            cmd.append("='")
            cmd.append(embedDaf(t))
            cmd.append("'")
            i++
        }
        while (i < 14) {                    // pad WHERE with: and an=''
            cmd.append(" and ")
            cmd.append(numsBT[i])
            cmd.append("=''")
            i++
        }

        // i = sql(g, svPtr, "mumps", cmd, bdx, XTAB);
        // (unsigned char bdx[128]; bdx is scratch - delete never
        // reads a result - so the CharArray discard ride is the
        // row-18 fold)
        sqlDaf(g, svPtr, "mumps", cmd.toString(), bdxDelBT, XTAB_BT)

        // ------- STORE new or replacement key -------
        // strcpy(save, (char *) key); - the insert sub-block
        // re-copies and re-tokenizes (vendor keep; *** 002 copy
        // of the two banners; the sprintf "replace into"
        // fossil comment is dead - the live line is the "insert
        // into" sprintf, kept as the comment fossil).
        if (tokens - 1 > 14 - 2) {
            System.err.print("\n*** 002 Maximum number if global array indices exceeded\n")
            System.err.print("in or near line " + svPtr.LineNumber + "\n")
            sigintD(100)
        }

        val toks2 = TokWalkerBT(key)    // strcpy(save, key); strtok fresh
        val q1 = toks2.next()           // p1 = strtok(save, delim);

        // sprintf(cmd, "insert into mumps values ('%s', ",
        //   embed(p1)); - the p3 pointer walk folds onto the
        // String builder (H-010).
        val cmd1 = StringBuilder("insert into mumps values ('")
        cmd1.append(embedDaf(q1))
        cmd1.append("', ")

        var j = 2
        while (j < 14) {                 // for (i = 2; i < 14; i++)
            val t = toks2.next()
            if (t == null) break
            lcheckD(t.toCharArray(), svPtr)
            cmd1.append('\'')           // *(p3++) = '\'';
            cmd1.append(embedDaf(t))    // while (*p2) *(p3++) = *(p2++);
            cmd1.append('\'')
            cmd1.append(',')            // *(p3++) = ',';
            cmd1.append(' ')            // *(p3++) = ' ';
            j++
        }
        while (j < 14) {                 // off count but final val in bd
            cmd1.append('\'')            // '',' pairs
            cmd1.append('\'')
            cmd1.append(',')
            cmd1.append(' ')
            j++
        }

        dcheckD(bd, svPtr)               // dcheck((char*)bd, svPtr);
        cmd1.append('\'')                // *(p3++) = '\'';
        cmd1.append(embedDaf(DafCRuntime.cstring(bd)))
        cmd1.append('\'')
        cmd1.append(' ')                 // *(p3++) = ' ';
        cmd1.append(')')                 // *(p3++) = ')';
        cmd1.append(';')                 // *(p3++) = ';';

        // i = sql(g, svPtr, "mumps", cmd1, bdx, " ");
        val iIns = sqlDaf(g, svPtr, "mumps", cmd1.toString(), BdxHolderBT.bdx, " ")

        if (iIns == 0) { // if (i == 0) { fprintf(stderr,...); sigint(100); }
            System.err.print("\n*** SQL error reported during assignment ")
            System.err.print("to global array:\n*** " + DafCRuntime.cstring(svPtr.pgsql_msg) + "\n")
            // Command: %s - the vendor prints cmd (the delete
            // command), NOT cmd1: verbatim quirk kept.
            System.err.print("Command: " + cmd.toString() + "\n")
            sigintD(100)
        }

        return 1
    }

    // ================== ORDERNEXT / ORDERPREV ==================
    if (g == ORDERNEXT_D || g == ORDERPREV_D) {

        // select aX from mumps where a1='...' and a2='...' ...
        //   and aX>'...' order by aX desc limit 1;   (NEXT)
        // select aX from mumps where a1='...' and a2='...' ...
        //   and aX<'...' order by aX asc limit 1;    (PREV)

        keyfixDaf(key) // keyfix(key);

        // if (tokens - 1 > 14 - 2) { return 0; // permits
        //   \u0024data to work at this level; printf; sigint(100);
        //   } - the return 0 PRECEDES the printf/sigint pair
        //   (dead rest, H-013 ledger).
        if (tokens - 1 > 14 - 2) {
            return 0 // permits \u0024data to work at this level
        }

        val svTable = DafCRuntime.cstring(svPtr.Table)
        val cmd = StringBuilder()

        if (tokens == 1) {
            cmd.append("select a1 from ")   // p1 = cmd + 15 shape
            cmd.append(svTable)
            cmd.append(" where a1='")
            // p1 += 12 after the 11-char literal: the vendor
            // skips one UNWRITTEN byte (uninitialized cmd - the
            // indeterminate gap modeled as a NUL append, H-013
            // ledger).
            cmd.append(NUL_D)
        } else {
            cmd.append("select ")
            cmd.append(numsBT[tokens])      // col whose value we want
            cmd.append(" from ")
            cmd.append(svTable)
            cmd.append(" where a1='")
        }

        val toks = TokWalkerBT(key)
        var p3 = toks.next() // p3 = strtok(key, delim);

        // for (p2 = embed(p3); *p2; p2++) *(p1++) = *p2;
        cmd.append(embedDaf(p3))
        cmd.append('\'') // *(p1++) = '\'';
        cmd.append(' ') // *(p1++) = ' ';

        var i = 2
        while (i < tokens) {               // for (i = 2; i < tokens; i++)
            p3 = toks.next()
            if (p3 == null) break

            cmd.append("and ")              // strcpy(p1,"and "); p1 += 4;
            cmd.append(numsBT[i])
            cmd.append('=')
            cmd.append('\'')
            cmd.append(embedDaf(p3))
            cmd.append('\'')
            cmd.append(' ')
            i++
        }

        p3 = toks.next() // p3 = strtok(NULL, delim);

        if (p3 != null) {
            if (g == ORDERNEXT_D) {
                cmd.append("and ")          // strcpy(p1,"and "); p1 += 4;
                cmd.append(numsBT[i])
                cmd.append('>')            // *(p1++) = '>';
                cmd.append('\'')
            } else {
                cmd.append("and ")
                cmd.append(numsBT[i])
                cmd.append('<')             // *(p1++) = '<';
                cmd.append('\'')
            }
            cmd.append(embedDaf(p3))        // for (p2=embed(p3);...)
            cmd.append('\'')
            cmd.append(' ')
        } else {
            // next requires a comp operator for empty string:
            // ONLY the ORDERNEXT arm stubs the empty compare (the
            // vendor nested if has no ORDERPREV else - asymmetry
            // kept); the commented-out cast(...) fossils stay
            // comments.
            if (g == ORDERNEXT_D) {
                cmd.append("and ")
                cmd.append(numsBT[i])
                cmd.append('>')
                cmd.append('\'')
                cmd.append('\'')
                cmd.append(' ')
            }
        }

        if (tokens == 1) {
            if (g == ORDERNEXT_D) cmd.append(" order by a1 asc limit 1;")
            else cmd.append(" order by a1 desc limit 1;")
        } else if (g == ORDERNEXT_D) {
            cmd.append("order by ")         // strcpy(p1,"order by "); p1 += 9;
            cmd.append(numsBT[tokens])
            cmd.append(" asc limit 1;")
        } else {
            cmd.append("order by ")
            cmd.append(numsBT[tokens])
            cmd.append(" desc limit 1;")
        }

        // i = sql(GLOBAL, svPtr, "mumps", cmd, bd, XNULL);
        return sqlDaf(GLOBAL_BT_D, svPtr, "mumps", cmd.toString(), bd, XNULL_BT)
    }

    // ================== XNEXT (used by \u0024query) =============
    if (g == XNEXT_D) {

        // if (tokens - 1 > 14 - 2) { *** 003 banner; sigint(100); }
        if (tokens - 1 > 14 - 2) {
            System.err.print("\n*** 003 Maximum number if global array indices exceeded\n")
            System.err.print("in or near line " + svPtr.LineNumber + "\n")
            sigintD(100)
        }

        val toks = TokWalkerBT(key)
        var p1 = toks.next() // p1 = strtok(key, delim);

        // #if 14==N ladder: the reference tree is configured 14,
        // so the #if 14==14 arm is the LIVE one and the other
        // twelve arms are compiled out (H-018 exclusion ladder;
        // the #if 14>=18 #error rail included in the vendor text).
        // 14==14: "select a1,..,a13 from mumps where a1=\"" + p1 +
        // "\" and (a2,...,a13) > ("
        // 14==14: "select a1,..,a13 from mumps where a1=\"" + p1 +
        // "\" and (a2,...,a13) > (" (the col-list literal verbatim)
        val cmd = StringBuilder("select a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13 from mumps where a1=\"")
        cmd.append(p1)
        cmd.append("\" and (a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13) > (")

        p1 = toks.next() // p1 = strtok(NULL, delim); get another

        var i = 2
        while (i <= tokens) {              // for (i = 2; i <= tokens; i++)
            if (p1 == null) {
                cmd.append("'',")          // if (p1==NULL) strcat(cmd,"'',");
            } else {
                cmd.append('\'')            // strcat(cmd,"'");
                lcheckD(p1.toCharArray(), svPtr)
                cmd.append(embedDaf(p1))   // strcat(cmd,embed(p1));
                cmd.append("',")           // strcat(cmd,"',");
                p1 = toks.next() // p1 = strtok(NULL, delim);
            }
            i++
        }

        if (p1 == null) {
            cmd.append("''") // if (p1==NULL) strcat(cmd,"''");
        } else {
            cmd.append('\'')
            lcheckD(p1.toCharArray(), svPtr)
            cmd.append(embedDaf(p1))
            cmd.append('\'')
        }

        while (i < 14 - 1) {               // for (; i < 14-1; i++)
            cmd.append(",''")
            i++
        }

        // #if 14==14 tail: ") order by a2,a3,a4,a5,a6,a7,a9,a10,
        //   a11,a12,a13,a14" - the a8 SKIP is a vendor quirk kept
        // verbatim (the compiled-out a4..a17 tails are ledgers in
        // the vendor text).
        cmd.append(") order by a2,a3,a4,a5,a6,a7,a9,a10,a11,a12,a13,a14")

        i = 2
        while (i < tokens - 1) {           // for (i=2; i<tokens-1; i++)
            // sprintf(tmp, ", a%d", i + 1); strcat(cmd, tmp);
            cmd.append(", a" + (i + 1))
            i++
        }

        cmd.append(" limit 1;")

        // strcpy((char *) key, keybak); - keybak is UNINITIALIZED
        // stack here (vendor UB; the H-013 unreachable-assignment
        // ledger): the write is dead because the next line
        // overwrites key from rsltSpec again (kept as the ledger
        // note; the subsequent strcpy(key, rsltSpec) is the
        // effective statement).

        // i = sql(g, svPtr, "mumps", cmd, bd, X01);
        val iXn = sqlDaf(g, svPtr, "mumps", cmd.toString(), bd, X01_BT)

        // strcpy((char *) key, (char *) rsltSpec); - the landed
        // unit-208 rsltSpecD seat content (String carrier).
        val spec = rsltSpecD
        val kMax = minOf(spec.length, key.size)
        for (k in 0 until kMax) key[k] = spec[k]
        for (k in kMax until key.size) key[k] = NUL_D // NUL-pad tail

        return iXn
    }

    // ================== DELETE / GKILL =========================
    if (g == KILL_D) {

        // VENDOR QUIRK (H-013 kept): this arm re-declares the
        // local int tokens = 0, SHADOWING the outer index count -
        // so the guard below is dead (0-1 never > 12) and the
        // tokens==1 arm is dead too (0 != 1); the else arm always
        // runs. keybak (copied below) is a dead store.
        var tokLoc = 0
        if (tokLoc - 1 > 14 - 2) {
            System.err.print("\n*** 003 Maximum number if global array indices exceeded\n")
            System.err.print("in or near line " + svPtr.LineNumber + "\n")
            sigintD(100)
        }

        val toks = TokWalkerBT(key)
        val cmd = StringBuilder("delete from mumps ") // strcpy(cmd,...)

        if (tokLoc == 1) { // dead arm (the shadow, ledger above)
            cmd.append("where a1='")
            val p3 = toks.next() // strtok(key, delim); array name
            cmd.append(p3 ?: "")
            cmd.append("';")
        } else {
            cmd.append("where ")
            var p3 = toks.next() // array name

            var i = 1
            while (i < 14 - 2) {           // for (i = 1; i < 14-2; i++)
                if (p3 == null) break
                if (i != 1) cmd.append(" and ")
                cmd.append(numsBT[i])
                cmd.append("='")
                lcheckD(p3.toCharArray(), svPtr)
                cmd.append(embedDaf(p3))
                cmd.append("' ")
                p3 = toks.next()
                i++
            }
            cmd.append(";")
        }

        // i = sql(g, svPtr, "mumps", cmd, bd, X01);
        return sqlDaf(g, svPtr, "mumps", cmd.toString(), bd, X01_BT)
    }

    // ================== GLOBAL NAMES ===========================
    if (g == GBLNME_BT_D) {
        // p1 = cmd + 15; strcpy(cmd, "select a1 from "); Table;
        //   " where a1='" + key + "' order by a1 limit 1;"
        val cmd = StringBuilder("select a1 from ")
        cmd.append(DafCRuntime.cstring(svPtr.Table))
        cmd.append(" where a1>'")
        cmd.append(DafCRuntime.cstring(key))
        cmd.append("' order by a1 limit 1;")

        bd[0] = NUL_D // bd[0] = 0; empty string
        // i = sql(RETRIEVE, svPtr, "mumps", cmd, bd, XTAB);
        return sqlDaf(RETRIEVE_D, svPtr, "mumps", cmd.toString(), bd, XTAB_BT)
    }

    // ================== CLOSE ==================================
    if (g == CLOSE_D) {
        // sql(CLOSE, svPtr, "", "", "", XTAB);
        sqlDaf(CLOSE_D, svPtr, "", "", "", XTAB_BT)
        return OK_BT_D
    }

    return OK_BT_D
}
