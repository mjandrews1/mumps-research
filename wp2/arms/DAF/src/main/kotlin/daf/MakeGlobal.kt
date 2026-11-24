package daf

// Unit 199 (DAF B7 T2, core): libmpscpp.cpp::global::MakeGlobal
// (38 LOC, score 14.6): the worker service that assembles a global
// node reference from a name plus a vararg subscript list: the
// int/char* two-arm loop keyed on flg builds both the buf carrier
// (strcatx chain) and the Sref display-carrier string; the Sref
// tail-comma is overwritten by the closing paren; the node's ref
// takes the buf build and the receiver returns by value.
// Consults (2026-11-24T16:11:30Z, nominal): BASE-log row 105
// hit-reuse (the C++ global node model: ctor/return-by-value fold;
// ref building), rows 6/22 hit-adapt (the file-scope statics Sref/
// Sname/ArgCount fold onto top-level Kotlin state on the row-62
// object-state discipline; Sref/Sname model the class-static
// mstring pair, ArgCount the extern int), row 37 hit-reuse (the
// count(key) service RAISED BELOW as the countDaf named-owner stub
// - owner libmpscpp.cpp:2932, not among this arm's drawn rows; the
// row-37 prototype discipline; the seat releases if a later DAF
// cycle draws the family), row 26 hit-reuse (sprintf(tmp,"%ld",ix)
// folds onto Locale.US digits per the unit-152 precedent), row 28
// hit-reuse (strcatxD the T2 inline-helper seat), rows 14/15
// hit-reuse (the strcmp "\"\"" empty-subscript sentinel test),
// manual-decision (no BASE row: the vararg flatten - the C va_list
// is typed by flg (long vs char*); the Kotlin vararg rides Any
// with the two-arm cast lattice the flg gate selects, the
// divergence noted; the vendor's commented-out strcat lines ride
// the KDoc note here, not re-emitted).
// Dafny: EXEMPT (reference assembly over seats; journal row).

// file-scope state (rows 6/22, the row-62 fold)
var srefD = ""     // mstring Sref class-static
var snameD = ""    // mstring Sname class-static
var argCountD = 0  // extern int ArgCount

// countDaf SEAT RELEASED (DAF B7 T3, unit 214): the real body
// (the comma/quote-aware subscript counter of libmpscpp.cpp:2932)
// landed in LibmpscppMisc6.kt as the residual cell of the
// <misc-6> roster row; the call site below resolves there.

fun makeGlobalD(flgIn: Int, gblnme: String, key: String, vararg rest: Any): MpsGlobalNode {

    var flg = flgIn
    val tmp = CharArray(32)
    val buf = CharArray(STR_MAX_DAF)

    srefD = gblnme      // Sref = gblnme;
    snameD = gblnme     // Sname = gblnme;
    srefD = srefD + "(" // Sref = Sref + "(";

    val j = countDaf(key.toCharArray()) // j = count(key);
    argCountD = j                        // ArgCount = j;
    buf[0] = NUL_D                      // strcpy(buf, "");
    strcatxD(buf, gblnme.toCharArray()) // strcatx(buf, gblnme);

    if (key == "\"\"") flg = 0 // if (strcmp(key,"\"\"")==0) flg=0;

    var i = 0
    while (i < j) { // for (i=0; i<j; i++)
        if (flg == 1) {
            // ix = va_arg(ap,long); sprintf(tmp,"%ld",ix);
            val ix = (rest[i] as Number).toLong()
            val ds = String.format(java.util.Locale.US, "%ld", ix)
            ds.toCharArray().copyInto(tmp, 0)
            strcatxD(buf, tmp)                    // strcatx(buf, tmp);
            srefD = srefD + ds                   // Sref = Sref + tmp;
            srefD = srefD + ","                  // Sref = Sref + ",";
        } else {
            // x = va_arg(ap,char *);
            val x = rest[i] as String
            strcatxD(buf, x.toCharArray())       // strcatx(buf, x);
            srefD = srefD + "\""                 // Sref = Sref + "\"";
            srefD = srefD + x                    // Sref = Sref + x;
            srefD = srefD + "\""                 // Sref = Sref + "\"";
            srefD = srefD + ","                  // Sref = Sref + ",";
        }
        i++
    }

    // strcpy(ref, buf): the receiver's ref takes the buf build;
    // Sref[Sref.length()-1] = ')';
    val refBuild = DafCRuntime.cstring(buf)
    if (srefD.isNotEmpty()) {
        val sb = StringBuilder(srefD)
        sb.setCharAt(sb.length - 1, ')')
        srefD = sb.toString()
    }

    return MpsGlobalNode(refBuild) // return *this;
}
