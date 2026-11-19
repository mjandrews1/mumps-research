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
    // OVERLOAD of the seat above (same-name resolution by arity/types);
    // owner interp-arith.cpp:624 (add-family multiply, batch-7 land);
    // unit 93 calls it with whole-array seats so its pointer-shift
    // shifts are zero in this render (H-014 ledger note).
    fun mult(a: CharArray, b: CharArray, c: CharArray): Unit =
        throw NotImplementedError("mult: out-of-scope owner (interp-arith.cpp::mult)")

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
    fun atTest(xd: CharArray, pd1: CharArray, pd1Last: ShortArray,
               lines: Int, xpx: Long): Int =
        throw NotImplementedError("atTest: out-of-scope owner (compiler-at-test.c::atTest) batch-4 unit")

    // TODO stub: atCopy (compiler-globals.h:104 declaration; no
    // definition is a roster unit).
    fun atCopy(at_flg: Int, tmp: CharArray): Unit =
        throw NotImplementedError("atCopy: out-of-scope owner (compiler-at-test.c helper)")

    // TODO stub: parse_() TAG CORRECTION (u160-Interpret mis-tag family
    // precedent): the zero-arg seat is the compiler twin
    // compiler-parse.c::parse_ (batch-9 unit 302, signature
    // int parse_()); the one-arg interp twin renders as unit 93.
    // IfCode.kt binds this zero-arg seat at its
    // CompilerState.ierr = Stubs.parse_() call site.
    fun parse_(): Int =
        throw NotImplementedError("parse_: out-of-scope owner (compiler-parse.c::parse_) batch-9 unit")

    // H-014 delegate (Extract2 seat-conversion precedent): the interp
    // twin int parse_(struct MSV *) is unit 93, rendered in Parse_.kt;
    // this delegate converts the former placeholder seat into a
    // same-name overload resolved by arity at call sites.
    fun parse_(svPtr: Msv): Int = doc.parse_(svPtr)

    // TODO stub: linepad owned by compiler-sys1.c::linepad (loc 5, below
    // roster min loc - not a corpus unit).
    fun linepad(n: Int, s: String): Unit =
        throw NotImplementedError("linepad: out-of-scope owner (compiler-sys1.c::linepad)")

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

    // void init(void) - matrix initializer; not a roster unit.
    fun init(): Unit =
        throw NotImplementedError("init: out-of-scope owner (libmpscpp.cpp alignment initializer)")

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

    // TODO stub: epi(char *, int) declared compiler-abrtx.c family;
    // owner is the compiler epilogue emitter; not a roster unit.
    fun epi(name: String, pid: Int): Unit =
        throw NotImplementedError("epi: out-of-scope owner (compiler epilogue emitter)")

    // TODO stub: EP(int, char *, int) declared compiler-abrtx.c
    // family; owner is the compiler Zfunction epilogue emitter; not
    // a roster unit.
    fun EP(x: Int, name: String, pid: Int): Unit =
        throw NotImplementedError("EP: out-of-scope owner (compiler Zfunction epilogue emitter)")

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

    // void lpad(unsigned char *, unsigned char *, short)
    fun lpad(a: CharArray, b: CharArray, sh: Short): Unit =
        throw NotImplementedError("lpad: out-of-scope owner (interp pad helper)")

    // void substr(unsigned char *, unsigned char *, int, int)
    fun substr(a: CharArray, b: CharArray, i: Int, j: Int): Unit =
        throw NotImplementedError("substr: out-of-scope owner (interp substr helper)")

    // void LocalName(unsigned char *, unsigned char *, unsigned
    // char *, MSV *); owner interp-sysfunc.cpp::LocalName (not a
    // roster unit).
    fun LocalName(a: CharArray, b: CharArray, c: CharArray, sv: Msv): Unit =
        throw NotImplementedError("LocalName: out-of-scope owner (interp-sysfunc.cpp::LocalName)")

    // void GlobalQuery(unsigned char *, unsigned char *, char,
    // MSV *); not a roster unit.
    fun GlobalQuery(a: CharArray, b: CharArray, ch: Char, sv: Msv): Unit =
        throw NotImplementedError("GlobalQuery: out-of-scope owner (interp query walker)")

    // void Qsub(unsigned char *, unsigned char *, unsigned char
    // *, MSV *); not a roster unit.
    fun Qsub(a: CharArray, b: CharArray, c: CharArray, sv: Msv): Unit =
        throw NotImplementedError("Qsub: out-of-scope owner (interp Qsub helper)")

    // void Qlength(unsigned char *, unsigned char *, MSV *);
    fun Qlength(a: CharArray, b: CharArray, sv: Msv): Unit =
        throw NotImplementedError("Qlength: out-of-scope owner (interp Qlength helper)")

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

    // int xindex(const unsigned char *, const unsigned char *,
    // short); not a roster unit.
    fun xindex(a: CharArray, b: CharArray, sh: Short): Int =
        throw NotImplementedError("xindex: out-of-scope owner (interp xindex helper)")

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
    // subprocess (not a roster unit).
    fun sql(a: Int, sv: Msv, dsn: CharArray, command: CharArray,
            e: CharArray, tail: String): Int =
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
    fun match(pts: Array<ShortArray>, lx: Int, ct: ShortArray,
              ad: CharArray, rslt: ShortArray): Unit =
        throw NotImplementedError("match: out-of-scope owner (compiler-sys1.c::match) batch-7 unit")

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
    fun set_code(entry_pt: Int): Int =
        throw NotImplementedError("set_code: out-of-scope owner (compiler-set.c::set_code) batch-4 unit")

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
    // unsubscripted/subscripted reference (owner libmpscpp.cpp:1651
    // global::Max) is not a corpus unit; owner-named seat raised by
    // unit 68 (sibling of global::Min, unit 44's real render).
    fun globalMax(g: GlobalRef): Double =
        throw NotImplementedError("globalMax: out-of-scope class member (libmpscpp.cpp::global::Max, not a corpus unit)")

    // ------------------------------------------------------------------
    // unit 31 carrier seats (H-014 stubs; owners below the roster or
    // outside the pre-registered chunks):

    // char *Extract1(char *); owner is compiler-parse.c:2119
    // (below the roster cut); mirror of Extract2 (unit 15 owner).
    fun Extract1(buf: CharArray): CharArray =
        throw NotImplementedError("Extract1: out-of-scope owner (compiler-parse.c:2119, below the roster cut)")

    // unsigned long lhash(char *); owner is libmpscpp.h:273 (hash
    // driver below the roster). Returns unsigned long (H-012); the
    // Long seat carries the domain, the sign bit is never set.
    fun lhash(s: String): Long = 0L

    // void global::MakeGlobal(int, const char *, const char *, ...);
    // the class-global member builds the subscripted MUMPS reference
    // (owner libmpscpp.cpp global::MakeGlobal, outside the unit 31
    // chunk). fmt is the a,b,c... letter list, args the subscripts.
    fun makeGlobal(g: GlobalRef, mode: Int, fmt: String,
            vararg args: String): Unit =
        throw NotImplementedError("makeGlobal: out-of-scope owner (libmpscpp.cpp::global::MakeGlobal)")

    // ---- seats raised by unit 93 (interp-parse.cpp::parse_) ----

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
        throw NotImplementedError("rpm: out-of-scope owner (interp-rpm.cpp::rpm)")

    // TODO stub: BuildGlobal (batch-8 unit 150,
    // interp-parse.cpp:1836); rslt nullable because unit 93 passes
    // NULL for the result pointer in one arm.
    fun BuildGlobal(code: Int, g: Int, str: CharArray,
            rslt: CharArray?, svPtr: Msv): Int =
        throw NotImplementedError("BuildGlobal: out-of-scope owner (interp-parse.cpp::BuildGlobal) batch-8 unit")

    // TODO stub: BuildLocal (batch-8 unit 152, interp-sym.cpp:97),
    // same shape as BuildGlobal.
    fun BuildLocal(code: Int, g: Int, str: CharArray,
            rslt: CharArray?, svPtr: Msv): Int =
        throw NotImplementedError("BuildLocal: out-of-scope owner (interp-sym.cpp::BuildLocal) batch-8 unit")
    // ------------------------------------------------------------------
    // unit 98 (B4 tranche 1): the class-global member Count over the
    // unsubscripted/subscripted reference (owner libmpscpp.cpp:2257
    // global::Count) is not a corpus unit; owner-named seat raised by
    // unit 98 (sibling of globalMax unit 68; note the REAL unit-44
    // GlobalRef.Min render needs no seat).
    fun globalCount(g: GlobalRef): Double =
        throw NotImplementedError("globalCount: out-of-scope class member (libmpscpp.cpp::global::Count, not a corpus unit)")
}
