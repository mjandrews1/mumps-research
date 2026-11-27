package both

// Unit 216 render: interp-sql.cpp::btree (roster 216, batch 8,
// 443 LOC). int btree(int g, unsigned char key[], unsigned
// char bd[], struct MSV * svPtr) - the SQLITE Mglobal routing
// engine: opens the connection, then dispatches the opcode
// (RETRIEVE / STORE=DELETE+INSERT / ORDERNEXT / ORDERPREV /
// XNEXT / GKILL / GBLNME / CLOSE / SQLCMD) building the SQL
// command text over the nums[] column-name roster.
//
// THIS IS THE Mglobal SQLITE ROUTING'S ONLY CALLEE (the
// btreeC seat, BothCRuntime:193, routes here; the record-29
// native-btree wrapper is the OTHER arm, not compiled in the
// reference build).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h007 char_buffer - reused: the ORDER arms build the cmd
//     by EXACT vendor pointer arithmetic (a CharArray with a
//     manual write index; the p1+=12 off-by-one below lands
//     one past the NUL - the truncation-then-continue shape
//     kept byte-for-byte; the strcat arms ride StringBuilder).
//   h009 format_string - reused: the ", a%d" splice keeps the
//     %d digit shape (string concatenation).
//   h013 dead_store - hit, three: (1) the ORDERNEXT arm's
//     max-index printf+sigint sits AFTER return 0 (dead -
//     kept in shape, ledgered); (2) the XNEXT arm's
//     strcpy(key, keybak) reads an UNINITIALIZED keybak
//     and is overwritten by the rsltSpec copy two lines
//     later (dead UB - quantized OUT, ledgered); (3) the
//     inner STORE block's duplicate strcpy(save, key) under
//     the "replace any existing tuple" comment (dead repeat -
//     ledgered).
//   h014 out_of_scope_call - reused and hit: sql (the
//     interp-sql.cpp 6-arg engine) is a NEW TODO stub raised
//     here - distinct from the seated libmpscpp sql
//     (Sql.kt:56, the 5-string-arg face); embed is a NEW
//     TODO stub raised here - COVERAGE FINDING: record 43
//     (interp-sql.cpp::embed, batch 2) has NO render on BOTH
//     master and NO events-CSV rows (verified against the
//     master logs this session); the embed1 seat (Embed1.kt,
//     record 208) is the DOUBLE-quote twin, NOT this
//     single-quote fixer - disclosed to the PI at close;
//     lcheck/dcheck direct (record 227, this batch);
//     keyfixC direct (the keyfix seat); sigint(100) direct;
//     Msv.Table rides the Msv seat; SqlRuntime.rsltSpec
//     direct (the record-227 fold).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: btree keeps vendor name (the
//     btreeC seat is the carrier wrapper; the record-29
//     native-btree stub is BothStubs-quoted and distinct);
//     the opcode consts seat privately (GKILL 3 collides with
//     the Interpret.kt private and ServerMain KILL pins; OPEN
//     206 collides with the defines.h OPEN family - the
//     vendor reuses the CodedOpen byte as an opcode, kept).
//   h020 pointer_handle - reused: the sql first call passes
//     tmp1 as BOTH Command and out (the vendor placeholder
//     pair); X01/XNULL/XTAB/XCMD seat as String delim
//     literals ({1,0}/{0,0}/tab/newline).
//
// Quirks kept (ledgered):
//   - the connection-open call happens EVERY btree call
//     (sql(OPEN,...) with the mumps DSN and the unused tmp1
//     pair).
//   - if (g == NEXT) g = XNEXT; rewrites \u0024query NEXTs before
//     the dispatch.
//   - RETRIEVE pads the WHERE to a14 with and an=''; STORE's
//     delete pads to a14, the INSERT pads to a13 + final value
//     (the three different pad ceilings kept).
//   - the STORE max-index message says "*** 001 xMaximum"
//     (vendor typo - kept); the RETRIEVE one says "*** 001
//     Maximum"; XNEXT says "*** 003".
//   - the ORDER tokens==1 arm's p1 += 12 lands one past the
//     NUL of " where a1='" (11 chars + NUL; the else arm's
//     += 11 lands ON the NUL) - the tokens==1 command string
//     truncates at the quote in C (the vendor bug kept via
//     the exact index arithmetic; sql reads the CharArray to
//     its first NUL, the truncation preserved).
//   - the XNEXT #if 14==N ladder: only the 14==14 arm is
//     compiled (TabSize 14; the a1..a13 select and the
//     (a2..a13) tuple compare); the order-by tail says
//     "a2,a3,a4,a5,a6,a7,a9,a10,a11,a12,a13,a14" - a8 IS
//     SKIPPED (vendor typo, kept verbatim; the ==9..==14 arms
//     all carry it).
//   - the XNEXT tail appends ", a%d" for i in 2..<tokens-1
//     (a3..a<tokens>-1 - column names appended AFTER the
//     order-by clause; kept).
//   - the GKILL arm declares a LOCAL int tokens = 0 which
//     shadows the count: the max-check never fires and the
//     tokens == 1 arm is dead (always the where-walk; kept
//     exact with the local).
//   - GBLNME embeds the key RAW (no strtok, no embed(), no
//     keyfix - kept).
//   - the ORDER arm strtok's the key IN PLACE (the param
//     buffer is consumed; the vendor mutation is not
//     observable post-return - ledgered).
//   - the commented replace into sprintf and the commented
//     cast( prefix in the ORDERNEXT empty-compare arm stay
//     ledgered (dead in the pristine tree).

