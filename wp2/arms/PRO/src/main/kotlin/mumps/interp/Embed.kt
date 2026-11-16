package mumps.interp

/**
 * PRO-arm translation of interp-sql.cpp::embed (pilot B2, unit 51,
 * provisional under deviation C-003). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 115-142 (units.csv: 19
 * LOC, score 27.3, rank 197).
 *
 * Fixes embedded single quote marks in SQL commands: any ' becomes
 * '' (SQL escaping). This is the h008 example unit - the vendor
 * builds into a static char tmp[STR_MAX] and returns either it or
 * the original pointer.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch     : static char tmp[STR_MAX] -> a fresh
 *    String per call; NULL in returns "" (the empty tmp); the fast
 *    path (no quotes) returns the original object, as in the vendor.
 *  - h015 grammar_collision : the vendor parameter in is a Kotlin
 *    hard keyword; renamed inStr (unit-44 val precedent).
 *  - h017 naming            : embed keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor scans once for a quote, then copies with doubling;
 *    the two-loop shape is kept (scan, then build).
 */

fun embed(inStr: String?): String {
    // vendor: tmp[0]=0; if (in == NULL) return tmp;
    if (inStr == null) return ""

    // vendor: for (p1=in; *p1!=0; p1++) if (*p1=='\'') { k=0; break; }
    //         if (k) return in; // contains no quotes
    if (!inStr.contains('\'')) return inStr

    // vendor: copy with doubling into tmp (h008: fresh String)
    val tmp = StringBuilder()
    for (c in inStr) {
        if (c != '\'') {
            tmp.append(c)
        } else {
            tmp.append('\'')
            tmp.append('\'')
        }
    }
    return tmp.toString()
}
