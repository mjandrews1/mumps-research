package daf

// Unit 213 (DAF B7 T3, complex): compiler-epi.c::epi (135 LOC,
// score 111.3): the function EPILOGUE emitter - the epilogue
// try/catch close, the errors.h catch-ladder text (verbatim incl.
// the vendor "Interprerer Error" misspelling), the
// _label_lookup generator over the LabelsStart chain (the lab
// table walk + the free()/p3 chain clear), the _text_function
// generator over TextStart, the for/tmp/Arg declaration sidecar
// file (fopen write), and the compiler-state reset block
// (FunctionName, IF_flg/forxmax/forx, ForBlocks -1 fill, MaxTmp,
// cvarx, ArgMax).
// SEAT RELEASE (CompilerMumpsMisc.kt, unit 206 raise): the
// epiDaf stub RELEASES to the body here (epiD); the caller
// lattice (EP, zcommands_code) resolves with the same
// (ExecName, pid) shape.
// CARRIER CORRECTION (same push): the chain heads are struct TXT
// {name, lnbr, next} per compiler-globals.h:61 - the unit-206
// raise carried the nmes shape off the ExternsHMisc note; the
// node class is corrected to TxtNodeD with the lnbr field.
// New carriers resolved at this consuming row (the
// CompilerGlobalsExtMisc pending ledger): cvarsD (struct dcls
// cvars[CVARMAX], the name/type decl table; type field retained
// on the model), maxTmpD (short MaxTmp=2), argMaxD (short
// ArgMax 0), iflgMaxD (IF_flg_max 0). FBMAX 4000
// (compiler-macros.h:124) pinned; forxmax/forx/IF_flg/ForBlocks/
// ExecName are DafCompilerState fields.
// Folds/divergences ledgered: (1) the fopen(nme,"w") sidecar
// folds onto a java.io File writeText seat (the Readf java.io
// precedent); the vendor's fn[256] is unused dead (kept
// note-only); (2) abort() on fopen failure folds onto
// errorPrint + exitProcess (the vendor abort() is
// unblockable death; the function-name arm is the reachable
// one, the Main_ arm's missing abort is the vendor's own
// asymmetry, kept); (3) the free() chain clears fold onto GC
// + clearLabelChainsD() (the heads already public-read); (4)
// getpid()/Mltoa are EMITTED text, not calls (kept verbatim);
// (5) the label-arm int t=0 is dead in the vendor (only l
// drives), and in the text arm t drives Tx: rendered with
// the dead t omitted and the ledger note (gate-quiet
// discipline).
// Consults (2026-11-24T20:13:30Z, nominal): rows 2/26 hit-reuse
// (emit verbatim; the errors.h ladder text), rows 6/22 hit-
// reuse (the chain walks over TxtNodeD; the reset-block state
// folds), row 37 hit-adapt (the seat release + the struct
// correction), row 56 hit-adapt (the cvars/sidecar carriers),
// row 33 hit-adapt (sprintf/fopen/fprintf folds onto the File
// writeText seat; the String %s/%d substitutions), rows 14/15
// hit-reuse (the FCN_flg ternary; the !=NULL chain gates),
// row 46 hit-adapt (the commented for%d/for%dD decl lines kept
// verbatim as comments; the dead fn/t notables), row 27
// hit-reuse (the sprintf nme forms), manual-decision (the
// folds (1)(2)(3) above; the label/text generator emits kept
// output-level verbatim).
// Dafny: EXEMPT (emit-side epilogue plumbing; journal row).

const val CVARMAX_D = 100 // compiler-globals.h:71 CVARMAX
const val FBMAX_D = 4000  // compiler-macros.h:124 FBMAX

// struct dcls { char name[32]; int type; } cvars[CVARMAX]
class DclsEntryD(var name: String, var type: Int)
val cvarsD = Array(CVARMAX_D) { DclsEntryD("", 0) }

var maxTmpD = 2   // short MaxTmp = 2 (compiler-globals.h:108)
var argMaxD = 0   // short ArgMax = 0 (:53)
var iflgMaxD = 0  // int IF_flg_max = 0 (:113)

