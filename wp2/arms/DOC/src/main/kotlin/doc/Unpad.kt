package doc

// Unit 289 (B10 tranche 2): interp-sysfunc.cpp::unpad
// (batch 10, core stratum; units.csv row 289: loc 16, cr 0.059,
// ptr 8, ctl 3, score 22.0, rank 175; physically verified
// this session at vendor interp-sysfunc.cpp:947-967, physLOC 16)
// - the gpad numeric-key pad REMOVER (the z~pad decode side of
// the GPADFMT family: the col9 gpad entry is the \u001f marker,
// a forced sign byte and up to 40 decimal digits of the
// 040.20lf pad). The corpus
// body drives the SysfuncMisc fullUnpad token loop through the
// Stubs.unpad seat, which FLIPS to delegate to this render
// this push. The gpad flag carrier is the MSV.gpad field
// (stateVector.h:70, Carriers.kt); GPADWIDTH rides the defines.h
// row-290 render (DefinesH.kt, the include owner). The sign
// byte is REWRITTEN IN PLACE before the copy (H-007 pointer
// idiom over the key buffer: '0' -> '-', anything else -> '+'),
// memcpy(tmp,r,GPADWIDTH) renders as arraycopy of the fixed
// 40-byte window with the NUL terminator at tmp[GPADWIDTH] per
// the vendor literal, and the parse is the atof leading-double
// seat (H-010: the Zfcn atofC / InterpJustify atofJ precedent;
// the vendor ignores the sscanf %lf return - the entry is a
// GPADFMT pad by construction, so a no-parse is the H-013
// vendor UB noted and folded to the 0.0 atof contract).
// sprintf((char*)key,"%lg",x) is the H-009 %g String.format
// Locale.US idiom (the Avg.kt %g precedent), copying over the
// key head (the vendor writes at key[0]; the tail of the old
// pad entry is discarded by the C NUL of the new string at
// key[strlen]). Naming per H-017 (unpad verbatim; no clash).
// Consult: unit 289, entry H-014, outcome hit-adapt,
// timestamp 2026-11-22T06:11:00Z.

private fun atofU(b: CharArray, off: Int): Double {
    // atof over the NUL-terminated window at b[off] (the window
    // carries its own NUL at tmp[GPADWIDTH]; the regex is the
    // leading-double contract, 0.0 on no parse)
    val s = CstringOps.cstringAt(b, off)
    val m = Regex("[+-]?(\\d+\\.?\\d*([eE][+-]?\\d+)?|\\.\\d+([eE][+-]?\\d+)?)").find(s)
        ?: return 0.0
    return m.value.toDoubleOrNull() ?: 0.0
}

fun unpad(key: CharArray, svPtr: Msv) {
    if (svPtr.gpad == 0) return

    if (key[0] == '\u001f') { // a gpad entry found
        val tmp = CharArray(64)
        var r = 1

        // if (*r=='0') *r='-'; else *r='+'; - the in-place
        // sign-byte rewrite (H-007)
        if (key[r] == '0') key[r] = '-' else key[r] = '+'

        // memcpy(tmp,r,GPADWIDTH); tmp[GPADWIDTH]='\0';
        System.arraycopy(key, r, tmp, 0, GPADWIDTH)
        tmp[GPADWIDTH] = NUL
        // sscanf((char*)tmp,"%lf",&x); - the atof seat (header)
        val x = atofU(tmp, 0)

        // sprintf((char*)key,"%lg",x); - H-009 %g at key[0]
        CstringOps.copyAtS(key, 0, java.lang.String.format(java.util.Locale.US, "%g", x))
    }
}
