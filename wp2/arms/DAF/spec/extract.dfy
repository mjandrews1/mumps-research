// DAF B6 T2 spec: the slice-copy kernel of
// interp-sym.cpp::_extract (unit 168). The render extracts the
// 1-based MUMPS range into the 0-based inclusive slice: the from
// adjust (negative clamps to 0, positive subtracts 1), the to
// adjust (negative becomes the from, positive subtracts 1), the
// inverted-range empty, the i==0&&j==0 first-char special, the
// i>n empty, the j>n clamp, then the copy loop inclusive of both
// endpoints. The spec'd kernel is that ladder plus the copied
// slice; the atoi parses are out of scope (the carrier seats).
//
// VENDOR QUIRKS (divergence ledger, journal row): (1) a start of
// "0" makes the post-adjust i = -1 and the vendor then reads
// in[-1] - the C reads one byte before the buffer; THIS spec
// excludes i0 == 0 from its domain and contracts only the
// well-formed states, keeping the render free of the OOB read (the
// render rides the at() end-guard and gets NUL there); (2) the
// i==0&&j==0 special returns in[0] even when in is empty - the
// spec requires |in| > 0, so the empty-carrier OOB read is out of
// the contracted domain; (3) the to-adjust asymmetry: a negative
// len becomes the ALREADY-adjusted from value (not from-1), kept
// exactly as AdjJ below.

function AdjI(i0: int): (i: int)
  requires i0 != 0
  ensures i >= 0
{
  if i0 < 0 then 0 else i0 - 1
}

function AdjJ(i0: int, j0: int): (j: int)
  requires i0 != 0
{
  if j0 < 0 then AdjI(i0) else j0 - 1
}

// the copy-loop result helper: the EFFECTIVE string the vendor
// loop writes. The C loop is inclusive of BOTH endpoints
// (i <= j), so a j inside the range copies in[i..j]; a j CLAMPED
// to n lets the loop also copy the terminator byte in[n] (a NUL
// into out[k] immediately before the final out[k]=0 - the
// observable string is unchanged), which folds the effective
// slice to the EXCLUSIVE bound min(j+1, n).
function ExtractSlice(inS: seq<char>, i: int, hi: int): (r: seq<char>)
  requires 0 <= i <= hi <= |inS|
  ensures r == inS[i..hi]
{
  inS[i..hi]
}

// the kernel: the ladder over the adjusted pair, the clamped
// inclusive slice the copy loop produces.
function ExtractKern(inS: seq<char>, i0: int, j0: int): (r: seq<char>)
  requires |inS| > 0 && i0 != 0
{
  var i := AdjI(i0);
  var j := AdjJ(i0, j0);
  if j < i then
    []
  else if i == 0 && j == 0 then
    inS[0..1]
  else if i > |inS| then
    []
  else
    var jj := if j > |inS| then |inS| else j;
    var hi := if jj >= |inS| then |inS| else jj + 1;
    ExtractSlice(inS, i, hi)
}

// the adjusted domain well-formedness: with i0 != 0 the from index
// lands at 0 or a non-negative 0-based position.
lemma AdjustDomain(i0: int)
  requires i0 != 0
  ensures AdjI(i0) >= 0
{
}

// the inverted-range contract: an inverted adjusted pair yields
// the empty write.
lemma InvertedRangeContract(inS: seq<char>, i0: int, j0: int)
  requires |inS| > 0 && i0 != 0 && AdjJ(i0, j0) < AdjI(i0)
  ensures ExtractKern(inS, i0, j0) == []
{
}

// the origin contract: the i==0&&j==0 special writes the first
// char of in. Reached for example by i0 < 0 with j0 < 0 (an
// omitted start and length: both adjust to 0).
lemma OriginContract(inS: seq<char>)
  requires |inS| > 0
  ensures ExtractKern(inS, -1, -1) == inS[0..1]
{
}

// the past-end contract: a from past the right end yields the
// empty write (after the origin special is excluded).
lemma PastEndContract(inS: seq<char>, i0: int, j0: int)
  requires |inS| > 0 && i0 != 0
    && AdjJ(i0, j0) >= AdjI(i0)
    && !(AdjI(i0) == 0 && AdjJ(i0, j0) == 0)
    && AdjI(i0) > |inS|
  ensures ExtractKern(inS, i0, j0) == []
{
}

// the clamp identity: inside the range the kernel is exactly the
// clamped inclusive slice in[from .. min(to, n)] (the copy loop
// shape, inclusive of both endpoints).
lemma ClampIdentity(inS: seq<char>, i0: int, j0: int)
  requires |inS| > 0 && i0 != 0
    && AdjJ(i0, j0) >= AdjI(i0)
    && !(AdjI(i0) == 0 && AdjJ(i0, j0) == 0)
    && 0 <= AdjI(i0) <= |inS|
  ensures var jj := if AdjJ(i0, j0) > |inS| then |inS| else AdjJ(i0, j0);
    var hi := if jj >= |inS| then |inS| else jj + 1;
    ExtractKern(inS, i0, j0) == ExtractSlice(inS, AdjI(i0), hi)
    == inS[AdjI(i0)..hi]
{
}

// Sanity carrier: the kernel is defined over the whole well-formed
// domain (the spec's totality check).
method ExtractTotalityCheck(inS: seq<char>, i0: int, j0: int) returns (r: seq<char>)
  requires |inS| > 0 && i0 != 0
{
  r := ExtractKern(inS, i0, j0);
}
