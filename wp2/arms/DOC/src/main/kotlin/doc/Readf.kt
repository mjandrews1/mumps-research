package doc

// Unit 78 (B3 tranche 2): compiler-sys1.c::readf_ (batch 3, core
// stratum; LOC 23, score 13.3). The controller source loader: reads
// bytes from infile into the compiler-global pd1 (CompilerState
// carrier, compiler-globals-ext.h:43), packing each source line as a
// NUL-terminated segment (LINEFEED 10 writes the line terminator) and
// the double-NUL end-of-file twin on the first k<1 (EOF or an
// embedded NUL byte), recording the line count in pd1len (the new
// CompilerState seat, compiler-globals-ext.h:36). H-001 application:
// the FILE* infile maps to the java.io.Reader carrier of the input
// direction (input-side precedent; Writer for the output direction
// per the entry text). H-018 application: the reference build leaves
// NOTABS undefined, so the TAB-to-space arm is compiled out and
// ledgered here; the k>200 byte filter (dropping every byte above
// 200, kept verbatim), the CRTN 13 and CTRLZ 26 skips, and the long
// i/k/n locals with the Int getc reads follow the vendor verbatim
// (H-010 promotions: n Long seat per the vendor long int n). Bounds
// guard: the vendor trusts the controller that source files pack
// under PS1; the pd1 writes are carrier-guarded against overrun (the
// vendor would silently overflow, the render raises - H-007 the
// Extract2 bounds-walk precedent; divergence ledgered). The vendor
// long int i is a pure byte counter (dead after the loop test) and
// the n=1 start offsets pd1[0] as the empty leading segment - both
// kept. Naming per H-017
// is an H-015-referenced verbatim name, not a Kotlin keyword or
// shell-escape issue: no rename required).

fun readf_(infile: java.io.Reader): Int {
    var n: Long = 1
    var k: Int

    // vendor long int i is the dead for-loop counter (never read,
    // H-013 dead local; the loop renders as while(true))
    while (true) {
        k = infile.read()

        if (k > 200) continue

        // H-018: NOTABS undefined in the reference build config - the
        // "if (k=='\\t') k=' ';" TAB-to-space line is compiled out.

        if (k == CRTN || k == CTRLZ) continue

        if (k == LINEFEED.code) {
            writePd1(n, NUL)
            n++
            continue
        }

        if (k < 1) {
            val nn = n.toInt()
            CompilerState.pd1[nn] = NUL
            CompilerState.pd1[nn + 1] = NUL
            CompilerState.pd1len = (n - 1).toInt()
            return 1
        }

        if (k > 0) {
            writePd1(n, k.toChar())
            n++
        }
    }
}

private fun writePd1(n: Long, ch: Char) {
    val idx = n.toInt()
    if (idx >= CompilerState.pd1.size) {
        throw IllegalStateException(
            "readf_: pd1 carrier overrun (PS1) - the vendor overflow would be silent"
        )
    }
    CompilerState.pd1[idx] = ch
}
