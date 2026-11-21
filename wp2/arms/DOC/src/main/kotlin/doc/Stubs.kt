package doc

// Arm DOC stub file (cookbook H-014). Each stub marks an out-of-scope C
// function called by a translated unit; the owning unit is named from
// units.csv. Delegation replaces a stub when its owner unit lands.

object Stubs {
    // Unit 167 (B6 tranche 2): resolved - delegates to the real
    // Mglobal render (libmpscpp.cpp Mglobal family; the Close/Close2
    // early-out, the SQLITE arm as the live reference config, and
    // the btree pass-through). Signature mirrors vendor
    // int Mglobal(int, unsigned char *, unsigned char *, struct MSV *).
    fun Mglobal(code: Int, key: CharArray, dest: CharArray, sv: Msv): Int =
        doc.Mglobal(code, key, dest, sv)

    fun Mglobal(code: Int, key: String, dest: CharArray, sv: Msv): Int =
        Mglobal(code, key.toCharArray().copyOf(STR_MAX).also { it[key.length] = '\u0000' }, dest, sv)

    // TODO stub: owned by include/mumpsc/libmpscpp.h inline ltoa-style macro
    // (libmpscpp.h:120); not a corpus unit.
    fun Mltoa(value: Int, out: CharArray): Unit =
        throw NotImplementedError("Mltoa: out-of-scope owner (libmpscpp.h:120 macro)")

    // Unit 168 (B6 tranche 2, resolved): Mextract delegates to the real
    // _extract render (interp-sym.cpp _extract family). Original stub
    // note, kept verbatim: TODO stub - owned by interp-sym.cpp _extract
    // family (batch-10 mis-tag; the batch-6 roster carries the unit as
    // roster row 168 - tag correction journaled with the render).
    fun Mextract(r: CharArray, tmp: CharArray, a: CharArray, b: CharArray): Unit =
        doc._extract(r, tmp, a, b)

    // TODO stub: owned by interp-sym.cpp _find family (batch 10 corpus).
    fun Mfind(r: CharArray, tmp: CharArray, key: CharArray, a: CharArray): Int =
        throw NotImplementedError("_find: out-of-scope owner (interp-sym.cpp) batch-10 unit")

    // TODO stub: owned by interp-lock-adjacent keyfix.h inline; not a corpus unit.
    fun keyfix(gtmp: CharArray): Unit =
        throw NotImplementedError("keyfix: out-of-scope owner (keyfix.h inline)")

    // TODO stub: owned by inline.h:38 strmove inline; not a corpus unit.
    fun strmove(dst: CharArray, src: CharArray): Unit =
        throw NotImplementedError("strmove: out-of-scope owner (inline.h:38 inline)")

    // RESOLVED + TAG CORRECTION: the prior seat tag called Mpiece
    // "not a corpus unit"; the _piece body OWNER is the landed
    // batch-4 unit 114 render (PieceMisc.kt, interp-piece.cpp
    // misc chunk carrying the _piece body; the strmove sibling
    // seats stay). This seat delegates to that real; the
    // unit-38 GlobalPiece call site binds through here unchanged.
    fun Mpiece(
        tmp: CharArray, gtmp: CharArray, gtmp1: CharArray,
        cc1: CharArray, cc2: CharArray, wild: Int, modes: CharArray, sv: Msv
    ): Unit = doc._piece(tmp, gtmp, gtmp1, cc1, cc2, wild, modes, sv)

    // TODO stub: owned by the interpreter core xecute helper
    // (libmpscpp.h:121 Xecute macro over __command__); not a corpus unit.
    fun xecute(cmd: CharArray): Int =
        throw NotImplementedError("xecute: out-of-scope owner (libmpscpp.h:121 macro)")

    // RESOLVED + TAG CORRECTION: the prior seat tag (the libmpscpp.h
    // 95 macro _SymGet; not a corpus unit) was a MIS-TAG - the
    // CharArray-name surface of SymGet resolves onto the
    // libmpscpp.cpp SymGet family (libmpscpp.h:179-181 declares the
    // mstring / char* / global overloads), which IS the batch-4
    // unit-120 render (SymOpsMisc.kt). The seat converts to the
    // unit-120 delegate; the char* form is the String seat there
    // (H-012), so the CharArray content is parsed to its C string
    // first. The GlobalOrder.kt and GlobalData.kt call sites bind
    // unchanged.
    fun SymGet(name: CharArray): Mstring =
        SymGet(CstringOps.cstring(name))

    // RESOLVED: int Interpret(const char *, struct MSV *) declared
    // libmpscpp.h:210-facing interp core; TAG CORRECTION from the
    // earlier "not a corpus unit" mis-tag: the owner IS a roster row
    // (row 185, interp-interp.cpp::Interpret, physLOC 1581, rank
    // 308), landed as the batch-6 big-three unit-185 render
    // (Interpret.kt). The seat converts to the unit-185 delegate per
    // the Mglobal precedent; the Fcn.kt, MumpsMainUnit150.kt,
    // RunMumps.kt and SQLTable.kt call sites bind unchanged.
    fun Interpret(cmd: String, sv: Msv): Int =
        doc.Interpret(cmd, sv)

    // RESOLVED: void fcn(MSV *) declared include/mumpsc/libmpscpp.h (fcn
    // dispatcher); owner landed as the batch-4 unit 123 render (Fcn.kt,
    // the T3 tail unit). The seat converts to the unit-123 delegate per
    // the H-014 Extract2 precedent; the Fnumber_/Fnumber/_fnumber and
    // Parse_ call sites bind unchanged.
    fun fcn(sv: Msv): Unit = doc.fcn(sv)

    // TODO stub: gcvt_wrapper owned by include/mumpsc/numSetup.h (gcvt
    // wrapper macro); the numSetup residue unit owns it.
    fun gcvtWrapper(a: Double, out: CharArray): Unit =
        throw NotImplementedError("gcvt_wrapper: out-of-scope owner (numSetup.h residue unit)")

    // void zfcn(unsigned char *, unsigned char *, MSV *) declared
    // libmpscpp.h (zfcn interpreter dispatcher); owner is unit 184
    // (batch-6 big three, interp-zfcns.cpp::zfcn, physLOC 1662).
    // TAG CORRECTION: the earlier note read "the interp zfcn
    // corpus unit of a later batch"; the owning roster unit is
    // 184, and the vendor Makefile reference build compiles
    // interp-zfcns.o (NOT interp-bifs.o) into libmumps.a, so this
    // unit owns the seat flip and the top-level fun zfcn name
    // (H-017; the bifs twin renders as zfcnBifs, unit 183, the
    // build-alternate TU). Delegate seat, the StopInit/Mglobal
    // landing precedent:
    fun zfcn(tmp: CharArray, tmp2: CharArray, sv: Msv): Unit =
        doc.zfcn(tmp, tmp2, sv)

    // TODO stub: void sigint(int) declared include/mumpsc/SYM.h:41;
    // owner is the interp SIGINT shutdown path; not a corpus unit.
    // H-014 delegate (Extract2 seat-conversion precedent): sigint
    // is unit 211 (interp-sysfunc.cpp::sigint), rendered in
    // Sigint.kt (this tranche); the SYM.h:41 call sites bind to
    // the real signal termination handler unchanged.
    fun sigint(i: Int): Unit =
        doc.sigint(i)

    // ------------------------------------------------------------------
    // B3 tranche 3 / sub-push A seats (units 83/84/86):

    // TODO stub: gtk_tree_store_append(GtkTreeStore*, GtkTreeIter*,
    // GtkTreeIter* parent) owned by the GTK library (out-of-scope C
    // function, vendor interp-zfcns.cpp:101); not a corpus unit. The
    // IntArray holders carry the opaque iters (unit 83 render).
    fun gtkTreeStoreAppend(t: String, itera: IntArray, iterb: IntArray?): Unit =
        throw NotImplementedError("gtk_tree_store_append: out-of-scope owner (GTK library)")

    // TODO stub: gtk_tree_store_set(GtkTreeStore*, GtkTreeIter*, int
    // column, value, -1) owned by the GTK library (vendor
    // interp-zfcns.cpp:103 etc.); not a corpus unit.
    fun gtkTreeStoreSet(t: String, itera: IntArray, column: Int, value: String): Unit =
        throw NotImplementedError("gtk_tree_store_set: out-of-scope owner (GTK library)")

    // TODO stub: mult(char a[], char b[], char c[]) declared
    // compiler-sys1.c:1080 (the decimal-string multiply of the
    // exponent expansion); not a corpus unit (units.csv has no
    // compiler-sys1.c mult row); owner named per H-014.
    fun mult(a: CharArray, b: String, c: CharArray): Unit =
        throw NotImplementedError("mult: out-of-scope owner (compiler-sys1.c::mult)")

