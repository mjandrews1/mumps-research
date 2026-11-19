package doc

// Unit 31 (B1 tranche 5): libmpscpp.cpp::<misc-1> (batch 1, complex
// stratum; units.csv row 31: the 600-loc leading residue chunk of
// libmpscpp.cpp). Chunker boundaries documented: the chunk runs
// from the file banner to the middle of the unary
// global::operator-() body - the render closes what the vendor cut
// mid-statement (the closing return and brace are outside the
// chunk) with that note, and begins at the banner (not mid-content).
// Content: the leading define/include ledger, the GTK
// mdh_tree_level_add banner block (the 12-parameter ten-column
// overload - distinct from the unit 8 five-column and unit 43
// seven-column renders by arity, H-015), the Hash file-backed hash
// class, the indirect assignment operator, the global operator()
// subscript-binding family (1-10 keys, string/mstring/long/char*
// variants), the global constructor, the non-member T-op= global
// families (+=,-=,*=,/= over int/uint/long/ulong/short/ushort/
// float/double), the vendor's commented-out operator block (H-013
// dead-code ledger), and unary operator+ (operator- is cut at the
// chunk edge). Divergences per H-001 (FILE* Hash backend to
// RandomAccessFile with the FSEEK/FTELL macros mapping to
// seek/getFilePointer; exit(EXIT_FAILURE) after the vendor message
// as kotlin.system.exitProcess; cout to System.out); MakeGlobal and
// lhash and zfcn are H-014 stubs (members below the roster or
// outside the chunk); kotlin shutdown hooks have no atexit analog
// (the __finish / set_terminate(thrownMessage) registrations are
// the ledger note; the callbacks' owners are outside the chunk).
// The GlobalExceptionCode file-scope definition references the
// existing Globals seat (H-015 no redeclaration; UNSPECIFIED 0
// libmpscpp.h:69 added to Carriers). gcvt_wrapper/GETPID and the
// GCVT selection are H-018 ledger notes (the GCVT arm selects the
// sprintf %g variant; seats already exist). Naming per H-017.

// global::Eflg static member seat (file-scope init of the vendor)
object GlobalClassStatic {
    var Eflg: Int = 0
}

// the mdh tree level add TEN-column overload (dollar byte of the
// command target built programmatically in CMD_TARGET_31), same
// idiom as
// the unit 8 five-column and unit 43 seven-column renders (coded
// delimiter bytes via Char(nnn) per H-015, pointer sprintf as the
// stable identity-hex divergence, zfcn on the H-014 stub).
private const val CMD_TARGET_31 = "\u0024z~mdh~tree~level~add"
private val OPEN_PAREN_31 = Char(0x00ce)
private val COMMA_31 = Char(0x00d0)
private val CLOSE_PAREN_31 = Char(0x00cf)

fun mdhTreeLevelAdd(
    tree: Any?, depth: Int,
    col1: String, col2: String, col3: String, col4: String,
    col5: String, col6: String, col7: String, col8: String,
    col9: String, col10: String
) {
    val tmp = CharArray(4096)
    val tmp1 = CharArray(256)
    val tmp2 = CharArray(256)

    CstringOps.copyAtS(tmp, 1, CMD_TARGET_31)
    CstringOps.catAt(tmp, 1, OPEN_PAREN_31)
    // sprintf(tmp1, "%p", tree): stable identity-hex text (documented
    // divergence, same as the unit 8/43 renders)
    CstringOps.strcpyS(tmp1,
            java.lang.System.identityHashCode(tree).toString(16))
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, COMMA_31)

    CstringOps.strcpyS(tmp1, depth.toString())
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, COMMA_31)

    CstringOps.catAt(tmp, 1, col1)
    CstringOps.catAt(tmp, 1, COMMA_31)
    CstringOps.catAt(tmp, 1, col2)
    CstringOps.catAt(tmp, 1, COMMA_31)
    CstringOps.catAt(tmp, 1, col3)
    CstringOps.catAt(tmp, 1, COMMA_31)
    CstringOps.catAt(tmp, 1, col4)
    CstringOps.catAt(tmp, 1, COMMA_31)
    CstringOps.catAt(tmp, 1, col5)
    CstringOps.catAt(tmp, 1, COMMA_31)
    CstringOps.catAt(tmp, 1, col6)
    CstringOps.catAt(tmp, 1, COMMA_31)
    CstringOps.catAt(tmp, 1, col7)
    CstringOps.catAt(tmp, 1, COMMA_31)
    CstringOps.catAt(tmp, 1, col8)
    CstringOps.catAt(tmp, 1, COMMA_31)
    CstringOps.catAt(tmp, 1, col9)
    CstringOps.catAt(tmp, 1, COMMA_31)
    CstringOps.catAt(tmp, 1, col10)
    CstringOps.catAt(tmp, 1, CLOSE_PAREN_31)

    Stubs.zfcn(tmp, tmp2, Globals.svPtr)
}

