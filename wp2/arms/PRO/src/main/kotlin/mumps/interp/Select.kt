package mumps.interp

/**
 * PRO-arm translation of interp-select.cpp::<misc> (roster row 284,
 * batch 10; units.csv: loc 156, comment_ratio 0.088, pointer_marks
 * 32, control_marks 22, score 115.0, rank 281). Translated
 * independently from the pristine vendor source (mumps-14-may-2026
 * .tgz, sha256 621deb25...ffdbe), reference source: the residual
 * <misc> chunk of interp-select.cpp - the file header block (the
 * license banner, the pragma/#define residue, the #include
 * declarations and the Interpret/ScanParse/Eval extern decls) plus
 * the complete void _select(out, in1..in20, svPtr) body.
 *
 * The chunk reproduces the study tool's misc residue exactly (loc
 * 156, cr 0.088, ptr 32, ctrl 22, score 115.0 - tools/
 * select_study_subset.py load_units repro verified).
 *
 * void _select(unsigned char *out, unsigned char *in1, ...,
 *              unsigned char *in20, struct MSV *svPtr)
 *
 * The <dollar>Select evaluator: pairs (in1,in2)..(in19,in20) form
 * (condition, value) rungs; the first nonzero condition's value is
 * moved to out. A NULL value slot or a fully-fallen ladder is the
 * M4 abort.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param : printf("*** Error M4 ...") -> System.out
 *    (the C-library trio, AllocSV.kt precedent).
 *  - h004 one_owner       : the file-header residue and the extern
 *    declarations are ledgered here, not rendered (SymMisc /
 *    CppsysfuncH precedent); Interpret / ScanParse / Eval are DONE
 *    seats (their named units); __label_lookup/__text_function are
 *    the file-tail pointer stubs ledgered in SymMisc.kt (row 259).
 *  - h009 format_string   : the M4 message text is byte-exact
 *    (%d rides the LineNumber Long concatenation).
 *  - h012 unsigned_byte_domain: in1..in20 ride CharArray (USTR
 *    carriers). The odd condition slots in3..in19 are the NULL-
 *    guarded vendor slots and stay CharArray? (nullable); the
 *    value slots in2..in20 and the unguarded in1 are plain
 *    CharArray (the vendor derefs them blind - caller contract).
 *  - h013 dead_code        : the vendor guard bodies end in
 *    sigint(100) which never returns; the Kotlin render adds the
 *    explicit trailing return (the vendor would otherwise fall
 *    through into the NULL deref - unreachable in the vendor).
 *  - h014 out_of_scope_call: strmove is the InterpStubs seat;
 *    sigint(int) is the interp-sysfunc.cpp -> Sigint.kt seat;
 *    atol is atoiC (CstringOps.kt).
 *  - h015 fold            : the ten identical
 *    NULL-check/printf/sigint guard blocks render once as the
 *    private selectM4 helper (ConCheck precedent,
 *    compiler-parse.c), all ten call sites kept in vendor order.
 *  - h017 naming          : _select keeps the vendor spelling with
 *    a leading underscore (Kotlin allows it, bstrcat precedent for
 *    word bodies).
 *  - h018 preprocessor_conditional: the #define SQLITE / _INTERP_ /
 *    CSTR / USTR / intLong lines and the commented SYMDEBUG /
 *    wait.h residue are fossil text, carried as this ledger note.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - in1 is atol'd WITHOUT a NULL guard (the vendor ladder starts
 *    at the second rung's guards) - kept.
 *  - The final printf/sigint pair after in19 is the no-rung-left
 *    abort - kept in full.
 */

/**
 * The repeated M4 abort rung (printf + sigint(100)), rendered once
 * as a private helper (ConCheck precedent, compiler-parse.c). In
 * the vendor sigint(100) never returns, so the trailing return
 * below is the Kotlin rendering of that abort - vendor would
 * otherwise fall through into the NULL deref (h013 note).
 */
private fun selectM4(svPtr: Msv) {
    System.out.print("*** Error M4 - Select in or near line " +
        svPtr.LineNumber + "\n")
    sigint(100)
    return
}

fun _select(
    out: CharArray,
    in1: CharArray,
    in2: CharArray,
    in3: CharArray?,
    in4: CharArray,
    in5: CharArray?,
    in6: CharArray,
    in7: CharArray?,
    in8: CharArray,
    in9: CharArray?,
    in10: CharArray,
    in11: CharArray?,
    in12: CharArray,
    in13: CharArray?,
    in14: CharArray,
    in15: CharArray?,
    in16: CharArray,
    in17: CharArray?,
    in18: CharArray,
    in19: CharArray?,
    in20: CharArray,
    svPtr: Msv,
) {
    if (atoiC(in1) != 0) {
        strmove(out, 0, in2, 0)
        return
    }

    if (in3 == null) { selectM4(svPtr); return }
    if (atoiC(in3) != 0) { strmove(out, 0, in4, 0); return }
    if (in5 == null) { selectM4(svPtr); return }
    if (atoiC(in5) != 0) { strmove(out, 0, in6, 0); return }
    if (in7 == null) { selectM4(svPtr); return }
    if (atoiC(in7) != 0) { strmove(out, 0, in8, 0); return }
    if (in9 == null) { selectM4(svPtr); return }
    if (atoiC(in9) != 0) { strmove(out, 0, in10, 0); return }
    if (in11 == null) { selectM4(svPtr); return }
    if (atoiC(in11) != 0) { strmove(out, 0, in12, 0); return }
    if (in13 == null) { selectM4(svPtr); return }
    if (atoiC(in13) != 0) { strmove(out, 0, in14, 0); return }
    if (in15 == null) { selectM4(svPtr); return }
    if (atoiC(in15) != 0) { strmove(out, 0, in16, 0); return }
    if (in17 == null) { selectM4(svPtr); return }
    if (atoiC(in17) != 0) { strmove(out, 0, in18, 0); return }
    if (in19 == null) { selectM4(svPtr); return }
    if (atoiC(in19) != 0) { strmove(out, 0, in20, 0); return }
    System.out.print("*** Error M4 - Select in or near line " +
        svPtr.LineNumber + "\n")
    sigint(100)
}