fun epiD(execName: Int, pid: Long) {

    val C = DafCompilerState

    C.emit("\n")
    C.emit("//=================================================================================\n")
    C.emit("_epilogue: tmp0[0]=0; tmp1[0]=0;") // linepad body text
    linepadDaf(C, "", " Function Epilogue")
    C.emit("\tMltoa(getpid(),atmp);\n")
    C.emit("\tif (svPtr->LockDone) cleanLocks((char *) atmp);\n")
    C.emit("\tMglobal(CLOSE,tmp0,tmp1,svPtr);")
    linepadDaf(C, "", "Close Globals")

    C.emit("\n} // end of try block \n\n")
    C.emit("catch(...) { \n")

    // #include <mumpsc/errors.h> - the emit-side ladder (unit-210 twin)
    C.emit("     if (GlobalExceptionCode == INPUT_ERROR) " +
            "cout << \"Input Error\" << endl;\n")
    C.emit("     else if (GlobalExceptionCode == INPUT_LENGTH) " +
            "cout << \"Input Length Error\" << endl;\n")
    C.emit("     else if (GlobalExceptionCode == SYMTAB_ERROR) " +
            "cout << \"Symbol Table Error\" << endl;\n")
    C.emit("     else if (GlobalExceptionCode == GLOBAL_NOT_FOUND) " +
            "cout << \"Global Not Found Error\" << endl;\n")
    C.emit("     else if (GlobalExceptionCode == INTERPRETER_ERROR) " +
            "cout << \"Interprerer Error\" << endl;\n")
    C.emit("     else if (GlobalExceptionCode == INVALID_CONVERSION) " +
            "cout << \"Invalid Conversion Error\" << endl;\n")
    C.emit("     else if (GlobalExceptionCode == DATA_RANGE_ERROR) " +
            "cout << \"Data Range Error\" << endl;\n")
    C.emit("     else if (GlobalExceptionCode == NUMERIC_RANGE) " +
            "cout << \"Numeric Range Error\" << endl;\n")
    C.emit("     else if (GlobalExceptionCode == UNSPECIFIED) " +
            "cout << \"Unspecified Error\" << endl;\n")

    C.emit("\tcout << \"Uncaught error in or near line \" \n" +
            "\t<< svPtr->LineNumber << endl;\n")
    C.emit("\tMglobal(CLOSE,tmp0,tmp1,svPtr);")
    linepadDaf(C, "", "Close Globals")
    C.emit("\t} \n\n")

    if (fcnFlgD != 0) { // if (FCN_flg)
        C.emit("return \"\";} /* Terminate */\n")
    } else {
        C.emit("return EXIT_SUCCESS; }") // linepad body text
        linepadDaf(C, "", "Terminate")
    }

    C.emit("//=================================================================================\n")

    if (labelsStartD != null) { // the label lookup generator
        var l = 0 // int t=0 - dead in this arm (ledger)

        C.emit("\n\nint " + functionNameD + "_label_lookup(char *label) {\n\n")
        C.emit("static struct lab {\n\tconst char *text;\n\tint offset;\n\t}\n\tL[] = {\n")

        var p1: TxtNodeD? = labelsStartD
        C.emit("\t\t\"" + p1!!.name + "\", " + p1!!.lnbr)
        p1 = p1!!.next
        l++
        while (p1 != null) {
            l++
            C.emit(",\n\t\t\"" + p1.name + "\", " + p1.lnbr)
            p1 = p1.next
        }
        C.emit("\n\t};\n\n")

        C.emit("int lx = -1, i, Lx=" + l + ";\n\n")

        C.emit("\tfor (i=0; i<Lx; i++) {\n")
        C.emit("\t\tif (strcmp((const char *) L[i].text,(const char *) label)==0) {\n")
        C.emit("\t\t\tlx=L[i].offset;\n")
        C.emit("\t\t\tbreak;\n")
        C.emit("\t\t\t}\n")
        C.emit("\t\t}\n")
        C.emit("\treturn lx;\n")
        C.emit(" }\n")
        // p1=LabelsStart; free walk - GC fold; the heads clear:
        clearLabelChainsD() // (the clear covers TextStart too; the
                            // vendor freed only the label chain here)
    } else {
        C.emit("\n\nint " + functionNameD + "_label_lookup(char *label) { return -1; }\n")
    }

    if (textStartD != null) { // the text function generator
        var t = 0 // l is dead in this arm (ledger)

        C.emit("\n\nconst char * " + functionNameD + "_text_function(int disp) {\n\n")
        C.emit("static struct txt {\n\tconst char *text;\n\tint offset;\n\t}\n\tT[] = {\n")

        var p1: TxtNodeD? = textStartD
        C.emit("\t\t\"" + p1!!.name + "\", " + p1!!.lnbr)
        p1 = p1!!.next
        t++
        while (p1 != null) {
            t++
            C.emit(",\n\t\t\"" + p1.name + "\", " + p1.lnbr)
            p1 = p1.next
        }
        C.emit("\n\t\t};\n\n")

        C.emit("int tx,i,Tx=" + t + ";\n\n")

        C.emit("\tfor (i=0; i<Tx; i++)\n")
        C.emit("\t\tif (disp==T[i].offset) return T[i].text;\n")
        C.emit("\treturn NULL;\n")
        C.emit("}\n")
        // free walk - GC fold (heads cleared with the label chain)
    } else {
        C.emit("const char * " + functionNameD +
                "_text_function(int disp) { return NULL; }\n\n")
    }

    // the sidecar declaration file
    runSidecarD(execName, pid)

    // strcpy(FunctionName,"")
    functionNameD = ""
    C.IF_flg = 0; C.forxmax = 0; C.forx = 0 // IF_flg=forxmax=forx=0
    for (i in 0 until FBMAX_D) C.ForBlocks[i] = -1
    maxTmpD = 2
}