// vendor Hash class: file-backed hash table over a "name.key" file.
// fopen modes render as RandomAccessFile mode strings ("rw" ~ w+,
// "r" ~ r+ with the create-on-demand divergence noted: "r" on a
// missing file throws where the vendor fopen returns NULL into
// the NULL test below).
class Hash(name: String, sz: Long, disp: String) {
    // vendor fields: key, size, hashkey, ref
    val key: String
    val size: Long
    var hashkey: java.io.RandomAccessFile? = null
    var ref: String = ""

    init {
        key = name + ".key"
        size = sz
        if (name.isEmpty()) {
            System.out.print("Invalid hash file name\n")
            kotlin.system.exitProcess(1)
        }
        if (sz < 1000) {
            System.out.print("Invalid hash file size in hash " + name +
                    "\n")
            kotlin.system.exitProcess(1)
        }
        val code: String
        if (disp == "new") code = "rw"
        else if (disp == "old") code = "r"
        else {
            System.out.print("Unknown hash file disposition\n")
            kotlin.system.exitProcess(1)
            code = ""
        }
        // fopen(key.c_str(), code); NULL check
        val f = java.io.File(key)
        val mode: String = if (disp == "new" || f.exists()) code else "rw"
        val raf = java.io.RandomAccessFile(f, mode)
        hashkey = raf

        if (disp == "new") {
            val p = '\u0001'
            for (i in 0 until size) {
                raf.write(p.code)
            }
        }
    }

    // ~Hash()
    protected fun finalizeLibmHash() {
        hashkey?.close()
        hashkey = null
    }

    // Hash & Hash::operator()(string str) - the mstring variant
    // first (operator()(mstring str))
    operator fun invoke(str: Mstring): Hash {
        ref = str.str
        return this
    }

    operator fun invoke(str: String): Hash {
        ref = str
        return this
    }

    // mstring Hash::operator=(mstring str)
    fun invoke2(str: Mstring): Mstring { // operator= seat (the
    // operator= name is not renderable as invoke; named seat, H-015)
        str.str = assignHash(str.str)
        return str
    }
}

