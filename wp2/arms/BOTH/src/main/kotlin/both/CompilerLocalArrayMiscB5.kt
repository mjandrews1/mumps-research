package both

// Unit 125 (BOTH B5 T1, simple): compiler-localarray.c::<misc> (30 LOC
// residue). Batch_plan ordinal 125: simple, LOC 30, score 5.2. Content:
// the GPL banner plus the include block only (stdio.h, mumpsc/memsize.h,
// mumpsc/compiler-macros.h, mumpsc/compiler-globals-ext.h); no function
// bodies - the file's executable unit is LocalArray (roster unit 21,
// batch 1).
//
// BOTH consults (find_rule before render, events logged):
//   h014 out_of_scope_call - consulted, ADAPTED (hit, not reuse): no
//     call site exists to stub, so the ledger carries the owner name;
//     the owner unit (LocalArray, roster 21) is not yet translated in
//     this arm (cold open at batch 5).
//   No other construct family occurs in the unit.
//
// Render: KDoc-only residue carrier (no runtime code emitted).

internal object CompilerLocalArrayMiscB5 {
    // Residue ledger (compiler-localarray.c, per vendor tree 2026-11-21):
    // - banner: GPL/copyright block (O'Kane), May 12, 2024
    // - include stdio.h
    // - include mumpsc/memsize.h
    // - include mumpsc/compiler-macros.h
    // - include mumpsc/compiler-globals-ext.h
    //     (header residue owner: roster unit 151, batch-5 complex
    //      tranche; renders later in this batch)
    // - file body owner: compiler-localarray.c::LocalArray (roster unit
    //   21, batch 1) - TODO-stub discipline applies at its future call
    //   sites, not here.
}
