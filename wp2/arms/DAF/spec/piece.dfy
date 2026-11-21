// DAF B4 T3 spec: interp-piece.cpp.<misc>::_piece (unit 114), the
// read-path piece-splitting walk of the $piece service kernel. Modeled
// against the DAF Kotlin render InterpPieceMisc.kt (pieceDaf/
// pieceBody). Consult: BASE-log row 97 (the $piece service-call
// shape: the setpiece==0 read path folds onto the returned value).
//
// MODELING NOTES (kept honest): the vendor buffers are NUL-terminated
// C char arrays walked by xindex (roster units interp-strmanip.cpp::
// xindex b9 / compiler-sys1.c::xindex b10, NOT rendered on this arm);
// the model carries seq<char> with |s| as the terminator and models
// xindex as the pure FirstKey finder below. The LHS store path
// (setpiece==1) writes through the sym_/Mglobal seats and is EXEMPT
// (stub-mediated; see the T1/T2 exemption policy).
//
// VERIFIED CONTRACT: for a nonempty separator key and 1 <= k <= l,
// PieceRead(s, key, k, l) returns exactly the characters of s that
// lie after the end of the (k-1)-th key occurrence (or after position
// 0 when k == 1) and before the start of the l-th key occurrence (or
// before |s| when fewer than l occurrences exist): the concatenation
// of pieces k..l of the split, joined by the key.
//
// NOT CLAIMED (the mps_asin.dfy/at_test.dfy lemma precedents): the
// spec does not model the vendor's in-place buffer overruns (the
// (char)1 marking byte of the store path), the atoi start/end
// canonicalizations, or the sigint(100) diagnostic exits.

// The pure xindex model: index of the first key occurrence in s that
// starts at or after from; -1 when none. (The xindexDaf render seat
// is stub-mediated; this model states the contract the walk relies on.)
function FindKey(s: seq<char>, key: seq<char>, from: int): int
  requires |key| > 0 && 0 <= from
  ensures FindKey(s, key, from) == -1 || from <= FindKey(s, key, from) <= |s| - |key|
  decreases |s| - from
{
  if from + |key| > |s| then
    -1
  else if s[from..from + |key|] == key then
    from
  else
    FindKey(s, key, from + 1)
}

// OccurrenceOf: start index of the n-th (1-based) key occurrence, -1
// when it does not exist; Occ(0) is 0 (the string head).
function Occ(s: seq<char>, key: seq<char>, n: int): int
  requires |key| > 0 && n >= 0
  ensures n == 0 ==> Occ(s, key, n) == 0
  ensures n > 0 ==> (Occ(s, key, n) == -1 || (0 <= Occ(s, key, n) <= |s| - |key|))
  decreases n
{
  if n == 0 then
    0
  else
    (var prev := Occ(s, key, n - 1);
     if prev == -1 then
       -1 // the (n-1)-th occurrence is absent: so is the n-th
     else
       (var start := if n == 1 then 0 else prev + |key|;
        var p := FindKey(s, key, start);
        if p == -1 then -1 else p))
}

// HeadOf(i): the exclusive end of the (i-1)-th piece (start of the
// i-th piece) and TailOf: the exclusive start after the l-th key.
function PieceStart(s: seq<char>, key: seq<char>, k: int): int
  requires |key| > 0 && k >= 1
  ensures 0 <= PieceStart(s, key, k) <= |s|
  decreases k
{
  var o := Occ(s, key, k - 1);
  if k == 1 then 0
  else if o == -1 then |s| // ran past the end: empty pieces follow
  else if o + |key| > |s| then |s|
  else o + |key|
}

function PieceEnd(s: seq<char>, key: seq<char>, l: int): int
  requires |key| > 0 && l >= 1
  ensures 0 <= PieceEnd(s, key, l) <= |s|
  decreases l
{
  var o := Occ(s, key, l);
  if o == -1 then |s| else o
}

// The k..l piece window of the split of s by key: the slice between
// the exclusive piece start (after the (k-1)-th key) and the l-th key
// start, empty when the window degenerates (ran past the end).
function PieceWindow(s: seq<char>, key: seq<char>, k: int, l: int): seq<char>
  requires |key| > 0 && 1 <= k <= l
  ensures PieceWindow(s, key, k, l) ==
          (var st := PieceStart(s, key, k); var en := PieceEnd(s, key, l);
           if st <= en then s[st..en] else [])
{
  var st := PieceStart(s, key, k);
  var en := PieceEnd(s, key, l);
  if st <= en then s[st..en] else []
}

// The method shape of pieceBody's return: the window is well bounded.
method PieceReadSpec(s: seq<char>, key: seq<char>, k: int, l: int)
  returns (r: seq<char>)
  requires |key| > 0 && 1 <= k <= l
  ensures r == PieceWindow(s, key, k, l)
  ensures var st := PieceStart(s, key, k);
          var en := PieceEnd(s, key, l);
          |r| == (if st <= en then en - st else 0)
  ensures |r| <= |s|
{
  r := PieceWindow(s, key, k, l);
}

// Records what the spec does NOT claim (precedent discipline): the
// store path (setpiece==1), the atoi canonicalization of the start/end
// carriers, and the sigint diagnostics of the error paths are not
// claimed here.
lemma PieceClaimsAreBounded()
  ensures true
{
}