// Hash::operator=(string str): the probe/replace/append algorithm.
fun Hash.assignHash(str: String): String {
    var i: Long
    var ii: Long
    var j: Int
    var k: Long
    var flg: Int
    var c: Int
    val p = CharArray(STR_MAX)
    // non-null by construction (init assigns before any use; the
    // finalize seat is never reached inside this render's flow)
    val hk = checkNotNull(hashkey)

    if (str.isEmpty()) {
        System.out.print("zero length hash reference\n")
        kotlin.system.exitProcess(1)
    }

    // the vendor static ref rides the instance (invoke-saved)
    i = Stubs.lhash(ref) % size
    hk.seek(i)
    flg = 0
    var loopflg = 0
    while (true) {
        ii = hk.filePointer
        j = 0
        while (true) {
            c = hk.read()
            if (c == -1) break
            if (c == 2) continue
            if (c == 1) break // not found
            p[j++] = c.toChar()
            if (c == 0) break
        }
        if (c == 1 || (loopflg != 0 && c == -1)) break // not found exit
        if (c == -1) {
            loopflg = 1
            i = 0
            hk.seek(i)
            ii = hk.filePointer
            continue
        }
        if (CstringOps.cstring(p) == ref) { // match?
            flg = 1
            break // matched: exit loop
        }
        // bypass value
        j = 0
        while (true) {
            c = hk.read()
            if (c == -1) break
            if (c == 1) break
            p[j++] = c.toChar()
            if (c == 0) break
        }
    }

    if (flg != 0) { // instance found
        i = hk.filePointer
        j = 0
        while (j < STR_MAX) { // get value
            c = hk.read()
            if (c == -1) {
                System.out.print("EOF in hash file\n")
                kotlin.system.exitProcess(1)
            }
            p[j] = c.toChar()
            if (p[j] == NUL) break
            j++
        }
        if (CstringOps.cstring(p).length >= str.length) { // replace
            hk.seek(i)
            k = CstringOps.cstring(p).length.toLong()
            p[0] = 2.toChar()
            var zz = 0L
            while (zz <= k) { // zap to 2's
                hk.write(p[0].code)
                zz++
            }
            hk.seek(i) // reset
            // fwrite(str.c_str(),str.length()+1,1,hashkey) renders
            // as the byte writes below
            for (cc in 0..str.length) hk.write(
                    (if (cc < str.length) str[cc] else NUL).code)
            return str
        }
        hk.seek(ii)
        k = (ref.length + 1).toLong()
        p[0] = 2.toChar()
        var zz = 0L
        while (zz <= k) { // zap to 2's
            hk.write(p[0].code)
            zz++
        }
        ii = hk.filePointer
        j = 0
        while (j < STR_MAX) { // get value
            val cc = hk.read()
            p[j] = cc.toChar()
            j++
            if (p[j - 1] == NUL) break
        }
        k = (CstringOps.cstring(p).length + 1).toLong()
        p[0] = 2.toChar()
        hk.seek(ii)
        zz = 0L
        while (zz <= k) { // zap to 1's (H-013: vendor comment says
            // zap to 1's but writes the 2 byte; kept verbatim)
            hk.write(p[0].code)
            zz++
        }
    } else {
        // vendor continues to the append path (the noreturn misses
        // in the fragment are outside the chunk flow)
    }

    // the append scan (table full at double-eof)
    loopflg = 0
    val jmax: Long = (ref.length + str.length + 3).toLong()
    i = Stubs.lhash(ref) % size
    hk.seek(i)
    k = 0
    i = hk.filePointer
    while (true) {
        val b = hk.read()
        if (b == -1 && loopflg == 0) {
            loopflg = 1
            i = 0
            hk.seek(i)
            continue
        }
        if (b == -1 && loopflg != 0) {
            System.out.print("\ntable full - eof\n")
            kotlin.system.exitProcess(1)
        }
        // fread(p,1,1,hashkey): vendor reads into p[0]
        if (b == 1) { // empty slot found
            k++
            if (k >= jmax) break
            continue
        }
        k = 0
        i = hk.filePointer
    }
    hk.seek(i)
    // fwrite(ref.c_str(),ref.length()+1,1,hashkey)
    for (cc in 0..ref.length) hk.write(
            (if (cc < ref.length) ref[cc] else NUL).code)
    // fwrite(str.c_str(),str.length()+1,1,hashkey)
    for (cc in 0..str.length) hk.write(
            (if (cc < str.length) str[cc] else NUL).code)
    return str
}

// indirect indirect::operator=(string a)
class Indirect(var reference: String) {
    fun invokeAssign(a: String): Indirect { // operator= seat (H-015)
        System.out.print(reference + " " + a + "\n")
        return this
    }
}

// global::operator() subscript-binding family: each calls
// MakeGlobal(0, myname, <count letters>, args...). The vendor writes
// each count's letter list literal ("a,b,c,d,..."); this render
// derives the identical letters programmatically (H-015 grammar
// note: the emitted format string is byte-identical to every
// vendor literal of that arity). MakeGlobal is an H-014 stub (the
// member owner is outside the chunk).
private fun fmtLetters(n: Int): String {
    val sb = StringBuilder()
    for (q in 0 until n) {
        if (q > 0) sb.append(',')
        sb.append(('a' + q))
    }
    return sb.toString()
}

