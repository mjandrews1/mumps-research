package both

// Unit 244 render: compiler-set.c::<misc> (roster 244, batch 8, 32 LOC
// dump). The compiler-set.c TU banner-and-includes residue: the GPL
// notice, the June 26, 2024 date line, and the include set
// (stdio.h, mumpsc/memsize.h, mumpsc/compiler-macros.h,
// mumpsc/compiler-globals-ext.h) - and NOTHING else. Include
// ledger only (h004; the kill/lock twin is the identical shape);
// no bodies, no seats to raise (the TU globals ride the
// CompilerGlobalsExt object; the opcode macros ride the
// compiler-macros.h ledger, record 226).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h004 include_only - reused: the dump is banner + includes;
//     no observable behavior; the ledger keeps the include
//     order verbatim.
//   h015 grammar-collision - reused: ASCII only, no
//     backticks, no raw comment dollars, trailing newline.
//
// Quirks kept (ledgered):
//   - the compiler-set.c body carries no file-scope residue beyond the
//     banner (the kill/lock/set commandfamily shares this shape -
//     the kill/lock twins, records 242/243); the unit exists in the
//     roster for TU completeness.
