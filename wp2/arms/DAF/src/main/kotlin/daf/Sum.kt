package daf

// Unit 143 (DAF B5 T2, core): libmpscpp.cpp::global::Sum
// (units.csv row 143: loc 21, ptr 1, score 11.9, rank 116, core):
// the class member that sums the sibling subscripts of this global:
// the XNEXT walk over the ref-prefixed keys, the value fetch per
// key, the tpx check with the GLOBAL_NOT_FOUND throw, the strtod
// accumulation. Fourth member of the node-walk family already
// rendered Count (98), Min (99) and Avg (130).
// Consults: BASE-log row 103 hit-reuse (the XNEXT sibling-walk
// skeleton reused from the KiLL/Min family: tmp dual-role carrier,
// loop breaks on Mglobal==0 / prefix mismatch - the.Min/IDF
// precedents), row 92 hit-reuse (the tpx retrieve-then-throw
// pattern: GlobalExceptionCode = GLOBAL_NOT_FOUND then
// MumpsGlobalException; the exception carrier is
// MumpsGlobalExceptionD, const 2 pinned from libmpscpp.h), row 40
// hit-reuse (strtod -> DafCRuntime.strtod over the rslt carrier),
// row 91 family hit-adapt (the Mglobal out-param buffers ttt/tmp
// ride CharArray carriers at DAF; MglobalDaf is the named-owner
// stub seat of the batch-6 roster unit). get_global is raised
// here as a named-owner stub seat (the libmpscpp.cpp TU service,
// kin of the unit-120 wrapper family: 16 call sites in the vendor
// file, none of them a corpus unit).
// The receiver: the C++ member reads this->ref; the render takes
// the MpsGlobalNode whose ref string is the receiver's name (row
// 54 node model).
// Dafny: EXEMPT (global-access walk over stub seats; journal row).
// (GLOBAL_NOT_FOUND_D is the MstringLength.kt const of B5 T1.)


// void get_global(char *, char *) - the libmpscpp.cpp TU value
// fetch service (16 vendor call sites; not a corpus unit;
// named-owner stub per the row 37 prototype discipline).
fun getGlobalDaf(tmp1: CharArray, rslt: CharArray): Unit =
    throw NotImplementedError(
        "get_global: out-of-scope owner (libmpscpp.cpp get_global TU service; not a corpus unit)")

fun globalSumD(node: MpsGlobalNode, svPtr: DafMsv): Double {

    val ttt = CharArray(STR_MAX_DAF)
    val tmp = CharArray(STR_MAX_DAF)
    val tmp1 = CharArray(STR_MAX_DAF)
    val rslt = CharArray(STR_MAX_DAF)
    var tot = 0.0 // double tot=0.0, val;

    val ref = node.ref.toCharArray()
    // strcpy(tmp, ref)
    var i = 0
    while (i < ref.size) {
        tmp[i] = ref[i]
        i++
    }
    tmp[i] = NUL_D
    // i = strlen(tmp)
    i = DafCRuntime.cstring(tmp).length

    // if (tmp[i-1] < 32 && tmp[i-2] < 32) tmp[--i] = 0;
    if (i >= 2 && tmp[i - 1] < ' ' && tmp[i - 2] < ' ') {
        i--
        tmp[i] = NUL_D
    }

    while (true) { // while(1)
        if (MglobalDaf(XNEXT_D, tmp, ttt, svPtr) == 0) break
        // if(Mglobal(XNEXT,tmp,ttt,svPtr)==0) break;

        // if (strncmp(ref,tmp,i)!=0) break;
        var mismatch = false
        if (DafCRuntime.cstring(tmp).length < i) mismatch = true
        else {
            var k = 0
            while (k < i) {
                if (tmp[k] != (if (k < ref.size) ref[k] else NUL_D)) {
                    mismatch = true
                    break
                }
                k++
            }
        }
        if (mismatch) break

        // strcpy(tmp1, tmp)
        var t = 0
        while (at(tmp, t) != NUL_D) {
            tmp1[t] = tmp[t]
            t++
        }
        tmp1[t] = NUL_D
        getGlobalDaf(tmp1, rslt)

        if (svPtr.tpx == 0) { // if (!svPtr->tpx)
            GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
            throw MumpsGlobalExceptionD(
                "global::Sum: symbol/global not found")
        }

        // val=strtod(rslt,NULL); tot=tot+val;
        tot += DafCRuntime.strtod(DafCRuntime.cstring(rslt))
    }

    return tot
}
