package mumps.interp

import mumps.mdh.keyfix

/**
 * PRO-arm translation of interp-sql.cpp::btree (B8 tranche 1,
 * unit 217, rank 293). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 150-877 (the SQL BTREE common section:
 * translate global-array ops to SQL and execute).
 *
 * Identity verified against the study tool: interp-sql.cpp::
 * btree, cpp, loc 443, comment_ratio 0.184, pointer_marks 58,
 * control_marks 99, score 316.3 - exact match to
 * wp1/study-subset/units.csv row 217 (batch 8).
 *
 * Overload disambiguation: this is the SQL-backed btree of
 * interp-sql.cpp (the SQLITE reference-build engine); the
 * native-buffer twin of interp-native-btree.cpp is DONE as
 * pilot unit 4 (NativeBtree.btree) - distinct object-member
 * seat, no collision (h015 scan clean).
 *
 * btree(g, key, bd, svPtr): opens the SQL connection (sql
 * OPEN), then SQLCMD relays raw; RETRIEVE/STORE/ORDERNEXT/
 * ORDERPREV/XNEXT/GKILL/GBLNME/CLOSE each build a "select/
 * delete/insert ... where a1='...'" command over the SOH-
 * split key tokens and dispatch through the sql seat. The
 * 32-entry nums[] column-name roster (a1..a32, slot 0 the
 * dummy space) and the 14-column pad shape are the vendor's
 * fixed schema.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h006 low_level_pointer : the p1/p3 cursor walks over cmd
 *    collapse into StringBuilder appends (byte-identical
 *    output text); strtok becomes the stateful Cstrtok
 *    carrier (the NUL punches into the caller's key buffer
 *    are preserved - XNEXT/GKILL/ORDERNEXT tokenize key in
 *    place, GKILL/ORDERNEXT leave it punched).
 *  - h007 char_buffer       : cmd/cmd1 STR_MAX (cmd1 the
 *    STR_MAX*2 double buffer), save/keybak/tmp scratch;
 *    h004: the nested-block static unsigned char bdx[256]
 *    is a fresh per-call buffer (write-only scratch passed
 *    to sql, unobservable sharing).
 *  - h012 unsigned_byte_domain: key/bd/cmd byte domains; the
 *    SOH delimiter (X01/delim) is Char(1).
 *  - h013 dead_store        : the GKILL arm's local tokens=0
 *    shadows the outer count (its bounds diagnostic can
 *    never fire) and the never-filled keybak (XNEXT strcpy
 *    reads uninitialized stack - quantized to the fresh
 *    zero buffer, u128 deterministic-UB precedent); both
 *    kept verbatim with notes. The STORE arm's double
 *    strcpy(save,key) is a second dead store, kept.
 *  - h014 out_of_scope_call : sql (InterpStubs stub, batch-9
 *    owner interp-sql.cpp::sql); sigint (real seat, u191);
 *    embed (real seat, Embed.kt - direct reuse); keyfix
 *    (MdhStubs whole-buffer seat, import rebind);
 *    SqlCallbackState.rsltSpec (u200 seat, the XNEXT
 *    copy-back source); lcheck/dcheck (vendor 78-121, below
 *    min_loc, not corpus units) rendered file-private.
 *  - h015 grammar_collision: new top-level btree - distinct
 *    from NativeBtree.btree (object member) - scan clean.
 *  - h017 naming           : btree/lcheck/dcheck/embed keep
 *    the vendor names; X01/XNULL/XTAB/XCMD keep theirs.
 *  - h018 preprocessor_conditional: the XNEXT command-shape
 *    ladder is the configure-generated `#if 14==N` family -
 *    the reference build resolves 14==14 ONLY (the 4..13 and
 *    15..17 arms and the #if 14>=18 #error are EXCLUDED,
 *    journaled); the order-by ladder's LIVE arm is
 *    ") order by a2,a3,a4,a5,a6,a7,a9,a10,a11,a12,a13,a14"
 *    - the a8-skipping/a14 tail VENDOR TYPO kept verbatim.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - RETRIEVE selects nums[14] ("a13") and pads the WHERE to
 *    column 11; STORE pads to 14 (the delete-first/build
 *    asymmetry kept).
 *  - The STORE banner says "001 xMaximum" (the x typo kept);
 *    the insert uses "insert into" (the commented-out
 *    "replace into" fossil kept as a comment).
 *  - STORE passes the XTAB delimiter to the delete call but
 *    a bare space to the insert call, and cmd1 is a plain
 *    strcpy of cmd (no observable effect) - kept.
 *  - ORDERNEXT/ORDERPREV with a missing final subscript
 *    emits the "and aN>''" pad only for ORDERNEXT; the
 *    ORDERPREV path emits NOTHING (malformed command, vendor
 *    quirk kept).
 *  - The GBLNME arm builds " where a1>'" over the caller's
 *    whole key (no embed) - kept.
 *  - The initial sql(OPEN,...) passes the uninitialized tmp1
 *    as the command (vendor quirk; quantized to the fresh
 *    zero buffer) - kept.
 *  - XNEXT: key is first wiped from the never-filled keybak
 *    (zero buffer) and then overwritten from rsltSpec - kept.
 */
