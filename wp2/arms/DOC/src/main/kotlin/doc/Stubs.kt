package doc

// Arm DOC stub file (cookbook H-014). Each stub marks an out-of-scope C
// function called by a translated unit; the owning unit is named from
// units.csv. Delegation replaces a stub when its owner unit lands.

object Stubs {
    // TODO stub: owned by the libmpscpp.cpp Mglobal family; not a selected
    // batch-2 unit (libmpscpp.cpp Mglobal helper is outside the corpus
    // units of this batch). Signature mirrors vendor
    // int Mglobal(int, unsigned char *, unsigned char *, struct MSV *).
    fun Mglobal(code: Int, key: CharArray, dest: CharArray, sv: Msv): Int =
        throw NotImplementedError("Mglobal: out-of-scope owner (libmpscpp.cpp Mglobal family)")

    fun Mglobal(code: Int, key: String, dest: CharArray, sv: Msv): Int =
        Mglobal(code, key.toCharArray().copyOf(STR_MAX).also { it[key.length] = '\u0000' }, dest, sv)

    // TODO stub: owned by include/mumpsc/libmpscpp.h inline ltoa-style macro
    // (libmpscpp.h:120); not a corpus unit.
    fun Mltoa(value: Int, out: CharArray): Unit =
        throw NotImplementedError("Mltoa: out-of-scope owner (libmpscpp.h:120 macro)")

    // TODO stub: owned by interp-sym.cpp _extract family (batch 10 corpus).
    fun Mextract(r: CharArray, tmp: CharArray, a: CharArray, b: CharArray): Unit =
        throw NotImplementedError("_extract: out-of-scope owner (interp-sym.cpp) batch-10 unit")

    // TODO stub: owned by interp-sym.cpp _find family (batch 10 corpus).
    fun Mfind(r: CharArray, tmp: CharArray, key: CharArray, a: CharArray): Int =
        throw NotImplementedError("_find: out-of-scope owner (interp-sym.cpp) batch-10 unit")

    // TODO stub: owned by interp-lock-adjacent keyfix.h inline; not a corpus unit.
    fun keyfix(gtmp: CharArray): Unit =
        throw NotImplementedError("keyfix: out-of-scope owner (keyfix.h inline)")

    // TODO stub: owned by inline.h:38 strmove inline; not a corpus unit.
    fun strmove(dst: CharArray, src: CharArray): Unit =
        throw NotImplementedError("strmove: out-of-scope owner (inline.h:38 inline)")

    // TODO stub: owned by the interp-piece.cpp _piece family
    // (vendor interp-piece.cpp:97); not a corpus unit.
    fun Mpiece(
        tmp: CharArray, gtmp: CharArray, gtmp1: CharArray,
        cc1: CharArray, cc2: CharArray, wild: Int, modes: CharArray, sv: Msv
    ): Unit = throw NotImplementedError("_piece: out-of-scope owner (interp-piece.cpp:97)")

    // TODO stub: owned by the interpreter core xecute helper
    // (libmpscpp.h:121 Xecute macro over __command__); not a corpus unit.
    fun xecute(cmd: CharArray): Int =
        throw NotImplementedError("xecute: out-of-scope owner (libmpscpp.h:121 macro)")

    // TODO stub: owned by the symbol-table SymGet path
    // (libmpscpp.h:95 macro _SymGet); not a corpus unit.
    fun SymGet(name: CharArray): Mstring =
        throw NotImplementedError("SymGet: out-of-scope owner (libmpscpp.h:95 macro)")

    // TODO stub: int Interpret(const char *, struct MSV *) declared
    // libmpscpp.h:210-facing interp core; not a corpus unit.
    fun Interpret(cmd: String, sv: Msv): Int =
        throw NotImplementedError("Interpret: out-of-scope owner (interpreter core Interpret)")

    // TODO stub: void fcn(MSV *) declared include/mumpsc/libmpscpp.h (fcn
    // dispatcher); owner is the interp fcn corpus unit of batch 4.
    fun fcn(sv: Msv): Unit = throw NotImplementedError("fcn: out-of-scope owner (fcn dispatcher) batch-4 unit")

    // TODO stub: gcvt_wrapper owned by include/mumpsc/numSetup.h (gcvt
    // wrapper macro); the numSetup residue unit owns it.
    fun gcvtWrapper(a: Double, out: CharArray): Unit =
        throw NotImplementedError("gcvt_wrapper: out-of-scope owner (numSetup.h residue unit)")

    // TODO stub: zfcn (unsigned char *, unsigned char *, MSV *) declared
    // libmpscpp.h (zfcn interpreter dispatcher); owner is the interp zfcn
    // corpus unit of a later batch; not a batch-2 unit.
    fun zfcn(tmp: CharArray, tmp2: CharArray, sv: Msv): Unit =
        throw NotImplementedError("zfcn: out-of-scope owner (interp zfcn dispatcher)")

    // TODO stub: void sigint(int) declared include/mumpsc/SYM.h:41;
    // owner is the interp SIGINT shutdown path; not a corpus unit.
    fun sigint(i: Int): Unit =
        throw NotImplementedError("sigint: out-of-scope owner (SYM.h:41 SIGINT path)")

    // global::Multiply three-reference member - DELEGATE (unit 53 of
    // this batch has landed): the u46 wrapper call sites bind unchanged
    // to this seat; the H-014 stub is superseded (delegate precedent).
    fun globalRefMultiply(a: GlobalRef, b: GlobalRef, c: GlobalRef): Unit =
        a.globalMultiply(b, c)

