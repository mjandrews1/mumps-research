package daf

/*
 * DAF batch-10 unit 279 (roster row 279; interp-sym.cpp::sym_,
 * LOC 484, ptr 29, score high, THE dispatcher). Consult
 * 2026-11-25T15:00:30Z.
 *
 * RELEASES two seats (named-stub discipline):
 * - symRetDaf (DafInterpStubsT3.kt raise deleted): the real
 *   6-arg gate seat, CharArray (name, int) offset pair flattened
 *   per the H-014 seat map; 30+ pre-existing call sites in
 *   Interpret/Fcn/MpstimeMain/InterpPieceMisc resolve unchanged.
 * - symDaf (InterpBifsMisc.kt raise deleted): the 3-arg String
 *   adapter seat (Mget/SymLoad callers); svPtr folds to
 *   DafInterpState.svPtr; the C NULL folds to the out content of
 *   the miss paths (empty string), value otherwise.
 *
 * STUB CORRECTION (row-279 adjudication): the b5 InterpBifsMisc
 * tag "not a roster unit for the dispatcher" was WRONG/stale -
 * interp-sym.cpp::sym_ is roster row 279 (484 loc, batch 10);
 * the correction is logged in the heuristic row and the fragment.
 *
 * INFRASTRUCTURE: svPtr->start[isym] chains ride the DafSymChains
 * side carrier at the same layer index (DafMsv.start is the Int
 * domain layer stack; unchanged, ledgered unit-138 fold).
 * struct nmes (the svPtr->nstart NEW/KILL except chain) rides the
 * new shared DafNmesState carrier; the Interpret NEW/KILL seat
 * previously folded it to its private InterpretState.nmesChain -
 * that fold is RENAMED to the shared carrier this unit (three
 * edited lines in Interpret.kt, release-noted there); the
 * DafMsv.nstart Int field keeps its opaque state-copy role.
 *
 * DISCLOSED DIVERGENCES (all flagged in the fragment):
 * 1. malloc/new NULL branches print+sigint(100): dead on the JVM
 *    (allocation cannot fail); kept as comment folds per the
 *    H-013 precedent.
 * 2. Write-path returns: vendor returns (char*)p1 (a NODE pointer
 *    cast to char*) on the store-create paths - the String?
 *    domain folds non-NULL pointer to the empty string "" (the
 *    miss/NULL paths return null); store call sites ignore the
 *    value, grep-verified.
 * 3. __label_lookup (the 101 miss path): extern function-pointer
 *    seat; the interpreter runtime never binds it - the fold
 *    returns the compiled default -1 (CompilerEpi.kt:150
 *    precedent: no-labels programs generate return -1).
 * 4. SymMarkCopyFlag and SymCPPdata are both 102 (defines.h
 *    52-53): the vendor OR arms collapse to one class check -
 *    kept as the single 102 compare with both names in the
 *    comment.
 * 5. Vendor oddities kept verbatim: the 9/101 block checks
 *    symflg==11 (unreachable there, symflg was 9 or 101); the
 *    order-33 reverse arm makes the trailing
 *    goto orderbackexit dead; the kill-selected unlink writes
 *    svPtr->start[svPtr->_Sym] (not [isym]); the New-inclusive
 *    if (symflg == 1) p2->copy = 1 is dead (symflg is 8); the
 *    101 p1 == NULL and p1->next == NULL null deref folds to the
 *    null-safe (p1 == null || p1.next == null) short-circuit.
 * 6. sscanf %lf on the coded-number exit decodes via a guarded
 *    Double fold (garbage -> 0.0, sscanf convention);
 *    sprintf %lg via DafCRuntime.fmtG (the JVM percent-g seat).
 * 7. a == NULL (the vendor null name guard): the CharArray domain
 *    has no null; callers never pass one (grep-verified) - the
 *    guard folds to the empty-name early return.
 *
 * H-015 hygiene: no raw dollars in comments (\u0024 escapes); no
 * backticks; ASCII only; trailing newline.
 * Dafny: EXEMPT (stateful dispatcher over mutable chains;
 * journal row).
 */

