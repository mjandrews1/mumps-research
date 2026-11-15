package mumps.mdh

import mumps.interp.CRuntime
import mumps.interp.InterpState
import mumps.interp.MsvState
import mumps.interp.STR_MAX
import java.io.RandomAccessFile
import java.util.Locale
import kotlin.system.exitProcess

/**
 * Kotlin translation of libmpscpp.cpp::<misc-1> (Study Subset unit 31,
 * Batch 1, arm BASE; translated 2026-11-15).
 *
 * UNIT SHAPE: this unit is the FILE-SCOPE RESIDUE of libmpscpp.cpp
 * (pristine-tree lines 1-4679, 600 physical LOC, 207 line-runs interleaved
 * with the larger functions): the banner/includes/defines block, the
 * file-scope globals, every small function the selector merged into the
 * residual chunk (below 15 physical LOC), the GTK section banner
 * comments, and the commented-out operator block (kept as
 * documentation, heuristic 26).
 *
 * C regions translated here:
 *  - lines 1-100: banner, #defines (SQLITE/MPSCPP/_INTERP_/VERSION/
 *    LOG2/NO/YES/gcvt_wrapper/GETPID), prototypes (sigint, pm), globals
 *    (ierr1, GlobalExceptionCode, svPtr, global::Eflg)
 *  - lines 534-589: mdh_tree_level_add 10-col overload (covered by unit 8)
 *  - lines 934-1148: the Hash class (ctor/dtor/operator()/operator=)
 *  - line 2360: indirect::operator=(string)
 *  - lines 2739-2930: global::operator() subscript overloads -> the
 *    real invoke() in MdhGlobal (see StubsMdh.kt)
 *  - lines 3451-3462: global::global(const char *) constructor ->
 *    MdhGlobal secondary constructor
 *  - lines 4454-4614: free operator+= -= *= /= overloads on primitives
 *  - lines 4616-4665: commented-out operator block (dead code, doc only)
 *  - lines 4671-4679: unary global::operator+ / operator- (-> MdhGlobal)
 */

// ---------------------------------------------------------------------------
// C: lines 31-76 — #define block
// ---------------------------------------------------------------------------

/** C: #define SQLITE — build-configuration marker; reference build uses
 *  SQLITE (banner: "DBMS: SQLITE"). */
const val SQLITE_MARKER = true

/** C: #define MPSCPP — C++ library build marker. */
const val MPSCPP_MARKER = true

/** C: #define _INTERP_ — interpreter (not standalone-compiler) build. */
const val INTERP_MARKER = true

/**
 * C: #define VERSION "@VERSION@" — QUIRK PRESERVED: the shipped tarball
 * contains the UNEXPANDED autoconf placeholder (the release was built
 * without configure substitution). Kept verbatim (heuristic 26 family).
 */
const val VERSION = "@VERSION@"

/** C: #define LOG2 log2 */
internal fun log2(x: Double): Double = kotlin.math.log2(x)

/** C: #define NO 0 / #define YES 1 */
const val NO = 0
const val YES = 1

/**
 * C: #ifdef GCVT -> gcvt_wrapper(x,i,a)=sprintf(a,"%g",x)
 *    #else        -> gcvt_wrapper(x,i,a)=gcvt(x,i,a)
 * NOTE the INVERSION vs interp-arith.cpp: THERE GCVT-undefined means
 * "%.8g"; HERE GCVT-undefined means real gcvt(). Same macro name,
 * opposite resolution, different translation unit (heuristic 12 family:
 * per-unit macro scoping). Reference build: GCVT undefined.
 */
internal fun libmpscppGcvtWrapper(x: Double, i: Int): String =
    if (InterpState.GCVT)
        String.format(Locale.US, "%g", x)
    else CRuntime.gcvt(x, i)

/** C: #define GETPID getpid — JVM ProcessHandle (heuristic 71 family). */
internal fun getpid(): Long = ProcessHandle.current().pid()

// ---------------------------------------------------------------------------
// C: lines 78-100 — file-scope globals and prototypes
// ---------------------------------------------------------------------------

// C: void sigint(int); — prototype only; the live stub is StubsInterp.kt.

