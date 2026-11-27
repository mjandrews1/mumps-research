package both

// Unit 194 render: compiler-fcninit.c::fcninit (roster 194,
// batch 7, 41 LOC; units.csv rank 230). The generated-program
// function prologue emitter: the temporary-variable
// declarations, the INCLUDE line keyed on FunctionName, the
// label/text function prototypes, the do-stack and Refs
// tables, and the console/signal defaults.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: linepad2 rendered here
//     (compiler-sys1.c:963 sys1 TU helper, NOT a corpus unit -
//     the ErrorMessage non-unit precedent: render, not stub);
//     the roster-191 fcninitZb TODO stub RELEASED at this
//     render (release note in Zcommands.kt; the two call sites
//     there now call fcninit direct). No other callables.
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars (the banner rides '#+'), trailing
//     newline.
//   h017 naming - reused: fcninit keeps its vendor name (the
//     released stub carried the Zb qualifier only for the
//     mechanical swap).
//   h019 class_member_carrier - reused: FunctionName rides the
//     CompilerGlobalsExt.FunctionName seat (:52); STR_MAX the
//     BothCRuntime const (strmax.h:3).
//   h013 dead_store - hit: the output_file parameter is NEVER
//     read in the vendor body (the INCLUDE emission keys on
//     FunctionName and pid only); the parameter shape is kept
//     with the fold journaled here.
//
// Quirks kept (ledgered):
//   - Sysout is the compiler-sys1.c:43 macro fprintf(Out,x);
//     Sysout_skip is compiler-macros.h:52 fprintf(Out,"\n").
//     No fcninit Sysout string contains a percent byte, so the
//     macro folds need no format processing.
//   - the '#+' banner separator is 82 '+' (counted) behind the
//     '//#' lead.
//   - the strlen(FunctionName)==0 arm emits 'INCLUDE Main_.%d'
//     and the else arm 'INCLUDE %s_.%d' - the same
//     empty-FunctionName quirk family the epi render journaled
//     (nme "Main_.pid").
//   - the emitted 'if (0) {' alarm block is dead generated
//     code by construction (the slice-zero guard); emitted
//     verbatim.

// fprintf(Out, ...) fold
private fun fiOutZb(s: String) {
    try {
        CompilerOutState.out.write(s)
    } catch (e: java.io.IOException) {
        // vendor ignores the fprintf return (failure is silent)
    }
}

// Sysout(x) macro fold - compiler-sys1.c:43
private fun fiSysoutZb(x: String) {
    fiOutZb(x)
}

// void linepad2(char *str) - compiler-sys1.c:963: moved to the
// unit-267 CompilerSys1Misc.kt render (BOTH B9); the call
// site below rebinds to it.
// void fcninit(char *output_file, long pid) - compiler-fcninit.c
internal fun fcninit(outputFile: CharArray, pid: Long) {
    val g = CompilerGlobalsExt

    fiSysoutZb("//#" + "+".repeat(82) + "\n")
    fiSysoutZb("//#+ Intermediate temporary variables used during execution of string expressions\n")
    fiSysoutZb("//#" + "+".repeat(82) + "\n")

    fiOutZb("\n") // Sysout_skip - compiler-macros.h:52
    fiSysoutZb("char * catx; // intermediate concat ptr\n")

    fiSysoutZb("char *_ftmp = new char[STR_MAX]; /* intermediate temp */\n")
    linepad2("Temporary variables")
    val fnName = cStringOf(g.FunctionName)
    if (fnName.isEmpty()) {
        fiOutZb("INCLUDE Main_." + pid + "\n")
    } else {
        fiOutZb("INCLUDE " + fnName + "_." + pid + "\n")
    }

    fiSysoutZb(
        "unsigned char *_dummy = new unsigned char[STR_MAX]; /* temporary variable */\n",
    )
    fiOutZb("unsigned char *atmp =new unsigned char[STR_MAX]; /* temporary variable */\n")
    fiOutZb("unsigned char *xgtmp = new unsigned char [STR_MAX]; /* temporary variable */\n")
    fiOutZb("unsigned char *gtmpL = new unsigned char[STR_MAX]; /* temporary variable */\n")
    fiOutZb("unsigned char *tmp = new unsigned char[STR_MAX]; /* temporary variable */\n")
    fiOutZb("unsigned char *gtmp = new unsigned char [2*STR_MAX]; /* temporary variable */\n")

    fiOutZb("int " + fnName + "_label_lookup(char *); /* resolution of labels */\n")
    fiOutZb("const char * " + fnName + "_text_function(int);\n")

    fiOutZb("static unsigned char _ReturnVal[STR_MAX]; /* function returns */\n")
    fiOutZb("int _ArgCount=0; /* Nbr of args to extrinsic */\n")
    fiOutZb("long _i,_jj,_j,_k,_new;\n")
    fiOutZb("int _SymPush=0,f=0;\n")
    fiOutZb("int g=0;\n")
    fiSysoutZb("jmp_buf _doStack[128]; /* <do> return stack mechanism */\n")
    fiSysoutZb("short   _doStackN[128]={0}; /* <do> return stack mechanism */\n")
    fiSysoutZb("static int     _doStx=0; /* <do> return stack mechanism */\n")
    fiSysoutZb("char _Refs[10][32];\n")
    fiSysoutZb("int _Refsx=0;\n")
    fiOutZb("\n")

    fiSysoutZb("if (svPtr == NULL) svPtr = AllocSV();\n\n")

    fiSysoutZb("_SymPush=0; // zap pushed tables \n\n")

    fiOutZb("svPtr->in_file[5]=stdin; // console default/\n")
    fiOutZb("svPtr->out_file[5]=stdout; // console default\n\n")

    fiOutZb("strcpy(svPtr->UDAT,\"data.dat\");\n")
    fiOutZb("strcpy(svPtr->UKEY,\"key.dat\");\n\n")

    fiOutZb("if ( 0) { // no alarm if slice is zero\n")
    fiOutZb("\tsignal(SIGALRM, SyncClock);\n")
    fiOutZb("\tset_alarm(1);\n")
    fiOutZb("\t}\n\n")
}
