// DAF B4 T1 spec: interp-stem.cpp::escan (unit 96), roster rank 15.
// Escape/replace stem walker over a C table of structs (ending, replace,
// offset, replen, next). Modeled against the DAF Kotlin render Escan.kt.
//
// SPEC-ASSUMPTION DIVERGENCE (logged in ARM_WORKING_NOTES DAF B4 T1):
// vendor C can loop forever on adversarial tables (a rule whose replace
// re-matches itself with next=false, or a replen-offset >= 0 cycle). The C
// table used by the interpreter is monotone: every application strictly
// shortens wl. This spec assumes that monotonicity (replen < offset for
// every live rule), so termination provable here is conditional; the C
// artifact's non-termination on adversarial tables is not refuted.
//
// Modeling notes (verbatim semantics kept):
//  - vendor replen is strlen(replace); strmove writes replace plus its
//    NUL, so the NUL lands at index wl' = base + replen, matching
//    wl' == wl + replen - offset exactly as the C arithmetic has it;
//  - the match test keeps the vendor's wl - offset > 3 guard;
//  - the scan stops at a table entry with an empty ending (modeled as
//    the inert default End([], [], 0, 0, false)).

datatype End = End(ending: seq<char>, replace: seq<char>, offset: int, replen: int, next: bool)

const NUL: char := '\0'

ghost predicate noNUL(s: seq<char>)
{
  forall i :: 0 <= i < |s| ==> s[i] != NUL
}

// A live table entry (vendor scans until it meets an entry whose
// ending string is empty; those sentinel entries are inert here).
ghost predicate live(e: End) { |e.ending| > 0 }

ghost predicate tableWellFormed(endings: seq<End>)
{
  forall i :: 0 <= i < |endings| ==> noNUL(endings[i].ending)
    && noNUL(endings[i].replace)
    && (live(endings[i]) ==>
      (endings[i].offset > 0 && endings[i].replen == |endings[i].replace|))
}

// The word is a NUL-terminated C string of current length wl.
ghost predicate terminated(word: seq<char>, wl: int)
{
  0 <= wl < |word| && word[wl] == NUL
}

// Vendor match test: wl - e.offset > 3 and the tail at wl - e.offset
// equals e.ending exactly (strcmp over the NUL-terminated tail).
ghost predicate matches(word: seq<char>, wl: int, e: End)
{
  live(e) && wl - e.offset > 3
    && wl - e.offset + |e.ending| < |word|
    && (forall k :: 0 <= k < |e.ending| ==> word[wl - e.offset + k] == e.ending[k])
    && word[wl - e.offset + |e.ending|] == NUL
}

// The replacement writes e.replace then a NUL at wl - e.offset, and
// the new length is wl + e.replen - e.offset (vendor arithmetic kept).
ghost predicate rewritesTo(word: seq<char>, wl: int, e: End, word': seq<char>, wl': int)
{
  |word'| == |word|
    && live(e) && e.offset > 0 && 0 <= wl < |word|
    && e.replen == |e.replace| && e.replen <= e.offset
    && 4 <= wl - e.offset
    && wl' == wl + e.replen - e.offset
    && 0 <= wl' < |word'|
    && (forall k :: 0 <= k < wl - e.offset ==> word'[k] == word[k])
    && (forall k :: 0 <= k < |e.replace| ==> word'[wl - e.offset + k] == e.replace[k])
    && word'[wl'] == NUL
    && (forall k :: wl' + 1 <= k < |word'| ==> word'[k] == word[k])
}

// Monotone-table assumption (see divergence note above): every live
// rule strictly shortens wl, which is what makes the walker terminate.
ghost predicate monotone(endings: seq<End>)
{
  forall i :: 0 <= i < |endings| && live(endings[i]) ==> endings[i].replen - endings[i].offset < 0
}

// Kleene-ish spec over the whole walk: the fixed point is either a
// word that matches no live rule (a normal stop), or a stop right
// after applying a rule flagged next == true.
ghost method escanSpec(word: seq<char>, wl: int, endings: seq<End>)
  returns (word': seq<char>, wl': int)
  requires forall i :: 0 <= i < |endings| ==> live(endings[i]) || endings[i] == End([], [], 0, 0, false)
  requires tableWellFormed(endings) && monotone(endings)
  requires terminated(word, wl)
  ensures terminated(word', wl')
  ensures 0 <= wl' <= wl
  ensures wl' == wl ==> forall i :: 0 <= i < |endings| ==> !matches(word', wl', endings[i])
  decreases wl
{
  if exists i :: 0 <= i < |endings| && matches(word, wl, endings[i]) {
    var i :| 0 <= i < |endings| && matches(word, wl, endings[i]);
    var e := endings[i];
    var w, len := applyRule(word, wl, e);
    assert terminated(w, len);
    if e.next {
      word', wl' := w, len;
    } else {
      word', wl' := escanSpec(w, len, endings);
    }
  } else {
    word', wl' := word, wl;
  }
}

// One rule application, verified against vendor strmove + wl update.
ghost method applyRule(word: seq<char>, wl: int, e: End) returns (word': seq<char>, wl': int)
  requires live(e)
  requires e.offset > 0
  requires e.replen == |e.replace|
  requires e.replen <= e.offset
  requires terminated(word, wl)
  requires matches(word, wl, e)
  ensures rewritesTo(word, wl, e, word', wl')
  ensures terminated(word', wl')
{
  var base := wl - e.offset;
  wl' := wl + e.replen - e.offset;
  assert 0 <= base && base + |e.replace| + 1 <= wl + 1 <= |word| by {
    assert wl' <= wl;
    assert |e.replace| <= wl - base;
    assert base > 3;
  }
  word' := word[..base] + e.replace + [NUL] + word[base + |e.replace| + 1..];
  assert |word'| == |word|;
  forall k | 0 <= k < base ensures word'[k] == word[k]
  {
    assert word[..base][k] == word[k];
  }
  forall k | 0 <= k < |e.replace| ensures word'[base + k] == e.replace[k]
  {
    assert (word[..base] + e.replace)[base + k] == e.replace[k];
  }
  assert word'[base + |e.replace|] == NUL;
  forall k | base + |e.replace| + 1 <= k < |word'|
    ensures word'[k] == word[k]
  {
    assert word[base + |e.replace| + 1..][k - (base + |e.replace| + 1)] == word[k];
  }
}