/** C: short ierr1 (libmpscpp.cpp:80) — interpreter error register.
 *  Distinct from CompilerState.ierr (compiler TU; heuristic 12 family). */
var ierr1: Short = 0

/** C: int GlobalExceptionCode = UNSPECIFIED (line 84) — the live var is
 *  mumps.interp.GlobalExceptionCode (Getstr1.kt, initial 0). */
const val UNSPECIFIED = 0 // C: include/mumpsc/libmpscpp.h:69

/** C: int pm(unsigned char [], const unsigned char [], struct MSV *);
 *  prototype only — body outside batch 1, stub so future units resolve. */
fun pm(string: String, pattern: String, svPtr: MsvState): Int =
    TODO("pm not yet translated (body outside batch 1)")

// C: MSV *svPtr = AllocSV(); (line 99) — global initial stack frame.
// Already translated by unit 8 as MdhRuntime.svPtr (MdhTreeLevelAdd.kt,
// which documents this exact C line); NOT redeclared here (heuristic 32
// family: one Kotlin owner per C entity).

// C: int global::Eflg = 0 (line 100) — lives on MdhGlobal.companion.

/** C: void thrownMessage() (libmpscpp.cpp:86) — body outside batch 1. */
fun thrownMessage(): Unit = TODO("thrownMessage not yet translated (body outside batch 1)")

/** C: void __finish() (libmpscpp.cpp:102) — body outside batch 1. */
fun __finish(): Unit = TODO("__finish not yet translated (body outside batch 1)")

// ---------------------------------------------------------------------------
// C: lines 167-930 — GTK interface section
//
// The section is a run of banner comments (each $z~mdh~ widget call is a
// larger function = its own unit elsewhere). Only mdh_tree_level_add
// fell into this residual chunk (below the selector's 15-LOC threshold).
// ---------------------------------------------------------------------------

/**
 * C: void mdh_tree_level_add(GtkTreeStore *tree, int depth, char *col1..col10)
 * (lines 534-589, the 10-column overload that fell into this residual
 * chunk) — ALREADY COVERED by unit 8's mdhTreeLevelAdd (MdhTreeLevelAdd.kt):
 * unit 8 translated the 1..4-column overloads as one required-col + vararg
 * function (heuristic 32), which covers every C++ column arity including
 * this one. NOT redeclared here (one Kotlin owner per C entity).
 *
 * The 10-column C body is otherwise identical in shape to the unit-8
 * overloads (same \xce/\xd0/\xcf zcall assembly, same uninitialized
 * tmp[0] read), so nothing else from this region needs translation.
 */

// ---------------------------------------------------------------------------
// C: lines 934-1148 — the Hash class
// ---------------------------------------------------------------------------

/**
 * C: class Hash — fixed-size direct-access hashed file (lines 934-1148).
 * FILE* hashkey -> RandomAccessFile; the byte-level store/replace
 * algorithm is carried 1:1. Sentinel bytes: 1 = empty slot marker,
 * 2 = deleted/zapped slot (both built as Int literals — no escapes).
 */
class Hash(name: String, sz: Long, disp: String) {

    var key: String = ""                    // C: string key
    var size: Long = 0                      // C: long size
    var ref: String = ""                    // C: string ref
    var hashkey: RandomAccessFile? = null   // C: FILE *hashkey

    init {
        key = "$name.key"
        size = sz

        if (name.isEmpty()) { // C: name.length()==0
            println("Invalid hash file name")
            exitProcess(1) // C: exit(EXIT_FAILURE)
        }
        if (sz < 1000) {
            println("Invalid hash file size in hash $name")
            exitProcess(1)
        }

        val mode: String = when (disp) {
            "new" -> "rw" // C: "w+"
            "old" -> "rw" // C: "r+" — JVM RandomAccessFile has no r+;
            // the preexistence difference is unobservable for the
            // oracle flows (documented divergence, heuristic 71 family)
            else -> {
                println("Unknown hash file disposition")
                exitProcess(1)
            }
        }
        hashkey = try {
            RandomAccessFile(key, mode)
        } catch (e: java.io.IOException) {
            println("Hash file open error.")
            exitProcess(1)
        }

        if (disp == "new") {
            // C: char x='\1'; char *p=&x; for (i=0;i<size;i++) fwrite(p,1,1,hashkey)
            for (i in 0 until size) hashkey!!.write(1)
        }
    }

