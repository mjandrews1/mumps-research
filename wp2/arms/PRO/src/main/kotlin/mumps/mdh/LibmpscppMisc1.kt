package mumps.mdh

import java.io.File
import java.io.FileNotFoundException
import java.io.RandomAccessFile
import kotlin.system.exitProcess
import mumps.interp.NUL
import mumps.interp.strlenC
import mumps.interp.strncmpC

/**
 * PRO-arm translation of libmpscpp.cpp::&lt;misc-1&gt; (pilot B1, unit 31,
 * 698-line / 600-physical-LOC residue chunk). Translated independently
 * from the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe).
 *
 * Chunk inventory (vendor order):
 *  - File-header residue: banner, #define SQLITE/MPSCPP/_INTERP_, the
 *    include set, VERSION "@VERSION@" (unresolved build substitution),
 *    LOG2/NO/YES macros, the GCVT-arm gcvt_wrapper macro (real
 *    implementation owned by unit 29: NumSetup.kt gcvtWrapper), the
 *    sigint/pm declarations (sigint stub: InterpStubs.kt, batch-7
 *    owner; pm is interp machinery, not a B1 unit), short ierr1, int
 *    GlobalExceptionCode = UNSPECIFIED (libmpscpp.h:69: 0), the
 *    file-scope svPtr (h004: rides MdhState.svPtr), int global::Eflg=0
 *    (h004: Global companion).
 *  - The ~25 dollar-z~mdh~ wrapper comment banners: their bodies lie
 *    beyond the misc-1 cutoff (misc-2 residue).
 *  - A residual mdh_tree_level_add 10-column body: unit 8 already
 *    translates this overload family (MdhTreeLevelAdd.kt, the
 *    col1..col10 overload) - NOT re-declared here.
 *  - The Hash class (constructor, destructor, operator() string and
 *    mstring, operator= mstring and the big operator= string), the
 *    indirect class assignment, the global::operator() subscript
 *    builder family (folded into GlobalCarrier.invoke - see journal),
 *    the global constructor (GlobalCarrier init), the 32 free
 *    compound-assignment operators over global, and the unary
 *    global::operator+/operator- (chunk boundary cuts operator- mid
 *    body; the closing return is supplied, see journal).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 FILE_stream         : FILE *hashkey -> RandomAccessFile
 *    (fgetc -> read, FSEEK -> seek, FTELL -> filePointer, feof ->
 *    read() == -1; see journal for the w+/r+ modeling).
 *  - h004 globals_ext         : file-scope globals ride MdhState /
 *    the Global companion; svPtr rides MdhState.svPtr.
 *  - h012 unsigned_byte_domain: p[STR_MAX] and hash-file bytes are a
 *    Char(0..255) domain; the 1 (empty-slot) and 2 (tombstone) markers
 *    are Char(0x01)/Char(0x02).
 *  - h013 dead_store          : vendor's long m in Hash::operator=
 *    (string) is never used - dropped with this comment, not "fixed".
 *  - h014 out_of_scope_call   : lhash, makeGlobal, __finish,
 *    thrownMessage, globalNumericValue, globalPlusInt, intMinusGlobal
 *    -> MdhStubs.kt (each names its owner).
 *  - h015 grammar_collision   : C control bytes as Kotlin escapes;
 *    no raw dollar signs in Kotlin string literals; C++ operator= ->
 *    named assign functions; the 32 free compound-assignment operators
 *    -> named helper functions (adaptation, journal).
 *  - h017 naming             : Hash/indirect keep the vendor names;
 *    plusAssign/minusAssign/timesAssign/divAssign helpers follow the
 *    Kotlin operator naming with C-type suffixes.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - Hash::operator()(mstring) reads via s_str() while the global
 *    family uses c_str(); both are kept distinct (Mstring carries an
 *    s_str alias).
 *  - The second zap pass comment reads "zap to 1's" but the vendor
 *    writes 2's (p[0]=2) - kept as written.
 *  - The re-read loop after the ii-seek has no EOF check (raw fgetc
 *    value stored); kept.
 *  - C exit(EXIT_FAILURE) -> kotlin.system.exitProcess(1).
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val EOF = -1 // C fgetc end-of-file marker
private const val EXIT_FAILURE = 1 // C stdlib EXIT_FAILURE

/** C exit(EXIT_FAILURE) with the vendor message on stdout. */
private fun fatal(msg: String): Nothing {
    println(msg)
    exitProcess(EXIT_FAILURE)
}

/**
 * Vendor class Hash (libmpscpp.cpp residue): fixed-size disk hash file.
 * FILE *hashkey -> RandomAccessFile (h001). The C++ destructor ~Hash
 * becomes an explicit close() (JVM has no deterministic destruction).
 */
class Hash(name: String, sz: Long, disp: String) {

    var key: String
    var size: Long
    var ref: String = ""
    private val hashkey: RandomAccessFile