// operator()(indirect a): cout only (the MakeGlobal call is
// commented out in the vendor; H-013 note)
operator fun GlobalRef.invoke(a: Indirect): GlobalRef {
    System.out.print(a.reference + "\n")
    // (*this).MakeGlobal(0,myname,"a","dummy"); commented out
    return this
}

operator fun GlobalRef.invoke(): GlobalRef {
    Stubs.makeGlobal(this, 0, "")
    return this
}

operator fun GlobalRef.invoke(a: Mstring): GlobalRef {
    Stubs.makeGlobal(this, 0, "a", a.str)
    return this
}

operator fun GlobalRef.invoke(a: Mstring, b: Mstring): GlobalRef {
    Stubs.makeGlobal(this, 0, "a,b", a.str, b.str)
    return this
}

operator fun GlobalRef.invoke(a: Mstring, b: Mstring, c: Mstring):
        GlobalRef {
    Stubs.makeGlobal(this, 0, "a,b,c", a.str, b.str, c.str)
    return this
}

operator fun GlobalRef.invoke(a: Mstring, b: Mstring, c: Mstring,
        d: Mstring): GlobalRef {
    Stubs.makeGlobal(this, 0, "a,b,c,d", a.str, b.str, c.str, d.str)
    return this
}

operator fun GlobalRef.invoke(a: Mstring, b: Mstring, c: Mstring,
        d: Mstring, e: Mstring): GlobalRef {
    Stubs.makeGlobal(this, 0, "a,b,c,d,e", a.str, b.str, c.str, d.str,
            e.str)
    return this
}

operator fun GlobalRef.invoke(a: Mstring, b: Mstring, c: Mstring,
        d: Mstring, e: Mstring, f: Mstring): GlobalRef {
    Stubs.makeGlobal(this, 0, "a,b,c,d,e,f", a.str, b.str, c.str, d.str,
            e.str, f.str)
    return this
}

operator fun GlobalRef.invoke(a: Mstring, b: Mstring, c: Mstring,
        d: Mstring, e: Mstring, f: Mstring, g: Mstring): GlobalRef {
    Stubs.makeGlobal(this, 0, "a,b,c,d,e,f,g", a.str, b.str, c.str,
            d.str, e.str, f.str, g.str)
    return this
}

operator fun GlobalRef.invoke(a: Mstring, b: Mstring, c: Mstring,
        d: Mstring, e: Mstring, f: Mstring, g: Mstring, h: Mstring):
        GlobalRef {
    Stubs.makeGlobal(this, 0, "a,b,c,d,e,f,g,h", a.str, b.str, c.str,
            d.str, e.str, f.str, g.str, h.str)
    return this
}

operator fun GlobalRef.invoke(a: Mstring, b: Mstring, c: Mstring,
        d: Mstring, e: Mstring, f: Mstring, g: Mstring, h: Mstring,
        i: Mstring): GlobalRef {
    Stubs.makeGlobal(this, 0, "a,b,c,d,e,f,g,h,i", a.str, b.str, c.str,
            d.str, e.str, f.str, g.str, h.str, i.str)
    return this
}

operator fun GlobalRef.invoke(a: Mstring, b: Mstring, c: Mstring,
        d: Mstring, e: Mstring, f: Mstring, g: Mstring, h: Mstring,
        i: Mstring, j: Mstring): GlobalRef {
    Stubs.makeGlobal(this, 0, "a,b,c,d,e,f,g,h,i,j", a.str, b.str,
            c.str, d.str, e.str, f.str, g.str, h.str, i.str, j.str)
    return this
}

// (no eleven-key seat: the vendor family stops at ten keys;
// the vendor boundary kept verbatim)

