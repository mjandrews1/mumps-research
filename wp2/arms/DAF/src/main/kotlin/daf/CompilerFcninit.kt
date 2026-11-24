package daf

// Unit 207 (DAF B7 T3, complex): compiler-fcninit.c::fcninit
// (41 LOC, score 43.9): the compiler's generated-function
// preamble EMITTER - the temporary-variable/decl block and
// console-state bootstrap written into the emitted C stream at
// function start. Pure emit-side node: every Sysout/fprintf(Out)
// appends verbatim emitted-C text onto the DafCompilerState
// writer (rows 2/26: Sysout(x) == fprintf(Out,x) and
// Sysout_skip == the bare "\n", pinned from vendor
// compiler-macros.h this session).
// Seats: linepad2Daf raised here (owner compiler-read.c
// linepad2; the CompilerGlobalsExtMisc pending-seat ledger names
// it; the linepadDaf ForCode kin); functionNameD raised (the
// compiler-globals pending-carrier ledger resolves at this
// consuming row; vendor FunctionName char*); pid rides the
// Long param. The output_file param is UNUSED by the body (kept
// in the signature verbatim, KDoc ledger).
// The emitted decl block is the SAME emitted-C text family the
// mpstime benchmark body executes (the unit-182 quirk ledger
// ticked the _ReturnVal/_ArgCount/_doStack decls comment-only on
// the RUNTIME side; HERE on the EMIT side the decls are the
// payload and render verbatim).
// Consults (2026-11-24T20:10:30Z, nominal): BASE-log rows 2/26
// hit-reuse (emit fold; emitted text verbatim), rows 6/22
// hit-reuse (FunctionName the pending-carrier fold to a
// top-level state var; the AllocSV bootstrap line is emitted TEXT
// here, not a runtime call), row 27 hit-reuse (the two
// fprintf(Out) INCLUDE arms with the strlen==0 ternary -
// format pieces substituted exactly; Locale.US immaterial, the
// pid is the only substituted item), row 37 hit-adapt (the
// linepad2D named-owner raise), row 33 hit-reuse (the
// char*/buffer decls emit as text; no Kotlin carriers raised -
// the emitted C is data, not code, to this arm),
// manual-decision (no BASE row: the Sysout vs fprintf(Out) macro
// split is preserved as call-site comments; the signal/set_alarm
// slice is EMITTED text (the if (0) guard is part of the
// generated program), unlike the unit-182 runtime fold).
// Dafny: EXEMPT (emit-side compiler plumbing; journal row).

// char *FunctionName - compiler-globals pending carrier, resolved
var functionNameD = ""

// void linepad2(char *) - named-owner stub (owner compiler-read.c
// linepad2; the CompilerGlobalsExtMisc ledger row; linepadDaf kin)
fun linepad2Daf(text: String): Unit =
    throw NotImplementedError(
        "linepad2: out-of-scope owner (compiler-read.c linepad2; not a corpus unit)")

// void fcninit (char *output_file, long pid)
fun fcninitDaf(outputFile: String, pid: Long) { // output_file unused (vendor-verbatim signature)

    DafCompilerState.emit("//#++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n")
    DafCompilerState.emit("//#+ Intermediate temporary variables used during execution of string expressions\n")
    DafCompilerState.emit("//#++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n")

    DafCompilerState.emit("\n")                          // Sysout_skip

    DafCompilerState.emit("char * catx; // intermediate concat ptr\n")

    DafCompilerState.emit("char *_ftmp = new char[STR_MAX]; /* intermediate temp */\n")
    linepad2Daf("Temporary variables")
    if (functionNameD.length == 0) { // if (strlen(FunctionName) == 0)
        DafCompilerState.emit("INCLUDE Main_." + pid + "\n")
    } else {
        DafCompilerState.emit("INCLUDE " + functionNameD + "_." + pid + "\n")
    }

    DafCompilerState.emit("unsigned char *_dummy = new unsigned char[STR_MAX]; /* temporary variable */\n")
    DafCompilerState.emit("unsigned char *atmp =new unsigned char[STR_MAX]; /* temporary variable */\n")
    DafCompilerState.emit("unsigned char *xgtmp = new unsigned char [STR_MAX]; /* temporary variable */\n")
    DafCompilerState.emit("unsigned char *gtmpL = new unsigned char [STR_MAX]; /* temporary variable */\n")
    DafCompilerState.emit("unsigned char *tmp = new unsigned char[STR_MAX]; /* temporary variable */\n")
    DafCompilerState.emit("unsigned char *gtmp = new unsigned char [2*STR_MAX]; /* temporary variable */\n")

    DafCompilerState.emit("int " + functionNameD + "_label_lookup(char *); /* resolution of labels */\n")
    DafCompilerState.emit("const char * " + functionNameD + "_text_function(int);\n")

    DafCompilerState.emit("static unsigned char _ReturnVal[STR_MAX]; /* function returns */\n")
    DafCompilerState.emit("int _ArgCount=0; /* Nbr of args to extrinsic */\n")
    DafCompilerState.emit("long _i,_jj,_j,_k,_new;\n")
    DafCompilerState.emit("int _SymPush=0,f=0;\n")
    DafCompilerState.emit("int g=0;\n")
    DafCompilerState.emit("jmp_buf _doStack[128]; /* <do> return stack mechanism */\n")
    DafCompilerState.emit("short   _doStackN[128]={0}; /* <do> return stack mechanism */\n")
    DafCompilerState.emit("static int     _doStx=0; /* <do> return stack mechanism */\n")
    DafCompilerState.emit("char _Refs[10][32];\n")
    DafCompilerState.emit("int _Refsx=0;\n")
    DafCompilerState.emit("\n")

    DafCompilerState.emit("if (svPtr == NULL) svPtr = AllocSV();\n\n")

    DafCompilerState.emit("_SymPush=0; // zap pushed tables \n\n")

    DafCompilerState.emit("svPtr->in_file[5]=stdin; // console default/\n")
    DafCompilerState.emit("svPtr->out_file[5]=stdout; // console default\n\n")

    DafCompilerState.emit("strcpy(svPtr->UDAT,\"data.dat\");\n")
    DafCompilerState.emit("strcpy(svPtr->UKEY,\"key.dat\");\n\n")

    DafCompilerState.emit("if ( 0) { // no alarm if slice is zero\n")
    DafCompilerState.emit("\tsignal(SIGALRM, SyncClock);\n")
    DafCompilerState.emit("\tset_alarm(1);\n")
    DafCompilerState.emit("\t}\n\n")
}
