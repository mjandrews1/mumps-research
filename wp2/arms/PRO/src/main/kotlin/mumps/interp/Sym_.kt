package mumps.interp

// ----------------------------------------------------------------------------
// (B10 unit 280) interp-sym.cpp::sym_ (vendor reference lines 33-516).
// Identity: units.csv row 280 - interp-sym.cpp::sym_, cpp, loc 484,
// comment_ratio 0.125, pointer_marks 201, control_marks 134,
// score 632.4, rank 301 - exact match; the batch-10 monster.
// ----------------------------------------------------------------------------

/**
 * char * sym_(int symflg, unsigned char * a, unsigned char * b,
 * struct MSV * svPtr): the ENTIRE run-time local symbol table
 * machine (the vendor banner comment verbatim: all mumps local
 * variables are created and stored here; the first argument is the
 * operation code, second is the variable name incl. array marker
 * information, third is the incoming value to store or the result
 * buffer sent back). Operation codes kept verbatim: 0 store, 1
 * retrieve, 2 delete explicit, 3 <dollar>next, 4 kill all, 5 kill
 * all except..., 6 <dollar>data, 7 New except (...), 8 New
 * inclusive, 9 no-copy flag, 10 return variable name, 11 retrieve
 * variable only (no labels), 33 order, 99 push table, 100 store at
 * this level only, 101 retrieve next, 150 mark copy flag (=102,
 * the vendor aliases 102 on purpose), 200 call-by-reference store
 * (original name in svPtr.reference), 102 SymCPPdata /
 * SymMarkCopyFlag.
 *
 * Seat: the raised InterpStubs 6-arg stub signature is KEPT
 * (symflg, a, aOff, b, bOff, svPtr -> CharArray?; offsets carry
 * the vendor pointer-into-buffer positions, h007); the MdhStubs
 * 4-arg stub becomes a thin delegate (bmg_fullsearch delegate
 * precedent); the SymFree/_SymFree store-back call, the Cgi store
 * arms and every other call site bind unchanged.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro : printf -> System.out.print with the
 *    vendor text kept verbatim ("*** Too many symbol table layers"
 *    in both the 7 and 99 arms).
 *  - h007 char_buffer        : key[1024] is the key-fixed name
 *    buffer (pointer walk kp/ky with the top-bit strip); b is the
 *    caller's C-string CharArray (writes land at bOff); stab.name
 *    is the malloc'd NUL-closed CharArray twin (Stab carrier in
 *    SymFree.kt).
 *  - h008 carrier            : struct nmes (sym.h:29-31) renders as
 *    the internal class Nmes - PROMOTED to this seat and shared
 *    with Interpret.kt (the private per-file copy there is removed
 *    and binds here; svPtr.nstart stays the opaque Any? carrier).
 *  - h009 format_string      : sprintf(b, "%d", i) collapses to the
 *    decimal string (writeCstring-style); the <dollar>next /
 *    <dollar>order coded-number tail re-renders via
 *    String.format(Locale.US, "%g", x) with the sscanf %lf fold to
 *    trim().toDoubleOrNull ?: 0.0 (Unpad.kt row-289 twin shape).
 *  - h012 unsigned_byte_domain: keys/subscripts are byte domains;
 *    the 0x01 code byte and the 0x80 top-bit strip are byte-domain
 *    chars; the mkpt prototype the vendor defines but never calls
 *    stays unrendered (dead prototype, h013 ledger note).
 *  - h013 dead_code          : the commented strcpy/keyfix pair in
 *    the banner (the live walk below replaces it); the
 *    unreachable second goto after 'goto end' in the reverse-order
 *    arm (the vendor jumps straight past orderbackexit); the
 *    symflg==1 test inside the New-inclusive loop (symflg is 8
 *    there, so p2->copy = 1 never runs - kept verbatim as dead);
 *    the symflg==11 test inside the 9/101 arm (11 was already
 *    claimed by the retrieve arm above) - ledger notes.
 *  - h014 out_of_scope_call  : fullUnpad is DONE
 *    (InterpSysfuncMisc.kt); sigint is DONE (Sigint.kt); the
 *    __label_lookup gate-exception name is NOT emitted - the
 *    interpreter arm's documented default binding (-1 = not found,
 *    the SymMisc row-259 / InterpFcnMisc u202 ledger precedent)
 *    renders as the constant lblIdx with the i != -1 arm kept and
 *    naturally excluded.
 *  - h015 grammar_collision  : same name/signature as the removed
 *    InterpStubs stub; recursion (New-except -> sym_(100,...),
 *    New-inclusive -> sym_(0,...)) binds to THIS seat.
 *  - h017 naming             : sym_ keeps the vendor spelling; the
 *    SYMSTORE/SYMRETRIEVE consts (vendor #defines at the body top)
 *    render file-private.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The store arms' "return (char *) p1" (returning the stab
 *    POINTER as a char*) has no analog on the CharArray? carrier;
 *    the seat returns b (the value stored). The only in-vendor
 *    consumer of the pointer return is the New-inclusive loop,
 *    whose copy-flag write is dead anyway (h013 above), so the
 *    p2 return value is simply discarded - journaled.
 *  - The vendor mutates its symflg parameter as an exit-status
 *    side channel (symflg = 0/1/2 before returns); the caller's
 *    copy is by value, so the mutated value has no external
 *    reader - kept as the local var only, journaled.
 *  - The New-except arm (7) raises _SymPush but NEVER pushes a
 *    table level (no _Sym++ on that path) - the except-names are
 *    simply re-sown into the current level with sym_(100,...);
 *    kept exactly.
 *  - The 99 push arm checks _Sym > SYM_MAX AFTER the increment
 *    (SYM_MAX = 200, defines.h:218; the Msv.start carrier is 200
 *    deep); kept exactly (the out-of-bounds write is unreachable
 *    for valid states; quantized by the array bounds, u128).
 *  - The <dollar>order reverse arm walks to the end label
 *    directly (the vendor 'goto orderbackexit' after it is dead
 *    text, h013); its p2 seat is the entry BELOW the key.
 *  - The STRCPY squeeze while (b[0] == ' ') shifts the result
 *    left over the NUL - kept as the explicit substring fold.
 *  - In the 101 arm, the vendor tests 'if (p1 == NULL &&
 *    p1->next == NULL)' (deref before the null check) - the render
 *    tests p1 first; quantized identically (u128).
 *  - The back-scan 'for (i = i - 2; key[i] != 1; i--)' runs down
 *    with no lower bound in the vendor (keys always carry a
 *    leading code byte); the render adds the i >= 0 bound and
 *    quantizes the unterminated case (u128).
 */