    // TODO stub: mult(char a[], char b[], char c[]) - the CHAR-ARRAY
    // OVERLOAD of the seat above (same-name resolution by arity/types).
    // B9 T2 FLIP: the u257 compiler-sys1.c::<misc> ledger render landed
    // the BODY (CompilerSys1Misc.kt - the K&R float-precision decimal-
    // string multiply, gcvt(7) quirk); the earlier interp-arith.cpp:624
    // batch-7 tag was a boundary misconstruction (the roster row is the
    // <misc> row 258); unit 93 calls bind unchanged; this seat now
    // DELEGATES. The String-arg overload above STAYS a stub for its own
    // emission call sites (Cannon.kt "10"/".1" exponent seats).
    fun mult(a: CharArray, b: CharArray, c: CharArray): Unit =
        doc.mult(a, b, c)

    // TODO stub: the POSIX read(2) raw file-descriptor entry; the JVM
    // carries no raw fd domain. fd 0 (stdin) routes to the arm
    // STDIN_READER carrier (getstr2 unit-48 precedent, chars standing
    // in for bytes per H-012); fd != 0 is out of scope (units.csv has
    // no POSIX unit - the owner here is the OS syscall interface),
    // named per H-014.
    fun posixRead(fd: Int, buf: CharArray, off: Int, len: Int): Int {
        if (fd != 0) {
            throw NotImplementedError("posixRead: out-of-scope owner (POSIX read(2), fd != 0)")
        }
        var n = 0
        while (n < len) {
            val c = STDIN_READER.read()
            if (c == -1) break
            buf[off + n] = c.toChar()
            n++
        }
        return if (n == 0) -1 else n
    }

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

    // H-014 delegate (Extract2 seat-conversion precedent): freadx is
    // unit 209 (interp-native-btree.cpp::freadx), rendered in
    // Freadx.kt; this seat flips the batch-2 placeholder to the
    // same-name owner resolved by arity at call sites.
    fun freadx(block: Int, mode: Int): ByteArray =
        doc.freadx(block, mode)

    // ------------------------------------------------------------------
    // batch-2 tail stubs (units 52/53/54)

    // TODO stub: memcheck owned by compiler-parse.c:2068; below roster
    // min loc - not a corpus unit.
    fun memcheck(iiitmp: Int): Unit = doc.memcheck(iiitmp)

    // Extract2 delegation: owned by compiler-parse.c::Extract2
    // (batch-1 unit 15, landed in tranche 3 - the unit-52 call in
    // IfCode passes 0; ExpandGet passes the CodedComma tail offset).
    fun Extract2(buf: CharArray, offset: Int): CharArray =
        extract2(buf, offset)

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
    // LANDS as batch-4 unit 109 (AtTest.kt): delegate per the H-014
    // Extract2 precedent; IfCode binds unchanged.
    fun atTest(xd: CharArray, pd1: CharArray, pd1Last: ShortArray,
               lines: Int, xpx: Long): Int =
        doc.atTest(xd, pd1, pd1Last, lines, xpx)

    // Unit 222 (B8 tranche 1) RESOLVES this seat with a TAG
    // CORRECTION: the earlier comment tagged the owner a
    // compiler-at-test.c helper with no roster row; the physical
    // owner is compiler-mumps.c:1956 and IS a roster unit
    // (units.csv row 241, the batch-8 atCopy render,
    // CompilerAtCopy.kt). The compiler-globals.h:104 declaration
    // above stands as the prototype anchor. Delegate per the
    // H-014 Extract2 precedent; IfCode SetCode and WriteCode
    // bind unchanged.
    fun atCopy(at_flg: Int, tmp: CharArray): Unit =
        doc.atCopy(at_flg, tmp)

    // B9 T3 FLIP (H-014 hit-adapt, consult 2026-11-22T05:30:00Z;
    // unit 268 compiler-parse.c::parse_, roster row 248, the
    // COURSE ARM monster loc 1201): the zero-arg seat converts
    // from the TODO throw (the u160 tag-correction note retained
    // above) to the CompilerParseMain.kt render delegate; the one-arg
    // interp twin of Parse_.kt is unaffected (overload by arity);
    // the IfCode.kt call site binds unchanged.
    fun parse_(): Int = doc.parse_()

    // H-014 delegate (Extract2 seat-conversion precedent): the interp
    // twin int parse_(struct MSV *) is unit 93, rendered in Parse_.kt;
    // this delegate converts the former placeholder seat into a
    // same-name overload resolved by arity at call sites.
    fun parse_(svPtr: Msv): Int = doc.parse_(svPtr)

    // TODO stub: linepad owned by compiler-sys1.c::linepad (loc 5, below
    // roster min loc - not a corpus unit). B9 T2 FLIP: the u257
    // compiler-sys1.c::<misc> ledger render landed the BODY
    // (CompilerSys1Misc.kt - the banner-comment emitter); Epi.kt/
    // ForCode.kt call sites bind unchanged; this seat now DELEGATES.
    fun linepad(n: Int, s: String): Unit =
        doc.linepad(n, s)

    // TODO stub: linepad2 owned by compiler-sys1.c:963; not a corpus
    // unit (below roster min loc). Seat added by the unit-207 fcninit
    // render (H-014 seat-conversion precedent). B9 T2 FLIP: the u257
    // compiler-sys1.c::<misc> ledger render landed the BODY
    // (CompilerSys1Misc.kt - the epigraph rule emitter); the fcninit
    // call sites bind unchanged; this seat now DELEGATES.
    fun linepad2(s: String): Unit =
        doc.linepad2(s)

    // ------------------------------------------------------------------
    // unit 60 (libmpscpp.cpp::global::TreePrint) member seats: the C++
    // class-global member functions are not roster units (units.csv has
    // no global::Order/Data/invoke rows across any arm unit).

    // (*this)(k1..kn).Order(1) - n-subscript Order walk (TreePrint uses
    // 3..10 subscripts; the one/two-subscript seats above cover the
    // small arities).
    fun globalOrderN(g: GlobalRef, vararg keys: String): String =
        throw NotImplementedError("global::Order(this,*keys): out-of-scope owner (libmpscpp.cpp member)")

    // (*this)(k1..kn).Data() - n-subscript Data probe returning the
    // integer DATA-test style value (TreePrint branches on 1/11/<2).
    fun globalDataN(g: GlobalRef, vararg keys: String): Int =
        throw NotImplementedError("global::Data(this,*keys): out-of-scope owner (libmpscpp.cpp member)")

    // (string)(*this)(k1..kn) - invoked-global string conversion
    // (operator string / operator<<).
    fun getGlobalString(g: GlobalRef, vararg keys: String): String =
        throw NotImplementedError("global string conversion: out-of-scope owner (libmpscpp.cpp member)")

    // ------------------------------------------------------------------
    // unit 61 (libmpscpp.cpp::sw) alignment-scope seats: none of the
    // remaining alignment helpers are roster units.

    // void init(void) - matrix initializer. LANDS as batch-4 unit 108
    // (Init.kt): this seat converts to the delegate per the H-014
    // Extract2 precedent; the sw call sites bind unchanged.
    fun init(): Unit = doc.init()

    // void Cost(char*,char*,int,int) - cost matrix fill. DELEGATE:
    // the owner landed as B3 batch-3 unit 74 (Cost.kt, tranche 2);
    // the unit-61 sw call sites bind unchanged to this seat
    // (H-014 delegate precedent, Extract2). Return widens Unit->Int:
    // the vendor return was always the memo cell value and sw
    // ignores it.
    fun Cost(s: CharArray, t: CharArray, i: Int, j: Int): Int =
        cost(s, t, i, j)

    // int nodash(char*) - non-dash char count of an alignment string;
    // not a roster unit.
    fun nodash(m: CharArray): Int =
        throw NotImplementedError("nodash: out-of-scope owner (libmpscpp.cpp nodash)")

    // ------------------------------------------------------------------
    // unit 62 (libmpscpp.cpp misc-2 arithmetic operator family) seats:
    // the *this >> value typed extraction, the *this = value raw
    // assignment, and *this << value store idioms are class-global
    // members, not roster units; one seat per C numeric type.

    fun globalGetInt(g: GlobalRef): Int =
        throw NotImplementedError("global int extraction: out-of-scope owner (libmpscpp.cpp member)")
    fun globalGetUInt(g: GlobalRef): UInt =
        throw NotImplementedError("global unsigned int extraction: out-of-scope owner (libmpscpp.cpp member)")
    fun globalGetShort(g: GlobalRef): Short =
        throw NotImplementedError("global short extraction: out-of-scope owner (libmpscpp.cpp member)")
    fun globalGetUShort(g: GlobalRef): UShort =
        throw NotImplementedError("global unsigned short extraction: out-of-scope owner (libmpscpp.cpp member)")
    fun globalGetLong(g: GlobalRef): Long =
        throw NotImplementedError("global long extraction: out-of-scope owner (libmpscpp.cpp member)")
    fun globalGetULong(g: GlobalRef): ULong =
        throw NotImplementedError("global unsigned long extraction: out-of-scope owner (libmpscpp.cpp member)")
    fun globalGetFloat(g: GlobalRef): Float =
        throw NotImplementedError("global float extraction: out-of-scope owner (libmpscpp.cpp member)")
    fun globalGetDouble(g: GlobalRef): Double =
        throw NotImplementedError("global double extraction: out-of-scope owner (libmpscpp.cpp member)")
    // char tmp[STR_MAX] extraction: the string read of the global.
    fun globalGetStrMax(g: GlobalRef): String =
        throw NotImplementedError("global char* extraction: out-of-scope owner (libmpscpp.cpp member)")

