package doc

// Unit 89 (B3 tranche 3 / sub-push B): include/mumpsc/globalClass.h::
// <misc> (batch 3, complex stratum; LOC 485, score 27.9). The mstring
// class-global declaration header: the June 5, 2022 banner ("Mumps
// Bioinformatics Software Library" copyright 2003, 2015 O'Kane
// ledger), the GLOBAL_CLASS_H include guard, and the public
// declaration roster of class global. Declaration-only ledger, no
// executable code (CopyrightMisc unit-1, CppsysfuncMisc unit-76,
// InterpArithMisc unit-90 precedents). The roster is kept by family
// (the operator-assignment/copy families alone declare over a hundred
// per-type overloads, each family one ledger row, per the
// H-015 aggregate rule for duplicated-shape residue):
//   global(const char*) - constructor
//   MakeGlobal(int, char* gblnme, char* key, ...) - varargs name
//     builder (the Stubs.makeGlobal seat names this owner)
//   Translate(mstring, mstring) / Translate(mstring)
//   Soundex()
//   Concat - SEVEN overloads (string, global, char*, mstring, int,
//     long, double); the mstring-argument member IS the landed unit-65
//     render (GlobalConcat.kt, the GlobalRef.concat extension names
//     this class member); the other six stay declaration-only
//   Extract(int=1, int=-1)
//   Find(mstring, int=1) / Find(global, int=1) / Find(char*, int=1)
//   Ascii(int start=1) / Ascii()
//   zBlanks(), Query(), Qlength(), Reverse(), Qsubscript(int)
//   Pattern(mstring&) / Pattern(string) / Pattern(global) /
//     Pattern(const char*) - no owner unit in the corpus, ledger
//   Justify(int, int=-1)
//   Length() / Length(char*) / Length(mstring) / Length(global) /
//     Length(string)
//   Piece(const char*, int, int=-1) and three more overloads
//     (mstring&, global, ...)
//   Fnumber(mstring, mstring) / Fnumber(mstring) - the one-argument
//     member IS the landed unit-72 render (GlobalFnumber.kt,
//     the GlobalRef.fnumber extension names this class member)
//   ReadLine() / ReadLine(FILE*) / ReadLine(istream&)
//   friend class mstring
//   Flush(), Transpose(global), Multiply(global, global),
//     Centroid(global), Sum(), TermCorrelate(global) - the keyed
//     read-add-assign operator family rides the Stubs keyed seats
//     of unit 14 that name TermCorrelate
//   DocCorrelate(global, string="Cosine", double=0.8) and two more
//     overloads (char*, mstring) - the string arm default "Cosine"
//     names the u85 render (Cosine.kt) as the live correlation fn
//   Avg(), IDF(double), Count(), Max(), Min() - Max IS the landed
//     unit-68 render (Max.kt, rendering the class member; the
//     Stubs.globalMax seat names the libmpscpp.cpp::global::Max
//     owner referenced there)
//   TreePrint(int=1, const char=' ') / TablePrint(int=1, ...)
//   operator+() / operator-() (unary plus/minus)
//   operator& - six overloads (mstring, char*, global, string, int,
//     double-family)
//   operator= - eleven per-type assignment overloads
//   operator<< - eleven stream-in overloads
//   operator() - the subscript binder, FIFTY-FOUR overloads across
//     the no-arg, string (up to ten), mstring (up to ten) and
//     const char* (up to ten) families plus the long-int and
//     const-char* single forms
//   operator>> - eleven per-type extraction overloads
//   friend char* __Name(global, struct MSV*)
//   Int(), Double(), Char(char*, int), Mstring()
//   operator+= - eighteen per-type overloads plus eight friend forms
//   operator-=, operator*=, operator/= - per-type member and friend
//     families, shapes identical to the += family
// No executable code is rendered; each ledger row cites its owner
// (landed renders named, non-corpus declarations stay ledger rows).
// Naming per H-017.

private const val GLOBAL_CLASS_BANNER_DATE = "June 5, 2022"

private const val GLOBAL_CLASS_BANNER_YEARS = "2003, 2015"

// include guard token (vendor residue, verbatim)
private const val GLOBAL_CLASS_GUARD = "GLOBAL_CLASS_H"

// Declaration families kept by ledger row (verbatim order). The
// non-corpus, non-landed rows stay here only.
private val globalClass89Roster = listOf(
    "global(const char*)",
    "MakeGlobal",
    "Translate x2",
    "Soundex",
    "Concat x7",
    "Extract",
    "Find x3",
    "Ascii x2",
    "zBlanks",
    "Query",
    "Qlength",
    "Reverse",
    "Qsubscript",
    "Pattern x4",
    "Justify",
    "Length x5",
    "Piece x4",
    "Fnumber x2",
    "ReadLine x3",
    "friend mstring",
    "Flush",
    "Transpose",
    "Multiply",
    "Centroid",
    "Sum",
    "TermCorrelate",
    "DocCorrelate x3",
    "Avg",
    "IDF",
    "Count",
    "Max",
    "Min",
    "TreePrint",
    "TablePrint",
    "operator+",
    "operator-",
    "operator& x6",
    "operator= x11",
    "operator<< x11",
    "operator() x54",
    "operator>> x11",
    "__Name",
    "Int",
    "Double",
    "Char",
    "Mstring",
    "operator+= x18+8friend",
    "operator-=",
    "operator*=",
    "operator/=",
)
