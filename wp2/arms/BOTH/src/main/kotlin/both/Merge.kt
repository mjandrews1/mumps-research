package both

// Unit 225 render: interp-sysfunc.cpp::Merge (roster 225,
// batch 8, 82 LOC). int Merge(unsigned char * left, unsigned
// char * right, struct MSV * svPtr) - the MERGE command
// engine: copies the top-level reference, builds the keyfix'd
// sibling keys (the >127 terminator scan, the SOH append
// fallback), then walks the XNEXT sibling chain storing each
// retrieved value under the left key with the swapped
// tmp3/tmp4 buffer dance.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h013 dead_store - hit: the SQLITE arm is EMPTY (#if
//     defined(SQLITE) / #endif with no body - kept as the
//     ledger note); the MDEBUG printf arms are compiled out
//     (ledgered, not rendered).
//   h014 out_of_scope_call - reused: Mglobal RETRIEVE/STORE/
//     XNEXT direct (the SQLITE routing seat); keyfixC direct
//     (the keyfix vendor seat, BothCRuntime:202).
//   h015 grammar-collision - reused: ASCII only, no
//     backticks, no raw comment dollars, trailing newline;
//     the FIX macro prefix drops (the 16-bit far-pointer
//     macro, empty in this build - ledgered).
//   h017 naming - reused: Merge keeps vendor name/case (the
//     MergeFlg CompilerGlobalsExt seat is the compiler-side
//     flag, distinct).
//   h007 char_buffer - reused: tmp1..tmp4 are STR_MAX scratch
//     (CharArrays); the strcpy/strlen folds ride writeC/
//     cStringOf.
//
// Quirks kept (ledgered):
//   - the first RETRIEVE result i is used only as "!= 0"
//     (the top-refs copy: RETRIEVE right -> tmp1, STORE left
//     -> tmp1 - a value copy, not a node copy).
//   - the >127 terminator scan walks BACKWARD from the end
//     (j=i-1 down to j>0); j==0 means NO high byte found and
//     appends SOH (1) then NUL at the tail (the two-arm
//     shape kept).
//   - if j!=0 the key is TRUNCATED at j+1 (tmp2[j+1]=0) - the
//     last high byte stays; kept exact.
//   - the strncmp prefix check breaks the loop BEFORE the
//     store (a sibling outside the right-prefix terminates -
//     kept order).
//   - the tmp3/tmp4 swap is a strcpy dance (no swap var -
//     kept via the two writeC calls).
//   - the vendor comment "// handle first case - may not
//     exist" rides the RETRIEVE != 0 arm; the final return is
//     0 (not a count).

// int Merge(unsigned char * left, unsigned char * right,
//           struct MSV * svPtr) - interp-sysfunc.cpp
internal fun Merge(left: CharArray, right: CharArray, svPtr: Msv): Int {
    val tmp1 = CharArray(STR_MAX)
    val tmp2 = CharArray(STR_MAX)
    val tmp3 = CharArray(STR_MAX)
    val tmp4 = CharArray(STR_MAX)

    // #ifdef MDEBUG / #if defined(SQLITE) arms: ledgered above.

    writeC(tmp2, cStringOf(right))
    writeC(tmp3, cStringOf(left))

    var i = Mglobal(RETRIEVE, tmp2, tmp1, svPtr) /* copy top refs */

    if (i != 0) Mglobal(STORE, tmp3, tmp1, svPtr)

    writeC(tmp2, cStringOf(right))
    i = cStringOf(tmp2).length

    var j = i - 1
    while (j > 0) {
        if (tmp2[j] > '\u007F') break
        j--
    }

    if (j != 0) {
        tmp2[j + 1] = '\u0000'
    } else {
        tmp2[i++] = '\u0001'
        tmp2[i] = '\u0000'
        j = i
    }

    keyfixC(tmp2)
    j = cStringOf(tmp2).length

    writeC(tmp3, cStringOf(left))
    i = cStringOf(tmp3).length

    var k = i - 1
    while (k > 0) {
        if (tmp3[k] > '\u007F') break
        k--
    }

    if (k != 0) {
        tmp3[k + 1] = '\u0000'
    } else {
        tmp3[i++] = '\u0001'
        tmp3[i] = '\u0000'
        k = i
    }

    keyfixC(tmp3)
    k = cStringOf(tmp3).length

    if (Mglobal(RETRIEVE, right, tmp1, svPtr) != 0) { // handle first case - may not exist
        writeC(tmp4, cStringOf(tmp3))
        Mglobal(STORE, tmp3, tmp1, svPtr)
        writeC(tmp3, cStringOf(tmp4))
    }

    while (true) {

        if (Mglobal(XNEXT, right, tmp1, svPtr) == 0) break

        // strcpy( FIX &tmp3[k], FIX &right[j]); - the tail
        // splice (the FIX macro drops, ledgered).
        var p = j
        var q = k
        while (right[p] != '\u0000') {
            tmp3[q++] = right[p++]
        }
        tmp3[q] = '\u0000'

        if (strncmpC(cStringOf(right), cStringOf(tmp2), j) != 0) break

        Mglobal(RETRIEVE, right, tmp1, svPtr)

        writeC(tmp4, cStringOf(tmp3))
        Mglobal(STORE, tmp3, tmp1, svPtr)
        writeC(tmp3, cStringOf(tmp4))
    }

    return 0
}
