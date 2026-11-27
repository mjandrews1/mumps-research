package both

// interp-sym.cpp::sym_ - roster 307, batch B10, both_seq 184,
// cpp, complex stratum, loc 484, score 632.4. Vendor
// interp-sym.cpp:144-627: the run-time symbol table dispatcher
// (all mumps local variables are created and stored here; the
// symflg op codes are the vendor banner's: 0 store/create,
// 1 retrieve, 2 kill explicit, 3 next on last argument, 4 kill
// all, 5 kill all except, 6 data, 7 New except, 8 New inclusive,
// 9 no copy flag, 10 return variable name, 11 retrieve no
// labels, 33 order, 99 push table, 100 store this level only,
// 101 retrieve next absolute, 150 mark copy flag, 200 call by
// reference; the store arm also admits SymCPPdata /
// SymMarkCopyFlag, both 102 per defines.h - the same-value
// double define kept, DefinesMisc.kt:60-61).
//
// Consults: h017 hit (the vendor name sym_ keeps; the seat
// signature matches the pre-existing MpstimeMain stub
// sym_(op, name, data, sv): CharArray?, whose TODO body is
// REMOVED at this render - the pin there already said roster
// 307); h019 reuse (the stab entries seat on the Stab carrier,
// BothStubs.kt:79, name/next/data/OrigName/size/copy/exec); h020
// hit (the C pointer choreography quantizes: the new/malloc/
// delete/free pairs fold onto GC; the (char*)p1 store-path
// return - a stab POINTER cast to char* - folds onto the data
// echo below; the a==NULL guard quantizes onto the non-null
// String carrier); h013 hit (the symflg dead stores - symflg is
// a by-value param, every symflg=... before a return is dead -
// and the dead if (symflg == 1) p2->copy = 1 inside the
// symflg==8 arm, and the dead symflg==11 test inside the 9/101
// arm, kept as ledgered comments); h016 reuse (the key[1024]
// normalization buffer, the STR_MAX scratch in the 101 arm, the
// 64-byte coded-number window); h007 reuse (in-place buffer
// rewrites: the leading-space strip loop shifts b onto itself).
//
// Carrier notes:
// - key normalization: bytes with the high bit set fold to the
//   \u0001 subscript marker (the vendor *kp & 0x80 ? 1 : *kp
//   walk); the String carrier performs the same per-char map.
// - the strcmp/strncmp lattice runs on Kotlin String compareTo
//   (char domain) against the vendor unsigned-byte domain; the
//   stored names are ASCII + \u0001 markers, where the two
//   orders agree (quantized, journaled).
// - b is the data param, in and out: the vendor writes results
//   into b and returns it; this render writes through writeC
//   and returns the same array (callers pass scratch buffers
//   and read them after the call). NULL returns return null.
// - the stab linked lists live on sv.start[] / sv._Sym / the
//   Nmes nstart chain (CompilerGlobalsExtMisc.kt:42).
//
// Raised stub: fullUnpadZb (the 101 arm's fullUnpad call; owner
// interp-sysfunc.cpp:969, non-roster cross-TU helper). The
// __label_lookup pointer defaults to _label_lookup which
// returns -1 (libmpscpp.h _MDH_ block, roster 305 ledger); the
// interpreter build keeps the default - rendered as the -1
// fold with the rebinding (Zcommands emits __label_lookup =
// _label_lookup) cross-cited.

// void fullUnpad(unsigned char * key, struct MSV * svPtr) -
// interp-sysfunc.cpp:969; the key depad service. Non-roster
// cross-TU helper (the interp-sysfunc.cpp residue owns it).
// TODO-stub (h014).
internal fun fullUnpadZb(key: CharArray, sv: Msv) {
    TODO("stub: owner interp-sysfunc.cpp::fullUnpad (non-roster cross-TU helper)")
}

// The _MDH_ default: int _label_lookup(char * x) { return -1; }
// (libmpscpp.h, the roster-305 ledger; the compiler runtime
// rebinds __label_lookup - the Zcommands emitted source does
// it; this arm keeps the interpreter default).
private fun labelLookupDefaultC(key: String): Int = -1

private const val SYM_MARK = '\u0001' // the 0x80-fold subscript marker

// store the result into b (the strcpy(b, ...) fold) and return it
private fun putB(b: CharArray, s: String): CharArray {
    writeC(b, s)
    return b
}

