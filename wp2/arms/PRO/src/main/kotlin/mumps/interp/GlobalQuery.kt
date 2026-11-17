package mumps.interp

/**
 * PRO-arm translation of interp-sym.cpp::GlobalQuery (B4 unit 98,
 * rank 269). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 987-1098.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-sym.cpp::GlobalQuery, cpp, loc 91,
 * comment_ratio 0.011, pointer_marks 28, control_marks 16, score
 * 89.3, batch 4 - exact match to wp1/study-subset/units.csv row
 * 269.
 *
 * GlobalQuery(tmp0, tmp1, tok, svPtrX): the dollar-query engine -
 * copies the state vector, parses the unevaluated reference in
 * tmp0, fetches the next global via Mglobal(XNEXT), and (when the
 * prefix matches) renders the next reference into tmp1.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro: printf -> System.out.print.
 *  - h007 char_buffer     : tmp0/tmp1 are NUL-terminated byte
 *    buffers (h012 domain); the malloc'd p1 becomes a local String
 *    view (free() dropped - GC).
 *  - h012 unsigned_byte_domain: v1d/tmp1 bytes 0-255; the 0x01
 *    subscript markers are Char(1).
 *  - h014 out_of_scope_call: AllocSV (state-vector machinery, not
 *    a corpus unit - InterpStubs stub raised by this unit);
 *    parse_ (DONE, unit 63 - direct call); keyfix (MdhStubs stub;
 *    the vendor passes and modifies and-v1d[1], carried via a
 *    local copy/write-back adapter); Mglobal (mglobalAt offset
 *    variant, InterpStubs stub).
 *  - h015 grammar_collision: pre-push scan incl. cross-file
 *    top-level names: new top-level GlobalQuery - no collisions;
 *    the vendor printf literal contains a dollar byte ("dollar-
 *    Query") - the dollar rides a file-private Char(36) constant
 *    (h015 action).
 *  - h017 naming          : GlobalQuery keeps the vendor name.
 *  - h018 preprocessor_conditional: the #if defined(SQLITE) block
 *    at vendor line 1023 is resolved against the reference build
 *    config (SQLITE defined) - the free+return arm is LIVE and
 *    translated; see the h018 paragraph at the end of this KDoc.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - memcpy(svPtr, svPtrX, sizeof(struct MSV)) is a whole-struct
 *    copy; the Kotlin carrier copies every Msv field into the
 *    AllocSV carrier (System.arraycopy for the arrays - the
 *    vendor's "copy old state vector - inefficient" comment kept).
 *  - svPtrX->LineNumber is saved before the copy and re-asserted
 *    after (vendor lines 995-997) - kept verbatim.
 *  - The v1d "no parens" fix-up appends three bytes (1,1,0) past
 *    the string end when the last subscript marker is missing
 *    (vendor 1013-1017) - kept exactly.
 *  - The scan loop "for (i=1; v1d[i] != 1 && v1d[i] != 0; i++);"
 *    runs twice (before and after the Mglobal fetch); its result
 *    feeds the strncmp prefix test - kept as the same variable.
 *
 * h018 preprocessor_conditional (vendor lines 1023-1027): after a
 * successful Mglobal(XNEXT,...) fetch the vendor has
 * "#if defined(SQLITE) - free(svPtr); free(p1); return; #endif".
 * The reference build config defines SQLITE, so this arm is LIVE:
 * GlobalQuery returns immediately after the fetch and the two
 * frees (dropped with the malloc/free - GC). The remaining vendor
 * code after the #endif (the strncmp(p1,tmp1,i) prefix test with
 * the empty-string fall-through, and the tmp3 reformat that
 * renders the result parenthesized/quoted for tok==0 or separated
 * by the caller's tok char otherwise) is compiled in every build
 * but is unreachable at runtime under the reference configuration
 * (the SQLITE arm returned); per the h018 action it is documented
 * here as the excluded path and not translated (MpsExp2/MpsExp10
 * precedent, units 40/41).
 */

private const val XNEXT_OP = 8 // vendor defines.h:88

/** vendor printf literal dollar byte (h015). */
private val DOLLARC = 36.toChar()

/** Whole-struct memcpy of the Msv carrier (vendor line 999);
 *  internal since unit 133 (Qlength reuses it - the whole-struct
 *  copy convention, one seat per file family avoided). */
