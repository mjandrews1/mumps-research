# select_study_subset.py — Random Unit Selection for the Mumps II Translation Study

**Version 2** (13 Nov 2026). Version 1 targeted the wrong component of the
O'Kane distribution ("Legacy-Mumps-Interpreter"); v2 targets the confirmed
study subject: the **Mumps II Interpreter-Compiler Library** (in the tarball:
`mumps/Mumps-Language-Processors/Mumps-Interpreter-Compiler-Library/`), i.e.
the Mumps II **compiler (C)** plus the Mumps II **interpreter (C++)**.

## What it does

Implements the sampling protocol of `RESEARCH_DESIGN_C_To_Kotlin.md` (§
Sampling): stratify translation units by complexity, seed-shuffle within
strata, greedily assign units to N balanced Study-Subset batches, and draw the
micro-replication units (3 per arm: BASE, DOC, PRO, DAF, BOTH; per-arm seeded
streams `micro-{ARM}`, so adding an arm later does not perturb existing draws).

Everything random flows from one pre-registered `--seed` (sha256-counter PRNG
with purpose-separated streams): the same source tree + seed reproduces the
assignment bit-for-bit; a different seed yields a genuinely different
assignment. **Commit the outputs (especially `protocol.json`) before the
pilot batch.** Regenerating with the same seed is verification; regenerating
with a different seed is a protocol deviation and must be logged as such.

## v2 changes (why and what)

1. **C++ support.** The interpreter half of the subject is C++
   (`interp-*.cpp`, `libmpscpp.cpp`, headers). Scans `.c/.h/.cpp/.cc/.hpp/.hh`
   and tags each unit `c` or `cpp` (column `lang` in `units.csv`, per-batch
   c/cpp counts in the plan, `lang_split` in the protocol).
2. **Default excludes.** The distribution ships near-duplicate copies of the
   sources in several places; v2 excludes by default:
   - `*.in` — autoconf templates (the plain files are the configure outputs
     that actually build; the `.in` twins differ only in `@substitution@`s);
   - `*/usr/*`, `*/DEBIAN/*` — deb package payloads;
   - `*/autom4te.cache/*` — autoconf cache;
   - `*/Checkout/*` — SVN checkout of demo/test programs (`btest1–8.cpp`,
     `global1/2.cpp`), part of the tarball but not library source.
   Add your own with `--exclude 'GLOB' ...`.
3. **C/C++ function chunking.** Brace counting ignores strings, char literals,
   and comments (multi-line `/*…*/` aware); the definition-head matcher
   handles `Class::method`, `~Destructor`, `&`/`*` qualifiers, templates,
   trailing `const`, and multi-line parameter lists; C/C++ keywords are
   filtered so control statements are never mistaken for functions.
4. **`<misc>` handling.** Per file, lines not claimed by any function
   (globals, macros, tables, plus functions below `--min-loc`) become one
   residual chunk. Because `libmpscpp.cpp`'s residual was >5,000 LOC and
   dominated any batch, oversized residual chunks are split into sequential
   `<misc-N>` chunks of at most `--chunk-loc` (default 600) physical LOC.
5. **protocol.json** now pins: subject label, unit granularity, `min_loc`,
   `chunk_loc`, excludes, arms, batch sizes, `lang_split`, stratum sizes, and
   a SHA-256 (first 16 hex) of every scanned source file.

## Usage

```bash
python3 select_study_subset.py \
  --source mumps/Mumps-Language-Processors/Mumps-Interpreter-Compiler-Library \
  --unit function --batches 10 --seed 20261113 --out study-subset
# options: --unit file | function   (default: function)
#          --min-loc 15             minimum physical LOC per unit
#          --chunk-loc 600          max LOC per residual <misc> chunk
#          --micro 3                micro-replication units per arm
#          --micro-shared           same micro units for all arms
#          --arms BASE DOC PRO DAF BOTH
#          --exclude 'GLOB' ...     extra excludes
```

Outputs: `units.csv` (manifest with metrics, stratum, batch),
`micro_replication.csv` (arm → unit draws), `batch_plan.md` (human-readable
work order), `protocol.json` (pre-registration record).

## Reference run (seed 20261113, subject = Mumps II Interpreter-Compiler Library)

`--unit function --batches 10 --min-loc 15 --chunk-loc 600`:

- **308 units** (96 C, 212 C++), strata 102/102/104 (simple/core/complex).
- 10 batches of 30–31 units each, 1,944–6,431 LOC per batch; every batch mixes
  both languages (c/cpp split per batch ranges 3/24 … 14/17).
- Largest single units: `interp-interp.cpp::Interpret` (1,581 LOC) and
  `libmpscpp.cpp::<misc-N>` chunks (~600 LOC each).
- Micro-replication: 3 units per arm, independent per-arm draws (see
  `EXAMPLE_micro_replication_seed-20261113.csv`).
- Verified: same seed → identical `units.csv` / `micro_replication.csv` /
  `batch_plan.md`; different seed → different assignment; no C/C++ keyword
  false positives among extracted function names.

`--unit file` yields 73 file-level units (49 C, 24 C++).

## Note on the subject's two languages

The compiler is C (~8.7k LOC across 21 `compiler-*.c` files plus headers);
the interpreter is C++ (~26k LOC across `interp-*.cpp` / `libmpscpp.cpp`).
The study therefore covers **C→Kotlin and C++→Kotlin** translation; the
`lang` tag allows per-language subgroup analysis in the metrics phase.