internal fun sym_(op: Int, name: String, data: CharArray, sv: Msv): CharArray? {
    var symflg = op // the vendor mutates its by-value param (h013 dead stores below)

    // unsigned char key[1024]; the high-bit fold
    val key = StringBuilder()
    for (c in name) {
        key.append(if (c.code and 0x80 != 0) SYM_MARK else c)
    }
    val keyS = key.toString()

    // struct stab *p1, *p2, *p3 - the walk cursors
    var p1: Stab? = null
    var p2: Stab? = null
    var p3: Stab? = null

    // ---------------- R E T R I E V E (1 / 11) ----------------
    if (symflg == 1 || symflg == 11) {
        var isym = sv._Sym
        while (isym >= 0) {
            p1 = sv.start[isym]
            while (p1 != null) {
                if (p1.name == keyS) {
                    return putB(data, p1.data ?: "")
                }
                p1 = p1.next
            }
            isym--
        }
        symflg = 0 // dead store (h013, the vendor sets its param)
        data[0] = '\u0000'
        return null
    }

    // ---------------- S T O R E (0 / 100 / 200 / 102) ----------
    if (symflg == 0 || symflg == 100 || symflg == 200 ||
        symflg == SymCPPdata || symflg == SymMarkCopyFlag
    ) {
        // $noerr special variable
        if (keyS.isNotEmpty() && keyS[0] == '$' &&
            keyS.equals("\u0024noerr", ignoreCase = true)
        ) {
            if (data.isNotEmpty() && data[0] == '1') sv.NOERR = 1 else sv.NOERR = 0
            return data
        }

        var isym = sv._Sym
        p2 = null
        var foundP3: Stab? = null
        var p2Keep: Stab? = null
        var p3Keep: Stab? = null
        var matched = false

        while (isym >= 0) { // search this and lower levels
            p2 = null
            p3 = sv.start[isym]
            while (p3 != null) { // all level scan requires exact match
                val icmp = (p3.name ?: "").compareTo(keyS)
                if (icmp == 0) {
                    p3.data = cStringOf(data) // *(p3->data) = (char *) b
                    return data
                }
                if (icmp > 0) break // past place where it should be
                p2 = p3
                p3 = p3.next
            }
            if (symflg == 100 || symflg == 200) {
                p2Keep = p2
                p3Keep = p3
                matched = true
                break // store this level only
            }
            isym--
        }
        if (!matched) {
            p2Keep = p2
            p3Keep = p3
        }

        if (symflg == 0) { // ***************************
            isym = 0 // searched to the bottom

            p1 = Stab() // new struct stab (the malloc/OOM-print pairs
            // quantize onto GC, h020)
            p1.OrigName = "" // p1->OrigName = new string
            p1.exec = 0
            p1.name = keyS

            if (p2Keep == null) { // no previous: list empty or no earlier element
                p1.next = p3Keep
                sv.start[isym] = p1
            } else {
                p1.next = p3Keep
                p2Keep.next = p1
            }

            p1.data = cStringOf(data) // p1->data = new string((char *) b)
            p1.copy = 0
            // return (char *) p1 - the stab-pointer return quantizes
            // onto the data echo (h020 ledger)
            return data
        }

        // the 100/200/102 fall-through: insert at this level
        isym = if (symflg == 100 || symflg == 200) sv._Sym else 0

        p2 = null
        p3 = sv.start[isym]
        while (p3 != null) {
            if ((p3.name ?: "").compareTo(keyS) > 0) break
            p2 = p3
            p3 = p3.next
        }

        p1 = Stab()

        // if this is a 'this level only' store (100/200), flag the level
        p1.exec = if (symflg == 100 || symflg == 200) 1 else 0

        // symflg == 200: call by reference var - the original name
        // sits in svPtr->reference; else the empty string
        p1.OrigName = if (symflg == 200) cStringOf(sv.reference) else ""

        p1.name = keyS

        if (p2 == null) { // no previous
            p1.next = p3
            sv.start[isym] = p1
        } else {
            p1.next = p3
            p2.next = p1
        }

        p1.data = cStringOf(data)
        p1.copy = 0
        return data // (char *) p1 quantized as above
    }

    // ------------- 9 (no copy flag) / 101 (next absolute) ------
    if (symflg == 9 || symflg == 101) {
        var cr = 0
        p1 = null
        var isym = sv._Sym
        while (isym >= 0) {
            p1 = sv.start[isym]
            while (p1 != null) {
                cr = (p1.name ?: "").compareTo(keyS)
                if (cr == 0) break
                if (symflg == 101 && cr > 0) break
                p1 = p1.next
            }
            if (p1 != null) break
            isym--
        }

        if (p1 == null) {
            // if (symflg == 11) return NULL; - DEAD (h013): the arm
            // only enters at 9 or 101
            if (symflg == 9) {
                symflg = 0 // dead store (h013)
                return null
            }

            val iL = labelLookupDefaultC(keyS)
            if (iL != -1) {
                symflg = 2 // dead store (h013)
                return putB(data, iL.toString())
            }

            symflg = 0 // dead store (h013)
            data[0] = '\u0000'
            return null
        }

        // found:
        if (symflg == 9) { // no copy flag
            symflg = 1 // dead store (h013)
            p1.copy = 0
            return null
        }

        // symflg == 101: next absolute
        // if (p1 == NULL && p1->next == NULL) - DEAD (h013): p1 is
        // non-null at found:
        if (cr == 0) {
            if (p1.next == null) {
                return putB(data, "")
            }
            p1 = p1.next // exact match - get next
        }

        var i = 0
        while (i < keyS.length && keyS[i] != SYM_MARK) i++

        val p1name = p1!!.name ?: ""
        if (i <= p1name.length && strncmpC(keyS, p1name, i) == 0 &&
            i < p1name.length && p1name[i] == SYM_MARK
        ) {
            val tmp = CharArray(STR_MAX)
            writeC(tmp, p1name)
            fullUnpadZb(tmp, sv)
            return putB(data, cStringOf(tmp))
        }
        return putB(data, "")
    }

    // ---------------- $order key tweak (33) ---------------------
    // key[i-1]='\x80'; key[i]=1; key[++i]=0 on the double-marker
    // tail (reverse order only): the 0x80 byte is the high-sort
    // sentinel (unsigned-byte domain); the char carrier keeps it
    // as \u0080 (same ordering vs the \u0001 markers), the new
    // strlen i = old len + 1.
    var i = keyS.length
    var keyWork = keyS
    if (symflg == 33 && sv.order < 0) {
        if (keyS.length >= 2 && keyS[keyS.length - 1] == SYM_MARK &&
            keyS[keyS.length - 2] == SYM_MARK
        ) {
            keyWork = keyS.substring(0, keyS.length - 1) + '\u0080' + SYM_MARK
            i = keyWork.length
        }
    }

    // ---------------- $next key tweak (3) -----------------------
    // the '-1' subscript tail collapses to a bare marker:
    // key[i-3]=1; key[i-2]=0; i = i - 2 (the new strlen).
    if (symflg == 3 &&
        keyS.length >= 3 && keyS[keyS.length - 3] == '-' &&
        keyS[keyS.length - 2] == '1' && keyS[keyS.length - 1] == SYM_MARK
    ) {
        keyWork = keyS.substring(0, keyS.length - 3) + SYM_MARK
        i = keyWork.length
    }

    if (symflg == 3 || symflg == 33) { // next or order
        return runOrderNext(symflg, keyWork, data, sv)
    }

    // ---------------- KILL selected (2) -------------------------
    if (symflg == 2) {
        var isym = sv._Sym
        while (isym >= 0) {
            // repeat:
            while (true) {
                p1 = sv.start[isym]
                p2 = null
                i = keyS.length

                while (p1 != null) {
                    val p1n = p1.name ?: ""
                    if (i <= p1n.length && strncmpC(p1n, keyS, i) == 0 &&
                        i < p1n.length && p1n[i] == SYM_MARK
                    ) break
                    if (p1n == keyS) break
                    p2 = p1
                    p1 = p1.next
                }

                if (p1 == null) break

                if (p2 == null) {
                    sv.start[sv._Sym] = p1.next // NOTE: the vendor indexes
                    // start[svPtr->_Sym] here, NOT start[isym] (kept)
                } else {
                    p2.next = p1.next
                }
                // delete/free choreography quantizes (h020)
            }
            isym--
        }
        return null
    }

    // ---------------- kill all (4) ------------------------------
    if (symflg == 4) {
        p1 = sv.start[sv._Sym]
        while (p1 != null) {
            p2 = p1.next
            p1 = p2
        }
        sv.start[sv._Sym] = null
        symflg = 1 // dead store (h013)
        return null
    }

    // ---------------- kill all except (5) ----------------------
    if (symflg == 5) {
        var p3x: Stab? = null
        p1 = sv.start[sv._Sym]
        while (p1 != null) {
            var np1 = sv.nstart
            var flg = false
            while (np1 != null) {
                i = np1.name.length - 1
                val p1n = p1!!.name ?: ""
                if ((i <= p1n.length && strncmpC(np1.name, p1n, i) == 0 &&
                        i < p1n.length && p1n[i] == SYM_MARK) || np1.name == p1n
                ) {
                    p3x = p1
                    p1 = p1.next
                    flg = true // don't delete p1
                    break
                }
                np1 = np1.next
            }

            if (flg) continue
            val nx = p1!!.next
            if (p3x == null) {
                sv.start[sv._Sym] = nx
            } else {
                p3x.next = nx
            }
            p1 = nx
        }

        // the nmes chain cleanup (free choreography quantizes)
        sv.nstart = null
        symflg = 1 // dead store (h013)
        return null
    }

    // ---------------- $data (6) ---------------------------------
    if (symflg == 6) {
        p1 = null
        var isym = sv._Sym
        while (isym >= 0) {
            p1 = sv.start[isym]
            while (p1 != null) {
                if (p1.name == keyS) break
                p1 = p1.next
            }
            if (p1 != null) break
            isym--
        }

        if (p1 == null) { // search fail
            i = keyS.length
            var q = sv.start[sv._Sym]
            while (q != null) {
                if (strncmpC(q.name ?: "", keyS, i) == 0) break
                q = q.next
            }
            if (q != null) {
                if (i > 0 && keyS[i - 1] == SYM_MARK) i--
                val qn = q.name ?: ""
                if (i >= qn.length || qn[i] != SYM_MARK) {
                    return putB(data, "0")
                }
                return putB(data, "10")
            }
            return putB(data, "0")
        }

        // exists
        val p1n = p1.name ?: ""
        val next1 = p1.next
        if (next1 == null) { // no possible descendants
            return putB(data, "1")
        }

        i = keyS.length
        if (i <= (next1.name ?: "").length &&
            strncmpC(keyS, next1.name ?: "", i) == 0
        ) {
            return putB(data, "11")
        }
        return putB(data, "1")
    }

    // ---------------- New except (7) ----------------------------
    if (symflg == 7) {
        var begin: Nmes? = null
        var isym = sv._Sym
        while (isym >= 0) { // build unique list of variable names
            var symtab = sv.start[isym]
            while (symtab != null) {
                var flg = 1
                var np2 = begin
                while (np2 != null) {
                    if (np2.name == symtab.name) flg = 0 // in list already?
                    np2 = np2.next
                }
                np2 = sv.nstart
                while (np2 != null) {
                    if (np2.name == symtab.name) flg = 0 // in except list?
                    np2 = np2.next
                }
                if (flg == 1) {
                    begin = Nmes(symtab.name ?: "", begin) // np3->next = begin
                }
                symtab = symtab.next
            }
            isym--
        }
        sv._SymPush++
        // the vendor prints and sigints AFTER the increment
        // (svPtr->_Sym>SYM_MAX never trips on the check order -
        // off-by-one quirk kept; the sigint seat exists, the
        // print is kept verbatim)
        if (sv._Sym > SYM_MAX) {
            println("*** Too many symbol table layers")
            sigint(100)
        }
        var np2 = begin
        while (np2 != null) {
            val empty = CharArray(1)
            sym_(100, np2.name, empty, sv)
            np2 = np2.next
        }
        // falls through to the final return NULL
    }

    // ---------------- push table (99) ---------------------------
    if (symflg == 99) {
        sv._SymPush++
        sv._Sym++
        sv.start[sv._Sym] = null
        if (sv._Sym > SYM_MAX) {
            println("*** Too many symbol table layers")
            sigint(100)
        }
        return null
    }

    // ---------------- New inclusive (8) --------------------------
    if (symflg == 8) {
        p1 = sv.start[sv._Sym]
        sv._Sym++
        sv.start[sv._Sym] = null

        while (p1 != null) {
            val buf = CharArray(STR_MAX)
            writeC(buf, p1.data ?: "")
            val p2r = sym_(0, p1.name ?: "", buf, sv)
            // if (symflg == 1) p2->copy = 1; - DEAD (h013):
            // symflg is 8 in this arm; kept as the ledger
            p1 = p1.next
        }

        symflg = 1 // dead store (h013)
        return null
    }

    // ---------------- return variable name (10) ------------------
    if (symflg == 10) {
        symflg = 1 // dead store (h013)
        return putB(data, name)
    }

    return null
}