// ---------------------------------------------------------------------------
// Seats

// char * embed(char *in) - owner: interp-sql.cpp::embed
// (roster 43, batch 2; the single-quote doubling fixer).
// TODO-stub (h014). COVERAGE FINDING: no BOTH render exists
// on master for this unit (verified this session); the embed1
// seat is the double-quote libmpscpp twin, distinct.
internal fun embed(in_: String): String {
    TODO("stub: owner interp-sql.cpp::embed (roster 43, batch 2; single-quote fixer - unrendered on master, PI disclosure at close)")
}

// int sql(int, struct MSV *, char *, char *, char *, const char *)
// - owner: interp-sql.cpp::sql (the TU engine; the record-227
// prototype ledger pin). Distinct from the seated libmpscpp
// sql (Sql.kt:56). TODO-stub (h014).
internal fun sql(
    op: Int, svPtr: Msv, DataSourceName: String, Command: CharArray,
    out: CharArray, dlim: String,
): Int {
    TODO("stub: owner interp-sql.cpp::sql (the 6-arg SQLITE engine, record 227 prototype pin)")
}

private const val BT_GKILL = 3 // defines.h:167
private const val BT_GLOBAL = 23 // defines.h:93
private const val BT_OPEN = 206 // defines.h:169 (the CodedOpen byte reused)
private const val BT_GBLNME = 20 // defines.h:245
private const val BT_SQLCMD = 55 // defines.h:181
private const val BT_OK = 1 // interp-sql.cpp OK

private const val X01 = "\u0001" // static char X01[2] = {1, 0}
private const val XNULL = "" // static char XNULL[2] = {0, 0}
private const val XTAB = "\t" // static char XTAB[2] = {'\t', 0}
private const val XCMD = "\n" // static char XCMD[2] = {10, 0}

// static char *nums[33] - the column-name roster (the head
// comment: number of columns limited to 32; the first is a
// dummy as the column names begin with a1).
private val numsBt = arrayOf(
    " ", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10",
    "a11", "a12", "a13", "a14", "a15", "a16", "a17", "a18", "a19",
    "a20", "a21", "a22", "a23", "a24", "a25", "a26", "a27", "a28",
    "a29", "a30", "a31", "a32",
)

// The strtok(save, {1,0}) fold: skips leading SOHs, token to
// the next SOH, null when exhausted (C strtok semantics).
private class StrtokBt(val s: String) {
    private var pos = 0
    fun next(): String? {
        while (pos < s.length && s[pos] == '\u0001') pos++
        if (pos >= s.length) return null
        val start = pos
        while (pos < s.length && s[pos] != '\u0001') pos++
        return s.substring(start, pos)
    }
}

