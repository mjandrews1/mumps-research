package doc

// Unit 149 (B5 tranche 3): include/mumpsc/mstringClass.h::<misc>
// (batch 5, complex stratum; units.csv row 149: loc 331, score
// 51.1, rank 234). The mstring class declaration header, whole-
// file residual by identity (no extracted functions in the
// header): the June 5 2022 "Mumps Bioinformatics Software
// Library" banner (2003, 2015 Kevin C. O'Kane GPL ledger; same
// surface as the unit-89 globalClass.h companion), the
// MSTRING_H include guard, and the full public declaration
// roster of class mstring. Declaration-only ledger, no
// executable code (unit-88/89 precedents). Roster kept by
// family (per the H-015 aggregate rule for duplicated-shape
// residue):
//   ctors - mstring(), (const char*), (long), (int), (double),
//     (float), (char), (const mstring&), (const string&) - the
//     Carriers.kt Mstring carrier renders the default and
//     String content ctors plus the private-name seat (EDIT of
//     this unit); the numeric ctors are conversion syntax sugar
//     over the string seat, ledger
//   friend class global (the GlobalRef carrier of the arm)
//   c_str() / s_str() - the carrier c_str() render IS the
//     const char* conversion witness (u120 SymOpsMisc note);
//     s_str() names the std::string extraction seat, ledger
//   friend Hash::operator=(mstring), global::Piece(mstring&,int,
//     int), SQL_Table(mstring,int) - named owners of the
//     existing renders/seats (Hash assign u31; GlobalPiece
//     unit-38 string seat; SQLTable unit-34)
//   Eval() - landed unit-79 render (MstringEval.kt)
//   Translate(mstring, mstring) / Translate(mstring) - the
//     one-arg member IS the landed unit-131 render
//     (MstringLength.kt neighboring GlobalTranslate.kt renders
//     the global member; the mstring::Translate members name
//     the batch-9 twin rows, ledger)
//   Soundex(), replace(mstring,mstring), decorate x3, ends(),
//     begins() - no owner unit in the corpus, ledger
//   Assign x7 (char*, global, mstring, string, int, long,
//     double) - the operator= backend family, ledger
//   friend SQL_Connect/SQL_Format/SQL_Command(mstring) - the
//     batch-10/11 SQL twin declarations, ledger
//   Concat x7 - the mstring:: Concat family, ledger (the
//     operator+ backend)
//   Reverse(); ReadLine(); ReadLine(FILE*); ReadLine(istream&)
//     - ledger (the FILE* pair rides the H-001 Reader domain
//     note of the arm)
//   conversion operators int(), float(), double(), char(),
//     short(), long(), unsigned long(), unsigned int(),
//     unsigned char(), unsigned short() - the byte/word
//     numeric conversion family (H-010): operator int/short/
//     long are the atoi domains, float/double the atof domain,
//     unsigned the same read over the unsigned carriers; the
//     bodies live in the libmpscpp.cpp chunks that own them
//     (batch 10 family), ledger here
//   Pattern x4, Query(), Qlength(), Qsubscript(int),
//     FixGblRef(), Justify(int,int=-1), Fnumber x2 - ledger;
//     the mstring::Fnumber(mstring,mstring) twin names the
//     unit-97 member family precedent
//   Length(mstring)/(global)/(string)/(char*)/() - the
//     mstring-needle member IS the landed unit-129 render
//     (MstringLength.kt names this member); the rest ledger
//   Ascii(int start) / Ascii() - ledger
//   zBlanks(); Extract(int=1, int=-1); Find x3; Piece x4 -
//     ledger
//   operator& x6, operator[] x4, operator^= x7, operator+= x8,
//     operator-= x8, operator*= x8, operator/= x8, operator%= x6,
//     operator= x8, operator+ x9, operator|| x8 + 7 friend
//     forms, operator- x9 + 8 friend forms, operator* x8 + 7
//     friend forms (ONE landed: operator*(string, mstring) is
//     the unit-120 SymOpsMisc render operator fun
//     String.times(k: Mstring)), operator/ x8 + 7 friend forms,
//     operator% x5 + 5 friend forms, operator++/-- x4, unary
//     operator!/+/- x3, operator<<(ostream,mstring),
//     operator>>(istream,mstring&) - the arithmetic and concat
//     families of the mstring LIBRARY backend, bodies owned by
//     the libmpscpp.cpp chunks (batch 7-10 family), ledger
//   comparison operators < > >= <= == != per-type x9 each -
//     the lt/gt ladders ARE landed (unit-120 SymOpsMisc word-
//     operator renders); the == and != ladders land with this
//     same tranche's unit-154 chunk render; the <= scalars and
//     the >= family name the batch-10 chunk that owns them,
//     ledger
//   operator const char*() - the c_str() conversion witness
//     (u120), ledger
//   private: string data; string name; - the Carriers.kt
//     EDIT-ONLY seat addition of this unit renders the private
//     name member (data is the existing str field; the EDIT is
//     the H-004 single-carrier note in Carriers.kt)
// No executable code is rendered; each ledger row cites its
// owner. Naming per H-017.

private const val MSTRING_CLASS_BANNER_DATE = "June 5, 2022"

private const val MSTRING_CLASS_BANNER_YEARS = "2003, 2015"

// include guard token (vendor residue, verbatim)
private const val MSTRING_H_GUARD = "MSTRING_H"
