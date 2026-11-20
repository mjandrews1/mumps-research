package doc

// Unit 207 (B7 tranche 3): compiler-fcninit.c::fcninit (batch 7,
// complex stratum; units.csv row 207: loc 41, score 43.9). The
// emitted-C prologue generator: writes the temporary-variables
// banner and the intermediate string-pointer declarations whose
// text is emitted C source - each Sysout line is a compiler
// emission (compiler-macros.h:50 Sysout fprintf(Out,x), H-002
// local helper preserving call sites; Sysout_skip is
// compiler-macros.h:52 fprintf(Out,newline)), the fprintf(Out,...)
// percent formats render via String.format Locale.US (H-009),
// Out is the CompilerState seat (compiler-globals-ext.h carrier,
// H-004) and FunctionName the :52 extern char render
// (CstringOps.cstring read). The INCLUDE line branches on the
// empty FunctionName (Main_ default vs function-name include),
// both stamped with the pid suffix. STR_MAX is carriers.h
// (32000). The emitted signal/alarm block and the UDAT/UKEY
// strcpy emissions are the runtime template text kept verbatim
// (string content preserved exactly, the double quotes render as
// Kotlin escaped doubles). H-013: the output_file parameter is
// never read by the body (the Out carrier is the true sink -
// dead-param seat kept verbatim per vendor shape). H-010: the
// long pid is a Long; the strlen empty test is the strlenC read.
// linepad2 is the compiler-sys1.c:963 out-of-scope owner - the
// Stubs.linepad2 seat (below-render, H-014 seat-conversion
// precedent; this render ADDS the seat). Naming per H-017
// (fcninit kept verbatim).

fun fcninit(outputFile: String /* vendor char *output_file: unread,
              the Out carrier is the sink - H-013 dead-param seat;
              the String render rides the CompilerState.output_file
              seat precedent (unit-18 abrtx) at the unit-210 call
              sites. */,
            pid: Long) {

    // compiler-macros.h:50 Sysout(x) - fprintf(Out,x) local helper.
    fun Sysout(x: String) {
        CompilerState.Out.write(x)
    }

    // compiler-macros.h:52 Sysout_skip - fprintf(Out,newline).
    fun SysoutSkip() {
        CompilerState.Out.write("\n")
    }

    Sysout("//#++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n")
    Sysout("//#+ Intermediate temporary variables used during execution of string expressions\n")
    Sysout("//#++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n")

    SysoutSkip()
    Sysout("char * catx; // intermediate concat ptr\n")

    Sysout("char *_ftmp = new char[STR_MAX]; /* intermediate temp */\n")
    Stubs.linepad2("Temporary variables")

    val fn = CstringOps.cstring(CompilerState.FunctionName)
    if (fn.length == 0) {
        CompilerState.Out.write(String.format(
            java.util.Locale.US, "INCLUDE Main_.%d\n", pid))
    } else {
        CompilerState.Out.write(String.format(
            java.util.Locale.US, "INCLUDE %s_.%d\n", fn, pid))
    }

    Sysout("unsigned char *_dummy = new unsigned char[STR_MAX]; /* temporary variable */\n")
    CompilerState.Out.write("unsigned char *atmp =new unsigned char[STR_MAX]; /* temporary variable */\n")
    CompilerState.Out.write("unsigned char *xgtmp = new unsigned char [STR_MAX]; /* temporary variable */\n")
    CompilerState.Out.write("unsigned char *gtmpL = new unsigned char[STR_MAX]; /* temporary variable */\n")
    CompilerState.Out.write("unsigned char *tmp = new unsigned char[STR_MAX]; /* temporary variable */\n")
    CompilerState.Out.write("unsigned char *gtmp = new unsigned char [2*STR_MAX]; /* temporary variable */\n")

    CompilerState.Out.write(String.format(java.util.Locale.US,
        "int %s_label_lookup(char *); /* resolution of labels */\n", fn))
    CompilerState.Out.write(String.format(java.util.Locale.US,
        "const char * %s_text_function(int);\n", fn))

    CompilerState.Out.write("static unsigned char _ReturnVal[STR_MAX]; /* function returns */\n")
    CompilerState.Out.write("int _ArgCount=0; /* Nbr of args to extrinsic */\n")
    CompilerState.Out.write("long _i,_jj,_j,_k,_new;\n")
    CompilerState.Out.write("int _SymPush=0,f=0;\n")
    CompilerState.Out.write("int g=0;\n")
    Sysout("jmp_buf _doStack[128]; /* <do> return stack mechanism */\n")
    Sysout("short   _doStackN[128]={0}; /* <do> return stack mechanism */\n")
    Sysout("static int     _doStx=0; /* <do> return stack mechanism */\n")
    Sysout("char _Refs[10][32];\n")
    Sysout("int _Refsx=0;\n")
    CompilerState.Out.write("\n")

    Sysout("if (svPtr == NULL) svPtr = AllocSV();\n\n")

    Sysout("_SymPush=0; // zap pushed tables \n\n")

    CompilerState.Out.write("svPtr->in_file[5]=stdin; // console default/\n")
    CompilerState.Out.write("svPtr->out_file[5]=stdout; // console default\n\n")

    CompilerState.Out.write("strcpy(svPtr->UDAT,\"data.dat\");\n")
    CompilerState.Out.write("strcpy(svPtr->UKEY,\"key.dat\");\n\n")

    CompilerState.Out.write("if ( 0) { // no alarm if slice is zero\n")
    CompilerState.Out.write("\tsignal(SIGALRM, SyncClock);\n")
    CompilerState.Out.write("\tset_alarm(1);\n")
    CompilerState.Out.write("\t}\n\n")
}
