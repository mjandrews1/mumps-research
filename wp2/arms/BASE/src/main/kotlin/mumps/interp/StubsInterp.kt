package mumps.interp

/**
 * Stubs for interpreter-side units not yet translated (BASE arm, pilot).
 * Replaced by real translations when each unit's turn comes in batch order.
 */

/**
 * C++: struct MSV — the Mumps interpreter state vector (svPtr).
 * A class (instance state), NOT a singleton: the interpreter may host
 * multiple state vectors. Members are added unit by unit.
 */
class MsvState {
    /** C: svPtr->program — program name from argv (mumps.cpp main) */
    var program: String = ""

    // --- stateVector.h fields (added unit 23: libmpscpp.cpp::Input) ---
    /** C: int io — current io unit */
    var io: Int = 0
    /** C: int symflg — symbol table op flag */
    var symflg: Int = 0
    /** C: long tpx */
    var tpx: Long = 0
    /** C: int _Sym — current symbol table level (stateVector.h) */
    var _Sym: Int = 0
    /** C: struct stab *start[SYM_MAX] — symbol table stack heads */
    val start: Array<Stab?> = arrayOfNulls(SYM_MAX)

    // --- stateVector.h fields (added unit 30: interp-parse.cpp::patrn1) ---
    /** C: unsigned char xd[STR_MAX] — expression parse buffer (immutable view here) */
    var xd: String = ""
    /** C: unsigned char bd[STR_MAX] — build buffer; strcat() appends map to append() */
    val bd: StringBuilder = StringBuilder(STR_MAX)
    /** C: int xpx — parse index (stateVector.h) */
    var xpx: Int = 0

    /** C: FILE *in_file[10] — input streams by io unit */
    val inFile: Array<java.io.BufferedReader?> = arrayOfNulls(10)
}

/** C: void sigint(int) — interpreter signal/abort handler (interp-interp.cpp) */
fun sigint(sig: Int): Unit =
    TODO("sigint not yet translated (lives in interp-interp.cpp)")

/** C: int floatstring(char *) — is this a float-formatted string?
 *  Lives in interp-arith.cpp outside batch 1 (not a selected unit). */
fun floatstring(s: String): Boolean =
    TODO("floatstring not yet translated (interp-arith.cpp, outside batch 1)")

/** C: char *sym_(int op, unsigned char *, unsigned char *, MSV *) — symbol
 *  table routing (interp-interp.cpp prototype; body outside batch 1) */
fun sym_(op: Int, name: String, value: String, svPtr: MsvState): String? =
    TODO("sym_ not yet translated (prototype from unit 19; body outside batch 1)")

/** C: $SymPut macro — set symbol (interp-sym.cpp region, not in batch 1) */
fun symPut(name: String, value: String, svPtr: MsvState): Unit =
    TODO("symPut not yet translated")

/** C: int Interpret(const char *, MSV *) — the interpreter core (not in batch 1) */
fun interpret(cmd: String, svPtr: MsvState): Int =
    TODO("interpret not yet translated (not in batch 1)")

/** C: int Mglobal(int op, unsigned char *, unsigned char *, MSV *) — global dispatch (not in batch 1) */
fun mglobal(op: Int, a: String, b: String, svPtr: MsvState): Int =
    TODO("mglobal not yet translated (not in batch 1)")

/** C: char *cleanLocks(char *) — global array lock service (not in batch 1) */
fun cleanLocks(pid: String): String =
    TODO("cleanLocks not yet translated (not in batch 1)")

/** C: unsigned char *zfcn(unsigned char *cmd, unsigned char *out, MSV *svPtr) — $Z function dispatcher; out-buffer -> String return (heuristic 7) */
fun zfcn(cmd: String, svPtr: MsvState): String =
    TODO("zfcn not yet translated (not in batch 1)")

/** C: int xindex(const unsigned char *s, const unsigned char *sub, short start) — substring search from position (interp-parse.cpp) */
fun xindex(s: String, sub: String, start: Short): Int =
    TODO("xindex not yet translated (not in batch 1)")

/** C: int btreeX(int g, unsigned char key[], unsigned char bd[], struct MSV * svPtr) */
fun btreeX(g: Int, key: String, bd: StringBuilder, svPtr: MsvState): Int =
    TODO("unit interp-native-btree.cpp::btreeX not yet translated (wrapper btree is unit 4)")
