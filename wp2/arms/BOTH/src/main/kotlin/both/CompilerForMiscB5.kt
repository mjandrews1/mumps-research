package both

// Unit 124 (BOTH B5 T1, simple): compiler-for.c::<misc> (30 LOC residue).
// The batch_plan bullet: simple, LOC 30, score 5.2 (batch_plan.md ordinal
// 124). Content: the GPL banner plus the include block only (stdio.h,
// mumpsc/memsize.h, mumpsc/compiler-macros.h, mumpsc/compiler-globals-ext.h);
// there are no function bodies in this residual chunk - the file's
// executable unit is for_code (roster unit 122).
//
// BOTH consults (find_rule before render, events logged):
//   h014 out_of_scope_call - consulted, ADAPTED (hit, not reuse): the
//     rule action is a Stubs.kt TODO seat naming the owner, but a
//     body-less residue has no call site to stub, so the ledger below
//     carries the owner name instead; the owner unit (for_code, 122)
//     is not yet translated in this arm (cold open at batch 5).
//   No other construct family occurs in the unit (no macros, no
//   declarations, no conditionals beyond the banner comment).
//
// Render: KDoc-only residue carrier (no runtime code emitted).

internal object CompilerForMiscB5 {
    // Residue ledger (compiler-for.c, per vendor tree 2026-11-21):
    // - banner: GPL/copyright block (O'Kane, 2003-2024), June 16, 2024
    // - include stdio.h
    // - include mumpsc/memsize.h
    // - include mumpsc/compiler-macros.h
    // - include mumpsc/compiler-globals-ext.h
    //     (header residue owner: roster unit 151, batch-5 complex
    //      tranche; renders later in this batch)
    // - file body owner: compiler-for.c::for_code (roster unit 122,
    //   batch 4) - TODO-stub discipline applies at its future call
    //   sites, not here.
}
