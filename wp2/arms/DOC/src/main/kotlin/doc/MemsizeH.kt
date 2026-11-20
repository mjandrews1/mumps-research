package doc

// Unit 164 (B6 tranche 1): include/mumpsc/memsize.h misc chunk
// (batch 6, simple stratum; LOC 50, score 10.0). The compiler
// run-time support header: build-configuration define banner
// (the OS-detect macro cascade) and the global constants the
// compiler-side units read. Rendered as a file-private const
// roster with the arm ledger - the header has no function
// bodies (misc shape of the interp.h/stateVector.h renders).
// All derived from the pristine vendor source this session
// (sha256 621deb...ffdbe).
//
// Arm ledger (reference build config resolution, H-018):
// - PS 4096 / PS1 4097 carry the vendor FIXME ("These two
//   constants also appear in sysparms.h") - the study build
//   seats PS live from sysparms.h (32000: CompilerCarriers.kt
//   :305, the batch-3 render; PS1 = 32001 rides the same seat)
//   so this header's 4096 pair stays documentary here; the
//   FIXME quirk is the header's own ledger line.
// - SHOW_CODE 1 and VERSION 1000: no reader found anywhere in
//   the vendor tree this session (grep-clean) - seated
//   documentary.
// - DDAT "data.dat" / DKEY "key.dat": no reader found in the
//   vendor tree - seated documentary.
// - NOTABS: defined unconditionally, and compiler-sys1.c
//   includes this header at its line 45, so the line 431
//   ifdef NOTABS TAB-to-space arm in the unit-129 readf render
//   IS live - that render's "NOTABS undefined" compile-out
//   note was a config misread, amended in Readf.kt this
//   tranche with the vendor expression line restored.
// - FORPARM 32: the only textual references (compiler-epi.c
//   :171 and the compiler-mumps.c emitted define line) sit in
//   string literals the preprocessor never expands - the
//   generated programs re-define FORPARM STR_MAX themselves
//   (compiler-mumps.c:141); the 32 stays documentary here.
// - GCVT: defined only under the __APPLE__ arm of the OS
//   cascade - not defined on the Linux reference build; the
//   gcvt_wrapper selections in the interp/trans units follow
//   their own established H-018 resolutions at their owner
//   files, not this header.
// - OS-detect cascade (_MSC_VER/GnuDos arms): the reference
//   build reaches the Linux arm (UNIX 1, DOS 2, SYSTEM UNIX,
//   USYSTEM LINUX) - seated live; the Solaris/Watcom/GnuDos/
//   MSVisualC arm bodies stay documentary.
// - STEM 0, UDAT "data.dat", UKEY "key.dat", RELEASE: commented
//   fossils in the vendor, kept documentary verbatim.
// Naming per H-017 (macro-case constants keep their lexemes).

// memsize.h:33-34 - the sysparms.h FIXME pair (documentary; the
// build seats the sysparms.h values 32000/32001).
private const val PS_MEMSIZE: Int = 4096
private const val PS1_MEMSIZE: Int = 4097

// memsize.h:36-38 - SHOW_CODE 1, VERSION 1000, DDAT/DKEY
// (no vendor reader; documentary seats).
private const val SHOW_CODE: Int = 1
private const val VERSION: Int = 1000
private const val DDAT: String = "data.dat"
private const val DKEY: String = "key.dat"

// memsize.h:43 - defined; the compiler-sys1.c:431 arm is live.
private const val NOTABS: Boolean = true

// memsize.h:44 - FORPARM 32 (string-literal references only;
// documentary seat).
private const val FORPARM_MEMSIZE: Int = 32

// memsize.h OS-detect cascade, Linux arm (the reference build
// falls through the not-MSVC, not-GnuDos default).
private const val UNIX_MEMSIZE: Int = 1
private const val DOS_MEMSIZE: Int = 2
private const val SYSTEM_MEMSIZE: Int = 1 // = UNIX on the Linux arm
private const val USYSTEM_MEMSIZE: String = "LINUX"

// document the __APPLE__ GCVT arm: not defined on the Linux
// reference build (per the H-018 ledger above; GCVT is documentary
// here - the gcvt_wrapper macro arms live at their .cpp owners).
private const val GCVT_APPLE_ONLY: Boolean = false