    /** C: Hash::~Hash() { if (hashkey!=NULL) fclose(hashkey); } — no Kotlin
     *  destructors; [close] is the explicit counterpart (heuristic 71). */
    fun close() {
        hashkey?.close()
        hashkey = null
    }

    /** C: Hash & Hash::operator()(string str) { ref=str; return *this; } */
    operator fun invoke(str: String): Hash {
        ref = str
        return this
    }

    /**
     * C: string Hash::operator=(string str) (lines 992-1148) — the full
     * lookup / replace / append algorithm, byte-exact. Returns the
     * assigned value (C++ operator= returns its argument).
     *
     * p is a ByteArray to keep C byte semantics (including the
     * stored-EOF quirk in the value-zap path below: the C stores
     * fgetc()'s -1 into p[j] and keeps reading — preserved).
     */
    fun assign(str: String): String {
        val f = hashkey!!
        val p = ByteArray(STR_MAX) // C: char p[STR_MAX]
        val EOF = -1
        val SLOT_EMPTY = 1  // C: byte 1 — empty-slot marker
        val SLOT_ZAPPED = 2 // C: byte 2 — deleted-slot marker

        /** C-string view of p: bytes up to the first NUL. */
        fun pstr(len: Int = STR_MAX): String {
            val end = minOf(len, p.indexOf(0.toByte()).takeIf { it >= 0 } ?: p.size)
            return String(p, 0, end, Charsets.ISO_8859_1)
        }

        if (str.isEmpty()) { // C: str.length()==0
            println("zero length hash reference")
            exitProcess(1)
        }

        var i = lhash(ref) % size // C: off_t i = lhash(ref.c_str())%size
        f.seek(i)                 // C: FSEEK(hashkey,i,SEEK_SET)

        var flg = 0
        var loopflg = 0
        var c: Int
        var ii = 0L // C: off_t ii — key start (needed past the loop)

        // C: while(1) lookup loop — restructured with labeled breaks (heuristic 73)
        lookup@ while (true) {
            ii = f.filePointer // C: ii=FTELL(hashkey)
            var j = 0
            readKey@ while (true) {
                c = f.read()
                if (c == EOF) break // C: if (c==EOF) break
                if (c == SLOT_ZAPPED) continue // C: if (c==2) continue
                if (c == SLOT_EMPTY) break     // C: if (c==1) break — not found
                p[j] = c.toByte(); j++
                if (c == 0) break // C: if (c=='\0') break
            }
            if (c == SLOT_EMPTY || (loopflg != 0 && c == EOF)) break@lookup // not found exit
            if (c == EOF) { // wrap around to file start
                loopflg = 1
                i = 0
                f.seek(0) // C: FSEEK(hashkey,0,SEEK_SET)
                ii = f.filePointer
                continue@lookup
            }
            if (pstr(j) == ref) { flg = 1; break@lookup } // C: strcmp(p,ref)==0
            j = 0
            bypassValue@ while (true) { // bypass value
                c = f.read()
                if (c == EOF) break
                if (c == SLOT_EMPTY) break
                p[j] = c.toByte(); j++
                if (c == 0) break
            }
        }

        if (flg == 1) { // instance found
            i = f.filePointer // C: i=FTELL(hashkey)
            var j = 0
            while (j < STR_MAX) { // get value
                c = f.read()
                if (c == EOF) {
                    println("EOF in hash file")
                    exitProcess(1)
                }
                p[j] = c.toByte()
                j++
                if (p[j - 1] == 0.toByte()) break // C: if (p[j]=='\0') break
            }

            if (pstr().length >= str.length) { // C: strlen(p)>=str.length() — replace
                f.seek(i) // C: FSEEK(hashkey,i,SEEK_SET)
                val k = pstr().length // C: k=strlen(p)
                for (z in 0..k) f.write(SLOT_ZAPPED) // C: p[0]=2; zap to 2's
                f.seek(i) // C: reset
                f.write(str.toByteArray(Charsets.ISO_8859_1) + 0.toByte()) // C: fwrite(str, len+1)
                return str
            }

            // no-replace path: zap the OLD key slot (at ii) and old value
            f.seek(ii) // C: FSEEK(hashkey,ii,SEEK_SET)
            var k = ref.length + 1 // C: k=ref.length()+1
            for (z in 0..k) f.write(SLOT_ZAPPED) // C: p[0]=2; zap to 2's
            ii = f.filePointer // C: ii=FTELL(hashkey)
            j = 0
            while (j < STR_MAX) { // get value — QUIRK: C stores fgetc's
                // EOF (-1) into p[j] and continues; preserved byte-exactly
                p[j] = f.read().toByte()
                j++
                if (p[j - 1] == 0.toByte()) break
            }
            k = pstr().length + 1 // C: k=strlen(p)+1
            f.seek(ii)            // C: FSEEK(hashkey,ii,SEEK_SET)
            // C: comment says "zap to 1's" but the code writes p[0]=2 —
            // QUIRK PRESERVED: 2's (comment/code mismatch, heuristic 26)
            for (z in 0..k) f.write(SLOT_ZAPPED)
        }

        // append path: find a run of j consecutive empty slots
        loopflg = 0
        val runNeeded = ref.length + str.length + 3 // C: j=ref.length()+str.length()+3
        i = lhash(ref) % size
        f.seek(i)
        var k = 0
        i = f.filePointer // C: i=FTELL(hashkey)
        scan@ while (true) {
            c = f.read() // C: fread(p,1,1,hashkey)
            if (c == EOF && loopflg == 0) { // C: feof&&!loopflg
                loopflg = 1
                i = 0
                f.seek(0)
                continue@scan
            }
            if (c == EOF && loopflg != 0) { // C: feof&&loopflg
                println()
                println("table full - eof")
                exitProcess(1)
            }
            if (c == SLOT_EMPTY) { // C: p[0]==1 — empty slot
                k++
                if (k >= runNeeded) break@scan
                continue@scan
            }
            k = 0
            i = f.filePointer // C: i=FTELL(hashkey)
        }

        f.seek(i) // C: FSEEK(hashkey,i,SEEK_SET)
        f.write(ref.toByteArray(Charsets.ISO_8859_1) + 0.toByte()) // C: fwrite(ref, ref.length()+1)
        f.write(str.toByteArray(Charsets.ISO_8859_1) + 0.toByte()) // C: fwrite(str, str.length()+1)
        return str
    }
}

