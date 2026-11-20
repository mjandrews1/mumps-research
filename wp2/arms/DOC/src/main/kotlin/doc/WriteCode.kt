package doc

// Unit 178 (B6 tranche 3): compiler-write.c::write_code (batch 6,
// complex stratum; roster row 178: loc 63, score 45.1, batch_plan
// strata order). The WRITE command compiler, the read_code
// sibling: the NULL out_file guard emission with its
// ErrorMessage(...Write to input file..., m1944) pair, the @
// indirection arm through the atTest seat (unit 108, real), and
// the argument loop routing the carriage-control forms into the
// inout seat (unit 59, real) with the iosub codes (1 new page,
// 2 skip line, 3 tab, 4 text). Seat table: atTest is the arm
// AtTest.kt render; atCopy (owner compiler-at-test.c) and
// linepad (owner compiler-sys1.c::linepad) ride their Stubs
// seats (H-014); parse_ is the compiler twin Stubs seat; the
// INTERPRET macro is the CompilerState.interpret seat
// (compiler-macros.h:39, H-002/H-003); the td1 slice of the
// inout call (the vendor passes the same pd1-plus-sdlim
// pointer as both text and col) renders as per-call
// NUL-terminated slice copies via cstringAt holders (H-012
// pointer-carrier; the inout seat reads both from index 0).
// h011: the vendor goto iosub / goto iosub1 family is
// linearized with the toIosub/toIosub1 fall-through flags
// preserving exactly which arms reach inout - only the star
// arm skips it. Vendor quirks kept and noted: the bang arm
// enters iosub WITHOUT setting t2 (the vendor leaves t2
// unchanged on the carriage-control arms that jump straight
// to iosub); the star arm calls linepad TWICE with the
// fprintf char-count analog (the CompilerState.emit return,
// the compiler-if.c:70 shape) over the two atoi(CSTR...)
// and putchar emissions; xpx is re-pointed to at_flg in the
// indirection arm BEFORE the post-conditional 1920 closer;
// t0px resets ONCE outside the argument loop; the vendor
// blank-line runs inside the loop are dropped (H-013). The
// emitted CSTR tokens are generated-program runtime text
// (H-015 note). The per-file PARSE_ERROR..ABRTX cascade
// consts ride the arms convention (compiler-macros.h:69,74).
// Naming per H-017.

private const val PARSE_ERROR_WC = 1 // compiler-macros.h:69
private const val ABRTX_WC = 6 // compiler-macros.h:74

fun write_code(): Int {
    CompilerState.emit("\tif (svPtr->out_file[svPtr->io]==NULL) \n")
    CompilerState.emit(
        "\t\tErrorMessage(\"Write to input file\", " +
                "svPtr->LineNumber); // m1944 \n"
    )

    CompilerState.at_flg = atTest(
        CompilerState.xd,
        CompilerState.pd1,
        CompilerState.pd1Last,
        CompilerState.lines,
        CompilerState.xpx
    )

    if (CompilerState.at_flg == -1) return ABRTX_WC // goto abrtx;

    if (CompilerState.at_flg != 0) {
        // char tmp[1024]: the StringBuilder carrier (H-007, the
        // atCopy seat parameter shape is CharArray; the vendor
        // strcpy/atCopy pair rides the CharArray form)
        val tmp = CharArray(1024)
        CstringOps.strcpyS(tmp, "w ")
        Stubs.atCopy(CompilerState.at_flg, tmp)
        CompilerState.interpret(CstringOps.cstring(tmp))

        CompilerState.xpx = CompilerState.at_flg.toLong()

        if (CompilerState.POST_flg != 0) {
            CompilerState.emit("\t} /* post conditional 1920*/\n")
            CompilerState.POST_flg = 0
        }
        return 0 // goto next_cmnd;
    }

    CompilerState.t0px = 0

    while (true) {
        var i = 0
        var toIosub = false
        var toIosub1 = false

        if (CompilerState.xd[CompilerState.xpxIdx()] == '!' ||
                CompilerState.xd[CompilerState.xpxIdx()] == '#'
        ) { // new line or page
            i = 1
            if (CompilerState.xd[CompilerState.xpxIdx()] != '#') i = 2
            CompilerState.xpxInc()
            toIosub = true // goto iosub (t2 NOT set on this arm)
        } else if (CompilerState.xd[CompilerState.xpxIdx()] == '*') {
            // star output arm
            CompilerState.xpxInc()
            CompilerState.t2 = 1
            CompilerState.ierr = Stubs.parse_()
            if (CompilerState.ierr != 0) return PARSE_ERROR_WC // goto parse_err;
            Stubs.linepad(
                CompilerState.emit(
                    "\t_i=atoi( CSTR " +
                            CstringOps.cstringAt(CompilerState.pd1, CompilerState.sdlim) +
                            ");"
                ),
                "Convert to integer"
            )
            Stubs.linepad(
                CompilerState.emit("\tputchar(_i);"),
                "Direct output"
            )
            i = 4
            toIosub1 = true // goto iosub1 (skips inout)
        } else if (CompilerState.xd[CompilerState.xpxIdx()] == '?') {
            // tab arm
            CompilerState.t2 = 1
            CompilerState.xpxInc()
            CompilerState.ierr = Stubs.parse_()
            if (CompilerState.ierr != 0) return PARSE_ERROR_WC // goto parse_err;
            i = 3
            toIosub = true
        } else {
            CompilerState.t2 = 1
            CompilerState.ierr = Stubs.parse_()
            if (CompilerState.ierr != 0) return PARSE_ERROR_WC // goto parse_err;
            i = 4
        }

        if (!toIosub1) { // iosub: inout(&pd1[sdlim], i, &pd1[sdlim]);
            // H-012: the two pointer args carry as fresh
            // NUL-terminated slice copies
            val textSlice = sliceAt(CompilerState.pd1, CompilerState.sdlim)
            val colSlice = sliceAt(CompilerState.pd1, CompilerState.sdlim)
            inout(textSlice, i.toShort(), colSlice)
        }

        // iosub1:
        if (CompilerState.xd[CompilerState.xpxIdx()] == ',') {
            CompilerState.xpxInc()
            continue
        }

        if (CompilerState.xd[CompilerState.xpxIdx()] == NUL ||
                CompilerState.xd[CompilerState.xpxIdx()] == ' ' ||
                CompilerState.xd[CompilerState.xpxIdx()] == '\t'
        ) {
            if (CompilerState.POST_flg != 0) {
                CompilerState.emit("\t} /* post conditional 4092*/\n")
                CompilerState.POST_flg = 0
            }
            return 0 // goto next_cmnd;
        }
    }
}

// the &pd1[sdlim] pointer carrier (H-012): a fresh NUL-terminated
// copy of the tail of buf from offset; the shared-ptr aliases of
// the vendor cannot render (the slice is a value at the inout
// boundary; the seat reads it from index 0)
private fun sliceAt(buf: CharArray, offset: Int): CharArray {
    val out = CharArray(STR_MAX)
    CstringOps.copyAt(out, 0, buf.copyOfRange(offset, buf.size))
    return out
}
