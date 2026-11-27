package both

// Unit 241 render: include/mumpsc/zfcns.h::<misc> (roster 241,
// batch 8, 62 LOC; the header include ledger). bifs.h - Mumps
// runtime library: the built-in function prototypes and the
// ServiceInterface declaration. Include ledger only - the
// prototypes carry no bodies (the owners are the interp-zfcns/
// interp-bifs units; the ExternsHMisc precedent). The vendor
// block in declaration order, pins are zfcns.h lines:
//
//   void _ascii(unsigned char *, unsigned char *,
//                unsigned char *);
//   void _horolog(unsigned char *);
//   void _justify(unsigned char *, unsigned char *,
//                 unsigned char *, unsigned char *);
//   void _length(unsigned char *, unsigned char *,
//                unsigned char *);
//   void _piece(unsigned char *, unsigned char *,
//                unsigned char *, unsigned char *,
//                unsigned char *, int, unsigned char *,
//                struct MSV *);
//   void _random(unsigned char *, unsigned char *);
//
//   #ifndef _INTERP_
//   void zfcn(unsigned char *, unsigned char *,
//             struct MSV *);
//   #endif
//
//   void ServiceInterface(unsigned char *, unsigned char *,
//                         unsigned char *, unsigned char *,
//                         unsigned char *, char,
//                         struct MSV *);
//
//   // int BuildGlobal(int,int,unsigned char *, unsigned char *,
//   //                  struct MSV *);
//   // int BuildLocal(int,int,unsigned char *, unsigned char *,
//   //                 struct MSV *);
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h004 include_only - reused: the unit is header declarations
//     with no observable behavior; the ledger keeps the
//     declaration order and the commented BuildGlobal/
//     BuildLocal pair dead in the pristine tree (the record
//     232/231 owners land live bodies - this ledger is their
//     pin source).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: the prototype names stay verbatim in
//     the ledger (the _pieceZb/_ascii carrier pins live with
//     their owner renders; the leading-underscore family does
//     not collide with Kotlin keywords).
//
// Quirks kept (ledgered):
//   - the zfcn guard is #ifndef _INTERP_ (the compiler-side
//     export; the interp build defines _INTERP_ and hides it -
//     kept as the ledger note).
//   - BuildGlobal/BuildLocal are commented out in the vendor
//     (dead in the pristine tree; kept commented).
