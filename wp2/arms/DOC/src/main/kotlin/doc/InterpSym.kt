package doc

// Unit 300 (B10 tranche 3): interp-sym.cpp::sym_ (batch 10,
// complex stratum; units.csv row 280: loc 484, cr 0.125, ptr 201,
// ctl 134, score 632.4, rank 301) - THE RUN TIME SYMBOL TABLE.
// All mumps local variables are created and stored here: the
// first argument is the operation code, the second the variable
// name (with array marker information), the third the incoming
// value or result buffer. The full vendor banner comment block
// (the opcode roster: 0 store/create, 1 retrieve, 2 delete
// explicit, 3 next-on-last-argument, 4 kill all, 5 kill all
// except, 6 data, 7 New except, 8 New inclusive, 9 no copy flag,
// 10 return variable name, 11 retrieve-no-labels, 33 order, 99
// push table, 100 store this level only, 101 retrieve next, 150
// mark copy flag, 200 call-by-reference store) is preserved in
// the journal fragment. Derived from the pristine vendor source
// this session (sha256 621deb...ffdbe).
//
// SEATS: the Stab class is the landed unit-171 SymFree.kt
// carrier; Msv.start/_Sym/_SymPush/nstart/reference are the
// Carriers seats; SYM_MAX the defines.h:218 const. The
// __label_lookup crumb rides the landed unit-_ _label_lookup
// (MpstimeMisc.kt); fullUnpad is the landed SysfuncMisc.kt
// seat; the out-of-memory probes fold to the GC (H-008) with
// thesigint(100) tails kept; the (char*)p1 pointer returns of
// the store arms are NOT reproducible (H-012: the pointer
// identity is dead in every callsite; the non-NULL shape is
// preserved by returning the b buffer); the symflg param
// writes are C DEAD STORES passed by value (H-013, one ledger
// note per arm, kept as a mutable local only where the vendor
// flow reads it back). The defines.h values are file-private:
// SymCPPdata 102 and SymMarkCopyFlag 102 are THE SAME VALUE in
// the vendor (defines.h:52-53, the shared-opcode quirk
// ledgered). Consult H-012 hit-reuse, timestamp
// 2026-11-22T06:29:00Z.
//
// SEAT FLIP this unit owns: the Stubs.sym_ complex roster row
// 280 seat (raised by the batch-2 raise set) flips to this
// render.

// defines.h:51-53 + the sym_ banner defines (values verified
// against the vendor header this session).
private const val SYM_STORE_M300 = 0
private const val SYM_RETRIEVE_M300 = 1
private const val SYM_RET_NO_LBLS_M300 = 11
private const val SYM_CPP_DATA_M300 = 102
private const val SYM_MARK_COPY_FLAG_M300 = 102

// strlen over the NUL-terminated CharArray idiom.
private fun symLenM300(s: CharArray): Int {
    var i = 0
    while (s[i] != NUL) i++
    return i
}

// strcmp over NUL-terminated CharArrays.
private fun symCmpM300(a: CharArray, b: CharArray): Int {
    var i = 0
    while (true) {
        val ca = a[i]
        val cb = b[i]
        if (ca == NUL && cb == NUL) return 0
        if (ca != cb) return if (ca < cb) -1 else 1
        i++
    }
}

// the nmes chain nodes are opaque on the Msv.nstart seat (the
// Nmes/NmesI twin builders); both carry the same name/next
// fields, read per-node here (the reflection bridge, H-007).
private fun nmesNameM300(node: Any?): String? {
    if (node == null) return null
    val f = node.javaClass.getDeclaredField("name")
    f.isAccessible = true
    return f.get(node) as? String
}

private fun nmesNextM300(node: Any?): Any? {
    if (node == null) return null
    val f = node.javaClass.getDeclaredField("next")
    f.isAccessible = true
    return f.get(node)
}