    init {
        // vendor: char x = '\1' - the padding byte for new files
        key = name + ".key"
        size = sz
        if (name.isEmpty()) fatal("Invalid hash file name")
        if (sz < 1000) fatal("Invalid hash file size in hash " + name)
        // vendor: "new" -> "w+", "old" -> "r+" (RandomAccessFile modes
        // mapped below; see journal)
        val code: String = when (disp) {
            "new" -> "w+"
            "old" -> "r+"
            else -> fatal("Unknown hash file disposition")
        }
        hashkey = try {
            val f = File(key)
            // r+ must open an existing file (fopen fails otherwise);
            // RandomAccessFile "rw" would create it, so enforce here.
            if (code == "r+" && !f.exists()) throw FileNotFoundException(key)
            RandomAccessFile(f, "rw")
        } catch (e: FileNotFoundException) {
            fatal("Hash file open error.")
        }
        if (disp == "new") {
            // w+ truncates; RandomAccessFile does not - do it here.
            hashkey.setLength(0)
            // vendor: for (i=0; i<size; i++) fwrite(p, 1, 1, hashkey)
            // writes the single byte x='\1' size times.
            for (i in 0 until size) hashkey.write(1)
        }
    }

    /** Vendor ~Hash() - destructor: if (hashkey != NULL) fclose(hashkey). */
    fun close() {
        hashkey.close()
    }

    /** Vendor: Hash & Hash::operator()(string str) { ref = str; return *this; } */
    operator fun invoke(str: String): Hash {
        ref = str
        return this
    }

    /** Vendor: Hash & Hash::operator()(mstring str) { ref = str.s_str(); ... } */
    operator fun invoke(str: Mstring): Hash {
        ref = str.s_str()
        return this
    }

    /** Vendor: mstring Hash::operator=(mstring str). */
    fun assign(str: Mstring): Mstring {
        str.value = assign(str.value)
        return str
    }

    /** fwrite(str.c_str(), str.length()+1, 1, hashkey) - bytes plus NUL. */
    private fun writeC(s: String) {
        for (c in s) hashkey.write(c.code)
        hashkey.write(0)
    }

    /**
     * Vendor: string Hash::operator=(string str) - the big hash-file
     * insert/replace body. Lines kept in vendor order; m is a dead
     * vendor local (h013, dropped); loopflg maps the C int 0/1 flags.
     */
    fun assign(str: String): String {
        val p = CharArray(STR_MAX)
        val f = hashkey
        if (str.isEmpty()) fatal("zero length hash reference")
        var i = lhash(ref) % size // h014 stub; calculate a hash
        f.seek(i) // FSEEK(hashkey, i, SEEK_SET)
        var flg = 0
        var loopflg = false
        var c: Int
        var ii: Long // off_t ii (vendor file-scope of the function body)
        outer@ while (true) {
            ii = f.filePointer // FTELL - starting point
            var j = 0L
            while (true) {
                c = f.read()
                if (c == EOF) break
                if (c == 2) continue
                if (c == 1) break // not found
                p[j.toInt()] = c.toChar() // p[j++] = c
                j++
                if (c == 0) break
            }
            if (c == 1 || (loopflg && c == EOF)) break // not found exit
            if (c == EOF) {
                loopflg = true
                i = 0
                f.seek(i)
                ii = f.filePointer
                continue@outer
            }
            if (strncmpC(p, 0, ref, ref.length) == 0) { // strcmp(p, ref.c_str()) == 0
                flg = 1
                break // matched: exit loop
            }
            j = 0
            while (true) { // bypass value
                c = f.read()
                if (c == EOF) break
                if (c == 1) break
                p[j.toInt()] = c.toChar()
                j++
                if (c == 0) break
            }
        }
        if (flg == 1) { // instance found
            i = f.filePointer
            var j = 0L
            var k: Long
            var jj: Long
            while (j < STR_MAX) { // get value
                c = f.read()
                if (c == EOF) fatal("EOF in hash file")
                p[j.toInt()] = c.toChar()
                if (p[j.toInt()] == NUL) break
                j++
            }
            if (strlenC(p) >= str.length) { // replace
                f.seek(i)
                k = strlenC(p).toLong()
                p[0] = '\u0002'
                jj = 0
                while (jj <= k) { // zap to 2's
                    f.write(2)
                    jj++
                }
                f.seek(i) // reset
                writeC(str) // fwrite(str.c_str(), str.length()+1, 1, hashkey)
                return str
            }
            f.seek(ii)
            k = (ref.length + 1).toLong()
            p[0] = '\u0002'
            jj = 0
            while (jj <= k) { // zap to 2's
                f.write(2)
                jj++
            }
            ii = f.filePointer
            j = 0
            while (j < STR_MAX) { // get value (no EOF check in vendor - kept)
                p[j.toInt()] = f.read().toChar()
                if (p[j.toInt()] == NUL) break
                j++
            }
            k = (strlenC(p) + 1).toLong()
            p[0] = '\u0002'
            f.seek(ii)
            jj = 0
            while (jj <= k) { // vendor comment says "zap to 1's" but writes 2's - kept
                f.write(2)
                jj++
            }
        }
        loopflg = false
        val j = (ref.length + str.length + 3).toLong()
        i = lhash(ref) % size
        f.seek(i)
        var k = 0L
        i = f.filePointer
        while (true) {
            val cc = f.read() // fread(p, 1, 1, hashkey)
            if (cc != EOF) p[0] = cc.toChar()
            if (cc == EOF && !loopflg) {
                loopflg = true
                i = 0
                f.seek(i)
                continue
            }
            if (cc == EOF && loopflg) {
                println()
                fatal("table full - eof")
            }
            if (p[0] == '\u0001') { // empty slot found
                k++
                if (k >= j) break
                continue
            }
            k = 0
            i = f.filePointer
        }
        f.seek(i)
        writeC(ref) // fwrite(ref.c_str(), ref.length()+1, 1, hashkey)
        writeC(str)
        return str
    }
}

