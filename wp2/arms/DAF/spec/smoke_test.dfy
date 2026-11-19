// smoke_test.dfy - toolchain smoke test, run at arm open (2026-11-19).
// Verifies the Dafny 4.11.0 + Z3 install resolves obligations end-to-end.
// Not a corpus unit; no events rows.

method SmokeLinearSearch(a: seq<int>, target: int) returns (i: int)
    ensures i == |a| || (0 <= i < |a| && a[i] == target)
    ensures i < |a| ==> forall k :: 0 <= k < i ==> a[k] != target
{
    i := 0;
    while i < |a|
        invariant 0 <= i <= |a|
        invariant forall k :: 0 <= k < i ==> a[k] != target
        decreases |a| - i
    {
        if a[i] == target { return; }
        i := i + 1;
    }
}