import java.util.Locale

private const val SYMSTORE = 0    // vendor: #define SYMSTORE 0
private const val SYMRETRIEVE = 1 // vendor: #define SYMRETRIEVE 1
private const val SYM_MAX = 200   // vendor defines.h:218 (Msv.start depth)

/**
 * vendor sym.h:29-31 struct nmes (the New/Kill except-list node).
 * PROMOTED internal seat shared with Interpret.kt (its private
 * copy is removed; svPtr.nstart stays the opaque Any? carrier).
 */
internal class Nmes(var name: String, var next: Nmes?)

fun sym_(symflgIn: Int, a: CharArray, aOff: Int, b: CharArray, bOff: Int,
         svPtr: Msv): CharArray? {
    var symflg = symflgIn // vendor mutates the parameter (exit-status channel)

    // vendor: unsigned char key[1024]; the key-fixed name copy
    // (kp/ky walk: *ky = *kp & 0x80 ? 1 : *kp)
    val key = CharArray(1024)
    var kp = aOff
    var ky = 0
    while (a[kp] != '\u0000') {
        val c = a[kp]
        key[ky] = if ((c.code and 0x80) != 0) 1.toChar() else c
        kp++
        ky++
    }
    key[ky] = '\u0000'

    // ---------------- R E T R I E V E (1, 11) ----------------
    if (symflg == SYMRETRIEVE || symflg == 11) {
        var isym = svPtr._Sym
        while (isym >= 0) {
            var p1 = svPtr.start[isym]
            while (p1 != null) {
                if (strcmpC(p1.name ?: error("vendor UB quantized: strcmp(p1->name)"), key) == 0) {
                    // strcpy((char *) b, (*p1->data).c_str())
                    strcpyC(b, p1.data ?: error("vendor UB quantized: (*p1->data).c_str()"), bOff)
                    return b
                }
                p1 = p1.next
            }
            isym--
        }
        symflg = 0
        b[bOff] = '\u0000'
        return null
    }

    // ---------------- S T O R E (0, 100, 200, 102/150) ----------------
    if (symflg == SYMSTORE || symflg == 100 || symflg == 200 || symflg == 102) {
        // vendor: if (key[0] == <dollar> && strcasecmp(key,
        // "<dollar>noerr") == 0)
        if (key[0] == 36.toChar() && equalsIgnoreCaseC(key, "noerr")) {
            if (b[bOff] == '1') svPtr.NOERR = 1 else svPtr.NOERR = 0
            return b
        }

        var isym = svPtr._Sym
        var p2: Stab? = null
        var p3 = svPtr.start[isym]

        if (symflg == 100 || symflg == 200) { // search THIS level only
            p2 = null
            p3 = svPtr.start[isym]
            while (p3 != null) {
                val icmp = strcmpC(p3.name ?: error("vendor UB quantized"), key)
                if (icmp == 0) {
                    p3.data = cstringAt(b, bOff) // *(p3->data) = (char *) b
                    return b
                }
                if (icmp > 0) break // past place where it should be
                p2 = p3
                p3 = p3.next
            }
        } else { // 0 / 102: search this and lower levels
            while (isym >= 0) {
                p2 = null
                p3 = svPtr.start[isym]
                while (p3 != null) { // all-level scan requires exact match
                    val icmp = strcmpC(p3.name ?: error("vendor UB quantized"), key)
                    if (icmp == 0) {
                        p3.data = cstringAt(b, bOff) // *(p3->data) = (char *) b
                        return b
                    }
                    if (icmp > 0) break
                    p2 = p3
                    p3 = p3.next
                }
                isym--
            }
        }

        // insert: level 0 for plain stores, the searched level for
        // this-level-only stores (vendor isym selection, kept)
        val level = if (symflg == 100 || symflg == 200) svPtr._Sym else 0

        val p1n = Stab()
        p1n.OrigName = "" // p1->OrigName = new string
        // this-level-only store flags the level
        p1n.exec = if (symflg == 100 || symflg == 200) 1 else 0

        if (symflg == 200) {
            // *p1->OrigName = svPtr->reference
            p1n.OrigName = cstringAt(svPtr.reference)
        }

        p1n.name = key.copyOf(key.indexOf('\u0000') + 1) // malloc + strcpy

        var q2: Stab? = null
        var q3 = svPtr.start[level]
        while (q3 != null) { // insertion point at the target level
            if (strcmpC(q3.name ?: error("vendor UB quantized"), key) > 0) break
            q2 = q3
            q3 = q3.next
        }

        if (q2 == null) { // list empty or no earlier element
            p1n.next = q3
            svPtr.start[level] = p1n
        } else { // add to list
            p1n.next = q3
            q2.next = p1n
        }

        p1n.data = cstringAt(b, bOff) // p1->data = new string((char *) b)
        p1n.copy = 0
        return b // vendor: return (char *) p1 (pointer; quantized to b)
    }

    // ---------------- 9 / 101 ----------------
    if (symflg == 9 || symflg == 101) { // retrieve or zero copy flag

        var p1: Stab? = null
        var cr = 0

        run {
            var isym = svPtr._Sym
            while (isym >= 0) {
                p1 = svPtr.start[isym]
                while (p1 != null) {
                    cr = strcmpC(p1.name ?: error("vendor UB quantized"), key)
                    if (cr == 0) return@run // found
                    if (symflg == 101 && cr > 0) return@run // found next doodad
                    p1 = p1.next
                }
                isym--
            }
            p1 = null
        }

        if (p1 == null) {
            // vendor: if (symflg == 11) return NULL - dead: 11 was
            // claimed by the retrieve arm above (h013 ledger note)
            if (symflg == 9) {
                symflg = 0
                return null
            }

            // i = __label_lookup(key): the interpreter arm's
            // documented default binding (-1 = not found); the
            // i != -1 arm below is kept and naturally excluded.
            val lblIdx = -1
            if (lblIdx != -1) {
                strcpyC(b, lblIdx.toString(), bOff) // sprintf(b, "%d", i)
                symflg = 2
                return b
            }

            symflg = 0
            b[bOff] = '\u0000'
            return null
        }

        val pn = p1 ?: error("internal invariant: the found seat is non-null")

        if (symflg == 9) { // no copy flag
            symflg = 1
            pn.copy = 0
            return null
        }

        // symflg == 101: next absolute
        var px: Stab? = pn
        if (cr == 0) { // exact match - get next
            px = px?.next
            if (px == null) {
                b[bOff] = '\u0000' // strcpy((char *) b, "")
                return null
            }
        }

        var i = 0
        while (key[i] != 1.toChar() && key[i] != '\u0000') i++

        val pxn = px ?: error("vendor UB quantized: p1->next deref")
        if (strncmpC(pxn.name ?: error("vendor UB quantized"), key, i) == 0 &&
            (pxn.name ?: error("vendor UB quantized")).getOrNull(i) == 1.toChar()) {
            val tmp = CharArray(32000) // STR_MAX scratch
            val nm2 = pxn.name ?: error("vendor UB quantized")
            strcpyC(tmp, cstringAt(nm2), 0) // strcpy(tmp, p1->name)
            fullUnpad(tmp, svPtr)
            strcpyC(b, cstringAt(tmp), bOff)
            return b
        }
        b[bOff] = '\u0000' // strcpy((char *) b, "")
        return null
    }

    // ---------------- <dollar>order pre-arm (33) ----------------
    if (symflg == 33) {
        val i = key.indexOf('\u0000') // strlen(key)
        if (svPtr.order < 0) { // reverse the string
            if (key[i - 1] == 1.toChar() && key[i - 2] == 1.toChar()) {
                key[i] = 0x80.toChar()
                key[i + 1] = '\u0000'
                // vendor: key[i-1] holds the last 0x01 code byte and is
                // folded into the appended 0x80 tail; kept exactly.
            }
        }
    }

    // ---------------- <dollar>next pre-arm (3) ----------------
    if (symflg == 3) {
        val i = key.indexOf('\u0000')
        if (key[i - 3] == '-' && key[i - 2] == '1' && key[i - 1] == 1.toChar()) {
            key[i - 3] = 1.toChar()
            key[i - 2] = '\u0000'
            // i -= 2 fold: the mutated key now ends one code byte
            // early; endProcessing recomputes strlen itself.
        }
    }

    // ---------------- <dollar>next / <dollar>order scan (3, 33) ----------------
    if (symflg == 3 || symflg == 33) {

        // fail tail: o_fail -> strcpy(b, -1 or empty) -> exit tail
        fun fail(): CharArray {
            if (symflg == 3) strcpyC(b, "-1", bOff) else b[bOff] = '\u0000'
            symflg = 1
            return finishOrderTail(b, bOff)
        }

        // the vendor 'end' label: recompute the code-byte index on
        // the CURRENT (post-mutation) key, i = strlen - 2.
        fun endIndex(): Int {
            var j = 0
            while (key[j] != '\u0000') j++ // strlen(key)
            var i = j - 2
            while (i >= 0 && key[i] != 1.toChar()) i-- // quantized bound
            return i
        }

        fun endProcessing(px: Stab): CharArray {
            val i = endIndex()
            if (strncmpC(key, px.name ?: error("vendor UB quantized"), i) != 0) {
                return fail()
            }
            val nm = px.name ?: error("vendor UB quantized")
            if (nm.getOrNull(i) == '\u0000') {
                b[bOff] = '\u0000' // scalar variable with same name
                symflg = 1
                return finishOrderTail(b, bOff)
            }
            strcpyC(b, cstringAt(nm, i + 1), bOff) // strcpy(b, p1->name + i + 1)
            symflg = 1
            return finishOrderTail(b, bOff)
        }

        var isym = svPtr._Sym
        o_again@ while (true) {
            var p1 = svPtr.start[isym]
            var p2: Stab? = null

            while (p1 != null) { // looking for entry GT or EQ key
                if (strncmpC(p1.name ?: error("vendor UB quantized"), key, strlenC(key)) >= 0) break
                p2 = p1
                p1 = p1.next
            }

            if (symflg == 33 && svPtr.order < 0) { // reverse order

                if (isym > 0 && p2 == null) { // not finished with sym table
                    isym--
                    continue@o_again
                }

                if (p2 == null) { // search fail
                    return fail()
                }

                // vendor jumps directly to 'end' (the second
                // 'goto orderbackexit' is dead text, h013 note)
                return endProcessing(p2)
            }

            if (p1 == null) { // search key GT all stored names
                if (isym > 0) { // more sym table levels to check
                    isym--
                    continue@o_again
                }
                return fail()
            }

            if (strncmpC(p1.name ?: error("vendor UB quantized"), key, strlenC(key)) > 0) {
                // stored name GT key: goto end
                return endProcessing(p1)
            }

            // found EQ key fragment: advance to 1st NE to key
            var px = p1.next
            while (px != null) {
                if (strncmpC(px.name ?: error("vendor UB quantized"), key, strlenC(key)) != 0) break
                px = px.next
            }

            if (px == null) { // none greater found
                return fail()
            }

            // end label
            return endProcessing(px)
        }
    }

    // ---------------- kill selected (2) ----------------
    if (symflg == 2) {
        var isym = svPtr._Sym
        while (isym >= 0) {
            // vendor 'repeat' label
            while (true) {
                var p1 = svPtr.start[isym]
                var p2: Stab? = null
                val i = strlenC(key)

                while (p1 != null) {
                    val nm = p1.name ?: error("vendor UB quantized")
                    if (strncmpC(nm, key, i) == 0 && nm.getOrNull(i) == 1.toChar()) break
                    if (strcmpC(nm, key) == 0) break
                    p2 = p1
                    p1 = p1.next
                }

                if (p1 == null) break // continue the outer level walk
                if (p2 == null) svPtr.start[isym] = p1.next else p2.next = p1.next
                // delete p1->data/name/OrigName/p1 - GC'd; unlink kept
            }
            isym--
        }
        return null
    }

    // ---------------- kill all (4) ----------------
    if (symflg == 4) {
        var p1 = svPtr.start[svPtr._Sym]
        while (p1 != null) {
            val p2 = p1.next
            p1.data = null     // delete p1->data
            p1.name = null     // free(p1->name)
            p1.OrigName = null // delete p1->OrigName
            p1 = p2
        }
        svPtr.start[svPtr._Sym] = null
        symflg = 1
        return null
    }

    // ---------------- kill all except (5) ----------------
    if (symflg == 5) {
        var p1 = svPtr.start[svPtr._Sym]
        var p3: Stab? = null
        outer@ while (p1 != null) {
            // loop-head null test, local carrier (the only-null
            // exit path is the loop condition itself)
            val node = p1 ?: continue@outer
            var np1 = svPtr.nstart as Nmes?
            var flg = 0
            while (np1 != null) { // search the except list
                val i = np1.name.length - 1 // strlen(np1->name) - 1
                val nameS = cstringAt(node.name ?: error("vendor UB quantized"))
                if ((strncmpS(np1.name, nameS, i) == 0 &&
                        nameS.getOrNull(i) == 1.toChar()) ||
                    np1.name == nameS) {
                    p3 = node
                    p1 = node.next
                    flg = 1 // don't delete p1
                    break
                }
                np1 = np1.next
            }

            if (flg == 1) continue@outer

            // delete p1
            node.data = null
            node.name = null
            node.OrigName = null
            val p2 = node.next
            if (p3 == null) svPtr.start[svPtr._Sym] = p2 else p3.next = p2
            p1 = p2
        }

        var np1 = svPtr.nstart as Nmes?
        while (np1 != null) { // cleanup the except list
            val np2 = np1.next
            np1.name = "" // free(np1->name)
            np1 = np2
        }
        svPtr.nstart = null // list freed in sym_() (vendor comment)

        symflg = 1
        return null
    }

    // ---------------- <dollar>data (6) ----------------
    if (symflg == 6) {
        var found: Stab? = null
        run {
            var isym = svPtr._Sym
            while (isym >= 0) {
                var p1 = svPtr.start[isym]
                while (p1 != null) {
                    if (strcmpC(p1.name ?: error("vendor UB quantized"), key) == 0) {
                        found = p1
                        return@run
                    }
                    p1 = p1.next
                }
                isym--
            }
        }

        if (found == null) { // search fail
            val i = strlenC(key)
            var p1 = svPtr.start[svPtr._Sym]
            while (p1 != null) {
                if (strncmpC(p1.name ?: error("vendor UB quantized"), key, i) == 0) break
                p1 = p1.next
            }

            if (p1 != null) {
                var ii = i
                if (key[i - 1] == 1.toChar()) ii--

                if ((p1.name ?: error("vendor UB quantized")).getOrNull(ii) != 1.toChar()) {
                    b[bOff] = '0'
                    b[bOff + 1] = '\u0000'
                    b[bOff + 2] = '\u0000'
                    return b
                } else {
                    strcpyC(b, "10", bOff)
                    return b
                }
            }

            b[bOff] = '0'
            b[bOff + 1] = '\u0000'
            return b
        }

        b[bOff] = '1' // exists
        val p1 = found.next
        if (p1 == null) { // no possible descendants
            b[bOff + 1] = '\u0000'
            return b
        }

        val i = strlenC(key)
        if (strncmpC(key, p1.name ?: error("vendor UB quantized"), i) == 0) {
            b[bOff + 1] = '1'
            b[bOff + 2] = '\u0000'
            return b
        }

        b[bOff + 1] = '\u0000'
        return b
    }

    // ---------------- New except (7) ----------------
    if (symflg == 7) {
        var begin: Nmes? = null

        var isym = svPtr._Sym
        while (isym >= 0) { // build unique list of variable names
            var symtab = svPtr.start[isym]
            while (symtab != null) {
                var flg = 1
                var np2 = begin
                while (np2 != null) { // in list already?
                    if (np2.name == cstringAt(symtab.name ?: error("vendor UB quantized"))) flg = 0
                    np2 = np2.next
                }
                np2 = svPtr.nstart as Nmes?
                while (np2 != null) { // in except list?
                    if (np2.name == cstringAt(symtab.name ?: error("vendor UB quantized"))) flg = 0
                    np2 = np2.next
                }
                if (flg == 1) {
                    // np3 = new nmes; assign; np3->next = begin; begin = np3
                    begin = Nmes(cstringAt(symtab.name ?: error("vendor UB quantized")), begin)
                }
                symtab = symtab.next
            }
            isym--
        }

        svPtr._SymPush++
        if (svPtr._Sym > SYM_MAX) {
            System.out.print("*** Too many symbol table layers")
            sigint(100)
        }
        var np2 = begin
        while (np2 != null) {
            sym_(100, toCbufLocal(np2.name), 0, toCbufLocal(""), 0, svPtr)
            np2 = np2.next
        }
        // vendor falls through WITHOUT pushing a table level
        // (no _Sym++ on this path); kept exactly.
    }

    // ---------------- push table (99) ----------------
    if (symflg == 99) {
        svPtr._SymPush++
        svPtr._Sym++
        svPtr.start[svPtr._Sym] = null
        if (svPtr._Sym > SYM_MAX) {
            System.out.print("*** Too many symbol table layers")
            sigint(100)
        }
        return null
    }

    // ---------------- New inclusive (8) ----------------
    if (symflg == 8) {
        var p1 = svPtr.start[svPtr._Sym]
        svPtr._Sym++
        svPtr.start[svPtr._Sym] = null

        while (p1 != null) {
            // p2 = (struct stab *) sym_(0, (unsigned char *)
            // p1->name, (unsigned char *) (*p1->data).c_str(), ...)
            val p2 = sym_(0, toCbufLocal(cstringAt(p1.name
                ?: error("vendor UB quantized"))), 0,
                toCbufLocal(p1.data ?: error("vendor UB quantized")), 0, svPtr)
            if (symflg == 1) {
                // dead: symflg is 8 here, so p2->copy = 1 never
                // runs (h013; kept verbatim as dead)
            }
            p1 = p1.next
        }

        symflg = 1
        return null
    }

    // ---------------- return variable name (10) ----------------
    if (symflg == 10) {
        b[bOff] = '\u0000'
        var t = bOff
        var s = aOff
        while (a[s] != '\u0000') { // strcpy(b, a)
            b[t++] = a[s]
            s++
        }
        b[t] = '\u0000'
        symflg = 1
        return b
    }

    return null
}