    fun globalSetInt(g: GlobalRef, v: Int): Unit =
        throw NotImplementedError("global int assignment: out-of-scope owner (libmpscpp.cpp member)")
    fun globalSetUInt(g: GlobalRef, v: UInt): Unit =
        throw NotImplementedError("global unsigned int assignment: out-of-scope owner (libmpscpp.cpp member)")
    fun globalSetShort(g: GlobalRef, v: Short): Unit =
        throw NotImplementedError("global short assignment: out-of-scope owner (libmpscpp.cpp member)")
    fun globalSetUShort(g: GlobalRef, v: UShort): Unit =
        throw NotImplementedError("global unsigned short assignment: out-of-scope owner (libmpscpp.cpp member)")
    fun globalSetLong(g: GlobalRef, v: Long): Unit =
        throw NotImplementedError("global long assignment: out-of-scope owner (libmpscpp.cpp member)")
    fun globalSetULong(g: GlobalRef, v: ULong): Unit =
        throw NotImplementedError("global unsigned long assignment: out-of-scope owner (libmpscpp.cpp member)")
    fun globalSetFloat(g: GlobalRef, v: Float): Unit =
        throw NotImplementedError("global float assignment: out-of-scope owner (libmpscpp.cpp member)")
    fun globalSetDouble(g: GlobalRef, v: Double): Unit =
        throw NotImplementedError("global double assignment: out-of-scope owner (libmpscpp.cpp member)")
    // ------------------------------------------------------------------
    // batch-1 tranche-3 seats (units 18/19/20 call sites; owners
    // named per H-014; seats convert to delegates when the owning
    // batch-1 units land - the Extract2 precedent)

    // H-014 delegate (Extract2 seat-conversion precedent): epi is
    // unit 213 (compiler-epi.c::epi, Epi.kt), this tranche; the
    // seat's abrtx-era (String, Int) guess was a prototype
    // mis-tag - the vendor signature is (short, long), the
    // CompilerState.ExecName/pid seats carry the TAG CORRECTION.
    fun epi(ExecName: Int, pid: Long): Unit =
        doc.epi(ExecName, pid)

    // H-014 delegate (Extract2 seat-conversion precedent): EP is
    // unit 206 (compiler-mumps.c::<misc> EP, CompilerMumpsMisc.kt),
    // this tranche; same TAG CORRECTION (int, short, long).
    fun EP(x: Int, ExecName: Int, pid: Long): Unit =
        doc.EP(x, ExecName, pid)

    // TODO stub: floatstring owned by interp-arith.cpp:597 (static
    // inline below-roster helper; numcomp branch selector).
    fun floatstring(p: CharArray): Boolean =
        throw NotImplementedError("floatstring: out-of-scope owner (interp-arith.cpp:597 inline)")

    // ------------------------------------------------------------------
    // unit 19 (interp-interp.cpp::<misc>) prototype roster seats:
    // declarations of the interpretation core; owners named. Several
    // are batch-1 units landing in later tranches (DirectRead =
    // unit 24, _SymFree = unit 28, patrn1 = unit 30); those seats
    // convert to delegates when the units land.

    // lpad RESOLVED: the real body rendered by the batch-5 unit 141
    // (interp-strmanip.cpp::<misc>); the Fcn unit-123 call site
    // rebinds to the render. Stub seat removed.

    // ------------------------------------------------------------------
    // unit 137: the MSV Transpose wrapper hands off to the class member
    // global::Transpose(global) (vendor ~libmpscpp.cpp:2164 family);
    // not a corpus unit - owner-named H-014 seat raised by unit 137.
    fun globalRefTranspose(a: GlobalRef, b: GlobalRef): Unit =
        a.Transpose(b)

    // RESOLVED: void substr(unsigned char *, unsigned char *, int,
    // int) - the owner is the batch-7 unit 190 render
    // (interp-strmanip.cpp twin aside: the owner row is
    // interp-strmanip.cpp::substr, StrmanipSubstr.kt). The past
    // "interp substr helper" tag was owner-correct; this seat now
    // delegates to the unit-190 real per the inout precedent; the
    // PatrnInterp/InterpParse call sites bind through here. The
    // compiler-sys1.c short-domain twin (batch-7 unit 189,
    // CompilerSubstr.kt) is the DISTINCT landed render; the
    // compiler-side call sites (Match.kt, PatrnSys1.kt) rebind
    // directly to it in the same push.
    fun substr(a: CharArray, b: CharArray, i: Int, j: Int): Unit =
        doc.substr(a, b, i, j)

    // LocalName RESOLVED: the seat's "not a roster unit" tag was a
    // mis-tag (TAG CORRECTION) - interp-sysfunc.cpp::LocalName IS the
    // batch-4 unit 105 (LocalName.kt, rendered with the nullable tmp2
    // of the vendor signature). The stub seat is removed; no in-arm
    // call site binds it today.

    // GlobalQuery RESOLVED: the seat's "not a roster unit" tag was
    // a mis-tag (TAG CORRECTION) - interp-sym.cpp::GlobalQuery IS the
    // batch-4 unit 119 (GlobalQuery.kt). The stub seat is removed;
    // no in-arm call site binds it today (the query family binds
    // through the Qsub/Qlength seats).

    // void Qsub(unsigned char *, unsigned char *, unsigned char
    // *, MSV *); not a roster unit.
    fun Qsub(a: CharArray, b: CharArray, c: CharArray, sv: Msv): Unit =
        throw NotImplementedError("Qsub: out-of-scope owner (interp Qsub helper)")

    // void Qlength(unsigned char *, unsigned char *, MSV *);
    // RESOLVED by the batch-5 unit 145 render (Qlength.kt).
    fun Qlength(a: CharArray, b: CharArray, sv: Msv): Unit =
        doc.Qlength(a, b, sv)

    // void ErrorMessage(const char *, long); owner
    // interp-parse.cpp::ErrorMessage (not a roster unit).
    // ErrorMessage delegation: owned by interp-sysfunc.cpp
    // (batch-1 unit 25 misc residue, landed in tranche 4).
    fun ErrorMessage(msg: String, ln: Long): Unit =
        errorMessage(msg, ln)

    // char * sym_(int, unsigned char *, unsigned char *, MSV *);
    // owner is the interp symbol-table routing interface.
    fun sym_(opcode: Int, name: CharArray, rslt: CharArray, sv: Msv): CharArray? =
        throw NotImplementedError("sym_: out-of-scope owner (interp symbol routing)")

    // void inout(char *, short, short, MSV *);
    // owner interp-parse.cpp::inout at loc 38
    // (batch-10 roster unit; the compiler-sys1.c::inout unit-59
    // render is the DISTINCT landed twin of the same C body).
    fun inout(text: CharArray, cmnd: Short, col: Short, sv: Msv): Unit =
        throw NotImplementedError("inout: out-of-scope owner (interp-parse.cpp::inout) batch-10 roster unit")

    // int Merge(unsigned char *, unsigned char *, MSV *);
    // owner interp-sysfunc.cpp:650 Merge (batch-8 roster unit,
    // loc 59 signature row).
    fun Merge(a: CharArray, b: CharArray, sv: Msv): Int = doc.Merge(a, b, sv)

    // int xindex(const unsigned char *, const unsigned char *,
    // short) - DELEGATE (unit 264 of batch 9 has landed; the
    // batch-1 "not a roster unit" tag was the u243-precedent
    // tag correction - owner interp-strmanip.cpp:87, row 265).
    fun xindex(a: CharArray, b: CharArray, sh: Short): Int =
        doc.xindex(a, b, sh)

    // int TimeOut(int, unsigned char *, char *, int); not a roster
    // unit.
    fun TimeOut(a: Int, b: CharArray, c: CharArray, d: Int): Int =
        throw NotImplementedError("TimeOut: out-of-scope owner (interp timeout helper)")

    // int TimeOut_getstr1(int, unsigned char *, char *, int);
    fun TimeOut_getstr1(a: Int, b: CharArray, c: CharArray, d: Int): Int =
        throw NotImplementedError("TimeOut_getstr1: out-of-scope owner (interp timeout reader)")

    // DirectRead delegation: owned by interp-sysfunc.cpp::DirectRead
    // (batch-1 unit 24, landed in tranche 4).
    fun DirectRead(a: Int, b: CharArray): Int =
        directRead(a, b)

    // int _SymFree(int, MSV *); the owning unit 28 landed
    // (SymFree.kt); the seat is the delegate to that render
    // (H-014 delegate precedent).
    fun SymFree(a: Int, sv: Msv): Int = symFree(a, sv)

    // int sql(int, struct MSV *, char *DataSourceName, char
    // *Command, char *, const char *); owner is the interp SQL
    // subprocess (not a roster unit). The CharArray parameters
    // are nullable per the unit-123 fcn call site (the vendor
    // NULL triple of the \u0024ztabsize OPEN call).
    fun sql(a: Int, sv: Msv, dsn: CharArray?, command: CharArray?,
            e: CharArray?, tail: String?): Int =
        throw NotImplementedError("sql: out-of-scope owner (interp SQL subprocess)")

