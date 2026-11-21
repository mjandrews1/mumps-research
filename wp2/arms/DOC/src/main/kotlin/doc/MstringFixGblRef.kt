package doc

// Unit 262 (B9 tranche 2): libmpscpp.cpp::mstring::FixGblRef
// (batch 9, core stratum; units.csv row 263: loc 25, ptr 4,
// ctl 0, score 6.0, rank 106; physLOC 25 physically verified
// this session). The reference-normalization member for the
// bare ^gbl() form: rewrites the empty subscripts to ^gbl("")
// so the downstream Mglobal key walk (H-012 domain) treats the
// empty-subscript form as the unsubscripted fetch entry per
// the vendor header comment ("handles specal case mstring ref
// of the type ^gbl() and converts t to ^gbl(\"\")" - vendor
// typos kept in the ledger note, H-004 V header-comment pulls).
// Rendered as a Mstring extension (unit-79 Eval member
// precedent). SHAPE RULINGS: the vendor mstring b="" return
// carrier folds to the Mstring("") empty construct for the
// three b-arms (j==0, the no-caret arm, the no-open-paren arm)
// while the tmp[i+1]!=')' arm returns *this (the SELF return,
// kept as this, not a copy - the vendor returns the same
// object); the isalpha walk body shares the C-locale
// isalphaXX idiom of the Zfcn family (ASCII letters only); the
// surgery is the two-quote splice tmp[i+1]='\"', tmp[i+2]='\"',
// tmp[i+3]=')', tmp[i+4]=0 over the STR_MAX CharArray tmp (the
// vendor char tmp[STR_MAX] carrier, H-008). Consult H-012
// hit-reuse (the ref-key domain rulings of the Mglobal
// family). The Stubs.fixGblRef seat flips to delegate this
// push. Naming per H-017 (FixGblRef verbatim member).

fun Mstring.FixGblRef(): Mstring {
    val tmp = CharArray(STR_MAX)

    // strcpy(tmp, data.c_str())
    CstringOps.strcpyS(tmp, str)

    // if ((j=strlen(tmp)) == 0) return b;
    var j = CstringOps.strlenC(tmp)
    if (j == 0) return Mstring("")

    // if (tmp[0] != '^') return b;
    if (tmp[0] != '^') return Mstring("")

    var i = 1
    while (i < j) { // avoid ^
        if (isalphaFGR(tmp[i])) {
            i++
            continue
        }
        break
    }

    // if (tmp[i] != '(') return b;
    if (tmp[i] != '(') return Mstring("")

    // if (tmp[i+1] != ')') return (*this);
    if (tmp[i + 1] != ')') {
        return this
    }

    tmp[i + 1] = '"'
    tmp[i + 2] = '"'
    tmp[i + 3] = ')'
    tmp[i + 4] = NUL
    // b = tmp;
    return Mstring(CstringOps.cstring(tmp))
}

// C-locale isalpha for this unit (the Zfcn isalphaC / Parse_
// isAlpha93 family idiom; ASCII letters only)
private fun isalphaFGR(c: Char): Boolean =
    (c in 'A'..'Z') || (c in 'a'..'z')