// struct nmes (compiler-globals-ext.h:56): the NEW/KILL except
// list node. Shared carrier: the Interpret NEW/KILL walk builds
// the chain; the sym_(5)/sym_(7) arms below consume it (the
// pre-existing private InterpretState fold, renamed here).
class DafNmesD(val name: String, val next: DafNmesD?)

object DafNmesState {
    var chain: DafNmesD? = null // struct nmes *nstart
}

// extern int (*__label_lookup)(char *) - the compiled-runtime
// function pointer; unbound on the interpreter arm. The default
// fold returns -1 per the CompilerEpi.kt:150 generated body.
private fun labelLookupS(key: String): Int {
    return -1 // int X_label_lookup(char *) { return -1; }
}

// strncmp(name, key, n) fold over the masked-name domain
private fun cmpPrefixS(name: String, key: String, n: Int): Int {
    var i = 0
    while (i < n) {
        val ca = if (i < name.length) name[i] else NUL_D
        val cb = if (i < key.length) key[i] else NUL_D
        if (ca != cb) return if (ca < cb) -1 else 1
        if (ca == NUL_D) return 0
        i++
    }
    return 0
}

// sscanf("%lf") fold: garbage -> 0.0 (the C convention)
private fun scanLfS(s: String): Double =
    try { s.trim().toDouble() } catch (e: NumberFormatException) { 0.0 }

