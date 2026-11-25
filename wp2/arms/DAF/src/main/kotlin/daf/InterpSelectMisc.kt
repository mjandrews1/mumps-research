package daf

// Unit 283 (DAF B10 T3, residue): interp-select.cpp::<misc> (units.csv
// row 283: 156 physical LOC): TU residue = the GPL banner + include
// deck (residue precedent: banner not emitted) + the _select
// runtime service, vendor :88. _select is the generated-code seat:
// CompilerParseC.kt:1138 emits the C call "_select((UNS) tmpN,
// (UNS) tmpI, ..., NULL pad, svPtr)" for the \u0024select()mvc of the
// parse-C arm; Interpret-side \u0024select (Fcn.kt case 8) is already
// seated - this is the LIBRARY twin, first binding, no stub.
// Consults (2026-11-25T15:03:00Z, nominal): row 6/22 hit-reuse
// (no file-scope state in _select; the TU's statics live in other
// seats), row 56 hit-adapt (atol pairs fold onto atolD), row 33
// hit-reuse (strmove folds onto the CharArray copy), row 97
// hit-reuse (printf -> System.out), row 92 hit-reuse (sigint(100)
// rides sigintD of the InterpSysfuncSigint render).
// VENDOR QUIRKS kept: (1) only the ODD inN args are NULL-checked
// (in2..in20 result slots are never checked; a non-null odd slot
// with a false test walks on); (2) the NULL checks are pointer
// guards - the C caller pads with NULL only BEYOND k>20 pad-outs,
// so a missing pair triggers the M4 banner + sigint(100), rendered
// as the null-check ladder; (3) after 10 pairs the fallthrough
// path also raises M4 (the vendor's final printf/sigint tail).
// Dafny: EXEMPT (atol-pair relay over 10 fixed slots; journal row).

// void _select(unsigned char *out, ..., unsigned char *in20,
//              struct MSV *svPtr)
fun selectDaf(out: CharArray,
              in1: String?, in2: String?,
              in3: String?, in4: String?,
              in5: String?, in6: String?,
              in7: String?, in8: String?,
              in9: String?, in10: String?,
              in11: String?, in12: String?,
              in13: String?, in14: String?,
              in15: String?, in16: String?,
              in17: String?, in18: String?,
              in19: String?, in20: String?,
              svPtr: DafMsv) {

    val tests = arrayOf(in1, in3, in5, in7, in9, in11, in13, in15, in17, in19)
    val res = arrayOf(in2, in4, in6, in8, in10, in12, in14, in16, in18, in20)

    // the 10 (test, result) pairs, vendor order (in pair i is the
    // 2*i+1-ary slot; the NULL checks guard the odd slots)
    for (i in 0 until 10) {
        val t = tests[i] ?: run { // if (in(2i+1) == NULL)
            System.out.printf("*** Error M4 - Select in or near line %d%n",
                svPtr.LineNumber)
            sigintD(100)
            return // (sigint is terminal; keep the C return shape)
        }
        if (atolD(t) != 0L) { // if (atol(in(2i+1)) != 0)
            strmoveSelect(out, res[i] ?: "")
            return
        }
    }

    // the vendor tail: 10 false pairs -> M4 banner + sigint(100)
    System.out.printf("*** Error M4 - Select in or near line %d%n",
        svPtr.LineNumber)
    sigintD(100)
}

// private strmove fold (target CharArray, source String)
private fun strmoveSelect(dst: CharArray, src: String) {
    var q = 0
    while (q < src.length && q < dst.size - 1) { dst[q] = src[q]; q++ }
    while (q < dst.size) { dst[q] = NUL_D; q++ }
}
