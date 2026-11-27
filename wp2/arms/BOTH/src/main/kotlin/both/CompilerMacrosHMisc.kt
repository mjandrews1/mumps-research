package both

// Unit 226 render: include/mumpsc/compiler-macros.h::<misc>
// (roster 226, batch 8, 130 LOC dump). The compiler macro
// header: the TU entry-point prototypes, the INTERPRET/SymCheck/
// Sysout/Sysout_skip macro family, the opcode/error-code define
// block, and the command-char block. Include ledger only - the
// macro family is CONSUMED as idioms at its call-site renders
// (the WriteCode atTest seat's five-fprintf INTERPRET emission,
// the Fcninit fiSysoutZb Sysout folds); the defines are seated
// at their consumers (pin map below).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h004 include_only - reused: prototypes and defines carry
//     no bodies; NO new const seats raised here - every define
//     is either already seated (pins below) or rides its
//     owner's render (the h014 discipline).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline; the macro text
//     keeps the vendor escapes verbatim in comments.
//   h017 naming - reused: INTERPRET/SymCheck/Sysout/Sysout_skip
//     keep vendor names in the ledger (the Kotlin seats are the
//     call-site fold helpers, e.g. fiSysoutZb; the outp()
//     five-fprintf family).
//
// Prototype owner ledger (compiler-macros.h:29-36): set_code
// (compiler-set.c, the record 244 TU), for_code (compiler-for.c),
// error_code (compiler-err.c), abtrx (compiler-abort.c),
// if_code (compiler-if.c), lock_code (compiler-lock.c, the
// record 243 TU), copyright_notice(FILE*) (the
// CompilerCopyrightNotice render, the DAF supp-4 family).
//
// Macro family ledger:
//   INTERPRET - the five-fprintf emission: _jj = Interpret
//     header, the quoted tmp argument, the svPtr->ERROR store,
//     the NOERR guard, the ErrorMessage("Indirection error",
//     svPtr->LineNumber) line. Consumed at the WriteCode
//     atTest seat (the record 240 release chain).
//   SymCheck - if (ExecSym[ExecX]) { _SymFree emission;
//     ExecSym[ExecX]=0 } - the symbol-layer fold at the do
//     command renders.
//   Sysout(x) - fprintf(Out,x); the fiSysoutZb fold precedent.
//   Sysout_skip - fprintf(Out,"\n"); the fiOutZb fold.
//
// Define pin map (seated): SWAP 30 / STORE 1 / RETRIEVE 0 /
// NEXT 2 / XNEXT 8 (BothCRuntime:281 defines.h pins) / GKILL 3
// (the ServerMain KILL=3 seat - the sym_ family twin) / INIT 6 /
// GOPEN 7 / EMPTY 5 / CLOSE 4 (BothCRuntime:285); PARSE_ERROR 1
// ... BAD_LOCK 9 (the error-code block: consumed at the
// errmod_/error_code owners; ABRTX 6 pinned at the WriteCode
// atTest seat release note); SET 0 / SET_STORE 1 (the
// compiler-set entry points); the command chars JOB 'j' (the
// InterpHMisc:66 seat) ... CATCH 'c' (consumed at the command
// owners); KILL2 20 / FCN 'D' / LBL 21; FBMAX 4000 (the
// CompilerGlobalsExtMisc:52 seat, this header's :124 pin).
//
// Quirks kept (ledgered):
//   - TAB is 9 under the default build and ' ' only under
//     #ifdef NOTABS (the ledger keeps the ifdef arm).
//   - BREAK and CONTINUE share the 'b' command char (kept; the
//     dispatch disambiguates by context).
//   - CLOSE is both a command char ('c') and an Mglobal opcode
//     (4) in this one header (kept - the two blocks).
//   - the define block re-declares STORE/RETRIEVE/NEXT/XNEXT/
//     CLOSE identically to defines.h (the compiler TU's own
//     macro copy; the record 219 ledger twin).
