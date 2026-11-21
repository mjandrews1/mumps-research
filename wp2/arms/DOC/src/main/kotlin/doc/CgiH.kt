package doc

// Unit 275 (B9 tranche 3): include/mumpsc/cgi.h::<misc> (batch
// 9, complex stratum; units.csv row 255: loc 78, score 14.2,
// rank 440; physLOC 78 physically verified this session) - the
// header-embedded CGI bootstrap block (decode arguments passed
// to processor from command line or environment; the
// install-default-program banner). Consult H-014 hit-adapt
// (2026-11-22T05:37:00Z): the body is a HEADER anonymous block
// spliced into the generated main of the CGI build family (no
// C function name; the H-016 header-named-BLOCK seat names the
// render cgiHMisc per the file: cgi.h). The getenv trio rides
// System.getenv (the OS environment consult; no corpus unit
// defines getenv - the C library idiom fold of the
// LibmpscppMisc7/LibMisc1 precedent); the sym_(1[1],...) store
// ops and the sym_(11,...) retrieve op ride the Stubs.sym_ seat
// (opcode 0/11 family; unsigned-char args carry on the
// CharArray seat, the vendor casts kept as the comment
// ledger); QS_MAX/QS_MAX1 are header defines rendered as
// private consts (H-004 banner-pull verbatim; the OS2/_
// envargs #if SYSTEM==OS2 dead arm kept in the ledger, the
// vendor fossil comment preserved in-situ).
//
// The percent-decode hex ladder kept verbatim including the
// VENDOR QUIRK: the lowercase test spans 'a'..'z' (not 'a'..
// 'f' - the -32 fold normalizes ANY lowercase letter to
// uppercase before the digit test; kept byte-identical, the
// Char minus Int arithmetic on the Kotlin Char seat per the
// Char-Int family rules). The multiple-instance dup counter
// (retrieve-then-append-N re-store) rides the sym_(11)
// retrieve + sym_(0) store pair exactly as the vendor; the
// goto nxtarg shift loop folds onto the while cursor;
// cgi_exit folds onto the labeled break (H-016). No raw
// dollar signs in this file (the vendor uses none).

private const val QS_MAX = 2048
private const val QS_MAX1 = 2047