private const val STR_MAX_BT = 32000 // vendor strmax.h:3 (unit-44 precedent)

// vendor defines.h values (the interp-sql.cpp btree opcode set)
private const val RETRIEVE_B = 0 // defines.h:83
private const val STORE_B = 1 // defines.h:84
private const val NEXT_B = 2 // defines.h:85
private const val GKILL_B = 3 // defines.h:167
private const val CLOSE_B = 4 // defines.h:87
private const val XNEXT_B = 8 // defines.h:88
private const val ORDERNEXT_B = 12 // defines.h:170
private const val ORDERPREV_B = 13 // defines.h:171
private const val GBLNME_B = 20 // defines.h:245
private const val GLOBAL_B = 23 // defines.h:93
private const val SQLCMD_B = 55 // defines.h:181
private const val OPEN_B = 206 // defines.h:169
private const val OK_B = 1 // vendor interp-sql.cpp:38
private const val NCOLS = 14 // the configure-generated column count

private val SOH = 1.toChar() // the key subscript separator (delim/X01)

/**
 * The vendor static char* nums[33] column-name roster (vendor
 * 170-177): slot 0 the dummy space, a1..a32 after it.
 */
private val NUMS = arrayOf(
    " ", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10",
    "a11", "a12", "a13", "a14", "a15", "a16", "a17", "a18", "a19",
    "a20", "a21", "a22", "a23", "a24", "a25", "a26", "a27", "a28",
    "a29", "a30", "a31", "a32"
)

/**
 * C strtok over the SOH delimiter: stateful cursor that punches
 * NUL at each delimiter and hands back the next token offset (the
 * caller's buffer is mutated, as in the vendor).
 */
private class Cstrtok(private val buf: CharArray) {
    private var pos = 0

    /** Returns the offset of the next token, or null when exhausted. */
    fun next(): Int? {
        var p = pos
        while (p < buf.size && buf[p] == SOH) p++ // skip leading separators
        if (p >= buf.size || buf[p] == NUL) return null
        val start = p
        while (p < buf.size && buf[p] != NUL && buf[p] != SOH) p++
        if (p < buf.size) buf[p] = NUL // the C strtok punch
        pos = p + 1
        return start
    }
}

/** vendor 78-91: lcheck - global array index length check (configure max 256). */
private fun lcheck(p: String, svPtr: Msv) {
    if (p.length > 256) { // index_size filled in by configure
        System.err.print("\n*** Length of global array index exceeds configuration max.\n")
        System.err.print("*** " + p + "\n")
        System.err.print("*** In or near line " + svPtr.LineNumber + "\n\n")
        sigint(100)
    }
    // return; // ok
}

/** vendor 97-111: dcheck - global array data length check (configure max 512). */
private fun dcheck(p: String, svPtr: Msv) {
    if (p.length > 512) { // data_size filled in by configure
        System.err.print("\n*** Length of global array data value exceeds configuration max.\n")
        System.err.print("*** " + p + "\n")
        System.err.print("*** In or near line " + svPtr.LineNumber + "\n\n")
        sigint(100)
    }
    // return; // ok
}

/** NUL-terminated one-char cstring carrier (X01/XNULL/XTAB/XCMD/space). */
private fun cstr(c: Char): CharArray {
    val a = CharArray(2)
    a[0] = c
    a[1] = NUL
    return a
}