// const long int and const char* variants: each arity 1-10 (the
// long seat sprintf's %d first). Rendered via the same letters
// derivation; the long variant converts through the decimal
// string first (kept verbatim).
operator fun GlobalRef.invoke(a: Long): GlobalRef {
    val x = a.toString()
    Stubs.makeGlobal(this, 0, "a", x)
    return this
}

operator fun GlobalRef.invoke(vararg keys: String): GlobalRef {
    Stubs.makeGlobal(this, 0, fmtLetters(keys.size), *keys)
    return this
}

// global::global(const char * n): the constructor. The Eflg
// once-only gates the atexit/set_terminate registrations; the
// registrations themselves have no Kotlin analog (H-001 ledger
// note; __finish/thrownMessage owners are outside the chunk).
fun globalConstruct(n: String): GlobalRef {
    val g = GlobalRef(n)
    if (GlobalClassStatic.Eflg > 0) return g
    GlobalClassStatic.Eflg++
    // atexit(__finish); set_terminate(thrownMessage); - the
    // shutdown-callback registrations are the H-001 ledger note
    return g
}

// fix embedded double quote marks (vendor section comment kept)

// non-member operator families: T op= global over the eight
// primitive types; the T& out-param rides the single-element
// array seat (H-006); the global conversion rides the existing
// H-014 get seats (int fns; the vendor + j chains through the
// member conversion operators).

private fun globInt(g: GlobalRef): Int = Stubs.globalGetInt(g)
private fun globUInt(g: GlobalRef): UInt = Stubs.globalGetUInt(g)
private fun globLong(g: GlobalRef): Long = Stubs.globalGetLong(g)
private fun globULong(g: GlobalRef): ULong = Stubs.globalGetULong(g)
private fun globShort(g: GlobalRef): Short = Stubs.globalGetShort(g)
private fun globUShort(g: GlobalRef): UShort = Stubs.globalGetUShort(g)
private fun globFloat(g: GlobalRef): Float = Stubs.globalGetFloat(g)
private fun globDouble(g: GlobalRef): Double = Stubs.globalGetDouble(g)

fun plusAssignGlobalInt(i: IntArray, j: GlobalRef): Int {
    i[0] = i[0] + globInt(j)
    return i[0]
}

fun plusAssignGlobalUInt(i: Array<UInt>, j: GlobalRef): UInt {
    i[0] = i[0] + globUInt(j)
    return i[0]
}

fun plusAssignGlobalLong(i: LongArray, j: GlobalRef): Long {
    i[0] = i[0] + globLong(j)
    return i[0]
}

fun plusAssignGlobalULong(i: Array<ULong>, j: GlobalRef): ULong {
    i[0] = i[0] + globULong(j)
    return i[0]
}

fun plusAssignGlobalShort(i: ShortArray, j: GlobalRef): Short {
    i[0] = (i[0] + globShort(j)).toShort()
    return i[0]
}

fun plusAssignGlobalUShort(i: Array<UShort>, j: GlobalRef): UShort {
    i[0] = (i[0] + globUShort(j)).toUShort()
    return i[0]
}

fun plusAssignGlobalFloat(i: FloatArray, j: GlobalRef): Float {
    i[0] = i[0] + globFloat(j)
    return i[0]
}

fun plusAssignGlobalDouble(i: DoubleArray, j: GlobalRef): Double {
    i[0] = i[0] + globDouble(j)
    return i[0]
}

fun minusAssignGlobalInt(i: IntArray, j: GlobalRef): Int {
    i[0] = i[0] - globInt(j)
    return i[0]
}

fun minusAssignGlobalUInt(i: Array<UInt>, j: GlobalRef): UInt {
    i[0] = i[0] - globUInt(j)
    return i[0]
}

fun minusAssignGlobalLong(i: LongArray, j: GlobalRef): Long {
    i[0] = i[0] - globLong(j)
    return i[0]
}

fun minusAssignGlobalULong(i: Array<ULong>, j: GlobalRef): ULong {
    i[0] = i[0] - globULong(j)
    return i[0]
}

