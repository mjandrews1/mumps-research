// length_count.dfy - BOTH-arm spec of the piece-count scan shared by
// batch-5 tranche-1 units 129 (libmpscpp.cpp::mstring::Length) and 133
// (libmpscpp.cpp::global::Length). One spec for both twins: the scan
// is the same (count of non-overlapping leftmost needle occurrences,
// plus one, with empty data yielding zero and the retrieved value
// scanned otherwise). Written after the Kotlin renders; verification
// edits never change graded behavior.
//
// The vendor's empty-needle nontermination quirk (ledgered in the
// renders) makes |needle| > 0 a precondition here: the C twins do not
// terminate for an empty retrieved needle value, so there is no total
// behavior to specify on that input.

predicate MatchAt(data: seq<char>, k: int, needle: seq<char>)
    requires |needle| > 0
    requires 0 <= k <= |data| - |needle|
{
    forall m :: 0 <= m < |needle| ==> data[k + m] == needle[m]
}

// first index >= from where needle matches in data, or |data| when
// no viable position remains (the Kotlin indexOf/==-1 idiom of the
// renders).
method Find(data: seq<char>, needle: seq<char>, from: int) returns (idx: int)
    requires |needle| > 0
    requires 0 <= from <= |data|
    ensures 0 <= idx <= |data|
    ensures (from <= idx <= |data|)
    ensures idx < |data| ==> 0 <= idx <= |data| - |needle| && MatchAt(data, idx, needle)
    ensures forall k :: from <= k < idx && k <= |data| - |needle| ==> !MatchAt(data, k, needle)
{
    idx := from;
    if from > |data| - |needle| {
        idx := |data|;
        return;
    }
    while idx <= |data| - |needle|
        invariant from <= idx <= |data| - |needle| + 1
        invariant forall k :: from <= k < idx && k <= |data| - |needle| ==> !MatchAt(data, k, needle)
        decreases |data| - |needle| + 1 - idx
    {
        if MatchAt(data, idx, needle) {
            return;
        }
        idx := idx + 1;
    }
    idx := |data|; // exhausted without a match
}

// The piece count of the twins: i == 0 for empty data; otherwise
// 1 + the non-overlapping leftmost occurrence count. With no
// occurrence anywhere, i == 1 (the vendor's "one piece" comment).
method PieceCount(data: seq<char>, needle: seq<char>) returns (i: int)
    requires |needle| > 0
    ensures |data| == 0 ==> i == 0
    ensures |data| > 0 ==> 1 <= i <= |data| + 1
    ensures |data| > 0 && (forall k :: 0 <= k <= |data| - |needle| ==> !MatchAt(data, k, needle)) ==> i == 1
{
    if |data| == 0 {
        i := 0;
        return;
    }
    i := 1;
    var cursor := 0;
    ghost var w := -1; // last match start (the i > 1 witness)
    if cursor > |data| - |needle| {
        // no viable match position at all: one piece
        assert forallViableNone: forall k :: 0 <= k <= |data| - |needle| ==> !MatchAt(data, k, needle);
        return;
    }
    while cursor <= |data| - |needle|
        invariant 1 <= i <= |data| + 1
        invariant 0 <= cursor <= |data|
        invariant i - 1 <= cursor
        invariant i > 1 <==> w >= 0
        invariant w == -1 || (0 <= w <= |data| - |needle| && w + |needle| <= cursor && MatchAt(data, w, needle))
        decreases |data| - |needle| + 1 - cursor
    {
        var idx := Find(data, needle, cursor);
        if idx == |data| {
            return;
        }
        assert found: MatchAt(data, idx, needle);
        i := i + 1;
        w := idx;
        cursor := idx + |needle|;
        assert advanced: i - 1 <= cursor;
    }
    if i > 1 {
        assert witnessedMatch: MatchAt(data, w, needle);
    }
}
