package doc

// Unit 144 (B5 tranche 3): include/mumpsc/sym.h::<misc> (batch 5,
// complex stratum; units.csv row 144: loc 39, score 34.3, rank 215).
// The whole-header residual: the A.D. 2001-2002-2017 Kevin C. O'Kane
// LGPL banner (kept verbatim below in the ledger), the sym.h module
// banner (Mumps Runtime Library - Mumps symbol table management),
// the include/using block, and the declaration surface of the
// interpreter symbol table. Header templates have no extracted
// functions, so the misc chunk IS the whole file (residual by
// identity; exact metrics match units.csv 39/0.466/11/3).
//
// H-004 (one owner per C entity): the struct stab carrier is the
// landed unit-28 render (SymFree.kt class Stab, sym.h:43-51 field
// anchors); no second carrier is raised here. The two extern
// globals of the file - none: sym.h declares only functions and
// the stab struct; every member below binds an existing render or
// a named out-of-scope owner seat. Ledger:
//   char *strsep(char **, char *delim)      - NEW H-014 stub seat
//     (Stubs.strsep; libc/interp-side strsep split helper, not a
//     roster unit; no in-arm call site today, seat raised because
//     the declaration is chunk content)
//   void sigint(int)                        - Stubs.sigint seat
//   struct stab                            - unit-28 Stab carrier
//   void GlobalName(unsigned char *, unsigned char, struct MSV *)
//                                           - unit-13 real render
//                                             (GlobalName.kt)
//   void fullUnpad(unsigned char *, struct MSV *)
//                                           - unit-25 real render
//                                             (SysfuncMisc.kt)
//   #ifdef _INTERP_ / #else / #endif pair (the keyfix declaration
//     comments) - BOTH arms are the same commented-out line, so
//     the conditional resolves EMPTY (H-018: the reference build
//     config takes the same observable state either way; the pair
//     is ledgered, no code either arm)
//   char *sym_(int, unsigned char *, unsigned char *, MSV *) - the
//     only LIVE duplicate declaration of the file's second half
//     (the same prototype appears twice in the vendor header,
//     once commented as "the line from mumps.c"); binds the
//     existing Stubs.sym_ seat (interp symbol routing owner)
//   int _SymFree(int, struct MSV *)        - unit-28 real render
//                                             (Stubs.SymFree seat
//                                             is the delegate)
//   void SysDump(long, struct MSV *)        - unit-142 real render
//                                             (SysDump.kt)
//   #include <mumpsc/arith.h>               - unit-57 ArithH.kt
//   void ErrorMessage(const char *, long)  - unit-25 real render
//                                             (Stubs.ErrorMessage
//                                             delegate)
//   int BuildGlobal(int,int,unsigned char *,unsigned char *, MSV *)
//                                           - Stubs.BuildGlobal seat
//     (batch-8 owner, correctly tagged)
//   BuildLocal (commented out in vendor)     - H-013 fossil, noted;
//     the separate live declaration in compiler-globals-ext.h owns
//     the Stubs.BuildLocal seat
//   void Qsub(unsigned char *, unsigned char *, unsigned char *, MSV *)
//                                           - Stubs.Qsub seat
//   void GlobalQuery(unsigned char *, unsigned char *, char, MSV *)
//                                           - unit-119 real render
//                                             (GlobalQuery.kt)
//   void ServiceInterface(unsigned char *, unsigned char *,
//        unsigned char *, unsigned char *, unsigned char *, char,
//        struct MSV *)                      - NEW H-014 stub seat
//     (Stubs.ServiceInterface; interp-side service entry, not a
//     roster unit; no in-arm call site today)
//   void Qlength(unsigned char *, unsigned char *, MSV *)
//                                           - the batch-5 roster
//                                             row 145 IS this
//                                             owner: the u145
//                                             render (Qlength.kt)
//                                             lands this tranche;
//                                             until that render
//                                             lands the callers
//                                             bind the Stubs
//                                             seat (raised by
//                                             unit 119), after it
//                                             the seat is the
//                                             delegate
//   void LocalName(unsigned char *, unsigned char *, unsigned char
//        *, MSV *)                          - unit-112 real render
//                                             (LocalName.kt)
//   int xindex(const unsigned char *, const unsigned char *, short)
//                                           - Stubs.xindex seat
//   void substr(unsigned char *, unsigned char *, int, int)
//                                           - Stubs.substr seat
//   void lpad(unsigned char *, unsigned char *, short)
//                                           - unit-141 real render
//                                             (StrmanipMisc.kt)
//   void _fnumber(unsigned char *, unsigned char *, unsigned char
//        *, unsigned char *, MSV *)        - unit-111 real render
//                                             (_fnumber seat)
// H-015: the C prototype text above is prose in comments; no
// comment-close sequence ends mid-line. H-017: the vendor names
// are kept verbatim in the seat names. No executable code is
// chunk content (the two new seats live in Stubs.kt per the
// arm's single Stubs owner rule).

private const val SYM_H_MISC_LEDGER =
    "sym.h residual: stab -> unit-28 Stab; declarations ledger per H-004/H-014"