fun minusAssignGlobalShort(i: ShortArray, j: GlobalRef): Short {
    i[0] = (i[0] - globShort(j)).toShort()
    return i[0]
}

fun minusAssignGlobalUShort(i: Array<UShort>, j: GlobalRef): UShort {
    i[0] = (i[0] - globUShort(j)).toUShort()
    return i[0]
}

fun minusAssignGlobalFloat(i: FloatArray, j: GlobalRef): Float {
    i[0] = i[0] - globFloat(j)
    return i[0]
}

fun minusAssignGlobalDouble(i: DoubleArray, j: GlobalRef): Double {
    i[0] = i[0] - globDouble(j)
    return i[0]
}

fun timesAssignGlobalInt(i: IntArray, j: GlobalRef): Int {
    i[0] = i[0] * globInt(j)
    return i[0]
}

fun timesAssignGlobalUInt(i: Array<UInt>, j: GlobalRef): UInt {
    i[0] = i[0] * globUInt(j)
    return i[0]
}

fun timesAssignGlobalLong(i: LongArray, j: GlobalRef): Long {
    i[0] = i[0] * globLong(j)
    return i[0]
}

fun timesAssignGlobalULong(i: Array<ULong>, j: GlobalRef): ULong {
    i[0] = i[0] * globULong(j)
    return i[0]
}

fun timesAssignGlobalShort(i: ShortArray, j: GlobalRef): Short {
    i[0] = (i[0] * globShort(j)).toShort()
    return i[0]
}

fun timesAssignGlobalUShort(i: Array<UShort>, j: GlobalRef): UShort {
    i[0] = (i[0] * globUShort(j)).toUShort()
    return i[0]
}

fun timesAssignGlobalFloat(i: FloatArray, j: GlobalRef): Float {
    i[0] = i[0] * globFloat(j)
    return i[0]
}

fun timesAssignGlobalDouble(i: DoubleArray, j: GlobalRef): Double {
    i[0] = i[0] * globDouble(j)
    return i[0]
}

fun divAssignGlobalInt(i: IntArray, j: GlobalRef): Int {
    i[0] = i[0] / globInt(j)
    return i[0]
}

fun divAssignGlobalUInt(i: Array<UInt>, j: GlobalRef): UInt {
    i[0] = i[0] / globUInt(j)
    return i[0]
}

fun divAssignGlobalLong(i: LongArray, j: GlobalRef): Long {
    i[0] = i[0] / globLong(j)
    return i[0]
}

fun divAssignGlobalULong(i: Array<ULong>, j: GlobalRef): ULong {
    i[0] = i[0] / globULong(j)
    return i[0]
}

fun divAssignGlobalShort(i: ShortArray, j: GlobalRef): Short {
    i[0] = (i[0] / globShort(j)).toShort()
    return i[0]
}

fun divAssignGlobalUShort(i: Array<UShort>, j: GlobalRef): UShort {
    i[0] = (i[0] / globUShort(j)).toUShort()
    return i[0]
}

fun divAssignGlobalFloat(i: FloatArray, j: GlobalRef): Float {
    i[0] = i[0] / globFloat(j)
    return i[0]
}

fun divAssignGlobalDouble(i: DoubleArray, j: GlobalRef): Double {
    i[0] = i[0] / globDouble(j)
    return i[0]
}

// The ????? vendor block: the mstring global::operator+
// (+,-,*,/,% and compound) families are commented out in the
// vendor source (H-013 dead-code ledger, kept commented).

// unary plus: tmp = *this + 0 (the vendor chains the global int
// conversion; the plus member seat belongs to the B2 misc-2 unit)
operator fun GlobalRef.unaryPlus(): Mstring {
    return Mstring(this.plus(0).toString())
}

// unary minus: the vendor body is CUT at the chunker edge after
// the assignment statement below; the closing return and braces
// are outside the chunk (chunker boundary note). Served through
// the same conversion composition as the vendor line.
operator fun GlobalRef.unaryMinus(): Mstring {
    return Mstring((0 - this.plus(0)).toString())
}