/**
 * The exit_processing tail of the 3/33 arm: NUL-close at the
 * first code byte, squeeze the leading spaces, then the 0x1f
 * coded-number re-render (the Unpad.kt row-289 twin shape).
 */
private fun finishOrderTail(b: CharArray, bOff: Int): CharArray {
    // for (i = 0; b[i] != 1 && b[i] != 0; i++); b[i] = 0
    var t = bOff
    while (b[t] != 1.toChar() && b[t] != '\u0000') t++
    b[t] = '\u0000'

    // while (b[0] == ' ') squeeze one left shift over the NUL
    var s = cstringAt(b, bOff)
    while (s.startsWith(" ")) s = s.substring(1)
    if (s != cstringAt(b, bOff)) {
        var t2 = bOff
        var i2 = 0
        while (i2 < s.length) {
            b[t2++] = s[i2]
            i2++
        }
        b[t2] = '\u0000'
    }

    // if (*b == '\x1f'): r = b + 1; sign fix; memcpy(tmp, r, 40);
    // tmp[40] = 0; sscanf %lf; sprintf "%lg"
    if (b[bOff] == 0x1f.toChar()) {
        val tmp = CharArray(64) // vendor unsigned char tmp[64]
        val r = bOff + 1
        if (b[r] == '0') b[r] = '-' else b[r] = '+'
        for (k in 0 until 40) { // memcpy(tmp, r, 40)
            tmp[k] = if (b[r + k] != '\u0000') b[r + k] else '\u0000'
        }
        tmp[40] = '\u0000'
        val x = cstringAt(tmp).trim().toDoubleOrNull() ?: 0.0 // sscanf %lf
        strcpyC(b, String.format(Locale.US, "%g", x), bOff) // sprintf "%lg"
    }
    return b
}

