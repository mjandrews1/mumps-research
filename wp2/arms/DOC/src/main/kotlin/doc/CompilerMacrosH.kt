package doc

// Unit 227 (B8 tranche 2): include/mumpsc/compiler-macros.h::<misc>
// (batch 8, core stratum; LOC 104, score 24.8, rank 190; units.csv
// row 227). The compiler emitter macro header recomputed with the
// study-subset chunker this session (physical loc 104 = the
// roster row; the whole header is one misc unit - no function
// bodies). Consult H-004 hit-reuse: a pure disposition ledger,
// the unit-187/zfcns.h convention - every prototype and define
// names its landed seat, future owner, or prior anchor; no new
// consts are opened (H-004 single-owner; every value here that
// exists in landed renders stays where it is).
//
// Prototype ledger (the emitter entry points):
// - int set_code(int): LANDED - the unit-121 batch-4 render
//   (SetCode.kt).
// - int for_code(): the compiler-for.c owner; its banner row is
//   batch 5 (u191 CompilerForMisc-style ledger); the body row is
//   not yet rendered.
// - int error_code(): no roster row located in the committed
//   batches to date; future-batch owner (compiler-sys1 family).
// - int abtrx(): the compiler-abrtx.c family; abrtx itself is
//   the batch-2 landed unit-66-family render (Abrtx.kt) - the
//   macros.h spelling abtrx names the same owner.
// - int if_code(): LANDED - the batch-2 unit-126-family render
//   (IfCode.kt).
// - int lock_code(): LANDED - the batch-4 LockCode.kt render.
// - void copyright_notice(FILE *): LANDED - the batch-1 core
//   render (CopyrightNotice.kt family).
//
// Macro ledger (the stateful emitter macros; H-003 family):
// - INTERPRET: ALREADY SEATED - the CompilerState.interpret
//   member renders this exact macro (compiler-macros.h:39, the
//   five-sequential-fprintf stateful inline; the seat note lives
//   in CompilerCarriers.kt).
// - SymCheck / Sysout / Sysout_skip: per-unit consumer rule - no
//   chunk-level seats; the landed emitters inline their own
//   fprintf(Out,...) text and cite this ledger (Sysout is the
//   CompilerState.emit call shape; Sysout_skip the emit of the
//   bare newline).
//
// Define ledger, values verbatim, dispositions inline:
// - SWAP 30: ALREADY SEATED (InterpMisc.kt SWAP const; the
//   Interpret.kt SWAP_I note already names this file's line).
// - STORE 1 / RETRIEVE 0 / NEXT 2 / XNEXT 8 / GKILL 3 / INIT 6 /
//   GOPEN 7 / EMPTY 5 / CLOSE 4: the Carriers.kt Mglobal-code
//   consts seat these (RETRIEVE/STORE/GKILL/XNEXT/NEXT verbatim;
//   CLOSE 4 = the Sigint.kt CLOSE_SI anchor defines.h:87 - this
//   header's CLOSE 4 is a THIRD same-value define of that code,
//   no new seat; INIT/GOPEN/EMPTY ride the comment ledger - no
//   landed render consumes them today).
// - Error codes PARSE_ERROR 1 / ARG_LIST_ERROR 2 /
//   NO_GLOBAL_PERMITTED 3 / MEM_ERROR 4 / TOO_MANY_LHS 5 /
//   ABRTX 6 / KILL_DCL_ERROR 7 / NEXT_LINE 8 / BAD_LOCK 9: no
//   landed seats today (the libmpscpp.h error-code consts are a
//   DIFFERENT family - e.g. INVALID_CONVERSION 1 and
//   GLOBAL_NOT_FOUND 2 of Carriers.kt collide in VALUE with
//   PARSE_ERROR/ARG_LIST_ERROR but live on different owners;
//   comment ledger here, no cross-file seat claimed).
// - SET 0 / SET_STORE 1: the set_code argument codes; comment
//   ledger (the unit-121 render documents its own call sites).
// - Command chars JOB 'j' through CONTINUE 'b' (SET 's', MERGE
//   'm', GOTO 'g', WRITE 'w', IF 'i', NEW 'n', NEW1 'N', OPEN
//   'o', ZCMDS 'z', READ 'r', QUIT 'q', XECUTE 'x', FOR 'f',
//   HALT 'h', ELSE 'e', EXPORTS 'y', DO 'd', KILL 'k', CLOSE
//   'c', CONTINUE 'b'): the dispatch switch of the compiler
//   driver; per-unit consumer rule, the errmod_/zcommands emitters
//   cite the chars inline (comment ledger).
//
// No runtime seat: the chunk is a header (banner - the GPL block
// of the 2003-2024 family, the May 15 2024 date line - prototypes
// and defines only). Naming per H-017.
