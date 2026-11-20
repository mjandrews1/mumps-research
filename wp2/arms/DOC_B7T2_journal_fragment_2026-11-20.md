# DOC B7 tranche 2 journal fragment, 2026-11-20

Units 196-205 (core stratum) of batch 7, per the committed row
order. The consult ledger rows live in the ARM_WORKING_NOTES
journal section of this same date (not duplicated here per the
C-005/C-008 one-consult rule and the B6 close splice-repair
lesson).

## Renders

- 196 libmpscpp.cpp::global::Fnumber (vendor :7415): the
  TWO-mstring member appended to the unit-72 file (arity resolves
  the family); unit-42 precedent for the unreachable b.c_str null
  branch (H-013, dropped with note); same coded command build and
  offset-1 v1d/bd fcn dispatch as units 72/42.
- 197 include/mumpsc/externs.h::<misc>: declaration-only ledger
  (unit-89 GlobalClassMisc shape); every extern mapped to its
  existing single-owner carrier (CompilerState / Msv / the
  InterpParseMisc top-level ierr1); the struct nmes tag references
  the landed Nmes type; nothing raised.
- 198 interp-sysfunc.cpp::GlobalData (vendor :733): the interpreter
  (dollar)data probe; H-018 resolves the SQLITE arm live (the two
  0x01 probe-root bytes, the strncmp descendant reduction); the
  mixed 0 and '0' terminator stores kept verbatim (H-012).
- 199 libmpscpp.cpp::global::MakeGlobal (vendor :6963): the varargs
  reference builder; Stubs.makeGlobal flips RESOLVED; GlobalRef
  gains the ArgCount/Sref/Sname member seats and ref widens for the
  strcpy(ref,buf) store (Carriers.kt ledger); MakeGlobalState holds
  the function-static buf; new Stubs.count seat (owner
  libmpscpp.cpp:2932); sprintf %ld rides the H-009 Locale.US fold;
  the two commented //strcat fossils kept.
- 200 interp-fcn.cpp::<misc>: the leading-residue ledger chunk
  (unit-19 shape); #define SQLITE live (H-018 authority note),
  SYMDEBUG fossil comment, CSTR/USTR cast-macro notes, the
  Interpret/ScanParse/Eval prototype roster onto their existing
  seats, the __label_lookup/__text_function roster notes; new
  top-level SYMSTORE/SYMRETRIEVE const seats.
- 201 libmpscpp.cpp::Multiply (vendor :2252): the char-star third
  free overload appended to the unit-46/138 file; the walk
  duplicates the unit-138 body verbatim (vendor copy-paste policy);
  CharArray carriers resolve the Kotlin overloads apart.
- 202 interp-sql.cpp::rslt_callback (vendor :888): the SQLITE
  callback row builder; SqlCallbackState raised (rsltSpec :69 +
  static call_back :886, H-004 TU owner); NULL-column empty-out,
  one-column fast path, the caret/quoted-name list surgery kept
  verbatim; err_msgs dead param kept (H-013).
- 203 compiler-parse.c::Extract (vendor :2466): the function-arg
  list emitter; the SSS/TTT pointer-pair squeeze renders as the
  local index walk (H-007 adapt, no holder raised; the OTHER user
  of the pair renders with its own unit); memcheck stub + Emit/
  Extract2/iiitmp seat reuse; the emitted C text kept byte-verbatim
  including the p2493 tag.
- 204 libmpscpp.cpp::Sim1 (vendor :1565): the document-similarity
  double; Stubs.sim1 flips RESOLVED (the unit-120 wrapper binds
  unchanged); the pass-by-value B.ref writes fold onto the local
  bRefLocal carrier (caller never clobbered, observable C value-copy
  semantics); XNEXT walk, get_global stub + tpx checks, internal
  strtod seat.
- 205 libmpscpp.cpp::embed1 (vendor :4151): the double-quote
  escaper twin of unit-51 embed; H-008 fresh String per call, the
  no-quote fast path returns the original String, null-in returns
  the empty result; dead i/j locals kept as comments (H-013).

## Gate and scan

- Whole-arm kotlinc 2.1.20 gate 0 errors / 0 warnings at 196
  files (188 + 8 new; 4 edits: GlobalFnumber.kt, Multiply.kt,
  Stubs.kt, Carriers.kt); the first compile round caught one
  expression-body type mismatch on the makeGlobal seat flip,
  repaired in-session (the only compile iteration).
- H-015 scan clean on all touched files (one block-comment
  close-sequence reword in the unit-201 KDoc; the double-bang
  assertions removed from unit-202 in favor of the name0 backstop
  binding).

## Files pushed

- 8 new: ExternsMisc.kt, SysfuncGlobalData.kt, MakeGlobal.kt,
  InterpFcnMisc.kt, RsltCallback.kt, CompilerParseExtract.kt,
  Sim1.kt, Embed1.kt (all under wp2/arms/DOC/src/main/kotlin/doc
  per the arm layout)
- 4 edited: GlobalFnumber.kt, Multiply.kt, Stubs.kt, Carriers.kt
- wp2/PILOT_TRACKING.md (batch-7 section, rows 196-205 translated)
- wp2/arms/ARM_WORKING_NOTES.md (tranche section + consult rows)
- wp2/logs/DOC_knowledge_events.csv (regenerated per C-005, +20
  event rows; the pre-tranche CSV verified as byte-exact prefix)
- this fragment