// The dispatcher: char *sym_(int, unsigned char *, unsigned char *,
// struct MSV *) - interp-sym.cpp gate seat. symflg is the
// vendor by-value parameter; the local reassignments (vendor
// symflg mutation) ride the symflgV local.
fun symRetDaf(symflg: Int, a: CharArray, aOff: Int,
        out: CharArray, outOff: Int, sv: DafMsv): String? {
    var symflgV = symflg

    // if (a == NULL) return NULL; - CharArray domain fold (div. 7)
    if (a.size == 0) return null

    // char key[1024]; unsigned char *kp = a, *ky = key;
    //   while (*kp) { *ky = *kp & 0x80 ? 1 : *kp; kp++; ky++ }
    val keyB = StringBuilder()
    var kpi = aOff
    while (kpi < a.size && a[kpi] != NUL_D) {
        val c = a[kpi]
        keyB.append(if ((c.code and 0x80) != 0) '\u0001' else c)
        kpi++
    }
    val key = keyB.toString() // *ky = 0

    var p1: DafStabNode? = null
    var p2: DafStabNode? = null
    var p3: DafStabNode? = null

    // -------------------
    // R E T R I E V E
    // -------------------
    if (symflgV == SYMRETRIEVE_D || symflgV == 11) {
        var isym = sv._Sym
        while (isym >= 0) {
            p1 = DafSymChains.start[isym]
            while (p1 != null) {
                if (p1.name.compareTo(key) == 0) {
                    storeOutS(out, outOff, p1.data) // strcpy(b, data)
                    return DafCRuntime.cstringAt(out, outOff)
                }
                p1 = p1.next
            }
            isym--
        }
        symflgV = 0
        out[outOff] = NUL_D // b[0] = 0
        return null
    }

    // -------------------
    // S T O R E (0/100/200/102; 100 = this level only)
    // -------------------
    if (symflgV == SYMSTORE_D || symflgV == 100 || symflgV == 200 ||
            symflgV == 102 /* SymCPPdata/SymMarkCopyFlag, div. 4 */) {

        var isym = sv._Sym

        // if (key is dollar-noerr, case-insensitive) { ... }
        if (key.startsWith("\u0024") && key.equals("\u0024noerr", ignoreCase = true)) {
            sv.NOERR = if (out[outOff] == '1') 1 else 0
            return DafCRuntime.cstringAt(out, outOff)
        }

        // search this and lower levels for the existing entry
        while (isym >= 0) {
            p2 = null
            p3 = DafSymChains.start[isym]
            while (p3 != null) { // all level scan requires exact match
                val icmp = p3.name.compareTo(key)
                if (icmp == 0) {
                    p3.data = DafCRuntime.cstringAt(out, outOff)
                    return DafCRuntime.cstringAt(out, outOff)
                    // variable exists; value updated
                }
                if (icmp > 0) break // past place where it should be
                p2 = p3
                p3 = p3.next
            }
            if (symflgV == 100 || symflgV == 200) break
            isym--
        }

        if (symflgV == SYMSTORE_D) {
            // ****************************************************
            isym = 0 // searched to the bottom

            p1 = DafStabNode()
            // (the vendor new/malloc NULL branches print and
            // sigint(100); dead on the JVM - div. 1 comment fold)

            // p1->OrigName = new string; p1->exec = 0;
            p1.origName = ""
            p1.exec = 0

            p1.name = key // strcpy(p1->name, key)

            if (p2 == null) { // no previous. list is empty or headless
                p1.next = p3
                DafSymChains.start[isym] = p1
            } else { // if last, p3 is NULL, next otherwise
                p1.next = p3
                p2.next = p1
            }

            p1.data = DafCRuntime.cstringAt(out, outOff) // new string(b)
            p1.copy = 0
            return "" // (char *) p1 - div. 2 non-NULL fold
        }

        isym = if (symflgV == 100 || symflgV == 200) sv._Sym else 0

        p2 = null
        p3 = DafSymChains.start[isym]

        while (p3 != null) {
            if (p3.name.compareTo(key) > 0) break
            p2 = p3
            p3 = p3.next
        }

        p1 = DafStabNode()
        // (vendor new NULL print+sigint fold, div. 1)
        p1.origName = "" // p1->OrigName = new string

        // this-level-only store (100/200) flags the level
        if (symflgV == 100 || symflgV == 200) p1.exec = 1 else p1.exec = 0

        // call by reference var keeps the underlying name
        if (symflgV == 200) {
            p1.origName = DafCRuntime.cstringAt(sv.reference, 0)
        } else p1.origName = ""

        p1.name = key
        // (vendor malloc NULL print+sigint fold, div. 1)

        if (p2 == null) {
            p1.next = p3
            DafSymChains.start[isym] = p1
        } else {
            p1.next = p3
            p2.next = p1
        }

        p1.data = DafCRuntime.cstringAt(out, outOff)
        p1.copy = 0
        return "" // (char *) p1 - div. 2
    }

    // 9/101: retrieve or zero copy flag
    if (symflgV == 9 || symflgV == 101) {

        var foundF = false
        var crF = 0

        outer@ for (isym in sv._Sym downTo 0) {
            p1 = DafSymChains.start[isym]
            while (p1 != null) {
                crF = p1.name.compareTo(key)
                if (crF == 0) { foundF = true; break@outer }
                if (symflgV == 101 && crF > 0) { foundF = true; break@outer }
                p1 = p1.next
            }
        }

        if (!foundF) { // if (p1 == NULL)
            if (symflgV == 11) return null // vendor dead test (div. 5)
            if (symflgV == 9) {
                symflgV = 0
                return null
            }

            val iL = labelLookupS(key) // i = __label_lookup(key)

            if (iL != -1) {
                storeOutS(out, outOff, iL.toString()) // sprintf(b, "%d", i)
                symflgV = 2
                return DafCRuntime.cstringAt(out, outOff)
            }

            symflgV = 0
            out[outOff] = NUL_D
            return null
        }

        // found:
        if (symflgV == 9) { // no copy flag
            symflgV = 1
            p1!!.copy = 0
            return null
        }

        if (symflgV == 101) { // next absolute

            val pn = p1!!
            if (pn.next == null) { // div. 5 null-safe fold
                storeOutS(out, outOff, "")
                return null
            }

            if (crF == 0) {
                if (pn.next == null) {
                    storeOutS(out, outOff, "")
                    return null
                }
                p1 = pn.next // exact match - get next
            }

            var iF = 0
            while (iF < key.length && key[iF] != '\u0001' && key[iF] != NUL_D) iF++

            val nxt = p1!!
            if (cmpPrefixS(nxt.name, key, iF) == 0 &&
                    nxt.name.length > iF && nxt.name[iF] == '\u0001') {
                val tmp = CharArray(STR_MAX_DAF)
                storeOutS(tmp, 0, nxt.name)
                fullUnpadDaf(tmp, sv)
                val res = DafCRuntime.cstring(tmp)
                storeOutS(out, outOff, res)
                return res
            }
            storeOutS(out, outOff, "")
            return null
        }

        // other cases
        val res = p1!!.data
        storeOutS(out, outOff, res)
        return res
    }

    // ------
    // order (33): reverse tail build
    // ------
    if (symflgV == 33) {
        var iK = key.length
        if (sv.order < 0) {
            if (iK >= 2 && key[iK - 1] == '\u0001' && key[iK - 2] == '\u0001') {
                // key[i-1]=0x80; key[i]=1; key[++i]=0
                keyB.setCharAt(iK - 1, '\u0080')
                keyB.append('\u0001')
                iK++
            }
        }
        // (the vendor commented-out else block kept as this note)
        return orderNextS(symflgV, keyB.toString(), iK, out, outOff, sv)
    }

    // ------
    // next (3)
    // ------
    if (symflgV == 3) {
        var iK = key.length
        if (iK >= 3 && key[iK - 3] == '-' && key[iK - 2] == '1' &&
                key[iK - 1] == '\u0001') {
            keyB.setCharAt(iK - 3, '\u0001')
            keyB.setCharAt(iK - 2, NUL_D)
            iK = iK - 2
        }
        return orderNextS(symflgV, keyB.toString(), iK, out, outOff, sv)
    }

    // --------------
    // kill selected (2)
    // --------------
    if (symflgV == 2) {
        for (isym in sv._Sym downTo 0) {
            // repeat:
            while (true) {
                p1 = DafSymChains.start[isym]
                p2 = null
                val iK = key.length

                while (p1 != null) {
                    val nm = p1.name
                    if (cmpPrefixS(nm, key, iK) == 0 &&
                            nm.length > iK && nm[iK] == '\u0001') break
                    if (nm == key) break
                    p2 = p1
                    p1 = p1.next
                }

                if (p1 == null) break // if (p1 == NULL) continue

                // vendor keeps svPtr->start[svPtr->_Sym] here (div. 5)
                if (p2 == null) {
                    DafSymChains.start[sv._Sym] = p1.next
                } else {
                    p2.next = p1.next
                }
                p1 = null // the delete/free folds to the GC drop
                // goto repeat
            }
        }
        return null
    }

    if (symflgV == 4) { // kill all
        p1 = DafSymChains.start[sv._Sym]
        while (p1 != null) {
            p2 = p1.next
            // delete data/name/OrigName/node -> GC drop
            p1 = p2
        }
        DafSymChains.start[sv._Sym] = null
        symflgV = 1
        return null
    }

    if (symflgV == 5) { // kill all except...

        p1 = DafSymChains.start[sv._Sym]
        p3 = null
        while (p1 != null) {
            val cur = p1!! // loop-body alias (the rebind below)
            var np1 = DafNmesState.chain // np1 = svPtr->nstart
            var flg = 0
            while (np1 != null) {
                val iK = np1.name.length - 1

                if ((cmpPrefixS(np1.name, cur.name, iK) == 0 &&
                        cur.name.length > iK && cur.name[iK] == '\u0001') ||
                        np1.name == cur.name) {
                    p3 = cur
                    p1 = cur.next
                    flg = 1 // don't delete p1
                    break
                }
                np1 = np1.next
            }

            if (flg == 1) continue

            p2 = cur.next // the delete/free folds to the GC drop
            if (p3 == null) DafSymChains.start[sv._Sym] = p2
            else p3.next = p2
            p1 = p2
        }

        // np1 cleanup: the free loop folds to the carrier reset
        DafNmesState.chain = null

        symflgV = 1
        return null
    }

    // ------
    // data (6)
    // ------
    if (symflgV == 6) {
        p1 = null
        var isym = sv._Sym
        while (isym >= 0) {
            p1 = DafSymChains.start[isym]
            while (p1 != null) {
                if (p1.name == key) break
                p1 = p1.next
            }
            if (p1 != null) break
            isym--
        }

        if (p1 == null) { // search fail

            var iK = key.length

            p1 = DafSymChains.start[sv._Sym]
            while (p1 != null) {
                if (cmpPrefixS(p1.name, key, iK) == 0) break
                p1 = p1.next
            }

            if (p1 != null) {
                if (iK >= 1 && key[iK - 1] == '\u0001') iK--

                if (p1.name.length <= iK || p1.name[iK] != '\u0001') {
                    out[outOff] = '0'; out[outOff + 1] = NUL_D
                    out[outOff + 2] = NUL_D
                    return DafCRuntime.cstringAt(out, outOff)
                } else {
                    storeOutS(out, outOff, "10")
                    return DafCRuntime.cstringAt(out, outOff)
                }
            }

            out[outOff] = '0'; out[outOff + 1] = NUL_D
            return DafCRuntime.cstringAt(out, outOff)
        }

        storeOutS(out, outOff, "1") // exists

        p1 = p1.next

        if (p1 == null) { // no possible descendants
            out[outOff + 1] = NUL_D
            return DafCRuntime.cstringAt(out, outOff)
        }

        val iK = key.length

        if (cmpPrefixS(p1.name, key, iK) == 0) {
            out[outOff + 1] = '1'; out[outOff + 2] = NUL_D
        } else {
            out[outOff + 1] = NUL_D
        }
        return DafCRuntime.cstringAt(out, outOff)
    }

    if (symflgV == 7) { // New except (...)

        var beginN: DafNmesD? = null // struct nmes *begin
        for (isym in sv._Sym downTo 0) {
            // build unique list of variable names
            var symtab = DafSymChains.start[isym]
            while (symtab != null) {
                var flg = 1
                var np2 = beginN
                while (np2 != null) { // in list already?
                    if (np2.name == symtab.name) flg = 0
                    np2 = np2.next
                }
                np2 = DafNmesState.chain
                while (np2 != null) { // in except list?
                    if (np2.name == symtab.name) flg = 0
                    np2 = np2.next
                }
                if (flg == 1) {
                    // (vendor malloc NULL print+sigint fold, div. 1)
                    beginN = DafNmesD(symtab.name, beginN)
                }
                symtab = symtab.next
            }
        }
        sv._SymPush++
        if (sv._Sym > SYM_MAX_D) {
            println("*** Too many symbol table layers")
            sigintD(100)
        }
        var np2 = beginN
        while (np2 != null) {
            symRetDaf(100, np2.name.toCharArray(), 0,
                    charArrayOf(NUL_D), 0, sv)
            np2 = np2.next
        }
        // vendor falls through (no return); symflgV stays 7
    }

    if (symflgV == 99) { // push table
        sv._SymPush++
        sv._Sym++
        sv.start[sv._Sym] = 0 // the C NULL sentinel (Int domain)
        DafSymChains.start[sv._Sym] = null
        if (sv._Sym > SYM_MAX_D) {
            println("*** Too many symbol table layers")
            sigintD(100)
        }
        return null
    }

    if (symflgV == 8) { // New inclusive

        p1 = DafSymChains.start[sv._Sym]
        sv._Sym++
        DafSymChains.start[sv._Sym] = null

        while (p1 != null) {
            // p2 = (struct stab *) sym_(0, p1->name, data, svPtr):
            // the vendor recasts the SymStore return (the node
            // pointer, div. 2); the arm recurses into the real
            // dispatcher and keeps the node through the carrier
            val tmp = CharArray(STR_MAX_DAF)
            storeOutS(tmp, 0, p1.data)
            symRetDaf(0, p1.name.toCharArray(), 0, tmp, 0, sv)
            // the vendor if (symflg == 1) p2->copy = 1; is dead
            // (symflg is 8 here) - div. 5, kept as this note
            p1 = p1.next
        }

        symflgV = 1
        return null
    }

    if (symflgV == 10) { // return variable name
        storeOutS(out, outOff, DafCRuntime.cstringAt(a, aOff))
        symflgV = 1
        return DafCRuntime.cstringAt(out, outOff)
    }

    return null
}