// strcpy at offset (writes the NUL - the pointer-arithmetic
// build idiom).
private fun strcpyAtBt(a: CharArray, off: Int, src: String) {
    var d = off
    for (k in src.indices) a[d++] = src[k]
    a[d] = '\u0000'
}

// static unsigned char bdx[256] - the inner STORE block's
// static (the last-value shape kept).
private val bdxStaticBt = CharArray(256)

// ---------------------------------------------------------------------------
// btree

internal fun btree(g0: Int, key: CharArray, bd: CharArray, svPtr: Msv): Int {
    var g = g0
    var tokens = 0

    // make sure connection is open
    val tmpOpen = CharArray(32) // unsigned char tmp1[32]
    sql(BT_OPEN, svPtr, "mumps", tmpOpen, tmpOpen, XCMD)

    if (g == NEXT) g = XNEXT

    // user command
    if (g == BT_SQLCMD) {
        return sql(BT_SQLCMD, svPtr, "mumps", key, bd, XCMD)
    }

    // count indices
    for (k in cStringOf(key)) if (k == '\u0001') tokens++

    // -----------------------------------------------------------------
    // RETRIEVE
    // -----------------------------------------------------------------
    if (g == RETRIEVE) {
        // select a14 from mumps where a1='...',a2='...',...
        if (tokens - 1 > 14 - 2) {
            System.err.print(
                "\n*** 001 Maximum number if global" +
                    " array indices exceeded\n" +
                    "in or near line " + svPtr.LineNumber + "\n"
            )
            sigint(100)
        }

        val st = StrtokBt(cStringOf(key))
        var p1 = st.next()

        val cmd = StringBuilder("select ")
        cmd.append(numsBt[14])
        cmd.append(" from mumps ")

        cmd.append(" where a1='")

        if (p1 != null) {
            lcheck(p1.toCharArray(), svPtr)
            cmd.append(embed(p1))
            cmd.append("' ")
        } else {
            cmd.append("' ") // no indices to global provided
        }

        var i = 2
        while (i < 14 - 2) {
            p1 = st.next()
            if (p1 == null) break
            lcheck(p1.toCharArray(), svPtr)
            cmd.append(" and ")
            cmd.append(numsBt[i])
            cmd.append("='")
            cmd.append(embed(p1))
            cmd.append("'")
            i++
        }

        while (i < 14 - 2) {
            cmd.append(" and ")
            cmd.append(numsBt[i])
            cmd.append("=''")
            i++
        }

        cmd.append(" limit 1;")
        bd[0] = '\u0000' // empty string
        val cmdBt = CharArray(STR_MAX)
        writeC(cmdBt, cmd.toString())
        return sql(RETRIEVE, svPtr, "mumps", cmdBt, bd, XTAB)
    }

    // -----------------------------------------------------------------
    // STORE (a DELETE then an INSERT)
    // -----------------------------------------------------------------
    if (g == STORE) {
        // select a10 from mumps where a1='...',a2='...',...
        if (tokens - 1 > 14 - 2) {
            System.err.print(
                "\n*** 001 xMaximum number " +
                    "if global array indices exceeded\n" +
                    "in or near line " + svPtr.LineNumber + "\n"
            )
            sigint(100)
        }

        val st = StrtokBt(cStringOf(key))
        var p1 = st.next()

        // delete any prior value this key
        val cmd = StringBuilder("delete ") // delete first
        cmd.append(" from mumps ")

        cmd.append(" where a1='")

        if (p1 != null) {
            lcheck(p1.toCharArray(), svPtr)
            cmd.append(embed(p1))
            cmd.append("' ")
        } else {
            cmd.append("' ") // no indices to global provided
        }

        var i = 2
        while (i < 14) {
            p1 = st.next()
            if (p1 == null) break
            lcheck(p1.toCharArray(), svPtr)
            cmd.append(" and ")
            cmd.append(numsBt[i])
            cmd.append("='")
            cmd.append(embed(p1))
            cmd.append("'")
            i++
        }

        while (i < 14) { // pad WHERE with: and an=''
            cmd.append(" and ")
            cmd.append(numsBt[i])
            cmd.append("=''")
            i++
        }

        val cmdDel = CharArray(STR_MAX)
        writeC(cmdDel, cmd.toString())
        val bdx = CharArray(128)
        sql(g, svPtr, "mumps", cmdDel, bdx, XTAB)

        // -------------------------------------------------------------
        // STORE new or replacement key
        // -------------------------------------------------------------
            if (tokens - 1 > 14 - 2) {
                System.err.print(
                    "\n*** 002 Maximum number if global array indices exceeded\n" +
                        "in or near line " + svPtr.LineNumber + "\n"
                )
                sigint(100)
            }

            // (the vendor's duplicate strcpy(save, key) under the
            // "replace any existing tuple" comment is a dead repeat -
            // h013, ledgered)

            val st2 = StrtokBt(cStringOf(key))
            var p1s = st2.next()

            val cmdIns = CharArray(STR_MAX * 2)
            // sprintf(cmd, "insert into mumps values ('%s', ",
            //          embed(p1)); - the commented replace-into arm
            // stays ledgered.
            strcpyAtBt(cmdIns, 0, "insert into mumps values ('")
            var p3 = 0
            if (p1s != null) {
                for (c in embed(p1s)) cmdIns[p3++] = c
            }
            strcpyAtBt(cmdIns, p3, "', ")
            p3 += 3

            var j = 2
            while (j < 14) {
                p1s = st2.next()
                if (p1s == null) break
                lcheck(p1s.toCharArray(), svPtr)
                cmdIns[p3++] = '\''
                for (c in embed(p1s)) cmdIns[p3++] = c
                cmdIns[p3++] = '\''
                cmdIns[p3++] = ','
                cmdIns[p3++] = ' '
                cmdIns[p3] = '\u0000'
                j++
            }

            while (j < 14) { // off count but final val in bd
                cmdIns[p3++] = '\''
                cmdIns[p3++] = '\''
                cmdIns[p3++] = ','
                cmdIns[p3++] = ' '
                j++
            }

            cmdIns[p3] = '\u0000'

            dcheck(bd, svPtr)
            val p2 = embed(cStringOf(bd))
            cmdIns[p3++] = '\''
            for (c in p2) cmdIns[p3++] = c
            cmdIns[p3++] = '\''
            cmdIns[p3++] = ' '
            cmdIns[p3++] = ')'
            cmdIns[p3++] = ';'
            cmdIns[p3] = '\u0000'

            val r = sql(g, svPtr, "mumps", cmdIns, bdxStaticBt, " ")

            if (r == 0) {
                System.err.print(
                    "\n*** SQL error reported during assignment " +
                        "to global array:\n*** " +
                        cStringOf(svPtr.pgsql_msg) + "\nCommand: " +
                        cStringOf(cmdIns) + "\n"
                )
                sigint(100)
            }

        return 1
    }

    // -----------------------------------------------------------------
    // ORDERNEXT / ORDERPREV (\u0024data / \u0024order)
    // -----------------------------------------------------------------
    if (g == CompilerParseDefs.ORDERNEXT || g == CompilerParseDefs.ORDERPREV) {
        // select aX from mumps where a1='...'
        //   and a2='...' ... and aX>'...' order by aX desc limit 1;
        // select aX from mumps where a1='...'
        //   and a2='...' ... and aX<'...' order by aX asc limit 1;

        keyfixC(key)

        if (tokens - 1 > 14 - 2) {
            return 0 // permits \u0024data to work at this level
            // the vendor printf+sigint below is DEAD after the
            // return (h013, ledgered)
        }

        val table = cStringOf(svPtr.Table)
        val cmd = CharArray(STR_MAX * 2)
        var p1: Int

        if (tokens == 1) {
            p1 = 15
            strcpyAtBt(cmd, 0, "select a1 from ")
            for (c in table) cmd[p1++] = c
            strcpyAtBt(cmd, p1, " where a1='")
            p1 += 12 // the vendor off-by-one (see header quirk)
        } else {
            p1 = 7
            strcpyAtBt(cmd, 0, "select ")
            for (c in numsBt[tokens]) { // col whose value we want
                cmd[p1++] = c // advances ptr by lngth of nums
            }
            strcpyAtBt(cmd, p1, " from ")
            p1 += 6
            for (c in table) cmd[p1++] = c
            strcpyAtBt(cmd, p1, " where a1='")
            p1 += 11
        }

        val st = StrtokBt(cStringOf(key)) // strtok's key in place
        var p3 = st.next()
        for (c in embed(p3!!)) cmd[p1++] = c

        cmd[p1++] = '\''
        cmd[p1++] = ' '

        var i = 2
        while (i < tokens) {
            p3 = st.next()
            if (p3 == null) break
            strcpyAtBt(cmd, p1, "and ")
            p1 += 4
            for (c in numsBt[i]) cmd[p1++] = c
            cmd[p1++] = '='
            cmd[p1++] = '\''
            for (c in embed(p3)) cmd[p1++] = c
            cmd[p1++] = '\''
            cmd[p1++] = ' '
            i++
        }

        p3 = st.next()

        if (p3 != null) {
            if (g == CompilerParseDefs.ORDERNEXT) {
                strcpyAtBt(cmd, p1, "and ")
                p1 += 4
                for (c in numsBt[i]) cmd[p1++] = c
                cmd[p1++] = '>'
                cmd[p1++] = '\''
            } else {
                strcpyAtBt(cmd, p1, "and ")
                p1 += 4
                for (c in numsBt[i]) cmd[p1++] = c
                cmd[p1++] = '<'
                cmd[p1++] = '\''
            }
            for (c in embed(p3)) cmd[p1++] = c
            cmd[p1++] = '\''
            cmd[p1++] = ' '
        } else { // next requires a comp operator for empty string
            if (g == CompilerParseDefs.ORDERNEXT) {
                strcpyAtBt(cmd, p1, "and ")
                p1 += 4
                // the commented cast( prefix arm stays ledgered
                for (c in numsBt[i]) cmd[p1++] = c
                cmd[p1++] = '>'
                cmd[p1++] = '\''
                cmd[p1++] = '\''
                cmd[p1++] = ' '
            }
        }

        if (tokens == 1) {
            if (g == CompilerParseDefs.ORDERNEXT) {
                strcpyAtBt(cmd, p1, " order by a1 asc limit 1;")
            } else {
                strcpyAtBt(cmd, p1, " order by a1 desc limit 1;")
            }
        } else if (g == CompilerParseDefs.ORDERNEXT) {
            strcpyAtBt(cmd, p1, "order by ")
            p1 += 9
            for (c in numsBt[tokens]) cmd[p1++] = c
            strcpyAtBt(cmd, p1, " asc limit 1;")
        } else {
            strcpyAtBt(cmd, p1, "order by ")
            p1 += 9
            for (c in numsBt[tokens]) cmd[p1++] = c
            strcpyAtBt(cmd, p1, " desc limit 1;")
        }

        val r = sql(BT_GLOBAL, svPtr, "mumps", cmd, bd, XNULL)
        return r
    }

    // -----------------------------------------------------------------
    // XNEXT - used by \u0024query
    // -----------------------------------------------------------------
    if (g == XNEXT) {
        val cmd = StringBuilder("")

        if (tokens - 1 > 14 - 2) {
            System.err.print(
                "\n*** 003 Maximum number if global array indices exceeded\n" +
                    "in or near line " + svPtr.LineNumber + "\n"
            )
            sigint(100)
        }

        val st = StrtokBt(cStringOf(key))
        var p1 = st.next()

        // the #if 14==N ladder: the reference build compiles the
        // 14==14 arm only (TabSize 14).
        cmd.append("select a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13 from mumps where a1=\"")
        cmd.append(p1)
        cmd.append("\" and (a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13) > (")

        p1 = st.next() // get another

        var i = 2
        while (i <= tokens) {
            if (p1 == null) {
                cmd.append("'',")
            } else {
                cmd.append("'")
                lcheck(p1.toCharArray(), svPtr)
                cmd.append(embed(p1))
                cmd.append("',")
                p1 = st.next() // get another
            }
            i++
        }

        if (p1 == null) {
            cmd.append("''")
        } else {
            cmd.append("'")
            lcheck(p1.toCharArray(), svPtr)
            cmd.append(embed(p1))
            cmd.append("'")
        }

        i2@ while (true) {
            if (i >= 14 - 1) break@i2
            cmd.append(",''")
            i++
        }

        // #if 14==14 arm (the a8-skip vendor typo kept verbatim)
        cmd.append(") order by a2,a3,a4,a5,a6,a7,a9,a10,a11,a12,a13,a14")

        var ii = 2
        while (ii < tokens - 1) {
            cmd.append(", a" + (ii + 1))
            ii++
        }

        cmd.append(" limit 1;")

        // the vendor strcpy(key, keybak) reads an UNINITIALIZED
        // keybak and is overwritten two lines later (dead UB -
        // h013, quantized OUT, ledgered).

        val cmdBt = CharArray(STR_MAX * 2)
        writeC(cmdBt, cmd.toString())
        val r = sql(g, svPtr, "mumps", cmdBt, bd, X01)

        writeC(key, cStringOf(SqlRuntime.rsltSpec))

        return r
    }

    // -----------------------------------------------------------------
    // DELETE / GKILL
    // -----------------------------------------------------------------
    if (g == BT_GKILL) {
        val cmd = StringBuilder("delete from mumps ")
        val tokensGk = 0 // the vendor LOCAL shadow (see header)

        if (tokensGk - 1 > 14 - 2) { // never fires (the local)
            System.err.print(
                "\n*** 003 Maximum number if global array indices exceeded\n" +
                    "in or near line " + svPtr.LineNumber + "\n"
            )
            sigint(100)
        }

        if (tokensGk == 1) { // dead arm (the local is 0)
            cmd.append("where a1='")
            val st = StrtokBt(cStringOf(key))
            val p3 = st.next() // array name
            cmd.append(p3)
            cmd.append("';")
        } else {
            cmd.append("where ")
            val st = StrtokBt(cStringOf(key))
            var p3 = st.next() // array name

            var i = 1
            while (i < 14 - 2) {
                if (p3 == null) break
                if (i != 1) cmd.append(" and ")
                cmd.append(numsBt[i])
                cmd.append("='")
                lcheck(p3.toCharArray(), svPtr)
                cmd.append(embed(p3))
                cmd.append("' ")
                p3 = st.next()
                i++
            }

            cmd.append(";")
        }

        val cmdBt = CharArray(STR_MAX)
        writeC(cmdBt, cmd.toString())
        val r = sql(g, svPtr, "mumps", cmdBt, bd, X01)
        return r
    }

    // -----------------------------------------------------------------
    // GLOBAL NAMES
    // -----------------------------------------------------------------
    if (g == BT_GBLNME) {
        val cmd = CharArray(STR_MAX * 2)
        var p1 = 15
        strcpyAtBt(cmd, 0, "select a1 from ")
        for (c in cStringOf(svPtr.Table)) cmd[p1++] = c
        strcpyAtBt(cmd, p1, " where a1>'")
        p1 += 11
        val tail = " order by a1 limit 1;" // strcat(p1,...) family
        for (c in cStringOf(key)) cmd[p1++] = c
        for (c in "'") cmd[p1++] = c
        // the vendor strcat(p1,"' order by a1 limit 1;") rides
        // the string here (the quote + tail in one literal)
        cmd[p1 - 1] = '\''
        for (c in " order by a1 limit 1;") cmd[p1++] = c
        cmd[p1] = '\u0000'

        bd[0] = '\u0000' // empty string
        val r = sql(RETRIEVE, svPtr, "mumps", cmd, bd, XTAB)
        return r
    }

    // -----------------------------------------------------------------
    // CLOSE
    // -----------------------------------------------------------------
    if (g == CLOSE) {
        val dmy = CharArray(1)
        sql(CLOSE, svPtr, "", dmy, dmy, XTAB)
        return BT_OK
    }

    return BT_OK
}