    // static void patrn1(short *, short *, struct MSV *); the
    // owning unit 30 landed (Patrn1Interp.kt); the seat is the
    // delegate to that render (H-014 delegate precedent). NOTE
    // (H-015): compiler-sys1.c::patrn1 is a DISTINCT batch-1 unit
    // (26, Patrn1Sys1.kt two-arg render) - same-name units resolve
    // by overload arity at the call sites.
    fun patrn1(jpx: ShortArray, ernbr: ShortArray, svPtr: Msv): Unit =
        doc.patrn1(jpx, ernbr, svPtr)

    // TODO stub: unit 88 (compiler-sys1.c::patrn) and unit 92
    // (interp-parse.cpp::patrn) definite/indefinite matcher seats.
    // void match(short pts[3][20], short lx, short ct[], unsigned
    // char *ad, short *rslt) - owner compiler-sys1.c::match, a
    // batch-7 unit; H-014 pointer-shift note: the patrn call sites
    // pass substr(&bd[1], ...) whose pointer shift cannot ride the
    // CharArray contract - the substr seat names it; the pts
    // short[3][20] renders as Array<ShortArray> (row 0 unused,
    // vendor indexing); ShortArray ct carries the count/ct2 tables;
    // the short* rslt out-param is the H-006 ShortArray(1) holder.
    // H-014 delegate (Extract2 seat-conversion precedent): the 5-arg
    // compiler twin is unit 212 (compiler-sys1.c::match), rendered in
    // CompilerMatch.kt (the 6-arg interp twin is Match.kt, unit 180);
    // the same-name pair resolves by arity at call sites.
    fun match(pts: Array<ShortArray>, lx: Int, ct: ShortArray,
              ad: CharArray, rslt: ShortArray): Unit =
        doc.match(pts, lx, ct, ad, rslt)

    // Same-name twin (H-015 overload-arity rule):
    // void match(short pts[][20], short lx, short ct[], unsigned
    // char *ad, short *rslt, unsigned char bd[]) - owner
    // interp-parse.cpp::match, batch-7 unit; the sixth argument
    // hands the matcher the interpreter bd scratch
    // (patrn(ad, svPtr) passes svPtr.bd, PatrnInterp.kt).
    fun match(pts: Array<ShortArray>, lx: Int, ct: ShortArray,
              ad: CharArray, rslt: ShortArray, bd: CharArray): Unit =
        throw NotImplementedError("match: out-of-scope owner (interp-parse.cpp::match) batch-7 unit")

    // int set_code(int entry_pt); owner compiler-set.c::set_code, a
    // batch-4 unit (compiler-macros.h:31 prototype). Passenger of
    // the unit-91 render (NewCode.kt): passed the SET set
    // entry-point token (the last of the two compiler-macros.h SET
    // defines - see the unit-91 quirk ledger) and returns the
    // PARSE_ERROR..ABRTX cascade verbatim. H-017: the C-ABI snake
    // name is kept verbatim (sym_ naming precedent).
    // LANDS as batch-4 unit 121 (SetCode.kt, same tranche): the
    // seat converts to the delegate per the H-014 Extract2
    // precedent; the LockCode.kt call site binds unchanged.
    fun set_code(entry_pt: Int): Int =
        doc.set_code(entry_pt)

    // ------------------------------------------------------------------
    // unit 14 (libmpscpp.cpp::global::TermCorrelate) keyed seats:
    // the subscripted class-global operator family is not in the
    // corpus; these named seats carry the keyed read-add-assign
    // chain (H-014).

    // B(k1,k2)+i: keyed global integer-plus operator result,
    // returned as the new value string (owner: libmpscpp.cpp
    // subscripted global operator+ family; not a roster unit).
    fun globalPlusIntKeyed(g: GlobalRef, i: Int, k1: String, k2: String): String =
        throw NotImplementedError("global keyed plus: out-of-scope owner (libmpscpp.cpp subscripted operator+ family)")

    // B(...) = value: keyed global string assignment (owner:
    // libmpscpp.cpp subscripted assignment operator family; not a
    // roster unit).
    fun globalSetKeyed(g: GlobalRef, v: String, vararg keys: String): Unit =
        throw NotImplementedError("global keyed assignment: out-of-scope owner (libmpscpp.cpp subscripted operator= family)")

    // TODO stub: unpad owned by interp-sysfunc.cpp::unpad (batch-10
    // unit, below tonight's stratum; fullUnpad call site).
    fun unpad(a: CharArray, sv: Msv): Unit =
        throw NotImplementedError("unpad: out-of-scope owner (interp-sysfunc.cpp::unpad) batch-10 unit")


    // ------------------------------------------------------------------
    // unit 68 (B3 tranche 1): the class-global member Max over the
    // unsubscripted/subscripted reference raised this seat. TAG
    // CORRECTION with unit 171 (B6 tranche 2): the roster row
    // libmpscpp.cpp::global::Max (loc 26) is a batch-6 corpus unit
    // after all (the earlier "not a corpus unit" note was a roster
    // mis-read, journaled); the seat now delegates to the real
    // GlobalMax.kt unit-171 render (the XNEXT walk Cosine family).
    fun globalMax(g: GlobalRef): Double = g.max()

    // ------------------------------------------------------------------
    // unit 31 carrier seats (H-014 stubs; owners below the roster or
    // outside the pre-registered chunks):

    // char *Extract1(char *); owner is compiler-parse.c:2119
    // (below the roster cut); mirror of Extract2 (unit 15 owner).
    fun Extract1(buf: CharArray): CharArray = doc.Extract1(buf)

    // unsigned long lhash(char *); owner is libmpscpp.h:273 (hash
    // driver below the roster). Returns unsigned long (H-012); the
    // Long seat carries the domain, the sign bit is never set.
    fun lhash(s: String): Long = doc.lhash(s)

    // void global::MakeGlobal(int, const char *, const char *, ...);
    // the class-global member builds the subscripted MUMPS reference
    // (owner libmpscpp.cpp global::MakeGlobal, outside the unit 31
    // chunk). fmt is the a,b,c... letter list, args the subscripts.
    // RESOLVED with unit 199 (B7 tranche 2): delegates to the real
    // MakeGlobal.kt render (the unit-31 invoke-family call sites
    // bind unchanged; the receiver's ref carrier serves the
    // vendor gblnme fold - the render's signature ledger).
    fun makeGlobal(g: GlobalRef, mode: Int, fmt: String,
            vararg args: String): Unit {
        g.makeGlobal(mode, g.ref, fmt, *args)
    }

    // TODO stub: int count(char *); owner libmpscpp.cpp:2932 - the
    // key letter/comma tally of the global::MakeGlobal varargs walk
    // (raised by the unit-199 render of B7 tranche 2); not a
    // corpus unit (no units.csv row). H-017: the vendor name kept.
    // H-017: the vendor name kept. RESOLVED by unit 214 (B7 T3,
    // libmpscpp.cpp::<misc-6>): delegate to doc.count.
    fun count(key: String): Int = doc.count(key)

    // ------------------------------------------------------------------
    // unit 214 (B7 T3, libmpscpp.cpp::<misc-6>) H-014 seats, the
    // operator& forwards reach the Concat(x7) members. RESOLVED by
    // unit 238 (B8 tranche 3, libmpscpp.cpp::<misc-7>): TAG
    // CORRECTION per the unit-119 precedent - the "batch-10" owner
    // tags below were forward-tags; the member bodies ARE the
    // misc-7 roster text, and every seat flips to its render below
    // (LibmpscppMisc7.kt). The global.global member stays the
    // landed unit-65 render (extension GlobalRef.concat; no seat);
    // the receiver-first args carry the member receiver (H-006 idiom).

    // mstring global::Concat(double)/(long)/(int)/(char*)/
    // (string)/(mstring) - unit 238 renders (misc-7).
    fun Concat(x: GlobalRef, b: Double): Mstring = x.concat(b)

    fun Concat(x: GlobalRef, b: Long): Mstring = x.concat(b)

    fun Concat(x: GlobalRef, b: Int): Mstring = x.concat(b)

    fun Concat(x: GlobalRef, b: CharArray): Mstring = x.concat(b)

    fun Concat(x: GlobalRef, b: String): Mstring = x.concat(b)

    fun Concat(x: GlobalRef, b: Mstring): Mstring = x.concat(b)

    // mstring::Concat(global)/(double)/(long)/(int)/(mstring)/
    // (char*)/(string) - unit 238 renders (misc-7).
    fun Concat(x: Mstring, b: GlobalRef): Mstring = x.concat(b)

    fun Concat(x: Mstring, b: Double): Mstring = x.concat(b)

    fun Concat(x: Mstring, b: Long): Mstring = x.concat(b)

    fun Concat(x: Mstring, b: Int): Mstring = x.concat(b)

    fun Concat(x: Mstring, b: Mstring): Mstring = x.concat(b)

    fun Concat(x: Mstring, b: CharArray): Mstring = x.concat(b)

    fun Concat(x: Mstring, b: String): Mstring = x.concat(b)