fun btree(g: Int, key: CharArray, bd: CharArray, svPtr: Msv): Int {
    val X01 = cstr(SOH) // vendor static char X01[2] = {1,0}
    val XNULL = cstr(NUL) // vendor static char XNULL[2] = {0,0}
    val XTAB = cstr('\t') // vendor static char XTAB[2] = {'\t',0}
    val XCMD = cstr(10.toChar()) // vendor static char XCMD[2] = {10,0}

    val tmp1 = CharArray(32) // vendor: uninitialized local; fresh zeros (h012 note)

    // make sure connection is open
    sql(OPEN_B, svPtr, "mumps".toC(), tmp1, tmp1, XCMD)

    val gg = if (g == NEXT_B) XNEXT_B else g

    // user command
    if (gg == SQLCMD_B) {
        return sql(SQLCMD_B, svPtr, "mumps".toC(), key, bd, XCMD)
    }

    // count indices (tokens = SOH count in key)
    var tokens = 0
    for (i in key.indices) {
        if (key[i] == NUL) break
        if (key[i] == SOH) tokens++
    }

    // ---------------------------------------------------------------
    // RETRIEVE
    // ---------------------------------------------------------------
    if (gg == RETRIEVE_B) {
        if (tokens - 1 > NCOLS - 2) {
            System.err.print(
                "\n*** 001 Maximum number if global" + " array indices exceeded\n" +
                    "in or near line " + svPtr.LineNumber + "\n"
            )
            sigint(100)
        }

        val save = CharArray(STR_MAX_BT)
        strcpyC(save, key)
        val tk = Cstrtok(save)
        val cmd = StringBuilder()
        cmd.append("select ")
        cmd.append(NUMS[14])
        cmd.append(" from mumps ")
        cmd.append(" where a1='")

        val first = tk.next()
        if (first != null) {
            val s = cstringAt(save, first)
            lcheck(s, svPtr)
            cmd.append(embed(s))
            cmd.append("' ")
        } else {
            cmd.append("' ") // no indices to global provided
        }

        var i = 2
        while (i < NCOLS - 2) {
            val p1 = tk.next() ?: break
            val s = cstringAt(save, p1)
            lcheck(s, svPtr)
            cmd.append(" and ")
            cmd.append(NUMS[i])
            cmd.append("='")
            cmd.append(embed(s))
            cmd.append("'")
            i++
        }
        while (i < NCOLS - 2) {
            cmd.append(" and ")
            cmd.append(NUMS[i])
            cmd.append("=''")
            i++
        }

        cmd.append(" limit 1;")
        bd[0] = NUL // empty string
        return sql(RETRIEVE_B, svPtr, "mumps".toC(), cmd.toString().toC(), bd, XTAB)
    }

    // ---------------------------------------------------------------
    // STORE (a DELETE then an INSERT)
    // ---------------------------------------------------------------
    if (gg == STORE_B) {
        if (tokens - 1 > NCOLS - 2) {
            System.err.print(
                "\n*** 001 xMaximum number " + "if global array indices exceeded\n" +
                    "in or near line " + svPtr.LineNumber + "\n"
            )
            sigint(100)
        }

        val save = CharArray(STR_MAX_BT)
        strcpyC(save, key)
        val tk = Cstrtok(save)

        // delete any prior value this key
        val cmd = StringBuilder()
        cmd.append("delete ")
        cmd.append(" from mumps ")
        cmd.append(" where a1='")

        val first = tk.next()
        if (first != null) {
            val s = cstringAt(save, first)
            lcheck(s, svPtr)
            cmd.append(embed(s))
            cmd.append("' ")
        } else {
            cmd.append("' ") // no indices to global provided
        }

        var i = 2
        while (i < NCOLS) {
            val p1 = tk.next() ?: break
            val s = cstringAt(save, p1)
            lcheck(s, svPtr)
            cmd.append(" and ")
            cmd.append(NUMS[i])
            cmd.append("='")
            cmd.append(embed(s))
            cmd.append("'")
            i++
        }
        while (i < NCOLS) { // pad WHERE with: and an=''
            cmd.append(" and ")
            cmd.append(NUMS[i])
            cmd.append("=''")
            i++
        }

        val bdx = CharArray(128) // vendor: unsigned char bdx[128]
        sql(gg, svPtr, "mumps".toC(), cmd.toString().toC(), bdx, XTAB)

        // STORE new or replacement key
        if (tokens - 1 > NCOLS - 2) {
            System.err.print(
                "\n*** 002 Maximum number if global array indices exceeded\n" +
                    "in or near line " + svPtr.LineNumber + "\n"
            )
            sigint(100)
        }

        val save2 = CharArray(STR_MAX_BT)
        strcpyC(save2, key) // vendor does strcpy(save,key) twice - second kept
        val tk2 = Cstrtok(save2)

        // replace any existing tuple
        // sprintf(cmd, "replace into mumps values ('%s', ", embed(p1));  [commented out in vendor]
        val cmd1 = StringBuilder()
        cmd1.append("insert into mumps values ('")
        cmd1.append(embed(cstringAt(save2, tk2.next() ?: 0)))
        cmd1.append("', ")

        var i2 = 2
        while (i2 < NCOLS) {
            val p1 = tk2.next() ?: break
            val s = cstringAt(save2, p1)
            lcheck(s, svPtr)
            cmd1.append("'")
            cmd1.append(embed(s))
            cmd1.append("', ")
            i2++
        }
        while (i2 < NCOLS) { // off count but final val in bd
            cmd1.append("''")
            cmd1.append(", ")
            i2++
        }

        dcheck(cstringAt(bd), svPtr)
        cmd1.append("'")
        cmd1.append(embed(cstringAt(bd)))
        cmd1.append("'")
        cmd1.append(" ")
        cmd1.append(")")
        cmd1.append(";")

        // vendor: static unsigned char bdx[256]; fresh per call (h004 note)
        val bdx2 = CharArray(256)
        val r = sql(gg, svPtr, "mumps".toC(), cmd1.toString().toC(), bdx2, " ".toC())

        if (r == 0) {
            System.err.print(
                "\n*** SQL error reported during assignment " +
                    "to global array:\n*** " + cstringAt(svPtr.pgsql_msg) + "\nCommand: " +
                    cmd1 + "\n"
            )
            sigint(100)
        }
        return 1
    }

    // ---------------------------------------------------------------
    // ORDERNEXT / ORDERPREV
    // ---------------------------------------------------------------
    if (gg == ORDERNEXT_B || gg == ORDERPREV_B) {
        keyfix(key)

        if (tokens - 1 > NCOLS - 2) {
            return 0 // permits data to work at this level
            // the vendor printf/sigint below is dead code (after return);
            // kept as this note (h013).
        }

        val cmd = StringBuilder()
        if (tokens == 1) {
            cmd.append("select a1 from ")
            cmd.append(svPtr.Table.toStr())
            cmd.append(" where a1='")
        } else {
            cmd.append("select ")
            cmd.append(NUMS[tokens]) // col whose value we want
            cmd.append(" from ")
            cmd.append(svPtr.Table.toStr())
            cmd.append(" where a1='")
        }

        val tk = Cstrtok(key)
        val first = tk.next() ?: 0
        cmd.append(embed(cstringAt(key, first)))
        cmd.append("'")
        cmd.append(" ")

        var i = 2
        while (i < tokens) {
            val p3 = tk.next() ?: break
            cmd.append("and ")
            cmd.append(NUMS[i])
            cmd.append("=")
            cmd.append("'")
            cmd.append(embed(cstringAt(key, p3)))
            cmd.append("'")
            cmd.append(" ")
            i++
        }

        val p3 = tk.next()

        if (p3 != null) {
            if (gg == ORDERNEXT_B) {
                cmd.append("and ")
                cmd.append(NUMS[i])
                cmd.append(">")
                cmd.append("'")
            } else {
                cmd.append("and ")
                cmd.append(NUMS[i])
                cmd.append("<")
                cmd.append("'")
            }
            cmd.append(embed(cstringAt(key, p3)))
            cmd.append("'")
            cmd.append(" ")
        } else { // next requires a comp operator for empty string
            if (gg == ORDERNEXT_B) {
                cmd.append("and ")
                cmd.append(NUMS[i])
                cmd.append(">")
                cmd.append("'")
                cmd.append("'")
                cmd.append(" ")
            }
            // ORDERPREV with no final subscript: vendor emits nothing
        }

        if (tokens == 1) {
            if (gg == ORDERNEXT_B) cmd.append(" order by a1 asc limit 1;")
            else cmd.append(" order by a1 desc limit 1;")
        } else if (gg == ORDERNEXT_B) {
            cmd.append("order by ")
            cmd.append(NUMS[tokens])
            cmd.append(" asc limit 1;")
        } else {
            cmd.append("order by ")
            cmd.append(NUMS[tokens])
            cmd.append(" desc limit 1;")
        }

        return sql(GLOBAL_B, svPtr, "mumps".toC(), cmd.toString().toC(), bd, XNULL)
    }

    // ---------------------------------------------------------------
    // XNEXT (used by dollar-query)
    // ---------------------------------------------------------------
    if (gg == XNEXT_B) {
        if (tokens - 1 > NCOLS - 2) {
            System.err.print(
                "\n*** 003 Maximum number if global array indices exceeded\n" +
                    "in or near line " + svPtr.LineNumber + "\n"
            )
            sigint(100)
        }

        val tk = Cstrtok(key)
        val first = tk.next() ?: 0
        val cmd = StringBuilder()

        // h018: the `#if 14==N` command-shape ladder resolves to the
        // 14==14 arm ONLY in the reference build (the 4..13/15..17 arms
        // and the #if 14>=18 #error are EXCLUDED, journaled).
        cmd.append("select a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13 from mumps where a1=\"")
        cmd.append(cstringAt(key, first))
        cmd.append("\" and (a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13) > (")

        var p1 = tk.next() // get another
        var i = 2
        while (i <= tokens) {
            if (p1 == null) {
                cmd.append("'',")
            } else {
                cmd.append("'")
                val s = cstringAt(key, p1)
                lcheck(s, svPtr)
                cmd.append(embed(s))
                cmd.append("',")
                p1 = tk.next() // get another
            }
            i++
        }

        if (p1 == null) {
            cmd.append("''")
        } else {
            cmd.append("'")
            val s = cstringAt(key, p1)
            lcheck(s, svPtr)
            cmd.append(embed(s))
            cmd.append("'")
        }

        while (i < NCOLS - 1) {
            cmd.append(",''")
            i++
        }

        // h018: the order-by ladder's LIVE 14==14 arm - the a8-skipping
        // VENDOR TYPO (a2..a7,a9..a13,a14) kept verbatim.
        cmd.append(") order by a2,a3,a4,a5,a6,a7,a9,a10,a11,a12,a13,a14")

        i = 2
        while (i < tokens - 1) {
            cmd.append(", a" + (i + 1))
            i++
        }

        cmd.append(" limit 1;")

        // vendor: char keybak[STR_MAX] never filled - strcpy reads
        // uninitialized stack; quantized to the fresh zero buffer (h013).
        val keybak = CharArray(STR_MAX_BT)
        strcpyC(key, keybak)

        val r = sql(gg, svPtr, "mumps".toC(), cmd.toString().toC(), bd, X01)

        strcpyC(key, SqlCallbackState.rsltSpec)
        return r
    }

    // ---------------------------------------------------------------
    // GKILL (DELETE)
    // ---------------------------------------------------------------
    if (gg == GKILL_B) {
        // vendor: local tokens=0 shadows the outer count - the bounds
        // diagnostic can never fire (h013 note).
        val tokensLocal = 0
        if (tokensLocal - 1 > NCOLS - 2) {
            System.err.print(
                "\n*** 003 Maximum number if global array indices exceeded\n" +
                    "in or near line " + svPtr.LineNumber + "\n"
            )
            sigint(100)
        }

        // sprintf(cmd, "delete from mumps ", svPtr->Table);  [commented out in vendor]
        val cmd = StringBuilder()
        cmd.append("delete from mumps ")

        if (tokensLocal == 1) {
            cmd.append("where a1='")
            val p3 = tk0(key) // array name
            cmd.append(cstringAt(key, p3))
            cmd.append("';")
        } else {
            cmd.append("where ")
            val tk = Cstrtok(key)
            var p3 = tk.next()

            var i = 1
            while (i < NCOLS - 2) {
                if (p3 == null) break
                if (i != 1) cmd.append(" and ")
                cmd.append(NUMS[i])
                cmd.append("='")
                val s = cstringAt(key, p3)
                lcheck(s, svPtr)
                cmd.append(embed(s))
                cmd.append("' ")
                p3 = tk.next()
                i++
            }

            cmd.append(";")
        }

        return sql(gg, svPtr, "mumps".toC(), cmd.toString().toC(), bd, X01)
    }

    // ---------------------------------------------------------------
    // GBLNME (GLOBAL NAMES)
    // ---------------------------------------------------------------
    if (gg == GBLNME_B) {
        val cmd = StringBuilder()
        cmd.append("select a1 from ")
        cmd.append(svPtr.Table.toStr())
        cmd.append(" where a1>'")
        cmd.append(cstringAt(key))
        cmd.append("' order by a1 limit 1;")

        bd[0] = NUL // empty string
        return sql(RETRIEVE_B, svPtr, "mumps".toC(), cmd.toString().toC(), bd, XTAB)
    }

    // ---------------------------------------------------------------
    // CLOSE
    // ---------------------------------------------------------------
    if (gg == CLOSE_B) {
        sql(CLOSE_B, svPtr, "".toC(), "".toC(), "".toC(), XTAB)
        return OK_B
    }

    return OK_B
}

/** The tokens==1 GKILL arm's single strtok over key (first token). */
private fun tk0(key: CharArray): Int {
    val tk = Cstrtok(key)
    return tk.next() ?: 0
}

/** String -> NUL-terminated CharArray (per-file bridge). */
private fun String.toC(): CharArray {
    val a = CharArray(this.length + 1)
    for (i in this.indices) a[i] = this[i]
    a[this.length] = NUL
    return a
}

/** CharArray (NUL-terminated, from offset 0) -> String. */
private fun CharArray.toStr(): String = cstringAt(this, 0)