/**
 * Vendor class indirect (libmpscpp.cpp residue, minimal carrier):
 * holds a reference string; operator= prints reference and value.
 */
class Indirect {
    var reference: String = ""

    /** Vendor: indirect indirect::operator=(string a). */
    fun assign(a: String): Indirect {
        println(reference + " " + a)
        return this
    }
}

/**
 * Vendor free compound-assignment operators (32 definitions):
 * operator+=, -=, *= and /= for int, unsigned int, long, unsigned
 * long, short, unsigned short, float and double, each with a global
 * right-hand side read through the global implicit-conversion
 * operators, each body "i = i op j; return i;".
 *
 * Kotlin cannot define compound-assignment operators on built-in
 * numeric receivers, so the family is rendered as named helper
 * functions returning the updated value (adaptation, journal). C
 * unsigned types ride signed Kotlin carriers of the same width.
 * The implicit conversion collapses to the globalNumericValue stub
 * (h014, see journal).
 */
fun plusAssignInt(i: Int, j: Global): Int = (i + globalNumericValue(j)).toInt()
fun plusAssignUInt(i: Int, j: Global): Int = (i + globalNumericValue(j)).toInt()
fun plusAssignLong(i: Long, j: Global): Long = (i + globalNumericValue(j)).toLong()
fun plusAssignULong(i: Long, j: Global): Long = (i + globalNumericValue(j)).toLong()
fun plusAssignShort(i: Short, j: Global): Short = (i + globalNumericValue(j)).toInt().toShort()
fun plusAssignUShort(i: Short, j: Global): Short = (i + globalNumericValue(j)).toInt().toShort()
fun plusAssignFloat(i: Float, j: Global): Float = (i + globalNumericValue(j)).toFloat()
fun plusAssignDouble(i: Double, j: Global): Double = i + globalNumericValue(j)

fun minusAssignInt(i: Int, j: Global): Int = (i - globalNumericValue(j)).toInt()
fun minusAssignUInt(i: Int, j: Global): Int = (i - globalNumericValue(j)).toInt()
fun minusAssignLong(i: Long, j: Global): Long = (i - globalNumericValue(j)).toLong()
fun minusAssignULong(i: Long, j: Global): Long = (i - globalNumericValue(j)).toLong()
fun minusAssignShort(i: Short, j: Global): Short = (i - globalNumericValue(j)).toInt().toShort()
fun minusAssignUShort(i: Short, j: Global): Short = (i - globalNumericValue(j)).toInt().toShort()
fun minusAssignFloat(i: Float, j: Global): Float = (i - globalNumericValue(j)).toFloat()
fun minusAssignDouble(i: Double, j: Global): Double = i - globalNumericValue(j)

fun timesAssignInt(i: Int, j: Global): Int = (i * globalNumericValue(j)).toInt()
fun timesAssignUInt(i: Int, j: Global): Int = (i * globalNumericValue(j)).toInt()
fun timesAssignLong(i: Long, j: Global): Long = (i * globalNumericValue(j)).toLong()
fun timesAssignULong(i: Long, j: Global): Long = (i * globalNumericValue(j)).toLong()
fun timesAssignShort(i: Short, j: Global): Short = (i * globalNumericValue(j)).toInt().toShort()
fun timesAssignUShort(i: Short, j: Global): Short = (i * globalNumericValue(j)).toInt().toShort()
fun timesAssignFloat(i: Float, j: Global): Float = (i * globalNumericValue(j)).toFloat()
fun timesAssignDouble(i: Double, j: Global): Double = i * globalNumericValue(j)

fun divAssignInt(i: Int, j: Global): Int = (i / globalNumericValue(j)).toInt()
fun divAssignUInt(i: Int, j: Global): Int = (i / globalNumericValue(j)).toInt()
fun divAssignLong(i: Long, j: Global): Long = (i / globalNumericValue(j)).toLong()
fun divAssignULong(i: Long, j: Global): Long = (i / globalNumericValue(j)).toLong()
fun divAssignShort(i: Short, j: Global): Short = (i / globalNumericValue(j)).toInt().toShort()
fun divAssignUShort(i: Short, j: Global): Short = (i / globalNumericValue(j)).toInt().toShort()
fun divAssignFloat(i: Float, j: Global): Float = (i / globalNumericValue(j)).toFloat()
fun divAssignDouble(i: Double, j: Global): Double = i / globalNumericValue(j)