// exit_processing shared tail of the order/next arms (vendor
// goto web folded on the end/orderbackexit labels)
private fun orderNextS(symflgV: Int, key: String, iIn: Int,
        out: CharArray, outOff: Int, sv: DafMsv): String? {

    var i = iIn
    var p1: DafStabNode? = null
    var p2: DafStabNode? = null
    var isym = sv._Sym

    o_again@ while (true) {

        p1 = DafSymChains.start[isym]
        p2 = null

        while (p1 != null) { // looking for entry GT or EQ key
            if (cmpPrefixS(p1.name, key, i) >= 0) break
            p2 = p1
            p1 = p1.next
        }

        if (symflgV == 33 && sv.order < 0) { // reverse order

            if (isym > 0 && p2 == null) { // not finished with table
                isym--
                continue@o_again // goto o_again
            }

            if (p2 == null) {
                storeOutS(out, outOff, "")
                return "" // symflg=1; return b
            }

            i = i - 2
            while (i >= 0 && i < key.length && key[i] != '\u0001') i--
            p1 = p2
            // goto end (the trailing goto orderbackexit is dead)
        } else {

            if (p1 == null) { // none found - key GT all stored names
                if (isym > 0) { // more sym table levels to check
                    isym--
                    continue@o_again
                }
                storeOutS(out, outOff, if (symflgV == 3) "-1" else "")
                return DafCRuntime.cstringAt(out, outOff)
            }

            if (cmpPrefixS(p1.name, key, i) > 0) {
                // stored name GT key: goto end
            } else {
                p1 = p1.next // found EQ key fragment
                while (p1 != null) { // advance to 1st NE to key
                    if (cmpPrefixS(p1.name, key, i) != 0) break
                    p1 = p1.next
                }
                if (p1 == null) { // none greater found
                    storeOutS(out, outOff, if (symflgV == 3) "-1" else "")
                    return DafCRuntime.cstringAt(out, outOff)
                }
            }
        }

        // end:
        i = key.length - 2 // back off to prior code lev (3 and 33)

        while (i >= 0 && key[i] != '\u0001') i--

        val pn = p1!!
        if (i < 0) {
            storeOutS(out, outOff, if (symflgV == 3) "-1" else "")
            return DafCRuntime.cstringAt(out, outOff)
        }
        if (cmpPrefixS(key, pn.name, i) != 0) {
            storeOutS(out, outOff, if (symflgV == 3) "-1" else "")
            return DafCRuntime.cstringAt(out, outOff)
        }

        // orderbackexit:
        if (pn.name.length <= i || pn.name[i] == NUL_D) {
            out[outOff] = NUL_D // scalar var with same name
            return DafCRuntime.cstringAt(out, outOff)
        }
        // strcpy(b, p1->name + i + 1)
        val tailB = StringBuilder()
        var ti = i + 1
        while (ti < pn.name.length && pn.name[ti] != NUL_D) {
            tailB.append(pn.name[ti]); ti++
        }
        var bStr = tailB.toString()

        // for (i = 0; b[i] != 1 && b[i] != 0; i++); b[i] = 0;
        var bi = 0
        while (bi < bStr.length && bStr[bi] != '\u0001' && bStr[bi] != NUL_D) bi++

        // while (b[0] == ' ') shift left (STRCPY)
        bStr = bStr.substring(0, bi)
        while (bStr.startsWith(" ")) {
            val sb = StringBuilder()
            var xi = 1
            while (xi < bStr.length && bStr[xi] != NUL_D) {
                sb.append(bStr[xi]); xi++
            }
            bStr = sb.toString()
            if (bStr.isEmpty()) break
        }

        // exit_processing: symflg = 1;
        if (bStr.isNotEmpty() && bStr[0] == '\u001F') { // coded number
            var r = bStr.substring(1)
            r = (if (r.isNotEmpty() && r[0] == '0') "-" else "+") +
                    (if (r.length > 1) r.substring(1) else "")
            // memcpy(tmp, r, 40); tmp[40] = 0; sscanf("%lf"); %lg
            val tmp = if (r.length > 40) r.substring(0, 40) else r
            val x = scanLfS(tmp)
            bStr = DafCRuntime.fmtG(x)
        }

        storeOutS(out, outOff, bStr)
        return bStr
    }
}

// strcpy(&out[outOff], s) private copy fold of this file
private fun storeOutS(out: CharArray, outOff: Int, s: String) {
    var i = 0
    while (i < s.length && outOff + i < out.size) {
        out[outOff + i] = s[i]
        i++
    }
    if (outOff + i < out.size) out[outOff + i] = NUL_D
}