/** strcmp over two NUL-closed CharArrays starting at 0. */
private fun strcmpC(x: CharArray, y: CharArray): Int {
    var i = 0
    while (true) {
        val c1 = x[i]
        val c2 = y[i]
        if (c1 != c2 || c1 == '\u0000') {
            return if (c1 == c2) 0
            else if (c1.code < c2.code) -1 else 1
        }
        i++
    }
}

/** strncmp(p1->name, key, n): CharArray vs CharArray, n bytes. */
private fun strncmpC(x: CharArray, y: CharArray, n: Int): Int {
    var i = 0
    while (i < n) {
        val c1 = x.getOrElse(i) { '\u0000' }
        val c2 = y.getOrElse(i) { '\u0000' }
        if (c1 != c2) return if (c1.code < c2.code) -1 else 1
        if (c1 == '\u0000') return 0
        i++
    }
    return 0
}

/** strncmp(np1->name, p1->name, i): String prefix vs n chars. */
private fun strncmpS(s: String, t: String, n: Int): Int {
    var i = 0
    while (i < n) {
        val c1 = s.getOrNull(i)
        val c2 = t.getOrNull(i)
        if (c1 == null || c2 == null) {
            return if (c1 == null && c2 == null) 0
            else if (c1 == null) -1 else 1
        }
        if (c1 != c2) return if (c1 < c2) -1 else 1
        if (c1 == '\u0000') return 0
        i++
    }
    return 0
}

/** strcasecmp(key, "noerr") fold (the key's leading 36-toChar()
 *  dollar byte is checked by the caller). */
private fun equalsIgnoreCaseC(key: CharArray, s: String): Boolean {
    for (i in s.indices) {
        val kc = key[i].lowercaseCharV()
        val sc = s[i]
        if (kc != sc.lowercaseCharV()) return false
    }
    return key[s.length] == '\u0000'
}

/** tolower fold for the strcasecmp shape (ASCII domain). */
private fun Char.lowercaseCharV(): Char =
    if (this in 'A'..'Z') (this.code + 32).toChar() else this

/** strlen fold: index of the NUL byte. */
private fun strlenC(x: CharArray): Int {
    var i = 0
    while (x[i] != '\u0000') i++
    return i
}

/** fresh NUL-terminated CharArray from String (USTR fold, h007). */
private fun toCbufLocal(s: String): CharArray {
    val ret = CharArray(s.length + 1)
    for (i in s.indices) ret[i] = s[i]
    ret[s.length] = '\u0000'
    return ret
}
