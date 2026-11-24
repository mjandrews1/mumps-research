package daf

// Unit 236 (DAF B8 T2, core): include/mumpsc/compiler-macros.h::
// <misc> (104 LOC, score 24.8): the compiler emitter macro header
// recomputed with the study-subset chunker this session (physical
// loc 104 = the roster row: the whole header is one misc unit, May
// 15 2024 date line; no function bodies - the seven prototypes
// name owner rows that render elsewhere).
// Consults (2026-11-24T21:20:00Z, nominal): a pure disposition
// ledger, the unit-187/zfcns.h convention - every prototype and
// define names its landed seat, future owner, or prior anchor; no
// new consts are opened (the single-owner rule; every value here
// that exists in landed renders stays where it is).
//
// Prototype ledger (the emitter entry points):
// - int set_code(int): LANDED - the unit-121 render
//   (SetCode.kt, setCodeDaf).
// - int for_code(): LANDED - the unit-122 render (ForCode.kt,
//   forCodeDaf).
// - int error_code(): no roster row located in the committed
//   batches to date; future-batch owner (the compiler-sys1
//   family).
// - int abtrx(): the compiler-abrtx.c family; not drawn on this
//   arm to date (the ABRTX_D error-code const below is the code
//   FAMILY define, not the entry point).
// - int if_code(): not drawn on this arm to date (its Latin-
//   square position never opened here).
// - int lock_code(): LANDED - the unit-116 render (LockCode.kt,
//   lockCodeDaf).
// - void copyright_notice(FILE *): not drawn on this arm to
//   date; the GPLv2 banner blocks of the banner chunks carry the
//   notice text itself.
//
// Macro ledger (the stateful emitter macros; the u207 fcninit
// pins):
// - INTERPRET: ALREADY SEATED - the emitInterpretDaf /
//   emitInterpretTailDaf members of SetCode.kt render this exact
//   macro (compiler-macros.h:39, the five-sequential-fprintf
//   stateful inline; the u121/u207 seat notes live there).
// - SymCheck: the Doty.kt:50 render inlines this exact macro (the
//   ExecSym[ExecX] -> _SymFree(1,svPtr) emit + the latch reset).
// - Sysout(x) == fprintf(Out,x) and Sysout_skip == the bare "\n":
//   pinned from this header at the unit-207 fcninit render (the
//   C.emit carrier call shape; the per-unit consumer rule - no
//   chunk-level seat).
//
// Define ledger, values verbatim, dispositions inline:
// - SWAP 30: ALREADY SEATED (Interpret.kt SWAP_I = 30, which
//   already names this file's line).
// - STORE 1 / RETRIEVE 0 / NEXT 2 / XNEXT 8 / GKILL 3 / INIT 6 /
//   GOPEN 7 / EMPTY 5 / CLOSE 4: the DafInterpStubsT3
//   Mglobal-code consts seat STORE/RETRIEVE/NEXT/KILL/XNEXT
//   verbatim; CLOSE 4 is the MumpsMainD CLOSE_D anchor
//   (defines.h:87 - this header's CLOSE 4 is another same-value
//   define of that code, no new seat); GKILL/INIT/GOPEN/EMPTY are
//   the same values as the KILL_D/INIT-family codes seated for the
//   Mglobal dispatch family (GKILL 3 == KILL_D 3); INIT/GOPEN/
//   EMPTY ride the comment ledger - no landed render consumes
//   them today.
// - Error return codes PARSE_ERROR 1 / ARG_LIST_ERROR 2 /
//   NO_GLOBAL_PERMITTED 3 / MEM_ERROR 4 / TOO_MANY_LHS 5 / ABRTX 6
//   / KILL_DCL_ERROR 7 / NEXT_LINE 8 / BAD_LOCK 9: SEATED at the
//   DafInterpStubsT3 vendor-consts region (PARSE_ERROR_D/
//   ARG_LIST_ERROR_D/NO_GLOBAL_PERMITTED_D/MEM_ERROR_D/
//   TOO_MANY_LHS_D/ABRTX_D/BAD_LOCK_D) and NEXT_LINE_D at
//   CompilerZcommandsCode.kt:49; KILL_DCL_ERROR 7 has no landed
//   consumer today (comment ledger; the libmpscpp.h error-code
//   consts are a DIFFERENT family - INVALID_CONVERSION_D 1 and
//   GLOBAL_NOT_FOUND_D 2 collide in VALUE but live on different
//   owners).
// - SET 0 / SET_STORE 1: SEATED (SET_CODE_SET_D/SET_STORE_D at
//   the DafInterpStubsT3 region).
// - Command letters SET 's' / NEW 'n' / NEW1 'N' / READ 'r' /
//   KILL 'k' / LOCK 'l': SEATED (CMD_SET_D/CMD_NEW_D/CMD_NEW1_D/
//   CMD_READ_D/CMD_KILL_D/CMD_LOCK_D); FOR 'f' SEATED at ForCode
//   (FOR_D); the JOB/GOTO/WRITE/IF/OPEN/ZCMDS/QUIT/XECUTE/HALT/
//   ELSE/EXPORTS/DO/CLOSE/CONTINUE/USE/CMMNT/BREAK/HTML letters
//   and the KILL2 20 / FCN 'D' / LBL 21 / CLSQ '}' / OPSQ '{' /
//   TRY 't' / CATCH 'c' codes ride the per-unit consumer rule
//   (no landed render consumes them today; comment ledger, no
//   cross-file seat claimed).
// - FBMAX 4000: SEATED (CompilerEpi.kt FBMAX_D, the unit-213
//   render).
// - TAB 9 (the non-NOTABS arm per the reference config): SEATED
//   (TAB_D, the defines-h family seat of DafInterpStubsT3).
//
// No runtime seat is opened by this chunk (banner, prototypes and
// defines only).
// Dafny: EXEMPT (declaration residue; journal row).
