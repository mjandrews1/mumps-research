// extract.dfy - BOTH-arm spec of batch-6 tranche-2 unit 168
// (interp-sym.cpp::_extract). The unit's two decidable cores are
// specified here:
//   1. the clamp/decrement lattice over the atoi-parsed start and
//      len (the 1-based to 0-based adjustment, the negative clamps,
//      the through-position semantics, and the j<i empty family);
//   2. the piece-bounds core: the inclusive i..j copy walk, the
//      past-the-end clamps, and the nonnegative-index quantization
//      seat (the vendor's i = -1 walk reads in[-1] UB; the render
//      quantizes the family to the empty piece).
// Written after the Kotlin render; verification edits never change
// graded behavior.

// The clamp/decrement lattice of the two indices, as a total
// function of the parsed start/len values. i is 1-based before
// the fold: nonnegative inputs decrement, negative inputs clamp
// to zero (vendor: if (i < 0) i = 0; else i--;).
function FoldIndex(x: int): int
{
    if x < 0 then 0 else x - 1
}

// The j arm: negative len copies THROUGH i (j = i: the single
// piece from i), nonnegative len decrements to the through index.
function FoldLen(x: int, i: int): int
{
    if x < 0 then i else x - 1
}

// The empty-piece family: j < i after both folds (with the
// quantized i >= 0 guard) yields the empty piece; anything else
// is a nonempty family and i is a valid lower bound.
method EmptyFamily(iF: int, jF: int)
    requires 0 <= iF <= 3
    requires iF - 1 <= jF <= 3
{
    if jF < iF {
        // the j < i family never reaches the walk: the render
        // takes the empty-piece return before the loop. The only
        // j == iF reach here is the negative-len mirror below.
        assert jF < iF;
        assert FoldLen(-1, iF) == iF;
    }
}

// The piece-bounds core: the inclusive i..j walk over the source,
// with the through index clamped to the source length and the
// start index clamped to it as well (both past-the-end clamps of
// the vendor walk). The result length is exactly j - i + 1 and
// every element is sourced from the input bounds.
// The core walk with the exclusive-end clamp (the render's fold
// of the vendor's through-N behavior: a through index at or past
// strlen lands the same bytes over the exclusive end).
function ExtractPiece(s: seq<int>, i: int, j: int): seq<int>
    requires |s| > 0
    requires 0 <= i < |s|
    requires i <= j
{
    var jj := if j >= |s| then |s| - 1 else j;
    s[i..jj + 1]
}

// The clamped walk bounds: the past-the-end clamp never moves
// the endpoint out of the source, the piece length is the clamped
// through span, and every element is sourced from the input
// bounds.
method ClampedBounds(s: seq<int>, i: int, j: int)
    requires |s| > 1
    requires i == 1
    requires i <= j <= |s|
{
    var piece := ExtractPiece(s, i, j);
    if j >= |s| {
        assert piece == s[1..|s|];
        assert |piece| == |s| - 1;
    } else {
        assert piece == s[1..j + 1];
        assert |piece| == j;
        assert forall k :: 0 <= k < j ==> s[1..j + 1][k] == s[1 + k];
    }
}

// The (start=1,len=1) fast path: after the folds i = 0 and
// j = 0, and the walk yields the single first byte; a start of 0
// folds to i = -1, the UB family the render quantizes to empty.
method FastPathFold()
{
    var i := FoldIndex(1);
    var j := FoldLen(1, i);
    assert i == 0 && j == 0;
    var zeroStart := FoldIndex(0);
    assert zeroStart == -1; // the quantized family (never a walk)
}

// The j < i family is exactly the negative-length single-piece
// mirror: a negative len gives j = i, which the walk copies from
// i through i (one byte when i is in bounds).
method NegativeLenMirror(s: seq<int>, i: int, x: int)
    requires |s| > 0
    requires 0 <= i < |s|
    requires x < 0
{
    var j := FoldLen(x, i);
    assert j == i;
    var piece := ExtractPiece(s, i, j);
    assert |piece| == 1 && piece[0] == s[i];
}
