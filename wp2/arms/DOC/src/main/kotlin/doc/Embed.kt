package doc

// Unit 51: interp-sql.cpp::embed (batch 2, core stratum) - the SQL quote
// escaper. The vendor static tmp buffer becomes a fresh String built per
// call (cookbook H-008 carriers rule); the no-quote fast path returns
// the ORIGINAL input string (vendor returns the input pointer - the
// original String, document note). NULL maps to null and returns the
// empty result as the vendor returns the empty static.

fun embed(inp: String?): String {
    if (inp == null) return ""

    var k = 1
    for (c in inp) {
        if (c == '\'') {
            k = 0
            break
        }
    }

    if (k == 1) return inp // contains no quotes

    val sb = StringBuilder()
    for (c in inp) {
        if (c != '\'') {
            sb.append(c)
        } else {
            sb.append('\'')
            sb.append('\'')
        }
    }

    return sb.toString()
}
