package daf

// R1 stub-completion (2026-11-27, C-020 erratum session, tranche 4):
// compiler-globalarray.c::GlobalArray (59 LOC, roster row 43,
// complex): the compile-time LHS global-array reference expander.
// The ^-name scan copies the 32..127 head into t, sets GBL_flg,
// writes the "tmp%d" iiitmp handle into vd (the String? return
// fold: null leaves the caller vd untouched; the handle is the
// sprintf(vd,"tmp%d",iiitmp) write-back - the call sites in
// SetCode.kt now write the returned value into C.vd, the
// LocalArray String? precedent), emits the strmove name line
// with the coded delimiter tail (\x + the %2x of the 206-or-
// delimiter byte), then loops the subscripts: each 32..127
// segment emits the strxcat index line (the #G#/#S# arms relay
// through Extract2, the raised roster-b1 seat, called exactly
// as the landed Expand* siblings call it), each delimiter byte
// (a NUL becomes 207, in-place on the v1d CharArray carrier)
// emits the strcat coded-comma tail.
// Consults (2026-11-27T00:00:00Z, nominal): BASE-log row 2
// family hit-reuse (fprintf(Out,...) rides DafCompilerState.emit
// with the emitted-C text kept VERBATIM: the tab prefix, the
// M3701/a2926/a3956 comment tags); row 30 hit-adapt (the
// short *GBL_flg out-param rides the IntCellD carrier; the
// render reads the cell back at the SetCode call sites);
// rows 111/68 hit-reuse (extract2Daf the raised roster-b1
// relay; the 206/207 codes seat beside CODED_COMMA_D);
// row 44 hit-reuse (conditional out-param write: vd receives
// the tmp handle only on the ^ arm - the unit-135/ExpandOrder
// String? fold).
// Vendor quirks kept: (1) the h[6] play - the 206-or-byte is
// staged into h[2], then sprintf(h,"%2x",...) overwrites h
// from index 0 and the tail reads h[0]/h[1] - the staging
// store is dead but kept as the modeled hc selection;
// (2) the initial head scan starts at i=2 (skips ^) - kept;
// (3) the iiitmp++/memcheck pair runs on BOTH arms (the ^ arm
// early-returns its own pair after the name-only reference);
// (4) the v1d NUL->207 rewrite mutates the ARGUMENT array
// (C.v1d at the call sites) - the CharArray carrier keeps it
// in-place, disclosed.
// Dafny: EXEMPT (emission-shaping into the Out writer; journal
// row).

fun globalArrayDaf(v1d: CharArray, flag: IntCellD): String? {

    val C = DafCompilerState
    val t = StringBuilder() // unsigned char t[1024]
    var handle: String? = null // unsigned char *vd (String? fold)

    if (v1d[1] == '^') { // Process LHS global array reference

        // for (i = 2; v1d[i] != 0; i++): the 32..127 head scan
        var i = 2
        while (v1d[i] != NUL_D) {
            if (v1d[i].code in 32..127) t.append(v1d[i])
            else break
            i++
        }

        flag.v = 1 // *GBL_flg = 1

        // sprintf(vd,"tmp%d",iiitmp) - the String? write-back
        handle = "tmp" + C.iiitmp

        // if (v1d[i]=='\0') h[2]=206; else h[2]=v1d[i];
        // sprintf(h, "%2x", h[2]) - the dead h[2] staging kept
        val hc = if (v1d[i] == NUL_D) 206 else v1d[i].code
        val h = String.format(java.util.Locale.US, "%2x", hc)

        t.append('\\').append('x').append(h[0]).append(h[1])
        val ts0 = t.toString() // t[j] = 0 folds: emit the pre-NUL cut

        // fprintf(Out, "\tstrmove((UNS) tmp%d, (UNS) \"%s\"); // M3701\n",
        //          iiitmp, t) - verbatim
        C.emit(TAB_GA + "strmove((UNS) tmp" + C.iiitmp + ", (UNS) \"" +
            ts0 + "\"); // M3701\n")

        if (v1d[i] == NUL_D) { // if (v1d[i]=='\0')
            C.iiitmp++
            memcheckDaf(C.iiitmp)
            return handle
        }
        i++

        while (true) { // while (1)

            // while (v1d[i] <= 127 && v1d[i] >= 32) t[j++] = v1d[i++]
            t.setLength(0)
            while (v1d[i].code <= 127 && v1d[i].code >= 32) {
                t.append(v1d[i])
                i++
            }
            t.append(NUL_D) // t[j] = 0

            if (t.length > 1) { // if (strlen(t))

                val ts = t.substring(0, t.length - 1)

                if (ts.startsWith("#G#") || ts.startsWith("#S#")) {
                    // strncmp(t,"#G#",3)==0 || strncmp(t,"#S#",3)==0
                    C.emit(TAB_GA + "catx = strxcat((char *) tmp" +
                        C.iiitmp + ", (char *) " + extract2Daf(ts) +
                        "); // a2926\n")
                } else {
                    C.emit(TAB_GA + "catx = strxcat((char *) tmp" +
                        C.iiitmp + ", (char *) " + ts + "); // b\n")
                }
            }

            // j = 0; if (v1d[i]==0) v1d[i]=207 (in-place carrier)
            if (v1d[i] == NUL_D) v1d[i] = Char(207)

            val h2 = String.format(java.util.Locale.US, "%2x", v1d[i].code)

            t.setLength(0)
            t.append('\\').append('x').append(h2[0]).append(h2[1])
            t.append(NUL_D)

            // fprintf(Out, "\tstrcat((char *) catx, (char *) \"%s\"); //a3956\n",
            //          t) - verbatim
            C.emit(TAB_GA + "strcat((char *) catx, (char *) \"" +
                t.substring(0, t.length - 1) + "\"); //a3956\n")

            i++ // if (v1d[++i] != 0) continue; else break
            if (v1d[i] != NUL_D) continue
            break
        }

    } else flag.v = 0 // else *GBL_flg = 0

    C.iiitmp++
    memcheckDaf(C.iiitmp)
    return handle
}

// the vendor fprintf tab prefix of the emitted lines (the raw tab
// byte of the C string literal - the LocalArray emit precedent)
private const val TAB_GA: String = "\t"
