package doc

// Unit 88 (B3 tranche 3 / sub-push B): compiler-sys1.c::patrn (batch 3,
// complex stratum; LOC 127, score 39.7). The compiler-side Mumps
// pattern matcher: reads the pattern argument staged by patrn1 in the
// compiler bd scratch (CompilerState.bd, the compiler-globals-ext.h:33
// extern carried by batch-1 unit 26; vendor 2048 byte sizing vs the
// STR_MAX carrier divergence, roster note kept at the carrier), parses
// definite (6) and indefinite (1.6 etc) count ranges into the
// pts/count/high/low tables, then either calls the definite matcher
// once or enumerates the indefinite cross-product with the
// retry/retry1/recount odometer until a match (ierr1 = 1) or
// exhaustion (ierr1 = 0). The string-gather arm handles the paired
// quote (the 210 coded byte rewrites to a double quote in place - the
// bd scratch mutation is preserved, H-007). Quirks kept and noted:
// the parse loop bound strlen(&bd[1]) is re-evaluated every iteration
// in the vendor but the loop never changes bd length (the 210
// rewrite is in-place), so the bound is hoisted once with this
// ledger note; the class-set literals cnpalue/CNPALUE are kept
// byte-verbatim; iptr is assigned then never read (dead local, H-013,
// not declared; ledger here); the substr(&bd[1], tmp, j, i-j) call
// carries the pointer shift the CharArray seat cannot express - the
// seat call below passes bd with the shift noted at the seat contract
// (H-014 pointer-shift note; owner interp substr helper batch-7).
// The goto web linearizes per H-011: retry/retry1 become the
// first-landing flag (the initial landing skips the ct2[stk[1]]++
// which retry performs), the recount ladder is the inner while, the
// k>lad tail test is the continue of the retry loop, and err is the
// shared early-return (ierr1 = 99). cannon is the direct call to the
// landed unit-84 render; atoi reuses the unit render of Find.kt;
// xindex rides the Stubs seat; the definite enumeration calls the
// NEW Stubs.match seat (five-argument shape; owner
// compiler-sys1.c::match, a batch-7 unit - the six-argument
// interp-parse.cpp::match twin has its own same-name overload seat,
// H-015 overload-arity rule). H-006: the short* j out-param renders
// as the ShortArray(1) holder. Arrays keep the vendor 1-based
// indexing with the zero row unused (pts[0] is never touched; the
// H-007 offset-preserving rule). Naming per H-017.

fun patrn(ad: CharArray) {
    val bd = CompilerState.bd
    val tmp = CharArray(10)
    val pts = Array(3) { ShortArray(20) }
    val count = ShortArray(20)
    val ct2 = ShortArray(20)
    val stk = IntArray(20) // H-010: the short table rides Int for
    // Kotlin Int-only array indexing (the values are table indices)
    val high = ShortArray(20)
    val low = ShortArray(20)
    var indef = false
    var lx = 0
    val lad = CstringOps.strlenC(ad)
    val jh = ShortArray(1) // short* j out-param holder (H-006)

    for (i in 0 until 20) {
        high[i] = lad.toShort()
        low[i] = 0
    }

    // strlen(&bd[1]): count from offset 1 to the NUL (hoisted; the
    // vendor re-evaluates per iteration but the loop cannot change
    // the length - ledger note above)
    var bdTop = 0
    while (bd[1 + bdTop] != NUL) bdTop++

    // parse loop (vendor for (i = 1; i <= strlen(&bd[1]); i++))
    var i = 1
    while (i <= bdTop) {
        lx++
        if (bd[i] < '0' || bd[i] > '9') {
            if (bd[i] != '.') i--
            count[lx] = (-1).toShort()
            indef = true
            i++
            if (bd[i] >= '0' && bd[i] <= '9') {
                var j = i
                while (bd[i] >= '0' && bd[i] <= '9') i++
                substr(bd, tmp, j.toShort(), (i - j).toShort())
                cannon(tmp)
                high[lx] = atoi(CstringOps.cstring(tmp)).toShort()
            }
        } else {
            var j = i
            while (bd[i] >= '0' && bd[i] <= '9') i++
            substr(bd, tmp, j.toShort(), (i - j).toShort())
            cannon(tmp)
            count[lx] = atoi(CstringOps.cstring(tmp)).toShort()
            if (bd[i] == '"') {
                low[lx] = count[lx]
                count[lx] = (-1).toShort()
                indef = true
                i++
                if (bd[i] >= '0' && bd[i] <= '9') {
                    j = i
                    while (bd[i] >= '0' && bd[i] <= '9') i++
                    substr(bd, tmp, j.toShort(), (i - j).toShort())
                    cannon(tmp)
                    high[lx] = atoi(CstringOps.cstring(tmp)).toShort()
                }
            }
        }

        if (bd[i] != '"') {
            tmp[0] = bd[i]
            tmp[1] = NUL
            if (Stubs.xindex("cnpalue".toCharArray(), tmp, 1) == 0 &&
                Stubs.xindex("CNPALUE".toCharArray(), tmp, 1) == 0
            ) {
                // goto err
                ierr1 = 99
                return
            }
            pts[1][lx] = i.toShort()
            pts[2][lx] = 1
            i++
            continue
        }

        var j = i

        if (bd[j + 1] == '"') {
            j++
            pts[1][lx] = i.toShort()
            pts[2][lx] = 0
            i = j
            i++
            continue
        }

        // a108: gather through the closing quote
        while (true) {
            j++
            if (bd[j] == NUL) {
                // goto err
                ierr1 = 99
                return
            }
            if (bd[j] == '"') break
            if (bd[j] == 210.toChar()) bd[j] = '"'
            // goto a108
        }

        pts[1][lx] = i.toShort()
        pts[2][lx] = (j - i).toShort()
        i = j
        i++
    }

    if (!indef) {
        Stubs.match(pts, lx, count, ad, jh)
        ierr1 = jh[0].toInt()
        return
    }

    // indefinite enumeration (retry/retry1/recount odometer, H-011)
    var stx = 0
    for (t in 1..lx) {
        if (count[t] < 0) {
            stx++
            stk[stx] = t
        }
    }

    for (t in 1..lx) {
        ct2[t] = if (count[t] >= 0) count[t] else low[t]
    }

    // goto retry1 on first landing (skips the retry increment)
    var firstLanding = true
    while (true) {
        if (firstLanding) {
            firstLanding = false
        } else {
            // retry:
            ct2[stk[1]] = (ct2[stk[1]] + 1).toShort()
        }

        // retry1:
        if (ct2[stk[1]] > lad || ct2[stk[1]] > high[stk[1]]) {
            ct2[stk[1]] = low[stk[1]]
            if (stx == 1) {
                ierr1 = 0
                return
            }
            var m = 2
            ct2[stk[m]] = (ct2[stk[m]] + 1).toShort()

            // recount ladder
            while (true) {
                if (ct2[stk[m]] > lad || ct2[stk[m]] > high[stk[m]]) {
                    if (m >= stx) {
                        ierr1 = 0
                        return
                    }
                    ct2[stk[m]] = low[stk[m]]
                    m++
                    ct2[stk[m]] = (ct2[stk[m]] + 1).toShort()
                    // goto recount
                } else {
                    break
                }
            }
        }

        var k = 0
        for (jj in 1..lx) k += ct2[jj]
        if (k > lad) continue // goto retry

        Stubs.match(pts, lx, ct2, ad, jh)
        ierr1 = jh[0].toInt()
        if (ierr1 == 1) return
        // goto retry
    }
}