// char * sym_(int symflg, unsigned char *a, unsigned char *b,
//             struct MSV * svPtr)
fun sym_(symflgIn: Int, a: CharArray, b: CharArray, svPtr: Msv): CharArray? {
    var symflg = symflgIn

    // unsigned char key[1024]; the high-bit fold: *ky = *kp &
    // 0x80 ? 1 : *kp (the strcpy/keyfix pair above it is
    // commented out in the vendor).
    val key = CharArray(1024)
    var kp = 0
    var ky = 0
    while (a[kp] != NUL) {
        key[ky] = if (a[kp].code and 0x80 != 0) 1.toChar() else a[kp]
        kp++
        ky++
    }
    key[ky] = NUL

    if (symflg == SYM_RETRIEVE_M300 || symflg == SYM_RET_NO_LBLS_M300) {

        // R E T R I E V E - walk from the current level down.
        for (isym in svPtr._Sym downTo 0) {
            var p1 = svPtr.start[isym]
            while (p1 != null) {
                if (symCmpM300(p1.name, key) == 0) {
                    CstringOps.strcpyS(b, p1.data!!.str)
                    return b
                }
                p1 = p1.next
            }
        }

        symflg = 0 // H-013 dead store (the param is by-value)
        b[0] = NUL
        return null
    }

    // ------------------------------------------------------------------
    // S T O R E (0 store/create; 100 this level only; 200 call by
    // reference; 102 the SymCPPdata/SymMarkCopyFlag shared opcode)
    // ------------------------------------------------------------------

    if (symflg == SYM_STORE_M300 || symflg == 100 || symflg == 200 ||
        symflg == SYM_CPP_DATA_M300 || symflg == SYM_MARK_COPY_FLAG_M300
    ) {

        // the special \u0024noerr case: strcasecmp over the key.
        if (key[0] == '\u0024' &&
            CstringOps.cstring(key).equals("noerr", ignoreCase = true)
        ) {
            if (b[0] == '1') svPtr.NOERR = 1 else svPtr.NOERR = 0
            return b
        }

        var isym = svPtr._Sym
        var p2: Stab? = null
        var p3: Stab? = null

        // search this and lower levels
        search@ while (isym >= 0) {
            p2 = null
            p3 = svPtr.start[isym]

            while (p3 != null) {
                val icmp = symCmpM300(p3.name, key)
                if (icmp == 0) {
                    // variable exists, value updated
                    p3.data!!.str = CstringOps.cstring(b)
                    return b
                }
                if (icmp > 0) break // past place where it should be
                p2 = p3
                p3 = p3.next
            }

            if (symflg == 100 || symflg == 200) break@search
            isym--
        }

        if (symflg == SYM_STORE_M300) {
            // searched to the bottom, new level-0 node
            isym = 0

            val p1 = Stab() // new struct stab (the probes fold to
            // the GC, H-008; the printf/sigint tails drop with
            // them - the out-of-memory path is dead)

            p1.OrigName = Mstring("")

            p1.exec = 0

            p1.name = CharArray(symLenM300(key) + 1)
            CstringOps.copyAtS(p1.name, 0, CstringOps.cstring(key))

            if (p2 == null) {
                // list is empty (p3==null) or has no element
                p1.next = p3
                svPtr.start[isym] = p1
            } else {
                // if last, p3 is null, next otherwise: add to list
                p1.next = p3
                p2.next = p1
            }

            p1.data = Mstring(CstringOps.cstring(b))
            p1.copy = 0
            return b // H-012: the vendor returns the (char*)p1
            // pointer; the non-NULL shape preserved via the b
            // buffer, pointer identity not reproducible
        }

        // the non-SymStore arms insert at the current level only
        if (symflg == 100 || symflg == 200) isym = svPtr._Sym else isym = 0

        var p2x: Stab? = null
        var p3x = svPtr.start[isym]

        while (p3x != null) {
            if (symCmpM300(p3x.name, key) > 0) break
            p2x = p3x
            p3x = p3x.next
        }

        val p1 = Stab()

        p1.OrigName = Mstring("")

        // this-level-only store (100/200) flags the level
        p1.exec = if (symflg == 100 || symflg == 200) 1 else 0

        if (symflg == 200) {
            // call by reference var: original name in reference
            p1.OrigName!!.str = CstringOps.cstring(svPtr.reference)
        }

        p1.name = CharArray(symLenM300(key) + 1)
        CstringOps.copyAtS(p1.name, 0, CstringOps.cstring(key))

        if (p2x == null) {
            p1.next = p3x
            svPtr.start[isym] = p1
        } else {
            p1.next = p3x
            p2x.next = p1
        }

        p1.data = Mstring(CstringOps.cstring(b))
        p1.copy = 0
        return b // H-012 pointer fold, the SymStore twin
    }

    // ------------------------------------------------------------------
    // 9 (zero copy flag) / 101 (retrieve next absolute) / the
    // dead 11 branch inside the not-found tail (H-013: the arm
    // is entered only with 9/101, the symflg==11 test never
    // fires; the p1==NULL test after the walk exhaustion is
    // always true - the C scoping quirk kept)
    // ------------------------------------------------------------------

    if (symflg == 9 || symflg == 101) {
        var p1: Stab? = null
        var cr = 0
        var found = false

        for (isym in svPtr._Sym downTo 0) {
            p1 = svPtr.start[isym]
            while (p1 != null) {
                cr = symCmpM300(p1.name, key)
                if (cr == 0) {
                    found = true
                    break
                }
                if (symflg == 101 && cr > 0) {
                    found = true
                    break
                }
                p1 = p1.next
            }
            if (found) break
            p1 = null
        }

        if (!found) {
            // the dead 11 branch (H-013)
            if (symflg == 9) {
                symflg = 0 // H-013 dead store
                return null
            }

            // symflg == 101: label lookup fallback
            val i = _label_lookup(CstringOps.cstring(key))

            if (i != -1) {
                CstringOps.strcpyS(b, String.format(java.util.Locale.US, "%d", i))
                symflg = 2 // H-013 dead store
                return b
            }

            symflg = 0 // H-013 dead store
            b[0] = NUL
            return null
        }

        if (symflg == 9) {
            // no copy flag
            symflg = 1 // H-013 dead store
            p1!!.copy = 0
            return null
        }

        // symflg == 101: next absolute
        if (cr == 0) {
            if (p1!!.next == null) {
                CstringOps.strcpyS(b, "")
                return null
            }
            p1 = p1.next // exact match - get next
        }

        var i = 0
        while (key[i] != 1.toChar() && key[i] != NUL) i++

        if (CstringOps.strncmpC(key, p1!!.name, i) == 0 && p1.name[i] == 1.toChar()) {
            val tmp = CharArray(STR_MAX)
            CstringOps.strcpyC(tmp, p1.name)
            fullUnpad(tmp, svPtr)
            CstringOps.strcpyC(b, tmp)
            return b
        }
        CstringOps.strcpyS(b, "")
        return null
        // the vendor not-found fall-through strcpy(b, data) is
        // unreachable from this arm pair (H-013, both real
        // branches return above; ledger, not rendered)
    }

    // ------------------------------------------------------------------
    // \u0024order (33) / \u0024next (3): the key fixups then the shared web
    // ------------------------------------------------------------------

    if (symflg == 33) {
        var i = symLenM300(key)
        if (svPtr.order < 0) {
            if (key[i - 1] == 1.toChar() && key[i - 2] == 1.toChar()) {
                key[i - 1] = '\u0080'
                key[i] = 1.toChar()
                i++
                key[i] = NUL
            }
        }
    }

    if (symflg == 3) {
        val i = symLenM300(key)
        if (key[i - 3] == '-' && key[i - 2] == '1' && key[i - 1] == 1.toChar()) {
            key[i - 3] = 1.toChar()
            key[i - 2] = NUL
            // i = i - 2 is a C dead write here: the shared web
            // recomputes the length (H-013)
        }
    }

    if (symflg == 3 || symflg == 33) {
        var isym = svPtr._Sym
        var i = symLenM300(key)

        // the end body shared by every web exit (the vendor
        // goto end / goto orderbackexit target pattern, folded
        // into a local function that returns the result).
        fun endBody(p1x: Stab): CharArray? {
            if (symflg == 3) i = symLenM300(key) - 2 // back off to
            else if (symflg == 33) i = symLenM300(key) - 2 // prior code lev

            while (key[i] != 1.toChar()) i-- // the C unbounded
            // backscan kept (the key root always carries a 1
            // separator byte; H-012 ledger)

            if (CstringOps.strncmpC(key, p1x.name, i) != 0) {
                CstringOps.strcpyS(b, if (symflg == 3) "-1" else "")
                return b
            }

            // orderbackexit
            if (p1x.name[i] == NUL) {
                // scalar var with same name
                b[0] = NUL
                return b
            }

            val nm = CstringOps.cstring(p1x.name)
            CstringOps.strcpyS(b, nm.substring(i + 1))

            var i2 = 0
            while (b[i2] != 1.toChar() && b[i2] != NUL) i2++
            b[i2] = NUL

            // the leading-space strip (the STRCPY comment idiom)
            while (b[0] == ' ') {
                var x = 0
                while (b[x] != NUL) {
                    b[x] = b[x + 1]
                    x++
                }
            }

            symflg = 1 // H-013 dead store

            if (b[0] == '\u001f') {
                // coded number - kok: the sign byte decode then
                // the atod wall of 40 then the sprintf %lg re-emit
                val sb = StringBuilder()
                sb.append(if (b[1] == '0') '-' else '+')
                var xi = 2
                while (xi < 41 && b[xi] != NUL) {
                    sb.append(b[xi])
                    xi++
                }
                val x = sb.toString().toDouble()
                CstringOps.strcpyS(b, String.format(java.util.Locale.US, "%g", x))
            }

            return b
        }

        o_again@ while (true) {
            var p1: Stab? = svPtr.start[isym]
            var p2: Stab? = null

            // looking for entry GT or EQ key
            while (p1 != null) {
                if (CstringOps.strncmpC(p1.name, key, i) >= 0) break
                p2 = p1
                p1 = p1.next
            }

            if (symflg == 33 && svPtr.order < 0) { // reverse order
                if (isym > 0 && p2 == null) { // not finished
                    isym--
                    continue@o_again
                }

                if (p2 == null) {
                    CstringOps.strcpyS(b, "")
                    return b
                }

                // the vendor i backscan here is dead: endBody
                // recomputes the length (H-013)
                return endBody(p2) // goto end
                // the trailing goto orderbackexit is unreachable
                // C text (H-013, not rendered)
            }

            if (p1 == null) { // none found - key GT all stored names
                if (isym > 0) { // more levels to check
                    isym--
                    continue@o_again
                }
                CstringOps.strcpyS(b, if (symflg == 3) "-1" else "")
                return b
            }

            if (CstringOps.strncmpC(p1.name, key, i) > 0) {
                return endBody(p1) // stored name GT key
            }

            // found EQ key fragment: advance to 1st NE to key
            p1 = p1.next
            while (p1 != null) {
                if (CstringOps.strncmpC(p1.name, key, i) != 0) break
                p1 = p1.next
            }

            if (p1 == null) { // none greater found
                CstringOps.strcpyS(b, if (symflg == 3) "-1" else "")
                return b
            }

            return endBody(p1)
        }
    }

    // ------------------------------------------------------------------
    // KILL selected (2) - the vendor head-rebind quirk: a
    // matched head unbinds from start[svPtr->_Sym], NOT
    // start[isym] (the level pointer differs once isym walks
    // below the current level; kept byte-verbatim)
    // ------------------------------------------------------------------

    if (symflg == 2) {
        for (isym in svPtr._Sym downTo 0) {
            repeat@ while (true) {
                var p1 = svPtr.start[isym]
                var p2: Stab? = null
                val i = symLenM300(key)

                while (p1 != null) {
                    if (CstringOps.strncmpC(p1.name, key, i) == 0 &&
                        p1.name[i] == 1.toChar()
                    ) break
                    if (symCmpM300(p1.name, key) == 0) break
                    p2 = p1
                    p1 = p1.next
                }

                if (p1 == null) break@repeat

                if (p2 == null) {
                    svPtr.start[svPtr._Sym] = p1.next // the quirk
                } else {
                    p2.next = p1.next
                }

                // the symFree deletion idiom (the landside
                // precedent): null the fields, CharArray(0) for
                // the freed name
                p1.data = null
                p1.name = CharArray(0)
                p1.OrigName = null
                p1.next = null
            }
        }
        return null
    }

    if (symflg == 4) { // kill all
        var p1 = svPtr.start[svPtr._Sym]
        while (p1 != null) {
            val p2 = p1.next
            p1.data = null
            p1.name = CharArray(0)
            p1.OrigName = null
            p1.next = null
            p1 = p2
        }
        svPtr.start[svPtr._Sym] = null
        return null // H-013 dead store
    }

    if (symflg == 5) { // kill all except...
        var p1 = svPtr.start[svPtr._Sym]
        var p3: Stab? = null
        while (p1 != null) {
            val p1c: Stab = p1
            var np1 = svPtr.nstart
            var flg = false
            while (np1 != null) {
                val nm = nmesNameM300(np1)
                var match = false

                if (nm != null) {
                    val i = nm.length - 1
                    if (i >= 0) {
                        val nmCz = czSymM300(nm)
                        if (CstringOps.strncmpC(nmCz, p1c.name, i) == 0 &&
                            p1c.name[i] == 1.toChar()
                        ) match = true
                    }
                    if (!match && nm == CstringOps.cstring(p1c.name)) match = true
                }

                if (match) {
                    // don't delete p1
                    p3 = p1c
                    p1 = p1c.next
                    flg = true
                    break
                }
                np1 = nmesNextM300(np1)
            }

            if (flg) continue

            // delete p1 (the external while guard keeps it
            // non-null; the local pins the Kotlin null-check)
            val p1d = p1!!
            p1d.data = null
            p1d.name = CharArray(0)
            p1d.OrigName = null
            val p2 = p1d.next
            p1d.next = null
            if (p3 == null) svPtr.start[svPtr._Sym] = p2 else p3.next = p2
            p1 = p2
        }

        // cleanup: the nmes chain free is the GC fold; the
        // vendor leaves nstart dangling, the landside convention
        // (the Interpret unit comment) nulls it here
        svPtr.nstart = null
        return null
    }

    // ------------------------------------------------------------------
    // \u0024data (6)
    // ------------------------------------------------------------------

    if (symflg == 6) {
        var p1: Stab? = null
        var isym = svPtr._Sym
        while (isym >= 0) {
            p1 = svPtr.start[isym]
            while (p1 != null) {
                if (symCmpM300(p1.name, key) == 0) break
                p1 = p1.next
            }
            if (p1 != null) break
            isym--
        }

        if (p1 == null) { // search fail
            var i = symLenM300(key)

            p1 = svPtr.start[svPtr._Sym]
            while (p1 != null) {
                if (CstringOps.strncmpC(p1.name, key, i) == 0) break
                p1 = p1.next
            }

            if (p1 != null) {
                if (key[i - 1] == 1.toChar()) i--

                if (p1.name[i] != 1.toChar()) {
                    b[0] = '0'
                    b[1] = NUL
                    b[2] = NUL
                    return b
                } else {
                    CstringOps.strcpyS(b, "10")
                    return b
                }
            }

            b[0] = '0'
            b[1] = NUL
            return b
        }

        b[0] = '1' // exists

        p1 = p1.next

        if (p1 == null) { // no possible descendants
            b[1] = NUL
            return b
        }

        val i = symLenM300(key)

        if (CstringOps.strncmpC(key, p1.name, i) == 0) {
            b[1] = '1'
            b[2] = NUL
            return b
        }

        b[1] = NUL
        return b
    }

    // ------------------------------------------------------------------
    // New except (7): build the unique name list, bump _SymPush
    // (the vendor does NOT push a table layer here - only 99
    // does; the asymmetry kept), then the sym_(100) recursion
    // over the list
    // ------------------------------------------------------------------

    if (symflg == 7) {
        var begin: Nmes? = null
        val beginNames = mutableSetOf<String>()

        for (isym in svPtr._Sym downTo 0) {
            var symtab = svPtr.start[isym]
            while (symtab != null) {
                val nmStr = CstringOps.cstring(symtab.name)
                var flg = true
                for (np2 in beginNames) {
                    if (np2 == nmStr) flg = false // in list already
                }
                var npw = svPtr.nstart
                while (npw != null) {
                    if (nmesNameM300(npw) == nmStr) flg = false // in except list
                    npw = nmesNextM300(npw)
                }
                if (flg) {
                    val np3 = Nmes()
                    np3.next = begin
                    np3.name = nmStr // H-012: the vendor aliases
                    // symtab->name; the String copy is the fold
                    begin = np3
                    beginNames.add(nmStr)
                }
                symtab = symtab.next
            }
        }

        svPtr._SymPush++
        // the vendor checks _Sym > SYM_MAX AFTER the _SymPush
        // bump without touching _Sym (kept; the check is the
        // same as the 99 arm's)
        if (svPtr._Sym > SYM_MAX) {
            System.out.print("*** Too many symbol table layers\n")
            Stubs.sigint(100)
        }

        var np2 = begin
        while (np2 != null) {
            sym_(100, czSymM300(np2.name!!), charArrayOf(NUL), svPtr)
            np2 = np2.next
        }
        // fall through (the vendor arm does not return here;
        // drops to the 99/8/10 checks below)
    }

    if (symflg == 99) { // push table
        svPtr._SymPush++
        svPtr._Sym++
        svPtr.start[svPtr._Sym] = null
        if (svPtr._Sym > SYM_MAX) {
            System.out.print("*** Too many symbol table layers\n")
            Stubs.sigint(100)
        }
        return null
    }

    if (symflg == 8) { // New inclusive
        var p1 = svPtr.start[svPtr._Sym]
        svPtr._Sym++
        svPtr.start[svPtr._Sym] = null

        while (p1 != null) {
            sym_(0, p1.name, p1.data!!.str.toCharArray(), svPtr)
            // the vendor p2->copy = 1 under symflg==1 is a dead
            // store (the param is 8 here; H-013, not rendered)
            p1 = p1.next
        }

        return null
    }

    if (symflg == 10) { // return variable name
        CstringOps.strcpyC(b, a) // copies a, NOT the key
        return b
    }

    return null
}

// the cz twin (the NUL-terminated buffer adapter) for the sym_
// recursion crumbs.
private fun czSymM300(s: String): CharArray {
    val b = CharArray(STR_MAX)
    CstringOps.copyAtS(b, 0, s)
    return b
}