/** C: unsigned long lhash(const char *) (libmpscpp.h:273) — body outside
 *  batch 1. */
fun lhash(s: String): Long =
    TODO("lhash not yet translated (body outside batch 1)")

// ---------------------------------------------------------------------------
// C: line 2360 — indirect
// ---------------------------------------------------------------------------

/** C: class indirect (libmpscpp.h) — operator= prints the pair. */
class Indirect(var reference: String = "") {
    /** C: indirect & indirect::operator=(string a) {
     *      cout << (*this).reference << " " << a << endl; return *this; } */
    fun assign(a: String): Indirect {
        println("$reference $a")
        return this
    }
}

// ---------------------------------------------------------------------------
// C: lines 4454-4614 — free operator+= -= *= /= overloads on primitives
//
// C++ free functions mutating a primitive through a reference parameter:
//     int operator+=(int &i, global j) { i = i + j; return i; }
// Kotlin cannot overload += on built-in numeric types (no mutable
// primitive receivers), so each becomes a NAMED extension function
// returning the new value (heuristics 71/54: honest JVM shape).
// The global->number conversion is the shared stub below (the C's
// operator int()/double() bodies are outside batch 1).
// ---------------------------------------------------------------------------

/** C: global's numeric conversions (operator int()/double(), outside
 *  batch 1) collapsed to one documented stub. */
fun MdhGlobal.numval(): Double =
    TODO("global numeric conversion not yet translated (outside batch 1)")

