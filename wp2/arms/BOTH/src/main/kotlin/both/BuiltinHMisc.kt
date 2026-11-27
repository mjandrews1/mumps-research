package both

// Unit 223 render: include/mumpsc/builtin.h::<misc> (roster 223,
// batch 8, 41 LOC dump; the builtin prototype ledger).
// builtin.h - the bioinformatics builtin prototype roster for
// the ServiceInterface dispatch. Include ledger only - no
// bodies (the owners are the libmpscpp service units; the
// ZfcnsHMisc/ExternsHMisc precedents). Entries in vendor order:
//
//   void Getenv(unsigned char *, unsigned char *);
//   char * Cosine (struct MSV *, char *, char *, char *);
//   char * Sim1 (struct MSV *, char *, char *, char *);
//   char * Dice (struct MSV *, char *, char *, char *);
//   char * Jaccard (struct MSV *, char *, char *, char *);
//   char * Avg (struct MSV *, char *, char *);
//   char * Count (struct MSV *, char *, char *);
//   char * Scan(struct MSV *);
//   // char * ScanAlnum(struct MSV *, char *); (superseded)
//   char * ScanAlnum(struct MSV *, char *, char * = NULL );
//   char * Max (struct MSV *, char *, char *);
//   char * Input (struct MSV *, char *, char *);
//   char * Min (struct MSV *, char *, char *);
//   char * Multiply (struct MSV *, char *, char *, char *,
//                    char *);
//   char * Sum (struct MSV *, char *, char *);
//   char * Transpose (struct MSV *, char *, char *, char *);
//   char * SmithWaterman (struct MSV *, char *, char * s1,
//         char *t1, char *sa, char *sm, char *gap, char *mm,
//         char *ma);
//   char * IDF (struct MSV *, char *, char *, char *);
//   char * TermCorrelate (struct MSV *, char *, char *,
//                         char *);
//   char * DocCorrelate (struct MSV *, char *, char *, char *,
//                        char *, char *);
//   char * StopInit (struct MSV *, char *, char *);
//   void   StopInit (char *);
//   char * StopLookup (struct MSV *, char *, char *);
//   int    StopLookup (char *);
//   char * SynInit (struct MSV *, char *, char *);
//   char * SynLookup (struct MSV *, char *, char *);
//   char * Centroid (struct MSV *, char *, char *, char *);
//   char * BMGSearch (struct MSV *, char *, char *, char *);
//   char * Shred (struct MSV*, char * ep, char *arg1,
//                 char *arg2);
//   char * ShredQuery (struct MSV*, char * ep, char *arg1,
//                 char *arg2);
//   char * Replace (struct MSV*, char * ep, char *arg1,
//                 char * arg2, char *arg3);
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h004 include_only - reused: prototypes carry no observable
//     behavior; the roster is the pin source for the service
//     renders (the InterpService *SvcZb stub seats cite their
//     owners against this ledger).
//   h014 out_of_scope_call - reused: the struct MSV forward
//     declaration (line 8 of the dump) has no seat to raise -
//     the Msv carrier is the BothCRuntime class.
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: the ledger keeps the vendor names and
//     the C default-argument / overload spellings verbatim (the
//     Kotlin render seats resolve them per owner).
//
// Quirks kept (ledgered):
//   - ScanAlnum is declared TWICE: the two-arg form is
//     commented out in favor of the three-arg form with a
//     default `char * = NULL` (the C++ default-argument quirk;
//     the ledger keeps both).
//   - StopInit and StopLookup are each declared as BOTH a
//     service form (struct MSV * first) and a plain form - C
//     overloads by arity; the ledger keeps both pairs.
//   - SmithWaterman's parameter names (s1/t1/sa/sm/gap/mm/ma)
//     are spelled in the prototype (unusual for this TU; kept).
//   - the #include <mumpsc/builtinTable.h> at the head ties
//     this ledger to the record 246 table render.