    // TODO stub: mstring::FixGblRef() - the quoted-reference fixer
    // of the unit-149 header ledger; owner libmpscpp.cpp::FixGblRef
    // (declared mstringClass.h, no roster row). Raised by unit 238
    // (B8 T3): the mstring qlength/query members substrate it.
    // B9 T2 FLIP: the roster row 263 lands the body this tranche
    // (MstringFixGblRef.kt, the u262 render) - the earlier "no
    // roster row" note was the ZfcnsH-batch-10 misconstruction
    // (the units.csv row 263 IS the row); this seat now DELEGATES.
    fun fixGblRef(a: Mstring): Mstring =
        a.FixGblRef()

    // ------------------------------------------------------------------
    // seats raised by unit 93 (interp-parse.cpp::parse_) ----

    // TODO stub: add(a, b, c) char-array string arithmetic family,
    // owner interp-arith.cpp:624 (not a roster unit); unit 93 calls
    // with whole-array seats (pd1/bd), pointer shifts are zero in
    // this render (H-014 ledger note).
    fun add(a: CharArray, b: CharArray, c: CharArray): Unit =
        throw NotImplementedError("add: out-of-scope owner (interp-arith.cpp::add)")

    // TODO stub: sub, owner interp-arith.cpp:658.
    fun sub(a: CharArray, b: CharArray, c: CharArray): Unit =
        throw NotImplementedError("sub: out-of-scope owner (interp-arith.cpp::sub)")

    // TODO stub: divx, owner interp-arith.cpp:682.
    fun divx(a: CharArray, b: CharArray, c: CharArray): Unit =
        throw NotImplementedError("divx: out-of-scope owner (interp-arith.cpp::divx)")

    // TODO stub: divi, owner interp-arith.cpp:768.
    fun divi(a: CharArray, b: CharArray, c: CharArray): Unit =
        throw NotImplementedError("divi: out-of-scope owner (interp-arith.cpp::divi)")

    // TODO stub: modulo, owner interp-arith.cpp:695.
    fun modulo(a: CharArray, b: CharArray, c: CharArray): Unit =
        throw NotImplementedError("modulo: out-of-scope owner (interp-arith.cpp::modulo)")

    // TODO stub: expx, owner interp-arith.cpp (batch-5 corpus unit;
    // ln/exponent arm of the arithmetic family).
    fun expx(a: CharArray, b: CharArray, c: CharArray): Unit =
        throw NotImplementedError("expx: out-of-scope owner (interp-arith.cpp::expx) batch-5 unit")

    // TODO stub: rpm(string_, pattern, svPtr) regular-expression match,
    // owner interp-rpm.cpp::rpm (NOT a corpus unit - its roster rows
    // are parse_pattern_element/parse_count/misc/scan_token/perl_pm/
    // leaning_toothpickize); raised because the vendor forward-declares
    // rpm mid-switch at interp-parse.cpp:1190 (H-014 seat).
    fun rpm(string_: CharArray, pattern: CharArray, svPtr: Msv): Int =
        doc.rpm(string_, pattern, svPtr)

    // Unit 233 (B8 tranche 2) RESOLVES this seat: delegates to the
    // real BuildGlobal.kt render (the three-arm build-stack dispatch
    // plus the four-arg GlobalOrder walk tail; the nullability of
    // the original widening kept; original stub note: batch-8
    // unit 150, interp-parse.cpp:1836; rslt nullable because unit
    // 93 passes NULL for the result pointer in one arm; str/svPtr
    // widened nullable (H-012) because the unit-185 Interpret
    // stack-cleaner arm passes NULL str and NULL svPtr (Build
    // (-1, 0, NULL, NULL, NULL) at the interp-interp.cpp MERGE cmd
    // cleaner line)). The unit-70/93 call sites bind unchanged.
    fun BuildGlobal(code: Int, g: Int, str: CharArray?,
            rslt: CharArray?, svPtr: Msv?): Int =
        doc.BuildGlobal(code, g, str, rslt, svPtr)

    // Unit 232 (B8 tranche 2) RESOLVES this seat: delegates to
    // the real BuildLocal.kt render (same shape as BuildGlobal,
    // widened the same way (H-012)); the code-1 tail walks the
    // LOCAL order through the LocalOrder seat raised below.
    fun BuildLocal(code: Int, g: Int, str: CharArray?,
            rslt: CharArray?, svPtr: Msv?): Int =
        doc.BuildLocal(code, g, str, rslt, svPtr)

    // void LocalOrder(unsigned char *, unsigned char *, char *,
    // struct MSV *); owner interp-sym.cpp::LocalOrder, NOT a corpus
    // unit (no roster row) - raised this push by the unit-232
    // BuildLocal render's code-1 walk tail (H-014).
    fun LocalOrder(a: CharArray, rslt: CharArray, dir: String,
            svPtr: Msv?): Unit =
        throw NotImplementedError("LocalOrder: out-of-scope owner (interp-sym.cpp::LocalOrder, not a corpus unit)")

    // POSIX read/write over the exec-pipe fd pairs of the compiler
    // READ transport (popn[lun][1]/[2] of compiler-sys1.c getstr1,
    // unit 229); out-of-scope POSIX, not corpus units (H-014;
    // raised by the unit-229 render's opnflg==3 arm).
    fun posixWrite(fd: Int, buf: CharArray, len: Int): Unit =
        throw NotImplementedError("write: out-of-scope owner (POSIX pipe transport, not a corpus unit)")

    fun posixRead(fd: Int, buf: CharArray, max: Int): Int =
        throw NotImplementedError("read: out-of-scope owner (POSIX pipe transport, not a corpus unit)")
    // ------------------------------------------------------------------
    // unit 98 (B4 tranche 1): the class-global member Count over the
    // unsubscripted/subscripted reference (owner libmpscpp.cpp:2257
    // global::Count) is not a corpus unit; owner-named seat raised by
    // unit 98 (sibling of globalMax unit 68; note the REAL unit-44
    // GlobalRef.Min render needs no seat).
    fun globalCount(g: GlobalRef): Double =
        g.Count()

    // ------------------------------------------------------------------
    // unit 130: the avg-intrinsic unsubscripted/subscripted reference
    // (owner libmpscpp.cpp::global::Avg, vendor :10251) IS a corpus
    // unit (roster row 262, batch 9); owner-named seat raised by unit
    // 130, delegate to the real render when batch 9 lands. B9 T2 FLIP:
    // the unit-261 render landed (GlobalAvg.kt) - this seat now
    // DELEGATES to it.
    fun globalAvg(g: GlobalRef): Double =
        g.Avg()

    // ------------------------------------------------------------------
    // unit 113 (interp-rpm.cpp::parse_pattern_element) H-014 seats

    // struct bstring bstrcat(struct bstring, struct bstring); owner
    // interp-bstring.cpp::bstrcat - CORRECTLY TAGGED batch-9 roster
    // row (the body sits below the unit-22 chunk cut, banner-only
    // there). RESOLVED at batch 9 tranche 1 (unit 255, Bstrcat.kt):
    // delegate below.
    fun bstrcat(a: Bstring, b: Bstring): Bstring =
        doc.bstrcat(a, b)

    // struct bstring leaning_toothpickize(const struct bstring);
    // owner interp-rpm.cpp::leaning_toothpickize - batch-6 roster
    // row of the same vendor file. Landed (unit 157): delegate.
    fun leaning_toothpickize(text: Bstring): Bstring =
        doc.leaning_toothpickize(text)

    // struct bstring parse_pattern_atom_list(struct parser*);
    // sub-min-loc function of interp-rpm.cpp - owned by the batch-5
    // interp-rpm.cpp::<misc> chunk.
    fun parse_pattern_atom_list(p: parser): Bstring =
        doc.parse_pattern_atom_list(p)

    // void accept_it(struct parser*); sub-min-loc function of
    // interp-rpm.cpp - batch-5 misc chunk owner.
    fun accept_it(p: parser): Unit =
        doc.accept_it(p)

    // void accept(struct parser*, const enum token_type); the
    // expectation-checking accept; batch-5 misc chunk owner.
    fun accept(p: parser, expected: token_type): Unit =
        doc.accept(p, expected)

    // static void syntax_error(const char[], const char[], const
    // int); RESOLVED by the batch-5 unit 152 render.
    fun syntax_error(msg: String, input: String, offset: Int): Unit =
        doc.syntax_error(msg, input, offset)

    // static void take_char(struct scanner *s); seat raised by
    // unit 147 (scan_token) and RESOLVED by its unit-152
    // same-tranche render.
    fun take_char(s: scanner): Unit =
        doc.take_char(s)

    // int perl_pm(const char[], const char[], const int, struct
    // MSV *); owner interp-rpm.cpp::perl_pm - batch-9 roster
    // row 257 (the reges conversion of the PCRE original). B9
    // T3 FLIP (C-008 consult 2026-11-22T05:39:00Z, H-015
    // hit-adapt): the unit-277 render landed the body
    // (PerlPm.kt, kotlin Regex over java PatternSyntaxException
    // per H-018); the rpm/perlmatch/perlcpp drivers of the
    // batch-5 unit-152 render bind unchanged.
    fun perlPm(string_: String, pattern: String, mode: Int,
            svPtr: Msv): Int = doc.perl_pm(string_, pattern, mode, svPtr)