private fun MdhGlobal.toIntVal(): Int = numval().toInt()
private fun MdhGlobal.toLongVal(): Long = numval().toLong()
private fun MdhGlobal.toShortVal(): Short = numval().toInt().toShort()

// C: operator+= (lines 4456-4494)
fun Int.plusGlobal(g: MdhGlobal): Int = this + g.toIntVal()
fun UInt.plusGlobal(g: MdhGlobal): UInt = this + g.toIntVal().toUInt()
fun Long.plusGlobal(g: MdhGlobal): Long = this + g.toLongVal()
fun ULong.plusGlobal(g: MdhGlobal): ULong = this + g.toLongVal().toULong()
fun Short.plusGlobal(g: MdhGlobal): Short = (this + g.toShortVal()).toShort()
fun UShort.plusGlobal(g: MdhGlobal): UShort =
    (this.toInt() + g.toIntVal()).toShort().toUShort()
fun Float.plusGlobal(g: MdhGlobal): Float = this + g.numval().toFloat()
fun Double.plusGlobal(g: MdhGlobal): Double = this + g.numval()

// C: operator-= (lines 4496-4534)
fun Int.minusGlobal(g: MdhGlobal): Int = this - g.toIntVal()
fun UInt.minusGlobal(g: MdhGlobal): UInt = this - g.toIntVal().toUInt()
fun Long.minusGlobal(g: MdhGlobal): Long = this - g.toLongVal()
fun ULong.minusGlobal(g: MdhGlobal): ULong = this - g.toLongVal().toULong()
fun Short.minusGlobal(g: MdhGlobal): Short = (this - g.toShortVal()).toShort()
fun UShort.minusGlobal(g: MdhGlobal): UShort =
    (this.toInt() - g.toIntVal()).toShort().toUShort()
fun Float.minusGlobal(g: MdhGlobal): Float = this - g.numval().toFloat()
fun Double.minusGlobal(g: MdhGlobal): Double = this - g.numval()

// C: operator*= (lines 4536-4574)
fun Int.timesGlobal(g: MdhGlobal): Int = this * g.toIntVal()
fun UInt.timesGlobal(g: MdhGlobal): UInt = this * g.toIntVal().toUInt()
fun Long.timesGlobal(g: MdhGlobal): Long = this * g.toLongVal()
fun ULong.timesGlobal(g: MdhGlobal): ULong = this * g.toLongVal().toULong()
fun Short.timesGlobal(g: MdhGlobal): Short = (this * g.toShortVal()).toShort()
fun UShort.timesGlobal(g: MdhGlobal): UShort =
    (this.toInt() * g.toIntVal()).toShort().toUShort()
fun Float.timesGlobal(g: MdhGlobal): Float = this * g.numval().toFloat()
fun Double.timesGlobal(g: MdhGlobal): Double = this * g.numval()

// C: operator/= (lines 4576-4614) — C integer division truncates; Kotlin
// Int/Long division matches; Short widened to Int then truncated back.
fun Int.divGlobal(g: MdhGlobal): Int = this / g.toIntVal()
fun UInt.divGlobal(g: MdhGlobal): UInt = this / g.toIntVal().toUInt()
fun Long.divGlobal(g: MdhGlobal): Long = this / g.toLongVal()
fun ULong.divGlobal(g: MdhGlobal): ULong = this / g.toLongVal().toULong()
fun Short.divGlobal(g: MdhGlobal): Short = (this.toInt() / g.toIntVal()).toShort()
fun UShort.divGlobal(g: MdhGlobal): UShort =
    (this.toInt() / g.toIntVal()).toShort().toUShort()
fun Float.divGlobal(g: MdhGlobal): Float = this / g.numval().toFloat()
fun Double.divGlobal(g: MdhGlobal): Double = this / g.numval()

// ---------------------------------------------------------------------------
// C: lines 4616-4679 — commented-out operator block (dead code, kept as
// documentation, heuristic 26) followed by unary +/-
// ---------------------------------------------------------------------------
//
// C (verbatim, commented out in the source — mstring global::operator+ - * / %
// and += -= *= /= % variants with EMPTY BODIES; kept as this comment).
//
// ---------------------------------------------------------------------------
