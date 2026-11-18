package mumps.interp

/**
 * PRO-arm translation of interp-sym.cpp::BuildLocal
 * (batch-8 unit 232, units.csv data line 232, rank 152).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 97-144.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-sym.cpp::BuildLocal, cpp, loc 29,
 * comment_ratio 0.065, pointer_marks 1, control_marks 8,
 * score 16.7, rank 152, batch 8 - exact match to
 * wp1/study-subset/units.csv line 232.
 *
 * BuildLocal(code, g, str, rslt, svPtr): the $ORDER local
 * stack - code<0 resets the stack pointer, code 0 pushes
 * str, code 2 pops into rslt, and any other code pops one
 * frame and orders over it via LocalOrder with the direction
 * given by g (NEXT/ORDERNEXT -> "1", ORDERPREV -> "-1").
 *
 * This unit RESOLVES the InterpStubs BuildLocal h014 stub
 * (raised by unit 94 fcn with the offset shape; the Fcn call
 * sites at :1106/:1135/:1154 bind unchanged).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch: static unsigned char tmp[TSTACK]
 *    [STR_MAX] and static int stk ride the BuildLocalState
 *    object (TSTACK = 10, interp.h:35; STR_MAX = 32000).
 *  - h012 unsigned_byte_domain: stack frames are Char(0..255)
 *    domains.
 *  - h014 out_of_scope_call: LocalOrder (InterpSysfuncMisc
 *    real seat) and sigint (unit 191) reused unmodified.
 *  - h015 grammar_collision: top-level BuildLocal (stub
 *    removed) - no collisions.
 *  - h017 naming      : BuildLocal keeps the vendor name and
 *    the stub's parameter names (a/g/key/arg4 offsets).
 *  - h018 preprocessor_conditional: none in this body.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The overflow arm prints the banner then calls
 *    sigint(100) (which exits) - the push is NOT skipped in
 *    the C source but is unreachable past sigint; kept as
 *    written (the JVM sigint exits too, unit 191).
 *  - The pop arm decrements stk BEFORE the frame use
 *    (stk-- then tmp[stk]) while the code-2 arm pre-decrements
 *    in the strcpy - both forms index the same frame; kept.
 *  - A code-2 pop on an empty stack writes a lone NUL into
 *    rslt and leaves stk at 0 (no underflow) - kept.
 *  - The commented-out "fff" debug printfs are kept out.
 */

private const val TSTACK = 10 // vendor interp.h:35
private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val NEXT = 2 // vendor defines.h
private const val ORDERNEXT = 12 // vendor defines.h
private const val ORDERPREV = 13 // vendor defines.h

/** Carrier for the vendor statics (h008). */
private object BuildLocalState {
    /** vendor: static unsigned char tmp[TSTACK][STR_MAX]. */
    val tmp = Array(TSTACK) { CharArray(STR_MAX) }

    /** vendor: static int stk = 0. */
    var stk: Int = 0
}

fun BuildLocal(a: Int, g: Int, key: CharArray?, keyOff: Int, arg4: CharArray?,
               svPtr: Msv, arg4Off: Int = 0): Int {
    val st = BuildLocalState

    if (a < 0) { // vendor: if (code<0) { stk=0; return 1; }
        st.stk = 0
        return 1
    }

    if (a == 0) { // push
        if (st.stk >= TSTACK) {
            println("*** Build Stack Overflow " + svPtr.LineNumber + "\n") // vendor banner
            sigint(100)
        }
        val k = key ?: CharArray(1) // vendor dereferences str unconditionally; null folds to a NUL scratch (adapter, non-null assertion avoided)
        writeCstring(st.tmp[st.stk], cstringAt(k, keyOff)) // vendor: strcpy(tmp[stk++],str);
        st.stk++
        return 1
    }

    if (a == 2) { // pop into rslt
        if (st.stk == 0) {
            if (arg4 != null) arg4[arg4Off] = NUL // vendor: rslt[0]=0;
        } else {
            st.stk--
            if (arg4 != null) {
                // vendor: strcpy(rslt, tmp[--stk]);
                val src = st.tmp[st.stk]
                var d = arg4Off
                var s = 0
                while (src[s] != NUL) {
                    arg4[d] = src[s]
                    d++
                    s++
                }
                arg4[d] = NUL
            }
        }
        return 1
    }

    st.stk-- // vendor: stk--;

    // vendor: if (g == NEXT) LocalOrder(tmp[stk],rslt,"1",svPtr);
    //         else if (g == ORDERNEXT) ... "1" ...
    //         else if (g == ORDERPREV) ... "-1" ...
    //         else return 0;
    if (g == NEXT || g == ORDERNEXT) {
        LocalOrder(st.tmp[st.stk], arg4 ?: EMPTY_RSLT, ONE_C, svPtr)
    } else if (g == ORDERPREV) {
        LocalOrder(st.tmp[st.stk], arg4 ?: EMPTY_RSLT, MINUS_ONE_C, svPtr)
    } else {
        return 0
    }

    return 1
}

private val ONE_C = CharArray(2).also { it[0] = '1' }
private val MINUS_ONE_C = CharArray(3).also { it[0] = '-'; it[1] = '1' }
private val EMPTY_RSLT = CharArray(STR_MAX)