    // int pm(char *, char *, struct MSV *); owner
    // interp-pm.cpp::pm (the '?' pattern-match motor) - not a
    // roster unit; raised by the batch-5 unit 154 pmw wrappers.
    fun pm(a: CharArray, b: CharArray, svPtr: Msv): Int =
        throw NotImplementedError("pm: out-of-scope owner (interp-pm.cpp::pm)")

    // int wstem(char *, int); owner libmpscpp.cpp::wstem (the
    // Porter-stemmer straddler) - not a roster unit; raised by
    // the batch-5 unit 154 Stem/stem renders.
    fun wstem(a: CharArray, len: Int): Int =
        throw NotImplementedError("wstem: out-of-scope owner (libmpscpp.cpp::wstem)")

    // char *strsep(char **, char *); libc strsep split helper
    // (declared by the sym.h misc chunk of unit 144) - not a
    // roster unit, no in-arm call site today; seat raised per
    // the unit-144 ledger. The char** stringp in-place holder
    // renders the Array<String> carrier (H-006 holder idiom).
    fun strsep(stringp: kotlin.Array<kotlin.String>, delim: String): String? =
        throw NotImplementedError("strsep: out-of-scope owner (libc string.h)")

    // void ServiceInterface(unsigned char *, unsigned char *,
    // unsigned char *, unsigned char *, unsigned char *, char,
    // struct MSV *); interp-side service entry declared by the
    // sym.h misc chunk of unit 144 - not a roster unit, no
    // in-arm call site today; seat raised per the unit-144
    // ledger. The c/d args are nullable (H-012): this TU's zcn callers
    // pass NULL x3/x4 (the unit-183 ledger).
    fun ServiceInterface(a: CharArray, b: CharArray, c: CharArray?,
            d: CharArray?, e: CharArray, f: Char, sv: Msv): Unit =
        doc.ServiceInterface(a, b, c, d, e, f, sv)
    // seat RESOLVED by unit 215 (B7 T3, interp-service.cpp::ServiceInterface).

    // unsigned char *IDF(struct MSV *, unsigned char *, unsigned char *,
    // unsigned char *); owner libmpscpp.cpp:10370 free IDF wrapper
    // (NOT the unit-49 GlobalIDF member; the global::IDF member twin
    // stays scoped) - declared by the unit-215 ServiceInterface INVDF
    // arm; H-014 seat for the batch-10 misc chunk.
    fun IDF(sv: Msv, ep: String, a: CharArray, b: CharArray): String =
        throw NotImplementedError("IDF: out-of-scope owner (libmpscpp.cpp:10370 free wrapper) batch-10 unit")

    // ------------------------------------------------------------------
    // unit 112 (libmpscpp.cpp::global::Centroid) one-subscript double
    // seats (the keyed global double operator family; vararg keys
    // cover the B(k) and B(k1,k2) invoke shapes)

    // (double) B(keys...) - double conversion of an invoked global;
    // owner: libmpscpp.cpp subscripted operator double family; not
    // a roster unit.
    fun getGlobalDoubleKeyed(g: GlobalRef, vararg keys: String): Double =
        throw NotImplementedError("global keyed double conversion: out-of-scope owner (libmpscpp.cpp subscripted operator family)")

    // B(keys...) = value - double assignment to an invoked global;
    // same owner family; not a roster unit.
    fun setGlobalDoubleKeyed(g: GlobalRef, v: Double, vararg keys: String): Unit =
        throw NotImplementedError("global keyed double assignment: out-of-scope owner (libmpscpp.cpp subscripted operator family)")
    // ------------------------------------------------------------------
    // unit 117 (compiler-doty.c::doty) H-014 seat

    // int LevCheck(short ExecName, short DoRetX, short DoRet[]);
    // owner compiler-mumps.c:1949 (the body's file); not a roster
    // unit.
    fun LevCheck(ExecName: Short, DoRetX: Short, DoRet: ShortArray): Int =
        throw NotImplementedError("LevCheck: out-of-scope owner (compiler-mumps.c:1949)")

    // ------------------------------------------------------------------
    // unit 119 (interp-sym.cpp::GlobalQuery) H-014 seat

    // struct MSV * AllocSV(void); owner
    // interp-sysfunc.cpp::AllocSV - CORRECTLY TAGGED batch-8 roster
    // row 219 (the state-vector push of the query walkers).
    fun AllocSV(): Msv = doc.AllocSV()

    // ------------------------------------------------------------------
    // unit 120 (libmpscpp.cpp::<misc-9>) H-014 seats: the MSV-intrinsic
    // wrapper call-outs of the chunk. Corpus-unit owners are named
    // with their batch; seats flip to delegates when those land. The
    // two class-global members reached through keyed invoke seats are
    // NOT stubbed again here (the unit-120 wrappers keep calling the
    // existing real renders: Cosine(A,B) double, the GlobalRef
    // globalTermCorrelate member, and the GlobalRef Centroid member).

    // double Sim1(global a, global b); owner
    // libmpscpp.cpp::Sim1 - batch-7 roster row 199 of this seat's
    // OLD numbering (the unit-204 render of tranche 2; MDH seat
    // numbering kept in place per the u130 tag precedent). The
    // wrapper compares two document vectors; the double returns
    // through the wrapper's sprintf percent-g render unchanged.
    // RESOLVED with unit 204 (B7 tranche 2): delegates to the real
    // Sim1.kt render; the SymOpsMisc.kt unit-120 wrapper call
    // sites bind unchanged.
    fun sim1(a: GlobalRef, b: GlobalRef): Double =
        doc.Sim1(a, b)

    // double Jaccard(global a, global b); owner
    // libmpscpp.cpp::Jaccard - batch-9 roster row 256. B9 T3
    // FLIP (C-008 consult 2026-11-22T05:38:00Z, H-015
    // hit-adapt): the unit-276 render landed the body
    // (Jaccard.kt, the Sim1.kt two-walk template twin); the
    // unit-120 wrapper call sites bind unchanged.
    fun jaccard(a: GlobalRef, b: GlobalRef): Double =
        doc.Jaccard(a, b)

    // double Dice(global a, global b); owner
    // libmpscpp.cpp::Dice - batch-6 roster row 166 (MDH numbering).
    fun dice(a: GlobalRef, b: GlobalRef): Double = Dice(a, b)

    // void global::DocCorrelate(global B, char * fcn, double
    // threshold); owner libmpscpp.cpp::global::DocCorrelate - batch-10
    // roster row 288 (the string twin carries the working body; the
    // char* / mstring twins landed with the batch-5 unit 154 render
    // and delegate into this seat - front-run ledger, seat NOT
    // flipped to avoid the delegation circle).
    fun globalDocCorrelate(
        a: GlobalRef,
        b: GlobalRef,
        fcn: String,
        threshold: Double
    ): Unit =
        throw NotImplementedError("global::DocCorrelate: out-of-scope owner (libmpscpp.cpp::global::DocCorrelate) batch-10 unit")

    // void StopInit(char * file); owner libmpscpp.cpp::StopInit -
    // batch-6 roster row 175 (the stopword table loader). Resolved
    // with unit 166 (B6 tranche 2): delegates to the real StopInit.kt
    // render, which loads the mutable stopWords carrier.
    fun stopInit(file: String): Unit = doc.stopInit(file)

    // int StopLookup(char * str); owner libmpscpp.cpp:7777 - not a
    // corpus unit (the stopword membership test).
    fun stopLookup(str: String): Int =
        throw NotImplementedError("StopLookup: out-of-scope owner (libmpscpp.cpp:7777 stopword table)")

    // int SynInit(char * file); owner libmpscpp.cpp::SynInit -
    // batch-5 roster row 133; RESOLVED by the batch-5 unit 146
    // render (SynInit.kt).
    fun synInit(file: String): Int =
        doc.synInit(file)

    // char * SYN(char * word); owner libmpscpp.cpp:7788 - not a
    // corpus unit (the synonym lookup).
    fun SYN(word: String): String =
        throw NotImplementedError("SYN: out-of-scope owner (libmpscpp.cpp:7788 synonym table)")

    // int bmg_fullsearch(char * p, char * inp); owner
    // interp-bmgsubs.c:116 - not a corpus unit. The free C search of
    // the full file mode; unit-86 bmg_fsearch (BmgFsearch.kt) is the
    // RELATED but DISTINCT keyed callback entry and stays separate.
    fun bmgFullSearch(p: String, inp: String): Int =
        throw NotImplementedError("bmg_fullsearch: out-of-scope owner (interp-bmgsubs.c:116 full-file twin)")

    // char * Shred(char * str, int size); owner
    // libmpscpp.cpp::Shred - batch-6 roster row 165.
    fun shred(str: String, size: Int): String =
        throw NotImplementedError("Shred: out-of-scope owner (libmpscpp.cpp::Shred) batch-6 unit")

