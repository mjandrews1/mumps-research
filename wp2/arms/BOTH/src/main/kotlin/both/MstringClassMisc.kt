package both

// Unit 149 (BOTH B5 T3, complex): include/mumpsc/mstringClass.h::
// <misc> (331 LOC residue). Batch_plan ordinal 149: complex, LOC
// 331, score 51.1. Content: the entire mstring class DECLARATION
// surface of the vendor header - the banner, the include guard, the
// constructors, the friend declarations, and the method and
// operator families. There are NO function bodies in this residual
// header chunk: every body lives in the libmpscpp.cpp TU (whose
// own residue chunks own them per the roster; the first of those,
// libmpscpp.cpp::<misc-5>, is this tranche's unit 154).
//
// THIS RENDER SEATS NOTHING NEW: per the T1 class_member_carrier
// discipline the mstring member surface stays carried by the
// package-level seats that pass receiver state explicitly; the
// member bodies that the misc-5 chunk renders (the comparison
// operator tails, the builtin service wrappers) reference this
// ledger. The private data members (string data; string name;) seat
// the String-receiver fold the arm already uses (T1 units 129/131/
// 133 precedent: the receivers pass data state explicitly).
//
// BOTH consults (find_rule before render, events logged):
//   h014 out_of_scope_call - consulted, ADAPTED (hit, not reuse):
//     a body-less declaration header has no call sites, so the
//     ledger below carries the owner/family names instead of TODO
//     stubs (the T1 residue precedent).
//   h017 naming - hit: Eval/Translate/Soundex/replace/decorate/
//     ends/begins/Assign/Concat/Reverse/ReadLine/Query/Qlength/
//     Qsubscript/FixGblRef/Justify/Fnumber/Length/Ascii/zBlanks/
//     Extract/Find/Piece keep vendor case at the future member
//     seats.
//   MISS class_member_carrier (recurrence grows): the mstring
//     class layout has no base rule representation; this render is
//     the layout owner but the arm's member seats stay
//     package-level (capture candidate promotion review at batch
//     close).
//
// Render: KDoc-only residue carrier (no runtime code emitted beyond
// the declaration ledger below).

internal object MstringClassMisc {
    // Residue ledger (include/mumpsc/mstringClass.h, banner dated
    // June 5, 2022; pristine vendor tree verified this session):
    // - 8 constructors: (), (const char *), (long), (int),
    //   (double), (float), (char), (const mstring &), (const string &)
    // - friend class global
    // - c_str() / s_str()
    // - friend Hash::operator=(mstring); friend global::Piece /
    //   SQL_Table
    // - mstring Eval()
    // - Translate(mstring, mstring), Translate(mstring), Soundex()
    // - int replace(mstring, mstring), decorate(mstring, mstring,
    //   mstring), ends(mstring), begins(mstring)
    // - Assign family: (char *), (global), (mstring), (string),
    //   (int), (long), (double)
    // - friend SQL_Connect / SQL_Format / SQL_Command
    // - Concat family: (char *), (global), (mstring), (string),
    //   (int), (long), (double)
    // - Reverse(); ReadLine(), ReadLine(FILE *), ReadLine(istream &)
    // - cast operators: int, float, double, char, short, long,
    //   unsigned long, unsigned int, unsigned char, unsigned short
    // - Pattern(mstring &), Pattern(string), Pattern(global),
    //   Pattern(const char *)
    // - Query(), Qlength() [NOTE: the CLASS-MEMBER int Qlength()
    //   is a different surface from this tranche's free-function
    //   unit 145 Qlength(interp-sym.cpp)],
    //   Qsubscript(int), FixGblRef(), Justify(int, int=-1)
    // - Fnumber(mstring, mstring), Fnumber(mstring)
    // - Length family: (mstring), (global), (string), (char *), ()
    // - Ascii(int), Ascii(); zBlanks(); Extract(int=1, int=-1)
    // - Find(const char *, int=1), Find(mstring, int=1),
    //   Find(global, int=1)
    // - Piece(const char *, int, int=-1), Piece(mstring &, int,
    //   int=-1), Piece(string, int, int=-1), Piece(global, int,
    //   int=-1)
    // - operator&(mstring / char * / global / string / int / long /
    //   double) - the string-concatenation ampersand
    // - operator[](mstring / int / const char * / string)
    // - operator^=(int / long / double / float / string / const
    //   char * / global) - the exponentiate family
    // - compound assignments += -= *= /= %= over int, long,
    //   double, float, string, const char *, mstring, global
    // - operator=(int / long / double / float / string / const
    //   char * / mstring / global)
    // - arithmetic + - * / % member forms over the eight operand
    //   types, with the friend forms for both operand orders
    // - ++ / -- pre and post; unary ! + -
    // - friend stream operators ostream << and istream >>
    // - comparison families < > >= <= == != over int, long, double,
    //   float, char *, const char *, string, global, mstring
    //   (the misc-5 chunk carries the <= / == / != member bodies
    //   and the < tail)
    // - operator const char * ()
    // - private: string data; string name;
}
