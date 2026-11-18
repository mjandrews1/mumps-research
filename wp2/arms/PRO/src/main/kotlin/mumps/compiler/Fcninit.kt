package mumps.compiler

import java.io.Writer
import mumps.interp.cstringAt

/**
 * PRO-arm translation of compiler-fcninit.c::fcninit (B7 tranche
 * 2, unit 195, rank 230). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 39-83 (definition at
 * line 39).
 *
 * Identity verified against the study tool: compiler-fcninit.c::
 * fcninit, c, loc 41, comment_ratio 0.024, pointer_marks 13,
 * control_marks 4, score 43.9 - exact match to
 * wp1/study-subset/units.csv row 195 (batch 7). RESOLVES the
 * Stubs.kt fcninit h014 stub raised by unit 192 (ZcommandsCode.kt
 * m/f arms; same signature, call sites bind unchanged).
 *
 * fcninit(char *output_file, long pid): emits the generated-C
 * temporary-variable preamble - the concat/scratch pointer
 * declarations, the label/text lookup prototypes, the do-stack
 * mechanism, the console-default wiring and the dormant alarm
 * block.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro : Sysout(x)/fprintf(Out, ...) ->
 *    out.write(...) verbatim; Sysout_skip is fprintf(Out, "\n").
 *  - h012 unsigned_byte_domain: output_file/FunctionName ride
 *    byte-domain CharArrays (cstringAt reads).
 *  - h013 dead_store        : the output_file PARAMETER is never
 *    used by the vendor body (the INCLUDE emission keys on pid
 *    and FunctionName only) - kept in-signature with the note.
 *  - h014 out_of_scope_call : linepad2 raised as a Stubs.kt stub
 *    by this unit (owner compiler-sys1.c:963, not a corpus unit;
 *    the compiler-sys1.c::<misc> residue is batch-9 row 258).
 *  - h015 grammar_collision : pre-push scan clean - no backtick,
 *    double-underscore, em dash or raw dollar in this file; the
 *    emitted _doStack/_Refs identifiers are vendor C names in
 *    string literals.
 *  - h017 naming           : fcninit keeps the vendor name.
 *  - h018 reference_build_visibility: no ifdefs in this body.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The INCLUDE emission branches on strlen(FunctionName)==0
 *    (main vs function module file names: Main_.pid vs
 *    FunctionName_.pid - note the EXTRA underscore on the function
 *    form, vendor spelling kept).
 *  - The console-default comment carries the vendor's stray slash
 *    ("console default/") - kept verbatim.
 *  - The alarm block emits the dormant if(0) { signal(SIGALRM...)
 *    } shape exactly (cf. the u158 h013 note on the generated
 *    side).
 */

/** h002: vendor macro Sysout(x) is fprintf(Out, x). */
private fun Sysout(out: Writer, s: String) {
    out.write(s)
}

/**
 * vendor 39: void fcninit(char *output_file, long pid). The
 * output_file parameter is dead in the vendor body (h013 note).
 */
fun fcninit(output_file: CharArray, pid: Long) {
    val st = CompilerState
    val out = st.out

    Sysout(out, "//#++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n")
    Sysout(out, "//#+ Intermediate temporary variables used during execution of string expressions\n")
    Sysout(out, "//#++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n")

    out.write("\n") // Sysout_skip

    Sysout(out, "char * catx; // intermediate concat ptr\n")

    Sysout(out, "char *_ftmp = new char[STR_MAX]; /* intermediate temp */\n")
    linepad2("Temporary variables")
    if (cstringAt(st.FunctionName).isEmpty()) {
        out.write("INCLUDE Main_." + pid + "\n")
    } else {
        out.write("INCLUDE " + cstringAt(st.FunctionName) + "_." + pid + "\n")
    }

    Sysout(out, "unsigned char *_dummy = new unsigned char[STR_MAX]; /* temporary variable */\n")
    out.write("unsigned char *atmp =new unsigned char[STR_MAX]; /* temporary variable */\n")
    out.write("unsigned char *xgtmp = new unsigned char [STR_MAX]; /* temporary variable */\n")
    out.write("unsigned char *gtmpL = new unsigned char[STR_MAX]; /* temporary variable */\n")
    out.write("unsigned char *tmp = new unsigned char[STR_MAX]; /* temporary variable */\n")
    out.write("unsigned char *gtmp = new unsigned char [2*STR_MAX]; /* temporary variable */\n")

    out.write("int " + cstringAt(st.FunctionName) +
        "_label_lookup(char *); /* resolution of labels */\n")
    out.write("const char * " + cstringAt(st.FunctionName) +
        "_text_function(int);\n")

    out.write("static unsigned char _ReturnVal[STR_MAX]; /* function returns */\n")
    out.write("int _ArgCount=0; /* Nbr of args to extrinsic */\n")
    out.write("long _i,_jj,_j,_k,_new;\n")
    out.write("int _SymPush=0,f=0;\n")
    out.write("int g=0;\n")
    Sysout(out, "jmp_buf _doStack[128]; /* <do> return stack mechanism */\n")
    Sysout(out, "short   _doStackN[128]={0}; /* <do> return stack mechanism */\n")
    Sysout(out, "static int     _doStx=0; /* <do> return stack mechanism */\n")
    Sysout(out, "char _Refs[10][32];\n")
    Sysout(out, "int _Refsx=0;\n")
    out.write("\n")

    Sysout(out, "if (svPtr == NULL) svPtr = AllocSV();\n\n")

    Sysout(out, "_SymPush=0; // zap pushed tables \n\n")

    out.write("svPtr->in_file[5]=stdin; // console default/\n")
    out.write("svPtr->out_file[5]=stdout; // console default\n\n")

    out.write("strcpy(svPtr->UDAT,\"data.dat\");\n")
    out.write("strcpy(svPtr->UKEY,\"key.dat\");\n\n")

    out.write("if ( 0) { // no alarm if slice is zero\n")
    out.write("\tsignal(SIGALRM, SyncClock);\n")
    out.write("\tset_alarm(1);\n")
    out.write("\t}\n\n")

    return
}