    // char * ShredQuery(char * str, int size); owner
    // libmpscpp.cpp:7970 - not a corpus unit (the shred query twin).
    fun shredQuery(str: String, size: Int): String =
        throw NotImplementedError("ShredQuery: out-of-scope owner (libmpscpp.cpp:7970)")

    // ------------------------------------------------------------------
    // unit 121 (compiler-set.c::set_code) H-014 seat

    // int cvar(char * str); owner compiler-parse.c:2074 (the
    // named-variable test of the LHS store) - not a corpus unit
    // (no units.csv row). H-017: the snake name kept verbatim.
    fun cvar(str: String): Int = doc.cvar(str)

    // ------------------------------------------------------------------
    // unit 123 (B4 tranche 3 tail) H-014 seats: the fcn dispatcher
    // call-outs of the batch-4 head unit (Fcn.kt).

    // int ScanParse(char *); owner interp-sym.cpp::ScanParse
    // (batch-8 roster row 234, interp-sym.cpp:81 prototype,
    // :1410 body). The call site passes the xd offset slice.
    // Unit 234 (B8 tranche 2) RESOLVES this seat: delegate per
    // the H-014 Extract2 precedent; the parse render binds
    // unchanged.
    fun ScanParse(s: CharArray): Int =
        doc.ScanParse(s)

    // int Eval(unsigned char *, unsigned char *, struct MSV *);
    // owner interp-sym.cpp::Eval (batch-9 roster row 261,
    // interp-sym.cpp:82 prototype, :1192 body). The vendor
    // in-place aliasing of the two \u0024qlength/\u0024qsubscript
    // operand carriers is kept by the call site passing the same
    // array twice (H-014 alias ledger). B9 T2 FLIP: the unit-260
    // render landed (Eval.kt) - this seat now DELEGATES.
    fun Eval(a: CharArray, b: CharArray, sv: Msv): Int =
        doc.Eval(a, b, sv)

    // inline void GlobalOrder(unsigned char *, unsigned char *,
    // unsigned char *, struct MSV *); globalOrder.h:8 - the
    // FOUR-ARG interpreter inline is the batch-9 roster row 266
    // (include/mumpsc/globalOrder.h; ints distinct from the landed
    // unit-39/69 libmpscpp GlobalOrder renders per the same-name
    // H-015 overload-family rule). tmp2 is the direction carrier
    // ("1" / "-1" literals of the \u0024order walk). B9 T2 FLIP:
    // the u265 render landed (GlobalOrderH.kt) - this seat now
    // DELEGATES; the tmp2 carrier widens NULLABLE (String?) per
    // the vendor NULL direction-argument arm (call sites all pass
    // non-null, so the widening is source-compatible).
    fun GlobalOrder(tmp0: CharArray, tmp1: CharArray, tmp2: String?,
            svPtr: Msv): Unit =
        doc.GlobalOrder(tmp0, tmp1, tmp2, svPtr)

    // int strfmon(char *s, size_t max, const char *format, double);
    // the POSIX monetary.h libc entry (no roster unit can own it -
    // the OS libc owner, the read(2) seat precedent). Both fcn
    // call sites (the \u0024fnumber str format arms) pass the
    // format String and read the dst carrier back by the slice.
    fun strfmon(dst: CharArray, max: Int, format: String, x: Double): Int =
        throw NotImplementedError("strfmon: out-of-scope owner (POSIX monetary.h libc)")
    // ------------------------------------------------------------------
    // unit 183 (batch-6 big three) seat raises: the zcn GTK arms and
    // the mps floor/ceil/trunc/log/cos/tan/exp2 arms of
    // interp-bifs.cpp::zfcn. The GTK seats are owned by the GTK C
    // library glue (NOT corpus units); the mps seats are owned by the
    // batch-9/-10 interp-arith.cpp roster units (out of current
    // batch scope). The same-family landed seats mpsLog2/ mpsLog10/
    // mpsExp/ mpsExp10/ mpsAbs/ mpsSin/ mps_asin/ mps_acos/ mps_atan/
    // mps_sqrt are the real unit renders and are called directly.
    // ------------------------------------------------------------------

    fun gtkDialogNewWithButtons(title: String, parent: String?, flags: Int,
            buttons: kotlin.Array<kotlin.Pair<String, Int>>): String =
        throw NotImplementedError("gtk_dialog_new_with_buttons: out-of-scope owner (GTK library glue)")

    fun gtkWindowSetKeepAbove(w: String, b: Boolean): Unit =
        throw NotImplementedError("gtk_window_set_keep_above: out-of-scope owner (GTK library glue)")

    fun gtkDialogRun(d: String): Int =
        throw NotImplementedError("gtk_dialog_run: out-of-scope owner (GTK library glue)")

    fun gtkWidgetDestroy(w: String): Unit =
        throw NotImplementedError("gtk_widget_destroy: out-of-scope owner (GTK library glue)")

    fun gtkWidgetHide(w: String): Unit =
        throw NotImplementedError("gtk_widget_hide: out-of-scope owner (GTK library glue)")

    fun gtkWidgetShow(w: String): Unit =
        throw NotImplementedError("gtk_widget_show: out-of-scope owner (GTK library glue)")

    fun gtkSpinButtonGetValue(spin: String?): Double =
        throw NotImplementedError("gtk_spin_button_get_value: out-of-scope owner (GTK library glue)")

    fun gtkSpinButtonSetValue(spin: String?, v: Double): Unit =
        throw NotImplementedError("gtk_spin_button_set_value: out-of-scope owner (GTK library glue)")

    fun gtkTreeStoreClear(t: String?): Unit =
        throw NotImplementedError("gtk_tree_store_clear: out-of-scope owner (GTK library glue)")

    fun gtkTreeModelGet(m: String?, iter: IntArray, col: Int): String =
        throw NotImplementedError("gtk_tree_model_get: out-of-scope owner (GTK library glue)")

    fun gtkTreeSelectionGetSelected(s: String?, m: StrHolderZb,
            iter: IntArray): Boolean =
        throw NotImplementedError("gtk_tree_selection_get_selected: out-of-scope owner (GTK library glue)")

    fun gtkTreeViewGetSelection(t: String?): String =
        throw NotImplementedError("gtk_tree_view_get_selection: out-of-scope owner (GTK library glue)")

    fun gtkLabelSetText(l: String, s: String): Unit =
        throw NotImplementedError("gtk_label_set_text: out-of-scope owner (GTK library glue)")

    fun gtkTextBufferSetText(b: String, s: String, n: Int): Unit =
        throw NotImplementedError("gtk_text_buffer_set_text: out-of-scope owner (GTK library glue)")

    fun gtkEntryGetText(e: String): String =
        throw NotImplementedError("gtk_entry_get_text: out-of-scope owner (GTK library glue)")

    fun gtkEntrySetText(e: String, s: String): Unit =
        throw NotImplementedError("gtk_entry_set_text: out-of-scope owner (GTK library glue)")

    fun gtkRangeGetAdjustment(e: String?): String =
        throw NotImplementedError("gtk_range_get_adjustment: out-of-scope owner (GTK library glue)")

    fun gtkAdjustmentGetValue(a: String?): Double =
        throw NotImplementedError("gtk_adjustment_get_value: out-of-scope owner (GTK library glue)")

    fun gtkFileChooserGetFilename(e: String?): String =
        throw NotImplementedError("gtk_file_chooser_get_filename: out-of-scope owner (GTK library glue)")

    fun gtkCalendarGetDate(e: String?, yr: IntArray, mn: IntArray,
            dy: IntArray): Unit =
        throw NotImplementedError("gtk_calendar_get_date: out-of-scope owner (GTK library glue)")

    fun gtkFontChooserGetFont(e: String?): String =
        throw NotImplementedError("gtk_font_chooser_get_font: out-of-scope owner (GTK library glue)")

    fun gtkColorChooserGetRgba(c: String?, r: GdkRgbaZb): Unit =
        throw NotImplementedError("gtk_color_chooser_get_rgba: out-of-scope owner (GTK library glue)")

    fun gtkToggleButtonSetActive(t: String?, b: Boolean): Unit =
        throw NotImplementedError("gtk_toggle_button_set_active: out-of-scope owner (GTK library glue)")

    fun gtkToggleButtonGetActive(t: String?): Boolean =
        throw NotImplementedError("gtk_toggle_button_get_active: out-of-scope owner (GTK library glue)")

    fun gtkSwitchGetActive(t: String?): Boolean =
        throw NotImplementedError("gtk_switch_get_active: out-of-scope owner (GTK library glue)")

    // captured rule (unit-83 era repair). BATCH-9 FLIP (B9 T1):
    // mpsLog/mpsCos/mpsTan/mpsExp2 now DELEGATE to the unit
    // 249/250/251/247 renders (this tranche); mpsFloor flips at
    // the batch-9 core tranche (units.csv row 268), mpsCeil/
    // mpsTrunc stay throwing (batch-10 roster rows). B9 T2 FLIP:
    // the unit-267 render landed (MpsFloor.kt) - mpsFloor now
    // DELEGATES; mpsCeil/mpsTrunc stay throwing (batch-10 rows).
    fun mpsFloor(bd: CharArray, in_: String): Unit =
        doc.mpsFloor(bd, in_)