internal fun msvCopyInto(dst: Msv, src: Msv) {
    System.arraycopy(src.program, 0, dst.program, 0, src.program.size)
    dst.io = src.io
    dst.symflg = src.symflg
    System.arraycopy(src.in_file, 0, dst.in_file, 0, 10)
    System.arraycopy(src.out_file, 0, dst.out_file, 0, 10)
    dst.tpx = src.tpx
    dst.LineNumber = src.LineNumber
    dst.gpad = src.gpad
    dst.order = src.order
    dst._Sym = src._Sym
    System.arraycopy(src.start, 0, dst.start, 0, 200)
    System.arraycopy(src.bd, 0, dst.bd, 0, src.bd.size)
    System.arraycopy(src.v1d, 0, dst.v1d, 0, src.v1d.size)
    System.arraycopy(src.xd, 0, dst.xd, 0, src.xd.size)
    dst.xpx = src.xpx
    System.arraycopy(src.pd1, 0, dst.pd1, 0, src.pd1.size)
    dst.NOERR = src.NOERR
    dst.ERROR = src.ERROR
    dst.ErrChar = src.ErrChar
    System.arraycopy(src.setname, 0, dst.setname, 0, src.setname.size)
    dst.kflg = src.kflg
    dst.ierr = src.ierr
    dst.sdlim = src.sdlim
    dst.t0px = src.t0px
    dst.pd1len = src.pd1len
    dst.t2 = src.t2
}

fun GlobalQuery(tmp0: CharArray, tmp1: CharArray, tok: Char, svPtrX: Msv) {
    var i: Int

    i = svPtrX.LineNumber.toInt()
    val svPtr = allocSV() // push statevector
    svPtr.LineNumber = i.toLong()

    msvCopyInto(svPtr, svPtrX) // copy old state vector - inefficient

    svPtr.t0px = 1
    svPtr.t2 = 1

    strcpyC(svPtr.xd, tmp0)

    svPtr.xpx = 0
    i = parse_(svPtr)

    if (i != 0) {
        System.out.print("** " + DOLLARC + "Query error\n")
        sigint(100)
    }

    // keyfix(&svPtr->v1d[1]) - in-place fix via a local
    // copy/write-back adapter (the MdhStubs keyfix stub takes a
    // whole buffer; the vendor's offset pointer semantics are
    // carried around the stub signature).
    val kf = CharArray(svPtr.v1d.size)
    var kn = 0
    var kk = 1
    while (svPtr.v1d[kk] != NUL) {
        kf[kn] = svPtr.v1d[kk]; kn++; kk++
    }
    kf[kn] = NUL
    mumps.mdh.keyfix(kf)
    kn = 0
    kk = 1
    while (kf[kn] != NUL) {
        svPtr.v1d[kk] = kf[kn]; kn++; kk++
    }
    svPtr.v1d[kk] = NUL

    // vendor: for (i=1; svPtr->v1d[i] != 1 && svPtr->v1d[i] != 0; i++);
    var ii = 1
    while (svPtr.v1d[ii] != Char(1) && svPtr.v1d[ii] != NUL) ii++

    // vendor: if ( svPtr->v1d[i=strlen(&svPtr->v1d[1])] != 1) - ^a case
    var ln = 0
    while (svPtr.v1d[1 + ln] != NUL) ln++
    if (svPtr.v1d[ln] != Char(1)) { // no parens
        ln++
        svPtr.v1d[ln] = Char(1)
        ln++
        svPtr.v1d[ln] = Char(1)
        ln++
        svPtr.v1d[ln] = NUL
    }

    // vendor: char *p1 = malloc+strcpy of and-v1d[1] - only consumed
    // by the excluded non-SQLITE tail (h018 paragraph above); the
    // allocation is dropped with this note (GC).

    ii = 1
    while (svPtr.v1d[ii] != Char(1) && svPtr.v1d[ii] != NUL) ii++

    if (mglobalAt(XNEXT_OP, svPtr.v1d, 1, tmp1, 0, svPtr) == 0) {
        writeCstring(tmp1, "")
        return
    }

    // h018 preprocessor_conditional: #if defined(SQLITE) - LIVE arm
    // (reference build config: SQLITE defined). free(svPtr)/free(p1)
    // drop with the malloc/free GC note; the excluded non-SQLITE
    // tail (prefix test + tmp3 reformat) is documented in the KDoc.
    return
}