// ------------------------------------------------------------------------
// The $next / $order walk (symflg 3 / 33) - the o_again/end/exit
// block of the vendor body, factored as a private kernel to keep
// the goto lattice readable. Every branch maps a vendor goto, per
// the inline comments.
// ------------------------------------------------------------------------

private fun runOrderNext(symflg: Int, key: String, b: CharArray, sv: Msv): CharArray? {
    var i = key.length

    var isym = sv._Sym

    var p1: Stab? = null // the end: target entry

    // o_again:
    o_again@ while (true) {
        var q = sv.start[isym]
        var p2: Stab? = null

        while (q != null) { // looking for entry GT or EQ key
            if (strncmpC(q.name ?: "", key, i) >= 0) break
            p2 = q
            q = q.next
        }

        if (symflg == 33 && sv.order < 0) { // reverse order
            if (isym > 0 && p2 == null) { // not finished with sym table
                isym--
                continue@o_again
            }
            if (p2 == null) {
                writeC(b, "")
                return b
            }
            // for (i=i-2; key[i] != 1; i--); p1 = p2; goto end;
            // NOTE: the end: block below RE-SETS i = strlen(key)-2,
            // so this scan is a DEAD recomputation (h013 ledger);
            // only the p1 = p2 assignment survives to end:
            p1 = p2
            break@o_again // goto end
        }

        if (q == null) { // none found - search key GT all stored names
            if (isym > 0) { // more sym table levels to check
                isym--
                continue@o_again
            }
            writeC(b, if (symflg == 3) "-1" else "")
            return b
        }

        if (strncmpC(q.name ?: "", key, i) > 0) { // stored name GT key
            p1 = q
            break@o_again // goto end
        }

        // found EQ key fragment: advance to 1st NE to key
        var r = q.next
        while (r != null) {
            if (strncmpC(r.name ?: "", key, i) != 0) break
            r = r.next
        }

        if (r == null) { // none greater found
            writeC(b, if (symflg == 3) "-1" else "")
            return b
        }
        p1 = r
        break@o_again // fallthrough to end
    }

    // end:
    i = key.length - 2 // back off to prior code lev (both 3 and 33)

    while (key[i] != SYM_MARK) i--

    if (strncmpC(key, p1?.name ?: "", i) != 0) {
        writeC(b, if (symflg == 3) "-1" else "")
        return b
    }

    // orderbackexit:
    val p1n = p1?.name ?: ""
    if (i >= p1n.length || p1n[i] == '\u0000') {
        b[0] = '\u0000' // scalar var with same name
        return b
    }
    writeC(b, p1n.substring(i + 1)) // strcpy(b, p1->name + i + 1)

    var j = 0
    while (j < b.size && b[j] != SYM_MARK && b[j] != '\u0000') j++
    if (j < b.size) b[j] = '\u0000'

    // while (b[0]==' ') shift left in place (strcpy onto self, h007)
    while (b[0] == ' ') {
        var x = 0
        while (x < b.size && b[x] != '\u0000') {
            b[x] = if (x + 1 < b.size) b[x + 1] else '\u0000'
            x++
        }
    }

    // exit_processing:
    if (b[0] == '\u001f') { // coded number - kok
        // r++; if (*r=='0') *r='-' else '+'; memcpy(tmp,r,40)
        val w = CharArray(41)
        var r = 1
        var first = true
        while (r < 41 && r < b.size && b[r] != '\u0000') {
            var c = b[r]
            if (first) {
                c = if (c == '0') '-' else '+'
                first = false
            }
            w[r - 1] = c
            r++
        }
        val x = strtodC(cStringOf(w)) // sscanf(tmp, "%lf", &x)
        writeC(b, fmtG(x)) // sprintf(b, "%lg", x)
    }

    return b
}
