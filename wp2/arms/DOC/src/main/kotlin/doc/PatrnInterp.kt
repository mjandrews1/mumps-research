package doc

// Unit 92 (B3 tranche 3 / sub-push B): interp-parse.cpp::patrn (batch 3,
// complex stratum; LOC 71, score 28.4). The interpreter-side twin of
// unit 88: the SAME pattern-matching algorithm reading the pattern
// argument staged in the interpreter bd scratch - svPtr.bd (the Msv
// carrier field, stateVector.h:42) instead of CompilerState.bd - and
// calling the SAME-name six-argument match (which hands the bd
// buffer to the matcher explicitly). Same-name distinct vendor units
// resolve by overload arity at the call sites (H-015; the unit-26/30
// patrn1 duo set the precedent): the two-argument shape patrn(ad,
// svPtr: Msv) is owned HERE, the one-argument shape by PatrnSys1.kt.
// Quirks kept and noted (all shared with the sys1 twin, verified
// against the vendor line by line): the vendor loop bound is
// (i <= strlen(&svPtr->bd[0])) - offset ZERO, not the &bd[1] of the
// sys1 twin (the parse therefore walks one NUL tail byte past the
// sys1 cut; kept verbatim with the bd[0]-based bound hoisted once,
// the same length-invariance ledger note); iptr is assigned then
// never read (dead local, H-013, ledger here); the substr pointer
// shift ledger rides the seat; the retry/retry1/recount goto web
// linearizes per H-011 (first-landing flag; recount ladder; k>lad as
// the retry continue); err is the shared early return (ierr1 = 99).
// cannon is the direct call to the landed unit-84 render; atoi
// reuses the unit render of Find.kt; xindex rides the Stubs seat;
// the definite and enumeration matcher calls use the NEW
// six-argument Stubs.match overload seat (owner
// interp-parse.cpp::match, batch-7 unit; the fifth+sixth form passes
// the bd buffer per the vendor). H-006: the short* j out-param is
// the ShortArray(1) holder. Arrays keep the vendor 1-based indexing
// (pts[0] row unused, H-007). Naming per H-017.

fun patrn(ad: CharArray, svPtr: Msv) {
    val bd = svPtr.bd
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

    // strlen(&svPtr->bd[0]): offset-ZERO bound of the vendor twin
    // (hoisted; the loop cannot change the bd length - ledger note)
    var bdTop = 0
    while (bd[bdTop] != NUL) bdTop++

    // parse loop (vendor for (i = 0; i <= strlen(&bd[0]); i++))
    var i = 0
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
                Stubs.substr(bd, tmp, j, i - j)
                cannon(tmp)
                high[lx] = atoi(CstringOps.cstring(tmp)).toShort()
            }
        } else {
            var j = i
            while (bd[i] >= '0' && bd[i] <= '9') i++
            Stubs.substr(bd, tmp, j, i - j)
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
                    Stubs.substr(bd, tmp, j, i - j)
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
        Stubs.match(pts, lx, count, ad, jh, bd)
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

        Stubs.match(pts, lx, ct2, ad, jh, bd)
        ierr1 = jh[0].toInt()
        if (ierr1 == 1) return
        // goto retry
    }
}
