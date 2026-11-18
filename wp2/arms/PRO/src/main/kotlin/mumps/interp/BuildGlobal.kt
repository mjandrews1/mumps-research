package mumps.interp

/**
 * PRO-arm translation of interp-parse.cpp::BuildGlobal
 * (batch-8 unit 233, units.csv data line 233, rank 150).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 1836-1878.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-parse.cpp::BuildGlobal, cpp, loc 27,
 * comment_ratio 0.036, pointer_marks 1, control_marks 8,
 * score 16.1, rank 150, batch 8 - exact match to
 * wp1/study-subset/units.csv line 233.
 *
 * BuildGlobal(code, g, str, rslt, svPtr): the $ORDER GLOBAL
 * stack - the exact twin of interp-sym.cpp::BuildLocal (unit
 * 232) with GlobalOrder in place of LocalOrder: code<0 resets,
 * code 0 pushes str, code 2 pops into rslt, code 1 pops one
 * frame and orders over it with the direction given by g.
 *
 * This unit RESOLVES the InterpStubs BuildGlobal h014 stub
 * (raised by unit 94 fcn with the offset shape; the Fcn call
 * sites bind unchanged).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch: static unsigned char tmp[TSTACK]
 *    [STR_MAX] and static int stk ride the BuildGlobalState
 *    object (TSTACK = 10, interp.h:35).
 *  - h012 unsigned_byte_domain: stack frames are Char(0..255)
 *    domains.
 *  - h014 out_of_scope_call: GlobalOrder (globalOrder.h inline
 *    stub, tmp1 the out buffer) and sigint (unit 191) reused
 *    unmodified.
 *  - h015 grammar_collision: top-level BuildGlobal (stub
 *    removed) - no collisions.
 *  - h017 naming      : BuildGlobal keeps the vendor name and
 *    the stub's parameter shape (a/g/key/arg4 offsets).
 *  - h018 preprocessor_conditional: none in this body.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor marks the fall-through arm with a "// code 1"
 *    comment (kept); the overflow/sigint banner matches
 *    BuildLocal's (kept).
 *  - The call sites at vendor 708/740 pass rslt = NULL on the
 *    push - the null-fold to a scratch buffer (journaled; the
 *    stub KDoc already documented NULL as null).
 *  - The commented GlobalNext alternative in the g == NEXT arm
 *    is kept as a comment.
 */

private const val TSTACK = 10 // vendor interp.h:35
private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val NEXT = 2 // vendor defines.h
private const val ORDERNEXT = 12 // vendor defines.h
private const val ORDERPREV = 13 // vendor defines.h

/** Carrier for the vendor statics (h008). */
private object BuildGlobalState {
    /** vendor: static unsigned char tmp[TSTACK][STR_MAX]. */
    val tmp = Array(TSTACK) { CharArray(STR_MAX) }

    /** vendor: static int stk = 0. */
    var stk: Int = 0
}

fun BuildGlobal(a: Int, g: Int, key: CharArray?, keyOff: Int, arg4: CharArray?,
                svPtr: Msv, arg4Off: Int = 0): Int {
    val st = BuildGlobalState

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

    // vendor: // code 1
    st.stk--

    // vendor: if (g == NEXT) GlobalOrder(tmp[stk],rslt,"1",svPtr);
    //         else if (g == ORDERNEXT) ... "1" ...
    //         else if (g == ORDERPREV) ... "-1" ...
    //         else return 0;
    if (g == NEXT || g == ORDERNEXT) {
        GlobalOrder(st.tmp[st.stk], 0, arg4 ?: EMPTY_RSLT_G, arg4Off, ONE_C_G, 0, svPtr)
    } else if (g == ORDERPREV) {
        GlobalOrder(st.tmp[st.stk], 0, arg4 ?: EMPTY_RSLT_G, arg4Off, MINUS_ONE_C_G, 0, svPtr)
    } else {
        return 0
    }

    return 1
}

private val ONE_C_G = CharArray(2).also { it[0] = '1' }
private val MINUS_ONE_C_G = CharArray(3).also { it[0] = '-'; it[1] = '1' }
private val EMPTY_RSLT_G = CharArray(STR_MAX)
