package doc

// Unit 151 (B5 tranche 3): include/mumpsc/compiler-globals-ext.h
// misc chunk (batch 5, complex stratum; units.csv row 151: loc
// 144, score 67.9, rank 251). The compile-side extern-oversight
// header: the struct types the compiler's pass machinery walks,
// and the function prototypes of the compiler-the pass owners.
// The extern VARIABLE roster is EDIT-ONLY closed in
// CompilerCarriers.kt (CompilerState seats - H-004 single
// owner); this file owns the header's three struct renders and
// the prototype/assert-roster ledger. All derived from the
// pristine vendor source this session (sha256 621deb...ffdbe).
//
// H-004 ownership notes on the header's idem partners: the
// interp-side twins the header externs in common with
// stateVector.h members keep their earlier per-organ carriers
// (setpiece/symlen on both organs; ErrChar/nstart on Msv;
// except's slots null until the Ztrap table unit lands).
// H-008: the nmes/txt malloc nodes render as GC'd class
// instances; the vendor free() walks are unreachable-in-JVM and
// ledgered at their owner units. H-017: the in keyword and
// out-of-Kotlin-keyword lexemes carry their in_ / suffixed
// renders at the CompilerState seats.

// compiler-globals-ext.h:56-59 struct nmes - the pushed
// compiler-name stack node (the fcn \u0024\u0024FUNCTION launch walk);
// next chains toward nstart (CompilerState.nstart seat holds
// the struct nmes* as the opaque Any? - the type lands here,
// batch-5 unit 151; the stateVector.h:72 seat note applies).
class Nmes {
    // char *name
    var name: String? = null
    // struct nmes *next
    var next: Nmes? = null
}

// compiler-globals-ext.h:61-65 struct txt - the label/text chain
// node the scanner drags (LabelsStart/TextStart/p1/p3 seats on
// CompilerState; unit 151).
class TxtEntry {
    // char *name
    var name: String? = null
    // int lnbr
    var lnbr: Int = 0
    // struct txt *next
    var next: TxtEntry? = null
}

// compiler-globals-ext.h:71 define CVARMAX
const val CVARMAX: Int = 100

// compiler-globals-ext.h:73-76 struct dcs - one declared-local
// row of the cvars table (CompilerState.cvars seat).
class Dcs {
    // char name[32]
    val name = CharArray(32)
    // int type
    var type: Int = 0
}

// ----------------------------------------------------------
// prototype-roster ledger (compiler-globals-ext.h:82-104 and
// :80). Each line of the header's prototype roster names its
// arm landing or its owning batch; no declaration stubs are
// raised (the header owns no bodies).
//
// :80 extern int cvar(char *) - the declared-local walk over
//   the cvars table; Stubs.cvar seat stands (compiler-parse.c:
//   2074 body, not a roster unit).
// :82 char *Extract2(unsigned char *) - LANDED (the extract2
//   render with its Extract2Static keep-first object).
// :83 char *Extract1(unsigned char *) - compiler-parse.c:2119
//   body; not yet a roster landing (batch owner noted for the
//   journal consult ledger; H-014 calls, if any, seat via the
//   future owner's file).
// :84 void ScanPastPostCond(long*, char*, int*) - declaration
//   fossil: no defining body exists anywhere in the vendor
//   tree (grep-verified); H-013 dead-prototype ledger.
// :85 int xindex(...) - LANDED xindex render (FindSym.kt).
// :86 void errmod_(int) - LANDED (Errmod.kt).
// :87 int readf_(unsigned char[], short) - LANDED (Readf_/readf
//   renders).
// :88 void inout(unsigned char[], short, unsigned char[]) -
//   LANDED (Inout.kt).
// :89 int pindex(...) - declaration fossil: no defining body
//   in the vendor tree (grep-verified); H-013 ledger.
// :90 void add(char*, char*, char*) - the compiler-expression
//   arithmetic concat push; arith.h:48 declares the library
//   twin (a distinct C symbol per arm; not yet a roster
//   landing).
// :91 int numcomp(char[], char[]) - LANDED (Numcomp.kt).
// :92 void linepad(int, char*) - compiler-sys1.c:949 body; not
//   yet a roster landing (emit-side right-pad helper).
// :93 void linepad2(char*) - compiler-sys1.c:963 body; not yet
//   a roster landing (seat: Stubs.linepad2, added by the
//   unit-207 fcninit render).
// :94 void fcninit(char*, long) - LANDED (Fcninit.kt).
// :95 void epi(short, long) - LANDED (Epi.kt).
// :96 void EP(int, short, long) - LANDED (CompilerMumpsMisc.kt).
// :97 int getstr1(FILE*, char[]) - LANDED (Getstr1.kt).
// :98 int LevCheck(...) - LANDED (CompilerMumpsMisc.kt).
// :99-100 int doty(...) - LANDED (Doty.kt).
// :101 void LocalArray(...) - LANDED (LocalArray.kt).
// :102 void GlobalArray(...) - LANDED (GlobalArray.kt).
// :103 int atTest(...) - LANDED (AtTest.kt).
// :104 void atCopy(int, unsigned char[]) -
//   compiler-mumps.c:1956 body; not yet a roster landing.
// ----------------------------------------------------------

// compiler-globals-ext.h:161-167 cod table (the opcode mnemonic
// ledger, comment-only in the vendor): cod208=208 comma,
// cod207=207 close, cod209=209 comma, cod210=210 colon,
// cod206=206 open (the CLOSE 207 twin of the Stubs roster's
// BifsMisc zfcn-support ledger comment; nothing to render).
