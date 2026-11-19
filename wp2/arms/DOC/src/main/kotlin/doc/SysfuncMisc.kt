package doc

// Unit 25 (B1 tranche 4): interp-sysfunc.cpp::<misc> (batch 1,
// complex stratum; units.csv row 25: loc 111). The sysfunc residue:
// O'Kane banner (Jan 10, 2026), the FIX macro define note, the
// SQLITE define, the include ledger (readline/history documented
// at the ledger only - no call site is inside this chunk), LINEFEED
// define ('\012' == '\n'), the banner headers for getstr1 /
// TimeOut_getstr1 / DirectRead / VariableNotFound / ErrorMessage /
// sigint / AllocSV (definitions outside the chunk: getstr1 and
// TimeOut_getstr1 are batch-8 units, DirectRead is unit 24 next to
// this chunk, AllocSV is not a corpus unit), and the eight small
// bodies this chunk owns: VariableNotFound, VariableNotFound1,
// ErrorMessage, LocalData, LocalOrder, LocalNext, GlobalNext,
// LocalGet, GlobalGet, fullUnpad. printf maps to System.out
// (H-001); sym_ and Mglobal are the H-014 stubs (the sym_ seat
// returns the vendor's NULL as the Kotlin nullable carrier); unpad
// is a batch-10 unit carried as an H-014 stub; the strtok walk over
// the \x01 delimiter renders as the manual split (strtok has no
// Kotlin analog, the stateful tokenizer note is in the KDoc);
// strcatx reuses the internal inline.h:46 helper (unit 23 file).
// The duplicated VariableNotFound banner header in the vendor (the
// comment banner appears twice) is noted. svPtr->order is the
// stateVector MSV field. Naming per H-017.

// VariableNotFound(long, char *): diagnostics + sigint + empty
// pointer return (rendered as the empty carrier copy).
fun variableNotFound(LineNumber: Long, var_: CharArray): CharArray {
    System.out.print("\n*** Variable " + CstringOps.cstring(var_) +
            " not found in or near line " + LineNumber + "\n")
    Stubs.sigint(100)
    return CharArray(STR_MAX)
}

fun variableNotFound1(LineNumber: Long, var_: CharArray): CharArray {
    System.out.print("\n*** Variable " + CstringOps.cstring(var_) +
            " not found in or near line " + LineNumber + "\n")
    Stubs.sigint(100)
    return CharArray(STR_MAX)
}

fun errorMessage(msg: String, LineNumber: Long) {
    System.out.print("\n*** " + msg + " in or near line " + LineNumber + "\n")
    Stubs.sigint(100)
}

fun LocalData(tmp0: CharArray, tmp2: CharArray, svPtr: Msv) {
    Stubs.sym_(6, tmp0, tmp2, svPtr)
    return
}

fun LocalOrder(tmp0: CharArray, tmp1: CharArray, tmp2: CharArray?,
        svPtr: Msv) {
    if (tmp2 == null) svPtr.order = 1
    else if (CstringOps.cstring(tmp2) == "1") svPtr.order = 1
    else if (CstringOps.cstring(tmp2) == "-1") svPtr.order = -1
    else errorMessage("order() second argument incorrect",
            svPtr.LineNumber)

    Stubs.sym_(33, tmp0, tmp1, svPtr)
    return
}

fun LocalNext(tmp0: CharArray, tmp1: CharArray, svPtr: Msv) {
    Stubs.sym_(3, tmp0, tmp1, svPtr)
    return
}

fun GlobalNext(tmp0: CharArray, tmp1: CharArray, svPtr: Msv) {
    Stubs.Mglobal(NEXT, tmp0, tmp1, svPtr)
    return
}

fun LocalGet(tmp0: CharArray, tmp1: CharArray, tmp2: CharArray?,
        svPtr: Msv) {
    if (Stubs.sym_(11, tmp0, tmp1, svPtr) == null) {
        if (tmp2 == null) CstringOps.strcpyS(tmp1, "")
        else CstringOps.strcpyC(tmp1, tmp2)
        return
    }
    return // ans in tmp1
}

fun GlobalGet(tmp0: CharArray, tmp1: CharArray, tmp2: CharArray?,
        svPtr: Msv) {
    if (Stubs.Mglobal(RETRIEVE, tmp0, tmp1, svPtr) == 0) {
        if (tmp2 == null) CstringOps.strcpyS(tmp1, "")
        else CstringOps.strcpyC(tmp1, tmp2)
        return
    }
    return // ans in tmp1
}

fun fullUnpad(key: CharArray, svPtr: Msv) {
    val tmp1 = CharArray(STR_MAX)
    val tmp2 = CharArray(STR_MAX)
    // static unsigned char delim[2] = {1, 0}
    val delim = CHAR_1

    // strtok(key, delim) seeds the first token; strtok(NULL, delim)
    // walks the remainders (stateful tokenizer rendered as the
    // manual in-order split - the visible token sequence is
    // byte-faithful)
    var tokens: MutableList<CharArray> = ArrayList()
    var startIdx = 0
    var i = 0
    while (i < key.size && key[i] != NUL) {
        if (key[i] == delim) {
            tokens.add(key.copyOfRange(startIdx, i))
            startIdx = i + 1
        }
        i++
    }
    if (startIdx < key.size && key[startIdx] != NUL) {
        tokens.add(key.copyOfRange(startIdx, i))
    }

    if (tokens.size > 0) {
        // vendor seed: strcatx(tmp1, strtok(key, delim))
        strcatx(tmp1, tokens[0])
        // vendor loop: per-remainder token copy, unpad, re-append
        for (t in 1 until tokens.size) {
            val tok = tokens[t]
            CstringOps.strcpyC(tmp2, tok)
            Stubs.unpad(tmp2, svPtr)
            strcatx(tmp1, tmp2)
        }
    }

    CstringOps.strcpyC(key, tmp1)
    return
}