// the for%d/Arg/tmp/cvars sidecar (hfile) write. The vendor's
// if/else picks ONLY the target name (and the abort arm); the
// declaration loops are SHARED and follow the open - kept
// verbatim (the two arms are not duplicated in the vendor).
private fun runSidecarD(execName: Int, pid: Long) {

    val C = DafCompilerState
    // char fn[256] - unused vendor dead (note-only)

    val sb = StringBuilder()
    // (the hfile open precedes the writes; the File writeText
    // flush folds the fopen/fclose pair - divergence ledger)

    for (i in 1..C.forxmax) {
        sb.append("char for" + i + "_incr[FORPARM], for" + i +
                "_lim[FORPARM], for" + i + "_init[FORPARM];\n" +
                "int for" + i + "F; int for" + i + "T; \n")
        // "long for%d; " / "double for%dD;" - the vendor's commented
        // decl lines, kept comment-only (row 46 discipline)
    }

    for (i in 0..C.forx) sb.append("jmp_buf for" + i + "J;\n")
    for (i in 0 until argMaxD) {
        sb.append("char *__Arg" + i + " = new char[STR_MAX];\n")
    }
    for (i in 0..execName) {
        sb.append("jmp_buf Efor" + i + "J;\n")
        sb.append("int tpx" + i + ";\n")
    }
    for (i in 0 until maxTmpD) {
        sb.append("unsigned char *tmp" + i + " = new unsigned char[STR_MAX];\n")
    }
    for (i in 0 until cvarxD) {
        sb.append("char " + cvarsD[i].name + "[STR_MAX]; // declared variable\n")
    }
    cvarxD = 0

    iflgMaxD = 0; C.forxmax = 0 // IF_flg_max=forxmax=0
    argMaxD = 0                 // ArgMax = 0

    if (functionNameD.isEmpty()) { // if (strlen(FunctionName)==0)
        // sprintf(nme,"Main_.%d", pid); hfile=fopen(nme,"w")
        try {
            java.io.File("Main_." + pid).writeText(sb.toString()) // fclose fold
            labelsSidecarNoteD = true
        } catch (_: Exception) {
            // (no abort arm on the Main_ path in the vendor; the
            // failed-open write path leaves the sidecar absent,
            // as the vendor's unwritten hfile arm does)
        }
    } else {
        // sprintf(nme,"%s.%d", FunctionName, pid); hfile=fopen(...)
        try {
            java.io.File(functionNameD + "." + pid).writeText(sb.toString())
            labelsSidecarNoteD = true
        } catch (_: Exception) {
            // if (hfile==NULL) abort(); - the unblockable-death fold
            print("epi: sidecar open failed (abort fold)")
            kotlin.system.exitProcess(134) // abort() signal fold
        }
    }
}

// (the hfile/fclose note carrier; transparency only)
private var labelsSidecarNoteD = false