    fun mpsCeil(bd: CharArray, in_: String): Unit =
        throw NotImplementedError("mps_ceil: out-of-scope owner (interp-arith.cpp batch-10 roster unit)")

    fun mpsTrunc(bd: CharArray, in_: String): Unit =
        throw NotImplementedError("mps_trunc: out-of-scope owner (interp-arith.cpp batch-10 roster unit)")

    fun mpsLog(bd: CharArray, in_: String): Unit =
        doc.mpsLog(bd, in_)

    fun mpsCos(bd: CharArray, in_: String): Unit =
        doc.mpsCos(bd, in_)

    fun mpsTan(bd: CharArray, in_: String): Unit =
        doc.mpsTan(bd, in_)

    fun mpsExp2(bd: CharArray, in_: String): Unit =
        doc.mpsExp2(bd, in_)

    // char *Replace(struct MSV *, const char *, const char *, const
    // char *, const char *) declared builtin.h:40, API forward of
    // the zReplace entry; NOT a corpus unit (builtin API seat owner,
    // the Replace machinery of libmpscpp; raised per the unit-183
    // zreplace-arm ledger).
    fun Replace(sv: Msv, ep: String, arg1: String, arg2: String,
            arg3: String): String =
        throw NotImplementedError("Replace: out-of-scope owner (builtin.h:40 API entry, not a corpus unit)")

    // H-014 seats raised in the B9 tranche-3 unit-269 render
    // (LibmpscppMisc8.kt): the mstring binary backends operator+/
    // -/*///% owned by libmpscpp.cpp (batch-10 roster rows per the
    // MstringClassMisc.kt header ledger). SEATS STAY THROWING until
    // the owner rows land (the unit-154 globalDocCorrelate front-run
    // precedent); the +=/-=/*=///=/%= assign families of unit 269
    // route through them by signature. The char* twins fold onto
    // the String seat (the mstring.lt ladder precedent, H-015).
    fun mstringPlus(a: Mstring, b: Mstring): Mstring =
        throw NotImplementedError("mstringPlus: out-of-scope owner (libmpscpp.cpp operator+ backend, batch-10 roster row)")
    fun mstringPlus(a: Mstring, b: Int): Mstring =
        throw NotImplementedError("mstringPlus: out-of-scope owner (libmpscpp.cpp operator+ backend, batch-10 roster row)")
    fun mstringPlus(a: Mstring, b: Long): Mstring =
        throw NotImplementedError("mstringPlus: out-of-scope owner (libmpscpp.cpp operator+ backend, batch-10 roster row)")
    fun mstringPlus(a: Mstring, b: String): Mstring =
        throw NotImplementedError("mstringPlus: out-of-scope owner (libmpscpp.cpp operator+ backend, batch-10 roster row)")
    fun mstringPlus(a: Mstring, b: GlobalRef): Mstring =
        throw NotImplementedError("mstringPlus: out-of-scope owner (libmpscpp.cpp operator+ backend, batch-10 roster row)")
    fun mstringPlus(a: Mstring, b: Double): Mstring =
        throw NotImplementedError("mstringPlus: out-of-scope owner (libmpscpp.cpp operator+ backend, batch-10 roster row)")
    fun mstringPlus(a: Mstring, b: Float): Mstring =
        throw NotImplementedError("mstringPlus: out-of-scope owner (libmpscpp.cpp operator+ backend, batch-10 roster row)")

    fun mstringMinus(a: Mstring, b: Mstring): Mstring =
        throw NotImplementedError("mstringMinus: out-of-scope owner (libmpscpp.cpp operator- backend, batch-10 roster row)")
    fun mstringMinus(a: Mstring, b: Int): Mstring =
        throw NotImplementedError("mstringMinus: out-of-scope owner (libmpscpp.cpp operator- backend, batch-10 roster row)")
    fun mstringMinus(a: Mstring, b: Long): Mstring =
        throw NotImplementedError("mstringMinus: out-of-scope owner (libmpscpp.cpp operator- backend, batch-10 roster row)")
    fun mstringMinus(a: Mstring, b: String): Mstring =
        throw NotImplementedError("mstringMinus: out-of-scope owner (libmpscpp.cpp operator- backend, batch-10 roster row)")
    fun mstringMinus(a: Mstring, b: GlobalRef): Mstring =
        throw NotImplementedError("mstringMinus: out-of-scope owner (libmpscpp.cpp operator- backend, batch-10 roster row)")
    fun mstringMinus(a: Mstring, b: Double): Mstring =
        throw NotImplementedError("mstringMinus: out-of-scope owner (libmpscpp.cpp operator- backend, batch-10 roster row)")
    fun mstringMinus(a: Mstring, b: Float): Mstring =
        throw NotImplementedError("mstringMinus: out-of-scope owner (libmpscpp.cpp operator- backend, batch-10 roster row)")

    fun mstringTimes(a: Mstring, b: Mstring): Mstring =
        throw NotImplementedError("mstringTimes: out-of-scope owner (libmpscpp.cpp operator* backend, batch-10 roster row)")
    fun mstringTimes(a: Mstring, b: Int): Mstring =
        throw NotImplementedError("mstringTimes: out-of-scope owner (libmpscpp.cpp operator* backend, batch-10 roster row)")
    fun mstringTimes(a: Mstring, b: Long): Mstring =
        throw NotImplementedError("mstringTimes: out-of-scope owner (libmpscpp.cpp operator* backend, batch-10 roster row)")
    fun mstringTimes(a: Mstring, b: String): Mstring =
        throw NotImplementedError("mstringTimes: out-of-scope owner (libmpscpp.cpp operator* backend, batch-10 roster row)")
    fun mstringTimes(a: Mstring, b: GlobalRef): Mstring =
        throw NotImplementedError("mstringTimes: out-of-scope owner (libmpscpp.cpp operator* backend, batch-10 roster row)")
    fun mstringTimes(a: Mstring, b: Double): Mstring =
        throw NotImplementedError("mstringTimes: out-of-scope owner (libmpscpp.cpp operator* backend, batch-10 roster row)")
    fun mstringTimes(a: Mstring, b: Float): Mstring =
        throw NotImplementedError("mstringTimes: out-of-scope owner (libmpscpp.cpp operator* backend, batch-10 roster row)")

    fun mstringDiv(a: Mstring, b: Mstring): Mstring =
        throw NotImplementedError("mstringDiv: out-of-scope owner (libmpscpp.cpp operator/ backend, batch-10 roster row)")
    fun mstringDiv(a: Mstring, b: Int): Mstring =
        throw NotImplementedError("mstringDiv: out-of-scope owner (libmpscpp.cpp operator/ backend, batch-10 roster row)")
    fun mstringDiv(a: Mstring, b: Long): Mstring =
        throw NotImplementedError("mstringDiv: out-of-scope owner (libmpscpp.cpp operator/ backend, batch-10 roster row)")
    fun mstringDiv(a: Mstring, b: String): Mstring =
        throw NotImplementedError("mstringDiv: out-of-scope owner (libmpscpp.cpp operator/ backend, batch-10 roster row)")
    fun mstringDiv(a: Mstring, b: GlobalRef): Mstring =
        throw NotImplementedError("mstringDiv: out-of-scope owner (libmpscpp.cpp operator/ backend, batch-10 roster row)")
    fun mstringDiv(a: Mstring, b: Double): Mstring =
        throw NotImplementedError("mstringDiv: out-of-scope owner (libmpscpp.cpp operator/ backend, batch-10 roster row)")
    fun mstringDiv(a: Mstring, b: Float): Mstring =
        throw NotImplementedError("mstringDiv: out-of-scope owner (libmpscpp.cpp operator/ backend, batch-10 roster row)")

    fun mstringRem(a: Mstring, b: Mstring): Mstring =
        throw NotImplementedError("mstringRem: out-of-scope owner (libmpscpp.cpp operator% backend, batch-10 roster row)")
    fun mstringRem(a: Mstring, b: Int): Mstring =
        throw NotImplementedError("mstringRem: out-of-scope owner (libmpscpp.cpp operator% backend, batch-10 roster row)")
    fun mstringRem(a: Mstring, b: Long): Mstring =
        throw NotImplementedError("mstringRem: out-of-scope owner (libmpscpp.cpp operator% backend, batch-10 roster row)")
    fun mstringRem(a: Mstring, b: String): Mstring =
        throw NotImplementedError("mstringRem: out-of-scope owner (libmpscpp.cpp operator% backend, batch-10 roster row)")
    fun mstringRem(a: Mstring, b: GlobalRef): Mstring =
        throw NotImplementedError("mstringRem: out-of-scope owner (libmpscpp.cpp operator% backend, batch-10 roster row)")
}

// vendor gchar* / GtkTreeModel* out-param holders for the unit-183
// GTK seats (the C pointer out-arg holder idiom, H-006)
class StrHolderZb(var value: String? = null)

// vendor struct GdkRGBA (the gtk_color_chooser_get_rgba out-param)
class GdkRgbaZb {
    var red = 0.0
    var green = 0.0
    var blue = 0.0
    var alpha = 0.0
}
