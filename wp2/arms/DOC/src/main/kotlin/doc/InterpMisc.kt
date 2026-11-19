package doc

// Unit 19 (B1 tranche 3): interp-interp.cpp::<misc> (batch 1, ledger
// chunk; units.csv row: loc 67). Chunker boundary: the leading residue
// of interp-interp.cpp - the runtime banner (April 9, 2026, LGPL),
// the include ledger (iostream, the GTK/gdk windowing includes, the
// SQLITE and _INTERP_ defines, global/fcns/sysparms/keyfix), the
// define constants, the class InterpreterException declaration, and
// the prototype roster of the interpretation core. Definitions named
// by the roster live elsewhere: Interpret (2485 lines, not a unit)
// starts right after the chunker cut - this file owns only the
// residue here. Each roster entry below is carried as named constants
// and an owner-object class per H-004; the prototypes render as H-014
// stub seats on Stubs with their owners named (several owners are
// batch-1 units lander later - the seats convert to delegates when
// they land, the Extract2 precedent). The OPEN_PAREN define (206,
// macro line) names the same coded byte as the Carriers CodedOpen -
// per H-015 the constant is referenced, not redeclared. The CSTR/USTR
// cast macros carry no Kotlin analog (comment ledger only). Naming
// per H-017.

// interp-interp.cpp define GOSUBMAX
const val GOSUBMAX = 50
// interp-interp.cpp define SWAP
const val SWAP = 30
// interp-interp.cpp define FORMAX
const val FORMAX = 30
// interp-interp.cpp define EXECLEVS
const val EXECLEVS = 40
// interp-interp.cpp define LBLS
const val LBLS = 80
// interp-interp.cpp define OPEN_PAREN 206 - same coded byte as the
// Carriers CodedOpen (H-015 reuse, no redeclaration)

// interp-interp.cpp class InterpreterException (declaration only in
// this chunk): the constructor and methods are defined outside the
// chunk; the declaration shape (message member + what accessor) is
// kept, note the undefined-in-chunk constructor.
class InterpreterException {
    // vendor private char *message
    private var message: CharArray = CharArray(0)

    fun what(): CharArray = message
}
