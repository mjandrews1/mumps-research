package mumps.compiler

/**
 * PRO-arm translation of compiler-write.c::write_code (B6 unit
 * 162). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 39-129.
 *
 * write_code(): the WRITE command compiler - the emitted C guards
 * against a NULL out_file, runs atTest to detect the indirection
 * (@) arm, then loops over the WRITE argument list emitting the
 * iosub codes (2 newline via '!'/1 page via '#', 3 tab via '?',
 * 4 text) and the '*' direct-output arm, closing on the comma
 * separator or a blank/NUL terminator (with the post-conditional
 * closer).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro : the INTERPRET macro
 *    (compiler-macros.h:39-44) -> the existing CompilerState
 *    .interpret(tmp) expansion seat; the linepad(fprintf(Out,...))
 *    pairs keep the vendor shape (emit, then linepad with the
 *    fprintf character-count analog).
 *  - h004 globals_ext        : at_flg/pd1Last/t2/t0px/POST_flg/
 *    xpx/sdlim/xd/pd1/ierr/Out ride CompilerState.
 *  - h007 char_buffer        : vendor char tmp[1024] with
 *    strcpy(tmp,"w ") -> a StringBuilder carrier (the atCopy
 *    seat's parameter type, Stubs.kt:23).
 *  - h011 goto               : the iosub/iosub1 label family
 *    linearized with fall-through flags (the iosub target runs
 *    inout then falls to iosub1; the '*' arm jumps straight to
 *    iosub1, skipping inout - ReadCode goCont precedent).
 *  - h012 unsigned_byte_domain : xd/pd1 C-string domains.
 *  - h014 out_of_scope_call  : atTest (AtTest.kt real seat, u108;
 *    the caller writes pd1Last back from the result echo),
 *    inout (Inout.kt real seat), atCopy (Stubs.kt stub, owner
 *    compiler-at-test.c), parse_ (Stubs.kt seat), linepad
 *    (Stubs.kt stub, owner compiler-read.c) - reused
 *    unmodified.
 *  - h015 grammar_collision  : new top-level writeCode plus a
 *    per-file pd1At slice helper (ReadCode precedent) - no
 *    collisions.
 *  - h017 naming            : write_code -> writeCode (vendor
 *    name in the header comment, LockCode/NewCode/readCode
 *    precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The iosub fall-through: the plain-text arm sets i=4 and
 *    falls DIRECTLY into iosub (no goto); the '!'/#'? arms goto
 *    iosub; only the '*' arm jumps past inout to iosub1 - the
 *    flag linearization preserves exactly which arms reach the
 *    inout call.
 *  - xpx = at_flg after the INTERPRET emit re-points the scan at
 *    the closing paren of the @(...) argument (atTest's return
 *    value) - kept.
 *  - linepad receives the fprintf RETURN VALUE (chars written);
 *    the analog passes the emitted string's length.
 *  - t0px = 0 happens ONCE before the loop (not per argument) -
 *    kept.
 *  - The emitted "CSTR" tokens are kept verbatim (they are the
 *    generated program's runtime text, not arm calls).
 */

private const val PARSE_ERROR = 1 // compiler-macros.h:69
private const val ABRTX = 6 // :74

/** &pd1[sdlim] pointer carrier: NUL-terminated slice copy (h012). */
private fun pd1At(): CharArray {
    val st = CompilerState
    val off = st.sdlim.toInt()
    var n = off
    while (st.pd1[n] != NUL) n++
    val b = CharArray(n - off + 1)
    for (k in 0 until n - off) b[k] = st.pd1[off + k]
    b[n - off] = NUL
    return b
}

fun writeCode(): Int {
    val st = CompilerState

    st.out.write("\tif (svPtr->out_file[svPtr->io]==NULL) \n")
    st.out.write("\t\tErrorMessage(\\\"Write to input file\\\", " +
        "svPtr->LineNumber); // m1944 \n")

    val r = atTest(st.xd, st.pd1, st.pd1Last, st.lines, st.xpx)
    st.at_flg = r.rc.toShort()
    st.pd1Last = r.pd1Last

    if (st.at_flg == (-1).toShort()) return ABRTX // goto abrtx;

    if (st.at_flg.toInt() != 0) {
        val tmp = StringBuilder(1024) // h007: vendor char tmp[1024]
        tmp.append("w ") // strcpy(tmp,"w ")
        atCopy(st.at_flg.toInt(), tmp)

        st.interpret(tmp.toString()) // h002: INTERPRET macro

        st.xpx = st.at_flg.toLong() // xpx = at_flg

        if (st.POST_flg.toInt() != 0) {
            st.out.write("\t} /* post conditional 1920*/\n")
            st.POST_flg = 0
        }
        return 0 // goto next_cmnd;
    }

    st.t0px = 0

    while (true) {
        val xpx = st.xpx.toInt()
        var i = 0

        var toIosub = false // h011: goto iosub / fall-through
        var toIosub1 = false // h011: goto iosub1 (skips inout)

        if (st.xd[xpx] == '!' || st.xd[xpx] == '#') { // new line or page
            i = 1
            if (st.xd[xpx] != '#') i = 2
            st.xpx++
            toIosub = true
        } else if (st.xd[xpx] == '*') { // star output
            st.xpx++
            st.t2 = 1
            st.ierr = parse_().toShort()
            if (st.ierr.toInt() != 0) return PARSE_ERROR // goto parse_err;
            val s1 = "\t_i=atoi( CSTR " + cstringAt(st.pd1, st.sdlim.toInt()) + ");"
            st.out.write(s1)
            linepad(s1.length, "Convert to integer")

            val s2 = "\tputchar(_i);"
            st.out.write(s2)
            linepad(s2.length, "Direct output")

            i = 4
            toIosub1 = true
        } else if (st.xd[xpx] == '?') { // tab
            st.t2 = 1
            st.xpx++
            st.ierr = parse_().toShort()
            if (st.ierr.toInt() != 0) return PARSE_ERROR // goto parse_err;
            i = 3
            toIosub = true
        } else {
            st.t2 = 1
            st.ierr = parse_().toShort()
            if (st.ierr.toInt() != 0) return PARSE_ERROR // goto parse_err;
            i = 4
            toIosub = true // vendor: falls directly into iosub
        }

        if (toIosub) { // iosub:
            inout(pd1At(), i.toShort(), pd1At())
        }
        // iosub1:

        val x1 = st.xpx.toInt()
        if (st.xd[x1] == ',') {
            st.xpx++
            continue
        }

        if (st.xd[x1] == NUL || st.xd[x1] == ' ' || st.xd[x1] == '\t') {
            if (st.POST_flg.toInt() != 0) {
                st.out.write("\t} /* post conditional 4092*/\n")
                st.POST_flg = 0
            }
            return 0 // goto next_cmnd;
        }
    }
}