// fun cgiHMisc(svPtr: Msv) - the block body (returns Unit; the
// vendor block result is void-at-best)
fun cgiHMisc(svPtr: Msv) {
    // char _ftmp[STR_MAX];
    val ftmp = CharArray(STR_MAX)
    val t = CharArray(QS_MAX)
    val t1 = CharArray(1024)

    // if (getenv("REMOTE_ADDR")!=NULL) { ... sym_(0,"%RA",t) }
    System.getenv("REMOTE_ADDR")?.let { ra ->
        CstringOps.copyAtS(t, 0, ra.take(QS_MAX1))
        // sym_((int)0,(unsigned char *)"%RA",(unsigned char *)t,svPtr);
        Stubs.sym_(0, toCz("%RA"), t, svPtr)
    }

    // if (getenv("REMOTE_HOST")!=NULL) { ... sym_(0,"%RH",t) }
    System.getenv("REMOTE_HOST")?.let { rh ->
        CstringOps.copyAtS(t, 0, rh.take(QS_MAX1))
        Stubs.sym_(0, toCz("%RH"), t, svPtr)
    }

    // if (getenv("QUERY_STRING")!=NULL) { ... }
    val qs = System.getenv("QUERY_STRING") ?: return
    // char t[QS_MAX],t1[1024]; int i,j,k,m,n,p,dup=0;
    var dup = 0
    CstringOps.copyAtS(t, 0, qs.take(QS_MAX1))

    // for (i=0; t[i]!='&' && t[i]!=0 && i<QS_MAX; i++); /* find end */
    var i = 0
    while (i < QS_MAX && t[i] != '&' && t[i] != NUL) i++
    if (i == QS_MAX) {
        // if (i==QS_MAX) t[0]='\0';
        t[0] = NUL
    }
    // sym_((int)0,(unsigned char *)"%QS",(unsigned char *)t,svPtr);
    Stubs.sym_(0, toCz("%QS"), t, svPtr)
    // if (t[0]=='\0') goto cgi_exit;
    if (t[0] == NUL) return

    // convert plus signs to blanks
    var ix = 0
    while (ix < QS_MAX && t[ix] != NUL) {
        if (t[ix] == '+') t[ix] = ' '
        ix++
    }

    var done = false
    while (!done) {
        // nxtarg: for (i=0; t[i]!='&' && t[i]!=0 && i<QS_MAX; i++);
        i = 0
        while (i < QS_MAX && t[i] != '&' && t[i] != NUL) i++

        // if (t[i]==0) k=0; else k=1;
        val k = t[i] != NUL

        // t[i]=0;
        t[i] = NUL

        // for (j=0; t[j]!='=' && j<QS_MAX; j++);
        // if (j==QS_MAX) goto cgi_exit;
        var j = 0
        while (j < QS_MAX && t[j] != '=') j++
        if (j == QS_MAX) return
        t[j] = NUL

        // copy right hand side to _ftmp (percent-decode walk)
        var n = 0
        var m = j + 1
        while (t[m] != NUL) {
            if (t[m] != '%') {
                ftmp[n] = t[m]
                n++
                m++
                continue
            }
            // first hex digit
            // if (t[m+1]>='a'&&t[m+1]<='z')t[m+1]=t[m+1]-32; (VERBATIM
            // quirk: the 'z' bound, kept - the Char-Int fold)
            if (t[m + 1] >= 'a' && t[m + 1] <= 'z') t[m + 1] = t[m + 1] - 32
            var p: Int
            if (t[m + 1] >= '0' && t[m + 1] <= '9') p = (t[m + 1] - '0') * 16
            else p = (t[m + 1] - 'A' + 10) * 16

            // second hex digit
            if (t[m + 2] >= 'a' && t[m + 2] <= 'z') t[m + 2] = t[m + 2] - 32
            if (t[m + 2] >= '0' && t[m + 2] <= '9') p = p + (t[m + 2] - '0')
            else p = p + (t[m + 2] - 'A' + 10)

            ftmp[n] = p.toChar() // insert value into output string
            n++
            m = m + 3
        }

        ftmp[n] = NUL

        // char * p1 = sym_((int)11,(unsigned char *)t,
        // (unsigned char *)t1,svPtr); /* retrieve */
        var p1 = Stubs.sym_(11, nameAt(t), t1, svPtr)

        // multiple instances of the same variable name have
        // nbrs appended to name
        if (p1 != null) {
            dup++
            // sprintf(t1,"%d",dup); strcat(t,t1);
            CstringOps.copyAtS(t1, 0, dup.toString())
            var q = 0
            while (q < QS_MAX && t[q] != NUL) q++
            var r = 0
            while (r < t1.size && t1[r] != NUL) {
                t[q + r] = t1[r]
                r++
            }
            t[q + r] = NUL
        }

        // p1=sym_((int)0,(unsigned char *)t,
        // (unsigned char *)_ftmp,svPtr); /* store */
        p1 = Stubs.sym_(0, nameAt(t), ftmp, svPtr)

        // not at end yet - shift input string (the goto nxtarg
        // tail)
        if (k) {
            // for (k=i+1; t[k]!=0 && k<QS_MAX; k++) t[k-(i+1)]=t[k];
            var kk = i + 1
            while (kk < QS_MAX && t[kk] != NUL) {
                t[kk - (i + 1)] = t[kk]
                kk++
            }
            t[kk - (i + 1)] = NUL
            // goto nxtarg (the loop continues)
        } else {
            done = true
        }
    }
}

// private helper: the (unsigned char *) name-carrier adapter
// (a fresh NUL-terminated CharArray; the vendor casts kept as
// the comment ledger)
private fun toCz(s: String): CharArray {
    val out = CharArray(STR_MAX)
    CstringOps.strcpyS(out, s)
    return out
}

// private helper: the mid-buffer name slice (t was NUL-
// terminated at j; the name runs to the NUL)
private fun nameAt(t: CharArray): CharArray {
    val len = CstringOps.cstring(t).length
    val out = CharArray(STR_MAX)
    for (q in 0 until len) out[q] = t[q]
    if (len < out.size) out[len] = NUL
    return out
}