    // TODO stub: global::get_global two-buffer member (libmpscpp.cpp);
    // owner unit not selected in this batch; translation pending.
    fun get_global(key: CharArray, rslt: CharArray): Unit {
        Globals.svPtr.tpx = 0
        throw NotImplementedError("get_global: out-of-scope owner (libmpscpp.cpp global member)")
    }

    // TODO stub: global::set_global two-buffer member (libmpscpp.cpp);
    // owner unit not selected in this batch; translation pending.
    fun set_global(key: CharArray, rslt: CharArray): Unit =
        throw NotImplementedError("set_global: out-of-scope owner (libmpscpp.cpp global member)")

    // TODO stub: interp-native-btree.cpp block reader freadx; owner is
    // the btree block-IO unit of a later batch; not a batch-2 unit.
    fun freadx(block: Int, mode: Int): ByteArray =
        throw NotImplementedError("freadx: out-of-scope owner (interp-native-btree.cpp block reader)")

    // ------------------------------------------------------------------
    // batch-2 tail stubs (units 52/53/54)

    // TODO stub: memcheck owned by compiler-parse.c:2068; below roster
    // min loc - not a corpus unit.
    fun memcheck(iiitmp: Int): Unit =
        throw NotImplementedError("memcheck: out-of-scope owner (compiler-parse.c:2068)")

    // TODO stub: Extract2 owned by compiler-parse.c::Extract2
    // (batch-1 corpus unit, row 15 of units.csv). Signature carries the
    // offset for the vendor pd1[sdlim] idiom; the unit-52 call passes 0.
    fun Extract2(buf: CharArray, offset: Int): CharArray =
        throw NotImplementedError("Extract2: out-of-scope owner (compiler-parse.c::Extract2) batch-1 unit")

    // ------------------------------------------------------------------
    // unit 53 (libmpscpp.cpp::global::Multiply) member seats: the C++
    // class-global member functions are not roster units; stubs name
    // their vendor owners.

    // global::Kill member (libmpscpp.cpp); not a roster unit.
    fun globalKill(g: GlobalRef): Unit =
        throw NotImplementedError("global::Kill: out-of-scope owner (libmpscpp.cpp member)")

    // (*this)(i).Order(1) - one-subscript Order walk (libmpscpp.cpp
    // global::Order member); not a roster unit.
    fun globalOrder(g: GlobalRef, key: String): String =
        throw NotImplementedError("global::Order(this,key): out-of-scope owner (libmpscpp.cpp member)")

    // (*this)(i,j).Order(1) - two-subscript Order walk; not a roster unit.
    fun globalOrder2(g: GlobalRef, key1: String, key2: String): String =
        throw NotImplementedError("global::Order(this,k1,k2): out-of-scope owner (libmpscpp.cpp member)")

    // B(k,m).Order(1) - level Order walk on another global; not a roster unit.
    fun globalOrderB(g: GlobalRef, base: String, key: String): String =
        throw NotImplementedError("global::Order(g,base,key): out-of-scope owner (libmpscpp.cpp member)")

    // B(k).Data() - one-subscript Data probe; not a roster unit.
    fun globalData(g: GlobalRef, key: String): Boolean =
        throw NotImplementedError("global::Data(g,key): out-of-scope owner (libmpscpp.cpp member)")

    // C(i,m).Data() - two-subscript Data probe; not a roster unit.
    fun globalData2(g: GlobalRef, key1: String, key2: String): Boolean =
        throw NotImplementedError("global::Data(g,k1,k2): out-of-scope owner (libmpscpp.cpp member)")

    // (double) operator on an invoked global; not a roster unit.
    fun getGlobalDouble(g: GlobalRef, key1: String, key2: String): Double =
        throw NotImplementedError("global double conversion: out-of-scope owner (libmpscpp.cpp member)")

    // global assignment from a double (operator=); not a roster unit.
    fun setGlobalDouble(g: GlobalRef, v: Double, key1: String, key2: String): Unit =
        throw NotImplementedError("global double assignment: out-of-scope owner (libmpscpp.cpp member)")

    // ------------------------------------------------------------------
    // unit 54 (compiler-if.c::if_code) stubs

    // TODO stub: atTest owned by compiler-at-test.c::atTest (units.csv
    // row 109, batch-4 core unit). pd1Last is the H-006 holder.
    fun atTest(xd: CharArray, pd1: CharArray, pd1Last: ShortArray,
               lines: Int, xpx: Long): Int =
        throw NotImplementedError("atTest: out-of-scope owner (compiler-at-test.c::atTest) batch-4 unit")

    // TODO stub: atCopy (compiler-globals.h:104 declaration; no
    // definition is a roster unit).
    fun atCopy(at_flg: Int, tmp: CharArray): Unit =
        throw NotImplementedError("atCopy: out-of-scope owner (compiler-at-test.c helper)")

    // TODO stub: parse_ owned by interp-parse.cpp::parse_ (batch-3
    // corpus unit).
    fun parse_(): Int =
        throw NotImplementedError("parse_: out-of-scope owner (interp-parse.cpp::parse_) batch-3 unit")

    // TODO stub: linepad owned by compiler-sys1.c::linepad (loc 5, below
    // roster min loc - not a corpus unit).
    fun linepad(n: Int, s: String): Unit =
        throw NotImplementedError("linepad: out-of-scope owner (compiler-sys1.c::linepad)")
}
